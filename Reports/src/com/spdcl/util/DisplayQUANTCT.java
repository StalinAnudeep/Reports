package com.spdcl.util;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.codec.multipart.SynchronossPartHttpMessageReader;

public class DisplayQUANTCT {
	public static Map<String,String> season = new HashMap<>();
	/*
	 * Need to remember To Date and From date in below methods
	 */
	//public static Map<String,String> trueup = getTrueUpMonth();
	public static Map<String,String> remainingamt = new HashMap<>();
	public static Map<String,String> totkwh = new HashMap<>();
	public static Map<String, String> gridinstldt = new HashMap<>();
	public static Map<String, String> fy21flag = new HashMap<>();
	public static Map<String, String> htfy21flag = new HashMap<>();
	public static Map<String, String> crosslist = new HashMap<>();
	public static Map<String, String> wheellist = new HashMap<>();
	
	public static Map<String, String> fppca2 = new HashMap<>();
	public static Map<String, String> fppcach = new HashMap<>();
	
	
public DisplayQUANTCT(Object o,PrintStream ps) {
	// TODO Auto-generated constructor stub
	generateStream(getUpdatedData(getData(o)),ps);
}

	public static void generateStream(Map<String, Map<String, List<OperandData>>> data, PrintStream ps) {

		for (Map.Entry<String, Map<String, List<OperandData>>> d : data.entrySet()) {
			//System.out.println(d.getKey());
			ps.println(d.getKey().toString().trim() 
					+ "\tKEY" 
					+ "\t" + d.getKey().toString().trim()
					+ "\t99991231"// 
			);
			
			for (Map.Entry<String, List<OperandData>> e : d.getValue().entrySet()) {
				
				System.out.println(e.getKey().toString().trim());
				ps.println(d.getKey().toString().trim() 
						+ "\t" +getStructure(e.getKey().toString().trim()) 
						+ "\t" + e.getKey().toString().trim()
						+ (getStructure(e.getKey().toString().trim()).equals("F_INTE")?"\t":"")
						+ "\tX"// 
				);
				List<OperandData> list = e.getValue();
				list.sort(Comparator.comparingInt(OperandData::getOrder));
				int i = 0, f = 0 ,s = 0 , t= 0 , conn = 0 , cont = 0 , ic = 0 , cc =0 ,tmo=0, fy =0 ,ht=0;
				int listsize = list.size();
				int itr = 0;
				for(OperandData od : list) {
					/*if(listsize == itr+1) {
						od.setEnddate("99991231");
					}*/
					if (od.getType().equals("QUANT")) {
						if(e.getKey().toString().equals("H_TRU_KWH")) {
						if(t==0)	
						ps.println(d.getKey().toString().trim() + "\tV_QUAN" + "\t"
								+ od.getStartdate().toString().trim() + "\t" + "99991231" + "\t"
								+ od.getConsumptionvalue().toString().trim());
						t++;
						}
						else if(e.getKey().toString().equals("H_TRU_HKWH")) {
							if(ht==0)	
							ps.println(d.getKey().toString().trim() + "\tV_QUAN" + "\t"
									+ od.getStartdate().toString().trim() + "\t" + "99991231" + "\t"
									+ od.getConsumptionvalue().toString().trim());
							ht++;
							}
					/*	else if(e.getKey().toString().equals("Q_KWH_FY")) {
							if(fy==0)
							ps.println(d.getKey().toString().trim() + "\tV_QUAN" + "\t"
									+ od.getStartdate().toString().trim() + "\t" + "99991231" + "\t"
									+ od.getConsumptionvalue().toString().trim());
							fy++;
						}*/
						else {
							ps.println(d.getKey().toString().trim() + "\tV_QUAN" + "\t"
									+ od.getStartdate().toString().trim() + "\t" + od.getEnddate().toString().trim() + "\t"
									+ od.getConsumptionvalue().toString().trim());
						}
					}
					if (od.getType().equals("DEMAND")) {

						if (e.getKey().toString().equals("D_CONN_DMD")) {
							if (conn == 0) {
								ps.println(d.getKey().toString().trim() + "\tV_DEMA" + "\t"
										+ od.getStartdate().toString().trim() + "\t" + "99991231" + "\t"
										+ od.getConsumptionvalue().toString().trim());
								conn++;
							}
						}
						else if (e.getKey().toString().equals("D_CONT_DMD")) {
							if (cont == 0) {
								ps.println(d.getKey().toString().trim() + "\tV_DEMA" + "\t"
										+ od.getStartdate().toString().trim() + "\t" + "99991231" + "\t"
										+ od.getConsumptionvalue().toString().trim());
								cont++;
							}
						}
						else if (e.getKey().toString().equals("D_INSTLCAP")) {
							System.out.println(d.getKey() +":" + gridinstldt.get(d.getKey())+" : "+od.getConsumptionvalue().toString().trim());
							if (ic == 0 && !od.getConsumptionvalue().toString().trim().equals("0") && gridinstldt.get(d.getKey())!=null) {
								
								ps.println(d.getKey().toString().trim() + "\tV_DEMA" + "\t"
										//+ gridinstldt.get(d.getKey()) + "\t" + "99991231" + "\t"
										+ od.getStartdate().toString().trim() + "\t" + "99991231" + "\t"
										+ od.getConsumptionvalue().toString().trim());
								ic++;
							}
						}
						/*else if (e.getKey().toString().equals("D_PPA_CAP")) {
							if (cc == 0) {
								ps.println(d.getKey().toString().trim() + "\tV_DEMA" + "\t"
										+ od.getStartdate().toString().trim() + "\t" + "99991231" + "\t"
										+ od.getConsumptionvalue().toString().trim());
								cc++;
							}
						}*/else {
						ps.println(d.getKey().toString().trim() + "\tV_DEMA" + "\t"
								+ od.getStartdate().toString().trim() + "\t" + od.getEnddate().toString().trim() + "\t"
								+ od.getConsumptionvalue().toString().trim());
						}

					}
					
					if (od.getType().equals("FLAG")) {
						/*if (s <= 0 && od.getConsumptionvalue().toString().trim().equals("X")) {	*/
						if (s <= 0 ) {	
								
								ps.println(d.getKey().toString().trim() + "\tV_FLAG" + "\t"
										+ od.getStartdate().toString().trim() + "\t" +( e.getKey().toString().equals("B_TRUE_UP")?od.getEnddate(): "99991231") + "\t"
										+ od.getConsumptionvalue().toString().trim());
								s++;							
						}
					}
					if (od.getType().equals("AMOUNT")) {
						if(e.getKey().toString().equals("H_TRU_MO")) {
							if(tmo==0) {
						  ps.println(d.getKey().toString().trim() + "\tV_AMOU" + "\t"
								+ od.getStartdate().toString().trim() + "\t" + "99991231" + "\t"
								+ od.getConsumptionvalue().toString().trim());
						   tmo++;
							}
						}else {
							ps.println(d.getKey().toString().trim() + "\tV_AMOU" + "\t"
									+ od.getStartdate().toString().trim() + "\t" + od.getEnddate().toString().trim() + "\t"
									+ od.getConsumptionvalue().toString().trim());
						}
					}
					if (od.getType().equals("INTE")) {
						if(i<1)
						ps.println(d.getKey().toString().trim() + "\tV_INTE" + "\t"
								+ od.getStartdate().toString().trim() + "\t" + od.getEnddate().toString().trim() + "\t"
								+ od.getConsumptionvalue().toString().trim());
						i++;
					}
					if (od.getType().equals("FACT")) {
						if(f<=0) {
						ps.println(d.getKey().toString().trim() + "\tV_FACT" + "\t"
								+ od.getStartdate().toString().trim() + "\t" + od.getEnddate().toString().trim() + "\t"
								+ od.getConsumptionvalue().toString().trim());
						f++;
						}
					}
					itr++;
				}
			}
			
					ps.println(d.getKey()+"\t&ENDE");
					
					
		}
		
	/*	for(String key: season.keySet()) {
			if(data.containsKey(key)) {
			String p[] = getMonthPeriod(season.get(key)).split("-");
			ps.println(key.toString().trim() + "\tF_INTE" + "\tI_SEAS_STR");
			ps.println(key.toString().trim() + "\tV_INTE" + "\t20220401" + "\t99993112" +"\t"+p[0]);
			ps.println(key.toString().trim() + "\tF_INTE" + "\tI_SEAS_END");
			ps.println(key.toString().trim() + "\tV_INTE" + "\t20220401" + "\t99993112" +"\t"+p[1]);
			ps.println(key+"\t&ENDE");
			}
		}
		
		for(String key: trueup.keySet()) {
			if(data.containsKey(key)) {
			ps.println(key.toString().trim() + "\tF_FACT" + "\tH_TRU_MTHS");
			ps.println(key.toString().trim() + "\tV_FACT" + "\t20220401" + "\t99993112" +"\t"+trueup.get(key));
			ps.println(key+"\t&ENDE");
			}
		}*/
		

	}
	public static Map<String, Map<String, List<OperandData>>> getUpdatedData(Map<String, Map<String, List<OperandData>>> map)
	{
		for (String key : map.keySet()) {
	        for(String okey : map.get(key).keySet()) {
	        	map.get(key).put(okey, getUpdatedList(map.get(key).get(okey)));
	        } 
	    }
		return map;
	}
	
	public static List<OperandData> getUpdatedList(List<OperandData> list){
		Comparator<OperandData> comp1 = (u1,u2) -> u1.getStartdate().compareTo(u2.getStartdate());
		Comparator<OperandData> comp2 = (u1,u2) -> u1.getEnddate().compareTo(u2.getEnddate());			
		List<OperandData> sortedlist = list.stream().sorted(comp1.thenComparing(comp2)).collect(Collectors.toList());
		for (int i = 0; i < sortedlist.size(); i++) {
			if(i!=0) {
				if(sortedlist.get(i).getStartdate().equals(sortedlist.get(i-1).getEnddate())){
					sortedlist.get(i).setStartdate(String.valueOf(Integer.parseInt(sortedlist.get(i).getStartdate())+1));
				}
			}
		}

		return sortedlist;
	}
public static Map<String, Map<String, List<OperandData>>> getData(Object o)
{
	List<Map<String, Object>> d = (List<Map<String, Object>>) o;
	//System.out.println(d);
		Map<String, Map<String, List<OperandData>>> factdata = new HashMap<>();
		
		for(Map<String, Object> m : d) {
			//System.out.println(m);
			if(factdata.containsKey(m.get("BTSCNO"))) {
				Map<String, List<OperandData>> addopr = factdata.get(m.get("BTSCNO"));
				for (Map.Entry<String, List<OperandData>> oprentry : addopr.entrySet()) {			
					add(oprentry,m)	;
				}		
			}else {
				LinkedHashMap<String, List<OperandData>> operands = loadBillOperands(m);
				if (!season.containsKey(m.get("BTSCNO").toString().trim())) {
				operands.remove("I_SEAS_STR");
				operands.remove("I_SEAS_END");
				}
				if(!remainingamt.containsKey(m.get("BTSCNO").toString().trim())) {
					operands.remove("H_TRU_MTHS");
				}
				/*
				 * 	map.put("I_SEAS_STR","F_INTE");
	map.put("I_SEAS_END","F_INTE");
	map.put("H_TRU_MTHS", "F_FACT");
				 */
				for (Map.Entry<String, List<OperandData>> oprentry : operands.entrySet()) {			
					add(oprentry,m)	;						
				}
				factdata.put(m.get("BTSCNO").toString(), operands);
			}
			
		}
		/*System.out.println(factdata.toString());*/
		return factdata;
}

public static void add(Map.Entry<String, List<OperandData>> oprentry,Map<String, Object> m ) {	
	// QUANT
	/*if(oprentry.getKey().equals("H_BLD_KWH")) {	
		oprentry.getValue().add(new OperandData(m.get("BTBLOPNDT").toString().trim(), m.get("BTBLCLSDT").toString().trim(), m.get("BTRKWH_HT").toString().trim(),"QUANT",1));
	}*/
	if(oprentry.getKey().equals("H_FPPCA2")) {	
		if(!m.get("H_FPPCA2").toString().trim().equals("0"))
		oprentry.getValue().add(new OperandData(m.get("BTBLOPNDT").toString().trim(), m.get("BTBLCLSDT").toString().trim(), m.get("H_FPPCA2").toString().trim(),"AMOUNT",1));
	}
	if(oprentry.getKey().equals("H_FPPCA_CH")) {
		if(!m.get("H_FPPCA_CH").toString().trim().equals("0"))
		oprentry.getValue().add(new OperandData(m.get("BTBLOPNDT").toString().trim(), m.get("BTBLCLSDT").toString().trim(), m.get("H_FPPCA_CH").toString().trim(),"AMOUNT",1));
	}
	if(oprentry.getKey().equals("H_BLD_KVAH")) {	
		if(!m.get("BTBKVAH").toString().trim().equals("0"))
		oprentry.getValue().add(new OperandData(m.get("BTBLOPNDT").toString().trim(), m.get("BTBLCLSDT").toString().trim(), m.get("BTBKVAH").toString().trim(),"QUANT",1));
	}
/*	if(oprentry.getKey().equals("H_KVAH_NOR")) {	
		oprentry.getValue().add(new OperandData(m.get("BTBLOPNDT").toString().trim(), m.get("BTBLCLSDT").toString().trim(), m.get("NOR_KVAH").toString().trim(),"QUANT",1));
	}*/
	if(oprentry.getKey().equals("H_KVAH_OP")) {	
		oprentry.getValue().add(new OperandData(m.get("BTBLOPNDT").toString().trim(), m.get("BTBLCLSDT").toString().trim(), m.get("TOT_OFFPEAK").toString().trim(),"QUANT",1));
	}
	if(oprentry.getKey().equals("H_KVAH_P")) {	
		oprentry.getValue().add(new OperandData(m.get("BTBLOPNDT").toString().trim(), m.get("BTBLCLSDT").toString().trim(), m.get("TOT_PEAK").toString().trim(),"QUANT",1));
	}
	if(oprentry.getKey().equals("H_MTR_KVAH")) {	
		if(!m.get("MDRECKVAH_HT").toString().trim().equals("0"))
		oprentry.getValue().add(new OperandData(m.get("BTBLOPNDT").toString().trim(), m.get("BTBLCLSDT").toString().trim(), m.get("MDRECKVAH_HT").toString().trim(),"QUANT",1));
	}
	if(oprentry.getKey().equals("H_MTR_KWH")) {	
		if(!m.get("MDRECKWH_HT").toString().trim().equals("0"))
		oprentry.getValue().add(new OperandData(m.get("BTBLOPNDT").toString().trim(), m.get("BTBLCLSDT").toString().trim(), m.get("MDRECKWH_HT").toString().trim(),"QUANT",1));
	}
	if(oprentry.getKey().equals("H_MTR_KWHE")) {	
		oprentry.getValue().add(new OperandData(m.get("BTBLOPNDT").toString().trim(), m.get("BTBLCLSDT").toString().trim(), m.get("SOLAR_KWH").toString().trim(),"QUANT",1));
	}
	if(oprentry.getKey().equals("H_MTR_T1")) {	
		if(!m.get("MDTOD1_RECKVAH").toString().trim().equals("0"))
		oprentry.getValue().add(new OperandData(m.get("BTBLOPNDT").toString().trim(), m.get("BTBLCLSDT").toString().trim(), m.get("MDTOD1_RECKVAH").toString().trim(),"QUANT",1));
	}
	if(oprentry.getKey().equals("H_MTR_T2")) {	
		if(!m.get("MDTOD2_RECKVAH").toString().trim().equals("0"))
		oprentry.getValue().add(new OperandData(m.get("BTBLOPNDT").toString().trim(), m.get("BTBLCLSDT").toString().trim(), m.get("MDTOD2_RECKVAH").toString().trim(),"QUANT",1));
	}
	if(oprentry.getKey().equals("H_MTR_T3")) {	
		if(!m.get("MDTOD3_RECKVAH").toString().trim().equals("0"))
		oprentry.getValue().add(new OperandData(m.get("BTBLOPNDT").toString().trim(), m.get("BTBLCLSDT").toString().trim(), m.get("MDTOD3_RECKVAH").toString().trim(),"QUANT",1));
	}
	if(oprentry.getKey().equals("H_MTR_T4")) {	
		if(!m.get("MDTOD4_RECKVAH").toString().trim().equals("0"))
		oprentry.getValue().add(new OperandData(m.get("BTBLOPNDT").toString().trim(), m.get("BTBLCLSDT").toString().trim(), m.get("MDTOD4_RECKVAH").toString().trim(),"QUANT",1));
	}
	if(oprentry.getKey().equals("H_MTR_T5")) {	
		if(!m.get("MDTOD5_RECKVAH").toString().trim().equals("0"))
		oprentry.getValue().add(new OperandData(m.get("BTBLOPNDT").toString().trim(), m.get("BTBLCLSDT").toString().trim(), m.get("MDTOD5_RECKVAH").toString().trim(),"QUANT",1));
	}
	if(oprentry.getKey().equals("H_MTR_T6")) {	
		if(!m.get("MDTOD6_RECKVAH").toString().trim().equals("0"))
		oprentry.getValue().add(new OperandData(m.get("BTBLOPNDT").toString().trim(), m.get("BTBLCLSDT").toString().trim(), m.get("MDTOD6_RECKVAH").toString().trim(),"QUANT",1));
	}
	/*if(oprentry.getKey().equals("Q_KVAH_ADJ")) {	
		oprentry.getValue().add(new OperandData(m.get("BTBLOPNDT").toString().trim(), m.get("BTBLCLSDT").toString().trim(), m.get("Q_KVAH_ADJ").toString().trim(),"QUANT",1));
	}
	if(oprentry.getKey().equals("Q_KWHE_ADJ")) {	
		oprentry.getValue().add(new OperandData(m.get("BTBLOPNDT").toString().trim(), m.get("BTBLCLSDT").toString().trim(), m.get("Q_KWHE_ADJ").toString().trim(),"QUANT",1));
	}
	if(oprentry.getKey().equals("Q_KWH_ADJ")) {	
		oprentry.getValue().add(new OperandData(m.get("BTBLOPNDT").toString().trim(), m.get("BTBLCLSDT").toString().trim(), m.get("Q_KWH_ADJ").toString().trim(),"QUANT",1));
	}
	if(oprentry.getKey().equals("Q_T1_ADJ")) {	
		oprentry.getValue().add(new OperandData(m.get("BTBLOPNDT").toString().trim(), m.get("BTBLCLSDT").toString().trim(), m.get("Q_T1_ADJ").toString().trim(),"QUANT",1));
	}
	if(oprentry.getKey().equals("Q_T2_ADJ")) {	
		oprentry.getValue().add(new OperandData(m.get("BTBLOPNDT").toString().trim(), m.get("BTBLCLSDT").toString().trim(), m.get("Q_T2_ADJ").toString().trim(),"QUANT",1));
	}
	if(oprentry.getKey().equals("Q_T3_ADJ")) {	
		oprentry.getValue().add(new OperandData(m.get("BTBLOPNDT").toString().trim(), m.get("BTBLCLSDT").toString().trim(), m.get("Q_T3_ADJ").toString().trim(),"QUANT",1));
	}
	if(oprentry.getKey().equals("Q_T4_ADJ")) {	
		oprentry.getValue().add(new OperandData(m.get("BTBLOPNDT").toString().trim(), m.get("BTBLCLSDT").toString().trim(), m.get("Q_T4_ADJ").toString().trim(),"QUANT",1));
	}
	if(oprentry.getKey().equals("Q_T5_ADJ")) {	
		oprentry.getValue().add(new OperandData(m.get("BTBLOPNDT").toString().trim(), m.get("BTBLCLSDT").toString().trim(), m.get("Q_T5_ADJ").toString().trim(),"QUANT",1));
	}
	if(oprentry.getKey().equals("Q_T6_ADJ")) {	
		oprentry.getValue().add(new OperandData(m.get("BTBLOPNDT").toString().trim(), m.get("BTBLCLSDT").toString().trim(), m.get("Q_T6_ADJ").toString().trim(),"QUANT",1));
	}*/
	/*if(oprentry.getKey().equals("Q_KWH_FY")) {	
		oprentry.getValue().add(new OperandData(m.get("MOVE_IN").toString().trim(), m.get("BTBLCLSDT").toString().trim(), totkwh.get(m.get("BTSCNO").toString().trim()),"QUANT",1));
	}*/
	if(oprentry.getKey().equals("H_TRU_KWH")) {	
		oprentry.getValue().add(new OperandData(m.get("MOVE_IN").toString().trim(), m.get("BTBLCLSDT").toString().trim(), m.get("H_TRU_KWH").toString().trim(),"QUANT",1));
	}
	if(oprentry.getKey().equals("H_TRU_HKWH")) {	
		oprentry.getValue().add(new OperandData(m.get("MOVE_IN").toString().trim(), m.get("BTBLCLSDT").toString().trim(), m.get("H_TRU_HKWH").toString().trim(),"QUANT",1));
	}
	if(oprentry.getKey().equals("Q_OPN_KVAH")) {	
		oprentry.getValue().add(new OperandData(m.get("BTBLOPNDT").toString().trim(), m.get("BTBLCLSDT").toString().trim(), m.get("Q_OPN_KVAH").toString().trim(),"QUANT",1));
	}
	if(oprentry.getKey().equals("Q_OPN_P")) {	
		oprentry.getValue().add(new OperandData(m.get("BTBLOPNDT").toString().trim(), m.get("BTBLCLSDT").toString().trim(), m.get("Q_OPN_P").toString().trim(),"QUANT",1));
	}
	if(oprentry.getKey().equals("Q_OPN_OP")) {	
		oprentry.getValue().add(new OperandData(m.get("BTBLOPNDT").toString().trim(), m.get("BTBLCLSDT").toString().trim(), m.get("Q_OPN_OP").toString().trim(),"QUANT",1));
	}
	if(oprentry.getKey().equals("Q_FAC_FY21")) {	
		if(!m.get("OPN_FY21").toString().trim().equals("0"))
		oprentry.getValue().add(new OperandData(m.get("OPN_FY21").toString().trim(), 
				m.get("OPN_FY21").toString().trim(),
				m.get("Q_FAC_FY21").toString().trim(),
				"QUANT",1));
	}
	if(oprentry.getKey().equals("Q_FA_HFY21")) {	
		if(!m.get("HT_OPN_FY21").toString().trim().equals("0"))
		oprentry.getValue().add(new OperandData(m.get("HT_OPN_FY21").toString().trim(), 
				m.get("HT_OPN_FY21").toString().trim(),
				m.get("Q_FA_HFY21").toString().trim(),
				"QUANT",1));
	}
	// DEMAND
	if(oprentry.getKey().equals("D_CONN_DMD")) {	
		oprentry.getValue().add(new OperandData(m.get("LEG_MOVE_IN").toString().trim(), m.get("BTBLCLSDT").toString().trim(), m.get("CTCONNLD").toString().trim(),"DEMAND",2));
	}
	if(oprentry.getKey().equals("D_OPN_MD")) {	
		if(!m.get("MDRECKVA_HT").toString().trim().equals("0"))
		oprentry.getValue().add(new OperandData(m.get("BTBLOPNDT").toString().trim(), m.get("BTBLCLSDT").toString().trim(), m.get("MDRECKVA_HT").toString().trim(),"DEMAND",2));
	}
	if(oprentry.getKey().equals("D_CONT_DMD")) {
		if(!m.get("BTCMD_HT").toString().trim().equals("0"))
		oprentry.getValue().add(new OperandData(m.get("BTBLOPNDT").toString().trim(), m.get("BTBLCLSDT").toString().trim(), m.get("BTCMD_HT").toString().trim(),"DEMAND",2));
	}
	if(oprentry.getKey().equals("D_INSTLCAP")) {	
		oprentry.getValue().add(new OperandData(m.get("LEG_MOVE_IN").toString().trim(), m.get("BTBLCLSDT").toString().trim(), m.get("INSTALLED_CAPACITY").toString().trim(),"DEMAND",2));
	}
	if(oprentry.getKey().equals("H_RMD_KVA")) {	
		if(!m.get("BTRKVA_HT").toString().trim().equals("0"))
		oprentry.getValue().add(new OperandData(m.get("BTBLOPNDT").toString().trim(), m.get("BTBLCLSDT").toString().trim(), m.get("BTRKVA_HT").toString().trim(),"DEMAND",2));
	}
	if(oprentry.getKey().equals("H_BMD_KVA")) {	
		if(!m.get("BTBLKVA_HT").toString().trim().equals("0"))
		oprentry.getValue().add(new OperandData(m.get("BTBLOPNDT").toString().trim(), m.get("BTBLCLSDT").toString().trim(), m.get("BTBLKVA_HT").toString().trim(),"DEMAND",2));
	}
	/*if(oprentry.getKey().equals("D_PPA_CAP")) {	
		oprentry.getValue().add(new OperandData(m.get("BTBLOPNDT").toString().trim(), m.get("BTBLCLSDT").toString().trim(), m.get("CONTRACT_CAPACITY").toString().trim(),"DEMAND",2));
	}*/
	if(oprentry.getKey().equals("D_RMD_ADJ")) {	
		oprentry.getValue().add(new OperandData(m.get("BTBLOPNDT").toString().trim(), m.get("BTBLCLSDT").toString().trim(), m.get("D_RMD_ADJ").toString().trim(),"DEMAND",2));
	}

	//FLAG 
	if(oprentry.getKey().equals("B_3PHASE")) {	
		oprentry.getValue().add(new OperandData(m.get("LEG_MOVE_IN").toString().trim(), m.get("BTBLCLSDT").toString().trim(), m.get("B_3PHASE").toString().trim(),"FLAG",4));
	}
	if(oprentry.getKey().equals("B_HT_MTR")) {	
		oprentry.getValue().add(new OperandData(m.get("LEG_MOVE_IN").toString().trim(), m.get("BTBLCLSDT").toString().trim(), m.get("B_HT_MTR").toString().trim(),"FLAG",4));
	}
	if(oprentry.getKey().equals("B_TOD_EXMP")) {	
		oprentry.getValue().add(new OperandData(m.get("MOVE_IN").toString().trim(), m.get("BTBLCLSDT").toString().trim(), m.get("B_TOD_EXMP").toString().trim(),"FLAG",4));
	}
	if(oprentry.getKey().equals("B_CONV_GEN")) {	
		oprentry.getValue().add(new OperandData(m.get("LEG_MOVE_IN").toString().trim(), m.get("BTBLCLSDT").toString().trim(), m.get("B_CONV_GEN").toString().trim(),"FLAG",4));
	}
	if(oprentry.getKey().equals("B_RFTP_GEN")) {	
		oprentry.getValue().add(new OperandData(m.get("LEG_MOVE_IN").toString().trim(), m.get("BTBLCLSDT").toString().trim(), m.get("B_RFTP_GEN").toString().trim(),"FLAG",4));
	}
	if(oprentry.getKey().equals("B_RNEW_GEN")) {	
		oprentry.getValue().add(new OperandData(m.get("LEG_MOVE_IN").toString().trim(), m.get("BTBLCLSDT").toString().trim(), m.get("B_RNEW_GEN").toString().trim(),"FLAG",4));
	}
	if(oprentry.getKey().equals("B_SGAR_GEN")) {	
		oprentry.getValue().add(new OperandData(m.get("LEG_MOVE_IN").toString().trim(), m.get("BTBLCLSDT").toString().trim(), m.get("B_SGAR_GEN").toString().trim(),"FLAG",4));
	}
	if(oprentry.getKey().equals("B_TEMP_CON")) {	
		oprentry.getValue().add(new OperandData(m.get("LEG_MOVE_IN").toString().trim(), m.get("BTBLCLSDT").toString().trim(), m.get("B_TEMP_CON").toString().trim(),"FLAG",4));
	}
	if(oprentry.getKey().equals("B_TRUE_UP")) {	// When service released before 2019  
		oprentry.getValue().add(new OperandData(m.get("TOD_EXM_DT").toString().trim(),"20250731", 
				!remainingamt.containsKey(m.get("BTSCNO").toString().trim())?" ":"X","FLAG",4));// Need to change the Months for true up
	}
	if(oprentry.getKey().equals("B_ED_EXMPT")) {	
		oprentry.getValue().add(new OperandData(m.get("LEG_MOVE_IN").toString().trim(), m.get("BTBLCLSDT").toString().trim(), m.get("B_ED_EXMPT").toString().trim(),"FLAG",4));
	}
	if(oprentry.getKey().equals("B_IND_FEED")) {	
		oprentry.getValue().add(new OperandData(m.get("LEG_MOVE_IN").toString().trim(), m.get("BTBLCLSDT").toString().trim(), m.get("B_IND_FEED").toString().trim(),"FLAG",4));
	}
	if(oprentry.getKey().equals("B_SP_GAS")) {	
		oprentry.getValue().add(new OperandData(m.get("LEG_MOVE_IN").toString().trim(), m.get("BTBLCLSDT").toString().trim(), m.get("B_SP_GAS").toString().trim(),"FLAG",4));
	}
	if(oprentry.getKey().equals("B_SP_HYDEL")) {	
		oprentry.getValue().add(new OperandData(m.get("LEG_MOVE_IN").toString().trim(), m.get("BTBLCLSDT").toString().trim(), m.get("B_SP_HYDEL").toString().trim(),"FLAG",4));
	}
	if(oprentry.getKey().equals("B_SP_NCE")) {	
		oprentry.getValue().add(new OperandData(m.get("LEG_MOVE_IN").toString().trim(), m.get("BTBLCLSDT").toString().trim(), m.get("B_SP_NCE").toString().trim(),"FLAG",4));
	}
	if(oprentry.getKey().equals("B_SP_THRML")) {	
		oprentry.getValue().add(new OperandData(m.get("LEG_MOVE_IN").toString().trim(), m.get("BTBLCLSDT").toString().trim(), m.get("B_SP_THRML").toString().trim(),"FLAG",4));
	}
	if(oprentry.getKey().equals("B_SP_W&S")) {	
		oprentry.getValue().add(new OperandData(m.get("LEG_MOVE_IN").toString().trim(), m.get("BTBLCLSDT").toString().trim(), m.get("B_SP_WS").toString().trim(),"FLAG",4));
	}
	/*if(oprentry.getKey().equals("B_MR_ESTM")) {	
		oprentry.getValue().add(new OperandData(m.get("BTBLOPNDT").toString().trim(), m.get("BTBLCLSDT").toString().trim(), m.get("B_MR_ESTM").toString().trim(),"FLAG",4));
	}*/
/*	if(oprentry.getKey().equals("B_SHUTDOWN")) {	
		oprentry.getValue().add(new OperandData(m.get("BTBLOPNDT").toString().trim(), m.get("BTBLCLSDT").toString().trim(), m.get("B_SHUTDOWN").toString().trim(),"FLAG",4));
	}*/
	/*if(oprentry.getKey().equals("B_OPN_ACSS")) {	
		oprentry.getValue().add(new OperandData(m.get("LEG_MOVE_IN").toString().trim(), m.get("BTBLCLSDT").toString().trim(), m.get("B_OPN_ACSS").toString().trim(),"FLAG",4));
	}*/
/*	if(oprentry.getKey().equals("B_COL_SEG")) {	
		oprentry.getValue().add(new OperandData(m.get("LEG_MOVE_IN").toString().trim(), m.get("BTBLCLSDT").toString().trim(), m.get("B_COL_SEG").toString().trim(),"FLAG",4));
	}*/
	if(oprentry.getKey().equals("B_GR_EXMPT")) {	
		oprentry.getValue().add(new OperandData(m.get("LEG_MOVE_IN").toString().trim(), m.get("BTBLCLSDT").toString().trim(), m.get("B_GR_EXMPT").toString().trim(),"FLAG",4));
	}
	// AMOUNT
	
	if(oprentry.getKey().equals("H_CC_CHRG")) {	
		oprentry.getValue().add(new OperandData(m.get("BTBLOPNDT").toString().trim(), m.get("BTBLCLSDT").toString().trim(), m.get("BTCUSTCHG").toString().trim(),"AMOUNT",5));
	}
	if(oprentry.getKey().equals("H_ED_CHRG")) {	
		oprentry.getValue().add(new OperandData(m.get("BTBLOPNDT").toString().trim(), m.get("BTBLCLSDT").toString().trim(), m.get("BTED").toString().trim(),"AMOUNT",5));
	}
	if(oprentry.getKey().equals("H_ENG_CHRG")) {	
		oprentry.getValue().add(new OperandData(m.get("BTBLOPNDT").toString().trim(), m.get("BTBLCLSDT").toString().trim(), m.get("BTENGCHG_NOR").toString().trim(),"AMOUNT",5));
	}
	if(oprentry.getKey().equals("H_GRID_SUP")) {	
		oprentry.getValue().add(new OperandData(m.get("BTBLOPNDT").toString().trim(), m.get("BTBLCLSDT").toString().trim(), m.get("BT_GS_CHG").toString().trim(),"AMOUNT",5));
	}
	if(oprentry.getKey().equals("H_TOT_BILL")) {	
		oprentry.getValue().add(new OperandData(m.get("BTBLOPNDT").toString().trim(), m.get("BTBLCLSDT").toString().trim(), m.get("TOT_BILL").toString().trim(),"AMOUNT",5));
	}
	if(oprentry.getKey().equals("H_TRU_CHRG")) {	
		oprentry.getValue().add(new OperandData(m.get("BTBLOPNDT").toString().trim(), m.get("BTBLCLSDT").toString().trim(), m.get("TRU_CHR").toString().trim(),"AMOUNT",5));
	}
	if(oprentry.getKey().equals("H_TRU_HCHR")) {	
		oprentry.getValue().add(new OperandData(m.get("BTBLOPNDT").toString().trim(), m.get("BTBLCLSDT").toString().trim(), m.get("H_TRU_CHR").toString().trim(),"AMOUNT",5));
	}
	if(oprentry.getKey().equals("H_TOT_SUB")) {	
		oprentry.getValue().add(new OperandData(m.get("BTBLOPNDT").toString().trim(), m.get("BTBLCLSDT").toString().trim(), m.get("TOT_SUB").toString().trim(),"AMOUNT",5));
	}
	if(oprentry.getKey().equals("H_TOT_INV")) {	
		oprentry.getValue().add(new OperandData(m.get("BTBLOPNDT").toString().trim(), m.get("BTBLCLSDT").toString().trim(), m.get("GRAND_TOT").toString().trim(),"AMOUNT",5));
	}
	if(oprentry.getKey().equals("H_DMD_CHRG")) {	
		oprentry.getValue().add(new OperandData(m.get("BTBLOPNDT").toString().trim(), m.get("BTBLCLSDT").toString().trim(), m.get("BTDEMCHG_NOR").toString().trim(),"AMOUNT",5));
	}
	if(oprentry.getKey().equals("H_DMD_PEN")) {	
		oprentry.getValue().add(new OperandData(m.get("BTBLOPNDT").toString().trim(), m.get("BTBLCLSDT").toString().trim(), m.get("BTDEMCHG_PEN").toString().trim(),"AMOUNT",5));
	}
	if(oprentry.getKey().equals("H_ENG_NORM")) {	
		oprentry.getValue().add(new OperandData(m.get("BTBLOPNDT").toString().trim(), m.get("BTBLCLSDT").toString().trim(), m.get("BTENGCHG_NORM").toString().trim(),"AMOUNT",5));
	}
	if(oprentry.getKey().equals("H_ENG_OP")) {	
		oprentry.getValue().add(new OperandData(m.get("BTBLOPNDT").toString().trim(), m.get("BTBLCLSDT").toString().trim(), m.get("ENG_OP").toString().trim(),"AMOUNT",5));
	}
	if(oprentry.getKey().equals("H_ENG_PEAK")) {	
		oprentry.getValue().add(new OperandData(m.get("BTBLOPNDT").toString().trim(), m.get("BTBLCLSDT").toString().trim(), m.get("ENG_PEAK").toString().trim(),"AMOUNT",5));
	}
	if(oprentry.getKey().equals("H_ENG_PEN")) {	
		oprentry.getValue().add(new OperandData(m.get("BTBLOPNDT").toString().trim(), m.get("BTBLCLSDT").toString().trim(), m.get("BTENGCHG_PEN").toString().trim(),"AMOUNT",5));
	}
	if(oprentry.getKey().equals("H_VOL_SCHR")) {	
		oprentry.getValue().add(new OperandData(m.get("BTBLOPNDT").toString().trim(), m.get("BTBLCLSDT").toString().trim(), m.get("BTVOLTSURCHG").toString().trim(),"AMOUNT",5));
	}
	if(oprentry.getKey().equals("H_TRU_MO")) {	
		oprentry.getValue().add(new OperandData(m.get("MOVE_IN").toString().trim(), m.get("BTBLCLSDT").toString().trim(),getRemaingAmt(m.get("BTSCNO").toString().trim()),"AMOUNT",5));
	}
	if(oprentry.getKey().equals("H_WHEL_CHR")) {	
		oprentry.getValue().add(new OperandData(m.get("BTBLOPNDT").toString().trim(), m.get("BTBLCLSDT").toString().trim(), m.get("BTWHEELCHGCASH_HT").toString().trim(),"AMOUNT",5));
	}
	if(oprentry.getKey().equals("H_CSS_CHR")) {	
		oprentry.getValue().add(new OperandData(m.get("BTBLOPNDT").toString().trim(), m.get("BTBLCLSDT").toString().trim(), m.get("BTCROSSSUBCHG").toString().trim(),"AMOUNT",5));
	}
	
	if (season.containsKey(m.get("BTSCNO").toString().trim())) {
		System.out.println(season.get(m.get("BTSCNO").toString().trim()));
				String p[] = getMonthPeriod(season.get(m.get("BTSCNO").toString().trim())).split("-");
				if (oprentry.getKey().equals("I_SEAS_STR")) {					
					oprentry.getValue().add(new OperandData("20220401", "99991231", p[0], "INTE", 6));					
				}
				if (oprentry.getKey().equals("I_SEAS_END")) {				
					oprentry.getValue().add(new OperandData("20220401", "99991231", p[1], "INTE", 6));					
				}
		
			
	}
	if(remainingamt.containsKey(m.get("BTSCNO").toString().trim())) {
		//System.out.println(oprentry.getKey());
		if (oprentry.getKey().equals("H_TRU_MTHS")) {
			oprentry.getValue().add(new OperandData(m.get("MOVE_IN").toString().trim(), "99991231", "24", "FACT", 3));// NEED TO CHANGE 
		}
	}

	
/*	List<OperandData> list = e.getValue();
	if(season.containsKey((d.getKey()))){
		getMonthPeriod(season.get(d.getKey()));
		list.add(new OperandData("20220401", "99993112", "0", "FACT", 3));
	}
	List<OperandData> list = e.getValue();
	list.sort(Comparator.comparingInt(OperandData::getOrder));*/
	
	
}
public static LinkedHashMap<String, List<OperandData>> loadBillOperands(Map<String, Object> m) {

	LinkedHashMap<String, List<OperandData>> map = new LinkedHashMap<>();
	//QUAN
	/*map.put("H_BLD_KWH",new ArrayList<OperandData>());*/
	if(!m.get("BTBKVAH").toString().trim().equals("0"))
	map.put("H_BLD_KVAH",new ArrayList<OperandData>());
	
	
	if((m.get("CTCAT").toString().equals("HT3") && m.get("CTSUBCAT").toString().equals("A"))) {
	/*map.put("H_KVAH_NOR",new ArrayList<OperandData>());*/ 
	if(!Optional.ofNullable(m.get("TOT_OFFPEAK")).orElse("").toString().trim().equals("0"))
	map.put("H_KVAH_OP",new ArrayList<OperandData>());
	if(!Optional.ofNullable(m.get("TOT_PEAK")).orElse("").toString().trim().equals("0"))
	map.put("H_KVAH_P",new ArrayList<OperandData>());
	}
	if(!Optional.ofNullable(m.get("MDRECKVAH_HT")).orElse("").toString().trim().equals("0"))
	map.put("H_MTR_KVAH",new ArrayList<OperandData>());
	if(!Optional.ofNullable(m.get("MDRECKWH_HT")).orElse("").toString().trim().equals("0"))
	map.put("H_MTR_KWH",new ArrayList<OperandData>());
	if(m.get("CTSOLAR_FLAG").toString().equals("Y"))
	map.put("H_MTR_KWHE",new ArrayList<OperandData>());
	if(m.get("CTCAT").toString().equals("HT3") && m.get("CTSUBCAT").toString().equals("A")) {
	if(!Optional.ofNullable(m.get("MDTOD1_RECKVAH")).orElse("").toString().trim().equals("0"))
	map.put("H_MTR_T1",new ArrayList<OperandData>());
		if(!Optional.ofNullable(m.get("MDTOD2_RECKVAH")).orElse("").toString().trim().equals("0"))
	map.put("H_MTR_T2",new ArrayList<OperandData>());
		if(!Optional.ofNullable(m.get("MDTOD3_RECKVAH")).orElse("").toString().trim().equals("0"))
	map.put("H_MTR_T3",new ArrayList<OperandData>());
		if(!Optional.ofNullable(m.get("MDTOD4_RECKVAH")).orElse("").toString().trim().equals("0"))
	map.put("H_MTR_T4",new ArrayList<OperandData>());
	if(!Optional.ofNullable(m.get("MDTOD5_RECKVAH")).orElse("").toString().trim().equals("0"))
	map.put("H_MTR_T5",new ArrayList<OperandData>());
	if(!Optional.ofNullable(m.get("MDTOD6_RECKVAH")).orElse("").toString().trim().equals("0"))
	map.put("H_MTR_T6",new ArrayList<OperandData>());
	}else if(m.get("CTCAT").toString().equals("HT2") && m.get("CTSUBCAT").toString().equals("A1")){
		if(!Optional.ofNullable(m.get("MDTOD5_RECKVAH")).orElse("").toString().trim().equals("0"))
		map.put("H_MTR_T5",new ArrayList<OperandData>());	
	}
	if(!m.get("H_TRU_KWH").toString().equals("0")) {
	map.put("H_TRU_KWH",new ArrayList<OperandData>());
	}
	if(!m.get("H_TRU_HKWH").toString().equals("0")) {
		map.put("H_TRU_HKWH",new ArrayList<OperandData>());
		}
	/*try {
	if(!totkwh.get(m.get("BTSCNO").toString()).equals("0")) {
		map.put("Q_KWH_FY",new ArrayList<OperandData>());
		}
	}catch(Exception e) {
		
	}*/
	if(!m.get("Q_OPN_KVAH").toString().equals("0")) {
		map.put("Q_OPN_KVAH",new ArrayList<OperandData>());
		}
	if(!m.get("Q_OPN_P").toString().equals("0")) {
		map.put("Q_OPN_P",new ArrayList<OperandData>());
		}
	if(!m.get("Q_OPN_OP").toString().equals("0")) {
		map.put("Q_OPN_OP",new ArrayList<OperandData>());
		}
	if(fy21flag.containsKey(m.get("BTSCNO").toString().trim()))
	map.put("Q_FAC_FY21",new ArrayList<OperandData>());
	
	if(htfy21flag.containsKey(m.get("BTSCNO").toString().trim()))
		map.put("Q_FA_HFY21",new ArrayList<OperandData>());
	
	/*map.put("Q_KVAH_ADJ",new ArrayList<OperandData>());
	map.put("Q_KWHE_ADJ",new ArrayList<OperandData>());
	map.put("Q_KWH_ADJ",new ArrayList<OperandData>());
	map.put("Q_T1_ADJ",new ArrayList<OperandData>());
	map.put("Q_T2_ADJ",new ArrayList<OperandData>());
	map.put("Q_T3_ADJ",new ArrayList<OperandData>());
	map.put("Q_T4_ADJ",new ArrayList<OperandData>());
	map.put("Q_T5_ADJ",new ArrayList<OperandData>());
	map.put("Q_T6_ADJ",new ArrayList<OperandData>());*/
	
	//DEMAND
	map.put("D_CONN_DMD",new ArrayList<OperandData>());
	if(!Optional.ofNullable(m.get("MDRECKVA_HT")).orElse("").toString().trim().equals("0"))
    map.put("D_OPN_MD",new ArrayList<OperandData>());
	if(!m.get("BTCMD_HT").toString().trim().equals("0"))
	map.put("D_CONT_DMD",new ArrayList<OperandData>());
	if(!m.get("INSTALLED_CAPACITY").toString().trim().equals("0")){
	map.put("D_INSTLCAP",new ArrayList<OperandData>());
	}
	if(!m.get("BTRKVA_HT").toString().trim().equals("0"))
	map.put("H_RMD_KVA",new ArrayList<OperandData>());
	if(!m.get("BTBLKVA_HT").toString().trim().equals("0"))
	map.put("H_BMD_KVA",new ArrayList<OperandData>());
	/*if(!m.get("CONTRACT_CAPACITY").toString().trim().equals("0")){
	map.put("D_PPA_CAP",new ArrayList<OperandData>());
	}*/
/*	map.put("D_RMD_ADJ",new ArrayList<OperandData>());*/
	
	//FACT
	map.put("H_TRU_MTHS",new ArrayList<OperandData>());
	// B_3PHASE, B_CONV_GEN, B_RFTP_GEN, B_RNEW_GEN, B_SGAR_GEN, B_TEMP_CON, B_TRUE_UP, B_ED_EXMPT, B_IND_FEED, B_SP_GAS, B_SP_HYDEL, B_SP_NCE, B_SP_THRML, B_SP_WS, B_MR_ESTM
	
	// FLAG
		if (m.get("B_3PHASE").toString().trim().equals("X")) {
			map.put("B_3PHASE", new ArrayList<OperandData>());
		}
		if (m.get("B_HT_MTR").toString().trim().equals("X")) {
			map.put("B_HT_MTR", new ArrayList<OperandData>());
		}
		if (m.get("B_TOD_EXMP").toString().trim().equals("X")) {
			map.put("B_TOD_EXMP", new ArrayList<OperandData>());
		}
		if (m.get("B_CONV_GEN").toString().trim().equals("X")) {
			map.put("B_CONV_GEN", new ArrayList<OperandData>());
		}
		if (m.get("B_RFTP_GEN").toString().trim().equals("X")) {
			map.put("B_RFTP_GEN", new ArrayList<OperandData>());
		}
		if (m.get("B_RNEW_GEN").toString().trim().equals("X")) {
			map.put("B_RNEW_GEN", new ArrayList<OperandData>());
		}
		if (m.get("B_SGAR_GEN").toString().trim().equals("X")) {
			map.put("B_SGAR_GEN", new ArrayList<OperandData>());
		}
		if (m.get("B_TEMP_CON").toString().trim().equals("X")) {
			map.put("B_TEMP_CON", new ArrayList<OperandData>());
		}
		if (remainingamt.containsKey(m.get("BTSCNO").toString().trim())) {
			map.put("B_TRUE_UP", new ArrayList<OperandData>());
		}
		if (m.get("B_ED_EXMPT").toString().trim().equals("X")) {
			map.put("B_ED_EXMPT", new ArrayList<OperandData>());
		}
		if (m.get("B_IND_FEED").toString().trim().equals("X")) {
			map.put("B_IND_FEED", new ArrayList<OperandData>());
		}
		if (m.get("B_SP_GAS").toString().trim().equals("X")) {
			map.put("B_SP_GAS", new ArrayList<OperandData>());
		}
		if (m.get("B_SP_HYDEL").toString().trim().equals("X")) {
			map.put("B_SP_HYDEL", new ArrayList<OperandData>());
		}
		if (m.get("B_SP_NCE").toString().trim().equals("X")) {
			map.put("B_SP_NCE", new ArrayList<OperandData>());
		}
		if (m.get("B_SP_THRML").toString().trim().equals("X")) {
			map.put("B_SP_THRML", new ArrayList<OperandData>());
		}
		if (m.get("B_SP_WS").toString().trim().equals("X")) {
			map.put("B_SP_W&S", new ArrayList<OperandData>());
		}
		/*if (m.get("B_MR_ESTM").toString().trim().equals("X")) {
			map.put("B_MR_ESTM", new ArrayList<OperandData>());
		}*/
	/*	if (m.get("B_SHUTDOWN").toString().trim().equals("X")) {
		map.put("B_SHUTDOWN", new ArrayList<OperandData>());
		}*/
		/*if (m.get("B_OPN_ACSS").toString().trim().equals("X")) {
			map.put("B_OPN_ACSS", new ArrayList<OperandData>());
			}*/
		/*if (m.get("B_COL_SEG").toString().trim().equals("X")) {
			map.put("B_COL_SEG", new ArrayList<OperandData>());
			}*/
		if (m.get("B_GR_EXMPT").toString().trim().equals("X")) {
			map.put("B_GR_EXMPT", new ArrayList<OperandData>());
		}
		
	//AMOUNT
	if (!m.get("BTCUSTCHG").toString().trim().equals("0")) {	
	map.put("H_CC_CHRG",new ArrayList<OperandData>());}
	
	if (!m.get("BTED").toString().trim().equals("0")) {	
	map.put("H_ED_CHRG",new ArrayList<OperandData>());}
	
	if (!m.get("BTENGCHG_NOR").toString().trim().equals("0")) {	
		/*if (m.get("BTENGCHG_NORM").toString().trim().equals("0")) {*/
	map.put("H_ENG_CHRG",new ArrayList<OperandData>());
		/*}*/
		}
	
	if (!m.get("BT_GS_CHG").toString().trim().equals("0")) {	
	map.put("H_GRID_SUP",new ArrayList<OperandData>());}
	
	if (!m.get("TOT_BILL").toString().trim().equals("0")) {	
	map.put("H_TOT_BILL",new ArrayList<OperandData>());}
	
	if(remainingamt.containsKey(m.get("BTSCNO").toString().trim())) {
	map.put("H_TRU_CHRG",new ArrayList<OperandData>());
	map.put("H_TRU_HCHR",new ArrayList<OperandData>());}
	
	if (!m.get("TOT_SUB").toString().trim().equals("0")) {
	map.put("H_TOT_SUB",new ArrayList<OperandData>());}
	
	if (!m.get("GRAND_TOT").toString().trim().equals("0")) {
	map.put("H_TOT_INV",new ArrayList<OperandData>());}
	
	if (!m.get("BTDEMCHG_NOR").toString().trim().equals("0")) {
	map.put("H_DMD_CHRG",new ArrayList<OperandData>());}
	
	if (!m.get("BTDEMCHG_PEN").toString().trim().equals("0")) {
	map.put("H_DMD_PEN",new ArrayList<OperandData>());}
	
		if ((m.get("CTCAT").toString().equals("HT3") && m.get("CTSUBCAT").toString().equals("A"))) {
			if (!m.get("BTENGCHG_NORM").toString().trim().equals("0")) {
				map.put("H_ENG_NORM", new ArrayList<OperandData>());
			}
		}
	
	if (!m.get("ENG_OP").toString().trim().equals("0")) {
	map.put("H_ENG_OP",new ArrayList<OperandData>());}
	
	if (!m.get("ENG_PEAK").toString().trim().equals("0")) {
	map.put("H_ENG_PEAK",new ArrayList<OperandData>());}
	
	if (!m.get("BTENGCHG_PEN").toString().trim().equals("0")) {
	map.put("H_ENG_PEN",new ArrayList<OperandData>());}
	
	if (!m.get("BTVOLTSURCHG").toString().trim().equals("0")) {
	map.put("H_VOL_SCHR",new ArrayList<OperandData>());}
	
	if(!getRemaingAmt(m.get("BTSCNO").toString().trim()).equals("0")) {
	map.put("H_TRU_MO",new ArrayList<OperandData>());}
	
	if(wheellist.containsKey(m.get("BTSCNO").toString().trim()))
	map.put("H_WHEL_CHR",new ArrayList<OperandData>());
	
	/*if (!m.get("BTCROSSSUBCHG").toString().trim().equals("0")) {*/
	if(crosslist.containsKey(m.get("BTSCNO").toString().trim()))
	map.put("H_CSS_CHR",new ArrayList<OperandData>());
	
	if(fppca2.containsKey(m.get("BTSCNO").toString().trim()))
	map.put("H_FPPCA2",new ArrayList<OperandData>());
	
	if(fppcach.containsKey(m.get("BTSCNO").toString().trim()))
	map.put("H_FPPCA_CH",new ArrayList<OperandData>());
	
	//INIT
	map.put("I_SEAS_STR",new ArrayList<OperandData>());
	map.put("I_SEAS_END",new ArrayList<OperandData>());



	

	return map;
}

/*public static LinkedHashMap<String, List<OperandData>> loadBillOperands(Map<String, Object> m) {

	LinkedHashMap<String, List<OperandData>> map = new LinkedHashMap<>();
	//QUAN
	if(!m.get("BTRKWH_HT").toString().trim().equals("0"))
	map.put("H_BLD_KWH",new ArrayList<OperandData>());
	
	if(!m.get("BTBKVAH").toString().trim().equals("0"))
	map.put("H_BLD_KVAH",new ArrayList<OperandData>());
	
	if((m.get("CTCAT").toString().equals("HT3") && m.get("CTSUBCAT").toString().equals("A"))) {
	if(!m.get("NOR_KVAH").toString().trim().equals("0"))
	map.put("H_KVAH_NOR",new ArrayList<OperandData>());
	
	if(!m.get("TOT_OFFPEAK").toString().trim().equals("0"))
	map.put("H_KVAH_OP",new ArrayList<OperandData>());
	
	if(!m.get("TOT_PEAK").toString().trim().equals("0"))
	map.put("H_KVAH_P",new ArrayList<OperandData>());
	}
	
	if(!m.get("MDRKVAH_HT").toString().trim().equals("0"))
	map.put("H_MTR_KVAH",new ArrayList<OperandData>());
	
	if(!m.get("MDRKWH_HT").toString().trim().equals("0"))
	map.put("H_MTR_KWH",new ArrayList<OperandData>());
	
	if(m.get("CTSOLAR_FLAG").toString().equals("Y"))
	map.put("H_MTR_KWHE",new ArrayList<OperandData>());
	
	if(m.get("CTCAT").toString().equals("HT3") && m.get("CTSUBCAT").toString().equals("A")) {
	if(m.get("MDTOD1_RECKVAH").toString().equals("Y"))
	map.put("H_MTR_T1",new ArrayList<OperandData>());
	if(m.get("MDTOD2_RECKVAH").toString().equals("Y"))
	map.put("H_MTR_T2",new ArrayList<OperandData>());
	if(m.get("MDTOD3_RECKVAH").toString().equals("Y"))
	map.put("H_MTR_T3",new ArrayList<OperandData>());
	if(m.get("MDTOD4_RECKVAH").toString().equals("Y"))
	map.put("H_MTR_T4",new ArrayList<OperandData>());
	if(m.get("MDTOD5_RECKVAH").toString().equals("Y"))
	map.put("H_MTR_T5",new ArrayList<OperandData>());
	if(m.get("MDTOD6_RECKVAH").toString().equals("Y"))
	map.put("H_MTR_T6",new ArrayList<OperandData>());
	}else if(m.get("CTCAT").toString().equals("HT2") && m.get("CTSUBCAT").toString().equals("A1")){
		if(m.get("MDTOD5_RECKVAH").toString().equals("Y"))
		map.put("H_MTR_T5",new ArrayList<OperandData>());	
	}
	if(!m.get("H_TRU_KWH").toString().equals("0")) {
	map.put("H_TRU_KWH",new ArrayList<OperandData>());
	}
	if(!m.get("H_TRU_HKWH").toString().equals("0")) {
		map.put("H_TRU_HKWH",new ArrayList<OperandData>());
		}
	try {
	if(!totkwh.get(m.get("BTSCNO").toString()).equals("0")) {
		map.put("Q_KWH_FY",new ArrayList<OperandData>());
		}
	}catch(Exception e) {
		
	}
	if(!m.get("Q_OPN_KVAH").toString().equals("0")) {
		map.put("Q_OPN_KVAH",new ArrayList<OperandData>());
		}
	if(!m.get("Q_OPN_P").toString().equals("0")) {
		map.put("Q_OPN_P",new ArrayList<OperandData>());
		}
	if(!m.get("Q_OPN_OP").toString().equals("0")) {
		map.put("Q_OPN_OP",new ArrayList<OperandData>());
		}
	
	
	map.put("Q_KVAH_ADJ",new ArrayList<OperandData>());
	map.put("Q_KWHE_ADJ",new ArrayList<OperandData>());
	map.put("Q_KWH_ADJ",new ArrayList<OperandData>());
	map.put("Q_T1_ADJ",new ArrayList<OperandData>());
	map.put("Q_T2_ADJ",new ArrayList<OperandData>());
	map.put("Q_T3_ADJ",new ArrayList<OperandData>());
	map.put("Q_T4_ADJ",new ArrayList<OperandData>());
	map.put("Q_T5_ADJ",new ArrayList<OperandData>());
	map.put("Q_T6_ADJ",new ArrayList<OperandData>());
	
	//DEMAND
	
	if(!m.get("CTCONNLD").toString().trim().equals("0"))
	map.put("D_CONN_DMD",new ArrayList<OperandData>());
	
	if(!m.get("MDRECKVA_HT").toString().trim().equals("0"))
		map.put("D_OPN_MD",new ArrayList<OperandData>());
	
	if(!m.get("BTCMD_HT").toString().trim().equals("0"))
	map.put("D_CONT_DMD",new ArrayList<OperandData>());
	
	if(!m.get("INSTALLED_CAPACITY").toString().trim().equals("0")){
	map.put("D_INSTLCAP",new ArrayList<OperandData>());
	}
	
	if(m.get("BTRKVA_HT").toString().equals("Y"))
	map.put("H_RMD_KVA",new ArrayList<OperandData>());
	
	if(m.get("BTBLKVA_HT").toString().equals("Y"))
	map.put("H_BMD_KVA",new ArrayList<OperandData>());
	
	if(!m.get("CONTRACT_CAPACITY").toString().trim().equals("0")){
	map.put("D_PPA_CAP",new ArrayList<OperandData>());
	}
	map.put("D_RMD_ADJ",new ArrayList<OperandData>());
	
	//FACT
	map.put("H_TRU_MTHS",new ArrayList<OperandData>());
	// B_3PHASE, B_CONV_GEN, B_RFTP_GEN, B_RNEW_GEN, B_SGAR_GEN, B_TEMP_CON, B_TRUE_UP, B_ED_EXMPT, B_IND_FEED, B_SP_GAS, B_SP_HYDEL, B_SP_NCE, B_SP_THRML, B_SP_WS, B_MR_ESTM
	
	// FLAG
		if (m.get("B_3PHASE").toString().trim().equals("X")) {
			map.put("B_3PHASE", new ArrayList<OperandData>());
		}
		if (m.get("B_CONV_GEN").toString().trim().equals("X")) {
			map.put("B_CONV_GEN", new ArrayList<OperandData>());
		}
		if (m.get("B_RFTP_GEN").toString().trim().equals("X")) {
			map.put("B_RFTP_GEN", new ArrayList<OperandData>());
		}
		if (m.get("B_RNEW_GEN").toString().trim().equals("X")) {
			map.put("B_RNEW_GEN", new ArrayList<OperandData>());
		}
		if (m.get("B_SGAR_GEN").toString().trim().equals("X")) {
			map.put("B_SGAR_GEN", new ArrayList<OperandData>());
		}
		if (m.get("B_TEMP_CON").toString().trim().equals("X")) {
			map.put("B_TEMP_CON", new ArrayList<OperandData>());
		}
		if (m.get("B_TRUE_UP").toString().trim().equals("X")) {
			map.put("B_TRUE_UP", new ArrayList<OperandData>());
		}
		if (m.get("B_ED_EXMPT").toString().trim().equals("X")) {
			map.put("B_ED_EXMPT", new ArrayList<OperandData>());
		}
		if (m.get("B_IND_FEED").toString().trim().equals("X")) {
			map.put("B_IND_FEED", new ArrayList<OperandData>());
		}
		if (m.get("B_SP_GAS").toString().trim().equals("X")) {
			map.put("B_SP_GAS", new ArrayList<OperandData>());
		}
		if (m.get("B_SP_HYDEL").toString().trim().equals("X")) {
			map.put("B_SP_HYDEL", new ArrayList<OperandData>());
		}
		if (m.get("B_SP_NCE").toString().trim().equals("X")) {
			map.put("B_SP_NCE", new ArrayList<OperandData>());
		}
		if (m.get("B_SP_THRML").toString().trim().equals("X")) {
			map.put("B_SP_THRML", new ArrayList<OperandData>());
		}
		if (m.get("B_SP_WS").toString().trim().equals("X")) {
			map.put("B_SP_W&S", new ArrayList<OperandData>());
		}
		if (m.get("B_MR_ESTM").toString().trim().equals("X")) {
			map.put("B_MR_ESTM", new ArrayList<OperandData>());
		}
		if (m.get("B_SHUTDOWN").toString().trim().equals("X")) {
		map.put("B_SHUTDOWN", new ArrayList<OperandData>());
		}
		if (m.get("B_OPN_ACSS").toString().trim().equals("X")) {
			map.put("B_OPN_ACSS", new ArrayList<OperandData>());
			}
	//AMOUNT
	if (!m.get("BTCUSTCHG").toString().trim().equals("0")) {	
	map.put("H_CC_CHRG",new ArrayList<OperandData>());}
	
	if (!m.get("BTED").toString().trim().equals("0")) {	
	map.put("H_ED_CHRG",new ArrayList<OperandData>());}
	
	if (!m.get("BTENGCHG_NOR").toString().trim().equals("0")) {	
		if (m.get("BTENGCHG_NORM").toString().trim().equals("0")) {
	map.put("H_ENG_CHRG",new ArrayList<OperandData>());
		}}
	
	if (!m.get("BT_GS_CHG").toString().trim().equals("0")) {	
	map.put("H_GRID_SUP",new ArrayList<OperandData>());}
	
	if (!m.get("TOT_BILL").toString().trim().equals("0")) {	
	map.put("H_TOT_BILL",new ArrayList<OperandData>());}
	//System.out.println(m.get("TRU_CHR").toString().trim());
	if (!m.get("TRU_CHR").toString().trim().equals("0")) {	
	map.put("H_TRU_CHRG",new ArrayList<OperandData>());}
	
	if (!m.get("H_TRU_CHR").toString().trim().equals("0")) {	
		map.put("H_TRU_HCHR",new ArrayList<OperandData>());}
	
	if (!m.get("TOT_SUB").toString().trim().equals("0")) {
	map.put("H_TOT_SUB",new ArrayList<OperandData>());}
	
	if (!m.get("GRAND_TOT").toString().trim().equals("0")) {
	map.put("H_TOT_INV",new ArrayList<OperandData>());}
	
	if (!m.get("BTDEMCHG_NOR").toString().trim().equals("0")) {
	map.put("H_DMD_CHRG",new ArrayList<OperandData>());}
	
	if (!m.get("BTDEMCHG_PEN").toString().trim().equals("0")) {
	map.put("H_DMD_PEN",new ArrayList<OperandData>());}
	
		if ((m.get("CTCAT").toString().equals("HT3") && m.get("CTSUBCAT").toString().equals("A"))) {
			if (!m.get("BTENGCHG_NORM").toString().trim().equals("0")) {
				map.put("H_ENG_NORM", new ArrayList<OperandData>());
			}
		}
	
	if (!m.get("ENG_OP").toString().trim().equals("0")) {
	map.put("H_ENG_OP",new ArrayList<OperandData>());}
	
	if (!m.get("ENG_PEAK").toString().trim().equals("0")) {
	map.put("H_ENG_PEAK",new ArrayList<OperandData>());}
	
	if (!m.get("BTENGCHG_PEN").toString().trim().equals("0")) {
	map.put("H_ENG_PEN",new ArrayList<OperandData>());}
	
	if (!m.get("BTVOLTSURCHG").toString().trim().equals("0")) {
	map.put("H_VOL_SCHR",new ArrayList<OperandData>());}
	
	if(!getRemaingAmt(m.get("BTSCNO").toString().trim()).equals("0")) {
	map.put("H_TRU_MO",new ArrayList<OperandData>());}
	
	if (!m.get("BTWHEELCHGCASH_HT").toString().trim().equals("0")) {
	map.put("H_WHEL_CHR",new ArrayList<OperandData>());}
	
	if (!m.get("BTCROSSSUBCHG").toString().trim().equals("0")) {
	map.put("H_CSS_CHR",new ArrayList<OperandData>());}
	
	
	//INIT
	map.put("I_SEAS_STR",new ArrayList<OperandData>());
	map.put("I_SEAS_END",new ArrayList<OperandData>());



	

	return map;
}
*/

public static String getStructure(String key) {
	Map<String, String> map = new HashMap<>();
/*	map.put("H_BLD_KWH","F_QUAN");*/
	map.put("H_BLD_KVAH","F_QUAN");
	/*map.put("H_KVAH_NOR","F_QUAN");*/
	map.put("H_KVAH_OP","F_QUAN");
	map.put("H_KVAH_P","F_QUAN");
	map.put("H_MTR_KVAH","F_QUAN");
	map.put("H_MTR_KWH","F_QUAN");
	map.put("H_MTR_KWHE","F_QUAN");
	map.put("H_MTR_T1","F_QUAN");
	map.put("H_MTR_T2","F_QUAN");
	map.put("H_MTR_T3","F_QUAN");
	map.put("H_MTR_T4","F_QUAN");
	map.put("H_MTR_T5","F_QUAN");
	map.put("H_MTR_T6","F_QUAN");
	
	//MOC 2
/*	map.put("H_WHEL_CHR","F_QUAN");
	map.put("H_CSS_CHR","F_QUAN");*/
	
	/*map.put("Q_KVAH_ADJ","F_QUAN");
	map.put("Q_KWHE_ADJ","F_QUAN");
	map.put("Q_KWH_ADJ","F_QUAN");
	map.put("Q_T1_ADJ","F_QUAN");
	map.put("Q_T2_ADJ","F_QUAN");
	map.put("Q_T3_ADJ","F_QUAN");
	map.put("Q_T4_ADJ","F_QUAN");
	map.put("Q_T5_ADJ","F_QUAN");
	map.put("Q_T6_ADJ","F_QUAN");*/
	//MOC 2 Q_KWH_FY
/*	map.put("Q_KWH_FY","F_QUAN");*/
	map.put("Q_OPN_P","F_QUAN");
	map.put("Q_OPN_OP","F_QUAN");
	map.put("Q_OPN_KVAH","F_QUAN");
	
	
	
	map.put("H_TRU_KWH","F_QUAN");
	map.put("H_TRU_HKWH","F_QUAN");
	
	
	
	map.put("Q_FAC_FY21","F_QUAN");
	map.put("Q_FA_HFY21","F_QUAN");
	
	map.put("D_CONN_DMD","F_DEMA");
	map.put("D_OPN_MD","F_DEMA");
	map.put("D_CONT_DMD","F_DEMA");
	map.put("D_INSTLCAP","F_DEMA");
	map.put("H_RMD_KVA","F_DEMA");
	map.put("H_BMD_KVA","F_DEMA");
	/*map.put("D_PPA_CAP","F_DEMA");*/
	map.put("D_RMD_ADJ","F_DEMA");
	
	
	map.put("B_3PHASE","F_FLAG");
	map.put("B_HT_MTR","F_FLAG");
	map.put("B_TOD_EXMP","F_FLAG");
	
	map.put("B_CONV_GEN","F_FLAG");
	map.put("B_RFTP_GEN","F_FLAG");
	map.put("B_RNEW_GEN","F_FLAG");
	map.put("B_SGAR_GEN","F_FLAG");
	map.put("B_TEMP_CON","F_FLAG");
	map.put("B_TRUE_UP","F_FLAG");
	map.put("B_ED_EXMPT","F_FLAG");
	map.put("B_IND_FEED","F_FLAG");
	map.put("B_SP_GAS","F_FLAG");
	map.put("B_SP_HYDEL","F_FLAG");
	map.put("B_SP_NCE","F_FLAG");
	map.put("B_SP_THRML","F_FLAG");
	map.put("B_SP_W&S","F_FLAG");
	/*map.put("B_COL_SEG","F_FLAG");*/
	map.put("B_GR_EXMPT","F_FLAG");
	
/*	map.put("B_MR_ESTM","F_FLAG");*/
	
	
	//MOCK2
	/*map.put("B_OPN_ACSS", "F_FLAG");*/
	/*map.put("B_COL_SEG", "F_FLAG");*/
/*	map.put("B_SHUTDOWN","F_FLAG");*/
	
	
	map.put("H_CC_CHRG","F_AMOU");
	map.put("H_ED_CHRG","F_AMOU");
	map.put("H_ENG_CHRG","F_AMOU");
	map.put("H_GRID_SUP","F_AMOU");
	map.put("H_TOT_BILL","F_AMOU");
	
	map.put("H_TRU_CHRG","F_AMOU");
	map.put("H_TRU_HCHR","F_AMOU");
	map.put("H_TOT_SUB","F_AMOU");
	map.put("H_TOT_INV","F_AMOU");
	map.put("H_DMD_CHRG","F_AMOU");
	map.put("H_DMD_PEN","F_AMOU");
	map.put("H_ENG_NORM","F_AMOU");
	map.put("H_ENG_OP","F_AMOU");
	map.put("H_ENG_PEAK","F_AMOU");
	map.put("H_ENG_PEN","F_AMOU");
	map.put("H_VOL_SCHR","F_AMOU");
	map.put("H_TRU_MO","F_AMOU");
	map.put("H_WHEL_CHR","F_AMOU");
	map.put("H_CSS_CHR","F_AMOU");
	map.put("H_FPPCA2","F_AMOU");
	map.put("H_FPPCA_CH","F_AMOU");
	
	map.put("I_SEAS_STR","F_INTE");
	map.put("I_SEAS_END","F_INTE");
	map.put("H_TRU_MTHS", "F_FACT");
	return map.get(key);
}

/*public static String getMonthPeriod(String key) {
	Map<String,String> map = new HashMap<>();
	map.put("NNNYYYYYYYNN","4-10");
	map.put("NNNYYYYYYNNN","4-9");
	map.put("NNNYYYYYYYYN","4-11");
	map.put("NNNNNNYYYNNN","7-9");
	map.put("NNNYYYYYYYYY","4-12");
	map.put("NNNNNNNYYYYY","8-12");
	map.put("YNNNNNYYYYYY","7-1");
	map.put("NNNNYYYYYYYN","5-11");
	map.put("NNNNNNNYYYYN","8-11");
	map.put("YNNNNNNYYYYY","8-1");
	map.put("NNNNNYYYYYYY","6-12");
	map.put("NNNNNYYYYNNN","6-9");
	map.put("NYYYYYYYYNNN","2-9");
	map.put("YYYNNNNNNYYY","10-3");
	map.put("YYYNNNNYYYYY","8-3");
	map.put("YYYYYNNNYYYY","9-5");
	map.put("YNNNYYYYYYYY","5-1");
	map.put("NNNNNNNYYYNN","8-10");
	map.put("NNNNNYYYYYYN","6-11");
	map.put("NNNNNYYYYYNN","6-10");
	map.put("NNNNYYYYYNNN","5-9");
	map.put("NNNNYYYYNNNN","5-8");
	map.put("NNNNNYYYNNNN","6-8");
	map.put("NNNNYYYYYYYY","5-12");
	map.put("NNNYYYNNNNNN","4-6");
	map.put("NNNNNNYYYYNN","7-10");
	map.put("NNNNYYYYYYNN","5-10");
	map.put("NNYYYYYYYNNN","3-9");
	map.put("YNNNNNNNNNYY","11-1");
	map.put("YYYNNNYYYYYY","7-3");
	map.put("NNYYYYYYYYNN","3-10");
	map.put("NNNYYYYYNNNN","4-8");
	map.put("YYNNNYYYYYYY","6-2");
	map.put("YYYYYYYYNNNN","1-8");
	return map.get(key);
}*/

public static String getMonthPeriod(String key) {
	Map<String,String> map = new HashMap<>();
	map.put("NNNYYYYYYYNN","11-3");
	map.put("NNNYYYYYYNNN","10-3");
	map.put("NNNYYYYYYYYN","12-3");
	map.put("NNNNNNYYYNNN","10-6");
	map.put("NNNYYYYYYYYY","1-3");
	map.put("NNNNNNNYYYYY","1-7");
	map.put("YNNNNNYYYYYY","2-6");
	map.put("NNNNYYYYYYYN","12-4");
	map.put("NNNNNNNYYYYN","12-7");
	map.put("YNNNNNNYYYYY","2-7");
	map.put("NNNNNYYYYYYY","1-5");
	map.put("NNNNNYYYYNNN","10-5");
	map.put("NYYYYYYYYNNN","10-1");
	map.put("YYYNNNNNNYYY","4-9");
	map.put("YYYNNNNYYYYY","4-7");
	map.put("YYYYYNNNYYYY","6-8");
	map.put("YNNNYYYYYYYY","2-4");
	map.put("NNNNNNNYYYNN","11-7");
	map.put("NNNNNYYYYYYN","12-5");
	map.put("NNNNNYYYYYNN","11-5");
	map.put("NNNNYYYYYNNN","10-4");
	map.put("NNNNYYYYNNNN","9-4");
	map.put("NNNNNYYYNNNN","9-5");
	map.put("NNNNYYYYYYYY","1-4");
	map.put("NNNYYYNNNNNN","7-3");
	map.put("NNNNNNYYYYNN","11-6");
	map.put("NNNNYYYYYYNN","11-4");
	map.put("NNYYYYYYYNNN","10-2");
	map.put("YNNNNNNNNNYY","2-10");
	map.put("YYYNNNYYYYYY","4-7");
	map.put("NNYYYYYYYYNN","11-2");
	map.put("NNNYYYYYNNNN","9-3");
	map.put("YYNNNYYYYYYY","3-6");
	map.put("YYYYYYYYNNNN","9-12");
	map.put("NNNNYYYNNNNN","8-4");
	
	return map.get(key);
}

private static String getRemaingAmt(String key) {
	
	/*
	 * select BTSCNO , TOT_AMT - DEM_TU  TO_PAID_AMT from 
(select BTSCNO , sum(nvl(BT_TU_CHG,0)) + sum(nvl(BT_LTU_CHG,0)) DEM_TU from bill_hist 
where  BTBLDT between to_date('01-04-2022','DD-MM-YYYY') and to_date('31-10-2022','DD-MM-YYYY') and substr(BTSCNO,1,3)='VJA'
group by BTSCNO having sum(nvl(BT_TU_CHG,0)) + sum(nvl(BT_LTU_CHG,0)) >0) A,

(select USCNO, sum(TOT_AMT) TOT_AMT from (
select USCNO, TOT_AMT from ht_trueup_chg
union all
select HT_USCNO USCNO ,TOT_AMOUNT TOT_AMT from lt_ht_tu_rj where HT_USCNO is not null)
group by uscno) B where A.BTSCNO = B.USCNO(+);
	 */
	if(DisplayQUANTCT.remainingamt.containsKey(key))
	return DisplayQUANTCT.remainingamt.get(key);
	else
	return "0";
}
}
