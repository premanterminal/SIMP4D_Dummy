package com.dispenda.model;

import java.util.List;

import com.dispenda.controller.ControllerFactory;

public enum KelurahanProvider {
	INSTANCE;
	
	private List<Kelurahan> lurah;
	
	private KelurahanProvider(){
		
	}
	
	public List<Kelurahan> getKelurahan(){
		lurah = ControllerFactory.getMainController().getCpKelurahanDAOImpl().getAllKelurahan();
		return lurah;
	}
	
	public List<Kelurahan> getKelurahan(Integer idKecamatan){
		List<Kelurahan> lurah = ControllerFactory.getMainController().getCpKelurahanDAOImpl().getAllKelurahan(idKecamatan);
		this.lurah = lurah;
		return this.lurah;
	}
	
	public Kelurahan getSelectedKelurahan(Integer idKelurahan){
		return lurah.get(idKelurahan);
	}
	
	public void addItem(Kelurahan x){
		lurah.add(x);
	}
	
	public void removeItem(int index){
		ControllerFactory.getMainController().getCpKelurahanDAOImpl().deleteKelurahan(lurah.get(index).getIdKecamatan());
		lurah.remove(index);
	}
}
