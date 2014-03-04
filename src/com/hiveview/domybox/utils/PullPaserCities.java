package com.hiveview.domybox.utils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;

import android.util.Xml;

public class PullPaserCities {

	public static Map<String,Map<String,List<String>>> getCities(InputStream inStream) throws Throwable {
		
		Map<String,Map<String,List<String>>> citiesMap = new LinkedHashMap<String, Map<String,List<String>>>();
		
		Map<String,List<String>> DCitiesMap = null;
		ArrayList<String> CCitiesList = null;
		
		String name;
		String attribute;
		
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(inStream, "UTF-8");
		int eventType = parser.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.START_DOCUMENT:
				break;

			case XmlPullParser.START_TAG:
				name = parser.getName();
				attribute = parser.getAttributeName(0);
				if(attribute.equals("pid")){
					DCitiesMap = new LinkedHashMap<String, List<String>>();
					citiesMap.put(name, DCitiesMap);
				}
				if(attribute.equals("did")){
					CCitiesList = new ArrayList<String>();
					DCitiesMap.put(name, CCitiesList);
				}
				if(attribute.equals("cid")){
					CCitiesList.add(name);
				}
				break;

			case XmlPullParser.END_TAG:
				break;
			}
			eventType = parser.next();
		}
		
		return citiesMap;
		
	}
	
	
	public static Map<String,Map<String,List<String>>> getJsonCities(InputStream inStream) throws Throwable {
		
//		省
		Map<String,Map<String,List<String>>> citiesMap = new LinkedHashMap<String, Map<String,List<String>>>();
		
//		市
		Map<String,List<String>> DCitiesMap = null;
		
//		县
		ArrayList<String> CCitiesList = null;
		
		String name;
		String attribute;
		
		String jsonStr = StringUtils.converStreamToString(inStream);
		JSONArray str = new JSONArray(jsonStr);
		
		int count = str.length();
		for(int i = 0 ; i < count ; i++){
			DCitiesMap = new LinkedHashMap<String, List<String>>();
			//省
			JSONObject province = str.getJSONObject(i);
			String provinceName = province.getString("provinceName");
			
			//城市
			JSONArray cityList = province.getJSONArray("cityList");
			int cityListSize = cityList.length();
			for (int j = 0 ; j < cityListSize ; j++){
				// 县
				JSONObject hsien = cityList.getJSONObject(i);
				JSONArray hsienList = hsien.getJSONArray("hsienList");
				int hsienListSize = hsienList.length();
				for (int k = 0; k < hsienListSize; k++) {
					
				}
				
			}
			
		}
			
		
		
		return citiesMap;
		
	}
	
	public static HashMap<String,String> getZones(InputStream inStream) throws Throwable {
		
		HashMap<String, String> zoneMap = new HashMap<String, String>();
		
		String name;
		String attribute;
		
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(inStream, "UTF-8");
		int eventType = parser.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.START_DOCUMENT:
				break;

			case XmlPullParser.START_TAG:
				name = parser.getName();
				attribute = parser.getAttributeName(0);
				if(attribute.equals("cid")){
					zoneMap.put(name, parser.getAttributeValue(0));
				}
				break;

			case XmlPullParser.END_TAG:
				break;
			}
			eventType = parser.next();
		}
		
		return zoneMap;
		
	}
}
