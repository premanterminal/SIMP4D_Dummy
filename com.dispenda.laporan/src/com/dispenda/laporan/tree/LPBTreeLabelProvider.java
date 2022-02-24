package com.dispenda.laporan.tree;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

import com.dispenda.model.BidangUsaha;
import com.dispenda.model.Bulan;
import com.dispenda.model.Hari;
import com.dispenda.model.LaporanRealisasi;
import com.dispenda.model.Pajak;

public class LPBTreeLabelProvider implements ITableLabelProvider {
	private NumberFormat indFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
	@Override
	public void addListener(ILabelProviderListener arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isLabelProperty(Object arg0, String arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Image getColumnImage(Object arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getColumnText(Object arg0, int arg1) {
		switch (arg1){
			case 0:
				if(arg0 instanceof Bulan)
					return((Bulan) arg0).getNamaBulan();
				if(arg0 instanceof Pajak)
					return ((Pajak) arg0).getNamaPajak();
				if(arg0 instanceof BidangUsaha)
					return ((BidangUsaha) arg0).getNamaBidUsaha();
				if(arg0 instanceof Hari)
					return ((Hari) arg0).getTgl();
				if(arg0 instanceof LaporanRealisasi)
					return ((LaporanRealisasi) arg0).getNoReg().toString();
				break;
			case 1:
				if(arg0 instanceof Bulan)
					return "";
				if(arg0 instanceof Pajak)
					return "";
				if(arg0 instanceof BidangUsaha)
					return "";
				if(arg0 instanceof Hari)
					return "";
				if(arg0 instanceof LaporanRealisasi)
					return ((LaporanRealisasi) arg0).getNpwpd();
				break;
			case 2:
				if(arg0 instanceof Bulan)
					return "";
				if(arg0 instanceof Pajak)
					return "";
				if(arg0 instanceof BidangUsaha)
					return "";
				if(arg0 instanceof Hari)
					return "";
				if(arg0 instanceof LaporanRealisasi)
					return ((LaporanRealisasi) arg0).getNamaBadan();
				break;
			case 3:
				if(arg0 instanceof Bulan)
					return "";
				if(arg0 instanceof Pajak)
					return "";
				if(arg0 instanceof BidangUsaha)
					return "";
				if(arg0 instanceof Hari)
					return "";
				if(arg0 instanceof LaporanRealisasi)
					return ((LaporanRealisasi) arg0).getBidUsaha();
				break;
			case 4:
				if(arg0 instanceof Bulan)
					return "";
				if(arg0 instanceof Pajak)
					return "";
				if(arg0 instanceof BidangUsaha)
					return "";
				if(arg0 instanceof Hari)
					return "";
				if(arg0 instanceof LaporanRealisasi)
					return ((LaporanRealisasi) arg0).getNoSKP();
				break;
			case 5:
				if(arg0 instanceof Bulan)
					return "";
				if(arg0 instanceof Pajak)
					return "";
				if(arg0 instanceof BidangUsaha)
					return "";
				if(arg0 instanceof Hari)
					return "";
				if(arg0 instanceof LaporanRealisasi)
					return ((LaporanRealisasi) arg0).getNoSSPD();
				break;
			case 6:
				if(arg0 instanceof Bulan)
					return "";
				if(arg0 instanceof Pajak)
					return "";
				if(arg0 instanceof BidangUsaha)
					return "";
				if(arg0 instanceof Hari)
					return "";
				if(arg0 instanceof LaporanRealisasi)
					return ((LaporanRealisasi) arg0).getBulanSKP();
				break;
			case 7:
				if(arg0 instanceof Bulan)
					return "";
				if(arg0 instanceof Pajak)
					return "";
				if(arg0 instanceof BidangUsaha)
					return "";
				if(arg0 instanceof Hari)
					return "";
				if(arg0 instanceof LaporanRealisasi)
					return ((LaporanRealisasi) arg0).getCicilanKe().toString();
				break;
			case 8:
				if(arg0 instanceof Bulan)
					return "";
				if(arg0 instanceof Pajak)
					return "";
				if(arg0 instanceof BidangUsaha)
					return "";
				if(arg0 instanceof Hari)
					return "";
				if(arg0 instanceof LaporanRealisasi){
					SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
					return sdf.format(((LaporanRealisasi) arg0).getTglBayar());
				}
				break;
			case 9:
				if(arg0 instanceof Bulan)
					return indFormat.format(((Bulan) arg0).getPokok());
				if(arg0 instanceof Pajak)
					return indFormat.format(((Pajak) arg0).getPokok());
				if(arg0 instanceof BidangUsaha)
					return indFormat.format(((BidangUsaha) arg0).getPokok());
				if(arg0 instanceof Hari)
					return indFormat.format(((Hari) arg0).getPokok());
				if(arg0 instanceof LaporanRealisasi)
					return indFormat.format(((LaporanRealisasi) arg0).getPokok());
				break;
			case 10:
				if(arg0 instanceof Bulan)
					return indFormat.format(((Bulan) arg0).getDenda());
				if(arg0 instanceof Pajak)
					return indFormat.format(((Pajak) arg0).getDenda());
				if(arg0 instanceof BidangUsaha)
					return indFormat.format(((BidangUsaha) arg0).getDendaT());
				if(arg0 instanceof Hari)
					return indFormat.format(((Hari) arg0).getDenda());
				if(arg0 instanceof LaporanRealisasi)
					return indFormat.format(((LaporanRealisasi) arg0).getDenda());
				break;
			case 11:
				if(arg0 instanceof Bulan)
					return indFormat.format(((Bulan) arg0).getJumlah());
				if(arg0 instanceof Pajak)
					return indFormat.format(((Pajak) arg0).getJumlah());
				if(arg0 instanceof BidangUsaha)
					return indFormat.format(((BidangUsaha) arg0).getJumlah());
				if(arg0 instanceof Hari)
					return indFormat.format(((Hari) arg0).getJumlah());
				if(arg0 instanceof LaporanRealisasi)
					return indFormat.format(((LaporanRealisasi) arg0).getJumlah());
				break;
		}
		return null;
	}

}
