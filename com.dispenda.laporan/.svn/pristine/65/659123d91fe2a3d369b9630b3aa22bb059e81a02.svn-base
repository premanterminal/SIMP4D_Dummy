package com.dispenda.laporan.tree;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.dispenda.model.BidangUsaha;
import com.dispenda.model.Bulan;
import com.dispenda.model.Hari;
import com.dispenda.model.Pajak;

public class LPBTreeContentProvider implements ITreeContentProvider {
	private static final Object[] EMPTY = new Object[] {};
	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void inputChanged(Viewer arg0, Object arg1, Object arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object[] getChildren(Object arg0) {
		if(arg0 instanceof Bulan)
			return ((Bulan) arg0).getPajak().toArray();
		if(arg0 instanceof Pajak)
			return ((Pajak) arg0).getBidUsaha().toArray();
		if(arg0 instanceof BidangUsaha)
			return ((BidangUsaha) arg0).getHari().toArray();
		if(arg0 instanceof Hari)
			return ((Hari) arg0).getLaporanRealisasi().toArray();
		return EMPTY;
	}

	@Override
	public Object[] getElements(Object arg0) {
		return ((InputTreeDetail) arg0).bulan;
	}

	@Override
	public Object getParent(Object arg0) {
		return ((Bulan) arg0).getJumlah();
	}

	@Override
	public boolean hasChildren(Object arg0) {
		return getChildren(arg0).length>0;
	}

}
