package com.dispenda.laporan.views;

import java.sql.Timestamp;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
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
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.SWTResourceManager;

import com.dispenda.controller.ControllerFactory;
import com.dispenda.controller.UIController;
import com.dispenda.laporan.tree.InputTreeRekap;
import com.dispenda.laporan.tree.LPBRekapTreeContentProvider;
import com.dispenda.laporan.tree.LPBRekapTreeLabelProvider;
import com.dispenda.laporan.tree.Node;
import com.dispenda.model.PajakProvider;
import com.dispenda.object.Preferences;
import com.dispenda.report.ReportAction;
import com.dispenda.report.ReportAppContext;
import com.dispenda.widget.ProgressBarProvider;

@SuppressWarnings("deprecation")
public class LaporanPendapatanBulananView extends ViewPart {
	public LaporanPendapatanBulananView() {
	}

	public static final String ID = LaporanPendapatanBulananView.class.getName();
//	private List<Sptpd> listMasaPajakDari;
	private Color fontColor = new Color(Display.getCurrent(),Preferences.FONT_COLOR);
	private Display display = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell().getDisplay();
	private DateTime calBulan;
	private Timestamp dateNow;
	private Button btnBulan;
	private Button btnTahun;
	private Date masaPajakDari;
	private Date masaPajakSampai;
	private String masaPajak;
	private Combo cmbKewajibanPajak;
	private TreeViewer treeViewer;
	private DateTime calTahun;
	private int selection = -1;
	private Vector input;
	private Thread requestThread;
	private Locale ind = new Locale("id", "ID");
	private NumberFormat indFormat = NumberFormat.getCurrencyInstance(ind);
	
	@Override
	public void createPartControl(Composite parent) {
		Locale.setDefault(new Locale("id","ID"));
		dateNow = (java.sql.Timestamp)ControllerFactory.getMainController().getCpWajibPajakDAOImpl().getDateNow();
		parent.setLayout(new GridLayout(1, false));
		
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		composite.setLayout(new GridLayout(5, false));
		
		Label lblKewajibanPajak = new Label(composite, SWT.NONE);
		lblKewajibanPajak.setText("Kewajiban Pajak");
		lblKewajibanPajak.setForeground(fontColor);
		lblKewajibanPajak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		cmbKewajibanPajak = new Combo(composite, SWT.READ_ONLY);
		cmbKewajibanPajak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		cmbKewajibanPajak.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		GridData gd_cmbKewajibanPajak = new GridData(SWT.LEFT, SWT.CENTER, false, false, 4, 1);
		gd_cmbKewajibanPajak.widthHint = 209;
		UIController.INSTANCE.loadPajak(cmbKewajibanPajak, PajakProvider.INSTANCE.getPajak().toArray());
		cmbKewajibanPajak.setLayoutData(gd_cmbKewajibanPajak);
		
		Label lblNewLabel = new Label(composite, SWT.NONE);
		lblNewLabel.setText("Tanggal Realisasi");
		lblNewLabel.setForeground(fontColor);
		lblNewLabel.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		Group group = new Group(composite, SWT.NONE);
		GridData gd_group = new GridData(SWT.LEFT, SWT.CENTER, false, false, 4, 1);
		gd_group.widthHint = 156;
		group.setLayoutData(gd_group);
		group.setLayout(new GridLayout(3, false));
		
		btnBulan = new Button(group, SWT.RADIO);
		btnBulan.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				calBulan.setVisible(true);
				calTahun.setVisible(false);
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
				calBulan.setVisible(false);
				calTahun.setVisible(true);
				setMasaPajak();
			}
		});
		btnTahun.setText("Tahun");
		btnTahun.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BORDER));
		btnTahun.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		new Label(group, SWT.NONE);
		
		Composite composite_1 = new Composite(group, SWT.NONE);
		composite_1.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 3, 1));
		composite_1.setLayout(new StackLayout());
		
		calTahun = new DateTime(composite_1, SWT.BORDER | SWT.DROP_DOWN);
		calTahun.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (calTahun.getYear() < 2016){
					MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Tanggal tidak valid");
					calTahun.setDate(2016, 0, 1);
				}
				else{
					setMasaPajak();
				}
			}
		});
		calTahun.setVisible(false);
		calTahun.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
//		calTahun.setDay(1);
		calTahun.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		
		calBulan = new DateTime(composite_1, SWT.BORDER | SWT.DROP_DOWN | SWT.SHORT);
		calBulan.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (calBulan.getYear() < 2016){
					MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Tanggal tidak valid");
					calBulan.setDate(2016, 0, 1);
				}
				else{
					setMasaPajak();
				}
					
			}
		});
		calBulan.setVisible(false);
		calBulan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		calBulan.setDay(1);
		calBulan.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		new Label(composite, SWT.NONE);
		
		Button btnTampil = new Button(composite, SWT.NONE);
		btnTampil.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// open progress bar here
				ProgressBarProvider.INSTANCE.open(100);
				// run thread
				requestThread = new Thread(){
					public void run() {
						display.syncExec(new Runnable() {
							public void run() {
								ProgressBarProvider.INSTANCE.setProcess("Get data from Database Server..");
								if(cmbKewajibanPajak.getText().equalsIgnoreCase(""))
									selection = -1;
								else
									selection = cmbKewajibanPajak.getSelectionIndex();
							}
						});
						input = new InputTreeRekap().getList(masaPajakDari, masaPajakSampai, selection);
						display.syncExec(new Runnable() {
							public void run() {
								ProgressBarProvider.INSTANCE.setProcess("Put into Tree..");
								treeViewer.setInput(input);
								ProgressBarProvider.INSTANCE.setProcess("Done loading..");
								treeViewer.refresh();
							}
						});
						// stop progress bar here
						display.asyncExec(new Runnable(){
							@Override
							public void run() {
								try {
									ProgressBarProvider.INSTANCE.setEnd(8);
									requestThread.join();
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}
						});
					};
				};
				requestThread.start();
			}
		});
		GridData gd_btnTampil = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnTampil.widthHint = 90;
		btnTampil.setLayoutData(gd_btnTampil);
		btnTampil.setText("Tampil");
		btnTampil.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		Button btnBaru = new Button(composite, SWT.NONE);
		btnBaru.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				cmbKewajibanPajak.deselectAll();
			}
		});
		GridData gd_btnBaru = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnBaru.widthHint = 90;
		btnBaru.setLayoutData(gd_btnBaru);
		btnBaru.setText("Baru");
		btnBaru.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		Button btnCetak = new Button(composite, SWT.NONE);
		btnCetak.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String MasaLaporan = "";
				if (btnBulan.getSelection())
					MasaLaporan = "Bulan " + UIController.INSTANCE.formatMonth(calBulan.getMonth() + 1, Locale.getDefault()) + " " + calBulan.getYear();
				else{
					if (calTahun.getMonth() == 0)
						MasaLaporan = "Bulan " + UIController.INSTANCE.formatMonth(calTahun.getMonth() + 1, Locale.getDefault()) + " " + calTahun.getYear();
					else
						MasaLaporan = "Bulan Januari - " + UIController.INSTANCE.formatMonth(calTahun.getMonth() + 1, Locale.getDefault()) + " " + calTahun.getYear();
				}
				ReportAppContext.name = "Variable";
				ReportAppContext.map.put("MasaLaporan", MasaLaporan);
				ReportAppContext.nameObject = "LaporanPendapatan";
				ReportAppContext.classLoader = LaporanPendapatanBulananView.class.getClassLoader();
				if(!ReportAppContext.object.isEmpty())
					ReportAppContext.object.clear();
				Vector items = (Vector) treeViewer.getInput();
				int count = 0;
				for(int j = 0;j<items.size();j++){
					Node parent = (Node) items.get(j);
					List<String> values = new ArrayList<String>();
					values.add("\u2023 "+parent.getName());
					values.add(indFormat.format(parent.getPokok()).substring(2).replace(".", "").replace(",", "."));
					values.add(indFormat.format(parent.getDenda()).substring(2).replace(".", "").replace(",", "."));
					values.add(indFormat.format(parent.getJumlah()).substring(2).replace(".", "").replace(",", "."));
					ReportAppContext.object.put(Integer.toString(count), values);
					count++;
					for(int i=0;i<parent.getSubCategories().size();i++){
						Vector child = (Vector) parent.getSubCategories();
						Node node = (Node) child.get(i);
						values = new ArrayList<String>();
						values.add("\u2022 "+node.getName()+"");
						values.add(indFormat.format(node.getPokok()).substring(2).replace(".", "").replace(",", "."));
						values.add(indFormat.format(node.getDenda()).substring(2).replace(".", "").replace(",", "."));
						values.add(indFormat.format(node.getJumlah()).substring(2).replace(".", "").replace(",", "."));
						ReportAppContext.object.put(Integer.toString(count), values);
						count++;
					}
				}
				ReportAction.start("LaporanPendapatan.rptdesign");
				treeViewer.collapseAll();
			}
		});
		GridData gd_btnCetak = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnCetak.widthHint = 90;
		btnCetak.setLayoutData(gd_btnCetak);
		btnCetak.setText("Cetak");
		btnCetak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		new Label(composite, SWT.NONE);
		
		treeViewer = new TreeViewer(composite, SWT.BORDER | SWT.FULL_SELECTION| SWT.VIRTUAL);
		Tree tree = treeViewer.getTree();
		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 4, 1));
		tree.setLinesVisible(true);
		tree.setHeaderVisible(true);
		tree.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		tree.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		new Label(composite, SWT.NONE);
		treeViewer.setContentProvider(new LPBRekapTreeContentProvider());
		treeViewer.setLabelProvider(new LPBRekapTreeLabelProvider());

		loadColumn();
	}

	private void loadColumn() {
//		String[] colName = new String[]{"No Reg.","NPWPD","Nama Badan","Bid. Usaha","No SKP","No SSPD","Bulan SKP","Cicilan Ke","Tgl Bayar","Pokok","Denda","Jumlah"};
//		Integer[] colWidth = new Integer[]{60,100,200,160,130,130,85,85,100,150,150,150};
		String[] colName = new String[]{"Nama Pajak","Pokok","Denda","Jumlah"};
		Integer[] colWidth = new Integer[]{230,200,200,200};
		Tree tree = treeViewer.getTree();
		for(int i=0;i<colName.length;i++){
			TreeColumn col = new TreeColumn(tree, SWT.NONE);
			col.setText(colName[i]);
			col.setWidth(colWidth[i]);
		}
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}
	
	public void setMasaPajak()
	{
		if (btnBulan.getSelection())
		{
			Calendar instance = Calendar.getInstance();
			instance.set(Calendar.DAY_OF_MONTH, calBulan.getDay());
			instance.set(Calendar.MONTH, calBulan.getMonth());
			instance.set(Calendar.YEAR, calBulan.getYear());
			masaPajakDari = new Date(calBulan.getYear() - 1900, calBulan.getMonth(), 1);
			masaPajakSampai = new Date(calBulan.getYear() - 1900, calBulan.getMonth(), instance.getActualMaximum(Calendar.DATE));
			masaPajak = UIController.INSTANCE.formatMonth(calBulan.getMonth() + 1, Locale.getDefault()) + " " + calBulan.getYear();
		}
		if (btnTahun.getSelection())
		{
			Calendar instance = Calendar.getInstance();
			instance.set(Calendar.DAY_OF_MONTH, calTahun.getDay());
			instance.set(Calendar.MONTH, calTahun.getMonth());
			instance.set(Calendar.YEAR, calTahun.getYear());
			masaPajakDari = new Date(calTahun.getYear() - 1900, 0, 1);
			masaPajakSampai = new Date(calTahun.getYear() - 1900, calTahun.getMonth(), calTahun.getDay());
			masaPajak = UIController.INSTANCE.formatMonth(calTahun.getMonth() + 1, Locale.getDefault()) + " " + calTahun.getYear();
//			masaPajakDari = new Date(spnTahun.getSelection() - 1900, 0, 1);
//			masaPajakSampai = new Date(spnTahun.getSelection() - 1900, 11, 31);
//			masaPajak = Integer.toString(spnTahun.getSelection());
		}
	
	}
	
//	private void loadMasaPajakDari(){
//		listMasaPajakDari = ControllerFactory.getMainController().getCpSptpdDAOImpl().getMasaPeriksaDari();
//		if (listMasaPajakDari.size() > 0)
//			UIController.INSTANCE.loadMasaPajakPeriksaDari(cmbBulanTahun, listMasaPajakDari.toArray()); //PajakProvider.INSTANCE.getPajak().toArray());
//	}
}

