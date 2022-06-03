package com.dispenda.sspd.views;

//import java.sql.Timestamp;
import java.awt.Desktop;
import java.awt.Dialog;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
//import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
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
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.SWTResourceManager;

import com.dispenda.controller.ControllerFactory;
import com.dispenda.controller.UIController;
import com.dispenda.dao.LogImp;
import com.dispenda.model.Pejabat;
import com.dispenda.model.Sspd;
import com.dispenda.model.SspdProvider;
import com.dispenda.model.UserModul;
import com.dispenda.object.GlobalVariable;
import com.dispenda.object.Preferences;
import com.dispenda.report.ReportAction;
import com.dispenda.report.ReportAppContext;
import com.dispenda.report.ReportModule;
import com.dispenda.sspd.bridge.ICommunicationView;
import com.dispenda.sspd.dialog.RegPbbDialog;
import com.dispenda.widget.MoneyField;
import com.ibm.icu.util.Currency;
import com.ibm.icu.util.CurrencyAmount;
import org.apache.commons.codec.binary.Base64;



public class AntriView extends ViewPart implements ICommunicationView {
	public AntriView() {
	}

	public static final String ID = AntriView.class.getName();
	private Table tblDaftarAntri;
	private Table tblTerima;
	private Text txtCari;
	private String[] masaPajak;
	private String[] masaPajakBayar;
	private List<Sspd> listAntri; 
	private List<Sspd> listBayar; 
	private List<Pejabat> listPejabat;
	private Locale ind = new Locale("id", "ID");
	private NumberFormat indFormat = NumberFormat.getCurrencyInstance(ind);
//	private java.sql.Timestamp dateNow;
	private String noSSPD = "";
	private String noSSPDGanti = "";
	private Integer idSSPDGanti = 0;
	private Composite composite;
	private Combo cmbPejabat;
	private HashMap<String,String> hashMap = null;
	private Color fontColor = new Color(Display.getCurrent(),Preferences.FONT_COLOR);
	private MoneyField txtTotal;
	private MoneyField txtDenda;
	private MoneyField txtPokok;
	private Menu menuTerima;
	private MenuItem menuItemGanti;
	@Override
	public void createPartControl(Composite parent) {
		Locale.setDefault(ind);
//		dateNow = (java.sql.Timestamp)ControllerFactory.getMainController().getCpWajibPajakDAOImpl().getDateNow();
		
		ScrolledComposite scrolledComposite = new ScrolledComposite(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		
		composite = new Composite(scrolledComposite, SWT.NONE);
		composite.setLayout(new GridLayout(5, false));
		
		Label lblDaftarAntriBayar = new Label(composite, SWT.NONE);
		lblDaftarAntriBayar.setForeground(fontColor);
		lblDaftarAntriBayar.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblDaftarAntriBayar.setText("Daftar Antri Bayar Hari Ini");
		
		Label label = new Label(composite, SWT.SEPARATOR | SWT.VERTICAL);
		label.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 13));
		
		Label lblDaftarTerimaBayar = new Label(composite, SWT.NONE);
		lblDaftarTerimaBayar.setForeground(fontColor);
		lblDaftarTerimaBayar.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblDaftarTerimaBayar.setText("Daftar Terima Bayar");
		
		Button btnRefresh = new Button(composite, SWT.NONE);
		btnRefresh.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				resetForm();
				createColumn();
				loadTableAntri();
				loadTableDaftarTerima("");
			}
		});
		GridData gd_btnRefresh = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_btnRefresh.widthHint = 90;
		btnRefresh.setLayoutData(gd_btnRefresh);
		btnRefresh.setText("Refresh");
		btnRefresh.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		Label label_1 = new Label(composite, SWT.SEPARATOR | SWT.VERTICAL);
		label_1.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 13));
		
		tblDaftarAntri = new Table(composite, SWT.BORDER | SWT.FULL_SELECTION | SWT.VIRTUAL);
		tblDaftarAntri.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		tblDaftarAntri.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ICommunicationView viewToOpen;
				try {
					viewToOpen = (ICommunicationView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(SSPDView.ID);
					viewToOpen.accept(listAntri.get(tblDaftarAntri.getSelectionIndex()));
					viewToOpen.setFocus();
				} catch (PartInitException e1) {
					e1.printStackTrace();
				}
			}
		});
		tblDaftarAntri.setForeground(SWTResourceManager.getColor(0, 0, 0));
		tblDaftarAntri.setFont(SWTResourceManager.getFont("Calibri", 11, SWT.NORMAL));
		GridData gd_tblDaftarAntri = new GridData(SWT.FILL, SWT.FILL, false, true, 1, 11);
		gd_tblDaftarAntri.heightHint = 408;
		tblDaftarAntri.setLayoutData(gd_tblDaftarAntri);
		tblDaftarAntri.setHeaderVisible(true);
		tblDaftarAntri.setLinesVisible(true);
		menuTerima = new Menu(tblDaftarAntri);
		MenuItem menuItemTerima = new MenuItem(menuTerima, SWT.PUSH);
		menuItemTerima.setText("Terima");
		menuItemTerima.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TerimaSSPD();
			}
		});
		tblDaftarAntri.setMenu(menuTerima);
		menuItemGanti = new MenuItem(menuTerima, SWT.PUSH);
		menuItemGanti.setText("Ganti");
		menuItemGanti.setEnabled(false);
		menuItemGanti.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (ControllerFactory.getMainController().getCpSspdDAOImpl().updateAntrianSSPD(noSSPDGanti, listAntri.get(tblDaftarAntri.getSelectionIndex()).getIdSspd())){
					if (ControllerFactory.getMainController().getCpSspdDAOImpl().deleteSspd(idSSPDGanti)){
						StringBuffer sb = new StringBuffer();
						sb.append("Ganti SSPD " +
								"SSPD:"+noSSPDGanti+
								" NPWPD Lama:"+listBayar.get(tblTerima.getSelectionIndex()).getNpwpd()+
								" NPWPD Baru:"+listAntri.get(tblDaftarAntri.getSelectionIndex()).getNpwpd()+
								" TanggalSSPD:"+listAntri.get(tblDaftarAntri.getSelectionIndex()).getTglSspd()+
								" NoSKP Lama:"+listBayar.get(tblTerima.getSelectionIndex()).getNoSkp()+
								" NoSKP Baru:"+listAntri.get(tblDaftarAntri.getSelectionIndex()).getNoSkp()+
								" JumlahBayar Lama:"+listBayar.get(tblTerima.getSelectionIndex()).getJumlahBayar()+
								" JumlahBayar Baru:"+listAntri.get(tblDaftarAntri.getSelectionIndex()).getJumlahBayar());
//								" Denda:"+listAntri.get(i).getDenda());
						new LogImp().setLog(sb.toString(), userModul.getDisplayName(), userModul.getSubModul().getNamaModul());
						MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "SSPD berhasil di ganti");
						loadTableAntri();
						loadTableDaftarTerima("");
					}else
						MessageDialog.openError(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Error Ganti SSPD. SSPD gagal dihapus");
				}else
					MessageDialog.openError(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Error Ganti SSPD. SSPD gagal di update");
				menuItemGanti.setEnabled(false);
			}
		});
		tblDaftarAntri.setMenu(menuTerima);
		
		Composite compCari = new Composite(composite, SWT.NONE);
		compCari.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 2, 1));
		compCari.setLayout(new GridLayout(3, false));
		
		Label lblCari = new Label(compCari, SWT.NONE);
		lblCari.setForeground(fontColor);
		lblCari.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblCari.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblCari.setText("Cari");
		
		txtCari = new Text(compCari, SWT.BORDER);
		txtCari.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtCari.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if ((e.keyCode == SWT.CR || e.keyCode == SWT.KEYPAD_CR ) && !txtCari.getText().equalsIgnoreCase("")){
					loadTableDaftarTerima(txtCari.getText());
					if (tblTerima.getItemCount() < 1)
						MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Hasil tidak ada");
				}
			}
		});
		txtCari.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		txtCari.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtCari.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Button btnCari = new Button(compCari, SWT.NONE);
		btnCari.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (!txtCari.getText().equals("")){
//					searchParam = txtCari.getText();
					loadTableDaftarTerima(txtCari.getText());
					if (tblTerima.getItemCount() < 1)
						MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Hasil tidak ada");
				}
			}
		});
		btnCari.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		GridData gd_btnCari = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_btnCari.widthHint = 90;
		btnCari.setLayoutData(gd_btnCari);
		btnCari.setText("Cari");
		
		tblTerima = new Table(composite, SWT.BORDER | SWT.FULL_SELECTION | SWT.VIRTUAL);
		tblTerima.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		tblTerima.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ICommunicationView viewToOpen = (ICommunicationView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(SSPDView.ID);
				viewToOpen.accept(listBayar.get(tblTerima.getSelectionIndex()));
				viewToOpen.setFocus();
			}
		});
		tblTerima.setForeground(SWTResourceManager.getColor(0, 0, 0));
		tblTerima.setFont(SWTResourceManager.getFont("Calibri", 11, SWT.NORMAL));
		GridData gd_tblTerima = new GridData(SWT.FILL, SWT.FILL, false, true, 2, 10);
		gd_tblTerima.heightHint = 355;
		tblTerima.setLayoutData(gd_tblTerima);
		tblTerima.setHeaderVisible(true);
		tblTerima.setLinesVisible(true);
		Menu menu = new Menu(tblTerima);
		MenuItem menuItem = new MenuItem(menu, SWT.PUSH);
		menuItem.setText("Cetak..");
		menuItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				menuItemGanti.setEnabled(false);
				int isDenda = ControllerFactory.getMainController().getCpSspdDAOImpl().isDenda(listBayar.get(tblTerima.getSelectionIndex()).getNoSspd());
				//tambahurldisini
//				String nosspd = listBayar.get(tblTerima.getSelectionIndex()).getNoSspd();
//				String npwpd = listBayar.get(tblTerima.getSelectionIndex()).getNpwpd();
//				String uniq = npwpd+'-'+nosspd+'-';
//				String encode = DatatypeConverter.printBase64Binary(uniq.getBytes());
//				String link = "https://simp4d.jarpajar.xyz/sspd/view.html?id="+encode+"=&is_pdf=1";
//				try {
////				    Desktop.getDesktop().browse(new URL("https://simp4d.jarpajar.xyz/sspd/view.html?id=MzAwMDIwMjIxMzAzLTIyNjE3L1NTUEQvMjAxNC0=&is_pdf=1").toURI());
//				    Desktop.getDesktop().browse(new URL(link).toURI());
//					
//				} catch (Exception e1) {}

//				printSSPD_link(); //versi print online sspd
				if (isDenda == 0)//jangan dihapus, ini versi tidak nge link
					printSSPD();
				else
					printSSPDDenda();
			}
		});
		tblTerima.setMenu(menu);
		if (userModul.getIdUser() == 1 || userModul.getIdUser() == 19 || userModul.getIdUser() == 20){
			MenuItem menuBatal = new MenuItem(menu, SWT.PUSH);
			menuBatal.setText("Batal");
			menuBatal.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					if (isSave()){
						noSSPDGanti = listBayar.get(tblTerima.getSelectionIndex()).getNoSspd();
						idSSPDGanti = listBayar.get(tblTerima.getSelectionIndex()).getIdSspd();
						menuItemGanti.setEnabled(true);
						MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Pilih SSPD pengganti dari daftar antrian");
					}else{
						MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Anda tidak memiliki hak untuk menyimpan atau merubah data.");
					}
				}
			});
		}
		
		tblTerima.setMenu(menu);
		if (userModul.getIdUser() == 1){
			MenuItem menuDouble = new MenuItem(menu, SWT.PUSH);
			menuDouble.setText("Double");
			menuDouble.addSelectionListener(new SelectionAdapter() {
				@SuppressWarnings("deprecation")
				@Override
				public void widgetSelected(SelectionEvent e) {
					boolean result = MessageDialog.openQuestion(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Ubah data SSPD?"); 
					if (result){
						int countDouble = 0;
						Integer idSSPD = listBayar.get(tblTerima.getSelectionIndex()).getIdSspd();
						String oldNoSSPD = listBayar.get(tblTerima.getSelectionIndex()).getNoSspd();
						String oldNoReg = listBayar.get(tblTerima.getSelectionIndex()).getNoReg();
						Integer idReg = ControllerFactory.getMainController().getCpSspdDAOImpl().getIDRegDouble(oldNoSSPD);
						for (int i=0;i<tblTerima.getItemCount();i++){
							if (tblTerima.getItem(i).getText(0).equalsIgnoreCase(oldNoReg))
								countDouble++;
						}
						if (countDouble > 1){
							String newNoSSPD = ControllerFactory.getMainController().getCpSspdDAOImpl().getNoSSPD(String.valueOf(listBayar.get(tblTerima.getSelectionIndex()).getTglCetak().getYear() + 1900));
							if (ControllerFactory.getMainController().getCpSspdDAOImpl().updateAntrianSSPD(newNoSSPD, idSSPD)){
								if (ControllerFactory.getMainController().getCpSspdDAOImpl().deleteNoReg(idReg)){
									int newNoReg = ControllerFactory.getMainController().getCpSspdDAOImpl().getMaxNoReg(listBayar.get(tblTerima.getSelectionIndex()).getTglCetak().getYear() + 1900);
									if (ControllerFactory.getMainController().getCpSspdDAOImpl().saveNoReg(newNoReg, newNoSSPD, "", listBayar.get(tblTerima.getSelectionIndex()).getTglCetak().getYear() + 1900)){
										StringBuffer sb = new StringBuffer();
										sb.append("Ubah SSPD Double" +
												" No SSPD Lama:"+oldNoSSPD+
												" No SSPD Baru:"+newNoSSPD+
												" No Reg Lama:"+oldNoReg+
												" No Reg Baru:"+newNoReg+
												" NPWPD:"+listBayar.get(tblTerima.getSelectionIndex()).getNpwpd()+
												" TanggalSSPD:"+listBayar.get(tblTerima.getSelectionIndex()).getTglSspd()+
												" NoSKP:"+listBayar.get(tblTerima.getSelectionIndex()).getNoSkp()+
												" JumlahBayar:"+listBayar.get(tblTerima.getSelectionIndex()).getJumlahBayar());
//												" Denda:"+listAntri.get(i).getDenda());
										new LogImp().setLog(sb.toString(), userModul.getDisplayName(), userModul.getSubModul().getNamaModul());
										MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Data SSPD berhasil diubah");
										loadTableAntri();
										loadTableDaftarTerima("");
									}
									
								}
							}
						}else
							MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Tidak ada data SSPD yang double");
					}
				}
			});
		}
		
		createColumn();
		
		Composite compButton = new Composite(composite, SWT.NONE);
		compButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 2));
		compButton.setLayout(new GridLayout(2, false));
		
		Button btnTerima = new Button(compButton, SWT.NONE);
		btnTerima.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TerimaSSPD();
			}
		});
		GridData gd_btnTerima = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnTerima.widthHint = 90;
		btnTerima.setLayoutData(gd_btnTerima);
		btnTerima.setText("Terima");
		btnTerima.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		Button btnTolak = new Button(compButton, SWT.NONE);
		btnTolak.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(isDelete()){
					int style = SWT.ICON_QUESTION |SWT.YES | SWT.NO;
					int count = 0;
					if (MessageDialog.open(6, Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Hapus data SSPD?", style)){
						int i=tblDaftarAntri.getSelectionIndex();
//						for (int i=0;i<tblDaftarAntri.getItems().length;i++){
//							if (tblDaftarAntri.getItem(i).getChecked()){
						if (i>=0){
								if (ControllerFactory.getMainController().getCpSspdDAOImpl().deleteSspd(listAntri.get(i).getIdSspd()))
								{
									count++;
									StringBuffer sb = new StringBuffer();
									sb.append("DELETE " +
											"selectedID:"+listAntri.get(i).getIdSspd()+
											" SSPD:"+listAntri.get(i).getNoSspd()+
											" SPTPD:"+listAntri.get(i).getNoSkp()+
											" NPWPD:"+listAntri.get(i).getNPWP()+
											" MasaPajakDari:"+listAntri.get(i).getMasaPajakDari()+
											" MasaPajakSampai:"+listAntri.get(i).getMasaPajakSampai()+
											" JumlahBayar:"+listAntri.get(i).getJumlahBayar()+
											" Denda:"+listAntri.get(i).getDenda());
									new LogImp().setLog(sb.toString(), userModul.getDisplayName(), userModul.getSubModul().getNamaModul());
								}else{
									MessageDialog.openError(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Error pada Database. Silahkan hubungi ADMIN.");
								}
							}
//						}
						if (count > 0){
							resetForm();
							createColumn();
							loadTableAntri();
							loadTableDaftarTerima("");
							MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Data SSPD berhasil dihapus.");
						}else
							MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Silahkan pilih data yang akan dihapus.");
					}
				}else
					MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Anda tidak memiliki hak untuk menghapus data.");
			}
		});
		GridData gd_btnTolak = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnTolak.widthHint = 90;
		btnTolak.setLayoutData(gd_btnTolak);
		btnTolak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnTolak.setText("Tolak");
		
		Button btnPBB = new Button(compButton, SWT.NONE);
		btnPBB.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				RegPbbDialog dialog = new RegPbbDialog(Display.getCurrent().getActiveShell(), "PBB");
				dialog.open();
			}
		});
		GridData gd_btnPBB = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnPBB.widthHint = 90;
		btnPBB.setLayoutData(gd_btnPBB);
		btnPBB.setText("Reg PBB");
		btnPBB.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		Button btnRegBphtb = new Button(compButton, SWT.NONE);
		btnRegBphtb.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				RegPbbDialog dialog = new RegPbbDialog(Display.getCurrent().getActiveShell(), "BPHTB");
				dialog.open();
			}
		});
		GridData gd_btnRegBphtb = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnRegBphtb.widthHint = 90;
		btnRegBphtb.setLayoutData(gd_btnRegBphtb);
		btnRegBphtb.setText("Reg BPHTB");
		btnRegBphtb.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		Composite compCetak = new Composite(composite, SWT.NONE);
		compCetak.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, 2, 1));
		compCetak.setLayout(new GridLayout(3, false));
		
		Label lblNamaPejabat = new Label(compCetak, SWT.NONE);
		lblNamaPejabat.setForeground(fontColor);
		lblNamaPejabat.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblNamaPejabat.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNamaPejabat.setText("Nama Pejabat");
		
		cmbPejabat = new Combo(compCetak, SWT.READ_ONLY);
		cmbPejabat.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		cmbPejabat.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
//				MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "Hapus", cmbPejabat.get);
			}
		});
		cmbPejabat.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		cmbPejabat.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		GridData gd_cmbPejabat = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_cmbPejabat.widthHint = 217;
		cmbPejabat.setLayoutData(gd_cmbPejabat);
		
		Button btnCetak = new Button(compCetak, SWT.NONE);
		btnCetak.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//tambahurldisini
//				printSSPD(); //jangan dihapus, ini versi tidak nge link
				printSSPD_link(); //versi print online sspd
				
//				String nosspd = listBayar.get(tblTerima.getSelectionIndex()).getNoSspd();
//				String npwpd = listBayar.get(tblTerima.getSelectionIndex()).getNpwpd();
//				String uniq = npwpd+'-'+nosspd+'-';
//				String encode = DatatypeConverter.printBase64Binary(uniq.getBytes());
//				String link = "https://simp4d.jarpajar.xyz/sspd/view.html?id="+encode+"=&is_pdf=1";
//				try {
////				    Desktop.getDesktop().browse(new URL("https://simp4d.jarpajar.xyz/sspd/view.html?id=MzAwMDIwMjIxMzAzLTIyNjE3L1NTUEQvMjAxNC0=&is_pdf=1").toURI());
//				    Desktop.getDesktop().browse(new URL(link).toURI());
//					
//				} catch (Exception e1) {}
				
			}
		});
		GridData gd_btnCetak = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnCetak.widthHint = 90;
		btnCetak.setLayoutData(gd_btnCetak);
		btnCetak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnCetak.setText("Cetak");
		new Label(composite, SWT.NONE);
		
		Group grpReal = new Group(composite, SWT.NONE);
		grpReal.setLayout(new GridLayout(2, false));
		grpReal.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 3, 1));
		grpReal.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		grpReal.setText("Realisasi");
		
		Label lblNewLabel = new Label(grpReal, SWT.NONE);
		lblNewLabel.setForeground(fontColor);
		lblNewLabel.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblNewLabel.setText("Pokok Pajak");
		
		txtPokok = new MoneyField(grpReal, SWT.BORDER);
		txtPokok.getText().setEditable(false);
		txtPokok.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		((GridData) txtPokok.getText().getLayoutData()).widthHint = 148;
		txtPokok.getText().setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtPokok.getText().setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtPokok.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtPokok.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		new Label(txtPokok, SWT.NONE);
		
		Label lblNewLabel_1 = new Label(grpReal, SWT.NONE);
		lblNewLabel_1.setForeground(fontColor);
		lblNewLabel_1.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblNewLabel_1.setText("Denda");
		
		txtDenda = new MoneyField(grpReal, SWT.BORDER);
		txtDenda.getText().setEditable(false);
		txtDenda.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		txtDenda.getText().setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtDenda.getText().setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtDenda.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtDenda.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		new Label(txtDenda, SWT.NONE);
		
		Label lblNewLabel_2 = new Label(grpReal, SWT.NONE);
		lblNewLabel_2.setForeground(fontColor);
		lblNewLabel_2.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblNewLabel_2.setText("Total");
		
		txtTotal = new MoneyField(grpReal, SWT.BORDER);
		txtTotal.getText().setEditable(false);
		GridData gd_txtTotal = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_txtTotal.widthHint = 283;
		txtTotal.setLayoutData(gd_txtTotal);
		txtTotal.getText().setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtTotal.getText().setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtTotal.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtTotal.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		new Label(txtTotal, SWT.NONE);
		new Label(grpReal, SWT.NONE);
		
		Button btnHitung = new Button(grpReal, SWT.NONE);
		btnHitung.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				List<Double> listPokok = ControllerFactory.getMainController().getCpSspdDAOImpl().getJumlahPenerimaan(txtCari.getText());
				txtPokok.setMoney(new CurrencyAmount(listPokok.get(0), Currency.getInstance("ind")));
				txtDenda.setMoney(new CurrencyAmount(listPokok.get(1), Currency.getInstance("ind")));
				txtTotal.setMoney(new CurrencyAmount(listPokok.get(0) + listPokok.get(1), Currency.getInstance("ind")));
//				MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "Total", indFormat.format(listPokok.get(0) + listPokok.get(1)));
			}
		});
		btnHitung.setText("Hitung Penerimaan");
		btnHitung.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));

		scrolledComposite.setContent(composite);
		scrolledComposite.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	
		loadTableAntri();
		checkAntri();
		loadTableDaftarTerima("");
		loadPejabat();
		cmbPejabat.setText("Fatimah Zuria, SE");
	}
	
	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}
	
	@SuppressWarnings("deprecation")
	private void loadTableAntri(){
		tblDaftarAntri.removeAll();
		SspdProvider.INSTANCE.newInstance(SspdProvider.ANTRI, "");
		listAntri = SspdProvider.INSTANCE.getAntri();
		masaPajak = new String[listAntri.size()];
		for(int i=0;i<listAntri.size();i++){
			TableItem ti = new TableItem(tblDaftarAntri, SWT.NONE);
			if (listAntri.get(i).getMasaPajakSampai() == null || listAntri.get(i).getMasaPajakDari().getMonth() == listAntri.get(i).getMasaPajakSampai().getMonth())
				masaPajak[i] = UIController.INSTANCE.formatMonth(listAntri.get(i).getMasaPajakDari().getMonth() + 1, Locale.getDefault()) + " " + (listAntri.get(i).getMasaPajakDari().getYear() + 1900);
			else
				masaPajak[i] = UIController.INSTANCE.formatMonth(listAntri.get(i).getMasaPajakDari().getMonth() + 1, Locale.getDefault()) + " " + (listAntri.get(i).getMasaPajakDari().getYear() + 1900) + " - " +
						UIController.INSTANCE.formatMonth(listAntri.get(i).getMasaPajakSampai().getMonth() + 1, Locale.getDefault()) + " " + (listAntri.get(i).getMasaPajakSampai().getYear() + 1900);
			
			if (listAntri.get(i).getNpwpd().substring(0, 1).equalsIgnoreCase("7")){
				masaPajak[i] = listAntri.get(i).getMasaPajakDari().getDate() + " " + UIController.INSTANCE.formatMonth(listAntri.get(i).getMasaPajakDari().getMonth() + 1, Locale.getDefault()) + " " + (listAntri.get(i).getMasaPajakDari().getYear() + 1900) + " - " +
						listAntri.get(i).getMasaPajakSampai().getDate() + " " + UIController.INSTANCE.formatMonth(listAntri.get(i).getMasaPajakSampai().getMonth() + 1, Locale.getDefault()) + " " + (listAntri.get(i).getMasaPajakSampai().getYear() + 1900);
			}
			
			ti.setText(new String[]{"",listAntri.get(i).getNotaBayar(),listAntri.get(i).getNpwpd(),masaPajak[i],String.valueOf(indFormat.format(listAntri.get(i).getJumlahBayar()))});
		}
		count = listAntri.size();
	}
	
	@SuppressWarnings("deprecation")
	private void loadTableDaftarTerima(String parameter){
		tblTerima.removeAll();
//		double total = 0.0;
		if (parameter.equalsIgnoreCase("")){
			SspdProvider.INSTANCE.newInstance(SspdProvider.SSPD, parameter);
			listBayar = SspdProvider.INSTANCE.getTerima();
		}
		else{
			SspdProvider.INSTANCE.newInstance(SspdProvider.SSPDSearch, parameter);
			listBayar = SspdProvider.INSTANCE.getTerimaSearch();
		}
		
		if (listBayar != null){
			masaPajakBayar = new String[listBayar.size()];
			for(int i=0;i<listBayar.size();i++){
				TableItem ti = new TableItem(tblTerima, SWT.NONE);
//				listBayar.get(i).getNoSkp()
				if (listBayar.get(i).getMasaPajakSampai() == null || (listBayar.get(i).getMasaPajakDari().getMonth() == listBayar.get(i).getMasaPajakSampai().getMonth() && listBayar.get(i).getMasaPajakDari().getYear() == listBayar.get(i).getMasaPajakSampai().getYear()))
					masaPajakBayar[i] = UIController.INSTANCE.formatMonth(listBayar.get(i).getMasaPajakDari().getMonth() + 1, Locale.getDefault()) + " " + (listBayar.get(i).getMasaPajakDari().getYear() + 1900);
				else
					masaPajakBayar[i] = UIController.INSTANCE.formatMonth(listBayar.get(i).getMasaPajakDari().getMonth() + 1, Locale.getDefault()) + " " + (listBayar.get(i).getMasaPajakDari().getYear() + 1900) + " - " +
							UIController.INSTANCE.formatMonth(listBayar.get(i).getMasaPajakSampai().getMonth() + 1, Locale.getDefault()) + " " + (listBayar.get(i).getMasaPajakSampai().getYear() + 1900);
				
				if (listBayar.get(i).getNpwpd().substring(0, 1).equalsIgnoreCase("7")){
					masaPajakBayar[i] = listBayar.get(i).getMasaPajakDari().getDate() + " " + UIController.INSTANCE.formatMonth(listBayar.get(i).getMasaPajakDari().getMonth() + 1, Locale.getDefault()) + " " + (listBayar.get(i).getMasaPajakDari().getYear() + 1900) + " - " +
							listBayar.get(i).getMasaPajakSampai().getDate() + " " + UIController.INSTANCE.formatMonth(listBayar.get(i).getMasaPajakSampai().getMonth() + 1, Locale.getDefault()) + " " + (listBayar.get(i).getMasaPajakSampai().getYear() + 1900);
				}
				String noReg = "";
				if (listBayar.get(i).getNoReg() != null)
					 noReg = Integer.parseInt(listBayar.get(i).getNoReg()) > 0 ? String.valueOf(listBayar.get(i).getNoReg()) : "";
				ti.setText(new String[]{noReg,listBayar.get(i).getNoSspd(),listBayar.get(i).getNotaBayar(),listBayar.get(i).getNpwpd(),masaPajakBayar[i],String.valueOf(indFormat.format(listBayar.get(i).getJumlahBayar()))});
//				total += listBayar.get(i).getJumlahBayar();
			}
		}
//		txtTotal.setMoney(new CurrencyAmount(total, Currency.getInstance(ind)));
	}
	
	private void createColumn()
	{
		if(tblDaftarAntri.getColumnCount() <= 0){
			String[] listColumn = {"", "No.Nota Antrian", "NPWPD", "Masa Pajak", "Jumlah Bayar"};
			int[] widthColumn = {10, 130, 110, 130, 140};
			for (int i=listColumn.length - 1;i>=0;i--)
			{
				TableColumn colPajak = new TableColumn(tblDaftarAntri, SWT.NONE,0);
				colPajak.setText(listColumn[i]);
				colPajak.setWidth(widthColumn[i]);
			}
			tblDaftarAntri.getColumn(0).addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					int count = 0;
					for(int i=0;i<tblDaftarAntri.getItemCount();i++){
						if(tblDaftarAntri.getItem(i).getChecked()){
							count++;
						}
					}
					if(count == tblDaftarAntri.getItemCount()){
						for(int i=0;i<tblDaftarAntri.getItemCount();i++){
							tblDaftarAntri.getItem(i).setChecked(false);
						}
					}else{
						for(int i=0;i<tblDaftarAntri.getItemCount();i++){
							tblDaftarAntri.getItem(i).setChecked(true);
						}
					}
				}
			});
		}
		
		if(tblTerima.getColumnCount() <= 0){
			String[] listColumn = {"No Reg", "No.SSPD","Nota Antri", "NPWPD", "Masa Pajak", "Jumlah Bayar"};
			int[] widthColumn = {70, 120,120, 110, 130, 140};
			for (int i=listColumn.length - 1;i>=0;i--)
			{
				TableColumn colPajak = new TableColumn(tblTerima, SWT.NONE,0);
				colPajak.setText(listColumn[i]);
				colPajak.setWidth(widthColumn[i]);
			}
//			tblTerima.getColumn(0).addSelectionListener(new SelectionAdapter() {
//				@Override
//				public void widgetSelected(SelectionEvent e) {
//					int count = 0;
//					for(int i=0;i<tblTerima.getItemCount();i++){
//						if(tblTerima.getItem(i).getChecked()){
//							count++;
//						}
//					}
//					if(count == tblTerima.getItemCount()){
//						for(int i=0;i<tblTerima.getItemCount();i++){
//							tblTerima.getItem(i).setChecked(false);
//						}
//					}else{
//						for(int i=0;i<tblTerima.getItemCount();i++){
//							tblTerima.getItem(i).setChecked(true);
//						}
//					}
//				}
//			});
		}
	}
	
	private void loadPejabat(){
		listPejabat = ControllerFactory.getMainController().getCpPejabatDAOImpl().getAllPejabat();
		for (int i=0;i<listPejabat.size();i++){
			UIController.INSTANCE.loadPejabat(cmbPejabat, listPejabat.toArray());
//			cmbPejabat.setData(listPejabat.get(i).getIdPejabatNIP(), listPejabat.get(i).getNamaPejabat());
		}
	}
	
	private void resetForm()
	{
		Control[] children = composite.getChildren();
		for (int i=0;i<children.length;i++)
		{
			if (children[i] instanceof Text)
			{
				Text child = (Text) children[i];
				child.setText("");
			}
		}
		txtCari.setText("");
		tblDaftarAntri.removeAll();
		tblTerima.removeAll();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void accept(Object parameter) {
		hashMap = (HashMap) parameter;
	}
	
	private UserModul userModul = ControllerFactory.getMainController().getCpUserDAOImpl()
			.getHMUserModul(GlobalVariable.userModul.getIdUser()).get(21); // 2 merupakan id sub modul.
	
	private boolean isSave(){		
		return userModul.getSimpan();
	}
	
	private boolean isDelete(){		
		return userModul.getHapus();
	}
	
	private boolean isPrint(){		
		return userModul.getCetak();
	}
	
	private Integer count = 0;
	
	@SuppressWarnings("rawtypes")
	private void printSSPD(){
		if(isPrint()){
			if (cmbPejabat.getText().equalsIgnoreCase("")){
				MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Silahkan isi nama pejabat");
			}
			else{
				SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
				SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
				if(!ReportAppContext.map.isEmpty())
					ReportAppContext.map.clear();
				int slashCount = listBayar.get(tblTerima.getSelectionIndex()).getNoSkp().length() - listBayar.get(tblTerima.getSelectionIndex()).getNoSkp().replace("/", "").length();
				ReportAppContext.map.put("NoSSPD", listBayar.get(tblTerima.getSelectionIndex()).getNoSspd());
				ReportAppContext.map.put("NamaBadan", listBayar.get(tblTerima.getSelectionIndex()).getNamaBadan() + " / " + listBayar.get(tblTerima.getSelectionIndex()).getNamaPemilik());
				ReportAppContext.map.put("AlamatBadan", listBayar.get(tblTerima.getSelectionIndex()).getAlabadJalan());
				ReportAppContext.map.put("NPWPD", listBayar.get(tblTerima.getSelectionIndex()).getNpwpd());
				ReportAppContext.map.put("NamaPajak", listBayar.get(tblTerima.getSelectionIndex()).getNamaPajak());
				ReportAppContext.map.put("TipeSKP", listBayar.get(tblTerima.getSelectionIndex()).getNoSkp().split("/")[slashCount - 1]); 
				ReportAppContext.map.put("MasaPajak", masaPajakBayar[tblTerima.getSelectionIndex()]);
				ReportAppContext.map.put("NoSKP", listBayar.get(tblTerima.getSelectionIndex()).getNoSkp());
				ReportAppContext.map.put("KodePajak", listBayar.get(tblTerima.getSelectionIndex()).getKodeBidUsaha());
				ReportAppContext.map.put("KodeDenda", listBayar.get(tblTerima.getSelectionIndex()).getKodeDenda());
				ReportAppContext.map.put("Total", indFormat.format(listBayar.get(tblTerima.getSelectionIndex()).getJumlahBayar()));
				ReportAppContext.map.put("Terbilang", new ReportModule().getTerbilang(listBayar.get(tblTerima.getSelectionIndex()).getJumlahBayar()));
				ReportAppContext.map.put("TanggalPenyetor", sdf.format(listBayar.get(tblTerima.getSelectionIndex()).getTglCetak()));
//				Timestamp parsedDate = ControllerFactory.getMainController().getCpWajibPajakDAOImpl().getDateNow();
				ReportAppContext.map.put("TanggalPejabat", format.format(listBayar.get(tblTerima.getSelectionIndex()).getTglCetak()));
				ReportAppContext.map.put("NamaPenyetor", listBayar.get(tblTerima.getSelectionIndex()).getNamaPenyetor());
				ReportAppContext.map.put("NamaPejabat", cmbPejabat.getText());
				if (listBayar.get(tblTerima.getSelectionIndex()).getNoReg() == null)
					ReportAppContext.map.put("NoReg", "");
				else if (Integer.parseInt(listBayar.get(tblTerima.getSelectionIndex()).getNoReg()) > 0)
					ReportAppContext.map.put("NoReg", listBayar.get(tblTerima.getSelectionIndex()).getNoReg());
				else
					ReportAppContext.map.put("NoReg", "");
//					ReportAppContext.map.put(key, value)
				
				if(hashMap != null){
					Iterator it = hashMap.entrySet().iterator();
				    while (it.hasNext()) {
				        Map.Entry pair = (Map.Entry)it.next();
				        if (pair.getValue().toString().startsWith("Rp.")){
				        	ReportAppContext.map.put(pair.getKey().toString(), pair.getValue().toString().substring(2).replace(".", "").replace(",", "."));
				        }else if (pair.getValue().toString().startsWith("-Rp")){
				        	ReportAppContext.map.put(pair.getKey().toString(),"Rp0,00");
				        }
				        else{
				        	ReportAppContext.map.put(pair.getKey().toString(), pair.getValue().toString());
				        }
				        it.remove();
				    }
				}
				Double sisa = Double.valueOf(ReportAppContext.map.get("TotalPajak").toString().substring(2).replace(".", "").replace(",", ".")) - Double.valueOf(ReportAppContext.map.get("Total").toString().substring(2).replace(".", "").replace(",", "."));
				ReportAppContext.map.put("Sisa", indFormat.format(sisa));
				
				if(listBayar.get(tblTerima.getSelectionIndex()).getNoSkp().split("/")[slashCount - 1].equalsIgnoreCase("SPTPD")){
					ReportAppContext.name = "SSPD_SPTPD";
					ReportAction.start("BlankoSSPD_SPTPD.rptdesign");
				}else if(listBayar.get(tblTerima.getSelectionIndex()).getNoSkp().split("/")[slashCount - 1].equalsIgnoreCase("SKPDKB")){
					ReportAppContext.name = "SSPD_SKPDKB";
					ReportAction.start("BlankoSSPD_SKPDKB.rptdesign");
				}else if(listBayar.get(tblTerima.getSelectionIndex()).getNoSkp().split("/")[slashCount - 1].equalsIgnoreCase("SKPDKBT")){
					ReportAppContext.name = "SSPD_SKPDKBT";
					ReportAction.start("BlankoSSPD_SKPDKBT.rptdesign");
				}else{
					ReportAppContext.name = "SSPD_SKPD";
					ReportAction.start("BlankoSSPD_SKPD.rptdesign");
				}
			}
		}else
			MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Anda tidak memiliki hak untuk mencetak.");
	}
	
	private void printSSPDDenda(){
		if(isPrint()){
			if (cmbPejabat.getText().equalsIgnoreCase("")){
				MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Silahkan isi nama pejabat");
			}
			else{
				SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
				SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
				if(!ReportAppContext.map.isEmpty())
					ReportAppContext.map.clear();
				int slashCount = listBayar.get(tblTerima.getSelectionIndex()).getNoSkp().length() - listBayar.get(tblTerima.getSelectionIndex()).getNoSkp().replace("/", "").length();
				ReportAppContext.map.put("NoSSPD", listBayar.get(tblTerima.getSelectionIndex()).getNoSspd());
				ReportAppContext.map.put("NamaBadan", listBayar.get(tblTerima.getSelectionIndex()).getNamaBadan() + " / " + listBayar.get(tblTerima.getSelectionIndex()).getNamaPemilik());
				ReportAppContext.map.put("AlamatBadan", listBayar.get(tblTerima.getSelectionIndex()).getAlabadJalan());
				ReportAppContext.map.put("NPWPD", listBayar.get(tblTerima.getSelectionIndex()).getNpwpd());
				ReportAppContext.map.put("NamaPajak", listBayar.get(tblTerima.getSelectionIndex()).getNamaPajak());
				ReportAppContext.map.put("TipeSKP", listBayar.get(tblTerima.getSelectionIndex()).getNoSkp().split("/")[slashCount-1]); 
				ReportAppContext.map.put("MasaPajak", masaPajakBayar[tblTerima.getSelectionIndex()]);
				ReportAppContext.map.put("NoSKP", listBayar.get(tblTerima.getSelectionIndex()).getNoSkp());
				ReportAppContext.map.put("KodePajak", listBayar.get(tblTerima.getSelectionIndex()).getKodeBidUsaha());
				ReportAppContext.map.put("KodeDenda", listBayar.get(tblTerima.getSelectionIndex()).getKodeDenda());
				ReportAppContext.map.put("Total", indFormat.format(listBayar.get(tblTerima.getSelectionIndex()).getJumlahBayar()));
				ReportAppContext.map.put("Terbilang", new ReportModule().getTerbilang(listBayar.get(tblTerima.getSelectionIndex()).getJumlahBayar()));
				ReportAppContext.map.put("TanggalPenyetor", sdf.format(listBayar.get(tblTerima.getSelectionIndex()).getTglCetak()));
//				Timestamp parsedDate = ControllerFactory.getMainController().getCpWajibPajakDAOImpl().getDateNow();
				ReportAppContext.map.put("TanggalPejabat", format.format(listBayar.get(tblTerima.getSelectionIndex()).getTglCetak()));
				ReportAppContext.map.put("NamaPenyetor", listBayar.get(tblTerima.getSelectionIndex()).getNamaPenyetor());
				ReportAppContext.map.put("NamaPejabat", cmbPejabat.getText());
				
				if (listBayar.get(tblTerima.getSelectionIndex()).getNoReg() == null)
					ReportAppContext.map.put("NoReg", "");
				else if (Integer.parseInt(listBayar.get(tblTerima.getSelectionIndex()).getNoReg()) > 0)
					ReportAppContext.map.put("NoReg", listBayar.get(tblTerima.getSelectionIndex()).getNoReg());
				else
					ReportAppContext.map.put("NoReg", "");
//					ReportAppContext.map.put(key, value)
				
				if(hashMap != null){
					Iterator it = hashMap.entrySet().iterator();
				    while (it.hasNext()) {
				        Map.Entry pair = (Map.Entry)it.next();
				        if (pair.getValue().toString().startsWith("Rp.")){
				        	ReportAppContext.map.put(pair.getKey().toString(), pair.getValue().toString().substring(2).replace(".", "").replace(",", "."));
				        }
				        else{
				        	ReportAppContext.map.put(pair.getKey().toString(), pair.getValue().toString());
				        }
				        
				        if (pair.getKey().toString().equalsIgnoreCase("TotalPajak")){
				        	try {
								ReportAppContext.map.put("JumlahBayarSebelum", indFormat.format(indFormat.parse(pair.getValue().toString()).doubleValue() - listBayar.get(tblTerima.getSelectionIndex()).getJumlahBayar()));
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
				        }
				        it.remove();
				    }
				}
				if (listBayar.get(tblTerima.getSelectionIndex()).getNoSkp().contains("SPTPD") || listBayar.get(tblTerima.getSelectionIndex()).getNoSkp().contains("/SKPD/")){
					ReportAppContext.map.put("PokokPajak", "Rp0.00");
					ReportAppContext.map.put("DendaSSPD", indFormat.format(listBayar.get(tblTerima.getSelectionIndex()).getJumlahBayar()));
					ReportAppContext.map.put("TotalPajak", indFormat.format(listBayar.get(tblTerima.getSelectionIndex()).getJumlahBayar()));
				}
				if(listBayar.get(tblTerima.getSelectionIndex()).getNoSkp().split("/")[slashCount - 1].equalsIgnoreCase("SPTPD")){
					ReportAppContext.name = "SSPD_SPTPD";
					ReportAction.start("BlankoSSPD_SPTPD.rptdesign");
				}else if(listBayar.get(tblTerima.getSelectionIndex()).getNoSkp().split("/")[slashCount - 1].equalsIgnoreCase("SKPDKB")){
					ReportAppContext.name = "SSPD_SKPDKB";
					ReportAction.start("BlankoSSPD_SKPDKB.rptdesign");
				}else if(listBayar.get(tblTerima.getSelectionIndex()).getNoSkp().split("/")[slashCount - 1].equalsIgnoreCase("SKPDKBT")){
					ReportAppContext.name = "SSPD_SKPDKBT";
					ReportAction.start("BlankoSSPD_SKPDKBT.rptdesign");
				}else{
					ReportAppContext.name = "SSPD_SKPD";
					ReportAction.start("BlankoSSPD_SKPD.rptdesign");
				}
			}
		}else
			MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Anda tidak memiliki hak untuk mencetak.");
	}
	
	private void printSSPD_link(){
		String nosspd = listBayar.get(tblTerima.getSelectionIndex()).getNoSspd();
		String npwpd = listBayar.get(tblTerima.getSelectionIndex()).getNpwpd();
		String uniq = npwpd+'-'+nosspd+'-';
		String encode = DatatypeConverter.printBase64Binary(uniq.getBytes());
//		String link_x = "https://simp4d.jarpajar.xyz/sspd/view.html?id="+encode+"=&is_pdf=1";
		String link = "https://simp4d.pemkomedan.go.id/sspd/view.html?id="+encode+"=&is_pdf=1";
		try {
//		    Desktop.getDesktop().browse(new URL("https://simp4d.jarpajar.xyz/sspd/view.html?id=MzAwMDIwMjIxMzAzLTIyNjE3L1NTUEQvMjAxNC0=&is_pdf=1").toURI());
		    Desktop.getDesktop().browse(new URL(link).toURI());
			
		} catch (Exception e1) {}
		
	}
	
	
	@SuppressWarnings("deprecation")
	private void TerimaSSPD(){
		if(isSave()){
			int no_reg = 0;
//			Integer count = 0;
			boolean isExit = false;
			int i=tblDaftarAntri.getSelectionIndex();
//			for (int i=0;i<tblDaftarAntri.getItems().length;i++){
			if (i >= 0){
				if ((listAntri.get(i).getTglCetak().getYear() + 1900) > 2015)
					no_reg = -1; //ControllerFactory.getMainController().getCpSspdDAOImpl().getMaxNoReg(listAntri.get(i).getTglCetak().getYear() + 1900);
				else
					no_reg = 0;
				
//				if (tblDaftarAntri.getItem(i).getChecked()){
					noSSPD = "INSERT"; //ControllerFactory.getMainController().getCpSspdDAOImpl().getNoSSPD(String.valueOf(listAntri.get(i).getTglCetak().getYear() + 1900));
					if (ControllerFactory.getMainController().getCpSspdDAOImpl().updateAntrianSSPD(noSSPD, listAntri.get(i).getIdSspd())){
						noSSPD = ControllerFactory.getMainController().getCpSspdDAOImpl().getLastNoSspd(listAntri.get(i).getIdSspd());
						if (ControllerFactory.getMainController().getCpSspdDAOImpl().saveNoReg(no_reg, noSSPD, "", listAntri.get(i).getTglCetak().getYear() + 1900)){
							no_reg = Integer.valueOf(ControllerFactory.getMainController().getCpSspdDAOImpl().getNoReg(noSSPD));
//							count++;
							StringBuffer sb = new StringBuffer();
							sb.append("SAVE " +
									"SSPD:"+noSSPD+
									" NoReg:"+no_reg+
									" NPWPD:"+listAntri.get(i).getNpwpd()+
									" TanggalSSPD:"+listAntri.get(i).getTglSspd()+
									" JumlahBayar:"+listAntri.get(i).getJumlahBayar()+
									" Denda:"+listAntri.get(i).getDenda());
							new LogImp().setLog(sb.toString(), userModul.getDisplayName(), userModul.getSubModul().getNamaModul());
						}else{
							MessageDialog.openError(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "No reg gagal disimpan");
							isExit = true;
						}
					}else{
						MessageDialog.openError(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Data gagal disimpan");
						isExit = true;
					}
//				}
			}
			if (i >= 0 && !isExit){
				resetForm();
				createColumn();
				loadTableAntri();
				loadTableDaftarTerima("");
				MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Data berhasil disimpan.");
			}else if (!isExit)
				MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Silahkan pilih data yg akan disimpan.");
		}else
			MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Anda tidak memiliki hak untuk menyimpan atau merubah data.");	
	}
	
	private void checkAntri(){
		Runnable timer = new Runnable() {
			private int i = 0;
			public void run() {
				i = ControllerFactory.getMainController().getCpSspdDAOImpl().countAntri();
				if (i < 0)
					Thread.currentThread().interrupt();
//				i = handler.getItem().countItemFromGudangToTEMPToko();
//				System.out.println(count+" "+i);
				if(count!=i){
					loadTableAntri();
//			        mp3.play();
//					fillTable();
				}else{
//					mp3.play();
				}
//				System.out.println("test");
				Display.getCurrent().timerExec(1000, this);
		    }
		};
		Display.getCurrent().timerExec(1000, timer);
	}
}