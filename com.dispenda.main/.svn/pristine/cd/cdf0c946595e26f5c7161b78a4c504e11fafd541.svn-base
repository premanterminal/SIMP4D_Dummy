package com.dispenda.main;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.equinox.p2.core.IProvisioningAgent;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import com.dispenda.database.DBConnection;
import com.dispenda.dialog.LoginDialog;
import com.dispenda.update.P2Util;
import com.dispenda.update.UpdateJob;

/**
 * This class controls all aspects of the application's execution
 */
public class Application implements IApplication {

	/* (non-Javadoc)
	 * @see org.eclipse.equinox.app.IApplication#start(org.eclipse.equinox.app.IApplicationContext)
	 */
	public Object start(IApplicationContext context) {
		Display display = PlatformUI.createDisplay();
		try {
			if (authenticate()) {
				DBConnection.INSTANCE.setDisplayName(MainActivator.getDefault().getDisplayName());
				/*BundleContext bundleContext = MainActivator.getDefault().getBundle().getBundleContext();
				ServiceReference<IProvisioningAgent> serviceReference =   bundleContext.getServiceReference(IProvisioningAgent.class);
				IProvisioningAgent agent = bundleContext.getService(serviceReference);
					if (agent == null) {
						System.out.println(">> no agent loaded!");
//				        return;
				    }
				    // Adding the repositories to explore
				    if (! P2Util.addRepository(agent, "http://172.16.72.222/p2/repository")) {
//				    if (! P2Util.addRepository(agent, "http://localhost/p2/repository")) {
				        System.out.println(">> could no add repostory!");
//				        return;
				    }
				    // scheduling job for updates
				UpdateJob updateJob = new UpdateJob(agent);
				updateJob.schedule();*/
				int returnCode = PlatformUI.createAndRunWorkbench(display, new ApplicationWorkbenchAdvisor());
				if (returnCode == PlatformUI.RETURN_RESTART)
					return IApplication.EXIT_RESTART;
				else
					return IApplication.EXIT_OK;
			}
				
			return IApplication.EXIT_OK;
		} finally {
			display.dispose();
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.equinox.app.IApplication#stop()
	 */
	public void stop() {
		if (!PlatformUI.isWorkbenchRunning())
			return;
		final IWorkbench workbench = PlatformUI.getWorkbench();
		final Display display = workbench.getDisplay();
		display.syncExec(new Runnable() {
			public void run() {
				if (!display.isDisposed())
					workbench.close();
			}
		});
	}
	
	private boolean authenticate() {
		Shell shell = new Shell();
		LoginDialog loginDialog = new LoginDialog(shell);
		int rs = loginDialog.open();
		if(rs==1){
//			shell.dispose();
			return false;
		}else{
			shell.dispose();
			return true;
		}
	}
}
