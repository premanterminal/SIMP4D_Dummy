package com.dispenda.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.sql.Date;

public class Mohon {
	private Integer idMohon = 0;
	private String noMohon = "";
	private Date tglMohon;
	private Integer jenisMohon = 0;
	private String alasanMohon = "";
	private Date tglJatuhTempo;
	private String npwpd = "";
	private String no_skp = "";
	private boolean statusMohon;
	private String namaPemohon = "";
	private String jabatanPemohon = "";
	private String alamatPemohon = "";
	private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
	
	public Mohon(){}
	
	public Mohon(Integer idMohon, String noMohon, Date tglMohon, Integer jenisMohon, String alasanMohon, Date tglJatuhTempo,
			String npwpd, String no_skp, boolean statusMohon, String namaPemohon, String jabatanPemohon, String alamatPemohon){
		this.idMohon = idMohon ;
		this.noMohon = noMohon ;
		this.tglMohon = tglMohon ;
		this.jenisMohon = jenisMohon ;
		this.alasanMohon = alasanMohon ;
		this.tglJatuhTempo = tglJatuhTempo ;
		this.npwpd = npwpd ;
		this.no_skp = no_skp ;
		this.statusMohon = statusMohon ;
		this.namaPemohon = namaPemohon ;
		this.jabatanPemohon = jabatanPemohon ;
		this.alamatPemohon = alamatPemohon ;
	}
	
	public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeSupport.removePropertyChangeListener(listener);
	}
	
	public void setIdMohon(Integer idMohon) {
		propertyChangeSupport.firePropertyChange("idMohon", this.idMohon, this.idMohon = idMohon);
	}
	
	public void setNoMohon(String noMohon) {
		propertyChangeSupport.firePropertyChange("noMohon", this.noMohon, this.noMohon = noMohon);
	}
	
	public void setTglMohon(Date tglMohon) {
		propertyChangeSupport.firePropertyChange("tglMohon", this.tglMohon, this.tglMohon = tglMohon);
	}
	
	public void setJenisMohon(Integer jenisMohon) {
		propertyChangeSupport.firePropertyChange("jenisMohon", this.jenisMohon, this.jenisMohon = jenisMohon);
	}
	
	public void setAlasanMohon(String alasanMohon) {
		propertyChangeSupport.firePropertyChange("alasanMohon", this.alasanMohon, this.alasanMohon = alasanMohon);
	}
	
	public void setTglJatuhTempo(Date tglJatuhTempo) {
		propertyChangeSupport.firePropertyChange("tglJatuhTempo", this.tglJatuhTempo, this.tglJatuhTempo = tglJatuhTempo);
	}
	
	public void setNpwpd(String npwpd) {
		propertyChangeSupport.firePropertyChange("npwpd", this.npwpd, this.npwpd = npwpd);
	}
	
	public void setNoSkp(String no_skp) {
		propertyChangeSupport.firePropertyChange("no_skp", this.no_skp, this.no_skp = no_skp);
	}
	
	public void setStatusMohon(boolean statusMohon) {
		propertyChangeSupport.firePropertyChange("statusMohon", this.statusMohon, this.statusMohon = statusMohon);
	}
	
	public void setNamaPemohon(String namaPemohon) {
		propertyChangeSupport.firePropertyChange("namaPemohon", this.namaPemohon, this.namaPemohon = namaPemohon);
	}
	
	public void setJabatanPemohon(String jabatanPemohon) {
		propertyChangeSupport.firePropertyChange("jabatanPemohon", this.jabatanPemohon, this.jabatanPemohon = jabatanPemohon);
	}
	
	public void setAlamatPemohon(String alamatPemohon) {
		propertyChangeSupport.firePropertyChange("alamatPemohon", this.alamatPemohon, this.alamatPemohon = alamatPemohon);
	}
	
	public Integer getIdMohon(){
		return idMohon;
	}
	
	public String getNoMohon(){
		return noMohon;
	}
	
	public Date getTglMohon(){
		return tglMohon;
	}
	
	public Integer getJenisMohon(){
		return jenisMohon;
	}
	
	public String getAlasanMohon(){
		return alasanMohon;
	}
	
	public Date getTglJatuhTempo(){
		return tglJatuhTempo;
	}
	
	public String getNpwpd(){
		return npwpd;
	}
	
	public String getNoSkp(){
		return no_skp;
	}
	
	public boolean getStatusMohon(){
		return statusMohon;
	}
	 
	public String getNamaPemohon(){
		return namaPemohon;
	}
	
	public String getJabatanPemohon(){
		return jabatanPemohon;
	}
	
	public String getAlamatPemohon(){
		return alamatPemohon;
	}
	

}
