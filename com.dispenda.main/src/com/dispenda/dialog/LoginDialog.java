package com.dispenda.dialog;

import java.sql.SQLException;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.wb.swt.SWTResourceManager;

import com.dispenda.main.MainActivator;
import com.dispenda.object.GlobalVariable;
import com.dispenda.object.OpenPreferenceFile;
import com.dispenda.object.Preferences;

public class LoginDialog extends Dialog{
	
	private static final Point DEFAULT_SIZE = new Point( 1024, 768 );
	private Image image;
	private Text txtValueUsername;
	private Text txtValuePassword;
	private Button buttonLogin;
	protected String username;
	protected String password;
	private OpenPreferenceFile opf = new OpenPreferenceFile();
	private Color fontColor = null;

	public LoginDialog(Shell parentShell) {
		super(parentShell);
		opf.readFile();
		fontColor = new Color(Display.getCurrent(),Preferences.FONT_COLOR);
	}
	
	protected void setShellStyle(int newShellStyle) {
		super.setShellStyle(SWT.NONE|SWT.APPLICATION_MODAL);
	}
	
	@Override
	protected void constrainShellSize() {
		getShell().setSize(getDisplaySize());
		getShell().setBounds(0, 0, getDisplaySize().x, getDisplaySize().y);
	}
	
	@Override
	protected Control createDialogArea(Composite main) {
		
		Composite parent = (Composite) super.createDialogArea(main);
		GridLayout gl_parent = new GridLayout(3, false);
		gl_parent.horizontalSpacing = 30;
		gl_parent.verticalSpacing = 20;
		parent.setLayout(gl_parent);
//		main.setBackgroundImage(new Image(Display.getCurrent(), MainActivator.getImageDescriptor("img/background.jpg").getImageData().scaledTo(getDisplaySize().x, getDisplaySize().y)));
//		main.setBackgroundImage(new Image(Display))
		main.setBackground(new Color(Display.getCurrent(),Preferences.BACKGROUND_COLOR));
		main.setBackgroundMode(SWT.INHERIT_FORCE);
		main.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE,Preferences.FONT_SIZE,SWT.NORMAL));
		
		/*Composite compo = new Composite(parent, SWT.NONE);
		GridLayout gl_compo = new GridLayout(3, false);
		gl_compo.horizontalSpacing = 30;
		gl_compo.verticalSpacing = 20;
		compo.setLayout(gl_compo);
		compo.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));*/
		
		Canvas compLogo = new Canvas(parent, SWT.TRANSPARENT);
		GridData gd_composite_3 = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
		gd_composite_3.heightHint = 85;
		gd_composite_3.widthHint = 70;
		compLogo.setLayoutData(gd_composite_3);
		image = new Image(Display.getCurrent(), MainActivator.getImageDescriptor("img/logo.png").getImageData());
		compLogo.addPaintListener(new PaintListener(){
			public void paintControl(PaintEvent e){
				e.gc.drawImage(image, 0, 0);
			}
		});
	    
	    Label lblPayaPinang = new Label(parent, SWT.NONE);
	    lblPayaPinang.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
//	    lblPayaPinang.setFont(SWTResourceManager.getFont("Calibri", 20, SWT.NORMAL));
	    lblPayaPinang.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, 18, SWT.NORMAL));
	    lblPayaPinang.setForeground(fontColor);
	    lblPayaPinang.setText("PEMERINTAH KOTA MEDAN\r\nBADAN PENGELOLA PAJAK DAN RETRIBUSI DAERAH");
	    
	    ToolBar toolBar = new ToolBar(parent, SWT.FLAT | SWT.RIGHT);
		toolBar.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, true, false, 1, 1));
	    
	    ToolItem tltmX = new ToolItem(toolBar, SWT.NONE);
		tltmX.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				cancelPressed();
			}
		});
		tltmX.setImage(new Image(Display.getCurrent(), MainActivator.getImageDescriptor("img/close_button.png").getImageData()));
	    		
		Label lblLogMasuk = new Label(parent, SWT.NONE);
		GridData gd_lblLogMasuk = new GridData(SWT.CENTER, SWT.BOTTOM, false, true, 3, 1);
		gd_lblLogMasuk.horizontalIndent = 30;
		lblLogMasuk.setLayoutData(gd_lblLogMasuk);
		lblLogMasuk.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, 14, SWT.NORMAL));
		lblLogMasuk.setForeground(fontColor);
		lblLogMasuk.setText("LOGIN");
		
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout gl_composite = new GridLayout(3, false);
		gl_composite.horizontalSpacing = 30;
		gl_composite.verticalSpacing = 20;
		composite.setLayout(gl_composite);
		composite.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, true, true, 3, 1));
		new Label(composite, SWT.NONE);
		
		Label label = new Label(composite, SWT.SEPARATOR | SWT.VERTICAL);
		GridData gd_label = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 4);
		gd_label.heightHint = 160;
		gd_label.widthHint = 12;
		label.setLayoutData(gd_label);
		new Label(composite, SWT.NONE);
		
		Label lblNamaPengguna = new Label(composite, SWT.NONE);
		lblNamaPengguna.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE,Preferences.FONT_SIZE, SWT.NORMAL));
		lblNamaPengguna.setForeground(fontColor);
		lblNamaPengguna.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNamaPengguna.setText("USERNAME");
		
		txtValueUsername = new Text(composite, SWT.BORDER);
		txtValueUsername.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE,Preferences.FONT_SIZE, SWT.NORMAL));
		txtValueUsername.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		txtValueUsername.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		GridData gd_txtuser = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_txtuser.widthHint = 200;
		txtValueUsername.setLayoutData(gd_txtuser);
		txtValueUsername.setFocus();
		
		Label lblKataSandi = new Label(composite, SWT.NONE);
		lblKataSandi.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE,Preferences.FONT_SIZE, SWT.NORMAL));
		lblKataSandi.setForeground(fontColor);
		lblKataSandi.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblKataSandi.setText("PASSWORD");
		
		txtValuePassword = new Text(composite, SWT.BORDER);
		txtValuePassword.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE,Preferences.FONT_SIZE, SWT.NORMAL));
		txtValuePassword.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		txtValuePassword.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		GridData gd_txtPass = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_txtPass.widthHint = 200;
		txtValuePassword.setLayoutData(gd_txtPass);
		txtValuePassword.setEchoChar('*');
		new Label(composite, SWT.NONE);
		
		buttonLogin = new Button(composite, SWT.FLAT);
		buttonLogin.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE,Preferences.FONT_SIZE, SWT.NORMAL));
		buttonLogin.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		buttonLogin.setImage(new Image(Display.getCurrent(),MainActivator.getImageDescriptor("img/login.png").getImageData()));

	    txtValueUsername.addListener(SWT.Modify, new Listener() {
	    	public void handleEvent(Event event) {
	    		try {
	    			username = new String(txtValueUsername.getText());
	    			if(!txtValueUsername.getText().equalsIgnoreCase("") && !txtValuePassword.getText().equalsIgnoreCase(""))
	    				buttonLogin.setEnabled(true);
	    		} catch (Exception e) {
	    			buttonLogin.setEnabled(false);
	    		}
	    	}
	    });
	    
	    txtValueUsername.addListener(SWT.DefaultSelection, new Listener() {
	    	public void handleEvent(Event event) {
	    		if(authenticated())
	    			okPressed();
		    }
		});
	    
	    txtValuePassword.addListener(SWT.DefaultSelection, new Listener() {
		    public void handleEvent(Event event) {
		    	if(authenticated())
	    			okPressed();
		    }
		});
	    
	    txtValuePassword.addListener(SWT.Modify, new Listener() {
	    	public void handleEvent(Event event) {
	    		try {
	    			password = new String(txtValuePassword.getText());
	    			if(!txtValueUsername.getText().equalsIgnoreCase("") && !txtValuePassword.getText().equalsIgnoreCase(""))
	    				buttonLogin.setEnabled(true);
	    		} catch (Exception e) {
	    			buttonLogin.setEnabled(false);
	    		}
	        }
	      });
	    
	    buttonLogin.addListener(SWT.Selection, new Listener() {
	    	public void handleEvent(Event event) {  
	    		if(authenticated())
	    			okPressed();
	    	}
	    });
		
		return parent;
	}
	
	private boolean authenticated(){
		boolean result = false;
		if(txtValueUsername.getText().equalsIgnoreCase("")||
				txtValuePassword.getText().equalsIgnoreCase("")){
			username = "";
			password = "";
		}else{
			username = txtValueUsername.getText();
			password = txtValuePassword.getText();
//			result = true;
			result= MainActivator.getDefault().checkLogin(username, password);
			if(result){
				MessageDialog.openInformation(getShell(), "LOG IN", "Log In Berhasil");
				GlobalVariable.userModul.setUsername(username);
				
				try {
					MainActivator.getDefault().getConnection().close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				getShell().dispose();
			}else{
				MessageDialog.openError(getShell(), "LOG IN GAGAL", "'Nama Pengguna' atau 'Kata Sandi' anda salah, coba perbaiki.");
			}
		}
		return result;
	}

	@Override
	protected Control createButtonBar(Composite parent) {
		return null;
	}
	
	
	private Point getDisplaySize() {
	    try {
	        Display display = Display.getCurrent();
	        Monitor monitor = display.getPrimaryMonitor();
	        Rectangle rect = monitor.getBounds();
	        return new Point( rect.width, rect.height );
	    }
	    catch ( Throwable ignore ) {
	        return DEFAULT_SIZE;
	    }
	}
}