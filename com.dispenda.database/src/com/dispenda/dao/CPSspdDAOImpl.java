package com.dispenda.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;

import com.dispenda.main.MainActivator;
import com.dispenda.model.Sspd;
import com.dispenda.database.DBOperation;

public class CPSspdDAOImpl {
	private Sspd sspdModel = new Sspd();
	private DBOperation db = new DBOperation();
	
	public boolean saveSspd(Sspd sspdModel){
		String query = "MERGE INTO SSPD USING (VALUES(" +
				"'"+sspdModel.getNoSspd()+"'," +
				"'"+sspdModel.getTglSspd()+"'," +
				"'"+sspdModel.getNpwpd()+"'," +
				"'"+sspdModel.getBulanSkp()+"'," +
				"'"+sspdModel.getNoSkp()+"'," +
				""+sspdModel.getCicilanKe()+"," +
				""+sspdModel.getDenda()+"," +
				""+sspdModel.getJumlahBayar()+"," +
				""+sspdModel.getJenisBayar()+"," +
				"'"+sspdModel.getCaraBayar()+"'," +
				"'"+sspdModel.getNamaPenyetor()+"'," +
				"'"+sspdModel.getLunas()+"'," +
				""+sspdModel.getIdSubPajak()+"," +
				"'"+sspdModel.getDendaLunas()+"'))" +
			" AS vals(" +
				"no," +
				"tgl," +
				"npwpd," +
				"bln_skp," +
				"no_skp," +
				"cicilan_ke," +
				"denda," +
				"jlh_bayar," +
				"jns_bayar," +
				"cara_bayar," +
				"nama_penyetor," +
				"lunas," +
				"id_subpajak," +
				"denda_lunas)" +
			" ON SSPD.NO_SSPD = vals.no" +
			" WHEN MATCHED THEN UPDATE SET " +
				"SSPD.TGL_SSPD = vals.tgl," +
				"SSPD.NPWPD_S = vals.npwpd," +
				"SSPD.BULAN_SKP = vals.bln_skp," +
				"SSPD.NOMOR_SKP = vals.no_skp," +
				"SSPD.CICILAN = vals.cicilan_ke," +
				"SSPD.DENDA_S = vals.denda," +
				"SSPD.JUMLAH_BAYAR = vals.jlh_bayar," +
				"SSPD.JENIS_BAYAR = vals.jns_bayar," +
				"SSPD.HOWTO_PAY = vals.cara_bayar," +
				"SSPD.NAME_PENYETOR = vals.nama_penyetor," +
				"SSPD.LUNASS = vals.lunas," +
				"SSPD.IDSUBPAJAK = vals.id_subpajak," +
				"SSPD.DENDALUNAS = vals.denda_lunas" +
			" WHEN NOT MATCHED THEN INSERT VALUES " +
				"vals.no," +
				"vals.tgl," +
				"vals.npwpd," +
				"vals.bln_skp," +
				"vals.no_skp," +
				"vals.cicilan_ke," +
				"vals.denda," +
				"vals.jlh_bayar," +
				"vals.jns_bayar," +
				"vals.cara_bayar," +
				"vals.nama_penyetor," +
				"vals.lunas," +
				"vals.id_subpajak," +
				"vals.denda_lunas";
		boolean result = db.ExecuteStatementQuery(query, MainActivator.getDefault().getConnection());
		if(!result)
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada MERGE(UPDATE/INSERT) tabel SSPD.");
		return result;
	}
	
	public Sspd getSspd(String no_sspd){
		String query = "SELECT * FROM SSPD WHERE SSPD.NO_SSPD"+no_sspd+"";
		ResultSet result = db.ResultExecutedStatementQuery(query, MainActivator.getDefault().getConnection());
		if(result != null){
			try{
				while(result.next()){
					this.sspdModel.setNoSspd(result.getString("NO_SSPD"));
					this.sspdModel.setTglSspd(result.getDate("TGL_SSPD"));
					this.sspdModel.setNpwpd(result.getString("NPWP_S"));
					this.sspdModel.setBulanSkp(result.getDate("BULAN_SKP"));
					this.sspdModel.setNoSkp(result.getString("NOMOR_SKP"));
					this.sspdModel.setCicilanKe(result.getInt("CICILAN"));
					this.sspdModel.setDenda(result.getDouble("DENDA_S"));
					this.sspdModel.setJumlahBayar(result.getDouble("JUMLAH_BAYAR"));
					this.sspdModel.setJenisBayar(result.getInt("JENIS_BAYAR"));
					this.sspdModel.setCaraBayar(result.getString("HOWTO_PAY"));
					this.sspdModel.setNamaPenyetor(result.getString("NAME_PENYETOR"));
					this.sspdModel.setLunas(result.getBoolean("LUNASS"));
					this.sspdModel.setIdSubPajak(result.getInt("IDSUBPAJAK"));
					this.sspdModel.setDendaLunas(result.getBoolean("DENDALUNAS"));
				}
				return this.sspdModel;
			} 
			catch(SQLException e){
				MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database SQL Error", e.getMessage());
				return null;
			}
		}
		else{
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada pembuatan LOAD DATA tabel SSPD.");
			return null;
		}
	}
	
	public List<Sspd> getAllSspd(){
		String query = "SELECT * FROM SSPD";
		ResultSet result = db.ResultExecutedStatementQuery(query, MainActivator.getDefault().getConnection());
		if(result != null){
			try{
				List<Sspd> listSspd = new ArrayList<Sspd>();
				while(result.next()){
					Sspd sspdModel = new Sspd();
					sspdModel.setNoSspd(result.getString("NO_SSPD"));
					sspdModel.setTglSspd(result.getDate("TGL_SSPD"));
					sspdModel.setNpwpd(result.getString("NPWP_S"));
					sspdModel.setBulanSkp(result.getDate("BULAN_SKP"));
					sspdModel.setNoSkp(result.getString("NOMOR_SKP"));
					sspdModel.setCicilanKe(result.getInt("CICILAN"));
					sspdModel.setDenda(result.getDouble("DENDA_S"));
					sspdModel.setJumlahBayar(result.getDouble("JUMLAH_BAYAR"));
					sspdModel.setJenisBayar(result.getInt("JENIS_BAYAR"));
					sspdModel.setCaraBayar(result.getString("HOWTO_PAY"));
					sspdModel.setNamaPenyetor(result.getString("NAME_PENYETOR"));
					sspdModel.setLunas(result.getBoolean("LUNASS"));
					sspdModel.setIdSubPajak(result.getInt("IDSUBPAJAK"));
					sspdModel.setDendaLunas(result.getBoolean("DENDALUNAS"));
					listSspd.add(sspdModel);
				}
				return listSspd;
			}
			catch(SQLException e){
				MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database SQL Error", e.getMessage());
				return null;
			}
		}
		else{
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada pembuatan LOAD DATA tabel SSPD.");
			return null;
		}
	}
	
	public void deleteSspd(String no_sspd){
		String query = "DELETE FROM SSPD WHERE NO_SSPD = "+no_sspd;
		boolean result = db.ExecuteStatementQuery(query, MainActivator.getDefault().getConnection());
		if(!result)
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada DELETE tabel SSPD dengan NO SSPD = "+no_sspd);
		
	}
	
	public String getLastNoSspd(){
		String query = "SELECT TOP 1 NO_SSPD FROM SSPD ORDER BY NO_SSPD DESC";
		try{
			ResultSet result = db.ResultExecutedStatementQuery(query, MainActivator.getDefault().getConnection());
			if(result.next()){
				return result.getString("NO_SSPD");
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
