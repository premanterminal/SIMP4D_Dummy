package com.dispenda.update;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.equinox.p2.core.IProvisioningAgent;
import org.eclipse.equinox.p2.operations.UpdateOperation;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.statushandlers.StatusManager;

import com.dispenda.main.MainActivator;

/**
 * A Basic Update Job.
 * 
 * @author mahieddine.ichir@free.fr
 */
public class UpdateJob extends Job {

	private IProvisioningAgent agent;
	private IStatus result;

	/**
	 * Base constructor.
	 * @param agent Provisioning agent
	 */
	public UpdateJob(IProvisioningAgent agent) {
		super("Checking for updates");
		this.agent = agent;
	}

	@Override
	protected IStatus run(IProgressMonitor arg0) {
		result = P2Util.checkForUpdates(agent, arg0);
		if (result.getCode() == UpdateOperation.STATUS_NOTHING_TO_UPDATE) {
//			popUpInformation("No to update!");
		} else {
			installUpdates();
		}
		return result;
	}

	/**
	 * Pop up for updates installation and, if accepted, install updates.
	 */
	private void installUpdates() {
		Display.getCurrent().asyncExec(new Runnable() {
			public void run() {
				boolean install = MessageDialog.openQuestion(null, "Updates", "There is an update. Do you want to procceed?");
				if (install) {
					ProgressMonitorDialog dialog = new ProgressMonitorDialog(Display.getCurrent().getActiveShell());
					try {
						dialog.run(true, true, new IRunnableWithProgress() {
							public void run(IProgressMonitor arg0) throws InvocationTargetException,
							InterruptedException {
								P2Util.installUpdates(agent, arg0);
//								PlatformUI.getWorkbench().restart();
							}
						});
					} catch (Exception e) {
						e.printStackTrace();
						MessageDialog.openError(new Shell(), "Error", e.getMessage());
						result = new Status(Status.ERROR, MainActivator.PLUGIN_ID, "Update failed!", e);
						StatusManager.getManager().handle(result);
					}
				}
			}
		});
	}

/*	*//**
	 * Show a message dialog.
	 * @param message
	 *//*
	private void popUpInformation(final String message) {
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				MessageDialog.openInformation(null, "Updates", message);
			}
		});
	}*/
}