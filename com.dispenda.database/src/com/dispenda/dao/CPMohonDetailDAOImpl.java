package com.dispenda.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;

import com.dispenda.model.MohonDetail;
import com.dispenda.database.DBConnection;
import com.dispenda.database.DBOperation;

public class CPMohonDetailDAOImpl{
	private MohonDetail mohonDetailModel = new MohonDetail();
	private DBOperation db = new DBOperation();
	
	public boolean saveMohonDetail(MohonDetail mohonDetailModel){
		String query = "MERGE INTO MOHON_DETAIL USING (VALUES(" +
				""+mohonDetailModel.getIdMohonDetail()+"," +
				""+mohonDetailModel.getIdMohon()+"," +
				""+mohonDetailModel.getNoAngsuran()+"," +
				"'"+mohonDetailModel.getTglAngsuran()+"'," +
				""+mohonDetailModel.getAngsuranPokok()+"," +
				""+mohonDetailModel.getBiayaAdministrasi()+"," +
				""+mohonDetailModel.getDendaSkpdkb()+"))" +
			" AS vals(" +
				"id," +
				"idMohon," +
				"no_angs," +
				"tgl_angs," +
				"angs_pokok," +
				"biaya_adm," +
				"denda_skpdkb)" +
			" ON MOHON_DETAIL.IDMOHONDETAIL = vals.id" +
			" WHEN MATCHED THEN UPDATE SET " +
				"MOHON_DETAIL.IDMOHONN = vals.idMohon," +
				"MOHON_DETAIL.NOANGSURAN = vals.no_angs," +
				"MOHON_DETAIL.TGLANGSURAN = vals.tgl_angs," +
				"MOHON_DETAIL.ANGSURANPOKOK = vals.angs_pokok," +
				"MOHON_DETAIL.BIAYAADMINISTRASI = vals.biaya_adm," +
				"MOHON_DETAIL.DENDASKPDKB = vals.denda_skpdkb" +
			" WHEN NOT MATCHED THEN INSERT VALUES " +
				"vals.id," +
				"vals.idMohon," +
				"vals.no_angs," +
				"vals.tgl_angs," +
				"vals.angs_pokok," +
				"vals.biaya_adm," +
				"vals.denda_skpdkb";
		boolean result = db.ExecuteStatementQuery(query, DBConnection.INSTANCE.open());
		if(!result)
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada MERGE(UPDATE/INSERT) tabel MOHON_DETAIL.");
		return result;
								
	}
	
	public MohonDetail getMohonDetail(Integer idMohonDetail){
		String query = "SELECT * FROM MOHON_DETAIL WHERE IDMOHONDETAIL = "+idMohonDetail;
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		if(result != null){
			try{
				while(result.next()){
					this.mohonDetailModel.setIdMohonDetail(result.getInt("IDMOHONDETAIL"));
					this.mohonDetailModel.setIdMohon(result.getInt("IDMOHONN"));
					this.mohonDetailModel.setNoAngsuran(result.getInt("NOANGSURAN"));
					this.mohonDetailModel.setTglAngsuran(result.getDate("TGLANGSURAN"));
					this.mohonDetailModel.setAngsuranPokok(result.getDouble("ANGSURANPOKOK"));
					this.mohonDetailModel.setBiayaAdministrasi(result.getDouble("BIAYAADMINISTRASI"));
					this.mohonDetailModel.setDendaSkpdkb(result.getDouble("DENDASKPDKB"));
				}
				return this.mohonDetailModel;
			}
			catch(SQLException e){
				MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", e.getMessage());
				return null;
			}
		}
		else{
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada pembuatan LOAD DATA tabel MOHON_DETAIL.");
			return null;
		}
	}
	
	public List<MohonDetail> getAllMohonDetail(){
		String query = "SELECT * FROM MOHON_DETAIL";
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		if(result != null){
			try{
				List<MohonDetail> listMohonDetail = new ArrayList<MohonDetail>();
				while(result.next()){
					MohonDetail mohonDetailModel = new MohonDetail();
					mohonDetailModel.setIdMohonDetail(result.getInt("IDMOHONDETAIL"));
					mohonDetailModel.setIdMohon(result.getInt("IDMOHONN"));
					mohonDetailModel.setNoAngsuran(result.getInt("NOANGSURAN"));
					mohonDetailModel.setTglAngsuran(result.getDate("TGLANGSURAN"));
					mohonDetailModel.setAngsuranPokok(result.getDouble("ANGSURANPOKOK"));
					mohonDetailModel.setBiayaAdministrasi(result.getDouble("BIAYAADMINISTRASI"));
					mohonDetailModel.setDendaSkpdkb(result.getDouble("DENDASKPDKB"));
					listMohonDetail.add(mohonDetailModel);
				}
				return listMohonDetail;
			}
			catch(SQLException e){
				MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", e.getMessage());
				return null;
			}
		}
		else{
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada pembuatan LOAD DATA tabel MOHON_DETAIL.");
			return null;
		}
	}
	
	public void deleteMohonDetail(Integer idMohonDetail){
		String query = "DELETE FROM MOHON_DETAIL WHERE IDMOHONDETAIL = "+idMohonDetail;
		boolean result = db.ExecuteStatementQuery(query, DBConnection.INSTANCE.open());
		if(!result)
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada DELETE tabel MOHON_DETAIL dengan IDMOHONDETAIL = "+idMohonDetail);
	}
	
	public Integer getLastMohonDetail(){
		String query = "SELECT TOP 1 IDMOHONDETAIL FROM MOHON_DETAIL ORDER BY IDMOHONDETAIL DESC";
		try{
			ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
			if(result.next()){
				return result.getInt("IDMOHONDETAIL");
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