package com.realaction.yunbomobile.view.caseviews;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.realaction.yunbomobile.R;
import com.realaction.yunbomobile.db.DBService;
import com.realaction.yunbomobile.moddel.CaseDocItem;

/**
 * 案例答案资源展示fragment
 * 
 * @author liumeng
 */
public class AnswerViewFragment extends Fragment {
	private Context context;
	private long caseId;
	private List<CaseDocItem> doclist;
	private DBService dbService;
	
	public AnswerViewFragment() {}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = getActivity().getApplicationContext();
		doclist = new ArrayList<CaseDocItem>();
		dbService = new DBService(context);
		
		Bundle bundle = getArguments();
		if (bundle != null) {
			caseId = bundle.getLong("caseId");
			doclist = dbService.findCaseDocsBycaseId(String.valueOf(caseId));
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_answerview, null);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		dbService.close();
	}

}
