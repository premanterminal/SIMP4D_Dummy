package com.dispenda.util;

import org.eclipse.swt.widgets.Display;

import com.dispenda.dialog.LoginDialog;

public class TimerTaskLogin implements Runnable {

	private LoginDialog loginDialog;
	public void run() {
		loginDialog = new LoginDialog(Display.getCurrent().getActiveShell());
		int rs = loginDialog.open();
		if(rs==1){
			System.exit(0);
		}
	}

}
