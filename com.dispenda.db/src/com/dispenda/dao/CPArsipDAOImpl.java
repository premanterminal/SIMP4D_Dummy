package com.dispenda.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.wb.swt.SWTResourceManager;

import com.dispenda.database.DBConnection;
import com.dispenda.database.DBOperation;
import com.dispenda.model.Arsip;
import com.dispenda.model.Sptpd;

public class CPArsipDAOImpl {
	private DBOperation db = new DBOperation();
	
	public List<Arsip> getArsip(String filter, String keyword){
		List<Arsip> listArsip = new ArrayList<Arsip>();
		try{
			String query = "";
			if (filter.equalsIgnoreCase("No SKP")){
				if (keyword.contains("SKPDKB") || keyword.contains("SKPDN")){
					query = "SELECT a.*, wp.Nama_Badan, wp.Alabad_Jalan FROM Arsip a inner join Wajib_Pajak wp on a.NPWPD = wp.NPWPD inner join periksa p on p.NPWPD = a.NPWPD " +
							"WHERE p.No_SKP like  '" + keyword + "%'";
				}
				else if (keyword.contains("SPTPD") || keyword.contains("SKPD")){
					query = "SELECT a.*, wp.Nama_Badan, wp.Alabad_Jalan FROM Arsip a inner join Wajib_Pajak wp on a.NPWPD = wp.NPWPD inner join SPTPD s on s.NPWPD = a.NPWPD " +
							"WHERE s.No_SPTPD like '" + keyword + "%'";
				}
			}
			else if (filter.equalsIgnoreCase("No SSPD")){
				String NPWPD = "";
				String queryWP = "Select NPWPD from SSPD where no_sspd = '" + keyword + "'";
				ResultSet result = db.ResultExecutedStatementQuery(queryWP, DBConnection.INSTANCE.open());
				if(result.next())
					NPWPD =  result.getString("NPWPD");
				query = "SELECT a.*, wp.Nama_Badan, wp.Alabad_Jalan FROM Arsip a inner join Wajib_Pajak wp on a.NPWPD = wp.NPWPD WHERE a.NPWPD = '" + NPWPD + "'";
			}
			else if (filter.equalsIgnoreCase("No Box")){
				String[] filterBox = keyword.split(";");
				query = "Select a.*, wp.Nama_Badan, wp.Alabad_Jalan from Arsip a inner join Wajib_Pajak wp on wp.NPWPD = a.NPWPD where a.No_Box = " + filterBox[0] +
						" and a.Nama_Rak like '%" + filterBox[1].trim() + "%' order by a.Nama_Rak, Cast(a.No_Map as INT);";
			}
			else if (filter.equalsIgnoreCase("No Reg")){
				String[] filterBox = new String[2];// = keyword.split(";");
				if (keyword.contains(";"))
					filterBox = keyword.split(";");
				else
					filterBox[0] = keyword;
				String NPWPD = "";
				String queryWP = "Select ss.NPWPD from No_Registrasi rg inner join SSPD ss on ss.No_SSPD = rg.No_SSPD where rg.No_Registrasi = '" + filterBox[0] + "'";
				if (keyword.contains(";")){
					queryWP += " and rg.Tahun = " + filterBox[1];
				}
				queryWP += " order by ID desc;";
				ResultSet result = db.ResultExecutedStatementQuery(queryWP, DBConnection.INSTANCE.open());
				if(result.next())
					NPWPD =  result.getString("NPWPD");
				query = "SELECT a.*, wp.Nama_Badan, wp.Alabad_Jalan FROM Arsip a inner join Wajib_Pajak wp on a.NPWPD = wp.NPWPD WHERE a.NPWPD = '" + NPWPD + "'";
			}
			else{
				try{
					Integer.parseInt(keyword);
					query = "SELECT a.*, wp.Nama_Badan, wp.Alabad_Jalan FROM Arsip a inner join Wajib_Pajak wp on a.NPWPD = wp.NPWPD WHERE a.NPWPD = '" + keyword + "' " +
							"or CAST(SUBSTRING(a.NPWPD, 2, 7) AS BIGINT) = '" + keyword + "'";
				}catch (Exception e){
					return null;
				}
			}
			ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
			while(result.next()){
				Arsip arsipModel = new Arsip();
				arsipModel.setNPWPD(result.getString("NPWPD"));
				arsipModel.setNamaRak(result.getString("Nama_Rak"));
				arsipModel.setNoBox(result.getInt("No_Box"));
				arsipModel.setNoMap(result.getInt("No_Map"));
				arsipModel.setNamaBadan(result.getString("Nama_Badan"));
				arsipModel.setAlamat(result.getString("Alabad_Jalan"));
				listArsip.add(arsipModel);
			}
			return listArsip;
		}
		catch(SQLException e){
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Error", e.toString());
			e.printStackTrace();
			return null;
		}	
	}
	
	public List<Sptpd> getSspd(String npwpd){
		String query = "SELECT s.NPWPD, ss.No_SSPD AS No_SKP, rg.No_Registrasi, s.No_SKPLama, ss.Tgl_SSPD AS Tanggal_SKP, " +
				"casewhen (Month(s.Masa_Pajak_Dari) = Month(s.Masa_Pajak_Sampai), concat(MonthName(s.Masa_Pajak_Dari), ' ' , year(s.Masa_Pajak_Dari)), " +
				"concat(MonthName(s.Masa_Pajak_Dari), ' ' , year(s.Masa_Pajak_Dari), ' - ' , MonthName(s.Masa_Pajak_Sampai), ' ' , year(s.Masa_Pajak_Sampai)))" +
				"AS MasaPajak, s.Masa_Pajak_Dari, s.Masa_Pajak_Sampai, ss.Pos_End FROM SSPD ss INNER JOIN sptpd s on s.NPWPD = ss.NPWPD and s.No_SPTPD = ss.No_SKP " +
				"LEFT JOIN No_Registrasi rg on rg.No_SSPD = ss.No_SSPD WHERE ss.NPWPD = '" + npwpd  + "' ORDER BY s.Masa_Pajak_Dari DESC;";
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		if(result != null){
			try{
				List<Sptpd> listSptpd = new ArrayList<Sptpd>();
				while(result.next()){
					Sptpd sptpdModel = new Sptpd();
					sptpdModel.setNoSPTPD(result.getString("NO_SKP"));
					sptpdModel.setNoNPPD(result.getString("No_registrasi"));
					sptpdModel.setNoSPTPDLama(result.getString("No_SKPLama") == null ? "" : result.getString("No_SKPLama"));
					sptpdModel.setMasaPajak(result.getString("MasaPajak"));
					sptpdModel.setTanggalSPTPD(result.getDate("Tanggal_SKP"));
					sptpdModel.setMasaPajakDari(result.getDate("MASA_PAJAK_DARI"));
					sptpdModel.setMasaPajakSampai(result.getDate("MASA_PAJAK_SAMPAI"));
					sptpdModel.setPosEnd(result.getInt("Pos_End"));
					listSptpd.add(sptpdModel);
				}
				return listSptpd;
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
	
	public List<Sptpd> getSspd_SKPDKB(String npwpd){
		String query = "SELECT p.NPWPD, ss.No_SSPD AS No_SKP, rg.No_Registrasi, p.No_SKPLama, ss.Tgl_SSPD AS Tanggal_SKP, " +
				"casewhen (Month(p.Masa_Pajak_Dari) = Month(p.Masa_Pajak_Sampai), concat(MonthName(p.Masa_Pajak_Dari), ' ' , year(p.Masa_Pajak_Dari)), " +
				"concat(MonthName(p.Masa_Pajak_Dari), ' ' , year(p.Masa_Pajak_Dari), ' - ' , MonthName(p.Masa_Pajak_Sampai), ' ' , year(p.Masa_Pajak_Sampai)))" +
				"AS MasaPajak, p.Masa_Pajak_Dari, p.Masa_Pajak_Sampai, ss.Pos_End FROM SSPD ss INNER JOIN periksa p on p.NPWPD = ss.NPWPD and p.No_SKP = ss.No_SKP " +
				"LEFT JOIN No_Registrasi rg on rg.No_SSPD = ss.No_SSPD WHERE ss.NPWPD = '" + npwpd  + "' ORDER BY p.Masa_Pajak_Dari DESC;";
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		if(result != null){
			try{
				List<Sptpd> listSptpd = new ArrayList<Sptpd>();
				while(result.next()){
					Sptpd sptpdModel = new Sptpd();
					sptpdModel.setNoSPTPD(result.getString("NO_SKP"));
					sptpdModel.setNoNPPD(result.getString("No_Registrasi"));
					sptpdModel.setNoSPTPDLama(result.getString("No_SKPLama") == null ? "" : result.getString("No_SKPLama"));
					sptpdModel.setMasaPajak(result.getString("MasaPajak"));
					sptpdModel.setTanggalSPTPD(result.getDate("Tanggal_SKP"));
					sptpdModel.setMasaPajakDari(result.getDate("MASA_PAJAK_DARI"));
					sptpdModel.setMasaPajakSampai(result.getDate("MASA_PAJAK_SAMPAI"));
					sptpdModel.setPosEnd(result.getInt("Pos_End"));
					listSptpd.add(sptpdModel);
				}
				return listSptpd;
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
	
	public ResultSet getArsipDetail(String npwpd, String noSkp){
		String query = "";
		if (noSkp.contains("SSPD")){
			query = "Select s.NPWPD, s.No_SKP, s.Bulan_SKP, s.No_SSPD, r.No_Registrasi, s.Tgl_SSPD, s.Tgl_Cetak, s.Jumlah_Bayar, s.Denda, s.Cicilan_Ke, " +
					"s.Nama_Penyetor from SSPD s LEFT JOIN No_Registrasi r on r.No_SSPD = s.No_SSPD where s.npwpd = '" + npwpd + "' and s.no_sspd = '" + noSkp + "'";
		}else if (noSkp.contains("SKPD/") || noSkp.contains("SPTPD")){
			query = "Select NPWPD, No_SPTPD, No_NPPD, Tgl_SPTPD, Masa_Pajak_Dari, Masa_Pajak_Sampai, Dasar_Pengenaan, Tarif_Pajak, Pajak_Terutang, Denda_Tambahan, Lokasi from SPTPD where npwpd = '" + npwpd + "' and no_sptpd = '" + noSkp + "'";
		}else if (noSkp.contains("SKPDKB")){
			query = "Select NPWPD, No_SKP, No_NPPD, Tipe_SKP, Tanggal_Periksa, Tanggal_SKP, Jenis_Periksa, Masa_Pajak_Dari, Masa_Pajak_Sampai, Kenaikan, " +
					"Total_Pajak_Periksa, Total_Pajak_Terutang, Total_Pajak_Bunga, Total_Kenaikan, Total_Denda from PERIKSA where npwpd = '" + npwpd + "' and no_skp = '" + noSkp + "'";
		}
		
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		return result;
	}
	
	public ResultSet getSKPD(String noSKP){
		String query = "SELECT S.NO_SPTPD,S.TGL_SPTPD,S.MASA_PAJAK_DARI,S.MASA_PAJAK_SAMPAI,S.NPWPD,W.NAMA_BADAN FROM SPTPD S " +
				"INNER JOIN WAJIB_PAJAK W ON W.NPWPD=S.NPWPD " +
				"WHERE S.NO_SPTPD = '"+noSKP+"';";
		return db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
	}
	
	public boolean updatePos(String tipeSKP, String tipeColomn, String noSKP, String npwpd, int posStart, int posEnd){
		String query = "UPDATE "+tipeSKP+" S SET S.POS_START="+posStart+",S.POS_END="+posEnd+" " +
				"WHERE S."+tipeColomn+" = '"+noSKP+"' AND S.NPWPD = '"+npwpd+"';";
		return db.ExecuteStatementQuery(query, DBConnection.INSTANCE.open());
	}
	
	public boolean updatePos(String tipeSKP, String tipeColomn, String noSKP, int posStart, int posEnd){
		String query = "UPDATE "+tipeSKP+" S SET S.POS_START="+posStart+",S.POS_END="+posEnd+" " +
				"WHERE S."+tipeColomn+" = '"+noSKP+"';";
		return db.ExecuteStatementQuery(query, DBConnection.INSTANCE.open());
	}
	
	public int getPos(String pos,String tipeSKP, String tipeColomn, String noSKP, String npwpd){
		String query = "SELECT S."+pos+" FROM "+tipeSKP+" S " +
				"INNER JOIN WAJIB_PAJAK W ON W.NPWPD=S.NPWPD " +
				"WHERE S."+tipeColomn+" = '"+noSKP+"' AND S.NPWPD = '"+npwpd+"';";
		ResultSet rs = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		int result = 99;
		try {
			if(rs.next())
				result = rs.getInt(pos);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	public int getPos(String pos,String tipeSKP, String tipeColomn, String noSKP){
		String query = "SELECT S."+pos+" FROM "+tipeSKP+" S " +
				"WHERE S."+tipeColomn+" = '"+noSKP+"';";
		ResultSet rs = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		int result = 99;
		try {
			if(rs.next())
				result = rs.getInt(pos);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public ResultSet getSKPDKB(String noSKP){
		String query = "SELECT S.NO_SKP AS NO_SPTPD, S.TANGGAL_SKP AS TGL_SPTPD,S.MASA_PAJAK_DARI,S.MASA_PAJAK_SAMPAI,S.NPWPD,W.NAMA_BADAN FROM PERIKSA S " +
				"INNER JOIN WAJIB_PAJAK W ON W.NPWPD=S.NPWPD " +
				"WHERE S.NO_SKP = '"+noSKP+"';";
		return db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
	}
	
	public ResultSet getSSPD(String noSKP){
		String query = "SELECT S.NO_SSPD AS NO_SPTPD,S.TGL_CETAK AS TGL_SPTPD,IFNULL(SP.MASA_PAJAK_DARI,P.MASA_PAJAK_DARI) AS MASA_PAJAK_DARI,IFNULL(SP.MASA_PAJAK_SAMPAI,P.MASA_PAJAK_SAMPAI) AS MASA_PAJAK_SAMPAI,S.NPWPD,W.NAMA_BADAN FROM SSPD S " +
				"INNER JOIN WAJIB_PAJAK W ON W.NPWPD=S.NPWPD " +
				"LEFT JOIN SPTPD SP ON SP.IDSPTPD=S.IDSPTPD " +
				"LEFT JOIN PERIKSA P ON P.IDPERIKSA=S.IDPERIKSA " +
				"WHERE S.NO_SSPD = '"+noSKP+"';";
		return db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
	}
	
	public List<TableItem> getData(String pos, Table table){
		List<TableItem> listItems = new ArrayList<TableItem>();
		String query = "SELECT S.NO_SPTPD,S.TGL_SPTPD,S.MASA_PAJAK_DARI,S.MASA_PAJAK_SAMPAI,S.NPWPD,W.NAMA_BADAN FROM SPTPD S " +
				"INNER JOIN WAJIB_PAJAK W ON W.NPWPD=S.NPWPD " +
				"WHERE S."+pos+"=1;";
		ResultSet rs1 = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		try {
			while(rs1.next()){
				TableItem item = new TableItem(table, SWT.NONE);
				item.setText(new String[]{rs1.getString("NO_SPTPD"),rs1.getString("TGL_SPTPD"),rs1.getString("MASA_PAJAK_DARI"),rs1.getString("MASA_PAJAK_SAMPAI"),rs1.getString("NPWPD"),rs1.getString("NAMA_BADAN")});
				listItems.add(item);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		query = "SELECT S.NO_SKP AS NO_SPTPD, S.TANGGAL_SKP AS TGL_SPTPD,S.MASA_PAJAK_DARI,S.MASA_PAJAK_SAMPAI,S.NPWPD,W.NAMA_BADAN FROM PERIKSA S " +
				"INNER JOIN WAJIB_PAJAK W ON W.NPWPD=S.NPWPD " +
				"WHERE S."+pos+"=1;";
		rs1 = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		try {
			while(rs1.next()){
				TableItem item = new TableItem(table, SWT.NONE);
				item.setText(new String[]{rs1.getString("NO_SPTPD"),rs1.getString("TGL_SPTPD"),rs1.getString("MASA_PAJAK_DARI"),rs1.getString("MASA_PAJAK_SAMPAI"),rs1.getString("NPWPD"),rs1.getString("NAMA_BADAN")});
				listItems.add(item);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		query = "SELECT S.NO_SSPD AS NO_SPTPD,S.TGL_CETAK AS TGL_SPTPD,IFNULL(SP.MASA_PAJAK_DARI,P.MASA_PAJAK_DARI) AS MASA_PAJAK_DARI,IFNULL(SP.MASA_PAJAK_SAMPAI,P.MASA_PAJAK_SAMPAI) AS MASA_PAJAK_SAMPAI,S.NPWPD,W.NAMA_BADAN FROM SSPD S " +
				"INNER JOIN WAJIB_PAJAK W ON W.NPWPD=S.NPWPD " +
				"LEFT JOIN SPTPD SP ON SP.IDSPTPD=S.IDSPTPD " +
				"LEFT JOIN PERIKSA P ON P.IDPERIKSA=S.IDPERIKSA " +
				"WHERE S."+pos+"=1;";
		rs1 = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		try {
			while(rs1.next()){
				TableItem item = new TableItem(table, SWT.NONE);
				item.setText(new String[]{rs1.getString("NO_SPTPD"),rs1.getString("TGL_SPTPD"),rs1.getString("MASA_PAJAK_DARI"),rs1.getString("MASA_PAJAK_SAMPAI"),rs1.getString("NPWPD"),rs1.getString("NAMA_BADAN")});
				listItems.add(item);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
			
		return listItems;
	}
	
	public List<TableItem> getDatabyNPWPD(String npwpd, Table table){
		List<TableItem> listItems = new ArrayList<TableItem>();
		String query = "Select * from (SELECT S.NO_SPTPD as NO_SPTPD ,S.TGL_SPTPD as TGL_SPTPD,S.MASA_PAJAK_DARI,S.MASA_PAJAK_SAMPAI,S.NPWPD,W.NAMA_BADAN,S.POS_END FROM SPTPD S " +
				"INNER JOIN WAJIB_PAJAK W ON W.NPWPD=S.NPWPD " +
				"WHERE W.NPWPD='" + npwpd + "' union " +
				"SELECT S.NO_SKP AS NO_SPTPD, S.TANGGAL_SKP AS TGL_SPTPD,S.MASA_PAJAK_DARI,S.MASA_PAJAK_SAMPAI,S.NPWPD,W.NAMA_BADAN,S.POS_END FROM PERIKSA S " +
				"INNER JOIN WAJIB_PAJAK W ON W.NPWPD=S.NPWPD " +
				"WHERE W.NPWPD='" + npwpd + "' union " +
				"SELECT S.NO_SSPD AS NO_SPTPD,S.TGL_CETAK AS TGL_SPTPD,IFNULL(SP.MASA_PAJAK_DARI,P.MASA_PAJAK_DARI) AS MASA_PAJAK_DARI,IFNULL(SP.MASA_PAJAK_SAMPAI,P.MASA_PAJAK_SAMPAI) AS MASA_PAJAK_SAMPAI,S.NPWPD,W.NAMA_BADAN,S.POS_END FROM SSPD S " +
				"INNER JOIN WAJIB_PAJAK W ON W.NPWPD=S.NPWPD " +
				"LEFT JOIN SPTPD SP ON SP.IDSPTPD=S.IDSPTPD " +
				"LEFT JOIN PERIKSA P ON P.IDPERIKSA=S.IDPERIKSA " +
				"WHERE W.NPWPD='" + npwpd + "') order by TGL_SPTPD desc;";
		ResultSet rs1 = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		try {
			while(rs1.next()){
				TableItem item = new TableItem(table, SWT.NONE);
				item.setText(new String[]{rs1.getString("NO_SPTPD"),rs1.getString("TGL_SPTPD"),rs1.getString("MASA_PAJAK_DARI"),rs1.getString("MASA_PAJAK_SAMPAI"),rs1.getString("NPWPD"),rs1.getString("NAMA_BADAN")});
				listItems.add(item);
				if (rs1.getInt("POS_END") == 3)
					item.setBackground(SWTResourceManager.getColor(SWT.COLOR_GREEN));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		/*query = "SELECT S.NO_SKP AS NO_SPTPD, S.TANGGAL_SKP AS TGL_SPTPD,S.MASA_PAJAK_DARI,S.MASA_PAJAK_SAMPAI,S.NPWPD,W.NAMA_BADAN FROM PERIKSA S " +
				"INNER JOIN WAJIB_PAJAK W ON W.NPWPD=S.NPWPD " +
				"WHERE W.NPWPD='" + npwpd + "';";
		rs1 = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		try {
			while(rs1.next()){
				TableItem item = new TableItem(table, SWT.NONE);
				item.setText(new String[]{rs1.getString("NO_SPTPD"),rs1.getString("TGL_SPTPD"),rs1.getString("MASA_PAJAK_DARI"),rs1.getString("MASA_PAJAK_SAMPAI"),rs1.getString("NPWPD"),rs1.getString("NAMA_BADAN")});
				listItems.add(item);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		query = "SELECT S.NO_SSPD AS NO_SPTPD,S.TGL_CETAK AS TGL_SPTPD,IFNULL(SP.MASA_PAJAK_DARI,P.MASA_PAJAK_DARI) AS MASA_PAJAK_DARI,IFNULL(SP.MASA_PAJAK_SAMPAI,P.MASA_PAJAK_SAMPAI) AS MASA_PAJAK_SAMPAI,S.NPWPD,W.NAMA_BADAN FROM SSPD S " +
				"INNER JOIN WAJIB_PAJAK W ON W.NPWPD=S.NPWPD " +
				"LEFT JOIN SPTPD SP ON SP.IDSPTPD=S.IDSPTPD " +
				"LEFT JOIN PERIKSA P ON P.IDPERIKSA=S.IDPERIKSA " +
				"WHERE W.NPWPD='" + npwpd + "';";
		rs1 = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		try {
			while(rs1.next()){
				TableItem item = new TableItem(table, SWT.NONE);
				item.setText(new String[]{rs1.getString("NO_SPTPD"),rs1.getString("TGL_SPTPD"),rs1.getString("MASA_PAJAK_DARI"),rs1.getString("MASA_PAJAK_SAMPAI"),rs1.getString("NPWPD"),rs1.getString("NAMA_BADAN")});
				listItems.add(item);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}*/
			
		return listItems;
	}
	
	public List<TableItem> getDatabyDate(Date tanggal, Table table, int tipe){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<TableItem> listItems = new ArrayList<TableItem>();
		String query = "";
		if (tipe == 0){
			query = "SELECT S.NO_SPTPD as NO_SPTPD ,S.TGL_SPTPD as TGL_SPTPD,S.MASA_PAJAK_DARI,S.MASA_PAJAK_SAMPAI,S.NPWPD,W.NAMA_BADAN,S.POS_END FROM SPTPD S " +
					"INNER JOIN WAJIB_PAJAK W ON W.NPWPD=S.NPWPD WHERE S.TGL_SPTPD=DATE'" + sdf.format(tanggal) + "';";
		}
		else if (tipe == 1){
			query = "SELECT S.NO_SKP AS NO_SPTPD, S.TANGGAL_SKP AS TGL_SPTPD,S.MASA_PAJAK_DARI,S.MASA_PAJAK_SAMPAI,S.NPWPD,W.NAMA_BADAN,S.POS_END FROM PERIKSA S " +
					"INNER JOIN WAJIB_PAJAK W ON W.NPWPD=S.NPWPD WHERE S.TANGGAL_SKP=DATE'" + sdf.format(tanggal) + "';";
		}
		else if (tipe == 2){
			query = "SELECT S.NO_SSPD AS NO_SPTPD,S.TGL_CETAK AS TGL_SPTPD,IFNULL(SP.MASA_PAJAK_DARI,P.MASA_PAJAK_DARI) AS MASA_PAJAK_DARI,IFNULL(SP.MASA_PAJAK_SAMPAI,P.MASA_PAJAK_SAMPAI) AS MASA_PAJAK_SAMPAI,S.NPWPD,W.NAMA_BADAN,S.POS_END FROM SSPD S " +
					"INNER JOIN WAJIB_PAJAK W ON W.NPWPD=S.NPWPD LEFT JOIN SPTPD SP ON SP.IDSPTPD=S.IDSPTPD LEFT JOIN PERIKSA P ON P.IDPERIKSA=S.IDPERIKSA " +
					"WHERE CAST(S.TGL_CETAK AS DATE)=DATE'" + sdf.format(tanggal) + "' order by CAST(SUBSTRING(S.NO_SSPD, 1, POSITION('/' in S.NO_SSPD) - 1) AS INTEGER);";
		}
		else{
			query = "Select * from (SELECT S.NO_SPTPD as NO_SPTPD ,S.TGL_SPTPD as TGL_SPTPD,S.MASA_PAJAK_DARI,S.MASA_PAJAK_SAMPAI,S.NPWPD,W.NAMA_BADAN,S.POS_END FROM SPTPD S " +
					"INNER JOIN WAJIB_PAJAK W ON W.NPWPD=S.NPWPD WHERE S.TGL_SPTPD=DATE'" + sdf.format(tanggal) + "' union " +
					"SELECT S.NO_SKP AS NO_SPTPD, S.TANGGAL_SKP AS TGL_SPTPD,S.MASA_PAJAK_DARI,S.MASA_PAJAK_SAMPAI,S.NPWPD,W.NAMA_BADAN,S.POS_END FROM PERIKSA S " +
					"INNER JOIN WAJIB_PAJAK W ON W.NPWPD=S.NPWPD WHERE S.TANGGAL_SKP=DATE'" + sdf.format(tanggal) + "' union " +
					"SELECT S.NO_SSPD AS NO_SPTPD,S.TGL_CETAK AS TGL_SPTPD,IFNULL(SP.MASA_PAJAK_DARI,P.MASA_PAJAK_DARI) AS MASA_PAJAK_DARI,IFNULL(SP.MASA_PAJAK_SAMPAI,P.MASA_PAJAK_SAMPAI) AS MASA_PAJAK_SAMPAI,S.NPWPD,W.NAMA_BADAN,S.POS_END FROM SSPD S " +
					"INNER JOIN WAJIB_PAJAK W ON W.NPWPD=S.NPWPD LEFT JOIN SPTPD SP ON SP.IDSPTPD=S.IDSPTPD LEFT JOIN PERIKSA P ON P.IDPERIKSA=S.IDPERIKSA " +
					"WHERE CAST(S.TGL_CETAK AS DATE)=DATE'" + sdf.format(tanggal) + "') order by TGL_SPTPD desc;";
		}
		
		ResultSet rs1 = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		try {
			while(rs1.next()){
				TableItem item = new TableItem(table, SWT.NONE);
				item.setText(new String[]{rs1.getString("NO_SPTPD"),rs1.getString("TGL_SPTPD"),rs1.getString("MASA_PAJAK_DARI"),rs1.getString("MASA_PAJAK_SAMPAI"),rs1.getString("NPWPD"),rs1.getString("NAMA_BADAN")});
				listItems.add(item);
				if (rs1.getInt("POS_END") == 3)
					item.setBackground(SWTResourceManager.getColor(SWT.COLOR_GREEN));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return listItems;
	}
	
	public int getNoBox(String namaPajak){
		String query = "select max(no_box) as No_Box from ARSIP where NAMA_RAK = '" + namaPajak + "';";
		try {
			ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
			if (result.next()){
				return result.getInt("NO_BOX");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	
	public int getNoMap(String namaPajak, int noBox){
		String query = "select max(no_map) as No_Map from ARSIP where NAMA_RAK = '" + namaPajak + "' and No_Box = " + noBox + ";";
		try {
			ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
			if (result.next()){
				return result.getInt("NO_MAP");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	
	public boolean saveArsip(Arsip arsipModel){
		String query = "Insert into ARSIP (NPWPD, NAMA_RAK, NO_BOX, NO_MAP) values('" + arsipModel.getNPWPD() + "', '" + arsipModel.getNamaRak() + "', " +
				arsipModel.getNoBox() + ", " + arsipModel.getNoMap() + ")";
		boolean result = db.ExecuteStatementQuery(query, DBConnection.INSTANCE.open());
		if(!result)
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada MERGE(UPDATE/INSERT) tabel WAJIB_PAJAK.");
		return result;
	}
}
