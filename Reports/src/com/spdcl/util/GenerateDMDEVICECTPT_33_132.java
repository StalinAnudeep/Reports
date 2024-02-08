package com.spdcl.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.spdcl.model.IdTypes;
public class GenerateDMDEVICECTPT_33_132 {

	public static ByteArrayInputStream generateBillTxt(List<Map<String, Object>>  data) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(out);
		for(Map<String, Object> d : data)
		printPageOne( ps,d);
		ps.close();
		return new ByteArrayInputStream(out.toByteArray());
	}//CTPT METER DEVICE FILE
	private static void printPageOne(PrintStream ps,Object o) {
		try {
		Map<String, Object> d = (Map<String, Object>) o;
		System.out.println(d.get("USCNO").toString().trim());
		ps.println(d.get("USCNO").toString().trim().replace(" ", "")
				 +"\tEQUI"
				 +"\t"+Optional.ofNullable(d.get("HT_AUTH_GROUP")).orElse("")//BEGRU
				 +"\t"+Optional.ofNullable(d.get("EQTYP")).orElse("")///EQTYP
				  +"\t"+Optional.ofNullable(d.get("METER_OWNER")).orElse("")//EQART
				 +"\t"+Optional.ofNullable(d.get("ACQ_DT")).orElse("")//ANDT Acquisition date
				 +"\t"+d.get("MAKE").toString().trim()//HERST 
				 +"\t"+d.get("SERIAL_NO").toString().trim()//SERGE
				 +"\t"+d.get("YEAR").toString().trim()//BAUJJ
				 +"\t"+d.get("MON_YEAR").toString().trim()//BAUMM
				 +"\t"+d.get("VALIDFROMT").toString().trim()//DATAB   Valid-From Date
				 +"\t"+d.get("MAIN_PLAN").toString().trim()//IWERK //MAIN_PLAN
				 +"\t"+d.get("MAIN_PLAN").toString().trim()//SWERK
				 +"\t"+Optional.ofNullable(d.get("DEVICECA")).orElse("")//MATNR
				 +"\t"/*+d.get("SERIAL_NO").toString().trim().replace(" ", "")*///SERNR
				 +"\t"/*+d.get("CER_DATE").toString().trim()*///CERT_DATE
				+"\t"/*+d.get("NEXT_REP").toString().trim()*///NXT_REPL_DATE
				 );
		ps.println(d.get("USCNO").toString().trim().replace(" ", "")
				+"\tEGERS"
				+"\t03"//BESITZ
				+"\t"//BGLJAHR
				+"\t"/*+d.get("CER_NUM").toString().trim()*///BGLNUM
				+"\t"/*+Optional.ofNullable(d.get("METER_POSITION")).orElse("").toString().trim()*///PARAMETR
				+"\t"+Optional.ofNullable(d.get("CT_CLASS")).orElse("").toString().trim()//PLOMBE
				+"\t"//BGLNETZ
				+"\t"//VLZEIT_DEV
				+"\t"/*+d.get("CER_DATE").toString().trim()*///CERT_DATE
				+"\t"/*+d.get("NEXT_REP").toString().trim()*///NXT_REPL_DATE				
				);
		ps.println(d.get("USCNO").toString().trim().replace(" ", "")
				+"\tEGERH"
/*				+"\t99991231" //BIS
*/				+ "\t"+d.get("VALIDFROMT").toString().trim()/*+d.get("VALIDFROMT").toString().trim()*////AB ACT_COM_DT
				);
		ps.println(d.get("USCNO").toString().trim().replace(" ", "")
				+"\tCLHEAD"
				+"\t"+(d.get("CT_PT_FLAG").toString().trim().equals("CT")?"ZCT_TRANS_SEALS":"ZPT_TRANS_SEALS")//CLASS
				+"\tIS2"// CLASSTYPE
				);
		ps.println(d.get("USCNO").toString().trim().replace(" ", "")
				+"\tCLDATA"
				+"\t"//CHARACT
				+"\t"//VALUE
				);

		ps.println(d.get("USCNO").toString().trim().replace(" ", "")+"\t&ENDE");
	
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
