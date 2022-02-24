package com.dispenda.sspd.views;

import java.lang.reflect.InvocationTargetException;
import java.math.RoundingMode;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.annotation.processing.Messager;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.fieldassist.AutoCompleteField;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.window.Window;
import org.eclipse.nebula.widgets.tablecombo.TableCombo;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
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
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.SWTResourceManager;

import com.dispenda.controller.ControllerFactory;
import com.dispenda.controller.UIController;
import com.dispenda.dao.LogImp;
import com.dispenda.model.Mohon;
import com.dispenda.model.MohonDetail;
import com.dispenda.model.Pajak;
import com.dispenda.model.Pejabat;
import com.dispenda.model.PendaftaranWajibPajak;
import com.dispenda.model.Periksa;
import com.dispenda.model.PeriksaDetail;
import com.dispenda.model.Sptpd;
import com.dispenda.model.Sspd;
import com.dispenda.model.UserModul;
import com.dispenda.model.WpTutup;
import com.dispenda.object.GlobalVariable;
import com.dispenda.object.Preferences;
import com.dispenda.report.ReportAction;
import com.dispenda.report.ReportAppContext;
import com.dispenda.report.ReportModule;
import com.dispenda.sspd.bridge.ICommunicationView;
import com.dispenda.sspd.dialog.CariLokasiDialog;
import com.dispenda.web.bkad.ApiPost;
import com.dispenda.widget.IMoneyChangeListener;
import com.dispenda.widget.MoneyChangeEvent;
import com.dispenda.widget.MoneyField;
import com.ibm.icu.util.Currency;
import com.ibm.icu.util.CurrencyAmount;

public class SSPDView extends ViewPart implements ICommunicationView {
	public static final String ID = SSPDView.class.getName();
	private boolean resultApi = false;
	private Table table;
	private Text txtKodePajak;
	private Text txtNamaBadan;
	private Text txtAlamat;
	private Text txtNoSPTPD;
	private MoneyField txtTotalPajakTerhutang;
	private MoneyField txtDibayar;
	private Text txtNamaPenyetor;
	private DateTime dateJatuhTempo;
	private Text txtNoUrut;
	private Text txtKodeKecamatan;
	private Text txtKodeKelurahan;
	private Text txtNoSSPD;
	private Color fontColor = new Color(Display.getCurrent(),Preferences.FONT_COLOR);
	PendaftaranWajibPajak wp;
	Sptpd spt;
	Sspd objSspd;
	private Integer selectedID = null;
	private Integer idSubPajak;
	private Integer idSPTPD;
	private Integer idPeriksa;
	private Integer tarif;
//	private List<Sptpd> listSptpd = new ArrayList<Sptpd>();
	private List<Sptpd> listMasaPajak = new ArrayList<Sptpd>();
	private List<Periksa> listPeriksa = new ArrayList<Periksa>();
//	private List<BidangUsaha> listBU = new ArrayList<BidangUsaha>();
	private List<PeriksaDetail> listPD = new ArrayList<PeriksaDetail>();
	private List<Pejabat> listPejabat;
	private Date pajakDari[];
	private Date pajakSampai[];
	private String jenisSKP[];
	private TableCombo cmbMasaPajak;
	private Composite comp;
	private java.sql.Timestamp dateNow;
	private Label lblKeterangan;
	private Label lblNpwpd;
	private int IDSSPDAngsur[];
	
	private String bulanSKP;
	private double pokokPajak;
	private double denda;
	private double dendaTambahan;
	private double dendaTerlambatSKPDKB;
	private double kenaikan	;
	private double dendaSSPD;
	private double totalPajakTerutang;
	private double totalDendaAngsuran;
	private Integer cicilanKe;
	private Integer jenisBayar;
	private String caraBayar;
	private String updNoSSPD[];
//	private int updIdSSPD[];
	private double updDenda[];
	private int angsuranTerakhir;
	private boolean angsuranLunas;
	private double pajakAngsuranPokok;
	private double pajakAngsuran;
	private boolean checkSKPDKB = true;
	private boolean enable;
	private boolean openViewAntri = true;
	
	private String keteranganSKPDKB;
	private Button btnBayar;
	private Label lblLunas;
	private Locale ind = new Locale("id", "ID");
    private NumberFormat indFormat = NumberFormat.getCurrencyInstance(ind);
    private Table tableAngsuran;
    private Text txtLokasi;
	private Button btnCetak;
	private Composite compPejabat;
	private Label lblPejabat;
	private Combo cmbPejabat;
	private Label label_4;
	private Label label_5;
	private Composite composite;
	private Button btnTepatWaktu;
	private Button btnAngsuran;
	private Label label;
	private Composite composite_1;
	private Button btnTunai;
	private Button btnBank;
	private Button btnGiro;
	private Label label_1;
	private DateTime datePembayaran;
	private DateTime dateCetak;
	private Label label_2;
	private Label label_3;
	private Label lblNoSspd;
	private Sspd sspd;
	private Composite compButton;
	private Label lblSudahDiangsur;
	private MoneyField txtTotalAngsur;
	private Label lblSisaPajak;
	private MoneyField txtSisa;
	private Table tblCicilan;
	private TableColumn tableColumn;
	private TableColumn tableColumn_1;
	private Button btnHapus;
	private Clipboard clipboard = new Clipboard(Display.getCurrent());
	private String tempNota;
	private String pesanValidate;
	private AutoCompleteField autoCompleteField;
	private final ArrayList<Integer> pressed = new ArrayList();
	private int cicil = 0;

	/**
	 * The constructor.
	 */
	public SSPDView() {
		
	}

	/**
	 * This is a callback that will allow us
	 * to create the viewer and initialize it.
	 */
	public void createPartControl(Composite parent) {
		Locale.setDefault(new Locale("id","ID"));
		dateNow = (java.sql.Timestamp)ControllerFactory.getMainController().getCpWajibPajakDAOImpl().getDateNow();
		
		ScrolledComposite scrolledComposite = new ScrolledComposite(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		
		comp = new Composite(scrolledComposite, SWT.NONE);
		comp.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BORDER));
		comp.setLayout(new GridLayout(14, false));
		
		lblNpwpd = new Label(comp, SWT.NONE);
		lblNpwpd.setForeground(fontColor);
		lblNpwpd.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblNpwpd.setText("NPWPD");
		
		txtKodePajak = new Text(comp, SWT.BORDER);
		txtKodePajak.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtKodePajak.setEditable(false);
		txtKodePajak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		GridData gd_txtKodePajak = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_txtKodePajak.widthHint = 14;
		txtKodePajak.setLayoutData(gd_txtKodePajak);
		
		txtNoUrut = new Text(comp, SWT.BORDER);
		txtNoUrut.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtNoUrut.addKeyListener(new KeyAdapter() {
			@SuppressWarnings("deprecation")
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.keyCode == SWT.CR || e.keyCode == SWT.KEYPAD_CR)
				{
					//resetForm();
					cmbMasaPajak.select(-1);
					cmbMasaPajak.setText("");
					lblLunas.setVisible(false);
					getDataWP();
					listMasaPajak = ControllerFactory.getMainController().getCpSptpdDAOImpl().getMasaPajakSPTPD(wp.getNPWP());
					listMasaPajak.addAll(ControllerFactory.getMainController().getCpSptpdDAOImpl().getMasaPajakSKPDKB(wp.getNPWP()));
					
					Collections.sort(listMasaPajak);
					
					if (listMasaPajak.size() > 0)
					{
						UIController.INSTANCE.loadTMasaPajak(cmbMasaPajak, listMasaPajak.toArray()); //PajakProvider.INSTANCE.getPajak().toArray());
						
					}

						if (wp.getStatus() != null && !wp.getStatus().equalsIgnoreCase("Aktif")){
							// TODO Set warna background dan getNoSuratKeteranganTutup dari table tutup
							lblKeterangan.setVisible(true);
							lblKeterangan.setText(wp.getKeteranganTutup());
							if(wp.getStatus().equalsIgnoreCase("Tutup")){
								WpTutup wptutup = ControllerFactory.getMainController().getCpWajibPajakDAOImpl().getWajibPajakTutup(wp.getNPWP());
								SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
								try{
									if(wptutup.getTglSampaiTutup().getYear()+1900 >= 9999){
										lblKeterangan.setBackground(SWTResourceManager.getColor(SWT.COLOR_DARK_RED));
										lblKeterangan.setText(wptutup.getNoSuratTutup()+"\n"+
												"Tanggal TUTUP dari :"+sdf.format(wptutup.getTglMulaiTutup())+" Permanen");
									}else{
										lblKeterangan.setBackground(SWTResourceManager.getColor(SWT.COLOR_CYAN));
										lblKeterangan.setText(wptutup.getNoSuratTutup()+"\n"+
										"Tanggal TUTUP dari :"+sdf.format(wptutup.getTglMulaiTutup())+" - "+sdf.format(wptutup.getTglSampaiTutup()));
									}
								}catch(Exception exc){
									lblKeterangan.setBackground(SWTResourceManager.getColor(SWT.COLOR_CYAN));
								}
							}else
								lblKeterangan.setBackground(SWTResourceManager.getColor(SWT.COLOR_CYAN));
						}
						else{
							lblKeterangan.setText("");
							lblKeterangan.setVisible(false);
						}
				}
				
				if(((e.stateMask & SWT.CTRL) == SWT.CTRL) && (e.keyCode == 'f')){
					if (txtKodePajak.getText().equalsIgnoreCase("7")){
						CariLokasiDialog dialog = new CariLokasiDialog(Display.getCurrent().getActiveShell());
						if (dialog.open() == Window.OK){
							String val = dialog.getEnteredText();
							List<Sptpd> listMasaPajakSearch = new ArrayList<Sptpd>();
							for (int i=0;i<listMasaPajak.size();i++){
								if (listMasaPajak.get(i).getLokasi().toUpperCase().contains(val.toUpperCase()))
									listMasaPajakSearch.add(listMasaPajak.get(i));
							}
							 
							if (listMasaPajakSearch.size() > 0)
							{
								UIController.INSTANCE.loadTMasaPajak(cmbMasaPajak, listMasaPajakSearch.toArray()); //PajakProvider.INSTANCE.getPajak().toArray());
								cmbMasaPajak.select(0);	
								cmbMasaPajakSelected();
							}else{
								MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Data Lokasi tidak ditemukan");
							}
						}
					}
                }
			}
		});
		txtNoUrut.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		GridData gd_txtNoUrut = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_txtNoUrut.widthHint = 83;
		txtNoUrut.setLayoutData(gd_txtNoUrut);
		
		txtKodeKecamatan = new Text(comp, SWT.BORDER);
		txtKodeKecamatan.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtKodeKecamatan.setEditable(false);
		txtKodeKecamatan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		GridData gd_txtKodeKecamatan = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_txtKodeKecamatan.widthHint = 20;
		txtKodeKecamatan.setLayoutData(gd_txtKodeKecamatan);
		
		txtKodeKelurahan = new Text(comp, SWT.BORDER);
		txtKodeKelurahan.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtKodeKelurahan.setEditable(false);
		txtKodeKelurahan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		GridData gd_txtKodeKelurahan = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
		gd_txtKodeKelurahan.widthHint = 19;
		txtKodeKelurahan.setLayoutData(gd_txtKodeKelurahan);
		new Label(comp, SWT.NONE);
		
		txtLokasi = new Text(comp, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
		txtLokasi.setEditable(false);
		txtLokasi.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtLokasi.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		GridData gd_txtLokasi = new GridData(SWT.FILL, SWT.FILL, false, false, 3, 3);
		gd_txtLokasi.heightHint = 56;
		txtLokasi.setLayoutData(gd_txtLokasi);
		txtLokasi.setVisible(false);
		
		lblKeterangan = new Label(comp, SWT.WRAP);
		GridData gd_lblKeterangan = new GridData(SWT.FILL, SWT.FILL, false, false, 4, 4);
		gd_lblKeterangan.heightHint = 70;
		gd_lblKeterangan.widthHint = 398;
		lblKeterangan.setLayoutData(gd_lblKeterangan);
		lblKeterangan.setForeground(fontColor);
		lblKeterangan.setBackground(SWTResourceManager.getColor(SWT.COLOR_CYAN));
		lblKeterangan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, 15, SWT.BOLD));
		lblKeterangan.setVisible(false);
		
		Label lblNamaBadan = new Label(comp, SWT.NONE);
		lblNamaBadan.setForeground(fontColor);
		lblNamaBadan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblNamaBadan.setText("Nama Badan");
		
		txtNamaBadan = new Text(comp, SWT.BORDER);
		txtNamaBadan.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtNamaBadan.setEditable(false);
		txtNamaBadan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		GridData gd_txtNamaBadan = new GridData(SWT.LEFT, SWT.CENTER, false, false, 6, 1);
		gd_txtNamaBadan.widthHint = 360;
		txtNamaBadan.setLayoutData(gd_txtNamaBadan);
		
		Label lblAlamat = new Label(comp, SWT.NONE);
		lblAlamat.setForeground(fontColor);
		lblAlamat.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblAlamat.setText("Alamat Badan");
		
		txtAlamat = new Text(comp, SWT.BORDER);
		txtAlamat.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtAlamat.setEditable(false);
		txtAlamat.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		GridData gd_txtAlamat = new GridData(SWT.LEFT, SWT.CENTER, false, false, 6, 1);
		gd_txtAlamat.widthHint = 360;
		txtAlamat.setLayoutData(gd_txtAlamat);
		
		Label lblMasaPajak = new Label(comp, SWT.NONE);
		lblMasaPajak.setForeground(fontColor);
		lblMasaPajak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblMasaPajak.setText("Masa Pajak");
		
		cmbMasaPajak = new TableCombo(comp, SWT.READ_ONLY);
		cmbMasaPajak.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(((e.stateMask & SWT.CTRL) == SWT.CTRL) && (e.keyCode == 'f'))
                {
					if (txtKodePajak.getText().equalsIgnoreCase("7")){
						CariLokasiDialog dialog = new CariLokasiDialog(Display.getCurrent().getActiveShell());
						if (dialog.open() == Window.OK){
							String val = dialog.getEnteredText();
							List<Sptpd> listMasaPajakSearch = new ArrayList<Sptpd>();
							for (int i=0;i<listMasaPajak.size();i++){
								if (listMasaPajak.get(i).getLokasi().toUpperCase().contains(val.toUpperCase()))
									listMasaPajakSearch.add(listMasaPajak.get(i));
							}
							 
							if (listMasaPajakSearch.size() > 0)
							{
								UIController.INSTANCE.loadTMasaPajak(cmbMasaPajak, listMasaPajakSearch.toArray()); //PajakProvider.INSTANCE.getPajak().toArray());
								cmbMasaPajak.select(0);	
								cmbMasaPajakSelected();
							}else{
								MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Data Lokasi tidak ditemukan");
							}
						}
					}
                }
			}
		});
		cmbMasaPajak.setEditable(true);
		cmbMasaPajak.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
//		cmbMasaPajak.sel
		cmbMasaPajak.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				cmbMasaPajakSelected();
//				cmbMasaPajak.pack(true);
			}
		});
		cmbMasaPajak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		GridData gd_cmbMasaPajak = new GridData(SWT.LEFT, SWT.CENTER, false, false, 6, 1);
		gd_cmbMasaPajak.widthHint = 260;
		cmbMasaPajak.setLayoutData(gd_cmbMasaPajak);
		cmbMasaPajak.defineColumns(2);
		cmbMasaPajak.setDisplayColumnIndex(1);
//		cmbMasaPajak.add
		
		
		lblNoReg = new Label(comp, SWT.NONE);
		lblNoReg.setText("No Reg");
		lblNoReg.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblNoReg.setForeground(fontColor);
		lblNoReg.setVisible(false);
		
		txtNoReg = new Text(comp, SWT.BORDER);
		txtNoReg.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtNoReg.setEditable(false);
		txtNoReg.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		GridData gd_txtNoReg = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_txtNoReg.widthHint = 62;
		txtNoReg.setLayoutData(gd_txtNoReg);
		txtNoReg.setVisible(false);
		new Label(comp, SWT.NONE);
		
		label_4 = new Label(comp, SWT.NONE);
		label_4.setText("No SPTPD/SKPD");
		label_4.setForeground(fontColor);
		label_4.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		txtNoSPTPD = new Text(comp, SWT.BORDER);
		txtNoSPTPD.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtNoSPTPD.setEditable(false);
		txtNoSPTPD.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		GridData gd_txtNoSPTPD = new GridData(SWT.LEFT, SWT.CENTER, false, false, 4, 1);
		gd_txtNoSPTPD.widthHint = 166;
		txtNoSPTPD.setLayoutData(gd_txtNoSPTPD);
		
		lblSlash = new Label(comp, SWT.NONE);
		GridData gd_lblSlash = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblSlash.horizontalIndent = 13;
		lblSlash.setLayoutData(gd_lblSlash);
		lblSlash.setText("/");
		lblSlash.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblSlash.setVisible(false);
		
		txtNoSKPLama = new Text(comp, SWT.BORDER);
		txtNoSKPLama.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtNoSKPLama.setEditable(false);
		txtNoSKPLama.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		GridData gd_txtNoSKPLama = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_txtNoSKPLama.widthHint = 114;
		txtNoSKPLama.setLayoutData(gd_txtNoSKPLama);
		txtNoSKPLama.setVisible(false);
		
		lblNoSspd = new Label(comp, SWT.NONE);
		lblNoSspd.setForeground(fontColor);
		GridData gd_lblNoSspd = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblNoSspd.widthHint = 76;
		lblNoSspd.setLayoutData(gd_lblNoSspd);
		lblNoSspd.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblNoSspd.setText("No SSPD");
		
		txtNoSSPD = new Text(comp, SWT.BORDER);
		txtNoSSPD.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				if (txtNoSSPD.getText().contains("-")){
					String noReg = ControllerFactory.getMainController().getCpSspdDAOImpl().getNoReg(txtNoSSPD.getText().split("-")[0]);
					noReg = noReg.equals("") ? "0" : noReg;
					noReg += "-" + ControllerFactory.getMainController().getCpSspdDAOImpl().getNoReg(txtNoSSPD.getText().split("-")[1]);
					if (noReg.length() > 0){
						txtNoReg.setText(noReg);
						txtNoReg.setVisible(true);
						lblNoReg.setVisible(true);
					}
					else{
						txtNoReg.setText("");
						txtNoReg.setVisible(false);
						lblNoReg.setVisible(false);
					}
//					btnCetakDenda.setVisible(true);
				}else{
					if (txtNoSSPD.getText().contains("/SSPD/")){
						String noReg = ControllerFactory.getMainController().getCpSspdDAOImpl().getNoReg(txtNoSSPD.getText());
						if (noReg.length() > 0){
							txtNoReg.setText(noReg);
							txtNoReg.setVisible(true);
							lblNoReg.setVisible(true);
						}
						else{
							txtNoReg.setText("");
							txtNoReg.setVisible(false);
							lblNoReg.setVisible(false);
						}
					}else{
						txtNoReg.setText("");
						txtNoReg.setVisible(false);
						lblNoReg.setVisible(false);
					}
					btnCetakDenda.setVisible(false);
				}
				
//				if (txtNoSSPD.getText().contains("-"))
//					btnCetakDenda.setVisible(true);
//				else
//					btnCetakDenda.setVisible(false);
				
				txtNoSSPD.pack();
				txtNoReg.pack();
			}
		});
		txtNoSSPD.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtNoSSPD.setEditable(false);
		txtNoSSPD.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		GridData gd_txtNoSSPD = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
		gd_txtNoSSPD.widthHint = 149;
		txtNoSSPD.setLayoutData(gd_txtNoSSPD);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		
		lblTanggalTerbit = new Label(comp, SWT.NONE);
		lblTanggalTerbit.setText("Tanggal Terbit");
		lblTanggalTerbit.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblTanggalTerbit.setForeground(fontColor);
		
		dateTerbit = new DateTime(comp, SWT.BORDER | SWT.DROP_DOWN);
		dateTerbit.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		dateTerbit.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		dateTerbit.setEnabled(false);
		dateTerbit.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		
		label_5 = new Label(comp, SWT.NONE);
		label_5.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1));
		label_5.setText("Tanggal Jatuh Tempo");
		label_5.setForeground(fontColor);
		label_5.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		dateJatuhTempo = new DateTime(comp, SWT.BORDER | SWT.DROP_DOWN);
		dateJatuhTempo.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		dateJatuhTempo.setEnabled(false);
		dateJatuhTempo.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		lblNoSts = new Label(comp, SWT.NONE);
		lblNoSts.setText("No STS");
		lblNoSts.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblNoSts.setForeground(fontColor);
		
		txtNoSTS = new Text(comp, SWT.BORDER);
		txtNoSTS.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.keyCode == SWT.CR || e.keyCode == SWT.KEYPAD_CR)
				{
					if (txtNoSTS.getEditable()){
						openViewAntri = false;
						List<Sspd> listSts = ControllerFactory.getMainController().getCpSspdDAOImpl().getDatabySTS(txtNoSTS.getText());
						accept(listSts.get(0));
						txtNoSTS.setEditable(false);
					}
				}
			}
		});
		txtNoSTS.addModifyListener(new ModifyListener() {
			@SuppressWarnings("deprecation")
			public void modifyText(ModifyEvent arg0) {
				Timestamp valid = new Timestamp(dtValid.getYear()-1900, dtValid.getMonth(), dtValid.getDay(), 23, 59, 59, 99);
				if (txtNoSTS.getText()!=""){
					dtValid.setVisible(true);
					lblValid.setVisible(true);
					if (dateNow.before(valid)){
//						btnGenerateSts.setEnabled(false);
						btnCetakSts.setVisible(true);
					}
					else{
						btnGenerateSts.setEnabled(true);
						btnCetakSts.setVisible(false);
					}
				}else{
					btnGenerateSts.setEnabled(true);
					btnCetakSts.setVisible(false);
					dtValid.setVisible(false);
					lblValid.setVisible(false);
				}
				
				if (txtNoSTS.getText().contains("-")){
					btnCetakStsDenda.setVisible(true);
				}else{
					btnCetakStsDenda.setVisible(false);
				}
			}
		});
		txtNoSTS.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtNoSTS.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		GridData gd_txtNoSTS = new GridData(SWT.LEFT, SWT.CENTER, true, false, 2, 1);
		gd_txtNoSTS.widthHint = 324;
		txtNoSTS.setLayoutData(gd_txtNoSTS);
		
		lblValid = new Label(comp, SWT.NONE);
		lblValid.setText("Valid");
		lblValid.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblValid.setForeground(fontColor);
		lblValid.setVisible(false);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		
		dtValid = new DateTime(comp, SWT.BORDER | SWT.DROP_DOWN);
		dtValid.setEnabled(false);
		dtValid.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		dtValid.setVisible(false);
		
		label = new Label(comp, SWT.NONE);
		label.setText("Jenis Pembayaran");
		label.setForeground(fontColor);
		label.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		composite = new Composite(comp, SWT.BORDER);
		composite.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 5, 1));
		composite.setEnabled(false);
		composite.setLayout(new GridLayout(2, false));
		
		btnTepatWaktu = new Button(composite, SWT.RADIO);
		btnTepatWaktu.setText("Tepat Waktu");
		btnTepatWaktu.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnTepatWaktu.setEnabled(true);
		
		
		btnAngsuran = new Button(composite, SWT.RADIO);
		btnAngsuran.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		btnAngsuran.setText("Angsuran");
		btnAngsuran.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnAngsuran.setEnabled(true);
		new Label(comp, SWT.NONE);
		
		tableAngsuran = new Table(comp, SWT.BORDER | SWT.FULL_SELECTION | SWT.VIRTUAL);
		tableAngsuran.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				loadPerhitunganAngsuran();
			}
		});
		tableAngsuran.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		tableAngsuran.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE-1, SWT.NORMAL));
		GridData gd_tableAngsuran = new GridData(SWT.FILL, SWT.FILL, true, false, 7, 3);
		gd_tableAngsuran.heightHint = 81;
		gd_tableAngsuran.widthHint = 803;
		tableAngsuran.setLayoutData(gd_tableAngsuran);
		tableAngsuran.setHeaderVisible(true);
		tableAngsuran.setLinesVisible(true);
		tableAngsuran.setVisible(false);
		tableAngsuran.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.keyCode == 99){
					clipboard.setContents(new Object[] { tableAngsuran.getItem(tableAngsuran.getSelectionIndex()).getText(1) },
				              new Transfer[] { TextTransfer.getInstance() });
				}
			}
		});
		Menu menuAngsuran = new Menu(tableAngsuran);
		MenuItem menuCopyAngsuran = new MenuItem(menuAngsuran, SWT.PUSH);
		menuCopyAngsuran.setText("Copy..");
		menuCopyAngsuran.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				clipboard.setContents(new Object[] { tableAngsuran.getItem(tableAngsuran.getSelectionIndex()).getText(1) },
			              new Transfer[] { TextTransfer.getInstance() });
			}
		});
		tableAngsuran.setMenu(menuAngsuran);
		
		label_1 = new Label(comp, SWT.NONE);
		label_1.setText("Cara Bayar");
		label_1.setForeground(fontColor);
		label_1.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		composite_1 = new Composite(comp, SWT.BORDER);
		composite_1.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 4, 1));
		composite_1.setLayout(new GridLayout(3, false));
		
		btnTunai = new Button(composite_1, SWT.RADIO);
		btnTunai.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				dateCetak.setEnabled(false);
				dateCetak.setDate(datePembayaran.getYear(), datePembayaran.getMonth(), datePembayaran.getDay());
				
				hitungPajak();
				dendaSSPD = hitungDendaSSPD(pokokPajak);
				TableItem itemSSPD;
				if (btnTepatWaktu.getSelection())
					itemSSPD =  table.getItem(table.getItemCount()-1);
				else
					itemSSPD =  tblCicilan.getItem(tblCicilan.getItemCount()-1);
				//TableItem item = table.getItem(table.getItemCount()-1);
				itemSSPD.setText(new String[]{"Denda SSPD", indFormat.format(dendaSSPD)});
			}
		});
		btnTunai.setText("Tunai");
		btnTunai.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		btnBank = new Button(composite_1, SWT.RADIO);
		btnBank.setLayoutData(new GridData(SWT.LEFT, SWT.BOTTOM, false, false, 1, 1));
		btnBank.setText("Bank");
		btnBank.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		btnGiro = new Button(composite_1, SWT.RADIO);
		btnGiro.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				dateCetak.setEnabled(true);
			}
		});
		btnGiro.setText("Giro");
		btnGiro.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BORDER));
		btnGiro.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		new Label(comp, SWT.NONE);
		
		new Label(comp, SWT.NONE);
		
		label_2 = new Label(comp, SWT.NONE);
		label_2.setText("Tanggal Pembayaran");
		label_2.setForeground(fontColor);
		label_2.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));

		datePembayaran = new DateTime(comp, SWT.BORDER | SWT.DROP_DOWN);
		datePembayaran.addKeyListener(new KeyAdapter() {
			@Override
		    public synchronized void keyPressed(KeyEvent e) {
		        pressed.add(e.keyCode);
//				System.out.println("Pressed : " + pressed.size());
		        if (pressed.size() > 1) {
		        	if (pressed.get(0) == SWT.CTRL && pressed.get(1) == SWT.CR && userModul.getIdUser() == 1){
		        		saveShortcut();
//		        		MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "OK", "Berhasil");
		        		pressed.clear();
		        	}
		        }
		    }

		    @Override
		    public synchronized void keyReleased(KeyEvent e) {
		    	try{
		    		pressed.clear();
//		    		System.out.println("Released : " + pressed.size());
		    	}catch (Exception ex) {
		    		ex.toString();
					// TODO: handle exception
				}
		        
		    }
		});
		datePembayaran.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		datePembayaran.addSelectionListener(new SelectionAdapter() {
			@SuppressWarnings("deprecation")
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (!cekTglSPT()){
					MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Tanggal Pembayaran tidak valid. Pembayaran tidak bisa dilakukan sebelum SKP terbit (" + listMasaPajak.get(cmbMasaPajak.getSelectionIndex()).getTanggalSPTPD() + ")");
					Date dateSPTPD = listMasaPajak.get(cmbMasaPajak.getSelectionIndex()).getTanggalSPTPD();
					datePembayaran.setDate(dateSPTPD.getYear()+1900, dateSPTPD.getMonth(), dateSPTPD.getDate());
				}
				if (btnTunai.getSelection()){
					dateCetak.setDate(datePembayaran.getYear(), datePembayaran.getMonth(), datePembayaran.getDay());
					Date bayar = new Date(datePembayaran.getYear() - 1900, datePembayaran.getMonth(), datePembayaran.getDay());
					Date tglAkhir = new Date(120,06,31);
					if (bayar.before(tglAkhir)){
						dateJatuhTempo.setDate(2020, 06, 31);
					}
				}
				if (!lblLunas.getVisible()){
					hitungPajak();
					//dendaSSPD = hitungDendaSSPD(pokokPajak);
					TableItem itemSSPD;
					if (btnTepatWaktu.getSelection())
						itemSSPD =  table.getItem(table.getItemCount()-1);
					else
						itemSSPD =  tblCicilan.getItem(tblCicilan.getItemCount()-1);
					itemSSPD.setText(new String[]{"Denda SSPD", indFormat.format(dendaSSPD)});
				}
				
			}
		});
		datePembayaran.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		datePembayaran.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		
		label_3 = new Label(comp, SWT.NONE);
		label_3.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1));
		label_3.setText("Tanggal Cetak");
		label_3.setForeground(fontColor);
		label_3.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		dateCetak = new DateTime(comp, SWT.BORDER | SWT.DROP_DOWN);
		dateCetak.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (!lblLunas.getVisible()){
					hitungPajak();
					dendaSSPD = hitungDendaSSPD(pokokPajak);
					TableItem itemSSPD;
					if (btnTepatWaktu.getSelection())
						itemSSPD =  table.getItem(table.getItemCount()-1);
					else
						itemSSPD =  tblCicilan.getItem(tblCicilan.getItemCount()-1);
					//TableItem item = table.getItem(table.getItemCount()-1);
					itemSSPD.setText(new String[]{"Denda SSPD", indFormat.format(dendaSSPD)});
	//				totalPajakTerutang = pokokPajak + dendaSSPD;
	//				txtTotalPajakTerhutang.setMoney(new CurrencyAmount(totalPajakTerutang, Currency.getInstance("ind")));
				}
			}
		});
		dateCetak.setEnabled(false);
		dateCetak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		dateCetak.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
//		new Label(comp, SWT.NONE);
//		new Label(comp, SWT.NONE);
//		new Label(comp, SWT.NONE);
//		new Label(comp, SWT.NONE);
//		new Label(comp, SWT.NONE);
//		new Label(comp, SWT.NONE);
//		new Label(comp, SWT.NONE);
//		new Label(comp, SWT.NONE);
//		new Label(comp, SWT.NONE);
//		new Label(comp, SWT.NONE);
//		new Label(comp, SWT.NONE);
//		new Label(comp, SWT.NONE);
//		new Label(comp, SWT.NONE);
//		new Label(comp, SWT.NONE);
//		new Label(comp, SWT.NONE);
//		new Label(comp, SWT.NONE);
//		new Label(comp, SWT.NONE);
//		new Label(comp, SWT.NONE);
//		new Label(comp, SWT.NONE);
//		new Label(comp, SWT.NONE);
//		new Label(comp, SWT.NONE);
//		new Label(comp, SWT.NONE);
//		new Label(comp, SWT.NONE);
//		new Label(comp, SWT.NONE);
		
		table = new Table(comp, SWT.BORDER | SWT.FULL_SELECTION | SWT.VIRTUAL);
		table.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		table.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		table.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE-1, SWT.NORMAL));
		GridData gd_table = new GridData(SWT.FILL, SWT.FILL, false, false, 7, 1);
		gd_table.heightHint = 122;
		table.setLayoutData(gd_table);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.keyCode == 99){
					clipboard.setContents(new Object[] { table.getItem(table.getSelectionIndex()).getText(1) },
				              new Transfer[] { TextTransfer.getInstance() });
				}
			}
		});
		Menu menu = new Menu(table);
		MenuItem menuCopy = new MenuItem(menu, SWT.PUSH);
		menuCopy.setText("Copy..");
		menuCopy.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				clipboard.setContents(new Object[] { table.getItem(table.getSelectionIndex()).getText(1) },
			              new Transfer[] { TextTransfer.getInstance() });
			}
		});
		table.setMenu(menu);
		
		tblCicilan = new Table(comp, SWT.BORDER | SWT.FULL_SELECTION);
//		tblCicilan.addMouseListener(new MouseAdapter() {
//			@Override
//			public void mouseDoubleClick(MouseEvent e) {
//				int index = tblCicilan.getSelectionIndex();
//				cicilanKe = Integer.parseInt(tblCicilan.getItem(index).getText(0));
//				Double totalAngsur = 0.0;
//				totalDendaAngsuran = 0;
//				for(TableItem ti : tblCicilan.getItems()){
//					if (Integer.parseInt(ti.getText(0)) == cicilanKe)
//						break;
////					totalAngsur += indFormat.parse(ti.getText(1));
////					tableAngsuran.getItem(i).getText(1)).doubleValue()
//					
//				}
//			}
//		});
		tblCicilan.setLinesVisible(true);
		tblCicilan.setHeaderVisible(true);
		tblCicilan.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		tblCicilan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE-1, SWT.NORMAL));
		tblCicilan.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		GridData gd_tblCicilan = new GridData(SWT.LEFT, SWT.FILL, false, false, 7, 1);
		gd_tblCicilan.widthHint = 516;
		tblCicilan.setVisible(false);
		tblCicilan.setLayoutData(gd_tblCicilan);
		tblCicilan.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.keyCode == 99){
					clipboard.setContents(new Object[] { tblCicilan.getItem(tblCicilan.getSelectionIndex()).getText(1) },
				              new Transfer[] { TextTransfer.getInstance() });
				}
			}
		});
		Menu menuCicilan = new Menu(tblCicilan);
		MenuItem menuItemCicilan = new MenuItem(menuCicilan, SWT.PUSH);
		menuItemCicilan.setText("Copy..");
		menuItemCicilan.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				clipboard.setContents(new Object[] { tblCicilan.getItem(tblCicilan.getSelectionIndex()).getText(1) },
			              new Transfer[] { TextTransfer.getInstance() });
			}
		});
		tblCicilan.setMenu(menuCicilan);
		
		tableColumn = new TableColumn(tblCicilan, SWT.NONE, 0);
		tableColumn.setWidth(250);
		tableColumn.setText("Pajak");
		
		tableColumn_1 = new TableColumn(tblCicilan, SWT.NONE, 1);
		tableColumn_1.setWidth(250);
		tableColumn_1.setText("Nilai");
		
		Label lblTotalPajakTerhutang = new Label(comp, SWT.NONE);
		lblTotalPajakTerhutang.setForeground(fontColor);
		lblTotalPajakTerhutang.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblTotalPajakTerhutang.setText("TOTAL PAJAK TERHUTANG");
		
		txtTotalPajakTerhutang = new MoneyField(comp, SWT.BORDER);
		txtTotalPajakTerhutang.getText().setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtTotalPajakTerhutang.getText().setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtTotalPajakTerhutang.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtTotalPajakTerhutang.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtTotalPajakTerhutang.addMoneyChangeListener(new IMoneyChangeListener() {
			@Override
			public void moneyChange(MoneyChangeEvent event) {
				checkSisaDenda();
			}
		});
		GridData gd_txtTotalPajakTerhutang = new GridData(SWT.LEFT, SWT.CENTER, false, false, 5, 1);
		gd_txtTotalPajakTerhutang.widthHint = 211;
		txtTotalPajakTerhutang.setLayoutData(gd_txtTotalPajakTerhutang);
		Text child = (Text)txtTotalPajakTerhutang.getChildren()[0];
		child.setEditable(false);
		new Label(txtTotalPajakTerhutang, SWT.NONE);
		
		lblSudahDiangsur = new Label(comp, SWT.NONE);
		lblSudahDiangsur.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblSudahDiangsur.setText("- Sudah diangsur");
		lblSudahDiangsur.setForeground(fontColor);
		lblSudahDiangsur.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblSudahDiangsur.setVisible(false);
		
		txtTotalAngsur = new MoneyField(comp, SWT.BORDER);
		txtTotalAngsur.getText().setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtTotalAngsur.addMoneyChangeListener(new IMoneyChangeListener() {
			public void moneyChange(MoneyChangeEvent event) {
				totalAngsurChanged();
			}
		});
		txtTotalAngsur.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtTotalAngsur.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtTotalAngsur.setVisible(false);
		GridData gd_txtTotalAngsur = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
		gd_txtTotalAngsur.widthHint = 183;
		txtTotalAngsur.setLayoutData(gd_txtTotalAngsur);
		Text childAngsur = (Text)txtTotalAngsur.getChildren()[0];
		childAngsur.setEditable(false);
		new Label(txtTotalAngsur, SWT.NONE);
		
		lblSisaPajak = new Label(comp, SWT.NONE);
		lblSisaPajak.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblSisaPajak.setText("= Sisa Pajak Terutang");
		lblSisaPajak.setForeground(fontColor);
		lblSisaPajak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblSisaPajak.setVisible(false);
		
		txtSisa = new MoneyField(comp, SWT.BORDER);	
		txtSisa.getText().setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtSisa.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtSisa.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		GridData gd_txtSisa = new GridData(SWT.LEFT, SWT.CENTER, false, false, 4, 1);
		gd_txtSisa.widthHint = 192;
		txtSisa.setLayoutData(gd_txtSisa);
		txtSisa.setVisible(false);
		Text childSisa = (Text)txtSisa.getChildren()[0];
		childSisa.setEditable(false);
		new Label(txtSisa, SWT.NONE);
		
		Label lblDibayar = new Label(comp, SWT.NONE);
		lblDibayar.setForeground(fontColor);
		lblDibayar.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblDibayar.setText("Dibayar");
		
		txtDibayar = new MoneyField(comp, SWT.BORDER);
		txtDibayar.getText().setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtDibayar.getText().setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtDibayar.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtDibayar.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtDibayar.addMoneyChangeListener(new IMoneyChangeListener() {
			@Override
			public void moneyChange(MoneyChangeEvent event) {
				checkSisaDenda();
//				DecimalFormat df = new DecimalFormat("#.##");
//				df.setMaximumFractionDigits(2);
//				df.setRoundingMode(RoundingMode.HALF_EVEN);
				Double jlhBayar = round(txtDibayar.getMoney().getNumber().doubleValue()); 
				if (!btnAngsuran.getSelection()){
					if(table.getItemCount()>1){
						try {
							if(indFormat.parse(table.getItem(0).getText(1)).doubleValue() > jlhBayar)
								btnBayar.setEnabled(false);
							else
								if (enable)
									btnBayar.setEnabled(true);
								
						} catch (ParseException e) {
							e.printStackTrace();
						}
					}
				}
			}
		});
		GridData gd_txtDibayar = new GridData(SWT.LEFT, SWT.CENTER, false, false, 5, 1);
		gd_txtDibayar.widthHint = 210;
		txtDibayar.setLayoutData(gd_txtDibayar);
		new Label(txtDibayar, SWT.NONE);
		
		lblSisaDenda = new Label(comp, SWT.NONE);
		lblSisaDenda.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblSisaDenda.setForeground(fontColor);
		lblSisaDenda.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblSisaDenda.setText("Sisa Denda");
		lblSisaDenda.setVisible(false);
		
		txtSisaDendaDibayar = new MoneyField(comp, SWT.BORDER);
		GridData gd_txtSisaDendaDibayar = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
		gd_txtSisaDendaDibayar.widthHint = 181;
		txtSisaDendaDibayar.setLayoutData(gd_txtSisaDendaDibayar);
		txtSisaDendaDibayar.getText().setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtSisaDendaDibayar.getText().setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtSisaDendaDibayar.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtSisaDendaDibayar.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtSisaDendaDibayar.setVisible(false);
		new Label(txtSisaDendaDibayar, SWT.NONE);
		
		btnGenerateStsDenda = new Button(comp, SWT.NONE);
		btnGenerateStsDenda.addSelectionListener(new SelectionAdapter() {
			@SuppressWarnings("deprecation")
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (isSave()){
					String skp = "";
					String message = "";
					if (txtNoSPTPD.getText().contains("SPTPD") || txtNoSPTPD.getText().contains("SKPD/"))
						skp = "SPTPD";
					else if (txtNoSPTPD.getText().contains("SKPDKB"))
						skp = "SKPDKB";
					final String[] listString = getParamDenda(skp);
					String stsDenda = "";
					if (txtNoSTS.getText().contains("-"))
						stsDenda = txtNoSTS.getText().substring(txtNoSTS.getText().indexOf("-")+1);
					else
						stsDenda = "";
					final String noSTS = stsDenda;
					resultApi = false;
					ProgressMonitorDialog progress = new ProgressMonitorDialog(Display.getCurrent().getActiveShell());
					try {
						progress.run(false, false, new IRunnableWithProgress() {
							@Override
							public void run(IProgressMonitor monitor) throws InvocationTargetException,InterruptedException {
								resultApi = ApiPost.Instance.sendPostDenda(listString, noSTS, monitor);
							}
						});
					} catch (InvocationTargetException | InterruptedException e1) {
						e1.printStackTrace();
					}
					if (resultApi){
						if (noSTS.equalsIgnoreCase("")){
							MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "STS Berhasil disimpan");
							message = "Insert STS";
						}
						else{
							MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "STS Berhasil diperbarui");
							message = "Update STS";
						}
						StringBuffer sb = new StringBuffer();
						sb.append(message +
								" SSPD:"+txtNoSSPD.getText()+
								" SPTPD:"+txtNoSPTPD.getText()+
								" STS:"+txtNoSTS.getText()+
								" NPWPD:"+wp.getNPWP()+
								" TanggalSSPD:"+new Date(datePembayaran.getYear()-1900, datePembayaran.getMonth(), datePembayaran.getDay())+
								" MasaPajak:"+cmbMasaPajak.getText()+
								" JumlahBayar:"+txtDibayar.getText().getText()+
								" SisaDenda:"+txtSisaDendaDibayar.getText().getText()+
								" PajakTerhutang:"+txtTotalPajakTerhutang.getText().getText());
						new LogImp().setLog(sb.toString(), userModul.getDisplayName(), userModul.getSubModul().getNamaModul());
						
						int idx = cmbMasaPajak.getSelectionIndex();
						getDataWP();
						
						listMasaPajak = ControllerFactory.getMainController().getCpSptpdDAOImpl().getMasaPajakSPTPD(wp.getNPWP());
						listMasaPajak.addAll(ControllerFactory.getMainController().getCpSptpdDAOImpl().getMasaPajakSKPDKB(wp.getNPWP()));
						
						Collections.sort(listMasaPajak);
						
						if (listMasaPajak.size() > 0)
							UIController.INSTANCE.loadTMasaPajak(cmbMasaPajak, listMasaPajak.toArray()); //PajakProvider.INSTANCE.getPajak().toArray());
						cmbMasaPajak.select(idx);
						cmbMasaPajakSelected();
					}
				}
			}
		});
		btnGenerateStsDenda.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnGenerateStsDenda.setVisible(false);
		btnGenerateStsDenda.setText("Generate STS Denda");
		
		btnCetakDenda = new Button(comp, SWT.NONE);
		btnCetakDenda.addSelectionListener(new SelectionAdapter() {
			@SuppressWarnings("deprecation")
			@Override
			public void widgetSelected(SelectionEvent e) {
				double jumlahPemeriksaan = 0;
				if(isPrint()){
					ReportAppContext.name = "Nota_Antri";
					List<Pajak> listPajak = ControllerFactory.getMainController().getCpPajakDAOImpl().getPajak(Integer.parseInt(txtKodePajak.getText()));
					if(!ReportAppContext.map.isEmpty())
						ReportAppContext.map.clear();
					String NoAntri = "";
					if (txtNoSSPD.getText().split("-")[1].contains("SSPD"))
						NoAntri = ControllerFactory.getMainController().getCpSspdDAOImpl().loadNoNotaAntri(txtNoSSPD.getText().split("-")[1]);
					else
						NoAntri = txtNoSSPD.getText().split("-")[1];
					double totalPajak = 0;
					double sisa = 0;
					double JumlahBayar = 0;
					try {
						totalPajak = indFormat.parse((table.getItem(2).getText(1))).doubleValue();
						if (txtSisaDendaDibayar.getMoney().getNumber().doubleValue() <= 1)
							JumlahBayar = totalPajak;
						else
							JumlahBayar = txtSisaDendaDibayar.getMoney().getNumber().doubleValue();
						sisa = totalPajak - JumlahBayar;
					} catch (ParseException e2) {
						e2.printStackTrace();
					}
					Double bayar = ControllerFactory.getMainController().getCpSspdDAOImpl().getKurangBayarDenda(NoAntri);
					if (totalPajak > bayar + 1){
						JumlahBayar = bayar;
						totalPajak = bayar;
					}
					int slashCount = txtNoSPTPD.getText().length() - txtNoSPTPD.getText().replace("/", "").length();
					ReportAppContext.map.put("NoAntri", NoAntri);
					ReportAppContext.map.put("Nama", txtNamaBadan.getText());
					ReportAppContext.map.put("Alamat", txtAlamat.getText());
					ReportAppContext.map.put("NPWPD", txtKodePajak.getText()+txtNoUrut.getText()+txtKodeKecamatan.getText()+txtKodeKelurahan.getText());
					ReportAppContext.map.put("NamaPajak", listPajak.get(0).getNamaPajak());
					ReportAppContext.map.put("TipeSKP", txtNoSPTPD.getText().split("/")[slashCount-1]); 
					ReportAppContext.map.put("MasaPajak", cmbMasaPajak.getText());
					ReportAppContext.map.put("NoSKP", txtNoSPTPD.getText());
					ReportAppContext.map.put("KodePajak", ControllerFactory.getMainController().getCpBidangUsahaDAOImpl().getKodeBidUsaha(wp.getIdSubPajak())); //belum tau
					ReportAppContext.map.put("KodeDenda", listPajak.get(0).getKodeDenda());
					ReportAppContext.map.put("TotalPajak", indFormat.format(totalPajak));
					ReportAppContext.map.put("Total", indFormat.format(JumlahBayar));
					ReportAppContext.map.put("Sisa", indFormat.format(sisa));
					ReportAppContext.map.put("Terbilang", new ReportModule().getTerbilang(JumlahBayar));
					ReportAppContext.map.put("TanggalPenyetor", String.format("%02d", dateCetak.getDay())+"-"+String.format("%02d", dateCetak.getMonth() + 1)+"-"+dateCetak.getYear());
					ReportAppContext.map.put("NamaPenyetor", txtNamaPenyetor.getText());
					
					if (txtNoSPTPD.getText().contains("SPTPD") || txtNoSPTPD.getText().contains("/SKPD/")){
						for(TableItem ti : table.getItems()){
							if(ti.getText(0).equalsIgnoreCase("Pokok Pajak")){
								ReportAppContext.map.put("PokokPajak", "Rp0.00");
							}else if(ti.getText(0).equalsIgnoreCase("Denda SSPD")){
								ReportAppContext.map.put("Denda", indFormat.format(bayar));
							}else if(ti.getText(0).equalsIgnoreCase("Denda Tambahan")){
								ReportAppContext.map.put("DendaTambahan", ti.getText(1));
							}
						}
						ReportAppContext.map.put("Lokasi", txtLokasi.getText());
						ReportAction.start("NotaAntriSSPD.rptdesign");
					}
					else{
						for(TableItem ti : table.getItems()){
							if(ti.getText(0).equalsIgnoreCase("Pokok Pajak")){
								ReportAppContext.map.put("PokokPajak", ti.getText(1));
							}else if(ti.getText(0).equalsIgnoreCase("Denda")){
								ReportAppContext.map.put("Denda", ti.getText(1));
							}else if(ti.getText(0).equalsIgnoreCase("Denda Tambahan")){
								ReportAppContext.map.put("KenaikanPajak", ti.getText(1));
							}else if(ti.getText(0).equalsIgnoreCase("Denda Terlambat SKPDKB")){
								ReportAppContext.map.put("DendaTerlambatSKPDKB", ti.getText(1));
							}else if(ti.getText(0).equalsIgnoreCase("Denda Terlambat Sebelumnya")){
								ReportAppContext.map.put("DendaTerlambatSebelumnya", ti.getText(1));
							}else if(ti.getText(0).equalsIgnoreCase("Denda SSPD")){
								ReportAppContext.map.put("DendaSSPD", ti.getText(1));
							}
						}
						for(TableItem ti : tblCicilan.getItems()){
							if(ti.getText(0).equalsIgnoreCase("Denda SSPD")){
								ReportAppContext.map.put("DendaSSPD", ti.getText(1));
							}
						}
						double JumlahBayarSebelum = 0;
						try {
							jumlahPemeriksaan = indFormat.parse(table.getItem(0).getText(1)).doubleValue() + indFormat.parse(table.getItem(1).getText(1)).doubleValue() + indFormat.parse(table.getItem(2).getText(1)).doubleValue() + indFormat.parse(table.getItem(3).getText(1)).doubleValue();
							totalPajak = jumlahPemeriksaan + indFormat.parse(table.getItem(4).getText(1)).doubleValue();
							if (txtSisaDendaDibayar.getMoney().getNumber().doubleValue() <= 1)
								JumlahBayar = indFormat.parse((table.getItem(4).getText(1))).doubleValue();
							else
								JumlahBayar = txtSisaDendaDibayar.getMoney().getNumber().doubleValue();

							//Perbaikan coding saat pembayaran pertama, denda dibayarkan setengah. kemudian bayar sisa dendanya di pembayaran kedua
							if (JumlahBayar > bayar + 1)
								JumlahBayar = bayar;  
							
							if (txtSisaDendaDibayar.getMoney().getNumber().doubleValue() <= 1)
								JumlahBayarSebelum = totalPajak - JumlahBayar;
							else
								JumlahBayarSebelum = txtDibayar.getMoney().getNumber().doubleValue();
//							sisa = totalPajak - JumlahBayar;
							
							
						} catch (ParseException e1) {
							e1.printStackTrace();
						}
						ReportAppContext.map.put("JumlahPemeriksaan", indFormat.format(jumlahPemeriksaan));
						ReportAppContext.map.put("JumlahBayarSebelum", indFormat.format(JumlahBayarSebelum));
						ReportAppContext.map.put("Total", indFormat.format(JumlahBayar));
						ReportAppContext.map.put("TotalPajak", indFormat.format(totalPajak));
						ReportAppContext.map.put("Terbilang", new ReportModule().getTerbilang(JumlahBayar));
						if (btnAngsuran.getSelection())
							ReportAppContext.map.put("JumlahTerhutang", indFormat.format(txtSisa.getMoney().getNumber().doubleValue() - txtDibayar.getMoney().getNumber().doubleValue()));
						else{
							ReportAppContext.map.put("JumlahTerhutang", indFormat.format(txtTotalPajakTerhutang.getMoney().getNumber().doubleValue() - txtDibayar.getMoney().getNumber().doubleValue() - txtSisaDendaDibayar.getMoney().getNumber().doubleValue()));
							ReportAppContext.map.put("DendaTerlambatSebelumnya", "Rp0,00");
						}
						
						ReportAction.start("NotaAntriSSPD_SKPDKB.rptdesign");
					}
				}else
					MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Anda tidak memiliki hak untuk mencetak.");
			}
		});
		GridData gd_btnCetakDenda = new GridData(SWT.LEFT, SWT.CENTER, false, false, 4, 1);
		gd_btnCetakDenda.widthHint = 94;
		btnCetakDenda.setLayoutData(gd_btnCetakDenda);
		btnCetakDenda.setText("Cetak Denda");
		btnCetakDenda.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnCetakDenda.setVisible(false);
		
		Label lblNamaPenyetor = new Label(comp, SWT.NONE);
		lblNamaPenyetor.setForeground(fontColor);
		lblNamaPenyetor.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblNamaPenyetor.setText("Nama Penyetor");
		
		txtNamaPenyetor = new Text(comp, SWT.BORDER);
		txtNamaPenyetor.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtNamaPenyetor.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		GridData gd_txtNamaPenyetor = new GridData(SWT.LEFT, SWT.CENTER, false, false, 5, 1);
		gd_txtNamaPenyetor.widthHint = 200;
		txtNamaPenyetor.setLayoutData(gd_txtNamaPenyetor);
		new Label(comp, SWT.NONE);
		
		lblAngsuranKe = new Label(comp, SWT.NONE);
		lblAngsuranKe.setVisible(false);
		lblAngsuranKe.setText("Angsuran Ke");
		
		cmbAngsuran = new Combo(comp, SWT.READ_ONLY);
		cmbAngsuran.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int idMohon = ControllerFactory.getMainController().getCpMohonDAOImpl().checkMohon(wp.getNPWP(), txtNoSPTPD.getText());
				List<MohonDetail> md = ControllerFactory.getMainController().getCpMohonDetailDAOImpl().getDaftarAngsuran(idMohon);
				if (md.get(Integer.valueOf(cmbAngsuran.getText())-1).getSts() == null)
					txtNoSTS.setText("");
				else
					txtNoSTS.setText(md.get(Integer.valueOf(cmbAngsuran.getText())-1).getSts());
			}
		});
		cmbAngsuran.setVisible(false);
		
		btnBayarDenda = new Button(comp, SWT.NONE);
		GridData gd_btnBayar_1 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnBayar_1.widthHint = 90;
		btnBayarDenda.setLayoutData(gd_btnBayar_1);
		btnBayarDenda.setText("Bayar");
		btnBayarDenda.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnBayarDenda.setVisible(false);
		btnBayarDenda.addSelectionListener(new SelectionAdapter() {
			@SuppressWarnings("deprecation")
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(isSave()){
					if(isValidate()){
	//					String noSSPD = "";
						String notaBayar = "";
						dateNow = (java.sql.Timestamp)ControllerFactory.getMainController().getCpWajibPajakDAOImpl().getDateNow();
						SimpleDateFormat sdf = new SimpleDateFormat("ddMMyy");
						java.util.Date tglSSPD = new java.util.Date(datePembayaran.getYear() - 1900, datePembayaran.getMonth(), datePembayaran.getDay(), dateNow.getHours(), dateNow.getMinutes(), dateNow.getSeconds());
						java.util.Date tglCetak = new java.util.Date(dateCetak.getYear() - 1900, dateCetak.getMonth(), dateCetak.getDay(), dateNow.getHours(), dateNow.getMinutes(), dateNow.getSeconds());
						SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
						Sspd sspd = new Sspd();
						sspd.setIdSspd(selectedID);
//						if (selectedID == null){
							notaBayar = ControllerFactory.getMainController().getCpSspdDAOImpl().getNotaBayar(sdf.format(dateNow));
							cicilanKe = tableAngsuran.getItemCount() + 1; 
							//noSSPD = ControllerFactory.getMainController().getCpSspdDAOImpl().getNoSSPD(String.valueOf(datePembayaran.getYear()));
//						}
//						else{
//							if (txtNoSSPD.getText().substring(txtNoSSPD.getText().length() - 6).equalsIgnoreCase(sdf.format(dateNow)))
//								notaBayar = txtNoSSPD.getText();
	//						cicilanKe = tableAngsuran.getItemCount() 
//							if (! notaBayar.substring(notaBayar.length() - 6).equalsIgnoreCase(sdf.format(dateNow)))
//								notaBayar = ControllerFactory.getMainController().getCpSspdDAOImpl().getNotaBayar(sdf.format(dateNow));
//						}
						String datesspd = dateFormat.format(tglSSPD);
						sspd.setTglSspd(java.sql.Timestamp.valueOf(datesspd));
						String datecetak = dateFormat.format(tglCetak);
						sspd.setTglCetak(java.sql.Timestamp.valueOf(datecetak));
	//					sspd.setTglSspd(new Date(datePembayaran.getYear() - 1900, datePembayaran.getMonth(), datePembayaran.getDay()));
	//					sspd.setTglCetak(new Date(dateCetak.getYear() - 1900, dateCetak.getMonth(), dateCetak.getDay()));
						sspd.setNpwpd(wp.getNPWP());
						sspd.setBulanSkp(bulanSKP);
						sspd.setNoSkp(txtNoSPTPD.getText());
						if (txtNoSPTPD.getText().contains("SPTPD"))
							sspd.setDenda(dendaSSPD + dendaTambahan);
						else
							sspd.setDenda(dendaSSPD);
						sspd.setJumlahBayar(txtSisaDendaDibayar.getMoney().getNumber().doubleValue());
						if (btnTunai.getSelection())
							caraBayar = "Tunai";
						else if (btnGiro.getSelection())
							caraBayar = "Giro";
						else if (btnBank.getSelection())
							caraBayar = "Bank";
						sspd.setCaraBayar(caraBayar);			
						sspd.setNamaPenyetor(txtNamaPenyetor.getText());
						sspd.setIdSubPajak(idSubPajak);
						sspd.setIdSPTPD(idSPTPD);
						sspd.setIdPeriksa(idPeriksa);
						sspd.setIdSspd(null);
						if (btnTepatWaktu.getSelection()) {
							jenisBayar = 0;
							cicilanKe = 0;
							// di set saat dibayar di listDaftarAntrian
							
							sspd.setDendaLunas(true);
								
						}
						else {
							jenisBayar = 2;
							try {
								if (indFormat.parse(tblCicilan.getItem(0).getText(1)).doubleValue() + indFormat.parse(tblCicilan.getItem(1).getText(1)).doubleValue() - txtDibayar.getMoney().getNumber().doubleValue() <= 1){
									if (indFormat.parse(tblCicilan.getItem(0).getText(1)).doubleValue() + indFormat.parse(tblCicilan.getItem(1).getText(1)).doubleValue() - txtDibayar.getMoney().getNumber().doubleValue() >= -1 ||
										indFormat.parse(tblCicilan.getItem(0).getText(1)).doubleValue() + indFormat.parse(tblCicilan.getItem(1).getText(1)).doubleValue() - txtDibayar.getMoney().getNumber().doubleValue() <= 1){
										sspd.setDendaLunas(true);
									}
									else{
										sspd.setDendaLunas(true);
										double sisaDenda = txtDibayar.getMoney().getNumber().doubleValue() - (indFormat.parse(tblCicilan.getItem(0).getText(1)).doubleValue() + indFormat.parse(tblCicilan.getItem(1).getText(1)).doubleValue());
										for (int i=0;i<tableAngsuran.getItemCount();i++){
											if (sisaDenda - updDenda[i] >= 0){
												ControllerFactory.getMainController().getCpSspdDAOImpl().updateDendaSebelumnya(IDSSPDAngsur[i]);
												sisaDenda -= updDenda[i];
											}
										}
									}
								}
								else if (indFormat.parse(tblCicilan.getItem(0).getText(1)).doubleValue() - txtDibayar.getMoney().getNumber().doubleValue() <= 1){
									sspd.setDendaLunas(false);
								}
								
							} catch (ParseException e1) {
								e1.printStackTrace();
							}
							
	//						if (angsuranTerakhir == cicilanKe)
						}
						sspd.setJenisBayar(jenisBayar);
						sspd.setCicilanKe(cicilanKe);
						sspd.setNoSspd("");
						sspd.setNotaBayar(notaBayar);
						
						if(ControllerFactory.getMainController().getCpSspdDAOImpl().saveSspd(sspd)){
							MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Data berhasil disimpan.");
//							if (selectedID == null) {
								selectedID = ControllerFactory.getMainController().getCpSspdDAOImpl().getLastId(wp.getNPWP());
								txtNoSSPD.setText(txtNoSSPD.getText() + "-" + notaBayar);
								lblNoSspd.setText("No SSPD");
//							}
							ControllerFactory.getMainController().getCpSspdDAOImpl().setToDenda(selectedID);
//							lblLunas.setVisible(false);
							compPejabat.setVisible(true);
							loadPejabat();
							if (btnAngsuran.getSelection()){
								loadAngsuran();
								tblCicilan.remove(tblCicilan.getItemCount() - 1);
								table.remove(table.getItemCount() - 1);
							}
							StringBuffer sb = new StringBuffer();
							sb.append("SAVE " +
									"SSPD:"+txtNoSSPD.getText()+
									" SPTPD:"+txtNoSPTPD.getText()+
									" NPWPD:"+wp.getNPWP()+
									" TanggalSSPD:"+tglSSPD+
									" MasaPajak:"+cmbMasaPajak.getText()+
									" JumlahBayar:"+txtDibayar.getText().getText()+
									" SisaDenda:"+txtSisaDendaDibayar.getText().getText()+
									" PajakTerhutang:"+txtTotalPajakTerhutang.getText().getText());
							new LogImp().setLog(sb.toString(), userModul.getDisplayName(), userModul.getSubModul().getNamaModul());
						}else{
							MessageDialog.openError(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Error pada Database. Silahkan hubungi ADMIN.");
						}
					}else
						MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Silahkan periksa kembali.");
				}else
					MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Anda tidak memiliki hak untuk menyimpan atau merubah data.");
			}
		});
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		
		lblLunas = new Label(comp, SWT.BORDER | SWT.SHADOW_IN);
		lblLunas.setAlignment(SWT.CENTER);
		lblLunas.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		lblLunas.setText("LUNAS");
		lblLunas.setForeground(fontColor);
		lblLunas.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, 15, SWT.NORMAL));
		lblLunas.setVisible(false);
		
		compButton = new Composite(comp, SWT.NONE);
		compButton.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 6, 1));
		compButton.setLayout(new GridLayout(3, false));
		
		btnGenerateSts = new Button(compButton, SWT.NONE);
		btnGenerateSts.setEnabled(false);
		btnGenerateSts.addSelectionListener(new SelectionAdapter() {
			@SuppressWarnings("deprecation")
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (isSave()){
					String skp = "";
					String message = "";
					if (btnAngsuran.getSelection()){
						String nilai = "";
						Double dendaAngsuran = 0.0;
						String nilaiDenda = "";
						final int idMohon = ControllerFactory.getMainController().getCpMohonDAOImpl().checkMohon(wp.getNPWP(), txtNoSPTPD.getText());
						List<MohonDetail> md = ControllerFactory.getMainController().getCpMohonDetailDAOImpl().getDaftarAngsuran(idMohon);
						if (cmbAngsuran.getSelectionIndex()<0){
							nilai = indFormat.format(roundUP(md.get(tableAngsuran.getItemCount()).getAngsuranPokok())).substring(2).replace(".", "").replace(",", ".");
							dendaAngsuran = roundUP(md.get(tableAngsuran.getItemCount()).getBiayaAdministrasi()) + roundUP(md.get(tableAngsuran.getItemCount()).getDendaSkpdkb()) + dendaSSPD;
							nilaiDenda = indFormat.format(dendaAngsuran).substring(2).replace(".", "").replace(",", ".");
							cicil = tableAngsuran.getItemCount()+1;
						}else{
							nilai = indFormat.format(roundUP(md.get(Integer.valueOf(cmbAngsuran.getText())-1).getAngsuranPokok())).substring(2).replace(".", "").replace(",", ".");
							dendaAngsuran = roundUP(md.get(Integer.valueOf(cmbAngsuran.getText())-1).getBiayaAdministrasi()) + roundUP(md.get(tableAngsuran.getItemCount()).getDendaSkpdkb()) + dendaSSPD;
							nilaiDenda = indFormat.format(dendaAngsuran).substring(2).replace(".", "").replace(",", ".");
							cicil = Integer.valueOf(cmbAngsuran.getText());
						}
						
						final String[] listString = getParamAngsuran(nilai, nilaiDenda);
						String stsPokok = "";
						if (txtNoSTS.getText().contains("-")){
							stsPokok = txtNoSTS.getText().substring(0, txtNoSTS.getText().indexOf("-"));
						}else{
							stsPokok = txtNoSTS.getText();
						}
						final String noSTS = stsPokok;
						resultApi = false;
						ProgressMonitorDialog progress = new ProgressMonitorDialog(Display.getCurrent().getActiveShell());
						try {
							progress.run(false, false, new IRunnableWithProgress() {
								@Override
								public void run(IProgressMonitor monitor) throws InvocationTargetException,InterruptedException {
									resultApi = ApiPost.Instance.sendPostAngsuran(listString, noSTS, idMohon, cicil, monitor);
								}
							});
						} catch (InvocationTargetException | InterruptedException e1) {
							e1.printStackTrace();
						}
						if (resultApi){
							if (noSTS.equalsIgnoreCase("")){
								MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "STS Berhasil disimpan");
								message = "Insert STS";
							}
							else{
								MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "STS Berhasil diperbarui");
								message = "Update STS";
							}
							StringBuffer sb = new StringBuffer();
							sb.append(message +
									" SSPD:"+txtNoSSPD.getText()+
									" SPTPD:"+txtNoSPTPD.getText()+
									" STS:"+txtNoSTS.getText()+
									" NPWPD:"+wp.getNPWP()+
									" TanggalSSPD:"+new Date(datePembayaran.getYear(), datePembayaran.getMonth(), datePembayaran.getDay())+
									" MasaPajak:"+cmbMasaPajak.getText()+
									" JumlahBayar:"+txtDibayar.getText().getText()+
									" PajakTerhutang:"+txtTotalPajakTerhutang.getText().getText());
							new LogImp().setLog(sb.toString(), userModul.getDisplayName(), userModul.getSubModul().getNamaModul());
							
							int idx = cmbMasaPajak.getSelectionIndex();
							getDataWP();
							
							listMasaPajak = ControllerFactory.getMainController().getCpSptpdDAOImpl().getMasaPajakSPTPD(wp.getNPWP());
							listMasaPajak.addAll(ControllerFactory.getMainController().getCpSptpdDAOImpl().getMasaPajakSKPDKB(wp.getNPWP()));
							
							Collections.sort(listMasaPajak);
							
							if (listMasaPajak.size() > 0)
								UIController.INSTANCE.loadTMasaPajak(cmbMasaPajak, listMasaPajak.toArray()); //PajakProvider.INSTANCE.getPajak().toArray());
							cmbMasaPajak.select(idx);
							cmbMasaPajakSelected();
						}
					}else{
						if (txtNoSPTPD.getText().contains("SPTPD") || txtNoSPTPD.getText().contains("SKPD/"))
							skp = "SPTPD";
						else if (txtNoSPTPD.getText().contains("SKPDKB"))
							skp = "SKPDKB";
						final String[] listString = getParam(skp);
						String stsPokok = "";
						if (txtNoSTS.getText().contains("-")){
							stsPokok = txtNoSTS.getText().substring(0, txtNoSTS.getText().indexOf("-"));
						}else{
							stsPokok = txtNoSTS.getText();
						}
						final String noSTS = stsPokok;
						resultApi = false;
						ProgressMonitorDialog progress = new ProgressMonitorDialog(Display.getCurrent().getActiveShell());
						try {
							progress.run(false, false, new IRunnableWithProgress() {
								@Override
								public void run(IProgressMonitor monitor) throws InvocationTargetException,InterruptedException {
									resultApi = ApiPost.Instance.sendPost(listString, noSTS, monitor);
								}
							});
						} catch (InvocationTargetException | InterruptedException e1) {
							e1.printStackTrace();
						}
						if (resultApi){
							if (noSTS.equalsIgnoreCase("")){
								MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "STS Berhasil disimpan");
								message = "Insert STS";
							}
							else{
								MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "STS Berhasil diperbarui");
								message = "Update STS";
							}
							StringBuffer sb = new StringBuffer();
							sb.append(message +
									" SSPD:"+txtNoSSPD.getText()+
									" SPTPD:"+txtNoSPTPD.getText()+
									" STS:"+txtNoSTS.getText()+
									" NPWPD:"+wp.getNPWP()+
									" TanggalSSPD:"+new Date(datePembayaran.getYear()-1900, datePembayaran.getMonth(), datePembayaran.getDay())+
									" MasaPajak:"+cmbMasaPajak.getText()+
									" JumlahBayar:"+txtDibayar.getText().getText()+
									" PajakTerhutang:"+txtTotalPajakTerhutang.getText().getText());
							new LogImp().setLog(sb.toString(), userModul.getDisplayName(), userModul.getSubModul().getNamaModul());
							
							int idx = cmbMasaPajak.getSelectionIndex();
							getDataWP();
							
							listMasaPajak = ControllerFactory.getMainController().getCpSptpdDAOImpl().getMasaPajakSPTPD(wp.getNPWP());
							listMasaPajak.addAll(ControllerFactory.getMainController().getCpSptpdDAOImpl().getMasaPajakSKPDKB(wp.getNPWP()));
							
							Collections.sort(listMasaPajak);
							
							if (listMasaPajak.size() > 0)
								UIController.INSTANCE.loadTMasaPajak(cmbMasaPajak, listMasaPajak.toArray()); //PajakProvider.INSTANCE.getPajak().toArray());
							cmbMasaPajak.select(idx);
							cmbMasaPajakSelected();
						}
					}
				}
			}
		});
		btnGenerateSts.setText("Generate STS");
		btnGenerateSts.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		btnHapus = new Button(compButton, SWT.NONE);
		btnHapus.setEnabled(false);
		btnHapus.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(isDelete()){
					int style = SWT.ICON_QUESTION |SWT.YES | SWT.NO;
					if (selectedID == null)
						MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Belum ada yg yang dipilih");
					else{
						if (MessageDialog.open(6, Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Hapus data (" + txtNoSSPD.getText() + ")?", style)){
							if (ControllerFactory.getMainController().getCpSspdDAOImpl().deleteSspd(selectedID))
							{
								MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Data berhasil dihapus.");
								StringBuffer sb = new StringBuffer();
								sb.append("DELETE " +
										"selectedID:"+selectedID+
										" SSPD:"+txtNoSSPD.getText()+
										" SPTPD:"+txtNoSPTPD.getText()+
										" NPWPD:"+wp.getNPWP()+
										" MasaPajak:"+cmbMasaPajak.getText()+
										" JumlahBayar:"+txtDibayar.getText().getText()+
										" PajakTerhutang:"+txtTotalPajakTerhutang.getText().getText());
								new LogImp().setLog(sb.toString(), userModul.getDisplayName(), userModul.getSubModul().getNamaModul());
		//						getDataSPTPD();
								resetForm();
		//						selectedID = null;
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
		
		Button btnBaru = new Button(compButton, SWT.NONE);
		btnBaru.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				resetForm();
			}
		});
		GridData gd_btnBaru = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnBaru.widthHint = 90;
		btnBaru.setLayoutData(gd_btnBaru);
		btnBaru.setText("Baru");
		btnBaru.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		compPejabat = new Composite(comp, SWT.NONE);
		compPejabat.setLayout(new GridLayout(3, false));
		GridData gd_compPejabat = new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1);
		gd_compPejabat.widthHint = 314;
		compPejabat.setLayoutData(gd_compPejabat);
		compPejabat.setVisible(false);
		
		lblPejabat = new Label(compPejabat, SWT.NONE);
		lblPejabat.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPejabat.setForeground(fontColor);
		lblPejabat.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblPejabat.setText("Pejabat");
		
		cmbPejabat = new Combo(compPejabat, SWT.NONE);
		cmbPejabat.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		cmbPejabat.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		cmbPejabat.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		GridData gd_cmbPejabat = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_cmbPejabat.widthHint = 163;
		cmbPejabat.setLayoutData(gd_cmbPejabat);
		loadPejabat();
		
		btnCetak = new Button(compPejabat, SWT.NONE);
		GridData gd_btnCetak = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnCetak.widthHint = 90;
		btnCetak.setVisible(false);
		btnCetak.setLayoutData(gd_btnCetak);
		btnCetak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnCetak.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				double jumlahPemeriksaan = 0;
				if(isPrint()){
					ReportAppContext.name = "Nota_Antri";
					List<Pajak> listPajak = ControllerFactory.getMainController().getCpPajakDAOImpl().getPajak(Integer.parseInt(txtKodePajak.getText()));
					if(!ReportAppContext.map.isEmpty())
						ReportAppContext.map.clear();
					String NoAntri = "";
					if (txtNoSSPD.getText().contains("SSPD") || txtNoSSPD.getText().equalsIgnoreCase(""))
						NoAntri = tempNota;
					else
						NoAntri = txtNoSSPD.getText();
					
					int slashCount = txtNoSPTPD.getText().length() - txtNoSPTPD.getText().replace("/", "").length();
					ReportAppContext.map.put("NoAntri", NoAntri);
					ReportAppContext.map.put("Nama", txtNamaBadan.getText());
					ReportAppContext.map.put("Alamat", txtAlamat.getText());
					ReportAppContext.map.put("NPWPD", txtKodePajak.getText()+txtNoUrut.getText()+txtKodeKecamatan.getText()+txtKodeKelurahan.getText());
					ReportAppContext.map.put("NamaPajak", listPajak.get(0).getNamaPajak());
					ReportAppContext.map.put("TipeSKP", txtNoSPTPD.getText().split("/")[slashCount - 1]); 
					ReportAppContext.map.put("MasaPajak", cmbMasaPajak.getText());
					ReportAppContext.map.put("NoSKP", txtNoSPTPD.getText());
					ReportAppContext.map.put("KodePajak", ControllerFactory.getMainController().getCpBidangUsahaDAOImpl().getKodeBidUsaha(wp.getIdSubPajak())); //belum tau
					ReportAppContext.map.put("KodeDenda", listPajak.get(0).getKodeDenda());
					ReportAppContext.map.put("TotalPajak", indFormat.format(txtTotalPajakTerhutang.getMoney().getNumber().doubleValue()));
					ReportAppContext.map.put("Total", indFormat.format(txtDibayar.getMoney().getNumber().doubleValue()));
					ReportAppContext.map.put("Sisa", indFormat.format(txtTotalPajakTerhutang.getMoney().getNumber().doubleValue()-txtDibayar.getMoney().getNumber().doubleValue()));
					ReportAppContext.map.put("Terbilang", new ReportModule().getTerbilang(txtDibayar.getMoney().getNumber().doubleValue()));
					ReportAppContext.map.put("TanggalPenyetor", String.format("%02d", dateCetak.getDay())+"-"+String.format("%02d", dateCetak.getMonth() + 1)+"-"+dateCetak.getYear());
					ReportAppContext.map.put("NamaPenyetor", txtNamaPenyetor.getText());
					
					if (txtNoSPTPD.getText().contains("SPTPD") || txtNoSPTPD.getText().contains("/SKPD/")){
						for(TableItem ti : table.getItems()){
							if(ti.getText(0).equalsIgnoreCase("Pokok Pajak")){
								ReportAppContext.map.put("PokokPajak", ti.getText(1));
							}else if(ti.getText(0).equalsIgnoreCase("Denda SSPD")){
								ReportAppContext.map.put("Denda", ti.getText(1));
							}else if(ti.getText(0).equalsIgnoreCase("Denda Tambahan")){
								ReportAppContext.map.put("DendaTambahan", ti.getText(1));
							}
						}
						ReportAppContext.map.put("Lokasi", txtLokasi.getText());
						ReportAction.start("NotaAntriSSPD.rptdesign");
					}
					else{
						for(TableItem ti : table.getItems()){
							if(ti.getText(0).equalsIgnoreCase("Pokok Pajak")){
								ReportAppContext.map.put("PokokPajak", ti.getText(1));
							}else if(ti.getText(0).equalsIgnoreCase("Denda")){
								ReportAppContext.map.put("Denda", ti.getText(1));
							}else if(ti.getText(0).equalsIgnoreCase("Denda Tambahan")){
								ReportAppContext.map.put("KenaikanPajak", ti.getText(1));
							}else if(ti.getText(0).equalsIgnoreCase("Denda Terlambat SKPDKB")){
								ReportAppContext.map.put("DendaTerlambatSKPDKB", ti.getText(1));
							}else if(ti.getText(0).equalsIgnoreCase("Denda Terlambat Sebelumnya")){
								ReportAppContext.map.put("DendaTerlambatSebelumnya", ti.getText(1));
							}else if(ti.getText(0).equalsIgnoreCase("Denda SSPD")){
								ReportAppContext.map.put("DendaSSPD", ti.getText(1));
							}
						}
						for(TableItem ti : tblCicilan.getItems()){
							if(ti.getText(0).equalsIgnoreCase("Denda SSPD")){
								ReportAppContext.map.put("DendaSSPD", ti.getText(1));
							}
						}
						try {
							jumlahPemeriksaan = indFormat.parse(table.getItem(0).getText(1)).doubleValue() + indFormat.parse(table.getItem(1).getText(1)).doubleValue() + indFormat.parse(table.getItem(2).getText(1)).doubleValue() + indFormat.parse(table.getItem(3).getText(1)).doubleValue();
						} catch (ParseException e1) {
							e1.printStackTrace();
						}
						ReportAppContext.map.put("JumlahPemeriksaan", indFormat.format(jumlahPemeriksaan));
						ReportAppContext.map.put("JumlahBayarSebelum", indFormat.format(txtTotalAngsur.getMoney().getNumber().doubleValue()));
						if (btnAngsuran.getSelection()){
//							DecimalFormat df = new DecimalFormat("#.##");
//							df.setMaximumFractionDigits(2);
//							df.setRoundingMode(RoundingMode.HALF_EVEN);
							ReportAppContext.map.put("JumlahTerhutang", indFormat.format(round(txtSisa.getMoney().getNumber().doubleValue()) - txtDibayar.getMoney().getNumber().doubleValue()));
						}
							
						else{
							ReportAppContext.map.put("JumlahTerhutang", indFormat.format(txtTotalPajakTerhutang.getMoney().getNumber().doubleValue() - txtDibayar.getMoney().getNumber().doubleValue()));
							ReportAppContext.map.put("DendaTerlambatSebelumnya", "Rp0,00");
						}
						
						ReportAction.start("NotaAntriSSPD_SKPDKB.rptdesign");
					}
				}else
					MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Anda tidak memiliki hak untuk mencetak.");
			}
		});
		btnCetak.setText("Cetak");
		
		btnCetakSts = new Button(comp, SWT.NONE);
		btnCetakSts.addSelectionListener(new SelectionAdapter() {
			@SuppressWarnings("deprecation")
			@Override
			public void widgetSelected(SelectionEvent e) {
				Integer cicilSTS = 0;
				if (cmbAngsuran.getSelectionIndex()<0)
					cicilSTS = tableAngsuran.getItemCount();
				else
					cicilSTS = Integer.valueOf(cmbAngsuran.getText())-1;
				if(isPrint()){
					Timestamp tglValid = new Timestamp(dtValid.getYear()-1900, dtValid.getMonth(), dtValid.getDay(), 23, 59, 59, 00);
					if (dateNow.before(tglValid)){
						ReportAppContext.name = "NotaSTS";
//						List<Pajak> listPajak = ControllerFactory.getMainController().getCpPajakDAOImpl().getPajak(Integer.parseInt(txtKodePajak.getText()));
						if(!ReportAppContext.map.isEmpty())
							ReportAppContext.map.clear();
						String nilai = "";
						String nilaiDenda = "";
						Double pokokAngsuran = 0.0;
						Double dendaAngsuran = 0.0;
						Date tglSetor = new Date(dateNow.getYear()+1900, dateNow.getMonth(), dateNow.getDate()); //new Date(datePembayaran.getYear() - 1900, datePembayaran.getMonth(), datePembayaran.getDay());
						Date tglTempo = new Date(dateJatuhTempo.getYear(), dateJatuhTempo.getMonth(), dateJatuhTempo.getDay());
						String tipeSKP = "";
//						String noSTS = txtNoSTS.getText().
//						SimpleDateFormat sdf = new SimpleDateFormat("dd-yyyy");
//						String jatuhTempo = sdf.format(dateJatuhTempo).replace("-", " " + UIController.INSTANCE.formatMonth(dateJatuhTempo.getMonth() + 1, ind) + " ");
//						String stsValid = sdf.format(dtValid).replace("-", " " + UIController.INSTANCE.formatMonth(dtValid.getMonth() + 1, ind) + " ");
						
						if (btnAngsuran.getSelection()){
							int idMohon = ControllerFactory.getMainController().getCpMohonDAOImpl().checkMohon(wp.getNPWP(), txtNoSPTPD.getText());
							List<MohonDetail> md = ControllerFactory.getMainController().getCpMohonDetailDAOImpl().getDaftarAngsuran(idMohon);
							pokokAngsuran = roundUP(md.get(cicilSTS).getAngsuranPokok());
							nilai = indFormat.format(pokokAngsuran);
							dendaAngsuran = roundUP(md.get(cicilSTS).getBiayaAdministrasi()) + roundUP(md.get(cicilSTS).getDendaSkpdkb());
							nilaiDenda = indFormat.format(dendaAngsuran);
							tipeSKP = "SKPDKB";
						}else{
							if (txtNoSPTPD.getText().contains("SPTPD")){
								nilai = indFormat.format(pokokPajak); //table.getItem(0).getText(2); //indFormat.format(txtPajakTerutang.getMoney().getNumber().doubleValue()).substring(2).replace(".", "").replace(",", ".");
								nilaiDenda = indFormat.format(dendaSSPD);
								tipeSKP = "SPTPD";
							}else if (txtNoSPTPD.getText().contains("SKPD/")){
								nilai = indFormat.format(pokokPajak); 
								nilaiDenda = indFormat.format(dendaSSPD);
								tipeSKP = "SKPD";
							}else if (txtNoSPTPD.getText().contains("SKPDKB")){
								nilai = indFormat.format(pokokPajak);
								nilaiDenda = indFormat.format(denda);
								tipeSKP = "SKPDKB";
							}
							else if (txtNoSPTPD.getText().contains("SKPDKBT")){
								nilai = indFormat.format(pokokPajak);
								nilaiDenda = indFormat.format(denda);
								tipeSKP = "SKPDKBT";
							}
							
						}
						Integer lamaBunga = 0;
						ReportAppContext.map.put("NPWPD", txtKodePajak.getText()+txtNoUrut.getText()+txtKodeKecamatan.getText()+txtKodeKelurahan.getText());
						ReportAppContext.map.put("NoSts", getSTSFormat(txtNoSTS.getText()));
						ReportAppContext.map.put("NamaBadan", wp.getNamaBadan());
						ReportAppContext.map.put("NamaPemilik", wp.getNamaPemilik());
						ReportAppContext.map.put("NoSKP", txtNoSPTPD.getText());
						ReportAppContext.map.put("MasaPajak", cmbMasaPajak.getText());
						ReportAppContext.map.put("JatuhTempo", dateJatuhTempo.getDay()+" "+UIController.INSTANCE.formatMonth(dateJatuhTempo.getMonth() + 1, ind)+" "+dateJatuhTempo.getYear());
						ReportAppContext.map.put("STSValid", dtValid.getDay()+" "+UIController.INSTANCE.formatMonth(dtValid.getMonth() + 1, ind)+" "+dtValid.getYear());
						ReportAppContext.map.put("TipeSKP", tipeSKP);
						ReportAppContext.map.put("JumlahBayar", indFormat.format(txtDibayar.getMoney().getNumber().doubleValue()));
						ReportAppContext.map.put("Terbilang", new ReportModule().getTerbilang(txtDibayar.getMoney().getNumber().doubleValue()));
						
						if (tipeSKP.equalsIgnoreCase("SPTPD") || tipeSKP.equalsIgnoreCase("SKPD")){
							lamaBunga = ((tglSetor.getYear() - tglTempo.getYear()) * 12) + (tglSetor.getMonth() - tglTempo.getMonth());
							if (lamaBunga == 0)
								lamaBunga = ((tglSetor.getYear() - tglTempo.getYear()) * 12) + (tglSetor.getMonth() - tglTempo.getMonth())+(tglSetor.getDate() > tglTempo.getDate() ? 1 : 0);
							lamaBunga = lamaBunga < 0 ? 0:lamaBunga;
							lamaBunga = lamaBunga > 24 ? 24 : lamaBunga;
							ReportAppContext.map.put("Bulan", lamaBunga.toString());
							ReportAppContext.map.put("Pokok", nilai);
							ReportAppContext.map.put("Denda", nilaiDenda);
							ReportAppContext.map.put("TotalDenda", nilaiDenda);
							ReportAction.start("NotaSTS.rptdesign");
						}else{
							lamaBunga = ((tglSetor.getYear() - tglTempo.getYear()) * 12) + (tglSetor.getMonth() - tglTempo.getMonth()) + (tglSetor.getDate() > tglTempo.getDate() ? 1 : 0);
							lamaBunga = lamaBunga < 0 ? 0:lamaBunga;
							lamaBunga = lamaBunga > 24 ? 24 : lamaBunga;
							ReportAppContext.map.put("Pokok", nilai);
							ReportAppContext.map.put("Denda", nilaiDenda);
							ReportAppContext.map.put("TotalDenda", nilaiDenda);
							ReportAppContext.map.put("Bulan", lamaBunga.toString());
							ReportAppContext.map.put("DendaSSPD", indFormat.format(dendaSSPD));
							ReportAppContext.map.put("TanggalSKP", dateTerbit.getDay()+" "+UIController.INSTANCE.formatMonth(dateTerbit.getMonth() + 1, ind)+" "+dateTerbit.getYear());
							if (btnAngsuran.getSelection()){
								ReportAppContext.map.put("PokokKenaikan", indFormat.format(0.0));
								String cicil = (cicilSTS+1) + " (" + new ReportModule().getTerbilang(Double.valueOf(cicilSTS+1)) + ")";
								ReportAppContext.map.put("CicilanKe", cicil.replace(" Rupiah.", ""));
								ReportAppContext.map.put("Terbilang", new ReportModule().getTerbilang(pokokAngsuran+dendaAngsuran+dendaSSPD));
								ReportAppContext.map.put("JumlahBayar", indFormat.format(pokokAngsuran+dendaAngsuran+dendaSSPD));
								ReportAppContext.map.put("Cicilan", "1");
							}else{
								ReportAppContext.map.put("PokokKenaikan", indFormat.format(dendaTambahan));
								ReportAppContext.map.put("CicilanKe", "0");
								ReportAppContext.map.put("Cicilan", "0");
							}
							ReportAction.start("NotaSTSSKPDKB.rptdesign");
						}
					}else
						MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "STS invalid. Klik tombol Generate STS untuk memperbarui");
				}else
					MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Anda tidak memiliki hak untuk mencetak.");
			}
		});
		btnCetakSts.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnCetakSts.setText("Cetak STS");
		btnCetakSts.setVisible(false);
		
		btnCetakStsDenda = new Button(comp, SWT.NONE);
		btnCetakStsDenda.addSelectionListener(new SelectionAdapter() {
			@SuppressWarnings("deprecation")
			@Override
			public void widgetSelected(SelectionEvent e) {
				Timestamp tglValid = new Timestamp(dtValid.getYear()-1900, dtValid.getMonth(), dtValid.getDay(), 23, 59, 59, 00);
				if (dateNow.before(tglValid)){
					if(isPrint()){
						ReportAppContext.name = "NotaSTS";
//						List<Pajak> listPajak = ControllerFactory.getMainController().getCpPajakDAOImpl().getPajak(Integer.parseInt(txtKodePajak.getText()));
						if(!ReportAppContext.map.isEmpty())
							ReportAppContext.map.clear();
						String nilai = "";
						String nilaiDenda = "";
						String totalDenda = "";
						Double pokokAngsuran = 0.0;
						Double dendaAngsuran = 0.0;
						Date tglSetor = new Date(dateNow.getYear()+1900, dateNow.getMonth(), dateNow.getDate()); //new Date(datePembayaran.getYear() - 1900, datePembayaran.getMonth(), datePembayaran.getDay());
						Date tglTempo = new Date(dateJatuhTempo.getYear(), dateJatuhTempo.getMonth(), dateJatuhTempo.getDay());
						String tipeSKP = "";
						if (btnAngsuran.getSelection()){
							/*int idMohon = ControllerFactory.getMainController().getCpMohonDAOImpl().checkMohon(wp.getNPWP(), txtNoSPTPD.getText());
							List<MohonDetail> md = ControllerFactory.getMainController().getCpMohonDetailDAOImpl().getDaftarAngsuran(idMohon);
							pokokAngsuran = md.get(tableAngsuran.getItemCount()).getAngsuranPokok();
							nilai = indFormat.format(0.0);
							dendaAngsuran = md.get(tableAngsuran.getItemCount()).getBiayaAdministrasi() + md.get(tableAngsuran.getItemCount()).getDendaSkpdkb();
							nilaiDenda = indFormat.format(dendaAngsuran);
							tipeSKP = "SKPDKB";*/
						}else{
							if (txtNoSPTPD.getText().contains("SPTPD")){
								nilai = indFormat.format(0.0); //table.getItem(0).getText(2); //indFormat.format(txtPajakTerutang.getMoney().getNumber().doubleValue()).substring(2).replace(".", "").replace(",", ".");
								nilaiDenda = indFormat.format(pokokPajak);
								totalDenda = indFormat.format(dendaSSPD);
								tipeSKP = "SPTPD";
							}else if (txtNoSPTPD.getText().contains("SKPD/")){
								nilai = indFormat.format(0.0); 
								nilaiDenda = indFormat.format(pokokPajak);
								totalDenda = indFormat.format(dendaSSPD);
								tipeSKP = "SKPD";
							}else if (txtNoSPTPD.getText().contains("SKPDKB")){
								nilai = indFormat.format(0.0);
								nilaiDenda = indFormat.format(pokokPajak);
								totalDenda = indFormat.format(dendaSSPD);
								tipeSKP = "SKPDKB";
							}
						}
						Integer lamaBunga = 0;
						
						ReportAppContext.map.put("NPWPD", txtKodePajak.getText()+txtNoUrut.getText()+txtKodeKecamatan.getText()+txtKodeKelurahan.getText());
						ReportAppContext.map.put("NoSts", getSTSFormat(txtNoSTS.getText().substring(txtNoSTS.getText().indexOf("-")+1)));
						ReportAppContext.map.put("NamaBadan", wp.getNamaBadan());
						ReportAppContext.map.put("NamaPemilik", wp.getNamaPemilik());
						ReportAppContext.map.put("NoSKP", txtNoSPTPD.getText());
						ReportAppContext.map.put("MasaPajak", cmbMasaPajak.getText());
						ReportAppContext.map.put("JatuhTempo", dateJatuhTempo.getDay()+" "+UIController.INSTANCE.formatMonth(dateJatuhTempo.getMonth() + 1, ind)+" "+dateJatuhTempo.getYear());
						ReportAppContext.map.put("STSValid", dtValid.getDay()+" "+UIController.INSTANCE.formatMonth(dtValid.getMonth() + 1, ind)+" "+dtValid.getYear());
						ReportAppContext.map.put("TipeSKP", tipeSKP);
						ReportAppContext.map.put("JumlahBayar", indFormat.format(dendaSSPD));
						ReportAppContext.map.put("Terbilang", new ReportModule().getTerbilang(dendaSSPD));
						
						if (tipeSKP.equalsIgnoreCase("SPTPD") || tipeSKP.equalsIgnoreCase("SKPD")){
							lamaBunga = ((tglSetor.getYear() - tglTempo.getYear()) * 12) + (tglSetor.getMonth() - tglTempo.getMonth());
							lamaBunga = lamaBunga < 0 ? 0:lamaBunga;
							lamaBunga = lamaBunga > 24 ? 24 : lamaBunga;
							ReportAppContext.map.put("Bulan", lamaBunga.toString());
							ReportAppContext.map.put("Pokok", nilai);
							ReportAppContext.map.put("Denda", nilaiDenda);
							ReportAppContext.map.put("TotalDenda", totalDenda);
							ReportAction.start("NotaSTSDenda.rptdesign");
						}else{
							lamaBunga = ((tglSetor.getYear() - tglTempo.getYear()) * 12) + (tglSetor.getMonth() - tglTempo.getMonth()) + (tglSetor.getDate() > tglTempo.getDate() ? 1 : 0);
							lamaBunga = lamaBunga < 0 ? 0:lamaBunga;
							lamaBunga = lamaBunga > 24 ? 24 : lamaBunga;
							ReportAppContext.map.put("Pokok", nilai);
							ReportAppContext.map.put("Denda", nilaiDenda);
							ReportAppContext.map.put("TotalDenda", totalDenda);
							ReportAppContext.map.put("Bulan", lamaBunga.toString());
							ReportAppContext.map.put("DendaSSPD", indFormat.format(dendaSSPD));
							ReportAppContext.map.put("TanggalSKP", dateTerbit.getDay()+" "+UIController.INSTANCE.formatMonth(dateTerbit.getMonth() + 1, ind)+" "+dateTerbit.getYear());
							if (btnAngsuran.getSelection()){
								ReportAppContext.map.put("PokokKenaikan", indFormat.format(0.0));
								String cicil = (tableAngsuran.getItemCount()+1) + " (" + new ReportModule().getTerbilang(Double.valueOf(tableAngsuran.getItemCount()+1)) + ")";
								ReportAppContext.map.put("CicilanKe", cicil.replace(" Rupiah.", ""));
								ReportAppContext.map.put("Terbilang", new ReportModule().getTerbilang(pokokAngsuran+dendaAngsuran+dendaSSPD));
								ReportAppContext.map.put("JumlahBayar", indFormat.format(pokokAngsuran+dendaAngsuran+dendaSSPD));
								ReportAppContext.map.put("Cicilan", "1");
							}else{
								ReportAppContext.map.put("PokokKenaikan", indFormat.format(dendaTambahan));
								ReportAppContext.map.put("CicilanKe", "0");
								ReportAppContext.map.put("Cicilan", "0");
							}
							ReportAction.start("NotaSTSSKPDKBDenda.rptdesign");
						}
					}else
						MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Anda tidak memiliki hak untuk mencetak.");
				}else
					MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "STS invalid. Klik tombol Generate STS untuk memperbarui");
			}
		});
		btnCetakStsDenda.setText("Cetak STS Denda");
		btnCetakStsDenda.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnCetakStsDenda.setVisible(false);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		
		btnNewButton = new Button(comp, SWT.NONE);
		btnNewButton.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 4, 1));
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				saveShortcut();
			}
		});
		btnNewButton.setText("New Button");
		if (userModul.getIdUser() == 1 || userModul.getIdUser() == 19 || userModul.getIdUser() == 23)
			btnNewButton.setVisible(true);
		else
			btnNewButton.setVisible(false);
		
		btnBayar = new Button(comp, SWT.NONE);
		btnBayar.setEnabled(false);
		btnBayar.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnBayar.setText("Bayar");
		btnBayar.setVisible(false);
		btnBayar.addSelectionListener(new SelectionAdapter() {
			@SuppressWarnings("deprecation")
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(isSave()){
					if(isValidate()){
	//					String noSSPD = "";
						String notaBayar = tempNota;
						dateNow = (java.sql.Timestamp)ControllerFactory.getMainController().getCpWajibPajakDAOImpl().getDateNow();
						SimpleDateFormat sdf = new SimpleDateFormat("ddMMyy");
						java.util.Date tglSSPD = new java.util.Date(datePembayaran.getYear() - 1900, datePembayaran.getMonth(), datePembayaran.getDay(), dateNow.getHours(), dateNow.getMinutes(), dateNow.getSeconds());
						java.util.Date tglCetak = new java.util.Date(dateCetak.getYear() - 1900, dateCetak.getMonth(), dateCetak.getDay(), dateNow.getHours(), dateNow.getMinutes(), dateNow.getSeconds());
						SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
						Sspd sspd = new Sspd();
						sspd.setIdSspd(selectedID);
						if (selectedID == null){
							notaBayar = ControllerFactory.getMainController().getCpSspdDAOImpl().getNotaBayar(sdf.format(dateNow));
							cicilanKe = tableAngsuran.getItemCount() + 1; 
							//noSSPD = ControllerFactory.getMainController().getCpSspdDAOImpl().getNoSSPD(String.valueOf(datePembayaran.getYear()));
						}
						else{
							if (txtNoSSPD.getText().substring(txtNoSSPD.getText().length() - 6).equalsIgnoreCase(sdf.format(dateNow)))
								notaBayar = txtNoSSPD.getText();
							if (btnAngsuran.getSelection())
								cicilanKe = tableAngsuran.getSelectionIndex()+1;
	//						cicilanKe = tableAngsuran.getItemCount() 
//							if (! notaBayar.substring(notaBayar.length() - 6).equalsIgnoreCase(sdf.format(dateNow)))
//							else
//								notaBayar = ""; //ControllerFactory.getMainController().getCpSspdDAOImpl().getNotaBayar(sdf.format(dateNow));
						}
						String datesspd = dateFormat.format(tglSSPD);
						sspd.setTglSspd(java.sql.Timestamp.valueOf(datesspd));
						String datecetak = dateFormat.format(tglCetak);
						sspd.setTglCetak(java.sql.Timestamp.valueOf(datecetak));
	//					sspd.setTglSspd(new Date(datePembayaran.getYear() - 1900, datePembayaran.getMonth(), datePembayaran.getDay()));
	//					sspd.setTglCetak(new Date(dateCetak.getYear() - 1900, dateCetak.getMonth(), dateCetak.getDay()));
						sspd.setNpwpd(wp.getNPWP());
						sspd.setBulanSkp(bulanSKP);
						sspd.setNoSkp(txtNoSPTPD.getText());
						if (txtNoSPTPD.getText().contains("SPTPD"))
							sspd.setDenda(dendaSSPD + dendaTambahan);
						else
							sspd.setDenda(dendaSSPD);
//						DecimalFormat df = new DecimalFormat("#.##");
//						df.setMaximumFractionDigits(2);
//						df.setRoundingMode(RoundingMode.HALF_EVEN);
						//totalPajakTerutang = Double.parseDouble(df.format(totalPajakTerutang).replace(',', '.'));
						
						sspd.setJumlahBayar(round(txtDibayar.getMoney().getNumber().doubleValue()));
						if (btnTunai.getSelection())
							caraBayar = "Tunai";
						else if (btnGiro.getSelection())
							caraBayar = "Giro";
						else if (btnBank.getSelection())
							caraBayar = "Bank";
						sspd.setCaraBayar(caraBayar);		
						sspd.setNamaPenyetor(txtNamaPenyetor.getText());
						sspd.setIdSubPajak(idSubPajak);
						sspd.setIdSPTPD(idSPTPD);
						sspd.setIdPeriksa(idPeriksa);
						if (btnTepatWaktu.getSelection()) {
							jenisBayar = 0;
							cicilanKe = 0;
							// di set saat dibayar di listDaftarAntrian
							if (txtDibayar.getMoney().getNumber().doubleValue() >= txtTotalPajakTerhutang.getMoney().getNumber().doubleValue()) {
								sspd.setLunas(lblLunas.getVisible());
								sspd.setDendaLunas(true);
							}
							else if (txtDibayar.getMoney().getNumber().doubleValue() >= pokokPajak){
								sspd.setLunas(lblLunas.getVisible());
								sspd.setDendaLunas(false);
							}
							else {
	//							sspd.setLunas(false);
								sspd.setDendaLunas(false);
							}
								
						}
						else {
							jenisBayar = 2;
							sspd.setLunas(lblLunas.getVisible());
							try {
								if (indFormat.parse(tblCicilan.getItem(0).getText(1)).doubleValue() + indFormat.parse(tblCicilan.getItem(1).getText(1)).doubleValue() - txtDibayar.getMoney().getNumber().doubleValue() <= 1){
									if (indFormat.parse(tblCicilan.getItem(0).getText(1)).doubleValue() + indFormat.parse(tblCicilan.getItem(1).getText(1)).doubleValue() - txtDibayar.getMoney().getNumber().doubleValue() >= -1 &&
										indFormat.parse(tblCicilan.getItem(0).getText(1)).doubleValue() + indFormat.parse(tblCicilan.getItem(1).getText(1)).doubleValue() - txtDibayar.getMoney().getNumber().doubleValue() <= 1){
										sspd.setDendaLunas(true);
									}
									else{
										sspd.setDendaLunas(true);
										double sisaDenda = txtDibayar.getMoney().getNumber().doubleValue() - (indFormat.parse(tblCicilan.getItem(0).getText(1)).doubleValue() + indFormat.parse(tblCicilan.getItem(1).getText(1)).doubleValue());
										for (int i=0;i<tableAngsuran.getItemCount();i++){
											if (sisaDenda - updDenda[i] >= 0){
												ControllerFactory.getMainController().getCpSspdDAOImpl().updateDendaSebelumnya(IDSSPDAngsur[i]);
												sisaDenda -= updDenda[i];
											}
										}
									}
								}
								else if (indFormat.parse(tblCicilan.getItem(0).getText(1)).doubleValue() - txtDibayar.getMoney().getNumber().doubleValue() <= 1){
									sspd.setDendaLunas(false);
								}
								
							} catch (ParseException e1) {
								e1.printStackTrace();
							}
							
	//						if (angsuranTerakhir == cicilanKe)
						}
						sspd.setJenisBayar(jenisBayar);
						sspd.setCicilanKe(cicilanKe);
						sspd.setNoSspd("");
						sspd.setNotaBayar(notaBayar);
						
//						if (angsuranTerakhir == cicilanKe){
//							if (txtTotalPajakTerhutang.getMoney().getNumber().doubleValue() - (txtTotalAngsur.getMoney().getNumber().doubleValue() + txtDibayar.getMoney().getNumber().doubleValue()) < 0.01){
								if(ControllerFactory.getMainController().getCpSspdDAOImpl().saveSspd(sspd)){
									MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Data berhasil disimpan.");
									if (selectedID == null) {
										selectedID = ControllerFactory.getMainController().getCpSspdDAOImpl().getLastId(wp.getNPWP());
										txtNoSSPD.setText(notaBayar);
										lblNoSspd.setText("Nota Bayar");
									}
									lblLunas.setVisible(false);
									compPejabat.setVisible(true);
									loadPejabat();
									if (btnAngsuran.getSelection()){
										loadAngsuran();
										tblCicilan.remove(tblCicilan.getItemCount() - 1);
										table.remove(table.getItemCount() - 1);
										tableAngsuran.select(tableAngsuran.getItemCount() - 1);
										loadPerhitunganAngsuran();
									}
									StringBuffer sb = new StringBuffer();
									sb.append("SAVE " +
											"SSPD:"+txtNoSSPD.getText()+
											" SPTPD:"+txtNoSPTPD.getText()+
											" NPWPD:"+wp.getNPWP()+
											" TanggalSSPD:"+tglSSPD+
											" MasaPajak:"+cmbMasaPajak.getText()+
											" JumlahBayar:"+txtDibayar.getText().getText()+
											" PajakTerhutang:"+txtTotalPajakTerhutang.getText().getText());
									new LogImp().setLog(sb.toString(), userModul.getDisplayName(), userModul.getSubModul().getNamaModul());
								}else{
									MessageDialog.openError(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Error pada Database. Silahkan hubungi ADMIN.");
								}
//							}
//							else{
//								MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "Dispenda Kota Medan", "Pembayaran tidak dapat disimpan karena masih ada sisa angsuran yang belum lunas");
//							}
//						}
					}else
						MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", pesanValidate);
				}else
					MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Anda tidak memiliki hak untuk menyimpan atau merubah data.");
			}
		});
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		
		
		btnHapusSts = new Button(comp, SWT.NONE);
		btnHapusSts.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 4, 1));
		btnHapusSts.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				boolean sukses = ControllerFactory.getMainController().getCpSspdDAOImpl().hapusSTS(txtNoSPTPD.getText(),txtNoSTS.getText());
				if (sukses){
					txtNoSTS.setText("");
					MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Penghapusan STS berhasil.");
				}
			}
		});
		btnHapusSts.setText("Hapus STS");
		
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		
		btnDendaManual = new Button(comp, SWT.CHECK);
	
		txtDendaManual = new Text(comp, SWT.BORDER);
		txtDendaManual.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		txtDendaManual.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtDendaManual.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
				
		if (userModul.getIdUser() == 1){
			btnDendaManual.setVisible(true);
			txtDendaManual.setVisible(true);
		}
		else{
			btnDendaManual.setVisible(false);
			txtDendaManual.setVisible(false);
		}
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		comp.setTabList(new Control[]{txtNoUrut, cmbMasaPajak, txtDibayar, txtNamaPenyetor, compButton, compPejabat});
		
		scrolledComposite.setContent(comp);
		scrolledComposite.setMinSize(comp.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}
	
	public void setFocus() {
		txtNoUrut.setFocus();
	}
	
	@SuppressWarnings("deprecation")
	public boolean isValidate(){
		cicilanKe = tableAngsuran.getItemCount() + 1;
		Date tglSKP = new Date(datePembayaran.getYear() - 1900, datePembayaran.getMonth(), datePembayaran.getDay());
		Date tgl_Cetak = new Date(dateCetak.getYear() - 1900, dateCetak.getMonth(), dateCetak.getDay());
		pesanValidate = "";
		if(txtKodePajak.getText().equalsIgnoreCase("") ||
				txtNamaBadan.getText().equalsIgnoreCase("") ||
				txtAlamat.getText().equalsIgnoreCase("") ||
				txtNoSPTPD.getText().equalsIgnoreCase("") ||
				txtTotalPajakTerhutang.getMoney().toString().equalsIgnoreCase("") ||
				txtDibayar.getMoney().toString().equalsIgnoreCase("") ||
				txtNamaPenyetor.getText().equalsIgnoreCase("") ||
				! (btnTepatWaktu.getSelection() || btnAngsuran.getSelection()) ||
				! (btnTunai.getSelection() || btnGiro.getSelection() || btnBank.getSelection())
				){
			pesanValidate = "Silahkan periksa kembali.";
			return false;
		}
		else if (btnGiro.getSelection() && (tgl_Cetak.compareTo(tglSKP) < 0)){
			pesanValidate = "Periksa Tanggal Bayar dan Tanggal Cetak";
			return false;
		}
		else if (angsuranTerakhir == cicilanKe && (txtTotalPajakTerhutang.getMoney().getNumber().doubleValue() - (txtTotalAngsur.getMoney().getNumber().doubleValue() + txtDibayar.getMoney().getNumber().doubleValue()) > 0.01)){
			pesanValidate = "Pembayaran tidak dapat disimpan karena masih ada sisa angsuran yang belum lunas";
			return false;
		}
		/*Cek masa pajak sebelumnya udah bayar apa belum
		 * else if {
			
		}*/
		else
			return true;
	}
	
	private void getDataWP()
	{
		Integer npwp = 0;
		try{
			npwp = Integer.parseInt(txtNoUrut.getText().trim());
		}catch(Exception ex){
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "NPWPD tidak valid. Silahkan periksa kembali");
			txtNoUrut.forceFocus();
		}
		wp = ControllerFactory.getMainController().getCpWajibPajakDAOImpl().getNPWPD(npwp);
		if (wp != null)
		{
			resetForm();
			TableColumn colPajak = new TableColumn(table, SWT.NONE,0);
			colPajak.setText("Pajak");
			colPajak.setWidth(250);
			TableColumn colNilai = new TableColumn(table, SWT.NONE,1);
			colNilai.setText("Nilai");
			colNilai.setWidth(250);
			txtKodePajak.setText(wp.getNPWP().substring(0, 1));
			txtNoUrut.setText(wp.getNPWP().substring(1, 8));
			txtKodeKecamatan.setText(wp.getNPWP().substring(8, 10));
			txtKodeKelurahan.setText(wp.getNPWP().substring(10, 12));
			txtNamaBadan.setText(wp.getNamaPemilik() + " / " + wp.getNamaBadan());
			txtAlamat.setText(wp.getAlabadJalan());
			idSubPajak = wp.getIdSubPajak();
			tarif = wp.getTarif();
			txtNoSPTPD.setText("");
			txtNoSSPD.setText("");	
		}else
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "NPWPD tidak ada. Silahkan periksa kembali");
	}
	
	public String formatMonth(int month, Locale locale) {
	    DateFormatSymbols symbols = new DateFormatSymbols(locale);
	    String[] monthNames = symbols.getMonths();
	    return monthNames[month - 1];
	}
	
	@SuppressWarnings("deprecation")
	private void setTanggalJatuhTempo(Integer jenisBayar, Integer index)
	{
		dateJatuhTempo.setVisible(true);
		Calendar cal = Calendar.getInstance();
//		String tipeSKP[] = new String[4];
//		tipeSKP = txtNoSPTPD.getText().split("/");
		Date dispensasiTempo = ControllerFactory.getMainController().getCpSspdDAOImpl().getDispensasiTempo(txtNoSPTPD.getText());
		if (dispensasiTempo == null){
			if (jenisBayar == 0){
				if (txtKodePajak.getText().equalsIgnoreCase("7")){
					//spt = ControllerFactory.getMainController().getCpSptpdDAOImpl().getSPTPD(wp.getNPWP(), txtNoSPTPD.getText());
					Date tglSKP = listMasaPajak.get(index).getTanggalSPTPD();
					cal.set(Calendar.DAY_OF_MONTH, tglSKP.getDate());
					cal.set(Calendar.MONTH, tglSKP.getMonth());
					cal.set(Calendar.YEAR, tglSKP.getYear() + 1900);
					cal.add(Calendar.MONTH, 1);
					dateJatuhTempo.setDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
				}
				else if (txtNoSPTPD.getText().contains("SPTPD/")){
					//spt = ControllerFactory.getMainController().getCpSptpdDAOImpl().getSPTPD(wp.getNPWP(), txtNoSPTPD.getText());
					Date tglSKP = listMasaPajak.get(index).getMasaPajakSampai();
					cal.set(Calendar.DAY_OF_MONTH, tglSKP.getDate());
					cal.set(Calendar.MONTH, tglSKP.getMonth());
					cal.set(Calendar.YEAR, tglSKP.getYear() + 1900);
					cal.add(Calendar.MONTH, 1);
					dateJatuhTempo.setDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.getActualMaximum(Calendar.DAY_OF_MONTH));
					
				}
				else if (txtNoSPTPD.getText().contains("SKPD/")){
					//spt = ControllerFactory.getMainController().getCpSptpdDAOImpl().getSPTPD(wp.getNPWP(), txtNoSPTPD.getText());
					if (listMasaPajak.get(index).getMasaPajakDari().getMonth() == listMasaPajak.get(index).getMasaPajakSampai().getMonth() || (txtKodePajak.getText().equalsIgnoreCase("2"))){
						Date tglSKP = listMasaPajak.get(index).getMasaPajakSampai();
						cal.set(Calendar.DAY_OF_MONTH, tglSKP.getDate());
						cal.set(Calendar.MONTH, tglSKP.getMonth());
						cal.set(Calendar.YEAR, tglSKP.getYear() + 1900);
						cal.add(Calendar.MONTH, 1);
						dateJatuhTempo.setDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.getActualMaximum(Calendar.DAY_OF_MONTH));
					}
					else{
						dateJatuhTempo.setDate(0, 0, 0);
						dateJatuhTempo.setVisible(false);
					}
				}
				else if (txtNoSPTPD.getText().contains("SKPDKB/") || txtNoSPTPD.getText().contains("SKPDKBT/")){
					Date tglSKP = listMasaPajak.get(index).getTanggalSPTPD();
					cal.set(Calendar.DAY_OF_MONTH, tglSKP.getDate());
					cal.set(Calendar.MONTH, tglSKP.getMonth());
					cal.set(Calendar.YEAR, tglSKP.getYear() + 1900);
					cal.add(Calendar.MONTH, 1);
					dateJatuhTempo.setDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
				}
				
				//Exception untuk RM. Bintang SPT bulan Maret dan SKPDKB Nov '16 - Feb '17
				//Jatuh tempo akhir bulan 4 dimundurin ke awal bulan 5
				if (txtNoSPTPD.getText().equalsIgnoreCase("1928/0207/SPTPD/2017") || txtNoSPTPD.getText().equalsIgnoreCase("165/0207/SKPDKB/2017")){
					Date tgl = new Date(dateJatuhTempo.getYear(), dateJatuhTempo.getMonth(), dateJatuhTempo.getDay());
					cal.set(Calendar.DAY_OF_MONTH, tgl.getDate());
					cal.set(Calendar.MONTH, tgl.getMonth());
					cal.set(Calendar.YEAR, tgl.getYear() + 1900);
					cal.add(Calendar.DATE, 3);
					dateJatuhTempo.setDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
				}
			}else{
				//dateJatuhTempo.setDate(0, 0, 0);
			}
		}else{
			dateJatuhTempo.setDate(dispensasiTempo.getYear()+1900, dispensasiTempo.getMonth(), dispensasiTempo.getDate());
		}
		
		//set tgl jatuh tempo utk 17-18 Juli 2015 (Hari raya) menjadi 22 Juli 2015
		//Date jthTempo = new Date(dateJatuhTempo.getYear() - 1900, dateJatuhTempo.getMonth(), dateJatuhTempo.getDay());
		//Date bayar = new Date(datePembayaran.getYear() - 1900, datePembayaran.getMonth(), datePembayaran.getDay());
		//Date tglAwal = new Date(117,05,22);
		//Date tglAkhir = new Date(117,06,04);
		//if ((jthTempo.after(tglAwal) && jthTempo.before(tglAkhir)) && bayar.before(tglAkhir)){
		//	dateJatuhTempo.setDate(2017, 06, 04);
		//}
		//set tgl jatuh tempo perwal terkait Covid-19
		Date jthTempo = new Date(dateJatuhTempo.getYear() - 1900, dateJatuhTempo.getMonth(), dateJatuhTempo.getDay());
		Date bayar = new Date(datePembayaran.getYear() - 1900, datePembayaran.getMonth(), datePembayaran.getDay());
		Date tglAwal = new Date(120,03,1);
		Date tglAkhir = new Date(120,06,31);
		if ((txtNoSPTPD.getText().contains("SPTPD/") || txtNoSPTPD.getText().contains("SKPD/")) && (jthTempo.after(tglAwal) && jthTempo.before(tglAkhir))){
			dateJatuhTempo.setDate(2020, 06, 31);
		}
	}
	
	@SuppressWarnings("deprecation")
	private void resetForm()
	{
		for(TableColumn column : table.getColumns()){
			column.dispose();
		}
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
		}
		selectedID = null;
		txtTotalPajakTerhutang.setMoney(new CurrencyAmount(0, Currency.getInstance("ind")));
		table.removeAll();
		tableAngsuran.removeAll();
		tableAngsuran.setVisible(false);
		tblCicilan.removeAll();
		tblCicilan.setVisible(false);
		lblSudahDiangsur.setVisible(false);
		txtTotalAngsur.setMoney(new CurrencyAmount(0, Currency.getInstance("ind")));
		txtTotalAngsur.setVisible(false);
		lblSisaPajak.setVisible(false);
		txtSisa.setMoney(new CurrencyAmount(0, Currency.getInstance("ind")));
		txtSisa.setVisible(false);
		txtDibayar.setMoney(new CurrencyAmount(0, Currency.getInstance("ind")));
		txtSisaDendaDibayar.setMoney(new CurrencyAmount(0, Currency.getInstance("ind")));
		lblLunas.setVisible(false);
		txtLokasi.setVisible(false);
		compPejabat.setVisible(false);
		txtNoUrut.setFocus();
		cmbMasaPajak.select(-1);
		cmbMasaPajak.setText("");
		cmbMasaPajak.getTable().removeAll();
		btnCetakSts.setVisible(false);
		txtNoSTS.setEditable(true);
		lblAngsuranKe.setVisible(false);
		cmbAngsuran.setVisible(false);
	}
	
	@SuppressWarnings("deprecation")
	private void loadSKPD()
	{
		tableAngsuran.setVisible(true);
		createColumnSKPD();
//		selectedID = null;
//		colMasaPajak.setText("Masa Pajak");
		String[] monthName = DateFormatSymbols.getInstance(ind).getMonths();
//		String[] listTable = new String[]{"Masa Pajak"};
		//Double[] listPajak = new Double[]{pokokPajak};
		Integer start = listMasaPajak.get(cmbMasaPajak.getSelectionIndex()).getMasaPajakDari().getMonth();
		Integer limit = listMasaPajak.get(cmbMasaPajak.getSelectionIndex()).getMasaPajakSampai().getMonth() - listMasaPajak.get(cmbMasaPajak.getSelectionIndex()).getMasaPajakDari().getMonth();
		for(int i=start;i<=start + limit;i++){
			TableItem item = new TableItem(tableAngsuran, SWT.NONE);
			item.setText(new String[]{monthName[i] + " " + (listMasaPajak.get(cmbMasaPajak.getSelectionIndex()).getMasaPajakDari().getYear() + 1900)});
//			totalPajakTerutang += listDouble[i];
		}
	}
	
	@SuppressWarnings("deprecation")
	private void getSKPD(String bulanSKPD)
	{
		double pajakBulan = totalPajakTerutang;
		if (bulanSKPD.length() == 6)
			bulanSKPD = "0" + bulanSKPD;
		objSspd	= ControllerFactory.getMainController().getCpSspdDAOImpl().getSSPD_SKPD(wp.getNPWP(), txtNoSPTPD.getText(), bulanSKPD);
		if (objSspd != null)
		{
			selectedID = objSspd.getIdSspd();
//			if (selectedID == 0)
//				selectedID = null;
			
			if (objSspd.getTglCetak() != null)
			{
				datePembayaran.setDate(objSspd.getTglSspd().getYear() + 1900, objSspd.getTglSspd().getMonth(), objSspd.getTglSspd().getDate());
				if (objSspd.getTglCetak() != null)
				{
					dateCetak.setDate(objSspd.getTglCetak().getYear() + 1900, objSspd.getTglCetak().getMonth(), objSspd.getTglCetak().getDate());
				}
				else
					dateCetak.setDate(objSspd.getTglSspd().getYear() + 1900, objSspd.getTglSspd().getMonth(), objSspd.getTglSspd().getDate());
			}
			else {
				datePembayaran.setDate(dateNow.getYear() + 1900, dateNow.getMonth(), dateNow.getDate());
				dateCetak.setDate(dateNow.getYear() + 1900, dateNow.getMonth(), dateNow.getDate());
				}

			bulanSKP = bulanSKPD;
			dateJatuhTempo.setVisible(true);
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.DAY_OF_MONTH, listMasaPajak.get(cmbMasaPajak.getSelectionIndex()).getMasaPajakDari().getDate());
			cal.set(Calendar.MONTH, listMasaPajak.get(cmbMasaPajak.getSelectionIndex()).getMasaPajakDari().getMonth() + tableAngsuran.getSelectionIndex());
			cal.set(Calendar.YEAR, listMasaPajak.get(cmbMasaPajak.getSelectionIndex()).getMasaPajakDari().getYear() + 1900);
			cal.add(Calendar.MONTH, 1);
			dateJatuhTempo.setDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.getActualMaximum(Calendar.DAY_OF_MONTH));
			hitungPajak();
			
				if (objSspd.getLunas())
				{
					lblLunas.setVisible(true);
					txtNoSSPD.setText(objSspd.getNoSspd());
					lblNoSspd.setText("No SSPD");
					txtDibayar.setMoney(new CurrencyAmount(objSspd.getJumlahBayar(),Currency.getInstance(ind)));
					txtNamaPenyetor.setText(objSspd.getNamaPenyetor());
					
					if (objSspd.getCaraBayar().equalsIgnoreCase("Tunai"))
						btnTunai.setSelection(true);
					else if (objSspd.getCaraBayar().equalsIgnoreCase("Giro"))
						btnGiro.setSelection(true);
					else if (objSspd.getCaraBayar().equalsIgnoreCase("Bank"))
						btnBank.setSelection(true);
				}
				else if (objSspd.getNotaBayar() != null)
				{
//					btnCetakSts.setVisible(true);
					lblLunas.setVisible(false);
					lblNoSspd.setText("Nota Bayar");
					if (objSspd.getNotaBayar() != null)
						txtNoSSPD.setText(objSspd.getNotaBayar());
					CurrencyAmount currencyAmount = new CurrencyAmount(objSspd.getJumlahBayar(),Currency.getInstance(ind));
					txtDibayar.setMoney(currencyAmount);
					txtNamaPenyetor.setText(objSspd.getNamaPenyetor());
					compPejabat.setVisible(true);
					
					if (objSspd.getCaraBayar().equalsIgnoreCase("Tunai")){
						btnTunai.setSelection(true);
						btnGiro.setSelection(false);
						btnBank.setSelection(false);
					}
					else if (objSspd.getCaraBayar().equalsIgnoreCase("Giro")){
						btnGiro.setSelection(true);
						btnTunai.setSelection(false);
						btnBank.setSelection(false);
					}
					else if (objSspd.getCaraBayar().equalsIgnoreCase("Bank")){
						btnBank.setSelection(true);
						btnGiro.setSelection(false);
						btnTunai.setSelection(false);
					}
				}
				else
				{
					lblLunas.setVisible(false);
					lblNoSspd.setText("Nota Bayar");
					txtNoSSPD.setText("");
//					txtDibayar.setMoney(new CurrencyAmount(0, Currency.getInstance("ind")));
					txtNamaPenyetor.setText(wp.getNamaPemilik());
					btnTunai.setSelection(true);
					datePembayaran.setDate(dateNow.getYear() + 1900, dateNow.getMonth(), dateNow.getDate());
					dateCetak.setDate(dateNow.getYear() + 1900, dateNow.getMonth(), dateNow.getDate());
					pajakBulan += dendaSSPD;
					txtTotalPajakTerhutang.setMoney(new CurrencyAmount(pajakBulan, Currency.getInstance("ind")));
					txtDibayar.setMoney(new CurrencyAmount(pajakBulan, Currency.getInstance("ind")));
//					txtNamaPenyetor.setText("");
					
				}
				TableItem ti = table.getItem(table.getItemCount()-1);
				ti.setText(new String[]{"Denda SSPD", indFormat.format(dendaSSPD)});
		}
//		hitungPajak();
//		dendaSSPD = hitungDendaSSPD(pokokPajak);
//		TableItem item = table.getItem(table.getItemCount()-1);
//		item.setText(new String[]{"Denda SSPD",indFormat.format(dendaSSPD)});
//		totalPajakTerutang = pokokPajak + dendaSSPD;
//		txtTotalPajakTerhutang.setMoney(new CurrencyAmount(totalPajakTerutang, Currency.getInstance("ind")));
	}
	
	@SuppressWarnings("deprecation")
	private void loadAngsuran(){
//		tableAngsuran.removeAll();
		createColumnAngsur();
		if (userModul.getIdUser() == 1){
			lblAngsuranKe.setVisible(true);
			cmbAngsuran.setVisible(true);
		}
		tblCicilan.setVisible(true);
		tableAngsuran.setVisible(true);
		lblSudahDiangsur.setVisible(true);
		txtTotalAngsur.setVisible(true);
		lblSisaPajak.setVisible(true);
		txtSisa.setVisible(true);
		List<Sspd> listAngsuran = ControllerFactory.getMainController().getCpSspdDAOImpl().getComboAngsuran(txtNoSPTPD.getText(), wp.getNPWP());
		IDSSPDAngsur = new int[listAngsuran.size()];
		SimpleDateFormat sdfformat = new SimpleDateFormat("dd-MM-yyyy");
		for(int i=0;i<listAngsuran.size();i++){
			TableItem ti = new TableItem(tableAngsuran, SWT.NONE);
//			masaPajakBayar[i] = UIController.INSTANCE.formatMonth(Integer.parseInt(listBayar.get(i).getBulanSkp().split("-")[0]), Locale.getDefault()) + " " + (listBayar.get(i).getBulanSkp().split("-")[1]);
			//ti.setText(new String[]{"",listBayar.get(i).getNoSspd(),listBayar.get(i).getNpwpd(),masaPajakBayar[i],String.valueOf(indFormat.format(listBayar.get(i).getJumlahBayar()))});
			IDSSPDAngsur[i] = listAngsuran.get(i).getIdSspd();
			ti.setText(0, listAngsuran.get(i).getCicilanKe().toString());
			ti.setText(1, indFormat.format(listAngsuran.get(i).getJumlahBayar().doubleValue()).toString());
			ti.setText(2, sdfformat.format(listAngsuran.get(i).getTglSspd()));
			ti.setText(3, listAngsuran.get(i).getNoSspd());
			ti.setText(4, listAngsuran.get(i).getNoSkp());
			ti.setText(5, indFormat.format(listAngsuran.get(i).getDenda().doubleValue()).toString());
			ti.setText(6, listAngsuran.get(i).getDendaLunas() == true ? String.valueOf(1) : String.valueOf(0));//Boolean.toString(listAngsuran.get(i).getDendaLunas()));			
		}
		
		Double totalAngsur = 0.0;
		totalDendaAngsuran = 0;
		updNoSSPD = new String[listAngsuran.size()];
//		updIdSSPD = new int[listAngsuran.size()];
		updDenda = new double[listAngsuran.size()];
//		int i=0;
		for (int i=0;i<tableAngsuran.getItemCount();i++){
			if (cicilanKe == Integer.parseInt(tableAngsuran.getItem(i).getText(0))){
				break;
			}
			try {
				totalAngsur += indFormat.parse(tableAngsuran.getItem(i).getText(1)).doubleValue();
				totalDendaAngsuran += indFormat.parse(tableAngsuran.getItem(i).getText(5)).doubleValue();
				
				if (tableAngsuran.getItem(i).getText(6) == String.valueOf(0)){
					updNoSSPD[i] = tableAngsuran.getItem(i).getText(3);
//					updIdSSPD[i] = 
					updDenda[i] = listAngsuran.get(i).getDenda().doubleValue();
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}	
		}
		TableItem item = new TableItem(table, SWT.NONE);
		item.setText(0, "Denda Terlambat Sebelumnya");
		item.setText(1, indFormat.format(totalDendaAngsuran));
		
//		try {
//			if(indFormat.parse(tblCicilan.getItem(0).getText(1)).doubleValue() > txtDibayar.getMoney().getNumber().doubleValue())
//				btnBayar.setEnabled(false);
//			else
//				if (enable)
//					btnBayar.setEnabled(true);
//				
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
		
		int idMohon = ControllerFactory.getMainController().getCpMohonDAOImpl().checkMohon(wp.getNPWP(), txtNoSPTPD.getText());
		Mohon mohon = ControllerFactory.getMainController().getCpMohonDAOImpl().getMohon(idMohon);
		List<MohonDetail> md = ControllerFactory.getMainController().getCpMohonDetailDAOImpl().getDaftarAngsuran(idMohon);
		double totalPajakAngsuran = 0;
		angsuranTerakhir = md.size();
		for (MohonDetail mohonDetail : md) {
			totalPajakAngsuran += mohonDetail.getAngsuranPokok() + mohonDetail.getBiayaAdministrasi() + mohonDetail.getDendaSkpdkb();
		}
		//Setelah launching online, semua decimal harus dihapuskan. karena tidak bisa masuk ke sistem BPKAD
//		Date batas = new Date(117, 9, 21);
//		if (mohon.getTglMohon().after(batas)){
			pokokPajak = roundUP(pokokPajak);
			denda = roundUP(denda);
			dendaTambahan = roundUP(dendaTambahan);
			table.getItem(0).setText(1, indFormat.format(pokokPajak));
			table.getItem(1).setText(1, indFormat.format(denda));
			table.getItem(3).setText(1, indFormat.format(dendaTambahan));
//		}
//		DecimalFormat df = new DecimalFormat("#.##");
//		df.setMaximumFractionDigits(2);
//		df.setRoundingMode(RoundingMode.HALF_EVEN);
		totalPajakAngsuran = round(totalPajakAngsuran);
		totalDendaAngsuran = round(totalDendaAngsuran);
		txtTotalPajakTerhutang.setMoney(new CurrencyAmount((totalPajakAngsuran + totalDendaAngsuran), Currency.getInstance("ind")));
		txtTotalAngsur.setMoney(new CurrencyAmount(totalAngsur, Currency.getInstance("ind")));
		
		for (Integer i=listAngsuran.size()+1;i<md.size()+1;i++){
			cmbAngsuran.add(i.toString());
		}
		
		if (txtTotalPajakTerhutang.getMoney().getNumber().doubleValue() - totalAngsur <= 1){
			lblLunas.setVisible(true);
			angsuranLunas = true;
		}
		else{
			lblLunas.setVisible(false);
			angsuranLunas = false;
		}
		
		if (tableAngsuran.getItemCount() < md.size()){
			if (md.get(tableAngsuran.getItemCount()).getSts()!=null){
				Date valid = md.get(tableAngsuran.getItemCount()).getStsValid();
				dtValid.setDate(valid.getYear()+1900, valid.getMonth(), valid.getDate());
				txtNoSTS.setText(md.get(tableAngsuran.getItemCount()).getSts());
			}
			else{
				txtNoSTS.setText("");
				dtValid.setDate(dateNow.getYear()+1900, dateNow.getMonth(), dateNow.getDate());
			}
		}
		loadTanggaljatuhTempo();
	}
	
	@SuppressWarnings("deprecation")
	private void loadTanggaljatuhTempo(){
		if (!angsuranLunas){
			Date jatuhTempo;
			int idMohon = ControllerFactory.getMainController().getCpMohonDAOImpl().checkMohon(wp.getNPWP(), txtNoSPTPD.getText());
//			Mohon mohon = ControllerFactory.getMainController().getCpMohonDAOImpl().getMohon(idMohon);
			List<MohonDetail> md = ControllerFactory.getMainController().getCpMohonDetailDAOImpl().getDaftarAngsuran(idMohon);
			if (cicilanKe > 0){
				jatuhTempo = md.get(cicilanKe - 1).getTglAngsuran();
				dateJatuhTempo.setDate(jatuhTempo.getYear() + 1900, jatuhTempo.getMonth(), jatuhTempo.getDate());
				pajakAngsuranPokok = roundUP(md.get(cicilanKe - 1).getAngsuranPokok());
				pajakAngsuran = pajakAngsuranPokok + roundUP(md.get(cicilanKe - 1).getBiayaAdministrasi()) + roundUP(md.get(cicilanKe - 1).getDendaSkpdkb());
			}
			else if (tableAngsuran.getItemCount() + 1 > md.size()){
				jatuhTempo = md.get(md.size() - 1).getTglAngsuran();
				dateJatuhTempo.setDate(jatuhTempo.getYear() + 1900, jatuhTempo.getMonth(), jatuhTempo.getDate());
				pajakAngsuranPokok = roundUP(md.get(md.size() - 1).getAngsuranPokok());
				pajakAngsuran = pajakAngsuranPokok + roundUP(md.get(md.size() - 1).getBiayaAdministrasi()) + roundUP(md.get(md.size() - 1).getDendaSkpdkb());
			}
			else{
				jatuhTempo = md.get(tableAngsuran.getItemCount()).getTglAngsuran();
				dateJatuhTempo.setDate(jatuhTempo.getYear() + 1900, jatuhTempo.getMonth(), jatuhTempo.getDate());
				pajakAngsuranPokok = roundUP(md.get(tableAngsuran.getItemCount()).getAngsuranPokok());
				pajakAngsuran = pajakAngsuranPokok + roundUP(md.get(tableAngsuran.getItemCount()).getBiayaAdministrasi()) + roundUP(md.get(tableAngsuran.getItemCount()).getDendaSkpdkb());
			}
			
			TableItem item = new TableItem(tblCicilan, SWT.NONE);
			item.setText(0, "Cicilan Angsuran");
			item.setText(1, indFormat.format(pajakAngsuran));
		}
		else
			pajakAngsuran = 0;
		
	}
	
	@SuppressWarnings("deprecation")
	private void hitungPajak()
	{
		dendaSSPD = hitungDendaSSPD(pokokPajak);
		Date tglSetor = new Date(datePembayaran.getYear() - 1900, datePembayaran.getMonth(), datePembayaran.getDay());
		Date tglBatas = new Date(2012 - 1900, 03, 01);
		if (tglSetor.compareTo(tglBatas) < 0 && txtNoSPTPD.getText().contains("SKPDKB")) {
			dendaSSPD = 0;
		}
		double dendaSSPDSebelumnya = totalDendaAngsuran;
		double dendaSKPDKB = 0;
		double totalPajak = round(pokokPajak) + round(dendaTambahan) + round(denda) + round(dendaSSPD) + round(dendaSKPDKB);
//		double totalPajakAngsur = 0;
		if (btnAngsuran.getSelection()){
//			totalPajakAngsur = pajakAngsuran + dendaSSPD;
			totalPajak = 0;
			int idMohon = ControllerFactory.getMainController().getCpMohonDAOImpl().checkMohon(wp.getNPWP(), txtNoSPTPD.getText());
			List<MohonDetail> md = ControllerFactory.getMainController().getCpMohonDetailDAOImpl().getDaftarAngsuran(idMohon);
//			int count = md.size();
			double nilaiDendaSKPDKB = 0;
			for (MohonDetail mohonDetail : md) {
				totalPajak += mohonDetail.getAngsuranPokok() + mohonDetail.getBiayaAdministrasi() + mohonDetail.getDendaSkpdkb();
				nilaiDendaSKPDKB += mohonDetail.getDendaSkpdkb();
			}
			dendaSKPDKB = nilaiDendaSKPDKB;
			totalPajak += dendaSSPD + dendaSSPDSebelumnya;
			System.out.println(totalPajak);
			System.out.println(dendaSSPD);
			System.out.println(dendaSSPDSebelumnya);
		}
		if (btnTepatWaktu.getSelection()){
			txtTotalPajakTerhutang.setMoney(new CurrencyAmount(totalPajak, Currency.getInstance("ind")));
			if(!lblLunas.getVisible() && txtNoSSPD.getText().equalsIgnoreCase(""))
				txtDibayar.setMoney(new CurrencyAmount(totalPajak, Currency.getInstance("ind")));
		}
		else if (btnAngsuran.getSelection()){
			TableItem item = table.getItem(3);
			item.setText(1, indFormat.format(dendaSKPDKB));
			txtTotalPajakTerhutang.setMoney(new CurrencyAmount((totalPajak), Currency.getInstance("ind")));
			totalAngsurChanged();
		}
	}
	
	@SuppressWarnings("deprecation")
	private Double hitungDendaSSPD(Double pajakTerutang)
	{
		Double retValue = 0.0;
		if (btnTepatWaktu.getSelection())
		{
			if ((txtNoSPTPD.getText().indexOf("SPTPD") > 1) || (txtNoSPTPD.getText().indexOf("SKPD/") > 1 && (!txtKodePajak.getText().equalsIgnoreCase("7")))) {
				Date tglSetor = new Date(datePembayaran.getYear() - 1900, datePembayaran.getMonth(), datePembayaran.getDay());
				Date tglTempo = new Date(dateJatuhTempo.getYear() - 1900, dateJatuhTempo.getMonth(), dateJatuhTempo.getDay());
				if (tglSetor.compareTo(tglTempo) > 0)
				{
					Integer lamaBunga = ((tglSetor.getYear() - tglTempo.getYear()) * 12) + tglSetor.getMonth() - tglTempo.getMonth();
//					+ (tglSetor.getDate() > tglTempo.getDate() ? 1 : 0);
					if (lamaBunga <= 24)
						retValue = 0.02 * lamaBunga * pajakTerutang;
					else
						retValue = 0.02 * 24 * pajakTerutang;
				}
				else
					retValue = 0.0;
			}
			else{
				Date tglSetor = new Date(datePembayaran.getYear() - 1900, datePembayaran.getMonth(), datePembayaran.getDay());
				Date tglTempo = new Date(dateJatuhTempo.getYear() - 1900, dateJatuhTempo.getMonth(), dateJatuhTempo.getDay());
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
			}
		}
		else{
			if (!angsuranLunas){
				Date tglSetor = new Date(datePembayaran.getYear() - 1900, datePembayaran.getMonth(), datePembayaran.getDay());
				Date tglTempo = new Date(dateJatuhTempo.getYear() - 1900, dateJatuhTempo.getMonth(), dateJatuhTempo.getDay());
				if (tglSetor.compareTo(tglTempo) > 0)
				{
					Integer lamaBunga = ((tglSetor.getYear() - tglTempo.getYear()) * 12) + tglSetor.getMonth() - tglTempo.getMonth() +
							 (tglSetor.getDate() > tglTempo.getDate() ? 1 : 0);
					if (lamaBunga <= 24)
						retValue = 0.02 * lamaBunga * pajakAngsuranPokok;
					else
						retValue = 0.02 * 24 * pajakAngsuranPokok;
				}
				else
					retValue = 0.0;
			}
		}
		DecimalFormat df = new DecimalFormat("#.##");
		df.setMaximumFractionDigits(0);
		df.setRoundingMode(RoundingMode.UP);
		return Double.parseDouble(df.format(retValue));
	}
	
	@SuppressWarnings("deprecation")
	private void loadSSPD(int id){
		Sspd oldSSPD = ControllerFactory.getMainController().getCpSspdDAOImpl().GetSSPDOld(id);
		txtDibayar.setMoney(new CurrencyAmount(oldSSPD.getJumlahBayar(), Currency.getInstance(ind)));
		datePembayaran.setDate(oldSSPD.getTglSspd().getYear() + 1900, oldSSPD.getTglSspd().getMonth(), oldSSPD.getTglSspd().getDate());
		dateCetak.setDate(oldSSPD.getTglCetak().getYear() + 1900, oldSSPD.getTglCetak().getMonth(), oldSSPD.getTglCetak().getDate());
		txtNamaPenyetor.setText(oldSSPD.getNamaPenyetor());
		tempNota = oldSSPD.getNotaBayar();
		if (oldSSPD.getCaraBayar().equalsIgnoreCase("Tunai")){
			btnTunai.setSelection(true);
			btnGiro.setSelection(false);
			btnBank.setSelection(false);
		}
		else if (oldSSPD.getCaraBayar().equalsIgnoreCase("Giro")){
			btnTunai.setSelection(false);
			btnGiro.setSelection(true);
			btnBank.setSelection(false);
		}
		else{
			btnTunai.setSelection(false);
			btnGiro.setSelection(false);
			btnBank.setSelection(true);
		}
			
		if (oldSSPD.getNoSspd().equalsIgnoreCase("") || oldSSPD.getNoSspd() == null){
			txtNoSSPD.setText(oldSSPD.getNotaBayar());
			lblNoSspd.setText("Nota Bayar");
		}
		else{
			txtNoSSPD.setText(oldSSPD.getNoSspd());
			lblNoSspd.setText("No SSPD");
		}
		if (!txtNoSSPD.getText().contains("SSPD")){
			if (userModul.getIdUser()==1)
				btnHapus.setEnabled(true);
			lblLunas.setVisible(false);
		}
		else{
			btnHapus.setEnabled(false);
			lblLunas.setVisible(true);
		}
	}
	
	private void loadPejabat(){
		listPejabat = ControllerFactory.getMainController().getCpPejabatDAOImpl().getAllPejabat();
		for (int i=0;i<listPejabat.size();i++){
			UIController.INSTANCE.loadPejabat(cmbPejabat, listPejabat.toArray());
//			cmbPejabat.setData(listPejabat.get(i).getIdPejabatNIP(), listPejabat.get(i).getNamaPejabat());
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void accept(Object parameter) {
		sspd = (Sspd) parameter;
		if(sspd != null){
			txtNoUrut.setText(sspd.getNpwpd().substring(1, 8));
			lblLunas.setVisible(false);
			getDataWP();
			if (wp.getStatus() != null && !wp.getStatus().equalsIgnoreCase("Aktif")){
				// TODO Set warna background dan getNoSuratKeteranganTutup dari table tutup
				lblKeterangan.setVisible(true);
				lblKeterangan.setText(wp.getKeteranganTutup());
				if(wp.getStatus().equalsIgnoreCase("Tutup")){
					WpTutup wptutup = ControllerFactory.getMainController().getCpWajibPajakDAOImpl().getWajibPajakTutup(wp.getNPWP());
					SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
					try{
						if(wptutup.getTglSampaiTutup().getYear()+1900 >= 9999){
							lblKeterangan.setBackground(SWTResourceManager.getColor(SWT.COLOR_DARK_RED));
							lblKeterangan.setText(wptutup.getNoSuratTutup()+"\n"+
									"Tanggal TUTUP dari :"+sdf.format(wptutup.getTglMulaiTutup())+" Permanen");
						}else{
							lblKeterangan.setBackground(SWTResourceManager.getColor(SWT.COLOR_CYAN));
							lblKeterangan.setText(wptutup.getNoSuratTutup()+"\n"+
							"Tanggal TUTUP dari :"+sdf.format(wptutup.getTglMulaiTutup())+" - "+sdf.format(wptutup.getTglSampaiTutup()));
						}
					}catch(Exception exc){
						lblKeterangan.setBackground(SWTResourceManager.getColor(SWT.COLOR_CYAN));
					}
				}else
					lblKeterangan.setBackground(SWTResourceManager.getColor(SWT.COLOR_CYAN));
			}else{
				lblKeterangan.setText("");
				lblKeterangan.setVisible(false);
			}
			
			txtNoSPTPD.setText(sspd.getNoSkp());
			if (!(txtNoSPTPD.getText().indexOf("SKPDKB/") > 1) && !(txtNoSPTPD.getText().indexOf("SKPDKBT/") > 1))
				listMasaPajak = ControllerFactory.getMainController().getCpSptpdDAOImpl().getMasaPajakSPT(sspd.getNpwpd(), sspd.getNoSkp());
			else
				listMasaPajak = ControllerFactory.getMainController().getCpSptpdDAOImpl().getMasaPajakSKPDKB(sspd.getNpwpd(), sspd.getNoSkp());
			if (listMasaPajak.size() > 0)
			{
				UIController.INSTANCE.loadTMasaPajak(cmbMasaPajak, listMasaPajak.toArray()); //PajakProvider.INSTANCE.getPajak().toArray());
				
			}
//			tesAntri();
			cmbMasaPajak.select((Integer)cmbMasaPajak.getData(sspd.getNoSkp()));
			objSspd = ControllerFactory.getMainController().getCpSspdDAOImpl().getSSPD(wp.getNPWP(), txtNoSPTPD.getText());
			checkSKPDKB = false;
			cmbMasaPajakSelected();
			
//			compButton.setVisible(false);
//			compPejabat.setVisible(false);
			
			if (btnAngsuran.getSelection()){
				for(int i =0;i<tableAngsuran.getItemCount();i++){
					if(tableAngsuran.getItem(i).getText(3).equalsIgnoreCase(sspd.getNoSspd())){
						tableAngsuran.select(i);
						loadPerhitunganAngsuran();
//						tableAngsuran.showItem(tableAngsuran.getItem(i));
//						tableAngsuran.getListeners(SWT.Selection)[0].handleEvent(null);
						break;
					}
				}
			}
			else{
				if (tableAngsuran.getVisible()){
					String masaPajakBayar = UIController.INSTANCE.formatMonth(Integer.parseInt(sspd.getBulanSkp().split("-")[0]), Locale.getDefault()) + " " + (sspd.getBulanSkp().split("-")[1]);
					for(int i =0;i<tableAngsuran.getItemCount();i++){
						if(tableAngsuran.getItem(i).getText(0).equalsIgnoreCase(masaPajakBayar)){
							tableAngsuran.select(i);
							loadPerhitunganAngsuran();
							break;
						}
					}
				}
			}
			
			HashMap<String, String> hashMap = new HashMap<String,String>();
			for(TableItem item : table.getItems())
				hashMap.put(item.getText(0).replace(" ", ""), item.getText(1));
			
			if (txtNoSPTPD.getText().contains("SKPDKB")){
				double jumlahPemeriksaan = 0.0;
				if (btnAngsuran.getSelection()){
					for(TableItem item : tblCicilan.getItems())
						hashMap.put(item.getText(0).replace(" ", ""), item.getText(1));
//					DecimalFormat df = new DecimalFormat("#.##");
//					df.setMaximumFractionDigits(2);
//					df.setRoundingMode(RoundingMode.HALF_EVEN);
					hashMap.put("JumlahTerhutang", indFormat.format(round(txtSisa.getMoney().getNumber().doubleValue()) - txtDibayar.getMoney().getNumber().doubleValue()));
					hashMap.put("JumlahBayarSebelum", indFormat.format(txtTotalAngsur.getMoney().getNumber().doubleValue()));
					//hashMap.put("JumlahTerhutang", indFormat.format(txtSisa.getMoney().getNumber().doubleValue() - txtDibayar.getMoney().getNumber().doubleValue()));
					try {
						jumlahPemeriksaan = indFormat.parse(table.getItem(0).getText(1)).doubleValue() + indFormat.parse(table.getItem(1).getText(1)).doubleValue() + indFormat.parse(table.getItem(2).getText(1)).doubleValue() + indFormat.parse(table.getItem(3).getText(1)).doubleValue();
//						double totalPajak = jumlahPemeriksaan + indFormat.parse(table.getItem(4).getText(1)).doubleValue() + indFormat.parse(tblCicilan.getItem(1).getText(1)).doubleValue();
						hashMap.put("JumlahPemeriksaan", indFormat.format(jumlahPemeriksaan));
//						hashMap.put("TotalPajak", totalPajak);
						
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
				else{
					try {
						jumlahPemeriksaan = indFormat.parse(table.getItem(0).getText(1)).doubleValue() + indFormat.parse(table.getItem(1).getText(1)).doubleValue() + indFormat.parse(table.getItem(2).getText(1)).doubleValue() + indFormat.parse(table.getItem(3).getText(1)).doubleValue();
//						double totalPajak = jumlahPemeriksaan + indFormat.parse(table.getItem(4).getText(1)).doubleValue() + indFormat.parse(tblCicilan.getItem(1).getText(1)).doubleValue();
						hashMap.put("JumlahPemeriksaan", indFormat.format(jumlahPemeriksaan));
//						hashMap.put("TotalPajak", totalPajak);
						
					} catch (ParseException e) {
						e.printStackTrace();
					}
					hashMap.put("DendaSSPD", table.getItem(4).getText(1));
					hashMap.put("JumlahBayarSebelum", indFormat.format(0.0));
					hashMap.put("JumlahTerhutang", indFormat.format(txtTotalPajakTerhutang.getMoney().getNumber().doubleValue() - txtDibayar.getMoney().getNumber().doubleValue()));
					hashMap.put("JumlahPemeriksaan", indFormat.format(jumlahPemeriksaan));
					hashMap.put("DendaTerlambatSebelumnya", indFormat.format(0.0));
				}
			}
			
			hashMap.put("TotalPajak", indFormat.format(txtTotalPajakTerhutang.getMoney().getNumber().doubleValue()));
			hashMap.put("Sisa", indFormat.format(txtTotalPajakTerhutang.getMoney().getNumber().doubleValue()-txtDibayar.getMoney().getNumber().doubleValue()));
			hashMap.put("Lokasi", txtLokasi.getText());
			hashMap.put("STS", txtNoSTS.getText());
			if (openViewAntri){
				ICommunicationView viewToOpen;
				try {
					viewToOpen = (ICommunicationView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(AntriView.ID);
					viewToOpen.accept(hashMap);
					viewToOpen.setFocus();
				} catch (PartInitException e1) {
					e1.printStackTrace();
				}
				openViewAntri = true;
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	private void cmbMasaPajakSelected(){
		selectedID = null;
		totalPajakTerutang = 0;
		pokokPajak = 0;
		denda = 0;
		dendaTambahan = 0;
		dendaTerlambatSKPDKB = 0;
		dendaSSPD = 0;
		kenaikan = 0;
		cicilanKe = 0;
		idPeriksa = null;
		idSPTPD = null;
		btnHapus.setEnabled(false);
		tableAngsuran.removeAll();
		tableAngsuran.setVisible(false);
		table.removeAll();
		tblCicilan.removeAll();
		tblCicilan.setVisible(false);
		txtNoSTS.setEditable(false);
		lblSudahDiangsur.setVisible(false);
		txtTotalAngsur.setMoney(new CurrencyAmount(0, Currency.getInstance("ind")));
		txtTotalAngsur.setVisible(false);
		lblSisaPajak.setVisible(false);
		txtSisa.setMoney(new CurrencyAmount(0, Currency.getInstance("ind")));
		txtSisa.setVisible(false);
		//MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "Pesan", cmbMasaPajak.getData(Integer.toString(cmbMasaPajak.getSelectionIndex())).toString());
		txtNoSPTPD.setText(cmbMasaPajak.getData(Integer.toString(cmbMasaPajak.getSelectionIndex())).toString());
		txtNoSSPD.setText("");
		btnTunai.setSelection(true);
		btnBank.setSelection(false);
		btnGiro.setSelection(false);
		datePembayaran.setDate(dateNow.getYear() + 1900, dateNow.getMonth(), dateNow.getDate());
		dateCetak.setDate(dateNow.getYear() + 1900, dateNow.getMonth(), dateNow.getDate());
		if (listMasaPajak.get(cmbMasaPajak.getSelectionIndex()).getSts()!=null){
			Date valid = listMasaPajak.get(cmbMasaPajak.getSelectionIndex()).getStsValid();
			dtValid.setDate(valid.getYear()+1900, valid.getMonth(), valid.getDate());
			txtNoSTS.setText(listMasaPajak.get(cmbMasaPajak.getSelectionIndex()).getSts());
		}
		else{
			txtNoSTS.setText("");
			dtValid.setDate(dateNow.getYear()+1900, dateNow.getMonth(), dateNow.getDate());
		}
		if (! listMasaPajak.get(cmbMasaPajak.getSelectionIndex()).getNoSPTPDLama().equalsIgnoreCase("")){
			lblSlash.setVisible(true);
			txtNoSKPLama.setVisible(true);
			txtNoSKPLama.setText(listMasaPajak.get(cmbMasaPajak.getSelectionIndex()).getNoSPTPDLama());
		}else{
			lblSlash.setVisible(false);
			txtNoSKPLama.setVisible(false);
			txtNoSKPLama.setText("");
		}
			
						
		listPeriksa = ControllerFactory.getMainController().getCpPeriksaDAOImpl().checkSKPDKB(wp.getNPWP());
		pajakDari = new Date[listPeriksa.size()];
		pajakSampai = new Date[listPeriksa.size()];
		jenisSKP = new String[listPeriksa.size()];
		if (listPeriksa.size() > 0){
			for (int i=0;i<listPeriksa.size();i++){
				pajakDari[i] = listPeriksa.get(i).getMasaPajakDari();
				pajakSampai[i] = listPeriksa.get(i).getMasaPajakSampai();
				jenisSKP[i] = listPeriksa.get(i).getTipeSkp();
			}
		}
		enable = true;
		if (!(txtNoSPTPD.getText().indexOf("SKPDKB/") > 1) && !(txtNoSPTPD.getText().indexOf("SKPDKBT/") > 1) && !(txtNoSPTPD.getText().indexOf("SKPDN/") > 1) && checkSKPDKB) 
		{
			for (int i=0;i<listPeriksa.size();i++){
				if (pajakSampai[i].after(listMasaPajak.get(cmbMasaPajak.getSelectionIndex()).getMasaPajakDari()) && pajakDari[i].before(listMasaPajak.get(cmbMasaPajak.getSelectionIndex()).getMasaPajakSampai()))
				{
					txtNamaPenyetor.setFocus();
					MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "SPTPD tidak bisa dibayar karena telah keluar SKPDKB");
					enable = false;
					break;
				}
			}
		}
		checkSKPDKB = true;
		btnBayar.setEnabled(enable);
		btnGenerateSts.setEnabled(enable);
		
		jenisBayar = ControllerFactory.getMainController().getCpSspdDAOImpl().getJenisBayar(wp.getNPWP(), txtNoSPTPD.getText());
		if (jenisBayar == 0){
			btnTepatWaktu.setSelection(true);
			btnAngsuran.setSelection(false);
		}
		else {
			btnAngsuran.setSelection(true);
			btnTepatWaktu.setSelection(false);
		}

		setTanggalJatuhTempo(jenisBayar, cmbMasaPajak.getSelectionIndex());
		if (txtNoSPTPD.getText().contains("/SKPDKB/"))
		{
			keteranganSKPDKB = ControllerFactory.getMainController().getCpPeriksaDAOImpl().GetKeteranganSKPDKB(txtNoSPTPD.getText(), wp.npwp);
			idPeriksa = ControllerFactory.getMainController().getCpPeriksaDAOImpl().GetIdPeriksa(txtNoSPTPD.getText(), wp.getNPWP());
			if (keteranganSKPDKB.contains("Pemeriksaan BPK")){
				pokokPajak = ControllerFactory.getMainController().getCpPeriksaDetailBPKDAOImpl().GetPajakSKPDKB(idPeriksa);
				denda = ControllerFactory.getMainController().getCpPeriksaDetailBPKDAOImpl().GetDendaSKPDKB(idPeriksa);
			}else{
				pokokPajak = ControllerFactory.getMainController().getCpPeriksaDAOImpl().GetPajakSKPDKB(txtNoSPTPD.getText(), wp.npwp);
				denda = ControllerFactory.getMainController().getCpPeriksaDAOImpl().GetDendaSKPDKB(txtNoSPTPD.getText(), wp.npwp);
			}
			denda += ControllerFactory.getMainController().getCpPeriksaDAOImpl().GetTotalDenda(txtNoSPTPD.getText(), wp.npwp);
			dendaTambahan = ControllerFactory.getMainController().getCpPeriksaDAOImpl().GetKenaikanSKPDKB(txtNoSPTPD.getText(), wp.npwp);
			dendaTerlambatSKPDKB = ControllerFactory.getMainController().getCpPeriksaDAOImpl().GetDendaTerlambatSKPDKB(txtNoSPTPD.getText(), wp.npwp);
			bulanSKP = listMasaPajak.get(cmbMasaPajak.getSelectionIndex()).getMasaPajakDari().getMonth()  + 1 + "-" + (listMasaPajak.get(cmbMasaPajak.getSelectionIndex()).getMasaPajakDari().getYear() + 1900);
			bulanSKP = bulanSKP.length() == 7 ? bulanSKP : '0' + bulanSKP;
			Date terbit = listMasaPajak.get(cmbMasaPajak.getSelectionIndex()).getTanggalSPTPD();
			dateTerbit.setDate(terbit.getYear() + 1900, terbit.getMonth(), terbit.getDate());
//			Date tglPeriksa = listMasaPajak.get(cmbMasaPajak.getSelectionIndex()).getTanggalNPPD();
			//Pembulatan angka SKPDKB untuk terbit SKPDKB mulai 2 September 2017
//			Date batas = new Date(117, 9, 21);
//			if (tglPeriksa.after(batas)){
				pokokPajak = roundUP(pokokPajak);
				denda = roundUP(denda);
				dendaTambahan = roundUP(dendaTambahan);
//			}
			if (!keteranganSKPDKB.equalsIgnoreCase("")){
				lblKeterangan.setText(lblKeterangan.getText() + "\r\n" + keteranganSKPDKB);
				lblKeterangan.setVisible(true);
			}
			else{
				if (wp.getStatus().equalsIgnoreCase("Aktif")){
					lblKeterangan.setVisible(false);
					lblKeterangan.setText("");
				}
			}
			String[] listTable = new String[]{"Pokok Pajak","Denda","Denda Tambahan","Denda Terlambat SKPDKB"};
			Double[] listDouble = new Double[]{pokokPajak,denda,dendaTambahan,dendaTerlambatSKPDKB};
			for(int i=0;i<4;i++){
				TableItem item = new TableItem(table, SWT.NONE);
				item.setText(new String[]{listTable[i],indFormat.format(listDouble[i])});
				totalPajakTerutang += listDouble[i];
			}
			if (btnAngsuran.getSelection())
				loadAngsuran();
		}
		else if (txtNoSPTPD.getText().contains("/SKPDKBT/"))
		{
			if (wp.getStatus().equalsIgnoreCase("Aktif")){
				lblKeterangan.setVisible(false);
				lblKeterangan.setText("");
			}
			idPeriksa = ControllerFactory.getMainController().getCpPeriksaDAOImpl().GetIdPeriksa(txtNoSPTPD.getText(), wp.getNPWP());
			dendaTambahan = ControllerFactory.getMainController().getCpPeriksaDAOImpl().GetKenaikanSKPDKB(txtNoSPTPD.getText(), wp.npwp);
			dendaTerlambatSKPDKB = ControllerFactory.getMainController().getCpPeriksaDAOImpl().GetDendaTerlambatSKPDKB(txtNoSPTPD.getText(), wp.npwp);
			listPD = ControllerFactory.getMainController().getCpPeriksaDAOImpl().GetPajakSKPDKBT(idPeriksa, wp.getNPWP());
			bulanSKP = listMasaPajak.get(cmbMasaPajak.getSelectionIndex()).getMasaPajakDari().getMonth() + 1 + "-" + (listMasaPajak.get(cmbMasaPajak.getSelectionIndex()).getMasaPajakDari().getYear() + 1900);
			bulanSKP = bulanSKP.length() == 7 ? bulanSKP : '0' + bulanSKP;
			Date terbit = listMasaPajak.get(cmbMasaPajak.getSelectionIndex()).getTanggalSPTPD();
			dateTerbit.setDate(terbit.getYear() + 1900, terbit.getMonth(), terbit.getDate());
			for (int i=0;i<listPD.size();i++){
				pokokPajak += listPD.get(i).getSelisihKurang() * tarif / 100;
				kenaikan += listPD.get(i).getKenaikanPajak();
			}
			denda = ControllerFactory.getMainController().getCpPeriksaDAOImpl().GetDendaSKPDKB(txtNoSPTPD.getText(), wp.npwp);
			String[] listTable = new String[]{"Pokok Pajak","Kenaikan 100%","Denda","Denda Terlambat SKPDKB"};
			Double[] listDouble = new Double[]{pokokPajak,kenaikan,denda,dendaTerlambatSKPDKB};
			for(int i=0;i<4;i++){
				TableItem item = new TableItem(table, SWT.NONE);
				item.setText(new String[]{listTable[i],indFormat.format(listDouble[i])});
				totalPajakTerutang += listDouble[i];
			}
			if (btnAngsuran.getSelection())
				loadAngsuran();
		}
		else if (txtNoSPTPD.getText().contains("/SPTPD/"))
		{
			if (wp.getStatus().equalsIgnoreCase("Aktif")){
				lblKeterangan.setVisible(false);
				lblKeterangan.setText("");
			}
			spt = ControllerFactory.getMainController().getCpSptpdDAOImpl().getSPTPD(wp.getNPWP(), txtNoSPTPD.getText());
			idSPTPD = spt.getID();
			dateTerbit.setDate(spt.getTanggalSPTPD().getYear() + 1900, spt.getTanggalSPTPD().getMonth(), spt.getTanggalSPTPD().getDate());
			pokokPajak = spt.getPajakterutang();
			denda = 0;
			dendaTambahan = spt.getDendaTambahan() == 1 ? 0 : spt.getDendaTambahan();
			bulanSKP = listMasaPajak.get(cmbMasaPajak.getSelectionIndex()).getMasaPajakDari().getMonth() + 1 + "-" + (listMasaPajak.get(cmbMasaPajak.getSelectionIndex()).getMasaPajakDari().getYear() + 1900);
			bulanSKP = bulanSKP.length() == 7 ? bulanSKP : '0' + bulanSKP;
			String[] listTable = new String[]{"Pokok Pajak","Denda Tambahan"};
			Double[] listDouble = new Double[]{pokokPajak,dendaTambahan};
			for(int i=0;i<2;i++){
				TableItem item = new TableItem(table, SWT.NONE);
				item.setText(new String[]{listTable[i],indFormat.format(listDouble[i])});
				totalPajakTerutang += listDouble[i];
			}
			
		}
		else if (txtNoSPTPD.getText().contains("/SKPD/"))
		{
			if (wp.getStatus().equalsIgnoreCase("Aktif")){
				lblKeterangan.setVisible(false);
				lblKeterangan.setText("");
			}
			
			spt = ControllerFactory.getMainController().getCpSptpdDAOImpl().getSPTPD(wp.getNPWP(), txtNoSPTPD.getText());
			idSPTPD = spt.getID();
			dateTerbit.setDate(spt.getTanggalSPTPD().getYear() + 1900, spt.getTanggalSPTPD().getMonth(), spt.getTanggalSPTPD().getDate());
			pokokPajak = spt.getPajakterutang();
			denda = 0;
			dendaTambahan = 0; //spt.getDendaTambahan() == 1 ? 0 : spt.getDendaTambahan();
			bulanSKP = listMasaPajak.get(cmbMasaPajak.getSelectionIndex()).getMasaPajakDari().getMonth() + 1 + "-" + (listMasaPajak.get(cmbMasaPajak.getSelectionIndex()).getMasaPajakDari().getYear() + 1900);
			bulanSKP = bulanSKP.length() == 7 ? bulanSKP : '0' + bulanSKP;

			if (txtKodePajak.getText().equalsIgnoreCase("7"))
			{
				txtLokasi.setVisible(true);
				txtLokasi.setText(spt.getLokasi());
			}
			else if ((!wp.getNPWP().substring(0, 1).equalsIgnoreCase("2")) && listMasaPajak.get(cmbMasaPajak.getSelectionIndex()).getMasaPajakDari().getMonth() != listMasaPajak.get(cmbMasaPajak.getSelectionIndex()).getMasaPajakSampai().getMonth())
			{
				pokokPajak = spt.getPajakterutang() / 3;
				loadSKPD();
				btnBayar.setEnabled(false);
				lblLunas.setVisible(false);
			}
			String[] listTable = new String[]{"Pokok Pajak","Denda Tambahan"};
			Double[] listDouble = new Double[]{pokokPajak,dendaTambahan};
			for(int i=0;i<2;i++){
				TableItem item = new TableItem(table, SWT.NONE);
				item.setText(new String[]{listTable[i],indFormat.format(listDouble[i])});
				totalPajakTerutang += listDouble[i];
			}
			
		}
		hitungPajak();
		objSspd = ControllerFactory.getMainController().getCpSspdDAOImpl().getSSPD(wp.getNPWP(), txtNoSPTPD.getText());
		
		if (objSspd != null)
		{
			if (objSspd.getTglCetak() != null)
			{
				datePembayaran.setDate(objSspd.getTglSspd().getYear() + 1900, objSspd.getTglSspd().getMonth(), objSspd.getTglSspd().getDate());
				if (objSspd.getTglCetak() != null)
				{
					dateCetak.setDate(objSspd.getTglCetak().getYear() + 1900, objSspd.getTglCetak().getMonth(), objSspd.getTglCetak().getDate());
				}
				else
					dateCetak.setDate(objSspd.getTglSspd().getYear() + 1900, objSspd.getTglSspd().getMonth(), objSspd.getTglSspd().getDate());
			}
			else {
				datePembayaran.setDate(dateNow.getYear() + 1900, dateNow.getMonth(), dateNow.getDate());
				dateCetak.setDate(dateNow.getYear() + 1900, dateNow.getMonth(), dateNow.getDate());
				}
			
			tempNota = objSspd.getNotaBayar();
			selectedID = objSspd.getIdSspd();
			
			if (selectedID != null){
				if (selectedID == 0)
					selectedID = null;
			}
			
			if (jenisBayar == 0)
			{
				if (objSspd.getLunas())
				{
					lblLunas.setVisible(true);
					lblNoSspd.setText("No SSPD");
					txtNoSSPD.setText(objSspd.getNoSspd());
//					btnCetakSts.setVisible(true);
					CurrencyAmount currencyAmount = new CurrencyAmount(objSspd.getJumlahBayar(),Currency.getInstance(ind));
					txtDibayar.setMoney(currencyAmount);
					txtNamaPenyetor.setText(objSspd.getNamaPenyetor());
					compPejabat.setVisible(true);
//					loadPejabat();
					
					if (objSspd.getCaraBayar().equalsIgnoreCase("Tunai")){
						btnTunai.setSelection(true);
						btnGiro.setSelection(false);
						btnBank.setSelection(false);
						dateCetak.setEnabled(false);
					}
					else if (objSspd.getCaraBayar().equalsIgnoreCase("Giro")){
						btnGiro.setSelection(true);
						btnTunai.setSelection(false);
						btnBank.setSelection(false);
						dateCetak.setEnabled(true);
					}
					else if (objSspd.getCaraBayar().equalsIgnoreCase("Bank")){
						btnBank.setSelection(true);
						btnGiro.setSelection(false);
						btnTunai.setSelection(false);
					}
//					txtDibayar.setText(indFormat.format(objSspd.getJumlahBayar()).toString());
				}
				else if (objSspd.getNotaBayar() != null)
				{
					if (userModul.getIdUser()==1)
						btnHapus.setEnabled(true);
					btnCetakSts.setVisible(true);
//					lblLunas.setVisible(false);
					lblNoSspd.setText("Nota Bayar");
					if (objSspd.getNotaBayar() != null)
						txtNoSSPD.setText(objSspd.getNotaBayar());
					CurrencyAmount currencyAmount = new CurrencyAmount(objSspd.getJumlahBayar(),Currency.getInstance(ind));
					txtDibayar.setMoney(currencyAmount);
					txtNamaPenyetor.setText(objSspd.getNamaPenyetor());
					compPejabat.setVisible(true);
					
					if (objSspd.getCaraBayar().equalsIgnoreCase("Tunai")){
						btnTunai.setSelection(true);
						btnGiro.setSelection(false);
						btnBank.setSelection(false);
					}
					else if (objSspd.getCaraBayar().equalsIgnoreCase("Giro")){
						btnGiro.setSelection(true);
						btnTunai.setSelection(false);
						btnBank.setSelection(false);
					}
					else if (objSspd.getCaraBayar().equalsIgnoreCase("Bank")){
						btnBank.setSelection(true);
						btnGiro.setSelection(false);
						btnTunai.setSelection(false);
					}
				}
				else
				{
					compPejabat.setVisible(false);
					lblLunas.setVisible(false);
					lblNoSspd.setText("Nota Bayar");
					if (objSspd.getNotaBayar() != null)
						txtNoSSPD.setText(objSspd.getNotaBayar());
//					CurrencyAmount currencyAmount = new CurrencyAmount(totalPajakTerutang, Currency.getInstance(ind));
					txtDibayar.setMoney(txtTotalPajakTerhutang.getMoney());
					txtNamaPenyetor.setText(wp.getNamaPemilik());
					btnTunai.setSelection(true);
					datePembayaran.setDate(dateNow.getYear() + 1900, dateNow.getMonth(), dateNow.getDate());
					dateCetak.setDate(dateNow.getYear() + 1900, dateNow.getMonth(), dateNow.getDate());
				}
				
				if (objSspd.getDendaLunas())
					dendaSSPD = objSspd.getDenda();
				else
					dendaSSPD = hitungDendaSSPD(pokokPajak);
				TableItem itemSSPD;
				if (btnTepatWaktu.getSelection())
					itemSSPD = new TableItem(table, SWT.NONE);
				else
					itemSSPD = new TableItem(tblCicilan, SWT.NONE);
				itemSSPD.setText(new String[]{"Denda SSPD",indFormat.format(dendaSSPD)});
				totalPajakTerutang += dendaSSPD;
//				DecimalFormat df = new DecimalFormat("#.##");
//				df.setMaximumFractionDigits(2);
//				df.setRoundingMode(RoundingMode.HALF_EVEN);
				totalPajakTerutang = round(totalPajakTerutang);
				txtTotalPajakTerhutang.setMoney(new CurrencyAmount(totalPajakTerutang, Currency.getInstance("ind")));
//				if (!objSspd.getLunas())
//					txtDibayar.setMoney(new CurrencyAmount(totalPajakTerutang, Currency.getInstance("ind")));
				
				//cek skpd 3 bulan
				if (tableAngsuran.getVisible() && tableAngsuran.getColumnCount() == 1){
					selectedID = null;
					txtNoSSPD.setText("");
					lblNoSspd.setText("No SSPD");
					lblLunas.setVisible(false);
					totalPajakTerutang -= dendaSSPD;
					txtTotalPajakTerhutang.setMoney(new CurrencyAmount(totalPajakTerutang, Currency.getInstance("ind")));
					txtDibayar.setMoney(new CurrencyAmount(0, Currency.getInstance("ind")));
					txtNamaPenyetor.setText("");
					TableItem ti = table.getItem(table.getItemCount()-1);
					ti.setText(new String[]{"Denda SSPD", indFormat.format(0)});
				}
			}
			else
			{
				datePembayaran.setDate(dateNow.getYear() + 1900, dateNow.getMonth(), dateNow.getDate());
				dateCetak.setDate(dateNow.getYear() + 1900, dateNow.getMonth(), dateNow.getDate());
				selectedID = null;
				if (lblLunas.getVisible()){
					btnBayar.setEnabled(false);
					btnHapus.setEnabled(false);
					btnCetakSts.setVisible(false);
					txtDibayar.setMoney(new CurrencyAmount(0, Currency.getInstance(ind)));
					txtNamaPenyetor.setText("");
				}
				else{
					txtNamaPenyetor.setText(wp.getNamaPemilik());
					TableItem itemSSPD = new TableItem(tblCicilan, SWT.NONE);
					itemSSPD.setText(new String[]{"Denda SSPD",indFormat.format(dendaSSPD)});
				}
					
			}
		}
		if (lblLunas.getVisible())
			btnGenerateSts.setEnabled(false);
		
		if (userModul.getIdUser() == 1)
			btnHapus.setEnabled(true);
//		hitungPajak();
//		dendaSSPD = hitungDendaSSPD(pokokPajak);
//		TableItem itemSSPD;
//		if (btnTepatWaktu.getSelection())
//			itemSSPD = new TableItem(table, SWT.NONE);
//		else
//			itemSSPD = new TableItem(tblCicilan, SWT.NONE);
//		itemSSPD.setText(new String[]{"Denda SSPD",indFormat.format(dendaSSPD)});
//		totalPajakTerutang += dendaSSPD;
//		txtTotalPajakTerhutang.setMoney(new CurrencyAmount(totalPajakTerutang, Currency.getInstance("ind")));
		if (userModul.getIdUser() == 1 || userModul.getIdUser() == 99)
			if (!txtNoSPTPD.getText().contains("SKPDKB"))
				btnHapusSts.setVisible(true);
			else
				btnHapusSts.setVisible(false);
	}
	
	private void totalAngsurChanged(){
		txtSisa.setMoney(new CurrencyAmount((txtTotalPajakTerhutang.getMoney().getNumber().doubleValue() - txtTotalAngsur.getMoney().getNumber().doubleValue()), Currency.getInstance("ind")));
		if (txtSisa.getMoney().getNumber().doubleValue() < 0){
			txtSisa.setMoney(new CurrencyAmount(0, Currency.getInstance("ind")));
		}
		if (lblLunas.getVisible() == false){
			if (angsuranTerakhir <= tableAngsuran.getSelectionIndex() + 1){
				txtDibayar.setMoney(new CurrencyAmount(roundUP(txtSisa.getMoney().getNumber().doubleValue()), Currency.getInstance(ind)));
			}
			else if (angsuranTerakhir == tableAngsuran.getItemCount() + 1){
				try{
					if (tableAngsuran.getSelectionIndex() >= 0)
						txtDibayar.setMoney(new CurrencyAmount(roundUP(indFormat.parse(tableAngsuran.getItem(tableAngsuran.getSelectionIndex()).getText(1)).doubleValue()), Currency.getInstance(ind)));
					else
						txtDibayar.setMoney(new CurrencyAmount(roundUP(txtSisa.getMoney().getNumber().doubleValue()), Currency.getInstance(ind)));
				}
				catch (ParseException ex){
					
				}
			}
			else{
//				DecimalFormat df = new DecimalFormat("#.##");
//				df.setMaximumFractionDigits(2);
//				df.setRoundingMode(RoundingMode.HALF_EVEN);
				try{
//					try {
//						txtDibayar.setMoney(new CurrencyAmount(indFormat.parse(tableAngsuran.getItem(index).getText(1)).doubleValue(), Currency.getInstance(ind)));
//					} catch (ParseException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
					if (objSspd != null){
						if ((objSspd.getLunas() || objSspd.getNotaBayar() != null) && tableAngsuran.getSelectionIndex() >= 0){
//							if (tableAngsuran.getSelectionIndex() >= 0)
								txtDibayar.setMoney(new CurrencyAmount(indFormat.parse(tableAngsuran.getItem(tableAngsuran.getSelectionIndex()).getText(1)).doubleValue(), Currency.getInstance(ind)));
//							else
//								txtDibayar.setMoney(txtSisa.getMoney());
//							CurrencyAmount currencyAmount = new CurrencyAmount(objSspd.getJumlahBayar(),Currency.getInstance(ind));
//							txtDibayar.setMoney(currencyAmount);
							
						}
						else
							txtDibayar.setMoney(new CurrencyAmount(round(pajakAngsuran) + dendaSSPD, Currency.getInstance("ind")));
					}else
						txtDibayar.setMoney(new CurrencyAmount(round(pajakAngsuran) + dendaSSPD, Currency.getInstance("ind")));
				}
				catch (Exception ex){
					System.out.print(ex.toString());
				}
			}
		}
	}
	
	private void deleteColumns(){
		tableAngsuran.removeAll();
		for(TableColumn col : tableAngsuran.getColumns()){
			col.dispose();
		}
//		for(TableItem item : tableAngsuran.getItems()){
//			item.dispose();
//		}
	}
	
	private void createColumnAngsur(){
		deleteColumns();
		if (tableAngsuran.getColumnCount() <= 0){
			String[] listText = new String[]{"Cicilan Ke","Jumlah Bayar","Tanggal SSPD","No SSPD","No SKP","Denda","Lunas Denda"};
			int[] listWidth = new int[]{70,140,100,130,130,120,80};
			for(int i=0;i<7;i++){
				TableColumn colTableAngsuran = new TableColumn(tableAngsuran, SWT.NONE);
				colTableAngsuran.setText(listText[i]);
				colTableAngsuran.setWidth(listWidth[i]);
			}
		}
	}
	
	private void createColumnSKPD(){
		deleteColumns();
		TableColumn colTableAngsuran = new TableColumn(tableAngsuran, SWT.NONE);
		colTableAngsuran.setText("Masa Pajak");
		colTableAngsuran.setWidth(150);
	}
	
	@SuppressWarnings("deprecation")
	private void loadPerhitunganAngsuran(){
		if (btnAngsuran.getSelection()){
			int idMohon = ControllerFactory.getMainController().getCpMohonDAOImpl().checkMohon(wp.getNPWP(), txtNoSPTPD.getText());
			int index = tableAngsuran.getSelectionIndex();
			cicilanKe = Integer.parseInt(tableAngsuran.getItem(index).getText(0));
			String sts = ControllerFactory.getMainController().getCpSptpdDAOImpl().getNoSTSAngsuran(idMohon, cicilanKe);
			if (sts.equalsIgnoreCase("") || sts == null){
				txtNoSTS.setText("");
				dtValid.setDate(dateNow.getYear()+1900, dateNow.getMonth(), dateNow.getDate());
			}else{
				txtNoSTS.setText(sts);
			}
			btnGenerateSts.setEnabled(false);
			Double totalAngsur = 0.0;
			totalDendaAngsuran = 0;
			tblCicilan.removeAll();			
			for(TableItem ti : tableAngsuran.getItems()){
				if (Integer.parseInt(ti.getText(0)) == cicilanKe)
					break;
				try {
					totalAngsur += indFormat.parse(ti.getText(1)).doubleValue();
					totalDendaAngsuran += indFormat.parse(ti.getText(5)).doubleValue();
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
			}
			angsuranLunas = false;
			loadTanggaljatuhTempo();
			selectedID = IDSSPDAngsur[index];
			loadSSPD(IDSSPDAngsur[index]);
			for(TableItem ti : table.getItems()){
				if (ti.getText(0).equalsIgnoreCase("Denda Terlambat Sebelumnya"))
					ti.setText(1, indFormat.format(totalDendaAngsuran));
			}
			
			txtTotalAngsur.setMoney(new CurrencyAmount(totalAngsur, Currency.getInstance(ind)));
			
			hitungPajak();
			dendaSSPD = hitungDendaSSPD(pokokPajak);
//			for(TableItem ti : tblCicilan.getItems()){
//				if (ti.getText(0).equalsIgnoreCase("Denda SSPD"))
//					ti.setText(1, indFormat.format(totalDendaAngsuran));
//			}
			TableItem itemSSPD = new TableItem(tblCicilan, SWT.NONE);
			itemSSPD.setText(new String[]{"Denda SSPD", indFormat.format(dendaSSPD)});
			if (lblNoSspd.getText().equalsIgnoreCase("Nota Bayar")){
				if(userModul.getIdUser()==1)
					btnHapus.setEnabled(true);
				btnBayar.setEnabled(true);
				compPejabat.setVisible(true);
//				btnCetakSts.setVisible(true);
			}
			else if (txtNoSSPD.getText().contains("SSPD")){
				btnBayar.setEnabled(true);
				btnHapus.setEnabled(false);
				compPejabat.setVisible(true);
//				btnCetakSts.setVisible(true);
			}
			else{
				btnBayar.setEnabled(false);
				btnHapus.setEnabled(false);
				compPejabat.setVisible(false);
				btnCetakSts.setVisible(false);
			}
			
//			totalPajakTerutang = pokokPajak + dendaSSPD;
//			txtTotalPajakTerhutang.setMoney(new CurrencyAmount(totalPajakTerutang, Currency.getInstance("ind")));
		}
		else{
			selectedID = null;
			int index = tableAngsuran.getSelectionIndex();
			String bulanSKPD = String.valueOf(listMasaPajak.get(cmbMasaPajak.getSelectionIndex()).getMasaPajakDari().getMonth() + 1 + index) + "-" +  (listMasaPajak.get(cmbMasaPajak.getSelectionIndex()).getMasaPajakDari().getYear() + 1900);
			getSKPD(bulanSKPD);
			btnBayar.setEnabled(true);
		}
	}
	
	private UserModul userModul = ControllerFactory.getMainController().getCpUserDAOImpl()
			.getHMUserModul(GlobalVariable.userModul.getIdUser()).get(6); // 2 merupakan id sub modul.
	private Label lblSisaDenda;
	private MoneyField txtSisaDendaDibayar;
	private Button btnBayarDenda;
	private Text txtNoReg;
	private Label lblNoReg;
	private Button btnCetakDenda;
	private Text txtNoSKPLama;
	private Label lblSlash;
	private Label lblTanggalTerbit;
	private DateTime dateTerbit;
	private Button btnNewButton;
	private Label lblNoSts;
	private Text txtNoSTS;
	private Label lblValid;
	private DateTime dtValid;
	private Button btnGenerateSts;
	private Button btnCetakSts;
	private Button btnCetakStsDenda;
	private Button btnGenerateStsDenda;
	private Label lblAngsuranKe;
	private Combo cmbAngsuran;
	private Button btnHapusSts;
	private Button btnDendaManual;
	private Text txtDendaManual;
	
	private boolean isSave(){		
		return userModul.getSimpan();
	}
	
	private boolean isDelete(){		
		return userModul.getHapus();
	}
	
	private boolean isPrint(){		
		return userModul.getCetak();
	}
	
	private void checkSisaDenda(){
		if (txtDibayar.getMoney() != null){
			if(txtTotalPajakTerhutang.getMoney().getNumber().doubleValue() - txtDibayar.getMoney().getNumber().doubleValue() > 1){
				if(!btnAngsuran.getSelection()){
					lblSisaDenda.setVisible(true);
					txtSisaDendaDibayar.setVisible(true);
//					btnBayarDenda.setVisible(true);
					btnGenerateStsDenda.setVisible(true);
					txtSisaDendaDibayar.setMoney(new CurrencyAmount(txtTotalPajakTerhutang.getMoney().getNumber().doubleValue() - txtDibayar.getMoney().getNumber().doubleValue(), Currency.getInstance(ind)));
				}else{
					lblSisaDenda.setVisible(false);
					txtSisaDendaDibayar.setVisible(false);
//					btnBayarDenda.setVisible(false);
					btnGenerateStsDenda.setVisible(false);
					txtSisaDendaDibayar.setMoney(new CurrencyAmount(0.0, Currency.getInstance(ind)));
				}
			}else{
				lblSisaDenda.setVisible(false);
				txtSisaDendaDibayar.setVisible(false);
//				btnBayarDenda.setVisible(false);
				btnGenerateStsDenda.setVisible(false);
				txtSisaDendaDibayar.setMoney(new CurrencyAmount(0.0, Currency.getInstance(ind)));
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	private boolean cekTglSPT(){
		if (listMasaPajak.get(cmbMasaPajak.getSelectionIndex()).getTanggalSPTPD().after(new Date(datePembayaran.getYear() - 1900, datePembayaran.getMonth(), datePembayaran.getDay())))
			return false;
		
		return true;
	}
	
	private static double round(double value) {
	    //if (places < 0) throw new IllegalArgumentException();

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
	
	@SuppressWarnings("deprecation")
	private void saveShortcut(){
		if(isSave()){
			if(isValidate()){
//					String noSSPD = "";
				String notaBayar = tempNota;
				dateNow = (java.sql.Timestamp)ControllerFactory.getMainController().getCpWajibPajakDAOImpl().getDateNow();
				SimpleDateFormat sdf = new SimpleDateFormat("ddMMyy");
				java.util.Date tglSSPD = new java.util.Date(datePembayaran.getYear() - 1900, datePembayaran.getMonth(), datePembayaran.getDay(), dateNow.getHours(), dateNow.getMinutes(), dateNow.getSeconds());
				java.util.Date tglCetak = new java.util.Date(dateCetak.getYear() - 1900, dateCetak.getMonth(), dateCetak.getDay(), dateNow.getHours(), dateNow.getMinutes(), dateNow.getSeconds());
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				Sspd sspd = new Sspd();
				sspd.setIdSspd(selectedID);
				if (selectedID == null){
					notaBayar = ControllerFactory.getMainController().getCpSspdDAOImpl().getNotaBayar(sdf.format(dateNow));
					cicilanKe = tableAngsuran.getItemCount() + 1; 
					//noSSPD = ControllerFactory.getMainController().getCpSspdDAOImpl().getNoSSPD(String.valueOf(datePembayaran.getYear()));
				}
				else{
					if (txtNoSSPD.getText().substring(txtNoSSPD.getText().length() - 6).equalsIgnoreCase(sdf.format(dateNow)))
						notaBayar = txtNoSSPD.getText();
					if (btnAngsuran.getSelection())
						cicilanKe = tableAngsuran.getSelectionIndex()+1;
//						cicilanKe = tableAngsuran.getItemCount() 
//					if (! notaBayar.substring(notaBayar.length() - 6).equalsIgnoreCase(sdf.format(dateNow)))
//					else
//						notaBayar = ""; //ControllerFactory.getMainController().getCpSspdDAOImpl().getNotaBayar(sdf.format(dateNow));
				}
				String datesspd = dateFormat.format(tglSSPD);
				sspd.setTglSspd(java.sql.Timestamp.valueOf(datesspd));
				String datecetak = dateFormat.format(tglCetak);
				sspd.setTglCetak(java.sql.Timestamp.valueOf(datecetak));
//					sspd.setTglSspd(new Date(datePembayaran.getYear() - 1900, datePembayaran.getMonth(), datePembayaran.getDay()));
//					sspd.setTglCetak(new Date(dateCetak.getYear() - 1900, dateCetak.getMonth(), dateCetak.getDay()));
				sspd.setNpwpd(wp.getNPWP());
				sspd.setBulanSkp(bulanSKP);
				sspd.setNoSkp(txtNoSPTPD.getText());
				if (txtNoSPTPD.getText().contains("SPTPD"))
					sspd.setDenda(dendaSSPD + dendaTambahan);
				else
					sspd.setDenda(dendaSSPD);
				
				if (btnDendaManual.getSelection()){
					dendaSSPD = Double.valueOf(txtDendaManual.getText());
					sspd.setDenda(dendaSSPD);
				}
//				DecimalFormat df = new DecimalFormat("#.##");
//				df.setMaximumFractionDigits(2);
//				df.setRoundingMode(RoundingMode.HALF_EVEN);
				//totalPajakTerutang = Double.parseDouble(df.format(totalPajakTerutang).replace(',', '.'));
				
				sspd.setJumlahBayar(round(txtDibayar.getMoney().getNumber().doubleValue()));
				
				if (btnDendaManual.getSelection()){
					sspd.setJumlahBayar(round(pokokPajak+dendaSSPD));
				}
				if (btnTunai.getSelection())
					caraBayar = "Tunai";
				else if (btnGiro.getSelection())
					caraBayar = "Giro";
				else if (btnBank.getSelection())
					caraBayar = "Bank";
				sspd.setCaraBayar(caraBayar);		
				sspd.setNamaPenyetor(txtNamaPenyetor.getText());
				sspd.setIdSubPajak(idSubPajak);
				sspd.setIdSPTPD(idSPTPD);
				sspd.setIdPeriksa(idPeriksa);
				if (btnTepatWaktu.getSelection()) {
					jenisBayar = 0;
					cicilanKe = 0;
					// di set saat dibayar di listDaftarAntrian
					if (txtDibayar.getMoney().getNumber().doubleValue() >= txtTotalPajakTerhutang.getMoney().getNumber().doubleValue()) {
						sspd.setLunas(true);
						sspd.setDendaLunas(true);
					}
					else if (txtDibayar.getMoney().getNumber().doubleValue() >= pokokPajak){
						sspd.setLunas(true);
						sspd.setDendaLunas(false);
					}
					else {
//							sspd.setLunas(false);
						sspd.setDendaLunas(false);
					}
						
				}
				else {
					jenisBayar = 2;
					sspd.setLunas(true);
					try {
						if (indFormat.parse(tblCicilan.getItem(0).getText(1)).doubleValue() + indFormat.parse(tblCicilan.getItem(1).getText(1)).doubleValue() - txtDibayar.getMoney().getNumber().doubleValue() <= 1){
							if (indFormat.parse(tblCicilan.getItem(0).getText(1)).doubleValue() + indFormat.parse(tblCicilan.getItem(1).getText(1)).doubleValue() - txtDibayar.getMoney().getNumber().doubleValue() >= -1 &&
								indFormat.parse(tblCicilan.getItem(0).getText(1)).doubleValue() + indFormat.parse(tblCicilan.getItem(1).getText(1)).doubleValue() - txtDibayar.getMoney().getNumber().doubleValue() <= 1){
								sspd.setDendaLunas(true);
							}
							else{
								sspd.setDendaLunas(true);
								double sisaDenda = txtDibayar.getMoney().getNumber().doubleValue() - (indFormat.parse(tblCicilan.getItem(0).getText(1)).doubleValue() + indFormat.parse(tblCicilan.getItem(1).getText(1)).doubleValue());
								for (int i=0;i<tableAngsuran.getItemCount();i++){
									if (sisaDenda - updDenda[i] >= 0){
										ControllerFactory.getMainController().getCpSspdDAOImpl().updateDendaSebelumnya(IDSSPDAngsur[i]);
										sisaDenda -= updDenda[i];
									}
								}
							}
						}
						else if (indFormat.parse(tblCicilan.getItem(0).getText(1)).doubleValue() - txtDibayar.getMoney().getNumber().doubleValue() <= 1){
							sspd.setDendaLunas(false);
						}
						
					} catch (ParseException e1) {
						e1.printStackTrace();
					}
					
//						if (angsuranTerakhir == cicilanKe)
				}
				sspd.setJenisBayar(jenisBayar);
				sspd.setCicilanKe(cicilanKe);
				Integer tahun = tglCetak.getYear() + 1900;
				sspd.setNoSspd(ControllerFactory.getMainController().getCpSspdDAOImpl().getNoSSPD(tahun.toString()));
				sspd.setNotaBayar(notaBayar);
				
//				if (angsuranTerakhir == cicilanKe){
//					if (txtTotalPajakTerhutang.getMoney().getNumber().doubleValue() - (txtTotalAngsur.getMoney().getNumber().doubleValue() + txtDibayar.getMoney().getNumber().doubleValue()) < 0.01){
				//if (tahun >= 2018){
					//MessageDialog.openError(Display.getCurrent().getActiveShell(), "Save", "Cek Tanggal Pembayaran");
				//}
				//else{
				if (btnDendaManual.getSelection()){
					if(ControllerFactory.getMainController().getCpSspdDAOImpl().saveSspd(sspd)){
							MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Data berhasil disimpan.");
							if (selectedID == null) {
								selectedID = ControllerFactory.getMainController().getCpSspdDAOImpl().getLastId(wp.getNPWP());
								txtNoSSPD.setText(notaBayar);
								lblNoSspd.setText("Nota Bayar");
							}
							lblLunas.setVisible(false);
							compPejabat.setVisible(true);
							loadPejabat();
							if (btnAngsuran.getSelection()){
								loadAngsuran();
								tblCicilan.remove(tblCicilan.getItemCount() - 1);
								table.remove(table.getItemCount() - 1);
								tableAngsuran.select(tableAngsuran.getItemCount() - 1);
								loadPerhitunganAngsuran();
							}
							StringBuffer sb = new StringBuffer();
							sb.append("SAVE " +
									"SSPD:"+txtNoSSPD.getText()+
									" SPTPD:"+txtNoSPTPD.getText()+
									" NPWPD:"+wp.getNPWP()+
									" TanggalSSPD:"+tglSSPD+
									" MasaPajak:"+cmbMasaPajak.getText()+
									" JumlahBayar:"+String.valueOf(pokokPajak+dendaSSPD)+
									" PajakTerhutang:"+String.valueOf(pokokPajak+dendaSSPD));
							new LogImp().setLog(sb.toString(), userModul.getDisplayName(), userModul.getSubModul().getNamaModul());
						}else{
							MessageDialog.openError(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Error pada Database. Silahkan hubungi ADMIN.");
						}
				}
				else{
					if(ControllerFactory.getMainController().getCpSspdDAOImpl().saveSspd(sspd)){
						MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Data berhasil disimpan.");
						if (selectedID == null) {
							selectedID = ControllerFactory.getMainController().getCpSspdDAOImpl().getLastId(wp.getNPWP());
							txtNoSSPD.setText(notaBayar);
							lblNoSspd.setText("Nota Bayar");
						}
						lblLunas.setVisible(false);
						compPejabat.setVisible(true);
						loadPejabat();
						if (btnAngsuran.getSelection()){
							loadAngsuran();
							tblCicilan.remove(tblCicilan.getItemCount() - 1);
							table.remove(table.getItemCount() - 1);
							tableAngsuran.select(tableAngsuran.getItemCount() - 1);
							loadPerhitunganAngsuran();
						}
						StringBuffer sb = new StringBuffer();
						sb.append("SAVE " +
								"SSPD:"+txtNoSSPD.getText()+
								" SPTPD:"+txtNoSPTPD.getText()+
								" NPWPD:"+wp.getNPWP()+
								" TanggalSSPD:"+tglSSPD+
								" MasaPajak:"+cmbMasaPajak.getText()+
								" JumlahBayar:"+txtDibayar.getText().getText()+
								" PajakTerhutang:"+txtTotalPajakTerhutang.getText().getText());
						new LogImp().setLog(sb.toString(), userModul.getDisplayName(), userModul.getSubModul().getNamaModul());
					}else{
						MessageDialog.openError(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Error pada Database. Silahkan hubungi ADMIN.");
					}
				}
				//}
//					}
//					else{
//						MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "Dispenda Kota Medan", "Pembayaran tidak dapat disimpan karena masih ada sisa angsuran yang belum lunas");
//					}
//				}
			}else
				MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", pesanValidate);
		}else
			MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Anda tidak memiliki hak untuk menyimpan atau merubah data.");
	}
	
	private static double roundUP(double value) {
	    //if (places < 0) throw new IllegalArgumentException();

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
	
	@SuppressWarnings("deprecation")
	private String[] getParam(String skp){
		String[] retValue = new String[10];
		String nilai = "";
		String nilaiDenda = "";
		if (skp.equalsIgnoreCase("SPTPD")){
			nilai = indFormat.format(pokokPajak).substring(2).replace(".", "").replace(",", "."); //table.getItem(0).getText(2); //indFormat.format(txtPajakTerutang.getMoney().getNumber().doubleValue()).substring(2).replace(".", "").replace(",", ".");
			nilaiDenda = indFormat.format(dendaSSPD).substring(2).replace(".", "").replace(",", ".");
		}else if (skp.equalsIgnoreCase("SKPDKB")){
			nilai = indFormat.format(pokokPajak+dendaTambahan).substring(2).replace(".", "").replace(",", ".");
			nilaiDenda = indFormat.format(denda+dendaSSPD).substring(2).replace(".", "").replace(",", ".");
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdfSKP = new SimpleDateFormat("MMyy");
		Date terbit = new Date(dateTerbit.getYear()-1900, dateTerbit.getMonth(), dateTerbit.getDay());
		retValue[0] = wp.getNPWP();
		retValue[1] = txtNamaBadan.getText();
		retValue[2] = txtAlamat.getText();
		retValue[3] = nilai.substring(0, nilai.indexOf("."));
		retValue[4] = nilaiDenda.substring(0, nilaiDenda.indexOf("."));
		retValue[5] = txtNoSPTPD.getText();
		retValue[6] = sdf.format(terbit);
		retValue[7] = wp.getKode_bid_usaha();
		if (skp.equalsIgnoreCase("SPTPD"))
			retValue[8] = sdf.format(listMasaPajak.get(cmbMasaPajak.getSelectionIndex()).getMasaPajakSampai());
		else
			retValue[8] = sdfSKP.format(listMasaPajak.get(cmbMasaPajak.getSelectionIndex()).getMasaPajakDari()) + "-" + sdfSKP.format(listMasaPajak.get(cmbMasaPajak.getSelectionIndex()).getMasaPajakSampai()); 
		Timestamp jthTempo = new Timestamp(dateJatuhTempo.getYear()-1900, dateJatuhTempo.getMonth(), dateJatuhTempo.getDay(), 23, 59, 59, 99);
		if (dateNow.before(jthTempo))
			retValue[9] = sdf.format(jthTempo);
		else
			retValue[9] = sdf.format(dateNow);
		
		return retValue;
	}
	
	@SuppressWarnings("deprecation")
	private String[] getParamDenda(String skp){
		String[] retValue = new String[10];
		String nilaiDenda = indFormat.format(txtSisaDendaDibayar.getMoney().getNumber().doubleValue()).substring(2).replace(".", "").replace(",", "."); //indFormat.format(dendaSSPD).substring(2).replace(".", "").replace(",", ".");
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdfSKP = new SimpleDateFormat("MMyy");
		Date terbit = new Date(dateTerbit.getYear()-1900, dateTerbit.getMonth(), dateTerbit.getDay());
		retValue[0] = wp.getNPWP();
		retValue[1] = txtNamaBadan.getText();
		retValue[2] = txtAlamat.getText();
		retValue[3] = "0";
		retValue[4] = nilaiDenda.substring(0, nilaiDenda.indexOf("."));
		retValue[5] = txtNoSPTPD.getText();
		retValue[6] = sdf.format(terbit);
		retValue[7] = wp.getKode_bid_usaha();
		if (skp.equalsIgnoreCase("SPTPD"))
			retValue[8] = sdf.format(listMasaPajak.get(cmbMasaPajak.getSelectionIndex()).getMasaPajakSampai());
		else
			retValue[8] = sdfSKP.format(listMasaPajak.get(cmbMasaPajak.getSelectionIndex()).getMasaPajakDari()) + "-" + sdfSKP.format(listMasaPajak.get(cmbMasaPajak.getSelectionIndex()).getMasaPajakSampai()); 
		Timestamp jthTempo = new Timestamp(dateJatuhTempo.getYear()-1900, dateJatuhTempo.getMonth(), dateJatuhTempo.getDay(), 23, 59, 59, 99);
		if (dateNow.before(jthTempo))
			retValue[9] = sdf.format(jthTempo);
		else
			retValue[9] = sdf.format(dateNow);
		
		return retValue;
	}
	
	@SuppressWarnings("deprecation")
	private String[] getParamAngsuran(String nilai, String nilaiDenda){
		String[] retValue = new String[10];
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdfSKP = new SimpleDateFormat("MMyy");
		Date terbit = new Date(dateTerbit.getYear()-1900, dateTerbit.getMonth(), dateTerbit.getDay());
		retValue[0] = wp.getNPWP();
		retValue[1] = txtNamaBadan.getText();
		retValue[2] = txtAlamat.getText();
		retValue[3] = nilai.substring(0, nilai.indexOf("."));
		retValue[4] = nilaiDenda.substring(0, nilaiDenda.indexOf("."));
		retValue[5] = txtNoSPTPD.getText();
		retValue[6] = sdf.format(terbit);
		retValue[7] = wp.getKode_bid_usaha();
		retValue[8] = sdfSKP.format(listMasaPajak.get(cmbMasaPajak.getSelectionIndex()).getMasaPajakDari()) + "-" + sdfSKP.format(listMasaPajak.get(cmbMasaPajak.getSelectionIndex()).getMasaPajakSampai()); 
		Timestamp jthTempo = new Timestamp(dateJatuhTempo.getYear()-1900, dateJatuhTempo.getMonth(), dateJatuhTempo.getDay(), 23, 59, 59, 99);
		if (dateNow.before(jthTempo))
			retValue[9] = sdf.format(jthTempo);
		else
			retValue[9] = sdf.format(dateNow);
		
		return retValue;
	}
	
	private String getSTSFormat(String sts){
		String retValue = "";
		for (int i=0;i<sts.length();i++){
			if (i == 3 || i == 7 || i == 10 || i == 14 || i==17)
				retValue += sts.substring(i, i+ 1) + "-";
			else
				retValue += sts.substring(i, i+ 1);
		}
		
		return retValue;
	}
}
