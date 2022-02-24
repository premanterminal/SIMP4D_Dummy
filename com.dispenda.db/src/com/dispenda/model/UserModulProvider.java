package com.dispenda.model;

import java.util.List;

import com.dispenda.controller.ControllerFactory;

public enum UserModulProvider{
	INSTANCE;
	
	private List<UserModul> listUserModul;
	
	private UserModulProvider(){
//		listUserModul = ControllerFactory.getMainController().getCpUserDAOImpl().getAllUserModul();
	}
	
	public List<UserModul> getUserModul(){
		return listUserModul;
	}
	
	public List<UserModul> getUserModul(Integer id){
		this.listUserModul = ControllerFactory.getMainController().getCpUserDAOImpl().getAllUserModul(id);
		return listUserModul;
	}
	
	public UserModul getSelectedUserModul(Integer index){
		return listUserModul.get(index);
	}
	
	public void addItem(UserModul x){
		listUserModul.add(x);
	}
	
	public void removeItem(int index){
		ControllerFactory.getMainController().getCpUserDAOImpl().deleteUserModul(index);
		listUserModul.remove(index);
	}
	
	
}