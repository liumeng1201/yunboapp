package com.realaction.yunbomobile.utils;

import java.io.InputStream;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.NameValuePair;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.content.Context;

import com.realaction.yunbomobile.moddel.CaseItem;

/**
 * �γ̰�����Դ������
 * 
 * @author liumeng
 */
public class CasesUtils {
	private Context context;
	private String scoreId;

	public CasesUtils(Context context, String scoreId) {
		this.context = context;
		this.scoreId = scoreId;
	}

	/**
	 * ������������Դ�����ذ���List
	 * 
	 * @param url
	 *            ��ȡ������Դ��url
	 * @param datas
	 *            ��Ҫ���ݵĲ���
	 * @return ����List
	 */
	public List<CaseItem> getCasesList(String url, List<NameValuePair> datas) {
		InputStream xmlStream = HttpTool.sendDataByPost(url, datas);
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser parser = factory.newSAXParser();
			XMLReader xmlreader = parser.getXMLReader();
			CasesHandler handler = new CasesHandler(context, scoreId);
			xmlreader.setContentHandler(handler);
			InputSource source = new InputSource(xmlStream);
			xmlreader.parse(source);
			List<CaseItem> items = handler.getCasesList();
			return items;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
