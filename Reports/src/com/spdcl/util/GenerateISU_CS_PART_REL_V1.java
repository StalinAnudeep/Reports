package com.spdcl.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.spdcl.model.IdTypes;
public class GenerateISU_CS_PART_REL_V1 {

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
				 +"\tINIT"
				 +"\t"+d.get("CTUSCNO").toString().trim()
				 +"\t"+d.get("CTUSCNO").toString().trim()
				+"\t99991231"
				+"\t"+Optional.ofNullable(d.get("CTSUPCONDT")).orElse("")
				+ "\tBUR001");
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

}
