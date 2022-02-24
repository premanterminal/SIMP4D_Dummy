package com.dispenda.laporan.views;

import java.io.File;
import java.io.IOException;
import java.math.RoundingMode;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StackLayout;
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
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.SWTResourceManager;

import com.dispenda.controller.ControllerFactory;
import com.dispenda.controller.UIController;
import com.dispenda.laporan.dialog.DetailKeuanganDialog;
import com.dispenda.model.BidangUsahaProvider;
import com.dispenda.model.PajakProvider;
import com.dispenda.model.UserModul;
import com.dispenda.object.GlobalVariable;
import com.dispenda.object.Preferences;
import com.dispenda.report.ReportAction;
import com.dispenda.report.ReportAppContext;

public class LaporanKeuanganView extends ViewPart {
	
	public static final String ID = LaporanKeuanganView.class.getName();
	private Color fontColor = new Color(Display.getCurrent(),Preferences.FONT_COLOR);
	private Combo cmbPajakSptpd;
	private Combo cmbSubPajakSptpd;
	private Table tableSptpd;
	private TableViewer tableViewerSptpd;
	private Combo cmbPajakSkpdkb;
	private Combo cmbSubPajakSkpdkb;
	private TableViewer tableViewerSkpdkb;
	private Table tableSkpdkb;
	private Timestamp dateNow;
	Locale ind = new Locale("id", "ID");
	NumberFormat indFormat = NumberFormat.getCurrencyInstance(ind);
	private DateTime calSPT;
	private Spinner yearSPT;
	private DateTime monthSPT;
	private Button btnTahun;
	private Button btnBulan;
	private Button btnTanggal;
	private Date masaPajakDari;
	private Date masaPajakSampai;
	private String masaPajak;
	private Button btnBulanSKPDKB;
	private Button btnTanggalSKPDKB;
	private Button btnTahunSKPDKB;
	private DateTime calSKPDKB;
	private Spinner yearSKPDKB;
	private DateTime monthSKPDKB;
	private String NoKet = "";
	private Button btnCetakNoSPT;
	private Button btnCetakNoSKP;
	private DateTime calSampaiSKPDKB;
	private Button btnPilihanSKPDKB;
	private DateTime calSampai;
	private Button btnPilihan;
	
	public LaporanKeuanganView() {
	}

	@SuppressWarnings("deprecation")
	@Override
	public void createPartControl(Composite parent) {
		Locale.setDefault(new Locale("id","ID"));
		
		dateNow = (java.sql.Timestamp)ControllerFactory.getMainController().getCpWajibPajakDAOImpl().getDateNow();
		ScrolledComposite scrolledComposite = new ScrolledComposite(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		
		Composite composite = new Composite(scrolledComposite, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));
		
		TabFolder tabFolder = new TabFolder(composite, SWT.NONE);
		tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		TabItem tbtmSptpd = new TabItem(tabFolder, SWT.NONE);
		tbtmSptpd.setText("SPTPD");
		
		Composite compositeSPTPD = new Composite(tabFolder, SWT.NONE);
		tbtmSptpd.setControl(compositeSPTPD);
		compositeSPTPD.setLayout(new GridLayout(3, false));
		
		Label lblKewajibanPajakSptpd = new Label(compositeSPTPD, SWT.NONE);
		lblKewajibanPajakSptpd.setText("Kewajiban Pajak");
		lblKewajibanPajakSptpd.setForeground(fontColor);
		lblKewajibanPajakSptpd.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		cmbPajakSptpd = new Combo(compositeSPTPD, SWT.READ_ONLY);
		cmbPajakSptpd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				UIController.INSTANCE.loadBidangUsaha(cmbSubPajakSptpd, BidangUsahaProvider.INSTANCE.getBidangUsaha((Integer)cmbPajakSptpd.getData(Integer.toString(cmbPajakSptpd.getSelectionIndex()))).toArray());
			}
		});
		GridData gd_cmbPajakSptpd = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_cmbPajakSptpd.widthHint = 250;
		cmbPajakSptpd.setLayoutData(gd_cmbPajakSptpd);
		cmbPajakSptpd.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		cmbPajakSptpd.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		cmbPajakSptpd.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		UIController.INSTANCE.loadPajak(cmbPajakSptpd, PajakProvider.INSTANCE.getPajak().toArray());
		new Label(compositeSPTPD, SWT.NONE);
		new Label(compositeSPTPD, SWT.NONE);
		
		cmbSubPajakSptpd = new Combo(compositeSPTPD, SWT.READ_ONLY);
		GridData gd_cmbSubPajakSptpd = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_cmbSubPajakSptpd.widthHint = 250;
		cmbSubPajakSptpd.setLayoutData(gd_cmbSubPajakSptpd);
		cmbSubPajakSptpd.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		cmbSubPajakSptpd.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		cmbSubPajakSptpd.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		new Label(compositeSPTPD, SWT.NONE);
		
		Label lblTanggalTerbit = new Label(compositeSPTPD, SWT.NONE);
		lblTanggalTerbit.setText("Tanggal Terbit");
		lblTanggalTerbit.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblTanggalTerbit.setForeground(fontColor);
		
		Group group = new Group(compositeSPTPD, SWT.NONE);
		GridData gd_group = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_group.widthHint = 344;
		gd_group.heightHint = 61;
		group.setLayoutData(gd_group);
		group.setLayout(new GridLayout(4, false));
		
		btnTanggal = new Button(group, SWT.RADIO);
		btnTanggal.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				calSPT.setVisible(true);
				calSPT.pack(true);
				monthSPT.setVisible(false);
				yearSPT.setVisible(false);
				calSampai.setVisible(false);
				setMasaPajak();
			}
		});
		btnTanggal.setText("Tanggal");
		btnTanggal.setSelection(false);
		btnTanggal.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BORDER));
		btnTanggal.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		btnBulan = new Button(group, SWT.RADIO);
		btnBulan.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				calSPT.setVisible(false);
				monthSPT.setVisible(true);
				yearSPT.setVisible(false);
				calSampai.setVisible(false);
				setMasaPajak();
			}
		});
		btnBulan.setText("Bulan");
		btnBulan.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BORDER));
		btnBulan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		btnTahun = new Button(group, SWT.RADIO);
		btnTahun.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				calSPT.setVisible(false);
				monthSPT.setVisible(false);
				yearSPT.setVisible(true);
				calSampai.setVisible(false);
				setMasaPajak();
			}
		});
		btnTahun.setText("Tahun");
		btnTahun.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BORDER));
		btnTahun.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		btnPilihan = new Button(group, SWT.RADIO);
		btnPilihan.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				calSPT.setVisible(true);
				calSPT.pack(true);
				calSampai.setVisible(true);
				monthSPT.setVisible(false);
				yearSPT.setVisible(false);
				setMasaPajak();
			}
		});
		btnPilihan.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BORDER));
		btnPilihan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnPilihan.setText("Pilihan");
		
		Composite composite_1 = new Composite(group, SWT.NONE);
		composite_1.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 3, 1));
		composite_1.setLayout(new StackLayout());
		
		calSPT = new DateTime(composite_1, SWT.BORDER | SWT.DROP_DOWN);
		calSPT.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setMasaPajak();
			}
		});
		calSPT.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BORDER));
		calSPT.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		calSPT.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		
		yearSPT = new Spinner(composite_1, SWT.BORDER);
		yearSPT.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setMasaPajak();
			}
		});
		yearSPT.setVisible(false);
		yearSPT.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		yearSPT.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		yearSPT.setValues(dateNow.getYear() + 1900, 2006, dateNow.getYear() + 1900, 0, 1, 10);
		yearSPT.setVisible(false);
		
		monthSPT = new DateTime(composite_1, SWT.BORDER | SWT.DROP_DOWN | SWT.SHORT);
		monthSPT.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setMasaPajak();
			}
		});
		monthSPT.setVisible(false);
		monthSPT.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		monthSPT.setVisible(false);
		monthSPT.setDay(1);
		monthSPT.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		
		calSampai = new DateTime(group, SWT.BORDER | SWT.DROP_DOWN);
		calSampai.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setMasaPajak();
			}
		});
		calSampai.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BORDER));
		calSampai.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		calSampai.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		calSampai.setVisible(false);
		new Label(group, SWT.NONE);
		new Label(group, SWT.NONE);
		new Label(group, SWT.NONE);
		new Label(group, SWT.NONE);
		new Label(compositeSPTPD, SWT.NONE);
		
		Button btnCheat = new Button(compositeSPTPD, SWT.NONE);
		GridData gd_btnCheat = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnCheat.widthHint = 61;
		btnCheat.setLayoutData(gd_btnCheat);
		btnCheat.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ImpSim imp = new ImpSim();
				try {
					imp.open();
				} catch (InstantiationException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IllegalAccessException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		if (userModul.getIdUser() == 1)
			btnCheat.setVisible(true);
		else
			btnCheat.setVisible(false);
		btnCheat.setText("Cheat");
		
		Composite compButtonSptpd = new Composite(compositeSPTPD, SWT.NONE);
		GridData gd_compButtonSptpd = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_compButtonSptpd.widthHint = 323;
		compButtonSptpd.setLayoutData(gd_compButtonSptpd);
		compButtonSptpd.setLayout(new GridLayout(4, false));
		
		Button btnLihatSptpd = new Button(compButtonSptpd, SWT.NONE);
		btnLihatSptpd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				tableSptpd.removeAll();
				ResultSet resultSPTPD = null;
				txtKet.setText("");
				if (cmbPajakSptpd.getText().equalsIgnoreCase(""))
					resultSPTPD = ControllerFactory.getMainController().getCpLaporanDAOImpl().GetListSPTPDbySKP(masaPajakDari, masaPajakSampai, "", "");
				else if (!cmbPajakSptpd.getText().equalsIgnoreCase("") && cmbSubPajakSptpd.getText().equalsIgnoreCase(""))
					resultSPTPD = ControllerFactory.getMainController().getCpLaporanDAOImpl().GetListSPTPDbySKP(masaPajakDari, masaPajakSampai, cmbPajakSptpd.getData(Integer.toString(cmbPajakSptpd.getSelectionIndex())).toString(),"");
				else
					resultSPTPD = ControllerFactory.getMainController().getCpLaporanDAOImpl().GetListSPTPDbySKP(masaPajakDari, masaPajakSampai, cmbPajakSptpd.getData(Integer.toString(cmbPajakSptpd.getSelectionIndex())).toString(), cmbSubPajakSptpd.getData(Integer.toString(cmbSubPajakSptpd.getSelectionIndex())).toString());
				if (tableSptpd.getColumnCount() <= 0)
					createColumn("sptpd");
				loadData(resultSPTPD, "sptpd");
				
			}
		});
		GridData gd_btnLihat = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnLihat.widthHint = 90;
		btnLihatSptpd.setLayoutData(gd_btnLihat);
		btnLihatSptpd.setText("Lihat");
		btnLihatSptpd.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		Button btnCetakSptpd = new Button(compButtonSptpd, SWT.NONE);
		GridData gd_btnCetakSptpd = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnCetakSptpd.widthHint = 90;
		btnCetakSptpd.setLayoutData(gd_btnCetakSptpd);
		btnCetakSptpd.setText("Cetak");
		btnCetakSptpd.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnCetakSptpd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(isPrint()){
					String namaPajak = cmbPajakSptpd.getText();
					if (btnTanggal.getSelection())
						namaPajak += " " + masaPajak;
					else if (btnBulan.getSelection())
						namaPajak += " Bulan " + masaPajak;
					else if (btnTahun.getSelection())
						namaPajak += " Tahun " + masaPajak;
					ReportAppContext.name = "Variable";
					ReportAppContext.map.put("NamaPajak", namaPajak);
					ReportAppContext.nameObject = "DaftarKeuangan";
					ReportAppContext.classLoader = LaporanKeuanganView.class.getClassLoader();
					if(!ReportAppContext.object.isEmpty())
						ReportAppContext.object.clear();
					for(int j = 0;j<tableSptpd.getItems().length-1;j++){
						List<String> values = new ArrayList<String>();
						for(int i=1;i<tableSptpd.getColumnCount();i++){
							if(tableSptpd.getItem(j).getText(i).startsWith("Rp"))
								values.add(tableSptpd.getItem(j).getText(i).substring(2).replace(".", ""));
							else
								values.add(tableSptpd.getItem(j).getText(i));
						}
						if(tableSptpd.getItem(j).getBackground().equals(SWTResourceManager.getColor(SWT.COLOR_RED)))
							values.add("Red");
						else if(tableSptpd.getItem(j).getBackground().equals(SWTResourceManager.getColor(SWT.COLOR_YELLOW)))
							values.add("Yellow");
						else
							values.add("");
						ReportAppContext.object.put(Integer.toString(j), values);
					}
					ReportAppContext.map.put("TotalPokok", tableSptpd.getItem(tableSptpd.getItemCount()-1).getText(6));
					ReportAppContext.map.put("TotalDenda", tableSptpd.getItem(tableSptpd.getItemCount()-1).getText(7));
					
					ReportAction.start("DaftarKeuangan.rptdesign");
				}else
					MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Anda tidak memiliki hak untuk mencetak.");
			}
		});
		
		Button btnCheckSptpd = new Button(compButtonSptpd, SWT.NONE);
		GridData gd_btnCheck = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnCheck.widthHint = 90;
		btnCheckSptpd.setLayoutData(gd_btnCheck);
		btnCheckSptpd.setText("Check");
		btnCheckSptpd.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnCheckSptpd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog fd = new FileDialog(Display.getCurrent().getActiveShell(), SWT.NONE);
				String filename = fd.open();
				if(filename != null){
					Workbook workbook;
					try {
						int tempIndex = 0;
						int tempCountTbl = 0;
						int countDiff = 0;
						int tempCountDiff = 0;
						String message = "";
						NoKet = "";
						workbook = Workbook.getWorkbook(new File(filename));
						Sheet sheet = workbook.getSheet(0);
						Cell totalCell = sheet.findCell("TOTAL");
						int countTbl = tableSptpd.getItemCount();
//						String totalString = sheet.getCell(totalCell.getColumn()+1, totalCell.getRow()).getContents();
//						if(totalString.split("]")[1].equalsIgnoreCase(tableSptpd.getItem(tableSptpd.getItemCount()-1).getText(4).substring(2))){
//							MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "Perbandingan Data", "Tidak ada data baru.");
//						}else{
							for (int i=totalCell.getRow();i>4;i--){
//								System.out.println(String.valueOf(i));
								Cell cellNo = sheet.getCell(4, i-1);
								if (cellNo.getContents().equalsIgnoreCase(tableSptpd.getItem(countTbl-2).getText(3))){
									tempIndex = 0;
									Cell cellPokok = sheet.getCell(7, i-1);
									try {
										if (!(Double.parseDouble(cellPokok.getContents().replace(',', '.')) == indFormat.parse(tableSptpd.getItem(countTbl-2).getText(6)).doubleValue())){
											tableSptpd.getItem(countTbl-2).setBackground(SWTResourceManager.getColor(SWT.COLOR_YELLOW));
											countDiff++;
										}
										else
											tableSptpd.getItem(countTbl-2).setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
									} catch (ParseException e1) {
										MessageDialog.openError(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", e1.getMessage());
										e1.printStackTrace();
									}
									countTbl--;
								}else{
									if (tempIndex == 0){
										tempIndex = i;
										tempCountTbl = countTbl;
										tempCountDiff = countDiff;
									}
									if (countTbl == 3){
										NoKet += cellNo.getContents() + ", ";
										i = tempIndex;
										tempIndex--;
										countTbl = tempCountTbl;
										countDiff = tempCountDiff;
									}
									else{
										tableSptpd.getItem(countTbl-2).setBackground(SWTResourceManager.getColor(SWT.COLOR_RED));
										countDiff++;
										i++;
										countTbl--;
									}
								}
							}
							
							checkRedSPTPD(sheet);
							
							if (!NoKet.equalsIgnoreCase(", ") && !NoKet.trim().equalsIgnoreCase("")){
								MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Data sudah tidak ada dengan No Ketetapan " + NoKet.substring(0, NoKet.length() - 2));
								txtKet.setText(NoKet.substring(0, NoKet.length() - 2));
							}
							if (countDiff > 0)
								message = "Ada " + countDiff + " perubahan.";
							else
								message = "Tidak ada perubahan.";
							MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Data check telah selesai. " + message);
//						}
						workbook.close();
					} catch (BiffException e1) {
						e1.printStackTrace();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		new Label(compositeSPTPD, SWT.NONE);
		
		tableViewerSptpd = new TableViewer(compositeSPTPD, SWT.BORDER | SWT.FULL_SELECTION);
		tableSptpd = tableViewerSptpd.getTable();
		tableSptpd.setLinesVisible(true);
		tableSptpd.setHeaderVisible(true);
		tableSptpd.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));
		tableSptpd.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		tableSptpd.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		tableSptpd.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		Menu menu = new Menu(tableSptpd);
		MenuItem menuItem = new MenuItem(menu, SWT.DROP_DOWN);
		menuItem.setText("Details..");
		menuItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String tglKetetapan = tableSptpd.getItem(tableSptpd.getSelectionIndex()).getText(2);
				String noRekening = tableSptpd.getItem(tableSptpd.getSelectionIndex()).getText(3);
				String ass = tableSptpd.getItem(tableSptpd.getSelectionIndex()).getText(1);
				if(ass.contains("SPTPD"))
					ass = "Self";
				else
					ass = "Official";
				ResultSet result = ControllerFactory.getMainController().getCpLaporanDAOImpl().getDetailSPTPD(tglKetetapan,noRekening,ass);
				DetailKeuanganDialog detail = new DetailKeuanganDialog(Display.getCurrent().getActiveShell());
				detail.setResult(result);
				detail.open();
			}
		});
		tableSptpd.setMenu(menu);
		
		TabItem tbtmSkpdkb = new TabItem(tabFolder, SWT.NONE);
		tbtmSkpdkb.setText("SKPDKB");
		
		Composite compositeSKPDKB = new Composite(tabFolder, SWT.NONE);
		tbtmSkpdkb.setControl(compositeSKPDKB);
		compositeSKPDKB.setLayout(new GridLayout(3, false));
		
		Label lblKewajibanPajakSkpdkb = new Label(compositeSKPDKB, SWT.NONE);
		lblKewajibanPajakSkpdkb.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblKewajibanPajakSkpdkb.setText("Kewajiban Pajak");
		lblKewajibanPajakSkpdkb.setForeground(fontColor);
		lblKewajibanPajakSkpdkb.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		cmbPajakSkpdkb = new Combo(compositeSKPDKB, SWT.READ_ONLY);
		cmbPajakSkpdkb.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				UIController.INSTANCE.loadBidangUsaha(cmbSubPajakSkpdkb, BidangUsahaProvider.INSTANCE.getBidangUsaha((Integer)cmbPajakSkpdkb.getData(Integer.toString(cmbPajakSkpdkb.getSelectionIndex()))).toArray());
			}
		});
		GridData gd_cmbPajakSkpdkb = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_cmbPajakSkpdkb.widthHint = 250;
		cmbPajakSkpdkb.setLayoutData(gd_cmbPajakSkpdkb);
		cmbPajakSkpdkb.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		cmbPajakSkpdkb.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		cmbPajakSkpdkb.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		UIController.INSTANCE.loadPajak(cmbPajakSkpdkb, PajakProvider.INSTANCE.getPajak().toArray());
		new Label(compositeSKPDKB, SWT.NONE);
		new Label(compositeSKPDKB, SWT.NONE);
		
		cmbSubPajakSkpdkb = new Combo(compositeSKPDKB, SWT.READ_ONLY);
		GridData gd_cmbSubPajakSkpdkb = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_cmbSubPajakSkpdkb.widthHint = 250;
		cmbSubPajakSkpdkb.setLayoutData(gd_cmbSubPajakSkpdkb);
		cmbSubPajakSkpdkb.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		cmbSubPajakSkpdkb.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		cmbSubPajakSkpdkb.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		new Label(compositeSKPDKB, SWT.NONE);
		
		Label label = new Label(compositeSKPDKB, SWT.NONE);
		label.setText("Tanggal Terbit");
		label.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		label.setForeground(fontColor);
		
		Group group_1 = new Group(compositeSKPDKB, SWT.NONE);
		GridData gd_group_1 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_group_1.widthHint = 348;
		gd_group_1.heightHint = 59;
		group_1.setLayoutData(gd_group_1);
		group_1.setLayout(new GridLayout(4, false));
		
		btnTanggalSKPDKB = new Button(group_1, SWT.RADIO);
		btnTanggalSKPDKB.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				calSKPDKB.setVisible(true);
				calSKPDKB.pack(true);
				monthSKPDKB.setVisible(false);
				yearSKPDKB.setVisible(false);
				calSampaiSKPDKB.setVisible(false);
				setMasaPajakSKPDKB();
			}
		});
		btnTanggalSKPDKB.setText("Tanggal");
		btnTanggalSKPDKB.setSelection(false);
		btnTanggalSKPDKB.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BORDER));
		btnTanggalSKPDKB.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		btnBulanSKPDKB = new Button(group_1, SWT.RADIO);
		btnBulanSKPDKB.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				calSKPDKB.setVisible(false);
				monthSKPDKB.setVisible(true);
				monthSKPDKB.pack(true);
				yearSKPDKB.setVisible(false);
				calSampaiSKPDKB.setVisible(false);
				setMasaPajakSKPDKB();
			}
		});
		btnBulanSKPDKB.setLayoutData(new GridData(SWT.LEFT, SWT.BOTTOM, false, false, 1, 1));
		btnBulanSKPDKB.setText("Bulan");
		btnBulanSKPDKB.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BORDER));
		btnBulanSKPDKB.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		btnTahunSKPDKB = new Button(group_1, SWT.RADIO);
		btnTahunSKPDKB.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				calSKPDKB.setVisible(false);
				monthSKPDKB.setVisible(false);
				yearSKPDKB.setVisible(true);
				yearSKPDKB.pack(true);
				calSampaiSKPDKB.setVisible(false);
				setMasaPajakSKPDKB();
			}
		});
		btnTahunSKPDKB.setText("Tahun");
		btnTahunSKPDKB.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BORDER));
		btnTahunSKPDKB.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		btnPilihanSKPDKB = new Button(group_1, SWT.RADIO);
		btnPilihanSKPDKB.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				calSKPDKB.setVisible(true);
				calSKPDKB.pack(true);
				calSampaiSKPDKB.setVisible(true);
				monthSKPDKB.setVisible(false);
				yearSKPDKB.setVisible(false);
				setMasaPajakSKPDKB();
			}
		});
		btnPilihanSKPDKB.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BORDER));
		btnPilihanSKPDKB.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnPilihanSKPDKB.setText("Pilihan");
		
		Composite composite_2 = new Composite(group_1, SWT.NONE);
		composite_2.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 3, 1));
		composite_2.setLayout(new StackLayout());
		
		calSKPDKB = new DateTime(composite_2, SWT.BORDER | SWT.DROP_DOWN);
		calSKPDKB.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setMasaPajakSKPDKB();
			}
		});
		calSKPDKB.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BORDER));
		calSKPDKB.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		calSKPDKB.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		
		yearSKPDKB = new Spinner(composite_2, SWT.BORDER);
		yearSKPDKB.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setMasaPajakSKPDKB();
			}
		});
		yearSKPDKB.setVisible(false);
		yearSKPDKB.setValues(dateNow.getYear() + 1900, 2006, dateNow.getYear() + 1900, 0, 1, 10);
		yearSKPDKB.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		yearSKPDKB.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		
		monthSKPDKB = new DateTime(composite_2, SWT.BORDER | SWT.DROP_DOWN | SWT.SHORT);
		monthSKPDKB.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setMasaPajakSKPDKB();
			}
		});
		monthSKPDKB.setVisible(false);
		monthSKPDKB.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		monthSKPDKB.setDay(1);
		monthSKPDKB.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		
		calSampaiSKPDKB = new DateTime(group_1, SWT.BORDER | SWT.DROP_DOWN);
		calSampaiSKPDKB.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setMasaPajakSKPDKB();
			}
		});
		calSampaiSKPDKB.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BORDER));
		calSampaiSKPDKB.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		calSampaiSKPDKB.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		calSampaiSKPDKB.setVisible(false);
		new Label(group_1, SWT.NONE);
		new Label(group_1, SWT.NONE);
		new Label(group_1, SWT.NONE);
		new Label(group_1, SWT.NONE);
		new Label(compositeSKPDKB, SWT.NONE);
		new Label(compositeSKPDKB, SWT.NONE);
		
		Composite compButtonSkpdkb = new Composite(compositeSKPDKB, SWT.NONE);
		GridData gd_compButtonSkpdkb = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_compButtonSkpdkb.widthHint = 394;
		compButtonSkpdkb.setLayoutData(gd_compButtonSkpdkb);
		compButtonSkpdkb.setLayout(new GridLayout(4, false));
		
		Button btnLihatSkpdkb = new Button(compButtonSkpdkb, SWT.NONE);
		btnLihatSkpdkb.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				tableSkpdkb.removeAll();
				ResultSet resultSKPDKB = null;
				txtKetSKPDKB.setText("");
				if (cmbPajakSkpdkb.getText().equalsIgnoreCase(""))
					resultSKPDKB = ControllerFactory.getMainController().getCpLaporanDAOImpl().GetListSKPDKBbySKP(masaPajakDari, masaPajakSampai, "", "");
				else if (!cmbPajakSkpdkb.getText().equalsIgnoreCase("") && cmbSubPajakSkpdkb.getText().equalsIgnoreCase(""))
					resultSKPDKB = ControllerFactory.getMainController().getCpLaporanDAOImpl().GetListSKPDKBbySKP(masaPajakDari, masaPajakSampai, cmbPajakSkpdkb.getData(Integer.toString(cmbPajakSkpdkb.getSelectionIndex())).toString(),"");
				else
					resultSKPDKB = ControllerFactory.getMainController().getCpLaporanDAOImpl().GetListSKPDKBbySKP(masaPajakDari, masaPajakSampai, cmbPajakSkpdkb.getData(Integer.toString(cmbPajakSkpdkb.getSelectionIndex())).toString(), cmbSubPajakSkpdkb.getData(Integer.toString(cmbSubPajakSkpdkb.getSelectionIndex())).toString());
				if (tableSkpdkb.getColumnCount() <= 0)
					createColumn("skpdkb");
				loadData(resultSKPDKB, "skpdkb");
			}
		});
		GridData gd_btnLihatSkpdkb = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnLihatSkpdkb.widthHint = 90;
		btnLihatSkpdkb.setLayoutData(gd_btnLihatSkpdkb);
		gd_btnLihat.widthHint = 90;
		btnLihatSkpdkb.setText("Lihat");
		btnLihatSkpdkb.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		Button btnReklame = new Button(compButtonSptpd, SWT.NONE);
		btnReklame.setText("Reklame");
		GridData gd_btnReklame = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnReklame.widthHint = 90;
		btnReklame.setLayoutData(gd_btnReklame);
		btnReklame.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnReklame.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String bulan = "";
				String masa = "";
				ReportAppContext.name = "Variable";
				ReportAppContext.nameObject = "ReklamePendapatanDimuka";
				ReportAppContext.classLoader = LaporanKeuanganView.class.getClassLoader();
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				SimpleDateFormat defSdf = new SimpleDateFormat("yyyy-MM-dd");
				/*Date tgl = new Date(calSKP.getYear() - 1900, calSKP.getMonth(), calSKP.getDay());
				if (btnTanggal.getSelection())
					bulan = "Tanggal";
				else if (btnBulan.getSelection())
					bulan = "Bulan";
				else if (btnTahun.getSelection())
					bulan = "Tahun";
				
				if (bulan == "Tanggal")
					masa = sdf.format(tgl);
				else if (bulan == "Bulan")
					masa = UIController.INSTANCE.formatMonth(monthSKP.getMonth() + 1, Locale.getDefault()) + " " + monthSKP.getYear();
				else if (bulan == "Tahun")
					masa = String.valueOf(yearSKP.getText());*/
				
				if(!ReportAppContext.object.isEmpty())
					ReportAppContext.object.clear();
				
				ResultSet result = ControllerFactory.getMainController().getCpLaporanDAOImpl().getListReklamePendapatanDimuka(yearSPT.getSelection());
				Integer row = 0;
				try {
					while(result.next()){
						List<String> values = new ArrayList<String>();
						values.add(result.getString("NAMA_BADAN"));
						values.add(sdf.format(defSdf.parse(result.getString("TGL_SPTPD"))));
						String[] masaPajak = result.getString("MASA_PAJAK").split(":");
						String repMasaPajak = sdf.format(defSdf.parse(masaPajak[0]))+" - "+sdf.format(defSdf.parse(masaPajak[1]));
						values.add(repMasaPajak);
						values.add(sdf.format(defSdf.parse(result.getString("TGL_SSPD"))));
						values.add(indFormat.format(result.getDouble("PAJAK_TERUTANG")));
						values.add(indFormat.format(result.getDouble("PENDAPATAN_BULAN")));
						Integer bulanPendLO = 0,bulanPendMuka=0;
						if(defSdf.parse(masaPajak[0]).getDate()<=15)
							bulanPendLO = 12-defSdf.parse(masaPajak[0]).getMonth();
						else
							bulanPendLO = 12-defSdf.parse(masaPajak[0]).getMonth()-1;
						bulanPendMuka = 12-bulanPendLO;
						values.add(Integer.toString(bulanPendLO));
						values.add(indFormat.format(result.getDouble("PENDAPATAN_BULAN")*bulanPendLO));
						values.add(Integer.toString(bulanPendMuka));
						values.add(indFormat.format(result.getDouble("PENDAPATAN_BULAN")*bulanPendMuka));
						
						ReportAppContext.object.put(Integer.toString(row), values);
						row++;
					}
				} catch (SQLException | ParseException e1) {
					e1.printStackTrace();
				}
//				Integer count = 0;
				/*for(int j=0;j<tblPendapatan.getItems().length; j++){
					List<String> values = new ArrayList<String>();
					for(int i=0;i<tblPendapatan.getColumnCount();i++){// untuk masukkan data berdasarkan colom table
						if(i==9||i==10||i==11||i==14||i==15||i==16)// untuk colom yang berhubungan dengan currency
							if(tblPendapatan.getItem(j).getText(i).length()>2)
								values.add(tblPendapatan.getItem(j).getText(i).substring(2).replace(".", "").replace(",", "."));
							else
								values.add(tblPendapatan.getItem(j).getText(i));
						else
							values.add(tblPendapatan.getItem(j).getText(i));
					}
					ReportAppContext.object.put(Integer.toString(j), values);
//					count++;
				}*/
				/*if (!cmbKewajibanPajak.getText().equalsIgnoreCase(""))
					ReportAppContext.map.put("Pajak", cmbKewajibanPajak.getText());	
				else
					ReportAppContext.map.put("Pajak", "Daerah");
				ReportAppContext.map.put("Bulan", bulan);
				ReportAppContext.map.put("Masa", masa);*/
				
				ReportAction.start("ReklamePendapatanDimuka.rptdesign");
			}
		});
		
		Button btnCetakSkpdkb = new Button(compButtonSkpdkb, SWT.NONE);
		GridData gd_btnCetakSkpdkb = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnCetakSkpdkb.widthHint = 90;
		btnCetakSkpdkb.setLayoutData(gd_btnCetakSkpdkb);
		gd_btnCetakSkpdkb.widthHint = 90;
		
		txtKet = new Text(compositeSPTPD, SWT.BORDER);
		txtKet.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				if (txtKet.getText().trim().length() > 0)
					btnCetakNoSPT.setEnabled(true);
				else
					btnCetakNoSPT.setEnabled(false);
			}
		});
		txtKet.setEditable(false);
		txtKet.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		
		btnCetakNoSPT = new Button(compositeSPTPD, SWT.NONE);
		btnCetakNoSPT.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ReportAppContext.name = "Variable";
				ReportAppContext.map.put("title", "Daftar No SKP " + cmbPajakSptpd.getText() + " yang sudah tidak ada");
				ReportAppContext.nameObject = "DaftarNoSKP";
				ReportAppContext.classLoader = LaporanKeuanganView.class.getClassLoader();
				if(!ReportAppContext.object.isEmpty())
					ReportAppContext.object.clear();
				
				String[] noSKP = txtKet.getText().split(",");
				for(int j = 0;j<noSKP.length;j++){
					List<String> values = new ArrayList<String>();
					values.add(noSKP[j]);
					ReportAppContext.object.put(Integer.toString(j), values);
				}
				
				ReportAction.start("DaftarSKPHapus.rptdesign");
			}
		});
		btnCetakNoSPT.setEnabled(false);
		GridData gd_btnCetakNoSPT = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnCetakNoSPT.widthHint = 90;
		btnCetakNoSPT.setLayoutData(gd_btnCetakNoSPT);
		btnCetakNoSPT.setText("Cetak");
		btnCetakSkpdkb.setText("Cetak");
		btnCetakSkpdkb.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		Button btnCheckSkpdkb = new Button(compButtonSkpdkb, SWT.NONE);
		GridData gd_btnCheckSkpdkb = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnCheckSkpdkb.widthHint = 90;
		btnCheckSkpdkb.setLayoutData(gd_btnCheckSkpdkb);
		btnCheckSkpdkb.setText("Check");
		btnCheckSkpdkb.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		Button btnCheckDenda = new Button(compButtonSkpdkb, SWT.NONE);
		btnCheckDenda.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog fd = new FileDialog(Display.getCurrent().getActiveShell(), SWT.NONE);
				String filename = fd.open();
				if(filename != null){
					Workbook workbook;
					try {
						int tempIndex = 0;
						int tempCountTbl = 0;
						int countDiff = 0;
						int tempCountDiff = 0;
						NoKet = "";
						String message = "";
						workbook = Workbook.getWorkbook(new File(filename));
						Sheet sheet = workbook.getSheet(0);
						Cell totalCell = sheet.findCell("TOTAL");
						int countTbl = tableSkpdkb.getItemCount();
//						String totalString = sheet.getCell(totalCell.getColumn()+1, totalCell.getRow()).getContents();
//						String totalDenda = sheet.getCell(totalCell.getColumn()+2, totalCell.getRow()).getContents();
//						if(totalString.split("]")[1].equalsIgnoreCase(tableSkpdkb.getItem(tableSkpdkb.getItemCount()-1).getText(4).substring(2)) ||
//							totalDenda.split("]")[1].equalsIgnoreCase(tableSkpdkb.getItem(tableSkpdkb.getItemCount()-1).getText(5).substring(2))){
//							MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "Perbandingan Data", "Tidak ada data baru.");
//						}else{
							for (int i=totalCell.getRow();i>4;i--){
								Cell cellNo = sheet.getCell(4, i-1);
								if (cellNo.getContents().equalsIgnoreCase(tableSkpdkb.getItem(countTbl-2).getText(3))){
									tempIndex = 0;
									Cell cellPokok = sheet.getCell(7, i-1);
									Cell cellDenda = sheet.getCell(7, i-1);
									try {
										if (!(Double.parseDouble(cellDenda.getContents().replace(',', '.')) == indFormat.parse(tableSkpdkb.getItem(countTbl-2).getText(7)).doubleValue())){
											tableSkpdkb.getItem(countTbl-2).setBackground(SWTResourceManager.getColor(SWT.COLOR_YELLOW));
											countDiff++;
										}
										else
											tableSkpdkb.getItem(countTbl-2).setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
									} catch (ParseException e1) {
										MessageDialog.openError(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", e1.getMessage());
										e1.printStackTrace();
									}
									countTbl--;
								}else{
									if (tempIndex == 0){
										tempIndex = i;
										tempCountTbl = countTbl;
										tempCountDiff = countDiff;
									}
									if (countTbl == 3){
										NoKet += cellNo.getContents() + ", ";
										i = tempIndex;
										tempIndex--;
										countTbl = tempCountTbl;
										countDiff = tempCountDiff;
									}
									else{
										tableSkpdkb.getItem(countTbl-2).setBackground(SWTResourceManager.getColor(SWT.COLOR_RED));
										countDiff++;
										i++;
										countTbl--;
									}
								}
							}
							checkRedSKPDKB(sheet);
							if (!NoKet.equalsIgnoreCase(", ") && !NoKet.equalsIgnoreCase("")){
								MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Data sudah tidak ada dengan No Ketetapan " + NoKet.substring(0, NoKet.length() - 2));
								txtKetSKPDKB.setText(NoKet.substring(0, NoKet.length() - 2));
							}
							if (countDiff > 0)
								message = "Ada " + countDiff + " perubahan.";
							else
								message = "Tidak ada perubahan.";
							MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Data check telah selesai. " + message);
//						}
						workbook.close();
					} catch (BiffException e1) {
						e1.printStackTrace();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		btnCheckDenda.setText("Check Denda");
		btnCheckSkpdkb.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog fd = new FileDialog(Display.getCurrent().getActiveShell(), SWT.NONE);
				String filename = fd.open();
				if(filename != null){
					Workbook workbook;
					try {
						int tempIndex = 0;
						int tempCountTbl = 0;
						int countDiff = 0;
						int tempCountDiff = 0;
						NoKet = "";
						String message = "";
						workbook = Workbook.getWorkbook(new File(filename));
						Sheet sheet = workbook.getSheet(0);
						Cell totalCell = sheet.findCell("TOTAL");
						int countTbl = tableSkpdkb.getItemCount();
//						String totalString = sheet.getCell(totalCell.getColumn()+1, totalCell.getRow()).getContents();
//						String totalDenda = sheet.getCell(totalCell.getColumn()+2, totalCell.getRow()).getContents();
//						if(totalString.split("]")[1].equalsIgnoreCase(tableSkpdkb.getItem(tableSkpdkb.getItemCount()-1).getText(4).substring(2)) ||
//							totalDenda.split("]")[1].equalsIgnoreCase(tableSkpdkb.getItem(tableSkpdkb.getItemCount()-1).getText(5).substring(2))){
//							MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "Perbandingan Data", "Tidak ada data baru.");
//						}else{
							for (int i=totalCell.getRow();i>4;i--){
								Cell cellNo = sheet.getCell(4, i-1);
								if (cellNo.getContents().equalsIgnoreCase(tableSkpdkb.getItem(countTbl-2).getText(3))){
									tempIndex = 0;
									Cell cellPokok = sheet.getCell(7, i-1);
									Cell cellDenda = sheet.getCell(8, i-1);
									try {
										if (!(Double.parseDouble(cellPokok.getContents().replace(',', '.')) == indFormat.parse(tableSkpdkb.getItem(countTbl-2).getText(6)).doubleValue())){
//												||
//											!(Double.parseDouble(cellDenda.getContents().replace(',', '.')) == indFormat.parse(tableSkpdkb.getItem(countTbl-2).getText(7)).doubleValue())){
											tableSkpdkb.getItem(countTbl-2).setBackground(SWTResourceManager.getColor(SWT.COLOR_YELLOW));
											countDiff++;
										}
										else
											tableSkpdkb.getItem(countTbl-2).setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
									} catch (ParseException e1) {
										MessageDialog.openError(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", e1.getMessage());
										e1.printStackTrace();
									}
									countTbl--;
								}else{
									if (tempIndex == 0){
										tempIndex = i;
										tempCountTbl = countTbl;
										tempCountDiff = countDiff;
									}
									if (countTbl == 2){
										NoKet += cellNo.getContents() + ", ";
										i = tempIndex;
										tempIndex--;
										countTbl = tempCountTbl;
										countDiff = tempCountDiff;
									}
									else{
										tableSkpdkb.getItem(countTbl-2).setBackground(SWTResourceManager.getColor(SWT.COLOR_RED));
										countDiff++;
										i++;
										countTbl--;
									}
								}
							}
							if (!NoKet.equalsIgnoreCase(", ") && !NoKet.equalsIgnoreCase("")){
								MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Data sudah tidak ada dengan No Ketetapan " + NoKet.substring(0, NoKet.length() - 2));
								txtKetSKPDKB.setText(NoKet.substring(0, NoKet.length() - 2));
							}
							if (countDiff > 0)
								message = "Ada " + countDiff + " perubahan.";
							else
								message = "Tidak ada perubahan.";
							MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Data check telah selesai. " + message);
//						}
						workbook.close();
					} catch (BiffException e1) {
						e1.printStackTrace();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		btnCetakSkpdkb.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(isPrint()){
					String namaPajakSKPDKB = cmbPajakSkpdkb.getText();
					if (btnTanggalSKPDKB.getSelection())
						namaPajakSKPDKB += " " + masaPajak;
					else if (btnBulanSKPDKB.getSelection())
						namaPajakSKPDKB += " Bulan " + masaPajak;
					else if (btnTahunSKPDKB.getSelection())
						namaPajakSKPDKB += " Tahun " + masaPajak;
					ReportAppContext.name = "Variable";
					ReportAppContext.map.put("NamaPajak", namaPajakSKPDKB);
					ReportAppContext.nameObject = "DaftarKeuangan";
					ReportAppContext.classLoader = LaporanKeuanganView.class.getClassLoader();
					if(!ReportAppContext.object.isEmpty())
						ReportAppContext.object.clear();
					for(int j = 0;j<tableSkpdkb.getItems().length-1;j++){
						List<String> values = new ArrayList<String>();
						for(int i=1;i<tableSkpdkb.getColumnCount();i++){
							if(tableSkpdkb.getItem(j).getText(i).startsWith("Rp"))
								values.add(tableSkpdkb.getItem(j).getText(i).substring(2).replace(".", ""));
							else
								values.add(tableSkpdkb.getItem(j).getText(i));
						}
						if(tableSkpdkb.getItem(j).getBackground().equals(SWTResourceManager.getColor(SWT.COLOR_RED)))
							values.add("Red");
						else if(tableSkpdkb.getItem(j).getBackground().equals(SWTResourceManager.getColor(SWT.COLOR_YELLOW)))
							values.add("Yellow");
						else
							values.add("");
						ReportAppContext.object.put(Integer.toString(j), values);
					}
					ReportAppContext.map.put("TotalPokok", tableSkpdkb.getItem(tableSkpdkb.getItemCount()-1).getText(6));
					ReportAppContext.map.put("TotalDenda", tableSkpdkb.getItem(tableSkpdkb.getItemCount()-1).getText(7));
					
					ReportAction.start("DaftarKeuangan.rptdesign");
				}else
					MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Anda tidak memiliki hak untuk mencetak.");
			}
		});
		new Label(compositeSKPDKB, SWT.NONE);
		
		tableViewerSkpdkb = new TableViewer(compositeSKPDKB, SWT.BORDER | SWT.FULL_SELECTION);
		tableSkpdkb = tableViewerSkpdkb.getTable();
		tableSkpdkb.setLinesVisible(true);
		tableSkpdkb.setHeaderVisible(true);
		tableSkpdkb.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));
		tableSkpdkb.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		tableSkpdkb.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		tableSkpdkb.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		txtKetSKPDKB = new Text(compositeSKPDKB, SWT.BORDER);
		txtKetSKPDKB.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				if (txtKetSKPDKB.getText().trim().length() > 0)
					btnCetakNoSKP.setEnabled(true);
				else
					btnCetakNoSKP.setEnabled(false);
			}
		});
		txtKetSKPDKB.setEditable(false);
		txtKetSKPDKB.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		
		btnCetakNoSKP = new Button(compositeSKPDKB, SWT.NONE);
		btnCetakNoSKP.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ReportAppContext.name = "Variable";
				ReportAppContext.map.put("title", "Daftar No SKP " + cmbPajakSkpdkb.getText() + " yang sudah tidak ada");
				ReportAppContext.nameObject = "DaftarNoSKP";
				ReportAppContext.classLoader = LaporanKeuanganView.class.getClassLoader();
				if(!ReportAppContext.object.isEmpty())
					ReportAppContext.object.clear();
				
				String[] noSKP = txtKetSKPDKB.getText().split(",");
				for(int j = 0;j<noSKP.length;j++){
					List<String> values = new ArrayList<String>();
					values.add(noSKP[j]);
					ReportAppContext.object.put(Integer.toString(j), values);
				}
				
				ReportAction.start("DaftarSKPHapus.rptdesign");
			}
		});
		GridData gd_btnCetakNoSKP = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnCetakNoSKP.widthHint = 90;
		btnCetakNoSKP.setLayoutData(gd_btnCetakNoSKP);
		btnCetakNoSKP.setEnabled(false);
		btnCetakNoSKP.setText("Cetak");

		scrolledComposite.setContent(composite);
		scrolledComposite.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}
	
	private void createColumn(String tipe){
		String[] listColumn = {"No.","NPWPD","Nama Badan","No Ketetapan", "Tgl Ketetapan", "No Rekening", "Pokok Pajak", "Denda", "Keterangan", "Tanggal SPTPD"};
		int[] columnWidth = {40, 110, 200, 210, 120, 120, 170, 150, 250, 0};
		for (int i=listColumn.length - 1;i>=0;i--)
		{
			if(tipe.equalsIgnoreCase("sptpd")){
				TableColumn colPajak = new TableColumn(tableSptpd, SWT.NONE,0);
				colPajak.setText(listColumn[i]);
				colPajak.setWidth(columnWidth[i]);
			}else{
				TableColumn colPajak = new TableColumn(tableSkpdkb, SWT.NONE,0);
				colPajak.setText(listColumn[i]);
				colPajak.setWidth(columnWidth[i]);
			}
		}
	}
	
	private void loadData(ResultSet result, String tipe){
		try
		{
			int count = 1;
			double totalPokokPajak = 0;
			double totalDenda = 0;
			double grandTotal = 0;
			TableItem item;
			DecimalFormat df = new DecimalFormat("#.##");
			df.setMaximumFractionDigits(2);
			df.setRoundingMode(RoundingMode.HALF_EVEN);
			while (result.next())
			{
				String strPokokPajak = df.format(result.getDouble("PokokPajak")).replace(',', '.');
				String strDenda = df.format(result.getDouble("Denda")).replace(',', '.');
				if (tipe.equalsIgnoreCase("sptpd"))
					item = new TableItem(tableSptpd, SWT.NONE);
				else
					item = new TableItem(tableSkpdkb, SWT.NONE);
				item.setText(0, count+".");
				item.setText(1, result.getString("NPWPD"));
				item.setText(2, result.getString("Nama_Badan"));
				item.setText(3, result.getString("NoKetetapan"));
				item.setText(4, result.getString("TglKetetapan"));
				item.setText(5, result.getString("NoRekening"));
				item.setText(6, indFormat.format(Double.parseDouble(strPokokPajak)));
				item.setText(7, indFormat.format(Double.parseDouble(strDenda)));
				item.setText(8, result.getString("Keterangan"));
				if (tipe.equalsIgnoreCase("sptpd"))
					item.setText(9, result.getDate("Tgl_SPTPD").toString());
				else
					item.setText(9, result.getDate("Tanggal_SKP").toString());
				totalPokokPajak += Double.parseDouble(strPokokPajak);
				totalDenda += Double.parseDouble(strDenda);
				count++;
			}
			grandTotal = totalPokokPajak  + totalDenda;
			if (tipe.equalsIgnoreCase("sptpd"))
				item = new TableItem(tableSptpd, SWT.NONE);
			else
				item = new TableItem(tableSkpdkb, SWT.NONE);
			item.setText(1, "Grand Total");
			item.setText(6, indFormat.format(totalPokokPajak));
			item.setText(7, indFormat.format(totalDenda));
			item.setText(8, indFormat.format(grandTotal));
		} catch (SQLException e) {
				e.printStackTrace();
		}
	}
	
	@SuppressWarnings("deprecation")
	public void setMasaPajak()
	{
		if (btnTanggal.getSelection())
		{
			Calendar instance = Calendar.getInstance();
			instance.set(Calendar.DAY_OF_MONTH, calSPT.getDay());
			instance.set(Calendar.MONTH, calSPT.getMonth());
			instance.set(Calendar.YEAR, calSPT.getYear());
			masaPajakDari = new Date(calSPT.getYear() - 1900, calSPT.getMonth(), calSPT.getDay());
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
			instance.set(Calendar.DAY_OF_MONTH, calSPT.getDay());
			instance.set(Calendar.MONTH, calSPT.getMonth());
			instance.set(Calendar.YEAR, calSPT.getYear());
			masaPajakDari = new Date(calSPT.getYear() - 1900, calSPT.getMonth(), calSPT.getDay());
			masaPajakSampai = new Date(calSampai.getYear() - 1900, calSampai.getMonth(), calSampai.getDay());
			masaPajak = masaPajakDari.getDate() + " " + UIController.INSTANCE.formatMonth(masaPajakDari.getMonth() + 1, Locale.getDefault()) + " " + (masaPajakDari.getYear() + 1900);
		}
	}
	
	@SuppressWarnings("deprecation")
	public void setMasaPajakSKPDKB()
	{
		if (btnTanggalSKPDKB.getSelection())
		{
			Calendar instance = Calendar.getInstance();
			instance.set(Calendar.DAY_OF_MONTH, calSKPDKB.getDay());
			instance.set(Calendar.MONTH, calSKPDKB.getMonth());
			instance.set(Calendar.YEAR, calSKPDKB.getYear());
			masaPajakDari = new Date(calSKPDKB.getYear() - 1900, calSKPDKB.getMonth(), calSKPDKB.getDay());
			masaPajakSampai = masaPajakDari;
			masaPajak = masaPajakDari.getDate() + " " + UIController.INSTANCE.formatMonth(masaPajakDari.getMonth() + 1, Locale.getDefault()) + " " + (masaPajakDari.getYear() + 1900);
		}
		if (btnBulanSKPDKB.getSelection())
		{
			Calendar instance = Calendar.getInstance();
			instance.set(Calendar.DAY_OF_MONTH, monthSKPDKB.getDay());
			instance.set(Calendar.MONTH, monthSKPDKB.getMonth());
			instance.set(Calendar.YEAR, monthSKPDKB.getYear());
			masaPajakDari = new Date(monthSKPDKB.getYear() - 1900, monthSKPDKB.getMonth(), 1);
			masaPajakSampai = new Date(monthSKPDKB.getYear() - 1900, monthSKPDKB.getMonth(), instance.getActualMaximum(Calendar.DATE));
			masaPajak = UIController.INSTANCE.formatMonth(monthSKPDKB.getMonth() + 1, Locale.getDefault()) + " " + monthSKPDKB.getYear();
		}
		if (btnTahunSKPDKB.getSelection())
		{
			masaPajakDari = new Date(yearSKPDKB.getSelection() - 1900, 0, 1);
			masaPajakSampai = new Date(yearSKPDKB.getSelection() - 1900, 11, 31);
			masaPajak = Integer.toString(yearSKPDKB.getSelection());
		}
		if (btnPilihanSKPDKB.getSelection())
		{
			Calendar instance = Calendar.getInstance();
			instance.set(Calendar.DAY_OF_MONTH, calSKPDKB.getDay());
			instance.set(Calendar.MONTH, calSKPDKB.getMonth());
			instance.set(Calendar.YEAR, calSKPDKB.getYear());
			masaPajakDari = new Date(calSKPDKB.getYear() - 1900, calSKPDKB.getMonth(), calSKPDKB.getDay());
			masaPajakSampai = new Date(calSampaiSKPDKB.getYear() - 1900, calSampaiSKPDKB.getMonth(), calSampaiSKPDKB.getDay());
			masaPajak = masaPajakDari.getDate() + " " + UIController.INSTANCE.formatMonth(masaPajakDari.getMonth() + 1, Locale.getDefault()) + " " + (masaPajakDari.getYear() + 1900);
		}
	}
	
	private void checkRedSPTPD(Sheet s){
		Cell totalCell = s.findCell("TOTAL");
		for (int i=tableSptpd.getItemCount();i>1;i--){
			if (tableSptpd.getItem(i - 2).getBackground().equals(SWTResourceManager.getColor(SWT.COLOR_RED))){
				for (int j=totalCell.getRow();j>4;j--){
					Cell cellNo = s.getCell(4, j-1);
					if (cellNo.getContents().equalsIgnoreCase(tableSptpd.getItem(i-2).getText(3))){
						NoKet = NoKet.replace(cellNo.getContents() + ", ", "");
						Cell cellPokok = s.getCell(7, j-1);
						try {
							if (!(Double.parseDouble(cellPokok.getContents().replace(',', '.')) == indFormat.parse(tableSptpd.getItem(i-2).getText(6)).doubleValue())){
								tableSptpd.getItem(i-2).setBackground(SWTResourceManager.getColor(SWT.COLOR_YELLOW));
							}
							else
								tableSptpd.getItem(i-2).setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
						} catch (ParseException e1) {
							MessageDialog.openError(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", e1.getMessage());
							e1.printStackTrace();
						}
					}
				}
			}
		}
	}
	
	private void checkRedSKPDKB(Sheet s){
		Cell totalCell = s.findCell("TOTAL");
		for (int i=tableSkpdkb.getItemCount();i>1;i--){
			if (tableSkpdkb.getItem(i - 2).getBackground().equals(SWTResourceManager.getColor(SWT.COLOR_RED))){
				for (int j=totalCell.getRow();j>4;j--){
					Cell cellNo = s.getCell(4, j-1);
					if (cellNo.getContents().equalsIgnoreCase(tableSkpdkb.getItem(i-2).getText(3))){
						NoKet = NoKet.replace(cellNo.getContents() + ", ", "");
						Cell cellPokok = s.getCell(7, j-1);
						Cell cellDenda = s.getCell(8, j-1);
						try {
							if (!(Double.parseDouble(cellPokok.getContents().replace(',', '.')) == indFormat.parse(tableSkpdkb.getItem(i-2).getText(6)).doubleValue())){
//								||}
//									!(Double.parseDouble(cellDenda.getContents().replace(',', '.')) == indFormat.parse(tableSkpdkb.getItem(i-2).getText(7)).doubleValue())){
									tableSkpdkb.getItem(i-2).setBackground(SWTResourceManager.getColor(SWT.COLOR_YELLOW));
								}
								else
									tableSkpdkb.getItem(i-2).setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
						} catch (ParseException e1) {
							MessageDialog.openError(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", e1.getMessage());
							e1.printStackTrace();
						}
					}
				}
			}
		}
	}
	
	private UserModul userModul = ControllerFactory.getMainController().getCpUserDAOImpl()
			.getHMUserModul(GlobalVariable.userModul.getIdUser()).get(34);
	private Text txtKet;
	private Text txtKetSKPDKB;
	
	private boolean isPrint(){		
		return userModul.getCetak();
	}
}
