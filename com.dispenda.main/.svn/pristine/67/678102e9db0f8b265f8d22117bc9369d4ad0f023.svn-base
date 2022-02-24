package com.dispenda.transfer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

public class CheckUpdateVersion {
	
	public CheckUpdateVersion() throws UnknownHostException, IOException{
		Socket mysocket = new Socket("localhost", 6103);
   	  	BufferedReader inFromServer2 = new BufferedReader(new InputStreamReader(mysocket.getInputStream()));
    	  
   	  	String line = null;
   	  	while ((line = inFromServer2.readLine()) != null) {
   		  System.out.println(line);
   	  	}
   	  	inFromServer2.close();
   	  	mysocket.close();
	}
	
	private void checkVersion(){
		
	}

}
