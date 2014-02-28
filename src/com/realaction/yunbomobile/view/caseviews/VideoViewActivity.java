package com.realaction.yunbomobile.view.caseviews;

import java.io.File;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.HttpHandler;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.realaction.yunbomobile.R;
import com.realaction.yunbomobile.db.DBService;
import com.realaction.yunbomobile.moddel.CaseGuideDocItem;
import com.realaction.yunbomobile.utils.AppInfo;
import com.realaction.yunbomobile.utils.MyDialog;

public class VideoViewActivity extends Activity {
	private static final String TAG = "VideoViewActivity";
	private Context context;
	private VideoView videoView;
	private TextView vv_no_resource;
	private RelativeLayout vv_dl_fail_retry;
	private Button btn_dl_fail_retry;
	private DBService dbService;
	private long guideId;
	private long caseId;
	private CaseGuideDocItem guidedoc;
	private MyDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_videoview);
		context = VideoViewActivity.this;
		videoView = (VideoView) findViewById(R.id.videoView);
		vv_no_resource = (TextView) findViewById(R.id.vv_no_resouce);
		vv_dl_fail_retry = (RelativeLayout) findViewById(R.id.vv_dl_fail_retry);
		btn_dl_fail_retry = (Button) findViewById(R.id.vv_dl_retry);

		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		final String download_url = bundle.getString("download_url");
		final String filepath = bundle.getString("target_name");
		guideId = bundle.getLong("guideId");
		caseId = bundle.getLong("caseId");
		
		downAndShow(download_url, filepath);
		btn_dl_fail_retry.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				downAndShow(download_url, filepath);
			}
		});
	}
	
	private void downAndShow(final String url, final String filepath) {
		guidedoc = dbService.findCaseGuideDocBycaseIdAndguideId(String.valueOf(caseId), String.valueOf(guideId));
		if (AppInfo.network_avabile
				&& ((guidedoc == null) || (guidedoc != null && (guidedoc.isDownload == 0)))) {
			dialog.show();
			File file = new File(filepath);
			if (!(file.getParentFile().exists())) {
				// 当文件目录不存在时则创建
				file.getParentFile().mkdirs();
			}
			if (!file.exists()) {
				// 视频未缓存在本地且网络可用，则下载该视频并显示
				FinalHttp fh = new FinalHttp();
				HttpHandler handler = fh.download(url, filepath, true, new AjaxCallBack<File>() {
							@Override
							public void onStart() {
								super.onStart();
								Log.d(TAG, "start download --> " + url);
							}

							@Override
							public void onFailure(Throwable t, int errorNo, String strMsg) {
								super.onFailure(t, errorNo, strMsg);
								Log.d(TAG, "fail to download");
								dialog.dismiss();
								vv_dl_fail_retry.setVisibility(View.VISIBLE);
								vv_no_resource.setVisibility(View.GONE);
								videoView.setVisibility(View.GONE);
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
								guidedoc.isDownload = 1;
								guidedoc.localPath = filepath;
								dbService.updateCaseGuideDoc(guidedoc);
								vv_dl_fail_retry.setVisibility(View.GONE);
								vv_no_resource.setVisibility(View.GONE);
								videoView.setVisibility(View.VISIBLE);
								displayvideo(filepath);
								Log.d(TAG, "下载完成: " + (t == null ? "null" : t.getAbsoluteFile().toString()));
							}
						});
			} else {
				// 当文件存在是则直接显示
				dialog.dismiss();
				vv_dl_fail_retry.setVisibility(View.GONE);
				vv_no_resource.setVisibility(View.GONE);
				videoView.setVisibility(View.VISIBLE);
				displayvideo(filepath);
			}
		} else if (guidedoc.isDownload == 1) {
			// 视频已下载到本地
			dialog.dismiss();
			vv_dl_fail_retry.setVisibility(View.GONE);
			vv_no_resource.setVisibility(View.GONE);
			videoView.setVisibility(View.VISIBLE);
			displayvideo(filepath);
		} else {
			// 无网络且未缓存，则不可用
			dialog.dismiss();
			vv_dl_fail_retry.setVisibility(View.GONE);
			vv_no_resource.setVisibility(View.VISIBLE);
			videoView.setVisibility(View.GONE);
		}
	}
	
	private void displayvideo(String filepath) {
		videoView.setVideoPath(filepath);
		videoView.setMediaController(new MediaController(context));
		videoView.setKeepScreenOn(true);
		videoView.setOnCompletionListener(new OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer mp) {
				VideoViewActivity.this.finish();
			}
		});
		videoView.requestFocus();
	}

	@Override
	protected void onResume() {
		super.onResume();
		videoView.start();
	}

	@Override
	protected void onPause() {
		super.onPause();
		videoView.pause();
	}

	@Override
	protected void onStop() {
		super.onStop();
		videoView.pause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		videoView.pause();
		videoView.stopPlayback();
		dbService.close();
	}

}
