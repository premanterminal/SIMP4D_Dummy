package com.dispenda.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;

import com.dispenda.main.MainActivator;
import com.dispenda.model.UU;
import com.dispenda.database.DBOperation;

public class CPUUDAOImpl {
	private UU uuModel = new UU();
	private DBOperation db = new DBOperation();
	
	public boolean saveUU(UU uuModel){
		String query = "MERGE INTO UU USING (VALUES(" +
				""+uuModel.getIdUU()+"," +
				""+uuModel.getIdPajak()+"," +
				"'"+uuModel.getUU()+"'))" +
			" AS vals(" +
				"id," +
				"id_pajak," +
				"uu)" +
			" ON UU.IDUU = vals.id" +
			" WHEN MATCHED THEN UPDATE SET " +
				"UU.IDPAJAK = vals.id_pajak," +
				"UU.UNDANGUNDANG = vals.uu" +
			" WHEN NOT MATCHED THEN INSERT VALUES " +
				"vals.id," +
				"vals.id_pajak," +
				"vals.uu" ;
		boolean result = db.ExecuteStatementQuery(query, MainActivator.getDefault().getConnection());
		if(!result)
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada MERGE(UPDATE/INSERT) tabel UU.");
		return result; 
	}
	
	public UU getUndangUndang(int idUU){
		String query = "SELECT * FROM UU WHERE UU.IDUU = "+idUU;
		ResultSet result = db.ResultExecutedStatementQuery(query, MainActivator.getDefault().getConnection());
		if(result != null){
			try{
				while(result.next()){
					this.uuModel.setIdUU(result.getInt("IDUU"));
					this.uuModel.setIdPajak(result.getInt("IDPAJAK"));
					this.uuModel.setUU(result.getString("UNDANGUNDANG"));
				}
				return this.uuModel;
			}
			catch(SQLException e){
				MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database SQL Error", e.getMessage());
				return null;
			}
		}
		else{
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada pembuatan LOAD DATA tabel UU.");
			return null;
		}
		
	}
	
	public List<UU> getAllUndangUndang(){
		String query = "SELECT * FROM UU";
		ResultSet result = db.ResultExecutedStatementQuery(query, MainActivator.getDefault().getConnection());
		if(result != null){
			try{
				List<UU> listUU = new ArrayList<UU>();
				while(result.next()){
					UU uuModel = new UU();
					this.uuModel.setIdUU(result.getInt("IDUU"));
					this.uuModel.setIdPajak(result.getInt("IDPAJAK"));
					this.uuModel.setUU(result.getString("UNDANGUNDANG"));
					listUU.add(uuModel);
				}
				return listUU;
			}
			catch(SQLException e){
				MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database SQL Error", e.getMessage());
				return null;
			}
		}
		else{
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada pembuatan LOAD DATA tabel UU.");
			return null;
		}
	}
	
	public void deleteUU(Integer idUU){
		String query = "DELETE FROM UU WHERE IDUU = "+idUU;
		boolean result = db.ExecuteStatementQuery(query, MainActivator.getDefault().getConnection());
		if(!result)
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada DELETE tabel UU dengan idUU = "+idUU);
	}
	
	public Integer getLastIdUU(){
		String query = "SELECT TOP 1 IDUU FROM UU ORDER BY IDUU DESC";
		try{
			ResultSet result = db.ResultExecutedStatementQuery(query, MainActivator.getDefault().getConnection());
			if(result.next()){
				return result.getInt("IDUU");
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
