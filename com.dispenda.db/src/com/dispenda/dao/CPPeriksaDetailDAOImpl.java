package com.dispenda.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;

import com.dispenda.model.PeriksaDetail;
import com.dispenda.database.DBConnection;
import com.dispenda.database.DBOperation;

public class CPPeriksaDetailDAOImpl{
	private DBOperation db = new DBOperation();
	
	public boolean savePeriksaDetail(List<PeriksaDetail> listPeriksaDetail, Integer idPeriksa){
		String subQuery = "";
		for (PeriksaDetail pd : listPeriksaDetail){
			subQuery += "(" +
					""+pd.getIdPeriksaDetail()+"," +
					""+idPeriksa+"," +
					"'"+pd.getNpwpd()+"'," +
					"'"+pd.getBulanPeriksa()+"'," +
					""+pd.getPengenaanPeriksa()+"," +
					""+pd.getPajakPeriksa()+"," +
					""+pd.getBungaPersen()+"," +
					""+pd.getBunga()+"," +
					""+pd.getKenaikanPajak()+")," ;
		}
		
		String query = "MERGE INTO PERIKSA_DETAIL USING(VALUES" + subQuery.substring(0, subQuery.length() - 1) +	
			") AS vals(" +
				"id," +
				"id_periksa," +
				"npwpd," +
				"bulan," +
				"pengenaan," +
				"pajak," +
				"bunga_persen," +
				"bungaa," +
				"naik_pajak)" +
			" ON PERIKSA_DETAIL.IDPERIKSA_DETAIL = vals.id" +
			" WHEN MATCHED THEN UPDATE SET " +
				"PERIKSA_DETAIL.IDPERIKSA = vals.id_periksa," +
				"PERIKSA_DETAIL.NPWPD = vals.npwpd," +
				"PERIKSA_DETAIL.BULAN_PERIKSA = vals.bulan," +
				"PERIKSA_DETAIL.PENGENAAN_PERIKSA = vals.pengenaan," +
				"PERIKSA_DETAIL.PAJAK_PERIKSA = vals.pajak," +
				"PERIKSA_DETAIL.BUNGA_PERSEN = vals.bunga_persen," +
				"PERIKSA_DETAIL.BUNGA = vals.bungaa," +
				"PERIKSA_DETAIL.KENAIKAN_PAJAK = vals.naik_pajak" +
			" WHEN NOT MATCHED THEN INSERT VALUES " +
				"vals.id," +
				"vals.id_periksa," +
				"vals.npwpd," +
				"vals.bulan," +
				"vals.pengenaan," +
				"vals.pajak," +
				"vals.bunga_persen," +
				"vals.bungaa," +
				"vals.naik_pajak";
		boolean result = db.ExecuteStatementQuery(query, DBConnection.INSTANCE.open());
		if(!result)
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada MERGE(UPDATE/INSERT) tabel PERIKSA_DETAIL.");
		return result;
	}
	
	public boolean savePeriksaDetail2(List<PeriksaDetail> listPeriksaDetail, Integer idPeriksa){
		String subQuery = "";
		for (PeriksaDetail pd : listPeriksaDetail){
			subQuery += "(" +
					""+pd.getIdPeriksaDetail()+"," +
					""+idPeriksa+"," +
					"'"+pd.getNpwpd()+"'," +
					"'"+pd.getBulanPeriksa()+"'," +
					"'"+pd.getDasarPengenaan()+"'," +
					""+pd.getPengenaanPeriksa()+"," +
					"'"+pd.getSelisihKurang()+"'," +
					"'"+pd.getKenaikanPajak()+"'," +
					""+pd.getTotalPeriksa()+"),";
		}
		String query = "MERGE INTO PERIKSA_DETAIL2 USING(VALUES" + subQuery.substring(0, subQuery.length() - 1) + 
			") AS vals(" +
				"id," +
				"id_periksa," +
				"npwpd," +
				"bulan," +
				"dasarpengenaan," +
				"periksa," +
				"selisih," +
				"kenaikan," +
				"total)" +
			" ON PERIKSA_DETAIL2.IDPERIKSA_DETAIL = vals.id" +
			" WHEN MATCHED THEN UPDATE SET " +
				"PERIKSA_DETAIL2.IDPERIKSA = vals.id_periksa," +
				"PERIKSA_DETAIL2.NPWPD = vals.npwpd," +
				"PERIKSA_DETAIL2.BULAN_PERIKSA = vals.bulan," +
				"PERIKSA_DETAIL2.DASAR_PENGENAAN = vals.dasarpengenaan," +
				"PERIKSA_DETAIL2.PENGENAAN_PERIKSA = vals.periksa," +
				"PERIKSA_DETAIL2.SELISIH_KURANG = vals.selisih," +
				"PERIKSA_DETAIL2.KENAIKAN = vals.kenaikan," +
				"PERIKSA_DETAIL2.TOTAL_PERIKSA = vals.total" +
			" WHEN NOT MATCHED THEN INSERT VALUES " +
				"vals.id," +
				"vals.id_periksa," +
				"vals.npwpd," +
				"vals.bulan," +
				"vals.dasarpengenaan," +
				"vals.periksa," +
				"vals.selisih," +
				"vals.kenaikan," +
				"vals.total";
		boolean result = db.ExecuteStatementQuery(query, DBConnection.INSTANCE.open());
		if(!result)
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada MERGE(UPDATE/INSERT) tabel PERIKSA_DETAIL2.");
		return result;
	}
	
	public List<PeriksaDetail> getPeriksaDetail(Integer idPeriksa, String npwpd){
		String query = "SELECT * FROM PERIKSA_DETAIL WHERE IDPERIKSA = "+idPeriksa+ " AND NPWPD = "+npwpd;
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		if(result != null){
			try{
				List<PeriksaDetail> listPeriksaDetail = new ArrayList<PeriksaDetail>();
				while(result.next()){
					PeriksaDetail periksaDetailModel = new PeriksaDetail();
					periksaDetailModel.setIdPeriksaDetail(result.getInt("IDPERIKSA_DETAIL"));
					periksaDetailModel.setIdPeriksa(result.getInt("IDPERIKSA"));
					periksaDetailModel.setNpwpd(result.getString("NPWPD"));
					periksaDetailModel.setBulanPeriksa(result.getString("BULAN_PERIKSA"));
					periksaDetailModel.setPengenaanPeriksa(result.getDouble("PENGENAAN_PERIKSA"));
					periksaDetailModel.setPajakPeriksa(result.getDouble("PAJAK_PERIKSA"));
					periksaDetailModel.setBungaPersen(result.getInt("BUNGA_PERSEN"));
					periksaDetailModel.setBunga(result.getDouble("BUNGA"));
					periksaDetailModel.setKenaikanPajak(result.getDouble("KENAIKAN_PAJAK"));
					listPeriksaDetail.add(periksaDetailModel);
				}
				return listPeriksaDetail;
			}
			catch(SQLException e){
				MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", e.getMessage());
				return null;
			}
		}
		else{
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada pembuatan LOAD DATA tabel PERIKSA_DETAIL.");
			return null;
		}
	}
	
	public List<PeriksaDetail> getAllPeriksaDetail(){
		String query = "SELECT * FROM PERIKSA_DETAIL";
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		if(result!=null){
			try{
				List<PeriksaDetail> listPeriksaDetail = new ArrayList<PeriksaDetail>();
				while(result.next()){
					PeriksaDetail periksaDetailModel = new PeriksaDetail();
					periksaDetailModel.setIdPeriksaDetail(result.getInt("IDPERIKSA_DETAIL"));
					periksaDetailModel.setIdPeriksa(result.getInt("IDPERIKSA"));
					periksaDetailModel.setNpwpd(result.getString("NPWPD"));
					periksaDetailModel.setBulanPeriksa(result.getString("BULAN_PERIKSA"));
					periksaDetailModel.setPengenaanPeriksa(result.getDouble("PENGENAAN_PERIKSA"));
					periksaDetailModel.setPajakPeriksa(result.getDouble("PAJAK_PERIKSA"));
					periksaDetailModel.setBungaPersen(result.getInt("BUNGA_PERSEN"));
					periksaDetailModel.setBunga(result.getDouble("BUNGA"));
					periksaDetailModel.setKenaikanPajak(result.getDouble("KENAIKAN_PAJAK"));
					listPeriksaDetail.add(periksaDetailModel);
				}
				return listPeriksaDetail;
			}
			catch(SQLException e){
				MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", e.getMessage());
				return null;
			}
		}
		else{
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada pembuatan LOAD DATA tabel PERIKSA_DETAIL.");
			return null;
		}
	}
	
	public void deletePeriksaDetail(Integer idPeriksaDetail){
		String query = "DELETE FROM PERIKSA_DETAIL WHERE IDPERIKSA_DETAIL = "+idPeriksaDetail;
		boolean result = db.ExecuteStatementQuery(query, DBConnection.INSTANCE.open());
		if(!result)
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada DELETE tabel PERIKSA_DETAIL dengan IDPERIKSADETAIL = "+idPeriksaDetail);
	}
	
	public Integer getLastIdPeriksaDetail(){
		String query = "SELECT TOP 1 IDPERIKSA_DETAIL FROM PERIKSA_DETAIL ORDER BY IDPERIKSA_DETAIL DESC";
		try{
			ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
			if(result.next()){
				return result.getInt("IDPERIKSADETAIL");
			}
			else
				return 0;
		}
		catch(SQLException e){
			e.printStackTrace();
			return 0;
		}
	}
	
}