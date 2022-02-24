package com.dispenda.widget;

import org.eclipse.swt.widgets.Display;

public enum ProgressBarProvider {
	INSTANCE;
	
	private ProgressBarDialog pbd;
	
	public void open(int steps){
		pbd = new ProgressBarDialog(Display.getCurrent().getActiveShell());
		pbd.setShellTitle("Mohon Tunggu..");
		pbd.setProcessBarStyle(true);
		pbd.setProcessMessage("Retreiving Data..");
		pbd.open(steps);
	}
	
	public void setProcess(String message){
		pbd.setProgressStep(message);
	}
	
	public void setEnd(int timer){
		pbd.setProgressEnd(timer);
	}

}
