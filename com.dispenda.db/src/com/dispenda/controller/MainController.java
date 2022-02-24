package com.dispenda.controller;

import com.dispenda.dao.CPArsipDAOImpl;
import com.dispenda.dao.CPArsipLogDAOImpl;
import com.dispenda.dao.CPBidangUsahaDAOImpl;
import com.dispenda.dao.CPDaftarAntrianDAOImpl;
import com.dispenda.dao.CPHistorySubPajak;
import com.dispenda.dao.CPKecamatanDAOImpl;
import com.dispenda.dao.CPKelurahanDAOImpl;
import com.dispenda.dao.CPLaporanDAOImpl;
import com.dispenda.dao.CPModulDAOImpl;
import com.dispenda.dao.CPMohonDAOImpl;
import com.dispenda.dao.CPMohonDetailDAOImpl;
import com.dispenda.dao.CPPajakDAOImpl;
import com.dispenda.dao.CPPejabatDAOImpl;
import com.dispenda.dao.CPPeriksaDAOImpl;
import com.dispenda.dao.CPPeriksaDetailBPKDAOImpl;
import com.dispenda.dao.CPPeriksaDetailDAOImpl;
import com.dispenda.dao.CPSspdDAOImpl;
import com.dispenda.dao.CPSuratIzinUsahaDAOImpl;
import com.dispenda.dao.CPSptpdDAOImpl;
import com.dispenda.dao.CPUUDAOImpl;
import com.dispenda.dao.CPUserDAOImpl;
import com.dispenda.dao.CPWajibPajakDAOImpl;
import com.dispenda.dao.CPWpTutupDAOImpl;
import com.dispenda.dao.DAOFactory;

public class MainController {
	private static CPSspdDAOImpl cpSspdDAOImpl = DAOFactory.getCpSspdDAOImpl();
	private static CPSptpdDAOImpl cpSptpdDAOImpl = DAOFactory.getCpSptpdDAOImpl();
	private static CPBidangUsahaDAOImpl cpBidangUsahaDAOImpl = DAOFactory.getCpBidangUsahaDAOImpl();
	private static CPPejabatDAOImpl cpPejabatDAOImpl = DAOFactory.getCpPejabatDAOImpl();
	private static CPPajakDAOImpl cpPajakDAOImpl = DAOFactory.getCpPajakDAOImpl();
	private static CPUUDAOImpl cpUUDAOImpl = DAOFactory.getCpUUDAOImpl();
	private static CPMohonDAOImpl cpMohonDAOImpl = DAOFactory.getCpMohonDAOImpl();
	private static CPMohonDetailDAOImpl cpMohonDetailDAOImpl = DAOFactory.getCpMohonDetailDAOImpl();
	private static CPPeriksaDAOImpl cpPeriksaDAOImpl = DAOFactory.getCpPeriksaDAOImpl();
	private static CPPeriksaDetailDAOImpl cpPeriksaDetailDAOImpl = DAOFactory.getCpPeriksaDetailDAOImpl();
	private static CPKecamatanDAOImpl cpKecamatanDAOImpl = new CPKecamatanDAOImpl();
	private static CPKelurahanDAOImpl cpKelurahanDAOImpl = new CPKelurahanDAOImpl();
	private static CPDaftarAntrianDAOImpl cpDaftarAntrianDAOImpl = new CPDaftarAntrianDAOImpl();
	private static CPWajibPajakDAOImpl cpWajibPajakDAOImpl = new CPWajibPajakDAOImpl();
	private static CPSuratIzinUsahaDAOImpl cpSuratIzinUsahaDAOImpl = new CPSuratIzinUsahaDAOImpl();
	private static CPModulDAOImpl cpModulDAOImpl = new CPModulDAOImpl();
	private static CPUserDAOImpl cpUserDAOImpl = new CPUserDAOImpl();
	private static CPWpTutupDAOImpl cpWpTutupDAOImpl = new CPWpTutupDAOImpl();
	private static CPLaporanDAOImpl cpLaporanDAOImpl = new CPLaporanDAOImpl();
	private static CPArsipDAOImpl cpArsipDaoImpl = new CPArsipDAOImpl();
	private static CPArsipLogDAOImpl cpArsipLogDaoImpl = new CPArsipLogDAOImpl();
	private static CPPeriksaDetailBPKDAOImpl cpPeriksaDetailBPKDaoImpl = new CPPeriksaDetailBPKDAOImpl();
	private static CPHistorySubPajak cpHistorySubPajak = new CPHistorySubPajak();
	
	public CPModulDAOImpl getCpModulDAOImpl() {
		return cpModulDAOImpl;
	}
	
	public CPUserDAOImpl getCpUserDAOImpl() {
		return cpUserDAOImpl;
	}
	
	public CPSuratIzinUsahaDAOImpl getCpSuratIzinUsahaDAOImpl() {
		return cpSuratIzinUsahaDAOImpl;
	}
	
	public CPWajibPajakDAOImpl getCpWajibPajakDAOImpl() {
		return cpWajibPajakDAOImpl;
	}
	
	public CPDaftarAntrianDAOImpl getCpDaftarAntrianDAOImpl() {
		return cpDaftarAntrianDAOImpl;
	}
	
	public CPKelurahanDAOImpl getCpKelurahanDAOImpl() {
		return cpKelurahanDAOImpl;
	}
	
	public CPKecamatanDAOImpl getCpKecamatanDAOImpl() {
		return cpKecamatanDAOImpl;
	}
	
	public CPSspdDAOImpl getCpSspdDAOImpl(){
		return cpSspdDAOImpl;
	}
	
	public CPSptpdDAOImpl getCpSptpdDAOImpl(){
		return cpSptpdDAOImpl;
	}
	
	public CPBidangUsahaDAOImpl getCpBidangUsahaDAOImpl(){
		return cpBidangUsahaDAOImpl;
	}
	
	public CPPejabatDAOImpl getCpPejabatDAOImpl(){
		return cpPejabatDAOImpl;
	}
	
	public CPPajakDAOImpl getCpPajakDAOImpl(){
		return cpPajakDAOImpl;
	}
	
	public CPUUDAOImpl getCpUUDAOImpl(){
		return cpUUDAOImpl;
	}
	
	public CPMohonDAOImpl getCpMohonDAOImpl(){
		return cpMohonDAOImpl;
	}
	
	public CPMohonDetailDAOImpl getCpMohonDetailDAOImpl(){
		return cpMohonDetailDAOImpl;
	}
	
	public CPPeriksaDAOImpl getCpPeriksaDAOImpl(){
		return cpPeriksaDAOImpl;
	}
	
	public CPPeriksaDetailDAOImpl getCpPeriksaDetailDAOImpl(){
		return cpPeriksaDetailDAOImpl;
	}
	
	public CPWpTutupDAOImpl getCpWpTutupDAOImpl(){
		return cpWpTutupDAOImpl;
	}
	
	public CPLaporanDAOImpl getCpLaporanDAOImpl(){
		return cpLaporanDAOImpl;
	}
	
	public CPArsipDAOImpl getCpArsipDAOImpl(){
		return cpArsipDaoImpl;
	}
	
	public CPArsipLogDAOImpl getCpArsipLogDAOImpl(){
		return cpArsipLogDaoImpl;
	}
	
	public CPPeriksaDetailBPKDAOImpl getCpPeriksaDetailBPKDAOImpl(){
		return cpPeriksaDetailBPKDaoImpl;
	}
	
	public CPHistorySubPajak getCpHistorySubPajak(){
		return cpHistorySubPajak;
	}

}
