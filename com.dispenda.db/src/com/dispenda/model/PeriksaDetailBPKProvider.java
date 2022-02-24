package com.dispenda.model;

import java.util.ArrayList;
import java.util.List;
import com.dispenda.controller.ControllerFactory;

public enum PeriksaDetailBPKProvider {
	INSTANCE;
	
	private List<PeriksaDetailBPK> periksaDetail = new ArrayList<PeriksaDetailBPK>();
	
	private PeriksaDetailBPKProvider(){
//		periksaDetail = ControllerFactory.getMainController().getCpPeriksaDetailBPKDAOImpl().getAllPeriksaDetail();
	}
	
	public List<PeriksaDetailBPK> getPeriksaDetail(){
		return periksaDetail;
	}
	
	public PeriksaDetailBPK getSelectedPeriksaDetail(Integer index){
		return periksaDetail.get(index);
	}
	
	public void addItem(PeriksaDetailBPK x){
		periksaDetail.add(x);
	}
	
	public void removeItem(int index){
		ControllerFactory.getMainController().getCpPeriksaDetailBPKDAOImpl().deletePeriksaDetail(periksaDetail.get(index).getIdPeriksaDetail());
		periksaDetail.remove(index);
	}
	
	public void removeAll(){
		periksaDetail.removeAll(periksaDetail);
	}

}
