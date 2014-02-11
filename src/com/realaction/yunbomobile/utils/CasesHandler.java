package com.realaction.yunbomobile.utils;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.content.Context;

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

	public CasesHandler(Context context) {
		this.context = context;
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
	public void endDocument() throws SAXException {
		super.endDocument();
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		super.endElement(uri, localName, qName);
	}

	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
		casesList = new ArrayList<CaseItem>();
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
			// TODO CaseGroup需要处理
			item.caseGroupName = attributes.getValue("caseGroupName");
			casesList.add(item);
		}
	}

}
