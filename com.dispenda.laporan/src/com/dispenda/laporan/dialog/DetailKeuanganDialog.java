package com.dispenda.laporan.dialog;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.Locale;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.wb.swt.SWTResourceManager;

import com.dispenda.object.Preferences;

public class DetailKeuanganDialog extends Dialog{
	
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

	public DetailKeuanganDialog(Shell parent) {
		super(parent);
		
	}
	@Override
	protected Control createDialogArea(Composite main) {
		Composite parent = (Composite) super.createDialogArea(main);
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
		
		String[] colHeader = new String[]{"No. SPTPD","Tgl. SPTPD","MasaPajakDari","MasaPajakSampai","NPWPD","Kode Bid. Usaha","Pajak Terhutang","Denda Tambahan","Nama Bid. Usaha"};
		int[] widthHeader = new int[]{150,150,150,150,150,100,200,200,250};
		for(int i=0;i<colHeader.length;i++){
			TableColumn col = new TableColumn(table, SWT.NONE);
			col.setText(colHeader[i]);
			col.setWidth(widthHeader[i]);
		}
		
		try {
			while(result.next()){
				TableItem item = new TableItem(table, SWT.NONE);
				item.setText(0, result.getString("NO_SPTPD"));
				item.setText(1, result.getString("TGL_SPTPD"));
				item.setText(2, result.getString("MASA_PAJAK_DARI"));
				item.setText(3, result.getString("MASA_PAJAK_SAMPAI"));
				item.setText(4, result.getString("NPWPD"));
				item.setText(5, result.getString("KODE_BID_USAHA"));
				item.setText(6, indFormat.format(result.getDouble("PAJAK_TERUTANG")));
				item.setText(7, indFormat.format(result.getDouble("DENDA_TAMBAHAN")));
				item.setText(8, result.getString("NAMA_BID_USAHA"));
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
		Button close = new Button(parent, SWT.PUSH);
		GridData gd_btnLihatSkpdkb = new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1);
		gd_btnLihatSkpdkb.widthHint = 90;
		close.setLayoutData(gd_btnLihatSkpdkb);
		close.setText("Tutup");
		close.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				close();
			}
		});
		return parent;
	}
	
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
