package com.spdcl.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.spdcl.model.IdTypes;
public class GenerateDISCORDER {

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
				 +"\tHEADER"
				 +"\t"+d.get("CTUSCNO").toString().trim()
				 +"\t"+d.get("DISCONNECT_DATE").toString().trim()				 			 
				 );

		ps.println(d.get("CTUSCNO")+"\t&ENDE");
		
	
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
}
