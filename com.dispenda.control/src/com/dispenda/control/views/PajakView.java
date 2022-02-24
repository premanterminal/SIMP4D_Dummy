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

import com.dispenda.control.dialogs.PajakDialog;
import com.dispenda.control.dialogs.SubPajakDialog;
import com.dispenda.model.BidangUsaha;
import com.dispenda.model.BidangUsahaProvider;
import com.dispenda.model.Pajak;
import com.dispenda.model.PajakProvider;
import com.dispenda.object.Preferences;

public class PajakView extends ViewPart {
	public PajakView() {
	}
	public static final String ID = PajakView.class.getName();
	private TableViewer viewerPajak;
	private Table tablePajak;
	private Button btnTambahPajak;
	private TableViewer viewerSubPajak;
	private Table tableSubPajak;
	private Button btnTambahSubPajak;
	private Button btnHapusPajak;
	private Button btnHapusSubPajak;

	@Override
	public void createPartControl(Composite parent) {
//		parent.setBackground(new Color(Display.getCurrent(), 32, 172, 192));
		
		ScrolledComposite scrolledComposite = new ScrolledComposite(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		
		Composite composite = new Composite(scrolledComposite, SWT.NONE);
		composite.setLayout(new GridLayout(5, false));
		
		viewerPajak = new TableViewer(composite,  SWT.H_SCROLL  | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER | SWT.VIRTUAL);
		
	    String[] titlesKecamatan = { "Kode Pajak", "Nama Pajak", "Kode Rekening Pajak Denda"};
	    int[] boundsKecamatan = {100, 200, 200};
	    
	    TableViewerColumn colKodePajak = createTableViewerColumn(viewerPajak,titlesKecamatan[0], boundsKecamatan[0], 0);
	    colKodePajak.setLabelProvider(new ColumnLabelProvider() {
	    	public String getText(Object element) {
	    		Pajak p = (Pajak) element;
		        return p.getKodePajak();
		    }
		});
	    
	    TableViewerColumn colNamaPajak = createTableViewerColumn(viewerPajak,titlesKecamatan[1], boundsKecamatan[1], 1);
	    colNamaPajak.setLabelProvider(new ColumnLabelProvider() {
	    	public String getText(Object element) {
	    		Pajak p = (Pajak) element;
		        return p.getNamaPajak();
		    }
		});
	    
	    TableViewerColumn colKodeDenda = createTableViewerColumn(viewerPajak,titlesKecamatan[2], boundsKecamatan[2], 2);
	    colKodeDenda.setLabelProvider(new ColumnLabelProvider() {
	    	public String getText(Object element) {
	    		Pajak p = (Pajak) element;
		        return p.getKodeDenda();
		    }
		});
		
	    tablePajak = viewerPajak.getTable();
	    tablePajak.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		tablePajak.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				btnHapusSubPajak.setEnabled(true);
				btnTambahSubPajak.setEnabled(true);
				Pajak pajak = (Pajak)tablePajak.getItem(tablePajak.getSelectionIndex()).getData();
				viewerSubPajak.setInput(BidangUsahaProvider.INSTANCE.getBidangUsaha(pajak.getIdPajak()));
			}
		});
		tablePajak.addKeyListener(new KeyAdapter() {
	    	@Override
	    	public void keyPressed(KeyEvent e) {
	    		if(e.keyCode==SWT.CR|e.keyCode==SWT.KEYPAD_CR){
	    			Pajak ckk = (Pajak)tablePajak.getItem(tablePajak.getSelectionIndex()).getData();
	    			PajakDialog ckd = new PajakDialog(Display.getCurrent().getActiveShell(), ckk,tablePajak.getSelectionIndex());
	    			ckd.open();
	    			viewerPajak.refresh();
	    		}
	    	}
	    });
		tablePajak.addMouseListener(new MouseAdapter() {
	    	public void mouseDoubleClick(MouseEvent e) {
	    		Pajak ckk = (Pajak)tablePajak.getItem(tablePajak.getSelectionIndex()).getData();
	    		PajakDialog ckd = new PajakDialog(Display.getCurrent().getActiveShell(), ckk,tablePajak.getSelectionIndex());
    			ckd.open();
    			viewerPajak.refresh();
	    	}
	    });
		tablePajak.setLinesVisible(true);
		tablePajak.setHeaderVisible(true);
		tablePajak.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		tablePajak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		tablePajak.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 2));
		
		viewerPajak.setContentProvider(new ArrayContentProvider());
		viewerPajak.setInput(PajakProvider.INSTANCE.getPajak());
		getSite().setSelectionProvider(viewerPajak);
	    
		btnTambahPajak = new Button(composite, SWT.NONE);
		btnTambahPajak.setEnabled(true);
		btnTambahPajak.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				PajakDialog cpd = new PajakDialog(Display.getCurrent().getActiveShell());
				cpd.open();
				viewerPajak.refresh();
			}
		});
		GridData gd_btnTambahKotaKecamatan = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnTambahKotaKecamatan.widthHint = 90;
		gd_btnTambahKotaKecamatan.verticalIndent = 30;
		gd_btnTambahKotaKecamatan.heightHint = 50;
		btnTambahPajak.setLayoutData(gd_btnTambahKotaKecamatan);
		btnTambahPajak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.BOLD));
		btnTambahPajak.setText("+ Tambah");
		
		Label label_1 = new Label(composite, SWT.SEPARATOR | SWT.VERTICAL);
		label_1.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 2));
		
		viewerSubPajak = new TableViewer(composite,  SWT.H_SCROLL  | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER | SWT.VIRTUAL);
		
	    String[] titlesSubPajak = { "Kode Rekening Pajak","Nama Bidang Usaha","Tarif","Bunga","Denda","Kenaikan 1","Kenaikan 2"};
	    int[] boundsSubPajak = { 200,200,100,100,100,150,150};
	    
	    TableViewerColumn colKodeSubPajak = createTableViewerColumn(viewerSubPajak,titlesSubPajak[0], boundsSubPajak[0], 0);
	    colKodeSubPajak.setLabelProvider(new ColumnLabelProvider() {
	    	public String getText(Object element) {
	    		BidangUsaha p = (BidangUsaha) element;
		        return p.getKodeBidUsaha();
		    }
		});
	    
	    TableViewerColumn colNamaSubPajak = createTableViewerColumn(viewerSubPajak,titlesSubPajak[1], boundsSubPajak[1], 1);
	    colNamaSubPajak.setLabelProvider(new ColumnLabelProvider() {
	    	public String getText(Object element) {
	    		BidangUsaha p = (BidangUsaha) element;
		        return p.getNamaBidUsaha();
		    }
		});
	    
	    TableViewerColumn colTarif = createTableViewerColumn(viewerSubPajak,titlesSubPajak[2], boundsSubPajak[2], 2);
	    colTarif.setLabelProvider(new ColumnLabelProvider() {
	    	public String getText(Object element) {
	    		BidangUsaha p = (BidangUsaha) element;
		        return p.getTarif().toString();
		    }
		});
	    
	    TableViewerColumn colBunga = createTableViewerColumn(viewerSubPajak,titlesSubPajak[3], boundsSubPajak[3], 3);
	    colBunga.setLabelProvider(new ColumnLabelProvider() {
	    	public String getText(Object element) {
	    		BidangUsaha p = (BidangUsaha) element;
		        return p.getBunga().toString();
		    }
		});
	    
	    TableViewerColumn colDenda = createTableViewerColumn(viewerSubPajak,titlesSubPajak[4], boundsSubPajak[4], 4);
	    colDenda.setLabelProvider(new ColumnLabelProvider() {
	    	public String getText(Object element) {
	    		BidangUsaha p = (BidangUsaha) element;
		        return p.getDenda().toString();
		    }
		});
	    
	    TableViewerColumn colKenaikan1 = createTableViewerColumn(viewerSubPajak,titlesSubPajak[5], boundsSubPajak[5], 5);
	    colKenaikan1.setLabelProvider(new ColumnLabelProvider() {
	    	public String getText(Object element) {
	    		BidangUsaha p = (BidangUsaha) element;
		        return p.getKenaikan1().toString();
		    }
		});
	    
	    TableViewerColumn colKenaikan2 = createTableViewerColumn(viewerSubPajak,titlesSubPajak[6], boundsSubPajak[6], 6);
	    colKenaikan2.setLabelProvider(new ColumnLabelProvider() {
	    	public String getText(Object element) {
	    		BidangUsaha p = (BidangUsaha) element;
		        return p.getKenaikan2().toString();
		    }
		});
	    
		tableSubPajak = viewerSubPajak.getTable();
		tableSubPajak.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		tableSubPajak.addKeyListener(new KeyAdapter() {
	    	@Override
	    	public void keyPressed(KeyEvent e) {
	    		if(e.keyCode==SWT.CR|e.keyCode==SWT.KEYPAD_CR){
	    			BidangUsaha ckk = (BidangUsaha)tableSubPajak.getItem(tableSubPajak.getSelectionIndex()).getData();
	    			SubPajakDialog ckd = new SubPajakDialog(Display.getCurrent().getActiveShell(), ckk, tableSubPajak.getSelectionIndex());
	    			ckd.open();
	    			viewerSubPajak.refresh();
	    		}
	    	}
	    });
		tableSubPajak.addMouseListener(new MouseAdapter() {
	    	public void mouseDoubleClick(MouseEvent e) {
	    		BidangUsaha ckk = (BidangUsaha)tableSubPajak.getItem(tableSubPajak.getSelectionIndex()).getData();
	    		SubPajakDialog ckd = new SubPajakDialog(Display.getCurrent().getActiveShell(), ckk, tableSubPajak.getSelectionIndex());
    			ckd.open();
    			viewerSubPajak.refresh();
	    	}
	    });
		tableSubPajak.setLinesVisible(true);
		tableSubPajak.setHeaderVisible(true);
		tableSubPajak.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		tableSubPajak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		tableSubPajak.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 2));
		
		viewerSubPajak.setContentProvider(new ArrayContentProvider());
		getSite().setSelectionProvider(viewerSubPajak);
		
		btnTambahSubPajak = new Button(composite, SWT.NONE);
		btnTambahSubPajak.setEnabled(false);
		btnTambahSubPajak.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Pajak ck = (Pajak)tablePajak.getItem(tablePajak.getSelectionIndex()).getData();
				SubPajakDialog cpd = new SubPajakDialog(Display.getCurrent().getActiveShell(),ck.getIdPajak());
				cpd.open();
				viewerSubPajak.refresh();
			}
		});
		GridData gd_btnTambahKotaKelurahan = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnTambahKotaKelurahan.widthHint = 90;
		gd_btnTambahKotaKelurahan.verticalIndent = 30;
		gd_btnTambahKotaKelurahan.heightHint = 50;
		btnTambahSubPajak.setLayoutData(gd_btnTambahKotaKelurahan);
		btnTambahSubPajak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.BOLD));
		btnTambahSubPajak.setText("+ Tambah");
		
		btnHapusPajak = new Button(composite, SWT.NONE);
		btnHapusPajak.setEnabled(true);
		btnHapusPajak.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				boolean result = MessageDialog.openConfirm(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Apakah anda yakin ingin menghapus data ini?");
		    	if(result){
		    		PajakProvider.INSTANCE.removeItem(tablePajak.getSelectionIndex());
		    		tablePajak.remove(tablePajak.getSelectionIndex());
		    		viewerPajak.refresh();
		    	}
			}
		});
		GridData gd_btnHapusKecamatan = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
		gd_btnHapusKecamatan.widthHint = 90;
		gd_btnHapusKecamatan.heightHint = 50;
		btnHapusPajak.setLayoutData(gd_btnHapusKecamatan);
		btnHapusPajak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.BOLD));
		btnHapusPajak.setText("- Hapus");
		
		btnHapusSubPajak = new Button(composite, SWT.NONE);
		btnHapusSubPajak.setEnabled(false);
		btnHapusSubPajak.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				boolean result = MessageDialog.openConfirm(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Apakah anda yakin ingin menghapus data ini?");
		    	if(result){
		    		BidangUsahaProvider.INSTANCE.removeItem(tableSubPajak.getSelectionIndex());
		    		tableSubPajak.remove(tableSubPajak.getSelectionIndex());
		    		viewerSubPajak.refresh();
		    	}
			}
		});
		GridData gd_btnHapusKotaKelurahan = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
		gd_btnHapusKotaKelurahan.widthHint = 90;
		gd_btnHapusKotaKelurahan.heightHint = 50;
		btnHapusSubPajak.setLayoutData(gd_btnHapusKotaKelurahan);
		btnHapusSubPajak.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.BOLD));
		btnHapusSubPajak.setText("- Hapus");
		
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
