package com.realaction.yunbomobile.utils;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.content.Context;
import android.util.Log;

import com.realaction.yunbomobile.db.DBService;
import com.realaction.yunbomobile.moddel.CaseItem;

/**
 * 课程案例资源解析类
 * 
 * @author liumeng
 */
public class CasesHandler extends DefaultHandler {
	private static final String TAG = "CourseHandler";
	private Context context;
	private List<CaseItem> casesList;
	private DBService dbService;
	private String scoreId;

	public CasesHandler(Context context, String scoreId) {
		this.context = context;
		this.scoreId = scoreId;
	}

	/**
	 * @return 案例列表
	 */
	public List<CaseItem> getCasesList() {
		return casesList;
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		super.characters(ch, start, length);
	}

	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
		casesList = new ArrayList<CaseItem>();
		dbService = new DBService(context);
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
		if (localName.equals("case")) {
			CaseItem item = new CaseItem();
			item.caseId = Long.parseLong(attributes.getValue("caseId"));
			item.caseName = attributes.getValue("caseName");
			item.keyWords = attributes.getValue("keyWords");
			item.devRoleName = attributes.getValue("devRoleName");
			item.teacherName = attributes.getValue("teacherName");
			item.caseGroupId = Long.parseLong(attributes.getValue("caseGroupId"));
			// TODO CaseGroup需要处理
			item.caseGroupName = attributes.getValue("caseGroupName");
			item.scoreId = scoreId;
			item.download = 0;
			item.caseDesc = attributes.getValue("caseDesc");
			casesList.add(item);
		}
	}
	
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		super.endElement(uri, localName, qName);
	}
	
	@Override
	public void endDocument() throws SAXException {
		super.endDocument();
		dbService.beginTransaction();
		try {
			for (CaseItem item : casesList) {
				dbService.insertCaseTb(item);
			}
			dbService.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dbService.endTransaction();
		}
		dbService.close();
	}

}
