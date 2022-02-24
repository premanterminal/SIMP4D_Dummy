package com.dispenda.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class UU {
	private Integer idUU;
	private Integer idPajak;
	private String undang_undang;
	private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
	
	public UU(){}
	
	public UU(Integer idUU, Integer idPajak, String undang_undang){
		this.idUU = idUU;
		this.idPajak = idPajak;
		this.undang_undang = undang_undang;
	}
	
	public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeSupport.removePropertyChangeListener(listener);
	}
	
	public void setIdUU(Integer idUU) {
		propertyChangeSupport.firePropertyChange("idUU", this.idUU, this.idUU = idUU);
	}
	
	public void setIdPajak(Integer idPajak) {
		propertyChangeSupport.firePropertyChange("idPajak", this.idPajak, this.idPajak = idPajak);
	}
	
	public void setUU(String undang_undang) {
		propertyChangeSupport.firePropertyChange("undang_undang", this.undang_undang, this.undang_undang = undang_undang);
	}
	
	public Integer getIdUU(){
		return idUU;
	}
	
	public Integer getIdPajak(){
		return idPajak;
	}
	
	public String getUU(){
		return undang_undang;
	}
}
