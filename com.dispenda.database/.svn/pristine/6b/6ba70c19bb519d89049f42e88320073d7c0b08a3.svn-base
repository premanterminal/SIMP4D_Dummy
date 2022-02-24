package com.dispenda.database;

import java.sql.Connection;

import org.eclipse.core.runtime.Platform;

public enum DBConnection {
	
	INSTANCE;

	private String dbPath = "jdbc:hsqldb:hsql://localhost/db/";
	private String DBName = "dbdis";
//	String dbPath = "jdbc:hsqldb:file:"+Platform.getLocation()+"/";
//	String DBName = "dbPP";
	private DbClass db;
	private Connection con;
		
	public Connection ConnectToDb() throws Exception {
		db = new DbClass (DBName, dbPath);
		con = DbClass.getConn();
//		System.out.println("Connecting to DataBase..");
		return con;
	}
	
	public void close() throws Exception{
		db.shutdown();
//		System.out.println("Disconnecting DataBase..");
	}
	
	public Connection open(){
		try{
			con = ConnectToDb();
			return con;
		} catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}
}
