package com.dispenda.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class UserModul extends User{
	private Integer index = 1 ;
	private Integer id;
	private Integer idUser;
	private Integer idSubModul;
	private Boolean buka;
	private Boolean simpan;
	private Boolean hapus;
	private Boolean cetak;
	private SubModul subModul = new SubModul();
	
	private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

	public UserModul(){}
	
	public UserModul(Integer index, Integer idUser, Integer idSubModul, Boolean buka, Boolean simpan,
			Boolean hapus, Boolean cetak){
		this.index = index;
		this.idUser = idUser;
		this.idSubModul = idSubModul;
		this.buka = buka;
		this.simpan = simpan;
		this.hapus = hapus;
		this.cetak = cetak;
	}
	
	public SubModul getSubModul() {
		return subModul;
	}
	
	public void setSubModul(SubModul subModul) {
		this.subModul = subModul;
	}
	
	public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeSupport.removePropertyChangeListener(listener);
	}
	
	public void setIndex(Integer index){
		propertyChangeSupport.firePropertyChange("index", this.index, this.index = index);
	}
	
	public void setIdUser(Integer idUser) {
		propertyChangeSupport.firePropertyChange("idUser", this.idUser, this.idUser=idUser);
	}
	
	public void setIdSubModul(Integer idSubModul) {
		propertyChangeSupport.firePropertyChange("idSubModul", this.idSubModul, this.idSubModul=idSubModul);
	}
	
	public void setBuka(Boolean buka) {
		propertyChangeSupport.firePropertyChange("buka", this.buka, this.buka=buka);
	}
	
	public void setSimpan(Boolean simpan) {
		propertyChangeSupport.firePropertyChange("simpan", this.simpan, this.simpan=simpan);
	}
	
	public void setHapus(Boolean hapus) {
		propertyChangeSupport.firePropertyChange("hapus", this.hapus, this.hapus=hapus);
	}
	
	public void setCetak(Boolean cetak) {
		propertyChangeSupport.firePropertyChange("cetak", this.cetak, this.cetak=cetak);
	}
	
	public void setId(Integer id) {
		propertyChangeSupport.firePropertyChange("id", this.id, this.id=id);
	}
	
	public Integer getIndex(){
		return index;
	}
	
	public Integer getIdUser() {
		return idUser;
	}
	
	public Integer getIdSubModul() {
		return idSubModul;
	}
	
	public Boolean getBuka() {
		return buka;
	}
	
	public Boolean getCetak() {
		return cetak;
	}
	
	public Boolean getHapus() {
		return hapus;
	}
	
	public Integer getId() {
		return id;
	}
	
	public Boolean getSimpan() {
		return simpan;
	}
}