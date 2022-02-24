package com.dispenda.model;

import java.util.List;

import com.dispenda.controller.ControllerFactory;

public enum SubModulProvider{
	INSTANCE;
	
	private List<SubModul> listSubModul;
	
	private SubModulProvider(){
		listSubModul = ControllerFactory.getMainController().getCpModulDAOImpl().getAllSubModul();
	}
	
	public List<SubModul> getSubModul(){
		return listSubModul;
	}
	
	public SubModul getSelectedSubModul(Integer index){
		return listSubModul.get(index);
	}
	
	public void addItem(SubModul x){
		listSubModul.add(x);
	}
	
	public void removeItem(int index){
		ControllerFactory.getMainController().getCpModulDAOImpl().deleteSubModul(listSubModul.get(index).getIdSubModul());
		listSubModul.remove(index);
	}
}