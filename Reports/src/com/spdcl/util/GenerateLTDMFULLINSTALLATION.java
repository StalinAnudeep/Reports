package com.spdcl.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.spdcl.model.IdTypes;
public class GenerateLTDMFULLINSTALLATION {
	static Map<String,String> autoinc = new HashMap<>();
	static Map<String,String> cserialno = new HashMap<>();
	
	public static ByteArrayInputStream generateBillTxt(List<Map<String, Object>>  data) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(out);
		/*for(Map<String, Object> d : data)*/
		printPageOne( ps,data);
		ps.close();
		return new ByteArrayInputStream(out.toByteArray());
	}
	private static void printPageOne(PrintStream ps,List<Map<String, Object>>  data) {
		try {
			Map<String, Map<String,LinkedHashMap<String, Register>> > udata = getData( data);
			
			for(Map.Entry<String, Map<String,LinkedHashMap<String, Register>> > sentry : udata.entrySet())
			{
				ps.println(sentry.getKey().toString().trim()
						 +"\tDI_INT"
						 +"\t"+sentry.getKey().toString().trim().split("_")[0]//INSTALLATION  // USCNO
						 +"\t"+autoinc.get(sentry.getKey())//ACTIVITY DATE
						 );	
				
			for(Map.Entry<String, LinkedHashMap<String, Register>> entry : sentry.getValue().entrySet()){
			
				for(Map.Entry<String, Register> rentry: entry.getValue().entrySet()) {	
					/*System.out.println("DEV_SOL" + rentry.getValue().getDEV_SOL().toString().trim());*/
					ps.println(sentry.getKey().toString().trim()// Repeat 19 or 25
							 +"\tDI_ZW"
							 +"\t"+rentry.getValue().getREG().toString().trim()//ZWNUMMERE 
							 +"\t"+rentry.getValue().getRATE_GRP().toString().trim()//KONDIGRE 
							 +"\t"+(rentry.getValue().getVALUE().toString().trim().equals("0")?rentry.getValue().getVALUE().toString().trim():(rentry.getValue().getVALUE().toString().trim().contains(".")?rentry.getValue().getVALUE().toString().trim():rentry.getValue().getVALUE().toString().trim()+".00"))//ZWSTANDCE
							 +"\t"+rentry.getValue().getIS_BILLABLE().toString().trim()//ZWNABR 
							 +"\t"+rentry.getValue().getREG_CODE().toString().trim()//TARIFART 
							 +"\t1"//PERVERBR
							 +"\t"+entry.getKey().toString().trim()//EQUNRE  
							 );
				}
			}
			for(Map.Entry<String, LinkedHashMap<String, Register>> entry : sentry.getValue().entrySet()){
				ps.println(sentry.getKey().toString().trim()//Only once
						 +"\tDI_GER"
						 +"\t"+entry.getKey().toString().trim()//EQUNRNEU  
						 +"\t"+cserialno.get(entry.getKey().toString().trim())+"_C"  //EQUNRNEU  NEED TO ENABLE FOR LT
						 );
			}
				for (Map.Entry<String, LinkedHashMap<String, Register>> entry : sentry.getValue().entrySet()) {
					for (Map.Entry<String, Register> rentry : entry.getValue().entrySet()) {
						ps.println(
								sentry.getKey().toString().trim() + "\tDI_EAB" + "\t" + entry.getKey().toString().trim()// EQUNR Repeat 19 or 25
										+ "\t" + rentry.getValue().getREG().toString().trim()// ZWNUMMER
										+ "\t11"// ISTABLART
						);
					}
				}
				ps.println(sentry.getKey().toString().trim()
						 +"\tDI_CNT"
						 +"\tX"//NO_AUTOMOV 
						);
				ps.println(sentry.getKey()+"\t&ENDE");
			}
		
		
	
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	public static Map<String, Map<String,LinkedHashMap<String, Register>> > getData(List<Map<String, Object>> data){
		
		Map<String, Map<String,LinkedHashMap<String, Register>>>  updata = new HashMap<>();
		
		for(Map<String, Object> d : data) {		
			autoinc.put(d.get("CTUSCNO").toString().trim(),d.get("ACT_COM_DT").toString().trim());
			cserialno.put(d.get("SERIAL_NO").toString().trim(),String.valueOf(d.get("C_SERIAL_NO")).equals("null")?"":d.get("C_SERIAL_NO").toString().trim());
			
			if(updata.containsKey(d.get("CTUSCNO").toString().trim())){
				
				Map<String,LinkedHashMap<String, Register>> metermap = updata.get(d.get("CTUSCNO").toString().trim());
				//System.out.println(metermap.size()+" : metermap : "+metermap);
				
				if(metermap.containsKey(d.get("SERIAL_NO").toString().trim())) {
						LinkedHashMap<String, Register> testregmap = new LinkedHashMap<>();
						testregmap.putAll(loadDefultRegisters(d.get("REG_GROUP").toString().trim(),"X"));
						//System.out.println("1" + d.get("REG_CODE").toString().trim()+":"+testregmap.containsKey(d.get("REG_CODE").toString().trim()));
						
								if(testregmap.containsKey(d.get("REG_CODE").toString().trim())) {
									//System.out.println(d.get("REG_CODE").toString().trim());
									metermap.get(d.get("SERIAL_NO").toString().trim()).put(d.get("REG_CODE").toString().trim(), new Register(
										d.get("REG").toString().trim(),
										d.get("RATE_GRP").toString().trim(), 
										d.get("VALUE").toString().trim(), 
										d.get("IS_BILLABLE").toString().trim(),
										d.get("REG_CODE").toString().trim(),
										d.get("ACT_COM_DT").toString().trim(),
										String.valueOf(d.get("C_SERIAL_NO")).equals("null")?"":d.get("C_SERIAL_NO").toString().trim(),
												"X"));
								}
				}else {
					
							Map<String,LinkedHashMap<String, Register>> ometermap = new HashMap<>();							
							LinkedHashMap<String, Register> regmap = new LinkedHashMap<>();
							regmap.putAll(loadDefultRegisters(d.get("REG_GROUP").toString().trim(),"X"));
							//System.out.println(regmap.size() + " :: " +d.get("REG_GROUP").toString().trim() + " : :");
							//System.out.println("2" + d.get("REG_CODE").toString().trim()+":"+regmap.containsKey(d.get("REG_CODE").toString().trim()));
								if(regmap.containsKey(d.get("REG_CODE").toString().trim())) {
								regmap.put(d.get("REG_CODE").toString().trim(), new Register(
										d.get("REG").toString().trim(),
										d.get("RATE_GRP").toString().trim(), 
										d.get("VALUE").toString().trim(), 
										d.get("IS_BILLABLE").toString().trim(),
										d.get("REG_CODE").toString().trim(),
										d.get("ACT_COM_DT").toString().trim(),
										String.valueOf(d.get("C_SERIAL_NO")).equals("null")?"":d.get("C_SERIAL_NO").toString().trim(),
												"X"));
								
								ometermap.put(d.get("SERIAL_NO").toString().trim(), regmap);
								}
								updata.get(d.get("CTUSCNO").toString().trim()).putAll(ometermap);
						
						}
				}
			else {
				
				Map<String,LinkedHashMap<String, Register>> metermap = new HashMap<>();
				System.out.println(String.valueOf(d.get("C_SERIAL_NO")).equals("null")?"":d.get("C_SERIAL_NO").toString().trim());
				System.out.println(d.get("REG_GROUP").toString().trim());
				LinkedHashMap<String, Register> regmap = new LinkedHashMap<>();
				regmap.putAll(loadDefultRegisters(d.get("REG_GROUP").toString().trim(),"X"));
				System.out.println(d.get("REG_CODE").toString().trim());
				//System.out.println(regmap.size() + " :: " +d.get("REG_GROUP").toString().trim() + " : :");
				//System.out.println("2" + d.get("REG_CODE").toString().trim()+":"+regmap.containsKey(d.get("REG_CODE").toString().trim()));
				if(regmap.containsKey(d.get("REG_CODE").toString().trim())) {
				regmap.put(d.get("REG_CODE").toString().trim(), new Register(
						d.get("REG").toString().trim(),
						d.get("RATE_GRP").toString().trim(), 
						d.get("VALUE").toString().trim(), 
						d.get("IS_BILLABLE").toString().trim(),
						d.get("REG_CODE").toString().trim(),
						d.get("ACT_COM_DT").toString().trim(),
						String.valueOf(d.get("C_SERIAL_NO")).equals("null")?"":d.get("C_SERIAL_NO").toString().trim(),
								"X"));
				
				metermap.put(d.get("SERIAL_NO").toString().trim(), regmap);
				updata.put(d.get("CTUSCNO").toString().trim(), metermap);
				}
			}
			
		}
		
		return updata;
	}

	public static LinkedHashMap<String, Register> loadDefultRegisters(String reggrp,String devsol){
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
			map.put("KVAH_CL",new Register("2"," ","0","X"," ","Y"));
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
			if(devsol.equals("X"))
			map.put("KWH_E",new Register("10","LT","0"," ","KWH_E","X"));
			else
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
