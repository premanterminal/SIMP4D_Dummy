package com.dispenda.model;

import java.util.List;
import com.dispenda.controller.ControllerFactory;

public enum PeriksaDetailProvider {
	INSTANCE;
	
	private List<PeriksaDetail> periksaDetail;
	
	private PeriksaDetailProvider(){
		periksaDetail = ControllerFactory.getMainController().getCpPeriksaDetailDAOImpl().getAllPeriksaDetail();
	}
	
	public List<PeriksaDetail> getPeriksaDetail(){
		return periksaDetail;
	}
	
	public PeriksaDetail getSelectedPeriksaDetail(Integer index){
		return periksaDetail.get(index);
	}
	
	public void addItem(PeriksaDetail x){
		periksaDetail.add(x);
	}
	
	public void removeItem(int index){
		ControllerFactory.getMainController().getCpPeriksaDetailDAOImpl().deletePeriksaDetail(periksaDetail.get(index).getIdPeriksaDetail());
		periksaDetail.remove(index);
	}

}
