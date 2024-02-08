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
public class GenerateMETER_READ {
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
			        //	System.out.println("Register"+ r);
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
			//System.out.println("SERVICE NO :" +sermap);
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
				System.out.println(d.get("REG").toString().trim());
				
				lastdate.get(d.get("SERIAL_NO").toString().trim()).put(d.get("REG").toString().trim(), new Register(
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
				System.out.println(" DEF MAP"+loadDefultRegisters(d.get("REG_GROUP").toString().trim(),"X"));
				regmap.putAll(loadDefultRegisters(d.get("REG_GROUP").toString().trim(),"X"));		
			/*	System.out.println("2. "+d.get("SERIAL_NO").toString().trim());*/
				regmap.put(d.get("REG").toString().trim(), new Register(
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
	
	public static LinkedHashMap<String, Register> loadDefultRegisters(String reggrp,String devsol){
		//System.out.println(reggrp);
		LinkedHashMap<String, Register> map = new LinkedHashMap<>();
		if(reggrp.equals("H19R8D2P") || reggrp.equals("HT19R8D2")) {
			map.put("1",new 					Register("1"," ","0","X"," ","Y"));
			map.put("2",new 					Register("2"," ","0","X"," ","Y"));
			map.put("3",new 					Register("3"," ","0","X"," ","Y"));
			map.put("4",new 					Register("4"," ","0","X"," ","Y"));
			map.put("5",new 					Register("5"," ","0","X"," ","Y"));
			map.put("6",new 					Register("6"," ","0","X"," ","Y"));
			map.put("7",new 					Register("7"," ","0","X"," ","Y"));
			map.put("8",new 					Register("8"," ","0","X"," ","Y"));
			map.put("9",new 					Register("9"," ","0","X"," ","Y"));
			map.put("10",new 			    	Register("10"," ","0","X"," ","Y"));
			map.put("11",new 			    	Register("11"," ","0","X"," ","Y"));
			map.put("12",new 			     	Register("12"," ","0","X"," ","Y"));
			map.put("13",new 					Register("13"," ","0","X"," ","Y"));
			map.put("14",new 					Register("14"," ","0","X"," ","Y"));
			map.put("15",new 					Register("15"," ","0","X"," ","Y"));
			map.put("16",new 					Register("16"," ","0","X"," ","Y"));
			map.put("17",new 					Register("17"," ","0","X"," ","Y"));
			map.put("18",new 					Register("18"," ","0","X"," ","Y"));
			map.put("19",new 					Register("19"," ","0","X"," ","Y"));
		}
		else if(reggrp.equals("CTE11R8D")) {
			map.put("1",new Register("1"," ","0","X"," ","Y"));
			map.put("2",new Register("2"," ","0","X"," ","Y"));
			map.put("3",new Register("3"," ","0","X"," ","Y"));			
			map.put("4",new Register("4"," ","0","X"," ","Y"));
			map.put("5",new Register("5"," ","0","X"," ","Y"));
			map.put("6",new Register("6"," ","0","X"," ","Y"));
			map.put("7",new Register("7"," ","0","X"," ","Y"));
			map.put("8",new Register("8"," ","0","X"," ","Y"));
			map.put("9",new Register("9"," ","0","X"," ","Y"));
			map.put("10",new Register("10"," ","0","X"," ","Y"));
			map.put("11",new Register("11"," ","0","X"," ","Y"));
		}
		else if(reggrp.equals("L3PE9R6D")) {
			map.put("1",new Register("1"," ","0","X"," ","Y"));
			map.put("2",new Register("2"," ","0","X"," ","Y"));
			map.put("3",new Register("3"," ","0","X"," ","Y"));			
			map.put("4",new Register("4"," ","0","X"," ","Y"));
			map.put("5",new Register("5"," ","0","X"," ","Y"));
			map.put("6",new Register("6"," ","0","X"," ","Y"));
			map.put("7",new Register("7"," ","0","X"," ","Y"));
			map.put("8",new Register("8"," ","0","X"," ","Y"));
			map.put("9",new Register("9"," ","0","X"," ","Y"));
		}
		else {
			map.put("1",new Register("1"," ","0","X"," ","Y"));
			map.put("2",new Register("2"," ","0","X"," ","Y"));
			map.put("3",new Register("3"," ","0","X"," ","Y"));
			map.put("4",new Register("4"," ","0","X"," ","Y"));
			map.put("5",new Register("5"," ","0","X"," ","Y"));
			map.put("6",new Register("6"," ","0","X"," ","Y"));
			map.put("7",new Register("7"," ","0","X"," ","Y"));
			map.put("8",new Register("8"," ","0","X"," ","Y"));
			map.put("9",new Register("9"," ","0","X"," ","Y"));
			/*map.put("10",new Register("10"," ","0","X"," ","Y"));*/
			if(devsol.equals("X"))
			map.put("10",new Register("10","HT","0","","KWH_E","X"));
			else
			map.put("10",new Register("10"," ","0","X"," ","Y"));	
			
			map.put("11",new Register("11"," ","0","X"," ","Y"));
			map.put("12",new Register("12"," ","0","X"," ","Y"));
			map.put("13",new Register("13"," ","0","X"," ","Y"));
			map.put("14",new Register("14"," ","0","X"," ","Y"));
			map.put("15",new Register("15"," ","0","X"," ","Y"));
			map.put("16",new Register("16"," ","0","X"," ","Y"));
			map.put("17",new Register("17"," ","0","X"," ","Y"));
			map.put("18",new Register("18"," ","0","X"," ","Y"));
			map.put("19",new Register("19"," ","0","X"," ","Y"));
			map.put("20",new Register("20"," ","0","X"," ","Y"));
			map.put("21",new Register("21"," ","0","X"," ","Y"));
			map.put("22",new Register("22"," ","0","X"," ","Y"));
			map.put("23",new Register("23"," ","0","X"," ","Y"));
			map.put("24",new Register("24"," ","0","X"," ","Y"));
			map.put("25",new Register("25"," ","0","X"," ","Y"));
		}
		return map;
		
	}
	
	
}
