package com.dispenda.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;

import com.dispenda.database.DBConnection;
import com.dispenda.database.DBOperation;
import com.dispenda.model.Sptpd;

public class LogImp {

	private DBOperation db = new DBOperation();
	
	public boolean setLog(String s,String displayName, String subModule){
		String query = "INSERT INTO LOG (IDLOG, ACTION, TRANSACTION, DISPLAYNAME, SUBMODULNAME) " +
				"VALUES (NULL, '"+s+"', '"+new CPWajibPajakDAOImpl().getDateNow()+"', '"+displayName+"', '"+subModule+"');";
		boolean result = db.ExecuteStatementQuery(query, DBConnection.INSTANCE.open());
		if(!result)
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Hubungi Admin.");
		return result;
	}
	
	public String[] getLogKodefikasiOld(String searchParam){
		String retValue[] = {};
		int count = 0;
		String query = "select ACTION from LOG where SUBMODULNAME = 'Kodefikasi' and Action like '%" + searchParam + "%'";
		try {
			ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
			
			if(result.last()){
				retValue = new String[result.getRow()];
				result.beforeFirst();
			}
			while (result.next())
			{
				retValue[count] = result.getString("ACTION");
				count++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return retValue;
	}
	
	public List<Sptpd> getLogKodefikasibyIDPajak(String tipeSkp, String idPajak){
		String query = "";
		if (tipeSkp.equalsIgnoreCase("SPTPD") || tipeSkp.equalsIgnoreCase("SKPD")){
			query = "select npwpd, no_skplama, no_sptpd as no_skp, no_nppd, tgl_sptpd as tanggal_skp, masa_pajak_dari, masa_pajak_sampai from SPTPD where substring(NPWPD, 1, 1) = '" + idPajak + "' " +
					"and NO_SKPLAMA is not null order by idsub_pajak, CAST(SUBSTRING(NO_SKP, 1, POSITION('/' in No_SKP) - 1) AS INTEGER);";
		} else {
			query = "select npwpd, no_skplama, no_skp, no_nppd, tanggal_skp, masa_pajak_dari, masa_pajak_sampai from Periksa where substring(NPWPD, 1, 1) = '" + idPajak + "' " +
					"and NO_SKPLAMA is not null order by idsub_pajak, CAST(SUBSTRING(NO_SKP, 1, POSITION('/' in No_SKP) - 1) AS INTEGER);";
		}
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		if(result!=null){
			try{
				List<Sptpd> listSptpd = new ArrayList<Sptpd>();
				while(result.next()){
					Sptpd sptpdModel = new Sptpd();
					sptpdModel.setNPWPD(result.getString("NPWPD"));
					sptpdModel.setNoSPTPD(result.getString("NO_SKP"));
					sptpdModel.setNoSPTPDLama(result.getString("No_SKPLama"));
					sptpdModel.setNoNPPD(result.getString("No_NPPD"));
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
	
	public List<Sptpd> getLogKodefikasibyIDSubPajak(String tipeSkp, int idSubPajak){
		String query = "";
		if (tipeSkp.equalsIgnoreCase("SPTPD") || tipeSkp.equalsIgnoreCase("SKPD")){
			query = "select npwpd, no_skplama, no_sptpd as no_skp, no_nppd, tgl_sptpd as tanggal_skp, masa_pajak_dari, masa_pajak_sampai from SPTPD where idsub_pajak = " + idSubPajak + " " +
					"and NO_SKPLAMA is not null order by CAST(SUBSTRING(NO_SKP, 1, POSITION('/' in No_SKP) - 1) AS INTEGER);";
		} else {
			query = "select npwpd, no_skplama, no_skp, no_nppd, tanggal_skp, masa_pajak_dari, masa_pajak_sampai from Periksa where idsub_pajak = " + idSubPajak + " " +
					"and NO_SKPLAMA is not null order by CAST(SUBSTRING(NO_SKP, 1, POSITION('/' in No_SKP) - 1) AS INTEGER);";
		}
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		if(result!=null){
			try{
				List<Sptpd> listSptpd = new ArrayList<Sptpd>();
				while(result.next()){
					Sptpd sptpdModel = new Sptpd();
					sptpdModel.setNPWPD(result.getString("NPWPD"));
					sptpdModel.setNoSPTPD(result.getString("NO_SKP"));
					sptpdModel.setNoSPTPDLama(result.getString("No_SKPLama"));
					sptpdModel.setNoNPPD(result.getString("No_NPPD"));
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
	
	public List<Sptpd> getLogKodefikasiAll(String tipeSkp){
		String query = "";
		if (tipeSkp.equalsIgnoreCase("SPTPD") || tipeSkp.equalsIgnoreCase("SKPD")){
			query = "select npwpd, no_skplama, no_sptpd as no_skp, no_nppd, tgl_sptpd as tanggal_skp, masa_pajak_dari, masa_pajak_sampai " +
					"from SPTPD where NO_SKPLAMA is not null order by idsub_pajak, CAST(SUBSTRING(NO_SKP, 1, POSITION('/' in No_SKP) - 1) AS INTEGER);";
		} else {
			query = "select npwpd, no_skplama, no_skp, no_nppd, tanggal_skp, masa_pajak_dari, masa_pajak_sampai from Periksa where NO_SKPLAMA is not null " +
					"order by idsub_pajak, CAST(SUBSTRING(NO_SKP, 1, POSITION('/' in No_SKP) - 1) AS INTEGER);";
		}
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		if(result!=null){
			try{
				List<Sptpd> listSptpd = new ArrayList<Sptpd>();
				while(result.next()){
					Sptpd sptpdModel = new Sptpd();
					sptpdModel.setNPWPD(result.getString("NPWPD"));
					sptpdModel.setNoSPTPD(result.getString("NO_SKP"));
					sptpdModel.setNoSPTPDLama(result.getString("No_SKPLama"));
					sptpdModel.setNoNPPD(result.getString("No_NPPD"));
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
	
	public String[] getCariKodefikasiOld(String searchParam){
		String retValue[] = {};
		int count = 0;
		String query = "select ACTION from LOG where SUBMODULNAME = 'Kodefikasi' and Action like '%NPWPD: " + searchParam + "%' or " +
				"Action like '%NoSKP: " + searchParam + "%' or Action like '%- " + searchParam + "%'";
		try {
			ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
			
			if(result.last()){
				retValue = new String[result.getRow()];
				result.beforeFirst();
			}
			while (result.next())
			{
				retValue[count] = result.getString("ACTION");
				count++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return retValue;
	}
	
	public List<Sptpd> getCariKodefikasiNew(String searchParam){
		String query = "Select x.idsub_pajak, x.NPWPD, x.No_SKP, x.No_SKPLama, x.No_NPPD, x.Tanggal_SKP, x.Masa_Pajak_Dari, x.Masa_Pajak_Sampai from ((select idsub_pajak, npwpd, no_skplama, no_sptpd as no_skp, no_nppd, tgl_sptpd as tanggal_skp, masa_pajak_dari, masa_pajak_sampai from SPTPD " +
				"where NO_SKPLAMA is not null) UNION " +
				"(select idsub_pajak, npwpd, no_skplama, no_skp, no_nppd, tanggal_skp, masa_pajak_dari, masa_pajak_sampai from Periksa where NO_SKPLAMA is not null)) x where " +
//				"and NPWPD like '%" + searchParam + "%' or No_SKP like '%" + searchParam + "%' or No_SKPLama like '%" + searchParam + "%') x where " +
				"x.NPWPD like '%" + searchParam + "%' or x.No_SKP like '" + searchParam + "%' or x.No_SKPLama like '" + searchParam + "%' " +
				"order by x.idsub_pajak, CAST(SUBSTRING(x.NO_SKP, 1, POSITION('/' in x.No_SKP) - 1) AS INTEGER);";
		
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		if(result!=null){
			try{
				List<Sptpd> listSptpd = new ArrayList<Sptpd>();
				while(result.next()){
					Sptpd sptpdModel = new Sptpd();
					sptpdModel.setNPWPD(result.getString("NPWPD"));
					sptpdModel.setNoSPTPD(result.getString("NO_SKP"));
					sptpdModel.setNoSPTPDLama(result.getString("No_SKPLama"));
					sptpdModel.setNoNPPD(result.getString("No_NPPD"));
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
}