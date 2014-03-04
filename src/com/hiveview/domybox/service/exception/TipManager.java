package com.hiveview.domybox.service.exception;

import java.util.HashMap;

public class TipManager {

	private static TipManager container = new TipManager();
	private static HashMap<Integer, String>  map = new HashMap<Integer, String>();  

	public static TipManager getInstance(){

		if(map.size() == 0) 
			initTips();

		return container;
	}

	public String getTip(Integer code){

		return map.get(code);
	}
	
	private static void initTips(){
		 
		map.put(1, "");
		map.put(2, "");
		map.put(3, "");
		map.put(4, "");
		map.put(5, "");
		map.put(6, "");
		map.put(7, "");
		map.put(8, "");
		map.put(9, "");
		map.put(10, "");
		map.put(11, "");
		map.put(12, "");
		
	}
	
	private TipManager(){};

}
