package com.dispenda.report;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.report.viewer.api.AppContextExtension;

public class ReportAppContext extends AppContextExtension{

	public static HashMap<String, String> map = new HashMap<String, String>();
//	public static HashMap<String, byte[]> mapQR = new HashMap<String, byte[]>();
	public static HashMap<String, String> list = new HashMap<String, String>();
	public static HashMap<String, List<String>> object = new HashMap<String, List<String>>();
	public static HashMap<String, List<Object>> objectQR = new HashMap<String, List<Object>>();
	public static String name = "";
//	public static String nameQR = "";
	public static String nameObject = "";
	public static String nameObjectQR = "";
	public static String nameList = "";
	public static Object classLoader;
	public static String[] obj;
	public static Object[] listObject;
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map getAppContext(Map appContext) {
		Map hm = super.getAppContext(appContext);
		hm.put("PARENT_CLASSLOADER", classLoader);
		hm.put(name, map);
//		hm.put(nameQR, mapQR);
		hm.put(nameObject, object);
		hm.put(nameObjectQR, objectQR);
		hm.put(nameList, list);
		for (int i=0;i<obj.length;i++){
			hm.put(obj[i], listObject[i]);
		}
		return hm;
	}
	
	@Override
	public String getName() {
		return "ReportAppContext";
	}

	
}