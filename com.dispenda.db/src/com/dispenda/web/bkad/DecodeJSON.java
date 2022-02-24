package com.dispenda.web.bkad;

import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public enum DecodeJSON {
	Instance;

	public void decodeJSONMultipleRow (String jsonMultipleString) throws JSONException{
		JSONArray jsonMultiple = new JSONArray(jsonMultipleString);
		for(int i = 0;i < jsonMultiple.length();i++){
			JSONObject jsonObjectRow = jsonMultiple.getJSONObject(i);
			decodeJSONSingleRow(jsonObjectRow.toString());
		}
	}

	public JSONObject decodeJSONSingleRow (String jsonSingleString) throws JSONException{
		JSONObject jsonSingle = new JSONObject(jsonSingleString);
		Iterator keys = jsonSingle.keys();
		String stringKey = "";
		while(keys.hasNext()){
			stringKey = (String)keys.next();
			System.out.println(stringKey +" = "+jsonSingle.get(stringKey));
		}
		return jsonSingle;
	}
}
