package com.realaction.yunbomobile.view.caseviews;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import com.realaction.yunbomobile.R;

public class VideoViewActivity extends Activity {
	private static final String TAG = "VideoViewActivity";
	private Context context;
	private VideoView videoView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_videoview);
		context = VideoViewActivity.this;
		videoView = (VideoView) findViewById(R.id.videoView);

		Intent intent = getIntent();
		String filepath = intent.getStringExtra("target_name");
		displayvideo(filepath);
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
	}

}
