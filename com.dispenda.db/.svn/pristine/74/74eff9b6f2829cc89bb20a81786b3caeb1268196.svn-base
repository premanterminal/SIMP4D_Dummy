package com.dispenda.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;

import com.dispenda.database.DBConnection;
import com.dispenda.database.DBOperation;
import com.dispenda.model.Kecamatan;

public class CPKecamatanDAOImpl {
	private Kecamatan kecamatanModel = new Kecamatan();
	private DBOperation db = new DBOperation();
	
	public boolean saveKecamatan(Kecamatan kecamatanModel){
		String query = "MERGE INTO KECAMATAN USING (VALUES(" +
				""+kecamatanModel.getIdKecamatan()+"," +
				"'"+kecamatanModel.getKodeKecamatan()+"'," +
				"'"+kecamatanModel.getNamaKecamatan()+"'))" +
			" AS vals(" +
				"id," +
				"kode," +
				"name)" +
			" ON KECAMATAN.IDKEC = vals.id" +
			" WHEN MATCHED THEN UPDATE SET " +
				"KECAMATAN.KODE_KECAMATAN = vals.kode," +
				"KECAMATAN.NAME_KECAMATAN = vals.name" +
			" WHEN NOT MATCHED THEN INSERT VALUES " +
				"vals.id," +
				"vals.kode," +
				"vals.name";
		boolean result = db.ExecuteStatementQuery(query, DBConnection.INSTANCE.open());
		if(!result)
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada MERGE(UPDATE/INSERT) tabel KECAMATAN.");
		return result;
	}
	
	public Kecamatan getKecamatan(int idKecamatan){
		String query = "SELECT * FROM KECAMATAN WHERE KECAMATAN.IDKEC = "+idKecamatan+"";
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		if(result != null){
			try{
				while(result.next()){
					this.kecamatanModel.setIdKecamatan(result.getInt("IDKEC"));
					this.kecamatanModel.setKodeKecamatan(result.getString("KODE_KECAMATAN"));
					this.kecamatanModel.setNamaKecamatan(result.getString("NAME_KECAMATAN"));
				}
				return this.kecamatanModel;
			}
			catch(SQLException e){
				MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database SQL Error", e.getMessage());
				return null;
			}
		}
		else{
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada pembuatan LOAD DATA tabel KECAMATAN.");
			return null;
		}
	}
	
	public List<Kecamatan> getAllKecamatan(){
		String query = "SELECT * FROM KECAMATAN";
		ResultSet result = db.ResultExecutedStatementQuery(query,DBConnection.INSTANCE.open());
		if(result != null){
			try{
				List<Kecamatan> listKecamatan = new ArrayList<Kecamatan>();
				while(result.next()){
					Kecamatan kecamatanModel = new Kecamatan();
					kecamatanModel.setIdKecamatan(result.getInt("IDKEC"));
					kecamatanModel.setKodeKecamatan(result.getString("KODE_KECAMATAN"));
					kecamatanModel.setNamaKecamatan(result.getString("NAME_KECAMATAN"));
					listKecamatan.add(kecamatanModel);
				}
				return listKecamatan;
			}
			catch(SQLException e){
				MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", e.getMessage());
				return null;
			}
		}
		else{
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada pembuatan LOAD DATA tabel KECAMATAN.");
			return null;
		}
	}
	
	public Integer getLastIdKecamatan(){
		String query = "SELECT TOP 1 IDKEC FROM KECAMATAN ORDER BY IDKEC DESC";
		try{
			ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
			if(result.next()){
				return result.getInt("IDKEC");
			}
			else
				return 0;
		}
		catch(SQLException e){
			e.printStackTrace();
			return 0;
		}
	}

    public void deleteKecamatan(Integer idKecamatan){
    	String query = "DELETE FROM KECAMATAN WHERE IDKEC = "+idKecamatan;
    	boolean result = db.ExecuteStatementQuery(query,DBConnection.INSTANCE.open());
    	if(!result)
    		MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada DELETE table KECAMATAN dengan IDKEC = "+idKecamatan);
    }
}
