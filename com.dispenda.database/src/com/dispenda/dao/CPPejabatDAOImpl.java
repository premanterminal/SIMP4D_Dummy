package com.dispenda.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;

import com.dispenda.main.MainActivator;
import com.dispenda.model.Pejabat;
import com.dispenda.database.DBOperation;

public class CPPejabatDAOImpl {
	private Pejabat pejabatModel = new Pejabat();
	private DBOperation db = new DBOperation();
	
	public boolean savePejabat(Pejabat pejabatModel){
		String query = "MERGE INTO PEJABAT USING (VALUES(" +
				""+pejabatModel.getIdPejabatNIP()+"," +
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
				"PEJABAT.NAMAPEJABAT = vals.nama," +
				"PEJABAT.PANGKATPEJABAT = vals.pangkat," +
				"PEJABAT.JABATANPEJABAT = vals.jabatan" +
			" WHEN NOT MATCHED THEN INSERT VALUES " +
				"vals.id," +
				"vals.nama," +
				"vals.pangkat," +
				"vals.jabatan";
		boolean result = db.ExecuteStatementQuery(query, MainActivator.getDefault().getConnection());
		if(!result)
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada MERGE(UPDATE/INSERT) tabel PEJABAT.");
		return result;
	}
	
	public Pejabat getPejabat(int idPejabatNIP){
		String query = "SELECT * FROM PEJABAT WHERE PEJABAT.IDPEJABAT = "+idPejabatNIP;
		ResultSet result = db.ResultExecutedStatementQuery(query, MainActivator.getDefault().getConnection());
		if(result!=null){
			try{
				while(result.next()){
					this.pejabatModel.setIdPejabatNIP(result.getInt("IDPEJABAT"));
					this.pejabatModel.setNamaPejabat(result.getString("NAMAPEJABAT"));
					this.pejabatModel.setPangkat(result.getString("PANGKATPEJABAT"));
					this.pejabatModel.setJabatan(result.getString("JABATANPEJABAT"));
				}
				return this.pejabatModel;
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
		String query = "SELECT * FROM PEJABAT";
		ResultSet result = db.ResultExecutedStatementQuery(query, MainActivator.getDefault().getConnection());
		if(result!=null){
			try{
				List<Pejabat> listPejabat = new ArrayList<Pejabat>();
				while(result.next()){
					Pejabat pejabatModel = new Pejabat();
					pejabatModel.setIdPejabatNIP(result.getInt("IDPEJABAT"));
					pejabatModel.setNamaPejabat(result.getString("NAMAPEJABAT"));
					pejabatModel.setPangkat(result.getString("PANGKATPEJABAT"));
					pejabatModel.setJabatan(result.getString("JABATANPEJABAT"));
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
	
	public void deletePejabat(Integer idPejabatNIP){
		String query = "DELETE FROM PEJABAT WHERE IDPEJABAT = "+idPejabatNIP;
		boolean result = db.ExecuteStatementQuery(query, MainActivator.getDefault().getConnection());
		if(!result)
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada DELETE tabel PEJABAT dengan IDPEJABAT = "+idPejabatNIP);
	}
	
	public Integer getLastIdPejabat(){
		String query = "SELECT TOP 1 IDPEJABAT FROM PEJABAT ORDER BY IDPEJABAT DESC";
		try{
			ResultSet result = db.ResultExecutedStatementQuery(query, MainActivator.getDefault().getConnection());
			if(result.next()){
				return result.getInt("IDPEJABAT");
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
