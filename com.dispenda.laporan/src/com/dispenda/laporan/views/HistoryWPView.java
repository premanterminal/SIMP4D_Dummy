package com.dispenda.laporan.views;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
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
import com.dispenda.model.UserModul;
import com.dispenda.object.GlobalVariable;
import com.dispenda.object.Preferences;
import com.dispenda.report.ReportAction;
import com.dispenda.report.ReportAppContext;

public class HistoryWPView extends ViewPart {
	public HistoryWPView() {
	}

	public static final String ID = HistoryWPView.class.getName();
	private Table tblSPTPD;
	private Text txtKodePajak;
	private Text txtNoUrut;
	private Text txtKodeKecamatan;
	private Text txtKodeKelurahan;
	private Color fontColor = new Color(Display.getCurrent(),Preferences.FONT_COLOR);
	private Text txtNamaBadan;
	private Text txtAlamat;
	private Text txtPemilik;
	private Text txtAlamatPemilik;
	private Text txtPajak;
	private Text txtSub;
	private Text txtDaftar;
	private Text txtStatus;
	private PendaftaranWajibPajak wp;
	private Table tblSKPDKB;
	private Table tblSKPDN;
	private Table tblSKPDKBT;
	private Table tblAngsuran;
	private String npwpd;
	private Locale ind = new Locale("id", "ID");
	private NumberFormat indFormat = NumberFormat.getCurrencyInstance(ind);
	private UserModul userModul = ControllerFactory.getMainController().getCpUserDAOImpl()
			.getHMUserModul(GlobalVariable.userModul.getIdUser()).get(20);
	@Override
	public void createPartControl(Composite arg0) {
		
		Composite composite = new Composite(arg0, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));
		
		Group grpDataLaporanSPTPD = new Group(composite, SWT.NONE);
		grpDataLaporanSPTPD.setLayout(new GridLayout(5, false));
		grpDataLaporanSPTPD.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		grpDataLaporanSPTPD.addPaintListener(new PaintListener() {
			@Override
			public void paintControl(PaintEvent pe) {
				pe.gc.setForeground(fontColor);
				pe.gc.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.BOLD));
				//pe.gc.drawText("Data Laporan SPTPD", 5, 0, false);
			}
		});
		
		Label lblNpwp = new Label(grpDataLaporanSPTPD, SWT.NONE);
		lblNpwp.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblNpwp.setForeground(fontColor);
		lblNpwp.setText("NPWPD");
		
		Composite compNoWp = new Composite(grpDataLaporanSPTPD, SWT.NONE);
		compNoWp.setLayout(new GridLayout(4, false));
		compNoWp.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, 2, 1));
		
		txtKodePajak = new Text(compNoWp, SWT.BORDER);
		txtKodePajak.setEditable(false);
		txtKodePajak.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		txtKodePajak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtKodePajak.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		GridData gd_txtKodePajak = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_txtKodePajak.widthHint = 15;
		txtKodePajak.setLayoutData(gd_txtKodePajak);
		
		txtNoUrut = new Text(compNoWp, SWT.BORDER);
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
						SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
						txtKodePajak.setText(wp.getNPWP().substring(0, 1));
						txtNoUrut.setText(wp.getNPWP().substring(1, 8));
						txtKodeKecamatan.setText(wp.getNPWP().substring(8, 10));
						txtKodeKelurahan.setText(wp.getNPWP().substring(10, 12));
						npwpd = txtKodePajak.getText() + txtNoUrut.getText() + txtKodeKecamatan.getText() + txtKodeKelurahan.getText();
						txtNamaBadan.setText(wp.getNamaBadan());
						txtAlamat.setText(wp.getAlabadJalan());
						txtPemilik.setText(wp.getNamaPemilik());
						txtAlamatPemilik.setText(wp.getAlatingJalan());
						txtPajak.setText(wp.getNamaPajak());
						txtSub.setText(wp.getBidangUsaha());
						txtDaftar.setText(sdf.format(wp.getTanggalDaftar()));
						txtStatus.setText(wp.getStatus());
					}else{
						MessageDialog.openError(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "NPWPD tidak valid. Silahkan periksa kembali");
						txtNoUrut.forceFocus();
					}
				}
			}
		});
		txtNoUrut.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		txtNoUrut.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtNoUrut.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		GridData gd_txtNoUrut = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_txtNoUrut.widthHint = 80;
		txtNoUrut.setLayoutData(gd_txtNoUrut);
		
		txtKodeKecamatan = new Text(compNoWp, SWT.BORDER);
		txtKodeKecamatan.setEditable(false);
		txtKodeKecamatan.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		txtKodeKecamatan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtKodeKecamatan.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		GridData gd_txtKodeKecamatan = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_txtKodeKecamatan.widthHint = 15;
		txtKodeKecamatan.setLayoutData(gd_txtKodeKecamatan);
		
		txtKodeKelurahan = new Text(compNoWp, SWT.BORDER);
		txtKodeKelurahan.setEditable(false);
		txtKodeKelurahan.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		txtKodeKelurahan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtKodeKelurahan.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		GridData gd_txtKodeKelurahan = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_txtKodeKelurahan.widthHint = 15;
		txtKodeKelurahan.setLayoutData(gd_txtKodeKelurahan);
		new Label(grpDataLaporanSPTPD, SWT.NONE);
		new Label(grpDataLaporanSPTPD, SWT.NONE);
		
		Label lblNamaBadan = new Label(grpDataLaporanSPTPD, SWT.NONE);
		lblNamaBadan.setText("Nama Badan");
		lblNamaBadan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblNamaBadan.setForeground(fontColor);
		
		txtNamaBadan = new Text(grpDataLaporanSPTPD, SWT.BORDER);
		txtNamaBadan.setEditable(false);
		GridData gd_txtNamaBadan = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
		gd_txtNamaBadan.widthHint = 350;
		txtNamaBadan.setLayoutData(gd_txtNamaBadan);
		txtNamaBadan.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtNamaBadan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		Label lblNewLabel = new Label(grpDataLaporanSPTPD, SWT.NONE);
		lblNewLabel.setText("Jenis Pajak");
		lblNewLabel.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblNewLabel.setForeground(fontColor);
		
		txtPajak = new Text(grpDataLaporanSPTPD, SWT.BORDER);
		txtPajak.setEditable(false);
		GridData gd_txtPajak = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_txtPajak.widthHint = 200;
		txtPajak.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtPajak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtPajak.setLayoutData(gd_txtPajak);
		
		Label lblAlamatBadan = new Label(grpDataLaporanSPTPD, SWT.NONE);
		lblAlamatBadan.setText("Alamat Badan");
		lblAlamatBadan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblAlamatBadan.setForeground(fontColor);
		
		txtAlamat = new Text(grpDataLaporanSPTPD, SWT.BORDER);
		txtAlamat.setEditable(false);
		GridData gd_txtAlamat = new GridData(SWT.LEFT, SWT.TOP, false, false, 2, 1);
		gd_txtAlamat.widthHint = 350;
		txtAlamat.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtAlamat.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtAlamat.setLayoutData(gd_txtAlamat);
		
		Label lblSubPajak = new Label(grpDataLaporanSPTPD, SWT.NONE);
		lblSubPajak.setText("Sub Pajak");
		lblSubPajak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblSubPajak.setForeground(fontColor);
		
		txtSub = new Text(grpDataLaporanSPTPD, SWT.BORDER);
		txtSub.setEditable(false);
		GridData gd_txtSub = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_txtSub.widthHint = 350;
		txtSub.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtSub.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtSub.setLayoutData(gd_txtSub);
		
		Label lblNamaPemilik = new Label(grpDataLaporanSPTPD, SWT.NONE);
		lblNamaPemilik.setText("Nama Pemilik");
		lblNamaPemilik.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblNamaPemilik.setForeground(fontColor);
		
		txtPemilik = new Text(grpDataLaporanSPTPD, SWT.BORDER);
		txtPemilik.setEditable(false);
		GridData gd_txtPemilik = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
		gd_txtPemilik.widthHint = 350;
		txtPemilik.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtPemilik.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtPemilik.setLayoutData(gd_txtPemilik);
		
		Label lblTanggalDaftar = new Label(grpDataLaporanSPTPD, SWT.NONE);
		lblTanggalDaftar.setText("Tanggal Daftar");
		lblTanggalDaftar.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblTanggalDaftar.setForeground(fontColor);
		
		txtDaftar = new Text(grpDataLaporanSPTPD, SWT.BORDER);
		txtDaftar.setEditable(false);
		GridData gd_txtDaftar = new GridData(SWT.LEFT, SWT.TOP, true, false, 1, 1);
		gd_txtDaftar.widthHint = 100;
		txtDaftar.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtDaftar.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtDaftar.setLayoutData(gd_txtDaftar);
		
		Label lblAlamatPemilik = new Label(grpDataLaporanSPTPD, SWT.NONE);
		lblAlamatPemilik.setText("Alamat Pemilik");
		lblAlamatPemilik.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblAlamatPemilik.setForeground(fontColor);
		
		txtAlamatPemilik = new Text(grpDataLaporanSPTPD, SWT.BORDER);
		txtAlamatPemilik.setEditable(false);
		GridData gd_txtAlamatPemilik = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
		gd_txtAlamatPemilik.widthHint = 350;
		txtAlamatPemilik.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtAlamatPemilik.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtAlamatPemilik.setLayoutData(gd_txtAlamatPemilik);
		
		Label lblStatusUsaha = new Label(grpDataLaporanSPTPD, SWT.NONE);
		lblStatusUsaha.setText("Status Usaha");
		lblStatusUsaha.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblStatusUsaha.setForeground(fontColor);
		
		txtStatus = new Text(grpDataLaporanSPTPD, SWT.BORDER);
		txtStatus.setEditable(false);
		GridData gd_txtStatus = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_txtStatus.widthHint = 100;
		txtStatus.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtStatus.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtStatus.setLayoutData(gd_txtStatus);
		new Label(grpDataLaporanSPTPD, SWT.NONE);
		
		Button btnTampil = new Button(grpDataLaporanSPTPD, SWT.NONE);
		btnTampil.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				deleteColumns();
				ResultSet resultSPT = ControllerFactory.getMainController().getCpLaporanDAOImpl().getHistorySPTPD(npwpd);
				ResultSet resultSKPDKB = ControllerFactory.getMainController().getCpLaporanDAOImpl().getHistorySKPDKB(npwpd);
				ResultSet resultSKPDKBT = ControllerFactory.getMainController().getCpLaporanDAOImpl().getHistorySKPDKBT(npwpd);
				ResultSet resultSKPDN = ControllerFactory.getMainController().getCpLaporanDAOImpl().getHistorySKPDN(npwpd);
				ResultSet resultAngsuran = ControllerFactory.getMainController().getCpLaporanDAOImpl().getHistoryAngsuran(npwpd);
				createColumns();
				loadTableSPTPD(resultSPT);
				loadTableSKPDKB(resultSKPDKB, "SKPDKB");
				loadTableSKPDKB(resultSKPDKBT, "SKPDKBT");
				loadTableSKPDN(resultSKPDN);
				loadTableAngsuran(resultAngsuran);
			}
		});
		btnTampil.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		GridData gd_btnTampil = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnTampil.widthHint = 80;
		btnTampil.setLayoutData(gd_btnTampil);
		btnTampil.setText("Tampil");
		
		Button btnCetak = new Button(grpDataLaporanSPTPD, SWT.NONE);
		btnCetak.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (isPrint()){
					ReportAppContext.obj = new String[]{"SPTPD", "SKPDKB", "SKPDKBT", "SKPDN", "ANGSURAN"};
					ReportAppContext.listObject = new Object[5];
					HashMap<String, List<String>> sptpd = new HashMap<String, List<String>>();
					HashMap<String, List<String>> skpdkb = new HashMap<String, List<String>>();
					HashMap<String, List<String>> skpdkbt = new HashMap<String, List<String>>();
					HashMap<String, List<String>> skpdn = new HashMap<String, List<String>>();
					HashMap<String, List<String>> angsuran = new HashMap<String, List<String>>();
					ReportAppContext.name = "Variable";
					ReportAppContext.classLoader = HistoryWPView.class.getClassLoader();
					if(!ReportAppContext.object.isEmpty())
						ReportAppContext.object.clear();
					ReportAppContext.map.put("NPWPD", txtKodePajak.getText() + "-" + txtNoUrut.getText() + "-" + txtKodeKecamatan.getText() + "-" + txtKodeKelurahan.getText());
					ReportAppContext.map.put("NamaBadan", txtNamaBadan.getText());
					ReportAppContext.map.put("AlamatBadan", txtAlamat.getText());
					ReportAppContext.map.put("JenisPajak", txtPajak.getText());
					ReportAppContext.map.put("SubPajak", txtSub.getText());
					ReportAppContext.map.put("TglDaftar", txtDaftar.getText());
					ReportAppContext.map.put("NamaPemilik", txtPemilik.getText());
					ReportAppContext.map.put("AlamatPemilik", txtAlamatPemilik.getText());
					ReportAppContext.map.put("Status", txtStatus.getText());
					if (txtKodePajak.getText().equalsIgnoreCase("2") || txtKodePajak.getText().equalsIgnoreCase("7"))
						ReportAppContext.map.put("Assesment", "SKPD");
					else
						ReportAppContext.map.put("Assesment", "SPTPD");
					
					ReportAppContext.nameObject = "SPTPD";
					if(!ReportAppContext.object.isEmpty())
						ReportAppContext.object.clear();
					for(int j = 0;j<tblSPTPD.getItems().length - 1;j++){
						List<String> values = new ArrayList<String>();
						for(int i=0;i<tblSPTPD.getColumnCount();i++){
							values.add(tblSPTPD.getItem(j).getText(i));
						}
						if (tblSPTPD.getItem(j).getText(tblSPTPD.getColumnCount()-1).equalsIgnoreCase(""))
							values.add("-");
						else
							values.add("LUNAS");
						sptpd.put(Integer.toString(j), values);
					}
					if (tblSPTPD.getItemCount() > 1)
						ReportAppContext.map.put("Sptpd", "show");
					else
						ReportAppContext.map.put("Sptpd", "hide");
					
					ReportAppContext.nameObject = "SKPDKB";
					if(!ReportAppContext.object.isEmpty())
						ReportAppContext.object.clear();
					for(int j = 0;j<tblSKPDKB.getItems().length - 1;j++){
						List<String> values = new ArrayList<String>();
						for(int i=0;i<tblSKPDKB.getColumnCount();i++){
							values.add(tblSKPDKB.getItem(j).getText(i));
						}
						try {
							if (tblSKPDKB.getItem(j).getText(tblSKPDKB.getColumnCount()-1).equalsIgnoreCase(""))
								values.add("-");
							else if (tblSKPDKB.getItem(j).getText(tblSKPDKB.getColumnCount()-1).contains(","))
								values.add("Angsuran");
							else if (indFormat.parse(tblSKPDKB.getItem(j).getText(7)).doubleValue() - indFormat.parse(tblSKPDKB.getItem(j).getText(8)).doubleValue() > 2)
								values.add("Angsuran");
							else
								values.add("LUNAS");
						} catch (ParseException e1) {
							// TODO Auto-generated catch block
							values.add("-");
							e1.printStackTrace();
						}
						skpdkb.put(Integer.toString(j), values);
					}
					if (tblSKPDKB.getItemCount() > 1)
						ReportAppContext.map.put("Skpdkb", "show");
					else
						ReportAppContext.map.put("Skpdkb", "hide");
					
					ReportAppContext.nameObject = "SKPDKBT";
					if(!ReportAppContext.object.isEmpty())
						ReportAppContext.object.clear();
					for(int j = 0;j<tblSKPDKBT.getItems().length - 1;j++){
						List<String> values = new ArrayList<String>();
						for(int i=0;i<tblSKPDKBT.getColumnCount();i++){
							values.add(tblSKPDKBT.getItem(j).getText(i));
						}
						try{
							if (tblSKPDKBT.getItem(j).getText(tblSKPDKBT.getColumnCount()-1).equalsIgnoreCase(""))
								values.add("-");
							else if (tblSKPDKBT.getItem(j).getText(tblSKPDKBT.getColumnCount()-1).contains(","))
								values.add("Angsuran");
							else if (indFormat.parse(tblSKPDKB.getItem(j).getText(7)).doubleValue() - indFormat.parse(tblSKPDKB.getItem(j).getText(8)).doubleValue() > 2)
								values.add("Angsuran");
							else
								values.add("LUNAS");
						} catch (ParseException e1) {
							// TODO Auto-generated catch block
							values.add("-");
							e1.printStackTrace();
						}
						skpdkbt.put(Integer.toString(j), values);
					}
					if (tblSKPDKBT.getItemCount() > 1)
						ReportAppContext.map.put("Skpdkbt", "show");
					else
						ReportAppContext.map.put("Skpdkbt", "hide");
					
					ReportAppContext.nameObject = "SKPDN";
					if(!ReportAppContext.object.isEmpty())
						ReportAppContext.object.clear();
					for(int j = 0;j<tblSKPDN.getItems().length - 1;j++){
						List<String> values = new ArrayList<String>();
						for(int i=0;i<tblSKPDN.getColumnCount();i++){
							values.add(tblSKPDN.getItem(j).getText(i));
						}
						skpdn.put(Integer.toString(j), values);
					}
					if (tblSKPDN.getItemCount() > 1)
						ReportAppContext.map.put("Skpdn", "show");
					else
						ReportAppContext.map.put("Skpdn", "hide");
					
					ReportAppContext.nameObject = "ANGSURAN";
					if(!ReportAppContext.object.isEmpty())
						ReportAppContext.object.clear();
					for(int j = 0;j<tblAngsuran.getItems().length - 1;j++){
						List<String> values = new ArrayList<String>();
						for(int i=0;i<tblAngsuran.getColumnCount();i++){
							values.add(tblAngsuran.getItem(j).getText(i));
						}
						if (tblAngsuran.getItem(j).getText(tblAngsuran.getColumnCount()-1).equalsIgnoreCase(""))
							values.add("-");
						else
							values.add("LUNAS");
						angsuran.put(Integer.toString(j), values);
					}
					if (tblAngsuran.getItemCount() > 1)
						ReportAppContext.map.put("Angsuran", "show");
					else
						ReportAppContext.map.put("Angsuran", "hide");
					
					ReportAppContext.listObject[0] = sptpd;
					ReportAppContext.listObject[1] = skpdkb;
					ReportAppContext.listObject[2] = skpdkbt;
					ReportAppContext.listObject[3] = skpdn;
					ReportAppContext.listObject[4] = angsuran;
					ReportAction.start("HistoryWP.rptdesign");
				}else
					MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Anda tidak memiliki hak untuk mencetak.");
			}
		});
		btnCetak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		GridData gd_btnCetak = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnCetak.widthHint = 80;
		btnCetak.setLayoutData(gd_btnCetak);
		btnCetak.setText("Cetak");
		new Label(grpDataLaporanSPTPD, SWT.NONE);
		new Label(grpDataLaporanSPTPD, SWT.NONE);
		
		Group grpHistoryWP = new Group(composite, SWT.NONE);
		grpHistoryWP.setLayout(new GridLayout(1, false));
		grpHistoryWP.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		grpHistoryWP.addPaintListener(new PaintListener() {
			@Override
			public void paintControl(PaintEvent pe) {
				pe.gc.setForeground(fontColor);
				pe.gc.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.BOLD));
				//pe.gc.drawText("SPTPD", 5, 0, false);
			}
		});
		
		CTabFolder tabFolder = new CTabFolder(grpHistoryWP, SWT.NONE);
		tabFolder.setSimple(false);
		tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		CTabItem tbtmSptpd = new CTabItem(tabFolder, SWT.NONE);
		tbtmSptpd.setText("SPTPD");
		tabFolder.setSelection(tbtmSptpd);
		
		tblSPTPD = new Table(tabFolder, SWT.BORDER | SWT.FULL_SELECTION | SWT.VIRTUAL);
		tbtmSptpd.setControl(tblSPTPD);
		tblSPTPD.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		tblSPTPD.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE-1, SWT.NORMAL));
		tblSPTPD.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		tblSPTPD.setHeaderVisible(true);
		tblSPTPD.setLinesVisible(true);
		
		CTabItem tbtmSkpdkb = new CTabItem(tabFolder, SWT.NONE);
		tbtmSkpdkb.setText("SKPDKB");
		
		tblSKPDKB = new Table(tabFolder, SWT.BORDER | SWT.FULL_SELECTION);
		tbtmSkpdkb.setControl(tblSKPDKB);
		tblSKPDKB.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		tblSKPDKB.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE-1, SWT.NORMAL));
		tblSKPDKB.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		tblSKPDKB.setHeaderVisible(true);
		tblSKPDKB.setLinesVisible(true);
		
		CTabItem tbtmSkpdn = new CTabItem(tabFolder, SWT.NONE);
		tbtmSkpdn.setText("SKPDN");
		
		tblSKPDN = new Table(tabFolder, SWT.BORDER | SWT.FULL_SELECTION);
		tbtmSkpdn.setControl(tblSKPDN);
		tblSKPDN.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		tblSKPDN.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE-1, SWT.NORMAL));
		tblSKPDN.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		tblSKPDN.setHeaderVisible(true);
		tblSKPDN.setLinesVisible(true);
		
		CTabItem tbtmSkpdkbt = new CTabItem(tabFolder, SWT.NONE);
		tbtmSkpdkbt.setText("SKPDKBT");
		
		tblSKPDKBT = new Table(tabFolder, SWT.BORDER | SWT.FULL_SELECTION);
		tbtmSkpdkbt.setControl(tblSKPDKBT);
		tblSKPDKBT.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		tblSKPDKBT.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE-1, SWT.NORMAL));
		tblSKPDKBT.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		tblSKPDKBT.setHeaderVisible(true);
		tblSKPDKBT.setLinesVisible(true);
		
		CTabItem tbtmAngsuran = new CTabItem(tabFolder, SWT.NONE);
		tbtmAngsuran.setText("Angsuran");
		
		tblAngsuran = new Table(tabFolder, SWT.BORDER | SWT.FULL_SELECTION);
		tbtmAngsuran.setControl(tblAngsuran);
		tblAngsuran.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		tblAngsuran.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE-1, SWT.NORMAL));
		tblAngsuran.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		tblAngsuran.setHeaderVisible(true);
		tblAngsuran.setLinesVisible(true);
		// TODO Auto-generated method stub

	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}
	
	private void deleteColumns(){
		for(TableColumn col : tblSPTPD.getColumns()){
			col.dispose();
		}
		
		for(TableColumn col : tblSKPDKB.getColumns()){
			col.dispose();
		}
		
		for(TableColumn col : tblSKPDN.getColumns()){
			col.dispose();
		}
		
		for(TableColumn col : tblSKPDKBT.getColumns()){
			col.dispose();
		}
		
		for(TableColumn col : tblAngsuran.getColumns()){
			col.dispose();
		}
		
		tblSPTPD.removeAll();
		tblSKPDKB.removeAll();
		tblSKPDN.removeAll();
		tblSKPDKBT.removeAll();
		tblAngsuran.removeAll();
	}

	private void createColumns()
	{
		if(tblSPTPD.getColumnCount() <= 0){
			String[] listColumn = {"No.", "No SPTPD","Tanggal SPTPD", "Masa Pajak", "Dasar Pengenaan","Tarif Pajak","Pajak Terutang", 
					"Denda Tambahan", "Total", "Jumlah Bayar", "Denda", "Tgl SSPD", "Tgl Cetak", "No SSPD"};
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
			String[] listColumn = {"No.", "No SKP", "Tanggal SKP", "Masa Pajak", "Cicilan", "Pokok Pajak", "Denda SKPDKB", 
					"Total", "Jumlah Bayar", "Denda SSPD", "Tgl SSPD", "Tgl Cetak", "No SSPD"};
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
		
		if (tblSKPDKBT.getColumnCount() <= 0){
			String[] listColumn = {"No.", "No SKP","Tanggal SKP", "Masa Pajak", "Cicilan", "Pokok Pajak", "Denda SKPDKB", 
					"Total", "Jumlah Bayar", "Denda SSPD", "Tgl SSPD", "Tgl Cetak", "No SSPD"};
			for (int i=listColumn.length - 1;i>=0;i--)
			{
				TableColumn colPajak = new TableColumn(tblSKPDKBT, SWT.NONE,0);
				colPajak.setText(listColumn[i]);
				if(i == 0)
					colPajak.setWidth(50);
				else
					colPajak.setWidth(120);
			}
		}
		
		if (tblSKPDN.getColumnCount() <= 0){
			String[] listColumn = {"No.", "No SKP", "Tanggal SKP", "Masa Pajak", "Pokok Pajak", "Denda", "Total"};
			for (int i=listColumn.length - 1;i>=0;i--)
			{
				TableColumn colPajak = new TableColumn(tblSKPDN, SWT.NONE,0);
				colPajak.setText(listColumn[i]);
				if(i == 0)
					colPajak.setWidth(50);
				else
					colPajak.setWidth(120);
			}
		}
		
		if (tblAngsuran.getColumnCount() <= 0){
			String[] listColumn = {"No.", "No SKP","Tanggal SKP", "Masa Pajak", "Cicilan", "Pokok Pajak", "Denda SKPDKB", 
					"Total", "Jumlah Bayar", "Denda SSPD", "Tgl SSPD", "Tgl Cetak", "No SSPD"};
			for (int i=listColumn.length - 1;i>=0;i--)
			{
				TableColumn colPajak = new TableColumn(tblAngsuran, SWT.NONE,0);
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
		int count = 1;
		try
		{
			while (result.next())
			{
				TableItem item = new TableItem(tblSPTPD, SWT.NONE);
				item.setText(0, count+".");
				item.setText(1, result.getString("No_SPTPD"));
				item.setText(2, formatter.format(result.getDate("Tgl_SPTPD")));
				item.setText(3, UIController.INSTANCE.formatMonth(result.getDate("Masa_Pajak_Dari").getMonth() + 1, ind) + " " + (result.getDate("Masa_Pajak_Dari").getYear() + 1900));
				item.setText(4, indFormat.format(result.getDouble("Dasar_Pengenaan")));
				item.setText(5, result.getString("tarif_Pajak"));
				item.setText(6, indFormat.format(result.getDouble("Pajak_Terutang")));
				item.setText(7, indFormat.format(result.getDouble("Denda_Tambahan")));
				item.setText(8, indFormat.format(result.getDouble("Pajak_Terutang") + result.getDouble("Denda_Tambahan")));
				item.setText(9, indFormat.format(result.getDouble("Jumlah_Bayar")));
				item.setText(10, indFormat.format(result.getDouble("Denda")));
				try{
					item.setText(11, formatter.format(result.getDate("Tgl_SSPD")));
					item.setText(12, formatter.format(result.getDate("Tgl_Cetak")));
					item.setText(13, result.getString("No_SSPD"));
				}catch(NullPointerException npe){
//					npe.printStackTrace();
				}
				try {
					totalPajak += indFormat.parse(item.getText(6)).doubleValue();
					totalDenda += indFormat.parse(item.getText(7)).doubleValue();
					GrandTotal += indFormat.parse(item.getText(8)).doubleValue();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				count++;
			}
			TableItem item = new TableItem(tblSPTPD, SWT.NONE);
			item.setText(1, "Grand Total");
			item.setText(6, indFormat.format(totalPajak));
			item.setText(7, indFormat.format(totalDenda));
			item.setText(8, indFormat.format(GrandTotal));
		} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		}
	}
	
	@SuppressWarnings("deprecation")
	private void loadTableSKPDKB(final ResultSet result, String tipe)
	{
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		double GrandTotal = 0;
		double totalPajak = 0;
		double totalDenda = 0;
		int count = 1;
		try
		{
			TableItem item;
			while (result.next())
			{
				if (tipe.equalsIgnoreCase("SKPDKB"))
					item = new TableItem(tblSKPDKB, SWT.NONE);
				else
					item = new TableItem(tblSKPDKBT, SWT.NONE);
				item.setText(0, count+".");
				item.setText(1, result.getString("No_SKP"));
				item.setText(2, formatter.format(result.getDate("Tanggal_SKP")));
				item.setText(3, UIController.INSTANCE.formatMonth(result.getDate("Masa_Pajak_Dari").getMonth() + 1, ind) + " " + (result.getDate("Masa_Pajak_Dari").getYear() + 1900) + " - " + UIController.INSTANCE.formatMonth(result.getDate("Masa_Pajak_Sampai").getMonth() + 1, ind) + " " + (result.getDate("Masa_Pajak_Sampai").getYear() + 1900));
				item.setText(4, result.getString("Cicilan"));
				item.setText(5, indFormat.format(result.getDouble("Pokok")));
				item.setText(6, indFormat.format(result.getDouble("Denda")));
				item.setText(7, indFormat.format(result.getDouble("Pokok") + result.getDouble("Denda")));
				item.setText(8, indFormat.format(result.getDouble("Jumlah_Bayar")));
				item.setText(9, indFormat.format(result.getDouble("DendaSSPD")));
				try{
					item.setText(10, formatter.format(result.getDate("Tgl_SSPD")));
					item.setText(11, formatter.format(result.getDate("Tgl_Cetak")));
					item.setText(12, result.getString("No_SSPD"));
				}catch(NullPointerException npe){
//					npe.printStackTrace();
				}
				try {
					totalPajak += indFormat.parse(item.getText(5)).doubleValue();
					totalDenda += indFormat.parse(item.getText(6)).doubleValue();
					GrandTotal += indFormat.parse(item.getText(7)).doubleValue();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				count++;
			}
			if (tipe.equalsIgnoreCase("SKPDKB"))
				item = new TableItem(tblSKPDKB, SWT.NONE);
			else
				item = new TableItem(tblSKPDKBT, SWT.NONE);
			item.setText(1, "Grand Total");
			item.setText(5, indFormat.format(totalPajak));
			item.setText(6, indFormat.format(totalDenda));
			item.setText(7, indFormat.format(GrandTotal));
		} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		}
	}
	
	@SuppressWarnings("deprecation")
	private void loadTableSKPDN(final ResultSet result)
	{
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		double GrandTotal = 0;
		double totalPajak = 0;
		double totalDenda = 0;
		int count = 1;
		try
		{
			while (result.next())
			{
				TableItem item = new TableItem(tblSKPDN, SWT.NONE);
				item.setText(0, count+".");
				item.setText(1, result.getString("No_SKP"));
				item.setText(2, formatter.format(result.getDate("Tanggal_SKP")));
				item.setText(3, UIController.INSTANCE.formatMonth(result.getDate("Masa_Pajak_Dari").getMonth() + 1, ind) + " " + (result.getDate("Masa_Pajak_Dari").getYear() + 1900) + " - " + UIController.INSTANCE.formatMonth(result.getDate("Masa_Pajak_Sampai").getMonth() + 1, ind) + " " + (result.getDate("Masa_Pajak_Sampai").getYear() + 1900));
				item.setText(4, indFormat.format(result.getDouble("Pokok")));
				item.setText(5, indFormat.format(result.getDouble("Denda")));
				item.setText(6, indFormat.format(result.getDouble("Pokok") + result.getDouble("Denda")));
				try {
					totalPajak += indFormat.parse(item.getText(4)).doubleValue();
					totalDenda += indFormat.parse(item.getText(5)).doubleValue();
					GrandTotal += indFormat.parse(item.getText(6)).doubleValue();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				count++;
			}
			TableItem item = new TableItem(tblSKPDN, SWT.NONE);
			item.setText(1, "Grand Total");
			item.setText(4, indFormat.format(totalPajak));
			item.setText(5, indFormat.format(totalDenda));
			item.setText(6, indFormat.format(GrandTotal));
		} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		}
	}
	
	@SuppressWarnings("deprecation")
	private void loadTableAngsuran(final ResultSet result)
	{
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		double GrandTotal = 0;
		double totalPajak = 0;
		double totalDenda = 0;
		int count = 1;
		try
		{
			while (result.next())
			{
				TableItem item = new TableItem(tblAngsuran, SWT.NONE);
				item.setText(0, count+".");
				item.setText(1, result.getString("No_SKP"));
				item.setText(2, formatter.format(result.getDate("Tanggal_SKP")));
				item.setText(3, UIController.INSTANCE.formatMonth(result.getDate("Masa_Pajak_Dari").getMonth() + 1, ind) + " " + (result.getDate("Masa_Pajak_Dari").getYear() + 1900) + " - " + UIController.INSTANCE.formatMonth(result.getDate("Masa_Pajak_Sampai").getMonth() + 1, ind) + " " + (result.getDate("Masa_Pajak_Sampai").getYear() + 1900));
				item.setText(4, result.getString("Cicilan"));
				item.setText(5, indFormat.format(result.getDouble("Pokok")));
				item.setText(6, indFormat.format(result.getDouble("Denda")));
				item.setText(7, indFormat.format(result.getDouble("Pokok") + result.getDouble("Denda")));
				item.setText(8, indFormat.format(result.getDouble("Jumlah_Bayar")));
				item.setText(9, indFormat.format(result.getDouble("DendaSSPD")));
				try{
					item.setText(10, formatter.format(result.getDate("Tgl_SSPD")));
					item.setText(11, formatter.format(result.getDate("Tgl_Cetak")));
					item.setText(12, result.getString("No_SSPD"));
				}catch(NullPointerException npe){
//					npe.printStackTrace();
				}
				try {
					totalPajak += indFormat.parse(item.getText(5)).doubleValue();
					totalDenda += indFormat.parse(item.getText(6)).doubleValue();
					GrandTotal += indFormat.parse(item.getText(7)).doubleValue();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				count++;
			}
			TableItem item = new TableItem(tblAngsuran, SWT.NONE);
			item.setText(1, "Grand Total");
			item.setText(5, indFormat.format(totalPajak));
			item.setText(6, indFormat.format(totalDenda));
			item.setText(7, indFormat.format(GrandTotal));
		} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		}
	}
	
	private boolean isPrint(){		
		return userModul.getCetak();
	}
}