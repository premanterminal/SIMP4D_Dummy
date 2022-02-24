package com.dispenda.laporan.views;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import com.dispenda.controller.ControllerFactory;

public class ImpSim {
	private static Table table;
	private StyledText styledText;
	private Locale ind = new Locale("id","ID");
	private NumberFormat indFormat = NumberFormat.getCurrencyInstance(ind);

	private Statement connectDb() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException{
		String username = "sa";
		String password = "";
		
		String url = "jdbc:sqlserver://10.10.39.10;databaseName=Medan2017_Akrual";
		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance();
		Connection conn = DriverManager.getConnection(url,username,password);
		Statement statement = conn.createStatement();
		return statement;
	}
	
	/**
	 * @wbp.parser.entryPoint
	 */
	public void open() throws InstantiationException, IllegalAccessException{
		
		Shell shell = new Shell(Display.getCurrent());
		shell.setLayout(new GridLayout(2, false));
		try {
			
			
			final Statement statement = connectDb();
		
		
		Composite composite = new Composite(shell, SWT.NONE);
		composite.setLayout(new GridLayout(3, false));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Button btnBrowse = new Button(composite, SWT.NONE);
		btnBrowse.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog fd = new FileDialog(Display.getCurrent().getActiveShell(), SWT.NONE);
				String filename = fd.open();
				if(filename != null){
					table.removeAll();
					Workbook workbook;
					try {
						workbook = Workbook.getWorkbook(new File(filename));
						Sheet sheet = workbook.getSheet(0);
						Cell totalCell = sheet.findCell("TOTAL");
						for (int i=0;i<totalCell.getRow();i++){
							TableItem item = new TableItem(table, SWT.NONE);
							item.setText(0, sheet.getCell(3, i).getContents());
							item.setText(1, sheet.getCell(4, i).getContents());
							item.setText(2, sheet.getCell(5, i).getContents());
							item.setText(3, sheet.getCell(8, i).getContents() + " " + sheet.getCell(9, i).getContents());
							item.setText(4, sheet.getCell(10, i).getContents());
						}
					}catch (BiffException e1) {
							e1.printStackTrace();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
				}
			
			}
		});
		btnBrowse.setText("Browse");
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		
		table = new Table(composite, SWT.BORDER | SWT.FULL_SELECTION);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		Button btnExecute = new Button(composite, SWT.NONE);
		btnExecute.addSelectionListener(new SelectionAdapter() {
			@SuppressWarnings("deprecation")
			@Override
			public void widgetSelected(SelectionEvent e) {
				int failedCount = 0;
				for (int i=0;i<table.getItemCount();i++){
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String noSKP = table.getItem(i).getText(0);
					Date dt = new Date(Integer.valueOf(table.getItem(i).getText(1).split("/")[2]) - 1900, Integer.valueOf(table.getItem(i).getText(1).split("/")[1]) - 1, Integer.valueOf(table.getItem(i).getText(1).split("/")[0]));
					String tgl = sdf.format(dt);
					int kodeRek4 = Integer.valueOf(table.getItem(i).getText(2).split("[.]")[3]);
					int kodeRek5 = Integer.valueOf(table.getItem(i).getText(2).split("[.]")[4]);
					String ket = table.getItem(i).getText(3);
					String nilai = table.getItem(i).getText(4).replace(",", ".");
					String query = "Insert into Ta_SKP values (2017, '" + noSKP + "', 4, 4, 5, 1, '" + tgl + "', '" + noSKP + "', '" + tgl + "', '" + ket + "')";
					try {
						statement.execute(query);
					} catch (SQLException e1) {
						e1.printStackTrace();
						failedCount++;
						System.out.print("Err no SKP : " + noSKP);
						continue;
					}
					
					String queryRinc = "Insert into Ta_SKP_Rinc values (2017, '" + noSKP + "', 1, 4, 4, 5, 1, 0, 0, 0, 4, 1, 1, " + kodeRek4 + ", " + kodeRek5 + ", " + nilai + ", '"+ ket + "')";
					try {
						statement.execute(queryRinc);
					} catch (SQLException e1) {
						e1.printStackTrace();
						System.out.print("Err no SKP : " + noSKP);
						continue;
					}
				}
				MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Import data selesai. Failed insert : " + failedCount);
			}
		});
		btnExecute.setText("Execute");
		
		Button btnExecDenda = new Button(composite, SWT.NONE);
		btnExecDenda.addSelectionListener(new SelectionAdapter() {
			@SuppressWarnings("deprecation")
			@Override
			public void widgetSelected(SelectionEvent e) {
				for (int i=0;i<table.getItemCount();i++){
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String noSKP = table.getItem(i).getText(0);
					Date dt = new Date(Integer.valueOf(table.getItem(i).getText(1).split("/")[2]) - 1900, Integer.valueOf(table.getItem(i).getText(1).split("/")[1]), Integer.valueOf(table.getItem(i).getText(1).split("/")[0]));
					String tgl = sdf.format(dt);
					int kodeRek4 = Integer.valueOf(table.getItem(i).getText(2).split("[.]")[3]);
					int kodeRek5 = 0;
					String ket = "Denda " + table.getItem(i).getText(3);
					if (ket.toUpperCase().contains("HOTEL")){
						kodeRek5 = 1;
					}
					else if (ket.toUpperCase().contains("RESTORAN")){
						kodeRek5 = 2;
					}
					else if (ket.toUpperCase().contains("HIBURAN")){
						kodeRek5 = 3;
					}
					else if (ket.toUpperCase().contains("PARKIR")){
						kodeRek5 = 7;
					}
					
					String nilai = table.getItem(i).getText(4).replace(",", ".");
					/*String query = "Insert into Ta_SKP values (2016, '" + noSKP + "', 1, 20, 5, 1, '" + tgl + "', '" + noSKP + "', '" + tgl + "', '" + ket + "')";
					try {
						statement.execute(query);
					} catch (SQLException e1) {
						e1.printStackTrace();
						System.out.print("Err no SKP : " + noSKP);
						continue;
					}*/
					
					String queryRinc = "Insert into Ta_SKP_Rinc values (2016, '" + noSKP + "', 2, 1, 20, 5, 1, 0, 0, 0, 4, 1, 4, 7, 2, " + nilai + ", '"+ ket + "')";
					try {
						statement.execute(queryRinc);
					} catch (SQLException e1) {
						e1.printStackTrace();
						System.out.print("Err no SKP : " + noSKP);
						continue;
					}
				}
				MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Import data denda selesai");
			}
		});
		btnExecDenda.setText("Exec Denda");
		
		Button btnPosting = new Button(composite, SWT.NONE);
		btnPosting.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int failedCount = 0;
				for (int i=0;i<table.getItemCount();i++){
//					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String noSKP = table.getItem(i).getText(0);
//					Date dt = new Date(Integer.valueOf(table.getItem(i).getText(1).split("/")[2]) - 1900, Integer.valueOf(table.getItem(i).getText(1).split("/")[1]) - 1, Integer.valueOf(table.getItem(i).getText(1).split("/")[0]));
//					String tgl = sdf.format(dt);
//					int kodeRek4 = Integer.valueOf(table.getItem(i).getText(2).split("[.]")[3]);
//					int kodeRek5 = Integer.valueOf(table.getItem(i).getText(2).split("[.]")[4]);
//					String ket = table.getItem(i).getText(3);
//					String nilai = table.getItem(i).getText(4).replace(",", ".");
					String query = "EXEC SP_Posting_TA_SKP '2017', '" + noSKP + "', '" + noSKP + "'";
					try {
						statement.execute(query);
					} catch (SQLException e1) {
						e1.printStackTrace();
						failedCount++;
						System.out.print("Err no SKP : " + noSKP);
						continue;
					}
					
					/*String queryRinc = "Insert into Ta_SKP_Rinc values (2016, '" + noSKP + "', 1, 1, 20, 5, 1, 0, 0, 0, 4, 1, 1, " + kodeRek4 + ", " + kodeRek5 + ", " + nilai + ", '"+ ket + "')";
					try {
						statement.execute(queryRinc);
					} catch (SQLException e1) {
						e1.printStackTrace();
						System.out.print("Err no SKP : " + noSKP);
						continue;
					}*/
				}
				MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Import data selesai. Failed insert : " + failedCount);
			}
		});
		btnPosting.setText("Posting");
		
		Button btnCompare = new Button(composite, SWT.NONE);
		btnCompare.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int simpeda = 0;
				int simda = 0;
				HashMap<String, String[]> hashMap = getHashMap();
				String query = "select r.No_SKP,r.Nilai,r.No_ID from Ta_SKP as t left join Ta_SKP_rinc as r on t.No_SKP = r.No_SKP where r.Kd_Bidang = 4 and r.Kd_Unit = 5 and r.Kd_Sub = 1";
				try {
					ResultSet rs = statement.executeQuery(query);
					simpeda = hashMap.size();
					while(rs.next()){
						if(hashMap.containsKey(rs.getString("No_SKP").trim())){	
							try{
								String[] result = hashMap.get(rs.getString("No_SKP"));
								if(rs.getInt("No_ID")==1){//Pokok Pajak
									simda++;
									if(!result[0].split(",")[0].equalsIgnoreCase(indFormat.format(rs.getDouble("Nilai")).split(",")[0])){// Jika Nilai Berbeda
										styledText.append("Pokok Pajak : "+rs.getString("No_SKP")+" "+rs.getString("Nilai")+" "+rs.getInt("No_ID")+" Real: "+result[0]+"\n");
									}
								}else{//Denda
									if(!result[1].split(",")[0].equalsIgnoreCase(indFormat.format(rs.getDouble("Nilai")).split(",")[0])){// Jika Nilai Berbeda
										styledText.append("Denda : "+rs.getString("No_SKP")+" "+rs.getString("Nilai")+" "+rs.getInt("No_ID")+" Real: "+result[1]+"\n");
									}
								}
							}catch (NullPointerException npe){
								styledText.append("Tidak jelas : "+rs.getString("No_SKP")+" "+rs.getString("Nilai")+" "+rs.getInt("No_ID")+"\n");
								continue;
							}
							 
						}else{ // Kalau ada No_SKP yang tidak ada dalam list HashMap
							if (rs.getString("No_SKP").contains("SPTPD") || rs.getString("No_SKP").contains("SKPD")){
//								simda++;
								styledText.append("No SKP gak ada: "+rs.getString("No_SKP")+" "+rs.getString("Nilai")+" "+rs.getInt("No_ID")+"\n");
							}
						}
					}
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Selesai. Simpeda : " + simpeda + ", Simda : " + simda);
			}
		});
		btnCompare.setText("Compare");
		
		Button btnUpdate = new Button(composite, SWT.NONE);
		btnUpdate.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int failedCount = 0;
				for (int i=0;i<table.getItemCount();i++){
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String noSKP = table.getItem(i).getText(0);
					Date dt = new Date(Integer.valueOf(table.getItem(i).getText(1).split("/")[2]) - 1900, Integer.valueOf(table.getItem(i).getText(1).split("/")[1]) - 1, Integer.valueOf(table.getItem(i).getText(1).split("/")[0]));
					String tgl = sdf.format(dt);
					int kodeRek4 = Integer.valueOf(table.getItem(i).getText(2).split("[.]")[3]);
					int kodeRek5 = Integer.valueOf(table.getItem(i).getText(2).split("[.]")[4]);
					String ket = table.getItem(i).getText(3);
					String nilai = table.getItem(i).getText(4).replace(",", ".");
					String query = "Update Ta_SKP set tgl_SKP = '" + tgl + "', tgl_ref = '" + tgl + "' where no_skp = '" + noSKP + "'";
//					String query = "EXEC SP_Posting_TA_SKP '2016', '6988/0209/SPTPD/2016', '6988/0209/SPTPD/2016'";
					try {
						statement.executeUpdate(query);
					} catch (SQLException e1) {
						e1.printStackTrace();
						failedCount++;
//						System.out.print("Err no SKP : " + noSKP);
//						continue;
					}
					
					/*String queryRinc = "Insert into Ta_SKP_Rinc values (2016, '" + noSKP + "', 1, 1, 20, 5, 1, 0, 0, 0, 4, 1, 1, " + kodeRek4 + ", " + kodeRek5 + ", " + nilai + ", '"+ ket + "')";
					try {
						statement.execute(queryRinc);
					} catch (SQLException e1) {
						e1.printStackTrace();
						System.out.print("Err no SKP : " + noSKP);
						continue;
					}*/
				}
				MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "BPPRD Kota Medan", "Import data selesai. Failed insert : " + failedCount);
			}
		});
//		btnUpdate.setVisible(false);
		btnUpdate.setText("update");
		new Label(composite, SWT.NONE);
		
		styledText = new StyledText(shell, SWT.BORDER);
		styledText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		createColums();
		} catch (ClassNotFoundException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		shell.open();
	}

	
	private void createColums(){
		String[] listColumn = {"No SKP","Tgl","Kode Rek","Keterangan", "Nilai"};
		int[] columnWidth = {120, 90, 100, 180, 60};
		for (int i=listColumn.length - 1;i>=0;i--)
		{
			TableColumn colPajak = new TableColumn(table, SWT.NONE,0);
			colPajak.setText(listColumn[i]);
			colPajak.setWidth(columnWidth[i]);
		}
	}
	
	@SuppressWarnings("deprecation")
	private HashMap<String,String[]> getHashMap(){
		ResultSet rs = ControllerFactory.getMainController().getCpLaporanDAOImpl().GetListSPTPDbySKP(new Date(2017-1900, 0, 1), new Date(2017-1900, 11, 31), "", "");
		ResultSet rsSk = ControllerFactory.getMainController().getCpLaporanDAOImpl().GetListSKPDKBbySKP(new Date(2017-1900, 0, 1), new Date(2017-1900, 11, 31), "", "");
		HashMap<String,String[]> hashMap = new HashMap<String, String[]>();
		try {
			while(rs.next()){
				hashMap.put(rs.getString("NoKetetapan").trim(), new String[]{indFormat.format(rs.getDouble("PokokPajak")),indFormat.format(rs.getDouble("denda"))});
			}
			
			while(rsSk.next()){
				hashMap.put(rsSk.getString("NoKetetapan").trim(), new String[]{indFormat.format(rsSk.getDouble("PokokPajak")),indFormat.format(rsSk.getDouble("denda"))});
			}
			
			return hashMap;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}
