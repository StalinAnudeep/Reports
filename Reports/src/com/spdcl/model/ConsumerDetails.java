package com.spdcl.model;
import java.io.Serializable;
import java.util.List;
public class ConsumerDetails  implements Serializable
{
	private static final long serialVersionUID = 2883351371627177695L;
	private String CIRNAME;
	private String DIVNAME;
	private String ERONAME;
	private String SUBNAME;
	private String SECNAME;
	private String CTACCT_ID;
	private String CTUSCNO;
	private String CTNAME;
	private String CTADD1;
	private String CTADD2;
	private String CTADD3;
	private String CTADD4;
	private String CTCITY;
	private String CTPINCODE;
	private String CTCAT;
	private String CTSUBCAT;
	private String CTSTATUS;
	private String CTDISCD;
	private String CTAREACD;
	private String CTCIRCD;
	private String CTDIVCD;
	private String CTEROCD;
	private String CTSUBDIVCD;
	private String CTSECCD;
	private String CTGROUP;
	private String CTLCNO;
	private String CTENTDT;
	private String CTAGRMTDT;
	private String CTAGRMTEXP_DT;
	private String CTSUPCONDT;
	private String CTSERVTYPE;
	private String CTCONNLD;
	private String CTCMD_HT;
	private String CTCAPAVAIL;
	private String CTCAPREQ;
	private String CTCAPSURCHGPER;
	private String CTBLSTDT;
	private String CTSDTOTAMT;
	private String CTSDREQ;
	private String CTPENINTIND;
	private String CTREBATEIND;
	private String CTINSTMTIND;
	private String CTMOBILE;
	private String CTEMAILID;
	private String CTEDFLAG;
	private String CTDTR_TYPE;
	private String CTACDPROV;
	private String CTCYCLE;
	private String CTSCSTFLAG;
	private String CTSAPGOVTCD;
	private String CTSAP_SECCD;
	private String CT_SAP_UNITCODE;
	private String CTSAP_SUBDVCD;
	private String CTEQPOINTS;
	private String CT_METERSIDE_FLAG;
	private String CTPAACODE;
	private String CTSOCIALGROUP;
	private String CTSTRUCTURECD;
	private String CTCAPACITOR_FLAG;
	private String CTLOGINNAME;
	private String CTRELEASECAT;
	private String CTHODTYPE;
	private String CTHODDEP;
	private String CTHODSUBDEP;
	private String CTBLPROSID;
	private String CTADVFLAG;
	private String CTPHONEON;
	private String CTSEASFLAG_HT;
	private String CTSNLSTRTMTH_HT;
	private String CTSNLMONTHS_HT;
	private String CTPOLENO;
	private String CTMTRRDRCD;
	private String CTMETERED;
	private String CTCSCNO;
	private String CTIPADDRESS;
	private String CTCSCNODT;
	private String CTOTHERREV;
	private String CTUKSCNO;
	private String CTUKSECCD;
	private String CTPOLE_GIS;
	private String CTPANNO;
	private String CTSPEC_KV;
	private String CTACTUAL_KV;
	private String CTLF_FLAG;
	private String CTCOLNYFLAG_HT;
	private String CTITFLAG_HT;
	private String CTTEMPFLAG_HT;
	private String CTVOLTSURCHG_HT;
	private String CTINDFEEDFLAG_HT;
	private String CTWHEELFLAG_HT;
	private String CTWHEELCNT_HT;
	private String CTTODSEG_HT;
	private String CTBANKACNO;
	private String CTNEWSERID;
	private String CTTDS_FLAG;
	private String CTGOVT_PVT;
	private String CTFEEDER_CODE;
	private String CTFEEDER_NAME;
	private String CTSS_NAME;
	private String CTOLDSCNO;
	private String CTEMPTY_1;
	private String CTEMPTY_2;
	private String CTEMPTY_3;
	private String CTMTRCOUNT_HT;
	private String CTMTRCOUNT_LF;
	private String CTMTRCOUNT_CL;
	private String CTSOLAR_FLAG;
	private String CTLOCA_TYPE;
	private String CTAQUA_FLAG;
	private String STDESC;
	private String TCTENTDT;
	private String LAST_PAID_DATE;
	private String CTSDTOTAMT_R;
	private String CTPANNO_TEMP;
	
	
	
	public String getCTPANNO_TEMP() {
		return CTPANNO_TEMP;
	}
	public void setCTPANNO_TEMP(String cTPANNO_TEMP) {
		CTPANNO_TEMP = cTPANNO_TEMP;
	}
	public String getCTSDTOTAMT_R() {
		return CTSDTOTAMT_R;
	}
	public void setCTSDTOTAMT_R(String cTSDTOTAMT_R) {
		CTSDTOTAMT_R = cTSDTOTAMT_R;
	}
	public String getLAST_PAID_DATE() {
		return LAST_PAID_DATE;
	}
	public void setLAST_PAID_DATE(String lAST_PAID_DATE) {
		LAST_PAID_DATE = lAST_PAID_DATE;
	}
	public String getTCTENTDT() {
		return TCTENTDT;
	}
	public void setTCTENTDT(String tCTENTDT) {
		TCTENTDT = tCTENTDT;
	}
	private List<IdTypes> idTypes;
	
	/*private List<MeterReadingDetail> mainmeters;
	private SolarMeterReading solarmeter;
	private List<ColonyMeter> colonymeters;
	private List<LFMeter> lfmeters;
	private List<IdTypes> idTypes;
	
	private HTBillDetail billdetails;*/
	public String getCIRNAME() {
		return CIRNAME;
	}
	public void setCIRNAME(String cIRNAME) {
		CIRNAME = cIRNAME;
	}
	public String getDIVNAME() {
		return DIVNAME;
	}
	public void setDIVNAME(String dIVNAME) {
		DIVNAME = dIVNAME;
	}
	public String getERONAME() {
		return ERONAME;
	}
	public void setERONAME(String eRONAME) {
		ERONAME = eRONAME;
	}
	public String getSUBNAME() {
		return SUBNAME;
	}
	public void setSUBNAME(String sUBNAME) {
		SUBNAME = sUBNAME;
	}
	public String getSECNAME() {
		return SECNAME;
	}
	public void setSECNAME(String sECNAME) {
		SECNAME = sECNAME;
	}
	public String getCTACCT_ID() {
		return CTACCT_ID;
	}
	public void setCTACCT_ID(String cTACCT_ID) {
		CTACCT_ID = cTACCT_ID;
	}
	public String getCTUSCNO() {
		return CTUSCNO;
	}
	public void setCTUSCNO(String cTUSCNO) {
		CTUSCNO = cTUSCNO;
	}
	public String getCTNAME() {
		return CTNAME;
	}
	public void setCTNAME(String cTNAME) {
		CTNAME = cTNAME;
	}
	public String getCTADD1() {
		return CTADD1;
	}
	public void setCTADD1(String cTADD1) {
		CTADD1 = cTADD1;
	}
	public String getCTADD2() {
		return CTADD2;
	}
	public void setCTADD2(String cTADD2) {
		CTADD2 = cTADD2;
	}
	public String getCTADD3() {
		return CTADD3;
	}
	public void setCTADD3(String cTADD3) {
		CTADD3 = cTADD3;
	}
	public String getCTADD4() {
		return CTADD4;
	}
	public void setCTADD4(String cTADD4) {
		CTADD4 = cTADD4;
	}
	public String getCTCITY() {
		return CTCITY;
	}
	public void setCTCITY(String cTCITY) {
		CTCITY = cTCITY;
	}
	public String getCTPINCODE() {
		return CTPINCODE;
	}
	public void setCTPINCODE(String cTPINCODE) {
		CTPINCODE = cTPINCODE;
	}
	public String getCTCAT() {
		return CTCAT;
	}
	public void setCTCAT(String cTCAT) {
		CTCAT = cTCAT;
	}
	public String getCTSUBCAT() {
		return CTSUBCAT;
	}
	public void setCTSUBCAT(String cTSUBCAT) {
		CTSUBCAT = cTSUBCAT;
	}
	public String getCTSTATUS() {
		return CTSTATUS;
	}
	public void setCTSTATUS(String cTSTATUS) {
		CTSTATUS = cTSTATUS;
	}
	public String getCTDISCD() {
		return CTDISCD;
	}
	public void setCTDISCD(String cTDISCD) {
		CTDISCD = cTDISCD;
	}
	public String getCTAREACD() {
		return CTAREACD;
	}
	public void setCTAREACD(String cTAREACD) {
		CTAREACD = cTAREACD;
	}
	public String getCTCIRCD() {
		return CTCIRCD;
	}
	public void setCTCIRCD(String cTCIRCD) {
		CTCIRCD = cTCIRCD;
	}
	public String getCTDIVCD() {
		return CTDIVCD;
	}
	public void setCTDIVCD(String cTDIVCD) {
		CTDIVCD = cTDIVCD;
	}
	public String getCTEROCD() {
		return CTEROCD;
	}
	public void setCTEROCD(String cTEROCD) {
		CTEROCD = cTEROCD;
	}
	public String getCTSUBDIVCD() {
		return CTSUBDIVCD;
	}
	public void setCTSUBDIVCD(String cTSUBDIVCD) {
		CTSUBDIVCD = cTSUBDIVCD;
	}
	public String getCTSECCD() {
		return CTSECCD;
	}
	public void setCTSECCD(String cTSECCD) {
		CTSECCD = cTSECCD;
	}
	public String getCTGROUP() {
		return CTGROUP;
	}
	public void setCTGROUP(String cTGROUP) {
		CTGROUP = cTGROUP;
	}
	public String getCTLCNO() {
		return CTLCNO;
	}
	public void setCTLCNO(String cTLCNO) {
		CTLCNO = cTLCNO;
	}
	public String getCTENTDT() {
		return CTENTDT;
	}
	public void setCTENTDT(String cTENTDT) {
		CTENTDT = cTENTDT;
	}
	public String getCTAGRMTDT() {
		return CTAGRMTDT;
	}
	public void setCTAGRMTDT(String cTAGRMTDT) {
		CTAGRMTDT = cTAGRMTDT;
	}
	public String getCTAGRMTEXP_DT() {
		return CTAGRMTEXP_DT;
	}
	public void setCTAGRMTEXP_DT(String cTAGRMTEXP_DT) {
		CTAGRMTEXP_DT = cTAGRMTEXP_DT;
	}
	public String getCTSUPCONDT() {
		return CTSUPCONDT;
	}
	public void setCTSUPCONDT(String cTSUPCONDT) {
		CTSUPCONDT = cTSUPCONDT;
	}
	public String getCTSERVTYPE() {
		return CTSERVTYPE;
	}
	public void setCTSERVTYPE(String cTSERVTYPE) {
		CTSERVTYPE = cTSERVTYPE;
	}
	public String getCTCONNLD() {
		return CTCONNLD;
	}
	public void setCTCONNLD(String cTCONNLD) {
		CTCONNLD = cTCONNLD;
	}
	public String getCTCMD_HT() {
		return CTCMD_HT;
	}
	public void setCTCMD_HT(String cTCMD_HT) {
		CTCMD_HT = cTCMD_HT;
	}
	public String getCTCAPAVAIL() {
		return CTCAPAVAIL;
	}
	public void setCTCAPAVAIL(String cTCAPAVAIL) {
		CTCAPAVAIL = cTCAPAVAIL;
	}
	public String getCTCAPREQ() {
		return CTCAPREQ;
	}
	public void setCTCAPREQ(String cTCAPREQ) {
		CTCAPREQ = cTCAPREQ;
	}
	public String getCTCAPSURCHGPER() {
		return CTCAPSURCHGPER;
	}
	public void setCTCAPSURCHGPER(String cTCAPSURCHGPER) {
		CTCAPSURCHGPER = cTCAPSURCHGPER;
	}
	public String getCTBLSTDT() {
		return CTBLSTDT;
	}
	public void setCTBLSTDT(String cTBLSTDT) {
		CTBLSTDT = cTBLSTDT;
	}
	public String getCTSDTOTAMT() {
		return CTSDTOTAMT;
	}
	public void setCTSDTOTAMT(String cTSDTOTAMT) {
		CTSDTOTAMT = cTSDTOTAMT;
	}
	public String getCTSDREQ() {
		return CTSDREQ;
	}
	public void setCTSDREQ(String cTSDREQ) {
		CTSDREQ = cTSDREQ;
	}
	public String getCTPENINTIND() {
		return CTPENINTIND;
	}
	public void setCTPENINTIND(String cTPENINTIND) {
		CTPENINTIND = cTPENINTIND;
	}
	public String getCTREBATEIND() {
		return CTREBATEIND;
	}
	public void setCTREBATEIND(String cTREBATEIND) {
		CTREBATEIND = cTREBATEIND;
	}
	public String getCTINSTMTIND() {
		return CTINSTMTIND;
	}
	public void setCTINSTMTIND(String cTINSTMTIND) {
		CTINSTMTIND = cTINSTMTIND;
	}
	public String getCTMOBILE() {
		return CTMOBILE;
	}
	public void setCTMOBILE(String cTMOBILE) {
		CTMOBILE = cTMOBILE;
	}
	public String getCTEMAILID() {
		return CTEMAILID;
	}
	public void setCTEMAILID(String cTEMAILID) {
		CTEMAILID = cTEMAILID;
	}
	public String getCTEDFLAG() {
		return CTEDFLAG;
	}
	public void setCTEDFLAG(String cTEDFLAG) {
		CTEDFLAG = cTEDFLAG;
	}
	public String getCTDTR_TYPE() {
		return CTDTR_TYPE;
	}
	public void setCTDTR_TYPE(String cTDTR_TYPE) {
		CTDTR_TYPE = cTDTR_TYPE;
	}
	public String getCTACDPROV() {
		return CTACDPROV;
	}
	public void setCTACDPROV(String cTACDPROV) {
		CTACDPROV = cTACDPROV;
	}
	public String getCTCYCLE() {
		return CTCYCLE;
	}
	public void setCTCYCLE(String cTCYCLE) {
		CTCYCLE = cTCYCLE;
	}
	public String getCTSCSTFLAG() {
		return CTSCSTFLAG;
	}
	public void setCTSCSTFLAG(String cTSCSTFLAG) {
		CTSCSTFLAG = cTSCSTFLAG;
	}
	public String getCTSAPGOVTCD() {
		return CTSAPGOVTCD;
	}
	public void setCTSAPGOVTCD(String cTSAPGOVTCD) {
		CTSAPGOVTCD = cTSAPGOVTCD;
	}
	public String getCTSAP_SECCD() {
		return CTSAP_SECCD;
	}
	public void setCTSAP_SECCD(String cTSAP_SECCD) {
		CTSAP_SECCD = cTSAP_SECCD;
	}
	public String getCT_SAP_UNITCODE() {
		return CT_SAP_UNITCODE;
	}
	public void setCT_SAP_UNITCODE(String cT_SAP_UNITCODE) {
		CT_SAP_UNITCODE = cT_SAP_UNITCODE;
	}
	public String getCTSAP_SUBDVCD() {
		return CTSAP_SUBDVCD;
	}
	public void setCTSAP_SUBDVCD(String cTSAP_SUBDVCD) {
		CTSAP_SUBDVCD = cTSAP_SUBDVCD;
	}
	public String getCTEQPOINTS() {
		return CTEQPOINTS;
	}
	public void setCTEQPOINTS(String cTEQPOINTS) {
		CTEQPOINTS = cTEQPOINTS;
	}
	public String getCT_METERSIDE_FLAG() {
		return CT_METERSIDE_FLAG;
	}
	public void setCT_METERSIDE_FLAG(String cT_METERSIDE_FLAG) {
		CT_METERSIDE_FLAG = cT_METERSIDE_FLAG;
	}
	public String getCTPAACODE() {
		return CTPAACODE;
	}
	public void setCTPAACODE(String cTPAACODE) {
		CTPAACODE = cTPAACODE;
	}
	public String getCTSOCIALGROUP() {
		return CTSOCIALGROUP;
	}
	public void setCTSOCIALGROUP(String cTSOCIALGROUP) {
		CTSOCIALGROUP = cTSOCIALGROUP;
	}
	public String getCTSTRUCTURECD() {
		return CTSTRUCTURECD;
	}
	public void setCTSTRUCTURECD(String cTSTRUCTURECD) {
		CTSTRUCTURECD = cTSTRUCTURECD;
	}
	public String getCTCAPACITOR_FLAG() {
		return CTCAPACITOR_FLAG;
	}
	public void setCTCAPACITOR_FLAG(String cTCAPACITOR_FLAG) {
		CTCAPACITOR_FLAG = cTCAPACITOR_FLAG;
	}
	public String getCTLOGINNAME() {
		return CTLOGINNAME;
	}
	public void setCTLOGINNAME(String cTLOGINNAME) {
		CTLOGINNAME = cTLOGINNAME;
	}
	public String getCTRELEASECAT() {
		return CTRELEASECAT;
	}
	public void setCTRELEASECAT(String cTRELEASECAT) {
		CTRELEASECAT = cTRELEASECAT;
	}
	public String getCTHODTYPE() {
		return CTHODTYPE;
	}
	public void setCTHODTYPE(String cTHODTYPE) {
		CTHODTYPE = cTHODTYPE;
	}
	public String getCTHODDEP() {
		return CTHODDEP;
	}
	public void setCTHODDEP(String cTHODDEP) {
		CTHODDEP = cTHODDEP;
	}
	public String getCTHODSUBDEP() {
		return CTHODSUBDEP;
	}
	public void setCTHODSUBDEP(String cTHODSUBDEP) {
		CTHODSUBDEP = cTHODSUBDEP;
	}
	public String getCTBLPROSID() {
		return CTBLPROSID;
	}
	public void setCTBLPROSID(String cTBLPROSID) {
		CTBLPROSID = cTBLPROSID;
	}
	public String getCTADVFLAG() {
		return CTADVFLAG;
	}
	public void setCTADVFLAG(String cTADVFLAG) {
		CTADVFLAG = cTADVFLAG;
	}
	public String getCTPHONEON() {
		return CTPHONEON;
	}
	public void setCTPHONEON(String cTPHONEON) {
		CTPHONEON = cTPHONEON;
	}
	public String getCTSEASFLAG_HT() {
		return CTSEASFLAG_HT;
	}
	public void setCTSEASFLAG_HT(String cTSEASFLAG_HT) {
		CTSEASFLAG_HT = cTSEASFLAG_HT;
	}
	public String getCTSNLSTRTMTH_HT() {
		return CTSNLSTRTMTH_HT;
	}
	public void setCTSNLSTRTMTH_HT(String cTSNLSTRTMTH_HT) {
		CTSNLSTRTMTH_HT = cTSNLSTRTMTH_HT;
	}
	public String getCTSNLMONTHS_HT() {
		return CTSNLMONTHS_HT;
	}
	public void setCTSNLMONTHS_HT(String cTSNLMONTHS_HT) {
		CTSNLMONTHS_HT = cTSNLMONTHS_HT;
	}
	public String getCTPOLENO() {
		return CTPOLENO;
	}
	public void setCTPOLENO(String cTPOLENO) {
		CTPOLENO = cTPOLENO;
	}
	public String getCTMTRRDRCD() {
		return CTMTRRDRCD;
	}
	public void setCTMTRRDRCD(String cTMTRRDRCD) {
		CTMTRRDRCD = cTMTRRDRCD;
	}
	public String getCTMETERED() {
		return CTMETERED;
	}
	public void setCTMETERED(String cTMETERED) {
		CTMETERED = cTMETERED;
	}
	public String getCTCSCNO() {
		return CTCSCNO;
	}
	public void setCTCSCNO(String cTCSCNO) {
		CTCSCNO = cTCSCNO;
	}
	public String getCTIPADDRESS() {
		return CTIPADDRESS;
	}
	public void setCTIPADDRESS(String cTIPADDRESS) {
		CTIPADDRESS = cTIPADDRESS;
	}
	public String getCTCSCNODT() {
		return CTCSCNODT;
	}
	public void setCTCSCNODT(String cTCSCNODT) {
		CTCSCNODT = cTCSCNODT;
	}
	public String getCTOTHERREV() {
		return CTOTHERREV;
	}
	public void setCTOTHERREV(String cTOTHERREV) {
		CTOTHERREV = cTOTHERREV;
	}
	public String getCTUKSCNO() {
		return CTUKSCNO;
	}
	public void setCTUKSCNO(String cTUKSCNO) {
		CTUKSCNO = cTUKSCNO;
	}
	public String getCTUKSECCD() {
		return CTUKSECCD;
	}
	public void setCTUKSECCD(String cTUKSECCD) {
		CTUKSECCD = cTUKSECCD;
	}
	public String getCTPOLE_GIS() {
		return CTPOLE_GIS;
	}
	public void setCTPOLE_GIS(String cTPOLE_GIS) {
		CTPOLE_GIS = cTPOLE_GIS;
	}
	public String getCTPANNO() {
		return CTPANNO;
	}
	public void setCTPANNO(String cTPANNO) {
		CTPANNO = cTPANNO;
	}
	public String getCTSPEC_KV() {
		return CTSPEC_KV;
	}
	public void setCTSPEC_KV(String cTSPEC_KV) {
		CTSPEC_KV = cTSPEC_KV;
	}
	public String getCTACTUAL_KV() {
		return CTACTUAL_KV;
	}
	public void setCTACTUAL_KV(String cTACTUAL_KV) {
		CTACTUAL_KV = cTACTUAL_KV;
	}
	public String getCTLF_FLAG() {
		return CTLF_FLAG;
	}
	public void setCTLF_FLAG(String cTLF_FLAG) {
		CTLF_FLAG = cTLF_FLAG;
	}
	public String getCTCOLNYFLAG_HT() {
		return CTCOLNYFLAG_HT;
	}
	public void setCTCOLNYFLAG_HT(String cTCOLNYFLAG_HT) {
		CTCOLNYFLAG_HT = cTCOLNYFLAG_HT;
	}
	public String getCTITFLAG_HT() {
		return CTITFLAG_HT;
	}
	public void setCTITFLAG_HT(String cTITFLAG_HT) {
		CTITFLAG_HT = cTITFLAG_HT;
	}
	public String getCTTEMPFLAG_HT() {
		return CTTEMPFLAG_HT;
	}
	public void setCTTEMPFLAG_HT(String cTTEMPFLAG_HT) {
		CTTEMPFLAG_HT = cTTEMPFLAG_HT;
	}
	public String getCTVOLTSURCHG_HT() {
		return CTVOLTSURCHG_HT;
	}
	public void setCTVOLTSURCHG_HT(String cTVOLTSURCHG_HT) {
		CTVOLTSURCHG_HT = cTVOLTSURCHG_HT;
	}
	public String getCTINDFEEDFLAG_HT() {
		return CTINDFEEDFLAG_HT;
	}
	public void setCTINDFEEDFLAG_HT(String cTINDFEEDFLAG_HT) {
		CTINDFEEDFLAG_HT = cTINDFEEDFLAG_HT;
	}
	public String getCTWHEELFLAG_HT() {
		return CTWHEELFLAG_HT;
	}
	public void setCTWHEELFLAG_HT(String cTWHEELFLAG_HT) {
		CTWHEELFLAG_HT = cTWHEELFLAG_HT;
	}
	public String getCTWHEELCNT_HT() {
		return CTWHEELCNT_HT;
	}
	public void setCTWHEELCNT_HT(String cTWHEELCNT_HT) {
		CTWHEELCNT_HT = cTWHEELCNT_HT;
	}
	public String getCTTODSEG_HT() {
		return CTTODSEG_HT;
	}
	public void setCTTODSEG_HT(String cTTODSEG_HT) {
		CTTODSEG_HT = cTTODSEG_HT;
	}
	public String getCTBANKACNO() {
		return CTBANKACNO;
	}
	public void setCTBANKACNO(String cTBANKACNO) {
		CTBANKACNO = cTBANKACNO;
	}
	public String getCTNEWSERID() {
		return CTNEWSERID;
	}
	public void setCTNEWSERID(String cTNEWSERID) {
		CTNEWSERID = cTNEWSERID;
	}
	public String getCTTDS_FLAG() {
		return CTTDS_FLAG;
	}
	public void setCTTDS_FLAG(String cTTDS_FLAG) {
		CTTDS_FLAG = cTTDS_FLAG;
	}
	public String getCTGOVT_PVT() {
		return CTGOVT_PVT;
	}
	public void setCTGOVT_PVT(String cTGOVT_PVT) {
		CTGOVT_PVT = cTGOVT_PVT;
	}
	public String getCTFEEDER_CODE() {
		return CTFEEDER_CODE;
	}
	public void setCTFEEDER_CODE(String cTFEEDER_CODE) {
		CTFEEDER_CODE = cTFEEDER_CODE;
	}
	public String getCTFEEDER_NAME() {
		return CTFEEDER_NAME;
	}
	public void setCTFEEDER_NAME(String cTFEEDER_NAME) {
		CTFEEDER_NAME = cTFEEDER_NAME;
	}
	public String getCTSS_NAME() {
		return CTSS_NAME;
	}
	public void setCTSS_NAME(String cTSS_NAME) {
		CTSS_NAME = cTSS_NAME;
	}
	public String getCTOLDSCNO() {
		return CTOLDSCNO;
	}
	public void setCTOLDSCNO(String cTOLDSCNO) {
		CTOLDSCNO = cTOLDSCNO;
	}
	public String getCTEMPTY_1() {
		return CTEMPTY_1;
	}
	public void setCTEMPTY_1(String cTEMPTY_1) {
		CTEMPTY_1 = cTEMPTY_1;
	}
	public String getCTEMPTY_2() {
		return CTEMPTY_2;
	}
	public void setCTEMPTY_2(String cTEMPTY_2) {
		CTEMPTY_2 = cTEMPTY_2;
	}
	public String getCTEMPTY_3() {
		return CTEMPTY_3;
	}
	public void setCTEMPTY_3(String cTEMPTY_3) {
		CTEMPTY_3 = cTEMPTY_3;
	}
	public String getCTMTRCOUNT_HT() {
		return CTMTRCOUNT_HT;
	}
	public void setCTMTRCOUNT_HT(String cTMTRCOUNT_HT) {
		CTMTRCOUNT_HT = cTMTRCOUNT_HT;
	}
	public String getCTMTRCOUNT_LF() {
		return CTMTRCOUNT_LF;
	}
	public void setCTMTRCOUNT_LF(String cTMTRCOUNT_LF) {
		CTMTRCOUNT_LF = cTMTRCOUNT_LF;
	}
	public String getCTMTRCOUNT_CL() {
		return CTMTRCOUNT_CL;
	}
	public void setCTMTRCOUNT_CL(String cTMTRCOUNT_CL) {
		CTMTRCOUNT_CL = cTMTRCOUNT_CL;
	}
	public String getCTSOLAR_FLAG() {
		return CTSOLAR_FLAG;
	}
	public void setCTSOLAR_FLAG(String cTSOLAR_FLAG) {
		CTSOLAR_FLAG = cTSOLAR_FLAG;
	}
	public String getCTLOCA_TYPE() {
		return CTLOCA_TYPE;
	}
	public void setCTLOCA_TYPE(String cTLOCA_TYPE) {
		CTLOCA_TYPE = cTLOCA_TYPE;
	}
	public String getCTAQUA_FLAG() {
		return CTAQUA_FLAG;
	}
	public void setCTAQUA_FLAG(String cTAQUA_FLAG) {
		CTAQUA_FLAG = cTAQUA_FLAG;
	}
	public String getSTDESC() {
		return STDESC;
	}
	public void setSTDESC(String sTDESC) {
		STDESC = sTDESC;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public List<IdTypes> getIdTypes() {
		return idTypes;
	}
	public void setIdTypes(List<IdTypes> idTypes) {
		this.idTypes = idTypes;
	}
	
	
}