package com.dispenda.pendaftaran.views;

import java.text.SimpleDateFormat;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.widgets.List;

import com.dispenda.model.PendaftaranWajibPajak;

public class PendaftaranFilter extends ViewerFilter {

	private String strNPWP = "",strNamaBadan="", strAlamat="",strTahunTerbit="",strBulanTerbit="",
		  strTanggalTerbit="",strKecamatan="",strKelurahan="",
		  strNamaPajak="",strBidUsaha="",strStatus="", strNamaPemilik = "", strInsidentil = "", strKeterangan = "";
//	private Boolean bolInsidentil = false;
	private SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
	private int length = 0;
	private String[] listString = new String[]{"Semua","NPWP","Nama Badan","Nama Pemilik","Alamat","Tahun Terbit","Bulan Terbit","Tanggal Terbit","Kecamatan","Kelurahan","Nama Pajak","Bidang Usaha","Status", "Insidentil", "Keterangan"};
	private List list;
	
	public PendaftaranFilter(List list) {
		this.list = list;
	}
  
	public void setSearchText(String s, List list) {
		this.list = list;
		String[] splitter = s.split(";");
		length = list.getSelectionCount();
		clearAll();
		for(int i=0;i<length;i++){
			if(list.getItem(list.getSelectionIndices()[i]).matches(listString[0])){
				break;
			}else if(list.getItem(list.getSelectionIndices()[i]).matches(listString[1])){
				strNPWP = splitter[i];	
			}else if(list.getItem(list.getSelectionIndices()[i]).matches(listString[2])){
				strNamaBadan = splitter[i];
			}else if(list.getItem(list.getSelectionIndices()[i]).matches(listString[3])){
				strNamaPemilik = splitter[i];
			}else if(list.getItem(list.getSelectionIndices()[i]).matches(listString[4])){
				strAlamat = splitter[i];
			}else if(list.getItem(list.getSelectionIndices()[i]).matches(listString[5])){
				strTahunTerbit = splitter[i];
			}else if(list.getItem(list.getSelectionIndices()[i]).matches(listString[6])){
				strBulanTerbit = splitter[i];
			}else if(list.getItem(list.getSelectionIndices()[i]).matches(listString[7])){
				strTanggalTerbit = splitter[i];
			}else if(list.getItem(list.getSelectionIndices()[i]).matches(listString[8])){
				strKecamatan = splitter[i];
			}else if(list.getItem(list.getSelectionIndices()[i]).matches(listString[9])){
				strKelurahan = splitter[i];
			}else if(list.getItem(list.getSelectionIndices()[i]).matches(listString[10])){
				strNamaPajak = splitter[i];
			}else if(list.getItem(list.getSelectionIndices()[i]).matches(listString[11])){
				strBidUsaha = splitter[i];
			}else if(list.getItem(list.getSelectionIndices()[i]).matches(listString[12])){
				strStatus = splitter[i];
			}else if(list.getItem(list.getSelectionIndices()[i]).matches(listString[13])){
				strInsidentil = splitter[i];
			}else if(list.getItem(list.getSelectionIndices()[i]).matches(listString[14])){
				strKeterangan = splitter[i];
			}
		}
//		this.searchString = ".*" + s + ".*";
	}
  
	private void clearAll(){
		strNamaBadan="";strNPWP = "";strAlamat="";strTahunTerbit="";strBulanTerbit="";strTanggalTerbit="";
		strKecamatan="";strKelurahan="";strNamaPajak="";strBidUsaha="";strStatus="";strNamaPemilik="";strInsidentil="";strKeterangan="";
	}
  
		int hitung = 0;
		@SuppressWarnings("deprecation")
		@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
//    if (searchString == null || searchString.length() == 0) {
//      return true;
//    }
	  	int count = 0;
	  	PendaftaranWajibPajak p = (PendaftaranWajibPajak) element;
	  	if(list.getItem(list.getSelectionIndex()).equalsIgnoreCase("Semua")){
	  		count++;
	  	}
	  	if(strNPWP.isEmpty()){
	  		if(p.getNPWP().equalsIgnoreCase(strNPWP)){
		 		count++;
		  	}
	  	}else{
	  		if (p.getNPWP().substring(1, 8).equalsIgnoreCase(String.format("%07d", Integer.parseInt(strNPWP)))) {
	  			count++;
			}
	  	}
	  	if (p.getNamaBadan().toUpperCase().matches("(?i).*"+strNamaBadan.toUpperCase()+".*")&&!strNamaBadan.isEmpty()) {
	  		count++;
	  	}
	  	if (p.getNamaPemilik().toUpperCase().matches("(?i).*"+strNamaPemilik.toUpperCase()+".*")&&!strNamaPemilik.isEmpty()) {
	  		count++;
	  	}
	  	if (p.getAlabadJalan().toUpperCase().matches("(?i).*"+strAlamat.toUpperCase()+".*")&&!strAlamat.isEmpty()) {
	  		count++;
	  	}
	  	if (Integer.toString(p.getTanggalDaftar().getYear()+1900).equalsIgnoreCase(strTahunTerbit)&&!strTahunTerbit.isEmpty()) {
	  		count++;
	  	}
	  	if ((String.format("%02d", (p.getTanggalDaftar().getMonth()+1))+"-"+Integer.toString(p.getTanggalDaftar().getYear()+1900)).equalsIgnoreCase(strBulanTerbit)&&!strBulanTerbit.isEmpty()) {
	  		count++;
	  	}
	  	if (!strTanggalTerbit.isEmpty()) {
	  		if(strTanggalTerbit.contains("-")){
	  			if(sdf.format(p.getTanggalDaftar()).equalsIgnoreCase(strTanggalTerbit))
	  				count++;
	  		}else{
	  			if(Integer.parseInt(sdf.format(p.getTanggalDaftar()).substring(0, 2))==Integer.parseInt(strTanggalTerbit))
		  			count++;	
	  		}
	  	}
	  	if (p.getAlabadKecamatan().equalsIgnoreCase(strKecamatan)&&!strKecamatan.isEmpty()) {
	  		count++;
	  	}
	  	if (p.getAlabadKelurahan().equalsIgnoreCase(strKelurahan)&&!strKelurahan.isEmpty()) {
	  		count++;
	  	}
	  	if (p.getNamaPajak().equalsIgnoreCase(strNamaPajak)&&!strNamaPajak.isEmpty()) {
	  		count++;
	  	}
	  	if (!(p.getNamaBidangUsaha() == null)){
	  		if (p.getNamaBidangUsaha().toLowerCase().contains(strBidUsaha.toLowerCase())&&!strBidUsaha.isEmpty()) {
		  		count++;
		  	}
	  	}
	  	if (p.getStatus().equalsIgnoreCase(strStatus)&&!strStatus.isEmpty()) {
	  		count++;
	  	}
    	if (p.getInsidentil()== (!strInsidentil.equalsIgnoreCase("0")) && !strInsidentil.isEmpty()){
    		count++;
    	}
    	if (p.getKeterangan().contains(strKeterangan)&&!strKeterangan.isEmpty()) {
	  		count++;
	  	}
	  	if(length == count)
	  		return true;
	  	else
	  		return false;
	}
} 
