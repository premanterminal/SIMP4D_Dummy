package com.dispenda.sptpd.views;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.SWTResourceManager;

import com.dispenda.controller.ControllerFactory;
import com.dispenda.controller.UIController;
import com.dispenda.model.PajakProvider;
import com.dispenda.model.Pejabat;
import com.dispenda.object.Preferences;
import com.dispenda.report.ReportAction;
import com.dispenda.report.ReportAppContext;
import com.dispenda.report.ReportModule;

import org.eclipse.swt.custom.StackLayout;

public class DaftarSKPDKeuanganView extends ViewPart {
	public DaftarSKPDKeuanganView() {
	}
	
	public static final String ID = DaftarSKPDKeuanganView.class.getName();
	private Button btnTanggal;
	private DateTime calSPTPD;
	private Combo cmbPajak;
	private Date masaPajakDari;
	private Date masaPajakSampai;
	private Button btnBulan;
	private Button btnTahun;
	private Table tbl_SKPD;
	private Locale ind = new Locale("id", "ID");
	private NumberFormat indFormat = NumberFormat.getCurrencyInstance(ind);
	private Group composite;
	private DateTime monthSPT;
	private Timestamp dateNow;
	private Spinner yearSPT;
	private Composite compAbsolute;
	private String masaPajak;
	private Color fontColor = new Color(Display.getCurrent(),Preferences.FONT_COLOR);
	private Combo cmbPejabat;
	private Composite composite_1;
	private Button btnCetakBlanko;
	private Button btnCetakDaftar;
	private Label lblPenandaTangan;
	private List<Pejabat> listPejabat;

	@SuppressWarnings("deprecation")
	@Override
	public void createPartControl(Composite parent) {
		Locale.setDefault(new Locale("id","ID"));
		dateNow = (java.sql.Timestamp)ControllerFactory.getMainController().getCpWajibPajakDAOImpl().getDateNow();
		
		ScrolledComposite scrolledComposite = new ScrolledComposite(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		
		Composite comp = new Composite(scrolledComposite, SWT.NONE);
		comp.setLayout(new GridLayout(4, false));
		
		Label lblKewajibanPajak = new Label(comp, SWT.NONE);
		lblKewajibanPajak.setForeground(fontColor);
		lblKewajibanPajak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblKewajibanPajak.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblKewajibanPajak.setText("Kewajiban Pajak");
		
		cmbPajak = new Combo(comp, SWT.READ_ONLY);
		cmbPajak.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		cmbPajak.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		cmbPajak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		GridData gd_cmbPajak = new GridData(SWT.LEFT, SWT.CENTER, true, false, 3, 1);
		gd_cmbPajak.widthHint = 222;
		UIController.INSTANCE.loadPajak(cmbPajak, PajakProvider.INSTANCE.getPajak().toArray());
		cmbPajak.setLayoutData(gd_cmbPajak);
		
		Label lblTanggalSkpd = new Label(comp, SWT.NONE);
		lblTanggalSkpd.setForeground(fontColor);
		lblTanggalSkpd.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblTanggalSkpd.setText("Tanggal SKPD");
		
		composite = new Group(comp, SWT.NONE);
		composite.setLayout(new GridLayout(3, false));
		composite.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1));
		
		btnTanggal = new Button(composite, SWT.RADIO);
		btnTanggal.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				calSPTPD.setVisible(true);
				calSPTPD.pack(true);
				monthSPT.setVisible(false);
				yearSPT.setVisible(false);
				setMasaPajak();
			}
		});
		btnTanggal.setSelection(false);
		btnTanggal.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BORDER));
		btnTanggal.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnTanggal.setText("Tanggal");
		
		
		btnBulan = new Button(composite, SWT.RADIO);
		btnBulan.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				monthSPT.setVisible(true);
				monthSPT.pack(true);
				calSPTPD.setVisible(false);
				yearSPT.setVisible(false);
				setMasaPajak();
			}
		});
		btnBulan.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BORDER));
		btnBulan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnBulan.setText("Bulan");
		
		
		btnTahun = new Button(composite, SWT.RADIO);
		btnTahun.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				yearSPT.setVisible(true);
				yearSPT.pack(true);
				calSPTPD.setVisible(false);
				monthSPT.setVisible(false);
				setMasaPajak();
			}
		});
		btnTahun.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BORDER));
		btnTahun.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnTahun.setText("Tahun");
		
		
		compAbsolute = new Composite(composite, SWT.NONE);
		compAbsolute.setLayout(new StackLayout());
		compAbsolute.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 3, 1));
		
		calSPTPD = new DateTime(compAbsolute, SWT.BORDER | SWT.DROP_DOWN);
		calSPTPD.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		calSPTPD.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setMasaPajak();
				
			}
		});
		calSPTPD.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BORDER));
		calSPTPD.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		yearSPT = new Spinner(compAbsolute, SWT.BORDER);
		yearSPT.setMaximum(dateNow.getYear() + 1900);
		yearSPT.setMinimum(2006);
		yearSPT.setSelection(dateNow.getYear() + 1900);
		yearSPT.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		yearSPT.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setMasaPajak();
			}
		});
		yearSPT.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		yearSPT.setValues(dateNow.getYear() + 1900, 2006, dateNow.getYear() + 1900, 0, 1, 10);
		yearSPT.setVisible(false);
		
		monthSPT = new DateTime(compAbsolute, SWT.BORDER | SWT.DROP_DOWN | SWT.SHORT);
		monthSPT.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		monthSPT.setDay(1);
		monthSPT.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setMasaPajak();
			}
		});
		monthSPT.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		monthSPT.setVisible(false);
		new Label(comp, SWT.NONE);
		
		Button btnLihat = new Button(comp, SWT.NONE);
		btnLihat.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnLihat.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				/*if (cmbPajak.getSelectionIndex() >= 0)
				{
					try {
						tbl_SKPD.removeAll();
						ResultSet result = ControllerFactory.getMainController().getCpSptpdDAOImpl().getDaftarSKPD(cmbPajak.getData(Integer.toString(cmbPajak.getSelectionIndex())).toString(), masaPajakDari, masaPajakSampai);
						createColumn();
						loadTableSPTPD(result);
					} catch (ParseException e1) {
						e1.printStackTrace();
					}
				}*/
			}
		});
		GridData gd_btnLihat = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnLihat.widthHint = 90;
		btnLihat.setLayoutData(gd_btnLihat);
		btnLihat.setText("Lihat");
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		
		lblPenandaTangan = new Label(comp, SWT.NONE);
		lblPenandaTangan.setForeground(fontColor);
		lblPenandaTangan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblPenandaTangan.setText("Penanda Tangan");
		
		cmbPejabat = new Combo(comp, SWT.READ_ONLY);
		GridData gd_cmbPejabat = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_cmbPejabat.widthHint = 203;
		cmbPejabat.setLayoutData(gd_cmbPejabat);
		cmbPejabat.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_FOREGROUND));
		cmbPejabat.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		cmbPejabat.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		loadPejabat();
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		
		composite_1 = new Composite(comp, SWT.NONE);
		composite_1.setLayout(new GridLayout(2, false));
		
		btnCetakBlanko = new Button(composite_1, SWT.NONE);
		btnCetakBlanko.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (cmbPejabat.getText().equalsIgnoreCase("")){
					MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Silahkan isi nama pejabat");
				}
				else
				{
					ReportAppContext.nameObject = "BlankoSKPD";
					ReportAppContext.classLoader = DaftarSKPDKeuanganView.class.getClassLoader();
					if(!ReportAppContext.object.isEmpty())
						ReportAppContext.object.clear();
					for(int j = 0;j<tbl_SKPD.getItems().length - 1;j++){// untuk masukan row table
						List<String> values = new ArrayList<String>();
						for(int i=0;i<tbl_SKPD.getColumnCount();i++){// untuk masukkan data berdasarkan colom table
							if (i == 6)
								values.add(getNPWPDFormat(tbl_SKPD.getItem(j).getText(i)));
							else
								values.add(tbl_SKPD.getItem(j).getText(i));
						}
						values.add(listPejabat.get(cmbPejabat.getSelectionIndex()).getNamaPejabat());
						values.add(listPejabat.get(cmbPejabat.getSelectionIndex()).getPangkat());
						values.add(listPejabat.get(cmbPejabat.getSelectionIndex()).getIdPejabatNIP());
						values.add(getNoUrutFormat(tbl_SKPD.getItem(j).getText(1)));
						try {
							values.add(new ReportModule().getTerbilang(indFormat.parse(tbl_SKPD.getItem(j).getText(14)).doubleValue()));
						} catch (ParseException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						ReportAppContext.object.put(Integer.toString(j), values);
					}
					ReportAction.start("BlankoSKPD.rptdesign");
				}
				
			}
		});
		GridData gd_btnCetakBlanko = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnCetakBlanko.widthHint = 140;
		btnCetakBlanko.setLayoutData(gd_btnCetakBlanko);
		btnCetakBlanko.setText("Cetak Blanko");
		btnCetakBlanko.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BORDER));
		btnCetakBlanko.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		btnCetakDaftar = new Button(composite_1, SWT.NONE);
		btnCetakDaftar.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
				ReportAppContext.name = "Variable";
				if (masaPajakSampai.compareTo(masaPajakDari) == 0){
					ReportAppContext.map.put("MasaPajak", df.format(masaPajakDari));
				}
				else{
					ReportAppContext.map.put("MasaPajak", df.format(masaPajakDari) + " s/d " + df.format(masaPajakSampai));
				}
//				ReportAppContext.map.put("TotalPajak", value);
				ReportAppContext.nameObject = "DaftarSKPD_tabel";
				ReportAppContext.classLoader = DaftarSKPDKeuanganView.class.getClassLoader();
				if(!ReportAppContext.object.isEmpty())
					ReportAppContext.object.clear();
				for(int j = 0;j<tbl_SKPD.getItems().length-1;j++){// untuk masukan row table
					List<String> values = new ArrayList<String>();
					values.add(String.valueOf(j + 1));
					values.add(tbl_SKPD.getItem(j).getText(1));
					values.add(tbl_SKPD.getItem(j).getText(6));
					values.add(tbl_SKPD.getItem(j).getText(4));
					values.add(tbl_SKPD.getItem(j).getText(5));
					values.add(tbl_SKPD.getItem(j).getText(8));
					values.add(tbl_SKPD.getItem(j).getText(19));
					values.add(tbl_SKPD.getItem(j).getText(14).substring(2).replace(".", "").replace(",", "."));
					values.add(tbl_SKPD.getItem(j).getText(20));
					values.add(tbl_SKPD.getItem(j).getText(2));
										
					ReportAppContext.object.put(Integer.toString(j), values);
				}
				
				ReportAction.start("DaftarSKPD.rptdesign");
			}
		});
		GridData gd_btnCetakDaftar = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnCetakDaftar.widthHint = 140;
		btnCetakDaftar.setLayoutData(gd_btnCetakDaftar);
		btnCetakDaftar.setText("Cetak Daftar");
		btnCetakDaftar.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BORDER));
		btnCetakDaftar.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		
		tbl_SKPD = new Table(comp, SWT.BORDER | SWT.FULL_SELECTION | SWT.VIRTUAL);
		tbl_SKPD.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		tbl_SKPD.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 4, 1));
		tbl_SKPD.setForeground(SWTResourceManager.getColor(0, 0, 0));
		tbl_SKPD.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE-1, SWT.NORMAL));
		tbl_SKPD.setHeaderVisible(true);
		tbl_SKPD.setLinesVisible(true);

		scrolledComposite.setContent(comp);
		scrolledComposite.setMinSize(comp.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}
	
	@SuppressWarnings("deprecation")
	public void setMasaPajak()
	{
		
		if (btnTanggal.getSelection())
		{
			Calendar instance = Calendar.getInstance();
			instance.set(Calendar.DAY_OF_MONTH, calSPTPD.getDay());
			instance.set(Calendar.MONTH, calSPTPD.getMonth());
			instance.set(Calendar.YEAR, calSPTPD.getYear());
			masaPajakDari = new Date(calSPTPD.getYear() - 1900, calSPTPD.getMonth(), calSPTPD.getDay());
			masaPajakSampai = masaPajakDari;
			masaPajak = masaPajakDari.getDate() + " " + UIController.INSTANCE.formatMonth(masaPajakDari.getMonth() + 1, Locale.getDefault()) + " " + (masaPajakDari.getYear() + 1900);
		}
		if (btnBulan.getSelection())
		{
			Calendar instance = Calendar.getInstance();
			instance.set(Calendar.DAY_OF_MONTH, monthSPT.getDay());
			instance.set(Calendar.MONTH, monthSPT.getMonth());
			instance.set(Calendar.YEAR, monthSPT.getYear());
			masaPajakDari = new Date(monthSPT.getYear() - 1900, monthSPT.getMonth(), 1);
			masaPajakSampai = new Date(monthSPT.getYear() - 1900, monthSPT.getMonth(), instance.getActualMaximum(Calendar.DATE));
			masaPajak = UIController.INSTANCE.formatMonth(monthSPT.getMonth() + 1, Locale.getDefault()) + " " + monthSPT.getYear();
		}
		if (btnTahun.getSelection())
		{
			masaPajakDari = new Date(yearSPT.getSelection() - 1900, 0, 1);
			masaPajakSampai = new Date(yearSPT.getSelection() - 1900, 11, 31);
			masaPajak = Integer.toString(yearSPT.getSelection());
		}
	
	}
	
	private void createColumn()
	{
		if(tbl_SKPD.getColumnCount() <= 0){
			String[] listColumn = {"No.","No SPTPD", "Tanggal SPTPD", "Tahun", "Nama Badan", "Alamat Badan", "NPWPD", "Kode Bidang Usaha", "Nama Pajak", "Dasar Pengenaan", "Pajak Terutang",
					"Bunga", "Kenaikan", "Denda", "Total", "Id Sub Pajak", "Masa Pajak Dari", "Masa Pajak Sampai", "Jatuh Tempo", "Masa Pajak", "Lokasi","Bulan sd Des 15","Pajak Per Bulan","Total Pajak sd Des 15"};
			for (int i=listColumn.length - 1;i>=0;i--)
			{
				TableColumn colPajak = new TableColumn(tbl_SKPD, SWT.NONE,0);
				colPajak.setText(listColumn[i]);
				if (i==15)
					colPajak.setWidth(0);
				else if(i > 0)
					colPajak.setWidth(120);
				else
					colPajak.setWidth(50);
			}
		}
	}
	
	private void loadTableSPTPD(final ResultSet result)
	{
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		String masaPajak = "";
		String temp[];
		double grandTotal = 0;
		int count = 1;
		try
		{
			while (result.next())
			{
				if (result.getString("MasaPajak").contains(" - ")){
					temp = result.getString("MasaPajak").split("-");
//					tempDate[0] = Date.parse(temp[0]);
					masaPajak = temp[2] + " " + UIController.INSTANCE.formatMonth(Integer.parseInt(temp[1]), Locale.getDefault()).substring(0, 3) + " " + temp[0] + " - ";
					masaPajak += temp[5] + " " + UIController.INSTANCE.formatMonth(Integer.parseInt(temp[4]), Locale.getDefault()).substring(0, 3) + " " + temp[3];
				}
				else{
					temp = result.getString("MasaPajak").split("-");
					masaPajak = UIController.INSTANCE.formatMonth(Integer.parseInt(temp[1]), Locale.getDefault()) + " " + temp[0];
				}
				TableItem item = new TableItem(tbl_SKPD, SWT.NONE);
				item.setText(0, count+".");
				item.setText(1, result.getString("No_SPTPD"));
				item.setText(2, formatter.format(result.getDate("Tgl_SPTPD")));
				item.setText(3, result.getString("Tahun"));
				item.setText(4, result.getString("Nama_Badan"));
				item.setText(5, result.getString("Alabad_Jalan"));
				item.setText(6, result.getString("NPWPD"));
				item.setText(7, result.getString("Kode_Bid_Usaha"));
				item.setText(8, result.getString("NamaPajak"));
				item.setText(9, indFormat.format(result.getDouble("Dasar_Pengenaan"))); 
				item.setText(10, indFormat.format(result.getDouble("Pajak_Terutang")));
				item.setText(11, indFormat.format(result.getDouble("Bunga")));
				item.setText(12, indFormat.format(result.getDouble("Kenaikan")));
				item.setText(13, indFormat.format(result.getDouble("Denda")));
				item.setText(14, indFormat.format(result.getDouble("Total")));
				item.setText(15, result.getString("IdSub_Pajak"));
				item.setText(16, formatter.format(result.getDate("Masa_pajak_Dari")));
				item.setText(17, formatter.format(result.getDate("Masa_Pajak_Sampai")));
				item.setText(18, formatter.format(result.getDate("TanggalJatuhTempo")));
				item.setText(19, masaPajak);
				item.setText(20, result.getString("Lokasi"));
				// Tambahan utk Reklame Keuangan
				
				Date dtMasaPajakDari = result.getDate("Masa_pajak_Dari");
				int numMonth = 0;
				Integer difMonth = 0;
				Double pajakPerBulan = new Double(0.0);
				Double totalSdDes = new Double(0.0);
				Double sisaPembDimuka = new Double(0.0);
				if((dtMasaPajakDari.getYear()+1900)==2014){
					
				}else if((dtMasaPajakDari.getYear()+1900)==2015){
					numMonth = dtMasaPajakDari.getMonth()+1;
					difMonth = 12 - numMonth;
					if(dtMasaPajakDari.getDate()==1)
						difMonth += 1;
					pajakPerBulan = result.getDouble("Pajak_Terutang")/12;
					totalSdDes = pajakPerBulan * difMonth;
					sisaPembDimuka = result.getDouble("Pajak_Terutang")-totalSdDes;
				}
				item.setText(21, difMonth.toString());
				item.setText(22, indFormat.format(pajakPerBulan));
				item.setText(23, indFormat.format(totalSdDes));
				
				
				try {
					grandTotal += indFormat.parse(item.getText(14)).doubleValue();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				count++;
			}
			TableItem item = new TableItem(tbl_SKPD, SWT.NONE);
			item.setText(1, "Grand Total");
			item.setText(14, indFormat.format(grandTotal));
		} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		}
	}
	
	private void loadPejabat(){
		listPejabat = ControllerFactory.getMainController().getCpPejabatDAOImpl().getAllPejabat();
		for (int i=0;i<listPejabat.size();i++){
			UIController.INSTANCE.loadPejabat(cmbPejabat, listPejabat.toArray());
		}
	}
	
	private String getNoUrutFormat(String noSKP){
		int idx = noSKP.indexOf("/");
		String noUrut = noSKP.substring(0, idx);
		noUrut = ("000000" + noUrut).substring(noUrut.length());
		String retValue = "";
		for (int i=0;i<noUrut.length();i++) {
			retValue += noUrut.substring(i, i+1) + "   ";
		}
		
		return retValue;
	}
	
//	private String GetNoRekFormat(String NoRek){
//		String noRekening = NoRek.replace(".", "");
//		String retValue = "";
//		for (int i=0;i<noRekening.length();i++) {
//			retValue += noRekening.substring(i, i+1) + "   ";
//		}
//		
//		return retValue;
//	}
	
	private String getNPWPDFormat(String npwpd){
		String retValue = "";
		for (int i=0;i<npwpd.length();i++){
			if (i == 0 || i == 7 || i == 9)
				retValue += npwpd.substring(i, i+ 1) + "      ";
			else
				retValue += npwpd.substring(i, i+ 1) + "   ";
		}
		
		return retValue;
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}
}