package com.dispenda.model;

import java.util.List;

import com.dispenda.controller.ControllerFactory;

public enum SspdProvider {
	INSTANCE;
	
	public static final Integer ANTRI = 2;
	public static final Integer SSPD = 1;
	public static final Integer SSPDSearch = 3;
	private List<Sspd> sspd;
	private List<Sspd> antri;
	private List<Sspd> sspdsearch;
	
	private SspdProvider(){
		
	}
	
	public void newInstance(Integer type, String param){
		switch (type){
			case 1: sspd = ControllerFactory.getMainController().getCpSspdDAOImpl().getDaftarTerimaSspd();
				break;
			case 2: antri = ControllerFactory.getMainController().getCpSspdDAOImpl().getNotaAntriSspd();
				break;
			case 3: sspdsearch = ControllerFactory.getMainController().getCpSspdDAOImpl().getDaftarTerimaSspd(param);
				break;
		}
	}
	
	public List<Sspd> getTerima(){
		return sspd;
	}
	
	public List<Sspd> getAntri(){
		return antri;
	}
	
	public List<Sspd> getTerimaSearch(){
		return sspdsearch;
	}
	
	public Sspd getSelectedAntri(Integer index){
		return antri.get(index);
	}
	
	public void addItemAntri(Sspd x){
		antri.add(x);
	}
	
	public void removeItemAntri(int index){
		ControllerFactory.getMainController().getCpSspdDAOImpl().deleteSspd(antri.get(index).getIdSspd());
		antri.remove(index);
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
		ControllerFactory.getMainController().getCpSspdDAOImpl().deleteSspd(sspd.get(index).getIdSspd());
		sspd.remove(index);
	}

}
