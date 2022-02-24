package com.dispenda.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;

import com.dispenda.model.BidangUsaha;
import com.dispenda.database.DBConnection;
import com.dispenda.database.DBOperation;

public class CPBidangUsahaDAOImpl {
	private DBOperation db = new DBOperation();
	
	public boolean saveBidangUsaha(BidangUsaha bidUsahaModel){
		String query = "MERGE INTO BIDANG_USAHA USING (VALUES(" +
				""+bidUsahaModel.getIdSubPajak()+"," +
				"'"+bidUsahaModel.getKodeBidUsaha()+"'," +
				"'"+bidUsahaModel.getNamaBidUsaha()+"'," +
				""+bidUsahaModel.getIdPajak()+"," +
				""+bidUsahaModel.getTarif()+"," +
				""+bidUsahaModel.getBunga()+"," +
				""+bidUsahaModel.getDenda()+"," +
				""+bidUsahaModel.getKenaikan1()+"," +
				""+bidUsahaModel.getKenaikan2()+"," +
				""+bidUsahaModel.getDLT()+"))" +
			" AS vals(" +
				"idsub_pajak," +
				"kodebid_usaha," +
				"namabid_usaha," +
				"id_pajak," +
				"tarif," +
				"bunga," +
				"denda," +
				"kenaikan_1," +
				"kenaikan_2," +
				"dlt)" +
			" ON BIDANG_USAHA.IDSUB_PAJAK = vals.idsub_pajak" +
			" WHEN MATCHED THEN UPDATE SET " +
				"BIDANG_USAHA.KODE_BID_USAHA = vals.kodebid_usaha," +
				"BIDANG_USAHA.NAMA_BID_USAHA = vals.namabid_usaha," +
				"BIDANG_USAHA.IDPAJAK = vals.id_pajak," +
				"BIDANG_USAHA.TARIF = vals.tarif," +
				"BIDANG_USAHA.BUNGA = vals.bunga," +
				"BIDANG_USAHA.DENDA = vals.denda," +
				"BIDANG_USAHA.KENAIKAN_1 = vals.kenaikan_1," +
				"BIDANG_USAHA.KENAIKAN_2 = vals.kenaikan_2," +
				"BIDANG_USAHA.DLT = vals.dlt" +
			" WHEN NOT MATCHED THEN INSERT VALUES " +
				"vals.idsub_pajak," +
				"vals.kodebid_usaha," +
				"vals.namabid_usaha," +
				"vals.id_pajak," +
				"vals.tarif," +
				"vals.bunga," +
				"vals.denda," +
				"vals.kenaikan_1," +
				"vals.kenaikan_2," +
				"vals.dlt" ;
		boolean result = db.ExecuteStatementQuery(query, DBConnection.INSTANCE.open());
		if(!result)
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada MERGE(UPDATE/INSERT) tabel BIDANG_USAHA.");
		return result;
	}
	
	public List<BidangUsaha> getBidangUsaha(Integer idPajak){
		String query = "SELECT * FROM BIDANG_USAHA WHERE IDPAJAK = "+idPajak+" AND DLT = 0 order by idSub_Pajak";
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		if(result != null){
			try{
				List<BidangUsaha> listBidangUsaha = new ArrayList<BidangUsaha>();
				while(result.next()){
					BidangUsaha bidUsahaModel = new BidangUsaha();
					bidUsahaModel.setIdSubPajak(result.getInt("IDSUB_PAJAK"));
					bidUsahaModel.setKodeBidUsaha(result.getString("KODE_BID_USAHA"));
					bidUsahaModel.setNamaBidUsaha(result.getString("NAMA_BID_USAHA"));
					bidUsahaModel.setIdPajak(result.getInt("IDPAJAK"));
					bidUsahaModel.setTarif(result.getInt("TARIF"));
					bidUsahaModel.setBunga(result.getInt("BUNGA"));
					bidUsahaModel.setDenda(result.getInt("DENDA"));
					bidUsahaModel.setKenaikan1(result.getInt("KENAIKAN_1"));
					bidUsahaModel.setKenaikan2(result.getInt("KENAIKAN_2"));
					bidUsahaModel.setDLT(result.getInt("DLT"));
					listBidangUsaha.add(bidUsahaModel);
				}
				return listBidangUsaha;
			}
			catch(SQLException e){
				MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", e.getMessage());
				return null;
			}
		}
		else{
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada pembuatan LOAD DATA tabel BIDANG_USAHA.");
			return null;
		}
	}
	
	public String getBidangUsahaByIdSubPajak (Integer idSubPajak){
		String query = "select Nama_Bid_Usaha from Bidang_Usaha where IdSub_Pajak = " + idSubPajak;
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		try {
			if (result.next()){
				return result.getString("Nama_Bid_Usaha");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	
	public String getKodeBidUsaha(Integer idSubPajak){
		String query = "Select * from Bidang_Usaha where idsub_pajak = " + idSubPajak;
		try{
			ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
			if(result.next()){
				return result.getString("Kode_Bid_Usaha");
			}
			else
				return "";
		}
		catch(SQLException e){
			e.printStackTrace();
			return null;
		}
	}
	
	public List<BidangUsaha> getAllBidangUsaha(){
		String query = "SELECT * FROM BIDANG_USAHA";
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		if(result!=null){
			try{
				List<BidangUsaha> listBidangUsaha = new ArrayList<BidangUsaha>();
				while(result.next()){
					BidangUsaha bidUsahaModel = new BidangUsaha();
					bidUsahaModel.setIdSubPajak(result.getInt("IDSUB_PAJAK"));
					bidUsahaModel.setKodeBidUsaha(result.getString("KODE_BID_USAHA"));
					bidUsahaModel.setNamaBidUsaha(result.getString("NAMA_BID_USAHA"));
					bidUsahaModel.setIdPajak(result.getInt("IDPAJAK"));
					bidUsahaModel.setTarif(result.getInt("TARIF"));
					bidUsahaModel.setBunga(result.getInt("BUNGA"));
					bidUsahaModel.setDenda(result.getInt("DENDA"));
					bidUsahaModel.setKenaikan1(result.getInt("KENAIKAN_1"));
					bidUsahaModel.setKenaikan2(result.getInt("KENAIKAN_2"));
					bidUsahaModel.setDLT(result.getInt("DLT"));
					listBidangUsaha.add(bidUsahaModel);
				}
				return listBidangUsaha;
			}
			catch(SQLException e){
				MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", e.getMessage());
				return null;
			}
		}
		else{
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada pembuatan LOAD DATA tabel BIDANG_USAHA.");
			return null;
		}
	}
	
	public void deleteBidangUsaha(Integer idSubPajak){
		String query = "DELETE FROM BIDANG_USAHA WHERE IDSUB_PAJAK = "+idSubPajak;
		boolean result = db.ExecuteStatementQuery(query, DBConnection.INSTANCE.open());
		if(!result)
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada DELETE tabel BIDANG_USAHA dengan IDSUB_PAJAK= "+idSubPajak);
	}
	
	public Integer getLastIdBidangUsaha(){
		String query = "SELECT TOP 1 IDSUB_PAJAK FROM BIDANG_USAHA ORDER BY IDSUB_PAJAK DESC";
		try{
			ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
			if(result.next()){
				return result.getInt("IDSUB_PAJAK");
			}
			else
				return null;
		}
		catch(SQLException e){
			e.printStackTrace();
			return null;
		}
	}
	
	public List<BidangUsaha> getDaftarUbahKodefikasiSPTPD(int idsubpajak, String tipeSKP){
		String query = "select sp.IDSPTPD, sp.NPWPD, sp.No_SPTPD, bu.KODE_BID_USAHA, bu.NAMA_BID_USAHA from SPTPD sp " +
				"left join BIDANG_USAHA bu on bu.IDSUB_PAJAK = sp.IDSUB_PAJAK where IDSUB_PAJAK = " + idsubpajak + " and year(sp.TGL_SPTPD) = 2016";
		if (tipeSKP.equalsIgnoreCase("SPTPD"))
			query += " order by CAST(SUBSTRING(sp.NO_SPTPD, 1, LENGTH(sp.NO_SPTPD) - 11) AS INTEGER)";
		else
			query += " and sp.No_SPTPD like '%SKPD/%' order by CAST(SUBSTRING(sp.NO_SPTPD, 1, LENGTH(sp.NO_SPTPD) - 10) AS INTEGER)";
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		if(result!=null){
			try{
				List<BidangUsaha> listBidangUsaha = new ArrayList<BidangUsaha>();
				while(result.next()){
					BidangUsaha bidUsahaModel = new BidangUsaha();
					bidUsahaModel.setIdSKP(result.getInt("IDSPTPD"));
					bidUsahaModel.setKodeBidUsaha(result.getString("KODE_BID_USAHA"));
					bidUsahaModel.setNamaBidUsaha(result.getString("NAMA_BID_USAHA"));
					bidUsahaModel.setNPWPD(result.getString("NPWPD"));
					bidUsahaModel.setNoSKP(result.getString("No_SPTPD"));
					listBidangUsaha.add(bidUsahaModel);
				}
				return listBidangUsaha;
			}
			catch(SQLException e){
				MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", e.getMessage());
				return null;
			}
		}
		else{
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada pembuatan LOAD DATA tabel BIDANG_USAHA.");
			return null;
		}
	}
	
	public List<BidangUsaha> getDaftarUbahKodefikasiSKPDKB(int idsubpajak, String tipeSKP){
		String query = "select p.IDPeriksa, p.NPWPD, p.No_SKP, bu.KODE_BID_USAHA, bu.NAMA_BID_USAHA from PERIKSA p " +
				"left join BIDANG_USAHA bu on bu.IDSUB_PAJAK = p.IDSUB_PAJAK where IDSUB_PAJAK = " + idsubpajak + " and year(p.TANGGAL_SKP) = 2016";
		if (tipeSKP.equalsIgnoreCase("SKPDKB"))
			query += " and p.No_SKP like '%/SKPDKB/%' order by CAST(SUBSTRING(p.NO_SKP, 1, LENGTH(p.NO_SKP) - 12) AS INTEGER)";
		else
			query += " and p.No_SKP like '%/SKPDN/%' order by CAST(SUBSTRING(p.NO_SKP, 1, LENGTH(p.NO_SKP) - 11) AS INTEGER)";
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		if(result!=null){
			try{
				List<BidangUsaha> listBidangUsaha = new ArrayList<BidangUsaha>();
				while(result.next()){
					BidangUsaha bidUsahaModel = new BidangUsaha();
					bidUsahaModel.setIdSKP(result.getInt("IDPERIKSA"));
					bidUsahaModel.setKodeBidUsaha(result.getString("KODE_BID_USAHA"));
					bidUsahaModel.setNamaBidUsaha(result.getString("NAMA_BID_USAHA"));
					bidUsahaModel.setNPWPD(result.getString("NPWPD"));
					bidUsahaModel.setNoSKP(result.getString("No_SKP"));
					listBidangUsaha.add(bidUsahaModel);
				}
				return listBidangUsaha;
			}
			catch(SQLException e){
				MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", e.getMessage());
				return null;
			}
		}
		else{
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada pembuatan LOAD DATA tabel BIDANG_USAHA.");
			return null;
		}
	}
	
	public boolean updateKodefikasiSPTPD(int idSKP, String newNoSKP, String newNoNPPD){
		String query = "Update SPTPD set No_SPTPD = '" + newNoSKP + "', No_NPPD = '" + newNoNPPD + "' where IDSPTPD = " + idSKP;
		boolean result = db.ExecuteStatementQuery(query, DBConnection.INSTANCE.open());
		String query1 = "Update SSPD set No_SKP = '" + newNoSKP + "' where IDSPTPD = " + idSKP;
		boolean result1 = db.ExecuteStatementQuery(query1, DBConnection.INSTANCE.open());
		if(!result || !result1)
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada MERGE(UPDATE/INSERT) tabel SPTPD.");
		return result;
	}
	
	public boolean updateKodefikasiSKPDKB(int idSKP, String newNoSKP, String newNoNPPD){
		String query = "Update Periksa set No_SKP = '" + newNoSKP + "', No_NPPD = '" + newNoNPPD + "' where IDPERIKSA = " + idSKP;
		boolean result = db.ExecuteStatementQuery(query, DBConnection.INSTANCE.open());
		String query1 = "Update SSPD set No_SKP = '" + newNoSKP + "' where IDPERIKSA = " + idSKP;
		boolean result1 = db.ExecuteStatementQuery(query1, DBConnection.INSTANCE.open());
		if(!result || !result1)
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada MERGE(UPDATE/INSERT) tabel SPTPD.");
		return result;
	}
	
	public boolean updateKodefikasiMohon(String oldNoSKP, String npwpd, String newNoSKP){
		String query = "Update MOHON set No_SKP = '" + newNoSKP + "' where NPWPD = '" + npwpd + "' and No_SKP = '" + oldNoSKP + "'";
		boolean result = db.ExecuteStatementQuery(query, DBConnection.INSTANCE.open());
		if(!result)
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada MERGE(UPDATE/INSERT) tabel SPTPD.");
		return result;
	}
	
	public boolean setNoSPTPDLama(int idSKP, String oldSkp){
		String query = "Update SPTPD set No_SKPLama = '" + oldSkp + "' where IDSPTPD = " + idSKP;
		boolean result = db.ExecuteStatementQuery(query, DBConnection.INSTANCE.open());
		if(!result)
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada MERGE(UPDATE/INSERT) tabel SPTPD.");
		return result;
	}
	
	public boolean setNoSKPDKBLama(int idSKP, String oldSkp){
		String query = "Update Periksa set No_SKPLama = '" + oldSkp + "' where IDPERIKSA = " + idSKP;
		boolean result = db.ExecuteStatementQuery(query, DBConnection.INSTANCE.open());
		if(!result)
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada MERGE(UPDATE/INSERT) tabel SPTPD.");
		return result;
	}
}
