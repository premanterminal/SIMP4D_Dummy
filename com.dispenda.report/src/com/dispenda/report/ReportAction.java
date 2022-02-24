package com.dispenda.report;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import org.eclipse.birt.report.viewer.ViewerPlugin;
import org.eclipse.birt.report.viewer.browsers.BrowserManager;
import org.eclipse.birt.report.viewer.utilities.WebViewer;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.OpenWindowListener;
import org.eclipse.swt.browser.WindowEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.osgi.framework.Bundle;

import com.dispenda.dialog.PrintChooserDialog;

public class ReportAction {

	public ReportAction() {
	}

	@SuppressWarnings({ "deprecation", "rawtypes", "unchecked" })
	public static void start(String reportName){
		String path = "";
		ViewerPlugin.getDefault( ).getPluginPreferences( ).setValue("APPCONTEXT_EXTENSION_KEY", "ReportAppContext");
	    try {
	      Bundle bundle = Platform.getBundle("com.dispenda.report");
	      URL url = FileLocator.find(bundle, new Path("/reports/"+reportName), null);
	      path = FileLocator.toFileURL(url).getPath();
	    } catch (MalformedURLException me) {
	      System.out.println("Invalid URL " + me.getStackTrace());
	    } catch (IOException e1) {
	      e1.printStackTrace();
	    }
	    Shell shell = new Shell();
	    shell.setText("Report");
		shell.setLayout(new GridLayout(1, false));
	    shell.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
	    shell.setMaximized(true);
	
	    BrowserManager.getInstance().setAlwaysUseExternal(true);
	    Browser browser = new Browser(shell, SWT.NONE);
		browser.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		browser.addOpenWindowListener( new OpenWindowListener( ) {
			public void open( final WindowEvent event ){
				final Shell shell = new Shell(Display.getCurrent().getActiveShell(), SWT.SHELL_TRIM | Window.getDefaultOrientation( ) );
				shell.setLayout( new FillLayout( ) );
				Browser browser = new Browser( shell, SWT.NONE );
				event.browser = browser;
				shell.setMaximized(true);
				shell.open( );
			}
		} );
		
		PrintChooserDialog printDialog = new PrintChooserDialog(Display.getCurrent().getActiveShell());
		printDialog.open();
		
		
	    HashMap myparms = new HashMap();
		myparms.put("SERVLET_NAME_KEY", "frameset");
		myparms.put("FORMAT_KEY", printDialog.getResult());
		myparms.put("ALLOW_PAGE", true);
//		myparms.put("MAX_ROWS_KEY", "30");
		
		 //external browser preference setting
//		 boolean useExternal = ViewerPlugin.getDefault( ).getPluginPreferences( ).getBoolean( BrowserManager.getInstance().ALWAYS_EXTERNAL_BROWSER_KEY );
		if(!printDialog.getResult().equalsIgnoreCase("")){
			WebViewer.display(path, browser, myparms);
			shell.open();
		}
	}
	
	
}
