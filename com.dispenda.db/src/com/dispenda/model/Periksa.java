package com.dispenda.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.sql.Date;

public class Periksa {
	private Integer idPeriksa = 0;
	private String noPeriksa = "";
	private Date tglPeriksa;
	private String npwpd = "";
	private String jenisPeriksa = "";
	private Integer kenaikanPersen = 0;
	private Double kenaikan;
	private Date masaPajakDari;
	private Date masaPajakSampai;
	private String no_skp = "";
	private String no_skplama = "";
	private Date tgl_skp;
	private String no_nppd = "";
	private Date tgl_nppd;
	private Integer idSubPajak = 0;
	private String tipe_skp = "";
	private Double totalPajakPeriksa;
	private Double totalPajakTerutang;
	private Double totalPajakBunga;
	private Double totalKenaikan;
	private Double totalDenda;
	private String keterangan = "";
	private String masaPajak = "";
	private String bulan = "";
	private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
	
	public Periksa(){}
	
	public Periksa(Integer idPeriksa, String noPeriksa, Date tglPeriksa, String npwpd, String jenisPeriksa,
			Integer kenaikanPersen, Double kenaikan, Date masaPajakDari, Date masaPajakSampai, String no_skp, String no_skplama,
			Date tgl_skp, String no_nppd, Date tgl_nppd, Integer idSubPajak, String tipe_skp, String masaPajak, String bulan,
			Double totalPajakPeriksa, Double totalPajakTerutang, Double totalPajakBunga, Double totalKenaikan, Double totalDenda, String keterangan){
		this.idPeriksa = idPeriksa;
		this.noPeriksa = noPeriksa;
		this.tglPeriksa = tglPeriksa;
		this.npwpd = npwpd;
		this.jenisPeriksa = jenisPeriksa;
		this.kenaikanPersen = kenaikanPersen;
		this.kenaikan = kenaikan;
		this.masaPajakDari = masaPajakDari;
		this.masaPajakSampai = masaPajakSampai;
		this.no_skp = no_skp;
		this.no_skplama = no_skplama;
		this.tgl_skp = tgl_skp;
		this.no_nppd = no_nppd;
		this.tgl_nppd = tgl_nppd;
		this.idSubPajak = idSubPajak;
		this.tipe_skp = tipe_skp;
		this.masaPajak = masaPajak;
		this.bulan = bulan;
		this.totalPajakPeriksa = totalPajakPeriksa;
		this.totalPajakTerutang = totalPajakTerutang;
		this.totalPajakBunga = totalPajakBunga;
		this.totalKenaikan = totalKenaikan;
		this.totalDenda = totalDenda;
		this.keterangan = keterangan;
	}
	
	public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeSupport.removePropertyChangeListener(listener);
	}
	
	public void setIdPeriksa(Integer idPeriksa) {
		propertyChangeSupport.firePropertyChange("idPeriksa", this.idPeriksa, this.idPeriksa = idPeriksa);
	}
	
	public void setNoPeriksa(String noPeriksa) {
		propertyChangeSupport.firePropertyChange("noPeriksa", this.noPeriksa, this.noPeriksa = noPeriksa);
	}
	
	public void setTglPeriksa(Date tglPeriksa) {
		propertyChangeSupport.firePropertyChange("tglPeriksa", this.tglPeriksa, this.tglPeriksa = tglPeriksa);
	}
	
	public void setNpwpd(String npwpd) {
		propertyChangeSupport.firePropertyChange("npwpd", this.npwpd, this.npwpd = npwpd);
	}
	
	public void setJenisPeriksa(String jenisPeriksa) {
		propertyChangeSupport.firePropertyChange("jenisPeriksa", this.jenisPeriksa, this.jenisPeriksa = jenisPeriksa);
	}
	
	public void setMasaPajak(String masaPajak) {
		propertyChangeSupport.firePropertyChange("masaPajak", this.masaPajak, this.masaPajak = masaPajak);
	}

	public void setBulan(String bulan) {
		propertyChangeSupport.firePropertyChange("bulan", this.bulan, this.bulan = bulan);
	}
	
	public void setKenaikanPersen(Integer kenaikanPersen) {
		propertyChangeSupport.firePropertyChange("kenaikanPersen", this.kenaikanPersen, this.kenaikanPersen = kenaikanPersen);
	}
		
	public void setKenaikan(Double kenaikan) {
		propertyChangeSupport.firePropertyChange("kenaikan", this.kenaikan, this.kenaikan = kenaikan);
	}
	
	public void setMasaPajakDari(Date masaPajakDari) {
		propertyChangeSupport.firePropertyChange("masaPajakDari", this.masaPajakDari, this.masaPajakDari = masaPajakDari);
	}
	
	public void setMasaPajakSampai(Date masaPajakSampai) {
		propertyChangeSupport.firePropertyChange("masaPajakSampai", this.masaPajakSampai, this.masaPajakSampai = masaPajakSampai);
	}
		
	public void setNoSkp(String no_skp) {
		propertyChangeSupport.firePropertyChange("no_skp", this.no_skp, this.no_skp = no_skp);
	}
	
	public void setNoSkpLama(String no_skplama) {
		propertyChangeSupport.firePropertyChange("no_skplama", this.no_skplama, this.no_skplama = no_skplama);
	}
	
	public void setTglSkp(Date tgl_skp) {
		propertyChangeSupport.firePropertyChange("tgl_skp", this.tgl_skp, this.tgl_skp = tgl_skp);
	}
	
	public void setNoNppd(String no_nppd) {
		propertyChangeSupport.firePropertyChange("no_nppd", this.no_nppd, this.no_nppd = no_nppd);
	}
		
	public void setTglNppd(Date tgl_nppd) {
		propertyChangeSupport.firePropertyChange("tgl_nppd", this.tgl_nppd, this.tgl_nppd = tgl_nppd);
	}
	
	public void setIdSubPajak(Integer idSubPajak) {
		propertyChangeSupport.firePropertyChange("idSubPajak", this.idSubPajak, this.idSubPajak = idSubPajak);
	}
	
	public void setTipeSkp(String tipe_skp) {
		propertyChangeSupport.firePropertyChange("tipe_skp", this.tipe_skp, this.tipe_skp = tipe_skp);
	}
	
	public void setTotalPajakPeriksa(Double totalPajakPeriksa) {
		propertyChangeSupport.firePropertyChange("totalPajakPeriksa", this.totalPajakPeriksa, this.totalPajakPeriksa = totalPajakPeriksa);
	}
	
	public void setTotalPajakTerutang(Double totalPajakTerutang) {
		propertyChangeSupport.firePropertyChange("totalPajakTerutang", this.totalPajakTerutang, this.totalPajakTerutang = totalPajakTerutang);
	}
	
	public void setTotalPajakBunga(Double totalPajakBunga) {
		propertyChangeSupport.firePropertyChange("totalPajakBunga", this.totalPajakBunga, this.totalPajakBunga = totalPajakBunga);
	}
	
	public void setTotalKenaikan(Double totalKenaikan) {
		propertyChangeSupport.firePropertyChange("totalKenaikan", this.totalKenaikan, this.totalKenaikan = totalKenaikan);
	}
	
	public void setTotalDenda(Double totalDenda) {
		propertyChangeSupport.firePropertyChange("totalDenda", this.totalDenda, this.totalDenda = totalDenda);
	}
	
	public void setKeterangan(String keterangan) {
		propertyChangeSupport.firePropertyChange("keterangan", this.keterangan, this.keterangan = keterangan);
	}
	
	public Integer getIdPeriksa() {
		return idPeriksa;
	}
	
	public String getNoPeriksa() {
		return noPeriksa;
	}
	
	public Date getTglPeriksa() {
		return tglPeriksa;
	}
	
	public String getNpwpd() {
		return npwpd;
	}
	
	public String getJenisPeriksa() {
		return jenisPeriksa;
	}
	
	public String getMasaPajak() {
		return masaPajak;
	}
	
	public String getBulan() {
		return bulan;
	}
	
	public Integer getKenaikanPersen() {
		return kenaikanPersen;
	}
	
	public Double getKenaikan() {
		return kenaikan;
	}
	
	public Date getMasaPajakDari() {
		return masaPajakDari;
	}
	
	public Date getMasaPajakSampai() {
		return masaPajakSampai;
	}
	
	public String getNoSkp() {
		return no_skp;
	}
	
	public String getNoSkplama() {
		return no_skplama;
	}
	
	public Date getTglSkp() {
		return tgl_skp;
	}
	
	public String getNoNppd() {
		return no_nppd;
	}
	
	public Date getTglNppd() {
		return tgl_nppd;
	}
	
	public Integer getIdSubPajak() {
		return idSubPajak;
	}
	
	public String getTipeSkp() {
		return tipe_skp;
	}
	
	public Double getTotalPajakPeriksa() {
		return totalPajakPeriksa;
	}
	
	public Double getTotalPajakTerutang() {
		return totalPajakTerutang;
	}
	
	public Double getTotalPajakBunga() {
		return totalPajakBunga;
	}
	
	public Double getTotalKenaikan() {
		return totalKenaikan;
	}
	
	public Double getTotalDenda() {
		return totalDenda;
	}
	
	public String getKeterangan() {
		return keterangan;
	}
	
	
}
