package com.dispenda.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;

import com.dispenda.database.DBConnection;
import com.dispenda.database.DBOperation;
import com.dispenda.model.HistorySubPajak;

public class CPHistorySubPajak {
	private DBOperation db = new DBOperation();
	
	public boolean saveHistory(HistorySubPajak historyModel){
		String query = "Insert into History_SubPajak (id, npwpd, subpajak_lama, subpajak_baru, tanggal) values(" +
				""+historyModel.getId()+"," +
				"'"+historyModel.getNpwpd()+"'," +
				""+historyModel.getIdSubPajakLama()+"," +
				""+historyModel.getIdSubPajakBaru()+"," +
				"'"+historyModel.getTanggal()+"')";
		boolean result = db.ExecuteStatementQuery(query, DBConnection.INSTANCE.open());
		if(!result)
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database Error", "Ada masalah pada MERGE(UPDATE/SET) tabel History Sub Pajak.");
		return result;
	}
	
	public boolean checkSubPajak(String npwpd, Integer subPajak){
		boolean retValue = false;
		String query = "Select * from wajib_pajak where npwpd = '" + npwpd + "' and idsub_pajak = " + subPajak;
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		try {
			if(result.next())
				retValue = true;
			else
				retValue = false;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return retValue;
	}
	
	public List<HistorySubPajak> getHistory(String npwpd){
		List<HistorySubPajak> listHistory = new ArrayList<>();
		String query = "select * from history_subpajak where npwpd = '" + npwpd + "' order by tanggal;";
		ResultSet result = db.ResultExecutedStatementQuery(query, DBConnection.INSTANCE.open());
		if(result!=null){
			try{
				while(result.next()){
					HistorySubPajak historyModel = new HistorySubPajak();
					historyModel.setId(result.getInt("id"));
					historyModel.setNpwpd(result.getString("npwpd"));
					historyModel.setIdSubPajakBaru(result.getInt("SubPajak_Baru"));
					historyModel.setIdSubPajakLama(result.getInt("SubPajak_Lama"));
					historyModel.setTanggal(result.getDate("tanggal"));
					listHistory.add(historyModel);
				}
				return listHistory;
			}catch(Exception ex){
				MessageDialog.openError(Display.getCurrent().getActiveShell(), "Database SQL Error", ex.getMessage());
				return null;
			}
		}else
			return null;
	}
}
