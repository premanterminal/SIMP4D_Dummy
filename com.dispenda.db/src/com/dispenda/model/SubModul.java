package com.dispenda.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class SubModul extends Modul {
	private Integer idSubModul ;
	private String namaSubModul = "";
	private Integer idModul ;
	private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
	
	public SubModul(){}
	
	public SubModul(Integer idSubModul, String namaSubModul, Integer idModul){
		this.idSubModul = idSubModul;
		this.namaSubModul = namaSubModul;
		this.idModul = idModul;
	}
	
	public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeSupport.removePropertyChangeListener(listener);
	}
	
	public void setIdSubModul(Integer idSubModul) {
		propertyChangeSupport.firePropertyChange("idSubModul", this.idSubModul, this.idSubModul=idSubModul);
	}
	
	public void setNamaSubModul(String namaSubModul) {
		propertyChangeSupport.firePropertyChange("namaSubModul", this.namaSubModul, this.namaSubModul = namaSubModul);
	}
	
	public void setIdModul(Integer idModul) {
		propertyChangeSupport.firePropertyChange("idModul", this.idModul, this.idModul=idModul);
	}
	
	public Integer getIdSubModul() {
		return idSubModul;
	}
	
	public String getNamaSubModul() {
		return namaSubModul;
	}
	
	public Integer getIdModul() {
		return idModul;
	}

}