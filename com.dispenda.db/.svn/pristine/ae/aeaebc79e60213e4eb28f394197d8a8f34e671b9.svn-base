package com.dispenda.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class PeriksaDetail {
	private Integer idPeriksaDetail = 0;
	private Integer idPeriksa = 0;
	private String npwpd = "";
	private String bulanPeriksa = "";
	private Double dasarPengenaan;
	private Double pengenaanPeriksa;
	private Double pajakPeriksa;
	private Double selisihKurang;
	private Integer bungaPersen = 0;
	private Double bunga;
	private Double kenaikanPajak;
	private Double totalPeriksa;
	private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
	
	public PeriksaDetail(){}
	
	public PeriksaDetail(Integer idPeriksaDetail, Integer idPeriksa, String npwpd, String bulanPeriksa,  Double dasarPengenaan,
			Double pengenaanPeriksa, Double pajakPeriksa, Double selisihKurang, Integer bungaPersen, Double bunga, Double kenaikanPajak, Double totalPeriksa){
		this.idPeriksaDetail = idPeriksaDetail;
		this.idPeriksa = idPeriksa;
		this.npwpd = npwpd;
		this.bulanPeriksa = bulanPeriksa;
		this.dasarPengenaan = dasarPengenaan;
		this.pengenaanPeriksa = pengenaanPeriksa;
		this.pajakPeriksa = pajakPeriksa;
		this.selisihKurang = selisihKurang;
		this.bungaPersen = bungaPersen;
		this.bunga = bunga;
		this.kenaikanPajak = kenaikanPajak;
		this.totalPeriksa = totalPeriksa;
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
	
	public void setBulanPeriksa(String bulanPeriksa) {
		propertyChangeSupport.firePropertyChange("bulanPeriksa", this.bulanPeriksa, this.bulanPeriksa = bulanPeriksa);
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
	
	public void setKenaikanPajak(Double kenaikanPajak) {
		propertyChangeSupport.firePropertyChange("kenaikanPajak", this.kenaikanPajak, this.kenaikanPajak = kenaikanPajak);
	}
	
	public void setTotalPeriksa(Double totalPeriksa) {
		propertyChangeSupport.firePropertyChange("totalPeriksa", this.totalPeriksa, this.totalPeriksa = totalPeriksa);
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
	
	public String getBulanPeriksa() {
		return bulanPeriksa;
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
	
	public Double getKenaikanPajak() {
		return kenaikanPajak;
	}
	
	public Double getTotalPeriksa() {
		return totalPeriksa;
	}
}
