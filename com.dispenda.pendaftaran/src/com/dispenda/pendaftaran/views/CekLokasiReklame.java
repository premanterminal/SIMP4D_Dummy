package com.dispenda.pendaftaran.views;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import com.dispenda.controller.ControllerFactory;
import com.dispenda.controller.UIController;
import com.dispenda.model.KecamatanProvider;
import com.dispenda.model.KelurahanProvider;
import com.dispenda.object.Preferences;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class CekLokasiReklame extends ViewPart{
	public CekLokasiReklame() {
	}

	public static final String ID = CekLokasiReklame.class.getName();
	private Text txtTeks;
	private Text txtLokasi;
	private Table tblDaftar;
	private Color fontColor = new Color(Display.getCurrent(),Preferences.FONT_COLOR);
	private Combo cmbKelurahan;
	private Combo cmbKecamatan;
	
	@Override
	public void createPartControl(Composite arg0) {
		
		Composite composite = new Composite(arg0, SWT.NONE);
		composite.setLayout(new GridLayout(3, false));
		
		Label lblTeksReklame = new Label(composite, SWT.NONE);
		lblTeksReklame.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblTeksReklame.setForeground(fontColor);
		lblTeksReklame.setText("Teks Reklame");
		
		txtTeks = new Text(composite, SWT.BORDER | SWT.WRAP | SWT.MULTI);
		txtTeks.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtTeks.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		GridData gd_txtTeks = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
		gd_txtTeks.heightHint = 40;
		gd_txtTeks.widthHint = 259;
		txtTeks.setLayoutData(gd_txtTeks);
		
		Label lblTitikLokasi = new Label(composite, SWT.NONE);
		lblTitikLokasi.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblTitikLokasi.setForeground(fontColor);
		lblTitikLokasi.setText("Titik Lokasi");
		
		txtLokasi = new Text(composite, SWT.BORDER | SWT.WRAP | SWT.MULTI);
		txtLokasi.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtLokasi.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		GridData gd_txtLokasi = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
		gd_txtLokasi.heightHint = 40;
		gd_txtLokasi.widthHint = 259;
		txtLokasi.setLayoutData(gd_txtLokasi);
		
		Label lblKecamatan = new Label(composite, SWT.NONE);
		lblKecamatan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblKecamatan.setForeground(fontColor);
		lblKecamatan.setText("Kecamatan");
		
		cmbKecamatan = new Combo(composite, SWT.READ_ONLY);
		cmbKecamatan.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Combo c = (Combo)e.widget;
				if(c.getSelectionIndex() > -1)
					UIController.INSTANCE.loadKelurahan(cmbKelurahan, KelurahanProvider.INSTANCE.getKelurahan((Integer)c.getData(Integer.toString(c.getSelectionIndex()))).toArray());
			}
		});
		cmbKecamatan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		cmbKecamatan.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		GridData gd_cmbKecamatan = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
		gd_cmbKecamatan.widthHint = 137;
		cmbKecamatan.setLayoutData(gd_cmbKecamatan);
		UIController.INSTANCE.loadKecamatan(cmbKecamatan, KecamatanProvider.INSTANCE.getKecamatan().toArray());
		
		Label lblKelurahan = new Label(composite, SWT.NONE);
		lblKelurahan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblKelurahan.setForeground(fontColor);
		lblKelurahan.setText("Kelurahan");
		
		cmbKelurahan = new Combo(composite, SWT.READ_ONLY);
		cmbKelurahan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		cmbKelurahan.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		GridData gd_cmbKelurahan = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
		gd_cmbKelurahan.widthHint = 137;
		cmbKelurahan.setLayoutData(gd_cmbKelurahan);
		new Label(composite, SWT.NONE);
		
		Button btnCari = new Button(composite, SWT.NONE);
		btnCari.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ResultSet resultWP = null;
				if (!txtLokasi.getText().equalsIgnoreCase("")){
					tblDaftar.removeAll();
					resultWP = ControllerFactory.getMainController().getCpWajibPajakDAOImpl().cekTitikLokasi(txtTeks.getText(), txtLokasi.getText(), cmbKecamatan.getText(), cmbKelurahan.getText());
					createColumns();
					loadTable(resultWP);
				}else{
					MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Titik Lokasi harus diisi");
					txtLokasi.forceFocus();
				}
			}
		});
		btnCari.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		GridData gd_btnCari = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnCari.widthHint = 70;
		btnCari.setLayoutData(gd_btnCari);
		btnCari.setText("Cari");
		
		Button btnBaru = new Button(composite, SWT.NONE);
		btnBaru.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				txtTeks.setText("");
				txtLokasi.setText("");
				cmbKecamatan.deselectAll();
				cmbKelurahan.deselectAll();
				cmbKelurahan.removeAll();
				tblDaftar.removeAll();
			}
		});
		btnBaru.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		GridData gd_btnBaru = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnBaru.widthHint = 70;
		btnBaru.setLayoutData(gd_btnBaru);
		btnBaru.setText("Baru");
		
		tblDaftar = new Table(composite, SWT.BORDER | SWT.FULL_SELECTION);
		tblDaftar.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE-1, SWT.NORMAL));
		tblDaftar.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));
		tblDaftar.setBackground(SWTResourceManager.getColor(173, 216, 230));
		tblDaftar.setHeaderVisible(true);
		tblDaftar.setLinesVisible(true);
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		
	}
	
	private void deleteColumns(){
		for(TableColumn col : tblDaftar.getColumns()){
			col.dispose();
		}
	}
	
	private void createColumns(){
		if(tblDaftar.getColumnCount() <= 0){
			String[] listColumn = {"No.","NPWPD", "Nama Badan", "Alamat", "Lokasi"};
			int[] widthColumn = {40, 120, 200, 300, 530};
			for (int i=listColumn.length - 1;i>=0;i--)
			{
				TableColumn colPajak = new TableColumn(tblDaftar, SWT.NONE,0);
				colPajak.setText(listColumn[i]);
				colPajak.setWidth(widthColumn[i]);
			}
		}
	}

	private void loadTable(ResultSet result){
		int count = 1;
		try
		{
			while (result.next())
			{
				TableItem item = new TableItem(tblDaftar, SWT.NONE);
				item.setText(0, count+".");
				item.setText(1, result.getString("NPWPD"));
				item.setText(2, result.getString("Nama_Badan"));
				item.setText(3, result.getString("Alabad_Jalan"));
				item.setText(4, result.getString("Lokasi"));
				count++;
			}
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
}
