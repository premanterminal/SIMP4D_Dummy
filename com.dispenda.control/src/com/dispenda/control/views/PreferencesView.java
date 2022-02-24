package com.dispenda.control.views;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.FontDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.SWTResourceManager;

import com.dispenda.object.OpenPreferenceFile;
import com.dispenda.object.Preferences;

public class PreferencesView extends ViewPart {
	protected GC gc;
	private Label lblFontColor;
	public PreferencesView() {
	}

	public static final String ID = PreferencesView.class.getName();
	private Text txtImage;
	private Text txtFont;
	private Spinner spnFontSize;
	private Label lblBackgroundColor;
	private OpenPreferenceFile opf = new OpenPreferenceFile();
	private Color fontColor = new Color(Display.getCurrent(), Preferences.FONT_COLOR);
	@Override
	public void createPartControl(Composite parent) {
//		opf.readFile();
		parent.setLayout(new GridLayout(1, false));
		Group grpFont = new Group(parent, SWT.NONE);
		grpFont.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		grpFont.setText("Font");
		grpFont.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		grpFont.setForeground(fontColor);
		grpFont.setLayout(new GridLayout(3, false));
		
		Label lblFont = new Label(grpFont, SWT.NONE);
		lblFont.setText("Font");
		lblFont.setForeground(fontColor);
		lblFont.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		txtFont = new Text(grpFont, SWT.BORDER);
		GridData gd_txtFont = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_txtFont.widthHint = 202;
		txtFont.setLayoutData(gd_txtFont);
		txtFont.setEditable(false);
		txtFont.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtFont.setText(Preferences.FONT_STYLE);
		
		Button btnFont = new Button(grpFont, SWT.NONE);
		GridData gd_btnFont = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnFont.widthHint = 90;
		btnFont.setLayoutData(gd_btnFont);
		btnFont.setText("Browse..");
		btnFont.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnFont.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FontDialog fd = new FontDialog(Display.getCurrent().getActiveShell());
				FontData fontData = fd.open();
				txtFont.setText(fontData.getName());
				spnFontSize.setSelection(fontData.getHeight());
			}
		});
		
		Label lblSize = new Label(grpFont, SWT.NONE);
		lblSize.setText("Size");
		lblSize.setForeground(fontColor);
		lblSize.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		spnFontSize = new Spinner(grpFont, SWT.BORDER);
		spnFontSize.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		spnFontSize.setSelection(Preferences.FONT_SIZE);
		new Label(grpFont, SWT.NONE);
		
		Label lblColor = new Label(grpFont, SWT.NONE);
		lblColor.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		lblColor.setText("Color");
		lblColor.setForeground(fontColor);
		lblColor.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		lblFontColor = new Label(grpFont, SWT.BORDER);
		lblFontColor.setText("     ");
		lblFontColor.setBackground(new Color(Display.getCurrent(),Preferences.FONT_COLOR));
		lblFontColor.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblFontColor.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				ColorDialog cd = new ColorDialog(Display.getCurrent().getActiveShell(), SWT.CENTER);
		    	RGB rgb = cd.open();
		    	Color color = new Color(Display.getCurrent(), rgb);
		    	lblFontColor.setBackground(color);
			}
		});
		new Label(grpFont, SWT.NONE);
		
		Group grpBackground = new Group(parent, SWT.NONE);
		grpBackground.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		grpBackground.setText("Background");
		grpBackground.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		grpBackground.setForeground(fontColor);
		grpBackground.setLayout(new GridLayout(3, false));
		
		Label lblImage = new Label(grpBackground, SWT.NONE);
		lblImage.setText("Image");
		lblImage.setForeground(fontColor);
		lblImage.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		txtImage = new Text(grpBackground, SWT.BORDER);
		txtImage.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		GridData gd_txtImage = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_txtImage.widthHint = 357;
		txtImage.setLayoutData(gd_txtImage);
		txtImage.setText(Preferences.BACKGROUND_IMAGE);
		
		Button btnImage = new Button(grpBackground, SWT.NONE);
		btnImage.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnImage.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog fd = new FileDialog(Display.getCurrent().getActiveShell(), SWT.NONE);
				txtImage.setText(fd.open());
			}
		});
		GridData gd_btnImage = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnImage.widthHint = 90;
		btnImage.setLayoutData(gd_btnImage);
		btnImage.setText("Browse..");
		
		Label lblColor_1 = new Label(grpBackground, SWT.NONE);
		lblColor_1.setText("Color");
		lblColor_1.setForeground(fontColor);
		lblColor_1.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		lblBackgroundColor = new Label(grpBackground, SWT.BORDER);
		lblBackgroundColor.setText("     ");
		lblBackgroundColor.setBackground(new Color(Display.getCurrent(), Preferences.BACKGROUND_COLOR));
		lblBackgroundColor.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblBackgroundColor.addMouseListener(new org.eclipse.swt.events.MouseAdapter() {
			@Override
			public void mouseDown(org.eclipse.swt.events.MouseEvent e) {
				ColorDialog cd = new ColorDialog(Display.getCurrent().getActiveShell(), SWT.CENTER);
		    	RGB rgb = cd.open();
		    	Color color = new Color(Display.getCurrent(), rgb);
		    	lblBackgroundColor.setBackground(color);
			}
		});
		new Label(grpBackground, SWT.NONE);
		
		Label lblMenuBar = new Label(grpBackground, SWT.NONE);
		lblMenuBar.setText("Menu Bar");
		lblMenuBar.setForeground(fontColor);
		lblMenuBar.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		Composite composite_1 = new Composite(grpBackground, SWT.NONE);
		composite_1.setLayout(new GridLayout(2, false));
		
		final Button btnSamping = new Button(composite_1, SWT.RADIO);
		btnSamping.setText("Samping");
		btnSamping.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		
		final Button btnAtas = new Button(composite_1, SWT.RADIO);
		btnAtas.setText("Atas");
		btnAtas.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnAtas.setSelection(true);
		new Label(grpBackground, SWT.NONE);
		
		if(Preferences.ORIENTATION == SWT.HORIZONTAL){
			btnSamping.setSelection(false);
			btnAtas.setSelection(true);
		}else{
			btnSamping.setSelection(true);
			btnAtas.setSelection(false);
		}
		
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));
		
		Button btnReset = new Button(composite, SWT.NONE);
		btnReset.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnReset.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				/*Preferences.FONT_STYLE = txtFont.getText();
				Preferences.FONT_SIZE = spnFontSize.getSelection();
				Preferences.FONT_COLOR = lblFontColor.getBackground().getRGB();
				Preferences.BACKGROUND_COLOR = lblBackgroundColor.getBackground().getRGB();
				Preferences.BACKGROUND_IMAGE = txtImage.getText();*/
				txtFont.setText("Calibri");
				spnFontSize.setSelection(12);
				lblFontColor.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
				lblBackgroundColor.setBackground(new Color(Display.getCurrent(),new RGB(32, 172, 192)));
				txtImage.setText(" ");
				btnAtas.setSelection(true);
				btnSamping.setSelection(false);
			}
		});
		GridData gd_btnReset = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnReset.widthHint = 90;
		btnReset.setLayoutData(gd_btnReset);
		btnReset.setText("Reset..");
		
		Button btnSave = new Button(composite, SWT.NONE);
		btnSave.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnSave.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Preferences.FONT_STYLE = txtFont.getText();
				Preferences.FONT_SIZE = spnFontSize.getSelection();
				Preferences.FONT_COLOR = lblFontColor.getBackground().getRGB();
				Preferences.BACKGROUND_COLOR = lblBackgroundColor.getBackground().getRGB();
				if(txtImage.getText().equalsIgnoreCase(""))
					Preferences.BACKGROUND_IMAGE = " ";
				else
					Preferences.BACKGROUND_IMAGE = txtImage.getText();
				if(btnAtas.getSelection())
					Preferences.ORIENTATION = SWT.HORIZONTAL;
				else
					Preferences.ORIENTATION = SWT.VERTICAL;
				if(opf.writeFile()){
					MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Data berhasil disimpan.");
					boolean result = MessageDialog.openQuestion(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Anda mau merestrart aplikasi untuk melihat perubahan settingan ini?");
					if(result)
						PlatformUI.getWorkbench().restart();
				}else
					MessageDialog.openError(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Data GAGAL disimpan.");
			}
		});
		GridData gd_btnSave = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnSave.widthHint = 90;
		btnSave.setLayoutData(gd_btnSave);
		btnSave.setText("Save..");
	}

	@Override
	public void setFocus() {

	}
}
