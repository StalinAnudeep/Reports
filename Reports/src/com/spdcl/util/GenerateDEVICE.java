package com.spdcl.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.spdcl.model.IdTypes;
public class GenerateDEVICE {

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
				 +"\tEQUI"
				 +"\t"//Technical Object authorization grup 
				 +"\t"//EQUI TYPE
				 +"\t"//Manufacturer Name
				 +"\t"//Manufacturer Serail Number
				 +"\t"//Year Of Construction // Manufacturer YEar 
				 +"\t"//Month OF Construction// Manufacturer Month
				 +"\t"//Valid From Date
				 +"\t3010"// Maintenance Planning Plant // Need to Map Other Circles
				 +"\t3010"//Maintenance Plant  // Need to Map Other Circles
				 +"\t"//Material Number
				 +"\t"//Serial Number//Meter Serial Number 
				 +"\t3010"//Plant Need To Mapping
				 +"\t01"
				 );
		ps.println(d.get("CTUSCNO").toString().trim()
				+"\tEGERS"
				+"\t"//Certificate Number
				+"\t"//Certify Installed Devices
				+"\t"//
				);
		ps.println(d.get("CTUSCNO").toString().trim()
				+"\tEGERH"
				+"\t"//Device Valid Date 
				);
		ps.println(d.get("CTUSCNO").toString().trim()
				+"\tCLHEAD"
				+"\t"//Class Number
				+"\t"// Class Type
				);
		ps.println(d.get("CTUSCNO").toString().trim()
				+"\tCLDATA"
				+"\t"//Characterstic Name
				+"\t"// Characterstic value
				);
		/*ps.println(d.get("CTUSCNO").toString().trim()
				 +"\tVKP"
				 +"\t"+d.get("CTUSCNO").toString().trim()
				 +"\tHT"
				 +"\tHT00"
				 +"\t"+d.get("CTCAT").toString().trim()
				 +"\tZAFBILL_ISU_BILL_HT_SSF"
				 +"\tHT_INV01"
				 +"\t"+d.get("EROCD").toString().trim()
				 + "\tMAIL"
				 );*/
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
