package com.dispenda.control.views;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
//import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.SWTResourceManager;

import com.dispenda.control.dialogs.PejabatDialog;
import com.dispenda.model.Pejabat;
import com.dispenda.model.PejabatProvider;
import com.dispenda.object.Preferences;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class PermintaanPerubahanView extends ViewPart {
	public PermintaanPerubahanView() {
	}
	public static final String ID = PermintaanPerubahanView.class.getName();
	private Text txtRincianJenisPermintaan;
	private Text txtNamaPengaju;
	private Text txtNamaBidang;
	private Text txtNoHandphone;
	private Text txtJudulPermintaan;
	private Text txtNPWPD_2;
	private Text txtNamaBadan;
	private Text txtRincianPermintaan;
	private Text txtNPWPD;
	private Color fontColor = new Color(Display.getCurrent(),Preferences.FONT_COLOR);

	@Override
	public void createPartControl(Composite parent) {
//		parent.setBackground(new Color(Display.getCurrent(), 32, 172, 192));
		
		ScrolledComposite scrolledComposite = new ScrolledComposite(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		
		Composite composite = new Composite(scrolledComposite, SWT.NONE);
		composite.setLayout(new GridLayout(7, false));
		
		Label lblJenisPerubahan = new Label(composite, SWT.NONE);
		lblJenisPerubahan.setForeground(fontColor);
		lblJenisPerubahan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblJenisPerubahan.setText("Jenis Perubahan");
		
		Button btnSptpd = new Button(composite, SWT.CHECK);
		btnSptpd.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnSptpd.setText("SPTPD");
		
		Button btnSspd = new Button(composite, SWT.CHECK);
		btnSspd.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnSspd.setText("SSPD");
		
		Button btnPemeriksaan = new Button(composite, SWT.CHECK);
		btnPemeriksaan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnPemeriksaan.setText("Pemeriksaan");
		
		Button btnLaporan = new Button(composite, SWT.CHECK);
		btnLaporan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnLaporan.setText("Laporan");
		
		Button btnLainnya = new Button(composite, SWT.CHECK);
		btnLainnya.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnLainnya.setText("Lainnya");
		
		txtRincianJenisPermintaan = new Text(composite, SWT.BORDER);
		txtRincianJenisPermintaan.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		txtRincianJenisPermintaan.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtRincianJenisPermintaan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		Label label = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		
		Label lblInformasiPengaju = new Label(composite, SWT.NONE);
		lblInformasiPengaju.setForeground(fontColor);
		lblInformasiPengaju.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblInformasiPengaju.setText("Informasi Pengaju");
		
		Label lblNamaPengaju = new Label(composite, SWT.NONE);
		lblNamaPengaju.setForeground(fontColor);
		lblNamaPengaju.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblNamaPengaju.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 6, 1));
		lblNamaPengaju.setText("Nama Pengaju");
		new Label(composite, SWT.NONE);
		
		txtNamaPengaju = new Text(composite, SWT.BORDER);
		txtNamaPengaju.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 6, 1));
		txtNamaPengaju.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtNamaPengaju.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		new Label(composite, SWT.NONE);
		
		Label lblNamaBidangjika = new Label(composite, SWT.NONE);
		lblNamaBidangjika.setForeground(fontColor);
		lblNamaBidangjika.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblNamaBidangjika.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 6, 1));
		lblNamaBidangjika.setText("Nama Bidang (Jika Pegawai)");
		new Label(composite, SWT.NONE);
		
		txtNamaBidang = new Text(composite, SWT.BORDER);
		txtNamaBidang.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 6, 1));
		txtNamaBidang.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtNamaBidang.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		new Label(composite, SWT.NONE);
		
		Label lblNpwpdjikaWajib = new Label(composite, SWT.NONE);
		lblNpwpdjikaWajib.setForeground(fontColor);
		lblNpwpdjikaWajib.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblNpwpdjikaWajib.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 6, 1));
		lblNpwpdjikaWajib.setText("NPWPD (Jika Wajib Pajak)");
		new Label(composite, SWT.NONE);
		
		txtNPWPD = new Text(composite, SWT.BORDER);
		txtNPWPD.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 6, 1));
		txtNPWPD.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtNPWPD.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		new Label(composite, SWT.NONE);
		
		Label lblNoHandphone = new Label(composite, SWT.NONE);
		lblNoHandphone.setForeground(fontColor);
		lblNoHandphone.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblNoHandphone.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 6, 1));
		lblNoHandphone.setText("No. Handphone");
		new Label(composite, SWT.NONE);
		
		txtNoHandphone = new Text(composite, SWT.BORDER);
		txtNoHandphone.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 6, 1));
		txtNoHandphone.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtNoHandphone.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		Label label_1 = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		
		Label lblInformasiPermintaan = new Label(composite, SWT.NONE);
		lblInformasiPermintaan.setForeground(fontColor);
		lblInformasiPermintaan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblInformasiPermintaan.setText("Informasi Permintaan");
		
		Label lblJudulPermintaan = new Label(composite, SWT.NONE);
		lblJudulPermintaan.setForeground(fontColor);
		lblJudulPermintaan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblJudulPermintaan.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 6, 1));
		lblJudulPermintaan.setText("Judul Permintaan");
		new Label(composite, SWT.NONE);
		
		txtJudulPermintaan = new Text(composite, SWT.BORDER);
		txtJudulPermintaan.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 6, 1));
		txtJudulPermintaan.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtJudulPermintaan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		new Label(composite, SWT.NONE);
		
		Label lblNpwpd = new Label(composite, SWT.NONE);
		lblNpwpd.setForeground(fontColor);
		lblNpwpd.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblNpwpd.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 6, 1));
		lblNpwpd.setText("NPWPD");
		new Label(composite, SWT.NONE);
		
		txtNPWPD_2 = new Text(composite, SWT.BORDER);
		txtNPWPD_2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 6, 1));
		txtNPWPD_2.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtNPWPD_2.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		new Label(composite, SWT.NONE);
		
		Label lblNamaBadan = new Label(composite, SWT.NONE);
		lblNamaBadan.setForeground(fontColor);
		lblNamaBadan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblNamaBadan.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 6, 1));
		lblNamaBadan.setText("Nama Badan");
		new Label(composite, SWT.NONE);
		
		txtNamaBadan = new Text(composite, SWT.BORDER);
		txtNamaBadan.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 6, 1));
		txtNamaBadan.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtNamaBadan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		new Label(composite, SWT.NONE);
		
		Label lblRincianPermintaan = new Label(composite, SWT.NONE);
		lblRincianPermintaan.setForeground(fontColor);
		lblRincianPermintaan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblRincianPermintaan.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 6, 1));
		lblRincianPermintaan.setText("Rincian Permintaan");
		new Label(composite, SWT.NONE);
		
		txtRincianPermintaan = new Text(composite, SWT.BORDER);
		txtRincianPermintaan.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 6, 1));
		txtRincianPermintaan.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtRincianPermintaan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
	    String[] titlesPejabat = { "NIP", "Nama Pejabat", "Pangkat", "Jabatan"};
	    int[] boundsPejabat = {250, 200, 200,250};
		
		scrolledComposite.setContent(composite);
		scrolledComposite.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));

	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}
}