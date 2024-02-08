package com.spdcl.util;

public class OperandData {
private String startdate;
private String enddate;
private String consumptionvalue;
private String type;
private int order;



public OperandData() {
	// TODO Auto-generated constructor stub
}









public OperandData(String startdate, String enddate, String consumptionvalue, String type, int order) {
	super();
	this.startdate = startdate;
	this.enddate = enddate;
	this.consumptionvalue = consumptionvalue;
	this.type = type;
	this.order = order;
}









public String getStartdate() {
	return startdate;
}
public void setStartdate(String startdate) {
	this.startdate = startdate;
}
public String getEnddate() {
	return enddate;
}
public void setEnddate(String enddate) {
	this.enddate = enddate;
}
public String getConsumptionvalue() {
	return consumptionvalue;
}
public void setConsumptionvalue(String consumptionvalue) {
	this.consumptionvalue = consumptionvalue;
}
public String getType() {
	return type;
}



public void setType(String type) {
	this.type = type;
}



public int getOrder() {
	return order;
}




public void setOrder(int order) {
	this.order = order;
}




@Override
public String toString() {
	return "OperandData [startdate=" + startdate + ", enddate=" + enddate + ", consumptionvalue=" + consumptionvalue
			+ ", type=" + type + "]";
}





}
