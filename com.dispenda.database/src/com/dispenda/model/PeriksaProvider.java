package com.dispenda.model;

import java.util.List;
import com.dispenda.controller.ControllerFactory;

public enum PeriksaProvider {
	INSTANCE;
	
	private List<Periksa> periksa;
	
	private PeriksaProvider(){
		periksa = ControllerFactory.getMainController().getCpPeriksaDAOImpl().getAllPeriksa();
	}
	
	public List<Periksa> getPeriksa(){
		return periksa;
	}
	
	public Periksa getSelectedPeriksa(Integer index){
		return periksa.get(index);
	}
	
	public void addItem(Periksa x){
		periksa.add(x);
	}
	
	public void removeItem(int index){
		ControllerFactory.getMainController().getCpPeriksaDAOImpl().deletePeriksa(periksa.get(index).getIdPeriksa());
		periksa.remove(index);
	}

}
