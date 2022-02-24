package com.dispenda.model;

import java.util.List;

public enum UserSubModulProvider{
	INSTANCE;
	
	private List<UserSubModul> listUserSubModul;
	
	private UserSubModulProvider(){
		//listUserModul = ControllerFactory.getUserController().getCpUserDAOImpl().getAllUserSubModul();
	}
	
	public List<UserSubModul> getUserSubModul(){
		return listUserSubModul;
	}
	
	/*
	public List<UserSubModul> getUserSubModul(--no promary key!!!--){
		List<UserSubMdoul> listUserModul = ControllerFactory.getUserController().getCpUserDAOImpl().getAllUserSubModul(--no primary key!!--);
		this.listUserSubModul = listUserSubModul;
		return this.listUserSubModul;
	}
	
	public UserSubModul getSelectedUserSubModul(Integer index --no index!, no primary key!--){
		return listUserSubModul.get(--what? index? primary?--)
	}
	*/
	
	public void addItem(UserSubModul x){
		listUserSubModul.add(x);
	}
	
	/*
	 * public void removeItem(--berdasarkan apa?--){
	 * 		ControllerFactory.getUserController().getCpUserDAOImpl().deleteUserModul(listUserModul.get(???).get???());
	 * }
	 */
	
}