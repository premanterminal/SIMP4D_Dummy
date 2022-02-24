package com.dispenda.model;

import java.util.List;

import com.dispenda.controller.ControllerFactory;

public enum PendaftaranAntrianProvider {
	INSTANCE;
	
	private List<PendaftaranAntrian> daftar_antri;
	private List<PendaftaranAntrian> daftar_antri_tolak;
	
	private PendaftaranAntrianProvider(){
		  daftar_antri = ControllerFactory.getMainController().getCpDaftarAntrianDAOImpl().getAllPendaftaranAntrian();
		  daftar_antri_tolak = ControllerFactory.getMainController().getCpDaftarAntrianDAOImpl().getPendaftaranAntrianTolak();
	}
	
	public List<PendaftaranAntrian> refresh(){
		if(!daftar_antri.isEmpty())
			daftar_antri.clear();
		daftar_antri = ControllerFactory.getMainController().getCpDaftarAntrianDAOImpl().getAllPendaftaranAntrian();
		return daftar_antri;
	}
	
	public List<PendaftaranAntrian> getAntriTolak(){
		if(!daftar_antri_tolak.isEmpty())
			daftar_antri_tolak.clear();
		daftar_antri_tolak = ControllerFactory.getMainController().getCpDaftarAntrianDAOImpl().getPendaftaranAntrianTolak();
		return daftar_antri_tolak;
	}
	
/*	public List<PendaftaranAntrian> getAllAntriTolak(){
		return daftar_antri_tolak;
	}*/
	
	public List<PendaftaranAntrian> getAntri(){
		return daftar_antri;
	}
	
	public PendaftaranAntrian getSelectedAntri(Integer index){
		return daftar_antri.get(index);
	}
	
	public void addItem(PendaftaranAntrian x){
		daftar_antri.add(x);
	}
	
	public void removeItem(int index){
		ControllerFactory.getMainController().getCpDaftarAntrianDAOImpl().deleteAntri(daftar_antri.get(index).getIndex());
		daftar_antri.remove(index);
	}
}