package com.realaction.yunbomobile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.realaction.yunbomobile.moddel.User;
import com.realaction.yunbomobile.utils.HttpTool;
import com.realaction.yunbomobile.utils.UserUtils;

/**
 * 登陆界面
 * 
 * @author liumeng
 */
public class LoginActivity extends Activity {
	private String TAG = "lm";
	private Context context;
	private Handler handler;
	private EditText et_name;
	private EditText et_passwd;
	private Button btn_login;
	private String url = "http://192.168.2.231:8080/formobile/formobileLogin.action";
	private ProgressDialog loginDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		context = LoginActivity.this;
		handler = new Handler();
		loginDialog = new ProgressDialog(context);
		loginDialog.setMessage(getString(R.string.login_wait));

		et_name = (EditText) findViewById(R.id.login_username);
		et_passwd = (EditText) findViewById(R.id.login_passwd);
		btn_login = (Button) findViewById(R.id.login_btn);

		btn_login.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final String username = et_name.getText().toString();
				final String password = et_passwd.getText().toString();
				if (username.equals("") || password.equals("")) {
					// 用户名或密码为空判断
					AlertDialog.Builder dialog = new Builder(context);
					dialog.setTitle(R.string.notice);
					dialog.setMessage(R.string.namepwnull);
					dialog.setNegativeButton(R.string.ok,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									dialog.dismiss();
								}
							});
					dialog.create();
					dialog.show();
				} else {
					loginDialog.show();
					new Thread() {
						@Override
						public void run() {
							super.run();
							List<NameValuePair> datas = new ArrayList<NameValuePair>();
							// 添加数据
							datas.add(new BasicNameValuePair("username", username));
							datas.add(new BasicNameValuePair("password", password));
							if (login(url, datas)) {
								// 登陆成功
								hideLoginDialog(handler);
								Intent intent = new Intent(context, MainActivity.class);
								startActivity(intent);
							}
						}
					}.start();
				}
			}
		});
	}
	
	// 隐藏登陆提示对话框
	private void hideLoginDialog(Handler handler) {
		handler.post(new Runnable() {
			@Override
			public void run() {
				loginDialog.dismiss();
			}
		});
	}

	/**
	 * 登陆操作
	 * 
	 * @param url
	 *            服务器登陆servlet
	 * @param datas
	 *            用户名密码
	 * @return 是否登陆成功
	 */
	private boolean login(String url, List<NameValuePair> datas) {
		InputStream is = HttpTool.sendDataByPost(url, datas);
		if (is != null) {
			String result = HttpTool.convertStreamToString(is);
			String resultarray[] = result.split("\\$");
			Log.d(TAG, result);
			// 根据返回的数据获取用户ID
			String userId = getUserIdFromResult(resultarray);
			// 验证用户身份
			if ("null".equals(userId) || "-1".equals(userId)) {
				// 验证失败
				hideLoginDialog(handler);
				HttpTool.showToast(context, handler, getString(R.string.user_error));
				return false;
			} else {
				// 验证成功
				User user = new User();
				user.userName = et_name.getText().toString();
				user.password = et_passwd.getText().toString();
				user.realName = getRealNameFromResult(resultarray);
				user.userTypeId = getUserTypeIdFromResult(resultarray);
				user.profileUrl = getProfilePathFromResult(resultarray);
				if (user.userTypeId == UserUtils.USER_STUDENT) {
					user.stuNo = getUserNoFromResult(resultarray);
				} else if (user.userTypeId == UserUtils.USER_TEACHER) {
					user.empNo = getUserNoFromResult(resultarray);
				}
				Log.d(TAG, "userName = " + user.userName + " , password = "
						+ user.password + " , realName = " + user.realName
						+ " , userTypeId = " + user.userTypeId
						+ " , profileUrl = " + user.profileUrl + " , stuNo = "
						+ user.stuNo + " , empNo = " + user.empNo);
				UserUtils uu = new UserUtils(context);
				uu.saveUserInfo(user.userName, user.realName, user.userTypeId, user.stuNo, user.empNo, user.profileUrl);
				return true;
			}
		} else {
			// 网络访问错误
			hideLoginDialog(handler);
			HttpTool.showToast(context, handler, getString(R.string.net_error));
			return false;
		}
	}

	// 从服务器返回的数据中获取用户ID
	private String getUserIdFromResult(String result[]) {
		return result[0];
	}

	// 从服务器返回的数据中获取用户真实名字
	private String getRealNameFromResult(String result[]) {
		return result[1];
	}

	// 从服务器返回的数据中获取用户头像文件路径
	private String getProfilePathFromResult(String result[]) {
		// 用户头像对应的url
		String portraits[] = (result[2].replace('\\', '/')).split("/");
		// 转换为assets目录下对应的文件名
		String portrait = portraits[1] + "/" + portraits[2] + "/" + portraits[3];
		return portrait;
	}

	// 从服务器返回的数据中获取用户类型标识
	private int getUserTypeIdFromResult(String result[]) {
		// userTypeId对应的string
		return Integer.parseInt(result[3]);
	}
	
	// 从服务器返回的数据中获取用户学号/工号
	private String getUserNoFromResult(String result[]) {
		return result[4];
	}
}
