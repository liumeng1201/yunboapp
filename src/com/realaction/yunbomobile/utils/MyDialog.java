package com.realaction.yunbomobile.utils;

import android.app.ProgressDialog;
import android.content.Context;

import com.realaction.yunbomobile.R;

/**
 * ������ʾ�Ի���
 * 
 * @author liumeng
 */
public class MyDialog {
	private Context context;
	private ProgressDialog dialog;

	public MyDialog(Context context) {
		this.context = context;
	}

	/**
	 * �����Ի���
	 */
	public void create() {
		dialog = new ProgressDialog(context);
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.setCanceledOnTouchOutside(false);
	}
	
	/**
	 * ������ʾ��Ϣ
	 * @param msg ��ʾ��Ϣ
	 */
	public void setMessage(String msg) {
		dialog.setMessage(msg);
	}

	/**
	 * չʾ�Ի���
	 */
	public void show() {
		if (dialog != null) {
			dialog.show();
		}
	}

	/**
	 * ���ضԻ���
	 */
	public void dismiss() {
		if (dialog != null) {
			dialog.dismiss();
		}
	}

}
