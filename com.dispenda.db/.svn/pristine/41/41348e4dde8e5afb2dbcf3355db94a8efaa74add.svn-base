package com.dispenda.model;

import java.util.List;

import com.dispenda.controller.ControllerFactory;

public enum PendaftaranSuratIzinUsahaProvider{
	INSTANCE;
	
	private List<PendaftaranSuratIzinUsaha> surat_izin;
	
	private PendaftaranSuratIzinUsahaProvider(){
		surat_izin = ControllerFactory.getMainController().getCpSuratIzinUsahaDAOImpl().getAllPendaftaranSuratIzinUsaha();
	}
	
	public List<PendaftaranSuratIzinUsaha> getSuratIzin(){
		return surat_izin;
	}
	
	public PendaftaranSuratIzinUsaha getSelectedSuratIzin(Integer index){
		return surat_izin.get(index);
	}
	
	public void addItem(PendaftaranSuratIzinUsaha x){
		surat_izin.add(x);
	}
	
	public void removeItem(int index){
		ControllerFactory.getMainController().getCpSuratIzinUsahaDAOImpl().deleteSuratIzinUsaha(surat_izin.get(index).getId());
		surat_izin.remove(index);
	}
}