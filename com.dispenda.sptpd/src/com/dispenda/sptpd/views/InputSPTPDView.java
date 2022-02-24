package com.dispenda.sptpd.views;

import java.lang.reflect.InvocationTargetException;
import java.math.RoundingMode;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
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
import com.dispenda.model.Sptpd;
import com.dispenda.model.Sspd;
import com.dispenda.model.UserModul;
import com.dispenda.model.WpTutup;
import com.dispenda.object.GlobalVariable;
import com.dispenda.object.Preferences;
import com.dispenda.sspd.dialog.CariLokasiDialog;
//import com.dispenda.sspd.dialog.CariLokasiDialog;
import com.dispenda.web.bkad.ApiPost;
import com.dispenda.widget.IMoneyChangeListener;
import com.dispenda.widget.MoneyChangeEvent;
import com.dispenda.widget.MoneyField;
import com.ibm.icu.text.NumberFormat;
import com.ibm.icu.util.Currency;
import com.ibm.icu.util.CurrencyAmount;

public class InputSPTPDView extends ViewPart {
	
	public static final String ID = InputSPTPDView.class.getName();
	private boolean resultApi = false;
	private Text txtKodePajak;
	private Text txtNoUrut;
	private Text txtKodeKecamatan;
	private Text txtKodeKelurahan;
	private Text txtNamaBadan;
	private Text txtNamaPemilik;
	private Text txtAlamat;
	private Text txtNoSPTPD;
	private Text txtAssesment;
	private Text txtSubPajak;
	private MoneyField txtDasarPengenaan;
	private MoneyField txtPajakTerutang;
	private Text txtTarif;
	private Text txtLokasi;
	private Label lblLokasi;
	private Label lblDasarPengenaan;
	private Label lblTarifPajak;
	
	private DateTime calTglSPTPD;
	private DateTime calMasaPajakAwal;
	private DateTime calMasaPajakAkhir;
	private Timestamp dateNow;
	private List<Sptpd> listSptpd = new ArrayList<Sptpd>();
	private List<Periksa> listPeriksa = new ArrayList<Periksa>();
	private Date pajakDari[];
	private Date pajakSampai[];
	private String jenisSKP[];
	private TableItem item;
	private Color fontColor = new Color(Display.getCurrent(),Preferences.FONT_COLOR);
	private String kodeBidUsaha = "";
	private Button chkDenda;
	private Clipboard clipboard = new Clipboard(Display.getCurrent());
	PendaftaranWajibPajak wp;
	Sptpd spt;
	DecimalFormat Df = new DecimalFormat(); 

	private Composite composite;

	private Button btnHapus;
	private Table tbl_SPT;
	private Integer selectedID = null;
	private boolean isLunas = false;
	private String noUrut;
	private int tableIndex = -1;
	
	private Locale ind = new Locale("id", "ID");
    private NumberFormat indFormat = NumberFormat.getCurrencyInstance(ind);
    private Label lblLunas;
    private Label label;
	private ScrolledComposite scrolledComposite;
	private Button btnSimpan;
	private Button btnPecah;
	private Label lblKeterangan;
	
	public InputSPTPDView() {
	}

	@SuppressWarnings("deprecation")
	@Override
	public void createPartControl(Composite parent) {
		Locale.setDefault(new Locale("id","ID"));
		dateNow = (java.sql.Timestamp)ControllerFactory.getMainController().getCpWajibPajakDAOImpl().getDateNow();
		
		scrolledComposite = new ScrolledComposite(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		
		composite = new Composite(scrolledComposite, SWT.NONE);
		composite.setLayout(new GridLayout(9, false));
		
		btnPemeriksaanBpk = new Button(composite, SWT.CHECK);
		btnPemeriksaanBpk.setForeground(fontColor);
		btnPemeriksaanBpk.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnPemeriksaanBpk.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (btnPemeriksaanBpk.getSelection() == true){
					lblDasarPengenaan.setVisible(false);
					lblTarifPajak.setVisible(false);
					txtDasarPengenaan.setVisible(false);
					txtTarif.setVisible(false);
					chkDenda.setVisible(false);
					txtPajakTerutang.setEnabled(true);
				}
				else{
					lblDasarPengenaan.setVisible(true);
					lblTarifPajak.setVisible(true);
					txtDasarPengenaan.setVisible(true);
					txtTarif.setVisible(true);
					chkDenda.setVisible(true);
					txtPajakTerutang.setEnabled(false);
				}
			}
		});
		btnPemeriksaanBpk.setText("Pemeriksaan BPK");
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		
		Label lblNpwpd = new Label(composite, SWT.NONE);
		lblNpwpd.setForeground(fontColor);
		lblNpwpd.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblNpwpd.setText("NPWPD*");
		
		txtKodePajak = new Text(composite, SWT.BORDER);
		GridData gd_txtKodePajak = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_txtKodePajak.widthHint = 20;
		txtKodePajak.setLayoutData(gd_txtKodePajak);
		txtKodePajak.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtKodePajak.setEditable(false);
		txtKodePajak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		txtNoUrut = new Text(composite, SWT.BORDER);
		GridData gd_txtNoUrut = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_txtNoUrut.widthHint = 70;
		txtNoUrut.setLayoutData(gd_txtNoUrut);
		txtNoUrut.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtNoUrut.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.keyCode == SWT.CR || e.keyCode == SWT.KEYPAD_CR)
				{
					noUrut = txtNoUrut.getText().trim();
					resetForm();
					getDataSPTPD();
					if (txtKodePajak.getText().equalsIgnoreCase("6") || wp.getInsidentil())
						txtTarif.setEditable(true);
					else
						txtTarif.setEditable(false);
					
					if (txtKodePajak.getText().equalsIgnoreCase("7"))
						ChkSkpdTerbit.setVisible(true);
					else
						ChkSkpdTerbit.setVisible(false);
					
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
			}
		});
		txtNoUrut.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtNoUrut.setFocus();
		
		txtKodeKecamatan = new Text(composite, SWT.BORDER);
		GridData gd_txtKodeKecamatan = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_txtKodeKecamatan.widthHint = 20;
		txtKodeKecamatan.setLayoutData(gd_txtKodeKecamatan);
		txtKodeKecamatan.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtKodeKecamatan.setEditable(false);
		txtKodeKecamatan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		txtKodeKelurahan = new Text(composite, SWT.BORDER);
		GridData gd_txtKodeKelurahan = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_txtKodeKelurahan.widthHint = 20;
		txtKodeKelurahan.setLayoutData(gd_txtKodeKelurahan);
		txtKodeKelurahan.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtKodeKelurahan.setEditable(false);
		txtKodeKelurahan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		label = new Label(composite, SWT.SEPARATOR | SWT.VERTICAL);
		GridData gd_label = new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 18);
		gd_label.horizontalIndent = 20;
		label.setLayoutData(gd_label);
		
		tbl_SPT = new Table(composite, SWT.BORDER | SWT.FULL_SELECTION);
		tbl_SPT.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(((e.stateMask & SWT.CTRL) == SWT.CTRL) && (e.keyCode == 'f')){
					int index = -1;
					if (txtKodePajak.getText().equalsIgnoreCase("7")){
						CariLokasiDialog dialog = new CariLokasiDialog(Display.getCurrent().getActiveShell());
						if (dialog.open() == Window.OK){
							String val = dialog.getEnteredText();
							for (int i=tableIndex + 1;i<listSptpd.size();i++){
								if (listSptpd.get(i).getLokasi().toUpperCase().contains(val.toUpperCase())){
									index = i;
									tableIndex = i;
									tbl_SPT.setTopIndex(i);
									break;
								}
							}
							 
							if (index >= 0)
							{
								tbl_SPT.select(index);
								selectTable();
							}else{
								MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Data Lokasi tidak ditemukan");
							}
						}
					}
                }
			}
		});
		tbl_SPT.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		GridData gd_tbl_SPT = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 18);
		gd_tbl_SPT.heightHint = 478;
		tbl_SPT.setLayoutData(gd_tbl_SPT);
		tbl_SPT.setFont(SWTResourceManager.getFont("Calibri", 11, SWT.NORMAL));
		tbl_SPT.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		tbl_SPT.setLinesVisible(true);
		tbl_SPT.setHeaderVisible(true);
		tbl_SPT.setItemCount(1);
		tbl_SPT.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				tableIndex = tbl_SPT.getSelectionIndex();
				selectTable();
			}
		});
		Menu menu = new Menu(tbl_SPT);
		MenuItem menuCopy = new MenuItem(menu, SWT.PUSH);
		menuCopy.setText("Copy");
		menuCopy.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				clipboard.setContents(new Object[] { tbl_SPT.getItem(tbl_SPT.getSelectionIndex()).getText(1) },
			              new Transfer[] { TextTransfer.getInstance() });
			}
		});
		tbl_SPT.setMenu(menu);
		
		TableColumn tblclmnNewColumn_1 = new TableColumn(tbl_SPT, SWT.NONE);
		tblclmnNewColumn_1.setWidth(140);
		tblclmnNewColumn_1.setText("No SPTPD");
		TableColumn tblclmnNewColumn_2 = new TableColumn(tbl_SPT, SWT.NONE);
		tblclmnNewColumn_2.setWidth(210);
		tblclmnNewColumn_2.setText("Masa Pajak");
		
		tblclmnNoSptpdLama = new TableColumn(tbl_SPT, SWT.NONE);
		tblclmnNoSptpdLama.setWidth(140);
		tblclmnNoSptpdLama.setText("No SPTPD Lama");
		
		btnPecah = new Button(composite, SWT.NONE);
		btnPecah.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String NoSPTPD = "";
				String NoNPPD = "";
				if (isValidated()){
					int index = tbl_SPT.getSelectionIndex();
					for (int i=0;i<=2;i++){
						Date masaDari = listSptpd.get(index).getMasaPajakDari();
						Date masaSampai = listSptpd.get(index).getMasaPajakSampai();
						Calendar calDari = Calendar.getInstance();
						calDari.set(Calendar.DAY_OF_MONTH, masaDari.getDate());
						calDari.set(Calendar.MONTH, masaDari.getMonth());
						calDari.set(Calendar.YEAR, masaDari.getYear());
						calDari.add(Calendar.MONTH, i);
						masaDari = new Date(calDari.get(Calendar.YEAR), calDari.get(Calendar.MONTH), 1);
						Calendar calSampai = Calendar.getInstance();
						calSampai.set(Calendar.DAY_OF_MONTH, masaDari.getDate());
						calSampai.set(Calendar.MONTH, masaDari.getMonth());
						calSampai.set(Calendar.YEAR, masaDari.getYear());
						masaSampai = new Date(calSampai.get(Calendar.YEAR), calSampai.get(Calendar.MONTH), calSampai.getActualMaximum(Calendar.DAY_OF_MONTH));
						Double DendaTambahan = 0.0;
						Sptpd spt = new Sptpd();
						spt.setID(null);
						spt.setNPWPD(txtKodePajak.getText() + txtNoUrut.getText() + txtKodeKecamatan.getText() + txtKodeKelurahan.getText());
						Date tglSPT = new Date(calTglSPTPD.getYear() - 1900, calTglSPTPD.getMonth(), calTglSPTPD.getDay());
						Date stsValid = getSTSValid(tglSPT, txtKodePajak.getText());
		//				Date MasaPajakDari = new Date(calMasaPajakAwal.getYear() - 1900, calMasaPajakAwal.getMonth(), calMasaPajakAwal.getDay());
		//				Date MasaPajakSampai = new Date(calMasaPajakAkhir.getYear() - 1900, calMasaPajakAkhir.getMonth(), calMasaPajakAkhir.getDay());
						spt.setTanggalSPTPD(tglSPT);
						spt.setTanggalNPPD(tglSPT);
						spt.setMasaPajakDari(masaDari);
						spt.setMasaPajakSampai(masaSampai);
						spt.setDasarPengenaan(txtDasarPengenaan.getMoney().getNumber().doubleValue()/3);
						spt.setTarifPajak((Integer.parseInt(txtTarif.getText())));
						spt.setPajakTerutang(txtPajakTerutang.getMoney().getNumber().doubleValue()/3);
						spt.setAssesment("Self");
						spt.setIdSubPajak(wp.getIdSubPajak());
						spt.setDendaTambahan(DendaTambahan);
						spt.setLokasi("");
						spt.setStsValid(stsValid);
						Format formatter = new SimpleDateFormat("yyyy");
						String year = formatter.format(tglSPT);
						NoSPTPD = ControllerFactory.getMainController().getCpSptpdDAOImpl().getNoSPTPD(year, wp.getIdSubPajak(), spt.getAssesment());
						NoNPPD = NoSPTPD.replace("SPTPD", "NPPD");
						spt.setNoSPTPD(NoSPTPD);
						spt.setNoNPPD(NoNPPD);
					
						if(ControllerFactory.getMainController().getCpSptpdDAOImpl().saveSptpd(spt)){
							if (i==2)
								MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Data berhasil disimpan.");
			
							StringBuffer sb = new StringBuffer();
							sb.append("SAVE " +
									"SPTPD:"+NoSPTPD+
									" NPWPD:"+wp.getNPWP()+
									" TanggalSPTPD:"+tglSPT+
									" PajakTerhutang:"+txtPajakTerutang.getText().getText()+
									" MasaPajakDari:"+masaDari+
									" MasaPajakSampai:"+masaSampai);
							new LogImp().setLog(sb.toString(), userModul.getDisplayName(), userModul.getSubModul().getNamaModul());
						}else
							MessageDialog.openError(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Error pada Database. Silahkan hubungi ADMIN.");
					}
					loadOldSPTPD();
					if (selectedID == null)
						selectedID = ControllerFactory.getMainController().getCpSptpdDAOImpl().getLastIdSptpd(wp.getNPWP());
					txtNoSPTPD.setText(NoSPTPD);
				}else
					MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Silahkan isi semua data");	
			}
		});
		btnPecah.setText("btn");
		btnPecah.setFont(SWTResourceManager.getFont("", 0, SWT.NORMAL));
		btnPecah.setEnabled(false);
		btnPecah.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		if (userModul.getIdUser() == 1 || userModul.getIdUser() == 2 || userModul.getIdUser() == 19)
			btnPecah.setVisible(true);
		else
			btnPecah.setVisible(false);
		
		lblKeterangan = new Label(composite, SWT.WRAP);
		GridData gd_lblKeterangan = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 3);
		gd_lblKeterangan.heightHint = 27;
		lblKeterangan.setLayoutData(gd_lblKeterangan);
		lblKeterangan.setForeground(fontColor);
		lblKeterangan.setBackground(SWTResourceManager.getColor(SWT.COLOR_CYAN));
		lblKeterangan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, 15, SWT.BOLD));
		lblKeterangan.setVisible(false);
	
		Label lblNamaBadan = new Label(composite, SWT.NONE);
		lblNamaBadan.setForeground(fontColor);
		lblNamaBadan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblNamaBadan.setText("Nama Badan");
		
		txtNamaBadan = new Text(composite, SWT.BORDER);
		GridData gd_txtNamaBadan = new GridData(SWT.LEFT, SWT.CENTER, false, false, 4, 1);
		gd_txtNamaBadan.widthHint = 190;
		txtNamaBadan.setLayoutData(gd_txtNamaBadan);
		txtNamaBadan.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtNamaBadan.setEditable(false);
		txtNamaBadan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		new Label(composite, SWT.NONE);
		
		Label lblNamaPemilik = new Label(composite, SWT.NONE);
		lblNamaPemilik.setForeground(fontColor);
		lblNamaPemilik.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblNamaPemilik.setText("Nama Pemilik");
		
		txtNamaPemilik = new Text(composite, SWT.BORDER);
		GridData gd_txtNamaPemilik = new GridData(SWT.LEFT, SWT.CENTER, false, false, 4, 1);
		gd_txtNamaPemilik.widthHint = 190;
		txtNamaPemilik.setLayoutData(gd_txtNamaPemilik);
		txtNamaPemilik.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtNamaPemilik.setEditable(false);
		txtNamaPemilik.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		new Label(composite, SWT.NONE);
		
		Label lblAlamatBadan = new Label(composite, SWT.NONE);
		lblAlamatBadan.setForeground(fontColor);
		lblAlamatBadan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblAlamatBadan.setText("Alamat Badan");
		
		txtAlamat = new Text(composite, SWT.BORDER);
		GridData gd_txtAlamat = new GridData(SWT.LEFT, SWT.CENTER, false, false, 4, 1);
		gd_txtAlamat.widthHint = 190;
		txtAlamat.setLayoutData(gd_txtAlamat);
		txtAlamat.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtAlamat.setEditable(false);
		txtAlamat.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		
		Label lblNoSttpd = new Label(composite, SWT.NONE);
		lblNoSttpd.setForeground(fontColor);
		lblNoSttpd.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblNoSttpd.setText("No SPTPD");
		
		txtNoSPTPD = new Text(composite, SWT.BORDER);
		GridData gd_txtNoSPTPD = new GridData(SWT.LEFT, SWT.CENTER, false, false, 4, 1);
		gd_txtNoSPTPD.widthHint = 190;
		txtNoSPTPD.setLayoutData(gd_txtNoSPTPD);
		txtNoSPTPD.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtNoSPTPD.setEditable(false);
		txtNoSPTPD.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				if (!txtNoSPTPD.getText().equalsIgnoreCase("")){
					if (userModul.getIdUser()==1)
						btnHapus.setEnabled(true);
					btnSimpan.setText("Ubah");
				}
				else{
					btnHapus.setEnabled(false);
					btnSimpan.setText("Simpan");
				}
			}
		});
		txtNoSPTPD.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		
		Label lblTanggalSttpd = new Label(composite, SWT.NONE);
		lblTanggalSttpd.setForeground(fontColor);
		lblTanggalSttpd.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblTanggalSttpd.setText("Tanggal SPTPD");
		
		calTglSPTPD = new DateTime(composite, SWT.BORDER | SWT.DROP_DOWN);
		calTglSPTPD.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		calTglSPTPD.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 4, 1));
		calTglSPTPD.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));		
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		Label lblAssesment = new Label(composite, SWT.NONE);
		lblAssesment.setForeground(fontColor);
		lblAssesment.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblAssesment.setText("Assesment");
		
		txtAssesment = new Text(composite, SWT.BORDER);
		GridData gd_txtAssesment = new GridData(SWT.LEFT, SWT.CENTER, false, false, 4, 1);
		gd_txtAssesment.widthHint = 190;
		txtAssesment.setLayoutData(gd_txtAssesment);
		txtAssesment.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtAssesment.setEditable(false);
		txtAssesment.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		
		Label lblJenisSubPajak = new Label(composite, SWT.NONE);
		lblJenisSubPajak.setForeground(fontColor);
		lblJenisSubPajak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblJenisSubPajak.setText("Jenis Sub Pajak");
		
		txtSubPajak = new Text(composite, SWT.BORDER);
		GridData gd_txtSubPajak = new GridData(SWT.LEFT, SWT.CENTER, false, false, 4, 1);
		gd_txtSubPajak.widthHint = 190;
		txtSubPajak.setLayoutData(gd_txtSubPajak);
		txtSubPajak.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtSubPajak.setEditable(false);
		txtSubPajak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		
		Label lblMasaPajakAwal = new Label(composite, SWT.NONE);
		lblMasaPajakAwal.setForeground(fontColor);
		lblMasaPajakAwal.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblMasaPajakAwal.setText("Masa Pajak Awal");
		
		calMasaPajakAwal = new DateTime(composite, SWT.BORDER | SWT.DROP_DOWN);
		calMasaPajakAwal.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		calMasaPajakAwal.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 4, 1));
		calMasaPajakAwal.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		
		Label lblMasaPajakAkhir = new Label(composite, SWT.NONE);
		lblMasaPajakAkhir.setForeground(fontColor);
		lblMasaPajakAkhir.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblMasaPajakAkhir.setText("Masa Pajak Akhir");
		
		calMasaPajakAkhir = new DateTime(composite, SWT.BORDER | SWT.DROP_DOWN);
		calMasaPajakAkhir.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		calMasaPajakAkhir.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 4, 1));
		calMasaPajakAkhir.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		Calendar instance = Calendar.getInstance();
		instance.set(Calendar.DAY_OF_MONTH, dateNow.getDate());
		instance.set(Calendar.MONTH, dateNow.getMonth());
		instance.set(Calendar.YEAR, dateNow.getYear()+1900);
		calMasaPajakAwal.setDate(instance.get(Calendar.YEAR), instance.get(Calendar.MONTH), 1);
		calMasaPajakAkhir.setDate(instance.get(Calendar.YEAR), instance.get(Calendar.MONTH), instance.getActualMaximum(Calendar.DAY_OF_MONTH));
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		
		lblDasarPengenaan = new Label(composite, SWT.NONE);
		lblDasarPengenaan.setForeground(fontColor);
		lblDasarPengenaan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblDasarPengenaan.setText("Dasar Pengenaan");
		
		txtDasarPengenaan = new MoneyField(composite, SWT.BORDER);
		txtDasarPengenaan.getText().setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtDasarPengenaan.getText().setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		GridData gd_txtDasarPengenaan = new GridData(SWT.LEFT, SWT.CENTER, false, false, 4, 1);
		gd_txtDasarPengenaan.widthHint = 190;
		txtDasarPengenaan.setLayoutData(gd_txtDasarPengenaan);
		txtDasarPengenaan.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtDasarPengenaan.addMoneyChangeListener(new IMoneyChangeListener() {
			public void moneyChange(MoneyChangeEvent event) {
				if(!txtTarif.getText().equalsIgnoreCase("")){
					if (!txtDasarPengenaan.getMoney().toString().equalsIgnoreCase("")){
						DecimalFormat df = new DecimalFormat("#.##");
						df.setMaximumFractionDigits(0);
						df.setRoundingMode(RoundingMode.UP);
						if (txtTarif.getText().equalsIgnoreCase("-")){
							double val = txtDasarPengenaan.getMoney().getNumber().doubleValue();
							double pajak = 1 * val;
							txtPajakTerutang.setMoney(new CurrencyAmount(Double.parseDouble(df.format(pajak)), Currency.getInstance("ind")));
						}
						else{
							double val = txtDasarPengenaan.getMoney().getNumber().doubleValue();
							double pajak = Double.parseDouble(txtTarif.getText()) / 100 * val;
							txtPajakTerutang.setMoney(new CurrencyAmount(Double.parseDouble(df.format(pajak)), Currency.getInstance("ind")));
						}
					}
					else
						txtPajakTerutang.setMoney(new CurrencyAmount(0, Currency.getInstance("ind")));
				}
				else
					txtPajakTerutang.setMoney(new CurrencyAmount(0, Currency.getInstance("ind")));
			}
		});
		txtDasarPengenaan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		new Label(txtDasarPengenaan, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		
		lblTarifPajak = new Label(composite, SWT.NONE);
		lblTarifPajak.setForeground(fontColor);
		lblTarifPajak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblTarifPajak.setText("Tarif Pajak");
		
		txtTarif = new Text(composite, SWT.BORDER);
		GridData gd_txtTarif = new GridData(SWT.LEFT, SWT.CENTER, false, false, 4, 1);
		gd_txtTarif.widthHint = 51;
		txtTarif.setLayoutData(gd_txtTarif);
		txtTarif.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtTarif.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtTarif.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent me) {
				Text txtTar = (Text) me.widget;
				if(!txtTarif.getText().equalsIgnoreCase(""))
					if (txtDasarPengenaan.getMoney() != null){
						DecimalFormat df = new DecimalFormat("#.##");
						df.setMaximumFractionDigits(0);
						df.setRoundingMode(RoundingMode.UP);
						if (txtTarif.getText().equalsIgnoreCase("-")){
							double val = txtDasarPengenaan.getMoney().getNumber().doubleValue();
							double pajak = 1 * val;
							txtPajakTerutang.setMoney(new CurrencyAmount(Double.parseDouble(df.format(pajak)), Currency.getInstance("ind")));
						}
						else{
							double val = txtDasarPengenaan.getMoney().getNumber().doubleValue();
							double pajak = Double.parseDouble(txtTarif.getText()) / 100 * val;
							txtPajakTerutang.setMoney(new CurrencyAmount(Double.parseDouble(df.format(pajak)), Currency.getInstance("ind")));
						}
					}else{
						txtPajakTerutang.setMoney(new CurrencyAmount(0, Currency.getInstance("ind")));
					}
			}
		});
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		
		Label lblPajakTerhutang = new Label(composite, SWT.NONE);
		lblPajakTerhutang.setForeground(fontColor);
		lblPajakTerhutang.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblPajakTerhutang.setText("Pajak Terhutang");
		
		txtPajakTerutang = new MoneyField(composite, SWT.BORDER);
		txtPajakTerutang.getText().setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtPajakTerutang.getText().setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		GridData gd_txtPajakTerutang = new GridData(SWT.LEFT, SWT.CENTER, false, false, 4, 1);
		gd_txtPajakTerutang.widthHint = 190;
		txtPajakTerutang.setLayoutData(gd_txtPajakTerutang);
		txtPajakTerutang.setEnabled(false);
		txtPajakTerutang.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtPajakTerutang.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		new Label(txtPajakTerutang, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		
		ChkSkpdTerbit = new Button(composite, SWT.CHECK);
		ChkSkpdTerbit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (ChkSkpdTerbit.getSelection()){
					lblTanggalTerbit.setVisible(true);
					calTerbit.setVisible(true);
				}else{
					lblTanggalTerbit.setVisible(false);
					calTerbit.setVisible(false);
				}
			}
		});
		ChkSkpdTerbit.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		ChkSkpdTerbit.setVisible(false);
		ChkSkpdTerbit.setText("SKPD Terbit");
		
		chkDenda = new Button(composite, SWT.CHECK);
		chkDenda.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 4, 1));
		chkDenda.setText("Denda Tambahan");
		chkDenda.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		
		lblTanggalTerbit = new Label(composite, SWT.NONE);
		lblTanggalTerbit.setVisible(false);
		lblTanggalTerbit.setForeground(fontColor);
		lblTanggalTerbit.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblTanggalTerbit.setText("Tanggal Terbit");
		
		calTerbit = new DateTime(composite, SWT.BORDER | SWT.DROP_DOWN);
		calTerbit.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		calTerbit.setVisible(false);
		calTerbit.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		calTerbit.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		
				
		lblLokasi = new Label(composite, SWT.NONE);
		lblLokasi.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		lblLokasi.setForeground(fontColor);
		lblLokasi.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblLokasi.setText("Lokasi / Keterangan");
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		
		txtLokasi = new Text(composite, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
		txtLokasi.setEditable(false);
		txtLokasi.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtLokasi.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		GridData gd_txtLokasi = new GridData(SWT.FILL, SWT.TOP, false, false, 5, 2);
		gd_txtLokasi.widthHint = 262;
		gd_txtLokasi.heightHint = 56;
		txtLokasi.setLayoutData(gd_txtLokasi);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		Group groupBtn = new Group(composite, SWT.NONE);
		groupBtn.setLayout(new GridLayout(3, false));
		GridData gd_groupBtn = new GridData(SWT.LEFT, SWT.CENTER, false, false, 6, 1);
		gd_groupBtn.heightHint = 38;
		groupBtn.setLayoutData(gd_groupBtn);
		
		Button btnBaru = new Button(groupBtn, SWT.NONE);
		btnBaru.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
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
		
		btnSimpan = new Button(groupBtn, SWT.NONE);
		btnSimpan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnSimpan.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				/*boolean akses = false;
				akses = ControllerFactory.getMainController().getCpUserDAOImpl().getAksesInputSptpd(txtKodePajak.getText(),GlobalVariable.userModul.getIdUser());
				if(!akses){
					//System.out.println("masuk");
					MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Anda tidak memiliki hak untuk menyimpan SPTPD jenis pajak ini.");
					return;
				}*/
				boolean result = true;
				double pajak = 0.0;
				Date MasaPajakDari = new Date(calMasaPajakAwal.getYear() - 1900, calMasaPajakAwal.getMonth(), calMasaPajakAwal.getDay());
				Date MasaPajakSampai = new Date(calMasaPajakAkhir.getYear() - 1900, calMasaPajakAkhir.getMonth(), calMasaPajakAkhir.getDay());
				Date tglSPT = new Date(calTglSPTPD.getYear() - 1900, calTglSPTPD.getMonth(), calTglSPTPD.getDay());
				boolean adaSKPDKB = false;
				listPeriksa = ControllerFactory.getMainController().getCpPeriksaDAOImpl().checkSKPDKB(wp.getNPWP());
				pajakDari = new Date[listPeriksa.size()];
				pajakSampai = new Date[listPeriksa.size()];
				//jenisSKP = new String[listPeriksa.size()];
				if (listPeriksa.size() > 0){
					for (int i=0;i<listPeriksa.size();i++){
						pajakDari[i] = listPeriksa.get(i).getMasaPajakDari();
						pajakSampai[i] = listPeriksa.get(i).getMasaPajakSampai();
						//jenisSKP[i] = listPeriksa.get(i).getTipeSkp();
					}
				}
				//enable = true;
				
					for (int i=0;i<listPeriksa.size();i++){
						if (pajakSampai[i].after(MasaPajakDari) && pajakDari[i].before(MasaPajakSampai))
						{
							//txtNamaPenyetor.setFocus();
							MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "SPTPD tidak bisa disimpan karena telah keluar SKPDKB");
							//enable = false;
							adaSKPDKB = true;
							break;
						}
					}
						
				if(isSave() && cekMasaPajak(MasaPajakDari, MasaPajakSampai, tglSPT) && !adaSKPDKB){
					if (btnSimpan.getText().equalsIgnoreCase("Ubah"))
						result = MessageDialog.openQuestion(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Ubah data? (" + txtNoSPTPD.getText() + ")");
					if (result){
						DecimalFormat df = new DecimalFormat("#.##");
						df.setMaximumFractionDigits(0);
						df.setRoundingMode(RoundingMode.UP);
						txtNoSPTPD.setBackground(SWTResourceManager.getColor(SWT.COLOR_RED));
						String NoSPTPD = "";
						String NoNPPD = "";
						Double DendaTambahan = 0.0;
						Sptpd spt = new Sptpd();
						spt.setID(selectedID);
						spt.setNPWPD(txtKodePajak.getText() + txtNoUrut.getText() + txtKodeKecamatan.getText() + txtKodeKelurahan.getText());
						Date stsValid = getSTSValid(txtKodePajak.getText().equalsIgnoreCase("7") ? tglSPT : MasaPajakSampai, txtKodePajak.getText());
						spt.setTanggalSPTPD(tglSPT);
						spt.setTanggalNPPD(tglSPT);
						if (txtKodePajak.getText().equalsIgnoreCase("7") && ChkSkpdTerbit.getSelection()){
							tglSPT = new Date(calTerbit.getYear() - 1900, calTerbit.getMonth(), calTerbit.getDay());
							spt.setTanggalSPTPD(tglSPT);
							stsValid = getSTSValid(txtKodePajak.getText().equalsIgnoreCase("7") ? tglSPT : MasaPajakSampai, txtKodePajak.getText());
						}
						spt.setMasaPajakDari(MasaPajakDari);
						spt.setMasaPajakSampai(MasaPajakSampai);
						spt.setStsValid(stsValid);
						spt.setDasarPengenaan(txtDasarPengenaan.getMoney().getNumber().doubleValue());
						spt.setTarifPajak((Integer.parseInt(txtTarif.getText())));
						pajak = Double.parseDouble(df.format(txtPajakTerutang.getMoney().getNumber().doubleValue()).replace(',', '.'));
						spt.setPajakTerutang(pajak);
						spt.setAssesment(txtAssesment.getText());
						spt.setIdSubPajak(wp.getIdSubPajak());
						if (chkDenda.getSelection() == true)
						{
							//DendaTambahan = getDendaTambahan(); 
						}
						else
						{
							DendaTambahan = 0.0;
						}
						spt.setDendaTambahan(DendaTambahan);
						spt.setLokasi(txtLokasi.getText().replace("'", "'"));
						Format formatter = new SimpleDateFormat("yyyy");
						String year = formatter.format(tglSPT);
						if (selectedID == null){
							
							if (Integer.valueOf(year) >= 2016){
								NoSPTPD = "INSERT"; //ControllerFactory.getMainController().getCpSptpdDAOImpl().getNoSPTPDBaru(year, wp.getIdSubPajak(), spt.getAssesment());
								NoNPPD = "INSERT";
							}
							else{
								NoSPTPD = ControllerFactory.getMainController().getCpSptpdDAOImpl().getNoSPTPD(year, wp.getIdSubPajak(), spt.getAssesment());
								if (spt.getAssesment().equalsIgnoreCase("Official"))
									NoNPPD = NoSPTPD.replace("SKPD", "NPPD");
								else
									NoNPPD = NoSPTPD.replace("SPTPD", "NPPD");
							}
						}
						else{
							NoSPTPD = txtNoSPTPD.getText();
							if (spt.getAssesment().equalsIgnoreCase("Official"))
								NoNPPD = NoSPTPD.replace("SKPD", "NPPD");
							else
								NoNPPD = NoSPTPD.replace("SPTPD", "NPPD");
						}
						spt.setNoSPTPD(NoSPTPD);
						spt.setNoNPPD(NoNPPD);
						if (btnPemeriksaanBpk.getSelection()==true){
							spt.setTarifPajak(null);
							spt.setDasarPengenaan(pajak);
						}
						
						if (isValidated()){
							if(ControllerFactory.getMainController().getCpSptpdDAOImpl().saveSptpd(spt)){
								if (selectedID == null)
									selectedID = ControllerFactory.getMainController().getCpSptpdDAOImpl().getLastIdSptpd(wp.getNPWP());
								NoSPTPD = ControllerFactory.getMainController().getCpSptpdDAOImpl().getNoSPTPDExist(selectedID);
								spt.setNoSPTPD(NoSPTPD);
								final String noSTS = ControllerFactory.getMainController().getCpSptpdDAOImpl().getNoSTS(selectedID);
								StringBuffer sb = new StringBuffer();
								sb.append("SAVE " +
										"SPTPD:"+NoSPTPD+
										" NPWPD:"+wp.getNPWP()+
										" TanggalSPTPD:"+tglSPT+
										" PajakTerhutang:"+txtPajakTerutang.getText().getText()+
										" MasaPajakDari:"+MasaPajakDari+
										" MasaPajakSampai:"+MasaPajakSampai);
								new LogImp().setLog(sb.toString(), userModul.getDisplayName(), userModul.getSubModul().getNamaModul());
								boolean isSTS = true;
								if (txtKodePajak.getText().equalsIgnoreCase("7") && !ChkSkpdTerbit.getSelection())
									isSTS = false;  
								/*if (isSTS){
									//Get No STS
									final String[] listString = getParam(spt);
									//System.out.println("List SPTPD "+listString.toString());
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
//									resultApi = true;
									if (resultApi){
										String sts = "";
										if (noSTS.equalsIgnoreCase(""))
											sts = ControllerFactory.getMainController().getCpSptpdDAOImpl().getNoSTS(selectedID);
										else
											sts = noSTS;
										StringBuffer sb2 = new StringBuffer();
										sb2.append("SAVE " +
												"STS:"+sts+
												" SPTPD:"+NoSPTPD+
												" NPWPD:"+wp.getNPWP());
										new LogImp().setLog(sb2.toString(), userModul.getDisplayName(), userModul.getSubModul().getNamaModul());
										if (btnSimpan.getText().equalsIgnoreCase("Simpan"))
											MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Data berhasil disimpan.");
										else
											MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Data berhasil diubah.");
										txtNoSPTPD.setText(NoSPTPD);
										txtNoSPTPD.pack(true);
										loadOldSPTPD();
									}else{
//										Rollback SPT
										if (btnSimpan.getText().equalsIgnoreCase("Simpan")){
											if (ControllerFactory.getMainController().getCpSptpdDAOImpl().deleteSptpd(selectedID)){
												StringBuffer sb2 = new StringBuffer();
												sb2.append("Rollback " +
														"SPTPD:"+NoSPTPD+
														" NPWPD:"+wp.getNPWP()+
														" TanggalSPTPD:"+tglSPT+
														" PajakTerhutang:"+txtPajakTerutang.getText().getText()+
														" MasaPajakDari:"+MasaPajakDari+
														" MasaPajakSampai:"+MasaPajakSampai);
												new LogImp().setLog(sb2.toString(), userModul.getDisplayName(), userModul.getSubModul().getNamaModul());
												MessageDialog.openError(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Insert STS gagal.");
											}
										}else
											MessageDialog.openError(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Ubah STS gagal.");
									}
								}else{
									if (btnSimpan.getText().equalsIgnoreCase("Simpan"))
										MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Data berhasil disimpan.");
									else
										MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Data berhasil diubah.");
									txtNoSPTPD.setText(NoSPTPD);
									txtNoSPTPD.pack(true);
									loadOldSPTPD();
								}*/
								if (btnSimpan.getText().equalsIgnoreCase("Simpan"))
									MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Data berhasil disimpan.");
								else
									MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Data berhasil diubah.");
								txtNoSPTPD.setText(NoSPTPD);
								txtNoSPTPD.pack(true);
								loadOldSPTPD();
							}else
								MessageDialog.openError(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Error pada Database. Silahkan hubungi ADMIN.");
						}else
							MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Silahkan isi semua data");	
					}				
				}
//				else
//					MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "Warning", "Anda tidak memiliki hak untuk menyimpan atau merubah data.");
				
			}
		});
		GridData gd_btnSimpan = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnSimpan.widthHint = 90;
		btnSimpan.setLayoutData(gd_btnSimpan);
		btnSimpan.setText("Simpan");
		
		btnHapus = new Button(groupBtn, SWT.NONE);
		btnHapus.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnHapus.setEnabled(false);
		btnHapus.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(isDelete()){
					int style = SWT.ICON_QUESTION |SWT.YES | SWT.NO;
					Sspd objSSPD = ControllerFactory.getMainController().getCpSspdDAOImpl().getSSPD(wp.getNPWP(), txtNoSPTPD.getText());
					if (!objSSPD.getLunas()){
						if (MessageDialog.open(6, Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Hapus data (" + txtNoSPTPD.getText() + ")?", style)){
							if (ControllerFactory.getMainController().getCpSptpdDAOImpl().deleteSptpd(selectedID)){
								MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Data berhasil dihapus.");
								StringBuffer sb = new StringBuffer();
								sb.append("DELETE " +
										"selectedID:"+selectedID+
										" SPTPD:"+txtNoSPTPD.getText()+
										" NPWPD:"+wp.getNPWP()+
										" PajakTerhutang:"+txtPajakTerutang.getText().getText());
								new LogImp().setLog(sb.toString(), userModul.getDisplayName(), userModul.getSubModul().getNamaModul());
								getDataSPTPD();
								selectedID = null;
								txtNoSPTPD.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
							}else{
								MessageDialog.openError(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Error pada Database. Silahkan hubungi ADMIN.");
							}
						}
					} else
						MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "SPTPD tidak dapat dihapus karena telah dilakukan pembayaran.");
				}else
					MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Anda tidak memiliki hak untuk menghapus data.");
			}
		});
		GridData gd_btnHapus = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnHapus.widthHint = 90;
		btnHapus.setLayoutData(gd_btnHapus);
		btnHapus.setText("Hapus");
		
		lblLunas = new Label(composite, SWT.SHADOW_IN | SWT.CENTER);
		GridData gd_lblLunas = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblLunas.widthHint = 88;
		gd_lblLunas.heightHint = 38;
		lblLunas.setLayoutData(gd_lblLunas);
		lblLunas.setText("LUNAS");
		lblLunas.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
		lblLunas.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, 15, SWT.BOLD));
		lblLunas.setAlignment(SWT.CENTER);
		lblLunas.setVisible(false);
		composite.setTabList(new Control[]{txtNoUrut, txtDasarPengenaan, txtLokasi, groupBtn});
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
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		
		scrolledComposite.setContent(composite);
		scrolledComposite.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}
	
	@SuppressWarnings("deprecation")
	private void loadOldSPTPD()
	{
		try
		{
			wp = ControllerFactory.getMainController().getCpWajibPajakDAOImpl().getNPWPD(Integer.parseInt(noUrut));
			if (wp.getNPWP() != "")
			{
//				txtKodePajak.setText(wp.getNPWP().substring(0, 1));
//				txtNoUrut.setText(wp.getNPWP().substring(1, 8));
//				txtKodeKecamatan.setText(wp.getNPWP().substring(8, 10));
//				txtKodeKelurahan.setText(wp.getNPWP().substring(10, 12));
//				txtNamaBadan.setText(wp.getNamaBadan());
//				txtNamaPemilik.setText(wp.getNamaPemilik());
//				txtAlamat.setText(wp.getAlabadJalan());
//				txtAssesment.setText(wp.getJenisAssesment());
//				txtSubPajak.setText(wp.getBidangUsaha());
//				txtTarif.setText(wp.getTarif().toString());
//				txtNoSPTPD.setText("");
//				if (txtAssesment.getText().equalsIgnoreCase("Official")){
//					chkDenda.setSelection(false);
//					chkDenda.setVisible(false);
//				}
//				else
//					chkDenda.setVisible(true);
//				
//				if (txtKodePajak.getText().equalsIgnoreCase("7")){
//					txtLokasi.setEnabled(true);
//				}
//				else{
//					txtLokasi.setEnabled(false);
//					txtLokasi.setText("");
//				}
//				
//				calTglSPTPD.setDate(dateNow.getYear(), dateNow.getMonth(), dateNow.getDate());
//				
				listSptpd = ControllerFactory.getMainController().getCpSptpdDAOImpl().getSptpd(wp.getNPWP());
				tbl_SPT.removeAll();
				
				if (listSptpd.size() > 0)
				{
					for (int i=0;i<listSptpd.size();i++)
					{
						String masaPajak = "";
						if (listSptpd.get(i).getMasaPajakDari().getMonth() + 1 == listSptpd.get(i).getMasaPajakSampai().getMonth() + 1 &&
							listSptpd.get(i).getMasaPajakDari().getYear() == listSptpd.get(i).getMasaPajakSampai().getYear()){
							masaPajak = formatMonth(listSptpd.get(i).getMasaPajakDari().getMonth() + 1, Locale.getDefault()) + " " + (listSptpd.get(i).getMasaPajakDari().getYear() + 1900);
						}else{
							masaPajak = formatMonth(listSptpd.get(i).getMasaPajakDari().getMonth() + 1, Locale.getDefault()) + " " + (listSptpd.get(i).getMasaPajakDari().getYear() + 1900) +
									" - " + formatMonth(listSptpd.get(i).getMasaPajakSampai().getMonth() + 1, Locale.getDefault()) + " " + (listSptpd.get(i).getMasaPajakSampai().getYear() + 1900);
						}
						item = new TableItem(tbl_SPT, SWT.NULL);
				        item.setText(0, listSptpd.get(i).getNoSPTPD());
				        item.setText(1, masaPajak);
				        item.setText(2, listSptpd.get(i).getNoSPTPDLama());
					}
				}
			}
		}
		catch(NumberFormatException exNumber)
		{
				exNumber.printStackTrace();
		}
		
	}
	
	@SuppressWarnings("deprecation")
	private void getDataSPTPD()
	{
		String[] colomNames = {"No SPTPD","Masa Pajak","No SPTPD Lama"};
		int[] widthColom = {160, 210, 140};
		createTableViewerColumn(colomNames[0], widthColom[0], 0);
		createTableViewerColumn(colomNames[1], widthColom[1], 1);
		createTableViewerColumn(colomNames[2], widthColom[2], 2);
		try
		{
			wp = ControllerFactory.getMainController().getCpWajibPajakDAOImpl().getNPWPD(Integer.parseInt(noUrut));
			if (wp != null)
			{
				txtKodePajak.setText(wp.getNPWP().substring(0, 1));
				txtKodePajak.pack(true);
				txtNoUrut.setLocation(txtKodePajak.getLocation().x+txtKodePajak.getSize().x+5, txtNoUrut.getLocation().y);
				txtNoUrut.setText(wp.getNPWP().substring(1, 8));
				txtNoUrut.pack(true);
				txtKodeKecamatan.setLocation(txtNoUrut.getLocation().x+txtNoUrut.getSize().x+5, txtKodeKecamatan.getLocation().y);
				txtKodeKecamatan.setText(wp.getNPWP().substring(8, 10));
				txtKodeKecamatan.pack(true);
				txtKodeKelurahan.setLocation(txtKodeKecamatan.getLocation().x+txtKodeKecamatan.getSize().x+5, txtKodeKelurahan.getLocation().y);
				txtKodeKelurahan.setText(wp.getNPWP().substring(10, 12));
				txtKodeKelurahan.pack(true);
				if (btnHapus.getSelection() == false){
					boolean akses = false;
					akses = ControllerFactory.getMainController().getCpUserDAOImpl().getAksesSptpd(txtKodePajak.getText(),GlobalVariable.userModul.getIdUser());
					if(!akses){
						//System.out.println("masuk");
						MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Anda tidak memiliki hak untuk mengakses SPTPD dengan jenis pajak ini.");
						resetForm();
						return;
					}
				}
				txtNamaBadan.setText(wp.getNamaBadan());
				txtNamaPemilik.setText(wp.getNamaPemilik());
				txtAlamat.setText(wp.getAlabadJalan());
				txtAssesment.setText(wp.getJenisAssesment());
				txtSubPajak.setText(wp.getBidangUsaha());
				kodeBidUsaha = wp.getKode_bid_usaha();
				txtTarif.setText(wp.getTarif().toString());
				if (wp.getTarif()==0){
					txtTarif.setText("-");
				}
				txtNoSPTPD.setText("");
				if (txtAssesment.getText().equalsIgnoreCase("Official")){
					chkDenda.setSelection(false);
					chkDenda.setVisible(false);
				}
				else
					chkDenda.setVisible(true);
				
				if (txtKodePajak.getText().equalsIgnoreCase("7")){
					txtLokasi.setEnabled(true);
					txtLokasi.setEditable(true);
				}
				else{
					txtLokasi.setEnabled(true);
					txtLokasi.setEditable(false);
					txtLokasi.setText("");
				}
				
				calTglSPTPD.setDate(dateNow.getYear(), dateNow.getMonth(), dateNow.getDate());
				
				listSptpd = ControllerFactory.getMainController().getCpSptpdDAOImpl().getSptpd(wp.getNPWP());
				tbl_SPT.removeAll();
				
				if (listSptpd.size() > 0)
				{
					for (int i=0;i<listSptpd.size();i++)
					{
						String masaPajak = "";
						if (wp.getNPWP().substring(0, 1).equalsIgnoreCase("7")){
							masaPajak = listSptpd.get(i).getMasaPajakDari().getDate() + " " + formatMonth(listSptpd.get(i).getMasaPajakDari().getMonth() + 1, Locale.getDefault()) + " " + (listSptpd.get(i).getMasaPajakDari().getYear() + 1900) +
									" - " + listSptpd.get(i).getMasaPajakSampai().getDate() + " " + formatMonth(listSptpd.get(i).getMasaPajakSampai().getMonth() + 1, Locale.getDefault()) + " " + (listSptpd.get(i).getMasaPajakSampai().getYear() + 1900);
						}else{
							if (listSptpd.get(i).getMasaPajakDari().getMonth() + 1 == listSptpd.get(i).getMasaPajakSampai().getMonth() + 1 &&
									listSptpd.get(i).getMasaPajakDari().getYear() == listSptpd.get(i).getMasaPajakSampai().getYear()){
									masaPajak = formatMonth(listSptpd.get(i).getMasaPajakDari().getMonth() + 1, Locale.getDefault()) + " " + (listSptpd.get(i).getMasaPajakDari().getYear() + 1900);
								}else{
									masaPajak = formatMonth(listSptpd.get(i).getMasaPajakDari().getMonth() + 1, Locale.getDefault()) + " " + (listSptpd.get(i).getMasaPajakDari().getYear() + 1900) +
											" - " + formatMonth(listSptpd.get(i).getMasaPajakSampai().getMonth() + 1, Locale.getDefault()) + " " + (listSptpd.get(i).getMasaPajakSampai().getYear() + 1900);
								}
						}
						item = new TableItem(tbl_SPT, SWT.NULL);
				        item.setText(0, listSptpd.get(i).getNoSPTPD());
				        item.setText(1, masaPajak);
				        item.setText(2, listSptpd.get(i).getNoSPTPDLama());
					}
					txtDasarPengenaan.setMoney(new CurrencyAmount(listSptpd.get(0).getDasarPengenaan(), Currency.getInstance("ind"))); //indFormat.format(Double.valueOf(Df.format(listSptpd.get(0).getDasarPengenaan().doubleValue()).replace(".", ""))));
					Calendar instance = Calendar.getInstance();
					instance.set(Calendar.DAY_OF_MONTH, listSptpd.get(0).getMasaPajakDari().getDate());
					instance.set(Calendar.MONTH, listSptpd.get(0).getMasaPajakDari().getMonth());
					instance.set(Calendar.YEAR, listSptpd.get(0).getMasaPajakDari().getYear()+1900);
					instance.add(Calendar.MONTH, 1);
					calMasaPajakAwal.setDate(instance.get(Calendar.YEAR), instance.get(Calendar.MONTH), 1);
					
					instance.set(Calendar.DAY_OF_MONTH, listSptpd.get(0).getMasaPajakSampai().getDate());
					instance.set(Calendar.MONTH, listSptpd.get(0).getMasaPajakSampai().getMonth());
					instance.set(Calendar.YEAR, listSptpd.get(0).getMasaPajakSampai().getYear()+1900);
					instance.add(Calendar.MONTH, 1);
					calMasaPajakAkhir.setDate(instance.get(Calendar.YEAR), instance.get(Calendar.MONTH), instance.getActualMaximum(Calendar.DAY_OF_MONTH));
					
				}
				else
				{
					txtDasarPengenaan.setMoney(new CurrencyAmount(0, Currency.getInstance("ind"))); 
					Calendar instance = Calendar.getInstance();
					instance.set(Calendar.MONTH, dateNow.getMonth());
					instance.set(Calendar.YEAR, dateNow.getYear()+1900);
					calMasaPajakAwal.setDate(instance.get(Calendar.YEAR), instance.get(Calendar.MONTH), 1);

					instance.set(Calendar.MONTH, dateNow.getMonth());
					instance.set(Calendar.YEAR, dateNow.getYear()+1900);
					calMasaPajakAkhir.setDate(instance.get(Calendar.YEAR), instance.get(Calendar.MONTH), instance.getActualMaximum(Calendar.DAY_OF_MONTH));
				}
			}else{
				MessageDialog.openError(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "NPWPD tidak valid. Silahkan periksa kembali");
				txtNoUrut.forceFocus();
			}
		}
		catch(NumberFormatException exNumber)
		{
			exNumber.printStackTrace();
		}
	}

	private boolean isValidated()
	{
		Control[] children = composite.getChildren();
		for (int i=0;i<children.length;i++)
		{
			if (children[i] instanceof Text)
			{
				Text child = (Text) children[i];
				
				if (child.getText().equalsIgnoreCase("") && child.getEditable() == true)
				{
					child.forceFocus();
					return false;
				}
			}
		}
		return true;
	}
	
	private boolean cekMasaPajak(Date MasaDari, Date MasaSampai, Date spt){
		boolean retValue = true;
		Date now = new Date(dateNow.getTime());
//		Timestamp tsd = new Timestamp(MasaDari.getTime());
//		Timestamp tss = new Timestamp(MasaSampai.getTime());
		if (!txtKodePajak.getText().equalsIgnoreCase("7") && MasaDari.after(now))
			retValue = false;
		else if (!txtKodePajak.getText().equalsIgnoreCase("7") && MasaSampai.after(now))
			retValue = false;
		else if (!txtKodePajak.getText().equalsIgnoreCase("7") && spt.after(now))
			retValue = false;
		
		if (!retValue)
			MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Masa Pajak/Tanggal SPTPD Invalid");
		return retValue;
	}
	public static String priceWithDecimal (Double price) {
	    DecimalFormat formatter = new DecimalFormat("###.###.###,00");
	    return formatter.format(price);
	}

	public static String priceWithoutDecimal (Double price) {
	    DecimalFormat formatter = new DecimalFormat("###.###.###,##");
	    return formatter.format(price);
	}

	public static String priceToString(Double price) {
	    String toShow = priceWithoutDecimal(price);
	    if (toShow.indexOf(".") > 0) {
	        return priceWithDecimal(price);
	    } else {
	        return priceWithoutDecimal(price);
	    }
	    
	}
	
	public String formatMonth(int month, Locale locale) {
	    DateFormatSymbols symbols = new DateFormatSymbols(locale);
	    String[] monthNames = symbols.getMonths();
	    return monthNames[month - 1];
	}
	
	@SuppressWarnings("deprecation")
	private void resetForm()
	{
		for(TableColumn column: tbl_SPT.getColumns()){
			column.dispose();
		}
		Control[] children = composite.getChildren();
		for (int i=0;i<children.length;i++)
		{
			if (children[i] instanceof Text)
			{
				Text child = (Text) children[i];
				child.setText("");
			}
		}
		selectedID = null;
		lblLunas.setVisible(false);
		btnSimpan.setEnabled(true);
		tbl_SPT.removeAll();
		txtDasarPengenaan.setMoney(new CurrencyAmount(0, Currency.getInstance("ind")));
		txtPajakTerutang.setMoney(new CurrencyAmount(0, Currency.getInstance("ind")));
		Calendar instance = Calendar.getInstance();
		instance.set(Calendar.DAY_OF_MONTH, dateNow.getDate());
		instance.set(Calendar.MONTH, dateNow.getMonth());
		instance.set(Calendar.YEAR, dateNow.getYear()+1900);
		calTglSPTPD.setDate(instance.get(Calendar.YEAR), instance.get(Calendar.MONTH), instance.get(Calendar.DATE));
		calMasaPajakAwal.setDate(instance.get(Calendar.YEAR), instance.get(Calendar.MONTH), 1);
		calMasaPajakAkhir.setDate(instance.get(Calendar.YEAR), instance.get(Calendar.MONTH), instance.getActualMaximum(Calendar.DAY_OF_MONTH));
		txtNoSPTPD.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		ChkSkpdTerbit.setSelection(false);
		lblTanggalTerbit.setVisible(false);
		calTerbit.setVisible(false);
		txtNoUrut.setFocus();
	}
	
	@Override
	public void setFocus() {
		txtNoUrut.setFocus();
	}
	
	private TableColumn createTableViewerColumn(String title, int bound, int colNumber) {
		TableColumn column = new TableColumn(tbl_SPT, SWT.NONE, colNumber);
		column.setText(title);
		column.setWidth(bound);
		column.setResizable(true);
		column.setMoveable(true);
		return column;
	}
	
	@SuppressWarnings("deprecation")
	private void selectTable(){
		txtNoSPTPD.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		int index = tbl_SPT.getSelectionIndex();
		txtNoSPTPD.setText(listSptpd.get(index).getNoSPTPD());
		txtNoSPTPD.pack();
		txtAssesment.setText(listSptpd.get(index).getAssesment()); 
		txtTarif.setText(String.valueOf(listSptpd.get(index).getTarifPajak()));
		if ((listSptpd.get(index)).getTarifPajak()==0){
			txtTarif.setText("-");
		}
		txtLokasi.setText(listSptpd.get(index).getLokasi());
		selectedID = listSptpd.get(index).getID();
		if (txtAssesment.getText().equalsIgnoreCase("Official")){
			chkDenda.setSelection(false);
			chkDenda.setVisible(false);
		}
		else
		{
			chkDenda.setVisible(true);
			if (listSptpd.get(index).getDendaTambahan() > 0)
				chkDenda.setSelection(true);
			else
				chkDenda.setSelection(false);
		}
		txtDasarPengenaan.setMoney(new CurrencyAmount(listSptpd.get(index).getDasarPengenaan(), Currency.getInstance(ind))); //))(indFormat.format(Double.valueOf(Df.format(listSptpd.get(index).getDasarPengenaan().doubleValue()).replace(".", ""))));
		Calendar instance = Calendar.getInstance();
		instance.set(Calendar.DAY_OF_MONTH, listSptpd.get(index).getMasaPajakDari().getDate());
		instance.set(Calendar.MONTH, listSptpd.get(index).getMasaPajakDari().getMonth());
		instance.set(Calendar.YEAR, listSptpd.get(index).getMasaPajakDari().getYear()+1900);
		calMasaPajakAwal.setDate(instance.get(Calendar.YEAR), instance.get(Calendar.MONTH), instance.get(Calendar.DATE));
		
		instance.set(Calendar.DAY_OF_MONTH, listSptpd.get(index).getMasaPajakSampai().getDate());
		instance.set(Calendar.MONTH, listSptpd.get(index).getMasaPajakSampai().getMonth());
		instance.set(Calendar.YEAR, listSptpd.get(index).getMasaPajakSampai().getYear()+1900);
		calMasaPajakAkhir.setDate(instance.get(Calendar.YEAR), instance.get(Calendar.MONTH), instance.get(Calendar.DATE));
		
		if (txtKodePajak.getText().equalsIgnoreCase("7")){
			Calendar nppd = Calendar.getInstance();
			nppd.set(Calendar.DAY_OF_MONTH, listSptpd.get(index).getTanggalNPPD().getDate());
			nppd.set(Calendar.MONTH, listSptpd.get(index).getTanggalNPPD().getMonth());
			nppd.set(Calendar.YEAR, listSptpd.get(index).getTanggalNPPD().getYear()+1900);
			calTglSPTPD.setDate(nppd.get(Calendar.YEAR), nppd.get(Calendar.MONTH), nppd.get(Calendar.DATE));
			
			Calendar terbit = Calendar.getInstance();
			terbit.set(Calendar.DAY_OF_MONTH, listSptpd.get(index).getTanggalSPTPD().getDate());
			terbit.set(Calendar.MONTH, listSptpd.get(index).getTanggalSPTPD().getMonth());
			terbit.set(Calendar.YEAR, listSptpd.get(index).getTanggalSPTPD().getYear()+1900);
			calTerbit.setDate(terbit.get(Calendar.YEAR), terbit.get(Calendar.MONTH), terbit.get(Calendar.DATE));
			if (terbit.after(nppd)){
				ChkSkpdTerbit.setVisible(true);
				ChkSkpdTerbit.setSelection(true);
				lblTanggalTerbit.setVisible(true);
				calTerbit.setVisible(true);
			}else{
				ChkSkpdTerbit.setVisible(true);
				ChkSkpdTerbit.setSelection(false);
				lblTanggalTerbit.setVisible(false);
				calTerbit.setVisible(false);
			}
		}else{
			instance.set(Calendar.DAY_OF_MONTH, listSptpd.get(index).getTanggalSPTPD().getDate());
			instance.set(Calendar.MONTH, listSptpd.get(index).getTanggalSPTPD().getMonth());
			instance.set(Calendar.YEAR, listSptpd.get(index).getTanggalSPTPD().getYear()+1900);
			calTglSPTPD.setDate(instance.get(Calendar.YEAR), instance.get(Calendar.MONTH), instance.get(Calendar.DATE));
		}
		
		isLunas = ControllerFactory.getMainController().getCpSspdDAOImpl().getSSPD(wp.getNPWP(), txtNoSPTPD.getText()).getLunas();
		lblLunas.setVisible(isLunas);
		btnSimpan.setEnabled(!isLunas);
		if (userModul.getIdUser()==1)
			btnHapus.setEnabled(!isLunas);
		
		if (tbl_SPT.getItem(index).getText(1).contains("-") && !txtKodePajak.getText().equalsIgnoreCase("7"))
			btnPecah.setEnabled(true);
		else
			btnPecah.setEnabled(false);
		
		//System.out.println("Dsr pngnn: "+listSptpd.get(index).getDasarPengenaan());
		/*if (listSptpd.get(index).getTarifPajak()==0){
			btnPemeriksaanBpk.setSelection(true);
			lblDasarPengenaan.setVisible(false);
			lblTarifPajak.setVisible(false);
			txtDasarPengenaan.setVisible(false);
			txtTarif.setVisible(false);
			chkDenda.setVisible(false);
			txtPajakTerutang.setEnabled(true);
			txtPajakTerutang.setMoney(new CurrencyAmount(listSptpd.get(index).getPajakterutang(), Currency.getInstance(ind)));
		}
		else{
			btnPemeriksaanBpk.setSelection(false);
			lblDasarPengenaan.setVisible(true);
			lblTarifPajak.setVisible(true);
			txtDasarPengenaan.setVisible(true);
			txtTarif.setVisible(true);
			chkDenda.setVisible(true);
			txtPajakTerutang.setEnabled(false);
		}*/
	}
	
	@SuppressWarnings("deprecation")
	private Date getSTSValid(Date masaPajakSampai, String kodePajak){
		Date stsValid;
		Calendar now = Calendar.getInstance();
		now.set(dateNow.getYear(), dateNow.getMonth(), dateNow.getDate());
		Calendar tempo = Calendar.getInstance();
		tempo.set(masaPajakSampai.getYear(), masaPajakSampai.getMonth(), masaPajakSampai.getDate());
		tempo.add(Calendar.MONTH, 1);
		if (kodePajak.equalsIgnoreCase("7")){
			stsValid = new Date(tempo.get(Calendar.YEAR), tempo.get(Calendar.MONTH), tempo.get(Calendar.DATE));
		}else{
			stsValid = new Date(tempo.get(Calendar.YEAR), tempo.get(Calendar.MONTH), tempo.getActualMaximum(Calendar.DAY_OF_MONTH));
		}
		return stsValid;
	}
	
	@SuppressWarnings("deprecation")
	private Date getYesterday(){
		Date retValue;
		Calendar now = Calendar.getInstance();
		now.set(dateNow.getYear(), dateNow.getMonth(), dateNow.getDate());
		now.add(Calendar.DATE, -1);
		retValue = new Date(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DATE));
		
		return retValue;
	}
	
	private String[] getParam(Sptpd spt){
		String[] retValue = new String[10];
		String nilai = indFormat.format(txtPajakTerutang.getMoney().getNumber().doubleValue()).substring(2).replace(".", "").replace(",", ".");
		retValue[0] = spt.getNPWPD();
		retValue[1] = txtNamaBadan.getText() + "/" + txtNamaPemilik.getText();
		retValue[2] = txtAlamat.getText();
		retValue[3] = nilai.substring(0, nilai.indexOf("."));
		retValue[4] = "0";
		retValue[5] = spt.getNoSPTPD();
		retValue[6] = spt.getTanggalSPTPD().toString();
		retValue[7] = kodeBidUsaha;
		retValue[8] = spt.getMasaPajakSampai().toString();
		retValue[9] = spt.getStsValid().toString();
		
		return retValue;
	}
	
	private UserModul userModul = ControllerFactory.getMainController().getCpUserDAOImpl()
			.getHMUserModul(GlobalVariable.userModul.getIdUser()).get(5); // 2 merupakan id sub modul.
	private TableColumn tblclmnNoSptpdLama;
	private Button ChkSkpdTerbit;
	private Label lblTanggalTerbit;
	private DateTime calTerbit;
	private Button btnPemeriksaanBpk;
	
	private boolean isSave(){
		if (!userModul.getSimpan())
			MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Anda tidak memiliki hak untuk menyimpan atau merubah data.");
		return userModul.getSimpan();
	}
	
	private boolean isDelete(){		
		return userModul.getHapus();
	}
}
