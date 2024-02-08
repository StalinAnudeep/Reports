package com.spdcl.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.spdcl.model.IdTypes;
public class GenerateISU_CS_FICA_ACCOUNT_V1 {

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
				 +"\t"+d.get("CTUSCNO").toString().trim());
		ps.println(d.get("CTUSCNO").toString().trim()
				 +"\tVKP"
				 +"\t"+d.get("CTUSCNO").toString().trim()//PARTNER
				 +"\tI2"//IKEY
				 +"\t"+(Optional.ofNullable(d.get("MTR_STATUS")).orElse("").toString().trim().equals("02")?"MO":"HT")//MAHNV 
				  +"\t"+Optional.ofNullable(d.get("HT_AUTH_GROUP")).orElse("")//BEGRU
				 +"\tHT00"
				 +"\t"+d.get("CTCAT").toString().trim()
				 +"\tZAFBILL_ISU_BILL_HT_SSF"
				 +"\tHT_INV01"				
				/* +"\t"+getSapEroCode(d.get("EROCD").toString().trim())*/
				 + "\t"+d.get("B_ARREA").toString().trim()
				 +"\t"+Optional.ofNullable(d.get("TDS_FLAG")).orElse("")// IS for 10 percent IT   NEED TO CHAGNE
				 +"\t"+Optional.ofNullable(d.get("LANDL")).orElse("")// For Goverment Space
/*				 + "\tMAIL"*/ // ABU
				 );
		
	/*	if(d.get("VKLOCK").toString().trim().equals("AGL_LIVE")) {
		ps.println(d.get("CTUSCNO").toString().trim()
				 +"\tVKLOCK"
				 +"\t"+d.get("CTUSCNO").toString().trim()//LOCKPARTNER
				 +"\t01"//PROID_KEY
				 +"\tF"//LOCKR_KEY
				 +"\t20230731"//FDATE_KEY
				 );
		ps.println(d.get("CTUSCNO").toString().trim()
				 +"\tVKLOCK"
				 +"\t"+d.get("CTUSCNO").toString().trim()//LOCKPARTNER
				 +"\t04"//PROID_KEY
				 +"\tF"//LOCKR_KEY
				 +"\t20230731"//FDATE_KEY
				 );
		}*/
	/*	if(d.get("VKLOCK").toString().trim().equals("AGL_BS")) {
		ps.println(d.get("CTUSCNO").toString().trim()
				 +"\tVKLOCK"
				 +"\t"+d.get("CTUSCNO").toString().trim()//LOCKPARTNER
				 +"\t01"//PROID_KEY
				 +"\tM"//LOCKR_KEY
				 +"\t20230731"//FDATE_KEY
				 );
		ps.println(d.get("CTUSCNO").toString().trim()
				 +"\tVKLOCK"
				 +"\t"+d.get("CTUSCNO").toString().trim()//LOCKPARTNER
				 +"\t04"//PROID_KEY
				 +"\tM"//LOCKR_KEY
				 +"\t20230731"//FDATE_KEY
				 );
		}*/
		
		if(d.get("MTR_STATUS").toString().trim().equals("03")||d.get("MTR_STATUS").toString().trim().equals("02")) {
		ps.println(d.get("CTUSCNO").toString().trim()
				 +"\tVKLOCK"
				 +"\t"+d.get("CTUSCNO").toString().trim()//LOCKPARTNER
				 +"\t04"//PROID_KEY
				 +"\tM"//LOCKR_KEY
				 +"\t20231004"//FDATE_KEY
				 );
		}
		
		ps.println(d.get("CTUSCNO")+"\t&ENDE");
	
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
/*	620

	VIJAYAWADA

	720

	GUNTUR

	920

	ONGOLE

	1420

	CRDA*/


	public static String getSapEroCode(String erocode) {
		Map<String,String> map = new HashMap<String,String>();
		map.put("173","737");
		map.put("411","930");
		map.put("472","925");
		map.put("473","933");
		map.put("421","929");
		map.put("422","924");
		map.put("423","932");
		map.put("424","934");
		map.put("474","940");
		map.put("434","922");
		map.put("435","921");
		map.put("441","923");
		map.put("461","928");
		map.put("443","931");
		map.put("462","936");
		map.put("445","935");
		map.put("111","721");
		map.put("112","732");
		map.put("121","725");
		map.put("124","724");
		map.put("131","726");
		map.put("132","727");
		map.put("134","734");
		map.put("141","728");
		map.put("142","733");
		map.put("143","735");
		map.put("144","738");
		map.put("911","1421");
		map.put("912","1422");
		map.put("171","723");
		map.put("172","730");
		map.put("113","739");
		map.put("451","927");
		map.put("452","926");
		map.put("633","633");
		map.put("631","631");
		map.put("662","632");
		map.put("643","622");
		map.put("652","639");
		map.put("663","637");
		map.put("672","638");
		map.put("622","627");
		map.put("624","636");
		map.put("642","621");
		map.put("651","623");
		map.put("661","626");
		map.put("671","641");
		map.put("673","630");
		map.put("611","625");
		map.put("612","640");
		map.put("613","635");
		map.put("614","634");
		map.put("623","629");
		map.put("632","624");
		map.put("923","1425");
		map.put("921","1424");		
		return Optional.ofNullable(map.get(erocode)).orElse("");

	}
}
