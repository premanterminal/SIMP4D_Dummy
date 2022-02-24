package com.dispenda.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;

import com.dispenda.model.Pajak;
import com.dispenda.database.DBConnection;
import com.dispenda.database.DBOperation;

public class CPPajakDAOImpl {
	private Pajak pajakModel = new Pajak();
	private DBOperation db = new DBOperation();
	
	public boolean savePajak(Pajak pajakModel){
		String query = "MERGE INTO PAJAK USING (VALUES(" +
				""+pajakModel.getIdPajak()+"," +
				"'"+pajakModel.getKodePajak()+"'," +
				"'"+pajakModel.getNamaPajak()+"'," +
				"'"+pajakModel.getKodeDenda()+"'))" +
			" AS vals(" +
				"id," +
				"kode," +
				"nama," +
				"kode_denda)" +
			" ON PAJAK.IDPAJAK = vals.id" +
			" WHEN MATCHED THEN UPDATE SET " +
				"PAJAK.KODEPAJAK = vals.kode," +
				"PAJAK.NAMAPAJAK = vals.nama," +
				"PAJAK.KODEDENDA = vals.kode_denda" +
			" WHEN NOT MATCHED THEN INSERT VALUES " +
				"vals.id," +
				"vals.kode," +
				"vals.nama," +
				"vals.kode_denda";
		boolean result = db.ExecuteStatementQuery(query, DBConnection.INSTANCE.open());
		if(!result)
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada MERGER(UPDATE/INSERT) tabel PAJAK.");
		return result;
	}
	
	public Pajak getPajak(int idPajak){
		String query = "SELECT * FROM PAJAK WHERE PAJAK.IDPAJAK = "+idPajak;
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		if(result!=null){
			try{
				while(result.next()){
					this.pajakModel.setIdPajak(result.getInt("IDPAJAK"));
					this.pajakModel.setKodePajak(result.getString("KODEPAJAK"));
					this.pajakModel.setNamaPajak(result.getString("NAMAPAJAK"));
					this.pajakModel.setKodeDenda(result.getString("KODEDENDA"));
				}
				return this.pajakModel;
			}
			catch(SQLException e){
				MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database SQL Error", e.getMessage());
				return null;
			}
		}
		else{
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada pembuatan LOAD DATA tabel PAJAK.");
			return null;
		}
	}
	
	public List<Pajak> getAllPajak(){
		String query = "SELECT * FROM PAJAK";
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		if(result!=null){
			try{
				List<Pajak> listPajak = new ArrayList<Pajak>();
				while(result.next()){
					Pajak pajakModel = new Pajak();
					pajakModel.setIdPajak(result.getInt("IDPAJAK"));
					pajakModel.setKodePajak(result.getString("KODEPAJAK"));
					pajakModel.setNamaPajak(result.getString("NAMAPAJAK"));
					pajakModel.setKodeDenda(result.getString("KODEDENDA"));
					listPajak.add(pajakModel);
				}
				return listPajak;
			}
			catch(SQLException e){
				MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database SQL Error", e.getMessage());
				return null;
			}
		}
		else{
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database SQL Error", "Ada masalah pada pembuatan LOAD DATA tabel PAJAK.");
			return null;
		}
	}
	
	public void deletePajak(Integer idPajak){
		String query = "DELETE FORM PAJAK WHERE IDPAJAK = "+idPajak;
		boolean result = db.ExecuteStatementQuery(query, DBConnection.INSTANCE.open());
		if(!result)
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada DELETE tabel PAJAK dengan IDPAJAK = "+idPajak);
	}
	
	public Integer getLastIdPajak(){
		String query = "SELECT TOP 1 IDPAJAK FROM PAJAK ORDER BY IDPAJAK DESC";
		try{
			ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
			if(result.next()){
				return result.getInt("IDPAJAK");
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
