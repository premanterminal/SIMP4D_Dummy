package com.dispenda.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;

import com.dispenda.database.DBConnection;
import com.dispenda.database.DBOperation;
import com.dispenda.model.Modul;
import com.dispenda.model.SubModul;

public class CPModulDAOImpl {
	private DBOperation db = new DBOperation();
	
	//FUNGSI TABEL MODUL
	public boolean saveModul(Modul modulModel){
		String query = "MERGE INTO MODUL USING (VALUES(" +
				"'"+modulModel.getIdModul()+"'," +
				"'"+modulModel.getNamaModul()+"'))" +
			" AS vals(" +
				"id," +
				"nama)" +
			" ON MODUL.IDMODUL = vals.id" +
			" WHEN MATCHED THEN UPDATE SET " +
				"MODUL.MODUL_NAME = vals.nama" +
			" WHEN NOT MATCHED THEN INSERT VALUES " +
				"vals.id," +
				"vals.nama";
		boolean result = db.ExecuteStatementQuery(query, DBConnection.INSTANCE.open());
		if(!result)
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada MERGE(UPDATE/INSERT) tabel MODUL.");
		return result;
	}
	
	public List<Modul> getAllModul() {
		String query = "SELECT * FROM MODUL";
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		if(result != null){
			try{
				List<Modul> listModul = new ArrayList<Modul>();
				while(result.next()){
					Modul modulModel = new Modul();
					modulModel.setIdModul(result.getInt("IDMODUL"));
					modulModel.setNamaModul(result.getString("MODUL_NAME"));
					listModul.add(modulModel);
				}
				return listModul;
			} catch (SQLException e){
				MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database SQL Error", e.getMessage());
				return null;
			}
		}
		else{
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database SQL Error", "Ada masalah pada pembuatan LOAD DATA tabel MODUL.");
			return null;
		}
	}
	
	public void deleteModul(Integer idModul){
		String query = "DELETE FROM MODUL WHERE IDMODUL ="+idModul;
		boolean result = db.ExecuteStatementQuery(query, DBConnection.INSTANCE.open());
		if(!result)
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada DELETE tabel MODUL dengan IDMODUL ="+idModul);
	}
	
	public Integer getLastModul(){
		String query = "SELECT TOP 1 IDMODUL FROM MODUL ORDER BY IDMODUL DESC";
		try{
			ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
			if(result.next()){
				return result.getInt("IDMODUL");
			}
			else
				return 0;
		}
		catch(SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	//FUNGSI TABEL SUBMODUL
	public boolean saveSubModul(SubModul subModulModel){
		String query = "MERGE INTO SUBMODUL USING (VALUES(" +
				"'"+subModulModel.getIdSubModul()+"'," +
				"'"+subModulModel.getNamaSubModul()+"'," +
				"'"+subModulModel.getIdModul()+"'))" +
			" AS vals(" +
				"idSubModul," +
				"nama," +
				"idModul)" +
			" ON SUBMODUL.IDSUBMODUL = vals.idSubModul AND SUBMODUL.IDMODUL = vals.idModul" +
			" WHEN MATCHED THEN UPDATE SET " +
				"SUBMODUL.SUBMODUL_NAME = vals.nama" +
			" WHEN NOT MATCHED THEN INSERT VALUES " +
				"vals.idSubModul," +
				"vals.nama" +
				"vals.idModul" ;
				
		boolean result = db.ExecuteStatementQuery(query, DBConnection.INSTANCE.open());
		if(!result)
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada MERGE(UPDATE/INSERT) tabel SUBMODUL.");
		return result;
	}
	
	public List<SubModul> getAllSubModul() {
		String query = "SELECT * FROM SUBMODUL";
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		if(result != null){
			try{
				List<SubModul> listSubModul = new ArrayList<SubModul>();
				while(result.next()){
					SubModul subModulModel = new SubModul();
					subModulModel.setIdSubModul(result.getInt("IDSUBMODUL"));
					subModulModel.setNamaSubModul(result.getString("SUBMODUL_NAME"));
					subModulModel.setIdModul(result.getInt("IDMODUL"));
					listSubModul.add(subModulModel);
				}
				return listSubModul;
			} catch (SQLException e){
				MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database SQL Error", e.getMessage());
				return null;
			}
		}
		else{
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database SQL Error", "Ada masalah pada pembuatan LOAD DATA tabel SUBMODUL.");
			return null;
		}
	}
	
	public void deleteSubModul(Integer idSubModul){
		String query = "DELETE FROM SUBMODUL WHERE IDSUBMODUL ="+idSubModul;
		boolean result = db.ExecuteStatementQuery(query, DBConnection.INSTANCE.open());
		if(!result)
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada DELETE tabel MODUL dengan IDSUBMODUL ="+idSubModul);
	}
	
	public Integer getLastSubModul(){
		String query = "SELECT TOP 1 IDSUBMODUL FROM SUBMODUL ORDER BY IDSUBMODUL DESC";
		try{
			ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
			if(result.next()){
				return result.getInt("IDSUBMODUL");
			}
			else
				return 0;
		}
		catch(SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}
}