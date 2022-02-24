package com.dispenda.realisasi.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.SWTResourceManager;

import com.dispenda.object.Preferences;

public class RealisasiPenerimaanView extends ViewPart {
	public RealisasiPenerimaanView() {
	}

	public static final String ID = RealisasiPenerimaanView.class.getName();
	private Table tblPendapatan;
	private Text txtTotalPokokPajak;
	private Color fontColor = new Color(Display.getCurrent(),Preferences.FONT_COLOR);
	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new GridLayout(1, false));
		
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		composite.setLayout(new GridLayout(3, false));
		
		Group grpDataRealisasiPenerimaan = new Group(composite, SWT.NONE);
		grpDataRealisasiPenerimaan.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 3, 1));
		grpDataRealisasiPenerimaan.addPaintListener(new PaintListener() {
			@Override
			public void paintControl(PaintEvent pe) {
				pe.gc.setForeground(fontColor);
				pe.gc.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.BOLD));
				pe.gc.drawText("Data Laporan Realisasi Penerimaan", 5, 0, false);
			}
		});
		grpDataRealisasiPenerimaan.setLayout(new GridLayout(5, false));
		
		Label lblBulanDanTahun = new Label(grpDataRealisasiPenerimaan, SWT.NONE);
		lblBulanDanTahun.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		lblBulanDanTahun.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblBulanDanTahun.setForeground(fontColor);
		lblBulanDanTahun.setText("Bulan dan Tahun");
		
		Combo cmbBulanTahun = new Combo(grpDataRealisasiPenerimaan, SWT.READ_ONLY);
		cmbBulanTahun.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));
		cmbBulanTahun.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		cmbBulanTahun.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		cmbBulanTahun.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		new Label(grpDataRealisasiPenerimaan, SWT.NONE);
		new Label(grpDataRealisasiPenerimaan, SWT.NONE);
		
		Label lblKewajibanPajak = new Label(grpDataRealisasiPenerimaan, SWT.NONE);
		lblKewajibanPajak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblKewajibanPajak.setForeground(fontColor);
		lblKewajibanPajak.setText("Kewajiban Pajak");
		
		Combo cmbKewajibanPajak1 = new Combo(grpDataRealisasiPenerimaan, SWT.READ_ONLY);
		GridData gd_cmbKewajibanPajak1 = new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1);
		gd_cmbKewajibanPajak1.widthHint = 180;
		cmbKewajibanPajak1.setLayoutData(gd_cmbKewajibanPajak1);
		cmbKewajibanPajak1.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		cmbKewajibanPajak1.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		cmbKewajibanPajak1.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		
		Label label = new Label(grpDataRealisasiPenerimaan, SWT.NONE);
		label.setForeground(fontColor);
		label.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		label.setText("-");
		
		Combo cmbKewajibanPajak2 = new Combo(grpDataRealisasiPenerimaan, SWT.READ_ONLY);
		GridData gd_cmbKewajibanPajak2 = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_cmbKewajibanPajak2.widthHint = 180;
		cmbKewajibanPajak2.setLayoutData(gd_cmbKewajibanPajak2);
		cmbKewajibanPajak2.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		cmbKewajibanPajak2.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		cmbKewajibanPajak2.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		new Label(grpDataRealisasiPenerimaan, SWT.NONE);
		
		Button btnTampil = new Button(grpDataRealisasiPenerimaan, SWT.NONE);
		GridData gd_btnTampil = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnTampil.widthHint = 90;
		btnTampil.setLayoutData(gd_btnTampil);
		btnTampil.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnTampil.setText("Tampil");
	
		
		Button btnCetak = new Button(grpDataRealisasiPenerimaan, SWT.NONE);
		GridData gd_btnCetak = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnCetak.widthHint = 90;
		btnCetak.setLayoutData(gd_btnCetak);
		btnCetak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnCetak.setText("Cetak");
		new Label(grpDataRealisasiPenerimaan, SWT.NONE);
		new Label(grpDataRealisasiPenerimaan, SWT.NONE);
		
		Group grpPendapatan =  new Group(composite, SWT.NONE);
		grpPendapatan.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));
		grpPendapatan.setLayout(new GridLayout(1, false));
		grpPendapatan.addPaintListener(new PaintListener() {
			@Override
			public void paintControl(PaintEvent pe) {
				pe.gc.setForeground(fontColor);
				pe.gc.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.BOLD));
				pe.gc.drawText("Data Laporan Realisasi Penerimaan Wajib Pajak", 5, 0, false);
			}
		});
		
		tblPendapatan = new Table(grpPendapatan, SWT.BORDER | SWT.FULL_SELECTION | SWT.VIRTUAL);
		tblPendapatan.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		tblPendapatan.setHeaderVisible(true);
		tblPendapatan.setLinesVisible(true);
		tblPendapatan.setBackground(SWTResourceManager.getColor(173, 216, 230));
		
		Label lblTotalPendapatanPokok = new Label(composite, SWT.NONE);
		lblTotalPendapatanPokok.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblTotalPendapatanPokok.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblTotalPendapatanPokok.setForeground(fontColor);
		lblTotalPendapatanPokok.setText("Total Pendapatan Pokok Pajak");
		
		txtTotalPokokPajak = new Text(composite, SWT.BORDER);
		txtTotalPokokPajak.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));
		txtTotalPokokPajak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtTotalPokokPajak.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		txtTotalPokokPajak.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		// TODO Auto-generated method stub

	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}
