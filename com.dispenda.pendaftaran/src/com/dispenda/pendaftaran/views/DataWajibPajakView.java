package com.dispenda.pendaftaran.views;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Spinner;

import com.dispenda.controller.ControllerFactory;
import com.dispenda.controller.UIController;
import com.dispenda.pendaftaran.tree.Node;
//import com.dispenda.laporan.views.LaporanPendapatanBulananView;
import com.dispenda.model.PajakProvider;
import com.dispenda.model.UserModul;
import com.dispenda.object.GlobalVariable;
import com.dispenda.object.Preferences;
import com.dispenda.pendaftaran.tree.InputTreeRekap;
import com.dispenda.pendaftaran.tree.LPBRekapTreeContentProvider;
import com.dispenda.pendaftaran.tree.LPBRekapTreeLabelProvider;
import com.dispenda.report.ReportAction;
import com.dispenda.report.ReportAppContext;
import com.dispenda.widget.ProgressBarProvider;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.Group;

public class DataWajibPajakView extends ViewPart{
	private Timestamp dateNow;
	private int selection = -1;
	private Vector input;
	private Vector inputInsidentil;

	public DataWajibPajakView() {
	}
	
	public static final String ID = DataWajibPajakView.class.getName();
	private Table tblWP;
	private Text txtAktif;
	private Text txtNonAktif;
	private Text txtTutup;
	private Combo cmbPajak;
	private Spinner spnTahun;
	private Text txtTotal;
	private Combo cmbPajakRekap;
	private TreeViewer treeViewer;
	private TreeViewer treeViewerInsidentil;
	private Group grpNonInsidentil;
	private Group grpInsidentil;
	

	@SuppressWarnings("deprecation")
	@Override
	public void createPartControl(Composite parent) {
		Locale.setDefault(new Locale("id","ID"));
		dateNow = (java.sql.Timestamp)ControllerFactory.getMainController().getCpWajibPajakDAOImpl().getDateNow();
		
		ScrolledComposite scrolledComposite = new ScrolledComposite(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		
		/*TabFolder tabFolder = new TabFolder(scrolledComposite, SWT.NONE);
		
		TabItem tbtmDetails = new TabItem(tabFolder, SWT.NONE);
		tbtmDetails.setText("New Item");
		
		Composite composite = new Composite(scrolledComposite, SWT.NONE);
//		tbtmDetails.setControl(composite);
		composite.setLayout(new GridLayout(8, false));*/
		
		Composite mainComp = new Composite(scrolledComposite, SWT.NONE);
		mainComp.setLayout(new GridLayout(1, false));
		
		TabFolder tabFolder = new TabFolder(mainComp, SWT.NONE);
		tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		TabItem tbtmNewItem = new TabItem(tabFolder, SWT.NONE);
		tbtmNewItem.setText("Rekap");
		
		Composite compositerekap = new Composite(tabFolder, SWT.NONE);
		tbtmNewItem.setControl(compositerekap);
		compositerekap.setLayout(new GridLayout(5, false));
		
		Label lblKewajibanPajak_1 = new Label(compositerekap, SWT.NONE);
		
		lblKewajibanPajak_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblKewajibanPajak_1.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblKewajibanPajak_1.setText("Kewajiban Pajak");
		
		cmbPajakRekap = new Combo(compositerekap, SWT.READ_ONLY);
		GridData gd_cmbPajakRekap = new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1);
		cmbPajakRekap.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		cmbPajakRekap.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		cmbPajakRekap.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		gd_cmbPajakRekap.widthHint = 169;
		cmbPajakRekap.setLayoutData(gd_cmbPajakRekap);
		UIController.INSTANCE.loadPajak(cmbPajakRekap, PajakProvider.INSTANCE.getPajak().toArray());
		new Label(compositerekap, SWT.NONE);
		new Label(compositerekap, SWT.NONE);
		
		Button btnLihatRekap = new Button(compositerekap, SWT.NONE);
		btnLihatRekap.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(cmbPajakRekap.getText().equalsIgnoreCase(""))
					selection = -1;
				else
					selection = cmbPajakRekap.getSelectionIndex();
				loadColumnRekap();
				getDataNonInsidentil();
				getDataInsidentil();
			}
		});
		GridData gd_btnLihatRekap = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnLihatRekap.widthHint = 80;
		
		btnLihatRekap.setLayoutData(gd_btnLihatRekap);
		btnLihatRekap.setText("Lihat");
		btnLihatRekap.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		Button btnCetakRekap = new Button(compositerekap, SWT.NONE);
		btnCetakRekap.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (isPrint()){
					String header = "";
					SimpleDateFormat sdf = new SimpleDateFormat("d MMMM yyyy");
					if (cmbPajakRekap.getSelectionIndex() < 0)
						header = "Data Status Wajib Pajak per " + sdf.format(dateNow);
					else
						header = "Data Status Wajib Pajak " + cmbPajakRekap.getText() + " per " + sdf.format(dateNow);
					ReportAppContext.obj = new String[]{"DataWPRekap", "DataWPRekapIns"};
					ReportAppContext.listObject = new Object[2];
					HashMap<String, List<String>> DataWPRekap = new HashMap<String, List<String>>();
					HashMap<String, List<String>> DataWPRekapIns = new HashMap<String, List<String>>();
					ReportAppContext.name = "Variable";
					ReportAppContext.map.put("header", header);
					ReportAppContext.nameObject = "DataWPRekap";
					ReportAppContext.classLoader = DataWajibPajakView.class.getClassLoader();
					if(!ReportAppContext.object.isEmpty())
						ReportAppContext.object.clear();
					Vector items = input;//(Vector) treeViewer.getInput();
					int count = 0;
					Integer totalAktif = 0;
					Integer totalNonAktif = 0;
					Integer totalTutup = 0;
					Integer totalSeluruh = 0;
					for(int j = 0;j<items.size();j++){
						Node parent = (Node) items.get(j);
						List<String> values = new ArrayList<String>();
						values.add("\u2023 "+parent.getName());
//						values.add(parent.getName());
						values.add(parent.getAktif().toString());
						values.add(parent.getNonAktif().toString());
						values.add(parent.getTutup().toString());
						values.add(parent.getTotal().toString());
						values.add("Gray");
						totalAktif += parent.getAktif();
						totalNonAktif += parent.getNonAktif();
						totalTutup += parent.getTutup();
						totalSeluruh += parent.getTotal();
						DataWPRekap.put(Integer.toString(count), values);
						count++;
						for(int i=0;i<parent.getSubCategories().size();i++){
							Vector child = (Vector) parent.getSubCategories();
							Node node = (Node) child.get(i);
							values = new ArrayList<String>();
							values.add("\u2022 "+node.getName()+"");
//							values.add(node.getName());
							values.add(node.getAktif().toString());
							values.add(node.getNonAktif().toString());
							values.add(node.getTutup().toString());
							values.add(node.getTotal().toString());
							values.add("");
							DataWPRekap.put(Integer.toString(count), values);
							count++;
						}
					}
					
					ReportAppContext.nameObject = "DataWPRekapIns";
					Vector itemsIns = inputInsidentil;//(Vector) treeViewerInsidentil.getInput();
					int countIns = 0;
					Integer totalAktifIns = 0;
					Integer totalNonAktifIns = 0;
					Integer totalTutupIns = 0;
					Integer totalSeluruhIns = 0;
					for(int j = 0;j<itemsIns.size();j++){
						Node parent = (Node) itemsIns.get(j);
						List<String> values = new ArrayList<String>();
						values.add("\u2023 "+parent.getName());
//						values.add(parent.getName());
						values.add(parent.getAktif().toString());
						values.add(parent.getNonAktif().toString());
						values.add(parent.getTutup().toString());
						values.add(parent.getTotal().toString());
						values.add("Gray");
						totalAktifIns += parent.getAktif();
						totalNonAktifIns += parent.getNonAktif();
						totalTutupIns += parent.getTutup();
						totalSeluruhIns += parent.getTotal();
						DataWPRekapIns.put(Integer.toString(countIns), values);
						countIns++;
						for(int i=0;i<parent.getSubCategories().size();i++){
							Vector child = (Vector) parent.getSubCategories();
							Node node = (Node) child.get(i);
							values = new ArrayList<String>();
							values.add("\u2022 "+node.getName()+"");
//							values.add(node.getName());
							values.add(node.getAktif().toString());
							values.add(node.getNonAktif().toString());
							values.add(node.getTutup().toString());
							values.add(node.getTotal().toString());
							values.add("");
							DataWPRekapIns.put(Integer.toString(countIns), values);
							countIns++;
						}
					}
					ReportAppContext.map.put("Aktif", totalAktif.toString());
					ReportAppContext.map.put("NonAktif", totalNonAktif.toString());
					ReportAppContext.map.put("Tutup", totalTutup.toString());
					ReportAppContext.map.put("Total", totalSeluruh.toString());
					ReportAppContext.map.put("AktifIns", totalAktifIns.toString());
					ReportAppContext.map.put("NonAktifIns", totalNonAktifIns.toString());
					ReportAppContext.map.put("TutupIns", totalTutupIns.toString());
					ReportAppContext.map.put("TotalIns", totalSeluruhIns.toString());
					ReportAppContext.listObject[0] = DataWPRekap;
					ReportAppContext.listObject[1] = DataWPRekapIns;
					ReportAction.start("DataWPRekap.rptdesign");
					treeViewer.collapseAll();
				}
			}
		});
		btnCetakRekap.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		GridData gd_btnCetakRekap = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnCetakRekap.widthHint = 80;
		btnCetakRekap.setLayoutData(gd_btnCetakRekap);
		btnCetakRekap.setText("Cetak");
		
		Button btnBaru = new Button(compositerekap, SWT.NONE);
		btnBaru.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				cmbPajakRekap.deselectAll();
				selection = -1;
				treeViewer.getTree().removeAll();
				treeViewerInsidentil.getTree().removeAll();
				Control[] children = grpNonInsidentil.getChildren();
				for (int i=0;i<children.length;i++)
				{
					if (children[i] instanceof Text)
					{
						Text child = (Text) children[i];
						child.setText("");
					}
				}
				
				Control[] children2 = grpInsidentil.getChildren();
				for (int i=0;i<children2.length;i++)
				{
					if (children2[i] instanceof Text)
					{
						Text child = (Text) children2[i];
						child.setText("");
					}
				}
			}
		});
		GridData gd_btnBaru = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnBaru.widthHint = 80;
		btnBaru.setLayoutData(gd_btnBaru);
		btnBaru.setText("Baru");
		btnBaru.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		new Label(compositerekap, SWT.NONE);
		
		TabFolder tabNonInsidentil = new TabFolder(compositerekap, SWT.NONE);
		tabNonInsidentil.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 5, 1));
		
		TabItem tbtmNonInsidentil = new TabItem(tabNonInsidentil, SWT.NONE);
		tbtmNonInsidentil.setText("Non Insidentil");
		
		treeViewer = new TreeViewer(tabNonInsidentil, SWT.BORDER | SWT.FULL_SELECTION| SWT.VIRTUAL);
		Tree tree = treeViewer.getTree();
		tbtmNonInsidentil.setControl(tree);
		tree.setLinesVisible(true);
		tree.setHeaderVisible(true);
		tree.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		tree.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		
		TabItem tbtmInsidentil = new TabItem(tabNonInsidentil, SWT.NONE);
		tbtmInsidentil.setText("Insidentil");
		
		treeViewerInsidentil = new TreeViewer(tabNonInsidentil, SWT.BORDER | SWT.FULL_SELECTION| SWT.VIRTUAL);
		Tree treeInsidentil = treeViewerInsidentil.getTree();
		treeInsidentil.setLinesVisible(true);
		treeInsidentil.setHeaderVisible(true);
		treeInsidentil.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		treeInsidentil.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		tbtmInsidentil.setControl(treeInsidentil);
		treeViewer.setContentProvider(new LPBRekapTreeContentProvider());
		treeViewer.setLabelProvider(new LPBRekapTreeLabelProvider());
		treeViewerInsidentil.setContentProvider(new LPBRekapTreeContentProvider());
		treeViewerInsidentil.setLabelProvider(new LPBRekapTreeLabelProvider());
		
		grpNonInsidentil = new Group(compositerekap, SWT.NONE);
		grpNonInsidentil.setText("Non Insidentil");
		grpNonInsidentil.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		grpNonInsidentil.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 4, 1));
		grpNonInsidentil.setLayout(new GridLayout(8, false));
		
		Label lblAktif_1 = new Label(grpNonInsidentil, SWT.NONE);
		lblAktif_1.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblAktif_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblAktif_1.setText("Aktif");
		
		txtWPAktif = new Text(grpNonInsidentil, SWT.BORDER);
		GridData gd_txtWPAktif = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_txtWPAktif.horizontalIndent = 5;
		txtWPAktif.setLayoutData(gd_txtWPAktif);
		txtWPAktif.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtWPAktif.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		Label lblNonaktif = new Label(grpNonInsidentil, SWT.NONE);
		lblNonaktif.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		GridData gd_lblNonaktif = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_lblNonaktif.horizontalIndent = 10;
		lblNonaktif.setLayoutData(gd_lblNonaktif);
		lblNonaktif.setText("NonAktif");
		
		txtWPNonAktif = new Text(grpNonInsidentil, SWT.BORDER);
		GridData gd_txtWPNonAktif = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_txtWPNonAktif.horizontalIndent = 5;
		txtWPNonAktif.setLayoutData(gd_txtWPNonAktif);
		txtWPNonAktif.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtWPNonAktif.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		Label lblTutup_1 = new Label(grpNonInsidentil, SWT.NONE);
		lblTutup_1.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		GridData gd_lblTutup_1 = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_lblTutup_1.horizontalIndent = 10;
		lblTutup_1.setLayoutData(gd_lblTutup_1);
		lblTutup_1.setText("Tutup");
		
		txtWPTutup = new Text(grpNonInsidentil, SWT.BORDER);
		GridData gd_txtWPTutup = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_txtWPTutup.horizontalIndent = 5;
		txtWPTutup.setLayoutData(gd_txtWPTutup);
		txtWPTutup.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtWPTutup.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		Label lblTotal_1 = new Label(grpNonInsidentil, SWT.NONE);
		lblTotal_1.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		GridData gd_lblTotal_1 = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_lblTotal_1.horizontalIndent = 10;
		lblTotal_1.setLayoutData(gd_lblTotal_1);
		lblTotal_1.setText("Total");
		
		txtWPTotal = new Text(grpNonInsidentil, SWT.BORDER);
		GridData gd_txtWPTotal = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_txtWPTotal.horizontalIndent = 5;
		txtWPTotal.setLayoutData(gd_txtWPTotal);
		txtWPTotal.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtWPTotal.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		grpInsidentil = new Group(compositerekap, SWT.NONE);
		grpInsidentil.setText("Insidentil");
		grpInsidentil.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		grpInsidentil.setLayout(new GridLayout(8, false));
		
		Label label = new Label(grpInsidentil, SWT.NONE);
		label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label.setText("Aktif");
		label.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		txtWPAktifIns = new Text(grpInsidentil, SWT.BORDER);
		txtWPAktifIns.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtWPAktifIns.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		GridData gd_txtWPAktifIns = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_txtWPAktifIns.horizontalIndent = 5;
		txtWPAktifIns.setLayoutData(gd_txtWPAktifIns);
		
		Label label_1 = new Label(grpInsidentil, SWT.NONE);
		GridData gd_label_1 = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_label_1.horizontalIndent = 10;
		label_1.setLayoutData(gd_label_1);
		label_1.setText("NonAktif");
		label_1.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		txtWPNonAktifIns = new Text(grpInsidentil, SWT.BORDER);
		txtWPNonAktifIns.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtWPNonAktifIns.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		GridData gd_txtWPNonAktifIns = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_txtWPNonAktifIns.horizontalIndent = 5;
		txtWPNonAktifIns.setLayoutData(gd_txtWPNonAktifIns);
		
		Label label_2 = new Label(grpInsidentil, SWT.NONE);
		GridData gd_label_2 = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_label_2.horizontalIndent = 10;
		label_2.setLayoutData(gd_label_2);
		label_2.setText("Tutup");
		label_2.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		txtWPTutupIns = new Text(grpInsidentil, SWT.BORDER);
		txtWPTutupIns.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtWPTutupIns.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		GridData gd_txtWPTutupIns = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_txtWPTutupIns.horizontalIndent = 5;
		txtWPTutupIns.setLayoutData(gd_txtWPTutupIns);
		
		Label label_3 = new Label(grpInsidentil, SWT.NONE);
		GridData gd_label_3 = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_label_3.horizontalIndent = 10;
		label_3.setLayoutData(gd_label_3);
		label_3.setText("Total");
		label_3.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		txtWPTotalIns = new Text(grpInsidentil, SWT.BORDER);
		txtWPTotalIns.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtWPTotalIns.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		GridData gd_txtWPTotalIns = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_txtWPTotalIns.horizontalIndent = 5;
		txtWPTotalIns.setLayoutData(gd_txtWPTotalIns);
		
		TabItem tbtmDetails = new TabItem(tabFolder, SWT.NONE);
		tbtmDetails.setText("Detail");
		
		Composite composite = new Composite(tabFolder, SWT.NONE);
		tbtmDetails.setControl(composite);
		composite.setLayout(new GridLayout(8, false));
		
		Label lblKewajibanPajak = new Label(composite, SWT.NONE);
		lblKewajibanPajak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		GridData gd_lblKewajibanPajak = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
		gd_lblKewajibanPajak.widthHint = 115;
		lblKewajibanPajak.setLayoutData(gd_lblKewajibanPajak);
		lblKewajibanPajak.setText("Kewajiban Pajak");
		
		cmbPajak = new Combo(composite, SWT.READ_ONLY);
		GridData gd_cmbPajak = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
		gd_cmbPajak.widthHint = 169;
		cmbPajak.setLayoutData(gd_cmbPajak);
		cmbPajak.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		cmbPajak.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		cmbPajak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		UIController.INSTANCE.loadPajak(cmbPajak, PajakProvider.INSTANCE.getPajak().toArray());
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		
		Label lblTahun = new Label(composite, SWT.NONE);
		lblTahun.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		lblTahun.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblTahun.setText("Tahun");
		
		spnTahun = new Spinner(composite, SWT.BORDER);
		spnTahun.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		spnTahun.setValues(dateNow.getYear() + 1900, 2009, dateNow.getYear() + 1900, 0, 1, 10);
		spnTahun.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		spnTahun.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		
		Button btnLihat = new Button(composite, SWT.NONE);
		btnLihat.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				tblWP.removeAll();
				ResultSet result = ControllerFactory.getMainController().getCpWajibPajakDAOImpl().getWPbyTglDaftar(cmbPajak.getData(Integer.toString(cmbPajak.getSelectionIndex())).toString(), Integer.valueOf(spnTahun.getText()));
				createColumn();
				loadData(result);
			}
		});
		GridData gd_btnLihat = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnLihat.widthHint = 80;
		btnLihat.setLayoutData(gd_btnLihat);
		btnLihat.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnLihat.setText("Lihat");
		
		Button btnCetakDetail = new Button(composite, SWT.NONE);
		btnCetakDetail.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(isPrint()){
					ReportAppContext.name = "Variable";
					ReportAppContext.map.put("header", cmbPajak.getText());
					ReportAppContext.nameObject = "DataWP";
					ReportAppContext.classLoader = DataWajibPajakView.class.getClassLoader();
					if(!ReportAppContext.object.isEmpty())
						ReportAppContext.object.clear();
					for(int j = 0;j<tblWP.getItems().length-1;j++){// untuk masukan row table
						List<String> values = new ArrayList<String>();
						for(int i=1;i<tblWP.getColumnCount();i++){// untuk masukkan data berdasarkan colom table
							values.add(tblWP.getItem(j).getText(i));
						}
						ReportAppContext.object.put(Integer.toString(j), values);
					}
					
					ReportAction.start("DataWPDetail.rptdesign");
				}else
					MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Anda tidak memiliki hak untuk mencetak.");
			}
		});
		btnCetakDetail.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		GridData gd_btnCetakDetail = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnCetakDetail.widthHint = 80;
		btnCetakDetail.setLayoutData(gd_btnCetakDetail);
		btnCetakDetail.setText("Cetak");
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		
		tblWP = new Table(composite, SWT.BORDER | SWT.FULL_SELECTION);
		tblWP.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 8, 2));
		tblWP.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		tblWP.setForeground(SWTResourceManager.getColor(0, 0, 0));
		tblWP.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE-1, SWT.NORMAL));
		tblWP.setHeaderVisible(true);
		tblWP.setLinesVisible(true);
		
		Label lblAktif = new Label(composite, SWT.NONE);
		lblAktif.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblAktif.setText("Aktif");
		
		txtAktif = new Text(composite, SWT.BORDER | SWT.READ_ONLY);
		txtAktif.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtAktif.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		GridData gd_txtAktif = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_txtAktif.horizontalIndent = 10;
		gd_txtAktif.widthHint = 75;
		txtAktif.setLayoutData(gd_txtAktif);
		
		Label lblNonAktif = new Label(composite, SWT.NONE);
		lblNonAktif.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		GridData gd_lblNonAktif = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblNonAktif.horizontalIndent = 35;
		lblNonAktif.setLayoutData(gd_lblNonAktif);
		lblNonAktif.setText("Non Aktif");
		
		txtNonAktif = new Text(composite, SWT.BORDER | SWT.READ_ONLY);
		txtNonAktif.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtNonAktif.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		GridData gd_txtNonAktif = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
		gd_txtNonAktif.horizontalIndent = 10;
		gd_txtNonAktif.widthHint = 75;
		txtNonAktif.setLayoutData(gd_txtNonAktif);
		
		Label lblTutup = new Label(composite, SWT.NONE);
		lblTutup.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		GridData gd_lblTutup = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblTutup.horizontalIndent = 35;
		lblTutup.setLayoutData(gd_lblTutup);
		lblTutup.setText("Tutup");
		
		txtTutup = new Text(composite, SWT.BORDER | SWT.READ_ONLY);
		txtTutup.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtTutup.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		GridData gd_txtTutup = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_txtTutup.horizontalIndent = 10;
		gd_txtTutup.widthHint = 75;
		txtTutup.setLayoutData(gd_txtTutup);
		
		Label lblTotal = new Label(composite, SWT.NONE);
		lblTotal.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		GridData gd_lblTotal = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_lblTotal.horizontalIndent = 35;
		lblTotal.setLayoutData(gd_lblTotal);
		lblTotal.setText("Total");
		
		txtTotal = new Text(composite, SWT.BORDER | SWT.READ_ONLY);
		txtTotal.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtTotal.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		GridData gd_txtTotal = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_txtTotal.widthHint = 75;
		gd_txtTotal.horizontalIndent = 10;
		txtTotal.setLayoutData(gd_txtTotal);
		
		scrolledComposite.setContent(mainComp);
		scrolledComposite.setMinSize(mainComp.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * 
	 */
	private void createColumn()
	{
		if(tblWP.getColumnCount() <= 0){
			String[] listColumn = {"No.","NPWPD", "Nama Badan", "Tanggal Daftar", "Tanggal Tutup", "Tutup Sampai", "SPTPD", "Status"};
			int[] columnsWidth = {50, 110, 180, 110, 110, 110, 90, 100};
			for (int i=listColumn.length - 1;i>=0;i--)
			{
				TableColumn colPajak = new TableColumn(tblWP, SWT.CENTER,0);
				colPajak.setText(listColumn[i]);
				colPajak.setWidth(columnsWidth[i]);
			}
		}
	}
	
	private void loadColumnRekap() {
		if (treeViewer.getTree().getColumnCount() < 1){
			String[] colName = new String[]{"Nama Pajak","Aktif","NonAktif","Tutup","Total"};
			Integer[] colWidth = new Integer[]{230,130,130,130,130};
			Tree tree = treeViewer.getTree();
			for(int i=0;i<colName.length;i++){
				TreeColumn col = new TreeColumn(tree, SWT.NONE);
				col.setText(colName[i]);
				col.setWidth(colWidth[i]);
			}
		}
		
		if (treeViewerInsidentil.getTree().getColumnCount() < 1){
			String[] colName = new String[]{"Nama Pajak","Aktif","NonAktif","Tutup","Total"};
			Integer[] colWidth = new Integer[]{230,130,130,130,130};
			Tree tree = treeViewerInsidentil.getTree();
			for(int i=0;i<colName.length;i++){
				TreeColumn col = new TreeColumn(tree, SWT.NONE);
				col.setText(colName[i]);
				col.setWidth(colWidth[i]);
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	private void loadData(final ResultSet result){
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		int count = 1;
		Integer wpAktif = 0;
		Integer wpNonAktif = 0;
		Integer wpTutup = 0;
		try {
			while (result.next()){
				TableItem item = new TableItem(tblWP, SWT.NONE);
				item.setText(0, count+".");
				item.setText(1, result.getString("NPWPD"));
				item.setText(2, result.getString("Nama_Badan"));
				item.setText(3, formatter.format(result.getDate("Tanggal_Daftar")));
				item.setText(4, result.getDate("Mulai") == null ? "-" : formatter.format(result.getDate("Mulai")));
				item.setText(5, result.getDate("Sampai") == null ? "-" : formatter.format(result.getDate("Sampai")));
				item.setText(6, result.getDate("SPTPD") == null ? "-" : formatter.format(result.getDate("SPTPD")));
				if (item.getText(4) == "-"){
					if (item.getText(6) != "-"){
						if (UIController.INSTANCE.getMonthDiff(result.getDate("SPTPD"), dateNow) > 12){
							item.setText(7, "Non Aktif");
							wpNonAktif++;
						}
						else{
							item.setText(7, "Aktif");
							wpAktif++;
						}
					}
					else
						if (UIController.INSTANCE.getMonthDiff(result.getDate("Tanggal_Daftar"), dateNow) > 12){
							item.setText(7, "Non Aktif");
							wpNonAktif++;
						}
						else{
							item.setText(7, "Aktif");
							wpAktif++;
						}
				}
				else{
					if ((result.getDate("Sampai").getYear() + 1900) < 6000){
						item.setText(7, "Aktif");
						wpAktif++;
					}
					else{
						if ((result.getDate("Mulai").getYear() + 1900) > Integer.valueOf(spnTahun.getText())){
							item.setText(7, "Aktif");
							wpAktif++;
						}
						else{
							item.setText(7, "Tutup");
							wpTutup++;
						}
					}
				}
				count++;
			}
			txtAktif.setText(wpAktif.toString());
			txtNonAktif.setText(wpNonAktif.toString());
			txtTutup.setText(wpTutup.toString());
			txtTotal.setText(String.valueOf((wpAktif + wpNonAktif + wpTutup)));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void getDataNonInsidentil(){
		input = new InputTreeRekap().getList(selection);
		treeViewer.setInput(input);
		treeViewer.refresh();
		
		Integer totalAktif = 0;
		Integer totalNonAktif = 0;
		Integer totalTutup = 0;
		Integer totalSeluruh = 0;
		Vector items = input; //(Vector) treeViewer.getInput();
		for(int j = 0;j<items.size();j++){
			Node parent = (Node) items.get(j);
			totalAktif += parent.getAktif();
			totalNonAktif += parent.getNonAktif();
			totalTutup += parent.getTutup();
			totalSeluruh += parent.getTotal();
		}
		txtWPAktif.setText(totalAktif.toString());
		txtWPNonAktif.setText(totalNonAktif.toString());
		txtWPTutup.setText(totalTutup.toString());
		txtWPTotal.setText(totalSeluruh.toString());
	}
	
	private void getDataInsidentil(){
		inputInsidentil = new InputTreeRekap().getListInsidentil(selection);
		treeViewerInsidentil.setInput(inputInsidentil);
		treeViewerInsidentil.refresh();
		
		Integer totalAktif = 0;
		Integer totalNonAktif = 0;
		Integer totalTutup = 0;
		Integer totalSeluruh = 0;
		Vector items = inputInsidentil;//(Vector) treeViewerInsidentil.getInput();
		for(int j = 0;j<items.size();j++){
			Node parent = (Node) items.get(j);
			totalAktif += parent.getAktif();
			totalNonAktif += parent.getNonAktif();
			totalTutup += parent.getTutup();
			totalSeluruh += parent.getTotal();
		}
		txtWPAktifIns.setText(totalAktif.toString());
		txtWPNonAktifIns.setText(totalNonAktif.toString());
		txtWPTutupIns.setText(totalTutup.toString());
		txtWPTotalIns.setText(totalSeluruh.toString());
	}

	private UserModul userModul = ControllerFactory.getMainController().getCpUserDAOImpl()
			.getHMUserModul(GlobalVariable.userModul.getIdUser()).get(40);
	private Text txtWPAktif;
	private Text txtWPNonAktif;
	private Text txtWPTutup;
	private Text txtWPTotal;
	private Text txtWPAktifIns;
	private Text txtWPNonAktifIns;
	private Text txtWPTutupIns;
	private Text txtWPTotalIns;
	private boolean isPrint(){		
		return userModul.getCetak();
	}
}