package com.dispenda.angsuran.views;

import java.math.RoundingMode;
import java.sql.Date;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.nebula.widgets.tablecombo.TableCombo;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
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
import com.dispenda.model.Mohon;
import com.dispenda.model.MohonDetail;
import com.dispenda.model.MohonDetailProvider;
import com.dispenda.model.PendaftaranWajibPajak;
import com.dispenda.model.PeriksaDetail;
import com.dispenda.model.Sptpd;
import com.dispenda.model.UserModul;
import com.dispenda.object.GlobalVariable;
import com.dispenda.object.Preferences;
import com.dispenda.widget.MoneyField;
import com.ibm.icu.util.Currency;
import com.ibm.icu.util.CurrencyAmount;


public class PermohonanView extends ViewPart {

	public static final String ID = PermohonanView.class.getName();
	public List<MohonDetail> listAngsuran = new ArrayList<MohonDetail>();
	private Text txtNoSKP;
	private Text txtAlasanMohon;
	private Text txtNoMohon;
	private Text txtNamaPemohon;
	private Text txtJabatanPemohon;
	private Text txtAlamatPemohon;
	private MoneyField txtPokokPajak;
	private MoneyField txtDenda;
	private MoneyField txtDendaSPTPD;
	private MoneyField txtTerlambatSKPDKB;
	private MoneyField txtTotalPajakTerhutang;
	private Table tbl_Angsuran;
	private MoneyField txtTotalDataAngsuran;
	private Text txtJumlahAngsuran;
	private Text txtKodePajak;
	private Text txtNoUrut;
	private Text txtKodeKecamatan;
	private Text txtKodeKelurahan;
	private Text txtNamaBadan;
	private Text txtAlamatBadan;

	private PendaftaranWajibPajak wp;
	private List<Sptpd> listMasaPajak = new ArrayList<Sptpd>();
	private Locale ind = new Locale("id", "ID");
	private NumberFormat indFormat = NumberFormat.getCurrencyInstance(ind);
	private java.sql.Timestamp dateNow;
	private Composite compo;
	@SuppressWarnings("unused")
	private String assesment;
	@SuppressWarnings("unused")
	private String subPajak;
	@SuppressWarnings("unused")
	private Integer idSubPajak;
	private Integer tarif;
	private TableCombo cmbMasaPajak;
	private Integer idMohon;
	private DateTime dateSKP;
	private DateTime dateMohon;
	private double pokokPajak;
	private double denda;
	private double dendaTambahan;
	private double kenaikan;
	private double dendaTerlambatSKPDKB;
	private double totalPajak;
	private Label lblDendaSptpd;
	private Group grpDataAngsuran;
	private Label lblNewLabel;
	private Color fontColor = new Color(Display.getCurrent(),Preferences.FONT_COLOR);
	
	public PermohonanView() {
	}
	public void createPartControl(Composite parent) {
		Locale.setDefault(new Locale("id","ID"));
		dateNow = (java.sql.Timestamp)ControllerFactory.getMainController().getCpWajibPajakDAOImpl().getDateNow();
//		parent.setBackground(new Color(Display.getCurrent(), 32, 172, 192));
		
//		RuleBasedNumberFormat format = new RuleBasedNumberFormat(Locale.getDefault(), RuleBasedNumberFormat.CURRENCYSTYLE);
		//RuleBasedNumberFormat number = new RuleBasedNumberFormat(230234);
//		System.out.println(format.format(129037.36));
		
		ScrolledComposite scrolledComposite = new ScrolledComposite(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		
		compo = new Composite(scrolledComposite, SWT.NONE);
		compo.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		compo.setLayout(new GridLayout(9, false));
		
		Label lblNpwpd = new Label(compo, SWT.NONE);
		lblNpwpd.setForeground(fontColor);
		lblNpwpd.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblNpwpd.setText("NPWPD");
		new Label(compo, SWT.NONE);
		
		txtKodePajak = new Text(compo, SWT.BORDER);
		txtKodePajak.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		txtKodePajak.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtKodePajak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtKodePajak.setEditable(false);
		GridData gd_txtKodePajak = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_txtKodePajak.widthHint = 20;
		txtKodePajak.setLayoutData(gd_txtKodePajak);
		
		txtNoUrut = new Text(compo, SWT.BORDER);
		txtNoUrut.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		txtNoUrut.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.keyCode == SWT.CR || e.keyCode == SWT.KEYPAD_CR)
				{
					//resetForm();
					cmbMasaPajak.select(-1);
					cmbMasaPajak.setText("");
					getDataWP();

//					ReportModule rm = new ReportModule();
//					System.out.println(rm.GetNPWPDFormatByDot(wp.getNPWP()));
				}
			}
		});
		txtNoUrut.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtNoUrut.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		GridData gd_txtNoUrut = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_txtNoUrut.widthHint = 70;
		txtNoUrut.setLayoutData(gd_txtNoUrut);
		
		txtKodeKecamatan = new Text(compo, SWT.BORDER);
		txtKodeKecamatan.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		txtKodeKecamatan.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtKodeKecamatan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtKodeKecamatan.setEditable(false);
		GridData gd_txtKodeKecamatan = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_txtKodeKecamatan.widthHint = 20;
		txtKodeKecamatan.setLayoutData(gd_txtKodeKecamatan);
		
		txtKodeKelurahan = new Text(compo, SWT.BORDER);
		txtKodeKelurahan.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		txtKodeKelurahan.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtKodeKelurahan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtKodeKelurahan.setEditable(false);
		GridData gd_txtKodeKelurahan = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_txtKodeKelurahan.widthHint = 20;
		txtKodeKelurahan.setLayoutData(gd_txtKodeKelurahan);
		new Label(compo, SWT.NONE);
		new Label(compo, SWT.NONE);
		
		lblNewLabel = new Label(compo, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		
		Label lblNamaBadan = new Label(compo, SWT.NONE);
		lblNamaBadan.setForeground(fontColor);
		lblNamaBadan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblNamaBadan.setText("Nama Badan");
		new Label(compo, SWT.NONE);
		
		txtNamaBadan = new Text(compo, SWT.BORDER);
		txtNamaBadan.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		txtNamaBadan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtNamaBadan.setEditable(false);
		txtNamaBadan.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtNamaBadan.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1));
		new Label(compo, SWT.NONE);
		new Label(compo, SWT.NONE);
		new Label(compo, SWT.NONE);
		
		Label lblAlamatBadan = new Label(compo, SWT.NONE);
		lblAlamatBadan.setForeground(fontColor);
		lblAlamatBadan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblAlamatBadan.setText("Alamat Badan");
		new Label(compo, SWT.NONE);
		
		txtAlamatBadan = new Text(compo, SWT.BORDER);
		txtAlamatBadan.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		txtAlamatBadan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtAlamatBadan.setEditable(false);
		txtAlamatBadan.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtAlamatBadan.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 4, 1));
		new Label(compo, SWT.NONE);
		new Label(compo, SWT.NONE);
		new Label(compo, SWT.NONE);
		
		Label lblMasaPajak = new Label(compo, SWT.NONE);
		lblMasaPajak.setForeground(fontColor);
		lblMasaPajak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblMasaPajak.setText("Masa Pajak");
		new Label(compo, SWT.NONE);
		
		cmbMasaPajak = new TableCombo(compo, SWT.READ_ONLY);
		cmbMasaPajak.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		cmbMasaPajak.addSelectionListener(new SelectionAdapter() {
			@SuppressWarnings("deprecation")
			@Override
			public void widgetSelected(SelectionEvent e) {
				idMohon = null;
				pokokPajak = 0;
				denda = 0;
				kenaikan = 0;
				dendaTerlambatSKPDKB = 0;
				resetInputForm();
				txtNoSKP.setText(cmbMasaPajak.getData(Integer.toString(cmbMasaPajak.getSelectionIndex())).toString());
				dateSKP.setDate((listMasaPajak.get(cmbMasaPajak.getSelectionIndex()).getTanggalSPTPD().getYear() + 1900), listMasaPajak.get(cmbMasaPajak.getSelectionIndex()).getTanggalSPTPD().getMonth(), listMasaPajak.get(cmbMasaPajak.getSelectionIndex()).getTanggalSPTPD().getDate());
				String keteranganSKPDKB = ControllerFactory.getMainController().getCpPeriksaDAOImpl().GetKeteranganSKPDKB(txtNoSKP.getText(), wp.npwp);
				Integer idPeriksa = ControllerFactory.getMainController().getCpPeriksaDAOImpl().GetIdPeriksa(txtNoSKP.getText(), wp.getNPWP());
				if (keteranganSKPDKB.contains("Pemeriksaan BPK")){
					denda = ControllerFactory.getMainController().getCpPeriksaDetailBPKDAOImpl().GetDendaSKPDKB(idPeriksa);
				}else{
					denda = ControllerFactory.getMainController().getCpPeriksaDAOImpl().GetDendaSKPDKB(txtNoSKP.getText(), wp.getNPWP());
				}
				
				if (txtNoSKP.getText().indexOf("SKPDKBT/") > 1)
				{
//					int idPeriksa = ControllerFactory.getMainController().getCpPeriksaDAOImpl().GetIdPeriksa(txtNoSKP.getText(), wp.getNPWP());
					List<PeriksaDetail> pd = ControllerFactory.getMainController().getCpPeriksaDAOImpl().GetPajakSKPDKBT(idPeriksa, wp.getNPWP()); 
					for (int i=0;i<pd.size();i++){
						pokokPajak += pd.get(i).getSelisihKurang() * tarif / 100;
						kenaikan += pd.get(i).getKenaikanPajak();
					}
					lblDendaSptpd.setText("Kenaikan 100%");
					//kenaikan = ControllerFactory.getMainController().getCpPeriksaDAOImpl().GetKenaikanSKPDKB(txtNoSKP.getText(), wp.getNPWP());  
//					txtDendaSPTPD.setMoney(new CurrencyAmount(kenaikan, Currency.getInstance("ind")));
				}
				else
				{
					if (keteranganSKPDKB.contains("Pemeriksaan BPK")){
						pokokPajak = ControllerFactory.getMainController().getCpPeriksaDetailBPKDAOImpl().GetPajakSKPDKB(idPeriksa);
					}else{
						pokokPajak = ControllerFactory.getMainController().getCpPeriksaDAOImpl().GetPajakSKPDKB(txtNoSKP.getText(), wp.getNPWP());
					}
					lblDendaSptpd.setText("Denda SPTPD");
					kenaikan = ControllerFactory.getMainController().getCpPeriksaDAOImpl().GetKenaikanSKPDKB(txtNoSKP.getText(), wp.getNPWP());  
//					txtDendaSPTPD.setMoney(new CurrencyAmount(kenaikan, Currency.getInstance("ind")));
				}
//				txtPokokPajak.setMoney(new CurrencyAmount(pokokPajak, Currency.getInstance("ind")));
//				txtDenda.setMoney(new CurrencyAmount(denda, Currency.getInstance("ind")));
//				dendaTerlambatSKPDKB = hitungDendaTerlambatSKPDKB();
				txtTerlambatSKPDKB.setMoney(new CurrencyAmount(dendaTerlambatSKPDKB, Currency.getInstance("ind")));
				totalPajak = pokokPajak + denda + kenaikan + dendaTerlambatSKPDKB; 
				txtTotalPajakTerhutang.setMoney(new CurrencyAmount(totalPajak, Currency.getInstance("ind")));
				
				idMohon = ControllerFactory.getMainController().getCpMohonDAOImpl().checkMohon(wp.getNPWP(), txtNoSKP.getText());
				if (idMohon != null)
					loadPermohonan(idMohon);
				else
					txtTotalDataAngsuran.setMoney(new CurrencyAmount(0, Currency.getInstance("ind")));
				
				hitungPajak();
			}
		});
		cmbMasaPajak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		cmbMasaPajak.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		cmbMasaPajak.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1));
		cmbMasaPajak.defineColumns(2);
		cmbMasaPajak.setDisplayColumnIndex(1);
		new Label(compo, SWT.NONE);
		new Label(compo, SWT.NONE);
		new Label(compo, SWT.NONE);
		
		Label lblNoSkp = new Label(compo, SWT.NONE);
		lblNoSkp.setText("No. SKP");
		lblNoSkp.setForeground(fontColor);
		lblNoSkp.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		new Label(compo, SWT.NONE);
		
		txtNoSKP = new Text(compo, SWT.BORDER | SWT.READ_ONLY);
		txtNoSKP.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		txtNoSKP.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtNoSKP.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtNoSKP.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 4, 1));
		
		Label lblPokokPajak = new Label(compo, SWT.NONE);
		GridData gd_lblPokokPajak = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblPokokPajak.horizontalIndent = 100;
		lblPokokPajak.setLayoutData(gd_lblPokokPajak);
		lblPokokPajak.setForeground(fontColor);
		lblPokokPajak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblPokokPajak.setText("Pokok Pajak");
		
		txtPokokPajak = new MoneyField(compo, SWT.BORDER);
		txtPokokPajak.getText().setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		txtPokokPajak.getText().setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtPokokPajak.getText().setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtPokokPajak.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		txtPokokPajak.setEnabled(false);
		txtPokokPajak.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		txtPokokPajak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		GridData gd_txtPokokPajak = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_txtPokokPajak.heightHint = 23;
		txtPokokPajak.setLayoutData(gd_txtPokokPajak);
		new Label(txtPokokPajak, SWT.NONE);
		new Label(txtPokokPajak, SWT.NONE);
		new Label(txtPokokPajak, SWT.NONE);
		new Label(compo, SWT.NONE);
		
		Label lblTanggalSkpd = new Label(compo, SWT.NONE);
		lblTanggalSkpd.setForeground(fontColor);
		lblTanggalSkpd.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblTanggalSkpd.setText("Tanggal SKP");
		new Label(compo, SWT.NONE);
		
		dateSKP = new DateTime(compo, SWT.BORDER | SWT.DROP_DOWN);
		dateSKP.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		dateSKP.setEnabled(false);
		dateSKP.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 4, 1));
		dateSKP.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		dateSKP.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		Label lblDenda = new Label(compo, SWT.NONE);
		GridData gd_lblDenda = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblDenda.horizontalIndent = 100;
		lblDenda.setLayoutData(gd_lblDenda);
		lblDenda.setForeground(fontColor);
		lblDenda.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblDenda.setText("Denda");
		
		txtDenda = new MoneyField(compo, SWT.BORDER);
		txtDenda.getText().setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		txtDenda.getText().setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtDenda.getText().setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtDenda.setEnabled(false);
		txtDenda.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		txtDenda.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		GridData gd_txtDenda = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_txtDenda.heightHint = 23;
		txtDenda.setLayoutData(gd_txtDenda);
		new Label(txtDenda, SWT.NONE);
		new Label(txtDenda, SWT.NONE);
		new Label(txtDenda, SWT.NONE);
		new Label(compo, SWT.NONE);
		
		Label lblAlasanMohon = new Label(compo, SWT.NONE);
		lblAlasanMohon.setForeground(fontColor);
		lblAlasanMohon.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblAlasanMohon.setText("Alasan Mohon");
		new Label(compo, SWT.NONE);
		
		txtAlasanMohon = new Text(compo, SWT.BORDER);
		txtAlasanMohon.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		txtAlasanMohon.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtAlasanMohon.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtAlasanMohon.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 4, 3));
		
		lblDendaSptpd = new Label(compo, SWT.NONE);
		GridData gd_lblDendaSptpd = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblDendaSptpd.horizontalIndent = 100;
		lblDendaSptpd.setLayoutData(gd_lblDendaSptpd);
		lblDendaSptpd.setForeground(fontColor);
		lblDendaSptpd.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblDendaSptpd.setText("Denda SPTPD");
		
		txtDendaSPTPD = new MoneyField(compo, SWT.BORDER);
		txtDendaSPTPD.getText().setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		txtDendaSPTPD.getText().setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtDendaSPTPD.getText().setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtDendaSPTPD.setEnabled(false);
		txtDendaSPTPD.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		txtDendaSPTPD.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		GridData gd_txtDendaSPTPD = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_txtDendaSPTPD.heightHint = 23;
		txtDendaSPTPD.setLayoutData(gd_txtDendaSPTPD);
		new Label(txtDendaSPTPD, SWT.NONE);
		new Label(txtDendaSPTPD, SWT.NONE);
		new Label(txtDendaSPTPD, SWT.NONE);
		new Label(compo, SWT.NONE);
		new Label(compo, SWT.NONE);
		new Label(compo, SWT.NONE);
		
		Label lblDendaKeterlambatanSkpdkb = new Label(compo, SWT.NONE);
		GridData gd_lblDendaKeterlambatanSkpdkb = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblDendaKeterlambatanSkpdkb.horizontalIndent = 100;
		lblDendaKeterlambatanSkpdkb.setLayoutData(gd_lblDendaKeterlambatanSkpdkb);
		lblDendaKeterlambatanSkpdkb.setForeground(fontColor);
		lblDendaKeterlambatanSkpdkb.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblDendaKeterlambatanSkpdkb.setText("Denda Keterlambatan SKPDKB");
		
		txtTerlambatSKPDKB = new MoneyField(compo, SWT.BORDER);
		txtTerlambatSKPDKB.getText().setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		txtTerlambatSKPDKB.getText().setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtTerlambatSKPDKB.getText().setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtTerlambatSKPDKB.setEnabled(false);
		txtTerlambatSKPDKB.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		txtTerlambatSKPDKB.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		GridData gd_txtTerlambatSKPDKB = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_txtTerlambatSKPDKB.heightHint = 23;
		txtTerlambatSKPDKB.setLayoutData(gd_txtTerlambatSKPDKB);
		new Label(txtTerlambatSKPDKB, SWT.NONE);
		new Label(txtTerlambatSKPDKB, SWT.NONE);
		new Label(txtTerlambatSKPDKB, SWT.NONE);
		new Label(compo, SWT.NONE);
		new Label(compo, SWT.NONE);
		new Label(compo, SWT.NONE);
		
		Label lblTotalPajakTerhutang = new Label(compo, SWT.NONE);
		GridData gd_lblTotalPajakTerhutang = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblTotalPajakTerhutang.horizontalIndent = 100;
		lblTotalPajakTerhutang.setLayoutData(gd_lblTotalPajakTerhutang);
		lblTotalPajakTerhutang.setForeground(fontColor);
		lblTotalPajakTerhutang.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblTotalPajakTerhutang.setText("TOTAL PAJAK TERHUTANG");
		
		txtTotalPajakTerhutang = new MoneyField(compo, SWT.BORDER);
		txtTotalPajakTerhutang.getText().setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		txtTotalPajakTerhutang.getText().setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtTotalPajakTerhutang.getText().setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtTotalPajakTerhutang.setEnabled(false);
		txtTotalPajakTerhutang.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		txtTotalPajakTerhutang.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		GridData gd_txtTotalPajakTerhutang = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_txtTotalPajakTerhutang.heightHint = 23;
		txtTotalPajakTerhutang.setLayoutData(gd_txtTotalPajakTerhutang);
		new Label(txtTotalPajakTerhutang, SWT.NONE);
		new Label(txtTotalPajakTerhutang, SWT.NONE);
		new Label(txtTotalPajakTerhutang, SWT.NONE);
		new Label(compo, SWT.NONE);
		
		Label lblTanggalMohon = new Label(compo, SWT.NONE);
		lblTanggalMohon.setForeground(fontColor);
		lblTanggalMohon.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblTanggalMohon.setText("Tanggal Mohon");
		new Label(compo, SWT.NONE);
		
		dateMohon = new DateTime(compo, SWT.BORDER | SWT.DROP_DOWN);
		dateMohon.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		dateMohon.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				hitungPajak();
			}
		});
		dateMohon.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 4, 1));
		dateMohon.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		dateMohon.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		grpDataAngsuran = new Group(compo, SWT.NONE);
		grpDataAngsuran.setLayout(new GridLayout(4, false));
		grpDataAngsuran.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 3, 6));
		grpDataAngsuran.addPaintListener(new PaintListener() {
			@Override
			public void paintControl(PaintEvent pe) {
				pe.gc.setForeground(fontColor);
				pe.gc.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
				pe.gc.drawText("Data Angsuran", 5,0, false);
			}
		});
		
		tbl_Angsuran = new Table(grpDataAngsuran, SWT.BORDER | SWT.FULL_SELECTION | SWT.VIRTUAL);
		tbl_Angsuran.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		tbl_Angsuran.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		tbl_Angsuran.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE-1, SWT.NORMAL));
		tbl_Angsuran.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 4, 1));
		tbl_Angsuran.setHeaderVisible(true);
		tbl_Angsuran.setLinesVisible(true);
		
		final TableEditor editor = new TableEditor(tbl_Angsuran);
        editor.horizontalAlignment = SWT.LEFT;
        editor.grabHorizontal = true;
        editor.minimumWidth = 50;
        final int EDITABLECOLUMN = 1;
        tbl_Angsuran.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.keyCode==SWT.CR || e.keyCode == SWT.KEYPAD_CR){
					if(tbl_Angsuran.getSelectionCount()>0){
						Control oldEditor = editor.getEditor();
	                	if (oldEditor != null) oldEditor.dispose();
	        
	                	// Identify the selected row
	                	TableItem item = tbl_Angsuran.getItem(tbl_Angsuran.getSelectionIndex());
	                	if (item == null) return;
	        
                        // The control that will be the editor must be a child of the Table
	                	DateTime newEditor = new DateTime(tbl_Angsuran, SWT.DROP_DOWN);
	                	String split[] = (item.getText(EDITABLECOLUMN)).split("-");
	                	newEditor.setDate(Integer.parseInt(split[2]), Integer.parseInt(split[1])-1, Integer.parseInt(split[0]));
	                	
	                	Listener textListener = new Listener() {
	                        private DateTime dateTime;
							public void handleEvent(final Event e) {
	                          switch (e.type) {
	                          case SWT.Traverse:
	                            switch (e.detail) {
	                            case SWT.TRAVERSE_RETURN:
	                            	dateTime = (DateTime)editor.getEditor();
	                            	editor.getItem().setText(EDITABLECOLUMN, String.format("%02d", dateTime.getDay())+"-"+String.format("%02d", dateTime.getMonth()+1)+"-"+Integer.toString(dateTime.getYear()));
	                            	tbl_Angsuran.setSelection(tbl_Angsuran.getSelectionIndex()+1);
	                            case SWT.TRAVERSE_ESCAPE:
	                            	dateTime = (DateTime)editor.getEditor();
	                            	dateTime.dispose();
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
		});
		
		Label lblJumlah = new Label(grpDataAngsuran, SWT.NONE);
		lblJumlah.setForeground(fontColor);
		lblJumlah.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblJumlah.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblJumlah.setText("Jumlah :");
		
		txtTotalDataAngsuran = new MoneyField(grpDataAngsuran, SWT.BORDER);
		txtTotalDataAngsuran.getText().setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		txtTotalDataAngsuran.getText().setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtTotalDataAngsuran.getText().setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtTotalDataAngsuran.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		txtTotalDataAngsuran.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		GridData gd_txtTotalDataAngsuran = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_txtTotalDataAngsuran.heightHint = 23;
		txtTotalDataAngsuran.setLayoutData(gd_txtTotalDataAngsuran);
		new Label(txtTotalDataAngsuran, SWT.NONE);
		new Label(txtTotalDataAngsuran, SWT.NONE);
		new Label(txtTotalDataAngsuran, SWT.NONE);
		
		txtJumlahAngsuran = new Text(grpDataAngsuran, SWT.BORDER);
		txtJumlahAngsuran.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		txtJumlahAngsuran.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtJumlahAngsuran.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		GridData gd_txtJumlahAngsuran = new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1);
		gd_txtJumlahAngsuran.widthHint = 20;
		txtJumlahAngsuran.setLayoutData(gd_txtJumlahAngsuran);
		
		Button btnAngsuran = new Button(grpDataAngsuran, SWT.NONE);
		GridData gd_btnAngsuran = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnAngsuran.widthHint = 90;
		btnAngsuran.setLayoutData(gd_btnAngsuran);
		btnAngsuran.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
//				if(listAngsuran != null)
//					listAngsuran.clear();
				int jlh = Integer.parseInt(txtJumlahAngsuran.getText());
				double pokok = roundUP((pokokPajak + kenaikan) / jlh); //(txtPokokPajak.getMoney().getNumber().doubleValue() + txtDendaSPTPD.getMoney().getNumber().doubleValue()) / jlh;
				double bunga = roundUP(denda / jlh); //txtDenda.getMoney().getNumber().doubleValue() / jlh;
				double denda = roundUP(dendaTerlambatSKPDKB / jlh); //txtTerlambatSKPDKB.getMoney().getNumber().doubleValue() / jlh;
				double total = pokok + denda + bunga;
				double totalAngsuran = 0;
				tbl_Angsuran.removeAll();
				createTableColumn();
				for (int i=0; i<jlh;i++)
				{
					String[] now = dateNow.toString().split(" ");
					SimpleDateFormat simpleDate = new SimpleDateFormat("dd-MM-yyyy");
//					item.setText(1, String.valueOf(simpleDate.format((Date)listAngsuran.get(i).getTglAngsuran())));
					TableItem item = new TableItem(tbl_Angsuran, SWT.NONE);
					item.setText(0, String.valueOf((i + 1)));
					item.setText(1, simpleDate.format(Date.valueOf(now[0])));
					item.setText(2, String.valueOf(indFormat.format(pokok)));
					item.setText(3, String.valueOf(indFormat.format(bunga)));
					item.setText(4, String.valueOf(indFormat.format(denda)));
					item.setText(5, String.valueOf(indFormat.format(total)));	
					totalAngsuran += total;
//					md.setIdMohon(idMohon);
//					md.setIdMohonDetail(null);
//					md.setNoAngsuran(i + 1);
//					String[] now = dateNow.toString().split(" ");
//					md.setTglAngsuran(Date.valueOf(now[0]));
//					md.setAngsuranPokok(pokok);
//					md.setBiayaAdministrasi(bunga);
//					md.setDendaSkpdkb(denda);
//					listAngsuran.add(md);
				}
				txtTotalDataAngsuran.setMoney(new CurrencyAmount(totalAngsuran, Currency.getInstance("ind")));
			}
		});
		btnAngsuran.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnAngsuran.setText("Angsuran");
		grpDataAngsuran.setTabList(new Control[]{txtJumlahAngsuran, btnAngsuran});
		
		Label lblNoMohon = new Label(compo, SWT.NONE);
		lblNoMohon.setForeground(fontColor);
		lblNoMohon.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblNoMohon.setText("No. Mohon");
		new Label(compo, SWT.NONE);
		
		txtNoMohon = new Text(compo, SWT.BORDER);
		txtNoMohon.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		txtNoMohon.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtNoMohon.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtNoMohon.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 4, 1));
		
		Label lblNamaPemohon = new Label(compo, SWT.NONE);
		lblNamaPemohon.setForeground(fontColor);
		lblNamaPemohon.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblNamaPemohon.setText("Nama Pemohon");
		new Label(compo, SWT.NONE);
		
		txtNamaPemohon = new Text(compo, SWT.BORDER);
		txtNamaPemohon.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		txtNamaPemohon.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtNamaPemohon.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtNamaPemohon.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 4, 1));
		
		Label lblJabatanPemohon = new Label(compo, SWT.NONE);
		lblJabatanPemohon.setForeground(fontColor);
		lblJabatanPemohon.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblJabatanPemohon.setText("Jabatan Pemohon");
		new Label(compo, SWT.NONE);
		
		txtJabatanPemohon = new Text(compo, SWT.BORDER);
		txtJabatanPemohon.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		txtJabatanPemohon.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtJabatanPemohon.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtJabatanPemohon.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 4, 1));
		
		Label lblAlamatPemohon = new Label(compo, SWT.NONE);
		lblAlamatPemohon.setForeground(fontColor);
		lblAlamatPemohon.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblAlamatPemohon.setText("Alamat Pemohon");
		new Label(compo, SWT.NONE);
		
		txtAlamatPemohon = new Text(compo, SWT.BORDER);
		txtAlamatPemohon.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		txtAlamatPemohon.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtAlamatPemohon.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		GridData gd_txtAlamatPemohon = new GridData(SWT.FILL, SWT.TOP, false, false, 4, 1);
		gd_txtAlamatPemohon.widthHint = 270;
		txtAlamatPemohon.setLayoutData(gd_txtAlamatPemohon);
		new Label(compo, SWT.NONE);
		new Label(compo, SWT.NONE);
		
		Composite compButton = new Composite(compo, SWT.NONE);
		compButton.setLayout(new GridLayout(3, false));
		GridData gd_compButton = new GridData(SWT.FILL, SWT.CENTER, false, false, 4, 1);
		gd_compButton.heightHint = 40;
		compButton.setLayoutData(gd_compButton);
		
		Button btnSimpan = new Button(compButton, SWT.NONE);
		GridData gd_btnSimpan = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_btnSimpan.widthHint = 90;
		btnSimpan.setLayoutData(gd_btnSimpan);
		btnSimpan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnSimpan.setText("Simpan");
		btnSimpan.addSelectionListener(new SelectionAdapter() {
			@SuppressWarnings("deprecation")
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(isSave()){
					if(isValidate()){
						if (MohonDetailProvider.INSTANCE.getMohonDetail() != null)
							MohonDetailProvider.INSTANCE.getMohonDetail().clear();
							Mohon mohon = new Mohon();
							Date tglMohon = new Date(dateMohon.getYear() - 1900, dateMohon.getMonth(), dateMohon.getDay());
							Date tglTempo = new Date(0001, 0, 01);
							mohon.setIdMohon(idMohon);
							mohon.setNoMohon(txtNoMohon.getText());
							mohon.setTglMohon(tglMohon);
							mohon.setTglJatuhTempo(tglTempo);
							mohon.setJenisMohon(2);
							mohon.setAlasanMohon(txtAlasanMohon.getText());
							mohon.setNpwpd(wp.getNPWP());
							mohon.setNoSkp(txtNoSKP.getText());
							mohon.setStatusMohon(0);
							mohon.setNamaPemohon(txtNamaPemohon.getText());
							mohon.setJabatanPemohon(txtJabatanPemohon.getText());
							mohon.setAlamatPemohon(txtAlamatPemohon.getText());
							
							try{
								for(TableItem item : tbl_Angsuran.getItems()){
									MohonDetail md = new MohonDetail();
									md.setIdMohon(idMohon);
									md.setIdMohonDetail(null);
									md.setNoAngsuran(Integer.parseInt(item.getText(0)));
									String[] tgl = item.getText(1).split("-");
									md.setTglAngsuran(Date.valueOf(tgl[2] + "-" + tgl[1] + "-" + tgl[0]));
									md.setAngsuranPokok((indFormat.parse(item.getText(2))).doubleValue());
									md.setBiayaAdministrasi((indFormat.parse(item.getText(3))).doubleValue());
									md.setDendaSkpdkb((indFormat.parse(item.getText(4))).doubleValue());
									md.setSts(null);
									md.setStsValid(null);
									MohonDetailProvider.INSTANCE.addItem(md);
								}
							}
							catch (ParseException e1){
								e1.printStackTrace();
							}
							
						if(ControllerFactory.getMainController().getCpMohonDAOImpl().saveMohon(mohon)){
							if (idMohon == null)
								idMohon = ControllerFactory.getMainController().getCpMohonDAOImpl().getLastIdMohon(wp.getNPWP());
							if (ControllerFactory.getMainController().getCpMohonDetailDAOImpl().saveMohonDetail(MohonDetailProvider.INSTANCE.getMohonDetail(), idMohon)){
								MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Data berhasil disimpan.");
								StringBuffer sb = new StringBuffer();
								sb.append("SAVE " +
										"idMohon:"+idMohon+
										" SKPDKB:"+txtNoSKP.getText()+
										" MasaPajak:"+cmbMasaPajak.getText()+
										" NPWPD:"+wp.getNPWP()+
										" PokokPajak:"+txtPokokPajak.getText().getText()+
										" Denda:"+txtDenda.getText().getText()+
										" DendaSPTPD:"+txtDendaSPTPD.getText().getText()+
										" DendaKeterlambatanSKPDKB:"+txtTerlambatSKPDKB.getText().getText()+
										" Angsuran:"+txtJumlahAngsuran.getText()+
										" TotalPajak:"+txtTotalPajakTerhutang.getText().getText());
								new LogImp().setLog(sb.toString(), userModul.getDisplayName(), userModul.getSubModul().getNamaModul());
	//							load();
							}else{
								MessageDialog.openError(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Error pada Database. Silahkan hubungi ADMIN.");
							}
						}else{
							MessageDialog.openError(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Error pada Database. Silahkan hubungi ADMIN.");
						}
					}else
						MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Silahkan periksa kembali.");
				}else
					MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Anda tidak memiliki hak untuk menyimpan atau merubah data.");
			}
		});
		
		Button btnHapus = new Button(compButton, SWT.NONE);
		btnHapus.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(isDelete()){
					int style = SWT.ICON_QUESTION |SWT.YES | SWT.NO;
					if (idMohon == null){
						MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Tidak ada data permohonan untuk dihapus");
					}
					else {
						if (MessageDialog.open(6, Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Hapus data (" + txtNoSKP.getText() + ")?", style)){
							if (ControllerFactory.getMainController().getCpMohonDAOImpl().deleteMohon(idMohon))
							{
								MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Data berhasil dihapus.");
								StringBuffer sb = new StringBuffer();
								sb.append("DELETE " +
										"idMohon:"+idMohon+
										" MasaPajak:"+cmbMasaPajak.getText()+
										" NPWPD:"+wp.getNPWP()+
										" PokokPajak:"+txtPokokPajak.getText().getText()+
										" Denda:"+txtDenda.getText().getText()+
										" DendaSPTPD:"+txtDendaSPTPD.getText().getText()+
										" DendaKeterlambatanSKPDKB:"+txtTerlambatSKPDKB.getText().getText()+
										" Angsuran:"+txtJumlahAngsuran.getText()+
										" TotalPajak:"+txtTotalPajakTerhutang.getText().getText());
								new LogImp().setLog(sb.toString(), userModul.getDisplayName(), userModul.getSubModul().getNamaModul());
								resetInputForm();
							}else{
								MessageDialog.openError(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Error pada Database. Silahkan hubungi ADMIN.");
							}
						}
					}
				}else
					MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Anda tidak memiliki hak untuk menghapus data.");
			}
		});
		GridData gd_btnHapus = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_btnHapus.widthHint = 90;
		btnHapus.setLayoutData(gd_btnHapus);
		btnHapus.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnHapus.setText("Hapus");
		
		Button btnReset = new Button(compButton, SWT.NONE);
		btnReset.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				resetForm();
			}
		});
		GridData gd_btnReset = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnReset.widthHint = 90;
		btnReset.setLayoutData(gd_btnReset);
		btnReset.setText("Reset");
		btnReset.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		compo.setTabList(new Control[]{txtNoUrut, cmbMasaPajak, txtAlasanMohon, txtNoMohon, txtNamaPemohon, txtJabatanPemohon, txtAlamatPemohon, grpDataAngsuran, compButton});
		
		scrolledComposite.setContent(compo);
		scrolledComposite.setMinSize(compo.computeSize(SWT.DEFAULT, SWT.DEFAULT));

	}

	public void setFocus() {
		txtNoUrut.setFocus();
	}
	
	private boolean isValidate(){
		boolean isLunas = ControllerFactory.getMainController().getCpSspdDAOImpl().getSSPD(wp.getNPWP(), txtNoSKP.getText()).getLunas();
		if (isLunas){
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Permohonan tidak dapat diproses karena SKPDKB telah dibayar");
			return false;
		}
		else if(txtNoSKP.getText().equalsIgnoreCase("") ||
			txtAlasanMohon.getText().equalsIgnoreCase("") ||
//			txtNoMohon.getText().equalsIgnoreCase("") ||
			txtNamaPemohon.getText().equalsIgnoreCase("") ||
			txtJabatanPemohon.getText().equalsIgnoreCase("") ||
			txtAlamatPemohon.getText().equalsIgnoreCase("") ||
			txtPokokPajak.getMoney().toString().equalsIgnoreCase("") ||
			txtDenda.getMoney().toString().equalsIgnoreCase("") ||
			txtDendaSPTPD.getMoney().toString().equalsIgnoreCase("") ||
			txtTerlambatSKPDKB.getMoney().toString().equalsIgnoreCase("") ||
			txtTotalPajakTerhutang.getMoney().toString().equalsIgnoreCase("") ||
			txtTotalDataAngsuran.getMoney().toString().equalsIgnoreCase("") ||
			tbl_Angsuran.getItemCount() < 1
			//txtJumlahAngsuran.getMoney().toString().equalsIgnoreCase("") 
			){
			return false;
		}else
			return true;
	}
	
	private void getDataWP()
	{
		wp = ControllerFactory.getMainController().getCpWajibPajakDAOImpl().getNPWPD(Integer.parseInt(txtNoUrut.getText().trim()));
		if (wp.getNPWP() != "")
		{
			resetForm();
			txtKodePajak.setText(wp.getNPWP().substring(0, 1));
			txtKodePajak.pack(true);
			txtNoUrut.setText(wp.getNPWP().substring(1, 8));
			txtNoUrut.pack(true);
			txtNoUrut.setLocation(txtKodePajak.getLocation().x+txtKodePajak.getSize().x+5, txtNoUrut.getLocation().y);
			txtKodeKecamatan.setText(wp.getNPWP().substring(8, 10));
			txtKodeKecamatan.pack(true);
			txtKodeKecamatan.setLocation(txtNoUrut.getLocation().x+txtNoUrut.getSize().x+5, txtKodeKecamatan.getLocation().y);
			txtKodeKelurahan.setText(wp.getNPWP().substring(10, 12));
			txtKodeKelurahan.pack(true);
			txtKodeKelurahan.setLocation(txtKodeKecamatan.getLocation().x+txtKodeKecamatan.getSize().x+5, txtKodeKelurahan.getLocation().y);
			txtNamaBadan.setText(wp.getNamaPemilik() + " / " + wp.getNamaBadan());
			txtAlamatBadan.setText(wp.getAlabadJalan());
			assesment = wp.getJenisAssesment();
			subPajak = wp.getBidangUsaha();
			idSubPajak = wp.getIdSubPajak();
			tarif = wp.getTarif();
//			txtNoSPTPD.setText("");
//			txtNoSSPD.setText("");
			
			listMasaPajak = ControllerFactory.getMainController().getCpSptpdDAOImpl().getMasaPajakSKPDKB(wp.getNPWP());
			
			if (listMasaPajak.size() > 0)
			{
				UIController.INSTANCE.loadTMasaPajak(cmbMasaPajak, listMasaPajak.toArray()); //PajakProvider.INSTANCE.getPajak().toArray());
				
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	private void resetForm()
	{
		txtTotalDataAngsuran.setMoney(new CurrencyAmount(0, Currency.getInstance("ind")));
		txtJumlahAngsuran.setText("");
		Control[] children = compo.getChildren();
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
			
			else if (children[i] instanceof MoneyField)
			{
				MoneyField child = (MoneyField) children[i];
				child.setMoney(new CurrencyAmount(0, Currency.getInstance("ind")));
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
		idMohon = 0;
		tbl_Angsuran.removeAll();
		listAngsuran.clear();
		MohonDetailProvider.INSTANCE.getMohonDetail().clear();
		txtNoUrut.setFocus();
		cmbMasaPajak.select(-1);
		cmbMasaPajak.setText("");
		cmbMasaPajak.getTable().removeAll();
//		for (int i=0;i<cmbMasaPajak.getItemCount();i++){
////			cmbMasaPajak.getItem(i).
//		}
//		txtDibayar.setMoney(new CurrencyAmount(0, Currency.getInstance("ind")));
//		txtTotalPajakTerhutang.setMoney(new CurrencyAmount(0, Currency.getInstance("ind")));
//		table.removeAll();
//		tableAngsuran.removeAll();
//		tableAngsuran.setVisible(false);
//		lblLunas.setVisible(false);
//		lblLokasi.setVisible(false);
//		txtLokasi.setVisible(false);
	}
	
	@SuppressWarnings("deprecation")
	private double hitungDendaTerlambatSKPDKB(double pajak)
	{
		double retValue = 0;
		Calendar tglJatuhTempo = Calendar.getInstance();
		tglJatuhTempo.set(dateSKP.getYear(), dateSKP.getMonth(), dateSKP.getDay());
		tglJatuhTempo.add(Calendar.MONTH, 1);
		Date tglTempo = new Date(tglJatuhTempo.get(Calendar.YEAR) - 1900, tglJatuhTempo.get(Calendar.MONTH), tglJatuhTempo.get(Calendar.DATE));
		Date tglSetor = new Date(dateMohon.getYear() - 1900, dateMohon.getMonth(), dateMohon.getDay());
		if (tglSetor.compareTo(tglTempo) > 0)
		{
			int lamaBunga = ((tglSetor.getYear() - tglTempo.getYear()) * 12) + tglSetor.getMonth() - tglTempo.getMonth() + (tglSetor.getDate() > tglTempo.getDate() ? 1 : 0);
	            //'retValue = 0.02 * lamaBunga * pajakTerutang
	            if (lamaBunga <= 24) {
	                retValue = 0.02 * lamaBunga * pajak;
	            }
	            else
	                retValue = 0.02 * 24 * pajak;
		}
		else
			retValue = 0;
	
		return retValue;
	}
	
	@SuppressWarnings("deprecation")
	private void hitungPajak()
	{
		dendaTerlambatSKPDKB = hitungDendaTerlambatSKPDKB(pokokPajak);
		Date periksa = new Date(dateSKP.getYear() - 1900, dateSKP.getMonth(), dateSKP.getDay());
		Date batas = new Date(117, 9, 21);
		if(periksa.after(batas)){
			pokokPajak = roundUP(pokokPajak);
			denda = roundUP(denda);
			kenaikan = roundUP(kenaikan);
			dendaTerlambatSKPDKB = roundUP(dendaTerlambatSKPDKB);
		}
		totalPajak =  pokokPajak + denda + kenaikan + dendaTerlambatSKPDKB;
		txtPokokPajak.setMoney(new CurrencyAmount(pokokPajak, Currency.getInstance("ind")));
		txtDenda.setMoney(new CurrencyAmount(denda, Currency.getInstance("ind")));
		txtDendaSPTPD.setMoney(new CurrencyAmount(kenaikan, Currency.getInstance("ind")));
		txtTerlambatSKPDKB.setMoney(new CurrencyAmount(dendaTerlambatSKPDKB, Currency.getInstance("ind")));
		txtTotalPajakTerhutang.setMoney(new CurrencyAmount(totalPajak, Currency.getInstance("ind")));
	}
	
	@SuppressWarnings("deprecation")
	private void loadPermohonan (int id)
	{
		double total = 0;
		Mohon itemMohon = ControllerFactory.getMainController().getCpMohonDAOImpl().getMohon(id);
		idMohon = itemMohon.getIdMohon();
		txtAlasanMohon.setText(itemMohon.getAlasanMohon());
		txtNoMohon.setText(itemMohon.getNoMohon());
		txtNamaPemohon.setText(itemMohon.getNamaPemohon());
		txtAlamatPemohon.setText(itemMohon.getAlamatPemohon());
		txtJabatanPemohon.setText(itemMohon.getJabatanPemohon());
		dateMohon.setDate(itemMohon.getTglMohon().getYear() + 1900, itemMohon.getTglMohon().getMonth(), itemMohon.getTglMohon().getDate());
		if (itemMohon.getJenisMohon().toString().equalsIgnoreCase("2"))
		{
//			listAngsuran = ControllerFactory.getMainController().getCpMohonDetailDAOImpl().getDaftarAngsuran(id);
			listAngsuran = MohonDetailProvider.INSTANCE.getMohonDetail(id);
			createTableColumn();
			for (int i=0;i<listAngsuran.size();i++)
			{
				TableItem item = new TableItem(tbl_Angsuran, SWT.NONE);
				item.setText(0, listAngsuran.get(i).getNoAngsuran().toString());
				SimpleDateFormat simpleDate = new SimpleDateFormat("dd-MM-yyyy");
				item.setText(1, String.valueOf(simpleDate.format((Date)listAngsuran.get(i).getTglAngsuran())));
				item.setText(2, String.valueOf(indFormat.format(listAngsuran.get(i).getAngsuranPokok())));
				item.setText(3, String.valueOf(indFormat.format(listAngsuran.get(i).getBiayaAdministrasi())));
				item.setText(4, String.valueOf(indFormat.format(listAngsuran.get(i).getDendaSkpdkb())));
				item.setText(5, String.valueOf(indFormat.format(listAngsuran.get(i).getAngsuranPokok() + listAngsuran.get(i).getBiayaAdministrasi() + listAngsuran.get(i).getDendaSkpdkb())));				
				total += listAngsuran.get(i).getAngsuranPokok() + listAngsuran.get(i).getBiayaAdministrasi() + listAngsuran.get(i).getDendaSkpdkb();
			}
		}
		txtJumlahAngsuran.setText(String.valueOf(listAngsuran.size()));
		txtTotalDataAngsuran.setMoney(new CurrencyAmount(total, Currency.getInstance("ind")));
	}
	
	@SuppressWarnings("deprecation")
	private void resetInputForm()
	{
		txtAlasanMohon.setText("");
		txtAlamatPemohon.setText("");
		txtNoMohon.setText("");
		txtJabatanPemohon.setText("");
		txtNamaPemohon.setText("");
		dateMohon.setDate(dateNow.getYear() + 1900, dateNow.getMonth(), dateNow.getDate());
		txtJumlahAngsuran.setText("");
		tbl_Angsuran.removeAll();
		listAngsuran.clear();
		MohonDetailProvider.INSTANCE.getMohonDetail().clear();
		
		Control[] children = compo.getChildren();
		for (int i=0;i<children.length;i++)
		{
			if (children[i] instanceof MoneyField)
			{
				MoneyField child = (MoneyField) children[i];
				child.setMoney(new CurrencyAmount(0, Currency.getInstance("ind")));
			}
		}
		txtTotalDataAngsuran.setMoney(new CurrencyAmount(0, Currency.getInstance("ind")));
	}
	
	private void createTableColumn()
	{
		if(tbl_Angsuran.getColumnCount() <= 0){
			String[] listColumn = {"No", "Tanggal", "Pokok", "Bunga", "Denda", "Jumlah"};
			for (int i=listColumn.length - 1;i>=0;i--)
			{
				TableColumn colPajak = new TableColumn(tbl_Angsuran, SWT.NONE,0);
				colPajak.setText(listColumn[i]);
				if (i == 0)
					colPajak.setWidth(30);
				else if (i == 1)
					colPajak.setWidth(100);
				else
					colPajak.setWidth(150);
					
			}
		}		
	}
	
	private UserModul userModul = ControllerFactory.getMainController().getCpUserDAOImpl()
			.getHMUserModul(GlobalVariable.userModul.getIdUser()).get(11); // 2 merupakan id sub modul.
	
	private boolean isSave(){		
		return userModul.getSimpan();
	}
	
	private boolean isDelete(){		
		return userModul.getHapus();
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
	
}