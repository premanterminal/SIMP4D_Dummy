package com.dispenda.skpdkb.views;
import java.sql.ResultSet;
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
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.SWTResourceManager;

import com.dispenda.controller.ControllerFactory;
import com.dispenda.controller.UIController;
import com.dispenda.model.BidangUsahaProvider;
import com.dispenda.model.PajakProvider;
import com.dispenda.model.Pejabat;
import com.dispenda.controller.ControllerFactory;
import com.dispenda.model.UserModul;
import com.dispenda.object.GlobalVariable;
import com.dispenda.object.Preferences;
import com.dispenda.report.ReportAction;
import com.dispenda.report.ReportAppContext;
import com.dispenda.report.ReportModule;

import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class NotaPerhitunganView extends ViewPart {
	public static final String ID = NotaPerhitunganView.class.getName();
	private Table tblNPPD;
	private Color fontColor = new Color(Display.getCurrent(),Preferences.FONT_COLOR);
	private DateTime calNPPD;
	private Spinner yearNPPD;
	private DateTime monthNPPD;
	private Button btnTanggal;
	private Button btnBulan;
	private Button btnTahun;
	private Date masaPajakDari;
	private Date masaPajakSampai;
	private String masaPajak;
	private Button btnSkpd;
	private Button btnSkpdkb;
	private Button btnSkpdlbcov;
	private Button btnSkpdn;
	private Button btnSkpdkbt;
	private List<Pejabat> listPejabat;
	private Timestamp dateNow;
	private Combo cmbSubPajak;
	private Combo cmbKewajibanPajak;
	private ResultSet result = null;
	private Locale ind = new Locale("id", "ID");
	private NumberFormat indFormat = NumberFormat.getCurrencyInstance(ind);
	private Combo cmbPenandatangan2;
	private Combo cmbPenandatangan1;
	
	public NotaPerhitunganView() {
	}

	@SuppressWarnings("deprecation")
	@Override
	public void createPartControl(Composite parent) {		
		Locale.setDefault(new Locale("id","ID"));
		dateNow = (java.sql.Timestamp)ControllerFactory.getMainController().getCpWajibPajakDAOImpl().getDateNow();
		ScrolledComposite scrolledComposite = new ScrolledComposite(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		
		Composite comp = new Composite(scrolledComposite, SWT.NONE);
		comp.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BORDER));
		comp.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		comp.setLayout(new GridLayout(9, false));
		
		Label lblJenisSkpd = new Label(comp, SWT.NONE);
		lblJenisSkpd.setForeground(fontColor);
		lblJenisSkpd.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblJenisSkpd.setText("Jenis SKPD");
		
		Group compJenisSKPD = new Group(comp, SWT.NONE);
		compJenisSKPD.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 8, 1));
		compJenisSKPD.setLayout(new GridLayout(5, false));
		
		btnSkpd = new Button(compJenisSKPD, SWT.RADIO);
		btnSkpd.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnSkpd.setText("SKPD");
		
		
		btnSkpdkb = new Button(compJenisSKPD, SWT.RADIO);
		btnSkpdkb.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnSkpdkb.setText("SKPDKB");
		
		
		btnSkpdn = new Button(compJenisSKPD, SWT.RADIO);
		btnSkpdn.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnSkpdn.setText("SKPDN");
		
		btnSkpdkbt = new Button(compJenisSKPD, SWT.RADIO);
		btnSkpdkbt.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnSkpdkbt.setText("SKPDKBT");
		
		btnSkpdlbcov = new Button(compJenisSKPD, SWT.RADIO);
		btnSkpdlbcov.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnSkpdlbcov.setText("SKPDLB (Covid-19)");
		
		Label lblKewajibanPajak = new Label(comp, SWT.NONE);
		lblKewajibanPajak.setForeground(fontColor);
		lblKewajibanPajak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblKewajibanPajak.setText("Kewajiban Pajak");
		
		cmbKewajibanPajak = new Combo(comp, SWT.READ_ONLY);
		cmbKewajibanPajak.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String idPajak = cmbKewajibanPajak.getData(Integer.toString(cmbKewajibanPajak.getSelectionIndex())).toString();
				UIController.INSTANCE.loadBidangUsaha(cmbSubPajak, BidangUsahaProvider.INSTANCE.getBidangUsaha(Integer.parseInt(idPajak)).toArray());
			}
		});
		cmbKewajibanPajak.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		cmbKewajibanPajak.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		cmbKewajibanPajak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		GridData gd_cmbKewajibanPajak = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_cmbKewajibanPajak.widthHint = 250;
		UIController.INSTANCE.loadPajak(cmbKewajibanPajak, PajakProvider.INSTANCE.getPajak().toArray());
		cmbKewajibanPajak.setLayoutData(gd_cmbKewajibanPajak);
		
		Label label = new Label(comp, SWT.NONE);
		label.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		label.setText("-");
		
		cmbSubPajak = new Combo(comp, SWT.READ_ONLY);
		cmbSubPajak.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		cmbSubPajak.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		cmbSubPajak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		GridData gd_cmbSubPajak = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
		gd_cmbSubPajak.widthHint = 219;
		cmbSubPajak.setLayoutData(gd_cmbSubPajak);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		
		Label lblTanggalNppd = new Label(comp, SWT.NONE);
		lblTanggalNppd.setForeground(fontColor);
		lblTanggalNppd.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblTanggalNppd.setText("Tanggal NPPD");
		
		Group group = new Group(comp, SWT.NONE);
		GridData gd_group = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_group.heightHint = 54;
		group.setLayoutData(gd_group);
		group.setLayout(new GridLayout(3, false));
		
		btnTanggal = new Button(group, SWT.RADIO);
		btnTanggal.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				calNPPD.setVisible(true);
				calNPPD.pack(true);
				monthNPPD.setVisible(false);
				yearNPPD.setVisible(false);
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
				monthNPPD.setVisible(true);
				monthNPPD.pack(true);
				calNPPD.setVisible(false);
				yearNPPD.setVisible(false);
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
				yearNPPD.setVisible(true);
				yearNPPD.pack(true);
				calNPPD.setVisible(false);
				monthNPPD.setVisible(false);
				setMasaPajak();
			}
		});
		btnTahun.setText("Tahun");
		btnTahun.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BORDER));
		btnTahun.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		Composite composite = new Composite(group, SWT.NONE);
		GridData gd_composite = new GridData(SWT.CENTER, SWT.CENTER, false, false, 3, 1);
		gd_composite.widthHint = 137;
		composite.setLayoutData(gd_composite);
		composite.setLayout(new StackLayout());
		
		calNPPD = new DateTime(composite, SWT.BORDER | SWT.DROP_DOWN);
		calNPPD.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setMasaPajak();
			}
		});
		calNPPD.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BORDER));
		calNPPD.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		calNPPD.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		
		yearNPPD = new Spinner(composite, SWT.BORDER);
		yearNPPD.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setMasaPajak();
			}
		});
		yearNPPD.setVisible(false);
		yearNPPD.setTextLimit(9999);
		yearNPPD.setMaximum(dateNow.getYear() + 1900);
		yearNPPD.setMinimum(2006);
		yearNPPD.setSelection(dateNow.getYear() + 1900);
		yearNPPD.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		yearNPPD.pack(true);
		yearNPPD.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		
		monthNPPD = new DateTime(composite, SWT.BORDER | SWT.DROP_DOWN | SWT.SHORT);
		monthNPPD.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setMasaPajak();
			}
		});
		monthNPPD.setVisible(false);
		monthNPPD.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		monthNPPD.setDay(1);
		monthNPPD.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		new Label(group, SWT.NONE);
		new Label(group, SWT.NONE);
		new Label(group, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		
		Button btnLihat = new Button(comp, SWT.NONE);
		btnLihat.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (btnSkpd.getSelection()){
					if (cmbSubPajak.getSelectionIndex() >= 0){
						result = ControllerFactory.getMainController().getCpPeriksaDAOImpl().GetNotaSKPDListSub(cmbKewajibanPajak.getData(Integer.toString(cmbKewajibanPajak.getSelectionIndex())).toString(), 
								cmbSubPajak.getData(Integer.toString(cmbSubPajak.getSelectionIndex())).toString(), masaPajakDari, masaPajakSampai);
					}
					else{
						result = ControllerFactory.getMainController().getCpPeriksaDAOImpl().GetNotaSKPDList(cmbKewajibanPajak.getData(Integer.toString(cmbKewajibanPajak.getSelectionIndex())).toString(), 
								masaPajakDari, masaPajakSampai);
					}
				}
				else if (btnSkpdkb.getSelection()){
					if (cmbSubPajak.getSelectionIndex() >= 0){
						result = ControllerFactory.getMainController().getCpPeriksaDAOImpl().GetNotaSKPDKBListSub(cmbKewajibanPajak.getData(Integer.toString(cmbKewajibanPajak.getSelectionIndex())).toString(), 
								cmbSubPajak.getData(Integer.toString(cmbSubPajak.getSelectionIndex())).toString(), masaPajakDari, masaPajakSampai);
					}
					else{
						result = ControllerFactory.getMainController().getCpPeriksaDAOImpl().GetNotaSKPDKBList(cmbKewajibanPajak.getData(Integer.toString(cmbKewajibanPajak.getSelectionIndex())).toString(), 
								masaPajakDari, masaPajakSampai);
					}
				}
				else if (btnSkpdkbt.getSelection()){
					result = ControllerFactory.getMainController().getCpPeriksaDAOImpl().GetNotaSKPDKBTList(cmbKewajibanPajak.getData(Integer.toString(cmbKewajibanPajak.getSelectionIndex())).toString(), 
							masaPajakDari, masaPajakSampai);
				}
				else if (btnSkpdn.getSelection()){
					if (cmbSubPajak.getSelectionIndex() >= 0){
						result = ControllerFactory.getMainController().getCpPeriksaDAOImpl().GetNotaSKPDNListSub(cmbKewajibanPajak.getData(Integer.toString(cmbKewajibanPajak.getSelectionIndex())).toString(), 
								cmbSubPajak.getData(Integer.toString(cmbSubPajak.getSelectionIndex())).toString(), masaPajakDari, masaPajakSampai);
					}
					else{
						result = ControllerFactory.getMainController().getCpPeriksaDAOImpl().GetNotaSKPDNList(cmbKewajibanPajak.getData(Integer.toString(cmbKewajibanPajak.getSelectionIndex())).toString(), 
								masaPajakDari, masaPajakSampai);
					}
				}
				else if (btnSkpdlbcov.getSelection()){
					
					if (cmbSubPajak.getSelectionIndex() >= 0){
						result = ControllerFactory.getMainController().getCpPeriksaDAOImpl().GetNotaSKPDLBCovListSub(cmbKewajibanPajak.getData(Integer.toString(cmbKewajibanPajak.getSelectionIndex())).toString(), 
								cmbSubPajak.getData(Integer.toString(cmbSubPajak.getSelectionIndex())).toString(), masaPajakDari, masaPajakSampai);
					}
					else{
						result = ControllerFactory.getMainController().getCpPeriksaDAOImpl().GetNotaSKPDLBCovList(cmbKewajibanPajak.getData(Integer.toString(cmbKewajibanPajak.getSelectionIndex())).toString(), 
								masaPajakDari, masaPajakSampai);
						
					}
				}
				if (result != null){
					UIController.INSTANCE.createColumn(tblNPPD, result);
					if (btnSkpdlbcov.getSelection()){
						UIController.INSTANCE.loadDataSKPDLBCov(tblNPPD, result);
					}
					else{
						UIController.INSTANCE.loadData(tblNPPD, result);
					}
				}
				if (tblNPPD.getItemCount() < 1)
					MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Data tidak ada");
			}
		});
		GridData gd_btnLihat = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnLihat.widthHint = 90;
		btnLihat.setLayoutData(gd_btnLihat);
		btnLihat.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BORDER));
		btnLihat.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnLihat.setText("Lihat");
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		
		Label lblPenandaTangan = new Label(comp, SWT.NONE);
		lblPenandaTangan.setForeground(fontColor);
		lblPenandaTangan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblPenandaTangan.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPenandaTangan.setText("Penanda Tangan 1");
		
		cmbPenandatangan1 = new Combo(comp, SWT.READ_ONLY);
		cmbPenandatangan1.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		cmbPenandatangan1.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		cmbPenandatangan1.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		GridData gd_cmbPenandatangan1 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_cmbPenandatangan1.widthHint = 250;
		loadPejabat(cmbPenandatangan1);
		cmbPenandatangan1.setLayoutData(gd_cmbPenandatangan1);
		
		Label lblPenandaTangan_1 = new Label(comp, SWT.NONE);
		lblPenandaTangan_1.setForeground(fontColor);
		lblPenandaTangan_1.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblPenandaTangan_1.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		lblPenandaTangan_1.setText("Penanda Tangan 2");
		
		cmbPenandatangan2 = new Combo(comp, SWT.READ_ONLY);
		cmbPenandatangan2.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		cmbPenandatangan2.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		GridData gd_cmbPenandatangan2 = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_cmbPenandatangan2.widthHint = 250;
		loadPejabat(cmbPenandatangan2);
		cmbPenandatangan2.setLayoutData(gd_cmbPenandatangan2);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		
		btnPlt = new Button(comp, SWT.CHECK);
		btnPlt.setText("plt.");
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		
		btnPlt2 = new Button(comp, SWT.CHECK);
		btnPlt2.setText("plt.");
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		
		Composite compButton = new Composite(comp, SWT.NONE);
		compButton.setLayout(new GridLayout(2, false));
		
		Button btnCetakNppd = new Button(compButton, SWT.NONE);
		btnCetakNppd.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BORDER));
		btnCetakNppd.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnCetakNppd.setText("Cetak NPPD");
		btnCetakNppd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(isPrint()){
					if (cmbPenandatangan1.getText().equalsIgnoreCase("") || cmbPenandatangan2.getText().equalsIgnoreCase("")){
						MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Silahkan isi nama pejabat");
					}
					else{
						if (btnSkpd.getSelection()){
							ReportAppContext.name = "Variable";
							ReportAppContext.nameObject = "NotaSKPD";
							ReportAppContext.classLoader = NotaPerhitunganView.class.getClassLoader();
							if(!ReportAppContext.object.isEmpty())
								ReportAppContext.object.clear();
							for(int j = 0;j<tblNPPD.getItems().length;j++){// untuk masukan row table
								List<String> values = new ArrayList<String>();
								values.add(tblNPPD.getItem(j).getText(9));
								values.add(tblNPPD.getItem(j).getText(4));
								values.add(tblNPPD.getItem(j).getText(1));
								values.add(tblNPPD.getItem(j).getText(8));
								values.add(tblNPPD.getItem(j).getText(19));
								values.add(getNPWPDFormat(tblNPPD.getItem(j).getText(3)));
								values.add(tblNPPD.getItem(j).getText(5));
								values.add(tblNPPD.getItem(j).getText(13));
								values.add(tblNPPD.getItem(j).getText(14).substring(2));
								values.add(tblNPPD.getItem(j).getText(15).substring(2));
								values.add(tblNPPD.getItem(j).getText(16).substring(2));
								values.add(tblNPPD.getItem(j).getText(17).substring(2));
								values.add(tblNPPD.getItem(j).getText(18).substring(2));
								values.add(tblNPPD.getItem(j).getText(2));
								try {
									values.add(new ReportModule().getTerbilang(indFormat.parse(tblNPPD.getItem(j).getText(18)).doubleValue()));
								} catch (ParseException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
								values.add(tblNPPD.getItem(j).getText(12).substring(2));
								String pangkat1 = "";
								String pangkat2 = "";
								if (masaPajakDari.after(new Date(117, 1, 19))){
									pangkat1 = listPejabat.get(cmbPenandatangan1.getSelectionIndex()).getJabatan();
									pangkat2 = listPejabat.get(cmbPenandatangan2.getSelectionIndex()).getJabatan();
								}
								else{
									pangkat2 = "Kepala Bidang Pendataan dan Penetapan";
									pangkat1 = "Kepala Seksi Penetapan";
								}
								
								ReportAppContext.map.put("Nama1", listPejabat.get(cmbPenandatangan1.getSelectionIndex()).getNamaPejabat());
								ReportAppContext.map.put("NIP1", listPejabat.get(cmbPenandatangan1.getSelectionIndex()).getIdPejabatNIP());
								ReportAppContext.map.put("Pangkat1", listPejabat.get(cmbPenandatangan1.getSelectionIndex()).getPangkat());
								if (btnPlt.getSelection() == true){
									pangkat1 = "Plt. "+pangkat1;
								}
								ReportAppContext.map.put("Jabatan1", pangkat1);
								ReportAppContext.map.put("Nama2", listPejabat.get(cmbPenandatangan2.getSelectionIndex()).getNamaPejabat());
								ReportAppContext.map.put("NIP2", listPejabat.get(cmbPenandatangan2.getSelectionIndex()).getIdPejabatNIP());
								ReportAppContext.map.put("Pangkat2", listPejabat.get(cmbPenandatangan2.getSelectionIndex()).getPangkat());
								if (btnPlt2.getSelection() == true){
									pangkat2 = "Plt. "+pangkat2;
								}
								ReportAppContext.map.put("Jabatan2", pangkat2);
								ReportAppContext.object.put(Integer.toString(j), values);
							}
							ReportAction.start("NotaSKPD.rptdesign");
						}
						else if (btnSkpdlbcov.getSelection()){
							ReportAppContext.name = "Variable";
							ReportAppContext.nameObject = "NotaSKPDLBCov";
							ReportAppContext.classLoader = NotaPerhitunganView.class.getClassLoader();
							if(!ReportAppContext.object.isEmpty())
								ReportAppContext.object.clear();
							Date date = new Date();				
							SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy");
							SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy");
							String noNPPDCov = "";
							for(int j = 0;j<tblNPPD.getItems().length;j++){// untuk masukan row table
								List<String> values = new ArrayList<String>();
								values.add(tblNPPD.getItem(j).getText(1));
								noNPPDCov = tblNPPD.getItem(j).getText(1).substring(0, tblNPPD.getItem(j).getText(1).indexOf("S"))+"/NPPD/"+Calendar.getInstance().get(Calendar.YEAR);
								values.add(noNPPDCov);
								values.add(tblNPPD.getItem(j).getText(3));
								values.add(tblNPPD.getItem(j).getText(4));
								values.add(tblNPPD.getItem(j).getText(5));
								values.add(tblNPPD.getItem(j).getText(6));
								values.add(tblNPPD.getItem(j).getText(7));
								values.add(tblNPPD.getItem(j).getText(8));
								values.add(tblNPPD.getItem(j).getText(9));
								values.add(tblNPPD.getItem(j).getText(10));
								values.add(tblNPPD.getItem(j).getText(11));
								values.add(tblNPPD.getItem(j).getText(12));
								values.add(tblNPPD.getItem(j).getText(13));
								values.add(tblNPPD.getItem(j).getText(14));
								values.add(tblNPPD.getItem(j).getText(15));
								values.add(tblNPPD.getItem(j).getText(16));
								values.add(tblNPPD.getItem(j).getText(17));
								values.add(tblNPPD.getItem(j).getText(18));
								values.add(tblNPPD.getItem(j).getText(19));
								
								//values.add(getNPWPDFormat(tblNPPD.getItem(j).getText(3)));
								//values.add(tblNPPD.getItem(j).getText(5));
								//values.add(tblNPPD.getItem(j).getText(13));
								//values.add(tblNPPD.getItem(j).getText(14).substring(2));
								//values.add(tblNPPD.getItem(j).getText(15).substring(2));
								//values.add(tblNPPD.getItem(j).getText(16).substring(2));
								//values.add(tblNPPD.getItem(j).getText(17).substring(2));
								//values.add(tblNPPD.getItem(j).getText(18).substring(2));
								//values.add(tblNPPD.getItem(j).getText(2));
								try {
									values.add(new ReportModule().getTerbilang(indFormat.parse(tblNPPD.getItem(j).getText(19)).doubleValue()));
								} catch (ParseException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
								values.add((tblNPPD.getItem(j).getText(1)).substring(0, ((tblNPPD.getItem(j).getText(3)).indexOf("/")))+"/"+(((values.get(11)).substring(6,11)).replace(".",""))
										+"/NPPD/"+formatter2.format(date));
								//values.add(tblNPPD.getItem(j).getText(12).substring(2));
								values.add(formatter.format(date));
								String pangkat1 = "";
								String pangkat2 = "";
								if (masaPajakDari.after(new Date(117, 1, 19))){
									pangkat1 = listPejabat.get(cmbPenandatangan1.getSelectionIndex()).getJabatan();
									pangkat2 = listPejabat.get(cmbPenandatangan2.getSelectionIndex()).getJabatan();
								}
								else{
									pangkat2 = "Kepala Bidang Pendataan dan Penetapan";
									pangkat1 = "Kepala Seksi Penetapan";
								}
								
								ReportAppContext.map.put("Nama1", listPejabat.get(cmbPenandatangan1.getSelectionIndex()).getNamaPejabat());
								ReportAppContext.map.put("nip1", listPejabat.get(cmbPenandatangan1.getSelectionIndex()).getIdPejabatNIP());
								ReportAppContext.map.put("pangkat1", listPejabat.get(cmbPenandatangan1.getSelectionIndex()).getPangkat());
								if (btnPlt.getSelection() == true){
									pangkat1 = "Plt. "+pangkat1;
								}
								ReportAppContext.map.put("jabatan1", pangkat1);
								ReportAppContext.map.put("Nama2", listPejabat.get(cmbPenandatangan2.getSelectionIndex()).getNamaPejabat());
								ReportAppContext.map.put("nip2", listPejabat.get(cmbPenandatangan2.getSelectionIndex()).getIdPejabatNIP());
								ReportAppContext.map.put("pangkat2", listPejabat.get(cmbPenandatangan2.getSelectionIndex()).getPangkat());
								if (btnPlt2.getSelection() == true){
									pangkat2 = "Plt. "+pangkat2;
								}
								ReportAppContext.map.put("jabatan2", pangkat2);
								ReportAppContext.object.put(Integer.toString(j), values);
								//System.out.println(values);
							}
							ReportAction.start("NotaSKPDLBCov.rptdesign");
						}
						else{
							ReportAppContext.name = "Variable";
							ReportAppContext.nameObject = "NotaSKPD";
							ReportAppContext.classLoader = NotaPerhitunganView.class.getClassLoader();
							if(!ReportAppContext.object.isEmpty())
								ReportAppContext.object.clear();
							for(int j = 0;j<tblNPPD.getItems().length;j++){// untuk masukan row table
								List<String> values = new ArrayList<String>();
								for(int i=0;i<tblNPPD.getColumnCount();i++){// untuk masukkan data berdasarkan colom table
									if (btnSkpdkbt.getSelection()){
										if (i == 3)
											values.add(getNPWPDFormat(tblNPPD.getItem(j).getText(i)));
										else
											if (tblNPPD.getItem(j).getText(i).startsWith("Rp"))
												values.add(tblNPPD.getItem(j).getText(i).substring(2));
											else
												values.add(tblNPPD.getItem(j).getText(i));
									}
									else{
										if (i == 8)
											values.add(getNPWPDFormat(tblNPPD.getItem(j).getText(i)));
										else
											if (tblNPPD.getItem(j).getText(i).startsWith("Rp"))
												values.add(tblNPPD.getItem(j).getText(i).substring(2));
											else if (tblNPPD.getItem(j).getText(i).startsWith("-Rp"))
												values.add("0.00");
											else
												values.add(tblNPPD.getItem(j).getText(i));
									}
								}
								try {
									if (btnSkpdkb.getSelection())
										values.add(new ReportModule().getTerbilang(indFormat.parse(tblNPPD.getItem(j).getText(21)).doubleValue()));
									else if (btnSkpdkbt.getSelection())
										values.add(new ReportModule().getTerbilang(indFormat.parse(tblNPPD.getItem(j).getText(15)).doubleValue()));
									else if (btnSkpdn.getSelection())
										values.add(new ReportModule().getTerbilang(indFormat.parse(tblNPPD.getItem(j).getText(20)).doubleValue()));
								} catch (ParseException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
								String pangkat1 = "";
								String pangkat2 = "";
								if (masaPajakDari.after(new Date(117, 1, 19))){
									pangkat1 = listPejabat.get(cmbPenandatangan1.getSelectionIndex()).getJabatan();
									pangkat2 = listPejabat.get(cmbPenandatangan2.getSelectionIndex()).getJabatan();
								}
								else{
									pangkat2 = "Kepala Bidang Pendataan dan Penetapan";
									pangkat1 = "Kepala Seksi Penetapan";
								}
								
								ReportAppContext.map.put("Nama1", listPejabat.get(cmbPenandatangan1.getSelectionIndex()).getNamaPejabat());
								ReportAppContext.map.put("NIP1", listPejabat.get(cmbPenandatangan1.getSelectionIndex()).getIdPejabatNIP());
								ReportAppContext.map.put("Pangkat1", listPejabat.get(cmbPenandatangan1.getSelectionIndex()).getPangkat());
								if (btnPlt.getSelection() == true){
									pangkat1 = "Plt. "+pangkat1;
								}
								ReportAppContext.map.put("Jabatan1", pangkat1);
								ReportAppContext.map.put("Nama2", listPejabat.get(cmbPenandatangan2.getSelectionIndex()).getNamaPejabat());
								ReportAppContext.map.put("NIP2", listPejabat.get(cmbPenandatangan2.getSelectionIndex()).getIdPejabatNIP());
								ReportAppContext.map.put("Pangkat2", listPejabat.get(cmbPenandatangan2.getSelectionIndex()).getPangkat());
								if (btnPlt2.getSelection() == true){
									pangkat2 = "Plt. "+pangkat2;
								}
								ReportAppContext.map.put("Jabatan2", pangkat2);
								ReportAppContext.object.put(Integer.toString(j), values);
							}
							if (btnSkpdkb.getSelection())
								ReportAction.start("NotaSKPDKB.rptdesign");
							else if (btnSkpdkbt.getSelection())
								ReportAction.start("NotaSKPDKBT.rptdesign");
							else if (btnSkpdn.getSelection())
								ReportAction.start("NotaSKPDN.rptdesign");
						}				
					}
				}else
					MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Anda tidak memiliki hak untuk mencetak.");
			}
		});
		
		Button btnCetakDaftarNppd = new Button(compButton, SWT.NONE);
		btnCetakDaftarNppd.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BORDER));
		btnCetakDaftarNppd.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnCetakDaftarNppd.setText("Cetak Daftar NPPD");
		btnCetakDaftarNppd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Double total = 0.0;
				String JenisSKP = "";
				if (btnSkpd.getSelection())
					JenisSKP = "SKPD";
				else if (btnSkpdkb.getSelection())
					JenisSKP = "SKPDKB";
				else if (btnSkpdkbt.getSelection())
					JenisSKP = "SKPDKBT";
				else if (btnSkpdn.getSelection())
					JenisSKP = "SKPDN";
				
				if(isPrint()){
					if (btnSkpd.getSelection()){
						ReportAppContext.name = "Variable";
						ReportAppContext.nameObject = "DaftarNPPDSKPD";
						ReportAppContext.classLoader = NotaPerhitunganView.class.getClassLoader();
						if(!ReportAppContext.object.isEmpty())
							ReportAppContext.object.clear();
						for(int j = 0;j<tblNPPD.getItems().length;j++){
							List<String> values = new ArrayList<String>();
							values.add(String.valueOf(j+1));
							values.add(tblNPPD.getItem(j).getText(1));
							values.add(tblNPPD.getItem(j).getText(6));
							values.add(tblNPPD.getItem(j).getText(3));
							values.add(tblNPPD.getItem(j).getText(4));
							values.add(tblNPPD.getItem(j).getText(5));
							values.add(tblNPPD.getItem(j).getText(9));
							values.add(tblNPPD.getItem(j).getText(14).substring(2).replace(".", "").replace(",", "."));
							values.add(tblNPPD.getItem(j).getText(17).substring(2).replace(".", "").replace(",", "."));
							values.add(tblNPPD.getItem(j).getText(15).substring(2).replace(".", "").replace(",", "."));
							values.add(tblNPPD.getItem(j).getText(18).substring(2).replace(".", "").replace(",", "."));
							values.add(tblNPPD.getItem(j).getText(11));
							values.add(tblNPPD.getItem(j).getText(19));
							try {
								total += indFormat.parse(tblNPPD.getItem(j).getText(18)).doubleValue();
							} catch (ParseException e1) {
								e1.printStackTrace();
							}
							ReportAppContext.object.put(Integer.toString(j), values);
						}
						ReportAppContext.map.put("TotalPajak", indFormat.format(total));
						ReportAppContext.map.put("MasaPajak", masaPajak);
						ReportAppContext.map.put("JenisSKP", JenisSKP);
						ReportAction.start("DaftarNPPDSKPD.rptdesign");
					}
					else if (btnSkpdlbcov.getSelection())
						MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Mohon maaf, fitur ini belum tersedia");
					else{
						ReportAppContext.name = "Variable";
						ReportAppContext.nameObject = "DaftarNPPD";
						ReportAppContext.classLoader = NotaPerhitunganView.class.getClassLoader();
						if(!ReportAppContext.object.isEmpty())
							ReportAppContext.object.clear();
						if (btnSkpdkbt.getSelection()){
							for(int j = 0;j<tblNPPD.getItems().length;j++){
								List<String> values = new ArrayList<String>();
								values.add(String.valueOf(j+1));
								values.add(tblNPPD.getItem(j).getText(1));
								values.add(tblNPPD.getItem(j).getText(6));
								values.add(tblNPPD.getItem(j).getText(3));
								values.add(tblNPPD.getItem(j).getText(4));
								values.add(tblNPPD.getItem(j).getText(5));
								values.add(tblNPPD.getItem(j).getText(8));
								values.add(tblNPPD.getItem(j).getText(11).substring(2).replace(".", "").replace(",", "."));
								values.add(tblNPPD.getItem(j).getText(12).substring(2).replace(".", "").replace(",", "."));
								values.add(tblNPPD.getItem(j).getText(13).substring(2).replace(".", "").replace(",", "."));
								values.add(indFormat.format(0).substring(2).replace(".", "").replace(",", "."));
								values.add(indFormat.format(0).substring(2).replace(".", "").replace(",", "."));
								values.add(tblNPPD.getItem(j).getText(14).substring(2).replace(".", "").replace(",", "."));
								values.add(indFormat.format(0).substring(2).replace(".", "").replace(",", "."));
								values.add(indFormat.format(0).substring(2).replace(".", "").replace(",", "."));
								values.add(tblNPPD.getItem(j).getText(15).substring(2).replace(".", "").replace(",", "."));
								values.add(tblNPPD.getItem(j).getText(9));
								values.add(tblNPPD.getItem(j).getText(16));
								try {
									total += indFormat.parse(tblNPPD.getItem(j).getText(15)).doubleValue();
								} catch (ParseException e1) {
									e1.printStackTrace();
								}
								ReportAppContext.object.put(Integer.toString(j), values);
							}
						}
						else {
							for(int j = 0;j<tblNPPD.getItems().length;j++){
								List<String> values = new ArrayList<String>();
								values.add(String.valueOf(j+1));
								values.add(tblNPPD.getItem(j).getText(2));
								values.add(tblNPPD.getItem(j).getText(1));
								values.add(tblNPPD.getItem(j).getText(8));
								values.add(tblNPPD.getItem(j).getText(6));
								values.add(tblNPPD.getItem(j).getText(7));
								values.add(tblNPPD.getItem(j).getText(11));
								values.add(tblNPPD.getItem(j).getText(13).substring(2).replace(".", "").replace(",", "."));
								values.add(tblNPPD.getItem(j).getText(14).substring(2).replace(".", "").replace(",", "."));
								values.add(tblNPPD.getItem(j).getText(15).substring(2).replace(".", "").replace(",", "."));
								values.add(tblNPPD.getItem(j).getText(16).substring(2).replace(".", "").replace(",", "."));
								if (btnSkpdn.getSelection()){
									values.add(indFormat.format(0).substring(2).replace(".", "").replace(",", "."));
									values.add(tblNPPD.getItem(j).getText(17).substring(2).replace(".", "").replace(",", "."));
									values.add(tblNPPD.getItem(j).getText(18).substring(2).replace(".", "").replace(",", "."));
									values.add(tblNPPD.getItem(j).getText(19).substring(2).replace(".", "").replace(",", "."));
									values.add(tblNPPD.getItem(j).getText(20).substring(2).replace(".", "").replace(",", "."));
									try {
										total += indFormat.parse(tblNPPD.getItem(j).getText(20)).doubleValue();
									} catch (ParseException e1) {
										e1.printStackTrace();
									}
								}
								else if (btnSkpdkb.getSelection()){
									values.add(tblNPPD.getItem(j).getText(17).substring(2).replace(".", "").replace(",", "."));
									values.add(tblNPPD.getItem(j).getText(18).substring(2).replace(".", "").replace(",", "."));
									values.add(tblNPPD.getItem(j).getText(19).substring(2).replace(".", "").replace(",", "."));
									values.add(tblNPPD.getItem(j).getText(20).substring(2).replace(".", "").replace(",", "."));
									values.add(tblNPPD.getItem(j).getText(21).substring(2).replace(".", "").replace(",", "."));
									try {
										total += indFormat.parse(tblNPPD.getItem(j).getText(21)).doubleValue();
									} catch (ParseException e1) {
										e1.printStackTrace();
									}
								}
								values.add(tblNPPD.getItem(j).getText(10));
								values.add(tblNPPD.getItem(j).getText(3));
								ReportAppContext.object.put(Integer.toString(j), values);
							}
						}
						ReportAppContext.map.put("TotalPajak", indFormat.format(total));
						ReportAppContext.map.put("MasaPajak", masaPajak);
						ReportAppContext.map.put("JenisSKP", JenisSKP);
						ReportAction.start("DaftarNPPD.rptdesign");
					}
					
				}else
					MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Anda tidak memiliki hak untuk mencetak.");
			}
		});
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		
		tblNPPD = new Table(comp, SWT.BORDER | SWT.FULL_SELECTION | SWT.VIRTUAL);
		tblNPPD.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		tblNPPD.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE-1, SWT.NORMAL));
		tblNPPD.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 9, 1));
		tblNPPD.setHeaderVisible(true);
		tblNPPD.setLinesVisible(true);
		
		scrolledComposite.setContent(comp);
		scrolledComposite.setMinSize(comp.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
	}
	
	@SuppressWarnings("deprecation")
	public void setMasaPajak()
	{
		
		if (btnTanggal.getSelection())
		{
			Calendar instance = Calendar.getInstance();
			instance.set(Calendar.DAY_OF_MONTH, calNPPD.getDay());
			instance.set(Calendar.MONTH, calNPPD.getMonth());
			instance.set(Calendar.YEAR, calNPPD.getYear());
			masaPajakDari = new Date(calNPPD.getYear() - 1900, calNPPD.getMonth(), calNPPD.getDay());
			masaPajakSampai = masaPajakDari;
			masaPajak = masaPajakDari.getDate() + " " + UIController.INSTANCE.formatMonth(masaPajakDari.getMonth() + 1, Locale.getDefault()) + " " + (masaPajakDari.getYear() + 1900);
		}
		if (btnBulan.getSelection())
		{
			Calendar instance = Calendar.getInstance();
			instance.set(Calendar.DAY_OF_MONTH, monthNPPD.getDay());
			instance.set(Calendar.MONTH, monthNPPD.getMonth());
			instance.set(Calendar.YEAR, monthNPPD.getYear());
			masaPajakDari = new Date(monthNPPD.getYear() - 1900, monthNPPD.getMonth(), 1);
			masaPajakSampai = new Date(monthNPPD.getYear() - 1900, monthNPPD.getMonth(), instance.getActualMaximum(Calendar.DATE));
			masaPajak = masaPajakDari.getDate() + " " + UIController.INSTANCE.formatMonth(masaPajakDari.getMonth() + 1, Locale.getDefault()) + " " + (masaPajakDari.getYear() + 1900) + " s/d ";
			masaPajak += masaPajakSampai.getDate() + " " + UIController.INSTANCE.formatMonth(masaPajakSampai.getMonth() + 1, Locale.getDefault()) + " " + (masaPajakSampai.getYear() + 1900);
		}
		if (btnTahun.getSelection())
		{
			masaPajakDari = new Date(yearNPPD.getSelection() - 1900, 0, 1);
			masaPajakSampai = new Date(yearNPPD.getSelection() - 1900, 11, 31);
			masaPajak = masaPajakDari.getDate() + " " + UIController.INSTANCE.formatMonth(masaPajakDari.getMonth() + 1, Locale.getDefault()) + " " + (masaPajakDari.getYear() + 1900) + " s/d ";
			masaPajak += masaPajakSampai.getDate() + " " + UIController.INSTANCE.formatMonth(masaPajakSampai.getMonth() + 1, Locale.getDefault()) + " " + (masaPajakSampai.getYear() + 1900);
		}
	
	}
	
	private void loadPejabat(Combo cmb){
		listPejabat = ControllerFactory.getMainController().getCpPejabatDAOImpl().getAllPejabat();
		for (int i=0;i<listPejabat.size();i++){
			UIController.INSTANCE.loadPejabat(cmb, listPejabat.toArray());
		}
	}

	@Override
	public void setFocus() {
		
	}
	
	private UserModul userModul = ControllerFactory.getMainController().getCpUserDAOImpl()
			.getHMUserModul(GlobalVariable.userModul.getIdUser()).get(10); // 1 merupakan id sub modul.
	private Button btnPlt;
	private Button btnPlt2;
	
	private boolean isPrint(){		
		return userModul.getCetak();
	}
	
	private String getNPWPDFormat(String npwpd){
		String retValue = "";
		for (int i=0;i<npwpd.length();i++){
			if (i == 0 || i == 7 || i == 9)
				retValue += npwpd.substring(i, i+ 1) + "      ";
			else
				retValue += npwpd.substring(i, i+ 1) + "    ";
		}
		
		return retValue;
	}
}