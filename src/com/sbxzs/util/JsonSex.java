package com.sbxzs.util;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class JsonSex {
      public static String getSex(String str){
    	  String sexMessage="��δ֪��"; 
     		try {
    			JSONTokener jsonParser = new JSONTokener(str);
    			JSONObject person = (JSONObject) jsonParser.nextValue();  
                String s=person.getString("gender");
                
                if(s.equals("m")){
                	sexMessage="������������";
                }
                else if(s.equals("f")){
                	sexMessage="������Ů����";
                }
                	
    		} catch (JSONException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
		return sexMessage;    	  
	}
}
