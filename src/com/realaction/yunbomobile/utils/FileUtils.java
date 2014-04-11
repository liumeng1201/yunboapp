package com.realaction.yunbomobile.utils;

import java.io.File;

import android.text.TextUtils;

/**
 * 文件工具类
 * 
 * @author liumeng
 */
public class FileUtils {
	/**
	 * 获取文件或文件夹大小
	 * 
	 * @param filePath
	 *            文件路径
	 * @return 文件夹大小,单位B
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
	 * 删除文件或文件夹
	 * 
	 * @param filePath
	 *            文件路径
	 * @param delFolder
	 *            是否删除文件夹标识
	 */
	public static void delFileAndFolder(String filePath, boolean delFolder) {
		if (!TextUtils.isEmpty(filePath)) {
			File file = new File(filePath);
			if (file.isDirectory()) {
				// 处理目录
				File files[] = file.listFiles();
				for (int i = 0; i < files.length; i++) {
					delFileAndFolder(files[i].getAbsolutePath(), true);
				}
			}
			if (delFolder) {
				if (!file.isDirectory()) {
					// 如果是文件，删除
					file.delete();
				} else {
					// 目录
					if (file.listFiles().length == 0) {
						// 目录下没有文件或者目录，删除
						file.delete();
					}
				}
			}
		}
	}
}
