package com.realaction.yunbomobile.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * �û���Ϣ������
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
	 * ���췽��,��ȡSharedPreference�����Editor�����Ա��������
	 * 
	 * @param context
	 *            ������ָ��
	 */
	public UserUtils(Context context) {
		userinfos = context.getSharedPreferences(SHARED_PREFERENCE, mode);
		editor = userinfos.edit();
	}

	/**
	 * ���û���Ϣ���浽SharedPreference�ļ����Ա���ʱȡ��
	 * 
	 * @param username
	 *            �û���
	 * @param passwd
	 *            ����
	 * @param userrealname
	 *            �û���ʵ����
	 * @param usertypeid
	 *            �û�����ID
	 * @param stuno
	 *            ѧ��
	 * @param empno
	 *            ����
	 * @param useravatar
	 *            �û�ͷ��url
	 * @param rmbuser
	 *            �Ƿ��ס�û�
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
	 * ��ȡ������SharedPreference�ļ��е��û���
	 * 
	 * @return �û���
	 */
	public String getUserName() {
		String username = userinfos.getString(USERNAME_REF, null);
		return username;
	}
	
	/**
	 * ��ȡ������SharedPreference�ļ��е��û�����
	 * 
	 * @return ����
	 */
	public String getPassword() {
		String passwd = userinfos.getString(PASSWORD_REF, null);
		return passwd;
	}

	/**
	 * ��ȡ������SharedPreference�ļ��е��û���ʵ����
	 * 
	 * @return
	 */
	public String getUserRealName() {
		String userrealname = userinfos.getString(USERREALNAME_REF, null);
		return userrealname;
	}

	/**
	 * ��ȡ������SharedPreference�ļ��е��û�ѧ��
	 * 
	 * @return �û�ѧ��
	 */
	public String getStuNo() {
		String stuno = userinfos.getString(STUNO_REF, null);
		return stuno;
	}

	/**
	 * ��ȡ������SharedPreference�ļ��е��û�����
	 * 
	 * @return �û�����
	 */
	public String getEmpNo() {
		String empno = userinfos.getString(EMPNO_REF, null);
		return empno;
	}

	/**
	 * ��ȡ������SharedPreference�ļ��е��û�����ID
	 * 
	 * @return �û�����ID
	 */
	public int getUserTypeId() {
		int usertypid = userinfos.getInt(USERTYPEID_REF, -1);
		return usertypid;
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

	/**
	 * �Ƿ��ס�û�
	 * @return true/false
	 */
	public boolean isRmbUser() {
		boolean rmbuser = userinfos.getBoolean(REMEMBERUSER_REF, false);
		return rmbuser;
	}
}
