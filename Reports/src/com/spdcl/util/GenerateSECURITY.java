package com.spdcl.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.spdcl.model.IdTypes;
public class GenerateSECURITY {

	public static ByteArrayInputStream generateBillTxt(List<Map<String, Object>>  data) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(out);
		for(Map<String, Object> d : data)
		printPageOne( ps,d);
		ps.close();
		return new ByteArrayInputStream(out.toByteArray());
	}
	private static void printPageOne(PrintStream ps,Object o) {
		try {
		Map<String, Object> d = (Map<String, Object>) o;
		if(!d.get("CB_SD").toString().trim().equals("0")) {
		ps.println(d.get("CTUSCNO").toString().trim()
				 +"\tSECD"
				 +"\t"+d.get("CTUSCNO").toString().trim()//Contract Account Number
				 +"\tM5"//Reason // Mapping
				 +"\t"+d.get("CB_SD_DT").toString().trim()//SECUITY START DATE
				 +"\tI3"//Interest Key
				 +"\tLEGACY MIGRATED SD AMT"//External Reference Number for Security Deposite				 
				 );
		
		ps.println(d.get("CTUSCNO").toString().trim()
				 +"\tSECC"
				 +"\t"+d.get("CB_SD").toString().trim()
				 );

		ps.println(d.get("CTUSCNO")+"\t&ENDE");
		}
		if(!d.get("CB_ACD").toString().trim().equals("0")) {
			ps.println(d.get("CTUSCNO").toString().trim()+"-1"
					 +"\tSECD"
					 +"\t"+d.get("CTUSCNO").toString().trim()//Contract Account Number
					 +"\tM3"//Reason // Mapping
					 +"\t20230420"//SECUITY START DATE
					 +"\tI3"//Interest Key
					 +"\tLEGACY MIGRATED ACD AMT"//External Reference Number for Security Deposite				 
					 );
			
			ps.println(d.get("CTUSCNO").toString().trim()+"-1"
					 +"\tSECC"
					 +"\t"+d.get("CB_ACD").toString().trim()
					 );

			ps.println(d.get("CTUSCNO")+"-1"+"\t&ENDE");
			}
		
		if(!d.get("CB_ISD").toString().trim().equals("0")) {
			ps.println(d.get("CTUSCNO").toString().trim()+"-2"
					 +"\tSECD"
					 +"\t"+d.get("CTUSCNO").toString().trim()//Contract Account Number
					 +"\tM5"//Reason // Mapping
					 +"\t20230502"//SECUITY START DATE
					 +"\tI3"//Interest Key
					 +"\tLEGACY MIGRATED ISD AMT"//External Reference Number for Security Deposite				 
					 );
			
			ps.println(d.get("CTUSCNO").toString().trim()+"-2"
					 +"\tSECC"
					 +"\t"+d.get("CB_ISD").toString().trim()
					 );

			ps.println(d.get("CTUSCNO")+"-2"+"\t&ENDE");
			}
	
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
