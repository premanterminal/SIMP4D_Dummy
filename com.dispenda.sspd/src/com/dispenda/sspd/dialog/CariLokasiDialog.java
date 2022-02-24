package com.dispenda.sspd.dialog;

import java.awt.Window;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import com.dispenda.controller.ControllerFactory;
import com.dispenda.object.Preferences;

import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;

public class CariLokasiDialog extends Dialog {
	private Text txtCari;
	private String value;

	public CariLokasiDialog(Shell parentShell) {
		super(parentShell);
		value = "";
	}

	@Override
	protected void setShellStyle(int newShellStyle) {
		super.setShellStyle(SWT.NONE|SWT.APPLICATION_MODAL);
	}
	
	@Override
	protected Control createDialogArea(Composite main) {
		Composite parent = (Composite) super.createDialogArea(main);
		GridLayout gridLayout = (GridLayout) parent.getLayout();
		gridLayout.numColumns = 2;
		
		Label lblCari = new Label(parent, SWT.NONE);
		lblCari.setFont(SWTResourceManager.getFont("Calibri", 12, SWT.NORMAL));
		lblCari.setText("Cari Lokasi");
		
		txtCari = new Text(parent, SWT.BORDER);
		txtCari.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.keyCode == SWT.CR || e.keyCode == SWT.KEYPAD_CR){
					value = txtCari.getText();
					close();
				}
			}
		});
		txtCari.setFont(SWTResourceManager.getFont("Calibri", 12, SWT.NORMAL));
		txtCari.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(parent, SWT.NONE);
		
		Composite composite = new Composite(parent, SWT.NONE);
		GridData gd_composite = new GridData(SWT.LEFT, SWT.TOP, false, true, 1, 3);
		gd_composite.heightHint = 51;
		composite.setLayoutData(gd_composite);
		composite.setLayout(new GridLayout(2, false));
		
		Button btnCari = new Button(composite, SWT.NONE);
		btnCari.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				value = txtCari.getText();
				close();
			}
		});
		GridData gd_btnCari = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnCari.widthHint = 90;
		btnCari.setLayoutData(gd_btnCari);
		btnCari.setFont(SWTResourceManager.getFont("Calibri", 12, SWT.NORMAL));
		btnCari.setText("Cari");
		
		Button btnCancel = new Button(composite, SWT.NONE);
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				close();
			}
		});
		GridData gd_btnCancel = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnCancel.widthHint = 90;
		btnCancel.setLayoutData(gd_btnCancel);
		btnCancel.setFont(SWTResourceManager.getFont("Calibri", 12, SWT.NORMAL));
		btnCancel.setText("Batal");
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		main.setBackgroundMode(SWT.INHERIT_FORCE);
		Control control = super.createDialogArea(parent);
		new Label(parent, SWT.NONE);
		GridData gd_control = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_control.widthHint = 20;
		control.setLayoutData(gd_control);
		return control;
		
	}
	
	
	@Override
	protected Control createButtonBar(Composite parent) {
		return null;
	}
	
	@Override
	protected void okPressed() {
		// TODO Auto-generated method stub
		value = txtCari.getText();
	}
	
	public String getEnteredText(){
		return value;
	}
}
