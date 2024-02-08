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
public class GenerateDMFULLINSTALLATION {
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
					System.out.println(rentry.getValue().getRATE_GRP());
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
						 +"\t"+cserialno.get(entry.getKey().toString().trim()) //EQUNRNEU  +"_C" NEED TO ENABLE FOR LT
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
			autoinc.put(d.get("CTUSCNO").toString().trim(),d.get("VALIDFROMT").toString().trim());
			cserialno.put(d.get("SERIAL_NO").toString().trim(),String.valueOf(d.get("C_SERIAL_NO")).equals("null")?"":d.get("C_SERIAL_NO").toString().trim());
			
			if(updata.containsKey(d.get("CTUSCNO").toString().trim())){
				
				Map<String,LinkedHashMap<String, Register>> metermap = updata.get(d.get("CTUSCNO").toString().trim());
				//System.out.println(metermap.size()+" : metermap : "+metermap);
				
				if(metermap.containsKey(d.get("SERIAL_NO").toString().trim())) {
						LinkedHashMap<String, Register> testregmap = new LinkedHashMap<>();
						testregmap.putAll(loadDefultRegisters(d.get("REG_GROUP").toString().trim(),"X"));
						//System.out.println("1" + d.get("REG_CODE").toString().trim()+":"+testregmap.containsKey(d.get("REG_CODE").toString().trim()));
						
								if(testregmap.containsKey(d.get("REG_CODE").toString().trim()) || !Optional.ofNullable(d.get("VALUE")).orElse("0").equals("0")  ) {
									//System.out.println(d.get("REG_CODE").toString().trim());
									metermap.get(d.get("SERIAL_NO").toString().trim()).put(d.get("REG").toString().trim(), new Register(
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
							if(regmap.containsKey(d.get("REG").toString().trim()) || !Optional.ofNullable(d.get("VALUE")).orElse("0").equals("0") ) {
								regmap.put(d.get("REG").toString().trim(), new Register(
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
				if(regmap.containsKey(d.get("REG").toString().trim()) || !Optional.ofNullable(d.get("VALUE")).orElse("0").equals("0") ) {
				regmap.put(d.get("REG").toString().trim(), new Register(
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
