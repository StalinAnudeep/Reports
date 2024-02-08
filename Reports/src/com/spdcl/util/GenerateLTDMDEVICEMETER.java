package com.spdcl.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.spdcl.model.IdTypes;
public class GenerateLTDMDEVICEMETER {

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
		System.out.println(d.get("MNO").toString().trim());
		ps.println(d.get("USCNO").toString().trim().replace(" ", "")
				 +"\tEQUI"
				 +"\t"+Optional.ofNullable(d.get("HT_AUTH_GROUP")).orElse("")//BEGRU
				 +"\t"+Optional.ofNullable(d.get("EQTYP")).orElse("")///EQTYP
				  +"\t"+Optional.ofNullable(d.get("METER_OWNER")).orElse("")//EQART
				 +"\t"+Optional.ofNullable(d.get("ACQ_DT")).orElse("")//ACQ_DT
				 +"\t"+d.get("METER_MAKE").toString().trim()//HERST 
				 +"\t"+d.get("MNO").toString().trim()//SERGE
				 +"\t"+d.get("YEAR").toString().trim()//BAUJJ
				 +"\t"+d.get("MON_YEAR").toString().trim()//BAUMM
				 +"\t"+d.get("VALIDFROM").toString().trim()//DATAB
				 +"\t"+d.get("MAIN_PLAN").toString().trim()//IWERK //MAIN_PLAN
				 +"\t"+d.get("MAIN_PLAN").toString().trim()//SWERK
				 +"\t"+Optional.ofNullable(d.get("DEVICECA")).orElse("")//MATNR
				 +"\t"+d.get("SERIAL_NO").toString().trim().replace(" ", "")//SERNR
				 +"\t"+Optional.ofNullable(d.get("CER_DATE")).orElse("")//CERT_DATE
				+"\t"+Optional.ofNullable(d.get("NEXT_REP")).orElse("")//NXT_REPL_DATE
				 );
		ps.println(d.get("USCNO").toString().trim().replace(" ", "")
				+"\tEGERS"
				+"\t"+Optional.ofNullable(d.get("BESITZ")).orElse("").toString().trim()//BESITZ // AS PER Dis Madhri Mam colony meter Meter's 03
				+"\t"+Optional.ofNullable(d.get("INS_YEAR")).orElse("").toString().trim()//BGLJAHR//BGLJAHR
				+"\t"+Optional.ofNullable(d.get("CER_NUM")).orElse("").toString().trim()//BGLNUM
				+"\t"+Optional.ofNullable(d.get("METER_POSITION")).orElse("").toString().trim()//PARAMETR
				+"\t"+Optional.ofNullable(d.get("M_CT_CLASS")).orElse("").toString().trim()//PLOMBE
				+"\t"+Optional.ofNullable(d.get("BGLNETZ")).orElse("").toString().trim()//BGLNETZ // for colony meter space
				+"\t"+Optional.ofNullable(d.get("VLZEIT_DEV")).orElse("").toString().trim()//VLZEIT_DEV//VLZEIT_DEV
				+"\t"+Optional.ofNullable(d.get("CER_DATE")).orElse("").toString().trim()//CERT_DATE
				+"\t"+Optional.ofNullable(d.get("NEXT_REP")).orElse("").toString().trim()//NXT_REPL_DATE				
				);
		ps.println(d.get("USCNO").toString().trim().replace(" ", "")
				+"\tEGERH"
/*				+"\t99991231" //BIS
*/				+ "\t"+d.get("VALIDFROM").toString().trim()///AB ACT_COM_DT
				);
			if (Optional.ofNullable(d.get("MTSEAL")).orElse("").toString().trim().length() != 0
					|| Optional.ofNullable(d.get("MTMRTSEAL1")).orElse("").toString().trim().length() != 0) {

				ps.println(d.get("USCNO").toString().trim().replace(" ", "") 
						+ "\tCLHEAD" 
						+ "\tZHT_METERSEALS"// CLASS
						+ "\tIS2"// CLASSTYPE
				);
				if(Optional.ofNullable(d.get("MTSEAL")).orElse("").toString().trim().length() != 0) {
				ps.println(d.get("USCNO").toString().trim().replace(" ", "") 
						+ "\tCLDATA" 
						+ "\tMETERBOARDTOBOX"// CHARACT
						+ "\t"+d.get("MTSEAL").toString().trim() // VALUE
				
				);
				}
				if(Optional.ofNullable(d.get("MTMRTSEAL1")).orElse("").toString().trim().length() != 0) {
					ps.println(d.get("USCNO").toString().trim().replace(" ", "") 
							+ "\tCLDATA" 
							+ "\tMETERCOVER"// CHARACT
							+ "\t"+d.get("MTMRTSEAL1").toString().trim() // VALUE
					
					);
				}
				if(Optional.ofNullable(d.get("MTMRTSEAL2")).orElse("").toString().trim().length() != 0) {
					ps.println(d.get("USCNO").toString().trim().replace(" ", "") 
							+ "\tCLDATA" 
							+ "\tMETERTERMINALCOVER"// CHARACT
							+ "\t"+d.get("MTMRTSEAL2").toString().trim() // VALUE
					
					);
				}
				if(Optional.ofNullable(d.get("MTMRTSEAL3")).orElse("").toString().trim().length() != 0) {
					ps.println(d.get("USCNO").toString().trim().replace(" ", "") 
							+ "\tCLDATA" 
							+ "\tMRI"// CHARACT
							+ "\t"+d.get("MTMRTSEAL3").toString().trim() // VALUE
					
					);
				}
				if(Optional.ofNullable(d.get("MTMRTSEAL4")).orElse("").toString().trim().length() != 0) {
					ps.println(d.get("USCNO").toString().trim().replace(" ", "") 
							+ "\tCLDATA" 
							+ "\tTTB"// CHARACT
							+ "\t"+d.get("MTMRTSEAL4").toString().trim() // VALUE
					
					);
				}
			}
			ps.println(d.get("USCNO").toString().trim().replace(" ", "")+"\t&ENDE");
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
