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
import com.dispenda.model.Pejabat;
import com.dispenda.model.PejabatProvider;

public class PejabatDialog extends Dialog {
	
	private Pejabat pejabat;
	private Text txtNamaPejabat;
	private Integer index;
	private Text txtNIP;
	private Text txtPangkat;
	private Text txtJabatan;

	/**
	 * @wbp.parser.constructor
	 */
	public PejabatDialog(Shell parentShell) {
		super(parentShell);
	}

	public PejabatDialog(Shell parentShell,Pejabat pejabat,Integer index) {
		super(parentShell);
		this.pejabat = pejabat;
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
		
		Label lblNIP = new Label(compo, SWT.NONE);
		lblNIP.setFont(SWTResourceManager.getFont("Calibri", 12, SWT.NORMAL));
		lblNIP.setText("NIP");
		
		txtNIP = new Text(compo, SWT.BORDER);
		txtNIP.setFont(SWTResourceManager.getFont("Calibri", 12, SWT.NORMAL));
		txtNIP.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblNamaPejabat = new Label(compo, SWT.NONE);
		lblNamaPejabat.setFont(SWTResourceManager.getFont("Calibri", 12, SWT.NORMAL));
		lblNamaPejabat.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNamaPejabat.setText("Nama Pejabat");
		
		txtNamaPejabat = new Text(compo, SWT.BORDER);
		txtNamaPejabat.setFont(SWTResourceManager.getFont("Calibri", 12, SWT.NORMAL));
		GridData gd_txtNamaKota = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_txtNamaKota.widthHint = 230;
		txtNamaPejabat.setLayoutData(gd_txtNamaKota);
		
		Label lblPangkat = new Label(compo, SWT.NONE);
		lblPangkat.setFont(SWTResourceManager.getFont("Calibri", 12, SWT.NORMAL));
		lblPangkat.setText("Pangkat");
		
		txtPangkat = new Text(compo, SWT.BORDER);
		txtPangkat.setFont(SWTResourceManager.getFont("Calibri", 12, SWT.NORMAL));
		GridData gd_txtPangkat = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_txtPangkat.widthHint = 230;
		txtPangkat.setLayoutData(gd_txtPangkat);
		
		Label lblJabatan = new Label(compo, SWT.NONE);
		lblJabatan.setFont(SWTResourceManager.getFont("Calibri", 12, SWT.NORMAL));
		lblJabatan.setText("Jabatan");
		
		txtJabatan = new Text(compo, SWT.BORDER);
		txtJabatan.setFont(SWTResourceManager.getFont("Calibri", 12, SWT.NORMAL));
		GridData gd_txtJabatan = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_txtJabatan.widthHint = 230;
		txtJabatan.setLayoutData(gd_txtJabatan);
		
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		composite.setLayout(new GridLayout(2, false));
		
		Button btnSave = new Button(composite, SWT.NONE);
		btnSave.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(checkFields()){
					if(pejabat!=null){
						PejabatProvider.INSTANCE.getSelectedPejabat(index).setNamaPejabat(txtNamaPejabat.getText());
						PejabatProvider.INSTANCE.getSelectedPejabat(index).setIdPejabatNIP(txtNIP.getText());
						PejabatProvider.INSTANCE.getSelectedPejabat(index).setPangkat(txtPangkat.getText());
						PejabatProvider.INSTANCE.getSelectedPejabat(index).setJabatan(txtJabatan.getText());
						if(ControllerFactory.getMainController().getCpPejabatDAOImpl().savePejabat(PejabatProvider.INSTANCE.getSelectedPejabat(index)))
							MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "Save Data", "Penyimpanan Data berhasil dilakukan.");
					}else{
						Pejabat pejabat = new Pejabat(txtNIP.getText(),txtNamaPejabat.getText(),txtPangkat.getText(),txtJabatan.getText());
						if(ControllerFactory.getMainController().getCpPejabatDAOImpl().savePejabat(pejabat)){
							MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "Save Data", "Penyimpanan Data berhasil dilakukan.");
//							pejabat.setIdPejabatNIP(ControllerFactory.getMainController().getCpPejabatDAOImpl().getLastIdPejabat());
							PejabatProvider.INSTANCE.addItem(pejabat);
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
		
		if(pejabat!=null){
			txtNamaPejabat.setText(pejabat.getNamaPejabat());
			txtNIP.setText(pejabat.getIdPejabatNIP());
			txtPangkat.setText(pejabat.getPangkat());
			txtJabatan.setText(pejabat.getJabatan());
		}
		
		return parent;
	}
	
	private boolean checkFields(){
		boolean result = false;
		if(txtNamaPejabat.getText().equalsIgnoreCase("")||txtNIP.getText().equalsIgnoreCase("")||txtPangkat.getText().equalsIgnoreCase("")
				||txtJabatan.getText().equalsIgnoreCase(""))
			result = false;
		else
			result = true;
		return result;
	}
}