package com.realaction.yunbomobile.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * �û���Ϣ������
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
	 * ���췽��,��ȡSharedPreference�����Editor�����Ա��������
	 * 
	 * @param context
	 *            ������ָ��
	 */
	public UserInfo(Context context) {
		userinfos = context.getSharedPreferences(SHARED_PREFERENCE, mode);
		editor = userinfos.edit();
	}

	/**
	 * ���û���Ϣ���浽SharedPreference�ļ����Ա���ʱȡ��
	 * 
	 * @param username
	 *            ����
	 * @param usernumber
	 *            ѧ��/����
	 * @param userclass
	 *            �༶
	 */
	public void saveUserInfo(String username, int usernumber, String userclass, String useravatar) {
		editor.putString(USERNAME_REF, username);
		editor.putInt(USERNUM_REF, usernumber);
		editor.putString(USERCLASS_REF, userclass);
		editor.putString(USERAVATAR_REF, useravatar);
		editor.commit();
	}

	/**
	 * ��ȡ������SharedPreference�ļ��е��û���
	 * 
	 * @return �û���
	 */
	public String getUserName() {
		String username = userinfos.getString(USERNAME_REF, null);
		return username;
	}

	/**
	 * ��ȡ������SharedPreference�ļ��е��û�ѧ��/����
	 * 
	 * @return �û�ѧ��/����
	 */
	public int getUserNumber() {
		int usernumber = userinfos.getInt(USERNUM_REF, -1);
		return usernumber;
	}

	/**
	 * ��ȡ������SharedPreference�ļ��е��û��༶
	 * 
	 * @return �û��༶
	 */
	public String getUserClass() {
		String userclass = userinfos.getString(USERCLASS_REF, null);
		return userclass;
	}

	/**
	 * ��ȡ������SharedPreference�ļ��е��û�ͷ��url
	 * 
	 * @return �û�ͷ��url
	 */
	public String getUserAvatar() {
		String useravatarurl = userinfos.getString(USERAVATAR_REF, null);
		return useravatarurl;
	}

}
