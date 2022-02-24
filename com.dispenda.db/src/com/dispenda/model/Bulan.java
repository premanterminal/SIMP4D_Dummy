package com.dispenda.model;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Bulan {
	private String namaBulan = "";
	private Double pokok = new Double(0.0);
	private Double denda = new Double(0.0);
	private Double jumlah = new Double(0.0);
	private List<Pajak> pajak = new LinkedList<Pajak>();
	
	public void setPajak(List<Pajak> pajak) {
		this.pajak = pajak;
	}
	
	public boolean addPajak(Pajak pajak){
		boolean add = this.pajak.add(pajak);
		if(add)
			pajak.setBulan(this);
		return add;
	}
	
	public List<Pajak> getPajak() {
		return Collections.unmodifiableList(pajak);
	}
	
	public Bulan() {
		// TODO Auto-generated constructor stub
	}
	
	public void setDenda(Double denda) {
		this.denda = denda;
	}
	
	public void setJumlah(Double jumlah) {
		this.jumlah = jumlah;
	}
	
	public void setNamaBulan(String namaBulan) {
		this.namaBulan = namaBulan;
	}
	
	public void setPokok(Double pokok) {
		this.pokok = pokok;
	}
	
	public Double getDenda() {
		return denda;
	}
	
	public String getNamaBulan() {
		return namaBulan;
	}
	
	public Double getJumlah() {
		return jumlah;
	}
	
	public Double getPokok() {
		return pokok;
	}
}
