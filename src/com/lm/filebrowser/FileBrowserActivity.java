package com.lm.filebrowser;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.realaction.yunbomobile.R;
import com.realaction.yunbomobile.db.DBService;
import com.realaction.yunbomobile.moddel.CaseDocItem;
import com.realaction.yunbomobile.moddel.CaseGuideDocItem;
import com.realaction.yunbomobile.moddel.CaseItem;
import com.realaction.yunbomobile.utils.AppInfo;
import com.realaction.yunbomobile.view.caseviews.AnswerViewActivity;

/**
 * �ļ��������,����ʵ�ִ���Դ�Ĳ鿴
 * 
 * @author liumeng
 */
public class FileBrowserActivity extends ListActivity {
	private String mRoot;
	private TextView mTextViewLocation;
	private File[] mFiles;
	private Context context;
	private DBService dbService;
	private List<CaseGuideDocItem> guidelist;
	private List<CaseDocItem> doclist;
	private long caseid;
	private int opt = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = FileBrowserActivity.this;
		dbService = new DBService(context);

		Intent intent = getIntent();
		caseid = intent.getLongExtra("caseId", -1);
		opt = intent.getIntExtra("opt", 0);
		CaseItem item = dbService.findCaseByCaseId(caseid);

		switch (opt) {
		case 1:
			// ָ����
			guidelist = dbService.findCaseGuideDocsBycaseId(String.valueOf(caseid));
			if (guidelist != null) {
				Log.d("lm", "caseid = " + caseid + "\nguidelist.length = " + guidelist.size());
				createpaths(opt);
				if (item != null) {
					mRoot = AppInfo.base_dir + "/" + item.casedir + "/ָ����";
				}
				setContentView(R.layout.file_explorer);
				mTextViewLocation = (TextView) findViewById(R.id.textview_path);
				getDirectory(mRoot);
			} else {
				setContentView(R.layout.file_explorer_nores);
			}
			break;
		case 2:
			// ��
			doclist = dbService.findCaseDocsBycaseId(String.valueOf(caseid));
			if (doclist != null) {
				Log.d("lm", "caseid = " + caseid + "\ndoclist.length = " + doclist.size());
				createpaths(opt);
				if (item != null) {
					mRoot = AppInfo.base_dir + "/" + item.casedir + "/��";
				}
				setContentView(R.layout.file_explorer);
				mTextViewLocation = (TextView) findViewById(R.id.textview_path);
				getDirectory(mRoot);
			} else {
				setContentView(R.layout.file_explorer_nores);
			}
			break;
		default:
			break;
		}
		
	}

	/**
	 * Ϊÿһ���𰸴�����Ӧ��·���������ļ����������ʽ��չʾ
	 * 
	 * @param list
	 */
	private void createpaths(List<CaseDocItem> list) {
		for (CaseDocItem item : list) {
			File file = new File(AppInfo.base_dir + "/" + item.docPath);
			if (!(file.getParentFile().exists())) {
				// ���ļ�Ŀ¼������ʱ�򴴽�
				file.getParentFile().mkdirs();
			}
			if (!file.exists()) {
				try {
					file.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	// Ϊÿһ����Դ����һ���ļ�
	private void createpaths(int opt) {
		switch (opt) {
		case 1:
			// ָ����
			for (CaseGuideDocItem item : guidelist) {
				File file = new File(AppInfo.base_dir + "/" + item.guideDocPath);
				if (!(file.getParentFile().exists())) {
					// ���ļ�Ŀ¼������ʱ�򴴽�
					file.getParentFile().mkdirs();
				}
				if (!file.exists()) {
					try {
						file.createNewFile();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			break;
		case 2:
			// ��
			for (CaseDocItem item : doclist) {
				File file = new File(AppInfo.base_dir + "/" + item.docPath);
				if (!(file.getParentFile().exists())) {
					// ���ļ�Ŀ¼������ʱ�򴴽�
					file.getParentFile().mkdirs();
				}
				if (!file.exists()) {
					try {
						file.createNewFile();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			break;
		default:
			break;
		}
	}
	
	private void sortFilesByDirectory(File[] files) {
		Arrays.sort(files, new Comparator<File>() {
			public int compare(File f1, File f2) {
				return Long.valueOf(f1.length()).compareTo(f2.length());
			}
		});
	}

	private void getDirectory(String dirPath) {
		try {
			switch (opt) {
			case 1:
				// ָ����
				if (dirPath.indexOf("ָ����") > 0) {
					mTextViewLocation.setText("Location: "
							+ dirPath.substring(dirPath.indexOf("ָ����")));
				} else {
					mTextViewLocation.setText("Location: " + dirPath);
				}
				break;
			case 2:
				// ��
				if (dirPath.indexOf("��") > 0) {
					mTextViewLocation.setText("Location: "
							+ dirPath.substring(dirPath.indexOf("��")));
				} else {
					mTextViewLocation.setText("Location: " + dirPath);
				}
				break;
			default:
				break;
			}
			File f = new File(dirPath);
			File[] temp = f.listFiles();
			sortFilesByDirectory(temp);
			File[] files = null;
			if (!dirPath.equals(mRoot)) {
				files = new File[temp.length + 1];
				System.arraycopy(temp, 0, files, 1, temp.length);
				files[0] = new File(f.getParent());
			} else {
				files = temp;
			}
			mFiles = files;
			setListAdapter(new FileBrowserAdapter(this, files,
					temp.length == files.length));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		File file = mFiles[position];
		if (file.isDirectory()) {
			if (file.canRead())
				getDirectory(file.getAbsolutePath());
			else {
				Toast.makeText(context,
						"[" + file.getName() + "] folder can't be read!",
						Toast.LENGTH_SHORT).show();
			}
		} else {
			showDoc(file.getAbsolutePath());
		}
	}

	private void showDoc(String filepath) {
		Intent intent = new Intent(context, AnswerViewActivity.class);
		String[] tmp = filepath.split(AppInfo.base_dir + "/");
		if (tmp.length > 1) {
			String path = tmp[1];
			Log.d("lm", "docpath = " + path);
			switch (opt) {
			case 1:
				// ָ����
				CaseGuideDocItem guideitem = dbService.findCaseGuideDocByguideDocPath(path);
				intent.putExtra("guideId", guideitem.guideId);
				intent.putExtra("guideName", guideitem.guideDocName);
				intent.putExtra("caseId", guideitem.caseId);
				intent.putExtra("guidePath", guideitem.guideDocPath);
				break;
			case 2:
				// ��
				CaseDocItem docitem = dbService.findCaseDocBydocPath(path);
				intent.putExtra("docId", docitem.docId);
				intent.putExtra("docName", docitem.docName);
				intent.putExtra("caseId", docitem.caseId);
				intent.putExtra("docPath", docitem.docPath);
				break;
			default:
				break;
			}
			intent.putExtra("opt", opt);
			startActivity(intent);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		dbService.close();
	}

}