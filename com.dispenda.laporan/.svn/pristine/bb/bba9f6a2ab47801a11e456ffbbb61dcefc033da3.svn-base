package com.dispenda.laporan.views;

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

public class LaporanSptpdView extends ViewPart {
	public LaporanSptpdView() {
	}

	public static final String ID = LaporanSptpdView.class.getName();
	private Table tblSPTPD;
	private Color fontColor = new Color(Display.getCurrent(),Preferences.FONT_COLOR);
	@Override
	public void createPartControl(Composite arg0) {
		
		Composite composite = new Composite(arg0, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));
		
		Group grpDataLaporanSPTPD = new Group(composite, SWT.NONE);
		grpDataLaporanSPTPD.setLayout(new GridLayout(5, false));
		grpDataLaporanSPTPD.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		grpDataLaporanSPTPD.addPaintListener(new PaintListener() {
			@Override
			public void paintControl(PaintEvent pe) {
				pe.gc.setForeground(fontColor);
				pe.gc.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.BOLD));
				pe.gc.drawText("Data Laporan SPTPD", 5, 0, false);
			}
		});
		
		Label label_1 = new Label(grpDataLaporanSPTPD, SWT.NONE);
		label_1.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		label_1.setForeground(fontColor);
		label_1.setText("Kewajiban Pajak");
		label_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		
		Combo cmbKewajibanPajak = new Combo(grpDataLaporanSPTPD, SWT.NONE);
		cmbKewajibanPajak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		GridData gd_cmbKewajibanPajak = new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1);
		gd_cmbKewajibanPajak.widthHint = 200;
		cmbKewajibanPajak.setLayoutData(gd_cmbKewajibanPajak);
		cmbKewajibanPajak.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		cmbKewajibanPajak.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		
		Label label = new Label(grpDataLaporanSPTPD, SWT.NONE);
		label.setForeground(fontColor);
		label.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label.setText("-");
		
		Combo cmbKewajibanPajak1 = new Combo(grpDataLaporanSPTPD, SWT.NONE);
		cmbKewajibanPajak1.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		GridData gd_cmbKewajibanPajak1 = new GridData(SWT.CENTER, SWT.BOTTOM, false, false, 1, 1);
		gd_cmbKewajibanPajak1.widthHint = 200;
		cmbKewajibanPajak1.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		cmbKewajibanPajak1.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		cmbKewajibanPajak1.setLayoutData(gd_cmbKewajibanPajak1);
		
		Label label_2 = new Label(grpDataLaporanSPTPD, SWT.NONE);
		label_2.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		label_2.setForeground(fontColor);
		label_2.setText("Masa Pajak");
		
		Combo cmbMasaPajak = new Combo(grpDataLaporanSPTPD, SWT.NONE);
		cmbMasaPajak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		GridData gd_cmbMasaPajak = new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1);
		gd_cmbMasaPajak.widthHint = 200;
		cmbMasaPajak.setLayoutData(gd_cmbMasaPajak);
		cmbMasaPajak.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		cmbMasaPajak.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		new Label(grpDataLaporanSPTPD, SWT.NONE);
		new Label(grpDataLaporanSPTPD, SWT.NONE);
		
		Label lblKeterlambatan = new Label(grpDataLaporanSPTPD, SWT.NONE);
		lblKeterlambatan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblKeterlambatan.setForeground(fontColor);
		lblKeterlambatan.setText("Keterlambatan");
		
		Group group = new Group(grpDataLaporanSPTPD, SWT.NONE);
		group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 2, 1));
		group.setLayout(new GridLayout(2, false));
		
		Button btnRadio50 = new Button(group, SWT.RADIO);
		btnRadio50.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnRadio50.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnRadio50.setText(">= 50");
		
		
		Button btnRadioAkhirBulan = new Button(group, SWT.RADIO);
		btnRadioAkhirBulan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnRadioAkhirBulan.setText("Akhir Bulan");
		new Label(grpDataLaporanSPTPD, SWT.NONE);
		new Label(grpDataLaporanSPTPD, SWT.NONE);
		new Label(grpDataLaporanSPTPD, SWT.NONE);
		
		Button btnTampil = new Button(grpDataLaporanSPTPD, SWT.NONE);
		btnTampil.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		GridData gd_btnTampil = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnTampil.widthHint = 80;
		btnTampil.setLayoutData(gd_btnTampil);
		btnTampil.setText("Tampil");
		
		Button btnCetak = new Button(grpDataLaporanSPTPD, SWT.NONE);
		btnCetak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		GridData gd_btnCetak = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnCetak.widthHint = 80;
		btnCetak.setLayoutData(gd_btnCetak);
		btnCetak.setText("Cetak");
		
		
		new Label(grpDataLaporanSPTPD, SWT.NONE);
		new Label(grpDataLaporanSPTPD, SWT.NONE);
		
		Group grpSPTPD = new Group(composite, SWT.NONE);
		grpSPTPD.setLayout(new GridLayout(1, false));
		grpSPTPD.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		grpSPTPD.addPaintListener(new PaintListener() {
			@Override
			public void paintControl(PaintEvent pe) {
				pe.gc.setForeground(fontColor);
				pe.gc.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.BOLD));
				pe.gc.drawText("SPTPD", 5, 0, false);
			}
		});
		
		tblSPTPD = new Table(grpSPTPD, SWT.BORDER | SWT.FULL_SELECTION | SWT.VIRTUAL);
		tblSPTPD.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 2));
		tblSPTPD.setBackground(SWTResourceManager.getColor(173, 216, 230));
		tblSPTPD.setHeaderVisible(true);
		tblSPTPD.setLinesVisible(true);
		// TODO Auto-generated method stub

	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}
