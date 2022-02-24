package com.dispenda.dao;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;

import com.dispenda.model.PeriksaDetailBPK;
import com.dispenda.database.DBConnection;
import com.dispenda.database.DBOperation;

public class CPPeriksaDetailBPKDAOImpl{
	private DBOperation db = new DBOperation();
	
	public boolean savePeriksaDetail(List<PeriksaDetailBPK> listPeriksaDetail, Integer idPeriksa){
		String subQuery = "";
		for (PeriksaDetailBPK pd : listPeriksaDetail){
			subQuery += "(" +
					""+pd.getIdPeriksaDetail()+"," +
					""+idPeriksa+"," +
					"'"+pd.getNpwpd()+"'," +
					"'"+pd.getBulan()+"'," +
					"'"+pd.getMasaPajak()+"'," +
					""+pd.getDasarPengenaan()+"," +
					""+pd.getPengenaanPeriksa()+"," +
					""+pd.getSelisihKurang()+"," +
					""+pd.getTarif()+"," +
					""+pd.getPajakTerutang()+"," +
					""+pd.getJumlahBayar()+"," +
					""+pd.getPajakPeriksa()+"," +
					""+pd.getKurangBayar()+"," +
					""+pd.getBungaPersen()+"," +
					""+pd.getBunga()+"," +
					""+pd.getDendaTambahan()+"," +
					""+pd.getKenaikan()+"," +
					""+pd.getTotal()+")," ;
		}
		
		String query = "MERGE INTO PERIKSA_DETAILBPK USING(VALUES" + subQuery.substring(0, subQuery.length() - 1) +	
			") AS vals(" +
				"id," +
				"id_periksa," +
				"npwpd," +
				"bulan," +
				"masaPajak," +
				"dasarPengenaan," +
				"pengenaanPeriksa," +
				"selisihKurang," +
				"tarif," +
				"pajakTerutang," +
				"jumlahBayar," +
				"pajakPeriksa," +
				"kurangBayar," +
				"bunga_persen," +
				"bunga," +
				"dendaTambahan," +
				"kenaikan," +
				"total)" +
			" ON PERIKSA_DETAILBPK.IDPERIKSA_DETAIL = vals.id" +
			" WHEN MATCHED THEN UPDATE SET " +
				"PERIKSA_DETAILBPK.IDPERIKSA = vals.id_periksa," +
				"PERIKSA_DETAILBPK.NPWPD = vals.npwpd," +
				"PERIKSA_DETAILBPK.BULAN = vals.bulan," +
				"PERIKSA_DETAILBPK.MASA_PAJAK = vals.masaPajak," +
				"PERIKSA_DETAILBPK.DASAR_PENGENAAN = vals.dasarPengenaan," +
				"PERIKSA_DETAILBPK.PENGENAAN_PERIKSA = vals.pengenaanPeriksa," +
				"PERIKSA_DETAILBPK.SELISIH_KURANG = vals.selisihKurang," +
				"PERIKSA_DETAILBPK.TARIF = vals.tarif," +
				"PERIKSA_DETAILBPK.PAJAK_TERUTANG = vals.pajakTerutang," +
				"PERIKSA_DETAILBPK.JUMLAH_BAYAR = vals.jumlahBayar," +
				"PERIKSA_DETAILBPK.PAJAK_PERIKSA = vals.pajakPeriksa," +
				"PERIKSA_DETAILBPK.KURANG_BAYAR = vals.kurangBayar," +
				"PERIKSA_DETAILBPK.BUNGA_PERSEN = vals.bunga_persen," +
				"PERIKSA_DETAILBPK.BUNGA = vals.bunga," +
				"PERIKSA_DETAILBPK.DENDA_TAMBAHAN = vals.dendaTambahan," +
				"PERIKSA_DETAILBPK.KENAIKAN = vals.kenaikan," +
				"PERIKSA_DETAILBPK.TOTAL = vals.total" +
			" WHEN NOT MATCHED THEN INSERT VALUES " +
				"vals.id," +
				"vals.id_periksa," +
				"vals.npwpd," +
				"vals.bulan," +
				"vals.masaPajak," +
				"vals.dasarPengenaan," +
				"vals.pengenaanPeriksa," +
				"vals.selisihKurang," +
				"vals.tarif," +
				"vals.pajakTerutang," +
				"vals.jumlahBayar," +
				"vals.pajakPeriksa," +
				"vals.kurangBayar," +
				"vals.bunga_persen," +
				"vals.bunga," +
				"vals.dendaTambahan," +
				"vals.kenaikan," +
				"vals.total";
		boolean result = db.ExecuteStatementQuery(query, DBConnection.INSTANCE.open());
		if(!result)
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada MERGE(UPDATE/INSERT) tabel PERIKSA_DETAILBPK.");
		return result;
	}
	
	public List<PeriksaDetailBPK> getPeriksaDetail(Integer idPeriksa, String npwpd){
		String query = "SELECT * FROM PERIKSA_DETAILBPK WHERE IDPERIKSA = "+idPeriksa+ " AND NPWPD = "+npwpd;
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		if(result != null){
			try{
				List<PeriksaDetailBPK> listPeriksaDetail = new ArrayList<PeriksaDetailBPK>();
				while(result.next()){
					PeriksaDetailBPK periksaDetailModel = new PeriksaDetailBPK();
					periksaDetailModel.setIdPeriksaDetail(result.getInt("IDPERIKSA_DETAIL"));
					periksaDetailModel.setIdPeriksa(result.getInt("IDPERIKSA"));
					periksaDetailModel.setNpwpd(result.getString("NPWPD"));
					periksaDetailModel.setBulan(result.getString("BULAN"));
					periksaDetailModel.setMasaPajak(result.getString("Masa_Pajak"));
					periksaDetailModel.setDasarPengenaan(result.getDouble("Dasar_Pengenaan"));
					periksaDetailModel.setPengenaanPeriksa(result.getDouble("PENGENAAN_PERIKSA"));
					periksaDetailModel.setSelisihKurang(result.getDouble("Selisih_Kurang"));
					periksaDetailModel.setTarif(result.getInt("TARIF"));
					periksaDetailModel.setPajakTerutang(result.getDouble("Pajak_Terutang"));
					periksaDetailModel.setJumlahBayar(result.getDouble("Jumlah_Bayar"));
					periksaDetailModel.setPajakPeriksa(result.getDouble("PAJAK_PERIKSA"));
					periksaDetailModel.setKurangBayar(result.getDouble("Kurang_Bayar"));
					periksaDetailModel.setBungaPersen(result.getInt("BUNGA_PERSEN"));
					periksaDetailModel.setBunga(result.getDouble("BUNGA"));
					periksaDetailModel.setDendaTambahan(result.getDouble("Denda_Tambahan"));
					periksaDetailModel.setKenaikan(result.getDouble("KENAIKAN"));
					periksaDetailModel.setTotal(result.getDouble("TOTAL"));
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
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada pembuatan LOAD DATA tabel PERIKSA_DETAILBPK.");
			return null;
		}
	}
	
	public List<PeriksaDetailBPK> getAllPeriksaDetail(){
		String query = "SELECT * FROM PERIKSA_DETAILBPK";
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		if(result!=null){
			try{
				List<PeriksaDetailBPK> listPeriksaDetail = new ArrayList<PeriksaDetailBPK>();
				while(result.next()){
					PeriksaDetailBPK periksaDetailModel = new PeriksaDetailBPK();
					periksaDetailModel.setIdPeriksaDetail(result.getInt("IDPERIKSA_DETAIL"));
					periksaDetailModel.setIdPeriksa(result.getInt("IDPERIKSA"));
					periksaDetailModel.setNpwpd(result.getString("NPWPD"));
					periksaDetailModel.setBulan(result.getString("BULAN"));
					periksaDetailModel.setPengenaanPeriksa(result.getDouble("PENGENAAN_PERIKSA"));
					periksaDetailModel.setPajakPeriksa(result.getDouble("PAJAK_PERIKSA"));
					periksaDetailModel.setBungaPersen(result.getInt("BUNGA_PERSEN"));
					periksaDetailModel.setBunga(result.getDouble("BUNGA"));
					periksaDetailModel.setKenaikan(result.getDouble("KENAIKAN"));
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
		String query = "DELETE FROM PERIKSA_DETAILBPK WHERE IDPERIKSA_DETAIL = "+idPeriksaDetail;
		boolean result = db.ExecuteStatementQuery(query, DBConnection.INSTANCE.open());
		if(!result)
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada DELETE tabel PERIKSA_DETAIL dengan IDPERIKSADETAIL = "+idPeriksaDetail);
	}
	
	public Integer getLastIdPeriksaDetail(){
		String query = "SELECT TOP 1 IDPERIKSA_DETAIL FROM PERIKSA_DETAILBPK ORDER BY IDPERIKSA_DETAIL DESC";
		try{
			ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
			if(result.next()){
				return result.getInt("IDPERIKSA_DETAIL");
			}
			else
				return 0;
		}
		catch(SQLException e){
			e.printStackTrace();
			return 0;
		}
	}
	
	public List<HashMap<String, Object>> GetDetailPeriksaBPKTable(String NPWPD, Date masaPajakDari, Date masaPajakSampai)
	{
		String sqlString = "SELECT * FROM periksa_detailBPK " +
					"WHERE NPWPD = '" + NPWPD + "' and CAST(CONCAT(SUBSTRING(Bulan,4,7), '-', SUBSTRING(Bulan,0,3), '-01') AS DATE) >= '" + masaPajakDari + "' " +
					"AND CAST(CONCAT(SUBSTRING(Bulan,4,7), '-', SUBSTRING(Bulan,0,3), '-01') AS DATE) <= '" + masaPajakSampai + "'";
//					"GROUP BY IdPeriksa_Detail, Pengenaan_Periksa, Pajak_Periksa, Bunga_Persen, Bunga, Kenaikan_Pajak ORDER BY Bulan ASC";
		
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
	
	public double GetPajakSKPDKB(Integer idPeriksa)
	{
		double retValue = 0.0;
		String sqlString = "SELECT sum(KURANG_BAYAR) as PokokPajak FROM " +
		"Periksa_DetailBPK WHERE IDPERIKSA = " + idPeriksa + ";";
		try{
			ResultSet result = db.ResultExecutedStatementQuery(sqlString, DBConnection.INSTANCE.open());
			if (result.next()){
				retValue += result.getDouble("PokokPajak");
			}
		}
		catch(SQLException e){
			e.printStackTrace();
			return 0;
		}
		
		return retValue;
	}
	
	public double GetDendaSKPDKB(Integer idPeriksa)
	{
		double retValue = 0.0;
		String sqlString = "SELECT SUM(Bunga) AS Denda FROM periksa_detailbpk WHERE IDPERIKSA = " + idPeriksa + ";";
		
		try{
			ResultSet result = db.ResultExecutedStatementQuery(sqlString, DBConnection.INSTANCE.open());
			if (result.next()){
				retValue += result.getDouble("Denda");
			}
		}
		catch(SQLException e){
			e.printStackTrace();
			return 0;
		}
		
		return retValue;
	}
}
