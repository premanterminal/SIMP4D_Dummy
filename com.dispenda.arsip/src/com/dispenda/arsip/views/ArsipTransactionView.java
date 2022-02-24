package com.dispenda.arsip.views;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.SWTResourceManager;

import com.dispenda.arsip.dialog.DetailKeluarMasukDialog;
import com.dispenda.arsip.dialog.PeminjamanArsipDialog;
import com.dispenda.controller.ControllerFactory;
import com.dispenda.controller.UIController;
import com.dispenda.model.ArsipLog;
import com.dispenda.model.ArsipLogProvider;
import com.dispenda.object.GlobalVariable;
import com.dispenda.object.Preferences;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.events.VerifyEvent;

public class ArsipTransactionView extends ViewPart {
	public ArsipTransactionView() {
	}
	public static final String ID = ArsipTransactionView.class.getName();
	private Color fontColor = new Color(Display.getCurrent(),Preferences.FONT_COLOR);
	private Text txtBarcodeMasuk;
	private Table tblMasuk;
	private Label lblMasuk;
	private Label label;
	private Label lblKeluar;
	private Label lblBarcodeKeluar;
	private Text txtBarcodeKeluar;
	private Table tblKeluar;
	private java.sql.Timestamp dateNow;
	@Override
	public void createPartControl(Composite parent) {
		dateNow = (java.sql.Timestamp)ControllerFactory.getMainController().getCpWajibPajakDAOImpl().getDateNow();
		parent.setLayout(new GridLayout(5, false));
		
		lblMasuk = new Label(parent, SWT.NONE);
		lblMasuk.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 2, 1));
		lblMasuk.setText("MASUK");
		lblMasuk.setForeground(fontColor);
		lblMasuk.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, 20, SWT.BOLD));
		
		label = new Label(parent, SWT.SEPARATOR | SWT.VERTICAL);
		label.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, true, 1, 3));
		
		lblKeluar = new Label(parent, SWT.NONE);
		lblKeluar.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 2, 1));
		lblKeluar.setText("KELUAR");
		lblKeluar.setForeground(fontColor);
		lblKeluar.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, 20, SWT.BOLD));
		
		Label lblBarcodeMasuk = new Label(parent, SWT.NONE);
		lblBarcodeMasuk.setText("Barcode");
		lblBarcodeMasuk.setForeground(fontColor);
		lblBarcodeMasuk.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		txtBarcodeMasuk = new Text(parent, SWT.BORDER | SWT.SEARCH);
		txtBarcodeMasuk.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				String[] tahun = txtBarcodeMasuk.getText().split("/");
				if (tahun.length > 2 && tahun[tahun.length-1].matches("\\d+")){
					if(Integer.parseInt(tahun[tahun.length-1]) > 2000)
						setMasuk(txtBarcodeMasuk.getText().toUpperCase());
				}
			}
		});
		txtBarcodeMasuk.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		txtBarcodeMasuk.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtBarcodeMasuk.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		txtBarcodeMasuk.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtBarcodeMasuk.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.keyCode==SWT.CR || e.keyCode==SWT.KEYPAD_CR){
					setMasuk(txtBarcodeMasuk.getText().toUpperCase());
				}
			}
		});
		
		lblBarcodeKeluar = new Label(parent, SWT.NONE);
		lblBarcodeKeluar.setText("Barcode");
		lblBarcodeKeluar.setForeground(fontColor);
		lblBarcodeKeluar.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		txtBarcodeKeluar = new Text(parent, SWT.BORDER);
		txtBarcodeKeluar.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				String[] tahun = txtBarcodeKeluar.getText().split("/");
				if (tahun.length > 2 && tahun[tahun.length-1].matches("\\d+")){
					if(Integer.parseInt(tahun[tahun.length-1]) > 2000)
						setMasuk(txtBarcodeKeluar.getText().toUpperCase());
				}
			}
		});
		txtBarcodeKeluar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		txtBarcodeKeluar.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtBarcodeKeluar.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		txtBarcodeKeluar.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtBarcodeKeluar.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.keyCode==SWT.CR || e.keyCode==SWT.KEYPAD_CR){
					setMasuk(txtBarcodeKeluar.getText().toUpperCase());
					/*String[] split = txtBarcodeKeluar.getText().split("/S");
					String regex = "\\d+/+\\d+";
					if(split[0].matches(regex)){
						if(txtBarcodeKeluar.getText().contains("/SKPD/")){
							int pos = ControllerFactory.getMainController().getCpArsipDAOImpl().getPos("POS_END","SPTPD","NO_SPTPD",txtBarcodeKeluar.getText());
							if(pos==3){
								ControllerFactory.getMainController().getCpArsipDAOImpl().updatePos("SPTPD","NO_SPTPD",txtBarcodeKeluar.getText(), pos, 4);
								TableItem ti = new TableItem(tblKeluar, SWT.NONE);
								ti.setText(new String[]{txtBarcodeKeluar.getText(),"",""});
								ti.setBackground(SWTResourceManager.getColor(SWT.COLOR_RED));
								txtBarcodeKeluar.setText("");
							}else{
								MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "Dokumen Arsip", "Dokumen yang ada input TIDAK berada pada posisi yang benar.");
								txtBarcodeKeluar.setText("");
							}
						}else if(txtBarcodeKeluar.getText().contains("/SKPDKB/") || 
								txtBarcodeKeluar.getText().contains("/SKPDN/")){
							// update posisi dokumen
							int pos = ControllerFactory.getMainController().getCpArsipDAOImpl().getPos("POS_END","PERIKSA","NO_SKP",txtBarcodeKeluar.getText());
							if(pos==3){
								ControllerFactory.getMainController().getCpArsipDAOImpl().updatePos("PERIKSA","NO_SKP",txtBarcodeKeluar.getText(), pos, 4);
								TableItem ti = new TableItem(tblKeluar, SWT.NONE);
								ti.setText(new String[]{txtBarcodeKeluar.getText(),"",""});
								ti.setBackground(SWTResourceManager.getColor(SWT.COLOR_RED));
								txtBarcodeKeluar.setText("");
							}else{
								MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "Dokumen Arsip", "Dokumen yang ada input TIDAK berada pada posisi yang benar.");
								txtBarcodeKeluar.setText("");
							}
						}else if(txtBarcodeKeluar.getText().contains("/SSPD/")){
							// update posisi dokumen
							ControllerFactory.getMainController().getCpArsipDAOImpl().updatePos("SSPD",txtBarcodeKeluar.getText(), 3, 4);
						}else{
							// Dokumen tidak dikenal
						}
					}else{
						DetailKeluarMasukDialog dialog = new DetailKeluarMasukDialog(Display.getCurrent().getActiveShell());
						String tipeSKP = "";
						String tipeColomn = "";
						if(txtBarcodeKeluar.getText().contains("/SKPD/")){
							ResultSet result = ControllerFactory.getMainController().getCpArsipDAOImpl().getSKPD(txtBarcodeKeluar.getText());
							dialog.setResult(result);
							tipeSKP = "SPTPD";
							tipeColomn = "NO_SPTPD";
						}else if(txtBarcodeKeluar.getText().contains("/SKPDKB/") || 
								txtBarcodeKeluar.getText().contains("/SKPDN/")){
							ResultSet result = ControllerFactory.getMainController().getCpArsipDAOImpl().getSKPDKB(txtBarcodeKeluar.getText());
							dialog.setResult(result);
							tipeSKP = "PERIKSA";
							tipeColomn = "NO_SKP";
						}else if(txtBarcodeKeluar.getText().contains("/SSPD/")){
							ResultSet result = ControllerFactory.getMainController().getCpArsipDAOImpl().getSSPD(txtBarcodeKeluar.getText());
							dialog.setResult(result);
							tipeSKP = "SSPD";
							tipeColomn = "NO_SSPD";
						}else{
							// Dokumen tidak dikenal
							dialog.setResult(null);
							tipeSKP = "";
						}
						dialog.open();
						if(GlobalVariable.INSTANCE.item!=null){
							// update posisi dokumen
							String[] item = GlobalVariable.INSTANCE.item;
							int pos = ControllerFactory.getMainController().getCpArsipDAOImpl().getPos("POS_END",tipeSKP,tipeColomn,txtBarcodeKeluar.getText(),item[4]);
							if(pos==3){
								ControllerFactory.getMainController().getCpArsipDAOImpl().updatePos(tipeSKP,tipeColomn,item[0], item[4], pos, 4);
								TableItem ti = new TableItem(tblKeluar, SWT.NONE);
								ti.setText(new String[]{item[0],item[4],item[5]});
								ti.setBackground(SWTResourceManager.getColor(SWT.COLOR_RED));
								txtBarcodeKeluar.setText("");
							}else{
								MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "Dokumen Arsip", "Dokumen yang ada input TIDAK berada pada posisi yang benar.");
								txtBarcodeKeluar.setText("");
							}
						}else{
							MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "Dokumen Arsip", "Dokumen yang ada input TIDAK ADA dalam arsip.");
							txtBarcodeKeluar.setText("");
						}
					}*/
				}
			}
		});
		
		tblMasuk = new Table(parent, SWT.BORDER | SWT.FULL_SELECTION);
		tblMasuk.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		tblMasuk.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		tblMasuk.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE-1, SWT.NORMAL));
		tblMasuk.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		tblMasuk.setHeaderVisible(true);
		tblMasuk.setLinesVisible(true);
		String[] colHeader = new String[]{"No. Dokumen","NPWPD","Nama Badan"};
		int[] widthHeader = new int[]{200,200,250};
		for(int i=0;i<colHeader.length;i++){
			TableColumn col = new TableColumn(tblMasuk, SWT.NONE);
			col.setText(colHeader[i]);
			col.setWidth(widthHeader[i]);
		}
		
		tblKeluar = new Table(parent, SWT.BORDER | SWT.FULL_SELECTION);
		tblKeluar.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		tblKeluar.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		tblKeluar.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE-1, SWT.NORMAL));
		tblKeluar.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		tblKeluar.setHeaderVisible(true);
		tblKeluar.setLinesVisible(true);
		for(int i=0;i<colHeader.length;i++){
			TableColumn col = new TableColumn(tblKeluar, SWT.NONE);
			col.setText(colHeader[i]);
			col.setWidth(widthHeader[i]);
		}
	}

	@Override
	public void setFocus() {
		txtBarcodeMasuk.setFocus();
	}
	
	private void setMasuk(String code){
		String[] split = code.split("/S");
		String regex = "\\d+/+\\d+";
		ArsipLog log = new ArsipLog();
		log.setNoSKP(code);
		java.sql.Date tgl = new java.sql.Date(dateNow.getTime());
		log.setTanggal(tgl);
		ArsipLogProvider.INSTANCE.setNoSKP(code);
		int pos = 0;
		if(split[0].matches(regex) || code.contains("SSPD")){
			if(code.contains("/SKPD/") ||
				code.contains("SPTPD")){
				pos = ControllerFactory.getMainController().getCpArsipDAOImpl().getPos("POS_END","SPTPD","NO_SPTPD",code.toUpperCase());
				if(pos==0 || pos==4 || pos==2){
					log.setStart(pos);
					log.setEnd(3);
					ControllerFactory.getMainController().getCpArsipLogDAOImpl().SaveArsipLog(log);
					ControllerFactory.getMainController().getCpArsipDAOImpl().updatePos("SPTPD","NO_SPTPD",code.toUpperCase(), pos, 3);
					TableItem ti = new TableItem(tblMasuk, SWT.NONE);
					ti.setText(new String[]{code.toUpperCase(),"",""});
					ti.setBackground(SWTResourceManager.getColor(SWT.COLOR_YELLOW));
					txtBarcodeMasuk.setText("");
					txtBarcodeKeluar.setText("");
				}else{
					setArsipKeluar("SPTPD", "NO_SPTPD", code, pos);
				}
			}else if(code.contains("/SKPDKB/") || 
					code.contains("/SKPDN/")){
				pos = ControllerFactory.getMainController().getCpArsipDAOImpl().getPos("POS_END","PERIKSA","NO_SKP",code.toUpperCase());
				if(pos==0 || pos==4 || pos==2){
					log.setStart(pos);
					log.setEnd(3);
					ControllerFactory.getMainController().getCpArsipLogDAOImpl().SaveArsipLog(log);
					ControllerFactory.getMainController().getCpArsipDAOImpl().updatePos("PERIKSA","NO_SKP",code.toUpperCase(), pos, 3);
					TableItem ti = new TableItem(tblMasuk, SWT.NONE);
					ti.setText(new String[]{code.toUpperCase(),"",""});
					ti.setBackground(SWTResourceManager.getColor(SWT.COLOR_YELLOW));
					txtBarcodeMasuk.setText("");
					txtBarcodeKeluar.setText("");
				}else{
					setArsipKeluar("PERIKSA", "NO_SKP", code, pos);
				}
			}else if(txtBarcodeMasuk.getText().contains("/SSPD/")){
				pos = ControllerFactory.getMainController().getCpArsipDAOImpl().getPos("POS_END","SSPD","NO_SSPD",code.toUpperCase());
				if(pos==0 || pos==4 || pos==2){
					log.setStart(pos);
					log.setEnd(3);
					ControllerFactory.getMainController().getCpArsipLogDAOImpl().SaveArsipLog(log);
					ControllerFactory.getMainController().getCpArsipDAOImpl().updatePos("SSPD","NO_SSPD",code.toUpperCase(), pos, 3);
					TableItem ti = new TableItem(tblMasuk, SWT.NONE);
					ti.setText(new String[]{code.toUpperCase(),"",""});
					ti.setBackground(SWTResourceManager.getColor(SWT.COLOR_YELLOW));
					txtBarcodeMasuk.setText("");
					txtBarcodeKeluar.setText("");
				}else{
					setArsipKeluar("SSPD", "NO_SSPD", code, pos);
				}
//				ControllerFactory.getMainController().getCpArsipDAOImpl().updatePos("SSPD",txtBarcodeMasuk.getText(), 4, 3);
			}else{
				// Dokumen tidak dikenal
				MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Dokumen tidak dikenal.");
			}
		}else{
			DetailKeluarMasukDialog dialog = new DetailKeluarMasukDialog(Display.getCurrent().getActiveShell());
			String tipeSKP = "";
			String tipeColomn = "";
			if(code.contains("/SKPD/") || code.contains("SPTPD")){
				ResultSet result = ControllerFactory.getMainController().getCpArsipDAOImpl().getSKPD(code);
				dialog.setResult(result);
				tipeSKP = "SPTPD";
				tipeColomn = "NO_SPTPD";
			}else if(code.contains("/SKPDKB/") || 
					code.contains("/SKPDN/")){
				ResultSet result = ControllerFactory.getMainController().getCpArsipDAOImpl().getSKPDKB(code);
				dialog.setResult(result);
				tipeSKP = "PERIKSA";
				tipeColomn = "NO_SKP";
			}else if(code.contains("/SSPD/")){
				ResultSet result = ControllerFactory.getMainController().getCpArsipDAOImpl().getSSPD(code);
				dialog.setResult(result);
				tipeSKP = "SSPD";
				tipeColomn = "NO_SSPD";
			}else{
				// Dokumen tidak dikenal
				dialog.setResult(null);
				tipeSKP = "";
				tipeColomn = "";
			}
			dialog.open();
			if(GlobalVariable.INSTANCE.item!=null){
				String[] item = GlobalVariable.INSTANCE.item;
				String npwpd = item[4];
				pos = ControllerFactory.getMainController().getCpArsipDAOImpl().getPos("POS_END",tipeSKP,tipeColomn,code,item[4]);
				if(pos==0 || pos==4 || pos==2){
					log.setStart(pos);
					log.setEnd(3);
					log.setNpwpd(npwpd);
					ControllerFactory.getMainController().getCpArsipLogDAOImpl().SaveArsipLog(log);
					ControllerFactory.getMainController().getCpArsipDAOImpl().updatePos(tipeSKP,tipeColomn,item[0], item[4], 4, 3);
					TableItem ti = new TableItem(tblMasuk, SWT.NONE);
					ti.setText(new String[]{item[0],item[4],item[5]});
					ti.setBackground(SWTResourceManager.getColor(SWT.COLOR_YELLOW));
					txtBarcodeMasuk.setText("");
					txtBarcodeKeluar.setText("");
				}else{
					int posEnd = 4;
					
//					ArsipLogProvider.INSTANCE.setNoSKP(code);
//					ArsipLogProvider.INSTANCE.setNpwpd("");
//					ArsipLogProvider.INSTANCE.setStart(pos);
//					ArsipLogProvider.INSTANCE.setEnd(posEnd);
					PeminjamanArsipDialog dialogArsip = new PeminjamanArsipDialog(Display.getCurrent().getActiveShell());
					dialogArsip.open();
					if (!ArsipLogProvider.INSTANCE.getPeminjam().equalsIgnoreCase("")){
						log.setNoSKP(code);
						log.setNpwpd(npwpd);
						log.setTanggal(ArsipLogProvider.INSTANCE.getTanggal());
						log.setPeminjam(ArsipLogProvider.INSTANCE.getPeminjam());
						log.setKeterangan(ArsipLogProvider.INSTANCE.getKeterangan());
						log.setStart(pos);
						log.setEnd(posEnd);
						ControllerFactory.getMainController().getCpArsipLogDAOImpl().SaveArsipLog(log);
						ControllerFactory.getMainController().getCpArsipDAOImpl().updatePos(tipeSKP,tipeColomn,item[0], item[4], 3, 4);
						TableItem ti = new TableItem(tblKeluar, SWT.NONE);
						ti.setText(new String[]{item[0],item[4],item[5]});
						ti.setBackground(SWTResourceManager.getColor(SWT.COLOR_RED));
						txtBarcodeKeluar.setText("");
						txtBarcodeMasuk.setText("");
					}
				}
			}else{
				MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Dokumen yang ada input TIDAK ADA dalam arsip.");
				txtBarcodeMasuk.setText("");
			}
		}
		
		txtBarcodeMasuk.setFocus();
	}
	
	private void setArsipKeluar(String tbl, String column, String noSkp, int pos){
		ArsipLog log = new ArsipLog();
		int posEnd = 4;
		String npwpd = "";
		ArsipLogProvider.INSTANCE.setNoSKP(noSkp);
//		ArsipLogProvider.INSTANCE.setNpwpd("");
//		ArsipLogProvider.INSTANCE.setStart(pos);
//		ArsipLogProvider.INSTANCE.setEnd(posEnd);
		PeminjamanArsipDialog dialog = new PeminjamanArsipDialog(Display.getCurrent().getActiveShell());
		dialog.open();
		if (!ArsipLogProvider.INSTANCE.getPeminjam().equalsIgnoreCase("")){
			log.setNoSKP(noSkp);
			log.setNpwpd(npwpd);
			log.setTanggal(ArsipLogProvider.INSTANCE.getTanggal());
			log.setPeminjam(ArsipLogProvider.INSTANCE.getPeminjam());
			log.setKeterangan(ArsipLogProvider.INSTANCE.getKeterangan());
			log.setStart(pos);
			log.setEnd(posEnd);
			ControllerFactory.getMainController().getCpArsipLogDAOImpl().SaveArsipLog(log);
			ControllerFactory.getMainController().getCpArsipDAOImpl().updatePos(tbl, column, noSkp, pos, posEnd);
			TableItem ti = new TableItem(tblKeluar, SWT.NONE);
			ti.setText(new String[]{noSkp,"",""});
			ti.setBackground(SWTResourceManager.getColor(SWT.COLOR_RED));
		}
		txtBarcodeKeluar.setText("");
		txtBarcodeMasuk.setText("");
	}
}
