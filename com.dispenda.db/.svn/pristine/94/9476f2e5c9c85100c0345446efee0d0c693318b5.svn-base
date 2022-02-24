package com.dispenda.model;
import java.util.List;

import com.dispenda.controller.ControllerFactory;

public enum KecamatanProvider {
	INSTANCE;
	
	private List<Kecamatan> camat;
	
	private KecamatanProvider(){
		camat = ControllerFactory.getMainController().getCpKecamatanDAOImpl().getAllKecamatan();
	}
	
	public List<Kecamatan> getKecamatan(){
		return camat;
	}
	
	public Kecamatan getSelectedKecamatan(Integer index){
		return camat.get(index);
	}
	
	public void addItem(Kecamatan x){
		camat.add(x);
	}
	
	public void removeItem(int index){
		ControllerFactory.getMainController().getCpKecamatanDAOImpl().deleteKecamatan(camat.get(index).getIdKecamatan());
		camat.remove(index);
	}
}
