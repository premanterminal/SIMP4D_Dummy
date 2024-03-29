package com.dispenda.database;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLTransientConnectionException;
import java.sql.Statement;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;

public class DBOperation {

	Connection con;

	// DBConnection connect;
	public DBOperation() {
		// connect = new DBConnection();
		// try {
		// con = connect.ConnectToDb();
		// }catch (Exception e){
		// e.printStackTrace();
		// }
	}

	public boolean ExecuteStatementQuery(String query, Connection con) {
		Statement st = null;
		boolean rs = false;
		try {
			System.out.println("querry is: " + query + " with con: "
					+ con.isClosed());

			st = con.createStatement();
			System.out.println("statement is: " + st.toString());
			rs = st.execute(query);
			rs = true;
			return rs;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean ExecutePreparedStatementQuery(String query, Connection con) {
		PreparedStatement pst = null;
		boolean rs = false;
		try {
			System.out.println("querry is: " + query + " with con: "
					+ con.isClosed());

			pst = con.prepareStatement(query);
			System.out.println("prepared statement is: " + pst.toString());
			pst.executeUpdate();
			rs = true;
			pst.close();
			return rs;
		} catch (Exception e) {
			System.out.println();
			e.printStackTrace();
			return false;
		}
	}

	public boolean ExecutePreparedStatementQuery(String query, Connection con,
			String param) {
		PreparedStatement pst = null;
		boolean rs = false;
		try {
			System.out.println("querry is: " + query + " with con: "
					+ con.isClosed());

			pst = con.prepareStatement(query);
			File file = new File(param);
			FileInputStream fis = new FileInputStream(file);
			pst.setBlob(1, fis, file.length());
			System.out.println("prepared statement is: " + pst.toString());
			pst.executeUpdate();
			rs = true;
			fis.close();
			pst.close();
			return rs;
		} catch (Exception e) {
			System.out.println();
			e.printStackTrace();
			return false;
		}
	}

	public ResultSet ResultExecutedStatementQuery(String query, Connection con) {
		ResultSet rs = null;
		try {
			// System.out.println(query);
			Statement stmt = con
					.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
							ResultSet.CONCUR_UPDATABLE);
			rs = stmt.executeQuery(query);
		} catch (SQLTransientConnectionException conEx) {
			// TODO Auto-generated catch block
			MessageDialog.openError(Display.getCurrent().getActiveShell(),
					"Disconnected", "Komputer tidak terhubung ke Server");
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Error on SQL syntax");
		} catch (NullPointerException eNull) {
			MessageDialog.openError(Display.getCurrent().getActiveShell(),
					"Disconnected", "Komputer tidak terhubung ke Server");
		}
		return rs;
	}

	public ResultSet ResultPreparedStatementQuery(String query, Connection con) {
		ResultSet rs = null;
		try {
			// System.out.println(query);
			PreparedStatement stmt = con.prepareStatement(query);
			// createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
			rs = stmt.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Error on SQL syntax");
		}
		return rs;
	}
}
