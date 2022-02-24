package com.dispenda.model;

import java.util.List;

import com.dispenda.controller.ControllerFactory;

public enum SspdProvider {
	INSTANCE;
	
	private List<Sspd> sspd;
	
	private SspdProvider(){
		sspd = ControllerFactory.getMainController().getCpSspdDAOImpl().getAllSspd();
	}
	
	public List<Sspd> getSspd(){
		return sspd;
	}
	
	public Sspd getSelectedSspd(Integer index){
		return sspd.get(index);
	}
	
	public void addItem(Sspd x){
		sspd.add(x);
	}
	
	public void removeItem(int index){
		ControllerFactory.getMainController().getCpSspdDAOImpl().deleteSspd(sspd.get(index).getNoSspd());
		sspd.remove(index);
	}

}
