package com.dispenda.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.sql.Date;

public class Sspd {
	private String no_sspd = "" ;
	private Date tgl_sspd ;
	private String npwpd = "" ;
	private Date bulan_skp ;
	private String no_skp = "" ;
	private Integer cicilan_ke = 0 ;
	private Double denda ;
	private Double jumlah_bayar ;
	private Integer jenis_bayar;
	private String cara_bayar ;
	private String nama_penyetor ;
	private boolean lunas;
	private Integer idsub_pajak = 0 ;
	private boolean denda_lunas ;
	private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
	
	public Sspd(){}
	
	public Sspd(String no_sspd, Date tgl_sspd, String npwpd, Date bulan_skp, String no_skp,
			Integer cicilan_ke, Double denda, Double jumlah_bayar, Integer jenis_bayar, String cara_bayar,
			String nama_penyetor, boolean lunas, Integer idsub_pajak, boolean denda_lunas){
		this.no_sspd = no_sspd;
		this.tgl_sspd = tgl_sspd;
		this.npwpd = npwpd;
		this.bulan_skp = bulan_skp;
		this.no_skp = no_skp;
		this.cicilan_ke = cicilan_ke;
		this.denda = denda;
		this.jumlah_bayar = jumlah_bayar;
		this.jenis_bayar = jenis_bayar;
		this.cara_bayar = cara_bayar;
		this.nama_penyetor = nama_penyetor;
		this.lunas = lunas;
		this.idsub_pajak = idsub_pajak;
		this.denda_lunas = denda_lunas;
	}
	
	public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeSupport.removePropertyChangeListener(listener);
	}
	
	public void setNoSspd(String no_sspd){
		propertyChangeSupport.firePropertyChange("no_sspd", this.no_sspd, this.no_sspd = no_sspd);
	}
	
	public void setTglSspd(Date tgl_sspd){
		propertyChangeSupport.firePropertyChange("tgl_sspd", this.tgl_sspd, this.tgl_sspd = tgl_sspd);
	}
	
	public void setNpwpd(String npwpd){
		propertyChangeSupport.firePropertyChange("npwpd", this.npwpd, this.npwpd = npwpd);
	}
	
	public void setBulanSkp(Date bulan_skp){
		propertyChangeSupport.firePropertyChange("bulan_skp", this.bulan_skp, this.bulan_skp = bulan_skp);
	}
	
	public void setNoSkp(String no_skp){
		propertyChangeSupport.firePropertyChange("no_skp", this.no_skp, this.no_skp = no_skp);
	}
	
	public void setCicilanKe(Integer cicilan_ke){
		propertyChangeSupport.firePropertyChange("cicilan_ke", this.cicilan_ke, this.cicilan_ke = cicilan_ke);
	}
	
	public void setDenda(Double denda){
		propertyChangeSupport.firePropertyChange("denda", this.denda, this.denda = denda);
	}
	
	public void setJumlahBayar(Double jumlah_bayar){
		propertyChangeSupport.firePropertyChange("jumlah_bayar", this.jumlah_bayar, this.jumlah_bayar = jumlah_bayar);
	}
	
	public void setJenisBayar(Integer jenis_bayar){
		propertyChangeSupport.firePropertyChange("jenis_bayar", this.jenis_bayar, this.jenis_bayar = jenis_bayar);
	}
	
	public void setCaraBayar(String cara_bayar){
		propertyChangeSupport.firePropertyChange("cara_bayar", this.cara_bayar, this.cara_bayar = cara_bayar);
	}
	
	public void setNamaPenyetor(String nama_penyetor){
		propertyChangeSupport.firePropertyChange("nama_penyetor", this.nama_penyetor, this.nama_penyetor = nama_penyetor);
	}
	
	public void setLunas(boolean lunas){
		propertyChangeSupport.firePropertyChange("lunas", this.lunas, this.lunas = lunas);
	}
	
	public void setIdSubPajak(Integer idsub_pajak){
		propertyChangeSupport.firePropertyChange("idsub_pajak", this.idsub_pajak, this.idsub_pajak = idsub_pajak);
	}
	
	public void setDendaLunas(boolean denda_lunas){
		propertyChangeSupport.firePropertyChange("denda_lunas", this.denda_lunas, this.denda_lunas = denda_lunas);
	}
	
	public String getNoSspd(){
		return no_sspd;
	}
	
	public Date getTglSspd(){
		return tgl_sspd;
	}
	
	public String getNpwpd(){
		return npwpd;
	}
	
	public Date getBulanSkp(){
		return bulan_skp;
	}
	
	public String getNoSkp(){
		return no_skp;
	}
	
	public Integer getCicilanKe(){
		return cicilan_ke;
	}
	
	public Double getDenda(){
		return denda;
	}
	
	public Double getJumlahBayar(){
		return jumlah_bayar;
	}
	
	public Integer getJenisBayar(){
		return jenis_bayar;
	}
	
	public String getCaraBayar(){
		return cara_bayar;
	}
	
	public String getNamaPenyetor(){
		return nama_penyetor;
	}
	
	public boolean getLunas(){
		return lunas;
	}
	
	public Integer getIdSubPajak(){
		return idsub_pajak;
	}
	
	public boolean getDendaLunas(){
		return denda_lunas;
	}
}

