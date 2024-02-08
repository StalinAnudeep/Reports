package com.spdcl.util;

public class Register {
private String REG;
private String RATE_GRP;
private String VALUE;
private String IS_BILLABLE;
private String REG_CODE;
private String ACT_COM_DT;
private String C_SERIAL_NO;
private String EQUIPMENT;
private String DEV_SOL;
private String SERIAL_NO;




public String getSERIAL_NO() {
	return SERIAL_NO;
}

public void setSERIAL_NO(String sERIAL_NO) {
	SERIAL_NO = sERIAL_NO;
}

public Register(String aCT_COM_DT, String eQUIPMENT) {
	super();
	ACT_COM_DT = aCT_COM_DT;
	EQUIPMENT = eQUIPMENT;
}

public Register(String aCT_COM_DT, String eQUIPMENT,String c_SERIAL_NO) {
	super();
	ACT_COM_DT = aCT_COM_DT;
	EQUIPMENT = eQUIPMENT;
	C_SERIAL_NO = c_SERIAL_NO;
}


public String getEQUIPMENT() {
	return EQUIPMENT;
}


public void setEQUIPMENT(String eQUIPMENT) {
	EQUIPMENT = eQUIPMENT;
}


public String getC_SERIAL_NO() {
	return C_SERIAL_NO;
}


public void setC_SERIAL_NO(String c_SERIAL_NO) {
	C_SERIAL_NO = c_SERIAL_NO;
}


public Register() {
	// TODO Auto-generated constructor stub
}




public Register(String rEG, String rATE_GRP, String vALUE, String iS_BILLABLE, String rEG_CODE, String aCT_COM_DT,
		String c_SERIAL_NO,String dEV_SOL) {
	super();
	REG = rEG;
	RATE_GRP = rATE_GRP;
	VALUE = vALUE;
	IS_BILLABLE = iS_BILLABLE;
	REG_CODE = rEG_CODE;
	ACT_COM_DT = aCT_COM_DT;
	C_SERIAL_NO = c_SERIAL_NO;
	DEV_SOL = dEV_SOL;
}

public Register(String rEG, String rATE_GRP, String vALUE, String iS_BILLABLE, String rEG_CODE, String aCT_COM_DT,
		String c_SERIAL_NO,String sERIAL_NO, int i) {
	super();
	REG = rEG;
	RATE_GRP = rATE_GRP;
	VALUE = vALUE;
	IS_BILLABLE = iS_BILLABLE;
	REG_CODE = rEG_CODE;
	ACT_COM_DT = aCT_COM_DT;
	C_SERIAL_NO = c_SERIAL_NO;
	SERIAL_NO = sERIAL_NO;
}


public Register(String rEG, String rATE_GRP, String vALUE, String iS_BILLABLE, String rEG_CODE,String dEV_SOL) {
	super();
	REG = rEG;
	RATE_GRP = rATE_GRP;
	VALUE = vALUE;
	IS_BILLABLE = iS_BILLABLE;
	REG_CODE = rEG_CODE;
	DEV_SOL = dEV_SOL;
}


public String getACT_COM_DT() {
	return ACT_COM_DT;
}

public void setACT_COM_DT(String aCT_COM_DT) {
	ACT_COM_DT = aCT_COM_DT;
}

public String getREG() {
	return REG.trim().length()==1?"00"+REG.trim() :"0"+REG.trim();
}
public void setREG(String rEG) {
	REG = rEG;
}
public String getRATE_GRP() {
	return RATE_GRP;
}
public void setRATE_GRP(String rATE_GRP) {
	RATE_GRP = rATE_GRP;
}
public String getVALUE() {
	return VALUE;
}
public void setVALUE(String vALUE) {
	VALUE = vALUE;
}
public String getIS_BILLABLE() {
	return IS_BILLABLE;
}
public void setIS_BILLABLE(String iS_BILLABLE) {
	IS_BILLABLE = iS_BILLABLE;
}
public String getREG_CODE() {
	return REG_CODE;
}
public void setREG_CODE(String rEG_CODE) {
	REG_CODE = rEG_CODE;
}



@Override
public String toString() {
	return "Register [REG=" + REG + ", RATE_GRP=" + RATE_GRP + ", VALUE=" + VALUE + ", IS_BILLABLE=" + IS_BILLABLE
			+ ", REG_CODE=" + REG_CODE + ", ACT_COM_DT=" + ACT_COM_DT + ", C_SERIAL_NO=" + C_SERIAL_NO + ", EQUIPMENT="
			+ EQUIPMENT + ", DEV_SOL=" + DEV_SOL + ", SERIAL_NO=" + SERIAL_NO + "]";
}

public String getDEV_SOL() {
	return DEV_SOL;
}

public void setDEV_SOL(String dEV_SOL) {
	DEV_SOL = dEV_SOL;
}



}
