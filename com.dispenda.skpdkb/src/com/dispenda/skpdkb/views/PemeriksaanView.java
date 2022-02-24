package com.dispenda.skpdkb.views;

import java.math.RoundingMode;
import java.sql.Date;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.SWTResourceManager;

import com.dispenda.controller.ControllerFactory;
import com.dispenda.controller.UIController;
import com.dispenda.dao.LogImp;
import com.dispenda.model.PendaftaranWajibPajak;
import com.dispenda.model.Periksa;
import com.dispenda.model.PeriksaDetail;
import com.dispenda.model.PeriksaDetailBPK;
import com.dispenda.model.PeriksaDetailBPKProvider;
import com.dispenda.model.PeriksaDetailProvider;
import com.dispenda.model.Sptpd;
import com.dispenda.model.UserModul;
import com.dispenda.object.GlobalVariable;
import com.dispenda.object.Preferences;
import com.dispenda.report.ReportAction;
import com.dispenda.report.ReportAppContext;
import com.dispenda.widget.MoneyField;
import com.ibm.icu.util.Currency;
import com.ibm.icu.util.CurrencyAmount;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Spinner;

public class PemeriksaanView extends ViewPart {
	public PemeriksaanView() {
	}
	
	public static final String ID = PemeriksaanView.class.getName();
	private Text txtNamaBadan;
	private Text txtAlamatBadan;
	private Table tbl_Periksa;
	private MoneyField txtTotalPajakTerhutang;
	private MoneyField txtKenaikan;
	private MoneyField txtTotal;
	private Text txtNoPemeriksaan;
	private Text txtNoSKPDKB;
	private Text txtKeterangan;
	private Text txtKodePajak;
	private Text txtNoUrut;
	private Text txtKodeKecamatan;
	private Text txtKodeKelurahan;

	private Locale ind = new Locale("id", "ID");
	private NumberFormat indFormat = NumberFormat.getCurrencyInstance(ind);
	private java.sql.Timestamp dateNow;
	PendaftaranWajibPajak wp;
	private Composite comp;
	private Integer idPeriksa = null;
	private Label lblKeteranganAktif;
	private List<Sptpd> listMasaPajakDari = new ArrayList<Sptpd>();
	private List<Sptpd> listMasaPajakSampai = new ArrayList<Sptpd>();
	private List<Periksa> listMasaPajak = new ArrayList<Periksa>();
	private Periksa dataPeriksa = new Periksa();
	private Combo cmbMasaPajakDari;
	private Combo cmbMasaPajakSampai;
	private java.sql.Date masaPajakDari;
	private	java.sql.Date masaPajakSampai;
	private int kenaikanPersen = 0;
	private Label lblKenaikan;
	private Button btnPeriksa;
	private Combo cmbMasaPajak;
	private Button btnPeriksa1;
	private Button btnPeriksa2;
	private DateTime calPemeriksaan;
	private DateTime calPenetapan;
	private Label lblTanggalPemeriksaan;
	private Label lblTanggalPenetapan;
	private int indexAwal;
	private int indexAkhir;
	private int idSubPajak = 0;
	private double totalPajakPeriksa;
    private double totalPajakTerutang;
    private double totalBunga;
    private double totalKenaikan;
    private double totalDenda;
    private double totalDibayar;
//    private double oldPajakPeriksa;
	private Group compPemeriksaan;
	private boolean editKenaikan = true;
	private Button chkKenaikan;
//	private TextCellEditor tbl_PeriksaEditor;
	private String jenisPeriksa;
	private String tipeSKP;
	private String NoSKP = "";
	private String NoNPPD = "";
	private Button btnSimpan;
	private Button btnHapus;
	private Color fontColor = new Color(Display.getCurrent(),Preferences.FONT_COLOR);
	
	@Override
	public void createPartControl(Composite parent) {
		Locale.setDefault(new Locale("id","ID"));
		dateNow = (java.sql.Timestamp)ControllerFactory.getMainController().getCpWajibPajakDAOImpl().getDateNow();
//		parent.setBackground(new Color(Display.getCurrent(), 32, 172, 192));
		
		ScrolledComposite scrolledComposite = new ScrolledComposite(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		
		comp = new Composite(scrolledComposite, SWT.NONE);
		comp.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BORDER));
		comp.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		comp.setLayout(new GridLayout(9, false));
		
		Label lblNpwpd = new Label(comp, SWT.NONE);
		lblNpwpd.setForeground(fontColor);
		lblNpwpd.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblNpwpd.setText("NPWPD");
		new Label(comp, SWT.NONE);
		
		txtKodePajak = new Text(comp, SWT.BORDER);
		txtKodePajak.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		txtKodePajak.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtKodePajak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtKodePajak.setEditable(false);
		GridData gd_txtKodePajak = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_txtKodePajak.widthHint = 14;
		txtKodePajak.setLayoutData(gd_txtKodePajak);
		
		txtNoUrut = new Text(comp, SWT.BORDER);
		txtNoUrut.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		txtNoUrut.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.keyCode == SWT.CR || e.keyCode == SWT.KEYPAD_CR)
				{
					//resetForm();
					getDataWP();
					txtNamaBadan.pack(true);

						if (wp.getStatus() != null && !wp.getStatus().equalsIgnoreCase("Aktif")){
							lblKeteranganAktif.setVisible(true);
							lblKeteranganAktif.setText(wp.getKeteranganTutup());
						}
						else{
							lblKeteranganAktif.setText("");
							lblKeteranganAktif.setVisible(false);
						}
						loadMasaPajakPeriksaDari();
				}
			}
		});
		txtNoUrut.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtNoUrut.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		GridData gd_txtNoUrut = new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1);
		gd_txtNoUrut.widthHint = 83;
		txtNoUrut.setLayoutData(gd_txtNoUrut);
		
		txtKodeKecamatan = new Text(comp, SWT.BORDER);
		txtKodeKecamatan.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		txtKodeKecamatan.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtKodeKecamatan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtKodeKecamatan.setEditable(false);
		GridData gd_txtKodeKecamatan = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_txtKodeKecamatan.widthHint = 20;
		txtKodeKecamatan.setLayoutData(gd_txtKodeKecamatan);
		
		txtKodeKelurahan = new Text(comp, SWT.BORDER);
		txtKodeKelurahan.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		txtKodeKelurahan.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtKodeKelurahan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtKodeKelurahan.setEditable(false);
		GridData gd_txtKodeKelurahan = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_txtKodeKelurahan.widthHint = 20;
		txtKodeKelurahan.setLayoutData(gd_txtKodeKelurahan);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		
		lblKeteranganAktif = new Label(comp, SWT.WRAP);
		GridData gd_lblKeteranganAktif = new GridData(SWT.FILL, SWT.TOP, true, false, 1, 3);
		gd_lblKeteranganAktif.heightHint = 86;
		gd_lblKeteranganAktif.widthHint = 295;
		lblKeteranganAktif.setLayoutData(gd_lblKeteranganAktif);
		lblKeteranganAktif.setForeground(SWTResourceManager.getColor(75, 0, 130));
		lblKeteranganAktif.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, 15, SWT.BOLD));
		lblKeteranganAktif.setBackground(SWTResourceManager.getColor(SWT.COLOR_CYAN));
		lblKeteranganAktif.setVisible(false);
		
		Label lblNamaBadan = new Label(comp, SWT.NONE);
		lblNamaBadan.setForeground(fontColor);
		lblNamaBadan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblNamaBadan.setText("Nama Badan");
		new Label(comp, SWT.NONE);
		
		txtNamaBadan = new Text(comp, SWT.BORDER);
		txtNamaBadan.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		txtNamaBadan.setEditable(false);
		txtNamaBadan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtNamaBadan.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		GridData gd_txtNamaBadan = new GridData(SWT.LEFT, SWT.CENTER, false, false, 5, 1);
		gd_txtNamaBadan.widthHint = 291;
		txtNamaBadan.setLayoutData(gd_txtNamaBadan);
		new Label(comp, SWT.NONE);
		
		Label lblAlamatBadan = new Label(comp, SWT.NONE);
		lblAlamatBadan.setForeground(fontColor);
		lblAlamatBadan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblAlamatBadan.setText("Alamat Badan");
		new Label(comp, SWT.NONE);
		
		txtAlamatBadan = new Text(comp, SWT.BORDER);
		txtAlamatBadan.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		txtAlamatBadan.setEditable(false);
		txtAlamatBadan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtAlamatBadan.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		GridData gd_txtAlamatBadan = new GridData(SWT.LEFT, SWT.CENTER, false, false, 5, 1);
		gd_txtAlamatBadan.widthHint = 291;
		txtAlamatBadan.setLayoutData(gd_txtAlamatBadan);
		new Label(comp, SWT.NONE);
		
		Label lblPilihPemeriksaan = new Label(comp, SWT.NONE);
		lblPilihPemeriksaan.setForeground(fontColor);
		lblPilihPemeriksaan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblPilihPemeriksaan.setText("Pilih Pemeriksaan");
		new Label(comp, SWT.NONE);
		
		compPemeriksaan = new Group(comp, SWT.NONE);
		compPemeriksaan.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 6, 1));
		compPemeriksaan.setLayout(new GridLayout(3, false));
		
		btnPeriksa1 = new Button(compPemeriksaan, SWT.RADIO);
		btnPeriksa1.setLayoutData(new GridData(SWT.LEFT, SWT.BOTTOM, false, false, 1, 1));
		btnPeriksa1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				kenaikanPersen = ControllerFactory.getMainController().getCpPeriksaDAOImpl().getKenaikanPeriksa1(wp.getNPWP());
				lblKenaikan.setText("Kenaikan (" + kenaikanPersen + "%)");
				loadComboPeriksa1();
				btnPeriksa.setEnabled(true);
				spinner.setVisible(false);
				txtKeterangan.setText("");
			}
		});
		btnPeriksa1.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnPeriksa1.setText("Pemeriksaan 1");
		
		
		btnPeriksa2 = new Button(compPemeriksaan, SWT.RADIO);
		btnPeriksa2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				kenaikanPersen = ControllerFactory.getMainController().getCpPeriksaDAOImpl().getKenaikanPeriksa2(wp.getNPWP());
				lblKenaikan.setText("Kenaikan (" + kenaikanPersen + "%)");
				loadComboPeriksa2();
				btnPeriksa.setEnabled(true);
				spinner.setVisible(false);
				txtKeterangan.setText("");
			}
		});
		btnPeriksa2.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnPeriksa2.setText("Pemeriksaan 2");
		
		btnPeriksaBpk = new Button(compPemeriksaan, SWT.RADIO);
		btnPeriksaBpk.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				kenaikanPersen = ControllerFactory.getMainController().getCpPeriksaDAOImpl().getKenaikanPeriksa1(wp.getNPWP());
				lblKenaikan.setText("Kenaikan (" + kenaikanPersen + "%)");
				loadComboPeriksaBPK();
				btnPeriksa.setEnabled(true);
				spinner.setVisible(true);
				txtKeterangan.setText("Pemeriksaan BPK");
				
			}
		});
		btnPeriksaBpk.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnPeriksaBpk.setText("Pemeriksaan BPK");
		btnPeriksaBpk.setVisible(false);
		if (userModul.getIdUser()==1 || userModul.getIdUser()==64 || userModul.getIdUser()==72)
			btnPeriksaBpk.setVisible(true);

		cmbMasaPajak = new Combo(compPemeriksaan, SWT.READ_ONLY);
		cmbMasaPajak.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		cmbMasaPajak.addSelectionListener(new SelectionAdapter() {
			@SuppressWarnings("deprecation")
			@Override
			public void widgetSelected(SelectionEvent e) {
				txtNoSKPDKB.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
				idPeriksa = listMasaPajak.get(cmbMasaPajak.getSelectionIndex()).getIdPeriksa();
				btnPeriksa.setEnabled(false);
				cmbMasaPajakDari.setEnabled(false);
				cmbMasaPajakSampai.setEnabled(false);
				lblTanggalPenetapan.setVisible(true);
				calPenetapan.setVisible(true);
				
//				cmbMasaPajakDari.setEnabled(false);
//				cmbMasaPajakSampai.setEnabled(false);
				tbl_Periksa.removeAll();
				dataPeriksa = ControllerFactory.getMainController().getCpPeriksaDAOImpl().GetPeriksa1Data(idPeriksa);
				if (dataPeriksa.getKenaikan() > 0)
				{
					txtKenaikan.setMoney(new CurrencyAmount(dataPeriksa.getKenaikan(), Currency.getInstance(ind)));
				}
//				String strMasaDari = UIController.INSTANCE.formatMonth(dataPeriksa.getMasaPajakDari().getMonth() + 1, Locale.getDefault()) + " " + (dataPeriksa.getMasaPajakDari().getYear() + 1900);
//				String strMasaSampai = UIController.INSTANCE.formatMonth(dataPeriksa.getMasaPajakSampai().getMonth() + 1, Locale.getDefault()) + " " + (dataPeriksa.getMasaPajakSampai().getYear() + 1900);
//				cmbMasaPajakDari.setText(strMasaDari);
//				cmbMasaPajakSampai.setText(strMasaSampai);
				masaPajakDari = dataPeriksa.getMasaPajakDari();
				masaPajakSampai = dataPeriksa.getMasaPajakSampai();
				int indexDari;
				int indexSampai;
				int idxYear;
				Calendar now = Calendar.getInstance();
				int year = now.get(Calendar.YEAR);
				int month = now.get(Calendar.MONTH);
				Calendar cal = Calendar.getInstance();
		        cal.setTime(masaPajakDari);
		        int yearStart = cal.get(Calendar.YEAR);
		        int monthStart = cal.get(Calendar.MONTH);
		        idxYear = year - yearStart;
		        indexDari = month - monthStart + (idxYear * 12);
				cmbMasaPajakDari.select(indexDari);
				loadMasaPajakPeriksaSampai(listMasaPajakDari.get(cmbMasaPajakDari.getSelectionIndex()).getMasaPajakSampai());
				
		        cal.setTime(masaPajakSampai);
		        int yearEnd = cal.get(Calendar.YEAR);
		        int monthEnd = cal.get(Calendar.MONTH);
		        idxYear = year - yearEnd;
		        indexSampai = month - monthEnd + (idxYear * 12);
				cmbMasaPajakSampai.select(indexSampai);
				
				calPemeriksaan.setDate(dataPeriksa.getTglPeriksa().getYear() + 1900, dataPeriksa.getTglPeriksa().getMonth(), dataPeriksa.getTglPeriksa().getDate());
				calPenetapan.setDate(dataPeriksa.getTglSkp().getYear() + 1900, dataPeriksa.getTglSkp().getMonth(), dataPeriksa.getTglSkp().getDate());
				txtNoPemeriksaan.setText(dataPeriksa.getNoPeriksa());
				txtNoSKPDKB.setText(dataPeriksa.getNoSkp());
				txtKeterangan.setText(dataPeriksa.getKeterangan());
				NoSKP = dataPeriksa.getNoSkp();
				NoNPPD = dataPeriksa.getNoNppd();
				
				if (btnPeriksa1.getSelection())
				{
					createTableColumn();
					periksa1();
					setBunga();
					hitungPajak();
				}
				else if (btnPeriksa2.getSelection())
				{
					createTableColumn();
					periksa2();
					hitungPajak();
				}else if (btnPeriksaBpk.getSelection()){
					createTableColumn();
					periksaBPK();
					hitungPajakBPK();
				}
				
				if (txtNoSKPDKB.getText().equalsIgnoreCase("")){
					btnSimpan.setEnabled(true);
					btnHapus.setEnabled(true);
				}
				else{
					if (userModul.getHapus())
						btnHapus.setEnabled(true);
					else
						btnHapus.setEnabled(false);
					
					if (userModul.getSimpan())
						btnSimpan.setEnabled(true);
					else
						btnSimpan.setEnabled(false);
				}
			}
		});
		cmbMasaPajak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		cmbMasaPajak.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		GridData gd_cmbMasaPajak = new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1);
		gd_cmbMasaPajak.widthHint = 242;
		cmbMasaPajak.setLayoutData(gd_cmbMasaPajak);
		
		spinner = new Spinner(comp, SWT.BORDER);
		spinner.setVisible(false);
		
		Label lblTanggalLhp = new Label(comp, SWT.NONE);
		lblTanggalLhp.setForeground(fontColor);
		lblTanggalLhp.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblTanggalLhp.setText("Tanggal LHP");
		new Label(comp, SWT.NONE);
		
		cmbMasaPajakDari = new Combo(comp, SWT.NONE);
		cmbMasaPajakDari.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		cmbMasaPajakDari.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				indexAwal = cmbMasaPajakDari.getSelectionIndex();
			}
		});
		cmbMasaPajakDari.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
						
				loadMasaPajakPeriksaSampai(listMasaPajakDari.get(cmbMasaPajakDari.getSelectionIndex()).getMasaPajakDari());
				//MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "Test", listMasaPajakDari.get(cmbMasaPajakDari.getSelectionIndex()).getMasaPajakDari().toString());
				masaPajakDari = listMasaPajakDari.get(cmbMasaPajakDari.getSelectionIndex()).getMasaPajakDari();
			}
		});
		cmbMasaPajakDari.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		cmbMasaPajakDari.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		cmbMasaPajakDari.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 3, 1));
		
		Label lblSD = new Label(comp, SWT.NONE);
		lblSD.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		lblSD.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BORDER));
		lblSD.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblSD.setText("s/d");
		
		cmbMasaPajakSampai = new Combo(comp, SWT.NONE);
		cmbMasaPajakSampai.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		cmbMasaPajakSampai.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				indexAkhir = cmbMasaPajakSampai.getSelectionIndex();
			}
		});
		cmbMasaPajakSampai.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "Test", listMasaPajakSampai.get(cmbMasaPajakSampai.getSelectionIndex()).getMasaPajakSampai().toString());
				masaPajakSampai = listMasaPajakSampai.get(cmbMasaPajakSampai.getSelectionIndex()).getMasaPajakSampai();
			}
		});
		cmbMasaPajakSampai.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		cmbMasaPajakSampai.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		GridData gd_cmbMasaPajakSampai = new GridData(SWT.LEFT, SWT.CENTER, true, false, 3, 1);
		gd_cmbMasaPajakSampai.widthHint = 173;
		cmbMasaPajakSampai.setLayoutData(gd_cmbMasaPajakSampai);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		
		Composite compButton1 = new Composite(comp, SWT.NONE);
		compButton1.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 7, 1));
		compButton1.setLayout(new GridLayout(3, false));
		
		 btnPeriksa = new Button(compButton1, SWT.NONE);
		 btnPeriksa.addSelectionListener(new SelectionAdapter() {
		 	@Override
		 	public void widgetSelected(SelectionEvent e) {
		 		NoSKP = "";
		 		tbl_Periksa.removeAll();
		 		btnSimpan.setEnabled(true);
		 		btnHapus.setEnabled(true);
		 		if (btnPeriksa1.getSelection())
		 		{
		 			createTableColumn();
		 			periksa1();
		 			setBunga();
		 			hitungPajak();
		 		}
		 		else if (btnPeriksa2.getSelection())
		 		{
		 			createTableColumn();
		 			periksa2();
		 			hitungPajak();
		 		}
		 		else if (btnPeriksaBpk.getSelection())
		 		{
		 			createTableColumn();
		 			periksaBPK();
//		 			hitungPajak();
		 		}
		 		else
		 		{
		 			compPemeriksaan.setFocus();
		 			MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Pilih jenis pemeriksaan");
		 		}
		 		
		 	}
		 });
		 btnPeriksa.setEnabled(false);
		GridData gd_btnPeriksa = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnPeriksa.widthHint = 90;
		btnPeriksa.setLayoutData(gd_btnPeriksa);
		btnPeriksa.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnPeriksa.setText("Periksa");
		
		Button btnBaru = new Button(compButton1, SWT.NONE);
		btnBaru.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				resetForm();
			}
		});
		GridData gd_btnBaru = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnBaru.widthHint = 90;
		btnBaru.setLayoutData(gd_btnBaru);
		btnBaru.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnBaru.setText("Baru");
		
		Button btnCetak = new Button(compButton1, SWT.NONE);
		btnCetak.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(isPrint()){
					if (btnPeriksa1.getSelection()||btnPeriksaBpk.getSelection()){
						Double totalSPTPD = 0.0;
						Double totalSSPD = 0.0;
						Double totalSelisih = 0.0;
						Double totalPeriksa = 0.0;
						Double totalDenda = 0.0;
						ReportAppContext.obj = new String[]{"DaftarPemeriksaan", "DaftarPemeriksaan2"};
						ReportAppContext.listObject = new Object[2];
						HashMap<String, List<String>> DaftarPemeriksaan = new HashMap<String, List<String>>();
						HashMap<String, List<String>> DaftarPemeriksaan2 = new HashMap<String, List<String>>();
						String bulanPeriksa = "";
						ReportAppContext.name = "Variable";
						ReportAppContext.map.put("NamaPajak", ControllerFactory.getMainController().getCpPeriksaDAOImpl().GetNamaPajak(txtKodePajak.getText()));
						ReportAppContext.nameObject = "DaftarPemeriksaan2";
						ReportAppContext.classLoader = PemeriksaanView.class.getClassLoader();
						if(!ReportAppContext.object.isEmpty())
							ReportAppContext.object.clear();
						
						for(int j = 0;j<tbl_Periksa.getItems().length;j++){// untuk masukan row table
							List<String> values2 = new ArrayList<String>();
							for(int i=0;i<tbl_Periksa.getColumnCount();i++){// untuk masukkan data berdasarkan colom table
									values2.add(tbl_Periksa.getItem(j).getText(i));
							}
							if (tbl_Periksa.getItem(j).getText(11).startsWith("-Rp"))
								totalSPTPD += Double.valueOf(tbl_Periksa.getItem(j).getText(11).substring(3).replace(".", "").replace(",", "."));
							else
								totalSPTPD += Double.valueOf(tbl_Periksa.getItem(j).getText(11).substring(2).replace(".", "").replace(",", "."));
							if (tbl_Periksa.getItem(j).getText(12).startsWith("-Rp"))
								totalSSPD+= Double.valueOf(tbl_Periksa.getItem(j).getText(12).substring(3).replace(".", "").replace(",", "."));
							else
								totalSSPD += Double.valueOf(tbl_Periksa.getItem(j).getText(12).substring(2).replace(".", "").replace(",", "."));
							if (tbl_Periksa.getItem(j).getText(13).startsWith("-Rp"))
								totalPeriksa += Double.valueOf(tbl_Periksa.getItem(j).getText(13).substring(3).replace(".", "").replace(",", "."));
							else
								totalPeriksa += Double.valueOf(tbl_Periksa.getItem(j).getText(13).substring(2).replace(".", "").replace(",", "."));
							if (tbl_Periksa.getItem(j).getText(14).startsWith("-Rp"))
								totalSelisih += Double.valueOf(tbl_Periksa.getItem(j).getText(14).substring(3).replace(".", "").replace(",", "."));
							else
								totalSelisih += Double.valueOf(tbl_Periksa.getItem(j).getText(14).substring(2).replace(".", "").replace(",", "."));
							if (tbl_Periksa.getItem(j).getText(16).startsWith("-Rp"))
								totalDenda += Double.valueOf(tbl_Periksa.getItem(j).getText(16).substring(3).replace(".", "").replace(",", "."));
							else
								totalDenda += Double.valueOf(tbl_Periksa.getItem(j).getText(16).substring(2).replace(".", "").replace(",", "."));
							DaftarPemeriksaan2.put(Integer.toString(j), values2);
						}
						
						List<String> values = new ArrayList<String>();
						values.add(txtNamaBadan.getText());
						values.add(txtAlamatBadan.getText());
						values.add(wp.getNPWP());
						values.add(ControllerFactory.getMainController().getCpPeriksaDAOImpl().GetNamaPajak(txtKodePajak.getText()));
						bulanPeriksa =  tbl_Periksa.getItem(0).getText(6) + " s/d " + tbl_Periksa.getItem(tbl_Periksa.getItemCount() - 1).getText(6);
						values.add(bulanPeriksa);
						values.add(indFormat.format(txtTotal.getMoney().getNumber().doubleValue()));
						values.add(indFormat.format(roundUP(totalSPTPD)));
						values.add(indFormat.format(roundUP(totalPeriksa)));
						values.add(indFormat.format(roundUP(totalSelisih)));
						values.add(indFormat.format(roundUP(totalSSPD)));
						values.add(indFormat.format(roundUP(totalDenda)));
						DaftarPemeriksaan.put(Integer.toString(0), values);
						
						ReportAppContext.listObject[0] = DaftarPemeriksaan;
						ReportAppContext.listObject[1] = DaftarPemeriksaan2;
						
						
						ReportAction.start("DaftarPemeriksaan.rptdesign");
					}else if (btnPeriksa2.getSelection()){
						Double totalDasarPeng = 0.0;
						Double totalPengPeriksa = 0.0;
						Double totalSelKurang = 0.0;
						Double totalSPTPD = 0.0;
						Double totalSelisih = 0.0;
						Double totalPeriksa = 0.0;
						Double totalKenaikan = 0.0;
						Double totalSKPDKBT = 0.0;
						ReportAppContext.obj = new String[]{"DaftarPemeriksaanSKPDKBT", "DaftarPemeriksaanSKPDKBT1"};
						ReportAppContext.listObject = new Object[2];
						HashMap<String, List<String>> DaftarPemeriksaanSKPDKBT = new HashMap<String, List<String>>();
						HashMap<String, List<String>> DaftarPemeriksaanSKPDKBT1 = new HashMap<String, List<String>>();
						String bulanPeriksa = "";
						ReportAppContext.name = "Variable";
						ReportAppContext.map.put("NamaPajak", ControllerFactory.getMainController().getCpPeriksaDAOImpl().GetNamaPajak(txtKodePajak.getText()));
						ReportAppContext.nameObject = "DaftarPemeriksaanSKPDKBT1";
						ReportAppContext.classLoader = PemeriksaanView.class.getClassLoader();
						if(!ReportAppContext.object.isEmpty())
							ReportAppContext.object.clear();
						
						for(int j = 0;j<tbl_Periksa.getItems().length;j++){// untuk masukan row table
							List<String> values2 = new ArrayList<String>();
							for(int i=0;i<tbl_Periksa.getColumnCount();i++){// untuk masukkan data berdasarkan colom table
									values2.add(tbl_Periksa.getItem(j).getText(i));
							}
							totalDasarPeng += Double.valueOf(tbl_Periksa.getItem(j).getText(7).substring(2).replace(".", "").replace(",", "."));
							totalPengPeriksa += Double.valueOf(tbl_Periksa.getItem(j).getText(8).substring(2).replace(".", "").replace(",", "."));
							totalSelKurang += Double.valueOf(tbl_Periksa.getItem(j).getText(9).substring(2).replace(".", "").replace(",", "."));
							totalSPTPD += Double.valueOf(tbl_Periksa.getItem(j).getText(11).substring(2).replace(".", "").replace(",", "."));
							totalPeriksa += Double.valueOf(tbl_Periksa.getItem(j).getText(13).substring(2).replace(".", "").replace(",", "."));
							totalSelisih += Double.valueOf(tbl_Periksa.getItem(j).getText(14).substring(2).replace(".", "").replace(",", "."));
							totalKenaikan += Double.valueOf(tbl_Periksa.getItem(j).getText(18).substring(2).replace(".", "").replace(",", "."));
							totalSKPDKBT += Double.valueOf(tbl_Periksa.getItem(j).getText(19).substring(2).replace(".", "").replace(",", "."));
//							totalDenda += Double.valueOf(tbl_Periksa.getItem(j).getText(16).substring(2).replace(".", "").replace(",", "."));
							DaftarPemeriksaanSKPDKBT1.put(Integer.toString(j), values2);
						}
						
						List<String> values = new ArrayList<String>();
						values.add(txtNamaBadan.getText());
						values.add(txtAlamatBadan.getText());
						values.add(wp.getNPWP());
						values.add(ControllerFactory.getMainController().getCpPeriksaDAOImpl().GetNamaPajak(txtKodePajak.getText()));
						bulanPeriksa =  tbl_Periksa.getItem(0).getText(6) + " s/d " + tbl_Periksa.getItem(tbl_Periksa.getItemCount() - 1).getText(6);
						values.add(bulanPeriksa);
						values.add(indFormat.format(txtTotal.getMoney().getNumber().doubleValue()));
						values.add(indFormat.format(totalSPTPD));
						values.add(indFormat.format(totalPeriksa));
						values.add(indFormat.format(totalSelisih));
						values.add(indFormat.format(totalKenaikan));
						values.add(indFormat.format(totalSKPDKBT));
						values.add(indFormat.format(totalDasarPeng));
						values.add(indFormat.format(totalPengPeriksa));
						values.add(indFormat.format(totalSelKurang));
						DaftarPemeriksaanSKPDKBT.put(Integer.toString(0), values);
						
						ReportAppContext.listObject[0] = DaftarPemeriksaanSKPDKBT;
						ReportAppContext.listObject[1] = DaftarPemeriksaanSKPDKBT1;
						
						
						ReportAction.start("DaftarPemeriksaanSKPDKBT.rptdesign");
					}
					
				}else
					MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Anda tidak memiliki hak untuk mencetak.");
			}
		});
		GridData gd_btnCetak = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnCetak.widthHint = 90;
		btnCetak.setLayoutData(gd_btnCetak);
		btnCetak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnCetak.setText("Cetak");
		
		tbl_Periksa = new Table(comp, SWT.BORDER | SWT.FULL_SELECTION | SWT.VIRTUAL);
		final TableEditor editor = new TableEditor(tbl_Periksa);
        //The editor must have the same size as the cell and must
        //not be any smaller than 50 pixels.
        editor.horizontalAlignment = SWT.LEFT;
        editor.grabHorizontal = true;
        editor.minimumWidth = 50;
		tbl_Periksa.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				if (btnPeriksaBpk.getSelection()){
					Point ptAbsolute = new Point(e.x, e.y);
		            Point pt = tbl_Periksa.toControl(ptAbsolute); // get position relative to the Tree widget
		            int colIndex = columnAtPoint(tbl_Periksa, pt) + spinner.getSelection();
//		            MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "", String.valueOf(colIndex));
		            final int colFinal = colIndex;
		            Double jlhBayar = 0.0;
		            Double jlhPeriksa = 0.0;
		            Control oldEditor = editor.getEditor();
	            	if (oldEditor != null) oldEditor.dispose();
	            	
	            	TableItem item = tbl_Periksa.getItem(tbl_Periksa.getSelectionIndex());
	            	if (item == null) return;
	            	
	            	if (colIndex==7||colIndex==8||colIndex==9||colIndex==11||colIndex==12||colIndex==13||colIndex==14||colIndex==16||colIndex==17||colIndex==18){
	            		MoneyField newEditor = new MoneyField(tbl_Periksa, SWT.NONE);
//						try {
							newEditor.setMoney(new CurrencyAmount(0, Currency.getInstance(ind)));
//							jlhBayar = indFormat.parse(item.getText(colFinal-1)).doubleValue();
						
//						final Double bayar = jlhBayar;
						newEditor.getText();
						Listener textListener = new Listener() {
	                        private MoneyField text;
							public void handleEvent(final Event e) {
	                          switch (e.type) {
	                          case SWT.Traverse:
	                            switch (e.detail) {
	                            case SWT.TRAVERSE_RETURN:
	                            	text = (MoneyField)editor.getEditor();
	                            	Double total = 0.0;
									try {
										editor.getItem().setText(colFinal, indFormat.format(text.getMoney().getNumber().doubleValue()));
		                            	if (colFinal == 8){
											editor.getItem().setText(9, indFormat.format(indFormat.parse(tbl_Periksa.getItem(tbl_Periksa.getSelectionIndex()).getText(8)).doubleValue()-indFormat.parse(tbl_Periksa.getItem(tbl_Periksa.getSelectionIndex()).getText(7)).doubleValue()));
										}
										if (colFinal == 11){
											editor.getItem().setText(12, tbl_Periksa.getItem(tbl_Periksa.getSelectionIndex()).getText(11));
										}
										if (colFinal == 13){
											editor.getItem().setText(14, indFormat.format(indFormat.parse(tbl_Periksa.getItem(tbl_Periksa.getSelectionIndex()).getText(13)).doubleValue()-indFormat.parse(tbl_Periksa.getItem(tbl_Periksa.getSelectionIndex()).getText(11)).doubleValue()));
										}
										total = indFormat.parse(tbl_Periksa.getItem(tbl_Periksa.getSelectionIndex()).getText(14)).doubleValue() + 
												indFormat.parse(tbl_Periksa.getItem(tbl_Periksa.getSelectionIndex()).getText(16)).doubleValue() + 
												indFormat.parse(tbl_Periksa.getItem(tbl_Periksa.getSelectionIndex()).getText(17)).doubleValue() + 
												indFormat.parse(tbl_Periksa.getItem(tbl_Periksa.getSelectionIndex()).getText(18)).doubleValue();
										
										editor.getItem().setText(19, indFormat.format(total));
		                            	hitungPajakBPK();
									} catch (ParseException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
	                            case SWT.TRAVERSE_ESCAPE:
	                            	text = (MoneyField)editor.getEditor();
	                              text.dispose();
	                              e.doit = false;
	                            }
	                            break;
	                          }
	                        }
					};
					newEditor.addListener(SWT.Traverse, textListener);
	            	newEditor.setFocus();
	            	editor.setEditor(newEditor, item, colIndex);
	            	}else if (colIndex==15){ //colIndex==15
	            		Text newEditor = new Text(tbl_Periksa, SWT.NONE);
						newEditor.setText(item.getText(colIndex)); //(new CurrencyAmount(indFormat.parse(item.getText(colIndex)), Currency.getInstance(ind)));
						newEditor.getText();
						try {
							jlhPeriksa = indFormat.parse(item.getText(14)).doubleValue();
						} catch (ParseException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						final Double periksa = jlhPeriksa;
						Listener textListener = new Listener() {
	                        private Text text;
							public void handleEvent(final Event e) {
	                          switch (e.type) {
	                          case SWT.Traverse:
	                            switch (e.detail) {
	                            case SWT.TRAVERSE_RETURN:
	                            	text = (Text)editor.getEditor();
	                            	Double bunga = (Double.valueOf(text.getText())/100) * periksa;
	                            	if (bunga < 0.0)
	                            		bunga = 0.0;
	                            	editor.getItem().setText(colFinal, text.getText());
	                            	editor.getItem().setText(colFinal+1, indFormat.format(bunga));
	                            	Double total = 0.0;
									try {
										total = indFormat.parse(tbl_Periksa.getItem(tbl_Periksa.getSelectionIndex()).getText(14)).doubleValue() + 
												indFormat.parse(tbl_Periksa.getItem(tbl_Periksa.getSelectionIndex()).getText(16)).doubleValue() + 
												indFormat.parse(tbl_Periksa.getItem(tbl_Periksa.getSelectionIndex()).getText(17)).doubleValue() + 
												indFormat.parse(tbl_Periksa.getItem(tbl_Periksa.getSelectionIndex()).getText(18)).doubleValue();
									} catch (ParseException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
									editor.getItem().setText(19, indFormat.format(total));
	                            	hitungPajakBPK();
	                            case SWT.TRAVERSE_ESCAPE:
	                            	text = (Text)editor.getEditor();
	                              text.dispose();
	                              e.doit = false;
	                            }
	                            break;
	                          }
	                        }
					};
					newEditor.addListener(SWT.Traverse, textListener);
	            	newEditor.setFocus();
	            	editor.setEditor(newEditor, item, colIndex);
	            	}
				}
			}
		});

		tbl_Periksa.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		tbl_Periksa.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE-1, SWT.NORMAL));
		tbl_Periksa.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		GridData gd_tbl_Periksa = new GridData(SWT.FILL, SWT.FILL, true, true, 9, 1);
		gd_tbl_Periksa.heightHint = 140;
		gd_tbl_Periksa.widthHint = 667;
		tbl_Periksa.setLayoutData(gd_tbl_Periksa);
		tbl_Periksa.setHeaderVisible(true);
		tbl_Periksa.setLinesVisible(true);
		
		/*final TableEditor editor = new TableEditor(tbl_Periksa);
        //The editor must have the same size as the cell and must
        //not be any smaller than 50 pixels.
        editor.horizontalAlignment = SWT.LEFT;
        editor.grabHorizontal = true;
        editor.minimumWidth = 50;*/
        // editing the second column
        final int EDITABLECOLUMN = 13;
		tbl_Periksa.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (!btnPeriksaBpk.getSelection() && txtNoSKPDKB.getText().equals("")){ //&& tbl_Periksa.getItem(tbl_Periksa.getSelectionIndex()).getText(0).equals(null)){
					if(e.keyCode==SWT.CR || e.keyCode == SWT.KEYPAD_CR){
						if(tbl_Periksa.getSelectionCount()>0){
							Control oldEditor = editor.getEditor();
		                	if (oldEditor != null) oldEditor.dispose();
		        
		                	// Identify the selected row
		                	TableItem item = tbl_Periksa.getItem(tbl_Periksa.getSelectionIndex());
		                	if (item == null) return;
		        
	                        // The control that will be the editor must be a child of the Table
		                	MoneyField newEditor = new MoneyField(tbl_Periksa, SWT.NONE);
		                	try {
								newEditor.setMoney(new CurrencyAmount(indFormat.parse(item.getText(EDITABLECOLUMN)), Currency.getInstance(ind)));
								newEditor.getText().selectAll();
							} catch (ParseException e1) {
								e1.printStackTrace();
							}
		                	Listener textListener = new Listener() {
		                        private MoneyField text;
								public void handleEvent(final Event e) {
		                          switch (e.type) {
		                          case SWT.Traverse:
		                            switch (e.detail) {
		                            case SWT.TRAVERSE_RETURN:
		                            	text = (MoneyField)editor.getEditor();
		                            	try {
		                            		double dasarPengenaan,pengenaanPeriksa, selisihKurang, /*jumlahBayar,*/ 
		                            		pajakPeriksa/*, 
		                            		kurangBayar, bunga, dendaTambahan, kenaikan, totalPeriksa*/;
		                            		int tarifPajak/*, bungaPersen*/;
		                            		
		                            		tarifPajak = Integer.parseInt(editor.getItem().getText(10));
//		                            		bungaPersen = Integer.parseInt(editor.getItem().getText(15));
		                            		
		                            		pajakPeriksa = text.getMoney().getNumber().doubleValue();
		                            		pengenaanPeriksa = pajakPeriksa * 100 / tarifPajak;
		                            		dasarPengenaan = indFormat.parse(editor.getItem().getText(7)).doubleValue();
		                            		selisihKurang = dasarPengenaan - pengenaanPeriksa;
//		                            		jumlahBayar = indFormat.parse(editor.getItem().getText(12)).doubleValue();
//		                            		kurangBayar = pajakPeriksa - jumlahBayar;
//		                            		bunga = kurangBayar * bungaPersen / 100;
//		                            		dendaTambahan = 0;
//		                            		kenaikan = 0;
//		                            		totalPeriksa = kurangBayar + bunga + dendaTambahan + kenaikan;
		                            		
		                            		editor.getItem().setText(EDITABLECOLUMN, indFormat.format(pajakPeriksa));
		                            		editor.getItem().setText(8, indFormat.format(pengenaanPeriksa));
											editor.getItem().setText(9, indFormat.format(selisihKurang));
											
											hitungPajak();
//											editor.getItem().setText(14, indFormat.format(kurangBayar));
//											editor.getItem().setText(16, indFormat.format(bunga));
//											editor.getItem().setText(17, indFormat.format(dendaTambahan));
//											editor.getItem().setText(18, indFormat.format(kenaikan));
//											editor.getItem().setText(19, indFormat.format(totalPeriksa));
										} catch (NumberFormatException e1) {
											e1.printStackTrace();
										} catch (ParseException e1) {
											e1.printStackTrace();
										}
		                            	tbl_Periksa.setSelection(tbl_Periksa.getSelectionIndex()+1);
		                            case SWT.TRAVERSE_ESCAPE:
		                            	text = (MoneyField)editor.getEditor();
		                              text.dispose();
		                              e.doit = false;
		                            }
		                            break;
		                          }
		                       }
		                    };
		                    newEditor.addListener(SWT.Traverse, textListener);
		                	newEditor.setFocus();
		                	editor.setEditor(newEditor, item, EDITABLECOLUMN);
						}
					}
				}
			}
		});
		
		Label lblTotalPajakTerhutang = new Label(comp, SWT.NONE);
		lblTotalPajakTerhutang.setForeground(fontColor);
		lblTotalPajakTerhutang.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblTotalPajakTerhutang.setText("Total Pajak Terhutang");
		new Label(comp, SWT.NONE);
		
		txtTotalPajakTerhutang = new MoneyField(comp, SWT.BORDER);
		txtTotalPajakTerhutang.getText().setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		txtTotalPajakTerhutang.getText().setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtTotalPajakTerhutang.getText().setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtTotalPajakTerhutang.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtTotalPajakTerhutang.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		GridData gd_txtTotalPajakTerhutang = new GridData(SWT.FILL, SWT.CENTER, false, false, 4, 1);
		gd_txtTotalPajakTerhutang.widthHint = 201;
		txtTotalPajakTerhutang.setLayoutData(gd_txtTotalPajakTerhutang);
		new Label(txtTotalPajakTerhutang, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		
		lblKenaikan = new Label(comp, SWT.NONE);
		GridData gd_lblKenaikan = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_lblKenaikan.widthHint = 139;
		lblKenaikan.setLayoutData(gd_lblKenaikan);
		lblKenaikan.setForeground(fontColor);
		lblKenaikan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblKenaikan.setText("Kenaikan (%)");
		
		chkKenaikan = new Button(comp, SWT.CHECK);
		chkKenaikan.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (chkKenaikan.getSelection())
				{
					double kenaikan = txtTotalPajakTerhutang.getMoney().getNumber().doubleValue() * (kenaikanPersen/ 100.0);
					txtKenaikan.setMoney(new CurrencyAmount(kenaikan, Currency.getInstance(ind)));
				}
				else
				{
					txtKenaikan.setMoney(new CurrencyAmount(0, Currency.getInstance(ind)));
				}
			}
		});
		
		txtKenaikan = new MoneyField(comp, SWT.BORDER);
		txtKenaikan.getText().setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		txtKenaikan.getText().setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtKenaikan.getText().setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtKenaikan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtKenaikan.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		txtKenaikan.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 4, 1));
		new Label(txtKenaikan, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		
		Label lblTotal = new Label(comp, SWT.NONE);
		lblTotal.setForeground(fontColor);
		lblTotal.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblTotal.setText("TOTAL");
		new Label(comp, SWT.NONE);
		
		txtTotal = new MoneyField(comp, SWT.BORDER);
		txtTotal.getText().setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		txtTotal.getText().setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtTotal.getText().setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtTotal.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtTotal.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		txtTotal.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 4, 1));
		new Label(txtTotal, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		
		Label lblNoPemeriksaan = new Label(comp, SWT.NONE);
		lblNoPemeriksaan.setForeground(fontColor);
		lblNoPemeriksaan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblNoPemeriksaan.setText("No. LHP");
		new Label(comp, SWT.NONE);
		
		txtNoPemeriksaan = new Text(comp, SWT.BORDER);
		txtNoPemeriksaan.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		txtNoPemeriksaan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtNoPemeriksaan.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtNoPemeriksaan.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 4, 1));
		new Label(comp, SWT.NONE);
		
		lblTanggalPemeriksaan = new Label(comp, SWT.NONE);
		lblTanggalPemeriksaan.setForeground(fontColor);
		lblTanggalPemeriksaan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblTanggalPemeriksaan.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		lblTanggalPemeriksaan.setText("Tanggal Pemeriksaan");
		
		calPemeriksaan = new DateTime(comp, SWT.BORDER | SWT.DROP_DOWN);
		calPemeriksaan.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		calPemeriksaan.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		calPemeriksaan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		calPemeriksaan.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setBunga();
				hitungPajak();
				if (!calPenetapan.getVisible())
					calPenetapan.setDate(calPemeriksaan.getYear(), calPemeriksaan.getMonth(), calPemeriksaan.getDay());
			}
		});
		
		Label lblNoSkpdkbskpdnskpdkbt = new Label(comp, SWT.NONE);
		lblNoSkpdkbskpdnskpdkbt.setForeground(fontColor);
		lblNoSkpdkbskpdnskpdkbt.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblNoSkpdkbskpdnskpdkbt.setText("No. SKPDKB/SKPDN/SKPDKBT");
		new Label(comp, SWT.NONE);
		
		txtNoSKPDKB = new Text(comp, SWT.BORDER);
		txtNoSKPDKB.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		txtNoSKPDKB.setEditable(false);
		txtNoSKPDKB.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtNoSKPDKB.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtNoSKPDKB.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 4, 1));
		new Label(comp, SWT.NONE);
		
		lblTanggalPenetapan = new Label(comp, SWT.NONE);
		lblTanggalPenetapan.setForeground(fontColor);
		lblTanggalPenetapan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblTanggalPenetapan.setText("Tanggal Penetapan");
		lblTanggalPenetapan.setVisible(false);
		
		calPenetapan = new DateTime(comp, SWT.BORDER | SWT.DROP_DOWN);
		calPenetapan.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		calPenetapan.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		calPenetapan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		calPenetapan.setVisible(false);
		
		Label lblKeterangan = new Label(comp, SWT.NONE);
		lblKeterangan.setForeground(fontColor);
		lblKeterangan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblKeterangan.setText("Keterangan");
		new Label(comp, SWT.NONE);
		
		txtKeterangan = new Text(comp, SWT.BORDER);
		txtKeterangan.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		txtKeterangan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtKeterangan.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtKeterangan.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 4, 1));
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		
		btnMagic = new Button(comp, SWT.NONE);
		btnMagic.setVisible(false);
		btnMagic.addSelectionListener(new SelectionAdapter() {
			@SuppressWarnings("deprecation")
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(userModul.getIdUser() == 1){
					if (isValidate()){
						PeriksaDetailProvider.INSTANCE.getPeriksaDetail().clear();
	
						if (btnPeriksa1.getSelection()){
							jenisPeriksa = "Pemeriksaan 1";
							if (txtTotal.getMoney().getNumber().doubleValue() > 0)
								tipeSKP = "SKPDKB";
							else
								tipeSKP = "SKPDN";
						}
						else{
							jenisPeriksa = "Pemeriksaan 2";
							tipeSKP = "SKPDKBT";
						}
						
						NoSKP = dataPeriksa.getNoSkp();//ControllerFactory.getMainController().getCpPeriksaDAOImpl().getNoSKP(String.valueOf(calPemeriksaan.getYear()), idSubPajak, tipeSKP);
						NoNPPD = NoSKP.split("/")[0] + "/NPPD/" + NoSKP.split("/")[2];
						
						Date tglPeriksa = new Date(calPemeriksaan.getYear() - 1900, calPemeriksaan.getMonth(), calPemeriksaan.getDay());
						Date tglPenetapan = new Date(calPenetapan.getYear() - 1900, calPenetapan.getMonth(), calPenetapan.getDay());
						Periksa periksa = new Periksa();
						periksa.setIdPeriksa(idPeriksa);
						periksa.setNoPeriksa(txtNoPemeriksaan.getText());
						periksa.setTglPeriksa(tglPeriksa);
						periksa.setNpwpd(wp.getNPWP());
						periksa.setJenisPeriksa(jenisPeriksa);
						periksa.setKenaikanPersen(kenaikanPersen);
						periksa.setKenaikan(txtKenaikan.getMoney().getNumber().doubleValue());
						periksa.setMasaPajakDari(masaPajakDari);
						periksa.setMasaPajakSampai(masaPajakSampai);
						periksa.setNoSkp(NoSKP);
						periksa.setTglSkp(tglPenetapan);
						periksa.setNoNppd(NoNPPD);
						periksa.setTglNppd(tglPenetapan);
						periksa.setIdSubPajak(idSubPajak);
						periksa.setTipeSkp(tipeSKP);
						periksa.setTotalPajakPeriksa(totalPajakPeriksa);
						periksa.setTotalPajakTerutang(totalPajakTerutang);
						periksa.setTotalPajakBunga(totalBunga);
						periksa.setTotalKenaikan(totalKenaikan);
						periksa.setTotalDenda(totalDenda);
						periksa.setKeterangan(txtKeterangan.getText());
						
						try{
							Integer idDetail;
							if (btnPeriksa1.getSelection()){
								for (TableItem item : tbl_Periksa.getItems()){
									PeriksaDetail pd = new PeriksaDetail();
									if (item.getText(4).equalsIgnoreCase("0"))
										idDetail = null;
									else
										idDetail = Integer.valueOf(item.getText(4));
									pd.setIdPeriksaDetail(idDetail);
									pd.setIdPeriksa(idPeriksa);
									pd.setNpwpd(wp.getNPWP());
									pd.setBulanPeriksa(item.getText(2));
									pd.setPengenaanPeriksa(indFormat.parse(item.getText(8)).doubleValue());
									pd.setPajakPeriksa(indFormat.parse(item.getText(13)).doubleValue());
									pd.setBungaPersen(Integer.valueOf(item.getText(15)));
									pd.setBunga(indFormat.parse(item.getText(16)).doubleValue());
									pd.setKenaikanPajak(indFormat.parse(item.getText(18)).doubleValue());
									PeriksaDetailProvider.INSTANCE.addItem(pd);
								}
							}
							else{
								for (TableItem item : tbl_Periksa.getItems()){
									PeriksaDetail pd = new PeriksaDetail();
									if (item.getText(4).equalsIgnoreCase("0"))
										idDetail = null;
									else
										idDetail = Integer.valueOf(item.getText(4));
									pd.setIdPeriksaDetail(idDetail);
									pd.setIdPeriksa(idPeriksa);
									pd.setNpwpd(wp.getNPWP());
									pd.setBulanPeriksa(item.getText(2));
									pd.setDasarPengenaan(indFormat.parse(item.getText(7)).doubleValue());
									pd.setPengenaanPeriksa(indFormat.parse(item.getText(8)).doubleValue());
									pd.setSelisihKurang(indFormat.parse(item.getText(9)).doubleValue());
									pd.setKenaikanPajak(indFormat.parse(item.getText(18)).doubleValue());
									pd.setTotalPeriksa(indFormat.parse(item.getText(19)).doubleValue());
									PeriksaDetailProvider.INSTANCE.addItem(pd);
								}
							}
						}
						catch (ParseException e1){
							e1.printStackTrace();
						}
						
						if (ControllerFactory.getMainController().getCpPeriksaDAOImpl().savePeriksa(periksa)){
							if (idPeriksa == null)
								idPeriksa = ControllerFactory.getMainController().getCpPeriksaDAOImpl().getLastIdPeriksa(wp.getNPWP());
							if (btnPeriksa1.getSelection()){
								if (ControllerFactory.getMainController().getCpPeriksaDetailDAOImpl().savePeriksaDetail(PeriksaDetailProvider.INSTANCE.getPeriksaDetail(), idPeriksa)){
									MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Data berhasil disimpan.");
									StringBuffer sb = new StringBuffer();
									sb.append("SAVE " +
											"SKPDKB:"+txtNoSKPDKB.getText()+
											" NoPemeriksaan:"+txtNoPemeriksaan.getText()+
											" NPWPD:"+wp.getNPWP()+
											" MasaPajak:"+cmbMasaPajak.getText()+
											" TanggalPeriksa:"+calPemeriksaan.getDay()+"/"+(calPemeriksaan.getMonth()+1)+"/"+calPemeriksaan.getYear()+
											" TanggalPenetapan:"+calPenetapan.getDay()+"/"+(calPenetapan.getMonth()+1)+"/"+calPenetapan.getYear()+
											" PajakTerhutang:"+txtTotalPajakTerhutang.getText().getText());
									new LogImp().setLog(sb.toString(), userModul.getDisplayName(), userModul.getSubModul().getNamaModul());
									//txtNoSKPDKB.setText(NoSKP);
									loadComboPeriksa1();
									cmbMasaPajak.select(0);
									refreshTable();
	//								load();
								}else
									MessageDialog.openError(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Error pada Database. Silahkan hubungi ADMIN.");
							}
							else{
								if (ControllerFactory.getMainController().getCpPeriksaDetailDAOImpl().savePeriksaDetail2(PeriksaDetailProvider.INSTANCE.getPeriksaDetail(), idPeriksa)){
									MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Data berhasil disimpan.");
									StringBuffer sb = new StringBuffer();
									sb.append("SAVE " +
											"SKPDKB:"+txtNoSKPDKB.getText()+
											" NoPemeriksaan:"+txtNoPemeriksaan.getText()+
											" NPWPD:"+wp.getNPWP()+
											" MasaPajak:"+cmbMasaPajak.getText()+
											" TanggalPeriksa:"+calPemeriksaan.getDay()+"/"+(calPemeriksaan.getMonth()+1)+"/"+calPemeriksaan.getYear()+
											" TanggalPenetapan:"+calPenetapan.getDay()+"/"+(calPenetapan.getMonth()+1)+"/"+calPenetapan.getYear()+
											" PajakTerhutang:"+txtTotalPajakTerhutang.getText().getText());
									new LogImp().setLog(sb.toString(), userModul.getDisplayName(), userModul.getSubModul().getNamaModul());
									//txtNoSKPDKB.setText(NoSKP);
									loadComboPeriksa2();
									cmbMasaPajak.select(0);
									refreshTable();
	//								load();
								}else
									MessageDialog.openError(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Error pada Database. Silahkan hubungi ADMIN.");
							}
						}else
							MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Silahkan periksa kembali.");
					}
				}else
					MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Anda tidak memiliki hak untuk menyimpan atau merubah data.");
			}
		});
		btnMagic.setText("Btn");
		if (userModul.getIdUser() == 1)
			btnMagic.setVisible(true);
		new Label(comp, SWT.NONE);
		
		Composite compoButton2 = new Composite(comp, SWT.NONE);
		compoButton2.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 7, 1));
		compoButton2.setLayout(new GridLayout(2, false));
		
		btnSimpan = new Button(compoButton2, SWT.NONE);
		btnSimpan.setEnabled(false);
		btnSimpan.addSelectionListener(new SelectionAdapter() {
			@SuppressWarnings("deprecation")
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(isSave()){
					if (isValidate()){
						PeriksaDetailProvider.INSTANCE.getPeriksaDetail().clear();
						txtNoSKPDKB.setBackground(SWTResourceManager.getColor(SWT.COLOR_RED));
						if (btnPeriksa1.getSelection()){
							jenisPeriksa = "Pemeriksaan 1";
							if (txtTotal.getMoney().getNumber().doubleValue() > 0)
								tipeSKP = "SKPDKB";
							else
								tipeSKP = "SKPDN";
						}else if (btnPeriksaBpk.getSelection()){
							jenisPeriksa = "Pemeriksaan 1";
							tipeSKP = "SKPDKB";
							hitungPajakBPK();
						}
						else{
							jenisPeriksa = "Pemeriksaan 2";
							tipeSKP = "SKPDKBT";
						}
	//					if (NoSKP.equalsIgnoreCase("")){
	//						NoSKP = ControllerFactory.getMainController().getCpPeriksaDAOImpl().getNoSKP(String.valueOf(calPemeriksaan.getYear()), idSubPajak, tipeSKP);
	//						NoNPPD = ControllerFactory.getMainController().getCpPeriksaDAOImpl().getNoNPPD(String.valueOf(calPemeriksaan.getYear()), idSubPajak, tipeSKP);
	//					}
						if (!NoSKP.equalsIgnoreCase("")){
							NoSKP = txtNoSKPDKB.getText();
							NoNPPD = NoSKP.split("/")[0] + "/NPPD/" + NoSKP.split("/")[2];
						}
						else{
							NoSKP = "";
							NoNPPD = "";
						}
						
						Date tglPeriksa = new Date(calPemeriksaan.getYear() - 1900, calPemeriksaan.getMonth(), calPemeriksaan.getDay());
						Date tglPenetapan = new Date(calPenetapan.getYear() - 1900, calPenetapan.getMonth(), calPenetapan.getDay());
						Periksa periksa = new Periksa();
						periksa.setIdPeriksa(idPeriksa);
						periksa.setNoPeriksa(txtNoPemeriksaan.getText());
						periksa.setTglPeriksa(tglPeriksa);
						periksa.setNpwpd(wp.getNPWP());
						periksa.setJenisPeriksa(jenisPeriksa);
						periksa.setKenaikanPersen(kenaikanPersen);
						periksa.setKenaikan(txtKenaikan.getMoney().getNumber().doubleValue());
						periksa.setMasaPajakDari(masaPajakDari);
						periksa.setMasaPajakSampai(masaPajakSampai);
						periksa.setNoSkp(NoSKP);
						periksa.setTglSkp(tglPenetapan);
						periksa.setNoNppd(NoNPPD);
						periksa.setTglNppd(tglPenetapan);
						periksa.setIdSubPajak(idSubPajak);
						periksa.setTipeSkp(tipeSKP);
						periksa.setTotalPajakPeriksa(totalPajakPeriksa);
						periksa.setTotalPajakTerutang(totalPajakTerutang);
						periksa.setTotalPajakBunga(totalBunga);
						periksa.setTotalKenaikan(totalKenaikan);
						periksa.setTotalDenda(totalDenda);
						periksa.setKeterangan(txtKeterangan.getText());
						
						try{
							Integer idDetail;
							if (btnPeriksa1.getSelection()){
								for (TableItem item : tbl_Periksa.getItems()){
									PeriksaDetail pd = new PeriksaDetail();
									if (item.getText(4).equalsIgnoreCase("0"))
										idDetail = null;
									else
										idDetail = Integer.valueOf(item.getText(4));
									pd.setIdPeriksaDetail(idDetail);
									pd.setIdPeriksa(idPeriksa);
									pd.setNpwpd(wp.getNPWP());
									pd.setBulanPeriksa(item.getText(2));
									pd.setPengenaanPeriksa(indFormat.parse(item.getText(8)).doubleValue());
									pd.setPajakPeriksa(indFormat.parse(item.getText(13)).doubleValue());
									pd.setBungaPersen(Integer.valueOf(item.getText(15)));
									pd.setBunga(indFormat.parse(item.getText(16)).doubleValue());
									pd.setKenaikanPajak(indFormat.parse(item.getText(18)).doubleValue());
									PeriksaDetailProvider.INSTANCE.addItem(pd);
								}
							}else if (btnPeriksaBpk.getSelection()){
								PeriksaDetailBPKProvider.INSTANCE.removeAll();
								for (TableItem item : tbl_Periksa.getItems()){
									PeriksaDetailBPK pd = new PeriksaDetailBPK();
									if (item.getText(4).equalsIgnoreCase("0"))
										idDetail = null;
									else
										idDetail = Integer.valueOf(item.getText(4));
									pd.setIdPeriksaDetail(idDetail);
									pd.setIdPeriksa(idPeriksa);
									pd.setNpwpd(wp.getNPWP());
									pd.setBulan(item.getText(2));
									pd.setMasaPajak(item.getText(6));
									pd.setDasarPengenaan(indFormat.parse(item.getText(7)).doubleValue());
									pd.setPengenaanPeriksa(indFormat.parse(item.getText(8)).doubleValue());
									pd.setSelisihKurang(indFormat.parse(item.getText(9)).doubleValue());
									pd.setTarif(Integer.valueOf(item.getText(10)));
									pd.setPajakTerutang(indFormat.parse(item.getText(11)).doubleValue());
									pd.setJumlahBayar(indFormat.parse(item.getText(12)).doubleValue());
									pd.setPajakPeriksa(indFormat.parse(item.getText(13)).doubleValue());
									pd.setKurangBayar(indFormat.parse(item.getText(14)).doubleValue());
									pd.setBungaPersen(Integer.valueOf(item.getText(15)));
									pd.setBunga(indFormat.parse(item.getText(16)).doubleValue());
									pd.setDendaTambahan(indFormat.parse(item.getText(17)).doubleValue());
									pd.setKenaikan(indFormat.parse(item.getText(18)).doubleValue());
									pd.setTotal(indFormat.parse(item.getText(19)).doubleValue());
									PeriksaDetailBPKProvider.INSTANCE.addItem(pd);
								}
							}
							else{
								for (TableItem item : tbl_Periksa.getItems()){
									PeriksaDetail pd = new PeriksaDetail();
									if (item.getText(4).equalsIgnoreCase("0"))
										idDetail = null;
									else
										idDetail = Integer.valueOf(item.getText(4));
									pd.setIdPeriksaDetail(idDetail);
									pd.setIdPeriksa(idPeriksa);
									pd.setNpwpd(wp.getNPWP());
									pd.setBulanPeriksa(item.getText(2));
									pd.setDasarPengenaan(indFormat.parse(item.getText(7)).doubleValue());
									pd.setPengenaanPeriksa(indFormat.parse(item.getText(8)).doubleValue());
									pd.setSelisihKurang(indFormat.parse(item.getText(9)).doubleValue());
									pd.setKenaikanPajak(indFormat.parse(item.getText(18)).doubleValue());
									pd.setTotalPeriksa(indFormat.parse(item.getText(19)).doubleValue());
									PeriksaDetailProvider.INSTANCE.addItem(pd);
								}
							}
						}
						catch (ParseException e1){
							e1.printStackTrace();
						}
						
						if (ControllerFactory.getMainController().getCpPeriksaDAOImpl().savePeriksa(periksa)){
							String message = "";
							if (idPeriksa == null){
								message = "simpan";
								idPeriksa = ControllerFactory.getMainController().getCpPeriksaDAOImpl().getLastIdPeriksa(wp.getNPWP());
							}else
								message = "ubah (" + txtNoSKPDKB.getText() + ")";
							if (btnPeriksa1.getSelection()){
								if (ControllerFactory.getMainController().getCpPeriksaDetailDAOImpl().savePeriksaDetail(PeriksaDetailProvider.INSTANCE.getPeriksaDetail(), idPeriksa)){
									MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Data berhasil di" + message);
									StringBuffer sb = new StringBuffer();
									String masaPajak = "";
									if (cmbMasaPajak.getText().equalsIgnoreCase("")){
										masaPajak = masaPajakDari.getDate() + " " + UIController.INSTANCE.formatMonth(masaPajakDari.getMonth() + 1, Locale.getDefault()) + " " + (masaPajakDari.getYear() + 1900) + " - " +
												masaPajakSampai.getDate() + " " + UIController.INSTANCE.formatMonth(masaPajakSampai.getMonth() + 1, Locale.getDefault()) + " " + (masaPajakSampai.getYear() + 1900);
									}else{
										masaPajak = cmbMasaPajak.getText();
									}
									
									sb.append("SAVE " +
											"SKPDKB:"+txtNoSKPDKB.getText()+
											" NoPemeriksaan:"+txtNoPemeriksaan.getText()+
											" NPWPD:"+wp.getNPWP()+
											" MasaPajak:"+masaPajak+
											" TanggalPeriksa:"+calPemeriksaan.getDay()+"/"+(calPemeriksaan.getMonth()+1)+"/"+calPemeriksaan.getYear()+
											" TanggalPenetapan:"+calPenetapan.getDay()+"/"+(calPenetapan.getMonth()+1)+"/"+calPenetapan.getYear()+
											" PajakTerhutang:"+txtTotalPajakTerhutang.getText().getText());
									new LogImp().setLog(sb.toString(), userModul.getDisplayName(), userModul.getSubModul().getNamaModul());
									//txtNoSKPDKB.setText(NoSKP);
									loadComboPeriksa1();
									cmbMasaPajak.select(0);
									refreshTable();
	//								load();
								}else
									MessageDialog.openError(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Error pada Database. Silahkan hubungi ADMIN.");
							}else if (btnPeriksaBpk.getSelection()){
								if (ControllerFactory.getMainController().getCpPeriksaDetailBPKDAOImpl().savePeriksaDetail(PeriksaDetailBPKProvider.INSTANCE.getPeriksaDetail(), idPeriksa)){
									MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Data berhasil di" + message);
									StringBuffer sb = new StringBuffer();
									String masaPajak = "";
									if (cmbMasaPajak.getText().equalsIgnoreCase("")){
										masaPajak = masaPajakDari.getDate() + " " + UIController.INSTANCE.formatMonth(masaPajakDari.getMonth() + 1, Locale.getDefault()) + " " + (masaPajakDari.getYear() + 1900) + " - " +
												masaPajakSampai.getDate() + " " + UIController.INSTANCE.formatMonth(masaPajakSampai.getMonth() + 1, Locale.getDefault()) + " " + (masaPajakSampai.getYear() + 1900);
									}else{
										masaPajak = cmbMasaPajak.getText();
									}
									
									sb.append("SAVE " +
											"SKPDKB:"+txtNoSKPDKB.getText()+
											" NoPemeriksaan:"+txtNoPemeriksaan.getText()+
											" NPWPD:"+wp.getNPWP()+
											" MasaPajak:"+masaPajak+
											" TanggalPeriksa:"+calPemeriksaan.getDay()+"/"+(calPemeriksaan.getMonth()+1)+"/"+calPemeriksaan.getYear()+
											" TanggalPenetapan:"+calPenetapan.getDay()+"/"+(calPenetapan.getMonth()+1)+"/"+calPenetapan.getYear()+
											" PajakTerhutang:"+txtTotalPajakTerhutang.getText().getText()+ " Pemeriksaan BPK");
									new LogImp().setLog(sb.toString(), userModul.getDisplayName(), userModul.getSubModul().getNamaModul());
									//txtNoSKPDKB.setText(NoSKP);
									loadComboPeriksa1();
									cmbMasaPajak.select(0);
									refreshTable();
								}else
									MessageDialog.openError(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Error pada Database. Silahkan hubungi ADMIN.");
							}
							else{
								if (ControllerFactory.getMainController().getCpPeriksaDetailDAOImpl().savePeriksaDetail2(PeriksaDetailProvider.INSTANCE.getPeriksaDetail(), idPeriksa)){
									MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Data berhasil disimpan.");
									StringBuffer sb = new StringBuffer();
									sb.append("SAVE " +
											"SKPDKB:"+txtNoSKPDKB.getText()+
											" NoPemeriksaan:"+txtNoPemeriksaan.getText()+
											" NPWPD:"+wp.getNPWP()+
											" MasaPajak:"+cmbMasaPajak.getText()+
											" TanggalPeriksa:"+calPemeriksaan.getDay()+"/"+(calPemeriksaan.getMonth()+1)+"/"+calPemeriksaan.getYear()+
											" TanggalPenetapan:"+calPenetapan.getDay()+"/"+(calPenetapan.getMonth()+1)+"/"+calPenetapan.getYear()+
											" PajakTerhutang:"+txtTotalPajakTerhutang.getText().getText());
									new LogImp().setLog(sb.toString(), userModul.getDisplayName(), userModul.getSubModul().getNamaModul());
									//txtNoSKPDKB.setText(NoSKP);
									loadComboPeriksa2();
									cmbMasaPajak.select(0);
									refreshTable();
	//								load();
								}else
									MessageDialog.openError(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Error pada Database. Silahkan hubungi ADMIN.");
							}
						}else
							MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Silahkan periksa kembali.");
					}
				}else
					MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Anda tidak memiliki hak untuk menyimpan atau merubah data.");
			}
		});
		GridData gd_btnSimpan = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnSimpan.widthHint = 90;
		btnSimpan.setLayoutData(gd_btnSimpan);
		btnSimpan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnSimpan.setText("Simpan");
		
		btnHapus = new Button(compoButton2, SWT.NONE);
		btnHapus.setEnabled(false);
		btnHapus.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(isDelete()){
					int style = SWT.ICON_QUESTION |SWT.YES | SWT.NO;
					if (idPeriksa == null){
						MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Tidak ada data permohonan untuk dihapus");
					}
					else {
						if (MessageDialog.open(6, Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Hapus data (" + cmbMasaPajak.getText() + ")?", style)){
							if (ControllerFactory.getMainController().getCpPeriksaDAOImpl().deletePeriksa(idPeriksa))
							{
								MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Data berhasil dihapus.");
								StringBuffer sb = new StringBuffer();
								sb.append("DELETE " +
										"selectedID:"+idPeriksa+
										" SKPDKB:"+txtNoSKPDKB.getText()+
										" NoPemeriksaan:"+txtNoPemeriksaan.getText()+
										" NPWPD:"+wp.getNPWP()+
										" MasaPajak:"+cmbMasaPajak.getText()+
										" TanggalPeriksa:"+calPemeriksaan.getDay()+"/"+(calPemeriksaan.getMonth()+1)+"/"+calPemeriksaan.getYear()+
										" TanggalPenetapan:"+calPenetapan.getDay()+"/"+(calPenetapan.getMonth()+1)+"/"+calPenetapan.getYear()+
										" PajakTerhutang:"+txtTotalPajakTerhutang.getText().getText());
								new LogImp().setLog(sb.toString(), userModul.getDisplayName(), userModul.getSubModul().getNamaModul());
								resetForm();
							}else{
								MessageDialog.openError(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Error pada Database. Silahkan hubungi ADMIN.");
							}
						}
					}
				}else
					MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Anda tidak memiliki hak untuk menghapus data.");
			}
		});
		GridData gd_btnHapus = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnHapus.widthHint = 90;
		btnHapus.setLayoutData(gd_btnHapus);
		btnHapus.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnHapus.setText("Hapus");
		comp.setTabList(new Control[]{txtNoUrut, compPemeriksaan, cmbMasaPajakDari, cmbMasaPajakSampai, compButton1, txtNoPemeriksaan, txtKeterangan, compoButton2});
		scrolledComposite.setContent(comp);
		scrolledComposite.setMinSize(comp.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
	}
	
	private boolean isValidate(){
		if (wp.getNPWP().equalsIgnoreCase("") || cmbMasaPajakDari.getText().equalsIgnoreCase("") ||
				cmbMasaPajakSampai.getText().equalsIgnoreCase("") && (btnPeriksa1.getSelection() == false && btnPeriksa2.getSelection() == false)){
			return false;
		}
		else {
			return true;
		}
	}

	private void getDataWP()
	{
		wp = ControllerFactory.getMainController().getCpWajibPajakDAOImpl().getNPWPD(Integer.parseInt(txtNoUrut.getText().trim()));
		if (wp.getNPWP() != "")
		{
			resetForm();
			txtKodePajak.setText(wp.getNPWP().substring(0, 1));
			txtNoUrut.setText(wp.getNPWP().substring(1, 8));
			txtKodeKecamatan.setText(wp.getNPWP().substring(8, 10));
			txtKodeKelurahan.setText(wp.getNPWP().substring(10, 12));
			boolean akses = false;
			akses = ControllerFactory.getMainController().getCpUserDAOImpl().getAksesLHP(txtKodePajak.getText(),GlobalVariable.userModul.getIdUser());
			if(!akses){
				//System.out.println("masuk");
				MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Anda tidak memiliki hak untuk mengakses Pemeriksaan dengan jenis pajak ini.");
				resetForm();
				return;
			}
			txtNamaBadan.setText(wp.getNamaPemilik() + " / " + wp.getNamaBadan());
			txtAlamatBadan.setText(wp.getAlabadJalan());
//			assesment = wp.getJenisAssesment();
//			subPajak = wp.getBidangUsaha();
//			idSubPajak = wp.getIdSubPajak();
//			tarif = wp.getTarif();
//			txtNoSPTPD.setText("");
//			txtNoSSPD.setText("");
			
		}
	}
	
	@SuppressWarnings("deprecation")
	private void resetForm()
	{
		Control[] children = comp.getChildren();
		for (int i=0;i<children.length;i++)
		{
			if (children[i] instanceof Text)
			{
				Text child = (Text) children[i];
				
				if (!child.getText().equalsIgnoreCase(""))
				{
					child.setText("");
				}
			}
			
			else if (children[i] instanceof DateTime)
			{
				DateTime child = (DateTime) children[i];
				child.setDate(dateNow.getYear() + 1900, dateNow.getMonth(), dateNow.getDate());				
			}
			
			else if (children[i] instanceof Combo)
			{
				Combo child = (Combo) children[i];
				child.removeAll();
			}
			
			else if (children[i] instanceof MoneyField)
			{
				MoneyField child = (MoneyField) children[i];
				child.setMoney(new CurrencyAmount(0, Currency.getInstance("ind")));
			}
		}
		lblKeteranganAktif.setText("");
		lblKeteranganAktif.setVisible(false);
		lblTanggalPenetapan.setVisible(false);
		calPenetapan.setVisible(false);
		idPeriksa = null;
		btnPeriksa1.setSelection(false);
		btnPeriksa2.setSelection(false);
		btnPeriksa.setEnabled(false);
		btnSimpan.setEnabled(false);
		btnHapus.setEnabled(false);
		cmbMasaPajak.setText("");
		cmbMasaPajak.removeAll();
		cmbMasaPajakDari.setEnabled(true);
		cmbMasaPajakSampai.setEnabled(true);
		tbl_Periksa.removeAll();
		txtNoUrut.setFocus();
		NoSKP = "";
		txtNoSKPDKB.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
	}
	
	private void loadMasaPajakPeriksaDari()
	{
		listMasaPajakDari = ControllerFactory.getMainController().getCpSptpdDAOImpl().getMasaPeriksaDari();
		
		if (listMasaPajakDari.size() > 0)
		{
			UIController.INSTANCE.loadMasaPajakPeriksaDari(cmbMasaPajakDari, listMasaPajakDari.toArray()); //PajakProvider.INSTANCE.getPajak().toArray());
		}
		
	}
	
	private void loadMasaPajakPeriksaSampai(Date pajakDari)
	{
		listMasaPajakSampai = ControllerFactory.getMainController().getCpSptpdDAOImpl().getMasaPeriksaSampai(pajakDari);
		
		if (listMasaPajakSampai.size() > 0)
		{
			UIController.INSTANCE.loadMasaPajakPeriksaSampai(cmbMasaPajakSampai, listMasaPajakSampai.toArray()); //PajakProvider.INSTANCE.getPajak().toArray());	
		}
	}
	
	private void loadComboPeriksa1()
	{
		listMasaPajak = ControllerFactory.getMainController().getCpPeriksaDAOImpl().GetComboPeriksa1(wp.getNPWP());
		
		if (listMasaPajak.size() > 0)
		{
			UIController.INSTANCE.loadMasaPajakPeriksa(cmbMasaPajak, listMasaPajak.toArray());
			cmbMasaPajak.pack(true);
//			compPemeriksaan.pack(true);
		}
		
	}
	
	private void loadComboPeriksa2()
	{
		listMasaPajak = ControllerFactory.getMainController().getCpPeriksaDAOImpl().GetComboPeriksa2(wp.getNPWP());
		
		if (listMasaPajak.size() > 0)
		{
			UIController.INSTANCE.loadMasaPajakPeriksa(cmbMasaPajak, listMasaPajak.toArray());
			cmbMasaPajak.pack(true);
//			compPemeriksaan.pack(true);
		}
		
	}
	
	private void loadComboPeriksaBPK()
	{
		listMasaPajak = ControllerFactory.getMainController().getCpPeriksaDAOImpl().GetComboPeriksaBPK(wp.getNPWP());
		
		if (listMasaPajak.size() > 0)
		{
			UIController.INSTANCE.loadMasaPajakPeriksa(cmbMasaPajak, listMasaPajak.toArray());
			cmbMasaPajak.pack(true);
//			compPemeriksaan.pack(true);
		}
		
	}
	
	private void removeColumns()
	{
		int count = tbl_Periksa.getColumnCount();
		for (int i=0;i<count;i++)
		{
			tbl_Periksa.getColumn(0).dispose();
		}
	}
	
	private void createTableColumn()
	{
		removeColumns();
//		if(tbl_Periksa.getColumnCount() <= 0){
			String[] listColumn = {"Tanggal Jatuh Tempo", "Tanggal Transaksi", "Bulan", "Masa Pajak Dari", "Id Periksa Detil", 
					"Id Sub Pajak", "Masa Pajak", "Dasar Pengenaan", "Pengenaan Periksa", "Selisih Kurang", "Tarif Pajak", 
					"Pajak Terutang", "Jumlah Bayar", "Pajak Periksa", "Kurang Bayar", "Bunga Persen", "Bunga", "Denda Tambahan",
					"Kenaikan", "Total Periksa"};
			for (int i=listColumn.length - 1;i>=0;i--)
			{
				TableColumn colPajak = new TableColumn(tbl_Periksa, SWT.NONE,0);
				colPajak.setText(listColumn[i]);
				colPajak.setWidth(120);
				if(i==0||i==1||i==4||i==5){
					colPajak.setWidth(0);
				}
				
			}
//		}
		
	}
	
	/*private void createTableColumn2()
	{
		removeColumns();
//		if(tbl_Periksa.getColumnCount() <= 0){
			String[] listColumn = {"Bulan", "Masa Pajak Dari", "Id Periksa Detil", 
					"Id Sub Pajak", "Masa Pajak", "Dasar Pengenaan", "Pengenaan Periksa", "Selisih Kurang", "Tarif Pajak", 
					"Pajak Terutang", "Pajak Periksa", "Kurang Bayar", "Kenaikan", "Total Periksa"};
			for (int i=listColumn.length - 1;i>=0;i--)
			{
				TableColumn colPajak = new TableColumn(tbl_Periksa, SWT.NONE,0);
				colPajak.setText(listColumn[i]);
				colPajak.setWidth(120);
				
			}
//		}
		
	}*/
	
	@SuppressWarnings("deprecation")
	public void periksa1()
	{
		List<HashMap<String, Object>> listSPTPD = null;
		if (txtKodePajak.getText().equalsIgnoreCase("6"))
			listSPTPD = ControllerFactory.getMainController().getCpPeriksaDAOImpl().GetSPTPDPeriksa1TableParkir(wp.getNPWP(), masaPajakDari, masaPajakSampai);
		else
			listSPTPD = ControllerFactory.getMainController().getCpPeriksaDAOImpl().GetSPTPDPeriksa1Table(wp.getNPWP(), masaPajakDari, masaPajakSampai);
		List<HashMap<String, Object>> listDetail = ControllerFactory.getMainController().getCpPeriksaDAOImpl().GetDetailPeriksa1Table(wp.getNPWP(), masaPajakDari, masaPajakSampai);
		int index = 0;
        int indexDtSPTPD = 0;
        int indexDtDetil = 0;
        String[] now = dateNow.toString().split(" ");
        Date blnSPTPD = Date.valueOf(now[0]);
        Date blnDetail = Date.valueOf(now[0]);
        int tarifPajak = wp.getTarif();
        idSubPajak = wp.getIdSubPajak();
        String masaPajak = "";
            			     
        /*Calendar cal = Calendar.getInstance();
        cal.setTime((Date)listSPTPD.get(0).get("MASA_PAJAK_DARI"));
        int yearStart = cal.get(Calendar.YEAR);
        int monthStart = cal.get(Calendar.MONTH);
        cal.setTime((Date)listSPTPD.get(listSPTPD.size() - 1).get("MASA_PAJAK_DARI"));
        int yearEnd = cal.get(Calendar.YEAR);
        int monthEnd = cal.get(Calendar.MONTH);
        int yearDiff = yearEnd - yearStart;
        int monthDiff = monthEnd - monthStart + (yearDiff * 12);*/
//        int year = (Date)listSPTPD.get(indexAwal).get("MASA_PAJAK_DARI");
        indexAwal = cmbMasaPajakDari.getSelectionIndex();
        indexAkhir = cmbMasaPajakSampai.getSelectionIndex();
        while (indexAwal >= indexAkhir)
        {
        	Date bln = listMasaPajakDari.get(indexAwal).getMasaPajakDari(); //cmbMasaPajakDari.getData(Integer.toString(indexAwal)).toString();
        	SimpleDateFormat format = new SimpleDateFormat("MM-yyyy");
//        	String blnView = format.format(strBln);
        	
        	if (cmbMasaPajakDari.getSelectionIndex() == -1)
        		bln = (Date)listSPTPD.get(index).get("MASA_PAJAK_DARI");
        	else
        		bln = listMasaPajakDari.get(indexAwal).getMasaPajakDari();
        	if (listSPTPD.size() > indexDtSPTPD)
        		blnSPTPD = (Date)listSPTPD.get(indexDtSPTPD).get("MASA_PAJAK_DARI");
        	else
        		blnSPTPD = Date.valueOf(now[0]);
        	if (listDetail.size() > indexDtDetil)
        		blnDetail = (Date)listDetail.get(indexDtDetil).get("BULAN");
        	else
        		blnDetail = Date.valueOf(now[0]);
        	
        	TableItem item = new TableItem(tbl_Periksa, SWT.NONE);
        	item.setText(2, format.format(bln)); //(String)listSPTPD.get(index).get("BULAN")
        	item.setText(3, String.valueOf(bln));
    		item.setText(18, String.valueOf(indFormat.format(1)));
        	if (bln.getMonth() == blnSPTPD.getMonth() && bln.getYear() == blnSPTPD.getYear() && listSPTPD.get(indexDtSPTPD) != null)
        	{
        		Date newDate = (Date)listSPTPD.get(indexDtSPTPD).get("MASA_PAJAK_DARI");
        		masaPajak = UIController.INSTANCE.formatMonth(((Date)listSPTPD.get(indexDtSPTPD).get("MASA_PAJAK_DARI")).getMonth() + 1, Locale.getDefault()) + " " + (newDate.getYear() + 1900);
        		item.setText(0, String.valueOf((Date)listSPTPD.get(indexDtSPTPD).get("TANGGALJATUHTEMPO")));
	        	item.setText(1, String.valueOf((Date)listSPTPD.get(indexDtSPTPD).get("TANGGALTRANSAKSI")));
	        	item.setText(5, String.valueOf((Integer)listSPTPD.get(indexDtSPTPD).get("IDSUB_PAJAK")));
	        	item.setText(6, masaPajak);
	        	item.setText(7, String.valueOf(indFormat.format(round((Double)listSPTPD.get(indexDtSPTPD).get("DASARPENGENAAN"), 2))));
	        	item.setText(10, String.valueOf((Integer)listSPTPD.get(indexDtSPTPD).get("TARIF_PAJAK")));
	        	item.setText(11, String.valueOf(indFormat.format(round((Double)listSPTPD.get(indexDtSPTPD).get("PAJAKTERUTANG"), 2))));
	        	item.setText(12, String.valueOf(indFormat.format(round((Double)listSPTPD.get(indexDtSPTPD).get("JUMLAHBAYAR"), 2))));
	        	item.setText(17, String.valueOf(indFormat.format(round((Double)listSPTPD.get(indexDtSPTPD).get("DENDA_TAMBAHAN"), 2))));
	        	indexDtSPTPD += 1;
	        }
        	else
        	{
        		Calendar tempo = Calendar.getInstance();
		        tempo.setTime(bln); //(Date)listSPTPD.get(indexDtSPTPD).get("TANGGALJATUHTEMPO")
		        tempo.add(Calendar.MONTH, 1);
		        Date jatuhTempo = new Date(tempo.get(Calendar.YEAR) - 1900, tempo.get(Calendar.MONTH), tempo.getActualMaximum(Calendar.DAY_OF_MONTH));
        		item.setText(0, String.valueOf(jatuhTempo));
        		item.setText(5, wp.getIdSubPajak().toString());
	        	item.setText(6, UIController.INSTANCE.formatMonth(bln.getMonth() + 1, Locale.getDefault()) + " " + (bln.getYear() + 1900));
	        	item.setText(7, indFormat.format(0));
	        	item.setText(10, String.valueOf(tarifPajak));
	        	item.setText(11, indFormat.format(0));
	        	item.setText(12, indFormat.format(0));
	        	item.setText(17, indFormat.format(0));
	        	item.setText(18, indFormat.format(0));
        	}
        	
        	if (bln.compareTo(blnDetail) == 0)
        	{
        		item.setText(4, String.valueOf((Integer)listDetail.get(indexDtDetil).get("IDPERIKSA_DETAIL")));
	        	item.setText(8, String.valueOf(indFormat.format(round((Double)listDetail.get(indexDtDetil).get("PENGENAAN_PERIKSA"), 2))));
        		item.setText(13, String.valueOf(indFormat.format(round((Double)listDetail.get(indexDtDetil).get("PAJAK_PERIKSA"), 2))));
        		item.setText(15, String.valueOf((Integer)listDetail.get(indexDtDetil).get("BUNGA_PERSEN")));
        		item.setText(16, String.valueOf(indFormat.format(round((Double)listDetail.get(indexDtDetil).get("BUNGA"), 2))));
        		item.setText(18, String.valueOf(indFormat.format(round((Double)listDetail.get(indexDtDetil).get("KENAIKAN_PAJAK"), 2))));
        		indexDtDetil += 1;
        	}
        	else
        	{
        		item.setText(4, String.valueOf(0));
	        	item.setText(8, String.valueOf(indFormat.format(0)));
        		item.setText(13, String.valueOf(indFormat.format(0)));
        		item.setText(15, String.valueOf(0));
        		item.setText(16, String.valueOf(indFormat.format(0)));
        		item.setText(18, String.valueOf(indFormat.format(0)));
        	}
        	item.setText(9, "");   	
        	item.setText(14, "");
        	item.setText(19, "");
        	index += 1;
        	if (indexDtSPTPD < listSPTPD.size()){
	        	if (!(listSPTPD.get(indexDtSPTPD).get("BULAN") == listSPTPD.get(indexDtSPTPD - 1).get("BULAN")))
	        		indexAwal -= 1;
        	}else{
        		indexAwal -= 1;
        	}
        }
	}
	
	/*@SuppressWarnings("deprecation")
	public void periksa1Parkir()
	{
		List<HashMap<String, Object>> listSPTPD = null;
		if (txtKodePajak.getText().equalsIgnoreCase("6"))
			listSPTPD = ControllerFactory.getMainController().getCpPeriksaDAOImpl().GetSPTPDPeriksa1TableParkir(wp.getNPWP(), masaPajakDari, masaPajakSampai);
		else
			listSPTPD = ControllerFactory.getMainController().getCpPeriksaDAOImpl().GetSPTPDPeriksa1Table(wp.getNPWP(), masaPajakDari, masaPajakSampai);
		List<HashMap<String, Object>> listDetail = ControllerFactory.getMainController().getCpPeriksaDAOImpl().GetDetailPeriksa1Table(wp.getNPWP(), masaPajakDari, masaPajakSampai);
		int index = 0;
        int indexDtSPTPD = 0;
        int indexDtDetil = 0;
        String[] now = dateNow.toString().split(" ");
        Date blnSPTPD = Date.valueOf(now[0]);
        Date blnDetail = Date.valueOf(now[0]);
        int tarifPajak = wp.getTarif();
        idSubPajak = wp.getIdSubPajak();
        String masaPajak = "";
            			     
        Calendar cal = Calendar.getInstance();
        cal.setTime((Date)listSPTPD.get(0).get("MASA_PAJAK_DARI"));
        int yearStart = cal.get(Calendar.YEAR);
        int monthStart = cal.get(Calendar.MONTH);
        cal.setTime((Date)listSPTPD.get(listSPTPD.size() - 1).get("MASA_PAJAK_DARI"));
        int yearEnd = cal.get(Calendar.YEAR);
        int monthEnd = cal.get(Calendar.MONTH);
        int yearDiff = yearEnd - yearStart;
        int monthDiff = monthEnd - monthStart + (yearDiff * 12);
//        int year = (Date)listSPTPD.get(indexAwal).get("MASA_PAJAK_DARI");
        indexAwal = cmbMasaPajakDari.getSelectionIndex();
        indexAkhir = cmbMasaPajakSampai.getSelectionIndex();
        while (indexAwal >= indexAkhir)
        {
        	Date bln = listMasaPajakDari.get(indexAwal).getMasaPajakDari(); //cmbMasaPajakDari.getData(Integer.toString(indexAwal)).toString();
        	SimpleDateFormat format = new SimpleDateFormat("MM-yyyy");
//        	String blnView = format.format(strBln);
        	
        	if (cmbMasaPajakDari.getSelectionIndex() == -1)
        		bln = (Date)listSPTPD.get(index).get("MASA_PAJAK_DARI");
        	else
        		bln = listMasaPajakDari.get(indexAwal).getMasaPajakDari();
        	if (listSPTPD.size() > indexDtSPTPD)
        		blnSPTPD = (Date)listSPTPD.get(indexDtSPTPD).get("MASA_PAJAK_DARI");
        	else
        		blnSPTPD = Date.valueOf(now[0]);
        	if (listDetail.size() > indexDtDetil)
        		blnDetail = (Date)listDetail.get(indexDtDetil).get("BULAN");
        	else
        		blnDetail = Date.valueOf(now[0]);
        	
        	TableItem item = new TableItem(tbl_Periksa, SWT.NONE);
        	item.setText(2, format.format(bln)); //(String)listSPTPD.get(index).get("BULAN")
        	item.setText(3, String.valueOf(bln));
    		item.setText(18, String.valueOf(indFormat.format(1)));
        	if (bln.getMonth() == blnSPTPD.getMonth() && bln.getYear() == blnSPTPD.getYear() && listSPTPD.get(indexDtSPTPD) != null)
        	{
        		Date newDate = (Date)listSPTPD.get(indexDtSPTPD).get("MASA_PAJAK_DARI");
        		masaPajak = UIController.INSTANCE.formatMonth(((Date)listSPTPD.get(indexDtSPTPD).get("MASA_PAJAK_DARI")).getMonth() + 1, Locale.getDefault()) + " " + (newDate.getYear() + 1900);
        		item.setText(0, String.valueOf((Date)listSPTPD.get(indexDtSPTPD).get("TANGGALJATUHTEMPO")));
	        	item.setText(1, String.valueOf((Date)listSPTPD.get(indexDtSPTPD).get("TANGGALTRANSAKSI")));
	        	item.setText(5, String.valueOf((Integer)listSPTPD.get(indexDtSPTPD).get("IDSUB_PAJAK")));
	        	item.setText(6, masaPajak);
	        	item.setText(7, String.valueOf(indFormat.format((Double)listSPTPD.get(indexDtSPTPD).get("DASARPENGENAAN"))));
	        	item.setText(10, String.valueOf((Integer)listSPTPD.get(indexDtSPTPD).get("TARIF_PAJAK")));
	        	item.setText(11, String.valueOf(indFormat.format((Double)listSPTPD.get(indexDtSPTPD).get("PAJAKTERUTANG"))));
	        	item.setText(12, String.valueOf(indFormat.format((Double)listSPTPD.get(indexDtSPTPD).get("JUMLAHBAYAR"))));
	        	item.setText(17, String.valueOf(indFormat.format((Double)listSPTPD.get(indexDtSPTPD).get("DENDA_TAMBAHAN"))));
	        	indexDtSPTPD += 1;
	        }
        	else
        	{
        		Calendar tempo = Calendar.getInstance();
		        tempo.setTime(bln); //(Date)listSPTPD.get(indexDtSPTPD).get("TANGGALJATUHTEMPO")
		        tempo.add(Calendar.MONTH, 1);
		        Date jatuhTempo = new Date(tempo.get(Calendar.YEAR) - 1900, tempo.get(Calendar.MONTH), tempo.getActualMaximum(Calendar.DAY_OF_MONTH));
        		item.setText(0, String.valueOf(jatuhTempo));
        		item.setText(5, wp.getIdSubPajak().toString());
	        	item.setText(6, UIController.INSTANCE.formatMonth(bln.getMonth() + 1, Locale.getDefault()) + " " + (bln.getYear() + 1900));
	        	item.setText(7, indFormat.format(0));
	        	item.setText(10, String.valueOf(tarifPajak));
	        	item.setText(11, indFormat.format(0));
	        	item.setText(12, indFormat.format(0));
	        	item.setText(17, indFormat.format(0));
	        	item.setText(18, indFormat.format(0));
        	}
        	
        	if (bln.compareTo(blnDetail) == 0)
        	{
        		item.setText(4, String.valueOf((Integer)listDetail.get(indexDtDetil).get("IDPERIKSA_DETAIL")));
	        	item.setText(8, String.valueOf(indFormat.format((Double)listDetail.get(indexDtDetil).get("PENGENAAN_PERIKSA"))));
        		item.setText(13, String.valueOf(indFormat.format((Double)listDetail.get(indexDtDetil).get("PAJAK_PERIKSA"))));
        		item.setText(15, String.valueOf((Integer)listDetail.get(indexDtDetil).get("BUNGA_PERSEN")));
        		item.setText(16, String.valueOf(indFormat.format((Double)listDetail.get(indexDtDetil).get("BUNGA"))));
        		item.setText(18, String.valueOf(indFormat.format((Double)listDetail.get(indexDtDetil).get("KENAIKAN_PAJAK"))));
        		indexDtDetil += 1;
        	}
        	else
        	{
        		item.setText(4, String.valueOf(0));
	        	item.setText(8, String.valueOf(indFormat.format(0)));
        		item.setText(13, String.valueOf(indFormat.format(0)));
        		item.setText(15, String.valueOf(0));
        		item.setText(16, String.valueOf(indFormat.format(0)));
        		item.setText(18, String.valueOf(indFormat.format(0)));
        	}
        	item.setText(9, "");   	
        	item.setText(14, "");
        	item.setText(19, "");
        	index += 1;
        	if (indexDtSPTPD < listSPTPD.size()){
	        	if (!(listSPTPD.get(indexDtSPTPD).get("BULAN") == listSPTPD.get(indexDtSPTPD - 1).get("BULAN")))
	        		indexAwal -= 1;
        	}else{
        		indexAwal -= 1;
        	}
        }
	}*/
	
	@SuppressWarnings("deprecation")
	public void periksa2()
	{
		List<HashMap<String, Object>> listDetail = ControllerFactory.getMainController().getCpPeriksaDAOImpl().GetDetailPeriksa1Table(wp.getNPWP(), masaPajakDari, masaPajakSampai);
		List<HashMap<String, Object>> listDetail2 = ControllerFactory.getMainController().getCpPeriksaDAOImpl().GetDetailPeriksa2Table(wp.getNPWP(), masaPajakDari, masaPajakSampai);
		int index = 0;
//        int indexDtSPTPD = 0;
        int indexDtDetil = 0;
        String[] now = dateNow.toString().split(" ");
//        Date blnSPTPD = Date.valueOf(now[0]);
        Date blnDetail = Date.valueOf(now[0]);
        int tarifPajak = wp.getTarif();
        idSubPajak = wp.getIdSubPajak();
            			     
        /*Calendar cal = Calendar.getInstance();
        cal.setTime((Date)listDetail2.get(0).get("BULAN"));
        int yearStart = cal.get(Calendar.YEAR);
        int monthStart = cal.get(Calendar.MONTH);
        cal.setTime((Date)listDetail2.get(listDetail2.size() - 1).get("BULAN"));
        int yearEnd = cal.get(Calendar.YEAR);
        int monthEnd = cal.get(Calendar.MONTH);
        int yearDiff = yearEnd - yearStart;
        int monthDiff = monthEnd - monthStart + (yearDiff * 12);*/
//        int year = (Date)listSPTPD.get(indexAwal).get("MASA_PAJAK_DARI");
        indexAwal = cmbMasaPajakDari.getSelectionIndex();
        indexAkhir = cmbMasaPajakSampai.getSelectionIndex();
        
        while (indexAwal >= indexAkhir)
        {
        	Date bln = listMasaPajakDari.get(indexAwal).getMasaPajakDari(); //cmbMasaPajakDari.getData(Integer.toString(indexAwal)).toString();
        	SimpleDateFormat format = new SimpleDateFormat("MM-yyyy");

        	if (cmbMasaPajakDari.getSelectionIndex() == -1)
        		bln = (Date)listDetail2.get(index).get("MASA_PAJAK_DARI");
        	else
        		bln = listMasaPajakDari.get(indexAwal).getMasaPajakDari();
        	
        	if (listDetail2.size() > indexDtDetil)
        		blnDetail = (Date)listDetail2.get(indexDtDetil).get("BULAN");
        	else
        		blnDetail = Date.valueOf(now[0]);
        	

        	TableItem item = new TableItem(tbl_Periksa, SWT.NONE);
        	item.setText(2, format.format(bln));
        	item.setText(3, String.valueOf(bln));
    		item.setText(5, wp.getIdSubPajak().toString());
    		String masaPajak = UIController.INSTANCE.formatMonth(Integer.valueOf(format.format(bln).split("-")[0]), Locale.getDefault()) + " " + (bln.getYear() + 1900);
    		item.setText(6, masaPajak);
    		if (bln.getMonth() == blnDetail.getMonth() && bln.getYear() == blnDetail.getYear() && listDetail2.get(indexDtDetil) != null)
        	{
    			try
    			{
	    			item.setText(4, String.valueOf((Integer)listDetail2.get(indexDtDetil).get("IDPERIKSA_DETAIL")));
		        	item.setText(7, String.valueOf(indFormat.format((Double)listDetail2.get(indexDtDetil).get("DASAR_PENGENAAN"))));
		        	item.setText(8, String.valueOf(indFormat.format((Double)listDetail2.get(indexDtDetil).get("PENGENAAN_PERIKSA"))));
		        	item.setText(9, String.valueOf(indFormat.format((Double)listDetail2.get(indexDtDetil).get("SELISIH_KURANG"))));
		        	item.setText(10, String.valueOf(tarifPajak));
		        	item.setText(11, String.valueOf(indFormat.format((Double)listDetail2.get(indexDtDetil).get("DASAR_PENGENAAN") * tarifPajak / 100)));
	        		item.setText(13, String.valueOf(indFormat.format((Double)listDetail2.get(indexDtDetil).get("PENGENAAN_PERIKSA") * tarifPajak / 100)));
	        		item.setText(14, String.valueOf(indFormat.format(Double.parseDouble(indFormat.parse(item.getText(13)).toString()) - Double.parseDouble(indFormat.parse(item.getText(11)).toString()))));
	        		item.setText(18, String.valueOf(indFormat.format((Double)listDetail2.get(indexDtDetil).get("KENAIKAN"))));
	        		item.setText(19, String.valueOf(indFormat.format((Double)listDetail2.get(indexDtDetil).get("TOTAL_PERIKSA"))));
	        		indexDtDetil++;
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
//        		tbl_Periksa
        	}
    		else
    		{
    			try {
    				item.setText(4, String.valueOf(0));
    	        	item.setText(7, String.valueOf(indFormat.format((Double)listDetail.get(indexDtDetil).get("PAJAK_PERIKSA") / tarifPajak * 100)));
    	        	item.setText(8, String.valueOf(indFormat.format((Double)listDetail.get(indexDtDetil).get("PENGENAAN_PERIKSA"))));
    	        	item.setText(9, String.valueOf(indFormat.format(Double.parseDouble(indFormat.parse(item.getText(7)).toString()) - Double.parseDouble(indFormat.parse(item.getText(8)).toString()))));
    	        	item.setText(10, String.valueOf(tarifPajak));
    	        	item.setText(11, String.valueOf(indFormat.format((Double)listDetail.get(indexDtDetil).get("PAJAK_PERIKSA"))));
            		item.setText(13, String.valueOf(indFormat.format((Double)listDetail.get(indexDtDetil).get("PENGENAAN_PERIKSA") * (tarifPajak / 100))));
            		item.setText(14, String.valueOf(indFormat.format(Double.parseDouble(indFormat.parse(item.getText(9)).toString()) * (tarifPajak / 100))));
            		item.setText(18, String.valueOf(indFormat.format(Double.parseDouble(indFormat.parse(item.getText(14)).toString()))));
            		item.setText(19, String.valueOf(indFormat.format(Double.parseDouble(indFormat.parse(item.getText(14)).toString()) * 2)));
            		indexDtDetil++;
    			} catch (Exception e) {
					// TODO: handle exception
				}
    			
    		}
    		index++;
    		indexAwal--;
        }
	}
	
	@SuppressWarnings("deprecation")
	public void periksaBPK()
	{
		List<HashMap<String, Object>> listSPTPD = null;
		listSPTPD = ControllerFactory.getMainController().getCpPeriksaDAOImpl().GetSPTPDPeriksa1Table(wp.getNPWP(), masaPajakDari, masaPajakSampai);
		List<HashMap<String, Object>> listDetail = ControllerFactory.getMainController().getCpPeriksaDetailBPKDAOImpl().GetDetailPeriksaBPKTable(wp.getNPWP(), masaPajakDari, masaPajakSampai);
		int index = 0;
        int indexDtSPTPD = 0;
        int indexDtDetil = 0;
        String[] now = dateNow.toString().split(" ");
        Date blnSPTPD = Date.valueOf(now[0]);
        Date blnDetail = Date.valueOf(now[0]);
        int tarifPajak = wp.getTarif();
        idSubPajak = wp.getIdSubPajak();
        String masaPajak = "";
        indexAwal = cmbMasaPajakDari.getSelectionIndex();
        indexAkhir = cmbMasaPajakSampai.getSelectionIndex();
        while (indexAwal >= indexAkhir)
        {
        	Date bln = listMasaPajakDari.get(indexAwal).getMasaPajakDari(); //cmbMasaPajakDari.getData(Integer.toString(indexAwal)).toString();
        	SimpleDateFormat format = new SimpleDateFormat("MM-yyyy");
//        	String blnView = format.format(strBln);
        	
        	if (cmbMasaPajakDari.getSelectionIndex() == -1)
        		bln = (Date)listSPTPD.get(index).get("MASA_PAJAK_DARI");
        	else
        		bln = listMasaPajakDari.get(indexAwal).getMasaPajakDari();
        	if (listSPTPD.size() > indexDtSPTPD)
        		blnSPTPD = (Date)listSPTPD.get(indexDtSPTPD).get("MASA_PAJAK_DARI");
        	else
        		blnSPTPD = Date.valueOf(now[0]);
        	String[] bulanDetil = new String[2];
        	if (listDetail.size() > 0)
        		bulanDetil = listDetail.get(indexDtDetil).get("BULAN").toString().split("-");
        	if (listDetail.size() > indexDtDetil)
        		blnDetail = new Date(Integer.valueOf(bulanDetil[1])-1900, Integer.valueOf(bulanDetil[0])-1, 1);
        	else
        		blnDetail = Date.valueOf(now[0]);
        	
        	TableItem item = new TableItem(tbl_Periksa, SWT.NONE);
        	item.setText(2, format.format(bln)); //(String)listSPTPD.get(index).get("BULAN")
        	item.setText(3, String.valueOf(bln));
    		item.setText(18, String.valueOf(indFormat.format(1)));
        	if (bln.getMonth() == blnSPTPD.getMonth() && bln.getYear() == blnSPTPD.getYear() && listSPTPD.get(indexDtSPTPD) != null)
        	{
        		Date newDate = (Date)listSPTPD.get(indexDtSPTPD).get("MASA_PAJAK_DARI");
        		masaPajak = UIController.INSTANCE.formatMonth(((Date)listSPTPD.get(indexDtSPTPD).get("MASA_PAJAK_DARI")).getMonth() + 1, Locale.getDefault()) + " " + (newDate.getYear() + 1900);
        		item.setText(0, String.valueOf((Date)listSPTPD.get(indexDtSPTPD).get("TANGGALJATUHTEMPO")));
	        	item.setText(1, String.valueOf((Date)listSPTPD.get(indexDtSPTPD).get("TANGGALTRANSAKSI")));
	        	item.setText(5, String.valueOf((Integer)listSPTPD.get(indexDtSPTPD).get("IDSUB_PAJAK")));
	        	item.setText(6, masaPajak);
	        	item.setText(10, String.valueOf(tarifPajak));
	        	indexDtSPTPD += 1;
	        }
        	else
        	{
        		Calendar tempo = Calendar.getInstance();
		        tempo.setTime(bln); //(Date)listSPTPD.get(indexDtSPTPD).get("TANGGALJATUHTEMPO")
		        tempo.add(Calendar.MONTH, 1);
		        Date jatuhTempo = new Date(tempo.get(Calendar.YEAR) - 1900, tempo.get(Calendar.MONTH), tempo.getActualMaximum(Calendar.DAY_OF_MONTH));
        		item.setText(0, String.valueOf(jatuhTempo));
        		item.setText(5, wp.getIdSubPajak().toString());
	        	item.setText(6, UIController.INSTANCE.formatMonth(bln.getMonth() + 1, Locale.getDefault()) + " " + (bln.getYear() + 1900));
	        	item.setText(7, indFormat.format(0));
	        	item.setText(10, String.valueOf(tarifPajak));
	        	item.setText(11, indFormat.format(0));
	        	item.setText(12, indFormat.format(0));
	        	item.setText(17, indFormat.format(0));
	        	item.setText(18, indFormat.format(0));
        	}
        	
        	if (bln.compareTo(blnDetail) == 0)
        	{
        		item.setText(4, String.valueOf((Integer)listDetail.get(indexDtDetil).get("IDPERIKSA_DETAIL")));
        		item.setText(7, String.valueOf(indFormat.format(round((Double)listDetail.get(indexDtDetil).get("DASAR_PENGENAAN"), 2))));
        		item.setText(8, String.valueOf(indFormat.format(round((Double)listDetail.get(indexDtDetil).get("PENGENAAN_PERIKSA"), 2))));
        		item.setText(9, String.valueOf(indFormat.format(round((Double)listDetail.get(indexDtDetil).get("SELISIH_KURANG"), 2))));
	        	item.setText(10, String.valueOf((Integer)listDetail.get(indexDtDetil).get("TARIF")));
	        	item.setText(11, String.valueOf(indFormat.format(round((Double)listDetail.get(indexDtDetil).get("PAJAK_TERUTANG"), 2))));
	        	item.setText(12, String.valueOf(indFormat.format(round((Double)listDetail.get(indexDtDetil).get("JUMLAH_BAYAR"), 2))));;
        		item.setText(13, String.valueOf(indFormat.format(round((Double)listDetail.get(indexDtDetil).get("PAJAK_PERIKSA"), 2))));
        		item.setText(14, String.valueOf(indFormat.format(round((Double)listDetail.get(indexDtDetil).get("KURANG_BAYAR"), 2))));
        		item.setText(15, String.valueOf((Integer)listDetail.get(indexDtDetil).get("BUNGA_PERSEN")));
        		item.setText(16, String.valueOf(indFormat.format(round((Double)listDetail.get(indexDtDetil).get("BUNGA"), 2))));
	        	item.setText(17, String.valueOf(indFormat.format(round((Double)listDetail.get(indexDtDetil).get("DENDA_TAMBAHAN"), 2))));
        		item.setText(18, String.valueOf(indFormat.format(round((Double)listDetail.get(indexDtDetil).get("KENAIKAN"), 2))));
        		item.setText(19, String.valueOf(indFormat.format(round((Double)listDetail.get(indexDtDetil).get("TOTAL"), 2))));
        		indexDtDetil += 1;
        	}
        	else
        	{
        		item.setText(4, String.valueOf(0));
        		item.setText(7, String.valueOf(indFormat.format(0)));
	        	item.setText(8, String.valueOf(indFormat.format(0)));
	        	item.setText(9, String.valueOf(indFormat.format(0)));
        		item.setText(13, String.valueOf(indFormat.format(0)));
        		item.setText(15, String.valueOf(0));
        		item.setText(16, String.valueOf(indFormat.format(0)));
        		item.setText(17, String.valueOf(indFormat.format(0)));
        		item.setText(18, String.valueOf(indFormat.format(0)));
        	}
        	index += 1;
        	if (indexDtSPTPD < listSPTPD.size()){
	        	if (!(listSPTPD.get(indexDtSPTPD).get("BULAN") == listSPTPD.get(indexDtSPTPD - 1).get("BULAN")))
	        		indexAwal -= 1;
        	}else{
        		indexAwal -= 1;
        	}
        }
	}
	
	@SuppressWarnings("deprecation")
	private void setBunga()
	{
		if (btnPeriksa1.getSelection())
		{
			int nDenda = 0;
			Date currentMonth = new Date(calPemeriksaan.getYear(), calPemeriksaan.getMonth(), calPemeriksaan.getDay());
			Date nMonth;
			TableItem[] item = tbl_Periksa.getItems();
			
			for (int i=0;i<item.length;i++)
			{
				nMonth = Date.valueOf(item[i].getText(3));
				nDenda = (currentMonth.getYear() - (nMonth.getYear() + 1900)) * 12 + (currentMonth.getMonth() - nMonth.getMonth()) - 1;
				nDenda = nDenda < 0 ? 0 : (nDenda > 24 ? 24 : nDenda);
				TableItem rowItem = tbl_Periksa.getItem(i);
				rowItem.setText(15, String.valueOf(nDenda * 2));
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	private void hitungPajak()
	{
		double totalPajak = 0;
		double selisihPajak = 0;
	    double dasarPengenaan = 0;
	    int tarifPajak = 0;
	    double pajakTerutang = 0;
//	    double ppTerutang = 0;
	    double pengenaanPeriksa = 0;
		double pajakPeriksa = 0;
		int bungaPersen = 0;
		double bunga = 0;
		double dendaTambahan = 0;
		double kenaikan = 0;
		double totalPeriksa = 0;
		
		Date tglJatuhTempo;
		Date tglTransaksi;
		
		totalPajakTerutang = 0;
		totalPajakPeriksa = 0;
		totalBunga = 0;
		totalKenaikan = 0;
		totalDenda = 0;
		
		TableItem[] item = tbl_Periksa.getItems();
		for (int i=0;i<item.length;i++)
		{
			TableItem subItem = tbl_Periksa.getItem(i);
			selisihPajak = 0;
			dendaTambahan = 0;
			if (btnPeriksa1.getSelection())
			{
				try {
					dasarPengenaan = (indFormat.parse(item[i].getText(7))).doubleValue();
					pengenaanPeriksa = (indFormat.parse(item[i].getText(8))).doubleValue();
					tarifPajak = Integer.valueOf(item[i].getText(10)); 
//					ppTerutang = (indFormat.parse(item[i].getText(11))).doubleValue();
					pajakTerutang = (indFormat.parse(item[i].getText(12))).doubleValue();
					totalPajakTerutang += pajakTerutang;
					
					if (pengenaanPeriksa > 0)
					{
						selisihPajak = pengenaanPeriksa - dasarPengenaan;
						pajakPeriksa = pengenaanPeriksa * tarifPajak / 100;
						subItem.setText(9, String.valueOf(indFormat.format(selisihPajak)));
						subItem.setText(13, String.valueOf(indFormat.format(pajakPeriksa)));
					}
					selisihPajak = 0;
					bunga = 0;
					pajakPeriksa = (indFormat.parse(item[i].getText(13))).doubleValue();
					totalPajakPeriksa += pajakPeriksa;
					bungaPersen = Integer.valueOf(item[i].getText(15));
					dendaTambahan = (indFormat.parse(item[i].getText(17))).doubleValue();
					kenaikan = (indFormat.parse(item[i].getText(18))).doubleValue();
					if (pajakPeriksa > 0)
					{
						selisihPajak = round(pajakPeriksa - pajakTerutang, 2);
						subItem.setText(14,  String.valueOf(indFormat.format(selisihPajak)));
						if (selisihPajak > 0)
						{
							bunga = selisihPajak * (bungaPersen / 100.0);
						}
					}
					else
					{
						subItem.setText(14,  String.valueOf(indFormat.format(0)));
						bunga = (indFormat.parse(item[i].getText(16))).doubleValue();
					}
					subItem.setText(16, String.valueOf(indFormat.format(bunga)));
					totalBunga += bunga;
					
					Calendar calTempo = Calendar.getInstance();
					calTempo.setTime(Date.valueOf(item[i].getText(0)));
					calTempo.add(Calendar.YEAR, -1900);
					tglJatuhTempo = new Date(calTempo.get(Calendar.YEAR), calTempo.get(Calendar.MONTH), calTempo.get(Calendar.DATE));
					if (item[i].getText(1) != null)
					{
						if (item[i].getText(1) != "")
						{
							Calendar calTransaksi = Calendar.getInstance();
							calTransaksi.setTime(Date.valueOf(item[i].getText(1)));
							calTransaksi.add(Calendar.YEAR, -1900);
							//tglJatuhTempo = new Date(calTempo.get(Calendar.YEAR), calTempo.get(Calendar.MONTH), calTempo.get(Calendar.DATE));
							tglTransaksi = new Date(calTransaksi.get(Calendar.YEAR), calTransaksi.get(Calendar.MONTH), calTransaksi.get(Calendar.DATE));
						}
						else
						{
							tglTransaksi = new Date(calPemeriksaan.getYear() - 1900, calPemeriksaan.getMonth(), calPemeriksaan.getDay());
						}
					}
					else
					{
						tglTransaksi = new Date(calPemeriksaan.getYear() - 1900, calPemeriksaan.getMonth(), calPemeriksaan.getDay());
					}
					
					Calendar cal = Calendar.getInstance();
					cal.set(Calendar.DAY_OF_MONTH, 1);
					cal.set(Calendar.MONTH, tglJatuhTempo.getMonth());
					cal.set(Calendar.YEAR, tglJatuhTempo.getYear());
					cal.add(Calendar.MONTH, -1);
					Date dateTempo = new Date(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), 1);
					
					if ((tglTransaksi.compareTo(tglJatuhTempo) > 0 && dateTempo.compareTo(Date.valueOf("2012-04-01")) >= 0) && editKenaikan)
					{
						kenaikan = 0.25 * selisihPajak;
//						oldPajakPeriksa = pajakPeriksa;
					}
					else if (tglTransaksi.compareTo(tglJatuhTempo) > 0 && dateTempo.compareTo(Date.valueOf("2012-03-01")) < 0)
					{
						String bulan = tbl_Periksa.getItem(i).getText(2);
						String npwpd = txtKodePajak.getText() + txtNoUrut.getText() + txtKodeKecamatan.getText() + txtKodeKelurahan.getText();
						if (ControllerFactory.getMainController().getCpPeriksaDAOImpl().checkDendaTambahan(npwpd, bulan)){
							dendaTambahan = 120000;
							subItem.setText(17, String.valueOf(indFormat.format(dendaTambahan)));
						}else
							dendaTambahan = 0;
					}
					else if (tglTransaksi.compareTo(tglJatuhTempo) <= 0)
					{
						kenaikan = 0;
					}
					
					totalKenaikan += kenaikan;
					totalDenda += dendaTambahan;
					subItem.setText(18, String.valueOf(indFormat.format(kenaikan)));
					totalPeriksa = round(selisihPajak, 2) + round(bunga, 2) + round(kenaikan, 2) + round(dendaTambahan, 2);
					subItem.setText(19, String.valueOf(indFormat.format(totalPeriksa)));
//					totalPajak += totalPeriksa;
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if (btnPeriksa2.getSelection())
			{
				try {
					pajakPeriksa = (indFormat.parse(item[i].getText(13))).doubleValue();
					dasarPengenaan = (indFormat.parse(item[i].getText(7))).doubleValue();
					tarifPajak = Integer.valueOf(item[i].getText(10));
					pengenaanPeriksa = (indFormat.parse(item[i].getText(13))).doubleValue() / tarifPajak * 100;
					subItem.setText(8, String.valueOf(indFormat.format(pengenaanPeriksa)));
					pajakTerutang = (indFormat.parse(item[i].getText(11))).doubleValue();
					
					if (pengenaanPeriksa > 0)
					{
						selisihPajak = pengenaanPeriksa - dasarPengenaan;
						subItem.setText(9, String.valueOf(indFormat.format(selisihPajak)));
			            pajakPeriksa = pengenaanPeriksa * tarifPajak / 100;
						subItem.setText(13, String.valueOf(indFormat.format(pajakPeriksa)));
					}
					
					if (pajakPeriksa > 0)
					{
						selisihPajak = pajakPeriksa - pajakTerutang;
						if (selisihPajak > 0)
						{
							subItem.setText(14, String.valueOf(indFormat.format(selisihPajak)));
						}
					}
					kenaikan = selisihPajak;
					subItem.setText(18, String.valueOf(indFormat.format(kenaikan)));
					totalPeriksa = selisihPajak;
					chkKenaikan.setSelection(true);
//					totalPajak += totalPeriksa;
					subItem.setText(19, String.valueOf(indFormat.format(totalPeriksa + kenaikan)));
					totalPajakTerutang += pajakTerutang;
			        totalPajakPeriksa += pajakPeriksa;
			        totalBunga = 0;
			        totalKenaikan += kenaikan;
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		Date periksa = new Date(calPemeriksaan.getYear() - 1900, calPemeriksaan.getMonth(), calPemeriksaan.getDay());
		Date batas = new Date(117, 9, 21);
		if (periksa.after(batas)){
//			totalPajakTerutang = roundUP(totalPajakTerutang);
//			totalPajakPeriksa = roundUP(totalPajakPeriksa);
			totalKenaikan = roundUP(totalKenaikan);
			totalBunga = roundUP(round(totalBunga, 2));
			totalDenda = roundUP(round(totalDenda, 2));
		}
		if (btnPeriksa1.getSelection())
			totalPajak = roundUP(totalPajakPeriksa - totalPajakTerutang) + totalKenaikan + totalBunga + totalDenda;
		else
			totalPajak = totalPajakPeriksa - totalPajakTerutang;
		/*System.out.println("Total Pajak Terutang : " + totalPajakTerutang);
		System.out.println("Total Pajak Periksa : " + totalPajakPeriksa);
		System.out.println("Total Bunga : " + totalBunga);
		System.out.println("Total kenaikan : " + totalKenaikan);*/

		if (totalPajak > 0)
		{
			txtTotalPajakTerhutang.setMoney(new CurrencyAmount(totalPajak, Currency.getInstance(ind)));
//			totalPajakTerutang = totalPajak;
		}
		else{
			txtTotalPajakTerhutang.setMoney(new CurrencyAmount(0, Currency.getInstance(ind)));
		}
		hitungTotalPajak();
		//HitungTotalBPK();
	}
	
	private void refreshTable(){
		tbl_Periksa.removeAll();
		if (btnPeriksa1.getSelection())
		{
			createTableColumn();
			periksa1();
			setBunga();
			hitungPajak();
		}
		else if (btnPeriksa2.getSelection())
		{
			createTableColumn();
			periksa2();
			hitungPajak();
		}
	}
	
	private void hitungTotalPajak()
	{
        double totalPajak = 0;
        double besarKenaikan = 0;
        double total = 0;
        totalPajak = txtTotalPajakTerhutang.getMoney().getNumber().doubleValue();
        if (chkKenaikan.getSelection())
        {
        	besarKenaikan = totalPajak * (kenaikanPersen / 100);
        	txtKenaikan.setMoney(new CurrencyAmount(besarKenaikan, Currency.getInstance(ind)));
        }
        total = totalPajak + besarKenaikan;
        if (total > 0)
        	txtTotal.setMoney(new CurrencyAmount(total, Currency.getInstance(ind)));
        else
        	txtTotal.setMoney(new CurrencyAmount(0, Currency.getInstance(ind)));
	}
	
	@Override
	public void setFocus() {
		txtNoUrut.setFocus();
	}
	
	private UserModul userModul = ControllerFactory.getMainController().getCpUserDAOImpl()
			.getHMUserModul(GlobalVariable.userModul.getIdUser()).get(8); // 2 merupakan id sub modul.
	private Button btnMagic;
	private Button btnPeriksaBpk;
	private Spinner spinner;
	
	private boolean isSave(){		
		return userModul.getSimpan();
	}
	
	private boolean isDelete(){		
		return userModul.getHapus();
	}
	
	private boolean isPrint(){		
		return userModul.getCetak();
	}
	
	private static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

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
	
	private static double roundUP(double value) {
//	    if (places < 0) throw new IllegalArgumentException();

	    DecimalFormat df = new DecimalFormat("#.##");
		df.setMaximumFractionDigits(0);
		df.setRoundingMode(RoundingMode.UP);
	    try {
			return df.parse(df.format(value)).doubleValue();
		} catch (ParseException e) {
			e.printStackTrace();
			return 0.0;
		}
	}
	
	private static int columnAtPoint(Table table, Point pt) {
        int colIndex = -1;
        // The only way how to get column bounds is to get TableItem
        // But empty table hasn't got any and headers does not count.
        // The only way is to temporarily create one and then immediately dispose.
        TableItem fakeRow = new TableItem(table, 0);
        for (int i = 0; i < table.getColumnCount(); i++) {
            Rectangle rec = fakeRow.getBounds(i);
            // It is safer to use X coordinate comparison directly rather then 
            // rec.contains(pt)
            // This way also works for Tree/TreeViews.
            if ((pt.x > rec.x)  && (pt.x < (rec.x + rec.width))) {
                colIndex = i;
            }
        }
        fakeRow.dispose();
        // Not the most efficient way. Rectangles obtained from "fake row" can be cached 
        // and recomputed on column resizes, moves and swaps.
        return colIndex;	
    }

	private void HitungTotalBPK(){
		Double total = 0.0;
		for (TableItem tbl : tbl_Periksa.getItems()){
			try {
				total += indFormat.parse(tbl.getText(19)).doubleValue();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		txtTotalPajakTerhutang.setMoney(new CurrencyAmount(total, Currency.getInstance(ind)));
		txtTotal.setMoney(new CurrencyAmount(total, Currency.getInstance(ind)));
	}
	
	private void hitungPajakBPK(){
		totalPajakTerutang = 0;
		totalPajakPeriksa = 0;
		totalBunga = 0;
		totalKenaikan = 0;
		totalDenda = 0;
		totalDibayar = 0;
		for (TableItem tbl : tbl_Periksa.getItems()){
			try {
				totalPajakPeriksa += indFormat.parse(tbl.getText(13)).doubleValue();
				totalPajakTerutang += indFormat.parse(tbl.getText(11)).doubleValue();
				totalBunga += indFormat.parse(tbl.getText(16)).doubleValue();
				totalKenaikan += indFormat.parse(tbl.getText(18)).doubleValue();
				totalDenda += indFormat.parse(tbl.getText(17)).doubleValue();
				totalDibayar += indFormat.parse(tbl.getText(12)).doubleValue();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		/*System.out.println(totalPajakPeriksa);
		System.out.println(totalPajakTerutang);
		System.out.println(totalBunga);
		System.out.println(totalKenaikan);
		System.out.println(totalDenda);*/
		Double totalPajak = (roundUP(totalPajakPeriksa) - roundUP(totalDibayar)) + roundUP(totalKenaikan) + roundUP(totalBunga);
		if (totalPajak > 0)
		{
			txtTotalPajakTerhutang.setMoney(new CurrencyAmount(totalPajak, Currency.getInstance(ind)));
//			totalPajakTerutang = totalPajak;
		}
		else{
			txtTotalPajakTerhutang.setMoney(new CurrencyAmount(0, Currency.getInstance(ind)));
		}
		hitungTotalPajak();
	}
}