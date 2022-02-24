package com.dispenda.sspd.dialog;

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

public class RegPbbDialog extends Dialog {
	private Text txtNoReg;
	private Text txtKet;
	private String key;
	private Timestamp dateNow;
	private Table tblReg;

	public RegPbbDialog(Shell parentShell, String key) {
		super(parentShell);
		this.key = key;
	}

	@Override
	protected void setShellStyle(int newShellStyle) {
		super.setShellStyle(SWT.NONE|SWT.APPLICATION_MODAL);
	}
	
	@Override
	protected Control createDialogArea(Composite main) {
		dateNow = (java.sql.Timestamp)ControllerFactory.getMainController().getCpWajibPajakDAOImpl().getDateNow();
		Composite parent = (Composite) super.createDialogArea(main);
		GridLayout gridLayout = (GridLayout) parent.getLayout();
		gridLayout.numColumns = 3;
		
		Label lblNoRegistrasi = new Label(parent, SWT.NONE);
		lblNoRegistrasi.setFont(SWTResourceManager.getFont("Calibri", 12, SWT.NORMAL));
		lblNoRegistrasi.setText("No. Registrasi");
		
		txtNoReg = new Text(parent, SWT.BORDER);
		txtNoReg.setFont(SWTResourceManager.getFont("Calibri", 12, SWT.NORMAL));
		txtNoReg.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		tblReg = new Table(parent, SWT.BORDER | SWT.FULL_SELECTION);
		GridData gd_tblReg = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 3);
		gd_tblReg.heightHint = 104;
		tblReg.setLayoutData(gd_tblReg);
		tblReg.setHeaderVisible(true);
		tblReg.setLinesVisible(true);
		tblReg.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		tblReg.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		tblReg.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE-1, SWT.NORMAL));
		loadData(ControllerFactory.getMainController().getCpSspdDAOImpl().getNoRegPBB());
		
		Label lblKeterangan = new Label(parent, SWT.NONE);
		lblKeterangan.setFont(SWTResourceManager.getFont("Calibri", 12, SWT.NORMAL));
		lblKeterangan.setText("Keterangan");
		
		txtKet = new Text(parent, SWT.BORDER);
		txtKet.setFont(SWTResourceManager.getFont("Calibri", 12, SWT.NORMAL));
		GridData gd_txtKet = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_txtKet.widthHint = 148;
		txtKet.setLayoutData(gd_txtKet);
		
		Composite composite = new Composite(parent, SWT.NONE);
		GridData gd_composite = new GridData(SWT.CENTER, SWT.CENTER, false, true, 2, 1);
		gd_composite.heightHint = 104;
		composite.setLayoutData(gd_composite);
		composite.setLayout(new GridLayout(2, false));
		
		Button btnSave = new Button(composite, SWT.NONE);
		btnSave.addSelectionListener(new SelectionAdapter() {
			@SuppressWarnings("deprecation")
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (txtNoReg.getText().equalsIgnoreCase(""))
				{
					MessageDialog.openError(Display.getCurrent().getActiveShell(), "Error", "Isi nomor registrasi");
					txtNoReg.setFocus();
				}
				else{
					try {
				        int no_reg = Integer.parseInt(txtNoReg.getText());;//ControllerFactory.getMainController().getCpSspdDAOImpl().getMaxNoReg();
						if (ControllerFactory.getMainController().getCpSspdDAOImpl().saveNoReg(no_reg, "", key + " " + txtKet.getText(), dateNow.getYear() + 1900)){
							MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "Save", "No Registrasi " + key + " berhasil disimpan.");
							loadData(ControllerFactory.getMainController().getCpSspdDAOImpl().getNoRegPBB());
						}
						else
							MessageDialog.openError(Display.getCurrent().getActiveShell(), "Error", "No reg gagal disimpan");
					    } catch (NumberFormatException nfe) {
					    	MessageDialog.openError(Display.getCurrent().getActiveShell(), "Error", "No reg hanya angka");
					    }
				}
				
			}
		});
		GridData gd_btnSave = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnSave.widthHint = 90;
		btnSave.setLayoutData(gd_btnSave);
		btnSave.setFont(SWTResourceManager.getFont("Calibri", 12, SWT.NORMAL));
		btnSave.setText("SAVE");
		
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
		btnCancel.setText("CANCEL");
		main.setBackgroundMode(SWT.INHERIT_FORCE);
		return parent;
	}
	
	private void createColumns(){
		if (tblReg.getColumnCount() <= 0){
			TableColumn colPajak = new TableColumn(tblReg, SWT.NONE,0);
			colPajak.setText("Keterangan");
			colPajak.setWidth(180);
			
			TableColumn colPajak1 = new TableColumn(tblReg, SWT.NONE,0);
			colPajak1.setText("No Reg");
			colPajak1.setWidth(90);
		}
	}
	
	private void loadData(ResultSet rs){
		String keterangan = "";
		createColumns();
		try {
			while (rs.next()){
				TableItem item = new TableItem(tblReg, SWT.NONE);
				item.setText(0, rs.getString("No_Registrasi"));
				keterangan = rs.getString("Keterangan");
				item.setText(1, keterangan.substring(keterangan.indexOf(" ")));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	protected Control createButtonBar(Composite parent) {
		return null;
	}
}
