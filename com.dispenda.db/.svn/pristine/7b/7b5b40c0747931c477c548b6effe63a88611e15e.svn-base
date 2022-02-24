package com.dispenda.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;

import com.dispenda.model.Pejabat;
import com.dispenda.database.DBConnection;
import com.dispenda.database.DBOperation;

public class CPPejabatDAOImpl {
	private DBOperation db = new DBOperation();
	
	public boolean savePejabat(Pejabat pejabatModel){
		String query = "MERGE INTO PEJABAT USING (VALUES(" +
				"'"+pejabatModel.getIdPejabatNIP()+"'," +
				"'"+pejabatModel.getNamaPejabat()+"'," +
				"'"+pejabatModel.getPangkat()+"'," +
				"'"+pejabatModel.getJabatan()+"'))" +
			" AS vals(" +
				"id," +
				"nama," +
				"pangkat," +
				"jabatan)" +
			" ON PEJABAT.IDPEJABAT = vals.id" +
			" WHEN MATCHED THEN UPDATE SET " +
				"PEJABAT.NAMA_PEJABAT = vals.nama," +
				"PEJABAT.PANGKAT = vals.pangkat," +
				"PEJABAT.JABATAN = vals.jabatan" +
			" WHEN NOT MATCHED THEN INSERT VALUES " +
				"vals.id," +
				"vals.nama," +
				"vals.pangkat," +
				"vals.jabatan";
		boolean result = db.ExecuteStatementQuery(query, DBConnection.INSTANCE.open());
		if(!result)
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada MERGE(UPDATE/INSERT) tabel PEJABAT.");
		return result;
	}
	
	public List<Pejabat> getPejabat(int idPejabatNIP){
		String query = "SELECT * FROM PEJABAT WHERE PEJABAT.IDPEJABAT = "+idPejabatNIP;
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		if(result!=null){
			try{
				List<Pejabat> listPejabat = new ArrayList<Pejabat>();
				while(result.next()){
					Pejabat pejabatModel = new Pejabat();
					pejabatModel.setIdPejabatNIP(result.getString("IDPEJABAT"));
					pejabatModel.setNamaPejabat(result.getString("NAMA_PEJABAT"));
					pejabatModel.setPangkat(result.getString("PANGKAT"));
					pejabatModel.setJabatan(result.getString("JABATAN"));
					listPejabat.add(pejabatModel);
				}
				return listPejabat;
			}
			catch(SQLException e){
				MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database SQL Error", e.getMessage());
				return null;
			}
		}
		else{
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada pembuatan LOAD DATA tabel PEJABAT.");
			return null;
		}
	}
	
	public List<Pejabat> getAllPejabat(){
		String query = "SELECT * FROM PEJABAT order by nama_pejabat";
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		if(result!=null){
			try{
				List<Pejabat> listPejabat = new ArrayList<Pejabat>();
				while(result.next()){
					Pejabat pejabatModel = new Pejabat();
					pejabatModel.setIdPejabatNIP(result.getString("IDPEJABAT"));
					pejabatModel.setNamaPejabat(result.getString("NAMA_PEJABAT"));
					pejabatModel.setPangkat(result.getString("PANGKAT"));
					pejabatModel.setJabatan(result.getString("JABATAN"));
					listPejabat.add(pejabatModel);
				}
				return listPejabat;
			}
			catch(SQLException e){
				MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database SQL Error", e.getMessage());
				return null;
			}
		}
		else{
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada pembuatan LOAD DATA tabel PEJABAT.");
			return null;
		}
	}
	
	public void deletePejabat(String idPejabatNIP){
		String query = "DELETE FROM PEJABAT WHERE IDPEJABAT = '"+idPejabatNIP+"'";
		boolean result = db.ExecuteStatementQuery(query, DBConnection.INSTANCE.open());
		if(!result)
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada DELETE tabel PEJABAT dengan IDPEJABAT = "+idPejabatNIP);
	}
	
	public String getLastIdPejabat(){
		String query = "SELECT TOP 1 IDPEJABAT FROM PEJABAT ORDER BY IDPEJABAT DESC";
		try{
			ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
			if(result.next()){
				return result.getString("IDPEJABAT");
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
