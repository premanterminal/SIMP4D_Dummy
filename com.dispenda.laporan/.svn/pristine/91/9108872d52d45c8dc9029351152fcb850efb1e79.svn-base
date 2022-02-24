package com.dispenda.laporan.views;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class CheckHiburanExcel {
	private static Text txtUdin;
	private static Text txtSistem;

	public static void main(String[] args) {
		final Shell shell = new Shell();
		shell.setSize(300, 250);
		shell.setLayout(new GridLayout(3, false));
		
		Label lblFile = new Label(shell, SWT.NONE);
		lblFile.setText("File Udin:");
		
		txtUdin = new Text(shell, SWT.BORDER);
		txtUdin.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Button btnOpenUdin = new Button(shell, SWT.NONE);
		btnOpenUdin.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog fd = new FileDialog(Display.getCurrent().getActiveShell(), SWT.NONE);
				txtUdin.setText(fd.open());
			}
		});
		btnOpenUdin.setText("Open..");
		
		Label lblFileSistem = new Label(shell, SWT.NONE);
		lblFileSistem.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblFileSistem.setText("File Sistem:");
		
		txtSistem = new Text(shell, SWT.BORDER);
		txtSistem.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Button btnOpenSistem = new Button(shell, SWT.NONE);
		btnOpenSistem.setText("Open..");
		btnOpenSistem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog fd = new FileDialog(Display.getCurrent().getActiveShell(), SWT.NONE);
				txtSistem.setText(fd.open());
			}
		});
		
		Composite composite = new Composite(shell, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1));
		composite.setLayout(new GridLayout(1, false));
		
		Button btnCheck = new Button(composite, SWT.NONE);
		btnCheck.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					PrintWriter writer = new PrintWriter("C:/ReklameUdinVsSistem.txt", "UTF-8");
					PrintWriter writer2 = new PrintWriter("C:/ReklameUdinVsSistemBeda.txt", "UTF-8");
					Workbook wbUdin = Workbook.getWorkbook(new File(txtUdin.getText()));
					Workbook wbSistem = Workbook.getWorkbook(new File(txtSistem.getText()));
					Sheet shUdin = wbUdin.getSheet(1);
					Sheet shSistem = wbSistem.getSheet(0);
					String noSKPUdin = "",pokokUdin="",pokokSistem="";
					boolean check = true;
					/*for(int i=9; i<=4011;i++){
						Cell cellUdin = shUdin.getCell(8, i);
						for(int j=3; j <= 3964; j++){
							Cell cellSistem = shSistem.getCell(11, j);
							if(cellUdin.getContents().equalsIgnoreCase(cellSistem.getContents())){
								if(shUdin.getCell(13, i).getContents().equalsIgnoreCase(shSistem.getCell(12, j).getContents())){
									writer.println(cellUdin.getContents()+" "+shUdin.getCell(13, i).getContents()+" "+shSistem.getCell(12, j).getContents()+"\n");
								}else{
									writer2.println(cellUdin.getContents()+" "+shUdin.getCell(13, i).getContents()+" "+shSistem.getCell(12, j).getContents()+"\n");
								}
								pokokSistem = shSistem.getCell(12, j).getContents();
								check = true;
								break;	
							}
						}
						if(!check){
							writer2.println(cellUdin.getContents()+" "+shUdin.getCell(13, i).getContents()+" "+pokokSistem+"\n");
						}
						
						check = false;
					}*/
					for(int j=3; j <= 3964; j++){
						Cell cellSistem = shSistem.getCell(11, j);
						for(int i=9; i<=3971;i++){
							Cell cellUdin = shUdin.getCell(8, i);
							if(cellUdin.getContents().equalsIgnoreCase(cellSistem.getContents())){
								if(shUdin.getCell(13, i).getContents().equalsIgnoreCase(shSistem.getCell(12, j).getContents())){
									writer.println(cellUdin.getContents()+" "+shUdin.getCell(13, i).getContents()+" "+shSistem.getCell(12, j).getContents()+"\n");
								}else{
									writer2.println(cellUdin.getContents()+" "+shUdin.getCell(13, i).getContents()+" "+shSistem.getCell(12, j).getContents()+"\n");
									check = false;
								}
								pokokSistem = shUdin.getCell(13, i).getContents();
								break;
							}
						}
						if(!check){
							writer2.println(cellSistem.getContents()+" "+shSistem.getCell(12, j).getContents()+" "+pokokSistem+"\n");
							check = true;
						}
					}
					
					wbUdin.close();
					wbSistem.close();
					writer.close();
					writer2.close();
				} catch (BiffException b) {
					b.printStackTrace();
				} catch (IOException c) {
					c.printStackTrace();
				} 
				System.exit(0);
			}
		});
		btnCheck.setText("Check..");
	    shell.open();
	    
	    
	    
	    Display display = Display.getDefault();
	    while (!shell.isDisposed()) {
	      if (!display.readAndDispatch())
	        display.sleep();
	    }
	}
}
