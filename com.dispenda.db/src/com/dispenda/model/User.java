package com.dispenda.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class User{
	private Integer idUser;
	private String username = "" ;
	private String password = "" ;
	private String displayName = "";
	private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
	
	public User(){}
	
	public User(Integer idUser, String username, String password, String displayName){
		this.idUser = idUser;
		this.username = username;
		this.password = password;
		this.displayName = displayName;
	}
	
	public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeSupport.removePropertyChangeListener(listener);
	}
	
	public void setIdUser(Integer idUser) {
		propertyChangeSupport.firePropertyChange("idUser", this.idUser, this.idUser=idUser);
	}
	
	public void setUsername(String username) {
		propertyChangeSupport.firePropertyChange("username", this.username, this.username=username);
	}
	
	public void setPassword(String password) {
		propertyChangeSupport.firePropertyChange("password", this.password, this.password = password);
	}
	
	public void setDisplayName(String displayName) {
		propertyChangeSupport.firePropertyChange("displayName", this.displayName, this.displayName = displayName);
	}
	
	public Integer getIdUser() {
		return idUser;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public String getDisplayName() {
		return displayName;
	}
}
