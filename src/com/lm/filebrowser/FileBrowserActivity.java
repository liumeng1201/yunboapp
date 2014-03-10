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
 * 文件浏览器类,用来实现答案资源的查看
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
	private long caseid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = FileBrowserActivity.this;
		dbService = new DBService(context);

		Intent intent = getIntent();
		caseid = intent.getLongExtra("caseId", -1);
		doclist = dbService.findCaseDocsBycaseId(String.valueOf(caseid));
		if (doclist != null) {
			Log.d("lm", "caseid = " + caseid + "\ndoclist.length = " + doclist.size());

			createpaths(doclist);

			CaseItem item = dbService.findCaseByCaseId(caseid);
			if (item != null) {
				mRoot = AppInfo.base_dir + "/" + item.casedir + "/答案";
			}

			setContentView(R.layout.file_explorer);
			mTextViewLocation = (TextView) findViewById(R.id.textview_path);
			getDirectory(mRoot);
		} else {
			setContentView(R.layout.file_explorer_nores);
		}
	}
	
	/**
	 * 为每一个答案创建对应的路径便于用文件浏览器的形式来展示
	 * 
	 * @param list
	 */
	private void createpaths(List<CaseDocItem> list) {
		for (CaseDocItem item : list) {
			File file = new File(AppInfo.base_dir + "/" + item.docPath);
			if (!(file.getParentFile().exists())) {
				// 当文件目录不存在时则创建
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
			if (dirPath.indexOf("答案") > 0) {
				mTextViewLocation.setText("Location: " + dirPath.substring(dirPath.indexOf("答案")));
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
			showDoc(file.getAbsolutePath());
		}
	}

	private void showDoc(String filepath) {
		Intent intent = new Intent(context, AnswerViewActivity.class);
		String[] tmp = filepath.split(AppInfo.base_dir + "/");
		if (tmp.length > 1) {
			String docpath = tmp[1];
			Log.d("lm", "docpath = " + docpath);
			CaseDocItem item = dbService.findCaseDocBydocPath(docpath);
			intent.putExtra("docId", item.docId);
			intent.putExtra("docName", item.docName);
			intent.putExtra("caseId", item.caseId);
			intent.putExtra("docPath", item.docPath);
			startActivity(intent);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		dbService.close();
	}

}