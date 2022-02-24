package com.dispenda.transfer;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;


public class FileTransferServer{ 

//	private static BufferedOutputStream put;

	public static void main(String args[])throws IOException{ 
		/*ServerSocket ss=null;
        try{  
        	ss=new ServerSocket(8085);
        }
        catch(IOException e){ 
            System.out.println("couldn't listen");
            System.exit(0);
        }
        Socket cs=null;
        try{ 
        	cs=ss.accept();
            System.out.println("Connection established"+cs);
        }
        catch(Exception e){ 
            System.out.println("Accept failed");
            System.exit(1);
        } 
        BufferedOutputStream put=new BufferedOutputStream(cs.getOutputStream());
        BufferedReader st=new BufferedReader(new InputStreamReader(cs.getInputStream()));
        String s=st.readLine();
        String str = "C:/wamp/www/DB/";
        String path = str + s; 
        System.out.println("The requested file is path: "+path);
        System.out.println("The requested file is : "+s);
        File f=new File(path);
        if(f.isFile()){ 
        	FileInputStream fis=new FileInputStream(f);

            byte []buf=new byte[1024];
            int read;
            while((read=fis.read(buf,0,1024))!=-1){
            	put.write(buf,0,read);
                put.flush();
            }
                   //d.close();
            System.out.println("File transfered");
            cs.close();
            ss.close();
        }*/  
		
		String clientSentence;
        String capitalizedSentence;
        ServerSocket welcomeSocket = new ServerSocket(6104);

        while(true)
        {
           Socket connectionSocket = welcomeSocket.accept();
//           BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
           PrintWriter outToClient = new PrintWriter(connectionSocket.getOutputStream(),true);
//           clientSentence = inFromClient.readLine();
//           System.out.println("Received view files request from user: " + clientSentence);
           String path = "C:/wamp/www/DB/";
           String text="";
           File f = new File(path);
           File[] listOfFiles = f.listFiles();
           for (int j = 0; j < listOfFiles.length; j++) {
        	   if (listOfFiles[j].isFile()) {
        		   text = listOfFiles[j].getName();
                   outToClient.println(text);
        	   }
           }
           outToClient.flush();
           outToClient.close();
           String line = null;
           BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
           BufferedOutputStream put=new BufferedOutputStream(connectionSocket.getOutputStream());
	   	  	while ((line = inFromClient.readLine()) != null) {
	   		  if(line.contains("TRANSFER")){
	   			String[] split = line.split(":");
	   			path = path+"/"+split[1];
	   			File file =new File(path);
	   	        if(file.isFile()){ 
	   	        	FileInputStream fis=new FileInputStream(file);

	   	            byte []buf=new byte[1024];
	   	            int read;
	   	            while((read=fis.read(buf,0,1024))!=-1){
	   	            	put.write(buf,0,read);
//	   	                put.flush();
	   	            }
	   	                   //d.close();
	   	            System.out.println("File transfered");
//	   	            cs.close();
//	   	            ss.close();
	   	        }
	   		  }
	   	  	}
//           welcomeSocket.
        }
	}  
}
