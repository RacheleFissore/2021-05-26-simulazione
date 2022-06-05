package it.polito.tdp.yelp.model;

public class ArcoGrafo {
	private String businessId1;
	private String businessId2;
	private double peso;
	private double media1;
	private double media2;
	
	public ArcoGrafo(String businessId1, String businessId2, double media1, double media2) {
		super();
		this.businessId1 = businessId1;
		this.businessId2 = businessId2;
		this.media1 = media1;
		this.media2 = media2;
	}
	public String getBusinessId1() {
		return businessId1;
	}
	public void setBusinessId1(String businessId1) {
		this.businessId1 = businessId1;
	}
	public String getBusinessId2() {
		return businessId2;
	}
	public void setBusinessId2(String businessId2) {
		this.businessId2 = businessId2;
	}
	public double getPeso() {
		return peso;
	}
	public void setPeso(double peso) {
		this.peso = peso;
	}
	public double getMedia1() {
		return media1;
	}
	public void setMedia1(double media1) {
		this.media1 = media1;
	}
	public double getMedia2() {
		return media2;
	}
	public void setMedia2(double media2) {
		this.media2 = media2;
	}
	
	
}
