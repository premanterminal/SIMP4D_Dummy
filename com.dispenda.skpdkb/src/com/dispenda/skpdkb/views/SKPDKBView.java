package com.dispenda.skpdkb.views;

import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.SWTResourceManager;

import com.dispenda.controller.ControllerFactory;
import com.dispenda.controller.UIController;
import com.dispenda.dao.LogImp;
import com.dispenda.model.PendaftaranWajibPajak;
import com.dispenda.model.Periksa;
import com.dispenda.model.UserModul;
import com.dispenda.object.GlobalVariable;
import com.dispenda.object.Preferences;
import com.dispenda.web.bkad.ApiPost;

public class SKPDKBView extends ViewPart {
	private boolean resultApi = false;
	private Table tbl_LHP;
	private Composite composite;
	private List<Periksa> daftarPeriksa;
	private String NoSKP = "";
	private String NoNPPD = "";
	public SKPDKBView() {
	}
	public static final String ID = SKPDKBView.class.getName();
	private Locale ind = new Locale("id", "ID");
	private NumberFormat indFormat = NumberFormat.getCurrencyInstance(ind);
	private Color fontColor = new Color(Display.getCurrent(),Preferences.FONT_COLOR);
	private Label lblTanggalPenetapan;
	private DateTime calPenetapan;
	private Timestamp dateNow;
	@Override
	public void createPartControl(Composite arg0) {
		Locale.setDefault(new Locale("id","ID"));
		dateNow = (java.sql.Timestamp)ControllerFactory.getMainController().getCpWajibPajakDAOImpl().getDateNow();
		arg0.setLayout(new GridLayout(1, false));
		ScrolledComposite scrolledComposite = new ScrolledComposite(arg0, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		
		composite = new Composite(scrolledComposite, SWT.NONE);
		composite.setLayout(new GridLayout(4, false));
		
		Label lblNewLabel = new Label(composite, SWT.NONE);
		GridData gd_lblNewLabel = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
		gd_lblNewLabel.widthHint = 121;
		lblNewLabel.setLayoutData(gd_lblNewLabel);
		lblNewLabel.setForeground(fontColor);
		lblNewLabel.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, 14, SWT.BOLD));
		lblNewLabel.setText("Daftar LHP");
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		
		tbl_LHP = new Table(composite, SWT.BORDER | SWT.CHECK | SWT.FULL_SELECTION | SWT.VIRTUAL);
		tbl_LHP.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 4, 2));
		tbl_LHP.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE-1, SWT.NORMAL));
		tbl_LHP.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		tbl_LHP.setHeaderVisible(true);
		tbl_LHP.setLinesVisible(true);
		
		Button btnRefresh = new Button(composite, SWT.NONE);
		GridData gd_btnRefresh = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnRefresh.widthHint = 90;
		btnRefresh.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnRefresh.setLayoutData(gd_btnRefresh);
		btnRefresh.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				getDaftarLHP();
			}
		});
		btnRefresh.setText("Refresh");
		
		Button btnTerima = new Button(composite, SWT.NONE);
		GridData gd_btnTerima = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnTerima.widthHint = 90;
		btnTerima.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnTerima.setLayoutData(gd_btnTerima);
		btnTerima.addSelectionListener(new SelectionAdapter() {
			@SuppressWarnings("deprecation")
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(isSave()){
					int count = 0;
					boolean isExit = false;
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					String tgl = sdf.format(new Date(calPenetapan.getYear() - 1900, calPenetapan.getMonth(), calPenetapan.getDay()));
					for (int i=0;i<tbl_LHP.getItems().length;i++){
						if (tbl_LHP.getItem(i).getChecked()){
							if (daftarPeriksa.get(i).getNoSkp().equalsIgnoreCase("")){
								if (calPenetapan.getYear() >= 2016)
									NoSKP = ControllerFactory.getMainController().getCpPeriksaDAOImpl().getNoSKPBaru(String.valueOf(calPenetapan.getYear()), daftarPeriksa.get(i).getIdSubPajak(), daftarPeriksa.get(i).getTipeSkp());
								else
									NoSKP = ControllerFactory.getMainController().getCpPeriksaDAOImpl().getNoSKP(String.valueOf(calPenetapan.getYear()), daftarPeriksa.get(i).getIdSubPajak(), daftarPeriksa.get(i).getTipeSkp());
								//NoNPPD = ControllerFactory.getMainController().getCpPeriksaDAOImpl().getNoNPPD(String.valueOf(calPenetapan.getYear()), daftarPeriksa.get(i).getIdSubPajak(), daftarPeriksa.get(i).getTipeSkp());
							}
							else{
								NoSKP = daftarPeriksa.get(i).getNoSkp();
								//NoNPPD = NoSKP.split("/")[0] + "/NPPD/" + NoSKP.split("/")[2];
							}
							if (daftarPeriksa.get(i).getTipeSkp().equalsIgnoreCase("SKPDKBT"))
								NoNPPD = NoSKP.replace("SKPDKBT", "NPPD");
							else if (daftarPeriksa.get(i).getTipeSkp().equalsIgnoreCase("SKPDN"))
								NoNPPD = NoSKP.replace("SKPDN", "NPPD");
							else if (daftarPeriksa.get(i).getTipeSkp().equalsIgnoreCase("SKPDKB"))
								NoNPPD = NoSKP.replace("SKPDKB", "NPPD");
							
							if (ControllerFactory.getMainController().getCpPeriksaDAOImpl().updateTerimaLHP(NoSKP, NoNPPD, daftarPeriksa.get(i).getIdPeriksa(), tgl)){
								StringBuffer sb = new StringBuffer();
								sb.append("SAVE " +
										"SKP:"+NoSKP+
										" NPPD:"+NoNPPD+
										" Tgl Penetapan:"+calPenetapan.getDay()+"/"+(calPenetapan.getMonth()+1)+"/"+calPenetapan.getYear()+
										" IdPeriksa:"+daftarPeriksa.get(i).getIdPeriksa());
								new LogImp().setLog(sb.toString(), userModul.getDisplayName(), userModul.getSubModul().getNamaModul());
								count++;
								//final String noSts = ControllerFactory.getMainController().getCpPeriksaDAOImpl().getNoSTS(daftarPeriksa.get(i).getIdPeriksa());
								//final String[] listString = getParam(daftarPeriksa, i);
								//System.out.println("List SKPDKB "+listString.toString());
								/*resultApi = false;
								ProgressMonitorDialog progress = new ProgressMonitorDialog(Display.getCurrent().getActiveShell());
								try {
									progress.run(false, false, new IRunnableWithProgress() {
										@Override
										public void run(IProgressMonitor monitor) throws InvocationTargetException,InterruptedException {
											resultApi = ApiPost.Instance.sendPost(listString, noSts, monitor);
										}
									});
								} catch (InvocationTargetException | InterruptedException e1) {
									e1.printStackTrace();
								}
								if (resultApi){
									//MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "Save", "Data berhasil disimpan.");
									/*txtNoSPTPD.setText(NoSPTPD);
									txtNoSPTPD.pack(true);
									loadOldSPTPD();*/
								/*}else{
//									Rollback SKP
									if (noSts == ""){
										if (ControllerFactory.getMainController().getCpPeriksaDAOImpl().RollBackUpdateTerimaLHP(daftarPeriksa.get(i).getIdPeriksa())){
											StringBuffer sb2 = new StringBuffer();
											sb2.append("Rollback " +
													"SKP:"+NoSKP+
													" NPPD:"+NoNPPD+
													" Tgl Penetapan:"+calPenetapan.getDay()+"/"+(calPenetapan.getMonth()+1)+"/"+calPenetapan.getYear()+
													" IdPeriksa:"+daftarPeriksa.get(i).getIdPeriksa());
											new LogImp().setLog(sb2.toString(), userModul.getDisplayName(), userModul.getSubModul().getNamaModul());
											MessageDialog.openError(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Insert STS gagal.");
											count--;
										}
									}else{
										MessageDialog.openError(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Ubah STS gagal.");
										count--;
									}
									break;
								}*/
							}
							else {
								MessageDialog.openError(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Data gagal disimpan");
								isExit = true;
							}
						}
						
					}
					if (count > 0 && !isExit){
						getDaftarLHP();
						MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", count + " Data berhasil disimpan.");
					}else if (!isExit)
						MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Silahkan pilih data yg akan disimpan.");
				}else
					MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Anda tidak memiliki hak untuk menyimpan atau merubah data.");
				
			}
		});
		btnTerima.setText("Terima");
		
		lblTanggalPenetapan = new Label(composite, SWT.NONE);
		GridData gd_lblTanggalPenetapan = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
		gd_lblTanggalPenetapan.widthHint = 147;
		lblTanggalPenetapan.setLayoutData(gd_lblTanggalPenetapan);
		lblTanggalPenetapan.setForeground(fontColor);
		lblTanggalPenetapan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblTanggalPenetapan.setText("Tanggal Penetapan");
		
		calPenetapan = new DateTime(composite, SWT.BORDER | SWT.DROP_DOWN);
		calPenetapan.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		calPenetapan.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		calPenetapan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		scrolledComposite.setContent(composite);
		scrolledComposite.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		getDaftarLHP();
	}
	
	private void createTableColumn()
	{
		//removeColumns();
		if(tbl_LHP.getColumnCount() <= 0){
			String[] listColumn = {"Select All", "IdPeriksa", "IdSubPajak","NPWPD", "No LHP", "Tipe SKP", "Tanggal LHP", "Masa Pajak", "Total Pajak Periksa", "Total Pajak Terutang", "Total Bunga", "No SKP"};
			for (int i=listColumn.length - 1;i>=0;i--)
			{
				TableColumn colPajak = new TableColumn(tbl_LHP, SWT.NONE,0);
				colPajak.setText(listColumn[i]);
//				colPajak.setWidth(120);
				if (i==0)
					colPajak.setWidth(90);
				if(i==1 || i==2)
					colPajak.setWidth(0);
				if (i==3 || i==6)
					colPajak.setWidth(100);
				if (i>3 && i<6)
					colPajak.setWidth(90);
				if (i==7)
					colPajak.setWidth(200);
				if (i>7)
					colPajak.setWidth(150);
			}
			tbl_LHP.getColumn(0).addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					int count = 0;
					for(int i=0;i<tbl_LHP.getItemCount();i++){
						if(tbl_LHP.getItem(i).getChecked()){
							count++;
						}
					}
					if(count == tbl_LHP.getItemCount()){
						for(int i=0;i<tbl_LHP.getItemCount();i++){
							tbl_LHP.getItem(i).setChecked(false);
						}
					}else{
						for(int i=0;i<tbl_LHP.getItemCount();i++){
							tbl_LHP.getItem(i).setChecked(true);
						}
					}
				}
			});
		}
		
		
	}
	
	@SuppressWarnings("deprecation")
	private void fillTable(List<Periksa> periksa)
	{
		String MasaPajak = "";
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		for (int i=0;i<daftarPeriksa.size();i++){
			MasaPajak = UIController.INSTANCE.formatMonth(daftarPeriksa.get(i).getMasaPajakDari().getMonth() + 1, Locale.getDefault()) + " " + (daftarPeriksa.get(i).getMasaPajakDari().getYear() + 1900) + " - ";
			MasaPajak += UIController.INSTANCE.formatMonth(daftarPeriksa.get(i).getMasaPajakSampai().getMonth() + 1, Locale.getDefault()) + " " + (daftarPeriksa.get(i).getMasaPajakSampai().getYear() + 1900);
			TableItem item = new TableItem(tbl_LHP, SWT.NONE);
			item.setText(1, daftarPeriksa.get(i).getIdPeriksa().toString());
			item.setText(2, daftarPeriksa.get(i).getIdSubPajak().toString());
			item.setText(3, daftarPeriksa.get(i).getNpwpd());
			item.setText(4, daftarPeriksa.get(i).getNoPeriksa());
			item.setText(5, daftarPeriksa.get(i).getTipeSkp());
			item.setText(6, formatter.format(daftarPeriksa.get(i).getTglPeriksa()));
			item.setText(7, MasaPajak);
			item.setText(8, indFormat.format(daftarPeriksa.get(i).getTotalPajakPeriksa()));
			item.setText(9, indFormat.format(daftarPeriksa.get(i).getTotalPajakTerutang()));
			item.setText(10, indFormat.format(daftarPeriksa.get(i).getTotalPajakBunga()));
			item.setText(11, daftarPeriksa.get(i).getNoSkp());
		}
	}
	
	private void getDaftarLHP()
	{
		tbl_LHP.removeAll();
		createTableColumn();
		daftarPeriksa = ControllerFactory.getMainController().getCpPeriksaDAOImpl().GetDaftarLHP();
		fillTable(daftarPeriksa);
		
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		
	}
	
	@SuppressWarnings("deprecation")
	private String[] getParam(List<Periksa> daftarPeriksa, int idx){
		SimpleDateFormat sdf = new SimpleDateFormat("MMyy");
		SimpleDateFormat dateString = new SimpleDateFormat("yyyy-MM-dd");
		PendaftaranWajibPajak wp = ControllerFactory.getMainController().getCpWajibPajakDAOImpl().getNPWPD(Integer.valueOf(daftarPeriksa.get(idx).getNpwpd().substring(1, 8)));
//		String[] wp = ControllerFactory.getMainController().getCpPeriksaDAOImpl().GetDataWP(daftarPeriksa.get(idx).getNpwpd());
		String[] retValue = new String[10];
		Double nilai = daftarPeriksa.get(idx).getTotalPajakPeriksa() - daftarPeriksa.get(idx).getTotalPajakTerutang() + daftarPeriksa.get(idx).getTotalKenaikan();
		if (nilai < 0.0)
			nilai = 0.0;
		String pokok = indFormat.format(nilai).substring(2).replace(".", "").replace(",", "."); //indFormat.format(txtPajakTerutang.getMoney().getNumber().doubleValue()).substring(2).replace(".", "").replace(",", ".");
		String denda = indFormat.format(daftarPeriksa.get(idx).getTotalPajakBunga()).substring(2).replace(".", "").replace(",", ".");
		retValue[0] = daftarPeriksa.get(idx).getNpwpd();
		retValue[1] = wp.getNamaBadan() + "/" + wp.getNamaPemilik();
		retValue[2] = wp.getAlabadJalan();
		retValue[3] = pokok.substring(0, pokok.indexOf("."));
		retValue[4] = denda.substring(0, denda.indexOf("."));
		retValue[5] = NoSKP;
		retValue[6] = calPenetapan.getYear() + "-" + (calPenetapan.getMonth() + 1) + "-" + calPenetapan.getDay();
		retValue[7] = wp.getKode_bid_usaha();
		retValue[8] = sdf.format(daftarPeriksa.get(idx).getMasaPajakDari()) + "-" + sdf.format(daftarPeriksa.get(idx).getMasaPajakSampai());
		retValue[9] = dateString.format(getYesterday());
		
		return retValue;
	}
	
	@SuppressWarnings("deprecation")
	private Date getYesterday(){
		Date retValue;
		Calendar now = Calendar.getInstance();
		now.set(dateNow.getYear(), dateNow.getMonth(), dateNow.getDate());
		now.add(Calendar.DATE, -1);
		retValue = new Date(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DATE));
		
		return retValue;
	}
	
	private UserModul userModul = ControllerFactory.getMainController().getCpUserDAOImpl()
			.getHMUserModul(GlobalVariable.userModul.getIdUser()).get(22); // 2 merupakan id sub modul.
	
	private boolean isSave(){		
		return userModul.getSimpan();
	}
}
