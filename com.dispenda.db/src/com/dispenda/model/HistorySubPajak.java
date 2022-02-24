package com.dispenda.model;

import java.sql.Date;

public class HistorySubPajak {
	private Integer id;
	private String npwpd = "";
	private Integer idSubPajakLama;
	private Integer idSubPajakBaru;
	private Date tanggal;
	
	public HistorySubPajak(){}
	
	public HistorySubPajak(Integer id, String npwpd, Integer idSubPajakLama, Integer idSubPajakBaru, Date tanggal) {
		this.id = id;
		this.npwpd = npwpd;
		this.idSubPajakBaru = idSubPajakBaru;
		this.idSubPajakLama = idSubPajakLama;
		this.tanggal = tanggal;
	}
	
	public Integer getId() {
		return id;
	}
	
	public Integer getIdSubPajakBaru() {
		return idSubPajakBaru;
	}
	
	public Integer getIdSubPajakLama() {
		return idSubPajakLama;
	}
	
	public String getNpwpd() {
		return npwpd;
	}
	
	public Date getTanggal() {
		return tanggal;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public void setIdSubPajakBaru(Integer idSubPajakBaru) {
		this.idSubPajakBaru = idSubPajakBaru;
	}
	
	public void setIdSubPajakLama(Integer idSubPajakLama) {
		this.idSubPajakLama = idSubPajakLama;
	}
	
	public void setNpwpd(String npwpd) {
		this.npwpd = npwpd;
	}
	
	public void setTanggal(Date tanggal) {
		this.tanggal = tanggal;
	}
}
