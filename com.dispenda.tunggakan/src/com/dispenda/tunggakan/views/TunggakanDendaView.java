package com.dispenda.tunggakan.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.SWTResourceManager;

import com.dispenda.object.Preferences;

public class TunggakanDendaView extends ViewPart {
	public TunggakanDendaView() {
	}
	
	public static final String ID = TunggakanDendaView.class.getName();
	private Table tblDenda;
	private Text txtTotalDenda;
	private Color fontColor = new Color(Display.getCurrent(),Preferences.FONT_COLOR);
	@Override
	public void createPartControl(Composite parent) {
		
		ScrolledComposite scrolledComposite = new ScrolledComposite(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		
		Composite comp = new Composite(scrolledComposite, SWT.NONE);
		comp.setLayout(new GridLayout(2, false));
		
		Label lblMasaPajak = new Label(comp, SWT.NONE);
		lblMasaPajak.setForeground(fontColor);
		lblMasaPajak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblMasaPajak.setText("Masa Pajak");
		scrolledComposite.setContent(comp);
		scrolledComposite.setMinSize(comp.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
		Group composite = new Group(comp, SWT.NONE);
		composite.setLayout(new GridLayout(3, false));
		
		Button btnTanggal = new Button(composite, SWT.RADIO);
		btnTanggal.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BORDER));
		btnTanggal.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnTanggal.setText("Tanggal");
		
		Button btnBulan = new Button(composite, SWT.RADIO);
		btnBulan.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BORDER));
		btnBulan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnBulan.setText("Bulan");
		
		Button btnTahun = new Button(composite, SWT.RADIO);
		btnTahun.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BORDER));
		btnTahun.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnTahun.setText("Tahun");
		
		DateTime dateTime = new DateTime(composite, SWT.BORDER | SWT.DROP_DOWN);
		dateTime.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BORDER));
		dateTime.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		dateTime.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 3, 1));
		new Label(comp, SWT.NONE);
		
		Composite compButton = new Composite(comp, SWT.NONE);
		compButton.setLayout(new GridLayout(2, false));
		
		Button btnTampil = new Button(compButton, SWT.NONE);
		GridData gd_btnTampil = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnTampil.widthHint = 90;
		btnTampil.setLayoutData(gd_btnTampil);
		btnTampil.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BORDER));
		btnTampil.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnTampil.setText("Tampil");
		
		Button btnCetak = new Button(compButton, SWT.NONE);
		GridData gd_btnCetak = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnCetak.widthHint = 90;
		btnCetak.setLayoutData(gd_btnCetak);
		btnCetak.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BORDER));
		btnCetak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnCetak.setText("Cetak");
		
		tblDenda = new Table(comp, SWT.BORDER | SWT.FULL_SELECTION | SWT.VIRTUAL);
		tblDenda.setBackground(SWTResourceManager.getColor(173, 216, 230));
		tblDenda.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		tblDenda.setHeaderVisible(true);
		tblDenda.setLinesVisible(true);
		
		Label lblTotalDenda = new Label(comp, SWT.NONE);
		lblTotalDenda.setForeground(fontColor);
		lblTotalDenda.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblTotalDenda.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblTotalDenda.setText("Total Denda :");
		
		txtTotalDenda = new Text(comp, SWT.BORDER);
		txtTotalDenda.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtTotalDenda.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		GridData gd_txtTotalDenda = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_txtTotalDenda.widthHint = 250;
		txtTotalDenda.setLayoutData(gd_txtTotalDenda);

	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}
