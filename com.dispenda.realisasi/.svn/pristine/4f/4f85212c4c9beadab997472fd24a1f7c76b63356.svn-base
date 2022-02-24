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
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.SWTResourceManager;

import com.dispenda.object.Preferences;


public class RealisasiPenerimaanWajibPajakView extends ViewPart {
	public RealisasiPenerimaanWajibPajakView() {
	}

	public static final String ID = RealisasiPenerimaanWajibPajakView.class.getName();
	private Table tblPendapatan;
	private Color fontColor = new Color(Display.getCurrent(),Preferences.FONT_COLOR);
	@Override
	public void createPartControl(Composite arg0) {
		
		Composite composite = new Composite(arg0, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));
		
		Group grpDataLaporan = new Group(composite, SWT.NONE);
		grpDataLaporan.setLayout(new GridLayout(3, false));
		grpDataLaporan.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		grpDataLaporan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.BOLD));
		grpDataLaporan.addPaintListener(new PaintListener() {
			@Override
			public void paintControl(PaintEvent pe) {
				pe.gc.setForeground(fontColor);
				pe.gc.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.BOLD));
				pe.gc.drawText("Data Laporan Realisasi Penerimaan Wajib Pajak", 5, 0, false);
			}
		});
		
		Label lblKewajibanPajak = new Label(grpDataLaporan, SWT.NONE);
		lblKewajibanPajak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblKewajibanPajak.setForeground(fontColor);
		GridData gd_lblKewajibanPajak = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_lblKewajibanPajak.widthHint = 125;
		lblKewajibanPajak.setLayoutData(gd_lblKewajibanPajak);
		lblKewajibanPajak.setText("Kewajiban Pajak");
		
		Combo cmbKewajibanPajak = new Combo(grpDataLaporan, SWT.READ_ONLY);
		cmbKewajibanPajak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		cmbKewajibanPajak.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		cmbKewajibanPajak.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		GridData gd_cmbKewajibanPajak = new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1);
		gd_cmbKewajibanPajak.widthHint = 200;
		cmbKewajibanPajak.setLayoutData(gd_cmbKewajibanPajak);
		
		Label lblKecamatan = new Label(grpDataLaporan, SWT.NONE);
		lblKecamatan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblKecamatan.setForeground(fontColor);
		lblKecamatan.setText("Kecamatan");
		
		Combo cmbKecamatan = new Combo(grpDataLaporan, SWT.READ_ONLY);
		cmbKecamatan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		cmbKecamatan.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		cmbKecamatan.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		cmbKecamatan.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));
		
		Label lblBulanRealisasi = new Label(grpDataLaporan, SWT.NONE);
		lblBulanRealisasi.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblBulanRealisasi.setForeground(fontColor);
		lblBulanRealisasi.setText("Bulan Realisasi");
		
		Combo cmbBulanrealisasi = new Combo(grpDataLaporan, SWT.READ_ONLY);
		cmbBulanrealisasi.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		cmbBulanrealisasi.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		cmbBulanrealisasi.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		cmbBulanrealisasi.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));
		new Label(grpDataLaporan, SWT.NONE);
		
		Button btnTampil = new Button(grpDataLaporan, SWT.NONE);
		btnTampil.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		GridData gd_btnTampil = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_btnTampil.widthHint = 80;
		btnTampil.setLayoutData(gd_btnTampil);
		btnTampil.setText("Tampil");
		
		Button btnCetak = new Button(grpDataLaporan, SWT.NONE);
		btnCetak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		GridData gd_btnCetak = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnCetak.widthHint = 80;
		btnCetak.setLayoutData(gd_btnCetak);
		btnCetak.setText("Cetak");
		
		Group grpPendapatan = new Group(composite, SWT.NONE);
		grpPendapatan.setLayout(new GridLayout(1, false));
		grpPendapatan.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		grpPendapatan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.BOLD));
		grpPendapatan.addPaintListener(new PaintListener() {
			@Override
			public void paintControl(PaintEvent pe) {
				pe.gc.setForeground(fontColor);
				pe.gc.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.BOLD));
				pe.gc.drawText("Pendapatan", 5, 0, false);
			}
		});
		
		tblPendapatan = new Table(grpPendapatan, SWT.BORDER | SWT.FULL_SELECTION | SWT.VIRTUAL);
		tblPendapatan.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		tblPendapatan.setHeaderVisible(true);
		tblPendapatan.setLinesVisible(true);
		tblPendapatan.setBackground(SWTResourceManager.getColor(173, 216, 230));
		
		
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}
}
