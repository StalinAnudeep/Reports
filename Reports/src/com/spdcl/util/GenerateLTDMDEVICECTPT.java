package com.spdcl.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.spdcl.model.IdTypes;
public class GenerateLTDMDEVICECTPT {

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
		System.out.println(d.get("SERIAL_NO").toString().trim());
		ps.println(d.get("USCNO").toString().trim().replace(" ", "")+"_C"
				 +"\tEQUI"
				 +"\t"+Optional.ofNullable(d.get("HT_AUTH_GROUP")).orElse("")//BEGRU
				 +"\t"+Optional.ofNullable(d.get("EQTYP")).orElse("")///EQTYP
				  +"\t"+Optional.ofNullable(d.get("METER_OWNER")).orElse("")//EQART
				 +"\t"+Optional.ofNullable(d.get("ACQ_DT")).orElse("")//ACQ_DT
				 +"\t"+d.get("CUBICAL_MAKE").toString().trim()//HERST 
				 +"\t"+d.get("SERIAL_NO").toString().trim()//SERGE
				 +"\t"+d.get("YEAR").toString().trim()//BAUJJ
				 +"\t"+d.get("MON_YEAR").toString().trim()//BAUMM
				 +"\t"+d.get("VALIDFROM").toString().trim()///DATAB **************
				 +"\t"+d.get("MAIN_PLAN").toString().trim()//IWERK //MAIN_PLAN
				 +"\t"+d.get("MAIN_PLAN").toString().trim()//SWERK
				 +"\t"+Optional.ofNullable(d.get("DEVICECA")).orElse("")//MATNR
				 +"\t"/*+d.get("SERIAL_NO").toString().trim().replace(" ", "")*///SERNR
				 +"\t"/*+d.get("CER_DATE").toString().trim()//CERT_DATE
*/				+"\t"/*+d.get("NEXT_REP").toString().trim()//NXT_REPL_DATE
*/				 );
		ps.println(d.get("USCNO").toString().trim().replace(" ", "")+"_C"
				+"\tEGERS"
				+"\t03"//BESITZ
				+"\t"+d.get("YEAR").toString().trim()//BGLJAHR
				+"\t"+Optional.ofNullable(d.get("CER_NUM")).orElse("").toString().trim()//BGLNUM
				+"\t"/*+Optional.ofNullable(d.get("METER_POSITION")).orElse("").toString().trim()*///PARAMETR
				+"\t"+Optional.ofNullable(d.get("CT_CLASS")).orElse("").toString().trim()//PLOMBE
				+"\t"//BGLNETZ
				+"\t"//VLZEIT_DEV
				+"\t"/*+d.get("CER_DATE").toString().trim()//CERT_DATE
*/				+"\t"/*+d.get("NEXT_REP").toString().trim()//NXT_REPL_DATE				
*/				);
		ps.println(d.get("USCNO").toString().trim().replace(" ", "")+"_C"
				+"\tEGERH"
/*				+"\t99991231" //BIS
*/				+ "\t"+d.get("VALIDFROM").toString().trim()//AB ACT_COM_DT ***************
				);
		/*ps.println(d.get("USCNO").toString().trim().replace(" ", "")
				+"\tCLHEAD"
				+"\t"//CLASS
				+"\t"// CLASSTYPE
				);
		ps.println(d.get("USCNO").toString().trim().replace(" ", "")
				+"\tCLDATA"
				+"\t"//CHARACT
				+"\t"//VALUE
				);*/

		ps.println(d.get("USCNO").toString().trim().replace(" ", "")+"_C"
				+"\t&ENDE");
	
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
