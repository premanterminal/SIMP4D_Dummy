package com.dispenda.skpdkb.views;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
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
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
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
import com.dispenda.skpdkb.table.RekomendasiPeriksaFilter;

public class RekomendasiPeriksaView extends ViewPart {
	public RekomendasiPeriksaView() {
	}

	public static final String ID = RekomendasiPeriksaView.class.getName();
	private Text txtKeyword;
	private Table tblRekomendasiPeriksa;
	private Combo cmbKewajibanPajak;
	private Button radioAscending;
	private Button radioDescending;
	private ResultSet resultRekomendasi;
	private Locale ind = new Locale("id", "ID");
	private NumberFormat indFormat = NumberFormat.getCurrencyInstance(ind);
	private Timestamp dateNow = (java.sql.Timestamp)ControllerFactory.getMainController().getCpWajibPajakDAOImpl().getDateNow();
	private TableViewer tableViewer;
	private Color fontColor = new Color(Display.getCurrent(),Preferences.FONT_COLOR);
	private List<RPObject> listObject = null;
	private RekomendasiPeriksaFilter filter = new RekomendasiPeriksaFilter();
	
	@Override
	public void createPartControl(Composite arg0) {
		GridData gd_grpRekomendasiPemeriksaan = new GridData(SWT.FILL, SWT.FILL, true, true, 5, 1);
		gd_grpRekomendasiPemeriksaan.heightHint = 341;
		arg0.setLayout(new GridLayout(7, false));
		
		Label lblKewajibanPajak = new Label(arg0, SWT.NONE);
		GridData gd_lblKewajibanPajak = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_lblKewajibanPajak.verticalIndent = 25;
		lblKewajibanPajak.setLayoutData(gd_lblKewajibanPajak);
		lblKewajibanPajak.setText("Kewajiban Pajak");
		lblKewajibanPajak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblKewajibanPajak.setForeground(fontColor);
		
		cmbKewajibanPajak = new Combo(arg0, SWT.READ_ONLY);
		cmbKewajibanPajak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		GridData gd_cmbKewajibanPajak = new GridData(SWT.FILL, SWT.CENTER, false, false, 4, 1);
		gd_cmbKewajibanPajak.verticalIndent = 25;
		cmbKewajibanPajak.setLayoutData(gd_cmbKewajibanPajak);
		cmbKewajibanPajak.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		cmbKewajibanPajak.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		UIController.INSTANCE.loadPajak(cmbKewajibanPajak, PajakProvider.INSTANCE.getPajak().toArray());
		
		Group grpOrderBy = new Group(arg0, SWT.NONE);
		grpOrderBy.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 2));
		grpOrderBy.setForeground(fontColor);
		grpOrderBy.setLayout(new GridLayout(1, false));
		grpOrderBy.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.BOLD));
		grpOrderBy.addPaintListener(new PaintListener() {
			@Override
			public void paintControl(PaintEvent pe) {
				pe.gc.setForeground(fontColor);
				pe.gc.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.BOLD));
				pe.gc.drawText("Order By", 5, 0, false);
			}
		});
		
		radioAscending = new Button(grpOrderBy, SWT.RADIO);
		radioAscending.setSelection(true);
		
		radioAscending.setForeground(fontColor);
		radioAscending.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		radioAscending.setText("Ascending");
		
		radioDescending = new Button(grpOrderBy, SWT.RADIO);
		radioDescending.setForeground(fontColor);
		radioDescending.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		radioDescending.setText("Descending");
		
		Group grpSearch = new Group(arg0, SWT.NONE);
		grpSearch.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 2));
		grpSearch.setLayout(new GridLayout(3, false));
		grpSearch.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.BOLD));
		grpSearch.addPaintListener(new PaintListener() {
			@Override
			public void paintControl(PaintEvent pe) {
				pe.gc.setForeground(fontColor);
				pe.gc.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.BOLD));
				pe.gc.drawText("Search", 5, 0, false);
			}
		});
		
		Label lblKeyword = new Label(grpSearch, SWT.NONE);
		lblKeyword.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblKeyword.setForeground(fontColor);
		lblKeyword.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		lblKeyword.setText("Keyword");
		
		txtKeyword = new Text(grpSearch, SWT.BORDER);
		txtKeyword.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent me) {
				Text text = (Text) me.widget;
				if(text.getText().equalsIgnoreCase("")){
					filter.setSearchText("");
					tableViewer.refresh();
				}
			}
		});
		txtKeyword.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.keyCode == SWT.CR || e.keyCode==SWT.KEYPAD_CR){
					filter.setSearchText(txtKeyword.getText());
					tableViewer.refresh();
				}
			}
		});
		txtKeyword.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtKeyword.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		txtKeyword.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		GridData gd_txtKeyword = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_txtKeyword.widthHint = 150;
		txtKeyword.setLayoutData(gd_txtKeyword);
		new Label(grpSearch, SWT.NONE);
		new Label(grpSearch, SWT.NONE);
		
		Button btnCari = new Button(grpSearch, SWT.NONE);
		btnCari.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				filter.setSearchText(txtKeyword.getText());
				tableViewer.refresh();
			}
		});
		GridData gd_btnCari = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_btnCari.widthHint = 90;
		btnCari.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnCari.setLayoutData(gd_btnCari);
		btnCari.setText("Cari");
		new Label(arg0, SWT.NONE);
		
		Button btnTampil = new Button(arg0, SWT.NONE);
		GridData gd_btnTampil = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnTampil.widthHint = 90;
		btnTampil.setLayoutData(gd_btnTampil);
		btnTampil.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnTampil.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (cmbKewajibanPajak.getText().equalsIgnoreCase("")){
					MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Pilih Kewajiban Pajak terlebih dulu");
				}
				else{
					String kodePajak = "'%%'";
					String varOrder = "";
					tblRekomendasiPeriksa.removeAll();
					if (radioAscending.getSelection())
						varOrder = "ASC";
					else
						varOrder = "DESC";
					
					kodePajak = cmbKewajibanPajak.getData(Integer.toString(cmbKewajibanPajak.getSelectionIndex())).toString();
					resultRekomendasi = ControllerFactory.getMainController().getCpPeriksaDAOImpl().getRekomendasiPeriksa(kodePajak, varOrder);
					loadTable();
					tableViewer.setInput(listObject);
					tableViewer.refresh();
				}
			}
		});
		btnTampil.setText("Tampil");
		
		Button btnCetak = new Button(arg0, SWT.NONE);
		GridData gd_btnCetak = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnCetak.widthHint = 90;
		btnCetak.setLayoutData(gd_btnCetak);
		btnCetak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnCetak.addSelectionListener(new SelectionAdapter() {
			@SuppressWarnings("deprecation")
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(isPrint()){
					ReportAppContext.name = "Variable";
					ReportAppContext.map.put("KodePajak", "Kewajiban Pajak: " + cmbKewajibanPajak.getText());
					ReportAppContext.map.put("Bulan", UIController.INSTANCE.formatMonth(dateNow.getMonth() + 1, Locale.getDefault()) + " " + (dateNow.getYear() + 1900));
					ReportAppContext.nameObject = "RekomendasiPeriksa";
					ReportAppContext.classLoader = RekomendasiPeriksaView.class.getClassLoader();
					if(!ReportAppContext.object.isEmpty())
						ReportAppContext.object.clear();
					Integer count = 0;
					for(int ti=0;ti<tblRekomendasiPeriksa.getItems().length; ti++){
						List<String> values = new ArrayList<String>();
						for(int i=0;i<tblRekomendasiPeriksa.getColumnCount();i++){
							if (i == 6)
								values.add(tblRekomendasiPeriksa.getItem(ti).getText(i).substring(2).replace(".", "").replace(",", "."));
							else
								values.add(tblRekomendasiPeriksa.getItem(ti).getText(i));
						}
						ReportAppContext.object.put(count.toString(), values);
						count++;
					}
					ReportAction.start("RekomendasiPemeriksaan.rptdesign");
				}else
					MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Anda tidak memiliki hak untuk mencetak.");
			}
		});
		btnCetak.setText("Cetak");
		new Label(arg0, SWT.NONE);
		new Label(arg0, SWT.NONE);
		
		Group group = new Group(arg0, SWT.NONE);
		group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 7, 1));
		group.setForeground(fontColor);
		group.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.BOLD));
		group.setLayout(new GridLayout(1, false));
		
		tableViewer = new TableViewer(group, SWT.BORDER | SWT.FULL_SELECTION | SWT.VIRTUAL);
		String[] listColumn = {"NPWPD", "Nama Badan", "Alamat Badan", "Nama Pemilik", "Masa Pajak Dari", "Masa Pajak Sampai", 
				"Pajak Periksa", "Bulan Daftar", "Status", "SPT Terakhir"};
		int[] columnWidth = {100, 230, 230, 130, 120, 130, 120, 120, 55, 90};
		TableViewerColumn tableViewerColumn = createTableViewerColumn(listColumn[0], columnWidth[0], 0);
		tableViewerColumn.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				RPObject antri = (RPObject) element;
				return antri.getNpwp();
			}
		});

		TableViewerColumn tableViewerColumn_1 = createTableViewerColumn(listColumn[1], columnWidth[1], 1);
		tableViewerColumn_1.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				RPObject antri = (RPObject) element;
				return antri.getNamaBadan();
			}
		});
		
		TableViewerColumn tableViewerColumn_2 = createTableViewerColumn(listColumn[2], columnWidth[2], 2);
		tableViewerColumn_2.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				RPObject antri = (RPObject) element;
				return antri.getAlamatBadan();
			}
		});
		
		TableViewerColumn tableViewerColumn_4 = createTableViewerColumn(listColumn[3], columnWidth[3], 3);
		tableViewerColumn_4.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				RPObject antri = (RPObject) element;
				return antri.getNamaPemilik();
			}
		});
		
		TableViewerColumn tableViewerColumn_5 = createTableViewerColumn(listColumn[4], columnWidth[4], 4);
		tableViewerColumn_5.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				RPObject antri = (RPObject) element;
				return antri.getMasaPajakDari();
			}
		});
		
		TableViewerColumn tableViewerColumn_6 = createTableViewerColumn(listColumn[5], columnWidth[5], 5);
		tableViewerColumn_6.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				RPObject antri = (RPObject) element;
				return antri.getMasaPajakSampai();
			}
		});
		
		TableViewerColumn tableViewerColumn_7 = createTableViewerColumn(listColumn[6], columnWidth[6], 6);
		tableViewerColumn_7.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				RPObject antri = (RPObject) element;
				return antri.getPajakPeriksa();
			}
		});
		
		TableViewerColumn tableViewerColumn_8 = createTableViewerColumn(listColumn[7], columnWidth[7], 7);
		tableViewerColumn_8.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				RPObject antri = (RPObject) element;
				return antri.getBulanDaftar();
			}
		});
		
		TableViewerColumn tableViewerColumn_9 = createTableViewerColumn(listColumn[8], columnWidth[8], 8);
		tableViewerColumn_9.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				RPObject antri = (RPObject) element;
				return antri.getStatus();
			}
		});
		
		TableViewerColumn tableViewerColumn_10 = createTableViewerColumn(listColumn[9], columnWidth[9], 9);
		tableViewerColumn_10.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				RPObject antri = (RPObject) element;
				return antri.getSpt();
			}
		});
		
		tblRekomendasiPeriksa = tableViewer.getTable();
		tblRekomendasiPeriksa.setLinesVisible(true);
		tblRekomendasiPeriksa.setHeaderVisible(true);
		tblRekomendasiPeriksa.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		tblRekomendasiPeriksa.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		tblRekomendasiPeriksa.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE-1, SWT.NONE));
		tblRekomendasiPeriksa.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		
		tableViewer.setContentProvider(new ArrayContentProvider());
		tableViewer.addFilter(filter);
		getSite().setSelectionProvider(tableViewer);
	}
	
	private void loadTable(){
		try {
			listObject = new ArrayList<RPObject>();
			while (resultRekomendasi.next()){
				RPObject obj = new RPObject();
				obj.setNpwp(resultRekomendasi.getString(1));
				obj.setNamaBadan(resultRekomendasi.getString(2));
				obj.setAlamatBadan(resultRekomendasi.getString(3));
				obj.setNamaPemilik(resultRekomendasi.getString(4));
				obj.setMasaPajakDari(resultRekomendasi.getString(5));
				obj.setMasaPajakSampai(resultRekomendasi.getString(6));
				obj.setPajakPeriksa(indFormat.format(resultRekomendasi.getDouble(7)));
				obj.setBulanDaftar(resultRekomendasi.getString(8));
				obj.setStatus(resultRekomendasi.getString(10));
				obj.setSpt(resultRekomendasi.getString(11));
				
				listObject.add(obj);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void setFocus() {

	}
	
	private TableViewerColumn createTableViewerColumn(String title, int bound, int colNumber) {
		TableViewerColumn viewerColumn = new TableViewerColumn(tableViewer,SWT.NONE, colNumber);
		TableColumn column = viewerColumn.getColumn();
		column.setText(title);
		column.setWidth(bound);
		column.setResizable(true);
		column.setMoveable(true);
		return viewerColumn;
	}
	
	public class RPObject{
		private String npwp;
		private String namaBadan;
		private String alamatBadan;
		private String namaPemilik;
		private String masaPajakDari;
		private String masaPajakSampai;
		private String pajakPeriksa;
		private String bulanDaftar;
		private String status;
		private String spt;
		
		public void setAlamatBadan(String alamatBadan) {
			this.alamatBadan = alamatBadan;
		}
		
		public void setBulanDaftar(String bulanDaftar) {
			this.bulanDaftar = bulanDaftar;
		}
		
		public void setMasaPajakDari(String masaPajakDari) {
			this.masaPajakDari = masaPajakDari;
		}
		
		public void setMasaPajakSampai(String masaPajakSampai) {
			this.masaPajakSampai = masaPajakSampai;
		}
		
		public void setNamaPemilik(String namaPemilik) {
			this.namaPemilik = namaPemilik;
		}
		
		public void setPajakPeriksa(String pajakPeriksa) {
			this.pajakPeriksa = pajakPeriksa;
		}
		
		public void setStatus(String status) {
			this.status = status;
		}
		
		public void setSpt(String spt) {
			this.spt = spt;
		}
		
		public String getAlamatBadan() {
			return alamatBadan;
		}
		
		public String getBulanDaftar() {
			return bulanDaftar;
		}
		
		public String getMasaPajakDari() {
			return masaPajakDari;
		}
		
		public String getMasaPajakSampai() {
			return masaPajakSampai;
		}
		
		public String getNamaPemilik() {
			return namaPemilik;
		}
		
		public String getPajakPeriksa() {
			return pajakPeriksa;
		}
		
		public String getStatus() {
			return status;
		}
		
		public String getSpt() {
			return spt;
		}
		
		public void setNpwp(String npwp) {
			this.npwp = npwp;
		}
		
		public void setNamaBadan(String namaBadan) {
			this.namaBadan = namaBadan;
		}
		
		public String getNpwp() {
			return npwp;
		}
		
		public String getNamaBadan() {
			return namaBadan;
		}
	}
	
	private UserModul userModul = ControllerFactory.getMainController().getCpUserDAOImpl()
			.getHMUserModul(GlobalVariable.userModul.getIdUser()).get(23); // 1 merupakan id sub modul.
	
	private boolean isPrint(){		
		return userModul.getCetak();
	}
}
