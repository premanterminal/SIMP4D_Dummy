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
	private BidangUsaha bidUsahaModel = new BidangUsaha();
	private DBOperation db = new DBOperation();
	
	public boolean saveBidangUsaha(BidangUsaha bidUsahaModel){
		String query = "MERGE INTO BID_USAHA USING (VALUES(" +
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
			" ON BID_USAHA.IDSUBPAJAK = vals.idsub_pajak" +
			" WHEN MATCHED THEN UPDATE SET " +
				"BID_USAHA.KODEBIDANGUSAHA = vals.kodebid_usaha," +
				"BID_USAHA.NAMABIDANGUSAHA = vals.namabid_usaha," +
				"BID_USAHA.IDPAJAK = vals.id_pajak," +
				"BID_USAHA.TARIF_BU = vals.tarif," +
				"BID_USAHA.BUNGA_BU = vals.bunga," +
				"BID_USAHA.DENDA_BU = vals.denda," +
				"BID_USAHA.KENAIKAN1 = vals.kenaikan_1," +
				"BID_USAHA.KENAIKAN2 = vals.kenaikan_2," +
				"BID_USAHA.DLT_BU = vals.dlt" +
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
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada MERGE(UPDATE/INSERT) tabel BID_USAHA.");
		return result;
	}
	
	public BidangUsaha getBidangUsaha(String kodebid_usaha){
		String query = "SELECT * FROM BID_USAHA WHERE KODEBIDANGUSAHA = "+kodebid_usaha;
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		if(result != null){
			try{
				while(result.next()){
					this.bidUsahaModel.setIdSubPajak(result.getInt("IDSUBPAJAK"));
					this.bidUsahaModel.setKodeBidUsaha(result.getString("KODEBIDANGUSAHA"));
					this.bidUsahaModel.setNamaBidUsaha(result.getString("NAMABIDANGUSAHA"));
					this.bidUsahaModel.setIdPajak(result.getInt("IDPAJAK"));
					this.bidUsahaModel.setTarif(result.getInt("TARIF_BU"));
					this.bidUsahaModel.setBunga(result.getInt("BUNGA_BU"));
					this.bidUsahaModel.setDenda(result.getInt("DENDA_BU"));
					this.bidUsahaModel.setKenaikan1(result.getInt("KENAIKAN1"));
					this.bidUsahaModel.setKenaikan2(result.getInt("KENAIKAN2"));
					this.bidUsahaModel.setDLT(result.getInt("DLT_BU"));
				}
				return this.bidUsahaModel;
			}
			catch(SQLException e){
				MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", e.getMessage());
				return null;
			}
		}
		else{
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada pembuatan LOAD DATA tabel BID_USAHA.");
			return null;
		}
	}
	
	public List<BidangUsaha> getAllBidangUsaha(){
		String query = "SELECT * FROM BID_USAHA";
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		if(result!=null){
			try{
				List<BidangUsaha> listBidangUsaha = new ArrayList<BidangUsaha>();
				while(result.next()){
					BidangUsaha bidUsahaModel = new BidangUsaha();
					bidUsahaModel.setIdSubPajak(result.getInt("IDSUBPAJAK"));
					bidUsahaModel.setKodeBidUsaha(result.getString("KODEBIDANGUSAHA"));
					bidUsahaModel.setNamaBidUsaha(result.getString("NAMABIDANGUSAHA"));
					bidUsahaModel.setIdPajak(result.getInt("IDPAJAK"));
					bidUsahaModel.setTarif(result.getInt("TARIF_BU"));
					bidUsahaModel.setBunga(result.getInt("BUNGA_BU"));
					bidUsahaModel.setDenda(result.getInt("DENDA_BU"));
					bidUsahaModel.setKenaikan1(result.getInt("KENAIKAN1"));
					bidUsahaModel.setKenaikan2(result.getInt("KENAIKAN2"));
					bidUsahaModel.setDLT(result.getInt("DLT_BU"));
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
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada pembuatan LOAD DATA tabel BID_USAHA.");
			return null;
		}
	}
	
	public void deleteBidangUsaha(String kodebid_usaha){
		String query = "DELETE FROM BID_USAHA WHERE KODEBIDANGUSAHA = "+kodebid_usaha;
		boolean result = db.ExecuteStatementQuery(query, DBConnection.INSTANCE.open());
		if(!result)
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada DELETE tabel BID_USAHA dengan KODEBIDANGUSAHA = "+kodebid_usaha);
	}
	
	public String getLastKodeBidUsaha(){
		String query = "SELECT TOP 1 KODEBIDANGUSAHA FROM BID_USAHA ORDER BY KODEBIDANGUSAHA DESC";
		try{
			ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
			if(result.next()){
				return result.getString("KODEBIDANGUSAHA");
			}
			else
				return "";
		}
		catch(SQLException e){
			e.printStackTrace();
			return "";
		}
	}
}
