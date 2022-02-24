package com.dispenda.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;

import com.dispenda.database.DBConnection;
import com.dispenda.database.DBOperation;
import com.dispenda.model.SubModul;
import com.dispenda.model.User;
import com.dispenda.model.UserModul;

public class CPUserDAOImpl {
	private DBOperation db = new DBOperation();
	
	public boolean saveUser(User userModel){
		String query = "MERGE INTO USER USING (VALUES(" +
				""+userModel.getIdUser()+"," +
				"'"+userModel.getUsername()+"'," +
				"'"+userModel.getPassword()+"'," +
				"'"+userModel.getDisplayName()+"'))" +
			" AS vals(" +
				"id," +
				"uname," +
				"passwd," +
				"status)" +
			" ON USER.IDUSER = vals.id" +
			" WHEN MATCHED THEN UPDATE SET " +
				"USER.USERNAMES = vals.uname," +
				"USER.PASSWORD = vals.passwd," +
				"USER.DISPLAY_NAME = vals.status" +
			" WHEN NOT MATCHED THEN INSERT VALUES " +
				"vals.id," +
				"vals.uname," +
				"vals.passwd," +
				"vals.status";
		boolean result = db.ExecuteStatementQuery(query, DBConnection.INSTANCE.open());
		if(!result)
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada MERGE(UPDATE/SET) tabel USER, USERMODUL, USERSUBMODUL.");
		return result;
	}
	
	public List<User> getAllUser() {
		String query = "SELECT * FROM USER" ;
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		if(result != null){
			try {
				List<User> listUser = new ArrayList<User>();
				while(result.next()){
					User userModel = new User();
					userModel.setIdUser(result.getInt("IDUSER"));
					userModel.setUsername(result.getString("USERNAMES"));
					userModel.setPassword(result.getString("PASSWORD"));
					userModel.setDisplayName(result.getString("DISPLAY_NAME"));
					listUser.add(userModel);
				}
				return listUser;
			}
			catch(SQLException e){
				MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database SQL Error", e.getMessage());
				return null;
			}
		}
		else{
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada pembuatan LOAD DATA tabel USER.");
			return null;
		}
	}
	
	public void deleteUser(Integer username){
		String query = "DELETE FROM USER WHERE IDUSER = "+username;
		boolean result = db.ExecuteStatementQuery(query, DBConnection.INSTANCE.open());
		if(!result)
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada DELETE tabel USER dengan IDUSER = "+username);
		else
			MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "Hapus", "User berhasil dihapus. IDUSER = "+username);
	}
	
	public Integer getLastUsername() {
		String query = "SELECT TOP 1 IDUSER FROM USER ORDER BY IDUSER DESC";
		try{
			ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
			if(result.next()){
				return result.getInt("IDUSER");
			}
			else
				return 0;
		}
		catch(SQLException e){
			e.printStackTrace();
			return 0;
		}
	}
	
	
	//FUNGSI UNTUK TABEL USER_MODUL
	public boolean saveUserModul(UserModul userModulModel){
		String query = "MERGE INTO USER_MODUL USING (VALUES(" +
				""+userModulModel.getId()+"," +
				""+userModulModel.getIdUser()+"," +
				""+userModulModel.getIdSubModul()+"," +
				""+userModulModel.getBuka()+"," +
				""+userModulModel.getSimpan()+"," +
				""+userModulModel.getHapus()+"," +
				""+userModulModel.getCetak()+"" +
						"))" +
			" AS vals(" +
				"id," +
				"iduser," +
				"idsm," +
				"buka," +
				"simpan," +
				"hapus," +
				"cetak" +
				")" +
			" ON USER_MODUL.IDUSER = vals.iduser AND USER_MODUL.IDSUBMODUL = vals.idsm" +
			" WHEN MATCHED THEN UPDATE SET " +
				"USER_MODUL.ID = vals.id," +
				"USER_MODUL.IDUSER = vals.iduser," +
				"USER_MODUL.IDSUBMODUL = vals.idsm," +
				"USER_MODUL.BUKA = vals.buka," +
				"USER_MODUL.SIMPAN = vals.simpan," +
				"USER_MODUL.HAPUS = vals.hapus," +
				"USER_MODUL.CETAK = vals.cetak" +
			" WHEN NOT MATCHED THEN INSERT VALUES " +
				"vals.id," +
				"vals.iduser," +
				"vals.idsm," +
				"vals.buka," +
				"vals.simpan," +
				"vals.hapus," +
				"vals.cetak" ;
		boolean result = db.ExecuteStatementQuery(query, DBConnection.INSTANCE.open());
//		if(!result)
//			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada MERGE(UPDATE/INSERT) tabel USER_MODUL.");
		return result;
		
	}
	
	public List<UserModul> getAllUserModul(){
		String query = "SELECT * FROM USER_MODUL LEFT JOIN USER on USER_MODUL.IDUSER = USER.IDUSER";
		
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		if(result!=null){
			try{
				List<UserModul> listUserModul = new ArrayList<UserModul>();
				int index = 1;
				while(result.next()){
					UserModul userModulModel = new UserModul();
					userModulModel.setIndex(index);
					userModulModel.setId(result.getInt("ID"));
					userModulModel.setIdUser(result.getInt("IDUSER"));
					userModulModel.setIdSubModul(result.getInt("IDSUBMODUL"));
					userModulModel.setDisplayName(result.getString("DISPLAY_NAME"));
					userModulModel.setBuka(result.getBoolean("BUKA"));
					userModulModel.setSimpan(result.getBoolean("SIMPAN"));
					userModulModel.setHapus(result.getBoolean("HAPUS"));
					userModulModel.setCetak(result.getBoolean("CETAK"));
					listUserModul.add(userModulModel);
					index++;
				}
				return listUserModul;
			}
			catch(SQLException e){
				MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database SQL Error", e.getMessage());
				return null;
			}
		}
		else{
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada pembuatan LOAD DATA tabel USER_MODUL.");
			return null;
		}
	}
	
	public List<UserModul> getAllUserModul(Integer idUser){
		String query = "SELECT * FROM USER_MODUL " +
				"LEFT JOIN SUBMODUL on USER_MODUL.IDSUBMODUL = SUBMODUL.IDSUBMODUL " +
				"LEFT JOIN USER on USER_MODUL.IDUSER = USER.IDUSER " +
				"WHERE IDUSER = "+idUser;
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		if(result != null){
			try{
				List<UserModul> listUserModul = new ArrayList<UserModul>();
				int index = 1;
				while(result.next()){
					UserModul userModulModel = new UserModul();
					userModulModel.setIndex(index);
					userModulModel.setId(result.getInt("ID"));
					userModulModel.setIdUser(result.getInt("IDUSER"));
					userModulModel.setIdSubModul(result.getInt("IDSUBMODUL"));
					userModulModel.setDisplayName(result.getString("DISPLAY_NAME"));
					SubModul subModul = new SubModul();
					subModul.setIdSubModul(result.getInt("IDSUBMODUL"));
					subModul.setNamaModul(result.getString("SUBMODUL_NAME"));
					userModulModel.setSubModul(subModul);
					userModulModel.setBuka(result.getBoolean("BUKA"));
					userModulModel.setSimpan(result.getBoolean("SIMPAN"));
					userModulModel.setHapus(result.getBoolean("HAPUS"));
					userModulModel.setCetak(result.getBoolean("CETAK"));
					listUserModul.add(userModulModel);
					index++;
				}
				return listUserModul;
			}
			catch(SQLException e){
				MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database SQL Error", e.getMessage());
				return null;
			}
		}
		else{
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada pembuatan LOAD DATA tabel USER_MODUL.");
			return null;
		}
	}
	
	public HashMap<Integer,UserModul> getHMUserModul(Integer idUser){
		String query = "SELECT * FROM USER_MODUL " +
				"LEFT JOIN SUBMODUL on USER_MODUL.IDSUBMODUL = SUBMODUL.IDSUBMODUL " +
				"LEFT JOIN USER on USER_MODUL.IDUSER = USER.IDUSER " +
				"WHERE IDUSER = "+idUser;
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		if(result != null){
			try{
				HashMap<Integer,UserModul> listUserModul = new HashMap<Integer,UserModul>();
				int index = 1;
				while(result.next()){
					UserModul userModulModel = new UserModul();
					userModulModel.setIndex(index);
					userModulModel.setId(result.getInt("ID"));
					userModulModel.setIdUser(result.getInt("IDUSER"));
					userModulModel.setIdSubModul(result.getInt("IDSUBMODUL"));
					userModulModel.setDisplayName(result.getString("DISPLAY_NAME"));
					SubModul subModul = new SubModul();
					subModul.setIdSubModul(result.getInt("IDSUBMODUL"));
					subModul.setNamaModul(result.getString("SUBMODUL_NAME"));
					userModulModel.setSubModul(subModul);
					userModulModel.setBuka(result.getBoolean("BUKA"));
					userModulModel.setSimpan(result.getBoolean("SIMPAN"));
					userModulModel.setHapus(result.getBoolean("HAPUS"));
					userModulModel.setCetak(result.getBoolean("CETAK"));
					listUserModul.put(result.getInt("IDSUBMODUL"),userModulModel);
					index++;
				}
				return listUserModul;
			}
			catch(SQLException e){
				MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database SQL Error", e.getMessage());
				return null;
			}
		}
		else{
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada pembuatan LOAD DATA tabel USER_MODUL.");
			return null;
		}
	}
	
	public void deleteUserModul(Integer id){
		String query = "DELETE FROM USER_MODUL WHERE ID = "+id;
		boolean result = db.ExecuteStatementQuery(query, DBConnection.INSTANCE.open());
		if(!result)
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada DELETE tabel USER_MODUL dengan ID = "+id);
	}
	
	public Integer getLastUserModul(){
		String query = "SELECT TOP 1 ID FROM USER_MODUL ORDER BY ID DESC";
		try{
			ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
			if(result.next()){
				return result.getInt("ID");
			}
			else
				return 0;
		}
		catch(SQLException e){
			e.printStackTrace();
			return 0;
		}
	}
	
	public boolean getAksesSptpd(String kodePajak, Integer id){
		String [] kdPajak = {"AIR_TANAH","HOTEL","RESTORAN","HIBURAN","PARKIR","REKLAME","BURUNG_WALET"};
		String query = "SELECT "+kdPajak[Integer.parseInt(kodePajak)-2]+" FROM AKSES_SPTPD WHERE IDUSER = "+id+";";
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		//System.out.println(kodePajak);
		//System.out.println(id);
		//System.out.println(query);
		//System.out.println(result);
		//System.out.println(kdPajak[Integer.parseInt(kodePajak)-2]);
		boolean rslt = false;
		try {
			if (result.next()){
				rslt = result.getBoolean(kdPajak[Integer.parseInt(kodePajak)-2]);
				//System.out.println(rslt);
				return rslt;
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rslt;
	}
	
	public boolean getAksesLHP(String kodePajak, Integer id){
		String [] kdPajak = {"AIR_TANAH","HOTEL","RESTORAN","HIBURAN","PARKIR","REKLAME","BURUNG_WALET"};
		String query = "SELECT "+kdPajak[Integer.parseInt(kodePajak)-2]+" FROM AKSES_LHP WHERE IDUSER = "+id+";";
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		//System.out.println(kodePajak);
		//System.out.println(id);
		//System.out.println(query);
		//System.out.println(result);
		//System.out.println(kdPajak[Integer.parseInt(kodePajak)-2]);
		boolean rslt = false;
		try {
			if (result.next()){
				rslt = result.getBoolean(kdPajak[Integer.parseInt(kodePajak)-2]);
				//System.out.println(rslt);
				return rslt;
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rslt;
	}
	
	/*//FUNGSI UNTUK TABEL USER_SUBMODUL
	public boolean saveUserSubModul(UserSubModul userSubModulModel){
		String query = "MERGE INTO USER_SUBMODUL USING (VALUES(" +
				"'"+userSubModulModel.getUsername()+"'," +
				"'"+userSubModulModel.getIdSubModul()+"'))" +
			" AS vals(" +
				"uname," +
				"id_submodul)" +
			" ON USER_SUBMODUL.USERNAME = vals.uname AND USER_SUBMODUL.IDSUBMODUL = vals.id_submodul" +
			" WHEN MATCHED THEN UPDATE SET " +
				"USER_SUBMODUL.USERNAME = vals.uname," +
				"USER_SUBMODUL.IDSUBMODUL = vals.id_submodul" +
			" WHEN NOT MATCHED THEN INSERT VALUES " +
				"vals.uname," +
				"vals.id_submodul" ;
		boolean result = db.ExecuteStatementQuery(query, DBConnection.INSTANCE.open());
		if(!result)
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada MERGE(UPDATE/INSERT) tabel USER_SUBMODUL.");
		return result;
		
	}
	
	public List<UserSubModul> getAllUserSubModul(){
		String query = "SELECT * FROM USER_SUBMODUL";
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		if(result!=null){
			try{
				List<UserSubModul> listUserSubModul = new ArrayList<UserSubModul>();
				int index = 1;
				while(result.next()){
					UserSubModul userSubModulModel = new UserSubModul();
					userSubModulModel.setIndex(index);
					userSubModulModel.setUsername(result.getString("USERNAME"));
					userSubModulModel.setIdSubModul(result.getInt("IDSUBMODUL"));
					listUserSubModul.add(userSubModulModel);
					index++;
				}
				return listUserSubModul;
			}
			catch(SQLException e){
				MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database SQL Error", e.getMessage());
				return null;
			}
		}
		else{
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada pembuatan LOAD DATA tabel USER_SUBMODUL.");
			return null;
		}
	}
	
	public List<UserSubModul> getAllUserSubModul(String uname){
		String query = "SELECT * FROM USER_SUBMODUL WHERE USERNAME = "+uname;
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		if(result != null){
			try{
				List<UserSubModul> listUserSubModul = new ArrayList<UserSubModul>();
				int index = 1;
				while(result.next()){
					UserSubModul userSubModulModel = new UserSubModul();
					userSubModulModel.setIndex(index);
					userSubModulModel.setUsername(result.getString("USERNAME"));
					userSubModulModel.setIdSubModul(result.getInt("IDSUBMODUL"));
					listUserSubModul.add(userSubModulModel);
					index++;
				}
				return listUserSubModul;
			}
			catch(SQLException e){
				MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database SQL Error", e.getMessage());
				return null;
			}
		}
		else{
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada pembuatan LOAD DATA tabel USER_SUBMODUL.");
			return null;
		}
	}
	
	public void deleteUserSubModul(Integer index){
		String query = "DELETE FROM USER_SUBMODUL WHERE INDEX"+index;
		boolean result = db.ExecuteStatementQuery(query, DBConnection.INSTANCE.open());
		if(!result)
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada DELETE tabel USER_SUBMODUL dengan INDEX = "+index);
	}
	
	public Integer getLastUserSubModul(){
		String query = "SELECT TOP 1 INDEX FROM USER_SUBMODUL ORDER BY INDEX DESC";
		try{
			ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
			if(result.next()){
				return result.getInt("INDEX");
			}
			else
				return 0;
		}
		catch(SQLException e){
			e.printStackTrace();
			return 0;
		}
	}*/
	
}