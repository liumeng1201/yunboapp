package com.realaction.yunbomobile;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class UserGuideActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_userguide);
	}

	public void okclick(View v) {
		UserGuideActivity.this.finish();
	}
}
