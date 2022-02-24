package com.dispenda.sptpd.views;

import java.lang.reflect.InvocationTargetException;
import java.math.RoundingMode;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.net.URISyntaxException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.awt.Desktop;
import java.net.URI;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
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
import com.dispenda.dao.LogImp;
import com.dispenda.model.PendaftaranWajibPajak;
import com.dispenda.model.Sptpd;
import com.dispenda.model.UserModul;
import com.dispenda.object.GlobalVariable;
import com.dispenda.object.Preferences;
import com.dispenda.report.ReportAction;
import com.dispenda.report.ReportAppContext;
//import com.dispenda.sspd.dialog.CariLokasiDialog;
import com.dispenda.web.bkad.ApiPost;
import com.dispenda.widget.IMoneyChangeListener;
import com.dispenda.widget.MoneyChangeEvent;
import com.dispenda.widget.MoneyField;
import com.ibm.icu.text.NumberFormat;
import com.ibm.icu.util.Currency;
import com.ibm.icu.util.CurrencyAmount;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.widgets.Spinner;

public class VerifikasiSPTPDView extends ViewPart {
	
	public static final String ID = VerifikasiSPTPDView.class.getName();
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
	private Date masaPajakDari;
	private Date masaPajakSampai;
	private DateTime calTglSPTPD;
	private DateTime calMasaPajakAwal;
	private DateTime calMasaPajakAkhir;
	private Timestamp dateNow;
	private List<Sptpd> listSptpd = new ArrayList<Sptpd>();
	private TableItem item;
	private Color fontColor = new Color(Display.getCurrent(),Preferences.FONT_COLOR);
	private Clipboard clipboard = new Clipboard(Display.getCurrent());
	PendaftaranWajibPajak wp;
	Sptpd spt;
	DecimalFormat Df = new DecimalFormat(); 

	private Composite composite;
	private Table tbl_SPT;
	private Locale ind = new Locale("id", "ID");
    private NumberFormat indFormat = NumberFormat.getCurrencyInstance(ind);
	private ScrolledComposite scrolledComposite;
	private TableColumn tableColumn_3;
	
	public VerifikasiSPTPDView() {
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
		composite.setLayout(new GridLayout(12, false));
		
		tbl_SPT = new Table(composite, SWT.BORDER | SWT.FULL_SELECTION);
		
		tbl_SPT.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		GridData gd_tbl_SPT = new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 21);
		gd_tbl_SPT.heightHint = 521;
		tbl_SPT.setLayoutData(gd_tbl_SPT);
		tbl_SPT.setFont(SWTResourceManager.getFont("Calibri", 11, SWT.NORMAL));
		tbl_SPT.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		tbl_SPT.setLinesVisible(true);
		tbl_SPT.setHeaderVisible(true);
		tbl_SPT.setItemCount(1);
		tbl_SPT.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				tbl_SPT.getSelectionIndex();
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
		tblclmnNewColumn_1.setWidth(100);
		tblclmnNewColumn_1.setText("NPWPD");
		TableColumn tblclmnNewColumn_2 = new TableColumn(tbl_SPT, SWT.NONE);
		tblclmnNewColumn_2.setWidth(150);
		tblclmnNewColumn_2.setText("Masa Pajak");
		TableColumn tblclmnNewColumn_3 = new TableColumn(tbl_SPT, SWT.NONE);
		tblclmnNewColumn_3.setWidth(150);
		tblclmnNewColumn_3.setText("Nama Badan");
		TableColumn tblclmnNewColumn_4 = new TableColumn(tbl_SPT, SWT.NONE);
		tblclmnNewColumn_4.setWidth(200);
		tblclmnNewColumn_4.setText("Alamat");
		
		
		label_1 = new Label(composite, SWT.SEPARATOR | SWT.VERTICAL);
		label_1.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 21));
		
		Label lblNpwpd = new Label(composite, SWT.NONE);
		lblNpwpd.setForeground(fontColor);
		lblNpwpd.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblNpwpd.setText("NPWPD");
		
		txtKodePajak = new Text(composite, SWT.BORDER);
		GridData gd_txtKodePajak = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_txtKodePajak.widthHint = 33;
		txtKodePajak.setLayoutData(gd_txtKodePajak);
		txtKodePajak.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtKodePajak.setEditable(false);
		txtKodePajak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		txtNoUrut = new Text(composite, SWT.BORDER);
		GridData gd_txtNoUrut = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_txtNoUrut.widthHint = 70;
		txtNoUrut.setLayoutData(gd_txtNoUrut);
		txtNoUrut.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		
		txtNoUrut.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		//txtNoUrut.setFocus();
		
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
		
		tblRekapHarian = new Table(composite, SWT.BORDER | SWT.FULL_SELECTION);
		tblRekapHarian.setLinesVisible(true);
		tblRekapHarian.setItemCount(1);
		tblRekapHarian.setHeaderVisible(true);
		tblRekapHarian.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		tblRekapHarian.setFont(SWTResourceManager.getFont("Calibri", 11, SWT.NORMAL));
		tblRekapHarian.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		GridData gd_tblRekapHarian = new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 21);
		gd_tblRekapHarian.widthHint = 394;
		tblRekapHarian.setLayoutData(gd_tblRekapHarian);
		
		tableColumn_9 = new TableColumn(tblRekapHarian, SWT.NONE);
		tableColumn_9.setWidth(100);
		tableColumn_9.setText("Tanggal");
		
		tableColumn_10 = new TableColumn(tblRekapHarian, SWT.NONE);
		tableColumn_10.setWidth(150);
		tableColumn_10.setText("Omzet");
		
		tableColumn_11 = new TableColumn(tblRekapHarian, SWT.NONE);
		tableColumn_11.setWidth(150);
		tableColumn_11.setText("Pembetulan Omzet");
		
		Menu menu_2 = new Menu(tblRekapHarian);
		tblRekapHarian.setMenu(menu_2);
		
		MenuItem menuItem_1 = new MenuItem(menu_2, SWT.NONE);
		menuItem_1.setText("Copy");
		
		label = new Label(composite, SWT.SEPARATOR);
		label.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 21));
		
		tblRiwayat = new Table(composite, SWT.BORDER | SWT.FULL_SELECTION);
		tblRiwayat.setVisible(false);
		tblRiwayat.setLinesVisible(true);
		tblRiwayat.setItemCount(1);
		tblRiwayat.setHeaderVisible(true);
		tblRiwayat.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		tblRiwayat.setFont(SWTResourceManager.getFont("Calibri", 11, SWT.NORMAL));
		tblRiwayat.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		GridData gd_tblRiwayat = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 21);
		gd_tblRiwayat.heightHint = 518;
		gd_tblRiwayat.widthHint = 500;
		tblRiwayat.setLayoutData(gd_tblRiwayat);
		
		tableColumn = new TableColumn(tblRiwayat, SWT.NONE);
		tableColumn.setWidth(140);
		tableColumn.setText("No SPTPD");
		
		tableColumn_3 = new TableColumn(tblRiwayat, SWT.NONE);
		tableColumn_3.setWidth(140);
		tableColumn_3.setText("Masa Pajak");
		
		tableColumn_1 = new TableColumn(tblRiwayat, SWT.NONE);
		tableColumn_1.setWidth(70);
		tableColumn_1.setText("Status");
		
		tableColumn_2 = new TableColumn(tblRiwayat, SWT.NONE);
		tableColumn_2.setWidth(300);
		tableColumn_2.setText("Keterangan");
		
		tableColumn_4 = new TableColumn(tblRiwayat, SWT.NONE);
		tableColumn_4.setWidth(140);
		tableColumn_4.setText("NPWPD");
		
		tableColumn_5 = new TableColumn(tblRiwayat, SWT.NONE);
		tableColumn_5.setWidth(140);
		tableColumn_5.setText("Nama Badan");
		
		tableColumn_6 = new TableColumn(tblRiwayat, SWT.NONE);
		tableColumn_6.setWidth(140);
		tableColumn_6.setText("Alamat Badan");
		
		tableColumn_7 = new TableColumn(tblRiwayat, SWT.NONE);
		tableColumn_7.setWidth(140);
		tableColumn_7.setText("Dasar Pengenaan");
		
		tableColumn_8 = new TableColumn(tblRiwayat, SWT.NONE);
		tableColumn_8.setWidth(140);
		tableColumn_8.setText("Pajak Terutang");
		
		menu_1 = new Menu(tblRiwayat);
		tblRiwayat.setMenu(menu_1);
		
		menuItem = new MenuItem(menu_1, SWT.NONE);
		menuItem.setText("Copy");
		new Label(composite, SWT.NONE);
		
		lblRiwayatPenerimaan = new Label(composite, SWT.NONE);
		lblRiwayatPenerimaan.setVisible(false);
		lblRiwayatPenerimaan.setForeground(fontColor);
		lblRiwayatPenerimaan.setText("Riwayat Penerimaan / Penolakan");
		lblRiwayatPenerimaan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
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
		
		group_1 = new Group(composite, SWT.NONE);
		group_1.setVisible(false);
		GridData gd_group_1 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 6);
		gd_group_1.heightHint = 119;
		gd_group_1.widthHint = 376;
		group_1.setLayoutData(gd_group_1);
		group_1.setLayout(new GridLayout(4, false));
		
		btnTanggal = new Button(group_1, SWT.RADIO);
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
		
		btnBulan = new Button(group_1, SWT.RADIO);
		btnBulan.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		btnBulan.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				calSKP.setVisible(false);
				calSampai.setVisible(false);
				monthSKP.setVisible(true);
				yearSKP.setVisible(false);
				setMasaPajak();
			}
		});
		btnBulan.setText("Bulan");
		btnBulan.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BORDER));
		btnBulan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		btnTahun = new Button(group_1, SWT.RADIO);
		btnTahun.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				calSKP.setVisible(false);
				calSampai.setVisible(false);
				monthSKP.setVisible(false);
				yearSKP.setVisible(true);
				setMasaPajak();
			}
		});
		btnTahun.setText("Tahun");
		btnTahun.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BORDER));
		btnTahun.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		btnPilihan = new Button(group_1, SWT.RADIO);
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
		
		composite_1 = new Composite(group_1, SWT.NONE);
		composite_1.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 3, 1));
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
		yearSKP.setVisible(false);
		yearSKP.setTextLimit(9999);
		yearSKP.setMaximum(dateNow.getYear() + 1900);
		yearSKP.setMinimum(2006);
		yearSKP.setSelection(dateNow.getYear() + 1900);
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
		
		calSampai = new DateTime(group_1, SWT.BORDER | SWT.DROP_DOWN);
		calSampai.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setMasaPajak();
			}
		});
		calSampai.setVisible(false);
		calSampai.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BORDER));
		calSampai.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		calSampai.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		new Label(group_1, SWT.NONE);
		new Label(group_1, SWT.NONE);
		new Label(group_1, SWT.NONE);
		new Label(group_1, SWT.NONE);
		
		btnRiwayat = new Button(group_1, SWT.NONE);
		btnRiwayat.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		btnRiwayat.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				tbl_SPT.removeAll();
				tblRiwayat.removeAll();
				tblRekapHarian.removeAll();
				txtNoSPTPD.setText("");
				txtKodePajak.setText("");
				txtNoUrut.setText("");
				txtKodeKecamatan.setText("");
				txtKodeKelurahan.setText("");
				txtNamaBadan.setText("");
				txtNamaPemilik.setText("");
				txtAlamat.setText("");
				txtSubPajak.setText("");
				txtAssesment.setText("");
				txtTarif.setText("");
				txtLokasi.setText("");
				txtDasarPengenaan.setMoney(null);
				calMasaPajakAwal.setDate(Calendar.YEAR, Calendar.MONTH, 1);
				calMasaPajakAkhir.setDate(Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH);
				calTglSPTPD.setDate(Calendar.YEAR, Calendar.MONTH, Calendar.DATE);
				txtPajakTerutang.setMoney(null);
				String [] status = {"Menunggu","Diterima","Ditolak"};
				listSptpd = ControllerFactory.getMainController().getCpSptpdDAOImpl().getSptpdTempList(masaPajakDari,masaPajakSampai);
				System.out.println("List size: "+listSptpd.size());
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
						item = new TableItem(tblRiwayat, SWT.NULL);
				        item.setText(0, listSptpd.get(i).getNoSPTPD());
				        item.setText(1, masaPajak);
				        item.setText(2, status[Integer.parseInt(listSptpd.get(i).getStatus())]);
				        item.setText(3, listSptpd.get(i).getKeterangan());
				        item.setText(4, listSptpd.get(i).getNPWPD());
				        item.setText(5, listSptpd.get(i).getNamaBadan());
				        item.setText(6, listSptpd.get(i).getAlamat());
				        item.setText(7, String.valueOf(listSptpd.get(i).getDasarPengenaan().longValue()));
				        item.setText(8, String.valueOf(listSptpd.get(i).getPajakterutang().longValue()));
					}
				}
				else{
					MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Tidak ada data pada rentang waktu tersebut.");
				}
			}
		});
		btnRiwayat.setText("Lihat");
		btnRiwayat.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		btnCetak = new Button(group_1, SWT.NONE);
		btnCetak.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
					ReportAppContext.nameObject = "DaftarRiwayatTT";
					ReportAppContext.classLoader = VerifikasiSPTPDView.class.getClassLoader();
					if(!ReportAppContext.object.isEmpty())
						ReportAppContext.object.clear();
					for(int j = 0;j<tblRiwayat.getItems().length;j++){
						List<String> values = new ArrayList<String>();
						values.add(String.valueOf(j+1));
						values.add(tblRiwayat.getItem(j).getText(0)); //1 SPTPD
						values.add(tblRiwayat.getItem(j).getText(1)); //2 Masa Pajak
						values.add(tblRiwayat.getItem(j).getText(2)); //3 Status
						values.add(tblRiwayat.getItem(j).getText(3)); //4 Keterangan
						values.add(tblRiwayat.getItem(j).getText(4)); //5 NPWPD
						values.add(tblRiwayat.getItem(j).getText(5)); //6 Nama Badan
						values.add(tblRiwayat.getItem(j).getText(6)); //7 Alamat Badan
						values.add(tblRiwayat.getItem(j).getText(7)); //8 Dasar Pengenaan
						values.add(tblRiwayat.getItem(j).getText(8)); //9 Pajak Terutang
						ReportAppContext.object.put(Integer.toString(j), values);
						//System.out.println(values);
					}
					ReportAction.start("DaftarRiwayatTT.rptdesign");
			}
		});
		btnCetak.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		btnCetak.setText("Cetak");
		btnCetak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		new Label(group_1, SWT.NONE);
		new Label(group_1, SWT.NONE);
		
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
		
		txtNoSPTPD.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		new Label(composite, SWT.NONE);
		
		Label lblTanggalSttpd = new Label(composite, SWT.NONE);
		lblTanggalSttpd.setForeground(fontColor);
		lblTanggalSttpd.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblTanggalSttpd.setText("Tanggal SPTPD");
		
		calTglSPTPD = new DateTime(composite, SWT.BORDER | SWT.DROP_DOWN);
		calTglSPTPD.setEnabled(false);
		calTglSPTPD.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		calTglSPTPD.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 4, 1));
		calTglSPTPD.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
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
		calMasaPajakAwal.setEnabled(false);
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
		calMasaPajakAkhir.setEnabled(false);
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
		
		Label lblDasarPengenaan = new Label(composite, SWT.NONE);
		lblDasarPengenaan.setForeground(fontColor);
		lblDasarPengenaan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblDasarPengenaan.setText("Dasar Pengenaan");
		
		txtDasarPengenaan = new MoneyField(composite, SWT.BORDER);
		txtDasarPengenaan.getText().setEditable(false);
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
						double val = txtDasarPengenaan.getMoney().getNumber().doubleValue();
						double pajak = Double.parseDouble(txtTarif.getText()) / 100 * val;
						txtPajakTerutang.setMoney(new CurrencyAmount(Double.parseDouble(df.format(pajak)), Currency.getInstance("ind")));
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
		
		lblPembetulanDasarPengenaan = new Label(composite, SWT.NONE);
		lblPembetulanDasarPengenaan.setVisible(false);
		lblPembetulanDasarPengenaan.setForeground(fontColor);
		lblPembetulanDasarPengenaan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblPembetulanDasarPengenaan.setText("Pembetulan Dasar Pengenaan");
		
		txtPembetulanDasarPengenaan = new MoneyField(composite, SWT.BORDER);
		txtPembetulanDasarPengenaan.setVisible(false);
		GridData gd_txtPembetulanDasarPengenaan = new GridData(SWT.LEFT, SWT.CENTER, false, false, 4, 1);
		gd_txtPembetulanDasarPengenaan.widthHint = 190;
		txtPembetulanDasarPengenaan.setLayoutData(gd_txtPembetulanDasarPengenaan);
		txtPembetulanDasarPengenaan.getText().setEditable(false);
		txtPembetulanDasarPengenaan.getText().setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtPembetulanDasarPengenaan.getText().setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		new Label(txtPembetulanDasarPengenaan, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		
		Label lblTarifPajak = new Label(composite, SWT.NONE);
		lblTarifPajak.setForeground(fontColor);
		lblTarifPajak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblTarifPajak.setText("Tarif Pajak");
		
		txtTarif = new Text(composite, SWT.BORDER);
		txtTarif.setEditable(false);
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
						double val = txtDasarPengenaan.getMoney().getNumber().doubleValue();
						double pajak = Double.parseDouble(txtTar.getText()) / 100 * val;
						txtPajakTerutang.setMoney(new CurrencyAmount(pajak, Currency.getInstance("ind")));
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
		txtPajakTerutang.getText().setEditable(false);
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
		
		lblPembetulanPajakTerhutang = new Label(composite, SWT.NONE);
		lblPembetulanPajakTerhutang.setVisible(false);
		lblPembetulanPajakTerhutang.setForeground(fontColor);
		lblPembetulanPajakTerhutang.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblPembetulanPajakTerhutang.setText("Pembetulan Pajak Terhutang");
		
		txtPembetulanPajakTerutang = new MoneyField(composite, SWT.BORDER);
		txtPembetulanPajakTerutang.setVisible(false);
		GridData gd_txtPembetulanPajakTerutang = new GridData(SWT.LEFT, SWT.CENTER, false, false, 4, 1);
		gd_txtPembetulanPajakTerutang.widthHint = 190;
		txtPembetulanPajakTerutang.setLayoutData(gd_txtPembetulanPajakTerutang);
		txtPembetulanPajakTerutang.getText().setEditable(false);
		txtPembetulanPajakTerutang.setEnabled(false);
		txtPembetulanPajakTerutang.getText().setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtPembetulanPajakTerutang.getText().setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		new Label(txtPembetulanPajakTerutang, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		
		lblLampiranLaporanTransaksi = new Label(composite, SWT.NONE);
		lblLampiranLaporanTransaksi.setVisible(false);
		lblLampiranLaporanTransaksi.setText("Lampiran Laporan Transaksi");
		lblLampiranLaporanTransaksi.setForeground(fontColor);
		lblLampiranLaporanTransaksi.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		btnLihat = new Button(composite, SWT.NONE);
		GridData gd_btnLihat = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnLihat.widthHint = 50;
		btnLihat.setLayoutData(gd_btnLihat);
		btnLihat.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnLihat.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int index = tbl_SPT.getSelectionIndex();
		        try {
		        	String namaFileLampiran = ControllerFactory.getMainController().getCpSptpdDAOImpl().getNamaFileLampiran(listSptpd.get(index).getID());
		        	String alamat="";
		        	if (namaFileLampiran == ""){
		        		MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "File tidak ditemukan");
		        		return;
		        	}
		        	//System.out.println(namaFileLampiran);
		        	alamat="https://simp4d.pemkomedan.go.id/assets/images/berkassptpdself/"+namaFileLampiran;
		        	if(listSptpd.get(index).getIsBNI()){
		        		alamat="https://simp4d.pemkomedan.go.id/assets/images/berkassptpdself/bni/"+namaFileLampiran;
		        	}
		        	Desktop desk = Desktop.getDesktop();
		            try {
						desk.browse(new URI(alamat));
					} catch (URISyntaxException e1) {
						e1.printStackTrace();
					}
		        } catch (MalformedURLException error) {
		            error.printStackTrace();
		        } catch (IOException err) {
		            err.printStackTrace();
		        } catch (SQLException er) {
					er.printStackTrace();
				}
			}
		});
		btnLihat.setVisible(false);
		btnLihat.setText("Lihat");
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		
		lblPerluPemeriksaan = new Label(composite, SWT.NONE);
		lblPerluPemeriksaan.setText("Perlu Pemeriksaan");
		lblPerluPemeriksaan.setForeground(fontColor);
		lblPerluPemeriksaan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblPerluPemeriksaan.setVisible(false);
		
		btnPerluPemeriksaan = new Button(composite, SWT.CHECK);
		btnPerluPemeriksaan.setVisible(false);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
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
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
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
		lblLokasi.setText("Keterangan");
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		
		txtLokasi = new Text(composite, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
		txtLokasi.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtLokasi.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		GridData gd_txtLokasi = new GridData(SWT.FILL, SWT.FILL, false, false, 5, 1);
		gd_txtLokasi.widthHint = 262;
		gd_txtLokasi.heightHint = 77;
		txtLokasi.setLayoutData(gd_txtLokasi);
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
		
		group = new Group(composite, SWT.NONE);
		group.setLayout(new GridLayout(1, false));
		
		btnTampil = new Button(group, SWT.NONE);
		btnTampil.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				tbl_SPT.removeAll();
				tblRiwayat.removeAll();
				tblRekapHarian.removeAll();
				txtNoSPTPD.setText("");
				txtKodePajak.setText("");
				txtNoUrut.setText("");
				txtKodeKecamatan.setText("");
				txtKodeKelurahan.setText("");
				txtNamaBadan.setText("");
				txtNamaPemilik.setText("");
				txtAlamat.setText("");
				txtSubPajak.setText("");
				txtAssesment.setText("");
				txtTarif.setText("");
				txtLokasi.setText("");
				txtDasarPengenaan.setMoney(null);
				calMasaPajakAwal.setDate(Calendar.YEAR, Calendar.MONTH, 1);
				calMasaPajakAkhir.setDate(Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH);
				calTglSPTPD.setDate(Calendar.YEAR, Calendar.MONTH, Calendar.DATE);
				txtPajakTerutang.setMoney(null);
				listSptpd = ControllerFactory.getMainController().getCpSptpdDAOImpl().getSptpdTemp();
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
				        item.setText(0, listSptpd.get(i).getNPWPD());
				        item.setText(1, masaPajak);
				        item.setText(2, listSptpd.get(i).getNamaBadan());
				        item.setText(3, listSptpd.get(i).getAlamat());
					}
				}
				else
				{
					MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Tidak ada data di waiting list");
				}
			}
		});
		GridData gd_btnTampil = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnTampil.widthHint = 116;
		btnTampil.setLayoutData(gd_btnTampil);
		btnTampil.setText("Tampilkan Data");
		btnTampil.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		new Label(composite, SWT.NONE);
		Group groupBtn = new Group(composite, SWT.NONE);
		groupBtn.setLayout(new GridLayout(2, false));
		GridData gd_groupBtn = new GridData(SWT.LEFT, SWT.CENTER, false, false, 6, 1);
		gd_groupBtn.heightHint = 38;
		groupBtn.setLayoutData(gd_groupBtn);
		
		btnTerima = new Button(groupBtn, SWT.NONE);
		btnTerima.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnTerima.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				wp = ControllerFactory.getMainController().getCpWajibPajakDAOImpl().getNPWPD(Integer.parseInt(txtNoUrut.getText()));
				int index = tbl_SPT.getSelectionIndex();
				boolean statusTemp = false;
				try {
					statusTemp = ControllerFactory.getMainController().getCpSptpdDAOImpl().periksaSptpdTemp(listSptpd.get(index).getID());
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				if (statusTemp){
					if (txtLokasi.getText().equals("") || txtLokasi.getText().isEmpty() || txtLokasi.getText().equals(null)){
						MessageDialog.openError(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Silahkan isi keterangan penerimaan di kolom keterangan.");
					}
					else{
						boolean konfirmasi = MessageDialog.openConfirm(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Apakah anda yakin?");
						if (konfirmasi){
							boolean suksesInsert = false;
							try{
								if(listSptpd.get(index).getIsBNI() == true && listSptpd.get(index).getNilaiRevisiBNI() != null){
									suksesInsert = ControllerFactory.getMainController().getCpSptpdDAOImpl().insertDariSptpdTempPembetulan(listSptpd.get(index).getID(),txtPembetulanDasarPengenaan.getMoney().getNumber().doubleValue(),txtPembetulanPajakTerutang.getMoney().getNumber().doubleValue());
								}
								else{
									suksesInsert = ControllerFactory.getMainController().getCpSptpdDAOImpl().insertDariSptpdTemp(listSptpd.get(index).getID());
								}
							}
							catch(Exception ex){
								MessageDialog.openError(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "SPTPD gagal diterima)");
							}
							if (suksesInsert){
								int LastID = ControllerFactory.getMainController().getCpSptpdDAOImpl().getLastIdSptpd(txtKodePajak.getText()+txtNoUrut.getText()+txtKodeKecamatan.getText()+txtKodeKelurahan.getText());
								String NoSPTPD = ControllerFactory.getMainController().getCpSptpdDAOImpl().getNoSPTPDExist(LastID);
								if (btnPerluPemeriksaan.getSelection()){
									if (ControllerFactory.getMainController().getCpSptpdDAOImpl().terimaSptpdTempBersyarat(listSptpd.get(index).getID(),NoSPTPD,txtLokasi.getText())){
										StringBuffer sb = new StringBuffer();
										String [] bulan = {"Januari","Februari","Maret","April","Mei","Juni","Juli","Agustus","September","Oktober","November","Desember"};
										sb.append("Terima" +
												" SPTPD:"+NoSPTPD+
												" NPWPD:"+txtKodePajak.getText()+txtNoUrut.getText()+txtKodeKecamatan.getText()+txtKodeKelurahan.getText()+
												" Masa Pajak: "+calMasaPajakAwal.getYear()+" "+bulan[calMasaPajakAwal.getMonth()]+" - "+calMasaPajakAkhir.getYear()+" "+bulan[calMasaPajakAkhir.getMonth()]+
												" Dasar Pengenaan: "+txtDasarPengenaan.getText().getText()+
												" Pajak Terutang: "+txtPajakTerutang.getText().getText());
										new LogImp().setLog(sb.toString(), userModul.getDisplayName(), userModul.getSubModul().getNamaModul());	
										final String noSTS = ControllerFactory.getMainController().getCpSptpdDAOImpl().getNoSTSUntukSPTPDTEMP(NoSPTPD);
										String nilai = indFormat.format(txtPajakTerutang.getMoney().getNumber().doubleValue()).substring(2).replace(".", "").replace(",", ".");
										if(listSptpd.get(index).getIsBNI() == true && listSptpd.get(index).getNilaiRevisiBNI() != null){
											//System.out.println(pajak);
											nilai = indFormat.format(txtPembetulanPajakTerutang.getMoney().getNumber().doubleValue()).substring(2).replace(".", "").replace(",", ".");
											//System.out.println(nilai);
										}
										Date MasaPajakSampai = new Date(calMasaPajakAkhir.getYear() - 1900, calMasaPajakAkhir.getMonth(), calMasaPajakAkhir.getDay());
										Date tglSPT = new Date(calTglSPTPD.getYear() - 1900, calTglSPTPD.getMonth(), calTglSPTPD.getDay());
										Date stsValid = getSTSValid(tglSPT, txtKodePajak.getText());
										//System.out.println("stsvalid: "+stsValid);
										final String[] listString = {txtKodePajak.getText()+txtNoUrut.getText()+txtKodeKecamatan.getText()+txtKodeKelurahan.getText(),txtNamaBadan.getText() + "/" + txtNamaPemilik.getText(),txtAlamat.getText(),
																	nilai.substring(0, nilai.indexOf(".")),"0",NoSPTPD,listSptpd.get(index).getTanggalSPTPD().toString(),
																	wp.getKode_bid_usaha(),MasaPajakSampai.toString(),stsValid.toString()};
										//int xxx = 0;
										//for (String isi: listString){
											//System.out.println("["+xxx+"] "+isi);
											//xxx++;
										//}
										resultApi = false;
										ProgressMonitorDialog progress = new ProgressMonitorDialog(Display.getCurrent().getActiveShell());
										if(listSptpd.get(index).getIsBNI()){
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
												String sts = "";
												if (noSTS.equalsIgnoreCase(""))
													sts = ControllerFactory.getMainController().getCpSptpdDAOImpl().getNoSTSUntukSPTPDTEMP(NoSPTPD);
												else
													sts = noSTS;
												StringBuffer sb2 = new StringBuffer();
												sb2.append("SAVE " +
														"STS:"+sts+
														" SPTPD:"+NoSPTPD+
														" NPWPD:"+txtKodePajak.getText()+txtNoUrut.getText()+txtKodeKecamatan.getText()+txtKodeKelurahan.getText());
												new LogImp().setLog(sb2.toString(), userModul.getDisplayName(), userModul.getSubModul().getNamaModul());
											}else{
												MessageDialog.openError(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Nomor STS gagal diterima");
											}
										}
										tbl_SPT.removeAll();
										txtNoSPTPD.setText("");
										txtKodePajak.setText("");
										txtNoUrut.setText("");
										txtKodeKecamatan.setText("");
										txtKodeKelurahan.setText("");
										txtNamaBadan.setText("");
										txtNamaPemilik.setText("");
										txtAlamat.setText("");
										txtSubPajak.setText("");
										txtAssesment.setText("");
										txtTarif.setText("");
										txtLokasi.setText("");
										txtDasarPengenaan.setMoney(null);
										calMasaPajakAwal.setDate(Calendar.YEAR, Calendar.MONTH, 1);
										calMasaPajakAkhir.setDate(Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH);
										calTglSPTPD.setDate(Calendar.YEAR, Calendar.MONTH, Calendar.DATE);
										txtPajakTerutang.setMoney(null);
										txtPembetulanPajakTerutang.setMoney(null);
										txtPembetulanDasarPengenaan.setMoney(null);
										btnPerluPemeriksaan.setSelection(false);
										tblRekapHarian.removeAll();
										listSptpd = ControllerFactory.getMainController().getCpSptpdDAOImpl().getSptpdTemp();
										if (listSptpd.size() > 0){
											for (int i=0;i<listSptpd.size();i++){
												String masaPajak = "";
												if (listSptpd.get(i).getMasaPajakDari().getMonth() + 1 == listSptpd.get(i).getMasaPajakSampai().getMonth() + 1 &&
													listSptpd.get(i).getMasaPajakDari().getYear() == listSptpd.get(i).getMasaPajakSampai().getYear()){
													masaPajak = formatMonth(listSptpd.get(i).getMasaPajakDari().getMonth() + 1, Locale.getDefault()) + " " + (listSptpd.get(i).getMasaPajakDari().getYear() + 1900);
												}else{
													masaPajak = formatMonth(listSptpd.get(i).getMasaPajakDari().getMonth() + 1, Locale.getDefault()) + " " + (listSptpd.get(i).getMasaPajakDari().getYear() + 1900) +
															" - " + formatMonth(listSptpd.get(i).getMasaPajakSampai().getMonth() + 1, Locale.getDefault()) + " " + (listSptpd.get(i).getMasaPajakSampai().getYear() + 1900);
												}
												item = new TableItem(tbl_SPT, SWT.NULL);
										        item.setText(0, listSptpd.get(i).getNPWPD());
										        item.setText(1, masaPajak);
											}
										}
										MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "SPTPD berhasil diterima");
									}
								}
								else{
									if (ControllerFactory.getMainController().getCpSptpdDAOImpl().terimaSptpdTemp(listSptpd.get(index).getID(),NoSPTPD,txtLokasi.getText())){
										StringBuffer sb = new StringBuffer();
										String [] bulan = {"Januari","Februari","Maret","April","Mei","Juni","Juli","Agustus","September","Oktober","November","Desember"};
										sb.append("Terima" +
												" SPTPD:"+NoSPTPD+
												" NPWPD:"+txtKodePajak.getText()+txtNoUrut.getText()+txtKodeKecamatan.getText()+txtKodeKelurahan.getText()+
												" Masa Pajak: "+calMasaPajakAwal.getYear()+" "+bulan[calMasaPajakAwal.getMonth()]+" - "+calMasaPajakAkhir.getYear()+" "+bulan[calMasaPajakAkhir.getMonth()]+
												" Dasar Pengenaan: "+txtDasarPengenaan.getText().getText()+
												" Pajak Terutang: "+txtPajakTerutang.getText().getText());
										new LogImp().setLog(sb.toString(), userModul.getDisplayName(), userModul.getSubModul().getNamaModul());
										final String noSTS = ControllerFactory.getMainController().getCpSptpdDAOImpl().getNoSTSUntukSPTPDTEMP(NoSPTPD);
										String nilai = indFormat.format(txtPajakTerutang.getMoney().getNumber().doubleValue()).substring(2).replace(".", "").replace(",", ".");
										if(listSptpd.get(index).getIsBNI() == true && listSptpd.get(index).getNilaiRevisiBNI() != null){
											//System.out.println(pajak);
											nilai = indFormat.format(txtPembetulanPajakTerutang.getMoney().getNumber().doubleValue()).substring(2).replace(".", "").replace(",", ".");
											//System.out.println(nilai);
										}
										Date MasaPajakSampai = new Date(calMasaPajakAkhir.getYear() - 1900, calMasaPajakAkhir.getMonth(), calMasaPajakAkhir.getDay());
										Date tglSPT = new Date(calTglSPTPD.getYear() - 1900, calTglSPTPD.getMonth(), calTglSPTPD.getDay());
										Date stsValid = getSTSValid(tglSPT, txtKodePajak.getText());
										final String[] listString = {txtKodePajak.getText()+txtNoUrut.getText()+txtKodeKecamatan.getText()+txtKodeKelurahan.getText(),txtNamaBadan.getText() + "/" + txtNamaPemilik.getText(),txtAlamat.getText(),
																	nilai.substring(0, nilai.indexOf(".")),"0",NoSPTPD,listSptpd.get(index).getTanggalSPTPD().toString(),
																	wp.getKode_bid_usaha(),MasaPajakSampai.toString(),stsValid.toString()};
										resultApi = false;
										ProgressMonitorDialog progress = new ProgressMonitorDialog(Display.getCurrent().getActiveShell());
										if(listSptpd.get(index).getIsBNI()){
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
												String sts = "";
												if (noSTS.equalsIgnoreCase(""))
													sts = ControllerFactory.getMainController().getCpSptpdDAOImpl().getNoSTSUntukSPTPDTEMP(NoSPTPD);
												else
													sts = noSTS;
												StringBuffer sb2 = new StringBuffer();
												sb2.append("SAVE " +
														"STS:"+sts+
														" SPTPD:"+NoSPTPD+
														" NPWPD:"+txtKodePajak.getText()+txtNoUrut.getText()+txtKodeKecamatan.getText()+txtKodeKelurahan.getText());
												new LogImp().setLog(sb2.toString(), userModul.getDisplayName(), userModul.getSubModul().getNamaModul());
											}else{
												MessageDialog.openError(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Nomor STS gagal diterima");
											}
										}
										tbl_SPT.removeAll();
										txtNoSPTPD.setText("");
										txtKodePajak.setText("");
										txtNoUrut.setText("");
										txtKodeKecamatan.setText("");
										txtKodeKelurahan.setText("");
										txtNamaBadan.setText("");
										txtNamaPemilik.setText("");
										txtAlamat.setText("");
										txtSubPajak.setText("");
										txtAssesment.setText("");
										txtTarif.setText("");
										txtLokasi.setText("");
										txtDasarPengenaan.setMoney(null);
										calMasaPajakAwal.setDate(Calendar.YEAR, Calendar.MONTH, 1);
										calMasaPajakAkhir.setDate(Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH);
										calTglSPTPD.setDate(Calendar.YEAR, Calendar.MONTH, Calendar.DATE);
										txtPajakTerutang.setMoney(null);
										txtPembetulanPajakTerutang.setMoney(null);
										txtPembetulanDasarPengenaan.setMoney(null);
										btnPerluPemeriksaan.setSelection(false);
										tblRekapHarian.removeAll();
										listSptpd = ControllerFactory.getMainController().getCpSptpdDAOImpl().getSptpdTemp();
										if (listSptpd.size() > 0){
											for (int i=0;i<listSptpd.size();i++){
												String masaPajak = "";
												if (listSptpd.get(i).getMasaPajakDari().getMonth() + 1 == listSptpd.get(i).getMasaPajakSampai().getMonth() + 1 &&
													listSptpd.get(i).getMasaPajakDari().getYear() == listSptpd.get(i).getMasaPajakSampai().getYear()){
													masaPajak = formatMonth(listSptpd.get(i).getMasaPajakDari().getMonth() + 1, Locale.getDefault()) + " " + (listSptpd.get(i).getMasaPajakDari().getYear() + 1900);
												}else{
													masaPajak = formatMonth(listSptpd.get(i).getMasaPajakDari().getMonth() + 1, Locale.getDefault()) + " " + (listSptpd.get(i).getMasaPajakDari().getYear() + 1900) +
															" - " + formatMonth(listSptpd.get(i).getMasaPajakSampai().getMonth() + 1, Locale.getDefault()) + " " + (listSptpd.get(i).getMasaPajakSampai().getYear() + 1900);
												}
												item = new TableItem(tbl_SPT, SWT.NULL);
										        item.setText(0, listSptpd.get(i).getNPWPD());
										        item.setText(1, masaPajak);
											}
										}
										MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "SPTPD berhasil diterima");
									}
								}	
							}
							else{
								MessageDialog.openError(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "SPTPD gagal diterima");
							}
						}
						else{
		
						}
					}
				}
				else{
					MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "SPTPD tersebut telah diproses.");	
				}
			}
		});
		GridData gd_btnTerima = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnTerima.widthHint = 150;
		btnTerima.setLayoutData(gd_btnTerima);
		btnTerima.setText("Terima");
		
		btnTolak = new Button(groupBtn, SWT.NONE);
		btnTolak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnTolak.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int index = tbl_SPT.getSelectionIndex();
				boolean statusTemp = false;
				try {
					statusTemp = ControllerFactory.getMainController().getCpSptpdDAOImpl().periksaSptpdTemp(listSptpd.get(index).getID());
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				if (statusTemp){
					if (btnTolak.getText() == "Tolak Pembetulan"){
						wp = ControllerFactory.getMainController().getCpWajibPajakDAOImpl().getNPWPD(Integer.parseInt(txtNoUrut.getText()));
						if (statusTemp){
							if (txtLokasi.getText().equals("") || txtLokasi.getText().isEmpty() || txtLokasi.getText().equals(null)){
								MessageDialog.openError(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Silahkan isi keterangan penerimaan di kolom keterangan.");
							}
							else{
								boolean konfirmasi = MessageDialog.openConfirm(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Apakah anda yakin?");
								if (konfirmasi){
									boolean suksesInsert = false;
									try{
										suksesInsert = ControllerFactory.getMainController().getCpSptpdDAOImpl().insertDariSptpdTemp(listSptpd.get(index).getID());
									}
									catch(Exception ex){
										MessageDialog.openError(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "SPTPD gagal diterima)");
									}
									//boolean suksesUpdate = ControllerFactory.getMainController().getCpSptpdDAOImpl().terimaSptpdTemp(txtNoSPTPD.getText(),txtLokasi.getText());
									if (suksesInsert){
										int LastID = ControllerFactory.getMainController().getCpSptpdDAOImpl().getLastIdSptpd(txtKodePajak.getText()+txtNoUrut.getText()+txtKodeKecamatan.getText()+txtKodeKelurahan.getText());
										String NoSPTPD = ControllerFactory.getMainController().getCpSptpdDAOImpl().getNoSPTPDExist(LastID);
										if (ControllerFactory.getMainController().getCpSptpdDAOImpl().terimaSptpdTemp(listSptpd.get(index).getID(),NoSPTPD,txtLokasi.getText())){
											
											StringBuffer sb = new StringBuffer();
											String [] bulan = {"Januari","Februari","Maret","April","Mei","Juni","Juli","Agustus","September","Oktober","November","Desember"};
											sb.append("Terima" +
													" SPTPD:"+NoSPTPD+
													" NPWPD:"+txtKodePajak.getText()+txtNoUrut.getText()+txtKodeKecamatan.getText()+txtKodeKelurahan.getText()+
													" Masa Pajak: "+calMasaPajakAwal.getYear()+" "+bulan[calMasaPajakAwal.getMonth()]+" - "+calMasaPajakAkhir.getYear()+" "+bulan[calMasaPajakAkhir.getMonth()]+
													" Dasar Pengenaan: "+txtDasarPengenaan.getText().getText()+
													" Pajak Terutang: "+txtPajakTerutang.getText().getText());
											new LogImp().setLog(sb.toString(), userModul.getDisplayName(), userModul.getSubModul().getNamaModul());
											
											final String noSTS = ControllerFactory.getMainController().getCpSptpdDAOImpl().getNoSTSUntukSPTPDTEMP(NoSPTPD);
											String nilai = indFormat.format(txtPajakTerutang.getMoney().getNumber().doubleValue()).substring(2).replace(".", "").replace(",", ".");
											Date MasaPajakSampai = new Date(calMasaPajakAkhir.getYear() - 1900, calMasaPajakAkhir.getMonth(), calMasaPajakAkhir.getDay());;
											Date tglSPT = new Date(calTglSPTPD.getYear() - 1900, calTglSPTPD.getMonth(), calTglSPTPD.getDay());
											Date stsValid = getSTSValid(tglSPT, txtKodePajak.getText());
											final String[] listString = {txtKodePajak.getText()+txtNoUrut.getText()+txtKodeKecamatan.getText()+txtKodeKelurahan.getText(),txtNamaBadan.getText() + "/" + txtNamaPemilik.getText(),txtAlamat.getText(),
																		nilai.substring(0, nilai.indexOf(".")),"0",NoSPTPD,listSptpd.get(index).getTanggalSPTPD().toString(),
																		wp.getKode_bid_usaha(),MasaPajakSampai.toString(),stsValid.toString()};
											//for (int i=0;i<listString.length;i++){
											//	System.out.println(listString[i]);
											//}
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
											//resultApi = true;
											if (resultApi){
												String sts = "";
												if (noSTS.equalsIgnoreCase(""))
													sts = ControllerFactory.getMainController().getCpSptpdDAOImpl().getNoSTSUntukSPTPDTEMP(NoSPTPD);
												else
													sts = noSTS;
												StringBuffer sb2 = new StringBuffer();
												sb2.append("SAVE " +
														"STS:"+sts+
														" SPTPD:"+NoSPTPD+
														" NPWPD:"+txtKodePajak.getText()+txtNoUrut.getText()+txtKodeKecamatan.getText()+txtKodeKelurahan.getText());
												new LogImp().setLog(sb2.toString(), userModul.getDisplayName(), userModul.getSubModul().getNamaModul());
											}else{
												MessageDialog.openError(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Nomor STS gagal diterima");
											}
											
											tbl_SPT.removeAll();
											txtNoSPTPD.setText("");
											txtKodePajak.setText("");
											txtNoUrut.setText("");
											txtKodeKecamatan.setText("");
											txtKodeKelurahan.setText("");
											txtNamaBadan.setText("");
											txtNamaPemilik.setText("");
											txtAlamat.setText("");
											txtSubPajak.setText("");
											txtAssesment.setText("");
											txtTarif.setText("");
											txtLokasi.setText("");
											txtDasarPengenaan.setMoney(null);
											calMasaPajakAwal.setDate(Calendar.YEAR, Calendar.MONTH, 1);
											calMasaPajakAkhir.setDate(Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH);
											calTglSPTPD.setDate(Calendar.YEAR, Calendar.MONTH, Calendar.DATE);
											txtPajakTerutang.setMoney(null);
											tblRekapHarian.removeAll();
											txtPembetulanPajakTerutang.setMoney(null);
											txtPembetulanDasarPengenaan.setMoney(null);
											btnPerluPemeriksaan.setSelection(false);
											listSptpd = ControllerFactory.getMainController().getCpSptpdDAOImpl().getSptpdTemp();
											if (listSptpd.size() > 0){
												for (int i=0;i<listSptpd.size();i++){
													String masaPajak = "";
													if (listSptpd.get(i).getMasaPajakDari().getMonth() + 1 == listSptpd.get(i).getMasaPajakSampai().getMonth() + 1 &&
														listSptpd.get(i).getMasaPajakDari().getYear() == listSptpd.get(i).getMasaPajakSampai().getYear()){
														masaPajak = formatMonth(listSptpd.get(i).getMasaPajakDari().getMonth() + 1, Locale.getDefault()) + " " + (listSptpd.get(i).getMasaPajakDari().getYear() + 1900);
													}else{
														masaPajak = formatMonth(listSptpd.get(i).getMasaPajakDari().getMonth() + 1, Locale.getDefault()) + " " + (listSptpd.get(i).getMasaPajakDari().getYear() + 1900) +
																" - " + formatMonth(listSptpd.get(i).getMasaPajakSampai().getMonth() + 1, Locale.getDefault()) + " " + (listSptpd.get(i).getMasaPajakSampai().getYear() + 1900);
													}
													item = new TableItem(tbl_SPT, SWT.NULL);
											        item.setText(0, listSptpd.get(i).getNPWPD());
											        item.setText(1, masaPajak);
												}
											}
											MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Pembetulan SPTPD berhasil ditolak, dan berhasil diterima SPTPD yang asli");
										}	
									}
									else{
										MessageDialog.openError(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", " Pembetulan SPTPD gagal ditolak");
									}
								}
								else{
				
								}
							}
						}
						else{
							MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "SPTPD tersebut telah diproses.");	
						}
					}
					else{
						if (txtLokasi.getText().equals("") || txtLokasi.getText().isEmpty() || txtLokasi.getText().equals(null)){
							MessageDialog.openError(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Silahkan isi alasan penolakan di kolom keterangan.");
						}
						else{
							boolean konfirmasi = MessageDialog.openConfirm(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Apakah anda yakin?");
							if (konfirmasi){
								boolean sukses = ControllerFactory.getMainController().getCpSptpdDAOImpl().tolakSptpdTemp(listSptpd.get(index).getID(),txtLokasi.getText());
								if(sukses){
									StringBuffer sb = new StringBuffer();
									String [] bulan = {"Januari","Februari","Maret","April","Mei","Juni","Juli","Agustus","September","Oktober","November","Desember"};
									sb.append("Tolak" +
											" NPWPD:"+txtKodePajak.getText()+txtNoUrut.getText()+txtKodeKecamatan.getText()+txtKodeKelurahan.getText()+
											" Masa Pajak: "+calMasaPajakAwal.getYear()+" "+bulan[calMasaPajakAwal.getMonth()]+" - "+calMasaPajakAkhir.getYear()+" "+bulan[calMasaPajakAkhir.getMonth()]+
											" Dasar Pengenaan: "+txtDasarPengenaan.getText().getText()+
											" Pajak Terutang: "+txtPajakTerutang.getText().getText());
									new LogImp().setLog(sb.toString(), userModul.getDisplayName(), userModul.getSubModul().getNamaModul());
									tbl_SPT.removeAll();
									txtNoSPTPD.setText("");
									txtKodePajak.setText("");
									txtNoUrut.setText("");
									txtKodeKecamatan.setText("");
									txtKodeKelurahan.setText("");
									txtNamaBadan.setText("");
									txtNamaPemilik.setText("");
									txtAlamat.setText("");
									txtSubPajak.setText("");
									txtAssesment.setText("");
									txtTarif.setText("");
									txtLokasi.setText("");
									txtDasarPengenaan.setMoney(null);
									calMasaPajakAwal.setDate(Calendar.YEAR, Calendar.MONTH, 1);
									calMasaPajakAkhir.setDate(Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH);
									calTglSPTPD.setDate(Calendar.YEAR, Calendar.MONTH, Calendar.DATE);
									txtPajakTerutang.setMoney(null);
									tblRekapHarian.removeAll();
									txtPembetulanPajakTerutang.setMoney(null);
									txtPembetulanDasarPengenaan.setMoney(null);
									btnPerluPemeriksaan.setSelection(false);
									listSptpd = ControllerFactory.getMainController().getCpSptpdDAOImpl().getSptpdTemp();
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
									        item.setText(0, listSptpd.get(i).getNPWPD());
									        item.setText(1, masaPajak);
										}
									}
									MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "SPTPD berhasil ditolak");
								}
								else{
									MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "SPTPD gagal ditolak");
								}
							}
							else{
								
							}
						}
					}
					
				}
				else{
					MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "SPTPD tersebut telah diproses.");	
				}
			}
		});
		GridData gd_btnTolak = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnTolak.widthHint = 150;
		btnTolak.setLayoutData(gd_btnTolak);
		btnTolak.setText("Tolak");
		composite.setTabList(new Control[]{txtNoUrut, txtDasarPengenaan, txtLokasi, groupBtn});
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		
		scrolledComposite.setContent(composite);
		scrolledComposite.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
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
	
	
	
	@Override
	public void setFocus() {
		//txtNoUrut.setFocus();
	}
	
	@SuppressWarnings("deprecation")
	private void selectTable(){
		txtNoSPTPD.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		//String adaRevisi;
		int index = tbl_SPT.getSelectionIndex();
		txtNoSPTPD.setText(listSptpd.get(index).getNoSPTPD());
		//txtNoSPTPD.pack();
		txtKodePajak.setText(listSptpd.get(index).getNPWPD().substring(0,1));
		txtNoUrut.setText(listSptpd.get(index).getNPWPD().substring(1,listSptpd.get(index).getNPWPD().length()-4));
		txtKodeKecamatan.setText(listSptpd.get(index).getNPWPD().substring(listSptpd.get(index).getNPWPD().length()-4,listSptpd.get(index).getNPWPD().length()-2));
		txtKodeKelurahan.setText(listSptpd.get(index).getNPWPD().substring(listSptpd.get(index).getNPWPD().length()-2,listSptpd.get(index).getNPWPD().length()));
		txtNamaBadan.setText(listSptpd.get(index).getNamaBadan());
		txtNamaPemilik.setText(listSptpd.get(index).getNamaPemilik());
		txtAlamat.setText(listSptpd.get(index).getAlamat());
		txtSubPajak.setText(listSptpd.get(index).getBidangUsaha());
		txtAssesment.setText(listSptpd.get(index).getAssesment()); 
		txtTarif.setText(String.valueOf(listSptpd.get(index).getTarifPajak()));
		txtLokasi.setText(listSptpd.get(index).getLokasi());
		listSptpd.get(index).getID();
		if(listSptpd.get(index).getIsBNI()){
			btnPerluPemeriksaan.setEnabled(true);
			btnPerluPemeriksaan.setVisible(true);
			lblPerluPemeriksaan.setVisible(true);
			if (listSptpd.get(index).getNilaiRevisiBNI() == null){
				btnTolak.setEnabled(false);
				lblLampiranLaporanTransaksi.setVisible(false);
				btnLihat.setVisible(false);
				lblPembetulanPajakTerhutang.setVisible(false);
				lblPembetulanDasarPengenaan.setVisible(false);
				txtPembetulanPajakTerutang.setVisible(false);
				txtPembetulanDasarPengenaan.setVisible(false);
				btnTolak.setText("Tolak");
				btnTerima.setText("Terima");
			}
			else{
				btnTolak.setEnabled(true);
				btnLihat.setVisible(true);
				lblLampiranLaporanTransaksi.setVisible(true);
				lblPembetulanPajakTerhutang.setVisible(true);
				lblPembetulanDasarPengenaan.setVisible(true);
				txtPembetulanPajakTerutang.setVisible(true);
				txtPembetulanDasarPengenaan.setVisible(true);
				btnTolak.setText("Tolak Pembetulan");
				btnTerima.setText("Terima Pembetulan");
			}
		}
		else{
			lblLampiranLaporanTransaksi.setVisible(false);
			btnLihat.setVisible(false);
			btnPerluPemeriksaan.setEnabled(false);
			btnPerluPemeriksaan.setVisible(false);
			lblPerluPemeriksaan.setVisible(false);
			lblPembetulanPajakTerhutang.setVisible(false);
			lblPembetulanDasarPengenaan.setVisible(false);
			txtPembetulanPajakTerutang.setVisible(false);
			txtPembetulanDasarPengenaan.setVisible(false);
			btnTolak.setEnabled(true);
			btnTolak.setText("Tolak");
			btnTerima.setText("Terima");
		}
		//System.out.println(listSptpd.get(index).getIsBNI());
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
		
		String rekapHarian = listSptpd.get(index).getRekapHarian();
		rekapHarian = rekapHarian.replace("ARRAY[","").replace("]","");
		ArrayList<String> rkpHarian = new ArrayList<String>(Arrays.asList(rekapHarian.split(",")));
		tblRekapHarian.removeAll();
		if (rkpHarian.size() > 0)
		{
			String masaPajak = "";
			if (listSptpd.get(index).getMasaPajakDari().getMonth() + 1 == listSptpd.get(index).getMasaPajakSampai().getMonth() + 1 &&
				listSptpd.get(index).getMasaPajakDari().getYear() == listSptpd.get(index).getMasaPajakSampai().getYear()){
				masaPajak = formatMonth(listSptpd.get(index).getMasaPajakDari().getMonth() + 1, Locale.getDefault()) + " " + (listSptpd.get(index).getMasaPajakDari().getYear() + 1900);
			}else{
				masaPajak = formatMonth(listSptpd.get(index).getMasaPajakDari().getMonth() + 1, Locale.getDefault()) + " " + (listSptpd.get(index).getMasaPajakDari().getYear() + 1900) +
						" - " + formatMonth(listSptpd.get(index).getMasaPajakSampai().getMonth() + 1, Locale.getDefault()) + " " + (listSptpd.get(index).getMasaPajakSampai().getYear() + 1900);
			}
			
			if (listSptpd.get(index).getNilaiRevisiBNI() == null){
				//System.out.println(adaRevisi);
				for (int i=0;i<rkpHarian.size();i++)
				{
					//System.out.println(i+" "+rkpHarian.get(i));
					item = new TableItem(tblRekapHarian, SWT.NULL);
					item.setText(0, String.valueOf((i+1)+" "+masaPajak));
					item.setText(1, String.valueOf(indFormat.format(Double.valueOf(rkpHarian.get(i)).longValue())));
				}
			}
			else{
				//System.out.println(adaRevisi);
				String NilaiRevisiBNI = listSptpd.get(index).getNilaiRevisiBNI();
				NilaiRevisiBNI = NilaiRevisiBNI.replace("ARRAY[","").replace("]","");
				ArrayList<String> NLIRevisiBNI = new ArrayList<String>(Arrays.asList(NilaiRevisiBNI.split(",")));
				double dasarPengenaanRevisi = 0;
				for (int i=0;i<rkpHarian.size();i++)
				{
					//System.out.println(i+" "+rkpHarian.get(i));
					item = new TableItem(tblRekapHarian, SWT.NULL);
					item.setText(0, String.valueOf((i+1)+" "+masaPajak));
					item.setText(1, String.valueOf(indFormat.format(Double.valueOf(rkpHarian.get(i)).longValue())));
					item.setText(2, String.valueOf(indFormat.format(Double.valueOf(NLIRevisiBNI.get(i)).longValue())));
					dasarPengenaanRevisi = dasarPengenaanRevisi + Double.parseDouble(NLIRevisiBNI.get(i));
				}
				txtPembetulanDasarPengenaan.setMoney(new CurrencyAmount(dasarPengenaanRevisi, Currency.getInstance(ind)));
				DecimalFormat df = new DecimalFormat("#.##");
				df.setMaximumFractionDigits(0);
				df.setRoundingMode(RoundingMode.UP);
				double val = txtPembetulanDasarPengenaan.getMoney().getNumber().doubleValue();
				double pajak = Double.parseDouble(txtTarif.getText()) / 100 * val;
				txtPembetulanPajakTerutang.setMoney(new CurrencyAmount(Double.parseDouble(df.format(pajak)), Currency.getInstance("ind")));
			}
		}
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
	
	private UserModul userModul = ControllerFactory.getMainController().getCpUserDAOImpl()
			.getHMUserModul(GlobalVariable.userModul.getIdUser()).get(44); // 2 merupakan id sub modul.
	private Button ChkSkpdTerbit;
	private Label lblTanggalTerbit;
	private DateTime calTerbit;
	private Label label_1;
	private Group group;
	private Button btnTampil;
	private Label label;
	private Table tblRiwayat;
	private TableColumn tableColumn;
	private TableColumn tableColumn_4;
	private TableColumn tableColumn_5;
	private TableColumn tableColumn_6;
	private TableColumn tableColumn_7;
	private TableColumn tableColumn_8;
	private TableColumn tableColumn_1;
	private TableColumn tableColumn_2;
	private Menu menu_1;
	private MenuItem menuItem;
	private Group group_1;
	private Button btnTanggal;
	private Button btnBulan;
	private Button btnTahun;
	private Button btnPilihan;
	private Composite composite_1;
	private DateTime calSKP;
	private Spinner yearSKP;
	private DateTime monthSKP;
	private DateTime calSampai;
	private Label lblRiwayatPenerimaan;
	private Button btnRiwayat;
	private Button btnCetak;
	private Table tblRekapHarian;
	private TableColumn tableColumn_9;
	private TableColumn tableColumn_10;
	private TableColumn tableColumn_11;
	private Button btnTerima;
	private Button btnTolak;
	private Label lblPerluPemeriksaan;
	private Button btnPerluPemeriksaan;
	private MoneyField txtPembetulanDasarPengenaan;
	private MoneyField txtPembetulanPajakTerutang;
	private Label lblPembetulanDasarPengenaan;
	private Label lblPembetulanPajakTerhutang;
	private Label lblLampiranLaporanTransaksi;
	private Button btnLihat;
	
	private boolean isDelete(){		
		return userModul.getHapus();
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
}
