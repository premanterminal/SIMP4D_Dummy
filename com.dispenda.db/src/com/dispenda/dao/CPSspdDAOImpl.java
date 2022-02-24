package com.dispenda.dao;

import java.math.RoundingMode;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;

import com.dispenda.model.LaporanRealisasi;
import com.dispenda.model.Periksa;
import com.dispenda.model.Sspd;
import com.dispenda.controller.ControllerFactory;
import com.dispenda.controller.UIController;
import com.dispenda.database.DBConnection;
import com.dispenda.database.DBOperation;

public class CPSspdDAOImpl {
	private DBOperation db = new DBOperation();

	public boolean saveSspd(Sspd sspdModel) {
		String query = "MERGE INTO SSPD USING (VALUES(" + ""
				+ sspdModel.getIdSspd()
				+ ","
				+ "'"
				+ sspdModel.getNoSspd()
				+ "',"
				+ "'"
				+ sspdModel.getTglSspd()
				+ "',"
				+ "'"
				+ sspdModel.getTglCetak()
				+ "',"
				+ "'"
				+ sspdModel.getNpwpd()
				+ "',"
				+ "'"
				+ sspdModel.getBulanSkp()
				+ "',"
				+ "'"
				+ sspdModel.getNoSkp()
				+ "',"
				+ ""
				+ sspdModel.getCicilanKe()
				+ ","
				+ ""
				+ sspdModel.getDenda()
				+ ","
				+ ""
				+ sspdModel.getJumlahBayar()
				+ ","
				+ ""
				+ sspdModel.getJenisBayar()
				+ ","
				+ "'"
				+ sspdModel.getCaraBayar()
				+ "',"
				+ "'"
				+ sspdModel.getNamaPenyetor()
				+ "',"
				+ "'"
				+ sspdModel.getLunas()
				+ "',"
				+ ""
				+ sspdModel.getIdSubPajak()
				+ ","
				+ ""
				+ sspdModel.getIdSPTPD()
				+ ","
				+ ""
				+ sspdModel.getIdPeriksa()
				+ ","
				+ "'"
				+ sspdModel.getNotaBayar()
				+ "',"
				+ "'"
				+ sspdModel.getDendaLunas()
				+ "'))"
				+ " AS vals("
				+ "id,"
				+ "no,"
				+ "tgl,"
				+ "tglCetak,"
				+ "npwpd,"
				+ "bln_skp,"
				+ "no_skp,"
				+ "cicilan_ke,"
				+ "denda,"
				+ "jlh_bayar,"
				+ "jns_bayar,"
				+ "cara_bayar,"
				+ "nama_penyetor,"
				+ "lunas,"
				+ "id_subpajak,"
				+ "idSptpd,"
				+ "idPeriksa,"
				+ "notabayar,"
				+ "denda_lunas)"
				+ " ON SSPD.idSspd = vals.id"
				+ " WHEN MATCHED THEN UPDATE SET "
				+
				// "SSPD.idSspd = vals.id," +
				"SSPD.TGL_SSPD = vals.tgl,"
				+ "SSPD.TGL_CETAK = vals.tglCetak,"
				+ "SSPD.NPWPD = vals.npwpd,"
				+ "SSPD.BULAN_SKP = vals.bln_skp,"
				+ "SSPD.NO_SKP = vals.no_skp,"
				+ "SSPD.CICILAN_KE = vals.cicilan_ke,"
				+ "SSPD.DENDA = vals.denda,"
				+ "SSPD.JUMLAH_BAYAR = vals.jlh_bayar,"
				+ "SSPD.JENIS_BAYAR = vals.jns_bayar,"
				+ "SSPD.CARA_BAYAR = vals.cara_bayar,"
				+ "SSPD.NAMA_PENYETOR = vals.nama_penyetor,"
				+ "SSPD.LUNAS = vals.lunas,"
				+ "SSPD.IDSUB_PAJAK = vals.id_subpajak,"
				+ "SSPD.IDSPTPD = vals.idSptpd,"
				+ "SSPD.IDPERIKSA = vals.idPeriksa,"
				+ "SSPD.NOTAANTRIBAYAR = vals.notabayar,"
				+ "SSPD.DENDA_LUNAS = vals.denda_lunas"
				+ " WHEN NOT MATCHED THEN INSERT (NO_SSPD, TGL_SSPD, NPWPD, BULAN_SKP, NO_SKP, CICILAN_KE, DENDA, JUMLAH_BAYAR, JENIS_BAYAR, "
				+ "CARA_BAYAR, NAMA_PENYETOR, LUNAS, IDSUB_PAJAK, DENDA_LUNAS, IDSPTPD, IDPERIKSA, TGL_CETAK, IDSSPD, NOTAANTRIBAYAR) VALUES "
				+ "vals.no," + "vals.tgl," + "vals.npwpd," + "vals.bln_skp,"
				+ "vals.no_skp," + "vals.cicilan_ke," + "vals.denda,"
				+ "vals.jlh_bayar," + "vals.jns_bayar," + "vals.cara_bayar,"
				+ "vals.nama_penyetor," + "vals.lunas," + "vals.id_subpajak,"
				+ "vals.denda_lunas," + "vals.idSptpd," + "vals.idPeriksa,"
				+ "vals.tglCetak," + "vals.id," + "vals.notabayar";
		boolean result = db.ExecuteStatementQuery(query,
				DBConnection.INSTANCE.open());
		if (!result)
			MessageDialog.openError(Display.getCurrent().getActiveShell(),
					"Database Error",
					"Ada masalah pada MERGE(UPDATE/INSERT) tabel SSPD.");
		return result;
	}

	public List<Sspd> getSspd(String npwpd) {
		String query = "SELECT * FROM SSPD S INNER JOIN N0_REGISTRASI R ON S.NO_SSPD = R.NO_SSPD WHERE S.NPWPD = '"
				+ npwpd + "'";
		ResultSet result = db.ResultExecutedStatementQuery(query,
				DBConnection.INSTANCE.open());
		if (result != null) {
			try {
				List<Sspd> listSspd = new ArrayList<Sspd>();
				while (result.next()) {
					Sspd sspdModel = new Sspd();
					sspdModel.setIdSspd(result.getInt("IDSSPD"));
					sspdModel.setNoSspd(result.getString("NO_SSPD"));
					sspdModel.setTglSspd(result.getTimestamp("TGL_SSPD"));
					sspdModel.setNpwpd(result.getString("NPWPD"));
					sspdModel.setBulanSkp(result.getString("BULAN_SKP"));
					sspdModel.setNoSkp(result.getString("NO_SKP"));
					sspdModel.setCicilanKe(result.getInt("CICILAN_KE"));
					sspdModel.setDenda(result.getDouble("DENDA"));
					sspdModel.setJumlahBayar(result.getDouble("JUMLAH_BAYAR"));
					sspdModel.setJenisBayar(result.getInt("JENIS_BAYAR"));
					sspdModel.setCaraBayar(result.getString("CARA_BAYAR"));
					sspdModel
							.setNamaPenyetor(result.getString("NAMA_PENYETOR"));
					sspdModel.setLunas(result.getBoolean("LUNAS"));
					sspdModel.setIdSubPajak(result.getInt("IDSUB_PAJAK"));
					sspdModel.setDendaLunas(result.getBoolean("DENDA_LUNAS"));
					sspdModel.setTglCetak(result.getTimestamp("TGL_CETAK"));
					sspdModel.setNoReg(result.getString("No_Registrasi"));
					listSspd.add(sspdModel);
				}
				return listSspd;
			} catch (SQLException e) {
				MessageDialog.openError(Display.getCurrent().getActiveShell(),
						"Database SQL Error", e.getMessage());
				return null;
			}
		} else {
			MessageDialog.openError(Display.getCurrent().getActiveShell(),
					"Database Error",
					"Ada masalah pada pembuatan LOAD DATA tabel SSPD.");
			return null;
		}
	}

	public List<Sspd> getAllSspd() {
		String query = "SELECT * FROM SSPD";
		ResultSet result = db.ResultExecutedStatementQuery(query,
				DBConnection.INSTANCE.open());
		if (result != null) {
			try {
				List<Sspd> listSspd = new ArrayList<Sspd>();
				while (result.next()) {
					Sspd sspdModel = new Sspd();
					sspdModel.setIdSspd(result.getInt("IDSSPD"));
					sspdModel.setNoSspd(result.getString("NO_SSPD"));
					sspdModel.setTglSspd(result.getTimestamp("TGL_SSPD"));
					sspdModel.setNpwpd(result.getString("NPWPD"));
					sspdModel.setBulanSkp(result.getString("BULAN_SKP"));
					sspdModel.setNoSkp(result.getString("NO_SKP"));
					sspdModel.setCicilanKe(result.getInt("CICILAN_KE"));
					sspdModel.setDenda(result.getDouble("DENDA"));
					sspdModel.setJumlahBayar(result.getDouble("JUMLAH_BAYAR"));
					sspdModel.setJenisBayar(result.getInt("JENIS_BAYAR"));
					sspdModel.setCaraBayar(result.getString("CARA_BAYAR"));
					sspdModel
							.setNamaPenyetor(result.getString("NAMA_PENYETOR"));
					sspdModel.setLunas(result.getBoolean("LUNAS"));
					sspdModel.setIdSubPajak(result.getInt("IDSUB_PAJAK"));
					sspdModel.setDendaLunas(result.getBoolean("DENDA_LUNAS"));
					sspdModel.setTglCetak(result.getTimestamp("TGL_CETAK"));
					listSspd.add(sspdModel);
				}
				return listSspd;
			} catch (SQLException e) {
				MessageDialog.openError(Display.getCurrent().getActiveShell(),
						"Database SQL Error", e.getMessage());
				return null;
			}
		} else {
			MessageDialog.openError(Display.getCurrent().getActiveShell(),
					"Database Error",
					"Ada masalah pada pembuatan LOAD DATA tabel SSPD.");
			return null;
		}
	}

	public List<Sspd> getNotaAntriSspd() {
		String query = "SELECT ss.*, wp.NAMA_BADAN, wp.NAMA_PEMILIK, wp.ALABAD_JALAN, "
				+ "ifnull(p.Masa_Pajak_Dari, sp.Masa_Pajak_Dari) as Masa_Pajak_Dari, ifnull(p.Masa_Pajak_Sampai, sp.Masa_Pajak_Sampai) as Masa_Pajak_Sampai "
				+ "FROM SSPD ss INNER JOIN WAJIB_PAJAK wp on wp.NPWPD = ss.NPWPD LEFT JOIN Periksa p on ss.IDPERIKSA = p.IDPERIKSA "
				+ "LEFT JOIN SPTPD sp on sp.IDSPTPD = ss.IDSPTPD WHERE ss.NOTAANTRIBAYAR IS NOT NULL AND (ss.NO_SSPD = '' OR ss.NO_SSPD IS NULL) "
				+ "order by cast(substring(ss.NOTAANTRIBAYAR, 1, length(ss.NOTAANTRIBAYAR) - 7) as int)";
		ResultSet result = db.ResultExecutedStatementQuery(query,
				DBConnection.INSTANCE.open());
		if (result != null) {
			try {
				List<Sspd> listSspd = new ArrayList<Sspd>();
				while (result.next()) {
					Sspd sspdModel = new Sspd();
					sspdModel.setIdSspd(result.getInt("IDSSPD"));
					sspdModel.setNoSspd(result.getString("NO_SSPD"));
					sspdModel.setTglSspd(result.getTimestamp("TGL_SSPD"));
					sspdModel.setNpwpd(result.getString("NPWPD"));
					sspdModel.setMasaPajakDari(result
							.getDate("Masa_Pajak_Dari"));
					sspdModel.setMasaPajakSampai(result
							.getDate("Masa_Pajak_Sampai"));
					sspdModel.setBulanSkp(result.getString("BULAN_SKP"));
					sspdModel.setNoSkp(result.getString("NO_SKP"));
					sspdModel.setCicilanKe(result.getInt("CICILAN_KE"));
					sspdModel.setDenda(result.getDouble("DENDA"));
					sspdModel.setJumlahBayar(result.getDouble("JUMLAH_BAYAR"));
					sspdModel.setJenisBayar(result.getInt("JENIS_BAYAR"));
					sspdModel.setCaraBayar(result.getString("CARA_BAYAR"));
					sspdModel
							.setNamaPenyetor(result.getString("NAMA_PENYETOR"));
					sspdModel.setLunas(result.getBoolean("LUNAS"));
					sspdModel.setIdSubPajak(result.getInt("IDSUB_PAJAK"));
					sspdModel.setDendaLunas(result.getBoolean("DENDA_LUNAS"));
					sspdModel.setTglCetak(result.getTimestamp("TGL_CETAK"));
					sspdModel.setNotaBayar(result.getString("NOTAANTRIBAYAR"));
					sspdModel.setNamaBadan(result.getString("NAMA_BADAN"));
					sspdModel.setNamaPemilik(result.getString("NAMA_PEMILIK"));
					sspdModel.setAlabadJalan(result.getString("ALABAD_JALAN"));
					listSspd.add(sspdModel);
				}
				return listSspd;
			} catch (SQLException e) {
				MessageDialog.openError(Display.getCurrent().getActiveShell(),
						"Database SQL Error", e.getMessage());
				return null;
			}
		} else {
			MessageDialog.openError(Display.getCurrent().getActiveShell(),
					"Database Error",
					"Ada masalah pada pembuatan LOAD DATA tabel SSPD.");
			return null;
		}
	}

	public List<Sspd> getDaftarTerimaSspd() {
		String query = "SELECT ss.*, rg.No_Registrasi, wp.NAMA_BADAN, wp.NAMA_PEMILIK, wp.ALABAD_JALAN, pj.Nama_Pajak, pj.Kode_Denda, bu.Kode_Bid_Usaha, "
				+ "ifnull(p.Masa_Pajak_Dari, sp.Masa_Pajak_Dari) as Masa_Pajak_Dari, ifnull(p.Masa_Pajak_Sampai, sp.Masa_Pajak_Sampai) as Masa_Pajak_Sampai "
				+ "FROM SSPD ss INNER JOIN WAJIB_PAJAK wp on wp.NPWPD = ss.NPWPD "
				+ "INNER JOIN PAJAK pj on pj.IDPajak = SUBSTRING(ss.NPWPD, 1, 1) "
				+ "inner join BIDANG_USAHA bu on bu.IDSUB_PAJAK = ss.IDSUB_PAJAK "
				+ "Left join PERIKSA p on ss.IDPERIKSA = p.IDPERIKSA "
				+ "Left Join SPTPD sp on sp.IDSPTPD = ss.IDSPTPD "
				+ "Left join NO_REGISTRASI rg on rg.NO_SSPD = ss.NO_SSPD "
				+ "WHERE ss.LUNAS is true and Cast(ss.TGL_CETAK as DATE) = curdate() order by rg.No_Registrasi,ss.TGL_SSPD DESC;";
		ResultSet result = db.ResultExecutedStatementQuery(query,
				DBConnection.INSTANCE.open());
		if (result != null) {
			try {
				List<Sspd> listSspd = new ArrayList<Sspd>();
				while (result.next()) {
					Sspd sspdModel = new Sspd();
					sspdModel.setIdSspd(result.getInt("IDSSPD"));
					sspdModel.setNotaBayar(result.getString("NOTAANTRIBAYAR"));
					sspdModel.setNoSspd(result.getString("NO_SSPD"));
					sspdModel.setTglSspd(result.getTimestamp("TGL_SSPD"));
					sspdModel.setNpwpd(result.getString("NPWPD"));
					sspdModel.setNoReg(result.getString("No_Registrasi"));
					sspdModel.setMasaPajakDari(result
							.getDate("Masa_Pajak_Dari"));
					sspdModel.setMasaPajakSampai(result
							.getDate("Masa_Pajak_Sampai"));
					sspdModel.setBulanSkp(result.getString("BULAN_SKP"));
					sspdModel.setNoSkp(result.getString("NO_SKP"));
					sspdModel.setCicilanKe(result.getInt("CICILAN_KE"));
					sspdModel.setDenda(result.getDouble("DENDA"));
					sspdModel.setJumlahBayar(result.getDouble("JUMLAH_BAYAR"));
					sspdModel.setJenisBayar(result.getInt("JENIS_BAYAR"));
					sspdModel.setCaraBayar(result.getString("CARA_BAYAR"));
					sspdModel
							.setNamaPenyetor(result.getString("NAMA_PENYETOR"));
					sspdModel.setLunas(result.getBoolean("LUNAS"));
					sspdModel.setIdSubPajak(result.getInt("IDSUB_PAJAK"));
					sspdModel.setDendaLunas(result.getBoolean("DENDA_LUNAS"));
					sspdModel.setTglCetak(result.getTimestamp("TGL_CETAK"));
					sspdModel.setNotaBayar(result.getString("NOTAANTRIBAYAR"));
					sspdModel.setNamaBadan(result.getString("NAMA_BADAN"));
					sspdModel.setNamaPemilik(result.getString("NAMA_PEMILIK"));
					sspdModel.setAlabadJalan(result.getString("ALABAD_JALAN"));
					sspdModel.setNamaPajak(result.getString("Nama_Pajak"));
					sspdModel.setKodeDenda(result.getString("Kode_Denda"));
					sspdModel.setKodeBidUsaha(result
							.getString("Kode_Bid_Usaha"));
					listSspd.add(sspdModel);
				}
				return listSspd;
			} catch (SQLException e) {
				MessageDialog.openError(Display.getCurrent().getActiveShell(),
						"Database SQL Error", e.getMessage());
				return null;
			}
		} else {
			MessageDialog.openError(Display.getCurrent().getActiveShell(),
					"Database Error",
					"Ada masalah pada pembuatan LOAD DATA tabel SSPD.");
			return null;
		}
	}

	public List<Sspd> getDaftarTerimaSspd(String searchParam) {
		String query = "SELECT ss.*, rg.No_Registrasi, wp.NAMA_BADAN, wp.NAMA_PEMILIK, wp.ALABAD_JALAN, pj.Nama_Pajak, pj.Kode_Denda, bu.Kode_Bid_Usaha, "
				+ "ifnull(p.Masa_Pajak_Dari, sp.Masa_Pajak_Dari) as Masa_Pajak_Dari, ifnull(p.Masa_Pajak_Sampai, sp.Masa_Pajak_Sampai) as Masa_Pajak_Sampai "
				+ "FROM SSPD ss INNER JOIN WAJIB_PAJAK wp on wp.NPWPD = ss.NPWPD "
				+ "INNER JOIN PAJAK pj on pj.IDPajak = SUBSTRING(ss.NPWPD, 1, 1) inner join BIDANG_USAHA bu on bu.IDSUB_PAJAK = ss.IDSUB_PAJAK "
				+ "LEFT JOIN Periksa p on ss.IDPERIKSA = p.IDPERIKSA "
				+ "Left Join SPTPD sp on sp.IDSPTPD = ss.IDSPTPD "
				+ "Left join NO_REGISTRASI rg on rg.NO_SSPD = ss.NO_SSPD WHERE ss.LUNAS is true "
				+ "and ((To_Char(ss.TGL_CETAK, 'YYYY-MM-DD') = '"
				+ searchParam
				+ "') or ss.No_SSPD = UPPER('"
				+ searchParam
				+ "') or "
				+ "ss.NPWPD like '%"
				+ searchParam
				+ "%') or LCASE(wp.NAMA_BADAN) like '%"
				+ searchParam.toLowerCase()
				+ "%' order by rg.No_Registrasi, CAST(SUBSTRING(No_SSPD ,1,length(no_sspd) - 10) AS int) DESC;";
		ResultSet result = db.ResultPreparedStatementQuery(query,
				DBConnection.INSTANCE.open());
		if (result != null) {
			try {
				List<Sspd> listSspd = new ArrayList<Sspd>();
				while (result.next()) {
					Sspd sspdModel = new Sspd();
					sspdModel.setIdSspd(result.getInt("IDSSPD"));
					sspdModel.setNoSspd(result.getString("NO_SSPD"));
					sspdModel.setTglSspd(result.getTimestamp("TGL_SSPD"));
					sspdModel.setNpwpd(result.getString("NPWPD"));
					sspdModel.setMasaPajakDari(result
							.getDate("Masa_Pajak_Dari"));
					sspdModel.setMasaPajakSampai(result
							.getDate("Masa_Pajak_Sampai"));
					sspdModel.setBulanSkp(result.getString("BULAN_SKP"));
					sspdModel.setNoSkp(result.getString("NO_SKP"));
					sspdModel.setCicilanKe(result.getInt("CICILAN_KE"));
					sspdModel.setDenda(result.getDouble("DENDA"));
					sspdModel.setJumlahBayar(result.getDouble("JUMLAH_BAYAR"));
					sspdModel.setJenisBayar(result.getInt("JENIS_BAYAR"));
					sspdModel.setCaraBayar(result.getString("CARA_BAYAR"));
					sspdModel
							.setNamaPenyetor(result.getString("NAMA_PENYETOR"));
					sspdModel.setLunas(result.getBoolean("LUNAS"));
					sspdModel.setIdSubPajak(result.getInt("IDSUB_PAJAK"));
					sspdModel.setDendaLunas(result.getBoolean("DENDA_LUNAS"));
					sspdModel.setTglCetak(result.getTimestamp("TGL_CETAK"));
					sspdModel.setNotaBayar(result.getString("NOTAANTRIBAYAR"));
					sspdModel.setNamaBadan(result.getString("NAMA_BADAN"));
					sspdModel.setNamaPemilik(result.getString("NAMA_PEMILIK"));
					sspdModel.setAlabadJalan(result.getString("ALABAD_JALAN"));
					sspdModel.setNamaPajak(result.getString("Nama_Pajak"));
					sspdModel.setKodeDenda(result.getString("Kode_Denda"));
					sspdModel.setKodeBidUsaha(result
							.getString("Kode_Bid_Usaha"));
					sspdModel.setNoReg(result.getString("No_Registrasi"));
					listSspd.add(sspdModel);
				}
				return listSspd;
			} catch (SQLException e) {
				MessageDialog.openError(Display.getCurrent().getActiveShell(),
						"Database SQL Error", e.getMessage());
				return null;
			}
		} else {
			MessageDialog.openError(Display.getCurrent().getActiveShell(),
					"Database Error",
					"Ada masalah pada pembuatan LOAD DATA tabel SSPD.");
			return null;
		}
	}

	public boolean deleteSspd(Integer id) {
		String query = "DELETE FROM SSPD WHERE IDSSPD = " + id;
		boolean result = db.ExecuteStatementQuery(query,
				DBConnection.INSTANCE.open());
		if (!result)
			MessageDialog.openError(Display.getCurrent().getActiveShell(),
					"Database Error", "Ada masalah pada DELETE tabel SSPD");

		return result;

	}

	public Integer getLastId(String npwpd) {
		String query = "SELECT TOP 1 IDSSPD FROM SSPD WHERE NPWPD = '" + npwpd
				+ "' ORDER BY IDSSPD DESC";
		try {
			ResultSet result = db.ResultExecutedStatementQuery(query,
					DBConnection.INSTANCE.open());
			if (result.next()) {
				return result.getInt("IDSSPD");
			} else
				return 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}

	public String getLastNoSspd() {
		String query = "SELECT TOP 1 NO_SSPD FROM SSPD ORDER BY NO_SSPD DESC";
		try {
			ResultSet result = db.ResultExecutedStatementQuery(query,
					DBConnection.INSTANCE.open());
			if (result.next()) {
				return result.getString("NO_SSPD");
			} else
				return "";
		} catch (SQLException e) {
			e.printStackTrace();
			return "";
		}
	}

	public String getLastNoSspd(int id) {
		String query = "SELECT NO_SSPD FROM SSPD WHERE IDSSPD = " + id;
		try {
			ResultSet result = db.ResultExecutedStatementQuery(query,
					DBConnection.INSTANCE.open());
			if (result.next()) {
				return result.getString("NO_SSPD");
			} else
				return "";
		} catch (SQLException e) {
			e.printStackTrace();
			return "";
		}
	}

	public Integer getJenisBayar(String NPWPD, String noSKP) {
		String query = "SELECT Jenis_Mohon FROM mohon WHERE Status_Mohon = 1 AND No_SKP = '"
				+ noSKP + "' AND NPWPD = '" + NPWPD + "'";
		try {
			ResultSet result = db.ResultExecutedStatementQuery(query,
					DBConnection.INSTANCE.open());
			if (result.next()) {
				if (result.getInt("Jenis_Mohon") == 2)
					return 2;
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
			// return 0;
		}
		return 0;

	}

	public Sspd getSSPD(String NPWPD, String noSKP) {
		String query = "";
		Sspd sspd = new Sspd();
		if (noSKP.contains("SPTPD")) {
			query = "select * from GETSSPDSPTPD WHERE No_SKP = '" + noSKP
					+ "' AND NPWPD = '" + NPWPD + "';";
		} else if (noSKP.contains("/SKPD/")) {
			query = "select * from GETSSPDSPTPD WHERE No_SKP = '" + noSKP
					+ "' AND NPWPD = '" + NPWPD + "';";
		} else // if (noSKP.contains("/SKPDKB"))
		{
			query = "SELECT SS.IDSSPD,SS.NPWPD,SS.NO_SKP,SS.CARA_BAYAR,SS.JENIS_BAYAR,COALESCE(SS.JUMLAH_BAYAR,0)AS JUMLAH_BAYAR,SS.CICILAN_KE,SS.NAMA_PENYETOR,SS.DENDA,"
					+ "SS.TGL_SSPD,SS.NO_SSPD,P.TANGGAL_PERIKSA AS TANGGAL_LAPOR,COALESCE(SS.LUNAS,FALSE)AS LUNAS,SS.TGL_CETAK,SS.DENDA_LUNAS,SS.NOTAANTRIBAYAR "
					+ "FROM PUBLIC.PERIKSA P left JOIN PUBLIC.SSPD SS ON SS.NO_SKP=P.NO_SKP AND SS.NPWPD=P.NPWPD WHERE P.No_SKP not like '%SKPDN%' and "
					+ "P.No_SKP = '"
					+ noSKP
					+ "' AND P.NPWPD = '"
					+ NPWPD
					+ "';";
		}
		ResultSet result = db.ResultExecutedStatementQuery(query,
				DBConnection.INSTANCE.open());
		int count = 0;
		if (result != null) {
			try {
				while (result.next()) {
					if (count == 0) {
						sspd.setIdSspd(result.getInt("idSspd"));
						sspd.setNpwpd(result.getString("NPWPD"));
						sspd.setNoSkp(result.getString("No_SKP"));
						if (result.getString("no_sspd") != null) {
							sspd.setNoSspd(result.getString("no_sspd"));
						}
						// else
						// sspd.setNoSspd("");
						if (result.getDate("tgl_sspd") != null) {
							sspd.setTglSspd(result.getTimestamp("tgl_sspd"));
						}
						if (result.getDate("tgl_cetak") != null) {
							sspd.setTglCetak(result.getTimestamp("tgl_cetak"));
						}

						if (result.getString("NotaAntriBayar") != null) {
							sspd.setNotaBayar(result
									.getString("NotaAntriBayar"));
						}

						if (Double.toString(result.getDouble("jumlah_bayar")) != null) {
							sspd.setJumlahBayar(result
									.getDouble("jumlah_bayar"));
						}
						// else
						// sspd.setJumlahBayar(0.0);
						if (result.getString("nama_penyetor") != null) {
							sspd.setNamaPenyetor(result
									.getString("nama_penyetor"));
						}
						// else
						// sspd.setNamaPenyetor("");
						if (Integer.toString(result.getInt("cicilan_ke")) != null) {
							sspd.setCicilanKe(result.getInt("cicilan_ke"));
						}
						if (result.getString("cara_bayar") != null) {
							sspd.setCaraBayar(result.getString("cara_bayar"));
						}
						if (NPWPD.substring(0, 1).equalsIgnoreCase("7")) {
							if (result.getString("Lokasi") != null) {
								sspd.setLokasi(result.getString("lokasi"));
							}
						}
						sspd.setDenda(result.getDouble("denda"));
						sspd.setJenisBayar(result.getInt("jenis_bayar"));
						sspd.setLunas(result.getBoolean("Lunas") == true ? true
								: false);
						sspd.setDendaLunas(result.getBoolean("Denda_Lunas") == true ? true
								: false);
					} else {
						if (!result.getString("No_SSPD").equalsIgnoreCase("")) {
							sspd.setNoSspd(sspd.getNoSspd().concat(
									"-" + result.getString("No_SSPD")));
						} else {
							sspd.setNoSspd(sspd.getNoSspd().concat(
									"-" + result.getString("NotaAntriBayar")));
						}
						if (Double.toString(result.getDouble("jumlah_bayar")) != null) {
							sspd.setJumlahBayar(sspd.getJumlahBayar()
									+ result.getDouble("jumlah_bayar"));
						}
					}
					count++;
				}
				return sspd;
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}

		} else {
			return null;
		}
	}

	public Sspd GetSSPDOld(int id) {
		Sspd sspd = new Sspd();
		String query = "Select * from SSPD WHERE IDSSPD = " + id;
		ResultSet result = db.ResultExecutedStatementQuery(query,
				DBConnection.INSTANCE.open());
		try {
			if (result.next()) {
				sspd.setNoSkp(result.getString("No_SKP"));
				sspd.setCaraBayar(result.getString("cara_bayar"));
				sspd.setJenisBayar(result.getInt("jenis_bayar"));
				sspd.setJumlahBayar(result.getDouble("jumlah_bayar"));
				sspd.setNamaPenyetor(result.getString("nama_penyetor"));
				sspd.setTglSspd(result.getTimestamp("tgl_sspd"));
				sspd.setTglCetak(result.getTimestamp("tgl_cetak"));
				if (result.getString("no_sspd") != null)
					sspd.setNoSspd(result.getString("no_sspd"));
				else
					sspd.setNoSspd("");
				if (result.getString("NotaAntriBayar") != null)
					sspd.setNotaBayar(result.getString("NotaAntriBayar"));
				else
					sspd.setNotaBayar("");

				return sspd;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

		return null;
	}

	public Sspd getSSPD_SKPD(String NPWPD, String noSKP, String bulanSKPD) {
		Sspd sspd = new Sspd();
		String query = "select * from GETSSPDSPTPD WHERE No_SKP = '" + noSKP
				+ "' AND NPWPD = '" + NPWPD + "' AND BULAN_SKP = '" + bulanSKPD
				+ "';";

		ResultSet result = db.ResultExecutedStatementQuery(query,
				DBConnection.INSTANCE.open());
		if (result != null) {
			try {
				if (result.next()) {
					sspd.setIdSspd(result.getInt("idSspd"));
					sspd.setNpwpd(result.getString("NPWPD"));
					sspd.setNoSkp(result.getString("No_SKP"));
					if (result.getString("no_sspd") != null) {
						sspd.setNoSspd(result.getString("no_sspd"));
					}
					if (result.getString("NotaAntriBayar") != null)
						sspd.setNotaBayar(result.getString("NotaAntriBayar"));
					// else
					// sspd.setNoSspd("");
					if (result.getDate("tgl_sspd") != null) {
						sspd.setTglSspd(result.getTimestamp("tgl_sspd"));
					}
					if (result.getDate("tgl_cetak") != null) {
						sspd.setTglCetak(result.getTimestamp("tgl_cetak"));
					}
					if (Double.toString(result.getDouble("jumlah_bayar")) != null) {
						sspd.setJumlahBayar(result.getDouble("jumlah_bayar"));
					}
					// else
					// sspd.setJumlahBayar(0.0);
					if (result.getString("nama_penyetor") != null) {
						sspd.setNamaPenyetor(result.getString("nama_penyetor"));
					}
					// else
					// sspd.setNamaPenyetor("");
					if (Integer.toString(result.getInt("cicilan_ke")) != null) {
						sspd.setCicilanKe(result.getInt("cicilan_ke"));
					}
					if (result.getString("cara_bayar") != null) {
						sspd.setCaraBayar(result.getString("cara_bayar"));
					}
					if (NPWPD.substring(0, 1).equalsIgnoreCase("7")) {
						if (result.getString("Lokasi") != null) {
							sspd.setLokasi(result.getString("lokasi"));
						}
					}
					sspd.setDenda(result.getDouble("denda"));
					sspd.setJenisBayar(result.getInt("jenis_bayar"));
					sspd.setLunas(result.getBoolean("Lunas") == true ? true
							: false);
					sspd.setDendaLunas(result.getBoolean("Denda_Lunas") == true ? true
							: false);
				}
				return sspd;
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}

		} else {
			return null;
		}
	}

	public String getNoSSPD(String varTahun) {
		String query = "select CaseWhen (COUNT(No_SSPD) > 0, CONCAT(MAX(CAST(SUBSTRING(No_SSPD,1,length(no_sspd) - 10) AS int)) + 1, '/SSPD/', '"
				+ varTahun
				+ "'), "
				+ "CONCAT('1/SSPD/','"
				+ varTahun
				+ "')) as NOSSPD from sspd WHERE YEAR(Tgl_Cetak) = '"
				+ varTahun + "' and NO_SSPD <> '';";
		ResultSet result = db.ResultExecutedStatementQuery(query,
				DBConnection.INSTANCE.open());
		try {
			if (result.next()) {
				return result.getString("NOSSPD");
			} else
				return "";
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}
	
	public String getNumSSPD(String varSKP) {
		String query = "select NO_SSPD from SSPD join SPTPD on SSPD.IDSPTPD = SPTPD.IDSPTPD where NO_SPTPD ='"+varSKP+"';";
		ResultSet result = db.ResultExecutedStatementQuery(query,
				DBConnection.INSTANCE.open());
		try {
			if (result.next()) {
				return result.getString("NO_SSPD");
			} else
				return "";
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}
	
	public String getDendaSSPD(String varSKP) {
		String query = "select DENDA from SSPD join SPTPD on SSPD.IDSPTPD = SPTPD.IDSPTPD where NO_SPTPD ='"+varSKP+"';";
		ResultSet result = db.ResultExecutedStatementQuery(query,
				DBConnection.INSTANCE.open());
		try {
			if (result.next()) {
				return String.valueOf((int)result.getDouble("DENDA"));
			} else
				return "";
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}

	public String getNotaBayar(String tanggal) {
		String query = "select CaseWhen (COUNT(NotaAntriBayar) > 0, CONCAT(MAX(CAST(SUBSTRING(NotaAntriBayar, 1, length(NotaAntriBayar) - 7) AS int)) + 1, '/', '"
				+ tanggal
				+ "'), "
				+ "CONCAT('1/', '"
				+ tanggal
				+ "')) as NotaBayar from sspd WHERE SUBSTRING(NotaAntriBayar, length(NotaAntriBayar) - 5) = '"
				+ tanggal + "';";
		ResultSet result = db.ResultExecutedStatementQuery(query,
				DBConnection.INSTANCE.open());
		try {
			if (result.next()) {
				return result.getString("NotaBayar");
			} else
				return "";
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}

	@SuppressWarnings("deprecation")
	public ResultSet getDaftarSSPD(String tipeSKP, String KodePajak,
			Date masaPajakDari, Date masaPajakSampai) {
		SimpleDateFormat formatDari = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat formatSampai = new SimpleDateFormat(
				"yyyy-MM-dd 23:59:59");
		String masaDari = formatDari.format(masaPajakDari);
		String masaSampai = formatSampai.format(masaPajakSampai);
		String tipeSKPQuery = "";
		String query = "";
		if (masaPajakSampai.equals(masaPajakDari)) {
			query = "SELECT s.No_SSPD, rg.No_Registrasi, s.Tgl_SSPD, s.Tgl_Cetak, s.NPWPD, wp.Nama_Badan, p.Nama_Pajak, b.Nama_Bid_Usaha as Bidang_Usaha, s.No_SKP, "
					+ "casewhen(s.IsDenda=0, IFNULL(md.Angsuran_Pokok, IFNULL(sp.Pajak_Terutang, (pr.Total_Pajak_Periksa - pr.Total_Pajak_Terutang + pr.Total_Kenaikan))),0) as PokokPajak, "
					+ "casewhen(s.IsDenda=0, IFNULL((md.Biaya_Administrasi + md.Denda_SKPDKB), IFNULL(sp.Denda_Tambahan, (pr.Total_Pajak_Bunga + pr.Total_Denda))),0) as Denda, "
					+ "s.Denda as DendaTerlambatBayar, 0 as DendaDibayar, s.Jumlah_Bayar, s.Bulan_SKP, s.Nama_Penyetor, ifnull(sp.Tgl_SPTPD, pr.Tanggal_SKP) as Tanggal_SKP FROM sspd s "
					+ "INNER JOIN wajib_pajak wp ON wp.NPWPD = s.NPWPD "
					+ "INNER JOIN bidang_usaha b ON b.IdSub_Pajak = s.IdSub_Pajak "
					+ "INNER JOIN pajak p ON p.Kode_Pajak = b.idpajak "
					+ "left JOIN periksa pr on pr.IDPERIKSA = s.IDPERIKSA "
					+ "left JOIN sptpd sp on sp.IDSPTPD = s.IDSPTPD "
					+ "left join mohon m on m.NPWPD = s.NPWPD and m.No_SKP = s.No_SKP "
					+ "left join mohon_detail md on md.IdMohon = m.IdMohon and s.Cicilan_Ke = md.No_Angsuran "
					+ "left join No_registrasi rg on rg.No_SSPD = s.No_SSPD "
					+ "WHERE s.Tgl_Cetak like '"
					+ masaDari
					+ "%' AND s.NO_SKP like '%"
					+ tipeSKP
					+ "%' "
					+ "AND p.Kode_Pajak = '"
					+ KodePajak
					+ "' AND s.Lunas = true ORDER BY Tanggal_SKP, rg.No_Registrasi;";
		} else {
			if (tipeSKP.equalsIgnoreCase("SKPDKB")
					|| tipeSKP.equalsIgnoreCase("SKPDKBT"))
				// tipeSKPQuery = "AND s.idperiksa > 0";// untuk mendapatkan
				// SSPD -> SKPDKB | SKPDKBT
				tipeSKPQuery = "AND (s.no_skp like '%/SKPDKB/%' OR s.no_skp like '%/SKPDKBT/%')";
			else if (tipeSKP.equalsIgnoreCase("SPTPD")
					|| tipeSKP.equalsIgnoreCase("SKPD"))
				// tipeSKPQuery = "AND s.idperiksa = 0"; // untuk mendapatkan
				// SSPD -> SPTPD | SKPD
				tipeSKPQuery = "AND (s.no_skp like '%/SPTPD/%' OR s.no_skp like '%/SKPD/%')";
			else
				tipeSKPQuery = "";
			query = "SELECT s.No_SSPD, rg.No_Registrasi, s.Tgl_SSPD, s.Tgl_Cetak, s.NPWPD, wp.Nama_Badan, p.Nama_Pajak, b.Nama_Bid_Usaha as Bidang_Usaha, s.No_SKP, "
					+ "casewhen(s.IsDenda=0, IFNULL(md.Angsuran_Pokok, IFNULL(sp.Pajak_Terutang, (pr.Total_Pajak_Periksa - pr.Total_Pajak_Terutang  + pr.Total_Kenaikan ))),0) as PokokPajak, "
					+ "casewhen(s.IsDenda=0, IFNULL((md.Biaya_Administrasi + md.Denda_SKPDKB), IFNULL(sp.Denda_Tambahan, (pr.Total_Pajak_Bunga+ pr.Total_Denda))),0) as Denda, "
					+ "s.Denda as DendaTerlambatBayar, 0 as DendaDibayar, s.Jumlah_Bayar, s.Bulan_SKP, s.Nama_Penyetor,ifnull(sp.TGL_SPTPD, pr.Tanggal_SKP) as Tanggal_SKP FROM sspd s "
					+ "INNER JOIN wajib_pajak wp ON wp.NPWPD = s.NPWPD "
					+ "INNER JOIN bidang_usaha b ON b.IdSub_Pajak = s.IdSub_Pajak "
					+ "INNER JOIN pajak p ON p.Kode_Pajak = b.idpajak "
					+ "left JOIN periksa pr on pr.IDPERIKSA = s.IDPERIKSA "
					+ "left JOIN sptpd sp on sp.IDSPTPD = s.IDSPTPD "
					+ "left join mohon m on m.NPWPD = s.NPWPD and m.No_SKP = s.No_SKP "
					+ "left join mohon_detail md on md.IdMohon = m.IdMohon and s.Cicilan_Ke = md.No_Angsuran "
					+ "left join No_registrasi rg on rg.No_SSPD = s.No_SSPD "
					+ "WHERE (s.Tgl_Cetak between Date '"
					+ masaDari
					+ "' AND Timestamp '"
					+ masaSampai
					+ "') "
					+ "AND p.Kode_Pajak = '"
					+ KodePajak
					+ "' AND s.Lunas = true "
					+ tipeSKPQuery
					+ " ORDER BY Tanggal_SKP,s.NO_SKP ;";
			/*
			 * query =
			 * "SELECT s.No_SSPD, rg.No_Registrasi, s.Tgl_SSPD, s.NPWPD, wp.Nama_Badan, p.Nama_Pajak, b.Nama_Bid_Usaha as Bidang_Usaha, s.No_SKP, IFNULL(md.Angsuran_Pokok, IFNULL(sp.Pajak_Terutang, (pr.Total_Pajak_Periksa - pr.Total_Pajak_Terutang + pr.Total_Kenaikan))) as PokokPajak, "
			 * +
			 * "IFNULL((md.Biaya_Administrasi + md.Denda_SKPDKB), IFNULL(sp.Denda_Tambahan, (pr.Total_Pajak_Bunga  + pr.Total_Denda))) as Denda, "
			 * +
			 * "s.Denda as DendaTerlambatBayar, s.Jumlah_Bayar, s.Bulan_SKP, s.Nama_Penyetor,pr.TANGGAL_SKP FROM sspd s "
			 * + "INNER JOIN wajib_pajak wp ON wp.NPWPD = s.NPWPD " +
			 * "INNER JOIN bidang_usaha b ON b.IdSub_Pajak = s.IdSub_Pajak " +
			 * "INNER JOIN pajak p ON p.Kode_Pajak = b.idpajak " +
			 * "left JOIN periksa pr on pr.NPWPD = s.npwpd and s.no_skp = pr.No_SKP "
			 * +
			 * "left JOIN sptpd sp on sp.NPWPD = s.npwpd and s.no_skp = sp.No_SPTPD "
			 * +
			 * "left join mohon m on m.NPWPD = s.NPWPD and m.No_SKP = s.No_SKP "
			 * +
			 * "left join mohon_detail md on md.IdMohon = m.IdMohon and s.Cicilan_Ke = md.No_Angsuran "
			 * + "left join No_registrasi rg on rg.No_SSPD = s.No_SSPD " +
			 * "WHERE (s.Tgl_SSPD between Date '" + masaDari +
			 * "' AND Timestamp '" + masaSampai + "') " + "AND p.Kode_Pajak = '"
			 * + KodePajak + "' AND s.Lunas = true "+tipeSKPQuery+
			 * " ORDER BY pr.TANGGAL_SKP,s.NO_SKP ;";
			 */
		}
		ResultSet result = null;
		if (masaPajakDari.getMonth() == masaPajakSampai.getMonth())
			result = db.ResultExecutedStatementQuery(query,
					DBConnection.INSTANCE.open());
		else
			result = db.ResultExecutedStatementQuery(query,
					DBConnection.INSTANCE.openBawah());
		return result;
	}

	public ResultSet getDaftarSSPDTidakBayar(String tipeSKP, String KodePajak,
			Date masaPajakDari, Date masaPajakSampai) {
		SimpleDateFormat formatDari = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat formatSampai = new SimpleDateFormat(
				"yyyy-MM-dd 23:59:59");
		String masaDari = formatDari.format(masaPajakDari);
		String masaSampai = formatSampai.format(masaPajakSampai);
		String tipeSKPQuery = "";
		String query = "";
		if (masaPajakSampai.equals(masaPajakDari)) {
			query = "SELECT s.No_SSPD, rg.No_Registrasi, s.Tgl_SSPD, s.NPWPD, wp.Nama_Badan, p.Nama_Pajak, b.Nama_Bid_Usaha as Bidang_Usaha, s.No_SKP, "
					+ "casewhen(s.IsDenda=0, IFNULL(md.Angsuran_Pokok, IFNULL(sp.Pajak_Terutang, (pr.Total_Pajak_Periksa - pr.Total_Pajak_Terutang + pr.Total_Kenaikan))),0) as PokokPajak, "
					+ "IFNULL((md.Biaya_Administrasi + md.Denda_SKPDKB), IFNULL(sp.Denda_Tambahan, (pr.Total_Pajak_Bunga + pr.Total_Denda))) as Denda, "
					+ "s.Denda as DendaTerlambatBayar, s.Jumlah_Bayar, s.Bulan_SKP, s.Nama_Penyetor, ifnull(sp.Tgl_SPTPD, pr.Tanggal_SKP) as Tanggal_SKP FROM sptpd sp "
					+ "INNER JOIN wajib_pajak wp ON wp.NPWPD = s.NPWPD "
					+ "INNER JOIN bidang_usaha b ON b.IdSub_Pajak = s.IdSub_Pajak "
					+ "INNER JOIN pajak p ON p.Kode_Pajak = b.idpajak "
					+ "left JOIN periksa pr on pr.IDPERIKSA = s.IDPERIKSA "
					+ "left JOIN sspd s on sp.IDSPTPD = s.IDSPTPD "
					+ "left join mohon m on m.NPWPD = s.NPWPD and m.No_SKP = s.No_SKP "
					+ "left join mohon_detail md on md.IdMohon = m.IdMohon and s.Cicilan_Ke = md.No_Angsuran "
					+ "left join No_registrasi rg on rg.No_SSPD = s.No_SSPD "
					+ "WHERE s.Tgl_Cetak like '"
					+ masaDari
					+ "%' AND s.NO_SKP like '%"
					+ tipeSKP
					+ "%' "
					+ "AND p.Kode_Pajak = '"
					+ KodePajak
					+ "' AND s.Lunas = true ORDER BY Tanggal_SKP, rg.No_Registrasi;";
		} else {
			if (tipeSKP.equalsIgnoreCase("SKPDKB")
					|| tipeSKP.equalsIgnoreCase("SKPDKBT"))
				// tipeSKPQuery = "AND s.idperiksa > 0";// untuk mendapatkan
				// SSPD -> SKPDKB | SKPDKBT
				tipeSKPQuery = "AND (s.no_skp like '%/SKPDKB/%' OR s.no_skp like '%/SKPDKBT/%')";
			else if (tipeSKP.equalsIgnoreCase("SPTPD")
					|| tipeSKP.equalsIgnoreCase("SKPD"))
				// tipeSKPQuery = "AND s.idperiksa = 0"; // untuk mendapatkan
				// SSPD -> SPTPD | SKPD
				tipeSKPQuery = "AND (s.no_skp like '%/SPTPD/%' OR s.no_skp like '%/SKPD/%')";
			else
				tipeSKPQuery = "";
			query = "SELECT s.No_SSPD, rg.No_Registrasi, s.Tgl_SSPD, s.NPWPD, wp.Nama_Badan, p.Nama_Pajak, b.Nama_Bid_Usaha as Bidang_Usaha, s.No_SKP, IFNULL(md.Angsuran_Pokok, IFNULL(sp.Pajak_Terutang, (pr.Total_Pajak_Periksa - pr.Total_Pajak_Terutang  + pr.Total_Kenaikan ))) as PokokPajak, "
					+ "IFNULL((md.Biaya_Administrasi + md.Denda_SKPDKB), IFNULL(sp.Denda_Tambahan, (pr.Total_Pajak_Bunga+ pr.Total_Denda))) as Denda, "
					+ "s.Denda as DendaTerlambatBayar, s.Jumlah_Bayar, s.Bulan_SKP, s.Nama_Penyetor,ifnull(sp.TGL_SPTPD, pr.Tanggal_SKP) as Tanggal_SKP FROM sptpd sp "
					+ "INNER JOIN wajib_pajak wp ON wp.NPWPD = s.NPWPD "
					+ "INNER JOIN bidang_usaha b ON b.IdSub_Pajak = s.IdSub_Pajak "
					+ "INNER JOIN pajak p ON p.Kode_Pajak = b.idpajak "
					+ "left JOIN periksa pr on pr.idperiksa = s.idperiksa "
					+ "left JOIN sspd s on sp.idsptpd = s.idsptpd "
					+ "left join mohon m on m.NPWPD = s.NPWPD and m.No_SKP = s.No_SKP "
					+ "left join mohon_detail md on md.IdMohon = m.IdMohon and s.Cicilan_Ke = md.No_Angsuran "
					+ "left join No_registrasi rg on rg.No_SSPD = s.No_SSPD "
					+ "WHERE (sp.TGL_SPTPD between Date '"
					+ masaDari
					+ "' AND Timestamp '"
					+ masaSampai
					+ "') "
					+ "AND p.Kode_Pajak = '"
					+ KodePajak
					+ "' AND s.Lunas = true "
					+ tipeSKPQuery
					+ " ORDER BY Tanggal_SKP,s.NO_SKP ;";
			/*
			 * query =
			 * "SELECT s.No_SSPD, rg.No_Registrasi, s.Tgl_SSPD, s.NPWPD, wp.Nama_Badan, p.Nama_Pajak, b.Nama_Bid_Usaha as Bidang_Usaha, s.No_SKP, IFNULL(md.Angsuran_Pokok, IFNULL(sp.Pajak_Terutang, (pr.Total_Pajak_Periksa - pr.Total_Pajak_Terutang + pr.Total_Kenaikan))) as PokokPajak, "
			 * +
			 * "IFNULL((md.Biaya_Administrasi + md.Denda_SKPDKB), IFNULL(sp.Denda_Tambahan, (pr.Total_Pajak_Bunga  + pr.Total_Denda))) as Denda, "
			 * +
			 * "s.Denda as DendaTerlambatBayar, s.Jumlah_Bayar, s.Bulan_SKP, s.Nama_Penyetor,pr.TANGGAL_SKP FROM sspd s "
			 * + "INNER JOIN wajib_pajak wp ON wp.NPWPD = s.NPWPD " +
			 * "INNER JOIN bidang_usaha b ON b.IdSub_Pajak = s.IdSub_Pajak " +
			 * "INNER JOIN pajak p ON p.Kode_Pajak = b.idpajak " +
			 * "left JOIN periksa pr on pr.NPWPD = s.npwpd and s.no_skp = pr.No_SKP "
			 * +
			 * "left JOIN sptpd sp on sp.NPWPD = s.npwpd and s.no_skp = sp.No_SPTPD "
			 * +
			 * "left join mohon m on m.NPWPD = s.NPWPD and m.No_SKP = s.No_SKP "
			 * +
			 * "left join mohon_detail md on md.IdMohon = m.IdMohon and s.Cicilan_Ke = md.No_Angsuran "
			 * + "left join No_registrasi rg on rg.No_SSPD = s.No_SSPD " +
			 * "WHERE (s.Tgl_SSPD between Date '" + masaDari +
			 * "' AND Timestamp '" + masaSampai + "') " + "AND p.Kode_Pajak = '"
			 * + KodePajak + "' AND s.Lunas = true "+tipeSKPQuery+
			 * " ORDER BY pr.TANGGAL_SKP,s.NO_SKP ;";
			 */
		}
		ResultSet result = db.ResultExecutedStatementQuery(query,
				DBConnection.INSTANCE.open());
		return result;
	}

	public ResultSet getDaftarSSPDDendaSPT(String KodePajak,
			Date masaPajakDari, Date masaPajakSampai) {
		SimpleDateFormat formatDari = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat formatSampai = new SimpleDateFormat(
				"yyyy-MM-dd 23:59:59");
		String masaDari = formatDari.format(masaPajakDari);
		String masaSampai = formatSampai.format(masaPajakSampai);
		String query = "";
		if (masaPajakSampai.equals(masaPajakDari)) {
			query = "SELECT min(s.No_SSPD) as No_SSPD, min(rg.No_Registrasi) as No_Registrasi, min(s.Tgl_SSPD) as Tgl_SSPD, s.NPWPD, wp.Nama_Badan, p.Nama_Pajak, b.Nama_Bid_Usaha as Bidang_Usaha, "
					+ "s.No_SKP, min(sp.Pajak_Terutang) as PokokPajak, min(sp.Denda_Tambahan) as Denda, min(s.Denda) as DendaTerlambatBayar, sum(s.Jumlah_Bayar) as Jumlah_Bayar, s.Bulan_SKP, "
					+ "min(s.Nama_Penyetor) as Nama_Penyetor, min(sp.TGL_SPTPD) as Tanggal_SKP, min(s.Cicilan_Ke) as Cicilan_Ke FROM sspd s INNER JOIN wajib_pajak wp ON wp.NPWPD = s.NPWPD "
					+ "INNER JOIN bidang_usaha b ON b.IdSub_Pajak = s.IdSub_Pajak INNER JOIN pajak p ON p.Kode_Pajak = b.idpajak "
					+ "left JOIN sptpd sp on sp.idsptpd = s.idsptpd left join No_registrasi rg on rg.No_SSPD = s.No_SSPD "
					+ "WHERE s.Tgl_Cetak like '"
					+ masaDari
					+ "%' AND p.Kode_Pajak = '"
					+ KodePajak
					+ "' AND s.Lunas = true "
					+ "AND s.No_SKP not like '%/SKPDKB%' " +
					// "AND s.TGL_SSPD > LAST_DAY(DATE_ADD(sp.Masa_Pajak_Dari, INTERVAL 1 Month)) "
					// +
					"Group By sp.IDSPTPD, s.NPWPD, wp.Nama_Badan, p.Nama_Pajak, Bidang_Usaha, s.NO_SKP, s.BULAN_SKP ORDER BY Tanggal_SKP, No_Registrasi;";
		} else {
			query = "SELECT min(s.No_SSPD) as No_SSPD, min(rg.No_Registrasi) as No_Registrasi, min(s.Tgl_SSPD) as Tgl_SSPD, s.NPWPD, wp.Nama_Badan, p.Nama_Pajak, b.Nama_Bid_Usaha as Bidang_Usaha, "
					+ "s.No_SKP, min(sp.Pajak_Terutang) as PokokPajak, min(sp.Denda_Tambahan) as Denda, min(s.Denda) as DendaTerlambatBayar, sum(s.Jumlah_Bayar) as Jumlah_Bayar, s.Bulan_SKP, "
					+ "min(s.Nama_Penyetor) as Nama_Penyetor, min(sp.TGL_SPTPD) as Tanggal_SKP, min(s.Cicilan_Ke) as Cicilan_Ke FROM sspd s INNER JOIN wajib_pajak wp ON wp.NPWPD = s.NPWPD "
					+ "INNER JOIN bidang_usaha b ON b.IdSub_Pajak = s.IdSub_Pajak INNER JOIN pajak p ON p.Kode_Pajak = b.idpajak "
					+ "left JOIN sptpd sp on sp.idsptpd = s.idsptpd left join No_registrasi rg on rg.No_SSPD = s.No_SSPD "
					+ "WHERE (s.Tgl_Cetak between Date '"
					+ masaDari
					+ "' AND Timestamp '"
					+ masaSampai
					+ "') AND p.Kode_Pajak = '"
					+ KodePajak
					+ "' AND s.Lunas = true "
					+ "AND s.No_SKP not like '%/SKPDKB%' " +
					// "AND s.TGL_SSPD > LAST_DAY(DATE_ADD(sp.Masa_Pajak_Dari, INTERVAL 1 Month)) "
					// +
					"Group By sp.IDSPTPD, s.NPWPD, wp.Nama_Badan, p.Nama_Pajak, Bidang_Usaha, s.NO_SKP, s.BULAN_SKP ORDER BY Tanggal_SKP, No_Registrasi;";
		}
		ResultSet result = db.ResultExecutedStatementQuery(query,
				DBConnection.INSTANCE.open());
		return result;
	}

	public ResultSet getDaftarSSPDDendaSKPDKB(String KodePajak,
			Date masaPajakDari, Date masaPajakSampai) {
		SimpleDateFormat formatDari = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat formatSampai = new SimpleDateFormat(
				"yyyy-MM-dd 23:59:59");
		String masaDari = formatDari.format(masaPajakDari);
		String masaSampai = formatSampai.format(masaPajakSampai);
		String query = "";
		if (masaPajakSampai.equals(masaPajakDari)) {
			query = "SELECT min(s.No_SSPD) as No_SSPD, min(rg.No_Registrasi) as No_Registrasi, min(s.Tgl_SSPD) as Tgl_SSPD, s.NPWPD, wp.Nama_Badan, p.Nama_Pajak, "
					+ "b.Nama_Bid_Usaha as Bidang_Usaha, s.No_SKP, min(IFNULL(md.Angsuran_Pokok, pr.Total_Pajak_Periksa - pr.Total_Pajak_Terutang  + pr.Total_Kenaikan )) as PokokPajak, "
					+ "min(IFNULL((md.Biaya_Administrasi + md.Denda_SKPDKB), (pr.Total_Pajak_Bunga+ pr.Total_Denda))) as Denda, min(s.Denda) as DendaTerlambatBayar, "
					+ "sum(s.Jumlah_Bayar) as Jumlah_Bayar, s.Bulan_SKP, min(s.Nama_Penyetor) as Nama_Penyetor, min(pr.Tanggal_SKP) as Tanggal_SKP, s.CICILAN_KE FROM sspd s "
					+ "INNER JOIN wajib_pajak wp ON wp.NPWPD = s.NPWPD INNER JOIN bidang_usaha b ON b.IdSub_Pajak = s.IdSub_Pajak "
					+ "INNER JOIN pajak p ON p.Kode_Pajak = b.idpajak left JOIN periksa pr on pr.idperiksa = s.idperiksa "
					+ "left join mohon m on m.NPWPD = s.NPWPD and m.No_SKP = s.No_SKP left join mohon_detail md on md.IdMohon = m.IdMohon and s.Cicilan_Ke = md.No_Angsuran "
					+ "left join No_registrasi rg on rg.No_SSPD = s.No_SSPD WHERE s.Tgl_Cetak like '"
					+ masaDari
					+ "%' AND p.Kode_Pajak = '"
					+ KodePajak
					+ "' AND s.Lunas = true "
					+ "AND s.No_SKP like '%/SKPDKB%' " +
					// "AND s.TGL_SSPD > DATE_ADD(IFNULL(md.TANGGAL_ANGSURAN, pr.TANGGAL_SKP), INTERVAL 1 Month) "
					// +
					"Group By pr.IDPeriksa, s.NPWPD, wp.Nama_Badan, p.Nama_Pajak, Bidang_Usaha, s.NO_SKP, s.BULAN_SKP, s.Cicilan_ke ORDER BY Tanggal_SKP, No_Registrasi;";
		} else {
			query = "SELECT min(s.No_SSPD) as No_SSPD, min(rg.No_Registrasi) as No_Registrasi, min(s.Tgl_SSPD) as Tgl_SSPD, s.NPWPD, wp.Nama_Badan, p.Nama_Pajak, "
					+ "b.Nama_Bid_Usaha as Bidang_Usaha, s.No_SKP, min(IFNULL(md.Angsuran_Pokok, pr.Total_Pajak_Periksa - pr.Total_Pajak_Terutang  + pr.Total_Kenaikan )) as PokokPajak, "
					+ "min(IFNULL((md.Biaya_Administrasi + md.Denda_SKPDKB), (pr.Total_Pajak_Bunga+ pr.Total_Denda))) as Denda, min(s.Denda) as DendaTerlambatBayar, "
					+ "sum(s.Jumlah_Bayar) as Jumlah_Bayar, s.Bulan_SKP, min(s.Nama_Penyetor) as Nama_Penyetor, min(pr.Tanggal_SKP) as Tanggal_SKP, s.CICILAN_KE FROM sspd s INNER JOIN wajib_pajak wp ON wp.NPWPD = s.NPWPD "
					+ "INNER JOIN bidang_usaha b ON b.IdSub_Pajak = s.IdSub_Pajak INNER JOIN pajak p ON p.Kode_Pajak = b.idpajak "
					+ "left JOIN periksa pr on pr.idperiksa = s.idperiksa left join mohon m on m.NPWPD = s.NPWPD and m.No_SKP = s.No_SKP "
					+ "left join mohon_detail md on md.IdMohon = m.IdMohon and s.Cicilan_Ke = md.No_Angsuran left join No_registrasi rg on rg.No_SSPD = s.No_SSPD "
					+ "WHERE (s.Tgl_Cetak between Date '"
					+ masaDari
					+ "' AND Timestamp '"
					+ masaSampai
					+ "') AND p.Kode_Pajak = '"
					+ KodePajak
					+ "' AND s.Lunas = true "
					+ "AND s.No_SKP like '%/SKPDKB%' " +
					// "AND s.TGL_SSPD > DATE_ADD(IFNULL(md.TANGGAL_ANGSURAN, pr.TANGGAL_SKP), INTERVAL 1 Month) "
					// +
					"Group By pr.IDPeriksa, s.NPWPD, wp.Nama_Badan, p.Nama_Pajak, Bidang_Usaha, s.NO_SKP, s.BULAN_SKP, s.Cicilan_ke ORDER BY Tanggal_SKP, No_Registrasi;";
		}
		ResultSet result = db.ResultExecutedStatementQuery(query,
				DBConnection.INSTANCE.open());
		return result;
	}

	public ResultSet getDaftarSSPDAll(Date masaPajakDari, Date masaPajakSampai,
			String tipeSKP) {
		SimpleDateFormat formatDari = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat formatSampai = new SimpleDateFormat(
				"yyyy-MM-dd 23:59:59");
		String masaDari = formatDari.format(masaPajakDari);
		String masaSampai = formatSampai.format(masaPajakSampai);
		String query = "";
		if (masaPajakSampai.equals(masaPajakDari)) {
			query = "SELECT s.No_SSPD, rg.No_Registrasi, s.Tgl_SSPD, s.NPWPD, wp.Nama_Badan, p.Nama_Pajak, b.Nama_Bid_Usaha as Bidang_Usaha, s.No_SKP, "
					+ "casewhen(s.IsDenda=0, IFNULL(md.Angsuran_Pokok, IFNULL(sp.Pajak_Terutang, (pr.Total_Pajak_Periksa - pr.Total_Pajak_Terutang + pr.Total_Kenaikan))), 0) as PokokPajak, "
					+ "IFNULL((md.Biaya_Administrasi + md.Denda_SKPDKB), IFNULL(sp.Denda_Tambahan, (pr.Total_Pajak_Bunga + pr.Total_Denda))) as Denda, "
					+ "s.Denda as DendaTerlambatBayar, 0 as DendaDibayar, s.Jumlah_Bayar, s.Bulan_SKP, s.Nama_Penyetor FROM sspd s "
					+ "INNER JOIN wajib_pajak wp ON wp.NPWPD = s.NPWPD "
					+ "INNER JOIN bidang_usaha b ON b.IdSub_Pajak = s.IdSub_Pajak "
					+ "INNER JOIN pajak p ON p.Kode_Pajak = b.idpajak "
					+ "left JOIN periksa pr on pr.IDPERIKSA = s.IDPERIKSA "
					+ "left JOIN sptpd sp on sp.IDSPTPD = s.IDSPTPD "
					+ "left join mohon m on m.NPWPD = s.NPWPD and m.No_SKP = s.No_SKP "
					+ "left join mohon_detail md on md.IdMohon = m.IdMohon and s.Cicilan_Ke = md.No_Angsuran "
					+ "left join No_registrasi rg on rg.No_SSPD = s.No_SSPD "
					+ "WHERE s.Tgl_Cetak like '"
					+ masaDari
					+ "%' AND s.NO_SKP like '%"
					+ tipeSKP
					+ "%' "
					+ "AND s.Lunas = true ORDER BY rg.No_Registrasi;";
		} else {
			query = "SELECT s.No_SSPD, rg.No_Registrasi, s.Tgl_SSPD, s.NPWPD, wp.Nama_Badan, p.Nama_Pajak, b.Nama_Bid_Usaha as Bidang_Usaha, s.No_SKP, "
					+ "casewhen(s.IsDenda=0, IFNULL(md.Angsuran_Pokok, IFNULL(sp.Pajak_Terutang, (pr.Total_Pajak_Periksa - pr.Total_Pajak_Terutang))),0) as PokokPajak, "
					+ "IFNULL((md.Biaya_Administrasi + md.Denda_SKPDKB), IFNULL(sp.Denda_Tambahan, (pr.Total_Pajak_Bunga + pr.Total_Kenaikan + pr.Total_Denda))) as Denda, "
					+ "s.Denda as DendaTerlambatBayar, 0 as DendaDibayar, s.Jumlah_Bayar, s.Bulan_SKP, s.Nama_Penyetor FROM sspd s "
					+ "INNER JOIN wajib_pajak wp ON wp.NPWPD = s.NPWPD "
					+ "INNER JOIN bidang_usaha b ON b.IdSub_Pajak = s.IdSub_Pajak "
					+ "INNER JOIN pajak p ON p.Kode_Pajak = b.idpajak "
					+ "left JOIN periksa pr on pr.IDPERIKSA = s.IDPERIKSA "
					+ "left JOIN sptpd sp on sp.IDSPTPD = s.IDSPTPD "
					+ "left join mohon m on m.NPWPD = s.NPWPD and m.No_SKP = s.No_SKP "
					+ "left join mohon_detail md on md.IdMohon = m.IdMohon and s.Cicilan_Ke = md.No_Angsuran "
					+ "left join No_registrasi rg on rg.No_SSPD = s.No_SSPD "
					+ "WHERE (s.Tgl_Cetak between Date '"
					+ masaDari
					+ "' AND Timestamp '"
					+ masaSampai
					+ "') AND s.NO_SKP like '%"
					+ tipeSKP
					+ "%' "
					+ "AND s.Lunas = true ORDER BY rg.No_Registrasi;";
		}
		ResultSet result = db.ResultExecutedStatementQuery(query,
				DBConnection.INSTANCE.open());
		return result;
	}

	public boolean updateAntrianSSPD(String noSSPD, Integer idsspd) {
		boolean result = false;
		String query = "update SSPD set NO_SSPD = '" + noSSPD
				+ "', LUNAS = True where IDSSPD = " + idsspd + ";";
		result = db.ExecuteStatementQuery(query, DBConnection.INSTANCE.open());
		if (!result)
			MessageDialog.openError(Display.getCurrent().getActiveShell(),
					"Database Error",
					"Ada masalah pada MERGE(UPDATE/INSERT) tabel SSPD.");
		return result;
	}

	public boolean updateDendaSebelumnya(int idSSPD) {
		boolean result = false;
		String query = "update SSPD set Denda_Lunas = true where IDSSPD = "
				+ idSSPD;
		result = db.ExecuteStatementQuery(query, DBConnection.INSTANCE.open());
		if (!result)
			MessageDialog.openError(Display.getCurrent().getActiveShell(),
					"Database Error",
					"Ada masalah pada MERGE(UPDATE/INSERT) tabel SSPD.");
		return result;
	}

	public boolean TolakAntrianSSPD(Integer id) {
		String query = "DELETE FROM SSPD WHERE IDSSPD = " + id;
		boolean result = db.ExecuteStatementQuery(query,
				DBConnection.INSTANCE.open());
		if (!result)
			MessageDialog.openError(Display.getCurrent().getActiveShell(),
					"Database Error", "Ada masalah pada DELETE tabel SSPD");

		return result;

	}

	public ResultSet GetSSPDTepatWaktu(java.sql.Date varDari,
			java.sql.Date varSampai) {
		int bulan = UIController.INSTANCE.getMonthDiff(varDari, varSampai) + 1;
		String query = "SELECT DISTINCT ss.NPWPD, wp.Nama_Badan, sum(sp.Pajak_Terutang) / COUNT(ss.NPWPD) as d, COUNT(ss.NPWPD) as c, "
				+ "casewhen (CAST(TO_Char(sp.Masa_Pajak_Dari, 'DD') as INTEGER) > 1 OR CAST(To_Char(sp.Masa_Pajak_Sampai, 'DD') as INTEGER) < 27, "
				+ bulan
				* 2
				+ ", "
				+ bulan
				+ ") as SPTPD, wp.Alabad_Jalan "
				+ "FROM sspd ss INNER join sptpd sp on sp.idsptpd = ss.idsptpd "
				+ "INNER join wajib_pajak wp on wp.NPWPD = ss.NPWPD WHERE "
				+ "TO_DATE(Bulan_SKP, 'MM-YYYY') >= TO_DATE('"
				+ varDari
				+ "', 'YYYY-MM-DD') "
				+ "AND LAST_DAY(TO_DATE(Bulan_SKP, 'MM-YYYY')) <= LAST_DAY(TO_DATE('"
				+ varSampai
				+ "', 'YYYY-MM-DD')) "
				+ "AND ss.Denda = 0 AND ss.Denda_Lunas = TRUE AND sp.Assesment = 'Self' and wp.Status = 'Aktif' and wp.Insidentil = FALSE GROUP BY ss.NPWPD, wp.NAMA_BADAN, wp.Alabad_Jalan, SPTPD "
				+ "having COUNT(ss.NPWPD) >= " + bulan + " ORDER BY d DESC;";
		ResultSet result = db.ResultExecutedStatementQuery(query,
				DBConnection.INSTANCE.open());
		return result;
	}

	public ResultSet GetSSPDTepatWaktuByKode(String kodePajak,
			java.sql.Date varDari, java.sql.Date varSampai) {
		int bulan = UIController.INSTANCE.getMonthDiff(varDari, varSampai) + 1;
		String query = "SELECT DISTINCT ss.NPWPD, wp.Nama_Badan, sum(sp.Pajak_Terutang) / COUNT(ss.NPWPD) as d, COUNT(ss.NPWPD) as c, "
				+ "casewhen (CAST(TO_Char(sp.Masa_Pajak_Dari, 'DD') as INTEGER) > 1 OR CAST(To_Char(sp.Masa_Pajak_Sampai, 'DD') as INTEGER) < 27, "
				+ bulan
				* 2
				+ ", "
				+ bulan
				+ ") as SPTPD, wp.Alabad_Jalan "
				+ "FROM sspd ss INNER join sptpd sp on sp.idsptpd = ss.idsptpd "
				+ "INNER join wajib_pajak wp on wp.NPWPD = ss.NPWPD WHERE "
				+ "TO_DATE(Bulan_SKP, 'MM-YYYY') >= TO_DATE('"
				+ varDari
				+ "', 'YYYY-MM-DD') "
				+ "AND LAST_DAY(TO_DATE(Bulan_SKP, 'MM-YYYY')) <= LAST_DAY(TO_DATE('"
				+ varSampai
				+ "', 'YYYY-MM-DD')) "
				+ "AND ss.Denda = 0 AND ss.Denda_Lunas = TRUE AND sp.Assesment = 'Self' and wp.Status = 'Aktif' and wp.Insidentil = FALSE AND wp.Kewajiban_Pajak = '"
				+ kodePajak
				+ "' "
				+ "GROUP BY ss.NPWPD, wp.NAMA_BADAN, wp.Alabad_Jalan, SPTPD having COUNT(ss.NPWPD) >= "
				+ bulan + " ORDER BY d DESC;";
		ResultSet result = db.ResultExecutedStatementQuery(query,
				DBConnection.INSTANCE.open());
		return result;
	}

	@SuppressWarnings("deprecation")
	public boolean cekSKPDKBAward(String npwpd) {
		Timestamp dateNow = (java.sql.Timestamp) ControllerFactory
				.getMainController().getCpWajibPajakDAOImpl().getDateNow();
		String query = "Select masa_Pajak_Sampai, masa_Pajak_Dari, Tipe_SKP, Tanggal_SKP, No_SKP from periksa where NPWPD = '"
				+ npwpd + "' order by masa_Pajak_Sampai desc";

		ResultSet result = db.ResultExecutedStatementQuery(query,
				DBConnection.INSTANCE.open());

		Calendar calBegin = Calendar.getInstance();
		calBegin.set(Calendar.DAY_OF_MONTH, 1);
		calBegin.set(Calendar.MONTH, dateNow.getMonth());
		calBegin.set(Calendar.YEAR, dateNow.getYear() + 1900);
		calBegin.add(Calendar.MONTH, -10);
		Date beginDay = calBegin.getTime();

		Calendar calEnd = Calendar.getInstance();
		calEnd.set(Calendar.DAY_OF_MONTH, 1);
		calEnd.set(Calendar.MONTH, dateNow.getMonth());
		calEnd.set(Calendar.YEAR, dateNow.getYear() + 1900);
		calEnd.add(Calendar.MONTH, -4);
		calEnd.add(Calendar.DATE, -1);
		Date endDay = calEnd.getTime();
		List<Periksa> listPeriksa = new ArrayList<Periksa>();

		if (result != null) {
			try {
				while (result.next()) {
					Periksa periksaModel = new Periksa();
					periksaModel.setNoSkp(result.getString("No_SKP"));
					periksaModel.setTipeSkp(result.getString("Tipe_SKP"));
					periksaModel.setTglSkp(result.getDate("tanggal_SKP"));
					periksaModel.setMasaPajakDari(result
							.getDate("Masa_Pajak_Dari"));
					periksaModel.setMasaPajakSampai(result
							.getDate("Masa_Pajak_Sampai"));
					listPeriksa.add(periksaModel);
				}

				for (int i = 0; i < listPeriksa.size(); i++) {
					if ((listPeriksa.get(i).getTglSkp().compareTo(beginDay) > 0)
							&& (listPeriksa.get(i).getTglSkp()
									.compareTo(endDay) < 0)
							&& (!listPeriksa.get(i).getTipeSkp()
									.equalsIgnoreCase("SKPDN"))) {
						if (!cekPembayaran(npwpd, listPeriksa.get(i)
								.getTglSkp(), listPeriksa.get(i).getNoSkp()))
							return false;
					}
				}

			} catch (SQLException e) {
				MessageDialog.openError(Display.getCurrent().getActiveShell(),
						"Database Error", e.getMessage());
				return false;
			}
		}

		return true;
	}

	@SuppressWarnings("deprecation")
	public boolean cekPembayaran(String npwpd, Date tglSKP, String noSkp) {
		Timestamp dateNow = (java.sql.Timestamp) ControllerFactory
				.getMainController().getCpWajibPajakDAOImpl().getDateNow();
		Calendar cal = Calendar.getInstance();
		cal.setTime(tglSKP);
		cal.add(Calendar.MONTH, 1);
		// tglSKP = cal.getTime();
		tglSKP = new Date(cal.get(Calendar.YEAR) - 1900,
				cal.get(Calendar.MONTH), cal.get(Calendar.DATE),
				cal.getActualMaximum(Calendar.HOUR_OF_DAY),
				cal.getActualMaximum(Calendar.MINUTE),
				cal.getActualMaximum(Calendar.SECOND));
		if (tglSKP.compareTo(dateNow) > 0) {
			return true;
		} else {
			// cek Angsuran
			String sqlMohon = "Select * from mohon where NPWPD = '" + npwpd
					+ "' and No_SKP = '" + noSkp + "'";
			try {
				ResultSet result = db.ResultExecutedStatementQuery(sqlMohon,
						DBConnection.INSTANCE.open());
				if (result.next())
					return false;
			} catch (Exception e) {
				// TODO: handle exception
			}

			// cek SKPDKB
			String sqlString = "Select * from sspd where NPWPD = '" + npwpd
					+ "' and No_SKP = '" + noSkp + "'";
			try {
				ResultSet result = db.ResultExecutedStatementQuery(sqlString,
						DBConnection.INSTANCE.open());
				if (result.next()) {
					if (result.getTimestamp("tgl_SSPD").compareTo(tglSKP) > 0) {
						return false;
					}
				} else
					return false;
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		return true;
	}

	public int countAntri() {
		String sqlString = "select count(*) from sspd ss where ss.NOTAANTRIBAYAR IS NOT NULL AND (ss.NO_SSPD = '' OR ss.NO_SSPD IS NULL)";
		try {
			ResultSet result = db.ResultExecutedStatementQuery(sqlString,
					DBConnection.INSTANCE.open());
			if (result != null)
				if (result.next())
					return result.getInt("C1");
				else
					return -1;
			else
				return -1;
		} catch (Exception e) {
			return -1;
		}
	}

	public int getMaxNoReg(int tahun) {
		String query = "select casewhen (count(no_registrasi) > 0, max(no_registrasi) + 1, 1) from NO_REGISTRASI where TAHUN = "
				+ tahun;
		ResultSet result = db.ResultExecutedStatementQuery(query,
				DBConnection.INSTANCE.open());
		try {
			if (result.next()) {
				return result.getInt("C1");
			} else
				return 1;
		} catch (SQLException e) {
			e.printStackTrace();
			return 1;
		}
	}

	public String getNoReg(String noSSPD) {
		String query = "select No_Registrasi from NO_REGISTRASI where No_SSPD = '"
				+ noSSPD + "';";
		ResultSet result = db.ResultExecutedStatementQuery(query,
				DBConnection.INSTANCE.open());
		try {
			if (result.next()) {
				return result.getString("No_Registrasi");
			} else
				return "";
		} catch (SQLException e) {
			e.printStackTrace();
			return "";
		}
	}

	public int getIDRegDouble(String noSSPD) {
		String query = "select * from NO_REGISTRASI where NO_SSPD = '" + noSSPD
				+ "' order by ID desc limit 1;";
		ResultSet result = db.ResultExecutedStatementQuery(query,
				DBConnection.INSTANCE.open());
		try {
			if (result.next()) {
				return result.getInt("ID");
			} else
				return 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}

	public boolean deleteNoReg(int id) {
		String query = "DELETE FROM NO_REGISTRASI WHERE ID = " + id;
		boolean result = db.ExecuteStatementQuery(query,
				DBConnection.INSTANCE.open());
		if (!result)
			MessageDialog.openError(Display.getCurrent().getActiveShell(),
					"Database Error", "Ada masalah pada DELETE tabel SSPD");

		return result;
	}

	public boolean saveNoReg(int no_reg, String no_sspd, String keterangan,
			int tahun) {
		String sqlQuery = "insert into no_registrasi values(null, " + no_reg
				+ ", '" + no_sspd + "', '" + keterangan + "', " + tahun + ");";
		boolean result = db.ExecuteStatementQuery(sqlQuery,
				DBConnection.INSTANCE.open());
		if (!result)
			MessageDialog
					.openError(Display.getCurrent().getActiveShell(),
							"Database Error",
							"Ada masalah pada MERGE(UPDATE/INSERT) tabel no registrasi.");
		return result;
	}

	public ResultSet getNoRegPBB() {
		String query = "select * from No_Registrasi where No_SSPD = '' order by ID desc limit 30";
		ResultSet result = db.ResultExecutedStatementQuery(query,
				DBConnection.INSTANCE.open());
		return result;
	}

	public void setToDenda(int idSSPD) {
		String sqlQuery = "update sspd set isDenda = 1 where idSSPD = "
				+ idSSPD;
		boolean result = db.ExecuteStatementQuery(sqlQuery,
				DBConnection.INSTANCE.open());
		if (!result)
			MessageDialog.openError(Display.getCurrent().getActiveShell(),
					"Database Error",
					"Ada masalah pada MERGE(UPDATE/INSERT) tabel sspd.");
		// return result;

	}

	public List<Sspd> getComboAngsuran(String noSkp, String NPWPD) {
		String query = "SELECT ss.IDSSPD, ss.Cicilan_Ke, ss.Jumlah_Bayar, ss.No_SKP, ss.No_SSPD, ss.Tgl_SSPD, ss.Denda, ss.Denda_Lunas FROM sspd ss WHERE ss.No_SKP = '"
				+ noSkp
				+ "' AND "
				+ "ss.NPWPD = '"
				+ NPWPD
				+ "' AND ss.Jenis_Bayar = 2 order by ss.cicilan_Ke;";
		ResultSet result = db.ResultExecutedStatementQuery(query,
				DBConnection.INSTANCE.open());
		if (result != null) {
			try {
				List<Sspd> listSspd = new ArrayList<Sspd>();
				while (result.next()) {
					Sspd sspdModel = new Sspd();
					sspdModel.setIdSspd(result.getInt("IDSSPD"));
					sspdModel.setNoSspd(result.getString("NO_SSPD"));
					sspdModel.setTglSspd(result.getTimestamp("TGL_SSPD"));
					sspdModel.setNoSkp(result.getString("NO_SKP"));
					sspdModel.setCicilanKe(result.getInt("CICILAN_KE"));
					sspdModel.setDenda(result.getDouble("DENDA"));
					sspdModel.setJumlahBayar(result.getDouble("JUMLAH_BAYAR"));
					sspdModel.setDendaLunas(result.getBoolean("DENDA_LUNAS"));
					listSspd.add(sspdModel);
				}
				return listSspd;
			} catch (SQLException e) {
				MessageDialog.openError(Display.getCurrent().getActiveShell(),
						"Database SQL Error", e.getMessage());
				return null;
			}
		} else {
			MessageDialog.openError(Display.getCurrent().getActiveShell(),
					"Database Error",
					"Ada masalah pada pembuatan LOAD DATA tabel SSPD.");
			return null;
		}
	}

	public List<Double> getJumlahPenerimaan(String tgl) {
		double pokokPajak = 0;
		double denda = 0;
		double totalPokokPajak = 0;
		double totalDenda = 0;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		java.sql.Date dateParam = java.sql.Date.valueOf(ControllerFactory
				.getMainController().getCpWajibPajakDAOImpl().getDateNow()
				.toString().split(" ")[0]);
		List<Double> retValue = new ArrayList<Double>();
		try {
			dateParam = java.sql.Date.valueOf(tgl);
		} catch (Exception e) {
			dateParam = java.sql.Date.valueOf(ControllerFactory
					.getMainController().getCpWajibPajakDAOImpl().getDateNow()
					.toString().split(" ")[0]);
		} finally {
			String query = "select ss.NPWPD, sp.NO_SPTPD, p.NO_SKP, ss.NO_SSPD, ss.CICILAN_KE, md.NO_ANGSURAN, ss.JUMLAH_BAYAR, sp.Pajak_Terutang as SPTPD, md.Angsuran_Pokok, "
					+ "p.Total_Pajak_Periksa-p.Total_Pajak_Terutang+p.Total_Kenaikan as SKPDKB from SSPD ss left join SPTPD sp on sp.IDSPTPD = ss.IDSPTPD "
					+ "left join PERIKSA p on p.IDPERIKSA = ss.IDPERIKSA left join MOHON m on m.NO_SKP = ss.NO_SKP AND m.NPWPD = ss.NPWPD "
					+ "left join MOHON_DETAIL md on md.IDMOHON = m.IDMOHON AND md.NO_ANGSURAN = ss.CICILAN_KE where Cast(TGL_CETAK as Date) = '"
					+ sdf.format(dateParam) + "';";
			ResultSet result = db.ResultExecutedStatementQuery(query,
					DBConnection.INSTANCE.open());

			if (result != null) {
				try {
					while (result.next()) {
						if (result.getString("NO_SPTPD") != null)
							pokokPajak = result.getDouble("SPTPD");
						else {
							if (result.getString("NO_ANGSURAN") != null)
								pokokPajak = result.getDouble("ANGSURAN_POKOK");
							else
								pokokPajak = result.getDouble("SKPDKB");
						}
						denda = result.getDouble("JUMLAH_BAYAR") - pokokPajak;

						totalPokokPajak += pokokPajak;
						totalDenda += denda;
					}
					retValue.add(totalPokokPajak);
					retValue.add(totalDenda);
				} catch (SQLException e) {
					MessageDialog.openError(Display.getCurrent()
							.getActiveShell(), "Database SQL Error", e
							.getMessage());
					retValue = null;
				}
			}
		}
		return retValue;
	}

	public ResultSet getDaftarRealisasiHarian(String KodePajak,
			Date masaPajakDari, Date masaPajakSampai) {
		SimpleDateFormat formatDari = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat formatSampai = new SimpleDateFormat(
				"yyyy-MM-dd 23:59:59");
		String masaDari = formatDari.format(masaPajakDari);
		String masaSampai = formatSampai.format(masaPajakSampai);
		String query = "";
		if (masaPajakSampai.equals(masaPajakDari)) {
			query = "SELECT rg.No_Registrasi, s.NPWPD, wp.Nama_Badan, wp.Alabad_Jalan, bu.Nama_Bid_usaha, s.No_SKP, ifnull(sp.Tgl_SPTPD, pr.Tanggal_SKP) as Tanggal_SKP, s.No_SSPD, s.Tgl_Cetak, s.Bulan_SKP, s.CICILAN_KE, "
					+ "Casewhen(s.IsDenda = 0, IFNULL(sp.Pajak_Terutang, IFNULL(md.Angsuran_Pokok, (pr.Total_Pajak_Periksa - pr.Total_Pajak_Terutang + pr.Total_Kenaikan))), 0) as PokokPajak, 0 as Denda, "
					+ "s.Jumlah_Bayar, casewhen(s.No_SKP like '%SKPDKB%', pr.MASA_PAJAK_DARI, sp.MASA_PAJAK_DARI) as PajakDari, "
					+ "casewhen(s.No_SKP like '%SKPDKB%', pr.MASA_PAJAK_SAMPAI, sp.MASA_PAJAK_SAMPAI) as PajakSampai "
					+ "FROM sspd s left JOIN wajib_pajak wp ON wp.NPWPD = s.NPWPD "
					+ "left JOIN periksa pr on pr.IDPERIKSA = s.IDPERIKSA left JOIN sptpd sp on sp.IDSPTPD = s.IDSPTPD "
					+ "left join mohon m on m.NPWPD = s.NPWPD and m.No_SKP = s.No_SKP left join mohon_detail md on md.IdMohon = m.IdMohon and s.Cicilan_Ke = md.No_Angsuran "
					+ "left join Bidang_Usaha bu on bu.idSub_Pajak = s.idSub_Pajak left join No_Registrasi rg on rg.No_SSPD = s.No_SSPD WHERE s.TGL_CETAK like '"
					+ masaDari + "%'";
			if (!KodePajak.equalsIgnoreCase(""))
				query += " AND substring(s.NPWPD, 1, 1) = '" + KodePajak + "'";
			query += " ORDER BY rg.No_Registrasi";
		} else {
			query = "SELECT rg.No_Registrasi, s.NPWPD, wp.Nama_Badan, wp.Alabad_Jalan, bu.Nama_Bid_usaha, s.No_SKP, ifnull(sp.Tgl_SPTPD, pr.Tanggal_SKP) as Tanggal_SKP, s.No_SSPD, s.Tgl_Cetak, s.Bulan_SKP, s.CICILAN_KE, "
					+ "Casewhen(s.IsDenda = 0, IFNULL(sp.Pajak_Terutang, IFNULL(md.Angsuran_Pokok, (pr.Total_Pajak_Periksa - pr.Total_Pajak_Terutang + pr.Total_Kenaikan))), 0) as PokokPajak, 0 as Denda, "
					+ "s.Jumlah_Bayar, casewhen(s.No_SKP like '%SKPDKB%', pr.MASA_PAJAK_DARI, sp.MASA_PAJAK_DARI) as PajakDari, "
					+ "casewhen(s.No_SKP like '%SKPDKB%', pr.MASA_PAJAK_SAMPAI, sp.MASA_PAJAK_SAMPAI) as PajakSampai "
					+ "FROM sspd s "
					+ "left JOIN wajib_pajak wp ON wp.NPWPD = s.NPWPD "
					+ "left JOIN periksa pr on pr.IDPERIKSA = s.IDPERIKSA "
					+ "left JOIN sptpd sp on sp.IDSPTPD = s.IDSPTPD "
					+ "left join mohon m on m.NPWPD = s.NPWPD and m.No_SKP = s.No_SKP "
					+ "left join mohon_detail md on md.IdMohon = m.IdMohon and s.Cicilan_Ke = md.No_Angsuran "
					+ "left join Bidang_Usaha bu on bu.idSub_Pajak = s.idSub_Pajak left join No_Registrasi rg on rg.No_SSPD = s.No_SSPD WHERE (s.Tgl_CETAK between Date '"
					+ masaDari + "' AND Timestamp '" + masaSampai + "')";
			if (!KodePajak.equalsIgnoreCase(""))
				query += " AND substring(s.NPWPD, 1, 1) = '" + KodePajak + "'";
			query += " ORDER BY rg.No_Registrasi";
		}
		ResultSet result = db.ResultExecutedStatementQuery(query,
				DBConnection.INSTANCE.open());
		return result;
	}

	@SuppressWarnings("deprecation")
	public ResultSet getDaftarRealisasiHarianNew(String KodePajak, int idSub,
			Date masaPajakDari, Date masaPajakSampai, String caraBayar) {
		SimpleDateFormat formatDari = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat formatSampai = new SimpleDateFormat(
				"yyyy-MM-dd 23:59:59");
		String masaDari = formatDari.format(masaPajakDari);
		String masaSampai = formatSampai.format(masaPajakSampai);
		String query = "";
		if (masaPajakSampai.equals(masaPajakDari)) {
			//update
			query = "SELECT rg.No_Registrasi, s.NPWPD, wp.Nama_Badan, wp.Alabad_Jalan, bu.Nama_Bid_usaha, s.No_SKP, ifnull(sp.Tgl_SPTPD, pr.Tanggal_SKP) as Tanggal_SKP, s.No_SSPD, "
					+ "IFNULL(md.STS, IFNULL(pr.STS, sp.STS)) as STS, s.Tgl_Cetak, s.Bulan_SKP, s.CICILAN_KE, "
					+ "Casewhen(s.IsDenda = 0, IFNULL(sp.Pajak_Terutang, IFNULL(md.Angsuran_Pokok, (ceiling(pr.Total_Pajak_Periksa) - pr.Total_Pajak_Terutang + pr.Total_Kenaikan))), 0) as PokokPajak, "
					+ "casewhen(s.IsDenda=0, IFNULL((ceiling(md.Biaya_Administrasi) + ceiling(md.Denda_SKPDKB)), IFNULL((pr.Total_Pajak_Bunga + pr.Total_Denda),0)),0) as Denda, 0 as DendaSSPD, "
					+ "s.Jumlah_Bayar, casewhen(s.No_SKP like '%SKPDKB%', pr.MASA_PAJAK_DARI, sp.MASA_PAJAK_DARI) as PajakDari, "
					+ "casewhen(s.No_SKP like '%SKPDKB%', pr.MASA_PAJAK_SAMPAI, sp.MASA_PAJAK_SAMPAI) as PajakSampai "
					+ "FROM sspd s left JOIN wajib_pajak wp ON wp.NPWPD = s.NPWPD "
					+ "left JOIN periksa pr on pr.IDPERIKSA = s.IDPERIKSA left JOIN sptpd sp on sp.IDSPTPD = s.IDSPTPD "
					+ "left join mohon m on m.NPWPD = s.NPWPD and m.No_SKP = s.No_SKP left join mohon_detail md on md.IdMohon = m.IdMohon and s.Cicilan_Ke = md.No_Angsuran "
					+ "left join Bidang_Usaha bu on bu.idSub_Pajak = s.idSub_Pajak left join No_Registrasi rg on rg.No_SSPD = s.No_SSPD WHERE s.TGL_CETAK like '"
					+ masaDari + "%'";
			//before
//			query = "SELECT rg.No_Registrasi, s.NPWPD, wp.Nama_Badan, wp.Alabad_Jalan, bu.Nama_Bid_usaha, s.No_SKP, ifnull(sp.Tgl_SPTPD, pr.Tanggal_SKP) as Tanggal_SKP, s.No_SSPD, "
//					+ "IFNULL(md.STS, IFNULL(pr.STS, sp.STS)) as STS, s.Tgl_Cetak, s.Bulan_SKP, s.CICILAN_KE, "
//					+ "Casewhen(s.IsDenda = 0, IFNULL(sp.Pajak_Terutang, IFNULL(md.Angsuran_Pokok, (pr.Total_Pajak_Periksa - pr.Total_Pajak_Terutang + pr.Total_Kenaikan))), 0) as PokokPajak, "
//					+ "casewhen(s.IsDenda=0, IFNULL((ceiling(md.Biaya_Administrasi) + ceiling(md.Denda_SKPDKB)), IFNULL((pr.Total_Pajak_Bunga + pr.Total_Denda),0)),0) as Denda, 0 as DendaSSPD, "
//					+ "s.Jumlah_Bayar, casewhen(s.No_SKP like '%SKPDKB%', pr.MASA_PAJAK_DARI, sp.MASA_PAJAK_DARI) as PajakDari, "
//					+ "casewhen(s.No_SKP like '%SKPDKB%', pr.MASA_PAJAK_SAMPAI, sp.MASA_PAJAK_SAMPAI) as PajakSampai "
//					+ "FROM sspd s left JOIN wajib_pajak wp ON wp.NPWPD = s.NPWPD "
//					+ "left JOIN periksa pr on pr.IDPERIKSA = s.IDPERIKSA left JOIN sptpd sp on sp.IDSPTPD = s.IDSPTPD "
//					+ "left join mohon m on m.NPWPD = s.NPWPD and m.No_SKP = s.No_SKP left join mohon_detail md on md.IdMohon = m.IdMohon and s.Cicilan_Ke = md.No_Angsuran "
//					+ "left join Bidang_Usaha bu on bu.idSub_Pajak = s.idSub_Pajak left join No_Registrasi rg on rg.No_SSPD = s.No_SSPD WHERE s.TGL_CETAK like '"
//					+ masaDari + "%'";
			if (idSub != 0) {
				query += " AND s.IdSub_Pajak = " + idSub;
			} else {
				if (!KodePajak.equalsIgnoreCase(""))
					query += " AND substring(s.NPWPD, 1, 1) = '" + KodePajak
							+ "'";
			}
			if (caraBayar.equalsIgnoreCase("Giro"))
				query += " AND CARA_BAYAR like '%Giro%'";
			else if (caraBayar.equalsIgnoreCase("Tunai"))
				query += " AND CARA_BAYAR not like '%Giro%'";
			query += " ORDER BY rg.No_Registrasi;";
			System.out.println(query);
		} else {
//			update
			query = "SELECT rg.No_Registrasi, s.NPWPD, wp.Nama_Badan, wp.Alabad_Jalan, bu.Nama_Bid_usaha, s.No_SKP, ifnull(sp.Tgl_SPTPD, pr.Tanggal_SKP) as Tanggal_SKP, s.No_SSPD, "
					+ "IFNULL(md.STS, IFNULL(pr.STS, sp.STS)) as STS, s.Tgl_Cetak, s.Bulan_SKP, s.CICILAN_KE, "
					+ "Casewhen(s.IsDenda = 0, IFNULL(sp.Pajak_Terutang, IFNULL(md.Angsuran_Pokok, (ceiling(pr.Total_Pajak_Periksa) - pr.Total_Pajak_Terutang + pr.Total_Kenaikan))), 0) as PokokPajak, "
					+ "casewhen(s.IsDenda=0, IFNULL((md.Biaya_Administrasi + md.Denda_SKPDKB), IFNULL((pr.Total_Pajak_Bunga + pr.Total_Denda),0)),0) as Denda, 0 as DendaSSPD, "
					+ "s.Jumlah_Bayar, casewhen(s.No_SKP like '%SKPDKB%', pr.MASA_PAJAK_DARI, sp.MASA_PAJAK_DARI) as PajakDari, "
					+ "casewhen(s.No_SKP like '%SKPDKB%', pr.MASA_PAJAK_SAMPAI, sp.MASA_PAJAK_SAMPAI) as PajakSampai "
					+ "FROM sspd s "
					+ "left JOIN wajib_pajak wp ON wp.NPWPD = s.NPWPD "
					+ "left JOIN periksa pr on pr.IDPERIKSA = s.IDPERIKSA "
					+ "left JOIN sptpd sp on sp.IDSPTPD = s.IDSPTPD "
					+ "left join mohon m on m.NPWPD = s.NPWPD and m.No_SKP = s.No_SKP "
					+ "left join mohon_detail md on md.IdMohon = m.IdMohon and s.Cicilan_Ke = md.No_Angsuran "
					+ "left join Bidang_Usaha bu on bu.idSub_Pajak = s.idSub_Pajak left join No_Registrasi rg on rg.No_SSPD = s.No_SSPD WHERE (s.Tgl_CETAK between Date '"
					+ masaDari + "' AND Timestamp '" + masaSampai + "') ";
//			before
//			query = "SELECT rg.No_Registrasi, s.NPWPD, wp.Nama_Badan, wp.Alabad_Jalan, bu.Nama_Bid_usaha, s.No_SKP, ifnull(sp.Tgl_SPTPD, pr.Tanggal_SKP) as Tanggal_SKP, s.No_SSPD, "
//					+ "IFNULL(md.STS, IFNULL(pr.STS, sp.STS)) as STS, s.Tgl_Cetak, s.Bulan_SKP, s.CICILAN_KE, "
//					+ "Casewhen(s.IsDenda = 0, IFNULL(sp.Pajak_Terutang, IFNULL(md.Angsuran_Pokok, (pr.Total_Pajak_Periksa - pr.Total_Pajak_Terutang + pr.Total_Kenaikan))), 0) as PokokPajak, "
//					+ "casewhen(s.IsDenda=0, IFNULL((md.Biaya_Administrasi + md.Denda_SKPDKB), IFNULL((pr.Total_Pajak_Bunga + pr.Total_Denda),0)),0) as Denda, 0 as DendaSSPD, "
//					+ "s.Jumlah_Bayar, casewhen(s.No_SKP like '%SKPDKB%', pr.MASA_PAJAK_DARI, sp.MASA_PAJAK_DARI) as PajakDari, "
//					+ "casewhen(s.No_SKP like '%SKPDKB%', pr.MASA_PAJAK_SAMPAI, sp.MASA_PAJAK_SAMPAI) as PajakSampai "
//					+ "FROM sspd s "
//					+ "left JOIN wajib_pajak wp ON wp.NPWPD = s.NPWPD "
//					+ "left JOIN periksa pr on pr.IDPERIKSA = s.IDPERIKSA "
//					+ "left JOIN sptpd sp on sp.IDSPTPD = s.IDSPTPD "
//					+ "left join mohon m on m.NPWPD = s.NPWPD and m.No_SKP = s.No_SKP "
//					+ "left join mohon_detail md on md.IdMohon = m.IdMohon and s.Cicilan_Ke = md.No_Angsuran "
//					+ "left join Bidang_Usaha bu on bu.idSub_Pajak = s.idSub_Pajak left join No_Registrasi rg on rg.No_SSPD = s.No_SSPD WHERE (s.Tgl_CETAK between Date '"
//					+ masaDari + "' AND Timestamp '" + masaSampai + "')";
			if (idSub != 0) {
				query += " AND s.IdSub_Pajak = " + idSub;
			} else {
				if (!KodePajak.equalsIgnoreCase(""))
					query += " AND substring(s.NPWPD, 1, 1) = '" + KodePajak
							+ "'";
			}
			if (caraBayar.equalsIgnoreCase("Giro"))
				query += " AND CARA_BAYAR like '%Giro%'";
			else if (caraBayar.equalsIgnoreCase("Tunai"))
				query += " AND CARA_BAYAR not like '%Giro%'";
			query += " ORDER BY rg.No_Registrasi;";
		}
		ResultSet result = null;
		System.out.println(query);
		if (masaPajakDari.getMonth() == masaPajakSampai.getMonth())
			result = db.ResultExecutedStatementQuery(query,
					DBConnection.INSTANCE.open());
		else
			result = db.ResultExecutedStatementQuery(query,
					DBConnection.INSTANCE.openBawah());
		return result;
	}

	public ResultSet getLaporanRealisasi(String KodePajak, Date masaPajakDari,
			Date masaPajakSampai) {
		SimpleDateFormat formatDari = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat formatSampai = new SimpleDateFormat(
				"yyyy-MM-dd 23:59:59");
		String masaDari = formatDari.format(masaPajakDari);
		String masaSampai = formatSampai.format(masaPajakSampai);
		String query = "";
		query = "SELECT rg.No_Registrasi, s.NPWPD, wp.Nama_Badan, bu.Nama_Bid_Usaha, s.No_SKP, s.No_SSPD, s.Bulan_SKP, s.CICILAN_KE, s.TGL_CETAK, "
				+ "Casewhen(s.IsDenda = 0, IFNULL(sp.Pajak_Terutang, IFNULL(md.Angsuran_Pokok, (pr.Total_Pajak_Periksa - pr.Total_Pajak_Terutang + pr.Total_Kenaikan))), 0) as PokokPajak, 0 as Denda, "
				+ "s.Jumlah_Bayar FROM sspd s left JOIN wajib_pajak wp ON wp.NPWPD = s.NPWPD "
				+ "left JOIN periksa pr on pr.IDPERIKSA = s.IDPERIKSA left JOIN sptpd sp on sp.IDSPTPD = s.IDSPTPD "
				+ "left join mohon m on m.NPWPD = s.NPWPD and m.No_SKP = s.No_SKP left join mohon_detail md on md.IdMohon = m.IdMohon and s.Cicilan_Ke = md.No_Angsuran "
				+ "left join Bidang_Usaha bu on bu.idSub_Pajak = s.idSub_Pajak left join No_Registrasi rg on rg.No_SSPD = s.No_SSPD WHERE (s.Tgl_CETAK between Date '"
				+ masaDari + "' AND Timestamp '" + masaSampai + "')";
		if (!KodePajak.equalsIgnoreCase(""))
			query += " AND substring(s.NPWPD, 1, 1) = '" + KodePajak + "'";
		query += " ORDER BY rg.No_Registrasi";
		ResultSet result = db.ResultExecutedStatementQuery(query,
				DBConnection.INSTANCE.open());
		return result;
	}

	public List<LaporanRealisasi> getLaporanRealisasi2(String NamaBidangUsaha,
			Date masaPajakDari, Date masaPajakSampai) {
		SimpleDateFormat formatDari = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat formatSampai = new SimpleDateFormat(
				"yyyy-MM-dd 23:59:59");
		String masaDari = formatDari.format(masaPajakDari);
		String masaSampai = formatSampai.format(masaPajakSampai);
		String query = "";
		query = "SELECT rg.No_Registrasi, s.NPWPD, wp.Nama_Badan, bu.Nama_Bid_Usaha, s.No_SKP, s.No_SSPD, s.Bulan_SKP, s.CICILAN_KE, s.TGL_CETAK, "
				+ "Casewhen(s.IsDenda = 0, IFNULL(sp.Pajak_Terutang, IFNULL(md.Angsuran_Pokok, (pr.Total_Pajak_Periksa - pr.Total_Pajak_Terutang + pr.Total_Kenaikan))), 0) as PokokPajak, 0 as Denda, "
				+ "s.Jumlah_Bayar FROM sspd s left JOIN wajib_pajak wp ON wp.NPWPD = s.NPWPD "
				+ "left JOIN periksa pr on pr.IDPERIKSA = s.IDPERIKSA left JOIN sptpd sp on sp.IDSPTPD = s.IDSPTPD "
				+ "left join mohon m on m.NPWPD = s.NPWPD and m.No_SKP = s.No_SKP left join mohon_detail md on md.IdMohon = m.IdMohon and s.Cicilan_Ke = md.No_Angsuran "
				+ "left join Bidang_Usaha bu on bu.idSub_Pajak = s.idSub_Pajak left join No_Registrasi rg on rg.No_SSPD = s.No_SSPD WHERE (s.Tgl_CETAK between Date '"
				+ masaDari + "' AND Timestamp '" + masaSampai + "')";
		if (!NamaBidangUsaha.equalsIgnoreCase(""))
			// query += " AND substring(s.NPWPD, 1, 1) = '" + KodePajak + "'";
			query += " AND bu.Nama_Bid_Usaha = '" + NamaBidangUsaha + "'";
		query += " ORDER BY rg.No_Registrasi";
		ResultSet result = db.ResultExecutedStatementQuery(query,
				DBConnection.INSTANCE.open());
		List<LaporanRealisasi> listLr = new ArrayList<LaporanRealisasi>();
		try {
			while (result.next()) {
				LaporanRealisasi lr = new LaporanRealisasi();
				lr.setNoReg(result.getInt("No_Registrasi"));
				lr.setNpwpd(result.getString("NPWPD"));
				lr.setNamaBadan(result.getString("Nama_Badan"));
				lr.setBidUsaha(result.getString("Nama_Bid_Usaha"));
				lr.setNoSKP(result.getString("NO_SKP"));
				lr.setNoSSPD(result.getString("no_sspd"));
				lr.setBulanSKP(result.getString("bulan_skp"));
				lr.setCicilanKe(result.getInt("cicilan_ke"));
				lr.setTglBayar(result.getDate("tgl_cetak"));
				lr.setPokok(result.getDouble("pokokpajak"));
				lr.setDenda(result.getDouble("jumlah_bayar")
						- result.getDouble("pokokpajak"));
				lr.setJumlah(result.getDouble("jumlah_bayar"));
				listLr.add(lr);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return listLr;
	}

	public List<LaporanRealisasi> getLaporanRealisasiRekap(
			String NamaBidangUsaha, Date masaPajakDari, Date masaPajakSampai) {
		SimpleDateFormat formatDari = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat formatSampai = new SimpleDateFormat(
				"yyyy-MM-dd 23:59:59");
		String masaDari = formatDari.format(masaPajakDari);
		String masaSampai = formatSampai.format(masaPajakSampai);
		String query = "";
		query = "SELECT SUM(Ceiling(Casewhen(s.IsDenda = 0, IFNULL(sp.Pajak_Terutang, IFNULL(md.Angsuran_Pokok, (pr.Total_Pajak_Periksa - pr.Total_Pajak_Terutang + pr.Total_Kenaikan))), 0))) as PokokPajak, "
				+ "0 as Denda, sum(s.Jumlah_Bayar) as jumlah_bayar FROM sspd s "
				+ "left JOIN periksa pr on pr.IDPERIKSA = s.IDPERIKSA left JOIN sptpd sp on sp.IDSPTPD = s.IDSPTPD "
				+ "left join mohon m on m.NPWPD = s.NPWPD and m.No_SKP = s.No_SKP left join mohon_detail md on md.IdMohon = m.IdMohon and s.Cicilan_Ke = md.No_Angsuran "
				+ "left join Bidang_Usaha bu on bu.idSub_Pajak = s.idSub_Pajak "
				+ "WHERE (s.Tgl_CETAK between Date '"
				+ masaDari
				+ "' AND Timestamp '"
				+ masaSampai
				+ "') AND bu.Nama_Bid_Usaha = '" + NamaBidangUsaha + "'";
		// if (!NamaBidangUsaha.equalsIgnoreCase(""))
		// query += " AND substring(s.NPWPD, 1, 1) = '" + KodePajak + "'";
		// query += " AND bu.Nama_Bid_Usaha = '" + NamaBidangUsaha + "'";
		// query += " ORDER BY rg.No_Registrasi";
		ResultSet result = db.ResultExecutedStatementQuery(query,
				DBConnection.INSTANCE.openBawah());
		List<LaporanRealisasi> listLr = new ArrayList<LaporanRealisasi>();
		try {
			while (result.next()) {
				LaporanRealisasi lr = new LaporanRealisasi();
				// lr.setNoReg(result.getInt("No_Registrasi"));
				// lr.setNpwpd(result.getString("NPWPD"));
				// lr.setNamaBadan(result.getString("Nama_Badan"));
				// lr.setBidUsaha(result.getString("Nama_Bid_Usaha"));
				// lr.setNoSKP(result.getString("NO_SKP"));
				// lr.setNoSSPD(result.getString("no_sspd"));
				// lr.setBulanSKP(result.getString("bulan_skp"));
				// lr.setCicilanKe(result.getInt("cicilan_ke"));
				// lr.setTglBayar(result.getDate("tgl_cetak"));
				lr.setPokok(roundUP(result.getDouble("pokokpajak")));
				lr.setDenda(result.getDouble("jumlah_bayar")
						- roundUP(result.getDouble("pokokpajak")));
				lr.setJumlah(result.getDouble("jumlah_bayar"));
				listLr.add(lr);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return listLr;
	}

	public String loadNoNotaAntri(String noSSPD) {
		String sqlQuery = "Select NotaAntribayar from sspd where No_SSPD = '"
				+ noSSPD + "'";
		ResultSet result = db.ResultExecutedStatementQuery(sqlQuery,
				DBConnection.INSTANCE.open());
		try {
			if (result.next()) {
				return result.getString("NotaAntriBayar");
			} else
				return "";
		} catch (SQLException e) {
			e.printStackTrace();
			return "";
		}
	}

	public int isDenda(String noSSPD) {
		int retValue = 0;
		String sqlQuery = "select isDenda from SSPD where No_SSPD = '" + noSSPD
				+ "'";
		ResultSet result = db.ResultExecutedStatementQuery(sqlQuery,
				DBConnection.INSTANCE.open());
		if (result != null)
			try {
				if (result.next())
					retValue = result.getInt("isDenda");
				else
					retValue = 0;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		else
			retValue = 0;

		return retValue;
	}

	public Double getKurangBayarDenda(String noAntri) {
		Double retValue = 0.0;
		String query = "select Jumlah_Bayar from SSPD where NotaAntriBayar = '"
				+ noAntri + "'";
		ResultSet result = db.ResultExecutedStatementQuery(query,
				DBConnection.INSTANCE.open());
		if (result != null)
			try {
				if (result.next())
					retValue = result.getDouble("Jumlah_Bayar");
				else
					retValue = 0.0;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		else
			retValue = 0.0;

		return retValue;
	}

	public List<Sspd> getDatabySTS(String sts) {
		String query = "SELECT ss.*, wp.NAMA_BADAN, wp.NAMA_PEMILIK, wp.ALABAD_JALAN, wp.NPWPD as WP, sp.Masa_Pajak_Dari, sp.Masa_Pajak_Sampai, sp.STS, sp.No_SPTPD as SKP "
				+ "FROM SPTPD sp LEFT JOIN SSPD ss on sp.IDSPTPD = ss.IDSPTPD INNER JOIN WAJIB_PAJAK wp on wp.NPWPD = sp.NPWPD where sp.STS like '%"
				+ sts
				+ "' or sp.sts like '%"
				+ sts
				+ "-%' union "
				+ "SELECT ss.*, wp.NAMA_BADAN, wp.NAMA_PEMILIK, wp.ALABAD_JALAN, wp.NPWPD as WP, p.Masa_Pajak_Dari, p.Masa_Pajak_Sampai, p.STS, p.No_SKP as SKP FROM Periksa p LEFT JOIN SSPD ss on p.IDPERIKSA = ss.IDPERIKSA "
				+ "LEFT JOIN WAJIB_PAJAK wp on wp.NPWPD = p.NPWPD where p.STS like '%"
				+ sts
				+ "' or p.sts like '%"
				+ sts
				+ "-%' union "
				+ "SELECT ss.*, wp.NAMA_BADAN, wp.NAMA_PEMILIK, wp.ALABAD_JALAN, wp.NPWPD as WP, p.Masa_Pajak_Dari, p.Masa_Pajak_Sampai, md.STS, p.No_SKP as SKP FROM Periksa p LEFT JOIN SSPD ss on p.IDPERIKSA = ss.IDPERIKSA "
				+ "LEFT JOIN WAJIB_PAJAK wp on wp.NPWPD = p.NPWPD INNER JOIN MOHON m on m.NO_SKP = p.NO_SKP and m.NPWPD = p.NPWPD INNER JOIN MOHON_DETAIL md on md.IDMOHON = m.IDMOHON "
				+ "where md.STS like '%"
				+ sts
				+ "' or md.sts like '%"
				+ sts
				+ "-%';";
		ResultSet result = db.ResultExecutedStatementQuery(query,
				DBConnection.INSTANCE.open());
		if (result != null) {
			try {
				List<Sspd> listSspd = new ArrayList<Sspd>();
				while (result.next()) {
					Sspd sspdModel = new Sspd();
					sspdModel.setIdSspd(result.getInt("IDSSPD"));
					sspdModel.setNoSspd(result.getString("NO_SSPD"));
					sspdModel.setTglSspd(result.getTimestamp("TGL_SSPD"));
					sspdModel.setNpwpd(result.getString("WP"));
					sspdModel.setMasaPajakDari(result
							.getDate("Masa_Pajak_Dari"));
					sspdModel.setMasaPajakSampai(result
							.getDate("Masa_Pajak_Sampai"));
					sspdModel.setBulanSkp(result.getString("BULAN_SKP"));
					sspdModel.setNoSkp(result.getString("SKP"));
					sspdModel.setCicilanKe(result.getInt("CICILAN_KE"));
					sspdModel.setDenda(result.getDouble("DENDA"));
					sspdModel.setJumlahBayar(result.getDouble("JUMLAH_BAYAR"));
					sspdModel.setJenisBayar(result.getInt("JENIS_BAYAR"));
					sspdModel.setCaraBayar(result.getString("CARA_BAYAR"));
					sspdModel
							.setNamaPenyetor(result.getString("NAMA_PENYETOR"));
					sspdModel.setLunas(result.getBoolean("LUNAS"));
					sspdModel.setIdSubPajak(result.getInt("IDSUB_PAJAK"));
					sspdModel.setDendaLunas(result.getBoolean("DENDA_LUNAS"));
					sspdModel.setTglCetak(result.getTimestamp("TGL_CETAK"));
					sspdModel.setNotaBayar(result.getString("NOTAANTRIBAYAR"));
					sspdModel.setNamaBadan(result.getString("NAMA_BADAN"));
					sspdModel.setNamaPemilik(result.getString("NAMA_PEMILIK"));
					sspdModel.setAlabadJalan(result.getString("ALABAD_JALAN"));
					listSspd.add(sspdModel);
				}
				return listSspd;
			} catch (SQLException e) {
				MessageDialog.openError(Display.getCurrent().getActiveShell(),
						"Database SQL Error", e.getMessage());
				return null;
			}
		} else {
			MessageDialog.openError(Display.getCurrent().getActiveShell(),
					"Database Error",
					"Ada masalah pada pembuatan LOAD DATA tabel SSPD.");
			return null;
		}
	}

	@SuppressWarnings("deprecation")
	public Double[] GetRealisasiBerjalan(String kodePajak, Date masaPajakDari,
			Date masaPajakSampai) throws SQLException {
		Double retValue[] = new Double[6];
		Calendar calBegin = Calendar.getInstance();
		calBegin.set(Calendar.DAY_OF_MONTH, 1);
		calBegin.set(Calendar.MONTH, masaPajakDari.getMonth());
		calBegin.set(Calendar.YEAR, masaPajakDari.getYear() + 1900);
		calBegin.add(Calendar.MONTH, -1);
		Date beginDay = calBegin.getTime();
		SimpleDateFormat formatDari = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat formatSampai = new SimpleDateFormat(
				"yyyy-MM-dd 23:59:59");
		String masaSPT = formatDari.format(beginDay);
		String masaDari = formatDari.format(masaPajakDari);
		String masaSampai = formatSampai.format(masaPajakSampai);

		String query1 = "select sum(casewhen(ss.ISDENDA=0,sp.Pajak_Terutang,0)) as pokok, sum(ss.JUMLAH_BAYAR) - sum(casewhen(ss.ISDENDA=0,sp.Pajak_Terutang,0)) as denda, sum(ss.JUMLAH_BAYAR) as jumlah "
				+ "from SSPD ss inner join SPTPD sp on sp.IDSPTPD = ss.IDSPTPD where (ss.TGL_CETAK between Date '"
				+ masaDari
				+ "' AND Timestamp '"
				+ masaSampai
				+ "') "
				+ "and sp.MASA_PAJAK_DARI >= '"
				+ masaSPT
				+ "' and substring(ss.NPWPD, 1, 1) = " + kodePajak + ";";
		ResultSet result = null;
		if (masaPajakDari.getMonth() == masaPajakSampai.getMonth())
			result = db.ResultExecutedStatementQuery(query1,
					DBConnection.INSTANCE.open());
		else
			result = db.ResultExecutedStatementQuery(query1,
					DBConnection.INSTANCE.openBawah());
		if (result.next()) {
			retValue[0] = result.getDouble("pokok");
			retValue[1] = result.getDouble("jumlah");
		}

		String query2 = "select sum(casewhen(ss.ISDENDA=0,sp.Pajak_Terutang,0)) as pokok, sum(ss.JUMLAH_BAYAR) - sum(casewhen(ss.ISDENDA=0,sp.Pajak_Terutang,0)) as denda, sum(ss.JUMLAH_BAYAR) as jumlah "
				+ "from SSPD ss inner join SPTPD sp on sp.IDSPTPD = ss.IDSPTPD where (ss.TGL_CETAK between Date '"
				+ masaDari
				+ "' AND Timestamp '"
				+ masaSampai
				+ "') "
				+ "and sp.MASA_PAJAK_DARI < '"
				+ masaSPT
				+ "' and substring(ss.NPWPD, 1, 1) = " + kodePajak + ";";
		ResultSet result2 = null;
		if (masaPajakDari.getMonth() == masaPajakSampai.getMonth())
			result2 = db.ResultExecutedStatementQuery(query2,
					DBConnection.INSTANCE.open());
		else
			result2 = db.ResultExecutedStatementQuery(query2,
					DBConnection.INSTANCE.openBawah());
		if (result2.next()) {
			retValue[2] = result2.getDouble("pokok");
			retValue[3] = result2.getDouble("jumlah");
		}

		String query3 = "select sum(casewhen(ss.ISDENDA=0, IFNULL(md.Angsuran_Pokok, (pr.Total_Pajak_Periksa - pr.Total_Pajak_Terutang + pr.Total_Kenaikan)), 0)) as pokok, "
				+ "sum(ss.JUMLAH_BAYAR) - sum(casewhen(ss.ISDENDA=0, IFNULL(md.Angsuran_Pokok, (pr.Total_Pajak_Periksa - pr.Total_Pajak_Terutang + pr.Total_Kenaikan)), 0)) as denda, sum(ss.JUMLAH_BAYAR) as jumlah "
				+ "from SSPD ss left JOIN periksa pr on pr.IDPERIKSA = ss.IDPERIKSA left join mohon m on m.NPWPD = ss.NPWPD and m.No_SKP = ss.No_SKP "
				+ "left join mohon_detail md on md.IdMohon = m.IdMohon and ss.Cicilan_Ke = md.No_Angsuran where (TGL_CETAK between Date '"
				+ masaDari
				+ "' AND Timestamp '"
				+ masaSampai
				+ "') "
				+ "and NO_SKP like '%/SKPDKB%' and substring(NPWPD, 1, 1) = "
				+ kodePajak + ";";
		ResultSet result3 = null;
		if (masaPajakDari.getMonth() == masaPajakSampai.getMonth())
			result3 = db.ResultExecutedStatementQuery(query3,
					DBConnection.INSTANCE.open());
		else
			result3 = db.ResultExecutedStatementQuery(query3,
					DBConnection.INSTANCE.openBawah());
		if (result3.next()) {
			retValue[4] = roundUP(result3.getDouble("pokok"));
			retValue[5] = roundUP(result3.getDouble("jumlah"));
		}

		return retValue;
	}

	private static double round(double value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();

		DecimalFormat df = new DecimalFormat("#.##");
		df.setMaximumFractionDigits(2);
		df.setRoundingMode(RoundingMode.HALF_EVEN);
		try {
			return df.parse(df.format(value)).doubleValue();
		} catch (ParseException e) {
			e.printStackTrace();
			return 0.0;
		}
	}

	private static double roundUP(double value) {
		DecimalFormat df = new DecimalFormat("#.##");
		df.setMaximumFractionDigits(0);
		df.setRoundingMode(RoundingMode.UP);
		try {
			return df.parse(df.format(value)).doubleValue();
		} catch (ParseException e) {
			e.printStackTrace();
			return 0.0;
		}
	}

	public java.sql.Date getDispensasiTempo(String noSKP) {
		java.sql.Date retValue = null;
		String query = "Select jatuh_tempo from dispensasi_tempo where no_skp = '"
				+ noSKP + "'";
		ResultSet result = db.ResultExecutedStatementQuery(query,
				DBConnection.INSTANCE.open());
		if (result != null) {
			try {
				if (result.next()) {
					retValue = result.getDate("jatuh_tempo");
				}
			} catch (Exception ex) {

			}
		}
		return retValue;
	}

	public boolean hapusSTS(String noSPTPD, String noSTS) {
		// TODO Auto-generated method stub
		String query = "UPDATE SPTPD SET STS = '' WHERE NO_SPTPD = '"+noSPTPD+"' AND STS = '"+noSTS+"'";
		boolean result = db.ExecuteStatementQuery(query, DBConnection.INSTANCE.open());
		if(!result)
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada (UPDATE) tabel SPTPD.");
		return result;
	}
}
