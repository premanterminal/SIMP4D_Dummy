package com.dispenda.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.sql.Date;

public class Sspd extends PendaftaranWajibPajak{
	private Integer idSspd = null;
	private String no_sspd = "" ;
	private java.sql.Timestamp tgl_sspd ;
	private java.sql.Timestamp tgl_cetak ;
	private String npwpd = "" ;
	private String bulan_skp ;
	private String no_skp = "" ;
	private Integer cicilan_ke = 0 ;
	private Double denda ;
	private Double jumlah_bayar ;
	private Integer jenis_bayar;
	private String cara_bayar ;
	private String nama_penyetor ;
	private boolean lunas;
	private Integer idsub_pajak = 0 ;
	private boolean denda_lunas ;
	private String lokasi;
	private Integer idSPTPD = 0;
	private Integer idPeriksa = 0;
	private String NotaBayar;
	private Date MasaPajakSampai;
	private Date MasaPajakDari;
	private String noReg;
	private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
	private Pajak pajak = new Pajak();
	private BidangUsaha usaha = new BidangUsaha(); 
	public Sspd(){}
	
	public Sspd(Integer idSspd, String no_sspd, java.sql.Timestamp tgl_sspd, java.sql.Timestamp tgl_cetak, String npwpd, String bulan_skp, String no_skp,
			Integer cicilan_ke, Double denda, Double jumlah_bayar, Integer jenis_bayar, String cara_bayar, String NotaBayar,
			String nama_penyetor, boolean lunas, Integer idsub_pajak, boolean denda_lunas, String lokasi, Integer idSPTPD, Integer idPeriksa, 
			Date MasaPajakSampai, Date MasaPajakDari, String noReg){
		this.idSspd = idSspd;
		this.no_sspd = no_sspd;
		this.tgl_sspd = tgl_sspd;
		this.tgl_cetak = tgl_cetak;
		this.npwpd = npwpd;
		this.bulan_skp = bulan_skp;
		this.no_skp = no_skp;
		this.cicilan_ke = cicilan_ke;
		this.denda = denda;
		this.jumlah_bayar = jumlah_bayar;
		this.jenis_bayar = jenis_bayar;
		this.cara_bayar = cara_bayar;
		this.nama_penyetor = nama_penyetor;
		this.lunas = lunas;
		this.idsub_pajak = idsub_pajak;
		this.denda_lunas = denda_lunas;
		this.lokasi = lokasi;
		this.idSPTPD = idSPTPD;
		this.idPeriksa = idPeriksa;
		this.NotaBayar = NotaBayar;
		this.MasaPajakSampai = MasaPajakSampai;
		this.MasaPajakDari = MasaPajakDari;
		this.noReg = noReg;
	}
	
	public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeSupport.removePropertyChangeListener(listener);
	}
	
	@Override
	public void setNamaBadan(String nama_badan) {
		// TODO Auto-generated method stub
		super.setNamaBadan(nama_badan);
	}
	
	@Override
	public void setNamaPemilik(String nama_pemilik) {
		// TODO Auto-generated method stub
		super.setNamaPemilik(nama_pemilik);
	}
	
	@Override
	public void setAlabadJalan(String alabad_jalan) {
		// TODO Auto-generated method stub
		super.setAlabadJalan(alabad_jalan);
	}
	
	public void setNamaPajak(String Nama_Pajak){
		pajak.setNamaPajak(Nama_Pajak);
	}
	
	public void setKodeDenda(String Kode_Denda){
		pajak.setKodeDenda(Kode_Denda);
	}
	
	public void setKodeBidUsaha(String kode_bid_usaha){
		usaha.setKodeBidUsaha(kode_bid_usaha);
	}
	
	public void setIdSspd(Integer idSspd){
		propertyChangeSupport.firePropertyChange("idSspd", this.idSspd, this.idSspd = idSspd);
	}
	
	public void setNoSspd(String no_sspd){
		propertyChangeSupport.firePropertyChange("no_sspd", this.no_sspd, this.no_sspd = no_sspd);
	}
	
	public void setTglSspd(java.sql.Timestamp tgl_sspd){
		propertyChangeSupport.firePropertyChange("tgl_sspd", this.tgl_sspd, this.tgl_sspd = tgl_sspd);
	}
	
	public void setTglCetak(java.sql.Timestamp tgl_cetak){
		propertyChangeSupport.firePropertyChange("tgl_cetak", this.tgl_cetak, this.tgl_cetak = tgl_cetak);
	}
	
	public void setNpwpd(String npwpd){
		propertyChangeSupport.firePropertyChange("npwpd", this.npwpd, this.npwpd = npwpd);
	}
	
	public void setBulanSkp(String bulan_skp){
		propertyChangeSupport.firePropertyChange("bulan_skp", this.bulan_skp, this.bulan_skp = bulan_skp);
	}
	
	public void setNoSkp(String no_skp){
		propertyChangeSupport.firePropertyChange("no_skp", this.no_skp, this.no_skp = no_skp);
	}
	
	public void setCicilanKe(Integer cicilan_ke){
		propertyChangeSupport.firePropertyChange("cicilan_ke", this.cicilan_ke, this.cicilan_ke = cicilan_ke);
	}
	
	public void setDenda(Double denda){
		propertyChangeSupport.firePropertyChange("denda", this.denda, this.denda = denda);
	}
	
	public void setJumlahBayar(Double jumlah_bayar){
		propertyChangeSupport.firePropertyChange("jumlah_bayar", this.jumlah_bayar, this.jumlah_bayar = jumlah_bayar);
	}
	
	public void setJenisBayar(Integer jenis_bayar){
		propertyChangeSupport.firePropertyChange("jenis_bayar", this.jenis_bayar, this.jenis_bayar = jenis_bayar);
	}
	
	public void setCaraBayar(String cara_bayar){
		propertyChangeSupport.firePropertyChange("cara_bayar", this.cara_bayar, this.cara_bayar = cara_bayar);
	}
	
	public void setNamaPenyetor(String nama_penyetor){
		propertyChangeSupport.firePropertyChange("nama_penyetor", this.nama_penyetor, this.nama_penyetor = nama_penyetor);
	}
	
	public void setLunas(boolean lunas){
		propertyChangeSupport.firePropertyChange("lunas", this.lunas, this.lunas = lunas);
	}
	
	public void setIdSubPajak(Integer idsub_pajak){
		propertyChangeSupport.firePropertyChange("idsub_pajak", this.idsub_pajak, this.idsub_pajak = idsub_pajak);
	}
	
	public void setDendaLunas(boolean denda_lunas){
		propertyChangeSupport.firePropertyChange("denda_lunas", this.denda_lunas, this.denda_lunas = denda_lunas);
	}
	
	public void setLokasi(String lokasi){
		propertyChangeSupport.firePropertyChange("lokasi", this.lokasi, this.lokasi = lokasi);
	}
	
	public void setIdSPTPD(Integer idSPTPD){
		propertyChangeSupport.firePropertyChange("idSPTPD", this.idSPTPD, this.idSPTPD = idSPTPD);
	}
	
	public void setIdPeriksa(Integer idPeriksa){
		propertyChangeSupport.firePropertyChange("idPeriksa", this.idPeriksa, this.idPeriksa = idPeriksa);
	}
	
	public void setNotaBayar(String notaBayar) {
		NotaBayar = notaBayar;
	}
	
	public void setMasaPajakDari(Date masaPajakDari) {
		MasaPajakDari = masaPajakDari;
	}
	
	public void setMasaPajakSampai(Date masaPajakSampai) {
		MasaPajakSampai = masaPajakSampai;
	}
	
	public void setNoReg(String noReg) {
		this.noReg = noReg;
	}
	
	public Integer getIdSspd(){
		return idSspd;
	}
	
	public String getNoSspd(){
		return no_sspd;
	}
	
	@Override
	public String getNamaBadan() {
		// TODO Auto-generated method stub
		return super.getNamaBadan();
	}
	
	@Override
	public String getNamaPemilik() {
		// TODO Auto-generated method stub
		return super.getNamaPemilik();
	}
	
	@Override
	public String getAlabadJalan() {
		// TODO Auto-generated method stub
		return super.getAlabadJalan();
	}
	
	public String getNamaPajak() {
		return pajak.getNamaPajak();
	}
	
	public String getKodeDenda(){
		return pajak.getKodeDenda();
	}
	
	public String getKodeBidUsaha(){
		return usaha.getKodeBidUsaha();
	}
	
	public java.sql.Timestamp getTglSspd(){
		return tgl_sspd;
	}
	
	public java.sql.Timestamp getTglCetak(){
		return tgl_cetak;
	}
	
	public String getNpwpd(){
		return npwpd;
	}
	
	public String getBulanSkp(){
		return bulan_skp;
	}
	
	public String getNoSkp(){
		return no_skp;
	}
	
	public Integer getCicilanKe(){
		return cicilan_ke;
	}
	
	public Double getDenda(){
		return denda;
	}
	
	public Double getJumlahBayar(){
		return jumlah_bayar;
	}
	
	public Integer getJenisBayar(){
		return jenis_bayar;
	}
	
	public String getCaraBayar(){
		return cara_bayar;
	}
	
	public String getNamaPenyetor(){
		return nama_penyetor;
	}
	
	public boolean getLunas(){
		return lunas;
	}
	
	public Integer getIdSubPajak(){
		return idsub_pajak;
	}
	
	public boolean getDendaLunas(){
		return denda_lunas;
	}
	
	public String getLokasi(){
		return lokasi;
	}
	
	public Integer getIdSPTPD(){
		return idSPTPD;
	}
	
	public Integer getIdPeriksa(){
		return idPeriksa;
	}
	
	public String getNotaBayar() {
		return NotaBayar;
	}
	
	public Date getMasaPajakDari() {
		return MasaPajakDari;
	}
	
	public Date getMasaPajakSampai() {
		return MasaPajakSampai;
	}
	
	public String getNoReg() {
		return noReg;
	}
}

