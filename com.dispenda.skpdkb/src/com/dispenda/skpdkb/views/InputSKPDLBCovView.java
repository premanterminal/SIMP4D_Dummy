package com.dispenda.skpdkb.views;

import java.sql.Date;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.SWTResourceManager;

import com.dispenda.controller.ControllerFactory;
import com.dispenda.controller.UIController;
import com.dispenda.dao.LogImp;
import com.dispenda.model.PendaftaranWajibPajak;
import com.dispenda.model.Periksa;
import com.dispenda.model.PeriksaDetail;
import com.dispenda.model.PeriksaDetailBPK;
import com.dispenda.model.PeriksaDetailBPKProvider;
import com.dispenda.model.PeriksaDetailProvider;
import com.dispenda.model.Sptpd;
import com.dispenda.model.UserModul;
import com.dispenda.model.WpTutup;
import com.dispenda.object.GlobalVariable;
import com.dispenda.object.Preferences;
import com.dispenda.report.ReportAction;
import com.dispenda.report.ReportAppContext;
import com.dispenda.sspd.dialog.CariLokasiDialog;
import com.dispenda.widget.MoneyField;
import com.ibm.icu.util.Currency;
import com.ibm.icu.util.CurrencyAmount;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.nebula.widgets.tablecombo.TableCombo;

public class InputSKPDLBCovView extends ViewPart {
	public InputSKPDLBCovView() {
	}
	
	public static final String ID = InputSKPDLBCovView.class.getName();
	private Text txtNamaBadan;
	private Text txtAlamatBadan;
	private Text txtKodePajak;
	private Text txtNoUrut;
	private Text txtKodeKecamatan;
	private Text txtKodeKelurahan;
	private Table table;
	private Locale ind = new Locale("id", "ID");
	private NumberFormat indFormat = NumberFormat.getCurrencyInstance(ind);
	private java.sql.Timestamp dateNow;
	PendaftaranWajibPajak wp;
	private Composite comp;
	private Integer idPeriksa = null;
	private List<Sptpd> listMasaPajak = new ArrayList<Sptpd>();
	private Label lblDendaSSPD;
	private Button btnPeriksa;
	private Label lblTanggalPenetapan;
	private Button btnSimpan;
	private Color fontColor = new Color(Display.getCurrent(),Preferences.FONT_COLOR);
	
	private void getDataWP()
	{
		Integer npwp = 0;
		try{
			npwp = Integer.parseInt(txtNoUrut.getText().trim());
		}catch(Exception ex){
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "NPWPD tidak valid. Silahkan periksa kembali");
			txtNoUrut.forceFocus();
		}
		wp = ControllerFactory.getMainController().getCpWajibPajakDAOImpl().getNPWPD(npwp);
		if (wp != null)
		{
			resetForm();
			/*TableColumn colPajak = new TableColumn(table, SWT.NONE,0);
			colPajak.setText("Pajak");
			colPajak.setWidth(250);
			TableColumn colNilai = new TableColumn(table, SWT.NONE,1);
			colNilai.setText("Nilai");
			colNilai.setWidth(250);*/
			txtKodePajak.setText(wp.getNPWP().substring(0, 1));
			txtNoUrut.setText(wp.getNPWP().substring(1, 8));
			txtKodeKecamatan.setText(wp.getNPWP().substring(8, 10));
			txtKodeKelurahan.setText(wp.getNPWP().substring(10, 12));
			txtNamaBadan.setText(wp.getNamaPemilik() + " / " + wp.getNamaBadan());
			txtAlamatBadan.setText(wp.getAlabadJalan());
			//idSubPajak = wp.getIdSubPajak();
			//tarif = wp.getTarif();
			txtNoSKP.setText("");
			txtNoSSPD.setText("");	
		}else
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "NPWPD tidak ada. Silahkan periksa kembali");
	}
	
	private void resetForm()
	{
		/*for(TableColumn column : table.getColumns()){
			column.dispose();
		}
		Control[] children = comp.getChildren();
		for (int i=0;i<children.length;i++)
		{
			if (children[i] instanceof Text)
			{
				Text child = (Text) children[i];
				
				if (!child.getText().equalsIgnoreCase(""))
				{
					child.setText("");
				}
			}
			
			else if (children[i] instanceof DateTime)
			{
				DateTime child = (DateTime) children[i];
				child.setDate(dateNow.getYear() + 1900, dateNow.getMonth(), dateNow.getDate());				
			}
			
			else if (children[i] instanceof Combo)
			{
				Combo child = (Combo) children[i];
				child.removeAll();
			}
		}
		//selectedID = null;
		//txtTotalPajakTerhutang.setMoney(new CurrencyAmount(0, Currency.getInstance("ind")));
		table.removeAll();
		//tableAngsuran.removeAll();
		//tableAngsuran.setVisible(false);
		//tblCicilan.removeAll();
		//tblCicilan.setVisible(false);
		//lblSudahDiangsur.setVisible(false);
		//txtTotalAngsur.setMoney(new CurrencyAmount(0, Currency.getInstance("ind")));
		//txtTotalAngsur.setVisible(false);
		//lblSisaPajak.setVisible(false);
		//txtSisa.setMoney(new CurrencyAmount(0, Currency.getInstance("ind")));
		//txtSisa.setVisible(false);
		//txtDibayar.setMoney(new CurrencyAmount(0, Currency.getInstance("ind")));
		//txtSisaDendaDibayar.setMoney(new CurrencyAmount(0, Currency.getInstance("ind")));
		//lblLunas.setVisible(false);
		//txtLokasi.setVisible(false);
		//compPejabat.setVisible(false);*/
		txtNoUrut.setFocus();
		cmbMasaPajak.select(-1);
		cmbMasaPajak.setText("");
		cmbMasaPajak.getTable().removeAll();
	//	btnCetakSts.setVisible(false);
	//	txtNoSTS.setEditable(true);
		//lblAngsuranKe.setVisible(false);
	//	cmbAngsuran.setVisible(false);
	}
	
	@Override
	public void createPartControl(Composite parent) {
		Locale.setDefault(new Locale("id","ID"));
		dateNow = (java.sql.Timestamp)ControllerFactory.getMainController().getCpWajibPajakDAOImpl().getDateNow();
//		parent.setBackground(new Color(Display.getCurrent(), 32, 172, 192));
		
		ScrolledComposite scrolledComposite = new ScrolledComposite(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		
		comp = new Composite(scrolledComposite, SWT.NONE);
		comp.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BORDER));
		comp.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		comp.setLayout(new GridLayout(6, false));
		
		Label lblNpwpd = new Label(comp, SWT.NONE);
		lblNpwpd.setForeground(fontColor);
		lblNpwpd.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblNpwpd.setText("NPWPD");
		
		txtKodePajak = new Text(comp, SWT.BORDER);
		txtKodePajak.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		txtKodePajak.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtKodePajak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtKodePajak.setEditable(false);
		GridData gd_txtKodePajak = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_txtKodePajak.widthHint = 14;
		txtKodePajak.setLayoutData(gd_txtKodePajak);
		
		txtNoUrut = new Text(comp, SWT.BORDER);
		txtNoUrut.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.keyCode == SWT.CR || e.keyCode == SWT.KEYPAD_CR)
				{
					//resetForm();
					txtNoSKPDLB.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
					txtNoSKP.setText("");
					txtNoSSPD.setText("");
					txtDendaSSPD.setText("");
					txtNoSKPDLB.setText("");
					cmbMasaPajak.select(-1);
					cmbMasaPajak.setText("");
					
					getDataWP();
					listMasaPajak = ControllerFactory.getMainController().getCpSptpdDAOImpl().getMasaPajakSPTPD(wp.getNPWP());
					listMasaPajak.addAll(ControllerFactory.getMainController().getCpSptpdDAOImpl().getMasaPajakSKPDKB(wp.getNPWP()));
					
					Collections.sort(listMasaPajak);
					
					if (listMasaPajak.size() > 0)
					{
						UIController.INSTANCE.loadTMasaPajak(cmbMasaPajak, listMasaPajak.toArray()); //PajakProvider.INSTANCE.getPajak().toArray());
						
					}
			}}
		});
				
		txtNoUrut.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		txtNoUrut.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtNoUrut.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		GridData gd_txtNoUrut = new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1);
		gd_txtNoUrut.widthHint = 83;
		txtNoUrut.setLayoutData(gd_txtNoUrut);
		
		txtKodeKecamatan = new Text(comp, SWT.BORDER);
		txtKodeKecamatan.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		txtKodeKecamatan.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtKodeKecamatan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtKodeKecamatan.setEditable(false);
		GridData gd_txtKodeKecamatan = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_txtKodeKecamatan.widthHint = 20;
		txtKodeKecamatan.setLayoutData(gd_txtKodeKecamatan);
		
		txtKodeKelurahan = new Text(comp, SWT.BORDER);
		txtKodeKelurahan.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		txtKodeKelurahan.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtKodeKelurahan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtKodeKelurahan.setEditable(false);
		GridData gd_txtKodeKelurahan = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_txtKodeKelurahan.widthHint = 20;
		txtKodeKelurahan.setLayoutData(gd_txtKodeKelurahan);
		new Label(comp, SWT.NONE);
		
		Label lblNamaBadan = new Label(comp, SWT.NONE);
		lblNamaBadan.setForeground(fontColor);
		lblNamaBadan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblNamaBadan.setText("Nama Badan");
		
		txtNamaBadan = new Text(comp, SWT.BORDER);
		txtNamaBadan.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		txtNamaBadan.setEditable(false);
		txtNamaBadan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtNamaBadan.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		GridData gd_txtNamaBadan = new GridData(SWT.LEFT, SWT.CENTER, false, false, 5, 1);
		gd_txtNamaBadan.widthHint = 291;
		txtNamaBadan.setLayoutData(gd_txtNamaBadan);
		
		Label lblAlamatBadan = new Label(comp, SWT.NONE);
		lblAlamatBadan.setForeground(fontColor);
		lblAlamatBadan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblAlamatBadan.setText("Alamat Badan");
		
		txtAlamatBadan = new Text(comp, SWT.BORDER);
		txtAlamatBadan.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		txtAlamatBadan.setEditable(false);
		txtAlamatBadan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtAlamatBadan.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		GridData gd_txtAlamatBadan = new GridData(SWT.LEFT, SWT.CENTER, false, false, 5, 1);
		gd_txtAlamatBadan.widthHint = 291;
		txtAlamatBadan.setLayoutData(gd_txtAlamatBadan);
		
		Label lblMasaPajak = new Label(comp, SWT.NONE);
		lblMasaPajak.setForeground(fontColor);
		lblMasaPajak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblMasaPajak.setText("Masa Pajak");
		
		cmbMasaPajak = new TableCombo(comp, SWT.READ_ONLY);
		cmbMasaPajak.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				txtNoSKP.setText(cmbMasaPajak.getData(Integer.toString(cmbMasaPajak.getSelectionIndex())).toString());
				
			}
		});
		cmbMasaPajak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		cmbMasaPajak.setEditable(true);
		cmbMasaPajak.defineColumns(2);
		cmbMasaPajak.setDisplayColumnIndex(1);
		cmbMasaPajak.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		GridData gd_cmbMasaPajak = new GridData(SWT.LEFT, SWT.CENTER, false, false, 5, 1);
		gd_cmbMasaPajak.widthHint = 291;
		cmbMasaPajak.setLayoutData(gd_cmbMasaPajak);
		
		Label lblNoSKP = new Label(comp, SWT.NONE);
		lblNoSKP.setForeground(fontColor);
		lblNoSKP.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblNoSKP.setText("No. SPTPD/SKPD");
		
		txtNoSKP = new Text(comp, SWT.BORDER);
		txtNoSKP.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtNoSKP.setEditable(false);
		txtNoSKP.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		GridData gd_txtNoSKP = new GridData(SWT.LEFT, SWT.CENTER, false, false, 5, 1);
		gd_txtNoSKP.widthHint = 291;
		txtNoSKP.setLayoutData(gd_txtNoSKP);
		
		Label lblNoSSPD = new Label(comp, SWT.NONE);
		lblNoSSPD.setForeground(fontColor);
		lblNoSSPD.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblNoSSPD.setText("No. SSPD");
		
		txtNoSSPD = new Text(comp, SWT.BORDER);
		txtNoSSPD.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtNoSSPD.setEditable(false);
		txtNoSSPD.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		GridData gd_txtNoSSPD = new GridData(SWT.LEFT, SWT.CENTER, false, false, 5, 1);
		gd_txtNoSSPD.widthHint = 291;
		txtNoSSPD.setLayoutData(gd_txtNoSSPD);
		new Label(comp, SWT.NONE);
		
		Composite compButton1 = new Composite(comp, SWT.NONE);
		compButton1.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 5, 1));
		compButton1.setLayout(new GridLayout(2, false));
		
		btnPeriksa = new Button(compButton1, SWT.NONE);
		btnPeriksa.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				txtNoSSPD.setText((ControllerFactory.getMainController().getCpSspdDAOImpl().getNumSSPD(txtNoSKP.getText())));
				txtDendaSSPD.setText((ControllerFactory.getMainController().getCpSspdDAOImpl().getDendaSSPD(txtNoSKP.getText())));
			}
		});
		btnPeriksa.setEnabled(true);
		GridData gd_btnPeriksa = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnPeriksa.widthHint = 90;
		btnPeriksa.setLayoutData(gd_btnPeriksa);
		btnPeriksa.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnPeriksa.setText("Periksa");
		
		Button btnBaru = new Button(compButton1, SWT.NONE);
		btnBaru.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				txtNoSKPDLB.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
				txtKodePajak.setText("");
				txtNoUrut.setText("");
				txtKodeKecamatan.setText("");
				txtKodeKelurahan.setText("");
				txtNamaBadan.setText("");
				txtAlamatBadan.setText("");
				cmbMasaPajak.setText("");
				txtNoSKP.setText("");
				txtNoSSPD.setText("");
				txtDendaSSPD.setText("");
				txtNoSKPDLB.setText("");
			}
		});
				
		GridData gd_btnBaru = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnBaru.widthHint = 90;
		btnBaru.setLayoutData(gd_btnBaru);
		btnBaru.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnBaru.setText("Baru");
        //The editor must have the same size as the cell and must
        //not be any smaller than 50 pixels.
        
		
		/*final TableEditor editor = new TableEditor(tbl_Periksa);
        //The editor must have the same size as the cell and must
        //not be any smaller than 50 pixels.
        editor.horizontalAlignment = SWT.LEFT;
        editor.grabHorizontal = true;
        editor.minimumWidth = 50;*/
        // editing the second column
		
		lblDendaSSPD = new Label(comp, SWT.NONE);
		GridData gd_lblDendaSSPD = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_lblDendaSSPD.widthHint = 139;
		lblDendaSSPD.setLayoutData(gd_lblDendaSSPD);
		lblDendaSSPD.setForeground(fontColor);
		lblDendaSSPD.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblDendaSSPD.setText("Denda SSPD");
		
		txtDendaSSPD = new Text(comp, SWT.BORDER);
		txtDendaSSPD.setEditable(false);
		txtDendaSSPD.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		txtDendaSSPD.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtDendaSSPD.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		GridData gd_txtDendaSSPD = new GridData(SWT.LEFT, SWT.CENTER, false, false, 5, 1);
		gd_txtDendaSSPD.widthHint = 291;
		txtDendaSSPD.setLayoutData(gd_txtDendaSSPD);
		
		Label lblNoSKPDLB = new Label(comp, SWT.NONE);
		lblNoSKPDLB.setForeground(fontColor);
		lblNoSKPDLB.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblNoSKPDLB.setText("No. SKPDLB");
		
		txtNoSKPDLB = new Text(comp, SWT.BORDER);
		txtNoSKPDLB.setEditable(false);
		txtNoSKPDLB.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		txtNoSKPDLB.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtNoSKPDLB.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		GridData gd_txtNoSKPDLB = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
		gd_txtNoSKPDLB.widthHint = 291;
		txtNoSKPDLB.setLayoutData(gd_txtNoSKPDLB);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		
		lblTanggalPenetapan = new Label(comp, SWT.NONE);
		lblTanggalPenetapan.setForeground(fontColor);
		lblTanggalPenetapan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblTanggalPenetapan.setText("Tanggal Penetapan");
		lblTanggalPenetapan.setVisible(false);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		
		Composite compoButton2 = new Composite(comp, SWT.NONE);
		compoButton2.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		compoButton2.setLayout(new GridLayout(1, false));
		
		btnSimpan = new Button(compoButton2, SWT.NONE);
		btnSimpan.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (txtNoSSPD.getText().equalsIgnoreCase(""))
					MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Data belum lengkap.");
				else{
					int idSKPDLB = ControllerFactory.getMainController().getCpPeriksaDAOImpl().getLastIdSKPDLB();
					String kodeBidUsaha=txtNoSKP.getText();
					String[] kodeBU=kodeBidUsaha.split("/");
					String noUrutSubPajak = ControllerFactory.getMainController().getCpPeriksaDAOImpl().getLastNoUrutSubPajak(kodeBU[1]);
					String[] noUrutSubP = noUrutSubPajak.split("/");
					int noUrutSP = Integer.parseInt(noUrutSubP[0]);
					if (noUrutSubP[3]!=String.valueOf(Calendar.getInstance().get(Calendar.YEAR))){
						noUrutSP = 0;
					}
					String noSKPDLB = String.valueOf(noUrutSP+1)+"/"+kodeBU[1]+"/SKPDLB/"+Calendar.getInstance().get(Calendar.YEAR);
					boolean SKPDLBres = ControllerFactory.getMainController().getCpPeriksaDAOImpl().saveSKPDLB(idSKPDLB+1,wp.npwp,noSKPDLB,txtNoSSPD.getText());
					if(SKPDLBres){
						txtNoSKPDLB.setBackground(SWTResourceManager.getColor(SWT.COLOR_RED));
						txtNoSKPDLB.setText(noSKPDLB);
						StringBuffer sb = new StringBuffer();
						sb.append("SAVE" +
								" SKPDLB:"+noSKPDLB+
								" NPWPD:"+wp.getNPWP());
						new LogImp().setLog(sb.toString(), userModul.getDisplayName(), userModul.getSubModul().getNamaModul());
						MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Data berhasil disimpan.");
					}
				}
			}
		});
		btnSimpan.setEnabled(true);
		btnSimpan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnSimpan.setText("Simpan");
		comp.setTabList(new Control[]{txtNoUrut, compButton1, compoButton2});
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		scrolledComposite.setContent(comp);
		scrolledComposite.setMinSize(comp.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
	}
	
	

	@Override
	public void setFocus() {
		txtNoUrut.setFocus();
	}
	
	private UserModul userModul = ControllerFactory.getMainController().getCpUserDAOImpl()
			.getHMUserModul(GlobalVariable.userModul.getIdUser()).get(43); // 2 merupakan id sub modul.
	private TableCombo cmbMasaPajak;
	private Text txtNoSKP;
	private Text txtNoSSPD;
	private Text txtDendaSSPD;
	private Text txtNoSKPDLB;
	
}