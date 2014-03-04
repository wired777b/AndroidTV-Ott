package com.hiveview.domybox.utils;

import android.graphics.Typeface;

import com.hiveview.domybox.common.PengDoctorApplication;

public class TypefaceUtils {
	private static Typeface standardfFontFace;
	private static Typeface ce35FontFace; 
	private static Typeface ce36FontFace;
	private static TypefaceUtils typeFace = new TypefaceUtils();

	private TypefaceUtils() {}
	
	/**幼圆字体*/
	public static Typeface  getStandardfFontFace(){
		if(standardfFontFace==null){
			standardfFontFace = Typeface.createFromAsset(PengDoctorApplication.mContext.getAssets(),"fonts/standard.TTF");
		}
		return standardfFontFace;
	}
	
	public static Typeface  getCE35FontFace(){
		if(ce35FontFace==null){
			ce35FontFace = Typeface.createFromAsset(PengDoctorApplication.mContext.getAssets(),"fonts/ce_35.TTF");
		}
		return ce35FontFace;
	}
	
	public TypefaceUtils getInstance(){
		if(typeFace==null){
			typeFace=new TypefaceUtils();
		}
		return typeFace;
	}
	
}
