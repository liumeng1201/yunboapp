package com.realaction.yunbomobile.view.caseviews;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.HttpHandler;

import org.vudroid.pdfdroid.codec.PdfContext;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
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
import com.realaction.yunbomobile.utils.AppInfo;
import com.realaction.yunbomobile.utils.MyDialog;

/**
 * 答案资源查看类
 * 
 * @author liumeng
 */
public class AnswerViewActivity extends Activity {
	private static final String TAG = "AnswerViewActivity";
	private Context context;
	private MyDialog dialog;
	private String download_url;
	private String target_name;
	private long docId;
	private long caseId;
	private DBService dbService;
	private DecodeService decodeService;
	private DocumentView documentView;
	private LinearLayout layout_no_resource;
	private RelativeLayout layout_dl_fail_retry;
	private Button btn_retry_dl;
	private CurrentPageModel currentPageModel;
	private CaseDocItem casedoc;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = AnswerViewActivity.this;
		dialog = new MyDialog(context);
		dialog.create();
		dbService = new DBService(context);
		Intent intent = getIntent();
		docId = intent.getLongExtra("docId", -1);
		caseId = intent.getLongExtra("caseId", -1);
		String docName = intent.getStringExtra("docName");
		setTitle(docName);
		String docPath = intent.getStringExtra("docPath");
		download_url = AppInfo.base_url + "/" + docPath;
		target_name = AppInfo.base_dir + "/" + docPath;
		try {
			// 转换下载路径中的空格
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
	}
	
	@Override
	public void onStart() {
		super.onStart();
		downloadAndShow(download_url, target_name);
	}
	
	/**
	 * 下载并显示资源
	 * 
	 * @param downloadurl
	 *            下载路径
	 * @param targetname
	 *            本地存储路径
	 */
	private void downloadAndShow(final String downloadurl, final String targetname) {
		casedoc = dbService.findCaseDocBycaseIdAnddocId(String.valueOf(caseId), String.valueOf(docId));
		if (AppInfo.network_avabile && (casedoc != null && (casedoc.isDownload == 0))) {
			dialog.show();
			// 实验指导书未缓存在本地且网络可用，则下载该指导书并显示
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
							Log.d(TAG, "下载进度: " + current + "/" + count);
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
							documentView.setVisibility(View.VISIBLE);
							decodeService.open(Uri.fromFile(new File(targetname)));
							documentView.showDocument();
							Log.d(TAG, "下载完成: "
									+ (t == null ? "null" : t.getAbsoluteFile().toString()));
						}
					});
		} else if (casedoc.isDownload == 1) {
			// 实验指导书已下载到本地
			dialog.dismiss();
			layout_dl_fail_retry.setVisibility(View.GONE);
			layout_no_resource.setVisibility(View.GONE);
			documentView.setVisibility(View.VISIBLE);
			decodeService.open(Uri.fromFile(new File(targetname)));
			documentView.showDocument();
		} else {
			// 无网络且未缓存，则不可用
			dialog.dismiss();
			layout_dl_fail_retry.setVisibility(View.GONE);
			layout_no_resource.setVisibility(View.VISIBLE);
			documentView.setVisibility(View.GONE);
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

	@Override
	public void onDestroy() {
		super.onDestroy();
		decodeService.recycle();
		decodeService = null;
		dbService.close();
	}
}
