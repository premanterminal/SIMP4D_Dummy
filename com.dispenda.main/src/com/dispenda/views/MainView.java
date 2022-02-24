package com.dispenda.views;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.SWTResourceManager;

import com.dispenda.main.MainActivator;
import com.dispenda.object.Preferences;

public class MainView extends ViewPart {
	private Image image;
	private Composite composite;
	private static final Point DEFAULT_SIZE = new Point( 1920, 1080 );
	private Color fontColor = new Color(Display.getCurrent(),Preferences.FONT_COLOR);
	
	public MainView() {
	}

	public static final String ID = MainView.class.getName();

	public void createPartControl(Composite parent) {
		composite = new Composite(parent, SWT.NONE);
		GridLayout gl_composite = new GridLayout(3, false);
		gl_composite.verticalSpacing = 20;
		composite.setLayout(gl_composite);
		
		Canvas compLogo = new Canvas(composite, SWT.TRANSPARENT);
		GridData gd_composite_3 = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
		gd_composite_3.heightHint = 60;
		gd_composite_3.widthHint = 48;
		compLogo.setLayoutData(gd_composite_3);
		image = new Image(Display.getCurrent(), MainActivator.getImageDescriptor("img/logo.png").getImageData().scaledTo(48, 60));
		compLogo.addPaintListener(new PaintListener(){
			public void paintControl(PaintEvent e){
				e.gc.drawImage(image, 0, 0);
			}
		});
//		compLogo.setBackgroundImage(new Image(Display.getCurrent(), MainActivator.getImageDescriptor("img/logo.png").getImageData()));
		Label lblSistemInformasiPajak = new Label(composite, SWT.NONE);
		lblSistemInformasiPajak.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		lblSistemInformasiPajak.setForeground(fontColor);
		lblSistemInformasiPajak.setFont(SWTResourceManager.getFont("", 16, SWT.NORMAL));
		lblSistemInformasiPajak.setText("PEMERINTAH KOTA MEDAN\r\nBADAN PENGELOLA PAJAK DAN RETRIBUSI DAERAH");
		
		ToolBar toolBar = 
				new ToolBar(composite, SWT.FLAT | SWT.RIGHT);
		toolBar.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, true, false, 1, 1));
		
		ToolItem tltmMin = new ToolItem(toolBar, SWT.NONE);
		tltmMin.setImage(scaledTo(MainActivator.getImageDescriptor("img/minimize_button.png").getImageData()));
		tltmMin.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				Display.getCurrent().getActiveShell().setMinimized(true);
			}
		});
		
		ToolItem tltmClose = new ToolItem(toolBar, SWT.NONE);
		tltmClose.setImage(scaledTo(MainActivator.getImageDescriptor("img/close_button.png").getImageData()));
		tltmClose.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				boolean result = MessageDialog.openConfirm(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Apakah anda yakin ingin menutup aplikasi ini?");
				if(result){
					Display.getCurrent().getActiveShell().close();
					System.exit(0);
				}
			}
		});
		
	}

	public void setFocus() {
		
	}
	
	private Image scaledTo(ImageData imgData){
		int width = 0;
		if(Preferences.ORIENTATION == SWT.HORIZONTAL)
			width = (imgData.width)*Display.getCurrent().getClientArea().width/DEFAULT_SIZE.x;
		else
			width = (imgData.width-10)*Display.getCurrent().getClientArea().width/DEFAULT_SIZE.x;
		ImageData scaledImage = imgData.scaledTo(width, width);
		return new Image(Display.getCurrent(), scaledImage);
	}
}