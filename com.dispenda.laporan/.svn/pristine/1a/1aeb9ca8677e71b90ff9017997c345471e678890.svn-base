package com.dispenda.laporan.tree;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

public class LPBRekapTreeLabelProvider implements ITableLabelProvider {
	private NumberFormat indFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));

	@SuppressWarnings("rawtypes")
	private List listeners;
	
	@SuppressWarnings("rawtypes")
	public LPBRekapTreeLabelProvider() {
		listeners = new ArrayList();
	}
	@SuppressWarnings("unchecked")
	@Override
	public void addListener(ILabelProviderListener arg0) {
		listeners.add(arg0);
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isLabelProperty(Object arg0, String arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener arg0) {
		listeners.remove(arg0);
	}

	@Override
	public String getColumnText(Object element, int colNumber) {
		switch (colNumber){
			case 0:
				return ((Node) element).getName();
			case 1:
				return indFormat.format(((Node) element).getPokok());
			case 2:
				return indFormat.format(((Node) element).getDenda());
			case 3:
				return indFormat.format(((Node) element).getJumlah());
			default:
				return null;
		}
	}
	@Override
	public Image getColumnImage(Object arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}
}
