package com.realaction.yunbomobile.utils;

import java.io.File;

import android.text.TextUtils;

/**
 * �ļ�������
 * 
 * @author liumeng
 */
public class FileUtils {
	/**
	 * ��ȡ�ļ����ļ��д�С
	 * 
	 * @param filePath
	 *            �ļ�·��
	 * @return �ļ��д�С,��λB
	 */
	public static long getFolderSize(File filePath) {
		long size = 0;
		File[] fileList = filePath.listFiles();
		if (fileList != null) {
			for (int i = 0; i < fileList.length; i++) {
				if (fileList[i].isDirectory()) {
					size = size + getFolderSize(fileList[i]);
				} else {
					size = size + fileList[i].length();
				}
			}
			return size;
		} else {
			return 0;
		}
	}

	/**
	 * ɾ���ļ����ļ���
	 * 
	 * @param filePath
	 *            �ļ�·��
	 * @param delFolder
	 *            �Ƿ�ɾ���ļ��б�ʶ
	 */
	public static void delFileAndFolder(String filePath, boolean delFolder) {
		if (!TextUtils.isEmpty(filePath)) {
			File file = new File(filePath);
			if (file.isDirectory()) {
				// ����Ŀ¼
				File files[] = file.listFiles();
				for (int i = 0; i < files.length; i++) {
					delFileAndFolder(files[i].getAbsolutePath(), true);
				}
			}
			if (delFolder) {
				if (!file.isDirectory()) {
					// ������ļ���ɾ��
					file.delete();
				} else {
					// Ŀ¼
					if (file.listFiles().length == 0) {
						// Ŀ¼��û���ļ�����Ŀ¼��ɾ��
						file.delete();
					}
				}
			}
		}
	}
}
