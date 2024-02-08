package com.spdcl.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.spdcl.model.IdTypes;
public class GenerateINSTALLATION {
	public static Map<String, String> seasonal = new HashMap<>();
	
	public static ByteArrayInputStream generateBillTxt(List<Map<String, Object>>  data) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(out);
		for(Map<String, Object> d : data)
		printPageOne( ps,d);
		ps.close();
		return new ByteArrayInputStream(out.toByteArray());
	}
	/*
	 * 
	 * 
	 * Need to change seasonal depends on Next Month 
	 */
	private static void printPageOne(PrintStream ps,Object o) {
		try {
		Map<String, Object> d = (Map<String, Object>) o;
		ps.println(d.get("CTUSCNO").toString().trim()
				 +"\tDATA"
				 +"\t"+d.get("CTUSCNO").toString().trim()//PRIMESE 
				 +"\tHT"//INSTALLATION TYPE
				 +"\t"+d.get("ACT_COM_DT").toString().trim()//INSTALLATION DATE YYYYMMDD // ACT_COM_DT
				 +"\t"+getCatCode(getCat(d.get("CTUSCNO").toString().trim(),d.get("CAT").toString().trim()))//RATE CATERGORY Based on Sub Category				
				 +"\t"+(seasonal.containsKey(d.get("CTUSCNO").toString().trim())?"HT3B":d.get("CAT").toString().trim())//BRANCHE				 
				 +"\tHT"//BILLING CLASS
				 +"\t"+d.get("MRU").toString().trim()//METER READING UNTI // Section Code // 
				 +"\t"+Optional.ofNullable(d.get("HT_AUTH_GROUP")).orElse("")//BEGRU
				 +"\tHT"//ISTYPE
				 +"\t"//SBM CODE 
				 );

		ps.println(d.get("CTUSCNO")+"\t&ENDE");
	
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private static String getTitle(String orgname) {
		return orgname.toUpperCase().contains("M/S")?"0005":"0003";
		
	}

	private static String getName(String orgname) {
		String str = orgname.toUpperCase().replace("M/S.", "").trim();
		if (str.length() > 40) {
			String tempstr = "";
			char temp[] = str.toCharArray();
			for (int i = 0; i < temp.length; i++) {
				if (i < 40)
					tempstr = tempstr + temp[i] + ((i == 39) ? "\t" : "");
				else if (i > 40 && i < 80)
					tempstr = tempstr + temp[i] + ((i == 79) ? "\t" : "");
				else
					tempstr = tempstr + temp[i];
			}
			return tempstr + "\t";
		}
		return str;
	}

	public static String getCatCode(String erocode) {
		Map<String,String> map = new HashMap<String,String>();
		map.put("HT1B","HT1B01TOW");
		map.put("HT2A1","HT2A01COM");
		map.put("HT2A3","HT2A03FUN");
		map.put("HT2B","HT2B00SPO");
		map.put("HT2C","HT2C00EVC");
		map.put("HT2D","HT2D00GPO");
		map.put("HT3A","HT3A00IND");
		map.put("HT3B","HT3B00SIN");
		map.put("HT3C","HT3C00EIN");
		map.put("HT4A","HT4A00UTL");
		map.put("HT4B","HT4B00GEN");
		map.put("HT4C","HT4C00REL");
		map.put("HT4D","HT4D00RLY");
		map.put("HT5B","HT5B00AQU");
		map.put("HT5E","HT5E00LIS");		
		return Optional.ofNullable(map.get(erocode)).orElse("");

	}
	public static String getCat(String uscno,String cat) {
	if(seasonal.containsKey(uscno)) {
		return "HT3B";
	}else return cat;
	}
	
}
