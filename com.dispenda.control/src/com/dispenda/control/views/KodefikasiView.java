package com.dispenda.control.views;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.layout.GridData;
import org.eclipse.wb.swt.SWTResourceManager;

import com.dispenda.controller.ControllerFactory;
import com.dispenda.controller.UIController;
import com.dispenda.dao.LogImp;
import com.dispenda.model.BidangUsaha;
import com.dispenda.model.BidangUsahaProvider;
import com.dispenda.model.PajakProvider;
import com.dispenda.model.Sptpd;
import com.dispenda.model.UserModul;
import com.dispenda.object.GlobalVariable;
import com.dispenda.object.Preferences;
import com.dispenda.report.ReportAction;
import com.dispenda.report.ReportAppContext;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;

public class KodefikasiView extends ViewPart{
	public static final String ID = KodefikasiView.class.getName();
	private Color fontColor = new Color(Display.getCurrent(),Preferences.FONT_COLOR);
	private Combo cmbSubPajak;
	private Combo cmbPajak;
	private Table tbl_Log;
	private List<BidangUsaha> listUbah = new ArrayList<BidangUsaha>();
	private int idsubPajak = 0;
	private Combo cmbTipeSKP;
	private Button btnCari;
	private List<Sptpd> listLog = new ArrayList<Sptpd>();
	private Locale ind = new Locale("id", "ID");
	public KodefikasiView(){
	}
	public void createPartControl(Composite parent){
		Locale.setDefault(new Locale("id","ID"));
		ScrolledComposite scrolledComposite = new ScrolledComposite(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		
		Composite composite = new Composite(scrolledComposite, SWT.NONE);
		composite.setLayout(new GridLayout(7, false));
		
		Label lblNewLabel = new Label(composite, SWT.NONE);
		GridData gd_lblNewLabel = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblNewLabel.widthHint = 132;
		lblNewLabel.setLayoutData(gd_lblNewLabel);
		lblNewLabel.setText("Kewajiban Pajak");
		lblNewLabel.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblNewLabel.setForeground(fontColor);
		
		cmbPajak = new Combo(composite, SWT.READ_ONLY);
		cmbPajak.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(cmbPajak.getSelectionIndex() > -1)
					UIController.INSTANCE.loadBidangUsaha(cmbSubPajak, BidangUsahaProvider.INSTANCE.getBidangUsaha((Integer)cmbPajak.getData(Integer.toString(cmbPajak.getSelectionIndex()))).toArray());
				
				if (cmbPajak.getText().equalsIgnoreCase("Air tanah") || cmbPajak.getText().equalsIgnoreCase("Reklame"))
					cmbTipeSKP.select(1);
			}
		});
		cmbPajak.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		cmbPajak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		cmbPajak.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		cmbPajak.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 4, 1));
		UIController.INSTANCE.loadPajak(cmbPajak, PajakProvider.INSTANCE.getPajak().toArray());
		new Label(composite, SWT.NONE);
		
		Group grpPencarian = new Group(composite, SWT.NONE);
		grpPencarian.setLayout(new GridLayout(3, false));
		GridData gd_grpPencarian = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 2);
		gd_grpPencarian.horizontalIndent = 50;
		gd_grpPencarian.heightHint = 36;
		gd_grpPencarian.widthHint = 341;
		grpPencarian.setLayoutData(gd_grpPencarian);
		
		Label lblNoSkp = new Label(grpPencarian, SWT.NONE);
		lblNoSkp.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNoSkp.setText("No SKP/NPWPD");
		lblNoSkp.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblNoSkp.setForeground(fontColor);
		
		txtCari = new Text(grpPencarian, SWT.BORDER);
		txtCari.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.keyCode == SWT.CR || e.keyCode == SWT.KEYPAD_CR){
					cariLogNew();
				}
			}
		});
		GridData gd_txtCari = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_txtCari.widthHint = 179;
		txtCari.setLayoutData(gd_txtCari);
		txtCari.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtCari.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		btnCari = new Button(grpPencarian, SWT.NONE);
		btnCari.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				cariLogNew();
			}
		});
		GridData gd_btnCari = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnCari.widthHint = 47;
		btnCari.setLayoutData(gd_btnCari);
		btnCari.setText("Cari");
		btnCari.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		Label lblNewLabel_1 = new Label(composite, SWT.NONE);
		lblNewLabel_1.setText("Bidang Usaha");
		lblNewLabel_1.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblNewLabel_1.setForeground(fontColor);
		
		cmbSubPajak = new Combo(composite, SWT.READ_ONLY);
		cmbSubPajak.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				idsubPajak = (Integer)cmbSubPajak.getData(Integer.toString(cmbSubPajak.getSelectionIndex()));
			}
		});
		cmbSubPajak.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		cmbSubPajak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		cmbSubPajak.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		GridData gd_cmbSubPajak = new GridData(SWT.FILL, SWT.CENTER, false, false, 4, 1);
		gd_cmbSubPajak.widthHint = 237;
		cmbSubPajak.setLayoutData(gd_cmbSubPajak);
		new Label(composite, SWT.NONE);
		
		Label lblTipeSkp = new Label(composite, SWT.NONE);
		lblTipeSkp.setText("Tipe SKP");
		lblTipeSkp.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblTipeSkp.setForeground(fontColor);
		
		cmbTipeSKP = new Combo(composite, SWT.READ_ONLY);
		cmbTipeSKP.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (cmbPajak.getText().equalsIgnoreCase("Air tanah") || cmbPajak.getText().equalsIgnoreCase("Reklame"))
					cmbTipeSKP.select(1);
			}
		});
		cmbTipeSKP.setItems(new String[] {"SPTPD", "SKPD", "SKPDKB", "SKPDN"});
		cmbTipeSKP.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		cmbTipeSKP.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		cmbTipeSKP.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		GridData gd_cmbTipeSKP = new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1);
		gd_cmbTipeSKP.widthHint = 102;
		cmbTipeSKP.setLayoutData(gd_cmbTipeSKP);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		
		Button btnRevertKode = new Button(composite, SWT.NONE);
		btnRevertKode.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int status = 1;
				if (isSave()){
					if (!cmbTipeSKP.getText().equalsIgnoreCase("")){
						if (cmbTipeSKP.getText().equalsIgnoreCase("SPTPD") || cmbTipeSKP.getText().equalsIgnoreCase("SKPD")){
							for (int i=0;i<tbl_Log.getItems().length;i++){
								if (!ControllerFactory.getMainController().getCpBidangUsahaDAOImpl().setNoSPTPDLama(Integer.parseInt(tbl_Log.getItem(i).getText(1)), tbl_Log.getItem(i).getText(3))){
									MessageDialog.openError(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Error on SQL syntax");
									status = 0;
									break;
								}
							}
						}
						else{
							for (int i=0;i<tbl_Log.getItems().length;i++){
								if (!ControllerFactory.getMainController().getCpBidangUsahaDAOImpl().setNoSKPDKBLama(Integer.parseInt(tbl_Log.getItem(i).getText(1)), tbl_Log.getItem(i).getText(3))){
									MessageDialog.openError(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Error on SQL syntax");
									status = 0;
									break;
								}
							}
						}
					}else{
						MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Pilih tipe SKP");
						status = 0;
					}
					if (status == 1)
						MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Revert kode berhasil");
				}
			}
		});
		btnRevertKode.setText("Revert Kode");
		btnRevertKode.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		if (userModul.getIdUser() == 1 || userModul.getIdUser() == 2)
			btnRevertKode.setVisible(true);
		else
			btnRevertKode.setVisible(false);
		
		Button btnUbahKode = new Button(composite, SWT.NONE);
		btnUbahKode.setEnabled(false);
		btnUbahKode.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (isSave()){
					String newNoSKP = "";
					String newNoNPPD = "";
					if (isValidated()){
						if (cmbTipeSKP.getText().equalsIgnoreCase("SPTPD") || cmbTipeSKP.getText().equalsIgnoreCase("SKPD"))
							listUbah = ControllerFactory.getMainController().getCpBidangUsahaDAOImpl().getDaftarUbahKodefikasiSPTPD(idsubPajak, cmbTipeSKP.getText());
						else
							listUbah = ControllerFactory.getMainController().getCpBidangUsahaDAOImpl().getDaftarUbahKodefikasiSKPDKB(idsubPajak, cmbTipeSKP.getText());
						
						if (listUbah.size() > 0){
							createColumnOld();
							tbl_Log.removeAll();
							for (int i=0;i<listUbah.size();i++){
								newNoSKP = i+1 + "/" + listUbah.get(i).getKodeBidUsaha().replace(".", "").substring(3) + "/" + cmbTipeSKP.getText() + "/2016";
								newNoNPPD = i+1 + "/" + listUbah.get(i).getKodeBidUsaha().replace(".", "").substring(3) + "/NPPD/2016";
								TableItem item = new TableItem(tbl_Log, SWT.NONE);
								item.setText(0, String.valueOf(i+1));
								item.setText(1, listUbah.get(i).getIdSKP().toString());
								item.setText(2, listUbah.get(i).getNPWPD());
								item.setText(3, listUbah.get(i).getNoSKP());
								item.setText(4, newNoSKP);
								boolean result = false;
								if (cmbTipeSKP.getText().equalsIgnoreCase("SPTPD") || cmbTipeSKP.getText().equalsIgnoreCase("SKPD"))
									result = ControllerFactory.getMainController().getCpBidangUsahaDAOImpl().updateKodefikasiSPTPD(listUbah.get(i).getIdSKP(), newNoSKP, newNoNPPD);
								else{
									if (ControllerFactory.getMainController().getCpBidangUsahaDAOImpl().updateKodefikasiSKPDKB(listUbah.get(i).getIdSKP(), newNoSKP, newNoNPPD))
										result = ControllerFactory.getMainController().getCpBidangUsahaDAOImpl().updateKodefikasiMohon(listUbah.get(i).getNoSKP(), listUbah.get(i).getNPWPD(), newNoSKP);
								}
								if (result){
									StringBuffer sb = new StringBuffer();
									sb.append("SAVE " +
											" IDSKP: " + listUbah.get(i).getIdSKP() +
											"; NPWPD: "+ listUbah.get(i).getNPWPD() +
											"; IDSub: " + idsubPajak + 
											"; NoSKP: " + listUbah.get(i).getNoSKP() + " - " + newNoSKP);
									new LogImp().setLog(sb.toString(), userModul.getDisplayName(), userModul.getSubModul().getNamaModul());
								}
							}
						}else{
							MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Data tidak ada");
						}
					}else
						MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Silahkan isi semua data");
				}else
					MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Anda tidak memiliki hak untuk mengubah data.");
			}
		});
		btnUbahKode.setText("Ubah Kode");
		btnUbahKode.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		if (userModul.getIdUser() == 1 || userModul.getIdUser() == 2)
			btnUbahKode.setEnabled(true);
		
		Button btnLihatLog = new Button(composite, SWT.NONE);
		btnLihatLog.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String tipeSkp = "";
				if (cmbTipeSKP.getText().equalsIgnoreCase("SPTPD") || tipeSkp.equalsIgnoreCase("SKPD"))
					tipeSkp = "SPTPD";
				else if (cmbTipeSKP.getText().equalsIgnoreCase("SKPDKB") || tipeSkp.equalsIgnoreCase("SKPDKBT"))
					tipeSkp = "SKPDKB";
				else
					tipeSkp = "";
				
				if (!cmbSubPajak.getText().equalsIgnoreCase("")){
					if (tipeSkp.equalsIgnoreCase("")){
						listLog = new LogImp().getLogKodefikasibyIDSubPajak("SPTPD", idsubPajak);
						listLog.addAll(new LogImp().getLogKodefikasibyIDSubPajak("SKPDKB", idsubPajak));
//						Collections.sort(listLog);
					}else{
						listLog = new LogImp().getLogKodefikasibyIDSubPajak(tipeSkp, idsubPajak);
					}
				}
				else if (!cmbPajak.getText().equalsIgnoreCase("")){
					if (tipeSkp.equalsIgnoreCase("")){
						listLog = new LogImp().getLogKodefikasibyIDPajak("SPTPD", String.valueOf(cmbPajak.getData(Integer.toString(cmbPajak.getSelectionIndex()))));
						listLog.addAll(new LogImp().getLogKodefikasibyIDPajak("SKPDKB", String.valueOf(cmbPajak.getData(Integer.toString(cmbPajak.getSelectionIndex())))));
//						Collections.sort(listLog);
					}else{
						listLog = new LogImp().getLogKodefikasibyIDPajak(tipeSkp, String.valueOf(cmbPajak.getData(Integer.toString(cmbPajak.getSelectionIndex()))));
					}
				}else{
					if (tipeSkp.equalsIgnoreCase("")){
						listLog = new LogImp().getLogKodefikasiAll("SPTPD");
						listLog.addAll(new LogImp().getLogKodefikasiAll("SKPDKB"));
//						Collections.sort(listLog);
					}else{
						listLog = new LogImp().getLogKodefikasiAll(tipeSkp);
					}
				}
				
				createColumnNew();
				tbl_Log.removeAll();
				if (listLog.size() == 0)
					MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Data tidak ada");
				else{
					loadTable();
				}
			}
		});
		btnLihatLog.setText("Lihat Log");
		btnLihatLog.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		Button btnReset = new Button(composite, SWT.NONE);
		btnReset.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				cmbPajak.deselectAll();
				cmbSubPajak.deselectAll();
				cmbTipeSKP.deselectAll();
				cmbSubPajak.removeAll();
				txtCari.setText("");
				tbl_Log.removeAll();
			}
		});
		btnReset.setText("Reset");
		btnReset.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		Button btnCetak = new Button(composite, SWT.NONE);
		btnCetak.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ReportAppContext.name = "Variable";
				String tipeSKP = "";
				String pajak = "";
				if (cmbTipeSKP.getText().equalsIgnoreCase(""))
					tipeSKP = "SPTPD/SKPD/SKPDKB/SKPDN";
				else
					tipeSKP = cmbTipeSKP.getText();
				
				if (cmbPajak.getText().equalsIgnoreCase(""))
					pajak = "";
				else{
					pajak = "Pajak " + cmbPajak.getText();
					if (!cmbSubPajak.getText().equalsIgnoreCase(""))
						pajak += " Sub Pajak " + cmbSubPajak.getText();
				}
				
				ReportAppContext.map.put("tipeSKP", tipeSKP);
				ReportAppContext.map.put("pajak", pajak);
				ReportAppContext.nameObject = "Daftar_Kodefikasi";
				ReportAppContext.classLoader = KodefikasiView.class.getClassLoader();
				if(!ReportAppContext.object.isEmpty())
					ReportAppContext.object.clear();
				for(int j = 0;j<tbl_Log.getItems().length;j++){
					List<String> values = new ArrayList<String>();
					for(int i=0;i<tbl_Log.getColumnCount();i++){
						values.add(tbl_Log.getItem(j).getText(i));
					}
					ReportAppContext.object.put(Integer.toString(j), values);
				}
				ReportAction.start("DaftarKodefikasi.rptdesign");
			}
		});
		GridData gd_btnCetak = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnCetak.widthHint = 58;
		btnCetak.setLayoutData(gd_btnCetak);
		btnCetak.setText("Cetak");
		btnCetak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		
		tbl_Log = new Table(composite, SWT.BORDER | SWT.FULL_SELECTION | SWT.VIRTUAL);
		tbl_Log.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		tbl_Log.setForeground(SWTResourceManager.getColor(0, 0, 0));
		tbl_Log.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE-1, SWT.NORMAL));
		tbl_Log.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 7, 2));
		tbl_Log.setHeaderVisible(true);
		tbl_Log.setLinesVisible(true);
		
		scrolledComposite.setContent(composite);
		scrolledComposite.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}
	
	public void setFocus(){
		
	}
	
	public boolean isValidated(){
		if (cmbSubPajak.getSelectionIndex() < 0 || cmbTipeSKP.getSelectionIndex() < 0)
			return false;
		else
			return true;
	}
	
	private void createColumnOld()
	{
		if(tbl_Log.getColumnCount() <= 0){
			String[] listColumn = {"No","IDSKP", "NPWPD", "No SKP Lama", "No SKP Baru"};
			int[] columnWidth = {50, 70, 110, 140, 180};
			for (int i=listColumn.length - 1;i>=0;i--)
			{
				TableColumn colPajak = new TableColumn(tbl_Log, SWT.NONE,0);
				colPajak.setText(listColumn[i]);
				colPajak.setWidth(columnWidth[i]);
			}
		}
	}
	
	private void createColumnNew()
	{
		if(tbl_Log.getColumnCount() <= 0){
			String[] listColumn = {"No", "NPWPD", "No SKP Lama", "No SKP Baru", "No NPPD", "Tanggal SKP", "Masa Pajak"};
			int[] columnWidth = {50, 110, 130, 170, 170, 110, 180};
			for (int i=listColumn.length - 1;i>=0;i--)
			{
				TableColumn colPajak = new TableColumn(tbl_Log, SWT.NONE,0);
				colPajak.setText(listColumn[i]);
				colPajak.setWidth(columnWidth[i]);
			}
		}
	}
	
	private boolean checkString(){
		if (txtCari.getText().contains("SAVE") || txtCari.getText().contains("IDSKP") ||
			txtCari.getText().contains("NPWPD") || txtCari.getText().contains("IDSub") ||
			txtCari.getText().contains("NoSKP"))
			return false;
		else
			return true;
	}
	
	private void cariLogNew(){
		listLog = new LogImp().getCariKodefikasiNew(txtCari.getText().toUpperCase());
		createColumnNew();
		tbl_Log.removeAll();
		loadTable();
	}
	
	private void cariLogOld(){
		if (checkString()){
			String[] result = new LogImp().getCariKodefikasiOld(txtCari.getText().toUpperCase());
			createColumnOld();
			tbl_Log.removeAll();
			for (int i=0;i<result.length;i++){
				String noSKP[] = result[i].split(";")[3].substring(8).split("-");
				TableItem item = new TableItem(tbl_Log, SWT.NONE);
				item.setText(0, String.valueOf(i+1));
				item.setText(1, result[i].split(";")[0].substring(13));
				item.setText(2, result[i].split(";")[1].substring(8));
				item.setText(3, noSKP[0]);
				item.setText(4, noSKP[1]);
			}
		}
	}
	
	private void lihatLog(){
		String search = "";
		if (!cmbSubPajak.getText().equalsIgnoreCase(""))
			search = "IDSub: " + idsubPajak;
		else if (!cmbPajak.getText().equalsIgnoreCase(""))
			search = "NPWPD: " + (cmbPajak.getSelectionIndex() + 2);
		
		if (!cmbTipeSKP.getText().equalsIgnoreCase(""))
			if (search.equalsIgnoreCase(""))
				search = "/" + cmbTipeSKP.getText() + "/";
			else
				search += "%' and ACTION like '%/" + cmbTipeSKP.getText() + "/";
		
		String[] result = new LogImp().getLogKodefikasiOld(search);
		createColumnOld();
		tbl_Log.removeAll();
		if (result.length == 0)
			MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Data tidak ada");
		else{
			for (int i=0;i<result.length;i++){
				String noSKP[] = result[i].split(";")[3].substring(8).split("-");
				TableItem item = new TableItem(tbl_Log, SWT.NONE);
				item.setText(0, String.valueOf(i+1));
				item.setText(1, result[i].split(";")[0].substring(13));
				item.setText(2, result[i].split(";")[1].substring(8));
				item.setText(3, noSKP[0]);
				item.setText(4, noSKP[1]);
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	private void loadTable(){
		for (int i=0;i<listLog.size();i++){
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			String tglSkp = sdf.format(listLog.get(i).getTanggalSPTPD());
			String masaPajak = "";
			if (listLog.get(i).getMasaPajakDari().getMonth() + 1 == listLog.get(i).getMasaPajakSampai().getMonth() + 1 &&
				listLog.get(i).getMasaPajakDari().getYear() == listLog.get(i).getMasaPajakSampai().getYear()){
				masaPajak = UIController.INSTANCE.formatMonth(listLog.get(i).getMasaPajakDari().getMonth() + 1, Locale.getDefault()) + " " + (listLog.get(i).getMasaPajakDari().getYear() + 1900);
			}else{
				masaPajak = UIController.INSTANCE.formatMonth(listLog.get(i).getMasaPajakDari().getMonth() + 1, Locale.getDefault()) + " " + (listLog.get(i).getMasaPajakDari().getYear() + 1900) +
				" - " + UIController.INSTANCE.formatMonth(listLog.get(i).getMasaPajakSampai().getMonth() + 1, Locale.getDefault()) + " " + (listLog.get(i).getMasaPajakSampai().getYear() + 1900);
			}
			
			TableItem item = new TableItem(tbl_Log, SWT.NONE);
			item.setText(0, String.valueOf(i+1));
			item.setText(1, listLog.get(i).getNPWPD());
			item.setText(2, listLog.get(i).getNoSPTPDLama());
			item.setText(3, listLog.get(i).getNoSPTPD());
			item.setText(4, listLog.get(i).getNoNPPD());
			item.setText(5, tglSkp);
			item.setText(6, masaPajak);
		}
		
		
	}
	
	private UserModul userModul = ControllerFactory.getMainController().getCpUserDAOImpl()
			.getHMUserModul(GlobalVariable.userModul.getIdUser()).get(35);
	private Text txtCari;
	
	private boolean isSave(){		
		return userModul.getSimpan();
	}
}
