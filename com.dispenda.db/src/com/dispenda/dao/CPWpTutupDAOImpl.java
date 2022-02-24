package com.dispenda.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;

import com.dispenda.model.WpTutup;
import com.dispenda.database.DBConnection;
import com.dispenda.database.DBOperation;

public class CPWpTutupDAOImpl {
	private DBOperation db = new DBOperation();
	
	public boolean saveWpTutup(WpTutup wpTutupModel){
		String query = "MERGE INTO WP_TUTUP USING (VALUES(" +
				""+ wpTutupModel.getIdTutup() + "," +
				"'"+ wpTutupModel.getNoSuratTutup() + "'," +
				"'"+ wpTutupModel.getTglMulaiTutup() + "'," +
				"'"+ wpTutupModel.getTglSampaiTutup() + "'," +
				"'"+ wpTutupModel.getNpwpd() + "'," +
				"'"+ wpTutupModel.getKeterangan() + "'))" +
			" AS vals(" +
				"idTutup," +
				"noSuratTutup," +
				"tglMulaiTutup," +
				"tglSampaiTutup," +
				"npwpd," +
				"keterangan)" +
			" ON WP_TUTUP.IDTUTUP = vals.idTutup" +
			" WHEN MATCHED THEN UPDATE SET " +
				"WP_TUTUP.NO_SURAT_TUTUP = vals.noSuratTutup," +
				"WP_TUTUP.TGL_MULAI_TUTUP = vals.tglMulaiTutup," +
				"WP_TUTUP.TGL_SAMPAI_TUTUP = vals.tglSampaiTutup," +
				"WP_TUTUP.NPWPD = vals.npwpd," +
				"WP_TUTUP.KETERANGAN = vals.keterangan" +
			" WHEN NOT MATCHED THEN INSERT VALUES " +
				"vals.idTutup," +
				"vals.noSuratTutup," +
				"vals.tglMulaiTutup," +
				"vals.tglSampaiTutup," +
				"vals.npwpd," +
				"vals.keterangan" ;
		boolean result = db.ExecuteStatementQuery(query, DBConnection.INSTANCE.open());
		if(!result)
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada MERGE(UPDATE/INSERT) tabel WP_TUTUP.");
		return result;
	}
	
	public List<WpTutup> getWpTutup(String npwpd){
		String query = "SELECT * FROM WP_TUTUP WHERE NPWPD = "+npwpd;
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		if(result != null){
			try{
				List<WpTutup> listWpTutup = new ArrayList<WpTutup>();
				while(result.next()){
					WpTutup wpTutupModel = new WpTutup();
					wpTutupModel.setIdTutup(result.getInt("IDTUTUP"));
					wpTutupModel.setNoSuratTutup(result.getString("NO_SURAT_TUTUP"));
					wpTutupModel.setTglMulaiTutup(result.getDate("TGL_MULAI_TUTUP"));
					wpTutupModel.setTglSampaiTutup(result.getDate("TGL_SAMPAI_TUTUP"));
					wpTutupModel.setNpwpd(result.getString("NPWPD"));
					wpTutupModel.setKeterangan(result.getString("KETERANGAN"));
					listWpTutup.add(wpTutupModel);
				}
				return listWpTutup;
			}
			catch(SQLException e){
				MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", e.getMessage());
				return null;
			}
		}
		else{
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada pembuatan LOAD DATA tabel WP_TUTUP.");
			return null;
		}
	}
	
	public List<WpTutup> getAllWpTutup(){
		String query = "SELECT * FROM WP_TUTUP";
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		if(result!=null){
			try{
				List<WpTutup> listWpTutup = new ArrayList<WpTutup>();
				while(result.next()){
					WpTutup wpTutupModel = new WpTutup();
					wpTutupModel.setIdTutup(result.getInt("IDTUTUP"));
					wpTutupModel.setNoSuratTutup(result.getString("NO_SURAT_TUTUP"));
					wpTutupModel.setTglMulaiTutup(result.getDate("TGL_MULAI_TUTUP"));
					wpTutupModel.setTglSampaiTutup(result.getDate("TGL_SAMPAI_TUTUP"));
					wpTutupModel.setNpwpd(result.getString("NPWPD"));
					wpTutupModel.setKeterangan(result.getString("KETERANGAN"));
					listWpTutup.add(wpTutupModel);
				}
				return listWpTutup;
			}
			catch(SQLException e){
				MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", e.getMessage());
				return null;
			}
		}
		else{
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada pembuatan LOAD DATA tabel WP_TUTUP.");
			return null;
		}
	}
	
	public void deleteWpTutup(Integer idTutup){
		String query = "DELETE FROM WP_TUTUP WHERE ID_TUTUP = "+ idTutup;
		boolean result = db.ExecuteStatementQuery(query, DBConnection.INSTANCE.open());
		if(!result)
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada DELETE tabel WP_TUTUP dengan IDTUTUP = "+idTutup);
	}
	
	public Integer getLastIdTutup(){
		String query = "SELECT TOP 1 IDTUTUP FROM WP_TUTUP ORDER BY IDTUTUP DESC";
		try{
			ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
			if(result.next()){
				return result.getInt("IDTUTUP");
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
