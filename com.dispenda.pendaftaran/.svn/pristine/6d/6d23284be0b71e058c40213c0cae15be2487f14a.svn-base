package com.dispenda.pendaftaran.tree;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import org.eclipse.swt.widgets.Display;

import com.dispenda.controller.ControllerFactory;
import com.dispenda.model.BidangUsaha;
import com.dispenda.model.BidangUsahaProvider;
import com.dispenda.model.LaporanRealisasi;
import com.dispenda.model.Pajak;
import com.dispenda.model.PajakProvider;
import com.dispenda.widget.ProgressBarProvider;

public class InputTreeRekap {
	@SuppressWarnings("rawtypes")
	private Vector nodes = new Vector();
	@SuppressWarnings("unused")
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	private Node bidUsahaNode;
	private Node pajak;
	
	public InputTreeRekap() {
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Vector getList(Integer index){
		List<Pajak> listPajak = new ArrayList<Pajak>();
		if(index > -1){
			Pajak pajak = PajakProvider.INSTANCE.getSelectedPajak(index);
			listPajak.add(pajak);
		}else{
			listPajak = PajakProvider.INSTANCE.getPajak();
		}
		for(int p=0;p<listPajak.size();p++){
			int totalAktif = 0;
			int totalNonAktif = 0;
			int totalTutup = 0;
			int totalJumlah = 0;
			pajak = new Node(listPajak.get(p).getNamaPajak(), totalAktif, totalNonAktif, totalTutup, totalJumlah, null);
			List<BidangUsaha> bidUsaha = BidangUsahaProvider.INSTANCE.getBidangUsaha(listPajak.get(p).getIdPajak());
			for(int bu=0;bu<bidUsaha.size();bu++){
				int totalBUAktif = 0;
				int totalBUNonAktif = 0;
				int totalBUTutup = 0;
				int totalBUJumlah = 0;
				//Date tgl = new Date(Date.parse(sdf.format(masaDari)));
				bidUsahaNode = new Node(bidUsaha.get(bu).getNamaBidUsaha(), totalBUAktif, totalBUNonAktif, totalBUTutup, totalBUJumlah, pajak);
				HashMap<Integer, List<Integer>> lr = ControllerFactory.getMainController().getCpWajibPajakDAOImpl().getDataWP(bidUsaha.get(bu).getIdSubPajak());
				if(lr.size()>0){
					totalBUAktif += lr.get(0).get(0);
					totalBUNonAktif += lr.get(0).get(1);
					totalBUTutup += lr.get(0).get(2);
					totalBUJumlah += lr.get(0).get(3);
				}else{
					totalBUAktif += 0;
					totalBUNonAktif += 0;
					totalBUTutup += 0;
					totalBUJumlah += 0;
				}
				totalAktif += totalBUAktif;
				totalNonAktif += totalBUNonAktif;
				totalTutup += totalBUTutup;
				totalJumlah += totalBUJumlah;
				bidUsahaNode.setAktif(totalBUAktif);
				bidUsahaNode.setNonAktif(totalBUNonAktif);
				bidUsahaNode.setTutup(totalBUTutup);
				bidUsahaNode.setTotal(totalBUJumlah);
				/*Display.getDefault().syncExec(new Runnable() {
					public void run() {
						ProgressBarProvider.INSTANCE.setProcess("Load Bidang Usaha : "+bidUsahaNode.getName());
					}
				});*/
			}
			pajak.setAktif(totalAktif);
			pajak.setNonAktif(totalNonAktif);
			pajak.setTutup(totalTutup);
			pajak.setTotal(totalJumlah);
			/*Display.getDefault().syncExec(new Runnable() {
				public void run() {
					ProgressBarProvider.INSTANCE.setProcess("Load Pajak : "+pajak.getName());
				}
			});*/
			nodes.add(pajak);
		}
		return nodes;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Vector getListInsidentil(Integer index){
		List<Pajak> listPajak = new ArrayList<Pajak>();
		if(index > -1){
			Pajak pajak = PajakProvider.INSTANCE.getSelectedPajak(index);
			listPajak.add(pajak);
		}else{
			listPajak = PajakProvider.INSTANCE.getPajak();
		}
		for(int p=0;p<listPajak.size();p++){
			int totalAktif = 0;
			int totalNonAktif = 0;
			int totalTutup = 0;
			int totalJumlah = 0;
			pajak = new Node(listPajak.get(p).getNamaPajak(), totalAktif, totalNonAktif, totalTutup, totalJumlah, null);
			List<BidangUsaha> bidUsaha = BidangUsahaProvider.INSTANCE.getBidangUsaha(listPajak.get(p).getIdPajak());
			for(int bu=0;bu<bidUsaha.size();bu++){
				int totalBUAktif = 0;
				int totalBUNonAktif = 0;
				int totalBUTutup = 0;
				int totalBUJumlah = 0;
				//Date tgl = new Date(Date.parse(sdf.format(masaDari)));
				bidUsahaNode = new Node(bidUsaha.get(bu).getNamaBidUsaha(), totalBUAktif, totalBUNonAktif, totalBUTutup, totalBUJumlah, pajak);
				HashMap<Integer, List<Integer>> lr = ControllerFactory.getMainController().getCpWajibPajakDAOImpl().getDataWPInsidentil(bidUsaha.get(bu).getIdSubPajak());
				if(lr.size()>0){
					totalBUAktif += lr.get(0).get(0);
					totalBUNonAktif += lr.get(0).get(1);
					totalBUTutup += lr.get(0).get(2);
					totalBUJumlah += lr.get(0).get(3);
				}else{
					totalBUAktif += 0;
					totalBUNonAktif += 0;
					totalBUTutup += 0;
					totalBUJumlah += 0;
				}
				totalAktif += totalBUAktif;
				totalNonAktif += totalBUNonAktif;
				totalTutup += totalBUTutup;
				totalJumlah += totalBUJumlah;
				bidUsahaNode.setAktif(totalBUAktif);
				bidUsahaNode.setNonAktif(totalBUNonAktif);
				bidUsahaNode.setTutup(totalBUTutup);
				bidUsahaNode.setTotal(totalBUJumlah);
				/*Display.getDefault().syncExec(new Runnable() {
					public void run() {
						ProgressBarProvider.INSTANCE.setProcess("Load Bidang Usaha : "+bidUsahaNode.getName());
					}
				});*/
			}
			pajak.setAktif(totalAktif);
			pajak.setNonAktif(totalNonAktif);
			pajak.setTutup(totalTutup);
			pajak.setTotal(totalJumlah);
			/*Display.getDefault().syncExec(new Runnable() {
				public void run() {
					ProgressBarProvider.INSTANCE.setProcess("Load Pajak : "+pajak.getName());
				}
			});*/
			nodes.add(pajak);
		}
		return nodes;
	}
}
