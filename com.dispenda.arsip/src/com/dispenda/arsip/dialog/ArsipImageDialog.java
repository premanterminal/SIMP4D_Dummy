package com.dispenda.arsip.dialog;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.URL;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Button;

import com.dispenda.object.Preferences;
import com.dispenda.object.UploadFile;
import com.dispenda.report.ReportAction;
import com.dispenda.report.ReportAppContext;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;


public class ArsipImageDialog extends Dialog{
	private String noBerkas = "";
	private Image image;
	private Color fontColor = new Color(Display.getCurrent(),Preferences.FONT_COLOR);
	private String urlImage = "";
	private Label lblNoBerkas;
	
	/**
	 * @wbp.parser.constructor
	 */
	protected ArsipImageDialog(Shell parentShell) {
		super(parentShell);
		setShellStyle(SWT.BORDER);
		// TODO Auto-generated constructor stub
	}

	public ArsipImageDialog(Shell parentShell, String noBerkas){
		super(parentShell);
		this.noBerkas = noBerkas;
		String path = "";
		URL url = null;
		String[] splitNo = noBerkas.split("/");
		String folder = splitNo[splitNo.length-2];
		String tahun = splitNo[splitNo.length-1].split("\\(")[0];
		path = "http://172.16.78.3/ScannedArsip/" + folder + "/" + tahun + "/" + noBerkas.replace("/", "-") + ".jpg";
		urlImage = path;
		try {
			url = new URL(path);
			ImageData imgData = new ImageData(url.openStream());
//			double ratio = (double)imgData.height/(double)imgData.width;
//			double heighT = ratio*600;
			image = new Image(Display.getCurrent(),imgData.scaledTo(650, 800));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	protected Control createDialogArea(Composite main) {
		Composite parent = (Composite) super.createDialogArea(main);
		GridLayout gridLayout = (GridLayout) parent.getLayout();
		gridLayout.numColumns = 3;
		parent.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BORDER));
		
		lblNoBerkas = new Label(parent, SWT.NONE);
		lblNoBerkas.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		lblNoBerkas.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblNoBerkas.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 3, 1));
		lblNoBerkas.setText(noBerkas);
		
		Canvas canvas = new Canvas(parent, SWT.NONE);
		canvas.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent pe) {
				/*int tmpX = image.getBounds().width;
			    tmpX = (600 / 2 - tmpX / 2);
			    int tmpY = image.getBounds().height;
			    tmpY = (680 / 2 - tmpY / 2);
			    if(tmpX <= 0) tmpX = pe.x;
			    else tmpX += pe.x;
			    if(tmpY <= 0) tmpY = pe.y;
			    else tmpY += pe.y;*/
				pe.gc.drawImage(image, 0, 0);
			}
		});
		GridData gd_canvas = new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1);
		gd_canvas.heightHint = 800;
		gd_canvas.widthHint = 650;
		canvas.setLayoutData(gd_canvas);
		
		Button btnSimpan = new Button(parent, SWT.NONE);
		btnSimpan.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog dialog = new FileDialog(Display.getCurrent().getActiveShell(), SWT.SAVE);
				dialog.setFilterNames(new String[] {"jpg Files"});
				dialog.setFilterExtensions(new String[] {"*.jpg"});
				String path = dialog.open();
				if(path!=null){
					/*try {
						File targetFile = new File(path);
						UploadFile.openFTP();
						if (UploadFile.download(targetFile, urlImage))
							MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "Simpan Berkas", "Berkas Arsip berhasil disimpan di " + path);
						else
							MessageDialog.openError(Display.getCurrent().getActiveShell(), "Simpan Berkas", "Berkas Arsip gagal disimpan");
						UploadFile.closeFTP();
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}*/
					ImageLoader saver = new ImageLoader();
					saver.data = new ImageData[] {image.getImageData()};
					saver.save(path, SWT.IMAGE_JPEG);
					MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "Simpan Berkas", "Berkas Arsip berhasil disimpan di " + path);
				}
			}
		});
		GridData gd_btnSimpan = new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1);
		gd_btnSimpan.widthHint = 100;
		btnSimpan.setLayoutData(gd_btnSimpan);
		btnSimpan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnSimpan.setText("Simpan");
		
		Button btnCetak = new Button(parent, SWT.NONE);
		btnCetak.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ReportAppContext.name = "dsArsip";
				if(!ReportAppContext.map.isEmpty())
					ReportAppContext.map.clear();
					ReportAppContext.map.put("image", urlImage);
				ReportAction.start("BerkasArsip.rptdesign");
				close();
			}
		});
		GridData gd_btnCetak = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
		gd_btnCetak.widthHint = 100;
		btnCetak.setLayoutData(gd_btnCetak);
		btnCetak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnCetak.setText("Cetak");
		
		Button btnTutup = new Button(parent, SWT.NONE);
		btnTutup.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				image.dispose();
				close();
			}
		});
		btnTutup.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		GridData gd_btnTutup = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_btnTutup.heightHint = 27;
		gd_btnTutup.widthHint = 100;
		btnTutup.setLayoutData(gd_btnTutup);
		btnTutup.setText("Tutup");
		parent.setTabList(new Control[]{btnTutup, btnSimpan, btnCetak});
		main.setBackgroundMode(SWT.INHERIT_FORCE);
		return parent;
	}
	
	@Override
	protected Control createButtonBar(Composite parent) {
		return null;
	}
	protected Point getInitialSize() {
		return new Point(678, 942);
	}
}
