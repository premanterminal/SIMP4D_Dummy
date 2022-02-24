package com.dispenda.control.dialogs;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;

import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import com.dispenda.controller.ControllerFactory;
import com.dispenda.model.Pajak;
import com.dispenda.model.PajakProvider;

public class PajakDialog extends Dialog {
	
	private Pajak pajak;
	private Text txtNamaPajak;
	private Integer index;
	private Text txtKodePajak;
	private Text txtKodeDenda;

	/**
	 * @wbp.parser.constructor
	 */
	public PajakDialog(Shell parentShell) {
		super(parentShell);
	}

	public PajakDialog(Shell parentShell,Pajak pajak,Integer index) {
		super(parentShell);
		this.pajak = pajak;
		this.index = index;
	}
	
	@Override
	protected void setShellStyle(int newShellStyle) {
		super.setShellStyle(SWT.NONE|SWT.APPLICATION_MODAL);
	}
	
	@Override
	protected Control createButtonBar(Composite parent) {
		return null;
	}
	
	@Override
	protected Control createDialogArea(Composite main) {
		Composite parent = (Composite) super.createDialogArea(main);
		main.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		main.setBackgroundMode(SWT.INHERIT_FORCE);
		
		Composite compo = new Composite(parent, SWT.NONE);
		GridLayout gl_compo = new GridLayout(2, false);
		gl_compo.horizontalSpacing = 30;
		gl_compo.verticalSpacing = 20;
		compo.setLayout(gl_compo);
		compo.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		
		Label lblKodePajak = new Label(compo, SWT.NONE);
		lblKodePajak.setFont(SWTResourceManager.getFont("Calibri", 12, SWT.NORMAL));
		lblKodePajak.setText("Kode Pajak");
		
		txtKodePajak = new Text(compo, SWT.BORDER);
		txtKodePajak.setFont(SWTResourceManager.getFont("Calibri", 12, SWT.NORMAL));
		txtKodePajak.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblNamaPajak = new Label(compo, SWT.NONE);
		lblNamaPajak.setFont(SWTResourceManager.getFont("Calibri", 12, SWT.NORMAL));
		lblNamaPajak.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNamaPajak.setText("Nama Pajak");
		
		txtNamaPajak = new Text(compo, SWT.BORDER);
		txtNamaPajak.setFont(SWTResourceManager.getFont("Calibri", 12, SWT.NORMAL));
		GridData gd_txtNamaKota = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_txtNamaKota.widthHint = 230;
		txtNamaPajak.setLayoutData(gd_txtNamaKota);
		
		Label lblKodeDenda = new Label(compo, SWT.NONE);
		lblKodeDenda.setFont(SWTResourceManager.getFont("Calibri", 12, SWT.NORMAL));
		lblKodeDenda.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblKodeDenda.setText("Kode Denda");
		
		txtKodeDenda = new Text(compo, SWT.BORDER);
		txtKodeDenda.setFont(SWTResourceManager.getFont("Calibri", 12, SWT.NORMAL));
		GridData gd_txtKodeDenda = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_txtKodeDenda.widthHint = 230;
		txtKodeDenda.setLayoutData(gd_txtKodeDenda);
		
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		composite.setLayout(new GridLayout(2, false));
		
		Button btnSave = new Button(composite, SWT.NONE);
		btnSave.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(checkFields()){
					if(pajak!=null){
						PajakProvider.INSTANCE.getSelectedPajak(index).setNamaPajak(txtNamaPajak.getText());
						PajakProvider.INSTANCE.getSelectedPajak(index).setKodePajak(txtKodePajak.getText());
						PajakProvider.INSTANCE.getSelectedPajak(index).setKodeDenda(txtKodeDenda.getText());
						if(ControllerFactory.getMainController().getCpPajakDAOImpl().savePajak(PajakProvider.INSTANCE.getSelectedPajak(index)))
							MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "Save Data", "Penyimpanan Data berhasil dilakukan.");
					}else{
						Pajak pajak = new Pajak(null,txtKodePajak.getText(),txtNamaPajak.getText(),txtKodeDenda.getText());
						if(ControllerFactory.getMainController().getCpPajakDAOImpl().savePajak(pajak)){
							MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "Save Data", "Penyimpanan Data berhasil dilakukan.");
							pajak.setIdPajak(ControllerFactory.getMainController().getCpPajakDAOImpl().getLastIdPajak());
							PajakProvider.INSTANCE.addItem(pajak);
						}
					}
					getShell().close();
				}else
					MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "Save Data", "Ada data yang belum di isi. Silahkan isi data yang kosong dengan benar.");
			}
		});
		GridData gd_btnSave = new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1);
		gd_btnSave.widthHint = 120;
		btnSave.setLayoutData(gd_btnSave);
		btnSave.setFont(SWTResourceManager.getFont("Calibri", 12, SWT.NORMAL));
		btnSave.setText("SAVE");
		
		Button btnCancel = new Button(composite, SWT.NONE);
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				getShell().close();
			}
		});
		btnCancel.setFont(SWTResourceManager.getFont("Calibri", 12, SWT.NORMAL));
		GridData gd_btnNewButton_1 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnNewButton_1.widthHint = 120;
		btnCancel.setLayoutData(gd_btnNewButton_1);
		btnCancel.setText("CANCEL");
		
		if(pajak!=null){
			txtNamaPajak.setText(pajak.getNamaPajak());
			txtKodePajak.setText(pajak.getKodePajak());
			txtKodeDenda.setText(pajak.getKodeDenda());
		}
		
		return parent;
	}
	
	private boolean checkFields(){
		boolean result = false;
		if(txtNamaPajak.getText().equalsIgnoreCase("")||txtKodePajak.getText().equalsIgnoreCase(""))
			result = false;
		else
			result = true;
		return result;
	}
}