package com.spdcl.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.spdcl.model.IdTypes;
public class GenerateOPEN_ITEMS {

	public static ByteArrayInputStream generateBillTxt(List<Map<String, Object>>  data) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(out);
	
		printPageOne( ps,getUpdateData(data));
		ps.close();
		return new ByteArrayInputStream(out.toByteArray());
	}

	private static void printPageOne(PrintStream ps, Map<String, List<Map<String, Object>>> data) {
		try {
			for (Map.Entry<String, List<Map<String, Object>>> entry : data.entrySet()) {
				int index = 0;//Need to change date
				for (Map<String, Object> d : entry.getValue()) {
					ps.println(d.get("USCNO").toString().trim()+"-"+d.get("TYPE").toString().trim() + "\tKO" + "\t20231231"// BLDAT
							+ "\t20231231"// BUDAT
							+ "\t"// XBLNR
					);
					ps.println(d.get("USCNO").toString().trim()+"-"+d.get("TYPE").toString().trim() + "\tOP" 
					+ "\t" + d.get("B_AREA").toString().trim()// GSBER
							+ "\t" + d.get("USCNO").toString().trim()// GPART
							+ "\t" + d.get("USCNO").toString().trim()// VKONT
							+ "\t" + d.get("MT").toString().trim()// HVORG
							+ "\t" + d.get("SMT").toString().trim()// TVORG
							+ "\t" + d.get("CAT").toString().trim()// KOFIZ
							+ "\t" + d.get("GL_AC").toString().trim()// HKONT
							+ "\t" //STAKZ
							+ "\t"+d.get("TYPE_DESC").toString().trim()+"-"+d.get("USCNO").toString().trim()// OPTXT
							+ "\t20231231"// FAEDN // SNAP CHART or LATEST LEDGER DATE
							+ "\t" + d.get("VALUE").toString().trim()// BETRW
							+ "\t" + d.get("AUGRS").toString().trim()//AUGRS //
							+"\t"+ (d.get("NEW_COL").toString().trim().equals("ED")?"I5":"")
							+ "\tHTSEG "// SEGMENT
							+ "\t" + d.get("P_AREA").toString().trim()// PRCTR
					);
					ps.println(d.get("USCNO").toString().trim()+"-"+d.get("TYPE").toString().trim() + "\tOPK" 
									+ "\t5999302"// HKONT
									+ "\t" + d.get("B_AREA").toString().trim()// GSBER
									+ "\t" + d.get("P_AREA").toString().trim()// PRCTR
									+ "\t" + (d.get("VALUE").toString().trim().contains("-")?d.get("VALUE").toString().trim().replace("-", ""):"-"+d.get("VALUE").toString().trim())// BETRW
									+ "\tHTSEG"// SEGMENT
							);
					ps.println(entry.getKey()+"-"+d.get("TYPE").toString().trim()  + "\t&ENDE");
					index++;
				}
				
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

public static Map<String,List<Map<String, Object>>> getUpdateData(List<Map<String, Object>> data){
	Map<String,List<Map<String, Object>>> map = new HashMap<>();
	for(Map<String, Object> d : data) {
		if(map.containsKey(d.get("USCNO").toString().trim())) {
			map.get(d.get("USCNO").toString().trim()).add(d);
		}else {
			List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
			list.add(d);			
			map.put(d.get("USCNO").toString().trim(), list);
		}
	}
	return map;
}
	
}
