package com.realaction.yunbomobile.utils;

import java.io.InputStream;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.NameValuePair;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.content.Context;

import com.realaction.yunbomobile.moddel.CourseItem;

/**
 * �γ���Ϣ������
 * 
 * @author liumeng
 */
public class CourseUtils {
	private Context context;
	private String userId;

	public CourseUtils(Context context, String userId) {
		this.context = context;
		this.userId = userId;
	}

	/**
	 * �����γ�����Դ�����ؿγ�List
	 * 
	 * @param url
	 *            ��ȡ�γ���Դ��url
	 * @param datas
	 *            ��Ҫ���ݵĲ���stuId/teaId
	 * @return �γ�List
	 */
	public List<CourseItem> getCourseList(String url, List<NameValuePair> datas) {
		InputStream xmlStream = HttpTool.sendDataByPost(url, datas);
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser parser = factory.newSAXParser();
			XMLReader xmlreader = parser.getXMLReader();
			CourseHandler handler = new CourseHandler(context, userId);
			xmlreader.setContentHandler(handler);
			InputSource source = new InputSource(xmlStream);
			xmlreader.parse(source);
			List<CourseItem> items = handler.getCourseList();
			return items;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
