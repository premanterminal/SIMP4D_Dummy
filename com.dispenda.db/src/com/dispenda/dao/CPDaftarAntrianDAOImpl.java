package com.dispenda.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;

import com.dispenda.database.DBConnection;
import com.dispenda.database.DBOperation;
import com.dispenda.model.PendaftaranAntrian;

public class CPDaftarAntrianDAOImpl {
	private DBOperation db = new DBOperation();
	
	public boolean savePendaftaranAntrian(PendaftaranAntrian daftarAntriModel){
		String query = "MERGE INTO DAFTAR_ANTRIAN USING (VALUES(" +
				""+ daftarAntriModel.getIndex()+"," +
				"'"+ daftarAntriModel.getNoFormulir()+"'," +
				"'"+ daftarAntriModel.getNamaBadan()+"'," +
				"'"+ daftarAntriModel.getNamaPemilik()+"'," +
				"'"+ daftarAntriModel.getJenisAssesment()+"'," +
				"'"+ daftarAntriModel.getJenisPajak()+"'," +
				"'"+ daftarAntriModel.getTanggalDaftar()+"'," +
				""+ daftarAntriModel.getIdSubPajak()+"," +
				""+ daftarAntriModel.getKewajibanPajak()+"," +
				"'"+ daftarAntriModel.getAlatingJalan()+"'," +
				"'"+ daftarAntriModel.getAlatingKelurahan()+"'," +
				"'"+ daftarAntriModel.getAlatingKecamatan()+"'," +
				"'"+ daftarAntriModel.getAlatingTelepon()+"'," +
				"'"+ daftarAntriModel.getAlatingKodePos()+"'," +
				"'"+ daftarAntriModel.getAlabadJalan()+"'," +
				"'"+ daftarAntriModel.getAlabadKelurahan()+"'," +
				"'"+ daftarAntriModel.getAlabadKecamatan()+"'," +
				"'"+ daftarAntriModel.getAlabadTelepon()+"'," +
				"'"+ daftarAntriModel.getAlabadKodePos()+"'," +
				"'"+ daftarAntriModel.getKewarganegaraan()+"'," +
				"'"+ daftarAntriModel.getTandaBuktiDiri()+"'," +
				"'"+ daftarAntriModel.getNoBuktiDiri()+"'," +
				"'"+ daftarAntriModel.getJabatan()+"'," +
				"'"+ daftarAntriModel.getAlatingKota()+"'," +
				"'"+ daftarAntriModel.getNoKartuKeluarga()+"'," +
				"'"+ daftarAntriModel.getKeterangan()+"'," +
				""+ daftarAntriModel.getIsidentil()+"," +
				""+ daftarAntriModel.getSuratIzin()+"," +
				""+ daftarAntriModel.getNoSurat()+"," +
				""+ daftarAntriModel.getTglSurat()+"," +
				"'"+ daftarAntriModel.getKetReject()+"'," +
				"'"+ new Timestamp(System.currentTimeMillis())+"'," +
				"'"+ new Timestamp(System.currentTimeMillis())+"'," +
				""+daftarAntriModel.getTolak()+","+
				"'"+daftarAntriModel.getTanggalTolak()+"','',null"+
				"))" +
			" AS vals(" +
				"index," +
				"no_formulir," +
				"nama_badan," +
				"nama_pemilik," +
				"jns_assesment," +
				"jns_pajak," +
				"tgl_daftar," +
				"bid_usaha," +
				"wajib_pajak," +
				"alating_jalan," +
				"alating_kel," +
				"alating_kec," +
				"alating_telp," +
				"alating_kodepos," +
				"alabad_jalan," +
				"alabad_kel," +
				"alabad_kec," +
				"alabad_telp," +
				"alabad_kodepos," +
				"warneg," +
				"bukti_diri," +
				"no_bukti_diri," +
				"jabatan,"+
				"alating_kota,"+
				"no_KK," +
				"ket," +
				"isiden," +
				"surat_izin," +
				"no_surat," +
				"tgl_surat," +
				"approval," +
				"date_approval," +
				"date_input," +
				"tolak,"+
				"tgl_tolak," +
				"identitas," +
				"perizinan"+
				")" +
			" ON DAFTAR_ANTRIAN.IDANTRIAN = vals.index" +
			" WHEN MATCHED THEN UPDATE SET " +
				"DAFTAR_ANTRIAN.NO_FORMULIR= vals.no_formulir," +
				"DAFTAR_ANTRIAN.NAMA_BADAN = vals.nama_badan," +
				"DAFTAR_ANTRIAN.NAMA_PEMILIK = vals.nama_pemilik," +
				"DAFTAR_ANTRIAN.JENIS_ASSESMENT = vals.jns_assesment," +
				"DAFTAR_ANTRIAN.JENIS_PAJAK = vals.jns_pajak," +
				"DAFTAR_ANTRIAN.TANGGAL_DAFTAR = vals.tgl_daftar," +
				"DAFTAR_ANTRIAN.IDSUB_PAJAK = vals.bid_usaha," +
				"DAFTAR_ANTRIAN.KEWAJIBAN_PAJAK = vals.wajib_pajak," +
				"DAFTAR_ANTRIAN.ALATING_JALAN = vals.alating_jalan," +
				"DAFTAR_ANTRIAN.ALATING_KELURAHAN = vals.alating_kel," +
				"DAFTAR_ANTRIAN.ALATING_KECAMATAN = vals.alating_kec," +
				"DAFTAR_ANTRIAN.ALATING_TELEPON = vals.alating_telp," +
				"DAFTAR_ANTRIAN.ALATING_KODEPOS = vals.alating_kodepos," +
				"DAFTAR_ANTRIAN.ALABAD_JALAN = vals.alabad_jalan," +
				"DAFTAR_ANTRIAN.ALABAD_KELURAHAN = vals.alabad_kel," +
				"DAFTAR_ANTRIAN.ALABAD_KECAMATAN = vals.alabad_kec," +
				"DAFTAR_ANTRIAN.ALABAD_TELEPON = vals.alabad_telp," +
				"DAFTAR_ANTRIAN.ALABAD_KODEPOS = vals.alabad_kodepos," +
				"DAFTAR_ANTRIAN.KEWARGANEGARAAN = vals.warneg," +
				"DAFTAR_ANTRIAN.TANDA_BUKTI_DIRI = vals.bukti_diri," +
				"DAFTAR_ANTRIAN.NO_BUKTI_DIRI = vals.no_bukti_diri," +
				"DAFTAR_ANTRIAN.JABATAN = vals.jabatan," +
				"DAFTAR_ANTRIAN.ALATING_KOTA = vals.alating_kota," +
				"DAFTAR_ANTRIAN.NO_KARTU_KELUARGA = vals.no_KK," +
				"DAFTAR_ANTRIAN.KETERANGAN = vals.ket," +
				"DAFTAR_ANTRIAN.INSIDENTIL = vals.isiden,"+
				"DAFTAR_ANTRIAN.SURAT_IZIN = vals.surat_izin,"+
				"DAFTAR_ANTRIAN.NO_SURAT = vals.no_surat,"+
				"DAFTAR_ANTRIAN.TANGGAL_SURAT = vals.tgl_surat,"+
				"DAFTAR_ANTRIAN.KETERANGAN_REJECT = vals.approval,"+
				"DAFTAR_ANTRIAN.DATE_APPROVAL = vals.date_approval,"+
				"DAFTAR_ANTRIAN.DATE_INPUT = vals.date_input,"+
				"DAFTAR_ANTRIAN.TOLAK = vals.tolak,"+
				"DAFTAR_ANTRIAN.TANGGAL_TOLAK = vals.tgl_tolak," +
				"DAFTAR_ANTRIAN.FILE_IDENTITAS = vals.identitas," +
				"DAFTAR_ANTRIAN.FILE_PERIZINAN = vals.perizinan"+
			" WHEN NOT MATCHED THEN INSERT VALUES " +
				"vals.index," +
				"vals.no_formulir," +
				"vals.nama_badan," +
				"vals.nama_pemilik," +
				"vals.jns_assesment," +
				"vals.jns_pajak," +
				"vals.tgl_daftar," +
				"vals.bid_usaha," +
				"vals.wajib_pajak," +
				"vals.alating_jalan," +
				"vals.alating_kel," +
				"vals.alating_kec," +
				"vals.alating_telp," +
				"vals.alating_kodepos," +
				"vals.alabad_jalan," +
				"vals.alabad_kel," +
				"vals.alabad_kec," +
				"vals.alabad_telp,"+
				"vals.alabad_kodepos," +
				"vals.warneg," +
				"vals.bukti_diri," +
				"vals.no_bukti_diri," +
				"vals.jabatan," +
				"vals.alating_kota," +
				"vals.no_KK," +
				"vals.ket," +
				"vals.isiden," +
				"vals.surat_izin," +
				"vals.no_surat," +
				"vals.tgl_surat," +
				"vals.approval," +
				"vals.date_approval," +
				"vals.date_input," +
				"vals.tolak," +
				"vals.tgl_tolak," +
				"vals.identitas," +
				"vals.perizinan";
		boolean result = db.ExecuteStatementQuery(query,DBConnection.INSTANCE.open());
		if(!result)
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "There are some problems of MERGE(UPDATE/INSERT) on table ANTRI.");
		return result;
	}
	
	public List<PendaftaranAntrian> getDaftarAntri(int index){
		String query = "SELECT * FROM DAFTAR_ANTRIAN WHERE DAFTAR_ANTRIAN.IDANTRIAN = "+index+"";
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		if(result != null){
			try{
				List<PendaftaranAntrian> listPendaftaranAntrian = new ArrayList<PendaftaranAntrian>();
//				int index = 1;
				while(result.next()){
					PendaftaranAntrian daftarAntriModel = new PendaftaranAntrian();
					daftarAntriModel.setIndex(result.getInt("IDANTRIAN"));
					daftarAntriModel.setNoFormulir(result.getString("NO_FORMULIR"));
					daftarAntriModel.setNamaBadan(result.getString("NAMA_BADAN"));
					daftarAntriModel.setNamaPemilik(result.getString("NAMA_PEMILIK"));
					daftarAntriModel.setJabatan(result.getString("JABATAN"));
					daftarAntriModel.setJenisAssesment(result.getString("JENIS_ASSESMENT"));
					daftarAntriModel.setJenisPajak(result.getString("JENIS_PAJAK"));
					daftarAntriModel.setTanggalDaftar(result.getDate("TANGGAL_DAFTAR"));
					daftarAntriModel.setIdSubPajak(result.getInt("IDSUB_PAJAK"));
					daftarAntriModel.setKeterangan(result.getString("KETERANGAN"));
					daftarAntriModel.setKewajibanPajak(result.getInt("KEWAJIBAN_PAJAK"));
					daftarAntriModel.setAlatingJalan(result.getString("ALATING_JALAN"));
					daftarAntriModel.setAlatingKelurahan(result.getString("ALATING_KELURAHAN"));
					daftarAntriModel.setAlatingKecamatan(result.getString("ALATING_KECAMATAN"));
					daftarAntriModel.setAlatingTelepon(result.getString("ALATING_TELEPON"));
					daftarAntriModel.setAlatingKodepos(result.getString("ALATING_KODEPOS"));
					daftarAntriModel.setAlabadJalan(result.getString("ALABAD_JALAN"));
					daftarAntriModel.setAlabadKelurahan(result.getString("ALABAD_KELURAHAN"));
					daftarAntriModel.setAlabadKecamatan(result.getString("ALABAD_KECAMATAN"));
					daftarAntriModel.setAlabadTelepon(result.getString("ALABAD_TELEPON"));
					daftarAntriModel.setAlabadKodePos(result.getString("ALABAD_KODEPOS"));
					daftarAntriModel.setKewarganegaraan(result.getString("KEWARGANEGARAAN"));
					daftarAntriModel.setTandaBuktiDiri(result.getString("TANDA_BUKTI_DIRI"));
					daftarAntriModel.setNoBuktiDiri(result.getString("NO_BUKTI_DIRI"));
					daftarAntriModel.setNoKartuKeluraga(result.getString("NO_KARTU_KELUARGA"));
					daftarAntriModel.setInsidentil(result.getBoolean("INSIDENTIL"));
					daftarAntriModel.setInsidentil(result.getBoolean("INSIDENTIL_PEMERINTAHAN"));
					daftarAntriModel.setSuratIzin(result.getArray("SURAT_IZIN"));
					daftarAntriModel.setNoSurat(result.getArray("NO_SURAT"));
					daftarAntriModel.setTglSurat(result.getArray("TANGGAL_SURAT"));
					daftarAntriModel.setKetReject(result.getString("APPROVAL"));
					daftarAntriModel.setDateApproval(result.getDate("DATE_APPROVAL"));
					daftarAntriModel.setDateInput(result.getDate("DATE_INPUT"));
					listPendaftaranAntrian.add(daftarAntriModel);
//					index++;
				}
				return listPendaftaranAntrian;
			} catch(SQLException e) {
				MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database SQL Error", e.getMessage());
				return null;
			}
		}
		else {
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada pembuatan LOAD DATA tabel DAFTAR_ANTRIAN.");
			return null;
		}
	}
	
	public List<PendaftaranAntrian> getAllPendaftaranAntrian() {
		String query = "SELECT * FROM DAFTAR_ANTRIAN da LEFT JOIN BIDANG_USAHA bu ON bu.IDSUB_PAJAK = da.IDSUB_PAJAK WHERE da.TOLAK = FALSE";
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		if(result != null){
			try{
				List<PendaftaranAntrian> listPendaftaranAntrian = new ArrayList<PendaftaranAntrian>();
//				int index = 1;
				while(result.next()){
					PendaftaranAntrian daftarAntriModel = new PendaftaranAntrian();
					daftarAntriModel.setIndex(result.getInt("IDANTRIAN"));
					daftarAntriModel.setNoFormulir(result.getString("NO_FORMULIR"));
					daftarAntriModel.setNamaBadan(result.getString("NAMA_BADAN"));
					daftarAntriModel.setNamaPemilik(result.getString("NAMA_PEMILIK"));
					daftarAntriModel.setJabatan(result.getString("JABATAN"));
					daftarAntriModel.setJenisAssesment(result.getString("JENIS_ASSESMENT"));
					daftarAntriModel.setJenisPajak(result.getString("JENIS_PAJAK"));
					daftarAntriModel.setTanggalDaftar(result.getDate("TANGGAL_DAFTAR"));
					daftarAntriModel.setIdSubPajak(result.getInt("IDSUB_PAJAK"));
					daftarAntriModel.setKeterangan(result.getString("KETERANGAN"));
					daftarAntriModel.setKewajibanPajak(result.getInt("KEWAJIBAN_PAJAK"));
					daftarAntriModel.setAlatingJalan(result.getString("ALATING_JALAN"));
					daftarAntriModel.setAlatingKelurahan(result.getString("ALATING_KELURAHAN"));
					daftarAntriModel.setAlatingKecamatan(result.getString("ALATING_KECAMATAN"));
					daftarAntriModel.setAlatingTelepon(result.getString("ALATING_TELEPON"));
					daftarAntriModel.setAlatingKodepos(result.getString("ALATING_KODEPOS"));
					daftarAntriModel.setAlabadJalan(result.getString("ALABAD_JALAN"));
					daftarAntriModel.setAlabadKelurahan(result.getString("ALABAD_KELURAHAN"));
					daftarAntriModel.setAlabadKecamatan(result.getString("ALABAD_KECAMATAN"));
					daftarAntriModel.setAlabadTelepon(result.getString("ALABAD_TELEPON"));
					daftarAntriModel.setAlabadKodePos(result.getString("ALABAD_KODEPOS"));
					daftarAntriModel.setKewarganegaraan(result.getString("KEWARGANEGARAAN"));
					daftarAntriModel.setTandaBuktiDiri(result.getString("TANDA_BUKTI_DIRI"));
					daftarAntriModel.setNoBuktiDiri(result.getString("NO_BUKTI_DIRI"));
					daftarAntriModel.setNoKartuKeluraga(result.getString("NO_KARTU_KELUARGA"));
					daftarAntriModel.setInsidentil(result.getBoolean("INSIDENTIL"));
					daftarAntriModel.setInsidentil_Pemerintah(result.getBoolean("INSIDENTIL_PEMERINTAHAN"));
					daftarAntriModel.setSuratIzin(result.getArray("SURAT_IZIN"));
					daftarAntriModel.setNoSurat(result.getArray("NO_SURAT"));
					daftarAntriModel.setTglSurat(result.getArray("TANGGAL_SURAT"));
					daftarAntriModel.setKetReject(result.getString("KETERANGAN_REJECT"));
					daftarAntriModel.setDateApproval(result.getDate("DATE_APPROVAL"));
					daftarAntriModel.setDateInput(result.getDate("DATE_INPUT"));
					daftarAntriModel.setFile_identitas(result.getString("File_Identitas"));
					daftarAntriModel.setFile_izin(result.getArray("FILE_PERIZINAN"));
					daftarAntriModel.setNamaBid_Usaha(result.getString("Nama_Bid_Usaha"));
					listPendaftaranAntrian.add(daftarAntriModel);
//					index++;
				}
				return listPendaftaranAntrian;
			} catch(SQLException e){
				MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database SQL Error", e.getMessage());
				return null;
			}
		}
		else{
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database error", "Ada masalah pada pembuatan LOAD DATA tabel DAFTAR_ANTRIAN");
			return null;
		}
	}
	
	public List<PendaftaranAntrian> getPendaftaranAntrianTolak() {
		String query = "SELECT * FROM DAFTAR_ANTRIAN WHERE TOLAK = TRUE";
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		if(result != null){
			try{
				List<PendaftaranAntrian> listPendaftaranAntrian = new ArrayList<PendaftaranAntrian>();
//				int index = 1;
				while(result.next()){
					PendaftaranAntrian daftarAntriModel = new PendaftaranAntrian();
					daftarAntriModel.setIndex(result.getInt("IDANTRIAN"));
					daftarAntriModel.setNoFormulir(result.getString("NO_FORMULIR"));
					daftarAntriModel.setNamaBadan(result.getString("NAMA_BADAN"));
					daftarAntriModel.setNamaPemilik(result.getString("NAMA_PEMILIK"));
					daftarAntriModel.setJabatan(result.getString("JABATAN"));
					daftarAntriModel.setJenisAssesment(result.getString("JENIS_ASSESMENT"));
					daftarAntriModel.setJenisPajak(result.getString("JENIS_PAJAK"));
					daftarAntriModel.setTanggalDaftar(result.getDate("TANGGAL_DAFTAR"));
					daftarAntriModel.setIdSubPajak(result.getInt("IDSUB_PAJAK"));
					daftarAntriModel.setKeterangan(result.getString("KETERANGAN"));
					daftarAntriModel.setKewajibanPajak(result.getInt("KEWAJIBAN_PAJAK"));
					daftarAntriModel.setAlatingJalan(result.getString("ALATING_JALAN"));
					daftarAntriModel.setAlatingKelurahan(result.getString("ALATING_KELURAHAN"));
					daftarAntriModel.setAlatingKecamatan(result.getString("ALATING_KECAMATAN"));
					daftarAntriModel.setAlatingTelepon(result.getString("ALATING_TELEPON"));
					daftarAntriModel.setAlatingKodepos(result.getString("ALATING_KODEPOS"));
					daftarAntriModel.setAlabadJalan(result.getString("ALABAD_JALAN"));
					daftarAntriModel.setAlabadKelurahan(result.getString("ALABAD_KELURAHAN"));
					daftarAntriModel.setAlabadKecamatan(result.getString("ALABAD_KECAMATAN"));
					daftarAntriModel.setAlabadTelepon(result.getString("ALABAD_TELEPON"));
					daftarAntriModel.setAlabadKodePos(result.getString("ALABAD_KODEPOS"));
					daftarAntriModel.setKewarganegaraan(result.getString("KEWARGANEGARAAN"));
					daftarAntriModel.setTandaBuktiDiri(result.getString("TANDA_BUKTI_DIRI"));
					daftarAntriModel.setNoBuktiDiri(result.getString("NO_BUKTI_DIRI"));
					daftarAntriModel.setNoKartuKeluraga(result.getString("NO_KARTU_KELUARGA"));
					daftarAntriModel.setInsidentil(result.getBoolean("INSIDENTIL"));
					daftarAntriModel.setInsidentil_Pemerintah(result.getBoolean("INSIDENTIL_PEMERINTAHAN"));
					daftarAntriModel.setSuratIzin(result.getArray("SURAT_IZIN"));
					daftarAntriModel.setNoSurat(result.getArray("NO_SURAT"));
					daftarAntriModel.setTglSurat(result.getArray("TANGGAL_SURAT"));
					daftarAntriModel.setKetReject(result.getString("KETERANGAN_REJECT"));
					daftarAntriModel.setDateApproval(result.getDate("DATE_APPROVAL"));
					daftarAntriModel.setDateInput(result.getDate("DATE_INPUT"));
					daftarAntriModel.setTanggalTolak(result.getDate("TANGGAL_TOLAK"));
					listPendaftaranAntrian.add(daftarAntriModel);
//					index++;
				}
				return listPendaftaranAntrian;
			} catch(SQLException e){
				MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database SQL Error", e.getMessage());
				return null;
			}
		}
		else{
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database error", "Ada masalah pada pembuatan LOAD DATA tabel DAFTAR_ANTRIAN");
			return null;
		}
	}
	
	public void deleteAntri(Integer index){
		String query = "DELETE FROM DAFTAR_ANTRIAN WHERE IDANTRIAN ="+index;
		boolean result = db.ExecuteStatementQuery(query, DBConnection.INSTANCE.open());
		if(!result)
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada DELETE tabel DAFTAR_ANTRIAN dengan IDANTRIAN = "+index);
	}
	
	public Integer getLastIndex(){
		String query = "SELECT TOP 1 IDANTRIAN FROM DAFTAR_ANTRIAN ORDER BY IDANTRIAN DESC";
		try{
			ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
			if(result.next()){
				return result.getInt("IDANTRIAN");
			}
			else
				return 0;
		} catch(SQLException e){
			e.printStackTrace();
			return 0;
		}
	}
}
