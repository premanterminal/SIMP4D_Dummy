package com.dispenda.dao;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;

import com.dispenda.model.Periksa;
import com.dispenda.model.PeriksaDetail;
import com.dispenda.controller.UIController;
import com.dispenda.database.DBConnection;
import com.dispenda.database.DBOperation;

public class CPPeriksaDAOImpl {
	private DBOperation db = new DBOperation();
	
	public boolean savePeriksa(Periksa periksaModel){
		String query = "MERGE INTO PERIKSA USING (VALUES(" +
				""+periksaModel.getIdPeriksa()+"," +
				"'"+periksaModel.getNoPeriksa()+"'," +
				"'"+periksaModel.getTglPeriksa()+"'," +
				"'"+periksaModel.getNpwpd()+"'," +
				"'"+periksaModel.getJenisPeriksa()+"'," +
				""+periksaModel.getKenaikanPersen()+"," +
				""+periksaModel.getKenaikan()+"," +
				"'"+periksaModel.getMasaPajakDari()+"'," +
				"'"+periksaModel.getMasaPajakSampai()+"'," +
				"'"+periksaModel.getNoSkp()+"'," +
				"'"+periksaModel.getTglSkp()+"'," +
				"'"+periksaModel.getNoNppd()+"'," +
				"'"+periksaModel.getTglNppd()+"'," +
				""+periksaModel.getIdSubPajak()+"," +
				"'"+periksaModel.getTipeSkp()+"'," +
				""+periksaModel.getTotalPajakPeriksa()+"," +
				""+periksaModel.getTotalPajakTerutang()+"," +
				""+periksaModel.getTotalPajakBunga()+"," +
				""+periksaModel.getTotalKenaikan()+"," +
				""+periksaModel.getTotalDenda()+"," +
				"'"+periksaModel.getKeterangan()+"'," +
//				"'"+periksaModel.getNoSkplama()+"'," +
				"0))" +
			" AS vals(" +
				"id," +
				"no," +
				"tgl," +
				"npwpd," +
				"jenis," +
				"kenaikan_persen," +
				"kenaikan," +
				"masadari," +
				"masasampai," +
				"no_skp," +
				"tgl_skp," +
				"no_nppd," +
				"tgl_nppd," +
				"id_sub_pajak," +
				"tipe_skp," +
				"tot_pajak_periksa," +
				"tot_pajak_terutang," +
				"tot_pajak_bunga," +
				"tot_kenaikan," +
				"tot_denda," +
				"ket," +
//				"noskplama," +
				"status)" +
			" ON PERIKSA.IDPERIKSA = vals.id" +
			" WHEN MATCHED THEN UPDATE SET " +
				"PERIKSA.NO_PERIKSA = vals.no," +
				"PERIKSA.TANGGAL_PERIKSA = vals.tgl," +
				"PERIKSA.NPWPD = vals.npwpd," +
				"PERIKSA.JENIS_PERIKSA = vals.jenis," +
				"PERIKSA.KENAIKAN_PERSEN = vals.kenaikan_persen," +
				"PERIKSA.KENAIKAN = vals.kenaikan," +
				"PERIKSA.MASA_PAJAK_DARI = vals.masadari," +
				"PERIKSA.MASA_PAJAK_SAMPAI = vals.masasampai," +
				"PERIKSA.NO_SKP = vals.no_skp," +
				"PERIKSA.TANGGAL_SKP = vals.tgl_skp," +
				"PERIKSA.NO_NPPD = vals.no_nppd," +
				"PERIKSA.TANGGAL_NPPD = vals.tgl_nppd," +
				"PERIKSA.IDSUB_PAJAK = vals.id_sub_pajak," +
				"PERIKSA.TIPE_SKP = vals.tipe_skp," +
				"PERIKSA.TOTAL_PAJAK_PERIKSA = vals.tot_pajak_periksa," +
				"PERIKSA.TOTAL_PAJAK_TERUTANG = vals.tot_pajak_terutang," +
				"PERIKSA.TOTAL_PAJAK_BUNGA = vals.tot_pajak_bunga," +
				"PERIKSA.TOTAL_KENAIKAN = vals.tot_kenaikan," +
				"PERIKSA.TOTAL_DENDA = vals.tot_denda," +
				"PERIKSA.KETERANGAN = vals.ket," +
//				"PERIKSA.NO_SKPLAMA = vals.noskplama," +
				"PERIKSA.STATUS = vals.status" +
			" WHEN NOT MATCHED THEN INSERT (IDPERIKSA, NO_PERIKSA, TANGGAL_PERIKSA, NPWPD, JENIS_PERIKSA, KENAIKAN_PERSEN, KENAIKAN, MASA_PAJAK_DARI, MASA_PAJAK_SAMPAI, " +
			"NO_SKP, TANGGAL_SKP, NO_NPPD, TANGGAL_NPPD, IDSUB_PAJAK, TIPE_SKP, TOTAL_PAJAK_PERIKSA, TOTAL_PAJAK_TERUTANG, TOTAL_PAJAK_BUNGA, TOTAL_KENAIKAN, TOTAL_DENDA, " +
			"KETERANGAN, STATUS) VALUES " +
				"vals.id," +
				"vals.no," +
				"vals.tgl," +
				"vals.npwpd," +
				"vals.jenis," +
				"vals.kenaikan_persen," +
				"vals.kenaikan," +
				"vals.masadari," +
				"vals.masasampai," +
				"vals.no_skp," +
				"vals.tgl_skp," +
				"vals.no_nppd," +
				"vals.tgl_nppd," +
				"vals.id_sub_pajak," +
				"vals.tipe_skp," +
				"vals.tot_pajak_periksa," +
				"vals.tot_pajak_terutang," +
				"vals.tot_pajak_bunga," +
				"vals.tot_kenaikan," +
				"vals.tot_denda," +
				"vals.ket," +
//				"vals.noskplama," +
				"vals.status";
		boolean result = db.ExecuteStatementQuery(query, DBConnection.INSTANCE.open());
		if(!result)
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada MERGE(UPDATE/INSERT) tabel PERIKSA.");
		return result;
	}
	
	public List<Periksa> getPeriksa(String npwpd){
		String query = "SELECT * FROM PERIKSA WHERE NPWPD = "+npwpd;
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		if(result != null){
			try{
				List<Periksa> listPeriksa = new ArrayList<Periksa>();
				while(result.next()){
					Periksa periksaModel = new Periksa();
					periksaModel.setIdPeriksa(result.getInt("IDPERIKSA"));
					periksaModel.setNoPeriksa(result.getString("NO_PERIKSA"));
					periksaModel.setTglPeriksa(result.getDate("TANGGAL_PERIKSA"));
					periksaModel.setNpwpd(result.getString("NPWPD"));
					periksaModel.setJenisPeriksa(result.getString("JENIS_PERIKSA"));
					periksaModel.setKenaikanPersen(result.getInt("KENAIKAN_PERSEN"));
					periksaModel.setKenaikan(result.getDouble("KENAIKAN"));
					periksaModel.setMasaPajakDari(result.getDate("MASA_PAJAK_DARI"));
					periksaModel.setMasaPajakSampai(result.getDate("MASA_PAJAK_SAMPAI"));
					periksaModel.setNoSkp(result.getString("NO_SKP"));
					periksaModel.setTglSkp(result.getDate("TANGGAL_SKP"));
					periksaModel.setNoNppd(result.getString("NO_NPPD"));
					periksaModel.setTglNppd(result.getDate("TANGGAL_NPD"));
					periksaModel.setIdSubPajak(result.getInt("IDSUB_PAJAK"));
					periksaModel.setTipeSkp(result.getString("TIPE_SKP"));
					periksaModel.setTotalPajakPeriksa(result.getDouble("TOTAL_PAJAK_PERIKSA"));
					periksaModel.setTotalPajakTerutang(result.getDouble("TOTAL_PAJAK_TERUTANG"));
					periksaModel.setTotalPajakBunga(result.getDouble("TOTAL_PAJAK_BUNGA"));
					periksaModel.setTotalKenaikan(result.getDouble("TOTAL_KENAIKAN"));
					periksaModel.setTotalDenda(result.getDouble("TOTAL_DENDA"));
					periksaModel.setKeterangan(result.getString("KETERANGAN"));
					listPeriksa.add(periksaModel);
				}
				return listPeriksa;
			}
			catch(SQLException e){
				MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", e.getMessage());
				return null;
			}
		}
		else{
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada pembuatan LOAD DATA tabel PERIKSA.");
			return null;
		}
	}
	
	public List<Periksa> getAllPeriksa(){
		String query = "SELECT * FROM PERIKSA";
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		if(result != null){
			try{
				List<Periksa> listPeriksa = new ArrayList<Periksa>();
				while(result.next()){
					Periksa periksaModel = new Periksa();
					periksaModel.setIdPeriksa(result.getInt("IDPERIKSA"));
					periksaModel.setNoPeriksa(result.getString("NO_PERIKSA"));
					periksaModel.setTglPeriksa(result.getDate("TANGGAL_PERIKSA"));
					periksaModel.setNpwpd(result.getString("NPWPD"));
					periksaModel.setJenisPeriksa(result.getString("JENIS_PERIKSA"));
					periksaModel.setKenaikanPersen(result.getInt("KENAIKAN_PERSEN"));
					periksaModel.setKenaikan(result.getDouble("KENAIKAN"));
					periksaModel.setMasaPajakDari(result.getDate("MASA_PAJAK_DARI"));
					periksaModel.setMasaPajakSampai(result.getDate("MASA_PAJAK_SAMPAI"));
					periksaModel.setNoSkp(result.getString("NO_SKP"));
					periksaModel.setTglSkp(result.getDate("TANGGAL_SKP"));
					periksaModel.setNoNppd(result.getString("NO_NPPD"));
					periksaModel.setTglNppd(result.getDate("TANGGAL_NPD"));
					periksaModel.setIdSubPajak(result.getInt("IDSUB_PAJAK"));
					periksaModel.setTipeSkp(result.getString("TIPE_SKP"));
					periksaModel.setTotalPajakPeriksa(result.getDouble("TOTAL_PAJAK_PERIKSA"));
					periksaModel.setTotalPajakTerutang(result.getDouble("TOTAL_PAJAK_TERUTANG"));
					periksaModel.setTotalPajakBunga(result.getDouble("TOTAL_PAJAK_BUNGA"));
					periksaModel.setTotalKenaikan(result.getDouble("TOTAL_KENAIKAN"));
					periksaModel.setTotalDenda(result.getDouble("TOTAL_DENDA"));
					periksaModel.setKeterangan(result.getString("KETERANGAN"));
					listPeriksa.add(periksaModel);
				}
				return listPeriksa;
			}
			catch(SQLException e){
				MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", e.getMessage());
				return null;
			}
		}
		else{
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada pembuatan LOAD DATA tabel PERIKSA.");
			return null;
		}
	}
	
	public boolean deletePeriksa(Integer idPeriksa){
		String query = "DELETE FROM PERIKSA WHERE IDPERIKSA = "+idPeriksa;
		boolean result = db.ExecuteStatementQuery(query, DBConnection.INSTANCE.open());
		if(!result)
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada DELETE tabel PERIKSA dengan IDPERIKSA = "+idPeriksa);
		
		return result;
	}
	
	public String getNoSKP(String tahun, Integer IDSubPajak, String tipeSKP)
	{
		Integer KodePajak = 0;
		String query = "select IDPajak from BIDANG_USAHA where IDSUB_PAJAK = " + IDSubPajak;
		String QuerySPT = "";
		try{
			ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
			if(result.next())
			{
				KodePajak = result.getInt("IDPajak");
				if (tipeSKP.equalsIgnoreCase("SKPDN"))
				{
					if (KodePajak == 5){
						QuerySPT = "SELECT casewhen (count(No_SKP) > 0, CONCAT(MAX(CAST(SUBSTRING(NO_SKP, 1, LENGTH(NO_SKP) - 11) AS INTEGER)) + 1, '/SKPDN/" + tahun + "'), " +
					 "'1/SKPDN/" + tahun + "') as SKP from Periksa where Year(tanggal_SKP) = '" + tahun + "' AND tipe_skp = '"+ tipeSKP + "' " +
					 "AND IDSUB_PAJAK = " + IDSubPajak +" AND NO_SKP != '';";
					}
					else
					{
						QuerySPT = "SELECT casewhen (count(No_SKP) > 0, CONCAT(MAX(CAST(SUBSTRING(NO_SKP, 1, LENGTH(NO_SKP) - 11) AS INTEGER)) + 1, '/SKPDN/" + tahun + "'), " +
								 "'1/SKPDN/" + tahun + "') as SKP from Periksa where Year(tanggal_SKP) = '" + tahun + "' AND tipe_skp = '"+ tipeSKP + "' " +
								"AND SUBSTRING(NPWPD,1,1) = " + KodePajak +" AND NO_SKP != '';";	
					}
				}
				else if (tipeSKP.equalsIgnoreCase("SKPDKB"))
				{
					if (KodePajak == 5){
						QuerySPT = "SELECT casewhen (count(No_SKP) > 0, CONCAT(MAX(CAST(SUBSTRING(NO_SKP, 1, LENGTH(NO_SKP) - 12) AS INTEGER)) + 1, '/SKPDKB/" + tahun + "'), " +
					 "'1/SKPDKB/" + tahun + "') as SKP from Periksa where Year(tanggal_SKP) = '" + tahun + "' AND tipe_skp = '"+ tipeSKP + "' " +
					 "AND IDSUB_PAJAK = " + IDSubPajak +" AND NO_SKP != '';";
					}
					else
					{
						QuerySPT = "SELECT casewhen (count(No_SKP) > 0, CONCAT(MAX(CAST(SUBSTRING(NO_SKP, 1, LENGTH(NO_SKP) - 12) AS INTEGER)) + 1, '/SKPDKB/" + tahun + "'), " +
								 "'1/SKPDKB/" + tahun + "') as SKP from Periksa where Year(tanggal_SKP) = '" + tahun + "' AND tipe_skp = '"+ tipeSKP + "' " +
								"AND SUBSTRING(NPWPD,1,1) = " + KodePajak +" AND NO_SKP != '';";	
					}
				}
				else if (tipeSKP.equalsIgnoreCase("SKPDKBT"))
				{
					if (KodePajak == 5){
						QuerySPT = "SELECT casewhen (count(No_SKP) > 0, CONCAT(MAX(CAST(SUBSTRING(NO_SKP, 1, LENGTH(NO_SKP) - 13) AS INTEGER)) + 1, '/SKPDKBT/" + tahun + "'), " +
					 "'1/SKPDKBT/" + tahun + "') as SKP from Periksa where Year(tanggal_SKP) = '" + tahun + "' AND tipe_skp = '"+ tipeSKP + "' " +
					 "AND IDSUB_PAJAK = " + IDSubPajak +" AND NO_SKP != '';";
					}
					else
					{
						QuerySPT = "SELECT casewhen (count(No_SKP) > 0, CONCAT(MAX(CAST(SUBSTRING(NO_SKP, 1, LENGTH(NO_SKP) - 13) AS INTEGER)) + 1, '/SKPDKBT/" + tahun + "'), " +
								 "'1/SKPDKBT/" + tahun + "') as SKP from Periksa where Year(tanggal_SKP) = '" + tahun + "' AND tipe_skp = '"+ tipeSKP + "' " +
								"AND SUBSTRING(NPWPD,1,1) = " + KodePajak +" AND NO_SKP != '';";	
					}
				}
				ResultSet resultSPT = db.ResultExecutedStatementQuery(QuerySPT, DBConnection.INSTANCE.open());
				if(resultSPT.next())
					return resultSPT.getString("SKP");	
			}
			else
				return "";
		}
		catch(SQLException e){
			e.printStackTrace();
			return "";
		}
		return "";
		
	}
	
	public String getNoSKPBaru(String tahun, Integer IDSubPajak, String tipeSKP)
	{
		if (IDSubPajak == 50)
			IDSubPajak = 20;
		String bidUsaha = "";
		String query = "select Kode_Bid_Usaha from BIDANG_USAHA where IDSUB_PAJAK = " + IDSubPajak;
		String QuerySPT = "";
		try{
			ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
			if(result.next())
			{
				bidUsaha = result.getString("Kode_Bid_Usaha").replace(".","").substring(3);
				if (tipeSKP.equalsIgnoreCase("SKPDN"))
				{
					QuerySPT = "SELECT casewhen (count(No_SKP) > 0, CONCAT(MAX(CAST(SUBSTRING(NO_SKP, 1, LENGTH(NO_SKP) - 16) AS INTEGER)) + 1, '/" + bidUsaha + "/SKPDN/" + tahun + "'), " +
					 "'1/" + bidUsaha + "/SKPDN/" + tahun + "') as SKP from Periksa where Year(tanggal_SKP) = '" + tahun + "' AND tipe_skp = '"+ tipeSKP + "' " +
					 "AND IDSUB_PAJAK = " + IDSubPajak +" AND NO_SKP != '';";
				}
				else if (tipeSKP.equalsIgnoreCase("SKPDKB"))
				{
					QuerySPT = "SELECT casewhen (count(No_SKP) > 0, CONCAT(MAX(CAST(SUBSTRING(NO_SKP, 1, LENGTH(NO_SKP) - 17) AS INTEGER)) + 1, '/" + bidUsaha + "/SKPDKB/" + tahun + "'), " +
					 "'1/" + bidUsaha + "/SKPDKB/" + tahun + "') as SKP from Periksa where Year(tanggal_SKP) = '" + tahun + "' AND tipe_skp = '"+ tipeSKP + "' " +
					 "AND IDSUB_PAJAK = " + IDSubPajak +" AND NO_SKP != '';";
				}
				else if (tipeSKP.equalsIgnoreCase("SKPDKBT"))
				{
					QuerySPT = "SELECT casewhen (count(No_SKP) > 0, CONCAT(MAX(CAST(SUBSTRING(NO_SKP, 1, LENGTH(NO_SKP) - 18) AS INTEGER)) + 1, '/" + bidUsaha + "/SKPDKBT/" + tahun + "'), " +
					 "'1/" + bidUsaha + "/SKPDKBT/" + tahun + "') as SKP from Periksa where Year(tanggal_SKP) = '" + tahun + "' AND tipe_skp = '"+ tipeSKP + "' " +
					 "AND IDSUB_PAJAK = " + IDSubPajak +" AND NO_SKP != '';";
				}
				ResultSet resultSPT = db.ResultExecutedStatementQuery(QuerySPT, DBConnection.INSTANCE.open());
				if(resultSPT.next())
					return resultSPT.getString("SKP");
			}
			else
				return "";
		}
		catch(SQLException e){
			e.printStackTrace();
			return "";
		}
		return "";
	}
	
	public String getNoNPPD(String tahun, Integer IDSubPajak, String tipeSKP)
	{
		Integer KodePajak = 0;
		String query = "select IDPajak from BIDANG_USAHA where IDSUB_PAJAK = " + IDSubPajak;
		String QuerySPT = "";
		try{
			ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
			if(result.next())
			{
				KodePajak = result.getInt("IDPajak");
					if (KodePajak == 5){
						QuerySPT = "SELECT casewhen (count(No_NPPD) > 0, CONCAT(MAX(CAST(SUBSTRING(NO_NPPD, 1, LENGTH(NO_NPPD) - 10) AS INTEGER)) + 1, '/NPPD/" + tahun + "'), " +
					 "'1/NPPD/" + tahun + "') as NPPD from Periksa where Year(tanggal_NPPD) = '" + tahun + "' AND tipe_skp = '"+ tipeSKP + "' " +
					 "AND IDSUB_PAJAK = " + IDSubPajak +" AND NO_NPPD != '';";
					}
					else
					{
						QuerySPT = "SELECT casewhen (count(No_NPPD) > 0, CONCAT(MAX(CAST(SUBSTRING(NO_NPPD, 1, LENGTH(NO_NPPD) - 10) AS INTEGER)) + 1, '/NPPD/" + tahun + "'), " +
								 "'1/NPPD/" + tahun + "') as NPPD from Periksa where Year(tanggal_NPPD) = '" + tahun + "' AND tipe_skp = '"+ tipeSKP + "' " +
								"AND SUBSTRING(NPWPD,1,1) = " + KodePajak +" AND NO_NPPD != '';";	
					}
				ResultSet resultSPT = db.ResultExecutedStatementQuery(QuerySPT, DBConnection.INSTANCE.open());
				if(resultSPT.next())
					return resultSPT.getString("NPPD");	
			}
			else
				return "";
		}
		catch(SQLException e){
			e.printStackTrace();
			return "";
		}
		return "";
		
	}	
	
	public Integer getLastIdPeriksa(String npwpd){
		String query = "SELECT TOP 1 IDPERIKSA FROM PERIKSA WHERE NPWPD = '" + npwpd + "' ORDER BY IDPERIKSA DESC";
		try{
			ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
			if(result.next()){
				return result.getInt("IDPERIKSA");
			}
			else
				return 0;
		}
		catch(SQLException e){
			e.printStackTrace();
			return 0;
		}
	}
	
	public Integer GetIdPeriksa(String noSKP, String NPWPD)
	{
		String sqlString = "Select IdPeriksa from periksa where NPWPD = '" + NPWPD + "' and No_SKP = '" + noSKP + "'";
		try{
			ResultSet result = db.ResultExecutedStatementQuery(sqlString, DBConnection.INSTANCE.open());
			if (result.next()){
				return result.getInt("IdPeriksa");
			}
			else
				return 0;
		}
		catch(SQLException e){
			e.printStackTrace();
			return 0;
		}
	}
	
	public double GetPajakSKPDKB(String noSKP, String NPWPD)
	{
		double retValue = 0.0;
		String sqlString = "SELECT round(SUM(pd.Pajak_Periksa)- (SELECT IFNULL(SUM(casewhen (spt.Denda_Tambahan = 120000, (casewhen (ss.Denda_Lunas = true, " +
		"COALESCE(ss.Jumlah_Bayar, 0) - (COALESCE(ss.Denda, 0) + 120000), COALESCE(ss.Jumlah_Bayar, 0) - 120000)), "+
		"(casewhen (ss.Denda_Lunas = 1, COALESCE(ss.Jumlah_Bayar, 0) - COALESCE(ss.Denda, 0), COALESCE(ss.Jumlah_Bayar, 0))))), 0) as PokokPajak  " +
		"FROM sptpd spt LEFT JOIN sspd ss ON ss.NPWPD = spt.NPWPD AND ss.No_SKP = spt.No_SPTPD " +
		"WHERE spt.NPWPD = '" + NPWPD + "' AND To_Char(spt.Masa_Pajak_Dari,'MM-YYYY') = pd.Bulan_Periksa), 2) AS PokokPajak FROM periksa p " +
		"INNER JOIN periksa_detail pd ON pd.IdPeriksa = p.IdPeriksa WHERE p.NPWPD = '" + NPWPD + "' AND p.No_SKP = '" + noSKP +"' group by pd.BULAN_PERIKSA;";
		try{
			ResultSet result = db.ResultExecutedStatementQuery(sqlString, DBConnection.INSTANCE.open());
			while (result.next()){
				retValue += result.getDouble("PokokPajak");
			}
		}
		catch(SQLException e){
			e.printStackTrace();
			return 0;
		}
		
		return retValue;
	}
	
	public double GetPajakSKPDKBMohon(String noSKP, String NPWPD)
	{
		double retValue = 0.0;
		String sqlString = "SELECT (pd.Pajak_Periksa-(SELECT IFNULL(SUM(casewhen (spt.Denda_Tambahan = 120000, (casewhen (ss.Denda_Lunas = true, COALESCE(ss.Jumlah_Bayar, 0) - " +
		"COALESCE(ss.Denda, 0) + 120000, COALESCE(ss.Jumlah_Bayar, 0)) - 120000), (casewhen (ss.isdenda = 0, casewhen (ss.Denda_Lunas = 1, COALESCE(ss.Jumlah_Bayar, 0) - COALESCE(ss.Denda, 0), " +
		"COALESCE(ss.Jumlah_Bayar, 0)), COALESCE(ss.Jumlah_Bayar, 0))))), 0) as PokokPajak FROM sptpd spt LEFT JOIN sspd ss ON ss.NPWPD = spt.NPWPD AND ss.No_SKP = spt.No_SPTPD " +
		"WHERE spt.NPWPD = '" + NPWPD + "' AND To_Char(spt.Masa_Pajak_Dari,'MM-YYYY') = pd.Bulan_Periksa)) AS PokokPajak FROM periksa p " +
		"INNER JOIN periksa_detail pd ON pd.IdPeriksa = p.IdPeriksa WHERE p.NPWPD = '" + NPWPD + "' AND p.No_SKP = '" + noSKP +"';";
		try{
			ResultSet result = db.ResultExecutedStatementQuery(sqlString, DBConnection.INSTANCE.open());
			while (result.next()){
				retValue += result.getDouble("PokokPajak");
			}
		}
		catch(SQLException e){
			e.printStackTrace();
			return 0;
		}
		
		return retValue;
	}
	
	public List<PeriksaDetail> GetPajakSKPDKBT(Integer idPeriksa, String NPWPD)
	{
		String sqlString = "SELECT pd.IdPeriksa_Detail,pd.Dasar_Pengenaan, pd.Pengenaan_Periksa, pd.Selisih_Kurang, pd.Kenaikan, pd.Total_Periksa "+
							"FROM periksa_detail2 pd " +
							"WHERE pd.NPWPD = '" + NPWPD + "' and pd.idperiksa = " + idPeriksa +";";
		ResultSet result = db.ResultExecutedStatementQuery(sqlString, DBConnection.INSTANCE.open());
		if (result != null){
			try{
				List<PeriksaDetail> listPD = new ArrayList<PeriksaDetail>();
				while (result.next()){
					PeriksaDetail pdModel = new PeriksaDetail();
					pdModel.setSelisihKurang(result.getDouble("Selisih_Kurang"));
					pdModel.setKenaikanPajak(result.getDouble("Kenaikan"));
					listPD.add(pdModel);
				}
				return listPD;
			}
			catch(SQLException e){
				e.printStackTrace();
				return null;
			}
		}
		else
			return null;
	}
	
	public double GetDendaSKPDKB(String noSKP, String NPWPD)
	{
		double retValue = 0.0;
		String sqlString = "SELECT SUM(cast (pd.Bunga as decimal(16,2))) AS Denda FROM periksa p INNER JOIN periksa_detail pd ON pd.IdPeriksa = p.IdPeriksa " +
		"WHERE p.NPWPD = '" + NPWPD + "' AND p.No_SKP = '" + noSKP + "';";
		
		try{
			ResultSet result = db.ResultExecutedStatementQuery(sqlString, DBConnection.INSTANCE.open());
			while (result.next()){
				retValue += result.getDouble("Denda");
			}
		}
		catch(SQLException e){
			e.printStackTrace();
			return 0;
		}
		
		return retValue;
	}
	
	public double GetTotalDenda(String noSKP, String NPWPD)
	{
		double retValue = 0.0;
		String sqlString = "Select Total_Denda from periksa where NPWPD = '" + NPWPD +"' and No_SKP = '" + noSKP +"';";
		
		try{
			ResultSet result = db.ResultExecutedStatementQuery(sqlString, DBConnection.INSTANCE.open());
			while (result.next()){
				retValue += result.getDouble("Total_Denda");
			}
		}
		catch(SQLException e){
			e.printStackTrace();
			return 0;
		}
		
		return retValue;
	}
	
	public double GetKenaikanSKPDKB(String noSKP, String NPWPD)
	{
		double retValue = 0.0;
		String sqlString = "SELECT Total_Kenaikan FROM periksa WHERE NPWPD = '" + NPWPD +"' AND No_SKP = '" + noSKP + "';";
		
		try{
			ResultSet result = db.ResultExecutedStatementQuery(sqlString, DBConnection.INSTANCE.open());
			if (result.next()){
				retValue = result.getDouble("Total_Kenaikan");
			}
		}
		catch(SQLException e){
			e.printStackTrace();
			return 0;
		}
		
		return retValue;
	}
	
	public double GetDendaTerlambatSKPDKB(String noSKP, String NPWPD)
	{
		double retValue = 0.0;
		String sqlString = "SELECT sum(md.Denda_SKPDKB) as denda FROM mohon m LEFT JOIN mohon_detail md ON m.IdMohon = md.IdMohon " +
							"WHERE NPWPD = '" + NPWPD +"' AND No_SKP = '" + noSKP + "';";
		
		try{
			ResultSet result = db.ResultExecutedStatementQuery(sqlString, DBConnection.INSTANCE.open());
			if (result.next()){
				retValue = result.getDouble("Denda");
			}
		}
		catch(SQLException e){
			e.printStackTrace();
			return 0;
		}
		
		return retValue;
	}
	
	public String GetKeteranganSKPDKB(String noSKP, String NPWPD)
	{
		String retValue = "";
		String sqlString = "Select Keterangan from periksa WHERE NPWPD = '" + NPWPD +"' AND No_SKP = '" + noSKP + "';";
		
		try{
			ResultSet result = db.ResultExecutedStatementQuery(sqlString, DBConnection.INSTANCE.open());
			if (result.next()){
				retValue = result.getString("Keterangan");
			}
		}
		catch(SQLException e){
			e.printStackTrace();
			retValue = "";
		}
		
		return retValue;
	}
	
	public List<Periksa> checkSKPDKB(String NPWPD)
	{
		String sqlString = "Select masa_Pajak_Sampai, masa_Pajak_Dari, Tipe_SKP from periksa where NPWPD = '" + NPWPD + "' order by masa_Pajak_Sampai desc;";
		
		ResultSet result = db.ResultExecutedStatementQuery(sqlString, DBConnection.INSTANCE.open());
		if (result != null){
			try{
				List<Periksa> listPeriksa = new ArrayList<Periksa>();
				while(result.next()){
					Periksa periksaModel = new Periksa();
					periksaModel.setMasaPajakDari(result.getDate("masa_pajak_dari"));
					periksaModel.setMasaPajakSampai(result.getDate("masa_pajak_sampai"));
					periksaModel.setTipeSkp(result.getString("Tipe_SKP"));
					listPeriksa.add(periksaModel);
				}
				return listPeriksa;
			}
			catch(SQLException e){
				MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", e.getMessage());
				return null;
			}
		}
		else{
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada pembuatan LOAD DATA tabel PERIKSA.");
			return null;
		}
	}
	
	public List<Periksa> GetComboPeriksa1(String NPWPD)
	{
		String sqlString = "SELECT IdPeriksa, CONCAT(Jenis_Periksa, ' ', MonthName(Masa_Pajak_Dari), ' ', year(Masa_Pajak_Dari), ' - ', " +
				"MonthName(Masa_Pajak_Sampai), ' ', To_Char(Masa_Pajak_Sampai, 'YYYY')) AS MasaPajak " +
				"FROM periksa WHERE NPWPD = '" + NPWPD + "' AND Jenis_Periksa = 'Pemeriksaan 1' order by MASA_PAJAK_DARI desc";
		
		ResultSet result = db.ResultExecutedStatementQuery(sqlString, DBConnection.INSTANCE.open());
		if (result != null){
			try{
				List<Periksa> listPeriksa = new ArrayList<Periksa>();
				while(result.next()){
					Periksa periksaModel = new Periksa();
					periksaModel.setIdPeriksa(result.getInt("IDPeriksa"));
					periksaModel.setMasaPajak(result.getString("MasaPajak"));
					listPeriksa.add(periksaModel);
				}
				return listPeriksa;
			}
			catch(SQLException e){
				MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", e.getMessage());
				return null;
			}
		}
		else{
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada pembuatan LOAD DATA tabel PERIKSA.");
			return null;
		}
	}
	
	public List<Periksa> GetComboPeriksa2(String NPWPD)
	{
		String sqlString = "SELECT IdPeriksa, CONCAT(Jenis_Periksa, ' ', MonthName(Masa_Pajak_Dari), ' ', year(Masa_Pajak_Dari), ' - ', " +
				"MonthName(Masa_Pajak_Sampai), ' ', To_Char(Masa_Pajak_Sampai, 'YYYY')) AS MasaPajak " +
				"FROM periksa WHERE NPWPD = '" + NPWPD + "' AND Jenis_Periksa = 'Pemeriksaan 2' order by MASA_PAJAK_DARI desc";
		
		ResultSet result = db.ResultExecutedStatementQuery(sqlString, DBConnection.INSTANCE.open());
		if (result != null){
			try{
				List<Periksa> listPeriksa = new ArrayList<Periksa>();
				while(result.next()){
					Periksa periksaModel = new Periksa();
					periksaModel.setIdPeriksa(result.getInt("IDPeriksa"));
					periksaModel.setMasaPajak(result.getString("MasaPajak"));
					listPeriksa.add(periksaModel);
				}
				return listPeriksa;
			}
			catch(SQLException e){
				MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", e.getMessage());
				return null;
			}
		}
		else{
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada pembuatan LOAD DATA tabel PERIKSA.");
			return null;
		}
	}
	
	public List<Periksa> GetComboPeriksaBPK(String NPWPD)
	{
		String sqlString = "SELECT IdPeriksa, CONCAT(Jenis_Periksa, ' ', MonthName(Masa_Pajak_Dari), ' ', year(Masa_Pajak_Dari), ' - ', " +
				"MonthName(Masa_Pajak_Sampai), ' ', To_Char(Masa_Pajak_Sampai, 'YYYY')) AS MasaPajak " +
				"FROM periksa WHERE NPWPD = '" + NPWPD + "' AND Jenis_Periksa = 'Pemeriksaan 1' AND Keterangan like '%Pemeriksaan BPK%' order by MASA_PAJAK_DARI desc";
		
		ResultSet result = db.ResultExecutedStatementQuery(sqlString, DBConnection.INSTANCE.open());
		if (result != null){
			try{
				List<Periksa> listPeriksa = new ArrayList<Periksa>();
				while(result.next()){
					Periksa periksaModel = new Periksa();
					periksaModel.setIdPeriksa(result.getInt("IDPeriksa"));
					periksaModel.setMasaPajak(result.getString("MasaPajak"));
					listPeriksa.add(periksaModel);
				}
				return listPeriksa;
			}
			catch(SQLException e){
				MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", e.getMessage());
				return null;
			}
		}
		else{
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada pembuatan LOAD DATA tabel PERIKSA.");
			return null;
		}
	}

	public int getKenaikanPeriksa1(String NPWPD)
	{
		int retValue = 0;
		String sqlString = "SELECT bu.Kenaikan_1 FROM wajib_pajak wp INNER JOIN bidang_usaha bu ON bu.idsub_pajak = wp.idsub_pajak WHERE wp.NPWPD = '" + NPWPD + "'";
		
		try{
			ResultSet result = db.ResultExecutedStatementQuery(sqlString, DBConnection.INSTANCE.open());
			if (result.next()){
				retValue = result.getInt("Kenaikan_1");
			}
		}
		catch(SQLException e){
			e.printStackTrace();
			retValue = 0;
		}
		
		return retValue;
	}
	
	public int getKenaikanPeriksa2(String NPWPD)
	{
		int retValue = 0;
		String sqlString = "SELECT bu.Kenaikan_2 FROM wajib_pajak wp INNER JOIN bidang_usaha bu ON bu.idsub_pajak = wp.idsub_pajak WHERE wp.NPWPD = '" + NPWPD + "'";
		
		try{
			ResultSet result = db.ResultExecutedStatementQuery(sqlString, DBConnection.INSTANCE.open());
			if (result.next()){
				retValue = result.getInt("Kenaikan_2");
			}
		}
		catch(SQLException e){
			e.printStackTrace();
			retValue = 0;
		}
		
		return retValue;
	}
	
	public Periksa GetPeriksa1Data(int idPeriksa)
	{
		String sqlString = "SELECT p.Kenaikan_Persen, p.Kenaikan, p.Masa_Pajak_Dari, p.Masa_Pajak_Sampai, p.Tanggal_Periksa, p.Tanggal_SKP, " +
						"p.No_Periksa, p.No_SKP, p.No_NPPD, p.Keterangan FROM periksa p WHERE p.IdPeriksa = " + idPeriksa;
		
		ResultSet result = db.ResultExecutedStatementQuery(sqlString, DBConnection.INSTANCE.open());
		Periksa periksaModel = new Periksa();
		if (result != null){
			try{
				//List<Periksa> listPeriksa = new ArrayList<Periksa>();
				while(result.next()){
					
					periksaModel.setKenaikanPersen(result.getInt("Kenaikan_Persen"));
					periksaModel.setKenaikan(result.getDouble("Kenaikan"));
					periksaModel.setMasaPajakDari(result.getDate("Masa_Pajak_Dari"));
					periksaModel.setMasaPajakSampai(result.getDate("Masa_Pajak_Sampai"));
					periksaModel.setTglPeriksa(result.getDate("Tanggal_Periksa"));
					periksaModel.setTglSkp(result.getDate("Tanggal_SKP"));
					periksaModel.setNoPeriksa(result.getString("No_Periksa"));
					periksaModel.setNoSkp(result.getString("No_SKP"));
					periksaModel.setKeterangan(result.getString("Keterangan"));
//					periksaModel.setIdPeriksa(result.getInt("IDPeriksa"));
//					periksaModel.setMasaPajak(result.getString("MasaPajak"));
					//listPeriksa.add(periksaModel);
				}
				return periksaModel;
			}
			catch(SQLException e){
				MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", e.getMessage());
				return null;
			}
		}
		else{
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada pembuatan LOAD DATA tabel PERIKSA.");
			return null;
		}		
	}
	
	public List<HashMap<String, Object>> GetSPTPDPeriksa1Table(String NPWPD, Date masaPajakDari, Date masaPajakSampai)
	{
		String sqlString = "SELECT min(spt.No_SPTPD), To_Char(spt.Masa_Pajak_Dari, 'MM-YY') AS Bulan, MIN(spt.Masa_Pajak_Dari) as Masa_Pajak_Dari, spt.IdSub_Pajak, " +
				"Cast(MIN(LAST_DAY(DATE_ADD(spt.Masa_Pajak_Dari, INTERVAL 1 MONTH))) as Date) AS TanggalJatuhTempo, MIN(spt.Tgl_SPTPD) AS TanggalTransaksi, " +
				"1 AS EditKenaikan, CASE CONCAT(MonthName(spt.Masa_Pajak_Dari), ' ', To_Char(spt.Masa_Pajak_Dari, 'YYYY')) " +
				"WHEN CONCAT(MonthName(spt.Masa_Pajak_Sampai), ' ', To_Char(spt.Masa_Pajak_Sampai, 'YYYY')) " +
				"THEN CONCAT(MonthName(spt.Masa_Pajak_Dari), ' ', To_Char(spt.Masa_Pajak_Dari, 'YYYY')) " +
				"ELSE CONCAT(MonthName(spt.Masa_Pajak_Dari), ' ', To_Char(spt.Masa_Pajak_Dari, 'YYYY'), ' - ', MonthName(spt.Masa_Pajak_Sampai), ' ', To_Char(spt.Masa_Pajak_Sampai, 'YYYY')) " +
				"END AS MasaPajak, MAX(spt.Dasar_Pengenaan) AS DasarPengenaan, spt.Tarif_Pajak, SUM(spt.Pajak_Terutang) AS PajakTerutang, " +
				"case when SUM(ss.Jumlah_Bayar) + 1 > SUM(spt.Pajak_Terutang) then SUM(spt.PAJAK_TERUTANG) else SUM(ss.Jumlah_Bayar) end case as JumlahBayar, " +
				"case when SUM(ss.Jumlah_Bayar) - SUM(spt.Pajak_Terutang) > 0 then 0 else SUM(spt.Denda_Tambahan) end case as Denda_Tambahan, " +
				"SUM(ss.Denda) AS Denda FROM sptpd spt LEFT JOIN sspd ss ON ss.NPWPD = spt.NPWPD AND ss.No_SKP = spt.No_SPTPD AND ss.isDenda = False " + 
				"WHERE spt.NPWPD = '" + NPWPD + "' AND spt.Masa_Pajak_Dari >= '" + masaPajakDari + "' AND spt.Masa_Pajak_Sampai <= '" + masaPajakSampai + "' " + 
				"GROUP BY Bulan, spt.IdSub_Pajak, MasaPajak, spt.Tarif_Pajak ORDER BY Masa_Pajak_Dari ASC";
		
		List<HashMap<String, Object>> listPeriksa = new ArrayList<HashMap<String, Object>>();
		
		try {
			ResultSet result = db.ResultExecutedStatementQuery(sqlString, DBConnection.INSTANCE.open());
			
			while (result.next())
			{
				HashMap<String, Object> map = new HashMap<String, Object>();
				ResultSetMetaData md = result.getMetaData();
				for (int j=1;j<=md.getColumnCount();j++)
				{	
					int tipe = md.getColumnType(j);
					if (tipe == Types.VARCHAR)
					{
						map.put(md.getColumnName(j), result.getString(j));
					}
					else if (tipe == Types.INTEGER)
					{
						map.put(md.getColumnName(j), result.getInt(j));
					}
					else if (tipe == Types.DOUBLE)
					{
						map.put(md.getColumnName(j), result.getDouble(j));
					}
					else if (tipe == Types.DATE)
					{
						map.put(md.getColumnName(j), result.getDate(j));
					}
				}
				listPeriksa.add(map);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return listPeriksa;
	}
	
	public List<HashMap<String, Object>> GetSPTPDPeriksa1TableParkir(String NPWPD, Date masaPajakDari, Date masaPajakSampai)
	{
		String sqlString = "SELECT spt.No_SPTPD, Min(To_Char(spt.Masa_Pajak_Dari, 'MM-YY')) AS Bulan, MIN(spt.Masa_Pajak_Dari) as Masa_Pajak_Dari, spt.IdSub_Pajak, " +
				"Cast(MIN(LAST_DAY(DATE_ADD(spt.Masa_Pajak_Dari, INTERVAL 1 MONTH))) as Date) AS TanggalJatuhTempo, MIN(spt.Tgl_SPTPD) AS TanggalTransaksi, " +
				"1 AS EditKenaikan, CASE CONCAT(MonthName(spt.Masa_Pajak_Dari), ' ', To_Char(spt.Masa_Pajak_Dari, 'YYYY')) " +
				"WHEN CONCAT(MonthName(spt.Masa_Pajak_Sampai), ' ', To_Char(spt.Masa_Pajak_Sampai, 'YYYY')) " +
				"THEN CONCAT(MonthName(spt.Masa_Pajak_Dari), ' ', To_Char(spt.Masa_Pajak_Dari, 'YYYY')) " +
				"ELSE CONCAT(MonthName(spt.Masa_Pajak_Dari), ' ', To_Char(spt.Masa_Pajak_Dari, 'YYYY'), ' - ', MonthName(spt.Masa_Pajak_Sampai), ' ', To_Char(spt.Masa_Pajak_Sampai, 'YYYY')) " +
				"END AS MasaPajak, SUM(spt.Dasar_Pengenaan) AS DasarPengenaan, spt.Tarif_Pajak, SUM(spt.Pajak_Terutang) AS PajakTerutang, " +
				"case when SUM(ss.Jumlah_Bayar) + 1 > SUM(spt.Pajak_Terutang) then SUM(spt.PAJAK_TERUTANG) else SUM(ss.Jumlah_Bayar) end case as JumlahBayar, " +
				"case when SUM(ss.Jumlah_Bayar) - SUM(spt.Pajak_Terutang) > 0 then 0 else SUM(spt.Denda_Tambahan) end case as Denda_Tambahan, " +
				"SUM(ss.Denda) AS Denda FROM sptpd spt LEFT JOIN sspd ss ON ss.NPWPD = spt.NPWPD AND ss.No_SKP = spt.No_SPTPD " + 
				"WHERE spt.NPWPD = '" + NPWPD + "' AND spt.Masa_Pajak_Dari >= '" + masaPajakDari + "' AND spt.Masa_Pajak_Sampai <= '" + masaPajakSampai + "' " + 
				"GROUP BY spt.No_SPTPD, spt.IdSub_Pajak, MasaPajak, spt.Tarif_Pajak ORDER BY Masa_Pajak_Dari ASC";
		
		List<HashMap<String, Object>> listPeriksa = new ArrayList<HashMap<String, Object>>();
		
		try {
			ResultSet result = db.ResultExecutedStatementQuery(sqlString, DBConnection.INSTANCE.open());
			
			while (result.next())
			{
				HashMap<String, Object> map = new HashMap<String, Object>();
				ResultSetMetaData md = result.getMetaData();
				for (int j=1;j<=md.getColumnCount();j++)
				{	
					int tipe = md.getColumnType(j);
					if (tipe == Types.VARCHAR)
					{
						map.put(md.getColumnName(j), result.getString(j));
					}
					else if (tipe == Types.INTEGER)
					{
						map.put(md.getColumnName(j), result.getInt(j));
					}
					else if (tipe == Types.DOUBLE)
					{
						map.put(md.getColumnName(j), result.getDouble(j));
					}
					else if (tipe == Types.DATE)
					{
						map.put(md.getColumnName(j), result.getDate(j));
					}
				}
				listPeriksa.add(map);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return listPeriksa;
	}
	
	public List<HashMap<String, Object>> GetDetailPeriksa1Table(String NPWPD, Date masaPajakDari, Date masaPajakSampai)
	{
		String sqlString = "SELECT IdPeriksa_Detail, CAST(CONCAT(SUBSTRING(Bulan_Periksa,4,7), '-', SUBSTRING(Bulan_Periksa,0,3), '-01') AS DATE) as Bulan, " +
					"Pengenaan_Periksa, Pajak_Periksa, Bunga_Persen, Bunga, Kenaikan_Pajak FROM periksa_detail " +
					"WHERE NPWPD = '" + NPWPD + "' and CAST(CONCAT(SUBSTRING(Bulan_Periksa,4,7), '-', SUBSTRING(Bulan_Periksa,0,3), '-01') AS DATE) >= '" + masaPajakDari + "' " +
					"AND CAST(CONCAT(SUBSTRING(Bulan_Periksa,4,7), '-', SUBSTRING(Bulan_Periksa,0,3), '-01') AS DATE) <= '" + masaPajakSampai + "' "+
					"GROUP BY IdPeriksa_Detail, Pengenaan_Periksa, Pajak_Periksa, Bunga_Persen, Bunga, Kenaikan_Pajak ORDER BY Bulan ASC";
		
		List<HashMap<String, Object>> listDetail = new ArrayList<HashMap<String, Object>>();
		
		try {
			ResultSet result = db.ResultExecutedStatementQuery(sqlString, DBConnection.INSTANCE.open());
			
			while (result.next())
			{
				HashMap<String, Object> map = new HashMap<String, Object>();
				ResultSetMetaData md = result.getMetaData();
				for (int j=1;j<=md.getColumnCount();j++)
				{	
					int tipe = md.getColumnType(j);
					if (tipe == Types.VARCHAR)
					{
						map.put(md.getColumnName(j), result.getString(j));
					}
					else if (tipe == Types.INTEGER)
					{
						map.put(md.getColumnName(j), result.getInt(j));
					}
					else if (tipe == Types.DOUBLE)
					{
						map.put(md.getColumnName(j), result.getDouble(j));
					}
					else if (tipe == Types.DATE)
					{
						map.put(md.getColumnName(j), result.getDate(j));
					}
				}
				listDetail.add(map);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return listDetail;
	}
	
	public List<HashMap<String, Object>> GetDetailPeriksa2Table(String NPWPD, Date masaPajakDari, Date masaPajakSampai)
	{
		String sqlString = "SELECT IdPeriksa_Detail, CAST(CONCAT(SUBSTRING(Bulan_Periksa,4,7), '-', SUBSTRING(Bulan_Periksa,0,3), '-01') AS DATE) as Bulan, " +
						"Dasar_Pengenaan, Pengenaan_Periksa, Selisih_Kurang, Kenaikan, Total_Periksa " +
						"FROM periksa_detail2 WHERE NPWPD = '" + NPWPD + "' and " +
						"CAST(CONCAT(SUBSTRING(Bulan_Periksa,4,7), '-', SUBSTRING(Bulan_Periksa,0,3), '-01') AS DATE) >= '" + masaPajakDari + "' AND " +
						"CAST(CONCAT(SUBSTRING(Bulan_Periksa,4,7), '-', SUBSTRING(Bulan_Periksa,0,3), '-01') AS DATE) <= '" + masaPajakSampai + "' " +
						"GROUP BY Bulan, IdPeriksa_Detail ORDER BY Bulan ASC;";
		
		List<HashMap<String, Object>> listDetail = new ArrayList<HashMap<String, Object>>();
		
		try {
			ResultSet result = db.ResultExecutedStatementQuery(sqlString, DBConnection.INSTANCE.open());
			
			while (result.next())
			{
				HashMap<String, Object> map = new HashMap<String, Object>();
				ResultSetMetaData md = result.getMetaData();
				for (int j=1;j<=md.getColumnCount();j++)
				{	
					int tipe = md.getColumnType(j);
					if (tipe == Types.VARCHAR)
					{
						map.put(md.getColumnName(j), result.getString(j));
					}
					else if (tipe == Types.INTEGER)
					{
						map.put(md.getColumnName(j), result.getInt(j));
					}
					else if (tipe == Types.DOUBLE)
					{
						map.put(md.getColumnName(j), result.getDouble(j));
					}
					else if (tipe == Types.DATE)
					{
						map.put(md.getColumnName(j), result.getDate(j));
					}
				}
				listDetail.add(map);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return listDetail;
	}
	
	public List<Periksa> GetDaftarLHP()
	{
		String sqlString = "select idPeriksa, idsub_pajak, Tanggal_Periksa, Tanggal_SKP, NPWPD, tipe_skp, No_Periksa, No_SKP, Total_Pajak_Periksa, Total_Pajak_Terutang, Total_Pajak_Bunga, Total_Kenaikan, " +
						"Masa_Pajak_Dari, Masa_Pajak_Sampai from PERIKSA where Status = 0;";
		
		ResultSet result = db.ResultExecutedStatementQuery(sqlString, DBConnection.INSTANCE.open());
		
		if (result != null){
			try{
				List<Periksa> listPeriksa = new ArrayList<Periksa>();
				while(result.next()){
					Periksa periksaModel = new Periksa();
					periksaModel.setIdPeriksa(result.getInt("IDPeriksa"));
					periksaModel.setIdSubPajak(result.getInt("idsub_pajak"));
					periksaModel.setTglPeriksa(result.getDate("Tanggal_Periksa"));
					periksaModel.setTglSkp(result.getDate("Tanggal_SKP"));
					periksaModel.setNpwpd(result.getString("NPWPD"));
					periksaModel.setTipeSkp(result.getString("Tipe_SKP"));
					periksaModel.setNoPeriksa(result.getString("No_Periksa"));
					periksaModel.setNoSkp(result.getString("No_SKP"));
					periksaModel.setTotalPajakPeriksa(result.getDouble("Total_Pajak_Periksa"));
					periksaModel.setTotalPajakTerutang(result.getDouble("Total_Pajak_Terutang"));
					periksaModel.setTotalPajakBunga(result.getDouble("Total_Pajak_Bunga"));
					periksaModel.setTotalKenaikan(result.getDouble("Total_Kenaikan"));
					periksaModel.setMasaPajakDari(result.getDate("Masa_Pajak_Dari"));
					periksaModel.setMasaPajakSampai(result.getDate("Masa_Pajak_Sampai"));
					listPeriksa.add(periksaModel);
				}
				return listPeriksa;
			}
			catch(SQLException e){
				MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", e.getMessage());
				return null;
			}
		}
		else{
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada pembuatan LOAD DATA tabel PERIKSA.");
			return null;
		}		
	}
	
	public boolean updateTerimaLHP(String NoSKP, String NoNPPD, Integer idPeriksa, String tgl){
		String sqlString = "update periksa set No_SKP = '"+ NoSKP + "', No_NPPD = '" + NoNPPD + "', Tanggal_SKP =  '" + tgl + "', Tanggal_NPPD = '" + tgl + "', Status = 1 " +
				"where idperiksa = " + idPeriksa; 
		boolean result = db.ExecuteStatementQuery(sqlString, DBConnection.INSTANCE.open());
		if(!result)
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada DELETE tabel PERIKSA dengan IDPERIKSA = "+idPeriksa);
		
		return result;
	}
	
	public boolean RollBackUpdateTerimaLHP(Integer idPeriksa){
		String sqlString = "update periksa set No_SKP = '', No_NPPD = '', Status = 0 " +
				"where idperiksa = " + idPeriksa; 
		boolean result = db.ExecuteStatementQuery(sqlString, DBConnection.INSTANCE.open());
		if(!result)
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada DELETE tabel PERIKSA dengan IDPERIKSA = "+idPeriksa);
		
		return result;
	}
	
	public String GetNamaPajak(String kodePajak)
	{
		String retValue = "";
		String sqlString = "SELECT Nama_Pajak FROM Pajak WHERE Kode_Pajak = " + kodePajak;
		
		try{
			ResultSet result = db.ResultExecutedStatementQuery(sqlString, DBConnection.INSTANCE.open());
			if (result.next()){
				retValue = result.getString("Nama_Pajak");
			}
		}
		catch(SQLException e){
			e.printStackTrace();
			retValue = "";
		}
		
		return retValue;
	}
	
	public Integer getLastIdSKPDLB(){
		String query = "SELECT TOP 1 IDSKPDLB FROM SKPDLB ORDER BY IDSKPDLB DESC";
		try{
			ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
			if(result.next()){
				return result.getInt("IDSKPDLB");
			}
			else
				return 0;
		}
		catch(SQLException e){
			e.printStackTrace();
			return 0;
		}
	}
	
	public String getLastNoUrutSubPajak(String kodeBU){
		String query = "SELECT TOP 1 NOSKPDLB FROM SKPDLB WHERE NOSKPDLB like '%"+kodeBU+"%' ORDER BY IDSKPDLB DESC;";
		try{
			ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
			if(result.next()){
				return result.getString("NOSKPDLB");
			}
			else
				return "";
		}
		catch(SQLException e){
			e.printStackTrace();
			return "";
		}
	}
	
	@SuppressWarnings("deprecation")
	public String getMasaPajakSKPDKB(String npwpd, String NoSKP){
		String retValue = "";
		String sqlString = "Select Masa_Pajak_Dari, Masa_Pajak_Sampai from Periksa where NPWPD = '" + npwpd + "' and No_SKP = '" + NoSKP +"'";
		String masaDari, masaSampai;
		try{
			ResultSet result = db.ResultExecutedStatementQuery(sqlString, DBConnection.INSTANCE.open());
			if (result.next()){
				Date pajakDari = result.getDate("Masa_Pajak_Dari");
				Date pajakSampai = result.getDate("Masa_Pajak_Sampai");
				masaDari = UIController.INSTANCE.formatMonth(pajakDari.getMonth() + 1, Locale.getDefault()) + " " + (pajakDari.getYear() + 1900);
				masaSampai = UIController.INSTANCE.formatMonth(pajakSampai.getMonth() + 1, Locale.getDefault()) + " " + (pajakSampai.getYear() + 1900);
				retValue = masaDari + "-" + masaSampai;
			}
		}
		catch(SQLException e){
			e.printStackTrace();
			retValue = "";
		}
		
		return retValue;
	}
	
	public ResultSet getRekomendasiPeriksa(String kodePajak, String varOrder){
		String sqlQuery = "";
		if (varOrder.equalsIgnoreCase("ASC")){
			sqlQuery = "select wp.NPWPD, min(wp.Nama_Badan) as Nama_Badan, min(wp.Alabad_Jalan) as Alabad_Jalan, min(wp.Nama_Pemilik) as Nama_Pemilik, " +
					"To_Char(max(p.Masa_Pajak_Dari), 'MMMM YYYY') as MasaPajakDari, To_Char(max(p.Masa_Pajak_Sampai), 'MMMM YYYY') as MasaPajakSampai, " +
					"avg(p.Total_Pajak_Periksa) as PajakPeriksa, To_Char(max(wp.Tanggal_Daftar), 'MMMM YYYY') as BulanDaftar, max(wp.Tanggal_Daftar), wp.status, " +
					"To_Char(max(sp.Masa_Pajak_Sampai), 'MM-YYYY') as spt from wajib_pajak wp LEFT JOIN periksa p on p.NPWPD = wp.NPWPD " +
					"LEFT JOIN SPTPD sp on sp.NPWPD = wp.NPWPD WHERE wp.Status = 'Aktif' and wp.Insidentil = 0 " +
					"and wp.Jenis_Assesment = 'Self' and wp.Kewajiban_Pajak <> 8 and wp.Kewajiban_Pajak like '" + kodePajak + "' group by wp.NPWPD " +
					"having DATEDIFF('Month', max(p.Masa_Pajak_Sampai), curdate()) > 3 union " +
					"SELECT wp.NPWPD, min(wp.Nama_Badan) as Nama_Badan, min(wp.Alabad_Jalan) as Alabad_Jalan, min(wp.Nama_Pemilik) as Nama_Pemilik, " +
					"'-' as MasaPajakDari, '-' as MasaPajakSampai, 0 as PajakPeriksa, "+
					"To_Char(max(wp.Tanggal_Daftar), 'MMMM YYYY') as BulanDaftar, max(wp.Tanggal_Daftar), wp.status, To_Char(max(sp.Masa_Pajak_Sampai), 'MM-YYYY') as spt " +
					"FROM wajib_pajak wp left join periksa p on p.NPWPD = wp.NPWPD " +
					"LEFT JOIN SPTPD sp on sp.NPWPD = wp.NPWPD where p.IDPERIKSA is null and wp.Status = 'Aktif' and wp.Insidentil = 0 and wp.Jenis_Assesment = 'Self' " +
					"and wp.Kewajiban_Pajak <> 8 and wp.Kewajiban_Pajak like '" + kodePajak + "' and DATEDIFF('Month', wp.TANGGAL_DAFTAR, curdate()) > 3 group by wp.NPWPD " +
					"ORDER BY MasaPajakSampai, BulanDaftar;";
		} else{
			sqlQuery = "select wp.NPWPD, min(wp.Nama_Badan) as Nama_Badan, min(wp.Alabad_Jalan) as Alabad_Jalan, min(wp.Nama_Pemilik) as Nama_Pemilik, " +
					"To_Char(max(p.Masa_Pajak_Dari), 'MMMM YYYY') as MasaPajakDari, To_Char(max(p.Masa_Pajak_Sampai), 'MMMM YYYY') as MasaPajakSampai, " +
					"avg(p.Total_Pajak_Periksa) as PajakPeriksa, To_Char(max(wp.Tanggal_Daftar), 'MMMM YYYY') as BulanDaftar, max(wp.Tanggal_Daftar), wp.status, " +
					"To_Char(max(sp.Masa_Pajak_Sampai), 'MM-YYYY') as spt from wajib_pajak wp LEFT JOIN periksa p on p.NPWPD = wp.NPWPD " +
					"LEFT JOIN SPTPD sp on sp.NPWPD = wp.NPWPD WHERE wp.Status = 'Aktif' and wp.Insidentil = 0 " +
					"and wp.Jenis_Assesment = 'Self' and wp.Kewajiban_Pajak <> 8 and wp.Kewajiban_Pajak like '" + kodePajak + "' group by wp.NPWPD " +
					"having DATEDIFF('Month', max(p.Masa_Pajak_Sampai), curdate()) > 3 union " +
					"SELECT wp.NPWPD, min(wp.Nama_Badan) as Nama_Badan, min(wp.Alabad_Jalan) as Alabad_Jalan, min(wp.Nama_Pemilik) as Nama_Pemilik, " +
					"'-' as MasaPajakDari, '-' as MasaPajakSampai, 0 as PajakPeriksa, "+
					"To_Char(max(wp.Tanggal_Daftar), 'MMMM YYYY') as BulanDaftar, max(wp.Tanggal_Daftar), wp.status, To_Char(max(sp.Masa_Pajak_Sampai), 'MM-YYYY') as spt " +
					"FROM wajib_pajak wp left join periksa p on p.NPWPD = wp.NPWPD " +
					"LEFT JOIN SPTPD sp on sp.NPWPD = wp.NPWPD where p.IDPERIKSA is null and wp.Status = 'Aktif' and wp.Insidentil = 0 and wp.Jenis_Assesment = 'Self' " +
					"and wp.Kewajiban_Pajak <> 8 and wp.Kewajiban_Pajak like '" + kodePajak + "' and DATEDIFF('Month', wp.TANGGAL_DAFTAR, curdate()) > 3 group by wp.NPWPD " +
					"ORDER BY MasaPajakSampai DESC, BulanDaftar DESC;";
		}
		
		ResultSet result = db.ResultExecutedStatementQuery(sqlQuery, DBConnection.INSTANCE.openBawah());
		return result;
	}
	
	public ResultSet getSKPDKBList(String kodePajak, java.util.Date masaPajakDari, java.util.Date masaPajakSampai){
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String masaDari = formatter.format(masaPajakDari);
		String masaSampai = formatter.format(masaPajakSampai);
		String query = "SELECT p.idperiksa,p.No_SKP, CONCAT(To_Char(p.Masa_Pajak_Dari,'MM'), ' ', To_Char(p.Masa_Pajak_Dari,'YYYY'), '-', To_Char(p.Masa_Pajak_Sampai,'MM'), ' ', To_Char(p.Masa_Pajak_Sampai,'YYYY')) " +
						"AS MasaPajak, p.Tipe_SKP, p.Tanggal_SKP, TO_CHAR(p.Tanggal_SKP,'YYYY') AS Tahun, wp.Nama_Pemilik, wp.Nama_Badan, wp.Alabad_Jalan, p.NPWPD, " +
						"DATE_ADD(p.Tanggal_SKP, INTERVAL 1 MONTH) AS TanggalJatuhTempo, bu.Kode_Bid_Usaha, bu.Nama_Bid_Usaha, pj.Nama_Pajak, " +
						"ceiling(p.Total_Pajak_Periksa) AS PajakPeriksa, ceiling(p.Total_Pajak_Terutang) AS PajakTerutang, (ceiling(p.Total_Pajak_Periksa - p.Total_Pajak_Terutang)) AS KurangBayar, " +
						"ceiling(p.Total_Pajak_Bunga) AS Bunga, ceiling(p.Total_Kenaikan) AS Kenaikan25, ceiling(p.Kenaikan) as Kenaikan, ceiling(p.Total_Denda) AS Denda, ceiling(p.Total_Pajak_Bunga + p.Kenaikan) AS BiayaAdministrasi, " +
						"(ceiling(p.Total_Pajak_Periksa - p.Total_Pajak_Terutang) + ceiling(p.Total_Pajak_Bunga + p.Total_Denda) + ceiling(p.Kenaikan + p.Total_Kenaikan)) AS Total " +
						"FROM periksa p LEFT JOIN wajib_pajak wp ON wp.NPWPD = p.NPWPD " +
						"LEFT JOIN bidang_usaha bu ON bu.Idsub_pajak = wp.idsub_pajak " +
						"LEFT JOIN pajak pj ON pj.Kode_Pajak = wp.Kewajiban_Pajak " +
						"WHERE p.Tipe_SKP = 'SKPDKB' AND p.NO_SKP <> '' AND p.Tanggal_SKP >= '" + masaDari + "' AND p.Tanggal_SKP <= '" + masaSampai + "' AND pj.Kode_Pajak = " + kodePajak + " " +
						"ORDER BY p.Tanggal_SKP, bu.idsub_pajak, CAST(SUBSTRING(p.No_SKP, 1, POSITION('/' in p.No_SKP) - 1) AS int) ASC;";
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		return result;
	}
	
	public ResultSet getSKPDKBListSub(String kodePajak, java.util.Date masaPajakDari, java.util.Date masaPajakSampai, int idSubPajak){
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String masaDari = formatter.format(masaPajakDari);
		String masaSampai = formatter.format(masaPajakSampai);
		String query = "SELECT p.No_SKP, CONCAT(To_Char(p.Masa_Pajak_Dari,'MM'), ' ', To_Char(p.Masa_Pajak_Dari,'YYYY'), '-', To_Char(p.Masa_Pajak_Sampai,'MM'), ' ', To_Char(p.Masa_Pajak_Sampai,'YYYY')) " +
						"AS MasaPajak, p.Tipe_SKP, p.Tanggal_SKP, TO_CHAR(p.Tanggal_SKP,'YYYY') AS Tahun, wp.Nama_Pemilik, wp.Nama_Badan, wp.Alabad_Jalan, p.NPWPD, " +
						"DATE_ADD(p.Tanggal_SKP, INTERVAL 1 MONTH) AS TanggalJatuhTempo, bu.Kode_Bid_Usaha, bu.Nama_Bid_Usaha, pj.Nama_Pajak, " +
						"ceiling(p.Total_Pajak_Periksa) AS PajakPeriksa, ceiling(p.Total_Pajak_Terutang) AS PajakTerutang, (ceiling(p.Total_Pajak_Periksa - p.Total_Pajak_Terutang)) AS KurangBayar, " +
						"ceiling(p.Total_Pajak_Bunga) AS Bunga, ceiling(p.Total_Kenaikan) AS Kenaikan25, ceiling(p.Kenaikan) as Kenaikan, ceiling(p.Total_Denda) AS Denda, ceiling(p.Total_Pajak_Bunga + p.Kenaikan) AS BiayaAdministrasi, " +
						"(ceiling(p.Total_Pajak_Periksa - p.Total_Pajak_Terutang) + ceiling(p.Total_Pajak_Bunga + p.Total_Denda) + ceiling(p.Kenaikan + p.Total_Kenaikan)) AS Total " +
						"FROM periksa p LEFT JOIN wajib_pajak wp ON wp.NPWPD = p.NPWPD " +
						"LEFT JOIN bidang_usaha bu ON bu.idsub_pajak = p.idsub_pajak AND bu.DLT = 0 " +
						"LEFT JOIN pajak pj ON pj.Kode_Pajak = wp.Kewajiban_Pajak " +
						"WHERE p.Tipe_SKP = 'SKPDKB' AND p.NO_SKP <> '' AND p.Tanggal_SKP >= '" + masaDari + "' AND p.Tanggal_SKP <= '" + masaSampai + "' AND pj.Kode_Pajak = " + kodePajak + " AND bu.IdSub_Pajak = " + idSubPajak + " " +
						"ORDER BY bu.idsub_pajak, CAST(SUBSTRING(p.No_SKP, 1, POSITION('/' in p.No_SKP) - 1) AS int) ASC;";
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		return result;
	}
	
	public ResultSet getSKPDNList(String kodePajak, java.util.Date masaPajakDari, java.util.Date masaPajakSampai){
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String masaDari = formatter.format(masaPajakDari);
		String masaSampai = formatter.format(masaPajakSampai);
		String query = "SELECT p.No_SKP, CONCAT(To_Char(p.Masa_Pajak_Dari,'MM'), ' ', To_Char(p.Masa_Pajak_Dari,'YYYY'), '-', To_Char(p.Masa_Pajak_Sampai,'MM'), ' ', To_Char(p.Masa_Pajak_Sampai,'YYYY')) " +
						"AS MasaPajak, p.Tipe_SKP, p.Tanggal_SKP, TO_CHAR(p.Tanggal_SKP,'YYYY') AS Tahun, wp.Nama_Pemilik, wp.Nama_Badan, wp.Alabad_Jalan, p.NPWPD, " +
						"DATE_ADD(p.Tanggal_SKP, INTERVAL 1 MONTH) AS TanggalJatuhTempo, bu.Kode_Bid_Usaha, bu.Nama_Bid_Usaha, pj.Nama_Pajak, " +
						"p.Total_Pajak_Periksa AS PajakPeriksa, p.Total_Pajak_Terutang AS PajakTerutang, (p.Total_Pajak_Periksa - p.Total_Pajak_Terutang) AS KurangBayar, " +
						"p.Total_Pajak_Bunga AS Bunga, p.Kenaikan, 0 AS Denda, (p.Total_Pajak_Bunga + p.Kenaikan) AS BiayaAdministrasi, " +
						"(p.Total_Pajak_Periksa - p.Total_Pajak_Terutang + p.Total_Pajak_Bunga + p.Kenaikan) AS Total " +
						"FROM periksa p LEFT JOIN wajib_pajak wp ON wp.NPWPD = p.NPWPD " +
						"LEFT JOIN bidang_usaha bu ON bu.idsub_pajak = wp.idsub_pajak AND bu.DLT = 0 " +
						"LEFT JOIN pajak pj ON pj.Kode_Pajak = wp.Kewajiban_Pajak " +
						"WHERE p.Tipe_SKP = 'SKPDN' AND p.NO_SKP <> '' AND p.Tanggal_SKP >= '" + masaDari + "' AND p.Tanggal_SKP <= '" + masaSampai + "' AND pj.Kode_Pajak = " + kodePajak + " " +
						"ORDER BY bu.idsub_pajak, CAST(SUBSTRING(p.No_SKP, 1, POSITION('/' in p.No_SKP) - 1) AS int) ASC;";
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		return result;
	}
	
	public ResultSet getSKPDNListSub(String kodePajak, java.util.Date masaPajakDari, java.util.Date masaPajakSampai, int idSubPajak){
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String masaDari = formatter.format(masaPajakDari);
		String masaSampai = formatter.format(masaPajakSampai);
		String query = "SELECT p.No_SKP, CONCAT(To_Char(p.Masa_Pajak_Dari,'MM'), ' ', To_Char(p.Masa_Pajak_Dari,'YYYY'), '-', To_Char(p.Masa_Pajak_Sampai,'MM'), ' ', To_Char(p.Masa_Pajak_Sampai,'YYYY')) " +
						"AS MasaPajak, p.Tipe_SKP, p.Tanggal_SKP, TO_CHAR(p.Tanggal_SKP,'YYYY') AS Tahun, wp.Nama_Pemilik, wp.Nama_Badan, wp.Alabad_Jalan, p.NPWPD, " +
						"DATE_ADD(p.Tanggal_SKP, INTERVAL 1 MONTH) AS TanggalJatuhTempo, bu.Kode_Bid_Usaha, bu.Nama_Bid_Usaha, pj.Nama_Pajak, " +
						"p.Total_Pajak_Periksa AS PajakPeriksa, p.Total_Pajak_Terutang AS PajakTerutang, (p.Total_Pajak_Periksa - p.Total_Pajak_Terutang) AS KurangBayar, " +
						"p.Total_Pajak_Bunga AS Bunga, p.Kenaikan, 0 AS Denda, (p.Total_Pajak_Bunga + p.Kenaikan) AS BiayaAdministrasi, " +
						"(p.Total_Pajak_Periksa - p.Total_Pajak_Terutang + p.Total_Pajak_Bunga + p.Kenaikan) AS Total " +
						"FROM periksa p LEFT JOIN wajib_pajak wp ON wp.NPWPD = p.NPWPD " +
						"LEFT JOIN bidang_usaha bu ON bu.idsub_pajak = wp.idsub_pajak " +
						"LEFT JOIN pajak pj ON pj.Kode_Pajak = wp.Kewajiban_Pajak " +
						"WHERE p.Tipe_SKP = 'SKPDN' AND p.NO_SKP <> '' AND p.Tanggal_SKP >= '" + masaDari + "' AND p.Tanggal_SKP <= '" + masaSampai + "' AND pj.Kode_Pajak = " + kodePajak + " AND bu.IdSub_Pajak = " + idSubPajak + " " +
						"ORDER BY bu.idsub_pajak, CAST(SUBSTRING(p.No_SKP, 1, POSITION('/' in p.No_SKP) - 1) AS int) ASC;";
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		return result;
	}
	
	public ResultSet getSKPDKBTList(String kodePajak, java.util.Date masaPajakDari, java.util.Date masaPajakSampai){
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String masaDari = formatter.format(masaPajakDari);
		String masaSampai = formatter.format(masaPajakSampai);
		String query = "SELECT p.No_SKP , p.Tanggal_SKP, TO_CHAR(p.Tanggal_SKP,'YYYY') AS Tahun, wp.Nama_Pemilik, wp.Nama_Badan, wp.Alabad_Jalan, p.NPWPD, " +
				"bu.Kode_Bid_Usaha, bu.Nama_Bid_Usaha, pj.Nama_Pajak, bu.IDSUB_PAJAK, " +
				"CONCAT(To_Char(p.Masa_Pajak_Dari,'MM'), ' ', To_Char(p.Masa_Pajak_Dari,'YYYY'), '-', To_Char(p.Masa_Pajak_Sampai,'MM'), ' ', To_Char(p.Masa_Pajak_Sampai,'YYYY')) " +
				"AS MasaPajak, p.MASA_PAJAK_DARI, p.MASA_PAJAK_SAMPAI, DATE_ADD(p.Tanggal_SKP, INTERVAL 1 MONTH) AS TanggalJatuhTempo, p.TANGGAL_PERIKSA, " +
				"p.Total_Pajak_Periksa AS PajakPeriksa, p.Total_Pajak_Terutang AS PajakTerutang, SUM(pd.Kenaikan) AS SelisihKurang, SUM(pd.Kenaikan) AS Kenaikan, " +
				"SUM(pd.Total_Periksa) AS Total FROM periksa p " +
				"INNER JOIN PERIKSA_DETAIL2 pd ON p.IDPERIKSA = pd.IDPERIKSA INNER JOIN wajib_pajak wp ON wp.NPWPD = p.NPWPD " +
				"INNER JOIN bidang_usaha bu ON bu.idsub_pajak = wp.idsub_pajak INNER JOIN pajak pj ON pj.Kode_Pajak = wp.Kewajiban_Pajak " +
				"WHERE p.Tipe_SKP = 'SKPDKBT' AND p.Tanggal_SKP >= '" + masaDari + "' AND p.Tanggal_SKP <= '" + masaSampai + "' AND pj.Kode_Pajak = " + kodePajak + " " +
				"GROUP BY p.IDPERIKSA, p.NO_SKP, p.TANGGAL_SKP, wp.NAMA_PEMILIK, wp.NAMA_BADAN, wp.ALABAD_JALAN, p.NPWPD, bu.KODE_BID_USAHA, bu.NAMA_BID_USAHA, pj.NAMA_PAJAK, bu.IDSUB_PAJAK " +
				"ORDER BY bu.idsub_pajak, CAST(SUBSTRING(p.No_SKP, 1, POSITION('/' in p.No_SKP) - 1) AS int) ASC;";
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		return result;
	}
	
	public ResultSet getSKPDLBCovList(String kodePajak, java.util.Date masaPajakDari, java.util.Date masaPajakSampai){
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String masaDari = formatter.format(masaPajakDari);
		String masaSampai = formatter.format(masaPajakSampai);
		String query = "SELECT lb.NOSKPDLB, s.NO_SSPD, s.TGL_SSPD, sp.NO_SPTPD, sp.TGL_SPTPD, CONCAT(To_Char(sp.Masa_Pajak_Dari,'MM'), ' ', To_Char(sp.Masa_Pajak_Dari,'YYYY'), '-', To_Char(sp.Masa_Pajak_Sampai,'MM'), ' ', To_Char(sp.Masa_Pajak_Sampai,'YYYY')) " +
						"AS MasaPajak, TO_CHAR(sp.TGL_SPTPD,'YYYY') AS Tahun, wp.Nama_Pemilik, wp.Nama_Badan, wp.Alabad_Jalan, wp.NPWPD, " +
						"bu.Kode_Bid_Usaha, bu.Nama_Bid_Usaha, pj.Nama_Pajak, " +
						"sp.Dasar_Pengenaan, sp.Pajak_Terutang, s.Jumlah_Bayar, " +
						"ceiling(s.denda) AS DendaSSPD " +
						"FROM skpdlb lb LEFT JOIN sspd s ON lb.IDSSPD = s.IDSSPD " +
						"LEFT JOIN sptpd sp ON s.IDSPTPD = sp.IDSPTPD " +
						"LEFT JOIN wajib_pajak wp ON wp.NPWPD = s.NPWPD " +
						"LEFT JOIN bidang_usaha bu ON bu.idsub_pajak = s.idsub_pajak AND bu.DLT = 0 " +
						"LEFT JOIN pajak pj ON pj.Kode_Pajak = wp.Kewajiban_Pajak " +
						"WHERE s.NO_SSPD <> '' AND s.DENDA <> 0 AND lb.TANGGAL >= '" + masaDari + "' AND lb.TANGGAL <= '" + masaSampai + "' AND pj.Kode_Pajak = " + kodePajak + " " +
						"ORDER BY lb.IDSKPDLB ASC;";
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		return result;
	}
	
	public ResultSet getSKPDLBCovListSub(String kodePajak, java.util.Date masaPajakDari, java.util.Date masaPajakSampai, int idSubPajak){
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String masaDari = formatter.format(masaPajakDari);
		String masaSampai = formatter.format(masaPajakSampai);
		String query = "SELECT lb.NOSKPDLB, s.NO_SSPD, s.TGL_SSPD, sp.NO_SPTPD, sp.TGL_SPTPD, CONCAT(To_Char(sp.Masa_Pajak_Dari,'MM'), ' ', To_Char(sp.Masa_Pajak_Dari,'YYYY'), '-', To_Char(sp.Masa_Pajak_Sampai,'MM'), ' ', To_Char(sp.Masa_Pajak_Sampai,'YYYY')) " +
				"AS MasaPajak, TO_CHAR(sp.TGL_SPTPD,'YYYY') AS Tahun, wp.Nama_Pemilik, wp.Nama_Badan, wp.Alabad_Jalan, wp.NPWPD, " +
				"bu.Kode_Bid_Usaha, bu.Nama_Bid_Usaha, pj.Nama_Pajak, " +
				"sp.Dasar_Pengenaan, sp.Pajak_Terutang, s.Jumlah_Bayar, " +
				"ceiling(s.denda) AS DendaSSPD " +
				"FROM skpdlb lb LEFT JOIN sspd s ON lb.IDSSPD = s.IDSSPD " +
				"LEFT JOIN sptpd sp ON s.IDSPTPD = sp.IDSPTPD " +
				"LEFT JOIN wajib_pajak wp ON wp.NPWPD = s.NPWPD " +
				"LEFT JOIN bidang_usaha bu ON bu.idsub_pajak = s.idsub_pajak AND bu.DLT = 0 " +
				"LEFT JOIN pajak pj ON pj.Kode_Pajak = wp.Kewajiban_Pajak " +
				"WHERE s.NO_SSPD <> '' AND s.DENDA <> 0 AND lb.TANGGAL >= '" + masaDari + "' AND lb.TANGGAL <= '" + masaSampai + "' AND pj.Kode_Pajak = " + kodePajak + " AND bu.IdSub_Pajak = " + idSubPajak + " " +
				"ORDER BY lb.IDSKPDLB ASC;";
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		return result;
	}
	
	public ResultSet GetNotaSKPDLBCovList(String kodePajak, java.util.Date masaPajakDari, java.util.Date masaPajakSampai){
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String masaDari = formatter.format(masaPajakDari);
		String masaSampai = formatter.format(masaPajakSampai);
		String query = "SELECT lb.NOSKPDLB, lb.NONPPD, s.NO_SSPD, s.TGL_SSPD, sp.NO_SPTPD, sp.TGL_SPTPD, CONCAT(To_Char(sp.Masa_Pajak_Dari,'MM'), ' ', To_Char(sp.Masa_Pajak_Dari,'YYYY'), '-', To_Char(sp.Masa_Pajak_Sampai,'MM'), ' ', To_Char(sp.Masa_Pajak_Sampai,'YYYY')) " +
						"AS MasaPajak, TO_CHAR(sp.TGL_SPTPD,'YYYY') AS Tahun, wp.Nama_Pemilik, wp.Nama_Badan, wp.Alabad_Jalan, wp.NPWPD, " +
						"bu.Kode_Bid_Usaha, bu.Nama_Bid_Usaha, pj.Nama_Pajak, " +
						"sp.Dasar_Pengenaan, sp.Pajak_Terutang, s.Jumlah_Bayar, " +
						"ceiling(s.denda) AS DendaSSPD " +
						"FROM skpdlb lb LEFT JOIN sspd s ON lb.IDSSPD = s.IDSSPD " +
						"LEFT JOIN sptpd sp ON s.IDSPTPD = sp.IDSPTPD " +
						"LEFT JOIN wajib_pajak wp ON wp.NPWPD = s.NPWPD " +
						"LEFT JOIN bidang_usaha bu ON bu.idsub_pajak = s.idsub_pajak AND bu.DLT = 0 " +
						"LEFT JOIN pajak pj ON pj.Kode_Pajak = wp.Kewajiban_Pajak " +
						"WHERE s.NO_SSPD <> '' AND s.DENDA <> 0 AND lb.TANGGAL >= '" + masaDari + "' AND lb.TANGGAL <= '" + masaSampai + "' AND pj.Kode_Pajak = " + kodePajak + " " +
						"ORDER BY lb.IDSKPDLB ASC;";
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		return result;
	}
	
	public ResultSet GetNotaSKPDLBCovListSub(String kodePajak, String idSubPajak, java.util.Date masaPajakDari, java.util.Date masaPajakSampai){
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String masaDari = formatter.format(masaPajakDari);
		String masaSampai = formatter.format(masaPajakSampai);
		String query = "SELECT lb.SKPDLB, lb.NONPPD, s.NO_SSPD, s.TGL_SSPD, sp.NO_SPTPD, sp.TGL_SPTPD, CONCAT(To_Char(sp.Masa_Pajak_Dari,'MM'), ' ', To_Char(sp.Masa_Pajak_Dari,'YYYY'), '-', To_Char(sp.Masa_Pajak_Sampai,'MM'), ' ', To_Char(sp.Masa_Pajak_Sampai,'YYYY')) " +
				"AS MasaPajak, TO_CHAR(sp.TGL_SPTPD,'YYYY') AS Tahun, wp.Nama_Pemilik, wp.Nama_Badan, wp.Alabad_Jalan, wp.NPWPD, " +
				"bu.Kode_Bid_Usaha, bu.Nama_Bid_Usaha, pj.Nama_Pajak, " +
				"sp.Dasar_Pengenaan, sp.Pajak_Terutang, s.Jumlah_Bayar, " +
				"ceiling(s.denda) AS DendaSSPD " +
				"FROM skpdlb lb LEFT JOIN sspd s ON lb.IDSSPD = s.IDSSPD " +
				"LEFT JOIN sptpd sp ON s.IDSPTPD = sp.IDSPTPD " +
				"LEFT JOIN wajib_pajak wp ON wp.NPWPD = s.NPWPD " +
				"LEFT JOIN bidang_usaha bu ON bu.idsub_pajak = s.idsub_pajak AND bu.DLT = 0 " +
				"LEFT JOIN pajak pj ON pj.Kode_Pajak = wp.Kewajiban_Pajak " +
				"WHERE s.NO_SSPD <> '' AND s.DENDA <> 0  AND lb.TANGGAL >= '" + masaDari + "' AND lb.TANGGAL <= '" + masaSampai + "' AND pj.Kode_Pajak = " + kodePajak + " AND bu.IdSub_Pajak = " + idSubPajak + " " +
				"ORDER BY lb.IDSKPDLB ASC;";
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		return result;
	}
	
	public ResultSet GetNotaSKPDList(String kodePajak, java.util.Date masaPajakDari, java.util.Date masaPajakSampai){
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String masaDari = formatter.format(masaPajakDari);
		String masaSampai = formatter.format(masaPajakSampai);
		String query = "SELECT sp.No_NPPD, sp.Tgl_SPTPD AS TanggalNPPD, sp.NPWPD, wp.Nama_Badan, wp.Alabad_Jalan, sp.No_SPTPD AS NoSKP, sp.Tgl_SPTPD AS TanggalSKP, " +
				"sp.No_SPTPD, p.Nama_Pajak, bu.Kode_Bid_Usaha, bu.Nama_Bid_Usaha, sp.Dasar_Pengenaan, sp.Tarif_Pajak, sp.Pajak_Terutang, CAST(0 AS DECIMAL(12,2)) AS Kenaikan, " +
				"sp.Denda_Tambahan, CAST(0 AS DECIMAL(12, 2)) AS Bunga, CAST((sp.Pajak_Terutang + sp.Denda_Tambahan)AS DECIMAL(12, 2)) AS Total, " +
				"casewhen (Month(sp.Masa_Pajak_Dari) != Month(sp.Masa_Pajak_Sampai) or Year(sp.Masa_Pajak_Dari) != Year(sp.Masa_Pajak_Sampai), " +
				"concat(sp.Masa_Pajak_Dari, ' - ',sp.Masa_Pajak_Sampai), concat(sp.Masa_Pajak_Dari, '')) AS MasaPajak FROM sptpd sp " +
				"INNER JOIN bidang_usaha bu ON sp.IdSub_Pajak = bu.IdSub_Pajak INNER JOIN wajib_pajak wp ON wp.NPWPD = sp.NPWPD " +
				"INNER JOIN pajak p ON p.Kode_Pajak = bu.IDPAJAK " +
				"WHERE sp.No_SPTPD like '%SKPD/%'  AND bu.IDPAJAK = '" + kodePajak + "' AND (sp.Tgl_SPTPD between Date'" + masaDari + "' and Date'" + masaSampai + "') " +
				"ORDER BY CAST(SUBSTRING(sp.No_SPTPD, 1, POSITION('/' in sp.No_SPTPD) - 1) AS int) ASC;";
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		return result;
	}
	
	public ResultSet GetNotaSKPDListSub(String kodePajak, String idSubPajak, java.util.Date masaPajakDari, java.util.Date masaPajakSampai){
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String masaDari = formatter.format(masaPajakDari);
		String masaSampai = formatter.format(masaPajakSampai);
		String query = "SELECT sp.No_NPPD, sp.Tgl_SPTPD AS TanggalNPPD, sp.NPWPD, wp.Nama_Badan, wp.Alabad_Jalan, sp.No_SPTPD AS NoSKP, sp.Tgl_SPTPD AS TanggalSKP, " +
				"sp.No_SPTPD, p.Nama_Pajak, bu.Kode_Bid_Usaha, bu.Nama_Bid_Usaha, sp.Dasar_Pengenaan, sp.Tarif_Pajak, sp.Pajak_Terutang, CAST(0 AS DECIMAL(12, 2)) AS Kenaikan, " +
				"sp.Denda_Tambahan, CAST(0 AS DECIMAL(12, 2)) AS Bunga, CAST((sp.Pajak_Terutang + sp.Denda_Tambahan)AS DECIMAL(12, 2)) AS Total, " +
				"casewhen (Month(sp.Masa_Pajak_Dari) != Month(sp.Masa_Pajak_Sampai) or Year(sp.Masa_Pajak_Dari) != Year(sp.Masa_Pajak_Sampai), " +
				"concat(sp.Masa_Pajak_Dari, ' - ',sp.Masa_Pajak_Sampai), concat(sp.Masa_Pajak_Dari, '')) AS MasaPajak FROM sptpd sp " +
				"INNER JOIN bidang_usaha bu ON sp.IdSub_Pajak = bu.IdSub_Pajak INNER JOIN wajib_pajak wp ON wp.NPWPD = sp.NPWPD " +
				"INNER JOIN pajak p ON p.Kode_Pajak = bu.IDPAJAK " +
				"WHERE sp.No_SPTPD like '%SKPD/%'  AND bu.IDPAJAK = '" + kodePajak + "' AND (sp.Tgl_SPTPD between Date'" + masaDari + "' and Date'" + masaSampai + "') AND bu.IdSub_Pajak = '"  + idSubPajak + "' " +
				"ORDER BY CAST(SUBSTRING(sp.No_SPTPD, 1, POSITION('/' in sp.No_SPTPD) - 1) AS int) ASC;";
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		return result;
	}
	
	public ResultSet GetNotaSKPDKBList(String kodePajak, java.util.Date masaPajakDari, java.util.Date masaPajakSampai){
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String masaDari = formatter.format(masaPajakDari);
		String masaSampai = formatter.format(masaPajakSampai);
		String query = "SELECT p.No_SKP, p.No_NPPD, casewhen (Month(p.Masa_Pajak_Dari) != Month(p.Masa_Pajak_Sampai) or Year(p.Masa_Pajak_Dari) != Year(p.Masa_Pajak_Sampai), " +
				"concat(p.Masa_Pajak_Dari, ' - ',p.Masa_Pajak_Sampai), concat(p.Masa_Pajak_Dari, '')) AS MasaPajak, p.Tipe_SKP, p.Tanggal_NPPD, wp.Nama_Badan, " +
				"wp.Alabad_Jalan as AlamatBadan, p.NPWPD, bu.Kode_Bid_Usaha as KodeBidangUsaha, bu.Nama_Bid_Usaha as NamaBidangUsaha, pj.Nama_Pajak, bu.Tarif, p.Total_Pajak_Periksa AS PajakPeriksa, " +
				"p.Total_Pajak_Terutang AS PajakTerutang, (p.Total_Pajak_Periksa - p.Total_Pajak_Terutang) AS KurangBayar, p.Total_Pajak_Bunga AS Bunga, " +
				"p.Total_Kenaikan AS Kenaikan25, p.Kenaikan, Cast(0 as Decimal(12, 2)) AS Denda, (p.Total_Pajak_Bunga + p.Kenaikan) AS BiayaAdministrasi, " +
				"(p.Total_Pajak_Periksa - p.Total_Pajak_Terutang + p.Total_Pajak_Bunga + p.Kenaikan + p.Total_Kenaikan) AS Total " +
				"FROM periksa p LEFT JOIN wajib_pajak wp ON wp.NPWPD = p.NPWPD " +
				"LEFT JOIN bidang_usaha bu ON bu.IDPajak = wp.Kewajiban_Pajak AND bu.idsub_pajak = wp.idsub_pajak " +
				"LEFT JOIN pajak pj ON pj.Kode_Pajak = wp.Kewajiban_Pajak " +
				"WHERE p.Tipe_SKP='SKPDKB' AND p.NO_SKP <> '' AND (p.Tanggal_SKP between Date'" + masaDari + "' and Date'" + masaSampai + "') AND pj.Kode_Pajak = '" + kodePajak + "' " +
				"ORDER BY CAST(SUBSTRING(p.No_SKP, 1, POSITION('/' in p.No_SKP) - 1) AS int) ASC;";
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		return result;
	}
	
	public ResultSet GetNotaSKPDKBListSub(String kodePajak, String idSubPajak, java.util.Date masaPajakDari, java.util.Date masaPajakSampai){
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String masaDari = formatter.format(masaPajakDari);
		String masaSampai = formatter.format(masaPajakSampai);
		String query = "SELECT p.No_SKP, p.No_NPPD, casewhen (Month(p.Masa_Pajak_Dari) != Month(p.Masa_Pajak_Sampai) or Year(p.Masa_Pajak_Dari) != Year(p.Masa_Pajak_Sampai), " +
				"concat(p.Masa_Pajak_Dari, ' - ',p.Masa_Pajak_Sampai), concat(p.Masa_Pajak_Dari, '')) AS MasaPajak, p.Tipe_SKP, p.Tanggal_NPPD, wp.Nama_Badan, " +
				"wp.Alabad_Jalan as AlamatBadan, p.NPWPD, bu.Kode_Bid_Usaha as KodeBidangUsaha, bu.Nama_Bid_Usaha as NamaBidangUsaha, pj.Nama_Pajak, bu.Tarif, p.Total_Pajak_Periksa AS PajakPeriksa, " +
				"p.Total_Pajak_Terutang AS PajakTerutang, (p.Total_Pajak_Periksa - p.Total_Pajak_Terutang) AS KurangBayar, p.Total_Pajak_Bunga AS Bunga, " +
				"p.Total_Kenaikan AS Kenaikan25, p.Kenaikan, Cast(0 as Decimal(12, 2)) AS Denda, (p.Total_Pajak_Bunga + p.Kenaikan) AS BiayaAdministrasi, " +
				"(p.Total_Pajak_Periksa - p.Total_Pajak_Terutang + p.Total_Pajak_Bunga + p.Kenaikan + p.Total_Kenaikan) AS Total " +
				"FROM periksa p LEFT JOIN wajib_pajak wp ON wp.NPWPD = p.NPWPD " +
				"LEFT JOIN bidang_usaha bu ON bu.IDPajak = wp.Kewajiban_Pajak AND bu.idsub_pajak = wp.idsub_pajak " +
				"LEFT JOIN pajak pj ON pj.Kode_Pajak = wp.Kewajiban_Pajak " +
				"WHERE p.Tipe_SKP='SKPDKB' AND p.NO_SKP <> '' AND (p.Tanggal_SKP between Date'" + masaDari + "' and Date'" + masaSampai + "') AND pj.Kode_Pajak = '" + kodePajak + "' AND bu.IdSub_Pajak = '" + idSubPajak + "' " +
				"ORDER BY CAST(SUBSTRING(p.No_SKP, 1, POSITION('/' in p.No_SKP) - 1) AS int) ASC;";
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		return result;
	}
	
	public ResultSet GetNotaSKPDKBTList(String kodePajak, java.util.Date masaPajakDari, java.util.Date masaPajakSampai){
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String masaDari = formatter.format(masaPajakDari);
		String masaSampai = formatter.format(masaPajakSampai);
		String query = "SELECT p.No_NPPD, p.Tanggal_NPPD,p.NPWPD, wp.Nama_Badan, wp.Alabad_Jalan as AlamatBadan, p.No_SKP, p.Tanggal_SKP, pj.Nama_Pajak, bu.Nama_Bid_Usaha, " +
				"bu.Tarif, p.Total_Pajak_Periksa as PajakPeriksa, p.Total_Pajak_Terutang as PajakTerutang, SUM(pd.Kenaikan) as Kurangbayar, sum(pd.Kenaikan) as Kenaikan, " +
				"sum(pd.Total_Periksa) as Total, casewhen (Month(p.Masa_Pajak_Dari) != Month(p.Masa_Pajak_Sampai) or Year(p.Masa_Pajak_Dari) != Year(p.Masa_Pajak_Sampai), " +
				"concat(p.Masa_Pajak_Dari, ' - ',p.Masa_Pajak_Sampai), concat(p.Masa_Pajak_Dari, '')) AS MasaPajak, p.Masa_Pajak_Dari, p.Masa_Pajak_Sampai " +
				"FROM periksa p INNER JOIN periksa_detail2 pd ON p.IdPeriksa = pd.IdPeriksa INNER JOIN wajib_pajak wp ON p.NPWPD = wp.NPWPD " +
				"INNER JOIN bidang_usaha bu ON bu.IdSub_Pajak = p.IdSub_Pajak INNER JOIN pajak pj ON pj.Kode_Pajak = bu.IDPajak " +
				"WHERE p.Tipe_SKP = 'SKPDKBT' AND pj.Kode_Pajak = '" + kodePajak + "' AND (p.Tanggal_SKP between Date'" + masaDari + "' AND Date'" + masaSampai + "') " +
				"GROUP BY p.IdPeriksa, p.NO_NPPD, p.TANGGAL_NPPD, p.NPWPD, wp.NAMA_BADAN, wp.ALABAD_JALAN, p.no_skp, p.TANGGAL_SKP, pj.NAMA_PAJAK, bu.NAMA_BID_USAHA, bu.TARIF " +
				"ORDER BY CAST(SUBSTRING(p.No_SKP, 1, POSITION('/' in p.No_SKP) - 1) AS int) ASC;";
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		return result;
	}
	
	public ResultSet GetNotaSKPDNList(String kodePajak, java.util.Date masaPajakDari, java.util.Date masaPajakSampai){
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String masaDari = formatter.format(masaPajakDari);
		String masaSampai = formatter.format(masaPajakSampai);
		String query = "SELECT p.No_SKP, p.No_NPPD, casewhen (Month(p.Masa_Pajak_Dari) != Month(p.Masa_Pajak_Sampai) or Year(p.Masa_Pajak_Dari) != Year(p.Masa_Pajak_Sampai), " +
				"concat(p.Masa_Pajak_Dari, ' - ', p.Masa_Pajak_Sampai), concat(p.Masa_Pajak_Dari, '')) AS MasaPajak, p.Tipe_SKP, p.Tanggal_NPPD, wp.Nama_Badan, " +
				"wp.Alabad_Jalan as AlamatBadan, p.NPWPD, bu.Kode_Bid_Usaha as KodeBidangUsaha, bu.Nama_Bid_Usaha as NamaBidangUsaha, pj.Nama_Pajak, bu.Tarif, " +
				"p.Total_Pajak_Periksa AS PajakPeriksa, p.Total_Pajak_Terutang AS PajakTerutang, (p.Total_Pajak_Periksa - p.Total_Pajak_Terutang) AS KurangBayar, " +
				"p.Total_Pajak_Bunga AS Bunga, p.Kenaikan, Cast(0 as Decimal(12,2)) AS Denda, (p.Total_Pajak_Bunga + p.Kenaikan) AS BiayaAdministrasi, " +
				"(p.Total_Pajak_Periksa - p.Total_Pajak_Terutang + p.Total_Pajak_Bunga + p.Kenaikan) AS Total FROM periksa p LEFT JOIN wajib_pajak wp ON wp.NPWPD = p.NPWPD " +
				"LEFT JOIN bidang_usaha bu ON bu.IDPajak = wp.Kewajiban_Pajak AND bu.idsub_pajak = wp.idsub_pajak " +
				"LEFT JOIN pajak pj ON pj.Kode_Pajak = wp.Kewajiban_Pajak " +
				"WHERE p.Tipe_SKP='SKPDN' AND p.NO_SKP <> '' AND (p.Tanggal_SKP between Date'" + masaDari + "' AND Date'" + masaSampai + "') AND pj.Kode_Pajak = '" + kodePajak + "' " +
				"ORDER BY CAST(SUBSTRING(p.No_SKP, 1, POSITION('/' in p.No_SKP) - 1) AS int) ASC;";
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		return result;
	}

	public ResultSet GetNotaSKPDNListSub(String kodePajak, String idSubPajak, java.util.Date masaPajakDari, java.util.Date masaPajakSampai){
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String masaDari = formatter.format(masaPajakDari);
		String masaSampai = formatter.format(masaPajakSampai);
		String query = "SELECT p.No_SKP, p.No_NPPD, casewhen (Month(p.Masa_Pajak_Dari) != Month(p.Masa_Pajak_Sampai) or Year(p.Masa_Pajak_Dari) != Year(p.Masa_Pajak_Sampai), " +
				"concat(p.Masa_Pajak_Dari, ' - ', p.Masa_Pajak_Sampai), concat(p.Masa_Pajak_Dari, '')) AS MasaPajak, p.Tipe_SKP, p.Tanggal_NPPD, wp.Nama_Badan, " +
				"wp.Alabad_Jalan as AlamatBadan, p.NPWPD, bu.Kode_Bid_Usaha as KodeBidangUsaha, bu.Nama_Bid_Usaha as NamaBidangUsaha, pj.Nama_Pajak, bu.Tarif, " +
				"p.Total_Pajak_Periksa AS PajakPeriksa, p.Total_Pajak_Terutang AS PajakTerutang, (p.Total_Pajak_Periksa - p.Total_Pajak_Terutang) AS KurangBayar, " +
				"p.Total_Pajak_Bunga AS Bunga, p.Kenaikan, 0 AS Denda, (p.Total_Pajak_Bunga + p.Kenaikan) AS BiayaAdministrasi, " +
				"(p.Total_Pajak_Periksa - p.Total_Pajak_Terutang + p.Total_Pajak_Bunga + p.Kenaikan) AS Total FROM periksa p LEFT JOIN wajib_pajak wp ON wp.NPWPD = p.NPWPD " +
				"LEFT JOIN bidang_usaha bu ON bu.IDPajak = wp.Kewajiban_Pajak AND bu.idsub_pajak = wp.idsub_pajak " +
				"LEFT JOIN pajak pj ON pj.Kode_Pajak = wp.Kewajiban_Pajak " +
				"WHERE p.Tipe_SKP='SKPDN' AND p.NO_SKP <> '' AND (p.Tanggal_SKP between Date'" + masaDari + "' AND Date'" + masaSampai + "') AND pj.Kode_Pajak = '" + kodePajak + "' AND bu.IdSub_Pajak = '" + idSubPajak + "' " +
				"ORDER BY CAST(SUBSTRING(p.No_SKP, 1, POSITION('/' in p.No_SKP) - 1) AS int) ASC;";
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		return result;
	}
	
	public ResultSet GetRealisasiPenerimaanSKPDKB(String kodePajak, java.util.Date masaPajakDari, java.util.Date masaPajakSampai, java.util.Date breakReal){
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String masaDari = formatter.format(masaPajakDari);
		String masaSampai = formatter.format(masaPajakSampai);
		String realisasi = formatter.format(breakReal);
//		String query = "SELECT wp.Nama_Badan, wp.Alabad_Jalan, p.NPWPD, p.Tanggal_SKP, p.No_SKP, To_Char(p.Masa_Pajak_Dari, 'MM-YYYY') as MasaPajakDari, " +
//				"To_Char(p.Masa_Pajak_Sampai, 'MM-YYYY') as MasaPajakSampai, p.Total_Pajak_Periksa as PajakPeriksa, " +
//				"IFNULL(md.Angsuran_Pokok, (p.Total_Pajak_Periksa - p.Total_Pajak_Terutang + p.Total_Kenaikan)) as SKPDKB, " +
//				"IFNULL(md.Biaya_Administrasi + md.Denda_SKPDKB, p.Total_Pajak_Bunga + p.TOTAL_DENDA) as Denda, ss.jumlah_Bayar as REALISASI, ss.Tgl_Cetak as TanggalBayar " +
//				"FROM periksa p LEFT JOIN sspd ss on ss.NPWPD = p.NPWPD and ss.No_SKP = p.No_SKP LEFT JOIN mohon m on m.NPWPD = ss.NPWPD and m.No_SKP = ss.No_SKP " +
//				"LEFT JOIN mohon_detail md on md.IdMohon = m.IdMohon and ss.Cicilan_Ke = md.No_Angsuran LEFT JOIN wajib_pajak wp on wp.NPWPD = p.NPWPD " +
//				"WHERE Substring(p.NPWPD, 1, 1) like '%" + kodePajak + "%' AND p.No_SKP like '%SKPDKB%' AND (p.Tanggal_SKP between Date'" + masaDari + "' AND Date'" + masaSampai + "') " +
//				"ORDER BY CAST(Substring(NPWPD,1,1) as int), CAST(SUBSTRING(p.No_SKP, 1, length(p.No_SKP) - 12) AS int) ASC";
		
		String query = "SELECT wp.Nama_Badan, wp.Alabad_Jalan, p.NPWPD, p.Tanggal_SKP, p.No_SKP, p.Masa_Pajak_Dari as MasaPajakDari, " +
				"p.Masa_Pajak_Sampai as MasaPajakSampai, p.Total_Pajak_Periksa as PajakPeriksa, " +
				"IFNULL(md.Angsuran_Pokok, (p.Total_Pajak_Periksa - p.Total_Pajak_Terutang + p.Total_Kenaikan)) as SKPDKB, " +
				"IFNULL(md.Biaya_Administrasi, p.Total_Pajak_Bunga + p.TOTAL_DENDA) as Denda, md.Denda_SKPDKB as DendaSKPDKB, ss.Denda as DendaSSPD, ss.jumlah_Bayar as REALISASI, " +
				"ss.Tgl_Cetak as TanggalBayar, wp.Keterangan_Tutup as Keterangan " + 
				"FROM periksa p LEFT JOIN sspd ss on ss.IDPERIKSA = p.IDPERIKSA AND ss.TGL_CETAK <= Timestamp'" + realisasi + " 23:59:59' " +
				"LEFT JOIN mohon m on m.NPWPD = ss.NPWPD and m.No_SKP = ss.No_SKP LEFT JOIN mohon_detail md on md.IdMohon = m.IdMohon and ss.Cicilan_Ke = md.No_Angsuran " +
				"LEFT JOIN wajib_pajak wp on wp.NPWPD = p.NPWPD " +
				"WHERE Substring(p.NPWPD, 1, 1) like '%" + kodePajak + "%' AND p.No_SKP like '%SKPDKB%' AND (p.Tanggal_SKP between Date'" + masaDari + "' AND Date'" + masaSampai + "') " +
				"ORDER BY CAST(Substring(NPWPD,1,1) as int), CAST(SUBSTRING(p.No_SKP, 1,  POSITION('/' in p.No_SKP) - 1) AS int) ASC";
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.openBawah());
		return result;
	}
	
	@SuppressWarnings("deprecation")
	public ResultSet GetRealisasiPenerimaanSKPDKBbyTglCetak(String kodePajak, java.util.Date masaPajakDari, java.util.Date masaPajakSampai){
		SimpleDateFormat formatDari = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat formatSampai = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
		String masaDari = formatDari.format(masaPajakDari);
		String masaSampai = formatSampai.format(masaPajakSampai);
		String query = "SELECT wp.Nama_Badan, wp.Alabad_Jalan, p.NPWPD, p.Tanggal_SKP, p.No_SKP, p.Masa_Pajak_Dari as MasaPajakDari, " +
				"p.Masa_Pajak_Sampai as MasaPajakSampai, p.Total_Pajak_Periksa as PajakPeriksa, " +
				"IFNULL(md.Angsuran_Pokok, (p.Total_Pajak_Periksa - p.Total_Pajak_Terutang + p.Total_Kenaikan)) as SKPDKB, " +
				"IFNULL(md.Biaya_Administrasi, p.Total_Pajak_Bunga + p.TOTAL_DENDA) as Denda, md.Denda_SKPDKB as DendaSKPDKB, ss.Denda as DendaSSPD, ss.jumlah_Bayar as REALISASI, " +
				"ss.Tgl_Cetak as TanggalBayar, wp.Keterangan_Tutup as Keterangan " +
				"FROM sspd ss left JOIN wajib_pajak wp ON wp.NPWPD = ss.NPWPD " +
				"left JOIN periksa p on p.IDPERIKSA = ss.IDPERIKSA left join mohon m on m.NPWPD = ss.NPWPD and m.No_SKP = ss.No_SKP " +
				"left join mohon_detail md on md.IdMohon = m.IdMohon and ss.Cicilan_Ke = md.No_Angsuran " +
				"WHERE Substring(ss.NPWPD, 1, 1) like '%" + kodePajak + "%' AND ss.No_SKP like '%SKPDKB%' AND (ss.Tgl_CETAK between Date '" + masaDari + "' AND Timestamp '" + masaSampai + "') " + 
				"ORDER BY CAST(Substring(NPWPD,1,1) as int), CAST(SUBSTRING(ss.No_SKP, 1,  POSITION('/' in ss.No_SKP) - 1) AS int) ASC";
		
		ResultSet result = null;
		if (masaPajakDari.getMonth() == masaPajakSampai.getMonth())
			result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		else
			result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.openBawah());
		return result;
	}
	
	public String[] GetDataWP(String NPWPD){
		String[] retValue = new String[4];
		String query = "Select Nama_Badan, Nama_Pemilik, Alabad_Jalan, Kode_Bid_Usaha from Wajib_Pajak where NPWPD = '" + NPWPD + "'";
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		try {
			if (result.next()){
				retValue[0] = result.getString("Nama_Badan");
				retValue[1] = result.getString("Nama_Pemilik");
				retValue[2] = result.getString("Alabad_Jalan");
				retValue[3] = result.getString("Kode_Bid_Usaha");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return retValue;
	}
	
	public String getNoSTS(int id){
		String query = "Select STS from Periksa where idPeriksa = " + id;
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		try {
			if (result.next()){
				if (result.getString("STS") != null)
					return result.getString("STS");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	
	public boolean checkDendaTambahan(String npwpd, String bulan){
		boolean retValue = true;
		String query = "Select * from SSPD ss INNER JOIN SPTPD sp on ss.IDSPTPD = sp.IDSPTPD where npwpd = '" + npwpd  +"' AND BULAN_SKP = '" + bulan + "'";
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		try{
			if (result.next()){
				boolean dendaLunas = result.getBoolean("Denda_Lunas");
				double jlhBayar = result.getDouble("Jumlah_Bayar");
				double denda = result.getDouble("Denda");
				double pokok = result.getDouble("Pajak_Terutang");
				if (dendaLunas){
					if (jlhBayar - (denda+pokok) == 120000)
						retValue = false;
				}else{
					if (jlhBayar - pokok == 120000)
						 retValue = false;
				}
				
			}
		}catch (Exception e) {
			// TODO: handle exception
		}
		return retValue;
	}
	
	/*public ResultSet getIDSSPDSPTPD(String noSSPD) throws SQLException{
		String queryGet = "Select IDSPTPD,IDSSPD from SSPD where NO_SSPD ='"+noSSPD+"';";
		System.out.println("qG: "+queryGet);
		ResultSet res = db.ResultExecutedStatementQuery(queryGet, DBConnection.INSTANCE.open());
		
		System.out.println("rS: "+res.getInt("IDSPTPD"));
		System.out.println("rS: "+res.getInt("IDSSPD"));
		return res;	
	}*/
	
	public boolean saveSKPDLB(int idSKPDLB,String NPWPD,String noSKPDLB,String noSSPD){
		String queryGet = "Select IDSPTPD,IDSSPD from SSPD where NO_SSPD ='"+noSSPD+"';";
		ResultSet res = db.ResultExecutedStatementQuery(queryGet, DBConnection.INSTANCE.open());
		int idSPTPD = 0;
		int idSSPD = 0;
		
		try {
			while(res.next()){
				idSPTPD = res.getInt(1);
				idSSPD = res.getInt(2);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		String querySave = "INSERT INTO PUBLIC.SKPDLB (IDSKPDLB, NPWPD, IDSPTPD, IDSSPD, NOSKPDLB, TANGGAL, NONPPD) VALUES ("
				+idSKPDLB+",'"
				+NPWPD+"',"
				+idSPTPD+","
				+idSSPD+",'"
				+noSKPDLB+"','"
				+formatter.format(cal.getTime())+"','"
				+noSKPDLB.replace("SKPDLB", "NPPD")+"');";
		boolean result = db.ExecuteStatementQuery(querySave, DBConnection.INSTANCE.open());
		if(!result)
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada INSERT tabel SKPDLB.");
		return result;
	}
}
