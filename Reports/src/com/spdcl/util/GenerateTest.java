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
public class GenerateTest {
	static int j = 1;
	public static ByteArrayInputStream generateBillTxt(List<Map<String, Object>>  data) {
		j=1;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Map<String, String> map = new HashMap<>();
		for(Map<String, Object> d : data) {
			map.put(d.get("EQUIPMENT").toString(), d.get("MM_CODE").toString());
		}
		Map<String,List<String>> testlist = new HashMap<>();
		map.forEach((k,v)->{
			if(testlist.containsKey(v)){
				testlist.get(v).add(k);
				
			}else {
				List<String> list = new ArrayList<>();
				list.add(k);
				testlist.put(v,list);
			}
		});
		PrintStream ps = new PrintStream(out);
		testlist.forEach((k,v)->{
			//System.out.println(k);
			int i =1 ;
			for(String s : v) {
				//ps.println("update PM_EQUIPMENTS set SEQ="+(i++)+" where EQUIPMENT='"+s+"' and MM_CODE='"+k+"';");
				//ps.println("update HT_MNP_CUBICAL_DETAILS set SEQ="+(i++)+" where EQUIPMENT_ID='"+s+"' and DEVICE_CAT='"+k+"';");
				//ps.println("update HT_CT_PT_DEVICE_CAT set SEQ="+(i++)+" where ROWID='"+s+"' and DEVICE_CAT='"+k+"';");
				// For Null Values 
				//ps.println("update HT_MNP_CUBICAL_DETAILS set SEQ="+(j++)+" where EQUIPMENT_ID='"+s+"' and DEVICE_CAT='"+k+"';");
				// MM CODE IS NULL
				//ps.println("update PM_EQUIPMENTS set SEQ="+(j++)+" where EQUIPMENT='"+s+"' and rowid='"+k+"';");
				//For Device is not null
				//ps.println("update HT_CT_PT_DEVICE_CAT set SEQ="+(j++)+" where ROWID='"+s+"' and DEVICE_CAT='"+k+"';");
				// for ct PT 33
				ps.println("update HT_CT_PT_DEVICE_CAT_MOCK2 set USCNO_SR_NO=USCNOCTPT||'_'||"+(i++)+" where PM_EQUIPMENT='"+s+"' and USCNOCTPT='"+k+"';");
			}
		});
		
		
		ps.close();
		return new ByteArrayInputStream(out.toByteArray());
	}
	}
