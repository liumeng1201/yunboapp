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
 * 课程信息工具类
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
	 * 解析课程树资源并返回课程List
	 * 
	 * @param url
	 *            获取课程资源的url
	 * @param datas
	 *            需要传递的参数stuId/teaId
	 * @return 课程List
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
