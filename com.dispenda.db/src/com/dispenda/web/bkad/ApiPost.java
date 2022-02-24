package com.dispenda.web.bkad;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.json.JSONException;
import org.json.JSONObject;

import com.dispenda.database.DBConnection;
import com.dispenda.database.DBOperation;
import com.dispenda.db.Activator;

public enum ApiPost {
	Instance;
	private DBOperation db = new DBOperation();
//	private ProgressMonitorDialog ProgressMonitorDialog progress;
	
	private boolean ping(){
		try {
			Process p = Runtime.getRuntime().exec("ping -n 1 172.16.78.2");
			//Process p = Runtime.getRuntime().exec("ping -n 1 45.32.108.141");
			
			
			int returnVal = p.waitFor();
			boolean val = (returnVal==0);
			return val;
		} catch ( IOException | InterruptedException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean sendPost(String[] objSptpd, String oldSts, IProgressMonitor monitor){
		monitor.beginTask("Accessing server..", 100);
		if(!ping())
			return false;
		boolean retValue = false;
		
		String url = "";
		if (oldSts.equalsIgnoreCase("")){ //url Insert
//			status = false;
			url = Activator.getDefault().readFile("apiurlget");
//			System.out.println(url);
//			url = "http://45.32.108.141/create";
		}else{ //url Update
//			status = true;
			url = Activator.getDefault().readFile("apiurlupdate");
//			System.out.println(url);
//			url = "http://45.32.108.141/update";
		}
		monitor.worked(1);
//			url = "http://172.16.73.4:8291/update";
//		String url = "http://202.159.112.161:1103/epaymentapi/api/web/index.php/apis/create-sts?access-token=8335b70212059154beab6b9662155d1c";

		try {
			HttpURLConnection client = (HttpURLConnection) new URL(url).openConnection();
			//System.out.println("Urlnya: "+url);
//		HttpPost post = new HttpPost(url);

		// add header
//		post.setHeader("User-Agent", USER_AGENT);
//		client.se
//		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
//		urlParameters.add(new BasicNameValuePair("No_Pokok_WP", objSptpd[0]));
//		urlParameters.add(new BasicNameValuePair("Nama_Pemilik", objSptpd[1]));
//		urlParameters.add(new BasicNameValuePair("Alamat_Pemilik", objSptpd[2]));
//		urlParameters.add(new BasicNameValuePair("Nilai", objSptpd[3]));
//		urlParameters.add(new BasicNameValuePair("Denda", objSptpd[4]));
//		urlParameters.add(new BasicNameValuePair("No_STPD", objSptpd[5]));
//		urlParameters.add(new BasicNameValuePair("Tgl_STPD", objSptpd[6]));
//		urlParameters.add(new BasicNameValuePair("Mata_Anggaran", objSptpd[7]));
//		urlParameters.add(new BasicNameValuePair("masa_bayar", objSptpd[8]));
//		urlParameters.add(new BasicNameValuePair("jatuh_tempo", objSptpd[9]));
			/*String urlParameters = "No_Pokok_WP="+objSptpd[0]+"&Nama_Pemilik="+objSptpd[1]+"&Alamat_Pemilik="+
					objSptpd[2]+"&Nilai="+objSptpd[3]+"&Denda="+objSptpd[4]+"&Tgl_STPD="+objSptpd[6]+
					"&Mata_Anggaran="+objSptpd[7]+"&masa_bayar="+objSptpd[8]+"&jatuh_tempo="+objSptpd[9]+
					"&No_STPD="+objSptpd[5];*/
			Date date = null;
			try {
				date = new SimpleDateFormat("yyyy-MM-dd").parse(objSptpd[6]);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");  
			String strDate = dateFormat.format(date);
			objSptpd[6] = strDate;
			String urlParameters = "No_Pokok_WP="+objSptpd[0]+"&Nama_Pemilik="+objSptpd[1]+"&Alamat_Pemilik="+
					objSptpd[2]+"&Nilai="+objSptpd[3]+"&Denda="+objSptpd[4]+"&No_SPTPD="+objSptpd[5]+"&Tgl_SPTPD="+objSptpd[6]+
					"&Mata_Anggaran="+objSptpd[7]+"&masa_bayar="+objSptpd[8]+"&jatuh_tempo="+objSptpd[9];
			
			if (!oldSts.equalsIgnoreCase("")){
				urlParameters += "&No_STS="+oldSts;
			}
			
			byte[] postData       = urlParameters.getBytes( StandardCharsets.UTF_8 );
			
			
			//System.out.println("objSptpd length:"+objSptpd.length);
			//for(int i=0;i<objSptpd.length;i++){
				//System.out.println("objSptpd ["+i+"]"+objSptpd[i]);
			//}
//			post.setEntity(new UrlEncodedFormEntity(urlParameters));
			client.setRequestMethod("POST");
			client.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(client.getOutputStream());
			wr.write(postData);
			wr.flush();
			wr.close();
//			post.getEntity().
	//		monitor.subTask("Set parameter..");
			monitor.worked(3);
	
//			HttpResponse response = client.set.execute(post);
//			
//			System.out.println("\nSending 'POST' request to URL : " + url);
//			System.out.println("Post parameters : " + post.getEntity());
//			System.out.println("Response Code : " +
//	                                    response.getStatusLine().getStatusCode());
//	
			BufferedReader rd = new BufferedReader(
	                        new InputStreamReader(client.getInputStream()));
	
			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
				monitor.worked(10);
			}
	//		System.out.println("Result dari JSON: "+result.toString());
	//		try {
			JSONObject jobj = new JSONObject(result.toString());
			System.out.println(jobj.get("responseCode").toString());
			if(jobj.get("responseCode").toString().equalsIgnoreCase("00")){ // jika json berhasil
				if(oldSts.equalsIgnoreCase("")){ // insert sts
					String noSts = jobj.get("No_STS").toString();
					System.out.println("No STS :"+noSts);
					String query = "";
					if (objSptpd[5].contains("SKPDKB"))
						query = "Update Periksa set STS = '" + noSts + "', STSVALID = '" + objSptpd[9] + "' where No_SKP = '" + objSptpd[5] + "' AND NPWPD = '" + objSptpd[0] + "'";
					else
						query = "Update SPTPD set STS = '" + noSts + "', STSVALID = '" + objSptpd[9] + "' where No_SPTPD = '" + objSptpd[5] + "' AND NPWPD = '" + objSptpd[0] + "'";
					boolean resultQuery = db.ExecuteStatementQuery(query, DBConnection.INSTANCE.open());
					if(!resultQuery)
						retValue = false;
	//				else
	//					MessageDialog.openError(Display.getCurrent().getActiveShell(), "STS", "Insert STS sukses.");
					retValue = true;
				}else{ // update sts
					String query = "";
					if (objSptpd[5].contains("SKPDKB"))
						query = "Update Periksa set STSVALID = '" + objSptpd[9] + "' where No_SKP = '" + objSptpd[5] + "' AND NPWPD = '" + objSptpd[0] + "'";
					else
						query = "Update SPTPD set STSVALID = '" + objSptpd[9] + "' where No_SPTPD = '" + objSptpd[5] + "' AND NPWPD = '" + objSptpd[0] + "'";
					boolean resultQuery = db.ExecuteStatementQuery(query, DBConnection.INSTANCE.open());
					if(!resultQuery)
						retValue = false;
					retValue = true;
				}
			}else if(jobj.get("responseCode").toString().equalsIgnoreCase("14")){ // jika json gagal
				MessageDialog.openError(Display.getCurrent().getActiveShell(), "Error STS Json", jobj.get("Message").toString());
			}else if(jobj.get("responseCode").toString().equalsIgnoreCase("91")){
				MessageDialog.openError(Display.getCurrent().getActiveShell(), "Error STS Json", "Internal Error. Response Code : " + jobj.get("responseCode").toString());
			}else{ // jika koneksi gagal
				MessageDialog.openError(Display.getCurrent().getActiveShell(), "Error STS Json", "Tidak ada response code dari server. Response Code : " + jobj.get("responseCode").toString());
			}
		} catch (UnsupportedEncodingException e1) {
			monitor.done();
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Encoding Failed", e1.getMessage());
			e1.printStackTrace();
		} catch (ClientProtocolException e) {
			monitor.done();
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Network Failed", e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			monitor.done();
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Read File Failed", e.getMessage());
			e.printStackTrace();
		} catch (JSONException e) {
			monitor.done();
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Error STS Json", e.getMessage());
			e.printStackTrace();
		}
		monitor.done();
		return retValue;
	}
	
	public boolean sendPostAngsuran(String[] objSptpd, String oldSts, Integer idMohon, Integer cicilan, IProgressMonitor monitor){
		monitor.beginTask("Accessing Server..", 100);
		if(!ping())
			return false;
		boolean retValue = false;

		String url = "";
		if (oldSts == ""){ //url Insert
//			url = "http://serpispaimen.pemkomedan.go.id/index.php/apis/create-sts-replika?access-token=8335b70212059154beab6b9662155d1c";
			url = Activator.getDefault().readFile("apiurlget");
			System.out.println(url);
//			url = "http://172.16.73.4:8291/home";
		}else{ //url Update
//			url = "http://serpispaimen.pemkomedan.go.id/index.php/apis/update-sts-rep?access-token=8335b70212059154beab6b9662155d1c";
			url = Activator.getDefault().readFile("apiurlupdate");
			System.out.println(url);
//			url = "http://172.16.73.4:8291/update";
//		String url = "http://202.159.112.161:1103/epaymentapi/api/web/index.php/apis/create-sts?access-token=8335b70212059154beab6b9662155d1c";
		}
		monitor.worked(2);
		
		try {
			HttpURLConnection client = (HttpURLConnection) new URL(url).openConnection();
//		HttpPost post = new HttpPost(url);

			String urlParameters = "No_Pokok_WP="+objSptpd[0]+"&Nama_Pemilik="+objSptpd[1]+"&Alamat_Pemilik="+
					objSptpd[2]+"&Nilai="+objSptpd[3]+"&Denda="+objSptpd[4]+"&Tgl_SPTPD="+objSptpd[6]+
					"&Mata_Anggaran="+objSptpd[7]+"&masa_bayar="+objSptpd[8]+"&jatuh_tempo="+objSptpd[9]+
					"&No_SPTPD="+objSptpd[5];
			if (!oldSts.equalsIgnoreCase(""))
				urlParameters += "&No_STS="+oldSts;
			byte[] postData       = urlParameters.getBytes( StandardCharsets.UTF_8 );

		
			client.setRequestMethod("POST");
			client.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(client.getOutputStream());
			wr.write(postData);
			wr.flush();
			wr.close();
			monitor.worked(3);
			BufferedReader rd = new BufferedReader(
	                        new InputStreamReader(client.getInputStream()));
	
			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
				monitor.worked(10);
			}
			JSONObject jobj = new JSONObject(result.toString());
			if(jobj.get("responseCode").toString().equalsIgnoreCase("00")){ // jika json berhasil
				if(oldSts.equalsIgnoreCase("")){ // insert sts
					String noSts = jobj.get("No_STS").toString();
					System.out.println("No STS :"+noSts);
					String query = "Update Mohon_Detail set STS = '" + noSts + "', STSVALID = '" + objSptpd[9] + "' where IDMOHON = " + idMohon + " AND No_Angsuran = " + cicilan;
					
					boolean resultQuery = db.ExecuteStatementQuery(query, DBConnection.INSTANCE.open());
					if(!resultQuery)
						retValue = false;
					retValue = true;
					monitor.worked(10);
				}else{ // update sts
					String query = "Update MOHON_DETAIL set STSVALID = '" + objSptpd[9] + "' where IDMOHON = " + idMohon + " AND No_Angsuran = " + cicilan;
					boolean resultQuery = db.ExecuteStatementQuery(query, DBConnection.INSTANCE.open());
					if(!resultQuery)
						retValue = false;
					retValue = true;
					monitor.worked(10);
				}
			}else if(jobj.get("responseCode").toString().equalsIgnoreCase("14")){ // jika json gagal
				MessageDialog.openError(Display.getCurrent().getActiveShell(), "Error STS Json", jobj.get("Message").toString());
			}else if(jobj.get("responseCode").toString().equalsIgnoreCase("91")){
				MessageDialog.openError(Display.getCurrent().getActiveShell(), "Error STS Json", "Internal Error. Response Code : " + jobj.get("responseCode").toString());
			}else{ // jika koneksi gagal
				MessageDialog.openError(Display.getCurrent().getActiveShell(), "Error STS Json", "Tidak ada response code dari server. Response Code : " + jobj.get("responseCode").toString());
			}
		} catch (UnsupportedEncodingException e1) {
			monitor.done();
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Encoding Failed", e1.getMessage());
			e1.printStackTrace();
		} catch (ClientProtocolException e) {
			monitor.done();
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Network Failed", e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			monitor.done();
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Read File Failed", e.getMessage());
			e.printStackTrace();
		} catch (JSONException e) {
			monitor.done();
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Error STS Json", e.getMessage());
			e.printStackTrace();
		}
		monitor.done();
		return retValue;
	}
	
	public boolean sendPostDenda(String[] objSptpd, String oldSts, IProgressMonitor monitor){
		monitor.beginTask("Accessing server..", 100);
		if(!ping())
			return false;
		boolean retValue = false;
		
		String url = "";
		if (oldSts.equalsIgnoreCase("")){ //url Insert
			url = Activator.getDefault().readFile("apiurlget");
			System.out.println(url);
//			url = "http://172.16.73.4:8291/home";
		}else{ //url Update
			url = Activator.getDefault().readFile("apiurlupdate");
			System.out.println(url);
		}
		monitor.worked(1);
//			url = "http://172.16.73.4:8291/update";
//		String url = "http://202.159.112.161:1103/epaymentapi/api/web/index.php/apis/create-sts?access-token=8335b70212059154beab6b9662155d1c";

		try {
			HttpURLConnection client = (HttpURLConnection) new URL(url).openConnection();
			String urlParameters = "No_Pokok_WP="+objSptpd[0]+"&Nama_Pemilik="+objSptpd[1]+"&Alamat_Pemilik="+
					objSptpd[2]+"&Nilai="+objSptpd[3]+"&Denda="+objSptpd[4]+"&Tgl_SPTPD="+objSptpd[6]+
					"&Mata_Anggaran="+objSptpd[7]+"&masa_bayar="+objSptpd[8]+"&jatuh_tempo="+objSptpd[9]+
					"&No_SPTPD="+objSptpd[5];
			if (!oldSts.equalsIgnoreCase(""))
				urlParameters += "&No_STS="+oldSts;
			byte[] postData       = urlParameters.getBytes( StandardCharsets.UTF_8 );

			client.setRequestMethod("POST");
			client.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(client.getOutputStream());
			wr.write(postData);
			wr.flush();
			wr.close();
			monitor.worked(3);
	
			BufferedReader rd = new BufferedReader(
	                        new InputStreamReader(client.getInputStream()));
	
			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
				monitor.worked(10);
			}
			
			JSONObject jobj = new JSONObject(result.toString());
			if(jobj.get("responseCode").toString().equalsIgnoreCase("00")){ // jika json berhasil
				if(oldSts.equalsIgnoreCase("")){ // insert sts
					String noSts = jobj.get("No_STS").toString();
					//System.out.println("No STS :"+noSts);
					String queryGetSTS = "";
					String stsPokok = "";
					if (objSptpd[5].contains("SKPDKB")){
						queryGetSTS = "Select STS from Periksa where No_SKP = '" + objSptpd[5] + "' AND NPWPD = '" + objSptpd[0] + "';";
					}else{
						queryGetSTS = "Select STS from SPTPD where No_SPTPD = '" + objSptpd[5] + "' AND NPWPD = '" + objSptpd[0] + "';";
					}
					ResultSet resultOld = db.ResultExecutedStatementQuery(queryGetSTS, DBConnection.INSTANCE.open());
					try {
						if (resultOld.next()){
							stsPokok = resultOld.getString("STS");
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					String query = "";
					if (objSptpd[5].contains("SKPDKB"))
						query = "Update Periksa set STS = '" + stsPokok + "-" + noSts + "', STSVALID = '" + objSptpd[9] + "' where No_SKP = '" + objSptpd[5] + "' AND NPWPD = '" + objSptpd[0] + "'";
					else
						query = "Update SPTPD set STS = '" + stsPokok + "-" + noSts + "', STSVALID = '" + objSptpd[9] + "' where No_SPTPD = '" + objSptpd[5] + "' AND NPWPD = '" + objSptpd[0] + "'";
					boolean resultQuery = db.ExecuteStatementQuery(query, DBConnection.INSTANCE.open());
					if(!resultQuery)
						retValue = false;
					retValue = true;
				}else{ // update sts
					String query = "";
					if (objSptpd[5].contains("SKPDKB"))
						query = "Update Periksa set STSVALID = '" + objSptpd[9] + "' where No_SKP = '" + objSptpd[5] + "' AND NPWPD = '" + objSptpd[0] + "'";
					else
						query = "Update SPTPD set STSVALID = '" + objSptpd[9] + "' where No_SPTPD = '" + objSptpd[5] + "' AND NPWPD = '" + objSptpd[0] + "'";
					boolean resultQuery = db.ExecuteStatementQuery(query, DBConnection.INSTANCE.open());
					if(!resultQuery)
						retValue = false;
					retValue = true;
				}
			}else if(jobj.get("responseCode").toString().equalsIgnoreCase("14")){ // jika json gagal
				MessageDialog.openError(Display.getCurrent().getActiveShell(), "Error STS Json", jobj.get("Message").toString());
			}else if(jobj.get("responseCode").toString().equalsIgnoreCase("91")){
				MessageDialog.openError(Display.getCurrent().getActiveShell(), "Error STS Json", "Internal Error. Response Code : " + jobj.get("responseCode").toString());
			}else{ // jika koneksi gagal
				MessageDialog.openError(Display.getCurrent().getActiveShell(), "Error STS Json", "Tidak ada response code dari server. Response Code : " + jobj.get("responseCode").toString());
			}
		} catch (UnsupportedEncodingException e1) {
			monitor.done();
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Encoding Failed", e1.getMessage());
			e1.printStackTrace();
		} catch (ClientProtocolException e) {
			monitor.done();
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Network Failed", e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			monitor.done();
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Read File Failed", e.getMessage());
			e.printStackTrace();
		} catch (JSONException e) {
			monitor.done();
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Error STS Json", e.getMessage());
			e.printStackTrace();
		}
		monitor.done();
		return retValue;
	}
}