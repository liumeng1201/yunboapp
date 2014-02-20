package com.realaction.yunbomobile.view.caseviews;

import java.io.File;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.HttpHandler;

import org.vudroid.pdfdroid.codec.PdfContext;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
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
import com.realaction.yunbomobile.moddel.CaseGuideDocItem;
import com.realaction.yunbomobile.utils.AppInfo;
import com.realaction.yunbomobile.utils.MyDialog;

/**
 * ������Դ�鿴Fragment,����չʾ�����ľ�������
 * 
 * @author liumeng
 */
public class CaseViewFragment extends Fragment {
	private static final String TAG = "CaseViewFragment";

	private Context context;
	private MyDialog dialog;
	private String filepath;
	private String download_url;
	private String target_name;
	private long guideId;
	private long caseId;
	private DBService dbService;
	private DecodeService decodeService;
	private DocumentView documentView;
	private LinearLayout layout_no_resource;
	private RelativeLayout layout_dl_fail_retry;
	private Button btn_retry_dl;
	private CurrentPageModel currentPageModel;
	private CaseGuideDocItem guidedoc;

	public CaseViewFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		context = getActivity().getApplicationContext();
		dialog = new MyDialog(getActivity());
		dialog.create();
		dbService = new DBService(context);
		Bundle bundle = this.getArguments();
		if (bundle != null) {
			filepath = bundle.getString("filepath");
			download_url = bundle.getString("download_url");
			target_name = bundle.getString("target_name");
			guideId = bundle.getLong("guideId");
			caseId = bundle.getLong("caseId");
		}

		initDecodeService();
		final ZoomModel zoomModel = new ZoomModel();
		final DecodingProgressModel progressModel = new DecodingProgressModel();
		currentPageModel = new CurrentPageModel();
		documentView = new DocumentView(context, zoomModel, progressModel,
				currentPageModel);
		zoomModel.addEventListener(documentView);
		documentView.setLayoutParams(new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT));
		decodeService.setContentResolver(context.getContentResolver());
		decodeService.setContainerView(documentView);
		documentView.setDecodeService(decodeService);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
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
		return frameLayout;
	}

	@Override
	public void onStart() {
		super.onStart();
		downloadAndShow(download_url, target_name);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		decodeService.recycle();
		decodeService = null;
		dbService.close();
	}

	private void initDecodeService() {
		if (decodeService == null) {
			decodeService = createDecodeService();
		}
	}

	private DecodeService createDecodeService() {
		return new DecodeServiceBase(new PdfContext());
	}

	/**
	 * ���ز���ʾ��Դ
	 * 
	 * @param downloadurl
	 *            ��Դ����url
	 * @param targetname
	 *            ��Դ���豸�ϵı���·��
	 */
	private void downloadAndShow(final String downloadurl, final String targetname) {
		guidedoc = dbService.findCaseGuideDocBycaseIdAndguideId(String.valueOf(caseId), String.valueOf(guideId));
		if (AppInfo.network_avabile
				&& ((guidedoc == null) || (guidedoc != null && (guidedoc.isDownload == 0)))) {
			dialog.show();
			// ʵ��ָ����δ�����ڱ�����������ã������ظ�ָ���鲢��ʾ
			FinalHttp fh = new FinalHttp();
			HttpHandler handler = fh.download(downloadurl, targetname, true,
					new AjaxCallBack<File>() {
						@Override
						public void onStart() {
							super.onStart();
							Log.d(TAG, "start download --> " + downloadurl);
						}

						@Override
						public void onFailure(Throwable t, int errorNo, String strMsg) {
							super.onFailure(t, errorNo, strMsg);
							Log.d(TAG, "fail to download --> ");
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
							// TODO ���سɹ�֮���޸�guidedoc��isDownload��localPaht��ֵ������
							guidedoc.isDownload = 1;
							guidedoc.localPath = targetname;
							dbService.updateCaseGuideDoc(guidedoc);
							decodeService.open(Uri.fromFile(new File(filepath)));
							documentView.showDocument();
							// decodeService.open(Uri.fromFile(new File(targetname)));
							Log.d(TAG, "�������: "
									+ (t == null ? "null" : t.getAbsoluteFile().toString()));
						}
					});
		} else if (guidedoc.isDownload == 1) {
			// ʵ��ָ���������ص�����
			layout_dl_fail_retry.setVisibility(View.VISIBLE);
			layout_no_resource.setVisibility(View.GONE);
			documentView.setVisibility(View.GONE);
			String filepath = AppInfo.base_dir + guidedoc.localPath;
			decodeService.open(Uri.fromFile(new File(filepath)));
			documentView.showDocument();
		} else {
			// ��������δ���棬�򲻿���
			layout_dl_fail_retry.setVisibility(View.GONE);
			layout_no_resource.setVisibility(View.VISIBLE);
			documentView.setVisibility(View.GONE);
		}
	}
}
