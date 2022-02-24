package com.dispenda.control.views;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
//import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.SWTResourceManager;

import com.dispenda.control.dialogs.KecamatanDialog;
import com.dispenda.control.dialogs.KelurahanDialog;
import com.dispenda.model.Kecamatan;
import com.dispenda.model.KecamatanProvider;
import com.dispenda.model.Kelurahan;
import com.dispenda.model.KelurahanProvider;
import com.dispenda.object.Preferences;

public class KecamatanView extends ViewPart {
	public KecamatanView() {
	}
	public static final String ID = KecamatanView.class.getName();
	private TableViewer viewerKecamatan;
	private Table tableKecamatan;
	private Button btnTambahKotaKecamatan;
	private TableViewer viewerKelurahan;
	private Table tableKelurahan;
	private Button btnTambahKotaKelurahan;
	private Button btnHapusKotaKecamatan;
	private Button btnHapusKotaKelurahan;
//	private Color fontColor = new Color(Display.getCurrent(),Preferences.FONT_COLOR);

	@Override
	public void createPartControl(Composite parent) {
		ScrolledComposite scrolledComposite = new ScrolledComposite(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		
		Composite composite = new Composite(scrolledComposite, SWT.NONE);
		composite.setLayout(new GridLayout(5, false));
		
		viewerKecamatan = new TableViewer(composite,  SWT.H_SCROLL  | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER | SWT.VIRTUAL);
		
	    String[] titlesKecamatan = { "Kode Kecamatan", "Nama Kecamatan"};
	    int[] boundsKecamatan = {250, 250};
	    
	    TableViewerColumn colKodeKecamatan = createTableViewerColumn(viewerKecamatan,titlesKecamatan[0], boundsKecamatan[0], 0);
	    colKodeKecamatan.setLabelProvider(new ColumnLabelProvider() {
	    	public String getText(Object element) {
	    		Kecamatan p = (Kecamatan) element;
		        return p.getKodeKecamatan();
		    }
		});
	    
	    TableViewerColumn colNamaKotaKecamatan = createTableViewerColumn(viewerKecamatan,titlesKecamatan[1], boundsKecamatan[1], 1);
	    colNamaKotaKecamatan.setLabelProvider(new ColumnLabelProvider() {
	    	public String getText(Object element) {
	    		Kecamatan p = (Kecamatan) element;
		        return p.getNamaKecamatan();
		    }
		});
	    
		tableKecamatan = viewerKecamatan.getTable();
		tableKecamatan.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		tableKecamatan.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				btnHapusKotaKelurahan.setEnabled(true);
				btnTambahKotaKelurahan.setEnabled(true);
				Kecamatan kecamatan = (Kecamatan)tableKecamatan.getItem(tableKecamatan.getSelectionIndex()).getData();
				viewerKelurahan.setInput(KelurahanProvider.INSTANCE.getKelurahan(kecamatan.getIdKecamatan()));
			}
		});
		tableKecamatan.addKeyListener(new KeyAdapter() {
	    	@Override
	    	public void keyPressed(KeyEvent e) {
	    		if(e.keyCode==SWT.CR|e.keyCode==SWT.KEYPAD_CR){
	    			Kecamatan ckk = (Kecamatan)tableKecamatan.getItem(tableKecamatan.getSelectionIndex()).getData();
	    			KecamatanDialog ckd = new KecamatanDialog(Display.getCurrent().getActiveShell(), ckk,tableKecamatan.getSelectionIndex());
	    			ckd.open();
	    			viewerKecamatan.refresh();
	    		}
	    	}
	    });
		tableKecamatan.addMouseListener(new MouseAdapter() {
	    	public void mouseDoubleClick(MouseEvent e) {
	    		Kecamatan ckk = (Kecamatan)tableKecamatan.getItem(tableKecamatan.getSelectionIndex()).getData();
    			KecamatanDialog ckd = new KecamatanDialog(Display.getCurrent().getActiveShell(), ckk,tableKecamatan.getSelectionIndex());
    			ckd.open();
    			viewerKecamatan.refresh();
	    	}
	    });
		tableKecamatan.setLinesVisible(true);
		tableKecamatan.setHeaderVisible(true);
		tableKecamatan.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		tableKecamatan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		tableKecamatan.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 2));
		
		viewerKecamatan.setContentProvider(new ArrayContentProvider());
		viewerKecamatan.setInput(KecamatanProvider.INSTANCE.getKecamatan());
		getSite().setSelectionProvider(viewerKecamatan);
		
		btnTambahKotaKecamatan = new Button(composite, SWT.NONE);
		btnTambahKotaKecamatan.setEnabled(true);
		btnTambahKotaKecamatan.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				KecamatanDialog cpd = new KecamatanDialog(Display.getCurrent().getActiveShell());
				cpd.open();
				viewerKecamatan.refresh();
			}
		});
		GridData gd_btnTambahKotaKecamatan = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnTambahKotaKecamatan.widthHint = 90;
		gd_btnTambahKotaKecamatan.verticalIndent = 30;
		gd_btnTambahKotaKecamatan.heightHint = 50;
		btnTambahKotaKecamatan.setLayoutData(gd_btnTambahKotaKecamatan);
		btnTambahKotaKecamatan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.BOLD));
		btnTambahKotaKecamatan.setText("+ Tambah");
		
		Label label_1 = new Label(composite, SWT.SEPARATOR | SWT.VERTICAL);
		label_1.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 2));
		
		viewerKelurahan = new TableViewer(composite,  SWT.H_SCROLL  | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER | SWT.VIRTUAL);
		
	    String[] titlesKelurahan = { "Kode Kelurahan","Nama Kelurahan"};
	    int[] boundsKelurahan = { 250,250};
	    
	    TableViewerColumn colKodeKelurahan = createTableViewerColumn(viewerKelurahan,titlesKelurahan[0], boundsKelurahan[0], 0);
	    colKodeKelurahan.setLabelProvider(new ColumnLabelProvider() {
	    	public String getText(Object element) {
	    		Kelurahan p = (Kelurahan) element;
		        return p.getKodeKelurahan();
		    }
		});
	    
	    TableViewerColumn colNamaKotaKelurahan = createTableViewerColumn(viewerKelurahan,titlesKelurahan[1], boundsKelurahan[1], 1);
	    colNamaKotaKelurahan.setLabelProvider(new ColumnLabelProvider() {
	    	public String getText(Object element) {
	    		Kelurahan p = (Kelurahan) element;
		        return p.getNamaKelurahan();
		    }
		});
	    
		tableKelurahan = viewerKelurahan.getTable();
		tableKelurahan.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		tableKelurahan.addKeyListener(new KeyAdapter() {
	    	@Override
	    	public void keyPressed(KeyEvent e) {
	    		if(e.keyCode==SWT.CR|e.keyCode==SWT.KEYPAD_CR){
	    			Kelurahan ckk = (Kelurahan)tableKelurahan.getItem(tableKelurahan.getSelectionIndex()).getData();
	    			KelurahanDialog ckd = new KelurahanDialog(Display.getCurrent().getActiveShell(), ckk, tableKelurahan.getSelectionIndex());
	    			ckd.open();
	    			viewerKelurahan.refresh();
	    		}
	    	}
	    });
		tableKelurahan.addMouseListener(new MouseAdapter() {
	    	public void mouseDoubleClick(MouseEvent e) {
	    		Kelurahan ckk = (Kelurahan)tableKelurahan.getItem(tableKelurahan.getSelectionIndex()).getData();
    			KelurahanDialog ckd = new KelurahanDialog(Display.getCurrent().getActiveShell(), ckk, tableKelurahan.getSelectionIndex());
    			ckd.open();
    			viewerKelurahan.refresh();
	    	}
	    });
		tableKelurahan.setLinesVisible(true);
		tableKelurahan.setHeaderVisible(true);
		tableKelurahan.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		tableKelurahan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		tableKelurahan.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 2));
		
		viewerKelurahan.setContentProvider(new ArrayContentProvider());
		getSite().setSelectionProvider(viewerKelurahan);
		
		btnTambahKotaKelurahan = new Button(composite, SWT.NONE);
		btnTambahKotaKelurahan.setEnabled(false);
		btnTambahKotaKelurahan.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Kecamatan ck = (Kecamatan)tableKecamatan.getItem(tableKecamatan.getSelectionIndex()).getData();
				KelurahanDialog cpd = new KelurahanDialog(Display.getCurrent().getActiveShell(),ck.getIdKecamatan());
				cpd.open();
				viewerKelurahan.refresh();
			}
		});
		GridData gd_btnTambahKotaKelurahan = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnTambahKotaKelurahan.widthHint = 90;
		gd_btnTambahKotaKelurahan.verticalIndent = 30;
		gd_btnTambahKotaKelurahan.heightHint = 50;
		btnTambahKotaKelurahan.setLayoutData(gd_btnTambahKotaKelurahan);
		btnTambahKotaKelurahan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.BOLD));
		btnTambahKotaKelurahan.setText("+ Tambah");
		
		btnHapusKotaKecamatan = new Button(composite, SWT.NONE);
		btnHapusKotaKecamatan.setEnabled(true);
		btnHapusKotaKecamatan.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				boolean result = MessageDialog.openConfirm(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Apakah anda yakin ingin menghapus data ini?");
		    	if(result){
		    		KecamatanProvider.INSTANCE.removeItem(tableKecamatan.getSelectionIndex());
		    		tableKecamatan.remove(tableKecamatan.getSelectionIndex());
		    		viewerKecamatan.refresh();
		    	}
			}
		});
		GridData gd_btnHapusKecamatan = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
		gd_btnHapusKecamatan.widthHint = 90;
		gd_btnHapusKecamatan.heightHint = 50;
		btnHapusKotaKecamatan.setLayoutData(gd_btnHapusKecamatan);
		btnHapusKotaKecamatan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.BOLD));
		btnHapusKotaKecamatan.setText("- Hapus");
		
		btnHapusKotaKelurahan = new Button(composite, SWT.NONE);
		btnHapusKotaKelurahan.setEnabled(false);
		btnHapusKotaKelurahan.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				boolean result = MessageDialog.openConfirm(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Apakah anda yakin ingin menghapus data ini?");
		    	if(result){
		    		KelurahanProvider.INSTANCE.removeItem(tableKelurahan.getSelectionIndex());
		    		tableKelurahan.remove(tableKelurahan.getSelectionIndex());
		    		viewerKelurahan.refresh();
		    	}
			}
		});
		GridData gd_btnHapusKotaKelurahan = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
		gd_btnHapusKotaKelurahan.widthHint = 90;
		gd_btnHapusKotaKelurahan.heightHint = 50;
		btnHapusKotaKelurahan.setLayoutData(gd_btnHapusKotaKelurahan);
		btnHapusKotaKelurahan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.BOLD));
		btnHapusKotaKelurahan.setText("- Hapus");
		
		scrolledComposite.setContent(composite);
		scrolledComposite.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));

	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	private TableViewerColumn createTableViewerColumn(TableViewer viewer, String title, int bound, int colNumber) {
		TableViewerColumn viewerColumn = new TableViewerColumn(viewer,SWT.NONE);
		TableColumn column = viewerColumn.getColumn();
		column.setText(title);
		column.setWidth(bound);
		column.setResizable(true);
		column.setMoveable(true);
		return viewerColumn;
	}
}
