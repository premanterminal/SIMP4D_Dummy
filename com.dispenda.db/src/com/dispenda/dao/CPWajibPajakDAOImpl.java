package com.dispenda.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;

import com.dispenda.controller.ControllerFactory;
import com.dispenda.database.DBConnection;
import com.dispenda.database.DBOperation;
import com.dispenda.model.PendaftaranWajibPajak;
import com.dispenda.model.UUwp;
import com.dispenda.model.WpTutup;

public class CPWajibPajakDAOImpl{
	private DBOperation db = new DBOperation();
	
	public boolean savePendaftaranWajibPajak(PendaftaranWajibPajak wajibPajakModel,String npwp){
		String query = "MERGE INTO WAJIB_PAJAK USING (VALUES(" +
				"'"+ wajibPajakModel.getNPWP()+"'," +
				"'"+ wajibPajakModel.getNoFormulir()+"'," +
				"'"+ wajibPajakModel.getNoPendaftaran()+"'," +
				"'"+ wajibPajakModel.getJenisPajak()+"'," +
				"'"+ wajibPajakModel.getJenisAssesment()+"'," +
				"'"+ wajibPajakModel.getTanggalDaftar()+"'," +
				"'"+ wajibPajakModel.getNamaBadan()+"'," +
				"'"+ wajibPajakModel.getNamaPemilik()+"'," +
				"'"+ wajibPajakModel.getJabatan()+"'," +
				""+ wajibPajakModel.getKewajibanPajak()+"," +
				""+ wajibPajakModel.getIdSubPajak()+"," +
				"'"+ wajibPajakModel.getAlabadJalan()+"'," +
				"'"+ wajibPajakModel.getAlabadKecamatan()+"'," +
				"'"+ wajibPajakModel.getAlabadKelurahan()+"'," +
				"'"+ wajibPajakModel.getAlabadKodePos()+"'," +
				"'"+ wajibPajakModel.getAlabadTelepon()+"'," +
				"'"+ wajibPajakModel.getAlatingJalan()+"'," +
				"'"+ wajibPajakModel.getAlatingKecamatan()+"'," +
				"'"+ wajibPajakModel.getAlatingKelurahan()+"'," +
				"'"+ wajibPajakModel.getAlatingKota()+"'," +
				"'"+ wajibPajakModel.getAlatingKodePos()+"'," +
				"'"+ wajibPajakModel.getAlatingTelepon()+"'," +
				"'"+ wajibPajakModel.getKewarganegaraan()+"'," +
				"'"+ wajibPajakModel.getTandaBuktiDiri()+"'," +
				"'"+ wajibPajakModel.getNoBuktiDiri()+"'," +
				"'"+ wajibPajakModel.getNoKartuKeluarga()+"'," +
				"'"+ wajibPajakModel.getStatus()+"'," +
				"'"+ wajibPajakModel.getKeterangan()+"'," +
				"'"+ wajibPajakModel.getTanggalApprove()+"'," +
				"'"+ wajibPajakModel.getBidangUsaha()+"'," +
				""+ wajibPajakModel.getInsidentil()+"," +
				""+ wajibPajakModel.getInsidentil_Pemerintah()+"," +
				"'"+ wajibPajakModel.getKeteranganTutup()+"'," +
				"'"+ ControllerFactory.getMainController().getCpWajibPajakDAOImpl().getDateNow().toString().split(" ")[0]+"'," +
				""+ wajibPajakModel.getAktif()+"," +
				""+ wajibPajakModel.getTutup()+"," +
				""+ wajibPajakModel.getEkswp()+"" +
						"))" +
			" AS vals(" +
				"npwp," +
				"no_formulir," +
				"no_daftar," +
				"jns_pajak," +
				"jns_assesment," +
				"tgl_daftar," +
				"nama_badan," +
				"nama_pemilik," +
				"jabatan," +
				"kewajiban_pajak," +
				"id_subpajak," +
				"alabad_jalan," +
				"alabad_kecamatan," +
				"alabad_kelurahan," +
				"alabad_kodepos," +
				"alabad_telepon," +
				"alating_jalan," +
				"alating_kecamatan," +
				"alating_kelurahan," +
				"alating_kota," +
				"alating_kodepos," +
				"alating_telepon," +
				"warneg," +
				"bukti_diri," +
				"no_bukti_diri," +
				"no_KK," +
				"status," +
				"keterangan," +
				"tgl_approve," +
				"bid_usaha," +
				"insidentil," +
				"insidentil_pemerintahan," +
				"ket_tutup," +
				"tgl_input," +
				"aktif," +
				"tutup," +
				"ekswp" +
				")" +
			" ON WAJIB_PAJAK.NPWPD = " +npwp+
			" WHEN MATCHED THEN UPDATE SET " +
				"WAJIB_PAJAK.NPWPD = vals.npwp,"+
				"WAJIB_PAJAK.NO_FORMULIR = vals.no_formulir," +
				"WAJIB_PAJAK.NO_PENDAFTARAN = vals.no_daftar," +
				"WAJIB_PAJAK.JENIS_PAJAK = vals.jns_pajak," +
				"WAJIB_PAJAK.JENIS_ASSESMENT = vals.jns_assesment," +
				"WAJIB_PAJAK.TANGGAL_DAFTAR = vals.tgl_daftar," +
				"WAJIB_PAJAK.NAMA_BADAN = vals.nama_badan," +
				"WAJIB_PAJAK.NAMA_PEMILIK = vals.nama_pemilik," +
				"WAJIB_PAJAK.JABATAN = vals.jabatan," +
				"WAJIB_PAJAK.KEWAJIBAN_PAJAK = vals.kewajiban_pajak," +
				"WAJIB_PAJAK.IDSUB_PAJAK = vals.id_subpajak," +
				"WAJIB_PAJAK.ALABAD_JALAN = vals.alabad_jalan," +
				"WAJIB_PAJAK.ALABAD_KECAMATAN = vals.alabad_kecamatan," +
				"WAJIB_PAJAK.ALABAD_KELURAHAN = vals.alabad_kelurahan," +
				"WAJIB_PAJAK.ALABAD_KODEPOS = vals.alabad_kodepos," +
				"WAJIB_PAJAK.ALABAD_TELEPON = vals.alabad_telepon," +
				"WAJIB_PAJAK.ALATING_JALAN = vals.alating_jalan," +
				"WAJIB_PAJAK.ALATING_KECAMATAN = vals.alating_kecamatan," +
				"WAJIB_PAJAK.ALATING_KELURAHAN = vals.alating_kelurahan," +
				"WAJIB_PAJAK.ALATING_KOTA = vals.alating_kota," +
				"WAJIB_PAJAK.ALATING_KODEPOS = vals.alating_kodepos," +
				"WAJIB_PAJAK.ALATING_TELEPON = vals.alating_telepon," +
				"WAJIB_PAJAK.KEWARGANEGARAAN = vals.warneg," +
				"WAJIB_PAJAK.TANDA_BUKTI_DIRI = vals.bukti_diri," +
				"WAJIB_PAJAK.NO_BUKTI_DIRI = vals.no_bukti_diri," +
				"WAJIB_PAJAK.NO_KARTU_KELUARGA = vals.no_KK," +
				"WAJIB_PAJAK.STATUS = vals.status," +
				"WAJIB_PAJAK.KETERANGAN = vals.keterangan," +
				"WAJIB_PAJAK.TANGGAL_APPROVE = vals.tgl_approve," +
				"WAJIB_PAJAK.KODE_BID_USAHA = vals.bid_usaha," +
				"WAJIB_PAJAK.INSIDENTIL = vals.insidentil," +
				"WAJIB_PAJAK.INSIDENTIL_PEMERINTAHAN = vals.insidentil_pemerintahan," +
				"WAJIB_PAJAK.KETERANGAN_TUTUP = vals.ket_tutup," +
				"WAJIB_PAJAK.TANGGAL_INPUT = vals.tgl_input," +
				"WAJIB_PAJAK.AKTIF = vals.aktif," +
				"WAJIB_PAJAK.TUTUP = vals.tutup," +
				"WAJIB_PAJAK.EKSWP = vals.ekswp" +
			" WHEN NOT MATCHED THEN INSERT VALUES " +
				"vals.npwp," +
				"vals.no_formulir," +
				"vals.no_daftar," +
				"vals.jns_pajak," +
				"vals.jns_assesment," +
				"vals.tgl_daftar," +
				"vals.nama_badan," +
				"vals.nama_pemilik," +
				"vals.jabatan," +
				"vals.kewajiban_pajak," +
				"vals.id_subpajak," +
				"vals.alabad_jalan," +
				"vals.alabad_kecamatan," +
				"vals.alabad_kelurahan," +
				"vals.alabad_kodepos," +
				"vals.alabad_telepon," +
				"vals.alating_jalan," +
				"vals.alating_kecamatan," +
				"vals.alating_kelurahan," +
				"vals.alating_kota," +
				"vals.alating_kodepos," +
				"vals.alating_telepon," +
				"vals.warneg," +
				"vals.bukti_diri," +
				"vals.no_bukti_diri," +
				"vals.no_KK," +
				"vals.status," +
				"vals.keterangan," +
				"vals.tgl_approve," +
				"vals.bid_usaha," +
				"vals.insidentil," +
				"vals.insidentil_pemerintahan," +
				"vals.ket_tutup," +
				"vals.tgl_input," +
				"vals.aktif," +
				"vals.tutup," +
				"vals.ekswp";
		boolean result = db.ExecuteStatementQuery(query, DBConnection.INSTANCE.open());
		if(!result)
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada MERGE(UPDATE/INSERT) tabel WAJIB_PAJAK.");
		return result;
	}
	
	public List<PendaftaranWajibPajak> getPendaftaranWajibPajak(Date tglApprove){
		String query = "SELECT * FROM WAJIB_PAJAK WP LEFT JOIN BIDANG_USAHA BU ON BU.IDSUB_PAJAK = WP.IDSUB_PAJAK WHERE WP.TANGGAL_APPROVE = '"+tglApprove.toString().substring(0, 10)+"'";
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		if(result != null){
			try{
				List<PendaftaranWajibPajak> listPendaftaranWajibPajak = new ArrayList<PendaftaranWajibPajak>();
				while(result.next()){
					PendaftaranWajibPajak wajibPajakModel = new PendaftaranWajibPajak();
					wajibPajakModel.setNPWP(result.getString("NPWPD"));
					wajibPajakModel.setNoFormulir(result.getString("NO_FORMULIR"));
					wajibPajakModel.setNoPendaftaran(result.getString("NO_PENDAFTARAN"));
					wajibPajakModel.setTanggalDaftar(result.getDate("TANGGAL_DAFTAR"));
					wajibPajakModel.setNamaBadan(result.getString("NAMA_BADAN"));
					wajibPajakModel.setNamaPemilik(result.getString("NAMA_PEMILIK"));
					wajibPajakModel.setNoKartuKeluarga(result.getString("NO_KARTU_KELUARGA"));
					wajibPajakModel.setJabatan(result.getString("JABATAN"));
					wajibPajakModel.setJenisAssesment(result.getString("JENIS_ASSESMENT"));
					wajibPajakModel.setJenisPajak(result.getString("JENIS_PAJAK"));
					wajibPajakModel.setStatus(result.getString("STATUS"));
					wajibPajakModel.setAlatingJalan(result.getString("ALATING_JALAN"));
					wajibPajakModel.setAlatingKelurahan(result.getString("ALATING_KELURAHAN"));
					wajibPajakModel.setAlatingKecamatan(result.getString("ALATING_KECAMATAN"));
					wajibPajakModel.setAlatingKota(result.getString("ALATING_KOTA"));
					wajibPajakModel.setAlatingTelepon(result.getString("ALATING_TELEPON"));
					wajibPajakModel.setAlatingKodepos(result.getString("ALATING_KODEPOS"));
					wajibPajakModel.setAlabadJalan(result.getString("ALABAD_JALAN"));
					wajibPajakModel.setAlabadKelurahan(result.getString("ALABAD_KELURAHAN"));
					wajibPajakModel.setAlabadKecamatan(result.getString("ALABAD_KECAMATAN"));
					wajibPajakModel.setAlabadTelepon(result.getString("ALABAD_TELEPON"));
					wajibPajakModel.setAlabadKodePos(result.getString("ALABAD_KODEPOS"));
					wajibPajakModel.setKewarganegaraan(result.getString("KEWARGANEGARAAN"));
					wajibPajakModel.setTandaBuktiDiri(result.getString("TANDA_BUKTI_DIRI"));
					wajibPajakModel.setNoBuktiDiri(result.getString("NO_BUKTI_DIRI"));
					wajibPajakModel.setKeterangan(result.getString("KETERANGAN"));
					wajibPajakModel.setBidangUsaha(result.getString("KODE_BID_USAHA"));
					wajibPajakModel.setKewajibanPajak(result.getInt("KEWAJIBAN_PAJAK"));
					wajibPajakModel.setTanggalApprove(result.getString("TANGGAL_APPROVE"));
					wajibPajakModel.setAktif(result.getBoolean("AKTIF"));
					wajibPajakModel.setTutup(result.getBoolean("TUTUP"));
					wajibPajakModel.setEkswp(result.getBoolean("EKSWP"));
					wajibPajakModel.setInsidentil(result.getBoolean("INSIDENTIL"));
					wajibPajakModel.setInsidentil_Pemerintah(result.getBoolean("INSIDENTIL_PEMERINTAHAN"));
					wajibPajakModel.setKeteranganTutup(result.getString("KETERANGAN_TUTUP"));
					wajibPajakModel.setIdSubPajak(result.getInt("IDSUB_PAJAK"));
					wajibPajakModel.setNamaBidangUsaha(result.getString("NAMA_BID_USAHA"));
					listPendaftaranWajibPajak.add(wajibPajakModel);
				}
				return listPendaftaranWajibPajak;
			}catch(SQLException e){
				MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database SQL Error", e.getMessage());
				return null;
			}
		}
		else{
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada pembuatan LOAD DATA tabel WAJIB_PAJAK.");
			return null;
		}
	}
	
	public List<UUwp> getUUWajibPajak(Integer idPajak) {
		String query = "select * from uu " +
				"where uu.idpajak = "+idPajak;
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		if(result != null){
			try{
				List<UUwp> listUU = new ArrayList<UUwp>();
				while(result.next()){
					UUwp uu = new UUwp();
					uu.setUu(result.getString("UNDANG_UNDANG"));
					uu.setIdPajak(result.getInt("IDPAJAK"));
					listUU.add(uu);
				}
				return listUU;
			} catch(SQLException e){
				MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database SQL Error", e.getMessage());
				return null;
			}
		}
		else{
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada pembuatan LOAD DATA tabel WAJIB_PAJAK.");
			return null;
		}
	}
	
	public List<PendaftaranWajibPajak> getAllPendaftaranWajibPajak() {
		String query = "select * from wajib_pajak " +
				"inner join PAJAK on pajak.KODE_PAJAK = wajib_pajak.KEWAJIBAN_PAJAK " +
				"left join BIDANG_USAHA on bidang_usaha.IDSUB_PAJAK = wajib_pajak.IDSUB_PAJAK " +
				"left join kecamatan on kecamatan.KODE_KECAMATAN = wajib_pajak.ALABAD_KECAMATAN " +
				"left join kelurahan on kelurahan.KECAMATAN_IDKECAMATAN = kecamatan.IDKEC and kelurahan.KODE_KELURAHAN = wajib_pajak.ALABAD_KELURAHAN " +
				"order by substring(wajib_pajak.npwpd,2,7)";
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		if(result != null){
			try{
				List<PendaftaranWajibPajak> listPendaftaranWajibPajak = new ArrayList<PendaftaranWajibPajak>();
				int i = 0;
				while(result.next()){
					PendaftaranWajibPajak wajibPajakModel = new PendaftaranWajibPajak();
					wajibPajakModel.setNoUrut(++i);
					wajibPajakModel.setNPWP(result.getString("NPWPD"));
					wajibPajakModel.setNoFormulir(result.getString("NO_FORMULIR"));
					wajibPajakModel.setNoPendaftaran(result.getString("NO_PENDAFTARAN"));
					wajibPajakModel.setTanggalDaftar(result.getDate("TANGGAL_DAFTAR"));
					wajibPajakModel.setNamaBadan(result.getString("NAMA_BADAN"));
					wajibPajakModel.setNamaPemilik(result.getString("NAMA_PEMILIK"));
					wajibPajakModel.setNoKartuKeluarga(result.getString("NO_KARTU_KELUARGA"));
					wajibPajakModel.setJabatan(result.getString("JABATAN"));
					wajibPajakModel.setJenisAssesment(result.getString("JENIS_ASSESMENT"));
					wajibPajakModel.setJenisPajak(result.getString("JENIS_PAJAK"));
					wajibPajakModel.setStatus(result.getString("STATUS"));
					wajibPajakModel.setAlatingJalan(result.getString("ALATING_JALAN"));
					wajibPajakModel.setAlatingKelurahan(result.getString("ALATING_KELURAHAN"));
					wajibPajakModel.setAlatingKecamatan(result.getString("ALATING_KECAMATAN"));
					wajibPajakModel.setAlatingKota(result.getString("ALATING_KOTA"));
					wajibPajakModel.setAlatingTelepon(result.getString("ALATING_TELEPON"));
					wajibPajakModel.setAlatingKodepos(result.getString("ALATING_KODEPOS"));
					wajibPajakModel.setAlabadJalan(result.getString("ALABAD_JALAN"));
					wajibPajakModel.setIdSubPajak(result.getInt("IdSub_Pajak"));
					if(result.getString("NAME_KELURAHAN") == null)
						wajibPajakModel.setAlabadKelurahan("");
					else
						wajibPajakModel.setAlabadKelurahan(result.getString("NAME_KELURAHAN"));
					if(result.getString("NAME_KECAMATAN") == null)
						wajibPajakModel.setAlabadKecamatan("");
					else
						wajibPajakModel.setAlabadKecamatan(result.getString("NAME_KECAMATAN"));
					wajibPajakModel.setAlabadTelepon(result.getString("ALABAD_TELEPON"));
					wajibPajakModel.setAlabadKodePos(result.getString("ALABAD_KODEPOS"));
					wajibPajakModel.setKewarganegaraan(result.getString("KEWARGANEGARAAN"));
					wajibPajakModel.setTandaBuktiDiri(result.getString("TANDA_BUKTI_DIRI"));
					wajibPajakModel.setNoBuktiDiri(result.getString("NO_BUKTI_DIRI"));
					wajibPajakModel.setKeterangan(result.getString("KETERANGAN"));
					wajibPajakModel.setBidangUsaha(result.getString("KODE_BID_USAHA"));
					wajibPajakModel.setNamaBidangUsaha(result.getString("NAMA_BID_USAHA"));
					wajibPajakModel.setKewajibanPajak(result.getInt("KEWAJIBAN_PAJAK"));
					wajibPajakModel.setNamaPajak(result.getString("NAMA_PAJAK"));
					wajibPajakModel.setKodePajak(result.getString("KODE_PAJAK"));
					wajibPajakModel.setTanggalApprove(result.getString("TANGGAL_APPROVE"));
					wajibPajakModel.setAktif(result.getBoolean("AKTIF"));
					wajibPajakModel.setTutup(result.getBoolean("TUTUP"));
					wajibPajakModel.setEkswp(result.getBoolean("EKSWP"));
					wajibPajakModel.setInsidentil(result.getBoolean("INSIDENTIL"));
					wajibPajakModel.setInsidentil_Pemerintah(result.getBoolean("INSIDENTIL_PEMERINTAHAN"));
//					System.out.println("insi get"+result.getBoolean("INSIDENTIL"));
//					System.out.println("insi p get"+result.getBoolean("INSIDENTIL_PEMERINTAHAN"));
					wajibPajakModel.setKeteranganTutup(result.getString("KETERANGAN_TUTUP"));
//					wajibPajakModel.setUu(result.getString("UNDANG_UNDANG"));
					listPendaftaranWajibPajak.add(wajibPajakModel);
				}
				return listPendaftaranWajibPajak;
			} catch(SQLException e){
				MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database SQL Error", e.getMessage());
				return null;
			}
		}
		else{
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada pembuatan LOAD DATA tabel WAJIB_PAJAK.");
			return null;
		}
	}
	
	public boolean deleteWajibPajak(String npwp){
		String query = "DELETE FROM WAJIB_PAJAK WHERE NPWPD = "+npwp;
		boolean result = db.ExecuteStatementQuery(query, DBConnection.INSTANCE.open());
		if(!result)
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada DELETE tabel WAJIB_PAJAK dengan NPWP = "+npwp);
		return result;
	}
	
	public Integer getLastWajibPajak(){
		String query = "SELECT TOP 1 NPWPD FROM WAJIB_PAJAK ORDER BY NPWPD DESC";
		try{
			ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
			if(result.next()){
				return result.getInt("NPWPD");
			}
			else
				return 0;
		}
		catch(SQLException e){
			e.printStackTrace();
			return 0;
		}
	}
	
	@SuppressWarnings("deprecation")
	public PendaftaranWajibPajak getNPWPD(Integer NoUrut)
	{
		String Query = "Select * from GetNPWPD where CAST(SUBSTRING(NPWPD, 2, 7) AS INTEGER) = '"+ NoUrut +"'";
		ResultSet result = db.ResultExecutedStatementQuery(Query, DBConnection.INSTANCE.open());
		if (result != null)
		{
			try
			{
				PendaftaranWajibPajak wp = new PendaftaranWajibPajak();
				if (result.next())
				{
					wp.setNPWP(result.getString("NPWPD"));
					wp.setNamaBadan(result.getString("Nama_Badan"));
					wp.setNamaPajak(result.getString("Nama_Pajak"));
					wp.setAlabadJalan(result.getString("Alabad_Jalan"));
					wp.setNamaPemilik(result.getString("Nama_Pemilik"));
					wp.setIdSubPajak(result.getInt("IdSub_Pajak"));
					wp.setKewajibanPajak(result.getInt("IDPAJAK"));
					wp.setStatus(result.getString("Status"));
					wp.setKeterangan(result.getString("Keterangan"));
					wp.setJenisAssesment(result.getString("Jenis_Assesment"));
					wp.setBidangUsaha(result.getString("Nama_Bid_Usaha"));
					wp.setKodeBidangUsaha(result.getString("Kode_Bid_Usaha"));
					wp.setTarif(result.getInt("Tarif"));
					wp.setInsidentil(result.getBoolean("Insidentil"));
					wp.setInsidentil_Pemerintah(result.getBoolean("Insidentil_pemerintahan"));
					if (result.getString("Alating_Jalan")==null)
						wp.setAlatingJalan("");
					else
						wp.setAlatingJalan(result.getString("Alating_Jalan"));
					if (result.getDate("Tanggal_Daftar")==null)
						wp.setTanggalDaftar(new java.sql.Date(1900, 01, 01));
					else
						wp.setTanggalDaftar(result.getDate("Tanggal_Daftar"));
					if(result.getString("Keterangan_Tutup")==null)
						wp.setKeteranganTutup("");
					else
						wp.setKeteranganTutup(result.getString("Keterangan_Tutup"));
					return wp;
				}
				else
					return null;
			}
			catch(SQLException e)
			{
				MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database SQL Error", e.getMessage());
				return null;
			}
		}
		else{
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada pembuatan LOAD DATA tabel SPTPD.");
			return null;
		}
	}
	
	public Timestamp getDateNow()
	{
		String Query = "select top 1 current_timestamp as Tanggal from PEJABAT";
		java.sql.Timestamp tDate;
		ResultSet result = db.ResultExecutedStatementQuery(Query, DBConnection.INSTANCE.open());
		if (result != null)
		{
			try
			{
				if(result.next())
				{
					tDate = result.getTimestamp("Tanggal");
					return tDate;
				}
			}
			catch(SQLException ex)
			{
				MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database SQL Error", ex.getMessage());
				return null;
			}
		}
		return null;
	}
	
	public String getNoPendaftaran(String tahun)
	{
		String query = "select casewhen(COUNT(No_Pendaftaran) > 0, CONCAT(MAX(CAST(SUBSTRING(No_Pendaftaran , 0, length(No_Pendaftaran) - 4) AS INTEGER)) + 1, '/', '" + tahun + "'), " +
				"CONCAT('1/','" + tahun + "')) from wajib_pajak WHERE YEAR(Tanggal_Approve) = '" + tahun + "';";
		
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		if (result != null)
		{
			try
			{
				if(result.next())
				{
					return result.getString(1);
				}
			}
			catch(SQLException ex)
			{
				MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database SQL Error", ex.getMessage());
				return null;
			}
		}
		return null;
	}
	
	public String getNewNPWPD()
	{
		String query = "SELECT COALESCE(MAX(CAST(SUBSTR(NPWPD, 2, 7) AS INTEGER)) + 1, 1) FROM wajib_pajak;";
		
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		if (result != null)
		{
			try
			{
				if(result.next())
				{
					return String.format("%07d", result.getInt(1));
				}
			}
			catch(SQLException ex)
			{
				MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database SQL Error", ex.getMessage());
				return null;
			}
		}
		return null;
	}

	public boolean saveWajibPajakTutup(WpTutup wajibPajakModel){
		String query = "MERGE INTO WP_TUTUP USING (VALUES(" +
				"null,"+
				"'"+ wajibPajakModel.getNpwpd()+"'," +
				"'"+ wajibPajakModel.getNoSuratTutup()+"'," +
				"'"+ wajibPajakModel.getTglMulaiTutup()+"'," +
				"'"+ wajibPajakModel.getTglSampaiTutup()+"'," +
				"'"+ wajibPajakModel.getKeterangan()+"'" +
						"))" +
			" AS vals(" +
				"id," +
				"npwp," +
				"nosurat," +
				"tglmulai," +
				"tglsampai," +
				"ket" +
				")" +
			" ON WP_TUTUP.ID = vals.id" +
			" WHEN MATCHED THEN UPDATE SET " +
				"WP_TUTUP.NPWPD = vals.npwp,"+
				"WP_TUTUP.NO_SURAT = vals.nosurat," +
				"WP_TUTUP.TGL_MULAI = vals.tglmulai," +
				"WP_TUTUP.TGL_SAMPAI = vals.tglsampai," +
				"WP_TUTUP.KETERANGAN = vals.ket" +
			" WHEN NOT MATCHED THEN INSERT VALUES " +
				"vals.id," +
				"vals.npwp," +
				"vals.nosurat," +
				"vals.tglmulai," +
				"vals.tglsampai," +
				"vals.ket;" ;
		boolean result = db.ExecuteStatementQuery(query, DBConnection.INSTANCE.open());
		if(!result)
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada MERGE(UPDATE/INSERT) tabel WAJIB_PAJAK.");
		return result;
	}
	
	public WpTutup getWajibPajakTutup(String npwpd){
		String Query = "Select * from WP_TUTUP where WP_TUTUP.NPWPD = '"+ npwpd +"' ORDER BY WP_TUTUP.TGL_SAMPAI DESC;";
		ResultSet result = db.ResultExecutedStatementQuery(Query, DBConnection.INSTANCE.open());
		if (result != null){
			try	{
				WpTutup wp = new WpTutup();
				if (result.next()){
					wp.setNoSuratTutup(result.getString("NO_SURAT"));
					wp.setTglMulaiTutup(result.getDate("TGL_MULAI"));
					wp.setTglSampaiTutup(result.getDate("TGL_SAMPAI"));
					wp.setKeterangan(result.getString("KETERANGAN"));
					wp.setNpwpd(result.getString("NPWPD"));
					wp.setIdTutup(null);
					return wp;
				}
				else
					return null;
			}
			catch(SQLException e)
			{
				MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database SQL Error", e.getMessage());
				return null;
			}
		}
		else{
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada load data Wajib Pajak Tutup.");
			return null;
		}
	}

	public List<PendaftaranWajibPajak> getDaftarTeguran(){
		String query = "select * from (SELECT MIN(wp.Nama_Badan) as Nama_Badan, MIN(wp.Alabad_Jalan) as Alamat, To_Char(max(sp.Masa_Pajak_Sampai), 'MM-YYYY') as SPTPD_Terakhir, " +
				"sp.NPWPD, MIN(wp.Tanggal_Daftar) as Tanggal_Daftar, min(kc.Name_Kecamatan) as Kecamatan, MIN(up.UPT) as UPT from sptpd sp left join wajib_pajak wp on wp.NPWPD = sp.NPWPD " +
				"left join kecamatan kc on wp.ALABAD_KECAMATAN = kc.Kode_Kecamatan left join UPT up on up.KODE_KECAMATAN = kc.KODE_KECAMATAN " +
				"where wp.Status = 'Aktif' and wp.Insidentil = 0 and wp.Jenis_Assesment = 'Self' and wp.Kewajiban_Pajak <> 8 and wp.Kewajiban_Pajak <> 6 Group by sp.NPWPD " +
				"having Datediff('month', max(sp.Masa_Pajak_Sampai), (curdate() - interval 2 month)) > 0 union " +
				"SELECT wp.Nama_Badan, wp.Alabad_Jalan as Alamat, '-' as SPTPD_Terakhir, wp.NPWPD, wp.Tanggal_Daftar, kc.Name_Kecamatan as Kecamatan, up.UPT " +
				"FROM wajib_pajak wp left join kecamatan kc on wp.ALABAD_KECAMATAN = kc.Kode_Kecamatan left join UPT up on up.KODE_KECAMATAN = kc.KODE_KECAMATAN " +
				"where NOT EXISTS (SELECT 1 from sptpd sp WHERE wp.NPWPD = sp.NPWPD) and wp.Status = 'Aktif' and wp.Insidentil = 0 and wp.Jenis_Assesment = 'Self' " +
				"and wp.Kewajiban_Pajak <> 8 and wp.Kewajiban_Pajak <> 6 and Datediff('month', wp.TANGGAL_DAFTAR, (curdate() - interval 2 month)) > 0) a " +
				"ORDER BY a.UPT, a.Kecamatan, cast(substring(a.NPWPD, 2, 7) as integer);";
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.openBawah());
		System.out.println(query);
		if(result != null){
			try{
				List<PendaftaranWajibPajak> listPendaftaranWajibPajak = new ArrayList<PendaftaranWajibPajak>();
				while(result.next()){
					PendaftaranWajibPajak wajibPajakModel = new PendaftaranWajibPajak();
					wajibPajakModel.setNamaBadan(result.getString("NAMA_BADAN"));
					wajibPajakModel.setAlabadJalan(result.getString("Alamat"));
					wajibPajakModel.setNamaPemilik(result.getString("SPTPD_Terakhir"));
					wajibPajakModel.setNPWP(result.getString("NPWPD"));
					wajibPajakModel.setTanggalDaftar(result.getDate("TANGGAL_DAFTAR"));
					if(result.getString("KECAMATAN") == null)
						wajibPajakModel.setAlabadKecamatan("");
					else
						wajibPajakModel.setAlabadKecamatan(result.getString("KECAMATAN"));
					wajibPajakModel.setIdSubPajak(result.getInt("UPT"));
					
					listPendaftaranWajibPajak.add(wajibPajakModel);
				}
				return listPendaftaranWajibPajak;
			} catch(SQLException e){
				MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database SQL Error", e.getMessage());
				return null;
			}
		}
		else{
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada pembuatan LOAD DATA tabel WAJIB_PAJAK.");
			return null;
		}
	}
	
	public List<PendaftaranWajibPajak> getDaftarTeguranbyKode(String pajak){
		String query = "select * from (SELECT MIN(wp.Nama_Badan) as Nama_Badan, MIN(wp.Alabad_Jalan) as Alamat, To_Char(max(sp.Masa_Pajak_Sampai), 'MM-YYYY') as SPTPD_Terakhir, " +
				"sp.NPWPD, MIN(wp.Tanggal_Daftar) as Tanggal_Daftar, min(kc.Name_Kecamatan) as Kecamatan, MIN(up.UPT) as UPT from sptpd sp left join wajib_pajak wp on wp.NPWPD = sp.NPWPD " +
				"left join kecamatan kc on wp.ALABAD_KECAMATAN = kc.Kode_Kecamatan left join UPT up on up.KODE_KECAMATAN = kc.KODE_KECAMATAN left join Pajak p on p.Kode_Pajak = wp.Kewajiban_Pajak " +
				"where wp.Status = 'Aktif' and wp.Insidentil = 0 and wp.Jenis_Assesment = 'Self' and wp.Kewajiban_Pajak <> 8 and wp.Kewajiban_Pajak <> 6 and p.Nama_Pajak like '%" + pajak + "%' Group by sp.NPWPD " +
				"having Datediff('month', max(sp.Masa_Pajak_Sampai), (curdate() - interval 2 month)) > 0 union " +
				"SELECT wp.Nama_Badan, wp.Alabad_Jalan as Alamat, '-' as SPTPD_Terakhir, wp.NPWPD, wp.Tanggal_Daftar, kc.Name_Kecamatan as Kecamatan, up.UPT " +
				"FROM wajib_pajak wp left join kecamatan kc on wp.ALABAD_KECAMATAN = kc.Kode_Kecamatan left join UPT up on up.KODE_KECAMATAN = kc.KODE_KECAMATAN " +
				"left join Pajak p on p.Kode_Pajak = wp.Kewajiban_Pajak " +
				"where NOT EXISTS (SELECT 1 from sptpd sp WHERE wp.NPWPD = sp.NPWPD) and wp.Status = 'Aktif' and wp.Insidentil = 0 and wp.Jenis_Assesment = 'Self' " +
				"and wp.Kewajiban_Pajak <> 8 and wp.Kewajiban_Pajak <> 6 and p.Nama_Pajak like '%" + pajak + "%' and Datediff('month', wp.TANGGAL_DAFTAR, (curdate() - interval 2 month)) > 0) a " +
				"ORDER BY a.UPT, a.Kecamatan, cast(substring(a.NPWPD, 2, 7) as integer);";
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.openBawah());
		if(result != null){
			try{
				List<PendaftaranWajibPajak> listPendaftaranWajibPajak = new ArrayList<PendaftaranWajibPajak>();
				while(result.next()){
					PendaftaranWajibPajak wajibPajakModel = new PendaftaranWajibPajak();
					wajibPajakModel.setNamaBadan(result.getString("NAMA_BADAN"));
					wajibPajakModel.setAlabadJalan(result.getString("Alamat"));
					wajibPajakModel.setNamaPemilik(result.getString("SPTPD_Terakhir"));
					wajibPajakModel.setNPWP(result.getString("NPWPD"));
					wajibPajakModel.setTanggalDaftar(result.getDate("TANGGAL_DAFTAR"));
					if(result.getString("KECAMATAN") == null)
						wajibPajakModel.setAlabadKecamatan("");
					else
						wajibPajakModel.setAlabadKecamatan(result.getString("KECAMATAN"));
					wajibPajakModel.setIdSubPajak(result.getInt("UPT"));
					
					listPendaftaranWajibPajak.add(wajibPajakModel);
				}
				return listPendaftaranWajibPajak;
			} catch(SQLException e){
				MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database SQL Error", e.getMessage());
				return null;
			}
		}
		else{
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada pembuatan LOAD DATA tabel WAJIB_PAJAK.");
			return null;
		}
	}
	
	public ResultSet cekTitikLokasi(String teks, String lokasi, String kecamatan, String kelurahan){
		String[] arrTeks = teks.split(" ");
		String[] arrLokasi = lokasi.split(" ");
		String query = "select wp.NPWPD, wp.Nama_Badan, wp.Alabad_Jalan, sp.Lokasi from SPTPD sp Inner join WAJIB_PAJAK wp on wp.NPWPD = sp.NPWPD where ";
		for (int i=0;i<arrTeks.length;i++){
			query += "UPPER(LOKASI) like '%" + arrTeks[i].toUpperCase() + "%' and ";
		}
		for (int i=0;i<arrLokasi.length;i++){
			query += "UPPER(LOKASI) like '%" + arrLokasi[i].toUpperCase() + "%' and ";
		}
		
		if (!kecamatan.equalsIgnoreCase(""))
			query += "UPPER(LOKASI) like '%" + kecamatan.toUpperCase() + "%' and ";
		
		if (!kelurahan.equalsIgnoreCase(""))
			query += "UPPER(LOKASI) like '%" + kelurahan.toUpperCase() + "%'";
		else
			query = query.substring(0, query.length() - 4);
		
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		
		return result;
	}
	
	public ResultSet getWPbyTglDaftar(String kodePajak, int tahun){
		String query = "select wp.npwpd, wp.Nama_Badan, wp.tanggal_daftar, max(wt.tgl_mulai) as mulai, max(wt.tgl_sampai) as sampai, max(sp.masa_pajak_sampai) as " +
				"sptpd from WAJIB_PAJAK wp left join WP_TUTUP wt on wt.NPWPD = wp.NPWPD left join SPTPD sp on sp.NPWPD = wp.NPWPD " +
				"where year(TANGGAL_DAFTAR) <= " + tahun + " and wp.Kewajiban_Pajak = '" + kodePajak + "' group by wp.NPWPD, wp.TANGGAL_DAFTAR";
		
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		
		return result;
	}
	
	public HashMap<Integer, List<Integer>> getDataWP(int idSub){
		Integer count = 0;
		HashMap<Integer, List<Integer>> hm = new HashMap<Integer, List<Integer>>();
//		List<String> retValue = new ArrayList<String>();
		String query = "select count(case Status when 'Aktif' then 1 else null end) as Aktif, " +
				"count(case Status when 'NonAktif' then 1 else null end) as nonAktif, " +
				"count(case Status when 'Tutup' then 1 else null end) as Tutup, " +
				"count(*) as Total from WAJIB_PAJAK where IDSUB_PAJAK = "+ idSub +" and Insidentil = 0;";
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		try {
			while (result.next()){
				List<Integer> list = new ArrayList<Integer>();
				list.add(result.getInt(1));
				list.add(result.getInt(2));
				list.add(result.getInt(3));
				list.add(result.getInt(4));
				hm.put(count, list);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return hm;
	}
	
	public HashMap<Integer, List<Integer>> getDataWPInsidentil(int idSub){
		Integer count = 0;
		HashMap<Integer, List<Integer>> hm = new HashMap<Integer, List<Integer>>();
//		List<String> retValue = new ArrayList<String>();
		String query = "select count(case Status when 'Aktif' then 1 else null end) as Aktif, " +
				"count(case Status when 'NonAktif' then 1 else null end) as nonAktif, " +
				"count(case Status when 'Tutup' then 1 else null end) as Tutup, " +
				"count(*) as Total from WAJIB_PAJAK where IDSUB_PAJAK = "+ idSub +" and Insidentil = 1;";
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		try {
			while (result.next()){
				List<Integer> list = new ArrayList<Integer>();
				list.add(result.getInt(1));
				list.add(result.getInt(2));
				list.add(result.getInt(3));
				list.add(result.getInt(4));
				hm.put(count, list);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return hm;
	}

	public ResultSet getMasaPajakSampaiLHP(String npwp) {
		// TODO Auto-generated method stub
		String query = "select to_char(masa_pajak_sampai, 'MM-YYYY') from (select top 1 masa_pajak_sampai from PERIKSA where NPWPD in '"+npwp+"' order by IDPERIKSA desc);";
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		return result;
	}
}