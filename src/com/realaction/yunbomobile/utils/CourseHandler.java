package com.realaction.yunbomobile.utils;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.content.Context;
import android.util.Log;

import com.realaction.yunbomobile.db.DBService;
import com.realaction.yunbomobile.moddel.CourseItem;

/**
 * �γ���Ϣ������
 * 
 * @author liumeng
 */
public class CourseHandler extends DefaultHandler {
	private static final String TAG = "CourseHandler";
	private Context context;
	private List<CourseItem> courseList;
	private DBService dbService;
	private long userId;

	public CourseHandler(Context context, String userId) {
		this.context = context;
		this.userId = Long.parseLong(userId);
	}

	/**
	 * @return �γ���List
	 */
	public List<CourseItem> getCourseList() {
		return courseList;
	}

	// ��ʼ������
	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
		courseList = new ArrayList<CourseItem>();
		dbService = new DBService(context);
	}
	
	// ��β����
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
				// ѧ��
				if ((attributes.getValue("scoreId")).equals("")
						|| (attributes.getValue("scoreId") == null)) {
					Log.d(TAG, "scoreid not exist");
				} else {
					item.scoreId = attributes.getValue("scoreId");
				}
				break;
			case 20:
			case 40:
				// ��ʦ
				break;
			}
			item.userId = userId;
			courseList.add(item);
			Log.d("lm", "insert result = " + dbService.insertCourseTb2(item));
		}
	}
	
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		super.endElement(uri, localName, qName);
	}

}