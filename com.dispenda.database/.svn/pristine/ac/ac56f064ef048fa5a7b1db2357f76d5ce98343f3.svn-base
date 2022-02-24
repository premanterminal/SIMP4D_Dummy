package com.dispenda.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;

import com.dispenda.main.MainActivator;
import com.dispenda.model.PeriksaDetail;
import com.dispenda.database.DBOperation;

public class CPPeriksaDetailDAOImpl{
	private PeriksaDetail periksaDetailModel = new PeriksaDetail();
	private DBOperation db = new DBOperation();
	
	public boolean savePeriksaDetail(PeriksaDetail periksaDetailModel){
		String query = "MERGE INTO PERIKSA_DETAIL USING(VALUES(" +
				""+periksaDetailModel.getIdPeriksaDetail()+"," +
				""+periksaDetailModel.getIdPeriksa()+"," +
				"'"+periksaDetailModel.getNpwpd()+"'," +
				"'"+periksaDetailModel.getBulanPeriksa()+"'," +
				""+periksaDetailModel.getPengenaanPeriksa()+"," +
				""+periksaDetailModel.getPajakPeriksa()+"," +
				""+periksaDetailModel.getBungaPersen()+"," +
				""+periksaDetailModel.getBunga()+"," +
				""+periksaDetailModel.getKenaikanPajak()+"))" +
			" AS vals(" +
				"id," +
				"id_periksa," +
				"npwpd," +
				"bulan," +
				"pengenaan," +
				"pajak," +
				"bunga_persen," +
				"bungaa," +
				"naik_pajak)" +
			" ON PERIKSA_DETAIL.IDPERIKSADETAIL = vals.id" +
			" WHEN MATCHED THEN UPDATE SET " +
				"PERIKSA.IDPERIKSA = vals.id_periksa," +
				"PERIKSA.NPWPD_PD = vals.npwpd," +
				"PERIKSA.BULANPERIKSA = vals.bulan," +
				"PERIKSA.PENGENAANPERIKSA = vals.pengenaan," +
				"PERIKSA.PAJAPERIKSA = vals.pajak," +
				"PERIKSA.BUNGAPERSEN = vals.bunga_persen," +
				"PERIKSA.BUNGA = vals.bungaa," +
				"PERIKSA.KENAIKANPAJAK = vals.naik_pajak" +
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
		boolean result = db.ExecuteStatementQuery(query, MainActivator.getDefault().getConnection());
		if(!result)
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada MERGE(UPDATE/INSERT) tabel PERIKSA_DETAIL.");
		return result;
	}
	
	public PeriksaDetail getPeriksaDetail(Integer idPeriksaDetail){
		String query = "SELECT * FROM PERIKSA_DETAIL WHERE IDPERIKSADETAIL = "+idPeriksaDetail;
		ResultSet result = db.ResultExecutedStatementQuery(query, MainActivator.getDefault().getConnection());
		if(result != null){
			try{
				while(result.next()){
					this.periksaDetailModel.setIdPeriksaDetail(result.getInt("IDPERIKSADETAIL"));
					this.periksaDetailModel.setIdPeriksa(result.getInt("IDPERIKSA"));
					this.periksaDetailModel.setNpwpd(result.getString("NPWPD_PD"));
					this.periksaDetailModel.setBulanPeriksa(result.getString("BULANPERIKSA"));
					this.periksaDetailModel.setPengenaanPeriksa(result.getDouble("PENGENAANPERIKSA"));
					this.periksaDetailModel.setPajakPeriksa(result.getDouble("PAJAPERIKSA"));
					this.periksaDetailModel.setBungaPersen(result.getInt("BUNGAPERSEN"));
					this.periksaDetailModel.setBunga(result.getDouble("BUNGA"));
					this.periksaDetailModel.setKenaikanPajak(result.getDouble("KENAIKANPAJAK"));
				}
				return this.periksaDetailModel;
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
		ResultSet result = db.ResultExecutedStatementQuery(query, MainActivator.getDefault().getConnection());
		if(result!=null){
			try{
				List<PeriksaDetail> listPeriksaDetail = new ArrayList<PeriksaDetail>();
				while(result.next()){
					PeriksaDetail periksaDetailModel = new PeriksaDetail();
					periksaDetailModel.setIdPeriksaDetail(result.getInt("IDPERIKSADETAIL"));
					periksaDetailModel.setIdPeriksa(result.getInt("IDPERIKSA"));
					periksaDetailModel.setNpwpd(result.getString("NPWPD_PD"));
					periksaDetailModel.setBulanPeriksa(result.getString("BULANPERIKSA"));
					periksaDetailModel.setPengenaanPeriksa(result.getDouble("PENGENAANPERIKSA"));
					periksaDetailModel.setPajakPeriksa(result.getDouble("PAJAPERIKSA"));
					periksaDetailModel.setBungaPersen(result.getInt("BUNGAPERSEN"));
					periksaDetailModel.setBunga(result.getDouble("BUNGA"));
					periksaDetailModel.setKenaikanPajak(result.getDouble("KENAIKANPAJAK"));
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
		String query = "DELETE FROM PERIKSA_DETAIL WHERE IDPERIKSADETAIL = "+idPeriksaDetail;
		boolean result = db.ExecuteStatementQuery(query, MainActivator.getDefault().getConnection());
		if(!result)
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada DELETE tabel PERIKSA_DETAIL dengan IDPERIKSADETAIL = "+idPeriksaDetail);
	}
	
	public Integer getLastIdPeriksaDetail(){
		String query = "SELECT TOP 1 IDPERIKSADETAIL FROM PERIKSA_DETAIL ORDER BY IDPERIKSADETAIL DESC";
		try{
			ResultSet result = db.ResultExecutedStatementQuery(query, MainActivator.getDefault().getConnection());
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
