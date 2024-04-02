package com.spdcl.model;

public class RelisationDetails {

	private String circle;
	private String cyCategory;
	private String cyNos;
	private String cySales;
	private String cyRevenue;
	private String pyCategory;
	private String pyNos;
	private String pySales;
	private String pyRevenue;
	
	public RelisationDetails(String circle, String cyCategory, String cyNos, String cySales, String cyRevenue,
			String pyCategory, String pyNos, String pySales, String pyRevenue) {
		super();
		this.circle = circle;
		this.cyCategory = cyCategory;
		this.cyNos = cyNos;
		this.cySales = cySales;
		this.cyRevenue = cyRevenue;
		this.pyCategory = pyCategory;
		this.pyNos = pyNos;
		this.pySales = pySales;
		this.pyRevenue = pyRevenue;
	}
	
	public String getCircle() {
		return circle;
	}
	public void setCircle(String circle) {
		this.circle = circle;
	}
	public String getCyCategory() {
		return cyCategory;
	}
	public void setCyCategory(String cyCategory) {
		this.cyCategory = cyCategory;
	}
	public String getPyCategory() {
		return pyCategory;
	}
	public void setPyCategory(String pyCategory) {
		this.pyCategory = pyCategory;
	}
	public String getCyNos() {
		return cyNos;
	}
	public void setCyNos(String cyNos) {
		this.cyNos = cyNos;
	}
	public String getPyNos() {
		return pyNos;
	}
	public void setPyNos(String pyNos) {
		this.pyNos = pyNos;
	}
	public String getCySales() {
		return cySales;
	}
	public void setCySales(String cySales) {
		this.cySales = cySales;
	}
	public String getCyRevenue() {
		return cyRevenue;
	}
	public void setCyRevenue(String cyRevenue) {
		this.cyRevenue = cyRevenue;
	}
	public String getPySales() {
		return pySales;
	}
	public void setPySales(String pySales) {
		this.pySales = pySales;
	}
	public String getPyRevenue() {
		return pyRevenue;
	}
	public void setPyRevenue(String pyRevenue) {
		this.pyRevenue = pyRevenue;
	}

	@Override
	public String toString() {
		return "RelisationDetails [circle=" + circle + ", cyCategory=" + cyCategory + ", cyNos=" + cyNos + ", cySales="
				+ cySales + ", cyRevenue=" + cyRevenue + ", pyCategory=" + pyCategory + ", pyNos=" + pyNos
				+ ", pySales=" + pySales + ", pyRevenue=" + pyRevenue + "]";
	}
	
	
	
	

}
