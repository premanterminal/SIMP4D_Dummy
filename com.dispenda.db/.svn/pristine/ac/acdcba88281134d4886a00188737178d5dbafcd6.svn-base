package com.dispenda.model;

import java.sql.Date;

public class Tunggakan implements Comparable<Tunggakan>{
	private Integer idSptpd;
	private Integer idPeriksa;
	private String npwpd;
	private String namaBadan;
	private String namaPemilik;
	private String Alamat;
	private String noSkp;
	private Date TglSkp;
	private Date masaPajakDari;
	private Date masaPajakSampai;
	private Double pokok;
	private Double kenaikan;
	private Double denda;
	private Double dendaSspd;
	private Integer idSubPajak;
	private String bidang_usaha;
	private Integer cicilan;
	private String Status;
	private Date tglJatuhTempo;
	
	public Tunggakan(){
		
	}
	
	public Integer getIdSptpd() {
		return idSptpd;
	}
	
	public Integer getIdPeriksa() {
		return idPeriksa;
	}
	
	public String getNpwpd() {
		return npwpd;
	}
	
	public String getNamaBadan() {
		return namaBadan;
	}
	
	public String getNamaPemilik() {
		return namaPemilik;
	}
	
	public String getAlamat() {
		return Alamat;
	}
	
	public String getNoSkp() {
		return noSkp;
	}
	
	public Date getTglSkp() {
		return TglSkp;
	}
	
	public Date getMasaPajakDari() {
		return masaPajakDari;
	}
	
	public Date getMasaPajakSampai() {
		return masaPajakSampai;
	}
	
	public Date getTglJatuhTempo() {
		return tglJatuhTempo;
	}
	
	public Double getPokok() {
		return pokok;
	}
	
	public Double getKenaikan() {
		return kenaikan;
	}
	
	public Double getDenda() {
		return denda;
	}
	
	public Double getDendaSspd() {
		return dendaSspd;
	}
	
	public Integer getIdSubPajak() {
		return idSubPajak;
	}
	
	public String getBidang_usaha() {
		return bidang_usaha;
	}
	
	public Integer getCicilan() {
		return cicilan;
	}
	
	public String getStatus() {
		return Status;
	}
	
	public void setIdSptpd(Integer idSptpd) {
		this.idSptpd = idSptpd;
	}
	
	public void setIdPeriksa(Integer idPeriksa) {
		this.idPeriksa = idPeriksa;
	}
	
	public void setNpwpd(String npwpd) {
		this.npwpd = npwpd;
	}
	
	public void setNamaBadan(String namaBadan) {
		this.namaBadan = namaBadan;
	}
	
	public void setNamaPemilik(String namaPemilik) {
		this.namaPemilik = namaPemilik;
	}
	
	public void setAlamat(String alamat) {
		this.Alamat = alamat;
	}
	
	public void setNoSkp(String noSkp) {
		this.noSkp = noSkp;
	}
	
	public void setTglSkp(Date tglSkp) {
		this.TglSkp = tglSkp;
	}
	
	public void setMasaPajakDari(Date masaPajakDari) {
		this.masaPajakDari = masaPajakDari;
	}
	
	public void setMasaPajakSampai(Date masaPajakSampai) {
		this.masaPajakSampai = masaPajakSampai;
	}
	
	public void setTglJatuhTempo(Date tglJatuhTempo) {
		this.tglJatuhTempo = tglJatuhTempo;
	}
	
	public void setPokok(Double pokok) {
		this.pokok = pokok;
	}
	
	public void setKenaikan(Double kenaikan) {
		this.kenaikan = kenaikan;
	}
	
	public void setDenda(Double denda) {
		this.denda = denda;
	}
	
	public void setDendaSspd(Double dendaSspd) {
		this.dendaSspd = dendaSspd;
	}
	
	public void setIdSubPajak(Integer idSubPajak) {
		this.idSubPajak = idSubPajak;
	}
	
	public void setBidang_usaha(String bidang_usaha) {
		this.bidang_usaha = bidang_usaha;
	}
	
	public void setCicilan(Integer cicilan) {
		this.cicilan = cicilan;
	}
	
	public void setStatus(String status) {
		Status = status;
	}

	@Override
	public int compareTo(Tunggakan t) {
		// TODO Auto-generated method stub
		int tgl = this.getTglSkp().compareTo(t.getTglSkp());
		if (tgl != 0){
			return tgl;
		}
		
		int no = this.getNoSkp().compareTo(t.noSkp);
		if (no != 0){
			return no;
		}
		
		return this.getCicilan().compareTo(t.getCicilan());
		
//		return this.getTglSkp().compareTo(t.getTglSkp());
	}
}
