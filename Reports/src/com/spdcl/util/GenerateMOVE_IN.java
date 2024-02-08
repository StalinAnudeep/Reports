package com.spdcl.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.spdcl.model.IdTypes;
public class GenerateMOVE_IN {

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
		ps.println(d.get("CTUSCNO").toString().trim()
				 +"\tEVER"
				 +"\t"+d.get("CTCAT").toString().trim()//AD ID (Account determination Id)// Category //
				 +"\t"// VABSCHLEVU Date on which utility concluded the contract
				 + "\t"+Optional.ofNullable(d.get("HT_AUTH_GROUP")).orElse("")//BEGRU
				 +"\t"+d.get("CTUSCNO").toString().trim()//INSTALLATION NO
				 +"\t"+d.get("CTUSCNO").toString().trim()//Contract Account Number
				 +"\t"+d.get("RDG_CLS_DT").toString().trim()//MOVE IN DATE 
				+"\t"+d.get("CTSUPCONDT").toString().trim()//Move in date in legacy // Date of Connection 
			 );

		ps.println(d.get("CTUSCNO")+"\t&ENDE");
	
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	

	

	
}
