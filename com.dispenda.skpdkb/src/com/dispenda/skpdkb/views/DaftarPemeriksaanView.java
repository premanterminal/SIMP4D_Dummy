package com.dispenda.skpdkb.views;

import java.math.RoundingMode;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.swing.JOptionPane;

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
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.SWTResourceManager;

import com.dispenda.controller.ControllerFactory;
import com.dispenda.controller.UIController;
import com.dispenda.model.BidangUsahaProvider;
import com.dispenda.model.PajakProvider;
import com.dispenda.model.Pejabat;
import com.dispenda.model.Sspd;
import com.dispenda.model.UU;
import com.dispenda.model.UserModul;
import com.dispenda.object.GlobalVariable;
import com.dispenda.object.Preferences;
import com.dispenda.report.ReportAction;
import com.dispenda.report.ReportAppContext;
import com.dispenda.report.ReportModule;

public class DaftarPemeriksaanView extends ViewPart {
	public DaftarPemeriksaanView() {
	}
	
	public static final String ID = DaftarPemeriksaanView.class.getName();
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
	private Button btnSkpdlbcov;
	private Combo cmbKewajibanPajak;
	private Locale ind = new Locale("id", "ID");
	private NumberFormat indFormat = NumberFormat.getCurrencyInstance(ind);
	private java.sql.Timestamp dateNow;
	private Combo cmbSubPajak;
	private int idSubPajak;
	String tipeSKP = "";
	private Color fontColor = new Color(Display.getCurrent(),Preferences.FONT_COLOR);
	private DateTime calSampai;
	private Button btnPilihan;
	
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
		compJenisSKPD.setLayout(new GridLayout(4, false));
		
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
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		
		
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
				//System.out.println("idSubPajak: "+idSubPajak);
			}
		});
		GridData gd_cmbSubPajak = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
		gd_cmbSubPajak.widthHint = 320;
		cmbSubPajak.setLayoutData(gd_cmbSubPajak);
		cmbSubPajak.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_FOREGROUND));
		cmbSubPajak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		cmbSubPajak.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		
		Label lblTanggalSkpdkbnkbt = new Label(comp, SWT.NONE);
		lblTanggalSkpdkbnkbt.setForeground(fontColor);
		lblTanggalSkpdkbnkbt.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblTanggalSkpdkbnkbt.setText("Tanggal SKPD(KB/N/KBT/LB)");
		
		Group group = new Group(comp, SWT.NONE);
		GridData gd_group = new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1);
		gd_group.heightHint = 80;
		group.setLayoutData(gd_group);
		group.setLayout(new GridLayout(4, false));
		
		btnTanggal = new Button(group, SWT.RADIO);
		btnTanggal.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				calSKP.setVisible(true);
				calSampai.setVisible(false);
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
				calSampai.setVisible(false);
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
				calSampai.setVisible(false);
				monthSKP.setVisible(false);
				setMasaPajak();
			}
		});
		btnTahun.setText("Tahun");
		btnTahun.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BORDER));
		btnTahun.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		btnPilihan = new Button(group, SWT.RADIO);
		btnPilihan.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				calSKP.setVisible(true);
				calSampai.setVisible(true);
				monthSKP.setVisible(false);
				yearSKP.setVisible(false);
				setMasaPajak();
			}
		});
		btnPilihan.setText("Pilihan");
		btnPilihan.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BORDER));
		btnPilihan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
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
		
		calSampai = new DateTime(group, SWT.BORDER | SWT.DROP_DOWN);
		calSampai.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setMasaPajak();
			}
		});
		calSampai.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BORDER));
		calSampai.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		calSampai.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		calSampai.setVisible(false);
		new Label(group, SWT.NONE);
		new Label(group, SWT.NONE);
		new Label(group, SWT.NONE);
		new Label(group, SWT.NONE);
		
		grpPenandatangan = new Group(comp, SWT.NONE);
		grpPenandatangan.setLayout(new GridLayout(2, false));
		GridData gd_grpPenandatangan = new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 3);
		gd_grpPenandatangan.widthHint = 442;
		grpPenandatangan.setLayoutData(gd_grpPenandatangan);
		grpPenandatangan.setText("Penanda Tangan");
		grpPenandatangan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		grpPenandatangan.setForeground(fontColor);
		
		lblPetugasPendaftaran = new Label(grpPenandatangan, SWT.NONE);
		lblPetugasPendaftaran.setText("Petugas Teknis 1");
		lblPetugasPendaftaran.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblPetugasPendaftaran.setForeground(fontColor);
		
		cmbPendaftaran = new Combo(grpPenandatangan, SWT.READ_ONLY);
		cmbPendaftaran.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_FOREGROUND));
		cmbPendaftaran.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		cmbPendaftaran.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		GridData gd_cmbPendaftaran = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_cmbPendaftaran.widthHint = 264;
		cmbPendaftaran.setLayoutData(gd_cmbPendaftaran);
		
		lblPetugasPenetapan = new Label(grpPenandatangan, SWT.NONE);
		lblPetugasPenetapan.setText("Petugas Teknis 2");
		lblPetugasPenetapan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblPetugasPenetapan.setForeground(fontColor);
		
		cmbPenetapan = new Combo(grpPenandatangan, SWT.READ_ONLY);
		cmbPenetapan.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_FOREGROUND));
		cmbPenetapan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		cmbPenetapan.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		GridData gd_cmbPenetapan = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_cmbPenetapan.widthHint = 264;
		cmbPenetapan.setLayoutData(gd_cmbPenetapan);
		
		lblKasiPendaftaran = new Label(grpPenandatangan, SWT.NONE);
		lblKasiPendaftaran.setText("Kasubbid Teknis");
		lblKasiPendaftaran.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblKasiPendaftaran.setForeground(fontColor);
		
		cmbKasiPendaftaran = new Combo(grpPenandatangan, SWT.READ_ONLY);
		cmbKasiPendaftaran.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_FOREGROUND));
		cmbKasiPendaftaran.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		cmbKasiPendaftaran.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		GridData gd_cmbKasiPendaftaran = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_cmbKasiPendaftaran.widthHint = 264;
		cmbKasiPendaftaran.setLayoutData(gd_cmbKasiPendaftaran);
		
		lblKasiPenetapan = new Label(grpPenandatangan, SWT.NONE);
		lblKasiPenetapan.setText("Kasi Penetapan");
		lblKasiPenetapan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblKasiPenetapan.setVisible(false);
		lblKasiPenetapan.setForeground(fontColor);
		
		
		cmbKasiPenetapan = new Combo(grpPenandatangan, SWT.READ_ONLY);
		cmbKasiPenetapan.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_FOREGROUND));
		cmbKasiPenetapan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		cmbKasiPenetapan.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		cmbKasiPenetapan.setVisible(false);
		GridData gd_cmbKasiPenetapan = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_cmbKasiPenetapan.widthHint = 264;
		cmbKasiPenetapan.setLayoutData(gd_cmbKasiPenetapan);
		new Label(comp, SWT.NONE);
		
		//SF Note : dibawah ini adalah fungsi dari button lihat untuk mengisi tblSKP
		
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
					else if (btnSkpdlbcov.getSelection()){
						tipeSKP = "SKPDLBCov";
						createColumnsSKPDLBCov();
						if (cmbSubPajak.getText().equalsIgnoreCase(""))
							resultSKP = ControllerFactory.getMainController().getCpPeriksaDAOImpl().getSKPDLBCovList(cmbKewajibanPajak.getData(Integer.toString(cmbKewajibanPajak.getSelectionIndex())).toString(), masaPajakDari, masaPajakSampai);
						else
							resultSKP = ControllerFactory.getMainController().getCpPeriksaDAOImpl().getSKPDLBCovListSub(cmbKewajibanPajak.getData(Integer.toString(cmbKewajibanPajak.getSelectionIndex())).toString(), masaPajakDari, masaPajakSampai, idSubPajak);				
						loadTableSKPDLBCov(resultSKP);
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
		
		cmbPenandaTangan.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_FOREGROUND));
		cmbPenandaTangan.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		cmbPenandaTangan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		GridData gd_cmbPenandaTangan = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_cmbPenandaTangan.widthHint = 250;
		cmbPenandaTangan.setLayoutData(gd_cmbPenandaTangan);
		
		loadPejabat();
		
		cmbPenandaTangan.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//System.out.println(cmbPenandaTangan.getSelection().toString();
				//btnPlt.setVisible(false);
				//if (cmbPenandaTangan.getSelection().toString().equals("Point {0, 25}") && btnSkpdkb.getSelection() == true){
				//	btnPlt.setVisible(true);
				//}
			}
		});
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		
		btnPlt = new Button(comp, SWT.CHECK);
		btnPlt.setText("plt.");
		//btnPlt.setVisible(false);
		
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		
		Composite compButton = new Composite(comp, SWT.NONE);
		compButton.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 4, 1));
		compButton.setLayout(new GridLayout(6, false));
		
		
		//SF Note : Dibawah ini fungsi untuk cetak blanko
		Button btnCetakBlanko = new Button(compButton, SWT.NONE);
		btnCetakBlanko.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (cmbPenandaTangan.getText().equalsIgnoreCase("")){
					MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Silahkan isi nama pejabat");
				}
				else
				{
					String pangkat = "";
					String line1, line2, line3;
					if (masaPajakDari.after(new Date(117, 1, 19)))
						pangkat = listPejabat.get(cmbPenandaTangan.getSelectionIndex()).getJabatan();
					else
						pangkat = "Kabid Pendataan dan Penetapan";
					
					if (cmbKewajibanPajak.getText().equalsIgnoreCase("PARKIR")){
						line1 = "BADAN PENGELOLA";
						line2 = "PAJAK DAN RETRIBUSI DAERAH KOTA MEDAN";
						line3 = "KEPALA";
						if (btnPlt.getSelection() == true){
							line3 = "Plt. "+line2;
						}
					}else{
						line1 = "Badan Pengelola Pajak dan Retribusi Daerah";
						line2 = listPejabat.get(cmbPenandaTangan.getSelectionIndex()).getJabatan();
						if (btnPlt.getSelection() == true){
							line2 = "Plt. "+line2;
						}
						line3 = "";
					}
					/*line1 = "BADAN PENGELOLA";
					line2 = "PAJAK DAN RETRIBUSI DAERAH KOTA MEDAN";
					line3 = "KEPALA";*/
					ReportAppContext.name = "Variable";
					ReportAppContext.map.put("Nama", cmbPenandaTangan.getText());
					ReportAppContext.map.put("Pangkat", pangkat);
					ReportAppContext.map.put("Line1", line1);
					ReportAppContext.map.put("Line2", line2);
					ReportAppContext.map.put("Line3", line3);
					
					// SF Note : dibawah ini script untuk generate data sesuai tblSKP
					
					if (btnSkpdkb.getSelection()){
						ReportAppContext.nameObject = "BlankoSKPDKB";
						ReportAppContext.classLoader = DaftarPemeriksaanView.class.getClassLoader();
						if(!ReportAppContext.object.isEmpty())
							ReportAppContext.object.clear();
						for(int j = 0;j<tblSKP.getItems().length - 1;j++){// untuk masukan row table
							List<String> values = new ArrayList<String>();
							for(int i=1;i<tblSKP.getColumnCount();i++){// untuk masukkan data berdasarkan colom table
								if (i == 9)
									values.add(getNPWPDFormat(tblSKP.getItem(j).getText(i)));
								else if (i == 11)
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
							values.add(getNoUrutFormat(tblSKP.getItem(j).getText(1)));
							try {
								values.add(new ReportModule().getTerbilang(indFormat.parse(tblSKP.getItem(j).getText(22)).doubleValue()));
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
						ReportAppContext.classLoader = DaftarPemeriksaanView.class.getClassLoader();
						if(!ReportAppContext.object.isEmpty())
							ReportAppContext.object.clear();
						for(int j = 0;j<tblSKP.getItems().length - 1;j++){// untuk masukan row table
							List<String> values = new ArrayList<String>();
							for(int i=1;i<tblSKP.getColumnCount();i++){// untuk masukkan data berdasarkan colom table
								if (i == 9)
									values.add(getNPWPDFormat(tblSKP.getItem(j).getText(i)));
								else if (i == 11)
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
							values.add(getNoUrutFormat(tblSKP.getItem(j).getText(1)));
							ReportAppContext.object.put(Integer.toString(j), values);
						}
						ReportAction.start("BlankoSKPDN.rptdesign");
					}
					else if (btnSkpdkbt.getSelection()){
						ReportAppContext.nameObject = "BlankoSKPDKBT";
						ReportAppContext.classLoader = DaftarPemeriksaanView.class.getClassLoader();
						if(!ReportAppContext.object.isEmpty())
							ReportAppContext.object.clear();
						for(int j = 0;j<tblSKP.getItems().length - 1;j++){// untuk masukan row table
							List<String> values = new ArrayList<String>();
							for(int i=1;i<tblSKP.getColumnCount();i++){// untuk masukkan data berdasarkan colom table
								if (i == 7)
									values.add(getNPWPDFormat(tblSKP.getItem(j).getText(i)));
								else if (i == 8)
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
							values.add(getNoUrutFormat(tblSKP.getItem(j).getText(1)));
							try {
								values.add(new ReportModule().getTerbilang(indFormat.parse(tblSKP.getItem(j).getText(20)).doubleValue()));
							} catch (ParseException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							ReportAppContext.object.put(Integer.toString(j), values);
						}
						ReportAction.start("BlankoSKPDKBT.rptdesign");
					}
					else if (btnSkpdlbcov.getSelection()){
						ReportAppContext.nameObject = "BlankoSKPDLBCov";
						ReportAppContext.classLoader = DaftarPemeriksaanView.class.getClassLoader();
						if(!ReportAppContext.object.isEmpty())
							ReportAppContext.object.clear();
						Date date = new Date();
						SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy");
						SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy");
						for(int j = 0;j<tblSKP.getItems().length - 1;j++){// untuk masukan row table
							List<String> values = new ArrayList<String>();
							for(int i=1;i<tblSKP.getColumnCount();i++){// untuk masukkan data berdasarkan colom table
								/*if (i == 7)
									values.add(getNPWPDFormat(tblSKP.getItem(j).getText(i)));
								else if (i == 8)
									values.add(GetNoRekFormat(tblSKP.getItem(j).getText(i)));
								else{
									if (tblSKP.getItem(j).getText(i).startsWith("Rp"))
										values.add(tblSKP.getItem(j).getText(i).substring(2));
									else*/
										values.add(tblSKP.getItem(j).getText(i));
									//}
							}
							values.add(listPejabat.get(cmbPenandaTangan.getSelectionIndex()).getNamaPejabat());
							values.add(listPejabat.get(cmbPenandaTangan.getSelectionIndex()).getPangkat());
							values.add(listPejabat.get(cmbPenandaTangan.getSelectionIndex()).getIdPejabatNIP());
							values.add(getNoUrutFormat(tblSKP.getItem(j).getText(1)));
							try {
								values.add(new ReportModule().getTerbilang(indFormat.parse(tblSKP.getItem(j).getText(18)).doubleValue()));
							} catch (ParseException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							values.add(formatter.format(date));
							//values.add((tblSKP.getItem(j).getText(1)).substring(0, ((tblSKP.getItem(j).getText(1)).indexOf("/")))+"/"+(((values.get(10)).substring(6,11)).replace(".",""))
							//		+"/SKPDLB/"+formatter2.format(date));
							ReportAppContext.object.put(Integer.toString(j), values);
							//System.out.println(values);
						}
						ReportAction.start("BlankoSKPDLBCov.rptdesign");
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
						ReportAppContext.classLoader = DaftarPemeriksaanView.class.getClassLoader();
						if(!ReportAppContext.object.isEmpty())
							ReportAppContext.object.clear();
						for(int j = 0;j<tblSKP.getItems().length - 1;j++){
							List<String> values = new ArrayList<String>();
							values.add(String.valueOf(j+1));
							values.add(tblSKP.getItem(j).getText(1));
							values.add(tblSKP.getItem(j).getText(2));
							values.add(tblSKP.getItem(j).getText(7));
							values.add(tblSKP.getItem(j).getText(4));
							values.add(tblSKP.getItem(j).getText(5));
							values.add(tblSKP.getItem(j).getText(6));
							values.add(tblSKP.getItem(j).getText(10));
							values.add(tblSKP.getItem(j).getText(11));
							values.add(tblSKP.getItem(j).getText(16).replace(".", "").replace(",", "."));
							values.add(tblSKP.getItem(j).getText(17).replace(".", "").replace(",", "."));
							values.add(tblSKP.getItem(j).getText(18).replace(".", "").replace(",", "."));
							values.add(tblSKP.getItem(j).getText(19).replace(".", "").replace(",", "."));
							values.add(tblSKP.getItem(j).getText(20).replace(".", "").replace(",", "."));
							values.add(tblSKP.getItem(j).getText(9));
							try {
								total += indFormat.parse(tblSKP.getItem(j).getText(20)).doubleValue();
							} catch (ParseException e1) {
								e1.printStackTrace();
							}
							ReportAppContext.object.put(Integer.toString(j), values);
						}
						ReportAppContext.map.put("Total", indFormat.format(total));
						ReportAction.start("DaftarSKPDKBT.rptdesign");
					}
					else if (tipeSKP.equalsIgnoreCase("SKPDLBCov")){
						MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Mohon maaf, fitur ini belum tersedia");
						/*ReportAppContext.nameObject = "DaftarSKPDLBCov_tabel";
						ReportAppContext.classLoader = DaftarPemeriksaanView.class.getClassLoader();
						if(!ReportAppContext.object.isEmpty())
							ReportAppContext.object.clear();
						for(int j = 0;j<tblSKP.getItems().length - 1;j++){
							List<String> values = new ArrayList<String>();
							values.add(String.valueOf(j+1));
							values.add(tblSKP.getItem(j).getText(1));
							values.add(tblSKP.getItem(j).getText(2));
							values.add(tblSKP.getItem(j).getText(3));
							values.add(tblSKP.getItem(j).getText(4));
							values.add(tblSKP.getItem(j).getText(5));
							values.add(tblSKP.getItem(j).getText(6));
							values.add(tblSKP.getItem(j).getText(7));
							values.add(tblSKP.getItem(j).getText(8));
							values.add(tblSKP.getItem(j).getText(9));
							values.add(tblSKP.getItem(j).getText(10));
							values.add(tblSKP.getItem(j).getText(11));
							values.add(tblSKP.getItem(j).getText(12));
							values.add(tblSKP.getItem(j).getText(13));
							values.add(tblSKP.getItem(j).getText(14));
							values.add(tblSKP.getItem(j).getText(15));
							values.add(tblSKP.getItem(j).getText(16));
							values.add(tblSKP.getItem(j).getText(17).substring((tblSKP.getItem(j).getText(17)).indexOf("p")+1,
									(tblSKP.getItem(j).getText(17).lastIndexOf(','))).replace(".", ""));
							try {
								total += indFormat.parse(tblSKP.getItem(j).getText(17)).doubleValue();
							} catch (ParseException e1) {
								e1.printStackTrace();
							}
							ReportAppContext.object.put(Integer.toString(j), values);
						}
						ReportAppContext.map.put("Total", indFormat.format(total));
						ReportAction.start("DaftarSKPDLBCov.rptdesign");*/
					}
					else{
						ReportAppContext.nameObject = "DaftarSKPDKB_tabel";
						ReportAppContext.classLoader = DaftarPemeriksaanView.class.getClassLoader();
						if(!ReportAppContext.object.isEmpty())
							ReportAppContext.object.clear();
						for(int j = 0;j<tblSKP.getItems().length - 1;j++){
							List<String> values = new ArrayList<String>();
							values.add(String.valueOf(j+1));
							values.add(tblSKP.getItem(j).getText(1));
							values.add(tblSKP.getItem(j).getText(4));
							values.add(tblSKP.getItem(j).getText(9));
							values.add(tblSKP.getItem(j).getText(6));
							values.add(tblSKP.getItem(j).getText(7));
							values.add(tblSKP.getItem(j).getText(8));
							values.add(tblSKP.getItem(j).getText(13));
							values.add(tblSKP.getItem(j).getText(2));
							values.add(tblSKP.getItem(j).getText(14).replace(".", "").replace(",", "."));
							values.add(tblSKP.getItem(j).getText(15).replace(".", "").replace(",", "."));
							if (tblSKP.getItem(j).getText(18).equalsIgnoreCase("-"))
								values.add(indFormat.format(0).replace(".", "").replace(",", "."));
							else
								values.add(tblSKP.getItem(j).getText(18).replace(".", "").replace(",", "."));
							values.add(tblSKP.getItem(j).getText(16).replace(".", "").replace(",", "."));
							values.add(tblSKP.getItem(j).getText(17).replace(".", "").replace(",", "."));
							values.add(tblSKP.getItem(j).getText(20).replace(".", "").replace(",", "."));
							values.add(tblSKP.getItem(j).getText(22).replace(".", "").replace(",", "."));
							values.add(tblSKP.getItem(j).getText(12));
							try {
								total += indFormat.parse(tblSKP.getItem(j).getText(22)).doubleValue();
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
		
		btnCetakSuratKeputusan = new Button(compButton, SWT.NONE);
		btnCetakSuratKeputusan.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			if(isPrint()){
				//if (btnSkpdlbcov.getSelection())
					//MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "Dispenda Medan", "Mohon maaf, fitur ini belum tersedia");
				//else{
					if (cmbPenandaTangan.getText().equalsIgnoreCase("")){
						MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Silahkan isi Penanda Tangan");
						cmbPenandaTangan.forceFocus();
					}else{
						ReportAppContext.name = "Variable";
						String nomor = "973.SI/" + (cmbKewajibanPajak.getSelectionIndex() + 2) + ".........";
						String NamaSKPD = "PAJAK " + cmbKewajibanPajak.getText().toUpperCase();
						if (cmbSubPajak.getSelectionIndex() > -1)
							NamaSKPD += " - " + cmbSubPajak.getText().toUpperCase();
						String kodePajak = cmbKewajibanPajak.getData(Integer.toString(cmbKewajibanPajak.getSelectionIndex())).toString();
						if (kodePajak.equals("2")){
							NamaSKPD += " OFFICIAL";
						}
						else{
							NamaSKPD += " SELF ASSESMENT";
						}
						String menetapkan = "";
						String Kepala = "Kepala";
						List<UU> listUU = new ArrayList<UU>();
						listUU = ControllerFactory.getMainController().getCpUUDAOImpl().getUUSK(cmbKewajibanPajak.getData(Integer.toString(cmbKewajibanPajak.getSelectionIndex())).toString());
						String point6 = listUU.get(0).getUU();
						String point8 = listUU.get(1).getUU();
	
						if (btnSkpdkb.getSelection()){
							menetapkan = "SURAT KEPUTUSAN KEPALA BADAN PENGELOLA PAJAK DAN RETRIBUSI DAERAH KOTA MEDAN TENTANG SURAT KETETAPAN PAJAK DAERAH KURANG BAYAR (SKPDKB) " +
									"PAJAK " + cmbKewajibanPajak.getText() + " SELF ASSESMENT.";
						}
						else if (btnSkpdkbt.getSelection()){
							menetapkan = "SURAT KEPUTUSAN KEPALA BADAN PENGELOLA PAJAK DAN RETRIBUSI DAERAH KOTA MEDAN TENTANG SURAT KETETAPAN PAJAK DAERAH KURANG BAYAR TAMBAHAN (SKPDKBT) " +
									"PAJAK " + cmbKewajibanPajak.getText() + " SELF ASSESMENT.";
						}
						else if (btnSkpdn.getSelection()){
							menetapkan = "SURAT KEPUTUSAN KEPALA BADAN PENGELOLA PAJAK DAN RETRIBUSI DAERAH KOTA MEDAN" +
									" TENTANG SURAT KETETAPAN PAJAK DAERAH NIHIL (SKPDN) " +
									"PAJAK " + cmbKewajibanPajak.getText() + " SELF ASSESMENT.";
						}
						else if(btnSkpdlbcov.getSelection()){
							menetapkan = "SURAT KEPUTUSAN KEPALA BADAN PENGELOLA PAJAK DAN RETRIBUSI DAERAH KOTA MEDAN TENTANG SURAT KETETAPAN PAJAK DAERAH LEBIH BAYAR (SKPDLB) " +
									"PAJAK " + cmbKewajibanPajak.getText();
							if (kodePajak.equals("2")){
								menetapkan += " OFFICIAL.";
							}
							else{
								menetapkan += " SELF ASSESMENT.";
							}
						}
						if (btnPlt.getSelection()==true){
							Kepala = "Plt. "+Kepala;
						}
						ReportAppContext.map.put("Kepala", Kepala);
						ReportAppContext.map.put("nomor", nomor);
						ReportAppContext.map.put("namaPajak", NamaSKPD);
						ReportAppContext.map.put("point6", point6);
						ReportAppContext.map.put("point8", point8);
						ReportAppContext.map.put("Menetapkan", menetapkan);
						ReportAppContext.map.put("penandatangan", String.valueOf(listPejabat.get(cmbPenandaTangan.getSelectionIndex()).getNamaPejabat().replace("Zulkarnain", "ZULKARNAIN")));
						ReportAppContext.map.put("jabatan", String.valueOf(listPejabat.get(cmbPenandaTangan.getSelectionIndex()).getPangkat()));
						ReportAppContext.map.put("nip", String.valueOf(listPejabat.get(cmbPenandaTangan.getSelectionIndex()).getIdPejabatNIP()));
						
					if (btnSkpdkb.getSelection())
						ReportAction.start("SuratKeputusanSKPDKB.rptdesign");
					else if (btnSkpdkbt.getSelection())
						ReportAction.start("SuratKeputusanSKPDKBT.rptdesign");
					else if (btnSkpdn.getSelection())
						ReportAction.start("SuratKeputusanSKPDN.rptdesign");
					else if (btnSkpdlbcov.getSelection())
						ReportAction.start("SuratKeputusanSKPDLBCov.rptdesign");
					//}
				}
			}else
				MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Anda tidak memiliki hak untuk mencetak.");
			}
		});
		btnCetakSuratKeputusan.setText("Cetak Surat Keputusan");
		btnCetakSuratKeputusan.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BORDER));
		btnCetakSuratKeputusan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		btnCetakHasilPerhitungan = new Button(compButton, SWT.NONE);
		btnCetakHasilPerhitungan.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(isPrint()){
					//if (btnSkpdlbcov.getSelection())
					//	MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "Dispenda Medan", "Mohon maaf, fitur ini belum tersedia");
					//else{
						if (isValid()){
							if (btnSkpdlbcov.getSelection()){
								ReportAppContext.name = "Variable";
								Double total = 0.0;
								String NamaSKPD = "PAJAK " + cmbKewajibanPajak.getText();
								if (cmbSubPajak.getSelectionIndex() > -1)
									NamaSKPD += " - " + cmbSubPajak.getText();
								String kodePajak = cmbKewajibanPajak.getData(Integer.toString(cmbKewajibanPajak.getSelectionIndex())).toString();
								if (kodePajak.equals("2")){
									NamaSKPD += " OFFICIAL TAHUN " + (masaPajakDari.getYear() + 1900);
															}
															else{
																NamaSKPD += " SELF ASSESMENT TAHUN " + (masaPajakDari.getYear() + 1900);
															}

								
								ReportAppContext.map.put("NamaSKPD", String.valueOf(NamaSKPD));
								ReportAppContext.map.put("petugasPendataan", String.valueOf(listPejabat.get(cmbPendaftaran.getSelectionIndex()).getNamaPejabat()));
								ReportAppContext.map.put("nipPendataan", String.valueOf(listPejabat.get(cmbPendaftaran.getSelectionIndex()).getIdPejabatNIP()));
								ReportAppContext.map.put("petugasPenetapan", String.valueOf(listPejabat.get(cmbPenetapan.getSelectionIndex()).getNamaPejabat()));
								ReportAppContext.map.put("nipPenetapan", String.valueOf(listPejabat.get(cmbPenetapan.getSelectionIndex()).getIdPejabatNIP()));
								ReportAppContext.map.put("kasiPendataan", String.valueOf(listPejabat.get(cmbKasiPendaftaran.getSelectionIndex()).getNamaPejabat()));
								ReportAppContext.map.put("nipKasiPendataan", String.valueOf(listPejabat.get(cmbKasiPendaftaran.getSelectionIndex()).getIdPejabatNIP()));
		//						ReportAppContext.map.put("kasiPenetapan", String.valueOf(listPejabat.get(cmbKasiPenetapan.getSelectionIndex()).getNamaPejabat()));
		//						ReportAppContext.map.put("nipKasiPenetapan", String.valueOf(listPejabat.get(cmbKasiPenetapan.getSelectionIndex()).getIdPejabatNIP()));
								ReportAppContext.map.put("kabid", String.valueOf(listPejabat.get(cmbPenandaTangan.getSelectionIndex()).getNamaPejabat()));
								ReportAppContext.map.put("jabatan", String.valueOf(listPejabat.get(cmbPenandaTangan.getSelectionIndex()).getPangkat()));
								ReportAppContext.map.put("nipKabid", String.valueOf(listPejabat.get(cmbPenandaTangan.getSelectionIndex()).getIdPejabatNIP()));
								ReportAppContext.map.put("jabKabid", String.valueOf(listPejabat.get(cmbPenandaTangan.getSelectionIndex()).getJabatan().replace("Kepala ", "")));
								ReportAppContext.map.put("Seksi", String.valueOf(listPejabat.get(cmbKasiPendaftaran.getSelectionIndex()).getJabatan().replace("Kepala ", "")));
								ReportAppContext.map.put("tahun", String.valueOf(masaPajakDari.getYear() + 1900));
								
								ReportAppContext.nameObject = "DaftarSKPDLBCov_tabel";
								ReportAppContext.classLoader = DaftarPemeriksaanView.class.getClassLoader();
								if(!ReportAppContext.object.isEmpty())
									ReportAppContext.object.clear();
								
								for(int j = 0;j<tblSKP.getItems().length - 1;j++){
									List<String> values = new ArrayList<String>();
									values.add(String.valueOf(j+1));
									values.add(tblSKP.getItem(j).getText(1));
									values.add(tblSKP.getItem(j).getText(2));
									values.add(tblSKP.getItem(j).getText(3));
									values.add(tblSKP.getItem(j).getText(4));
									values.add(tblSKP.getItem(j).getText(5));
									values.add(tblSKP.getItem(j).getText(6));
									values.add(tblSKP.getItem(j).getText(7));
									values.add(tblSKP.getItem(j).getText(8));
									values.add(tblSKP.getItem(j).getText(9));
									values.add(tblSKP.getItem(j).getText(10));
									values.add(tblSKP.getItem(j).getText(11));
									values.add(tblSKP.getItem(j).getText(12));
									values.add(tblSKP.getItem(j).getText(13));
									values.add(tblSKP.getItem(j).getText(14));
									values.add(tblSKP.getItem(j).getText(15).replace(".", "").replace(",", "."));
									values.add(tblSKP.getItem(j).getText(16).replace(".", "").replace(",", "."));
									values.add(tblSKP.getItem(j).getText(17).replace(".", "").replace(",", "."));
									values.add(tblSKP.getItem(j).getText(18).replace(".", "").replace(",", "."));
									//values.add(tblSKP.getItem(j).getText(19).replace(".", "").replace(",", "."));
									ReportAppContext.object.put(Integer.toString(j), values);
									//System.out.println(values);
								}
								if (btnPlt.getSelection() == true){
									ReportAction.start("DaftarHasilPerhitunganCovPlt.rptdesign");
								}
								else{
									ReportAction.start("DaftarHasilPerhitunganCov.rptdesign");
								}
							}
							else if (btnSkpdkbt.getSelection()){
								ReportAppContext.name = "Variable";
								Double total = 0.0;
								String NamaSKPD = "PAJAK " + cmbKewajibanPajak.getText();
								if (cmbSubPajak.getSelectionIndex() > -1)
									NamaSKPD += " - " + cmbSubPajak.getText();
								NamaSKPD += " SELF ASSESMENT TAHUN " + (masaPajakDari.getYear() + 1900);
								ReportAppContext.map.put("NamaSKPD", String.valueOf(NamaSKPD));
								ReportAppContext.map.put("petugasPendataan", String.valueOf(listPejabat.get(cmbPendaftaran.getSelectionIndex()).getNamaPejabat()));
								ReportAppContext.map.put("nipPendataan", String.valueOf(listPejabat.get(cmbPendaftaran.getSelectionIndex()).getIdPejabatNIP()));
								ReportAppContext.map.put("petugasPenetapan", String.valueOf(listPejabat.get(cmbPenetapan.getSelectionIndex()).getNamaPejabat()));
								ReportAppContext.map.put("nipPenetapan", String.valueOf(listPejabat.get(cmbPenetapan.getSelectionIndex()).getIdPejabatNIP()));
								ReportAppContext.map.put("kasiPendataan", String.valueOf(listPejabat.get(cmbKasiPendaftaran.getSelectionIndex()).getNamaPejabat()));
								ReportAppContext.map.put("nipKasiPendataan", String.valueOf(listPejabat.get(cmbKasiPendaftaran.getSelectionIndex()).getIdPejabatNIP()));
		//						ReportAppContext.map.put("kasiPenetapan", String.valueOf(listPejabat.get(cmbKasiPenetapan.getSelectionIndex()).getNamaPejabat()));
		//						ReportAppContext.map.put("nipKasiPenetapan", String.valueOf(listPejabat.get(cmbKasiPenetapan.getSelectionIndex()).getIdPejabatNIP()));
								ReportAppContext.map.put("kabid", String.valueOf(listPejabat.get(cmbPenandaTangan.getSelectionIndex()).getNamaPejabat()));
								ReportAppContext.map.put("jabatan", String.valueOf(listPejabat.get(cmbPenandaTangan.getSelectionIndex()).getPangkat()));
								ReportAppContext.map.put("nipKabid", String.valueOf(listPejabat.get(cmbPenandaTangan.getSelectionIndex()).getIdPejabatNIP()));
								ReportAppContext.map.put("jabKabid", String.valueOf(listPejabat.get(cmbPenandaTangan.getSelectionIndex()).getJabatan().replace("Kepala ", "")));
								ReportAppContext.map.put("Seksi", String.valueOf(listPejabat.get(cmbKasiPendaftaran.getSelectionIndex()).getJabatan().replace("Kepala ", "")));
								ReportAppContext.map.put("tahun", String.valueOf(masaPajakDari.getYear() + 1900));
								
								ReportAppContext.nameObject = "DaftarSKPDKBT_tabel";
								ReportAppContext.classLoader = DaftarPemeriksaanView.class.getClassLoader();
								if(!ReportAppContext.object.isEmpty())
									ReportAppContext.object.clear();
								for(int j = 0;j<tblSKP.getItems().length - 1;j++){
									List<String> values = new ArrayList<String>();
									values.add(String.valueOf(j+1));
									values.add(tblSKP.getItem(j).getText(1));
									values.add(tblSKP.getItem(j).getText(4));
									values.add(tblSKP.getItem(j).getText(7));
									values.add(tblSKP.getItem(j).getText(4));
									values.add(tblSKP.getItem(j).getText(5));
									values.add(tblSKP.getItem(j).getText(6));
									values.add(tblSKP.getItem(j).getText(11));
							
									values.add(tblSKP.getItem(j).getText(16).replace(".", "").replace(",", "."));
									values.add(tblSKP.getItem(j).getText(17).replace(".", "").replace(",", "."));
									values.add(tblSKP.getItem(j).getText(18).replace(".", "").replace(",", "."));
									values.add(tblSKP.getItem(j).getText(19).replace(".", "").replace(",", "."));
									values.add(tblSKP.getItem(j).getText(20).replace(".", "").replace(",", "."));
									/*values.add(tblSKP.getItem(j).getText(15).replace(".", "").replace(",", "."));
									if (tblSKP.getItem(j).getText(18).equalsIgnoreCase("-"))
										values.add(indFormat.format(0).replace(".", "").replace(",", "."));
									else
										values.add(tblSKP.getItem(j).getText(18).replace(".", "").replace(",", "."));
									values.add(tblSKP.getItem(j).getText(16).replace(".", "").replace(",", "."));
									values.add(tblSKP.getItem(j).getText(17).replace(".", "").replace(",", "."));
									values.add(tblSKP.getItem(j).getText(20).replace(".", "").replace(",", "."));
									values.add(tblSKP.getItem(j).getText(22).replace(".", "").replace(",", "."));
									values.add(tblSKP.getItem(j).getText(12));
									try {
										total += indFormat.parse(tblSKP.getItem(j).getText(22)).doubleValue();
									} catch (ParseException e1) {
										e1.printStackTrace();
									}*/
									ReportAppContext.object.put(Integer.toString(j), values);
								}
								if (btnPlt.getSelection() == true){
									ReportAction.start("DaftarHasilPerhitunganKBTPLT.rptdesign");
								}
								else{
									if (listPejabat.get(cmbPenandaTangan.getSelectionIndex()).getJabatan().equals("Sekretaris")){
										ReportAction.start("DaftarHasilPerhitunganKBTSek.rptdesign");
									}
									else{
										ReportAction.start("DaftarHasilPerhitunganKBT.rptdesign");
									}
								}
							}
							else{
								ReportAppContext.name = "Variable";
								Double total = 0.0;
								String NamaSKPD = "PAJAK " + cmbKewajibanPajak.getText();
								if (cmbSubPajak.getSelectionIndex() > -1)
									NamaSKPD += " - " + cmbSubPajak.getText();
								NamaSKPD += " SELF ASSESMENT TAHUN " + (masaPajakDari.getYear() + 1900);
								ReportAppContext.map.put("NamaSKPD", String.valueOf(NamaSKPD));
								ReportAppContext.map.put("petugasPendataan", String.valueOf(listPejabat.get(cmbPendaftaran.getSelectionIndex()).getNamaPejabat()));
								ReportAppContext.map.put("nipPendataan", String.valueOf(listPejabat.get(cmbPendaftaran.getSelectionIndex()).getIdPejabatNIP()));
								ReportAppContext.map.put("petugasPenetapan", String.valueOf(listPejabat.get(cmbPenetapan.getSelectionIndex()).getNamaPejabat()));
								ReportAppContext.map.put("nipPenetapan", String.valueOf(listPejabat.get(cmbPenetapan.getSelectionIndex()).getIdPejabatNIP()));
								ReportAppContext.map.put("kasiPendataan", String.valueOf(listPejabat.get(cmbKasiPendaftaran.getSelectionIndex()).getNamaPejabat()));
								ReportAppContext.map.put("nipKasiPendataan", String.valueOf(listPejabat.get(cmbKasiPendaftaran.getSelectionIndex()).getIdPejabatNIP()));
		//						ReportAppContext.map.put("kasiPenetapan", String.valueOf(listPejabat.get(cmbKasiPenetapan.getSelectionIndex()).getNamaPejabat()));
		//						ReportAppContext.map.put("nipKasiPenetapan", String.valueOf(listPejabat.get(cmbKasiPenetapan.getSelectionIndex()).getIdPejabatNIP()));
								ReportAppContext.map.put("kabid", String.valueOf(listPejabat.get(cmbPenandaTangan.getSelectionIndex()).getNamaPejabat()));
								ReportAppContext.map.put("jabatan", String.valueOf(listPejabat.get(cmbPenandaTangan.getSelectionIndex()).getPangkat()));
								ReportAppContext.map.put("nipKabid", String.valueOf(listPejabat.get(cmbPenandaTangan.getSelectionIndex()).getIdPejabatNIP()));
								ReportAppContext.map.put("jabKabid", String.valueOf(listPejabat.get(cmbPenandaTangan.getSelectionIndex()).getJabatan().replace("Kepala ", "")));
								ReportAppContext.map.put("Seksi", String.valueOf(listPejabat.get(cmbKasiPendaftaran.getSelectionIndex()).getJabatan().replace("Kepala ", "")));
								ReportAppContext.map.put("tahun", String.valueOf(masaPajakDari.getYear() + 1900));
								
								ReportAppContext.nameObject = "DaftarSKPDKB_tabel";
								ReportAppContext.classLoader = DaftarPemeriksaanView.class.getClassLoader();
								if(!ReportAppContext.object.isEmpty())
									ReportAppContext.object.clear();
								for(int j = 0;j<tblSKP.getItems().length - 1;j++){
									List<String> values = new ArrayList<String>();
									values.add(String.valueOf(j+1));
									values.add(tblSKP.getItem(j).getText(1));
									values.add(tblSKP.getItem(j).getText(4));
									values.add(tblSKP.getItem(j).getText(9));
									values.add(tblSKP.getItem(j).getText(6));
									values.add(tblSKP.getItem(j).getText(7));
									values.add(tblSKP.getItem(j).getText(8));
									values.add(tblSKP.getItem(j).getText(13));
									values.add(tblSKP.getItem(j).getText(2));
									values.add(tblSKP.getItem(j).getText(14).replace(".", "").replace(",", "."));
									values.add(tblSKP.getItem(j).getText(15).replace(".", "").replace(",", "."));
									if (tblSKP.getItem(j).getText(18).equalsIgnoreCase("-"))
										values.add(indFormat.format(0).replace(".", "").replace(",", "."));
									else
										values.add(tblSKP.getItem(j).getText(18).replace(".", "").replace(",", "."));
									values.add(tblSKP.getItem(j).getText(16).replace(".", "").replace(",", "."));
									values.add(tblSKP.getItem(j).getText(17).replace(".", "").replace(",", "."));
									values.add(tblSKP.getItem(j).getText(20).replace(".", "").replace(",", "."));
									values.add(tblSKP.getItem(j).getText(22).replace(".", "").replace(",", "."));
									values.add(tblSKP.getItem(j).getText(12));
									try {
										total += indFormat.parse(tblSKP.getItem(j).getText(22)).doubleValue();
									} catch (ParseException e1) {
										e1.printStackTrace();
									}
									ReportAppContext.object.put(Integer.toString(j), values);
								}
								if (btnPlt.getSelection() == true){
									ReportAction.start("DaftarHasilPerhitunganPlt.rptdesign");
								}
								else{
									if (listPejabat.get(cmbPenandaTangan.getSelectionIndex()).getJabatan().equals("Sekretaris")){
										ReportAction.start("DaftarHasilPerhitunganSek.rptdesign");
									}
									else{
										ReportAction.start("DaftarHasilPerhitungan.rptdesign");
									}
								}
							}
							
						}
						else
							MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Silahkan isi Penanda Tangan");
					//}
			}else
				MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Anda tidak memiliki hak untuk mencetak.");
			}
		});
		btnCetakHasilPerhitungan.setText("Cetak Hasil Perhitungan");
		btnCetakHasilPerhitungan.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BORDER));
		btnCetakHasilPerhitungan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		btnCetakLampiranSK = new Button(compButton, SWT.NONE);
		btnCetakLampiranSK.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(isPrint()){
					//if (btnSkpdlbcov.getSelection())
					//	MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "Dispenda Medan", "Mohon maaf, fitur ini belum tersedia");
					//else{
					if (cmbPenandaTangan.getText().equalsIgnoreCase("")){
						MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Silahkan isi Penanda Tangan");
						cmbPenandaTangan.forceFocus();
					}
					else{
						ReportAppContext.name = "Variable";
						Double total = 0.0;
						String tentang = "";
						if (btnSkpdkb.getSelection())
							tentang = "Surat Ketetapan Pajak Daerah Kurang Bayar (SKPDKB) Pajak " + cmbKewajibanPajak.getText().substring(0, 1) + cmbKewajibanPajak.getText().substring(1).toLowerCase() + " "; //SELF ASSESMENT TAHUN " + (masaPajakDari.getYear() + 1900);
						else if (btnSkpdkbt.getSelection())
							tentang = "Surat Ketetapan Pajak Daerah Kurang Bayar Tambahan (SKPDKBT) Pajak " + cmbKewajibanPajak.getText().substring(0, 1) + cmbKewajibanPajak.getText().substring(1).toLowerCase() + " "; //SELF ASSESMENT TAHUN " + (masaPajakDari.getYear() + 1900);
						else if (btnSkpdn.getSelection())
							tentang = "Surat Ketetapan Pajak Daerah NIHIL (SKPDKN) Pajak " + cmbKewajibanPajak.getText().substring(0, 1) + cmbKewajibanPajak.getText().substring(1).toLowerCase() + " "; //SELF ASSESMENT TAHUN " + (masaPajakDari.getYear() + 1900);
						else if (btnSkpdlbcov.getSelection())
							tentang = "Surat Ketetapan Pajak Daerah Lebih Bayar (SKPDLB) Pajak " + cmbKewajibanPajak.getText().substring(0, 1) + cmbKewajibanPajak.getText().substring(1).toLowerCase() + " "; //SELF ASSESMENT TAHUN " + (masaPajakDari.getYear() + 1900);
						if (cmbSubPajak.getSelectionIndex() > -1)
							tentang += " - " + cmbSubPajak.getText() + " ";
						String kodePajak = cmbKewajibanPajak.getData(Integer.toString(cmbKewajibanPajak.getSelectionIndex())).toString();
						if (kodePajak.equals("2")){
							tentang += "Official";
						}
						else{
							tentang += "Self Assesment";
						}
						String Kepala = "Kepala";
						if (btnPlt.getSelection()==true){
							Kepala = "Plt. "+Kepala;
						}
						if (String.valueOf(listPejabat.get(cmbPenandaTangan.getSelectionIndex()).getJabatan()).equals("Sekretaris")){
							Kepala = "Sekretaris";
						}
						ReportAppContext.map.put("nomor", "973.SI/" + (cmbKewajibanPajak.getSelectionIndex() + 2));
						ReportAppContext.map.put("tentang", tentang);
						ReportAppContext.map.put("Kepala", Kepala);
						ReportAppContext.map.put("penandatangan", String.valueOf(listPejabat.get(cmbPenandaTangan.getSelectionIndex()).getNamaPejabat()));
						ReportAppContext.map.put("jabatan", String.valueOf(listPejabat.get(cmbPenandaTangan.getSelectionIndex()).getPangkat()));
						ReportAppContext.map.put("nip", String.valueOf(listPejabat.get(cmbPenandaTangan.getSelectionIndex()).getIdPejabatNIP()));
						if (btnSkpdlbcov.getSelection()){
							ReportAppContext.nameObject = "DaftarSKPDLBCov_tabel";
							ReportAppContext.classLoader = DaftarPemeriksaanView.class.getClassLoader();
							if(!ReportAppContext.object.isEmpty())
								ReportAppContext.object.clear();
							for(int j = 0;j<tblSKP.getItems().length - 1;j++){
								List<String> values = new ArrayList<String>();
								values.add(String.valueOf(j+1));
								values.add(tblSKP.getItem(j).getText(1));
								values.add(tblSKP.getItem(j).getText(2));
								values.add(tblSKP.getItem(j).getText(3));
								values.add(tblSKP.getItem(j).getText(4));
								values.add(tblSKP.getItem(j).getText(5));
								values.add(tblSKP.getItem(j).getText(6));
								values.add(tblSKP.getItem(j).getText(7));
								values.add(tblSKP.getItem(j).getText(8));
								values.add(tblSKP.getItem(j).getText(9));
								values.add(tblSKP.getItem(j).getText(10));
								values.add(tblSKP.getItem(j).getText(11));
								values.add(tblSKP.getItem(j).getText(12));
								values.add(tblSKP.getItem(j).getText(13));
								values.add(tblSKP.getItem(j).getText(14));
								values.add(tblSKP.getItem(j).getText(15).replace(".", "").replace(",", "."));
								values.add(tblSKP.getItem(j).getText(16).replace(".", "").replace(",", "."));
								values.add(tblSKP.getItem(j).getText(17).replace(".", "").replace(",", "."));
								values.add(tblSKP.getItem(j).getText(18).replace(".", "").replace(",", "."));
								//values.add(tblSKP.getItem(j).getText(19).replace(".", "").replace(",", "."));
								ReportAppContext.object.put(Integer.toString(j), values);
								//System.out.println(values);
							}
							ReportAction.start("DaftarSKLHPCov.rptdesign");
						}
						else if (btnSkpdkbt.getSelection()){
							ReportAppContext.nameObject = "DaftarSKPDKBT_tabel";
							ReportAppContext.classLoader = DaftarPemeriksaanView.class.getClassLoader();
							if(!ReportAppContext.object.isEmpty())
								ReportAppContext.object.clear();
							for(int j = 0;j<tblSKP.getItems().length - 1;j++){
								List<String> values = new ArrayList<String>();
								values.add(String.valueOf(j+1));
								values.add(tblSKP.getItem(j).getText(1));
								values.add(tblSKP.getItem(j).getText(4));
								values.add(tblSKP.getItem(j).getText(7));
								values.add(tblSKP.getItem(j).getText(4));
								values.add(tblSKP.getItem(j).getText(5));
								values.add(tblSKP.getItem(j).getText(6));
								values.add(tblSKP.getItem(j).getText(11));
						
								values.add(tblSKP.getItem(j).getText(16).replace(".", "").replace(",", "."));
								values.add(tblSKP.getItem(j).getText(17).replace(".", "").replace(",", "."));
								values.add(tblSKP.getItem(j).getText(18).replace(".", "").replace(",", "."));
								values.add(tblSKP.getItem(j).getText(19).replace(".", "").replace(",", "."));
								values.add(tblSKP.getItem(j).getText(20).replace(".", "").replace(",", "."));
							
								ReportAppContext.object.put(Integer.toString(j), values);
							}
							ReportAction.start("DaftarSKLHPT.rptdesign");
						}
						else{
							ReportAppContext.nameObject = "DaftarSKPDKB_tabel";
							ReportAppContext.classLoader = DaftarPemeriksaanView.class.getClassLoader();
							if(!ReportAppContext.object.isEmpty())
								ReportAppContext.object.clear();
							for(int j = 0;j<tblSKP.getItems().length - 1;j++){
								List<String> values = new ArrayList<String>();
								values.add(String.valueOf(j+1));
								values.add(tblSKP.getItem(j).getText(1));
								values.add(tblSKP.getItem(j).getText(4));
								values.add(tblSKP.getItem(j).getText(9));
								values.add(tblSKP.getItem(j).getText(6));
								values.add(tblSKP.getItem(j).getText(7));
								values.add(tblSKP.getItem(j).getText(8));
								values.add(tblSKP.getItem(j).getText(13));
								values.add(tblSKP.getItem(j).getText(2));
								values.add(tblSKP.getItem(j).getText(14).replace(".", "").replace(",", "."));
								values.add(tblSKP.getItem(j).getText(15).replace(".", "").replace(",", "."));
								if (tblSKP.getItem(j).getText(18).equalsIgnoreCase("-"))
									values.add(indFormat.format(0).replace(".", "").replace(",", "."));
								else
									values.add(tblSKP.getItem(j).getText(18).replace(".", "").replace(",", "."));
								values.add(tblSKP.getItem(j).getText(16).replace(".", "").replace(",", "."));
								values.add(tblSKP.getItem(j).getText(17).replace(".", "").replace(",", "."));
								values.add(tblSKP.getItem(j).getText(20).replace(".", "").replace(",", "."));
								values.add(tblSKP.getItem(j).getText(22).replace(".", "").replace(",", "."));
								values.add(tblSKP.getItem(j).getText(12));
								try {
									total += indFormat.parse(tblSKP.getItem(j).getText(22)).doubleValue();
								} catch (ParseException e1) {
									e1.printStackTrace();
								}
								ReportAppContext.object.put(Integer.toString(j), values);
							}
							ReportAction.start("DaftarSKLHP.rptdesign");
						}
					}
						
					//}
			}else
				MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Anda tidak memiliki hak untuk mencetak.");
			}
		});
		btnCetakLampiranSK.setText("Cetak Lampiran SK");
		btnCetakLampiranSK.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BORDER));
		btnCetakLampiranSK.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		btnCetakNotaDinas = new Button(compButton, SWT.NONE);
		btnCetakNotaDinas.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			if(isPrint()){
				//if(btnSkpdlbcov.getSelection())
					//MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "Dispenda Medan", "Mohon maaf, fitur ini belum tersedia");
				//else{
					if (cmbPenandaTangan.getText().equalsIgnoreCase("")){
						MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Silahkan isi Penanda Tangan");
					}else{
						try{
							String kodePajak = cmbKewajibanPajak.getData(Integer.toString(cmbKewajibanPajak.getSelectionIndex())).toString();
							String tipe = "";
							if (btnSkpdkb.getSelection())
								tipe = "SKPDKB";
							else if (btnSkpdkbt.getSelection())
								tipe = "SKPDKBT";
							else if (btnSkpdn.getSelection())
								tipe = "SKPDN";
							else if (btnSkpdlbcov.getSelection())
								tipe = "SKPDLB";
							
							ResultSet result;
							if (btnSkpdlbcov.getSelection())
								result = ControllerFactory.getMainController().getCpSptpdDAOImpl().getNotaSKPDLBCov(cmbKewajibanPajak.getData(Integer.toString(cmbKewajibanPajak.getSelectionIndex())).toString(), masaPajakDari, masaPajakSampai, tipe);
							else
								result = ControllerFactory.getMainController().getCpSptpdDAOImpl().getNotaSKPDKB(cmbKewajibanPajak.getData(Integer.toString(cmbKewajibanPajak.getSelectionIndex())).toString(), masaPajakDari, masaPajakSampai, tipe);
							
							ReportAppContext.name = "Variable";
							String tahun = String.valueOf(dateNow.getYear() + 1900);
							String noUrut = "";
	//						String nomor = tblSKP.getItem(0).getText(1).split("/")[0] + " s/d " + tblSKP.getItem(tblSKP.getItemCount() - 2).getText(1).split("/")[0];
	//						String jlhWP = String.valueOf(tblSKP.getItemCount() - 1);
	//						String jlhPajak = tblSKP.getItem(tblSKP.getItemCount() - 1).getText(22);
							String point2 = "";
							
							String perihal = "Konsep Naskah Surat Keputusan Kepala Badan Pengelola Pajak dan Retribusi Daerah Kota Medan Tentang ";
							if (btnSkpdkb.getSelection()){
								perihal += "Surat Ketetapan Pajak Daerah Kurang Bayar (SKPDKB) Pajak " + cmbKewajibanPajak.getText().substring(0, 1) + cmbKewajibanPajak.getText().substring(1).toLowerCase() + " ";
								point2 = "Surat Ketetapan Pajak Daerah Kurang Bayar (SKPDKB) tersebut diatas, diterbitkan berdasarkan pendataan Wajib Pajak " +
										"melalui Laporan Hasil Pemeriksaan Petugas. Jika Bapak sependapat mohon kiranya dapat menandatangani naskah " +
										"Surat Keputusan yang telah kami persiapkan sebagaimana terlampir.";
								noUrut = "No. Urut SKPDKB";
							}
							else if (btnSkpdkbt.getSelection()){
								perihal += "Surat Ketetapan Pajak Daerah Kurang Bayar Tambahan (SKPDKBT) Pajak " + cmbKewajibanPajak.getText().substring(0, 1) + cmbKewajibanPajak.getText().substring(1).toLowerCase() + " ";
								point2 = "Surat Ketetapan Pajak Daerah Kurang Bayar Tambahan (SKPDKBT) tersebut diatas, diterbitkan berdasarkan pendataan Wajib Pajak " +
										"melalui Laporan Hasil Pemeriksaan Petugas. Jika Bapak sependapat mohon kiranya dapat menandatangani naskah " +
										"Surat Keputusan yang telah kami persiapkan sebagaimana terlampir.";
								noUrut = "No. Urut SKPDKBT";
							}
							else if (btnSkpdn.getSelection()){
								perihal += "Surat Ketetapan Pajak Daerah NIHIL (SKPDN) Pajak " + cmbKewajibanPajak.getText().substring(0, 1) + cmbKewajibanPajak.getText().substring(1).toLowerCase() + " ";
								point2 = "Surat Ketetapan Pajak Daerah Nihil (SKPDN) tersebut diatas, diterbitkan berdasarkan pendataan Wajib Pajak " +
										"melalui Laporan Hasil Pemeriksaan Petugas. Jika Bapak sependapat mohon kiranya dapat menandatangani naskah " +
										"Surat Keputusan yang telah kami persiapkan sebagaimana terlampir.";
	//							jlhPajak = "Rp.    Nihil";
								noUrut = "No. Urut SKPDN";
							}
							else if (btnSkpdlbcov.getSelection()){
								perihal += "Surat Ketetapan Pajak Daerah Lebih Bayar (SKPDLB) Pajak " + cmbKewajibanPajak.getText().substring(0, 1) + cmbKewajibanPajak.getText().substring(1).toLowerCase() + " ";
								point2 = "Surat Ketetapan Pajak Daerah Lebih Bayar (SKPDLB) tersebut diatas, diterbitkan berdasarkan Laporan Hasil Pemeriksaan Kepatuhan Badan Pemeriksa Keuangan (BPK) Perwakilan Provinsi Sumatera Utara Atas Penanganan Pandemi Corona Virus Disease - 2019 (Covid-19) Tahun 2020 pada Pemerintah Kota Medan. " +
										" Jika Bapak sependapat mohon kiranya dapat menandatangani naskah " +
										"Surat Keputusan yang telah kami persiapkan sebagaimana terlampir.";
								noUrut = "No. Urut SKPDLB";
							}
							if (cmbSubPajak.getSelectionIndex() > -1)
								perihal += "- " + cmbSubPajak.getText() + " ";
							if (kodePajak.equals("2")){
								perihal += "Official";
							}
							else{
								perihal += "Self Assesment";
							}
							
							
							ReportAppContext.map.put("tahun", tahun);
							ReportAppContext.map.put("noUrut", noUrut);
							if (kodePajak.equals("2")){
								ReportAppContext.map.put("Bidang", "Parkir, Reklame, PPJ, ABT, SBW, dan Retribusi");
							}
							else{
								ReportAppContext.map.put("Bidang", "Hotel, Restoran dan Hiburan");
							}
	//						ReportAppContext.map.put("jlhWP", jlhWP);
	//						ReportAppContext.map.put("jlhPajak", jlhPajak);
							ReportAppContext.map.put("point2", point2);
							ReportAppContext.map.put("perihal", perihal);
							ReportAppContext.map.put("penandatangan", String.valueOf(listPejabat.get(cmbPenandaTangan.getSelectionIndex()).getNamaPejabat()));
							ReportAppContext.map.put("jabatan", String.valueOf(listPejabat.get(cmbPenandaTangan.getSelectionIndex()).getPangkat()));
							ReportAppContext.map.put("nip", String.valueOf(listPejabat.get(cmbPenandaTangan.getSelectionIndex()).getIdPejabatNIP()));
							
							if (btnSkpdlbcov.getSelection()){
								ReportAppContext.nameObject = "NotaSKPDCov";
								ReportAppContext.classLoader = DaftarPemeriksaanView.class.getClassLoader();
								if(!ReportAppContext.object.isEmpty())
									ReportAppContext.object.clear();
								int j=0;
								while (result.next()){
									//String noUrutSKPDKB = "";
									List<String> values = new ArrayList<String>();
		//							values.add(String.valueOf(j + 1));
									//if (result.getString("minNo").equalsIgnoreCase(result.getString("maxNo")))
									//	noUrutSKPDKB = result.getString("minNo");
									//else
									//	noUrutSKPDKB = result.getString("minNo") + " s/d " + result.getString("maxNo");
									//values.add(noUrutSKPDKB);
									values.add(result.getString("NAMA_BID_USAHA"));
									values.add(result.getString("JUMLAHWP"));
									values.add(result.getString("DENDA"));
									//values.add(indFormat.format(result.getDouble("jumlahPajak")).substring(2).replace(".", "").replace(",", "."));
									ReportAppContext.object.put(Integer.toString(j), values);
									j++;
								}
								//System.out.println(cmbKewajibanPajak.getData(Integer.toString(cmbKewajibanPajak.getSelectionIndex())).toString());
								if (kodePajak.equals("2")){
									ReportAction.start("NotaDinasSKPDSubCovOfc.rptdesign");
								}
								else{
									ReportAction.start("NotaDinasSKPDSubCov.rptdesign");
								}
								
							}
							else{
								ReportAppContext.nameObject = "NotaSKPD";
								ReportAppContext.classLoader = DaftarPemeriksaanView.class.getClassLoader();
								if(!ReportAppContext.object.isEmpty())
									ReportAppContext.object.clear();
								int j=0;
								while (result.next()){
									String noUrutSKPDKB = "";
									List<String> values = new ArrayList<String>();
		//							values.add(String.valueOf(j + 1));
									if (result.getString("minNo").equalsIgnoreCase(result.getString("maxNo")))
										noUrutSKPDKB = result.getString("minNo");
									else
										noUrutSKPDKB = result.getString("minNo") + " s/d " + result.getString("maxNo");
									values.add(noUrutSKPDKB);
									values.add(result.getString("bidangUsaha"));
									values.add(result.getString("jumlahWP"));
									values.add(indFormat.format(result.getDouble("jumlahPajak")).substring(2).replace(".", "").replace(",", "."));
									ReportAppContext.object.put(Integer.toString(j), values);
									j++;
								}
								if (btnPlt.getSelection() == true){
									ReportAction.start("NotaDinasSKPDSubPlt.rptdesign");
								}
								else{
									if (listPejabat.get(cmbPenandaTangan.getSelectionIndex()).getJabatan().equals("Sekretaris")){
										ReportAction.start("NotaDinasSKPDSubSek.rptdesign");
									}
									else{
										ReportAction.start("NotaDinasSKPDSub.rptdesign");
									}
									
								}
							}
							
						}
						catch (ParseException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					//}
				}
			}else
				MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Anda tidak memiliki hak untuk mencetak.");
			}
		});
		btnCetakNotaDinas.setText("Cetak Nota Dinas");
		btnCetakNotaDinas.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BORDER));
		btnCetakNotaDinas.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		tblSKP = new Table(comp, SWT.BORDER | SWT.FULL_SELECTION | SWT.VIRTUAL);
		tblSKP.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		tblSKP.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		tblSKP.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE-1, SWT.NORMAL));
		GridData gd_tblSKP = new GridData(SWT.FILL, SWT.FILL, true, true, 5, 1);
		gd_tblSKP.heightHint = 284;
		tblSKP.setLayoutData(gd_tblSKP);
		tblSKP.setHeaderVisible(true);
		tblSKP.setLinesVisible(true);
//		tblSKP.setComparator(comparator);
//		getSite().setSelectionProvider(tableViewer);
		scrolledComposite.setContent(comp);
		scrolledComposite.setMinSize(comp.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
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
		if (btnPilihan.getSelection())
		{
			Calendar instance = Calendar.getInstance();
			instance.set(Calendar.DAY_OF_MONTH, calSKP.getDay());
			instance.set(Calendar.MONTH, calSKP.getMonth());
			instance.set(Calendar.YEAR, calSKP.getYear());
			masaPajakDari = new Date(calSKP.getYear() - 1900, calSKP.getMonth(), calSKP.getDay());
			masaPajakSampai = new Date(calSampai.getYear() - 1900, calSampai.getMonth(), calSampai.getDay());
//			masaPajak = masaPajakDari.getDate() + " " + UIController.INSTANCE.formatMonth(masaPajakDari.getMonth() + 1, Locale.getDefault()) + " " + (masaPajakDari.getYear() + 1900);
		}
	}
	
	private void loadPejabat(){
		listPejabat = ControllerFactory.getMainController().getCpPejabatDAOImpl().getAllPejabat();
		for (int i=0;i<listPejabat.size();i++){
			UIController.INSTANCE.loadPejabat(cmbPenandaTangan, listPejabat.toArray());
			UIController.INSTANCE.loadPejabat(cmbPendaftaran, listPejabat.toArray());
			UIController.INSTANCE.loadPejabat(cmbPenetapan, listPejabat.toArray());
			UIController.INSTANCE.loadPejabat(cmbKasiPendaftaran, listPejabat.toArray());
			UIController.INSTANCE.loadPejabat(cmbKasiPenetapan, listPejabat.toArray());
		}
	}

	private void createColumnsSKP(){
		if(tblSKP.getColumnCount() <= 0){
			String[] listColumn = {"No", "No SKP", "Masa Pajak", "Tipe SKP", "Tanggal SKP", "Tahun", "Nama Pemilik", "Nama Badan", "Alamat Badan", "NPWPD", "Jatuh Tempo", 
					"Kode Bidang Usaha", "Nama Bidang Usaha", "Nama Pajak", "Pajak Periksa", "Pajak Terutang", "Kurang Bayar", "Bunga", "Kenaikan 25", "Kenaikan", "Denda",
					"Biaya Administrasi", "Total"};
			for (int i=listColumn.length - 1;i>=0;i--)
			{
				TableColumn colPajak = new TableColumn(tblSKP, SWT.NONE,0);
				colPajak.setText(listColumn[i]);
				if (i == 0)
					colPajak.setWidth(40);
				else
					colPajak.setWidth(120);
				
			}
		}
	}
	
	private void createColumnsSKPDKBT(){
		if(tblSKP.getColumnCount() <= 0){
			String[] listColumn = {"No", "No SKP", "Tanggal SKP", "Tahun", "Nama Pemilik", "Nama Badan", "Alamat Badan", "NPWPD", "Kode Bidang Usaha", "Nama Bidang Usaha", "Nama Pajak", 
					"Masa Pajak", "Masa Pajak Dari", "Masa Pajak Sampai", "Jatuh Tempo", "Tanggal Periksa", "Pajak Periksa", "Pajak Terutang", "Selisih Kurang", "Kenaikan", "Total"};
			for (int i=listColumn.length - 1;i>=0;i--)
			{
				TableColumn colPajak = new TableColumn(tblSKP, SWT.NONE,0);
				colPajak.setText(listColumn[i]);
				if (i == 0)
					colPajak.setWidth(40);
				else
					colPajak.setWidth(120);
				
			}
		}
	}
	
	private void createColumnsSKPDLBCov(){
		if(tblSKP.getColumnCount() <= 0){
			String[] listColumn = {"No", "No SKPDLB", "No SSSPD", "Tanggal SSPD", "No SPTPD", "Tanggal SPTPD", "Masa Pajak", "Tahun", "Nama Pemilik", "Nama Badan", "Alamat Badan", "NPWPD", 
					"Kode Bidang Usaha", "Nama Bidang Usaha", "Nama Pajak", "Dasar Pengenaan", "Pajak Terutang", "Jumlah Bayar", "Denda SSPD"};
			for (int i=listColumn.length - 1;i>=0;i--)
			{
				TableColumn colPajak = new TableColumn(tblSKP, SWT.NONE,0);
				colPajak.setText(listColumn[i]);
				if (i == 0)
					colPajak.setWidth(40);
				else
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
		int count = 0;
		double GrandTotal = 0;
		try
		{
			while (resultSKP.next())
			{
				count++;
				TableItem item = new TableItem(tblSKP, SWT.NONE);		
				item.setText(0, String.valueOf(count));
				item.setText(1, resultSKP.getString("No_SKP"));
				item.setText(2, getMasaPajak(resultSKP.getString("MasaPajak")));
				item.setText(3, resultSKP.getString("Tipe_SKP"));
				item.setText(4, formatter.format(resultSKP.getDate("tanggal_SKP")));
				item.setText(5, resultSKP.getString("Tahun"));
				item.setText(6, resultSKP.getString("Nama_Pemilik"));
				item.setText(7, resultSKP.getString("Nama_Badan"));
				item.setText(8, resultSKP.getString("Alabad_Jalan"));
				item.setText(9, resultSKP.getString("NPWPD"));
				item.setText(10, formatter.format(resultSKP.getDate("TanggalJatuhTempo")));
				item.setText(11, resultSKP.getString("Kode_Bid_Usaha"));
				item.setText(12, resultSKP.getString("Nama_Bid_Usaha"));
				item.setText(13, resultSKP.getString("Nama_Pajak"));
				item.setText(14, indFormat.format(resultSKP.getDouble("PajakPeriksa")));
				item.setText(15, indFormat.format(resultSKP.getDouble("PajakTerutang")));
				item.setText(16, indFormat.format(resultSKP.getDouble("KurangBayar")));
				item.setText(17, indFormat.format(resultSKP.getDouble("Bunga")));
				if (btnSkpdkb.getSelection())
					item.setText(18, indFormat.format(resultSKP.getDouble("Kenaikan25")));
				else if (btnSkpdn.getSelection())
					item.setText(18, "-");
				item.setText(19, indFormat.format(resultSKP.getDouble("Kenaikan")));
				item.setText(20, indFormat.format(resultSKP.getDouble("Denda")));
				item.setText(21, indFormat.format(resultSKP.getDouble("BiayaAdministrasi")));
				item.setText(22, indFormat.format(resultSKP.getDouble("Total")));
				GrandTotal += resultSKP.getDouble("Total");
			}
			TableItem item = new TableItem(tblSKP, SWT.NONE);		
			item.setText(1, "Total");		
			item.setText(22, indFormat.format(GrandTotal));
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void loadTableSKPDKBT(ResultSet resultSKP){
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		int count = 0;
		double GrandTotal = 0;
		try
		{
			while (resultSKP.next())
			{
				count++;
				TableItem item = new TableItem(tblSKP, SWT.NONE);
				item.setText(0, String.valueOf(count));
				item.setText(1, resultSKP.getString("No_SKP"));
				item.setText(2, formatter.format(resultSKP.getDate("tanggal_SKP")));
				item.setText(3, resultSKP.getString("Tahun"));
				item.setText(4, resultSKP.getString("Nama_Pemilik"));
				item.setText(5, resultSKP.getString("Nama_Badan"));
				item.setText(6, resultSKP.getString("Alabad_Jalan"));
				item.setText(7, resultSKP.getString("NPWPD"));
				item.setText(8, resultSKP.getString("Kode_Bid_Usaha"));
				item.setText(9, resultSKP.getString("Nama_Bid_Usaha"));
				item.setText(10, resultSKP.getString("Nama_Pajak"));
				item.setText(11, getMasaPajak(resultSKP.getString("MasaPajak")));
				item.setText(12, formatter.format(resultSKP.getDate("Masa_Pajak_Dari")));
				item.setText(13, formatter.format(resultSKP.getDate("Masa_Pajak_Sampai")));
				item.setText(14, formatter.format(resultSKP.getDate("TanggalJatuhTempo")));
				item.setText(15, formatter.format(resultSKP.getDate("Tanggal_Periksa")));
				item.setText(16, indFormat.format(resultSKP.getDouble("PajakPeriksa")));
				item.setText(17, indFormat.format(resultSKP.getDouble("PajakTerutang")));
				item.setText(18, indFormat.format(resultSKP.getDouble("SelisihKurang")));
				item.setText(19, indFormat.format(resultSKP.getDouble("Kenaikan")));
				item.setText(20, indFormat.format(resultSKP.getDouble("Total")));
				GrandTotal += resultSKP.getDouble("Total");
			}
			TableItem item = new TableItem(tblSKP, SWT.NONE);		
			item.setText(1, "Total");		
			item.setText(20, indFormat.format(GrandTotal));
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void loadTableSKPDLBCov(ResultSet resultSKP){
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		int count = 0;
		double GrandTotal = 0;
		try
		{
			while (resultSKP.next())
			{
				count++;
				TableItem item = new TableItem(tblSKP, SWT.NONE);		
				item.setText(0, String.valueOf(count));
				item.setText(1, resultSKP.getString("NOSKPDLB"));
				item.setText(2, resultSKP.getString("No_SSPD"));
				item.setText(3, formatter.format(resultSKP.getDate("TGL_SSPD")));
				item.setText(4, resultSKP.getString("No_SPTPD"));
				item.setText(5, formatter.format(resultSKP.getDate("TGL_SPTPD")));
				item.setText(6, getMasaPajak(resultSKP.getString("MasaPajak")));
				item.setText(7, resultSKP.getString("Tahun"));
				item.setText(8, resultSKP.getString("Nama_Pemilik"));
				item.setText(9, resultSKP.getString("Nama_Badan"));
				item.setText(10, resultSKP.getString("Alabad_Jalan"));
				item.setText(11, resultSKP.getString("NPWPD"));
				item.setText(12, resultSKP.getString("Kode_Bid_Usaha"));
				item.setText(13, resultSKP.getString("Nama_Bid_Usaha"));
				item.setText(14, resultSKP.getString("Nama_Pajak"));
				item.setText(15, indFormat.format(resultSKP.getDouble("Dasar_Pengenaan")));
				item.setText(16, indFormat.format(resultSKP.getDouble("Pajak_Terutang")));
				item.setText(17, indFormat.format(resultSKP.getDouble("Jumlah_Bayar")));
				item.setText(18, indFormat.format(resultSKP.getDouble("DendaSSPD")));
				GrandTotal += resultSKP.getDouble("DendaSSPD");
			}
			TableItem item = new TableItem(tblSKP, SWT.NONE);		
			item.setText(1, "Total");		
			item.setText(18, indFormat.format(GrandTotal));
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
	private Button btnCetakHasilPerhitungan;
	private Group grpPenandatangan;
	private Label lblPetugasPendaftaran;
	private Combo cmbPendaftaran;
	private Label lblPetugasPenetapan;
	private Combo cmbPenetapan;
	private Label lblKasiPendaftaran;
	private Label lblKasiPenetapan;
	private Combo cmbKasiPendaftaran;
	private Combo cmbKasiPenetapan;
	private Button btnCetakLampiranSK;
	private Button btnCetakNotaDinas;
	private Button btnCetakSuratKeputusan;
	private Button btnPlt;
	
	private boolean isPrint(){		
		return userModul.getCetak();
	}
	
	private boolean isValid(){
		Control[] children = grpPenandatangan.getChildren();
		for (int i=0;i<children.length;i++)
		{
			if (children[i] instanceof Combo)
			{
				Combo child = (Combo) children[i];
				
				if (child.getText().equalsIgnoreCase("") && child.getVisible())
				{
					child.forceFocus();
					return false;
				}
			}
			
			if (cmbPenandaTangan.getText().equalsIgnoreCase("")){
				cmbPenandaTangan.forceFocus();
				return false;
			}
		}
		return true;
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
