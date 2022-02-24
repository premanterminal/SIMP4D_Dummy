package com.dispenda.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.sql.Array;
import java.sql.Date;

public class Sptpd implements Comparable<Sptpd>{
	private Integer ID;
	private String NamaBadan = "";
	private String NamaPemilik = "";
	private String Alamat = "";
	private String BidangUsaha = "";
	private String Assesment = "";
	private String NoNPPD = "";
	private Date TanggalNPPD;
	private String NoSPTPD = "";
	private String NoSPTPDLama = "";
	private String Status = "";
	private String Keterangan = "";
	private Date TanggalSPTPD;
	private String MasaPajak;
	private Date MasaPajakDari;
	private Date MasaPajakSampai;
	private String NPWPD = "";
	private Double DasarPengenaan = 0.0;
	private Integer TarifPajak = 0;
	private Double PajakTerutang = 0.0;
	private Double DendaTambahan = 0.0;
	private Integer IdSubPajak = 0;
	private String Lokasi = "";
	private Integer posStart = 0;
	private Integer posEnd = 0;
	private String sts = "";
	private String rekapHarian;
	private boolean IsBNI;
	private String NilaiRevisiBNI;
	private Date stsValid; 
	private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
	
	
	public Sptpd(){}
	
	public Sptpd(Integer ID, String NamaBadan, String NamaPemilik, String Status, String Keterangan, String Alamat, String BidangUsaha, String Assesment, String NoNPPD, Date TanggalNPPD, String NoSPTPD, Date TanggalSPTPD, String MasaPajak, Date MasaPajakDari, Date MasaPajakSampai, 
			String NPWPD, Double DasarPengenaan, Integer TarifPajak, Double PajakTerutang, Double DendaTambahan, Integer IdSubPajak, String Lokasi, String NoSPTPDLama,
			Integer posStart, Integer posEnd, String sts, Date stsValid, String rekapHarian, boolean IsBNI, String NilaiRevisiBNI)
	{
		this.ID = ID;
		this.NamaBadan = NamaBadan;
		this.NamaPemilik = NamaPemilik;
		this.Alamat = Alamat;
		this.BidangUsaha = BidangUsaha;
		this.Assesment = Assesment;
		this.NoNPPD = NoNPPD;
		this.TanggalNPPD = TanggalNPPD;
		this.NoSPTPD = NoSPTPD;
		this.Status = Status;
		this.Keterangan = Keterangan;
		this.TanggalSPTPD = TanggalSPTPD;
		this.MasaPajak = MasaPajak;
		this.MasaPajakDari = MasaPajakDari;
		this.MasaPajakSampai = MasaPajakSampai;
		this.NPWPD = NPWPD;
		this.DasarPengenaan = DasarPengenaan;
		this.TarifPajak = TarifPajak;
		this.PajakTerutang = PajakTerutang;
		this.DendaTambahan = DendaTambahan;
		this.IdSubPajak = IdSubPajak;
		this.Lokasi = Lokasi;
		this.NoSPTPDLama = NoSPTPDLama;
		this.posStart = posStart;
		this.posEnd = posEnd;
		this.sts = sts;
		this.stsValid = stsValid;
		this.rekapHarian = rekapHarian;
		this.NilaiRevisiBNI = NilaiRevisiBNI;
		this.IsBNI = IsBNI;
	}
	
	public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener){
		propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
	}
	
	public void removePropertyChangeListener(PropertyChangeListener listener){
		propertyChangeSupport.removePropertyChangeListener(listener);
	}
	
	public void setID(Integer ID){
		propertyChangeSupport.firePropertyChange("ID", this.ID, this.ID = ID);
	}
	
	public void setAssesment(String Assesment){
		propertyChangeSupport.firePropertyChange("Assesment", this.Assesment, this.Assesment = Assesment);		
	}
	
	public void setNoNPPD(String NoNPPD){
		propertyChangeSupport.firePropertyChange("NoNPPD", this.NoNPPD, this.NoNPPD = NoNPPD);
	}
	
	public void setTanggalNPPD(Date TanggalNPPD){
		propertyChangeSupport.firePropertyChange("TanggalNPPD", this.TanggalNPPD, this.TanggalNPPD = TanggalNPPD);
	}
	
	public void setNoSPTPD(String NoSPTPD){
		propertyChangeSupport.firePropertyChange("NoSPTPD", this.NoSPTPD, this.NoSPTPD = NoSPTPD);
	}
	
	public void setNoSPTPDLama(String noSPTPDLama) {
		NoSPTPDLama = noSPTPDLama;
	}
	
	public void setTanggalSPTPD(Date TanggalSPTPD){
		propertyChangeSupport.firePropertyChange("TanggalSPTPD", this.TanggalSPTPD, this.TanggalSPTPD = TanggalSPTPD);
	}
	
	public void setMasaPajak(String MasaPajak){
		propertyChangeSupport.firePropertyChange("MasaPajak", this.MasaPajak, this.MasaPajak = MasaPajak);
	}
	
	public void setMasaPajakDari(Date MasaPajakDari){
		propertyChangeSupport.firePropertyChange("MasaPajakDari", this.MasaPajakDari, this.MasaPajakDari = MasaPajakDari);
	}
	
	public void setMasaPajakSampai(Date MasaPajakSampai){
		propertyChangeSupport.firePropertyChange("MasaPajakSampai", this.MasaPajakSampai, this.MasaPajakSampai = MasaPajakSampai);
	}
	
	public void setNPWPD(String NPWPD){
		propertyChangeSupport.firePropertyChange("NPWPD", this.NPWPD, this.NPWPD = NPWPD);
	}
	
	public void setDasarPengenaan(double DasarPengenaan){
		propertyChangeSupport.firePropertyChange("DasarPengenaan", this.DasarPengenaan, this.DasarPengenaan = DasarPengenaan);
	}
	
	public void setTarifPajak(Integer TarifPajak){
		propertyChangeSupport.firePropertyChange("TarifPajak", this.TarifPajak, this.TarifPajak = TarifPajak);
	}
	
	public void setPajakTerutang(double PajakTerutang){
		propertyChangeSupport.firePropertyChange("PajakTerutang", this.PajakTerutang, this.PajakTerutang = PajakTerutang);
	}
	
	public void setDendaTambahan(double DendaTambahan){
		propertyChangeSupport.firePropertyChange("DendaTambahan", this.DendaTambahan, this.DendaTambahan = DendaTambahan);
	}
	
	public void setIdSubPajak(Integer IdSubPajak){
		propertyChangeSupport.firePropertyChange("IdSubPajak", this.IdSubPajak, this.IdSubPajak = IdSubPajak);
	}
	
	public void setLokasi(String Lokasi){
		propertyChangeSupport.firePropertyChange("Lokasi", this.Lokasi, this.Lokasi = Lokasi);
	}
	
	public void setPosStart(Integer posStart) {
		this.posStart = posStart;
	}
	
	public void setPosEnd(Integer posEnd) {
		this.posEnd = posEnd;
	}
	
	public void setSts(String sts) {
		this.sts = sts;
	}
	
	public void setStsValid(Date stsValid) {
		this.stsValid = stsValid;
	}
	
	public Integer getID(){
		return ID;
	}
	
	public String getAssesment(){
		return Assesment;
	}
	
	public String getNoNPPD(){
		return NoNPPD;
	}
	
	public Date getTanggalNPPD(){
		return TanggalNPPD;
	}
	
	public String getNoSPTPD(){
		return NoSPTPD;
	}
	
	public String getNoSPTPDLama() {
		return NoSPTPDLama;
	}
	
	public Date getTanggalSPTPD(){
		return TanggalSPTPD;
	}
	
	public String getMasaPajak(){
		return MasaPajak;
	}
	
	public Date getMasaPajakDari(){
		return MasaPajakDari;
	}
	
	public Date getMasaPajakSampai(){
		return MasaPajakSampai;
	}
	
	public String getNPWPD(){
		return NPWPD;
	}
	
	public Double getDasarPengenaan(){
		return DasarPengenaan;
	}
	
	public Integer getTarifPajak(){
		return TarifPajak;
	}
	
	public Double getPajakterutang(){
		return PajakTerutang;
	}
	
	public Double getDendaTambahan(){
		return DendaTambahan;
	}
	
	public Integer getIdSubPajak(){
		return IdSubPajak;
	}
	
	public String getLokasi(){
		return Lokasi;
	}
	
	public Integer getPosStart() {
		return posStart;
	}
	
	public Integer getPosEnd() {
		return posEnd;
	}
	
	public String getSts() {
		return sts;
	}
	
	public Date getStsValid() {
		return stsValid;
	}
	

	@Override
	public int compareTo(Sptpd s) {
		return s.getMasaPajakDari().compareTo(this.getMasaPajakDari()); // Descending
//		return this.getMasaPajakDari().compareTo(s.getMasaPajakDari()); // Ascending
	}
	
	public void setNamaBadan(String NamaBadan){
		propertyChangeSupport.firePropertyChange("NamaBadan", this.NamaBadan, this.NamaBadan = NamaBadan);
	}

	public String getNamaBadan() {
		return NamaBadan;
	}
	
	public void setNamaPemilik(String NamaPemilik){
		propertyChangeSupport.firePropertyChange("NamaPemilik", this.NamaPemilik, this.NamaPemilik = NamaPemilik);
	}

	public void setRekapHarian(String rekapHarian){
		propertyChangeSupport.firePropertyChange("rekapHarian", this.rekapHarian, this.rekapHarian = rekapHarian);
	}
	
	public String getRekapHarian(){
		return rekapHarian;
	}
	
	public String getNamaPemilik() {
		return NamaPemilik;
	}
	
	public void setAlamat(String Alamat){
		propertyChangeSupport.firePropertyChange("Alamat", this.Alamat, this.Alamat = Alamat);
	}

	public String getAlamat() {
		return Alamat;
	}
	
	public void setBidangUsaha(String BidangUsaha){
		propertyChangeSupport.firePropertyChange("BidangUsaha", this.BidangUsaha, this.BidangUsaha = BidangUsaha);
	}

	public String getBidangUsaha() {
		return BidangUsaha;
	}

	public void setStatus(String Status) {
		// TODO Auto-generated method stub
		propertyChangeSupport.firePropertyChange("Status", this.Status, this.Status = Status);
	}
	
	public String getStatus() {
		return Status;
	}
	
	public void setKeterangan(String Keterangan) {
		// TODO Auto-generated method stub
		propertyChangeSupport.firePropertyChange("Keterangan", this.Keterangan, this.Keterangan = Keterangan);
	}
	
	public String getKeterangan() {
		return Keterangan;
	}
	
	public void setIsBNI(boolean IsBNI){
		propertyChangeSupport.firePropertyChange("IsBNI", this.IsBNI, this.IsBNI = IsBNI);
	}

	public boolean getIsBNI() {
		return IsBNI;
	}
	
	public void setNilaiRevisiBNI(String NilaiRevisiBNI){
		propertyChangeSupport.firePropertyChange("NilaiRevisiBNI", this.NilaiRevisiBNI, this.NilaiRevisiBNI = NilaiRevisiBNI);
	}

	public String getNilaiRevisiBNI() {
		return NilaiRevisiBNI;
	}
	
	
}
