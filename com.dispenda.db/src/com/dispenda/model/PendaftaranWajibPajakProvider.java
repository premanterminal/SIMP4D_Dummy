package com.dispenda.model;

import java.util.Date;
import java.util.List;

import com.dispenda.controller.ControllerFactory;

public enum PendaftaranWajibPajakProvider {
	INSTANCE;
	
	private List<PendaftaranWajibPajak> wajib_pajak;
	
	private PendaftaranWajibPajakProvider(){
		
	}
	
	public List<PendaftaranWajibPajak> getWajibPajak(){
		wajib_pajak = ControllerFactory.getMainController().getCpWajibPajakDAOImpl().getAllPendaftaranWajibPajak();
		return wajib_pajak;
	}
	
	public List<PendaftaranWajibPajak> getWajibPajak(Date tglDaftar){
		wajib_pajak = ControllerFactory.getMainController().getCpWajibPajakDAOImpl().getPendaftaranWajibPajak(tglDaftar);
		return wajib_pajak;
	}
	
	public PendaftaranWajibPajak getSelectedWajibPajak(Integer index){
		return wajib_pajak.get(index);
	}
	
	public void addItem(PendaftaranWajibPajak x){
		wajib_pajak.add(x);
	}
	
	public void removeItem(int index){
		ControllerFactory.getMainController().getCpWajibPajakDAOImpl().deleteWajibPajak(wajib_pajak.get(index).getNPWP());
		wajib_pajak.remove(index);
	}
}