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
import java.util.Set;

import com.spdcl.model.IdTypes;
public class GenerateCTMETER_READ {
	public static Map<String,LinkedHashMap<String, Register>> lastdate = new HashMap<>(); 
	public static Map<String, Integer> servicenocount = new HashMap<>();
	public static ByteArrayInputStream generateBillTxt(List<Map<String, Object>>  data) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(out);
		printPageOne(ps,getUpdateData(data));
		ps.close();
		return new ByteArrayInputStream(out.toByteArray());
	}
	private static void printPageOne(PrintStream ps,LinkedHashMap<String, List<Map<String, Object>>>  o) {
		try {

			Map<String,Integer> sermap = new HashMap<String,Integer>();
			for(Map.Entry<String, List<Map<String, Object>>> entry : o.entrySet()) {
				int i =0;
				List<Map<String, Object>> list = entry.getValue();
				for(Map<String, Object> d:list) {
					/*if(sermap.containsKey(entry.getKey().toString().trim()))
						sermap.put(entry.getKey().toString().trim(), (sermap.get(entry.getKey().toString().trim().split("-")[0])+1));
					else*/
						sermap.put(entry.getKey().toString().trim(), 1);
				if(d.get("MOVE_IN").toString().trim().equals("09")) {
						ps.println(entry.getKey().toString().trim()
								 +"\tIEABLU"
								 +"\t"+d.get("SERIAL_NO").toString().trim()//EQUP NO
								 +"\t"+(d.get("REG").toString().trim().length()==1?"00"+d.get("REG").toString().trim() :"0"+d.get("REG").toString().trim())//Register 001 002 003 
								 +"\t"+d.get("MOVE_IN").toString().trim()//Meter Reading Reason 09 history - 06 Move in 
							/*	 +"\t"+d.get("VALUE").toString().trim()//Meter Reading Recorded  
*/								 +"\t"+(d.get("VALUE").toString().trim().equals("0")?d.get("VALUE").toString().trim():(d.get("VALUE").toString().trim().contains(".")?d.get("VALUE").toString().trim():d.get("VALUE").toString().trim()+".00"))//ZWSTANDCE
								 +"\t"+(d.get("MDCLKWHSTAT_HT").toString().trim().length()==1?"0"+d.get("MDCLKWHSTAT_HT").toString().trim():d.get("MDCLKWHSTAT_HT").toString().trim())
								 +"\t11"//Meter Reading Type 11 actual or 12 Estimated
								 +"\t"//ABLESER
								 +"\t"+d.get("MDCLRDG_DT").toString().trim()//METER Reading Date Date Formte YYYYMMDD 
								 );
				}else{
					if(i==0) {
					LinkedHashMap<String, Register> l = lastdate.get(d.get("SERIAL_NO").toString().trim());
					Set<String> keys = l.keySet();			        
			        // printing the elements of LinkedHashMap
			        for (String key : keys) {
			        	Register r = l.get(key);
			        	System.out.println("Register"+ r);
			        	ps.println(entry.getKey().toString().trim()
								 +"\tIEABLU"
								 +"\t"+Optional.ofNullable(r.getSERIAL_NO()).orElse(d.get("SERIAL_NO").toString().trim())//EQUP NO
								 +"\t"+(r.getREG())//Register 001 002 003 
								 +"\t"+d.get("MOVE_IN").toString().trim()//Meter Reading Reason 09 history - 06 Move in 
								/* +"\t"+r.getVALUE().trim()//Meter Reading Recorded 
*/								  +"\t"+(r.getVALUE().toString().trim().equals("0")?r.getVALUE().toString().trim():(r.getVALUE().toString().trim().contains(".")?r.getVALUE().toString().trim():r.getVALUE().toString().trim()+".00"))//ZWSTANDCE
								 +"\t"+(d.get("MDCLKWHSTAT_HT").toString().trim().length()==1?"0"+d.get("MDCLKWHSTAT_HT").toString().trim():d.get("MDCLKWHSTAT_HT").toString().trim())
								 +"\t11"//Meter Reading Type 11 actual or 12 Estimated
								  +"\t"//ABLESER
								 +"\t"+d.get("MDCLRDG_DT").toString().trim()//METER Reading Date Date Formte YYYYMMDD 
								 );
			        	}
			        i=1;
					}
				  }
				}
/*				System.out.println(servicenocount.get(entry.getKey().toString().trim().split("-")[0])+"=="+sermap.get(entry.getKey().toString().trim().split("-")[0]));
				if(servicenocount.get(entry.getKey().toString().trim().split("-")[0]).equals(sermap.get(entry.getKey().toString().trim().split("-")[0])))*/
				ps.println(entry.getKey().toString().trim()+"\t&ENDE");
			}
			System.out.println("SERVICE NO :" +sermap);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static LinkedHashMap<String, List<Map<String, Object>>> getUpdateData(List<Map<String, Object>>  data){
		LinkedHashMap<String, List<Map<String, Object>>>  updata = new LinkedHashMap<String, List<Map<String, Object>>>();
		
		for(Map<String, Object> d : data) {
			if(updata.containsKey(d.get("SERIAL_NO").toString().trim())) {
				updata.get(d.get("SERIAL_NO").toString().trim()).add(d);
			}else {
				List<Map<String, Object>> list = new ArrayList<>();
				list.add(d);
				updata.put(d.get("SERIAL_NO").toString().trim(), list);
			}
			
			if(d.get("MOVE_IN").toString().trim().equals("06")) {
			if(lastdate.containsKey(d.get("SERIAL_NO").toString().trim())) {
				/*System.out.println("1. "+d.get("SERIAL_NO").toString().trim());*/
				
				lastdate.get(d.get("SERIAL_NO").toString().trim()).put(d.get("REG_CODE").toString().trim(), new Register(
						d.get("REG").toString().trim(),
						d.get("RATE_GRP").toString().trim(), 
						d.get("VALUE").toString().trim(), 
						d.get("IS_BILLABLE").toString().trim(),
						d.get("REG_CODE").toString().trim(),
						d.get("ACT_COM_DT").toString().trim(),
						String.valueOf(d.get("C_SERIAL_NO")).equals("null")?"":d.get("C_SERIAL_NO").toString().trim(),
								d.get("SERIAL_NO").toString().trim(),0));
				
			}else {
				LinkedHashMap<String, Register> regmap = new LinkedHashMap<>();
				regmap.putAll(loadDefultRegisters(d.get("REG_GROUP").toString().trim()));		
			/*	System.out.println("2. "+d.get("SERIAL_NO").toString().trim());*/
				regmap.put(d.get("REG_CODE").toString().trim(), new Register(
						d.get("REG").toString().trim(),
						d.get("RATE_GRP").toString().trim(), 
						d.get("VALUE").toString().trim(), 
						d.get("IS_BILLABLE").toString().trim(),
						d.get("REG_CODE").toString().trim(),
						d.get("ACT_COM_DT").toString().trim(),
						String.valueOf(d.get("C_SERIAL_NO")).equals("null")?"":d.get("C_SERIAL_NO").toString().trim(),
								d.get("SERIAL_NO").toString().trim(),0));
				
				
				lastdate.put(d.get("SERIAL_NO").toString().trim(), regmap);
			}
			}
		}
		
		
		return updata;
		
	}
	
	public static LinkedHashMap<String, Register> loadDefultRegisters(String reggrp){
		//System.out.println(reggrp);
		LinkedHashMap<String, Register> map = new LinkedHashMap<>();
		if(reggrp.equals("H19R8D2P") || reggrp.equals("HT19R8D2")) {
			map.put("KWH",new 					Register("1"," ","0","X"," ","Y"));
			map.put("KVAH",new 					Register("2"," ","0","X"," ","Y"));
			map.put("KVA",new 					Register("3"," ","0","X"," ","Y"));
			map.put("T1_KVAH",new 				Register("4"," ","0","X"," ","Y"));
			map.put("T2_KVAH",new 				Register("5"," ","0","X"," ","Y"));
			map.put("T3_KVAH",new 				Register("6"," ","0","X"," ","Y"));
			map.put("T4_KVAH",new 				Register("7"," ","0","X"," ","Y"));
			map.put("T5_KVAH",new 				Register("8"," ","0","X"," ","Y"));
			map.put("T6_KVAH",new 				Register("9"," ","0","X"," ","Y"));
			map.put("KVARH(LAG)",new 			Register("10"," ","0","X"," ","Y"));
			map.put("KVARH(LEAD)",new 			Register("11"," ","0","X"," ","Y"));
			map.put("RESET COUNT",new 			Register("12"," ","0","X"," ","Y"));
			map.put("CMD",new 					Register("13"," ","0","X"," ","Y"));
			map.put("VR",new 					Register("14"," ","0","X"," ","Y"));
			map.put("VY",new 					Register("15"," ","0","X"," ","Y"));
			map.put("VB",new 					Register("16"," ","0","X"," ","Y"));
			map.put("IR",new 					Register("17"," ","0","X"," ","Y"));
			map.put("IY",new 					Register("18"," ","0","X"," ","Y"));
			map.put("IB",new 					Register("19"," ","0","X"," ","Y"));
		}
		else if(reggrp.equals("CTE11R8D")) {
			map.put("KWH",new Register("1"," ","0","X"," ","Y"));
			map.put("KVAH",new Register("2"," ","0","X"," ","Y"));
			map.put("KVA",new Register("3"," ","0","X"," ","Y"));			
			map.put("KVARH(LAG)",new Register("4"," ","0","X"," ","Y"));
			map.put("KVARH(LEAD)",new Register("5"," ","0","X"," ","Y"));
			map.put("VR",new Register("6"," ","0","X"," ","Y"));
			map.put("VY",new Register("7"," ","0","X"," ","Y"));
			map.put("VB",new Register("8"," ","0","X"," ","Y"));
			map.put("IR",new Register("9"," ","0","X"," ","Y"));
			map.put("IY",new Register("10"," ","0","X"," ","Y"));
			map.put("IB",new Register("11"," ","0","X"," ","Y"));
		}
		else if(reggrp.equals("L3PE9R6D")) {
			map.put("KWH",new Register("1"," ","0","X"," ","Y"));
			map.put("KVAH_CL",new Register("2"," ","0","X"," ","Y"));
			map.put("KVA",new Register("3"," ","0","X"," ","Y"));			
			map.put("VR",new Register("4"," ","0","X"," ","Y"));
			map.put("VY",new Register("5"," ","0","X"," ","Y"));
			map.put("VB",new Register("6"," ","0","X"," ","Y"));
			map.put("IR",new Register("7"," ","0","X"," ","Y"));
			map.put("IY",new Register("8"," ","0","X"," ","Y"));
			map.put("IB",new Register("9"," ","0","X"," ","Y"));
		}
		else {
			map.put("KWH",new Register("1"," ","0","X"," ","Y"));
			map.put("KVAH",new Register("2"," ","0","X"," ","Y"));
			map.put("KVA",new Register("3"," ","0","X"," ","Y"));
			map.put("T1_KVAH",new Register("4"," ","0","X"," ","Y"));
			map.put("T2_KVAH",new Register("5"," ","0","X"," ","Y"));
			map.put("T3_KVAH",new Register("6"," ","0","X"," ","Y"));
			map.put("T4_KVAH",new Register("7"," ","0","X"," ","Y"));
			map.put("T5_KVAH",new Register("8"," ","0","X"," ","Y"));
			map.put("T6_KVAH",new Register("9"," ","0","X"," ","Y"));
			map.put("KWH_E",new Register("10"," ","0","X"," ","Y"));				
			map.put("KVAH(E)",new Register("11"," ","0","X"," ","Y"));
			map.put("RMD(E)",new Register("12"," ","0","X"," ","Y"));
			map.put("KVARH(LAG)",new Register("13"," ","0","X"," ","Y"));
			map.put("KVARH(LEAD)",new Register("14"," ","0","X"," ","Y"));
			map.put("KVARH(LAG E)",new Register("15"," ","0","X"," ","Y"));
			map.put("KVARH(LEAD E)",new Register("16"," ","0","X"," ","Y"));
			map.put("RESET COUNT",new Register("17"," ","0","X"," ","Y"));
			map.put("CMD",new Register("18"," ","0","X"," ","Y"));
			map.put("CMD(E)",new Register("19"," ","0","X"," ","Y"));
			map.put("VR",new Register("20"," ","0","X"," ","Y"));
			map.put("VY",new Register("21"," ","0","X"," ","Y"));
			map.put("VB",new Register("22"," ","0","X"," ","Y"));
			map.put("IR",new Register("23"," ","0","X"," ","Y"));
			map.put("IY",new Register("24"," ","0","X"," ","Y"));
			map.put("IB",new Register("25"," ","0","X"," ","Y"));
		}
		return map;
		
	}
	
	
}
