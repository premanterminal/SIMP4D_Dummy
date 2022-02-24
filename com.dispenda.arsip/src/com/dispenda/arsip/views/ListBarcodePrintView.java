package com.dispenda.arsip.views;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.SWTResourceManager;

import com.dispenda.arsip.barcode.QRGenerator;
import com.dispenda.controller.ControllerFactory;
import com.dispenda.model.PendaftaranWajibPajak;
import com.dispenda.object.Preferences;
import com.dispenda.report.ReportAction;
import com.dispenda.report.ReportAppContext;

import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.DateTime;

public class ListBarcodePrintView extends ViewPart {
	public ListBarcodePrintView() {
	}

	public final static String ID = ListBarcodePrintView.class.getName();
	private Table tblBarcode;
	private Color fontColor = new Color(Display.getCurrent(),Preferences.FONT_COLOR);
	private Text txtNoUrut;
	private Text txtKodePajak;
	private Text txtKecamatan;
	private Text txtKelurahan;
	private DateTime dtKetetapan;
	private Date masaPajak;
	private Combo cmbKetetapan;
	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new GridLayout(9, false));
		
		Label lblKetetapan = new Label(parent, SWT.NONE);
		lblKetetapan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblKetetapan.setForeground(fontColor);
		lblKetetapan.setText("Ketetapan");
		
		cmbKetetapan = new Combo(parent, SWT.READ_ONLY);
		GridData gd_cmbKetetapan = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_cmbKetetapan.widthHint = 197;
		cmbKetetapan.setLayoutData(gd_cmbKetetapan);
		cmbKetetapan.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		cmbKetetapan.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		cmbKetetapan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		cmbKetetapan.setItems(new String[] {"SPTPD/SKPD", "SKPDKB/SKPDN", "SSPD"});
		
		Label label = new Label(parent, SWT.SEPARATOR | SWT.CENTER);
		GridData gd_label = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 4);
		gd_label.heightHint = 100;
		gd_label.widthHint = 64;
		label.setLayoutData(gd_label);
		
		Label lblNpwpd = new Label(parent, SWT.NONE);
		lblNpwpd.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblNpwpd.setForeground(fontColor);
		lblNpwpd.setText("NPWPD");
		
		txtKodePajak = new Text(parent, SWT.BORDER);
		txtKodePajak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtKodePajak.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtKodePajak.setEditable(false);
		GridData gd_txtKodePajak = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_txtKodePajak.widthHint = 20;
		txtKodePajak.setLayoutData(gd_txtKodePajak);
		
		txtNoUrut = new Text(parent, SWT.BORDER);
		txtNoUrut.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.keyCode == SWT.CR || e.keyCode == SWT.KEYPAD_CR)
				{
					String noUrut = txtNoUrut.getText().trim();
					PendaftaranWajibPajak wp = ControllerFactory.getMainController().getCpWajibPajakDAOImpl().getNPWPD(Integer.parseInt(noUrut));
					if (wp != null)
					{
						txtKodePajak.setText(wp.getNPWP().substring(0, 1));
						txtKodePajak.pack(true);
						txtNoUrut.setLocation(txtKodePajak.getLocation().x+txtKodePajak.getSize().x+5, txtNoUrut.getLocation().y);
						txtNoUrut.setText(wp.getNPWP().substring(1, 8));
						txtNoUrut.pack(true);
						txtKecamatan.setLocation(txtNoUrut.getLocation().x+txtNoUrut.getSize().x+5, txtKecamatan.getLocation().y);
						txtKecamatan.setText(wp.getNPWP().substring(8, 10));
						txtKecamatan.pack(true);
						txtKelurahan.setLocation(txtKecamatan.getLocation().x+txtKecamatan.getSize().x+5, txtKelurahan.getLocation().y);
						txtKelurahan.setText(wp.getNPWP().substring(10, 12));
						txtKelurahan.pack(true);
						if(tblBarcode.getItemCount()>0)
							tblBarcode.removeAll();
						List<TableItem> listItem = ControllerFactory.getMainController().getCpArsipDAOImpl().getDatabyNPWPD(wp.getNPWP(), tblBarcode);
					}
				}
			}
		});
		txtNoUrut.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtNoUrut.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		GridData gd_txtNoUrut = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_txtNoUrut.widthHint = 70;
		txtNoUrut.setLayoutData(gd_txtNoUrut);
		
		txtKecamatan = new Text(parent, SWT.BORDER);
		txtKecamatan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtKecamatan.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtKecamatan.setEditable(false);
		GridData gd_txtKecamatan = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_txtKecamatan.widthHint = 20;
		txtKecamatan.setLayoutData(gd_txtKecamatan);
		
		txtKelurahan = new Text(parent, SWT.BORDER);
		txtKelurahan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtKelurahan.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtKelurahan.setEditable(false);
		GridData gd_txtKelurahan = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_txtKelurahan.widthHint = 20;
		txtKelurahan.setLayoutData(gd_txtKelurahan);
		new Label(parent, SWT.NONE);
		
		Label lblTanggal = new Label(parent, SWT.NONE);
		lblTanggal.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblTanggal.setForeground(fontColor);
		lblTanggal.setText("Tanggal");
		
		dtKetetapan = new DateTime(parent, SWT.BORDER | SWT.DROP_DOWN);
		dtKetetapan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		
		Button btnLihat = new Button(parent, SWT.NONE);
		btnLihat.addSelectionListener(new SelectionAdapter() {
			@SuppressWarnings("deprecation")
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(tblBarcode.getItemCount()>0)
					tblBarcode.removeAll();
				Calendar instance = Calendar.getInstance();
				instance.set(Calendar.DAY_OF_MONTH, dtKetetapan.getDay());
				instance.set(Calendar.MONTH, dtKetetapan.getMonth());
				instance.set(Calendar.YEAR, dtKetetapan.getYear());
				masaPajak = new Date(dtKetetapan.getYear() - 1900, dtKetetapan.getMonth(), dtKetetapan.getDay());
				List<TableItem> listItem = ControllerFactory.getMainController().getCpArsipDAOImpl().getDatabyDate(masaPajak, tblBarcode, cmbKetetapan.getSelectionIndex());
				if (listItem.size() < 1)
					MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Hasil pencarian tidak ada");
			}
		});
		btnLihat.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		GridData gd_btnLihat = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnLihat.widthHint = 74;
		btnLihat.setLayoutData(gd_btnLihat);
		btnLihat.setText("Lihat");
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		
		Label lblBarcodeTable = new Label(parent, SWT.NONE);
		lblBarcodeTable.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		lblBarcodeTable.setText("Barcode Table");
		lblBarcodeTable.setForeground(fontColor);
		lblBarcodeTable.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		
		tblBarcode = new Table(parent, SWT.BORDER | SWT.FULL_SELECTION | SWT.CHECK);
		tblBarcode.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 8, 1));
		tblBarcode.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		tblBarcode.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE-1, SWT.NORMAL));
		tblBarcode.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		tblBarcode.setHeaderVisible(true);
		tblBarcode.setLinesVisible(true);
		
		String[] colHeader = new String[]{"No. Dokumen","Tgl. Dokumen","MasaPajakDari","MasaPajakSampai","NPWPD","Nama Badan"};
		int[] widthHeader = new int[]{200,150,150,150,120,300};
		for(int i=0;i<colHeader.length;i++){
			TableColumn col = new TableColumn(tblBarcode, SWT.NONE);
			col.setText(colHeader[i]);
			col.setWidth(widthHeader[i]);
		}
		tblBarcode.getColumn(0).addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int count = 0;
				for(int i=0;i<tblBarcode.getItemCount();i++){
					if(tblBarcode.getItem(i).getChecked()){
						count++;
					}
				}
				if(count == tblBarcode.getItemCount()){
					for(int i=0;i<tblBarcode.getItemCount();i++){
						tblBarcode.getItem(i).setChecked(false);
					}
				}else{
					for(int i=0;i<tblBarcode.getItemCount();i++){
						tblBarcode.getItem(i).setChecked(true);
					}
				}
			}
		});
		
		Composite compButton = new Composite(parent, SWT.NONE);
		compButton.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		compButton.setLayout(new GridLayout(1, false));
		
		Button btnRefresh = new Button(compButton, SWT.NONE);
		GridData gd_btnRefresh = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnRefresh.widthHint = 90;
		btnRefresh.setLayoutData(gd_btnRefresh);
		btnRefresh.setText("Refresh");
		btnRefresh.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BORDER));
		btnRefresh.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnRefresh.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				refresh();
			}
		});
		
		Button btnPrintAll = new Button(compButton, SWT.NONE);
		GridData gd_btnPrintAll = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnPrintAll.widthHint = 90;
		btnPrintAll.setLayoutData(gd_btnPrintAll);
		btnPrintAll.setText("Print All..");
		btnPrintAll.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BORDER));
		btnPrintAll.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnPrintAll.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				for(TableItem item : tblBarcode.getItems()){
					
				}
			}
		});
		
		Button btnPrint = new Button(compButton, SWT.NONE);
		GridData gd_btnPrint = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnPrint.widthHint = 90;
		btnPrint.setLayoutData(gd_btnPrint);
		btnPrint.setText("Print..");
		btnPrint.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BORDER));
		btnPrint.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnPrint.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int idx = 0;
				QRGenerator qr = new QRGenerator();
				ReportAppContext.nameObjectQR = "dsQRcode";
				ReportAppContext.classLoader = ListBarcodePrintView.class.getClassLoader();
				if(!ReportAppContext.objectQR.isEmpty())
					ReportAppContext.objectQR.clear();
				for(int j=0;j<tblBarcode.getItems().length;j++){
					if(tblBarcode.getItem(j).getChecked()){
						List<Object> valuesQR = new ArrayList<Object>();
						byte[] qrCodeImage = qr.generateImage(tblBarcode.getItem(j).getText(0));
						valuesQR.add(qrCodeImage);
						valuesQR.add(tblBarcode.getItem(j).getText(0));
						ReportAppContext.objectQR.put(Integer.toString(idx), valuesQR);
						idx++;
					}
				}
				ReportAction.start("Barcode.rptdesign");
			}
		});
	}

	@Override
	public void setFocus() {

	}

	private void refresh(){
		txtKodePajak.setText("");
		txtNoUrut.setText("");
		txtKecamatan.setText("");
		txtKelurahan.setText("");
		cmbKetetapan.deselectAll();
		if(tblBarcode.getItemCount()>0)
			tblBarcode.removeAll();
//		List<TableItem> listItem = ControllerFactory.getMainController().getCpArsipDAOImpl().getData("POS_END", tblBarcode);
	}
}