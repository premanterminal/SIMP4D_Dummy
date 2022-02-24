package com.dispenda.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.sql.Date;


public class MohonDetail {
	private Integer idMohonDetail = 0;
	private Integer idMohon = 0;
	private Integer noAngsuran = 0;
	private Date tglAngsuran;
	private Double angsuranPokok;
	private Double biayaAdministrasi;
	private Double dendaSkpdkb;
	private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
	
	public MohonDetail(){}
	
	public MohonDetail(Integer idMohonDetail, Integer idMohon, Integer noAngsuran, 
			Date tglAngsuran, Double angsuranPokok, Double biayaAdministrasi, Double dendaSkpdkb){
		this.idMohonDetail = idMohonDetail;
		this.idMohon = idMohon;
		this.noAngsuran = noAngsuran;
		this.tglAngsuran = tglAngsuran;
		this.angsuranPokok = angsuranPokok;
		this.biayaAdministrasi = biayaAdministrasi;
		this.dendaSkpdkb = dendaSkpdkb;
	}
	
	public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeSupport.removePropertyChangeListener(listener);
	}
	
	public void setIdMohonDetail(Integer idMohonDetail) {
		propertyChangeSupport.firePropertyChange("idMohonDetail", this.idMohonDetail, this.idMohonDetail = idMohonDetail);
	}
	
	public void setIdMohon(Integer idMohon) {
		propertyChangeSupport.firePropertyChange("idMohon", this.idMohon, this.idMohon = idMohon);
	}
	
	public void setNoAngsuran(Integer noAngsuran) {
		propertyChangeSupport.firePropertyChange("noAngsuran", this.noAngsuran, this.noAngsuran = noAngsuran);
	}
	
	public void setTglAngsuran(Date tglAngsuran) {
		propertyChangeSupport.firePropertyChange("tglAngsuran", this.tglAngsuran, this.tglAngsuran = tglAngsuran);
	}
	
	public void setAngsuranPokok(Double angsuranPokok) {
		propertyChangeSupport.firePropertyChange("angsuranPokok", this.angsuranPokok, this.angsuranPokok = angsuranPokok);
	}
	
	public void setBiayaAdministrasi(Double biayaAdministrasi) {
		propertyChangeSupport.firePropertyChange("biayaAdministrasi", this.biayaAdministrasi, this.biayaAdministrasi = biayaAdministrasi);
	}
	
	public void setDendaSkpdkb(Double dendaSkpdkb) {
		propertyChangeSupport.firePropertyChange("dendaSkpdkb", this.dendaSkpdkb, this.dendaSkpdkb = dendaSkpdkb);
	}
	
	public Integer getIdMohonDetail(){
		return idMohonDetail;
	}
	
	public Integer getIdMohon(){
		return idMohon;
	}
	
	public Integer getNoAngsuran(){
		return noAngsuran;
	}
	
	public Date getTglAngsuran(){
		return tglAngsuran;
	}
	
	public Double getAngsuranPokok(){
		return angsuranPokok;
	}
	
	public Double getBiayaAdministrasi(){
		return biayaAdministrasi;
	}
	
	public Double getDendaSkpdkb(){
		return dendaSkpdkb;
	}
	

}
