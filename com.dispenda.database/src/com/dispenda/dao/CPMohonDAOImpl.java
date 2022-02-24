package com.dispenda.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;

import com.dispenda.model.Mohon;
import com.dispenda.database.DBConnection;
import com.dispenda.database.DBOperation;

public class CPMohonDAOImpl {
	private Mohon mohonModel = new Mohon();
	private DBOperation db = new DBOperation();
	
	public boolean saveMohon(Mohon mohonModel){
		String query = "MERGE INTO MOHON USING (VALUES(" +
				""+mohonModel.getIdMohon()+"," +
				"'"+mohonModel.getNoMohon()+"'," +
				"'"+mohonModel.getTglMohon()+"'," +
				""+mohonModel.getJenisMohon()+"," +
				"'"+mohonModel.getAlasanMohon()+"'," +
				"'"+mohonModel.getTglJatuhTempo()+"'," +
				"'"+mohonModel.getNpwpd()+"'," +
				"'"+mohonModel.getNoSkp()+"'," +
				"'"+mohonModel.getStatusMohon()+"'," +
				"'"+mohonModel.getNamaPemohon()+"'," +
				"'"+mohonModel.getJabatanPemohon()+"'," +
				"'"+mohonModel.getAlamatPemohon()+"'))" +
			" AS vals(" +
				"id," +
				"no," +
				"tgl," +
				"jenis," +
				"alasan," +
				"tgljatuhtempo," +
				"npwpd," +
				"no_skp," +
				"status," +
				"nama," +
				"jabatan," +
				"alamat)" +
			" ON MOHON.IDMOHON = vals.id" +
			" WHEN MATCHED THEN UPDATE SET " +
				"MOHON.IDMOHON = vals.id," +
				"MOHON.NOMOHON = vals.no," +
				"MOHON.TGLMOHON = vals.tgl," +
				"MOHON.JENISMOHON = vals.jenis," +
				"MOHON.ALASANMOHON = vals.alasan," +
				"MOHON.TGL_JATUH_TEMPO = vals.tgljatuhtempo," +
				"MOHON.NPWPD_M = vals.npwpd," +
				"MOHON.NOSKP = vals.no_skp," +
				"MOHON.STATUSMOHON = vals.status," +
				"MOHON.NAMAPEMOHON = vals.nama," +
				"MOHON.JABATANPEMOHON = vals.jabatan," +
				"MOHON.ALAMATPEMOHON = vals.alamat" +
			" WHEN NOT MATCHED THEN INSERT VALUES " +
				"vals.id," +
				"vals.no," +
				"vals.tgl," +
				"vals.jenis," +
				"vals.alasan," +
				"vals.tgljatuhtempo," +
				"vals.npwpd," +
				"vals.no_skp," +
				"vals.status," +
				"vals.nama," +
				"vals.jabatan," +
				"vals.alamat" ;
		boolean result = db.ExecuteStatementQuery(query, DBConnection.INSTANCE.open());
		if(!result)
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada MERGE(UPDATE/INSERT) tabel MOHON.");
		return result;
	}
	
	public Mohon getMohon(Integer idMohon){
		String query = "SELECT * FROM MOHON WHERE IDMOHON = "+idMohon;
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		if(result != null){
			try{
				while(result.next()){
					this.mohonModel.setIdMohon(result.getInt("IDMOHON"));
					this.mohonModel.setNoMohon(result.getString("NOMOHON"));
					this.mohonModel.setTglMohon(result.getDate("TGLMOHON"));
					this.mohonModel.setJenisMohon(result.getInt("JENISMOHON"));
					this.mohonModel.setAlasanMohon(result.getString("ALASANMOHON"));
					this.mohonModel.setTglJatuhTempo(result.getDate("TGL_JATUH_TEMPO"));
					this.mohonModel.setNpwpd(result.getString("NPWPD_M"));
					this.mohonModel.setNoSkp(result.getString("NOSKP"));
					this.mohonModel.setStatusMohon(result.getBoolean("STATUSMOHON"));
					this.mohonModel.setNamaPemohon(result.getString("NAMAPEMOHON"));
					this.mohonModel.setJabatanPemohon(result.getString("JABATANPEMOHON"));
					this.mohonModel.setAlamatPemohon(result.getString("ALAMATPEMOHON"));
				}
				return this.mohonModel;
			}
			catch(SQLException e){
				MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", e.getMessage());
				return null;
			}
		}
		else{
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada pembuatan LOAD DATA tabel MOHON.");
			return null;
		}
	}
	
	public List<Mohon> getAllMohon(){
		String query = "SELECT * FROM MOHON";
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		if(result != null){
			try{
				List<Mohon> listMohon = new ArrayList<Mohon>();
				while(result.next()){
					Mohon mohonModel = new Mohon();
					mohonModel.setIdMohon(result.getInt("IDMOHON"));
					mohonModel.setNoMohon(result.getString("NOMOHON"));
					mohonModel.setTglMohon(result.getDate("TGLMOHON"));
					mohonModel.setJenisMohon(result.getInt("JENISMOHON"));
					mohonModel.setAlasanMohon(result.getString("ALASANMOHON"));
					mohonModel.setTglJatuhTempo(result.getDate("TGL_JATUH_TEMPO"));
					mohonModel.setNpwpd(result.getString("NPWPD_M"));
					mohonModel.setNoSkp(result.getString("NOSKP"));
					mohonModel.setStatusMohon(result.getBoolean("STATUSMOHON"));
					mohonModel.setNamaPemohon(result.getString("NAMAPEMOHON"));
					mohonModel.setJabatanPemohon(result.getString("JABATANPEMOHON"));
					mohonModel.setAlamatPemohon(result.getString("ALAMATPEMOHON"));
					listMohon.add(mohonModel);
				}
				return listMohon;
			}
			catch(SQLException e){
				MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", e.getMessage());
				return null;
			}
		}
		else{
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada pembuatan LOAD DATA tabel MOHON.");
			return null;
		}
	}
	
	public void deleteMohon(Integer idMohon){
		String query = "DELETE FROM MOHON WHERE IDMOHON = "+idMohon;
		boolean result = db.ExecuteStatementQuery(query, DBConnection.INSTANCE.open());
		if(!result)
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada DELETE tabel MOHON dengan IDMOHON = "+idMohon);
	}
	
	public Integer getLastIdMohon(){
		String query = "SELECT TOP 1 MOHON FORM MOHON ORDER BY IDMOHON DESC";
		try{
			ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
			if(result.next()){
				return result.getInt("IDMOHON");
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