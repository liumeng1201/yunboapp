package com.realaction.yunbomobile.utils;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.content.Context;
import android.util.Log;

import com.realaction.yunbomobile.db.DBService;
import com.realaction.yunbomobile.moddel.CaseDocItem;
import com.realaction.yunbomobile.moddel.CaseGuideDocItem;

public class CaseSourcesHandler extends DefaultHandler {
	private static final String TAG = "CaseSourcesHandler";
	private Context context;
	private List<CaseGuideDocItem> casesourcesList;
	private List<CaseDocItem> casedocsList;
	private DBService dbService;
	
	public CaseSourcesHandler(Context context) {
		this.context = context;
	}

	/**
	 * @return 课程案例实验指导书List
	 */
	public List<CaseGuideDocItem> getCaseSourcesList() {
		return casesourcesList;
	}
	
	// 初始化工作
	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
		casesourcesList = new ArrayList<CaseGuideDocItem>();
		casedocsList = new ArrayList<CaseDocItem>();
		dbService = new DBService(context);
	}
	
	// 收尾工作
	@Override
	public void endDocument() throws SAXException {
		super.endDocument();
		dbService.close();
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		super.characters(ch, start, length);
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		super.endElement(uri, localName, qName);
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
		if (localName.equals("caseguidedoc")) {
			CaseGuideDocItem item = new CaseGuideDocItem();
			item.guideId = Long.parseLong(attributes.getValue("guideId"));
			item.guideDocName = attributes.getValue("guideDocName");
			item.guideDocDesc = attributes.getValue("guideDocDesc");
			item.guideDocPath = attributes.getValue("guideDocPath");
			item.guideTmp = attributes.getValue("guideTmp");
			item.mediaTypeId = Integer.parseInt(attributes.getValue("mediaTypeId"));
			item.caseId = Long.parseLong(attributes.getValue("caseId"));
			item.isDownload = 0;
			item.localPath = String.valueOf(0);
			Log.d("lm", "insertCaseGuideDocTb result = " + dbService.insertCaseGuideDocTb(item));
			casesourcesList.add(item);
		}
		if (localName.equals("casedoc")) {
			CaseDocItem item = new CaseDocItem();
			item.docId = Long.parseLong(attributes.getValue("docId"));
			item.docName = attributes.getValue("docName");
			item.docDesc = attributes.getValue("docDesc");
			item.docPath = attributes.getValue("docPath");
			item.docTypeId = Integer.parseInt(attributes.getValue("docTypeId"));
			item.caseId = Long.parseLong(attributes.getValue("caseId"));
			item.isDownload = 0;
			item.localPath = String.valueOf(0);
			Log.d("lm", "insertCaseDocTb result = " + dbService.insertCaseDocTb(item));
			casedocsList.add(item);
		}
	}
}
