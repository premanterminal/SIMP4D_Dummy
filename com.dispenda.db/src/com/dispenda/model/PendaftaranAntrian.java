package com.dispenda.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.sql.Array;
import java.sql.Date;

public class PendaftaranAntrian {
	private Integer index = null;
	private String no_formulir = "" ;
	private String nama_badan = "" ;
	private String nama_pemilik = "" ;
	private String jabatan = "" ;
	private String jenis_assesment = "" ;
	private String jenis_pajak = "" ;
	private Date tanggal_daftar;
	private Integer bidang_usaha;
	private String keterangan = "" ;
	private Integer kewajiban_pajak = 0 ;
	private String alating_jalan = "" ;
	private String alating_kelurahan = "" ;
	private String alating_kecamatan = "" ;
	private String alating_telepon = "" ;
	private String alating_kodepos = "" ;
	private String alabad_jalan = "" ;
	private String alabad_kelurahan = "" ;
	private String alabad_kecamatan = "" ;
	private String alabad_telepon = "" ;
	private String alabad_kodepos = "" ;
	private String kewarganegaraan = "" ;
	private String tanda_bukti_diri = "" ;
	private String no_bukti_diri = "" ;
	private String no_KK = "" ;
	private Boolean isidentil;
//	private Boolean insidentil_pemerintahan;
	private Array suratIzin;
	private Array noSurat;
	private Array tglSurat;
	private String ketReject;
	private Date dateApproval;
	private Date dateInput;
	private String alatingKota = "";
	private Boolean tolak;
	private java.util.Date tanggal_tolak;
	private String file_identitas = "";
	private Array file_izin;
	private String nama_bid_usaha;
	
	private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
	
	public PendaftaranAntrian(){}
	
	public PendaftaranAntrian(Integer index, String no_formulir, String nama_badan, String nama_pemilik, String jabatan, String jenis_assesment, 
			String jenis_pajak, Date tanggal_daftar, Integer bidang_usaha, String keterangan, Integer kewajiban_pajak, String alating_jalan, String alating_kelurahan,
			String alating_kecamatan, String alating_telepon, String alating_kodepos, String alabad_jalan, 
			String alabad_kelurahan, String alabad_kecamatan, String alabad_telepon, String alabad_kodepos,
			String kewarganegaraan, String tanda_bukti_diri, String no_bukti_diri, String no_KK, 
			Boolean isidentil, 
//			Boolean insidentil_pemerintahan,Array suratIzin, Array noSurat, Array tglSurat, String ketReject,
			String file_identitas, Array file_izin, String nama_bid_usaha)
	{
		this.index = index ;
		this.no_formulir = no_formulir ;
		this.nama_badan = nama_badan ;
		this.nama_pemilik = nama_pemilik ;
		this.jabatan = jabatan ;
		this.jenis_assesment = jenis_assesment ;
		this.jenis_pajak = jenis_pajak ;
		this.tanggal_daftar = tanggal_daftar ;
		this.bidang_usaha = bidang_usaha ;
		this.keterangan = keterangan ;
		this.kewajiban_pajak = kewajiban_pajak ;
		this.alating_jalan = alating_jalan ;
		this.alating_kelurahan = alating_kelurahan ;
		this.alating_kecamatan = alating_kecamatan ;
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
		this.no_KK = no_KK ;
		this.isidentil = isidentil;
//		this.insidentil_pemerintahan = insidentil_pemerintahan;
		this.suratIzin = suratIzin;
		this.noSurat = noSurat;
		this.ketReject = ketReject;
		this.file_identitas = file_identitas;
		this.file_izin = file_izin;
		this.nama_bid_usaha = nama_bid_usaha;
	}
	
	public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener){
		propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
	}
	
	public void removePropertyChangeListener(PropertyChangeListener listener){
		propertyChangeSupport.removePropertyChangeListener(listener);
	}
	
	public void setIndex(Integer index){
		propertyChangeSupport.firePropertyChange("index", this.index, this.index = index);
	}
	
	public void setNoFormulir(String no_formulir){
		propertyChangeSupport.firePropertyChange("no_formulir", this.no_formulir, this.no_formulir = no_formulir);
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
	
	public void setJenisAssesment(String jenis_assesment){
		propertyChangeSupport.firePropertyChange("jenis_assesment", this.jenis_assesment, this.jenis_assesment = jenis_assesment);
	}
	
	public void setJenisPajak(String jenis_pajak){
		propertyChangeSupport.firePropertyChange("jenis_pajak", this.jenis_pajak, this.jenis_pajak = jenis_pajak);
	}
	
	public void setTanggalDaftar(Date tanggal_daftar){
		propertyChangeSupport.firePropertyChange("tanggal_daftar", this.tanggal_daftar, this.tanggal_daftar = tanggal_daftar);
	}
	
	public void setIdSubPajak(Integer bidang_usaha){
		propertyChangeSupport.firePropertyChange("bidang_usaha", this.bidang_usaha, this.bidang_usaha = bidang_usaha);
	}
	
	public void setKeterangan(String keterangan){
		propertyChangeSupport.firePropertyChange("keterangan", this.keterangan, this.keterangan = keterangan);
	}
	
	public void setKewajibanPajak(Integer kewajiban_pajak){
		propertyChangeSupport.firePropertyChange("kewajiban_pajak", this.kewajiban_pajak, this.kewajiban_pajak = kewajiban_pajak);
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
	
	public void setAlatingPendaftaran(String alating_telepon){
		propertyChangeSupport.firePropertyChange("alating_telepon", this.alating_telepon, this.alating_telepon = alating_telepon);
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
	
	public void setNoKartuKeluraga(String no_KK){
		propertyChangeSupport.firePropertyChange("no_KK", this.no_KK, this.no_KK = no_KK);
	}
	
	public void setInsidentil(Boolean isidentil) {
		propertyChangeSupport.firePropertyChange("isidentil", this.isidentil, this.isidentil = isidentil);
	}
//	public void setInsidentil_Pemerintah(Boolean insidentil_pemerintahan) {
//		propertyChangeSupport.firePropertyChange("insidentil_pemerintahan", this.insidentil_pemerintahan, this.insidentil_pemerintahan = insidentil_pemerintahan);
//	}
	
	public void setSuratIzin(Array suratIzin) {
		propertyChangeSupport.firePropertyChange("suratIzin", this.suratIzin, this.suratIzin = suratIzin);
	}
	
	public void setNoSurat(Array noSurat) {
		propertyChangeSupport.firePropertyChange("noSurat", this.noSurat, this.noSurat = noSurat);
	}
	
	public void setTglSurat(Array tglSurat) {
		propertyChangeSupport.firePropertyChange("tglSurat", this.tglSurat, this.tglSurat = tglSurat);
	}
	
	public void setKetReject(String ketReject) {
		propertyChangeSupport.firePropertyChange("ketReject", this.ketReject, this.ketReject = ketReject);
	}
	
	public void setDateApproval(Date dateApproval) {
		propertyChangeSupport.firePropertyChange("dateApproval", this.dateApproval, this.dateApproval = dateApproval);
	}
	
	public void setDateInput(Date dateInput) {
		propertyChangeSupport.firePropertyChange("dateInput", this.dateInput, this.dateInput = dateInput);
	}
	
	public void setAlatingKota(String alatingKota) {
		propertyChangeSupport.firePropertyChange("alatingKota", this.alatingKota, this.alatingKota = alatingKota);
	}
	
	public void setTolak(Boolean tolak) {
		propertyChangeSupport.firePropertyChange("tolak", this.tolak, this.tolak = tolak);
	}
	
	public void setTanggalTolak(java.util.Date tanggal_tolak) {
		propertyChangeSupport.firePropertyChange("tanggal_tolak", this.tanggal_tolak, this.tanggal_tolak = tanggal_tolak);
	}
	
	public void setFile_identitas(String file_identitas) {
		this.file_identitas = file_identitas;
	}
	
	public void setFile_izin(Array file_izin) {
		this.file_izin = file_izin;
	}
	
	public void setNamaBid_Usaha(String nama_bid_usaha) {
		this.nama_bid_usaha = nama_bid_usaha;
	}
	
	public String getAlatingKota() {
		return alatingKota;
	}
	
	public Integer getIndex() {
		return index;
	}
	
	public String getNoFormulir(){
		return no_formulir;
	}
	
	public String getNamaBadan(){
		return nama_badan;
	}
	
	public String getNamaPemilik(){
		return nama_pemilik;
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
	
	public Date getTanggalDaftar(){
		return tanggal_daftar;
	}
	
	public Integer getIdSubPajak(){
		return bidang_usaha;
	}
	
	public String getKeterangan(){
		return keterangan;
	}
	
	public Integer getKewajibanPajak(){
		return kewajiban_pajak;
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
	
	public String getNoKartuKeluarga(){
		return no_KK;
	}
	
	public Boolean getIsidentil() {
		return isidentil;
	}
	
//	public Boolean getInsidentil_Pemerintah() {
//		return insidentil_pemerintahan;
//	}
	
	public Array getSuratIzin() {
		return suratIzin;
	}
	
	public Array getNoSurat() {
		return noSurat;
	}
	
	public Array getTglSurat() {
		return tglSurat;
	}
	
	public String getKetReject() {
		return ketReject;
	}
	
	public Date getDateApproval() {
		return dateApproval;
	}
	
	public Date getDateInput() {
		return dateInput;
	}
	
	public Boolean getTolak() {
		return tolak;
	}
	
	public java.util.Date getTanggalTolak() {
		return tanggal_tolak;
	}
	
	public String getFile_identitas() {
		return file_identitas;
	}
	
	public Array getFile_izin() {
		return file_izin;
	}
	
	public String getNamaBid_Usaha() {
		return nama_bid_usaha;
	}
}