package com.dispenda.laporan.views;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.TableTreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.custom.TableTree;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.SWTResourceManager;

import com.dispenda.controller.ControllerFactory;
import com.dispenda.controller.UIController;
import com.dispenda.laporan.tree.InputTreeDetail;
import com.dispenda.laporan.tree.LPBTreeContentProvider;
import com.dispenda.laporan.tree.LPBTreeLabelProvider;
import com.dispenda.model.PajakProvider;
import com.dispenda.object.Preferences;

@SuppressWarnings("deprecation")
public class LaporanPendapatanHarianView extends ViewPart {
	public LaporanPendapatanHarianView() {
	}
	
	public static final String ID = LaporanPendapatanHarianView.class.getName();
	private Color fontColor = new Color(Display.getCurrent(),Preferences.FONT_COLOR);
	private DateTime calBulan;
	private Spinner calTahun;
	private Timestamp dateNow;
	private Button btnBulan;
	private Button btnTahun;
	private Date masaPajakDari;
	private Date masaPajakSampai;
	private String masaPajak;
	private Combo cmbKewajibanPajak;
	private TableTreeViewer tableTreeViewer;
	
	@Override
	public void createPartControl(Composite parent) {
		Locale.setDefault(new Locale("id","ID"));
		dateNow = (java.sql.Timestamp)ControllerFactory.getMainController().getCpWajibPajakDAOImpl().getDateNow();
		parent.setLayout(new GridLayout(1, false));
		
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		composite.setLayout(new GridLayout(4, false));
		
		Label lblKewajibanPajak = new Label(composite, SWT.NONE);
		lblKewajibanPajak.setText("Kewajiban Pajak");
		lblKewajibanPajak.setForeground(fontColor);
		lblKewajibanPajak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		cmbKewajibanPajak = new Combo(composite, SWT.READ_ONLY);
		cmbKewajibanPajak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		cmbKewajibanPajak.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		GridData gd_cmbKewajibanPajak = new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1);
		gd_cmbKewajibanPajak.widthHint = 209;
		UIController.INSTANCE.loadPajak(cmbKewajibanPajak, PajakProvider.INSTANCE.getPajak().toArray());
		cmbKewajibanPajak.setLayoutData(gd_cmbKewajibanPajak);
		
		Label lblNewLabel = new Label(composite, SWT.NONE);
		lblNewLabel.setText("Tanggal Realisasi");
		lblNewLabel.setForeground(fontColor);
		lblNewLabel.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		Group group = new Group(composite, SWT.NONE);
		GridData gd_group = new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1);
		gd_group.widthHint = 156;
		group.setLayoutData(gd_group);
		group.setLayout(new GridLayout(3, false));
		
		btnBulan = new Button(group, SWT.RADIO);
		btnBulan.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				calBulan.setVisible(true);
				calTahun.setVisible(false);
				setMasaPajak();
			}
		});
		btnBulan.setText("Bulan");
		btnBulan.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BORDER));
		btnBulan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		btnTahun = new Button(group, SWT.RADIO);
		btnTahun.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				calBulan.setVisible(false);
				calTahun.setVisible(true);
				setMasaPajak();
			}
		});
		btnTahun.setText("Tahun");
		btnTahun.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BORDER));
		btnTahun.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		new Label(group, SWT.NONE);
		
		Composite composite_1 = new Composite(group, SWT.NONE);
		composite_1.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 3, 1));
		composite_1.setLayout(new StackLayout());
		
		calTahun = new Spinner(composite_1, SWT.BORDER);
		calTahun.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setMasaPajak();
			}
		});
		calTahun.setVisible(false);
		calTahun.setTextLimit(9999);
		calTahun.setMaximum(dateNow.getYear() + 1900);
		calTahun.setMinimum(2016);
		calTahun.setSelection(dateNow.getYear() + 1900);
		calTahun.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		calTahun.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		calTahun.setVisible(true);
		calTahun.setValues(dateNow.getYear() + 1900, 2006, dateNow.getYear() + 1900, 0, 1, 10);
		calTahun.pack();
		
		calBulan = new DateTime(composite_1, SWT.BORDER | SWT.DROP_DOWN | SWT.SHORT);
		calBulan.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (calBulan.getYear() < 2016){
					MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Tanggal tidak valid");
					calBulan.setDate(2016, 0, 1);
				}
				else{
					setMasaPajak();
				}
					
			}
		});
		calBulan.setVisible(false);
		calBulan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		calBulan.setDay(1);
		calBulan.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		new Label(composite, SWT.NONE);
		
		Button btnTampil = new Button(composite, SWT.NONE);
		btnTampil.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (cmbKewajibanPajak.getText().equalsIgnoreCase("")){
					//result = ControllerFactory.getMainController().getCpSspdDAOImpl().getLaporanRealisasi("", masaPajakDari, masaPajakSampai);

					tableTreeViewer.setInput(new InputTreeDetail(calTahun.getSelection()));
//					tableTreeViewer.setInput(new InputTreeRekap(Calendar.getInstance()));
				}else{
					//result = ControllerFactory.getMainController().getCpSspdDAOImpl().getLaporanRealisasi(cmbKewajibanPajak.getData(Integer.toString(cmbKewajibanPajak.getSelectionIndex())).toString(), masaPajakDari, masaPajakSampai);
				}
			}
		});
		GridData gd_btnTampil = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnTampil.widthHint = 90;
		btnTampil.setLayoutData(gd_btnTampil);
		btnTampil.setText("Tampil");
		btnTampil.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		Button btnBaru = new Button(composite, SWT.NONE);
		btnBaru.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				cmbKewajibanPajak.deselectAll();
			}
		});
		GridData gd_btnBaru = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnBaru.widthHint = 90;
		btnBaru.setLayoutData(gd_btnBaru);
		btnBaru.setText("Baru");
		btnBaru.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		Button btnCetak = new Button(composite, SWT.NONE);
		GridData gd_btnCetak = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnCetak.widthHint = 90;
		btnCetak.setLayoutData(gd_btnCetak);
		btnCetak.setText("Cetak");
		btnCetak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		tableTreeViewer = new TableTreeViewer(composite, SWT.BORDER | SWT.FULL_SELECTION);
		TableTree tableTree = tableTreeViewer.getTableTree();
		tableTree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 4, 1));
		tableTree.getTable().setLinesVisible(true);
		tableTree.getTable().setHeaderVisible(true);
		tableTree.getTable().setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		tableTree.getTable().setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		loadColumn();
		
		tableTreeViewer.setContentProvider(new LPBTreeContentProvider());
		tableTreeViewer.setLabelProvider(new LPBTreeLabelProvider());
//		loadMasaPajakDari();
		
	}

	private void loadColumn() {
		String[] colName = new String[]{"No Reg.","NPWPD","Nama Badan","Bid. Usaha","No SKP","No SSPD","Bulan SKP","Cicilan Ke","Tgl Bayar","Pokok","Denda","Jumlah"};
		Integer[] colWidth = new Integer[]{60,100,200,160,130,130,85,85,100,150,150,150};
//		String[] colName = new String[]{"Nama Pajak","Pokok","Denda","Jumlah"};
//		Integer[] colWidth = new Integer[]{200,150,150,150};
		Table table = tableTreeViewer.getTableTree().getTable();
		for(int i=0;i<colName.length;i++){
			TableColumn col = new TableColumn(table, SWT.NONE);
			col.setText(colName[i]);
			col.setWidth(colWidth[i]);
		}
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}
	
	public void setMasaPajak()
	{
		if (btnBulan.getSelection())
		{
			Calendar instance = Calendar.getInstance();
			instance.set(Calendar.DAY_OF_MONTH, calBulan.getDay());
			instance.set(Calendar.MONTH, calBulan.getMonth());
			instance.set(Calendar.YEAR, calBulan.getYear());
			masaPajakDari = new Date(calBulan.getYear() - 1900, calBulan.getMonth(), 1);
			masaPajakSampai = new Date(calBulan.getYear() - 1900, calBulan.getMonth(), instance.getActualMaximum(Calendar.DATE));
			masaPajak = UIController.INSTANCE.formatMonth(calBulan.getMonth() + 1, Locale.getDefault()) + " " + calBulan.getYear();
		}
		if (btnTahun.getSelection())
		{
			masaPajakDari = new Date(calTahun.getSelection() - 1900, 0, 1);
			masaPajakSampai = new Date(calTahun.getSelection() - 1900, 11, 31);
			masaPajak = Integer.toString(calTahun.getSelection());
		}
	
	}

}
