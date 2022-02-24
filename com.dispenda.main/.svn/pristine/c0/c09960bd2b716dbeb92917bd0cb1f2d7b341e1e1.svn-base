package com.dispenda.main;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.equinox.internal.p2.core.helpers.LogHelper;
import org.eclipse.equinox.internal.p2.core.helpers.ServiceHelper;
import org.eclipse.equinox.p2.core.IProvisioningAgent;
import org.eclipse.equinox.p2.operations.UpdateOperation;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

import com.dispenda.object.OpenPreferenceFile;
import com.dispenda.object.Preferences;
import com.dispenda.update.P2Util;

public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {

	private static final String JUSTUPDATED = "justUpdated";
	private static final Point DEFAULT_SIZE = new Point( 1024, 768 );
	
    public ApplicationWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
        super(configurer);
    }

    public ActionBarAdvisor createActionBarAdvisor(IActionBarConfigurer configurer) {
        return new ApplicationActionBarAdvisor(configurer);
    }
    
    public void preWindowOpen() {
        IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
        configurer.setInitialSize( getDisplaySize() );
        configurer.setShowPerspectiveBar(false);
        configurer.setShowCoolBar(false);
        configurer.setShowStatusLine(false);
        configurer.setShowFastViewBars(false);
        configurer.setShowProgressIndicator(false);
        configurer.setShowMenuBar(false);
        
        configurer.setShowStatusLine(false);
        configurer.setShowProgressIndicator(false);
        configurer.setShellStyle(SWT.NO_TRIM);
        new OpenPreferenceFile().readFile();
  
    }
    
    public void postWindowOpen() {
    	getWindowConfigurer().getWindow().getShell().setBackground(new Color(Display.getCurrent(), Preferences.BACKGROUND_COLOR));
		getWindowConfigurer().getWindow().getShell().setBackgroundMode(SWT.INHERIT_FORCE);
		if(!Preferences.BACKGROUND_IMAGE.equalsIgnoreCase(" "))
			getWindowConfigurer().getWindow().getShell().setBackgroundImage(new Image(Display.getCurrent(),new Image(Display.getCurrent(), Preferences.BACKGROUND_IMAGE).getImageData().scaledTo(getDisplaySize().x, getDisplaySize().y)));
		
		/*final IProvisioningAgent agent = (IProvisioningAgent) ServiceHelper.getService(MainActivator.getDefault().getBundle().getBundleContext(),
						IProvisioningAgent.SERVICE_NAME);
		if (agent == null) {
			LogHelper
					.log(new Status(IStatus.ERROR, MainActivator.PLUGIN_ID,
							"No provisioning agent found.  This application is not set up for updates."));
		}
		// XXX if we're restarting after updating, don't check again.
		final IPreferenceStore prefStore = MainActivator.getDefault().getPreferenceStore();
		if (prefStore.getBoolean(JUSTUPDATED)) {
			prefStore.setValue(JUSTUPDATED, false);
			return;
		}

		// XXX check for updates before starting up.
		// If an update is performed, restart. Otherwise log
		// the status.
		IRunnableWithProgress runnable = new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor)throws InvocationTargetException, InterruptedException  {

				IStatus updateStatus = P2Util.checkForUpdates(agent, monitor);
				if (updateStatus.getCode() == UpdateOperation.STATUS_NOTHING_TO_UPDATE) {
					PlatformUI.getWorkbench().getDisplay()
							.asyncExec(new Runnable() {
								public void run() {
									MessageDialog.openInformation(null,
											"Updates", "No updates were found");
								}
							});
				} else if (updateStatus.getSeverity() != IStatus.ERROR) {
					prefStore.setValue(JUSTUPDATED, true);
					PlatformUI.getWorkbench().restart();
				} else {
					LogHelper.log(updateStatus);
				}
			}
		};
		try {
			new ProgressMonitorDialog(null).run(true, true, runnable);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
		}*/
//    	super.postWindowOpen();
    	
    }
    
	private Point getDisplaySize() {
	    try {
	        Display display = Display.getCurrent();
	        Monitor monitor = display.getPrimaryMonitor();
	        Rectangle rect = monitor.getBounds();
	        return new Point( rect.width, rect.height );
	    }
	    catch ( Throwable ignore ) {
	        return DEFAULT_SIZE;
	    }
	}   
}