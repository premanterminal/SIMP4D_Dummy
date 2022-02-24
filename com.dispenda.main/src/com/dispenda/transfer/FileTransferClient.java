package com.dispenda.transfer;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class FileTransferClient{ 
    public static void main(String srgs[]){
        /*Socket s=null;
        BufferedInputStream get=null;
        PrintWriter put=null;
        try{ 
            s=new Socket("127.0.0.1",8085);
            get=new BufferedInputStream(s.getInputStream());
            put=new PrintWriter(s.getOutputStream(),true);

            String f;
            int u;
            System.out.println("Enter the file name to transfer from server:");
            DataInputStream dis=new DataInputStream(System.in);
            f=dis.readLine();
            put.println(f);
            File f1=new File(f);
            String str = "D:/";
            FileOutputStream  fs=new FileOutputStream(new File(str,f1.toString()));
            byte jj[]=new byte[1024];
            while((u=get.read(jj,0,1024))!=-1){ 
                fs.write(jj,0,u);
            } 
            fs.close();
            System.out.println("File received");
            s.close();
        }catch(Exception e){
            e.printStackTrace();
            System.exit(0);
        }*/
    	
    	Socket mysocket;
		try {
			mysocket = new Socket("localhost", 6104);
			BufferedReader inFromServer2 = new BufferedReader(new InputStreamReader(mysocket.getInputStream()));
			PrintWriter outToServer = new PrintWriter(mysocket.getOutputStream(),true);
	   	  	String line = null;
	   	  	int u;
	   	  	while ((line = inFromServer2.readLine()) != null) {
	   	  		System.out.println(line);
	   		  if(line.equalsIgnoreCase("Update IDSUBPAJAK.txt")){
	   			  outToServer.println("TRANSFER:IDSUBPAJAK.txt");
	   			  	BufferedInputStream get=new BufferedInputStream(mysocket.getInputStream());
	   			  	FileOutputStream  fs=new FileOutputStream(new File("D:/Client/","Update IDSUBPAJAK.txt"));
	   			  	byte jj[]=new byte[1024];
	   			  	while((u=get.read(jj,0,1024))!=-1){ 
	   			  		fs.write(jj,0,u);
	   			  	}
	   			  	fs.close();
//	   			inFromServer2.close();
//	   			outToServer.close();
	   			/*BufferedInputStream get=new BufferedInputStream(mysocket.getInputStream());
		   	  	FileOutputStream  fs=new FileOutputStream(new File("D:/Client/","IDSUBPAJAK.txt"));
		   	  	byte jj[]=new byte[1024];
		   	  	while((u=get.read(jj,0,1024))!=-1){ 
		   	  		fs.write(jj,0,u);*/
		   	  	}
	   	  	}
			/*BufferedInputStream get=new BufferedInputStream(mysocket.getInputStream());
	   	  	FileOutputStream  fs=new FileOutputStream(new File("D:/Client/","Update IDSUBPAJAK.txt"));
	   	  	byte jj[]=new byte[1024];
	   	  	while((u=get.read(jj,0,1024))!=-1){ 
	   	  		fs.write(jj,0,u);
	   	  	}
	   	  	fs.close();*/
	   	  	
         System.out.println("File received");
//   	  	inFromServer2.close();
   	  	mysocket.close();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    	  
    }	      
}
