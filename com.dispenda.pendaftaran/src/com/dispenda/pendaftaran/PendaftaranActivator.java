package com.dispenda.pendaftaran;


import java.sql.Connection;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;


/**
 * The activator class controls the plug-in life cycle
 */
public class PendaftaranActivator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.dispenda.pendaftaran"; //$NON-NLS-1$

	// The shared instance
	private static PendaftaranActivator plugin;
	private Connection con;
	/**
	 * The constructor
	 */
	public PendaftaranActivator() {
//		setConnection();
	}
	
/*	public void setConnection() {
		DBConnection connect = new DBConnection();
		try{
			con = connect.ConnectToDb();
		} catch (Exception e){
			e.printStackTrace();
		}
	}*/
	
	public Connection getConnection(){
		return con;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static PendaftaranActivator getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}
}
