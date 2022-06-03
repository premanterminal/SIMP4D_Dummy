package com.dispenda.sptpd.views;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
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
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.SWTResourceManager;

import com.dispenda.controller.ControllerFactory;
import com.dispenda.controller.UIController;
import com.dispenda.model.KecamatanProvider;
import com.dispenda.model.KelurahanProvider;
import com.dispenda.model.PajakProvider;
import com.dispenda.model.Pejabat;
import com.dispenda.model.UU;
import com.dispenda.model.UserModul;
import com.dispenda.object.GlobalVariable;
import com.dispenda.object.Preferences;
import com.dispenda.report.ReportAction;
import com.dispenda.report.ReportAppContext;
import com.dispenda.report.ReportModule;

import org.eclipse.swt.custom.StackLayout;

public class DaftarSKPDView extends ViewPart {
	public DaftarSKPDView() {
	}
	
	public static final String ID = DaftarSKPDView.class.getName();
	private Button btnTanggal;
	private DateTime calSPTPD;
	private Combo cmbPajak;
	private Date masaPajakDari;
	private Date masaPajakSampai;
	private Button btnBulan;
	private Button btnTahun;
	private Table tbl_SKPD;
	private Locale ind = new Locale("id", "ID");
	private NumberFormat indFormat = NumberFormat.getCurrencyInstance(ind);
	private Group composite;
	private DateTime monthSPT;
	private Timestamp dateNow;
	private Spinner yearSPT;
	private Composite compAbsolute;
	private String masaPajak;
	private Color fontColor = new Color(Display.getCurrent(),Preferences.FONT_COLOR);
	private Combo cmbPejabat;
	private Composite composite_1;
	private Button btnCetakBlanko;
	private Button btnCetakDaftar;
	private Label lblPenandaTangan;
	private List<Pejabat> listPejabat;
	private Button btnPilihan;
	private DateTime calSampai;
	private Button btnCetakHasilPerhitungan;
	private Button btnCetakLampiranSK;
	private Button btnCetakNota;
	private Group grpPenandatangan;
	private UserModul userModul = ControllerFactory.getMainController().getCpUserDAOImpl()
			.getHMUserModul(GlobalVariable.userModul.getIdUser()).get(32); // 1 merupakan id sub modul.
	private Combo cmbPendaftaran;
	private Combo cmbPenetapan;
	private Combo cmbKasiPendaftaran;
	private Combo cmbKasiPenetapan;
	private Button chkSSPD;
	private Label lblKecamatan;
	private Combo cmbKecamatan;
	private Label label;
	private Combo cmbKelurahan;
	private Button btnCetakSuratKeputusan;
	private Button btnplt;

	@SuppressWarnings("deprecation")
	@Override
	public void createPartControl(Composite parent) {
		Locale.setDefault(new Locale("id","ID"));
		dateNow = (java.sql.Timestamp)ControllerFactory.getMainController().getCpWajibPajakDAOImpl().getDateNow();
		
		ScrolledComposite scrolledComposite = new ScrolledComposite(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		
		Composite comp = new Composite(scrolledComposite, SWT.NONE);
		comp.setLayout(new GridLayout(11, false));
		
		Label lblKewajibanPajak = new Label(comp, SWT.NONE);
		lblKewajibanPajak.setForeground(fontColor);
		lblKewajibanPajak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblKewajibanPajak.setText("Kewajiban Pajak");
		
		//Catatan SF: ini style nya si dropdown jenis pajak
		cmbPajak = new Combo(comp, SWT.READ_ONLY);
		cmbPajak.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		cmbPajak.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		cmbPajak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		GridData gd_cmbPajak = new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1);
		gd_cmbPajak.widthHint = 222;
		UIController.INSTANCE.loadPajak(cmbPajak, PajakProvider.INSTANCE.getPajak().toArray());
		cmbPajak.setLayoutData(gd_cmbPajak);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		
		lblKecamatan = new Label(comp, SWT.NONE);
		lblKecamatan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblKecamatan.setForeground(fontColor);
		lblKecamatan.setText("Kecamatan");
		
		cmbKecamatan = new Combo(comp, SWT.READ_ONLY);
		cmbKecamatan.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Combo c = (Combo)e.widget;
				if(c.getSelectionIndex() > -1)
					UIController.INSTANCE.loadKelurahan(cmbKelurahan, KelurahanProvider.INSTANCE.getKelurahan((Integer)c.getData(Integer.toString(c.getSelectionIndex()))).toArray());
			}
		});
		cmbKecamatan.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent arg0) {
				if(cmbKecamatan.getSelectionIndex() > -1)
					UIController.INSTANCE.loadKelurahan(cmbKelurahan, KelurahanProvider.INSTANCE.getKelurahan((Integer)cmbKecamatan.getData(Integer.toString(cmbKecamatan.getSelectionIndex()))).toArray());
			}
		});
		cmbKecamatan.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		cmbKecamatan.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		cmbKecamatan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		GridData gd_cmbKecamatan = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_cmbKecamatan.widthHint = 183;
		cmbKecamatan.setLayoutData(gd_cmbKecamatan);
		UIController.INSTANCE.loadKecamatan(cmbKecamatan, KecamatanProvider.INSTANCE.getKecamatan().toArray());
		
		label = new Label(comp, SWT.NONE);
		label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label.setText("-");
		
		cmbKelurahan = new Combo(comp, SWT.READ_ONLY);
		cmbKelurahan.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		cmbKelurahan.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		cmbKelurahan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		GridData gd_cmbKelurahan = new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1);
		gd_cmbKelurahan.widthHint = 209;
		cmbKelurahan.setLayoutData(gd_cmbKelurahan);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		
		Label lblTanggalSkpd = new Label(comp, SWT.NONE);
		lblTanggalSkpd.setForeground(fontColor);
		lblTanggalSkpd.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblTanggalSkpd.setText("Tanggal SKPD");
		
		composite = new Group(comp, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1));
		composite.setLayout(new GridLayout(4, false));
		
		btnTanggal = new Button(composite, SWT.RADIO);
		btnTanggal.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				calSPTPD.setVisible(true);
				calSPTPD.pack(true);
				calSampai.setVisible(false);
				monthSPT.setVisible(false);
				yearSPT.setVisible(false);
				setMasaPajak();
			}
		});
		btnTanggal.setSelection(false);
		btnTanggal.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BORDER));
		btnTanggal.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnTanggal.setText("Tanggal");
		
		
		btnBulan = new Button(composite, SWT.RADIO);
		btnBulan.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				monthSPT.setVisible(true);
				monthSPT.pack(true);
				calSPTPD.setVisible(false);
				calSampai.setVisible(false);
				yearSPT.setVisible(false);
				setMasaPajak();
			}
		});
		btnBulan.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BORDER));
		btnBulan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnBulan.setText("Bulan");
		
		
		btnTahun = new Button(composite, SWT.RADIO);
		btnTahun.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				yearSPT.setVisible(true);
				yearSPT.pack(true);
				calSPTPD.setVisible(false);
				calSampai.setVisible(false);
				monthSPT.setVisible(false);
				setMasaPajak();
			}
		});
		btnTahun.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BORDER));
		btnTahun.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnTahun.setText("Tahun");
		
		btnPilihan = new Button(composite, SWT.RADIO);
		btnPilihan.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				calSPTPD.setVisible(true);
				calSPTPD.pack(true);
				calSampai.setVisible(true);
				monthSPT.setVisible(false);
				yearSPT.setVisible(false);
				setMasaPajak();
			}
		});
		btnPilihan.setText("Pilihan");
		btnPilihan.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BORDER));
		btnPilihan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		
		compAbsolute = new Composite(composite, SWT.NONE);
		compAbsolute.setLayout(new StackLayout());
		compAbsolute.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 3, 1));
		
		calSPTPD = new DateTime(compAbsolute, SWT.BORDER | SWT.DROP_DOWN);
		calSPTPD.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		calSPTPD.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setMasaPajak();
				
			}
		});
		calSPTPD.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BORDER));
		calSPTPD.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		yearSPT = new Spinner(compAbsolute, SWT.BORDER);
		yearSPT.setMaximum(dateNow.getYear() + 1900);
		yearSPT.setMinimum(2006);
		yearSPT.setSelection(dateNow.getYear() + 1900);
		yearSPT.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		yearSPT.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setMasaPajak();
			}
		});
		yearSPT.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		yearSPT.setValues(dateNow.getYear() + 1900, 2006, dateNow.getYear() + 1900, 0, 1, 10);
		yearSPT.setVisible(false);
		
		monthSPT = new DateTime(compAbsolute, SWT.BORDER | SWT.DROP_DOWN | SWT.SHORT);
		monthSPT.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		monthSPT.setDay(1);
		monthSPT.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setMasaPajak();
			}
		});
		monthSPT.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		monthSPT.setVisible(false);
		
		calSampai = new DateTime(composite, SWT.BORDER | SWT.DROP_DOWN);
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
		
		grpPenandatangan = new Group(comp, SWT.NONE);
		grpPenandatangan.setText("Penanda Tangan");
		grpPenandatangan.setLayout(new GridLayout(2, false));
		GridData gd_grpPenandatangan = new GridData(SWT.LEFT, SWT.FILL, false, false, 2, 3);
		gd_grpPenandatangan.widthHint = 465;
		grpPenandatangan.setLayoutData(gd_grpPenandatangan);
		grpPenandatangan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		Label lblNewLabel = new Label(grpPenandatangan, SWT.NONE);
		lblNewLabel.setText("Petugas Teknis 1");
		lblNewLabel.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblNewLabel.setForeground(fontColor);
		
		cmbPendaftaran = new Combo(grpPenandatangan, SWT.READ_ONLY);
		GridData gd_cmbPendaftaran = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_cmbPendaftaran.widthHint = 260;
		cmbPendaftaran.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_FOREGROUND));
		cmbPendaftaran.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		cmbPendaftaran.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		cmbPendaftaran.setLayoutData(gd_cmbPendaftaran);
		
		Label lblNewLabel_1 = new Label(grpPenandatangan, SWT.NONE);
		lblNewLabel_1.setText("Petugas Teknis 2");
		lblNewLabel_1.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblNewLabel_1.setForeground(fontColor);
		
		cmbPenetapan = new Combo(grpPenandatangan, SWT.READ_ONLY);
		GridData gd_cmbPenetapan = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
		gd_cmbPenetapan.widthHint = 263;
		cmbPenetapan.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_FOREGROUND));
		cmbPenetapan.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		cmbPenetapan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		cmbPenetapan.setLayoutData(gd_cmbPenetapan);
		
		Label lblNewLabel_2 = new Label(grpPenandatangan, SWT.NONE);
		lblNewLabel_2.setText("Kasubbid Teknis");
		lblNewLabel_2.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblNewLabel_2.setForeground(fontColor);
		
		cmbKasiPendaftaran = new Combo(grpPenandatangan, SWT.READ_ONLY);
		GridData gd_cmbKasiPendaftaran = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_cmbKasiPendaftaran.widthHint = 263;
		cmbKasiPendaftaran.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_FOREGROUND));
		cmbKasiPendaftaran.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		cmbKasiPendaftaran.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		cmbKasiPendaftaran.setLayoutData(gd_cmbKasiPendaftaran);
		
		Label lblNewLabel_3 = new Label(grpPenandatangan, SWT.NONE);
		lblNewLabel_3.setText("Kasi Penetapan");
		lblNewLabel_3.setVisible(false);
		lblNewLabel_3.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblNewLabel_3.setForeground(fontColor);
		
		cmbKasiPenetapan = new Combo(grpPenandatangan, SWT.READ_ONLY);
		GridData gd_cmbKasiPenetapan = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_cmbKasiPenetapan.widthHint = 263;
		cmbKasiPenetapan.setVisible(false);
		cmbKasiPenetapan.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_FOREGROUND));
		cmbKasiPenetapan.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		cmbKasiPenetapan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		cmbKasiPenetapan.setLayoutData(gd_cmbKasiPenetapan);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		
		Button btnLihat = new Button(comp, SWT.NONE);
		btnLihat.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnLihat.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//Catatan SF: cek dropdown pajak apakah kosong atau tidak
				if (cmbPajak.getSelectionIndex() >= 0)
				{
					//Catatan SF: jika sudah dipilih jenis pajaknya, maka jalankan perintah dibawah
					try {
						ResultSet result = null;
						tbl_SKPD.removeAll();
						deleteColumns();
						int kec = 0;
						int kel = 0;
						if (cmbKecamatan.getSelectionIndex() >= 0)
							kec = Integer.valueOf((String) cmbKecamatan.getData(cmbKecamatan.getItem(cmbKecamatan.getSelectionIndex())));
						if (cmbKelurahan.getSelectionIndex() >= 0)
							kel = Integer.valueOf((String)cmbKelurahan.getData(cmbKelurahan.getItem(cmbKelurahan.getSelectionIndex())));
						
						if (chkSSPD.getSelection()){
							//Catatan SF: Jika centang "tampilkan sspd" maka jalankan perintah dibawah ini
							result = ControllerFactory.getMainController().getCpSptpdDAOImpl().getDaftarSKPDSSPD(cmbPajak.getData(Integer.toString(cmbPajak.getSelectionIndex())).toString(), masaPajakDari, masaPajakSampai, kec, kel);
							createColumnSSPD();
						}else{
							//Catatan SF:  Jika tidak centang "tampilkan sspd" maka jalankan perintah dibawah ini
							result = ControllerFactory.getMainController().getCpSptpdDAOImpl().getDaftarSKPD(cmbPajak.getData(Integer.toString(cmbPajak.getSelectionIndex())).toString(), masaPajakDari, masaPajakSampai, kec, kel);
							createColumn();
						}
						loadTableSPTPD(result);
					} catch (ParseException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		GridData gd_btnLihat = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnLihat.widthHint = 90;
		btnLihat.setLayoutData(gd_btnLihat);
		btnLihat.setText("Lihat");
		new Label(comp, SWT.NONE);
		
		chkSSPD = new Button(comp, SWT.CHECK);
		chkSSPD.setText("Tampilkan SSPD");
		chkSSPD.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		
		lblPenandaTangan = new Label(comp, SWT.NONE);
		lblPenandaTangan.setForeground(fontColor);
		lblPenandaTangan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblPenandaTangan.setText("Penanda Tangan");
		
		cmbPejabat = new Combo(comp, SWT.READ_ONLY);
		GridData gd_cmbPejabat = new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1);
		gd_cmbPejabat.widthHint = 203;
		cmbPejabat.setLayoutData(gd_cmbPejabat);
		cmbPejabat.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_FOREGROUND));
		cmbPejabat.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		cmbPejabat.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		loadPejabat();
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		
		btnplt = new Button(comp, SWT.CHECK);
		btnplt.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		btnplt.setText("plt.");
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
//		new Label(comp, SWT.NONE);
//		new Label(comp, SWT.NONE);
//		new Label(comp, SWT.NONE);
//		new Label(comp, SWT.NONE);
//		new Label(comp, SWT.NONE);
//		new Label(comp, SWT.NONE);
//		new Label(comp, SWT.NONE);
//		new Label(comp, SWT.NONE);
//		new Label(comp, SWT.NONE);
//		new Label(comp, SWT.NONE);
		
		composite_1 = new Composite(comp, SWT.NONE);
		composite_1.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 9, 1));
		composite_1.setLayout(new GridLayout(6, false));
		
		btnCetakBlanko = new Button(composite_1, SWT.NONE);
		btnCetakBlanko.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (cmbPejabat.getText().equalsIgnoreCase("")){
					MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Silahkan isi nama pejabat");
				}
				else
				{
					String pangkat = "";
					if (masaPajakDari.after(new Date(117, 1, 19)))
						pangkat = listPejabat.get(cmbPejabat.getSelectionIndex()).getJabatan();
					else
						pangkat = "Kabid Pendataan dan Penetapan";
					ReportAppContext.name = "Variable";
					ReportAppContext.map.put("Nama", cmbPejabat.getText());
					ReportAppContext.map.put("Pangkat", pangkat);
					ReportAppContext.nameObject = "BlankoSKPD";
					ReportAppContext.classLoader = DaftarSKPDView.class.getClassLoader();
					if(!ReportAppContext.object.isEmpty())
						ReportAppContext.object.clear();
					for(int j = 0;j<tbl_SKPD.getItems().length - 1;j++){// untuk masukan row table
						List<String> values = new ArrayList<String>();
						for(int i=0;i<tbl_SKPD.getColumnCount();i++){// untuk masukkan data berdasarkan colom table
							if (i == 6)
								values.add(getNPWPDFormat(tbl_SKPD.getItem(j).getText(i)));
							else
								values.add(tbl_SKPD.getItem(j).getText(i));
						}
						values.add(listPejabat.get(cmbPejabat.getSelectionIndex()).getNamaPejabat());
						values.add(listPejabat.get(cmbPejabat.getSelectionIndex()).getPangkat());
						values.add(listPejabat.get(cmbPejabat.getSelectionIndex()).getIdPejabatNIP());
						values.add(getNoUrutFormat(tbl_SKPD.getItem(j).getText(1)));
						try {
							values.add(new ReportModule().getTerbilang(indFormat.parse(tbl_SKPD.getItem(j).getText(14)).doubleValue()));
						} catch (ParseException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						ReportAppContext.object.put(Integer.toString(j), values);
					}
					if (btnplt.getSelection() == true){
						ReportAction.start("BlankoSKPDPLT.rptdesign");
					}
					else{
						ReportAction.start("BlankoSKPD.rptdesign");
					}
					
				}
				
			}
		});
		GridData gd_btnCetakBlanko = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnCetakBlanko.widthHint = 140;
		btnCetakBlanko.setLayoutData(gd_btnCetakBlanko);
		btnCetakBlanko.setText("Cetak Blanko");
		btnCetakBlanko.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BORDER));
		btnCetakBlanko.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		btnCetakDaftar = new Button(composite_1, SWT.NONE);
		btnCetakDaftar.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
				ReportAppContext.name = "Variable";
				if (masaPajakSampai.compareTo(masaPajakDari) == 0){
					ReportAppContext.map.put("MasaPajak", df.format(masaPajakDari));
				}
				else{
					ReportAppContext.map.put("MasaPajak", df.format(masaPajakDari) + " s/d " + df.format(masaPajakSampai));
				}
//				ReportAppContext.map.put("TotalPajak", value);
				ReportAppContext.nameObject = "DaftarSKPD_tabel";
				ReportAppContext.classLoader = DaftarSKPDView.class.getClassLoader();
				if(!ReportAppContext.object.isEmpty())
					ReportAppContext.object.clear();
				for(int j = 0;j<tbl_SKPD.getItems().length-1;j++){// untuk masukan row table
					List<String> values = new ArrayList<String>();
					values.add(String.valueOf(j + 1));
					values.add(tbl_SKPD.getItem(j).getText(1));
					values.add(tbl_SKPD.getItem(j).getText(6));
					values.add(tbl_SKPD.getItem(j).getText(4));
					values.add(tbl_SKPD.getItem(j).getText(5));
					values.add(tbl_SKPD.getItem(j).getText(8));
					values.add(tbl_SKPD.getItem(j).getText(19));
					values.add(tbl_SKPD.getItem(j).getText(14).substring(2).replace(".", "").replace(",", "."));
					values.add(tbl_SKPD.getItem(j).getText(20));
					values.add(tbl_SKPD.getItem(j).getText(2));
					if (chkSSPD.getSelection()){
						values.add(tbl_SKPD.getItem(j).getText(21).substring(2).replace(".", "").replace(",", "."));
						values.add(tbl_SKPD.getItem(j).getText(22).substring(2).replace(".", "").replace(",", "."));
						values.add(tbl_SKPD.getItem(j).getText(23));
						values.add(tbl_SKPD.getItem(j).getText(24));
						values.add(tbl_SKPD.getItem(j).getText(25));
					}
					ReportAppContext.object.put(Integer.toString(j), values);
				}
				if (chkSSPD.getSelection())
					ReportAction.start("DaftarSKPD2.rptdesign");
				else
					ReportAction.start("DaftarSKPD.rptdesign");
			}
		});
		GridData gd_btnCetakDaftar = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnCetakDaftar.widthHint = 140;
		btnCetakDaftar.setLayoutData(gd_btnCetakDaftar);
		btnCetakDaftar.setText("Cetak Daftar");
		btnCetakDaftar.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BORDER));
		btnCetakDaftar.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		btnCetakSuratKeputusan = new Button(composite_1, SWT.NONE);
		btnCetakSuratKeputusan.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (cmbPejabat.getText().equalsIgnoreCase("")){
					MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Silahkan isi Penanda Tangan");
					cmbPejabat.forceFocus();
				}
				else{
					ReportAppContext.name = "Variable";
					String nomor = "973.SI/" + (cmbPajak.getSelectionIndex() + 2) + ".........";
					String NamaSKPD = "PAJAK " + cmbPajak.getText().toUpperCase();
					if (cmbPajak.getSelectionIndex() > -1)
						NamaSKPD += " - " + cmbPajak.getText().toUpperCase();
					NamaSKPD += " OFFICIAL";
					String menetapkan = "";
					//List<UU> listUU = new ArrayList<UU>();
					//listUU = ControllerFactory.getMainController().getCpUUDAOImpl().getUUSK(cmbPajak.getData(Integer.toString(cmbPajak.getSelectionIndex())).toString());
					//String point6 = listUU.get(0).getUU();
					//String point8 = listUU.get(1).getUU();
					menetapkan = "SURAT KEPUTUSAN KEPALA BADAN PENGELOLA PAJAK DAN RETRIBUSI DAERAH KOTA MEDAN" +
							" TENTANG SURAT KETETAPAN PAJAK DAERAH (SKPD) " +
							"PAJAK " + cmbPajak.getText() + ".";
					String pattern = "dd-MM-yyyy";
					String dateInString =new SimpleDateFormat(pattern).format(new Date());
					//ReportAppContext.map.put("nomor", nomor);
					//ReportAppContext.map.put("namaPajak", NamaSKPD);
					//ReportAppContext.map.put("point6", point6);
					//ReportAppContext.map.put("point8", point8);
					ReportAppContext.map.put("tanggal", dateInString);
					ReportAppContext.map.put("Menetapkan", menetapkan);
					ReportAppContext.map.put("penandatangan", String.valueOf(listPejabat.get(cmbPejabat.getSelectionIndex()).getNamaPejabat().replace("Zulkarnain", "ZULKARNAIN")));
					ReportAppContext.map.put("jabatan", String.valueOf(listPejabat.get(cmbPejabat.getSelectionIndex()).getPangkat()));
					ReportAppContext.map.put("nip", String.valueOf(listPejabat.get(cmbPejabat.getSelectionIndex()).getIdPejabatNIP()));
					if (cmbPajak.getSelectionIndex() == 0){
						if (btnplt.getSelection() == true){
							ReportAction.start("SuratKeputusanSKPDPLT-AirTanah.rptdesign");
						}
						else{
							ReportAction.start("SuratKeputusanSKPD-AirTanah.rptdesign");
						}
					}
					else if (cmbPajak.getSelectionIndex() == 5){
						if (btnplt.getSelection() == true){
							ReportAction.start("SuratKeputusanSKPDPLT-Reklame.rptdesign");
						}
						else{
							ReportAction.start("SuratKeputusanSKPD-Reklame.rptdesign");
						}
					}
					else
						MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Surat Keputusan Pajak Daerah (SKPD) Tidak Tersedia");
				}
				
			}
		});
		btnCetakSuratKeputusan.setText("Cetak Surat Keputusan");
		btnCetakSuratKeputusan.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BORDER));
		btnCetakSuratKeputusan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		btnCetakHasilPerhitungan = new Button(composite_1, SWT.NONE);
		btnCetakHasilPerhitungan.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(isPrint()){
					if (isValid()){
						ReportAppContext.name = "Variable";
						String NamaSKPD = "PAJAK " + cmbPajak.getText();
						NamaSKPD += " OFFICIAL ASSESMENT TAHUN " + (masaPajakDari.getYear() + 1900);
						ReportAppContext.map.put("NamaSKPD", String.valueOf(NamaSKPD));
						ReportAppContext.map.put("petugasPendataan", String.valueOf(listPejabat.get(cmbPendaftaran.getSelectionIndex()).getNamaPejabat()));
						ReportAppContext.map.put("nipPendataan", String.valueOf(listPejabat.get(cmbPendaftaran.getSelectionIndex()).getIdPejabatNIP()));
						ReportAppContext.map.put("petugasPenetapan", String.valueOf(listPejabat.get(cmbPenetapan.getSelectionIndex()).getNamaPejabat()));
						ReportAppContext.map.put("nipPenetapan", String.valueOf(listPejabat.get(cmbPenetapan.getSelectionIndex()).getIdPejabatNIP()));
						ReportAppContext.map.put("kasiPendataan", String.valueOf(listPejabat.get(cmbKasiPendaftaran.getSelectionIndex()).getNamaPejabat()));
						ReportAppContext.map.put("nipKasiPendataan", String.valueOf(listPejabat.get(cmbKasiPendaftaran.getSelectionIndex()).getIdPejabatNIP()));
//						ReportAppContext.map.put("kasiPenetapan", String.valueOf(listPejabat.get(cmbKasiPenetapan.getSelectionIndex()).getNamaPejabat()));
//						ReportAppContext.map.put("nipKasiPenetapan", String.valueOf(listPejabat.get(cmbKasiPenetapan.getSelectionIndex()).getIdPejabatNIP()));
						ReportAppContext.map.put("kabid", String.valueOf(listPejabat.get(cmbPejabat.getSelectionIndex()).getNamaPejabat()));
						ReportAppContext.map.put("jabatan", String.valueOf(listPejabat.get(cmbPejabat.getSelectionIndex()).getPangkat()));
						ReportAppContext.map.put("nipKabid", String.valueOf(listPejabat.get(cmbPejabat.getSelectionIndex()).getIdPejabatNIP()));
						ReportAppContext.map.put("jabKabid", String.valueOf(listPejabat.get(cmbPejabat.getSelectionIndex()).getJabatan().replace("Kepala ", "")));
						ReportAppContext.map.put("Seksi", String.valueOf(listPejabat.get(cmbKasiPendaftaran.getSelectionIndex()).getJabatan().replace("Kepala ", "")));
						ReportAppContext.map.put("tahun", String.valueOf(masaPajakDari.getYear() + 1900));
						
						ReportAppContext.nameObject = "DaftarSKPDKB_tabel";
						ReportAppContext.classLoader = DaftarSKPDView.class.getClassLoader();
						if(!ReportAppContext.object.isEmpty())
							ReportAppContext.object.clear();
						for(int j = 0;j<tbl_SKPD.getItems().length - 1;j++){
							List<String> values = new ArrayList<String>();
							values.add(tbl_SKPD.getItem(j).getText(0));
							values.add(tbl_SKPD.getItem(j).getText(1));
							values.add(tbl_SKPD.getItem(j).getText(6));
							values.add(tbl_SKPD.getItem(j).getText(4));
							values.add(tbl_SKPD.getItem(j).getText(19));
							values.add(tbl_SKPD.getItem(j).getText(5));
							values.add(tbl_SKPD.getItem(j).getText(10).replace(".", "").replace(",", "."));
							values.add(tbl_SKPD.getItem(j).getText(13).replace(".", "").replace(",", "."));
							values.add(tbl_SKPD.getItem(j).getText(14).replace(".", "").replace(",", "."));
							ReportAppContext.object.put(Integer.toString(j), values);
						}
						if (btnplt.getSelection()==true){
							ReportAction.start("DaftarHasilPerhitunganSKPDPLT.rptdesign");
						}
						else{
							ReportAction.start("DaftarHasilPerhitunganSKPD.rptdesign");
						}
						
				}
				else
					MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Silahkan isi Penanda Tangan");
			}else
				MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Anda tidak memiliki hak untuk mencetak.");
			}
		});
		btnCetakHasilPerhitungan.setText("Cetak Hasil Perhitungan");
		btnCetakHasilPerhitungan.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BORDER));
		btnCetakHasilPerhitungan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		btnCetakLampiranSK = new Button(composite_1, SWT.NONE);
		btnCetakLampiranSK.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(isPrint()){
					ReportAppContext.name = "Variable";
					Double total = 0.0;
					String tentang = "Surat Ketetapan Pajak Daerah (SKPD) Pajak " + cmbPajak.getText() + " Official Assesment";
					
					ReportAppContext.map.put("nomor", "973.SI/" + (cmbPajak.getSelectionIndex() + 2));
					ReportAppContext.map.put("tentang", tentang);
					
					ReportAppContext.nameObject = "DaftarSKPDKB_tabel";
					ReportAppContext.classLoader = DaftarSKPDView.class.getClassLoader();
					if(!ReportAppContext.object.isEmpty())
						ReportAppContext.object.clear();
					if(!ReportAppContext.object.isEmpty())
						ReportAppContext.object.clear();
					for(int j = 0;j<tbl_SKPD.getItems().length - 1;j++){
						List<String> values = new ArrayList<String>();
						values.add(tbl_SKPD.getItem(j).getText(0));
						values.add(tbl_SKPD.getItem(j).getText(1));
						values.add(tbl_SKPD.getItem(j).getText(6));
						values.add(tbl_SKPD.getItem(j).getText(4));
						values.add(tbl_SKPD.getItem(j).getText(19));
						values.add(tbl_SKPD.getItem(j).getText(5));
						values.add(tbl_SKPD.getItem(j).getText(10).replace(".", "").replace(",", "."));
						values.add(tbl_SKPD.getItem(j).getText(13).replace(".", "").replace(",", "."));
						values.add(tbl_SKPD.getItem(j).getText(14).replace(".", "").replace(",", "."));
						ReportAppContext.object.put(Integer.toString(j), values);
					}
					ReportAppContext.map.put("penandatangan", String.valueOf(listPejabat.get(cmbPejabat.getSelectionIndex()).getNamaPejabat().replace("Zulkarnain", "ZULKARNAIN")));
					ReportAppContext.map.put("jabatan", String.valueOf(listPejabat.get(cmbPejabat.getSelectionIndex()).getPangkat()));
					ReportAppContext.map.put("nip", String.valueOf(listPejabat.get(cmbPejabat.getSelectionIndex()).getIdPejabatNIP()));
					if (btnplt.getSelection()==true){
						ReportAction.start("DaftarSKSKPDPLT.rptdesign");
					}
					else{
						ReportAction.start("DaftarSKSKPD.rptdesign");
					}
					
			}else
				MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Anda tidak memiliki hak untuk mencetak.");
			}
		});
		btnCetakLampiranSK.setText("Cetak Lampiran SK");
		btnCetakLampiranSK.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BORDER));
		btnCetakLampiranSK.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		btnCetakNota = new Button(composite_1, SWT.NONE);
		btnCetakNota.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(isPrint()){
					if (cmbPejabat.getText().equalsIgnoreCase("")){
						MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Silahkan isi Penanda Tangan");
						cmbPejabat.forceFocus();
					}else{
						try {
							ResultSet result = ControllerFactory.getMainController().getCpSptpdDAOImpl().getNotaSKPD(cmbPajak.getData(Integer.toString(cmbPajak.getSelectionIndex())).toString(), masaPajakDari, masaPajakSampai);
						
							ReportAppContext.name = "Variable";
							String tahun = String.valueOf(dateNow.getYear() + 1900);
							String noUrut = "No. Urut SKPD";
//							String nomor = tbl_SKPD.getItem(0).getText(1).split("/")[0] + " s/d " + tbl_SKPD.getItem(tbl_SKPD.getItemCount() - 2).getText(1).split("/")[0];
//							String jlhWP = String.valueOf(tbl_SKPD.getItemCount() - 1);
//							String jlhPajak = tbl_SKPD.getItem(tbl_SKPD.getItemCount() - 1).getText(14);
							String point2 = "";
							
							String perihal = "Konsep Naskah Surat Keputusan Kepala Badan Pengelola Pajak dan Retribusi Daerah Kota Medan Tentang " +
											"Surat Ketetapan Pajak Daerah (SKPD) Pajak " + cmbPajak.getText().substring(0, 1) + cmbPajak.getText().substring(1).toLowerCase() + " Official Assesment";
							point2 = "Surat Ketetapan Pajak Daerah (SKPD) tersebut diatas, diterbitkan berdasarkan hasil pendataan Wajib Pajak " +
									"oleh petugas wilayah. Surat Pemberitahuan Pajak Daerah (SPTPD) dan perhitungan Pajak Daerah oleh Pejabat yang " +
									"berwenang, maka jika Bapak sependapat, mohon kiranya Bapak dapat menetapkan/menandatangani naskah " +
									"Surat Keputusan yang telah kami persiapkan sebagaimana terlampir";
							
							ReportAppContext.map.put("tahun", tahun);
							ReportAppContext.map.put("noUrut", noUrut);
							ReportAppContext.map.put("Bidang", "Parkir, Reklame, Penerangan Jalan, Air Tanah, Sarang Burung Walet, dan Retribusi");
//							ReportAppContext.map.put("jlhWP", jlhWP);
//							ReportAppContext.map.put("jlhPajak", jlhPajak);
							ReportAppContext.map.put("point2", point2);
							ReportAppContext.map.put("perihal", perihal);
							ReportAppContext.map.put("penandatangan", String.valueOf(listPejabat.get(cmbPejabat.getSelectionIndex()).getNamaPejabat()));
							ReportAppContext.map.put("jabatan", String.valueOf(listPejabat.get(cmbPejabat.getSelectionIndex()).getPangkat()));
							ReportAppContext.map.put("nip", String.valueOf(listPejabat.get(cmbPejabat.getSelectionIndex()).getIdPejabatNIP()));
							
							ReportAppContext.nameObject = "NotaSKPD";
							ReportAppContext.classLoader = DaftarSKPDView.class.getClassLoader();
							if(!ReportAppContext.object.isEmpty())
								ReportAppContext.object.clear();
							
							int j=0;
							while (result.next()){
								String noUrutSKPD = "";
								List<String> values = new ArrayList<String>();
//								values.add(String.valueOf(j + 1));
								if (result.getString("minNo").equalsIgnoreCase(result.getString("maxNo")))
									noUrutSKPD = result.getString("minNo");
								else
									noUrutSKPD = result.getString("minNo") + " s/d " + result.getString("maxNo");
								values.add(noUrutSKPD);
								values.add(result.getString("bidangUsaha"));
								values.add(result.getString("jumlahWP"));
								values.add(indFormat.format(result.getDouble("jumlahPajak")).substring(2).replace(".", "").replace(",", "."));
								ReportAppContext.object.put(Integer.toString(j), values);
								j++;
							}
							
							if (btnplt.getSelection()==true){
								ReportAction.start("NotaDinasSKPDPLTSub2.rptdesign");
							}
							else{
								ReportAction.start("NotaDinasSKPDSub2.rptdesign");
							}
						} catch (ParseException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}else
					MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Anda tidak memiliki hak untuk mencetak.");
			}
		});
		btnCetakNota.setText("Cetak Nota Dinas");
		btnCetakNota.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BORDER));
		btnCetakNota.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		
		tbl_SKPD = new Table(comp, SWT.BORDER | SWT.FULL_SELECTION | SWT.VIRTUAL);
		tbl_SKPD.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		tbl_SKPD.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 11, 1));
		tbl_SKPD.setForeground(SWTResourceManager.getColor(0, 0, 0));
		tbl_SKPD.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE-1, SWT.NORMAL));
		tbl_SKPD.setHeaderVisible(true);
		tbl_SKPD.setLinesVisible(true);
//		new Label(comp, SWT.NONE);
//		new Label(comp, SWT.NONE);
//		new Label(comp, SWT.NONE);
//		new Label(comp, SWT.NONE);

		scrolledComposite.setContent(comp);
		scrolledComposite.setMinSize(comp.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}
	
	@SuppressWarnings("deprecation")
	public void setMasaPajak()
	{
		
		if (btnTanggal.getSelection())
		{
			Calendar instance = Calendar.getInstance();
			instance.set(Calendar.DAY_OF_MONTH, calSPTPD.getDay());
			instance.set(Calendar.MONTH, calSPTPD.getMonth());
			instance.set(Calendar.YEAR, calSPTPD.getYear());
			masaPajakDari = new Date(calSPTPD.getYear() - 1900, calSPTPD.getMonth(), calSPTPD.getDay());
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
			instance.set(Calendar.DAY_OF_MONTH, calSPTPD.getDay());
			instance.set(Calendar.MONTH, calSPTPD.getMonth());
			instance.set(Calendar.YEAR, calSPTPD.getYear());
			masaPajakDari = new Date(calSPTPD.getYear() - 1900, calSPTPD.getMonth(), calSPTPD.getDay());
			masaPajakSampai = new Date(calSampai.getYear() - 1900, calSampai.getMonth(), calSampai.getDay());
			masaPajak = masaPajakDari.getDate() + " " + UIController.INSTANCE.formatMonth(masaPajakDari.getMonth() + 1, Locale.getDefault()) + " " + (masaPajakDari.getYear() + 1900);
		}
	}
	
	private void deleteColumns(){
		for(TableColumn col : tbl_SKPD.getColumns()){
			col.dispose();
		}
	}
	
	private void createColumn()
	{
		if(tbl_SKPD.getColumnCount() <= 0){
			String[] listColumn = {"No.","No SPTPD", "Tanggal SPTPD", "Tahun", "Nama Badan", "Alamat Badan", "NPWPD", "Kode Bidang Usaha", "Nama Pajak", "Dasar Pengenaan", "Pajak Terutang",
					"Bunga", "Kenaikan", "Denda", "Total", "Id Sub Pajak", "Masa Pajak Dari", "Masa Pajak Sampai", "Jatuh Tempo", "Masa Pajak", "Lokasi"};
			for (int i=listColumn.length - 1;i>=0;i--)
			{
				TableColumn colPajak = new TableColumn(tbl_SKPD, SWT.NONE,0);
				colPajak.setText(listColumn[i]);
				if (i==15)
					colPajak.setWidth(0);
				else if(i > 0)
					colPajak.setWidth(120);
				else
					colPajak.setWidth(50);
			}
		}
	}
	
	private void createColumnSSPD()
	{
		if(tbl_SKPD.getColumnCount() <= 0){
			String[] listColumn = {"No.","No SPTPD", "Tanggal SPTPD", "Tahun", "Nama Badan", "Alamat Badan", "NPWPD", "Kode Bidang Usaha", "Nama Pajak", "Dasar Pengenaan", "Pajak Terutang",
					"Bunga", "Kenaikan", "Denda", "Total", "Id Sub Pajak", "Masa Pajak Dari", "Masa Pajak Sampai", "Jatuh Tempo", "Masa Pajak", "Lokasi", 
					"Jumlah Bayar", "Denda", "Tgl SSPD", "Tgl Cetak", "No SSPD"};
			for (int i=listColumn.length - 1;i>=0;i--)
			{
				TableColumn colPajak = new TableColumn(tbl_SKPD, SWT.NONE,0);
				colPajak.setText(listColumn[i]);
				if (i==15)
					colPajak.setWidth(0);
				else if(i > 0)
					colPajak.setWidth(120);
				else
					colPajak.setWidth(50);
			}
		}
	}
	
	private void loadTableSPTPD(final ResultSet result)
	{
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		String masaPajak = "";
		String temp[];
		double grandTotal = 0;
		double totalBayar = 0;
		double totalDenda = 0;
		int count = 1;
		try
		{
			while (result.next())
			{
				if (result.getString("MasaPajak").contains(" - ")){
					temp = result.getString("MasaPajak").split("-");
//					tempDate[0] = Date.parse(temp[0]);
					masaPajak = temp[2] + " " + UIController.INSTANCE.formatMonth(Integer.parseInt(temp[1]), Locale.getDefault()).substring(0, 3) + " " + temp[0] + " - ";
					masaPajak += temp[5] + " " + UIController.INSTANCE.formatMonth(Integer.parseInt(temp[4]), Locale.getDefault()).substring(0, 3) + " " + temp[3];
				}
				else{
					temp = result.getString("MasaPajak").split("-");
					masaPajak = UIController.INSTANCE.formatMonth(Integer.parseInt(temp[1]), Locale.getDefault()) + " " + temp[0];
				}
				TableItem item = new TableItem(tbl_SKPD, SWT.NONE);
				item.setText(0, count+".");
				item.setText(1, result.getString("No_SPTPD"));
				item.setText(2, formatter.format(result.getDate("Tgl_SPTPD")));
				item.setText(3, result.getString("Tahun"));
				item.setText(4, result.getString("Nama_Badan"));
				item.setText(5, result.getString("Alabad_Jalan"));
				item.setText(6, result.getString("NPWPD"));
				item.setText(7, result.getString("Kode_Bid_Usaha"));
				item.setText(8, result.getString("NamaPajak"));
				item.setText(9, indFormat.format(result.getDouble("Dasar_Pengenaan"))); 
				item.setText(10, indFormat.format(result.getDouble("Pajak_Terutang")));
				item.setText(11, indFormat.format(result.getDouble("Bunga")));
				item.setText(12, indFormat.format(result.getDouble("Kenaikan")));
				item.setText(13, indFormat.format(result.getDouble("Denda")));
				item.setText(14, indFormat.format(result.getDouble("Total")));
				item.setText(15, result.getString("IdSub_Pajak"));
				item.setText(16, formatter.format(result.getDate("Masa_pajak_Dari")));
				item.setText(17, formatter.format(result.getDate("Masa_Pajak_Sampai")));
				item.setText(18, formatter.format(result.getDate("TanggalJatuhTempo")));
				item.setText(19, masaPajak);
				item.setText(20, result.getString("Lokasi"));
				if (chkSSPD.getSelection()){
					item.setText(21, indFormat.format(result.getDouble("Jumlah_Bayar")));
					item.setText(22, indFormat.format(result.getDouble("DendaSSPD")));
					try{
						item.setText(23, formatter.format(result.getDate("Tgl_SSPD")));
						item.setText(24, formatter.format(result.getDate("Tgl_Cetak")));
						item.setText(25, result.getString("No_SSPD"));
						totalBayar += indFormat.parse(item.getText(21)).doubleValue();
						totalDenda += indFormat.parse(item.getText(22)).doubleValue();
					}catch(NullPointerException npe){
//						npe.printStackTrace();
					}catch(ParseException e){
						
					}
				}
				try {
					grandTotal += indFormat.parse(item.getText(14)).doubleValue();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				count++;
			}
			TableItem item = new TableItem(tbl_SKPD, SWT.NONE);
			item.setText(1, "Grand Total");
			item.setText(14, indFormat.format(grandTotal));
			item.setText(21, indFormat.format(totalBayar));
			item.setText(22, indFormat.format(totalDenda));
		} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		}
	}
	
	private void loadPejabat(){
		listPejabat = ControllerFactory.getMainController().getCpPejabatDAOImpl().getAllPejabat();
		for (int i=0;i<listPejabat.size();i++){
			UIController.INSTANCE.loadPejabat(cmbPejabat, listPejabat.toArray());
			UIController.INSTANCE.loadPejabat(cmbPendaftaran, listPejabat.toArray());
			UIController.INSTANCE.loadPejabat(cmbPenetapan, listPejabat.toArray());
			UIController.INSTANCE.loadPejabat(cmbKasiPendaftaran, listPejabat.toArray());
			UIController.INSTANCE.loadPejabat(cmbKasiPenetapan, listPejabat.toArray());
		}
	}
	
	private String getNoUrutFormat(String noSKP){
		int idx = noSKP.indexOf("/");
		String noUrut = noSKP.substring(0, idx);
		noUrut = ("000000" + noUrut).substring(noUrut.length());
		String retValue = "";
		for (int i=0;i<noUrut.length();i++) {
			retValue += noUrut.substring(i, i+1) + "   ";
		}
		
		return retValue;
	}
	
//	private String GetNoRekFormat(String NoRek){
//		String noRekening = NoRek.replace(".", "");
//		String retValue = "";
//		for (int i=0;i<noRekening.length();i++) {
//			retValue += noRekening.substring(i, i+1) + "   ";
//		}
//		
//		return retValue;
//	}
	
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

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}
	
	private boolean isPrint(){		
		return userModul.getCetak();
	}
	
	private boolean isValid(){
		Control[] children = grpPenandatangan.getChildren();
		for (int i=0;i<children.length;i++)
		{
			if (children[i] instanceof Combo)
			{
				Combo child = (Combo) children[i];
				
				if (child.getText().equalsIgnoreCase("") && child.getVisible())
				{
					child.forceFocus();
					return false;
				}
			}
			
			if (cmbPejabat.getText().equalsIgnoreCase("")){
				cmbPejabat.forceFocus();
				return false;
			}
		}
		return true;
	}
}