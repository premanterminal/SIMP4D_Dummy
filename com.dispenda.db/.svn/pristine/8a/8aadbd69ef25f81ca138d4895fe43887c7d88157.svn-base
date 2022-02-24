package com.dispenda.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;

import com.dispenda.database.DBConnection;
import com.dispenda.database.DBOperation;
import com.dispenda.model.Kelurahan;

public class CPKelurahanDAOImpl {
	private DBOperation db = new DBOperation();
	
	public boolean saveKelurahan(Kelurahan kelurahanModel){
		String query = "MERGE INTO KELURAHAN USING (VALUES(" +
				""+kelurahanModel.getIdKelurahan()+"," +
				"'"+kelurahanModel.getKodeKelurahan()+"'," +
				""+kelurahanModel.getIdKecamatan()+"," +
				"'"+kelurahanModel.getNamaKelurahan()+"'))" +
			" AS vals(" +
				"id," +
				"kode," +
				"idKecamatan," +
				"name)" +
			" ON KELURAHAN.IDKEL = vals.id AND KELURAHAN.KECAMATAN_IDKECAMATAN = vals.idKecamatan" +
			" WHEN MATCHED THEN UPDATE SET " +
				"KELURAHAN.KODE_KELURAHAN = vals.kode," +
				"KELURAHAN.NAME_KELURAHAN = vals.name" +
			" WHEN NOT MATCHED THEN INSERT VALUES " +
				"vals.id," +
				"vals.kode," +
				"vals.idKecamatan," +
				"vals.name";
		boolean result = db.ExecuteStatementQuery(query, DBConnection.INSTANCE.open());
		if(!result)
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada MERGE(UPDATE/INSERT) tabel KECAMATAN.");
		return result;
	}
	
	public List<Kelurahan> getAllKelurahan(Integer idKecamatan){
		String query = "SELECT * FROM KELURAHAN WHERE KELURAHAN.KECAMATAN_IDKECAMATAN = "+idKecamatan;
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		if(result != null){
			try{
				List<Kelurahan> listKelurahan = new ArrayList<Kelurahan>();
				while(result.next()){
					Kelurahan kelurahanModel = new Kelurahan();
					kelurahanModel.setIdKelurahan(result.getInt("IDKEL"));
					kelurahanModel.setKodeKelurahan(result.getString("KODE_KELURAHAN"));
					kelurahanModel.setNamaKelurahan(result.getString("NAME_KELURAHAN"));
					kelurahanModel.setIdKecamatan(result.getInt("KECAMATAN_IDKECAMATAN"));
					listKelurahan.add(kelurahanModel);
				}
				return listKelurahan;
			}
			catch(SQLException e){
				MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database SQL Error", e.getMessage());
				return null;
			}
		}
		else{
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada pembuatan LOAD DATA tabel KELURAHAN.");
			return null;
		}
	}
	
	public List<Kelurahan> getAllKelurahan(){
		String query = "SELECT * FROM KELURAHAN";
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		if(result != null){
			try{
				List<Kelurahan> listKelurahan = new ArrayList<Kelurahan>();
				while(result.next()){
					Kelurahan kelurahanModel = new Kelurahan();
					kelurahanModel.setIdKelurahan(result.getInt("IDKEL"));
					kelurahanModel.setKodeKelurahan(result.getString("KODE_KELURAHAN"));
					kelurahanModel.setNamaKelurahan(result.getString("NAME_KELURAHAN"));
					kelurahanModel.setIdKecamatan(result.getInt("KECAMATAN_IDKECAMATAN"));
					listKelurahan.add(kelurahanModel);
				}
				return listKelurahan;
			}
			catch(SQLException e){
				MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", e.getMessage());
				return null;
			}
		}
		else{
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada pembuatan LOAD DATA tabel KELURAHAN.");
			return null;
		}
	}
	
	public Integer getLastIdKelurahan(){
		String query = "SELECT TOP 1 IDKEL FROM KELURAHAN ORDER BY IDKEL DESC";
		try{
			ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
			if(result.next()){
				return result.getInt("IDKEL");
			}
			else
				return 0;
		}
		catch(SQLException e){
			e.printStackTrace();
			return 0;
		}
	}
	
	public String getKelurahanByID(int idKel, int idKec){
		String query = "SELECT NAME_KELURAHAN FROM KELURAHAN WHERE Kode_Kelurahan = " + idKel + " AND KECAMATAN_IDKECAMATAN = " + idKec;
		try{
			ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
			if(result.next()){
				return result.getString("NAME_KELURAHAN");
			}
			else
				return "";
		}
		catch(SQLException e){
			e.printStackTrace();
			return "";
		}
	}

    public void deleteKelurahan(Integer idKelurahan){
    	String query = "DELETE FROM KELURAHAN WHERE IDKEL = "+idKelurahan;
    	boolean result = db.ExecuteStatementQuery(query,DBConnection.INSTANCE.open());
    	if(!result)
    		MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada DELETE table KELURAHAN dengan IDKEL = "+idKelurahan);
    }
}
