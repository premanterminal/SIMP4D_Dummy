package com.dispenda.pendaftaran.tree;

import java.util.Vector;

public class Node {
	private String name = "";
	private Integer aktif = 0;
	private Integer nonAktif = 0;
	private Integer tutup = 0;
	private Integer total = 0;
	@SuppressWarnings("rawtypes")
	private Vector subCategories;
	private Node parent;
	
	@SuppressWarnings("rawtypes")
	public Vector getSubCategories() {
		return subCategories;
	}
	
	public Node(String name, Integer aktif, Integer nonAktif, Integer tutup, Integer total, Node parent) {
		this.name = name;
		this.aktif = aktif;
		this.nonAktif = nonAktif;
		this.tutup = tutup;
		this.total = total;
		this.parent = parent;
		if(parent != null)
			parent.addSubCategory(this);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void addSubCategory(Node subCategory){
		if(subCategories == null)
			subCategories = new Vector();
		if(!subCategories.contains(subCategory))
			subCategories.add(subCategory);
	}
	
	public String getName() {
		return name;
	}
	
	public Integer getAktif() {
		return aktif;
	}
	
	public Integer getNonAktif() {
		return nonAktif;
	}
	
	public Integer getTutup() {
		return tutup;
	}
	
	public Integer getTotal() {
		return total;
	}
	
	public Node getParent() {
		return parent;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setParent(Node parent) {
		this.parent = parent;
	}
	
	public void setAktif(int aktif) {
		this.aktif = aktif;
	}
	
	public void setNonAktif(int nonAktif) {
		this.nonAktif = nonAktif;
	}
	
	public void setTutup(int tutup) {
		this.tutup = tutup;
	}
	
	public void setTotal(int total) {
		this.total = total;
	}
}
