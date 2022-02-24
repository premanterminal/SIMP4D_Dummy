package local.service.servlet;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(
		name = "UpdateStsServlet",
		urlPatterns = {"/update"}
)
public class UpdateStsServlet extends HttpServlet{
//	private String domain = "172.16.73.4:8291";
	//private String domain = "http://192.168.102.2:1987/sts/update?access-token=8335b70212059154beab6b9662155d1c";
	private String domain = "http://182.23.79.66/sts_service/index.php/create-umum?access-token=20d47dbbbc1fb82cdc38f93a2b6db471";	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//		String url = "http://"+domain+"/update";
		String url = domain;
		
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		//add reuqest header
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", "Mozilla/5.0");
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		con.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded");
		con.setDoOutput(true);
		
		String urlParameters = "No_Pokok_WP="+req.getParameter("No_Pokok_WP")+"&Nama_Pemilik="+req.getParameter("Nama_Pemilik")+"&Alamat_Pemilik="+
				req.getParameter("Alamat_Pemilik")+"&Nilai="+req.getParameter("Nilai")+"&Denda="+req.getParameter("Denda")+"&Tgl_SPTPD="+req.getParameter("Tgl_SPTPD")+
				"&Mata_Anggaran="+req.getParameter("Mata_Anggaran")+"&masa_bayar="+req.getParameter("masa_bayar")+"&jatuh_tempo="+req.getParameter("jatuh_tempo")+
				"&No_SPTPD="+req.getParameter("No_SPTPD")+"&No_STS="+req.getParameter("No_STS");
		
		DataOutputStream wr;
		wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();

		int responseCode = con.getResponseCode();

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		resp.getWriter().write(response.toString());
//		JsonArray jArray = JSONObject.readFrom(response.toString());
	}
}
