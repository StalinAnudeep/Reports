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
public class GenerateTECHINSTALLATION {
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
	
	private static Map<String,List<Register>> getUpdatedData(List<Map<String, Object>>  data){
		 Map<String,List<Register>> map = new HashMap<>();
		 
		 for(Map<String, Object> d : data) {
			if( map.containsKey(d.get("USCNO_T").toString().trim()))
			{
				map.get(d.get("USCNO_T").toString().trim()).add(new Register(d.get("VALIDFROMT").toString(), d.get("USCNO").toString()));
			}else {
				List<Register> list = new ArrayList<Register>();
			//System.out.println(d.get("ACT_COM_DT").toString());
				list.add(new Register(d.get("VALIDFROMT").toString(), d.get("USCNO").toString()));
				map.put(d.get("USCNO_T").toString().trim(), list);
			}
		 }

		return map;
	}
	private static void printPageOne(PrintStream ps,Map<String,List<Register>>   data) {
		try {
			
			for(Map.Entry<String, List<Register>> entry :data.entrySet()) {
				System.out.println(entry.getKey());
				ps.println(entry.getKey().toString().trim()
						 +"\tDI_INT"
						 +"\t"+entry.getKey().toString().trim()//INSTALLATION  
						 +"\t"+entry.getValue().get(0).getACT_COM_DT()//ACTIVITY DATE
						 );	
				for(Register r : entry.getValue()) {
					ps.println(entry.getKey().toString().trim()//Only once
							 +"\tDI_GER"
							 /*+"\t"+entry.getKey().toString().trim()//EQUNRNEU  
*/							 +"\t"+r.getEQUIPMENT() //EQUNRNEU  
							 );
				}
				ps.println(entry.getKey()+"\t&ENDE");
			}
			
			/*Map<String, Map<String,LinkedHashMap<String, Register>> > udata =  data;
			
			for(Map.Entry<String, Map<String,LinkedHashMap<String, Register>> > sentry : udata.entrySet())
			{
				ps.println(sentry.getKey().toString().trim()
						 +"\tDI_INT"
						 +"\t"+sentry.getKey().toString().trim()//INSTALLATION  
						 +"\t"+autoinc.get(sentry.getKey())//ACTIVITY DATE
						 );	
				

			for(Map.Entry<String, LinkedHashMap<String, Register>> entry : sentry.getValue().entrySet()){
				ps.println(sentry.getKey().toString().trim()//Only once
						 +"\tDI_GER"
						 +"\t"+entry.getKey().toString().trim()//EQUNRNEU  
						 +"\t"+cserialno.get(sentry.getKey().toString().trim()).replace("-CTPT", "") //EQUNRNEU  
						 );
			}

				ps.println(sentry.getKey()+"\t&ENDE");
			}
		*/
		
	
		}catch(Exception e) {
			e.printStackTrace();
		}
	}



}
