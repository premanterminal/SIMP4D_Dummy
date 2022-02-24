package com.dispenda.dialog;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;

import com.dispenda.object.Preferences;

public class PrintChooserDialog extends Dialog{
	
	private String result = "";
	private Button btnExport;
	private Button btnPrint;
	
	public String getResult() {
		return result;
	}

	public PrintChooserDialog(Shell parent) {
		super(parent);
		
	}
	@Override
	protected Control createDialogArea(Composite main) {
		Composite parent = (Composite) super.createDialogArea(main);
		GridLayout gl_parent = new GridLayout(2, false);
		gl_parent.horizontalSpacing = 30;
		gl_parent.verticalSpacing = 20;
		parent.setLayout(gl_parent);
//		main.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BORDER));
		parent.setBackground(SWTResourceManager.getColor(Preferences.BACKGROUND_COLOR));
		parent.setBackgroundMode(SWT.INHERIT_FORCE);
//		main.setFont(SWTResourceManager.getFont("Calibri",11,SWT.NORMAL));
		
		Label lblExport = new Label(parent, SWT.NONE);
		lblExport.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
		lblExport.setForeground(SWTResourceManager.getColor(Preferences.FONT_COLOR));
		lblExport.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE,Preferences.FONT_SIZE,SWT.NORMAL));
		lblExport.setText("Export to Excel");
		
		btnExport = new Button(parent, SWT.RADIO);
		btnExport.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
		btnExport.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE,Preferences.FONT_SIZE,SWT.NORMAL));
		btnExport.setText("");
		
		Label lblPrint = new Label(parent, SWT.NONE);
		lblPrint.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
		lblPrint.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE,Preferences.FONT_SIZE,SWT.NORMAL));
		lblPrint.setForeground(SWTResourceManager.getColor(Preferences.FONT_COLOR));
		lblPrint.setText("Print to PDF");
		
		btnPrint = new Button(parent, SWT.RADIO);
		btnPrint.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
		btnPrint.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE,Preferences.FONT_SIZE,SWT.NORMAL));
		btnPrint.setText("");
		btnPrint.setSelection(true);
		
		return parent;
	}
	
	protected void setShellStyle(int newShellStyle) {
		super.setShellStyle(SWT.NONE|SWT.APPLICATION_MODAL);
	}
	
	
	
	@Override
	protected void buttonPressed(int buttonId) {
		if(buttonId == 0){
			if(btnExport.getSelection())
				result = "xls";
			else if(btnPrint.getSelection())
				result = "pdf";
			else
				result = "";
		}else{
			result = "";
		}
		super.buttonPressed(buttonId);
	}
}
