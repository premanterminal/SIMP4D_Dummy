package com.dispenda.angsuran.views;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.SWTResourceManager;

import com.dispenda.controller.ControllerFactory;
import com.dispenda.controller.UIController;
import com.dispenda.model.PajakProvider;
import com.dispenda.model.Sptpd;
import com.dispenda.model.UserModul;
import com.dispenda.object.GlobalVariable;
import com.dispenda.object.Preferences;
import com.dispenda.report.ReportAction;
import com.dispenda.report.ReportAppContext;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

public class DaftarAngsuranView extends ViewPart {
	public DaftarAngsuranView() {
	}

	public static final String ID = DaftarAngsuranView.class.getName();
	private Table tblDaftarAngsuran;
	private List<Sptpd> listMasaPajakDari;
	private List<Sptpd> listMasaPajakSampai;
	private Combo cmbTerbitAngsuran;
	private Combo cmbSampai;
	@SuppressWarnings("unused")
	private int indexAwal;
	@SuppressWarnings("unused")
	private int indexAkhir;
	private java.sql.Date masaPajakDari;
	private	java.sql.Date masaPajakSampai;
	private Combo cmbKewajibanPajak;
	private Locale ind = new Locale("id", "ID");
	private NumberFormat indFormat = NumberFormat.getCurrencyInstance(ind);
	private Timestamp dateNow;
	private Color fontColor = new Color(Display.getCurrent(),Preferences.FONT_COLOR);
	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new GridLayout(6, false));
		dateNow = (java.sql.Timestamp)ControllerFactory.getMainController().getCpWajibPajakDAOImpl().getDateNow();
		Label lblKewajibanPajak = new Label(parent, SWT.NONE);
		lblKewajibanPajak.setForeground(fontColor);
		lblKewajibanPajak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblKewajibanPajak.setText("Kewajiban Pajak");
		
		cmbKewajibanPajak = new Combo(parent, SWT.READ_ONLY);
		cmbKewajibanPajak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		cmbKewajibanPajak.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 3, 1));
		cmbKewajibanPajak.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		cmbKewajibanPajak.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		UIController.INSTANCE.loadPajak(cmbKewajibanPajak, PajakProvider.INSTANCE.getPajak().toArray());
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		
		Label lblTerbitAngsuran = new Label(parent, SWT.NONE);
		lblTerbitAngsuran.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblTerbitAngsuran.setForeground(fontColor);
		lblTerbitAngsuran.setText("Terbit Angsuran");
		
		cmbTerbitAngsuran = new Combo(parent, SWT.READ_ONLY);
		cmbTerbitAngsuran.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		cmbTerbitAngsuran.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				loadMasaPajakSampai(listMasaPajakDari.get(cmbTerbitAngsuran.getSelectionIndex()).getMasaPajakSampai());

				masaPajakDari = listMasaPajakDari.get(cmbTerbitAngsuran.getSelectionIndex()).getMasaPajakDari();
				masaPajakSampai = listMasaPajakSampai.get(0).getMasaPajakSampai();
			}
		});
		cmbTerbitAngsuran.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				indexAwal = cmbTerbitAngsuran.getSelectionIndex();
			}
		});
		cmbTerbitAngsuran.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 3, 1));
		cmbTerbitAngsuran.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		cmbTerbitAngsuran.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		
		Label lblSampai = new Label(parent, SWT.NONE);
		lblSampai.setForeground(fontColor);
		lblSampai.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblSampai.setText(" Sampai");
		
		cmbSampai = new Combo(parent, SWT.READ_ONLY);
		cmbSampai.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		GridData gd_cmbSampai = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_cmbSampai.widthHint = 162;
		cmbSampai.setLayoutData(gd_cmbSampai);
		cmbSampai.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (!cmbSampai.getText().equalsIgnoreCase(""))
					masaPajakSampai = listMasaPajakSampai.get(cmbSampai.getSelectionIndex()).getMasaPajakSampai();
				else
					masaPajakSampai = listMasaPajakSampai.get(0).getMasaPajakSampai();
			}
		});
		cmbSampai.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				indexAkhir = cmbSampai.getSelectionIndex();
			}
		});
		cmbSampai.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		cmbSampai.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		new Label(parent, SWT.NONE);
		
		loadMasaPajakDari();
		
		Button btnLihat = new Button(parent, SWT.NONE);
		btnLihat.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (!cmbTerbitAngsuran.getText().equalsIgnoreCase("")){
					ResultSet result;
					ResultSet resultLunas;
					tblDaftarAngsuran.removeAll();
					tblLunas.removeAll();
					if (cmbKewajibanPajak.getText().equalsIgnoreCase("")){
						result = ControllerFactory.getMainController().getCpMohonDAOImpl().GetDaftarAngsuran(masaPajakDari, masaPajakSampai);
						resultLunas = ControllerFactory.getMainController().getCpMohonDAOImpl().GetDaftarAngsuranLunas(masaPajakDari, masaPajakSampai);
					}else{
						result = ControllerFactory.getMainController().getCpMohonDAOImpl().GetDaftarAngsuranByKode(masaPajakDari, masaPajakSampai, cmbKewajibanPajak.getData(Integer.toString(cmbKewajibanPajak.getSelectionIndex())).toString());
						resultLunas = ControllerFactory.getMainController().getCpMohonDAOImpl().GetDaftarAngsuranLunasByKode(masaPajakDari, masaPajakSampai, cmbKewajibanPajak.getData(Integer.toString(cmbKewajibanPajak.getSelectionIndex())).toString());
					}
					createColumns();
					loadDaftarAngsuran(result);
					loadDaftarAngsuranLunas(resultLunas);
					if (tblDaftarAngsuran.getItemCount() < 1 && tblLunas.getItemCount() < 1)
						MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Data tidak ada");
				}
				else{
					MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Silahkan isi Bulan Terbit Angsuran");
				}
				
			}
		});
		btnLihat.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		GridData gd_btnLihat = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_btnLihat.widthHint = 90;
		btnLihat.setLayoutData(gd_btnLihat);
		btnLihat.setText("Lihat");
		
		Button btnCetak = new Button(parent, SWT.NONE);
		btnCetak.addSelectionListener(new SelectionAdapter() {
			@SuppressWarnings("deprecation")
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(isPrint()){
					String pajak = "";
					String bulan = "";
					if (!cmbKewajibanPajak.getText().equalsIgnoreCase(""))
						pajak = "Kewajiban Pajak: " + cmbKewajibanPajak.getText();
					if (cmbTerbitAngsuran.getText().equalsIgnoreCase(cmbSampai.getText()))
						bulan = UIController.INSTANCE.formatMonth(masaPajakDari.getMonth() + 1, ind) + " " + (masaPajakDari.getYear() + 1900);
					else						
						bulan = UIController.INSTANCE.formatMonth(masaPajakDari.getMonth() + 1, ind) + " " + (masaPajakDari.getYear() + 1900) + "-" + UIController.INSTANCE.formatMonth(masaPajakSampai.getMonth() + 1, ind) + " " + (masaPajakSampai.getYear() + 1900);
					Timestamp dateNow = (java.sql.Timestamp)ControllerFactory.getMainController().getCpWajibPajakDAOImpl().getDateNow();
					SimpleDateFormat formatter = new SimpleDateFormat("d  yyyy HH:mm:ss");
					String tglCetak = formatter.format(dateNow);
					StringBuilder sbCetak = new StringBuilder(tglCetak);
					sbCetak.insert(tglCetak.indexOf(" ") + 1 , UIController.INSTANCE.formatMonth(dateNow.getMonth() + 1, ind));
					ReportAppContext.name = "Variable";
					ReportAppContext.map.put("KodePajak", pajak);
					ReportAppContext.map.put("Bulan", bulan);
					ReportAppContext.map.put("TglCetak", sbCetak.toString());
					ReportAppContext.nameObject = "DaftarAngsuran";
					ReportAppContext.classLoader = DaftarAngsuranView.class.getClassLoader();
					if(!ReportAppContext.object.isEmpty())
						ReportAppContext.object.clear();
					for(int j = 0;j<tblDaftarAngsuran.getItems().length;j++){
						List<String> values = new ArrayList<String>();
						for(int i=0;i<tblDaftarAngsuran.getColumnCount();i++){
							values.add(tblDaftarAngsuran.getItem(j).getText(i));
						}
						ReportAppContext.object.put(Integer.toString(j), values);
					}
					
					ReportAction.start("DaftarAngsuran.rptdesign");
				}else
					MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Anda tidak memiliki hak untuk mencetak.");
			}
		});
		GridData gd_btnCetak = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnCetak.widthHint = 90;
		btnCetak.setLayoutData(gd_btnCetak);
		btnCetak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnCetak.setText("Cetak");
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		
		TabFolder tabFolder = new TabFolder(parent, SWT.NONE);
		tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 6, 1));
		
		TabItem tbtmTertunggak = new TabItem(tabFolder, SWT.NONE);
		tbtmTertunggak.setText("Tertunggak");
		
		tblDaftarAngsuran = new Table(tabFolder, SWT.BORDER | SWT.FULL_SELECTION | SWT.VIRTUAL);
		tbtmTertunggak.setControl(tblDaftarAngsuran);
		tblDaftarAngsuran.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		tblDaftarAngsuran.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE-1, SWT.NORMAL));
		tblDaftarAngsuran.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		tblDaftarAngsuran.setHeaderVisible(true);
		tblDaftarAngsuran.setLinesVisible(true);
		
		TabItem tbtmLunas = new TabItem(tabFolder, SWT.NONE);
		tbtmLunas.setText("Lunas");
		
		tblLunas = new Table(tabFolder, SWT.BORDER | SWT.FULL_SELECTION | SWT.VIRTUAL);
		tblLunas.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		tblLunas.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE-1, SWT.NORMAL));
		tblLunas.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		tbtmLunas.setControl(tblLunas);
		tblLunas.setHeaderVisible(true);
		tblLunas.setLinesVisible(true);

	}
	
	private void loadMasaPajakDari()
	{
		listMasaPajakDari = ControllerFactory.getMainController().getCpSptpdDAOImpl().getMasaPeriksaDari();
		
		if (listMasaPajakDari.size() > 0)
		{
			UIController.INSTANCE.loadMasaPajakPeriksaDari(cmbTerbitAngsuran, listMasaPajakDari.toArray()); //PajakProvider.INSTANCE.getPajak().toArray());
		}
		
	}
	
	@SuppressWarnings("deprecation")
	private void loadMasaPajakSampai(Date pajakDari)
	{
		Calendar calBegin = Calendar.getInstance();
		calBegin.set(Calendar.DAY_OF_MONTH, 1);
		calBegin.set(Calendar.MONTH, pajakDari.getMonth());
		calBegin.set(Calendar.YEAR, pajakDari.getYear() + 1900);
		calBegin.add(Calendar.MONTH, -1);
		java.util.Date beginDay = calBegin.getTime();
		listMasaPajakSampai = ControllerFactory.getMainController().getCpSptpdDAOImpl().getMasaPeriksaSampai(beginDay);
		
		if (listMasaPajakSampai.size() > 0)
		{
			UIController.INSTANCE.loadMasaPajakPeriksaSampai(cmbSampai, listMasaPajakSampai.toArray()); //PajakProvider.INSTANCE.getPajak().toArray());	
		}
	}
	
	private void createColumns(){
		if(tblDaftarAngsuran.getColumnCount() <= 0){
			String[] listColumn = {"NPWPD", "Nama Badan", "Alamat Badan", "No SKP", "Masa Pajak", 
					"Angsuran Ke", "Jatuh Tempo", "Angsuran Pokok", "Biaya Administrasi Bunga", "Denda SKPDKB", "Denda Keterlambatan", "Jumlah"};
			int[] widthColumn = {110, 170, 160, 130, 190, 110, 120, 140, 160, 130, 140, 130};
			for (int i=listColumn.length - 1;i>=0;i--)
			{
				TableColumn colPajak = new TableColumn(tblDaftarAngsuran, SWT.NONE,0);
				colPajak.setText(listColumn[i]);
				colPajak.setWidth(widthColumn[i]);
			}
		}
		
		if(tblLunas.getColumnCount() <= 0){
			String[] listColumn = {"NPWPD", "Nama Badan", "Alamat Badan", "No SKP", "Masa Pajak", "Angsuran Ke", "Jatuh Tempo", "Angsuran Pokok", 
					"Biaya Administrasi Bunga", "Denda SKPDKB", "Denda Keterlambatan", "Jumlah Bayar", "Tanggal Bayar", "No SSPD"};
			int[] widthColumn = {110, 170, 160, 130, 190, 110, 120, 140, 160, 130, 140, 130, 110, 140};
			for (int i=listColumn.length - 1;i>=0;i--)
			{
				TableColumn colPajak = new TableColumn(tblLunas, SWT.NONE,0);
				colPajak.setText(listColumn[i]);
				colPajak.setWidth(widthColumn[i]);
			}
		}
	}
	
	private void loadDaftarAngsuran(ResultSet result){
		try
		{
			String comparator = "";
			Double total = new Double(0);
			Double grandTotal = new Double(0);
			while (result.next())
			{
				comparator = result.getString("NPWPD");
				TableItem item = new TableItem(tblDaftarAngsuran, SWT.NONE);
				item.setText(0, result.getString("NPWPD"));
				item.setText(1, result.getString("Nama_Badan"));
				item.setText(2, result.getString("Alabad_Jalan"));
				item.setText(3, result.getString("No_SKP"));
				item.setText(4, result.getString("MasaPajak"));
				item.setText(5, result.getString("AngsuranKe"));
				item.setText(6, result.getString("JatuhTempo"));
				item.setText(7, indFormat.format(result.getDouble("Angsuran_Pokok")));
				item.setText(8, indFormat.format(result.getDouble("Biaya_Administrasi")));
				item.setText(9, indFormat.format(result.getDouble("Denda_SKPDKB")));
				item.setText(10, indFormat.format(hitungDenda(Date.valueOf(result.getString("JatuhTempo")), result.getDouble("Angsuran_Pokok"))));
				try {
					item.setText(11, indFormat.format(result.getDouble("Jumlah") + indFormat.parse(item.getText(10)).doubleValue()));
					total += indFormat.parse(item.getText(11)).doubleValue();
				} catch (ParseException e) {
					e.printStackTrace();
				}
				
				if(result.next()){
					if(!comparator.equalsIgnoreCase(result.getString("NPWPD"))){
						TableItem itemTotal = new TableItem(tblDaftarAngsuran, SWT.NONE);
						itemTotal.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
						itemTotal.setBackground(SWTResourceManager.getColor(SWT.COLOR_DARK_GREEN));
						itemTotal.setText(0,"TOTAL");
						itemTotal.setText(11,indFormat.format(total));
						grandTotal += total;
						total = new Double(0);
					}
					result.previous();
				}else{
					TableItem itemTotal = new TableItem(tblDaftarAngsuran, SWT.NONE);
					itemTotal.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
					itemTotal.setBackground(SWTResourceManager.getColor(SWT.COLOR_DARK_GREEN));
					itemTotal.setText(0,"TOTAL");
					itemTotal.setText(11,indFormat.format(total));
					grandTotal += total;
				}
			}
			TableItem itemGT = new TableItem(tblDaftarAngsuran, SWT.NONE);
			itemGT.setBackground(SWTResourceManager.getColor(SWT.COLOR_RED));
			itemGT.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
			itemGT.setText(0, "GRAND TOTAL");
			itemGT.setText(11, indFormat.format(grandTotal));
		} catch (SQLException e) {
				e.printStackTrace();
		}
	}
	
	private void loadDaftarAngsuranLunas(ResultSet result){
		try
		{
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			String comparator = "";
			Double total = new Double(0);
			Double grandTotal = new Double(0);
			while (result.next())
			{
				comparator = result.getString("NPWPD");
				TableItem item = new TableItem(tblLunas, SWT.NONE);
				item.setText(0, result.getString("NPWPD"));
				item.setText(1, result.getString("Nama_Badan"));
				item.setText(2, result.getString("Alabad_Jalan"));
				item.setText(3, result.getString("No_SKP"));
				item.setText(4, result.getString("MasaPajak"));
				item.setText(5, result.getString("AngsuranKe"));
				item.setText(6, result.getString("JatuhTempo"));
				item.setText(7, indFormat.format(result.getDouble("Angsuran_Pokok")));
				item.setText(8, indFormat.format(result.getDouble("Biaya_Administrasi")));
				item.setText(9, indFormat.format(result.getDouble("Denda_SKPDKB")));
				item.setText(10, indFormat.format(hitungDenda(Date.valueOf(result.getString("JatuhTempo")), result.getDouble("Angsuran_Pokok"))));
				try {
					item.setText(11, indFormat.format(result.getDouble("Jumlah") + indFormat.parse(item.getText(10)).doubleValue()));
					item.setText(12, sdf.format(result.getDate("Tgl_SSPD")));
					item.setText(13, result.getString("No_SSPD"));
					total += indFormat.parse(item.getText(11)).doubleValue();
				} catch (ParseException e) {
					e.printStackTrace();
				}
				
				if(result.next()){
					if(!comparator.equalsIgnoreCase(result.getString("NPWPD"))){
						TableItem itemTotal = new TableItem(tblLunas, SWT.NONE);
						itemTotal.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
						itemTotal.setBackground(SWTResourceManager.getColor(SWT.COLOR_DARK_GREEN));
						itemTotal.setText(0,"TOTAL");
						itemTotal.setText(11,indFormat.format(total));
						grandTotal += total;
						total = new Double(0);
					}
					result.previous();
				}else{
					TableItem itemTotal = new TableItem(tblLunas, SWT.NONE);
					itemTotal.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
					itemTotal.setBackground(SWTResourceManager.getColor(SWT.COLOR_DARK_GREEN));
					itemTotal.setText(0,"TOTAL");
					itemTotal.setText(11,indFormat.format(total));
					grandTotal += total;
				}
			}
			TableItem itemGT = new TableItem(tblLunas, SWT.NONE);
			itemGT.setBackground(SWTResourceManager.getColor(SWT.COLOR_RED));
			itemGT.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
			itemGT.setText(0, "GRAND TOTAL");
			itemGT.setText(11, indFormat.format(grandTotal));
		} catch (SQLException e) {
				e.printStackTrace();
		}
	}
	
	@SuppressWarnings("deprecation")
	private double hitungDenda(java.util.Date tglTempo, double pajakTerutang){
		double retValue = 0.0;
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_MONTH, dateNow.getDate());
		cal.set(Calendar.MONTH, dateNow.getMonth());
		cal.set(Calendar.YEAR, dateNow.getYear() + 1900);
		java.util.Date tglSetor = cal.getTime();
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
	}

	@Override
	public void setFocus() {

	}

	private UserModul userModul = ControllerFactory.getMainController().getCpUserDAOImpl()
			.getHMUserModul(GlobalVariable.userModul.getIdUser()).get(24); // 1 merupakan id sub modul.
	private Table tblLunas;
	
	private boolean isPrint(){		
		return userModul.getCetak();
	}
}