package com.realaction.yunbomobile.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * ���ݿ������
 * 
 * @author liumeng
 */
public class DBOpenHelper extends SQLiteOpenHelper {

	private static final String name = "yunbo.db";
	private static final int version = 1;

	public DBOpenHelper(Context context) {
		super(context, name, null, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO ��������ʷ��¼���ݱ���
		// db.execSQL("CREATE TABLE IF NOT EXISTS favorite (id integer primary key autoincrement, )"caseid,path,count);
		// db.execSQL("CREATE TABLE IF NOT EXISTS history (id integer primary key autoincrement, )"caseid,path,time);
		// ����user��
		db.execSQL(UserTb.CREATE_USER_TB);
		db.execSQL(UserTb.CREATE_USER_TB_UNIQUE_INDEX);
		// �����γ̱�
		db.execSQL(CourseTb.CREATE_COURSE_TB);
		// ����������
		db.execSQL(CaseTb.CREATE_CASE_TB);
		// �����γ̰���ʵ��ָ�����
		db.execSQL(CaseGuideDocTb.CREATE_CASEGUIDEDOC_TB);
		// �����γ̰����𰸱�
		db.execSQL(CaseDocTb.CREATE_CASEDOC_TB);
		// ����������¼��
		// ������ʷ�����¼��
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(UserTb.DROP_USER_TB);
		db.execSQL(CourseTb.DROP_COURSE_TB);
		db.execSQL(CaseTb.DROP_CASE_TB);
		db.execSQL(CaseGuideDocTb.DROP_CASEGUIDEDOC_TB);
		db.execSQL(CaseDocTb.DROP_CASEDOC_TB);
		onCreate(db);
	}

}
