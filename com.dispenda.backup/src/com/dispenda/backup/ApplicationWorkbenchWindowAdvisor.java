package com.dispenda.backup;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

import com.dispenda.database.DBConnection;
import com.dispenda.database.DBOperation;

public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {

    private String time;

	public ApplicationWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
        super(configurer);
    }

    public ActionBarAdvisor createActionBarAdvisor(IActionBarConfigurer configurer) {
        return new ApplicationActionBarAdvisor(configurer);
    }
    
    public void preWindowOpen() {
//    	DBConnection.INSTANCE.open();
    	Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				ProgressMonitorDialog dialog = new ProgressMonitorDialog(new Shell());
				try {
					dialog.run(true, true, new IRunnableWithProgress() {
						
						@Override
						public void run(IProgressMonitor monitor) throws InvocationTargetException,
								InterruptedException {
							// TODO Auto-generated method stub
							monitor.beginTask("Creating Backup", 3);
							backup();
							monitor.worked(1);
							monitor.subTask("Send to server..");
							sendToSite();
							monitor.worked(2);
							monitor.done();
//							getWindowConfigurer().getWindow().getShell().close();
							System.exit(0);
						}
					});
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
    	
        IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
        configurer.setInitialSize(new Point(0, 0));
        configurer.setShowCoolBar(false);
        configurer.setShowStatusLine(false);
        configurer.setTitle("Hello RCP"); //$NON-NLS-1$
//        configurer.getWindow().getShell().setVisible(false);
//        System.exit(0);
    }
    
    private void backup(){
    	SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhhmmss");
    	time = format.format(new Date());
    	String query = "BACKUP DATABASE TO 'C:\\Backup\\"+ time +".tgz' BLOCKING;";
    	DBOperation db = new DBOperation();
		db.ExecuteStatementQuery(query, DBConnection.INSTANCE.open());
    }
    
    private void sendToSite(){
    	String server = "code.mataniari.asia";
        int port = 21;
        String user = "adminmataniari";
        String pass = "matahariterang";
 
        FTPClient ftpClient = new FTPClient();
        try {
 
            ftpClient.connect(server, port);
            ftpClient.login(user, pass);
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
 
            // APPROACH #1: using retrieveFile(String, OutputStream)
            String filename = "/backup/" + time + ".tgz";
            File uploadFile1 = new File("C:/Backup/" + time + ".tgz");
            FileInputStream fis = new FileInputStream(uploadFile1);
            boolean success = ftpClient.storeFile(filename, fis);
            
            fis.close();
 
            if (success) {
                System.out.println("File has been uploaded successfully.");
            }
            else
            	System.out.println("Failed upload File.");
 
 
        } catch (IOException ex) {
            System.out.println("Error: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            try {
                if (ftpClient.isConnected()) {
                    ftpClient.logout();
                    ftpClient.disconnect();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
