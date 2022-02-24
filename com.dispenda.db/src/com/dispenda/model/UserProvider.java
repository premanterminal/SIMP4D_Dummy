package com.dispenda.model;

import java.util.List;

import com.dispenda.controller.ControllerFactory;

public enum UserProvider{
	INSTANCE;
	
	private List<User> listUser;
	
	private UserProvider(){
		listUser = ControllerFactory.getMainController().getCpUserDAOImpl().getAllUser();
	}
	
	public List<User> getUser(){
		return listUser;
	}
	
	public User getSelectedUser(Integer index){
		return listUser.get(index);
	}
	
	public void addItem(User x){
		listUser.add(x);
	}
	
	public void removeItem(int index){
		ControllerFactory.getMainController().getCpUserDAOImpl().deleteUser(listUser.get(index).getIdUser());
		listUser.remove(index);
	}
}