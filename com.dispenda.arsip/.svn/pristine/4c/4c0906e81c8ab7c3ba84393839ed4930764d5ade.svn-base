package com.dispenda.arsip.views;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;

import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.nebula.widgets.grid.GridItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import com.dispenda.arsip.barcode.QRCode;
import com.dispenda.object.Preferences;
import com.google.zxing.NotFoundException;

/*
 * Create a grid with a checkbox in the second column.
 *
 * For a list of all Nebula Grid example snippets see
 * http://www.eclipse.org/nebula/widgets/grid/snippets.php
 */
public class GridSnippet3 {

private static Text text;
protected static File[] listFiles;
private static Grid grid;
private static Composite comPreview;
private final static int NUM_COL = 4; 

public static void main (String [] args) throws FileNotFoundException {
    Display display = new Display ();
    Shell shell = new Shell (display);
    shell.setLayout(new GridLayout(1, true));

    Composite composite = new Composite(shell, SWT.NONE);
	composite.setLayout(new GridLayout(3, false));
	composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
	
	Label lblFolderPath = new Label(composite, SWT.NONE);
	lblFolderPath.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
	lblFolderPath.setText("Folder Path :");
	
	text = new Text(composite, SWT.BORDER);
	text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
	
	Button btnBrowse = new Button(composite, SWT.NONE);
	btnBrowse.setText("Browse...");
	btnBrowse.addSelectionListener(new SelectionAdapter() {
		@Override
		public void widgetSelected(SelectionEvent e) {
			DirectoryDialog dd = new DirectoryDialog(Display.getCurrent().getActiveShell());
			String path = dd.open();
			if(path!=null){
				File dir = new File(path);
				text.setText(dir.toString());
				FilenameFilter textFilter = new FilenameFilter() {
					@Override
					public boolean accept(File dir, String name) {
						if(name.endsWith(".jpg")){
							return true;
						}else
							return false;
					}
				};
				listFiles = dir.listFiles(textFilter);
				int j = 0;
				GridItem item = null;
				for(int i=0; i<listFiles.length;i++){
					j = i%NUM_COL;
					if(j==0){
						item = new GridItem(grid,SWT.NONE);
						 Image image = null;
						 try {
							image = new Image(Display.getCurrent(),new Image(Display.getCurrent(), new FileInputStream(listFiles[i])).getImageData().scaledTo(300, 360));
						} catch (FileNotFoundException e1) {
							e1.printStackTrace();
						}
//						 item.setText(j, listFiles[i].getName());
						item.setImage(j,image);
						item.setToolTipText(j, listFiles[i].getName());
						
						item.setHeight(160);
					}else{
						Image image = null;
						 try {
							image = new Image(Display.getCurrent(),new Image(Display.getCurrent(), new FileInputStream(listFiles[i])).getImageData().scaledTo(300, 360));
						} catch (FileNotFoundException e1) {
							e1.printStackTrace();
						}
//						 item.setText(j, listFiles[i].getName());
						item.setImage(j,image);
						item.setToolTipText(j, listFiles[i].getName());
						
						item.setHeight(360);
					}
				}
			}
		}
	});
    
    Composite composite_1 = new Composite(composite, SWT.NONE);
    composite_1.setLayout(new GridLayout(2, false));
    composite_1.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1));
    
    Button btnSelectAll = new Button(composite_1, SWT.NONE);
    btnSelectAll.addSelectionListener(new SelectionAdapter() {
    	@Override
    	public void widgetSelected(SelectionEvent e) {
    		for(GridItem item : grid.getItems())
    			for(int i=0;i<NUM_COL;i++)
    				item.setChecked(i, true);
    	}
    });
    GridData gd_btnSelectAll = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
    gd_btnSelectAll.widthHint = 90;
    btnSelectAll.setLayoutData(gd_btnSelectAll);
    btnSelectAll.setText("Select All..");
    
    Button btnDeselectAll = new Button(composite_1, SWT.NONE);
    GridData gd_btnDeselectAll = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
    gd_btnDeselectAll.widthHint = 90;
    btnDeselectAll.setLayoutData(gd_btnDeselectAll);
    btnDeselectAll.setText("Deselect All..");
    btnDeselectAll.addSelectionListener(new SelectionAdapter() {
    	@Override
    	public void widgetSelected(SelectionEvent e) {
    		for(GridItem item : grid.getItems())
    			for(int i=0;i<NUM_COL;i++)
    				item.setChecked(i, false);
    	}
	});
	
    grid = new Grid(composite,SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
    grid.setCellSelectionEnabled(true);
    grid.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
    
    comPreview = new Composite(composite, SWT.NONE);
    GridData gd_comPreview = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
    gd_comPreview.heightHint = 680;
    gd_comPreview.widthHint = 600;
    comPreview.setLayoutData(gd_comPreview);
    
    Composite composite_2 = new Composite(composite, SWT.NONE);
    composite_2.setLayout(new GridLayout(1, false));
    composite_2.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 3, 1));
    
    Button btnSimpan = new Button(composite_2, SWT.NONE);
    GridData gd_btnSimpan = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
    gd_btnSimpan.widthHint = 90;
    btnSimpan.setLayoutData(gd_btnSimpan);
    btnSimpan.setText("Simpan...");
    btnSimpan.addSelectionListener(new SelectionAdapter() {
    	@Override
    	public void widgetSelected(SelectionEvent e) {
    		int itemCount = 0;
    		for(GridItem item : grid.getItems()){
    			for(int i=0;i<NUM_COL;i++)
    				if(item.getChecked(i)){
    					int selectionIndex = i + (itemCount*NUM_COL);
    					String result = null;
    					try {
							result = QRCode.readQRCode(listFiles[selectionIndex].getPath());
						} catch (NotFoundException	| IOException e1) {
							e1.printStackTrace();
						}
    					if(result==null)
    						System.out.println(listFiles[selectionIndex].getName());
    					else
    						System.out.println(result);
    				}
    			itemCount++;
    		}
    	}
	});
    
    
    for(int i=0; i<NUM_COL; i++){
    	GridColumn column2 = new GridColumn(grid,SWT.CHECK | SWT.CENTER);
        column2.setWidth(300);
    }
    grid.addSelectionListener(new SelectionAdapter() {
		@Override
    	public void widgetSelected(SelectionEvent e) {
    		Grid widget = (Grid)e.widget;
    		Point[] point = widget.getCellSelection(); // x = column, y = row, starts from zero
    		if(point.length>0){
	    		int selectionIndex = point[0].x + (point[0].y*NUM_COL);
	    		try {
					comPreview.setBackgroundImage(new Image(Display.getCurrent(),new Image(Display.getCurrent(), new FileInputStream(listFiles[selectionIndex])).getImageData().scaledTo(600, 680)));
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
    		}
    	}
	});
    
    shell.setSize(777,902);
    shell.open ();
    while (!shell.isDisposed()) {
        if (!display.readAndDispatch ()) display.sleep ();
    }
    display.dispose ();
}
} 
