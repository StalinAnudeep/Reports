package com.spdcl.model;

import java.io.Serializable;




public class IdTypes  implements Serializable{

	private static final long serialVersionUID = -8078384718587758219L;
	private String USCNO;
	private String ID_TYPE_CD;
	private String PER_ID_NBR;
	private String VERSION;
	public String getUSCNO() {
		return USCNO;
	}
	public void setUSCNO(String uSCNO) {
		USCNO = uSCNO;
	}
	public String getID_TYPE_CD() {
		return ID_TYPE_CD;
	}
	public void setID_TYPE_CD(String iD_TYPE_CD) {
		ID_TYPE_CD = iD_TYPE_CD;
	}
	public String getPER_ID_NBR() {
		return PER_ID_NBR;
	}
	public void setPER_ID_NBR(String pER_ID_NBR) {
		PER_ID_NBR = pER_ID_NBR;
	}
	public String getVERSION() {
		return VERSION;
	}
	public void setVERSION(String vERSION) {
		VERSION = vERSION;
	}
	
	
	
}
