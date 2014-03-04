package com.hiveview.domybox.utils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GetCitiesId {
	
	
	public static GetCitiesId instance;
	
	public static String citiesIdContents;
	
	String regEx_city = " cid=\"([\\w\\W]+?)\"";
	
	
	public static GetCitiesId getInstance(){
		if(instance==null){
			instance = new GetCitiesId();
			InputStream inputStream = GetCitiesId.class.getClassLoader().getResourceAsStream("weathercn.xml");
			try {
				byte[] bts = readStream(inputStream);
				citiesIdContents = new String(bts, "UTF-8");
			} catch (UnsupportedEncodingException e) {
			} catch (Exception e) {
			}
		}
		return instance;
	}
	
	public String getCitiesId(String cityName,String zoneName){
		String coupon = null;
		String sCoupon = null;
		
		String secondCity = cityName + regEx_city;
		Pattern sPat_coupon = Pattern.compile(secondCity);
		Matcher sMat_coupon = sPat_coupon.matcher(citiesIdContents);
		while (sMat_coupon.find()) {
			sCoupon = sMat_coupon.group(1);
			break;
		}
		
		String temp_regEx_city = zoneName + regEx_city;
		Pattern pat_coupon = Pattern.compile(temp_regEx_city);
		Matcher mat_coupon = pat_coupon.matcher(citiesIdContents);
		while (mat_coupon.find()) {
			coupon = mat_coupon.group(1);
			if(coupon.contains(sCoupon))
			{
				break;
			}
		}
		
		return coupon;
	}
	
	
	public static byte[] readStream(InputStream inStream) throws Exception {
		
		ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = -1;
		while ((len = inStream.read(buffer)) != -1) {
			outSteam.write(buffer, 0, len);
		}
		outSteam.close();
		inStream.close();
		return outSteam.toByteArray();
		
	}


}
