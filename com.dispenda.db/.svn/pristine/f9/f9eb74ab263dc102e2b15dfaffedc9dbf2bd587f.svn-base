package com.dispenda.database;

import java.sql.Connection;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;

import com.dispenda.db.Activator;

public enum DBConnection {
	
	INSTANCE;

	private String dbPath[] = new String[1];
	private String serverBawah[] = new String[1];

	private String DBName = "dispenda";
	private DbClass db;
	private Connection con;
	private DbClass serverBawahDB;
	private Connection conBawah;
		
	public Connection ConnectToDb(){
		dbPath[0] = Activator.getDefault().readFile("urldb");
		for(int i=0;i<dbPath.length;){
			try {
				db = new DbClass (DBName, dbPath[i]);
				break;
			} catch (Exception e) {
//				MessageDialog.openError(Display.getCurrent().getActiveShell(), "Error", e.getMessage());
				break;
				
			}
		}
		con = DbClass.getConn();
//		System.out.println("Connecting to DataBase..");
		return con;
	}
	
	public void close() throws Exception{
		db.shutdown();
		serverBawahDB.shutdown();
//		System.out.println("Disconnecting DataBase..");
	}
	
	public Connection open(){
		try{
			if(con == null || con.isClosed())
				con = ConnectToDb();
			return con;
		} catch (Exception e){
			String message = e.getMessage();
			boolean result = MessageDialog.open(SWT.ERROR, new Shell(), "Connection to Database Error", message, SWT.ERROR);
			if(result)
				System.exit(0);
			return null;
		}
	}

	private String displayName = "";
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
	public String getDisplayName() {
		return displayName;
	}
	
	public Connection ConnectServerBawah(){
		serverBawah[0] = Activator.getDefault().readFile("urldbbawah");
		for(int i=0;i<serverBawah.length;){
			try {
				serverBawahDB = new DbClass (DBName, serverBawah[i]);
				break;
			} catch (Exception e) {
//				MessageDialog.openError(Display.getCurrent().getActiveShell(), "Error", e.getMessage());
				break;
				
			}
		}
		conBawah = DbClass.getConn();
//		System.out.println("Connecting to DataBase..");
		return conBawah;
	}
	
	public Connection openBawah(){
		try{
			if(conBawah == null || conBawah.isClosed())
				conBawah = ConnectServerBawah();
			return conBawah;
		} catch (Exception e){
			String message = e.getMessage();
			boolean result = MessageDialog.open(SWT.ERROR, new Shell(), "Connection to Database Error", message, SWT.ERROR);
			if(result)
				System.exit(0);
			return null;
		}
	}
}