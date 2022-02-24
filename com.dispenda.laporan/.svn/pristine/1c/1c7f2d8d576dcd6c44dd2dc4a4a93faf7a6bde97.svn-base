package com.dispenda.laporan.tree;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
	public Vector getList(Date masaDari, Date masaSampai,Integer index){
		List<Pajak> listPajak = new ArrayList<Pajak>();
		if(index > -1){
			Pajak pajak = PajakProvider.INSTANCE.getSelectedPajak(index);
			listPajak.add(pajak);
		}else{
			listPajak = PajakProvider.INSTANCE.getPajak();
		}
		for(int p=0;p<listPajak.size();p++){
			Double totalPajakPokok = new Double(0.0);
			Double totalPajakDenda = new Double(0.0);
			Double totalPajakJumlah = new Double(0.0);
			pajak = new Node(listPajak.get(p).getNamaPajak(), totalPajakPokok, totalPajakDenda, totalPajakJumlah, null);
			List<BidangUsaha> bidUsaha = BidangUsahaProvider.INSTANCE.getBidangUsaha(listPajak.get(p).getIdPajak());
			for(int bu=0;bu<bidUsaha.size();bu++){
				Double totalBUPokok = new Double(0.0);
				Double totalBUDenda = new Double(0.0);
				Double totalBUJumlah = new Double(0.0);
				//Date tgl = new Date(Date.parse(sdf.format(masaDari)));
				bidUsahaNode = new Node(bidUsaha.get(bu).getNamaBidUsaha(), totalBUPokok, totalBUDenda, totalPajakJumlah, pajak);
				List<LaporanRealisasi> lr = ControllerFactory.getMainController().getCpSspdDAOImpl().getLaporanRealisasiRekap(bidUsaha.get(bu).getNamaBidUsaha(), masaDari, masaSampai);
				if(lr.size()>0){
					totalBUPokok += lr.get(0).getPokok();
					totalBUDenda += lr.get(0).getDenda();
					totalBUJumlah += lr.get(0).getJumlah();
				}else{
					totalBUPokok += 0;
					totalBUDenda += 0;
					totalBUJumlah += 0;
				}
				totalPajakPokok += totalBUPokok;
				totalPajakDenda += totalBUDenda;
				totalPajakJumlah += totalBUJumlah;
				bidUsahaNode.setPokok(totalBUPokok);
				bidUsahaNode.setDenda(totalBUDenda);
				bidUsahaNode.setJumlah(totalBUJumlah);
				Display.getDefault().syncExec(new Runnable() {
					public void run() {
						ProgressBarProvider.INSTANCE.setProcess("Load Bidang Usaha : "+bidUsahaNode.getName());
					}
				});
			}
			pajak.setPokok(totalPajakPokok);
			pajak.setDenda(totalPajakDenda);
			pajak.setJumlah(totalPajakJumlah);
			Display.getDefault().syncExec(new Runnable() {
				public void run() {
					ProgressBarProvider.INSTANCE.setProcess("Load Pajak : "+pajak.getName());
				}
			});
			nodes.add(pajak);
		}
		return nodes;
	}
}
