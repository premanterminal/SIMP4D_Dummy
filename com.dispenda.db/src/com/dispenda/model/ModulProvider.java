package com.dispenda.model;

import java.util.List;

import com.dispenda.controller.ControllerFactory;

public enum ModulProvider{
	INSTANCE;
	
	private List<Modul> listModul;
	
	private ModulProvider(){
		listModul = ControllerFactory.getMainController().getCpModulDAOImpl().getAllModul();
	}
	
	public List<Modul> getModul(){
		return listModul;
	}
	
	public Modul getSelectedModul(Integer index){
		return listModul.get(index);
	}
	
	public void addItem(Modul x){
		listModul.add(x);
	}
	
	public void removeItem(int index){
		ControllerFactory.getMainController().getCpModulDAOImpl().deleteModul(listModul.get(index).getIdModul());
		listModul.remove(index);
	}
}