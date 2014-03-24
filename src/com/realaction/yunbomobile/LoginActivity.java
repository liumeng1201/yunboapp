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
 * ��½����
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
					// �û���������Ϊ���ж�
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
							// �������
							datas.add(new BasicNameValuePair("username", username));
							datas.add(new BasicNameValuePair("password", password));
							if (login(url, datas)) {
								// ��½�ɹ�
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
	
	// ��ʼ������
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
	
	// ���ص�½��ʾ�Ի���
	private void hideLoginDialog(Handler handler) {
		handler.post(new Runnable() {
			@Override
			public void run() {
				loginDialog.dismiss();
			}
		});
	}

	/**
	 * ��½����
	 * 
	 * @param url
	 *            ��������½servlet
	 * @param datas
	 *            �û�������
	 * @return �Ƿ��½�ɹ�
	 */
	private boolean login(String url, List<NameValuePair> datas) {
		InputStream is = HttpTool.sendDataByPost(url, datas);
		if (is != null) {
			AppInfo.network_avabile = true;
			String result = HttpTool.convertStreamToString(is);
			String resultarray[] = result.split("\\$");
			if (resultarray.length <= 5) {
				// ��֤ʧ��
				hideLoginDialog(handler);
				HttpTool.showToast(context, handler, getString(R.string.user_error));
				return false;
			} else {
				// ���ݷ��ص����ݻ�ȡ�û�ID
				String userId = getUserIdFromResult(resultarray);
				// ��֤�û����
				if ("null".equals(userId) || "-1".equals(userId)) {
					// ��֤ʧ��
					hideLoginDialog(handler);
					HttpTool.showToast(context, handler, getString(R.string.user_error));
					return false;
				} else {
					// ��֤�ɹ�
					if (needUpdate(resultarray[6])) {
						isUpdate = true;
						updatefile = resultarray[5];
						updatemsg = "�����°汾:" + resultarray[7];
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
			// ������ʴ���
			// �ж�������û������Ƿ�Ϊ֮ǰ�ѵ�½�����û�
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

	// �ӷ��������ص������л�ȡ�û�ID
	private String getUserIdFromResult(String result[]) {
		return result[0];
	}

	// �ӷ��������ص������л�ȡ�û���ʵ����
	private String getRealNameFromResult(String result[]) {
		return result[1];
	}

	// �ӷ��������ص������л�ȡ�û�ͷ���ļ�·��
	private String getProfilePathFromResult(String result[]) {
		// �û�ͷ���Ӧ��url
		String portraits[] = (result[2].replace('\\', '/')).split("/");
		// ת��ΪassetsĿ¼�¶�Ӧ���ļ���
		String portrait = portraits[1] + "/" + portraits[2] + "/" + portraits[3];
		return portrait;
	}

	// �ӷ��������ص������л�ȡ�û����ͱ�ʶ
	private int getUserTypeIdFromResult(String result[]) {
		// userTypeId��Ӧ��string
		return Integer.parseInt(result[3]);
	}
	
	// �ӷ��������ص������л�ȡ�û�ѧ��/����
	private String getUserNoFromResult(String result[]) {
		return result[4];
	}
	
	/**
	 * �Ƿ���Ҫ����
	 * 
	 * @param versioncode
	 *            �ӷ�������ȡ�Ŀͻ��˰汾��
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
