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
import com.dispenda.encrypt.MD5;
import com.dispenda.model.User;
import com.dispenda.model.UserProvider;

public class UserDialog extends Dialog {
	
	private User controlUser;
	private Text txtPassword;
	private Integer index;
	private Text txtUsername;
	private Text txtRetypePassword;
	private Text txtDisplayName;

	/**
	 * @wbp.parser.constructor
	 */
	public UserDialog(Shell parentShell) {
		super(parentShell);
	}

	public UserDialog(Shell parentShell,User controlUser,Integer index) {
		super(parentShell);
		this.controlUser = controlUser;
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
		lblKodeKecamatan.setText("Username");
		
		txtUsername = new Text(compo, SWT.BORDER);
		txtUsername.setFont(SWTResourceManager.getFont("Calibri", 12, SWT.NORMAL));
		txtUsername.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblNamaKotakabupaten = new Label(compo, SWT.NONE);
		lblNamaKotakabupaten.setFont(SWTResourceManager.getFont("Calibri", 12, SWT.NORMAL));
		lblNamaKotakabupaten.setText("Password");
		
		txtPassword = new Text(compo, SWT.BORDER);
		txtPassword.setFont(SWTResourceManager.getFont("Calibri", 12, SWT.NORMAL));
		GridData gd_txtNamaKota = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_txtNamaKota.widthHint = 230;
		txtPassword.setLayoutData(gd_txtNamaKota);
		txtPassword.setEchoChar('*');
		
		Label lblRetypePassword = new Label(compo, SWT.NONE);
		lblRetypePassword.setFont(SWTResourceManager.getFont("Calibri", 12, SWT.NORMAL));
		lblRetypePassword.setText("Re-type Password");
		
		txtRetypePassword = new Text(compo, SWT.BORDER);
		txtRetypePassword.setFont(SWTResourceManager.getFont("Calibri", 12, SWT.NORMAL));
		txtRetypePassword.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		txtRetypePassword.setEchoChar('*');
		
		Label lblDisplayName = new Label(compo, SWT.NONE);
		lblDisplayName.setFont(SWTResourceManager.getFont("Calibri", 12, SWT.NORMAL));
		lblDisplayName.setText("Display Name");
		
		txtDisplayName = new Text(compo, SWT.BORDER);
		txtDisplayName.setFont(SWTResourceManager.getFont("Calibri", 12, SWT.NORMAL));
		txtDisplayName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		composite.setLayout(new GridLayout(2, false));
		
		Button btnSave = new Button(composite, SWT.NONE);
		btnSave.addSelectionListener(new SelectionAdapter() {
			@SuppressWarnings("static-access")
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(checkFields()){
					if (txtPassword.getText().length() >= 3){
						if(controlUser!=null){
							MD5 md5 = new MD5();
							String hashPass = md5.encodeString(txtUsername.getText()+txtPassword.getText(), "ISO-8859-1");
							UserProvider.INSTANCE.getSelectedUser(index).setUsername(txtUsername.getText());
							UserProvider.INSTANCE.getSelectedUser(index).setPassword(hashPass);
							UserProvider.INSTANCE.getSelectedUser(index).setDisplayName(txtDisplayName.getText());
							if(ControllerFactory.getMainController().getCpUserDAOImpl().saveUser(UserProvider.INSTANCE.getSelectedUser(index)))
								MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "Save Data", "Penyimpanan Data berhasil dilakukan.");
						}else{
							MD5 md5 = new MD5();
							String hashPass = md5.encodeString(txtUsername.getText()+txtPassword.getText(), "ISO-8859-1");
							User userModel = new User(null,txtUsername.getText(),hashPass,txtDisplayName.getText());
							if(ControllerFactory.getMainController().getCpUserDAOImpl().saveUser(userModel)){
								MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "Save Data", "Penyimpanan Data berhasil dilakukan.");
								userModel.setIdUser(ControllerFactory.getMainController().getCpUserDAOImpl().getLastUsername());
								UserProvider.INSTANCE.addItem(userModel);
							}
						}
						getShell().close();
					}
					else{
						MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "Save Data", "Panjang Password Minimal 3 Karakter.");
						txtPassword.setText("");
						txtRetypePassword.setText("");
					}
				}else{
					MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "Save Data", "Ada data yang belum di isi atau PASSWORD tidak sama. Silahkan isi data yang kosong dengan benar dan isi kembali password anda.");
					txtPassword.setText("");
					txtRetypePassword.setText("");
				}
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
		
		if(controlUser!=null){
			txtUsername.setText(controlUser.getUsername());
			txtPassword.setText(controlUser.getPassword());
			txtDisplayName.setText(controlUser.getDisplayName());
		}
		
		return parent;
	}
	
	private boolean checkFields(){
		boolean result = false;
		if(txtPassword.getText().equalsIgnoreCase("")||(!txtPassword.getText().equalsIgnoreCase(txtRetypePassword.getText()))||txtUsername.getText().equalsIgnoreCase("")
				||txtDisplayName.getText().equalsIgnoreCase(""))
			result = false;
		else
			result = true;
		return result;
	}
}