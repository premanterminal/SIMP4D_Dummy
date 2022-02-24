package com.dispenda.transfer;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;

public class Server implements Runnable {

	ServerSocket serversocket;
	BufferedReader br1, br2;
	PrintWriter pr1;
	Socket socket;
	Thread t1, t2;
	String in="",out="";
	String path = "C:/wamp/www/DB/";

	public Server() {
		try {
			t1 = new Thread(this);
			t2 = new Thread(this);
			serversocket = new ServerSocket(5000);
			System.out.println("Server is waiting. . . . ");
			socket = serversocket.accept();
			System.out.println("Client connected with Ip " +        socket.getInetAddress().getHostAddress());
			t1.start();
			t2.start();
		} catch (Exception e) {
		}
	}

	public void run() {
		try {
			if (Thread.currentThread() == t1) {
				do {
//					br1 = new BufferedReader(new InputStreamReader(System.in));
					pr1 = new PrintWriter(socket.getOutputStream(), true);
//					in = br1.readLine();
//					pr1.println(in);
					/*if(in.equalsIgnoreCase("CHECK")){
						System.out.println("Test");
						String path = "C:/wamp/www/DB/";
				        String text="";
				        File f = new File(path);
				        File[] listOfFiles = f.listFiles();
				        for (int j = 0; j < listOfFiles.length; j++) {
				        	if (listOfFiles[j].isFile()) {
				        		text = listOfFiles[j].getName();
				        		pr1.println(text);
				        	}
				        }
					}*/
//			        pr1.close();
				} while (!in.equals("END"));
			} else {
				do {
					br2 = new BufferedReader(new   InputStreamReader(socket.getInputStream()));
					out = br2.readLine();
					if(out.equalsIgnoreCase("CHECK")){
						System.out.println("Test");
						String path = "D:/S1/";
				        String text="";
				        File f = new File(path);
				        File[] listOfFiles = f.listFiles();
				        for (int j = 0; j < listOfFiles.length; j++) {
				        	if (listOfFiles[j].isFile()) {
				        		text = listOfFiles[j].getName();
				        		pr1.println(text);
				        		System.out.println(text);
				        		if(out.contains("TRANSFER")){
									BufferedOutputStream put=new BufferedOutputStream(socket.getOutputStream());
						   			String[] split = out.split(":");
						   			path = path+"/"+split[1];
						   			File file =new File(path);
						   	        if(file.isFile()){ 
						   	        	FileInputStream fis=new FileInputStream(file);

						   	            byte []buf=new byte[1024];
						   	            int read;
						   	            while((read=fis.read(buf,0,1024))!=-1){
						   	            	put.write(buf,0,read);
						   	                put.flush();
						   	            }
						   	            System.out.println("File transfered");
						   	        }
								}
				        	}
				        }
					}
					
					System.out.println("Client says : : : " + out);
				} while (!out.equals("END"));
			}
		} catch (Exception e) {
		}
	}

	public static void main(String[] args) {
		new Server();
	}
}