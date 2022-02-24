package com.dispenda.model;

import java.util.List;

import com.dispenda.controller.ControllerFactory;

public enum PajakProvider {
	INSTANCE;
	
	private List<Pajak> pajak;
	
	private PajakProvider(){
		pajak = ControllerFactory.getMainController().getCpPajakDAOImpl().getAllPajak();
	}
	
	public List<Pajak> getPajak(){
		return pajak;
	}
	
	public Pajak getSelectedBidangUsaha(Integer index){
		return pajak.get(index);
	}
	
	public void addItem(Pajak x){
		pajak.add(x);
	}
	
	public void removeItem(int index){
		ControllerFactory.getMainController().getCpPajakDAOImpl().deletePajak(pajak.get(index).getIdPajak());
		pajak.remove(index);
	}
}
