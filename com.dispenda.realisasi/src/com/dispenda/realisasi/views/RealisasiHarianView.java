package com.dispenda.realisasi.views;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Array;
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

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
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
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.SWTResourceManager;

import com.dispenda.controller.ControllerFactory;
import com.dispenda.controller.UIController;
import com.dispenda.model.BidangUsahaProvider;
import com.dispenda.model.PajakProvider;
import com.dispenda.model.UserModul;
import com.dispenda.object.GlobalVariable;
import com.dispenda.object.Preferences;
import com.dispenda.report.ReportAction;
import com.dispenda.report.ReportAppContext;

import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Spinner;

public class RealisasiHarianView extends ViewPart {
	public RealisasiHarianView() {
	}
	
	public static final String ID = RealisasiHarianView.class.getName();
	private Text txtPokokPajak;
	private Text txtDenda;
	private Text txtTotal;
	private Table tblPendapatan;
	private Color fontColor = new Color(Display.getCurrent(),Preferences.FONT_COLOR);
	private Timestamp dateNow;
	private Locale ind = new Locale("id", "ID");
	private Button btnTahun;
	private Button btnBulan;
	private Button btnTanggal;
	private DateTime calSKP;
	private Spinner yearSKP;
	private DateTime monthSKP;
	private Date masaPajakDari;
	private Date masaPajakSampai;
	private String masaPajak;
	private NumberFormat indFormat = NumberFormat.getCurrencyInstance(ind);
	private Combo cmbKewajibanPajak;
	private UserModul userModul = ControllerFactory.getMainController().getCpUserDAOImpl()
			.getHMUserModul(GlobalVariable.userModul.getIdUser()).get(33);
	private DateTime calSampai;
	private Label lblDash;
	SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-YYYY");
	private Text txtDendaSSPD;
	private int idSubPajak;
	private Combo cmbSubPajak;
	private Combo cmbBayar;
	private Text txtSPTNow;
	private Text txtSPTOld;
	private Text txtSKPDKB;
	private Text txtTotalReal;
	private Text txtSPTNowDenda;
	private Text txtSPTOldDenda;
	private Text txtSKPDKBDenda;
	private Text txtTotalDenda;
	private Text txtSPTNowTotal;
	private Text txtSPTOldTotal;
	private Text txtSKPDKBTotal;
	private Text txtTotalSemua;
	private Group grpRealisasi;

	@SuppressWarnings("deprecation")
	@Override
	public void createPartControl(Composite parent) {
		Locale.setDefault(new Locale("id","ID"));
		dateNow = (java.sql.Timestamp)ControllerFactory.getMainController().getCpWajibPajakDAOImpl().getDateNow();
		
		parent.setLayout(new GridLayout(1, false));
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(11, false));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Group grpDataLaporan = new Group(composite, SWT.NONE);
		grpDataLaporan.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 10, 1));
		grpDataLaporan.setLayout(new GridLayout(7, false));
		grpDataLaporan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.BOLD));
		grpDataLaporan.addPaintListener(new PaintListener() {
			@Override
			public void paintControl(PaintEvent pe) {
				pe.gc.setForeground(fontColor);
				pe.gc.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.BOLD));
				pe.gc.drawText("Data Laporan Realisasi Penerimaan", 5, 0, false);
			}
		});
		
		Label lblKewajibanPajak = new Label(grpDataLaporan, SWT.NONE);
		lblKewajibanPajak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblKewajibanPajak.setForeground(fontColor);
		lblKewajibanPajak.setText("Kewajiban Pajak");
		
		cmbKewajibanPajak = new Combo(grpDataLaporan, SWT.READ_ONLY);
		cmbKewajibanPajak.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				idSubPajak = 0;
				UIController.INSTANCE.loadBidangUsaha(cmbSubPajak, BidangUsahaProvider.INSTANCE.getBidangUsaha((Integer)cmbKewajibanPajak.getData(Integer.toString(cmbKewajibanPajak.getSelectionIndex()))).toArray());
			}
		});
		cmbKewajibanPajak.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 3, 1));
		cmbKewajibanPajak.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		cmbKewajibanPajak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		UIController.INSTANCE.loadPajak(cmbKewajibanPajak, PajakProvider.INSTANCE.getPajak().toArray());
		
		Label label_1 = new Label(grpDataLaporan, SWT.NONE);
		label_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label_1.setText("-");
		
		cmbSubPajak = new Combo(grpDataLaporan, SWT.READ_ONLY);
		cmbSubPajak.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				idSubPajak = (Integer)cmbSubPajak.getData(Integer.toString(cmbSubPajak.getSelectionIndex()));
			}
		});
		cmbSubPajak.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		cmbSubPajak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		GridData gd_cmbSubPajak = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
		gd_cmbSubPajak.widthHint = 242;
		cmbSubPajak.setLayoutData(gd_cmbSubPajak);
		
		Label lblPembayaran = new Label(grpDataLaporan, SWT.NONE);
		lblPembayaran.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblPembayaran.setForeground(fontColor);
		lblPembayaran.setText("Pembayaran");
		
		cmbBayar = new Combo(grpDataLaporan, SWT.READ_ONLY);
		cmbBayar.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		cmbBayar.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		cmbBayar.setItems(new String[] {"Tunai", "Giro"});
		GridData gd_cmnBayar = new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1);
		gd_cmnBayar.widthHint = 121;
		cmbBayar.setLayoutData(gd_cmnBayar);
		new Label(grpDataLaporan, SWT.NONE);
		new Label(grpDataLaporan, SWT.NONE);
		
		grpRealisasi = new Group(grpDataLaporan, SWT.NONE);
		grpRealisasi.setLayout(new GridLayout(4, false));
		grpRealisasi.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		GridData gd_grpRealisasi = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 3);
		gd_grpRealisasi.heightHint = 152;
		gd_grpRealisasi.widthHint = 717;
		grpRealisasi.setLayoutData(gd_grpRealisasi);
		grpRealisasi.setText("Realisasi");
		new Label(grpRealisasi, SWT.NONE);
		
		Label lblPokok = new Label(grpRealisasi, SWT.NONE);
		lblPokok.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		lblPokok.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblPokok.setForeground(fontColor);
		lblPokok.setText("Pokok");
		
		Label lblDenda_2 = new Label(grpRealisasi, SWT.NONE);
		lblDenda_2.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		lblDenda_2.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblDenda_2.setForeground(fontColor);
		lblDenda_2.setText("Denda");
		
		Label lblTotal_2 = new Label(grpRealisasi, SWT.NONE);
		lblTotal_2.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		lblTotal_2.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblTotal_2.setForeground(fontColor);
		lblTotal_2.setText("Total");
		
		Label lblSptpdTahunBerjalan = new Label(grpRealisasi, SWT.NONE);
		lblSptpdTahunBerjalan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblSptpdTahunBerjalan.setForeground(fontColor);
		lblSptpdTahunBerjalan.setText("SPTPD Berjalan");
		
		txtSPTNow = new Text(grpRealisasi, SWT.BORDER | SWT.RIGHT);
		txtSPTNow.setEditable(false);
		txtSPTNow.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtSPTNow.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtSPTNow.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		txtSPTNowDenda = new Text(grpRealisasi, SWT.BORDER | SWT.RIGHT);
		txtSPTNowDenda.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtSPTNowDenda.setEditable(false);
		txtSPTNowDenda.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtSPTNowDenda.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		txtSPTNowTotal = new Text(grpRealisasi, SWT.BORDER | SWT.RIGHT);
		txtSPTNowTotal.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtSPTNowTotal.setEditable(false);
		txtSPTNowTotal.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtSPTNowTotal.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblSptpdTunggakan = new Label(grpRealisasi, SWT.NONE);
		lblSptpdTunggakan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblSptpdTunggakan.setForeground(fontColor);
		lblSptpdTunggakan.setText("SPTPD Tunggakan");
		
		txtSPTOld = new Text(grpRealisasi, SWT.BORDER | SWT.RIGHT);
		txtSPTOld.setEditable(false);
		txtSPTOld.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtSPTOld.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtSPTOld.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		txtSPTOldDenda = new Text(grpRealisasi, SWT.BORDER | SWT.RIGHT);
		txtSPTOldDenda.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtSPTOldDenda.setEditable(false);
		txtSPTOldDenda.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtSPTOldDenda.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		
		txtSPTOldTotal = new Text(grpRealisasi, SWT.BORDER | SWT.RIGHT);
		txtSPTOldTotal.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtSPTOldTotal.setEditable(false);
		txtSPTOldTotal.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtSPTOldTotal.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblSkpdkb = new Label(grpRealisasi, SWT.NONE);
		lblSkpdkb.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblSkpdkb.setForeground(fontColor);
		lblSkpdkb.setText("SKPDKB");
		
		txtSKPDKB = new Text(grpRealisasi, SWT.BORDER | SWT.RIGHT);
		txtSKPDKB.setEditable(false);
		txtSKPDKB.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtSKPDKB.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtSKPDKB.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		txtSKPDKBDenda = new Text(grpRealisasi, SWT.BORDER | SWT.RIGHT);
		txtSKPDKBDenda.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtSKPDKBDenda.setEditable(false);
		txtSKPDKBDenda.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtSKPDKBDenda.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		txtSKPDKBTotal = new Text(grpRealisasi, SWT.BORDER | SWT.RIGHT);
		txtSKPDKBTotal.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtSKPDKBTotal.setEditable(false);
		txtSKPDKBTotal.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtSKPDKBTotal.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblTotal_1 = new Label(grpRealisasi, SWT.NONE);
		lblTotal_1.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblTotal_1.setForeground(fontColor);
		lblTotal_1.setText("TOTAL");
		
		txtTotalReal = new Text(grpRealisasi, SWT.BORDER | SWT.RIGHT);
		txtTotalReal.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtTotalReal.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtTotalReal.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		txtTotalDenda = new Text(grpRealisasi, SWT.BORDER | SWT.RIGHT);
		txtTotalDenda.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtTotalDenda.setEditable(false);
		txtTotalDenda.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtTotalDenda.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		txtTotalSemua = new Text(grpRealisasi, SWT.BORDER | SWT.RIGHT);
		txtTotalSemua.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtTotalSemua.setEditable(false);
		txtTotalSemua.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtTotalSemua.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblTanggalPenerimaan = new Label(grpDataLaporan, SWT.NONE);
		lblTanggalPenerimaan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblTanggalPenerimaan.setText("Tanggal Penerimaan");
		lblTanggalPenerimaan.setForeground(fontColor);
		
		Group group = new Group(grpDataLaporan, SWT.NONE);
		GridData gd_group = new GridData(SWT.LEFT, SWT.CENTER, false, false, 5, 1);
		gd_group.widthHint = 321;
		gd_group.heightHint = 65;
		group.setLayoutData(gd_group);
		group.setLayout(new GridLayout(5, false));
		
		btnTanggal = new Button(group, SWT.RADIO);
		btnTanggal.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				calSKP.setVisible(true);
				calSampai.setVisible(true);
				lblDash.setVisible(true);
				monthSKP.setVisible(false);
				yearSKP.setVisible(false);
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
				calSKP.setVisible(false);
				calSampai.setVisible(false);
				lblDash.setVisible(false);
				monthSKP.setVisible(true);
				yearSKP.setVisible(false);
				setMasaPajak();
			}
		});
		btnBulan.setText("Bulan");
		btnBulan.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BORDER));
		btnBulan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		btnTahun = new Button(group, SWT.RADIO);
		btnTahun.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1));
		btnTahun.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				calSKP.setVisible(false);
				calSampai.setVisible(false);
				lblDash.setVisible(false);
				monthSKP.setVisible(false);
				yearSKP.setVisible(true);
				setMasaPajak();
			}
		});
		btnTahun.setText("Tahun");
		btnTahun.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BORDER));
		btnTahun.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		Composite composite_1 = new Composite(group, SWT.NONE);
		composite_1.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1));
		composite_1.setLayout(new StackLayout());
		
		lblDash = new Label(group, SWT.NONE);
		lblDash.setText("-");
		lblDash.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblDash.setVisible(false);
		
		Composite composite_3 = new Composite(group, SWT.NONE);
		composite_3.setLayout(new StackLayout());
		
		calSampai = new DateTime(composite_3, SWT.BORDER | SWT.DROP_DOWN);
		calSampai.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setMasaPajak();
				if (masaPajakSampai.before(masaPajakDari)){
					MessageDialog.openError(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Tanggal masa dari (" + sdf.format(masaPajakDari) + ") harus lebih kecil dari masa sampai (" + sdf.format(masaPajakSampai) + ")");
					calSampai.setDate(calSKP.getYear(), calSKP.getMonth(), calSKP.getDay());
					setMasaPajak();
				}
			}
		});
		calSampai.setBounds(-28, 0, 113, 24);
		calSampai.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		calSampai.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BORDER));
		calSampai.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		calSKP = new DateTime(composite_1, SWT.BORDER | SWT.DROP_DOWN);
		calSKP.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		calSKP.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				calSampai.setDate(calSKP.getYear(), calSKP.getMonth(), calSKP.getDay());
				setMasaPajak();
			}
		});
		calSKP.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BORDER));
		calSKP.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		yearSKP = new Spinner(composite_1, SWT.BORDER);
		yearSKP.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		yearSKP.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setMasaPajak();
			}
		});
		yearSKP.setTextLimit(9999);
		yearSKP.setMaximum(dateNow.getYear() + 1900);
		yearSKP.setMinimum(2006);
		yearSKP.setSelection(dateNow.getYear() + 1900);
		yearSKP.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		yearSKP.setVisible(false);
		yearSKP.setValues(dateNow.getYear() + 1900, 2006, dateNow.getYear() + 1900, 0, 1, 10);
		yearSKP.pack(true);
		
		monthSKP = new DateTime(composite_1, SWT.BORDER | SWT.DROP_DOWN | SWT.SHORT);
		monthSKP.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		monthSKP.setDay(1);
		monthSKP.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setMasaPajak();
			}
		});
		monthSKP.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		monthSKP.setVisible(false);
		new Label(group, SWT.NONE);
		new Label(group, SWT.NONE);
		new Label(group, SWT.NONE);
		new Label(group, SWT.NONE);
		new Label(group, SWT.NONE);
		new Label(grpDataLaporan, SWT.NONE);
		
		Button btnTampil = new Button(grpDataLaporan, SWT.NONE);
		btnTampil.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ResultSet result = null;
				tblPendapatan.removeAll();
				
//				if (cmbSubPajak.getText().equalsIgnoreCase("")){
//					
//				}else{
					if (cmbKewajibanPajak.getText().equalsIgnoreCase(""))
						result = ControllerFactory.getMainController().getCpSspdDAOImpl().getDaftarRealisasiHarianNew("", idSubPajak, masaPajakDari, masaPajakSampai, cmbBayar.getText());
					else
						result = ControllerFactory.getMainController().getCpSspdDAOImpl().getDaftarRealisasiHarianNew(cmbKewajibanPajak.getData(Integer.toString(cmbKewajibanPajak.getSelectionIndex())).toString(), idSubPajak, masaPajakDari, masaPajakSampai, cmbBayar.getText());
//				}
				createColumn();
				loadTableSSPD(result);
				
				if (masaPajakDari.getMonth() == masaPajakSampai.getMonth() && masaPajakDari.getYear() == masaPajakSampai.getYear() && cmbKewajibanPajak.getSelectionIndex() > -1){
					Double[] realisasi = new Double[6];
					try {
						realisasi = ControllerFactory.getMainController().getCpSspdDAOImpl().GetRealisasiBerjalan(cmbKewajibanPajak.getData(Integer.toString(cmbKewajibanPajak.getSelectionIndex())).toString(), masaPajakDari, masaPajakSampai);
						txtSPTNow.setText(indFormat.format(realisasi[0]));
						txtSPTNowDenda.setText(indFormat.format(realisasi[1]-realisasi[0]));
						txtSPTNowTotal.setText(indFormat.format(realisasi[1]));
						txtSPTOld.setText(indFormat.format(realisasi[2]));
						txtSPTOldDenda.setText(indFormat.format(realisasi[3]-realisasi[2]));
						txtSPTOldTotal.setText(indFormat.format(realisasi[3]));
						txtSKPDKB.setText(indFormat.format(realisasi[4]));
						txtSKPDKBDenda.setText(indFormat.format(realisasi[5]-realisasi[4]));
						txtSKPDKBTotal.setText(indFormat.format(realisasi[5]));
						Double total = 0.0;
						Double totalPokok = 0.0;
						Double totalDenda = 0.0;
						int i=0;
						for (double element : realisasi) {
							if (i % 2 == 0)
								totalPokok += element;
							else
								total += element;
//					        totalDenda += element;
					        i++;
					    }
						totalDenda = total - totalPokok;
						txtTotalReal.setText(indFormat.format(totalPokok));
						txtTotalDenda.setText(indFormat.format(totalDenda));
						txtTotalSemua.setText(indFormat.format(total));
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}else{
					Text child = null;
					for (Control txt : grpRealisasi.getChildren()){
						if (txt instanceof Text){
							child = (Text) txt;
							child.setText("");
						}
						
					}
				}
			}
		});
		GridData gd_btnTampil = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_btnTampil.widthHint = 90;
		btnTampil.setLayoutData(gd_btnTampil);
		btnTampil.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnTampil.setText("Tampil");
		
		Button btnBaru = new Button(grpDataLaporan, SWT.NONE);
		btnBaru.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				tblPendapatan.removeAll();
				txtPokokPajak.setText("");
				txtDenda.setText("");
				txtDendaSSPD.setText("");
				txtTotal.setText("");
				cmbKewajibanPajak.deselectAll();
				cmbSubPajak.deselectAll();
				cmbBayar.deselectAll();
			}
		});
		GridData gd_btnBaru = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnBaru.widthHint = 90;
		btnBaru.setLayoutData(gd_btnBaru);
		btnBaru.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnBaru.setText("Baru");
		
		Button btnCetak = new Button(grpDataLaporan, SWT.NONE);
		btnCetak.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(isPrint()){
					ReportAppContext.name = "Variable";
					ReportAppContext.classLoader = RealisasiHarianView.class.getClassLoader();
					if(!ReportAppContext.object.isEmpty())
						ReportAppContext.object.clear();
					SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
					Date tgl = new Date(calSKP.getYear() - 1900, calSKP.getMonth(), calSKP.getDay());
					Integer count = 0;
					if (!cmbKewajibanPajak.getText().equalsIgnoreCase("")){
						for(int ti=0;ti<tblPendapatan.getItems().length; ti++){
							List<String> values = new ArrayList<String>();
							values.add(tblPendapatan.getItem(ti).getText(3));
							values.add(tblPendapatan.getItem(ti).getText(11).substring(2).replace(".", "").replace(",", "."));
							values.add(tblPendapatan.getItem(ti).getText(12).substring(2).replace(".", "").replace(",", "."));
							values.add(tblPendapatan.getItem(ti).getText(13).substring(2).replace(".", "").replace(",", "."));
							values.add(tblPendapatan.getItem(ti).getText(7));
							ReportAppContext.object.put(count.toString(), values);
							count++;
						}
						if (cmbSubPajak.getText().equalsIgnoreCase(""))
							ReportAppContext.map.put("NamaPajak", cmbKewajibanPajak.getText());
						else
							ReportAppContext.map.put("NamaPajak", cmbSubPajak.getText());
						ReportAppContext.map.put("Total", "Pokok + Denda = " + txtTotal.getText());
						ReportAppContext.map.put("Header", "Pokok Pajak");
					}
					else{
						for(int ti=0;ti<tblPendapatan.getItems().length; ti++){
							List<String> values = new ArrayList<String>();
							values.add(tblPendapatan.getItem(ti).getText(3));
							values.add(tblPendapatan.getItem(ti).getText(14).substring(2).replace(".", "").replace(",", "."));
							values.add(tblPendapatan.getItem(ti).getText(12).substring(2).replace(".", "").replace(",", "."));
							values.add(tblPendapatan.getItem(ti).getText(13).substring(2).replace(".", "").replace(",", "."));
							values.add(tblPendapatan.getItem(ti).getText(7));
							ReportAppContext.object.put(count.toString(), values);
							count++;
						}
						ReportAppContext.map.put("NamaPajak", "Seluruh Kode Pajak");
						ReportAppContext.map.put("Total", "");
						ReportAppContext.map.put("Header", "Jumlah Bayar");
					}
					ReportAppContext.map.put("Tanggal", sdf.format(tgl));
					ReportAppContext.nameObject = "Realisasi";
					ReportAction.start("RealisasiHarian.rptdesign");
				}else
					MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Anda tidak memiliki hak untuk mencetak.");
			}
		});
		GridData gd_btnCetak = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnCetak.widthHint = 90;
		btnCetak.setLayoutData(gd_btnCetak);
		btnCetak.setText("Cetak");
		btnCetak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		Button btnCetakDaftar = new Button(grpDataLaporan, SWT.NONE);
		btnCetakDaftar.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(isPrint()){
					String bulan = "";
					String masa = "";
					ReportAppContext.name = "Variable";
					ReportAppContext.nameObject = "Realisasi";
					ReportAppContext.classLoader = RealisasiHarianView.class.getClassLoader();
					SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
					Date tgl = new Date(calSKP.getYear() - 1900, calSKP.getMonth(), calSKP.getDay());
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
						masa = String.valueOf(yearSKP.getText());
					
					if(!ReportAppContext.object.isEmpty())
						ReportAppContext.object.clear();
//					Integer count = 0;
					for(int j=0;j<tblPendapatan.getItems().length; j++){
						List<String> values = new ArrayList<String>();
						for(int i=0;i<tblPendapatan.getColumnCount();i++){// untuk masukkan data berdasarkan colom table
							if(i==11||i==12||i==13||i==14)// untuk colom yang berhubungan dengan currency
								values.add(tblPendapatan.getItem(j).getText(i).substring(2).replace(".", "").replace(",", "."));
							else
								values.add(tblPendapatan.getItem(j).getText(i));
						}
						ReportAppContext.object.put(Integer.toString(j), values);
//						count++;
					}
					if (!cmbKewajibanPajak.getText().equalsIgnoreCase("")){
						if (cmbSubPajak.getText().equalsIgnoreCase(""))
							ReportAppContext.map.put("Pajak", cmbKewajibanPajak.getText());
						else
							ReportAppContext.map.put("Pajak", cmbSubPajak.getText());
					}
					else
						ReportAppContext.map.put("Pajak", "Daerah");
					ReportAppContext.map.put("Bulan", bulan);
					ReportAppContext.map.put("Masa", masa);
					
					ReportAction.start("RealisasiHarianLaporan.rptdesign");
				}else
					MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Anda tidak memiliki hak untuk mencetak.");
			}
		});
		GridData gd_btnCetakDaftar = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
		gd_btnCetakDaftar.widthHint = 99;
		btnCetakDaftar.setLayoutData(gd_btnCetakDaftar);
		btnCetakDaftar.setText(" Cetak Daftar");
		btnCetakDaftar.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		new Label(composite, SWT.NONE);
		
		Group grpPendapatan = new Group(composite, SWT.NONE);
		grpPendapatan.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 11, 2));
		grpPendapatan.setLayout(new GridLayout(1, false));
		grpPendapatan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.BOLD));
		
		tblPendapatan = new Table(grpPendapatan, SWT.BORDER | SWT.FULL_SELECTION | SWT.VIRTUAL);
		tblPendapatan.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		tblPendapatan.setForeground(SWTResourceManager.getColor(0, 0, 0));
		tblPendapatan.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		tblPendapatan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE-1, SWT.NORMAL));
		tblPendapatan.setHeaderVisible(true);
		tblPendapatan.setLinesVisible(true);
	
		Label lblPokokPajak = new Label(composite, SWT.NONE);
		lblPokokPajak.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPokokPajak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblPokokPajak.setForeground(fontColor);
		lblPokokPajak.setText("Pokok Pajak");
		
		txtPokokPajak = new Text(composite, SWT.BORDER);
		txtPokokPajak.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		txtPokokPajak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		GridData gd_txtPokokPajak = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_txtPokokPajak.widthHint = 150;
		txtPokokPajak.setLayoutData(gd_txtPokokPajak);
//		txtPokokPajak.setForeground(fontColor);
		txtPokokPajak.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		
		Label lblDenda = new Label(composite, SWT.NONE);
		lblDenda.setForeground(fontColor);
		lblDenda.setText("+");
		
		Label lblDenda_1 = new Label(composite, SWT.NONE);
		lblDenda_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblDenda_1.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblDenda_1.setForeground(fontColor);
		lblDenda_1.setText("Denda");
		
		txtDenda = new Text(composite, SWT.BORDER);
		txtDenda.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		txtDenda.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		GridData gd_txtDenda = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_txtDenda.widthHint = 150;
		txtDenda.setLayoutData(gd_txtDenda);
//		txtDenda.setForeground(fontColor);
		txtDenda.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		
		Label lblDendaSspd = new Label(composite, SWT.NONE);
		lblDendaSspd.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblDendaSspd.setForeground(fontColor);
		lblDendaSspd.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblDendaSspd.setText("+ Denda SSPD");
		
		txtDendaSSPD = new Text(composite, SWT.BORDER);
		GridData gd_txtDendaSSPD = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_txtDendaSSPD.widthHint = 150;
		txtDendaSSPD.setLayoutData(gd_txtDendaSSPD);
		txtDendaSSPD.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtDendaSSPD.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		
		Label label = new Label(composite, SWT.NONE);
		label.setForeground(fontColor);
		label.setText("=");
		
		Label lblTotal = new Label(composite, SWT.NONE);
		lblTotal.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblTotal.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblTotal.setForeground(fontColor);
		txtDenda.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		lblTotal.setText("Total");
		
		txtTotal = new Text(composite, SWT.BORDER);
		txtTotal.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		txtTotal.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		GridData gd_txtTotal = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_txtTotal.widthHint = 150;
		txtTotal.setLayoutData(gd_txtTotal);
//		txtTotal.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		txtTotal.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		new Label(composite, SWT.NONE);
		
//		Label label_2 = new Label(composite, SWT.NONE);

	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}
	
	@SuppressWarnings("deprecation")
	public void setMasaPajak()
	{
		if (btnTanggal.getSelection())
		{
//			Calendar instance = Calendar.getInstance();
//			instance.set(Calendar.DAY_OF_MONTH, calSKP.getDay());
//			instance.set(Calendar.MONTH, calSKP.getMonth());
//			instance.set(Calendar.YEAR, calSKP.getYear());
			masaPajakDari = new Date(calSKP.getYear() - 1900, calSKP.getMonth(), calSKP.getDay());
			masaPajakSampai = new Date(calSampai.getYear() - 1900, calSampai.getMonth(), calSampai.getDay());
			masaPajak = masaPajakDari.getDate() + " " + UIController.INSTANCE.formatMonth(masaPajakDari.getMonth() + 1, Locale.getDefault()) + " " + (masaPajakDari.getYear() + 1900);
			masaPajak += masaPajakSampai.getDate() + " " + UIController.INSTANCE.formatMonth(masaPajakSampai.getMonth() + 1, Locale.getDefault()) + " " + (masaPajakSampai.getYear() + 1900);
		}
		if (btnBulan.getSelection())
		{
			Calendar instance = Calendar.getInstance();
			instance.set(Calendar.DAY_OF_MONTH, monthSKP.getDay());
			instance.set(Calendar.MONTH, monthSKP.getMonth());
			instance.set(Calendar.YEAR, monthSKP.getYear());
			masaPajakDari = new Date(monthSKP.getYear() - 1900, monthSKP.getMonth(), 1);
			masaPajakSampai = new Date(monthSKP.getYear() - 1900, monthSKP.getMonth(), instance.getActualMaximum(Calendar.DATE));
			masaPajak = UIController.INSTANCE.formatMonth(monthSKP.getMonth() + 1, Locale.getDefault()) + " " + monthSKP.getYear();
		}
		if (btnTahun.getSelection())
		{
			masaPajakDari = new Date(yearSKP.getSelection() - 1900, 0, 1);
			masaPajakSampai = new Date(yearSKP.getSelection() - 1900, 11, 31);
			masaPajak = Integer.toString(yearSKP.getSelection());
		}
	
	}
	
	private void createColumn()
	{
		if(tblPendapatan.getColumnCount() <= 0){
//			String[] listColumn = {"No Reg", "NPWPD", "Nama Badan", "Bidang Usaha", "No SKP", "No SSPD", "Bulan SKP", "Cicilan Ke", "Pokok Pajak", 
//					"Denda", "Jumlah Bayar"};
//			int[] widthColumn = {60, 100, 200, 160, 130, 130, 85, 85, 150, 130, 150};
			String[] listColumn = {"Bidang Usaha", "Nama Badan", "Alamat", "NPWPD", "No SKP", "Tanggal SKP", "No SSPD", "No Reg", "STS", "Tgl Cetak", "Masa Pajak", "Pokok Pajak", 
					"Denda", "Denda SSPD", "Jumlah Bayar", "Cicilan Ke"};
			int[] widthColumn = {160, 200, 160, 100, 160, 100, 130, 70, 170, 90, 200, 150, 130, 130, 150, 85};
			for (int i=listColumn.length - 1;i>=0;i--)
			{
				TableColumn colPajak = new TableColumn(tblPendapatan, SWT.NONE,0);
				colPajak.setText(listColumn[i]);
				colPajak.setWidth(widthColumn[i]);
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	private void loadTableSSPD(final ResultSet result)
	{
		String masaPajak = "";
		double totalPokokPajak = 0;
		double totalDenda = 0;
		double totalDendaSSPD = 0;
		double totalJumlahBayar = 0;
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
		try
		{
			while (result.next())
			{
				masaPajak = UIController.INSTANCE.formatMonth(result.getDate("PajakDari").getMonth() + 1, Locale.getDefault()) + " " + (result.getDate("PajakDari").getYear()+ 1900);
				if (! (result.getDate("PajakDari").getMonth() == result.getDate("PajakSampai").getMonth() && result.getDate("PajakDari").getYear() == result.getDate("PajakSampai").getYear()))
					masaPajak += " - " + UIController.INSTANCE.formatMonth(result.getDate("PajakSampai").getMonth() + 1, Locale.getDefault()) + " " + (result.getDate("PajakSampai").getYear() + 1900);
				
				TableItem item = new TableItem(tblPendapatan, SWT.NONE);
				if (result.getString("No_Registrasi") == null)
					item.setText(7, "");
				else
					item.setText(7, result.getString("No_Registrasi"));
				item.setText(0, result.getString("Nama_Bid_Usaha"));
				item.setText(1, result.getString("Nama_Badan"));
				item.setText(2, result.getString("Alabad_Jalan"));
				item.setText(3, result.getString("NPWPD"));
				item.setText(4, result.getString("No_SKP"));
				item.setText(5, formatter.format(result.getDate("Tanggal_SKP")));
				item.setText(6, result.getString("No_SSPD"));
				if (result.getString("STS") == null)
					item.setText(8, "");
				else
					item.setText(8, result.getString("STS"));
				item.setText(9, formatter.format(result.getTimestamp("Tgl_Cetak")));
				item.setText(10, masaPajak);
//				item.setText(11, indFormat.format(roundUP(result.getDouble("PokokPajak"))));
				item.setText(11, indFormat.format(round(result.getDouble("PokokPajak"),2)));
				Double bayar = round(result.getDouble("Jumlah_Bayar"), 2);
//				Double pokok = roundUP(result.getDouble("PokokPajak"));
				Double pokok = round(result.getDouble("PokokPajak"), 2);
				Double denda = round(result.getDouble("Denda"),2);
//				Double denda = roundUP(result.getDouble("Denda"));
//				item.setText(12, indFormat.format(round(bayar - pokok >= denda ? denda :bayar - pokok,2)));
				item.setText(12, indFormat.format(round(bayar - pokok >= denda ? denda : bayar - pokok,2)));
				double dendaSSPD = round(result.getDouble("Jumlah_Bayar"), 2) - round(result.getDouble("PokokPajak"),2) - round(result.getDouble("Denda"),2);
//				double dendaSSPD = round(result.getDouble("Jumlah_Bayar"), 2) - round(result.getDouble("PokokPajak"),2) - round(result.getDouble("Denda"),2);
				item.setText(13, indFormat.format(dendaSSPD >= 0 ? dendaSSPD : 0.0));
				item.setText(14, indFormat.format(round(result.getDouble("Jumlah_Bayar"), 2)));
				item.setText(15, result.getString("Cicilan_Ke"));
//				item.setText(1, result.getString("NPWPD"));
//				item.setText(2, result.getString("Nama_Badan"));
//				item.setText(3, result.getString("Nama_Bid_Usaha"));
//				item.setText(4, result.getString("No_SKP"));
//				item.setText(5, result.getString("No_SSPD"));
//				item.setText(6, result.getString("Bulan_SKP"));
//				item.setText(7, result.getString("Cicilan_Ke"));
//				item.setText(8, indFormat.format(round(result.getDouble("PokokPajak"), 2)));
//				item.setText(9, indFormat.format(round(result.getDouble("Jumlah_Bayar"), 2) - round(result.getDouble("PokokPajak"), 2)));
//				item.setText(10, indFormat.format(round(result.getDouble("Jumlah_Bayar"), 2)));
				try {
					totalPokokPajak += round(indFormat.parse(item.getText(11)).doubleValue(), 2);
					totalDenda += round(indFormat.parse(item.getText(12)).doubleValue(), 2);
					totalDendaSSPD += round(indFormat.parse(item.getText(13)).doubleValue(), 2);
					totalJumlahBayar += round(indFormat.parse(item.getText(14)).doubleValue(), 2);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//System.out.println(item.getText(9));
				for (int i=0;i<=14;i++){
					System.out.print(item.getText(i)+" ");
				}
				System.out.println("");
			}
			TableItem item = new TableItem(tblPendapatan, SWT.NONE);
			item.setText(0, "Total");
			item.setText(11, indFormat.format(totalPokokPajak));
			item.setText(12, indFormat.format(totalDenda));
			item.setText(13, indFormat.format(totalDendaSSPD));
			item.setText(14, indFormat.format(totalJumlahBayar));
			txtPokokPajak.setText(indFormat.format(round(totalPokokPajak, 2)));
			txtDenda.setText(indFormat.format(round(totalDenda, 2)));
			txtDendaSSPD.setText(indFormat.format(round(totalDendaSSPD, 2)));
			txtTotal.setText(indFormat.format(round(totalJumlahBayar, 2)));
			
		} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		}
	}
	
	private boolean isPrint(){		
		return userModul.getCetak();
	}
	
	private static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

//	    BigDecimal bd = new BigDecimal(value);
//	    bd = bd.setScale(places, RoundingMode.HALF_DOWN);
	    
	    DecimalFormat df = new DecimalFormat("#.##");
		df.setMaximumFractionDigits(2);
		df.setRoundingMode(RoundingMode.HALF_EVEN);
	    try {
			return df.parse(df.format(value)).doubleValue();
		} catch (ParseException e) {
			e.printStackTrace();
			return 0.0;
		}
	}
	
	private static double roundUP(double value) {
	    //if (places < 0) throw new IllegalArgumentException();

	    DecimalFormat df = new DecimalFormat("#.##");
		df.setMaximumFractionDigits(0);
		df.setRoundingMode(RoundingMode.UP);
	    try {
			return df.parse(df.format(value)).doubleValue();
		} catch (ParseException e) {
			e.printStackTrace();
			return 0.0;
		}
	}

}
