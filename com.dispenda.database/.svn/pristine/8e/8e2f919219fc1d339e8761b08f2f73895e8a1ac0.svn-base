package com.dispenda.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class BidangUsaha {
	private Integer idsub_pajak = 0 ;
	private String kode_bid_usaha = "" ;
	private String nama_bid_usaha = "" ;
	private Integer id_pajak = 0 ;
	private Integer tarif = 0 ;
	private Integer bunga = 0 ;
	private Integer denda = 0 ;
	private Integer kenaikan_1 = 0 ;
	private Integer kenaikan_2 = 0 ;
	private Integer dlt = 0 ;
	private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
	
	public BidangUsaha(){}
	
	public BidangUsaha(Integer idsub_pajak, String kode_bid_usaha, String nama_bid_usaha, Integer id_pajak, Integer tarif,
			Integer bunga, Integer denda, Integer kenaikan_1, Integer kenaikan_2, Integer dlt){
		this.idsub_pajak = idsub_pajak ;
		this.kode_bid_usaha = kode_bid_usaha;
		this.nama_bid_usaha = nama_bid_usaha;
		this.id_pajak = id_pajak;
		this.tarif = tarif;
		this.bunga = bunga;
		this.denda = denda;
		this.kenaikan_1 = kenaikan_1;
		this.kenaikan_2 = kenaikan_2;
		this.dlt = dlt;
	}
	
	public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeSupport.removePropertyChangeListener(listener);
	}
	
	public void setIdSubPajak(Integer idsub_pajak){
		propertyChangeSupport.firePropertyChange("idsub_pajak", this.idsub_pajak, this.idsub_pajak = idsub_pajak);
	}
	
	public void setKodeBidUsaha(String kode_bid_usaha){
		propertyChangeSupport.firePropertyChange("kode_bid_usaha", this.kode_bid_usaha, this.kode_bid_usaha = kode_bid_usaha);
	}
	
	public void setNamaBidUsaha(String nama_bid_usaha){
		propertyChangeSupport.firePropertyChange("nama_bid_usaha", this.nama_bid_usaha, this.nama_bid_usaha = nama_bid_usaha);
	}
	
	public void setIdPajak(Integer id_pajak){
		propertyChangeSupport.firePropertyChange("id_pajak", this.id_pajak, this.id_pajak = id_pajak);
	}
	
	public void setTarif(Integer tarif){
		propertyChangeSupport.firePropertyChange("tarif", this.tarif, this.tarif = tarif);
	}
	
	public void setBunga(Integer bunga){
		propertyChangeSupport.firePropertyChange("bunga", this.bunga, this.bunga = bunga);
	}
	
	public void setDenda(Integer denda){
		propertyChangeSupport.firePropertyChange("denda", this.denda, this.denda = denda);
	}
	
	public void setKenaikan1(Integer kenaikan_1){
		propertyChangeSupport.firePropertyChange("kenaikan_1", this.kenaikan_1, this.kenaikan_1 = kenaikan_1);
	}
	
	public void setKenaikan2(Integer kenaikan_2){
		propertyChangeSupport.firePropertyChange("kenaikan_2", this.kenaikan_2, this.kenaikan_2 = kenaikan_2);
	}
	
	public void setDLT(Integer dlt){
		propertyChangeSupport.firePropertyChange("dlt", this.dlt, this.dlt = dlt);
	}
	
	public Integer getIdSubPajak(){
		return idsub_pajak;
	}
	
	public String getKodeBidUsaha(){
		return kode_bid_usaha;
	}
	
	public String getNamaBidUsaha(){
		return nama_bid_usaha;
	}
	
	public Integer getIdPajak(){
		return id_pajak;
	}
	
	public Integer getTarif(){
		return tarif;
	}
	
	public Integer getBunga(){
		return bunga;
	}
	
	public Integer getDenda(){
		return denda;
	}
	
	public Integer getKenaikan1(){
		return kenaikan_1;
	}
	
	public Integer getKenaikan2(){
		return kenaikan_2;
	}
	
	public Integer getDLT(){
		return dlt;
	}
}
