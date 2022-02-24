package com.dispenda.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class Pejabat {
	private Integer idpejabat_nip = 0;
	private String nama_pejabat = "";
	private String pangkat = "";
	private String jabatan = "";
	private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
	
	public Pejabat(){}
	
	public Pejabat(Integer idpejabat_nip, String nama_pejabat, String pangkat, String jabatan){
		this.idpejabat_nip = idpejabat_nip;
		this.nama_pejabat = nama_pejabat;
		this.pangkat = pangkat;
		this.jabatan = jabatan;
	}
	
	public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeSupport.removePropertyChangeListener(listener);
	}
	
	public void setIdPejabatNIP(Integer idpejabat_nip){
		propertyChangeSupport.firePropertyChange("idpejabat_nip", this.idpejabat_nip, this.idpejabat_nip = idpejabat_nip);
	}
	
	public void setNamaPejabat(String nama_pejabat){
		propertyChangeSupport.firePropertyChange("nama_pejabat", this.nama_pejabat, this.nama_pejabat = nama_pejabat);
	}
	
	public void setPangkat(String pangkat){
		propertyChangeSupport.firePropertyChange("pangkat", this.pangkat, this.pangkat = pangkat);
	}
	
	public void setJabatan(String jabatan){
		propertyChangeSupport.firePropertyChange("jabatan", this.jabatan, this.jabatan = jabatan);
	}
	
	public Integer getIdPejabatNIP(){
		return idpejabat_nip;
	}
	
	public String getNamaPejabat(){
		return nama_pejabat;
	}
	
	public String getPangkat(){
		return pangkat;
	}
	
	public String getJabatan(){
		return jabatan;
	}

}
