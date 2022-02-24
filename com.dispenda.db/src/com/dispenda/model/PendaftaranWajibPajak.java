package com.dispenda.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.sql.Date;

public class PendaftaranWajibPajak{
	
	private BidangUsaha bidangUsaha = new BidangUsaha();
	private Pajak pajak = new Pajak();
	public String npwp = "" ;
	private String no_formulir = "" ;
	private String no_pendaftaran = "" ;
	private String jenis_pajak = "" ;
	private String jenis_assesment = "" ;
	private Date tanggal_daftar;
	private String nama_badan = "" ;
	private String nama_pemilik = "" ;
	private String jabatan = "" ;
	private Integer kewajiban_pajak = 0 ;
	private Integer idsub_pajak = 0;
	private String alabad_jalan = "" ;
	private String alabad_kecamatan = "" ;
	private String alabad_kelurahan = "" ;
	private String alabad_kodepos = "" ;
	private String alabad_telepon = "" ;
	private String alating_jalan = "" ;
	private String alating_kecamatan = "" ;
	private String alating_kelurahan = "" ;
	private String alating_kota = "" ;
	private String alating_kodepos = "" ;
	private String alating_telepon = "" ;
	private String kewarganegaraan = "" ;
	private String tanda_bukti_diri = "" ;
	private String no_bukti_diri = "" ;
	private String no_KK = "" ;
	private String status = "" ;
	private String keterangan = "";
	private String tanggal_approve = "";
	private String bidang_usaha = "";
	private String kode_bid_usaha = "";
	private Boolean insidentil;
	private Boolean insidentil_pemerintahan;
	private String keterangan_tutup;
	private Boolean aktif;
	private Boolean tutup;
	private Boolean ekswp;
	private Integer tarif = 0;
	private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
	private Integer noUrut;
	private String uu;
	
	public PendaftaranWajibPajak(){}
	
	public PendaftaranWajibPajak(
			String npwp,
			String no_formulir,
			String no_pendaftaran,
			String jenis_pajak, 
			String jenis_assesment,
			Date tanggal_daftar,
			String nama_badan,
			String nama_pemilik,
			String jabatan,
			Integer kewajiban_pajak,
			Integer idsub_pajak,
			String alabad_jalan,
			String alabad_kecamatan,
			String alabad_kelurahan,
			String alabad_kodepos,
			String alabad_telepon,
			String alating_jalan,
			String alating_kecamatan,
			String alating_kelurahan,
			String alating_kota,
			String alating_kodepos,
			String alating_telepon,
			String kewarganegaraan,
			String tanda_bukti_diri,
			String no_bukti_diri,
			String no_KK,
			String status,
			String keterangan,
			String tanggal_approve,
			String bidang_usaha,
			String Kode_bid_usaha,
			Boolean insidentil,
			Boolean insidentil_pemerintahan,
			String keterangan_tutup,
			Boolean aktif,
			Boolean tutup,
			Boolean ekswp
			)
	{
		this.npwp = npwp;
		this.no_formulir = no_formulir ;
		this.no_pendaftaran = no_pendaftaran ;
		this.tanggal_daftar = tanggal_daftar ;
		this.nama_badan = nama_badan ;
		this.nama_pemilik = nama_pemilik ;
		this.no_KK = no_KK ;
		this.jabatan = jabatan ;
		this.jenis_assesment = jenis_assesment ;
		this.jenis_pajak = jenis_pajak ;
		this.status = status;
		this.idsub_pajak = idsub_pajak;
		this.alating_jalan = alating_jalan ;
		this.alating_kelurahan = alating_kelurahan ;
		this.alating_kecamatan = alating_kecamatan ;
		this.alating_kota = alating_kota ;
		this.alating_telepon = alating_telepon ;
		this.alating_kodepos = alating_kodepos ;
		this.alabad_jalan = alabad_jalan ;
		this.alabad_kelurahan = alabad_kelurahan ;
		this.alabad_kecamatan = alabad_kecamatan ;
		this.alabad_telepon = alabad_telepon ;
		this.alabad_kodepos = alabad_kodepos ;
		this.kewarganegaraan = kewarganegaraan ;
		this.tanda_bukti_diri = tanda_bukti_diri ;
		this.no_bukti_diri = no_bukti_diri ;
		this.keterangan = keterangan ;
		this.bidang_usaha = bidang_usaha ;
		this.kode_bid_usaha = Kode_bid_usaha;
		this.kewajiban_pajak = kewajiban_pajak ;
		this.keterangan_tutup = keterangan_tutup;
		this.aktif = aktif;
		this.tutup = tutup;
		this.ekswp = ekswp;
	}
	
	public String getUu() {
		return uu;
	}
	
	public void setUu(String uu) {
		this.uu = uu;
	}
	
	public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener)
	{
		propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
	}
	
	public void removePropertyChangeListener(PropertyChangeListener listener)
	{
		propertyChangeSupport.removePropertyChangeListener(listener);
	}
	
	public void setNPWP(String npwp)
	{
		propertyChangeSupport.firePropertyChange("npwp", this.npwp, this.npwp = npwp);
	}
	
	public void setNoFormulir(String no_formulir){
		propertyChangeSupport.firePropertyChange("no_formulir", this.no_formulir, this.no_formulir = no_formulir);
	}
	
	public void setNoPendaftaran(String no_pendaftaran){
		propertyChangeSupport.firePropertyChange("no_pendaftaran", this.no_pendaftaran, this.no_pendaftaran = no_pendaftaran);
	}
	
	public void setJenisPajak(String jenis_pajak){
		propertyChangeSupport.firePropertyChange("jenis_pajak", this.jenis_pajak, this.jenis_pajak = jenis_pajak);
	}
	
	public void setJenisAssesment(String jenis_assesment){
		propertyChangeSupport.firePropertyChange("jenis_assesment", this.jenis_assesment, this.jenis_assesment = jenis_assesment);
	}
	
	public void setTanggalDaftar(Date tanggal_daftar){
		propertyChangeSupport.firePropertyChange("tanggal_daftar", this.tanggal_daftar, this.tanggal_daftar = tanggal_daftar);
	}
	
	public void setNamaBadan(String nama_badan){
		propertyChangeSupport.firePropertyChange("nama_badan", this.nama_badan, this.nama_badan = nama_badan);
	}
	
	public void setNamaPemilik(String nama_pemilik){
		propertyChangeSupport.firePropertyChange("nama_pemilik", this.nama_pemilik, this.nama_pemilik = nama_pemilik);
	}
	
	public void setJabatan(String jabatan){
		propertyChangeSupport.firePropertyChange("jabatan", this.jabatan, this.jabatan = jabatan);
	}
	
	public void setNoKartuKeluarga(String no_KK){
		propertyChangeSupport.firePropertyChange("no_KK", this.no_KK, this.no_KK = no_KK);
	}
	
	public void setStatus(String status){
		propertyChangeSupport.firePropertyChange("status", this.status, this.status = status);
	}
	
	public void setAlatingJalan(String alating_jalan){
		propertyChangeSupport.firePropertyChange("alating_jalan", this.alating_jalan, this.alating_jalan = alating_jalan);
	}
	
	public void setAlatingKelurahan(String alating_kelurahan){
		propertyChangeSupport.firePropertyChange("alating_kelurahan", this.alating_kelurahan, this.alating_kelurahan = alating_kelurahan);
	}
	
	public void setAlatingKecamatan(String alating_kecamatan){
		propertyChangeSupport.firePropertyChange("alating_kecamatan", this.alating_kecamatan, this.alating_kecamatan = alating_kecamatan);
	}
	
	public void setAlatingKota(String alating_kota){
		propertyChangeSupport.firePropertyChange("alating_kota", this.alating_kota, this.alating_kota = alating_kota);
	}
	
	public void setAlatingTelepon(String alating_telepon){
		propertyChangeSupport.firePropertyChange("alating_telepon", this.alating_telepon, this.alating_telepon = alating_telepon);
	}
	
	public void setAlatingKodepos(String alating_kodepos){
		propertyChangeSupport.firePropertyChange("alating_kodepos", this.alating_kodepos, this.alating_kodepos = alating_kodepos);
	}
	
	public void setAlabadJalan(String alabad_jalan){
		propertyChangeSupport.firePropertyChange("alabad_jalan", this.alabad_jalan, this.alabad_jalan = alabad_jalan);
	}
	
	public void setAlabadKelurahan(String alabad_kelurahan){
		propertyChangeSupport.firePropertyChange("alabad_kelurahan", this.alabad_kelurahan, this.alabad_kelurahan = alabad_kelurahan);
	}
	
	public void setAlabadKecamatan(String alabad_kecamatan){
		propertyChangeSupport.firePropertyChange("alabad_kecamatan", this.alabad_kecamatan, this.alabad_kecamatan = alabad_kecamatan);
	}
	
	public void setAlabadTelepon(String alabad_telepon){
		propertyChangeSupport.firePropertyChange("alabad_telepon", this.alabad_telepon, this.alabad_telepon = alabad_telepon);
	}
	
	public void setAlabadKodePos(String alabad_kodepos){
		propertyChangeSupport.firePropertyChange("alabad_kodepos", this.alabad_kodepos, this.alabad_kodepos = alabad_kodepos);
	}
	
	public void setKewarganegaraan(String kewarganegaraan){
		propertyChangeSupport.firePropertyChange("kewarganegaraan", this.kewarganegaraan, this.kewarganegaraan = kewarganegaraan);
	}
	
	public void setTandaBuktiDiri(String tanda_bukti_diri){
		propertyChangeSupport.firePropertyChange("tanda_bukti_diri", this.tanda_bukti_diri, this.tanda_bukti_diri = tanda_bukti_diri);
	}
	
	public void setNoBuktiDiri(String no_bukti_diri){
		propertyChangeSupport.firePropertyChange("no_bukti_diri", this.no_bukti_diri, this.no_bukti_diri = no_bukti_diri);
	}
	
	public void setKeterangan(String keterangan){
		propertyChangeSupport.firePropertyChange("keterangan", this.keterangan, this.keterangan = keterangan);
	}
	
	public void setKeteranganTutup(String keterangan_tutup){
		propertyChangeSupport.firePropertyChange("keterangan_tutup", this.keterangan_tutup, this.keterangan_tutup = keterangan_tutup);
	}
	
	public void setBidangUsaha(String bidang_usaha){
		propertyChangeSupport.firePropertyChange("bidang_usaha", this.bidang_usaha, this.bidang_usaha = bidang_usaha);
	}
	
	public void setKodeBidangUsaha(String kode_bid_usaha){
		propertyChangeSupport.firePropertyChange("kode_bid_usaha", this.kode_bid_usaha, this.kode_bid_usaha = kode_bid_usaha);
	}
	
	public void setKewajibanPajak(Integer kewajiban_pajak){
		propertyChangeSupport.firePropertyChange("kewajiban_pajak", this.kewajiban_pajak, this.kewajiban_pajak = kewajiban_pajak);
	}
	
	public void setIdSubPajak(Integer idsub_pajak){
		propertyChangeSupport.firePropertyChange("idsub_pajak", this.idsub_pajak, this.idsub_pajak = idsub_pajak);
	}
	
	public void setTanggalApprove(String tanggal_approve){
		propertyChangeSupport.firePropertyChange("tanggal_approve", this.tanggal_approve, this.tanggal_approve = tanggal_approve);
	}
	
	public void setAktif(Boolean aktif) {
		propertyChangeSupport.firePropertyChange("aktif", this.aktif, this.aktif = aktif);
	}
	
	public void setEkswp(Boolean ekswp) {
		propertyChangeSupport.firePropertyChange("ekswp", this.ekswp, this.ekswp = ekswp);
	}
	
	public void setInsidentil(Boolean insidentil) {
		propertyChangeSupport.firePropertyChange("insidentil", this.insidentil, this.insidentil = insidentil);
	}
	
	public void setInsidentil_Pemerintah(Boolean insidentil_pemerintahan) {
		propertyChangeSupport.firePropertyChange("insidentil_pemerintahan", this.insidentil_pemerintahan, this.insidentil_pemerintahan = insidentil_pemerintahan);
	}
	
	public void setTutup(Boolean tutup) {
		propertyChangeSupport.firePropertyChange("tutup", this.tutup, this.tutup = tutup);
	}
	
	public void setTarif(Integer tarif){
		propertyChangeSupport.firePropertyChange("tarif", this.tarif, this.tarif = tarif);
	}
	
	public String getNPWP(){
		return npwp;
	}
	
	public String getNoPendaftaran(){
		return no_pendaftaran;
	}
	
	public String getNoFormulir(){
		return no_formulir;
	}
	
	public Date getTanggalDaftar(){
		return tanggal_daftar;
	}
		
	public String getNamaBadan(){
		return nama_badan;
	}
	
	public String getNamaPemilik(){
		return nama_pemilik;
	}
	
	public String getNoKartuKeluarga(){
		return no_KK;
	}
	
	public String getJabatan(){
		return jabatan;
	}
	
	public String getJenisAssesment(){
		return jenis_assesment;
	}
	
	public String getJenisPajak(){
		return jenis_pajak;
	}
	
	public String getStatus(){
		return status;
	}
	
	public String getAlatingJalan(){
		return alating_jalan;
	}
	
	public String getAlatingKelurahan(){
		return alating_kelurahan;
	}
	
	public String getAlatingKecamatan(){
		return alating_kecamatan;
	}
	
	public String getAlatingKota(){
		return alating_kota;
	}
	
	public String getAlatingTelepon(){
		return alating_telepon;
	}
	
	public String getAlatingKodePos(){
		return alating_kodepos;
	}
	
	public String getAlabadJalan(){
		return alabad_jalan;
	}
	
	public String getAlabadKelurahan(){
		return alabad_kelurahan;
	}
	
	public String getAlabadKecamatan(){
		return alabad_kecamatan;
	}
	
	public String getAlabadTelepon(){
		return alabad_telepon;
	}
		
	public String getAlabadKodePos(){
		return alabad_kodepos;
	}
	
	public String getKewarganegaraan(){
		return kewarganegaraan;
	}
	
	public String getTandaBuktiDiri(){
		return tanda_bukti_diri;
	}
	
	public String getNoBuktiDiri(){
		return no_bukti_diri;
	}
	
	public String getKeterangan(){
		return keterangan;
	}
	
	public String getKeteranganTutup(){
		return keterangan_tutup;
	}
	
	public String getBidangUsaha(){
		return bidang_usaha;
	}
	
	public String getKode_bid_usaha() {
		return kode_bid_usaha;
	}
	
	public Integer getKewajibanPajak(){
		return kewajiban_pajak;
	}
	
	public Integer getIdSubPajak(){
		return idsub_pajak;
	}
	
	public String getTanggalApprove(){
		return tanggal_approve;
	}

	public Integer getTarif(){
		return tarif;
	}
	public Boolean getAktif() {
		return aktif;
	}
	
	public Boolean getEkswp() {
		return ekswp;
	}
	
	public Boolean getInsidentil() {
		return insidentil;
	}
	
	public Boolean getInsidentil_Pemerintah() {
		return insidentil_pemerintahan;
	}
	
	public Boolean getTutup() {
		return tutup;
	}
	
	public void setNamaBidangUsaha(String bidangUsaha){
		this.bidangUsaha.setNamaBidUsaha(bidangUsaha);
	}
	
	public String getNamaBidangUsaha(){
		return this.bidangUsaha.getNamaBidUsaha();
	}
	
	public void setKodePajak(String kodePajak){
		this.pajak.setKodePajak(kodePajak);
	}
	
	public String getKodePajak(){
		return this.pajak.getKodePajak();
	}
	
	public void setNamaPajak(String namaPajak){
		this.pajak.setNamaPajak(namaPajak);
	}
	
	public String getNamaPajak(){
		return this.pajak.getNamaPajak();
	}
	
	public void setNoUrut(int i){
		noUrut = i;
	}
	
	public Integer getNoUrut() {
		return noUrut;
	}
	
}