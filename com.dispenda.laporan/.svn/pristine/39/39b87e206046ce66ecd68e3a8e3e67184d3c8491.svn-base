package com.dispenda.laporan.tree;

import java.util.Calendar;

import com.dispenda.controller.ControllerFactory;
import com.dispenda.model.BidangUsaha;
import com.dispenda.model.BidangUsahaProvider;
import com.dispenda.model.Bulan;
import com.dispenda.model.Hari;
import com.dispenda.model.LaporanRealisasi;
import com.dispenda.model.Pajak;
import com.dispenda.model.PajakProvider;
import com.ibm.icu.text.SimpleDateFormat;

public class InputTreeDetail {
	public Bulan[] bulan;
	private Pajak[] pajak;
	private BidangUsaha[] bidUsaha;
	private Hari[] hari;
	private LaporanRealisasi[] lr;
	public String[] namaBulan = new String[]{"Januari"/*,"Februari","Maret","April","Mei","Juni",
			"Juli","Agustus","September","Oktober","November","Desember"*/};
//	private Integer jlhPajak = PajakProvider.INSTANCE.getPajak();
	
	public InputTreeDetail(int year){
		bulan = new Bulan[1];
		Double totalTahunPokok = new Double(0.0);
		Double totalTahunDenda = new Double(0.0);
		Double totalTahunJumlah = new Double(0.0);
		for(int b=0;b<namaBulan.length;b++){
			long startBulan = System.currentTimeMillis();
			bulan[b] = new Bulan();
			bulan[b].setNamaBulan(namaBulan[b]);
			Double totalBulanPokok = new Double(0.0);
			Double totalBulanDenda = new Double(0.0);
			Double totalBulanJumlah = new Double(0.0);
			pajak = PajakProvider.INSTANCE.getPajak().toArray(new Pajak[0]);
			for(int p=0;p<pajak.length;p++){
				long startPajak = System.currentTimeMillis();
				Double totalPajakPokok = new Double(0.0);
				Double totalPajakDenda = new Double(0.0);
				Double totalPajakJumlah = new Double(0.0);
				bidUsaha = BidangUsahaProvider.INSTANCE.getBidangUsaha(pajak[p].getIdPajak()).toArray(new BidangUsaha[0]);
				for(int bu=0;bu<bidUsaha.length;bu++){
					long startBidUsaha = System.currentTimeMillis();
					Double totalBUPokok = new Double(0.0);
					Double totalBUDenda = new Double(0.0);
					Double totalBUJumlah = new Double(0.0);
					Calendar instance = Calendar.getInstance();
					instance.set(Calendar.DAY_OF_MONTH, 1);
					instance.set(Calendar.MONTH, b);
					instance.set(Calendar.YEAR, year);
					hari = new Hari[instance.getActualMaximum(Calendar.DATE)];
					for(int d=0;d<hari.length;d++){
//						long startHari = System.currentTimeMillis();
						hari[d] = new Hari();
						instance.set(Calendar.DAY_OF_MONTH, d+1);
						SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
						hari[d].setTgl(sdf.format(instance.getTime()));
						Double totalHariPokok = new Double(0.0);
						Double totalHariDenda = new Double(0.0);
						Double totalHariJumlah = new Double(0.0);
						lr = ControllerFactory.getMainController().getCpSspdDAOImpl().getLaporanRealisasi2(bidUsaha[bu].getNamaBidUsaha(), instance.getTime(), instance.getTime()).toArray(new LaporanRealisasi[0]);
						if(lr.length>0){
							for(int i=0;i<lr.length;i++){
								totalHariPokok += lr[i].getPokok();
								totalHariDenda += lr[i].getDenda();
								totalHariJumlah += lr[i].getJumlah();
								hari[d].addLaporanRealisasi(lr[i]);
							}
						}else{
							totalHariPokok += 0;
							totalHariDenda += 0;
							totalHariJumlah += 0;
							hari[d].addLaporanRealisasi(new LaporanRealisasi());
						}
						totalBUPokok += totalHariPokok;
						totalBUDenda += totalHariDenda;
						totalBUJumlah += totalHariJumlah;
						hari[d].setPokok(totalHariPokok);
						hari[d].setDenda(totalHariDenda);
						hari[d].setJumlah(totalHariJumlah);
						bidUsaha[bu].addHari(hari[d]);
//						long endHari = System.currentTimeMillis();
//						System.out.println("Hari: "+((endHari-startHari))+"ms.");
					}
					totalPajakPokok += totalBUPokok;
					totalPajakDenda += totalBUDenda;
					totalPajakJumlah += totalBUJumlah;
					bidUsaha[bu].setPokok(totalBUPokok);
					bidUsaha[bu].setDendaT(totalBUDenda);
					bidUsaha[bu].setJumlah(totalBUJumlah);
					pajak[p].addBidangUsaha(bidUsaha[bu]);
					long endBidUsaha = System.currentTimeMillis();
					System.out.println("Bid Usaha: "+((endBidUsaha-startBidUsaha)/1000)+"s. ");
				}
				totalBulanPokok += totalPajakPokok;
				totalBulanDenda += totalPajakDenda;
				totalBulanJumlah += totalPajakJumlah;
				pajak[p].setPokok(totalPajakPokok);
				pajak[p].setDenda(totalPajakDenda);
				pajak[p].setJumlah(totalPajakJumlah);
				bulan[b].addPajak(pajak[p]);
				long endPajak = System.currentTimeMillis();
				System.out.println("Pajak: "+((endPajak-startPajak)/1000)+"s. ");
			}
			totalTahunPokok += totalBulanPokok;
			totalTahunDenda += totalBulanDenda;
			totalTahunJumlah += totalBulanJumlah;
			bulan[b].setPokok(totalBulanPokok);
			bulan[b].setDenda(totalBulanDenda);
			bulan[b].setJumlah(totalBulanJumlah);
			long endBulan = System.currentTimeMillis();
			System.out.println("Bulan: "+((endBulan-startBulan)/1000)+"s. ");
		}
	}

}
