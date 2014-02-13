package com.realaction.yunbomobile.utils;

import java.io.InputStream;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.NameValuePair;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.realaction.yunbomobile.moddel.CaseSourcesItem;

import android.content.Context;
import android.util.Log;

public class CaseSourcesUtils {
	private static final String TAG = "CaseSourcesUtils";
	
	private Context context;

	public CaseSourcesUtils(Context context) {
		this.context = context;
	}

	public List<CaseSourcesItem> getCaseSourcesList(String url, List<NameValuePair> datas) {
		InputStream xmlStream = HttpTool.sendDataByPost(url, datas);
//		Log.d(TAG, HttpTool.convertStreamToString(xmlStream));
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser parser = factory.newSAXParser();
			XMLReader xmlreader = parser.getXMLReader();
			CaseSourcesHandler handler = new CaseSourcesHandler(context);
			xmlreader.setContentHandler(handler);
			InputSource source = new InputSource(xmlStream);
			xmlreader.parse(source);
			List<CaseSourcesItem> items = handler.getCaseSourcesList();
			return items;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}