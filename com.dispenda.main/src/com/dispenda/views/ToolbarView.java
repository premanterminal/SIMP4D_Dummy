package com.dispenda.views;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.part.ViewPart;

import com.dispenda.angsuran.views.DaftarAngsuranView;
import com.dispenda.angsuran.views.DaftarPermohonanView;
import com.dispenda.angsuran.views.PermohonanView;
import com.dispenda.arsip.views.ArsipTransactionView;
import com.dispenda.arsip.views.ArsipView;
import com.dispenda.arsip.views.ImageBarcodeToDatabaseView;
import com.dispenda.arsip.views.ListBarcodePrintView;
import com.dispenda.arsip.views.VerifikasiDataArsipView;
import com.dispenda.control.views.AwardView;
import com.dispenda.control.views.KecamatanView;
import com.dispenda.control.views.KodefikasiView;
import com.dispenda.control.views.PajakView;
import com.dispenda.control.views.PejabatView;
import com.dispenda.control.views.PermintaanPerubahanView;
import com.dispenda.control.views.PreferencesView;
import com.dispenda.control.views.UserView;
import com.dispenda.controller.ControllerFactory;
import com.dispenda.laporan.views.HistoryWPView;
import com.dispenda.laporan.views.LaporanDendaSSPD;
import com.dispenda.laporan.views.LaporanKeuanganHarianView;
import com.dispenda.laporan.views.LaporanKeuanganView;
import com.dispenda.laporan.views.LaporanPendapatanBulananView;
import com.dispenda.laporan.views.LaporanPiutang;
import com.dispenda.main.MainActivator;
import com.dispenda.model.UserModul;
import com.dispenda.object.GlobalVariable;
import com.dispenda.object.Preferences;
import com.dispenda.pendaftaran.views.CariPendaftaranView;
import com.dispenda.pendaftaran.views.CekLokasiReklame;
import com.dispenda.pendaftaran.views.DataWajibPajakView;
import com.dispenda.pendaftaran.views.PendaftaranView;
import com.dispenda.pendaftaran.views.TerimaPendaftaranView2;
import com.dispenda.realisasi.views.RealisasiHarianKeuanganView;
import com.dispenda.realisasi.views.RealisasiHarianView;
import com.dispenda.realisasi.views.RealisasiPenerimaanSkpdkbView;
import com.dispenda.realisasi.views.RealisasiPenerimaanSptpdView;
import com.dispenda.skpdkb.views.DaftarPemeriksaanKeuanganView;
import com.dispenda.skpdkb.views.DaftarPemeriksaanView;
import com.dispenda.skpdkb.views.InputSKPDLBCovView;
import com.dispenda.skpdkb.views.NotaPerhitunganView;
import com.dispenda.skpdkb.views.PemeriksaanView;
import com.dispenda.skpdkb.views.RekomendasiPeriksaView;
import com.dispenda.skpdkb.views.SKPDKBView;
import com.dispenda.sptpd.views.DaftarSKPDView;
import com.dispenda.sptpd.views.DaftarSPTPDView;
import com.dispenda.sptpd.views.InputSPTPDView;
import com.dispenda.sptpd.views.VerifikasiSPTPDView;
import com.dispenda.sspd.views.AntriView;
import com.dispenda.sspd.views.DaftarSSPDView;
import com.dispenda.sspd.views.SSPDView;
import com.dispenda.tunggakan.views.TunggakanDendaView;
import com.dispenda.tunggakan.views.TunggakanSptpdView;
import com.dispenda.tunggakan.views.TunggakanWpView;

public class ToolbarView extends ViewPart {
	
	public final static String ID = ToolbarView.class.getName();
	private HashMap<Integer, MenuItem> hashMenuItem;
	private Composite composite;
	protected Menu dropMenuPendaftaran;
	protected Menu dropMenuSPTPD;
	protected Menu dropMenuPembayaran;
	protected Menu dropMenuPemeriksaan;
	protected Menu dropMenuAngsuran;
	protected Menu dropMenuTunggakan;
	private Menu dropMenuLaporan;
	private Menu dropMenuRPenerimaan;
	private Menu dropMenuArsip;
	private Menu dropMenuCP;
	/* HIDE :
	 * 1. Menu Laporan :
	 * 		a. Laporan Pendapatan Harian
	 * 2. Menu Realisasi :
	 * 		a. Realisasi Penerimaan SPTPD
	 * 		b. Realisasi Penerimaan
	 * 3. Menu Tunggakan : SEMUA
	 * 
	 * */ 
	private static final Point DEFAULT_SIZE = new Point( 1920, 1080 );

	@Override
	public void createPartControl(Composite parent) {
		hashMenuItem = new HashMap<Integer, MenuItem>();
		composite = new Composite(parent, SWT.NONE);
		GridLayout gl_composite = new GridLayout(1, false);
		gl_composite.verticalSpacing = 20;
		composite.setLayout(gl_composite);

		ToolBar toolBarMain = new ToolBar(composite, SWT.FLAT | Preferences.ORIENTATION);
		if(Preferences.ORIENTATION == SWT.HORIZONTAL){
			toolBarMain.setLayoutData(new GridData(SWT.CENTER, SWT.BOTTOM, true, false, 1, 1));
			ToolItem tltmRegistration = new ToolItem(toolBarMain, SWT.NONE);
			tltmRegistration.setToolTipText("Pendaftaran");
			tltmRegistration.setImage(scaledTo(MainActivator.getImageDescriptor("img/pendaftaran.png").getImageData()));
			tltmRegistration.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {

	                final ToolItem toolItem = (ToolItem) e.widget;
	                final ToolBar  toolBar = toolItem.getParent();
	                Point point = toolBar.toDisplay(new Point(e.x, e.y));
	                Point iconSize = new Point(toolItem.getBounds().width,toolItem.getBounds().height);
	                dropMenuPendaftaran.setLocation(point.x+(iconSize.x*0), point.y+iconSize.y);
	                dropMenuPendaftaran.setVisible(true);
				}
			});
			
			dropMenuPendaftaran = new Menu(new Shell(), SWT.POP_UP);
			MenuItem itemPendaftaranAction = new MenuItem(dropMenuPendaftaran, SWT.PUSH);
			itemPendaftaranAction.setEnabled(false);
	        itemPendaftaranAction.setText("Pendaftaran");
	        itemPendaftaranAction.setID(1);
	        itemPendaftaranAction.setImage(scaledTo(MainActivator.getImageDescriptor("img/pendaftaran.png").getImageData().scaledTo(40, 40)));
	        hashMenuItem.put(1, itemPendaftaranAction);
	        itemPendaftaranAction.addSelectionListener(new SelectionAdapter() {
	        	@Override
	        	public void widgetSelected(SelectionEvent e) {
	        		try {
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(PendaftaranView.ID);
					} catch (PartInitException e1) {
						e1.printStackTrace();
					}
	        	}
			});
	        MenuItem itemDaftarAntrianAction = new MenuItem(dropMenuPendaftaran, SWT.PUSH);
	        itemDaftarAntrianAction.setEnabled(false);
	        itemDaftarAntrianAction.setText("Daftar Antrian");
	        itemDaftarAntrianAction.setID(2);
	        itemDaftarAntrianAction.setImage(scaledTo(MainActivator.getImageDescriptor("img/antrian.png").getImageData()));
	        hashMenuItem.put(2, itemDaftarAntrianAction);
	        itemDaftarAntrianAction.addSelectionListener(new SelectionAdapter() {
	        	@Override
	        	public void widgetSelected(SelectionEvent e) {
	        		try {
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(TerimaPendaftaranView2.ID);
					} catch (PartInitException e1) {
						e1.printStackTrace();
					}
	        	}
			});
	        MenuItem itemCariWPAction = new MenuItem(dropMenuPendaftaran, SWT.PUSH);
	        itemCariWPAction.setEnabled(false);
	        itemCariWPAction.setText("Cari Wajib Pajak");
	        itemCariWPAction.setID(3);
	        itemCariWPAction.setImage(scaledTo(MainActivator.getImageDescriptor("img/cari_wp.png").getImageData()));
	        hashMenuItem.put(3, itemCariWPAction);
	        itemCariWPAction.addSelectionListener(new SelectionAdapter() {
	        	@Override
	        	public void widgetSelected(SelectionEvent e) {
	        		try {
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(CariPendaftaranView.ID);
					} catch (PartInitException e1) {
						e1.printStackTrace();
					}
	        	}
			});
	        MenuItem itemDataWPAction = new MenuItem(dropMenuPendaftaran, SWT.PUSH);
	        itemDataWPAction.setEnabled(false);
	        itemDataWPAction.setText("Data Wajib Pajak");
	        itemDataWPAction.setID(40);
	        itemDataWPAction.setImage(scaledTo(MainActivator.getImageDescriptor("img/cari_wp.png").getImageData()));
	        hashMenuItem.put(40, itemDataWPAction);
	        itemDataWPAction.addSelectionListener(new SelectionAdapter() {
	        	@Override
	        	public void widgetSelected(SelectionEvent e) {
	        		try {
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(DataWajibPajakView.ID);
					} catch (PartInitException e1) {
						e1.printStackTrace();
					}
	        	}
			});
	        MenuItem itemCekLokasiAction = new MenuItem(dropMenuPendaftaran, SWT.PUSH);
	        itemCekLokasiAction.setEnabled(false);
	        itemCekLokasiAction.setText("Cek Lokasi Reklame");
	        itemCekLokasiAction.setID(39);
	        itemCekLokasiAction.setImage(scaledTo(MainActivator.getImageDescriptor("img/cari_wp.png").getImageData()));
	        hashMenuItem.put(39, itemCekLokasiAction);
	        itemCekLokasiAction.addSelectionListener(new SelectionAdapter() {
	        	@Override
	        	public void widgetSelected(SelectionEvent e) {
	        		try {
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(CekLokasiReklame.ID);
					} catch (PartInitException e1) {
						e1.printStackTrace();
					}
	        	}
			});
			
			ToolItem tltmSPTPD = new ToolItem(toolBarMain, SWT.NONE);
			tltmSPTPD.setToolTipText("SPTPD");
			tltmSPTPD.setImage(scaledTo(MainActivator.getImageDescriptor("img/sptpd.png").getImageData()));
			tltmSPTPD.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {

		            final ToolItem toolItem = (ToolItem) e.widget;
		            final ToolBar  toolBar = toolItem.getParent();
		            Point point = toolBar.toDisplay(new Point(e.x, e.y));
		            Point iconSize = new Point(toolItem.getBounds().width,toolItem.getBounds().height);
		            dropMenuSPTPD.setLocation(point.x+(iconSize.x*1), point.y+iconSize.y);
		            dropMenuSPTPD.setVisible(true);
				}
			});
			
			dropMenuSPTPD = new Menu(new Shell(), SWT.POP_UP);
			MenuItem itemDaftarSPTPDAction = new MenuItem(dropMenuSPTPD, SWT.PUSH);
			itemDaftarSPTPDAction.setEnabled(false);
	        itemDaftarSPTPDAction.setText("Daftar SPTPD");
	        itemDaftarSPTPDAction.setID(4);
	        itemDaftarSPTPDAction.setImage(scaledTo(MainActivator.getImageDescriptor("img/sptpd.png").getImageData().scaledTo(40, 40)));
	        hashMenuItem.put(4, itemDaftarSPTPDAction);
	        itemDaftarSPTPDAction.addSelectionListener(new SelectionAdapter() {
	        	@Override
	        	public void widgetSelected(SelectionEvent e) {
	        		try {
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(DaftarSPTPDView.ID);
					} catch (PartInitException e1) {
						e1.printStackTrace();
					}
	        	}
			});
	        MenuItem itemDaftarSKPDAction = new MenuItem(dropMenuSPTPD, SWT.PUSH);
			itemDaftarSKPDAction.setEnabled(false);
	        itemDaftarSKPDAction.setText("Daftar SKPD");
	        itemDaftarSKPDAction.setID(32);
	        itemDaftarSKPDAction.setImage(scaledTo(MainActivator.getImageDescriptor("img/sptpd.png").getImageData().scaledTo(40, 40)));
	        hashMenuItem.put(32, itemDaftarSKPDAction);
	        itemDaftarSKPDAction.addSelectionListener(new SelectionAdapter() {
	        	@Override
	        	public void widgetSelected(SelectionEvent e) {
	        		try {
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(DaftarSKPDView.ID);
					} catch (PartInitException e1) {
						e1.printStackTrace();
					}
	        	}
			});
	        MenuItem itemInputSPTPDAction = new MenuItem(dropMenuSPTPD, SWT.PUSH);
	        itemInputSPTPDAction.setEnabled(false);
	        itemInputSPTPDAction.setText("Input SPTPD");
	        itemInputSPTPDAction.setID(5);
	        hashMenuItem.put(5, itemInputSPTPDAction);
	        itemInputSPTPDAction.setImage(scaledTo(MainActivator.getImageDescriptor("img/sptpd.png").getImageData().scaledTo(40, 40)));
	        itemInputSPTPDAction.addSelectionListener(new SelectionAdapter() {
	        	@Override
	        	public void widgetSelected(SelectionEvent e) {
	        		try {
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(InputSPTPDView.ID);
					} catch (PartInitException e1) {
						e1.printStackTrace();
					}
	        	}
			});
	        
	        MenuItem itemVerifikasiSPTPDAction = new MenuItem(dropMenuSPTPD, SWT.PUSH);
	        itemVerifikasiSPTPDAction.setEnabled(false);
	        itemVerifikasiSPTPDAction.setText("Verifikasi SPTPD");
	        itemVerifikasiSPTPDAction.setID(44);
	        hashMenuItem.put(44, itemVerifikasiSPTPDAction);
	        itemVerifikasiSPTPDAction.setImage(scaledTo(MainActivator.getImageDescriptor("img/sptpd.png").getImageData().scaledTo(40, 40)));
	        itemVerifikasiSPTPDAction.addSelectionListener(new SelectionAdapter() {
	        	@Override
	        	public void widgetSelected(SelectionEvent e) {
	        		try {
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(VerifikasiSPTPDView.ID);
					} catch (PartInitException e1) {
						e1.printStackTrace();
					}
	        	}
			});
			
			ToolItem tltmPembayaran = new ToolItem(toolBarMain, SWT.NONE);
			tltmPembayaran.setToolTipText("Pembayaran");
			tltmPembayaran.setImage(scaledTo(MainActivator.getImageDescriptor("img/pembayaran.png").getImageData()));
			tltmPembayaran.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {

					final ToolItem toolItem = (ToolItem) e.widget;
		            final ToolBar  toolBar = toolItem.getParent();
		            Point point = toolBar.toDisplay(new Point(e.x, e.y));
		            Point iconSize = new Point(toolItem.getBounds().width,toolItem.getBounds().height);
		            dropMenuPembayaran.setLocation(point.x+(iconSize.x*2), point.y+iconSize.y);
		            dropMenuPembayaran.setVisible(true);
				}
			});
			dropMenuPembayaran = new Menu(new Shell(), SWT.POP_UP);
			MenuItem itemDaftarSSPDAction = new MenuItem(dropMenuPembayaran, SWT.PUSH);
			itemDaftarSSPDAction.setEnabled(false);
	        itemDaftarSSPDAction.setText("Daftar SSPD");
	        itemDaftarSSPDAction.setID(7);
	        hashMenuItem.put(7, itemDaftarSSPDAction);
	        itemDaftarSSPDAction.setImage(scaledTo(MainActivator.getImageDescriptor("img/pembayaran.png").getImageData().scaledTo(40, 40)));
	        itemDaftarSSPDAction.addSelectionListener(new SelectionAdapter() {
	        	@Override
	        	public void widgetSelected(SelectionEvent e) {
	        		try {
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(DaftarSSPDView.ID);
					} catch (PartInitException e1) {
						e1.printStackTrace();
					}
	        	}
			});
	        MenuItem itemNotaAntrianAction = new MenuItem(dropMenuPembayaran, SWT.PUSH);
	        itemNotaAntrianAction.setEnabled(false);
	        itemNotaAntrianAction.setText("Nota Antrian / Pembayaran");
	        itemNotaAntrianAction.setID(6);
	        hashMenuItem.put(6, itemNotaAntrianAction);
	        itemNotaAntrianAction.setImage(scaledTo(MainActivator.getImageDescriptor("img/pembayaran.png").getImageData().scaledTo(40, 40)));
	        itemNotaAntrianAction.addSelectionListener(new SelectionAdapter() {
	        	@Override
	        	public void widgetSelected(SelectionEvent e) {
	        		try {
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(SSPDView.ID);
					} catch (PartInitException e1) {
						e1.printStackTrace();
					}
	        	}
			});
	        MenuItem itemListAnriSSPDAction = new MenuItem(dropMenuPembayaran, SWT.PUSH);
	        itemListAnriSSPDAction.setEnabled(false);
	        itemListAnriSSPDAction.setText("List Antrian Pembayaran");
	        itemListAnriSSPDAction.setID(21);
	        hashMenuItem.put(21, itemListAnriSSPDAction);
	        itemListAnriSSPDAction.setImage(scaledTo(MainActivator.getImageDescriptor("img/pembayaran.png").getImageData().scaledTo(40, 40)));
	        itemListAnriSSPDAction.addSelectionListener(new SelectionAdapter() {
	        	@Override
	        	public void widgetSelected(SelectionEvent e) {
	        		IPerspectiveDescriptor descriptor = PlatformUI.getWorkbench().getPerspectiveRegistry().findPerspectiveWithId("com.dispenda.sspd.perspectives.PembayaranPerspective");
	                PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().setPerspective(descriptor);
	                try {
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(AntriView.ID);
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(SSPDView.ID);
					} catch (PartInitException e1) {
						e1.printStackTrace();
					}
	        	}
			});
			
			ToolItem tltmPemeriksaan = new ToolItem(toolBarMain, SWT.NONE);
			tltmPemeriksaan.setToolTipText("Pemeriksaan");
			tltmPemeriksaan.setImage(scaledTo(MainActivator.getImageDescriptor("img/pemeriksaan.png").getImageData()));
			tltmPemeriksaan.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {

					final ToolItem toolItem = (ToolItem) e.widget;
		            final ToolBar  toolBar = toolItem.getParent();
		            Point point = toolBar.toDisplay(new Point(e.x, e.y));
		            Point iconSize = new Point(toolItem.getBounds().width,toolItem.getBounds().height);
		            dropMenuPemeriksaan.setLocation(point.x+(iconSize.x*3), point.y+iconSize.y);
		            dropMenuPemeriksaan.setVisible(true);
				}
			});
			dropMenuPemeriksaan = new Menu(new Shell(), SWT.POP_UP);
	        MenuItem itemDaftarPemeriksaanAction = new MenuItem(dropMenuPemeriksaan, SWT.PUSH);
	        itemDaftarPemeriksaanAction.setEnabled(false);
	        itemDaftarPemeriksaanAction.setText("Daftar Pemeriksaan");
	        itemDaftarPemeriksaanAction.setID(9);
	        hashMenuItem.put(9, itemDaftarPemeriksaanAction);
	        itemDaftarPemeriksaanAction.setImage(scaledTo(MainActivator.getImageDescriptor("img/pemeriksaan.png").getImageData().scaledTo(40, 40)));
	        itemDaftarPemeriksaanAction.addSelectionListener(new SelectionAdapter() {
	        	@Override
	        	public void widgetSelected(SelectionEvent e) {
	        		try {
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(DaftarPemeriksaanView.ID);
					} catch (PartInitException e1) {
						e1.printStackTrace();
					}
	        	}
			});
	        MenuItem itemNotaPerAction = new MenuItem(dropMenuPemeriksaan, SWT.PUSH);
	        itemNotaPerAction.setEnabled(false);
	        itemNotaPerAction.setText("Nota Perhitungan");
	        itemNotaPerAction.setID(10);
	        hashMenuItem.put(10, itemNotaPerAction);
	        itemNotaPerAction.setImage(scaledTo(MainActivator.getImageDescriptor("img/pemeriksaan.png").getImageData().scaledTo(40, 40)));
	        itemNotaPerAction.addSelectionListener(new SelectionAdapter() {
	        	@Override
	        	public void widgetSelected(SelectionEvent e) {
	        		try {
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(NotaPerhitunganView.ID);
					} catch (PartInitException e1) {
						e1.printStackTrace();
					}
	        	}
			});
	        MenuItem itemPemeriksaanAction = new MenuItem(dropMenuPemeriksaan, SWT.PUSH);
	        itemPemeriksaanAction.setEnabled(false);
	        itemPemeriksaanAction.setText("Pemeriksaan");
	        itemPemeriksaanAction.setID(8);
	        hashMenuItem.put(8, itemPemeriksaanAction);
	        itemPemeriksaanAction.setImage(scaledTo(MainActivator.getImageDescriptor("img/pemeriksaan.png").getImageData().scaledTo(40, 40)));
	        itemPemeriksaanAction.addSelectionListener(new SelectionAdapter() {
	        	@Override
	        	public void widgetSelected(SelectionEvent e) {
	        		try {
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(PemeriksaanView.ID);
					} catch (PartInitException e1) {
						e1.printStackTrace();
					}
	        	}
			});
	        MenuItem itemSKPDKBAction = new MenuItem(dropMenuPemeriksaan, SWT.RADIO);
	        itemSKPDKBAction.setEnabled(false);
	        itemSKPDKBAction.setText("SKPDKB");
	        itemSKPDKBAction.setID(22);
	        hashMenuItem.put(22, itemSKPDKBAction);
	        itemSKPDKBAction.setImage(scaledTo(MainActivator.getImageDescriptor("img/pemeriksaan.png").getImageData().scaledTo(40, 40)));
	        itemSKPDKBAction.addSelectionListener(new SelectionAdapter() {
	        	@Override
	        	public void widgetSelected(SelectionEvent e) {
	        		try {
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(SKPDKBView.ID);
					} catch (PartInitException e1) {
						e1.printStackTrace();
					}
	        	}
			});
	        MenuItem itemRekomendasiPeriksaAction = new MenuItem(dropMenuPemeriksaan, SWT.RADIO);
	        itemRekomendasiPeriksaAction.setEnabled(false);
	        itemRekomendasiPeriksaAction.setText("Rekomendasi Periksa");
	        itemRekomendasiPeriksaAction.setID(23);
	        hashMenuItem.put(23, itemRekomendasiPeriksaAction);
	        itemRekomendasiPeriksaAction.setImage(scaledTo(MainActivator.getImageDescriptor("img/pemeriksaan.png").getImageData().scaledTo(40, 40)));
	        itemRekomendasiPeriksaAction.addSelectionListener(new SelectionAdapter() {
	        	@Override
	        	public void widgetSelected(SelectionEvent e) {
	        		try {
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(RekomendasiPeriksaView.ID);
					} catch (PartInitException e1) {
						e1.printStackTrace();
					}
	        	}
			});
			
			ToolItem tltmAngsuran = new ToolItem(toolBarMain, SWT.NONE);
			tltmAngsuran.setToolTipText("Angsuran");
			tltmAngsuran.setImage(scaledTo(MainActivator.getImageDescriptor("img/angsuran.png").getImageData()));
			tltmAngsuran.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {

					final ToolItem toolItem = (ToolItem) e.widget;
		            final ToolBar  toolBar = toolItem.getParent();
		            Point point = toolBar.toDisplay(new Point(e.x, e.y));
		            Point iconSize = new Point(toolItem.getBounds().width,toolItem.getBounds().height);
		            dropMenuAngsuran.setLocation(point.x+(iconSize.x*4), point.y+iconSize.y);
		            dropMenuAngsuran.setVisible(true);
				}
			});
	        dropMenuAngsuran = new Menu(new Shell(), SWT.POP_UP);
			MenuItem itemDaftarPermohonanAction = new MenuItem(dropMenuAngsuran, SWT.PUSH);
			itemDaftarPermohonanAction.setEnabled(false);
	        itemDaftarPermohonanAction.setText("Daftar Permohonan");
	        itemDaftarPermohonanAction.setID(12);
	        hashMenuItem.put(12, itemDaftarPermohonanAction);
	        itemDaftarPermohonanAction.setImage(scaledTo(MainActivator.getImageDescriptor("img/angsuran.png").getImageData().scaledTo(40, 40)));
	        itemDaftarPermohonanAction.addSelectionListener(new SelectionAdapter() {
	        	@Override
	        	public void widgetSelected(SelectionEvent e) {
	        		try {
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(DaftarPermohonanView.ID);
					} catch (PartInitException e1) {
						e1.printStackTrace();
					}
	        	}
			});
	        MenuItem itemPermohonanDAction = new MenuItem(dropMenuAngsuran, SWT.PUSH);
	        itemPermohonanDAction.setEnabled(false);
	        itemPermohonanDAction.setText("Permohonan");
	        itemPermohonanDAction.setID(11);
	        hashMenuItem.put(11, itemPermohonanDAction);
	        itemPermohonanDAction.setImage(scaledTo(MainActivator.getImageDescriptor("img/angsuran.png").getImageData().scaledTo(40, 40)));
	        itemPermohonanDAction.addSelectionListener(new SelectionAdapter() {
	        	@Override
	        	public void widgetSelected(SelectionEvent e) {
	        		try {
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(PermohonanView.ID);
					} catch (PartInitException e1) {
						e1.printStackTrace();
					}
	        	}
			});
	        MenuItem itemDaftarAngsuranAction = new MenuItem(dropMenuAngsuran, SWT.PUSH);
	        itemDaftarAngsuranAction.setEnabled(false);
	        itemDaftarAngsuranAction.setText("Daftar Angsuran");
	        itemDaftarAngsuranAction.setID(24);
	        hashMenuItem.put(24, itemDaftarAngsuranAction);
	        itemDaftarAngsuranAction.setImage(scaledTo(MainActivator.getImageDescriptor("img/angsuran.png").getImageData().scaledTo(40, 40)));
	        itemDaftarAngsuranAction.addSelectionListener(new SelectionAdapter() {
	        	@Override
	        	public void widgetSelected(SelectionEvent e) {
	        		try {
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(DaftarAngsuranView.ID);
					} catch (PartInitException e1) {
						e1.printStackTrace();
					}
	        	}
			});
			
			ToolItem tltmTunggakan = new ToolItem(toolBarMain, SWT.NONE);
			tltmTunggakan.setToolTipText("Tunggakan");
			tltmTunggakan.setImage(scaledTo(MainActivator.getImageDescriptor("img/tunggakan.png").getImageData()));
			tltmTunggakan.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {

					final ToolItem toolItem = (ToolItem) e.widget;
		            final ToolBar  toolBar = toolItem.getParent();
		            Point point = toolBar.toDisplay(new Point(e.x, e.y));
		            Point iconSize = new Point(toolItem.getBounds().width,toolItem.getBounds().height);
		            dropMenuTunggakan.setLocation(point.x+(iconSize.x*5), point.y+iconSize.y);
		            dropMenuTunggakan.setVisible(true);
				}
			});
			dropMenuTunggakan = new Menu(new Shell(), SWT.POP_UP);
	        MenuItem itemTSPTPDAction = new MenuItem(dropMenuTunggakan, SWT.PUSH);
	        itemTSPTPDAction.setEnabled(false);
	        itemTSPTPDAction.setText("Tunggakan SPTPD & SKPDKB");
	        itemTSPTPDAction.setID(13);
	        hashMenuItem.put(13, itemTSPTPDAction);
	        itemTSPTPDAction.setImage(scaledTo(MainActivator.getImageDescriptor("img/tunggakan.png").getImageData().scaledTo(40, 40)));
	        itemTSPTPDAction.addSelectionListener(new SelectionAdapter() {
	        	public void widgetSelected(SelectionEvent e) {
	        		try {
	        			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(TunggakanSptpdView.ID);
					} catch (WorkbenchException e1) {
						e1.printStackTrace();
					}
	        	}
			});
	        MenuItem itemTunggakanDendaAction = new MenuItem(dropMenuTunggakan, SWT.PUSH);
	        itemTunggakanDendaAction.setEnabled(false);
	        itemTunggakanDendaAction.setText("Tunggakan Denda");
	        itemTunggakanDendaAction.setID(25);
	        hashMenuItem.put(25, itemTunggakanDendaAction);
	        itemTunggakanDendaAction.setImage(scaledTo(MainActivator.getImageDescriptor("img/tunggakan.png").getImageData().scaledTo(40, 40)));
	        itemTunggakanDendaAction.addSelectionListener(new SelectionAdapter() {
	        	public void widgetSelected(SelectionEvent e) {
	        		try {
	        			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(TunggakanDendaView.ID);
					} catch (WorkbenchException e1) {
						e1.printStackTrace();
					}
	        	}
			});
	        MenuItem itemTunggakanWPAction = new MenuItem(dropMenuTunggakan, SWT.PUSH);
	        itemTunggakanWPAction.setEnabled(false);
	        itemTunggakanWPAction.setText("Tunggakan WP");
	        itemTunggakanWPAction.setID(26);
	        hashMenuItem.put(26, itemTunggakanWPAction);
	        itemTunggakanWPAction.setImage(scaledTo(MainActivator.getImageDescriptor("img/tunggakan.png").getImageData().scaledTo(40, 40)));
	        itemTunggakanWPAction.addSelectionListener(new SelectionAdapter() {
	        	public void widgetSelected(SelectionEvent e) {
	        		try {
	        			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(TunggakanWpView.ID);
					} catch (WorkbenchException e1) {
						e1.printStackTrace();
					}
	        	}
			});
			ToolItem tltmLaporan = new ToolItem(toolBarMain, SWT.NONE);
			tltmLaporan.setToolTipText("Laporan");
			tltmLaporan.setImage(scaledTo(MainActivator.getImageDescriptor("img/laporan.png").getImageData()));
			tltmLaporan.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					
					final ToolItem toolItem = (ToolItem) e.widget;
		            final ToolBar  toolBar = toolItem.getParent();
		            Point point = toolBar.toDisplay(new Point(e.x, e.y));
		            Point iconSize = new Point(toolItem.getBounds().width,toolItem.getBounds().height);
		            dropMenuLaporan.setLocation(point.x+(iconSize.x*6), point.y+iconSize.y);
		            dropMenuLaporan.setVisible(true);
				}
			});
	        dropMenuLaporan = new Menu(new Shell(), SWT.POP_UP);
			MenuItem itemPendapatanBulananAction = new MenuItem(dropMenuLaporan, SWT.PUSH);
			itemPendapatanBulananAction.setEnabled(false);
	        itemPendapatanBulananAction.setText("Laporan Pendapatan Bulanan");
	        itemPendapatanBulananAction.setID(27);
	        hashMenuItem.put(27, itemPendapatanBulananAction);
	        itemPendapatanBulananAction.setImage(scaledTo(MainActivator.getImageDescriptor("img/laporan.png").getImageData().scaledTo(40, 40)));
	        itemPendapatanBulananAction.addSelectionListener(new SelectionAdapter() {
	        	public void widgetSelected(SelectionEvent e) {
	        		try {
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(LaporanPendapatanBulananView.ID);
					} catch (PartInitException e1) {
						e1.printStackTrace();
					}
	        	}
			});
	        /* Hide Kode 1-a
	         * 
	         * MenuItem itemPendapatanHarianAction = new MenuItem(dropMenuLaporan, SWT.PUSH);
	        itemPendapatanHarianAction.setEnabled(false);
	        itemPendapatanHarianAction.setText("Laporan Pendapatan Harian");
	        itemPendapatanHarianAction.setID(28);
	        hashMenuItem.put(28, itemPendapatanHarianAction);
	        itemPendapatanHarianAction.setImage(scaledTo(MainActivator.getImageDescriptor("img/laporan.png").getImageData().scaledTo(40, 40)));
	        itemPendapatanHarianAction.addSelectionListener(new SelectionAdapter() {
	        	public void widgetSelected(SelectionEvent e) {
	        		try {
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(LaporanPendapatanHarianView.ID);
					} catch (PartInitException e1) {
						e1.printStackTrace();
					}
	        	}
			});*/
	        
	        MenuItem itemHistoryWPAction = new MenuItem(dropMenuLaporan, SWT.PUSH);
	        itemHistoryWPAction.setEnabled(false);
	        itemHistoryWPAction.setText("History WP");
	        itemHistoryWPAction.setID(20);
	        hashMenuItem.put(20, itemHistoryWPAction);
	        itemHistoryWPAction.setImage(scaledTo(MainActivator.getImageDescriptor("img/laporan.png").getImageData().scaledTo(40, 40)));
	        itemHistoryWPAction.addSelectionListener(new SelectionAdapter() {
	        	public void widgetSelected(SelectionEvent e) {
	        		try {
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(HistoryWPView.ID);
					} catch (PartInitException e1) {
						e1.printStackTrace();
					}
	        	}
			});
	        MenuItem itemKeuanganAction = new MenuItem(dropMenuLaporan, SWT.PUSH);
	        itemKeuanganAction.setEnabled(false);
	        itemKeuanganAction.setText("Keuangan");
	        itemKeuanganAction.setID(34);
	        hashMenuItem.put(34, itemKeuanganAction);
	        itemKeuanganAction.setImage(scaledTo(MainActivator.getImageDescriptor("img/laporan.png").getImageData().scaledTo(40, 40)));
	        itemKeuanganAction.addSelectionListener(new SelectionAdapter() {
	        	public void widgetSelected(SelectionEvent e) {
	        		try {
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(LaporanKeuanganView.ID);
//						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(LaporanKeuanganHarianView.ID);
//						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(DaftarPemeriksaanKeuanganView.ID);
//						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(RealisasiHarianKeuanganView.ID);
//						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(LaporanDendaSSPD.ID);
					} catch (PartInitException e1) {
						e1.printStackTrace();
					}
	        	}
			});
	        MenuItem itemPiutangAction = new MenuItem(dropMenuLaporan, SWT.PUSH);
	        itemPiutangAction.setEnabled(false);
	        itemPiutangAction.setText("Laporan Piutang");
	        itemPiutangAction.setID(41);
	        hashMenuItem.put(41, itemPiutangAction);
	        itemPiutangAction.setImage(scaledTo(MainActivator.getImageDescriptor("img/laporan.png").getImageData().scaledTo(40, 40)));
	        itemPiutangAction.addSelectionListener(new SelectionAdapter() {
	        	public void widgetSelected(SelectionEvent e) {
	        		try {
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(LaporanPiutang.ID);
					} catch (PartInitException e1) {
						e1.printStackTrace();
					}
	        	}
			});
			
			ToolItem tltmRealisasi = new ToolItem(toolBarMain, SWT.NONE);
			tltmRealisasi.setToolTipText("Realisasi Penerimaan");
			tltmRealisasi.setImage(scaledTo(MainActivator.getImageDescriptor("img/realisasi.png").getImageData()));
			tltmRealisasi.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					final ToolItem toolItem = (ToolItem) e.widget;
		            final ToolBar  toolBar = toolItem.getParent();
		            Point point = toolBar.toDisplay(new Point(e.x, e.y));
		            Point iconSize = new Point(toolItem.getBounds().width,toolItem.getBounds().height);
		            dropMenuRPenerimaan.setLocation(point.x+(iconSize.x*7), point.y+iconSize.y);
		            dropMenuRPenerimaan.setVisible(true);
				}
			});

	        dropMenuRPenerimaan = new Menu(new Shell(), SWT.POP_UP);
	        /* Hide Kode 2-b
	         * 
	         * MenuItem itemPenerimaanAction = new MenuItem(dropMenuRPenerimaan, SWT.PUSH);
	        itemPenerimaanAction.setEnabled(false);
	        itemPenerimaanAction.setText("Realisasi Penerimaan");
	        itemPenerimaanAction.setID(14);
	        hashMenuItem.put(14, itemPenerimaanAction);
	        itemPenerimaanAction.setImage(scaledTo(MainActivator.getImageDescriptor("img/realisasi.png").getImageData().scaledTo(40, 40)));
	        itemPenerimaanAction.addSelectionListener(new SelectionAdapter() {
	        	public void widgetSelected(SelectionEvent e) {
	        		try {
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(RealisasiPenerimaanView.ID);
					} catch (PartInitException e1) {
						e1.printStackTrace();
					}
	        	}
			});*/
	        MenuItem itemRealisasiHarian = new MenuItem(dropMenuRPenerimaan, SWT.PUSH);
	        itemRealisasiHarian.setEnabled(false);
	        itemRealisasiHarian.setText("Realisasi Harian");
	        itemRealisasiHarian.setID(33);
	        hashMenuItem.put(33, itemRealisasiHarian);
	        itemRealisasiHarian.setImage(scaledTo(MainActivator.getImageDescriptor("img/realisasi.png").getImageData().scaledTo(40, 40)));
	        itemRealisasiHarian.addSelectionListener(new SelectionAdapter() {
	        	public void widgetSelected(SelectionEvent e) {
	        		try {
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(RealisasiHarianView.ID);
					} catch (PartInitException e1) {
						e1.printStackTrace();
					}
	        	}
			});
	        /*MenuItem itemPenerimaanPajakDaerahAction = new MenuItem(dropMenuRPenerimaan, SWT.PUSH);
	        itemPenerimaanPajakDaerahAction.setText("Penerimaan Pajak Daerah");
	        itemPenerimaanPajakDaerahAction.setImage(scaledTo(MainActivator.getImageDescriptor("img/realisasi.png").getImageData().scaledTo(40, 40)));
	        itemPenerimaanPajakDaerahAction.addSelectionListener(new SelectionAdapter() {
	        	public void widgetSelected(SelectionEvent e) {
	        		try {
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(RealisasiPenerimaanPajakDaerahView.ID);
					} catch (PartInitException e1) {
						e1.printStackTrace();
					}
	        	}
			});
	        MenuItem itemPenerimaanWajibPajakAction = new MenuItem(dropMenuRPenerimaan, SWT.PUSH);
	        itemPenerimaanWajibPajakAction.setText("Penerimaan Wajib Pajak");
	        itemPenerimaanWajibPajakAction.setImage(scaledTo(MainActivator.getImageDescriptor("img/realisasi.png").getImageData().scaledTo(40, 40)));
	        itemPenerimaanWajibPajakAction.addSelectionListener(new SelectionAdapter() {
	        	public void widgetSelected(SelectionEvent e) {
	        		try {
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(RealisasiPenerimaanWajibPajakView.ID);
					} catch (PartInitException e1) {
						e1.printStackTrace();
					}
	        	}
			});*/
	        MenuItem itemPenerimaanSptpdAction = new MenuItem(dropMenuRPenerimaan, SWT.PUSH);
	        itemPenerimaanSptpdAction.setEnabled(false);
	        itemPenerimaanSptpdAction.setText("Penerimaan SPTPD");
	        itemPenerimaanSptpdAction.setID(29);
	        hashMenuItem.put(29, itemPenerimaanSptpdAction);
	        itemPenerimaanSptpdAction.setImage(scaledTo(MainActivator.getImageDescriptor("img/realisasi.png").getImageData().scaledTo(40, 40)));
	        itemPenerimaanSptpdAction.addSelectionListener(new SelectionAdapter() {
	        	public void widgetSelected(SelectionEvent e) {
	        		try {
					PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(RealisasiPenerimaanSptpdView.ID);
					} catch (PartInitException e1) {
						e1.printStackTrace();
					}
	        	}
			});
	        MenuItem itemPenerimaanSkpdkbAction = new MenuItem(dropMenuRPenerimaan, SWT.PUSH);
	        itemPenerimaanSkpdkbAction.setEnabled(false);
	        itemPenerimaanSkpdkbAction.setText("Penerimaan SKPDKB");
	        itemPenerimaanSkpdkbAction.setID(30);
	        hashMenuItem.put(30, itemPenerimaanSkpdkbAction);
	        itemPenerimaanSkpdkbAction.setImage(scaledTo(MainActivator.getImageDescriptor("img/realisasi.png").getImageData().scaledTo(40, 40)));
	        itemPenerimaanSkpdkbAction.addSelectionListener(new SelectionAdapter() {
	        	public void widgetSelected(SelectionEvent e) {
	        		try {
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(RealisasiPenerimaanSkpdkbView.ID);
					} catch (PartInitException e1) {
						e1.printStackTrace();
					}
	        	}
			});
			
	        ToolItem tltmArsip = new ToolItem(toolBarMain, SWT.NONE);
	        tltmArsip.setToolTipText("Arsip");
	        tltmArsip.setImage(scaledTo(MainActivator.getImageDescriptor("img/control_panel.png").getImageData()));
	        tltmArsip.addSelectionListener(new SelectionAdapter() {
	        	public void widgetSelected(SelectionEvent e) {
	        		final ToolItem toolItem = (ToolItem) e.widget;
	        		final ToolBar toolBar = toolItem.getParent();
	        		Point point = toolBar.toDisplay(new Point(e.x, e.y));
		            Point iconSize = new Point(toolItem.getBounds().width,toolItem.getBounds().height);
		            dropMenuArsip.setLocation(point.x+(iconSize.x*8), point.y+iconSize.y);
		            dropMenuArsip.setVisible(true);
	        	}
			});
	        dropMenuArsip = new Menu(new Shell(), SWT.POP_UP);
	        MenuItem itemArsipAction = new MenuItem(dropMenuArsip, SWT.PUSH);
	        itemArsipAction.setEnabled(false);
	        itemArsipAction.setText("Arsip");
	        itemArsipAction.setID(36);
	        hashMenuItem.put(36, itemArsipAction);
	        itemArsipAction.setImage(scaledTo(MainActivator.getImageDescriptor("img/user.png").getImageData()));
	        itemArsipAction.addSelectionListener(new SelectionAdapter() {
	        	@Override
	        	public void widgetSelected(SelectionEvent e) {
	        		try {
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(ArsipView.ID);
					} catch (PartInitException e1) {
						e1.printStackTrace();
					}
	        	}
			});
	        MenuItem itemArsipTransactionAction = new MenuItem(dropMenuArsip, SWT.PUSH);
	        itemArsipTransactionAction.setEnabled(false);
	        itemArsipTransactionAction.setText("Keluar Masuk Arsip");
	        itemArsipTransactionAction.setID(37);
	        hashMenuItem.put(37, itemArsipTransactionAction);
	        itemArsipTransactionAction.setImage(scaledTo(MainActivator.getImageDescriptor("img/user.png").getImageData()));
	        itemArsipTransactionAction.addSelectionListener(new SelectionAdapter() {
	        	@Override
	        	public void widgetSelected(SelectionEvent e) {
	        		try {
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(ArsipTransactionView.ID);
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(VerifikasiDataArsipView.ID);
					} catch (PartInitException e1) {
						e1.printStackTrace();
					}
	        	}
			});
	        MenuItem itemArsipPrintedAction = new MenuItem(dropMenuArsip, SWT.PUSH);
	        itemArsipPrintedAction.setEnabled(false);
	        itemArsipPrintedAction.setText("Cetak Barcode");
	        itemArsipPrintedAction.setID(38);
	        hashMenuItem.put(38, itemArsipPrintedAction);
	        itemArsipPrintedAction.setImage(scaledTo(MainActivator.getImageDescriptor("img/user.png").getImageData()));
	        itemArsipPrintedAction.addSelectionListener(new SelectionAdapter() {
	        	@Override
	        	public void widgetSelected(SelectionEvent e) {
	        		try {
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(ListBarcodePrintView.ID);
					} catch (PartInitException e1) {
						e1.printStackTrace();
					}
	        	}
			});
	        MenuItem itemArsipListImage = new MenuItem(dropMenuArsip, SWT.PUSH);
	        itemArsipListImage.setEnabled(false);
	        itemArsipListImage.setText("Berkas Arsip");
	        itemArsipListImage.setID(42);
	        hashMenuItem.put(42, itemArsipListImage);
	        itemArsipListImage.setImage(scaledTo(MainActivator.getImageDescriptor("img/user.png").getImageData()));
	        itemArsipListImage.addSelectionListener(new SelectionAdapter() {
	        	@Override
	        	public void widgetSelected(SelectionEvent e) {
	        		try {
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(ImageBarcodeToDatabaseView.ID);
					} catch (PartInitException e1) {
						e1.printStackTrace();
					}
	        	}
			});
	        
			ToolItem tltmControlPanel = new ToolItem(toolBarMain, SWT.NONE);
			tltmControlPanel.setToolTipText("Control Panel");
			tltmControlPanel.setImage(scaledTo(MainActivator.getImageDescriptor("img/control_panel.png").getImageData()));
			tltmControlPanel.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {

					final ToolItem toolItem = (ToolItem) e.widget;
		            final ToolBar  toolBar = toolItem.getParent();
		            Point point = toolBar.toDisplay(new Point(e.x, e.y));
		            Point iconSize = new Point(toolItem.getBounds().width,toolItem.getBounds().height);
		            dropMenuCP.setLocation(point.x+(iconSize.x*9), point.y+iconSize.y);
		            dropMenuCP.setVisible(true);
				}
			});
	        dropMenuCP = new Menu(new Shell(), SWT.POP_UP);
	        MenuItem itemUserAction = new MenuItem(dropMenuCP, SWT.PUSH);
	        itemUserAction.setEnabled(false);
	        itemUserAction.setText("Pengguna");
	        itemUserAction.setID(15);
	        hashMenuItem.put(15, itemUserAction);
	        itemUserAction.setImage(scaledTo(MainActivator.getImageDescriptor("img/user.png").getImageData()));
	        itemUserAction.addSelectionListener(new SelectionAdapter() {
	        	@Override
	        	public void widgetSelected(SelectionEvent e) {
	        		try {
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(UserView.ID);
					} catch (PartInitException e1) {
						e1.printStackTrace();
					}
	        	}
			});
	        MenuItem itemKecamatanAction = new MenuItem(dropMenuCP, SWT.PUSH);
	        itemKecamatanAction.setEnabled(false);
	        itemKecamatanAction.setText("Kecamatan");
	        itemKecamatanAction.setID(16);
	        hashMenuItem.put(16, itemKecamatanAction);
	        itemKecamatanAction.setImage(scaledTo(MainActivator.getImageDescriptor("img/kecamatan.png").getImageData()));
	        itemKecamatanAction.addSelectionListener(new SelectionAdapter() {
	        	@Override
	        	public void widgetSelected(SelectionEvent e) {
	        		try {
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(KecamatanView.ID);
					} catch (PartInitException e1) {
						e1.printStackTrace();
					}
	        	}
			});
	        MenuItem itemPajakAction = new MenuItem(dropMenuCP, SWT.PUSH);
	        itemPajakAction.setEnabled(false);
	        itemPajakAction.setText("Pajak");
	        itemPajakAction.setID(17);
	        hashMenuItem.put(17, itemPajakAction);
	        itemPajakAction.setImage(scaledTo(MainActivator.getImageDescriptor("img/subpajak.png").getImageData()));
	        itemPajakAction.addSelectionListener(new SelectionAdapter() {
	        	@Override
	        	public void widgetSelected(SelectionEvent e) {
	        		try {
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(PajakView.ID);
					} catch (PartInitException e1) {
						e1.printStackTrace();
					}
	        	}
			});
	        MenuItem itemPejabatAction = new MenuItem(dropMenuCP, SWT.PUSH);
	        itemPejabatAction.setEnabled(false);
	        itemPejabatAction.setText("Pejabat");
	        itemPejabatAction.setID(19);
	        hashMenuItem.put(19, itemPejabatAction);
	        itemPejabatAction.setImage(scaledTo(MainActivator.getImageDescriptor("img/pejabat.png").getImageData()));
	        itemPejabatAction.addSelectionListener(new SelectionAdapter() {
	        	@Override
	        	public void widgetSelected(SelectionEvent e) {
	        		try {
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(PejabatView.ID);
					} catch (PartInitException e1) {
						e1.printStackTrace();
					}
	        	}
			});
	        MenuItem itemKodefikasiAction = new MenuItem(dropMenuCP, SWT.PUSH);
	        itemKodefikasiAction.setEnabled(false);
	        itemKodefikasiAction.setText("Kodefikasi");
	        itemKodefikasiAction.setID(35);
	        hashMenuItem.put(35, itemKodefikasiAction);
	        itemKodefikasiAction.setImage(scaledTo(MainActivator.getImageDescriptor("img/laporan.png").getImageData().scaledTo(40,40)));
	        itemKodefikasiAction.addSelectionListener(new SelectionAdapter() {
	        	@Override
	        	public void widgetSelected(SelectionEvent e) {
	        		try {
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(KodefikasiView.ID);
					} catch (PartInitException e1) {
						e1.printStackTrace();
					}
	        	}
			});
	        MenuItem itemAwardAction = new MenuItem(dropMenuCP, SWT.PUSH);
	        itemAwardAction.setEnabled(false);
	        itemAwardAction.setText("Award");
	        itemAwardAction.setID(31);
	        hashMenuItem.put(31, itemAwardAction);
	        itemAwardAction.setImage(scaledTo(MainActivator.getImageDescriptor("img/award.png").getImageData().scaledTo(40, 40)));
	        itemAwardAction.addSelectionListener(new SelectionAdapter() {
	        	@Override
	        	public void widgetSelected(SelectionEvent e) {
	        		try {
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(AwardView.ID);
					} catch (PartInitException e1) {
						e1.printStackTrace();
					}
	        	}
			});
	        
	        MenuItem itemPreferencesAction = new MenuItem(dropMenuCP, SWT.PUSH);
	        itemPreferencesAction.setEnabled(true);
	        itemPreferencesAction.setText("Preferences");
	        itemPreferencesAction.setImage(scaledTo(MainActivator.getImageDescriptor("img/control_panel.png").getImageData().scaledTo(40, 40)));
	        itemPreferencesAction.addSelectionListener(new SelectionAdapter() {
	        	@Override
	        	public void widgetSelected(SelectionEvent e) {
	        		try {
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(PreferencesView.ID);
					} catch (PartInitException e1) {
						e1.printStackTrace();
					}
	        	}
			});
	        
	        /*MenuItem itemPermintaanPerubahanAction = new MenuItem(dropMenuCP, SWT.PUSH);
	        itemPermintaanPerubahanAction.setEnabled(true);
	        itemPermintaanPerubahanAction.setText("Permintaan Perubahan");
	        itemPermintaanPerubahanAction.setImage(scaledTo(MainActivator.getImageDescriptor("img/control_panel.png").getImageData().scaledTo(40, 40)));
	        itemPermintaanPerubahanAction.addSelectionListener(new SelectionAdapter() {
	        	@Override
	        	public void widgetSelected(SelectionEvent e) {
	        		try {
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(PermintaanPerubahanView.ID);
					} catch (PartInitException e1) {
						e1.printStackTrace();
					}
	        	}
			});*/
			
			/*ToolItem tltmHistory = new ToolItem(toolBarMain, SWT.NONE);
			tltmHistory.setToolTipText("History Wajib Pajak");
			tltmHistory.setImage(scaledTo(MainActivator.getImageDescriptor("img/history.png").getImageData()));
			tltmHistory.addSelectionListener(new SelectionAdapter() {
				Menu dropMenu = null;
				public void widgetSelected(SelectionEvent e) {
					if(dropMenu == null) {
		                dropMenu = new Menu(Display.getCurrent().getActiveShell(), SWT.POP_UP);
		                composite.setMenu(dropMenu);
		                MenuItem itemDaftarSPTPDAction = new MenuItem(dropMenu, SWT.PUSH);
		                itemDaftarSPTPDAction.setText("History Wajib Pajak");
		                itemDaftarSPTPDAction.setImage(scaledTo(MainActivator.getImageDescriptor("img/history.png").getImageData().scaledTo(40, 40)));
		            }

					final ToolItem toolItem = (ToolItem) e.widget;
		            final ToolBar  toolBar = toolItem.getParent();
		            Point point = toolBar.toDisplay(new Point(e.x, e.y));
		            Point iconSize = new Point(toolItem.getBounds().width,toolItem.getBounds().height);
		            dropMenu.setLocation(point.x+(iconSize.x*9), point.y+iconSize.y);
		            dropMenu.setVisible(true);
				}
			});*/
			
			ToolItem tltmShutdown = new ToolItem(toolBarMain, SWT.NONE);
			tltmShutdown.setImage(scaledTo(MainActivator.getImageDescriptor("img/close.png").getImageData()));
			tltmShutdown.setToolTipText("Matikan Program");
			tltmShutdown.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					boolean result = MessageDialog.openConfirm(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Apakah anda yakin ingin menutup aplikasi ini?");
					if(result){
						Display.getCurrent().getActiveShell().close();
						System.exit(0);
					}
				}
			});
			MenuItem itemInputSKPDLBCov = new MenuItem(dropMenuPemeriksaan, SWT.RADIO);
	        itemInputSKPDLBCov.setEnabled(true);
	        itemInputSKPDLBCov.setText("Input SKPDLB (Covid-19)");
	        itemInputSKPDLBCov.setID(43);
	        hashMenuItem.put(43, itemInputSKPDLBCov);
	        itemInputSKPDLBCov.setImage(scaledTo(MainActivator.getImageDescriptor("img/pemeriksaan.png").getImageData().scaledTo(40, 40)));
	        itemInputSKPDLBCov.addSelectionListener(new SelectionAdapter() {
	        	@Override
	        	public void widgetSelected(SelectionEvent e) {
	        		try {
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(InputSKPDLBCovView.ID);
					} catch (PartInitException e1) {
						e1.printStackTrace();
					}
	        	}
			});
		}else{
			toolBarMain.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, false, 1, 1));
			ToolItem tltmRegistration = new ToolItem(toolBarMain, SWT.NONE);
			tltmRegistration.setToolTipText("Pendaftaran");
			tltmRegistration.setImage(scaledTo(MainActivator.getImageDescriptor("img/pendaftaran.png").getImageData()));
			tltmRegistration.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {

	                final ToolItem toolItem = (ToolItem) e.widget;
	                final ToolBar  toolBar = toolItem.getParent();
	                Point point = toolBar.toDisplay(new Point(e.x, e.y));
	                Point iconSize = new Point(toolItem.getBounds().width,toolItem.getBounds().height);
	                dropMenuPendaftaran.setLocation(point.x+(iconSize.x), point.y+(iconSize.y*0));
	                dropMenuPendaftaran.setVisible(true);
				}
			});
			
			dropMenuPendaftaran = new Menu(new Shell(), SWT.POP_UP);
			MenuItem itemPendaftaranAction = new MenuItem(dropMenuPendaftaran, SWT.PUSH);
			itemPendaftaranAction.setEnabled(false);
	        itemPendaftaranAction.setText("Pendaftaran");
	        itemPendaftaranAction.setID(1);
	        itemPendaftaranAction.setImage(scaledTo(MainActivator.getImageDescriptor("img/pendaftaran.png").getImageData().scaledTo(40, 40)));
	        hashMenuItem.put(1, itemPendaftaranAction);
	        itemPendaftaranAction.addSelectionListener(new SelectionAdapter() {
	        	@Override
	        	public void widgetSelected(SelectionEvent e) {
	        		try {
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(PendaftaranView.ID);
					} catch (PartInitException e1) {
						e1.printStackTrace();
					}
	        	}
			});
	        MenuItem itemDaftarAntrianAction = new MenuItem(dropMenuPendaftaran, SWT.PUSH);
	        itemDaftarAntrianAction.setEnabled(false);
	        itemDaftarAntrianAction.setText("Daftar Antrian");
	        itemDaftarAntrianAction.setID(2);
	        itemDaftarAntrianAction.setImage(scaledTo(MainActivator.getImageDescriptor("img/antrian.png").getImageData()));
	        hashMenuItem.put(2, itemDaftarAntrianAction);
	        itemDaftarAntrianAction.addSelectionListener(new SelectionAdapter() {
	        	@Override
	        	public void widgetSelected(SelectionEvent e) {
	        		try {
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(TerimaPendaftaranView2.ID);
					} catch (PartInitException e1) {
						e1.printStackTrace();
					}
	        	}
			});
	        MenuItem itemCariWPAction = new MenuItem(dropMenuPendaftaran, SWT.PUSH);
	        itemCariWPAction.setEnabled(false);
	        itemCariWPAction.setText("Cari Wajib Pajak");
	        itemCariWPAction.setID(3);
	        itemCariWPAction.setImage(scaledTo(MainActivator.getImageDescriptor("img/cari_wp.png").getImageData()));
	        hashMenuItem.put(3, itemCariWPAction);
	        itemCariWPAction.addSelectionListener(new SelectionAdapter() {
	        	@Override
	        	public void widgetSelected(SelectionEvent e) {
	        		try {
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(CariPendaftaranView.ID);
					} catch (PartInitException e1) {
						e1.printStackTrace();
					}
	        	}
			});
	        MenuItem itemDataWPAction = new MenuItem(dropMenuPendaftaran, SWT.PUSH);
	        itemDataWPAction.setEnabled(false);
	        itemDataWPAction.setText("Data Wajib Pajak");
	        itemDataWPAction.setID(40);
	        itemDataWPAction.setImage(scaledTo(MainActivator.getImageDescriptor("img/cari_wp.png").getImageData()));
	        hashMenuItem.put(40, itemDataWPAction);
	        itemDataWPAction.addSelectionListener(new SelectionAdapter() {
	        	@Override
	        	public void widgetSelected(SelectionEvent e) {
	        		try {
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(DataWajibPajakView.ID);
					} catch (PartInitException e1) {
						e1.printStackTrace();
					}
	        	}
			});
	        MenuItem itemCekLokasiAction = new MenuItem(dropMenuPendaftaran, SWT.PUSH);
	        itemCekLokasiAction.setEnabled(false);
	        itemCekLokasiAction.setText("Cek Lokasi Reklame");
	        itemCekLokasiAction.setID(39);
	        itemCekLokasiAction.setImage(scaledTo(MainActivator.getImageDescriptor("img/cari_wp.png").getImageData()));
	        hashMenuItem.put(39, itemCekLokasiAction);
	        itemCekLokasiAction.addSelectionListener(new SelectionAdapter() {
	        	@Override
	        	public void widgetSelected(SelectionEvent e) {
	        		try {
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(CekLokasiReklame.ID);
					} catch (PartInitException e1) {
						e1.printStackTrace();
					}
	        	}
			});
			
			ToolItem tltmSPTPD = new ToolItem(toolBarMain, SWT.NONE);
			tltmSPTPD.setToolTipText("SPTPD");
			tltmSPTPD.setImage(scaledTo(MainActivator.getImageDescriptor("img/sptpd.png").getImageData()));
			tltmSPTPD.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {

		            final ToolItem toolItem = (ToolItem) e.widget;
		            final ToolBar  toolBar = toolItem.getParent();
		            Point point = toolBar.toDisplay(new Point(e.x, e.y));
		            Point iconSize = new Point(toolItem.getBounds().width,toolItem.getBounds().height);
		            dropMenuSPTPD.setLocation(point.x+(iconSize.x), point.y+(iconSize.y*1));
		            dropMenuSPTPD.setVisible(true);
				}
			});
			
			dropMenuSPTPD = new Menu(new Shell(), SWT.POP_UP);
			MenuItem itemDaftarSPTPDAction = new MenuItem(dropMenuSPTPD, SWT.PUSH);
			itemDaftarSPTPDAction.setEnabled(false);
	        itemDaftarSPTPDAction.setText("Daftar SPTPD");
	        itemDaftarSPTPDAction.setID(4);
	        itemDaftarSPTPDAction.setImage(scaledTo(MainActivator.getImageDescriptor("img/sptpd.png").getImageData().scaledTo(40, 40)));
	        hashMenuItem.put(4, itemDaftarSPTPDAction);
	        itemDaftarSPTPDAction.addSelectionListener(new SelectionAdapter() {
	        	@Override
	        	public void widgetSelected(SelectionEvent e) {
	        		try {
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(DaftarSPTPDView.ID);
					} catch (PartInitException e1) {
						e1.printStackTrace();
					}
	        	}
			});
	        MenuItem itemDaftarSKPDAction = new MenuItem(dropMenuSPTPD, SWT.PUSH);
			itemDaftarSKPDAction.setEnabled(false);
	        itemDaftarSKPDAction.setText("Daftar SKPD");
	        itemDaftarSKPDAction.setID(32);
	        itemDaftarSKPDAction.setImage(scaledTo(MainActivator.getImageDescriptor("img/sptpd.png").getImageData().scaledTo(40, 40)));
	        hashMenuItem.put(32, itemDaftarSKPDAction);
	        itemDaftarSKPDAction.addSelectionListener(new SelectionAdapter() {
	        	@Override
	        	public void widgetSelected(SelectionEvent e) {
	        		try {
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(DaftarSKPDView.ID);
					} catch (PartInitException e1) {
						e1.printStackTrace();
					}
	        	}
			});
	        MenuItem itemInputSPTPDAction = new MenuItem(dropMenuSPTPD, SWT.PUSH);
	        itemInputSPTPDAction.setEnabled(false);
	        itemInputSPTPDAction.setText("Input SPTPD");
	        itemInputSPTPDAction.setID(5);
	        hashMenuItem.put(5, itemInputSPTPDAction);
	        itemInputSPTPDAction.setImage(scaledTo(MainActivator.getImageDescriptor("img/sptpd.png").getImageData().scaledTo(40, 40)));
	        itemInputSPTPDAction.addSelectionListener(new SelectionAdapter() {
	        	@Override
	        	public void widgetSelected(SelectionEvent e) {
	        		try {
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(InputSPTPDView.ID);
					} catch (PartInitException e1) {
						e1.printStackTrace();
					}
	        	}
			});
			
	        MenuItem itemVerifikasiSPTPDAction = new MenuItem(dropMenuSPTPD, SWT.PUSH);
	        itemVerifikasiSPTPDAction.setEnabled(false);
	        itemVerifikasiSPTPDAction.setText("Verifikasi SPTPD");
	        itemVerifikasiSPTPDAction.setID(44);
	        hashMenuItem.put(44, itemVerifikasiSPTPDAction);
	        itemVerifikasiSPTPDAction.setImage(scaledTo(MainActivator.getImageDescriptor("img/sptpd.png").getImageData().scaledTo(40, 40)));
	        itemVerifikasiSPTPDAction.addSelectionListener(new SelectionAdapter() {
	        	@Override
	        	public void widgetSelected(SelectionEvent e) {
	        		try {
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(VerifikasiSPTPDView.ID);
					} catch (PartInitException e1) {
						e1.printStackTrace();
					}
	        	}
			});
	        
			ToolItem tltmPembayaran = new ToolItem(toolBarMain, SWT.NONE);
			tltmPembayaran.setToolTipText("Pembayaran");
			tltmPembayaran.setImage(scaledTo(MainActivator.getImageDescriptor("img/pembayaran.png").getImageData()));
			tltmPembayaran.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {

					final ToolItem toolItem = (ToolItem) e.widget;
		            final ToolBar  toolBar = toolItem.getParent();
		            Point point = toolBar.toDisplay(new Point(e.x, e.y));
		            Point iconSize = new Point(toolItem.getBounds().width,toolItem.getBounds().height);
		            dropMenuPembayaran.setLocation(point.x+(iconSize.x), point.y+(iconSize.y*2));
		            dropMenuPembayaran.setVisible(true);
				}
			});
			dropMenuPembayaran = new Menu(new Shell(), SWT.POP_UP);
			MenuItem itemDaftarSSPDAction = new MenuItem(dropMenuPembayaran, SWT.PUSH);
			itemDaftarSSPDAction.setEnabled(false);
	        itemDaftarSSPDAction.setText("Daftar SSPD");
	        itemDaftarSSPDAction.setID(7);
	        hashMenuItem.put(7, itemDaftarSSPDAction);
	        itemDaftarSSPDAction.setImage(scaledTo(MainActivator.getImageDescriptor("img/pembayaran.png").getImageData().scaledTo(40, 40)));
	        itemDaftarSSPDAction.addSelectionListener(new SelectionAdapter() {
	        	@Override
	        	public void widgetSelected(SelectionEvent e) {
	        		try {
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(DaftarSSPDView.ID);
					} catch (PartInitException e1) {
						e1.printStackTrace();
					}
	        	}
			});
	        MenuItem itemNotaAntrianAction = new MenuItem(dropMenuPembayaran, SWT.PUSH);
	        itemNotaAntrianAction.setEnabled(false);
	        itemNotaAntrianAction.setText("Nota Antrian / Pembayaran");
	        itemNotaAntrianAction.setID(6);
	        hashMenuItem.put(6, itemNotaAntrianAction);
	        itemNotaAntrianAction.setImage(scaledTo(MainActivator.getImageDescriptor("img/pembayaran.png").getImageData().scaledTo(40, 40)));
	        itemNotaAntrianAction.addSelectionListener(new SelectionAdapter() {
	        	@Override
	        	public void widgetSelected(SelectionEvent e) {
	        		try {
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(SSPDView.ID);
					} catch (PartInitException e1) {
						e1.printStackTrace();
					}
	        	}
			});
	        MenuItem itemListAnriSSPDAction = new MenuItem(dropMenuPembayaran, SWT.PUSH);
	        itemListAnriSSPDAction.setEnabled(false);
	        itemListAnriSSPDAction.setText("List Antrian Pembayaran");
	        itemListAnriSSPDAction.setID(21);
	        hashMenuItem.put(21, itemListAnriSSPDAction);
	        itemListAnriSSPDAction.setImage(scaledTo(MainActivator.getImageDescriptor("img/pembayaran.png").getImageData().scaledTo(40, 40)));
	        itemListAnriSSPDAction.addSelectionListener(new SelectionAdapter() {
	        	@Override
	        	public void widgetSelected(SelectionEvent e) {
	        		IPerspectiveDescriptor descriptor = PlatformUI.getWorkbench().getPerspectiveRegistry().findPerspectiveWithId("com.dispenda.sspd.perspectives.PembayaranPerspective");
	                PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().setPerspective(descriptor);
	                try {
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(AntriView.ID);
					} catch (PartInitException e1) {
						e1.printStackTrace();
					}
	        	}
			});
			
			ToolItem tltmPemeriksaan = new ToolItem(toolBarMain, SWT.NONE);
			tltmPemeriksaan.setToolTipText("Pemeriksaan");
			tltmPemeriksaan.setImage(scaledTo(MainActivator.getImageDescriptor("img/pemeriksaan.png").getImageData()));
			tltmPemeriksaan.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {

					final ToolItem toolItem = (ToolItem) e.widget;
		            final ToolBar  toolBar = toolItem.getParent();
		            Point point = toolBar.toDisplay(new Point(e.x, e.y));
		            Point iconSize = new Point(toolItem.getBounds().width,toolItem.getBounds().height);
		            dropMenuPemeriksaan.setLocation(point.x+(iconSize.x), point.y+(iconSize.y*3));
		            dropMenuPemeriksaan.setVisible(true);
				}
			});
			dropMenuPemeriksaan = new Menu(new Shell(), SWT.POP_UP);
	        MenuItem itemDaftarPemeriksaanAction = new MenuItem(dropMenuPemeriksaan, SWT.PUSH);
	        itemDaftarPemeriksaanAction.setEnabled(false);
	        itemDaftarPemeriksaanAction.setText("Daftar Pemeriksaan");
	        itemDaftarPemeriksaanAction.setID(9);
	        hashMenuItem.put(9, itemDaftarPemeriksaanAction);
	        itemDaftarPemeriksaanAction.setImage(scaledTo(MainActivator.getImageDescriptor("img/pemeriksaan.png").getImageData().scaledTo(40, 40)));
	        itemDaftarPemeriksaanAction.addSelectionListener(new SelectionAdapter() {
	        	@Override
	        	public void widgetSelected(SelectionEvent e) {
	        		try {
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(DaftarPemeriksaanView.ID);
					} catch (PartInitException e1) {
						e1.printStackTrace();
					}
	        	}
			});
	        MenuItem itemNotaPerAction = new MenuItem(dropMenuPemeriksaan, SWT.PUSH);
	        itemNotaPerAction.setEnabled(false);
	        itemNotaPerAction.setText("Nota Perhitungan");
	        itemNotaPerAction.setID(10);
	        hashMenuItem.put(10, itemNotaPerAction);
	        itemNotaPerAction.setImage(scaledTo(MainActivator.getImageDescriptor("img/pemeriksaan.png").getImageData().scaledTo(40, 40)));
	        itemNotaPerAction.addSelectionListener(new SelectionAdapter() {
	        	@Override
	        	public void widgetSelected(SelectionEvent e) {
	        		try {
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(NotaPerhitunganView.ID);
					} catch (PartInitException e1) {
						e1.printStackTrace();
					}
	        	}
			});
	        MenuItem itemPemeriksaanAction = new MenuItem(dropMenuPemeriksaan, SWT.PUSH);
	        itemPemeriksaanAction.setEnabled(false);
	        itemPemeriksaanAction.setText("Pemeriksaan");
	        itemPemeriksaanAction.setID(8);
	        hashMenuItem.put(8, itemPemeriksaanAction);
	        itemPemeriksaanAction.setImage(scaledTo(MainActivator.getImageDescriptor("img/pemeriksaan.png").getImageData().scaledTo(40, 40)));
	        itemPemeriksaanAction.addSelectionListener(new SelectionAdapter() {
	        	@Override
	        	public void widgetSelected(SelectionEvent e) {
	        		try {
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(PemeriksaanView.ID);
					} catch (PartInitException e1) {
						e1.printStackTrace();
					}
	        	}
			});
	        MenuItem itemSKPDKBAction = new MenuItem(dropMenuPemeriksaan, SWT.RADIO);
	        itemSKPDKBAction.setEnabled(false);
	        itemSKPDKBAction.setText("SKPDKB");
	        itemSKPDKBAction.setID(22);
	        hashMenuItem.put(22, itemSKPDKBAction);
	        itemSKPDKBAction.setImage(scaledTo(MainActivator.getImageDescriptor("img/pemeriksaan.png").getImageData().scaledTo(40, 40)));
	        itemSKPDKBAction.addSelectionListener(new SelectionAdapter() {
	        	@Override
	        	public void widgetSelected(SelectionEvent e) {
	        		try {
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(SKPDKBView.ID);
					} catch (PartInitException e1) {
						e1.printStackTrace();
					}
	        	}
			});
	        MenuItem itemRekomendasiPeriksaAction = new MenuItem(dropMenuPemeriksaan, SWT.RADIO);
	        itemRekomendasiPeriksaAction.setEnabled(false);
	        itemRekomendasiPeriksaAction.setText("Rekomendasi Periksa");
	        itemRekomendasiPeriksaAction.setID(23);
	        hashMenuItem.put(23, itemRekomendasiPeriksaAction);
	        itemRekomendasiPeriksaAction.setImage(scaledTo(MainActivator.getImageDescriptor("img/pemeriksaan.png").getImageData().scaledTo(40, 40)));
	        itemRekomendasiPeriksaAction.addSelectionListener(new SelectionAdapter() {
	        	@Override
	        	public void widgetSelected(SelectionEvent e) {
	        		try {
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(RekomendasiPeriksaView.ID);
					} catch (PartInitException e1) {
						e1.printStackTrace();
					}
	        	}
			});
	        MenuItem itemInputSKPDLBCov = new MenuItem(dropMenuPemeriksaan, SWT.RADIO);
	        itemInputSKPDLBCov.setEnabled(true);
	        itemInputSKPDLBCov.setText("Input SKPDLB (Covid-19)");
	        itemInputSKPDLBCov.setID(43);
	        hashMenuItem.put(43, itemInputSKPDLBCov);
	        itemInputSKPDLBCov.setImage(scaledTo(MainActivator.getImageDescriptor("img/pemeriksaan.png").getImageData().scaledTo(40, 40)));
	        itemInputSKPDLBCov.addSelectionListener(new SelectionAdapter() {
	        	@Override
	        	public void widgetSelected(SelectionEvent e) {
	        		try {
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(InputSKPDLBCovView.ID);
					} catch (PartInitException e1) {
						e1.printStackTrace();
					}
	        	}
			});
			ToolItem tltmAngsuran = new ToolItem(toolBarMain, SWT.NONE);
			tltmAngsuran.setToolTipText("Angsuran");
			tltmAngsuran.setImage(scaledTo(MainActivator.getImageDescriptor("img/angsuran.png").getImageData()));
			tltmAngsuran.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {

					final ToolItem toolItem = (ToolItem) e.widget;
		            final ToolBar  toolBar = toolItem.getParent();
		            Point point = toolBar.toDisplay(new Point(e.x, e.y));
		            Point iconSize = new Point(toolItem.getBounds().width,toolItem.getBounds().height);
		            dropMenuAngsuran.setLocation(point.x+(iconSize.x), point.y+(iconSize.y*4));
		            dropMenuAngsuran.setVisible(true);
				}
			});
	        dropMenuAngsuran = new Menu(new Shell(), SWT.POP_UP);
			MenuItem itemDaftarPermohonanAction = new MenuItem(dropMenuAngsuran, SWT.PUSH);
			itemDaftarPermohonanAction.setEnabled(false);
	        itemDaftarPermohonanAction.setText("Daftar Permohonan");
	        itemDaftarPermohonanAction.setID(12);
	        hashMenuItem.put(12, itemDaftarPermohonanAction);
	        itemDaftarPermohonanAction.setImage(scaledTo(MainActivator.getImageDescriptor("img/angsuran.png").getImageData().scaledTo(40, 40)));
	        itemDaftarPermohonanAction.addSelectionListener(new SelectionAdapter() {
	        	@Override
	        	public void widgetSelected(SelectionEvent e) {
	        		try {
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(DaftarPermohonanView.ID);
					} catch (PartInitException e1) {
						e1.printStackTrace();
					}
	        	}
			});
	        MenuItem itemPermohonanDAction = new MenuItem(dropMenuAngsuran, SWT.PUSH);
	        itemPermohonanDAction.setEnabled(false);
	        itemPermohonanDAction.setText("Permohonan");
	        itemPermohonanDAction.setID(11);
	        hashMenuItem.put(11, itemPermohonanDAction);
	        itemPermohonanDAction.setImage(scaledTo(MainActivator.getImageDescriptor("img/angsuran.png").getImageData().scaledTo(40, 40)));
	        itemPermohonanDAction.addSelectionListener(new SelectionAdapter() {
	        	@Override
	        	public void widgetSelected(SelectionEvent e) {
	        		try {
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(PermohonanView.ID);
					} catch (PartInitException e1) {
						e1.printStackTrace();
					}
	        	}
			});
	        MenuItem itemDaftarAngsuranAction = new MenuItem(dropMenuAngsuran, SWT.PUSH);
	        itemDaftarAngsuranAction.setEnabled(false);
	        itemDaftarAngsuranAction.setText("Daftar Angsuran");
	        itemDaftarAngsuranAction.setID(24);
	        hashMenuItem.put(24, itemDaftarAngsuranAction);
	        itemDaftarAngsuranAction.setImage(scaledTo(MainActivator.getImageDescriptor("img/angsuran.png").getImageData().scaledTo(40, 40)));
	        itemDaftarAngsuranAction.addSelectionListener(new SelectionAdapter() {
	        	@Override
	        	public void widgetSelected(SelectionEvent e) {
	        		try {
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(DaftarAngsuranView.ID);
					} catch (PartInitException e1) {
						e1.printStackTrace();
					}
	        	}
			});
			
			ToolItem tltmTunggakan = new ToolItem(toolBarMain, SWT.NONE);
			tltmTunggakan.setToolTipText("Tunggakan");
			tltmTunggakan.setImage(scaledTo(MainActivator.getImageDescriptor("img/tunggakan.png").getImageData()));
			tltmTunggakan.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {

					final ToolItem toolItem = (ToolItem) e.widget;
		            final ToolBar  toolBar = toolItem.getParent();
		            Point point = toolBar.toDisplay(new Point(e.x, e.y));
		            Point iconSize = new Point(toolItem.getBounds().width,toolItem.getBounds().height);
		            dropMenuTunggakan.setLocation(point.x+(iconSize.x), point.y+(iconSize.y*5));
		            dropMenuTunggakan.setVisible(true);
				}
			});
			dropMenuTunggakan = new Menu(new Shell(), SWT.POP_UP);
	        MenuItem itemTSPTPDAction = new MenuItem(dropMenuTunggakan, SWT.PUSH);
	        itemTSPTPDAction.setEnabled(false);
	        itemTSPTPDAction.setText("Tunggakan SPTPD & SKPDKB");
	        itemTSPTPDAction.setID(13);
	        hashMenuItem.put(13, itemTSPTPDAction);
	        itemTSPTPDAction.setImage(scaledTo(MainActivator.getImageDescriptor("img/tunggakan.png").getImageData().scaledTo(40, 40)));
	        itemTSPTPDAction.addSelectionListener(new SelectionAdapter() {
	        	public void widgetSelected(SelectionEvent e) {
	        		try {
	        			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(TunggakanSptpdView.ID);
					} catch (WorkbenchException e1) {
						e1.printStackTrace();
					}
	        	}
			});
	        MenuItem itemTunggakanDendaAction = new MenuItem(dropMenuTunggakan, SWT.PUSH);
	        itemTunggakanDendaAction.setEnabled(false);
	        itemTunggakanDendaAction.setText("Tunggakan Denda");
	        itemTunggakanDendaAction.setID(25);
	        hashMenuItem.put(25, itemTunggakanDendaAction);
	        itemTunggakanDendaAction.setImage(scaledTo(MainActivator.getImageDescriptor("img/tunggakan.png").getImageData().scaledTo(40, 40)));
	        itemTunggakanDendaAction.addSelectionListener(new SelectionAdapter() {
	        	public void widgetSelected(SelectionEvent e) {
	        		try {
	        			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(TunggakanDendaView.ID);
					} catch (WorkbenchException e1) {
						e1.printStackTrace();
					}
	        	}
			});
	        MenuItem itemTunggakanWPAction = new MenuItem(dropMenuTunggakan, SWT.PUSH);
	        itemTunggakanWPAction.setEnabled(false);
	        itemTunggakanWPAction.setText("Tunggakan WP");
	        itemTunggakanWPAction.setID(26);
	        hashMenuItem.put(26, itemTunggakanWPAction);
	        itemTunggakanWPAction.setImage(scaledTo(MainActivator.getImageDescriptor("img/tunggakan.png").getImageData().scaledTo(40, 40)));
	        itemTunggakanWPAction.addSelectionListener(new SelectionAdapter() {
	        	public void widgetSelected(SelectionEvent e) {
	        		try {
	        			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(TunggakanWpView.ID);
					} catch (WorkbenchException e1) {
						e1.printStackTrace();
					}
	        	}
			});
			ToolItem tltmLaporan = new ToolItem(toolBarMain, SWT.NONE);
			tltmLaporan.setToolTipText("Laporan");
			tltmLaporan.setImage(scaledTo(MainActivator.getImageDescriptor("img/laporan.png").getImageData()));
			tltmLaporan.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					
					final ToolItem toolItem = (ToolItem) e.widget;
		            final ToolBar  toolBar = toolItem.getParent();
		            Point point = toolBar.toDisplay(new Point(e.x, e.y));
		            Point iconSize = new Point(toolItem.getBounds().width,toolItem.getBounds().height);
		            dropMenuLaporan.setLocation(point.x+(iconSize.x), point.y+(iconSize.y*6));
		            dropMenuLaporan.setVisible(true);
				}
			});
	        dropMenuLaporan = new Menu(new Shell(), SWT.POP_UP);
			MenuItem itemPendapatanBulananAction = new MenuItem(dropMenuLaporan, SWT.PUSH);
			itemPendapatanBulananAction.setEnabled(false);
	        itemPendapatanBulananAction.setText("Laporan Pendapatan Bulanan");
	        itemPendapatanBulananAction.setID(27);
	        hashMenuItem.put(27, itemPendapatanBulananAction);
	        itemPendapatanBulananAction.setImage(scaledTo(MainActivator.getImageDescriptor("img/laporan.png").getImageData().scaledTo(40, 40)));
	        itemPendapatanBulananAction.addSelectionListener(new SelectionAdapter() {
	        	public void widgetSelected(SelectionEvent e) {
	        		try {
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(LaporanPendapatanBulananView.ID);
					} catch (PartInitException e1) {
						e1.printStackTrace();
					}
	        	}
			});
	        /* Hide Kode 1-a
	         * 
	         * MenuItem itemPendapatanHarianAction = new MenuItem(dropMenuLaporan, SWT.PUSH);
	        itemPendapatanHarianAction.setEnabled(false);
	        itemPendapatanHarianAction.setText("Laporan Pendapatan Harian");
	        itemPendapatanHarianAction.setID(28);
	        hashMenuItem.put(28, itemPendapatanHarianAction);
	        itemPendapatanHarianAction.setImage(scaledTo(MainActivator.getImageDescriptor("img/laporan.png").getImageData().scaledTo(40, 40)));
	        itemPendapatanHarianAction.addSelectionListener(new SelectionAdapter() {
	        	public void widgetSelected(SelectionEvent e) {
	        		try {
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(LaporanPendapatanHarianView.ID);
					} catch (PartInitException e1) {
						e1.printStackTrace();
					}
	        	}
			});*/
	        
	        MenuItem itemHistoryWPAction = new MenuItem(dropMenuLaporan, SWT.PUSH);
	        itemHistoryWPAction.setEnabled(false);
	        itemHistoryWPAction.setText("History WP");
	        itemHistoryWPAction.setID(20);
	        hashMenuItem.put(20, itemHistoryWPAction);
	        itemHistoryWPAction.setImage(scaledTo(MainActivator.getImageDescriptor("img/laporan.png").getImageData().scaledTo(40, 40)));
	        itemHistoryWPAction.addSelectionListener(new SelectionAdapter() {
	        	public void widgetSelected(SelectionEvent e) {
	        		try {
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(HistoryWPView.ID);
					} catch (PartInitException e1) {
						e1.printStackTrace();
					}
	        	}
			});
	        MenuItem itemKeuanganAction = new MenuItem(dropMenuLaporan, SWT.PUSH);
	        itemKeuanganAction.setEnabled(false);
	        itemKeuanganAction.setText("Keuangan");
	        itemKeuanganAction.setID(34);
	        hashMenuItem.put(34, itemKeuanganAction);
	        itemKeuanganAction.setImage(scaledTo(MainActivator.getImageDescriptor("img/laporan.png").getImageData().scaledTo(40, 40)));
	        itemKeuanganAction.addSelectionListener(new SelectionAdapter() {
	        	public void widgetSelected(SelectionEvent e) {
	        		try {
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(LaporanKeuanganView.ID);
//						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(LaporanKeuanganHarianView.ID);
//						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(DaftarPemeriksaanKeuanganView.ID);
//						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(RealisasiHarianKeuanganView.ID);
//						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(LaporanDendaSSPD.ID);
					} catch (PartInitException e1) {
						e1.printStackTrace();
					}
	        	}
			});
	        MenuItem itemPiutangAction = new MenuItem(dropMenuLaporan, SWT.PUSH);
	        itemPiutangAction.setEnabled(false);
	        itemPiutangAction.setText("Laporan Piutang");
	        itemPiutangAction.setID(41);
	        hashMenuItem.put(41, itemPiutangAction);
	        itemPiutangAction.setImage(scaledTo(MainActivator.getImageDescriptor("img/laporan.png").getImageData().scaledTo(40, 40)));
	        itemPiutangAction.addSelectionListener(new SelectionAdapter() {
	        	public void widgetSelected(SelectionEvent e) {
	        		try {
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(LaporanPiutang.ID);
					} catch (PartInitException e1) {
						e1.printStackTrace();
					}
	        	}
			});
			
			ToolItem tltmRealisasi = new ToolItem(toolBarMain, SWT.NONE);
			tltmRealisasi.setToolTipText("Realisasi Penerimaan");
			tltmRealisasi.setImage(scaledTo(MainActivator.getImageDescriptor("img/realisasi.png").getImageData()));
			tltmRealisasi.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					final ToolItem toolItem = (ToolItem) e.widget;
		            final ToolBar  toolBar = toolItem.getParent();
		            Point point = toolBar.toDisplay(new Point(e.x, e.y));
		            Point iconSize = new Point(toolItem.getBounds().width,toolItem.getBounds().height);
		            dropMenuRPenerimaan.setLocation(point.x+(iconSize.x), point.y+(iconSize.y*7));
		            dropMenuRPenerimaan.setVisible(true);
				}
			});

	        dropMenuRPenerimaan = new Menu(new Shell(), SWT.POP_UP);
	        /* Hide Kode 2-b
	         * 
	         * MenuItem itemPenerimaanAction = new MenuItem(dropMenuRPenerimaan, SWT.PUSH);
	        itemPenerimaanAction.setEnabled(false);
	        itemPenerimaanAction.setText("Realisasi Penerimaan");
	        itemPenerimaanAction.setID(14);
	        hashMenuItem.put(14, itemPenerimaanAction);
	        itemPenerimaanAction.setImage(scaledTo(MainActivator.getImageDescriptor("img/realisasi.png").getImageData().scaledTo(40, 40)));
	        itemPenerimaanAction.addSelectionListener(new SelectionAdapter() {
	        	public void widgetSelected(SelectionEvent e) {
	        		try {
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(RealisasiPenerimaanView.ID);
					} catch (PartInitException e1) {
						e1.printStackTrace();
					}
	        	}
			});*/
	        MenuItem itemRealisasiHarian = new MenuItem(dropMenuRPenerimaan, SWT.PUSH);
	        itemRealisasiHarian.setEnabled(false);
	        itemRealisasiHarian.setText("Realisasi Harian");
	        itemRealisasiHarian.setID(33);
	        hashMenuItem.put(33, itemRealisasiHarian);
	        itemRealisasiHarian.setImage(scaledTo(MainActivator.getImageDescriptor("img/realisasi.png").getImageData().scaledTo(40, 40)));
	        itemRealisasiHarian.addSelectionListener(new SelectionAdapter() {
	        	public void widgetSelected(SelectionEvent e) {
	        		try {
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(RealisasiHarianView.ID);
					} catch (PartInitException e1) {
						e1.printStackTrace();
					}
	        	}
			});
	        /*MenuItem itemPenerimaanPajakDaerahAction = new MenuItem(dropMenuRPenerimaan, SWT.PUSH);
	        itemPenerimaanPajakDaerahAction.setText("Penerimaan Pajak Daerah");
	        itemPenerimaanPajakDaerahAction.setImage(scaledTo(MainActivator.getImageDescriptor("img/realisasi.png").getImageData().scaledTo(40, 40)));
	        itemPenerimaanPajakDaerahAction.addSelectionListener(new SelectionAdapter() {
	        	public void widgetSelected(SelectionEvent e) {
	        		try {
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(RealisasiPenerimaanPajakDaerahView.ID);
					} catch (PartInitException e1) {
						e1.printStackTrace();
					}
	        	}
			});
	        MenuItem itemPenerimaanWajibPajakAction = new MenuItem(dropMenuRPenerimaan, SWT.PUSH);
	        itemPenerimaanWajibPajakAction.setText("Penerimaan Wajib Pajak");
	        itemPenerimaanWajibPajakAction.setImage(scaledTo(MainActivator.getImageDescriptor("img/realisasi.png").getImageData().scaledTo(40, 40)));
	        itemPenerimaanWajibPajakAction.addSelectionListener(new SelectionAdapter() {
	        	public void widgetSelected(SelectionEvent e) {
	        		try {
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(RealisasiPenerimaanWajibPajakView.ID);
					} catch (PartInitException e1) {
						e1.printStackTrace();
					}
	        	}
			});*/
	        MenuItem itemPenerimaanSptpdAction = new MenuItem(dropMenuRPenerimaan, SWT.PUSH);
	        itemPenerimaanSptpdAction.setEnabled(false);
	        itemPenerimaanSptpdAction.setText("Penerimaan SPTPD");
	        itemPenerimaanSptpdAction.setID(29);
	        hashMenuItem.put(29, itemPenerimaanSptpdAction);
	        itemPenerimaanSptpdAction.setImage(scaledTo(MainActivator.getImageDescriptor("img/realisasi.png").getImageData().scaledTo(40, 40)));
	        itemPenerimaanSptpdAction.addSelectionListener(new SelectionAdapter() {
	        	public void widgetSelected(SelectionEvent e) {
	        		try {
					PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(RealisasiPenerimaanSptpdView.ID);
					} catch (PartInitException e1) {
						e1.printStackTrace();
					}
	        	}
			});
	        MenuItem itemPenerimaanSkpdkbAction = new MenuItem(dropMenuRPenerimaan, SWT.PUSH);
	        itemPenerimaanSkpdkbAction.setEnabled(false);
	        itemPenerimaanSkpdkbAction.setText("Penerimaan SKPDKB");
	        itemPenerimaanSkpdkbAction.setID(30);
	        hashMenuItem.put(30, itemPenerimaanSkpdkbAction);
	        itemPenerimaanSkpdkbAction.setImage(scaledTo(MainActivator.getImageDescriptor("img/realisasi.png").getImageData().scaledTo(40, 40)));
	        itemPenerimaanSkpdkbAction.addSelectionListener(new SelectionAdapter() {
	        	public void widgetSelected(SelectionEvent e) {
	        		try {
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(RealisasiPenerimaanSkpdkbView.ID);
					} catch (PartInitException e1) {
						e1.printStackTrace();
					}
	        	}
			});
	        
	        ToolItem tltmArsip = new ToolItem(toolBarMain, SWT.NONE);
	        tltmArsip.setToolTipText("Arsip");
	        tltmArsip.setImage(scaledTo(MainActivator.getImageDescriptor("img/control_panel.png").getImageData()));
	        tltmArsip.addSelectionListener(new SelectionAdapter() {
	        	public void widgetSelected(SelectionEvent e) {
	        		final ToolItem toolItem = (ToolItem) e.widget;
	        		final ToolBar toolBar = toolItem.getParent();
	        		Point point = toolBar.toDisplay(new Point(e.x, e.y));
		            Point iconSize = new Point(toolItem.getBounds().width,toolItem.getBounds().height);
		            dropMenuCP.setLocation(point.x+(iconSize.x), point.y+(iconSize.y*8));
		            dropMenuArsip.setVisible(true);
	        	}
			});
	        dropMenuArsip = new Menu(new Shell(), SWT.POP_UP);
	        MenuItem itemArsipAction = new MenuItem(dropMenuArsip, SWT.PUSH);
	        itemArsipAction.setEnabled(false);
	        itemArsipAction.setText("Arsip");
	        itemArsipAction.setID(36);
	        hashMenuItem.put(36, itemArsipAction);
	        itemArsipAction.setImage(scaledTo(MainActivator.getImageDescriptor("img/user.png").getImageData()));
	        itemArsipAction.addSelectionListener(new SelectionAdapter() {
	        	@Override
	        	public void widgetSelected(SelectionEvent e) {
	        		try {
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(ArsipView.ID);
					} catch (PartInitException e1) {
						e1.printStackTrace();
					}
	        	}
			});
	        MenuItem itemArsipTransactionAction = new MenuItem(dropMenuArsip, SWT.PUSH);
	        itemArsipTransactionAction.setEnabled(false);
	        itemArsipTransactionAction.setText("Keluar Masuk Arsip");
	        itemArsipTransactionAction.setID(37);
	        hashMenuItem.put(37, itemArsipTransactionAction);
	        itemArsipTransactionAction.setImage(scaledTo(MainActivator.getImageDescriptor("img/user.png").getImageData()));
	        itemArsipTransactionAction.addSelectionListener(new SelectionAdapter() {
	        	@Override
	        	public void widgetSelected(SelectionEvent e) {
	        		try {
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(ArsipTransactionView.ID);
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(VerifikasiDataArsipView.ID);
					} catch (PartInitException e1) {
						e1.printStackTrace();
					}
	        	}
			});
	        MenuItem itemArsipPrintedAction = new MenuItem(dropMenuArsip, SWT.PUSH);
	        itemArsipPrintedAction.setEnabled(false);
	        itemArsipPrintedAction.setText("Cetak Barcode");
	        itemArsipPrintedAction.setID(38);
	        hashMenuItem.put(38, itemArsipPrintedAction);
	        itemArsipPrintedAction.setImage(scaledTo(MainActivator.getImageDescriptor("img/user.png").getImageData()));
	        itemArsipPrintedAction.addSelectionListener(new SelectionAdapter() {
	        	@Override
	        	public void widgetSelected(SelectionEvent e) {
	        		try {
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(ListBarcodePrintView.ID);
					} catch (PartInitException e1) {
						e1.printStackTrace();
					}
	        	}
			});
	        MenuItem itemArsipListImage = new MenuItem(dropMenuArsip, SWT.PUSH);
	        itemArsipListImage.setEnabled(false);
	        itemArsipListImage.setText("Berkas Arsip");
	        itemArsipListImage.setID(42);
	        hashMenuItem.put(42, itemArsipListImage);
	        itemArsipListImage.setImage(scaledTo(MainActivator.getImageDescriptor("img/user.png").getImageData()));
	        itemArsipListImage.addSelectionListener(new SelectionAdapter() {
	        	@Override
	        	public void widgetSelected(SelectionEvent e) {
	        		try {
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(ImageBarcodeToDatabaseView.ID);
					} catch (PartInitException e1) {
						e1.printStackTrace();
					}
	        	}
			});
			
			ToolItem tltmControlPanel = new ToolItem(toolBarMain, SWT.NONE);
			tltmControlPanel.setToolTipText("Control Panel");
			tltmControlPanel.setImage(scaledTo(MainActivator.getImageDescriptor("img/control_panel.png").getImageData()));
			tltmControlPanel.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {

					final ToolItem toolItem = (ToolItem) e.widget;
		            final ToolBar  toolBar = toolItem.getParent();
		            Point point = toolBar.toDisplay(new Point(e.x, e.y));
		            Point iconSize = new Point(toolItem.getBounds().width,toolItem.getBounds().height);
		            dropMenuCP.setLocation(point.x+(iconSize.x), point.y+(iconSize.y*9));
		            dropMenuCP.setVisible(true);
				}
			});
	        dropMenuCP = new Menu(new Shell(), SWT.POP_UP);
	        MenuItem itemUserAction = new MenuItem(dropMenuCP, SWT.PUSH);
	        itemUserAction.setEnabled(false);
	        itemUserAction.setText("Pengguna");
	        itemUserAction.setID(15);
	        hashMenuItem.put(15, itemUserAction);
	        itemUserAction.setImage(scaledTo(MainActivator.getImageDescriptor("img/user.png").getImageData()));
	        itemUserAction.addSelectionListener(new SelectionAdapter() {
	        	@Override
	        	public void widgetSelected(SelectionEvent e) {
	        		try {
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(UserView.ID);
					} catch (PartInitException e1) {
						e1.printStackTrace();
					}
	        	}
			});
	        MenuItem itemKecamatanAction = new MenuItem(dropMenuCP, SWT.PUSH);
	        itemKecamatanAction.setEnabled(false);
	        itemKecamatanAction.setText("Kecamatan");
	        itemKecamatanAction.setID(16);
	        hashMenuItem.put(16, itemKecamatanAction);
	        itemKecamatanAction.setImage(scaledTo(MainActivator.getImageDescriptor("img/kecamatan.png").getImageData()));
	        itemKecamatanAction.addSelectionListener(new SelectionAdapter() {
	        	@Override
	        	public void widgetSelected(SelectionEvent e) {
	        		try {
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(KecamatanView.ID);
					} catch (PartInitException e1) {
						e1.printStackTrace();
					}
	        	}
			});
	        MenuItem itemPajakAction = new MenuItem(dropMenuCP, SWT.PUSH);
	        itemPajakAction.setEnabled(false);
	        itemPajakAction.setText("Pajak");
	        itemPajakAction.setID(17);
	        hashMenuItem.put(17, itemPajakAction);
	        itemPajakAction.setImage(scaledTo(MainActivator.getImageDescriptor("img/subpajak.png").getImageData()));
	        itemPajakAction.addSelectionListener(new SelectionAdapter() {
	        	@Override
	        	public void widgetSelected(SelectionEvent e) {
	        		try {
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(PajakView.ID);
					} catch (PartInitException e1) {
						e1.printStackTrace();
					}
	        	}
			});
	        MenuItem itemPejabatAction = new MenuItem(dropMenuCP, SWT.PUSH);
	        itemPejabatAction.setEnabled(false);
	        itemPejabatAction.setText("Pejabat");
	        itemPejabatAction.setID(19);
	        hashMenuItem.put(19, itemPejabatAction);
	        itemPejabatAction.setImage(scaledTo(MainActivator.getImageDescriptor("img/pejabat.png").getImageData()));
	        itemPejabatAction.addSelectionListener(new SelectionAdapter() {
	        	@Override
	        	public void widgetSelected(SelectionEvent e) {
	        		try {
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(PejabatView.ID);
					} catch (PartInitException e1) {
						e1.printStackTrace();
					}
	        	}
			});
	        MenuItem itemKodefikasiAction = new MenuItem(dropMenuCP, SWT.PUSH);
	        itemKodefikasiAction.setEnabled(false);
	        itemKodefikasiAction.setText("Kodefikasi");
	        itemKodefikasiAction.setID(35);
	        hashMenuItem.put(35, itemKodefikasiAction);
	        itemKodefikasiAction.setImage(scaledTo(MainActivator.getImageDescriptor("img/laporan.png").getImageData().scaledTo(40,40)));
	        itemKodefikasiAction.addSelectionListener(new SelectionAdapter() {
	        	@Override
	        	public void widgetSelected(SelectionEvent e) {
	        		try {
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(KodefikasiView.ID);
					} catch (PartInitException e1) {
						e1.printStackTrace();
					}
	        	}
			});
	        MenuItem itemAwardAction = new MenuItem(dropMenuCP, SWT.PUSH);
	        itemAwardAction.setEnabled(false);
	        itemAwardAction.setText("Award");
	        itemAwardAction.setID(31);
	        hashMenuItem.put(31, itemAwardAction);
	        itemAwardAction.setImage(scaledTo(MainActivator.getImageDescriptor("img/award.png").getImageData().scaledTo(40, 40)));
	        itemAwardAction.addSelectionListener(new SelectionAdapter() {
	        	@Override
	        	public void widgetSelected(SelectionEvent e) {
	        		try {
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(AwardView.ID);
					} catch (PartInitException e1) {
						e1.printStackTrace();
					}
	        	}
			});
	        
	        MenuItem itemPreferencesAction = new MenuItem(dropMenuCP, SWT.PUSH);
	        itemPreferencesAction.setEnabled(true);
	        itemPreferencesAction.setText("Preferences");
	        itemPreferencesAction.setImage(scaledTo(MainActivator.getImageDescriptor("img/control_panel.png").getImageData().scaledTo(40, 40)));
	        itemPreferencesAction.addSelectionListener(new SelectionAdapter() {
	        	@Override
	        	public void widgetSelected(SelectionEvent e) {
	        		try {
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(PreferencesView.ID);
					} catch (PartInitException e1) {
						e1.printStackTrace();
					}
	        	}
			});
	        
	        /*MenuItem itemPermintaanPerubahanAction = new MenuItem(dropMenuCP, SWT.PUSH);
	        itemPermintaanPerubahanAction.setEnabled(true);
	        itemPermintaanPerubahanAction.setText("Permintaan Perubahan");
	        itemPermintaanPerubahanAction.setImage(scaledTo(MainActivator.getImageDescriptor("img/control_panel.png").getImageData().scaledTo(40, 40)));
	        itemPermintaanPerubahanAction.addSelectionListener(new SelectionAdapter() {
	        	@Override
	        	public void widgetSelected(SelectionEvent e) {
	        		try {
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(PermintaanPerubahanView.ID);
					} catch (PartInitException e1) {
						e1.printStackTrace();
					}
	        	}
			});*/
			
/*			ToolItem tltmHistory = new ToolItem(toolBarMain, SWT.NONE);
			tltmHistory.setToolTipText("History Wajib Pajak");
			tltmHistory.setImage(scaledTo(MainActivator.getImageDescriptor("img/history.png").getImageData()));
			tltmHistory.addSelectionListener(new SelectionAdapter() {
				Menu dropMenu = null;
				public void widgetSelected(SelectionEvent e) {
					if(dropMenu == null) {
		                dropMenu = new Menu(Display.getCurrent().getActiveShell(), SWT.POP_UP);
		                composite.setMenu(dropMenu);
		                MenuItem itemDaftarSPTPDAction = new MenuItem(dropMenu, SWT.PUSH);
		                itemDaftarSPTPDAction.setText("History Wajib Pajak");
		                itemDaftarSPTPDAction.setImage(scaledTo(MainActivator.getImageDescriptor("img/history.png").getImageData().scaledTo(40, 40)));
		            }

					final ToolItem toolItem = (ToolItem) e.widget;
		            final ToolBar  toolBar = toolItem.getParent();
		            Point point = toolBar.toDisplay(new Point(e.x, e.y));
		            Point iconSize = new Point(toolItem.getBounds().width,toolItem.getBounds().height);
		            dropMenu.setLocation(point.x+(iconSize.x), point.y+(iconSize.y*9));
		            dropMenu.setVisible(true);
				}
			});*/
			
			ToolItem tltmShutdown = new ToolItem(toolBarMain, SWT.NONE);
			tltmShutdown.setImage(scaledTo(MainActivator.getImageDescriptor("img/close.png").getImageData()));
			tltmShutdown.setToolTipText("Matikan Program");
			tltmShutdown.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					boolean result = MessageDialog.openConfirm(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Apakah anda yakin ingin menutup aplikasi ini?");
					if(result){
						Display.getCurrent().getActiveShell().close();
						System.exit(0);
					}
				}
			});
		}
		
		
		
		List<UserModul> listUserModul = ControllerFactory.getMainController().getCpUserDAOImpl().getAllUserModul(GlobalVariable.userModul.getIdUser());
		Iterator<UserModul> iterator = listUserModul.iterator();
		while(iterator.hasNext()){
			UserModul userModul = iterator.next();
			MenuItem item = hashMenuItem.get(userModul.getIdSubModul());
			if (hashMenuItem.get(userModul.getIdSubModul()) != null){
				if(userModul.getBuka())
					item.setEnabled(true);
				else
					item.setEnabled(false);
			}
		}
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	private Image scaledTo(ImageData imgData){
		int width = 0;
		if(Preferences.ORIENTATION == SWT.HORIZONTAL)
			width = (imgData.width)*Display.getCurrent().getClientArea().width/DEFAULT_SIZE.x;
		else
			width = (imgData.width-10)*Display.getCurrent().getClientArea().width/DEFAULT_SIZE.x;
		ImageData scaledImage = imgData.scaledTo(width, width);
		return new Image(Display.getCurrent(), scaledImage);
	}
}
