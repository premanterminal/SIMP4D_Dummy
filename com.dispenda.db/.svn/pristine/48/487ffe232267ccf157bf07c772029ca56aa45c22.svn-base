package com.dispenda.dao;

import java.math.RoundingMode;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;

import com.dispenda.model.MohonDetail;
import com.dispenda.controller.ControllerFactory;
import com.dispenda.controller.UIController;
import com.dispenda.database.DBConnection;
import com.dispenda.database.DBOperation;
import com.ibm.icu.text.NumberFormat;

public class CPMohonDetailDAOImpl{
	private Locale ind = new Locale("id", "ID");
    private NumberFormat indFormat = NumberFormat.getCurrencyInstance(ind);
	private DBOperation db = new DBOperation();
	private Timestamp dateNow;
	
	public boolean saveMohonDetail(List<MohonDetail> listMohonDetail, Integer idMohon){
		//if (listMohonDetail.get(0).getIdMohon() != null)
			deleteMohonDetail(idMohon);
		String subQuery = "";
		for(MohonDetail md : listMohonDetail){
			subQuery += "(" +
					""+md.getIdMohonDetail()+"," +
					""+idMohon+"," +
					""+md.getNoAngsuran()+"," +
					"'"+md.getTglAngsuran()+"'," +
					""+md.getAngsuranPokok()+"," +
					""+md.getBiayaAdministrasi()+"," +
					""+md.getDendaSkpdkb()+","+
					""+md.getSts()+","+
					""+md.getStsValid()+"),";
		}
		
		String query = "MERGE INTO MOHON_DETAIL USING (VALUES" + subQuery.substring(0, subQuery.length() - 1) + 
			") AS vals(" +
				"id," +
				"idMohon," +
				"no_angs," +
				"tgl_angs," +
				"angs_pokok," +
				"biaya_adm," +
				"denda_skpdkb," +
				"sts," +
				"stsvalid)" +
			" ON MOHON_DETAIL.IDMOHON_DETAIL = vals.id" +
			" WHEN MATCHED THEN UPDATE SET " +
				"MOHON_DETAIL.IDMOHON = vals.idMohon," +
				"MOHON_DETAIL.NO_ANGSURAN = vals.no_angs," +
				"MOHON_DETAIL.TANGGAL_ANGSURAN = vals.tgl_angs," +
				"MOHON_DETAIL.ANGSURAN_POKOK = vals.angs_pokok," +
				"MOHON_DETAIL.BIAYA_ADMINISTRASI = vals.biaya_adm," +
				"MOHON_DETAIL.DENDA_SKPDKB = vals.denda_skpdkb" +
//				"MOHON_DETAIL.STS = vals.sts," +
//				"MOHON_DETAIL.STSVALID = vals.stsvalid" +
			" WHEN NOT MATCHED THEN INSERT VALUES " +
				"vals.id," +
				"vals.idMohon," +
				"vals.no_angs," +
				"vals.tgl_angs," +
				"vals.angs_pokok," +
				"vals.biaya_adm," +
				"vals.denda_skpdkb," +
				"vals.sts," +
				"vals.stsvalid";
		boolean result = db.ExecuteStatementQuery(query, DBConnection.INSTANCE.open());
		if(!result)
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada MERGE(UPDATE/INSERT) tabel MOHON_DETAIL.");
		return result;
								
	}
	
	@SuppressWarnings("deprecation")
	public List<MohonDetail> getMohonDetail(Integer idMohonDetail){
		String query = "SELECT * FROM MOHON_DETAIL WHERE IDMOHON_DETAIL = "+idMohonDetail;
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		if(result != null){
			try{
				List<MohonDetail> listMohonDetail = new ArrayList<MohonDetail>();
				while(result.next()){
					MohonDetail mohonDetailModel = new MohonDetail();
					mohonDetailModel.setIdMohonDetail(result.getInt("IDMOHON_DETAIL"));
					mohonDetailModel.setIdMohon(result.getInt("IDMOHON"));
					mohonDetailModel.setNoAngsuran(result.getInt("NO_ANGSURAN"));
					mohonDetailModel.setTglAngsuran(result.getDate("TANGGAL_ANGSURAN"));
					mohonDetailModel.setAngsuranPokok(result.getDouble("ANGSURAN_POKOK"));
					mohonDetailModel.setBiayaAdministrasi(result.getDouble("BIAYA_ADMINISTRASI"));
					mohonDetailModel.setDendaSkpdkb(result.getDouble("DENDA_SKPDKB"));
					if (result.getString("STS")!=null){
						mohonDetailModel.setSts(result.getString("STS"));
						mohonDetailModel.setSts(result.getString("STSVALID"));
					}else{
						mohonDetailModel.setSts("");
						mohonDetailModel.setStsValid(new Date(dateNow.getYear(), dateNow.getMonth(), dateNow.getDate()));
					}
						
					listMohonDetail.add(mohonDetailModel);
				}
				return listMohonDetail;
			}
			catch(SQLException e){
				MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", e.getMessage());
				return null;
			}
		}
		else{
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada pembuatan LOAD DATA tabel MOHON_DETAIL.");
			return null;
		}
	}
	
	public List<MohonDetail> getAllMohonDetail(){
		String query = "SELECT * FROM MOHON_DETAIL";
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		if(result != null){
			try{
				List<MohonDetail> listMohonDetail = new ArrayList<MohonDetail>();
				while(result.next()){
					MohonDetail mohonDetailModel = new MohonDetail();
					mohonDetailModel.setIdMohonDetail(result.getInt("IDMOHON_DETAIL"));
					mohonDetailModel.setIdMohon(result.getInt("IDMOHON"));
					mohonDetailModel.setNoAngsuran(result.getInt("NO_ANGSURAN"));
					mohonDetailModel.setTglAngsuran(result.getDate("TANGGAL_ANGSURAN"));
					mohonDetailModel.setAngsuranPokok(result.getDouble("ANGSURAN_POKOK"));
					mohonDetailModel.setBiayaAdministrasi(result.getDouble("BIAYA_ADMINISTRASI"));
					mohonDetailModel.setDendaSkpdkb(result.getDouble("DENDA_SKPDKB"));
					listMohonDetail.add(mohonDetailModel);
				}
				return listMohonDetail;
			}
			catch(SQLException e){
				MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", e.getMessage());
				return null;
			}
		}
		else{
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada pembuatan LOAD DATA tabel MOHON_DETAIL.");
			return null;
		}
	}
	
	public void deleteMohonDetail(Integer idMohon){
		String query = "DELETE FROM MOHON_DETAIL WHERE IDMOHON = "+idMohon;
		boolean result = db.ExecuteStatementQuery(query, DBConnection.INSTANCE.open());
		if(!result)
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada DELETE tabel MOHON_DETAIL dengan IDMOHON = "+idMohon);
	}
	
	public Integer getLastMohonDetail(){
		String query = "SELECT TOP 1 IDMOHON_DETAIL FROM MOHON_DETAIL ORDER BY IDMOHON_DETAIL DESC";
		try{
			ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
			if(result.next()){
				return result.getInt("IDMOHON_DETAIL");
			}
			else
				return 0;
		}
		catch(SQLException e){
			e.printStackTrace();
			return 0;
		}
	}
	
	public List<MohonDetail> getDaftarAngsuran(int idMohon)
	{
		String query = "SELECT IDMOHON_DETAIL, IDMOHON, No_Angsuran, Tanggal_Angsuran, Angsuran_Pokok, Biaya_Administrasi, Denda_SKPDKB, STS, STSVALID FROM mohon_detail " + 
					"WHERE IdMohon = " + idMohon + " ORDER BY No_Angsuran ASC";
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		if(result != null){
			try{
				List<MohonDetail> listMohonDetail = new ArrayList<MohonDetail>();
				while(result.next()){
					MohonDetail mohonDetailModel = new MohonDetail();
					mohonDetailModel.setIdMohonDetail(result.getInt("IDMOHON_DETAIL"));
					mohonDetailModel.setIdMohon(result.getInt("IDMOHON"));
					mohonDetailModel.setNoAngsuran(result.getInt("NO_ANGSURAN"));
					mohonDetailModel.setTglAngsuran(result.getDate("TANGGAL_ANGSURAN"));
					mohonDetailModel.setAngsuranPokok(result.getDouble("ANGSURAN_POKOK"));
					mohonDetailModel.setBiayaAdministrasi(result.getDouble("BIAYA_ADMINISTRASI"));
					mohonDetailModel.setDendaSkpdkb(result.getDouble("DENDA_SKPDKB"));
					mohonDetailModel.setSts(result.getString("STS"));
					mohonDetailModel.setStsValid(result.getDate("STSVALID"));
					listMohonDetail.add(mohonDetailModel);
				}
				return listMohonDetail;
			}
			catch(SQLException e){
				MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", e.getMessage());
				return null;
			}
		}
		else{
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada pembuatan LOAD DATA tabel MOHON_DETAIL.");
			return null;
		}
	}
	
	public HashMap<String, String[]> getDataSTS(String npwp, String noSkp){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdfSKP = new SimpleDateFormat("MMyy");
		String[] value = new String[11];
		Integer count = 0;
		HashMap<String, String[]> hm = new HashMap<String, String[]>();
		String query = "Select wp.NPWPD, wp.Nama_Badan, wp.Nama_Pemilik, wp.Alabad_Jalan, md.Angsuran_Pokok, md.Biaya_Administrasi, md.Denda_SKPDKB, m.No_SKP, p.Tanggal_SKP, " +
				"bu.Kode_Bid_Usaha, p.Masa_Pajak_Dari, p.Masa_Pajak_Sampai, md.STS from Mohon m Inner Join Mohon_Detail md on md.IdMohon = m.IdMohon " +
				"Inner Join Wajib_Pajak wp on wp.Npwpd = m.Npwpd Inner Join Periksa p on p.npwpd = m.npwpd and p.No_Skp = m.No_Skp INNER join Bidang_Usaha bu on bu.IdSub_Pajak = wp.IDSub_Pajak " +
				"where m.Npwpd = '" + npwp +"' and m.No_Skp = '" + noSkp +"'";
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		if(result != null){
			try{
				while(result.next()){
					value[0] = result.getString("NPWPD");
					value[1] = result.getString("Nama_Badan") + "/" + result.getString("Nama_Pemilik");
					value[2] = result.getString("Alabad_Jalan");
					value[3] = indFormat.format(roundUP(result.getDouble("Angsuran_Pokok"))).substring(2).replace(".", "").replace(",", ".");
					Double denda = roundUP(result.getDouble("Biaya_Administrasi")) + roundUP(result.getDouble("Denda_SKPDKB"));
					value[4] = indFormat.format(denda).substring(2).replace(".", "").replace(",", ".");
					value[5] = result.getString("No_SKP");
					value[6] = sdf.format(result.getDate("Tanggal_SKP"));
					value[7] = result.getString("Kode_Bid_Usaha");
					value[8] = sdfSKP.format(result.getDate("Masa_Pajak_Dari")) + "-" + sdfSKP.format(result.getDate("Masa_Pajak_Sampai"));
					value[9] = sdf.format(getYesterday());
					if (result.getString("STS") == null || result.getString("STS").equalsIgnoreCase(""))
						value[10] = "";
					else
						value[10] = result.getString("STS");
					
					hm.put(count.toString(), value);
					count++;
				}
				return hm;
			}
			catch(SQLException e){
				MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", e.getMessage());
				return null;
			}
		}
		else{
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah saat load data STS.");
			return null;
		}
	}
	
	@SuppressWarnings("deprecation")
	private Date getYesterday(){
		dateNow = (java.sql.Timestamp)ControllerFactory.getMainController().getCpWajibPajakDAOImpl().getDateNow();
		Date retValue;
		Calendar now = Calendar.getInstance();
		now.set(dateNow.getYear(), dateNow.getMonth(), dateNow.getDate());
		now.add(Calendar.DATE, -1);
		retValue = new Date(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DATE));
		
		return retValue;
	}
	
	private static double roundUP(double value) {
	    //if (places < 0) throw new IllegalArgumentException();

	    DecimalFormat df = new DecimalFormat("#.##");
		df.setMaximumFractionDigits(0);
		df.setRoundingMode(RoundingMode.UP);
	    try {
			return df.parse(df.format(value)).doubleValue();
		} catch (ParseException e) {
			e.printStackTrace();
			return 0.0;
		}
	}
}