package com.spdcl.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.spdcl.model.IdTypes;
public class GenerateDMDEVICEMETER {

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
		System.out.println(Optional.ofNullable(d.get("ACQ_DT")).orElse(""));
		// SERIAL_NO,HT_AUTH_GROUP,EQTYP,METER_OWNER,ACQ_DT,METER_MAKE,MNO,YEAR,MON_YEAR,VALIDFROMT,MAIN_PLAN,DEVICECA,CER_DATE,
		//NEXT_REP,CER_NUM,METER_POSITION,M_CT_CLASS,CLASS_EQTYP
		ps.println(d.get("SERIAL_NO").toString().trim().replace(" ", "")
				 +"\tEQUI"
				 +"\t"+Optional.ofNullable(d.get("HT_AUTH_GROUP")).orElse("")//BEGRU
				 +"\t"+Optional.ofNullable(d.get("EQTYP")).orElse("")///EQTYP
				  +"\t"+Optional.ofNullable(d.get("METER_OWNER")).orElse("")//EQART
				 +"\t"+Optional.ofNullable(d.get("ACQ_DT")).orElse("")//ACQ_DT Acquisition date
				 +"\t"+d.get("METER_MAKE").toString().trim()//HERST 
				 +"\t"+d.get("MNO").toString().trim()//SERGE
				 +"\t"+Optional.ofNullable(d.get("YEAR")).orElse("").toString().trim()//BAUJJ
				 +"\t"+Optional.ofNullable(d.get("MON_YEAR")).orElse("").toString().trim()//BAUMM
				 +"\t"+Optional.ofNullable(d.get("VALIDFROMT")).orElse("").toString().trim()//DATAB Valid-From Date
				 +"\t"+d.get("MAIN_PLAN").toString().trim()//IWERK //MAIN_PLAN
				 +"\t"+d.get("MAIN_PLAN").toString().trim()//SWERK
				 +"\t"+Optional.ofNullable(d.get("DEVICECA")).orElse("")//MATNR
				 +"\t"+d.get("MNO").toString().trim().replace(" ", "")//SERNR
				 +"\t"+Optional.ofNullable(d.get("CER_DATE")).orElse("").toString().trim()//CERT_DATE
				+"\t"+Optional.ofNullable(d.get("NEXT_REP")).orElse("").toString().trim()//NXT_REPL_DATE
				 );
		ps.println(d.get("SERIAL_NO").toString().trim().replace(" ", "")
				+"\tEGERS"
				+"\t"+Optional.ofNullable(d.get("BESITZ")).orElse("").toString().trim()//BESITZ
				+"\t"+Optional.ofNullable(d.get("INS_YEAR")).orElse("").toString().trim()//BGLJAHR
				+"\t"+Optional.ofNullable(d.get("CER_NUM")).orElse("").toString().trim()//BGLNUM
				+"\t"+Optional.ofNullable(d.get("METER_POSITION")).orElse("").toString().trim()//PARAMETR
				+"\t"+Optional.ofNullable(d.get("M_CT_CLASS")).orElse("").toString().trim()//PLOMBE
				+"\t"+Optional.ofNullable(d.get("BGLNETZ")).orElse("").toString().trim()//BGLNETZ
				+"\t"+Optional.ofNullable(d.get("VLZEIT_DEV")).orElse("").toString().trim()//VLZEIT_DEV
				+"\t"+Optional.ofNullable(d.get("CER_DATE")).orElse("").toString().trim()//CERT_DATE
				+"\t"+Optional.ofNullable(d.get("NEXT_REP")).orElse("").toString().trim()//NXT_REPL_DATE	
				+"\t"/*+d.get("USCNO").toString().trim()*/
				);
		ps.println(d.get("SERIAL_NO").toString().trim().replace(" ", "")
				+"\tEGERH"
/*				+"\t99991231" //BIS
*/				+ "\t"+Optional.ofNullable(d.get("VALIDFROMT")).orElse("").toString().trim()///AB ACT_COM_DT
				);
		ps.println(d.get("SERIAL_NO").toString().trim().replace(" ", "")
				+"\tCLHEAD"
				+"\t"+Optional.ofNullable(d.get("CLASS_EQTYP")).orElse("").toString().trim()//CLASS
				+"\tIS2"// CLASSTYPE
				);
		ps.println(d.get("SERIAL_NO").toString().trim().replace(" ", "")
				+"\tCLDATA"
				+"\t"//CHARACT
				);

		ps.println(d.get("SERIAL_NO").toString().trim().replace(" ", "")+"\t&ENDE");
	
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
