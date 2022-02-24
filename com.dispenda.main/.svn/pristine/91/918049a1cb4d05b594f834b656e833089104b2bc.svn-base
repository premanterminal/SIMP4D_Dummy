package com.dispenda.transfer;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.util.HashMap;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

public class Client implements Runnable {

	private BufferedReader br1, br2;
	private PrintWriter pr1;
	private Socket socket;
	private Thread t1, t2;
	private String in = "", out = "";
	private URL url;
	private HashMap<String, String> listofPlugins = new HashMap<String, String>();

	public Client() {
		try {
//			url = Platform.getInstallLocation().getURL();
//			url = new URL("D:/Job/Mataniari/Dispenda2014/Export/v0.0.2/dispenda/plugins/");
//			File f = new File(url.getFile()+"/plugins/");
			File f = new File("D:/Job/Mataniari/Dispenda2014/Export/v0.0.2/dispenda/plugins/");
			File[] listOfFiles = f.listFiles();
	        for (int j = 0; j < listOfFiles.length; j++) {
	        	if (listOfFiles[j].isFile() && listOfFiles[j].toString().contains("com.dispenda.")) {
	        		String[] split = listOfFiles[j].getName().split("_");
	        		listofPlugins.put(split[0], split[1]);
	        		System.out.println(listOfFiles[j].getName());
//	        		MessageDialog.openInformation(new Shell(), "", listOfFiles[j].getName());
	        	}
	        }
			t1 = new Thread(this);
			t2 = new Thread(this);
			socket = new Socket("localhost", 5000);
			t1.start();
			t2.start();

		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public void run() {

		try {
			if (Thread.currentThread() == t2) {
				do {
//					br1 = new BufferedReader(new InputStreamReader(System.in));
					pr1 = new PrintWriter(socket.getOutputStream(), true);
//					in = br1.readLine();
					pr1.println("CHECK");
					pr1.println("END");
				} while (!in.equals("END"));
			} else {
				do {
//					pr1.println("CHECK");
					br2 = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					out = br2.readLine();
					String[] split = out.split("_");
					System.out.println(split[0]+" "+split[1]);
					if(listofPlugins.containsKey(split[0])){
						if(!listofPlugins.get(split[0]).equalsIgnoreCase(split[1])){
//							File file = new File(url.getFile()+"/plugins/"+split[0]+"_"+listofPlugins.get(split[0]));
//							File file = new File("D:/Job/Mataniari/Dispenda2014/Export/v0.0.2/dispenda/plugins/"+split[0]+"_"+listofPlugins.get(split[0]));
//							file.delete();
							
							pr1.println("TRANSFER:"+out);
							
				   			BufferedInputStream get=new BufferedInputStream(socket.getInputStream());
//				   			FileOutputStream  fs=new FileOutputStream(new File(url.getFile()+"/plugins/",out));
				   			FileOutputStream  fs=new FileOutputStream(new File("D:/Client/",out));
				   			int u = 0;
				   			byte jj[]=new byte[1024];
				   			while((u=get.read(jj,0,1024))!=-1){ 
				   				fs.write(jj,0,u);
				   				fs.flush();
				   			}
				   			fs.close();
						}
					}else{
						pr1.println("TRANSFER:"+out);
						
			   			BufferedInputStream get=new BufferedInputStream(socket.getInputStream());
//			   			FileOutputStream  fs=new FileOutputStream(new File(url.getFile()+"/plugins/",out));
			   			FileOutputStream  fs=new FileOutputStream(new File("D:/Client/",out));
			   			int u = 0;
			   			byte jj[]=new byte[1024];
			   			while((u=get.read(jj,0,1024))!=-1){ 
			   				fs.write(jj,0,u);
			   				fs.flush();
			   			}
			   			fs.close();
					}
					
					System.out.println("Server says : : : " + out);
				} while (!out.equals("END"));
			}
		} catch (Exception e) {
		}

	}

	public static void main(String[] args) {
		new Client();
	}
}