package com.dispenda.model;

import java.util.List;
import com.dispenda.controller.ControllerFactory;

public enum MohonProvider {
	INSTANCE;
	
	private List<Mohon> mohon;
	
	private MohonProvider(){
		mohon = ControllerFactory.getMainController().getCpMohonDAOImpl().getAllMohon();
	}
	
	public List<Mohon> getMohon(){
		return mohon;
	}
	
	public Mohon getSelectedMohon(Integer index){
		return mohon.get(index);
	}
	
	public void addItem(Mohon x){
		mohon.add(x);
	}
	
	public void removeItem(int index){
		ControllerFactory.getMainController().getCpMohonDAOImpl().deleteMohon(mohon.get(index).getIdMohon());
		mohon.remove(index);
	}

}
