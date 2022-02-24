package com.dispenda.controller;

import java.math.RoundingMode;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.List;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.eclipse.nebula.widgets.tablecombo.TableCombo;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import com.dispenda.model.BidangUsaha;
import com.dispenda.model.Kecamatan;
import com.dispenda.model.Kelurahan;
import com.dispenda.model.Pajak;
import com.dispenda.model.Pejabat;
import com.dispenda.model.Periksa;
import com.dispenda.model.Sptpd;
import com.ibm.icu.util.GregorianCalendar;

public enum UIController {

	INSTANCE;
	
	private Timestamp dateNow = (Timestamp)ControllerFactory.getMainController().getCpWajibPajakDAOImpl().getDateNow();
	
	public void loadKecamatan(Combo cmbKecamatan, Object[] content){
		cmbKecamatan.removeAll();
		for(int i = 0; i<content.length; i++){
			Kecamatan k = (Kecamatan) content[i];
			cmbKecamatan.add(k.getNamaKecamatan());
			cmbKecamatan.setData(k.getNamaKecamatan(), k.getKodeKecamatan());
			cmbKecamatan.setData(Integer.toString(i), k.getIdKecamatan());
		}
	}
	
	public void loadKelurahan(Combo cmbKelurahan, Object[] content){
		cmbKelurahan.removeAll();
		for(int i = 0; i<content.length; i++){
			Kelurahan k = (Kelurahan) content[i];
			cmbKelurahan.add(k.getNamaKelurahan());
			cmbKelurahan.setData(k.getNamaKelurahan(), k.getKodeKelurahan());
			cmbKelurahan.setData(Integer.toString(i), k.getIdKelurahan());
		}
	}
	
	public void loadPejabat(Combo cmbPejabat, Object[] content){
		cmbPejabat.removeAll();
		for(int i = 0; i<content.length; i++){
			Pejabat k = (Pejabat) content[i];
			cmbPejabat.add(k.getNamaPejabat());
			cmbPejabat.setData(k.getNamaPejabat(), k);
		}
	}
	
	public void loadBidangUsaha(Combo cmbBidangUsaha, Object[] content){
		cmbBidangUsaha.removeAll();
		for(int i = 0; i<content.length; i++){
			BidangUsaha b = (BidangUsaha) content[i];
			cmbBidangUsaha.add(b.getNamaBidUsaha());
			cmbBidangUsaha.setData(b.getNamaBidUsaha(), b.getKodeBidUsaha());
			cmbBidangUsaha.setData(Integer.toString(i), b.getIdSubPajak());
			cmbBidangUsaha.setData("B"+Integer.toString(b.getIdSubPajak()),b.getNamaBidUsaha());
		}
	}
	
	public void loadPajak(Combo cmbPajak, Object[] content){
		cmbPajak.removeAll();
		for(int i = 0; i<content.length; i++){
			Pajak p = (Pajak) content[i];
			cmbPajak.add(p.getNamaPajak());
			cmbPajak.setData(p.getNamaPajak(), p.getKodePajak());
			cmbPajak.setData(Integer.toString(i), p.getIdPajak());
		}
	}
	
	@SuppressWarnings("deprecation")
	public void loadMasaPajak(Combo cmbMasaPajak, Object[] content){
		cmbMasaPajak.removeAll();
		for(int i = 0; i<content.length; i++){
			Sptpd sp = (Sptpd) content[i];
			if (sp.getMasaPajakDari().getMonth() == sp.getMasaPajakSampai().getMonth() && sp.getMasaPajakDari().getYear() == sp.getMasaPajakSampai().getYear())
			{
				cmbMasaPajak.add(formatMonth(sp.getMasaPajakDari().getMonth() + 1, Locale.getDefault()) + " " + (sp.getMasaPajakDari().getYear() + 1900));
				cmbMasaPajak.setData(formatMonth(sp.getMasaPajakDari().getMonth() + 1, Locale.getDefault()) + " " + (sp.getMasaPajakDari().getYear() + 1900), sp.getNoSPTPD());
				cmbMasaPajak.setData(sp.getNoSPTPD(), i);
			}
			else
			{
				cmbMasaPajak.add(formatMonth(sp.getMasaPajakDari().getMonth() + 1, Locale.getDefault()) + " " + (sp.getMasaPajakDari().getYear() + 1900) + " - " + 
						formatMonth(sp.getMasaPajakSampai().getMonth() + 1, Locale.getDefault()) + " " + (sp.getMasaPajakSampai().getYear() + 1900));
				cmbMasaPajak.setData(formatMonth(sp.getMasaPajakDari().getMonth() + 1, Locale.getDefault()) + " " + (sp.getMasaPajakDari().getYear() + 1900) + " - " + 
						formatMonth(sp.getMasaPajakSampai().getMonth() + 1, Locale.getDefault()) + " " + (sp.getMasaPajakSampai().getYear() + 1900), sp.getNoSPTPD());
				cmbMasaPajak.setData(sp.getNoSPTPD(), i);
			}
			cmbMasaPajak.setData(Integer.toString(i), sp.getNoSPTPD());
			
		}
	}
	
	@SuppressWarnings("deprecation")
	public void loadTMasaPajak(TableCombo cmbMasaPajak, Object[] content){
		cmbMasaPajak.getTable().removeAll();
		Table table = cmbMasaPajak.getTable();
		for(int i = 0; i<content.length; i++){
			Sptpd sp = (Sptpd) content[i];
			TableItem ti = new TableItem(table, SWT.NONE);
			if (sp.getNPWPD().substring(0, 1).equalsIgnoreCase("7")){
				ti.setText(new String[] {sp.getNoSPTPD(),sp.getMasaPajakDari().getDate() + " " + formatMonth(sp.getMasaPajakDari().getMonth() + 1, Locale.getDefault()) + " " + (sp.getMasaPajakDari().getYear() + 1900) + " - " + 
						sp.getMasaPajakSampai().getDate() + " " + formatMonth(sp.getMasaPajakSampai().getMonth() + 1, Locale.getDefault()) + " " + (sp.getMasaPajakSampai().getYear() + 1900)});
				cmbMasaPajak.setData(sp.getMasaPajakDari().getDate() + " " + formatMonth(sp.getMasaPajakDari().getMonth() + 1, Locale.getDefault()) + " " + (sp.getMasaPajakDari().getYear() + 1900) +
						" - " + sp.getMasaPajakSampai().getDate() + " " + formatMonth(sp.getMasaPajakSampai().getMonth() + 1, Locale.getDefault()) + " " + (sp.getMasaPajakSampai().getYear() + 1900));
				cmbMasaPajak.setData(sp.getNoSPTPD(), i);
			}else{
				if (sp.getMasaPajakDari().getMonth() == sp.getMasaPajakSampai().getMonth() && sp.getMasaPajakDari().getYear() == sp.getMasaPajakSampai().getYear())
				{
//					cmbMasaPajak.add(formatMonth(sp.getMasaPajakDari().getMonth() + 1, Locale.getDefault()) + " " + (sp.getMasaPajakDari().getYear() + 1900));
					ti.setText(new String[] {sp.getNoSPTPD(),formatMonth(sp.getMasaPajakDari().getMonth() + 1, Locale.getDefault()) + " " + (sp.getMasaPajakDari().getYear() + 1900)});
					cmbMasaPajak.setData(formatMonth(sp.getMasaPajakDari().getMonth() + 1, Locale.getDefault()) + " " + (sp.getMasaPajakDari().getYear() + 1900), sp.getNoSPTPD());
					cmbMasaPajak.setData(sp.getNoSPTPD(), i);
				}
				else
				{
					ti.setText(new String[] {sp.getNoSPTPD(),formatMonth(sp.getMasaPajakDari().getMonth() + 1, Locale.getDefault()) + " " + (sp.getMasaPajakDari().getYear() + 1900) + " - " + 
							formatMonth(sp.getMasaPajakSampai().getMonth() + 1, Locale.getDefault()) + " " + (sp.getMasaPajakSampai().getYear() + 1900)});
//					cmbMasaPajak.add(formatMonth(sp.getMasaPajakDari().getMonth() + 1, Locale.getDefault()) + " " + (sp.getMasaPajakDari().getYear() + 1900) + " - " + 
//							formatMonth(sp.getMasaPajakSampai().getMonth() + 1, Locale.getDefault()) + " " + (sp.getMasaPajakSampai().getYear() + 1900));
					cmbMasaPajak.setData(formatMonth(sp.getMasaPajakDari().getMonth() + 1, Locale.getDefault()) + " " + (sp.getMasaPajakDari().getYear() + 1900) + " - " + 
							formatMonth(sp.getMasaPajakSampai().getMonth() + 1, Locale.getDefault()) + " " + (sp.getMasaPajakSampai().getYear() + 1900), sp.getNoSPTPD());
					cmbMasaPajak.setData(sp.getNoSPTPD(), i);
				}
			}
			
			cmbMasaPajak.setData(Integer.toString(i), sp.getNoSPTPD());
			
		}
	}
	
	public void loadMasaPajakPeriksa(Combo cmbMasaPajak, Object[] content){
		cmbMasaPajak.removeAll();
		for(int i = 0; i<content.length; i++){
			Periksa p = (Periksa) content[i];
			
			cmbMasaPajak.add(p.getMasaPajak());
			cmbMasaPajak.setData(p.getMasaPajak(), p.getIdPeriksa());
			cmbMasaPajak.setData(Integer.toString(i), p.getIdPeriksa());
			
		}
	}
	
	@SuppressWarnings("deprecation")
	public void loadMasaPajakPeriksaDari(Combo cmbMasaPajak, Object[] content){
		cmbMasaPajak.removeAll();
		for(int i = 0; i<content.length; i++){
			Sptpd sp = (Sptpd) content[i];
			if (sp.getMasaPajakDari().getMonth() == sp.getMasaPajakSampai().getMonth())
			{
				cmbMasaPajak.add(formatMonth(sp.getMasaPajakDari().getMonth() + 1, Locale.getDefault()) + " " + (sp.getMasaPajakDari().getYear() + 1900));
				cmbMasaPajak.setData(formatMonth(sp.getMasaPajakDari().getMonth() + 1, Locale.getDefault()) + " " + (sp.getMasaPajakDari().getYear() + 1900), sp.getMasaPajakDari());
			}
			else
			{
				cmbMasaPajak.add(formatMonth(sp.getMasaPajakDari().getMonth() + 1, Locale.getDefault()) + " " + (sp.getMasaPajakDari().getYear() + 1900) + " - " + 
						formatMonth(sp.getMasaPajakSampai().getMonth() + 1, Locale.getDefault()) + " " + (sp.getMasaPajakSampai().getYear() + 1900));
				cmbMasaPajak.setData(formatMonth(sp.getMasaPajakDari().getMonth() + 1, Locale.getDefault()) + " " + (sp.getMasaPajakDari().getYear() + 1900) + " - " + 
						formatMonth(sp.getMasaPajakSampai().getMonth() + 1, Locale.getDefault()) + " " + (sp.getMasaPajakSampai().getYear() + 1900), sp.getMasaPajakDari());
			}
			cmbMasaPajak.setData(Integer.toString(i), sp.getNoSPTPD());
			
		}
	}
	
	@SuppressWarnings("deprecation")
	public void loadMasaPajakPeriksaSampai(Combo cmbMasaPajak, Object[] content){
		cmbMasaPajak.removeAll();
		for(int i = 0; i<content.length; i++){
			Sptpd sp = (Sptpd) content[i];
			if (sp.getMasaPajakDari().getMonth() == sp.getMasaPajakSampai().getMonth())
			{
				cmbMasaPajak.add(formatMonth(sp.getMasaPajakDari().getMonth() + 1, Locale.getDefault()) + " " + (sp.getMasaPajakDari().getYear() + 1900));
				cmbMasaPajak.setData(formatMonth(sp.getMasaPajakDari().getMonth() + 1, Locale.getDefault()) + " " + (sp.getMasaPajakDari().getYear() + 1900), sp.getMasaPajakSampai());
			}
			else
			{
				cmbMasaPajak.add(formatMonth(sp.getMasaPajakDari().getMonth() + 1, Locale.getDefault()) + " " + (sp.getMasaPajakDari().getYear() + 1900) + " - " + 
						formatMonth(sp.getMasaPajakSampai().getMonth() + 1, Locale.getDefault()) + " " + (sp.getMasaPajakSampai().getYear() + 1900));
				cmbMasaPajak.setData(formatMonth(sp.getMasaPajakDari().getMonth() + 1, Locale.getDefault()) + " " + (sp.getMasaPajakDari().getYear() + 1900) + " - " + 
						formatMonth(sp.getMasaPajakSampai().getMonth() + 1, Locale.getDefault()) + " " + (sp.getMasaPajakSampai().getYear() + 1900), sp.getMasaPajakSampai());
			}
			cmbMasaPajak.setData(Integer.toString(i), sp.getNoSPTPD());
			
		}
	}
	
	public void createColumn(Table tbl, ResultSet result){
		//System.out.println("Create column check");
		String[] listColumns = null;
		tbl.removeAll();
		try {
			listColumns = new String[result.getMetaData().getColumnCount()];
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		for(TableColumn col : tbl.getColumns()){
			col.dispose();
		}
		
		try {
			for (int i=0;i<result.getMetaData().getColumnCount();i++){
				listColumns[i] = result.getMetaData().getColumnLabel(i+1).replace("_", " ");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for (int i=listColumns.length - 1;i>=0;i--)
		{
			TableColumn colPajak = new TableColumn(tbl, SWT.NONE,0);
			colPajak.setText(listColumns[i]);
			colPajak.setWidth(120);
		}
		
		TableColumn colPajak = new TableColumn(tbl, SWT.NONE,0);
		colPajak.setText("No.");
		colPajak.setWidth(50);
	}
	
	public void loadData(Table tbl, ResultSet result){
		//System.out.println("Load data check");
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		Locale ind = new Locale("id", "ID");
		NumberFormat indFormat = NumberFormat.getCurrencyInstance(ind);
		int count = 1;
		try
		{
			while (result.next())
			{
				TableItem item = new TableItem(tbl, SWT.NONE);
				item.setText(0, count+".");
				for (int i=0;i<result.getMetaData().getColumnCount();i++){
					if (result.getMetaData().getColumnTypeName(i+1) == "VARCHAR"){
						if (result.getMetaData().getColumnName(i+1).equalsIgnoreCase("MASAPAJAK"))
							item.setText(i+1, getMasaPajak(result.getString(i+1)));
						else
							if (result.getString(i+1) != null)
								item.setText(i+1, result.getString(i+1));
							else
								item.setText(i+1, "");
					}
					else if (result.getMetaData().getColumnTypeName(i+1) == "DATE"){
						item.setText(i+1, formatter.format(result.getDate(i+1)));
					}
					else if (result.getMetaData().getColumnTypeName(i+1) == "TIMESTAMP"){
						if (result.getTimestamp(i+1) != null)
							item.setText(i+1, formatter.format(result.getTimestamp(i+1)));
						else
							item.setText(i+1, "-");
					}
					else if (result.getMetaData().getColumnTypeName(i+1) == "DOUBLE"){
//						if (result.getDouble(i+1) < 0)
//							item.setText(i, indFormat.format(0));
//						else
							item.setText(i+1, indFormat.format(result.getDouble(i+1)));
					}
					else if (result.getMetaData().getColumnTypeName(i+1) == "INTEGER"){
						item.setText(i+1, String.valueOf(result.getInt(i+1)));
					}
					else if (result.getMetaData().getColumnTypeName(i+1) == "BIGINT"){
						item.setText(i+1, String.valueOf(result.getInt(i+1)));
					}
					else if (result.getMetaData().getColumnTypeName(i+1) == "DECIMAL"){
						item.setText(i+1, indFormat.format(result.getDouble(i+1)));
					}else if (result.getMetaData().getColumnTypeName(i+1) == "BOOLEAN"){
						item.setText(i+1, String.valueOf(result.getBoolean(i+1)));
					}
				}
				count++;
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void loadDataSKPDLBCov(Table tbl, ResultSet result){
		//System.out.println("Load data check");
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		Locale ind = new Locale("id", "ID");
		NumberFormat indFormat = NumberFormat.getCurrencyInstance(ind);
		int count = 1;
		try
		{
			while (result.next())
			{
				TableItem item = new TableItem(tbl, SWT.NONE);
				item.setText(0, count+".");
				for (int i=0;i<result.getMetaData().getColumnCount();i++){
					if (result.getMetaData().getColumnTypeName(i+1) == "VARCHAR"){
						item.setText(i+1, result.getString(i+1));
					}
					else if (result.getMetaData().getColumnTypeName(i+1) == "DATE"){
						item.setText(i+1, formatter.format(result.getDate(i+1)));
					}
					else if (result.getMetaData().getColumnTypeName(i+1) == "TIMESTAMP"){
						if (result.getTimestamp(i+1) != null)
							item.setText(i+1, formatter.format(result.getTimestamp(i+1)));
						else
							item.setText(i+1, "-");
					}
					else if (result.getMetaData().getColumnTypeName(i+1) == "DOUBLE"){
//						if (result.getDouble(i+1) < 0)
//							item.setText(i, indFormat.format(0));
//						else
							item.setText(i+1, indFormat.format(result.getDouble(i+1)));
					}
					else if (result.getMetaData().getColumnTypeName(i+1) == "INTEGER"){
						item.setText(i+1, String.valueOf(result.getInt(i+1)));
					}
					else if (result.getMetaData().getColumnTypeName(i+1) == "BIGINT"){
						item.setText(i+1, String.valueOf(result.getInt(i+1)));
					}
					else if (result.getMetaData().getColumnTypeName(i+1) == "DECIMAL"){
						item.setText(i+1, indFormat.format(result.getDouble(i+1)));
					}else if (result.getMetaData().getColumnTypeName(i+1) == "BOOLEAN"){
						item.setText(i+1, String.valueOf(result.getBoolean(i+1)));
					}
				}
				count++;
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void loadDataVert(Table tbl, ResultSet result){
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		Locale ind = new Locale("id", "ID");
		NumberFormat indFormat = NumberFormat.getCurrencyInstance(ind);
		int count = 1;
		try
		{
			tbl.removeAll();
			for(TableColumn col : tbl.getColumns()){
				col.dispose();
			}
			TableColumn colPajak1 = new TableColumn(tbl, SWT.NONE,0);
			colPajak1.setText("");
			colPajak1.setWidth(250);
			TableColumn colPajak2 = new TableColumn(tbl, SWT.NONE,0);
			colPajak2.setText("");
			colPajak2.setWidth(200);
			TableColumn colPajak = new TableColumn(tbl, SWT.NONE,0);
			colPajak.setText("No.");
			colPajak.setWidth(50);
			while (result.next())
			{
				for (int i=0;i<result.getMetaData().getColumnCount();i++){
					TableItem item = new TableItem(tbl, SWT.NONE);
					item.setText(0, count+".");
					if (result.getMetaData().getColumnTypeName(i+1) == "VARCHAR"){
						item.setText(1, result.getMetaData().getColumnName(i+1).replace("_", " "));
						if (result.getMetaData().getColumnName(i+1).equalsIgnoreCase("MASAPAJAK"))
							item.setText(2, getMasaPajak(result.getString(i+1)));
						else
							if (result.getString(i+1) != null)
								item.setText(2, result.getString(i+1));
							else
								item.setText(2, "");
					}
					else if (result.getMetaData().getColumnTypeName(i+1) == "DATE"){
						item.setText(1, result.getMetaData().getColumnName(i+1).replace("_", " "));
						item.setText(2, formatter.format(result.getDate(i+1)));
					}
					else if (result.getMetaData().getColumnTypeName(i+1) == "TIMESTAMP"){
						item.setText(1, result.getMetaData().getColumnName(i+1).replace("_", " "));
						if (result.getTimestamp(i+1) != null)
							item.setText(2, formatter.format(result.getTimestamp(i+1)));
						else
							item.setText(2, "-");
					}
					else if (result.getMetaData().getColumnTypeName(i+1) == "DOUBLE"){
//						if (result.getDouble(i+1) < 0)
//							item.setText(i, indFormat.format(0));
//						else
						item.setText(1, result.getMetaData().getColumnName(i+1).replace("_", " "));
							item.setText(2, indFormat.format(result.getDouble(i+1)));
					}
					else if (result.getMetaData().getColumnTypeName(i+1) == "INTEGER"){
						item.setText(1, result.getMetaData().getColumnName(i+1).replace("_", " "));
						item.setText(2, String.valueOf(result.getInt(i+1)));
					}
					else if (result.getMetaData().getColumnTypeName(i+1) == "BIGINT"){
						item.setText(1, result.getMetaData().getColumnName(i+1).replace("_", " "));
						item.setText(2, String.valueOf(result.getInt(i+1)));
					}
					else if (result.getMetaData().getColumnTypeName(i+1) == "DECIMAL"){
						item.setText(1, result.getMetaData().getColumnName(i+1).replace("_", " "));
						item.setText(2, indFormat.format(result.getDouble(i+1)));
					}else if (result.getMetaData().getColumnTypeName(i+1) == "BOOLEAN"){
						item.setText(1, result.getMetaData().getColumnName(i+1).replace("_", " "));
						item.setText(2, String.valueOf(result.getBoolean(i+1)));
					}
					count++;
				}
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public String getMasaPajak(String masaPajak){
		Locale ind = new Locale("id", "ID");
		String retValue;
		try{
			String arrMasaPajak[] =  masaPajak.split(" - ");
			String masaDari = formatMonth(Integer.parseInt(arrMasaPajak[0].split("-")[1]), ind) + " " + arrMasaPajak[0].split("-")[0];
			String masaSampai = formatMonth(Integer.parseInt(arrMasaPajak[1].split("-")[1]), ind) + " " + arrMasaPajak[1].split("-")[0];
			retValue = masaDari + " - " + masaSampai;	
		}
		catch (Exception ex){
			retValue = formatMonth(Integer.parseInt(masaPajak.split("-")[1]), ind) + " " + masaPajak.split("-")[0];
		}
			
		return retValue;
	}
	
	public String formatMonth(int month, Locale locale) {
	    DateFormatSymbols symbols = new DateFormatSymbols(locale);
	    String[] monthNames = symbols.getMonths();
	    return monthNames[month - 1];
	}
	
	public int getMonthDiff(Date startDate, Date endDate){
		GregorianCalendar startCalendar = new GregorianCalendar();
		startCalendar.setTime(startDate);
		GregorianCalendar endCalendar = new GregorianCalendar();
		endCalendar.setTime(endDate);

		int diffYear = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
		int diffMonth = diffYear * 12 + endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH);
		
		return diffMonth;
	}
	
	@SuppressWarnings("deprecation")
	public Double hitungDendaSSPD(Double pajakTerutang, Date jatuhTempo, String tipe, String kode){
		Double retValue = 0.0;
		Date tglSetor = new Date(dateNow.getYear(), dateNow.getMonth(), dateNow.getDate()); //new Date(datePembayaran.getYear() - 1900, datePembayaran.getMonth(), datePembayaran.getDay());
		Date tglTempo = new Date(jatuhTempo.getYear(), jatuhTempo.getMonth(), jatuhTempo.getDate());
		if (tipe.equalsIgnoreCase("SPTPD") && !kode.equalsIgnoreCase("7")) {
			if (tglSetor.compareTo(tglTempo) > 0)
			{
				Integer lamaBunga = ((tglSetor.getYear() - tglTempo.getYear()) * 12) + tglSetor.getMonth() - tglTempo.getMonth();
//						 (tglSetor.getDate() > tglTempo.getDate() ? 1 : 0);
				if (lamaBunga <= 24)
					retValue = 0.02 * lamaBunga * pajakTerutang;
				else
					retValue = 0.02 * 24 * pajakTerutang;
			}
			else
				retValue = 0.0;
		}
		else{
			if (tglSetor.compareTo(tglTempo) > 0)
			{
				Integer lamaBunga = ((tglSetor.getYear() - tglTempo.getYear()) * 12) + tglSetor.getMonth() - tglTempo.getMonth() +
						 (tglSetor.getDate() > tglTempo.getDate() ? 1 : 0);
				if (lamaBunga <= 24)
					retValue = 0.02 * lamaBunga * pajakTerutang;
				else
					retValue = 0.02 * 24 * pajakTerutang;
			}
			else
				retValue = 0.0;
		}
		
		DecimalFormat df = new DecimalFormat("#.##");
		df.setMaximumFractionDigits(0);
		df.setRoundingMode(RoundingMode.UP);
		return Double.parseDouble(df.format(retValue));
	}
	
	/*@SuppressWarnings("deprecation")
	public Double hitungDendaSSPD(Double pajakTerutang, java.sql.Date jatuhTempo){
		Double retValue = 0.0;
		java.sql.Date tglSetor = new java.sql.Date(dateNow.getYear(), dateNow.getMonth(), dateNow.getDate()); //new Date(datePembayaran.getYear() - 1900, datePembayaran.getMonth(), datePembayaran.getDay());
		java.sql.Date tglTempo = new java.sql.Date(jatuhTempo.getYear(), jatuhTempo.getMonth(), jatuhTempo.getDate());
		if (tglSetor.compareTo(tglTempo) > 0)
		{
			Integer lamaBunga = ((tglSetor.getYear() - tglTempo.getYear()) * 12) + tglSetor.getMonth() - tglTempo.getMonth() +
					 (tglSetor.getDate() > tglTempo.getDate() ? 1 : 0);
			if (lamaBunga <= 24)
				retValue = 0.02 * lamaBunga * pajakTerutang;
			else
				retValue = 0.02 * 24 * pajakTerutang;
		}
		else
			retValue = 0.0;
		
		return retValue;
	}*/
}
