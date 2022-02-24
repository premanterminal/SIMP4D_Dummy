package com.dispenda.pendaftaran.views;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Drawable;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.GCData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;
import org.osgi.framework.Bundle;

import com.dispenda.controller.ControllerFactory;
import com.dispenda.model.PendaftaranAntrian;

import com.dispenda.object.Preferences;
public class BrowserPendaftaran extends Dialog{
	private Text txtNamaBadan;
	private Text txtNamaPemilik;
	private Text txtJenisPajak;
	private Text txtBidangUsaha;
	private Text txtAlamat;
	private Text txtKecamatan;
	private Text txtKelurahan;
	private Text txtBuktiDiri;
	private Text txtNomor;
	private Color fontColor = new Color(Display.getCurrent(),Preferences.FONT_COLOR);
	private PendaftaranAntrian wp;

	/**
	 * @wbp.parser.constructor
	 */
	protected BrowserPendaftaran(Shell parentShell) {
		super(parentShell);
		// TODO Auto-generated constructor stub
	}
	
	public BrowserPendaftaran(Shell parentShell, PendaftaranAntrian wp){
		super(parentShell);
		this.wp = wp;
	}

	protected Control createDialogArea(Composite main) {
		String fileKTP = wp.getFile_identitas();
		Object[] fileSIUP = new String[0];
		if (!fileKTP.equalsIgnoreCase("")){
			try {
				fileSIUP = (Object[])wp.getFile_izin().getArray();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		Composite parent = (Composite) super.createDialogArea(main);
		parent.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BORDER));
		parent.setLayout(new GridLayout(5, false));
		
		Label lblNamaBadan = new Label(parent, SWT.NONE);
		lblNamaBadan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		//		lblNamaBadan.setForeground(fontColor);
				lblNamaBadan.setText("Nama Badan");
		
		txtNamaBadan = new Text(parent, SWT.BORDER);
		txtNamaBadan.setEditable(false);
		txtNamaBadan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtNamaBadan.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		GridData gd_txtNamaBadan = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_txtNamaBadan.widthHint = 305;
		txtNamaBadan.setLayoutData(gd_txtNamaBadan);
		
		txtNamaBadan.setText(wp.getNamaBadan());
		
		Label label = new Label(parent, SWT.SEPARATOR | SWT.VERTICAL);
		label.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 12));
		
		Label lblAlamat = new Label(parent, SWT.NONE);
		lblAlamat.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
//		lblAlamat.setForeground(fontColor);
		lblAlamat.setText("Alamat");
		
		txtAlamat = new Text(parent, SWT.BORDER);
		txtAlamat.setEditable(false);
		txtAlamat.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtAlamat.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		GridData gd_txtAlamat = new GridData(SWT.LEFT, SWT.TOP, true, false, 1, 1);
		gd_txtAlamat.widthHint = 319;
		txtAlamat.setLayoutData(gd_txtAlamat);
		
		Label lblNamaPemilik = new Label(parent, SWT.NONE);
		lblNamaPemilik.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		//		lblNamaBadan.setForeground(fontColor);
				lblNamaPemilik.setText("Nama Pemilik");
		
		txtNamaPemilik = new Text(parent, SWT.BORDER);
		txtNamaPemilik.setEditable(false);
		txtNamaPemilik.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtNamaPemilik.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		GridData gd_txtNamaPemilik = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_txtNamaPemilik.widthHint = 305;
		txtNamaPemilik.setLayoutData(gd_txtNamaPemilik);
		txtNamaPemilik.setText(wp.getNamaPemilik());
		
		Label lblKecamatan = new Label(parent, SWT.NONE);
		lblKecamatan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
//		lblKecamatan.setForeground(fontColor);
		lblKecamatan.setText("Kecamatan");
		
		txtKecamatan = new Text(parent, SWT.BORDER);
		txtKecamatan.setEditable(false);
		txtKecamatan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtKecamatan.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		GridData gd_txtKecamatan = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_txtKecamatan.widthHint = 319;
		txtKecamatan.setLayoutData(gd_txtKecamatan);
		
		Label lblJenis = new Label(parent, SWT.NONE);
		lblJenis.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		//		lblJenis.setForeground(fontColor);
				lblJenis.setText("Jenis Pajak");
		
		txtJenisPajak = new Text(parent, SWT.BORDER);
		txtJenisPajak.setEditable(false);
		txtJenisPajak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtJenisPajak.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		GridData gd_txtJenisPajak = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
		gd_txtJenisPajak.widthHint = 305;
		txtJenisPajak.setLayoutData(gd_txtJenisPajak);
		txtJenisPajak.setText(ControllerFactory.getMainController().getCpPajakDAOImpl().getPajak(wp.getKewajibanPajak()).get(0).getNamaPajak());
		
		Label lblKelurahan = new Label(parent, SWT.NONE);
		lblKelurahan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
//		lblKelurahan.setForeground(fontColor);
		lblKelurahan.setText("Kelurahan");
		
		txtKelurahan = new Text(parent, SWT.BORDER);
		txtKelurahan.setEditable(false);
		txtKelurahan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtKelurahan.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		GridData gd_txtKelurahan = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_txtKelurahan.widthHint = 319;
		txtKelurahan.setLayoutData(gd_txtKelurahan);
		
		Label lblBidangUsaha = new Label(parent, SWT.NONE);
		lblBidangUsaha.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		//		lblBidangUsaha.setForeground(fontColor);
				lblBidangUsaha.setText("Bidang Usaha");
		
		txtBidangUsaha = new Text(parent, SWT.BORDER);
		txtBidangUsaha.setEditable(false);
		txtBidangUsaha.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtBidangUsaha.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		GridData gd_txtBidangUsaha = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_txtBidangUsaha.widthHint = 305;
		txtBidangUsaha.setLayoutData(gd_txtBidangUsaha);
		txtBidangUsaha.setText(ControllerFactory.getMainController().getCpBidangUsahaDAOImpl().getBidangUsahaByIdSubPajak(wp.getIdSubPajak()));
		
		Label lblTandaBuktiDiri = new Label(parent, SWT.NONE);
		lblTandaBuktiDiri.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
//		lblTandaBuktiDiri.setForeground(fontColor);
		lblTandaBuktiDiri.setText("Tanda Bukti Diri");
		
		txtBuktiDiri = new Text(parent, SWT.BORDER);
		txtBuktiDiri.setEditable(false);
		txtBuktiDiri.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtBuktiDiri.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		GridData gd_txtBuktiDiri = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_txtBuktiDiri.widthHint = 319;
		txtBuktiDiri.setLayoutData(gd_txtBuktiDiri);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		
		Label lblNomorBuktiDiri = new Label(parent, SWT.NONE);
		lblNomorBuktiDiri.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
//		lblNomorBuktiDiri.setForeground(fontColor);
		lblNomorBuktiDiri.setText("Nomor Bukti Diri");
		
		txtNomor = new Text(parent, SWT.BORDER);
		txtNomor.setEditable(false);
		txtNomor.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		txtNomor.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		GridData gd_txtNomor = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_txtNomor.widthHint = 319;
		txtNomor.setLayoutData(gd_txtNomor);
		
		Label lblKtp = new Label(parent, SWT.NONE);
		lblKtp.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
//		lblKtp.setForeground(fontColor);
		lblKtp.setText("KTP");
		new Label(parent, SWT.NONE);
		
		Label lblSiup = new Label(parent, SWT.NONE);
		lblSiup.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
//		lblSiup.setForeground(fontColor);
		lblSiup.setText("SIUP");
		new Label(parent, SWT.NONE);
		
		Canvas compImageKTP = new Canvas(parent, SWT.NONE);
		final GridData gd_compImage = new GridData(SWT.FILL, SWT.FILL, true, true, 2, 6);
		gd_compImage.heightHint = 600;
		gd_compImage.widthHint = 800;
		compImageKTP.setLayoutData(gd_compImage);
		try {
			String path = "";
			URL url = null;
			if (fileKTP.equalsIgnoreCase("")){
				path = "img/allnoimage.png";
				Bundle bundle = Platform.getBundle("com.dispenda.main");
				url = FileLocator.find(bundle, new Path(path), null);
			}
			else{
				path = "http://simp4d.pemkomedan.go.id/assets/images/scanktp/" + fileKTP;
				url = new URL(path);
			}
			ImageData imgData = new ImageData(url.openStream());
			double ratio = (double)imgData.height/(double)imgData.width;
			double heighT = ratio*gd_compImage.widthHint;
			final Image image = new Image(Display.getCurrent(),imgData.scaledTo(800, (int)heighT));
//			GC gc = new GC(new Image(Display.getCurrent(), imgData.scaledTo(ratio*imgData.width, ratio*imgData.height)));
		//			gc.drawI
		//			compImage.print(gc);
					
					
		//			compImage.setBackgroundImage(new Image(Display.getCurrent(), imgData.scaledTo(600, 400)));
			compImageKTP.addPaintListener(new PaintListener() {
			@Override
			public void paintControl(PaintEvent pe) {
				int tmpX = image.getBounds().width;
		            tmpX = (gd_compImage.widthHint / 2 - tmpX / 2);
		            int tmpY = image.getBounds().height;
		            tmpY = (gd_compImage.heightHint / 2 - tmpY / 2);
		            if(tmpX <= 0) tmpX = pe.x;
		            else tmpX += pe.x;
		            if(tmpY <= 0) tmpY = pe.y;
		            else tmpY += pe.y;
				pe.gc.drawImage(image, tmpX, tmpY);
			}
			
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
		compImageKTP.setLayout(new GridLayout(1, false));
		
		Canvas compImageSIUP = new Canvas(parent, SWT.NONE);
		final GridData gd_compImageSIUP = new GridData(SWT.FILL, SWT.FILL, true, true, 2, 6);
		gd_compImageSIUP.heightHint = 800;
		gd_compImageSIUP.widthHint = 600;
		compImageSIUP.setLayoutData(gd_compImageSIUP);
		try {
			String path = "";
			URL url = null;
			if (fileSIUP.length == 0){
				path = "img/allnoimage.png";
				Bundle bundle = Platform.getBundle("com.dispenda.main");
				url = FileLocator.find(bundle, new Path(path), null);
			}
			else{
				path = "http://simp4d.pemkomedan.go.id/assets/images/scansuratizin/" + fileSIUP[0];
				url = new URL(path);
			}
			ImageData imgData = new ImageData(url.openStream());
			double ratio = (double)imgData.width/(double)imgData.height;
			double heighT = ratio*gd_compImageSIUP.heightHint;
			final Image image = new Image(Display.getCurrent(),imgData.scaledTo((int)heighT, 800));
			compImageSIUP.addPaintListener(new PaintListener() {
			@Override
			public void paintControl(PaintEvent pe) {
				int tmpX = image.getBounds().height;
		            tmpX = (gd_compImageSIUP.heightHint / 2 - tmpX / 2);
		            int tmpY = image.getBounds().width;
		            tmpY = (gd_compImageSIUP.widthHint / 2 - tmpY / 2);
		            if(tmpX <= 0) tmpX = pe.x;
		            else tmpX += pe.x;
		            if(tmpY <= 0) tmpY = pe.y;
		            else tmpY += pe.y;
				pe.gc.drawImage(image, tmpX, tmpY);
			}
		});
		}catch (IOException e) {
				e.printStackTrace();
		}compImageSIUP.setLayout(new GridLayout(1, false));
		txtAlamat.setText(wp.getAlabadJalan());
		txtKecamatan.setText(ControllerFactory.getMainController().getCpKecamatanDAOImpl().getKecamatan(Integer.valueOf(wp.getAlabadKecamatan())).getNamaKecamatan());
		txtKelurahan.setText(ControllerFactory.getMainController().getCpKelurahanDAOImpl().getKelurahanByID(Integer.valueOf(wp.getAlabadKelurahan()), Integer.valueOf(wp.getAlabadKecamatan())));
		txtBuktiDiri.setText(wp.getTandaBuktiDiri());
		txtNomor.setText(wp.getNoBuktiDiri());
		return parent;
	}
}
