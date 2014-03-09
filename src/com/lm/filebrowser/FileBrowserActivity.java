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
	private List<CaseDocItem> doclist;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = FileBrowserActivity.this;
		dbService = new DBService(context);

		Intent intent = getIntent();
		long caseid = intent.getLongExtra("caseId", -1);
		doclist = dbService.findCaseDocsBycaseId(String.valueOf(caseid));
		Log.d("lm", "caseid = " + caseid + "\ndoclist.length = " + doclist.size());
		
		// TODO ��Ҫ�����߼��Ƿ���ȷ
		createpaths(doclist);
		
		CaseItem item = dbService.findCaseByCaseId(caseid);
		if (item != null) {
			mRoot = AppInfo.base_dir + "/" + item.casedir + "/��";
		}

		setContentView(R.layout.file_explorer);
		mTextViewLocation = (TextView) findViewById(R.id.textview_path);
		getDirectory(mRoot);
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

	private void sortFilesByDirectory(File[] files) {
		Arrays.sort(files, new Comparator<File>() {
			public int compare(File f1, File f2) {
				return Long.valueOf(f1.length()).compareTo(f2.length());
			}
		});
	}

	private void getDirectory(String dirPath) {
		try {
			if (dirPath.indexOf("��") > 0) {
				mTextViewLocation.setText("Location: " + dirPath.substring(dirPath.indexOf("��")));
			} else {
				mTextViewLocation.setText("Location: " + dirPath);
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
			showDoc(doclist.get(position));
		}
	}

	private void showDoc(CaseDocItem caseDocItem) {
		Intent intent = new Intent(context, AnswerViewActivity.class);
		intent.putExtra("docId", caseDocItem.docId);
		intent.putExtra("docName", caseDocItem.docName);
		intent.putExtra("caseId", caseDocItem.caseId);
		intent.putExtra("docPath", caseDocItem.docPath);
		startActivity(intent);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		dbService.close();
	}

}