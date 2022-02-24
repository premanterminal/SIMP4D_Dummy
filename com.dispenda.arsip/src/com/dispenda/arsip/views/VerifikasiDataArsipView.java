package com.dispenda.arsip.views;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.operations.RedoActionHandler;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.SWTResourceManager;

import com.dispenda.controller.ControllerFactory;
import com.dispenda.controller.UIController;
import com.dispenda.model.Arsip;
import com.dispenda.model.Sptpd;
import com.dispenda.object.Preferences;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.ModifyEvent;

public class VerifikasiDataArsipView extends ViewPart {
	public VerifikasiDataArsipView() {
	}
	
	public static final String ID = VerifikasiDataArsipView.class.getName();
	private Color fontColor = new Color(Display.getCurrent(),Preferences.FONT_COLOR);
	private Text txtNPWPD;
	private Table tblArsip;
	private List<Arsip> listMapArsip = new ArrayList<Arsip>();
	private List<Sptpd> listArsip;
	private Text txtRak;
	private Label lblNamaRak;
	private Label lblNewLabel;
	private Text txtBox;
	private Label lblNoMap;
	private Text txtMap;
	private Text txtNamaBadan;
	private Label lblNamaBadan;
	private Label lblAlamat;
	private Text txtAlamat;
	private Button btnCheckSemua;
	private Button btnBaru;
	private Button btnKeluar;
	private Label lblCari;
	private Text txtCari;
	private Button btnCari;
	private int countStart;
	
	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new GridLayout(1, false));
		
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		composite.setLayout(new GridLayout(8, false));
		
		Label lblNpwpd = new Label(composite, SWT.NONE);
		lblNpwpd.setText("NPWPD");
		lblNpwpd.setForeground(fontColor);
		lblNpwpd.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		txtNPWPD = new Text(composite, SWT.BORDER);
		txtNPWPD.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.keyCode == SWT.CR || e.keyCode == SWT.KEYPAD_CR){
					getDataArsip();
					getDetailArsip();
					setTableColour();
				}
			}
		});
		GridData gd_txtNPWPD = new GridData(SWT.LEFT, SWT.CENTER, false, false, 5, 1);
		gd_txtNPWPD.widthHint = 187;
		txtNPWPD.setLayoutData(gd_txtNPWPD);
		txtNPWPD.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtNPWPD.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		txtNPWPD.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		
		lblNamaBadan = new Label(composite, SWT.NONE);
		lblNamaBadan.setText("Nama Badan");
		lblNamaBadan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblNamaBadan.setForeground(fontColor);
		
		txtNamaBadan = new Text(composite, SWT.BORDER);
		txtNamaBadan.setEditable(false);
		txtNamaBadan.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		txtNamaBadan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtNamaBadan.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtNamaBadan.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 3, 1));
		
		lblAlamat = new Label(composite, SWT.NONE);
		lblAlamat.setText("Alamat");
		lblAlamat.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblAlamat.setForeground(fontColor);
		
		txtAlamat = new Text(composite, SWT.BORDER);
		txtAlamat.setEditable(false);
		GridData gd_txtAlamat = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_txtAlamat.widthHint = 280;
		txtAlamat.setLayoutData(gd_txtAlamat);
		txtAlamat.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		txtAlamat.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtAlamat.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		
		lblNamaRak = new Label(composite, SWT.NONE);
		lblNamaRak.setText("Nama Rak");
		lblNamaRak.setForeground(fontColor);
		lblNamaRak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		txtRak = new Text(composite, SWT.BORDER);
		GridData gd_txtRak = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_txtRak.widthHint = 132;
		txtRak.setLayoutData(gd_txtRak);
		txtRak.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		txtRak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtRak.setEditable(false);
		txtRak.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		
		lblNewLabel = new Label(composite, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel.setText("No Box");
		lblNewLabel.setForeground(fontColor);
		lblNewLabel.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		txtBox = new Text(composite, SWT.BORDER);
		txtBox.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		txtBox.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		txtBox.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtBox.setEditable(false);
		txtBox.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		
		lblNoMap = new Label(composite, SWT.NONE);
		lblNoMap.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNoMap.setText("No Map");
		lblNoMap.setForeground(fontColor);
		lblNoMap.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		txtMap = new Text(composite, SWT.BORDER);
		txtMap.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		txtMap.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtMap.setEditable(false);
		txtMap.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		
		lblCari = new Label(composite, SWT.NONE);
		lblCari.setText("Cari");
		lblCari.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblCari.setForeground(fontColor);
		
		txtCari = new Text(composite, SWT.BORDER);
		txtCari.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.keyCode == SWT.CR || e.keyCode == SWT.KEYPAD_CR){
					cariData();
				}
			}
		});
		txtCari.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				countStart = 0;
			}
		});
		txtCari.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		txtCari.setFont(SWTResourceManager.getFont("", 0, SWT.NORMAL));
		txtCari.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtCari.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		txtCari.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		btnCari = new Button(composite, SWT.NONE);
		btnCari.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				cariData();
			}
		});
		GridData gd_btnCari = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnCari.widthHint = 58;
		btnCari.setLayoutData(gd_btnCari);
		btnCari.setText("Cari");
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		
		tblArsip = new Table(composite, SWT.BORDER | SWT.CHECK | SWT.FULL_SELECTION);
		tblArsip.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 8, 1));
		tblArsip.setHeaderVisible(true);
		tblArsip.setLinesVisible(true);
		tblArsip.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		tblArsip.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		tblArsip.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		btnCheckSemua = new Button(composite, SWT.CHECK);
		btnCheckSemua.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (btnCheckSemua.getSelection()){
					for (int i=0;i<tblArsip.getItemCount();i++){
						tblArsip.getItem(i).setChecked(true);
					}
				}else{
					for (int i=0;i<tblArsip.getItemCount();i++){
						tblArsip.getItem(i).setChecked(false);
					}
				}
			}
		});
		btnCheckSemua.setText("Check Semua");
		btnCheckSemua.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnCheckSemua.setForeground(fontColor);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		
		btnBaru = new Button(composite, SWT.NONE);
		btnBaru.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				txtNPWPD.setText("");
				txtAlamat.setText("");
				txtBox.setText("");
				txtMap.setText("");
				txtNamaBadan.setText("");
				txtRak.setText("");
				tblArsip.clearAll();
				txtNPWPD.setFocus();
				//txtNPWPD.selectAll();
				deleteColumns();
			}
		});
		GridData gd_btnBaru = new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1);
		gd_btnBaru.widthHint = 84;
		btnBaru.setLayoutData(gd_btnBaru);
		btnBaru.setText("Baru");
		
		btnKeluar = new Button(composite, SWT.NONE);
		btnKeluar.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (MessageDialog.openQuestion(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Keluarkan Arsip?")){
					for (int i=0; i<tblArsip.getItemCount();i++){
						if (tblArsip.getItem(i).getChecked()){
							if (tblArsip.getItem(i).getText(1).contains("SPTPD") || tblArsip.getItem(i).getText(1).contains("SKPD/")){
								int pos = ControllerFactory.getMainController().getCpArsipDAOImpl().getPos("POS_END","SPTPD","NO_SPTPD",tblArsip.getItem(i).getText(1), txtNPWPD.getText());
								if (ControllerFactory.getMainController().getCpArsipDAOImpl().updatePos("SPTPD","NO_SPTPD",tblArsip.getItem(i).getText(1), txtNPWPD.getText(), pos, 0))
									tblArsip.getItem(i).setText(3, "0");
							}
							else if (tblArsip.getItem(i).getText(1).contains("SKPDKB/") || tblArsip.getItem(i).getText(1).contains("SKPDKBT")){
								int pos = ControllerFactory.getMainController().getCpArsipDAOImpl().getPos("POS_END","PERIKSA","NO_SKP",tblArsip.getItem(i).getText(1), txtNPWPD.getText());
								if (ControllerFactory.getMainController().getCpArsipDAOImpl().updatePos("PERIKSA","NO_SKP",tblArsip.getItem(i).getText(1), txtNPWPD.getText(), pos, 0))
									tblArsip.getItem(i).setText(3, "0");
							}
							else if (tblArsip.getItem(i).getText(1).contains("SSPD")){
								int pos = ControllerFactory.getMainController().getCpArsipDAOImpl().getPos("POS_END","SSPD","NO_SSPD",tblArsip.getItem(i).getText(1), txtNPWPD.getText());
								if (ControllerFactory.getMainController().getCpArsipDAOImpl().updatePos("SSPD","NO_SSPD",tblArsip.getItem(i).getText(1), txtNPWPD.getText(), pos, 0))
									tblArsip.getItem(i).setText(3, "0");
							}
						}
						if (i==tblArsip.getItemCount() - 1)
							MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Pengarsipan Dokumen selesai");
					}
					txtNPWPD.selectAll();
				}
			}
		});
		GridData gd_btnKeluar = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnKeluar.widthHint = 81;
		btnKeluar.setLayoutData(gd_btnKeluar);
		btnKeluar.setText("Keluar");
		
		Button btnSimpan = new Button(composite, SWT.NONE);
		GridData gd_btnSimpan = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_btnSimpan.widthHint = 90;
		btnSimpan.setLayoutData(gd_btnSimpan);
		btnSimpan.setText("Simpan");
		btnSimpan.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
//				super.widgetSelected(e);
				if (MessageDialog.openQuestion(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Simpan Arsip?")){
					for (int i=0; i<tblArsip.getItemCount();i++){
						if (tblArsip.getItem(i).getChecked()){
							if (tblArsip.getItem(i).getText(1).contains("SPTPD") || tblArsip.getItem(i).getText(1).contains("SKPD/")){
								int pos = ControllerFactory.getMainController().getCpArsipDAOImpl().getPos("POS_END","SPTPD","NO_SPTPD",tblArsip.getItem(i).getText(1), txtNPWPD.getText());
								if(pos==0 || pos==4 || pos==2){
									if (ControllerFactory.getMainController().getCpArsipDAOImpl().updatePos("SPTPD","NO_SPTPD",tblArsip.getItem(i).getText(1), txtNPWPD.getText(), pos, 3))
										tblArsip.getItem(i).setText(3, "3");
								}else{
									MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Dokumen " + tblArsip.getItem(i).getText(1) + " TIDAK berada pada posisi yang benar.");
								}
							}
							else if (tblArsip.getItem(i).getText(1).contains("SKPDKB/") || tblArsip.getItem(i).getText(1).contains("SKPDKBT")){
								int pos = ControllerFactory.getMainController().getCpArsipDAOImpl().getPos("POS_END","PERIKSA","NO_SKP",tblArsip.getItem(i).getText(1), txtNPWPD.getText());
								if(pos==0 || pos==4 || pos==2){
									if (ControllerFactory.getMainController().getCpArsipDAOImpl().updatePos("PERIKSA","NO_SKP",tblArsip.getItem(i).getText(1), txtNPWPD.getText(), pos, 3))
										tblArsip.getItem(i).setText(3, "3");
								}else{
									MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Dokumen " + tblArsip.getItem(i).getText(1) + " TIDAK berada pada posisi yang benar.");
								}
							}
							else if (tblArsip.getItem(i).getText(1).contains("SSPD")){
								int pos = ControllerFactory.getMainController().getCpArsipDAOImpl().getPos("POS_END","SSPD","NO_SSPD",tblArsip.getItem(i).getText(1), txtNPWPD.getText());
								if(pos==0 || pos==4 || pos==2){
									if (ControllerFactory.getMainController().getCpArsipDAOImpl().updatePos("SSPD","NO_SSPD",tblArsip.getItem(i).getText(1), txtNPWPD.getText(), pos, 3))
										tblArsip.getItem(i).setText(3, "3");
								}else{
									MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Dokumen " + tblArsip.getItem(i).getText(1) + " TIDAK berada pada posisi yang benar.");
								}
							}
						}
						if (i==tblArsip.getItemCount() - 1)
							MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Pengarsipan Dokumen selesai");
					}
					txtNPWPD.selectAll();
				}
			}
		});
	}
	
	private void getDataArsip(){
		listMapArsip = ControllerFactory.getMainController().getCpArsipDAOImpl().getArsip("NPWPD", txtNPWPD.getText());
		if (listMapArsip != null && listMapArsip.size()>0){
//			createColumnNPWPD();
			for (int i=0;i<listMapArsip.size();i++){
				txtNPWPD.setText(listMapArsip.get(0).getNPWPD());
				txtNamaBadan.setText(listMapArsip.get(0).getNamaBadan());
				txtAlamat.setText(listMapArsip.get(0).getAlamat());
				txtRak.setText(listMapArsip.get(0).getNamaRak());
				txtBox.setText(String.valueOf(listMapArsip.get(0).getNoBox()));
				txtMap.setText(String.valueOf(listMapArsip.get(0).getNoMap()));
			}
		}else{
			MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Data Arsip tidak ditemukan");
			txtRak.setText("");
			txtBox.setText("");
			txtMap.setText("");	
			txtNamaBadan.setText("");
			txtAlamat.setText("");
			tblArsip.removeAll();
		}
	}
	
	@SuppressWarnings("deprecation")
	private void getDetailArsip(){
		tblArsip.removeAll();
		listArsip = ControllerFactory.getMainController().getCpSptpdDAOImpl().getMasaPajakSKPD(txtNPWPD.getText());
		listArsip.addAll(ControllerFactory.getMainController().getCpSptpdDAOImpl().getMasaPajakSKPDKB(txtNPWPD.getText()));
		listArsip.addAll(ControllerFactory.getMainController().getCpArsipDAOImpl().getSspd(txtNPWPD.getText()));
		listArsip.addAll(ControllerFactory.getMainController().getCpArsipDAOImpl().getSspd_SKPDKB(txtNPWPD.getText()));
		Collections.sort(listArsip);
		createColumnBerkas();
		
		if (listArsip.size() > 0)
		{	
			for(int i = 0; i<listArsip.size(); i++){
				TableItem ti = new TableItem(tblArsip, SWT.NONE);
				if (listArsip.get(i).getMasaPajakDari().getMonth() == listArsip.get(i).getMasaPajakSampai().getMonth() && listArsip.get(i).getMasaPajakDari().getYear() == listArsip.get(i).getMasaPajakSampai().getYear())
				{
					ti.setText(new String[] {"", listArsip.get(i).getNoSPTPD(), UIController.INSTANCE.formatMonth(listArsip.get(i).getMasaPajakDari().getMonth() + 1, Locale.getDefault()) + " " + (listArsip.get(i).getMasaPajakDari().getYear() + 1900), listArsip.get(i).getPosEnd().toString(), listArsip.get(i).getNoNPPD()});
				}
				else
				{
					ti.setText(new String[] {"", listArsip.get(i).getNoSPTPD(), UIController.INSTANCE.formatMonth(listArsip.get(i).getMasaPajakDari().getMonth() + 1, Locale.getDefault()) + " " + (listArsip.get(i).getMasaPajakDari().getYear() + 1900) + " - " + 
							UIController.INSTANCE.formatMonth(listArsip.get(i).getMasaPajakSampai().getMonth() + 1, Locale.getDefault()) + " " + (listArsip.get(i).getMasaPajakSampai().getYear() + 1900), listArsip.get(i).getPosEnd().toString(), listArsip.get(i).getNoNPPD()});
				}
			}
		}
	}
	
	private void setTableColour(){
		for(int i = 0; i<listArsip.size(); i++){
			if (tblArsip.getItem(i).getText(3).equalsIgnoreCase("3")){
				tblArsip.getItem(i).setBackground(SWTResourceManager.getColor(SWT.COLOR_GREEN));
			}
		}
	}
	
	private void createColumnBerkas(){
		if(tblArsip.getColumnCount() <= 0){
			TableColumn colPajak4 = new TableColumn(tblArsip, SWT.NONE,0);
			colPajak4.setText("No Reg");
			colPajak4.setWidth(90);
			TableColumn colPajak = new TableColumn(tblArsip, SWT.NONE,0);
			colPajak.setText("Posisi Berkas");
			colPajak.setWidth(120);
			TableColumn colPajak1 = new TableColumn(tblArsip, SWT.NONE,0);
			colPajak1.setText("Masa Pajak");
			colPajak1.setWidth(280);
			TableColumn colPajak2 = new TableColumn(tblArsip, SWT.NONE,0);
			colPajak2.setText("No Berkas");
			colPajak2.setWidth(180);
			TableColumn colPajak3 = new TableColumn(tblArsip, SWT.NONE,0);
			colPajak3.setText("");
			colPajak3.setWidth(40);
		}
	}
	
	private void cariData(){
		TableItem[] items = tblArsip.getItems();
		for (int i=countStart;i<items.length;i++){
			if (items[i].getText(1).toUpperCase().contains(txtCari.getText().toUpperCase()) || items[i].getText(2).toUpperCase().contains(txtCari.getText().toUpperCase())){
				tblArsip.setSelection(i);
				tblArsip.setFocus();
				countStart = i + 1;
				if (i == items.length - 1)
					countStart = 0;
				break;
			}
			
			if (i == items.length - 1)
				countStart = 0;
		}
	}
	
	private void deleteColumns(){
		for(TableItem itm : tblArsip.getItems()){
			itm.dispose();
		}
	}

	@Override
	public void setFocus() {

	}

}
