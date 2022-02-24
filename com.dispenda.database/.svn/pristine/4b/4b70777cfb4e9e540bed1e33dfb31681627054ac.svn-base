package com.dispenda.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class Pajak {
	private Integer idPajak = 0;
	private String kode_pajak = "";
	private String nama_pajak = "";
	private String kode_denda = "";
	private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
	
	public Pajak(){}
	
	public Pajak(Integer idPajak, String kode_pajak, String nama_pajak, String kode_denda){
		this.idPajak = idPajak;
		this.kode_pajak = kode_pajak;
		this.nama_pajak = nama_pajak;
		this.kode_denda = kode_denda;
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
