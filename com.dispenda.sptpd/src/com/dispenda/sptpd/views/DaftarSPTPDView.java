package com.dispenda.sptpd.views;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
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
import com.dispenda.database.DBConnection;
import com.dispenda.database.DBOperation;
import com.dispenda.database.DbClass;
import com.dispenda.model.BidangUsahaProvider;
import com.dispenda.model.PajakProvider;
import com.dispenda.model.UserModul;
import com.dispenda.object.GlobalVariable;
import com.dispenda.object.Preferences;
import com.dispenda.report.ReportAction;
import com.dispenda.report.ReportAppContext;

public class DaftarSPTPDView extends ViewPart {
	public DaftarSPTPDView() {
	}
	
	public static final String ID = DaftarSPTPDView.class.getName();
	private Button btnTanggal;
	private DateTime calSPTPD;
	private Combo cmbPajak;
	private Date masaPajakDari;
	private Date masaPajakSampai;
	private Button btnBulan;
	private Button btnTahun;
	private Table tbl_SPT;
	private Locale ind = new Locale("id", "ID");
	private NumberFormat indFormat = NumberFormat.getCurrencyInstance(ind);
	private Group composite;
	private DateTime monthSPT;
	private Timestamp dateNow;
	private Spinner yearSPT;
	private Button btnBaru;
	private Button btnCetak;
	private Composite compAbsolute;
	private String masaPajak;
	private int idSubPajak = 0;
	private Color fontColor = new Color(Display.getCurrent(),Preferences.FONT_COLOR);

	@SuppressWarnings("deprecation")
	@Override
	public void createPartControl(Composite parent) {
		Locale.setDefault(new Locale("id","ID"));
		dateNow = (java.sql.Timestamp)ControllerFactory.getMainController().getCpWajibPajakDAOImpl().getDateNow();
		
		ScrolledComposite scrolledComposite = new ScrolledComposite(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		
		Composite comp = new Composite(scrolledComposite, SWT.NONE);
		comp.setLayout(new GridLayout(9, false));
		
		Label lblKewajibanPajak = new Label(comp, SWT.NONE);
		lblKewajibanPajak.setForeground(fontColor);
		lblKewajibanPajak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblKewajibanPajak.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblKewajibanPajak.setText("Kewajiban Pajak");
		
		cmbPajak = new Combo(comp, SWT.READ_ONLY);
		cmbPajak.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				idSubPajak = 0;
//				if (cmbPajak.getSelectionIndex() == 3){
//					cmbSubPajak.setVisible(true);
//					lblDash.setVisible(true);
//				}
//				else{
//					cmbSubPajak.setVisible(false);
//					lblDash.setVisible(false);
//				}
				UIController.INSTANCE.loadBidangUsaha(cmbSubPajak, BidangUsahaProvider.INSTANCE.getBidangUsaha((Integer)cmbPajak.getData(Integer.toString(cmbPajak.getSelectionIndex()))).toArray());
			}
		});
		cmbPajak.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		cmbPajak.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		cmbPajak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		GridData gd_combo = new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1);
		gd_combo.widthHint = 222;
		UIController.INSTANCE.loadPajak(cmbPajak, PajakProvider.INSTANCE.getPajak().toArray());
		cmbPajak.setLayoutData(gd_combo);
		
		lblDash = new Label(comp, SWT.NONE);
		lblDash.setText("-");
		lblDash.setVisible(true);
		
		cmbSubPajak = new Combo(comp, SWT.READ_ONLY);
		cmbSubPajak.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				idSubPajak = (Integer)cmbSubPajak.getData(Integer.toString(cmbSubPajak.getSelectionIndex()));
			}
		});
		cmbSubPajak.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		cmbSubPajak.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		cmbSubPajak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		GridData gd_cmbSubPajak = new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1);
		gd_cmbSubPajak.widthHint = 180;
		cmbSubPajak.setLayoutData(gd_cmbSubPajak);
		cmbSubPajak.setVisible(true);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		
		Label lblTanggalSptpd = new Label(comp, SWT.NONE);
		lblTanggalSptpd.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 2));
		lblTanggalSptpd.setForeground(fontColor);
		lblTanggalSptpd.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblTanggalSptpd.setText("Tanggal SPTPD");
		
		composite = new Group(comp, SWT.NONE);
		composite.setLayout(new GridLayout(5, false));
		composite.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 5, 2));
		
		btnTanggal = new Button(composite, SWT.RADIO);
		btnTanggal.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				calSPTPD.setVisible(true);
				calSPTPD.pack(true);
				calSampai.setVisible(false);
				monthSPT.setVisible(false);
				yearSPT.setVisible(false);
				setMasaPajak();
			}
		});
		btnTanggal.setSelection(false);
		btnTanggal.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BORDER));
		btnTanggal.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnTanggal.setText("Tanggal");
		
		
		btnBulan = new Button(composite, SWT.RADIO);
		btnBulan.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				monthSPT.setVisible(true);
				monthSPT.pack(true);
				calSPTPD.setVisible(false);
				calSampai.setVisible(false);
				yearSPT.setVisible(false);
				setMasaPajak();
			}
		});
		btnBulan.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BORDER));
		btnBulan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnBulan.setText("Bulan");
		
		
		btnTahun = new Button(composite, SWT.RADIO);
		btnTahun.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				yearSPT.setVisible(true);
				yearSPT.pack(true);
				calSPTPD.setVisible(false);
				calSampai.setVisible(false);
				monthSPT.setVisible(false);
				setMasaPajak();
			}
		});
		btnTahun.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BORDER));
		btnTahun.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnTahun.setText("Tahun");
		
		btnPilihan = new Button(composite, SWT.RADIO);
		btnPilihan.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				calSPTPD.setVisible(true);
				calSPTPD.pack(true);
				calSampai.setVisible(true);
				monthSPT.setVisible(false);
				yearSPT.setVisible(false);
				setMasaPajak();
			}
		});
		btnPilihan.setText("Pilihan");
		btnPilihan.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BORDER));
		btnPilihan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		new Label(composite, SWT.NONE);
		
		
		compAbsolute = new Composite(composite, SWT.NONE);
		compAbsolute.setLayout(new StackLayout());
		compAbsolute.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 3, 1));
		
		calSPTPD = new DateTime(compAbsolute, SWT.BORDER | SWT.DROP_DOWN);
		calSPTPD.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		calSPTPD.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setMasaPajak();
				
			}
		});
		calSPTPD.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BORDER));
		calSPTPD.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		yearSPT = new Spinner(compAbsolute, SWT.BORDER);
		yearSPT.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		yearSPT.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setMasaPajak();
			}
		});
		yearSPT.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		yearSPT.setValues(dateNow.getYear() + 1900, 2006, dateNow.getYear() + 1900, 0, 1, 10);
		yearSPT.setVisible(false);
		
		monthSPT = new DateTime(compAbsolute, SWT.BORDER | SWT.DROP_DOWN | SWT.SHORT);
		monthSPT.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		monthSPT.setDay(1);
		monthSPT.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setMasaPajak();
			}
		});
		monthSPT.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		monthSPT.setVisible(false);
		
		calSampai = new DateTime(composite, SWT.BORDER | SWT.DROP_DOWN);
		calSampai.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setMasaPajak();
			}
		});
		calSampai.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		calSampai.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BORDER));
		calSampai.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		calSampai.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		calSampai.setVisible(false);
		
		lblUrutBerdasarkan = new Label(comp, SWT.NONE);
		lblUrutBerdasarkan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblUrutBerdasarkan.setText("Urut berdasarkan");
		
		cmbSort = new Combo(comp, SWT.READ_ONLY);
		cmbSort.setItems(new String[] {"No SPTPD", "Bidang Usaha", "Tanggal SPTPD"});
		cmbSort.setVisible(true);
		cmbSort.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		cmbSort.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		cmbSort.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		cmbSort.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		cmbSort.select(0);
		new Label(comp, SWT.NONE);
		
		chkInsidentil = new Button(comp, SWT.CHECK);
		chkInsidentil.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		chkInsidentil.setText("Insidentil");
		chkInsidentil.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		chkSSPD = new Button(comp, SWT.CHECK);
		chkSSPD.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		chkSSPD.setText("Tampilkan SSPD");
		chkSSPD.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		new Label(comp, SWT.NONE);
		
		btnNewButton = new Button(comp, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				DBOperation db = new DBOperation();
				deleteColumns();
				String sql = "select no_pendaftaran, max(npwpd), count(npwpd) from wajib_pajak where NO_PENDAFTARAN like '%2016' group by NO_PENDAFTARAN having count(npwpd) > 1 order by cast(substring(NO_PENDAFTARAN, 0, length(no_pendaftaran)-4) as int);";
				ResultSet result = db.ResultExecutedStatementQuery(sql, DBConnection.INSTANCE.open());
				createColumn();
				try {
					while (result.next()){
						TableItem item = new TableItem(tbl_SPT, SWT.NONE);
						item.setText(1, result.getString("No_Pendaftaran"));
						item.setText(2, result.getString("C2"));
						item.setText(0, result.getString("C3"));
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnNewButton.setText("New Button");
		btnNewButton.setVisible(false);
		
		Button btnLihat = new Button(comp, SWT.NONE);
		btnLihat.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnLihat.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (cmbPajak.getSelectionIndex() >= 0)
				{
					ResultSet result = null;
					deleteColumns();
					try {
						tbl_SPT.removeAll();
						if (idSubPajak > 0)
							result = ControllerFactory.getMainController().getCpSptpdDAOImpl().getDaftarSPTPDSub(idSubPajak, masaPajakDari, masaPajakSampai, chkInsidentil.getSelection(), chkSSPD.getSelection());
						else
							result = ControllerFactory.getMainController().getCpSptpdDAOImpl().getDaftarSPTPD(cmbPajak.getData(Integer.toString(cmbPajak.getSelectionIndex())).toString(), masaPajakDari, masaPajakSampai, cmbSort.getSelectionIndex(), chkInsidentil.getSelection(), chkSSPD.getSelection());
						
						if (chkSSPD.getSelection())
							createColumnSSPD();
						else
							createColumn();
						loadTableSPTPD(result);
					} catch (ParseException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		GridData gd_btnLihat = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnLihat.widthHint = 90;
		btnLihat.setLayoutData(gd_btnLihat);
		btnLihat.setText("Lihat");
		
		btnBaru = new Button(comp, SWT.NONE);
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
		
		btnCetak = new Button(comp, SWT.NONE);
		btnCetak.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(isPrint()){
					ReportAppContext.name = "Variable";
					ReportAppContext.map.put("MasaPajak", masaPajak);
					ReportAppContext.nameObject = "LIST_SPTPD";
					ReportAppContext.classLoader = DaftarSPTPDView.class.getClassLoader();
					if(!ReportAppContext.object.isEmpty())
						ReportAppContext.object.clear();
					for(int j = 0;j<tbl_SPT.getItems().length-1;j++){// untuk masukan row table
						List<String> values = new ArrayList<String>();
						for(int i=0;i<tbl_SPT.getColumnCount();i++){// untuk masukkan data berdasarkan colom table
							if(i==10||i==12||i==13||i==14||i==15||i==16||i==17)// untuk colom yang berhubungan dengan currency
								values.add(tbl_SPT.getItem(j).getText(i).substring(2).replace(".", "").replace(",", "."));
							else if(i>0)
								values.add(tbl_SPT.getItem(j).getText(i));
						}
						ReportAppContext.object.put(Integer.toString(j), values);
					}
					
					if (chkSSPD.getSelection())
						ReportAction.start("DaftarSPTPD2.rptdesign");
					else
						ReportAction.start("DaftarSPTPD.rptdesign");
				}else
					MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Anda tidak memiliki hak untuk mencetak.");
			}
		});
		btnCetak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		GridData gd_btnCetak = new GridData(SWT.LEFT, SWT.CENTER, false, false, 5, 1);
		gd_btnCetak.widthHint = 90;
		btnCetak.setLayoutData(gd_btnCetak);
		btnCetak.setText("Cetak");
		new Label(comp, SWT.NONE);
		
		btnFix = new Button(comp, SWT.NONE);
		btnFix.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				boolean state = true;
				DBOperation db = new DBOperation();
				for (int i=0;i<tbl_SPT.getItemCount();i++){
					String wp = tbl_SPT.getItem(i).getText(2);
					String sql = "update WAJIB_PAJAK set NO_PENDAFTARAN = (select (max(cast(substring(NO_PENDAFTARAN, 0, length(no_pendaftaran)-4) as int)) + 1) + '/2016' from WAJIB_PAJAK where NO_PENDAFTARAN like '%2016') where NPWPD = '" + wp + "';";
					if (!db.ExecuteStatementQuery(sql, DBConnection.INSTANCE.open())){
						state = false;
						MessageDialog.openError(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Error Save No Pendaftaran");
					}
				}
				if (state)
					MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Update No Pendaftaran berhasil");
			}
		});
		GridData gd_btnFix = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnFix.widthHint = 55;
		btnFix.setLayoutData(gd_btnFix);
		btnFix.setText("Fix");
		btnFix.setVisible(false);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		
		tbl_SPT = new Table(comp, SWT.BORDER | SWT.FULL_SELECTION | SWT.VIRTUAL);
		tbl_SPT.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		tbl_SPT.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 9, 1));
		tbl_SPT.setForeground(SWTResourceManager.getColor(0, 0, 0));
		tbl_SPT.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE-1, SWT.NORMAL));
		tbl_SPT.setHeaderVisible(true);
		tbl_SPT.setLinesVisible(true);

		scrolledComposite.setContent(comp);
		scrolledComposite.setMinSize(comp.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}
	
	@SuppressWarnings("deprecation")
	public void setMasaPajak()
	{
		if (btnTanggal.getSelection())
		{
			Calendar instance = Calendar.getInstance();
			instance.set(Calendar.DAY_OF_MONTH, calSPTPD.getDay());
			instance.set(Calendar.MONTH, calSPTPD.getMonth());
			instance.set(Calendar.YEAR, calSPTPD.getYear());
			masaPajakDari = new Date(calSPTPD.getYear() - 1900, calSPTPD.getMonth(), calSPTPD.getDay());
			masaPajakSampai = masaPajakDari;
			masaPajak = masaPajakDari.getDate() + " " + UIController.INSTANCE.formatMonth(masaPajakDari.getMonth() + 1, Locale.getDefault()) + " " + (masaPajakDari.getYear() + 1900);
		}
		if (btnBulan.getSelection())
		{
			Calendar instance = Calendar.getInstance();
			instance.set(Calendar.DAY_OF_MONTH, monthSPT.getDay());
			instance.set(Calendar.MONTH, monthSPT.getMonth());
			instance.set(Calendar.YEAR, monthSPT.getYear());
			masaPajakDari = new Date(monthSPT.getYear() - 1900, monthSPT.getMonth(), 1);
			masaPajakSampai = new Date(monthSPT.getYear() - 1900, monthSPT.getMonth(), instance.getActualMaximum(Calendar.DATE));
			masaPajak = UIController.INSTANCE.formatMonth(monthSPT.getMonth() + 1, Locale.getDefault()) + " " + monthSPT.getYear();
		}
		if (btnTahun.getSelection())
		{
			masaPajakDari = new Date(yearSPT.getSelection() - 1900, 0, 1);
			masaPajakSampai = new Date(yearSPT.getSelection() - 1900, 11, 31);
			masaPajak = Integer.toString(yearSPT.getSelection());
		}
		if (btnPilihan.getSelection())
		{
			Calendar instance = Calendar.getInstance();
			instance.set(Calendar.DAY_OF_MONTH, calSPTPD.getDay());
			instance.set(Calendar.MONTH, calSPTPD.getMonth());
			instance.set(Calendar.YEAR, calSPTPD.getYear());
			masaPajakDari = new Date(calSPTPD.getYear() - 1900, calSPTPD.getMonth(), calSPTPD.getDay());
			masaPajakSampai = new Date(calSampai.getYear() - 1900, calSampai.getMonth(), calSampai.getDay());
			masaPajak = masaPajakDari.getDate() + " " + UIController.INSTANCE.formatMonth(masaPajakDari.getMonth() + 1, Locale.getDefault()) + " " + (masaPajakDari.getYear() + 1900);
		}
	}
	
	private void deleteColumns(){
		for(TableColumn col : tbl_SPT.getColumns()){
			col.dispose();
		}
	}
	
	private void createColumn()
	{
		if(tbl_SPT.getColumnCount() <= 0){
			String[] listColumn = {"No.","NPWPD", "Nama Badan", "Nama Pemilik", "Alamat", "Bidang Usaha", "No SPTPD", 
					"Tanggal SPTPD", "Assesment", "Masa Pajak", "Dasar Pengenaan", "Tarif Pajak", 
					"Pajak Terutang", "Denda Tambahan", "Total"};
			for (int i=listColumn.length - 1;i>=0;i--)
			{
				TableColumn colPajak = new TableColumn(tbl_SPT, SWT.NONE,0);
				colPajak.setText(listColumn[i]);
				if(i == 0)
					colPajak.setWidth(50);
				else
					colPajak.setWidth(120);
			}
		}
	}
	
	private void createColumnSSPD()
	{
		if(tbl_SPT.getColumnCount() <= 0){
			String[] listColumn = {"No.","NPWPD", "Nama Badan", "Nama Pemilik", "Alamat", "Bidang Usaha", "No SPTPD", 
					"Tanggal SPTPD", "Assesment", "Masa Pajak", "Dasar Pengenaan", "Tarif Pajak", 
					"Pajak Terutang", "Denda Tambahan", "Total", "Jumlah Bayar", "Pokok Pajak Sudah Dibayar", "Denda Sudah Dibayar", "Tgl SSPD", "Tgl Cetak", "No SSPD"};
			for (int i=listColumn.length - 1;i>=0;i--)
			{
				TableColumn colPajak = new TableColumn(tbl_SPT, SWT.NONE,0);
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
		double totalBayar = 0;
		double totalPajakTerutang = 0;
		double totalDendaBayar = 0;
		int count = 1;
		try
		{
			while (result.next())
			{
				TableItem item = new TableItem(tbl_SPT, SWT.NONE);
				item.setText(0, count+".");
				item.setText(1, result.getString("NPWPD"));
				item.setText(2, result.getString("Nama_Badan"));
				item.setText(3, result.getString("Nama_Pemilik"));
				item.setText(4, result.getString("Alabad_Jalan"));
				item.setText(5, result.getString("Nama_Bid_Usaha"));
				item.setText(6, result.getString("No_SPTPD"));
				item.setText(7, formatter.format(result.getDate("Tgl_SPTPD")));
				item.setText(8, result.getString("Jenis_Assesment"));
				item.setText(9, UIController.INSTANCE.formatMonth(result.getDate("Masa_Pajak_Dari").getMonth() + 1, ind) + " " + (result.getDate("Masa_Pajak_Dari").getYear() + 1900));
				//item.setText(9, result.getString("Masa_Pajak_Sampai")); 
				item.setText(10, indFormat.format(result.getDouble("Dasar_Pengenaan")));
				item.setText(11, result.getString("tarif_Pajak"));
				item.setText(12, indFormat.format(result.getDouble("Pajak_Terutang")));
				item.setText(13, indFormat.format(result.getDouble("Denda_Tambahan")));
				item.setText(14, indFormat.format(result.getDouble("Pajak_Terutang") + result.getDouble("Denda_Tambahan")));
				if (chkSSPD.getSelection()){
					item.setText(15, indFormat.format(result.getDouble("Jumlah_Bayar")));
					if (result.getDouble("Jumlah_Bayar")!=0.0)
						item.setText(16, indFormat.format(result.getDouble("Pajak_Terutang")));
					else
						item.setText(16, indFormat.format(0.0));
					item.setText(17, indFormat.format(result.getDouble("Denda")));
					try{
						item.setText(18, formatter.format(result.getDate("Tgl_SSPD")));
						item.setText(19, formatter.format(result.getDate("Tgl_Cetak")));
						item.setText(20, result.getString("No_SSPD"));
					}catch(NullPointerException npe){
//						npe.printStackTrace();
					}
				}
				try {
					totalPajak += indFormat.parse(item.getText(12)).doubleValue();
					totalDenda += indFormat.parse(item.getText(13)).doubleValue();
					GrandTotal += indFormat.parse(item.getText(14)).doubleValue();
					if (chkSSPD.getSelection()){
						totalBayar += indFormat.parse(item.getText(15)).doubleValue();
						totalPajakTerutang += indFormat.parse(item.getText(16)).doubleValue();
						totalDendaBayar += indFormat.parse(item.getText(17)).doubleValue();
					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				count++;
			}
			TableItem item = new TableItem(tbl_SPT, SWT.NONE);
			item.setText(1, "Grand Total");
			item.setText(12, indFormat.format(totalPajak));
			item.setText(13, indFormat.format(totalDenda));
			item.setText(14, indFormat.format(GrandTotal));
			if (chkSSPD.getSelection()){
				item.setText(15, indFormat.format(totalBayar));
				item.setText(16, indFormat.format(totalPajakTerutang));
				item.setText(17, indFormat.format(totalDendaBayar));
			}
		} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		}
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}
	
	@SuppressWarnings("deprecation")
	private void resetForm()
	{
		Control[] children = composite.getChildren();
		for (int i=0;i<children.length;i++)
		{
			if (children[i] instanceof Text)
			{
				Text child = (Text) children[i];
				child.setText("");
			}
		}
		cmbPajak.deselectAll();
		cmbSubPajak.removeAll();
		tbl_SPT.removeAll();
		Calendar instance = Calendar.getInstance();
		instance.set(Calendar.DAY_OF_MONTH, dateNow.getDate());
		instance.set(Calendar.MONTH, dateNow.getMonth());
		instance.set(Calendar.YEAR, dateNow.getYear()+1900);
		calSPTPD.setDate(instance.get(Calendar.YEAR), instance.get(Calendar.MONTH), instance.get(Calendar.DATE));
		monthSPT.setDate(instance.get(Calendar.YEAR), instance.get(Calendar.MONTH), 1);
		setMasaPajak();
		//calMasaPajakAkhir.setDate(instance.get(Calendar.YEAR), instance.get(Calendar.MONTH), instance.getActualMaximum(Calendar.DAY_OF_MONTH));
	}
	
	private UserModul userModul = ControllerFactory.getMainController().getCpUserDAOImpl()
			.getHMUserModul(GlobalVariable.userModul.getIdUser()).get(4); // 1 merupakan id sub modul.
	private Label lblDash;
	private Combo cmbSubPajak;
	private Combo cmbSort;
	private Label lblUrutBerdasarkan;
	private Button chkInsidentil;
	private Button chkSSPD;
	private Button btnPilihan;
	private DateTime calSampai;
	private Button btnNewButton;
	private Button btnFix;
	
	private boolean isPrint(){		
		return userModul.getCetak();
	}
}