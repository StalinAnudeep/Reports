package com.spdcl.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class GenerateDEVICEGRP {
	static Map<String,String> autoinc = new HashMap<>();
	static Map<String,String> cserialno = new HashMap<>();
	
	public static ByteArrayInputStream generateBillTxt(List<Map<String, Object>>  data) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(out);
		/*for(Map<String, Object> d : data)*/
		//printPageOne( ps,data);
		printPageOne( ps,getUpdatedData(data));
		ps.close();
		return new ByteArrayInputStream(out.toByteArray());
	}
	
	private static Map<String,Map<String,List<Register>>> getUpdatedData(List<Map<String, Object>>  data){
		Map<String,Map<String,List<Register>>> map = new HashMap<>();
		 
		 for(Map<String, Object> d : data) {
			if( map.containsKey(d.get("USCNO_T").toString().trim()))
			{
				if(map.get(d.get("USCNO_T").toString().trim()).containsKey(d.get("EQUIPMENT_ID").toString().trim())) {
					map.get(d.get("USCNO_T").toString().trim()).get(d.get("EQUIPMENT_ID").toString().trim()).add(new Register(d.get("VALIDFROMT").toString(), d.get("HT_AUTH_GROUP").toString(), d.get("USCNO").toString()));
				}else {
					List<Register> list = new ArrayList<Register>();
					//System.out.println(d.get("ACT_COM_DT").toString());
						list.add(new Register(d.get("VALIDFROMT").toString(), d.get("HT_AUTH_GROUP").toString(), d.get("USCNO").toString()));
					map.get(d.get("USCNO_T").toString().trim()).put(d.get("EQUIPMENT_ID").toString().trim(), list);
				}
			}else {
				Map<String,List<Register>> emap = new HashMap<>();
				List<Register> list = new ArrayList<Register>();
			//System.out.println(d.get("ACT_COM_DT").toString());
				list.add(new Register(d.get("VALIDFROMT").toString(), d.get("HT_AUTH_GROUP").toString(), d.get("USCNO").toString()));
				emap.put(d.get("EQUIPMENT_ID").toString().trim(), list);
				map.put(d.get("USCNO_T").toString().trim(), emap);
			}
		 }

		return map;
	}
	private static void printPageOne(PrintStream ps,Map<String,Map<String,List<Register>>>   data) {
		try {
			
			for(Map.Entry<String, Map<String,List<Register>>> entry :data.entrySet()) {
				/*System.out.println(entry.getKey());
				System.out.println(entry.getValue());*/
				

				int i = 0;
				for (Map.Entry<String, List<Register>> ntry : entry.getValue().entrySet()) {
					String uscno = entry.getKey().toString().trim();
					uscno = uscno + (i == 0 ? "" : "_" + i);
					ps.println(uscno + "\tEDEVGR" 
					+ "\t0003" 
					+"\t"+ntry.getValue().get(0).getEQUIPMENT()
					+ "\t"+ntry.getValue().get(0).getACT_COM_DT()// ACTIVITY DATE
					);
					for (Register r : ntry.getValue()) {
						// System.out.println("\t\t"+r);
						ps.println(uscno + "\tDEVICE" + "\t" + r.getC_SERIAL_NO());
					}
					ps.println(uscno + "\t&ENDE");
					// System.out.println("\t"+ntry.getKey());
					i++;
				}
				
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}



}
