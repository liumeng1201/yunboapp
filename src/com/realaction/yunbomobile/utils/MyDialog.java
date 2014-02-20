package com.realaction.yunbomobile.utils;

import android.app.ProgressDialog;
import android.content.Context;

import com.realaction.yunbomobile.R;

/**
 * 载入提示对话框
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
	 * 创建对话框
	 */
	public void create() {
		dialog = new ProgressDialog(context);
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.setMessage(context.getResources().getString(R.string.dialog_load));
		dialog.setCanceledOnTouchOutside(false);
	}

	/**
	 * 展示对话框
	 */
	public void show() {
		if (dialog != null) {
			dialog.show();
		}
	}

	/**
	 * 隐藏对话框
	 */
	public void dismiss() {
		if (dialog != null) {
			dialog.dismiss();
		}
	}

}
