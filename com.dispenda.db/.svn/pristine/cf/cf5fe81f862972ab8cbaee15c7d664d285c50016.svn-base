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
	private String namaBadan = "";
	private String alamat = "";
	private String no_skp = "";
	private int statusMohon;
	private String namaPemohon = "";
	private String jabatanPemohon = "";
	private String alamatPemohon = "";
	private Double pokokPajak = 0.0 ;
	private Double denda = 0.0;
	private Double denda_sptpd = 0.0;
	private Double denda_skpdkb = 0.0;
	private Double tot_pajak_terhutang = 0.0;
	private Integer jumlahAngsuran;
	private Double Angsuran = 0.0;
	private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
	
	public Mohon(){}
	
	public Mohon(Integer idMohon, String noMohon, Date tglMohon, Integer jenisMohon, String alasanMohon, Date tglJatuhTempo,
			String npwpd, String no_skp, int statusMohon, String namaPemohon, String jabatanPemohon, String alamatPemohon,
			Double pokokPajak, Double denda, Double denda_sptpd, Double denda_skpdkb, Double tot_pajak_terhutang){
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
		this.pokokPajak = pokokPajak ;
		this.denda = denda ;
		this.denda_sptpd = denda_sptpd ;
		this.denda_skpdkb = denda_skpdkb ;
		this.tot_pajak_terhutang = tot_pajak_terhutang ;
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
	
	public void setStatusMohon(int statusMohon) {
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
	
	public void setPokokPajak(Double pokokPajak) {
		propertyChangeSupport.firePropertyChange("pokokPajak", this.pokokPajak, this.pokokPajak = pokokPajak);
	}
	
	public void setDenda(Double denda) {
		propertyChangeSupport.firePropertyChange("denda", this.denda, this.denda = denda);
	}
	
	public void setDendaSptpd(Double denda_sptpd) {
		propertyChangeSupport.firePropertyChange("denda_sptpd", this.denda_sptpd, this.denda_sptpd = denda_sptpd);
	}
	
	public void setDendaSkpdkb(Double denda_skpdkb) {
		propertyChangeSupport.firePropertyChange("denda_skpdkb", this.denda_skpdkb, this.denda_skpdkb = denda_skpdkb);
	}
	
	public void setTotalPajakTerhutang(Double tot_pajak_terhutang) {
		propertyChangeSupport.firePropertyChange("tot_pajak_terhutang", this.tot_pajak_terhutang, this.tot_pajak_terhutang = tot_pajak_terhutang);
	}
	
	public void setNamaBadan(String namaBadan) {
		this.namaBadan = namaBadan;
	}
	
	public void setAlamat(String alamat) {
		this.alamat = alamat;
	}
	
	public void setJumlahAngsuran(Integer jumlahAngsuran) {
		this.jumlahAngsuran = jumlahAngsuran;
	}
	
	public void setAngsuran(Double angsuran) {
		Angsuran = angsuran;
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
	
	public String getNamaBadan() {
		return namaBadan;
	}
	
	public String getAlamat() {
		return alamat;
	}
	
	public Integer getJumlahAngsuran() {
		return jumlahAngsuran;
	}
	
	public Double getAngsuran() {
		return Angsuran;
	}
	
	public String getNoSkp(){
		return no_skp;
	}
	
	public int getStatusMohon(){
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
	
	public Double getPokokPajak(){
		return pokokPajak;
	}
	
	public Double getDenda(){
		return denda;
	}
	
	public Double getDendaSptpd(){
		return denda_sptpd;
	}
	
	public Double getDendaSkpdkb(){
		return denda_skpdkb;
	}
	
	public Double getTotalPajakTerhutang(){
		return tot_pajak_terhutang;
	}

}
