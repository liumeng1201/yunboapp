package com.realaction.yunbomobile.utils;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.realaction.yunbomobile.moddel.CaseSourcesItem;

import android.content.Context;

public class CaseSourcesHandler extends DefaultHandler {
	private static final String TAG = "CaseSourcesHandler";
	private Context context;
	private List<CaseSourcesItem> casesourcesList;

	public CaseSourcesHandler(Context context) {
		this.context = context;
	}

	/**
	 * @return 课程树List
	 */
	public List<CaseSourcesItem> getCaseSourcesList() {
		return casesourcesList;
	}

	// 收尾工作
	@Override
	public void endDocument() throws SAXException {
		super.endDocument();
	}

	// 初始化工作
	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
		casesourcesList = new ArrayList<CaseSourcesItem>();
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
			CaseSourcesItem item = new CaseSourcesItem();
			item.guideId = Long.parseLong(attributes.getValue("guideId"));
			item.guideDocName = attributes.getValue("guideDocName");
			item.guideDocDesc = attributes.getValue("guideDocDesc");
			item.guideDocPath = attributes.getValue("guideDocPath");
			item.guideTmp = attributes.getValue("guideTmp");
			item.mediaTypeId = Integer.parseInt(attributes.getValue("mediaTypeId"));
			item.caseId = Long.parseLong(attributes.getValue("caseId"));
			casesourcesList.add(item);
		}
	}
}
