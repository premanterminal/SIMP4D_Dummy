package com.dispenda.model;

import java.util.List;

import com.dispenda.controller.ControllerFactory;

public enum PejabatProvider {
	INSTANCE;
	
	private List<Pejabat> pejabat;
	
	private PejabatProvider(){
		pejabat = ControllerFactory.getMainController().getCpPejabatDAOImpl().getAllPejabat();
	}
	
	public List<Pejabat> getPejabat(){
		return pejabat;
	}
	
	public Pejabat getSelectedPejabat(Integer index){
		return pejabat.get(index);
	}
	
	public void addItem(Pejabat x){
		pejabat.add(x);
	}
	
	public void removeItem(int index){
		ControllerFactory.getMainController().getCpPejabatDAOImpl().deletePejabat(pejabat.get(index).getIdPejabatNIP());
		pejabat.remove(index);
	}

}
