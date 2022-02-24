package com.dispenda.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class Modul {
	private Integer idModul;
	private String namaModul = "";
	private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
	
	public Modul(){}
	
	public Modul(Integer idModul, String namaModul){
		this.idModul = idModul;
		this.namaModul = namaModul;
	}
	
	public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeSupport.removePropertyChangeListener(listener);
	}
	
	public void setIdModul(Integer idModul) {
		propertyChangeSupport.firePropertyChange("idModul", this.idModul, this.idModul=idModul);
	}
	
	public void setNamaModul(String namaModul) {
		propertyChangeSupport.firePropertyChange("namaModul", this.namaModul, this.namaModul = namaModul);
	}
	
	public Integer getIdModul() {
		return idModul;
	}
	
	public String getNamaModul() {
		return namaModul;
	}
}