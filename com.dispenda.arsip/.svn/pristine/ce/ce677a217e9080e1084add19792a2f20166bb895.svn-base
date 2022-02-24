package com.dispenda.arsip.dialog;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.Locale;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.wb.swt.SWTResourceManager;

import com.dispenda.object.GlobalVariable;
import com.dispenda.object.Preferences;
import org.eclipse.swt.widgets.Table;

public class DetailKeluarMasukDialog extends Dialog{
	
	private ResultSet result;
	private Table table;
	Locale ind = new Locale("id", "ID");
	NumberFormat indFormat = NumberFormat.getCurrencyInstance(ind);
	
	public ResultSet getResult() {
		return result;
	}
	
	public void setResult(ResultSet result) {
		this.result = result;
	}

	public DetailKeluarMasukDialog(Shell parent) {
		super(parent);
		
	}
	@Override
	protected Control createDialogArea(Composite main) {
		Composite parent = (Composite) super.createDialogArea(main);
		parent.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		GridLayout gl_parent = new GridLayout(1, false);
		gl_parent.horizontalSpacing = 30;
		gl_parent.verticalSpacing = 20;
		parent.setLayout(gl_parent);
		parent.setBackground(SWTResourceManager.getColor(Preferences.BACKGROUND_COLOR));
		parent.setBackgroundMode(SWT.INHERIT_FORCE);
		
		table = new Table(parent, SWT.BORDER | SWT.FULL_SELECTION);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BORDER));
		table.setForeground(SWTResourceManager.getColor(Preferences.FONT_COLOR));
		table.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				if(table.getSelectionIndex()>=0){
					for(int i=0;i<6;i++){
						GlobalVariable.INSTANCE.item[i] = table.getItem(table.getSelectionIndex()).getText(i);
					}
					close();
				}else{
					MessageDialog.openError(getShell(), "Error", "Tidak ada data yang dipilih.");
				}
			}
		});
		table.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.keyCode == SWT.CR || e.keyCode == SWT.KEYPAD_CR){
					if(table.getSelectionIndex()>=0){
						for(int i=0;i<6;i++)
							GlobalVariable.INSTANCE.item[i] = table.getItem(table.getSelectionIndex()).getText(i);
						close();
					}else{
						MessageDialog.openError(getShell(), "Error", "Tidak ada data yang dipilih.");
					}
				}
			}
		});
		String[] colHeader = new String[]{"No. Dokumen","Tgl. Dokumen","MasaPajakDari","MasaPajakSampai","NPWPD","Nama Badan"};
		int[] widthHeader = new int[]{150,150,150,150,200,200,250};
		for(int i=0;i<colHeader.length;i++){
			TableColumn col = new TableColumn(table, SWT.NONE);
			col.setText(colHeader[i]);
			col.setWidth(widthHeader[i]);
		}
		try {
			if(result!=null){
				while(result.next()){
					TableItem item = new TableItem(table, SWT.NONE);
					item.setText(0, result.getString("NO_SPTPD"));
					item.setText(1, result.getString("TGL_SPTPD"));
					item.setText(2, result.getString("MASA_PAJAK_DARI"));
					item.setText(3, result.getString("MASA_PAJAK_SAMPAI"));
					item.setText(4, result.getString("NPWPD"));
					item.setText(5, result.getString("NAMA_BADAN"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
				
		return parent;
	}
	
	protected void setShellStyle(int newShellStyle) {
		super.setShellStyle(SWT.NONE|SWT.APPLICATION_MODAL);
	}
		
	@Override
	protected Control createButtonBar(Composite parent) {
		GridLayout gl_parent = new GridLayout(2, false);
		parent.setLayout(gl_parent);
		Button choose = new Button(parent, SWT.PUSH);
		GridData gd_btnchoose = new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1);
		gd_btnchoose.widthHint = 90;
		choose.setLayoutData(gd_btnchoose);
		choose.setText("Pilih");
		choose.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(table.getSelectionIndex()>=0){
					for(int i=0;i<6;i++){
						GlobalVariable.INSTANCE.item[i] = table.getItem(table.getSelectionIndex()).getText(i);
					}
					close();
				}else{
					MessageDialog.openError(getShell(), "Error", "Tidak ada data yang dipilih.");
				}
			}
		});
		
		Button cancel = new Button(parent, SWT.PUSH);
		GridData gd_btnLihatSkpdkb = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnLihatSkpdkb.widthHint = 90;
		cancel.setLayoutData(gd_btnLihatSkpdkb);
		cancel.setText("Batal");
		cancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				GlobalVariable.INSTANCE.item = null;
				close();
			}
		});
		return parent;
	}
	
	/*private void selectedItem(){
		if(table.getSelectionIndex()>=0){
			for(int i=0;i<6;i++)
				GlobalVariable.INSTANCE.item[i] = table.getItem(table.getSelectionIndex()).getText(i);
			close();
		}else{
			MessageDialog.openError(getShell(), "Error", "Tidak ada data yang dipilih.");
		}
	}*/
	/*
	
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
	}*/
}
