package com.dispenda.laporan.views;

import java.io.File;
import java.io.IOException;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public abstract class TestExcel {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Workbook workbook = Workbook.getWorkbook(new File("D:/DataExport/lina keuangan/skpd akhir des/skpd air tanah akhir des.xls"));
			Sheet sheet = workbook.getSheet(0);
			Cell cell = sheet.getCell(1, 4); // (Column,Row) NoKetetapan
			cell = sheet.getCell(3, 4); // Tanggal Ketetapan
			cell = sheet.getCell(4, 4); // No. Rekening
			cell = sheet.getCell(5, 4); // Pokok Pajak
			cell = sheet.getCell(6, 4); // Denda
			cell = sheet.getCell(7, 4); // Keterangan
			System.out.println(cell.getContents());
			workbook.close();
		} catch (BiffException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
