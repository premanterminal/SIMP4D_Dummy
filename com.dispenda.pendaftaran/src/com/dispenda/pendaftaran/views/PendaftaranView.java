package com.dispenda.pendaftaran.views;

import java.sql.Date;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseWheelListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.SWTResourceManager;
import org.hsqldb.jdbc.JDBCArrayBasic;

import com.dispenda.controller.ControllerFactory;
import com.dispenda.controller.UIController;
import com.dispenda.dao.LogImp;
import com.dispenda.model.BidangUsahaProvider;
import com.dispenda.model.KecamatanProvider;
import com.dispenda.model.KelurahanProvider;
import com.dispenda.model.PajakProvider;
import com.dispenda.model.PendaftaranAntrian;
import com.dispenda.model.PendaftaranWajibPajak;
import com.dispenda.model.UserModul;
import com.dispenda.model.WpTutup;
import com.dispenda.object.GlobalVariable;
import com.dispenda.object.Preferences;
import com.dispenda.pendaftaran.bridge.ICommunicationView;
import com.dispenda.widget.SuratIzinTableEditorComposite;
import com.dispenda.model.PendaftaranSuratIzinUsaha;

public class PendaftaranView extends ViewPart implements ICommunicationView {
	public PendaftaranView() {
	}
	
	public static final String ID = PendaftaranView.class.getName();
	private Text txtPeng;
	private Text txtNoPendaftaran;
	private Text txtKodePajak;
	private Text txtNoPajak;
	private Text txtKodeKecamatan;
	private Text txtKodeKelurahan;
	private Text txtNamaBadan;
	private Text txtPemilik;
	private Text txtJabatan;
	private Text txtKeterangan;
	private Text txtJalanBadan;
	private Text txtKotaBadan;
	private Text txtKodePosBadan;
	private Text txtTelponBadan;
	private Text txtJalanPribadi;
	private Text txtKotaPribadi;
	private Text txtKodePosPribadi;
	private Text txtTelponPribadi;
	private Text txtNoKK;
	private Text txtBuktiDiri;
	private Text txtKeteranganTutup;
	private Combo cmbKecamatanBadan;
	private Combo cmbKelurahanBadan;
	private Text txtKecamatanPribadi;
	private Text txtKelurahanPribadi;
	private Combo cmbJenisPajak;
	private Combo cmbJenisAssesment;
	private Combo cmbKewajibanPajak;
	private Combo cmbBidangUsaha;
	private Button chkInsidentil;
//	private Button chkInsidentil_pemerintahan;
	private Button btnWni;
	private Button btnWna;
	private DateTime calTanggalDaftar;
	private Text txtKeteranganNonAktif;
	private Text txtSuratKeteranganTutup;
	private Button chkAktif;
	private Group grpTutup;
	private Button chkTutup;
	private SuratIzinTableEditorComposite suratIzinTable;
	private Button btnKtp;
	private Button btnSim;
	private Button btnPassport;
	private Text txtNoFormulir;
	private Spinner spnIdAntri;
	private ScrolledComposite scrolledComposite;
	private Color fontColor = new Color(Display.getCurrent(),Preferences.FONT_COLOR);
	private DateTime dateTglMulaiTutup;
	private DateTime dateTglSampaiTutup;
	private boolean edit = false;
	private Composite composite;
	private Group grpPendaftaran;
	private Group grpAlamatBadanusaha;
	private java.sql.Timestamp dateNow;

	public void createPartControl(Composite parent) {
		scrolledComposite = new ScrolledComposite(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		scrolledComposite.addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseScrolled(MouseEvent me) {
				Point currentScroll = scrolledComposite.getOrigin();
				scrolledComposite.setOrigin(currentScroll.x, currentScroll.y - (me.count * 5));
			}
		});
		dateNow = (java.sql.Timestamp)ControllerFactory.getMainController().getCpWajibPajakDAOImpl().getDateNow();
		
		composite = new Composite(scrolledComposite, SWT.NONE);
		composite.setLayout(new GridLayout(3, false));
		
		grpPendaftaran = new Group(composite, SWT.NONE);
		grpPendaftaran.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, true, false, 1, 3));
		grpPendaftaran.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BORDER));
		grpPendaftaran.setLayout(new GridLayout(6, false));
		grpPendaftaran.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.BOLD));
		grpPendaftaran.addPaintListener(new PaintListener() {
			@Override
			public void paintControl(PaintEvent pe) {
				pe.gc.setForeground(fontColor);
				pe.gc.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.BOLD));
				pe.gc.drawText("Pendaftaran (Wajib diisi)", 5, -5, true);
			}
		});
		
		Label lblNoPendaftaran = new Label(grpPendaftaran, SWT.NONE);
		lblNoPendaftaran.setForeground(fontColor);
		lblNoPendaftaran.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblNoPendaftaran.setText("No Pendaftaran");
		
		Label label = new Label(grpPendaftaran, SWT.NONE);
		label.setForeground(fontColor);
		label.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		GridData gd_label = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 14);
		gd_label.widthHint = 35;
		label.setLayoutData(gd_label);
		
		txtPeng = new Text(grpPendaftaran, SWT.BORDER);
		txtPeng.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtPeng.setEnabled(false);
		txtPeng.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		txtPeng.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtPeng.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		txtPeng.setText("973.SI/Peng/");
		
		txtNoPendaftaran = new Text(grpPendaftaran, SWT.BORDER);
		txtNoPendaftaran.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtNoPendaftaran.setEnabled(false);
		txtNoPendaftaran.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		txtNoPendaftaran.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		GridData gd_txtNoPendaftaran = new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1);
		gd_txtNoPendaftaran.widthHint = 105;
		txtNoPendaftaran.setLayoutData(gd_txtNoPendaftaran);
		
		Label lblNpwpd_1 = new Label(grpPendaftaran, SWT.NONE);
		lblNpwpd_1.setForeground(fontColor);
		lblNpwpd_1.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblNpwpd_1.setText("NPWPD");
		
		txtKodePajak = new Text(grpPendaftaran, SWT.BORDER);
		txtKodePajak.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtKodePajak.setEnabled(false);
		txtKodePajak.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		txtKodePajak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		GridData gd_txtKodePajak = new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1);
		gd_txtKodePajak.widthHint = 10;
		txtKodePajak.setLayoutData(gd_txtKodePajak);
		
		txtNoPajak = new Text(grpPendaftaran, SWT.BORDER | SWT.RIGHT);
		txtNoPajak.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
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
		txtNoPajak.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		txtNoPajak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		GridData gd_txtNoPajak = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_txtNoPajak.widthHint = 45;
		txtNoPajak.setLayoutData(gd_txtNoPajak);
		
		txtKodeKecamatan = new Text(grpPendaftaran, SWT.BORDER);
		txtKodeKecamatan.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtKodeKecamatan.setEnabled(false);
		txtKodeKecamatan.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		txtKodeKecamatan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		GridData gd_txtKodeKecamatan = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_txtKodeKecamatan.widthHint = 10;
		txtKodeKecamatan.setLayoutData(gd_txtKodeKecamatan);
		
		txtKodeKelurahan = new Text(grpPendaftaran, SWT.BORDER);
		txtKodeKelurahan.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtKodeKelurahan.setEnabled(false);
		txtKodeKelurahan.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		txtKodeKelurahan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		GridData gd_txtKodeKelurahan = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_txtKodeKelurahan.widthHint = 10;
		txtKodeKelurahan.setLayoutData(gd_txtKodeKelurahan);
		
		Label lblNoFormulir = new Label(grpPendaftaran, SWT.NONE);
		lblNoFormulir.setForeground(fontColor);
		lblNoFormulir.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblNoFormulir.setText("No. Formulir");
		
		txtNoFormulir = new Text(grpPendaftaran, SWT.BORDER);
		txtNoFormulir.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtNoFormulir.setEnabled(false);
		txtNoFormulir.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtNoFormulir.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1));
		
		Label lblNoAntrian = new Label(grpPendaftaran, SWT.NONE);
		lblNoAntrian.setForeground(fontColor);
		lblNoAntrian.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblNoAntrian.setText("No. Antrian*");
		
		spnIdAntri = new Spinner(grpPendaftaran, SWT.BORDER);
		spnIdAntri.setEnabled(false);
		spnIdAntri.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		spnIdAntri.setTextLimit(99999);
		spnIdAntri.setMaximum(99999);
		spnIdAntri.setMinimum(1);
		spnIdAntri.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 4, 1));
		spnIdAntri.setSelection(ControllerFactory.getMainController().getCpDaftarAntrianDAOImpl().getLastIndex()+1);
		
		Label lblJenisAsesment_1 = new Label(grpPendaftaran, SWT.NONE);
		lblJenisAsesment_1.setForeground(fontColor);
		lblJenisAsesment_1.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblJenisAsesment_1.setText("Jenis Pajak*");
		
		cmbJenisPajak = new Combo(grpPendaftaran, SWT.READ_ONLY);
		cmbJenisPajak.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		cmbJenisPajak.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		cmbJenisPajak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		cmbJenisPajak.setItems(new String[] {"Badan", "Pribadi"});
		cmbJenisPajak.setData("Badan", "B");
		cmbJenisPajak.setData("Pribadi", "P");
		cmbJenisPajak.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 4, 1));
		
		Label lblJenisAsesment_2 = new Label(grpPendaftaran, SWT.NONE);
		lblJenisAsesment_2.setForeground(fontColor);
		lblJenisAsesment_2.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblJenisAsesment_2.setText("Jenis Assesment");
		
		cmbJenisAssesment = new Combo(grpPendaftaran, SWT.READ_ONLY);
		cmbJenisAssesment.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (cmbJenisAssesment.getSelectionIndex() == 0){
					if (cmbKewajibanPajak.getText().equalsIgnoreCase("AIR TANAH") || cmbKewajibanPajak.getText().equalsIgnoreCase("REKLAME")){
						MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Tidak dapat merubah Jenis Assesment Pajak " + cmbKewajibanPajak.getText() + " menjadi Self");
						cmbJenisAssesment.select(1);
					}
				}else{
					if (cmbKewajibanPajak.getSelectionIndex() > -1){
						if (!(cmbKewajibanPajak.getText().equalsIgnoreCase("AIR TANAH") || cmbKewajibanPajak.getText().equalsIgnoreCase("REKLAME"))){
							MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Tidak dapat merubah Jenis Assesment Pajak " + cmbKewajibanPajak.getText() + " menjadi Official");
							cmbJenisAssesment.select(0);
						}
					}
				}
			}
		});
		cmbJenisAssesment.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		cmbJenisAssesment.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		cmbJenisAssesment.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		cmbJenisAssesment.setItems(new String[] {"Self", "Official"});
		cmbJenisAssesment.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 4, 1));
		
		Label lblTanggalDaftar = new Label(grpPendaftaran, SWT.NONE);
		lblTanggalDaftar.setForeground(fontColor);
		lblTanggalDaftar.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblTanggalDaftar.setText("Tanggal Daftar*");
		
		calTanggalDaftar = new DateTime(grpPendaftaran, SWT.BORDER | SWT.DROP_DOWN);
		calTanggalDaftar.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		calTanggalDaftar.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		calTanggalDaftar.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		calTanggalDaftar.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 4, 1));
		
		Label lblNamaBadan = new Label(grpPendaftaran, SWT.NONE);
		lblNamaBadan.setForeground(fontColor);
		lblNamaBadan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblNamaBadan.setText("Nama Badan / Merk Usaha*");
		
		txtNamaBadan = new Text(grpPendaftaran, SWT.BORDER);
		txtNamaBadan.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtNamaBadan.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		txtNamaBadan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		GridData gd_txtNamaBadan = new GridData(SWT.FILL, SWT.CENTER, false, false, 4, 1);
		gd_txtNamaBadan.widthHint = 257;
		txtNamaBadan.setLayoutData(gd_txtNamaBadan);
		
		Label lblNamaPemilik = new Label(grpPendaftaran, SWT.NONE);
		lblNamaPemilik.setForeground(fontColor);
		lblNamaPemilik.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblNamaPemilik.setText("Nama Pemilik*");
		
		txtPemilik = new Text(grpPendaftaran, SWT.BORDER);
		txtPemilik.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtPemilik.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		txtPemilik.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		GridData gd_txtPemilik = new GridData(SWT.FILL, SWT.TOP, false, false, 4, 1);
		gd_txtPemilik.widthHint = 257;
		txtPemilik.setLayoutData(gd_txtPemilik);
		
		Label lblJabatan = new Label(grpPendaftaran, SWT.NONE);
		lblJabatan.setForeground(fontColor);
		lblJabatan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblJabatan.setText("Jabatan*");
		
		txtJabatan = new Text(grpPendaftaran, SWT.BORDER);
		txtJabatan.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtJabatan.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		txtJabatan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		GridData gd_txtJabatan = new GridData(SWT.FILL, SWT.CENTER, false, false, 4, 1);
		gd_txtJabatan.widthHint = 257;
		txtJabatan.setLayoutData(gd_txtJabatan);
		
		Label lblKewajibanPajak = new Label(grpPendaftaran, SWT.NONE);
		lblKewajibanPajak.setForeground(fontColor);
		lblKewajibanPajak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblKewajibanPajak.setText("Kewajiban Pajak*");
		
		cmbKewajibanPajak = new Combo(grpPendaftaran, SWT.READ_ONLY);
		cmbKewajibanPajak.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(cmbKewajibanPajak.getSelectionIndex() > -1)
					UIController.INSTANCE.loadBidangUsaha(cmbBidangUsaha, BidangUsahaProvider.INSTANCE.getBidangUsaha((Integer)cmbKewajibanPajak.getData(Integer.toString(cmbKewajibanPajak.getSelectionIndex()))).toArray());
				if(edit)
					txtKodePajak.setText((String)cmbKewajibanPajak.getData(cmbKewajibanPajak.getText()));
				
				if (cmbKewajibanPajak.getText().equalsIgnoreCase("AIR TANAH") || cmbKewajibanPajak.getText().equalsIgnoreCase("REKLAME"))
					cmbJenisAssesment.select(1);
				else
					cmbJenisAssesment.select(0);
			}
		});
		cmbKewajibanPajak.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent arg0) {
				if(cmbKewajibanPajak.getSelectionIndex() > -1)
					UIController.INSTANCE.loadBidangUsaha(cmbBidangUsaha, BidangUsahaProvider.INSTANCE.getBidangUsaha((Integer)cmbKewajibanPajak.getData(Integer.toString(cmbKewajibanPajak.getSelectionIndex()))).toArray());
			}
		});
		UIController.INSTANCE.loadPajak(cmbKewajibanPajak, PajakProvider.INSTANCE.getPajak().toArray());
		cmbKewajibanPajak.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		cmbKewajibanPajak.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		cmbKewajibanPajak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		cmbKewajibanPajak.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 4, 1));
		
		Label lblBidangUsaha = new Label(grpPendaftaran, SWT.NONE);
		lblBidangUsaha.setForeground(fontColor);
		lblBidangUsaha.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblBidangUsaha.setText("Bidang Usaha*");
		
		cmbBidangUsaha = new Combo(grpPendaftaran, SWT.READ_ONLY);
		cmbBidangUsaha.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		cmbBidangUsaha.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		cmbBidangUsaha.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		cmbBidangUsaha.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 4, 1));
		
		Label lblKeterangan = new Label(grpPendaftaran, SWT.NONE);
		lblKeterangan.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		lblKeterangan.setForeground(fontColor);
		lblKeterangan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblKeterangan.setText("Keterangan*");
		
		txtKeterangan = new Text(grpPendaftaran, SWT.BORDER | SWT.WRAP | SWT.MULTI);
		txtKeterangan.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtKeterangan.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		txtKeterangan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		GridData gd_txtKeterangan = new GridData(SWT.FILL, SWT.CENTER, false, false, 4, 1);
		gd_txtKeterangan.heightHint = 120;
		txtKeterangan.setLayoutData(gd_txtKeterangan);
		
		Label lblInsidentil = new Label(grpPendaftaran, SWT.NONE);
		lblInsidentil.setForeground(fontColor);
		lblInsidentil.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblInsidentil.setText("Insidentil*");
		
		chkInsidentil = new Button(grpPendaftaran, SWT.CHECK);
		new Label(grpPendaftaran, SWT.NONE);
		new Label(grpPendaftaran, SWT.NONE);
		new Label(grpPendaftaran, SWT.NONE);
		
		lblInsidentilPemerintahan = new Label(grpPendaftaran, SWT.NONE);
		lblInsidentilPemerintahan.setText("Insidentil Pemerintahan*");
		lblInsidentilPemerintahan.setForeground(fontColor);
		lblInsidentilPemerintahan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		new Label(grpPendaftaran, SWT.NONE);
		
//		chkInsidentil_pemerintahan = new Button(grpPendaftaran, SWT.CHECK);
		new Label(grpPendaftaran, SWT.NONE);
		new Label(grpPendaftaran, SWT.NONE);
		new Label(grpPendaftaran, SWT.NONE);
		grpPendaftaran.setTabList(new Control[]{cmbJenisPajak, cmbJenisAssesment, calTanggalDaftar, txtNamaBadan, txtPemilik, txtJabatan, cmbKewajibanPajak, cmbBidangUsaha, txtKeterangan, chkInsidentil, 
//				chkInsidentil_pemerintahan
				});
		
		grpAlamatBadanusaha = new Group(composite, SWT.NONE);
		grpAlamatBadanusaha.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1));
		grpAlamatBadanusaha.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BORDER));
		grpAlamatBadanusaha.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.BOLD));
		grpAlamatBadanusaha.addPaintListener(new PaintListener() {
			@Override
			public void paintControl(PaintEvent pe) {
				pe.gc.setForeground(fontColor);
				pe.gc.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.BOLD));
				pe.gc.drawText("Alamat Badan/Usaha", 5, -5, true);
			}
		});
		grpAlamatBadanusaha.setLayout(new GridLayout(3, false));
		
		Label lblNpwpd = new Label(grpAlamatBadanusaha, SWT.NONE);
		lblNpwpd.setForeground(fontColor);
		lblNpwpd.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblNpwpd.setText("Jalan*");
		
		Label label_1 = new Label(grpAlamatBadanusaha, SWT.NONE);
		label_1.setForeground(fontColor);
		GridData gd_label_1 = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 6);
		gd_label_1.widthHint = 26;
		label_1.setLayoutData(gd_label_1);
		
		txtJalanBadan = new Text(grpAlamatBadanusaha, SWT.BORDER);
		txtJalanBadan.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtJalanBadan.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		txtJalanBadan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		GridData gd_txtJalanBadan = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_txtJalanBadan.widthHint = 200;
		txtJalanBadan.setLayoutData(gd_txtJalanBadan);
		
		Label lblNewLabel = new Label(grpAlamatBadanusaha, SWT.NONE);
		lblNewLabel.setForeground(fontColor);
		lblNewLabel.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblNewLabel.setText("Kecamatan*");
		
		cmbKecamatanBadan = new Combo(grpAlamatBadanusaha, SWT.READ_ONLY);
		cmbKecamatanBadan.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		cmbKecamatanBadan.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		cmbKecamatanBadan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		cmbKecamatanBadan.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		cmbKecamatanBadan.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Combo c = (Combo)e.widget;
				if(c.getSelectionIndex() > -1)
					UIController.INSTANCE.loadKelurahan(cmbKelurahanBadan, KelurahanProvider.INSTANCE.getKelurahan((Integer)c.getData(Integer.toString(c.getSelectionIndex()))).toArray());
				if(edit)
					txtKodeKecamatan.setText((String)cmbKecamatanBadan.getData(cmbKecamatanBadan.getText()));
				
			}
		});
		cmbKecamatanBadan.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent arg0) {
				if(cmbKecamatanBadan.getSelectionIndex() > -1)
					UIController.INSTANCE.loadKelurahan(cmbKelurahanBadan, KelurahanProvider.INSTANCE.getKelurahan((Integer)cmbKecamatanBadan.getData(Integer.toString(cmbKecamatanBadan.getSelectionIndex()))).toArray());
			}
		});
		UIController.INSTANCE.loadKecamatan(cmbKecamatanBadan, KecamatanProvider.INSTANCE.getKecamatan().toArray());
				
		Label lblJenisAsesment = new Label(grpAlamatBadanusaha, SWT.NONE);
		lblJenisAsesment.setForeground(fontColor);
		lblJenisAsesment.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblJenisAsesment.setText("Kelurahan*");
			
		cmbKelurahanBadan = new Combo(grpAlamatBadanusaha, SWT.READ_ONLY);
		cmbKelurahanBadan.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		cmbKelurahanBadan.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		cmbKelurahanBadan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		cmbKelurahanBadan.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		cmbKelurahanBadan.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(edit)
					txtKodeKelurahan.setText((String)cmbKelurahanBadan.getData(cmbKelurahanBadan.getText()));
			}
		});
				
		Label lblKota = new Label(grpAlamatBadanusaha, SWT.NONE);
		lblKota.setForeground(fontColor);
		lblKota.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblKota.setText("Kota");
				
		txtKotaBadan = new Text(grpAlamatBadanusaha, SWT.BORDER);
		txtKotaBadan.setText("Medan");
		txtKotaBadan.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtKotaBadan.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		txtKotaBadan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtKotaBadan.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
				
		Label lblKodePos = new Label(grpAlamatBadanusaha, SWT.NONE);
		lblKodePos.setForeground(fontColor);
		lblKodePos.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblKodePos.setText("Kode Pos");
				
		txtKodePosBadan = new Text(grpAlamatBadanusaha, SWT.BORDER);
		txtKodePosBadan.setTextLimit(5);
		txtKodePosBadan.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtKodePosBadan.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		txtKodePosBadan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtKodePosBadan.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
				
		Label lblNoTelpon = new Label(grpAlamatBadanusaha, SWT.NONE);
		lblNoTelpon.setForeground(fontColor);
		lblNoTelpon.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblNoTelpon.setText("No. Telpon");
				
		txtTelponBadan = new Text(grpAlamatBadanusaha, SWT.BORDER);
		txtTelponBadan.setTextLimit(13);
		txtTelponBadan.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtTelponBadan.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		txtTelponBadan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtTelponBadan.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Group grpPendaftaranOpsi = new Group(composite, SWT.NONE);
		grpPendaftaranOpsi.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, true, false, 1, 2));
		grpPendaftaranOpsi.setLayout(new GridLayout(6, false));
		grpPendaftaranOpsi.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.BOLD));
		grpPendaftaranOpsi.addPaintListener(new PaintListener() {
			@Override
			public void paintControl(PaintEvent pe) {
				pe.gc.setForeground(fontColor);
				pe.gc.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.BOLD));
				pe.gc.drawText("Pendaftaran", 5, -5, true);
			}
		});
		
		Label lblKewarganegaraan = new Label(grpPendaftaranOpsi, SWT.NONE);
		lblKewarganegaraan.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		lblKewarganegaraan.setForeground(fontColor);
		lblKewarganegaraan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblKewarganegaraan.setText("Kewarganegaraan");
				
		Composite compositeWN = new Composite(grpPendaftaranOpsi, SWT.BORDER);
		compositeWN.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 4, 1));
		compositeWN.setLayout(new GridLayout(2, false));
				
		btnWni = new Button(compositeWN, SWT.RADIO);
		btnWni.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
//		btnWni.setBounds(10, 10, 44, 16);
		btnWni.setText("WNI");
				
		btnWna = new Button(compositeWN, SWT.RADIO);
		btnWna.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
//		btnWna.setBounds(61, 10, 49, 16);
		btnWna.setText("WNA");
			
		Label lblTandaBuktiDiri = new Label(grpPendaftaranOpsi, SWT.NONE);
		lblTandaBuktiDiri.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		lblTandaBuktiDiri.setForeground(fontColor);
		lblTandaBuktiDiri.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblTandaBuktiDiri.setText("Tanda Bukti Diri");
				
		Composite compositeBukti = new Composite(grpPendaftaranOpsi, SWT.BORDER);
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
				lblNoBuktiDiri.setForeground(fontColor);
				lblNoBuktiDiri.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
				lblNoBuktiDiri.setText("No Bukti Diri");
				
				txtBuktiDiri = new Text(grpPendaftaranOpsi, SWT.BORDER);
				txtBuktiDiri.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
				txtBuktiDiri.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
				txtBuktiDiri.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
				GridData gd_txtBuktiDiri = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
				gd_txtBuktiDiri.widthHint = 115;
				txtBuktiDiri.setLayoutData(gd_txtBuktiDiri);
				
				Label lblNoKartuKeluarga = new Label(grpPendaftaranOpsi, SWT.NONE);
				lblNoKartuKeluarga.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
				lblNoKartuKeluarga.setForeground(fontColor);
				lblNoKartuKeluarga.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
				lblNoKartuKeluarga.setText("No Kartu Keluarga");
				
				txtNoKK = new Text(grpPendaftaranOpsi, SWT.BORDER);
				txtNoKK.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
				txtNoKK.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
				txtNoKK.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
				txtNoKK.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));
				new Label(grpPendaftaranOpsi, SWT.NONE);
				new Label(grpPendaftaranOpsi, SWT.NONE);
				
				Label lblSuratIzin = new Label(grpPendaftaranOpsi, SWT.NONE);
				lblSuratIzin.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
				lblSuratIzin.setForeground(fontColor);
				lblSuratIzin.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
				lblSuratIzin.setText("Surat Izin");
				
				suratIzinTable = new SuratIzinTableEditorComposite(grpPendaftaranOpsi);
				suratIzinTable.getTable().setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
				GridData gd_suratIzinTable = new GridData(SWT.FILL, SWT.CENTER, false, false, 4, 1);
				gd_suratIzinTable.heightHint = 93;
				suratIzinTable.setLayoutData(gd_suratIzinTable);
				
				chkAktif = new Button(grpPendaftaranOpsi, SWT.CHECK);
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
				grpTutup.addPaintListener(new PaintListener() {
					@Override
					public void paintControl(PaintEvent pe) {
						pe.gc.setForeground(fontColor);
						pe.gc.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
						pe.gc.drawText("Detail Tutup", 5, -5, true);
					}
				});
				
				Label lblSuratKeteranganTutup = new Label(grpTutup, SWT.NONE);
				lblSuratKeteranganTutup.setForeground(fontColor);
				lblSuratKeteranganTutup.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
				lblSuratKeteranganTutup.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
				lblSuratKeteranganTutup.setText("Surat Keterangan Tutup");
				
				txtSuratKeteranganTutup = new Text(grpTutup, SWT.BORDER);
				txtSuratKeteranganTutup.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
				txtSuratKeteranganTutup.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
				txtSuratKeteranganTutup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
				
				Label lblTanggalMulaiTutup = new Label(grpTutup, SWT.NONE);
				lblTanggalMulaiTutup.setForeground(fontColor);
				lblTanggalMulaiTutup.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
				lblTanggalMulaiTutup.setBounds(0, 0, 70, 20);
				lblTanggalMulaiTutup.setText("Tanggal Mulai Tutup");
				
				dateTglMulaiTutup = new DateTime(grpTutup, SWT.BORDER | SWT.DROP_DOWN);
				dateTglMulaiTutup.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
				dateTglMulaiTutup.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
				dateTglMulaiTutup.setBounds(0, 0, 102, 28);
				
				Label lblTanggalSampaiTutup = new Label(grpTutup, SWT.NONE);
				lblTanggalSampaiTutup.setForeground(fontColor);
				lblTanggalSampaiTutup.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
				lblTanggalSampaiTutup.setBounds(0, 0, 70, 20);
				lblTanggalSampaiTutup.setText("Tanggal Sampai Tutup");
				
				dateTglSampaiTutup = new DateTime(grpTutup, SWT.BORDER | SWT.DROP_DOWN);
				dateTglSampaiTutup.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
				dateTglSampaiTutup.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
				dateTglSampaiTutup.setBounds(0, 0, 102, 28);
				
				Label lblKeteranganTutup = new Label(grpTutup, SWT.NONE);
				lblKeteranganTutup.setForeground(fontColor);
				lblKeteranganTutup.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
				lblKeteranganTutup.setBounds(0, 0, 70, 20);
				lblKeteranganTutup.setText("Keterangan Tutup");
				
				txtKeteranganTutup = new Text(grpTutup, SWT.BORDER | SWT.WRAP | SWT.MULTI);
				GridData gd_txtKeteranganTutup = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
				gd_txtKeteranganTutup.heightHint = 65;
				txtKeteranganTutup.setLayoutData(gd_txtKeteranganTutup);
				txtKeteranganTutup.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
				txtKeteranganTutup.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
				txtKeteranganTutup.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
				
				Group grpAlamatTempatTinggal = new Group(composite, SWT.NONE);
				grpAlamatTempatTinggal.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, true, false, 1, 1));
				grpAlamatTempatTinggal.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.BOLD));
				grpAlamatTempatTinggal.addPaintListener(new PaintListener() {
					@Override
					public void paintControl(PaintEvent pe) {
						Group grp = (Group) pe.widget;
						pe.gc.setForeground(fontColor);
						pe.gc.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.BOLD));
						pe.gc.drawText("Alamat Tempat Tinggal", 5, grp.getLocation().y-5, true);
					}
				});
				grpAlamatTempatTinggal.setLayout(new GridLayout(2, false));
				
				Label lblJalan = new Label(grpAlamatTempatTinggal, SWT.NONE);
				lblJalan.setForeground(fontColor);
				lblJalan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
				lblJalan.setText("Jalan");
				
				txtJalanPribadi = new Text(grpAlamatTempatTinggal, SWT.BORDER);
				GridData gd_txtJalanPribadi = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
				gd_txtJalanPribadi.widthHint = 200;
				txtJalanPribadi.setLayoutData(gd_txtJalanPribadi);
				txtJalanPribadi.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
				txtJalanPribadi.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
				txtJalanPribadi.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
				
				Label lblKecamatan = new Label(grpAlamatTempatTinggal, SWT.NONE);
				lblKecamatan.setForeground(fontColor);
				lblKecamatan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
				lblKecamatan.setText("Kecamatan");
				
				txtKecamatanPribadi = new Text(grpAlamatTempatTinggal, SWT.BORDER);
				txtKecamatanPribadi.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
				txtKecamatanPribadi.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
				txtKecamatanPribadi.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
				txtKecamatanPribadi.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
				
				Label lblKelurahan = new Label(grpAlamatTempatTinggal, SWT.NONE);
				lblKelurahan.setForeground(fontColor);
				lblKelurahan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
				lblKelurahan.setText("Kelurahan");
				
				txtKelurahanPribadi = new Text(grpAlamatTempatTinggal, SWT.BORDER);
				txtKelurahanPribadi.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
				txtKelurahanPribadi.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
				txtKelurahanPribadi.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
				txtKelurahanPribadi.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
				
				Label lblKabupatenkota = new Label(grpAlamatTempatTinggal, SWT.NONE);
				lblKabupatenkota.setForeground(fontColor);
				lblKabupatenkota.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
				lblKabupatenkota.setText("Kabupaten/Kota");
				
				txtKotaPribadi = new Text(grpAlamatTempatTinggal, SWT.BORDER);
				txtKotaPribadi.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
				txtKotaPribadi.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
				txtKotaPribadi.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
				txtKotaPribadi.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
				
				Label label_4 = new Label(grpAlamatTempatTinggal, SWT.NONE);
				label_4.setForeground(fontColor);
				label_4.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
				label_4.setText("Kode Pos");
				
				txtKodePosPribadi = new Text(grpAlamatTempatTinggal, SWT.BORDER);
				txtKodePosPribadi.setTextLimit(5);
				txtKodePosPribadi.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
				txtKodePosPribadi.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
				txtKodePosPribadi.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
				txtKodePosPribadi.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
				
				Label label_5 = new Label(grpAlamatTempatTinggal, SWT.NONE);
				label_5.setForeground(fontColor);
				label_5.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
				label_5.setText("No. Telpon");
				
				txtTelponPribadi = new Text(grpAlamatTempatTinggal, SWT.BORDER);
				txtTelponPribadi.setTextLimit(13);
				txtTelponPribadi.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
				txtTelponPribadi.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
				txtTelponPribadi.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
				txtTelponPribadi.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
				grpAlamatTempatTinggal.setTabList(new Control[]{txtJalanPribadi, txtKecamatanPribadi, txtKelurahanPribadi, txtKotaPribadi, txtKodePosPribadi, txtTelponPribadi});
		new Label(composite, SWT.NONE);
		
		Composite grpButton = new Composite(composite, SWT.NONE);
		grpButton.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		grpButton.setLayout(new GridLayout(3, false));
		
		Button btnBaru = new Button(grpButton, SWT.NONE);
		btnBaru.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				clearControls();
				edit = false;
			}
		});
		btnBaru.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		GridData gd_btnBaru = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_btnBaru.widthHint = 90;
		btnBaru.setLayoutData(gd_btnBaru);
		btnBaru.setText("Baru");
		
		Button btnSimpan = new Button(grpButton, SWT.NONE);
		btnSimpan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnSimpan.addSelectionListener(new SelectionAdapter() {
			@SuppressWarnings("deprecation")
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(isSave())
					if(isValidate()){
						if(edit){ // khusus untuk edit
	        				PendaftaranWajibPajak wajibPajakModel = new PendaftaranWajibPajak();
	        				wajibPajakModel.setAktif(chkAktif.getSelection());
	        				wajibPajakModel.setAlabadJalan(txtJalanBadan.getText());
	        				wajibPajakModel.setAlabadKecamatan((String)cmbKecamatanBadan.getData(cmbKecamatanBadan.getItem(cmbKecamatanBadan.getSelectionIndex())));
	        				wajibPajakModel.setAlabadKelurahan((String)cmbKelurahanBadan.getData(cmbKelurahanBadan.getItem(cmbKelurahanBadan.getSelectionIndex())));
	        				wajibPajakModel.setAlabadKodePos(txtKodePosBadan.getText());
	        				wajibPajakModel.setAlabadTelepon(txtTelponBadan.getText());
	        				wajibPajakModel.setAlatingJalan(txtJalanPribadi.getText());
	        				wajibPajakModel.setAlatingKecamatan(txtKecamatanPribadi.getText());
	        				wajibPajakModel.setAlatingKelurahan(txtKelurahanPribadi.getText());
	        				wajibPajakModel.setAlatingKodepos(txtKodePosPribadi.getText());
	        				wajibPajakModel.setAlatingKota(txtKotaPribadi.getText());
	        				wajibPajakModel.setAlatingTelepon(txtTelponPribadi.getText());
	        				
	        				wajibPajakModel.setBidangUsaha(""); // setelah migrasi ke server dispenda hapus KODE_BID_USAHA pada table database
	        			
	        				wajibPajakModel.setEkswp(false);
	        				wajibPajakModel.setIdSubPajak((Integer)cmbBidangUsaha.getData(Integer.toString(cmbBidangUsaha.getSelectionIndex())));
	        				wajibPajakModel.setInsidentil(chkInsidentil.getSelection());
//	        				wajibPajakModel.setInsidentil_Pemerintah(chkInsidentil_pemerintahan.getSelection());
	        				wajibPajakModel.setJabatan(txtJabatan.getText());
	        				wajibPajakModel.setJenisAssesment(cmbJenisAssesment.getText());
	        				wajibPajakModel.setJenisPajak((String)cmbJenisPajak.getData(cmbJenisPajak.getText()));
	        				wajibPajakModel.setKeterangan(txtKeterangan.getText());
	        				wajibPajakModel.setKeteranganTutup(txtKeteranganNonAktif.getText());
	        				wajibPajakModel.setKewajibanPajak((Integer)cmbKewajibanPajak.getData(Integer.toString(cmbKewajibanPajak.getSelectionIndex())));
	        				if(btnWni.getSelection())
	        					wajibPajakModel.setKewarganegaraan("WNI");
	        				else
	        					wajibPajakModel.setKewarganegaraan("WNA");
	        				wajibPajakModel.setNamaBadan(txtNamaBadan.getText().replace("'", "''"));
	        				wajibPajakModel.setNamaPemilik(txtPemilik.getText());
	        				wajibPajakModel.setNoBuktiDiri(txtBuktiDiri.getText());
	        				wajibPajakModel.setNoFormulir(txtNoFormulir.getText());
	        				wajibPajakModel.setNoKartuKeluarga(txtNoKK.getText());
	        				if(chkAktif.getSelection())
	        					wajibPajakModel.setStatus("Aktif");
	        				else if(chkAktif.getSelection() == false && chkTutup.getSelection() == false)
	        					wajibPajakModel.setStatus("NonAktif");
	        				else if(chkTutup.getSelection())
	        					wajibPajakModel.setStatus("Tutup");
	        				
	        				if(btnKtp.getSelection())
	        					wajibPajakModel.setTandaBuktiDiri("KTP");
	        				else if(btnSim.getSelection())
	        					wajibPajakModel.setTandaBuktiDiri("SIM");
	        				else
	        					wajibPajakModel.setTandaBuktiDiri("Paspor");
	        				wajibPajakModel.setTanggalApprove("2006-10-11");
	        				Date date = new Date(calTanggalDaftar.getYear()-1900, calTanggalDaftar.getMonth(), calTanggalDaftar.getDay());
	        				wajibPajakModel.setTanggalDaftar(date);
	        				
	        				wajibPajakModel.setNoPendaftaran(txtNoPendaftaran.getText());
	        				wajibPajakModel.setNPWP(txtKodePajak.getText()+txtNoPajak.getText()+txtKodeKecamatan.getText()+txtKodeKelurahan.getText());
	        				String dateNow = ControllerFactory.getMainController().getCpWajibPajakDAOImpl().getDateNow().toString().split(" ")[0];
	        				if (ControllerFactory.getMainController().getCpWajibPajakDAOImpl().savePendaftaranWajibPajak(wajibPajakModel,npwpLama)){
	        					saveSuratIzin();
	        					StringBuffer sb = new StringBuffer();
								sb.append("SAVE EDIT Wajib Pajak NPWP Baru: "+wajibPajakModel.getNPWP()+" NPWP Lama: "+npwpLama
		        						+" Tanggal Edit:"+dateNow+" Tanggal Daftar:"+wajibPajakModel.getTanggalDaftar());
								new LogImp().setLog(sb.toString(), userModul.getDisplayName(), userModul.getSubModul().getNamaModul());
								
								// Untuk keterangan tutup
								if(chkTutup.getSelection()){
									WpTutup wpTutupModel = new WpTutup();
									wpTutupModel.setNoSuratTutup(txtSuratKeteranganTutup.getText());
									wpTutupModel.setTglMulaiTutup(new Date(dateTglMulaiTutup.getYear() - 1900, dateTglMulaiTutup.getMonth(), dateTglMulaiTutup.getDay()));
									wpTutupModel.setTglSampaiTutup(new Date(dateTglSampaiTutup.getYear() - 1900, dateTglSampaiTutup.getMonth(), dateTglSampaiTutup.getDay()));
									wpTutupModel.setKeterangan(txtKeteranganTutup.getText());
									wpTutupModel.setNpwpd(txtKodePajak.getText()+txtNoPajak.getText()+txtKodeKecamatan.getText()+txtKodeKelurahan.getText());
									if(ControllerFactory.getMainController().getCpWajibPajakDAOImpl().saveWajibPajakTutup(wpTutupModel)){
//										sb = new StringBuffer();
//										sb.append("SAVE EDIT Wajib Pajak NPWP Baru: "+wajibPajakModel.getNPWP()+" NPWP Lama: "+npwpLama
//				        						+" Tanggal Edit:"+dateNow+" Tanggal Daftar:"+wajibPajakModel.getTanggalDaftar() + "Mulai Tanggal Tutup");
//										new LogImp().setLog(sb.toString(), userModul.getDisplayName(), userModul.getSubModul().getNamaModul());
										MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Data berhasil diubah.");
									}else{
										MessageDialog.openError(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Error pada Wajib Pajak Tutup. Silahkan hubungi ADMIN.");
									}
								}else
									MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Data berhasil diubah.");
	        				}else{
	        					MessageDialog.openError(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Error pada Database. Silahkan hubungi ADMIN.");
	        				}
						}else{ // khusus untuk save
							PendaftaranAntrian antri = new PendaftaranAntrian();
							if(suratIzinTable.getTable().getItems().length>0){
								Object[] suratIzin = new Object[suratIzinTable.getTable().getItems().length];
								Object[] noSurat = new Object[suratIzinTable.getTable().getItems().length];
								Object[] tglSurat = new Object[suratIzinTable.getTable().getItems().length];
								int i =0;
								for(TableItem item : suratIzinTable.getTable().getItems()){
									suratIzin[i] = item.getText(0);
									noSurat[i] = item.getText(1);
									tglSurat[i] = item.getText(2); // tanggal belum diset
									i++;
								}
								org.hsqldb.types.Type type = org.hsqldb.types.Type.SQL_VARCHAR_DEFAULT;
								JDBCArrayBasic arrSuratIzin = new JDBCArrayBasic(suratIzin, type);
								JDBCArrayBasic arrNoSurat = new JDBCArrayBasic(noSurat, type);
								JDBCArrayBasic arrTglSurat = new JDBCArrayBasic(tglSurat, type);
								antri.setSuratIzin(arrSuratIzin);
								antri.setNoSurat(arrNoSurat);
								antri.setTglSurat(arrTglSurat);
							}
							
		//					antri.setNoFormulir(txtNoFormulir.getText());
							antri.setIndex(spnIdAntri.getSelection());
							antri.setNamaBadan(txtNamaBadan.getText());
							antri.setNamaPemilik(txtPemilik.getText());
							antri.setJenisAssesment(cmbJenisAssesment.getItem(cmbJenisAssesment.getSelectionIndex()));
							antri.setJenisPajak((String)cmbJenisPajak.getData(cmbJenisPajak.getItem(cmbJenisPajak.getSelectionIndex())));
							
							antri.setIdSubPajak((Integer)cmbBidangUsaha.getData(Integer.toString(cmbBidangUsaha.getSelectionIndex())));
							antri.setKewajibanPajak((Integer)cmbKewajibanPajak.getData(Integer.toString(cmbKewajibanPajak.getSelectionIndex())));
							
							antri.setAlatingJalan(txtJalanPribadi.getText());
							antri.setAlatingKecamatan(txtKecamatanPribadi.getText());
							antri.setAlatingKelurahan(txtKelurahanPribadi.getText());
							antri.setAlatingKota(txtKotaPribadi.getText());
							antri.setAlatingKodepos(txtKodePosPribadi.getText());
							antri.setAlatingTelepon(txtTelponPribadi.getText());
							
							antri.setJabatan(txtJabatan.getText());
							antri.setKeterangan(txtKeterangan.getText());
							
							antri.setAlabadJalan(txtJalanBadan.getText());
							antri.setAlabadKodePos(txtKodePosBadan.getText());
							antri.setAlabadTelepon(txtTelponBadan.getText());
							antri.setAlabadKecamatan((String)cmbKecamatanBadan.getData(cmbKecamatanBadan.getItem((cmbKecamatanBadan.getSelectionIndex()))));
							antri.setAlabadKelurahan((String)cmbKelurahanBadan.getData(cmbKelurahanBadan.getItem((cmbKelurahanBadan.getSelectionIndex()))));
							
							antri.setNoKartuKeluraga(txtNoKK.getText());
							antri.setNoBuktiDiri(txtBuktiDiri.getText());
		
							antri.setJenisPajak((String)cmbJenisPajak.getData(cmbJenisPajak.getItem(cmbJenisPajak.getSelectionIndex())));
							antri.setJenisAssesment(cmbJenisAssesment.getItem(cmbJenisAssesment.getSelectionIndex()));
							antri.setKewajibanPajak((Integer)cmbKewajibanPajak.getData(Integer.toString(cmbKewajibanPajak.getSelectionIndex())));
							antri.setIdSubPajak((Integer)cmbBidangUsaha.getData(Integer.toString(cmbBidangUsaha.getSelectionIndex())));
							antri.setInsidentil(chkInsidentil.getSelection());
//							antri.setInsidentil_Pemerintah(chkInsidentil_pemerintahan.getSelection());
							antri.setKetReject("");
							
							if(btnWni.getSelection())
								antri.setTandaBuktiDiri(btnWni.getText());
							else
								antri.setTandaBuktiDiri(btnWna.getText());
							
							if(btnKtp.getSelection())
								antri.setKewarganegaraan(btnKtp.getText());
							else if(btnPassport.getSelection())
								antri.setKewarganegaraan(btnPassport.getText());
							else
								antri.setKewarganegaraan(btnSim.getText());
							
							Date date = new Date(calTanggalDaftar.getYear()-1900, calTanggalDaftar.getMonth(), calTanggalDaftar.getDay());
							antri.setTanggalDaftar(date);
							
							antri.setTolak(false);
							antri.setTanggalTolak(Date.valueOf("0001-01-01"));
							
							if(ControllerFactory.getMainController().getCpDaftarAntrianDAOImpl().savePendaftaranAntrian(antri)){
								StringBuffer sb = new StringBuffer();
								sb.append("SAVE Antri "+spnIdAntri.getSelection()+" "+txtNamaBadan.getText()+" "+date);
								new LogImp().setLog(sb.toString(), userModul.getDisplayName(), userModul.getSubModul().getNamaModul());
								MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Data berhasil disimpan.");
							}else{
								MessageDialog.openError(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Error pada Database. Silahkan hubungi ADMIN.");
							}
						}
					}else
						MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Data belum lengkap. Silahkan periksa kembali.");
				else
					MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Anda tidak memiliki hak untuk menyimpan atau merubah data.");
			}
		});
		GridData gd_btnSimpan = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_btnSimpan.widthHint = 90;
		btnSimpan.setLayoutData(gd_btnSimpan);
		btnSimpan.setText("Simpan");
		new Label(grpButton, SWT.NONE);
		composite.setTabList(new Control[]{grpPendaftaran, grpAlamatBadanusaha, grpAlamatTempatTinggal, grpPendaftaranOpsi, grpButton});

		scrolledComposite.setContent(composite);
		scrolledComposite.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
//		installMouseWheelScrollRecursively(scrolledComposite);
	}
	
/*	public static void installMouseWheelScrollRecursively(final ScrolledComposite scrollable) {
		MouseWheelListener scroller = createMouseWheelScroller(scrollable);
		if (scrollable.getParent() != null)
			scrollable.getParent().addMouseWheelListener(scroller);
		installMouseWheelScrollRecursively(scroller, scrollable);
	}
 
	public static MouseWheelListener createMouseWheelScroller(final ScrolledComposite scrollable) {
		return new MouseWheelListener() {
			
			@Override
			public void mouseScrolled(MouseEvent e) {
				Point currentScroll = scrollable.getOrigin();
				scrollable.setOrigin(currentScroll.x, currentScroll.y - (e.count * 5));
			}
		};
	}
 
	private static void installMouseWheelScrollRecursively(MouseWheelListener scroller, Control c) {
		c.addMouseWheelListener(scroller);
		if (c instanceof Composite) {
			Composite comp = (Composite) c;
			for (Control child : comp.getChildren()) {
				installMouseWheelScrollRecursively(scroller, child);
			}
		}
	}*/

	@SuppressWarnings("deprecation")
	protected void clearControls() {
		txtBuktiDiri.setText("");
		txtJabatan.setText("");
		cmbBidangUsaha.select(-1);
		txtNamaBadan.setText("");
		txtPemilik.setText("");
		txtKeterangan.setText("");
		txtJalanBadan.setText("");
		txtKotaBadan.setText("");
		txtKodePosBadan.setText("");
		txtTelponBadan.setText("");
		txtJalanPribadi.setText("");
		txtKotaPribadi.setText("");
		txtKodePosPribadi.setText("");
		txtTelponPribadi.setText("");
		txtNoKK.setText("");
		txtBuktiDiri.setText("");
		cmbJenisAssesment.select(-1);
		cmbJenisPajak.select(-1);
		cmbKecamatanBadan.select(-1);
		txtKecamatanPribadi.setText("");
		cmbKelurahanBadan.select(-1);
		txtKelurahanPribadi.setText("");
		cmbKewajibanPajak.select(-1);
		calTanggalDaftar.setDate(dateNow.getYear() + 1900, dateNow.getMonth(), dateNow.getDate());
		Control[] daftar = grpPendaftaran.getChildren();
		for (int i=0;i<daftar.length;i++)
		{
			if (daftar[i] instanceof Combo)
			{
				Combo child = (Combo) daftar[i];
//				child.removeAll();
				child.deselectAll();
			}
		}
		Control[] badan = grpAlamatBadanusaha.getChildren();
		for (int i=0;i<badan.length;i++)
		{
			if (badan[i] instanceof Combo)
			{
				Combo child = (Combo) badan[i];
//				child.removeAll();
				child.deselectAll();
			}
		}
		suratIzinTable.getTable().removeAll();
		spnIdAntri.setSelection(ControllerFactory.getMainController().getCpDaftarAntrianDAOImpl().getLastIndex()+1);
	}

	public void setFocus() {

	}
	
	private boolean isValidate(){
		if(txtBuktiDiri.getText().equalsIgnoreCase("") ||
//				txtJabatan.getText().equalsIgnoreCase("") ||
//				cmbBidangUsaha.getSelectionIndex() == -1 ||
//				txtKodePajak.getText().equalsIgnoreCase("") ||
//				txtNoPajak.getText().equalsIgnoreCase("") ||
//				txtKodeKecamatan.getText().equalsIgnoreCase("") ||
//				txtKodeKelurahan.getText().equalsIgnoreCase("") ||
//				txtNamaBadan.getText().equalsIgnoreCase("") ||
//				txtPemilik.getText().equalsIgnoreCase("") ||
//				txtJabatan.getText().equalsIgnoreCase("") ||
//				txtKeterangan.getText().equalsIgnoreCase("") ||
//				txtJalanBadan.getText().equalsIgnoreCase("") ||
//				txtKotaBadan.getText().equalsIgnoreCase("") ||
//				txtKodePosBadan.getText().equalsIgnoreCase("") ||
//				txtTelponBadan.getText().equalsIgnoreCase("") ||
//				txtJalanPribadi.getText().equalsIgnoreCase("") ||
//				txtKotaPribadi.getText().equalsIgnoreCase("") ||
//				txtKodePosPribadi.getText().equalsIgnoreCase("") ||
//				txtTelponPribadi.getText().equalsIgnoreCase("") ||
//				txtNoKK.getText().equalsIgnoreCase("") ||
				cmbKecamatanBadan.getSelectionIndex() == -1 ||
				cmbKelurahanBadan.getSelectionIndex() == -1 ||
				cmbJenisPajak.getSelectionIndex() == -1 ||
				cmbJenisAssesment.getSelectionIndex() == -1 ||
				cmbKewajibanPajak.getSelectionIndex() == -1 ||
				cmbBidangUsaha.getSelectionIndex() == -1
				){
			return false;
		}else
			return true;
	}
	
	private UserModul userModul = ControllerFactory.getMainController().getCpUserDAOImpl()
			.getHMUserModul(GlobalVariable.userModul.getIdUser()).get(1); // 1 merupakan id sub modul.
	private String dateApproval;
	private String npwpLama;
	private Label lblInsidentilPemerintahan;
	
	
	private boolean isSave(){		
		return userModul.getSimpan();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void accept(Object parameter) {
		try{
			edit = true;
			PendaftaranWajibPajak wp = (PendaftaranWajibPajak) parameter;

			txtNoPendaftaran.setText(wp.getNoPendaftaran());
			txtKodePajak.setText(wp.getNPWP().substring(0, 1));
			txtKodePajak.pack(true);
			txtNoPajak.setText(wp.getNPWP().substring(1, 8));
			txtNoPajak.pack(true);
			txtKodeKecamatan.setText(wp.getNPWP().substring(8, 10));
			txtKodeKecamatan.pack(true);
			txtKodeKelurahan.setText(wp.getNPWP().substring(10, 12));
			txtKodeKelurahan.pack(true);
			if(wp.getNoFormulir() == null)
				txtNoFormulir.setText("");
			else
				txtNoFormulir.setText(wp.getNoFormulir());
			spnIdAntri.setSelection(0);
			spnIdAntri.setEnabled(false);
			if(wp.getJenisPajak().equalsIgnoreCase("B"))
				cmbJenisPajak.setText("Badan");
			else
				cmbJenisPajak.setText("Pribadi");
			if(wp.getJenisAssesment().equalsIgnoreCase("Self"))
				cmbJenisAssesment.select(0);
			else
				cmbJenisAssesment.select(1);
			calTanggalDaftar.setDate(wp.getTanggalDaftar().getYear()+1900, wp.getTanggalDaftar().getMonth(), wp.getTanggalDaftar().getDate());
			txtNamaBadan.setText(wp.getNamaBadan());
			txtPemilik.setText(wp.getNamaPemilik());
			txtJabatan.setText(wp.getJabatan());
			cmbKewajibanPajak.select(wp.getKewajibanPajak()-2);
			cmbBidangUsaha.select(cmbBidangUsaha.indexOf((String)cmbBidangUsaha.getData("B"+wp.getIdSubPajak())));

			txtKeterangan.setText(wp.getKeterangan());
			chkInsidentil.setSelection(wp.getInsidentil());
//			System.out.println(wp.getInsidentil_Pemerintah());
//			System.out.println(wp.getInsidentil());
//			chkInsidentil_pemerintahan.setSelection(wp.getInsidentil_Pemerintah());
			
			txtJalanBadan.setText(wp.getAlabadJalan());
			cmbKecamatanBadan.select(cmbKecamatanBadan.indexOf(wp.getAlabadKecamatan()));
			cmbKelurahanBadan.select(cmbKelurahanBadan.indexOf(wp.getAlabadKelurahan()));
			txtKodePosBadan.setText(wp.getAlabadKodePos());
			txtTelponBadan.setText(wp.getAlabadTelepon());
			
			txtJalanPribadi.setText(wp.getAlatingJalan());
			txtKecamatanPribadi.setText(wp.getAlatingKecamatan());
			txtKelurahanPribadi.setText(wp.getAlatingKelurahan());
			txtKodePosPribadi.setText(wp.getAlatingKodePos());
			txtKotaPribadi.setText(wp.getAlatingKota());
			txtTelponPribadi.setText(wp.getAlatingTelepon());
			
			if(wp.getKewarganegaraan().equalsIgnoreCase("WNA")){
				btnWni.setSelection(false);
				btnWna.setSelection(true);
			}else{
				btnWni.setSelection(true);
				btnWna.setSelection(false);
			}
			
			if(wp.getTandaBuktiDiri().equalsIgnoreCase("Paspor")){
				btnKtp.setSelection(false);
				btnPassport.setSelection(true);
				btnSim.setSelection(false);
			}else if(wp.getTandaBuktiDiri().equalsIgnoreCase("SIM")){
				btnKtp.setSelection(false);
				btnPassport.setSelection(false);
				btnSim.setSelection(true);
			}else{
				btnKtp.setSelection(true);
				btnPassport.setSelection(false);
				btnSim.setSelection(false);
			}
			txtBuktiDiri.setText(wp.getNoBuktiDiri());
			txtNoKK.setText(wp.getNoKartuKeluarga());
			
//			suratIzinTable
			suratIzinTable.loadSuratIzin(ControllerFactory.getMainController().getCpSuratIzinUsahaDAOImpl().getPendaftaranSuratIzinUsaha(wp.getNPWP()));
			
			if(wp.getStatus().equalsIgnoreCase("Aktif")){
				chkAktif.setSelection(true);
				txtKeteranganNonAktif.setEnabled(false);
				txtKeteranganNonAktif.setText("");
				txtKeteranganTutup.setText("");
				chkTutup.setSelection(false);
				chkTutup.setEnabled(false);
			}else if(wp.getStatus().equalsIgnoreCase("NonAktif")){
				chkAktif.setSelection(false);
				txtKeteranganNonAktif.setEnabled(true);
				txtKeteranganNonAktif.setText(wp.getKeteranganTutup());
				txtKeteranganTutup.setText("");
				chkTutup.setEnabled(true);
				chkTutup.setSelection(false);
			}else{
				chkAktif.setSelection(false);
				chkTutup.setEnabled(true);
				chkTutup.setSelection(true);
				txtKeteranganTutup.setEnabled(true);
				txtKeteranganTutup.setEditable(true);
				dateTglMulaiTutup.setEnabled(true);
				dateTglSampaiTutup.setEnabled(true);
				txtSuratKeteranganTutup.setEnabled(true);
				txtSuratKeteranganTutup.setEditable(true);
				grpTutup.setEnabled(true);
				WpTutup wpT = ControllerFactory.getMainController().getCpWajibPajakDAOImpl().getWajibPajakTutup(wp.getNPWP());
				if(wpT != null){
					if(!wpT.getNoSuratTutup().equalsIgnoreCase(""))
						txtSuratKeteranganTutup.setText(wpT.getNoSuratTutup());
					if(wpT.getTglMulaiTutup() != null)
						dateTglMulaiTutup.setDate(wpT.getTglMulaiTutup().getYear()+1900, wpT.getTglMulaiTutup().getMonth(), wpT.getTglMulaiTutup().getDate());
					if(wpT.getTglSampaiTutup() != null)
						dateTglSampaiTutup.setDate(wpT.getTglSampaiTutup().getYear()+1900, wpT.getTglSampaiTutup().getMonth(), wpT.getTglSampaiTutup().getDate());
					if(!wpT.getKeterangan().equalsIgnoreCase(""))
						txtKeteranganTutup.setText(wpT.getKeterangan());
					else
						txtKeteranganTutup.setText(wp.getKeteranganTutup());
				}else{
					txtSuratKeteranganTutup.setText("");
					dateTglMulaiTutup.setDate(dateNow.getYear()+1900,dateNow.getMonth(),dateNow.getDate());
					dateTglSampaiTutup.setDate(dateNow.getYear()+1900,dateNow.getMonth(),dateNow.getDate());
					txtKeteranganTutup.setText(wp.getKeteranganTutup());
				}
			}
			
			txtNoFormulir.setEnabled(true);
			
			dateApproval = wp.getTanggalApprove();
			npwpLama = wp.getNPWP();
		}
		catch (Exception ex){
			ex.printStackTrace();
			MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Error Loading Data");
		}
		
	}
	
	private void saveSuratIzin(){
		ControllerFactory.getMainController().getCpSuratIzinUsahaDAOImpl().deleteSuratIzinUsaha(npwpLama);
		if(suratIzinTable.getTable().getItems().length>0){
			PendaftaranSuratIzinUsaha izinUsaha = new PendaftaranSuratIzinUsaha();
			/*Object[] suratIzin = new Object[suratIzinTable.getTable().getItems().length];
			Object[] noSurat = new Object[suratIzinTable.getTable().getItems().length];
			Object[] tglSurat = new Object[suratIzinTable.getTable().getItems().length];*/
			for(TableItem item : suratIzinTable.getTable().getItems()){
				izinUsaha.setId(null);
				izinUsaha.setNPWP(npwpLama);
				izinUsaha.setSuratIzin(item.getText(0));
				izinUsaha.setNoSurat(item.getText(1));
				izinUsaha.setTanggalSurat(item.getText(2));
				ControllerFactory.getMainController().getCpSuratIzinUsahaDAOImpl().savePendaftaranSuratIzinUsaha(izinUsaha);
			}
			/*org.hsqldb.types.Type type = org.hsqldb.types.Type.SQL_VARCHAR_DEFAULT;
			JDBCArrayBasic arrSuratIzin = new JDBCArrayBasic(suratIzin, type);
			JDBCArrayBasic arrNoSurat = new JDBCArrayBasic(noSurat, type);
			JDBCArrayBasic arrTglSurat = new JDBCArrayBasic(tglSurat, type);
			izinUsaha.setSuratIzin(arrSuratIzin);
			izinUsaha.setNoSurat(arrNoSurat);
			izinUsaha.setTglSurat(arrTglSurat);*/
		}
	}
}