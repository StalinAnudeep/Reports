package com.spdcl.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.spdcl.model.IdTypes;
public class GenerateCS_PARTNER_V1 {
	static String regex = "^[A-Za-z0-9+_.-]+@(.+)$";  
	String eregex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";  
	Pattern epattern = Pattern.compile(regex);  
    //Compile regular expression to get the pattern  
    static Pattern pattern = Pattern.compile(regex);  
    static Map<String, Object> master ;
    
	public static ByteArrayInputStream generateBillTxt(List<Map<String, Object>>  data,  Map<String,List<IdTypes>>  idtypes, List<Map<String, Object>>  mdata) {		
		master = mdata.stream().collect(Collectors.toMap(s -> (String) s.get("USCNO"), s->s));
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		/*System.out.println("DATA SIZE : "+data.size());*/
		PrintStream ps = new PrintStream(out);
		for(Map<String, Object> d : data)
		printPageOne( ps,d,idtypes);
		ps.close();
		return new ByteArrayInputStream(out.toByteArray());
	}
	private static void printPageOne(PrintStream ps,Object o,  Map<String,List<IdTypes>>  idtypes) {
		try {
		Map<String, Object> d = (Map<String, Object>) o;
		ps.println(d.get("CTUSCNO").toString().trim()
				+"\tINIT"
				+ "\tMKK"//BU_RLTYP
				+ "\t2"//BU_TYPE
			/*	+ "\tISU1"*/
				+ "\tZHT"//BPKIND
				+"\t"+Optional.ofNullable(d.get("CSC_REG_NO")).orElse("")//BPEXT
				+ "\tCRM000"//RLTP2
				);
		
		ps.println(d.get("CTUSCNO").toString().trim()+
				"\tBUT000"
				+ "\t"+Optional.ofNullable(d.get("SOURCE")).orElse("")//SOURCE
				+ "\t"+getTitle(d)//TITLE/0005
				+ "\t"+Optional.ofNullable(d.get("HT_AUTH_GROUP")).orElse("")//BEGRU
				+"\t"+Optional.ofNullable(d.get("ORG1").toString().trim()).orElse("")//ORG1 , ORG2, ORG3 
				+"\t"+(String.valueOf(d.get("ORG2")).equals("null")?"":Optional.ofNullable(d.get("ORG2").toString().trim()).orElse(""))//ORG2
				+"\t"+(String.valueOf(d.get("ORG3")).equals("null")?"":Optional.ofNullable(d.get("ORG3").toString().trim()).orElse(""))//ORG3
				+"\t"													//NAME_LAST
				+ "\t"													//NAME_FIRST
				/*+ "\t"					*/								//NICKNAME
				+ "\t"													//NAMEMIDDLE			
				+ "\t"													//LANGU_CORR
				+"\t"+Optional.ofNullable(d.get("TYPE_OF_SC")).orElse("")//ZZ1_MSMETYPE_BUS
				+"\t"+Optional.ofNullable(d.get("TYPE_OF_ENT")).orElse("")//ZZ1_MSMESRVTYPE_BUS
				+"\t"+Optional.ofNullable(d.get("CTHODDEP")).orElse("")   //ZZ1_GOVTDEPTNAME_BUS 
				+"\t"+Optional.ofNullable(d.get("CTHODTYPE")).orElse("")  //ZZ1_GOVTTYPE_BUS
				+"\t"+Optional.ofNullable(d.get("UAM_NO")).orElse("")//ZZ1_MSMEUAM_BUS
				+"\t"+(master.containsKey(d.get("CTUSCNO"))?Optional.ofNullable(  ((Map<String, String>)master.get(d.get("CTUSCNO"))).get("PRONAME")).orElse(""):"")//ZZ1_PROPRIETORNAME_BUS
				+"\t"+Optional.ofNullable(getSUBDEPTCODE(d.get("NCTHODSUBDEP").toString().trim()).trim().replace(" ", "")).orElse("")//ZZ1_GOVTSUBDEPT_BUS
				+"\t" /*+(master.containsKey(d.get("CTUSCNO"))?Optional.ofNullable(  ((Map<String, String>)master.get(d.get("CTUSCNO"))).get("FNAME")).orElse(""):"")//ZZ1_CONFATHERNAME2_BUS
*/				+"\t"+Optional.ofNullable(d.get("LOCAL_TYPE")).orElse("") //ZZ1_LOCALTYPE2_BUS\t
				+"\t"+(master.containsKey(d.get("CTUSCNO"))?Optional.ofNullable(  ((Map<String, String>)master.get(d.get("CTUSCNO"))).get("CONS_TYPE")).orElse("1"):"1")//ZZ1_CONSUMERTYPE_BUS
				+"\t"//ZZ1_SURVEYNO_BUS
				+"\t"+Optional.ofNullable(d.get("AADHAARNO")).orElse("")//ZZ1_AADHAARNOOFOWNER_BUS
				+"\t"//ZZ1_RELATIONSHIPOCCUPA_BUS
				+"\t"//ZZ1_TENANTADDRAADHAAR_BUS
				+"\t"//ZZ1_REGISTEREDACT2022_BUS
				+"\t"//ZZ1_AADHAARNAMEOFOWNER_BUS
				+"\t"//ZZ1_PONDSIZE_BUS
				+"\t"//ZZ1_AADHAARNAMETENANT_BUS
				+"\t"//ZZ1_AQUAZONE_BUS
				+"\t"/*+(master.containsKey(d.get("CTUSCNO"))?Optional.ofNullable(  ((Map<String, String>)master.get(d.get("CTUSCNO"))).get("DISTRIBUTION")).orElse(""):"")*/// ZZ1_DISTRIBUTION1_BUS ***
				+"\t"//ZZ1_REGISTRATIONNUM_BUS
				+"\t"//ZZ1_AADHAARNOTENANT_BUS
				+"\t"/*+Optional.ofNullable(d.get("PAN")).orElse("")*///ZZ1_NAMEPANNUMBER_BUS
				+"\t"+Optional.ofNullable(d.get("DDOCODE")).orElse("")//ZZ1_DDO_CODE_BUS
				+"\t"+Optional.ofNullable(d.get("GEN_TYPE_ID")).orElse("")//ZZ1_GRID_TYPE_BUS
				+"\t"+Optional.ofNullable(d.get("LGDCODE")).orElse("")//ZZ1_LGD_CODE_BUS
				+"\t"//ZZ1_GP_NAME_BUS
				+"\t"+Optional.ofNullable(d.get("SUB_GRID_TYPE_ID")).orElse("")//ZZ1_SUBGRID_TYPE_BUS
				+"\t"+Optional.ofNullable(d.get("DISCONNECT_DATE")).orElse("") //ZZ1_DISCONNECT_DATE_BUS
			/*	+"\t"//ZZ1_GP_LGD_CODE_BUS				*/		// ABU
				+"\t"+d.get("COL_SEG_BUS").toString().trim()//ZZ1_COL_SEG_BUS // COL - Y , COL - N
				+"\tN"//ZZ1_ADV_OPT_BUS
				+"\t"+d.get("INSTALLED_CAPACITY").toString().trim()//ZZ1_INSTAL_CAP_BUS
				+"\t"+d.get("CTSUPCONDT").toString().trim()//ZZ1_LEG_MOVE_DATE_BUS //Move in date in legacy // Date of Connection 
				);
		System.out.println(d.get("CTUSCNO").toString());
				ps.println(d.get("CTUSCNO").toString().trim()
						+"\tBUT020"
						+ "\t"+Optional.ofNullable(d.get("CTCITY")).orElse("")//CITY1 // MANDAL
						+ "\t"+Optional.ofNullable(d.get("CTDISTRICT")).orElse("")//CITY2						
						+ "\t"+Optional.ofNullable((d.get("CTPINCODE").toString().equals("0")?
								getPincode(d.get("SECNAME").toString()):
									d.get("CTPINCODE").toString())).orElse("")//POST_CODE1											
						+ "\t"+Optional.ofNullable(d.get("CTADD1")).orElse("")///STREET // CITY
						+ "\t"/*+(master.containsKey(d.get("CTUSCNO"))?Optional.ofNullable(  ((Map<String, String>)master.get(d.get("CTUSCNO"))).get("HOUSE_NO")).orElse(""):"")*///HOUSE_NUM1
						+ "\t"+Optional.ofNullable(d.get("CTADD2")).orElse("")//STR_SUPPL1
						+ "\t"+Optional.ofNullable(d.get("CTADD3")).orElse("")//STR_SUPPL2
						+ "\t"//LOCATION
						+ "\tE"//LANGU
						+ "\t"+isValidMobileNo(Optional.ofNullable(String.valueOf(d.get("CTMOBILE"))).orElse(""))//TEL_NUMBER
						+ "\t"+Optional.ofNullable(d.get("CTADD4")).orElse("")//STR_SUPPL3
						+ "\t"+checkEmail((String.valueOf(d.get("CTEMAILID")).contains(",")?
								(pattern.matcher(Optional.ofNullable(String.valueOf(d.get("CTEMAILID")).split(",")[0]).orElse("")).matches())?
										Optional.ofNullable(String.valueOf(d.get("CTEMAILID")).split(",")[0]).orElse(""):""
								:pattern.matcher(String.valueOf(d.get("CTEMAILID"))).matches()?String.valueOf(d.get("CTEMAILID")):"").replace("null", ""))//SMTP_ADDR
						);
				ps.println(d.get("CTUSCNO").toString().trim()
						+"\tBUT021"
						+ "\tCON_OBJECT");
				// 
				
				
				
				List<IdTypes> temp = idtypes.get(d.get("CTUSCNO").toString().trim());
				if(temp != null) {
					boolean tflag = false;
					String ttan = "";
					boolean gflag = false;
					String gtan = "";
					for(IdTypes id : temp) {					
					if(id.getID_TYPE_CD().equals("TIN")) //TIN
						{
						tflag = true;
						ttan = Optional.ofNullable(id.getPER_ID_NBR()).orElse("");
						}
						if(id.getID_TYPE_CD().equals("GST")) //TIN
						{
							gflag = true;
							gtan = Optional.ofNullable(id.getPER_ID_NBR()).orElse("");
						}
					}
					
					if(tflag)//
					ps.println(d.get("CTUSCNO").toString().trim()
							+"\tTAXNUM"
							+"\tIN2"
							+ "\tI"
							+ "\t" +ttan);//TIN 
				/*	else
						ps.println(d.get("CTUSCNO").toString().trim()
								+"\tTAXNUM"
								+"\tIN2"
								+ "\tI"
								+ "\t");//TIN 
*/					if(gflag)//
					ps.println(d.get("CTUSCNO").toString().trim()
							+"\tTAXNUM"
							+ "\tIN3"
							+ "\tI"
							+ "\t"+gtan);//GST
				/*	else
					ps.println(d.get("CTUSCNO").toString().trim()
								+"\tTAXNUM"
								+ "\tIN3"
								+ "\tI"
								+ "\t");//GST
*/						
					}
				if(!d.get("CTBANKACNO").toString().trim().equals("0"))
				ps.println(d.get("CTUSCNO").toString().trim()
						+"\tBUT0ID"  
						+ "\tI"
						+ "\t" +d.get("IFSC_CODE").toString().trim()+":"+d.get("CTBANKACNO").toString().trim()
						+ "\tZVBACC");
				
				/*ps.println(d.get("CTUSCNO").toString().trim()
						+"\tTAXNUM"
						+"\tIN2"
						+ "\tI"
						+ "\t" +d.get("CTBANKACNO").toString().trim());
						//TIN 
*/				
				if(temp != null) {
				for(IdTypes id : temp) {
				if(id.getID_TYPE_CD().equals("UID")) {
				if(id.getPER_ID_NBR().length()==12 && VerhoeffAlgorithm.validateVerhoeff(id.getPER_ID_NBR()))
				ps.println(d.get("CTUSCNO").toString().trim()
						+"\tBUT0ID"
						+ "\tI"
						+ "\t" +id.getPER_ID_NBR()
						+ "\tZADHAR");
				}
				if(id.getID_TYPE_CD().equals("PAN")) {				
				if(isValidPanCardNo(id.getPER_ID_NBR()))
					ps.println(d.get("CTUSCNO").toString().trim()
							+"\tBUT0ID"
							+ "\tI"
							+ "\t" +id.getPER_ID_NBR()
							+ "\tZPAN");
				}
				if(id.getID_TYPE_CD().equals("TAN"))
					ps.println(d.get("CTUSCNO").toString().trim()
							+"\tBUT0ID"
							+ "\tI"
							+ "\t" +id.getPER_ID_NBR()
							+ "\tZTAN");
				}
				}
				ps.println(d.get("CTUSCNO").toString().trim()+
						"\tKNA1"
						+ "\t"
						+ "\t1"
						+ "\t"+d.get("MTR_STATUS").toString().trim());  /*for live =01 ,udc=03,billstop=02,dismantle=04*/
				ps.println(d.get("CTUSCNO")+"\t&ENDE");
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	private static String getTitle(Map<String, Object> d) {
		/*if (master.containsKey(d.get("CTUSCNO").toString().trim())) {
			String title = ((Map<String, String>)master.get(d.get("CTUSCNO"))).get("TITLE");
			title = (title==null)
					? (d.get("CTNAME").toString().trim().toUpperCase().contains("M/S") ? "0005" : "")
					: title;
			return title;
		} else {
			return d.get("CTNAME").toString().trim().toUpperCase().contains("M/S") ? "0005" : "";
		}*/
		return d.get("CTNAME").toString().trim().toUpperCase().contains("M/S") ? "0005" : "";
	}

/*	public static String checkNullEmptyBlank(String strToCheck) {  
        // check whether the given string is null or not  
    if (strToCheck == null) {  
        return "NULL";  
        }  
        // check whether the given string is empty or not  
    else if(strToCheck.isEmpty()) {  
        return "EMPTY";  
        }  
        // check whether the given string is blank or not  
    else if(strToCheck.isBlank()) {  
        return "BLANK";  
        }  
    else {  
        return "neither NULL, EMPTY nor BLANK";  
        }  
    }  */
	
	private static String getName(String orgname) {
		String str = orgname.toUpperCase().replace("M/S.", "").trim().replace("M/S.", "").trim().replace("  ", "").replace("M/S", "");
		if (str.length() > 40) {
			char temp[] = str.toCharArray();
			String org1 ="";
			String org2 ="";
			String org3 ="";
			for (int i = 0; i < temp.length; i++) {
				if (i <=40)
					org1 = org1 + temp[i] ;
				else if (i > 40 && i < 80)
					org2 = org2 + temp[i] ;
				else
					org3 = org3 + temp[i];
			}
			return org1+"\t"+org2+"\t"+org3;
		}else {
			str = str+"\t\t";
		}
		return str.trim();
	}

	  public static boolean isValidGSTNo(String str)
	    {
	        // Regex to check valid
	        // GST (Goods and Services Tax) number
	        String regex = "^[0-9]{2}[A-Z]{5}[0-9]{4}"
	                       + "[A-Z]{1}[1-9A-Z]{1}"
	                       + "Z[0-9A-Z]{1}$";
	 
	        // Compile the ReGex
	        Pattern p = Pattern.compile(regex);
	 
	        // If the string is empty
	        // return false
	        if (str == null)
	        {
	            return false;
	        }
	 
	        // Pattern class contains matcher()
	        // method to find the matching
	        // between the given string
	        // and the regular expression.
	        Matcher m = p.matcher(str);
	 
	        // Return if the string
	        // matched the ReGex
	        return m.matches();
	    }
	  
	  public static boolean isValidPanCardNo(String panCardNo)
	    {
	 
	        // Regex to check valid PAN Card number.
	        String regex = "[A-Z]{5}[0-9]{4}[A-Z]{1}";
	 
	        // Compile the ReGex
	        Pattern p = Pattern.compile(regex);
	 
	        // If the PAN Card number
	        // is empty return false
	        if (panCardNo == null)
	        {
	            return false;
	        }
	 
	        // Pattern class contains matcher() method
	        // to find matching between given
	        // PAN Card number using regular expression.
	        Matcher m = p.matcher(panCardNo);
	 
	        // Return if the PAN Card number
	        // matched the ReGex
	        return m.matches();
	    }
	  
	  public static String checkEmail(String email) {
		  try {
		  Pattern pattern = Pattern.compile(regex);  
		  Matcher matcher = pattern.matcher(email);
		  if(matcher.matches())
			  return email;
		  else
			  return "";
		  }catch (Exception e) {
			// TODO: handle exception
			  return "";
		}
		  
	  }
	  
		public static String getPincode(String secname) {
			  Map<String,String> map = new HashMap<String,String>();
			  map.put("KUNCHANAPALLI","522501");
			  map.put("AE-RURALS","522201");
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
			  map.put("UNGUTURU","521109");
			  map.put("MOVVA","521135");
			  map.put("PEDAPARAPUDI","521263");
			  map.put("GANAPAVARAM","522619");
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
			  map.put("GANAPAVARUM","522619");
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
			  map.put("CHEBROLU","522212");
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
	  
	  public static String getSUBDEPTCODE(String subcode) {
		  Map<String,String> map = new HashMap<String,String>();
		  map.put("SGREV01REV60","REV60");
		  map.put("BSUNSNS","NS");
		  map.put("SGREV01REV61","REV61");
		  map.put("SGREV01REV62","REV62");
		  map.put("SGREV01REV63","REV63");
		  map.put("SGREV01REV64","REV64");
		  map.put("SGRTG01RTG51","RTG51");
		  map.put("SGSEI01SEI51","SEI51");
		  map.put("SGSEI01SEI52","SEI52");
		  map.put("SGSEI01SEI53","SEI53");
map.put("SGSEI01SEI53","SEI53");
map.put("NGUNSUNS","UNS");
map.put("SGSOW01SOW05","SOW05");
map.put("SGSOW01SOW06","SOW06");
map.put("SGSOW01SOW51","SOW51");
map.put("SGSOW01SOW54","SOW54");
map.put("SGSOW01SOW55","SOW55");
map.put("SGSOW01SOW56","SOW56");
map.put("SGTRB01TRB51","TRB51");
map.put("SGINC01ENE04","ENE04");
map.put("SGHOM01HOM04","HOM04");
map.put("SGSOW01SOW03","SOW03");
map.put("SGAGC05AG101","AG101");
map.put("SGAGC05AG139","AG139");
map.put("SGHOM01HOM01","HOM01");
map.put("SGAGC05AG280","AG280");
map.put("SGAGC05AG173","AG173");
map.put("SGAGC05AG194","AG194");
map.put("SGAGC05AG225","AG225");
map.put("SGAGC05AG252","AG252");
map.put("PVTBNK01","10001");
map.put("PVTBNK02","20001");
map.put("PVTBRK01","10002");
map.put("PVTCIN01","10003");
map.put("PVTEDU01","10005");
map.put("PVTEDU02","20002");
map.put("PVTEDU03","30001");
map.put("PVTEDU04","40001");
map.put("PVTEDU05","50001");
map.put("PVTFNH01","10006");
map.put("PVTHOT01","10008");
map.put("PVTHOT02","20003");
map.put("PVTHOT03","30002");
map.put("PVTIND01","10010");
map.put("PVTIND02","20004");
map.put("PVTIND03","30003");
map.put("PVTIND04","40002");
map.put("PVTIND05","50002");
map.put("PVTIND06","60002");
map.put("PVTIND07","70001");
map.put("PVTIND08","80001");
map.put("PVTIND09","90001");
map.put("PVTIND10","10110");
map.put("PVTIND11","11001");
map.put("PVTMED01","10011");
map.put("PVTMIL01","10012");
map.put("PVTMIL02","20005");
map.put("PVTMIL03","30004");
map.put("PVTMLK01","10013");
map.put("PVTOIL01","10014");
map.put("PVTOIL02","20006");
map.put("PVTOIL03","30005");
map.put("PVTOIL04","30006");
map.put("PVTPOU01","10015");
map.put("PVTPOW01","10016");
map.put("PVTSTG01","10018");
map.put("PVTSTG02","20008");
map.put("PVTTPL01","10020");
map.put("PVTTPL02","20010");
map.put("PVTTPL03","30007");
map.put("PVTTRT01","10021");
map.put("PVTWTR01","10023");
map.put("PVTTEL01","10019");
map.put("PVTTEL02","20009");
map.put("PVTTEL03","30006");
map.put("PVTTEL04","40004");
map.put("PVTTEL05","50003");
map.put("PVTTEL06","60003");
map.put("PVTTEL09","90002");
map.put("SGAGC05AG285","AG285");
map.put("PVTTTD01","10022");
map.put("PVTTTD02","20011");
map.put("PVTTTD03","30008");
map.put("PVTTTD04","40005");
map.put("PVTTTD05","50004");
map.put("PVTUNSUNS","UNS");
map.put("SGEHE01EHE54","EHE54");
map.put("SGINC01ENE62","ENE62");
map.put("PVTSHP01","10017");
map.put("SGHMF01HMF56","HMF56");
map.put("SGLAE01LAE53","LAE53");
map.put("PVTIND12","12001");
map.put("PVTEDU06","60001");
map.put("PVTSHP02","20007");
map.put("PVTHOW01","10009");
map.put("PVTTEL10","10111");
map.put("PVTTEL11","11002");
map.put("SGMAU01MA035","MA035");
map.put("SGMAU01MA084","MA084");
map.put("PVTDHO01","10004");
map.put("PVTIND13","13001");
map.put("PVTTEL07","70002");
map.put("PVTTEL08","80002");
map.put("SGPRR05PR006","PR006");
map.put("SGBCW01BCW02","BCW02");
map.put("SGEHE01EHE02","EHE02");
map.put("SGEHE01EHE06","EHE06");
map.put("SGESE01ESE02","ESE02");
map.put("SGESE01ESE09","ESE09");
map.put("SGENE01ENE02","ENE02");
map.put("SGINC01INC04","INC04");
map.put("SGFIN01FIN08","FIN08");
map.put("SGGAD01GAD02","GAD02");
map.put("SGGAD01GAD10","GAD10");
map.put("SGGAD01GAD18","GAD18");
map.put("SGHMF01HMF04","HMF04");
map.put("SGHMF01HMF06","HMF06");
map.put("SGHOM01HOM03","HOM03");
map.put("SGHOM01HOM12","HOM12");
map.put("SGICD01ICD02","ICD02");
map.put("SGICD01ICD05","ICD05");
map.put("SGITC01ITC02","ITC02");
map.put("SGLAE01LAE05","LAE05");
map.put("SGLAE01LAE19","LAE19");
map.put("SGMAU01MAU03","MAU03");
map.put("SGPLG01PLG02","PLG02");
map.put("SGREV01REV02","REV02");
map.put("SGREV01REV04","REV04");
map.put("SGSOW01SOW02","SOW02");
map.put("SGSOW01SOW04","SOW04");
map.put("SGTRB01TRB03","TRB03");
map.put("SGWDC01WDC03","WDC03");
map.put("SGYTC01YTC05","YTC05");
map.put("SGHOM02HOM45","HOM45");
map.put("SGHOM02HOM49","HOM49");
map.put("SGICD01ICD06","ICD06");
map.put("SGICD05ICD19","ICD19");
map.put("SGICD01ICD26","ICD26");
map.put("SGICD01ICD43","ICD43");
map.put("SGICD01ICD08","ICD08");
map.put("SGICD01ICD17","ICD17");
map.put("SGICD01ICD21","ICD21");
map.put("SGICD01ICD27","ICD27");
map.put("SGICD01ICD34","ICD34");
map.put("SGICD05ICD18","ICD18");
map.put("SGHOM02HOM15","HOM15");
map.put("SGAGC05AG109","AG109");
map.put("SGAGC05AG115","AG115");
map.put("SGAGC05AG122","AG122");
map.put("SGAGC05AG129","AG129");
map.put("SGAGC05AG135","AG135");
map.put("SGAGC05AG142","AG142");
map.put("SGAGC05AG148","AG148");
map.put("SGAGC05AG155","AG155");
map.put("SGAGC05AG281","AG281");
map.put("SGAGC05AG288","AG288");
map.put("SGAGC01AGC55","AGC55");
map.put("SGAGC01AGC66","AGC66");
map.put("SGAHF01AHF54","AHF54");
map.put("SGENE01ENE61","ENE61");
map.put("SGENE01ENE66","ENE66");
map.put("SGESE01ESE51","ESE51");
map.put("SGESE01ESE53","ESE53");
map.put("SGFCS01FCS52","FCS52");
map.put("SGGAD01GAD04","GAD04");
map.put("SGGAD01GAD12","GAD12");
map.put("SGGAD01GAD19","GAD19");
map.put("SGGAD01GAD52","GAD52");
map.put("CGNABARDNABARD01","NABARD01");
map.put("CGAAIAAI01","AAI01");
map.put("CGBHELBHEL01","BHEL01");
map.put("CGAEAE01","AE01");
map.put("CGCECE01","CE01");
map.put("CGITIT01","IT01");
map.put("CGGWCGWC01","GWC01");
map.put("CGAIRAIR01","AIR01");
map.put("CGDDDD01","DD01");
map.put("CGBPBP01","BP01");
map.put("CGECILECIL01","ECIL01");
map.put("CGBNKBNK01","BNK01");
map.put("CGBNKBNK02","BNK02");
map.put("CGBNKBNK03","BNK03");
map.put("CGBNKBNK04","BNK04");
map.put("CGBNKBNK05","BNK05");
map.put("CGBNKBNK06","BNK06");
map.put("CGISROISRO01","ISRO01");
map.put("CGPSTPST01","PST01");
map.put("CGPSTPST01","PST01");
map.put("PVTROG37","37001");
map.put("PVTIND14","14001");
map.put("PVTIND15","15001");
map.put("CGBSNLBSNL04","BSNL04");
map.put("CGRLYRLY01","RLY01");
map.put("CGRLYRLY02","RLY02");
map.put("CGRLYRLY03","RLY03");
map.put("CGRLYRLY04","RLY04");
map.put("CGNCCNCC01","NCC01");
map.put("CGSAISAI01","SAI01");
map.put("CGSAILSAIL01","SAIL01");
map.put("CGNTPCNTPC01","NTPC01");
map.put("SGAGC01AGC02","AGC02");
map.put("SGAGC01AGC03","AGC03");
map.put("SGAGC01AGC04","AGC04");
map.put("SGAGC01AGC05","AGC05");
map.put("SGAGC01AGC06","AGC06");
map.put("SGAHF01AHF02","AHF02");
map.put("SGAHF01AHF03","AHF03");
map.put("SGEFS01EFS02","EFS02");
map.put("SGEHE01EHE03","EHE03");
map.put("SGEHE01EHE04","EHE04");
map.put("SGESE01EHE05","EHE05");
map.put("SGESE01ESE08","ESE08");
map.put("SGESE01ESE10","ESE10");
map.put("SGFCS01FCS02","FCS02");
map.put("SGFCS01FCS03","FCS03");
map.put("SGFIN01FIN02","FIN02");
map.put("SGFIN01FIN03","FIN03");
map.put("SGFIN01FIN04","FIN04");
map.put("SGFIN01FIN05","FIN05");
map.put("SGGAD01GAD05","GAD05");
map.put("SGGAD01GAD06","GAD06");
map.put("SGGAD01GAD11","GAD11");
map.put("SGHMF01HMF02","HMF02");
map.put("SGHMF01HMF03","HMF03");
map.put("SGHMF01HMF05","HMF05");
map.put("SGHMF01HMF07","HMF07");
map.put("SGHOM01HOM02","HOM02");
map.put("SGHOM01HOM05","HOM05");
map.put("SGHOM01HOM06","HOM06");
map.put("SGICD01ICD03","ICD03");
map.put("SGICD01ICD33","ICD33");
map.put("SGINC01INC02","INC02");
map.put("SGINC01INC03","INC03");
map.put("SGINC01INC05","INC05");
map.put("SGLAE01LAE02","LAE02");
map.put("SGLAE01LAE03","LAE03");
map.put("SGLAE01LAE18","LAE18");
map.put("SGHOM01LAW03","LAW03");
map.put("SGMAU01MAU02","MAU02");
map.put("SGMAU01MAU04","MAU04");
map.put("SGMNW01MNW03","MNW03");
map.put("SGPRR01PRR02","PRR02");
map.put("SGPRR01PRR03","PRR03");
map.put("SGPRR01PRR05","PRR05");
map.put("SGPRR01PRR06","PRR06");
map.put("SGREV01REV03","REV03");
map.put("SGREV01REV05","REV05");
map.put("SGREV01REV07","REV07");
map.put("SGREV01REV08","REV08");
map.put("SGGAD01GAD54","GAD54");
map.put("SGHMF01HMF54","HMF54");
map.put("SGHMF01HMF58","HMF58");
map.put("SGICD01ICD51","ICD51");
map.put("SGICD01ICD53","ICD53");
map.put("SGINC01INC60","INC60");
map.put("SGITC01ITC52","ITC52");
map.put("SGITC01ITC54","ITC54");
map.put("SGITC01ITC56","ITC56");
map.put("SGLAE01LAE51","LAE51");
map.put("SGMAU01MA002","MA002");
map.put("SGMAU01MA006","MA006");
map.put("SGMAU01MA008","MA008");
map.put("SGMAU01MA047","MA047");
map.put("SGMAU01MA049","MA049");
map.put("SGMAU01MA051","MA051");
map.put("SGMAU01MA054","MA054");
map.put("SGMAU01MA056","MA056");
map.put("SGMAU01MA058","MA058");
map.put("SGMAU01MA060","MA060");
map.put("SGMAU01MA063","MA063");
map.put("SGMAU01MA065","MA065");
map.put("SGMAU01MA067","MA067");
map.put("SGMAU01MA070","MA070");
map.put("SGMAU01MA072","MA072");
map.put("SGMAU01MA074","MA074");
map.put("SGMAU01MA077","MA077");
map.put("SGMAU01MA079","MA079");
map.put("SGMAU01MA081","MA081");
map.put("SGMAU01MA083","MA083");
map.put("SGMAU01MA086","MA086");
map.put("SGMAU01MA088","MA088");
map.put("SGMAU01MA090","MA090");
map.put("SGMAU01MA093","MA093");
map.put("SGMAU01MA095","MA095");
map.put("SGMAU01MA097","MA097");
map.put("SGMAU01MA099","MA099");
map.put("SGMAU01MA102","MA102");
map.put("SGMAU01MA104","MA104");
map.put("SGMAU01MA107","MA107");
map.put("SGMAU01MA109","MA109");
map.put("SGMAU01MA111","MA111");
map.put("SGMAU01MAU54","MAU54");
map.put("SGMAU01MAU56","MAU56");
map.put("SGMAU01MAU58","MAU58");
map.put("SGMAU01MAU61","MAU61");
map.put("SGMAU01MAU64","MAU64");
map.put("SGMAU01MAU68","MAU68");
map.put("SGMNW01MNW02","MNW02");
map.put("SGMNW01MNW54","MNW54");
map.put("SGMNW01MNW57","MNW57");
map.put("SGPLG01PLG51","PLG51");
map.put("SGPRR05PR003","PR003");
map.put("SGPRR05PR009","PR009");
map.put("SGPRR01PRR07","PRR07");
map.put("SGREV01REV54","REV54");
map.put("SGWDC01WDC51","WDC51");
map.put("SGYTC01YTC51","YTC51");
map.put("SGYTC01YTC56","YTC56");
map.put("SGYTC01YTC60","YTC60");
map.put("SGREV01REV01","REV01");
map.put("SGRTG01RTG01","RTG01");
map.put("SGINC01INC01","INC01");
map.put("SGAGC05AG103","AG103");
map.put("CGBSNLBSNL01","BSNL01");
map.put("CGBSNLBSNL02","BSNL02");
map.put("CGBSNLBSNL03","BSNL03");
map.put("BSAPTRAUNS","UNS");
map.put("BSAPSPDUNS","UNS");
map.put("BSAPCPDUNS","UNS");
map.put("BSAPGENUNS","UNS");
map.put("SGMAU01MA062","MA062");
map.put("SGMAU01MA064","MA064");
map.put("SGMAU01MA066","MA066");
map.put("SGMAU01MA068","MA068");
map.put("SGMAU01MA069","MA069");
map.put("SGMAU01MA071","MA071");
map.put("SGMAU01MA073","MA073");
map.put("SGMAU01MA075","MA075");
map.put("SGMAU01MA076","MA076");
map.put("SGMAU01MA078","MA078");
map.put("SGMAU01MA080","MA080");
map.put("SGMAU01MA082","MA082");
map.put("SGMAU01MA085","MA085");
map.put("SGMAU01MA087","MA087");
map.put("SGMAU01MA089","MA089");
map.put("SGMAU01MA091","MA091");
map.put("SGMAU01MA092","MA092");
map.put("SGMAU01MA094","MA094");
map.put("SGMAU01MA096","MA096");
map.put("SGMAU01MA098","MA098");
map.put("SGMAU01MA100","MA100");
map.put("SGMAU01MA101","MA101");
map.put("SGMAU01MA103","MA103");
map.put("SGMAU01MA105","MA105");
map.put("SGMAU01MA106","MA106");
map.put("SGMAU01MA108","MA108");
map.put("SGMAU01MA110","MA110");
map.put("SGMAU01MAU51","MAU51");
map.put("SGMAU01MAU53","MAU53");
map.put("SGMAU01MAU55","MAU55");
map.put("SGMAU01MAU57","MAU57");
map.put("SGMAU01MAU59","MAU59");
map.put("SGMAU01MAU60","MAU60");
map.put("SGMAU01MAU62","MAU62");
map.put("SGMAU01MAU63","MAU63");
map.put("SGMAU01MAU65","MAU65");
map.put("SGMAU01MAU67","MAU67");
map.put("SGMAU01MAU69","MAU69");
map.put("SGMAU01MAU70","MAU70");
map.put("SGMNW01MNW51","MNW51");
map.put("SGMNW01MNW53","MNW53");
map.put("SGMNW01MNW55","MNW55");
map.put("SGMNW01MNW56","MNW56");
map.put("SGMNW03MNW58","MNW58");
map.put("SGPLG01PLG53","PLG53");
map.put("SGPRR05PR001","PR001");
map.put("SGPRR05PR002","PR002");
map.put("SGPRR05PR004","PR004");
map.put("SGPRR05PR007","PR007");
map.put("SGPRR05PR008","PR008");
map.put("SGPRR05PR010","PR010");
map.put("SGPRR05PR011","PR011");
map.put("SGPRR05PR013","PR013");
map.put("SGPRR01PRR51","PRR51");
map.put("SGPRR01PRR52","PRR52");
map.put("SGPRR01PRR54","PRR54");
map.put("SGPRR01PRR55","PRR55");
map.put("SGREV01REV52","REV52");
map.put("SGREV01REV53","REV53");
map.put("SGREV01REV55","REV55");
map.put("SGREV55REV56","REV56");
map.put("SGREV01REV58","REV58");
map.put("SGINC05INC63","INC63");
map.put("SGITC01ITC57","ITC57");
map.put("SGMAU01MA004","MA004");
map.put("SGMAU01MA005","MA005");
map.put("SGMAU01MA013","MA013");
map.put("SGMAU01MA014","MA014");
map.put("SGMAU01MA020","MA020");
map.put("SGMAU01MA021","MA021");
map.put("SGMAU01MAU66","MAU66");
map.put("SGMNW01MNW52","MNW52");
map.put("SGITC01PLG52","PLG52");
map.put("SGPRR05PR005","PR005");
map.put("SGPRR05PR012","PR012");
map.put("SGPRR01PRR56","PRR56");
map.put("SGREV01REV51","REV51");
map.put("SGREV01REV59","REV59");
map.put("SGREV01REV65","REV65");
map.put("SGSOW01SOW52","SOW52");
map.put("SGSOW01SOW53","SOW53");
map.put("SGTRB01TRB52","TRB52");
map.put("SGAGC05AG172","AG172");
map.put("SGAGC05AG174","AG174");
map.put("SGAGC05AG175","AG175");
map.put("SGAGC05AG176","AG176");
map.put("SGAGC05AG190","AG190");
map.put("SGAGC05AG191","AG191");
map.put("SGAGC05AG192","AG192");
map.put("SGAGC05AG193","AG193");
map.put("SGAGC05AG195","AG195");
map.put("SGAGC05AG196","AG196");
map.put("SGAGC05AG197","AG197");
map.put("SGAGC05AG198","AG198");
map.put("SGAGC05AG199","AG199");
map.put("SGAGC05AG200","AG200");
map.put("SGAGC05AG201","AG201");
map.put("SGAGC05AG202","AG202");
map.put("SGAGC05AG203","AG203");
map.put("SGAGC05AG204","AG204");
map.put("SGAGC05AG205","AG205");
map.put("SGAGC05AG206","AG206");
map.put("SGAGC05AG207","AG207");
map.put("SGAGC05AG208","AG208");
map.put("SGAGC05AG209","AG209");
map.put("SGAGC05AG210","AG210");
map.put("SGAGC05AG211","AG211");
map.put("SGAGC05AG212","AG212");
map.put("SGAGC05AG213","AG213");
map.put("SGAGC05AG214","AG214");
map.put("SGAGC05AG215","AG215");
map.put("SGAGC05AG216","AG216");
map.put("SGAGC05AG217","AG217");
map.put("SGAGC05AG218","AG218");
map.put("SGAGC05AG219","AG219");
map.put("SGAGC05AG220","AG220");
map.put("SGAGC05AG221","AG221");
map.put("SGAGC05AG222","AG222");
map.put("SGAGC05AG223","AG223");
map.put("SGAGC05AG224","AG224");
map.put("SGAGC05AG226","AG226");
map.put("SGAGC05AG227","AG227");
map.put("SGAGC05AG228","AG228");
map.put("SGAGC05AG229","AG229");
map.put("SGAGC05AG230","AG230");
map.put("SGAGC05AG231","AG231");
map.put("SGAGC05AG232","AG232");
map.put("SGAGC05AG233","AG233");
map.put("SGAGC05AG234","AG234");
map.put("SGAGC05AG235","AG235");
map.put("SGAGC05AG236","AG236");
map.put("SGAGC05AG237","AG237");
map.put("SGAGC05AG238","AG238");
map.put("SGAGC05AG239","AG239");
map.put("SGAGC05AG240","AG240");
map.put("SGAGC05AG241","AG241");
map.put("SGAGC05AG242","AG242");
map.put("SGAGC05AG243","AG243");
map.put("SGAGC05AG244","AG244");
map.put("SGAGC05AG245","AG245");
map.put("SGAGC05AG246","AG246");
map.put("SGAGC05AG177","AG177");
map.put("SGAGC05AG178","AG178");
map.put("SGAGC05AG179","AG179");
map.put("SGAGC05AG180","AG180");
map.put("SGAGC05AG181","AG181");
map.put("SGAGC05AG182","AG182");
map.put("SGAGC05AG183","AG183");
map.put("SGAGC05AG184","AG184");
map.put("SGAGC05AG185","AG185");
map.put("SGAGC05AG186","AG186");
map.put("SGAGC05AG187","AG187");
map.put("SGAGC05AG188","AG188");
map.put("SGAGC05AG189","AG189");
map.put("SGAGC05AG247","AG247");
map.put("SGAGC05AG248","AG248");
map.put("SGAGC05AG249","AG249");
map.put("SGAGC05AG250","AG250");
map.put("SGAGC05AG251","AG251");
map.put("SGAGC05AG253","AG253");
map.put("SGAGC05AG254","AG254");
map.put("SGAGC05AG255","AG255");
map.put("SGAGC05AG256","AG256");
map.put("SGAGC05AG257","AG257");
map.put("SGAGC05AG258","AG258");
map.put("SGAGC05AG259","AG259");
map.put("SGAGC05AG260","AG260");
map.put("SGAGC05AG261","AG261");
map.put("SGAGC05AG262","AG262");
map.put("SGAGC05AG263","AG263");
map.put("SGAGC05AG264","AG264");
map.put("SGAGC05AG265","AG265");
map.put("SGAGC05AG266","AG266");
map.put("SGAGC05AG267","AG267");
map.put("SGAGC05AG268","AG268");
map.put("SGAGC05AG269","AG269");
map.put("SGAGC05AG270","AG270");
map.put("SGAGC05AG271","AG271");
map.put("SGAGC05AG272","AG272");
map.put("SGAGC05AG273","AG273");
map.put("SGAGC05AG274","AG274");
map.put("SGAGC05AG275","AG275");
map.put("SGAGC05AG276","AG276");
map.put("SGAGC05AG277","AG277");
map.put("SGAGC05AG278","AG278");
map.put("SGAGC05AG279","AG279");
map.put("SGAGC05AG282","AG282");
map.put("SGAGC05AG283","AG283");
map.put("SGAGC05AG284","AG284");
map.put("SGAGC05AG287","AG287");
map.put("SGAGC05AG289","AG289");
map.put("SGAGC01AGC51","AGC51");
map.put("SGAGC01AGC52","AGC52");
map.put("SGAGC01AGC53","AGC53");
map.put("SGAGC01AGC56","AGC56");
map.put("SGAGC01AGC57","AGC57");
map.put("SGAGC01AGC58","AGC58");
map.put("SGINC01ENE64","ENE64");
map.put("SGINC01ENE65","ENE65");
map.put("SGENE01ENE67","ENE67");
map.put("SGENE58ENE68","ENE68");
map.put("SGENE58ENE69","ENE69");
map.put("SGESE01ESE13","ESE13");
map.put("SGESE01ESE52","ESE52");
map.put("SGESE01ESE54","ESE54");
map.put("SGESE01ESE55","ESE55");
map.put("SGFCS01FCS04","FCS04");
map.put("SGFCS01FCS51","FCS51");
map.put("SGFCS01FCS53","FCS53");
map.put("SGFIN01FIN51","FIN51");
map.put("SGGAD01GAD03","GAD03");
map.put("SGGAD01GAD08","GAD08");
map.put("SGGAD01GAD13","GAD13");
map.put("SGGAD01GAD17","GAD17");
map.put("SGINC01GAD51","GAD51");
map.put("SGGAD01GAD53","GAD53");
map.put("SGGAD01GAD55","GAD55");
map.put("SGHMF01HMF51","HMF51");
map.put("SGHMF01HMF52","HMF52");
map.put("SGHMF01HMF53","HMF53");
map.put("SGHMF01HMF55","HMF55");
map.put("SGHMF01HMF57","HMF57");
map.put("SGHMF01HMF59","HMF59");
map.put("SGHOM01HOM10","HOM10");
map.put("SGHOM01HOM51","HOM51");
map.put("SGHOU01HOU02","HOU02");
map.put("SGICD01ICD52","ICD52");
map.put("SGINC01INC51","INC51");
map.put("SGINC01INC52","INC52");
map.put("SGINC01INC53","INC53");
map.put("SGINC01INC54","INC54");
map.put("SGINC01INC55","INC55");
map.put("SGINC01INC56","INC56");
map.put("SGINC01INC58","INC58");
map.put("SGINC01INC59","INC59");
map.put("SGINC01INC61","INC61");
map.put("SGINC01INC62","INC62");
map.put("SGITC01ITC51","ITC51");
map.put("SGITC01ITC53","ITC53");
map.put("SGITC01ITC55","ITC55");
map.put("SGLAE01LAE10","LAE10");
map.put("SGLAE01LAE11","LAE11");
map.put("SGLAE01LAE12","LAE12");
map.put("SGLAE01LAE52","LAE52");
map.put("SGLAW01LAW02","LAW02");
map.put("SGMAU01MA003","MA003");
map.put("SGMAU01MA007","MA007");
map.put("SGMAU01MA009","MA009");
map.put("SGMAU01MA010","MA010");
map.put("SGMAU01MA011","MA011");
map.put("SGMAU01MA012","MA012");
map.put("SGMAU01MA015","MA015");
map.put("SGMAU01MA016","MA016");
map.put("SGMAU01MA017","MA017");
map.put("SGMAU01MA018","MA018");
map.put("SGMAU01MA019","MA019");
map.put("SGMAU01MA022","MA022");
map.put("SGMAU01MA023","MA023");
map.put("SGMAU01MA024","MA024");
map.put("SGMAU01MA025","MA025");
map.put("SGMAU01MA026","MA026");
map.put("SGMAU01MA027","MA027");
map.put("SGMAU01MA028","MA028");
map.put("SGMAU01MA029","MA029");
map.put("SGMAU01MA030","MA030");
map.put("SGMAU01MA031","MA031");
map.put("SGMAU01MA032","MA032");
map.put("SGMAU01MA033","MA033");
map.put("SGMAU01MA034","MA034");
map.put("SGMAU01MA036","MA036");
map.put("SGMAU01MA037","MA037");
map.put("SGMAU01MA038","MA038");
map.put("SGMAU01MA039","MA039");
map.put("SGMAU01MA040","MA040");
map.put("SGMAU01MA041","MA041");
map.put("SGMAU01MA042","MA042");
map.put("SGMAU01MA043","MA043");
map.put("SGMAU01MA044","MA044");
map.put("SGMAU01MA045","MA045");
map.put("SGMAU01MA046","MA046");
map.put("SGMAU01MA048","MA048");
map.put("SGMAU01MA050","MA050");
map.put("SGMAU01MA052","MA052");
map.put("SGMAU01MA053","MA053");
map.put("SGMAU01MA055","MA055");
map.put("SGMAU01MA057","MA057");
map.put("SGMAU01MA059","MA059");
map.put("SGMAU01MA061","MA061");
map.put("PVTGOS01","10007");
map.put("SGMAU01MA112","MA112");
map.put("SGEDU01","10005");
map.put("CGEDU01","10005");
map.put("PVTNBCBPL01","BPL01");
map.put("PVTLRCBPL01","BPL01");
map.put("PVTMBCBPL01","BPL01");
map.put("PVTGSPBPL01","BPL01");
map.put("SGAGC01AGC59","AGC59");
map.put("SGAGC01AGC60","AGC60");
map.put("SGAGC01AGC61","AGC61");
map.put("SGAGC01AGC62","AGC62");
map.put("SGAGC01AGC63","AGC63");
map.put("SGAHF01AHF51","AHF51");
map.put("SGAHF01AHF53","AHF53");
map.put("SGAHF01AHF55","AHF55");
map.put("SGAHF01AHF56","AHF56");
map.put("SGBCW01BCW51","BCW51");
map.put("SGBCW01BCW52","BCW52");
map.put("SGBCW01BCW53","BCW53");
map.put("SGBCW01BCW54","BCW54");
map.put("SGBCW01BCW55","BCW55");
map.put("SGBCW01BCW56","BCW56");
map.put("SGBCW01BCW57","BCW57");
map.put("SGBCW01BCW58","BCW58");
map.put("SGBCW01BCW59","BCW59");
map.put("SGBCW01BCW60","BCW60");
map.put("SGBCW01BCW61","BCW61");
map.put("SGBCW01BCW62","BCW62");
map.put("SGBCW01BCW63","BCW63");
map.put("SGBCW01BCW64","BCW64");
map.put("SGBCW01BCW65","BCW65");
map.put("SGEFS01EFS51","EFS51");
map.put("SGEFS01EFS52","EFS52");
map.put("SGEFS01EFS53","EFS53");
map.put("SGEFS01EFS54","EFS54");
map.put("SGEFS01EFS55","EFS55");
map.put("SGEFS01EFS56","EFS56");
map.put("SGEFS01EFS57","EFS57");
map.put("SGEFS01EFS58","EFS58");
map.put("SGEFS01EFS59","EFS59");
map.put("SGEHE01EHE51","EHE51");
map.put("SGEHE01EHE52","EHE52");
map.put("SGEHE01EHE53","EHE53");
map.put("SGEHE01EHE55","EHE55");
map.put("SGEHE01EHE56","EHE56");
map.put("SGEHE01EHE57","EHE57");
map.put("SGEHE01EHE58","EHE58");
map.put("SGESE01EHE59","EHE59");
map.put("SGEHE01EHE60","EHE60");
map.put("SGEHE01EHE61","EHE61");
map.put("SGEHE01EHE62","EHE62");
map.put("SGEHE01EHE63","EHE63");
map.put("SGEHE01EHE64","EHE64");
map.put("SGEHE01EHE65","EHE65");
map.put("SGEHE01EHE66","EHE66");
map.put("SGEHE01EHE67","EHE67");
map.put("SGEHE01EHE68","EHE68");
map.put("SGEHE01EHE69","EHE69");
map.put("SGEHE01EHE70","EHE70");
map.put("SGEHE01EHE71","EHE71");
map.put("SGESE01EHE72","EHE72");
map.put("SGEHE01EHE73","EHE73");
map.put("SGEHE01EHE74","EHE74");
map.put("SGENE01ENE51","ENE51");
map.put("SGENE01ENE52","ENE52");
map.put("SGENE01ENE53","ENE53");
map.put("SGENE01ENE54","ENE54");
map.put("SGENE01ENE55","ENE55");
map.put("SGINC01ENE56","ENE56");
map.put("SGENE01ENE57","ENE57");
map.put("SGINC01ENE58","ENE58");
map.put("SGINC01ENE59","ENE59");
map.put("SGINC01ENE60","ENE60");
map.put("SGINC01ENE63","ENE63");
map.put("SGTRB01TRB02","TRB02");
map.put("SGWDC01WDC02","WDC02");
map.put("SGWDC01WDC04","WDC04");
map.put("SGYTC01YTC04","YTC04");
map.put("SGYTC01YTC07","YTC07");
map.put("SGAGC01AG001","AG001");
map.put("SGAGC01AG002","AG002");
map.put("SGAGC01AG003","AG003");
map.put("SGAGC01AG004","AG004");
map.put("SGAGC01AG005","AG005");
map.put("SGAGC01AG006","AG006");
map.put("SGAGC01AG007","AG007");
map.put("SGAGC01AG008","AG008");
map.put("SGAGC01AG009","AG009");
map.put("SGAGC01AG010","AG010");
map.put("SGAGC01AG011","AG011");
map.put("SGAGC01AG012","AG012");
map.put("SGAGC01AG013","AG013");
map.put("SGAGC05AG102","AG102");
map.put("SGAGC05AG104","AG104");
map.put("SGAGC05AG105","AG105");
map.put("SGAGC05AG106","AG106");
map.put("SGAGC05AG107","AG107");
map.put("SGAGC05AG108","AG108");
map.put("SGAGC05AG110","AG110");
map.put("SGAGC05AG111","AG111");
map.put("SGAGC05AG112","AG112");
map.put("SGAGC05AG113","AG113");
map.put("SGAGC05AG114","AG114");
map.put("SGAGC05AG116","AG116");
map.put("SGAGC05AG117","AG117");
map.put("SGAGC05AG118","AG118");
map.put("SGAGC05AG119","AG119");
map.put("SGAGC05AG120","AG120");
map.put("SGAGC05AG121","AG121");
map.put("SGAGC05AG123","AG123");
map.put("SGAGC05AG124","AG124");
map.put("SGAGC05AG125","AG125");
map.put("SGAGC05AG126","AG126");
map.put("SGAGC05AG127","AG127");
map.put("SGAGC05AG128","AG128");
map.put("SGAGC05AG130","AG130");
map.put("SGAGC05AG131","AG131");
map.put("SGAGC05AG132","AG132");
map.put("SGAGC05AG133","AG133");
map.put("SGAGC05AG134","AG134");
map.put("SGAGC05AG136","AG136");
map.put("SGAGC05AG137","AG137");
map.put("SGAGC05AG138","AG138");
map.put("SGAGC05AG140","AG140");
map.put("SGAGC05AG141","AG141");
map.put("SGAGC05AG143","AG143");
map.put("SGAGC05AG144","AG144");
map.put("SGAGC05AG145","AG145");
map.put("SGAGC05AG146","AG146");
map.put("SGAGC05AG147","AG147");
map.put("SGAGC05AG149","AG149");
map.put("SGAGC05AG150","AG150");
map.put("SGAGC05AG151","AG151");
map.put("SGAGC05AG152","AG152");
map.put("SGAGC05AG153","AG153");
map.put("SGAGC05AG154","AG154");
map.put("SGAGC05AG156","AG156");
map.put("SGAGC05AG157","AG157");
map.put("SGAGC05AG158","AG158");
map.put("SGAGC05AG159","AG159");
map.put("SGAGC05AG160","AG160");
map.put("SGAGC05AG161","AG161");
map.put("SGAGC05AG162","AG162");
map.put("SGAGC05AG163","AG163");
map.put("SGAGC05AG164","AG164");
map.put("SGAGC05AG165","AG165");
map.put("SGAGC05AG166","AG166");
map.put("SGAGC05AG167","AG167");
map.put("SGAGC05AG168","AG168");
map.put("SGAGC05AG170","AG170");
map.put("SGAGC05AG171","AG171");
map.put("SGTRB01TRB53","TRB53");
map.put("SGWDC01WDC52","WDC52");
map.put("SGWDC01WDC53","WDC53");
map.put("SGYTC01YTC52","YTC52");
map.put("SGYTC01YTC54","YTC54");
map.put("SGYTC01YTC55","YTC55");
map.put("SGYTC01YTC57","YTC57");
map.put("SGYTC01YTC58","YTC58");
map.put("SGYTC01YTC59","YTC59");
map.put("SGGAD01GAD56","GAD56");
map.put("SGHOM02HOM09","HOM09");
map.put("SGHOM02HOM13","HOM13");
map.put("SGHOM02HOM46","HOM46");
map.put("SGHOM02HOM47","HOM47");
map.put("SGHOM02HOM48","HOM48");
map.put("SGHOM02HOM50","HOM50");
map.put("SGICD01ICD12","ICD12");
map.put("SGICD01ICD20","ICD20");
map.put("SGICD01ICD25","ICD25");
map.put("SGICD01ICD28","ICD28");
map.put("SGICD01ICD32","ICD32");
map.put("SGICD05ICD36","ICD36");
map.put("SGICD05ICD44","ICD44");
map.put("SGLAW01LAW51","LAW51");
map.put("SGTRB01TRB01","TRB01");
map.put("SGMAU01MAU01","MAU01");
map.put("SGPLG01PLG01","PLG01");
map.put("SGPRR01PRR01","PRR01");
map.put("SGICD01ICD01","ICD01");
map.put("SGMNW01MNW01","MNW01");
map.put("SGYTC01YTC01","YTC01");
map.put("SGHMF01HMF01","HMF01");
map.put("SGITC01ITC01","ITC01");
map.put("SGLAE01LAE01","LAE01");
map.put("SGAHF01AHF01","AHF01");
map.put("SGHOU01HOU01","HOU01");
map.put("SGBCW01BCW01","BCW01");
map.put("SGFCS01FCS01","FCS01");
map.put("SGENE01ENE01","ENE01");
map.put("SGFIN01FIN01","FIN01");
map.put("SGSEI01SEI01","SEI01");
map.put("SGEFS01EFS01","EFS01");
map.put("SGEHE01EHE01","EHE01");
map.put("SGGAD01GAD01","GAD01");
map.put("SGLAW01LAW01","LAW01");
map.put("SGAGC01AGC01","AGC01");
map.put("SGSOW01SOW01","SOW01");
map.put("SGESE01ESE01","ESE01");
map.put("SGWDC01WDC01","WDC01");
map.put("SGAGC05AG169","AG169");
map.put("SGAGC05AG286","AG286");
map.put("SGAGC01AGC54","AGC54");
map.put("SGAHF01AHF52","AHF52");
map.put("SGHOU01HOU51","HOU51");
map.put("SGHOU01HOU52","HOU52");
map.put("SGINC01INC57","INC57");
map.put("SGEDC11","YTC61");
map.put("BSTRA01","YTC62");
map.put("CGEXCI01","YTC63");
map.put("CGPG01","YTC64");
map.put("SGEDC06","YTC65");
map.put("CGTOBC01","YTC66");
map.put("CGSPIC01","YTC67");
map.put("CGHDMUNS","UNS");
map.put("SGRWSUNS","UNS");
map.put("SGHDM18","YTC68");
map.put("SGHDM02","YTC69");
map.put("SGRTC01","YTC70");
map.put("SGHDM10","YTC71");
map.put("SGEDC02","YTC72");
map.put("CGEPFO02","YTC73");
return Optional.ofNullable(map.get(subcode)).orElse("");
	  }
	  
	  public static String isValidMobileNo(String str)  
	  {  
	  //(0/91): number starts with (0/91)  
	  //[7-9]: starting of the number may contain a digit between 0 to 9  
	  //[0-9]: then contains digits 0 to 9  
	  Pattern ptrn = Pattern.compile("(0/91)?[7-9][0-9]{9}");  
	  //the matcher() method creates a matcher that will match the given input against this pattern  
	  Matcher match = ptrn.matcher(str);  
	  //returns a boolean value  
	  
	  
	  if (match.find() && match.group().equals(str))  
		  return str.trim();     
		  else  
		  return "";   
	  } 
}
