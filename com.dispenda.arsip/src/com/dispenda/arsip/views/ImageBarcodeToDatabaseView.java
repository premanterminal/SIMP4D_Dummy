package com.dispenda.arsip.views;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.SocketException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.nebula.widgets.grid.GridItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
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
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.SWTResourceManager;

import com.dispenda.arsip.barcode.QRCode;
import com.dispenda.controller.ControllerFactory;
import com.dispenda.model.UserModul;
import com.dispenda.object.FTPUploader;
import com.dispenda.object.GlobalVariable;
import com.dispenda.object.Preferences;
import com.dispenda.object.UploadFile;
import com.dispenda.widget.ProgressBarProvider;
import com.google.zxing.NotFoundException;

public class ImageBarcodeToDatabaseView extends ViewPart {
	public ImageBarcodeToDatabaseView() {
	}
	
	public static final String ID = ImageBarcodeToDatabaseView.class.getName();
	private Color fontColor = new Color(Display.getCurrent(),Preferences.FONT_COLOR);
	private Text text;
	protected File[] listFiles;
	private Grid grid;
	private final static Integer THUMBNAIL_WIDTH = 327;
	private final static Integer THUMBNAIL_HEIGHT = 363;
	private Composite comPreview;
	private ScrolledComposite scrolledComposite;
	private Composite composite;
	private Timestamp dateNow;
	private final static int NUM_COL = 4;
	private UserModul userModul = ControllerFactory.getMainController().getCpUserDAOImpl().getHMUserModul(GlobalVariable.userModul.getIdUser()).get(42);
	private String openedDir = "";
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	private Display display = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell().getDisplay();
	private Thread requestThread;
	private String path;
	private File dir;
	private Button btnSimpan;
	private Button btnSimpanSemua;
	
	@Override
	public void createPartControl(Composite parent) {
//		parent.setLayout(new GridLayout(1, true));
		Locale.setDefault(new Locale("id","ID"));
		dateNow = (java.sql.Timestamp)ControllerFactory.getMainController().getCpWajibPajakDAOImpl().getDateNow();
		
		scrolledComposite = new ScrolledComposite(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		composite = new Composite(scrolledComposite, SWT.NONE);
		composite.setLayout(new GridLayout(4, false));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		Label lblFolderPath = new Label(composite, SWT.NONE);
		lblFolderPath.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblFolderPath.setForeground(fontColor);
		lblFolderPath.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		lblFolderPath.setText("Folder Path :");
		
		text = new Text(composite, SWT.BORDER);
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		text.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		text.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		text.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		text.setEditable(false);
		
		Button btnBrowse = new Button(composite, SWT.NONE);
		btnBrowse.setText("Browse...");
		btnBrowse.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BORDER));
		btnBrowse.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
		btnBrowse.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				DirectoryDialog dd = new DirectoryDialog(Display.getCurrent().getActiveShell());
				path = dd.open();
				if(path!=null){
					btnSimpan.setEnabled(true);
					btnSimpanSemua.setEnabled(true);
					grid.disposeAllItems();
		    		comPreview.setBackgroundImage(null);
					dir = new File(path);
					openedDir = dir.getName();
					text.setText(dir.toString());
					ProgressBarProvider.INSTANCE.open(32 * 10 / 2);
					requestThread = new Thread(){
						public void run() {
							display.syncExec(new Runnable() {
								public void run() {
									ProgressBarProvider.INSTANCE.setProcess("Loading Images...");
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
									Image image = null;
									int imageLength = listFiles.length;
									if (listFiles.length > 32)
										imageLength = 32;
									for(int i=0; i<imageLength;i++){
										ProgressBarProvider.INSTANCE.setProcess("Loading Images...");
										j = i%NUM_COL;
										if(j==0){
											item = new GridItem(grid,SWT.NONE);
											 
											 try {
												image = new Image(Display.getCurrent(),new Image(Display.getCurrent(), new FileInputStream(listFiles[i])).getImageData().scaledTo(300, 360));
											} catch (Exception e1) {
												e1.printStackTrace();
											}
//											 item.setText(j, listFiles[i].getName());
											item.setImage(j,image);
											item.setToolTipText(j, listFiles[i].getName());
											item.setHeight(160);
										}else{
//											Image image = null;
											 try {
												image = new Image(Display.getCurrent(),new Image(Display.getCurrent(), new FileInputStream(listFiles[i])).getImageData().scaledTo(300, 360));
											} catch (Exception e1) {
												e1.printStackTrace();
												
											}
//											 item.setText(j, listFiles[i].getName());
											item.setImage(j,image);
											item.setToolTipText(j, listFiles[i].getName());
											item.setHeight(360);
										}
									}
								}
							});
							
							display.syncExec(new Runnable() {
								public void run() {
									ProgressBarProvider.INSTANCE.setProcess("Loading Done.");
								}
							});
//							image.dispose();
//							item.dispose();	
							display.asyncExec(new Runnable(){
								@Override
								public void run() {
									try {
										ProgressBarProvider.INSTANCE.setProcess("Loading Done.");
										ProgressBarProvider.INSTANCE.setEnd(5);
										requestThread.join();
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
								}
							});
						};
					};
					requestThread.start();
				}
				
			}
		});
	    
	    Composite composite_1 = new Composite(composite, SWT.NONE);
	    composite_1.setLayout(new GridLayout(3, false));
	    composite_1.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
	    
	    Button btnSelectAll = new Button(composite_1, SWT.NONE);
	    btnSelectAll.addSelectionListener(new SelectionAdapter() {
	    	@Override
	    	public void widgetSelected(SelectionEvent e) {
	    		int count = 0;
	    		int length = listFiles.length;
	    		if (listFiles.length > 32)
	    			length = 32;
	    		for(GridItem item : grid.getItems())
	    			for(int i=0;i<NUM_COL;i++){
	    				item.setChecked(i, true);
	    				count++;
	    				if (count == length)
	    					break;
	    			}
	    	}
	    });
	    GridData gd_btnSelectAll = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
	    gd_btnSelectAll.widthHint = 90;
	    btnSelectAll.setLayoutData(gd_btnSelectAll);
	    btnSelectAll.setText("Check");
	    btnSelectAll.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BORDER));
	    btnSelectAll.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
	    
	    Button btnDeselectAll = new Button(composite_1, SWT.NONE);
	    GridData gd_btnDeselectAll = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
	    gd_btnDeselectAll.widthHint = 90;
	    btnDeselectAll.setLayoutData(gd_btnDeselectAll);
	    btnDeselectAll.setText("Uncheck");
	    btnDeselectAll.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BORDER));
	    btnDeselectAll.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
	    btnDeselectAll.addSelectionListener(new SelectionAdapter() {
	    	@Override
	    	public void widgetSelected(SelectionEvent e) {
	    		for(GridItem item : grid.getItems())
	    			for(int i=0;i<NUM_COL;i++)
	    				item.setChecked(i, false);
	    	}
		});
	    
	    Button btnDispose = new Button(composite_1, SWT.NONE);
	    GridData gd_btnDispose = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
	    gd_btnDispose.widthHint = 64;
	    btnDispose.setLayoutData(gd_btnDispose);
	    btnDispose.addSelectionListener(new SelectionAdapter() {
	    	@Override
	    	public void widgetSelected(SelectionEvent e) {
	    		grid.disposeAllItems();
	    		comPreview.setBackgroundImage(null);
	    		btnSimpan.setEnabled(false);
	    		btnSimpanSemua.setEnabled(false);
//	    		display.getActiveShell().close();
//	    		System.gc();
//	    		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().resetPerspective();
	    	}
	    });
	    btnDispose.setText("Reset");
	    btnDispose.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BORDER));
	    btnDispose.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
	    
	    Composite composite_2 = new Composite(composite, SWT.NONE);
	    composite_2.setLayout(new GridLayout(2, false));
	    
	    btnSimpan = new Button(composite_2, SWT.NONE);
	    btnSimpan.setEnabled(false);
	    btnSimpan.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
	    btnSimpan.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BORDER));
	    GridData gd_btnSimpan = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
	    gd_btnSimpan.widthHint = 90;
	    btnSimpan.setLayoutData(gd_btnSimpan);
	    btnSimpan.setText("Simpan ");
	    btnSimpan.addSelectionListener(new SelectionAdapter() {
			@Override
	    	public void widgetSelected(SelectionEvent e) {
	    		// open progress bar here
				ProgressBarProvider.INSTANCE.open(32 * 10 /2);
				// run thread
				requestThread = new Thread(){
					public void run() {
						/*display.syncExec(new Runnable() {
							public void run() {
								ProgressBarProvider.INSTANCE.setProcess("Get Image from Local Computer");
							}
						});*/
//						input = new InputTreeRekap().getList(masaPajakDari, masaPajakSampai, selection);
						display.syncExec(new Runnable() {
							public void run() {
								ProgressBarProvider.INSTANCE.setProcess("Start Uploading Image to Server..");
								startUpload();
								ProgressBarProvider.INSTANCE.setProcess("Done loading..");
							}
						});
						// stop progress bar here
						display.asyncExec(new Runnable(){
							@Override
							public void run() {
								try {
									ProgressBarProvider.INSTANCE.setEnd(4);
									requestThread.join();
									MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Upload telah selesai. Silahkan lihat log di Direktori D:\\LogArsip\\LogArsip(" + openedDir + ")-" + sdf.format(dateNow) + ".txt");
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}
						});
					};
				};
				requestThread.start();
	    	}
		});
	    
	    btnSimpanSemua = new Button(composite_2, SWT.NONE);
	    btnSimpanSemua.setEnabled(false);
	    btnSimpanSemua.setFont(SWTResourceManager.getFont(Preferences.FONT_STYLE, Preferences.FONT_SIZE, SWT.NORMAL));
	    btnSimpanSemua.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BORDER));
	    btnSimpanSemua.addSelectionListener(new SelectionAdapter() {
	    	@Override
	    	public void widgetSelected(SelectionEvent e) {
	    		// open progress bar here
				ProgressBarProvider.INSTANCE.open(listFiles.length * 10 / 2);
				// run thread
				requestThread = new Thread(){
					public void run() {
						/*display.syncExec(new Runnable() {
							public void run() {
								ProgressBarProvider.INSTANCE.setProcess("Start Uploading Image to Server..");
							}
						});*/
//						input = new InputTreeRekap().getList(masaPajakDari, masaPajakSampai, selection);
						display.syncExec(new Runnable() {
							public void run() {
								ProgressBarProvider.INSTANCE.setProcess("Start Uploading Image to Server..");
								startUploadAll();
								ProgressBarProvider.INSTANCE.setProcess("Done loading..");
							}
						});
						// stop progress bar here
						display.asyncExec(new Runnable(){
							@Override
							public void run() {
								try {
									ProgressBarProvider.INSTANCE.setEnd(4);
									requestThread.join();
									MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Upload telah selesai. Silahkan lihat log di Direktori D:\\LogArsip\\LogArsip(" + openedDir + ")-" + sdf.format(dateNow) + ".txt");
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}
						});
					};
				};
				requestThread.start();
	    	}
	    });
	    btnSimpanSemua.setText("Simpan Semua");
	    new Label(composite, SWT.NONE);
		
	    grid = new Grid(composite,SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
	    grid.setCellSelectionEnabled(true);
	    grid.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));
	    
	    comPreview = new Composite(composite, SWT.NONE);
	    GridData gd_comPreview = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
	    gd_comPreview.heightHint = 680;
	    gd_comPreview.widthHint = 600;
	    comPreview.setLayoutData(gd_comPreview);
	    
	    
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
		
	    scrolledComposite.setContent(composite);
		scrolledComposite.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		
	}
	
	private String setDirectory(String noSkp){
		String result = "";
		if (noSkp.contains("SPTPD/") || noSkp.contains("SKPD/"))
			result = "SPTPD";
		else if (noSkp.contains("SKPDKB") || noSkp.contains("SKPDN"))
			result = "SKPDKB";
		else if (noSkp.contains("SSPD"))
			result = "SSPD";
		
		String[] skpSplit = noSkp.split("/");
		result += "/" + skpSplit[skpSplit.length-1] + "/";
		return result;
	}
	
	private String setFilename(String namaFile, String direktori, int count){
		if (UploadFile.checkFile(direktori+namaFile, count)){
			count++;
			return setFilename(namaFile, direktori, count);
		}else{
			if (count > 0)
				return namaFile + "(" + count + ")";
			else
				return namaFile;
		}
//		return retValue;
	}
	
	private void startUpload(){
		String direktori = "";
		String logMessage = "";
		int checkedCount = 1;
		int itemCount = 0;
		if (isSave()){
			try {
				UploadFile.openFTP();
			} catch (SocketException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
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
    					if(result==null){
    						logMessage += checkedCount + ". File path: " + listFiles[selectionIndex].getAbsolutePath() + " gagal di scan."+System.lineSeparator();
    					}else{
    						try {
    							direktori = setDirectory(result);
    							String namaFile = setFilename(result.replace("/", "-"), direktori, 0);
								logMessage += UploadFile.upload(listFiles[selectionIndex], namaFile, direktori, checkedCount);
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
								display.syncExec(new Runnable(){
									@Override
									public void run() {
										ProgressBarProvider.INSTANCE.setEnd(4);
									}
								});
								MessageDialog.openError(display.getActiveShell(), "BPPRD Kota Medan", "Upload Berkas gagal");
								break;
							}
    					}
    					ProgressBarProvider.INSTANCE.setProcess("Uploading Image : "+listFiles[selectionIndex].getName());
    					checkedCount++;
    				}
    			itemCount++;
    		}
    		UploadFile.closeFTP();
    		try {
    			dateNow = (java.sql.Timestamp)ControllerFactory.getMainController().getCpWajibPajakDAOImpl().getDateNow();
                File logText = new File("D:\\LogArsip\\LogArsip(" + openedDir + ")-" + sdf.format(dateNow) + ".txt");
                logText.getParentFile().mkdirs();
                logText.createNewFile();
                FileOutputStream is = new FileOutputStream(logText, false);
                OutputStreamWriter osw = new OutputStreamWriter(is);    
                Writer w = new BufferedWriter(osw);
                w.write(logMessage);
                w.close();
            } catch (Exception ex) {
                System.err.println("Problem writing to the file LogArsip.txt");
            }
//    		MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "Upload Arsip", "Upload telah selesai. Silahkan lihat log di Direktori D:\\LogArsip\\LogArsip(" + openedDir + ")-" + sdf.format(dateNow) + ".txt");
		}else
			MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Anda tidak memiliki hak untuk mengupload berkas arsip.");
	}
	
	private void startUploadAll(){
		String direktori = "";
		String logMessage = "";
		int checkedCount = 1;
		if (isSave()){
			try {
				UploadFile.openFTP();
			} catch (SocketException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
    		for(int i=0;i<listFiles.length;i++){
				final int count = i;
    			String result = null;
    			try {
					result = QRCode.readQRCode(listFiles[i].getPath());
					/*display.syncExec(new Runnable() {
						public void run() {
							ProgressBarProvider.INSTANCE.setProcess("Reading QRCode : "+listFiles[count].getName());
						}
					});*/
				} catch (NotFoundException	| IOException e1) {
					e1.printStackTrace();
				}
    			if(result==null){
    				logMessage += checkedCount + ". File path: " + listFiles[i].getAbsolutePath() + " gagal di scan."+System.lineSeparator();
    			}else{
    				try {
    					direktori = setDirectory(result);
    					String namaFile = setFilename(result.replace("/", "-"), direktori, 0);
						logMessage += UploadFile.upload(listFiles[i], namaFile, direktori, checkedCount);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						display.syncExec(new Runnable(){
							@Override
							public void run() {
								ProgressBarProvider.INSTANCE.setEnd(4);
							}
						});
						MessageDialog.openError(display.getActiveShell(), "BPPRD Kota Medan", "Upload Berkas gagal");
						break;
					}
    			}
    			ProgressBarProvider.INSTANCE.setProcess("Uploading Image : "+listFiles[count].getName());
    			/*display.syncExec(new Runnable() {
					public void run() {
						ProgressBarProvider.INSTANCE.setProcess("Uploading Image : "+listFiles[count].getName());
					}
				});*/
    			checkedCount++;
    		}
    		UploadFile.closeFTP();
    		try {
    			/*display.syncExec(new Runnable() {
					public void run() {
						ProgressBarProvider.INSTANCE.setProcess("Writing log file...");
					}
				});*/
    			dateNow = (java.sql.Timestamp)ControllerFactory.getMainController().getCpWajibPajakDAOImpl().getDateNow();
                File logText = new File("D:\\LogArsip\\LogArsip(" + openedDir + ")-" + sdf.format(dateNow) + ".txt");
                logText.getParentFile().mkdirs();
                logText.createNewFile();
                FileOutputStream is = new FileOutputStream(logText, false);
                OutputStreamWriter osw = new OutputStreamWriter(is);    
                Writer w = new BufferedWriter(osw);
                w.write(logMessage);
                w.close();
                
            } catch (Exception ex) {
                System.err.println("Problem writing to the file LogArsip.txt");
            }
		}else
			MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Anda tidak memiliki hak untuk mengupload berkas arsip.");
	}
	
	private boolean isSave(){		
		return userModul.getSimpan();
	}
}
