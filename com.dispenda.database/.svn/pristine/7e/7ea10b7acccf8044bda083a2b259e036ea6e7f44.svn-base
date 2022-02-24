package com.dispenda.model;

import java.util.List;

import com.dispenda.controller.ControllerFactory;

public enum UUProvider {
	INSTANCE;
	
	private List<UU> uu;
	
	private UUProvider(){
		uu = ControllerFactory.getMainController().getCpUUDAOImpl().getAllUndangUndang();
	}
	
	public List<UU> getUU(){
		return uu;
	}
	
	public UU getSelectedUU(Integer index){
		return uu.get(index);
	}
	
	public void addItem(UU x){
		uu.add(x);
	}
	
	public void removeItem(int index){
		ControllerFactory.getMainController().getCpUUDAOImpl().deleteUU(uu.get(index).getIdUU());
		uu.remove(index);
	}
}
