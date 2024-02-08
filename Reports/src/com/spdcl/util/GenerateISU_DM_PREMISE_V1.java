package com.spdcl.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.spdcl.model.IdTypes;
public class GenerateISU_DM_PREMISE_V1 {

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
		System.out.println(d.get("CTUSCNO").toString().trim() + " { SERVICE TYPE }="+d.get("STCODE"));
		ps.println(d.get("CTUSCNO").toString().trim()
				 +"\tEVBSD"
				 +"\t"+d.get("CTUSCNO").toString().trim()//CONN OBJ 
				 +"\t"+Optional.ofNullable(getSapEroCode(d.get("STCODE").toString())).orElse("") //PRIMESE TYPE
				 +"\t"+Optional.ofNullable(d.get("HT_AUTH_GROUP")).orElse("")//BEGRU
				 +"\t"+d.get("CTUSCNO").toString().trim()//LEGACY SCN
				 +"\t"//SCN HISTORY
				 +"\t"+d.get("CTUSCNO").toString().trim()//SERVICE NO
				 +"\t"//SCN HISTORY UPDATE 
				 );
		ps.println(d.get("CTUSCNO")+"\t&ENDE");
	
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static String getSapEroCode(String erocode) {
		Map<String,String> map = new HashMap<String,String>();
		map.put("60","HT3A60");
		map.put("61","HT3B61");
		map.put("62","HT3A62");
		map.put("63","HT3A63");
		map.put("64","HT5E64");
		map.put("65","HT3A65");
		map.put("66","HT3A66");
		map.put("67","HT3A67");
		map.put("68","HT4B68");
		map.put("69","HT3A69");
		map.put("70","HT3A70");
		map.put("71","HT3A71");
		map.put("72","HT3A72");
		map.put("73","HT4B73");
		map.put("74","HT3A74");
		map.put("75","HT3A75");
		map.put("76","HT2A176");
		map.put("77","HT3A77");
		map.put("78","HT3A78");
		map.put("79","HT2A179");
		map.put("80","HT3A80");
		map.put("81","HT3A81");
		map.put("82","HT2A182");
		map.put("83","HT3A83");
		map.put("84","HT3A84");
		map.put("85","HT3A85");
		map.put("86","HT3A86");
		map.put("87","HT2A187");
		map.put("88","HT2A188");
		map.put("89","HT4B89");
		map.put("90","HT3A90");
		map.put("91","HT5B91");
		map.put("92","HT2A192");
		map.put("93","HT3A93");
		map.put("94","HT4B94");
		map.put("95","HT5B95");
		map.put("96","HT2A196");
		map.put("97","HT4B97");
		map.put("98","HT5B98");
		map.put("99","HT2A199");
		map.put("100","HT4B100");
		map.put("101","HT5B101");
		map.put("102","HT2A1102");
		map.put("103","HT4B103");
		map.put("104","HT4B104");
		map.put("105","HT4B105");
		map.put("106","HT4B106");
		map.put("107","HT2A3107");
		map.put("108","HT3A108");
		map.put("109","HT2A1109");
		map.put("110","HT4B110");
		map.put("111","HT2A1111");
		map.put("112","HT3A112");
		map.put("113","HT3A113");
		map.put("114","HT5B114");
		map.put("115","HT2A1115");
		map.put("116","HT3A116");
		map.put("117","HT3A117");
		map.put("118","HT5B118");
		map.put("119","HT4B119");
		map.put("120","HT4B120");
		map.put("121","HT2A1121");
		map.put("122","HT4B122");
		map.put("123","HT2A1123");
		map.put("124","HT4B124");
		map.put("125","HT2A1125");
		map.put("126","HT2A1126");
		map.put("127","HT2A1127");
		map.put("128","HT2A1128");
		map.put("129","HT2A1129");
		map.put("130","HT2A1130");
		map.put("131","HT5E131");
		map.put("132","HT5C1132");
		map.put("133","HT5C1133");
		map.put("134","HT5C2134");
		map.put("135","HT5C2135");
		map.put("136","HT3C136");
		map.put("137","HT4B137");
		map.put("138","HT1B138");
		map.put("139","HT1B139");
		map.put("140","HT1B140");
		map.put("1","HT2A11");
		map.put("2","HT2A12");
		map.put("3","HT3A3");
		map.put("4","HT3A4");
		map.put("5","HT3A5");
		map.put("6","HT3A6");
		map.put("7","HT3A7");
		map.put("8","HT3A8");
		map.put("9","HT2A19");
		map.put("10","HT3B10");
		map.put("11","HT3A11");
		map.put("12","HT3A12");
		map.put("13","HT3A13");
		map.put("14","HT3A14");
		map.put("15","HT3A15");
		map.put("16","HT3A16");
		map.put("17","HT3A17");
		map.put("18","HT4B18");
		map.put("19","HT3A19");
		map.put("20","HT3A20");
		map.put("21","HT3A21");
		map.put("22","HT3A22");
		map.put("23","HT2A123");
		map.put("24","HT3A24");
		map.put("25","HT3A25");
		map.put("26","HT5B26");
		map.put("27","HT3A27");
		map.put("28","HT3A28");
		map.put("29","HT3A29");
		map.put("30","HT3A30");
		map.put("31","HT3B31");
		map.put("32","HT5B32");
		map.put("33","HT3A33");
		map.put("34","HT3A34");
		map.put("35","HT3A35");
		map.put("36","HT2A136");
		map.put("37","HT4D37");
		map.put("38","HT3A38");
		map.put("39","HT3A39");
		map.put("40","HT3A40");
		map.put("41","HT2A141");
		map.put("42","HT2A142");
		map.put("43","HT3A43");
		map.put("44","HT3A44");
		map.put("141","HT1B141");
		map.put("142","HT2A1142");
		map.put("143","HT2A1143");
		map.put("144","HT2A1144");
		map.put("145","HT2A1145");
		map.put("146","HT2A1146");
		map.put("147","HT2A1147");
		map.put("148","HT2A1148");
		map.put("149","HT2A1149");
		map.put("150","HT2A1150");
		map.put("151","HT2A1151");
		map.put("152","HT2A1152");
		map.put("153","HT2A1153");
		map.put("154","HT2A1154");
		map.put("155","HT2A1155");
		map.put("156","HT2A1156");
		map.put("157","HT2A1157");
		map.put("158","HT2A1158");
		map.put("159","HT2A1159");
		map.put("160","HT2A1160");
		map.put("161","HT2A1161");
		map.put("162","HT2A1162");
		map.put("163","HT2A1163");
		map.put("164","HT2A1164");
		map.put("165","HT2A1165");
		map.put("166","HT2A1166");
		map.put("167","HT2A1167");
		map.put("168","HT2A1168");
		map.put("169","HT2A1169");
		map.put("170","HT2A3170");
		map.put("171","HT2A3171");
		map.put("172","HT2A3172");
		map.put("173","HT2B173");
		map.put("174","HT2B174");
		map.put("175","HT2B175");
		map.put("176","HT2C176");
		map.put("177","HT4A177");
		map.put("178","HT4A178");
		map.put("179","HT4A179");
		map.put("180","HT4A180");
		map.put("181","HT4A181");
		map.put("182","HT4A182");
		map.put("183","HT4B183");
		map.put("184","HT4B184");
		map.put("185","HT4B185");
		map.put("186","HT4B186");
		map.put("187","HT4B187");
		map.put("188","HT4B188");
		map.put("189","HT4B189");
		map.put("190","HT4B190");
		map.put("191","HT4B191");
		map.put("192","HT4B192");
		map.put("193","HT4B193");
		map.put("194","HT4B194");
		map.put("195","HT4C195");
		map.put("196","HT4C196");
		map.put("197","HT4C197");
		map.put("198","HT4C198");
		map.put("199","HT4C199");
		map.put("200","HT5B200");
		map.put("201","HT5B201");
		map.put("202","HT5E202");
		map.put("203","HT3C203");
		map.put("204","HT3B204");
		map.put("205","HT3B205");
		map.put("206","HT3B206");
		map.put("207","HT3A207");
		map.put("208","HT3A208");
		map.put("209","HT3A209");
		map.put("210","HT3A210");
		map.put("211","HT3C211");
		map.put("212","HT3A212");
		map.put("213","HT3A213");
		map.put("214","HT3C214");
		map.put("215","HT3B215");
		map.put("216","HT3B216");
		map.put("217","HT3A217");
		map.put("218","HT3B218");
		map.put("219","HT3A219");
		map.put("220","HT3A219");
		map.put("221","HT3A221");
		map.put("222","HT3A222");
		map.put("223","HT4B223");
		map.put("45","HT3A45");
		map.put("46","HT3C46");
		map.put("47","HT3A47");
		map.put("48","HT3A220");
		map.put("49","HT2A149");
		map.put("50","HT3B50");
		map.put("51","HT3A51");
		map.put("53","HT3A53");
		map.put("54","HT3A54");
		map.put("55","HT3A55");
		map.put("56","HT2A156");
		map.put("57","HT3A57");
		map.put("58","HT2A158");
		map.put("59","HT3A59");
		map.put("52","HT4B52");
		map.put("224","HT2A1224");
		map.put("225","HT2A1225");
		map.put("226","HT2A1226");
		map.put("230","HT2A1230");
		map.put("227","HT2A1227");
		map.put("228","HT2A1228");
		map.put("229","HT2A1229");
		map.put("231","HT2A1231");
		map.put("232","HT2A1232");
		map.put("233","HT2A1233");
		map.put("234","HT2A3234");
		map.put("235","HT3A235");
		map.put("236","HT3A236");
		map.put("237","HT3A237");
		map.put("238","HT3A238");
		map.put("239","HT3A239");
		map.put("240","HT3A240");
		map.put("245","HT2A1245");
		map.put("243","HT3A243");
		map.put("241","HT2A1241");
		map.put("242","HT4B242");
		map.put("244","HT4B244");
		map.put("246","HT5C2135");

		return Optional.ofNullable(map.get(erocode)).orElse("");

	}
}
