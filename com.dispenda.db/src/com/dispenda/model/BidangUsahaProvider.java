package com.dispenda.model;

import java.util.List;
import com.dispenda.controller.ControllerFactory;

public enum BidangUsahaProvider {
	INSTANCE;
	
	private List<BidangUsaha> bid_usaha;
	
	private BidangUsahaProvider(){
		
	}
	
	public List<BidangUsaha> getBidangUsaha(){
		bid_usaha = ControllerFactory.getMainController().getCpBidangUsahaDAOImpl().getAllBidangUsaha();
		return bid_usaha;
	}
	
	public List<BidangUsaha> getBidangUsaha(Integer idPajak){
		bid_usaha = ControllerFactory.getMainController().getCpBidangUsahaDAOImpl().getBidangUsaha(idPajak);
		return bid_usaha;
	}
	
	public BidangUsaha getSelectedBidangUsaha(Integer index){
		return bid_usaha.get(index);
	}
	
	public void addItem(BidangUsaha x){
		bid_usaha.add(x);
	}
	
	public void removeItem(int index){
		ControllerFactory.getMainController().getCpBidangUsahaDAOImpl().deleteBidangUsaha(bid_usaha.get(index).getIdSubPajak());
		bid_usaha.remove(index);
	}

}
