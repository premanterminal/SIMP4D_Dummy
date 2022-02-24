package local.service.servlet;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(
		name = "CreateStsServlet",
		urlPatterns = {"/create"}
)
public class CreateStsServlet extends HttpServlet{
//	private String domain = "172.16.73.4:8291";
	//private String domain = "http://192.168.102.2:1987/sts/create?access-token=8335b70212059154beab6b9662155d1c";
	private String domain = "http://182.23.79.66/sts_service/index.php/create-umum?access-token=20d47dbbbc1fb82cdc38f93a2b6db471";
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String url = domain;
		URL obj = new URL(url);

		String urlParameters = "No_Pokok_WP="+req.getParameter("No_Pokok_WP")+"&Nama_Pemilik="+req.getParameter("Nama_Pemilik")+"&Alamat_Pemilik="+
				req.getParameter("Alamat_Pemilik")+"&Nilai="+req.getParameter("Nilai")+"&Denda="+req.getParameter("Denda")+"&Tgl_SPTPD="+req.getParameter("Tgl_SPTPD")+
				"&Mata_Anggaran="+req.getParameter("Mata_Anggaran")+"&masa_bayar="+req.getParameter("masa_bayar")+"&jatuh_tempo="+req.getParameter("jatuh_tempo")+
				"&No_SPTPD="+req.getParameter("No_SPTPD");
		
		byte[] postData       = urlParameters.getBytes( StandardCharsets.UTF_8 );
		int    postDataLength = postData.length;
		HttpURLConnection conn= (HttpURLConnection) obj.openConnection();           
		conn.setDoOutput( true );
		conn.setInstanceFollowRedirects( false );
		conn.setRequestMethod( "POST" );
		conn.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded"); 
		conn.setRequestProperty( "charset", "utf-8");
		conn.setRequestProperty( "Content-Length", Integer.toString( postDataLength ));
		conn.setUseCaches( false );
		DataOutputStream wr;
		wr = new DataOutputStream(conn.getOutputStream());
		wr.write(postData);
		wr.flush();
		wr.close();
		
		BufferedReader in = new BufferedReader(
		        new InputStreamReader(conn.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		resp.getWriter().write(response.toString());
	}
}
