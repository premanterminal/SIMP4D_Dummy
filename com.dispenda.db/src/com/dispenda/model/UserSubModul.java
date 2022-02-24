package com.dispenda.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class UserSubModul{
	private Integer index = 1;
	private String username = "";
	private Integer idSubModul;
	private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

	public UserSubModul(){}
	
	public UserSubModul(Integer index, String username, Integer idSubModul){
		this.index = index;
		this.username = username;
		this.idSubModul = idSubModul;
	}
	
	public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeSupport.removePropertyChangeListener(listener);
	}
	
	public void setIndex(Integer index){
		propertyChangeSupport.firePropertyChange("index", this.index, this.index=index);
	}
	
	public void setUsername(String username) {
		propertyChangeSupport.firePropertyChange("username", this.username, this.username=username);
	}
	
	public void setIdSubModul(Integer idSubModul) {
		propertyChangeSupport.firePropertyChange("idSubModul", this.idSubModul, this.idSubModul=idSubModul);
	}
	
	public Integer getIndex(){
		return index;
	}
	
	public String getUsername() {
		return username;
	}
	
	public Integer getIdSubModul() {
		return idSubModul;
	}
}