package com.spdcl.model;

public class HtServiceDetails {
	
	
	private String releasedCircle;
	private String releasedHt1;
	private String releasedHt2;
	private String releasedHt3;
	private String releasedHt4;
	private String releasedHt5;
	private String existingCircle;
	private String existingHt1;
	private String existingHt2;
	private String existingHt3;
	private String existingHt4;
	private String existingHt5;
	private String Circle;
	private String live;
	private String UDC;
	private String billStop;
	public HtServiceDetails(String releasedCircle, String releasedHt1, String releasedHt2, String releasedHt3,
			String releasedHt4, String releasedHt5, String existingCircle, String existingHt1, String existingHt2,
			String existingHt3, String existingHt4, String existingHt5, String circle, String live, String uDC,
			String billStop) {
		super();
		this.releasedCircle = releasedCircle;
		this.releasedHt1 = releasedHt1;
		this.releasedHt2 = releasedHt2;
		this.releasedHt3 = releasedHt3;
		this.releasedHt4 = releasedHt4;
		this.releasedHt5 = releasedHt5;
		this.existingCircle = existingCircle;
		this.existingHt1 = existingHt1;
		this.existingHt2 = existingHt2;
		this.existingHt3 = existingHt3;
		this.existingHt4 = existingHt4;
		this.existingHt5 = existingHt5;
		Circle = circle;
		this.live = live;
		UDC = uDC;
		this.billStop = billStop;
	}
	public String getReleasedCircle() {
		return releasedCircle;
	}
	public void setReleasedCircle(String releasedCircle) {
		this.releasedCircle = releasedCircle;
	}
	public String getReleasedHt1() {
		return releasedHt1;
	}
	public void setReleasedHt1(String releasedHt1) {
		this.releasedHt1 = releasedHt1;
	}
	public String getReleasedHt2() {
		return releasedHt2;
	}
	public void setReleasedHt2(String releasedHt2) {
		this.releasedHt2 = releasedHt2;
	}
	public String getReleasedHt3() {
		return releasedHt3;
	}
	public void setReleasedHt3(String releasedHt3) {
		this.releasedHt3 = releasedHt3;
	}
	public String getReleasedHt4() {
		return releasedHt4;
	}
	public void setReleasedHt4(String releasedHt4) {
		this.releasedHt4 = releasedHt4;
	}
	public String getReleasedHt5() {
		return releasedHt5;
	}
	public void setReleasedHt5(String releasedHt5) {
		this.releasedHt5 = releasedHt5;
	}
	public String getExistingCircle() {
		return existingCircle;
	}
	public void setExistingCircle(String existingCircle) {
		this.existingCircle = existingCircle;
	}
	public String getExistingHt1() {
		return existingHt1;
	}
	public void setExistingHt1(String existingHt1) {
		this.existingHt1 = existingHt1;
	}
	public String getExistingHt2() {
		return existingHt2;
	}
	public void setExistingHt2(String existingHt2) {
		this.existingHt2 = existingHt2;
	}
	public String getExistingHt3() {
		return existingHt3;
	}
	public void setExistingHt3(String existingHt3) {
		this.existingHt3 = existingHt3;
	}
	public String getExistingHt4() {
		return existingHt4;
	}
	public void setExistingHt4(String existingHt4) {
		this.existingHt4 = existingHt4;
	}
	public String getExistingHt5() {
		return existingHt5;
	}
	public void setExistingHt5(String existingHt5) {
		this.existingHt5 = existingHt5;
	}
	public String getCircle() {
		return Circle;
	}
	public void setCircle(String circle) {
		Circle = circle;
	}
	public String getLive() {
		return live;
	}
	public void setLive(String live) {
		this.live = live;
	}
	public String getUDC() {
		return UDC;
	}
	public void setUDC(String uDC) {
		UDC = uDC;
	}
	public String getBillStop() {
		return billStop;
	}
	public void setBillStop(String billStop) {
		this.billStop = billStop;
	}
	@Override
	public String toString() {
		return "HtServiceDetails [releasedCircle=" + releasedCircle + ", releasedHt1=" + releasedHt1 + ", releasedHt2="
				+ releasedHt2 + ", releasedHt3=" + releasedHt3 + ", releasedHt4=" + releasedHt4 + ", releasedHt5="
				+ releasedHt5 + ", existingCircle=" + existingCircle + ", existingHt1=" + existingHt1 + ", existingHt2="
				+ existingHt2 + ", existingHt3=" + existingHt3 + ", existingHt4=" + existingHt4 + ", existingHt5="
				+ existingHt5 + ", Circle=" + Circle + ", live=" + live + ", UDC=" + UDC + ", billStop=" + billStop
				+ "]";
	}
	
	

}
