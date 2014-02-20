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
		dialog.setMessage(context.getResources().getString(R.string.dialog_load));
		dialog.setCanceledOnTouchOutside(false);
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
