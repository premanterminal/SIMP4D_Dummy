package com.dispenda.object;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

public class FTPUploader {
	FTPClient ftp = null;

	public FTPUploader(String host, String user, String pwd) throws Exception{
	    ftp = new FTPClient();
	    ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
	    int reply;
	    ftp.connect(host);
	    reply = ftp.getReplyCode();
	    if (!FTPReply.isPositiveCompletion(reply)) {
	        ftp.disconnect();
	        throw new Exception("Exception in connecting to FTP Server");
	    }
	    ftp.login(user, pwd);
	    ftp.setFileType(FTP.BINARY_FILE_TYPE);
	    ftp.enterLocalPassiveMode();
	}
	public void uploadFile(String localFileFullName, String fileName, String hostDir)
	        throws Exception {
	    try(InputStream input = new FileInputStream(new File(localFileFullName))){
	    this.ftp.storeFile(hostDir + fileName, input);
	    }
	}

	public void disconnect(){
	    if (this.ftp.isConnected()) {
	        try {
	            this.ftp.logout();
	            this.ftp.disconnect();
	        } catch (IOException f) {
	             // DO NOTHING
	        }
	    }
	}
	/*public static void main(String[] args) {
	   try{
	       System.out.println("Start");
	       FTPUploader ftpUploader = new FTPUploader("172.16.78.3", "Administrator", "Kuc1ngKuru5123");

	                  ftpUploader.uploadFile("D:/Venkatesh.pptx", "65.pptx", "C:/ScannedArsip");
	       ftpUploader.disconnect();
	       System.out.println("Done");
	   }catch(Exception exception){
	       exception.printStackTrace();
	   }
	}*/
}
