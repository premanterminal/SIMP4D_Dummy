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
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.SWTResourceManager;

import com.dispenda.controller.ControllerFactory;
import com.dispenda.controller.UIController;
import com.dispenda.model.PajakProvider;
import com.dispenda.model.UserModul;
import com.dispenda.object.GlobalVariable;
import com.dispenda.object.Preferences;
import com.dispenda.report.ReportAction;
import com.dispenda.report.ReportAppContext;

public class LaporanDendaSSPD extends ViewPart {
	public LaporanDendaSSPD() {
	}
	
	public static final String ID = LaporanDendaSSPD.class.getName();
	private Table tbl_SSPD;
	private Timestamp dateNow;
	private Locale ind = new Locale("id", "ID");
	private NumberFormat indFormat = NumberFormat.getCurrencyInstance(ind);
	private DateTime dateSPT;
	private Spinner yearSPT;
	private DateTime monthSPT;
	private Date masaPajakDari;
	private Date masaPajakSampai;
	private String masaPajak;
	private Button btnTanggal;
	private Button btnBulan;
	private Button btnTahun;
	private Combo cmbKewajibanPajak;
	private Color fontColor = new Color(Display.getCurrent(),Preferences.FONT_COLOR);
	private Combo cmbTipeSKP;

	@SuppressWarnings("deprecation")
	@Override
	public void createPartControl(Composite parent) {
		Locale.setDefault(new Locale("id","ID"));
		dateNow = (java.sql.Timestamp)ControllerFactory.getMainController().getCpWajibPajakDAOImpl().getDateNow();
		
		ScrolledComposite scrolledComposite = new ScrolledComposite(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		
		Composite comp = new Composite(scrolledComposite, SWT.NONE);
		comp.setLayout(new GridLayout(3, false));
		
		Label lblKewajibanPajak = new Label(comp, SWT.NONE);
		lblKewajibanPajak.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblKewajibanPajak.setForeground(fontColor);
		lblKewajibanPajak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblKewajibanPajak.setText("Kewajiban Pajak");
		
		cmbKewajibanPajak = new Combo(comp, SWT.READ_ONLY);
		cmbKewajibanPajak.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		cmbKewajibanPajak.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		cmbKewajibanPajak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		UIController.INSTANCE.loadPajak(cmbKewajibanPajak, PajakProvider.INSTANCE.getPajak().toArray());
		new Label(comp, SWT.NONE);
		
		Label lblTipeSkp = new Label(comp, SWT.NONE);
		lblTipeSkp.setForeground(fontColor);
		lblTipeSkp.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblTipeSkp.setText("Tipe SKP");
		
		cmbTipeSKP = new Combo(comp, SWT.READ_ONLY);
		cmbTipeSKP.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		cmbTipeSKP.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		cmbTipeSKP.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		cmbTipeSKP.add("SPTPD");
		cmbTipeSKP.add("SKPD");
		cmbTipeSKP.add("SKPDKB");
		cmbTipeSKP.add("SKPDKBT");
		new Label(comp, SWT.NONE);
		
		Label lblTanggalSspd = new Label(comp, SWT.NONE);
		lblTanggalSspd.setForeground(fontColor);
		lblTanggalSspd.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblTanggalSspd.setText("Tanggal SSPD");
		
		Group compSSPD = new Group(comp, SWT.NONE);
		compSSPD.setLayout(new GridLayout(3, false));
		
		btnTanggal = new Button(compSSPD, SWT.RADIO);
		btnTanggal.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				dateSPT.setVisible(true);
				monthSPT.setVisible(false);
				yearSPT.setVisible(false);
				setMasaPajak();
			}
		});
		btnTanggal.setText("Tanggal");
		btnTanggal.setSelection(false);
		btnTanggal.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		
		btnBulan = new Button(compSSPD, SWT.RADIO);
		btnBulan.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				dateSPT.setVisible(false);
				monthSPT.setVisible(true);
				yearSPT.setVisible(false);
				setMasaPajak();
			}
		});
		btnBulan.setText("Bulan");
		btnBulan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
	
		
		btnTahun = new Button(compSSPD, SWT.RADIO);
		btnTahun.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				dateSPT.setVisible(false);
				monthSPT.setVisible(false);
				yearSPT.setVisible(true);
				setMasaPajak();
			}
		});
		btnTahun.setText("Tahun");
		btnTahun.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		
		Composite composite = new Composite(compSSPD, SWT.NONE);
		composite.setLayout(new StackLayout());
		composite.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 3, 1));
		
		dateSPT = new DateTime(composite, SWT.BORDER | SWT.DROP_DOWN);
		dateSPT.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		dateSPT.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setMasaPajak();
			}
		});
		dateSPT.pack(true);
		dateSPT.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BORDER));
		dateSPT.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		monthSPT = new DateTime(composite, SWT.BORDER | SWT.DROP_DOWN | SWT.SHORT);
		monthSPT.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		monthSPT.setDay(1);
		monthSPT.pack(true);
		monthSPT.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setMasaPajak();
			}
		});
		monthSPT.setVisible(false);
		monthSPT.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		yearSPT = new Spinner(composite, SWT.BORDER);
		yearSPT.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		yearSPT.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setMasaPajak();
			}
		});
		yearSPT.setTextLimit(9999);
		yearSPT.setMaximum(dateNow.getYear() + 1900);
		yearSPT.setMinimum(2006);
		yearSPT.setSelection(dateNow.getYear() + 1900);
		yearSPT.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		yearSPT.setVisible(false);
		yearSPT.pack(true);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		
		Composite compButton = new Composite(comp, SWT.NONE);
		compButton.setLayout(new GridLayout(2, false));
		
		Button btnLihat = new Button(compButton, SWT.NONE);
		btnLihat.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ResultSet result = null;
				if (cmbKewajibanPajak.getSelectionIndex() >= 0){
					if(cmbTipeSKP.getSelectionIndex()>=0)
						if (cmbTipeSKP.getText().equalsIgnoreCase("SPTPD") || cmbTipeSKP.getText().equalsIgnoreCase("SKPD"))
							result = ControllerFactory.getMainController().getCpSspdDAOImpl().getDaftarSSPDDendaSPT(cmbKewajibanPajak.getData(Integer.toString(cmbKewajibanPajak.getSelectionIndex())).toString(), masaPajakDari, masaPajakSampai);
						else
							result = ControllerFactory.getMainController().getCpSspdDAOImpl().getDaftarSSPDDendaSKPDKB(cmbKewajibanPajak.getData(Integer.toString(cmbKewajibanPajak.getSelectionIndex())).toString(), masaPajakDari, masaPajakSampai);
					else
						MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Silahkan pilih tipe skp");
				}else
					MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Silahkan pilih kode pajak");
				
				tbl_SSPD.removeAll();
				createColumn();
				loadTableSSPD(result);
			}
		});
		GridData gd_btnLihat = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnLihat.widthHint = 90;
		btnLihat.setLayoutData(gd_btnLihat);
		btnLihat.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnLihat.setText("Lihat");
		
		Button btnCetak = new Button(compButton, SWT.NONE);
		GridData gd_btnCetak = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnCetak.widthHint = 90;
		btnCetak.setLayoutData(gd_btnCetak);
		btnCetak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnCetak.setText("Cetak");
		btnCetak.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(isPrint()){
					ReportAppContext.name = "Variable";
					ReportAppContext.map.put("MasaPajak", masaPajak);
					ReportAppContext.nameObject = "LIST_SSPD";
					ReportAppContext.classLoader = LaporanDendaSSPD.class.getClassLoader();
					if(!ReportAppContext.object.isEmpty())
						ReportAppContext.object.clear();
					Integer count = 0;
					for(int ti=0;ti<tbl_SSPD.getItems().length - 1; ti++){
						//String[] values = new String[tbl_SPT.getColumnCount()];
						List<String> values = new ArrayList<String>();
						for(int i=0;i<tbl_SSPD.getColumnCount();i++){
							if (i == 9 || i == 10 || i == 11 || i == 12 || i == 15)
								values.add(tbl_SSPD.getItem(ti).getText(i).substring(2).replace(".", "").replace(",", "."));
							else
								values.add(tbl_SSPD.getItem(ti).getText(i));
						}
						ReportAppContext.object.put(count.toString(), values);
						count++;
					}
					ReportAction.start("DaftarDendaSSPD.rptdesign");
				}else
					MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Anda tidak memiliki hak untuk mencetak.");
			}
		});
		new Label(comp, SWT.NONE);
		
		tbl_SSPD = new Table(comp, SWT.BORDER | SWT.FULL_SELECTION | SWT.VIRTUAL);
		tbl_SSPD.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));
		tbl_SSPD.setForeground(SWTResourceManager.getColor(0, 0, 0));
		tbl_SSPD.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		tbl_SSPD.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE-1, SWT.NORMAL));
		tbl_SSPD.setHeaderVisible(true);
		tbl_SSPD.setLinesVisible(true);
		scrolledComposite.setContent(comp);
		scrolledComposite.setMinSize(comp.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		

	}
	
	@SuppressWarnings("deprecation")
	public void setMasaPajak()
	{
		
		if (btnTanggal.getSelection())
		{
			Calendar instance = Calendar.getInstance();
			instance.set(Calendar.DAY_OF_MONTH, dateSPT.getDay());
			instance.set(Calendar.MONTH, dateSPT.getMonth());
			instance.set(Calendar.YEAR, dateSPT.getYear());
			masaPajakDari = new Date(dateSPT.getYear() - 1900, dateSPT.getMonth(), dateSPT.getDay());
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
		if (btnTahun.getSelection()){
			masaPajakDari = new Date(yearSPT.getSelection() - 1900, 0, 1);
			masaPajakSampai = new Date(yearSPT.getSelection() - 1900, 11, 31);
			masaPajak = Integer.toString(yearSPT.getSelection());
		}
	}
	
	private void createColumn()
	{
		if(tbl_SSPD.getColumnCount() <= 0){
			String[] listColumn = {"Tgl SPTPD","No SSPD", "No Reg", "Tanggal SSPD", "NPWPD", "Nama Badan", "Nama Pajak", "Bidang Usaha", "No SKP", "Pokok Pajak", 
					"Denda", "Denda Terlambat Bayar", "Jumlah Bayar", "Bulan SKP", "Cicilan Ke", "Sisa Denda", "Nama Penyetor"};
			int[] widthColumn = {130, 130, 100, 115, 120, 160, 140, 160, 150, 150, 140, 150, 150, 90, 90, 150, 150};
			for (int i=listColumn.length - 1;i>=0;i--)
			{
				TableColumn colPajak = new TableColumn(tbl_SSPD, SWT.NONE,0);
				colPajak.setText(listColumn[i]);
				colPajak.setWidth(widthColumn[i]);
			}
		}
	}
	
	private void loadTableSSPD(final ResultSet result)
	{
		double totalPokokPajak = 0;
		double totalDenda = 0;
		double totalDendaTerlambat = 0;
		double totalJumlahBayar = 0;
		double totalSisa = 0;
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		try
		{
			while (result.next())
			{
				TableItem item = new TableItem(tbl_SSPD, SWT.NONE);
//				if(result.getString("TGL_SPTPD") == null)
					item.setText(0, result.getString("Tanggal_SKP"));
//				else
//					item.setText(0, result.getString("TGL_SPTPD"));
				item.setText(1, result.getString("No_SSPD"));
				String noReg = result.getString("No_Registrasi") == null ? "": result.getString("No_Registrasi");
				item.setText(2, noReg);
				item.setText(3, formatter.format(result.getDate("Tgl_SSPD"))); //.split(" ")[0]);
				item.setText(4, result.getString("NPWPD"));
				item.setText(5, result.getString("Nama_Badan"));
				item.setText(6, result.getString("Nama_Pajak"));
				item.setText(7, result.getString("Bidang_Usaha"));
				item.setText(8, result.getString("No_SKP"));
				item.setText(9, indFormat.format(result.getDouble("PokokPajak")));
				item.setText(10, indFormat.format(result.getDouble("Denda")));
				item.setText(11, indFormat.format(result.getDouble("DendaTerlambatbayar")));
				item.setText(12, indFormat.format(result.getDouble("Jumlah_Bayar")));
				item.setText(13, result.getString("Bulan_SKP"));
				item.setText(14, result.getString("Cicilan_Ke"));
				double sisa = (result.getDouble("PokokPajak") + result.getDouble("Denda") + result.getDouble("DendaTerlambatBayar")) - result.getDouble("Jumlah_Bayar");
				item.setText(15, indFormat.format(sisa));
				item.setText(16, result.getString("Nama_Penyetor"));
				try {
					totalPokokPajak += indFormat.parse(item.getText(9)).doubleValue();
					totalDenda += indFormat.parse(item.getText(10)).doubleValue();
					totalDendaTerlambat += indFormat.parse(item.getText(11)).doubleValue();
					totalJumlahBayar += indFormat.parse(item.getText(12)).doubleValue();
					totalSisa += sisa;
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			TableItem item = new TableItem(tbl_SSPD, SWT.NONE);
			item.setText(0, "Total");
			item.setText(9, indFormat.format(totalPokokPajak));
			item.setText(10, indFormat.format(totalDenda));
			item.setText(11, indFormat.format(totalDendaTerlambat));
			item.setText(12, indFormat.format(totalJumlahBayar));
			item.setText(15, indFormat.format(totalSisa));
		} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		}
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
	}
	
	private UserModul userModul = ControllerFactory.getMainController().getCpUserDAOImpl()
			.getHMUserModul(GlobalVariable.userModul.getIdUser()).get(7); // 1 merupakan id sub modul.
	
	private boolean isPrint(){		
		return userModul.getCetak();
	}
}