package com.dispenda.dao;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;

import com.dispenda.database.DBConnection;
import com.dispenda.database.DBOperation;
import com.dispenda.model.ArsipLog;

public class CPArsipLogDAOImpl {
	private DBOperation db = new DBOperation();
	
	public boolean SaveArsipLog(ArsipLog logModel){
		String query = "Insert into Log_Arsip values (null, '" + 
						logModel.getNoSKP() + "', '" +
						logModel.getNpwpd() + "', '" +
						logModel.getTanggal() + "', '" +
						logModel.getPeminjam() + "', '" +
						logModel.getKeterangan() + "', " +
						logModel.getStart() + ", " +
						logModel.getEnd() + ")";
		boolean result = db.ExecuteStatementQuery(query, DBConnection.INSTANCE.open());
		if(!result)
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada MERGE(UPDATE/INSERT) tabel Arsip.");
		return result;
	}
}
