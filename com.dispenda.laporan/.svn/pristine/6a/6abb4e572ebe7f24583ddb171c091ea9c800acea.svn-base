package com.dispenda.laporan.tree;

import java.util.Vector;

public class Node {
	private String name = "";
	private Double pokok = new Double(0.0);
	private Double denda = new Double(0.0);
	private Double jumlah = new Double(0.0);
	@SuppressWarnings("rawtypes")
	private Vector subCategories;
	private Node parent;
	
	@SuppressWarnings("rawtypes")
	public Vector getSubCategories() {
		return subCategories;
	}
	
	public Node(String name, Double pokok, Double denda, Double jumlah, Node parent) {
		this.name = name;
		this.pokok = pokok;
		this.denda = denda;
		this.jumlah = jumlah;
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
	
	public Double getDenda() {
		return denda;
	}
	
	public Double getJumlah() {
		return jumlah;
	}
	
	public Node getParent() {
		return parent;
	}
	
	public Double getPokok() {
		return pokok;
	}
	
	public void setDenda(Double denda) {
		this.denda = denda;
	}
	
	public void setJumlah(Double jumlah) {
		this.jumlah = jumlah;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setParent(Node parent) {
		this.parent = parent;
	}
	
	public void setPokok(Double pokok) {
		this.pokok = pokok;
	}
}
