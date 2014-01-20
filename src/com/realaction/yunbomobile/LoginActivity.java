package com.realaction.yunbomobile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

/**
 * µÇÂ½½çÃæ
 * 
 * @author liumeng
 */
public class LoginActivity extends Activity {
	private EditText et_name;
	private EditText et_passwd;
	private Button btn_login;
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		context = LoginActivity.this;

		et_name = (EditText) findViewById(R.id.login_username);
		et_passwd = (EditText) findViewById(R.id.login_passwd);
		btn_login = (Button) findViewById(R.id.login_btn);

		btn_login.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, MainActivity.class);
				startActivity(intent);
			}
		});
	}
}
