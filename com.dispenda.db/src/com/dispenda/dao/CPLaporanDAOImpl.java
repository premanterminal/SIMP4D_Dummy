package com.dispenda.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;

import com.dispenda.controller.ControllerFactory;
import com.dispenda.controller.UIController;
import com.dispenda.database.DBConnection;
import com.dispenda.database.DBOperation;
import com.dispenda.model.Tunggakan;

public class CPLaporanDAOImpl {
	private DBOperation db = new DBOperation();
	private Timestamp dateNow;
	
	
	public ResultSet GetListSPTPDbyDay(Date masaPajakDari, Date masaPajakSampai, String kode){
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String masaDari = formatter.format(masaPajakDari);
		String masaSampai = formatter.format(masaPajakSampai);
		String query = "select concat(TO_CHAR(sp.Tgl_SPTPD,'MMDD'),'/', replace(bu.Kode_Bid_Usaha,'.',''),'/', RTRIM(casewhen(sp.ASSESMENT = 'Self', 'SPTPD/', 'SKPD/')) ,substring(cast(sp.Tgl_SPTPD as varchar(10)),1,4)) as NoKetetapan," +
				" to_char(sp.Tgl_SPTPD,'DD/MM/YYYY') as TglKetetapan, bu.Kode_Bid_Usaha as NoRekening, sum(sp.Pajak_Terutang) as PokokPajak, cast(0 as double) as denda, concat(CAST(COUNT(*) as varchar(20)), RTRIM(casewhen(sp.ASSESMENT = 'Self', ' SPTPD', ' SKPD')), ' ', bu.NAMA_BID_USAHA) as keterangan " +
				",sp.Tgl_SPTPD " +
				"from sptpd sp INNER JOIN bidang_usaha bu on bu.IdSub_Pajak = sp.IdSub_Pajak WHERE Tgl_SPTPD between Date'" + masaDari + "' and Date'" + masaSampai + "'";
		if (!kode.equalsIgnoreCase(""))
			query += " AND bu.IDPAJAK = '" + kode + "'";
		query += " GROUP BY bu.Kode_Bid_Usaha, TglKetetapan, bu.Nama_Bid_Usaha,sp.Tgl_SPTPD, sp.Assesment ORDER BY sp.Tgl_SPTPD, bu.Kode_Bid_Usaha;";
		
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		return result;
	}
	
	public ResultSet GetListSPTPDbySKP(Date masaPajakDari, Date masaPajakSampai, String kode, String subKode){
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String masaDari = formatter.format(masaPajakDari);
		String masaSampai = formatter.format(masaPajakSampai);
		String query = "select sp.NPWPD, wp.Nama_Badan, sp.No_SPTPD as NoKetetapan, to_char(sp.Tgl_SPTPD,'DD/MM/YYYY') as TglKetetapan, bu.Kode_Bid_Usaha as NoRekening, sp.Pajak_Terutang as PokokPajak, " +
				"cast(0 as double) as denda, concat(RTRIM(casewhen(sp.ASSESMENT = 'Self', ' SPTPD', ' SKPD')), ' ', bu.NAMA_BID_USAHA) as keterangan, sp.Tgl_SPTPD " +
				"from sptpd sp INNER JOIN Wajib_Pajak wp on wp.NPWPD = sp.NPWPD INNER JOIN bidang_usaha bu on bu.IdSub_Pajak = sp.IdSub_Pajak WHERE Tgl_SPTPD between Date'" + masaDari + "' and Date'" + masaSampai + "'";
		if (!kode.equalsIgnoreCase("") && subKode.equalsIgnoreCase(""))
			query += " AND bu.IDPAJAK = '" + kode + "'";
		else if (!kode.equalsIgnoreCase("") && !subKode.equalsIgnoreCase(""))
			query += " AND bu.IDPAJAK = '" + kode + "' AND bu.IDSUB_PAJAK = '" + subKode + "'";
		query += " ORDER BY sp.Tgl_SPTPD, sp.No_SPTPD, bu.Kode_Bid_Usaha, CAST(SUBSTRING(sp.NO_SPTPD, 1, POSITION('/' in sp.NO_SPTPD) - 1) AS INTEGER);";
		System.out.println(query);
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		return result;
	}
	
	public ResultSet GetListSKPDKBbyDay(Date masaPajakDari, Date masaPajakSampai, String kode){
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String masaDari = formatter.format(masaPajakDari);
		String masaSampai = formatter.format(masaPajakSampai);
		String query = "select concat(TO_CHAR(p.Tanggal_SKP,'MMDD'),'/', replace(bu.Kode_Bid_Usaha,'.',''),'/', RTRIM(casewhen(p.TIPE_SKP = 'SKPDKB', 'SKPDKB/', 'SKPDKBT/')), substring(cast(p.Tanggal_SKP as varchar(10)),1,4)) as NoKetetapan, " +
				"to_char(p.Tanggal_SKP,'DD/MM/YYYY') as TglKetetapan, bu.Kode_Bid_Usaha as NoRekening, sum(p.Total_Pajak_Periksa - p.Total_Pajak_Terutang  + p.Total_Kenaikan) as PokokPajak, " +
				"sum(p.Total_Pajak_Bunga + p.Total_Denda) as Denda, " +
				"concat(CAST(COUNT(*) as varchar(20)), RTRIM(casewhen(p.TIPE_SKP = 'SKPDKB', ' SKPDKB ', ' SKPDKBT ')), ' ', bu.NAMA_BID_USAHA) as Keterangan, p.Tanggal_SKP from periksa p  " +
				"INNER JOIN bidang_usaha bu on bu.IdSub_Pajak = p.IdSub_Pajak WHERE (p.Tanggal_SKP between Date'" + masaDari + "' and Date'" + masaSampai + "') AND p.Tipe_SKP <> 'SKPDN'"; 
		if (!kode.equalsIgnoreCase(""))
			query += " AND bu.IDPAJAK = '" + kode + "'";
		query += " GROUP BY Kode_Bid_Usaha, Nama_Bid_Usaha, Tanggal_SKP, TglKetetapan, Tipe_SKP ORDER BY p.Tanggal_SKP, bu.Kode_Bid_Usaha";
		
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		return result;
	}
	
	public ResultSet GetListSKPDKBbySKP(Date masaPajakDari, Date masaPajakSampai, String kode, String subKode){
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String masaDari = formatter.format(masaPajakDari);
		String masaSampai = formatter.format(masaPajakSampai);
		String query = "select p.NPWPD, wp.Nama_Badan, p.No_SKP as NoKetetapan, to_char(p.Tanggal_SKP,'DD/MM/YYYY') as TglKetetapan, bu.Kode_Bid_Usaha as NoRekening, " +
				"ceiling(p.Total_Pajak_Periksa - p.Total_Pajak_Terutang  + p.Total_Kenaikan) as PokokPajak, ceiling(p.Total_Pajak_Bunga + p.Total_Denda) as denda, " +
				"concat(RTRIM(casewhen(p.Tipe_SKP = 'SKPDKB', ' SKPDKB', ' SKPDKBT')), ' ', bu.NAMA_BID_USAHA) as keterangan, p.Tanggal_SKP " +
				"from Periksa p INNER JOIN Wajib_Pajak wp on wp.NPWPD = p.NPWPD INNER JOIN bidang_usaha bu on bu.IdSub_Pajak = p.IdSub_Pajak WHERE (p.Tanggal_SKP between Date'" + masaDari + "' and Date'" + masaSampai + "') " +
				"AND p.Tipe_SKP <> 'SKPDN' AND p.No_SKP <> ''";
		if (!kode.equalsIgnoreCase("") && subKode.equalsIgnoreCase(""))
			query += " AND bu.IDPAJAK = '" + kode + "'";
		else if (!kode.equalsIgnoreCase("") && !subKode.equalsIgnoreCase(""))
			query += " AND bu.IDPAJAK = '" + kode + "' AND bu.IDSUB_PAJAK = '" + subKode + "'";
		query += " ORDER BY p.Tanggal_SKP, p.No_SKP, bu.Kode_Bid_Usaha, CAST(SUBSTRING(p.NO_SKP, 1, POSITION('/' in p.NO_SKP) - 1) AS INTEGER);";
		
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		return result;
	}
	
	public ResultSet getDetailSPTPD(String tglKetetapan, String kode, String assesment){
		String[] splitter = tglKetetapan.split("/");
		String query = "select s.NO_SPTPD,s.TGL_SPTPD,s.MASA_PAJAK_DARI,s.MASA_PAJAK_SAMPAI,s.NPWPD,bu.KODE_BID_USAHA,s.PAJAK_TERUTANG,s.DENDA_TAMBAHAN,bu.NAMA_BID_USAHA from sptpd s "+ 
						"inner join BIDANG_USAHA bu on bu.IDSUB_PAJAK = s.IDSUB_PAJAK "+
						"where s.TGL_SPTPD = '"+splitter[2]+"-"+splitter[1]+"-"+splitter[0]+"' and bu.KODE_BID_USAHA = '"+kode+"' and s.ASSESMENT = '"+assesment+"'";
		
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		return result;
	}
	
	public ResultSet getRealisasiPenerimaanSPTPD(String kecamatan, String kodePajak, java.util.Date masaPajakDari, java.util.Date masaPajakSampai){
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String masaDari = formatter.format(masaPajakDari);
		String masaSampai = formatter.format(masaPajakSampai);
		
		String query = "SELECT wp.Nama_Badan, Kec.Name_Kecamatan, wp.Alabad_Jalan, sp.NPWPD, sp.Tgl_SPTPD, sp.No_SPTPD, sp.Pajak_Terutang as SPTPD, ss.Denda, " +
				"ss.jumlah_Bayar as REALISASI, ss.Tgl_SSPD as TanggalBayar, To_Char(sp.Masa_Pajak_Dari, 'MM') as BulanMasaPajak FROM sptpd sp " +
				"LEFT JOIN sspd ss on sp.IDSPTPD = ss.IDSPTPD " +
				"left join wajib_pajak wp on wp.NPWPD = sp.NPWPD inner join kecamatan kec on kec.Kode_Kecamatan = wp.Alabad_Kecamatan " +
				"WHERE wp.Alabad_Kecamatan like '" + kecamatan + "' and SUBSTR(sp.NPWPD, 1, 1) = '" + kodePajak + "' AND sp.Masa_Pajak_Dari >= '" + masaDari + "' AND sp.Masa_Pajak_Sampai <= '" + masaSampai + "' " +
				"ORDER BY sp.IdSub_Pajak, wp.Alabad_Kecamatan, sp.NPWPD, sp.Masa_Pajak_Dari asc;";
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.openBawah());
		return result;
	}
	
	@SuppressWarnings("deprecation")
	public ResultSet getTunggakanSPTPD(String idPajak, int subPajak, Date masaPajakDari, Date masaPajakSampai, boolean tutup, String[] kecamatan){
		Locale.setDefault(new Locale("id","ID"));
		dateNow = (java.sql.Timestamp)ControllerFactory.getMainController().getCpWajibPajakDAOImpl().getDateNow();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String masaDari = formatter.format(masaPajakDari);
		String masaSampai = formatter.format(masaPajakSampai);
		Calendar calBegin = Calendar.getInstance();
		calBegin.set(Calendar.DAY_OF_MONTH, dateNow.getDate());
		calBegin.set(Calendar.MONTH,dateNow.getMonth());
		calBegin.set(Calendar.YEAR, dateNow.getYear());
		calBegin.add(Calendar.MONTH, -2);
		Date masaPajak = new Date(calBegin.get(Calendar.YEAR), calBegin.get(Calendar.MONTH), calBegin.getActualMaximum(Calendar.DAY_OF_MONTH));
		String masaPajakStr = formatter.format(masaPajak);
		
//		String query = "Select * from getSPTPDREALISASIViews where Substring(NPWPD, 1, 1) = '" + idPajak + "' and NO_SSPD is null and " +
//		"(TGL_SPTPD between Date'" + masaDari + "' and date'" + masaSampai + "')";
		String query = "Select wp.NPWPD, wp.Nama_Badan, wp.Nama_Pemilik, wp.Alabad_Jalan, sp.NO_SPTPD, sp.TGL_SPTPD, sp.ASSESMENT as Jenis_Assesment, sp.MASA_PAJAK_DARI, sp.MASA_PAJAK_SAMPAI, " +
				"'' as jatuhTempo, sp.DASAR_PENGENAAN, sp.TARIF_PAJAK, sp.PAJAK_TERUTANG, sp.DENDA_TAMBAHAN, bu.Nama_Bid_Usaha, kc.Name_Kecamatan, wp.Status " +
				"from SPTPD sp left join SSPD ss on ss.IDSPTPD = sp.IDSPTPD INNER JOIN WAJIB_PAJAK WP ON WP.NPWPD=sp.NPWPD " +
				"INNER JOIN BIDANG_USAHA BU ON BU.IDSUB_PAJAK=sp.IDSUB_PAJAK LEFT JOIN Periksa_Detail pd on sp.NPWPD = pd.NPWPD and pd.Bulan_periksa = To_Char(sp.Masa_Pajak_Dari, 'MM-YYYY') " +
				"left join kecamatan kc on wp.ALABAD_KECAMATAN = kc.Kode_Kecamatan left join UPT up on up.KODE_KECAMATAN = kc.KODE_KECAMATAN " +
				"where Substring(sp.NPWPD, 1, 1) = '"+ idPajak + "' and ss.NO_SSPD is null and pd.IDPERIKSA is null and " +
				"(TGL_SPTPD between Date'" + masaDari + "' and date'" + masaSampai + "') AND sp.Pajak_Terutang > 1 AND sp.Masa_Pajak_Dari < '" + masaPajakStr + "'";
		if (subPajak != 0)
			query += " and idSub_Pajak = " + subPajak;
		if (!tutup)
			query += " and wp.status <> 'Tutup'";
		if (kecamatan.length > 0){
			query += " and (";
			for (int i=0;i<kecamatan.length;i++){
				if (i>0)
					query += " or";
				query += " kc.Name_Kecamatan = '" + kecamatan[i] + "'";
			}
			query += ")";
		}
		query += " Order by UPT, Kode_Kecamatan, NPWPD, Tgl_SPTPD, IDSub_Pajak, No_SPTPD;";
	
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		return result;
	}
	
	public ResultSet getTunggakanSPTPDReklameBPK(){
		String query = "select * from BPK_REKLAME;";
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		return result;
	}
	
	@SuppressWarnings("deprecation")
	public ResultSet getTagihanSPTPD(String idPajak, int subPajak, Date masaPajakDari, Date masaPajakSampai, boolean tutup, String[] kecamatan){
		Locale.setDefault(new Locale("id","ID"));
		dateNow = (java.sql.Timestamp)ControllerFactory.getMainController().getCpWajibPajakDAOImpl().getDateNow();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String masaDari = formatter.format(masaPajakDari);
		String masaSampai = formatter.format(masaPajakSampai);
		Calendar calBegin = Calendar.getInstance();
		calBegin.set(Calendar.DAY_OF_MONTH, dateNow.getDate());
		calBegin.set(Calendar.MONTH,dateNow.getMonth());
		calBegin.set(Calendar.YEAR, dateNow.getYear());
		calBegin.add(Calendar.MONTH, -2);
		Date masaPajak = new Date(calBegin.get(Calendar.YEAR), calBegin.get(Calendar.MONTH), calBegin.getActualMaximum(Calendar.DAY_OF_MONTH));
		String masaPajakStr = formatter.format(masaPajak);

		String query = "Select wp.NPWPD, max(wp.Nama_Badan) as Nama_Badan, max(wp.Nama_Pemilik) as Nama_Pemilik, max(wp.Alabad_Jalan) as Alabad_Jalan, max(sp.ASSESMENT) as Jenis_Assesment, " +
				"MIN(sp.MASA_PAJAK_DARI) as Masa_Pajak_Dari, Max(sp.MASA_PAJAK_SAMPAI) as Masa_Pajak_Sampai, '' as jatuhTempo, SUM(sp.DASAR_PENGENAAN) as Dasar_Pengenaan, MAX(sp.TARIF_PAJAK) as Tarif_Pajak, " +
				"SUM(sp.PAJAK_TERUTANG) as Pajak_Terutang, SUM(sp.DENDA_TAMBAHAN) as Denda_Tambahan, MAX(bu.Nama_Bid_Usaha) as Nama_Bid_Usaha, MAX(wp.Status) as Status, MAX(kc.Kode_Kecamatan) as kec, MAX(up.UPT) as UPT " +
				"from SPTPD sp left join SSPD ss on ss.IDSPTPD = sp.IDSPTPD INNER JOIN WAJIB_PAJAK WP ON WP.NPWPD=sp.NPWPD " +
				"INNER JOIN BIDANG_USAHA BU ON BU.IDSUB_PAJAK=sp.IDSUB_PAJAK LEFT JOIN Periksa_Detail pd on sp.NPWPD = pd.NPWPD and pd.Bulan_periksa = To_Char(sp.Masa_Pajak_Dari, 'MM-YYYY') " +
				"left join kecamatan kc on wp.ALABAD_KECAMATAN = kc.Kode_Kecamatan left join UPT up on up.KODE_KECAMATAN = kc.KODE_KECAMATAN " +
				"where Substring(sp.NPWPD, 1, 1) = '"+ idPajak + "' and ss.NO_SSPD is null and pd.IDPERIKSA is null and " +
				"(TGL_SPTPD between Date'" + masaDari + "' and date'" + masaSampai + "') AND sp.Pajak_Terutang > 1 AND sp.Masa_Pajak_Dari < '" + masaPajakStr + "'";
		if (subPajak != 0)
			query += " and idSub_Pajak = " + subPajak;
		if (!tutup)
			query += " and wp.status <> 'Tutup'";
		if (kecamatan.length > 0){
			query += " and (";
			for (int i=0;i<kecamatan.length;i++){
				if (i>0)
					query += " or";
				query += " kc.Name_Kecamatan = '" + kecamatan[i] + "'";
			}
			query += ")";
		}
		query += " Group by wp.NPWPD order by UPT, kec, NPWPD;";
	
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		return result;
	}
	
	@SuppressWarnings("deprecation")
	public List<Tunggakan> getTunggakanSKPDKB(String idPajak, int subPajak, Date masaPajakDari, Date masaPajakSampai, boolean tutup, String[] kecamatan){
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String masaDari = formatter.format(masaPajakDari);
		String masaSampai = formatter.format(masaPajakSampai);
		Calendar cal = Calendar.getInstance();
		java.sql.Date tglTempo;
		
		String query = "SELECT P.IDPERIKSA,MIN(WP.NPWPD) AS NPWPD,MIN(WP.NAMA_BADAN) AS NAMA_BADAN,MIN(WP.NAMA_PEMILIK) AS NAMA_PEMILIK,MIN(WP.ALABAD_JALAN) AS ALAMAT," +
				"P.NO_SKP,P.TANGGAL_SKP,P.MASA_PAJAK_DARI,P.MASA_PAJAK_SAMPAI,now() as jatuhTempo,MIN(p.Total_Pajak_Periksa - p.Total_Pajak_Terutang) AS POKOK, MIN(p.Total_Kenaikan) as Kenaikan, " +
				"MIN(P.TOTAL_PAJAK_BUNGA + TOTAL_DENDA) AS DENDA,MIN(WP.IDSUB_PAJAK) AS IDSUBPAJAK,MIN(BU.NAMA_BID_USAHA) AS SUBPajak,0 as cicilan, MIN(wp.Status) as Status " +
				"FROM PERIKSA P INNER JOIN WAJIB_PAJAK WP ON WP.NPWPD=P.NPWPD INNER JOIN BIDANG_USAHA BU ON BU.IDSUB_PAJAK=P.IDSUB_PAJAK " +
				"LEFT JOIN SSPD S ON P.IDPERIKSA=S.IDPERIKSA LEFT JOIN MOHON m on m.NO_SKP = p.NO_SKP " +
				"left join kecamatan kc on wp.ALABAD_KECAMATAN = kc.Kode_Kecamatan left join UPT up on up.KODE_KECAMATAN = kc.KODE_KECAMATAN " +
				"where Substring(NPWPD, 1, 1) = '" + idPajak + "' and NO_SKP not like '%SKPDN%' and NO_SSPD is null and m.NO_SKP is null " +
				"and (TANGGAL_SKP between Date'" + masaDari + "' and DATE_ADD(date'" + masaSampai + "', Interval -1 Month))";
		if (subPajak != 0)
			query += " and idSub_Pajak = " + subPajak;
		if (!tutup)
			query += " and wp.status <> 'Tutup'";
		if (kecamatan.length > 0){
			query += " and (";
			for (int i=0;i<kecamatan.length;i++){
				if (i>0)
					query += " or";
				query += " kc.Name_Kecamatan = '" + kecamatan[i] + "'";
			}
			query += ")";
		}
		query += " GROUP BY P.IDPERIKSA,P.NO_SKP,P.TANGGAL_SKP,P.MASA_PAJAK_DARI,P.MASA_PAJAK_SAMPAI ORDER BY NPWPD, TANGGAL_SKP, NO_SKP;";
		
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.openBawah());
		if (result != null){
			try{
				List<Tunggakan> listSKPDKB = new ArrayList<Tunggakan>();
				while (result.next()){
					tglTempo = result.getDate("Tanggal_SKP");
					cal.set(Calendar.DAY_OF_MONTH, tglTempo.getDate());
					cal.set(Calendar.MONTH, tglTempo.getMonth());
					cal.set(Calendar.YEAR, tglTempo.getYear());
					cal.add(Calendar.MONTH, 1);
					java.sql.Date jatuhTempo = new java.sql.Date(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
					Tunggakan tunggakanModel = new Tunggakan();
					tunggakanModel.setIdPeriksa(result.getInt("IDPERIKSA"));
					tunggakanModel.setNpwpd(result.getString("NPWPD"));
					tunggakanModel.setNamaBadan(result.getString("Nama_Badan"));
					tunggakanModel.setNamaPemilik(result.getString("Nama_Pemilik"));
					tunggakanModel.setAlamat(result.getString("Alamat"));
					tunggakanModel.setNoSkp(result.getString("No_SKP"));
					tunggakanModel.setTglSkp(result.getDate("Tanggal_SKP"));
					tunggakanModel.setMasaPajakDari(result.getDate("Masa_Pajak_Dari"));
					tunggakanModel.setMasaPajakSampai(result.getDate("Masa_Pajak_Sampai"));
					tunggakanModel.setPokok(result.getDouble("Pokok"));
					tunggakanModel.setKenaikan(result.getDouble("Kenaikan"));
					tunggakanModel.setDenda(result.getDouble("Denda"));
					tunggakanModel.setIdSubPajak(result.getInt("IdSubPajak"));
					tunggakanModel.setBidang_usaha(result.getString("SubPajak"));
					tunggakanModel.setCicilan(result.getInt("cicilan"));
					tunggakanModel.setStatus(result.getString("Status"));
					tunggakanModel.setTglJatuhTempo(jatuhTempo);
					tunggakanModel.setDendaSspd(UIController.INSTANCE.hitungDendaSSPD(result.getDouble("Pokok"), jatuhTempo, "SKPDKB", "0"));
					listSKPDKB.add(tunggakanModel);
				}
				return listSKPDKB;
			}catch (SQLException e){
				MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database SQL Error", e.getMessage());
				return null;
			}
		}
		return null;
	}
	
	public List<Tunggakan> getTunggakanAngsuran(String idPajak, int subPajak, Date masaPajakDari, Date masaPajakSampai, boolean tutup, String[] kecamatan){
		Locale.setDefault(new Locale("id","ID"));
		dateNow = (java.sql.Timestamp)ControllerFactory.getMainController().getCpWajibPajakDAOImpl().getDateNow();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String masaDari = formatter.format(masaPajakDari);
		String masaSampai = formatter.format(masaPajakSampai);
		
		String query = "SELECT P.IDPERIKSA, MIN(wp.NPWPD) as NPWPD, MIN(wp.Nama_Badan) as Nama_Badan, MIN(wp.Nama_Pemilik) as Nama_Pemilik, MIN(wp.Alabad_Jalan) as Alamat, " +
				"p.No_SKP, P.TANGGAL_SKP,P.MASA_PAJAK_DARI,P.MASA_PAJAK_SAMPAI, MIN(md.Tanggal_Angsuran) as Tanggal_Angsuran, MIN(md.Angsuran_Pokok) as Pokok, MIN(md.Biaya_Administrasi + md.Denda_SKPDKB) as Denda, " +
				"MIN(wp.IDSUB_Pajak) as IDSUBPAJAK, MIN(BU.Nama_Bid_Usaha) as SubPajak, md.No_Angsuran as Cicilan, MIN(wp.Status) as Status " +
				"FROM mohon_detail md INNER JOIN mohon m on md.IdMohon = m.IdMohon INNER JOIN wajib_pajak wp on wp.NPWPD = m.NPWPD " +
				"INNER JOIN periksa p on p.No_SKP = m.No_SKP AND m.NPWPD = p.NPWPD INNER JOIN BIDANG_USAHA BU ON BU.IDSUB_PAJAK=P.IDSUB_PAJAK " +
				"LEFT JOIN SSPD ss on p.IDPERIKSA = ss.IDPERIKSA AND ss.Cicilan_Ke = md.No_Angsuran " +
				"left join kecamatan kc on wp.ALABAD_KECAMATAN = kc.Kode_Kecamatan left join UPT up on up.KODE_KECAMATAN = kc.KODE_KECAMATAN " +
				"WHERE ss.No_SSPD is null AND Substring(NPWPD, 1, 1) = '" + idPajak + "' and NO_SKP not like '%SKPDN%' " +
				"AND (TANGGAL_SKP between Date'" + masaDari + "' and date'" + masaSampai + "') AND md.Tanggal_Angsuran < Date'" + formatter.format(dateNow) + "'";
		if (subPajak != 0)
			query += " and idSub_Pajak = " + subPajak;
		if (!tutup)
			query += " and wp.status <> 'Tutup'";
		if (kecamatan.length > 0){
			query += " and (";
			for (int i=0;i<kecamatan.length;i++){
				if (i>0)
					query += " or";
				query += " kc.Name_Kecamatan = '" + kecamatan[i] + "'";
			}
			query += ")";
		}
		query += " GROUP BY md.No_Angsuran, P.IDPERIKSA,P.NO_SKP,P.TANGGAL_SKP,P.MASA_PAJAK_DARI,P.MASA_PAJAK_SAMPAI ORDER BY NPWPD, TANGGAL_SKP, NO_SKP;";
		
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.openBawah());
		if (result != null){
			try{
				List<Tunggakan> listSKPDKB = new ArrayList<Tunggakan>();
				while (result.next()){
					Tunggakan tunggakanModel = new Tunggakan();
					tunggakanModel.setIdPeriksa(result.getInt("IDPERIKSA"));
					tunggakanModel.setNpwpd(result.getString("NPWPD"));
					tunggakanModel.setNamaBadan(result.getString("Nama_Badan"));
					tunggakanModel.setNamaPemilik(result.getString("Nama_Pemilik"));
					tunggakanModel.setAlamat(result.getString("Alamat"));
					tunggakanModel.setNoSkp(result.getString("No_SKP"));
					tunggakanModel.setTglSkp(result.getDate("Tanggal_SKP"));
					tunggakanModel.setMasaPajakDari(result.getDate("Masa_Pajak_Dari"));
					tunggakanModel.setMasaPajakSampai(result.getDate("Masa_Pajak_Sampai"));
					tunggakanModel.setPokok(result.getDouble("Pokok"));
					tunggakanModel.setKenaikan(0.00);
					tunggakanModel.setDenda(result.getDouble("Denda"));
					tunggakanModel.setIdSubPajak(result.getInt("IdSubPajak"));
					tunggakanModel.setBidang_usaha(result.getString("SubPajak"));
					tunggakanModel.setCicilan(result.getInt("cicilan"));
					tunggakanModel.setStatus(result.getString("Status"));
					tunggakanModel.setTglJatuhTempo(result.getDate("Tanggal_Angsuran"));
					tunggakanModel.setDendaSspd(UIController.INSTANCE.hitungDendaSSPD(result.getDouble("Pokok"), result.getDate("Tanggal_Angsuran"), "SKPDKB", "0"));
					listSKPDKB.add(tunggakanModel);
				}
				return listSKPDKB;
			}catch (SQLException e){
				MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database SQL Error", e.getMessage());
				return null;
			}
		}
		return null;
	}
	
	public ResultSet getTunggakanSPTPDWP(String npwpd, String tahun, String tahunAkhir){
		String query = "Select sp.NO_SPTPD, sp.TGL_SPTPD, sp.ASSESMENT, sp.MASA_PAJAK_DARI, sp.MASA_PAJAK_SAMPAI, '' as jatuhTempo, sp.DASAR_PENGENAAN, sp.TARIF_PAJAK, sp.PAJAK_TERUTANG, " +
				"sp.DENDA_TAMBAHAN from SPTPD sp left join SSPD ss on ss.IDSPTPD = sp.IDSPTPD " +
				"LEFT JOIN Periksa_Detail pd on sp.NPWPD = pd.NPWPD and pd.Bulan_periksa = To_Char(sp.Masa_Pajak_Dari, 'MM-YYYY') " +
				"where sp.NPWPD = '"+ npwpd + "' and ss.NO_SSPD is null and pd.IDPERIKSA is null";
		if (!tahun.equalsIgnoreCase("") && !tahunAkhir.equalsIgnoreCase(""))
			query += " and year(sp.TGL_SPTPD) >= " + tahun + " and year(sp.TGL_SPTPD) <= " + tahunAkhir + "";
		else if (!tahun.equalsIgnoreCase("") && tahunAkhir.equalsIgnoreCase(""))
			query += " and year(sp.TGL_SPTPD) >= " + tahun + "";
		else if (tahun.equalsIgnoreCase("") && !tahunAkhir.equalsIgnoreCase(""))
			query += " and year(sp.TGL_SPTPD) <= " + tahunAkhir + "";
		query += " Order by Tgl_SPTPD, No_SPTPD;";
	
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.openBawah());
		return result;
	}
	
	@SuppressWarnings("deprecation")
	public List<Tunggakan> getTunggakanSKPDKBWP(String npwpd, String tahun, String tahunAkhir){
		Calendar cal = Calendar.getInstance();
		java.sql.Date tglTempo;
		String query = "SELECT P.IDPERIKSA,P.NO_SKP,P.TANGGAL_SKP,P.MASA_PAJAK_DARI,P.MASA_PAJAK_SAMPAI,now() as jatuhTempo," +
				"MIN(p.Total_Pajak_Periksa - p.Total_Pajak_Terutang) AS POKOK,MIN(p.Total_Kenaikan) as Kenaikan,MIN(P.TOTAL_PAJAK_BUNGA + TOTAL_DENDA) AS DENDA,0 as cicilan " +
				"FROM PERIKSA P LEFT JOIN SSPD S ON P.IDPERIKSA=S.IDPERIKSA LEFT JOIN MOHON m on m.NO_SKP = p.NO_SKP " +
				"where NPWPD = '" + npwpd + "' and NO_SKP not like '%SKPDN%' and NO_SSPD is null and m.NO_SKP is null";
		//if (!tahun.equalsIgnoreCase(""))
			//query += " and year(p.TANGGAL_SKP) = " + tahun + "";
		if (!tahun.equalsIgnoreCase("") && !tahunAkhir.equalsIgnoreCase(""))
			query += " and year(p.TANGGAL_SKP) >= " + tahun + " and year(p.TANGGAL_SKP) <= " + tahunAkhir + "";
		else if (!tahun.equalsIgnoreCase("") && tahunAkhir.equalsIgnoreCase(""))
			query += " and year(p.TANGGAL_SKP) >= " + tahun + "";
		else if (tahun.equalsIgnoreCase("") && !tahunAkhir.equalsIgnoreCase(""))
			query += " and year(p.TANGGAL_SKP) <= " + tahunAkhir + "";
		query += " GROUP BY P.IDPERIKSA,P.NO_SKP,P.TANGGAL_SKP,P.MASA_PAJAK_DARI,P.MASA_PAJAK_SAMPAI ORDER BY TANGGAL_SKP, NO_SKP;";
		
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.openBawah());
		if (result != null){
			try{
				List<Tunggakan> listSKPDKB = new ArrayList<Tunggakan>();
				while (result.next()){
					tglTempo = result.getDate("Tanggal_SKP");
					cal.set(Calendar.DAY_OF_MONTH, tglTempo.getDate());
					cal.set(Calendar.MONTH, tglTempo.getMonth());
					cal.set(Calendar.YEAR, tglTempo.getYear());
					cal.add(Calendar.MONTH, 1);
					java.sql.Date jatuhTempo = new java.sql.Date(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
					Tunggakan tunggakanModel = new Tunggakan();
					tunggakanModel.setIdPeriksa(result.getInt("IDPERIKSA"));
					tunggakanModel.setNoSkp(result.getString("No_SKP"));
					tunggakanModel.setTglSkp(result.getDate("Tanggal_SKP"));
					tunggakanModel.setMasaPajakDari(result.getDate("Masa_Pajak_Dari"));
					tunggakanModel.setMasaPajakSampai(result.getDate("Masa_Pajak_Sampai"));
					tunggakanModel.setPokok(result.getDouble("Pokok"));
					tunggakanModel.setKenaikan(result.getDouble("Kenaikan"));
					tunggakanModel.setDenda(result.getDouble("Denda"));
					tunggakanModel.setCicilan(result.getInt("cicilan"));
					tunggakanModel.setTglJatuhTempo(jatuhTempo);
					tunggakanModel.setDendaSspd(UIController.INSTANCE.hitungDendaSSPD(result.getDouble("Pokok"), jatuhTempo, "SKPDKB", "0"));
					listSKPDKB.add(tunggakanModel);
				}
				return listSKPDKB;
			}catch (SQLException e){
				MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database SQL Error", e.getMessage());
				return null;
			}
		}
		return null;
	}
	
	public List<Tunggakan> getTunggakanAngsuranWP(String npwpd, String tahun, String tahunAkhir){
		String query = "SELECT P.IDPERIKSA, p.No_SKP, P.TANGGAL_SKP,P.MASA_PAJAK_DARI,P.MASA_PAJAK_SAMPAI,md.Tanggal_Angsuran," +
				"md.Angsuran_Pokok as Pokok, (md.Biaya_Administrasi + md.Denda_SKPDKB) as Denda, md.No_Angsuran as Cicilan " +
				"FROM mohon_detail md INNER JOIN mohon m on md.IdMohon = m.IdMohon " +
				"INNER JOIN periksa p on p.No_SKP = m.No_SKP AND m.NPWPD = p.NPWPD " +
				"LEFT JOIN SSPD ss on p.IDPERIKSA = ss.IDPERIKSA AND m.NPWPD = ss.NPWPD AND ss.Cicilan_Ke = md.No_Angsuran " +
				"WHERE ss.No_SSPD is null AND NPWPD = '" + npwpd + "' and NO_SKP not like '%SKPDN%' ";
		//if (!tahun.equalsIgnoreCase(""))
			//query += " and year(P.TANGGAL_SKP) = " + tahun + "";
		if (!tahun.equalsIgnoreCase("") && !tahunAkhir.equalsIgnoreCase(""))
			query += " and year(p.TANGGAL_SKP) >= " + tahun + " and year(p.TANGGAL_SKP) <= " + tahunAkhir + "";
		else if (!tahun.equalsIgnoreCase("") && tahunAkhir.equalsIgnoreCase(""))
			query += " and year(p.TANGGAL_SKP) >= " + tahun + "";
		else if (tahun.equalsIgnoreCase("") && !tahunAkhir.equalsIgnoreCase(""))
			query += " and year(p.TANGGAL_SKP) <= " + tahunAkhir + "";
		query += " ORDER BY TANGGAL_SKP, NO_SKP;";
		
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.openBawah());
		if (result != null){
			try{
				List<Tunggakan> listSKPDKB = new ArrayList<Tunggakan>();
				while (result.next()){
					Tunggakan tunggakanModel = new Tunggakan();
					tunggakanModel.setIdPeriksa(result.getInt("IDPERIKSA"));
					tunggakanModel.setNoSkp(result.getString("No_SKP"));
					tunggakanModel.setTglSkp(result.getDate("Tanggal_SKP"));
					tunggakanModel.setMasaPajakDari(result.getDate("Masa_Pajak_Dari"));
					tunggakanModel.setMasaPajakSampai(result.getDate("Masa_Pajak_Sampai"));
					tunggakanModel.setPokok(result.getDouble("Pokok"));
					tunggakanModel.setKenaikan(0.00);
					tunggakanModel.setDenda(result.getDouble("Denda"));
					tunggakanModel.setCicilan(result.getInt("cicilan"));
					tunggakanModel.setTglJatuhTempo(result.getDate("Tanggal_Angsuran"));
					tunggakanModel.setDendaSspd(UIController.INSTANCE.hitungDendaSSPD(result.getDouble("Pokok"), result.getDate("Tanggal_Angsuran"), "SKPDKB", "0"));
					listSKPDKB.add(tunggakanModel);
				}
				return listSKPDKB;
			}catch (SQLException e){
				MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database SQL Error", e.getMessage());
				return null;
			}
		}
		return null;
	}
	
	public ResultSet getHistorySPTPD(String npwpd){
		String query = "Select * from getSPTPDREALISASIViews where NPWPD = '" + npwpd + "' Order by Tgl_SPTPD, No_SPTPD;";
	
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.openBawah());
		return result;
	}
	
	public ResultSet getHistorySKPDKB(String npwpd){
		String query = "SELECT P.IDPERIKSA,MIN(WP.NPWPD) AS NPWPD,MIN(WP.NAMA_BADAN) AS NAMA_BADAN,MIN(WP.NAMA_PEMILIK) AS NAMA_PEMILIK,MIN(WP.ALABAD_JALAN) AS ALABAD_JALAN," +
				"P.NO_SKP,P.TANGGAL_SKP,P.MASA_PAJAK_DARI,P.MASA_PAJAK_SAMPAI,MIN(p.Total_Pajak_Periksa - p.Total_Pajak_Terutang + p.Total_Kenaikan) AS POKOK," +
				"MIN(P.TOTAL_PAJAK_BUNGA + TOTAL_DENDA) AS DENDA,MIN(WP.IDSUB_PAJAK) as IDSUB_PAJAK,MIN(BU.NAMA_BID_USAHA) as Nama_Bid_Usaha,0 as cicilan, SUM(s.Jumlah_Bayar) as Jumlah_Bayar, " +
				"sum(s.Denda) as DendaSSPD, MIN(s.Tgl_SSPD) as Tgl_SSPD, min(s.Tgl_Cetak) as Tgl_Cetak, GROUP_CONCAT(s.No_SSPD SEPARATOR ', ') as No_SSPD " +
				"FROM PERIKSA P INNER JOIN WAJIB_PAJAK WP ON WP.NPWPD=P.NPWPD INNER JOIN BIDANG_USAHA BU ON BU.IDSUB_PAJAK=P.IDSUB_PAJAK " +
				"LEFT JOIN SSPD AS S ON P.IDPERIKSA=S.IDPERIKSA where NPWPD = '" + npwpd + "' and NO_SKP like '%SKPDKB/%' " +
				"GROUP BY P.IDPERIKSA,P.NO_SKP,P.TANGGAL_SKP,P.MASA_PAJAK_DARI,P.MASA_PAJAK_SAMPAI ORDER BY TANGGAL_SKP, NO_SKP;";
		
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.openBawah());
		return result;
	}
	
	public ResultSet getHistorySKPDKBT(String npwpd){
		String query = "SELECT P.IDPERIKSA,MIN(WP.NPWPD) AS NPWPD,MIN(WP.NAMA_BADAN) AS NAMA_BADAN,MIN(WP.NAMA_PEMILIK) AS NAMA_PEMILIK,MIN(WP.ALABAD_JALAN) AS ALABAD_JALAN," +
				"P.NO_SKP,P.TANGGAL_SKP,P.MASA_PAJAK_DARI,P.MASA_PAJAK_SAMPAI,MIN(p.Total_Pajak_Periksa - p.Total_Pajak_Terutang + p.Total_Kenaikan) AS POKOK," +
				"MIN(P.TOTAL_PAJAK_BUNGA + TOTAL_DENDA) AS DENDA,MIN(WP.IDSUB_PAJAK) as IDSUB_PAJAK,MIN(BU.NAMA_BID_USAHA) as Nama_Bid_Usaha,0 as cicilan, SUM(s.Jumlah_Bayar) as Jumlah_Bayar, " +
				"sum(s.Denda) as DendaSSPD, MIN(s.Tgl_SSPD) as Tgl_SSPD, min(s.Tgl_Cetak) as Tgl_Cetak, GROUP_CONCAT(s.No_SSPD SEPARATOR ', ') as No_SSPD " +
				"FROM PERIKSA P INNER JOIN WAJIB_PAJAK WP ON WP.NPWPD=P.NPWPD INNER JOIN BIDANG_USAHA BU ON BU.IDSUB_PAJAK=P.IDSUB_PAJAK " +
				"LEFT JOIN SSPD AS S ON P.IDPERIKSA=S.IDPERIKSA where NPWPD = '" + npwpd + "' and NO_SKP like '%SKPDKBT/%' " +
				"GROUP BY P.IDPERIKSA,P.NO_SKP,P.TANGGAL_SKP,P.MASA_PAJAK_DARI,P.MASA_PAJAK_SAMPAI ORDER BY TANGGAL_SKP, NO_SKP;";
		
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.openBawah());
		return result;
	}
	
	public ResultSet getHistorySKPDN(String npwpd){
		String query = "SELECT P.IDPERIKSA,WP.NPWPD,WP.NAMA_BADAN,WP.NAMA_PEMILIK,WP.ALABAD_JALAN,P.NO_SKP,P.TANGGAL_SKP," +
				"P.MASA_PAJAK_DARI,P.MASA_PAJAK_SAMPAI,(p.Total_Pajak_Periksa - p.Total_Pajak_Terutang + p.Total_Kenaikan) AS POKOK," +
				"(P.TOTAL_PAJAK_BUNGA + TOTAL_DENDA) AS DENDA,WP.IDSUB_PAJAK,BU.NAMA_BID_USAHA " +
				"FROM PERIKSA P INNER JOIN WAJIB_PAJAK WP ON WP.NPWPD=P.NPWPD INNER JOIN BIDANG_USAHA BU ON BU.IDSUB_PAJAK=P.IDSUB_PAJAK " +
				"where NPWPD = '" + npwpd + "' and NO_SKP like '%SKPDN/%' ORDER BY TANGGAL_SKP, NO_SKP;";
		
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.openBawah());
		return result;
	}
	
	public ResultSet getHistoryAngsuran(String npwpd){
		String query = "SELECT P.IDPERIKSA,WP.NPWPD,WP.NAMA_BADAN,WP.NAMA_PEMILIK,WP.ALABAD_JALAN,P.NO_SKP,P.TANGGAL_SKP," +
				"P.MASA_PAJAK_DARI,P.MASA_PAJAK_SAMPAI,md.No_Angsuran as Cicilan, md.Angsuran_Pokok as Pokok, (md.Biaya_Administrasi + md.Denda_SKPDKB) as Denda," +
				"WP.IDSUB_PAJAK,BU.NAMA_BID_USAHA, s.Jumlah_Bayar, s.Denda as DendaSSPD, s.Tgl_SSPD, s.Tgl_Cetak, s.No_SSPD " +
				"FROM mohon_detail md INNER JOIN mohon m on md.IdMohon = m.IdMohon INNER JOIN PERIKSA P on p.No_SKP = m.No_SKP AND m.NPWPD = p.NPWPD " +
				"INNER JOIN WAJIB_PAJAK WP ON WP.NPWPD=P.NPWPD INNER JOIN BIDANG_USAHA BU ON BU.IDSUB_PAJAK=P.IDSUB_PAJAK " +
				"LEFT JOIN SSPD AS S ON P.IDPERIKSA=S.IDPERIKSA AND md.No_Angsuran = s.Cicilan_ke where NPWPD = '" + npwpd + "' ORDER BY TANGGAL_SKP, NO_SKP, No_Angsuran;";
		
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.openBawah());
		return result;
	}
	
	public ResultSet GetListSPTPD_SKPDKB(Date masaPajakDari, Date masaPajakSampai, String kode, int idsub, Date breakdown){
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String masaDari = formatter.format(masaPajakDari);
		String masaSampai = formatter.format(masaPajakSampai);
		String query1 = "Select wp.NPWPD, wp.Nama_Badan, wp.Nama_Pemilik, wp.ALABAD_JALAN, bu.NAMA_BID_USAHA, sp.NO_SPTPD, sp.TGL_SPTPD, sp.MASA_PAJAK_DARI, " +
				"sp.MASA_PAJAK_SAMPAI, p.No_SKP, p.Masa_Pajak_Dari as SKPDKBDari, p.Masa_Pajak_Sampai as SKPDKBSampai, sp.DASAR_PENGENAAN, sp.TARIF_PAJAK, " +
				"sp.PAJAK_TERUTANG, '-' as DendaTambahan, rg.NO_REGISTRASI, ss.TGL_CETAK, ss.JUMLAH_BAYAR, bu.Kode_Bid_Usaha from SPTPD sp " +
				"left join SSPD ss on ss.IDSPTPD = sp.IDSPTPD AND ss.Tgl_Cetak <= '" + formatter.format(breakdown) + " 23:59:59' INNER JOIN WAJIB_PAJAK wp ON wp.NPWPD=sp.NPWPD left join NO_REGISTRASI rg on rg.NO_SSPD = ss.NO_SSPD " +
				"INNER JOIN BIDANG_USAHA BU ON BU.IDSUB_PAJAK=sp.IDSUB_PAJAK LEFT JOIN Periksa_Detail pd on sp.NPWPD = pd.NPWPD and pd.Bulan_periksa = To_Char(sp.Masa_Pajak_Dari, 'MM-YYYY') " +
				"Left Join Periksa p on p.NPWPD = sp.NPWPD and p.IdPeriksa = pd.IdPeriksa and p.Tanggal_SKP <= '" + formatter.format(breakdown) + "' " + 
				"where " +
				"(TGL_SPTPD between Date'" + masaDari + "' and date'" + masaSampai + "')";
		if (idsub > 0)
			query1 += " AND bu.IdSub_Pajak = " + idsub;
		else if (!kode.equalsIgnoreCase(""))
			query1 += " AND bu.IDPAJAK = '" + kode + "'";
		query1 += " ORDER BY sp.Tgl_SPTPD, bu.IdSub_Pajak, bu.Kode_Bid_Usaha, CAST(SUBSTRING(sp.NO_SPTPD, 1, POSITION('/' in sp.NO_SPTPD) - 1) AS INTEGER)";
		
		ResultSet result = db.ResultExecutedStatementQuery(query1, DBConnection.INSTANCE.openBawah());
		
		return result;
	}

	public ResultSet getListReklamePendapatanDimuka(Integer tahun){
		/*SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String masaDari = formatter.format(masaPajakDari);
		String masaSampai = formatter.format(masaPajakSampai);*/
		String query = "select w.NAMA_BADAN,sp.TGL_SPTPD,concat(sp.MASA_PAJAK_DARI,':',sp.MASA_PAJAK_SAMPAI) as MASA_PAJAK,ss.TGL_SSPD,sp.PAJAK_TERUTANG,(sp.PAJAK_TERUTANG/12) as pendapatan_bulan from SSPD ss " +
					   "left join SPTPD sp on sp.IDSPTPD = ss.IDSPTPD " + 
                       "left join wajib_pajak w on sp.NPWPD = w.NPWPD " +
                       "where w.KEWAJIBAN_PAJAK=7 and ((sp.MASA_PAJAK_DARI >= '"+tahun+"-01-01' and sp.MASA_PAJAK_DARI <= '"+tahun+"-12-31') and sp.TGL_SPTPD like '"+tahun+"-%') and ss.TGL_SSPD not like '"+(tahun+1)+"-%'";
		System.out.println(query);
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		return result;
	}
}
