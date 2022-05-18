package com.dispenda.pendaftaran.views;

import java.text.SimpleDateFormat;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
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
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.SWTResourceManager;

import com.dispenda.controller.UIController;
import com.dispenda.model.BidangUsahaProvider;
import com.dispenda.model.KecamatanProvider;
import com.dispenda.model.KelurahanProvider;
import com.dispenda.model.PajakProvider;
import com.dispenda.model.PendaftaranAntrian;
import com.dispenda.model.PendaftaranAntrianProvider;
import com.dispenda.object.Preferences;
import com.dispenda.widget.SuratIzinTableEditorComposite;

public class TerimaPendaftaranView extends ViewPart {
	public TerimaPendaftaranView() {
	}
	
	public static final String ID = TerimaPendaftaranView.class.getName();
	private Table table;
	private Text txtPeng;
	private Text txtNoPendaftaran;
	private Text txtKodePajak;
	private Text txtNoPajak;
	private Text txtKodeKecamatan;
	private Text txtKodeKelurahan;
	private Text txtNoFormulir;
	private Combo cmbJenisPajak;
	private Combo cmbJenisAssesment;
	private DateTime calTanggalDaftar;
	private Text txtJabatan;
	private Combo cmbKewajibanPajak;
	private Combo cmbBidangUsaha;
	private Text txtNamaBadan;
	private Text txtPemilik;
	private Text txtKeterangan;
	private Button chkInsidentil;
	private Text txtJalanPribadi;
	private Combo cmbKecamatanPribadi;
	private Combo cmbKelurahanPribadi;
	private Text txtKotaPribadi;
	private Text txtKodePosPribadi;
	private Text txtTelponPribadi;
	private Text txtJalanBadan;
	private Combo cmbKecamatanBadan;
	private Combo cmbKelurahanBadan;
	private Text txtKotaBadan;
	private Text txtKodePosBadan;
	private Text txtTelponBadan;
	private Button btnWni;
	private Button btnWna;
	private Button btnKtp;
	private Button btnSim;
	private Button btnPassport;
	private Text txtBuktiDiri;
	private Text txtNoKK;
	private SuratIzinTableEditorComposite suratIzinTable;
	private Button chkAktif;
	private Text txtKeteranganNonAktif;
	private Button chkTutup;
	private Group grpTutup;
	private Text txtSuratKeteranganTutup;
	private Text txtKeteranganTutup;
	private TableViewer tableViewer;

	public void createPartControl(Composite parent) {
//		parent.setBackground(new Color(Display.getCurrent(), 32, 172, 192));
		
		ScrolledComposite scrolledComposite = new ScrolledComposite(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		
		Composite composite = new Composite(scrolledComposite, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));
		
		Group grpListWP = new Group(composite, SWT.NONE);
		grpListWP.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		grpListWP.setLayout(new GridLayout(1, false));
		grpListWP.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		grpListWP.setText("Daftar Wajib Pajak");
		
		tableViewer = new TableViewer(grpListWP, SWT.BORDER | SWT.FULL_SELECTION |SWT.MULTI  | SWT.VIRTUAL);
		table = tableViewer.getTable();
		table.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				loadDataWajibPajak((PendaftaranAntrian)tableViewer.getElementAt(table.getSelectionIndex()));
			}
		});
		table.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		table.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		table.setHeaderVisible(true);
		
		String[] titles = { "No. Antrian", "Tanggal Daftar", "No. Formulir", "Nama Badan", "Alamat Badan", "Bidang Usaha", "Nama Wajib Pajak"};
	    int[] bounds = { 110, 135,110,135, 150, 150, 180};
		
		TableViewerColumn tableViewerColumn = createTableViewerColumn(titles[0], bounds[0], 0);
		tableViewerColumn.setLabelProvider(new ColumnLabelProvider(){
			public String getText(Object element) {
				PendaftaranAntrian antri = (PendaftaranAntrian) element;
				return Integer.toString(antri.getIndex());
			}
		});

		TableViewerColumn tableViewerColumn_1 = createTableViewerColumn(titles[1], bounds[1], 1);
		tableViewerColumn_1.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				PendaftaranAntrian antri = (PendaftaranAntrian) element;
				SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-YYYY");
				return sdf.format(antri.getTanggalDaftar());
			}
		});
		
		TableViewerColumn tableViewerColumn_2 = createTableViewerColumn(titles[2], bounds[2], 2);
		tableViewerColumn_2.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				PendaftaranAntrian antri = (PendaftaranAntrian) element;
				return antri.getNoFormulir();
			}
		});
		
		TableViewerColumn tableViewerColumn_4 = createTableViewerColumn(titles[3], bounds[3], 3);
		tableViewerColumn_4.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				PendaftaranAntrian antri = (PendaftaranAntrian) element;
				return antri.getNamaBadan();
			}
		});
		
		TableViewerColumn tableViewerColumn_5 = createTableViewerColumn(titles[4], bounds[4], 4);
		tableViewerColumn_5.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				PendaftaranAntrian antri = (PendaftaranAntrian) element;
				return antri.getAlabadJalan();
			}
		});
		
		TableViewerColumn tableViewerColumn_6 = createTableViewerColumn(titles[5], bounds[5], 5);
		tableViewerColumn_6.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				PendaftaranAntrian antri = (PendaftaranAntrian) element;
				return antri.getIdSubPajak().toString();
			}
		});
		
		TableViewerColumn tableViewerColumn_3 = createTableViewerColumn(titles[6], bounds[6], 6);
		tableViewerColumn_3.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				PendaftaranAntrian antri = (PendaftaranAntrian) element;
				return antri.getNamaPemilik();
			}
		});
		
		tableViewer.setContentProvider(new ArrayContentProvider());
		tableViewer.setInput(PendaftaranAntrianProvider.INSTANCE.getAntri());
		getSite().setSelectionProvider(tableViewer);
		
		Composite grpDataWP = new Composite(composite, SWT.NONE);
		grpDataWP.setLayout(new GridLayout(2, false));
		grpDataWP.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Group grpPendaftaran = new Group(grpDataWP, SWT.NONE);
		grpPendaftaran.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BORDER));
		grpPendaftaran.setLayout(new GridLayout(6, false));
		grpPendaftaran.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 2, 1));
		grpPendaftaran.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.BOLD));
		grpPendaftaran.setText("Data Wajib Pajak");
		
		Label lblNoPendaftaran = new Label(grpPendaftaran, SWT.NONE);
		lblNoPendaftaran.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblNoPendaftaran.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblNoPendaftaran.setText("No Pendaftaran*");
		
		Label label = new Label(grpPendaftaran, SWT.NONE);
		label.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		label.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		GridData gd_label = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 13);
		gd_label.widthHint = 35;
		label.setLayoutData(gd_label);
		
		txtPeng = new Text(grpPendaftaran, SWT.BORDER);
		txtPeng.setEnabled(false);
		txtPeng.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtPeng.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		txtPeng.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		GridData gd_txtPeng = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
		gd_txtPeng.widthHint = 71;
		txtPeng.setLayoutData(gd_txtPeng);
		txtPeng.setText("973.SI/Peng/");
		
		txtNoPendaftaran = new Text(grpPendaftaran, SWT.BORDER);
		txtNoPendaftaran.setEnabled(false);
		txtNoPendaftaran.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtNoPendaftaran.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		txtNoPendaftaran.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		GridData gd_txtNoPendaftaran = new GridData(SWT.LEFT, SWT.CENTER, true, false, 2, 1);
		gd_txtNoPendaftaran.widthHint = 105;
		txtNoPendaftaran.setLayoutData(gd_txtNoPendaftaran);
		txtNoPendaftaran.setEditable(false);
		
		Label lblNpwpd_1 = new Label(grpPendaftaran, SWT.NONE);
		lblNpwpd_1.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblNpwpd_1.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblNpwpd_1.setText("NPWPD*");
		
		txtKodePajak = new Text(grpPendaftaran, SWT.BORDER);
		txtKodePajak.setEnabled(false);
		txtKodePajak.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtKodePajak.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		txtKodePajak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		GridData gd_txtKodePajak = new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1);
		gd_txtKodePajak.widthHint = 10;
		txtKodePajak.setLayoutData(gd_txtKodePajak);
		txtKodePajak.setEditable(false);
		
		txtNoPajak = new Text(grpPendaftaran, SWT.BORDER | SWT.RIGHT);
		txtNoPajak.setEnabled(false);
		txtNoPajak.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(e.detail == SWT.TRAVERSE_RETURN){
					
				}
			}
		});
		txtNoPajak.addVerifyListener(new VerifyListener() {
			public void verifyText(VerifyEvent e) {
				String string = e.text;
		        char[] chars = new char[string.length()];
		        string.getChars(0, chars.length, chars, 0);
		        for (int i = 0; i < chars.length; i++) {
		        	if (!('0' <= chars[i] && chars[i] <= '9')) {
		        		e.doit = false;
		        		return;
		        	}
		        }
			}
		});
		txtNoPajak.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtNoPajak.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		txtNoPajak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		GridData gd_txtNoPajak = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_txtNoPajak.widthHint = 45;
		txtNoPajak.setLayoutData(gd_txtNoPajak);
		txtNoPajak.setEditable(false);
		
		txtKodeKecamatan = new Text(grpPendaftaran, SWT.BORDER);
		txtKodeKecamatan.setEnabled(false);
		txtKodeKecamatan.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtKodeKecamatan.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		txtKodeKecamatan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		GridData gd_txtKodeKecamatan = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_txtKodeKecamatan.widthHint = 10;
		txtKodeKecamatan.setLayoutData(gd_txtKodeKecamatan);
		txtKodeKecamatan.setEditable(false);
		
		txtKodeKelurahan = new Text(grpPendaftaran, SWT.BORDER);
		txtKodeKelurahan.setEnabled(false);
		txtKodeKelurahan.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtKodeKelurahan.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		txtKodeKelurahan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		GridData gd_txtKodeKelurahan = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_txtKodeKelurahan.widthHint = 10;
		txtKodeKelurahan.setLayoutData(gd_txtKodeKelurahan);
		txtKodeKelurahan.setEditable(false);
		
		Label lblNoFormulir = new Label(grpPendaftaran, SWT.NONE);
		lblNoFormulir.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblNoFormulir.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblNoFormulir.setText("No. Formulir");
		
		txtNoFormulir = new Text(grpPendaftaran, SWT.BORDER);
		txtNoFormulir.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		txtNoFormulir.setEnabled(false);
		txtNoFormulir.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtNoFormulir.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtNoFormulir.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1));
		
		Label lblJenisAsesment_1 = new Label(grpPendaftaran, SWT.NONE);
		lblJenisAsesment_1.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblJenisAsesment_1.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblJenisAsesment_1.setText("Jenis Pajak*");
		
		cmbJenisPajak = new Combo(grpPendaftaran, SWT.NONE);
		cmbJenisPajak.setEnabled(false);
		cmbJenisPajak.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		cmbJenisPajak.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		cmbJenisPajak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		cmbJenisPajak.setItems(new String[] {"Badan", "Pribadi"});
		cmbJenisPajak.setData("Badan", "B");
		cmbJenisPajak.setData("Pribadi", "P");
		cmbJenisPajak.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 4, 1));
		
		Label lblJenisAsesment_2 = new Label(grpPendaftaran, SWT.NONE);
		lblJenisAsesment_2.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblJenisAsesment_2.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblJenisAsesment_2.setText("Jenis Assesment");
		
		cmbJenisAssesment = new Combo(grpPendaftaran, SWT.NONE);
		cmbJenisAssesment.setEnabled(false);
		cmbJenisAssesment.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		cmbJenisAssesment.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		cmbJenisAssesment.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		cmbJenisAssesment.setItems(new String[] {"Self", "Official"});
		cmbJenisAssesment.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 4, 1));
		
		Label lblTanggalDaftar = new Label(grpPendaftaran, SWT.NONE);
		lblTanggalDaftar.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblTanggalDaftar.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblTanggalDaftar.setText("Tanggal Daftar*");
		
		calTanggalDaftar = new DateTime(grpPendaftaran, SWT.BORDER | SWT.DROP_DOWN);
		calTanggalDaftar.setEnabled(false);
		calTanggalDaftar.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		calTanggalDaftar.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		calTanggalDaftar.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		calTanggalDaftar.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 4, 1));
		
		Label lblNamaBadan = new Label(grpPendaftaran, SWT.NONE);
		lblNamaBadan.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblNamaBadan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblNamaBadan.setText("Nama Badan / Merk Usaha*");
		
		txtNamaBadan = new Text(grpPendaftaran, SWT.BORDER);
		txtNamaBadan.setEnabled(false);
		txtNamaBadan.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtNamaBadan.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		txtNamaBadan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		GridData gd_txtNamaBadan = new GridData(SWT.FILL, SWT.CENTER, false, false, 4, 1);
		gd_txtNamaBadan.widthHint = 257;
		txtNamaBadan.setLayoutData(gd_txtNamaBadan);
		
		Label lblNamaPemilik = new Label(grpPendaftaran, SWT.NONE);
		lblNamaPemilik.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblNamaPemilik.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblNamaPemilik.setText("Nama Pemilik*");
		
		txtPemilik = new Text(grpPendaftaran, SWT.BORDER);
		txtPemilik.setEnabled(false);
		txtPemilik.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtPemilik.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		txtPemilik.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		GridData gd_txtPemilik = new GridData(SWT.FILL, SWT.TOP, false, false, 4, 1);
		gd_txtPemilik.widthHint = 257;
		txtPemilik.setLayoutData(gd_txtPemilik);
		
		Label lblJabatan = new Label(grpPendaftaran, SWT.NONE);
		lblJabatan.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblJabatan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblJabatan.setText("Jabatan*");
		
		txtJabatan = new Text(grpPendaftaran, SWT.BORDER);
		txtJabatan.setEnabled(false);
		txtJabatan.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtJabatan.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		txtJabatan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		GridData gd_txtJabatan = new GridData(SWT.FILL, SWT.CENTER, false, false, 4, 1);
		gd_txtJabatan.widthHint = 257;
		txtJabatan.setLayoutData(gd_txtJabatan);
		
		Label lblKewajibanPajak = new Label(grpPendaftaran, SWT.NONE);
		lblKewajibanPajak.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblKewajibanPajak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblKewajibanPajak.setText("Kewajiban Pajak*");
		
		cmbKewajibanPajak = new Combo(grpPendaftaran, SWT.NONE);
		cmbKewajibanPajak.setEnabled(false);
		cmbKewajibanPajak.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				UIController.INSTANCE.loadBidangUsaha(cmbBidangUsaha, BidangUsahaProvider.INSTANCE.getBidangUsaha((Integer)cmbKewajibanPajak.getData(Integer.toString(cmbKewajibanPajak.getSelectionIndex()))).toArray());
			}
		});
		UIController.INSTANCE.loadPajak(cmbKewajibanPajak, PajakProvider.INSTANCE.getPajak().toArray());
		cmbKewajibanPajak.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		cmbKewajibanPajak.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		cmbKewajibanPajak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		cmbKewajibanPajak.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 4, 1));
		
		Label lblBidangUsaha = new Label(grpPendaftaran, SWT.NONE);
		lblBidangUsaha.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblBidangUsaha.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblBidangUsaha.setText("Bidang Usaha*");
		
		cmbBidangUsaha = new Combo(grpPendaftaran, SWT.NONE);
		cmbBidangUsaha.setEnabled(false);
		cmbBidangUsaha.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		cmbBidangUsaha.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		cmbBidangUsaha.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		cmbBidangUsaha.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 4, 1));
		
		Label lblKeterangan = new Label(grpPendaftaran, SWT.NONE);
		lblKeterangan.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblKeterangan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblKeterangan.setText("Keterangan*");
		
		txtKeterangan = new Text(grpPendaftaran, SWT.BORDER);
		txtKeterangan.setEnabled(false);
		txtKeterangan.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtKeterangan.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		txtKeterangan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtKeterangan.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 4, 1));
		
		Label lblInsidentil = new Label(grpPendaftaran, SWT.NONE);
		lblInsidentil.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblInsidentil.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblInsidentil.setText("Insidentil*");
		
		chkInsidentil = new Button(grpPendaftaran, SWT.CHECK);
		chkInsidentil.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		chkInsidentil.setEnabled(false);
		new Label(grpPendaftaran, SWT.NONE);
		new Label(grpPendaftaran, SWT.NONE);
		new Label(grpPendaftaran, SWT.NONE);
		
		Group grpAlamatTempatTinggal = new Group(grpDataWP, SWT.NONE);
		grpAlamatTempatTinggal.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		grpAlamatTempatTinggal.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.BOLD));
		grpAlamatTempatTinggal.setText("Alamat Tempat Tinggal");
		grpAlamatTempatTinggal.setLayout(new GridLayout(2, false));
		
		Label lblJalan = new Label(grpAlamatTempatTinggal, SWT.NONE);
		lblJalan.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblJalan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblJalan.setText("Jalan");
		
		txtJalanPribadi = new Text(grpAlamatTempatTinggal, SWT.BORDER);
		txtJalanPribadi.setEnabled(false);
		txtJalanPribadi.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		txtJalanPribadi.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtJalanPribadi.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		txtJalanPribadi.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		Label lblKecamatan = new Label(grpAlamatTempatTinggal, SWT.NONE);
		lblKecamatan.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblKecamatan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblKecamatan.setText("Kecamatan");
		
		cmbKecamatanPribadi = new Combo(grpAlamatTempatTinggal, SWT.NONE);
		cmbKecamatanPribadi.setEnabled(false);
		cmbKecamatanPribadi.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		cmbKecamatanPribadi.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		cmbKecamatanPribadi.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		cmbKecamatanPribadi.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		cmbKecamatanPribadi.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Combo c = (Combo)e.widget;
				UIController.INSTANCE.loadKelurahan(cmbKelurahanPribadi, KelurahanProvider.INSTANCE.getKelurahan((Integer)c.getData(Integer.toString(c.getSelectionIndex()))).toArray());
			}
		});
		UIController.INSTANCE.loadKecamatan(cmbKecamatanPribadi, KecamatanProvider.INSTANCE.getKecamatan().toArray());
		
		Label lblKelurahan = new Label(grpAlamatTempatTinggal, SWT.NONE);
		lblKelurahan.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblKelurahan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblKelurahan.setText("Kelurahan");
		
		cmbKelurahanPribadi = new Combo(grpAlamatTempatTinggal, SWT.NONE);
		cmbKelurahanPribadi.setEnabled(false);
		cmbKelurahanPribadi.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		cmbKelurahanPribadi.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		cmbKelurahanPribadi.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		cmbKelurahanPribadi.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblKabupatenkota = new Label(grpAlamatTempatTinggal, SWT.NONE);
		lblKabupatenkota.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblKabupatenkota.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblKabupatenkota.setText("Kabupaten/Kota");
		
		txtKotaPribadi = new Text(grpAlamatTempatTinggal, SWT.BORDER);
		txtKotaPribadi.setEnabled(false);
		txtKotaPribadi.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtKotaPribadi.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		txtKotaPribadi.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtKotaPribadi.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label label_4 = new Label(grpAlamatTempatTinggal, SWT.NONE);
		label_4.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		label_4.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		label_4.setText("Kode Pos");
		
		txtKodePosPribadi = new Text(grpAlamatTempatTinggal, SWT.BORDER);
		txtKodePosPribadi.setEnabled(false);
		txtKodePosPribadi.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtKodePosPribadi.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		txtKodePosPribadi.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtKodePosPribadi.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label label_5 = new Label(grpAlamatTempatTinggal, SWT.NONE);
		label_5.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		label_5.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		label_5.setText("No. Telpon");
		
		txtTelponPribadi = new Text(grpAlamatTempatTinggal, SWT.BORDER);
		txtTelponPribadi.setEnabled(false);
		txtTelponPribadi.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtTelponPribadi.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		txtTelponPribadi.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtTelponPribadi.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Group grpAlamatBadanusaha = new Group(grpDataWP, SWT.NONE);
		grpAlamatBadanusaha.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BORDER));
		grpAlamatBadanusaha.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.BOLD));
		grpAlamatBadanusaha.setText("Alamat Badan/Usaha");
		grpAlamatBadanusaha.setLayout(new GridLayout(3, false));
		
		Label lblNpwpd = new Label(grpAlamatBadanusaha, SWT.NONE);
		lblNpwpd.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblNpwpd.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblNpwpd.setText("Jalan*");
		
		Label label_1 = new Label(grpAlamatBadanusaha, SWT.NONE);
		label_1.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		GridData gd_label_1 = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 6);
		gd_label_1.widthHint = 26;
		label_1.setLayoutData(gd_label_1);
		
		txtJalanBadan = new Text(grpAlamatBadanusaha, SWT.BORDER);
		txtJalanBadan.setEnabled(false);
		txtJalanBadan.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtJalanBadan.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		txtJalanBadan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtJalanBadan.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblNewLabel = new Label(grpAlamatBadanusaha, SWT.NONE);
		lblNewLabel.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblNewLabel.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblNewLabel.setText("Kecamatan*");
		
		cmbKecamatanBadan = new Combo(grpAlamatBadanusaha, SWT.NONE);
		cmbKecamatanBadan.setEnabled(false);
		cmbKecamatanBadan.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		cmbKecamatanBadan.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		cmbKecamatanBadan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		cmbKecamatanBadan.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		cmbKecamatanBadan.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Combo c = (Combo)e.widget;
				UIController.INSTANCE.loadKelurahan(cmbKelurahanBadan, KelurahanProvider.INSTANCE.getKelurahan((Integer)c.getData(Integer.toString(c.getSelectionIndex()))).toArray());
			}
		});
		UIController.INSTANCE.loadKecamatan(cmbKecamatanBadan, KecamatanProvider.INSTANCE.getKecamatan().toArray());
				
		Label lblJenisAsesment = new Label(grpAlamatBadanusaha, SWT.NONE);
		lblJenisAsesment.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblJenisAsesment.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblJenisAsesment.setText("Kelurahan*");
			
		cmbKelurahanBadan = new Combo(grpAlamatBadanusaha, SWT.NONE);
		cmbKelurahanBadan.setEnabled(false);
		cmbKelurahanBadan.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		cmbKelurahanBadan.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		cmbKelurahanBadan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		cmbKelurahanBadan.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
				
		Label lblKota = new Label(grpAlamatBadanusaha, SWT.NONE);
		lblKota.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblKota.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblKota.setText("Kota");
				
		txtKotaBadan = new Text(grpAlamatBadanusaha, SWT.BORDER);
		txtKotaBadan.setEnabled(false);
		txtKotaBadan.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtKotaBadan.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		txtKotaBadan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtKotaBadan.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
				
		Label lblKodePos = new Label(grpAlamatBadanusaha, SWT.NONE);
		lblKodePos.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblKodePos.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblKodePos.setText("Kode Pos");
				
		txtKodePosBadan = new Text(grpAlamatBadanusaha, SWT.BORDER);
		txtKodePosBadan.setEnabled(false);
		txtKodePosBadan.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtKodePosBadan.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		txtKodePosBadan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtKodePosBadan.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
				
		Label lblNoTelpon = new Label(grpAlamatBadanusaha, SWT.NONE);
		lblNoTelpon.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblNoTelpon.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblNoTelpon.setText("No. Telpon");
				
		txtTelponBadan = new Text(grpAlamatBadanusaha, SWT.BORDER);
		txtTelponBadan.setEnabled(false);
		txtTelponBadan.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtTelponBadan.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		txtTelponBadan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtTelponBadan.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Group grpPendaftaranOpsi = new Group(grpDataWP, SWT.NONE);
		grpPendaftaranOpsi.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		grpPendaftaranOpsi.setLayout(new GridLayout(6, false));
		grpPendaftaranOpsi.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.BOLD));
		grpPendaftaranOpsi.setText("Pendaftaran");
		
		Label lblKewarganegaraan = new Label(grpPendaftaranOpsi, SWT.NONE);
		lblKewarganegaraan.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		lblKewarganegaraan.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblKewarganegaraan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblKewarganegaraan.setText("Kewarganegaraan");
				
				Composite compositeWN = new Composite(grpPendaftaranOpsi, SWT.BORDER);
				compositeWN.setEnabled(false);
				compositeWN.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 4, 1));
				compositeWN.setLayout(new GridLayout(2, false));
				

				btnWni = new Button(compositeWN, SWT.RADIO);
				btnWni.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
				btnWni.setBounds(10, 10, 44, 16);
				btnWni.setText("WNI");
				
				btnWna = new Button(compositeWN, SWT.RADIO);
				btnWna.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
				btnWna.setBounds(61, 10, 49, 16);
				btnWna.setText("WNA");
				
				
				Label lblTandaBuktiDiri = new Label(grpPendaftaranOpsi, SWT.NONE);
				lblTandaBuktiDiri.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
				lblTandaBuktiDiri.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
				lblTandaBuktiDiri.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
				lblTandaBuktiDiri.setText("Tanda Bukti Diri");
				
				Composite compositeBukti = new Composite(grpPendaftaranOpsi, SWT.BORDER);
				compositeBukti.setEnabled(false);
				compositeBukti.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
				compositeBukti.setLayout(new GridLayout(3, false));
				
				btnKtp = new Button(compositeBukti, SWT.RADIO);
				btnKtp.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
				btnKtp.setText("KTP");
				
				btnSim = new Button(compositeBukti, SWT.RADIO);
				btnSim.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
				btnSim.setText("SIM");
				
				btnPassport = new Button(compositeBukti, SWT.RADIO);
				btnPassport.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
				btnPassport.setText("Paspor");
				
				Label lblNoBuktiDiri = new Label(grpPendaftaranOpsi, SWT.NONE);
				lblNoBuktiDiri.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
				lblNoBuktiDiri.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
				lblNoBuktiDiri.setText("No Bukti Diri");
				
				txtBuktiDiri = new Text(grpPendaftaranOpsi, SWT.BORDER);
				txtBuktiDiri.setEnabled(false);
				txtBuktiDiri.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
				txtBuktiDiri.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
				txtBuktiDiri.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
				GridData gd_txtBuktiDiri = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
				gd_txtBuktiDiri.widthHint = 115;
				txtBuktiDiri.setLayoutData(gd_txtBuktiDiri);
				
				Label lblNoKartuKeluarga = new Label(grpPendaftaranOpsi, SWT.NONE);
				lblNoKartuKeluarga.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
				lblNoKartuKeluarga.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
				lblNoKartuKeluarga.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
				lblNoKartuKeluarga.setText("No Kartu Keluarga");
				
				txtNoKK = new Text(grpPendaftaranOpsi, SWT.BORDER);
				txtNoKK.setEnabled(false);
				txtNoKK.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
				txtNoKK.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
				txtNoKK.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
				txtNoKK.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));
				new Label(grpPendaftaranOpsi, SWT.NONE);
				new Label(grpPendaftaranOpsi, SWT.NONE);
				
				Label lblSuratIzin = new Label(grpPendaftaranOpsi, SWT.NONE);
				lblSuratIzin.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
				lblSuratIzin.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
				lblSuratIzin.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
				lblSuratIzin.setText("Surat Izin");
				
				suratIzinTable = new SuratIzinTableEditorComposite(grpPendaftaranOpsi);
				suratIzinTable.getTable().setEnabled(false);
				suratIzinTable.setEnabled(false);
				GridData gd_suratIzinTable = new GridData(SWT.FILL, SWT.CENTER, false, false, 4, 1);
				gd_suratIzinTable.heightHint = 93;
				suratIzinTable.setLayoutData(gd_suratIzinTable);
				
				chkAktif = new Button(grpPendaftaranOpsi, SWT.CHECK);
				chkAktif.setEnabled(false);
				chkAktif.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						if(chkAktif.getSelection()){
							txtKeteranganNonAktif.setEnabled(false);
							chkTutup.setEnabled(false);
							grpTutup.setEnabled(false);
						}else{
							txtKeteranganNonAktif.setEnabled(true);
							chkTutup.setEnabled(true);
						}
					}
				});
				chkAktif.setSelection(true);
				chkAktif.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
				chkAktif.setText("Aktif");
				
				txtKeteranganNonAktif = new Text(grpPendaftaranOpsi, SWT.BORDER | SWT.WRAP | SWT.MULTI);
				txtKeteranganNonAktif.setEnabled(false);
				txtKeteranganNonAktif.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
				txtKeteranganNonAktif.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
				txtKeteranganNonAktif.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
				GridData gd_txtKeteranganNonAktif = new GridData(SWT.FILL, SWT.CENTER, true, false, 5, 1);
				gd_txtKeteranganNonAktif.heightHint = 65;
				txtKeteranganNonAktif.setLayoutData(gd_txtKeteranganNonAktif);
				
				chkTutup = new Button(grpPendaftaranOpsi, SWT.CHECK);
				chkTutup.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						if(chkTutup.getSelection()){
							grpTutup.setEnabled(true);
						}else{
							grpTutup.setEnabled(false);
						}
					}
				});
				chkTutup.setEnabled(false);
				chkTutup.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
				chkTutup.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
				chkTutup.setText("Tutup");
				
				grpTutup = new Group(grpPendaftaranOpsi, SWT.NONE);
				grpTutup.setEnabled(false);
				grpTutup.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
				grpTutup.setLayout(new GridLayout(2, false));
				grpTutup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 5, 1));
				grpTutup.setText("Detail Tutup");
				
				Label lblSuratKeteranganTutup = new Label(grpTutup, SWT.NONE);
				lblSuratKeteranganTutup.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BORDER));
				lblSuratKeteranganTutup.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
				lblSuratKeteranganTutup.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
				lblSuratKeteranganTutup.setText("Surat Keterangan Tutup");
				
				txtSuratKeteranganTutup = new Text(grpTutup, SWT.BORDER);
				txtSuratKeteranganTutup.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
				txtSuratKeteranganTutup.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
				txtSuratKeteranganTutup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
				
				Label lblTanggalMulaiTutup = new Label(grpTutup, SWT.NONE);
				lblTanggalMulaiTutup.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BORDER));
				lblTanggalMulaiTutup.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
				lblTanggalMulaiTutup.setBounds(0, 0, 70, 20);
				lblTanggalMulaiTutup.setText("Tanggal Mulai Tutup");
				
				DateTime dateTglMulaiTutup = new DateTime(grpTutup, SWT.BORDER | SWT.DROP_DOWN);
				dateTglMulaiTutup.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
				dateTglMulaiTutup.setBounds(0, 0, 102, 28);
				
				Label lblTanggalSampaiTutup = new Label(grpTutup, SWT.NONE);
				lblTanggalSampaiTutup.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BORDER));
				lblTanggalSampaiTutup.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
				lblTanggalSampaiTutup.setBounds(0, 0, 70, 20);
				lblTanggalSampaiTutup.setText("Tanggal Sampai Tutup");
				
				DateTime dateTglSampaiTutup = new DateTime(grpTutup, SWT.BORDER | SWT.DROP_DOWN);
				dateTglSampaiTutup.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
				dateTglSampaiTutup.setBounds(0, 0, 102, 28);
				
				Label lblKeteranganTutup = new Label(grpTutup, SWT.NONE);
				lblKeteranganTutup.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BORDER));
				lblKeteranganTutup.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
				lblKeteranganTutup.setBounds(0, 0, 70, 20);
				lblKeteranganTutup.setText("Keterangan Tutup");
				
				txtKeteranganTutup = new Text(grpTutup, SWT.BORDER | SWT.WRAP | SWT.MULTI);
				GridData gd_txtKeteranganTutup = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
				gd_txtKeteranganTutup.heightHint = 65;
				txtKeteranganTutup.setLayoutData(gd_txtKeteranganTutup);
				txtKeteranganTutup.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
				txtKeteranganTutup.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
				txtKeteranganTutup.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
				
				Group grpButton = new Group(grpDataWP, SWT.NONE);
				grpButton.setLayout(new GridLayout(2, false));
				
				Button btnTerima = new Button(grpButton, SWT.NONE);
				btnTerima.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
//						clearControls();
						table.getItem(table.getSelectionIndex()).setBackground(new Color(Display.getCurrent(), 185, 255, 147));
					}
				});
				btnTerima.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
				GridData gd_btnBaru = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
				gd_btnBaru.widthHint = 90;
				btnTerima.setLayoutData(gd_btnBaru);
				btnTerima.setText("Terima");
				
				Button btnTolak = new Button(grpButton, SWT.NONE);
				btnTolak.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
//						clearControls();
						table.getItem(table.getSelectionIndex()).setBackground(new Color(Display.getCurrent(), 233, 90, 90));
					}
				});
				btnTolak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
				GridData gd_btnTolak = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
				gd_btnTolak.widthHint = 90;
				btnTolak.setLayoutData(gd_btnTolak);
				btnTolak.setText("Tolak");
				new Label(grpDataWP, SWT.NONE);
		
				scrolledComposite.setContent(composite);
				scrolledComposite.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}

	@SuppressWarnings("deprecation")
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
//		chkInsidentil.setSelection(antri.getIsidentil());
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
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

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
}
