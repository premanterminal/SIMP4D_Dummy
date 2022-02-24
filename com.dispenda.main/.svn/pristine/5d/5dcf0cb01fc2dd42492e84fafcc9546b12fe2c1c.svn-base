package com.dispenda.main;

import org.eclipse.equinox.p2.core.IProvisioningAgent;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import com.dispenda.perspectives.MainPerspective;
import com.dispenda.update.P2Util;
import com.dispenda.update.UpdateJob;

/**
 * This workbench advisor creates the window advisor, and specifies
 * the perspective id for the initial window.
 */
public class ApplicationWorkbenchAdvisor extends WorkbenchAdvisor {
	
    public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
        return new ApplicationWorkbenchWindowAdvisor(configurer);
    }

	public String getInitialWindowPerspectiveId() {
		return MainPerspective.ID;
	} 
	
	@Override
	public void preStartup() {
		/*BundleContext bundleContext = MainActivator.getDefault().getBundle().getBundleContext();
		ServiceReference<IProvisioningAgent> serviceReference =   bundleContext.getServiceReference(IProvisioningAgent.class);
		IProvisioningAgent agent = bundleContext.getService(serviceReference);
		if (agent == null) {
			System.out.println(">> no agent loaded!");
		    return;
		}
			    // Adding the repositories to explore
		if (! P2Util.addRepository(agent, "http://192.168.1.2/p2/repository")) {
//			    if (! P2Util.addRepository(agent, "http://localhost/p2/repository")) {
//		    System.out.println(">> could no add repostory!");
		    return;
		}
			    // scheduling job for updates
		P2Util.addRepository(agent, "http://192.168.1.2/p2/repository");
		UpdateJob updateJob = new UpdateJob(agent);
		updateJob.schedule();*/
//		P2Util.checkForUpdates();
	}
}
