package com.realaction.yunbomobile.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 用户信息工具类
 * 
 * @author liumeng
 */
public class UserInfo {
	private static final String SHARED_PREFERENCE = "yunbomobile";
	private static final String USERNAME_REF = "username";
	private static final String USERNUM_REF = "usernumber";
	private static final String USERCLASS_REF = "userclass";
	private static final String USERAVATAR_REF = "useravatarurl";
	private static final int mode = 1;
	private SharedPreferences userinfos;
	private SharedPreferences.Editor editor;

	/**
	 * 构造方法,获取SharedPreference对象和Editor对象以便后续操作
	 * 
	 * @param context
	 *            上下文指针
	 */
	public UserInfo(Context context) {
		userinfos = context.getSharedPreferences(SHARED_PREFERENCE, mode);
		editor = userinfos.edit();
	}

	/**
	 * 将用户信息保存到SharedPreference文件中以便随时取用
	 * 
	 * @param username
	 *            姓名
	 * @param usernumber
	 *            学号/工号
	 * @param userclass
	 *            班级
	 */
	public void saveUserInfo(String username, int usernumber, String userclass, String useravatar) {
		editor.putString(USERNAME_REF, username);
		editor.putInt(USERNUM_REF, usernumber);
		editor.putString(USERCLASS_REF, userclass);
		editor.putString(USERAVATAR_REF, useravatar);
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
	 * 获取保存在SharedPreference文件中的用户学号/工号
	 * 
	 * @return 用户学号/工号
	 */
	public int getUserNumber() {
		int usernumber = userinfos.getInt(USERNUM_REF, -1);
		return usernumber;
	}

	/**
	 * 获取保存在SharedPreference文件中的用户班级
	 * 
	 * @return 用户班级
	 */
	public String getUserClass() {
		String userclass = userinfos.getString(USERCLASS_REF, null);
		return userclass;
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

}
