package com.spdcl.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.spdcl.model.IdTypes;
public class GeneratePAYMENT {

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
				 +"\tFKKKO"
				 +"\t"+d.get("CB_SD_DT").toString().trim()//Document Date // Bill Date
				 +"\t"+d.get("CB_SD_DT").toString().trim()//Posting Date in the Document/ Last  SD Payment Receipt Date
				 +"\t"//Number of A FI-CA document - Security Number
				 );
		
		ps.println(d.get("CTUSCNO").toString().trim()
				 +"\tFKKOPK"
				 +"\t5999304"//General Account Number
				 +"\t"+d.get("B_ARREA").toString().trim()
				 +"\t"+d.get("P_CENTER").toString().trim()//Profit Center
				 +"\t"+d.get("CB_SD").toString().trim()//Amount in Transaction Currency with +/- Sign Security Amount
				 );

		ps.println(d.get("CTUSCNO")+"\t&ENDE");
		}
		if(!d.get("CB_ACD").toString().trim().equals("0")) {
			ps.println(d.get("CTUSCNO").toString().trim()+"-1"
					 +"\tFKKKO"
					 +"\t20230420"//Document Date // Bill Date
					 +"\t20230420"//Posting Date in the Document/ Last  SD Payment Receipt Date
					 +"\t"//Number of A FI-CA document - Security Number
					 );
			
			ps.println(d.get("CTUSCNO").toString().trim()+"-1"
					 +"\tFKKOPK"
					 +"\t5999304"//General Account Number
					 +"\t"+d.get("B_ARREA").toString().trim()
					 +"\t"+d.get("P_CENTER").toString().trim()//Profit Center
					 +"\t"+d.get("CB_ACD").toString().trim()//Amount in Transaction Currency with +/- Sign Security Amount
					 );

			ps.println(d.get("CTUSCNO")+"-1"+"\t&ENDE");
			}
		if(!d.get("CB_ISD").toString().trim().equals("0")) {
			ps.println(d.get("CTUSCNO").toString().trim()+"-2"
					 +"\tFKKKO"
					 +"\t20230502"//Document Date // Bill Date
					 +"\t20230502"//Posting Date in the Document/ Last  SD Payment Receipt Date
					 +"\t"//Number of A FI-CA document - Security Number
					 );
			
			ps.println(d.get("CTUSCNO").toString().trim()+"-2"
					 +"\tFKKOPK"
					 +"\t5999304"//General Account Number
					 +"\t"+d.get("B_ARREA").toString().trim()
					 +"\t"+d.get("P_CENTER").toString().trim()//Profit Center
					 +"\t"+d.get("CB_ISD").toString().trim()//Amount in Transaction Currency with +/- Sign Security Amount
					 );

			ps.println(d.get("CTUSCNO")+"-2"+"\t&ENDE");
			}
	
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
