package com.dispenda.main;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.dispenda.database.DBConnection;
import com.dispenda.database.DBOperation;
import com.dispenda.encrypt.MD5;
import com.dispenda.model.User;
import com.dispenda.object.GlobalVariable;

/**
 * The activator class controls the plug-in life cycle
 */
public class MainActivator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.dispenda.main"; //$NON-NLS-1$

	// The shared instance
	private static MainActivator plugin;
	
	private Connection con;
	
//	private DBOperation db = new DBOperation();
	
	/**
	 * The constructor
	 */
	public MainActivator() {
		setConnection();
	}
	
	public void setConnection() {
		this.con = DBConnection.INSTANCE.open();
	}
	
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
	public static MainActivator getDefault() {
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

	private String displayName = "";
	
	public String getDisplayName() {
		return displayName;
	}
	public boolean checkLogin(String username, String password) {
		DBOperation db = new DBOperation();
		String query = "SELECT * FROM USER " +
//				"LEFT JOIN USER_MODUL ON USER.IDUSER = USER_MODUL.IDUSER " +
//				"INNER JOIN SUBMODUL ON USER_MODUL.IDSUBMODUL = SUBMODUL.IDSUBMODUL " +
//				"INNER JOIN MODUL ON SUBMODUL.IDMODUL = MODUL.IDMODUL " +
				"WHERE USER.USERNAMES = '"+username+"'";
		ResultSet rs = db.ResultExecutedStatementQuery(query,con);
		boolean check = false;
		try {
			while(rs.next()){
				MD5 md5 = new MD5();
				String hashPass = md5.encodeString(username+password, "ISO-8859-1");
				if (rs.getString("USERNAMES").equalsIgnoreCase(username)&&rs.getString("PASSWORD").equalsIgnoreCase(hashPass)){

					GlobalVariable.userModul.setUsername(rs.getString("USERNAMES"));
					GlobalVariable.userModul.setIdUser(rs.getInt("IDUSER"));
					GlobalVariable.userModul.setDisplayName(rs.getString("DISPLAY_NAME"));
					GlobalVariable.userModul.setPassword(rs.getString("PASSWORD"));
//					GlobalVariable.userModul.
					check =  true;
					/*privilege.put("MODUL_CONTROL_PANEL", rs.getBoolean("MODUL_CONTROL_PANEL"));
					privilege.put("MODUL_VIEW", rs.getBoolean("MODUL_VIEW"));
					privilege.put("MODUL_RIWAYAT_MUTASI", rs.getBoolean("MODUL_RIWAYAT_MUTASI"));
					privilege.put("MODUL_PENGGAJIAN", rs.getBoolean("MODUL_PENGGAJIAN"));
					privilege.put("MODUL_EDIT_DATA_PRIBADI", rs.getBoolean("MODUL_EDIT_DATA_PRIBADI"));
					privilege.put("MODUL_EDIT_DATA_KEPEGAWAIAN", rs.getBoolean("MODUL_EDIT_DATA_KEPEGAWAIAN"));
					privilege.put("MODUL_LAPORAN", rs.getBoolean("MODUL_LAPORAN"));
					privilege.put("MODUL_INPUT_DATA_PRIBADI", rs.getBoolean("MODUL_INPUT_DATA_PRIBADI"));
					privilege.put("MODUL_INPUT_DATA_KEPEGAWAIAN", rs.getBoolean("MODUL_INPUT_DATA_KEPEGAWAIAN"));*/
				}else
					check =  false;
			}
		}catch (SQLException e){
			e.printStackTrace();
		}
		return check;
	}
}
