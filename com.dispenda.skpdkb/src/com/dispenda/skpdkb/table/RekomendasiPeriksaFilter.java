package com.dispenda.skpdkb.table;

import java.text.SimpleDateFormat;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.widgets.List;

import com.dispenda.skpdkb.views.RekomendasiPeriksaView.RPObject;

public class RekomendasiPeriksaFilter extends ViewerFilter {

	private String strNpwp = "",strNamaBadan = "",search="";
	private SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
	private String[] listString = new String[]{"NPWP","NamaBadan"};
	private List list;
	
	public RekomendasiPeriksaFilter(/*List list*/){
//		this.list = list;
	}
	
	public void setSearchText(String s/*, List list*/){
//		this.list = list;
//		String[] splitter = s.split(";");
		search = s;
		
	}
	
	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		int count = 0;
		RPObject rpo = (RPObject) element;
		if(rpo.getNpwp().matches("(?i).*"+search.toUpperCase()+".*")){
			return true;
		}
		else if(rpo.getNamaBadan().matches("(?i).*"+search.toUpperCase()+".*")){
			return true;
		}else
			return false;
		
		/*if(length == count)
			return true;
		else
			return false;*/
	}
}
