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
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.SWTResourceManager;

import com.dispenda.control.dialogs.PejabatDialog;
import com.dispenda.model.Pejabat;
import com.dispenda.model.PejabatProvider;
import com.dispenda.object.Preferences;

public class PejabatView extends ViewPart {
	public PejabatView() {
	}
	public static final String ID = PejabatView.class.getName();
	private TableViewer viewerPejabat;
	private Table tablePejabat;
	private Button btnTambahPejabat;
	private Button btnHapusPejabat;

	@Override
	public void createPartControl(Composite parent) {
//		parent.setBackground(new Color(Display.getCurrent(), 32, 172, 192));
		
		ScrolledComposite scrolledComposite = new ScrolledComposite(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		
		Composite composite = new Composite(scrolledComposite, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));
		
		viewerPejabat = new TableViewer(composite,  SWT.H_SCROLL  | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER | SWT.VIRTUAL);
		
	    String[] titlesPejabat = { "NIP", "Nama Pejabat", "Pangkat", "Jabatan"};
	    int[] boundsPejabat = {250, 200, 200,250};
	    
	    TableViewerColumn colNIP = createTableViewerColumn(viewerPejabat,titlesPejabat[0], boundsPejabat[0], 0);
	    colNIP.setLabelProvider(new ColumnLabelProvider() {
	    	public String getText(Object element) {
	    		Pejabat p = (Pejabat) element;
		        return p.getIdPejabatNIP();
		    }
		});
	    
	    TableViewerColumn colNamaPejabat = createTableViewerColumn(viewerPejabat,titlesPejabat[1], boundsPejabat[1], 1);
	    colNamaPejabat.setLabelProvider(new ColumnLabelProvider() {
	    	public String getText(Object element) {
	    		Pejabat p = (Pejabat) element;
		        return p.getNamaPejabat();
		    }
		});
	    
	    TableViewerColumn colPangkat = createTableViewerColumn(viewerPejabat,titlesPejabat[2], boundsPejabat[2], 2);
	    colPangkat.setLabelProvider(new ColumnLabelProvider() {
	    	public String getText(Object element) {
	    		Pejabat p = (Pejabat) element;
		        return p.getPangkat();
		    }
		});
		
	    TableViewerColumn colJabatan = createTableViewerColumn(viewerPejabat,titlesPejabat[3], boundsPejabat[3], 3);
	    colJabatan.setLabelProvider(new ColumnLabelProvider() {
	    	public String getText(Object element) {
	    		Pejabat p = (Pejabat) element;
		        return p.getJabatan();
		    }
		});
	    
	    tablePejabat = viewerPejabat.getTable();
	    tablePejabat.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		tablePejabat.addKeyListener(new KeyAdapter() {
	    	@Override
	    	public void keyPressed(KeyEvent e) {
	    		if(e.keyCode==SWT.CR|e.keyCode==SWT.KEYPAD_CR){
	    			Pejabat ckk = (Pejabat)tablePejabat.getItem(tablePejabat.getSelectionIndex()).getData();
	    			PejabatDialog ckd = new PejabatDialog(Display.getCurrent().getActiveShell(), ckk,tablePejabat.getSelectionIndex());
	    			ckd.open();
	    			viewerPejabat.refresh();
	    		}
	    	}
	    });
		tablePejabat.addMouseListener(new MouseAdapter() {
	    	public void mouseDoubleClick(MouseEvent e) {
	    		Pejabat ckk = (Pejabat)tablePejabat.getItem(tablePejabat.getSelectionIndex()).getData();
	    		PejabatDialog ckd = new PejabatDialog(Display.getCurrent().getActiveShell(), ckk,tablePejabat.getSelectionIndex());
    			ckd.open();
    			viewerPejabat.refresh();
	    	}
	    });
		tablePejabat.setLinesVisible(true);
		tablePejabat.setHeaderVisible(true);
		tablePejabat.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		tablePejabat.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		tablePejabat.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 2));
		
		viewerPejabat.setContentProvider(new ArrayContentProvider());
		viewerPejabat.setInput(PejabatProvider.INSTANCE.getPejabat());
		getSite().setSelectionProvider(viewerPejabat);
	    
		btnTambahPejabat = new Button(composite, SWT.NONE);
		btnTambahPejabat.setEnabled(true);
		btnTambahPejabat.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				PejabatDialog cpd = new PejabatDialog(Display.getCurrent().getActiveShell());
				cpd.open();
				viewerPejabat.refresh();
			}
		});
		GridData gd_btnTambahKotaKecamatan = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnTambahKotaKecamatan.widthHint = 90;
		gd_btnTambahKotaKecamatan.verticalIndent = 30;
		gd_btnTambahKotaKecamatan.heightHint = 50;
		btnTambahPejabat.setLayoutData(gd_btnTambahKotaKecamatan);
		btnTambahPejabat.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.BOLD));
		btnTambahPejabat.setText("+ Tambah");
		
		btnHapusPejabat = new Button(composite, SWT.NONE);
		btnHapusPejabat.setEnabled(true);
		btnHapusPejabat.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				boolean result = MessageDialog.openConfirm(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Apakah anda yakin ingin menghapus data ini?");
		    	if(result){
		    		PejabatProvider.INSTANCE.removeItem(tablePejabat.getSelectionIndex());
		    		tablePejabat.remove(tablePejabat.getSelectionIndex());
		    		viewerPejabat.refresh();
		    	}
			}
		});
		GridData gd_btnHapusKecamatan = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
		gd_btnHapusKecamatan.widthHint = 90;
		gd_btnHapusKecamatan.heightHint = 50;
		btnHapusPejabat.setLayoutData(gd_btnHapusKecamatan);
		btnHapusPejabat.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.BOLD));
		btnHapusPejabat.setText("- Hapus");
		
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