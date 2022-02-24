package com.dispenda.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;

import com.dispenda.main.MainActivator;
import com.dispenda.model.Periksa;
import com.dispenda.database.DBOperation;


public class CPPeriksaDAOImpl {
	private Periksa periksaModel = new Periksa();
	private DBOperation db = new DBOperation();
	
	public boolean savePeriksa(Periksa periksaModel){
		String query = "MERGE INTO PERIKSA USING (VALUES(" +
				""+periksaModel.getIdPeriksa()+"," +
				"'"+periksaModel.getNoPeriksa()+"'," +
				"'"+periksaModel.getTglPeriksa()+"'," +
				"'"+periksaModel.getNpwpd()+"'," +
				"'"+periksaModel.getJenisPeriksa()+"'," +
				""+periksaModel.getKenaikanPersen()+"," +
				""+periksaModel.getKenaikan()+"," +
				"'"+periksaModel.getMasaPajakDari()+"'," +
				"'"+periksaModel.getMasaPajakSampai()+"'," +
				"'"+periksaModel.getNoSkp()+"'," +
				"'"+periksaModel.getTglSkp()+"'," +
				"'"+periksaModel.getNoNppd()+"'," +
				"'"+periksaModel.getTglNppd()+"'," +
				""+periksaModel.getIdSubPajak()+"," +
				"'"+periksaModel.getTipeSkp()+"'," +
				""+periksaModel.getTotalPajakPeriksa()+"," +
				""+periksaModel.getTotalPajakTerutang()+"," +
				""+periksaModel.getTotalPajakBunga()+"," +
				""+periksaModel.getTotalKenaikan()+"," +
				""+periksaModel.getTotalDenda()+"," +
				"'"+periksaModel.getKeterangan()+"'))" +
			" AS vals(" +
				"id," +
				"no," +
				"tgl," +
				"npwpd," +
				"jenis," +
				"kenaikan_persen," +
				"kenaikan," +
				"masadari," +
				"masasampai," +
				"no_skp," +
				"tgl_skp," +
				"no_nppd," +
				"tgl_nnpd," +
				"id_sub_pajak," +
				"tipe_skp," +
				"tot_pajak_periksa," +
				"tot_pajak_terutang," +
				"tot_pajak_bunga," +
				"tot_kenaikan," +
				"tot_denda," +
				"ket)" +
			" ON PERIKSA.IDPERIKSA = vals.id" +
			" WHEN MATCHED THEN UPDATE SET " +
				"PERIKSA.NOPERIKSA = vals.no," +
				"PERIKSA.TGLPERIKSA = vals.tgl," +
				"PERIKSA.NPWPD_P = vals.npwpd," +
				"PERIKSA.JENISPERIKSA = vals.jenis," +
				"PERIKSA.KENAIKANPERSEN = vals.kenaikan_persen," +
				"PERIKSA.NAIK = vals.kenaikan," +
				"PERIKSA.MASAPAJAKDARI = vals.masadari," +
				"PERIKSA.MASAPAJAKSAMPAI = vals.masasampai," +
				"PERIKSA.NOSKP = vals.no_skp," +
				"PERIKSA.TGLSKP = vals.tgl_skp," +
				"PERIKSA.NONPPD = vals.no_nppd," +
				"PERIKSA.TGLNPPD = vals.tgl_nppd," +
				"PERIKSA.IDSUBPAJAK = vals.id_sub_pajak," +
				"PERIKSA.TIPESKP = vals.tipe_skp," +
				"PERIKSA.TOTALPAJAKPERIKSA = vals.tot_pajak_periksa," +
				"PERIKSA.TOTALPAJAKTERUTANG = vals.tot_pajak_terutang," +
				"PERIKSA.TOTALPAJAKBUNGA = vals.tot_pajak_bunga," +
				"PERIKSA.TOTALKENAIKAN = vals.tot_kenaikan," +
				"PERIKSA.TOTALDENDA = vals.tot_denda," +
				"PERIKSA.KETERANGAN = vals.ket" +
			" WHEN NOT MATCHED THEN INSERT VALUES " +
				"vals.id," +
				"vals.no," +
				"vals.tgl," +
				"vals.npwpd," +
				"vals.jenis," +
				"vals.kenaikan_persen," +
				"vals.kenaikan," +
				"vals.masadari," +
				"vals.masasampai," +
				"vals.no_skp," +
				"vals.tgl_skp," +
				"vals.no_nppd," +
				"vals.tgl_nppd," +
				"vals.id_sub_pajak," +
				"vals.tipe_skp," +
				"vals.tot_pajak_periksa," +
				"vals.tot_pajak_terutang," +
				"vals.tot_pajak_bunga," +
				"vals.tot_kenaikan," +
				"vals.tot_denda," +
				"vals.ket";
		boolean result = db.ExecuteStatementQuery(query, MainActivator.getDefault().getConnection());
		if(!result)
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada MERGE(UPDATE/INSERT) tabel PERIKSA.");
		return result;
	}
	
	public Periksa getPeriksa(Integer idPeriksa){
		String query = "SELECT * FROM PERIKSA WHERE IDPERIKSA = "+idPeriksa;
		ResultSet result = db.ResultExecutedStatementQuery(query, MainActivator.getDefault().getConnection());
		if(result != null){
			try{
				while(result.next()){
					this.periksaModel.setIdPeriksa(result.getInt("IDPERIKSA"));
					this.periksaModel.setNoPeriksa(result.getString("NOPERIKSA"));
					this.periksaModel.setTglPeriksa(result.getDate("TGLPERIKSA"));
					this.periksaModel.setNpwpd(result.getString("NPWPD_P"));
					this.periksaModel.setJenisPeriksa(result.getString("JENISPERIKSA"));
					this.periksaModel.setKenaikanPersen(result.getInt("KENAIKANPERSEN"));
					this.periksaModel.setKenaikan(result.getDouble("NAIK"));
					this.periksaModel.setMasaPajakDari(result.getDate("MASAPAJAKDARI"));
					this.periksaModel.setMasaPajakSampai(result.getDate("MASAPAJAKSAMPAI"));
					this.periksaModel.setNoSkp(result.getString("NOSKP"));
					this.periksaModel.setTglSkp(result.getDate("TGLSKP"));
					this.periksaModel.setNoNppd(result.getString("NONPPD"));
					this.periksaModel.setTglNppd(result.getDate("TGLNPD"));
					this.periksaModel.setIdSubPajak(result.getInt("IDSUBPAJAK"));
					this.periksaModel.setTipeSkp(result.getString("TIPESKP"));
					this.periksaModel.setTotalPajakPeriksa(result.getDouble("TOTALPAJAKPERIKSA"));
					this.periksaModel.setTotalPajakTerutang(result.getDouble("TOTALPAJAKTERUTANG"));
					this.periksaModel.setTotalPajakBunga(result.getDouble("TOTALPAJAKBUNGA"));
					this.periksaModel.setTotalKenaikan(result.getDouble("TOTALKENAIKAN"));
					this.periksaModel.setTotalDenda(result.getDouble("TOTALDENDA"));
					this.periksaModel.setKeterangan(result.getString("KETERANGAN"));
				}
				return this.periksaModel;
			}
			catch(SQLException e){
				MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", e.getMessage());
				return null;
			}
		}
		else{
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada pembuatan LOAD DATA tabel PERIKSA.");
			return null;
		}
	}
	
	public List<Periksa> getAllPeriksa(){
		String query = "SELECT * FROM PERIKSA";
		ResultSet result = db.ResultExecutedStatementQuery(query, MainActivator.getDefault().getConnection());
		if(result != null){
			try{
				List<Periksa> listPeriksa = new ArrayList<Periksa>();
				while(result.next()){
					Periksa periksaModel = new Periksa();
					periksaModel.setIdPeriksa(result.getInt("IDPERIKSA"));
					periksaModel.setNoPeriksa(result.getString("NOPERIKSA"));
					periksaModel.setTglPeriksa(result.getDate("TGLPERIKSA"));
					periksaModel.setNpwpd(result.getString("NPWPD_P"));
					periksaModel.setJenisPeriksa(result.getString("JENISPERIKSA"));
					periksaModel.setKenaikanPersen(result.getInt("KENAIKANPERSEN"));
					periksaModel.setKenaikan(result.getDouble("NAIK"));
					periksaModel.setMasaPajakDari(result.getDate("MASAPAJAKDARI"));
					periksaModel.setMasaPajakSampai(result.getDate("MASAPAJAKSAMPAI"));
					periksaModel.setNoSkp(result.getString("NOSKP"));
					periksaModel.setTglSkp(result.getDate("TGLSKP"));
					periksaModel.setNoNppd(result.getString("NONPPD"));
					periksaModel.setTglNppd(result.getDate("TGLNPD"));
					periksaModel.setIdSubPajak(result.getInt("IDSUBPAJAK"));
					periksaModel.setTipeSkp(result.getString("TIPESKP"));
					periksaModel.setTotalPajakPeriksa(result.getDouble("TOTALPAJAKPERIKSA"));
					periksaModel.setTotalPajakTerutang(result.getDouble("TOTALPAJAKTERUTANG"));
					periksaModel.setTotalPajakBunga(result.getDouble("TOTALPAJAKBUNGA"));
					periksaModel.setTotalKenaikan(result.getDouble("TOTALKENAIKAN"));
					periksaModel.setTotalDenda(result.getDouble("TOTALDENDA"));
					periksaModel.setKeterangan(result.getString("KETERANGAN"));
					listPeriksa.add(periksaModel);
				}
				return listPeriksa;
			}
			catch(SQLException e){
				MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", e.getMessage());
				return null;
			}
		}
		else{
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada pembuatan LOAD DATA tabel PERIKSA.");
			return null;
		}
	}
	
	public void deletePeriksa(Integer idPeriksa){
		String query = "DELETE FROM PERIKSA WHERE IDPERIKSA = "+idPeriksa;
		boolean result = db.ExecuteStatementQuery(query, MainActivator.getDefault().getConnection());
		if(!result)
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada DELETE tabel PERIKSA dengan IDPERIKSA = "+idPeriksa);
	}
	
	public Integer getLastIdPeriksa(){
		String query = "SELECT TOP 1 IDPERIKSA FROM PERIKSA ORDER BY IDPERIKSA DESC";
		try{
			ResultSet result = db.ResultExecutedStatementQuery(query, MainActivator.getDefault().getConnection());
			if(result.next()){
				return result.getInt("IDPERIKSA");
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
