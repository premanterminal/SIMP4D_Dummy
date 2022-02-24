package com.dispenda.laporan.views;

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
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.SWTResourceManager;

import com.dispenda.controller.ControllerFactory;
import com.dispenda.controller.UIController;
import com.dispenda.model.BidangUsahaProvider;
import com.dispenda.model.PajakProvider;
import com.dispenda.model.UserModul;
import com.dispenda.object.GlobalVariable;
import com.dispenda.object.Preferences;
import com.dispenda.report.ReportAction;
import com.dispenda.report.ReportAppContext;

public class LaporanPiutang extends ViewPart {
	public static final String ID = LaporanPiutang.class.getName();
	private Color fontColor = new Color(Display.getCurrent(),Preferences.FONT_COLOR);
	Locale ind = new Locale("id", "ID");
	NumberFormat indFormat = NumberFormat.getCurrencyInstance(ind);
	private Timestamp dateNow;
	private Button btnTanggalSPT;
	private Button btnBulanSPT;
	private Button btnTahunSPT;
	private DateTime dtBulanSPT;
	private Spinner spnTahunSPT;
	private DateTime dtTanggalSPT;
	private Date masaPajakDariSPT;
	private Date masaPajakSampaiSPT;
	private Date breakdownSPT;
	private Date masaPajakDariSKPDKB;
	private Date masaPajakSampaiSKPDKB;
	private String masaPajak;
	private Composite compTanggal;
	private StackLayout stackLayout;
	private Table tblSPT;
	private Integer idSubPajak = 0;
	private Integer idSubPajakSKP = 0;
	private Combo cmbPajakSPT;
	private Combo cmbSubPajakSPT;
	private DateTime dtBreakdown;
	private StackLayout stackLayoutSKP;
	private Table tblSKP;
	private Button btnTanggalSKP;
	private Button btnBulanSKP;
	private Combo cmbPajakSKP;
	private Combo cmbSubPajakSKP;
	private Button btnTahun;
	private DateTime dtTanggalSKP;
	private DateTime dtBulanSKP;
	private Spinner spnTahunSKP;
	private DateTime dtBreakdownSKP;
	
	public LaporanPiutang() {
	}

	@SuppressWarnings("deprecation")
	@Override
	public void createPartControl(Composite parent) {	
		// TODO Auto-generated method stub
		Locale.setDefault(new Locale("id","ID"));
		dateNow = (java.sql.Timestamp)ControllerFactory.getMainController().getCpWajibPajakDAOImpl().getDateNow();
		ScrolledComposite scrolledComposite = new ScrolledComposite(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		
		Composite composite = new Composite(scrolledComposite, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));
		
		TabFolder tabFolder = new TabFolder(composite, SWT.NONE);
		tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		TabItem tbtmSptpd = new TabItem(tabFolder, SWT.NONE);
		tbtmSptpd.setText("SPTPD");
		
		Composite compositeSPTPD = new Composite(tabFolder, SWT.NONE);
		tbtmSptpd.setControl(compositeSPTPD);
		compositeSPTPD.setLayout(new GridLayout(5, false));
		
		Label lblKewajibanPajak = new Label(compositeSPTPD, SWT.NONE);
		lblKewajibanPajak.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblKewajibanPajak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblKewajibanPajak.setForeground(fontColor);
		lblKewajibanPajak.setText("Kewajiban Pajak");
		
		cmbPajakSPT = new Combo(compositeSPTPD, SWT.READ_ONLY);
		cmbPajakSPT.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				UIController.INSTANCE.loadBidangUsaha(cmbSubPajakSPT, BidangUsahaProvider.INSTANCE.getBidangUsaha((Integer)cmbPajakSPT.getData(Integer.toString(cmbPajakSPT.getSelectionIndex()))).toArray());
				idSubPajak = 0;
			}
		});
		cmbPajakSPT.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		cmbPajakSPT.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		GridData gd_cmbPajakSPT = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_cmbPajakSPT.widthHint = 150;
		cmbPajakSPT.setLayoutData(gd_cmbPajakSPT);
		UIController.INSTANCE.loadPajak(cmbPajakSPT, PajakProvider.INSTANCE.getPajak().toArray());
		
		Label label = new Label(compositeSPTPD, SWT.NONE);
		label.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		label.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		label.setForeground(fontColor);
		label.setText("-");
		
		cmbSubPajakSPT = new Combo(compositeSPTPD, SWT.READ_ONLY);
		cmbSubPajakSPT.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				idSubPajak = (Integer)cmbSubPajakSPT.getData(Integer.toString(cmbSubPajakSPT.getSelectionIndex()));
			}
		});
		cmbSubPajakSPT.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		cmbSubPajakSPT.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		GridData gd_cmbSubPajakSPT = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
		gd_cmbSubPajakSPT.widthHint = 200;
		cmbSubPajakSPT.setLayoutData(gd_cmbSubPajakSPT);
		
		Label lblTerbitSptpd = new Label(compositeSPTPD, SWT.NONE);
		lblTerbitSptpd.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblTerbitSptpd.setForeground(fontColor);
		lblTerbitSptpd.setText("Terbit SPTPD");
		
		Group groupSPT = new Group(compositeSPTPD, SWT.NONE);
		groupSPT.setLayout(new GridLayout(3, false));
		GridData gd_groupSPT = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
		gd_groupSPT.heightHint = 66;
		gd_groupSPT.widthHint = 204;
		groupSPT.setLayoutData(gd_groupSPT);
		
		btnTanggalSPT = new Button(groupSPT, SWT.RADIO);
		btnTanggalSPT.setSelection(true);
		btnTanggalSPT.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				stackLayout.topControl = dtTanggalSPT;
				dtTanggalSPT.setVisible(true);
				dtTanggalSPT.pack(true);
				dtBulanSPT.setVisible(false);
				spnTahunSPT.setVisible(false);
			}
		});
		btnTanggalSPT.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnTanggalSPT.setText("Tanggal");
		
		btnBulanSPT = new Button(groupSPT, SWT.RADIO);
		btnBulanSPT.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				stackLayout.topControl = dtBulanSPT;
				dtBulanSPT.setVisible(true);
				dtBulanSPT.pack(true);
				dtTanggalSPT.setVisible(false);
				spnTahunSPT.setVisible(false);
			}
		});
		btnBulanSPT.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnBulanSPT.setText("Bulan");
		
		btnTahunSPT = new Button(groupSPT, SWT.RADIO);
		btnTahunSPT.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				stackLayout.topControl = spnTahunSPT;
				spnTahunSPT.setVisible(true);
				spnTahunSPT.pack(true);
				dtTanggalSPT.setVisible(false);
				dtBulanSPT.setVisible(false);
			}
		});
		btnTahunSPT.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnTahunSPT.setText("Tahun");
		
		compTanggal = new Composite(groupSPT, SWT.NONE);
		stackLayout = new StackLayout();
		GridData gd_compTanggal = new GridData(SWT.FILL, SWT.CENTER, false, false, 3, 1);
		gd_compTanggal.widthHint = 137;
		gd_compTanggal.heightHint = 38;
		compTanggal.setLayoutData(gd_compTanggal);
		
		spnTahunSPT = new Spinner(compTanggal, SWT.BORDER);
		spnTahunSPT.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		spnTahunSPT.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		spnTahunSPT.setValues(dateNow.getYear() + 1900, 2009, dateNow.getYear() + 1900, 0, 1, 10);
		spnTahunSPT.setBounds(68, 8, 60, 22);
		spnTahunSPT.setVisible(false);
		
		dtBulanSPT = new DateTime(compTanggal, SWT.BORDER | SWT.DROP_DOWN | SWT.SHORT);
		dtBulanSPT.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		dtBulanSPT.setBackground(SWTResourceManager.getColor(Preferences.BACKGROUND_COLOR));
		dtBulanSPT.setBounds(27, 7, 142, 24);
		dtBulanSPT.setVisible(false);
		
		dtTanggalSPT = new DateTime(compTanggal, SWT.BORDER | SWT.DROP_DOWN);
		dtTanggalSPT.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		dtTanggalSPT.setBackground(SWTResourceManager.getColor(Preferences.BACKGROUND_COLOR));
		dtTanggalSPT.setBounds(27, 7, 142, 24);
		stackLayout.topControl = dtTanggalSPT;
		
		Label lblBreakdown = new Label(compositeSPTPD, SWT.NONE);
		lblBreakdown.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblBreakdown.setForeground(fontColor);
		lblBreakdown.setText("Breakdown");
		
		dtBreakdown = new DateTime(compositeSPTPD, SWT.BORDER | SWT.DROP_DOWN);
		dtBreakdown.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		new Label(compositeSPTPD, SWT.NONE);
		
		Composite CompButton = new Composite(compositeSPTPD, SWT.NONE);
		CompButton.setLayout(new GridLayout(3, false));
		CompButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 4, 1));
		
		Button btnLihat = new Button(CompButton, SWT.NONE);
		btnLihat.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ResultSet result = null;
				String kodePajak = "";
				if (cmbPajakSPT.getSelectionIndex() >= 0)
					kodePajak = cmbPajakSPT.getData(Integer.toString(cmbPajakSPT.getSelectionIndex())).toString();
				tblSPT.removeAll();
				setMasaPajakSPT();
//				List<ResultSet> resultSets = new ArrayList<ResultSet>();
				result = ControllerFactory.getMainController().getCpLaporanDAOImpl().GetListSPTPD_SKPDKB(masaPajakDariSPT, masaPajakSampaiSPT, kodePajak, idSubPajak, breakdownSPT);
//				resultSets.add(ControllerFactory.getMainController().getCpLaporanDAOImpl().GetListSPTPD_SKPDKB(masaPajakDariSPT, masaPajakSampaiSPT, kodePajak, idSubPajak));
//				result = new ResultSets(res);
				createColumn();
				loadData(result);
			}
		});
		btnLihat.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		GridData gd_btnLihat = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnLihat.widthHint = 90;
		btnLihat.setLayoutData(gd_btnLihat);
		btnLihat.setText("Lihat");
		
		Button btnBaru = new Button(CompButton, SWT.NONE);
		btnBaru.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				resetFormSPT();
			}
		});
		btnBaru.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		GridData gd_btnBaru = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnBaru.widthHint = 90;
		btnBaru.setLayoutData(gd_btnBaru);
		btnBaru.setText("Baru");
		
		Button btnCetak = new Button(CompButton, SWT.NONE);
		btnCetak.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(isPrint()){
					ReportAppContext.name = "Variable";
					ReportAppContext.map.put("WajibPajak", cmbPajakSPT.getText());
					ReportAppContext.map.put("MasaPajak", masaPajak);
					ReportAppContext.nameObject = "LaporanPiutang";
					ReportAppContext.classLoader = LaporanPiutang.class.getClassLoader();
					if(!ReportAppContext.object.isEmpty())
						ReportAppContext.object.clear();
					for(int j = 0;j<tblSPT.getItems().length-1;j++){
						List<String> values = new ArrayList<String>();
						for(int i=0;i<tblSPT.getColumnCount();i++){
							values.add(tblSPT.getItem(j).getText(i));
						}
						ReportAppContext.object.put(Integer.toString(j), values);
					}
					ReportAction.start("LaporanPiutang.rptdesign");
				}else
					MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Anda tidak memiliki hak untuk mencetak.");
			}
		});
		btnCetak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		GridData gd_btnCetak = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnCetak.widthHint = 90;
		btnCetak.setLayoutData(gd_btnCetak);
		btnCetak.setText("Cetak");
		
		tblSPT = new Table(compositeSPTPD, SWT.BORDER | SWT.FULL_SELECTION);
		tblSPT.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 5, 1));
		tblSPT.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		tblSPT.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE-1, SWT.NORMAL));
		tblSPT.setForeground(SWTResourceManager.getColor(0, 0, 0));
		tblSPT.setHeaderVisible(true);
		tblSPT.setLinesVisible(true);
		
		TabItem tbtmSkpdkb = new TabItem(tabFolder, SWT.NONE);
		tbtmSkpdkb.setText("SKPDKB");
		
		Composite compositeSKPDKB = new Composite(tabFolder, SWT.NONE);
		tbtmSkpdkb.setControl(compositeSKPDKB);
		compositeSKPDKB.setLayout(new GridLayout(5, false));
		
		Label lblKewajibanPajak_1 = new Label(compositeSKPDKB, SWT.NONE);
		lblKewajibanPajak_1.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblKewajibanPajak_1.setForeground(fontColor);
		lblKewajibanPajak_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblKewajibanPajak_1.setText("Kewajiban Pajak");
		
		cmbPajakSKP = new Combo(compositeSKPDKB, SWT.READ_ONLY);
		cmbPajakSKP.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				UIController.INSTANCE.loadBidangUsaha(cmbSubPajakSKP, BidangUsahaProvider.INSTANCE.getBidangUsaha((Integer)cmbPajakSKP.getData(Integer.toString(cmbPajakSKP.getSelectionIndex()))).toArray());
				idSubPajakSKP = 0;
			}
		});
		cmbPajakSKP.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		cmbPajakSKP.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		GridData gd_cmbPajakSKP = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_cmbPajakSKP.widthHint = 150;
		cmbPajakSKP.setLayoutData(gd_cmbPajakSKP);
		UIController.INSTANCE.loadPajak(cmbPajakSKP, PajakProvider.INSTANCE.getPajak().toArray());
		
		Label label_1 = new Label(compositeSKPDKB, SWT.NONE);
		label_1.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		label_1.setForeground(fontColor);
		label_1.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		label_1.setText("-");
		
		cmbSubPajakSKP = new Combo(compositeSKPDKB, SWT.READ_ONLY);
		cmbSubPajakSKP.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		cmbSubPajakSKP.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		GridData gd_cmbSubPajakSKP = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
		gd_cmbSubPajakSKP.widthHint = 200;
		cmbSubPajakSKP.setLayoutData(gd_cmbSubPajakSKP);
		
		Label lblTerbitSkp = new Label(compositeSKPDKB, SWT.NONE);
		lblTerbitSkp.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblTerbitSkp.setForeground(fontColor);
		lblTerbitSkp.setText("Terbit SKP");
		
		Group groupSKP = new Group(compositeSKPDKB, SWT.NONE);
		GridData gd_groupSKP = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
		gd_groupSKP.heightHint = 66;
		gd_groupSKP.widthHint = 204;
		groupSKP.setLayoutData(gd_groupSKP);
		groupSKP.setLayout(new GridLayout(3, false));
		
		btnTanggalSKP = new Button(groupSKP, SWT.RADIO);
		btnTanggalSKP.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				stackLayoutSKP.topControl = dtTanggalSKP;
				dtTanggalSKP.setVisible(true);
				dtTanggalSKP.pack(true);
				dtBulanSKP.setVisible(false);
				spnTahunSKP.setVisible(false);
			}
		});
		btnTanggalSKP.setSelection(true);
		btnTanggalSKP.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnTanggalSKP.setText("Tanggal");
		
		btnBulanSKP = new Button(groupSKP, SWT.RADIO);
		btnBulanSKP.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				stackLayoutSKP.topControl = dtBulanSKP;
				dtBulanSKP.setVisible(true);
				dtBulanSKP.pack(true);
				dtTanggalSKP.setVisible(false);
				spnTahunSKP.setVisible(false);
			}
		});
		btnBulanSKP.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnBulanSKP.setText("Bulan");
		
		btnTahun = new Button(groupSKP, SWT.RADIO);
		btnTahun.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				stackLayoutSKP.topControl = spnTahunSKP;
				spnTahunSKP.setVisible(true);
				spnTahunSKP.pack(true);
				dtTanggalSKP.setVisible(false);
				dtBulanSKP.setVisible(false);
			}
		});
		btnTahun.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnTahun.setText("Tahun");
		
		Composite compTanggalSKP = new Composite(groupSKP, SWT.NONE);
		stackLayoutSKP = new StackLayout();
		GridData gd_compTanggalSKP = new GridData(SWT.FILL, SWT.FILL, false, false, 3, 1);
		gd_compTanggalSKP.widthHint = 137;
		gd_compTanggalSKP.heightHint = 38;
		compTanggalSKP.setLayoutData(gd_compTanggalSKP);
		
		dtTanggalSKP = new DateTime(compTanggalSKP, SWT.BORDER | SWT.DROP_DOWN);
		dtTanggalSKP.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		dtTanggalSKP.setBounds(27, 7, 142, 24);
		stackLayoutSKP.topControl = dtTanggalSKP;
		
		dtBulanSKP = new DateTime(compTanggalSKP, SWT.BORDER | SWT.DROP_DOWN | SWT.SHORT);
		dtBulanSKP.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		dtBulanSKP.setBounds(27, 7, 142, 24);
		dtBulanSKP.setVisible(false);
		
		spnTahunSKP = new Spinner(compTanggalSKP, SWT.BORDER);
		spnTahunSKP.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		spnTahunSKP.setValues(dateNow.getYear() + 1900, 2009, dateNow.getYear() + 1900, 0, 1, 10);
		spnTahunSKP.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		spnTahunSKP.setBounds(68, 8, 60, 22);
		spnTahunSKP.setVisible(false);
		
		Label lblBreakdown_1 = new Label(compositeSKPDKB, SWT.NONE);
		lblBreakdown_1.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblBreakdown_1.setForeground(fontColor);
		lblBreakdown_1.setText("Breakdown");
		
		dtBreakdownSKP = new DateTime(compositeSKPDKB, SWT.BORDER | SWT.DROP_DOWN);
		dtBreakdownSKP.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		new Label(compositeSKPDKB, SWT.NONE);
		
		Composite compButtonSKP = new Composite(compositeSKPDKB, SWT.NONE);
		compButtonSKP.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 4, 1));
		compButtonSKP.setLayout(new GridLayout(3, false));
		
		Button btnLihatSKP = new Button(compButtonSKP, SWT.NONE);
		GridData gd_btnLihatSKP = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnLihatSKP.widthHint = 90;
		btnLihatSKP.setLayoutData(gd_btnLihatSKP);
		btnLihatSKP.setText("Lihat");
		btnLihatSKP.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		Button btnBaruSKP = new Button(compButtonSKP, SWT.NONE);
		btnBaruSKP.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				resetFormSKP();
			}
		});
		GridData gd_btnBaruSKP = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnBaruSKP.widthHint = 90;
		btnBaruSKP.setLayoutData(gd_btnBaruSKP);
		btnBaruSKP.setText("Baru");
		btnBaruSKP.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		Button btnCetakSKP = new Button(compButtonSKP, SWT.NONE);
		GridData gd_btnCetakSKP = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnCetakSKP.widthHint = 90;
		btnCetakSKP.setLayoutData(gd_btnCetakSKP);
		btnCetakSKP.setText("Cetak");
		btnCetakSKP.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		tblSKP = new Table(compositeSKPDKB, SWT.BORDER | SWT.FULL_SELECTION);
		tblSKP.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		tblSKP.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE-1, SWT.NORMAL));
		tblSKP.setForeground(SWTResourceManager.getColor(0, 0, 0));
		tblSKP.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 5, 1));
		tblSKP.setHeaderVisible(true);
		tblSKP.setLinesVisible(true);
		
		scrolledComposite.setContent(composite);
		scrolledComposite.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}
	
	@SuppressWarnings("deprecation")
	public void setMasaPajakSPT()
	{
		if (btnTanggalSPT.getSelection())
		{
			Calendar instance = Calendar.getInstance();
			instance.set(Calendar.DAY_OF_MONTH, dtTanggalSPT.getDay());
			instance.set(Calendar.MONTH, dtTanggalSPT.getMonth());
			instance.set(Calendar.YEAR, dtTanggalSPT.getYear());
			masaPajakDariSPT = new Date(dtTanggalSPT.getYear() - 1900, dtTanggalSPT.getMonth(), dtTanggalSPT.getDay());
			masaPajakSampaiSPT = masaPajakDariSPT;
			masaPajak = masaPajakDariSPT.getDate() + " " + UIController.INSTANCE.formatMonth(masaPajakDariSPT.getMonth() + 1, Locale.getDefault()) + " " + (masaPajakDariSPT.getYear() + 1900);
		}
		if (btnBulanSPT.getSelection())
		{
			Calendar instance = Calendar.getInstance();
			instance.set(Calendar.DAY_OF_MONTH, dtBulanSPT.getDay());
			instance.set(Calendar.MONTH, dtBulanSPT.getMonth());
			instance.set(Calendar.YEAR, dtBulanSPT.getYear());
			masaPajakDariSPT = new Date(dtBulanSPT.getYear() - 1900, dtBulanSPT.getMonth(), 1);
			masaPajakSampaiSPT = new Date(dtBulanSPT.getYear() - 1900, dtBulanSPT.getMonth(), instance.getActualMaximum(Calendar.DATE));
			masaPajak = UIController.INSTANCE.formatMonth(dtBulanSPT.getMonth() + 1, Locale.getDefault()) + " " + dtBulanSPT.getYear();
		}
		if (btnTahunSPT.getSelection())
		{
			masaPajakDariSPT = new Date(spnTahunSPT.getSelection() - 1900, 0, 1);
			masaPajakSampaiSPT = new Date(spnTahunSPT.getSelection() - 1900, 11, 31);
			masaPajak = Integer.toString(spnTahunSPT.getSelection());
		}
		breakdownSPT = new Date(dtBreakdown.getYear() - 1900, dtBreakdown.getMonth(), dtBreakdown.getDay());
	}
	
	private void resetFormSPT(){
		cmbPajakSPT.deselectAll();
		cmbSubPajakSPT.deselectAll();
		cmbSubPajakSPT.removeAll();
		idSubPajak = 0;
		tblSPT.removeAll();
	}
	
	private void resetFormSKP(){
		cmbPajakSKP.deselectAll();
		cmbSubPajakSKP.deselectAll();
		cmbSubPajakSKP.removeAll();
		idSubPajakSKP = 0;
		tblSKP.removeAll();
	}
	
	private void createColumn(){
		if(tblSPT.getColumnCount() <= 0){
			String[] listColumn = {"No.","NPWPD","Nama Badan","Nama Pemilik","Alamat","Bidang Usaha","No SPTPD", "Tanggal SPTPD", "Masa Pajak","Dasar Pengenaan","Tarif","Pajak Terutang","Denda Tambahan","Total",
					"No/Tgl SSPD","Realisasi Rutin","Realisasi Tunggakan","Mutasi Kurang LHP","Mutasi Kurang Tutup", "Sisa"};
			int[] columnWidth = {40, 110, 200, 170, 170, 170, 160, 110, 120, 150, 70, 150, 110, 150, 240, 150, 150, 150, 150, 120};
			for (int i=listColumn.length - 1;i>=0;i--)
			{
				TableColumn colPajak = new TableColumn(tblSPT, SWT.NONE,0);
				colPajak.setText(listColumn[i]);
				colPajak.setWidth(columnWidth[i]);
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	private void loadData(final ResultSet result)
	{
		double totalPajakTerutang = 0;
		double total = 0;
		double realisasi = 0;
		double mutasiLHP = 0;
		double sisa = 0;
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		Integer count = 1;
		try
		{
			while (result.next())
			{
				TableItem item = new TableItem(tblSPT, SWT.NONE);
				item.setText(0, count.toString());
				item.setText(1, result.getString("NPWPD"));
				item.setText(2, result.getString("Nama_Badan"));
				item.setText(3, result.getString("Nama_Pemilik"));
				item.setText(4, result.getString("Alabad_Jalan"));
				item.setText(5, result.getString("Nama_Bid_Usaha"));
				item.setText(6, result.getString("No_Sptpd"));
				item.setText(7, formatter.format(result.getDate("Tgl_SPTPD")));
				item.setText(8, UIController.INSTANCE.formatMonth(result.getDate("Masa_Pajak_Dari").getMonth() + 1, ind) + " " + (result.getDate("Masa_Pajak_Dari").getYear() + 1900));
				item.setText(9, indFormat.format(result.getDouble("Dasar_Pengenaan")));
				item.setText(10, result.getString("Tarif_Pajak"));
				item.setText(11, indFormat.format(result.getDouble("Pajak_Terutang")));
				item.setText(12, "-");
				item.setText(13, indFormat.format(result.getDouble("Pajak_Terutang")));
				if (result.getString("No_Registrasi") == null){
					if (result.getString("No_SKP") == null){
						item.setText(14, "-");
						item.setText(15, "-");
						item.setText(19, indFormat.format(result.getDouble("Pajak_Terutang")));
					}else{
						item.setText(14, "LHP " + UIController.INSTANCE.formatMonth(result.getDate("SKPDKBDari").getMonth() + 1, ind) + " " + (result.getDate("SKPDKBDari").getYear() + 1900) + " - " + 
								UIController.INSTANCE.formatMonth(result.getDate("SKPDKBSampai").getMonth() + 1, ind) + " " + (result.getDate("SKPDKBSampai").getYear() + 1900));
						item.setText(15, "-");
						item.setText(17, indFormat.format(result.getDouble("Pajak_Terutang")));
					}
				}else{
					item.setText(14, result.getString("No_Registrasi") + "/" + formatter.format(result.getDate("Tgl_Cetak")));
					item.setText(15, indFormat.format(result.getDouble("Pajak_Terutang")));
				}
//				item.setText(6, result.getString("No_SKP"));
//				item.setText(7, formatter.format(result.getDate("Tanggal_SKP"))); //.split(" ")[0]);
//				item.setText(8, UIController.INSTANCE.formatMonth(result.getDate("SKPDKBDari").getMonth() + 1, ind) + " " + (result.getDate("SKPDKBDari").getYear() + 1900) + " - " + UIController.INSTANCE.formatMonth(result.getDate("SKPDKBSampai").getMonth() + 1, ind) + " " + (result.getDate("SKPDKBSampai").getYear() + 1900));
//				item.setText(9, result.getString("Kode_Bid_Usaha"));
//				item.setText(10, indFormat.format(result.getDouble("Pajak_Terutang")));
//				item.setText(11, indFormat.format(result.getDouble("Denda_Tambahan")));
//				item.setText(12, indFormat.format(result.getDouble("Pajak_Terutang") + result.getDouble("Denda_Tambahan")));
				try {
					totalPajakTerutang += indFormat.parse(item.getText(11)).doubleValue();
					total += indFormat.parse(item.getText(11)).doubleValue();
					realisasi += indFormat.parse(item.getText(15).equalsIgnoreCase("-") ? "Rp0.0" : item.getText(15)).doubleValue();
					mutasiLHP += indFormat.parse(item.getText(17).equalsIgnoreCase("") ? "Rp0.0" : item.getText(17)).doubleValue();
					sisa += indFormat.parse(item.getText(19).equalsIgnoreCase("") ? "Rp0.0" : item.getText(19)).doubleValue();
				} catch (ParseException e) {
					e.printStackTrace();
				}
				count++;
			}
			TableItem item = new TableItem(tblSPT, SWT.NONE);
			item.setText(1, "Grand Total");
			item.setText(11, indFormat.format(totalPajakTerutang));
			item.setText(13, indFormat.format(total));
			item.setText(15, indFormat.format(realisasi));
			item.setText(17, indFormat.format(mutasiLHP));
			item.setText(19, indFormat.format(sisa));
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private UserModul userModul = ControllerFactory.getMainController().getCpUserDAOImpl()
			.getHMUserModul(GlobalVariable.userModul.getIdUser()).get(7);
	private boolean isPrint(){		
		return userModul.getCetak();
	}
}
