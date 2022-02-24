package com.dispenda.angsuran.views;

import java.lang.reflect.InvocationTargetException;
import java.sql.Date;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.SWTResourceManager;

import com.dispenda.controller.ControllerFactory;
import com.dispenda.dao.LogImp;
import com.dispenda.model.Mohon;
import com.dispenda.model.Sptpd;
import com.dispenda.model.UserModul;
import com.dispenda.object.GlobalVariable;
import com.dispenda.object.Preferences;
import com.dispenda.report.ReportAppContext;
import com.dispenda.web.bkad.ApiPost;

public class DaftarPermohonanView extends ViewPart {
	public static final String ID = DaftarPermohonanView.class.getName();
	private boolean resultApi = false;
	private Table tbl_Mohon;
	private List<Mohon> listMohon = new ArrayList<Mohon>();
	private DateTime datePermohonan;
	private Color fontColor = new Color(Display.getCurrent(),Preferences.FONT_COLOR);
	private DateTime dateMohonSampai;
	private Locale ind = new Locale("id", "ID");
	private NumberFormat indFormat = NumberFormat.getCurrencyInstance(ind);
	private HashMap<String, String[]> valSTS;
	public DaftarPermohonanView() {
	}

	@Override
	public void createPartControl(Composite parent) {		
		ScrolledComposite scrolledComposite = new ScrolledComposite(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		
		Composite comp = new Composite(scrolledComposite, SWT.NONE);
		comp.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		comp.setLayout(new GridLayout(4, false));
		
		Label lblTanggalPermohonan = new Label(comp, SWT.NONE);
		lblTanggalPermohonan.setForeground(fontColor);
		lblTanggalPermohonan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblTanggalPermohonan.setText("Tanggal Permohonan");
		
		datePermohonan = new DateTime(comp, SWT.BORDER | SWT.DROP_DOWN);
		datePermohonan.addSelectionListener(new SelectionAdapter() {
			@SuppressWarnings("deprecation")
			@Override
			public void widgetSelected(SelectionEvent e) {
				Date awal = new Date(datePermohonan.getYear(), datePermohonan.getMonth(), datePermohonan.getDay());
				Date akhir = new Date(dateMohonSampai.getYear(), dateMohonSampai.getMonth(), dateMohonSampai.getDay());
				if (awal.after(akhir)){
					dateMohonSampai.setDate(datePermohonan.getYear(), datePermohonan.getMonth(), datePermohonan.getDay());
				}
			}
		});
		datePermohonan.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		datePermohonan.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		datePermohonan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		dateMohonSampai = new DateTime(comp, SWT.BORDER | SWT.DROP_DOWN);
		dateMohonSampai.addSelectionListener(new SelectionAdapter() {
			@SuppressWarnings("deprecation")
			@Override
			public void widgetSelected(SelectionEvent e) {
				Date awal = new Date(datePermohonan.getYear(), datePermohonan.getMonth(), datePermohonan.getDay());
				Date akhir = new Date(dateMohonSampai.getYear(), dateMohonSampai.getMonth(), dateMohonSampai.getDay());
				if (akhir.before(awal)){
					datePermohonan.setDate(dateMohonSampai.getYear(), dateMohonSampai.getMonth(), dateMohonSampai.getDay());
				}
			}
		});
		dateMohonSampai.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		dateMohonSampai.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		dateMohonSampai.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		Button btnCari = new Button(comp, SWT.NONE);
		GridData gd_btnCari = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnCari.widthHint = 90;
		btnCari.setLayoutData(gd_btnCari);
		btnCari.addSelectionListener(new SelectionAdapter() {
			@SuppressWarnings("deprecation" )
			@Override
			public void widgetSelected(SelectionEvent e) {
				Date tglMohon = new Date(datePermohonan.getYear() - 1900, datePermohonan.getMonth(), datePermohonan.getDay());
				Date tglMohonSampai = new Date(dateMohonSampai.getYear() - 1900, dateMohonSampai.getMonth(), dateMohonSampai.getDay());
				listMohon = ControllerFactory.getMainController().getCpMohonDAOImpl().getMohonList(tglMohon, tglMohonSampai);
				tbl_Mohon.removeAll();
				createColumn();
				loadTableMohon();
			}
		});
		btnCari.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnCari.setText("Cari..");
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		
		tbl_Mohon = new Table(comp, SWT.BORDER | SWT.FULL_SELECTION | SWT.VIRTUAL);
		tbl_Mohon.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				valSTS = ControllerFactory.getMainController().getCpMohonDetailDAOImpl().getDataSTS(tbl_Mohon.getItem(tbl_Mohon.getSelectionIndex()).getText(4), tbl_Mohon.getItem(tbl_Mohon.getSelectionIndex()).getText(7));
			}
		});
		tbl_Mohon.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		tbl_Mohon.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 4, 1));
		tbl_Mohon.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		tbl_Mohon.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE-1, SWT.NORMAL));
		tbl_Mohon.setHeaderVisible(true);
		tbl_Mohon.setLinesVisible(true);
		
		Composite compButton = new Composite(comp, SWT.NONE);
		compButton.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 4, 1));
		compButton.setLayout(new GridLayout(2, false));
		
		Button btnSetuju = new Button(compButton, SWT.NONE);
		btnSetuju.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(isSave()){
					if (tbl_Mohon.getSelectionCount() == 1)
					{
						int idx = tbl_Mohon.getSelectionIndex();
						final int idMohon = listMohon.get(idx).getIdMohon();
						if (ControllerFactory.getMainController().getCpMohonDAOImpl().editStatusMohon(idMohon, 1)){
							boolean status = true;
							for (int i=0;i<valSTS.size();i++){
								//final String[] listString = getParam(valSTS, i);
								//final int count = i+1;
								/*resultApi = false;
								ProgressMonitorDialog progress = new ProgressMonitorDialog(Display.getCurrent().getActiveShell());
								try {
									progress.run(false, false, new IRunnableWithProgress() {
										@Override
										public void run(IProgressMonitor monitor) throws InvocationTargetException,InterruptedException {
											resultApi = ApiPost.Instance.sendPostAngsuran(listString, listString[10], idMohon, count, monitor);
										}
									});
								} catch (InvocationTargetException | InterruptedException e1) {
									e1.printStackTrace();
								}
								if (resultApi){
									
								}else{
									//Rollback Permohonan
									if (listString[10] == ""){
										if (ControllerFactory.getMainController().getCpMohonDAOImpl().editStatusMohon(idMohon, 0)){
											StringBuffer sb2 = new StringBuffer();
											sb2.append("Rollback " +
													"idMohon:"+idMohon+
													" Status:Rollback"+
													" NPWPD:"+listMohon.get(idx).getNpwpd()+
													" SKP:"+listMohon.get(idx).getNoSkp());
											new LogImp().setLog(sb2.toString(), userModul.getDisplayName(), userModul.getSubModul().getNamaModul());
											MessageDialog.openError(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Insert STS Angsuran gagal.");
										}
									}else{
										MessageDialog.openError(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Ubah STS Angsuran gagal.");
									}
									status = false;
									break;
								}*/
							}
							if (status){
								tbl_Mohon.getItem(idx).setText(6, "Diterima");
								MessageDialog.openInformation(Display.getCurrent().getActiveShell(),"BPPRD Kota Medan", "Permohonan Disetujui");
								StringBuffer sb = new StringBuffer();
								sb.append("SAVE " +
										"idMohon:"+idMohon+
										" Status:Terima"+
										" NPWPD:"+listMohon.get(idx).getNpwpd()+
										" SKP:"+listMohon.get(idx).getNoSkp());
								new LogImp().setLog(sb.toString(), userModul.getDisplayName(), userModul.getSubModul().getNamaModul());
							}
								
						}
						else{
							MessageDialog.openInformation(Display.getCurrent().getActiveShell(),"BPPRD Kota Medan", "Data Gagal diproses");
						}
					}
				}else
					MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Anda tidak memiliki hak untuk menyimpan atau merubah data.");
			}
		});
		GridData gd_btnSetuju = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnSetuju.widthHint = 90;
		btnSetuju.setLayoutData(gd_btnSetuju);
		btnSetuju.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnSetuju.setText("Setuju");
		
		Button btnTolak = new Button(compButton, SWT.NONE);
		btnTolak.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(isSave()){
					if (tbl_Mohon.getSelectionCount() == 1)
					{
						int idx = tbl_Mohon.getSelectionIndex();
						int idMohon = listMohon.get(idx).getIdMohon();
						if (ControllerFactory.getMainController().getCpMohonDAOImpl().editStatusMohon(idMohon, 2)){
							tbl_Mohon.getItem(idx).setText(6, "Ditolak");
							MessageDialog.openInformation(Display.getCurrent().getActiveShell(),"BPPRD Kota Medan", "Permohonan Ditolak");
							StringBuffer sb = new StringBuffer();
							sb.append("SAVE " +
									"idMohon:"+idMohon+
									" Status:Tolak"+
									" NPWPD:"+listMohon.get(idx).getNpwpd()+
									" SKP:"+listMohon.get(idx).getNoSkp());
							new LogImp().setLog(sb.toString(), userModul.getDisplayName(), userModul.getSubModul().getNamaModul());
						}
						else{
							MessageDialog.openInformation(Display.getCurrent().getActiveShell(),"BPPRD Kota Medan", "Data Gagal diproses");
						}
					}
				}else
					MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Anda tidak memiliki hak untuk menyimpan atau merubah data.");
			}
		});
		GridData gd_btnTolak = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnTolak.widthHint = 90;
		btnTolak.setLayoutData(gd_btnTolak);
		btnTolak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnTolak.setText("Tolak");
		scrolledComposite.setContent(comp);
		scrolledComposite.setMinSize(comp.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		// TODO Auto-generated method stub

	}
	
	private void createColumn()
	{
		if(tbl_Mohon.getColumnCount() <= 0){
			String[] listColumn = {"No Mohon", "Tanggal Mohon", "Jenis Mohon", "Alasan Mohon", "NPWPD", "Nama Badan", "Alamat", "No SKP", "Status Mohon", "Total SKPDKB", "Jumlah Angsuran", "Angsuran"};
			int[] widthColumn = {50, 110, 100, 180, 110, 200, 200, 170, 100, 130, 110, 130};
			for (int i=listColumn.length - 1;i>=0;i--)
			{
				TableColumn colPajak = new TableColumn(tbl_Mohon, SWT.NONE,0);
				colPajak.setText(listColumn[i]);
				colPajak.setWidth(widthColumn[i]);
			}
		}
	}
	
	private void loadTableMohon()
	{
		String status = "";
		try{
			if (listMohon.size() > 0){
				for (int i=0;i<listMohon.size();i++){
					if (listMohon.get(i).getStatusMohon() == 0)
						status = "Proses";
					else if (listMohon.get(i).getStatusMohon() == 1)
						status = "Diterima";
					else
						status = "Ditolak";
					
					TableItem item = new TableItem(tbl_Mohon, SWT.NONE);
					item.setText(0, listMohon.get(i).getNoMohon());
					item.setText(1, String.valueOf(listMohon.get(i).getTglMohon()));
					item.setText(2, "Angsuran");
					item.setText(3, listMohon.get(i).getAlasanMohon());
					item.setText(4, listMohon.get(i).getNpwpd());
					item.setText(5, listMohon.get(i).getNamaBadan());
					item.setText(6, listMohon.get(i).getAlamat());
					item.setText(7, listMohon.get(i).getNoSkp());
					item.setText(8, status);
					item.setText(9, indFormat.format(listMohon.get(i).getTotalPajakTerhutang()));
					item.setText(10, listMohon.get(i).getJumlahAngsuran().toString());
					item.setText(11, indFormat.format(listMohon.get(i).getAngsuran()));
				}
			}
		}
		catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}
	
	private UserModul userModul = ControllerFactory.getMainController().getCpUserDAOImpl()
			.getHMUserModul(GlobalVariable.userModul.getIdUser()).get(12); // 2 merupakan id sub modul.
	
	private boolean isSave(){		
		return userModul.getSimpan();
	}
	
	@SuppressWarnings("rawtypes")
	private String[] getParam(HashMap<String, String[]> val, Integer idx){
		String[] retValue = new String[11];
		if(val != null){
			String[] st = val.get(idx.toString());
			for (int i=0;i<st.length;i++){
				retValue[0] = st[0];
				retValue[1] = st[1];
				retValue[2] = st[2];
				retValue[3] = st[3].substring(0, st[3].indexOf("."));
				retValue[4] = st[4].substring(0, st[4].indexOf("."));
				retValue[5] = st[5];
				retValue[6] = st[6];
				retValue[7] = st[7];
				retValue[8] = st[8];
				retValue[9] = st[9];
				retValue[10] = st[10];
			}
			return retValue;
		}
		return null;
	}
}
