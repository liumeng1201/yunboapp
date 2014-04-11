package com.lm.filebrowser;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import android.app.ActionBar;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
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
import com.realaction.yunbomobile.view.caseviews.CaseSourceViewActivity;

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
			// 指导书
			guidelist = dbService.findCaseGuideDocsBycaseId(String.valueOf(caseid));
			if (guidelist != null) {
				createpaths(opt);
				if (item != null) {
					mRoot = AppInfo.base_dir + "/" + item.casedir + "/指导书";
				}
				setContentView(R.layout.file_explorer);
				mTextViewLocation = (TextView) findViewById(R.id.textview_path);
				getDirectory(mRoot);
			} else {
				setContentView(R.layout.file_explorer_nores);
			}
			break;
		case 2:
			// 答案
			doclist = dbService.findCaseDocsBycaseId(String.valueOf(caseid));
			if (doclist != null) {
				createpaths(opt);
				if (item != null) {
					mRoot = AppInfo.base_dir + "/" + item.casedir + "/答案";
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
		
		ActionBar actionbar = getActionBar();
		actionbar.setDisplayHomeAsUpEnabled(true);
	}

	// 为每一个资源创建一个文件
	private void createpaths(int opt) {
		switch (opt) {
		case 1:
			// 指导书
			for (CaseGuideDocItem item : guidelist) {
				File file = new File(AppInfo.base_dir + "/" + item.guideDocPath);
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
			break;
		case 2:
			// 答案
			for (CaseDocItem item : doclist) {
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
				// 指导书
				if (dirPath.indexOf("指导书") > 0) {
					mTextViewLocation.setText(getString(R.string.current_location)
									+ ": " + dirPath.substring(dirPath.indexOf("指导书")));
				} else {
					mTextViewLocation.setText(getString(R.string.current_location)
									+ ": " + dirPath);
				}
				break;
			case 2:
				// 答案
				if (dirPath.indexOf("答案") > 0) {
					mTextViewLocation.setText(getString(R.string.current_location)
									+ ": " + dirPath.substring(dirPath.indexOf("答案")));
				} else {
					mTextViewLocation.setText(getString(R.string.current_location)
									+ ": " + dirPath);
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
		Intent intent = new Intent(context, CaseSourceViewActivity.class);
		String[] tmp = filepath.split(AppInfo.base_dir + "/");
		if (tmp.length > 1) {
			String path = tmp[1];
			switch (opt) {
			case 1:
				// 指导书
				CaseGuideDocItem guideitem = dbService.findCaseGuideDocByguideDocPath(path);
				intent.putExtra("guideId", guideitem.guideId);
				intent.putExtra("guideName", guideitem.guideDocName);
				intent.putExtra("caseId", guideitem.caseId);
				intent.putExtra("guidePath", guideitem.guideDocPath);
				break;
			case 2:
				// 答案
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
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			this.finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		dbService.close();
	}

}