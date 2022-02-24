package com.dispenda.model;

import java.util.List;
import com.dispenda.controller.ControllerFactory;

public enum WpTutupProvider {
	INSTANCE;
	
	private List<WpTutup> wp_tutup;
	
	private WpTutupProvider(){
		wp_tutup = ControllerFactory.getMainController().getCpWpTutupDAOImpl().getAllWpTutup();
	}
	
	public List<WpTutup> getWpTutup(){
		return wp_tutup;
	}
	
	public WpTutup getSelectedWpTutup(Integer index){
		return wp_tutup.get(index);
	}
	
	public void addItem(WpTutup x){
		wp_tutup.add(x);
	}
	
	public void removeItem(int index){
		ControllerFactory.getMainController().getCpWpTutupDAOImpl().deleteWpTutup(wp_tutup.get(index).getIdTutup());
		wp_tutup.remove(index);
	}

}
