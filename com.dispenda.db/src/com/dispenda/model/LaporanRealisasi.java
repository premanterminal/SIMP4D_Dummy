package com.dispenda.model;

import java.util.Date;

public class LaporanRealisasi {
	private Integer noReg = 0;
	private String npwpd = "";
	private String namaBadan = "";
	private String bidUsaha = "";
	private String noSKP = "";
	private String noSSPD = "";
	private String bulanSKP = "";
	private Integer cicilanKe = 0;
	private Date tglBayar;
	private Double pokok = new Double(0.0);
	private Double denda = new Double(0.0);
	private Double jumlah = new Double(0.0);
	private Hari hari;
	private BidangUsaha bidangUsaha;
	
	public void setBidangUsaha(BidangUsaha bidangUsaha) {
		this.bidangUsaha = bidangUsaha;
	}
	
	public BidangUsaha getBidangUsaha() {
		return bidangUsaha;
	}
	
	public void setHari(Hari hari) {
		this.hari = hari;
	}
	
	public Hari getHari() {
		return hari;
	}
	
	public LaporanRealisasi() {
		// TODO Auto-generated constructor stub
	}
	
	public void setBidUsaha(String bidUsaha) {
		this.bidUsaha = bidUsaha;
	}
	
	public void setBulanSKP(String bulanSKP) {
		this.bulanSKP = bulanSKP;
	}
	
	public void setCicilanKe(Integer cicilanKe) {
		this.cicilanKe = cicilanKe;
	}
	
	public void setDenda(Double denda) {
		this.denda = denda;
	}
	
	public void setJumlah(Double jumlah) {
		this.jumlah = jumlah;
	}
	
	public void setNamaBadan(String namaBadan) {
		this.namaBadan = namaBadan;
	}
	
	public void setNoReg(Integer noReg) {
		this.noReg = noReg;
	}
	
	public void setNoSKP(String noSKP) {
		this.noSKP = noSKP;
	}
	
	public void setNoSSPD(String noSSPD) {
		this.noSSPD = noSSPD;
	}
	
	public void setNpwpd(String npwpd) {
		this.npwpd = npwpd;
	}
	
	public void setPokok(Double pokok) {
		this.pokok = pokok;
	}
	
	public void setTglBayar(Date tglBayar) {
		this.tglBayar = tglBayar;
	}
	
	public String getBidUsaha() {
		return bidUsaha;
	}
	
	public String getBulanSKP() {
		return bulanSKP;
	}
	
	public Integer getCicilanKe() {
		return cicilanKe;
	}
	
	public Double getDenda() {
		return denda;
	}
	
	public Double getJumlah() {
		return jumlah;
	}
	
	public String getNamaBadan() {
		return namaBadan;
	}
	
	public Integer getNoReg() {
		return noReg;
	}
	
	public String getNoSKP() {
		return noSKP;
	}
	
	public String getNoSSPD() {
		return noSSPD;
	}
	
	public String getNpwpd() {
		return npwpd;
	}
	
	public Double getPokok() {
		return pokok;
	}
	
	public Date getTglBayar() {
		return tglBayar;
	}
}
