package com.dispenda.tunggakan.views;

import java.math.RoundingMode;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
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
import com.dispenda.dao.LogImp;
import com.dispenda.database.DBConnection;
import com.dispenda.model.BidangUsahaProvider;
import com.dispenda.model.KecamatanProvider;
import com.dispenda.model.PajakProvider;
import com.dispenda.model.Tunggakan;
import com.dispenda.model.UserModul;
import com.dispenda.object.GlobalVariable;
import com.dispenda.object.Preferences;
import com.dispenda.report.ReportAction;
import com.dispenda.report.ReportAppContext;
import com.dispenda.report.ReportModule;

import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class TunggakanSptpdView extends ViewPart {
	public TunggakanSptpdView() {
	}
	
	public static final String ID = TunggakanSptpdView.class.getName();
	private Text txtPajakTerutang1;
	private Text txtDenda1;
	private Text txtTotal1;
	private Table tblSPTPD;
	private Table tblSKPDKB;
	private Text txtPajakTerutang;
	private Text txtDenda;
	private Text txtTotal;
	private Color fontColor = new Color(Display.getCurrent(),Preferences.FONT_COLOR);
	private DateTime calSampai;
	private DateTime calDari;
	private Timestamp dateNow;
	private Combo cmbSubKewajibanPajak;
	private Combo cmbKewajibanPajak;
	private Integer idSubPajak = 0;
	private Date masaPajakDari;
	private Date masaPajakSampai;
	private Locale ind = new Locale("id", "ID");
	private NumberFormat indFormat = NumberFormat.getCurrencyInstance(ind);
	private UserModul userModul = ControllerFactory.getMainController().getCpUserDAOImpl()
			.getHMUserModul(GlobalVariable.userModul.getIdUser()).get(13);
	private Button btnWPTutup;
	private HashMap<String, List<String>> hmTagihan = new HashMap<String, List<String>>();
	private HashMap<String, List<String>> hmTagihanSKPDKB = new HashMap<String, List<String>>();
	private Combo cmbKecamatan;
	private org.eclipse.swt.widgets.List list;
	private CTabFolder tabFolder;
	private CTabItem tbtmSkpdkb;
	private CTabItem tbtmSPTPD;
	
	@Override
	public void createPartControl(Composite parent) {
		dateNow = (java.sql.Timestamp)ControllerFactory.getMainController().getCpWajibPajakDAOImpl().getDateNow();
		ScrolledComposite scrolledComposite = new ScrolledComposite(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		
		Composite composite = new Composite(scrolledComposite, SWT.NONE);
		composite.setForeground(fontColor);
		composite.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		composite.setLayout(new GridLayout(11, false));
		
		Label lblMasaPajakDari = new Label(composite, SWT.NONE);
		GridData gd_lblMasaPajakDari = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblMasaPajakDari.widthHint = 83;
		lblMasaPajakDari.setLayoutData(gd_lblMasaPajakDari);
		lblMasaPajakDari.setForeground(fontColor);
		lblMasaPajakDari.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblMasaPajakDari.setText("Bulan Terbit");
		
		calDari = new DateTime(composite, SWT.BORDER | SWT.DROP_DOWN);
		calDari.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		calDari.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		Label lblSampai = new Label(composite, SWT.NONE);
		lblSampai.setForeground(fontColor);
		lblSampai.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblSampai.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		lblSampai.setText("Sampai");
		
		calSampai = new DateTime(composite, SWT.BORDER | SWT.DROP_DOWN);
		calSampai.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1));
		calSampai.addSelectionListener(new SelectionAdapter() {
			@SuppressWarnings("deprecation")
			@Override
			public void widgetSelected(SelectionEvent e) {
				Date masaDari = new Date(calDari.getYear() - 1900, calDari.getMonth(), calDari.getDay());
				Date masaSampai = new Date(calSampai.getYear() - 1900, calSampai.getMonth(), calSampai.getDay());
				if (masaDari.after(masaSampai)){
					MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Silahkan cek masa pajak");
					calSampai.setDate(dateNow.getYear() + 1900, dateNow.getMonth(), dateNow.getDate());
				}
			}
		});
		calSampai.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		
		Label lblKecamatan = new Label(composite, SWT.NONE);
		lblKecamatan.setForeground(fontColor);
		lblKecamatan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblKecamatan.setText("Kecamatan");
		
		cmbKecamatan = new Combo(composite, SWT.READ_ONLY);
		cmbKecamatan.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		cmbKecamatan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		GridData gd_cmbKecamatan = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_cmbKecamatan.widthHint = 183;
		cmbKecamatan.setLayoutData(gd_cmbKecamatan);
		UIController.INSTANCE.loadKecamatan(cmbKecamatan, KecamatanProvider.INSTANCE.getKecamatan().toArray());
		
		Button btnPilih = new Button(composite, SWT.NONE);
		GridData gd_btnPilih = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnPilih.widthHint = 50;
		btnPilih.setLayoutData(gd_btnPilih);
		btnPilih.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				boolean isExist = false;
				for (int i=0;i<list.getItemCount();i++){
					if (list.getItem(i).equalsIgnoreCase(cmbKecamatan.getText()))
						isExist = true;
				}
				if (!isExist)
					list.add(cmbKecamatan.getText());
			}
		});
		btnPilih.setText("Pilih");
		
		Label lblKewajibanPajak = new Label(composite, SWT.NONE);
		lblKewajibanPajak.setForeground(fontColor);
		lblKewajibanPajak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblKewajibanPajak.setText("Kewajiban Pajak");
		
		cmbKewajibanPajak = new Combo(composite, SWT.READ_ONLY);
		cmbKewajibanPajak.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				UIController.INSTANCE.loadBidangUsaha(cmbSubKewajibanPajak, BidangUsahaProvider.INSTANCE.getBidangUsaha((Integer)cmbKewajibanPajak.getData(Integer.toString(cmbKewajibanPajak.getSelectionIndex()))).toArray());
				idSubPajak = 0;
			}
		});
		cmbKewajibanPajak.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		cmbKewajibanPajak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		GridData gd_cmbKewajibanPajak = new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1);
		gd_cmbKewajibanPajak.widthHint = 200;
		cmbKewajibanPajak.setLayoutData(gd_cmbKewajibanPajak);
		UIController.INSTANCE.loadPajak(cmbKewajibanPajak, PajakProvider.INSTANCE.getPajak().toArray());
		
		Label lblNewLabel = new Label(composite, SWT.NONE);
		lblNewLabel.setText("-");
		lblNewLabel.setForeground(fontColor);
		lblNewLabel.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		cmbSubKewajibanPajak = new Combo(composite, SWT.READ_ONLY);
		cmbSubKewajibanPajak.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				idSubPajak = (Integer)cmbSubKewajibanPajak.getData(Integer.toString(cmbSubKewajibanPajak.getSelectionIndex()));
			}
		});
		cmbSubKewajibanPajak.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		cmbSubKewajibanPajak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		GridData gd_cmbSubKewajibanPajak = new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1);
		gd_cmbSubKewajibanPajak.widthHint = 200;
		cmbSubKewajibanPajak.setLayoutData(gd_cmbSubKewajibanPajak);
		new Label(composite, SWT.NONE);
		
		list = new org.eclipse.swt.widgets.List(composite, SWT.BORDER);
		list.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE-1, SWT.NORMAL));
		GridData gd_list = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 2);
		gd_list.widthHint = 150;
		gd_list.heightHint = 80;
		list.setLayoutData(gd_list);
		
		Button btnHapus = new Button(composite, SWT.NONE);
		GridData gd_btnHapus = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnHapus.widthHint = 50;
		btnHapus.setLayoutData(gd_btnHapus);
		btnHapus.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (list.getSelectionIndex()>-1)
					list.remove(list.getSelectionIndex());
				else
					MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Pilih kecamatan yang akan dihapus");
			}
		});
		btnHapus.setText("Hapus");
		
		Label lblTampilkanWpTutup = new Label(composite, SWT.NONE);
		lblTampilkanWpTutup.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		lblTampilkanWpTutup.setForeground(fontColor);
		lblTampilkanWpTutup.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblTampilkanWpTutup.setText("Tampilkan WP Tutup");
		
		btnWPTutup = new Button(composite, SWT.CHECK);
		btnWPTutup.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		btnWPTutup.setSelection(true);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		
		Group grpSptpd = new Group(composite, SWT.NONE);
		grpSptpd.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 7, 1));
		grpSptpd.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		grpSptpd.setLayout(new GridLayout(4, false));
		grpSptpd.setText("SPTPD");
		
		Button btnTampil = new Button(grpSptpd, SWT.NONE);
		GridData gd_btnTampil = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnTampil.widthHint = 68;
		btnTampil.setLayoutData(gd_btnTampil);
		btnTampil.addSelectionListener(new SelectionAdapter() {
			@SuppressWarnings("deprecation")
			@Override
			public void widgetSelected(SelectionEvent e) {
				ResultSet resultSPT = null;
				deleteColumns();
				masaPajakDari = new Date(calDari.getYear() - 1900, calDari.getMonth(), calDari.getDay());
				masaPajakSampai = new Date(calSampai.getYear() - 1900, calSampai.getMonth(), calSampai.getDay());
				if (cmbKewajibanPajak.getSelectionIndex() < 0){
					MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Silahkan pilih Kode Pajak");
					cmbKewajibanPajak.forceFocus();
				}else{
					tblSPTPD.removeAll();
					resultSPT = ControllerFactory.getMainController().getCpLaporanDAOImpl().getTunggakanSPTPD(cmbKewajibanPajak.getData(Integer.toString(cmbKewajibanPajak.getSelectionIndex())).toString(), idSubPajak, masaPajakDari, masaPajakSampai, btnWPTutup.getSelection(), list.getItems());
					createColumnSPTPD();
					loadTableSPTPD(resultSPT);
					tabFolder.setSelection(tbtmSPTPD);
				}
			}
		});
		btnTampil.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BORDER));
		btnTampil.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnTampil.setText("Tampil");
		
		Button btnCetak = new Button(grpSptpd, SWT.NONE);
		btnCetak.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(isPrint()){
					ReportAppContext.name = "Variable";
					String terbit = "";
					String pajak = "";
					if (calDari.getYear() == calSampai.getYear()){
						if (calDari.getMonth() == calSampai.getMonth()) 
							terbit = UIController.INSTANCE.formatMonth(calDari.getMonth() + 1, ind) + " " + calDari.getYear();
						else
							terbit = UIController.INSTANCE.formatMonth(calDari.getMonth() + 1, ind) + " - " +  UIController.INSTANCE.formatMonth(calSampai.getMonth() + 1, ind) + " " + calSampai.getYear();
					}else
						terbit = UIController.INSTANCE.formatMonth(calDari.getMonth() + 1, ind) + " " + calDari.getYear() + " - " +  UIController.INSTANCE.formatMonth(calSampai.getMonth() + 1, ind) + " " + calSampai.getYear();
					pajak = cmbKewajibanPajak.getText();
					if (!cmbSubKewajibanPajak.getText().equalsIgnoreCase(""))
						pajak += " - " + cmbSubKewajibanPajak.getText();
					ReportAppContext.map.put("terbit", terbit);
					ReportAppContext.map.put("pajak", pajak);
					ReportAppContext.nameObject = "TunggakanSPTPD";
					ReportAppContext.classLoader = TunggakanSptpdView.class.getClassLoader();
					if(!ReportAppContext.object.isEmpty())
						ReportAppContext.object.clear();
					for(int j = 0;j<tblSPTPD.getItems().length-1;j++){
						List<String> values = new ArrayList<String>();
						for(int i=0;i<tblSPTPD.getColumnCount();i++){
							if (i==12 || i==14 || i==15 || i==16 || i==17)
								values.add(tblSPTPD.getItem(j).getText(i).substring(2).replace(".", "").replace(",", "."));
							else
								values.add(tblSPTPD.getItem(j).getText(i));
						}
						ReportAppContext.object.put(Integer.toString(j), values);
					}
					
					ReportAction.start("TunggakanSPTPD.rptdesign");
				}else
					MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Anda tidak memiliki hak untuk mencetak.");
			}
		});
		btnCetak.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BORDER));
		btnCetak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnCetak.setText("Cetak Daftar");
		
		Button btnCetakTagihan = new Button(grpSptpd, SWT.NONE);
		btnCetakTagihan.addSelectionListener(new SelectionAdapter() {
			@SuppressWarnings("deprecation")
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (cmbKewajibanPajak.getSelectionIndex() < 0){
					MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Silahkan pilih Kode Pajak");
					cmbKewajibanPajak.forceFocus();
				}else{
//					Calendar cal = Calendar.getInstance();
//				    cal.setTime(new Date(118,02,03));
//				    Date now = new Date(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_WEEK));
//				    if (cal.get(Calendar.DAY_OF_WEEK) == 1)
//				    	cal.add(Calendar.DATE, -2);
//				    else if (cal.get(Calendar.DAY_OF_WEEK) == 7)
//				    	cal.add(Calendar.DATE, -1);
					
					if(isPrint()){
						masaPajakDari = new Date(calDari.getYear() - 1900, calDari.getMonth(), calDari.getDay());
						masaPajakSampai = new Date(calSampai.getYear() - 1900, calSampai.getMonth(), calSampai.getDay());
						ResultSet resultSPT = ControllerFactory.getMainController().getCpLaporanDAOImpl().getTagihanSPTPD(cmbKewajibanPajak.getData(Integer.toString(cmbKewajibanPajak.getSelectionIndex())).toString(), idSubPajak, masaPajakDari, masaPajakSampai, btnWPTutup.getSelection(), list.getItems());
						
						ReportAppContext.name = "Variable";
						/*String terbit = "";
						String pajak = "";
						if (calDari.getYear() == calSampai.getYear()){
							if (calDari.getMonth() == calSampai.getMonth()) 
								terbit = UIController.INSTANCE.formatMonth(calDari.getMonth() + 1, ind) + " " + calDari.getYear();
							else
								terbit = UIController.INSTANCE.formatMonth(calDari.getMonth() + 1, ind) + " - " +  UIController.INSTANCE.formatMonth(calSampai.getMonth() + 1, ind) + " " + calSampai.getYear();
						}else
							terbit = UIController.INSTANCE.formatMonth(calDari.getMonth() + 1, ind) + " " + calDari.getYear() + " - " +  UIController.INSTANCE.formatMonth(calSampai.getMonth() + 1, ind) + " " + calSampai.getYear();
						pajak = cmbKewajibanPajak.getText();
						if (!cmbSubKewajibanPajak.getText().equalsIgnoreCase(""))
							pajak += " - " + cmbSubKewajibanPajak.getText();
						ReportAppContext.map.put("terbit", terbit);
						ReportAppContext.map.put("pajak", pajak);*/
						ReportAppContext.nameObject = "SuratTagihan";
						ReportAppContext.map.put("Tahun", UIController.INSTANCE.formatMonth(dateNow.getMonth() + 1, ind) + " " + String.valueOf(dateNow.getYear() + 1900));
						ReportAppContext.classLoader = TunggakanSptpdView.class.getClassLoader();
						if(!ReportAppContext.object.isEmpty())
							ReportAppContext.object.clear();
//						for(int j = 0;j<tblSPTPD.getItems().length-1;j++){
						int j=0;
						try {
							while (resultSPT.next()){
								List<String> valuesTagihan = new ArrayList<String>();
								SimpleDateFormat sdf = new SimpleDateFormat("MMMM yyyy");
								String masaPajak = "";
								Date masaDari = resultSPT.getDate("Masa_Pajak_Dari");
								Date masaSampai = resultSPT.getDate("Masa_Pajak_Sampai");
//								if (UIController.INSTANCE.getMonthDiff(masaSampai, dateNow) == 2){
//									masaPajak = sdf.format(masaSampai);
//								}else{
									if (masaDari.getYear() == masaSampai.getYear() && masaDari.getMonth() == masaSampai.getMonth())
										masaPajak = sdf.format(masaDari);
									else if (masaDari.getYear() == masaSampai.getYear())
										masaPajak = UIController.INSTANCE.formatMonth(masaDari.getMonth() + 1, Locale.getDefault()) + " - " + sdf.format(masaSampai);
									else
										masaPajak = sdf.format(masaDari) + " - " + sdf.format(masaSampai);
//								}
								Calendar cal = Calendar.getInstance();
							    cal.setTime(dateNow);
							    cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
							    if (cal.get(Calendar.DAY_OF_WEEK) == 1)
							    	cal.add(Calendar.DATE, -2);
							    else if (cal.get(Calendar.DAY_OF_WEEK) == 7)
							    	cal.add(Calendar.DATE, -1);
							    Date now = new Date(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
								String tglbayar = now.getDate() + " " + UIController.INSTANCE.formatMonth(now.getMonth()+1, ind) + " " + now.getYear();
								valuesTagihan.add(resultSPT.getString("NPWPD")); 
								valuesTagihan.add(resultSPT.getString("Nama_Badan"));
								valuesTagihan.add(resultSPT.getString("Alabad_Jalan"));
								valuesTagihan.add(masaPajak);
								valuesTagihan.add(indFormat.format(resultSPT.getDouble("Pajak_Terutang")));
								valuesTagihan.add(new ReportModule().getTerbilang(resultSPT.getDouble("Pajak_Terutang")));
								valuesTagihan.add(tglbayar);
								ReportAppContext.object.put(Integer.toString(j), valuesTagihan);
								hmTagihan.put(Integer.toString(j), valuesTagihan);
								j++;
							}
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						
						ReportAction.start("SuratTagihanSPT.rptdesign");
					}else
						MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Anda tidak memiliki hak untuk mencetak.");
				}
			}
		});
		btnCetakTagihan.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BORDER));
		btnCetakTagihan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnCetakTagihan.setText("Cetak Tagihan");
		
		Button btnCetakAmplop = new Button(grpSptpd, SWT.NONE);
		btnCetakAmplop.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (hmTagihan.size() < 1)
					MessageDialog.openError(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Tidak ada data untuk dicetak");
				else{
					if(!ReportAppContext.object.isEmpty())
						ReportAppContext.object.clear();
					ReportAppContext.nameObject = "Amplop";
					ReportAppContext.classLoader = TunggakanSptpdView.class.getClassLoader();
					Integer count = 0;
					for (int i=0;i<hmTagihan.size();i++){
						List<String> values = new ArrayList<String>();
						values.add(hmTagihan.get(""+i).get(1));
						values.add(hmTagihan.get(""+i).get(2));
						ReportAppContext.object.put(count.toString(), values);
						count++;
					}
					ReportAction.start("Amplop.rptdesign");
				}
			}
		});
		btnCetakAmplop.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BORDER));
		btnCetakAmplop.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnCetakAmplop.setText("Cetak Amplop");
		
		Group grpSkpdkb = new Group(composite, SWT.NONE);
		grpSkpdkb.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1));
		grpSkpdkb.setLayout(new GridLayout(4, false));
		grpSkpdkb.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		grpSkpdkb.setText("SKPDKB");
		
		Button btnTampilSKPDKB = new Button(grpSkpdkb, SWT.NONE);
		btnTampilSKPDKB.addSelectionListener(new SelectionAdapter() {
			@SuppressWarnings("deprecation")
			@Override
			public void widgetSelected(SelectionEvent e) {
				List<Tunggakan> listSKPDKB = new ArrayList<Tunggakan>();
				deleteColumns();
				masaPajakDari = new Date(calDari.getYear() - 1900, calDari.getMonth(), calDari.getDay());
				masaPajakSampai = new Date(calSampai.getYear() - 1900, calSampai.getMonth(), calSampai.getDay());
				if (cmbKewajibanPajak.getSelectionIndex() < 0){
					MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Silahkan pilih Kode Pajak");
					cmbKewajibanPajak.forceFocus();
				}else{
					tblSKPDKB.removeAll();
					listSKPDKB = ControllerFactory.getMainController().getCpLaporanDAOImpl().getTunggakanSKPDKB(cmbKewajibanPajak.getData(Integer.toString(cmbKewajibanPajak.getSelectionIndex())).toString(), idSubPajak, masaPajakDari, masaPajakSampai, btnWPTutup.getSelection(), list.getItems());
					listSKPDKB.addAll(ControllerFactory.getMainController().getCpLaporanDAOImpl().getTunggakanAngsuran(cmbKewajibanPajak.getData(Integer.toString(cmbKewajibanPajak.getSelectionIndex())).toString(), idSubPajak, masaPajakDari, masaPajakSampai, btnWPTutup.getSelection(), list.getItems()));
					Collections.sort(listSKPDKB);
					createColumnSKPDKB();
					loadTableSKPDKB(listSKPDKB);
					tabFolder.setSelection(tbtmSkpdkb);
				}
			}
		});
		GridData gd_btnTampilSKPDKB = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnTampilSKPDKB.widthHint = 67;
		btnTampilSKPDKB.setLayoutData(gd_btnTampilSKPDKB);
		btnTampilSKPDKB.setText("Tampil");
		btnTampilSKPDKB.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BORDER));
		btnTampilSKPDKB.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		Button btnCetakSKPDKB = new Button(grpSkpdkb, SWT.NONE);
		btnCetakSKPDKB.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(isPrint()){
					ReportAppContext.name = "Variable";
					String terbit = "";
					String pajak = "";
					if (calDari.getYear() == calSampai.getYear()){
						if (calDari.getMonth() == calSampai.getMonth()) 
							terbit = UIController.INSTANCE.formatMonth(calDari.getMonth() + 1, ind) + " " + calDari.getYear();
						else
							terbit = UIController.INSTANCE.formatMonth(calDari.getMonth() + 1, ind) + " - " +  UIController.INSTANCE.formatMonth(calSampai.getMonth() + 1, ind) + " " + calSampai.getYear();
					}else
						terbit = UIController.INSTANCE.formatMonth(calDari.getMonth() + 1, ind) + " " + calDari.getYear() + " - " +  UIController.INSTANCE.formatMonth(calSampai.getMonth() + 1, ind) + " " + calSampai.getYear();
					pajak = cmbKewajibanPajak.getText();
					if (!cmbSubKewajibanPajak.getText().equalsIgnoreCase(""))
						pajak += " - " + cmbSubKewajibanPajak.getText();
					ReportAppContext.map.put("terbit", terbit);
					ReportAppContext.map.put("pajak", pajak);
					ReportAppContext.nameObject = "TunggakanSKPDKB";
					ReportAppContext.classLoader = TunggakanSptpdView.class.getClassLoader();
					if(!ReportAppContext.object.isEmpty())
						ReportAppContext.object.clear();
					for(int j = 0;j<tblSKPDKB.getItems().length-1;j++){
						List<String> values = new ArrayList<String>();
						for(int i=0;i<tblSKPDKB.getColumnCount();i++){
							if (i==11 || i==12 || i==13 || i==14)
								values.add(tblSKPDKB.getItem(j).getText(i).substring(2).replace(".", "").replace(",", "."));
							else
								values.add(tblSKPDKB.getItem(j).getText(i));
						}
						ReportAppContext.object.put(Integer.toString(j), values);
					}
					
					ReportAction.start("TunggakanSKPDKB.rptdesign");
				}else
					MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Anda tidak memiliki hak untuk mencetak.");
			}
		});
		btnCetakSKPDKB.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BORDER));
		btnCetakSKPDKB.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnCetakSKPDKB.setText("Cetak Daftar");
		
		Button btnCetakTagihanSKPDKB = new Button(grpSkpdkb, SWT.NONE);
		btnCetakTagihanSKPDKB.addSelectionListener(new SelectionAdapter() {
			@SuppressWarnings("deprecation")
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (cmbKewajibanPajak.getSelectionIndex() < 0){
					MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Silahkan pilih Kode Pajak");
					cmbKewajibanPajak.forceFocus();
				}else{
					if(isPrint()){
						masaPajakDari = new Date(calDari.getYear() - 1900, calDari.getMonth(), calDari.getDay());
						masaPajakSampai = new Date(calSampai.getYear() - 1900, calSampai.getMonth(), calSampai.getDay());
//						ResultSet resultSPT = ControllerFactory.getMainController().getCpLaporanDAOImpl().getTagihanSPTPD(cmbKewajibanPajak.getData(Integer.toString(cmbKewajibanPajak.getSelectionIndex())).toString(), idSubPajak, masaPajakDari, masaPajakSampai, btnWPTutup.getSelection(), list.getItems());

						ReportAppContext.name = "Variable";
						ReportAppContext.nameObject = "SuratTagihanSKPDKB";
						ReportAppContext.map.put("Tahun", UIController.INSTANCE.formatMonth(dateNow.getMonth() + 1, ind) + " " + String.valueOf(dateNow.getYear() + 1900));
						ReportAppContext.classLoader = TunggakanSptpdView.class.getClassLoader();
						if(!ReportAppContext.object.isEmpty())
							ReportAppContext.object.clear();
//						int j=0;
//						try {
							for (int i=0;i<tblSKPDKB.getItems().length-1;i++){
								List<String> valuesTagihan = new ArrayList<String>();
								SimpleDateFormat sdf = new SimpleDateFormat("MMMM yyyy");
								String masaPajak = "";
//								Date masaDari = resultSPT.getDate("Masa_Pajak_Dari");
//								Date masaSampai = resultSPT.getDate("Masa_Pajak_Sampai");
//								if (UIController.INSTANCE.getMonthDiff(masaSampai, dateNow) == 2){
//									masaPajak = sdf.format(masaSampai);
//								}else{
									/*if (masaDari.getYear() == masaSampai.getYear() && masaDari.getMonth() == masaSampai.getMonth())
										masaPajak = sdf.format(masaDari);
									else if (masaDari.getYear() == masaSampai.getYear())
										masaPajak = UIController.INSTANCE.formatMonth(masaDari.getMonth() + 1, Locale.getDefault()) + " - " + sdf.format(masaSampai);
									else
										masaPajak = sdf.format(masaDari) + " - " + sdf.format(masaSampai);*/
//								}
								Calendar cal = Calendar.getInstance();
							    cal.setTime(dateNow);
							    cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
							    if (cal.get(Calendar.DAY_OF_WEEK) == 1)
							    	cal.add(Calendar.DATE, -2);
							    else if (cal.get(Calendar.DAY_OF_WEEK) == 7)
							    	cal.add(Calendar.DATE, -1);
							    Date now = new Date(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
								String tglbayar = now.getDate() + " " + UIController.INSTANCE.formatMonth(now.getMonth()+1, ind) + " " + now.getYear();
								String strTempo = tblSKPDKB.getItem(i).getText(10);
								Date dtTempo = new Date(Integer.valueOf(strTempo.split("-")[2]), Integer.valueOf(strTempo.split("-")[1]), Integer.valueOf(strTempo.split("-")[0]));
								Integer bln = now.getMonth()+1;
								if (dtTempo.getDate()<now.getDate())
									bln += 1;
								String tglTempo = dtTempo.getDate() + "-" + bln + "-" + now.getYear();
								String angsuran = tblSKPDKB.getItem(i).getText(8);
								valuesTagihan.add(tblSKPDKB.getItem(i).getText(1));
								valuesTagihan.add(tblSKPDKB.getItem(i).getText(2));
								valuesTagihan.add(tblSKPDKB.getItem(i).getText(4));
								valuesTagihan.add(tblSKPDKB.getItem(i).getText(9));
								valuesTagihan.add(tblSKPDKB.getItem(i).getText(11).substring(2).replace(".", "").replace(",", "."));
								valuesTagihan.add(angsuran.equalsIgnoreCase("0")?"-":angsuran);
//								valuesTagihan.add(new ReportModule().getTerbilang(indFormat.parse(tblSKPDKB.getItem(i).getText(11).substring(2).replace(".", "").replace(",", ".")).doubleValue()));
								valuesTagihan.add(tglbayar);
								valuesTagihan.add(tblSKPDKB.getItem(i).getText(12).substring(2).replace(".", "").replace(",", "."));
								valuesTagihan.add(tblSKPDKB.getItem(i).getText(13).substring(2).replace(".", "").replace(",", "."));
								valuesTagihan.add(tblSKPDKB.getItem(i).getText(14).substring(2).replace(".", "").replace(",", "."));
								valuesTagihan.add(tglTempo);
								
								ReportAppContext.object.put(Integer.toString(i), valuesTagihan);
								hmTagihanSKPDKB.put(Integer.toString(i), valuesTagihan);
//								j++;
							}
						/*} catch (SQLException pe) {
							// TODO Auto-generated catch block
							pe.printStackTrace();
						}*/
						
						ReportAction.start("SuratTagihanSKPDKB.rptdesign");
					}else
						MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Anda tidak memiliki hak untuk mencetak.");
				}
			}
		});
		btnCetakTagihanSKPDKB.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnCetakTagihanSKPDKB.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BORDER));
		btnCetakTagihanSKPDKB.setText("Cetak Tagihan");
		
		Button btnCetakAmplopSKPDKB = new Button(grpSkpdkb, SWT.NONE);
		btnCetakAmplopSKPDKB.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (hmTagihanSKPDKB.size() < 1)
					MessageDialog.openError(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Tidak ada data untuk dicetak");
				else{
					if(!ReportAppContext.object.isEmpty())
						ReportAppContext.object.clear();
					ReportAppContext.nameObject = "Amplop";
					ReportAppContext.classLoader = TunggakanSptpdView.class.getClassLoader();
					Integer count = 0;
					for (int i=0;i<hmTagihanSKPDKB.size();i++){
						List<String> values = new ArrayList<String>();
						values.add(hmTagihanSKPDKB.get(""+i).get(1));
						values.add(hmTagihanSKPDKB.get(""+i).get(2));
						ReportAppContext.object.put(count.toString(), values);
						count++;
					}
					ReportAction.start("Amplop.rptdesign");
				}
			}
		});
		btnCetakAmplopSKPDKB.setText("Cetak Amplop");
		btnCetakAmplopSKPDKB.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BORDER));
		btnCetakAmplopSKPDKB.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		Group group = new Group(composite, SWT.NONE);
		group.setLayout(new GridLayout(1, false));
		group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 11, 3));
		
		tabFolder = new CTabFolder(group, SWT.BORDER);
		tabFolder.setSimple(false);
		tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		tabFolder.setSelectionBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		
		tbtmSPTPD = new CTabItem(tabFolder, SWT.NONE);
		tbtmSPTPD.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		tbtmSPTPD.setText("SPTPD");
		tabFolder.setSelection(tbtmSPTPD);
		
		Composite compSPTPD = new Composite(tabFolder, SWT.NONE);
		tbtmSPTPD.setControl(compSPTPD);
		compSPTPD.setLayout(new GridLayout(6, false));
		
		tblSPTPD = new Table(compSPTPD, SWT.BORDER | SWT.FULL_SELECTION | SWT.VIRTUAL);
		tblSPTPD.setBackground(SWTResourceManager.getColor(173, 216, 230));
		tblSPTPD.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 6, 6));
		tblSPTPD.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE-1, SWT.NORMAL));
		tblSPTPD.setHeaderVisible(true);
		tblSPTPD.setLinesVisible(true);
		
		Label label_1 = new Label(compSPTPD, SWT.NONE);
		label_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label_1.setText("Pajak Terutang :");
		label_1.setForeground(fontColor);
		label_1.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		txtPajakTerutang1 = new Text(compSPTPD, SWT.BORDER);
		txtPajakTerutang1.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		txtPajakTerutang1.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		txtPajakTerutang1.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtPajakTerutang1.setEditable(false);
		txtPajakTerutang1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label label_2 = new Label(compSPTPD, SWT.NONE);
		label_2.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label_2.setText("+ Denda :");
		label_2.setForeground(fontColor);
		label_2.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		txtDenda1 = new Text(compSPTPD, SWT.BORDER);
		txtDenda1.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		txtDenda1.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		txtDenda1.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtDenda1.setEditable(false);
		txtDenda1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label label_3 = new Label(compSPTPD, SWT.NONE);
		label_3.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label_3.setText(" = Total :");
		label_3.setForeground(fontColor);
		label_3.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		txtTotal1 = new Text(compSPTPD, SWT.BORDER);
		txtTotal1.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		txtTotal1.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		txtTotal1.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtTotal1.setEditable(false);
		txtTotal1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		tbtmSkpdkb = new CTabItem(tabFolder, SWT.NONE);
		tbtmSkpdkb.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		tbtmSkpdkb.setText("SKPDKB");
		
		Composite compSKPDKB = new Composite(tabFolder, SWT.NONE);
		tbtmSkpdkb.setControl(compSKPDKB);
		compSKPDKB.setLayout(new GridLayout(7, false));
		
		tblSKPDKB = new Table(compSKPDKB, SWT.BORDER | SWT.FULL_SELECTION | SWT.VIRTUAL);
		tblSKPDKB.setBackground(SWTResourceManager.getColor(173, 216, 230));
		tblSKPDKB.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 7, 1));
		tblSKPDKB.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE-1, SWT.NORMAL));
		tblSKPDKB.setHeaderVisible(true);
		tblSKPDKB.setLinesVisible(true);
		
		Label lblPajakTerutang = new Label(compSKPDKB, SWT.NONE);
		lblPajakTerutang.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPajakTerutang.setForeground(fontColor);
		lblPajakTerutang.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblPajakTerutang.setText("Pajak Terutang :");
		
		txtPajakTerutang = new Text(compSKPDKB, SWT.BORDER);
		txtPajakTerutang.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		txtPajakTerutang.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		txtPajakTerutang.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		Label lblDenda = new Label(compSKPDKB, SWT.NONE);
		lblDenda.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblDenda.setForeground(fontColor);
		lblDenda.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblDenda.setText("+ Denda :");
		
		txtDenda = new Text(compSKPDKB, SWT.BORDER);
		txtDenda.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		txtDenda.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		txtDenda.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		Label lblTotal = new Label(compSKPDKB, SWT.NONE);
		lblTotal.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 2, 1));
		lblTotal.setForeground(fontColor);
		lblTotal.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblTotal.setText(" = Total :");
		
		txtTotal = new Text(compSKPDKB, SWT.BORDER);
		txtTotal.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		txtTotal.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		txtTotal.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		scrolledComposite.setContent(composite);
		scrolledComposite.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));

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
	}

	private void createColumnSPTPD(){
		if(tblSPTPD.getColumnCount() <= 0){
			String[] listColumn = {"No.","NPWPD", "Nama Badan", "Nama Pemilik", "Alamat", "Sub Pajak", "Kecamatan", "No SPTPD", 
					"Tanggal SPTPD", "Assesment", "Masa Pajak", "Jatuh Tempo", "Dasar Pengenaan", "Tarif Pajak", 
					"Pajak Terutang", "Denda Tambahan", "Denda SSPD", "Total", "Status"};
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
		
		
	}
	
	private void createColumnSKPDKB(){
		if (tblSKPDKB.getColumnCount() <= 0){
			String[] listColumn = {"No.","NPWPD", "Nama Badan", "Nama Pemilik", "Alamat", "Sub Pajak", "No SKP", 
					"Tanggal SKP", "Cicilan", "Masa Pajak", "Jatuh Tempo", "Pokok Pajak", "Denda", "Denda SSPD", "Total", "Status"};
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
		/*ResultSet bpkReklame = ControllerFactory.getMainController().getCpLaporanDAOImpl().getTunggakanSPTPDReklameBPK();
		ArrayList <String> bpkRklame = new ArrayList <String>();
		try {
			while (bpkReklame.next()){
				bpkRklame.add(bpkReklame.getString("NO_SKP"));
				//System.out.println(bpkReklame.getString("NO_SKP"));
				//if (bpkReklame.getString("NO_SKP").equals(result.getString("No_SPTPD")))
				//	dendaSspd = 0.0;
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/
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
				
				Double dendaSspd = UIController.INSTANCE.hitungDendaSSPD(result.getDouble("Pajak_Terutang"), jatuhTempo, "SPTPD", result.getString("NPWPD").substring(0, 1));
				item.setText(0, count+".");
				item.setText(1, result.getString("NPWPD"));
				item.setText(2, result.getString("Nama_Badan"));
				item.setText(3, result.getString("Nama_Pemilik"));
				item.setText(4, result.getString("Alabad_Jalan"));
				item.setText(5, result.getString("Nama_Bid_Usaha"));
				item.setText(6, result.getString("Name_Kecamatan"));
				item.setText(7, result.getString("No_SPTPD"));
				item.setText(8, formatter.format(result.getDate("Tgl_SPTPD")));
				item.setText(9, result.getString("Jenis_Assesment"));
				item.setText(10, UIController.INSTANCE.formatMonth(result.getDate("Masa_Pajak_Dari").getMonth() + 1, ind) + " " + (result.getDate("Masa_Pajak_Dari").getYear() + 1900));
				//item.setText(9, result.getString("Masa_Pajak_Sampai")); 
				item.setText(11, formatter.format(jatuhTempo));
				item.setText(12, indFormat.format(result.getDouble("Dasar_Pengenaan")));
				if (result.getString("tarif_Pajak")==null){
					item.setText(13, "-");
					dendaSspd = 0.0;
				}
				else{
					item.setText(13, result.getString("tarif_Pajak"));
				}
				item.setText(14, indFormat.format(result.getDouble("Pajak_Terutang")));
				item.setText(15, indFormat.format(result.getDouble("Denda_Tambahan")));
				//System.out.println();
				/*for (int i=0;i<bpkRklame.size();i++){
					//System.out.println(bpkRklame.get(i)+" "+result.getString("No_SPTPD"));
					if (bpkRklame.get(i).equals(result.getString("No_SPTPD"))){
						dendaSspd = 0.0;
					}
				}*/
				item.setText(16, indFormat.format(dendaSspd));
				item.setText(17, indFormat.format(result.getDouble("Pajak_Terutang") + result.getDouble("Denda_Tambahan") + dendaSspd));
				item.setText(18, result.getString("Status"));
				try {
					totalPajak += indFormat.parse(item.getText(14)).doubleValue();
					totalDenda += indFormat.parse(item.getText(15)).doubleValue();
					totalDendaSSPD += indFormat.parse(item.getText(16)).doubleValue();
					GrandTotal += indFormat.parse(item.getText(17)).doubleValue();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				count++;
			}
			TableItem item = new TableItem(tblSPTPD, SWT.NONE);
			item.setText(1, "Grand Total");
			item.setText(14, indFormat.format(totalPajak));
			item.setText(15, indFormat.format(totalDenda));
			item.setText(16, indFormat.format(totalDendaSSPD));
			item.setText(17, indFormat.format(GrandTotal));
			txtPajakTerutang1.setText(indFormat.format(totalPajak));
			txtDenda1.setText(indFormat.format(totalDenda));
			txtTotal1.setText(indFormat.format(GrandTotal));
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
		double totalDendaSSPD = 0;
		for (int i=0;i<list.size();i++){
			TableItem item = new TableItem(tblSKPDKB, SWT.NONE);
			item.setText(0, (i+1)+".");
			item.setText(1, list.get(i).getNpwpd());
			item.setText(2, list.get(i).getNamaBadan());
			item.setText(3, list.get(i).getNamaPemilik());
			item.setText(4, list.get(i).getAlamat());
			item.setText(5, list.get(i).getBidang_usaha());
			item.setText(6, list.get(i).getNoSkp());
			item.setText(7, formatter.format(list.get(i).getTglSkp()));
			item.setText(8, list.get(i).getCicilan().toString());
			item.setText(9, UIController.INSTANCE.formatMonth(list.get(i).getMasaPajakDari().getMonth() + 1, ind) + " " + (list.get(i).getMasaPajakDari().getYear() + 1900) + " - " + UIController.INSTANCE.formatMonth(list.get(i).getMasaPajakSampai().getMonth() + 1, ind) + " " + (list.get(i).getMasaPajakSampai().getYear() + 1900));
			item.setText(10, formatter.format(list.get(i).getTglJatuhTempo()));
			item.setText(11, indFormat.format(roundUP(list.get(i).getPokok() + list.get(i).getKenaikan())));
			item.setText(12, indFormat.format(roundUP(list.get(i).getDenda())));
			item.setText(13, indFormat.format(list.get(i).getDendaSspd()));
			item.setText(14, indFormat.format(roundUP(list.get(i).getPokok() + list.get(i).getKenaikan()) + roundUP(list.get(i).getDenda()) + list.get(i).getDendaSspd()));
			item.setText(15, list.get(i).getStatus());
			try {
				totalPajak += indFormat.parse(item.getText(11)).doubleValue();
				totalDenda += indFormat.parse(item.getText(12)).doubleValue();
				totalDendaSSPD += indFormat.parse(item.getText(13)).doubleValue();
				GrandTotal += indFormat.parse(item.getText(14)).doubleValue();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		TableItem item = new TableItem(tblSKPDKB, SWT.NONE);
		item.setText(1, "Grand Total");
		item.setText(11, indFormat.format(totalPajak));
		item.setText(12, indFormat.format(totalDenda));
		item.setText(13, indFormat.format(totalDendaSSPD));
		item.setText(14, indFormat.format(GrandTotal));
		txtPajakTerutang.setText(indFormat.format(totalPajak));
		txtDenda.setText(indFormat.format(totalDenda));
		txtTotal.setText(indFormat.format(GrandTotal));
	}
	
	private boolean isPrint(){		
		return userModul.getCetak();
	}
	
	private static double roundUP(double value) {
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
