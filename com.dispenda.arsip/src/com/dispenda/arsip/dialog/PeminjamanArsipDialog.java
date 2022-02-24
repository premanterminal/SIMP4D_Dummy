package com.dispenda.arsip.dialog;

import java.sql.Date;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;

import com.dispenda.model.ArsipLogProvider;
import com.dispenda.object.GlobalVariable;
import com.dispenda.object.Preferences;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.DateTime;

public class PeminjamanArsipDialog extends Dialog{
	private Text txtNoSKP;
	private Text txtPinjam;
	private Text txtKeterangan;
	private Color fontColor = new Color(Display.getCurrent(),Preferences.FONT_COLOR);
	private DateTime calTanggal;
	public PeminjamanArsipDialog(Shell parentShell) {
		super(parentShell);
		// TODO Auto-generated constructor stub
	}

	protected Control createDialogArea(Composite main) {
		Composite parent = (Composite) super.createDialogArea(main);
		parent.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		GridLayout gl_parent = new GridLayout(2, false);
		gl_parent.horizontalSpacing = 30;
		gl_parent.verticalSpacing = 20;
		parent.setLayout(gl_parent);
		parent.setBackground(SWTResourceManager.getColor(Preferences.BACKGROUND_COLOR));
		parent.setBackgroundMode(SWT.INHERIT_FORCE);
		
		Label lblNoSkp = new Label(parent, SWT.NONE);
		lblNoSkp.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblNoSkp.setForeground(fontColor);
		lblNoSkp.setText("No SKP");
		
		txtNoSKP = new Text(parent, SWT.BORDER | SWT.READ_ONLY);
		txtNoSKP.setText(ArsipLogProvider.INSTANCE.getNoSKP());
		GridData gd_txtNoSKP = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		txtNoSKP.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtNoSKP.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		gd_txtNoSKP.widthHint = 150;
		txtNoSKP.setLayoutData(gd_txtNoSKP);
		
		Label lblTanggal = new Label(parent, SWT.NONE);
		lblTanggal.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblTanggal.setForeground(fontColor);
		lblTanggal.setText("Tanggal");
		
		calTanggal = new DateTime(parent, SWT.BORDER | SWT.DROP_DOWN);
		calTanggal.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		calTanggal.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		
		Label lblPeminjam = new Label(parent, SWT.NONE);
		lblPeminjam.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblPeminjam.setForeground(fontColor);
		lblPeminjam.setText("Peminjam");
		
		txtPinjam = new Text(parent, SWT.BORDER);
		txtPinjam.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtPinjam.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		GridData gd_txtPinjam = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_txtPinjam.widthHint = 198;
		txtPinjam.setLayoutData(gd_txtPinjam);
		
		Label lblKeterangan = new Label(parent, SWT.NONE);
		lblKeterangan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblKeterangan.setForeground(fontColor);
		lblKeterangan.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		lblKeterangan.setText("Keterangan");
		
		txtKeterangan = new Text(parent, SWT.BORDER | SWT.WRAP | SWT.MULTI);
		txtKeterangan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtKeterangan.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		GridData gd_txtKeterangan = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_txtKeterangan.heightHint = 50;
		txtKeterangan.setLayoutData(gd_txtKeterangan);
		
		return parent;
	}
	
	@Override
	protected Control createButtonBar(Composite parent) {
		GridLayout gl_parent = new GridLayout(2, false);
		parent.setLayout(gl_parent);
		Button choose = new Button(parent, SWT.PUSH);
		GridData gd_btnchoose = new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1);
		gd_btnchoose.widthHint = 90;
		choose.setLayoutData(gd_btnchoose);
		choose.setText("Simpan");
		choose.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (txtPinjam.getText().equalsIgnoreCase("")){
					MessageDialog.openError(Display.getCurrent().getActiveShell(), "Arsip", "Silahkan masukkan nama peminjam");
					txtPinjam.forceFocus();
				}else{
					ArsipLogProvider.INSTANCE.setTanggal(new Date(calTanggal.getYear() - 1900, calTanggal.getMonth(), calTanggal.getDay()));
					ArsipLogProvider.INSTANCE.setPeminjam(txtPinjam.getText());
					ArsipLogProvider.INSTANCE.setKeterangan(txtKeterangan.getText());
					close();
				}
			}
		});
		
		Button cancel = new Button(parent, SWT.PUSH);
		GridData gd_btnLihatSkpdkb = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnLihatSkpdkb.widthHint = 90;
		cancel.setLayoutData(gd_btnLihatSkpdkb);
		cancel.setText("Batal");
		cancel.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				ArsipLogProvider.INSTANCE.setPeminjam("");
				close();
			}
		});
		return parent;
	}
	protected void setShellStyle(int newShellStyle) {
		super.setShellStyle(SWT.NONE|SWT.APPLICATION_MODAL);
	}
}
