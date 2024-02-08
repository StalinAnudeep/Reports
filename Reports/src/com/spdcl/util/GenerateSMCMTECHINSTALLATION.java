package com.spdcl.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.spdcl.model.IdTypes;
public class GenerateSMCMTECHINSTALLATION {
	static Map<String,String> autoinc = new HashMap<>();
	static Map<String,String> cserialno = new HashMap<>();
	
	public static ByteArrayInputStream generateBillTxt(List<Map<String, Object>>  data) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(out);
		printPageOne( ps,data);
		ps.close();
		return new ByteArrayInputStream(out.toByteArray());
	}
	

	private static void printPageOne(PrintStream ps,List<Map<String, Object>>  data) {
		try {
			
			for(Map<String, Object> d : data) {
				 ps.println(d.get("SERIAL_NO").toString().trim()
						 +"\tDI_INT"
						 +"\t"+d.get("CTUSCNO").toString().trim()//
						 +"\t"+d.get("VALIDFROMT").toString().trim()//ACTIVITY DATE
						 );	
				 for(int i=1;i<=getNoofReg(d.get("REG_GROUP").toString().trim());i++)
					 ps.println(d.get("SERIAL_NO").toString().trim().trim()//
							 +"\tDI_ZW"
							 +"\t"+i// 
							 +"\t"+0// 
							 +"\t"+1//
							 +"\t"+d.get("SERIAL_NO").toString().trim() // 
							 );
				 ps.println(d.get("SERIAL_NO").toString().trim()//Only once
						 +"\tDI_GER"
						 +"\t"+d.get("SERIAL_NO").toString().trim() //
						 +"\t"+d.get("C_SERIAL_NO").toString().trim() 
						 );
				 ps.println(d.get("SERIAL_NO").toString().trim()+"\t&ENDE");
			}
	
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

public static int getNoofReg(String regcode){
	Map<String,Integer> map = new HashMap<>();
	map.put("H19R8D2P",19);
	map.put("HTS25R82",25);
	map.put("HT19R8D2",19);
	map.put("HS25R8D2",25);
	return map.get(regcode);
}

}
