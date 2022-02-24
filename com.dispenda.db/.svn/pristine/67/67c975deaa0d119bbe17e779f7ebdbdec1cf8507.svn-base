package com.dispenda.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Pajak {
	private Integer idPajak = 0;
	private String kode_pajak = "";
	private String nama_pajak = "";
	private String kode_denda = "";
	private Double pokok = new Double(0.0);
	private Double denda = new Double(0.0);
	private Double jumlah = new Double(0.0);
	private List<BidangUsaha> bidUsaha = new ArrayList<BidangUsaha>();
	private Bulan bulan;
	private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
	
	
	
	public boolean addBidangUsaha(BidangUsaha bidUsaha){
		boolean add = this.bidUsaha.add(bidUsaha);
		if(add)
			bidUsaha.setPajak(this);
		return add;
	}
	
	public List<BidangUsaha> getBidUsaha() {
		return Collections.unmodifiableList(bidUsaha);
	}
	
	public void setBulan(Bulan bulan) {
		this.bulan = bulan;
	}
	
	public Bulan getBulan() {
		return bulan;
	}
	
	public void setPokok(Double pokok) {
		this.pokok = pokok;
	}
	
	public void setDenda(Double denda) {
		this.denda = denda;
	}
	
	public void setJumlah(Double jumlah) {
		this.jumlah = jumlah;
	}
	
	public Double getPokok() {
		return pokok;
	}
	
	public Double getDenda() {
		return denda;
	}
	
	public Double getJumlah() {
		return jumlah;
	}
	
	public Pajak(){}
	
	public Pajak(Integer idPajak, String kode_pajak, String nama_pajak, String kode_denda){
		this.idPajak = idPajak;
		this.kode_pajak = kode_pajak;
		this.nama_pajak = nama_pajak;
		this.kode_denda = kode_denda;
	}
	
	public void setBidUsaha(List<BidangUsaha> bidUsaha) {
		this.bidUsaha = bidUsaha;
	}
	
	public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeSupport.removePropertyChangeListener(listener);
	}
	
	public void setIdPajak(Integer idPajak){
		propertyChangeSupport.firePropertyChange("idPajak", this.idPajak, this.idPajak = idPajak);
	}
	
	public void setKodePajak(String kode_pajak){
		propertyChangeSupport.firePropertyChange("kode_pajak", this.kode_pajak, this.kode_pajak = kode_pajak);
	}
	
	public void setNamaPajak(String nama_pajak){
		propertyChangeSupport.firePropertyChange("nama_pajak", this.nama_pajak, this.nama_pajak = nama_pajak);
	}
	
	public void setKodeDenda(String kode_denda){
		propertyChangeSupport.firePropertyChange("kode_denda", this.kode_denda, this.kode_denda = kode_denda);
	}
	
	public Integer getIdPajak(){
		return idPajak;
	}
	
	public String getKodePajak(){
		return kode_pajak;
	}
	
	public String getNamaPajak(){
		return nama_pajak;
	}
	
	public String getKodeDenda(){
		return kode_denda;
	}

}
