package com.dispenda.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;

import com.dispenda.database.DBConnection;
import com.dispenda.database.DBOperation;
import com.dispenda.model.PendaftaranSuratIzinUsaha;

public class CPSuratIzinUsahaDAOImpl{
	private DBOperation db = new DBOperation();
	
	public boolean savePendaftaranSuratIzinUsaha(PendaftaranSuratIzinUsaha izinUsahaModel){
		String query = "MERGE INTO SURAT_IZIN_USAHA USING (VALUES(" +
				""+izinUsahaModel.getId()+"," +
				"'"+izinUsahaModel.getNPWP()+"'," +
				"'"+izinUsahaModel.getNoSurat()+"'," +
				"'"+izinUsahaModel.getTanggalSurat()+"'," +
				"'"+izinUsahaModel.getSuratIzin()+"'))" +
			" AS vals(" +
				"id," +
				"npwp," +
				"no_surat," +
				"tgl_surat," +
				"surat_izin)" +
			" ON SURAT_IZIN_USAHA.ID = vals.id" +
			" WHEN MATCHED THEN UPDATE SET " +
				"SURAT_IZIN_USAHA.NPWPD = vals.npwp," +
				"SURAT_IZIN_USAHA.NO_SURAT = vals.no_surat," +
				"SURAT_IZIN_USAHA.SURAT_IZIN = vals.surat_izin," +
				"SURAT_IZIN_USAHA.TANGGAL_SURAT = vals.tgl_surat" +
			" WHEN NOT MATCHED THEN INSERT VALUES " +
				"vals.id," +
				"vals.npwp," +
				"vals.no_surat," +
				"vals.tgl_surat," +
				"vals.surat_izin" ;
		boolean result = db.ExecuteStatementQuery(query, DBConnection.INSTANCE.open());
		if(!result)
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "There are some problems of MERGE(UPDATE/INSERT) on table SURAT_IZIN_USAHA.");
		return result;
		
	}
	
	public List<PendaftaranSuratIzinUsaha> getPendaftaranSuratIzinUsaha(String npwpd){
		String query = "SELECT * FROM SURAT_IZIN_USAHA WHERE NPWPD = '"+npwpd+"';";
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		if(result != null){
			try{
				List<PendaftaranSuratIzinUsaha> listPendaftaranSuratIzinUsaha = new ArrayList<PendaftaranSuratIzinUsaha>();
				while(result.next()){
					PendaftaranSuratIzinUsaha izinUsahaModel = new PendaftaranSuratIzinUsaha();
					izinUsahaModel.setId(result.getInt("ID"));
					izinUsahaModel.setNPWP(result.getString("NPWPD"));
					izinUsahaModel.setNoSurat(result.getString("NO_SURAT"));
					izinUsahaModel.setSuratIzin(result.getString("SURAT_IZIN"));
					izinUsahaModel.setTanggalSurat(result.getString("TANGGAL_SURAT"));
					listPendaftaranSuratIzinUsaha.add(izinUsahaModel);
					
				}
				return listPendaftaranSuratIzinUsaha;
			}
			catch(SQLException e){
				MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database SQL Error", e.getMessage());
				return null;
			}
		}
		else{
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada pembuatan LOAD DATA tabel SURAT_IZIN_USAHA.");
			return null;
		}
	}
	
	public List<PendaftaranSuratIzinUsaha> getAllPendaftaranSuratIzinUsaha(){
		String query = "SELECT * FROM SURAT_IZIN_USAHA";
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		if(result != null){
			try{
				List<PendaftaranSuratIzinUsaha> listPendaftaranSuratIzinUsaha = new ArrayList<PendaftaranSuratIzinUsaha>();
				while(result.next()){
					PendaftaranSuratIzinUsaha izinUsahaModel = new PendaftaranSuratIzinUsaha();
					izinUsahaModel.setId(result.getInt("ID"));
					izinUsahaModel.setNPWP(result.getString("NPWPD"));
					izinUsahaModel.setNoSurat(result.getString("NO_SURAT"));
					izinUsahaModel.setSuratIzin(result.getString("SURAT_IZIN"));
					izinUsahaModel.setTanggalSurat(result.getString("TANGGAL_SURAT"));
					listPendaftaranSuratIzinUsaha.add(izinUsahaModel);
					
				}
				return listPendaftaranSuratIzinUsaha;
			} catch(SQLException e){
				MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database SQL Error", e.getMessage());
				return null;
			}
		}
		else{
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada pembuatan LOAD DATA tabel SURAT_IZIN_USAHA.");
			return null;
		}
	}
	
	public void deleteSuratIzinUsaha(Integer id){
		String query = "DELETE FROM SURAT_IZIN_USAHA WHERE ID = "+id;
		boolean result = db.ExecuteStatementQuery(query, DBConnection.INSTANCE.open());
		if(!result)
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada DELETE tabel SURAT_IZIN_USAHA dengan ID = "+id);
	}
	
	public void deleteSuratIzinUsaha(String npwpd){
		String query = "DELETE FROM SURAT_IZIN_USAHA WHERE NPWPD = '"+npwpd+"';";
		boolean result = db.ExecuteStatementQuery(query, DBConnection.INSTANCE.open());
		if(!result)
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada DELETE tabel SURAT_IZIN_USAHA dengan NPWPD = "+npwpd);
		
	}
	
	public Integer getLastId(){
		String query = "SELECT TOP 1 ID FROM SURAT_IZIN_USAHA ORDER BY ID DESC";
		try{
			ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
			if(result.next()){
				return result.getInt("IDX");
			}
			else
				return 0;
		} catch(SQLException e){
			e.printStackTrace();
			return 0;
		}
	}
}