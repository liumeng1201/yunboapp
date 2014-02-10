package com.realaction.yunbomobile.utils;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.content.Context;
import android.util.Log;

import com.realaction.yunbomobile.moddel.CourseItem;

/**
 * 课程树解析类
 * 
 * @author liumeng
 */
public class CourseHandler extends DefaultHandler {
	private static final String TAG = "CourseHandler";
	private Context context;
	private List<CourseItem> courseList;

	public CourseHandler(Context context) {
		this.context = context;
	}

	/**
	 * @return 课程树List
	 */
	public List<CourseItem> getCourseList() {
		return courseList;
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
		courseList = new ArrayList<CourseItem>();
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
		if (localName.equals("course")) {
			CourseItem item = new CourseItem();
			item.courseId = Long.parseLong(attributes.getValue("courseId"));
			item.courseName = attributes.getValue("courseName");
			item.type = attributes.getValue("type");
			item.icon = attributes.getValue("largeIcon");
			item.courseCode = attributes.getValue("courseCode");
			switch ((new UserUtils(context)).getUserTypeId()) {
			case 10:
				// 学生
				if ((attributes.getValue("scoreId")).equals("")
						|| (attributes.getValue("scoreId") == null)) {
					Log.d(TAG, "scoreid not exist");
				} else {
					item.scoreId = attributes.getValue("scoreId");
				}
				break;
			case 20:
			case 40:
				// 老师
				break;
			}
			courseList.add(item);
		}
	}

}