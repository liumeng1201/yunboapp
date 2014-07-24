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
 * 课程案例资源工具类
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
	 * 解析案例树资源并返回案例List
	 * 
	 * @param url
	 *            获取案例资源的url
	 * @param datas
	 *            需要传递的参数
	 * @return 案例List
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
