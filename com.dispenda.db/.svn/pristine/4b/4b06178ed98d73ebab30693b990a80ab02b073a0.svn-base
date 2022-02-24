package com.dispenda.model;

import java.sql.Date;
import java.util.List;

public enum ArsipLogProvider {
	INSTANCE;
	
	private String noSKP = "";
	private Date tanggal;
	private String peminjam = "";
	private String npwpd = "";
	private String keterangan = "";
	private Integer start = null;
	private Integer end = null;
	private ArsipLog arsipLog = new ArsipLog();
	
	public ArsipLog getArsipLog(){
		arsipLog.setNoSKP(getNoSKP());
		arsipLog.setTanggal(getTanggal());
		arsipLog.setPeminjam(getPeminjam());
		arsipLog.setKeterangan(getKeterangan());
		arsipLog.setNpwpd(getNpwpd());
		arsipLog.setStart(getStart());
		arsipLog.setEnd(getEnd());
		return arsipLog;
	}
	
	public void clear(){
		
	}
	
	public String getNoSKP() {
		return noSKP;
	}
	
	public Date getTanggal() {
		return tanggal;
	}
	
	public String getPeminjam() {
		return peminjam;
	}
	
	public String getNpwpd() {
		return npwpd;
	}
	
	public String getKeterangan() {
		return keterangan;
	}
	
	public Integer getStart() {
		return start;
	}
	
	public Integer getEnd() {
		return end;
	}
	
	public void setNoSKP(String noSKP) {
		this.noSKP = noSKP;
	}
	
	public void setTanggal(Date tanggal) {
		this.tanggal = tanggal;
	}
	
	public void setNpwpd(String npwpd) {
		this.npwpd = npwpd;
	}
	
	public void setPeminjam(String peminjam) {
		this.peminjam = peminjam;
	}
	
	public void setKeterangan(String keterangan) {
		this.keterangan = keterangan;
	}
	
	public void setStart(Integer start) {
		this.start = start;
	}
	
	public void setEnd(Integer end) {
		this.end = end;
	}

	/*public void addItem(ArsipLog x){
		arsipLog.add(x);
	}
	
	public void removeItem(int x){
		arsipLog.remove(x);
	}*/
}
