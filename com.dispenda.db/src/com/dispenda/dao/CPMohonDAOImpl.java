package com.dispenda.dao;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;

import com.dispenda.model.Mohon;
import com.dispenda.database.DBConnection;
import com.dispenda.database.DBOperation;

public class CPMohonDAOImpl {
	private DBOperation db = new DBOperation();
	
	public boolean saveMohon(Mohon mohonModel){
		String query = "MERGE INTO MOHON USING (VALUES(" +
				""+mohonModel.getIdMohon()+"," +
				"'"+mohonModel.getNoMohon()+"'," +
				"'"+mohonModel.getTglMohon()+"'," +
				""+mohonModel.getJenisMohon()+"," +
				"'"+mohonModel.getAlasanMohon()+"'," +
				"'"+mohonModel.getTglJatuhTempo()+"'," +
				"'"+mohonModel.getNpwpd()+"'," +
				"'"+mohonModel.getNoSkp()+"'," +
				"'"+mohonModel.getStatusMohon()+"'," +
				"'"+mohonModel.getNamaPemohon()+"'," +
				"'"+mohonModel.getJabatanPemohon()+"'," +
				"'"+mohonModel.getAlamatPemohon()+"'))" +
//				""+mohonModel.getPokokPajak()+"," +
//				""+mohonModel.getDenda()+"," +
//				""+mohonModel.getDendaSptpd()+"," +
//				""+mohonModel.getDendaSkpdkb()+"," +
//				""+mohonModel.getTotalPajakTerhutang()+"))" +
			" AS vals(" +
				"id," +
				"no," +
				"tgl," +
				"jenis," +
				"alasan," +
				"tgljatuhtempo," +
				"npwpd," +
				"no_skp," +
				"status," +
				"nama," +
				"jabatan," +
				"alamat)" +
//				"pokokPajak," +
//				"denda," +
//				"denda_sptpd," +
//				"denda_skpdkb," +
//				"tot_pajak_terhutang))" +				
			" ON MOHON.IDMOHON = vals.id" +
			" WHEN MATCHED THEN UPDATE SET " +
				"MOHON.IDMOHON = vals.id," +
				"MOHON.NO_MOHON = vals.no," +
				"MOHON.TANGGAL_MOHON = vals.tgl," +
				"MOHON.JENIS_MOHON = vals.jenis," +
				"MOHON.ALASAN_MOHON = vals.alasan," +
				"MOHON.TANGGAL_JATUH_TEMPO = vals.tgljatuhtempo," +
				"MOHON.NPWPD = vals.npwpd," +
				"MOHON.NO_SKP = vals.no_skp," +
				"MOHON.STATUS_MOHON = vals.status," +
				"MOHON.NAMA_PEMOHON = vals.nama," +
				"MOHON.JABATAN_PEMOHON = vals.jabatan," +
				"MOHON.ALAMAT_PEMOHON = vals.alamat " +
//				"MOHON.POKOKPAJAK = vals.pokokPajak," +
//				"MOHON.DENDA = vals.denda," +
//				"MOHON.DENDA_SPTPD = vals.denda_sptpd," +
//				"MOHON.DENDA_SKPDKB = vals.denda_skpdkb" +
//				"MOHON.TOTAL_PAJAK_TERHUTANG = vals.tot_pajak_terhutang" +
			" WHEN NOT MATCHED THEN INSERT VALUES " +
				"vals.id," +
				"vals.no," +
				"vals.tgl," +
				"vals.jenis," +
				"vals.alasan," +
				"vals.tgljatuhtempo," +
				"vals.npwpd," +
				"vals.no_skp," +
				"vals.status," +
				"vals.nama," +
				"vals.jabatan," +
				"vals.alamat";
//				"vals.pokokPajak," +
//				"vals.denda," +
//				"vals.denda_sptpd," +
//				"vals.denda_skpdkb," +
//				"vals.tot_pajak_terhutang" ;
		boolean result = db.ExecuteStatementQuery(query, DBConnection.INSTANCE.open());
		if(!result)
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada MERGE(UPDATE/INSERT) tabel MOHON.");
		return result;
	}
	
	public List<Mohon> getMohon(String npwpd){
		String query = "SELECT * FROM MOHON WHERE NPWPD = "+npwpd;
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		if(result != null){
			try{
				List<Mohon> listMohon = new ArrayList<Mohon>();
				while(result.next()){
					Mohon mohonModel = new Mohon();
					mohonModel.setIdMohon(result.getInt("IDMOHON"));
					mohonModel.setNoMohon(result.getString("NO_MOHON"));
					mohonModel.setTglMohon(result.getDate("TANGGAL_MOHON"));
					mohonModel.setJenisMohon(result.getInt("JENIS_MOHON"));
					mohonModel.setAlasanMohon(result.getString("ALASAN_MOHON"));
					mohonModel.setTglJatuhTempo(result.getDate("TANGGAL_JATUH_TEMPO"));
					mohonModel.setNpwpd(result.getString("NPWPD"));
					mohonModel.setNoSkp(result.getString("NO_SKP"));
					mohonModel.setStatusMohon(result.getInt("STATUS_MOHON"));
					mohonModel.setNamaPemohon(result.getString("NAMA_PEMOHON"));
					mohonModel.setJabatanPemohon(result.getString("JABATAN_PEMOHON"));
					mohonModel.setAlamatPemohon(result.getString("ALAMAT_PEMOHON"));
					mohonModel.setPokokPajak(result.getDouble("POKOKPAJAK"));
					mohonModel.setDenda(result.getDouble("DENDA"));
					mohonModel.setDendaSptpd(result.getDouble("DENDA_SPTPD"));
					mohonModel.setDendaSkpdkb(result.getDouble("DENDA_SKPDKB"));
					mohonModel.setTotalPajakTerhutang(result.getDouble("TOTAL_PAJAK_TERHUTANG"));
					listMohon.add(mohonModel);
				}
				return listMohon;
			}
			catch(SQLException e){
				MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", e.getMessage());
				return null;
			}
		}
		else{
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada pembuatan LOAD DATA tabel MOHON.");
			return null;
		}
	}
	
	public List<Mohon> getAllMohon(){
		String query = "SELECT * FROM MOHON";
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		if(result != null){
			try{
				List<Mohon> listMohon = new ArrayList<Mohon>();
				while(result.next()){
					Mohon mohonModel = new Mohon();
					mohonModel.setIdMohon(result.getInt("IDMOHON"));
					mohonModel.setNoMohon(result.getString("NO_MOHON"));
					mohonModel.setTglMohon(result.getDate("TANGGAL_MOHON"));
					mohonModel.setJenisMohon(result.getInt("JENIS_MOHON"));
					mohonModel.setAlasanMohon(result.getString("ALASAN_MOHON"));
					mohonModel.setTglJatuhTempo(result.getDate("TANGGAL_JATUH_TEMPO"));
					mohonModel.setNpwpd(result.getString("NPWPD"));
					mohonModel.setNoSkp(result.getString("NO_SKP"));
					mohonModel.setStatusMohon(result.getInt("STATUS_MOHON"));
					mohonModel.setNamaPemohon(result.getString("NAMA_PEMOHON"));
					mohonModel.setJabatanPemohon(result.getString("JABATAN_PEMOHON"));
					mohonModel.setAlamatPemohon(result.getString("ALAMAT_PEMOHON"));
					mohonModel.setPokokPajak(result.getDouble("POKOKPAJAK"));
					mohonModel.setDenda(result.getDouble("DENDA"));
					mohonModel.setDendaSptpd(result.getDouble("DENDA_SPTPD"));
					mohonModel.setDendaSkpdkb(result.getDouble("DENDA_SKPDKB"));
					mohonModel.setTotalPajakTerhutang(result.getDouble("TOTAL_PAJAK_TERHUTANG"));
					listMohon.add(mohonModel);
				}
				return listMohon;
			}
			catch(SQLException e){
				MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", e.getMessage());
				return null;
			}
		}
		else{
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada pembuatan LOAD DATA tabel MOHON.");
			return null;
		}
	}
	
	public boolean deleteMohon(Integer idMohon){
		String query = "DELETE FROM MOHON WHERE IDMOHON = "+idMohon;
		boolean result = db.ExecuteStatementQuery(query, DBConnection.INSTANCE.open());
		if(!result)
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada DELETE tabel MOHON dengan IDMOHON = "+idMohon);
		
		return result;
	}
	
	public Integer getLastIdMohon(String npwpd){
		String query = "SELECT TOP 1 IDMOHON FROM MOHON WHERE NPWPD = '" + npwpd + "' ORDER BY IDMOHON DESC";
		try{
			ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
			if(result.next()){
				return result.getInt("IDMOHON");
			}
			else
				return 0;
		}
		catch(SQLException e){
			e.printStackTrace();
			return 0;
		}
	}
	
	public Integer checkMohon(String NPWPD, String noSKP)
	{
		String query = "SELECT IdMohon FROM mohon WHERE NPWPD = '" + NPWPD + "' AND No_SKP = '" + noSKP + "' and STATUS_MOHON <> 2";
		try{
			ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
			if(result.next()){
				return result.getInt("IdMohon");
			}
			else
				return null;
		}
		catch(SQLException e){
			e.printStackTrace();
			return null;
		}
	}
	
	public Mohon getMohon(int idMohon)
	{
		String query = "SELECT IDMOHON, No_Mohon, Tanggal_Mohon, Jenis_Mohon, Tanggal_Jatuh_Tempo, Alasan_Mohon, NPWPD, No_SKP, Status_Mohon, Nama_Pemohon, Jabatan_Pemohon, " +
					"Alamat_Pemohon FROM mohon WHERE IdMohon = " + idMohon + ";";
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		if(result != null){
			try{
				Mohon mohonModel = new Mohon();
				while(result.next()){
					mohonModel.setIdMohon(result.getInt("IDMOHON"));
					mohonModel.setNoMohon(result.getString("NO_MOHON"));
					mohonModel.setTglMohon(result.getDate("TANGGAL_MOHON"));
					mohonModel.setJenisMohon(result.getInt("JENIS_MOHON"));
					mohonModel.setAlasanMohon(result.getString("ALASAN_MOHON"));
					mohonModel.setTglJatuhTempo(result.getDate("TANGGAL_JATUH_TEMPO"));
					mohonModel.setNpwpd(result.getString("NPWPD"));
					mohonModel.setNoSkp(result.getString("NO_SKP"));
					mohonModel.setStatusMohon(result.getInt("STATUS_MOHON"));
					mohonModel.setNamaPemohon(result.getString("NAMA_PEMOHON"));
					mohonModel.setJabatanPemohon(result.getString("JABATAN_PEMOHON"));
					mohonModel.setAlamatPemohon(result.getString("ALAMAT_PEMOHON"));
//					mohonModel.setPokokPajak(result.getDouble("POKOKPAJAK"));
//					mohonModel.setDenda(result.getDouble("DENDA"));
//					mohonModel.setDendaSptpd(result.getDouble("DENDA_SPTPD"));
//					mohonModel.setDendaSkpdkb(result.getDouble("DENDA_SKPDKB"));
//					mohonModel.setTotalPajakTerhutang(result.getDouble("TOTAL_PAJAK_TERHUTANG"));
				}
				return mohonModel;
			}
			catch(SQLException e){
				MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", e.getMessage());
				return null;
			}
		}
		else{
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada pembuatan LOAD DATA tabel MOHON.");
			return null;
		}
	}
	
	public List<Mohon> getMohonList(Date tanggal, Date tanggalSampai)
	{
		String query = "SELECT m.IdMohon, MAX(m.No_Mohon) as NO_MOHON, MAX(m.Tanggal_Mohon) as Tanggal_Mohon, MAX(m.Jenis_Mohon) as JENIS_MOHON, " +
				"MAX(m.Tanggal_Jatuh_Tempo) as Tanggal_Jatuh_Tempo, MAX(m.Alasan_Mohon) as Alasan_Mohon, MAX(m.NPWPD) as NPWPD, MAX(wp.Nama_Badan) as Nama_Badan, " +
				"MAX(wp.Alabad_Jalan) as Alabad_Jalan, MAX(m.No_SKP) as No_SKP, MAX(m.Status_Mohon) as Status_Mohon, " +
				"MAX(p.Total_Pajak_Periksa - p.Total_Pajak_Terutang + p.Total_Pajak_Bunga + p.Total_Kenaikan + p.Total_Denda) as Total, MAX(md.NO_ANGSURAN) as No_Angsuran, " +
				"MAX(md.Angsuran_Pokok + md.Biaya_Administrasi + md.Denda_SKPDKB) as Angsuran FROM mohon m INNER JOIN WAJIB_PAJAK wp on wp.NPWPD = m.NPWPD " +
				"INNER JOIN PERIKSA p on p.NPWPD = m.NPWPD and p.NO_SKP = m.NO_SKP INNER JOIN MOHON_DETAIL md on md.IDMOHON = m.IDMOHON " +
				"WHERE m.Tanggal_Mohon between Date'" + tanggal + "' and Date'" + tanggalSampai + "' group by IDMOHON order by MAX(m.Tanggal_Mohon);";
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		if(result != null){
			try{
				List<Mohon> listMohon = new ArrayList<Mohon>();
				while(result.next()){
					Mohon mohonModel = new Mohon();
					mohonModel.setIdMohon(result.getInt("idMohon"));
					mohonModel.setNoMohon(result.getString("NO_MOHON"));
					mohonModel.setTglMohon(result.getDate("TANGGAL_MOHON"));
					mohonModel.setJenisMohon(result.getInt("JENIS_MOHON"));
					mohonModel.setAlasanMohon(result.getString("ALASAN_MOHON"));
					mohonModel.setTglJatuhTempo(result.getDate("TANGGAL_JATUH_TEMPO"));
					mohonModel.setNpwpd(result.getString("NPWPD"));
					mohonModel.setNamaBadan(result.getString("Nama_Badan"));
					mohonModel.setAlamat(result.getString("Alabad_Jalan"));
					mohonModel.setNoSkp(result.getString("NO_SKP"));
					mohonModel.setStatusMohon(result.getInt("STATUS_MOHON"));
					mohonModel.setTotalPajakTerhutang(result.getDouble("Total"));
					mohonModel.setJumlahAngsuran(result.getInt("No_Angsuran"));
					mohonModel.setAngsuran(result.getDouble("Angsuran"));
					listMohon.add(mohonModel);
				}
				return listMohon;
			}
			catch(SQLException e){
				MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", e.getMessage());
				return null;
			}
		}
		else{
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada pembuatan LOAD DATA tabel MOHON.");
			return null;
		}
	}
	
	public boolean editStatusMohon(int idMohon, int status)
	{
		String query = "Update Mohon set Status_Mohon = '" + status + "' where idMohon = " + idMohon;
		boolean result = db.ExecuteStatementQuery(query, DBConnection.INSTANCE.open());
		if(!result)
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada DELETE tabel MOHON dengan IDMOHON = "+idMohon);
		
		return result;
	}
	
	public ResultSet GetDaftarAngsuran(Date masaDari,Date masaSampai){
		String sqlString = "SELECT wp.NPWPD, wp.Nama_Badan, wp.Alabad_Jalan, p.No_SKP, CONCAT(To_Char(p.Masa_Pajak_Dari, 'MMMM YYYY'), '-', " +
							"To_Char(p.Masa_Pajak_Sampai, 'MMMM YYYY')) as MasaPajak, md.No_Angsuran as AngsuranKe, md.Tanggal_Angsuran as JatuhTempo, md.Angsuran_Pokok, " +
							"md.Biaya_Administrasi, md.Denda_SKPDKB, 0 as DendaKeterlambatan, (md.Angsuran_Pokok+md.Biaya_Administrasi+md.Denda_SKPDKB) as Jumlah " +
							"FROM mohon_detail md INNER JOIN mohon m on md.IdMohon = m.IdMohon INNER JOIN wajib_pajak wp on wp.NPWPD = m.NPWPD " +
							"INNER JOIN periksa p on p.No_SKP = m.No_SKP AND m.NPWPD = p.NPWPD " +
							"WHERE NOT EXISTS (SELECT 1 from sspd ss where p.IDPERIKSA = ss.IDPERIKSA AND ss.Cicilan_Ke = md.No_Angsuran) " +
							"AND md.Tanggal_Angsuran BETWEEN '" + masaDari + "' AND '" + masaSampai + "' ORDER BY m.NPWPD, m.Tanggal_Mohon, m.No_SKP, md.Tanggal_Angsuran, md.No_Angsuran";
		ResultSet result = db.ResultExecutedStatementQuery(sqlString, DBConnection.INSTANCE.open());
		return result;
	}
	
	public ResultSet GetDaftarAngsuranByKode(Date masaDari,Date masaSampai, String kodePajak){
		String sqlString = "SELECT wp.NPWPD, wp.Nama_Badan, wp.Alabad_Jalan, p.No_SKP, CONCAT(To_Char(p.Masa_Pajak_Dari, 'MMMM YYYY'), '-', " +
							"To_Char(p.Masa_Pajak_Sampai, 'MMMM YYYY')) as MasaPajak, md.No_Angsuran as AngsuranKe, md.Tanggal_Angsuran as JatuhTempo, md.Angsuran_Pokok, " +
							"md.Biaya_Administrasi, md.Denda_SKPDKB, 0 as DendaKeterlambatan, (md.Angsuran_Pokok+md.Biaya_Administrasi+md.Denda_SKPDKB) as Jumlah " +
							"FROM mohon_detail md INNER JOIN mohon m on md.IdMohon = m.IdMohon INNER JOIN wajib_pajak wp on wp.NPWPD = m.NPWPD " +
							"INNER JOIN periksa p on p.No_SKP = m.No_SKP AND m.NPWPD = p.NPWPD " +
							"WHERE NOT EXISTS (SELECT 1 from sspd ss where p.IDPERIKSA = ss.IDPERIKSA AND ss.Cicilan_Ke = md.No_Angsuran) " +
							"AND (md.Tanggal_Angsuran BETWEEN '" + masaDari + "' AND '" + masaSampai + "') and wp.Kewajiban_Pajak = '" + kodePajak + "' ORDER BY m.NPWPD, m.Tanggal_Mohon, m.No_SKP, md.Tanggal_Angsuran, md.No_Angsuran";
		ResultSet result = db.ResultExecutedStatementQuery(sqlString, DBConnection.INSTANCE.open());
		return result;
	}
	
	public ResultSet GetDaftarAngsuranLunas(Date masaDari,Date masaSampai){
		String sqlString = "SELECT wp.NPWPD, wp.Nama_Badan, wp.Alabad_Jalan, p.No_SKP, CONCAT(To_Char(p.Masa_Pajak_Dari, 'MMMM YYYY'), '-', " +
							"To_Char(p.Masa_Pajak_Sampai, 'MMMM YYYY')) as MasaPajak, md.No_Angsuran as AngsuranKe, md.Tanggal_Angsuran as JatuhTempo, md.Angsuran_Pokok, " +
							"md.Biaya_Administrasi, md.Denda_SKPDKB, 0 as DendaKeterlambatan, (md.Angsuran_Pokok+md.Biaya_Administrasi+md.Denda_SKPDKB) as Jumlah, " +
							"s.Jumlah_Bayar, s.Denda as DendaSSPD, s.Tgl_SSPD, s.No_SSPD " + 
							"FROM mohon_detail md INNER JOIN mohon m on md.IdMohon = m.IdMohon INNER JOIN wajib_pajak wp on wp.NPWPD = m.NPWPD " +
							"INNER JOIN periksa p on p.No_SKP = m.No_SKP AND m.NPWPD = p.NPWPD INNER JOIN sspd s on p.IDPERIKSA = s.IDPERIKSA AND s.Cicilan_Ke = md.No_Angsuran " +
							"WHERE md.Tanggal_Angsuran BETWEEN '" + masaDari + "' AND '" + masaSampai + "' ORDER BY m.NPWPD, m.Tanggal_Mohon, m.No_SKP, md.Tanggal_Angsuran, md.No_Angsuran";
		ResultSet result = db.ResultExecutedStatementQuery(sqlString, DBConnection.INSTANCE.open());
		return result;
	}
	
	public ResultSet GetDaftarAngsuranLunasByKode(Date masaDari,Date masaSampai, String kodePajak){
		String sqlString = "SELECT wp.NPWPD, wp.Nama_Badan, wp.Alabad_Jalan, p.No_SKP, CONCAT(To_Char(p.Masa_Pajak_Dari, 'MMMM YYYY'), '-', " +
							"To_Char(p.Masa_Pajak_Sampai, 'MMMM YYYY')) as MasaPajak, md.No_Angsuran as AngsuranKe, md.Tanggal_Angsuran as JatuhTempo, md.Angsuran_Pokok, " +
							"md.Biaya_Administrasi, md.Denda_SKPDKB, 0 as DendaKeterlambatan, (md.Angsuran_Pokok+md.Biaya_Administrasi+md.Denda_SKPDKB) as Jumlah, " +
							"s.Jumlah_Bayar, s.Denda as DendaSSPD, s.Tgl_SSPD, s.No_SSPD " + 
							"FROM mohon_detail md INNER JOIN mohon m on md.IdMohon = m.IdMohon INNER JOIN wajib_pajak wp on wp.NPWPD = m.NPWPD " +
							"INNER JOIN periksa p on p.No_SKP = m.No_SKP AND m.NPWPD = p.NPWPD INNER JOIN sspd s on p.IDPERIKSA = s.IDPERIKSA AND s.Cicilan_Ke = md.No_Angsuran " +
							"WHERE (md.Tanggal_Angsuran BETWEEN '" + masaDari + "' AND '" + masaSampai + "') and wp.Kewajiban_Pajak = '" + kodePajak + "' ORDER BY m.NPWPD, m.Tanggal_Mohon, m.No_SKP, md.Tanggal_Angsuran, md.No_Angsuran";
		ResultSet result = db.ResultExecutedStatementQuery(sqlString, DBConnection.INSTANCE.open());
		return result;
	}
	
}