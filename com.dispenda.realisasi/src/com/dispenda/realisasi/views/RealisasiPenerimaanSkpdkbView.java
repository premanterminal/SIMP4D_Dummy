package com.dispenda.realisasi.views;

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
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
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
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class RealisasiPenerimaanSkpdkbView extends ViewPart {
	public RealisasiPenerimaanSkpdkbView() {
	}

	public static final String ID = RealisasiPenerimaanSkpdkbView.class.getName();
	private Text txtPokokPajak;
	private Text txtDenda;
	private Text txtTotal;
	private Table tblPendapatan;
	private Text txtRealisasi;
	private Color fontColor = new Color(Display.getCurrent(),Preferences.FONT_COLOR);
	private Combo cmbKewajibanPajak;
	private DateTime calSKP;
	private Spinner yearSKP;
	private DateTime monthSKP;
	private Timestamp dateNow;
	private Locale ind = new Locale("id", "ID");
	private Button btnTanggal;
	private Button btnBulan;
	private Button btnTahun;
	private Date masaPajakDari;
	private Date masaPajakSampai;
	private Date realisasi;
//	private String masaPajak;
	private NumberFormat indFormat = NumberFormat.getCurrencyInstance(ind);
	private UserModul userModul = ControllerFactory.getMainController().getCpUserDAOImpl()
			.getHMUserModul(GlobalVariable.userModul.getIdUser()).get(30);
	private Text txtDendaAngsuran;
	private DateTime dtRealisasi;
	private Combo cmbTanggal;
	private Text txtDendaSSPD;
	
	@SuppressWarnings("deprecation")
	@Override
	public void createPartControl(Composite parent) {
		Locale.setDefault(new Locale("id","ID"));
		dateNow = (java.sql.Timestamp)ControllerFactory.getMainController().getCpWajibPajakDAOImpl().getDateNow();
		parent.setLayout(new GridLayout(1, false));
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(14, false));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Group grpDataLaporan = new Group(composite, SWT.NONE);
		grpDataLaporan.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 14, 1));
		grpDataLaporan.setLayout(new GridLayout(6, false));
		grpDataLaporan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.BOLD));
		grpDataLaporan.addPaintListener(new PaintListener() {
			@Override
			public void paintControl(PaintEvent pe) {
				pe.gc.setForeground(fontColor);
				pe.gc.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.BOLD));
				pe.gc.drawText("Data Laporan Realisasi Penerimaan SKPDKB", 5, 0, false);
			}
		});
		
		Label lblKewajibanPajak = new Label(grpDataLaporan, SWT.NONE);
		lblKewajibanPajak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblKewajibanPajak.setForeground(fontColor);
		lblKewajibanPajak.setText("Kewajiban Pajak");
		
		cmbKewajibanPajak = new Combo(grpDataLaporan, SWT.READ_ONLY);
		cmbKewajibanPajak.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 3, 1));
		cmbKewajibanPajak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		cmbKewajibanPajak.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		UIController.INSTANCE.loadPajak(cmbKewajibanPajak, PajakProvider.INSTANCE.getPajak().toArray());
		new Label(grpDataLaporan, SWT.NONE);
		new Label(grpDataLaporan, SWT.NONE);
		
		Label lblLihatBerdasarkan = new Label(grpDataLaporan, SWT.NONE);
		lblLihatBerdasarkan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblLihatBerdasarkan.setText("Lihat Berdasarkan");
		lblLihatBerdasarkan.setForeground(fontColor);
		
		cmbTanggal = new Combo(grpDataLaporan, SWT.READ_ONLY);
		cmbTanggal.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		cmbTanggal.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		cmbTanggal.setItems(new String[] {"Tanggal Ketetapan", "Tanggal Realisasi"});
		cmbTanggal.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));
		cmbTanggal.select(0);
		new Label(grpDataLaporan, SWT.NONE);
		new Label(grpDataLaporan, SWT.NONE);
		new Label(grpDataLaporan, SWT.NONE);
		new Label(grpDataLaporan, SWT.NONE);
		
		Group group = new Group(grpDataLaporan, SWT.NONE);
		GridData gd_group = new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1);
		gd_group.widthHint = 214;
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
		btnTanggal.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnTanggal.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BORDER));
		btnTanggal.setText("Tanggal");
		
				
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
		btnBulan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnBulan.setText("Bulan");
		btnBulan.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BORDER));
				
		
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
		btnTahun.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnTahun.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BORDER));
		btnTahun.setText("Tahun");
		
		Composite composite_1 = new Composite(group, SWT.NONE);
		GridData gd_composite_1 = new GridData(SWT.CENTER, SWT.CENTER, false, false, 3, 1);
		gd_composite_1.widthHint = 138;
		composite_1.setLayoutData(gd_composite_1);
		composite_1.setLayout(new StackLayout());
		
		calSKP = new DateTime(composite_1, SWT.BORDER | SWT.DROP_DOWN);
		calSKP.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setMasaPajak();
			}
		});
		calSKP.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BORDER));
		calSKP.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		calSKP.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		
		yearSKP = new Spinner(composite_1, SWT.BORDER);
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
		yearSKP.setVisible(false);
		yearSKP.setValues(dateNow.getYear() + 1900, 2006, dateNow.getYear() + 1900, 0, 1, 10);
		yearSKP.pack(true);
		yearSKP.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		yearSKP.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		
		monthSKP = new DateTime(composite_1, SWT.BORDER | SWT.DROP_DOWN | SWT.SHORT);
		monthSKP.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setMasaPajak();
			}
		});
		monthSKP.setVisible(false);
		monthSKP.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		monthSKP.setDay(1);
		monthSKP.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		
		Label lblBreakdownRealisasi = new Label(grpDataLaporan, SWT.NONE);
		lblBreakdownRealisasi.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblBreakdownRealisasi.setForeground(fontColor);
		lblBreakdownRealisasi.setText("Breakdown Realisasi");
		
		dtRealisasi = new DateTime(grpDataLaporan, SWT.BORDER | SWT.DROP_DOWN);
		GridData gd_dtRealisasi = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_dtRealisasi.widthHint = 121;
		dtRealisasi.setLayoutData(gd_dtRealisasi);
		dtRealisasi.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BORDER));
		dtRealisasi.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		dtRealisasi.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		new Label(grpDataLaporan, SWT.NONE);
		
		Button btnTampil = new Button(grpDataLaporan, SWT.NONE);
		btnTampil.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ResultSet result = null;
				tblPendapatan.removeAll();
				Calendar instance = Calendar.getInstance();
				instance.set(Calendar.DAY_OF_MONTH, dtRealisasi.getDay());
				instance.set(Calendar.MONTH, dtRealisasi.getMonth());
				instance.set(Calendar.YEAR, dtRealisasi.getYear());
				realisasi = new Date(dtRealisasi.getYear() - 1900, dtRealisasi.getMonth(), dtRealisasi.getDay());
				
				if (cmbTanggal.getSelectionIndex() == 0){
					if (cmbKewajibanPajak.getText().equalsIgnoreCase(""))
						result = ControllerFactory.getMainController().getCpPeriksaDAOImpl().GetRealisasiPenerimaanSKPDKB("", masaPajakDari, masaPajakSampai, realisasi);
					else
						result = ControllerFactory.getMainController().getCpPeriksaDAOImpl().GetRealisasiPenerimaanSKPDKB(cmbKewajibanPajak.getData(Integer.toString(cmbKewajibanPajak.getSelectionIndex())).toString(), masaPajakDari, masaPajakSampai, realisasi);
				}else if (cmbTanggal.getSelectionIndex() == 1){
					if (cmbKewajibanPajak.getText().equalsIgnoreCase(""))
						result = ControllerFactory.getMainController().getCpPeriksaDAOImpl().GetRealisasiPenerimaanSKPDKBbyTglCetak("", masaPajakDari, masaPajakSampai);
					else
						result = ControllerFactory.getMainController().getCpPeriksaDAOImpl().GetRealisasiPenerimaanSKPDKBbyTglCetak(cmbKewajibanPajak.getData(Integer.toString(cmbKewajibanPajak.getSelectionIndex())).toString(), masaPajakDari, masaPajakSampai);
				}
				
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
		btnBaru.setText("Baru");
		btnBaru.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		Button btnCetak = new Button(grpDataLaporan, SWT.NONE);
		btnCetak.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(isPrint()){
					String bulan = "";
					ReportAppContext.name = "Variable";
					ReportAppContext.nameObject = "RealisasiSKPDKB";
					ReportAppContext.classLoader = RealisasiHarianView.class.getClassLoader();
					SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
					Date tgl = new Date(calSKP.getYear() - 1900, calSKP.getMonth(), calSKP.getDay());
					if (btnTanggal.getSelection())
						bulan = "Realisasi Penerimaan SKPDKB " + sdf.format(tgl);
					else if (btnBulan.getSelection())
						bulan = "Realisasi Penerimaan SKPDKB Bulan " + UIController.INSTANCE.formatMonth(monthSKP.getMonth() + 1, Locale.getDefault()) + " " + monthSKP.getYear();
					else if (btnTahun.getSelection())
						bulan = "Realisasi Penerimaan SKPDKB Tahun " + String.valueOf(yearSKP.getText());
					
					if(!ReportAppContext.object.isEmpty())
						ReportAppContext.object.clear();
//					Integer count = 0;
					for(int j=0;j<tblPendapatan.getItems().length; j++){
						List<String> values = new ArrayList<String>();
						for(int i=0;i<tblPendapatan.getColumnCount();i++){// untuk masukkan data berdasarkan colom table
							if(i==6||i==7||i==8||i==9||i==10||i==11)// untuk colom yang berhubungan dengan currency
								values.add(tblPendapatan.getItem(j).getText(i).substring(2).replace(".", "").replace(",", "."));
							else
								values.add(tblPendapatan.getItem(j).getText(i));
						}
						ReportAppContext.object.put(Integer.toString(j), values);
//						count++;
					}
					if (!cmbKewajibanPajak.getText().equalsIgnoreCase(""))
						ReportAppContext.map.put("Pajak", "Pajak " + cmbKewajibanPajak.getText());	
					else
						ReportAppContext.map.put("Pajak", "");
					ReportAppContext.map.put("BulanTahun", bulan);
					ReportAppContext.map.put("PokokPajak", txtPokokPajak.getText());
					ReportAppContext.map.put("Denda", txtDenda.getText());
					ReportAppContext.map.put("DendaAngsuran", txtDendaAngsuran.getText());
					ReportAppContext.map.put("DendaSSPD", txtDendaSSPD.getText());
					ReportAppContext.map.put("Total", txtTotal.getText());
					ReportAppContext.map.put("Realisasi", txtRealisasi.getText());
					
					ReportAction.start("DaftarRealisasiSKPDKB.rptdesign");
				}else
					MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Anda tidak memiliki hak untuk mencetak.");
			}
		});
		GridData gd_btnCetak = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnCetak.widthHint = 90;
		btnCetak.setLayoutData(gd_btnCetak);
		btnCetak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnCetak.setText("Cetak");
		new Label(grpDataLaporan, SWT.NONE);
		new Label(grpDataLaporan, SWT.NONE);
		
		Group grpPendapatan = new Group(composite, SWT.NONE);
		grpPendapatan.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 14, 2));
		grpPendapatan.setLayout(new GridLayout(1, false));
		grpPendapatan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.BOLD));
		
		tblPendapatan = new Table(grpPendapatan, SWT.BORDER | SWT.FULL_SELECTION | SWT.VIRTUAL);
		tblPendapatan.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		tblPendapatan.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		tblPendapatan.setForeground(SWTResourceManager.getColor(0, 0, 0));
		tblPendapatan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE-1, SWT.NORMAL));
		tblPendapatan.setHeaderVisible(true);
		tblPendapatan.setLinesVisible(true);
		tblPendapatan.addPaintListener(new PaintListener() {
			@Override
			public void paintControl(PaintEvent pe) {
				pe.gc.setForeground(fontColor);
				pe.gc.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.BOLD));
				pe.gc.drawText("Pendapatan", 5, 0, false);
			}
		});
		
		
		Label lblPokokPajak = new Label(composite, SWT.NONE);
		lblPokokPajak.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPokokPajak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblPokokPajak.setForeground(fontColor);
		lblPokokPajak.setText("Pokok Pajak");
		
		txtPokokPajak = new Text(composite, SWT.BORDER | SWT.READ_ONLY);
		txtPokokPajak.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		txtPokokPajak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtPokokPajak.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		//txtPokokPajak.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		txtPokokPajak.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		
		Label lblDenda = new Label(composite, SWT.NONE);
		lblDenda.setForeground(fontColor);
		lblDenda.setText("+");
		
		Label lblDenda_1 = new Label(composite, SWT.NONE);
		lblDenda_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblDenda_1.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblDenda_1.setForeground(fontColor);
		lblDenda_1.setText("Denda");
		
		txtDenda = new Text(composite, SWT.BORDER | SWT.READ_ONLY);
		txtDenda.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		txtDenda.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtDenda.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
//		txtDenda.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		txtDenda.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		
		Label label_2 = new Label(composite, SWT.NONE);
		label_2.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		label_2.setForeground(fontColor);
		label_2.setText("+");
		
		Label lblDendaAngsuran = new Label(composite, SWT.NONE);
		lblDendaAngsuran.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblDendaAngsuran.setForeground(fontColor);
		lblDendaAngsuran.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblDendaAngsuran.setText("Denda Angsuran");
		
		txtDendaAngsuran = new Text(composite, SWT.BORDER | SWT.READ_ONLY);
		txtDendaAngsuran.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtDendaAngsuran.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtDendaAngsuran.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label label_1 = new Label(composite, SWT.NONE);
		label_1.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		label_1.setForeground(fontColor);
		label_1.setText("+");
		
		Label lblDendaSspd = new Label(composite, SWT.NONE);
		lblDendaSspd.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblDendaSspd.setForeground(fontColor);
		lblDendaSspd.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblDendaSspd.setText("Denda SSPD");
		
		txtDendaSSPD = new Text(composite, SWT.BORDER | SWT.READ_ONLY);
		txtDendaSSPD.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtDendaSSPD.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtDendaSSPD.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label label = new Label(composite, SWT.NONE);
		label.setForeground(fontColor);
		label.setText("=");
		
		Label lblTotal = new Label(composite, SWT.NONE);
		lblTotal.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblTotal.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblTotal.setForeground(fontColor);
		lblTotal.setText("Total");
		
		txtTotal = new Text(composite, SWT.BORDER | SWT.READ_ONLY);
		txtTotal.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		txtTotal.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtTotal.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
//		txtTotal.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		txtTotal.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		
		Label lblRealisasi = new Label(composite, SWT.NONE);
		lblRealisasi.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblRealisasi.setForeground(fontColor);
		lblRealisasi.setText("Realisasi");
		
		txtRealisasi = new Text(composite, SWT.BORDER | SWT.READ_ONLY);
		txtRealisasi.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		txtRealisasi.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtRealisasi.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
//		txtRealisasi.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		txtRealisasi.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}
	
	@SuppressWarnings("deprecation")
	private void setMasaPajak()
	{
		if (btnTanggal.getSelection())
		{
			Calendar instance = Calendar.getInstance();
			instance.set(Calendar.DAY_OF_MONTH, calSKP.getDay());
			instance.set(Calendar.MONTH, calSKP.getMonth());
			instance.set(Calendar.YEAR, calSKP.getYear());
			masaPajakDari = new Date(calSKP.getYear() - 1900, calSKP.getMonth(), calSKP.getDay());
			masaPajakSampai = masaPajakDari;
//			masaPajak = masaPajakDari.getDate() + " " + UIController.INSTANCE.formatMonth(masaPajakDari.getMonth() + 1, Locale.getDefault()) + " " + (masaPajakDari.getYear() + 1900);
		}
		if (btnBulan.getSelection())
		{
			Calendar instance = Calendar.getInstance();
			instance.set(Calendar.DAY_OF_MONTH, monthSKP.getDay());
			instance.set(Calendar.MONTH, monthSKP.getMonth());
			instance.set(Calendar.YEAR, monthSKP.getYear());
			masaPajakDari = new Date(monthSKP.getYear() - 1900, monthSKP.getMonth(), 1);
			masaPajakSampai = new Date(monthSKP.getYear() - 1900, monthSKP.getMonth(), instance.getActualMaximum(Calendar.DATE));
//			masaPajak = UIController.INSTANCE.formatMonth(monthSKP.getMonth() + 1, Locale.getDefault()) + " " + monthSKP.getYear();
		}
		if (btnTahun.getSelection())
		{
			masaPajakDari = new Date(yearSKP.getSelection() - 1900, 0, 1);
			masaPajakSampai = new Date(yearSKP.getSelection() - 1900, 11, 31);
//			masaPajak = Integer.toString(yearSKP.getSelection());
		}
	
	}
	
	private void createColumn(){
		if(tblPendapatan.getColumnCount() <= 0){
			String[] listColumn = {"Nama Badan", "Alamat", "NPWPD", "Tanggal SKP", "No SKP", "Masa Pajak", "Pajak Periksa", "SKPDKB", "Denda", "Denda Angsuran", "Denda SSPD", "Realisasi", "Tanggal Bayar", "Keterangan"};
			int[] widthColumn = {200, 160, 100, 100, 160, 200, 140, 140, 140, 140, 140, 140, 110, 160};
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
		double totalDendaSKPDKB = 0;
		double totalJumlahBayar = 0;
		double totalDendaSSPD = 0;
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		try
		{
			while (result.next())
			{
				masaPajak = UIController.INSTANCE.formatMonth(result.getDate("MasaPajakDari").getMonth() + 1, Locale.getDefault()) + " " + (result.getDate("MasaPajakDari").getYear()+ 1900);
				if (! (result.getDate("MasaPajakDari").getMonth() == result.getDate("MasaPajakSampai").getMonth() && result.getDate("MasaPajakDari").getYear() == result.getDate("MasaPajakSampai").getYear()))
					masaPajak += " - " + UIController.INSTANCE.formatMonth(result.getDate("MasaPajakSampai").getMonth() + 1, Locale.getDefault()) + " " + (result.getDate("MasaPajakSampai").getYear() + 1900);
				
				TableItem item = new TableItem(tblPendapatan, SWT.NONE);
				item.setText(0, result.getString("Nama_Badan"));
				item.setText(1, result.getString("Alabad_Jalan"));
				item.setText(2, result.getString("NPWPD"));
				item.setText(3, formatter.format(result.getDate("Tanggal_SKP")));
				item.setText(4, result.getString("No_SKP"));
				item.setText(5, masaPajak);
				item.setText(6, indFormat.format(round(result.getDouble("PajakPeriksa"), 2)));
				item.setText(7, indFormat.format(round(result.getDouble("SKPDKB"), 2)));
				item.setText(8, indFormat.format(round(result.getDouble("Denda"), 2)));
				item.setText(9, indFormat.format(round(result.getDouble("DendaSKPDKB"), 2)));
				item.setText(10, indFormat.format(round(result.getDouble("DendaSSPD"), 2)));
				item.setText(11, indFormat.format(round(result.getDouble("Realisasi"), 2)));
				if (result.getTimestamp("TanggalBayar") == null)
					item.setText(12, "");
				else
					item.setText(12, formatter.format(result.getTimestamp("TanggalBayar")));
				item.setText(13, result.getString("Keterangan"));
				try {
					totalPokokPajak += round(indFormat.parse(item.getText(7)).doubleValue(), 2);
					totalDenda += round(indFormat.parse(item.getText(8)).doubleValue(), 2);
					totalDendaSKPDKB += round(indFormat.parse(item.getText(9)).doubleValue(), 2);
					totalDendaSSPD += round(indFormat.parse(item.getText(10)).doubleValue(), 2);
					totalJumlahBayar += round(indFormat.parse(item.getText(11)).doubleValue(), 2);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
//			TableItem item = new TableItem(tblPendapatan, SWT.NONE);
//			item.setText(0, "Total");
//			item.setText(9, indFormat.format(totalPokokPajak));
//			item.setText(10, indFormat.format(totalDenda));
//			item.setText(11, indFormat.format(totalJumlahBayar));
			txtPokokPajak.setText(indFormat.format(round(totalPokokPajak, 2)));
			txtDenda.setText(indFormat.format(round(totalDenda, 2)));
			txtDendaAngsuran.setText(indFormat.format(round(totalDendaSKPDKB, 2)));
			txtDendaSSPD.setText(indFormat.format(round(totalDendaSSPD, 2)));
			txtTotal.setText(indFormat.format(round(totalPokokPajak + totalDenda + totalDendaSKPDKB + totalDendaSSPD, 2)));
			txtRealisasi.setText(indFormat.format(round(totalJumlahBayar, 2)));
		} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		}
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
	
	private boolean isPrint(){		
		return userModul.getCetak();
	}

}
