package com.dispenda.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.sql.Date;


public class WpTutup {
	private Integer idTutup = 0 ;
	private String noSuratTutup = "" ;
	private Date tglMulaiTutup;
	private Date tglSampaiTutup;
	private String npwpd = "" ;
	private String keterangan = "" ;
	private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
	
	public WpTutup(){}
	
	public WpTutup(Integer idTutup, String noSuratTutup, Date tglMulaiTutup, Date tglSampaiTutup, String npwp, String keterangan){
		this.idTutup = idTutup ;
		this.noSuratTutup = noSuratTutup ;
		this.tglMulaiTutup = tglMulaiTutup ;
		this.tglSampaiTutup = tglSampaiTutup ;
		this.npwpd = npwp ;
		this.keterangan = keterangan ;
	}
	
	public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeSupport.removePropertyChangeListener(listener);
	}
	
	public void setIdTutup(Integer idTutup){
		propertyChangeSupport.firePropertyChange("idTutup", this.idTutup, this.idTutup = idTutup);
	}
	
	public void setNoSuratTutup(String noSuratTutup){
		propertyChangeSupport.firePropertyChange("noSuratTutup", this.noSuratTutup, this.noSuratTutup = noSuratTutup);
	}
	
	public void setTglMulaiTutup(Date tglMulaiTutup){
		propertyChangeSupport.firePropertyChange("tglMulaiTutup", this.tglMulaiTutup, this.tglMulaiTutup = tglMulaiTutup);
	}
	
	public void setTglSampaiTutup(Date tglSampaiTutup){
		propertyChangeSupport.firePropertyChange("tglSampaiTutup", this.tglSampaiTutup, this.tglSampaiTutup = tglSampaiTutup);
	}
	
	public void setNpwpd(String npwpd){
		propertyChangeSupport.firePropertyChange("npwpd", this.npwpd, this.npwpd = npwpd);
	}
	
	public void setKeterangan(String keterangan){
		propertyChangeSupport.firePropertyChange("keterangan", this.keterangan, this.keterangan = keterangan);
	}
	
	public Integer getIdTutup(){
		return idTutup;
	}
	
	public String getNoSuratTutup(){
		return noSuratTutup;
	}
	
	public Date getTglMulaiTutup(){
		return tglMulaiTutup;
	}
	
	public Date getTglSampaiTutup(){
		return tglSampaiTutup;
	}
	
	public String getNpwpd(){
		return npwpd;
	}
	
	public String getKeterangan(){
		return keterangan;
	}

}
