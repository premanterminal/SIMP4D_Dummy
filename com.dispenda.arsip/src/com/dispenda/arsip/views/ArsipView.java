package com.dispenda.arsip.views;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.nebula.widgets.tablecombo.TableCombo;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Button;

import com.dispenda.arsip.dialog.ArsipImageDialog;
import com.dispenda.controller.ControllerFactory;
import com.dispenda.controller.UIController;
import com.dispenda.model.Arsip;
import com.dispenda.model.Pajak;
import com.dispenda.model.PajakProvider;
import com.dispenda.model.PendaftaranWajibPajak;
import com.dispenda.model.Sptpd;
import com.dispenda.object.Preferences;
import com.dispenda.report.ReportAction;
import com.dispenda.report.ReportAppContext;

import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.osgi.framework.Bundle;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseAdapter;

public class ArsipView extends ViewPart {
	public ArsipView() {
	}
	public static final String ID = ArsipView.class.getName();
	private Color fontColor = new Color(Display.getCurrent(),Preferences.FONT_COLOR);
	private Text txtRak;
	private Text txtBox;
	private Text txtMap;
	private Table tblDetail;
	private Text txtKeterangan;
	private Text txtNamaBadan;
	private Text txtLocId;
	private Text txtDocId;
	private Text txtCari;
	private Table tblCari;
	private Combo cmbFilter;
	private Text txtNpwpd;
	private Arsip ArsipModel = new Arsip();
	private List<Sptpd> listArsip;
	private List<Arsip> listMapArsip = new ArrayList<Arsip>();
	private Table tblBerkas;
	private Integer noBox;
	private Integer noMap;
	private String npwpd;
	private String namaPajak;
	private Button btnSimpan;
	private Canvas canvas;
	private Combo cmbPajak;
	private Label lblDash;
	private Image image;
	private Button btnPrev;
	private Button btnNext;
	private Integer indexImage = 0;
	private Button btnCetak;

	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new GridLayout(2, false));
		
		Composite compSearch = new Composite(parent, SWT.NONE);
		compSearch.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		compSearch.setLayout(new GridLayout(5, false));
		compSearch.setFocus();
		
		Label lblFilter = new Label(compSearch, SWT.NONE);
		lblFilter.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblFilter.setText("Filter");
		lblFilter.setForeground(fontColor);
		lblFilter.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		cmbFilter = new Combo(compSearch, SWT.READ_ONLY);
		cmbFilter.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (cmbFilter.getText().equalsIgnoreCase("QR Code"))
					txtCari.setFocus();
				if (cmbFilter.getText().equalsIgnoreCase("No Box")){
					cmbPajak.setVisible(true);
					lblDash.setVisible(true);
					cmbPajak.deselectAll();
				}else{
					cmbPajak.setVisible(false);
					lblDash.setVisible(false);
					cmbPajak.deselectAll();
				}
			}
		});
		cmbFilter.setItems(new String[] {"NPWPD", "No SKP", "No SSPD", "No Box", "No Reg", "QR Code"});
		GridData gd_cmbFilter = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_cmbFilter.widthHint = 164;
		cmbFilter.setLayoutData(gd_cmbFilter);
		cmbFilter.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		cmbFilter.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		cmbFilter.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		cmbFilter.select(0);
		
		lblDash = new Label(compSearch, SWT.NONE);
		lblDash.setText("-");
		lblDash.setVisible(false);
		lblDash.setForeground(fontColor);
		lblDash.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		cmbPajak = new Combo(compSearch, SWT.READ_ONLY);
		cmbPajak.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		cmbPajak.setVisible(false);
		cmbPajak.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		cmbPajak.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		cmbPajak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		UIController.INSTANCE.loadPajak(cmbPajak, PajakProvider.INSTANCE.getPajak().toArray());
		
		Label lblCari = new Label(compSearch, SWT.NONE);
		lblCari.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblCari.setText("Cari :");
		lblCari.setForeground(fontColor);
		lblCari.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		txtCari = new Text(compSearch, SWT.BORDER | SWT.SEARCH);
		txtCari.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				if (cmbFilter.getText().equalsIgnoreCase("QR Code")){
					indexImage = 0;
					btnPrev.setEnabled(false);
					btnNext.setEnabled(true);
					String filter = "";
					String[] tahun = txtCari.getText().split("/");
					if (tahun.length > 2 && tahun[tahun.length-1].matches("\\d+")){
						if(Integer.parseInt(tahun[tahun.length-1]) > 2000){
							if(txtCari.getText().contains("/SPTPD") || txtCari.getText().contains("/SKPD"))
								filter = "No SKP";
							else if (txtCari.getText().contains("/SSPD/"))
								filter = "No SSPD";
							getDataArsip(filter);
							if (tblCari.getItemCount() > 0){
								tblCari.setSelection(0);
								txtNpwpd.setText(listMapArsip.get(tblCari.getSelectionIndex()).getNPWPD());
								txtNamaBadan.setText(listMapArsip.get(tblCari.getSelectionIndex()).getNamaBadan());
								txtRak.setText(listMapArsip.get(tblCari.getSelectionIndex()).getNamaRak());
								txtBox.setText(String.valueOf(listMapArsip.get(tblCari.getSelectionIndex()).getNoBox()));
								txtMap.setText(String.valueOf(listMapArsip.get(tblCari.getSelectionIndex()).getNoMap()));
								getDetailArsip();
								int idx = getIndex(txtCari.getText());
								if (idx >= 0){
									ResultSet rsArsip = ControllerFactory.getMainController().getCpArsipDAOImpl().getArsipDetail(txtNpwpd.getText(), tblBerkas.getItem(idx).getText(0));
									UIController.INSTANCE.loadDataVert(tblDetail, rsArsip);
									loadImages(tblBerkas.getItem(idx).getText(0));
								}
							}
							
//							tblBerkas.setSelection(tblBerkas.getItems())
						}
							
					}
				}
			}
		});
		txtCari.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.keyCode == SWT.CR || e.keyCode == SWT.KEYPAD_CR){
					indexImage = 0;
					btnPrev.setEnabled(false);
					btnNext.setEnabled(true);
					getDataArsip(cmbFilter.getText());
					if (tblCari.getItemCount() > 0){
						tblCari.setSelection(0);
						txtNpwpd.setText(listMapArsip.get(tblCari.getSelectionIndex()).getNPWPD());
						txtNamaBadan.setText(listMapArsip.get(tblCari.getSelectionIndex()).getNamaBadan());
						txtRak.setText(listMapArsip.get(tblCari.getSelectionIndex()).getNamaRak());
						txtBox.setText(String.valueOf(listMapArsip.get(tblCari.getSelectionIndex()).getNoBox()));
						txtMap.setText(String.valueOf(listMapArsip.get(tblCari.getSelectionIndex()).getNoMap()));
						getDetailArsip();
					}
				}
			}
		});
		txtCari.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		txtCari.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtCari.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		txtCari.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtCari.setFocus();
		
		Button btnSearch = new Button(compSearch, SWT.NONE);
		btnSearch.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				btnSimpan.setEnabled(false);
				indexImage = 0;
				btnPrev.setEnabled(false);
				btnNext.setEnabled(true);
				getDataArsip(cmbFilter.getText());
				if (tblCari.getItemCount() > 0){
					tblCari.setSelection(0);
					txtNpwpd.setText(listMapArsip.get(tblCari.getSelectionIndex()).getNPWPD());
					txtNamaBadan.setText(listMapArsip.get(tblCari.getSelectionIndex()).getNamaBadan());
					txtRak.setText(listMapArsip.get(tblCari.getSelectionIndex()).getNamaRak());
					txtBox.setText(String.valueOf(listMapArsip.get(tblCari.getSelectionIndex()).getNoBox()));
					txtMap.setText(String.valueOf(listMapArsip.get(tblCari.getSelectionIndex()).getNoMap()));
					getDetailArsip();
				}
			}
		});
		GridData gd_btnSearch = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnSearch.widthHint = 90;
		btnSearch.setLayoutData(gd_btnSearch);
		btnSearch.setText("Search..");
		btnSearch.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BORDER));
		btnSearch.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		tblCari = new Table(compSearch, SWT.BORDER | SWT.FULL_SELECTION);
		tblCari.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				txtNpwpd.setText(listMapArsip.get(tblCari.getSelectionIndex()).getNPWPD());
				txtNamaBadan.setText(listMapArsip.get(tblCari.getSelectionIndex()).getNamaBadan());
				txtRak.setText(listMapArsip.get(tblCari.getSelectionIndex()).getNamaRak());
				txtBox.setText(String.valueOf(listMapArsip.get(tblCari.getSelectionIndex()).getNoBox()));
				txtMap.setText(String.valueOf(listMapArsip.get(tblCari.getSelectionIndex()).getNoMap()));
				getDetailArsip();
			}
		});
		GridData gd_tblCari = new GridData(SWT.FILL, SWT.FILL, true, false, 5, 1);
		gd_tblCari.heightHint = 96;
		tblCari.setLayoutData(gd_tblCari);
		tblCari.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		tblCari.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		tblCari.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		tblCari.setHeaderVisible(true);
		tblCari.setLinesVisible(true);
		
		Label lblBerkas = new Label(compSearch, SWT.NONE);
		lblBerkas.setText("Berkas");
		lblBerkas.setForeground(fontColor);
		lblBerkas.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		new Label(compSearch, SWT.NONE);
		new Label(compSearch, SWT.NONE);
		new Label(compSearch, SWT.NONE);
		new Label(compSearch, SWT.NONE);
		
		tblBerkas = new Table(compSearch, SWT.BORDER | SWT.FULL_SELECTION);
		tblBerkas.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 5, 1));
		tblBerkas.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				indexImage = 0;
				btnPrev.setEnabled(false);
				btnNext.setEnabled(true);
				ResultSet rsArsip = ControllerFactory.getMainController().getCpArsipDAOImpl().getArsipDetail(txtNpwpd.getText(), tblBerkas.getItem(tblBerkas.getSelectionIndex()).getText(0));
				UIController.INSTANCE.loadDataVert(tblDetail, rsArsip);
				loadImages(tblBerkas.getItem(tblBerkas.getSelectionIndex()).getText(0));
			}
		});
		tblBerkas.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		tblBerkas.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		tblBerkas.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		tblBerkas.setHeaderVisible(true);
		tblBerkas.setLinesVisible(true);
		compSearch.setTabList(new Control[]{cmbFilter, txtCari, btnSearch});
		
		Composite compDetail = new Composite(parent, SWT.NONE);
		compDetail.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		compDetail.setLayout(new GridLayout(2, false));
		
		Group grpLokasi = new Group(compDetail, SWT.NONE);
		GridData gd_grpLokasi = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
		gd_grpLokasi.widthHint = 223;
		grpLokasi.setLayoutData(gd_grpLokasi);
		grpLokasi.setText("Lokasi");
		grpLokasi.setForeground(fontColor);
		grpLokasi.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		grpLokasi.setLayout(new GridLayout(2, false));
		
		Label lblNoRak = new Label(grpLokasi, SWT.NONE);
		lblNoRak.setText("Nama Rak");
		lblNoRak.setForeground(fontColor);
		lblNoRak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		txtRak = new Text(grpLokasi, SWT.BORDER);
		txtRak.setEditable(false);
		txtRak.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		txtRak.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtRak.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		txtRak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		Label lblNoBox = new Label(grpLokasi, SWT.NONE);
		lblNoBox.setText("No. Box");
		lblNoBox.setForeground(fontColor);
		lblNoBox.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		txtBox = new Text(grpLokasi, SWT.BORDER);
		txtBox.setEditable(false);
		txtBox.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		txtBox.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtBox.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		txtBox.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		Label lblNoMap = new Label(grpLokasi, SWT.NONE);
		lblNoMap.setText("No. Map");
		lblNoMap.setForeground(fontColor);
		lblNoMap.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		txtMap = new Text(grpLokasi, SWT.BORDER);
		txtMap.setEditable(false);
		txtMap.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		txtMap.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtMap.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		txtMap.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		Composite compID = new Composite(compDetail, SWT.NONE);
		compID.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 2));
		compID.setLayout(new GridLayout(7, false));
		
		Label lblBarcode = new Label(compID, SWT.NONE);
		lblBarcode.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 7, 1));
		lblBarcode.setText("Barcode");
		lblBarcode.setForeground(fontColor);
		lblBarcode.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		canvas = new Canvas(compID, SWT.NONE);
		canvas.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				if (image != null){
					String noBerkas = tblBerkas.getItem(tblBerkas.getSelectionIndex()).getText(0);
					if (indexImage > 0)
						noBerkas += "(" + indexImage + ")";
					ArsipImageDialog arsip = new ArsipImageDialog(Display.getCurrent().getActiveShell(), noBerkas);
					arsip.open();
				}
			}
		});
		GridData gd_canvas = new GridData(SWT.LEFT, SWT.CENTER, false, false, 7, 1);
		gd_canvas.widthHint = 200;
		gd_canvas.heightHint = 230;
		canvas.addPaintListener(new PaintListener() {
		@Override
		public void paintControl(PaintEvent pe) {
			int tmpX = image.getBounds().width;
		    tmpX = (200 / 2 - tmpX / 2);
		    int tmpY = image.getBounds().height;
		    tmpY = (230 / 2 - tmpY / 2);
		    if(tmpX <= 0) tmpX = pe.x;
		    else tmpX += pe.x;
		    if(tmpY <= 0) tmpY = pe.y;
		    else tmpY += pe.y;
			pe.gc.drawImage(image, tmpX, tmpY);
		}});
		canvas.setLayoutData(gd_canvas);
		
		btnPrev = new Button(compID, SWT.NONE);
		btnPrev.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (indexImage > 1){
					indexImage--;
					if (loadNextImages(tblBerkas.getItem(tblBerkas.getSelectionIndex()).getText(0) + "(" + indexImage + ")")){
						btnNext.setEnabled(true);
					}
				}else if (indexImage == 1){
					indexImage--;
					if (loadNextImages(tblBerkas.getItem(tblBerkas.getSelectionIndex()).getText(0))){
						btnNext.setEnabled(true);
						btnPrev.setEnabled(false);
					}
				}
				
			}
		});
		btnPrev.setToolTipText("Sebelumnya");
		GridData gd_btnPrev = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnPrev.widthHint = 50;
		btnPrev.setLayoutData(gd_btnPrev);
		btnPrev.setText("<<");
		btnPrev.setEnabled(false);
		
		btnCetak = new Button(compID, SWT.NONE);
		btnCetak.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String noBerkas = tblBerkas.getItem(tblBerkas.getSelectionIndex()).getText(0);
				String[] splitNo = noBerkas.split("/");
				String folder = splitNo[splitNo.length-2];
				String tahun = splitNo[splitNo.length-1].split("\\(")[0];
				String urlImage = "http://172.16.78.3/ScannedArsip/" + folder + "/" + tahun + "/" + noBerkas.replace("/", "-") + ".jpg";
				
				ReportAppContext.name = "dsArsip";
				if(!ReportAppContext.map.isEmpty())
					ReportAppContext.map.clear();
				ReportAppContext.map.put("image", urlImage);
				
				ReportAction.start("BerkasArsip.rptdesign");
			}
		});
		GridData gd_btnCetak = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnCetak.widthHint = 90;
		btnCetak.setLayoutData(gd_btnCetak);
		btnCetak.setText("Cetak");
		
		btnNext = new Button(compID, SWT.NONE);
		btnNext.setToolTipText("Berikutnya");
		btnNext.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				indexImage++;
				if (loadNextImages(tblBerkas.getItem(tblBerkas.getSelectionIndex()).getText(0) + "(" + indexImage + ")")){
					btnPrev.setEnabled(true);
				}else{
					indexImage--;
					btnNext.setEnabled(false);
					MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Tidak ada berkas selanjutnya");
					if (indexImage == 0)
						loadNextImages(tblBerkas.getItem(tblBerkas.getSelectionIndex()).getText(0));
					else
						loadNextImages(tblBerkas.getItem(tblBerkas.getSelectionIndex()).getText(0) + "(" + indexImage + ")");
				}
			}
		});
		GridData gd_btnNext = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnNext.widthHint = 50;
		btnNext.setLayoutData(gd_btnNext);
		btnNext.setText(">>");
		new Label(compID, SWT.NONE);
		
		Label label = new Label(compID, SWT.NONE);
		label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label.setText("-");
		label.setForeground(fontColor);
		label.setVisible(false);
		label.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		txtLocId = new Text(compID, SWT.BORDER);
		txtLocId.setEditable(false);
		GridData gd_txtLocId = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_txtLocId.widthHint = 44;
		txtLocId.setLayoutData(gd_txtLocId);
		txtLocId.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtLocId.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		txtLocId.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtLocId.setVisible(false);
		
		txtDocId = new Text(compID, SWT.BORDER);
		txtDocId.setEditable(false);
		txtDocId.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		txtDocId.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtDocId.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		txtDocId.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtDocId.setVisible(false);
		
		btnSimpan = new Button(compDetail, SWT.NONE);
		btnSimpan.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ArsipModel.setNamaRak(namaPajak);
				ArsipModel.setNPWPD(npwpd);
				ArsipModel.setNoBox(noBox);
				ArsipModel.setNoMap(noMap);
				if (ControllerFactory.getMainController().getCpArsipDAOImpl().saveArsip(ArsipModel))
					MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Box Arsip berhasil ditambah");
				else
					MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Box Arsip gagal ditambah");
			}
		});
		btnSimpan.setEnabled(false);
		GridData gd_btnSimpan = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
		gd_btnSimpan.widthHint = 97;
		btnSimpan.setLayoutData(gd_btnSimpan);
		btnSimpan.setText("Simpan");
		
		Composite composite = new Composite(compDetail, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		composite.setLayout(new GridLayout(2, false));
		
		Label lblNpwpd = new Label(composite, SWT.NONE);
		lblNpwpd.setText("NPWPD");
		lblNpwpd.setForeground(fontColor);
		lblNpwpd.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		txtNpwpd = new Text(composite, SWT.BORDER);
		txtNpwpd.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		txtNpwpd.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtNpwpd.setEditable(false);
		txtNpwpd.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtNpwpd.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblNamaBadan = new Label(composite, SWT.NONE);
		lblNamaBadan.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNamaBadan.setText("Nama Badan");
		lblNamaBadan.setForeground(fontColor);
		lblNamaBadan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		txtNamaBadan = new Text(composite, SWT.BORDER);
		txtNamaBadan.setEditable(false);
		txtNamaBadan.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		txtNamaBadan.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtNamaBadan.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		txtNamaBadan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		tblDetail = new Table(composite, SWT.BORDER | SWT.FULL_SELECTION);
		tblDetail.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		tblDetail.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		tblDetail.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		tblDetail.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		tblDetail.setHeaderVisible(true);
		tblDetail.setLinesVisible(true);
		
		txtKeterangan = new Text(composite, SWT.BORDER | SWT.MULTI);
		txtKeterangan.setEditable(false);
		GridData gd_txtKeterangan = new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1);
		gd_txtKeterangan.heightHint = 61;
		txtKeterangan.setLayoutData(gd_txtKeterangan);
		txtKeterangan.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtKeterangan.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		txtKeterangan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
	}

	@Override
	public void setFocus() {

	}
	
	private void createColumnNPWPD(){
		tblCari.removeAll();
		if(tblCari.getColumnCount() <= 0){
			String[] listColumn = {"NPWPD", "Nama Badan"};
			for (int i=listColumn.length - 1;i>=0;i--)
			{
				TableColumn colPajak = new TableColumn(tblCari, SWT.NONE,0);
				colPajak.setText(listColumn[i]);
				colPajak.setWidth((i * 350) + 130);
			}
		}
	}
	
	private void getDataArsip(String filter){
		if (filter.equalsIgnoreCase("No Box")){
			String pajak = cmbPajak.getText();
			listMapArsip = ControllerFactory.getMainController().getCpArsipDAOImpl().getArsip(filter, txtCari.getText().toUpperCase() + ";" + (pajak.equalsIgnoreCase("") ? " " : pajak));
		}else
			listMapArsip = ControllerFactory.getMainController().getCpArsipDAOImpl().getArsip(filter, txtCari.getText().toUpperCase());
		if (listMapArsip != null && listMapArsip.size()>0){
			createColumnNPWPD();
			for (int i=0;i<listMapArsip.size();i++){
				TableItem item = new TableItem(tblCari, SWT.NONE);
				item.setText(0, listMapArsip.get(i).getNPWPD());
				item.setText(1, listMapArsip.get(i).getNamaBadan());
			}
		}else if (cmbFilter.getText().equalsIgnoreCase("NPWPD")){
			if (MessageDialog.openConfirm(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Data Arsip tidak ditemukan. Tambah box untuk NPWPD?")){
				List<Pajak> listPajak = new ArrayList<Pajak>();
				try{
					PendaftaranWajibPajak wp = ControllerFactory.getMainController().getCpWajibPajakDAOImpl().getNPWPD(Integer.parseInt(txtCari.getText()));
					npwpd = wp.getNPWP();
					listPajak = ControllerFactory.getMainController().getCpPajakDAOImpl().getPajak(Integer.valueOf(npwpd.substring(0, 1)));
					namaPajak = listPajak.get(0).getNamaPajak();
					noBox = ControllerFactory.getMainController().getCpArsipDAOImpl().getNoBox(namaPajak);
					noMap = ControllerFactory.getMainController().getCpArsipDAOImpl().getNoMap(namaPajak, noBox);
					if (noMap == 10){
						noBox += 1;
						noMap = 1;
					}else{
						noMap += 1;
					}
					
					txtRak.setText(listPajak.get(0).getNamaPajak());
					txtBox.setText(noBox.toString());
					txtMap.setText(noMap.toString());
					btnSimpan.setEnabled(true);
				}catch(Exception e){
					MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Terjadi kesalahan. Data gagal di diproses");
					txtRak.setText("");
					txtBox.setText("");
					txtMap.setText("");
					btnSimpan.setEnabled(false);
				}
			}else{
				txtRak.setText("");
				txtBox.setText("");
				txtMap.setText("");
			}
			txtNpwpd.setText("");
			txtNamaBadan.setText("");
			tblBerkas.removeAll();
			tblCari.removeAll();
			tblDetail.removeAll();
		}else{
			MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Data Arsip tidak ditemukan.");
			txtRak.setText("");
			txtBox.setText("");
			txtMap.setText("");
			txtNpwpd.setText("");
			txtNamaBadan.setText("");
			tblBerkas.removeAll();
			tblCari.removeAll();
			tblDetail.removeAll();
		}
	}
	
	@SuppressWarnings("deprecation")
	private void getDetailArsip(){
		tblBerkas.removeAll();
		tblDetail.removeAll();
		listArsip = ControllerFactory.getMainController().getCpSptpdDAOImpl().getMasaPajakSKPD(listMapArsip.get(tblCari.getSelectionIndex()).getNPWPD());
		listArsip.addAll(ControllerFactory.getMainController().getCpSptpdDAOImpl().getMasaPajakSKPDKB(listMapArsip.get(tblCari.getSelectionIndex()).getNPWPD()));
		listArsip.addAll(ControllerFactory.getMainController().getCpArsipDAOImpl().getSspd(listMapArsip.get(tblCari.getSelectionIndex()).getNPWPD()));
		listArsip.addAll(ControllerFactory.getMainController().getCpArsipDAOImpl().getSspd_SKPDKB(listMapArsip.get(tblCari.getSelectionIndex()).getNPWPD()));
		Collections.sort(listArsip);
		createColumnBerkas();
		
		if (listArsip.size() > 0)
		{	
			for(int i = 0; i<listArsip.size(); i++){
				TableItem ti = new TableItem(tblBerkas, SWT.NONE);
				if (listArsip.get(i).getMasaPajakDari().getMonth() == listArsip.get(i).getMasaPajakSampai().getMonth() && listArsip.get(i).getMasaPajakDari().getYear() == listArsip.get(i).getMasaPajakSampai().getYear())
				{
					ti.setText(new String[] {listArsip.get(i).getNoSPTPD(), UIController.INSTANCE.formatMonth(listArsip.get(i).getMasaPajakDari().getMonth() + 1, Locale.getDefault()) + " " + (listArsip.get(i).getMasaPajakDari().getYear() + 1900), listArsip.get(i).getPosEnd().toString(), listArsip.get(i).getNoNPPD()});
				}
				else
				{
					ti.setText(new String[] {listArsip.get(i).getNoSPTPD(), UIController.INSTANCE.formatMonth(listArsip.get(i).getMasaPajakDari().getMonth() + 1, Locale.getDefault()) + " " + (listArsip.get(i).getMasaPajakDari().getYear() + 1900) + " - " + 
							UIController.INSTANCE.formatMonth(listArsip.get(i).getMasaPajakSampai().getMonth() + 1, Locale.getDefault()) + " " + (listArsip.get(i).getMasaPajakSampai().getYear() + 1900), listArsip.get(i).getPosEnd().toString(), listArsip.get(i).getNoNPPD()});
				}
			}
		}
	}
	
	private void loadImages(String noBerkas){
		try {
			String path = "";
			URL url = null;
			String[] splitNo = noBerkas.split("/");
			String folder = splitNo[splitNo.length-2];
			String tahun = splitNo[splitNo.length-1].split("\\(")[0];
			path = "http://172.16.78.3/ScannedArsip/" + folder + "/" + tahun + "/" + noBerkas.replace("/", "-") + ".jpg";
			url = new URL(path);
			
			ImageData imgData = new ImageData(url.openStream());
			double ratio = (double)imgData.height/(double)imgData.width;
			double heighT = ratio*200;
			image = new Image(Display.getCurrent(),imgData.scaledTo(200, (int)heighT));
			canvas.redraw();
		} catch (IOException e) {
//			e.printStackTrace();
			image = null;
			canvas.redraw();
		}
	}
	
	private boolean loadNextImages(String noBerkas){
		try {
			String path = "";
			URL url = null;
			String[] splitNo = noBerkas.split("/");
			String folder = splitNo[splitNo.length-2];
			String tahun = splitNo[splitNo.length-1].split("\\(")[0];
			path = "http://172.16.78.3/ScannedArsip/" + folder + "/" + tahun + "/" + noBerkas.replace("/", "-") + ".jpg";
			url = new URL(path);
			
			ImageData imgData = new ImageData(url.openStream());
			double ratio = (double)imgData.height/(double)imgData.width;
			double heighT = ratio*200;
			image = new Image(Display.getCurrent(),imgData.scaledTo(200, (int)heighT));
			canvas.redraw();
			return true;
		} catch (IOException e) {
//			e.printStackTrace();
			image = null;
			canvas.redraw();
			return false;
		}
	}

	private void createColumnBerkas(){
		if(tblBerkas.getColumnCount() <= 0){
			TableColumn colPajak4 = new TableColumn(tblBerkas, SWT.NONE,0);
			colPajak4.setText("No Reg");
			colPajak4.setWidth(90);
			TableColumn colPajak = new TableColumn(tblBerkas, SWT.NONE,0);
			colPajak.setText("Posisi Berkas");
			colPajak.setWidth(120);
			TableColumn colPajak1 = new TableColumn(tblBerkas, SWT.NONE,0);
			colPajak1.setText("Masa Pajak");
			colPajak1.setWidth(280);
			TableColumn colPajak2 = new TableColumn(tblBerkas, SWT.NONE,0);
			colPajak2.setText("No Berkas");
			colPajak2.setWidth(160);
		}
	}
	
	private int getIndex(String noBerkas){
		int retValue = -1;
		TableItem[] items = tblBerkas.getItems();
		for (int i=0;i<items.length;i++){
			if (items[i].getText(0).toUpperCase().contains(noBerkas)){
				tblBerkas.setSelection(i);
				tblBerkas.setFocus();
				retValue = i;
				break;
			}
		}
		return retValue;
	}
	
	/*private void scanQR(String code){
		String[] split = code.split("/S");
		
			if(code.contains("/SKPD/") ||
				code.contains("SPTPD")){
				int pos = ControllerFactory.getMainController().getCpArsipDAOImpl().getPos("POS_END","SPTPD","NO_SPTPD",code.toUpperCase());
				if(pos==0 || pos==4 || pos==2){
					ControllerFactory.getMainController().getCpArsipDAOImpl().updatePos("SPTPD","NO_SPTPD",code.toUpperCase(), pos, 3);
					TableItem ti = new TableItem(tblMasuk, SWT.NONE);
					ti.setText(new String[]{code.toUpperCase(),"",""});
					ti.setBackground(SWTResourceManager.getColor(SWT.COLOR_YELLOW));
					txtBarcodeMasuk.setText("");
					txtBarcodeKeluar.setText("");
				}else{
					ControllerFactory.getMainController().getCpArsipDAOImpl().updatePos("SPTPD","NO_SPTPD",code, pos, 4);
					TableItem ti = new TableItem(tblKeluar, SWT.NONE);
					ti.setText(new String[]{code,"",""});
					ti.setBackground(SWTResourceManager.getColor(SWT.COLOR_RED));
					txtBarcodeKeluar.setText("");
					txtBarcodeMasuk.setText("");
				}
			}else if(code.contains("/SKPDKB/") || 
					code.contains("/SKPDN/")){
				int pos = ControllerFactory.getMainController().getCpArsipDAOImpl().getPos("POS_END","PERIKSA","NO_SKP",code.toUpperCase());
				if(pos==0 || pos==4 || pos==2){
					ControllerFactory.getMainController().getCpArsipDAOImpl().updatePos("PERIKSA","NO_SKP",code.toUpperCase(), pos, 3);
					TableItem ti = new TableItem(tblMasuk, SWT.NONE);
					ti.setText(new String[]{code.toUpperCase(),"",""});
					ti.setBackground(SWTResourceManager.getColor(SWT.COLOR_YELLOW));
					txtBarcodeMasuk.setText("");
					txtBarcodeKeluar.setText("");
				}else{
					ControllerFactory.getMainController().getCpArsipDAOImpl().updatePos("PERIKSA","NO_SKP",code, pos, 4);
					TableItem ti = new TableItem(tblKeluar, SWT.NONE);
					ti.setText(new String[]{code,"",""});
					ti.setBackground(SWTResourceManager.getColor(SWT.COLOR_RED));
					txtBarcodeKeluar.setText("");
					txtBarcodeMasuk.setText("");
				}
			}else if(txtBarcodeMasuk.getText().contains("/SSPD/")){
				int pos = ControllerFactory.getMainController().getCpArsipDAOImpl().getPos("POS_END","SSPD","NO_SSPD",code.toUpperCase());
				if(pos==0 || pos==4 || pos==2){
					ControllerFactory.getMainController().getCpArsipDAOImpl().updatePos("SSPD","NO_SSPD",code.toUpperCase(), pos, 3);
					TableItem ti = new TableItem(tblMasuk, SWT.NONE);
					ti.setText(new String[]{code.toUpperCase(),"",""});
					ti.setBackground(SWTResourceManager.getColor(SWT.COLOR_YELLOW));
					txtBarcodeMasuk.setText("");
					txtBarcodeKeluar.setText("");
				}else{
					ControllerFactory.getMainController().getCpArsipDAOImpl().updatePos("SSPD","NO_SSPD",code, pos, 4);
					TableItem ti = new TableItem(tblKeluar, SWT.NONE);
					ti.setText(new String[]{code,"",""});
					ti.setBackground(SWTResourceManager.getColor(SWT.COLOR_RED));
					txtBarcodeKeluar.setText("");
					txtBarcodeMasuk.setText("");
				}
//				ControllerFactory.getMainController().getCpArsipDAOImpl().updatePos("SSPD",txtBarcodeMasuk.getText(), 4, 3);
			}else{
				// Dokumen tidak dikenal
				MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "Dokumen Arsip", "Dokumen tidak dikenal.");
			}
		}*/
	
}
