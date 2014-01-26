package com.realaction.yunbomobile.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 用户信息工具类
 * 
 * @author liumeng
 */
public class UserUtils {
	public static final int USER_STUDENT = 10;
	public static final int USER_TEACHER = 20;
	public static final int USER_ADMIN = 40;
	
	private static final String SHARED_PREFERENCE = "yunbomobile_user";
	private static final String USERNAME_REF = "username";
	private static final String PASSWORD_REF = "password";
	private static final String USERREALNAME_REF = "userrealname";
	private static final String USERTYPEID_REF = "usertypeid";
	private static final String STUNO_REF = "stuno";
	private static final String EMPNO_REF = "empno";
	private static final String USERAVATAR_REF = "useravatarurl";
	private static final String REMEMBERUSER_REF = "rememberuser";
	private static final int mode = 1;
	private SharedPreferences userinfos;
	private SharedPreferences.Editor editor;

	/**
	 * 构造方法,获取SharedPreference对象和Editor对象以便后续操作
	 * 
	 * @param context
	 *            上下文指针
	 */
	public UserUtils(Context context) {
		userinfos = context.getSharedPreferences(SHARED_PREFERENCE, mode);
		editor = userinfos.edit();
	}

	/**
	 * 将用户信息保存到SharedPreference文件中以便随时取用
	 * 
	 * @param username
	 *            用户名
	 * @param passwd
	 *            密码
	 * @param userrealname
	 *            用户真实姓名
	 * @param usertypeid
	 *            用户类型ID
	 * @param stuno
	 *            学号
	 * @param empno
	 *            工号
	 * @param useravatar
	 *            用户头像url
	 * @param rmbuser
	 *            是否记住用户
	 */
	public void saveUserInfo(String username, String passwd, String userrealname,
			int usertypeid, String stuno, String empno, String useravatar, boolean rmbuser) {
		editor.putString(USERNAME_REF, username);
		editor.putString(PASSWORD_REF, passwd);
		editor.putString(USERREALNAME_REF, userrealname);
		editor.putInt(USERTYPEID_REF, usertypeid);
		editor.putString(STUNO_REF, stuno);
		editor.putString(EMPNO_REF, empno);
		editor.putString(USERAVATAR_REF, useravatar);
		editor.putBoolean(REMEMBERUSER_REF, rmbuser);
		editor.commit();
	}

	/**
	 * 获取保存在SharedPreference文件中的用户名
	 * 
	 * @return 用户名
	 */
	public String getUserName() {
		String username = userinfos.getString(USERNAME_REF, null);
		return username;
	}
	
	/**
	 * 获取保存在SharedPreference文件中的用户密码
	 * 
	 * @return 密码
	 */
	public String getPassword() {
		String passwd = userinfos.getString(PASSWORD_REF, null);
		return passwd;
	}

	/**
	 * 获取保存在SharedPreference文件中的用户真实姓名
	 * 
	 * @return
	 */
	public String getUserRealName() {
		String userrealname = userinfos.getString(USERREALNAME_REF, null);
		return userrealname;
	}

	/**
	 * 获取保存在SharedPreference文件中的用户学号
	 * 
	 * @return 用户学号
	 */
	public String getStuNo() {
		String stuno = userinfos.getString(STUNO_REF, null);
		return stuno;
	}

	/**
	 * 获取保存在SharedPreference文件中的用户工号
	 * 
	 * @return 用户工号
	 */
	public String getEmpNo() {
		String empno = userinfos.getString(EMPNO_REF, null);
		return empno;
	}

	/**
	 * 获取保存在SharedPreference文件中的用户类型ID
	 * 
	 * @return 用户类型ID
	 */
	public int getUserTypeId() {
		int usertypid = userinfos.getInt(USERTYPEID_REF, -1);
		return usertypid;
	}

	/**
	 * 获取保存在SharedPreference文件中的用户头像url
	 * 
	 * @return 用户头像url
	 */
	public String getUserAvatar() {
		String useravatarurl = userinfos.getString(USERAVATAR_REF, null);
		return useravatarurl;
	}

	/**
	 * 是否记住用户
	 * @return true/false
	 */
	public boolean isRmbUser() {
		boolean rmbuser = userinfos.getBoolean(REMEMBERUSER_REF, false);
		return rmbuser;
	}
}
