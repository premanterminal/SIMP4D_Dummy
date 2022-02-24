package com.dispenda.model;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Hari {
	private String tgl = "";
	private Double pokok = new Double(0.0);
	private Double denda = new Double(0.0);
	private Double jumlah = new Double(0.0);
	private List<LaporanRealisasi> laporanRealisasi = new LinkedList<LaporanRealisasi>();
	private BidangUsaha bidUsaha;
	
	public boolean addLaporanRealisasi (LaporanRealisasi lr){
		boolean add = this.laporanRealisasi.add(lr);
		if(add)
			lr.setHari(this);
		return add;
	}
	
	public List<LaporanRealisasi> getLaporanRealisasi() {
		return Collections.unmodifiableList(laporanRealisasi);
	}
	
	public void setBidUsaha(BidangUsaha bidUsaha) {
		this.bidUsaha = bidUsaha;
	}
	
	public BidangUsaha getBidUsaha() {
		return bidUsaha;
	}
	
	public Hari() {
		// TODO Auto-generated constructor stub
	}
	
	public void setDenda(Double denda) {
		this.denda = denda;
	}
	
	public void setJumlah(Double jumlah) {
		this.jumlah = jumlah;
	}
	
	public void setLaporanRealisasi(List<LaporanRealisasi> laporanRealisasi) {
		this.laporanRealisasi = laporanRealisasi;
	}
	
	public void setPokok(Double pokok) {
		this.pokok = pokok;
	}
	
	public void setTgl(String tgl) {
		this.tgl = tgl;
	}
	
	public Double getDenda() {
		return denda;
	}
	
	public Double getJumlah() {
		return jumlah;
	}
	

	public Double getPokok() {
		return pokok;
	}
	
	public String getTgl() {
		return tgl;
	}
}
