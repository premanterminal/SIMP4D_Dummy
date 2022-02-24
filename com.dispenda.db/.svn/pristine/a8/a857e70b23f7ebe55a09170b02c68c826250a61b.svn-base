package com.dispenda.model;

import java.util.ArrayList;
import java.util.List;

import com.dispenda.controller.ControllerFactory;

public enum MohonDetailProvider {
	INSTANCE;
	
	private List<MohonDetail> mohonDetail = new ArrayList<MohonDetail>();
	
	private MohonDetailProvider(){
		mohonDetail = ControllerFactory.getMainController().getCpMohonDetailDAOImpl().getAllMohonDetail();
	}
	
	public List<MohonDetail> getMohonDetail(Integer idMohon){
		mohonDetail = ControllerFactory.getMainController().getCpMohonDetailDAOImpl().getDaftarAngsuran(idMohon);
		return mohonDetail;
	}
	
	public List<MohonDetail> getMohonDetail(){
		return mohonDetail;
	}
	
	public MohonDetail getSelectedMohonDetail(Integer index){
		return mohonDetail.get(index);
	}
	
	public void addItem(MohonDetail x){
		mohonDetail.add(x);
	}
	
	public void removeItem(int index){
		ControllerFactory.getMainController().getCpMohonDetailDAOImpl().deleteMohonDetail(mohonDetail.get(index).getIdMohonDetail());
		mohonDetail.remove(index);
	}

}
