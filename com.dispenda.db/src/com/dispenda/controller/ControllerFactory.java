package com.dispenda.controller;

public class ControllerFactory {
	private static MainController mainController = new MainController();
	/**
     * @return 
     * 
     */
	public static MainController getMainController(){
		return mainController;
	}
}
