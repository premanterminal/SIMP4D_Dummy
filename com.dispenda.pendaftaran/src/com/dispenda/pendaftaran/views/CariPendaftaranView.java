package com.dispenda.pendaftaran.views;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.SwingUtilities;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
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
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.SWTResourceManager;

import com.dispenda.controller.ControllerFactory;
import com.dispenda.controller.UIController;
import com.dispenda.dao.LogImp;
import com.dispenda.database.DBConnection;
import com.dispenda.model.Pejabat;
import com.dispenda.model.PendaftaranWajibPajak;
import com.dispenda.model.PendaftaranWajibPajakProvider;
import com.dispenda.model.Periksa;
import com.dispenda.model.UUwp;
import com.dispenda.model.UserModul;
import com.dispenda.object.GlobalVariable;
import com.dispenda.object.Preferences;
import com.dispenda.pendaftaran.bridge.ICommunicationView;
import com.dispenda.report.ReportAction;
import com.dispenda.report.ReportAppContext;

public class CariPendaftaranView extends ViewPart {
	public CariPendaftaranView() {
	}

	public static final String ID = CariPendaftaranView.class.getName();
	private Text txtCari;
	private Table table;
	private TableViewer tableViewer;
	private List listPencarian;
	private PendaftaranComparator comparator = new PendaftaranComparator();
	private PendaftaranFilter filter;
	private Combo cmbTandaTangan;
	private Color fontColor = new Color(Display.getCurrent(),Preferences.FONT_COLOR);
	protected java.util.List<PendaftaranWajibPajak> listTeguran;
	private Timestamp dateNow;
	private boolean isTeguran = false;
	private Button btnDaftarWajibPajak;
	private Button btnSuratKeputusanNpwp;
	private Button btnKartuNpwpd;
	private Button btnFormulirPendaftaran;
	private Button btnSuratTeguran;
	private Button btnDaftarTeguran;
	private Button btnAmplop;
	private Button btnTeguran;
	private ArrayList<Periksa> listPeriksa = new ArrayList<Periksa>();
	private Date pajakDari[];
	private Date pajakSampai[];
	PendaftaranWajibPajak wp;
	
	public void createPartControl(Composite parent) {
		dateNow = (java.sql.Timestamp)ControllerFactory.getMainController().getCpWajibPajakDAOImpl().getDateNow();
		Locale.setDefault(new Locale("id","ID"));
		ScrolledComposite scrolledComposite = new ScrolledComposite(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		
		Composite composite = new Composite(scrolledComposite, SWT.NONE);
		composite.setLayout(new GridLayout(6, false));
		
		btnBantuan = new Button(composite, SWT.NONE);
		btnBantuan.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(!ReportAppContext.object.isEmpty())
					ReportAppContext.object.clear();
				ReportAppContext.nameObject = "Help";
				ReportAppContext.name = "Variable";
				
				ReportAppContext.classLoader = CariPendaftaranView.class.getClassLoader();
				ReportAction.start("PendaftaranHelp.rptdesign");
			}
		});
		btnBantuan.setText("Bantuan");
		
		Label lblCari = new Label(composite, SWT.NONE);
		lblCari.setForeground(fontColor);
		lblCari.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblCari.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 1));
		lblCari.setText("Cari :");
		
		txtCari = new Text(composite, SWT.BORDER);
		txtCari.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtCari.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		txtCari.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		GridData gd_txtCari = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_txtCari.widthHint = 435;
		txtCari.setLayoutData(gd_txtCari);
		txtCari.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.keyCode == SWT.CR || e.keyCode == SWT.KEYPAD_CR){
					filter.setSearchText(txtCari.getText(),listPencarian);
			        tableViewer.refresh();
			        txtJumlahWP.setText(tableViewer.getTable().getItemCount()+"");
			        isTeguran = false;
			        disableBtnCetak();
				}
			}
		});
		
		Button btnSearch = new Button(composite, SWT.NONE);
		btnSearch.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				filter.setSearchText(txtCari.getText(),listPencarian);
			    tableViewer.refresh();
			    txtJumlahWP.setText(tableViewer.getTable().getItemCount()+"");
			    isTeguran = false;
			    disableBtnCetak();
			}
		});
		btnSearch.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BORDER));
		btnSearch.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnSearch.setText("Search...");
		
		final Button btnRefresh = new Button(composite, SWT.NONE);
		btnRefresh.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				for(TableColumn col : tableViewer.getTable().getColumns())
					col.dispose();
				createColumnsNormal();
				for(TableItem item : tableViewer.getTable().getItems())
					item.dispose();
				tableViewer.setContentProvider(new ArrayContentProvider());
				tableViewer.setInput(PendaftaranWajibPajakProvider.INSTANCE.getWajibPajak());
				tableViewer.refresh();
				txtJumlahWP.setText(tableViewer.getTable().getItemCount()+"");
//				tableViewer.setComparator(comparator);
//				getSite().setSelectionProvider(tableViewer);
//				filter = new PendaftaranFilter(listPencarian);
//				tableViewer.addFilter(filter);
			}
		});
		btnRefresh.setText("Refresh");
		btnRefresh.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BORDER));
		btnRefresh.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		btnTeguran = new Button(composite, SWT.NONE);
		btnTeguran.setText("Teguran");
		btnTeguran.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnTeguran.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				for(TableColumn col : tableViewer.getTable().getColumns())
					col.dispose();
				createTeguranColumns();
				/*
				 * Cara Pertama
				 */
				/*tableViewer.removeFilter(filter);
				tableViewer.remove(PendaftaranWajibPajakProvider.INSTANCE.getWajibPajak());
				for(TableItem item : tableViewer.getTable().getItems())
					item.dispose();
				tableViewer.refresh();
				tableViewer.setContentProvider(new ArrayContentProvider());
				listTeguran = ControllerFactory.getMainController().getCpWajibPajakDAOImpl().getDaftarTeguran();
				tableViewer.setInput(listTeguran);
				tableViewer.refresh();*/
//				txtJumlahWP.setText(tableViewer.getTable().getItemCount()+"");
				
				/*
				 * Cara Kedua
				 */
				for(TableItem item : tableViewer.getTable().getItems())
					item.dispose();
				if (txtCari.getText().equalsIgnoreCase(""))
					listTeguran = ControllerFactory.getMainController().getCpWajibPajakDAOImpl().getDaftarTeguran();
				else
					listTeguran = ControllerFactory.getMainController().getCpWajibPajakDAOImpl().getDaftarTeguranbyKode(txtCari.getText().toUpperCase());
				//SimpleDateFormat bulanFormat = new SimpleDateFormat("MM-yyyy");
				//Date bulan = null, bulan2 = new Date();
				//ResultSet cekMasaPajakSampaiLHP;
				for(int i=0; i<listTeguran.size();i++){
					/*if (!listTeguran.get(i).getNamaPemilik().equalsIgnoreCase("-")){
						cekMasaPajakSampaiLHP = ControllerFactory.getMainController().getCpWajibPajakDAOImpl().getMasaPajakSampaiLHP(listTeguran.get(i).getNPWP());
						try {
							if (cekMasaPajakSampaiLHP.getDate(0) == null){
								//System.out.println("cekMasaPajakSampaiLHP try");
								bulan = bulanFormat.parse(listTeguran.get(i).getNamaPemilik());
								System.out.println("bulan: "+bulan);
								System.out.println("cekMasaPajakSampaiLHP: "+cekMasaPajakSampaiLHP.getDate(0));	
							}
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
							//System.out.println("cekMasaPajakSampaiLHP catch");
						}
					}*/
					/*boolean adaSKPDKB = false;
					listPeriksa = (ArrayList<Periksa>) ControllerFactory.getMainController().getCpPeriksaDAOImpl().checkSKPDKB(wp.getNPWP());
					pajakDari = new Date[listPeriksa.size()];
					pajakSampai = new Date[listPeriksa.size()];
					//jenisSKP = new String[listPeriksa.size()];
					if (listPeriksa.size() > 0){
						for (int i1=0;i1<listPeriksa.size();i1++){
							pajakDari[i1] = listPeriksa.get(i1).getMasaPajakDari();
							pajakSampai[i1] = listPeriksa.get(i1).getMasaPajakSampai();
							//jenisSKP[i] = listPeriksa.get(i).getTipeSkp();
						}
					}
					//enable = true;
					
						for (int i1=0;i1<listPeriksa.size();i1++){
							if (pajakSampai[i1].after(listPeriksa.get(i1)) && pajakDari[i1].before(listPeriksa.get(i1)))
							{
								//txtNamaPenyetor.setFocus();
								MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "Warning", "SPTPD tidak bisa disimpan karena telah keluar SKPDKB");
								//enable = false;
								adaSKPDKB = true;
								break;
							}
						}
						*/
					
					
					
					
					PendaftaranWajibPajak wp = listTeguran.get(i);
					SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
					TableItem item = new TableItem(tableViewer.getTable(), SWT.NONE, i);
					item.setText(new String[]{"",wp.getNamaBadan(),wp.getAlabadJalan(),wp.getNamaPemilik(),wp.getNPWP(),
							sdf.format(wp.getTanggalDaftar()),wp.getAlabadKecamatan(),Integer.toString(wp.getIdSubPajak())});
				}
				isTeguran = true;
				disableBtnCetak();
				txtJumlahWP.setText(table.getItemCount()+"");
				String action = "Lihat Daftar Teguran";
				new LogImp().setLog(action, userModul.getDisplayName(), userModul.getSubModul().getNamaModul());
			}
		});
		
		listPencarian = new List(composite, SWT.BORDER | SWT.MULTI);
		listPencarian.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		listPencarian.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		listPencarian.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		listPencarian.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 2, 1));
		String[] listString = new String[]{"Semua","NPWP","Nama Badan","Nama Pemilik","Alamat","Tahun Terbit","Bulan Terbit","Tanggal Terbit","Kecamatan","Kelurahan","Nama Pajak","Bidang Usaha","Status","Insidentil","Keterangan"};
		for(int i = 0; i<listString.length; i++){
			listPencarian.add(listString[i]);
		}
		listPencarian.select(0);
		
		tableViewer = new TableViewer(composite, SWT.BORDER | SWT.FULL_SELECTION | SWT.CHECK | SWT.VIRTUAL);
		table = tableViewer.getTable();
		table.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		table.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE-1, SWT.NORMAL));
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 4, 8));
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				PendaftaranWajibPajak wp = (PendaftaranWajibPajak)tableViewer.getElementAt(table.getSelectionIndex());
				ICommunicationView viewToOpen;
				try {
					viewToOpen = (ICommunicationView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(PendaftaranView.ID);
					viewToOpen.accept(wp);
					viewToOpen.setFocus();
				} catch (PartInitException e1) {
					e1.printStackTrace();
				}
			}
		});
		table.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.keyCode == SWT.CR || e.keyCode == SWT.KEYPAD_CR){
					PendaftaranWajibPajak wp = (PendaftaranWajibPajak)tableViewer.getElementAt(table.getSelectionIndex());
					ICommunicationView viewToOpen;
					try {
						viewToOpen = (ICommunicationView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(PendaftaranView.ID);
						viewToOpen.accept(wp);
						viewToOpen.setFocus();
					} catch (PartInitException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		
		createColumnsNormal();
		
		tableViewer.setContentProvider(new ArrayContentProvider());
		tableViewer.setInput(PendaftaranWajibPajakProvider.INSTANCE.getWajibPajak());
		tableViewer.setComparator(comparator);
		getSite().setSelectionProvider(tableViewer);
		filter = new PendaftaranFilter(listPencarian);
		tableViewer.addFilter(filter);
		
		Label lblPenandaTangan = new Label(composite, SWT.NONE);
		lblPenandaTangan.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 2));
		lblPenandaTangan.setForeground(fontColor);
		lblPenandaTangan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblPenandaTangan.setText("Penanda Tangan");
		
		cmbTandaTangan = new Combo(composite, SWT.NONE);
		cmbTandaTangan.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		cmbTandaTangan.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		cmbTandaTangan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		cmbTandaTangan.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));
		loadPejabat();
		
		btnPlt = new Button(composite, SWT.CHECK);
		btnPlt.setText("plt.");
		new Label(composite, SWT.NONE);
		
		Label lblCetak = new Label(composite, SWT.NONE);
		lblCetak.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		lblCetak.setForeground(fontColor);
		lblCetak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblCetak.setText("Cetak");
		
		Group grpCetak = new Group(composite, SWT.NONE);
		grpCetak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		grpCetak.setLayout(new GridLayout(1, false));
		grpCetak.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 2, 1));
		
		btnDaftarWajibPajak = new Button(grpCetak, SWT.FLAT);
		btnDaftarWajibPajak.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(!ReportAppContext.object.isEmpty())
					ReportAppContext.object.clear();
				ReportAppContext.nameObject = "NPWPDHarian";
				ReportAppContext.name = "Variable";
				
				ReportAppContext.classLoader = CariPendaftaranView.class.getClassLoader();
				Integer count = 0;
				for(int i=0;i<table.getItemCount();i++){
					if(table.getItem(i).getChecked()){
						java.util.List<String> values = new ArrayList<String>();
						PendaftaranWajibPajak wp = (PendaftaranWajibPajak) tableViewer.getElementAt(i);
						values.add("973.SI/Peng/" + wp.getNoPendaftaran());
						values.add(wp.getAlabadKecamatan());
						values.add(wp.getAlabadKelurahan());
						values.add(wp.getNamaPajak());
						values.add(wp.getNPWP());
						values.add(wp.getNamaBadan());
						values.add(wp.getAlabadJalan());
						values.add(wp.getNamaPemilik());
						values.add(wp.getNamaBidangUsaha());
						values.add(wp.getJenisPajak());
						values.add(wp.getJenisAssesment());
						SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
						values.add(sdf.format(wp.getTanggalDaftar()));
						if (wp.getKeteranganTutup() == null || wp.getKeteranganTutup() == "")
							values.add(wp.getKeteranganTutup());
						else
							values.add(wp.getKeterangan());
						values.add(wp.getStatus());
						values.add(wp.getNoBuktiDiri());
						values.add(wp.getAlatingJalan());
						ReportAppContext.object.put(count.toString(), values);
						count++;
					}
				}
				
				ReportAppContext.map.put("TanggalDaftar1", txtCari.getText());
				ReportAppContext.map.put("TotalNPWPD", ""+count);
				ReportAction.start("DaftarNPWPDHarian.rptdesign");
			}
		});
		btnDaftarWajibPajak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnDaftarWajibPajak.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnDaftarWajibPajak.setText("Daftar Wajib Pajak");
		
		btnSuratKeputusanNpwp = new Button(grpCetak, SWT.FLAT);
		btnSuratKeputusanNpwp.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (isPrint()){
					if (!cmbTandaTangan.getText().equalsIgnoreCase("")){
						if(!ReportAppContext.object.isEmpty())
							ReportAppContext.object.clear();
						ReportAppContext.nameObject = "SKNPWPD";
						ReportAppContext.name = "Variable";
						Pejabat p = (Pejabat)cmbTandaTangan.getData(cmbTandaTangan.getText());
						ReportAppContext.map.put("Nama", p.getNamaPejabat());
						ReportAppContext.map.put("Pangkat", p.getPangkat());
						ReportAppContext.map.put("NIP", p.getIdPejabatNIP());
						ReportAppContext.classLoader = CariPendaftaranView.class.getClassLoader();
						Integer count = 0;
						for(int i=0;i<table.getItemCount();i++){
							if(table.getItem(i).getChecked()){
								java.util.List<String> values = new ArrayList<String>();
								PendaftaranWajibPajak wp = (PendaftaranWajibPajak) tableViewer.getElementAt(i);
								values.add(wp.getNoPendaftaran());
								values.add(wp.getNamaPemilik());
								values.add(wp.getNamaBadan());
								values.add(wp.getAlabadJalan());
								values.add(getNPWPDFormat(wp.getNPWP()));
								values.add(wp.getAlatingJalan());
								values.add(wp.getNamaPajak());
								SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
								values.add(sdf.format(wp.getTanggalDaftar()));
								java.util.List <UUwp> listuu = ControllerFactory.getMainController().getCpWajibPajakDAOImpl().getUUWajibPajak(Integer.parseInt(wp.getKodePajak()));
								String uu = "";
								int k = 7;
								Iterator<UUwp> iterate = listuu.iterator();
								while(iterate.hasNext()){
									UUwp uuwp = iterate.next();
									uu += k+".  "+uuwp.getUu()+"\n";
									k++;
								}
								values.add(uu);
								ReportAppContext.object.put(count.toString(), values);
								count++;
							}
						}
						if (btnPlt.getSelection()==true){
							ReportAction.start("SKNPWPDPLT.rptdesign");
						}
						else{
							ReportAction.start("SKNPWPD.rptdesign");
						}
						
					}else{
						MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Silahkan isi Nama Penandatangan");
						cmbTandaTangan.setFocus();
					}
				}
			}
		});
		btnSuratKeputusanNpwp.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnSuratKeputusanNpwp.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnSuratKeputusanNpwp.setText("Surat Keputusan NPWPD");
		
		btnKartuNpwpd = new Button(grpCetak, SWT.FLAT);
		btnKartuNpwpd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (cmbTandaTangan.getText().equalsIgnoreCase("")){
					MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Silahkan isi Nama Penandatangan");
					cmbTandaTangan.setFocus();
				}
				else{
					if(!ReportAppContext.object.isEmpty())
						ReportAppContext.object.clear();
					ReportAppContext.name = "Variable";
					ReportAppContext.nameObject = "KartuNPWPD";
					Pejabat p = (Pejabat)cmbTandaTangan.getData(cmbTandaTangan.getText());
					ReportAppContext.map.put("Nama", p.getNamaPejabat());
					ReportAppContext.map.put("NIP", p.getIdPejabatNIP());
					String Kepala = "Kepala Badan Pengelola Pajak dan Retribusi Daerah Kota Medan";
					
					if (btnPlt.getSelection()==true){
						Kepala = "Plt. "+Kepala;
					}
					ReportAppContext.map.put("Kepala", Kepala);
					ReportAppContext.classLoader = CariPendaftaranView.class.getClassLoader();
					Integer count = 0;
					for(int i=0;i<table.getItemCount();i++){
						if(table.getItem(i).getChecked()){
							java.util.List<String> values = new ArrayList<String>();
							PendaftaranWajibPajak wp = (PendaftaranWajibPajak) tableViewer.getElementAt(i);
							values.add("973.SI/Peng/" + wp.getNoPendaftaran());
							values.add(wp.getNamaBadan());
							values.add(wp.getNamaPemilik());
							values.add(wp.getAlabadJalan());
							values.add(getNPWPDFormat(wp.getNPWP()));
							SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
							values.add(sdf.format(wp.getTanggalDaftar()));
							values.add(p.getNamaPejabat());
							values.add(p.getIdPejabatNIP());
							ReportAppContext.object.put(count.toString(), values);
							count++;
						}
					}
					ReportAction.start("KartuNPWPD.rptdesign");
				}
			}
		});
		btnKartuNpwpd.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnKartuNpwpd.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnKartuNpwpd.setText("Kartu NPWPD");
		
		btnKartuNpwpdCopy = new Button(grpCetak, SWT.FLAT);
		btnKartuNpwpdCopy.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (cmbTandaTangan.getText().equalsIgnoreCase("")){
					MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Silahkan isi Nama Penandatangan");
					cmbTandaTangan.setFocus();
				}
				else{
					if(!ReportAppContext.object.isEmpty())
						ReportAppContext.object.clear();
					ReportAppContext.name = "Variable";
					ReportAppContext.nameObject = "KartuNPWPD";
					Pejabat p = (Pejabat)cmbTandaTangan.getData(cmbTandaTangan.getText());
					String Kepala = "Kepala Badan Pengelola Pajak dan Retribusi Daerah Kota Medan";	
					if (btnPlt.getSelection()==true){
						Kepala = "Plt. "+Kepala;
					}
					ReportAppContext.map.put("Kepala", Kepala);
					ReportAppContext.map.put("Nama", p.getNamaPejabat());
					ReportAppContext.map.put("NIP", p.getIdPejabatNIP());
					ReportAppContext.classLoader = CariPendaftaranView.class.getClassLoader();
					Integer count = 0;
					for(int i=0;i<table.getItemCount();i++){
						if(table.getItem(i).getChecked()){
							java.util.List<String> values = new ArrayList<String>();
							PendaftaranWajibPajak wp = (PendaftaranWajibPajak) tableViewer.getElementAt(i);
							values.add("973.SI/Peng/" + wp.getNoPendaftaran());
							values.add(wp.getNamaBadan());
							values.add(wp.getNamaPemilik());
							values.add(wp.getAlabadJalan());
							values.add(getNPWPDFormat(wp.getNPWP()));
							SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
							values.add(sdf.format(wp.getTanggalDaftar()));
							values.add(p.getNamaPejabat());
							values.add(p.getIdPejabatNIP());
							ReportAppContext.object.put(count.toString(), values);
							count++;
						}
					}
					ReportAction.start("KartuNPWPDCopy.rptdesign");
				}
			}
		});
		btnKartuNpwpdCopy.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnKartuNpwpdCopy.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnKartuNpwpdCopy.setText("Kartu NPWPD Copy");
		
		btnFormulirPendaftaran = new Button(grpCetak, SWT.FLAT);
		btnFormulirPendaftaran.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(!ReportAppContext.object.isEmpty())
					ReportAppContext.object.clear();
				
				ReportAppContext.name = "Variable";
				ReportAppContext.nameObject = "FormulirPendaftaranBadan";
				Pejabat p = (Pejabat)cmbTandaTangan.getData(cmbTandaTangan.getText());
				ReportAppContext.map.put("Nama", p.getNamaPejabat());
				ReportAppContext.map.put("NIP", p.getIdPejabatNIP());
				ReportAppContext.classLoader = CariPendaftaranView.class.getClassLoader();
				Integer count = 0;
				for(int i=0;i<table.getItemCount();i++){
					if(table.getItem(i).getChecked()){
						java.util.List<String> values = new ArrayList<String>();
						PendaftaranWajibPajak wp = (PendaftaranWajibPajak) tableViewer.getElementAt(i);
						values.add(wp.getNoPendaftaran());
						values.add(wp.getNamaBadan());
						values.add(wp.getNamaPemilik());
						values.add(wp.getAlabadJalan());
						values.add(wp.getNPWP());
						SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
						values.add(sdf.format(wp.getTanggalDaftar()));
						values.add(p.getNamaPejabat());
						values.add(p.getIdPejabatNIP());
						ReportAppContext.object.put(count.toString(), values);
						count++;
					}
				}
				ReportAction.start("KartuNPWPD.rptdesign"); 
			}
		});
		btnFormulirPendaftaran.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnFormulirPendaftaran.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnFormulirPendaftaran.setText("Formulir Pendaftaran");
		
		btnSuratTeguran = new Button(grpCetak, SWT.FLAT);
		btnSuratTeguran.addSelectionListener(new SelectionAdapter() {
			@SuppressWarnings("deprecation")
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (isPrint()){
					if (!cmbTandaTangan.getText().equalsIgnoreCase("")){
						if(!ReportAppContext.object.isEmpty())
							ReportAppContext.object.clear();
						
						String bulanSPTPD = "";
						Date bulan = new Date();
						Date bulanSampai = new Date();
						SimpleDateFormat sdf = new SimpleDateFormat("MMMM yyyy");
						SimpleDateFormat bulanFormat = new SimpleDateFormat("MM-yyyy");
						ReportAppContext.name = "Variable";
						Calendar cal = Calendar.getInstance();
					    cal.setTime(dateNow);
					    cal.add(Calendar.MONTH, -2);
					    bulanSampai = cal.getTime();
					    String [] bln = {"Januari","Februari","Maret","April","Mei","Juni","Juli","Agustus","September","Oktober","November","Desember"};
						ReportAppContext.name = "Variable";
						ReportAppContext.map.put("Tahun", String.valueOf(bln[dateNow.getMonth()]+" "+(dateNow.getYear() + 1900)));
						Pejabat p = (Pejabat)cmbTandaTangan.getData(cmbTandaTangan.getText());
						ReportAppContext.map.put("Nama", p.getNamaPejabat());
						ReportAppContext.map.put("Pangkat", p.getPangkat());
						ReportAppContext.map.put("NIP", p.getIdPejabatNIP());
						if (btnPlt.getSelection() == true){
							ReportAppContext.map.put("Jabatan", "Plt. "+p.getJabatan());
						}
						else{
							ReportAppContext.map.put("Jabatan", p.getJabatan());
						}
						
						
						ReportAppContext.nameObject = "SuratTeguran";
						ReportAppContext.classLoader = CariPendaftaranView.class.getClassLoader();
						Integer count = 0;
						for(int i=0;i<table.getItemCount();i++){
							if(table.getItem(i).getChecked()){
								try {
									if (!listTeguran.get(i).getNamaPemilik().equalsIgnoreCase("-"))
										bulan = bulanFormat.parse(listTeguran.get(i).getNamaPemilik());
									else{
										bulan = listTeguran.get(i).getTanggalDaftar();
										cal.setTime(bulan);
										cal.add(Calendar.MONTH, -1);
										bulan = cal.getTime();
									}
								} catch (ParseException e1) {
									e1.printStackTrace();
								}
								
								if (UIController.INSTANCE.getMonthDiff(bulan, dateNow) == 3){
									cal.setTime(bulan);
									cal.add(Calendar.MONTH, 1);
									bulan = cal.getTime();
									bulanSPTPD = sdf.format(bulan);
								}else{
									cal.setTime(bulan);
									cal.add(Calendar.MONTH, 1);
									bulan = cal.getTime();
									if (bulan.getYear() == bulanSampai.getYear()){
										bulanSPTPD = UIController.INSTANCE.formatMonth(bulan.getMonth() + 1, Locale.getDefault()) + " - " + sdf.format(bulanSampai);
									}else{
										bulanSPTPD = sdf.format(bulan) + " - " + sdf.format(bulanSampai);
									}
								}
	//							if(table.getItem(i).getChecked()){
									java.util.List<String> values = new ArrayList<String>();
	//								PendaftaranWajibPajak wp = (PendaftaranWajibPajak) tableViewer.getElementAt(i);
									values.add(new StringBuilder(listTeguran.get(i).getNPWP()).insert(1, "-").insert(9, "-").insert(12, "-").toString());
									values.add(listTeguran.get(i).getNamaBadan());
									values.add(listTeguran.get(i).getAlabadJalan());
									values.add(bulanSPTPD);
									//System.out.println(bulanSPTPD);
									ReportAppContext.object.put(count.toString(), values);
									count++;
							}
						}
						ReportAction.start("SuratTeguran.rptdesign");
					}else{
						MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Silahkan isi Nama Penandatangan");
						cmbTandaTangan.setFocus();
					}
				}
			}
		});
		btnSuratTeguran.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnSuratTeguran.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnSuratTeguran.setText("Surat Teguran");
		
		btnDaftarTeguran = new Button(grpCetak, SWT.FLAT);
		btnDaftarTeguran.addSelectionListener(new SelectionAdapter() {
			@SuppressWarnings("deprecation")
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(isPrint()){
					if(!ReportAppContext.object.isEmpty())
						ReportAppContext.object.clear();
//					new LogImp().setLog("", "");
					String bulanSPTPD = "";
					Date bulan = new Date();
					Date bulanSampai = new Date();
					SimpleDateFormat sdf = new SimpleDateFormat("MMMM yyyy");
					SimpleDateFormat bulanFormat = new SimpleDateFormat("MM-yyyy");
					ReportAppContext.name = "Variable";
					Calendar cal = Calendar.getInstance();
				    cal.setTime(dateNow);
				    cal.add(Calendar.MONTH, -2);
				    bulanSampai = cal.getTime();
					ReportAppContext.map.put("MasaPajak", sdf.format(cal.getTime()));
					ReportAppContext.nameObject = "DaftarTeguran";
					ReportAppContext.classLoader = CariPendaftaranView.class.getClassLoader();
					Integer count = 0;
									
					for(int i=0;i<table.getItemCount();i++){
						if(table.getItem(i).getChecked()){
							try {
								if (!listTeguran.get(i).getNamaPemilik().equalsIgnoreCase("-"))
									bulan = bulanFormat.parse(listTeguran.get(i).getNamaPemilik());
								else{
									bulan = listTeguran.get(i).getTanggalDaftar();
									cal.setTime(bulan);
									cal.add(Calendar.MONTH, -1);
									bulan = cal.getTime();
								}
							} catch (ParseException e1) {
								e1.printStackTrace();
							}
							
							if (UIController.INSTANCE.getMonthDiff(bulan, dateNow) == 3){
								cal.setTime(bulan);
								cal.add(Calendar.MONTH, 1);
								bulan = cal.getTime();
								bulanSPTPD = sdf.format(bulan);
							}else{
								cal.setTime(bulan);
								cal.add(Calendar.MONTH, 1);
								bulan = cal.getTime();
								if (bulan.getYear() == bulanSampai.getYear()){
									bulanSPTPD = UIController.INSTANCE.formatMonth(bulan.getMonth() + 1, Locale.getDefault()) + " - " + sdf.format(bulanSampai);
								}else{
									bulanSPTPD = sdf.format(bulan) + " - " + sdf.format(bulanSampai);
								}
							}
								
							java.util.List<String> values = new ArrayList<String>();
	//							PendaftaranWajibPajak wp = (PendaftaranWajibPajak) tableViewer.getElementAt(i);
							values.add(listTeguran.get(i).getNPWP());
							values.add(listTeguran.get(i).getNamaBadan());
							values.add(listTeguran.get(i).getAlabadJalan());
							values.add(bulanSPTPD);	
							values.add(listTeguran.get(i).getAlabadKecamatan());
							values.add(listTeguran.get(i).getIdSubPajak().toString());
							ReportAppContext.object.put(count.toString(), values);
							count++;
						}
					}
					ReportAppContext.map.put("TotalWP", count.toString());
					ReportAction.start("Daftar_Teguran.rptdesign");
				}
			}
		});
		btnDaftarTeguran.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnDaftarTeguran.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnDaftarTeguran.setText("Daftar Teguran");
		
		btnAmplop = new Button(grpCetak, SWT.FLAT);
		btnAmplop.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				StringBuffer sb = new StringBuffer();
				sb.append("Print Amplop dari Modul Pendaftaran-CariPendaftaranView ");
				if(!ReportAppContext.object.isEmpty())
					ReportAppContext.object.clear();
				ReportAppContext.nameObject = "Amplop";
				ReportAppContext.classLoader = CariPendaftaranView.class.getClassLoader();
				Integer count = 0;
				for(int i=0;i<table.getItemCount();i++){
//					if(table.getItem(i).getChecked()){
						java.util.List<String> values = new ArrayList<String>();
//						PendaftaranWajibPajak wp = (PendaftaranWajibPajak) tableViewer.getElementAt(i);
						values.add(listTeguran.get(i).getNamaBadan());
//						sb.append("\n Nama Badan : "+wp.getNamaBadan());
						values.add(listTeguran.get(i).getAlabadJalan());
//						sb.append("\n Alamat Badan : "+wp.getAlabadJalan());
						ReportAppContext.object.put(count.toString(), values);
						count++;
//					}
				}
				new LogImp().setLog(sb.toString(), DBConnection.INSTANCE.getDisplayName(), userModul.getSubModul().getNamaModul());
				ReportAction.start("Amplop.rptdesign");
			}
		});
		btnAmplop.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnAmplop.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnAmplop.setText("Amplop");
		
		Composite composite_1 = new Composite(composite, SWT.NONE);
		composite_1.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 2, 1));
		composite_1.setLayout(new GridLayout(1, false));
		
		Label lblJumlahWajibPajak = new Label(composite_1, SWT.NONE);
		lblJumlahWajibPajak.setText("Jumlah Wajib Pajak");
		lblJumlahWajibPajak.setForeground(fontColor);
		lblJumlahWajibPajak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		txtJumlahWP = new Text(composite_1, SWT.BORDER | SWT.CENTER);
		txtJumlahWP.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		txtJumlahWP.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtJumlahWP.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtJumlahWP.setEditable(false);
		
		if(GlobalVariable.userModul.getDisplayName().equalsIgnoreCase("Admin")){
			Menu menu = new Menu(parent);
			table.setMenu(menu);
			MenuItem itemEdit = new MenuItem(menu, SWT.DROP_DOWN);
			itemEdit.setText("EDIT");
			itemEdit.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					PendaftaranWajibPajak wp = (PendaftaranWajibPajak)tableViewer.getElementAt(table.getSelectionIndex());
					ICommunicationView viewToOpen;
					try {
						viewToOpen = (ICommunicationView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(PendaftaranView.ID);
						viewToOpen.accept(wp);
						viewToOpen.setFocus();
					} catch (PartInitException e1) {
						e1.printStackTrace();
					}
				}
			});
			MenuItem itemHapus = new MenuItem(menu,SWT.DROP_DOWN);
			itemHapus.setText("HAPUS");
			itemHapus.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					PendaftaranWajibPajak wp = (PendaftaranWajibPajak)tableViewer.getElementAt(table.getSelectionIndex());
					if(ControllerFactory.getMainController().getCpWajibPajakDAOImpl().deleteWajibPajak(wp.getNPWP())){
						MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Data berhasil di HAPUS.");
						tableViewer.setInput(PendaftaranWajibPajakProvider.INSTANCE.getWajibPajak());
						tableViewer.refresh();
					}
				}
			});
		}else{
			Menu menu = new Menu(parent);
			table.setMenu(menu);
			MenuItem itemEdit = new MenuItem(menu, SWT.DROP_DOWN);
			itemEdit.setText("EDIT");
			itemEdit.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					PendaftaranWajibPajak wp = (PendaftaranWajibPajak)tableViewer.getElementAt(table.getSelectionIndex());
					ICommunicationView viewToOpen;
					try {
						viewToOpen = (ICommunicationView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(PendaftaranView.ID);
						viewToOpen.accept(wp);
						viewToOpen.setFocus();
					} catch (PartInitException e1) {
						e1.printStackTrace();
					}
				}
			});
		}
		
		scrolledComposite.setContent(composite);
		scrolledComposite.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}

	private void createColumnsNormal() {
		String[] titles = { "No.", "Nama Kecamatan", "Nama Kelurahan", "Nama Pajak", "NPWPD", "Nama Badan", "Nama Pemilik", "Alamat Badan","Jenis Pajak","Assement","Tanggal Daftar"
				,"Kode Bidang Usaha","Nama Bidang","No. Pendaftaran","Kode Pajak","Alamat Tinggal","No Bukti Diri","Status", "Keterangan"};
	    int[] bounds = { 110, 135, 110, 135, 150, 150, 180, 180, 160, 170, 190, 180, 180, 180, 190, 150, 150, 100, 200};
	    
	    TableViewerColumn tableViewerColumn = createTableViewerColumn(titles[0], bounds[0], 0);
		tableViewerColumn.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				PendaftaranWajibPajak antri = (PendaftaranWajibPajak) element;
				return Integer.toString(antri.getNoUrut());
			}
		});

		TableViewerColumn tableViewerColumn_1 = createTableViewerColumn(titles[1], bounds[1], 1);
		tableViewerColumn_1.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				PendaftaranWajibPajak antri = (PendaftaranWajibPajak) element;
				return antri.getAlabadKecamatan();
			}
		});
		
		TableViewerColumn tableViewerColumn_2 = createTableViewerColumn(titles[2], bounds[2], 2);
		tableViewerColumn_2.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				PendaftaranWajibPajak antri = (PendaftaranWajibPajak) element;
				return antri.getAlabadKelurahan();
			}
		});
		
		TableViewerColumn tableViewerColumn_4 = createTableViewerColumn(titles[3], bounds[3], 3);
		tableViewerColumn_4.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				PendaftaranWajibPajak antri = (PendaftaranWajibPajak) element;
				return antri.getNamaPajak();
			}
		});
		
		TableViewerColumn tableViewerColumn_5 = createTableViewerColumn(titles[4], bounds[4], 4);
		tableViewerColumn_5.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				PendaftaranWajibPajak antri = (PendaftaranWajibPajak) element;
				return antri.getNPWP();
			}
		});
		
		TableViewerColumn tableViewerColumn_6 = createTableViewerColumn(titles[5], bounds[5], 5);
		tableViewerColumn_6.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				PendaftaranWajibPajak antri = (PendaftaranWajibPajak) element;
				return antri.getNamaBadan();
			}
		});
		
		TableViewerColumn tableViewerColumn_3 = createTableViewerColumn(titles[6], bounds[6], 6);
		tableViewerColumn_3.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				PendaftaranWajibPajak antri = (PendaftaranWajibPajak) element;
				return antri.getNamaPemilik();
			}
		});
		
		TableViewerColumn tableViewerColumn_7 = createTableViewerColumn(titles[7], bounds[7], 7);
		tableViewerColumn_7.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				PendaftaranWajibPajak antri = (PendaftaranWajibPajak) element;
				return antri.getAlabadJalan();
			}
		});
		
		TableViewerColumn tableViewerColumn_8 = createTableViewerColumn(titles[8], bounds[8], 8);
		tableViewerColumn_8.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				PendaftaranWajibPajak antri = (PendaftaranWajibPajak) element;
				return antri.getJenisPajak();
			}
		});
		
		TableViewerColumn tableViewerColumn_9 = createTableViewerColumn(titles[9], bounds[9], 9);
		tableViewerColumn_9.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				PendaftaranWajibPajak antri = (PendaftaranWajibPajak) element;
				return antri.getJenisAssesment();
			}
		});
		
		TableViewerColumn tableViewerColumn_10 = createTableViewerColumn(titles[10], bounds[10], 10);
		tableViewerColumn_10.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				PendaftaranWajibPajak antri = (PendaftaranWajibPajak) element;
				SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
				return sdf.format(antri.getTanggalDaftar());
			}
		});
		
		TableViewerColumn tableViewerColumn_11 = createTableViewerColumn(titles[11], bounds[11], 11);
		tableViewerColumn_11.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				PendaftaranWajibPajak antri = (PendaftaranWajibPajak) element;
				return antri.getBidangUsaha();
			}
		});
		
		TableViewerColumn tableViewerColumn_12 = createTableViewerColumn(titles[12], bounds[12], 12);
		tableViewerColumn_12.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				PendaftaranWajibPajak antri = (PendaftaranWajibPajak) element;
				return antri.getNamaBidangUsaha();
			}
		});
		
		TableViewerColumn tableViewerColumn_13 = createTableViewerColumn(titles[13], bounds[13], 13);
		tableViewerColumn_13.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				PendaftaranWajibPajak antri = (PendaftaranWajibPajak) element;
				return antri.getNoPendaftaran();
			}
		});
		
		TableViewerColumn tableViewerColumn_14 = createTableViewerColumn(titles[14], bounds[14], 14);
		tableViewerColumn_14.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				PendaftaranWajibPajak antri = (PendaftaranWajibPajak) element;
				return antri.getKodePajak();
			}
		});
		
		TableViewerColumn tableViewerColumn_15 = createTableViewerColumn(titles[15], bounds[15], 15);
		tableViewerColumn_15.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				PendaftaranWajibPajak antri = (PendaftaranWajibPajak) element;
				return antri.getAlatingJalan();
			}
		});
		
		TableViewerColumn tableViewerColumn_16 = createTableViewerColumn(titles[16], bounds[16], 16);
		tableViewerColumn_16.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				PendaftaranWajibPajak antri = (PendaftaranWajibPajak) element;
				return antri.getNoBuktiDiri();
			}
		});
		
		TableViewerColumn tableViewerColumn_17 = createTableViewerColumn(titles[17], bounds[17], 17);
		tableViewerColumn_17.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				PendaftaranWajibPajak antri = (PendaftaranWajibPajak) element;
				return antri.getStatus();
			}
		});
		
		TableViewerColumn tableViewerColumn_18 = createTableViewerColumn(titles[18], bounds[18], 18);
		tableViewerColumn_18.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				PendaftaranWajibPajak antri = (PendaftaranWajibPajak) element;
				return antri.getKeterangan();
			}
		});
	}
	
	private void createTeguranColumns(){
		String[] titles = { "No.", "Nama Badan", "Alamat Badan", "SPTPD Terakhir", "NPWPD", "Tanggal Daftar", "Kecamatan", "UPT"};
	    int[] bounds = { 60, 135, 110, 135, 150, 150, 180, 180};
	    
	    TableViewerColumn tableViewerColumn = createTableViewerColumn(titles[0], bounds[0], 0);
		tableViewerColumn.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				PendaftaranWajibPajak antri = (PendaftaranWajibPajak) element;
				return Integer.toString(0);
			}
		});

		TableViewerColumn tableViewerColumn_1 = createTableViewerColumn(titles[1], bounds[1], 1);
		tableViewerColumn_1.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				PendaftaranWajibPajak antri = (PendaftaranWajibPajak) element;
				return antri.getNamaBadan();
			}
		});
		
		TableViewerColumn tableViewerColumn_2 = createTableViewerColumn(titles[2], bounds[2], 2);
		tableViewerColumn_2.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				PendaftaranWajibPajak antri = (PendaftaranWajibPajak) element;
				return antri.getAlabadJalan();
			}
		});
		
		TableViewerColumn tableViewerColumn_4 = createTableViewerColumn(titles[3], bounds[3], 3);
		tableViewerColumn_4.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				PendaftaranWajibPajak antri = (PendaftaranWajibPajak) element;
				return antri.getNamaPemilik();
			}
		});
		
		TableViewerColumn tableViewerColumn_5 = createTableViewerColumn(titles[4], bounds[4], 4);
		tableViewerColumn_5.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				PendaftaranWajibPajak antri = (PendaftaranWajibPajak) element;
				return antri.getNPWP();
			}
		});
		
		TableViewerColumn tableViewerColumn_6 = createTableViewerColumn(titles[5], bounds[5], 5);
		tableViewerColumn_6.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				PendaftaranWajibPajak antri = (PendaftaranWajibPajak) element;
				SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
				return sdf.format(antri.getTanggalDaftar());
			}
		});
		TableViewerColumn tableViewerColumn_3 = createTableViewerColumn(titles[6], bounds[6], 6);
		tableViewerColumn_3.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				PendaftaranWajibPajak antri = (PendaftaranWajibPajak) element;
				return antri.getAlabadKecamatan();
			}
		});
		
		TableViewerColumn tableViewerColumn_7 = createTableViewerColumn(titles[7], bounds[7], 7);
		tableViewerColumn_7.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				PendaftaranWajibPajak antri = (PendaftaranWajibPajak) element;
				return Integer.toString(antri.getIdSubPajak());
			}
		});
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
//		column.addSelectionListener(getSelectionAdapter(column, colNumber));
		if(colNumber == 0){
			column.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					boolean checkBoxFlag = false;
			        for (int i = 0; i < table.getItemCount(); i++)
			        	if (table.getItems()[i].getChecked()) 
			        		checkBoxFlag = true;
			            
			        if (checkBoxFlag) 
			            for (int m = 0; m < table.getItemCount(); m++) {
			                table.getItems()[m].setChecked(false);
			                table.deselectAll();
			            }
			        else 
			            for (int m = 0; m < table.getItemCount(); m++) {
			                table.getItems()[m].setChecked(true);
			                table.selectAll();
			            }
				}
			});
		}
		return viewerColumn;
	}
	
	/*private SelectionAdapter getSelectionAdapter(final TableColumn column, final int index) {
		SelectionAdapter selectionAdapter = new SelectionAdapter() {
			@Override
		    public void widgetSelected(SelectionEvent e) {
				comparator.setColumn(index);
		        int dir = comparator.getDirection();
		        tableViewer.getTable().setSortDirection(dir);
		        tableViewer.getTable().setSortColumn(column);
		        tableViewer.refresh();
		    }
		};
		return selectionAdapter;
	}*/
	
	private void loadPejabat(){
		java.util.List<Pejabat>listPejabat = ControllerFactory.getMainController().getCpPejabatDAOImpl().getAllPejabat();
		for (int i=0;i<listPejabat.size();i++){
			UIController.INSTANCE.loadPejabat(cmbTandaTangan, listPejabat.toArray());
		}
	}
	
	private UserModul userModul = ControllerFactory.getMainController().getCpUserDAOImpl()
			.getHMUserModul(GlobalVariable.userModul.getIdUser()).get(3); // 1 merupakan id sub modul.
	private Text txtJumlahWP;
	private Button btnBantuan;
	private Button btnKartuNpwpdCopy;
	private Button btnPlt;
	
	private boolean isPrint(){		
		return userModul.getCetak();
	}

	private String getNPWPDFormat(String npwpd){
		String retValue = "";
		for (int i=0;i<npwpd.length();i++){
			if (i == 0 || i == 7 || i == 9)
				retValue += npwpd.substring(i, i+ 1) + "      ";
			else
				retValue += npwpd.substring(i, i+ 1) + "   ";
		}
		
		return retValue;
	}
	
	private void disableBtnCetak(){
		btnAmplop.setEnabled(isTeguran);
		btnDaftarTeguran.setEnabled(isTeguran);
		btnSuratTeguran.setEnabled(isTeguran);
		btnDaftarWajibPajak.setEnabled(!isTeguran);
		btnFormulirPendaftaran.setEnabled(!isTeguran);
		btnKartuNpwpd.setEnabled(!isTeguran);
		btnSuratKeputusanNpwp.setEnabled(!isTeguran);
	}
}