package com.dispenda.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;

import com.dispenda.model.Sptpd;
import com.dispenda.database.DBConnection;
import com.dispenda.database.DBOperation;

public class CPSptpdDAOImpl {
	private Sptpd sptpdModel = new Sptpd();
	private DBOperation db = new DBOperation();
	
	public boolean saveSptpd(Sptpd sptpdModel){
		String query = "MERGE INTO SPTPD USING (VALUES("+
				""+sptpdModel.getID()+"," +
				"'"+sptpdModel.getNoNPPD()+"'," +
				"'"+sptpdModel.getTanggalNPPD()+"'," +
				"'"+sptpdModel.getNoSPTPD()+"'," +
				"'"+sptpdModel.getTanggalSPTPD()+"'," +
				"'"+sptpdModel.getMasaPajakDari()+"'," +
				"'"+sptpdModel.getMasaPajakSampai()+"'," +
				"'"+sptpdModel.getNPWPD()+"'," +
				""+sptpdModel.getDasarPengenaan()+"," +
				""+sptpdModel.getTarifPajak()+"," +
				""+sptpdModel.getPajakterutang()+"," +
				""+sptpdModel.getDendaTambahan()+"," +
				""+sptpdModel.getIdSubPajak()+"," +
				"'"+sptpdModel.getLokasi()+"'," +
				"'"+sptpdModel.getAssesment()+"'," + 
				"'"+sptpdModel.getStsValid() + "'))" +
//				"'"+sptpdModel.getNoSPTPDLama()+"'))" +
			" AS vals(" +
				"id," +
				"no_nppd," +
				"tgl_nppd," +
				"no_sptpd," +
				"tgl_sptpd," +
				"masa_pajak_dari," +
				"masa_pajak_sampai," +
				"npwpd," +
				"dasar_pengenaan," +
				"tarif_pajak," +
				"pajak_terhutang," +
				"denda_tambahan," +
				"idsub_pajak," +
				"lokasi," +
				"assesment," +
				"stsvalid)" +
//				"noSKPLama)" + 
			" ON SPTPD.IDSPTPD = vals.id" +
			" WHEN MATCHED THEN UPDATE SET " +
				"SPTPD.NO_NPPD = vals.no_nppd," +
				"SPTPD.TGL_NPPD = vals.tgl_nppd," +
				"SPTPD.NO_SPTPD = vals.no_sptpd," +
				"SPTPD.TGL_SPTPD = vals.tgl_sptpd," +
				"SPTPD.MASA_PAJAK_DARI = vals.masa_pajak_dari," +
				"SPTPD.MASA_PAJAK_SAMPAI = vals.masa_pajak_sampai," +
				"SPTPD.NPWPD = vals.npwpd," +
				"SPTPD.DASAR_PENGENAAN = vals.dasar_pengenaan," +
				"SPTPD.TARIF_PAJAK = vals.tarif_pajak," +
				"SPTPD.PAJAK_TERUTANG = vals.pajak_terhutang," +
				"SPTPD.DENDA_TAMBAHAN = vals.denda_tambahan," +
				"SPTPD.IDSUB_PAJAK = vals.idsub_pajak," +
				"SPTPD.LOKASI = vals.lokasi," +
				"SPTPD.ASSESMENT = vals.assesment," +
				"SPTPD.STSVALID = vals.stsvalid" +
//				"SPTPD.NO_SKPLAMA = vals.noSKPLama" +
			" WHEN NOT MATCHED THEN INSERT (IDSPTPD, NO_NPPD, TGL_NPPD, NO_SPTPD, TGL_SPTPD, MASA_PAJAK_DARI, MASA_PAJAK_SAMPAI, NPWPD, DASAR_PENGENAAN, TARIF_PAJAK, " +
			"PAJAK_TERUTANG, DENDA_TAMBAHAN, IDSUB_PAJAK, LOKASI, ASSESMENT, STSVALID) VALUES " +
				"vals.id," +
				"vals.no_nppd," +
				"vals.tgl_nppd," +
				"vals.no_sptpd," +
				"vals.tgl_sptpd," +
				"vals.masa_pajak_dari," +
				"vals.masa_pajak_sampai," +
				"vals.npwpd," +
				"vals.dasar_pengenaan," +
				"vals.tarif_pajak," +
				"vals.pajak_terhutang," +
				"vals.denda_tambahan," +
				"vals.idsub_pajak," +
				"vals.lokasi," +
				"vals.assesment,"+
				"vals.stsvalid;";
//				"vals.noSKPLama" ;
		boolean result = db.ExecuteStatementQuery(query, DBConnection.INSTANCE.open());
		if(!result)
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada MERGE(UPDATE/INSERT) tabel SPTPD.");
		return result;
	}
	
	public List<Sptpd> getSptpd(String npwpd){
		String query = "SELECT * FROM SPTPD WHERE NPWPD = '"+npwpd+"' order by masa_pajak_sampai desc, CAST(SUBSTRING(NO_SPTPD, 1, POSITION('/' in No_SPTPD) - 1) AS INTEGER) DESC";
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		if(result!=null){
			try{
				List<Sptpd> listSptpd = new ArrayList<Sptpd>();
				while(result.next()){
					Sptpd sptpdModel = new Sptpd();
					sptpdModel.setID(result.getInt("IDSPTPD"));
					sptpdModel.setNoNPPD(result.getString("NO_NPPD"));
					sptpdModel.setTanggalNPPD(result.getDate("TGL_NPPD"));
					sptpdModel.setNoSPTPD(result.getString("NO_SPTPD"));
					sptpdModel.setTanggalSPTPD(result.getDate("TGL_SPTPD"));
					sptpdModel.setMasaPajakDari(result.getDate("MASA_PAJAK_DARI"));
					sptpdModel.setMasaPajakSampai(result.getDate("MASA_PAJAK_SAMPAI"));
					sptpdModel.setNPWPD(result.getString("NPWPD"));
					sptpdModel.setDasarPengenaan(result.getDouble("DASAR_PENGENAAN"));
					sptpdModel.setTarifPajak(result.getInt("TARIF_PAJAK"));
					sptpdModel.setPajakTerutang(result.getDouble("PAJAK_TERUTANG"));
					sptpdModel.setDendaTambahan(result.getDouble("DENDA_TAMBAHAN"));
					sptpdModel.setIdSubPajak(result.getInt("IDSUB_PAJAK"));
					sptpdModel.setLokasi(result.getString("LOKASI"));
					sptpdModel.setAssesment(result.getString("ASSESMENT"));
					sptpdModel.setNoSPTPDLama(result.getString("No_SKPLama") == null? "" : result.getString("No_SKPLama"));
					listSptpd.add(sptpdModel);
				}
				return listSptpd;
			}
			catch(SQLException e){
				MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database SQL Error", e.getMessage());
				return null;
			}
		}
		else{
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada pembuatan LOAD DATA tabel SPTPD.");
			return null;
		}
		
	}
	
	public List<Sptpd> getMasaPajak(String npwpd){
		String query = "SELECT x.No_SKP, x.Tanggal_SKP, " +
				"x.MasaPajak, x.Masa_Pajak_Dari, x.Masa_Pajak_Sampai FROM ((SELECT s.NPWPD, s.No_SPTPD AS No_SKP, s.Tgl_SPTPD AS Tanggal_SKP, " +
				"casewhen (Month(s.Masa_Pajak_Dari) = Month(s.Masa_Pajak_Sampai), concat(MonthName(s.Masa_Pajak_Dari), ' ' , year(s.Masa_Pajak_Dari)), " +
				"concat(MonthName(s.Masa_Pajak_Dari), ' ' , year(s.Masa_Pajak_Dari), ' - ' , MonthName(s.Masa_Pajak_Sampai), ' ' , year(s.Masa_Pajak_Sampai)))" +
				"AS MasaPajak, s.Masa_Pajak_Dari, s.Masa_Pajak_Sampai FROM sptpd s) UNION " +
				"(SELECT p.NPWPD, p.No_SKP, p.Tanggal_SKP, " +
				"casewhen (Month(p.Masa_Pajak_Dari) = Month(p.Masa_Pajak_Sampai), concat(MonthName(p.Masa_Pajak_Dari), ' ' , year(p.Masa_Pajak_Dari)), " +
				"concat(MonthName(p.Masa_Pajak_Dari), ' ' , To_Char(p.Masa_Pajak_Dari, 'YY'), ' - ' , MonthName(p.Masa_Pajak_Sampai), ' ' , To_Char(p.Masa_Pajak_Sampai, 'YY'))) " +
				"AS MasaPajak, p.Masa_Pajak_Dari, p.Masa_Pajak_Sampai FROM periksa p where p.No_SKP <> '')) x WHERE x.NPWPD = '" + npwpd  + "' ORDER BY x.Masa_Pajak_Dari DESC;";
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		if(result!=null){
			try{
				List<Sptpd> listSptpd = new ArrayList<Sptpd>();
				while(result.next()){
					Sptpd sptpdModel = new Sptpd();
					sptpdModel.setNoSPTPD(result.getString("NO_SKP"));
					sptpdModel.setMasaPajak(result.getString("MasaPajak"));
					sptpdModel.setTanggalSPTPD(result.getDate("Tanggal_SKP"));
					sptpdModel.setMasaPajakDari(result.getDate("MASA_PAJAK_DARI"));
					sptpdModel.setMasaPajakSampai(result.getDate("MASA_PAJAK_SAMPAI"));
					listSptpd.add(sptpdModel);
				}
				return listSptpd;
			}
			catch(SQLException e){
				MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database SQL Error", e.getMessage());
				return null;
			}
		}
		else{
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada pembuatan LOAD DATA tabel SPTPD.");
			return null;
		}
		
	}
	
	public List<Sptpd> getMasaPajakSKPD(String npwpd){
		String query = "SELECT s.NPWPD, s.No_SPTPD AS No_SKP, s.No_SKPLama, s.Tgl_SPTPD AS Tanggal_SKP, " +
				"casewhen (Month(s.Masa_Pajak_Dari) = Month(s.Masa_Pajak_Sampai), concat(MonthName(s.Masa_Pajak_Dari), ' ' , year(s.Masa_Pajak_Dari)), " +
				"concat(MonthName(s.Masa_Pajak_Dari), ' ' , year(s.Masa_Pajak_Dari), ' - ' , MonthName(s.Masa_Pajak_Sampai), ' ' , year(s.Masa_Pajak_Sampai))) " +
				"AS MasaPajak, s.Masa_Pajak_Dari, s.Masa_Pajak_Sampai, s.Pos_End FROM sptpd s WHERE (s.No_SPTPD like '%SKPD/%' or (s.No_SPTPD like '%SPTPD%' and s.Masa_Pajak_Dari < '2016-01-01')) " +
				"and s.NPWPD = '" + npwpd  + "' ORDER BY s.Masa_Pajak_Dari DESC, CAST(SUBSTRING(NO_SKP, 1, POSITION('/' in No_SKP) - 1) AS INTEGER) DESC;";
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		if(result!=null){
			try{
				List<Sptpd> listSptpd = new ArrayList<Sptpd>();
				while(result.next()){
					Sptpd sptpdModel = new Sptpd();
					sptpdModel.setNoSPTPD(result.getString("NO_SKP"));
					sptpdModel.setNoSPTPDLama(result.getString("No_SKPLama") == null ? "" : result.getString("No_SKPLama"));
					sptpdModel.setMasaPajak(result.getString("MasaPajak"));
					sptpdModel.setTanggalSPTPD(result.getDate("Tanggal_SKP"));
					sptpdModel.setMasaPajakDari(result.getDate("MASA_PAJAK_DARI"));
					sptpdModel.setMasaPajakSampai(result.getDate("MASA_PAJAK_SAMPAI"));
					sptpdModel.setPosEnd(result.getInt("Pos_End"));
					listSptpd.add(sptpdModel);
				}
				return listSptpd;
			}
			catch(SQLException e){
				MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database SQL Error", e.getMessage());
				return null;
			}
		}
		else{
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada pembuatan LOAD DATA tabel SPTPD.");
			return null;
		}
	}
	
	public List<Sptpd> getMasaPajakSPTPD(String npwpd){
		String query = "SELECT s.NPWPD, s.No_SPTPD AS No_SKP, s.No_SKPLama, s.Tgl_SPTPD AS Tanggal_SKP, s.Lokasi, " +
				"casewhen (Month(s.Masa_Pajak_Dari) = Month(s.Masa_Pajak_Sampai), concat(MonthName(s.Masa_Pajak_Dari), ' ' , year(s.Masa_Pajak_Dari)), " +
				"concat(MonthName(s.Masa_Pajak_Dari), ' ' , year(s.Masa_Pajak_Dari), ' - ' , MonthName(s.Masa_Pajak_Sampai), ' ' , year(s.Masa_Pajak_Sampai)))" +
				"AS MasaPajak, s.Masa_Pajak_Dari, s.Masa_Pajak_Sampai, s.STS, s.STSVALID FROM sptpd s WHERE s.NPWPD = '" + npwpd  + "' ORDER BY s.Masa_Pajak_Dari DESC, CAST(SUBSTRING(NO_SKP, 1, POSITION('/' in No_SKP) - 1) AS INTEGER) DESC;";
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		if(result!=null){
			try{
				List<Sptpd> listSptpd = new ArrayList<Sptpd>();
				while(result.next()){
					Sptpd sptpdModel = new Sptpd();
					sptpdModel.setNPWPD(result.getString("NPWPD"));
					sptpdModel.setNoSPTPD(result.getString("NO_SKP"));
					sptpdModel.setNoSPTPDLama(result.getString("No_SKPLama") == null ? "" : result.getString("No_SKPLama"));
					sptpdModel.setMasaPajak(result.getString("MasaPajak"));
					sptpdModel.setTanggalSPTPD(result.getDate("Tanggal_SKP"));
					sptpdModel.setMasaPajakDari(result.getDate("MASA_PAJAK_DARI"));
					sptpdModel.setMasaPajakSampai(result.getDate("MASA_PAJAK_SAMPAI"));
					sptpdModel.setLokasi(result.getString("Lokasi"));
					sptpdModel.setSts(result.getString("STS"));
					sptpdModel.setStsValid(result.getDate("STSVALID"));
					listSptpd.add(sptpdModel);
				}
				return listSptpd;
			}
			catch(SQLException e){
				MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database SQL Error", e.getMessage());
				return null;
			}
		}
		else{
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada pembuatan LOAD DATA tabel SPTPD.");
			return null;
		}
	}
	
	public List<Sptpd> getMasaPajakSKPDKB(String npwpd){
		String query = "SELECT p.NPWPD, p.No_SKP, p.No_SKPLama, p.Tanggal_SKP, p.Tanggal_Periksa, '' as Lokasi, " +
				"casewhen (Month(p.Masa_Pajak_Dari) = Month(p.Masa_Pajak_Sampai), concat(MonthName(p.Masa_Pajak_Dari), ' ' , year(p.Masa_Pajak_Dari)), " +
				"concat(MonthName(p.Masa_Pajak_Dari), ' ' , To_Char(p.Masa_Pajak_Dari, 'YY'), ' - ' , MonthName(p.Masa_Pajak_Sampai), ' ' , To_Char(p.Masa_Pajak_Sampai, 'YY'))) " +
				"AS MasaPajak, p.Masa_Pajak_Dari, p.Masa_Pajak_Sampai, p.Pos_End, p.STS, p.STSVALID FROM periksa p where p.No_SKP <> '' and p.NPWPD = '" + npwpd  + "' ORDER BY p.Masa_Pajak_Dari DESC;";
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		if(result!=null){
			try{
				List<Sptpd> listSptpd = new ArrayList<Sptpd>();
				while(result.next()){
					Sptpd sptpdModel = new Sptpd();
					sptpdModel.setNPWPD(result.getString("NPWPD"));
					sptpdModel.setNoSPTPD(result.getString("NO_SKP"));
					sptpdModel.setNoSPTPDLama(result.getString("No_SKPLama") == null ? "" : result.getString("No_SKPLama"));
					sptpdModel.setMasaPajak(result.getString("MasaPajak"));
					sptpdModel.setTanggalSPTPD(result.getDate("Tanggal_SKP"));
					sptpdModel.setTanggalNPPD(result.getDate("Tanggal_Periksa"));
					sptpdModel.setMasaPajakDari(result.getDate("MASA_PAJAK_DARI"));
					sptpdModel.setMasaPajakSampai(result.getDate("MASA_PAJAK_SAMPAI"));
					sptpdModel.setLokasi(result.getString("Lokasi"));
					sptpdModel.setPosEnd(result.getInt("Pos_End"));
					sptpdModel.setSts(result.getString("STS"));
					sptpdModel.setStsValid(result.getDate("STSVALID"));
					listSptpd.add(sptpdModel);
				}
				return listSptpd;
			}
			catch(SQLException e){
				MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database SQL Error", e.getMessage());
				return null;
			}
		}
		else{
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada pembuatan LOAD DATA tabel SPTPD.");
			return null;
		}
	}
	
	public List<Sptpd> getMasaPajakSPT(String npwpd, String noSKP){
		String query = "SELECT x.NPWPD, x.No_SKP, x.Tanggal_SKP, x.MasaPajak, x.Masa_Pajak_Dari, x.Masa_Pajak_Sampai, x.STS, x.STSVALID FROM " +
				"(SELECT s.NPWPD, s.No_SPTPD AS No_SKP, s.Tgl_SPTPD AS Tanggal_SKP, " +
				"casewhen (Month(s.Masa_Pajak_Dari) = Month(s.Masa_Pajak_Sampai), concat(MonthName(s.Masa_Pajak_Dari), ' ' , year(s.Masa_Pajak_Dari)), " +
				"concat(MonthName(s.Masa_Pajak_Dari), ' ' , year(s.Masa_Pajak_Dari), ' - ' , MonthName(s.Masa_Pajak_Sampai), ' ' , year(s.Masa_Pajak_Sampai))) AS MasaPajak, " +
				"s.Masa_Pajak_Dari, s.Masa_Pajak_Sampai, s.STS, s.STSVALID FROM sptpd s) x WHERE x.NPWPD = '" + npwpd + "' and x.No_SKP = '" + noSKP + "' ORDER BY x.Masa_Pajak_Dari DESC;";
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		if(result!=null){
			try{
				List<Sptpd> listSptpd = new ArrayList<Sptpd>();
				while(result.next()){
					Sptpd sptpdModel = new Sptpd();
					sptpdModel.setNPWPD(result.getString("NPWPD"));
					sptpdModel.setNoSPTPD(result.getString("NO_SKP"));
					sptpdModel.setMasaPajak(result.getString("MasaPajak"));
					sptpdModel.setTanggalSPTPD(result.getDate("Tanggal_SKP"));
					sptpdModel.setMasaPajakDari(result.getDate("MASA_PAJAK_DARI"));
					sptpdModel.setMasaPajakSampai(result.getDate("MASA_PAJAK_SAMPAI"));
					sptpdModel.setSts(result.getString("STS"));
					sptpdModel.setStsValid(result.getDate("STSVALID"));
					listSptpd.add(sptpdModel);
				}
				return listSptpd;
			}
			catch(SQLException e){
				MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database SQL Error", e.getMessage());
				return null;
			}
		}
		else{
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada pembuatan LOAD DATA tabel SPTPD.");
			return null;
		}
	}
	
	public List<Sptpd> getMasaPajakSKPDKB(String npwpd, String noSKP){
		String query = "SELECT x.NPWPD, x.No_SKP, x.Tanggal_SKP, x.Tanggal_Periksa, x.MasaPajak, x.Masa_Pajak_Dari, x.Masa_Pajak_Sampai, x.STS, x.STSVALID FROM " +
				"(SELECT p.NPWPD, p.No_SKP, p.Tanggal_SKP, p.Tanggal_Periksa, " +
				"casewhen (Month(p.Masa_Pajak_Dari) = Month(p.Masa_Pajak_Sampai), concat(MonthName(p.Masa_Pajak_Dari), ' ' , year(p.Masa_Pajak_Dari)), " +
				"concat(MonthName(p.Masa_Pajak_Dari), ' ' , To_Char(p.Masa_Pajak_Dari, 'YY'), ' - ' , MonthName(p.Masa_Pajak_Sampai), ' ' , To_Char(p.Masa_Pajak_Sampai, 'YY'))) AS MasaPajak, " +
				"p.Masa_Pajak_Dari, p.Masa_Pajak_Sampai, p.STS, p.STSVALID FROM periksa p where p.No_SKP <> '') x WHERE x.NPWPD = '" + npwpd + "' and x.No_SKP = '" + noSKP + "' ORDER BY x.Masa_Pajak_Dari DESC;";
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		if(result!=null){
			try{
				List<Sptpd> listSptpd = new ArrayList<Sptpd>();
				while(result.next()){
					Sptpd sptpdModel = new Sptpd();
					sptpdModel.setNPWPD(result.getString("NPWPD"));
					sptpdModel.setNoSPTPD(result.getString("NO_SKP"));
					sptpdModel.setMasaPajak(result.getString("MasaPajak"));
					sptpdModel.setTanggalSPTPD(result.getDate("Tanggal_SKP"));
					sptpdModel.setTanggalNPPD(result.getDate("Tanggal_Periksa"));
					sptpdModel.setMasaPajakDari(result.getDate("MASA_PAJAK_DARI"));
					sptpdModel.setMasaPajakSampai(result.getDate("MASA_PAJAK_SAMPAI"));
					sptpdModel.setSts(result.getString("STS"));
					sptpdModel.setStsValid(result.getDate("STSVALID"));
					listSptpd.add(sptpdModel);
				}
				return listSptpd;
			}
			catch(SQLException e){
				MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database SQL Error", e.getMessage());
				return null;
			}
		}
		else{
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada pembuatan LOAD DATA tabel SPTPD.");
			return null;
		}
	}
	
	public Sptpd getSPTPD(String NPWPD, String NoSKP)
	{
		String query = "SELECT sp.*, bu.Nama_Bid_Usaha AS Nama_Bid_Usaha FROM sptpd sp " +
						"INNER JOIN bidang_usaha bu ON bu.IdSub_Pajak = sp.IdSub_Pajak " +
						"WHERE sp.No_SPTPD = '" + NoSKP + "' AND sp.NPWPD = '" + NPWPD +"'";
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		try {
			if (result.next()){
				sptpdModel.setID(result.getInt("IDSPTPD"));
				sptpdModel.setNoSPTPD(result.getString("No_Sptpd"));
				sptpdModel.setTanggalSPTPD(result.getDate("tgl_SPTPD"));
				sptpdModel.setAssesment(result.getString("Assesment"));
				sptpdModel.setMasaPajakDari(result.getDate("Masa_Pajak_Dari"));
				sptpdModel.setMasaPajakSampai(result.getDate("Masa_Pajak_Sampai"));
				sptpdModel.setDasarPengenaan(result.getDouble("Dasar_Pengenaan"));
				sptpdModel.setTarifPajak(result.getInt("Tarif_Pajak"));
				sptpdModel.setIdSubPajak(result.getInt("IdSub_Pajak"));
				sptpdModel.setPajakTerutang(result.getDouble("Pajak_Terutang"));
				sptpdModel.setDendaTambahan(result.getDouble("Denda_Tambahan"));
				sptpdModel.setLokasi(result.getString("Lokasi"));
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sptpdModel;
	}
	
	public List<Sptpd> getAllSptpd(){
		String query = "SELECT * FROM SPTPD";
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		if(result != null){
			try{
				List<Sptpd> listSptpd = new ArrayList<Sptpd>();
				while(result.next()){
					Sptpd sptpdModel = new Sptpd();
					sptpdModel.setID(result.getInt("IDSPTPD"));
					sptpdModel.setNoNPPD(result.getString("NO_NPPD"));
					sptpdModel.setTanggalNPPD(result.getDate("TGL_NPPD"));
					sptpdModel.setNoSPTPD(result.getString("NO_SPTPD"));
					sptpdModel.setTanggalSPTPD(result.getDate("TGL_SPTPD"));
					sptpdModel.setMasaPajakDari(result.getDate("MASA_PAJAK_DARI"));
					sptpdModel.setMasaPajakSampai(result.getDate("MASA_PAJAK_SAMPAI"));
					sptpdModel.setNPWPD(result.getString("NPWPD"));
					sptpdModel.setDasarPengenaan(result.getDouble("DASAR_PENGENAAN"));
					sptpdModel.setTarifPajak(result.getInt("TARIF_PAJAK"));
					sptpdModel.setPajakTerutang(result.getDouble("PAJAK_TERUTANG"));
					sptpdModel.setDendaTambahan(result.getDouble("DENDA_TAMBAHAN"));
					sptpdModel.setIdSubPajak(result.getInt("IDSUB_PAJAK"));
					sptpdModel.setLokasi(result.getString("LOKASI"));
					sptpdModel.setAssesment(result.getString("ASSESMENT"));
					listSptpd.add(sptpdModel);
				}
				return listSptpd;
			}
			catch(SQLException e){
				MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database SQL Error", e.getMessage());
				return null;
			}
		}
		else{
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada pembuatan LOAD DATA tabel SPTPD.");
			return null;
		}
	}
	
	public boolean deleteSptpd(Integer idSptpd){
		String query = "DELETE FROM SPTPD WHERE IDSPTPD = "+idSptpd;
		boolean result = db.ExecuteStatementQuery(query,DBConnection.INSTANCE.open());
		if(!result)
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada DELETE tabel SPTPD dengan IDSPTPD = "+idSptpd);
		
		return result;
	}
	
	public Integer getLastIdSptpd(String npwpd){
		String query = "SELECT TOP 1 IDSPTPD FROM SPTPD WHERE NPWPD = '" + npwpd + "' ORDER BY IDSPTPD DESC";
		try{
			ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
			if(result.next()){
				return result.getInt("IDSPTPD");
			}
			else
				return 0;
		}
		catch(SQLException e){
			e.printStackTrace();
			return 0;
		}
	}
	
	public String getNoSPTPDExist(int id){
		String query = "Select No_SPTPD from SPTPD where IDSPTPD = " + id;
		try{
			ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
			if(result.next()){
				return result.getString("No_SPTPD");
			}
			else
				return "";
		}
		catch(SQLException e){
			e.printStackTrace();
			return "";
		}
	}
	
	public String getNoSPTPD(String tahun, Integer IDSubPajak, String Assesment)
	{
		Integer KodePajak = 0;
		String query = "select IDPajak from BIDANG_USAHA where IDSUB_PAJAK = " + IDSubPajak;
		String QuerySPT = "";
		try{
			ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
			if(result.next())
			{
				KodePajak = result.getInt("IDPajak");
				if (Assesment.equalsIgnoreCase("Self"))
				{
					if (KodePajak == 5){
						QuerySPT = "SELECT casewhen (count(No_SPTPD) > 0, CONCAT(MAX(CAST(SUBSTRING(NO_SPTPD, 1, LENGTH(NO_SPTPD) - 11) AS INTEGER)) + 1, '/SPTPD/" + tahun + "'), " +
					 "'1/SPTPD/" + tahun + "') as SPT from SPTPD where SUBSTRING(No_SPTPD, length(No_SPTPD) - 3 ,4) = '" + tahun + "' AND NO_SPTPD LIKE '%SPTPD%' "+
					 "AND IDSUB_PAJAK = " + IDSubPajak +";";
					}
					else
					{
						QuerySPT = "SELECT casewhen (count(No_SPTPD) > 0, CONCAT(MAX(CAST(SUBSTRING(NO_SPTPD, 1, LENGTH(NO_SPTPD) - 11) AS INTEGER)) + 1, '/SPTPD/" + tahun + "'), " +
								 "'1/SPTPD/" + tahun + "') as SPT from SPTPD where SUBSTRING(No_SPTPD, length(No_SPTPD) - 3 ,4) = '" + tahun + "' AND NO_SPTPD LIKE '%SPTPD%' " +
								"AND SUBSTRING(NPWPD,1,1) = " + KodePajak +";";	
					}
				}
				else if (Assesment.equalsIgnoreCase("Official"))
				{
					if (KodePajak == 5){
						QuerySPT = "SELECT casewhen (count(No_SPTPD) > 0, CONCAT(MAX(CAST(SUBSTRING(NO_SPTPD, 1, LENGTH(NO_SPTPD) - 10) AS INTEGER)) + 1, '/SKPD/" + tahun + "'), " +
					 "'1/SKPD/" + tahun + "') as SPT from SPTPD where SUBSTRING(No_SPTPD, length(No_SPTPD) - 3 ,4) = '" + tahun + "' AND NO_SPTPD LIKE '%SKPD%' "+
					 "AND IDSUB_PAJAK = " + IDSubPajak +";";
					}
					else
					{
						QuerySPT = "SELECT casewhen (count(No_SPTPD) > 0, CONCAT(MAX(CAST(SUBSTRING(NO_SPTPD, 1, LENGTH(NO_SPTPD) - 10) AS INTEGER)) + 1, '/SKPD/" + tahun + "'), " +
								 "'1/SKPD/" + tahun + "') as SPT from SPTPD where SUBSTRING(No_SPTPD, length(No_SPTPD) - 3 ,4) = '" + tahun + "' AND NO_SPTPD LIKE '%SKPD%' " +
								"AND SUBSTRING(NPWPD,1,1) = " + KodePajak +";";	
					}
				}
				ResultSet resultSPT = db.ResultExecutedStatementQuery(QuerySPT, DBConnection.INSTANCE.open());
				if(resultSPT.next())
					return resultSPT.getString("SPT");	
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
	
	public String getNoSPTPDBaru(String tahun, Integer IDSubPajak, String Assesment)
	{
//		if (IDSubPajak == 50)
//			IDSubPajak = 20;
		String bidUsaha = "";
		String query = "select Kode_Bid_Usaha from BIDANG_USAHA where IDSUB_PAJAK = " + IDSubPajak;
		String QuerySPT = "";
		try{
			ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
			if(result.next())
			{
				bidUsaha = result.getString("Kode_Bid_Usaha").replace(".", "").substring(3);
				if (Assesment.equalsIgnoreCase("Self"))
				{
					QuerySPT = "SELECT casewhen (count(No_SPTPD) > 0, CONCAT(MAX(CAST(SUBSTRING(NO_SPTPD, 1, LENGTH(NO_SPTPD) - 16) AS INTEGER)) + 1, '/" + bidUsaha + "/SPTPD/" + tahun + "'), " +
					 "'1/" + bidUsaha + "/SPTPD/" + tahun + "') as SPT from SPTPD where SUBSTRING(No_SPTPD, length(No_SPTPD) - 3, 4) = '" + tahun + "' AND NO_SPTPD LIKE '%SPTPD%' ";
					if (IDSubPajak == 20 || IDSubPajak == 50)
						QuerySPT += "AND (IDSUB_PAJAK = 20 or IDSUB_PAJAK = 50);";
					else
						QuerySPT += "AND IDSUB_PAJAK = " + IDSubPajak +";";
				}
				else if (Assesment.equalsIgnoreCase("Official"))
				{
					QuerySPT = "SELECT casewhen (count(No_SPTPD) > 0, CONCAT(MAX(CAST(SUBSTRING(NO_SPTPD, 1, LENGTH(NO_SPTPD) - 15) AS INTEGER)) + 1, '/" + bidUsaha + "/SKPD/" + tahun + "'), " +
					 "'1/" + bidUsaha + "/SKPD/" + tahun + "') as SPT from SPTPD where SUBSTRING(No_SPTPD, length(No_SPTPD) - 3, 4) = '" + tahun + "' AND NO_SPTPD LIKE '%SKPD%' "+
					 "AND IDSUB_PAJAK = " + IDSubPajak +";";
				}
				ResultSet resultSPT = db.ResultExecutedStatementQuery(QuerySPT, DBConnection.INSTANCE.open());
				if(resultSPT.next())
					return resultSPT.getString("SPT");	
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
	
	public ResultSet getDaftarSPTPD(String KodePajak, Date masaPajakDari, Date masaPajakSampai, int sortType, boolean insident, boolean sspd) throws ParseException
	{
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String masaDari = formatter.format(masaPajakDari);
		String masaSampai = formatter.format(masaPajakSampai);
		String query = "";
		ResultSet result = null;
		
		if (sspd){
			query = "Select * from getSPTPDREALISASIViews where no_sptpd like '%SPTPD%' and Substring(NPWPD, 1, 1) = '" + KodePajak +"' and " +
					"(TGL_SPTPD between Date'" + masaDari +"' and date'" + masaSampai + "') ";
			/*query = "SELECT 	SP.IDSPTPD, SP.NPWPD, WP.NAMA_BADAN, WP.NAMA_PEMILIK, WP.ALABAD_JALAN, SP.NO_SPTPD, SP.TGL_SPTPD, WP.JENIS_ASSESMENT, SP.MASA_PAJAK_DARI, SP.MASA_PAJAK_SAMPAI, SP.DASAR_PENGENAAN,"+
					"SP.TARIF_PAJAK, SP.PAJAK_TERUTANG, SP.DENDA_TAMBAHAN, SP.IDSUB_PAJAK, BU.NAMA_BID_USAHA, SS.TGL_SSPD, SS.TGL_CETAK, SS.JUMLAH_BAYAR, SP.PAJAK_TERUTANG, SS.DENDA, SS.NO_SSPD, WP.INSIDENTIL, SS.ISDENDA "+
					"FROM 	SPTPD SP "+
					"JOIN 	SSPD SS ON SP.IDSPTPD = SS.IDSPTPD "+
					"JOIN 	WAJIB_PAJAK WP ON SP.NPWPD = WP.NPWPD "+
					"JOIN	BIDANG_USAHA BU ON SP.IDSUB_PAJAK = BU.IDSUB_PAJAK "+
					"WHERE 	no_sptpd like '%SPTPD%' and Substring(NPWPD, 1, 1) = '" + KodePajak +"' and "+
					"(TGL_SPTPD between Date'" + masaDari +"' and date'" + masaSampai + "') ";*/
			System.out.println(query);
		}else{
			query = "Select * from getSPTPDViews where no_sptpd like '%SPTPD%' and Substring(NPWPD, 1, 1) = '" + KodePajak +"' and " +
					"(TGL_SPTPD between Date'" + masaDari +"' and date'" + masaSampai + "') ";
		}
		if (insident)
			query += "and INSIDENTIL = 1 ";
		query += "order by ";
		if (sortType == 1)
			query += "idSub_Pajak, ";
		else if (sortType == 2)
			query += "Tgl_SPTPD, idSub_Pajak, ";
		query += "CAST(SUBSTRING(NO_SPTPD, 1, POSITION('/' in NO_SPTPD) - 1) AS INTEGER);";
		if (sspd)
			result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.openBawah());
		else
			result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		return result;
	}
	
	public ResultSet getDaftarSPTPDSub(int idSubPajak, Date masaPajakDari, Date masaPajakSampai, boolean insident, boolean sspd) throws ParseException
	{
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String masaDari = formatter.format(masaPajakDari);
		String masaSampai = formatter.format(masaPajakSampai);
		String query = "";
		ResultSet result = null;
		
		if (sspd){
			query = "Select * from getSPTPDREALISASIViews where no_sptpd like '%SPTPD%' and idSub_Pajak = " + idSubPajak +" and " +
					"(TGL_SPTPD between Date'" + masaDari +"' and date'" + masaSampai + "') ";
		}else{
			query = "Select * from getSPTPDViews where no_sptpd like '%SPTPD%' and idSub_Pajak = " + idSubPajak +" and " +
					"(TGL_SPTPD between Date'" + masaDari +"' and date'" + masaSampai + "') ";
		}
		
		if (insident)
			query += "and INSIDENTIL = 1 ";
		query += "order by CAST(SUBSTRING(NO_SPTPD, 1, POSITION('/' in NO_SPTPD) - 1) AS INTEGER)";
		if (sspd)
			result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		else
			result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		return result;
	}
	
	public ResultSet getDaftarSKPD(String KodePajak, Date masaPajakDari, Date masaPajakSampai, int kec, int kel) throws ParseException
	{
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String masaDari = formatter.format(masaPajakDari);
		String masaSampai = formatter.format(masaPajakSampai);
		
		String query = "SELECT sp.No_SPTPD, sp.Tgl_SPTPD, To_Char(sp.Tgl_SPTPD,'YYYY') AS Tahun, wp.Nama_Badan, wp.Alabad_Jalan, sp.NPWPD, bu.Kode_Bid_Usaha, " +
				"bu.Nama_Bid_Usaha AS NamaPajak, sp.Dasar_Pengenaan, sp.Pajak_Terutang, CAST(0 AS DECIMAL(12, 2)) AS Bunga, CAST(0 AS DECIMAL(12, 2)) AS Kenaikan, " +
				"sp.Denda_Tambahan AS Denda, (sp.Pajak_Terutang + sp.Denda_Tambahan) AS Total, sp.IdSub_Pajak, sp.Masa_Pajak_Dari, sp.Masa_Pajak_Sampai, " +
				"DATE_ADD(sp.Tgl_SPTPD, INTERVAL 1 MONTH) AS TanggalJatuhTempo, " +
				"casewhen (Month(sp.Masa_Pajak_Dari) != Month(sp.Masa_Pajak_Sampai) or Year(sp.Masa_Pajak_Dari) != Year(sp.Masa_Pajak_Sampai), " +
				"concat(sp.Masa_Pajak_Dari, ' - ',sp.Masa_Pajak_Sampai), concat(sp.Masa_Pajak_Dari, '')) AS MasaPajak, sp.Lokasi from sptpd sp " +
				"INNER JOIN bidang_usaha bu ON sp.IdSub_Pajak = bu.IdSub_Pajak INNER JOIN wajib_pajak wp ON wp.NPWPD = sp.NPWPD " +
				"INNER JOIN KECAMATAN kc ON kc.KODE_KECAMATAN = wp.Alabad_Kecamatan INNER JOIN KELURAHAN kl on kl.KODE_KELURAHAN = wp.Alabad_Kelurahan AND kl.KECAMATAN_IDKECAMATAN = wp.Alabad_Kecamatan " +
				"where no_sptpd like '%SKPD/%' and Substring(NPWPD, 1, 1) = '" + KodePajak + "' and (TGL_SPTPD between Date'" + masaDari + "' and date'" + masaSampai + "') ";
		if (kel > 0)
			query += "AND (kl.Kode_Kelurahan = " + kel + " AND kl.Kecamatan_IdKecamatan = " + kec + ") ";
		else if (kec > 0) 
			query += "AND kc.Kode_Kecamatan = " + kec + " ";
		
		query += "order by CAST(SUBSTRING(NO_SPTPD, 1, POSITION('/' in NO_SPTPD) - 1) AS INTEGER);";
		
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		return result;
	}
	
	public ResultSet getDaftarSKPDSSPD(String KodePajak, Date masaPajakDari, Date masaPajakSampai, int kec, int kel) throws ParseException
	{
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String masaDari = formatter.format(masaPajakDari);
		String masaSampai = formatter.format(masaPajakSampai);
		
		String query = "SELECT sp.No_SPTPD, sp.Tgl_SPTPD, To_Char(sp.Tgl_SPTPD,'YYYY') AS Tahun, wp.Nama_Badan, wp.Alabad_Jalan, sp.NPWPD, bu.Kode_Bid_Usaha, " +
				"bu.Nama_Bid_Usaha AS NamaPajak, sp.Dasar_Pengenaan, sp.Pajak_Terutang, CAST(0 AS DECIMAL(12, 2)) AS Bunga, CAST(0 AS DECIMAL(12, 2)) AS Kenaikan, " +
				"sp.Denda_Tambahan AS Denda, (sp.Pajak_Terutang + sp.Denda_Tambahan) AS Total, sp.IdSub_Pajak, sp.Masa_Pajak_Dari, sp.Masa_Pajak_Sampai, " +
				"DATE_ADD(sp.Tgl_SPTPD, INTERVAL 1 MONTH) AS TanggalJatuhTempo, " +
				"casewhen (Month(sp.Masa_Pajak_Dari) != Month(sp.Masa_Pajak_Sampai) or Year(sp.Masa_Pajak_Dari) != Year(sp.Masa_Pajak_Sampai), " +
				"concat(sp.Masa_Pajak_Dari, ' - ',sp.Masa_Pajak_Sampai), concat(sp.Masa_Pajak_Dari, '')) AS MasaPajak, sp.Lokasi, ss.Jumlah_Bayar, ss.Denda as DendaSSPD, ss.Tgl_SSPD, " +
				"ss.Tgl_Cetak, ss.No_SSPD from sptpd sp INNER JOIN bidang_usaha bu ON sp.IdSub_Pajak = bu.IdSub_Pajak " +
				"INNER JOIN wajib_pajak wp ON wp.NPWPD = sp.NPWPD Left Join SSPD ss on ss.IDSPTPD = sp.IDSPTPD " +
				"INNER JOIN KECAMATAN kc ON kc.KODE_KECAMATAN = wp.Alabad_Kecamatan INNER JOIN KELURAHAN kl on kl.KODE_KELURAHAN = wp.Alabad_Kelurahan AND kl.KECAMATAN_IDKECAMATAN = wp.Alabad_Kecamatan " +
				"where no_sptpd like '%SKPD/%' and Substring(NPWPD, 1, 1) = '" + KodePajak + "' and (TGL_SPTPD between Date'" + masaDari + "' and date'" + masaSampai + "') ";
		if (kel > 0)
			query += "AND (kl.Kode_Kelurahan = " + kel + " AND kl.Kecamatan_IdKecamatan = " + kec + ") ";
		else if (kec > 0) 
			query += "AND kc.Kode_Kecamatan = " + kec + " ";
		
		query += "order by CAST(SUBSTRING(NO_SPTPD, 1, POSITION('/' in NO_SPTPD) - 1) AS INTEGER);";
		
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		return result;
	}
	
	public ResultSet getNotaSKPD(String KodePajak, Date masaPajakDari, Date masaPajakSampai) throws ParseException
	{
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String masaDari = formatter.format(masaPajakDari);
		String masaSampai = formatter.format(masaPajakSampai);
		
		String query = "select min(CAST(SUBSTRING(NO_SPTPD, 1, POSITION('/' in NO_SPTPD) - 1) AS INTEGER)) as minNo, max(CAST(SUBSTRING(NO_SPTPD, 1, POSITION('/' in NO_SPTPD) - 1) AS INTEGER)) as maxNo, " +
				"min(bu.Nama_Bid_Usaha) as bidangUsaha, count(*) as JumlahWP, sum(Pajak_Terutang) as JumlahPajak " +
				"from sptpd sp INNER JOIN bidang_usaha bu ON sp.IdSub_Pajak = bu.IdSub_Pajak INNER JOIN wajib_pajak wp ON wp.NPWPD = sp.NPWPD " +
				"where no_sptpd like '%SKPD/%' and Substring(NPWPD, 1, 1) = '" + KodePajak + "' and (TGL_SPTPD between Date'" + masaDari + "' and date'" + masaSampai + "') " +
				"group by idsub_pajak order by idsub_pajak;";
		
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		return result;
	}
	
	public ResultSet getNotaSKPDKB(String KodePajak, Date masaPajakDari, Date masaPajakSampai, String tipe) throws ParseException
	{
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String masaDari = formatter.format(masaPajakDari);
		String masaSampai = formatter.format(masaPajakSampai);
		
		String query = "select min(CAST(SUBSTRING(NO_SKP, 1, POSITION('/' in NO_SKP) - 1) AS INTEGER)) as minNo, max(CAST(SUBSTRING(NO_SKP, 1, POSITION('/' in NO_SKP) - 1) AS INTEGER)) as maxNo, " +
				"min(bu.Nama_Bid_Usaha) as bidangUsaha, count(*) as JumlahWP, sum(p.Total_Pajak_Periksa - p.Total_Pajak_Terutang + p.Total_Pajak_Bunga + p.Kenaikan + p.Total_Kenaikan) as JumlahPajak " +
				"from periksa p INNER JOIN bidang_usaha bu ON p.IdSub_Pajak = bu.IdSub_Pajak INNER JOIN wajib_pajak wp ON wp.NPWPD = p.NPWPD " +
				"where no_skp like '%" + tipe + "/%' and Substring(NPWPD, 1, 1) = '" + KodePajak + "' and (TANGGAL_SKP between Date'" + masaDari + "' and date'" + masaSampai + "') " +
				"group by idsub_pajak order by idsub_pajak;";
		
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		return result;
	}
	
	public ResultSet getNotaSKPDLBCov(String KodePajak, Date masaPajakDari, Date masaPajakSampai, String tipe) throws ParseException
	{
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String masaDari = formatter.format(masaPajakDari);
		String masaSampai = formatter.format(masaPajakSampai);
		
		String query =  "select count(bu.NAMA_BID_USAHA) as jumlahwp, bu.nama_bid_usaha, sum(cast(s.denda as integer)) as denda " +
						"from skpdlb slb join sspd s on slb.IDSSPD = s.IDSSPD join bidang_usaha bu on bu.idsub_pajak = s.IDSUB_PAJAK join SPTPD sp on slb.IDSPTPD = sp.IDSPTPD " + 
						"where bu.IDPAJAK = "+KodePajak+" and slb.TANGGAL >= '"+masaDari+"' AND slb.TANGGAL <= '"+masaSampai+"' " +
						"group by bu.NAMA_BID_USAHA;";
		//System.out.println(query);
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		return result;
	}

	@SuppressWarnings("deprecation")
	public List<Sptpd> getMasaPeriksaDari(){
		Date dt = new Date();

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.MONTH, dt.getMonth());
		cal.set(Calendar.YEAR, dt.getYear() + 1900);
		java.sql.Date startDate = new java.sql.Date(2006 - 1900, 11, 1);
		java.sql.Date endDate = new java.sql.Date(cal.get(Calendar.YEAR) - 1900, cal.get(Calendar.MONTH), 1);
		java.sql.Date sampai = new java.sql.Date(cal.get(Calendar.YEAR) - 1900, cal.get(Calendar.MONTH), cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		List<Sptpd> listSptpd = new ArrayList<Sptpd>();
		while(endDate.compareTo(startDate) > 0){
			Sptpd sptpdModel = new Sptpd();
			sptpdModel.setMasaPajak(new SimpleDateFormat("MMMM yyyy").format(cal.getTime()));
			sptpdModel.setMasaPajakDari(endDate);
			sptpdModel.setMasaPajakSampai(sampai);
					
			listSptpd.add(sptpdModel);
			cal.add(Calendar.MONTH, -1);
			endDate = new java.sql.Date(cal.get(Calendar.YEAR) - 1900, cal.get(Calendar.MONTH), 1);
			sampai = new java.sql.Date(cal.get(Calendar.YEAR) - 1900, cal.get(Calendar.MONTH), cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		}
		return listSptpd;
	}
	
	@SuppressWarnings("deprecation")
	public List<Sptpd> getMasaPeriksaSampai(Date pajakDari){
		Date dt = new Date();

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.MONTH, dt.getMonth());
		cal.set(Calendar.YEAR, dt.getYear() + 1900);
		java.sql.Date startDate = new java.sql.Date(pajakDari.getYear(), pajakDari.getMonth() - 1, 1);
		java.sql.Date endDate = new java.sql.Date(cal.get(Calendar.YEAR) - 1900, cal.get(Calendar.MONTH), 1);
		java.sql.Date sampai = new java.sql.Date(cal.get(Calendar.YEAR) - 1900, cal.get(Calendar.MONTH), cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		List<Sptpd> listSptpd = new ArrayList<Sptpd>();
		while(endDate.compareTo(startDate) > 0){
			Sptpd sptpdModel = new Sptpd();
			sptpdModel.setMasaPajak(new SimpleDateFormat("MMMM yyyy").format(cal.getTime()));
			sptpdModel.setMasaPajakDari(endDate);
			sptpdModel.setMasaPajakSampai(sampai);
					
			listSptpd.add(sptpdModel);
			cal.add(Calendar.MONTH, -1);
			endDate = new java.sql.Date(cal.get(Calendar.YEAR) - 1900, cal.get(Calendar.MONTH), 1);
			sampai = new java.sql.Date(cal.get(Calendar.YEAR) - 1900, cal.get(Calendar.MONTH), cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		}
		return listSptpd;
	}
	
	public String getNoSTS(int id){
		String query = "Select STS from SPTPD where idSptpd = " + id;
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
	
	public String getNoSTSUntukSPTPDTEMP(String NoSPTPD){
		String query = "Select STS from SPTPD where NO_SPTPD = '" + NoSPTPD +"'";
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
	
	public String getNoSTSAngsuran(int idMohon, int cicilanKe){
		String query = "Select STS from Mohon_Detail where idMohon = " + idMohon + " and No_Angsuran = " + cicilanKe;
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
	
	public List<Sptpd> getSptpdTemp(){
		String query = "SELECT st.*, wp.NAMA_BADAN, wp.NAMA_PEMILIK, wp.ALABAD_JALAN, bu.NAMA_BID_USAHA " +
				"FROM SPTPD_TEMP st join WAJIB_PAJAK wp on st.NPWPD = wp.NPWPD " +
				"join BIDANG_USAHA bu on st.IDSUB_PAJAK = bu.IDSUB_PAJAK " +
				"where status = 0 " +
				"order by IDSPTPDTEMP DESC";
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		if(result!=null){
			try{
				List<Sptpd> listSptpd = new ArrayList<Sptpd>();
				while(result.next()){
					Sptpd sptpdModel = new Sptpd();
					sptpdModel.setID(result.getInt("IDSPTPDTEMP"));
					sptpdModel.setNamaBadan(result.getString("NAMA_BADAN"));
					sptpdModel.setNamaPemilik(result.getString("NAMA_PEMILIK"));
					sptpdModel.setAlamat(result.getString("ALABAD_JALAN"));
					sptpdModel.setBidangUsaha(result.getString("NAMA_BID_USAHA"));
					sptpdModel.setNoNPPD(result.getString("NO_NPPD"));
					sptpdModel.setTanggalNPPD(result.getDate("TGL_NPPD"));
					sptpdModel.setNoSPTPD(result.getString("NO_SPTPD"));
					sptpdModel.setTanggalSPTPD(result.getDate("TGL_SPTPD"));
					sptpdModel.setMasaPajakDari(result.getDate("MASA_PAJAK_DARI"));
					sptpdModel.setMasaPajakSampai(result.getDate("MASA_PAJAK_SAMPAI"));
					sptpdModel.setNPWPD(result.getString("NPWPD"));
					sptpdModel.setDasarPengenaan(result.getDouble("DASAR_PENGENAAN"));
					sptpdModel.setTarifPajak(result.getInt("TARIF_PAJAK"));
					sptpdModel.setPajakTerutang(result.getDouble("PAJAK_TERUTANG"));
					sptpdModel.setDendaTambahan(result.getDouble("DENDA_TAMBAHAN"));
					sptpdModel.setIdSubPajak(result.getInt("IDSUB_PAJAK"));
					sptpdModel.setLokasi(result.getString("LOKASI"));
					sptpdModel.setAssesment(result.getString("ASSESMENT"));
					sptpdModel.setRekapHarian(result.getString("REKAP_HARIAN"));
					sptpdModel.setIsBNI(result.getBoolean("IS_BNI"));
					sptpdModel.setNilaiRevisiBNI(result.getString("NILAI_REVISI"));
				
					sptpdModel.setNoSPTPDLama(result.getString("No_SKPLama") == null? "" : result.getString("No_SKPLama"));
					listSptpd.add(sptpdModel);
				}
				return listSptpd;
			}
			catch(SQLException e){
				MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database SQL Error", e.getMessage());
				return null;
			}
		}
		else{
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada pembuatan LOAD DATA tabel SPTPD_TEMP.");
			return null;
		}
		
	}
	
	public boolean tolakSptpdTemp(int IDSPTPDTEMP, String Keterangan) {
		String tanggal = String.valueOf(Calendar.getInstance().get(Calendar.YEAR)+"-"+(Calendar.getInstance().get(Calendar.MONTH)+1)+"-"+Calendar.getInstance().get(Calendar.DATE));
		String query = "UPDATE SPTPD_TEMP SET STATUS = 2, KETERANGAN = '"+Keterangan+"', TGL_RESPON = '"+tanggal+"' WHERE IDSPTPDTEMP = '"+IDSPTPDTEMP+"';";
		boolean result = db.ExecuteStatementQuery(query, DBConnection.INSTANCE.open());
		if(!result)
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada (UPDATE) tabel SPTPD_TEMP.");
		return result;
	}

	public boolean terimaSptpdTemp(int IDSPTPDTEMP, String NoSPTPD, String Keterangan) {
		String tanggal = String.valueOf(Calendar.getInstance().get(Calendar.YEAR)+"-"+(Calendar.getInstance().get(Calendar.MONTH)+1)+"-"+Calendar.getInstance().get(Calendar.DATE));
		String query = "UPDATE SPTPD_TEMP SET STATUS = 1, NO_SPTPD = '"+NoSPTPD+"', KETERANGAN = '"+Keterangan+"', TGL_RESPON = '"+tanggal+"', PERLU_PEMERIKSAAN = FALSE WHERE IDSPTPDTEMP = '"+IDSPTPDTEMP+"';";
		boolean result = db.ExecuteStatementQuery(query, DBConnection.INSTANCE.open());
		if(!result)
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada (UPDATE) tabel SPTPD_TEMP.");
		return result;
	}
	
	public boolean terimaSptpdTempBersyarat(int IDSPTPDTEMP, String NoSPTPD, String Keterangan) {
		String tanggal = String.valueOf(Calendar.getInstance().get(Calendar.YEAR)+"-"+(Calendar.getInstance().get(Calendar.MONTH)+1)+"-"+Calendar.getInstance().get(Calendar.DATE));
		String query = "UPDATE SPTPD_TEMP SET STATUS = 1, NO_SPTPD = '"+NoSPTPD+"', KETERANGAN = '"+Keterangan+"', TGL_RESPON = '"+tanggal+"', PERLU_PEMERIKSAAN = TRUE WHERE IDSPTPDTEMP = '"+IDSPTPDTEMP+"';";
		boolean result = db.ExecuteStatementQuery(query, DBConnection.INSTANCE.open());
		if(!result)
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada (UPDATE) tabel SPTPD_TEMP.");
		return result;
	}

	public boolean periksaSptpdTemp(int IDSPTPDTEMP) throws SQLException {
		// TODO Auto-generated method stub
		String query = "SELECT * FROM SPTPD_TEMP WHERE STATUS = 0 AND IDSPTPDTEMP = '"+IDSPTPDTEMP+"';";
		ResultSet result = db.ResultPreparedStatementQuery(query, DBConnection.INSTANCE.open());
		if (result.next()){
			return true;
		}
		else{
			return false;
		}
	}

	public boolean cekBank(int IDSPTPDTEMP) {
		String query = "SELECT IS_BNI FROM SPTPD_TEMP WHERE IDSPTPDTEMP = '"+IDSPTPDTEMP+"';";
		ResultSet result = db.ResultPreparedStatementQuery(query, DBConnection.INSTANCE.open());
		if (result.equals(true)){
			return true;
		}
		else{
			return false;
		}
	}
	
	public List<Sptpd> getSptpdTempList(java.util.Date masaPajakDari,
			java.util.Date masaPajakSampai) {
		// TODO Auto-generated method stub
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String masaDari = formatter.format(masaPajakDari);
		String masaSampai = formatter.format(masaPajakSampai);
		String query = "SELECT st.*, wp.NAMA_BADAN, wp.ALABAD_JALAN FROM SPTPD_TEMP st JOIN WAJIB_PAJAK wp ON st.NPWPD = wp.NPWPD "+
						"WHERE STATUS != 0 AND TGL_RESPON >= '" + masaDari + "' AND TGL_RESPON <= '" + masaSampai +"' "+
						"ORDER BY IDSPTPDTEMP DESC;";
		//System.out.println(query);
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		if(result!=null){
			try{
				List<Sptpd> listSptpd = new ArrayList<Sptpd>();
				while(result.next()){
					Sptpd sptpdModel = new Sptpd();
					sptpdModel.setID(result.getInt("IDSPTPDTEMP"));
					sptpdModel.setNoSPTPD(result.getString("NO_SPTPD"));
					sptpdModel.setTanggalSPTPD(result.getDate("TGL_SPTPD"));
					sptpdModel.setMasaPajakDari(result.getDate("MASA_PAJAK_DARI"));
					sptpdModel.setMasaPajakSampai(result.getDate("MASA_PAJAK_SAMPAI"));
					sptpdModel.setNPWPD(result.getString("NPWPD"));
					sptpdModel.setDasarPengenaan(result.getDouble("DASAR_PENGENAAN"));
					sptpdModel.setTarifPajak(result.getInt("TARIF_PAJAK"));
					sptpdModel.setPajakTerutang(result.getDouble("PAJAK_TERUTANG"));
					sptpdModel.setDendaTambahan(result.getDouble("DENDA_TAMBAHAN"));
					sptpdModel.setIdSubPajak(result.getInt("IDSUB_PAJAK"));
					sptpdModel.setAssesment(result.getString("ASSESMENT"));
					sptpdModel.setStatus(result.getString("STATUS"));
					sptpdModel.setKeterangan(result.getString("KETERANGAN"));
					sptpdModel.setNamaBadan(result.getString("NAMA_BADAN"));
					sptpdModel.setAlamat(result.getString("ALABAD_JALAN"));
					sptpdModel.setNoSPTPDLama(result.getString("No_SKPLama") == null? "" : result.getString("No_SKPLama"));
					listSptpd.add(sptpdModel);
				}
				return listSptpd;
			}
			catch(SQLException e){
				MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database SQL Error", e.getMessage());
				return null;
			}
		}
		else{
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada pembuatan LOAD DATA tabel SPTPD_TEMP.");
			return null;
		}
	}

	public boolean insertDariSptpdTemp(int IDSPTPDTEMP) {
		// TODO Auto-generated method stub
		String query = "INSERT INTO SPTPD " +
				"(IDSPTPD,NO_NPPD,TGL_NPPD,NO_SPTPD,TGL_SPTPD,MASA_PAJAK_DARI,MASA_PAJAK_SAMPAI,NPWPD,DASAR_PENGENAAN,TARIF_PAJAK,PAJAK_TERUTANG,DENDA_TAMBAHAN,IDSUB_PAJAK," +
				"LOKASI,ASSESMENT,NO_SKPLAMA,POS_START,POS_END,FILE_REKAP,REKAP_HARIAN) " +
				"SELECT " +
				"NULL,'INSERT',ST.TGL_NPPD,'INSERT',ST.TGL_SPTPD,ST.MASA_PAJAK_DARI,ST.MASA_PAJAK_SAMPAI,ST.NPWPD,ST.DASAR_PENGENAAN,ST.TARIF_PAJAK,ST.PAJAK_TERUTANG,ST.DENDA_TAMBAHAN,ST.IDSUB_PAJAK," +
				"ST.LOKASI,ST.ASSESMENT,ST.NO_SKPLAMA,ST.POS_START,ST.POS_END,ST.FILE_REKAP,ST.REKAP_HARIAN " +
				"FROM SPTPD_TEMP ST " +
				"WHERE IDSPTPDTEMP = '"+IDSPTPDTEMP+"'";
		boolean result = db.ExecuteStatementQuery(query, DBConnection.INSTANCE.open());
		if(!result)
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada (INSERT) tabel SPTPD.");
		return result;
	}
	
	public boolean insertDariSptpdTempPembetulan(int IDSPTPDTEMP,double pembtlnDasarPengenaan, double pembtlnPajakTerutang) {
		// TODO Auto-generated method stub
		String query = "INSERT INTO SPTPD " +
				"(IDSPTPD,NO_NPPD,TGL_NPPD,NO_SPTPD,TGL_SPTPD,MASA_PAJAK_DARI,MASA_PAJAK_SAMPAI,NPWPD,DASAR_PENGENAAN,TARIF_PAJAK,PAJAK_TERUTANG,DENDA_TAMBAHAN,IDSUB_PAJAK," +
				"LOKASI,ASSESMENT,NO_SKPLAMA,POS_START,POS_END,FILE_REKAP,REKAP_HARIAN) " +
				"SELECT " +
				"NULL,'INSERT',ST.TGL_NPPD,'INSERT',ST.TGL_SPTPD,ST.MASA_PAJAK_DARI,ST.MASA_PAJAK_SAMPAI,ST.NPWPD,'"+pembtlnDasarPengenaan+"',ST.TARIF_PAJAK,'"+pembtlnPajakTerutang+"',ST.DENDA_TAMBAHAN,ST.IDSUB_PAJAK," +
				"ST.LOKASI,ST.ASSESMENT,ST.NO_SKPLAMA,ST.POS_START,ST.POS_END,ST.FILE_REKAP,ST.NILAI_REVISI " +
				"FROM SPTPD_TEMP ST " +
				"WHERE IDSPTPDTEMP = '"+IDSPTPDTEMP+"'";
		boolean result = db.ExecuteStatementQuery(query, DBConnection.INSTANCE.open());
		if(!result)
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada (INSERT) tabel SPTPD.");
		return result;
	}
	
	public String getNamaFileLampiran(int IDSPTPDTEMP) throws SQLException{
		String query = "SELECT FILE_REKAP FROM SPTPD_TEMP WHERE IDSPTPDTEMP = "+IDSPTPDTEMP+";";
		//System.out.println(query)
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		if (result.next()){
			return result.getString("FILE_REKAP");
		}
		else{
			return "";
		}
	}
}
