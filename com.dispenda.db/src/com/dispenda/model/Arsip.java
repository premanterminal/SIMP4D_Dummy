package com.dispenda.model;

public class Arsip {
	private String NPWPD = "";
	private String namaRak = "";
	private int noBox = 0;
	private int noMap = 0;
	private String namaBadan = "";
	private String alamat = "";
	
	public Arsip(){}
	
	public Arsip(String NPWPD, String namaRak, int noBox, int noMap, String namaBadan, String alamat){
		this.NPWPD = NPWPD;
		this.namaRak = namaRak;
		this.noBox = noBox;
		this.noMap = noMap;	
		this.namaBadan = namaBadan;
		this.alamat = alamat;
	}
	
	public String getNPWPD() {
		return NPWPD;
	}
	
	public String getNamaRak() {
		return namaRak;
	}
	
	public int getNoBox() {
		return noBox;
	}
	
	public int getNoMap() {
		return noMap;
	}
	
	public String getNamaBadan() {
		return namaBadan;
	}
	
	public String getAlamat() {
		return alamat;
	}
	
	public void setNPWPD(String nPWPD) {
		NPWPD = nPWPD;
	}
	
	public void setNamaRak(String namaRak) {
		this.namaRak = namaRak;
	}
	
	public void setNoBox(int noBox) {
		this.noBox = noBox;
	}
	
	public void setNoMap(int noMap) {
		this.noMap = noMap;
	}

	public void setNamaBadan(String namaBadan) {
		this.namaBadan = namaBadan;
	}
	
	public void setAlamat(String alamat) {
		this.alamat = alamat;
	}
}
