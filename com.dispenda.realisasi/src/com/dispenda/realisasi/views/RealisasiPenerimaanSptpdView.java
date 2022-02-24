package com.dispenda.realisasi.views;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import com.dispenda.model.BidangUsahaProvider;
import com.dispenda.model.KecamatanProvider;
import com.dispenda.model.PajakProvider;
import com.dispenda.model.UserModul;
import com.dispenda.object.GlobalVariable;
import com.dispenda.object.Preferences;
import com.dispenda.report.ReportAction;
import com.dispenda.report.ReportAppContext;
import com.dispenda.widget.MoneyField;
import com.ibm.icu.util.Currency;
import com.ibm.icu.util.CurrencyAmount;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class RealisasiPenerimaanSptpdView extends ViewPart {
	public RealisasiPenerimaanSptpdView() {
	}

	public static final String ID = RealisasiPenerimaanSptpdView.class.getName();
	private Table tblPendapatan;
	private MoneyField txtPokokPajak;
	private MoneyField txtDenda;
	private MoneyField txtTotal;
	private MoneyField txtRealisasi;
	private Color fontColor = new Color(Display.getCurrent(),Preferences.FONT_COLOR);
	private Timestamp dateNow;
	private Combo cmbTahunRealisasi;
	private Combo cmbKewajibanPajak;
	private Combo cmbKecamatan;
	private ResultSet result = null;
	Double totalPajak = 0.0;
	Double totalDenda = 0.0;
	Double totalRealisasi = 0.0;
	private Locale ind = new Locale("id", "ID");
	private NumberFormat indFormat = NumberFormat.getCurrencyInstance(ind);
	private Combo cmbSubPajak;
	private int idSubPajak;
	
	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new GridLayout(1, false));
		
		dateNow = (java.sql.Timestamp)ControllerFactory.getMainController().getCpWajibPajakDAOImpl().getDateNow();
		// TODO Auto-generated method stub
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		composite.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		composite.setLayout(new GridLayout(8, false));
		
		Group grpDataLaporan = new Group(composite, SWT.NONE);
		grpDataLaporan.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 8, 1));
		grpDataLaporan.setLayout(new GridLayout(6, false));
		grpDataLaporan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.BOLD));
		grpDataLaporan.addPaintListener(new PaintListener() {
			@Override
			public void paintControl(PaintEvent pe) {
				pe.gc.setForeground(fontColor);
				pe.gc.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.BOLD));
				pe.gc.drawText("Data Laporan Realisasi Penerimaan SPTPD", 5, 0, false);
			}
		});
		
		Label lblKewajibanPajak = new Label(grpDataLaporan, SWT.NONE);
		lblKewajibanPajak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblKewajibanPajak.setForeground(fontColor);
		lblKewajibanPajak.setText("Kewajiban Pajak");
		
		cmbKewajibanPajak = new Combo(grpDataLaporan, SWT.READ_ONLY);
		cmbKewajibanPajak.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				idSubPajak = 0;
				UIController.INSTANCE.loadBidangUsaha(cmbSubPajak, BidangUsahaProvider.INSTANCE.getBidangUsaha((Integer)cmbKewajibanPajak.getData(Integer.toString(cmbKewajibanPajak.getSelectionIndex()))).toArray());
			}
		});
		GridData gd_cmbKewajibanPajak = new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1);
		gd_cmbKewajibanPajak.widthHint = 185;
		cmbKewajibanPajak.setLayoutData(gd_cmbKewajibanPajak);
		cmbKewajibanPajak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
//		cmbKewajibanPajak.setForeground(fontColor);
		cmbKewajibanPajak.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		UIController.INSTANCE.loadPajak(cmbKewajibanPajak, PajakProvider.INSTANCE.getPajak().toArray());
		
		Label label_1 = new Label(grpDataLaporan, SWT.NONE);
		label_1.setVisible(true);
		label_1.setText("-");
		label_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		
		cmbSubPajak = new Combo(grpDataLaporan, SWT.READ_ONLY);
		cmbSubPajak.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				idSubPajak = (Integer)cmbSubPajak.getData(Integer.toString(cmbSubPajak.getSelectionIndex()));
			}
		});
		cmbSubPajak.setVisible(true);
		cmbSubPajak.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		cmbSubPajak.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		cmbSubPajak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		GridData gd_cmbSubPajak = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_cmbSubPajak.widthHint = 225;
		cmbSubPajak.setLayoutData(gd_cmbSubPajak);
		
		Label lblKecamatan = new Label(grpDataLaporan, SWT.NONE);
		lblKecamatan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblKecamatan.setForeground(fontColor);
		lblKecamatan.setText("Kecamatan");
		
		cmbKecamatan = new Combo(grpDataLaporan, SWT.READ_ONLY);
		GridData gd_cmbKecamatan = new GridData(SWT.FILL, SWT.CENTER, false, false, 3, 1);
		gd_cmbKecamatan.widthHint = 172;
		cmbKecamatan.setLayoutData(gd_cmbKecamatan);
		cmbKecamatan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
//		cmbKecamatan.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		cmbKecamatan.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		UIController.INSTANCE.loadKecamatan(cmbKecamatan, KecamatanProvider.INSTANCE.getKecamatan().toArray());
		new Label(grpDataLaporan, SWT.NONE);
		new Label(grpDataLaporan, SWT.NONE);
		
		Label lblTahunRealisasi = new Label(grpDataLaporan, SWT.NONE);
		lblTahunRealisasi.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblTahunRealisasi.setForeground(fontColor);
		lblTahunRealisasi.setText("Tahun Realisasi");
		
		cmbTahunRealisasi = new Combo(grpDataLaporan, SWT.READ_ONLY);
		GridData gd_cmbTahunRealisasi = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
		gd_cmbTahunRealisasi.widthHint = 64;
		cmbTahunRealisasi.setLayoutData(gd_cmbTahunRealisasi);
		cmbTahunRealisasi.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
//		cmbTahunRealisasi.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		cmbTahunRealisasi.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		new Label(grpDataLaporan, SWT.NONE);
		new Label(grpDataLaporan, SWT.NONE);
		new Label(grpDataLaporan, SWT.NONE);
		new Label(grpDataLaporan, SWT.NONE);
		loadTahunSPT();
		
		Button btnTampil = new Button(grpDataLaporan, SWT.NONE);
		btnTampil.addSelectionListener(new SelectionAdapter() {
			@SuppressWarnings("deprecation")
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (isValidated()){
					tblPendapatan.removeAll();
					String kodePajak = cmbKewajibanPajak.getData(Integer.toString(cmbKewajibanPajak.getSelectionIndex())).toString();
					String kodeKec = "%";
					Date masaPajakDari = new Date(Integer.valueOf(cmbTahunRealisasi.getText()) - 1901, 11, 1);
					Date masaPajakSampai = new Date(Integer.valueOf(cmbTahunRealisasi.getText()) - 1900, 10, 30);
					if (!cmbKecamatan.getText().equalsIgnoreCase(""))
						kodeKec = (String)cmbKecamatan.getData(cmbKecamatan.getText());
					
					result = ControllerFactory.getMainController().getCpLaporanDAOImpl().getRealisasiPenerimaanSPTPD(kodeKec, kodePajak, masaPajakDari, masaPajakSampai);
					createColumns();
					loadData(tblPendapatan, result);
					showReport();
				}
			}
		});
		GridData gd_btnTampil = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_btnTampil.widthHint = 90;
		btnTampil.setLayoutData(gd_btnTampil);
		btnTampil.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnTampil.setText("Tampil");
		
		Button btnCetak = new Button(grpDataLaporan, SWT.NONE);
		btnCetak.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(isPrint()){
					ReportAppContext.name = "Variable";
					ReportAppContext.map.put("Tahun", cmbTahunRealisasi.getText());
					ReportAppContext.map.put("Pajak", indFormat.format(txtPokokPajak.getMoney().getNumber().doubleValue()));
					ReportAppContext.map.put("Denda", indFormat.format(txtDenda.getMoney().getNumber().doubleValue()));
					ReportAppContext.map.put("Total", indFormat.format(txtTotal.getMoney().getNumber().doubleValue()));
					ReportAppContext.map.put("Realisasi", indFormat.format(txtRealisasi.getMoney().getNumber().doubleValue()));
					ReportAppContext.nameObject = "RealisasiPenerimaanSPTPD";
					ReportAppContext.classLoader = RealisasiPenerimaanSptpdView.class.getClassLoader();
					if(!ReportAppContext.object.isEmpty())
						ReportAppContext.object.clear();
					Integer count = 0;
					for(int ti=0;ti<tblPendapatan.getItems().length; ti++){
						List<String> values = new ArrayList<String>();
						for(int i=1;i<tblPendapatan.getColumnCount();i++){
							if ((i % 7 == 0 || i % 7 == 1|| i % 7 == 2) && i > 6 && tblPendapatan.getItem(ti).getText(i).length() > 2)
								values.add(tblPendapatan.getItem(ti).getText(i).substring(2).replace(".", "").replace(",", "."));
							else
								values.add(tblPendapatan.getItem(ti).getText(i));
						}
						ReportAppContext.object.put(count.toString(), values);
						count++;
					}
					ReportAction.start("RealisasiPenerimaanSPTPD.rptdesign");
				}else
					MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Anda tidak memiliki hak untuk mencetak.");
			}
		});
		GridData gd_btnCetak = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnCetak.widthHint = 90;
		btnCetak.setLayoutData(gd_btnCetak);
		btnCetak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnCetak.setText("Cetak");
		
		Button btnCetakRealisasi = new Button(grpDataLaporan, SWT.NONE);
		btnCetakRealisasi.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(isPrint()){
					ReportAppContext.name = "Variable";
					ReportAppContext.map.put("Tahun", cmbTahunRealisasi.getText());
					ReportAppContext.map.put("Pajak", indFormat.format(txtPokokPajak.getMoney().getNumber().doubleValue()));
					ReportAppContext.map.put("Denda", indFormat.format(txtDenda.getMoney().getNumber().doubleValue()));
					ReportAppContext.map.put("Total", indFormat.format(txtTotal.getMoney().getNumber().doubleValue()));
					ReportAppContext.map.put("Realisasi", indFormat.format(txtRealisasi.getMoney().getNumber().doubleValue()));
					ReportAppContext.nameObject = "RealisasiSPTPDTahunan";
					ReportAppContext.classLoader = RealisasiPenerimaanSptpdView.class.getClassLoader();
					if(!ReportAppContext.object.isEmpty())
						ReportAppContext.object.clear();
					Integer count = 0;
					for(int ti=0;ti<tblPendapatan.getItems().length; ti++){
						List<String> values = new ArrayList<String>();
						for(int i=1;i<tblPendapatan.getColumnCount();i++){
							if ((i % 7 == 0 || i % 7 == 1) && i > 6 && tblPendapatan.getItem(ti).getText(i).length() > 2)
								values.add(tblPendapatan.getItem(ti).getText(i).substring(2).replace(".", "").replace(",", "."));
							else if ((i % 7 == 2) && i > 6 && tblPendapatan.getItem(ti).getText(i).length() > 2)
								try {
									Double jlhBayar = Double.valueOf(tblPendapatan.getItem(ti).getText(i).substring(2).replace(".", "").replace(",", "."));
									values.add(jlhBayar > 0.0 ? "Lunas" : "Belum Bayar");
//									Double.valueOf(tblPendapatan.getItem(ti).getText(i).substring(2).replace(".", "").replace(",", "."));
								} catch (Exception e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
									values.add("Belum Bayar");
								}
							else
								values.add(tblPendapatan.getItem(ti).getText(i));
						}
						ReportAppContext.object.put(count.toString(), values);
						count++;
					}
					ReportAction.start("RealisasiSPTPDTahunan.rptdesign");
				}else
					MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Anda tidak memiliki hak untuk mencetak.");
			}
		});
		btnCetakRealisasi.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1));
		btnCetakRealisasi.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnCetakRealisasi.setText("Cetak Realisasi");
		
		Group grpPendapatan = new Group(composite, SWT.NONE);
		grpPendapatan.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 8, 1));
		grpPendapatan.setLayout(new GridLayout(1, false));
		grpPendapatan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.BOLD));
//		grpPendapatan.addPaintListener(new PaintListener() {
//			@Override
//			public void paintControl(PaintEvent pe) {
//				pe.gc.setForeground(fontColor);
//				pe.gc.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.BOLD));
//				pe.gc.drawText("Pendapatan", 5, 0, false);
//			}
//		});
		
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
		
		Label lblNewLabel = new Label(composite, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblNewLabel.setForeground(fontColor);
		lblNewLabel.setText("Pokok Pajak");
		
		txtPokokPajak = new MoneyField(composite, SWT.BORDER);
		txtPokokPajak.getText().setEditable(false);
		txtPokokPajak.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		txtPokokPajak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtPokokPajak.getText().setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtPokokPajak.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		txtPokokPajak.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		new Label(txtPokokPajak, SWT.NONE);
//		txtPokokPajak.setText("");
		
		Label lblDenda = new Label(composite, SWT.NONE);
		lblDenda.setForeground(fontColor);
		lblDenda.setText("+");
		
		Label lblDenda_1 = new Label(composite, SWT.NONE);
		lblDenda_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblDenda_1.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblDenda_1.setForeground(fontColor);
		lblDenda_1.setText("Denda");
		
		txtDenda = new MoneyField(composite, SWT.BORDER);
		txtDenda.getText().setEditable(false);
		txtDenda.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		txtDenda.getText().setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtDenda.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtDenda.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		txtDenda.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		new Label(txtDenda, SWT.NONE);
		
		Label label = new Label(composite, SWT.NONE);
		label.setForeground(fontColor);
		label.setText("=");
		
		Label lblTotal = new Label(composite, SWT.NONE);
		lblTotal.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblTotal.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblTotal.setForeground(fontColor);
		lblTotal.setText("Total");
		
		txtTotal = new MoneyField(composite, SWT.BORDER);
		txtTotal.getText().setEditable(false);
		txtTotal.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		txtTotal.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtTotal.getText().setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtTotal.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		txtTotal.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		new Label(txtTotal, SWT.NONE);
		
		Label lblRealisasi = new Label(composite, SWT.NONE);
		lblRealisasi.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblRealisasi.setForeground(fontColor);
		lblRealisasi.setText("Realisasi");
		
		txtRealisasi = new MoneyField(composite, SWT.BORDER);
		txtRealisasi.getText().setEditable(false);
		txtRealisasi.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		txtRealisasi.getText().setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtRealisasi.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtRealisasi.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		txtRealisasi.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		new Label(txtRealisasi, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		
				
		// TODO Auto-generated method stub

	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}
	
	@SuppressWarnings("deprecation")
	private void loadTahunSPT(){
		for (int i=dateNow.getYear()+1900;i>=2006;i--){
			cmbTahunRealisasi.add(String.valueOf(i));
		}
	}
	
	private boolean isValidated(){
		if (cmbKewajibanPajak.getText().equalsIgnoreCase("")){
			MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Silahkan isi Jenis Kewajiban Pajak");
			return false;
		}
		else if (cmbTahunRealisasi.getText().equalsIgnoreCase("")){
			MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Silahkan isi Tahun Realisasi");
			return false;
		}
		return true;
	}
	
	private void createColumns(){
		if(tblPendapatan.getColumnCount() <= 0){
			String[] listColumn = new String[89]; //{"Nama Badan", "Kecamatan", "Alamat", "NPWPD", "Tanggal SKP", "No SPTPD", "SPTPD", "Denda", "REALISASI", "Tanggal Bayar", "Masa Pajak"};
//			int[] widthColumn = {200, 160, 100, 100, 160, 200, 140, 140, 130, 140, 110, 160};
			listColumn[0] = "";
			listColumn[1] = "Nama Badan";
			listColumn[2] = "Kecamatan";
			listColumn[3] = "Alamat";
			listColumn[4] = "NPWPD";
			
			String[] namaKolom = {"Tanggal SKP", "No SPTPD", "SPTPD", "Denda", "REALISASI", "Tanggal Bayar", "Bulan Masa Pajak"};
			int count = 5;
			for (int j=0;j<12;j++){
				for (int i=0;i<=6;i++){
					listColumn[count] = namaKolom[i];
					count+=1;
				}
			}
			for (int i=listColumn.length - 1;i>=0;i--)
			{
				TableColumn colPajak = new TableColumn(tblPendapatan, SWT.NONE,0);
				colPajak.setText(listColumn[i]);
				if (i==0)
					colPajak.setWidth(5);
				else
					colPajak.setWidth(140);
			}
		}
	}
	
	private void loadData(Table tbl, ResultSet result){
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		try
		{
			while (result.next())
			{
				TableItem item = new TableItem(tbl, SWT.NONE);
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
					}
				}
				try {
					totalPajak += indFormat.parse(item.getText(7)).doubleValue();
					totalDenda += indFormat.parse(item.getText(8)).doubleValue();
					totalRealisasi += indFormat.parse(item.getText(9)).doubleValue();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}
		txtPokokPajak.setMoney(new CurrencyAmount(totalPajak, Currency.getInstance("ind")));
		txtDenda.setMoney(new CurrencyAmount(totalDenda, Currency.getInstance("ind")));
		txtTotal.setMoney(new CurrencyAmount(totalPajak + totalDenda, Currency.getInstance("ind")));
		txtRealisasi.setMoney(new CurrencyAmount(totalRealisasi, Currency.getInstance("ind")));
	}
	
	private void showReport(){
		int tanda = 0;
		List<Integer> arrTanda = new ArrayList<Integer>();
		arrTanda.add(0);
		int length = tblPendapatan.getItemCount();
		
		for (int j=0;j<length;j++){
			int bulan = Integer.valueOf(tblPendapatan.getItem(j).getText(11)); //result.getInt("BulanMasaPajak");
			if (j == length - 1){
				if (bulan != 12){
					for (int h=0;h<=6;h++){
						int pos = (bulan * 7) + 5 + h;
//						TableItem item = new TableItem(tblPendapatan, SWT.NONE);
						tblPendapatan.getItem(tanda).setText(pos, tblPendapatan.getItem(j).getText(h+5));
						tblPendapatan.getItem(j).setText(h+5, "");
					}
				}else{
					for (int h=0;h<=6;h++){
						tblPendapatan.getItem(tanda).setText(h+5, tblPendapatan.getItem(j).getText(h+5));
					}
				}
			} else{
				if (bulan != 12){
					if (tblPendapatan.getItem(j).getText(4).equalsIgnoreCase(tblPendapatan.getItem(j+1).getText(4))){
						for (int h=0;h<=6;h++){
							int pos = (bulan * 7) + 5 + h;
							tblPendapatan.getItem(tanda).setText(pos, tblPendapatan.getItem(j).getText(h+5));
							tblPendapatan.getItem(j).setText(h+5, "");
						}
						tblPendapatan.getItem(j+1).setText(1, "");
					}else{
						for (int h=0;h<=6;h++){
							int pos = (bulan * 7) + 5 + h;
							tblPendapatan.getItem(tanda).setText(pos, tblPendapatan.getItem(j).getText(h+5));
							tblPendapatan.getItem(j).setText(h+5, "");
						}
						tanda = j+1;
						arrTanda.add(tanda);
					}
				} else{
					if (tblPendapatan.getItem(j).getText(4).equalsIgnoreCase(tblPendapatan.getItem(j+1).getText(4))){
						for (int h=0;h<=6;h++){
							tblPendapatan.getItem(tanda).setText(h+5, tblPendapatan.getItem(j).getText(h+5));
						}
						tblPendapatan.getItem(j+1).setText(1, "");
					}else{
						for (int h=0;h<=6;h++){
							tblPendapatan.getItem(tanda).setText(h+5, tblPendapatan.getItem(j).getText(h+5));
						}
						tanda = j+1;
						arrTanda.add(tanda);
					}
				}
				
			}
		}
		
		for (int m=0;m<length;m++){
			if (m == length)
				break;
			else{
				if (tblPendapatan.getItem(m).getText(1).equalsIgnoreCase("")){
					tblPendapatan.remove(m);
					m -= 1;
					length -= 1;
				}
			}
		}
		
	}
	
	private UserModul userModul = ControllerFactory.getMainController().getCpUserDAOImpl()
			.getHMUserModul(GlobalVariable.userModul.getIdUser()).get(29); // 1 merupakan id sub modul.
	
	private boolean isPrint(){		
		return userModul.getCetak();
	}

}