package com.dispenda.model;

import java.util.List;

public enum SptpdProvider {
	INSTANCE;
	
	private List<Sptpd> sptpd;
	
	private SptpdProvider(){
//		sptpd = ControllerFactory.getSptpdController().getCpSptpdDAOImpl().getAllSptpd();
	}
	
	public List<Sptpd> getSptpd(){
		return sptpd;
	}
	
	public Sptpd getSelectedSptpd(Integer index){
		return sptpd.get(index);
	}
	
	public void addItem(Sptpd x){
		sptpd.add(x);
	}
	
	public void removeItem(int index){
//		ControllerFactory.getSptpdController().getCpSptpdDAOImpl().deleteSptpd(sptpd.get(index).getID());
		sptpd.remove(index);
	}

}
