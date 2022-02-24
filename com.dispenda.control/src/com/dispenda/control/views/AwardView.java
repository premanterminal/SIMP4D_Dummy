package com.dispenda.control.views;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.SWTResourceManager;

import com.dispenda.controller.ControllerFactory;
import com.dispenda.controller.UIController;
import com.dispenda.model.PajakProvider;
import com.dispenda.model.Sspd;
import com.dispenda.object.Preferences;
import com.dispenda.report.ReportAction;
import com.dispenda.report.ReportAppContext;
import org.eclipse.swt.widgets.DateTime;

public class AwardView extends ViewPart {
	public AwardView() {
	}

	public static final String ID = AwardView.class.getName();
	private Combo cmbKewajibanPajak;
	private Table tblCekSSPD;
	private Table tblCekSKPDKB;
	private ResultSet resultSSPD;
	private Locale ind = new Locale("id", "ID");
	private NumberFormat indFormat = NumberFormat.getCurrencyInstance(ind);
	private Button btnCekSkpdkb;
	private Table tblKlasTinggi;
	private Table tblKlasSedang;
	private Table tblKlasRendah;
	private Label lblKlasifikasiReward_1;
	private Button btnKlasifikasi;
	private Button btnCetak;
	private Button btnCetakKlas;
	private Label lblWpTerbaik;
	private Label lblPajakTinggi;
	private Label lblPajakSedang;
	private Label lblPajakRendah;
	private List<Sspd> listAward = new ArrayList<Sspd>();
	private Color fontColor = new Color(Display.getCurrent(),Preferences.FONT_COLOR);
	private Label lblMasaPajak;
	private DateTime calDari;
	private Label label;
	private DateTime calSampai;
	
	@Override
	public void createPartControl(Composite parent) {
		ScrolledComposite scrolledComposite = new ScrolledComposite(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		
		Composite comp = new Composite(scrolledComposite, SWT.NONE);
		comp.setLayout(new GridLayout(12, false));
		
		Label lblKewajibanPajak = new Label(comp, SWT.NONE);
		lblKewajibanPajak.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 2, 1));
		lblKewajibanPajak.setForeground(fontColor);
		lblKewajibanPajak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblKewajibanPajak.setText("Kewajiban Pajak");
		
		cmbKewajibanPajak = new Combo(comp, SWT.READ_ONLY);
		cmbKewajibanPajak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		GridData gd_cmbKewajibanPajak = new GridData(SWT.FILL, SWT.CENTER, false, false, 3, 1);
		gd_cmbKewajibanPajak.widthHint = 154;
		cmbKewajibanPajak.setLayoutData(gd_cmbKewajibanPajak);
		cmbKewajibanPajak.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		cmbKewajibanPajak.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		UIController.INSTANCE.loadPajak(cmbKewajibanPajak, PajakProvider.INSTANCE.getPajak().toArray());
		
		Button btnBaru = new Button(comp, SWT.NONE);
		btnBaru.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				resetForm();
			}
		});
		btnBaru.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		GridData gd_btnBaru = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnBaru.widthHint = 90;
		btnBaru.setLayoutData(gd_btnBaru);
		btnBaru.setText("Baru");
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		
		lblMasaPajak = new Label(comp, SWT.NONE);
		lblMasaPajak.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		lblMasaPajak.setText("Masa Pajak");
		lblMasaPajak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblMasaPajak.setForeground(fontColor);
		
		calDari = new DateTime(comp, SWT.BORDER | SWT.DROP_DOWN | SWT.SHORT);
		calDari.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Calendar calPajak = Calendar.getInstance();
				calPajak.set(Calendar.DAY_OF_MONTH, 1);
				calPajak.set(Calendar.MONTH, calDari.getMonth());
				calPajak.set(Calendar.YEAR, calDari.getYear());
				calPajak.add(Calendar.MONTH, 11);
				
				calSampai.setDate(calPajak.get(Calendar.YEAR), calPajak.get(Calendar.MONTH), calPajak.getActualMaximum(Calendar.DAY_OF_MONTH));
			}
		});
		calDari.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		label = new Label(comp, SWT.NONE);
		label.setText("-");
		label.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		calSampai = new DateTime(comp, SWT.BORDER | SWT.DROP_DOWN | SWT.SHORT);
		calSampai.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		calSampai.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		
		Button btnCekSSPD = new Button(comp, SWT.NONE);
		btnCekSSPD.addSelectionListener(new SelectionAdapter() {
			@SuppressWarnings("deprecation")
			@Override
			public void widgetSelected(SelectionEvent e) {
				tblCekSSPD.removeAll();
				tblCekSKPDKB.removeAll();
				tblKlasRendah.removeAll();
				tblKlasSedang.removeAll();
				tblKlasTinggi.removeAll();
				Date masaDari = new Date(calDari.getYear() - 1900, calDari.getMonth(), 1);
				Date masaSampai = new Date(calSampai.getYear() - 1900, calSampai.getMonth(), calSampai.getDay());
				if (cmbKewajibanPajak.getText().equalsIgnoreCase(""))
					resultSSPD = ControllerFactory.getMainController().getCpSspdDAOImpl().GetSSPDTepatWaktu(masaDari, masaSampai);
				else
					resultSSPD = ControllerFactory.getMainController().getCpSspdDAOImpl().GetSSPDTepatWaktuByKode(cmbKewajibanPajak.getData(Integer.toString(cmbKewajibanPajak.getSelectionIndex())).toString(), masaDari, masaSampai);
			
				createColumnsSSPD();
				loadTableSSPD();
				btnCekSkpdkb.setEnabled(true);
				btnKlasifikasi.setEnabled(true);
				btnCetak.setEnabled(false);
				btnCetakKlas.setEnabled(false);
			}
		});
		btnCekSSPD.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		GridData gd_btnCekSSPD = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
		gd_btnCekSSPD.widthHint = 90;
		btnCekSSPD.setLayoutData(gd_btnCekSSPD);
		btnCekSSPD.setText("Cek SSPD");
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		
		btnCekSkpdkb = new Button(comp, SWT.NONE);
		btnCekSkpdkb.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				createColumnsSKPDKB();
				int count = 0;
				for (int i=0; i<tblCekSSPD.getItemCount();i++){
					String wp = tblCekSSPD.getItem(i).getText(0);
					if (ControllerFactory.getMainController().getCpSspdDAOImpl().cekSKPDKBAward(wp)){
						TableItem tItem = new TableItem(tblCekSKPDKB, SWT.None);
						tItem.setText(0, tblCekSSPD.getItem(i).getText(0));
						tItem.setText(1, tblCekSSPD.getItem(i).getText(1));
						tItem.setText(2, tblCekSSPD.getItem(i).getText(2));
						tItem.setText(3, tblCekSSPD.getItem(i).getText(3));
						count++;
					}
					if (count == 500)
						break;
				}
				btnCetak.setEnabled(true);
			}
		});
		btnCekSkpdkb.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnCekSkpdkb.setText("Cek SKPDKB");
		btnCekSkpdkb.setEnabled(false);
		
		btnCetak = new Button(comp, SWT.NONE);
		btnCetak.addSelectionListener(new SelectionAdapter() {
			@SuppressWarnings("deprecation")
			@Override
			public void widgetSelected(SelectionEvent e) {
				ReportAppContext.name = "Variable";
				String kodePajak = "";
				int tahun = ControllerFactory.getMainController().getCpWajibPajakDAOImpl().getDateNow().getYear() + 1900;
				if (cmbKewajibanPajak.getText().equalsIgnoreCase(""))
					kodePajak = "DAFTAR WAJIB PAJAK TERBAIK";
				else
					kodePajak = "DAFTAR WAJIB PAJAK " + cmbKewajibanPajak.getText() + " TERBAIK";
				ReportAppContext.map.put("tahun", String.valueOf(tahun));
				ReportAppContext.map.put("kodePajak", kodePajak);
				ReportAppContext.nameObject = "DaftarAward";
				ReportAppContext.classLoader = AwardView.class.getClassLoader();
				if(!ReportAppContext.object.isEmpty())
					ReportAppContext.object.clear();
				for(int j = 0;j<tblCekSKPDKB.getItems().length;j++){// untuk masukan row table
					List<String> values = new ArrayList<String>();
					values.add(String.valueOf(j+1));
					for(int i=0;i<tblCekSKPDKB.getColumnCount();i++){// untuk masukkan data berdasarkan colom table
//						values.add(tblCekSKPDKB.getItem(j).getText(i));
						values.add(tblCekSKPDKB.getItem(j).getText(i));
					}
					ReportAppContext.object.put(Integer.toString(j), values);
				}
				
				ReportAction.start("Award.rptdesign");
			}
		});
		btnCetak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnCetak.setEnabled(false);
		GridData gd_btnCetak = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnCetak.widthHint = 80;
		btnCetak.setLayoutData(gd_btnCetak);
		btnCetak.setText("Cetak");
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		
		lblWpTerbaik = new Label(comp, SWT.NONE);
		lblWpTerbaik.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblWpTerbaik.setForeground(fontColor);
		lblWpTerbaik.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblWpTerbaik.setText("WP TERBAIK");
		
		tblCekSSPD = new Table(comp, SWT.BORDER | SWT.FULL_SELECTION | SWT.VIRTUAL);
		tblCekSSPD.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, 11, SWT.NORMAL));
		GridData gd_tblCekSSPD = new GridData(SWT.FILL, SWT.FILL, false, true, 7, 1);
		gd_tblCekSSPD.heightHint = 280;
		gd_tblCekSSPD.widthHint = 700;
		tblCekSSPD.setLayoutData(gd_tblCekSSPD);
		tblCekSSPD.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		tblCekSSPD.setHeaderVisible(true);
		tblCekSSPD.setLinesVisible(true);
		
		tblCekSKPDKB = new Table(comp, SWT.BORDER | SWT.FULL_SELECTION | SWT.VIRTUAL);
		tblCekSKPDKB.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, 11, SWT.NORMAL));
		GridData gd_tblCekSKPDKB = new GridData(SWT.FILL, SWT.FILL, false, true, 5, 1);
		gd_tblCekSKPDKB.widthHint = 700;
		tblCekSKPDKB.setLayoutData(gd_tblCekSKPDKB);
		tblCekSKPDKB.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		tblCekSKPDKB.setHeaderVisible(true);
		tblCekSKPDKB.setLinesVisible(true);
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
		new Label(comp, SWT.NONE);
		
		Label lblKlasifikasiReward = new Label(comp, SWT.SEPARATOR | SWT.HORIZONTAL);
		lblKlasifikasiReward.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 12, 1));
		lblKlasifikasiReward.setText("Klasifikasi Reward");
		new Label(comp, SWT.NONE);
		
		lblKlasifikasiReward_1 = new Label(comp, SWT.NONE);
		lblKlasifikasiReward_1.setForeground(fontColor);
		lblKlasifikasiReward_1.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblKlasifikasiReward_1.setText("Klasifikasi Reward");
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
		
		btnKlasifikasi = new Button(comp, SWT.NONE);
		btnKlasifikasi.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int countKTinggi = 0;
				int countKSedang = 0;
				int countKRendah = 0;
				int kLimit = 1000;
				createColumnsKlas();
								
				for (int i=0;i<listAward.size();i++){
					if (countKTinggi < kLimit && listAward.get(i).getJumlahBayar() >= 200000000){
						if (ControllerFactory.getMainController().getCpSspdDAOImpl().cekSKPDKBAward(listAward.get(i).getNpwpd())){
							TableItem itemTinggi = new TableItem(tblKlasTinggi, SWT.None);
							itemTinggi.setText(0, listAward.get(i).getNpwpd());
							itemTinggi.setText(1, listAward.get(i).getNamaBadan());
							itemTinggi.setText(2, indFormat.format(listAward.get(i).getJumlahBayar()));
							itemTinggi.setText(3, listAward.get(i).getAlabadJalan());
							countKTinggi++;
						}
					}
					else if (countKSedang < kLimit && (listAward.get(i).getJumlahBayar() >= 10000000 && listAward.get(i).getJumlahBayar() < 200000000)){
						if (ControllerFactory.getMainController().getCpSspdDAOImpl().cekSKPDKBAward(listAward.get(i).getNpwpd())){
							TableItem itemSedang = new TableItem(tblKlasSedang, SWT.None);
							itemSedang.setText(0, listAward.get(i).getNpwpd());
							itemSedang.setText(1, listAward.get(i).getNamaBadan());
							itemSedang.setText(2, indFormat.format(listAward.get(i).getJumlahBayar()));
							itemSedang.setText(3, listAward.get(i).getAlabadJalan());
							countKSedang++;
						}
					}
					else if (countKRendah < kLimit && listAward.get(i).getJumlahBayar() < 10000000){
						if (ControllerFactory.getMainController().getCpSspdDAOImpl().cekSKPDKBAward(listAward.get(i).getNpwpd())){
							TableItem itemRendah = new TableItem(tblKlasRendah, SWT.None);
							itemRendah.setText(0, listAward.get(i).getNpwpd());
							itemRendah.setText(1, listAward.get(i).getNamaBadan());
							itemRendah.setText(2, indFormat.format(listAward.get(i).getJumlahBayar()));
							itemRendah.setText(3, listAward.get(i).getAlabadJalan());
							countKRendah++;
						}
					}
					
					if (countKTinggi == kLimit && countKSedang == kLimit && countKRendah == kLimit){
						break;
					}
				}
				btnCetakKlas.setEnabled(true);
			}
		});
		btnKlasifikasi.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnKlasifikasi.setText("Klasifikasi");
		btnKlasifikasi.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnKlasifikasi.setEnabled(false);
		
		btnCetakKlas = new Button(comp, SWT.NONE);
		btnCetakKlas.addSelectionListener(new SelectionAdapter() {
			@SuppressWarnings("deprecation")
			@Override
			public void widgetSelected(SelectionEvent e) {
				ReportAppContext.obj = new String[]{"AwardRendah", "AwardSedang", "AwardTinggi"};
				ReportAppContext.listObject = new Object[3];
				HashMap<String, List<String>> AwardRendah = new HashMap<String, List<String>>();
				HashMap<String, List<String>> AwardSedang = new HashMap<String, List<String>>();
				HashMap<String, List<String>> AwardTinggi = new HashMap<String, List<String>>();
				ReportAppContext.name = "Variable";
				String kodePajak = "";
				int tahun = ControllerFactory.getMainController().getCpWajibPajakDAOImpl().getDateNow().getYear() + 1900;
				if (cmbKewajibanPajak.getText().equalsIgnoreCase(""))
					kodePajak = "DAFTAR WAJIB PAJAK TERBAIK";
				else
					kodePajak = "DAFTAR WAJIB PAJAK " + cmbKewajibanPajak.getText() + " TERBAIK";
				ReportAppContext.map.put("tahun", String.valueOf(tahun));
				ReportAppContext.map.put("kodePajak", kodePajak);
				
				ReportAppContext.nameObject = "AwardRendah";
				ReportAppContext.classLoader = AwardView.class.getClassLoader();
				if(!ReportAppContext.object.isEmpty())
					ReportAppContext.object.clear();
				for(int j = 0;j<tblKlasRendah.getItems().length;j++){
					List<String> values = new ArrayList<String>();
					values.add(String.valueOf(j+1));
					for(int i=0;i<tblKlasRendah.getColumnCount();i++){
						values.add(tblKlasRendah.getItem(j).getText(i));
					}
					AwardRendah.put(Integer.toString(j), values);
				}
				
				ReportAppContext.nameObject = "AwardSedang";
				if(!ReportAppContext.object.isEmpty())
					ReportAppContext.object.clear();
				for(int j = 0;j<tblKlasSedang.getItems().length;j++){
					List<String> values = new ArrayList<String>();
					values.add(String.valueOf(j+1));
					for(int i=0;i<tblKlasSedang.getColumnCount();i++){
						values.add(tblKlasSedang.getItem(j).getText(i));
					}
					AwardSedang.put(Integer.toString(j), values);
				}
				
				ReportAppContext.nameObject = "AwardTinggi";
				if(!ReportAppContext.object.isEmpty())
					ReportAppContext.object.clear();
				for(int j = 0;j<tblKlasTinggi.getItems().length;j++){
					List<String> values = new ArrayList<String>();
					values.add(String.valueOf(j+1));
					for(int i=0;i<tblKlasTinggi.getColumnCount();i++){
						values.add(tblKlasTinggi.getItem(j).getText(i));
					}
					AwardTinggi.put(Integer.toString(j), values);
				}
				
				ReportAppContext.listObject[0] = AwardRendah;
				ReportAppContext.listObject[1] = AwardSedang;
				ReportAppContext.listObject[2] = AwardTinggi;
				
				ReportAction.start("AwardKlasifikasi.rptdesign");
			}
		});
		btnCetakKlas.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnCetakKlas.setEnabled(false);
		GridData gd_btnCetakKlas = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnCetakKlas.widthHint = 80;
		btnCetakKlas.setLayoutData(gd_btnCetakKlas);
		btnCetakKlas.setText("Cetak");
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		
//		Label lblKewajibanPajak = new Label(comp, SWT.NONE);
//		lblKewajibanPajak.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 2));
//		lblKewajibanPajak.setForeground(fontColor);
//		lblKewajibanPajak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
//		lblKewajibanPajak.setText("Kewajiban Pajak");
		lblPajakTinggi = new Label(comp, SWT.NONE);
		lblPajakTinggi.setForeground(fontColor);
		lblPajakTinggi.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblPajakTinggi.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPajakTinggi.setText("Pajak Tinggi");
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		
		lblPajakSedang = new Label(comp, SWT.NONE);
		lblPajakSedang.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPajakSedang.setForeground(fontColor);
		lblPajakSedang.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblPajakSedang.setText("Pajak Sedang");
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		
		lblPajakRendah = new Label(comp, SWT.NONE);
		lblPajakRendah.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPajakRendah.setForeground(fontColor);
		lblPajakRendah.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblPajakRendah.setText("Pajak Rendah");
		
		tblKlasTinggi = new Table(comp, SWT.BORDER | SWT.FULL_SELECTION | SWT.VIRTUAL);
		tblKlasTinggi.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE-1, SWT.NORMAL));
		GridData gd_tblKlasTinggi = new GridData(SWT.FILL, SWT.FILL, false, true, 6, 1);
		gd_tblKlasTinggi.heightHint = 280;
		tblKlasTinggi.setLayoutData(gd_tblKlasTinggi);
		tblKlasTinggi.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		tblKlasTinggi.setHeaderVisible(true);
		tblKlasTinggi.setLinesVisible(true);
		
		tblKlasSedang = new Table(comp, SWT.BORDER | SWT.FULL_SELECTION | SWT.VIRTUAL);
		tblKlasSedang.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE-1, SWT.NORMAL));
		tblKlasSedang.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 3, 1));
		tblKlasSedang.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		tblKlasSedang.setHeaderVisible(true);
		tblKlasSedang.setLinesVisible(true);
		
		tblKlasRendah = new Table(comp, SWT.BORDER | SWT.FULL_SELECTION | SWT.VIRTUAL);
		tblKlasRendah.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE-1, SWT.NORMAL));
		tblKlasRendah.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));
		tblKlasRendah.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		tblKlasRendah.setHeaderVisible(true);
		tblKlasRendah.setLinesVisible(true);
		// TODO Auto-generated method stub

		scrolledComposite.setContent(comp);
		scrolledComposite.setMinSize(comp.computeSize(SWT.DEFAULT, SWT.DEFAULT));
//		new Label(parent, SWT.NONE);
//		new Label(parent, SWT.NONE);
//		new Label(parent, SWT.NONE);
//		new Label(parent, SWT.NONE);
//		new Label(parent, SWT.NONE);
//		new Label(parent, SWT.NONE);
//		new Label(parent, SWT.NONE);
//		new Label(parent, SWT.NONE);
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}
	
	private void createColumnsSSPD(){
		if(tblCekSSPD.getColumnCount() <= 0){
			String[] listColumn = {"NPWPD", "Nama Badan", "Pajak", "Alamat"};
			int[] columnWidth = {110, 230, 140, 230};
			for (int i=listColumn.length - 1;i>=0;i--)
			{
				TableColumn colPajak = new TableColumn(tblCekSSPD, SWT.NONE,0);
				colPajak.setText(listColumn[i]);
				colPajak.setWidth(columnWidth[i]);
				
			}
		}
	}

	private void createColumnsSKPDKB(){
		if(tblCekSKPDKB.getColumnCount() <= 0){
			String[] listColumn = {"NPWPD", "Nama Badan", "Pajak rata-rata", "Alamat"};
			int[] columnWidth = {110, 230, 140, 230};
			for (int i=listColumn.length - 1;i>=0;i--)
			{
				TableColumn colPajak = new TableColumn(tblCekSKPDKB, SWT.NONE,0);
				colPajak.setText(listColumn[i]);
				colPajak.setWidth(columnWidth[i]);
				
			}
		}
	}
	
	private void createColumnsKlas(){
		tblKlasTinggi.removeAll();
		tblKlasSedang.removeAll();
		tblKlasRendah.removeAll();
		if(tblKlasTinggi.getColumnCount() <= 0){
			String[] listColumn = {"NPWPD", "Nama Badan", "Pajak rata-rata", "Alamat"};
			int[] columnWidth = {110, 230, 140, 230};
			for (int i=listColumn.length - 1;i>=0;i--)
			{
				TableColumn colPajak = new TableColumn(tblKlasTinggi, SWT.NONE,0);
				colPajak.setText(listColumn[i]);
				colPajak.setWidth(columnWidth[i]);
				
			}
		}
		
		if(tblKlasSedang.getColumnCount() <= 0){
			String[] listColumn = {"NPWPD", "Nama Badan", "Pajak rata-rata", "Alamat"};
			int[] columnWidth = {110, 230, 140, 230};
			for (int i=listColumn.length - 1;i>=0;i--)
			{
				TableColumn colPajak = new TableColumn(tblKlasSedang, SWT.NONE,0);
				colPajak.setText(listColumn[i]);
				colPajak.setWidth(columnWidth[i]);
				
			}
		}
		
		if(tblKlasRendah.getColumnCount() <= 0){
			String[] listColumn = {"NPWPD", "Nama Badan", "Pajak rata-rata", "Alamat"};
			int[] columnWidth = {110, 230, 140, 230};
			for (int i=listColumn.length - 1;i>=0;i--)
			{
				TableColumn colPajak = new TableColumn(tblKlasRendah, SWT.NONE,0);
				colPajak.setText(listColumn[i]);
				colPajak.setWidth(columnWidth[i]);
				
			}
		}
	}
	
	private void loadTableSSPD(){
//		for (int i=0;i<listSSPD.size();i++){
//			TableItem item = new TableItem(tblCekSSPD, SWT.None);
//			if (listSSPD.get(i).getTarif() / listSSPD.get(i).getCicilanKe() >= 1){
//				item.setText(0, listSSPD.get(i).getNpwpd());
//				item.setText(1, listSSPD.get(i).getNamaBadan());
//				item.setText(2, indFormat.format(listSSPD.get(i).getJumlahBayar()));
//			}
//		}
		try {
			listAward.clear();
			while (resultSSPD.next()){
					if (resultSSPD.getInt(4)/ resultSSPD.getInt(5) >= 1){
						TableItem item = new TableItem(tblCekSSPD, SWT.None);
						item.setText(0, resultSSPD.getString(1));
						item.setText(1, resultSSPD.getString(2));
						item.setText(2, indFormat.format(resultSSPD.getDouble(3)));
						item.setText(3, resultSSPD.getString(6));
						Sspd sspdModel = new Sspd();
						sspdModel.setNpwpd(resultSSPD.getString(1));
						sspdModel.setNamaBadan(resultSSPD.getString(2));
						sspdModel.setJumlahBayar(resultSSPD.getDouble(3));
						sspdModel.setAlabadJalan(resultSSPD.getString(6));
						listAward.add(sspdModel);
					}
				}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void resetForm(){
		tblCekSSPD.removeAll();
		tblCekSKPDKB.removeAll();
		tblKlasRendah.removeAll();
		tblKlasSedang.removeAll();
		tblKlasTinggi.removeAll();
		btnCekSkpdkb.setEnabled(false);
		btnKlasifikasi.setEnabled(false);
		btnCetak.setEnabled(false);
		btnCetakKlas.setEnabled(false);
		resultSSPD = null;
		cmbKewajibanPajak.deselectAll();
	}
}
