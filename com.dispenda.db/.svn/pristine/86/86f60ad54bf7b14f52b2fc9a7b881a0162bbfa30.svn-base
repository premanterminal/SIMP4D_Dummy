package com.dispenda.database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
//import org.hsqldb.util.ScriptTool;

/**
 * This is the database class to connect to HSQL database.
 * 
 * @author fermi
 *
 */
public class DbClass {
	private static Connection conn;

	private static String url;

	private static String DBName;

	@SuppressWarnings("rawtypes")
	private static Vector result2;

	private static ResultSetMetaData metaRS;

	private static Object obj;

	private static int colCount;

	private static int ii;

	@SuppressWarnings("rawtypes")
	private static Vector row;

	private static int[] resultInt;

	@SuppressWarnings("rawtypes")
	private Vector result;
	
	private Statement st;
	
	private ResultSet rs;

	private int counter;

	private static String resultStr;

	private static String[][] resultArrLst;

	public DbClass(String dbName, String dbPath){
		try {
			Class.forName("org.hsqldb.jdbcDriver").newInstance();
			url =  dbPath;
			DBName = dbName;
			System.out.println(url + DBName+";shutdown=true");
			DriverManager.setLoginTimeout(10);
			conn = DriverManager.getConnection(url + DBName+";shutdown=true", "scbd_medan", "d15pend@");
//			System.setProperty("javax.net.ssl.trustStore", "C:\\Users\\Mael\\client-dispenda.keystore");
//			System.setProperty("javax.net.ssl.trustStorePassword", "Alt+2360");
//			conn = DriverManager.getConnection("jdbc:hsqldb:hsqls://dev.tokoartis.com/dispenda", "scbd_medan", "d15pend@");
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Query the db for worm signature.
	 * 
	 * @param queryString
	 * @return
	 * @throws SQLException
	 */
	public synchronized String[][] queryDBForWormSig(String queryString) throws SQLException {
		st = null;
		rs = null;
		st = conn.createStatement();
		rs = st.executeQuery("SELECT COUNT(WORM_STRING) AS NUMOFSIG FROM WORM_PATTERN");
		rs.next();
		counter= rs.getInt("NUMOFSIG");
		rs = st.executeQuery(queryString);
		resultArrLst = new String[3][counter];
		resultArrLst = dumpRSArrLst(rs);
		st.close();
		return resultArrLst;
	}
	
	/**
	 * Query the db for any information (general)
	 * 
	 * @param queryString
	 * @return
	 * @throws SQLException
	 */
	@SuppressWarnings("rawtypes")
	public synchronized Vector queryDB(String queryString) throws SQLException {
		st = null;
		rs = null;
		result = null;
		st = conn.createStatement();
		rs = st.executeQuery(queryString);
		result = dumpRS(rs);
		st.close();
		return result;
	}
	
	/**
	 * Query the db for version
	 * @param queryString
	 * @return
	 * @throws SQLException
	 */
	public synchronized String queryDBVersion(String queryString) throws SQLException {
		st = null;
		rs = null;
		st = conn.createStatement();
		rs = st.executeQuery(queryString);
		resultStr = dumpRSForVersion(rs);
		st.close();
		return resultStr;
	}
	
	/**
	 * Query the db for ports being monitored.
	 * 
	 * @param queryString
	 * @return
	 * @throws SQLException
	 */
	public synchronized int[] queryDBForPorts(String queryString) throws SQLException {
		st = null;
		rs = null;
		st = conn.createStatement();
		rs = st.executeQuery("SELECT COUNT(PORT_NO) AS NUMOFPORT FROM PORT_INFO");
		rs.next();
		counter= rs.getInt("NUMOFPORT");
		rs = st.executeQuery(queryString);
//		System.out.println("about to parse the result set with counter: "+ counter);
		resultInt = new int[counter];
		resultInt = dumpRSPorts(rs);
		st.close();
		return resultInt;
	}

	/**
	 * update the db with the updateString.
	 * 
	 * @param updateString
	 * @throws SQLException
	 */
	public synchronized void updateDB(String updateString) throws SQLException {
		st = null;
		st = conn.createStatement();
		int i = st.executeUpdate(updateString);

		if (i == -1) {
			System.out.println("db error : " + updateString);
		}
		st.close();
	}

	/**
	 * check the table name whether it is true or not
	 * 
	 * @param tableName
	 * @return
	 * @throws SQLException
	 */
	public static boolean checkTable(String tableName) throws SQLException {
		DatabaseMetaData metaDB = conn.getMetaData();
		ResultSet rs = metaDB.getTables(null, null, tableName, null);
		ResultSetMetaData metaRS = rs.getMetaData();
		int i;
		int colCount = metaRS.getColumnCount();
		Object obj = null;

		for (; rs.next();) {
			for (i = 1; i <= colCount; i++) {
				obj = rs.getObject(i);

				if (obj == tableName) {
					return true;
				}
			}
		}
		return false;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Vector dumpRS(ResultSet rs) throws SQLException {
		result2 = new Vector();
		metaRS = rs.getMetaData();
		colCount = metaRS.getColumnCount();
		obj = null;

		for (; rs.next();) {
			row = new Vector();
			for (ii = 1; ii <= colCount; ii++) {
				obj = rs.getObject(ii);
				row.add(obj.toString());
			}
			result2.add(row);
		}

		return result2;
	}

	public static String dumpRSForVersion(ResultSet rs) throws SQLException {
		while(rs.next()) {
			resultStr=rs.getString(1);
			
//			System.out.println("test: "+ resultArrLst[2][ii]);
			
		}

		return resultStr;
	}

	public static String[][] dumpRSArrLst(ResultSet rs) throws SQLException {
		ii=0;
		while(rs.next()) {
			resultArrLst[0][ii]=rs.getString(3);
			resultArrLst[1][ii]=rs.getString(4);
			resultArrLst[2][ii]=rs.getString(6);//String.valueOf(rs.getInt(6));
//			System.out.println("test: "+ resultArrLst[2][ii]);
			ii++;
		}

		return resultArrLst;
	}

	public static int[] dumpRSPorts(ResultSet rs) throws SQLException {
		
		ii=0;
		try{
		while (rs.next()) {
				resultInt[ii] = rs.getInt(1);
//				System.out.println("test: "+ resultInt[ii]);
				ii++;
		}
		}catch(Exception ex){
			ex.printStackTrace();
		}

		return resultInt;
	}

	/*public void runScript(String urlString) {
		ScriptTool sTool = new ScriptTool();

		String[] targs = { "-url", url, "-database", DBName, "-script",
				urlString, "-log", "false", "-batch", "false" };
		sTool.execute(targs);
	}*/

	public void shutdown() throws SQLException {
		Statement st = conn.createStatement();
		st.execute("SHUTDOWN COMPACT");
		conn.close();
	}

	public static Connection getConn() {
		return conn;
	}
}
