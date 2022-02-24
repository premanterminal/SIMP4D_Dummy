package com.dispenda.model;

import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;

public class Kelurahan {
	private Integer idKelurahan;
	private String kodeKelurahan;
	private Integer idKecamatan;
	private String namaKelurahan = "";
	private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
	
	public Kelurahan(){}
	
	public Kelurahan(Integer idKelurahan, String kodeKelurahan, Integer idKecamatan, String namaKelurahan){
		this.idKelurahan = idKelurahan;
		this.kodeKelurahan = kodeKelurahan;
		this.idKecamatan = idKecamatan;
		this.namaKelurahan = namaKelurahan;
	}
	
	public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeSupport.removePropertyChangeListener(listener);
	}
	
	public void setIdKelurahan(Integer idKelurahan){
		propertyChangeSupport.firePropertyChange("idKelurahan", this.idKelurahan, this.idKelurahan=idKelurahan);
	}
	
	public void setKodeKelurahan(String kodeKelurahan){
		propertyChangeSupport.firePropertyChange("kodeKelurahan", this.kodeKelurahan, this.kodeKelurahan=kodeKelurahan);
	}
	
	public void setNamaKelurahan(String namaKelurahan){
		propertyChangeSupport.firePropertyChange("namaKelurahan", this.namaKelurahan, this.namaKelurahan=namaKelurahan);
	}
	
	public void setIdKecamatan(Integer idKecamatan){
		propertyChangeSupport.firePropertyChange("idKecamatan", this.idKecamatan, this.idKecamatan=idKecamatan);
	}
	
	public Integer getIdKelurahan(){
		return idKelurahan;
	}
	
	public String getKodeKelurahan(){
		return kodeKelurahan;
	}
	
	public String getNamaKelurahan(){
		return namaKelurahan;
	}
	
	public Integer getIdKecamatan(){
		return idKecamatan;
	}
}
