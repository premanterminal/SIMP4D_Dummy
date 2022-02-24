package com.dispenda.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class PendaftaranSuratIzinUsaha{
	private Integer id = 0 ;
	private String npwp = "" ;
	private String no_surat = "" ;
	private String surat_izin = "";
	private String tanggal_surat;
	private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
	
	public PendaftaranSuratIzinUsaha(){}
	
	public PendaftaranSuratIzinUsaha(Integer index, Integer id, String npwp, String no_surat, String surat_izin, String tanggal_surat)
	{
		this.id = id;
		this.npwp = npwp;
		this.no_surat = no_surat;
		this.surat_izin = surat_izin;
		this.tanggal_surat = tanggal_surat;
	}
	
	public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener)
	{
		propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
	}
	
	public void removePropertyChangeListener(PropertyChangeListener listener)
	{
		propertyChangeSupport.removePropertyChangeListener(listener);
	}
		
	public void setId(Integer id)
	{
		propertyChangeSupport.firePropertyChange("id", this.id, this.id = id);
	}
	
	public void setNPWP(String npwp)
	{
		propertyChangeSupport.firePropertyChange("npwp", this.npwp, this.npwp = npwp);
	}
	
	public void setNoSurat(String no_surat)
	{
		propertyChangeSupport.firePropertyChange("no_surat", this.no_surat, this.no_surat = no_surat);
	}
	
	public void setSuratIzin(String surat_izin)
	{
		propertyChangeSupport.firePropertyChange("surat_izin", this.surat_izin, this.surat_izin = surat_izin);
	}
	
	public void setTanggalSurat(String tanggal_surat)
	{
		propertyChangeSupport.firePropertyChange("tanggal_surat", this.tanggal_surat, this.tanggal_surat = tanggal_surat);
	}
		
	public Integer getId(){
		return id;
	}
	
	public String getNPWP(){
		return npwp;
	}
	
	public String getNoSurat(){
		return no_surat;
	}
	
	public String getSuratIzin(){
		return surat_izin;
	}
	
	public String getTanggalSurat(){
		return tanggal_surat;
	}
}