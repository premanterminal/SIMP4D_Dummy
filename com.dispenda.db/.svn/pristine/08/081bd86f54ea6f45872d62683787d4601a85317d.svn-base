package com.dispenda.model;

import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;

public class Kecamatan {
	private Integer idKecamatan;
	private String namaKecamatan = "";
	private String kodeKecamatan = "";
	private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
	
	public Kecamatan(){}
	
	public Kecamatan(Integer idKecamatan, String namaKecamatan, String kodeKecamatan){
		this.idKecamatan = idKecamatan;
		this.namaKecamatan = namaKecamatan;
		this.kodeKecamatan = kodeKecamatan;
	}
	
	public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeSupport.removePropertyChangeListener(listener);
	}
	
	public void setIdKecamatan(Integer idKecamatan){
		propertyChangeSupport.firePropertyChange("idKecamatan", this.idKecamatan, this.idKecamatan=idKecamatan);
	}
	
	public void setKodeKecamatan(String kodeKecamatan){
		propertyChangeSupport.firePropertyChange("kodeKecamatan", this.kodeKecamatan, this.kodeKecamatan=kodeKecamatan);
	}
	
	public void setNamaKecamatan(String namaKecamatan){
		propertyChangeSupport.firePropertyChange("namaKecamatan", this.namaKecamatan, this.namaKecamatan=namaKecamatan);
	}
	
	public Integer getIdKecamatan(){
		return idKecamatan;
	}
	
	public String getKodeKecamatan(){
		return kodeKecamatan;
	}
	
	public String getNamaKecamatan(){
		return namaKecamatan;
	}
}
