package com.realaction.yunbomobile.view.caseviews;

import java.io.File;

import org.vudroid.pdfdroid.codec.PdfContext;

import com.lm.pdfviewer.DecodeService;
import com.lm.pdfviewer.DecodeServiceBase;
import com.lm.pdfviewer.DocumentView;
import com.lm.pdfviewer.models.CurrentPageModel;
import com.lm.pdfviewer.models.DecodingProgressModel;
import com.lm.pdfviewer.models.ZoomModel;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * 案例资源查看Fragment,用来展示案例的具体内容
 * 
 * @author liumeng
 */
public class CaseViewFragment extends Fragment {

	private Context context;
	private String filepath;
	private DecodeService decodeService;
	private DocumentView documentView;
	private CurrentPageModel currentPageModel;

	public CaseViewFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		context = getActivity().getApplicationContext();
		Bundle bundle = this.getArguments();
		if (bundle != null) {
			filepath = bundle.getString("filepath");
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
		decodeService.open(Uri.fromFile(new File(filepath)));
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		FrameLayout frameLayout = new FrameLayout(context);
		frameLayout.addView(documentView);
		documentView.showDocument();
		return frameLayout;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		decodeService.recycle();
		decodeService = null;
	}

	private void initDecodeService() {
		if (decodeService == null) {
			decodeService = createDecodeService();
		}
	}

	private DecodeService createDecodeService() {
		return new DecodeServiceBase(new PdfContext());
	}

}
