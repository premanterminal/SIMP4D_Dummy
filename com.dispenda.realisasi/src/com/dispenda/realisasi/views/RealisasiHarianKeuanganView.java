package com.dispenda.realisasi.views;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
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
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.SWTResourceManager;

import com.dispenda.controller.ControllerFactory;
import com.dispenda.controller.UIController;
import com.dispenda.model.PajakProvider;
import com.dispenda.model.UserModul;
import com.dispenda.object.GlobalVariable;
import com.dispenda.object.Preferences;
import com.dispenda.report.ReportAction;
import com.dispenda.report.ReportAppContext;

import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Spinner;

public class RealisasiHarianKeuanganView extends ViewPart {
	public RealisasiHarianKeuanganView() {
	}
	
	public static final String ID = RealisasiHarianKeuanganView.class.getName();
	private Text txtPokokPajak;
	private Text txtDenda;
	private Text txtTotal;
	private Table tblPendapatan;
	private Color fontColor = new Color(Display.getCurrent(),Preferences.FONT_COLOR);
	private Timestamp dateNow;
	private Locale ind = new Locale("id", "ID");
	private Button btnTahun;
	private Button btnBulan;
	private Button btnTanggal;
	private DateTime calSKP;
	private Spinner yearSKP;
	private DateTime monthSKP;
	private Date masaPajakDari;
	private Date masaPajakSampai;
	private String masaPajak;
	private NumberFormat indFormat = NumberFormat.getCurrencyInstance(ind);
	private Combo cmbKewajibanPajak;
	private UserModul userModul = ControllerFactory.getMainController().getCpUserDAOImpl()
			.getHMUserModul(GlobalVariable.userModul.getIdUser()).get(33);

	@SuppressWarnings("deprecation")
	@Override
	public void createPartControl(Composite parent) {
		Locale.setDefault(new Locale("id","ID"));
		dateNow = (java.sql.Timestamp)ControllerFactory.getMainController().getCpWajibPajakDAOImpl().getDateNow();
		
		parent.setLayout(new GridLayout(1, false));
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(9, false));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Group grpDataLaporan = new Group(composite, SWT.NONE);
		grpDataLaporan.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 8, 1));
		grpDataLaporan.setLayout(new GridLayout(5, false));
		grpDataLaporan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.BOLD));
		grpDataLaporan.addPaintListener(new PaintListener() {
			@Override
			public void paintControl(PaintEvent pe) {
				pe.gc.setForeground(fontColor);
				pe.gc.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.BOLD));
				pe.gc.drawText("Data Laporan Realisasi Penerimaan", 5, 0, false);
			}
		});
		
		Label lblKewajibanPajak = new Label(grpDataLaporan, SWT.NONE);
		lblKewajibanPajak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblKewajibanPajak.setForeground(fontColor);
		lblKewajibanPajak.setText("Kewajiban Pajak");
		
		cmbKewajibanPajak = new Combo(grpDataLaporan, SWT.READ_ONLY);
		cmbKewajibanPajak.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 3, 1));
		lblKewajibanPajak.setForeground(fontColor);
		cmbKewajibanPajak.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		cmbKewajibanPajak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		UIController.INSTANCE.loadPajak(cmbKewajibanPajak, PajakProvider.INSTANCE.getPajak().toArray());
		new Label(grpDataLaporan, SWT.NONE);
		
		Label lblTanggalPenerimaan = new Label(grpDataLaporan, SWT.NONE);
		lblTanggalPenerimaan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblTanggalPenerimaan.setText("Tanggal Penerimaan");
		lblTanggalPenerimaan.setForeground(fontColor);
		
		Group group = new Group(grpDataLaporan, SWT.NONE);
		GridData gd_group = new GridData(SWT.LEFT, SWT.CENTER, false, false, 4, 1);
		gd_group.heightHint = 54;
		group.setLayoutData(gd_group);
		group.setLayout(new GridLayout(3, false));
		
		btnTanggal = new Button(group, SWT.RADIO);
		btnTanggal.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				calSKP.setVisible(true);
				monthSKP.setVisible(false);
				yearSKP.setVisible(false);
				setMasaPajak();
			}
		});
		btnTanggal.setText("Tanggal");
		btnTanggal.setSelection(false);
		btnTanggal.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BORDER));
		btnTanggal.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		btnBulan = new Button(group, SWT.RADIO);
		btnBulan.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				calSKP.setVisible(false);
				monthSKP.setVisible(true);
				yearSKP.setVisible(false);
				setMasaPajak();
			}
		});
		btnBulan.setText("Bulan");
		btnBulan.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BORDER));
		btnBulan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		btnTahun = new Button(group, SWT.RADIO);
		btnTahun.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				calSKP.setVisible(false);
				monthSKP.setVisible(false);
				yearSKP.setVisible(true);
				setMasaPajak();
			}
		});
		btnTahun.setText("Tahun");
		btnTahun.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BORDER));
		btnTahun.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		Composite composite_1 = new Composite(group, SWT.NONE);
		composite_1.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 3, 1));
		composite_1.setLayout(new StackLayout());
		
		calSKP = new DateTime(composite_1, SWT.BORDER | SWT.DROP_DOWN);
		calSKP.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		calSKP.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setMasaPajak();
			}
		});
		calSKP.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BORDER));
		calSKP.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		yearSKP = new Spinner(composite_1, SWT.BORDER);
		yearSKP.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		yearSKP.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setMasaPajak();
			}
		});
		yearSKP.setTextLimit(9999);
		yearSKP.setMaximum(dateNow.getYear() + 1900);
		yearSKP.setMinimum(2006);
		yearSKP.setSelection(dateNow.getYear() + 1900);
		yearSKP.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		yearSKP.setVisible(false);
		yearSKP.setValues(dateNow.getYear() + 1900, 2006, dateNow.getYear() + 1900, 0, 1, 10);
		yearSKP.pack(true);
		
		monthSKP = new DateTime(composite_1, SWT.BORDER | SWT.DROP_DOWN | SWT.SHORT);
		monthSKP.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		monthSKP.setDay(1);
		monthSKP.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setMasaPajak();
			}
		});
		monthSKP.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		monthSKP.setVisible(false);
		new Label(group, SWT.NONE);
		new Label(group, SWT.NONE);
		new Label(group, SWT.NONE);
		new Label(grpDataLaporan, SWT.NONE);
		
		Button btnTampil = new Button(grpDataLaporan, SWT.NONE);
		btnTampil.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ResultSet result = null;
				tblPendapatan.removeAll();
				if (cmbKewajibanPajak.getText().equalsIgnoreCase(""))
					result = ControllerFactory.getMainController().getCpSspdDAOImpl().getDaftarRealisasiHarian("", masaPajakDari, masaPajakSampai);
				else
					result = ControllerFactory.getMainController().getCpSspdDAOImpl().getDaftarRealisasiHarian(cmbKewajibanPajak.getData(Integer.toString(cmbKewajibanPajak.getSelectionIndex())).toString(), masaPajakDari, masaPajakSampai);
				createColumn();
				loadTableSSPD(result);
			}
		});
		GridData gd_btnTampil = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_btnTampil.widthHint = 90;
		btnTampil.setLayoutData(gd_btnTampil);
		btnTampil.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnTampil.setText("Tampil");
		
		Button btnBaru = new Button(grpDataLaporan, SWT.NONE);
		btnBaru.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				tblPendapatan.removeAll();
				txtPokokPajak.setText("");
				txtDenda.setText("");
				txtTotal.setText("");
				cmbKewajibanPajak.deselectAll();
			}
		});
		GridData gd_btnBaru = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnBaru.widthHint = 90;
		btnBaru.setLayoutData(gd_btnBaru);
		btnBaru.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnBaru.setText("Baru");
		
		Button btnCetak = new Button(grpDataLaporan, SWT.NONE);
		btnCetak.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(isPrint()){
					ReportAppContext.name = "Variable";
					ReportAppContext.classLoader = RealisasiHarianKeuanganView.class.getClassLoader();
					if(!ReportAppContext.object.isEmpty())
						ReportAppContext.object.clear();
					SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
					Date tgl = new Date(calSKP.getYear() - 1900, calSKP.getMonth(), calSKP.getDay());
					Integer count = 0;
					if (!cmbKewajibanPajak.getText().equalsIgnoreCase("")){
						for(int ti=0;ti<tblPendapatan.getItems().length; ti++){
							List<String> values = new ArrayList<String>();
							values.add(tblPendapatan.getItem(ti).getText(3));
							values.add(tblPendapatan.getItem(ti).getText(9).substring(2).replace(".", "").replace(",", "."));
							values.add(tblPendapatan.getItem(ti).getText(10).substring(2).replace(".", "").replace(",", "."));
							values.add(tblPendapatan.getItem(ti).getText(6));
							ReportAppContext.object.put(count.toString(), values);
							count++;
						}
						ReportAppContext.map.put("NamaPajak", cmbKewajibanPajak.getText());
						ReportAppContext.map.put("Total", "Pokok + Denda = " + txtTotal.getText());
						ReportAppContext.map.put("Header", "Pokok Pajak");
					}
					else{
						for(int ti=0;ti<tblPendapatan.getItems().length; ti++){
							List<String> values = new ArrayList<String>();
							values.add(tblPendapatan.getItem(ti).getText(3));
							values.add(tblPendapatan.getItem(ti).getText(11).substring(2).replace(".", "").replace(",", "."));
							values.add(tblPendapatan.getItem(ti).getText(10).substring(2).replace(".", "").replace(",", "."));
							values.add(tblPendapatan.getItem(ti).getText(6));
							ReportAppContext.object.put(count.toString(), values);
							count++;
						}
						ReportAppContext.map.put("NamaPajak", "Seluruh Kode Pajak");
						ReportAppContext.map.put("Total", "");
						ReportAppContext.map.put("Header", "Jumlah Bayar");
					}
					ReportAppContext.map.put("Tanggal", sdf.format(tgl));
					ReportAppContext.nameObject = "Realisasi";
					ReportAction.start("RealisasiHarian.rptdesign");
				}else
					MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Anda tidak memiliki hak untuk mencetak.");
			}
		});
		GridData gd_btnCetak = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnCetak.widthHint = 90;
		btnCetak.setLayoutData(gd_btnCetak);
		btnCetak.setText("Cetak");
		btnCetak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		Button btnCetakDaftar = new Button(grpDataLaporan, SWT.NONE);
		btnCetakDaftar.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(isPrint()){
					String bulan = "";
					String masa = "";
					ReportAppContext.name = "Variable";
					ReportAppContext.nameObject = "Realisasi";
					ReportAppContext.classLoader = RealisasiHarianKeuanganView.class.getClassLoader();
					SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
					Date tgl = new Date(calSKP.getYear() - 1900, calSKP.getMonth(), calSKP.getDay());
					if (btnTanggal.getSelection())
						bulan = "Tanggal";
					else if (btnBulan.getSelection())
						bulan = "Bulan";
					else if (btnTahun.getSelection())
						bulan = "Tahun";
					
					if (bulan == "Tanggal")
						masa = sdf.format(tgl);
					else if (bulan == "Bulan")
						masa = UIController.INSTANCE.formatMonth(monthSKP.getMonth() + 1, Locale.getDefault()) + " " + monthSKP.getYear();
					else if (bulan == "Tahun")
						masa = String.valueOf(yearSKP.getText());
					
					if(!ReportAppContext.object.isEmpty())
						ReportAppContext.object.clear();
//					Integer count = 0;
					for(int j=0;j<tblPendapatan.getItems().length; j++){
						List<String> values = new ArrayList<String>();
						for(int i=0;i<tblPendapatan.getColumnCount();i++){// untuk masukkan data berdasarkan colom table
							if(i==9||i==10||i==11||i==14||i==15||i==16)// untuk colom yang berhubungan dengan currency
								if(tblPendapatan.getItem(j).getText(i).length()>2)
									values.add(tblPendapatan.getItem(j).getText(i).substring(2).replace(".", "").replace(",", "."));
								else
									values.add(tblPendapatan.getItem(j).getText(i));
							else
								values.add(tblPendapatan.getItem(j).getText(i));
						}
						ReportAppContext.object.put(Integer.toString(j), values);
//						count++;
					}
					if (!cmbKewajibanPajak.getText().equalsIgnoreCase(""))
						ReportAppContext.map.put("Pajak", cmbKewajibanPajak.getText());	
					else
						ReportAppContext.map.put("Pajak", "Daerah");
					ReportAppContext.map.put("Bulan", bulan);
					ReportAppContext.map.put("Masa", masa);
					
					ReportAction.start("RealisasiHarianLaporanKeuangan.rptdesign");
				}else
					MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Anda tidak memiliki hak untuk mencetak.");
			}
		});
		GridData gd_btnCetakDaftar = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnCetakDaftar.widthHint = 90;
		btnCetakDaftar.setLayoutData(gd_btnCetakDaftar);
		btnCetakDaftar.setText(" Cetak Daftar");
		btnCetakDaftar.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		new Label(composite, SWT.NONE);
		
		Group grpPendapatan = new Group(composite, SWT.NONE);
		grpPendapatan.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 9, 2));
		grpPendapatan.setLayout(new GridLayout(1, false));
		grpPendapatan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.BOLD));
		
		tblPendapatan = new Table(grpPendapatan, SWT.BORDER | SWT.FULL_SELECTION | SWT.VIRTUAL);
		tblPendapatan.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		tblPendapatan.setForeground(SWTResourceManager.getColor(0, 0, 0));
		tblPendapatan.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		tblPendapatan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE-1, SWT.NORMAL));
		tblPendapatan.setHeaderVisible(true);
		tblPendapatan.setLinesVisible(true);
	
		Label lblPokokPajak = new Label(composite, SWT.NONE);
		lblPokokPajak.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPokokPajak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblPokokPajak.setForeground(fontColor);
		lblPokokPajak.setText("Pokok Pajak");
		
		txtPokokPajak = new Text(composite, SWT.BORDER);
		txtPokokPajak.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		txtPokokPajak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		GridData gd_txtPokokPajak = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_txtPokokPajak.widthHint = 150;
		txtPokokPajak.setLayoutData(gd_txtPokokPajak);
//		txtPokokPajak.setForeground(fontColor);
		txtPokokPajak.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		
		Label lblDenda = new Label(composite, SWT.NONE);
		lblDenda.setForeground(fontColor);
		lblDenda.setText("+");
		
		Label lblDenda_1 = new Label(composite, SWT.NONE);
		lblDenda_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblDenda_1.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblDenda_1.setForeground(fontColor);
		lblDenda_1.setText("Denda");
		
		txtDenda = new Text(composite, SWT.BORDER);
		txtDenda.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		txtDenda.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		GridData gd_txtDenda = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_txtDenda.widthHint = 150;
		txtDenda.setLayoutData(gd_txtDenda);
//		txtDenda.setForeground(fontColor);
		txtDenda.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		
		
		Label label = new Label(composite, SWT.NONE);
		label.setForeground(fontColor);
		label.setText("=");
		
		Label lblTotal = new Label(composite, SWT.NONE);
		lblTotal.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblTotal.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblTotal.setForeground(fontColor);
		txtDenda.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		lblTotal.setText("Total");
		
		txtTotal = new Text(composite, SWT.BORDER);
		txtTotal.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		txtTotal.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		GridData gd_txtTotal = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_txtTotal.widthHint = 150;
		txtTotal.setLayoutData(gd_txtTotal);
//		txtTotal.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		txtTotal.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		new Label(composite, SWT.NONE);
		
//		Label label_2 = new Label(composite, SWT.NONE);

	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}
	
	@SuppressWarnings("deprecation")
	public void setMasaPajak()
	{
		if (btnTanggal.getSelection())
		{
			Calendar instance = Calendar.getInstance();
			instance.set(Calendar.DAY_OF_MONTH, calSKP.getDay());
			instance.set(Calendar.MONTH, calSKP.getMonth());
			instance.set(Calendar.YEAR, calSKP.getYear());
			masaPajakDari = new Date(calSKP.getYear() - 1900, calSKP.getMonth(), calSKP.getDay());
			masaPajakSampai = masaPajakDari;
			masaPajak = masaPajakDari.getDate() + " " + UIController.INSTANCE.formatMonth(masaPajakDari.getMonth() + 1, Locale.getDefault()) + " " + (masaPajakDari.getYear() + 1900);
		}
		if (btnBulan.getSelection())
		{
			Calendar instance = Calendar.getInstance();
			instance.set(Calendar.DAY_OF_MONTH, monthSKP.getDay());
			instance.set(Calendar.MONTH, monthSKP.getMonth());
			instance.set(Calendar.YEAR, monthSKP.getYear());
			masaPajakDari = new Date(monthSKP.getYear() - 1900, monthSKP.getMonth(), 1);
			masaPajakSampai = new Date(monthSKP.getYear() - 1900, monthSKP.getMonth(), instance.getActualMaximum(Calendar.DATE));
			masaPajak = UIController.INSTANCE.formatMonth(monthSKP.getMonth() + 1, Locale.getDefault()) + " " + monthSKP.getYear();
		}
		if (btnTahun.getSelection())
		{
			masaPajakDari = new Date(yearSKP.getSelection() - 1900, 0, 1);
			masaPajakSampai = new Date(yearSKP.getSelection() - 1900, 11, 31);
			masaPajak = Integer.toString(yearSKP.getSelection());
		}
	
	}
	
	private void createColumn()
	{
		if(tblPendapatan.getColumnCount() <= 0){
//			String[] listColumn = {"No Reg", "NPWPD", "Nama Badan", "Bidang Usaha", "No SKP", "No SSPD", "Bulan SKP", "Cicilan Ke", "Pokok Pajak", 
//					"Denda", "Jumlah Bayar"};
//			int[] widthColumn = {60, 100, 200, 160, 130, 130, 85, 85, 150, 130, 150};
			String[] listColumn = {"Bidang Usaha", "Nama Badan", "Alamat", "NPWPD", "No SKP", "No SSPD", "No Reg", "Tgl Cetak", "Masa Pajak", "Pokok Pajak", 
					"Denda", "Jumlah Bayar", "Cicilan Ke","Bulan sd Akhr Tahun","Pajak Per Bulan","Total Pajak sd Akhr Tahun","Sisa Pemb. Dimuka"};
			int[] widthColumn = {160, 200, 160, 100, 130, 130, 70, 90, 200, 150, 130, 150, 85, 150, 150, 150, 150};
			for (int i=listColumn.length - 1;i>=0;i--)
			{
				TableColumn colPajak = new TableColumn(tblPendapatan, SWT.NONE,0);
				colPajak.setText(listColumn[i]);
				colPajak.setWidth(widthColumn[i]);
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	private void loadTableSSPD(final ResultSet result)
	{
		String masaPajak = "";
		double totalPokokPajak = 0;
		double totalDenda = 0;
		double totalJumlahBayar = 0;
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		try
		{
			while (result.next())
			{
				masaPajak = UIController.INSTANCE.formatMonth(result.getDate("PajakDari").getMonth() + 1, Locale.getDefault()) + " " + (result.getDate("PajakDari").getYear()+ 1900);
				if (! (result.getDate("PajakDari").getMonth() == result.getDate("PajakSampai").getMonth() && result.getDate("PajakDari").getYear() == result.getDate("PajakSampai").getYear()))
					masaPajak += " - " + UIController.INSTANCE.formatMonth(result.getDate("PajakSampai").getMonth() + 1, Locale.getDefault()) + " " + (result.getDate("PajakSampai").getYear() + 1900);
				
				TableItem item = new TableItem(tblPendapatan, SWT.NONE);
				if (result.getString("No_Registrasi") == null)
					item.setText(6, "");
				else
					item.setText(6, result.getString("No_Registrasi"));
				item.setText(0, result.getString("Nama_Bid_Usaha"));
				item.setText(1, result.getString("Nama_Badan"));
				item.setText(2, result.getString("Alabad_Jalan"));
				item.setText(3, result.getString("NPWPD"));
				item.setText(4, result.getString("No_SKP"));
				item.setText(5, result.getString("No_SSPD"));
				item.setText(7, formatter.format(result.getDate("Tgl_Cetak")));
				item.setText(8, masaPajak);
				item.setText(9, indFormat.format(round(result.getDouble("PokokPajak"), 2)));
				item.setText(10, indFormat.format(round(result.getDouble("Jumlah_Bayar"), 2) - round(result.getDouble("PokokPajak"), 2)));
				item.setText(11, indFormat.format(round(result.getDouble("Jumlah_Bayar"), 2)));
				item.setText(12, result.getString("Cicilan_Ke"));
				// 	Tambahan utk Reklame Keuangan
				
				Date dtMasaPajakDari = result.getDate("PajakDari");
				int numMonth = 0;
				Integer difMonth = 0;
				Double pajakPerBulan = new Double(0.0);
				Double totalSdDes = new Double(0.0);
				Double sisaPembDimuka = new Double(0.0);
				if((dtMasaPajakDari.getYear()+1900)==2014){
					numMonth = dtMasaPajakDari.getMonth()+1;
					difMonth = 12 - numMonth;
					pajakPerBulan = result.getDouble("PokokPajak")/12;
					totalSdDes = pajakPerBulan * difMonth;
					sisaPembDimuka = result.getDouble("PokokPajak")-totalSdDes;
				}else if((dtMasaPajakDari.getYear()+1900)==2015){
					numMonth = dtMasaPajakDari.getMonth()+1;
					difMonth = 12 - numMonth;
					pajakPerBulan = result.getDouble("PokokPajak")/12;
					totalSdDes = pajakPerBulan * difMonth;
					sisaPembDimuka = result.getDouble("PokokPajak")-totalSdDes;
				}
				item.setText(13, difMonth.toString());
				item.setText(14, indFormat.format(round(pajakPerBulan, 2)));
				item.setText(15, indFormat.format(round(totalSdDes, 2)));
				item.setText(16, indFormat.format(round(sisaPembDimuka, 2)));
//				item.setText(1, result.getString("NPWPD"));
//				item.setText(2, result.getString("Nama_Badan"));
//				item.setText(3, result.getString("Nama_Bid_Usaha"));
//				item.setText(4, result.getString("No_SKP"));
//				item.setText(5, result.getString("No_SSPD"));
//				item.setText(6, result.getString("Bulan_SKP"));
//				item.setText(7, result.getString("Cicilan_Ke"));
//				item.setText(8, indFormat.format(round(result.getDouble("PokokPajak"), 2)));
//				item.setText(9, indFormat.format(round(result.getDouble("Jumlah_Bayar"), 2) - round(result.getDouble("PokokPajak"), 2)));
//				item.setText(10, indFormat.format(round(result.getDouble("Jumlah_Bayar"), 2)));
				try {
					totalPokokPajak += round(indFormat.parse(item.getText(9)).doubleValue(), 2);
					totalDenda += round(indFormat.parse(item.getText(10)).doubleValue(), 2);
					totalJumlahBayar += round(indFormat.parse(item.getText(11)).doubleValue(), 2);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			TableItem item = new TableItem(tblPendapatan, SWT.NONE);
			item.setText(0, "Total");
			item.setText(9, indFormat.format(totalPokokPajak));
			item.setText(10, indFormat.format(totalDenda));
			item.setText(11, indFormat.format(totalJumlahBayar));
			txtPokokPajak.setText(indFormat.format(round(totalPokokPajak, 2)));
			txtDenda.setText(indFormat.format(round(totalDenda, 2)));
			txtTotal.setText(indFormat.format(round(totalJumlahBayar, 2)));
		} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		}
	}
	
	private boolean isPrint(){		
		return userModul.getCetak();
	}
	
	private static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

//	    BigDecimal bd = new BigDecimal(value);
//	    bd = bd.setScale(places, RoundingMode.HALF_DOWN);
	    
	    DecimalFormat df = new DecimalFormat("#.##");
		df.setMaximumFractionDigits(2);
		df.setRoundingMode(RoundingMode.HALF_EVEN);
	    try {
			return df.parse(df.format(value)).doubleValue();
		} catch (ParseException e) {
			e.printStackTrace();
			return 0.0;
		}
	}

}
