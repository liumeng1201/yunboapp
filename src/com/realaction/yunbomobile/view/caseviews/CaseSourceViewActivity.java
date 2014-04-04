package com.realaction.yunbomobile.view.caseviews;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.HttpHandler;

import org.vudroid.pdfdroid.codec.PdfContext;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.lm.pdfviewer.DecodeService;
import com.lm.pdfviewer.DecodeServiceBase;
import com.lm.pdfviewer.DocumentView;
import com.lm.pdfviewer.models.CurrentPageModel;
import com.lm.pdfviewer.models.DecodingProgressModel;
import com.lm.pdfviewer.models.ZoomModel;
import com.realaction.yunbomobile.R;
import com.realaction.yunbomobile.db.DBService;
import com.realaction.yunbomobile.moddel.CaseDocItem;
import com.realaction.yunbomobile.moddel.CaseGuideDocItem;
import com.realaction.yunbomobile.utils.AppInfo;
import com.realaction.yunbomobile.utils.MyDialog;

/**
 * ����Դ�鿴��
 * 
 * @author liumeng
 */
public class CaseSourceViewActivity extends Activity {
	private static final String TAG = "AnswerViewActivity";
	private Context context;
	private MyDialog dialog;
	private String download_url;
	private String target_name;
	private long docId;
	private long guideId;
	private long caseId;
	private int opt = 0;
	private DBService dbService;
	private DecodeService decodeService;
	private DocumentView documentView;
	private LinearLayout layout_no_resource;
	private RelativeLayout layout_dl_fail_retry;
	private Button btn_retry_dl;
	private CurrentPageModel currentPageModel;
	private CaseDocItem casedoc;
	private CaseGuideDocItem guidedoc;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = CaseSourceViewActivity.this;
		dialog = new MyDialog(context);
		dialog.create();
		dbService = new DBService(context);
		Intent intent = getIntent();
		opt = intent.getIntExtra("opt", 0);
		caseId = intent.getLongExtra("caseId", -1);
		
		switch (opt) {
		case 1:
			// ָ����
			guideId = intent.getLongExtra("guideId", -1);
			String guideName = intent.getStringExtra("guideName");
			setTitle(guideName);
			String guidePath = intent.getStringExtra("guidePath");
			download_url = AppInfo.base_url + "/" + guidePath;
			target_name = AppInfo.base_dir + "/" + guidePath;
			break;
		case 2:
			// ��
			docId = intent.getLongExtra("docId", -1);
			String docName = intent.getStringExtra("docName");
			setTitle(docName);
			String docPath = intent.getStringExtra("docPath");
			download_url = AppInfo.base_url + "/" + docPath;
			target_name = AppInfo.base_dir + "/" + docPath;
			break;
		default:
			break;
		}
		
		try {
			// ת������·���еĿո�
			String tmp = URLEncoder.encode(download_url, "UTF-8");
			tmp = tmp.replaceAll("\\+", "%20");
			tmp = tmp.replaceAll("%3A", ":").replaceAll("%2F", "/");
			download_url = tmp;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		initDecodeService();
		final ZoomModel zoomModel = new ZoomModel();
		final DecodingProgressModel progressModel = new DecodingProgressModel();
		currentPageModel = new CurrentPageModel();
		documentView = new DocumentView(context, zoomModel, progressModel, currentPageModel);
		zoomModel.addEventListener(documentView);
		documentView.setLayoutParams(new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT));
		decodeService.setContentResolver(context.getContentResolver());
		decodeService.setContainerView(documentView);
		documentView.setDecodeService(decodeService);
		
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
		layout_no_resource = (LinearLayout) inflater.inflate(R.layout.caseview_noresource, null);
		layout_dl_fail_retry = (RelativeLayout) inflater.inflate(R.layout.caseview_retry, null);
		btn_retry_dl = (Button) layout_dl_fail_retry.findViewById(R.id.caseview_dl_retry);
		btn_retry_dl.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				downloadAndShow(download_url, target_name);
			}
		});
		FrameLayout frameLayout = new FrameLayout(context);
		frameLayout.addView(documentView);
		frameLayout.addView(layout_dl_fail_retry);
		frameLayout.addView(layout_no_resource);
		layout_dl_fail_retry.setVisibility(View.GONE);
		layout_no_resource.setVisibility(View.GONE);
		documentView.setVisibility(View.VISIBLE);
		setContentView(frameLayout);
		
		ActionBar actionbar = getActionBar();
		actionbar.setDisplayHomeAsUpEnabled(true);
	}
	
	@Override
	public void onStart() {
		super.onStart();
		downloadAndShow(download_url, target_name);
	}
	
	/**
	 * ���ز���ʾ��Դ
	 * 
	 * @param downloadurl
	 *            ����·��
	 * @param targetname
	 *            ���ش洢·��
	 */
	private void downloadAndShow(final String downloadurl, final String targetname) {
		switch (opt) {
		case 1:
			// ָ����
			guidedoc = dbService.findCaseGuideDocBycaseIdAndguideId(String.valueOf(caseId), String.valueOf(guideId));
			if (AppInfo.network_avabile && (guidedoc != null && (guidedoc.isDownload == 0))) {
				dialog.show();
				if ((new File(targetname)).exists()) {
					(new File(targetname)).delete();
				}
				// ʵ��ָ����δ�����ڱ�����������ã������ظ�ָ���鲢��ʾ
				FinalHttp fh = new FinalHttp();
				HttpHandler handler = fh.download(downloadurl, targetname, true, new AjaxCallBack<File>() {
							@Override
							public void onStart() {
								super.onStart();
								Log.d(TAG, "start download --> " + downloadurl);
							}

							@Override
							public void onFailure(Throwable t, int errorNo, String strMsg) {
								super.onFailure(t, errorNo, strMsg);
								Log.d(TAG, "fail to download");
								dialog.dismiss();
								layout_dl_fail_retry.setVisibility(View.VISIBLE);
								layout_no_resource.setVisibility(View.GONE);
								documentView.setVisibility(View.GONE);
							}

							@Override
							public void onLoading(long count, long current) {
								super.onLoading(count, current);
								Log.d(TAG, "���ؽ���: " + current + "/" + count);
							}

							@Override
							public void onSuccess(File t) {
								super.onSuccess(t);
								dialog.dismiss();
								guidedoc.isDownload = 1;
								guidedoc.localPath = targetname;
								dbService.updateCaseGuideDoc(guidedoc);
								layout_dl_fail_retry.setVisibility(View.GONE);
								layout_no_resource.setVisibility(View.GONE);
								if (targetname.contains(".mp4") || targetname.contains(".3gp")) {
									showVideoActivity(targetname);
								} else {
									documentView.setVisibility(View.VISIBLE);
									decodeService.open(Uri.fromFile(new File(targetname)));
									documentView.showDocument();
								}
								Log.d(TAG, "�������: "
										+ (t == null ? "null" : t.getAbsoluteFile().toString()));
							}
						});
			} else if (guidedoc.isDownload == 1) {
				// ʵ��ָ���������ص�����
				dialog.dismiss();
				layout_dl_fail_retry.setVisibility(View.GONE);
				layout_no_resource.setVisibility(View.GONE);
				if (targetname.contains(".mp4") || targetname.contains(".3gp")) {
					showVideoActivity(targetname);
				} else {
					documentView.setVisibility(View.VISIBLE);
					decodeService.open(Uri.fromFile(new File(targetname)));
					documentView.showDocument();
				}
			} else {
				// ��������δ���棬�򲻿���
				dialog.dismiss();
				layout_dl_fail_retry.setVisibility(View.GONE);
				layout_no_resource.setVisibility(View.VISIBLE);
				documentView.setVisibility(View.GONE);
			}
			break;
		case 2:
			// ��
			casedoc = dbService.findCaseDocBycaseIdAnddocId(String.valueOf(caseId), String.valueOf(docId));
			if (AppInfo.network_avabile && (casedoc != null && (casedoc.isDownload == 0))) {
				dialog.show();
				if ((new File(targetname)).exists()) {
					(new File(targetname)).delete();
				}
				// ʵ��ָ����δ�����ڱ�����������ã������ظ�ָ���鲢��ʾ
				FinalHttp fh = new FinalHttp();
				HttpHandler handler = fh.download(downloadurl, targetname, true, new AjaxCallBack<File>() {
							@Override
							public void onStart() {
								super.onStart();
								Log.d(TAG, "start download --> " + downloadurl);
							}

							@Override
							public void onFailure(Throwable t, int errorNo, String strMsg) {
								super.onFailure(t, errorNo, strMsg);
								Log.d(TAG, "fail to download");
								dialog.dismiss();
								layout_dl_fail_retry.setVisibility(View.VISIBLE);
								layout_no_resource.setVisibility(View.GONE);
								documentView.setVisibility(View.GONE);
							}

							@Override
							public void onLoading(long count, long current) {
								super.onLoading(count, current);
								Log.d(TAG, "���ؽ���: " + current + "/" + count);
							}

							@Override
							public void onSuccess(File t) {
								super.onSuccess(t);
								dialog.dismiss();
								casedoc.isDownload = 1;
								casedoc.localPath = targetname;
								dbService.updateCaseDoc(casedoc);
								layout_dl_fail_retry.setVisibility(View.GONE);
								layout_no_resource.setVisibility(View.GONE);
								if (targetname.contains(".mp4") || targetname.contains(".3gp")) {
									showVideoActivity(targetname);
								} else {
									documentView.setVisibility(View.VISIBLE);
									decodeService.open(Uri.fromFile(new File(targetname)));
									documentView.showDocument();
								}
								Log.d(TAG, "�������: "
										+ (t == null ? "null" : t.getAbsoluteFile().toString()));
							}
						});
			} else if (casedoc.isDownload == 1) {
				// ʵ��ָ���������ص�����
				dialog.dismiss();
				layout_dl_fail_retry.setVisibility(View.GONE);
				layout_no_resource.setVisibility(View.GONE);
				if (targetname.contains(".mp4") || targetname.contains(".3gp")) {
					showVideoActivity(targetname);
				} else {
					documentView.setVisibility(View.VISIBLE);
					decodeService.open(Uri.fromFile(new File(targetname)));
					documentView.showDocument();
				}
			} else {
				// ��������δ���棬�򲻿���
				dialog.dismiss();
				layout_dl_fail_retry.setVisibility(View.GONE);
				layout_no_resource.setVisibility(View.VISIBLE);
				documentView.setVisibility(View.GONE);
			}
			break;
		default:
			break;
		}
		
	}
	
	private void initDecodeService() {
		if (decodeService == null) {
			decodeService = createDecodeService();
		}
	}
	
	private DecodeService createDecodeService() {
		return new DecodeServiceBase(new PdfContext());
	}
	
	private void showVideoActivity(String filepath) {
		Intent intent = new Intent(context, VideoViewActivity.class);
		intent.putExtra("target_name", filepath);
		startActivity(intent);
		CaseSourceViewActivity.this.finish();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			this.finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		decodeService.recycle();
		decodeService = null;
		dbService.close();
	}
}
