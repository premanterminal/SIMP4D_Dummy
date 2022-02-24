package com.dispenda.object;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

public class UploadFile {
	private static String server = "172.16.78.3";
    private static String user = "Administrator";
    private static String pass = "Kuc1ngKuru5123";

    static FTPClient ftpClient = new FTPClient();
    
	public static void openFTP() throws SocketException{
		try {
			ftpClient.connect(server);
			ftpClient.login(user, pass);
	        ftpClient.enterLocalPassiveMode();
	        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void closeFTP(){
		try {
            if (ftpClient.isConnected()) {
                ftpClient.logout();
                ftpClient.disconnect();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
	}
	
	public static String upload(File file, String filename, String directory, int count) throws Exception {
		String logMessage = "";
        File firstLocalFile = new File(file.getPath());
 
        String firstRemoteFile = "ScannedArsip/" + directory + filename + ".jpg";
        InputStream inputStream = new FileInputStream(firstLocalFile);
 
        boolean done = ftpClient.storeFile(firstRemoteFile, inputStream);
        inputStream.close();
        if (done) {
        	logMessage = count + ". File path: " + file.getPath() + " filename: " + filename + " berhasil disimpan." + System.lineSeparator();
        }else{
        	logMessage = count + ". File path: " + file.getPath() + " filename: " + filename + " gagal disimpan." + System.lineSeparator();
        }
 
        return logMessage;
	}
	
	public static boolean download(File targetFile, String sourcePath) throws Exception {
//		File remoteFile = new File(sourcePath.split("3/")[1]);
        /*OutputStream outputStream = new FileOutputStream(sourcePath.split("3/")[1]);
        boolean done = ftpClient.retrieveFile(targetFile.getPath(), outputStream);
        outputStream.close();*/
		boolean done = false;
        InputStream initialStream = ftpClient.retrieveFileStream(sourcePath.split("3/")[1]);
        if (initialStream != null){
        	byte[] buffer = new byte[initialStream.available()];
            initialStream.read(buffer);
         
            OutputStream outStream = new FileOutputStream(targetFile);
            outStream.write(buffer);
        	done = true;
        }
        
        return done;
	}
	
	public static boolean checkFile(String filename, int count){
		boolean retValue = false;
		String remotePath = "/ScannedArsip/";
		if (count > 0)
			remotePath += filename + "(" + count +").jpg";
		else
			remotePath += filename + ".jpg";
		try {
			FTPFile[] remoteFiles = ftpClient.listFiles(remotePath);
			if (remoteFiles.length > 0)
				retValue = true;
			else
				retValue = false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return retValue;
	}
}
