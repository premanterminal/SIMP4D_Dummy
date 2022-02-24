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
import com.dispenda.model.Kecamatan;
import com.dispenda.model.KecamatanProvider;

public class KecamatanDialog extends Dialog {
	
	private Kecamatan controlKotaKecamatan;
	private Text txtNamaKotaKecamatan;
	private Integer index;
	private Text txtKodeKecamatan;

	/**
	 * @wbp.parser.constructor
	 */
	public KecamatanDialog(Shell parentShell) {
		super(parentShell);
	}

	public KecamatanDialog(Shell parentShell,Kecamatan controlKotaKecamatan,Integer index) {
		super(parentShell);
		this.controlKotaKecamatan = controlKotaKecamatan;
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
		
		Label lblKodeKecamatan = new Label(compo, SWT.NONE);
		lblKodeKecamatan.setFont(SWTResourceManager.getFont("Calibri", 12, SWT.NORMAL));
		lblKodeKecamatan.setText("Kode Kecamatan");
		
		txtKodeKecamatan = new Text(compo, SWT.BORDER);
		txtKodeKecamatan.setFont(SWTResourceManager.getFont("Calibri", 12, SWT.NORMAL));
		txtKodeKecamatan.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblNamaKotakabupaten = new Label(compo, SWT.NONE);
		lblNamaKotakabupaten.setFont(SWTResourceManager.getFont("Calibri", 12, SWT.NORMAL));
		lblNamaKotakabupaten.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNamaKotakabupaten.setText("Nama Kecamatan");
		
		txtNamaKotaKecamatan = new Text(compo, SWT.BORDER);
		txtNamaKotaKecamatan.setFont(SWTResourceManager.getFont("Calibri", 12, SWT.NORMAL));
		GridData gd_txtNamaKota = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_txtNamaKota.widthHint = 230;
		txtNamaKotaKecamatan.setLayoutData(gd_txtNamaKota);
		
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		composite.setLayout(new GridLayout(2, false));
		
		Button btnSave = new Button(composite, SWT.NONE);
		btnSave.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(checkFields()){
					if(controlKotaKecamatan!=null){
						KecamatanProvider.INSTANCE.getSelectedKecamatan(index).setNamaKecamatan(txtNamaKotaKecamatan.getText());
						KecamatanProvider.INSTANCE.getSelectedKecamatan(index).setKodeKecamatan(txtKodeKecamatan.getText());
						if(ControllerFactory.getMainController().getCpKecamatanDAOImpl().saveKecamatan(KecamatanProvider.INSTANCE.getSelectedKecamatan(index)))
							MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "Save Data", "Penyimpanan Data berhasil dilakukan.");
					}else{
						Kecamatan kotaKecamatanModel = new Kecamatan(null,txtNamaKotaKecamatan.getText(),txtKodeKecamatan.getText());
						if(ControllerFactory.getMainController().getCpKecamatanDAOImpl().saveKecamatan(kotaKecamatanModel)){
							MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "Save Data", "Penyimpanan Data berhasil dilakukan.");
							kotaKecamatanModel.setIdKecamatan(ControllerFactory.getMainController().getCpKecamatanDAOImpl().getLastIdKecamatan());
							KecamatanProvider.INSTANCE.addItem(kotaKecamatanModel);
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
		
		if(controlKotaKecamatan!=null){
			txtNamaKotaKecamatan.setText(controlKotaKecamatan.getNamaKecamatan());
			txtKodeKecamatan.setText(controlKotaKecamatan.getKodeKecamatan());
		}
		
		return parent;
	}
	
	private boolean checkFields(){
		boolean result = false;
		if(txtNamaKotaKecamatan.getText().equalsIgnoreCase("")||txtKodeKecamatan.getText().equalsIgnoreCase(""))
			result = false;
		else
			result = true;
		return result;
	}
}
