package com.dispenda.control.views;

import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.TableEditor;
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
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.SWTResourceManager;

import com.dispenda.control.dialogs.UserDialog;
import com.dispenda.controller.ControllerFactory;
import com.dispenda.model.SubModul;
import com.dispenda.model.SubModulProvider;
import com.dispenda.model.User;
import com.dispenda.model.UserModul;
import com.dispenda.model.UserModulProvider;
import com.dispenda.model.UserProvider;
import com.dispenda.object.Preferences;

public class UserView extends ViewPart {
	
	public static final String ID = UserView.class.getName();
	private Button btnTambah;
	private Table table;
	private TableViewer tableViewer;
	private Table tablePrivilage;
	private List<SubModul> listSubModul;
	private Button btnHapus;
	private Button btnSimpan;
	
	public UserView() {
	}

	public void createPartControl(Composite parent) {
//		parent.setBackground(new Color(Display.getCurrent(), 32, 172, 192));
		
		ScrolledComposite scrolledComposite = new ScrolledComposite(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		
		Composite composite = new Composite(scrolledComposite, SWT.NONE);
		composite.setLayout(new GridLayout(4, false));
		
		btnTambah = new Button(composite, SWT.NONE);
		btnTambah.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				UserDialog userDialog = new UserDialog(Display.getCurrent().getActiveShell());
				userDialog.open();
				tableViewer.refresh();
			}
		});
		GridData gd_btnTambah = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnTambah.heightHint = 50;
		gd_btnTambah.widthHint = 90;
		btnTambah.setLayoutData(gd_btnTambah);
		btnTambah.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.BOLD));
		btnTambah.setText("+ Tambah");
		
		tableViewer = new TableViewer(composite, SWT.BORDER | SWT.FULL_SELECTION | SWT.VIRTUAL);
		String[] titles = { "Username","Display Name"};
	    int[] bounds = { 250,250};
	    
	    TableViewerColumn colUsername = createTableViewerColumn(tableViewer,titles[0], bounds[0], 0);
	    colUsername.setLabelProvider(new ColumnLabelProvider() {
	    	public String getText(Object element) {
	    		User p = (User) element;
		        return p.getUsername();
		    }
		});
	    
	    TableViewerColumn colDisplayName = createTableViewerColumn(tableViewer,titles[1], bounds[1], 1);
	    colDisplayName.setLabelProvider(new ColumnLabelProvider() {
	    	public String getText(Object element) {
	    		User p = (User) element;
		        return p.getDisplayName();
		    }
		});
	    
		table = tableViewer.getTable();
		table.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		table.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				User user = (User)table.getItem(table.getSelectionIndex()).getData();
				List<UserModul> userModul = UserModulProvider.INSTANCE.getUserModul(user.getIdUser());
				Control[] children = tablePrivilage.getChildren();
				for(int m=0;m<children.length;m++){
					Button check = (Button)children[m];
					check.setSelection(false);
				}
				int l = 0;
				if(userModul.size()>0){
					for(int i=0;i<listSubModul.size();i++){
						for(int j=0;j<userModul.size();j++){
							if(listSubModul.get(i).getIdSubModul() == userModul.get(j).getIdSubModul()){
								Button check = (Button)children[l];
								if(userModul.get(j).getBuka())
									check.setSelection(true);
								else
									check.setSelection(false);
								check = (Button)children[l+1];
								if(userModul.get(j).getSimpan())
									check.setSelection(true);
								else
									check.setSelection(false);
								check = (Button)children[l+2];
								if(userModul.get(j).getHapus())
									check.setSelection(true);
								else
									check.setSelection(false);
								check = (Button)children[l+3];
								if(userModul.get(j).getCetak())
									check.setSelection(true);
								else
									check.setSelection(false);
								check = (Button)children[l+4];
								if(userModul.get(j).getCetak()&&userModul.get(j).getBuka()&&userModul.get(j).getHapus()&&userModul.get(j).getSimpan())
									check.setSelection(true);
								else
									check.setSelection(false);
								l = l+5;
							}
						}
					}
				}
			}
		});
		table.addKeyListener(new KeyAdapter() {
	    	@Override
	    	public void keyPressed(KeyEvent e) {
	    		if(e.keyCode==SWT.CR|e.keyCode==SWT.KEYPAD_CR){
	    			User user = (User)table.getItem(table.getSelectionIndex()).getData();
	    			UserDialog ckd = new UserDialog(Display.getCurrent().getActiveShell(), user, table.getSelectionIndex());
	    			ckd.open();
	    			tableViewer.refresh();
	    		}
	    	}
	    });
		table.addMouseListener(new MouseAdapter() {
	    	public void mouseDoubleClick(MouseEvent e) {
	    		User user = (User)table.getItem(table.getSelectionIndex()).getData();
	    		UserDialog ckd = new UserDialog(Display.getCurrent().getActiveShell(), user, table.getSelectionIndex());
    			ckd.open();
    			tableViewer.refresh();
	    	}
	    });
		table.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 4));
		
		tableViewer.setContentProvider(new ArrayContentProvider());
		tableViewer.setInput(UserProvider.INSTANCE.getUser());
		getSite().setSelectionProvider(tableViewer);
		
		Label label = new Label(composite, SWT.SEPARATOR | SWT.VERTICAL);
		GridData gd_label = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 3);
		gd_label.heightHint = 161;
		label.setLayoutData(gd_label);
		
		tablePrivilage = new Table(composite, SWT.BORDER | SWT.FULL_SELECTION | SWT.VIRTUAL);
		tablePrivilage.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		tablePrivilage.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		tablePrivilage.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 3));
		tablePrivilage.setHeaderVisible(true);
		tablePrivilage.setLinesVisible(true);
		String[] listText = new String[]{"SubModul","Buka","Simpan","Hapus","Cetak","Select All"};
		int[] listWidth = new int[]{250,150,150,150,150,150};
		for(int i=0;i<6;i++){
			TableColumn colTablePrivilage = new TableColumn(tablePrivilage, SWT.CENTER);
			colTablePrivilage.setText(listText[i]);
			colTablePrivilage.setWidth(listWidth[i]);
		}
		loadTableSubModuleForPrivilage();
		
		btnHapus = new Button(composite, SWT.NONE);
		btnHapus.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				boolean result = MessageDialog.openConfirm(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Apakah anda yakin ingin menghapus user " + table.getItem(table.getSelectionIndex()).getText(1) + "?");
				if(result){
					UserProvider.INSTANCE.removeItem(table.getSelectionIndex());
					table.remove(table.getSelectionIndex());
					tableViewer.refresh();
				}
			}
		});
		GridData gd_btnHapus = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnHapus.widthHint = 90;
		gd_btnHapus.heightHint = 50;
		btnHapus.setLayoutData(gd_btnHapus);
		btnHapus.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.BOLD));
		btnHapus.setText("- Hapus");
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		
		btnSimpan = new Button(composite, SWT.NONE);
		btnSimpan.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				User user = (User)table.getItem(table.getSelectionIndex()).getData();
//				List<UserModul> userModul = UserModulProvider.INSTANCE.getUserModul(user.getIdUser());
				Control[] children = tablePrivilage.getChildren();
				int j = 0;
				boolean result = false;
				for(int i=0; i<listSubModul.size();i++){
					Button buka = (Button)children[j];
					Button simpan = (Button)children[j+1];
					Button hapus = (Button)children[j+2];
					Button cetak = (Button)children[j+3];
					UserModul userModul = new UserModul(null, user.getIdUser(), listSubModul.get(i).getIdSubModul(), buka.getSelection(), simpan.getSelection(), hapus.getSelection(), cetak.getSelection());
					result = ControllerFactory.getMainController().getCpUserDAOImpl().saveUserModul(userModul);
					j = j+5;
				}
				if(result)
					MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Penyimpanan Data berhasil dilakukan.");
				else
					MessageDialog.openError(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Ada masalah pada MERGE(UPDATE/INSERT) tabel USER_MODUL.");
			}
		});
		btnSimpan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		GridData gd_btnSimpan = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_btnSimpan.widthHint = 102;
		btnSimpan.setLayoutData(gd_btnSimpan);
		btnSimpan.setText("Simpan");
		
		scrolledComposite.setContent(composite);
		scrolledComposite.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}

	private void loadTableSubModuleForPrivilage() {
		listSubModul = SubModulProvider.INSTANCE.getSubModul();
		for(int i=0;i<listSubModul.size();i++){
			TableItem item = new TableItem(tablePrivilage, SWT.NONE);
			item.setText(listSubModul.get(i).getNamaSubModul());
			TableEditor editor = new TableEditor (tablePrivilage);
			Button checkButton = new Button(tablePrivilage, SWT.CHECK);
			checkButton.pack();
			editor.minimumWidth = checkButton.getSize ().x;
			editor.horizontalAlignment = SWT.CENTER;
			editor.setEditor(checkButton, item, 1);
			
			editor = new TableEditor(tablePrivilage);
			checkButton = new Button(tablePrivilage, SWT.CHECK);
			checkButton.pack();
			editor.minimumWidth = checkButton.getSize ().x;
			editor.horizontalAlignment = SWT.CENTER;
			editor.setEditor(checkButton, item, 2);
			
			editor = new TableEditor(tablePrivilage);
			checkButton = new Button(tablePrivilage, SWT.CHECK);
			checkButton.pack();
			editor.minimumWidth = checkButton.getSize ().x;
			editor.horizontalAlignment = SWT.CENTER;
			editor.setEditor(checkButton, item, 3);
			
			editor = new TableEditor(tablePrivilage);
			checkButton = new Button(tablePrivilage, SWT.CHECK);
			checkButton.pack();
			editor.minimumWidth = checkButton.getSize ().x;
			editor.horizontalAlignment = SWT.CENTER;
			editor.setEditor(checkButton, item, 4);
			
			editor = new TableEditor(tablePrivilage);
			checkButton = new Button(tablePrivilage, SWT.CHECK);
			checkButton.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					selectAll(e);
				}
			});
			checkButton.pack();
			editor.minimumWidth = checkButton.getSize ().x;
			editor.horizontalAlignment = SWT.CENTER;
			editor.setEditor(checkButton, item, 5);
		}
		
	}

	protected void selectAll(SelectionEvent e) {
		Button check = (Button)e.widget;
		if(check.getSelection()){
			Control[] children = tablePrivilage.getChildren();
			for(int i=1;i<=children.length;i++){
				Button btnSelectAll = (Button)children[i-1];
				if(i>0&&(i%5)==0&&btnSelectAll.getSelection()){
					for(int j=i-1;j>i-5;j--){
						Button select = (Button)children[j-1];
						select.setSelection(true);	
					}
				}
			}
		}else{
			Control[] children = tablePrivilage.getChildren();
			for(int i=1;i<=children.length;i++){
				Button btnSelectAll = (Button)children[i-1];
				if(i>0&&(i%5)==0&&!btnSelectAll.getSelection()){
					for(int j=i-1;j>i-5;j--){
						Button select = (Button)children[j-1];
						select.setSelection(false);	
					}
				}
			}
		}
	}

	public void setFocus() {
		
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