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
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.realaction.yunbomobile.db.DBService;
import com.realaction.yunbomobile.moddel.User;
import com.realaction.yunbomobile.utils.AppInfo;
import com.realaction.yunbomobile.utils.HttpTool;
import com.realaction.yunbomobile.utils.UserUtils;

/**
 * 登陆界面
 * 
 * @author liumeng
 */
public class LoginActivity extends Activity {
	private Context context;
	private Handler handler;
	private EditText et_name;
	private EditText et_passwd;
	private CheckBox cb_rmbuser;
	private Button btn_login;
	private String url = AppInfo.base_url + "/formobile/formobileLogin.action";
	private ProgressDialog loginDialog;
	private UserUtils uu;
	private DBService dbService;
	private boolean isUpdate = false;
	private String updatemsg;
	private String updatefile;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		init();
		
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
								if (isUpdate) {
									intent.putExtra("isupdate", isUpdate);
									intent.putExtra("updatefile", updatefile);
									intent.putExtra("updatemsg", updatemsg);
								}
								startActivity(intent);
								LoginActivity.this.finish();
							}
						}
					}.start();
				}
			}
		});
	}
	
	// 初始化工作
	private void init() {
		context = LoginActivity.this;
		uu = new UserUtils(context);
		dbService = new DBService(context);
		handler = new Handler();
		loginDialog = new ProgressDialog(context);
		loginDialog.setMessage(getString(R.string.login_wait));

		et_name = (EditText) findViewById(R.id.login_username);
		et_passwd = (EditText) findViewById(R.id.login_passwd);
		btn_login = (Button) findViewById(R.id.login_btn);
		cb_rmbuser = (CheckBox) findViewById(R.id.login_rememberuser);
		
		if (uu.isRmbUser()) {
			String username = uu.getUserName();
			String passwd = uu.getPassword();
			boolean isrmbuser = uu.isRmbUser();
			et_name.setText(username);
			et_passwd.setText(passwd);
			cb_rmbuser.setChecked(isrmbuser);
		}
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
			AppInfo.network_avabile = true;
			String result = HttpTool.convertStreamToString(is);
			String resultarray[] = result.split("\\$");
			if (resultarray.length <= 5) {
				// 验证失败
				hideLoginDialog(handler);
				HttpTool.showToast(context, handler, getString(R.string.user_error));
				return false;
			} else {
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
					if (needUpdate(resultarray[6])) {
						isUpdate = true;
						updatefile = resultarray[5];
						updatemsg = "发现新版本:" + resultarray[7];
					}
					User user = new User();
					user.userId = Long.parseLong(userId);
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
					uu.saveUserInfoToPref(user.userName, user.password,
							user.realName, user.userTypeId, user.stuNo,
							user.empNo, user.profileUrl, cb_rmbuser.isChecked());
					dbService.insertUserTb(user);
					return true;
				}
			}
		} else {
			// 网络访问错误
			// 判断输入的用户数据是否为之前已登陆过的用户
			AppInfo.network_avabile = false;
			SharedPreferences apppre = context.getSharedPreferences(getPackageName() + "_preferences", 1);
			boolean offline_mode = apppre.getBoolean("pref_setting_offline", true);
			User user = dbService.findUserByuserName(et_name.getText().toString());
			if (offline_mode && user != null && user.password != null && (user.password).equals(et_passwd.getText().toString())) {
				uu.saveUserInfoToPref(user.userName, user.password,
						user.realName, user.userTypeId, user.stuNo, user.empNo,
						user.profileUrl, cb_rmbuser.isChecked());
				hideLoginDialog(handler);
				HttpTool.showToast(context, handler, getString(R.string.login_offline_mode));
				return true;
			} else {
				hideLoginDialog(handler);
				HttpTool.showToast(context, handler, getString(R.string.net_error));
				return false;
			}
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
	
	/**
	 * 是否需要升级
	 * 
	 * @param versioncode
	 *            从服务器获取的客户端版本号
	 * @return
	 */
	private boolean needUpdate(String versioncode) {
		try {
			int verCode = context.getPackageManager().getPackageInfo("com.realaction.yunbomobile", 0).versionCode;
			if (verCode < Integer.parseInt(versioncode)) {
				return true;
			}
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		dbService.close();
	}
}
