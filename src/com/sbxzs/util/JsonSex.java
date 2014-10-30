package com.sbxzs.util;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class JsonSex {
      public static String getSex(String str){
    	  String sexMessage="『未知』"; 
     		try {
    			JSONTokener jsonParser = new JSONTokener(str);
    			JSONObject person = (JSONObject) jsonParser.nextValue();  
                String s=person.getString("gender");
                
                if(s.equals("m")){
                	sexMessage="『我是男生』";
                }
                else if(s.equals("f")){
                	sexMessage="『我是女生』";
                }
                	
    		} catch (JSONException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
		return sexMessage;    	  
	}
}
