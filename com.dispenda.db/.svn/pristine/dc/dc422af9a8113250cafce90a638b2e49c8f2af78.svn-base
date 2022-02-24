package com.dispenda.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class PeriksaDetailBPK {
	private Integer idPeriksaDetail = 0;
	private Integer idPeriksa = 0;
	private String npwpd = "";
	private String bulan = "";
	private String masaPajak = "";
	private Double dasarPengenaan;
	private Double pengenaanPeriksa;
	private Double selisihKurang;
	private Integer tarif;
	private Double pajakTerutang;
	private Double jumlahBayar;
	private Double pajakPeriksa;
	private Double kurangBayar;
	private Integer bungaPersen = 0;
	private Double bunga;
	private Double dendaTambahan;
	private Double kenaikan;
	private Double total;
	private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
	
	public PeriksaDetailBPK(){}
	
	public PeriksaDetailBPK(Integer idPeriksaDetail, Integer idPeriksa, String npwpd, String bulanPeriksa,  Double dasarPengenaan,
			Double pengenaanPeriksa, Double pajakPeriksa, Double selisihKurang, Integer bungaPersen, Double bunga, Double kenaikan, Double totalPeriksa,
			String bulan, String masaPajak, Integer tarif, Double pajakTerutang, Double jumlahBayar, Double kurangBayar, Double dendaTambahan, Double total){
		this.idPeriksaDetail = idPeriksaDetail;
		this.idPeriksa = idPeriksa;
		this.npwpd = npwpd;
		this.bulan = bulan;
		this.dasarPengenaan = dasarPengenaan;
		this.pengenaanPeriksa = pengenaanPeriksa;
		this.pajakPeriksa = pajakPeriksa;
		this.selisihKurang = selisihKurang;
		this.bungaPersen = bungaPersen;
		this.bunga = bunga;
		this.kenaikan = kenaikan;
		this.masaPajak = masaPajak;
		this.tarif = tarif;
		this.pajakTerutang = pajakTerutang;
		this.jumlahBayar = jumlahBayar;
		this.kurangBayar = kurangBayar;
		this.dendaTambahan = dendaTambahan;
		this.total = total;
	}
	
	public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeSupport.removePropertyChangeListener(listener);
	}
	
	public void setIdPeriksaDetail(Integer idPeriksaDetail) {
		propertyChangeSupport.firePropertyChange("idPeriksaDetail", this.idPeriksaDetail, this.idPeriksaDetail = idPeriksaDetail);
	}
	
	public void setIdPeriksa(Integer idPeriksa) {
		propertyChangeSupport.firePropertyChange("idPeriksa", this.idPeriksa, this.idPeriksa = idPeriksa);
	}
	
	public void setNpwpd(String npwpd) {
		propertyChangeSupport.firePropertyChange("npwpd", this.npwpd, this.npwpd = npwpd);
	}
	
	public void setBulan(String bulan) {
		propertyChangeSupport.firePropertyChange("bulan", this.bulan, this.bulan = bulan);
	}
	
	public void setDasarPengenaan(Double dasarPengenaan) {
		propertyChangeSupport.firePropertyChange("dasarPengenaan", this.dasarPengenaan, this.dasarPengenaan = dasarPengenaan);
	}
	
	public void setPengenaanPeriksa(Double pengenaanPeriksa) {
		propertyChangeSupport.firePropertyChange("pengenaanPeriksa", this.pengenaanPeriksa, this.pengenaanPeriksa = pengenaanPeriksa);
	}
	
	public void setPajakPeriksa(Double pajakPeriksa) {
		propertyChangeSupport.firePropertyChange("pajakPeriksa", this.pajakPeriksa, this.pajakPeriksa = pajakPeriksa);
	}
	
	public void setSelisihKurang(Double selisihKurang) {
		propertyChangeSupport.firePropertyChange("selisihKurang", this.selisihKurang, this.selisihKurang = selisihKurang);
	}
	
	public void setBungaPersen(Integer bungaPersen) {
		propertyChangeSupport.firePropertyChange("bungaPersen", this.bungaPersen, this.bungaPersen = bungaPersen);
	}
	
	public void setBunga(Double bunga) {
		propertyChangeSupport.firePropertyChange("bunga", this.bunga, this.bunga = bunga);
	}
	
	public void setKenaikan(Double kenaikan) {
		propertyChangeSupport.firePropertyChange("kenaikan", this.kenaikan, this.kenaikan = kenaikan);
	}
	
	public void setTotal(Double total) {
		propertyChangeSupport.firePropertyChange("total", this.total, this.total = total);
	}
	
	public void setDendaTambahan(Double dendaTambahan) {
		this.dendaTambahan = dendaTambahan;
	}
	
	public void setJumlahBayar(Double jumlahBayar) {
		this.jumlahBayar = jumlahBayar;
	}
	
	public void setKurangBayar(Double kurangBayar) {
		this.kurangBayar = kurangBayar;
	}
	
	public void setMasaPajak(String masaPajak) {
		this.masaPajak = masaPajak;
	}
	
	public void setPajakTerutang(Double pajakTerutang) {
		this.pajakTerutang = pajakTerutang;
	}
	
	public void setTarif(Integer tarif) {
		this.tarif = tarif;
	}
	
	public Integer getIdPeriksaDetail() {
		return idPeriksaDetail;
	}
	
	public Integer getIdPeriksa() {
		return idPeriksa;
	}
	
	public String getNpwpd() {
		return npwpd;
	}
	
	public String getBulan() {
		return bulan;
	}
	
	public Double getDasarPengenaan() {
		return dasarPengenaan;
	}
	
	public Double getPengenaanPeriksa() {
		return pengenaanPeriksa;
	}
	
	public Double getSelisihKurang() {
		return selisihKurang;
	}
	
	public Double getPajakPeriksa() {
		return pajakPeriksa;
	}
	
	public Integer getBungaPersen() {
		return bungaPersen;
	}
	
	public Double getBunga() {
		return bunga;
	}
	
	public Double getKenaikan() {
		return kenaikan;
	}
	
	public Double getTotal() {
		return total;
	}
	
	public Double getDendaTambahan() {
		return dendaTambahan;
	}
	
	public Double getJumlahBayar() {
		return jumlahBayar;
	}
	
	public Double getKurangBayar() {
		return kurangBayar;
	}
	
	public String getMasaPajak() {
		return masaPajak;
	}
	
	public Double getPajakTerutang() {
		return pajakTerutang;
	}
	
	public Integer getTarif() {
		return tarif;
	}
}
