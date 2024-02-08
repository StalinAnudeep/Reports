package com.spdcl.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.spdcl.model.IdTypes;
public class GenerateISU_DM_CONNOBJ_V1 {
    static Map<String, Object> master ;
	public static ByteArrayInputStream generateBillTxt(List<Map<String, Object>>  data, List<Map<String, Object>>  mdata) {
		master = mdata.stream().collect(Collectors.toMap(s -> (String) s.get("USCNO"), s->s));
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
				 +"\tCO_EHA"
				 +"\t"+Optional.ofNullable(d.get("FMSAPFCODE")).orElse("")///FEEDER_CODE // TPLMA
				 +"\t"+Optional.ofNullable(d.get("HT_AUTH_GROUP")).orElse("")//BEGRU
				 +"\t"+Optional.ofNullable(d.get("MAIN_PLAN")).orElse("")//3010 vijayawada //3020 Guntur
				 +"\t"//LONGITUDE
				 +"\t"//LATITUDE
				/* +"\t"+Optional.ofNullable(d.get("SAPSECCD")).orElse("")//SECTION CODE
*/				 +"\t"+Optional.ofNullable(d.get("SAP_SECTION_CODE")).orElse("")//SECTION CODE
				 +"\t"//DISTRIBUTION
				 +"\t"+Optional.ofNullable(d.get("CTLEASE_FLAG")).orElse("")//TYPE OF PROPERTY// OWNED //LEASED 
				 +"\t02"//ZZOVERHEAD
				 +"\t"//NO OF SHIFTS //01 - 02 -03
				 +"\t"//SUBSTATION LOCATION
				 +"\t"//BAY EXTN
				 +"\t"//DIST FROM SEA
				 +"\t"//AREA
				 +"\t"// EXECUTION
				 +"\t"//ZZLINEMAN_USER
				  +"\t"//ZZGSWS_CODE
				 );//Need Configure
		ps.println(d.get("CTUSCNO").toString().trim()
				 +"\tCO_ADR"
				 +"\t"+Optional.ofNullable(d.get("CTCITY")).orElse("")//CITY1
				 +"\t"+Optional.ofNullable(d.get("CTDISTRICT")).orElse("")//CITY2// PASS STATIC VALUE KRISHNA
				 + "\t"+Optional.ofNullable((d.get("CTPINCODE").toString().equals("0")?getPincode(d.get("SECNAME").toString()):d.get("CTPINCODE").toString())).orElse("")//POST_CODE1
				 +"\t"+Optional.ofNullable(d.get("CTADD1")).orElse("")
				 +"\t"/*+(master.containsKey(d.get("CTUSCNO"))?Optional.ofNullable(  ((Map<String, String>)master.get(d.get("CTUSCNO"))).get("HOUSE_NO")).orElse(""):"")*///HOUSE_NUM1
				 +"\t"+Optional.ofNullable(d.get("CTADD2")).orElse("")
				 +"\t"+Optional.ofNullable(d.get("CTADD3")).orElse("")
				 +"\t"//LOCATION
				 +"\t"//REMARK
				 +"\t"+Optional.ofNullable(d.get("CTADD4")).orElse("")//STR_SUPPL3
				);
		/*ps.println(d.get("CTUSCNO").toString().trim()
				 +"\tVKP"
				 +"\t"+d.get("CTUSCNO").toString().trim()
				 +"\tHT"
				 +"\tHT00"
				 +"\t"+d.get("CTCAT").toString().trim()
				 +"\tZAFBILL_ISU_BILL_HT_SSF"
				 +"\tHT_INV01"
				 +"\t"+d.get("EROCD").toString().trim()
				 + "\tMAIL"
				 );*/
		ps.println(d.get("CTUSCNO")+"\t&ENDE");
	
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	public static String getPincode(String secname) {
		  Map<String,String> map = new HashMap<String,String>();
		  map.put("CHITTINAGAR","520001");
		  map.put("R.R.NAGAR","520010");
		  map.put("KGMARKET","520001");
		  map.put("MUTYALAMPADU","520011");
		  map.put("AJITSINGHNAGAR","520015");
		  map.put("SURYARAOPET","520002");
		  map.put("GOVERNORPET","520002");
		  map.put("GANDHINAGAR","520003");
		  map.put("SKEWBRIDGE","520010");
		  map.put("NUNNA","521212");
		  map.put("MOGHALRAJPURAM","520010");
		  map.put("KRISHNALANKA","520013");
		  map.put("RINGROAD","520008");
		  map.put("GUNADALATN","520004");
		  map.put("KANURU","520007");
		  map.put("YENAMALAKUDURU","520007");
		  map.put("AUTO NAGAR-VJA","520007");
		  map.put("PENUMULURU","521139");
		  map.put("ENIKEPADU","521108");
		  map.put("MEERJAPURAM","521111");
		  map.put("GANNAVARAM","521101");
		  map.put("TELEPROLU","521109");
		  map.put("IBRAHIMPATNAM","521456");
		  map.put("GOLLAPUDI","521225");
		  map.put("G.KONDURU","521229");
		  map.put("KONDAPALLI","521228");
		  map.put("ALLURU-NDG","521343");
		  map.put("NANDIGAMA RURAL","521185");
		  map.put("NANDIGAMA TOWN","521185");
		  map.put("CHANDARLAPADU","521182");
		  map.put("CHILLAKALLU","521178");
		  map.put("HANUMAN JUNCTION","521110");
		  map.put("VEERAVALLI","521110");
		  map.put("NUZVID  TOWN","521201");
		  map.put("RAMANAKKAPET","521213");
		  map.put("GAMPALAGUDEM","521403");
		  map.put("JAGGAIAHPET","521457");
		  map.put("D2-GUDIVADA","521301");
		  map.put("D1-GUDIVADA","521301");
		  map.put("KOWTHAVARAM","521356");
		  map.put("D2 MTM NORTH","521001");
		  map.put("D3 MTM","521001");
		  map.put("AVANIGADDA","521121");
		  map.put("VUYYURU TOWN","521165");
		  map.put("PAMARRU","521157");
		  map.put("KOTHAPET","520001");
		  map.put("KAIKALURU TOWN","521333");
		  map.put("PENUGANCHIPROLU","521190");
		  map.put("MUDINEPALLI","521325");
		  map.put("MADHURANAGAR","520011");
		  map.put("ADAVINEKKALAM","521212");
		  map.put("KAIKALURU RURAL","521333");
		  map.put("KANKIPADU","521151");
		  map.put("MANDAVALLI","521345");
		  map.put("BHPURAM TOWN","520010");
		  map.put("NUZVID RURALS","521201");
		  map.put("KAMBHAMPADU","521229");
		  map.put("BANTUMILLI","521324");
		  map.put("PAYAKAPURAM","520015");
		  map.put("NANDIVADA","521321");
		  map.put("PATAMATA","520010");
		  map.put("VATCHAVAI","521402");
		  map.put("PEDANA","521366");
		  map.put("KALIDINDI","521344");
		  map.put("MUSUNURU","521207");
		  map.put("AGIRIPALLI","521211");
		  map.put("KANCHIKACHERLA","521180");
		  map.put("LAXMIPURAM","521181");
		  map.put("KRUTHIVENNU","521324");
		  map.put("MTM RURALS","521001");
		  map.put("THOTLAVALLURU","521163");
		  map.put("NAGAYALANKA","521120");
		  map.put("GHANTASALA","521133");
		  map.put("VISSANNAPETA","521215");
		  map.put("TIRUVURU TOWN","521235");
		  map.put("MYLAVARAM-IBH","521230");
		  map.put("CHATRAI","521214");
		  map.put("TIRUVURU RURAL","521235");
		  map.put("MOPIDEVI","521125");
		  map.put("KODURU","521328");
		  map.put("D1 MTM SOUTH","521001");
		  map.put("UNGUTURU","534411");
		  map.put("MOVVA","521135");
		  map.put("PEDAPARAPUDI","521263");
		  map.put("GANAPAVARAM","534198");
		  map.put("RURAL-GUDIVADA","521301");
		  map.put("GUDURU","521149");
		  map.put("REDDYGUDEM","521215");
		  map.put("CHINNAPURAM","521001");
		  map.put("AMARAVATHI","522020");
		  map.put("ARDHAVEEDU","523335");
		  map.put("BALLIKURVA","523301");
		  map.put("BELLAMKONDA","522411");
		  map.put("BHATTIPROLU","522256");
		  map.put("C.S.PURAM","523112");
		  map.put("D1 CHIRALA","523155");
		  map.put("D2 CHIRALA","523155");
		  map.put("DARSI","523247");
		  map.put("DONAKONDA","523336");
		  map.put("DUGGIRALA","522330");
		  map.put("DURGI","522612");
		  map.put("EDLAPADU","522233");
		  map.put("GIDDALUR","523357");
		  map.put("GUDLURU","523281");
		  map.put("D3-GUNTUR","522002");
		  map.put("D13-GUNTUR","522002");
		  map.put("D6-GUNTUR","522002");
		  map.put("D9-GUNTUR","522002");
		  map.put("D4-GUNTUR","522002");
		  map.put("D1-GUNTUR","522002");
		  map.put("D10-GUNTUR","522002");
		  map.put("D2-GUNTUR","522002");
		  map.put("D8-GUNTUR","522002");
		  map.put("D7-GUNTUR","522002");
		  map.put("D5-GUNTUR","522002");
		  map.put("D11-GUNTUR","522002");
		  map.put("H.M.PADU","523227");
		  map.put("INKOLLU","523167");
		  map.put("IPURU","522658");
		  map.put("JARUGUMALLI","523274");
		  map.put("KAKUMANU","522112");
		  map.put("KARAMCHEDU","523168");
		  map.put("KAREMPUDI","522614");
		  map.put("KARLAPALEM","522111");
		  map.put("KOLLIPARA","522304");
		  map.put("KOLLURU","522411");
		  map.put("KONDEPI","523270");
		  map.put("KOTHAPATNAM","523286");
		  map.put("KROSURU","522410");
		  map.put("KURICHEDU","523304");
		  map.put("MACHAVARAM","522615");
		  map.put("MADDIPADU","523211");
		  map.put("MANDADAM","522237");
		  map.put("MANGALAGIRI-D1","522503");
		  map.put("MANGALAGIRI-D2","522503");
		  map.put("MARKAPUR","523316");
		  map.put("MARRIPUDI","522310");
		  map.put("MUNDLAPADU","523367");
		  map.put("NIZAMPATNAM","522314");
		  map.put("D1 ONGOLE","523001");
		  map.put("D4 ONGOLE","523001");
		  map.put("D2 ONGOLE","523001");
		  map.put("D3 ONGOLE","523001");
		  map.put("P.C.PALLI","523117");
		  map.put("PEDAKAKANI","522509");
		  map.put("PEDAKURAPADU","522402");
		  map.put("PEDANANDIPADU","522235");
		  map.put("PHIRANGIPURAM","522529");
		  map.put("PIDUGURALLA-(TOWN)","522413");
		  map.put("PODILI","523240");
		  map.put("PULLALA CHERUVU","523328");
		  map.put("RACHERLA","523368");
		  map.put("RAJUPALEM","522235");
		  map.put("RAYAPUDI","522237");
		  map.put("REMIDICHERLA","522663");
		  map.put("RENTACHINTALA","522421");
		  map.put("S.KONDA","523101");
		  map.put("SAVALYAPURAM","522646");
		  map.put("TADIKONDA","522236");
		  map.put("TALLURU","522529");
		  map.put("TANGUTUR","523274");
		  map.put("TENALI-D1","522201");
		  map.put("TENALI-D3","522201");
		  map.put("TENALI-D2","522201");
		  map.put("ULAVAPADU","523292");
		  map.put("VELIGANDLA","523241");
		  map.put("VEMURU","522261");
		  map.put("VETAPALEM","522212");
		  map.put("ONGOLE RURALS","523001");
		  map.put("VINUKONDA-RURAL","522647");
		  map.put("ADDANKI TOWN","523201");
		  map.put("NARASARAOPET-RURAL","522601");
		  map.put("BAPATLA-TOWN","522101");
		  map.put("PONNUR-TOWN","522124");
		  map.put("DARSI RURALS","523247");
		  map.put("REPALLE-TOWN","522265");
		  map.put("PIDUGURALLA-RURAL","522413");
		  map.put("D12-GUNTUR","522002");
		  map.put("REPALLE-RURAL","522265");
		  map.put("CUMBUM","523372");
		  map.put("CHERUKUPALLI","522259");
		  map.put("KOMAROLU","523373");
		  map.put("PRATHIPADU","522019");
		  map.put("MACHERLA-TOWN","522426");
		  map.put("V.V.PALEM","523116");
		  map.put("KANIGIRI RURALS","523230");
		  map.put("PONNUR-RURAL","522124");
		  map.put("VETAPALEM RURALS","522212");
		  map.put("LINES-GUNTUR","522002");
		  map.put("MARKAPUR RURALS","523316");
		  map.put("RURALS CH.PET","522616");
		  map.put("GANAPAVARUM","534198");
		  map.put("PONNALURU","523109");
		  map.put("PAMURU","523108");
		  map.put("MCHPALEM","522411");
		  map.put("D1-NRT","522601");
		  map.put("ATCHEMPETA","522409");
		  map.put("NEWJENDLA","522660");
		  map.put("KDK TOWN","");
		  map.put("P.DORNAL","523331");
		  map.put("NGLPADU","523183");
		  map.put("B.V.PETA","523334");
		  map.put("SNPADU","523225");
		  map.put("MARTUR","523301");
		  map.put("TOWN-II CH.PET","522616");
		  map.put("CHINNAGANJAM","523135");
		  map.put("AE-RURALS","");
		  map.put("S.MAGULURU","523302");
		  map.put("D2-NRT","522601");
		  map.put("MUPPALA","522408");
		  map.put("GURAZALA","522415");
		  map.put("VELDURTHI","518216");
		  map.put("EDDANAPUDI","521136");
		  map.put("SATTENAPALLI-TOWN","522403");
		  map.put("PV-PALEM","522329");
		  map.put("THARULUPADU","523332");
		  map.put("THRIPURANTHAKAM","523326");
		  map.put("VATTICHERUKURU","522212");
		  map.put("BOLLAPALLY","523261");
		  map.put("KONAKALAMITLA","523241");
		  map.put("KRISHNANAGAR","520007");
		  map.put("P.ARAVEEDU","523320");
		  map.put("AMRUTHALURU","522325");
		  map.put("SATTENAPALLI-RURAL","522403");
		  map.put("DACHEPALLI-TOWN","522414");
		  map.put("DOKIPARRU","521332");
		  map.put("TOWN-I CH.PET","522616");
		  map.put("MUNDLAMURU","523265");
		  map.put("CHIRALARURALS","523155");
		  map.put("MEDARAMETLA","523212");
		  map.put("KANIGIRI TOWN","523230");
		  map.put("PARCHOOR","523169");
		  map.put("NAKARIKALLU","522615");
		  map.put("CHEBROLU","533449");
		  map.put("ROMPICHERLA-NRT","522617");
		  map.put("MACHARAL-RURAL","522426");
		  map.put("BAPATLA-RURAL","522101");
		  map.put("KDK RURALS","523105");
		  map.put("NAGARAM","522309");
		  map.put("TANGUTUR RURALS","523274");
		  map.put("CHIMAKURTHY","523226");
		  map.put("ADDANKI RURALS","523201");
		  map.put("PANGULURU","523214");
		  map.put("DACHEPALLI-RURAL","522414");
		  map.put("VINUKONDA-TOWN","522647");
		  map.put("Y.PALEM","523327");
		  map.put("KDK TOWN","523105");
		  map.put("L.SAMUDRAM","523114");
		  map.put("PENAMALURU","520001");
			return Optional.ofNullable(map.get(secname)).orElse("");
	  }
}
