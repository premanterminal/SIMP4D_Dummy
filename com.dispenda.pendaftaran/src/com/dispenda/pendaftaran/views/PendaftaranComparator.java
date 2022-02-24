package com.dispenda.pendaftaran.views;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;

import com.dispenda.model.PendaftaranWajibPajak;

public class PendaftaranComparator extends ViewerComparator {
  private int propertyIndex;
  private static final int DESCENDING = 1;
  private int direction = DESCENDING;

  public PendaftaranComparator() {
    this.propertyIndex = 0;
    direction = DESCENDING;
  }

  public int getDirection() {
    return direction == 1 ? SWT.DOWN : SWT.UP;
  }

  public void setColumn(int column) {
    if (column == this.propertyIndex) {
      // Same column as last sort; toggle the direction
      direction = 1 - direction;
    } else {
      // New column; do an ascending sort
      this.propertyIndex = column;
      direction = DESCENDING;
    }
  }

  @Override
  public int compare(Viewer viewer, Object e1, Object e2) {
	PendaftaranWajibPajak p1 = (PendaftaranWajibPajak) e1;
    PendaftaranWajibPajak p2 = (PendaftaranWajibPajak) e2;
    int rc = 0;
    switch (propertyIndex) {
    case 0:
    	rc = p1.getNoUrut().compareTo(p2.getNoUrut());
      break;
    case 1:
    	if(p1.getAlabadKecamatan() == null){
    		p1.setAlabadKecamatan("");
    		if (p2.getAlabadKecamatan() == null)
    			p2.setAlabadKecamatan("");
    		rc = p1.getAlabadKecamatan().compareTo(p2.getAlabadKecamatan());
    	}else if(p2.getAlabadKecamatan() == null){
    		p2.setAlabadKecamatan("");
    		rc = p1.getAlabadKecamatan().compareTo(p2.getAlabadKecamatan());
    	}else
    		rc = p1.getAlabadKecamatan().compareTo(p2.getAlabadKecamatan());
    	break;
    case 2:
    	if(p1.getAlabadKelurahan() == null){
    		p1.setAlabadKelurahan("");
    		if (p2.getAlabadKelurahan() == null)
    			p2.setAlabadKelurahan("");
    		rc = p1.getAlabadKelurahan().compareTo(p2.getAlabadKelurahan());
    	}else if(p2.getAlabadKelurahan() == null){
    		p2.setAlabadKelurahan("");
    		rc = p1.getAlabadKelurahan().compareTo(p2.getAlabadKelurahan());
    	}else
    		rc = p1.getAlabadKelurahan().compareTo(p2.getAlabadKelurahan());
    	break;
    case 3:
    	rc = p1.getNamaPajak().compareTo(p2.getNamaPajak());
      	break;
    default:
      rc = 0;
    }
    // If descending order, flip the direction
    if (direction == DESCENDING) {
      rc = -rc;
    }
    return rc;
  }

} 
