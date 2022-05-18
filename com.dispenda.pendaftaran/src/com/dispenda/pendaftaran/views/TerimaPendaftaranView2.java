package com.dispenda.pendaftaran.views;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.SWTResourceManager;

import com.dispenda.controller.ControllerFactory;
import com.dispenda.dao.LogImp;
import com.dispenda.model.PendaftaranAntrian;
import com.dispenda.model.PendaftaranAntrianProvider;
import com.dispenda.model.PendaftaranSuratIzinUsaha;
import com.dispenda.model.PendaftaranWajibPajak;
import com.dispenda.model.PendaftaranWajibPajakProvider;
import com.dispenda.model.UserModul;
import com.dispenda.object.GlobalVariable;
import com.dispenda.object.Preferences;
import com.dispenda.pendaftaran.PendaftaranActivator;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.hsqldb.jdbc.JDBCArrayBasic;

public class TerimaPendaftaranView2 extends ViewPart {
	public TerimaPendaftaranView2() {
	}
	
	public static final String ID = TerimaPendaftaranView2.class.getName();
	private Table table;
	private TableViewer tableViewer;
	private TableViewer tableViewerTerima;
	private Table tableTerima;
	private TableViewer tableViewerTolak;
	private Table tableTolak;
	private Color fontColor = new Color(Display.getCurrent(),Preferences.FONT_COLOR);

	public void createPartControl(Composite parent) {
		ScrolledComposite scrolledComposite = new ScrolledComposite(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		
		Composite composite = new Composite(scrolledComposite, SWT.NONE);
		composite.setLayout(new GridLayout(3, false));
		
		Group grpListWP = new Group(composite, SWT.NONE);
		grpListWP.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		grpListWP.setLayout(new GridLayout(1, false));
		grpListWP.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		grpListWP.addPaintListener(new PaintListener() {
			@Override
			public void paintControl(PaintEvent pe) {
				Group grp = (Group) pe.widget;
				pe.gc.setForeground(fontColor);
				pe.gc.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
				pe.gc.drawText("Daftar Antrian", grp.getLocation().x, grp.getLocation().y-5, false);
			}
		});
		
		tableViewer = new TableViewer(grpListWP, SWT.BORDER | SWT.FULL_SELECTION |SWT.MULTI | SWT.VIRTUAL);
		table = tableViewer.getTable();
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				List<PendaftaranAntrian> listAntrian = (List<PendaftaranAntrian>)tableViewer.getInput();
//    			for(int i=0; i<table.getSelectionIndices().length;i++){
    				PendaftaranAntrian antrian = listAntrian.get(table.getSelectionIndex());
    				antrian.setAlabadJalan(antrian.getAlabadJalan());
    				antrian.setAlabadKecamatan(antrian.getAlabadKecamatan());
    				antrian.setAlabadKelurahan(antrian.getAlabadKelurahan());
    				antrian.setIdSubPajak(antrian.getIdSubPajak());
    				antrian.setKewajibanPajak(antrian.getKewajibanPajak());
    				antrian.setNamaBadan(antrian.getNamaBadan());
    				antrian.setNamaPemilik(antrian.getNamaPemilik());
    				antrian.setNoBuktiDiri(antrian.getNoBuktiDiri());
    				antrian.setNoFormulir(antrian.getNoFormulir());
    				antrian.setTandaBuktiDiri(antrian.getTandaBuktiDiri());
    				
    				BrowserPendaftaran brow = new BrowserPendaftaran(Display.getCurrent().getActiveShell(), antrian);
        			brow.open();
    			}
//			}
		});
		Menu menu = new Menu(table);
        MenuItem menuItem1 = new MenuItem(menu, SWT.PUSH);
        menuItem1.setText("Terima");
        menuItem1.setImage(new Image(Display.getCurrent(),PendaftaranActivator.getImageDescriptor("icons/antrian.gif").getImageData()));
        menuItem1.addSelectionListener(new SelectionAdapter() {
        	@SuppressWarnings("unchecked")
			@Override
        	public void widgetSelected(SelectionEvent e) {
        		if(isSave()){
	        		if(table.getSelectionCount()>0){
	        			boolean result = MessageDialog.openConfirm(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Apakah anda yakin ingin menerima wajib pajak ini?");
	        			if(result){
	        				List<PendaftaranAntrian> listAntrian = (List<PendaftaranAntrian>)tableViewer.getInput();
		        			for(int i=0; i<table.getSelectionIndices().length;i++){
		        				PendaftaranAntrian antrian = listAntrian.get(table.getSelectionIndices()[i]);
		        				PendaftaranWajibPajak wajibPajakModel = new PendaftaranWajibPajak();
		        				wajibPajakModel.setAktif(true);
		        				wajibPajakModel.setAlabadJalan(antrian.getAlabadJalan());
		        				wajibPajakModel.setAlabadKecamatan(antrian.getAlabadKecamatan());
		        				wajibPajakModel.setAlabadKelurahan(antrian.getAlabadKelurahan());
		        				wajibPajakModel.setAlabadKodePos(antrian.getAlabadKodePos());
		        				wajibPajakModel.setAlabadTelepon(antrian.getAlabadTelepon());
		        				wajibPajakModel.setAlatingJalan(antrian.getAlatingJalan());
		        				wajibPajakModel.setAlatingKecamatan(antrian.getAlatingKecamatan());
		        				wajibPajakModel.setAlatingKelurahan(antrian.getAlatingKelurahan());
		        				wajibPajakModel.setAlatingKodepos(antrian.getAlatingKodePos());
		        				wajibPajakModel.setAlatingKota(antrian.getAlatingKota());
		        				wajibPajakModel.setAlatingTelepon(antrian.getAlatingTelepon());
		        				
		        				wajibPajakModel.setBidangUsaha(""); // setelah migrasi ke server dispenda hapus KODE_BID_USAHA pada table database
		        			
		        				wajibPajakModel.setEkswp(false);
		        				wajibPajakModel.setIdSubPajak(antrian.getIdSubPajak());
		        				wajibPajakModel.setInsidentil(antrian.getIsidentil());
		        				//wajibPajakModel.setInsidentil_Pemerintah(antrian.getInsidentil_Pemerintah());
		        				wajibPajakModel.setJabatan(antrian.getJabatan());
		        				wajibPajakModel.setJenisAssesment(antrian.getJenisAssesment());
		        				wajibPajakModel.setJenisPajak(antrian.getJenisPajak());
		        				wajibPajakModel.setKeterangan(antrian.getKeterangan());
	//	        				wajibPajakModel.setKeteranganTutup(keterangan_tutup)
		        				wajibPajakModel.setKewajibanPajak(antrian.getKewajibanPajak());
		        				wajibPajakModel.setKewarganegaraan(antrian.getKewarganegaraan());
		        				wajibPajakModel.setNamaBadan(antrian.getNamaBadan());
		        				wajibPajakModel.setNamaPemilik(antrian.getNamaPemilik());
		        				wajibPajakModel.setNoBuktiDiri(antrian.getNoBuktiDiri());
		        				wajibPajakModel.setNoFormulir(antrian.getNoFormulir());
		        				wajibPajakModel.setNoKartuKeluarga(antrian.getNoKartuKeluarga());
		        				wajibPajakModel.setStatus("Aktif");
		        				wajibPajakModel.setTandaBuktiDiri(antrian.getTandaBuktiDiri());
		        				String dateNow = ControllerFactory.getMainController().getCpWajibPajakDAOImpl().getDateNow().toString().split(" ")[0];
		        				wajibPajakModel.setTanggalApprove(dateNow);
		        				wajibPajakModel.setTanggalDaftar(antrian.getTanggalDaftar());
	//	        				wajibPajakModel.setTarif(tarif)
		        				wajibPajakModel.setTutup(false);
		        				
		        				wajibPajakModel.setNoPendaftaran(ControllerFactory.getMainController().getCpWajibPajakDAOImpl().getNoPendaftaran(dateNow.split("-")[0]));
		        				wajibPajakModel.setNPWP(antrian.getKewajibanPajak()+ControllerFactory.getMainController().getCpWajibPajakDAOImpl().getNewNPWPD()
		        						+antrian.getAlabadKecamatan()+antrian.getAlabadKelurahan());
		        				
		        				ControllerFactory.getMainController().getCpWajibPajakDAOImpl().savePendaftaranWajibPajak(wajibPajakModel,null);
		        				saveSuratIzin(antrian, wajibPajakModel.getNPWP());
		        				ControllerFactory.getMainController().getCpDaftarAntrianDAOImpl().deleteAntri(antrian.getIndex());
		        				StringBuffer sb = new StringBuffer();
								sb.append("SAVE TERIMA Wajib Pajak DELETE IdAntri:"+antrian.getIndex()+" NPWP"+antrian.getKewajibanPajak()+ControllerFactory.getMainController().getCpWajibPajakDAOImpl().getNewNPWPD()
		        						+antrian.getAlabadKecamatan()+antrian.getAlabadKelurahan()+" Tanggal Terima:"+dateNow+" Tanggal Daftar:"+antrian.getTanggalDaftar());
								new LogImp().setLog(sb.toString(), userModul.getDisplayName(), userModul.getSubModul().getNamaModul());
		        			}
		        			tableViewer.setInput(PendaftaranAntrianProvider.INSTANCE.refresh());
		        			tableViewer.refresh();
		        			tableViewerTerima.setInput(PendaftaranWajibPajakProvider.INSTANCE.getWajibPajak((Date)ControllerFactory.getMainController().getCpWajibPajakDAOImpl().getDateNow()));
		        			tableViewerTerima.refresh();
	        			}
	        		}
        		}else{
        			MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Anda tidak memiliki hak untuk menyimpan atau merubah data.");
        		}
        	}
		});
        
        MenuItem menuItem2 = new MenuItem(menu, SWT.PUSH);
        menuItem2.setText("Tolak");
        menuItem2.setImage(new Image(Display.getCurrent(),PendaftaranActivator.getImageDescriptor("icons/antrian.gif").getImageData()));
        menuItem2.addSelectionListener(new SelectionAdapter() {
        	@SuppressWarnings("unchecked")
			@Override
        	public void widgetSelected(SelectionEvent e) {
        		if(isSave()){
	        		if(table.getSelectionCount()>0){
	        			boolean result = MessageDialog.openConfirm(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Apakah anda yakin ingin menolak wajib pajak ini?");
	        			if(result){
	        				List<PendaftaranAntrian> listAntrian = (List<PendaftaranAntrian>)tableViewer.getInput();
		        			for(int i=0; i<table.getSelectionIndices().length;i++){
		        				PendaftaranAntrian antrian = listAntrian.get(table.getSelectionIndices()[i]);
		        				antrian.setTolak(true);
		        				antrian.setTanggalTolak(java.sql.Date.valueOf(ControllerFactory.getMainController().getCpWajibPajakDAOImpl().getDateNow().toString().split(" ")[0]));
		        				ControllerFactory.getMainController().getCpDaftarAntrianDAOImpl().savePendaftaranAntrian(antrian);
		        				StringBuffer sb = new StringBuffer();
								sb.append("SAVE TOLAK Wajib Pajak IdAntri:"+antrian.getIndex()+" Tanggal Tolak:"+java.sql.Date.valueOf(ControllerFactory.getMainController().getCpWajibPajakDAOImpl().getDateNow().toString().split(" ")[0])+" Tanggal Daftar:"+antrian.getTanggalDaftar());
								new LogImp().setLog(sb.toString(), userModul.getDisplayName(), userModul.getSubModul().getNamaModul());
		        			}
		        			tableViewer.setInput(PendaftaranAntrianProvider.INSTANCE.refresh());
		        			tableViewer.refresh();
		        			tableViewerTolak.setInput(PendaftaranAntrianProvider.INSTANCE.getAntriTolak());
		        			tableViewerTolak.refresh();
	        			}
	        		}
        		}else{
        			MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Anda tidak memiliki hak untuk menyimpan atau merubah data.");
        		}
        	}
		});
        
        MenuItem menuItemRefresh = new MenuItem(menu, SWT.PUSH);
        menuItemRefresh.setText("Refresh");
        menuItemRefresh.addSelectionListener(new SelectionAdapter() {
        	@Override
        	public void widgetSelected(SelectionEvent e) {
        		tableViewer.setInput(PendaftaranAntrianProvider.INSTANCE.refresh());
    			tableViewer.refresh();
        	}
		});
		table.setMenu(menu);
		table.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE-1, SWT.NORMAL));
		table.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1));
		table.setHeaderVisible(true);
		
		String[] titles = { "No. Antrian", "Tanggal Daftar", "Nama Badan", "Alamat Badan", "Bidang Usaha", "Nama Wajib Pajak"};
	    int[] bounds = { 110, 135,135, 150, 150, 180};
		
		TableViewerColumn tableViewerColumn = createTableViewerColumn(tableViewer, titles[0], bounds[0], 0);
		tableViewerColumn.setLabelProvider(new ColumnLabelProvider(){
			public String getText(Object element) {
				PendaftaranAntrian antri = (PendaftaranAntrian) element;
				return Integer.toString(antri.getIndex());
			}
		});

		TableViewerColumn tableViewerColumn_1 = createTableViewerColumn(tableViewer, titles[1], bounds[1], 1);
		tableViewerColumn_1.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				PendaftaranAntrian antri = (PendaftaranAntrian) element;
				SimpleDateFormat date = new SimpleDateFormat("dd-MM-yyyy");
				return date.format(antri.getTanggalDaftar());
			}
		});
		
		TableViewerColumn tableViewerColumn_4 = createTableViewerColumn(tableViewer, titles[2], bounds[2], 2);
		tableViewerColumn_4.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				PendaftaranAntrian antri = (PendaftaranAntrian) element;
				return antri.getNamaBadan();
			}
		});
		
		TableViewerColumn tableViewerColumn_5 = createTableViewerColumn(tableViewer, titles[3], bounds[3], 3);
		tableViewerColumn_5.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				PendaftaranAntrian antri = (PendaftaranAntrian) element;
				return antri.getAlabadJalan();
			}
		});
		
		TableViewerColumn tableViewerColumn_6 = createTableViewerColumn(tableViewer, titles[4], bounds[4], 4);
		tableViewerColumn_6.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				PendaftaranAntrian antri = (PendaftaranAntrian) element;
				return antri.getNamaBid_Usaha();
			}
		});
		
		TableViewerColumn tableViewerColumn_3 = createTableViewerColumn(tableViewer, titles[5], bounds[5], 5);
		tableViewerColumn_3.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				PendaftaranAntrian antri = (PendaftaranAntrian) element;
				return antri.getNamaPemilik();
			}
		});
		
		tableViewer.setContentProvider(new ArrayContentProvider());
		tableViewer.setInput(PendaftaranAntrianProvider.INSTANCE.getAntri());
//		getSite().setSelectionProvider(tableViewer);
		
		Group grpDaftarTerima = new Group(composite, SWT.NONE);
		grpDaftarTerima.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		grpDaftarTerima.setLayout(new GridLayout(1, false));
		grpDaftarTerima.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		grpDaftarTerima.addPaintListener(new PaintListener() {
			@Override
			public void paintControl(PaintEvent pe) {
				Group grp = (Group) pe.widget;
				pe.gc.setForeground(fontColor);
				pe.gc.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
				pe.gc.drawText("Daftar Terima", 5, grp.getLocation().y-5, false);
			}
		});
		
		tableViewerTerima = new TableViewer(grpDaftarTerima, SWT.BORDER | SWT.FULL_SELECTION |SWT.MULTI);
		tableTerima = tableViewerTerima.getTable();
		tableTerima.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
//				loadDataWajibPajak((PendaftaranAntrian)tableViewer.getElementAt(table.getSelectionIndex()));
			}
		});
		tableTerima.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE-1, SWT.NORMAL));
		tableTerima.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		tableTerima.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1));
		tableTerima.setHeaderVisible(true);
		
		String[] titlesTerima = { "Tanggal Daftar","No. Formulir", "NPWPD", "Nama Badan", "Alamat Badan", "Bidang Usaha", "Nama Wajib Pajak"};
	    int[] boundsTerima = { 110,110, 135,135, 150, 150, 180};
		
		TableViewerColumn tableViewerTerimaColumn = createTableViewerColumn(tableViewerTerima, titlesTerima[0], boundsTerima[0], 0);
		tableViewerTerimaColumn.setLabelProvider(new ColumnLabelProvider(){
			public String getText(Object element) {
				PendaftaranWajibPajak antri = (PendaftaranWajibPajak) element;
				SimpleDateFormat date = new SimpleDateFormat("dd-MM-yyyy");
				return date.format(antri.getTanggalDaftar());
			}
		});
		
		TableViewerColumn tableViewerTerimaColumn_2 = createTableViewerColumn(tableViewerTerima, titlesTerima[1], boundsTerima[1], 1);
		tableViewerTerimaColumn_2.setLabelProvider(new ColumnLabelProvider(){
			public String getText(Object element) {
				PendaftaranWajibPajak antri = (PendaftaranWajibPajak) element;
				return antri.getNoFormulir();
			}
		});

		TableViewerColumn tableViewerTerimaColumn_1 = createTableViewerColumn(tableViewerTerima, titlesTerima[2], boundsTerima[2], 2);
		tableViewerTerimaColumn_1.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				PendaftaranWajibPajak antri = (PendaftaranWajibPajak) element;
				return antri.getNPWP();
			}
		});
		
		TableViewerColumn tableViewerTerimaColumn_4 = createTableViewerColumn(tableViewerTerima, titlesTerima[3], boundsTerima[3], 3);
		tableViewerTerimaColumn_4.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				PendaftaranWajibPajak antri = (PendaftaranWajibPajak) element;
				return antri.getNamaBadan();
			}
		});
		
		TableViewerColumn tableViewerTerimaColumn_5 = createTableViewerColumn(tableViewerTerima, titlesTerima[4], boundsTerima[4], 4);
		tableViewerTerimaColumn_5.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				PendaftaranWajibPajak antri = (PendaftaranWajibPajak) element;
				return antri.getAlabadJalan();
			}
		});
		
		TableViewerColumn tableViewerTerimaColumn_6 = createTableViewerColumn(tableViewerTerima, titlesTerima[5], boundsTerima[5], 5);
		tableViewerTerimaColumn_6.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				PendaftaranWajibPajak antri = (PendaftaranWajibPajak) element;
				return antri.getNamaBidangUsaha();
			}
		});
		
		TableViewerColumn tableViewerTerimaColumn_3 = createTableViewerColumn(tableViewerTerima, titlesTerima[6], boundsTerima[6], 6);
		tableViewerTerimaColumn_3.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				PendaftaranWajibPajak antri = (PendaftaranWajibPajak) element;
				return antri.getNamaPemilik();
			}
		});
		
		tableViewerTerima.setContentProvider(new ArrayContentProvider());
		tableViewerTerima.setInput(PendaftaranWajibPajakProvider.INSTANCE.getWajibPajak((Date)ControllerFactory.getMainController().getCpWajibPajakDAOImpl().getDateNow()));
//		getSite().setSelectionProvider(tableViewerTerima);
		
		Group grpDaftarTolak = new Group(composite, SWT.NONE);
		grpDaftarTolak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		grpDaftarTolak.setLayout(new GridLayout(1, false));
		grpDaftarTolak.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		grpDaftarTolak.addPaintListener(new PaintListener() {
			@Override
			public void paintControl(PaintEvent pe) {
				Group grp = (Group) pe.widget;
				pe.gc.setForeground(fontColor);
				pe.gc.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
				pe.gc.drawText("Daftar Tolak", 5, grp.getLocation().y-5, false);
			}
		});
		
		tableViewerTolak = new TableViewer(grpDaftarTolak, SWT.BORDER | SWT.FULL_SELECTION |SWT.MULTI);
		tableTolak = tableViewerTolak.getTable();
		tableTolak.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
//				loadDataWajibPajak((PendaftaranAntrian)tableViewer.getElementAt(table.getSelectionIndex()));
			}
		});
		tableTolak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE-1, SWT.NORMAL));
		tableTolak.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		tableTolak.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1));
		tableTolak.setHeaderVisible(true);
		
		String[] titlesTolak = { "Tanggal DiTolak","No. Antrian", "Nama Badan", "Alamat Badan", "Bidang Usaha", "Nama Wajib Pajak"};
	    int[] boundsTolak = { 110,110, 135, 150, 150, 180};
		
		TableViewerColumn tableViewerTolakColumn = createTableViewerColumn(tableViewerTolak, titlesTolak[0], boundsTolak[0], 0);
		tableViewerTolakColumn.setLabelProvider(new ColumnLabelProvider(){
			public String getText(Object element) {
				PendaftaranAntrian antri = (PendaftaranAntrian) element;
				SimpleDateFormat date = new SimpleDateFormat("dd-MM-yyyy");
				return date.format(antri.getTanggalTolak());
			}
		});
		
		TableViewerColumn tableViewerTolakColumn_2 = createTableViewerColumn(tableViewerTolak, titlesTolak[1], boundsTolak[1], 1);
		tableViewerTolakColumn_2.setLabelProvider(new ColumnLabelProvider(){
			public String getText(Object element) {
				PendaftaranAntrian antri = (PendaftaranAntrian) element;
				return antri.getIndex().toString();
			}
		});
		
		TableViewerColumn tableViewerTolakColumn_4 = createTableViewerColumn(tableViewerTolak, titlesTolak[2], boundsTolak[2], 2);
		tableViewerTolakColumn_4.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				PendaftaranAntrian antri = (PendaftaranAntrian) element;
				return antri.getNamaBadan();
			}
		});
		
		TableViewerColumn tableViewerTolakColumn_5 = createTableViewerColumn(tableViewerTolak, titlesTolak[3], boundsTolak[3], 3);
		tableViewerTolakColumn_5.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				PendaftaranAntrian antri = (PendaftaranAntrian) element;
				return antri.getAlabadJalan();
			}
		});
		
		TableViewerColumn tableViewerTolakColumn_6 = createTableViewerColumn(tableViewerTolak, titlesTolak[4], boundsTolak[4], 4);
		tableViewerTolakColumn_6.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				PendaftaranAntrian antri = (PendaftaranAntrian) element;
				return antri.getIdSubPajak().toString();
			}
		});
		
		TableViewerColumn tableViewerTolakColumn_3 = createTableViewerColumn(tableViewerTolak, titlesTolak[5], boundsTolak[5], 5);
		tableViewerTolakColumn_3.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				PendaftaranAntrian antri = (PendaftaranAntrian) element;
				return antri.getNamaPemilik();
			}
		});
		
		Button btnRefresh = new Button(grpListWP, SWT.NONE);
		btnRefresh.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				tableViewer.setInput(PendaftaranAntrianProvider.INSTANCE.refresh());
    			tableViewer.refresh();
			}
		});
		btnRefresh.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnRefresh.setText("Refresh..");
		
		tableViewerTolak.setContentProvider(new ArrayContentProvider());
		tableViewerTolak.setInput(PendaftaranAntrianProvider.INSTANCE.getAntriTolak());
		
		scrolledComposite.setContent(composite);
		scrolledComposite.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}

	/*@SuppressWarnings("deprecation")
	protected void loadDataWajibPajak(PendaftaranAntrian antri) {
		txtNoFormulir.setText(antri.getNoFormulir());
		txtNamaBadan.setText(antri.getNamaBadan());
		txtPemilik.setText(antri.getNamaPemilik());
		txtJabatan.setText(antri.getJabatan());
		txtKeterangan.setText(antri.getKeterangan());
		txtJalanBadan.setText(antri.getAlabadJalan());
		txtKodePosBadan.setText(antri.getAlabadKodePos());
		txtTelponBadan.setText(antri.getAlabadTelepon());
//		antri.setAlabadKecamatan((String)cmbKecamatanBadan.getData(cmbKecamatanBadan.getItem((cmbKecamatanBadan.getSelectionIndex()))));
//		antri.setAlabadKelurahan((String)cmbKelurahanBadan.getData(cmbKelurahanBadan.getItem((cmbKelurahanBadan.getSelectionIndex()))));
		txtJalanPribadi.setText(antri.getAlatingJalan());
		txtKotaPribadi.setText(antri.getAlatingKota());
//		antri.setAlatingKecamatan((String)cmbKecamatanPribadi.getData(cmbKecamatanPribadi.getItem((cmbKecamatanPribadi.getSelectionIndex()))));
//		antri.setAlatingKelurahan((String)cmbKelurahanPribadi.getData(cmbKelurahanPribadi.getItem((cmbKelurahanPribadi.getSelectionIndex()))));
		txtKodePosPribadi.setText(antri.getAlatingKodePos());
		txtTelponPribadi.setText(antri.getAlatingTelepon());
		txtNoKK.setText(antri.getNoKartuKeluarga());
		txtBuktiDiri.setText(antri.getNoBuktiDiri());
		
//		antri.setJenisPajak((String)cmbJenisPajak.getData(cmbJenisPajak.getItem(cmbJenisPajak.getSelectionIndex())));
//		antri.setJenisAssesment(cmbJenisAssesment.getItem(cmbJenisAssesment.getSelectionIndex()));
//		antri.setKewajibanPajak((Integer)cmbKewajibanPajak.getData(Integer.toString(cmbKewajibanPajak.getSelectionIndex())));
//		antri.setIdSubPajak((Integer)cmbBidangUsaha.getData(Integer.toString(cmbBidangUsaha.getSelectionIndex())));
		chkInsidentil.setSelection(antri.getIsidentil());
//		antri.setKetReject("");
		
		if(antri.getTandaBuktiDiri().equalsIgnoreCase(btnWni.getText()))
			btnWni.setSelection(true);
		else
			btnWna.setSelection(true);
		
		if(antri.getKewarganegaraan().equalsIgnoreCase(btnKtp.getText()))
			btnKtp.setSelection(true);
		else if(antri.getKewarganegaraan().equalsIgnoreCase(btnPassport.getText()))
			btnPassport.setSelection(true);
		else
			btnSim.setSelection(true);
		
		calTanggalDaftar.setYear(antri.getTanggalDaftar().getYear());
		calTanggalDaftar.setMonth(antri.getTanggalDaftar().getMonth());
		calTanggalDaftar.setDay(antri.getTanggalDaftar().getDay());
	}*/

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}
	
	private TableViewerColumn createTableViewerColumn(TableViewer tv,String title, int bound, int colNumber) {
		TableViewerColumn viewerColumn = new TableViewerColumn(tv,SWT.NONE, colNumber);
		TableColumn column = viewerColumn.getColumn();
		column.setText(title);
		column.setWidth(bound);
		column.setResizable(true);
		column.setMoveable(true);
		return viewerColumn;
	}
	
	private UserModul userModul = ControllerFactory.getMainController().getCpUserDAOImpl()
			.getHMUserModul(GlobalVariable.userModul.getIdUser()).get(2); // 2 merupakan id sub modul.
	
	private boolean isSave(){		
		return userModul.getSimpan();
	}
	
	private boolean saveSuratIzin(PendaftaranAntrian antri, String npwpd){
		Object[] suratIzin;
		try {
			suratIzin = (Object[]) antri.getSuratIzin().getArray();
			Object[] noSurat = (Object[]) antri.getNoSurat().getArray();
			Object[] tglSurat = (Object[]) antri.getTglSurat().getArray();
			/*org.hsqldb.types.Type type = org.hsqldb.types.Type.SQL_VARCHAR_DEFAULT;
			JDBCArrayBasic arrSuratIzin = new JDBCArrayBasic(suratIzin, type);
			JDBCArrayBasic arrNoSurat = new JDBCArrayBasic(noSurat, type);
			JDBCArrayBasic arrTglSurat = new JDBCArrayBasic(tglSurat, type);*/
			/*String[] strSuratIzin = (String[]) antri.getSuratIzin().getArray();
			String[] strNoSurat = (String[]) antri.getNoSurat().getArray();
			String[] strTglSurat = (String[]) antri.getTglSurat().getArray();*/
			for (int i=0;i<suratIzin.length;i++){
				PendaftaranSuratIzinUsaha izinUsaha = new PendaftaranSuratIzinUsaha();
				izinUsaha.setId(null);
				izinUsaha.setNPWP(npwpd);
				izinUsaha.setSuratIzin(suratIzin[i].toString().replace("'", ""));
				izinUsaha.setTanggalSurat(tglSurat[i].toString().replace("'", ""));
				izinUsaha.setNoSurat(noSurat[i].toString().replace("'", ""));
				ControllerFactory.getMainController().getCpSuratIzinUsahaDAOImpl().savePendaftaranSuratIzinUsaha(izinUsaha);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
