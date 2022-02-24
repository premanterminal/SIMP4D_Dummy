package com.dispenda.tunggakan.views;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.ScrolledComposite;
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
import com.dispenda.model.PendaftaranWajibPajak;
import com.dispenda.model.Tunggakan;
import com.dispenda.model.UserModul;
import com.dispenda.object.GlobalVariable;
import com.dispenda.object.Preferences;
import com.dispenda.report.ReportAction;
import com.dispenda.report.ReportAppContext;

import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class TunggakanWpView extends ViewPart {
	public TunggakanWpView() {
	}
	
	public static final String ID = TunggakanWpView.class.getName();
	private Text txtKodePajak;
	private Text txtNoUrut;
	private Text txtKodeKecamatan;
	private Text txtKodeKelurahan;
	private Text txtPajakTerutang;
	private Text txtDenda;
	private Text txtTotal;
	private Table tblSPTPD;
	private Text txtPajakTerutang1;
	private Text txtDenda1;
	private Text txtTotal1;
	private Table tblSKPDKB;
	private Color fontColor = new Color(Display.getCurrent(),Preferences.FONT_COLOR);
	private Text txtNamaBadan;
	private Text txtAlamat;
	private Text txtPajak;
	private Text txtSub;
	private PendaftaranWajibPajak wp;
	private String npwpd = null;
	private Timestamp dateNow;
	private Combo cmbTahun;
	private Combo cmbTahunAkhir;
	private Locale ind = new Locale("id", "ID");
	private NumberFormat indFormat = NumberFormat.getCurrencyInstance(ind);
	private UserModul userModul = ControllerFactory.getMainController().getCpUserDAOImpl()
			.getHMUserModul(GlobalVariable.userModul.getIdUser()).get(26);

	@Override
	public void createPartControl(Composite parent) {
		Locale.setDefault(new Locale("id","ID"));
		dateNow = (java.sql.Timestamp)ControllerFactory.getMainController().getCpWajibPajakDAOImpl().getDateNow();
		
		ScrolledComposite scrolledComposite = new ScrolledComposite(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		
		Composite comp = new Composite(scrolledComposite, SWT.NONE);
		comp.setLayout(new GridLayout(4, false));
		
		Label lblNpwpd = new Label(comp, SWT.NONE);
		lblNpwpd.setForeground(fontColor);
		lblNpwpd.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblNpwpd.setText("NPWPD");
		
		Composite compNPWPD = new Composite(comp, SWT.NONE);
		compNPWPD.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1));
		compNPWPD.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BORDER));
		compNPWPD.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		compNPWPD.setLayout(new GridLayout(4, false));
		
		txtKodePajak = new Text(compNPWPD, SWT.BORDER);
		txtKodePajak.setEditable(false);
		GridData gd_txtKodePajak = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_txtKodePajak.widthHint = 15;
		txtKodePajak.setLayoutData(gd_txtKodePajak);
		txtKodePajak.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		txtKodePajak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		txtNoUrut = new Text(compNPWPD, SWT.BORDER);
		txtNoUrut.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.keyCode == SWT.CR || e.keyCode == SWT.KEYPAD_CR){
					Integer npwp = 0;
					try{
						npwp = Integer.parseInt(txtNoUrut.getText().trim());
					}catch(Exception ex){
						MessageDialog.openError(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "NPWPD tidak valid. Silahkan periksa kembali");
						txtNoUrut.forceFocus();
					}
					wp = ControllerFactory.getMainController().getCpWajibPajakDAOImpl().getNPWPD(npwp);
					if (wp != null){
						txtKodePajak.setText(wp.getNPWP().substring(0, 1));
						txtNoUrut.setText(wp.getNPWP().substring(1, 8));
						txtKodeKecamatan.setText(wp.getNPWP().substring(8, 10));
						txtKodeKelurahan.setText(wp.getNPWP().substring(10, 12));
						npwpd = txtKodePajak.getText() + txtNoUrut.getText() + txtKodeKecamatan.getText() + txtKodeKelurahan.getText();
						txtNamaBadan.setText(wp.getNamaBadan());
						txtAlamat.setText(wp.getAlabadJalan());
						txtPajak.setText(wp.getNamaPajak());
						txtSub.setText(wp.getBidangUsaha());
						txtSub.pack();
					}else{
						MessageDialog.openError(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "NPWPD tidak valid. Silahkan periksa kembali");
						txtNoUrut.forceFocus();
					}
				}
			}
		});
		GridData gd_txtNoUrut = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_txtNoUrut.widthHint = 80;
		txtNoUrut.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		txtNoUrut.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtNoUrut.setLayoutData(gd_txtNoUrut);
		
		txtKodeKecamatan = new Text(compNPWPD, SWT.BORDER);
		txtKodeKecamatan.setEditable(false);
		GridData gd_txtKodeKecamatan = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_txtKodeKecamatan.widthHint = 15;
		txtKodeKecamatan.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		txtKodeKecamatan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtKodeKecamatan.setLayoutData(gd_txtKodeKecamatan);
		
		txtKodeKelurahan = new Text(compNPWPD, SWT.BORDER);
		txtKodeKelurahan.setEditable(false);
		GridData gd_txtKodeKelurahan = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_txtKodeKelurahan.widthHint = 15;
		txtKodeKelurahan.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		txtKodeKelurahan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtKodeKelurahan.setLayoutData(gd_txtKodeKelurahan);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		
		Label lblNewLabel = new Label(comp, SWT.NONE);
		lblNewLabel.setText("Nama Badan");
		lblNewLabel.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblNewLabel.setForeground(fontColor);
		
		txtNamaBadan = new Text(comp, SWT.BORDER);
		txtNamaBadan.setEditable(false);
		GridData gd_txtNamaBadan = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_txtNamaBadan.widthHint = 350;
		txtNamaBadan.setLayoutData(gd_txtNamaBadan);
		txtNamaBadan.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtNamaBadan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		Label lblJenisPajak = new Label(comp, SWT.NONE);
		lblJenisPajak.setText("Jenis Pajak");
		lblJenisPajak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblJenisPajak.setForeground(fontColor);
		
		txtPajak = new Text(comp, SWT.BORDER);
		txtPajak.setEditable(false);
		GridData gd_txtPajak = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_txtPajak.widthHint = 150;
		txtPajak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtPajak.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtPajak.setLayoutData(gd_txtPajak);
		
		Label lblNewLabel_1 = new Label(comp, SWT.NONE);
		lblNewLabel_1.setText("Alamat Badan");
		lblNewLabel_1.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblNewLabel_1.setForeground(fontColor);
		
		txtAlamat = new Text(comp, SWT.BORDER);
		txtAlamat.setEditable(false);
		GridData gd_txtAlamat = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_txtAlamat.widthHint = 350;
		txtAlamat.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtAlamat.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtAlamat.setLayoutData(gd_txtAlamat);
		
		Label lblSubPajak = new Label(comp, SWT.NONE);
		lblSubPajak.setText("Sub Pajak");
		lblSubPajak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblSubPajak.setForeground(fontColor);
		
		txtSub = new Text(comp, SWT.BORDER);
		txtSub.setEditable(false);
		GridData gd_txtSub = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
		gd_txtSub.minimumWidth = 150;
		gd_txtSub.widthHint = 200;
		txtSub.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtSub.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtSub.setLayoutData(gd_txtSub);
		
		Label lblTahun = new Label(comp, SWT.NONE);
		lblTahun.setForeground(fontColor);
		lblTahun.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblTahun.setText("Tahun");
		
		
		Composite compTahun = new Composite(comp, SWT.NONE);
		GridData gd_compTahun = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_compTahun.widthHint = 196;
		compTahun.setLayoutData(gd_compTahun);
		compTahun.setLayout(new GridLayout(2, false));
		
		cmbTahun = new Combo(compTahun, SWT.NONE);
		GridData gd_cmbTahun = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_cmbTahun.widthHint = 63;
		cmbTahun.setLayoutData(gd_cmbTahun);
		cmbTahun.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		cmbTahun.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		
		cmbTahunAkhir = new Combo(compTahun, SWT.NONE);
		cmbTahunAkhir.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		cmbTahunAkhir.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		GridData gd_cmbTahunAkhir = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_cmbTahunAkhir.widthHint = 65;
		cmbTahunAkhir.setLayoutData(gd_cmbTahunAkhir);
		loadTahun();
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		
		Composite compButton = new Composite(comp, SWT.NONE);
		compButton.setLayout(new GridLayout(2, false));
		
		Button btnTampil = new Button(compButton, SWT.NONE);
		btnTampil.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ResultSet resultSPT = null;
				List<Tunggakan> listSKPDKB = new ArrayList<Tunggakan>();
				deleteColumns();
				if (npwpd == null){
					MessageDialog.openError(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Silahkan masukkan NPWPD");
					txtNoUrut.forceFocus();
				}else{
					tblSPTPD.removeAll();
					tblSKPDKB.removeAll();
					resultSPT = ControllerFactory.getMainController().getCpLaporanDAOImpl().getTunggakanSPTPDWP(npwpd, cmbTahun.getText(), cmbTahunAkhir.getText());
					listSKPDKB = ControllerFactory.getMainController().getCpLaporanDAOImpl().getTunggakanSKPDKBWP(npwpd, cmbTahun.getText(), cmbTahunAkhir.getText());
					listSKPDKB.addAll(ControllerFactory.getMainController().getCpLaporanDAOImpl().getTunggakanAngsuranWP(npwpd, cmbTahun.getText(), cmbTahunAkhir.getText()));
					Collections.sort(listSKPDKB);
					createColumns();
					loadTableSPTPD(resultSPT);
					loadTableSKPDKB(listSKPDKB);
				}
			}
		});
		GridData gd_btnTampil = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnTampil.widthHint = 90;
		btnTampil.setLayoutData(gd_btnTampil);
		btnTampil.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnTampil.setText("Tampil");
		
		Button btnCetak = new Button(compButton, SWT.NONE);
		btnCetak.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (isPrint()){
					ReportAppContext.obj = new String[]{"TunggakanSPTPD", "TunggakanSKPDKB"};
					ReportAppContext.listObject = new Object[2];
					HashMap<String, List<String>> sptpd = new HashMap<String, List<String>>();
					HashMap<String, List<String>> skpdkb = new HashMap<String, List<String>>();
					ReportAppContext.name = "Variable";
					ReportAppContext.classLoader = TunggakanWpView.class.getClassLoader();
					if(!ReportAppContext.object.isEmpty())
						ReportAppContext.object.clear();
					ReportAppContext.map.put("npwpd", txtKodePajak.getText() + "-" + txtNoUrut.getText() + "-" + txtKodeKecamatan.getText() + "-" + txtKodeKelurahan.getText());
					ReportAppContext.map.put("namaBadan", txtNamaBadan.getText());
					ReportAppContext.map.put("alamat", txtAlamat.getText());
					ReportAppContext.map.put("jenisPajak", txtPajak.getText());
					ReportAppContext.map.put("subPajak", txtSub.getText());
					if (tblSPTPD.getItem(0).getText(1).contains("SPTPD"))
						ReportAppContext.map.put("sptpd", "SPTPD");
					else
						ReportAppContext.map.put("sptpd", "SKPD");
					
					ReportAppContext.nameObject = "TunggakanSPTPD";
					for(int j = 0;j<tblSPTPD.getItems().length - 1;j++){
						List<String> values = new ArrayList<String>();
						for(int i=0;i<tblSPTPD.getColumnCount();i++){
							if (i==6 || i==8 || i==9 || i==10 || i == 11)
								values.add(tblSPTPD.getItem(j).getText(i).substring(2).replace(".", "").replace(",", "."));
							else
								values.add(tblSPTPD.getItem(j).getText(i));
						}
						sptpd.put(Integer.toString(j), values);
					}
					
					ReportAppContext.nameObject = "TunggakanSKPDKB";
					for(int j = 0;j<tblSKPDKB.getItems().length - 1;j++){
						List<String> values = new ArrayList<String>();
						for(int i=0;i<tblSKPDKB.getColumnCount();i++){
							if (i==6 || i==7 || i==8 || i == 9)
								values.add(tblSKPDKB.getItem(j).getText(i).substring(2).replace(".", "").replace(",", "."));
							else
								values.add(tblSKPDKB.getItem(j).getText(i));
						}
						skpdkb.put(Integer.toString(j), values);
					}
					
					ReportAppContext.listObject[0] = sptpd;
					ReportAppContext.listObject[1] = skpdkb;
					ReportAction.start("TunggakanWP.rptdesign");
				}else
					MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Anda tidak memiliki hak untuk mencetak.");
			}
		});
		GridData gd_btnCetak = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnCetak.widthHint = 90;
		btnCetak.setLayoutData(gd_btnCetak);
		btnCetak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnCetak.setText("Cetak");
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		
		Group group = new Group(comp, SWT.NONE);
		group.setLayout(new GridLayout(1, false));
		group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 4, 1));
		
		CTabFolder tabFolder = new CTabFolder(group, SWT.BORDER);
		tabFolder.setSimple(false);
		tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		tabFolder.setSelectionBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		
		CTabItem tbtmSptpd = new CTabItem(tabFolder, SWT.NONE);
		tbtmSptpd.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		tbtmSptpd.setText("SPTPD");
		tabFolder.setSelection(tbtmSptpd);
		
		Composite compSPTPD = new Composite(tabFolder, SWT.NONE);
		tbtmSptpd.setControl(compSPTPD);
		compSPTPD.setLayout(new GridLayout(6, false));
		
		tblSPTPD = new Table(compSPTPD, SWT.BORDER | SWT.FULL_SELECTION | SWT.VIRTUAL);
		tblSPTPD.setBackground(SWTResourceManager.getColor(173, 216, 230));
		tblSPTPD.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 6, 1));
		tblSPTPD.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE-1, SWT.NORMAL));
		tblSPTPD.setHeaderVisible(true);
		tblSPTPD.setLinesVisible(true);
		
		Label lblPajakTerutang = new Label(compSPTPD, SWT.NONE);
		lblPajakTerutang.setForeground(fontColor);
		lblPajakTerutang.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblPajakTerutang.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPajakTerutang.setText("Pajak Terutang :");
		
		txtPajakTerutang = new Text(compSPTPD, SWT.BORDER);
		txtPajakTerutang.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		txtPajakTerutang.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		
		Label lblDenda = new Label(compSPTPD, SWT.NONE);
		lblDenda.setForeground(fontColor);
		lblDenda.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblDenda.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblDenda.setText("+ Denda :");
		
		txtDenda = new Text(compSPTPD, SWT.BORDER);
		txtDenda.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		txtDenda.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		
		Label lblTotal = new Label(compSPTPD, SWT.NONE);
		lblTotal.setForeground(fontColor);
		lblTotal.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblTotal.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblTotal.setText(" = Total :");
		
		txtTotal = new Text(compSPTPD, SWT.BORDER);
		txtTotal.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		txtTotal.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		
		CTabItem tbtmSkpdkb = new CTabItem(tabFolder, SWT.NONE);
		tbtmSkpdkb.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		tbtmSkpdkb.setText("SKPDKB");
		
		Composite compSKPDKB = new Composite(tabFolder, SWT.NONE);
		tbtmSkpdkb.setControl(compSKPDKB);
		compSKPDKB.setLayout(new GridLayout(6, false));
		
		tblSKPDKB = new Table(compSKPDKB, SWT.BORDER | SWT.FULL_SELECTION | SWT.VIRTUAL);
		tblSKPDKB.setBackground(SWTResourceManager.getColor(173, 216, 230));
		tblSKPDKB.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 6, 1));
		tblSKPDKB.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE-1, SWT.NORMAL));
		tblSKPDKB.setHeaderVisible(true);
		tblSKPDKB.setLinesVisible(true);
		
		Label lblPajakTerutang_1 = new Label(compSKPDKB, SWT.NONE);
		lblPajakTerutang_1.setForeground(fontColor);
		lblPajakTerutang_1.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblPajakTerutang_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPajakTerutang_1.setText("Pajak Terutang :");
		
		txtPajakTerutang1 = new Text(compSKPDKB, SWT.BORDER);
		txtPajakTerutang1.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		txtPajakTerutang1.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		
		Label lblDenda_1 = new Label(compSKPDKB, SWT.NONE);
		lblDenda_1.setForeground(fontColor);
		lblDenda_1.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblDenda_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblDenda_1.setText("+ Denda :");
		
		txtDenda1 = new Text(compSKPDKB, SWT.BORDER);
		txtDenda1.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		txtDenda1.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		
		Label lblTotal_1 = new Label(compSKPDKB, SWT.NONE);
		lblTotal_1.setForeground(fontColor);
		lblTotal_1.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblTotal_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblTotal_1.setText(" = Total :");
		
		txtTotal1 = new Text(compSKPDKB, SWT.BORDER);
		txtTotal1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		txtTotal1.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		scrolledComposite.setContent(comp);
		scrolledComposite.setMinSize(comp.computeSize(SWT.DEFAULT, SWT.DEFAULT));

	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}
	
	@SuppressWarnings("deprecation")
	private void loadTahun(){
		int tahunAwal = 2008;
		int tahunAkhir = dateNow.getYear() + 1900;
		for (Integer i=tahunAkhir;i>=tahunAwal;i--){
			cmbTahun.add(i.toString());
			cmbTahunAkhir.add(i.toString());
		}
	}
	
	private void deleteColumns(){
		for(TableColumn col : tblSPTPD.getColumns()){
			col.dispose();
		}
		
		for(TableColumn col : tblSKPDKB.getColumns()){
			col.dispose();
		}
	}
	
	private void createColumns(){
		if(tblSPTPD.getColumnCount() <= 0){
			String[] listColumn = {"No.","No SPTPD", "Tanggal SPTPD", "Assesment", "Masa Pajak", "Jatuh Tempo", "Dasar Pengenaan", "Tarif Pajak", 
					"Pajak Terutang", "Denda Tambahan", "Denda SSPD", "Total"};
			for (int i=listColumn.length - 1;i>=0;i--)
			{
				TableColumn colPajak = new TableColumn(tblSPTPD, SWT.NONE,0);
				colPajak.setText(listColumn[i]);
				if(i == 0)
					colPajak.setWidth(50);
				else
					colPajak.setWidth(120);
			}
		}
		
		if (tblSKPDKB.getColumnCount() <= 0){
			String[] listColumn = {"No.", "No SKP","Tanggal SKP", "Cicilan", "Masa Pajak", "Jatuh Tempo", "Pokok Pajak", "Denda", "Denda SSPD", "Total"};
			for (int i=listColumn.length - 1;i>=0;i--)
			{
				TableColumn colPajak = new TableColumn(tblSKPDKB, SWT.NONE,0);
				colPajak.setText(listColumn[i]);
				if(i == 0)
					colPajak.setWidth(50);
				else
					colPajak.setWidth(120);
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	private void loadTableSPTPD(final ResultSet result)
	{
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		double GrandTotal = 0;
		double totalPajak = 0;
		double totalDenda = 0;
		double totalDendaSSPD = 0;
		int count = 1;
		Calendar cal = Calendar.getInstance();
		Date tglTempo;
		try
		{
			while (result.next())
			{
				TableItem item = new TableItem(tblSPTPD, SWT.NONE);
				tglTempo = result.getDate("Masa_Pajak_Sampai");
				
				cal.set(Calendar.DAY_OF_MONTH, tglTempo.getDate());
				cal.set(Calendar.MONTH, tglTempo.getMonth());
				cal.set(Calendar.YEAR, tglTempo.getYear());
				cal.add(Calendar.MONTH, 1);
				Date jatuhTempo = new Date(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.getActualMaximum(Calendar.DAY_OF_MONTH));
				
				Date jthTempo = new Date(jatuhTempo.getYear(), jatuhTempo.getMonth(), jatuhTempo.getDate());
				Date tglAwal = new Date(120,03,1);
				Date tglAkhir = new Date(120,06,31);
				//System.out.println(tglAwal);
				if (jthTempo.after(tglAwal) && jthTempo.before(tglAkhir)){
					jatuhTempo.setYear(120);
					jatuhTempo.setMonth(6);
					jatuhTempo.setDate(31);
				}
				
				Double dendaSspd = UIController.INSTANCE.hitungDendaSSPD(result.getDouble("Pajak_Terutang"), jatuhTempo, "SPTPD", txtKodePajak.getText());
				item.setText(0, count+".");
				item.setText(1, result.getString("No_SPTPD"));
				item.setText(2, formatter.format(result.getDate("Tgl_SPTPD")));
				item.setText(3, result.getString("Assesment"));
				item.setText(4, UIController.INSTANCE.formatMonth(result.getDate("Masa_Pajak_Dari").getMonth() + 1, ind) + " " + (result.getDate("Masa_Pajak_Dari").getYear() + 1900));
				item.setText(5, formatter.format(jatuhTempo));
				item.setText(6, indFormat.format(result.getDouble("Dasar_Pengenaan")));
				if (result.getString("tarif_Pajak")==null){
					item.setText(7, "-");
					dendaSspd = 0.0;
				}
				else{
					item.setText(7, result.getString("tarif_Pajak"));
				}
				item.setText(8, indFormat.format(result.getDouble("Pajak_Terutang")));
				item.setText(9, indFormat.format(result.getDouble("Denda_Tambahan")));
				item.setText(10, indFormat.format(dendaSspd));
				item.setText(11, indFormat.format(result.getDouble("Pajak_Terutang") + result.getDouble("Denda_Tambahan") + dendaSspd));
				try {
					totalPajak += indFormat.parse(item.getText(8)).doubleValue();
					totalDenda += indFormat.parse(item.getText(9)).doubleValue();
					totalDendaSSPD += indFormat.parse(item.getText(10)).doubleValue();
					GrandTotal += indFormat.parse(item.getText(11)).doubleValue();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				count++;
			}
			TableItem item = new TableItem(tblSPTPD, SWT.NONE);
			item.setText(1, "Grand Total");
			item.setText(8, indFormat.format(totalPajak));
			item.setText(9, indFormat.format(totalDenda));
			item.setText(10, indFormat.format(totalDendaSSPD));
			item.setText(11, indFormat.format(GrandTotal));
			txtPajakTerutang.setText(indFormat.format(totalPajak));
			txtDenda.setText(indFormat.format(totalDenda));
			txtTotal.setText(indFormat.format(GrandTotal));
		} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		}
	}

	@SuppressWarnings("deprecation")
	private void loadTableSKPDKB(final List<Tunggakan> list){
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		double GrandTotal = 0;
		double totalPajak = 0;
		double totalDenda = 0;
		double totalDendaSspd = 0;
		for (int i=0;i<list.size();i++){
			TableItem item = new TableItem(tblSKPDKB, SWT.NONE);
			item.setText(0, (i+1)+".");
			item.setText(1, list.get(i).getNoSkp());
			item.setText(2, formatter.format(list.get(i).getTglSkp()));
			item.setText(3, list.get(i).getCicilan().toString());
			item.setText(4, UIController.INSTANCE.formatMonth(list.get(i).getMasaPajakDari().getMonth() + 1, ind) + " " + (list.get(i).getMasaPajakDari().getYear() + 1900) + " - " + UIController.INSTANCE.formatMonth(list.get(i).getMasaPajakSampai().getMonth() + 1, ind) + " " + (list.get(i).getMasaPajakSampai().getYear() + 1900));
			item.setText(5, formatter.format(list.get(i).getTglJatuhTempo()));
			item.setText(6, indFormat.format(list.get(i).getPokok() + list.get(i).getKenaikan()));
			item.setText(7, indFormat.format(list.get(i).getDenda()));
			item.setText(8, indFormat.format(list.get(i).getDendaSspd()));
			item.setText(9, indFormat.format(list.get(i).getPokok() + list.get(i).getKenaikan() + list.get(i).getDenda() + list.get(i).getDendaSspd()));
			try {
				totalPajak += indFormat.parse(item.getText(6)).doubleValue();
				totalDenda += indFormat.parse(item.getText(7)).doubleValue();
				totalDendaSspd += indFormat.parse(item.getText(8)).doubleValue();
				GrandTotal += indFormat.parse(item.getText(9)).doubleValue();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		TableItem item = new TableItem(tblSKPDKB, SWT.NONE);
		item.setText(1, "Grand Total");
		item.setText(6, indFormat.format(totalPajak));
		item.setText(7, indFormat.format(totalDenda));
		item.setText(8, indFormat.format(totalDendaSspd));
		item.setText(9, indFormat.format(GrandTotal));
		txtPajakTerutang1.setText(indFormat.format(totalPajak));
		txtDenda1.setText(indFormat.format(totalDenda));
		txtTotal1.setText(indFormat.format(GrandTotal));
	}
	
	private boolean isPrint(){		
		return userModul.getCetak();
	}
	
	/*@SuppressWarnings("deprecation")
	private Double hitungDendaSPT(Double pajakTerutang, Date jatuhTempo){
		Double retValue = 0.0;
		Date tglSetor = new Date(dateNow.getYear(), dateNow.getMonth(), dateNow.getDate()); //new Date(datePembayaran.getYear() - 1900, datePembayaran.getMonth(), datePembayaran.getDay());
		Date tglTempo = new Date(jatuhTempo.getYear(), jatuhTempo.getMonth(), jatuhTempo.getDate());
		if (!txtKodePajak.getText().equalsIgnoreCase("7")) {
			if (tglSetor.compareTo(tglTempo) > 0)
			{
				Integer lamaBunga = ((tglSetor.getYear() - tglTempo.getYear()) * 12) + tglSetor.getMonth() - tglTempo.getMonth();
//						 (tglSetor.getDate() > tglTempo.getDate() ? 1 : 0);
				if (lamaBunga <= 24)
					retValue = 0.02 * lamaBunga * pajakTerutang;
				else
					retValue = 0.02 * 24 * pajakTerutang;
			}
			else
				retValue = 0.0;
		}
		else{
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
		
		return retValue;
	}*/
}
