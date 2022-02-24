package com.dispenda.skpdkb.views;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StackLayout;
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
import com.dispenda.model.BidangUsahaProvider;
import com.dispenda.model.PajakProvider;
import com.dispenda.model.Pejabat;
import com.dispenda.model.UserModul;
import com.dispenda.object.GlobalVariable;
import com.dispenda.object.Preferences;
import com.dispenda.report.ReportAction;
import com.dispenda.report.ReportAppContext;
import com.dispenda.report.ReportModule;

public class DaftarPemeriksaanKeuanganView extends ViewPart {
	public DaftarPemeriksaanKeuanganView() {
	}
	
	public static final String ID = DaftarPemeriksaanKeuanganView.class.getName();
	private Table tblSKP;
	private DateTime calSKP;
	private Spinner yearSKP;
	private DateTime monthSKP;
	private Button btnTanggal;
	private Button btnBulan;
	private Button btnTahun;
	private Date masaPajakDari;
	private Date masaPajakSampai;
//	private String masaPajak;
	private Combo cmbPenandaTangan;
	private List<Pejabat> listPejabat;
	private Button btnSkpdkb;
	private Button btnSkpdn;
	private Button btnSkpdkbt;
	private Combo cmbKewajibanPajak;
	private Locale ind = new Locale("id", "ID");
	private NumberFormat indFormat = NumberFormat.getCurrencyInstance(ind);
	private java.sql.Timestamp dateNow;
	private Combo cmbSubPajak;
	private int idSubPajak;
	String tipeSKP = "";
	private Color fontColor = new Color(Display.getCurrent(),Preferences.FONT_COLOR);
	
	@SuppressWarnings("deprecation")
	@Override
	public void createPartControl(Composite parent) {
		ScrolledComposite scrolledComposite = new ScrolledComposite(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		Locale.setDefault(new Locale("id","ID"));
		dateNow = (java.sql.Timestamp)ControllerFactory.getMainController().getCpWajibPajakDAOImpl().getDateNow();
		
		Composite comp = new Composite(scrolledComposite, SWT.NONE);
		comp.setLayout(new GridLayout(5, false));
		
		Label lblJenisSkpd = new Label(comp, SWT.NONE);
		lblJenisSkpd.setForeground(fontColor);
		lblJenisSkpd.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblJenisSkpd.setText("Jenis SKPD");
		
		Group compJenisSKPD = new Group(comp, SWT.NONE);
		compJenisSKPD.setLayout(new GridLayout(3, false));
		
		btnSkpdkb = new Button(compJenisSKPD, SWT.RADIO);
		btnSkpdkb.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnSkpdkb.setText("SKPDKB");
		
		
		btnSkpdn = new Button(compJenisSKPD, SWT.RADIO);
		btnSkpdn.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnSkpdn.setText("SKPDN");
		
		
		btnSkpdkbt = new Button(compJenisSKPD, SWT.RADIO);
		btnSkpdkbt.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnSkpdkbt.setText("SKPDKBT");
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		
		tblDetailSPTPD = new Table(comp, SWT.BORDER | SWT.FULL_SELECTION);
		tblDetailSPTPD.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		tblDetailSPTPD.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 6));
		tblDetailSPTPD.setHeaderVisible(true);
		tblDetailSPTPD.setLinesVisible(true);
		
		
		Label lblKewajibanPajak = new Label(comp, SWT.NONE);
		lblKewajibanPajak.setForeground(fontColor);
		lblKewajibanPajak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblKewajibanPajak.setText("Kewajiban Pajak");
		
		cmbKewajibanPajak = new Combo(comp, SWT.READ_ONLY);
		cmbKewajibanPajak.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				idSubPajak = 0;
				UIController.INSTANCE.loadBidangUsaha(cmbSubPajak, BidangUsahaProvider.INSTANCE.getBidangUsaha((Integer)cmbKewajibanPajak.getData(Integer.toString(cmbKewajibanPajak.getSelectionIndex()))).toArray());
			}
		});
		cmbKewajibanPajak.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_FOREGROUND));
		cmbKewajibanPajak.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		cmbKewajibanPajak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		GridData gd_cmbKewajibanPajak = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_cmbKewajibanPajak.widthHint = 250;
		UIController.INSTANCE.loadPajak(cmbKewajibanPajak, PajakProvider.INSTANCE.getPajak().toArray());
		cmbKewajibanPajak.setLayoutData(gd_cmbKewajibanPajak);
		
		Label label = new Label(comp, SWT.NONE);
		label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label.setText("-");
		
		cmbSubPajak = new Combo(comp, SWT.READ_ONLY);
		cmbSubPajak.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				idSubPajak = (Integer)cmbSubPajak.getData(Integer.toString(cmbSubPajak.getSelectionIndex()));
			}
		});
		GridData gd_cmbSubPajak = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_cmbSubPajak.widthHint = 320;
		cmbSubPajak.setLayoutData(gd_cmbSubPajak);
		cmbSubPajak.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_FOREGROUND));
		cmbSubPajak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		cmbSubPajak.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		
		Label lblTanggalSkpdkbnkbt = new Label(comp, SWT.NONE);
		lblTanggalSkpdkbnkbt.setForeground(fontColor);
		lblTanggalSkpdkbnkbt.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblTanggalSkpdkbnkbt.setText("Tanggal SKPD(KB/N/KBT)");
		
		Group group = new Group(comp, SWT.NONE);
		GridData gd_group = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_group.heightHint = 80;
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
		btnTanggal.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BORDER));
		btnTanggal.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		btnBulan = new Button(group, SWT.RADIO);
		btnBulan.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				monthSKP.setVisible(true);
				calSKP.setVisible(false);
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
				yearSKP.setVisible(true);
				calSKP.setVisible(false);
				monthSKP.setVisible(false);
				setMasaPajak();
			}
		});
		btnTahun.setText("Tahun");
		btnTahun.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BORDER));
		btnTahun.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		Composite composite = new Composite(group, SWT.NONE);
		composite.setLayout(new StackLayout());
		composite.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 3, 1));
		
		calSKP = new DateTime(composite, SWT.BORDER | SWT.DROP_DOWN);
		calSKP.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		calSKP.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setMasaPajak();
			}
		});
		calSKP.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BORDER));
		calSKP.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		yearSKP = new Spinner(composite, SWT.BORDER);
		yearSKP.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		yearSKP.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setMasaPajak();
			}
		});
		yearSKP.setVisible(false);
		yearSKP.setTextLimit(9999);
		yearSKP.setMaximum(dateNow.getYear() + 1900);
		yearSKP.setMinimum(2006);
		yearSKP.setSelection(dateNow.getYear() + 1900);
		yearSKP.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		monthSKP = new DateTime(composite, SWT.BORDER | SWT.DROP_DOWN | SWT.SHORT);
		monthSKP.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		monthSKP.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setMasaPajak();
			}
		});
		monthSKP.setVisible(false);
		monthSKP.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		monthSKP.setDay(1);
		new Label(group, SWT.NONE);
		new Label(group, SWT.NONE);
		new Label(group, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		
		Button btnLihat = new Button(comp, SWT.NONE);
		btnLihat.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				tblSKP.removeAll();
				deleteColumns();
				ResultSet resultSKP;
				if (!cmbKewajibanPajak.getText().equalsIgnoreCase("")){
					if (btnSkpdkb.getSelection()){
						tipeSKP = "SKPDKB";
						createColumnsSKP();
						if (cmbSubPajak.getText().equalsIgnoreCase(""))
							resultSKP = ControllerFactory.getMainController().getCpPeriksaDAOImpl().getSKPDKBList(cmbKewajibanPajak.getData(Integer.toString(cmbKewajibanPajak.getSelectionIndex())).toString(), masaPajakDari, masaPajakSampai);
						else
							resultSKP = ControllerFactory.getMainController().getCpPeriksaDAOImpl().getSKPDKBListSub(cmbKewajibanPajak.getData(Integer.toString(cmbKewajibanPajak.getSelectionIndex())).toString(), masaPajakDari, masaPajakSampai, idSubPajak);
						loadTableSKP(resultSKP);
					}
					else if (btnSkpdkbt.getSelection()){
						tipeSKP = "SKPDKBT";
						createColumnsSKPDKBT();
						resultSKP = ControllerFactory.getMainController().getCpPeriksaDAOImpl().getSKPDKBTList(cmbKewajibanPajak.getData(Integer.toString(cmbKewajibanPajak.getSelectionIndex())).toString(), masaPajakDari, masaPajakSampai);
						loadTableSKPDKBT(resultSKP);
					}
					else if (btnSkpdn.getSelection()){
						tipeSKP = "SKPDN";
						createColumnsSKP();
						if (cmbSubPajak.getText().equalsIgnoreCase(""))
							resultSKP = ControllerFactory.getMainController().getCpPeriksaDAOImpl().getSKPDNList(cmbKewajibanPajak.getData(Integer.toString(cmbKewajibanPajak.getSelectionIndex())).toString(), masaPajakDari, masaPajakSampai);
						else
							resultSKP = ControllerFactory.getMainController().getCpPeriksaDAOImpl().getSKPDNListSub(cmbKewajibanPajak.getData(Integer.toString(cmbKewajibanPajak.getSelectionIndex())).toString(), masaPajakDari, masaPajakSampai, idSubPajak);				
						loadTableSKP(resultSKP);
					}
					else
						MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Silahkan Pilih Tipe SKP");
				}
				else
					MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Silahkan Pilih Kewajiban Pajak");
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
		
		Label lblPenandaTangan = new Label(comp, SWT.NONE);
		lblPenandaTangan.setForeground(fontColor);
		lblPenandaTangan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblPenandaTangan.setText("Penanda Tangan");
		
		cmbPenandaTangan = new Combo(comp, SWT.READ_ONLY);
		cmbPenandaTangan.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
			}
		});
		cmbPenandaTangan.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_FOREGROUND));
		cmbPenandaTangan.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		cmbPenandaTangan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		GridData gd_cmbPenandaTangan = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_cmbPenandaTangan.widthHint = 250;
		cmbPenandaTangan.setLayoutData(gd_cmbPenandaTangan);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		loadPejabat();
		
		Composite compButton = new Composite(comp, SWT.NONE);
		compButton.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		compButton.setLayout(new GridLayout(2, false));
		
		Button btnCetakBlanko = new Button(compButton, SWT.NONE);
		btnCetakBlanko.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (cmbPenandaTangan.getText().equalsIgnoreCase("")){
					MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Silahkan isi nama pejabat");
				}
				else
				{
					if (btnSkpdkb.getSelection()){
						ReportAppContext.nameObject = "BlankoSKPDKB";
						ReportAppContext.classLoader = DaftarPemeriksaanKeuanganView.class.getClassLoader();
						if(!ReportAppContext.object.isEmpty())
							ReportAppContext.object.clear();
						for(int j = 0;j<tblSKP.getItems().length;j++){// untuk masukan row table
							List<String> values = new ArrayList<String>();
							for(int i=0;i<tblSKP.getColumnCount();i++){// untuk masukkan data berdasarkan colom table
								if (i == 8)
									values.add(getNPWPDFormat(tblSKP.getItem(j).getText(i)));
								else if (i == 10)
									values.add(GetNoRekFormat(tblSKP.getItem(j).getText(i)));
								else{
									if (tblSKP.getItem(j).getText(i).startsWith("Rp"))
										values.add(tblSKP.getItem(j).getText(i).substring(2));
									else
										values.add(tblSKP.getItem(j).getText(i));
								}
							}
							values.add(listPejabat.get(cmbPenandaTangan.getSelectionIndex()).getNamaPejabat());
							values.add(listPejabat.get(cmbPenandaTangan.getSelectionIndex()).getPangkat());
							values.add(listPejabat.get(cmbPenandaTangan.getSelectionIndex()).getIdPejabatNIP());
							values.add(getNoUrutFormat(tblSKP.getItem(j).getText(0)));
							try {
								values.add(new ReportModule().getTerbilang(indFormat.parse(tblSKP.getItem(j).getText(21)).doubleValue()));
							} catch (ParseException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							ReportAppContext.object.put(Integer.toString(j), values);
						}
						ReportAction.start("BlankoSKPDKB.rptdesign");
					}
					else if (btnSkpdn.getSelection()){
						ReportAppContext.nameObject = "BlankoSKPDN";
						ReportAppContext.classLoader = DaftarPemeriksaanKeuanganView.class.getClassLoader();
						if(!ReportAppContext.object.isEmpty())
							ReportAppContext.object.clear();
						for(int j = 0;j<tblSKP.getItems().length;j++){// untuk masukan row table
							List<String> values = new ArrayList<String>();
							for(int i=0;i<tblSKP.getColumnCount();i++){// untuk masukkan data berdasarkan colom table
								if (i == 8)
									values.add(getNPWPDFormat(tblSKP.getItem(j).getText(i)));
								else if (i == 10)
									values.add(GetNoRekFormat(tblSKP.getItem(j).getText(i)));
								else{
									if (tblSKP.getItem(j).getText(i).startsWith("Rp"))
										values.add(tblSKP.getItem(j).getText(i).substring(2));
									else
										values.add(tblSKP.getItem(j).getText(i));
								}
							}
							values.add(listPejabat.get(cmbPenandaTangan.getSelectionIndex()).getNamaPejabat());
							values.add(listPejabat.get(cmbPenandaTangan.getSelectionIndex()).getPangkat());
							values.add(listPejabat.get(cmbPenandaTangan.getSelectionIndex()).getIdPejabatNIP());
							values.add(getNoUrutFormat(tblSKP.getItem(j).getText(0)));
							ReportAppContext.object.put(Integer.toString(j), values);
						}
						ReportAction.start("BlankoSKPDN.rptdesign");
					}
					else if (btnSkpdkbt.getSelection()){
						ReportAppContext.nameObject = "BlankoSKPDKBT";
						ReportAppContext.classLoader = DaftarPemeriksaanKeuanganView.class.getClassLoader();
						if(!ReportAppContext.object.isEmpty())
							ReportAppContext.object.clear();
						for(int j = 0;j<tblSKP.getItems().length;j++){// untuk masukan row table
							List<String> values = new ArrayList<String>();
							for(int i=0;i<tblSKP.getColumnCount();i++){// untuk masukkan data berdasarkan colom table
								if (i == 6)
									values.add(getNPWPDFormat(tblSKP.getItem(j).getText(i)));
								else if (i == 7)
									values.add(GetNoRekFormat(tblSKP.getItem(j).getText(i)));
								else{
									if (tblSKP.getItem(j).getText(i).startsWith("Rp"))
										values.add(tblSKP.getItem(j).getText(i).substring(2));
									else
										values.add(tblSKP.getItem(j).getText(i));
								}
							}
							values.add(listPejabat.get(cmbPenandaTangan.getSelectionIndex()).getNamaPejabat());
							values.add(listPejabat.get(cmbPenandaTangan.getSelectionIndex()).getPangkat());
							values.add(listPejabat.get(cmbPenandaTangan.getSelectionIndex()).getIdPejabatNIP());
							values.add(getNoUrutFormat(tblSKP.getItem(j).getText(0)));
							try {
								values.add(new ReportModule().getTerbilang(indFormat.parse(tblSKP.getItem(j).getText(19)).doubleValue()));
							} catch (ParseException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							ReportAppContext.object.put(Integer.toString(j), values);
						}
						ReportAction.start("BlankoSKPDKBT.rptdesign");
					}
				}
								
			}
		});
		GridData gd_btnCetakBlanko = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnCetakBlanko.widthHint = 140;
		btnCetakBlanko.setLayoutData(gd_btnCetakBlanko);
		btnCetakBlanko.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BORDER));
		btnCetakBlanko.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnCetakBlanko.setText("Cetak Blanko");
		btnCetakBlanko.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(isPrint()){
					
				}else
					MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Anda tidak memiliki hak untuk mencetak.");
			}
		});
		
		Button btnCetakDaftar = new Button(compButton, SWT.NONE);
		btnCetakDaftar.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(isPrint()){
					ReportAppContext.name = "Variable";
					Double total = 0.0;
					String NamaSKPD = "Daftar Penerbitan " + tipeSKP +" Pajak " + cmbKewajibanPajak.getText() + " Tahun " + (masaPajakDari.getYear() + 1900);
					String masaPajak = "";
					//SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
					if (btnTanggal.getSelection())
						masaPajak = masaPajakDari.getDate() + " " + UIController.INSTANCE.formatMonth(masaPajakDari.getMonth() + 1, ind) + " " + (masaPajakDari.getYear() + 1900);
					else 
						masaPajak = masaPajakDari.getDate() + " " + UIController.INSTANCE.formatMonth(masaPajakDari.getMonth() + 1, ind) + " " + (masaPajakDari.getYear() + 1900) + " s/d " + masaPajakSampai.getDate() + " " + UIController.INSTANCE.formatMonth(masaPajakSampai.getMonth() + 1, ind) + " " + (masaPajakSampai.getYear() + 1900);
	//				else if (btnTahun.getSelection())
	//					masaPajak = yearSKP.getText();
					ReportAppContext.map.put("NamaSKPD", String.valueOf(NamaSKPD));
					ReportAppContext.map.put("MasaPajak", masaPajak);
					if (tipeSKP.equalsIgnoreCase("SKPDKBT")){
						ReportAppContext.nameObject = "DaftarSKPDKBT_tabel";
						ReportAppContext.classLoader = DaftarPemeriksaanKeuanganView.class.getClassLoader();
						if(!ReportAppContext.object.isEmpty())
							ReportAppContext.object.clear();
						for(int j = 0;j<tblSKP.getItems().length;j++){
							List<String> values = new ArrayList<String>();
							values.add(String.valueOf(j+1));
							values.add(tblSKP.getItem(j).getText(0));
							values.add(tblSKP.getItem(j).getText(1));
							values.add(tblSKP.getItem(j).getText(6));
							values.add(tblSKP.getItem(j).getText(3));
							values.add(tblSKP.getItem(j).getText(4));
							values.add(tblSKP.getItem(j).getText(5));
							values.add(tblSKP.getItem(j).getText(9));
							values.add(tblSKP.getItem(j).getText(10));
							values.add(tblSKP.getItem(j).getText(15).replace(".", "").replace(",", "."));
							values.add(tblSKP.getItem(j).getText(16).replace(".", "").replace(",", "."));
							values.add(tblSKP.getItem(j).getText(17).replace(".", "").replace(",", "."));
							values.add(tblSKP.getItem(j).getText(18).replace(".", "").replace(",", "."));
							values.add(tblSKP.getItem(j).getText(19).replace(".", "").replace(",", "."));
							values.add(tblSKP.getItem(j).getText(8));
							try {
								total += indFormat.parse(tblSKP.getItem(j).getText(19)).doubleValue();
							} catch (ParseException e1) {
								e1.printStackTrace();
							}
							ReportAppContext.object.put(Integer.toString(j), values);
						}
						ReportAppContext.map.put("Total", indFormat.format(total));
						ReportAction.start("DaftarSKPDKBT.rptdesign");
					}
					else{
						ReportAppContext.nameObject = "DaftarSKPDKB_tabel";
						ReportAppContext.classLoader = DaftarPemeriksaanKeuanganView.class.getClassLoader();
						if(!ReportAppContext.object.isEmpty())
							ReportAppContext.object.clear();
						for(int j = 0;j<tblSKP.getItems().length;j++){
							List<String> values = new ArrayList<String>();
							values.add(String.valueOf(j+1));
							values.add(tblSKP.getItem(j).getText(0));
							values.add(tblSKP.getItem(j).getText(3));
							values.add(tblSKP.getItem(j).getText(8));
							values.add(tblSKP.getItem(j).getText(5));
							values.add(tblSKP.getItem(j).getText(6));
							values.add(tblSKP.getItem(j).getText(7));
							values.add(tblSKP.getItem(j).getText(12));
							values.add(tblSKP.getItem(j).getText(1));
							values.add(tblSKP.getItem(j).getText(13).replace(".", "").replace(",", "."));
							values.add(tblSKP.getItem(j).getText(14).replace(".", "").replace(",", "."));
							if (tblSKP.getItem(j).getText(17).equalsIgnoreCase("-"))
								values.add(indFormat.format(0).replace(".", "").replace(",", "."));
							else
								values.add(tblSKP.getItem(j).getText(17).replace(".", "").replace(",", "."));
							values.add(tblSKP.getItem(j).getText(15).replace(".", "").replace(",", "."));
							values.add(tblSKP.getItem(j).getText(16).replace(".", "").replace(",", "."));
							values.add(tblSKP.getItem(j).getText(19).replace(".", "").replace(",", "."));
							values.add(tblSKP.getItem(j).getText(21).replace(".", "").replace(",", "."));
							values.add(tblSKP.getItem(j).getText(11));
							try {
								total += indFormat.parse(tblSKP.getItem(j).getText(21)).doubleValue();
							} catch (ParseException e1) {
								e1.printStackTrace();
							}
							ReportAppContext.object.put(Integer.toString(j), values);
						}
						ReportAppContext.map.put("Total", indFormat.format(total));
						ReportAction.start("DaftarSKPDKB.rptdesign");
					}
				}else
					MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Anda tidak memiliki hak untuk mencetak.");
			}
		});
		GridData gd_btnCetakDaftar = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnCetakDaftar.widthHint = 140;
		btnCetakDaftar.setLayoutData(gd_btnCetakDaftar);
		btnCetakDaftar.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BORDER));
		btnCetakDaftar.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnCetakDaftar.setText("Cetak Daftar");
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		
		tblSKP = new Table(comp, SWT.BORDER | SWT.FULL_SELECTION | SWT.VIRTUAL);
		tblSKP.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		tblSKP.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		tblSKP.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE-1, SWT.NORMAL));
		GridData gd_tblSKP = new GridData(SWT.FILL, SWT.FILL, true, true, 5, 1);
		gd_tblSKP.heightHint = 284;
		tblSKP.setLayoutData(gd_tblSKP);
		tblSKP.setHeaderVisible(true);
		tblSKP.setLinesVisible(true);
		tblSKP.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(tblDetailSPTPD.getItemCount()>0)
					tblDetailSPTPD.removeAll();
//				ControllerFactory.getMainController().
				String npwpd = tblSKP.getItem(tblSKP.getSelectionIndex()).getText(8);
				String masaPajak = tblSKP.getItem(tblSKP.getSelectionIndex()).getText(1);
				String[] split = masaPajak.split("-");
				Date date1,date2;
				try {
					date1 = new SimpleDateFormat("MMM", Locale.getDefault()).parse(split[0].substring(0, split[0].length()-5).trim());
					Calendar cal1 = Calendar.getInstance();
				    cal1.setTime(date1);
				    int month1 = cal1.get(Calendar.MONTH);
					java.sql.Date dateMPD = new java.sql.Date(Integer.parseInt(split[0].substring(split[0].length()-5).trim())-1900, month1, 1);
					
					date2 = new SimpleDateFormat("MMM", Locale.getDefault()).parse(split[1].substring(1, split[1].length()-5).trim());
					Calendar cal2 = Calendar.getInstance();
				    cal2.setTime(date2);
				    int month2 = cal2.get(Calendar.MONTH);
					java.sql.Date dateMPS = new java.sql.Date(Integer.parseInt(split[1].substring(split[1].length()-5).trim())-1900, month2, 1);
					
					
					loadDataTableDetail(npwpd, dateMPD, dateMPS);
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
			}
		});
		createColoumTableDetail();
//		tblSKP.setComparator(comparator);
//		getSite().setSelectionProvider(tableViewer);
		scrolledComposite.setContent(comp);
		scrolledComposite.setMinSize(comp.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
	}
	
	private void createColoumTableDetail(){
		String[] listColumn = {"No.", "Masa Pajak Dari", "Masa Pajak Sampai", "Masa Pajak", "No.SPTPD",	
				"Pajak Terutang", "Jumlah Bayar", "Pajak Periksa", "Kurang Bayar", "Bunga Persen", "Bunga", "Denda Tambahan",
				"Kenaikan", "Total Periksa"};
		for (int i=listColumn.length - 1;i>=0;i--)
		{
			TableColumn colPajak = new TableColumn(tblDetailSPTPD, SWT.NONE,0);
			colPajak.setText(listColumn[i]);
			colPajak.setWidth(120);
		}
	}
	
	@SuppressWarnings("unchecked")
	private void loadDataTableDetail(String npwpd, java.sql.Date masaPajakDari, java.sql.Date masaPajakSampai){
		@SuppressWarnings("unused")
		List<HashMap<String, Object>> listSPTPD = ControllerFactory.getMainController().getCpPeriksaDAOImpl().GetSPTPDPeriksa1Table(npwpd, masaPajakDari, masaPajakSampai);
		List<HashMap<String, Object>> listDetail = ControllerFactory.getMainController().getCpPeriksaDAOImpl().GetDetailPeriksa1Table(npwpd, masaPajakDari, masaPajakSampai);
		
		Iterator itSPTPD = listSPTPD.iterator();
		Iterator itDetail = listDetail.iterator();
		int i = 1;
		while(itSPTPD.hasNext()){
			HashMap<String, Object> mapSPTPD = (HashMap<String, Object>)itSPTPD.next();
			HashMap<String, Object> mapDetail = (HashMap<String, Object>)itDetail.next();
			TableItem item = new TableItem(tblDetailSPTPD, SWT.NONE);
			item.setText(0, i+".");
			Date masapajakdari = (Date)mapSPTPD.get("MASA_PAJAK_DARI");
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			item.setText(1, sdf.format(masapajakdari));
			item.setText(3, (String)mapSPTPD.get("MASAPAJAK"));
			item.setText(4, (String)mapSPTPD.get("NO_SPTPD"));
			Double pajakterhutang = (Double)mapSPTPD.get("PAJAKTERUTANG");
			item.setText(5, indFormat.format(pajakterhutang));
			Double jumlahbayar =(Double)mapSPTPD.get("JUMLAHBAYAR"); 
			item.setText(6, indFormat.format(jumlahbayar));
			Double pajakperiksa =(Double)mapDetail.get("PAJAK_PERIKSA"); 
			item.setText(7, indFormat.format(pajakperiksa));
			i++;
		}
	}

	@Override
	public void setFocus() {

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
	
	private void loadPejabat(){
		listPejabat = ControllerFactory.getMainController().getCpPejabatDAOImpl().getAllPejabat();
		for (int i=0;i<listPejabat.size();i++){
			UIController.INSTANCE.loadPejabat(cmbPenandaTangan, listPejabat.toArray());
		}
	}

	private void createColumnsSKP(){
		if(tblSKP.getColumnCount() <= 0){
			String[] listColumn = {"No SKP", "Masa Pajak", "Tipe SKP", "Tanggal SKP", "Tahun", "Nama Pemilik", "Nama Badan", "Alamat Badan", "NPWPD", "Jatuh Tempo", 
					"Kode Bidang Usaha", "Nama Bidang Usaha", "Nama Pajak", "Pajak Periksa", "Pajak Terutang", "Kurang Bayar", "Bunga", "Kenaikan 25", "Kenaikan", "Denda",
					"Biaya Administrasi", "Total"};
			for (int i=listColumn.length - 1;i>=0;i--)
			{
				TableColumn colPajak = new TableColumn(tblSKP, SWT.NONE,0);
				colPajak.setText(listColumn[i]);
				colPajak.setWidth(120);
				
			}
		}
	}
	
	private void createColumnsSKPDKBT(){
		if(tblSKP.getColumnCount() <= 0){
			String[] listColumn = {"No SKP", "Tanggal SKP", "Tahun", "Nama Pemilik", "Nama Badan", "Alamat Badan", "NPWPD", "Kode Bidang Usaha", "Nama Bidang Usaha", "Nama Pajak", 
					"Masa Pajak", "Masa Pajak Dari", "Masa Pajak Sampai", "Jatuh Tempo", "Tanggal Periksa", "Pajak Periksa", "Pajak Terutang", "Selisih Kurang", "Kenaikan", "Total"};
			for (int i=listColumn.length - 1;i>=0;i--)
			{
				TableColumn colPajak = new TableColumn(tblSKP, SWT.NONE,0);
				colPajak.setText(listColumn[i]);
				colPajak.setWidth(120);
				
			}
		}
	}
	
	private void deleteColumns(){
		for(TableColumn col : tblSKP.getColumns()){
			col.dispose();
		}
	}
	
	private void loadTableSKP(ResultSet resultSKP){
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
//		double GrandTotal = 0;
		try
		{
			while (resultSKP.next())
			{
				TableItem item = new TableItem(tblSKP, SWT.NONE);				
				item.setText(0, resultSKP.getString("No_SKP"));
				item.setText(1, getMasaPajak(resultSKP.getString("MasaPajak")));
				item.setText(2, resultSKP.getString("Tipe_SKP"));
				item.setText(3, formatter.format(resultSKP.getDate("tanggal_SKP")));
				item.setText(4, resultSKP.getString("Tahun"));
				item.setText(5, resultSKP.getString("Nama_Pemilik"));
				item.setText(6, resultSKP.getString("Nama_Badan"));
				item.setText(7, resultSKP.getString("Alabad_Jalan"));
				item.setText(8, resultSKP.getString("NPWPD"));
				item.setText(9, formatter.format(resultSKP.getDate("TanggalJatuhTempo")));
				item.setText(10, resultSKP.getString("Kode_Bid_Usaha"));
				item.setText(11, resultSKP.getString("Nama_Bid_Usaha"));
				item.setText(12, resultSKP.getString("Nama_Pajak"));
				item.setText(13, indFormat.format(resultSKP.getDouble("PajakPeriksa")));
				item.setText(14, indFormat.format(resultSKP.getDouble("PajakTerutang")));
				item.setText(15, indFormat.format(resultSKP.getDouble("KurangBayar")));
				item.setText(16, indFormat.format(resultSKP.getDouble("Bunga")));
				if (btnSkpdkb.getSelection())
					item.setText(17, indFormat.format(resultSKP.getDouble("Kenaikan25")));
				else if (btnSkpdn.getSelection())
					item.setText(17, "-");
				item.setText(18, indFormat.format(resultSKP.getDouble("Kenaikan")));
				item.setText(19, indFormat.format(resultSKP.getDouble("Denda")));
				item.setText(20, indFormat.format(resultSKP.getDouble("BiayaAdministrasi")));
				item.setText(21, indFormat.format(resultSKP.getDouble("Total")));
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void loadTableSKPDKBT(ResultSet resultSKP){
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
//		double GrandTotal = 0;
		try
		{
			while (resultSKP.next())
			{
				TableItem item = new TableItem(tblSKP, SWT.NONE);				
				item.setText(0, resultSKP.getString("No_SKP"));
				item.setText(1, formatter.format(resultSKP.getDate("tanggal_SKP")));
				item.setText(2, resultSKP.getString("Tahun"));
				item.setText(3, resultSKP.getString("Nama_Pemilik"));
				item.setText(4, resultSKP.getString("Nama_Badan"));
				item.setText(5, resultSKP.getString("Alabad_Jalan"));
				item.setText(6, resultSKP.getString("NPWPD"));
				item.setText(7, resultSKP.getString("Kode_Bid_Usaha"));
				item.setText(8, resultSKP.getString("Nama_Bid_Usaha"));
				item.setText(9, resultSKP.getString("Nama_Pajak"));
				item.setText(10, getMasaPajak(resultSKP.getString("MasaPajak")));
				item.setText(11, formatter.format(resultSKP.getDate("Masa_Pajak_Dari")));
				item.setText(12, formatter.format(resultSKP.getDate("Masa_Pajak_Sampai")));
				item.setText(13, formatter.format(resultSKP.getDate("TanggalJatuhTempo")));
				item.setText(14, formatter.format(resultSKP.getDate("Tanggal_Periksa")));
				item.setText(15, indFormat.format(resultSKP.getDouble("PajakPeriksa")));
				item.setText(16, indFormat.format(resultSKP.getDouble("PajakTerutang")));
				item.setText(17, indFormat.format(resultSKP.getDouble("SelisihKurang")));
				item.setText(18, indFormat.format(resultSKP.getDouble("Kenaikan")));
				item.setText(19, indFormat.format(resultSKP.getDouble("Total")));
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private String getMasaPajak(String masaPajak){
		String arrMasaPajak[] =  masaPajak.split("-");
		String masaDari = UIController.INSTANCE.formatMonth(Integer.parseInt(arrMasaPajak[0].split(" ")[0]), ind) + " " + arrMasaPajak[0].split(" ")[1];
		String masaSampai = UIController.INSTANCE.formatMonth(Integer.parseInt(arrMasaPajak[1].split(" ")[0]), ind) + " " + arrMasaPajak[1].split(" ")[1];
		String retValue = masaDari + " - " + masaSampai;		
		return retValue;
	}

	private String getNoUrutFormat(String noSKP){
		int idx = noSKP.indexOf("/");
		String noUrut = noSKP.substring(0, idx);
		noUrut = String.format("%06d",Integer.parseInt(noUrut));
//		noUrut = ("000000" + noUrut).substring(noUrut.length());
		String retValue = "";
		for (int i=0;i<noUrut.length();i++) {
			retValue += noUrut.substring(i, i+1) + "   ";
		}
		
		return retValue;
	}
	
	private String GetNoRekFormat(String NoRek){
		String noRekening = NoRek.replace(".", "");
		String retValue = "";
		for (int i=0;i<noRekening.length();i++) {
			retValue += noRekening.substring(i, i+1) + "   ";
		}
		
		return retValue;
	}
	
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
	
	private UserModul userModul = ControllerFactory.getMainController().getCpUserDAOImpl()
			.getHMUserModul(GlobalVariable.userModul.getIdUser()).get(9); // 1 merupakan id sub modul.
	private Table tblDetailSPTPD;
	
	private boolean isPrint(){		
		return userModul.getCetak();
	}
	
/*	private SelectionAdapter getSelectionAdapter(final TableColumn column, final int index) {
		SelectionAdapter selectionAdapter = new SelectionAdapter() {
			@Override
		    public void widgetSelected(SelectionEvent e) {
				comparator.setColumn(index);
		        int dir = comparator.getDirection();
		        tableViewer.getTable().setSortDirection(dir);
		        tableViewer.getTable().setSortColumn(column);
		        tableViewer.refresh();
		    }
		};
		return selectionAdapter;
	}*/
}
