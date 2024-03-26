package com.spdcl.dao;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class NewReportDao {

	@Autowired
	@Qualifier("jdbcTemplate")
	JdbcTemplate jdbcTemplate;

	@Autowired
	@Qualifier("jdbcTemplate1")
	JdbcTemplate jdbcTemplate1;

	@Autowired
	@Qualifier("jdbcTemplate2")
	JdbcTemplate jdbcTemplate2;

	@Autowired
	@Qualifier("jdbcTemplateDev")
	JdbcTemplate jdbcTemplateDev;

	@Autowired
	@Qualifier("jdbcTemplateLTDev")
	JdbcTemplate jdbcTemplateLTDev;

	private static Logger log = Logger.getLogger(NewReportDao.class);

	// 125
	public List<Map<String, Object>> getFinancialConsumption(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		String fin_year = request.getParameter("year");
		String fromdate = "01-APR-" + fin_year.split("-")[0];
		String todate = "31-MAR-" + fin_year.split("-")[1];
		if (circle.equals("ALL")) {
			String sql = "SELECT CASE WHEN MON_YEAR IS NULL THEN 'TOTAL' ELSE MON_YEAR END MON_YEAR,\r\n"
					+ "CASE WHEN CIRCLE IS NULL THEN 'TOTAL' ELSE CIRCLE END CIRCLE,\r\n"
					+ "HT1scs,HT1units,HT1demand,HT1ec,HT1SPECIFIC_CONSUMPTION,HT1SPECIFIC_REVENUE,\r\n"
					+ "HT2scs,HT2units,HT2demand,HT2ec,HT2SPECIFIC_CONSUMPTION,HT2SPECIFIC_REVENUE,\r\n"
					+ "HT3scs,HT3units,HT3demand,HT3ec,HT3SPECIFIC_CONSUMPTION,HT3SPECIFIC_REVENUE,\r\n"
					+ "HT4scs,HT4units,HT4demand,HT4ec,HT4SPECIFIC_CONSUMPTION,HT4SPECIFIC_REVENUE,\r\n"
					+ "HT5Bscs,HT5Bunits,HT5Bdemand,HT5Bec,HT5BSPECIFIC_CONSUMPTION,HT5BSPECIFIC_REVENUE,\r\n"
					+ "HT5Escs,HT5Eunits,HT5Edemand,HT5Eec,HT5ESPECIFIC_CONSUMPTION,HT5ESPECIFIC_REVENUE\r\n" + "\r\n"
					+ "FROM (SELECT CIRCLE,MON_YEAR                       \r\n" + "\r\n"
					+ ",SUM(case when CTCAT='HT1' then SCS END) HT1scs,\r\n"
					+ "ROUND(SUM(case when CTCAT='HT1' then UNITS END)) HT1units,\r\n"
					+ "ROUND(SUM(case when CTCAT='HT1' then DEMAND END)) HT1demand,\r\n"
					+ "ROUND(SUM(case when CTCAT='HT1' then EC END)) HT1ec,\r\n"
					+ "ROUND(SUM(case when CTCAT='HT1' then SPECIFIC_CONSUMPTION END)) HT1SPECIFIC_CONSUMPTION,\r\n"
					+ "SUM(case when CTCAT='HT1' then SPECIFIC_REVENUE END) HT1SPECIFIC_REVENUE\r\n" + "\r\n"
					+ ",SUM(case when CTCAT='HT2' then SCS END) HT2scs,\r\n"
					+ "ROUND(SUM(case when CTCAT='HT2' then UNITS END)) HT2units,\r\n"
					+ "ROUND(SUM(case when CTCAT='HT2' then DEMAND END)) HT2demand,\r\n"
					+ "ROUND(SUM(case when CTCAT='HT2' then EC END)) HT2ec,\r\n"
					+ "ROUND(SUM(case when CTCAT='HT2' then SPECIFIC_CONSUMPTION END)) HT2SPECIFIC_CONSUMPTION,\r\n"
					+ "SUM(case when CTCAT='HT2' then SPECIFIC_REVENUE END) HT2SPECIFIC_REVENUE\r\n" + "\r\n"
					+ ",SUM(case when CTCAT='HT3' then SCS END) HT3scs,\r\n"
					+ "ROUND(SUM(case when CTCAT='HT3' then UNITS END)) HT3units,\r\n"
					+ "ROUND(SUM(case when CTCAT='HT3' then DEMAND END)) HT3demand,\r\n"
					+ "ROUND(SUM(case when CTCAT='HT3' then EC END)) HT3ec,\r\n"
					+ "ROUND(SUM(case when CTCAT='HT3' then SPECIFIC_CONSUMPTION END)) HT3SPECIFIC_CONSUMPTION,\r\n"
					+ "SUM(case when CTCAT='HT3' then SPECIFIC_REVENUE END) HT3SPECIFIC_REVENUE\r\n" + "\r\n"
					+ ",SUM(case when CTCAT='HT4' then SCS END) HT4scs,\r\n"
					+ "ROUND(SUM(case when CTCAT='HT4' then UNITS END)) HT4units,\r\n"
					+ "ROUND(SUM(case when CTCAT='HT4' then DEMAND END)) HT4demand,\r\n"
					+ "ROUND(SUM(case when CTCAT='HT4' then EC END)) HT4ec,\r\n"
					+ "ROUND(SUM(case when CTCAT='HT4' then SPECIFIC_CONSUMPTION END)) HT4SPECIFIC_CONSUMPTION,\r\n"
					+ "SUM(case when CTCAT='HT4' then SPECIFIC_REVENUE END) HT4SPECIFIC_REVENUE\r\n" + "\r\n"
					+ ",SUM(case when CTCAT='HT5B' then SCS END) HT5Bscs,\r\n"
					+ "ROUND(SUM(case when CTCAT='HT5B' then UNITS END)) HT5Bunits,\r\n"
					+ "ROUND(SUM(case when CTCAT='HT5B' then DEMAND END)) HT5Bdemand,\r\n"
					+ "ROUND(SUM(case when CTCAT='HT5B' then EC END)) HT5Bec,\r\n"
					+ "ROUND(SUM(case when CTCAT='HT5B' then SPECIFIC_CONSUMPTION END)) HT5BSPECIFIC_CONSUMPTION,\r\n"
					+ "SUM(case when CTCAT='HT5B' then SPECIFIC_REVENUE END) HT5BSPECIFIC_REVENUE\r\n" + "\r\n"
					+ ",SUM(case when CTCAT='HT5E' then SCS END) HT5Escs,\r\n"
					+ "ROUND(SUM(case when CTCAT='HT5E' then UNITS END)) HT5Eunits,\r\n"
					+ "ROUND(SUM(case when CTCAT='HT5E' then DEMAND END)) HT5Edemand,\r\n"
					+ "ROUND(SUM(case when CTCAT='HT5E' then EC END)) HT5Eec,\r\n"
					+ "ROUND(SUM(case when CTCAT='HT5E' then SPECIFIC_CONSUMPTION END)) HT5ESPECIFIC_CONSUMPTION,\r\n"
					+ "SUM(case when CTCAT='HT5E' then SPECIFIC_REVENUE END) HT5ESPECIFIC_REVENUE\r\n" + "\r\n"
					+ "FROM\r\n" + "(SELECT SUBSTR(CTUSCNO,1,3)CIRCLE,MON_YEAR, \r\n"
					+ "case when CTCAT='HT5' AND CTSUBCAT='B' THEN 'HT5B'\r\n"
					+ "WHEN CTCAT='HT5' AND CTSUBCAT='E' THEN 'HT5E' ELSE CTCAT END CTCAT,\r\n" + "COUNT(*)SCS,\r\n"
					+ "SUM(MN_KVAH) UNITS,\r\n" + "SUM(DEMAND) DEMAND,\r\n" + "SUM(EC)EC,\r\n"
					+ "ROUND(SUM(MN_KVAH)/COUNT(*),2) SPECIFIC_CONSUMPTION,\r\n"
					+ "ROUND(SUM(EC)/SUM(MN_KVAH),2) SPECIFIC_REVENUE FROM CONS,\r\n"
					+ "(SELECT USCNO,MON_YEAR,SUM(MN_KVAH)MN_KVAH,SUM(NVL(CMD,0) +NVL(CCLPC,0)+NVL(DRJ,0)+NVL(RJ_CCLPC,0)+NVL(RJ_OTH,0))DEMAND,\r\n"
					+ "SUM(NVL(BTENGCHG_NOR,0)+ NVL(BTENGCHG_PEN,0))EC\r\n" + "FROM LEDGER_HT_HIST ,BILL_HIST \r\n"
					+ "WHERE uscno = btscno and to_char(BTBLDT,'MON-YYYY')=MON_YEAR and to_date(MON_YEAR,'MON-YYYY') between \r\n"
					+ "to_date(?,'DD-MM-YYYY') and to_date(?,'DD-MM-YYYY')\r\n" + "GROUP BY USCNO,MON_YEAR)\r\n"
					+ "WHERE CTUSCNO=USCNO  \r\n"
					+ "GROUP BY SUBSTR(CTUSCNO,1,3),MON_YEAR,case when CTCAT='HT5' AND CTSUBCAT='B' THEN 'HT5B'\r\n"
					+ "WHEN CTCAT='HT5' AND CTSUBCAT='E' THEN 'HT5E' ELSE CTCAT END\r\n"
					+ "ORDER BY TO_DATE(MON_YEAR,'MON-YYYY'),CIRCLE,CTCAT)GROUP BY CUBE(MON_YEAR,CIRCLE)   ORDER BY TO_DATE(MON_YEAR,'MON-YYYY'),CIRCLE)";
			log.info(sql);
			return jdbcTemplate.queryForList(sql, new Object[] { fromdate, todate });
		} else {
			String sql = "SELECT CASE WHEN MON_YEAR IS NULL THEN 'TOTAL' ELSE MON_YEAR END MON_YEAR,\r\n"
					+ "CASE WHEN CIRCLE IS NULL THEN 'TOTAL' ELSE CIRCLE END CIRCLE,\r\n"
					+ "HT1scs,HT1units,HT1demand,HT1ec,HT1SPECIFIC_CONSUMPTION,HT1SPECIFIC_REVENUE,\r\n"
					+ "HT2scs,HT2units,HT2demand,HT2ec,HT2SPECIFIC_CONSUMPTION,HT2SPECIFIC_REVENUE,\r\n"
					+ "HT3scs,HT3units,HT3demand,HT3ec,HT3SPECIFIC_CONSUMPTION,HT3SPECIFIC_REVENUE,\r\n"
					+ "HT4scs,HT4units,HT4demand,HT4ec,HT4SPECIFIC_CONSUMPTION,HT4SPECIFIC_REVENUE,\r\n"
					+ "HT5Bscs,HT5Bunits,HT5Bdemand,HT5Bec,HT5BSPECIFIC_CONSUMPTION,HT5BSPECIFIC_REVENUE,\r\n"
					+ "HT5Escs,HT5Eunits,HT5Edemand,HT5Eec,HT5ESPECIFIC_CONSUMPTION,HT5ESPECIFIC_REVENUE\r\n" + "\r\n"
					+ "FROM (SELECT CIRCLE,MON_YEAR                       \r\n" + "\r\n"
					+ ",SUM(case when CTCAT='HT1' then SCS END) HT1scs,\r\n"
					+ "ROUND(SUM(case when CTCAT='HT1' then UNITS END)) HT1units,\r\n"
					+ "ROUND(SUM(case when CTCAT='HT1' then DEMAND END)) HT1demand,\r\n"
					+ "ROUND(SUM(case when CTCAT='HT1' then EC END)) HT1ec,\r\n"
					+ "ROUND(SUM(case when CTCAT='HT1' then SPECIFIC_CONSUMPTION END)) HT1SPECIFIC_CONSUMPTION,\r\n"
					+ "SUM(case when CTCAT='HT1' then SPECIFIC_REVENUE END) HT1SPECIFIC_REVENUE\r\n" + "\r\n"
					+ ",SUM(case when CTCAT='HT2' then SCS END) HT2scs,\r\n"
					+ "ROUND(SUM(case when CTCAT='HT2' then UNITS END)) HT2units,\r\n"
					+ "ROUND(SUM(case when CTCAT='HT2' then DEMAND END)) HT2demand,\r\n"
					+ "ROUND(SUM(case when CTCAT='HT2' then EC END)) HT2ec,\r\n"
					+ "ROUND(SUM(case when CTCAT='HT2' then SPECIFIC_CONSUMPTION END)) HT2SPECIFIC_CONSUMPTION,\r\n"
					+ "SUM(case when CTCAT='HT2' then SPECIFIC_REVENUE END) HT2SPECIFIC_REVENUE\r\n" + "\r\n"
					+ ",SUM(case when CTCAT='HT3' then SCS END) HT3scs,\r\n"
					+ "ROUND(SUM(case when CTCAT='HT3' then UNITS END)) HT3units,\r\n"
					+ "ROUND(SUM(case when CTCAT='HT3' then DEMAND END)) HT3demand,\r\n"
					+ "ROUND(SUM(case when CTCAT='HT3' then EC END)) HT3ec,\r\n"
					+ "ROUND(SUM(case when CTCAT='HT3' then SPECIFIC_CONSUMPTION END)) HT3SPECIFIC_CONSUMPTION,\r\n"
					+ "SUM(case when CTCAT='HT3' then SPECIFIC_REVENUE END) HT3SPECIFIC_REVENUE\r\n" + "\r\n"
					+ ",SUM(case when CTCAT='HT4' then SCS END) HT4scs,\r\n"
					+ "ROUND(SUM(case when CTCAT='HT4' then UNITS END)) HT4units,\r\n"
					+ "ROUND(SUM(case when CTCAT='HT4' then DEMAND END)) HT4demand,\r\n"
					+ "ROUND(SUM(case when CTCAT='HT4' then EC END)) HT4ec,\r\n"
					+ "ROUND(SUM(case when CTCAT='HT4' then SPECIFIC_CONSUMPTION END)) HT4SPECIFIC_CONSUMPTION,\r\n"
					+ "SUM(case when CTCAT='HT4' then SPECIFIC_REVENUE END) HT4SPECIFIC_REVENUE\r\n" + "\r\n"
					+ ",SUM(case when CTCAT='HT5B' then SCS END) HT5Bscs,\r\n"
					+ "ROUND(SUM(case when CTCAT='HT5B' then UNITS END)) HT5Bunits,\r\n"
					+ "ROUND(SUM(case when CTCAT='HT5B' then DEMAND END)) HT5Bdemand,\r\n"
					+ "ROUND(SUM(case when CTCAT='HT5B' then EC END)) HT5Bec,\r\n"
					+ "ROUND(SUM(case when CTCAT='HT5B' then SPECIFIC_CONSUMPTION END)) HT5BSPECIFIC_CONSUMPTION,\r\n"
					+ "SUM(case when CTCAT='HT5B' then SPECIFIC_REVENUE END) HT5BSPECIFIC_REVENUE\r\n" + "\r\n"
					+ ",SUM(case when CTCAT='HT5E' then SCS END) HT5Escs,\r\n"
					+ "ROUND(SUM(case when CTCAT='HT5E' then UNITS END)) HT5Eunits,\r\n"
					+ "ROUND(SUM(case when CTCAT='HT5E' then DEMAND END)) HT5Edemand,\r\n"
					+ "ROUND(SUM(case when CTCAT='HT5E' then EC END)) HT5Eec,\r\n"
					+ "ROUND(SUM(case when CTCAT='HT5E' then SPECIFIC_CONSUMPTION END)) HT5ESPECIFIC_CONSUMPTION,\r\n"
					+ "SUM(case when CTCAT='HT5E' then SPECIFIC_REVENUE END) HT5ESPECIFIC_REVENUE\r\n" + "\r\n"
					+ "FROM\r\n" + "(SELECT SUBSTR(CTUSCNO,1,3)CIRCLE,MON_YEAR, \r\n"
					+ "case when CTCAT='HT5' AND CTSUBCAT='B' THEN 'HT5B'\r\n"
					+ "WHEN CTCAT='HT5' AND CTSUBCAT='E' THEN 'HT5E' ELSE CTCAT END CTCAT,\r\n" + "COUNT(*)SCS,\r\n"
					+ "SUM(MN_KVAH) UNITS,\r\n" + "SUM(DEMAND) DEMAND,\r\n" + "SUM(EC)EC,\r\n"
					+ "ROUND(SUM(MN_KVAH)/COUNT(*),2) SPECIFIC_CONSUMPTION,\r\n"
					+ "ROUND(SUM(EC)/SUM(MN_KVAH),2) SPECIFIC_REVENUE FROM CONS,\r\n"
					+ "(SELECT USCNO,MON_YEAR,SUM(MN_KVAH)MN_KVAH,SUM(NVL(CMD,0) +NVL(CCLPC,0)+NVL(DRJ,0)+NVL(RJ_CCLPC,0)+NVL(RJ_OTH,0))DEMAND,\r\n"
					+ "SUM(NVL(BTENGCHG_NOR,0)+ NVL(BTENGCHG_PEN,0))EC\r\n" + "FROM LEDGER_HT_HIST ,BILL_HIST \r\n"
					+ "WHERE uscno = btscno and to_char(BTBLDT,'MON-YYYY')=MON_YEAR and to_date(MON_YEAR,'MON-YYYY') between \r\n"
					+ "to_date(?,'DD-MM-YYYY') and to_date(?,'DD-MM-YYYY')\r\n" + "GROUP BY USCNO,MON_YEAR)\r\n"
					+ "WHERE CTUSCNO=USCNO  \r\n"
					+ "GROUP BY SUBSTR(CTUSCNO,1,3),MON_YEAR,case when CTCAT='HT5' AND CTSUBCAT='B' THEN 'HT5B'\r\n"
					+ "WHEN CTCAT='HT5' AND CTSUBCAT='E' THEN 'HT5E' ELSE CTCAT END\r\n"
					+ "ORDER BY TO_DATE(MON_YEAR,'MON-YYYY'),CIRCLE,CTCAT) WHERE CIRCLE=? GROUP BY MON_YEAR,CIRCLE   ORDER BY TO_DATE(MON_YEAR,'MON-YYYY'),CIRCLE)";

			log.info(sql);

			return jdbcTemplate.queryForList(sql, new Object[] { fromdate, todate, circle });
		}
	}

	// 23A
	public List<Map<String, Object>> getEmailsAndSmsReport(HttpServletRequest request) {

		String monthYear = request.getParameter("month") + "-" + request.getParameter("year");

		System.out.println("Year is : " + monthYear);
		String circle = request.getParameter("circle");
		String type = request.getParameter("type");
		if (circle.equals("ALL")) {
			try {
				if (type.equals("EMAIL")) {

					String mailQuery = "SELECT DISTINCT A.CTUSCNO,A.CTNAME,A.CTCAT,A.CTSUBCAT,B.SCNO,A.CIRCLE,A.CTEMAILID,replace(replace(B.EMAIL,'[',''),']','') email,B.MTH FROM\r\n"
							+ "(SELECT SUBSTR(CTUSCNO,1,3)CIRCLE,CTUSCNO,CTNAME,CTCAT,CTSUBCAT,CTEMAILID FROM CONS WHERE  CTEMAILID NOT IN(' ','NA','---','0','-','--'))A,\r\n"
							+ "(SELECT DISTINCT SUBSTR(SCNO,1,3)CIRCLE,SCNO,EMAIL,MTH FROM (select SCNO,TRIM(EMAIL)EMAIL,TRIM(MOBILE)MOBILE,TRIM(FLAG)FLAG,TRIM(MTH)MTH from \r\n"
							+ "SMS_EMAIL_SENT WHERE MOBILE='-' AND EMAIL!='-' AND FLAG='S' AND TO_CHAR(MTH,'MON-YYYY')=? group by SCNO,EMAIL,MOBILE,FLAG,MTH))B\r\n"
							+ "WHERE A.CIRCLE=B.CIRCLE(+) \r\n" + "AND A.CTUSCNO=B.SCNO ORDER BY CIRCLE,A.CTUSCNO";

					log.info(mailQuery);

					return jdbcTemplate.queryForList(mailQuery, new Object[] { monthYear });

				} else {

					String smsQuery = "SELECT DISTINCT D.CTUSCNO,D.CTNAME,D.CTCAT,D.CTSUBCAT,C.SCNO,D.CIRCLE,d.ctmobile,TRIM(c.mobile)mobile,MTH FROM\r\n"
							+ "(SELECT UNIQUE SUBSTR(SCNO,1,3)CIRCLE,SCNO,MOBILE,MTH FROM (select SCNO,EMAIL,TRIM(MOBILE)MOBILE,TRIM(FLAG)FLAG,TRIM(MTH)MTH from \r\n"
							+ "SMS_EMAIL_SENT \r\n"
							+ "WHERE MOBILE!='-' AND EMAIL='-' AND FLAG='S' AND TO_CHAR(MTH,'MON-YYYY')=? group by SCNO,EMAIL,MOBILE,FLAG,MTH))C,\r\n"
							+ "(SELECT SUBSTR(CTUSCNO,1,3)CIRCLE,CTUSCNO,CTNAME,CTCAT,CTSUBCAT,ctmobile FROM CONS WHERE  ctmobile NOT IN(' ','NA','---','0','-','--')  and CONS.CTSTATUS=1 )D\r\n"
							+ "WHERE D.CIRCLE=C.CIRCLE(+)\r\n" + "AND D.ctmobile=C.MOBILE(+)\r\n"
							+ "AND D.CTUSCNO=C.SCNO\r\n" + "ORDER BY CIRCLE";

					log.info(smsQuery);
					return jdbcTemplate.queryForList(smsQuery, new Object[] { monthYear });
				}

			} catch (DataAccessException e) {
				log.error(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}
		} else {
			try {
				if (type.equals("EMAIL")) {
					String mailQuery = "SELECT DISTINCT A.CTUSCNO,A.CTNAME,A.CTCAT,A.CTSUBCAT,B.SCNO,A.CIRCLE,A.CTEMAILID,replace(replace(B.EMAIL,'[',''),']','') email,B.MTH FROM\r\n"
							+ "(SELECT SUBSTR(CTUSCNO,1,3)CIRCLE,CTUSCNO,CTNAME,CTCAT,CTSUBCAT,CTEMAILID FROM CONS WHERE  CTEMAILID NOT IN(' ','NA','---','0','-','--'))A,\r\n"
							+ "(SELECT DISTINCT SUBSTR(SCNO,1,3)CIRCLE,SCNO,EMAIL,MTH FROM (select SCNO,TRIM(EMAIL)EMAIL,TRIM(MOBILE)MOBILE,TRIM(FLAG)FLAG,TRIM(MTH)MTH from \r\n"
							+ "SMS_EMAIL_SENT WHERE MOBILE='-' AND EMAIL!='-' AND FLAG='S' AND TO_CHAR(MTH,'MON-YYYY')=? group by SCNO,EMAIL,MOBILE,FLAG,MTH))B\r\n"
							+ "WHERE A.CIRCLE=B.CIRCLE(+) \r\n" + "AND A.CIRCLE=?\r\n"
							+ "AND A.CTUSCNO=B.SCNO ORDER BY CIRCLE,A.CTUSCNO";

					log.info(mailQuery);
					return jdbcTemplate.queryForList(mailQuery, new Object[] { monthYear, circle });
				} else {

					String smsQuery = "SELECT DISTINCT D.CTUSCNO,D.CTNAME,D.CTCAT,D.CTSUBCAT,C.SCNO,D.CIRCLE,d.ctmobile,TRIM(c.mobile)mobile,MTH FROM\r\n"
							+ "(SELECT UNIQUE SUBSTR(SCNO,1,3)CIRCLE,SCNO,MOBILE,MTH FROM (select SCNO,EMAIL,TRIM(MOBILE)MOBILE,TRIM(FLAG)FLAG,TRIM(MTH)MTH from \r\n"
							+ "SMS_EMAIL_SENT \r\n"
							+ "WHERE MOBILE!='-' AND EMAIL='-' AND FLAG='S' AND TO_CHAR(MTH,'MON-YYYY')=? group by SCNO,EMAIL,MOBILE,FLAG,MTH))C,\r\n"
							+ "(SELECT SUBSTR(CTUSCNO,1,3)CIRCLE,CTUSCNO,CTNAME,CTCAT,CTSUBCAT,ctmobile FROM CONS WHERE  ctmobile NOT IN(' ','NA','---','0','-','--')  and CONS.CTSTATUS=1 )D\r\n"
							+ "WHERE D.CIRCLE=C.CIRCLE(+)\r\n" + "AND D.ctmobile=C.MOBILE(+)\r\n"
							+ "AND D.CTUSCNO=C.SCNO\r\n" + "AND D.CIRCLE=?\r\n" + "ORDER BY CIRCLE";

					log.info(smsQuery);
					return jdbcTemplate.queryForList(smsQuery, new Object[] { monthYear, circle });
				}
			} catch (DataAccessException e) {
				log.error(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}
		}
	}

	// 97D
	public List<Map<String, Object>> getHtDCBCollectionSplitMonthlyWiseAbstract(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		String levi_month = "01-" + request.getParameter("fmonth") + "-" + request.getParameter("fyear");
		if (circle.equalsIgnoreCase("ALL")) {
			try {
				String sql = "select  NVL(T_LDT,'TOTAL')LDT,NVL(CIRNAME,'TOTAL') CIRNAME,divname,subname,secname,T_LDT,round(SUM(TOB)) TOB,ROUND(SUM(DEMAND)) DEMAND,ROUND(SUM(COLL_ARREAR)) COLL_ARREAR,ROUND(SUM(COLL_DEMAND))COLL_DEMAND,ROUND(SUM(COLLECTION)) COLLECTION,ROUND(SUM(CB)) CB,ROUND(sum(SD)) SD from \r\n"
						+ "(SELECT  UNIQUE TO_CHAR(LDT,'MON-YYYY') T_LDT,SUBSTR(CTUSCNO,1,3) CIRNAME,divname,subname,secname,SUM(NVL(OB,0)) TOB,SUM(NVL(DEM,0)+NVL(DR_AMT,0)) DEMAND,\r\n"
						+ "SUM(CASE WHEN NVL(OB,0)>0 THEN CASE WHEN NVL(OB,0)>(NVL(PAY,0)+NVL(CR_AMT,0)) THEN (NVL(PAY,0)+NVL(CR_AMT,0)) ELSE NVL(OB,0) END END) COLL_ARREAR,\r\n"
						+ "SUM(CASE WHEN NVL(OB,0)>0 THEN CASE WHEN NVL(OB,0)<(NVL(PAY,0)+NVL(CR_AMT,0)) THEN (NVL(PAY,0)+NVL(CR_AMT,0)-NVL(OB,0)) END ELSE (NVL(PAY,0)+NVL(CR_AMT,0)) END )COLL_DEMAND,\r\n"
						+ "SUM(NVL(PAY,0)+NVL(CR_AMT,0)) COLLECTION, SUM(NVL(CB,0)) CB , SUM(NVL(CB_SD,0)) SD \r\n"
						+ "FROM CONS,master.spdclmaster,\r\n" + "\r\n"
						+ "(SELECT USCNO,TRUNC(TO_DATE(MON_YEAR,'MON-YYYY'),'MM') LDT,(Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)) OB,(NVL(TOT_PAY,0)) PAY,(NVL(CMD,0)+NVL(CCLPC,0)) DEM,NVL(CBTOT,0)+NVL(CB_CCLPC,0)+NVL(CB_OTH,0) CB ,NVL(CB_SD,0) CB_SD\r\n"
						+ "FROM LEDGER_HT_HIST WHERE TRUNC(TO_DATE(MON_YEAR,'MON-YYYY'),'MM')>=? AND SUBSTR(USCNO,1,3) IN('GNT','VJA','ONG','CRD')) L,\r\n"
						+ "\r\n"
						+ "(select uscno,TRUNC(rjdt,'MM') RDT,SUM(CASE WHEN RJTYPE='DR' THEN (nvl(ENGCHG,0)+nvl(THEFT,0)+nvl(OTHCHG,0)+nvl(CUSTCHG,0)+nvl(DEMCHG,0)+nvl(ED,0)+nvl(LPC,0)+nvl(EDI,0)+nvl(FSA,0)+NVL(CC_LPC,0)+NVL(CC_OTH,0)) END) DR_AMT,\r\n"
						+ "SUM(CASE WHEN RJTYPE='CR'THEN (nvl(ENGCHG,0)+nvl(THEFT,0)+nvl(OTHCHG,0)+nvl(CUSTCHG,0)+nvl(DEMCHG,0)+nvl(ED,0)+nvl(LPC,0)+nvl(EDI,0)+nvl(FSA,0)+NVL(CC_LPC,0)+NVL(CC_OTH,0)) END) CR_AMT \r\n"
						+ "from JOURNAL_HIST WHERE TRUNC(rjdt,'MM')>=?  and SUBSTR(USCNO,1,3) IN('GNT','VJA','ONG','CRD') AND TRIM(STATUS) NOT IN ('X','E') GROUP BY USCNO,TRUNC(rjdt,'MM')) RJ\r\n"
						+ "\r\n"
						+ "WHERE CTUSCNO=L.USCNO AND L.USCNO=RJ.USCNO(+) AND LDT=RDT(+) and substr(ctseccd,-5)=seccd GROUP BY LDT,SUBSTR(CTUSCNO,1,3),divname,subname,secname)\r\n"
						+ "GROUP BY T_LDT,CIRNAME,divname,subname,secname\r\n"
						+ "ORDER BY TO_DATE(T_LDT,'MON-YYYY'),case when CIRNAME = 'VJA' then '001' when CIRNAME = 'GNT' then '002' when CIRNAME = 'ONG' then '003'  when CIRNAME = 'CRD' then '009' else CIRNAME end,divname,subname,secname";
				log.info(sql);
				return jdbcTemplate.queryForList(sql, new Object[] { levi_month, levi_month });
			} catch (DataAccessException e) {
				e.printStackTrace();
				log.error(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}
		} else {
			try {
				String sql = "select  NVL(T_LDT,'TOTAL')LDT,NVL(CIRNAME,'TOTAL') CIRNAME,divname,subname,secname,T_LDT,ROUND(SUM(TOB)) TOB,ROUND(SUM(DEMAND)) DEMAND,ROUND(SUM(COLL_ARREAR)) COLL_ARREAR,ROUND(SUM(COLL_DEMAND))COLL_DEMAND,ROUND(SUM(COLLECTION)) COLLECTION,ROUND(SUM(CB)) CB,ROUND(sum(SD)) SD from \r\n"
						+ "(SELECT  UNIQUE TO_CHAR(LDT,'MON-YYYY') T_LDT,SUBSTR(CTUSCNO,1,3) CIRNAME,divname,subname,secname,SUM(NVL(OB,0)) TOB,SUM(NVL(DEM,0)+NVL(DR_AMT,0)) DEMAND,\r\n"
						+ "SUM(CASE WHEN NVL(OB,0)>0 THEN CASE WHEN NVL(OB,0)>(NVL(PAY,0)+NVL(CR_AMT,0)) THEN (NVL(PAY,0)+NVL(CR_AMT,0)) ELSE NVL(OB,0) END END) COLL_ARREAR,\r\n"
						+ "SUM(CASE WHEN NVL(OB,0)>0 THEN CASE WHEN NVL(OB,0)<(NVL(PAY,0)+NVL(CR_AMT,0)) THEN (NVL(PAY,0)+NVL(CR_AMT,0)-NVL(OB,0)) END ELSE (NVL(PAY,0)+NVL(CR_AMT,0)) END )COLL_DEMAND,\r\n"
						+ "SUM(NVL(PAY,0)+NVL(CR_AMT,0)) COLLECTION, SUM(NVL(CB,0)) CB , SUM(NVL(CB_SD,0)) SD \r\n"
						+ "FROM CONS,master.spdclmaster,\r\n" + "\r\n"
						+ "(SELECT USCNO,TRUNC(TO_DATE(MON_YEAR,'MON-YYYY'),'MM') LDT,(Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)) OB,(NVL(TOT_PAY,0)) PAY,(NVL(CMD,0)+NVL(CCLPC,0)) DEM,NVL(CBTOT,0)+NVL(CB_CCLPC,0)+NVL(CB_OTH,0) CB ,NVL(CB_SD,0) CB_SD\r\n"
						+ "FROM LEDGER_HT_HIST WHERE TRUNC(TO_DATE(MON_YEAR,'MON-YYYY'),'MM')>=? AND SUBSTR(USCNO,1,3) IN('GNT','VJA','ONG','CRD')) L,\r\n"
						+ "\r\n"
						+ "(select uscno,TRUNC(rjdt,'MM') RDT,SUM(CASE WHEN RJTYPE='DR' THEN (nvl(ENGCHG,0)+nvl(THEFT,0)+nvl(OTHCHG,0)+nvl(CUSTCHG,0)+nvl(DEMCHG,0)+nvl(ED,0)+nvl(LPC,0)+nvl(EDI,0)+nvl(FSA,0)+NVL(CC_LPC,0)+NVL(CC_OTH,0)) END) DR_AMT,\r\n"
						+ "SUM(CASE WHEN RJTYPE='CR'THEN (nvl(ENGCHG,0)+nvl(THEFT,0)+nvl(OTHCHG,0)+nvl(CUSTCHG,0)+nvl(DEMCHG,0)+nvl(ED,0)+nvl(LPC,0)+nvl(EDI,0)+nvl(FSA,0)+NVL(CC_LPC,0)+NVL(CC_OTH,0)) END) CR_AMT \r\n"
						+ "from JOURNAL_HIST WHERE TRUNC(rjdt,'MM')>=?  and SUBSTR(USCNO,1,3) IN('GNT','VJA','ONG','CRD') AND TRIM(STATUS) NOT IN ('X','E') GROUP BY USCNO,TRUNC(rjdt,'MM')) RJ\r\n"
						+ "\r\n"
						+ "WHERE CTUSCNO=L.USCNO AND L.USCNO=RJ.USCNO(+) AND LDT=RDT(+) and substr(ctseccd,-5)=seccd GROUP BY LDT,SUBSTR(CTUSCNO,1,3),divname,subname,secname)\r\n"
						+ "where CIRNAME=?\r\n" + "GROUP BY T_LDT,CIRNAME,divname,subname,secname\r\n"
						+ "ORDER BY TO_DATE(T_LDT,'MON-YYYY'),case when CIRNAME = 'VJA' then '001' when CIRNAME = 'GNT' then '002' when CIRNAME = 'ONG' then '003'  when CIRNAME = 'CRD' then '009' else CIRNAME end,divname,subname,secname";
				log.info(sql);
				return jdbcTemplate.queryForList(sql, new Object[] { levi_month, levi_month, circle });
			} catch (DataAccessException e) {
				e.printStackTrace();
				log.error(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}

		}
	}

	// 123
	public List<Map<String, Object>> getMonthWiseTariffReport(HttpServletRequest request) {

		String month = request.getParameter("month");
		String year = request.getParameter("year");
		String fromDate = "05-" + month + "-" + year;
		String toDate = "30-" + month + "-" + year;
		String monthYear = "01-APR-" + year;

		try {
			String sql = "SELECT  UNIQUE bdt Bmonth,substr(CATEGORY,1,1) cat,substr(CATEGORY,2) subcat,voltage,DESC_HT DESCRIPT ,COUNT(*) NOS,\r\n"
					+ "--SUM(NVL(RUNITS,0)) REC_UNITS,\r\n" + "SUM(NVL(BUNIT,0)) BILLED_UNITS,\r\n"
					+ "SUM(NVL(OFPU,0)) OFF_PEAK_UNITS,\r\n" + "SUM(NVL(PEU,0)) PEAK_UNITS,\r\n"
					+ "SUM(NVL(COL,0)) COLONY_UNITS,\r\n" + "SUM(NVL(EC,0)) ECHG,\r\n" + "SUM(NVL(FIXED,0)) FCHG,\r\n"
					+ "SUM(NVL(CC,0)) CCHG,\r\n" + "--SUM(NVL(ED,0)) EDCHG,\r\n"
					+ "--SUM(NVL(EC,0)+NVL(FIXED,0)+NVL(CC,0)+NVL(ED,0)) DEMAND_CHGS,\r\n"
					+ "SUM(NVL(DEM,0)-NVL(EC,0)-NVL(FIXED,0)-NVL(CC,0)) OTHER_CHGS,\r\n"
					+ "SUM(NVL(CR_AMT,0)+NVL(PAY,0)) COLL,\r\n"
					+ "SUM(NVL(INC,0)) INCENTIVE_AMT,SUM(CTCMD_HT) CMD,SUM(NVL(BMD,0)) RMD\r\n"
					+ "--SUM(NVL(VSUR,0)) VOLTAGE_SURCHARGE_AMT\r\n" + "--SUM(NVL(DEM,0)) MAR_21_DEM\r\n"
					+ " from cons,SPDCLMASTER,HT_TARIFF,\r\n" + "(\r\n"
					+ "SELECT BTSCNO,BTBLCAT,BTBLSCAT,BTACTUAL_KV,TRUNC(BTBLDT,'MM') BDT,NVL(BTRKVA_HT,0) BMD,NVL(BTRKVAH_HT,0) RUNITS,(NVL(BTBKVAH,0)) BUNIT,NVL(BTTOD1,0)+NVL(BTTOD6,0) OPUNITS,\r\n"
					+ "NVL(BTTOD2,0)+NVL(BTTOD5,0) PUNITS,NVL(BTBLCOLNY_HT,0) COLUNITS,NVL(BTCURDEM,0)-(NVL(BTCUSTCHG,0)+NVL(BTADLCHG,0)+NVL(BTED,0)+NVL(BTED_INT,0)+NVL(BTDEMCHG_NOR,0)+NVL(BTDEMCHG_PEN,0)+NVL(BTOTHERCHG,0)) EC,\r\n"
					+ "NVL(BTCUSTCHG,0) CC,\r\n" + "NVL(BTDEMCHG_NOR,0)+NVL(BTDEMCHG_PEN,0) FIXED,\r\n"
					+ "NVL(BTED,0) ED,\r\n" + "NVL(BTVOLTSURCHG,0) VSUR,\r\n" + "NVL(BTTOD1,0)+NVL(BTTOD6,0) OFPU,\r\n"
					+ "NVL(BTTOD2,0)+NVL(BTTOD5,0) PEU,\r\n" + "NVL(BTBLCOLNY_HT,0) COL,\r\n"
					+ "--NVL(BTCURDEM,0)-(NVL(BTADLCHG,0)+NVL(BTED_INT,0)) DEMCHG,\r\n"
					+ "--NVL(BTADLCHG,0)+NVL(BTCOURT_LPC,0) SURCHG,\r\n" + "NVL(BTLFINCENTIVE_HT,0) INC,\r\n"
					+ "(NVL(BTCURDEM,0)+NVL(BTCOURT_LPC,0)) DEM FROM BILL_hist where btbldt = ? ) B, \r\n"
					+ "(SELECT USCNO,TRUNC(PAY_DATE,'MM') PDT,SUM(NVL(PCMD,0)) PAY FROM PAY_ht_hist WHERE PAY_DATE between ? and ?   AND pay_sta_flg='C' \r\n"
					+ "GROUP BY USCNO,TRUNC(PAY_DATE,'MM')) P,\r\n" + "(select uscno,TRUNC(RJDT,'MM') RDT,\r\n"
					+ "SUM(CASE WHEN RJTYPE='CR'THEN (nvl(ENGCHG,0)+nvl(THEFT,0)+nvl(OTHCHG,0)+nvl(CUSTCHG,0)+nvl(DEMCHG,0)+nvl(ED,0)+nvl(LPC,0)+nvl(EDI,0)+nvl(FSA,0)+NVL(CC_LPC,0)+NVL(CC_OTH,0)) END) CR_AMT  \r\n"
					+ "from JOURNAL_HIST WHERE  RJDT between ? and ?    and SUBSTR(USCNO,1,3) IN('GNT','VJA','ONG','CRD') AND TRIM(STATUS) NOT IN ('X') \r\n"
					+ "GROUP BY USCNO,TRUNC(RJDT,'MM')) RJ\r\n"
					+ "WHERE substr(ctseccd,-5)=seccd AND CTUSCNO=BTSCNO AND  BDT=RDT(+) AND BDT=PDT(+)  AND CTUSCNO=P.USCNO(+) AND BTBLCAT=SUBSTR(HT_CAT,3) AND BTBLSCAT=HT_SUBCAT \r\n"
					+ "and ctactual_kv=voltage AND FROM_DT=?\r\n"
					+ "AND CTUSCNO=RJ.USCNO(+)  GROUP BY BDT,substr(category,1,1),substr(category,2),voltage,DESC_HT ORDER BY 1,2,3,4\r\n";
			log.info(sql);
			return jdbcTemplate.queryForList(sql,
					new Object[] { fromDate, fromDate, toDate, fromDate, toDate, monthYear });
		} catch (DataAccessException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			e.printStackTrace();
			return Collections.emptyList();
		}

	}

	// 124
	public List<Map<String, Object>> getFinancialYearTariffReport(HttpServletRequest request) {
		String fin_year = request.getParameter("year");
		String fromdate = "01-APR-" + fin_year.split("-")[0];
		String todate = "31-MAR-" + fin_year.split("-")[1];

		try {
			String sql = "sELECT  UNIQUE bdt Bmonth,substr(CATEGORY,1,1) cat,substr(CATEGORY,2) subcat,voltage,DESC_HT DESCRIPT ,COUNT(*) NOS,\r\n"
					+ "--SUM(NVL(RUNITS,0)) REC_UNITS,\r\n" + "SUM(NVL(BUNIT,0)) BILLED_UNITS,\r\n"
					+ "SUM(NVL(OFPU,0)) OFF_PEAK_UNITS,\r\n" + "SUM(NVL(PEU,0)) PEAK_UNITS,\r\n"
					+ "SUM(NVL(COL,0)) COLONY_UNITS,\r\n" + "SUM(NVL(EC,0)) ECHG,\r\n" + "SUM(NVL(FIXED,0)) FCHG,\r\n"
					+ "SUM(NVL(CC,0)) CCHG,\r\n" + "--SUM(NVL(ED,0)) EDCHG,\r\n"
					+ "--SUM(NVL(EC,0)+NVL(FIXED,0)+NVL(CC,0)+NVL(ED,0)) DEMAND_CHGS,\r\n"
					+ "SUM(NVL(DEM,0)-NVL(EC,0)-NVL(FIXED,0)-NVL(CC,0)) OTHER_CHGS,\r\n"
					+ "SUM(NVL(CR_AMT,0)+NVL(PAY,0)) COLL,\r\n"
					+ "SUM(NVL(INC,0)) INCENTIVE_AMT,SUM(CTCMD_HT) CMD,SUM(NVL(BMD,0)) RMD\r\n"
					+ "--SUM(NVL(VSUR,0)) VOLTAGE_SURCHARGE_AMT\r\n" + "--SUM(NVL(DEM,0)) MAR_21_DEM\r\n"
					+ " from cons,SPDCLMASTER,HT_TARIFF,\r\n" + "(\r\n"
					+ "SELECT BTSCNO,BTBLCAT,BTBLSCAT,BTACTUAL_KV,TRUNC(BTBLDT,'MM') BDT,NVL(BTRKVA_HT,0) BMD,NVL(BTRKVAH_HT,0) RUNITS,(NVL(BTBKVAH,0)) BUNIT,NVL(BTTOD1,0)+NVL(BTTOD6,0) OPUNITS,NVL(BTTOD2,0)+NVL(BTTOD5,0) PUNITS,NVL(BTBLCOLNY_HT,0) COLUNITS,NVL(BTCURDEM,0)-(NVL(BTCUSTCHG,0)+NVL(BTADLCHG,0)+NVL(BTED,0)+NVL(BTED_INT,0)+NVL(BTDEMCHG_NOR,0)+NVL(BTDEMCHG_PEN,0)+NVL(BTOTHERCHG,0)) EC,\r\n"
					+ "NVL(BTCUSTCHG,0) CC,\r\n" + "NVL(BTDEMCHG_NOR,0)+NVL(BTDEMCHG_PEN,0) FIXED,\r\n"
					+ "NVL(BTED,0) ED,\r\n" + "NVL(BTVOLTSURCHG,0) VSUR,\r\n" + "NVL(BTTOD1,0)+NVL(BTTOD6,0) OFPU,\r\n"
					+ "NVL(BTTOD2,0)+NVL(BTTOD5,0) PEU,\r\n" + "NVL(BTBLCOLNY_HT,0) COL,\r\n"
					+ "--NVL(BTCURDEM,0)-(NVL(BTADLCHG,0)+NVL(BTED_INT,0)) DEMCHG,\r\n"
					+ "--NVL(BTADLCHG,0)+NVL(BTCOURT_LPC,0) SURCHG,\r\n" + "NVL(BTLFINCENTIVE_HT,0) INC,\r\n"
					+ "(NVL(BTCURDEM,0)+NVL(BTCOURT_LPC,0)) DEM FROM BILL_hist where btbldt between ? and ?) B, \r\n"
					+ "(SELECT USCNO,TRUNC(PAY_DATE,'MM') PDT,SUM(NVL(PCMD,0)) PAY FROM PAY_ht_hist WHERE PAY_DATE between ? and ? AND pay_sta_flg='C' GROUP BY USCNO,TRUNC(PAY_DATE,'MM')) P,\r\n"
					+ "(select uscno,TRUNC(RJDT,'MM') RDT,SUM(CASE WHEN RJTYPE='CR'THEN (nvl(ENGCHG,0)+nvl(THEFT,0)+nvl(OTHCHG,0)+nvl(CUSTCHG,0)+nvl(DEMCHG,0)+nvl(ED,0)+nvl(LPC,0)+nvl(EDI,0)+nvl(FSA,0)+NVL(CC_LPC,0)+NVL(CC_OTH,0)) END) CR_AMT  from JOURNAL_HIST WHERE  RJDT between ? and ?   and SUBSTR(USCNO,1,3) IN('GNT','VJA','ONG','CRD') AND TRIM(STATUS) NOT IN ('X') GROUP BY USCNO,TRUNC(RJDT,'MM')) RJ\r\n"
					+ "WHERE substr(ctseccd,-5)=seccd AND CTUSCNO=BTSCNO AND  BDT=RDT(+) AND BDT=PDT(+)  AND CTUSCNO=P.USCNO(+) AND BTBLCAT=SUBSTR(HT_CAT,3) AND BTBLSCAT=HT_SUBCAT and ctactual_kv=voltage AND FROM_DT='01-APR-20'\r\n"
					+ "AND CTUSCNO=RJ.USCNO(+)  GROUP BY BDT,substr(category,1,1),substr(category,2),voltage,DESC_HT ORDER BY 1,2,3,4";
			log.info(sql);
			return jdbcTemplate.queryForList(sql,
					new Object[] { fromdate, todate, fromdate, todate, fromdate, todate });
		} catch (DataAccessException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	// 126
	public List<Map<String, Object>> getFyConsumption2(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		String fromYear = "01-APR-" + request.getParameter("fyear");
		String toYear = "31-MAR-" + request.getParameter("tyear");

		System.out.println(fromYear);

		SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate.getDataSource())
				.withProcedureName("FINANCIAL_YEAR_CONSUMOTION_REPORT");
		Map<String, String> inputs = new HashMap<String, String>();
		inputs.put("CIR", circle);
		inputs.put("FROM_YEAR", fromYear);
		inputs.put("TO_YEAR", toYear);

		log.info("Executing Procedure { exec HT_BILL_CALC ('" + circle + "','" + fromYear + "','" + toYear + "'}'");
		SqlParameterSource in = new MapSqlParameterSource().addValues(inputs);
		Map<String, Object> out = jdbcCall.execute(in);
		log.info("Procedure Result " + out);

		return (List<Map<String, Object>>) out.get("PRC");
	}

	// 127
	public List<Map<String, Object>> getTodConsumptionOfFyReport(HttpServletRequest request) {
		String fin_year = request.getParameter("year");
		String fromdate = "01-APR-" + fin_year.split("-")[0];
		String todate = "31-MAR-" + fin_year.split("-")[1];

		try {
			String sql = "select * from(SELECT FINANCIAL_YEAR,CTCAT,CTSUBCAT,\r\n"
					+ "CTACTUAL_KV,SCS,LOAD,PEAK,OFFPEAK,NOR,COLONY\r\n" + "FROM\r\n"
					+ "(select FINANCIAL_YEAR,CTACTUAL_KV,CTCAT,CTSUBCAT,count(DISTINCT CTUSCNO)SCS, \r\n"
					+ "max(load) LOAD, \r\n" + "sum(NVL(BTTOD2,0)+NVL(BTTOD5,0))PEAK, \r\n"
					+ "sum(NVL(BTTOD3,0)+NVL(BTTOD1,0)) OFFPEAK,  \r\n" + "sum(NVL(BTTOD4,0)+NVL(BTTOD6,0)) NOR, \r\n"
					+ "sum(nvl(BTBLCOLNY_HT,0)) COLONY \r\n" + "from \r\n" + "( \r\n"
					+ "select CTUscno,get_financial_year_BILL(BTBLDT) FINANCIAL_YEAR,CTACTUAL_KV,CTCAT,\r\n"
					+ " CTSUBCAT,\r\n" + "BTTOD2,BTTOD5,BTTOD3,BTTOD1,BTTOD4,BTTOD6,BTBLDT,BTBLCOLNY_HT \r\n"
					+ "from bill_hist,CONS WHERE CTUSCNO=BTSCNO)\r\n"
					+ ",(select uscno,to_number(load) load ,MON_YEAR from LEDGER_HT_HIST where MON_YEAR=\r\n"
					+ "(select to_char(max(to_date(MON_YEAR,'MON-YYYY')),'MON-YYYY')  from LEDGER_HT_HIST where to_date(MON_YEAR,'MON-YYYY') \r\n"
					+ "between to_date(?,'DD-MM-YYYY') and to_date(?,'DD-MM-YYYY'))) \r\n" + "where  \r\n"
					+ "CTUscno = uscno(+) and \r\n"
					+ "BTBLDT between to_date(?,'DD-MM-YY') AND to_date (?,'DD-MM-YY') \r\n"
					+ "group by FINANCIAL_YEAR,CTACTUAL_KV,CTCAT,CTSUBCAT\r\n"
					+ "order by FINANCIAL_YEAR,CTACTUAL_KV,CTCAT,CTSUBCAT))\r\n"
					+ "PIVOT (SUM(SCS) AS SCS,MAX(LOAD) AS LOAD,SUM(PEAK)AS PEAK,SUM(OFFPEAK) AS OFFPEAK ,SUM(NOR) AS NORMAL,SUM(COLONY) AS COLONY \r\n"
					+ "FOR CTACTUAL_KV IN ('11' AS V11,'33' AS V33,'132' AS V132,'220' AS V220))";
			log.info(sql);
			return jdbcTemplate.queryForList(sql, new Object[] { fromdate, todate, fromdate, todate });
		} catch (DataAccessException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	// 128
	public List<Map<String, Object>> getTodConsumptionMonthReport(HttpServletRequest request) {
		String fin_year = request.getParameter("year");
		String fromdate = "01-APR-" + fin_year.split("-")[0];
		String todate = "31-MAR-" + fin_year.split("-")[1];

		try {
			String sql = "select * from (select BILL_MON,CTACTUAL_KV,CTCAT,CTSUBCAT ,count(DISTINCT CTUSCNO) SCS, \r\n"
					+ "max(load) LOAD, \r\n" + "sum(NVL(BTTOD2,0)+NVL(BTTOD5,0))PEAK, \r\n"
					+ "sum(NVL(BTTOD3,0)+NVL(BTTOD1,0)) OFFPEAK,  \r\n"
					+ "sum(NVL(BTTOD4,0)+NVL(BTTOD6,0)) NORMAL , \r\n" + "sum(nvl(BTBLCOLNY_HT,0)) COLONY \r\n"
					+ "from (select CTUscno,to_char(BTBLDT,'MON-YYYY') BILL_MON,CTACTUAL_KV,CTCAT,CTSUBCAT,BTTOD2,BTTOD5,BTTOD3,BTTOD1,BTTOD4,BTTOD6,BTBLDT,BTBLCOLNY_HT \r\n"
					+ "from bill_hist,CONS WHERE CTUSCNO=BTSCNO)\r\n"
					+ ",(select uscno,to_number(load) load from LEDGER_HT_HIST where MON_YEAR=(select to_char(max(to_date(MON_YEAR,'MON-YYYY')),'MON-YYYY')  from LEDGER_HT_HIST where to_date(MON_YEAR,'MON-YYYY') \r\n"
					+ "between to_date(?,'DD-MM-YYYY') and to_date(?,'DD-MM-YYYY'))) \r\n"
					+ "where  CTUscno = uscno(+) and \r\n"
					+ "BTBLDT between to_date(?,'DD-MM-YY') AND to_date (?,'DD-MM-YY') \r\n"
					+ "group by BILL_MON,CTACTUAL_KV,CTCAT,CTSUBCAT)\r\n"
					+ "order by BILL_MON,CTACTUAL_KV,CTCAT,CTSUBCAT";
			log.info(sql);
			return jdbcTemplate.queryForList(sql, new Object[] { fromdate, todate, fromdate, todate });
		} catch (DataAccessException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	// 129
	public List<Map<String, Object>> getFySalesReport(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		String fin_year = request.getParameter("year");
		String fromdate = "01-APR-" + fin_year.split("-")[0];
		String todate = "31-MAR-" + fin_year.split("-")[1];
		String load_year = "MAR-" + fin_year.split("-")[1];

		if (circle.equals("ALL")) {

			try {
				String sql = " select nvl(CIRCLE,'APCPDCL') CIRCLE,nvl(CTCAT,'TOTAL') CTCAT,SUM(SCS)SCS,SUM(CAPACITY)CAPACITY,SUM(SALES_MU)SALES_MU,\r\n"
						+ " SUM(DEMAND_LAKHS) DEMAND_LAKHS,SUM(COLLECTION_LAKHS)COLLECTION_LAKHS,SUM(CB_LAKHS)CB_LAKHS FROM(\r\n"
						+ "SELECT SUBSTR(CTUSCNO,1,3)CIRCLE,case when FINANCIAL_YEAR is null then 'TOTAL' ELSE FINANCIAL_YEAR END FINANCIAL_YEAR,\r\n"
						+ "case when CTCAT is null then 'TOTAL' ELSE CTCAT END CTCAT,COUNT(*)SCS,ROUND(SUM(CAPACITY))CAPACITY,ROUND(SUM(SALES))SALES_MU,\r\n"
						+ "ROUND(SUM(DEMAND))DEMAND_LAKHS,ROUND(SUM(COLLECTION))COLLECTION_LAKHS,ROUND(SUM(CB))CB_LAKHS FROM CONS,\r\n"
						+ "(SELECT USCNO,GET_FINANCIAL_YEAR_LEDGER(MON_YEAR)FINANCIAL_YEAR,ROUND(SUM(MN_KVAH)/1000,2) SALES,\r\n"
						+ "ROUND(SUM(NVL(CMD,0) +NVL(CCLPC,0)+NVL(DRJ,0)+NVL(RJ_CCLPC,0)+NVL(RJ_OTH,0))/100000,2)DEMAND,\r\n"
						+ "ROUND(SUM(NVL(CRJ,0)+NVL(TOT_PAY,0))/100000,2) COLLECTION,round(SUM(Nvl(Cbtot,0)+Nvl(Cb_Oth,0)+Nvl(Cb_Cclpc,0))/100000,2)CB\r\n"
						+ "FROM LEDGER_HT_HIST WHERE TO_DATE(MON_YEAR,'MON-YYYY') BETWEEN TO_DATE(?,'DD-MM-YYYY') AND TO_DATE(?,'DD-MM-YYYY')\r\n"
						+ "GROUP BY USCNO,GET_FINANCIAL_YEAR_LEDGER(MON_YEAR))A,\r\n"
						+ "(SELECT USCNO,SUM(LOAD )CAPACITY FROM LEDGER_HT_HIST WHERE MON_YEAR=? GROUP BY USCNO)B\r\n"
						+ "WHERE CTUSCNO=A.USCNO \r\n" + "AND CTUSCNO=B.USCNO \r\n"
						+ "GROUP BY SUBSTR(CTUSCNO,1,3), FINANCIAL_YEAR, CTCAT\r\n"
						+ "ORDER BY CIRCLE,FINANCIAL_YEAR,CTCAT)\r\n" + "GROUP BY CUBE(CIRCLE,CTCAT)\r\n"
						+ "ORDER BY\r\n"
						+ "case when CIRCLE = 'VJA' then '001' when CIRCLE = 'GNT' then '002' when CIRCLE = 'ONG' then '003'  \r\n"
						+ "when CIRCLE = 'CRD' then '009' else CIRCLE end,CTCAT";
				log.info(sql);
				return jdbcTemplate.queryForList(sql, new Object[] { fromdate, todate, load_year });
			} catch (DataAccessException e) {
				e.printStackTrace();
				log.error(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}
		} else {
			try {
				String sql = "SELECT * FROM(\r\n"
						+ "select  CIRCLE,nvl(CTCAT,'TOTAL') CTCAT,SUM(SCS)SCS,SUM(CAPACITY)CAPACITY,SUM(SALES_MU)SALES_MU,\r\n"
						+ " SUM(DEMAND_LAKHS) DEMAND_LAKHS,SUM(COLLECTION_LAKHS)COLLECTION_LAKHS,SUM(CB_LAKHS)CB_LAKHS FROM(\r\n"
						+ "SELECT SUBSTR(CTUSCNO,1,3)CIRCLE,case when FINANCIAL_YEAR is null then 'TOTAL' ELSE FINANCIAL_YEAR END FINANCIAL_YEAR,\r\n"
						+ "case when CTCAT is null then 'TOTAL' ELSE CTCAT END CTCAT,COUNT(*)SCS,ROUND(SUM(CAPACITY))CAPACITY,ROUND(SUM(SALES))SALES_MU,\r\n"
						+ "ROUND(SUM(DEMAND))DEMAND_LAKHS,ROUND(SUM(COLLECTION))COLLECTION_LAKHS,ROUND(SUM(CB))CB_LAKHS FROM CONS,\r\n"
						+ "(SELECT USCNO,GET_FINANCIAL_YEAR_LEDGER(MON_YEAR)FINANCIAL_YEAR,ROUND(SUM(MN_KVAH)/1000,2) SALES,\r\n"
						+ "ROUND(SUM(NVL(CMD,0) +NVL(CCLPC,0)+NVL(DRJ,0)+NVL(RJ_CCLPC,0)+NVL(RJ_OTH,0))/100000,2)DEMAND,\r\n"
						+ "ROUND(SUM(NVL(CRJ,0)+NVL(TOT_PAY,0))/100000,2) COLLECTION,round(SUM(Nvl(Cbtot,0)+Nvl(Cb_Oth,0)+Nvl(Cb_Cclpc,0))/100000,2)CB\r\n"
						+ "FROM LEDGER_HT_HIST WHERE TO_DATE(MON_YEAR,'MON-YYYY') BETWEEN TO_DATE(?,'DD-MM-YYYY') AND TO_DATE(?,'DD-MM-YYYY')\r\n"
						+ "GROUP BY USCNO,GET_FINANCIAL_YEAR_LEDGER(MON_YEAR))A,\r\n"
						+ "(SELECT USCNO,SUM(LOAD )CAPACITY FROM LEDGER_HT_HIST WHERE MON_YEAR=? GROUP BY USCNO)B\r\n"
						+ "WHERE CTUSCNO=A.USCNO \r\n" + "AND CTUSCNO=B.USCNO \r\n" + "AND SUBSTR(CTUSCNO,1,3)=?\r\n"
						+ "GROUP BY SUBSTR(CTUSCNO,1,3), FINANCIAL_YEAR, CTCAT\r\n"
						+ "ORDER BY CIRCLE,FINANCIAL_YEAR,CTCAT)\r\n" + "GROUP BY ROLLUP(CIRCLE,CTCAT)\r\n"
						+ "ORDER BY\r\n"
						+ "case when CIRCLE = 'VJA' then '001' when CIRCLE = 'GNT' then '002' when CIRCLE = 'ONG' then '003'  \r\n"
						+ "when CIRCLE = 'CRD' then '009' else CIRCLE end,CTCAT)\r\n" + "WHERE CIRCLE IS NOT NULL";
				log.info(sql);
				return jdbcTemplate.queryForList(sql, new Object[] { fromdate, todate, load_year, circle });
			} catch (DataAccessException e) {
				e.printStackTrace();
				log.error(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}
		}
	}

	public List<Map<String, Object>> getFySalesReportForDivision(String circle, String year) {
		String fromdate = "01-APR-" + year.split("-")[0];
		String todate = "31-MAR-" + year.split("-")[1];
		String load_year = "MAR-" + year.split("-")[1];

		if (circle.equals("APCPDCL")) {

			try {
				String sql = "SELECT * FROM (\r\n"
						+ " select DIVNAME,nvl(CTCAT,'TOTAL') CTCAT,SUM(SCS)SCS,SUM(CAPACITY)CAPACITY,SUM(SALES_MU)SALES_MU,\r\n"
						+ " SUM(DEMAND_LAKHS) DEMAND_LAKHS,SUM(COLLECTION_LAKHS)COLLECTION_LAKHS,SUM(CB_LAKHS)CB_LAKHS FROM(\r\n"
						+ "SELECT DIVNAME,case when FINANCIAL_YEAR is null then 'TOTAL' ELSE FINANCIAL_YEAR END FINANCIAL_YEAR,\r\n"
						+ "case when CTCAT is null then 'TOTAL' ELSE CTCAT END CTCAT,COUNT(*)SCS,ROUND(SUM(CAPACITY))CAPACITY,ROUND(SUM(SALES))SALES_MU,\r\n"
						+ "ROUND(SUM(DEMAND))DEMAND_LAKHS,ROUND(SUM(COLLECTION))COLLECTION_LAKHS,ROUND(SUM(CB))CB_LAKHS FROM CONS,MASTER.SPDCLMASTER,\r\n"
						+ "(SELECT USCNO,GET_FINANCIAL_YEAR_LEDGER(MON_YEAR)FINANCIAL_YEAR,ROUND(SUM(MN_KVAH)/1000,2) SALES,\r\n"
						+ "ROUND(SUM(NVL(CMD,0) +NVL(CCLPC,0)+NVL(DRJ,0)+NVL(RJ_CCLPC,0)+NVL(RJ_OTH,0))/100000,2)DEMAND,\r\n"
						+ "ROUND(SUM(NVL(CRJ,0)+NVL(TOT_PAY,0))/100000,2) COLLECTION,round(SUM(Nvl(Cbtot,0)+Nvl(Cb_Oth,0)+Nvl(Cb_Cclpc,0))/100000,2)CB\r\n"
						+ "FROM LEDGER_HT_HIST WHERE TO_DATE(MON_YEAR,'MON-YYYY') BETWEEN TO_DATE(?,'DD-MM-YYYY') AND TO_DATE(?,'DD-MM-YYYY')\r\n"
						+ "GROUP BY USCNO,GET_FINANCIAL_YEAR_LEDGER(MON_YEAR))A,\r\n"
						+ "(SELECT USCNO,SUM(LOAD )CAPACITY FROM LEDGER_HT_HIST WHERE MON_YEAR=? GROUP BY USCNO)B\r\n"
						+ "WHERE CTUSCNO=A.USCNO \r\n" + "AND CTUSCNO=B.USCNO \r\n"
						+ "AND SUBSTR(CTSECCD,-5)=SECCD(+)\r\n" + "GROUP BY DIVNAME, FINANCIAL_YEAR, CTCAT\r\n"
						+ "ORDER BY DIVNAME,FINANCIAL_YEAR,CTCAT)\r\n" + "GROUP BY CUBE(DIVNAME,CTCAT)\r\n"
						+ "ORDER BY\r\n"
						+ "case when DIVNAME = 'VJA' then '001' when DIVNAME = 'GNT' then '002' when DIVNAME = 'ONG' then '003'  \r\n"
						+ "when DIVNAME = 'CRD' then '009' else DIVNAME end,CTCAT) WHERE DIVNAME IS NOT NULL";
				log.info(sql);
				return jdbcTemplate.queryForList(sql, new Object[] { fromdate, todate, load_year });
			} catch (DataAccessException e) {
				e.printStackTrace();
				log.error(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}
		} else {
			try {
				String sql = "SELECT * FROM(\r\n" + "(SELECT DIVNAME,\r\n"
						+ "case when CTCAT is null then 'TOTAL' ELSE CTCAT END CTCAT,COUNT(*)SCS,ROUND(SUM(CAPACITY))CAPACITY,ROUND(SUM(SALES))SALES_MU,\r\n"
						+ "ROUND(SUM(DEMAND))DEMAND_LAKHS,ROUND(SUM(COLLECTION))COLLECTION_LAKHS,ROUND(SUM(CB))CB_LAKHS FROM CONS,MASTER.SPDCLMASTER,\r\n"
						+ "(SELECT USCNO,GET_FINANCIAL_YEAR_LEDGER(MON_YEAR)FINANCIAL_YEAR,ROUND(SUM(MN_KVAH)/1000,2) SALES,\r\n"
						+ "ROUND(SUM(NVL(CMD,0) +NVL(CCLPC,0)+NVL(DRJ,0)+NVL(RJ_CCLPC,0)+NVL(RJ_OTH,0))/100000,2)DEMAND,\r\n"
						+ "ROUND(SUM(NVL(CRJ,0)+NVL(TOT_PAY,0))/100000,2) COLLECTION,round(SUM(Nvl(Cbtot,0)+Nvl(Cb_Oth,0)+Nvl(Cb_Cclpc,0))/100000,2)CB\r\n"
						+ "FROM LEDGER_HT_HIST WHERE TO_DATE(MON_YEAR,'MON-YYYY') BETWEEN TO_DATE(?,'DD-MM-YYYY') AND TO_DATE(?,'DD-MM-YYYY')\r\n"
						+ "GROUP BY USCNO,GET_FINANCIAL_YEAR_LEDGER(MON_YEAR))A,\r\n"
						+ "(SELECT USCNO,SUM(LOAD )CAPACITY FROM LEDGER_HT_HIST WHERE MON_YEAR=? GROUP BY USCNO)B\r\n"
						+ "WHERE CTUSCNO=A.USCNO \r\n" + "AND CTUSCNO=B.USCNO \r\n" + "AND SUBSTR(CTSECCD,-5)=SECCD\r\n"
						+ "AND SUBSTR(CTUSCNO,1,3)=?\r\n" + "GROUP BY ROLLUP(DIVNAME, CTCAT)\r\n"
						+ "ORDER BY DIVNAME,CTCAT)) WHERE DIVNAME IS NOT NULL";
				log.info(sql);
				return jdbcTemplate.queryForList(sql, new Object[] { fromdate, todate, load_year, circle });
			} catch (DataAccessException e) {
				e.printStackTrace();
				log.error(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}
		}
	}

	public List<Map<String, Object>> getFySalesReportForSubDivision(String division, String year) {
		String fromdate = "01-APR-" + year.split("-")[0];
		String todate = "31-MAR-" + year.split("-")[1];
		String load_year = "MAR-" + year.split("-")[1];

		try {
			String sql = "SELECT * FROM(\r\n" + "(SELECT SUBNAME,\r\n"
					+ "case when CTCAT is null then 'TOTAL' ELSE CTCAT END CTCAT,COUNT(*)SCS,ROUND(SUM(CAPACITY))CAPACITY,ROUND(SUM(SALES))SALES_MU,\r\n"
					+ "ROUND(SUM(DEMAND))DEMAND_LAKHS,ROUND(SUM(COLLECTION))COLLECTION_LAKHS,ROUND(SUM(CB))CB_LAKHS FROM CONS,MASTER.SPDCLMASTER,\r\n"
					+ "(SELECT USCNO,GET_FINANCIAL_YEAR_LEDGER(MON_YEAR)FINANCIAL_YEAR,ROUND(SUM(MN_KVAH)/1000,2) SALES,\r\n"
					+ "ROUND(SUM(NVL(CMD,0) +NVL(CCLPC,0)+NVL(DRJ,0)+NVL(RJ_CCLPC,0)+NVL(RJ_OTH,0))/100000,2)DEMAND,\r\n"
					+ "ROUND(SUM(NVL(CRJ,0)+NVL(TOT_PAY,0))/100000,2) COLLECTION,round(SUM(Nvl(Cbtot,0)+Nvl(Cb_Oth,0)+Nvl(Cb_Cclpc,0))/100000,2)CB\r\n"
					+ "FROM LEDGER_HT_HIST WHERE TO_DATE(MON_YEAR,'MON-YYYY') BETWEEN TO_DATE(?,'DD-MM-YYYY') AND TO_DATE(?,'DD-MM-YYYY')\r\n"
					+ "GROUP BY USCNO,GET_FINANCIAL_YEAR_LEDGER(MON_YEAR))A,\r\n"
					+ "(SELECT USCNO,SUM(LOAD )CAPACITY FROM LEDGER_HT_HIST WHERE MON_YEAR=? GROUP BY USCNO)B\r\n"
					+ "WHERE CTUSCNO=A.USCNO \r\n" + "AND CTUSCNO=B.USCNO \r\n" + "AND SUBSTR(CTSECCD,-5)=SECCD\r\n"
					+ "AND DIVNAME=?\r\n" + "GROUP BY ROLLUP(SUBNAME, CTCAT)\r\n"
					+ "ORDER BY SUBNAME,CTCAT)) WHERE SUBNAME IS NOT NULL";
			log.info(sql);
			return jdbcTemplate.queryForList(sql, new Object[] { fromdate, todate, load_year, division });
		} catch (DataAccessException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			e.printStackTrace();
			return Collections.emptyList();
		}

	}

	public List<Map<String, Object>> getFySalesReportForSection(String subDivision, String year) {
		String fromdate = "01-APR-" + year.split("-")[0];
		String todate = "31-MAR-" + year.split("-")[1];
		String load_year = "MAR-" + year.split("-")[1];

		try {
			String sql = "SELECT * FROM(\r\n" + "(SELECT SECNAME,\r\n"
					+ "case when CTCAT is null then 'TOTAL' ELSE CTCAT END CTCAT,COUNT(*)SCS,ROUND(SUM(CAPACITY))CAPACITY,ROUND(SUM(SALES))SALES_MU,\r\n"
					+ "ROUND(SUM(DEMAND))DEMAND_LAKHS,ROUND(SUM(COLLECTION))COLLECTION_LAKHS,ROUND(SUM(CB))CB_LAKHS FROM CONS,MASTER.SPDCLMASTER,\r\n"
					+ "(SELECT USCNO,GET_FINANCIAL_YEAR_LEDGER(MON_YEAR)FINANCIAL_YEAR,ROUND(SUM(MN_KVAH)/1000,2) SALES,\r\n"
					+ "ROUND(SUM(NVL(CMD,0) +NVL(CCLPC,0)+NVL(DRJ,0)+NVL(RJ_CCLPC,0)+NVL(RJ_OTH,0))/100000,2)DEMAND,\r\n"
					+ "ROUND(SUM(NVL(CRJ,0)+NVL(TOT_PAY,0))/100000,2) COLLECTION,round(SUM(Nvl(Cbtot,0)+Nvl(Cb_Oth,0)+Nvl(Cb_Cclpc,0))/100000,2)CB\r\n"
					+ "FROM LEDGER_HT_HIST WHERE TO_DATE(MON_YEAR,'MON-YYYY') BETWEEN TO_DATE(?,'DD-MM-YYYY') AND TO_DATE(?,'DD-MM-YYYY')\r\n"
					+ "GROUP BY USCNO,GET_FINANCIAL_YEAR_LEDGER(MON_YEAR))A,\r\n"
					+ "(SELECT USCNO,SUM(LOAD )CAPACITY FROM LEDGER_HT_HIST WHERE MON_YEAR=? GROUP BY USCNO)B\r\n"
					+ "WHERE CTUSCNO=A.USCNO \r\n" + "AND CTUSCNO=B.USCNO \r\n" + "AND SUBSTR(CTSECCD,-5)=SECCD\r\n"
					+ "AND SUBNAME=?\r\n" + "GROUP BY ROLLUP(SECNAME, CTCAT)\r\n"
					+ "ORDER BY SECNAME,CTCAT)) WHERE SECNAME IS NOT NULL";
			log.info(sql);
			return jdbcTemplate.queryForList(sql, new Object[] { fromdate, todate, load_year, subDivision });
		} catch (DataAccessException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	// 130
	public List<Map<String, Object>> getMonthSalesReport(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		String fin_year = request.getParameter("year");
		String fromdate = "01-APR-" + fin_year.split("-")[0];
		String todate = "31-MAR-" + fin_year.split("-")[1];

		if (circle.equals("ALL")) {

			try {
				String sql = "SELECT * FROM(\r\n"
						+ "SELECT SUBSTR(CTUSCNO,1,3)CIRCLE,MON_YEAR,CASE WHEN CTCAT IS NULL THEN 'TOTAL' ELSE CTCAT END CTCAT,COUNT(*)SCS,SUM(CAPACITY)CAPACITY,SUM(SALES)SALES_MU,SUM(DEMAND)DEMAND_LAKHS,SUM(COLLECTION)COLLECTION_LAKHS,SUM(CB)CB_LAKHS FROM CONS,\r\n"
						+ "(SELECT USCNO,MON_YEAR,SUM(LOAD)CAPACITY,ROUND(SUM(MN_KVAH)/1000,2) SALES,ROUND(SUM(NVL(CMD,0) +NVL(CCLPC,0)+NVL(DRJ,0)+NVL(RJ_CCLPC,0)+NVL(RJ_OTH,0))/100000,2)DEMAND,\r\n"
						+ "ROUND(SUM(NVL(CRJ,0)+NVL(TOT_PAY,0))/100000,2) COLLECTION,round(SUM(Nvl(Cbtot,0)+Nvl(Cb_Oth,0)+Nvl(Cb_Cclpc,0))/100000,2)CB\r\n"
						+ "FROM LEDGER_HT_HIST WHERE TO_DATE(MON_YEAR,'MON-YYYY') BETWEEN TO_DATE(?,'DD-MM-YYYY') AND TO_DATE(?,'DD-MM-YYYY')\r\n"
						+ "GROUP BY USCNO,MON_YEAR)A\r\n" + "WHERE CTUSCNO=A.USCNO\r\n"
						+ "GROUP BY CUBE(SUBSTR(CTUSCNO,1,3),MON_YEAR,CTCAT)\r\n"
						+ "ORDER BY CIRCLE,TO_DATE(MON_YEAR,'MON-YYYY'),CTCAT)\r\n"
						+ "WHERE CIRCLE IS NOT NULL AND MON_YEAR IS NOT NULL";
				log.info(sql);
				return jdbcTemplate.queryForList(sql, new Object[] { fromdate, todate });
			} catch (DataAccessException e) {
				e.printStackTrace();
				log.error(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}
		} else {

			try {
				String sql = "SELECT * FROM(\r\n"
						+ "SELECT SUBSTR(CTUSCNO,1,3)CIRCLE,MON_YEAR,CASE WHEN CTCAT IS NULL THEN 'TOTAL' ELSE CTCAT END CTCAT,COUNT(*)SCS,SUM(CAPACITY)CAPACITY,SUM(SALES)SALES_MU,SUM(DEMAND)DEMAND_LAKHS,SUM(COLLECTION)COLLECTION_LAKHS,SUM(CB)CB_LAKHS FROM CONS,\r\n"
						+ "(SELECT USCNO,MON_YEAR,SUM(LOAD)CAPACITY,ROUND(SUM(MN_KVAH)/1000,2) SALES,ROUND(SUM(NVL(CMD,0) +NVL(CCLPC,0)+NVL(DRJ,0)+NVL(RJ_CCLPC,0)+NVL(RJ_OTH,0))/100000,2)DEMAND,\r\n"
						+ "ROUND(SUM(NVL(CRJ,0)+NVL(TOT_PAY,0))/100000,2) COLLECTION,round(SUM(Nvl(Cbtot,0)+Nvl(Cb_Oth,0)+Nvl(Cb_Cclpc,0))/100000,2)CB\r\n"
						+ "FROM LEDGER_HT_HIST WHERE TO_DATE(MON_YEAR,'MON-YYYY') BETWEEN TO_DATE(?,'DD-MM-YYYY') AND TO_DATE(?,'DD-MM-YYYY')\r\n"
						+ "GROUP BY USCNO,MON_YEAR)A\r\n" + "WHERE CTUSCNO=A.USCNO\r\n"
						+ "AND SUBSTR(CTUSCNO,1,3)=?\r\n" + "GROUP BY CUBE(SUBSTR(CTUSCNO,1,3),MON_YEAR,CTCAT)\r\n"
						+ "ORDER BY CIRCLE,TO_DATE(MON_YEAR,'MON-YYYY'),CTCAT)\r\n"
						+ "WHERE CIRCLE IS NOT NULL AND MON_YEAR IS NOT NULL";
				log.info(sql);
				return jdbcTemplate.queryForList(sql, new Object[] { fromdate, todate, circle });
			} catch (DataAccessException e) {
				e.printStackTrace();
				log.error(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}
		}
	}

	// 102A
	public List<Map<String, Object>> getVoltagewiseFinancialYearAbstract(HttpServletRequest request) {
		String fin_year = request.getParameter("year");
		String fromdate = "01-04-" + fin_year.split("-")[0];
		String todate = "31-03-" + fin_year.split("-")[1];
		String circle = request.getParameter("circle");
		String division = request.getParameter("division");
		String subdivision = request.getParameter("subdivision");
		String voltage = request.getParameter("voltage");

		try {
			StringBuilder sqlBuilder = new StringBuilder();
			sqlBuilder.append("SELECT UNIQUE MON_YEAR, substr(ctuscno,1,3) circle,\r\n");
			sqlBuilder.append("case when divcd is null then 'TOTAL' ELSE divcd END divcd,\r\n");
			sqlBuilder.append("case when divname is null then 'TOTAL' ELSE DIVNAME END DIVNAME,\r\n");
			sqlBuilder.append("CASE WHEN subcd IS NULL THEN 'TOTAL' ELSE subcd END subcd,\r\n");
			sqlBuilder.append("CASE WHEN subname IS NULL THEN 'TOTAL' ELSE subname END subname, \r\n");
			sqlBuilder.append(
					"CASE WHEN CTACTUAL_KV IS NULL THEN 'TOTAL' ELSE TO_CHAR(CTACTUAL_KV) END CTACTUAL_KV,\r\n");
			sqlBuilder.append("CTACTUAL_KV VOLTAGE,\r\n");
			sqlBuilder.append("count(distinct(ctuscno)) NOS, \r\n");
			sqlBuilder.append("SUM(Round(Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0))) Ob,\r\n");
			sqlBuilder.append("SUM(Mn_Kvah) Sales,\r\n");
			sqlBuilder.append("SUM(round(Nvl(Cmd,0)+Nvl(Cclpc,0))) Demand,\r\n");
			sqlBuilder.append("SUM(Nvl(round(CASE WHEN Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)>0 THEN\r\n");
			sqlBuilder.append(
					"CASE WHEN Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)>(NVL(Tot_Pay,0)) THEN (NVL(Tot_Pay,0)) ELSE Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0) END END),0)) COLL_ARREAR,\r\n");
			sqlBuilder.append("SUM(Nvl(round(CASE WHEN Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)>0 THEN\r\n");
			sqlBuilder.append(
					"CASE WHEN Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)<(NVL(Tot_Pay,0)) THEN (NVL(Tot_Pay,0)-(Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0))) END ELSE (NVL(Tot_Pay,0)) END ),0)) COLL_DEMAND,\r\n");
			sqlBuilder.append(
					"SUM(round(Nvl(Tot_Pay,0))) Collection,SUM(round(Nvl(Rj_Oth,0)+Nvl(Drj,0)+Nvl(Rj_Cclpc,0))) Drj,SUM(round(Nvl(Crj,0))) Crj,\r\n");
			sqlBuilder.append(
					"SUM(round(Nvl(Cbtot,0)+Nvl(Cb_Oth,0)+Nvl(Cb_Cclpc,0))) Cb FROM ledger_ht_hist a, cons b, MASTER.SPDCLMASTER\r\n");
			sqlBuilder.append(
					"WHERE TO_DATE(MON_YEAR,'MON-YYYY') BETWEEN TO_DATE(?,'DD-MM-YYYY') AND TO_DATE(?,'DD-MM-YYYY')\r\n");

			List<Object> params = new ArrayList<>();
			params.add(fromdate);
			params.add(todate);

			if (circle != null && !circle.isEmpty()) {
				sqlBuilder.append("AND substr(ctuscno,1,3) = ? \r\n");
				params.add(circle);
			}

			if (division != null && !division.isEmpty() && !division.equals("0")) {
				sqlBuilder.append("AND divcd = ? \r\n");
				params.add(division);
			} else {
				sqlBuilder.append("");
			}

			if (subdivision != null && !subdivision.isEmpty() && !subdivision.equals("0")) {
				sqlBuilder.append("AND subcd = ?\r\n");
				params.add(subdivision);
			} else {
				sqlBuilder.append("");
			}

			if (voltage != null && !voltage.isEmpty() && !voltage.equals("ALL")) {
				sqlBuilder.append("AND b.CTACTUAL_KV = ? \r\n");
				params.add(voltage);
			} else {
				sqlBuilder.append("");
			}

			sqlBuilder.append("AND A.Uscno = B.CTUscno  \r\n");
			sqlBuilder.append("AND SUBSTR(CTSECCD,-5) = SECCD\r\n");
			sqlBuilder.append(
					"GROUP BY  substr(ctuscno,1,3),GROUPING SETS((mon_year,divcd,divname,SUBCD,subname,CTACTUAL_KV),(mon_year,divcd,divname,SUBCD,subname),\r\n");
			sqlBuilder.append("(mon_year,divcd,divname),(MON_YEAR)) \r\n");
			sqlBuilder.append("ORDER BY TO_DATE(MON_YEAR,'MON-YYYY'), CIRCLE, divcd, subcd, CTACTUAL_KV");

			String sql = sqlBuilder.toString();
			log.info(sql);

			return jdbcTemplate.queryForList(sql, params.toArray());
		} catch (DataAccessException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return Collections.emptyList();
		}

	}

	// 103B
	public List<Map<String, Object>> getFeederwiseFYConsumption(HttpServletRequest request) {
		String fin_year = request.getParameter("year");
		String fromdate = "01-APR-" + fin_year.split("-")[0];
		String todate = "31-MAR-" + fin_year.split("-")[1];
		String feederCode = request.getParameter("feeder");
		String circle = request.getParameter("circle");
		String division = request.getParameter("division");
		String subdivision = request.getParameter("subdivision");

		System.out.println(division);
		System.out.println(subdivision);

		try {
			StringBuilder sqlBuilder = new StringBuilder();
			sqlBuilder.append("Select UNIQUE mon_year,SUBSTR(CTUSCNO,1,3)CIRCLE,\r\n");
			sqlBuilder.append("case when divcd is null then 'TOTAL' ELSE divcd END divcd,\r\n");
			sqlBuilder.append("case when divname is null then 'TOTAL' ELSE DIVNAME END DIVNAME,\r\n");
			sqlBuilder.append("CASE WHEN subcd IS NULL THEN 'TOTAL' ELSE subcd END subcd,\r\n");
			sqlBuilder.append("CASE WHEN subname IS NULL THEN 'TOTAL' ELSE subname END subname,\r\n");
			sqlBuilder.append("CASE WHEN fmsapfcode IS NULL THEN 'TOTAL' ELSE fmsapfcode END fmsapfcode,\r\n");
			sqlBuilder.append("CASE WHEN FMFNAME IS NULL THEN 'TOTAL' ELSE FMFNAME END FMFNAME,\r\n");
			sqlBuilder.append("Count(distinct(ctuscno)) NOS,\r\n");
			sqlBuilder.append("sum(nvl(mn_kvah,0)) sales,\r\n");
			sqlBuilder.append("SUM(nvl(REC_KWH,0)) KWH_UNITS,\r\n");
			sqlBuilder.append("SUM(nvl(Mn_Kvah,0)) BKVA_UNITS,\r\n");
			sqlBuilder.append("SUM(Round(Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0))) Ob,\r\n");
			sqlBuilder.append("SUM(round(Nvl(Cmd,0)+Nvl(Cclpc,0))) Demand,\r\n");
			sqlBuilder.append("SUM(Nvl(round(CASE WHEN Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)>0 THEN\r\n");
			sqlBuilder.append(
					"CASE WHEN Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)>(NVL(Tot_Pay,0)) THEN (NVL(Tot_Pay,0)) ELSE Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0) END END),0)) COLL_ARREAR,\r\n");
			sqlBuilder.append("SUM(Nvl(round(CASE WHEN Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)>0 THEN\r\n");
			sqlBuilder.append(
					"CASE WHEN Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)<(NVL(Tot_Pay,0)) THEN (NVL(Tot_Pay,0)-(Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0))) END ELSE (NVL(Tot_Pay,0)) END ),0)) COLL_DEMAND,\r\n");
			sqlBuilder.append(
					"SUM(round(Nvl(Tot_Pay,0))) Collection,SUM(round(Nvl(Rj_Oth,0)+Nvl(Drj,0)+Nvl(Rj_Cclpc,0))) Drj,SUM(round(Nvl(Crj,0))) Crj,\r\n");
			sqlBuilder.append(
					"SUM(round(Nvl(Cbtot,0)+Nvl(Cb_Oth,0)+Nvl(Cb_Cclpc,0))) Cb from ledger_ht_hist a,cons b,feedermast,MASTER.SPDCLMASTER\r\n");
			sqlBuilder.append(
					"where TO_DATE(MON_YEAR,'MON-YYYY') BETWEEN TO_DATE(?,'DD-MM-YYYY') AND TO_DATE(?,'DD-MM-YYYY')\r\n");
			sqlBuilder.append("and  A.Uscno=B.CTUscno and ctfeeder_code=fmsapfcode AND SUBSTR(CTSECCD,-5)=SECCD\r\n");

			List<Object> params = new ArrayList<>();
			params.add(fromdate);
			params.add(todate);

			if (circle != null && !circle.isEmpty()) {
				sqlBuilder.append("AND SUBSTR(CTUSCNO,1,3)=? \r\n");
				params.add(circle);
			}

			if (division != null && !division.isEmpty() && !division.equals("0")) {
				sqlBuilder.append("AND DIVCD=? \r\n");
				params.add(division);
			} else {
				sqlBuilder.append("");
			}

			if (subdivision != null && !subdivision.isEmpty() && !subdivision.equals("0")) {
				sqlBuilder.append("AND SUBCD=? \r\n");
				params.add(subdivision);
			} else {
				sqlBuilder.append("");
			}

			if (feederCode != null && !feederCode.isEmpty() && !feederCode.equals("ALL")) {
				sqlBuilder.append("AND ctfeeder_code=? \r\n");
				params.add(feederCode);
			} else {
				sqlBuilder.append("");
			}

			sqlBuilder.append(
					"GROUP BY mon_year,SUBSTR(CTUSCNO,1,3),DIVCD,divname,SUBCD,subname,FMFNAME,fmsapfcode \r\n");
			sqlBuilder.append("Order By to_date(mon_year,'MON-YYYY'),cirCLE,divname,subname,fmsapfcode");

			String sql = sqlBuilder.toString();
			log.info(sql);

			return jdbcTemplate.queryForList(sql, params.toArray());
		} catch (DataAccessException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return Collections.emptyList();
		}
	}

	public List<Map<String, Object>> getFeederWiseConsumptionForNOS(String circle, String division, String subdiv,
			String year, String feedercode) {

		String fyear = "01-04-" + year.split("-")[0];
		String tyear = "31-03-" + year.split("-")[1];
		try {
			StringBuilder sqlBuilder = new StringBuilder();
			sqlBuilder.append("Select UNIQUE ctuscno,sum(nvl(mn_kvah,0)) sales,SUM(nvl(REC_KWH,0)) KWH_UNITS,\r\n");
			sqlBuilder.append(
					"SUM(nvl(Mn_Kvah,0)) BKVA_UNITS,SUM(Round(Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0))) Ob,\r\n");
			sqlBuilder.append("SUM(round(Nvl(Cmd,0)+Nvl(Cclpc,0))) Demand,\r\n");
			sqlBuilder.append("SUM(Nvl(round(CASE WHEN Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)>0 THEN\r\n");
			sqlBuilder.append(
					"CASE WHEN Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)>(NVL(Tot_Pay,0)) THEN (NVL(Tot_Pay,0)) ELSE Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0) END END),0)) COLL_ARREAR, \r\n");
			sqlBuilder.append("SUM(Nvl(round(CASE WHEN Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)>0 THEN\r\n");
			sqlBuilder.append(
					"CASE WHEN Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)<(NVL(Tot_Pay,0)) THEN (NVL(Tot_Pay,0)-(Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0))) END ELSE (NVL(Tot_Pay,0)) END ),0)) COLL_DEMAND,\r\n");
			sqlBuilder.append(
					"SUM(round(Nvl(Tot_Pay,0))) Collection,SUM(round(Nvl(Rj_Oth,0)+Nvl(Drj,0)+Nvl(Rj_Cclpc,0))) Drj,SUM(round(Nvl(Crj,0))) Crj, \r\n");
			sqlBuilder.append(
					"SUM(round(Nvl(Cbtot,0)+Nvl(Cb_Oth,0)+Nvl(Cb_Cclpc,0))) Cb from ledger_ht_hist a,cons b,feedermast,MASTER.SPDCLMASTER\r\n");
			sqlBuilder.append(
					"where TO_DATE(MON_YEAR,'MON-YYYY') BETWEEN TO_DATE(?,'DD-MM-YYYY') AND TO_DATE(?,'DD-MM-YYYY')\r\n");
			sqlBuilder.append("and  A.Uscno=B.CTUscno and ctfeeder_code=fmsapfcode AND SUBSTR(CTSECCD,-5)=SECCD\r\n");

			List<Object> params = new ArrayList<>();
			params.add(fyear);
			params.add(tyear);

			if (circle != null && !circle.isEmpty()) {
				sqlBuilder.append("AND SUBSTR(CTUSCNO,1,3)=?\r\n");
				params.add(circle);
			}

			if (division != null && !division.isEmpty() && !division.equals("0")) {
				sqlBuilder.append("AND DIVNAME=? \r\n");
				params.add(division);
			} else {
				sqlBuilder.append("");
			}

			if (subdiv != null && !subdiv.isEmpty() && !subdiv.equals("0")) {
				sqlBuilder.append("AND SUBNAME=?\r\n");
				params.add(subdiv);
			} else {
				sqlBuilder.append("");
			}

			if (feedercode != null && !feedercode.isEmpty() && !feedercode.equals("ALL")) {
				sqlBuilder.append("AND ctfeeder_code=? \r\n");
				params.add(feedercode);
			} else {
				sqlBuilder.append("");
			}

			sqlBuilder.append("GROUP BY ctuscno\r\n");
			sqlBuilder.append("Order By ctuscno\r\n");

			String sql = sqlBuilder.toString();
			log.info(sql);

			return jdbcTemplate.queryForList(sql, params.toArray());
		} catch (DataAccessException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return Collections.emptyList();
		}
	}

	// 97C1
	public List<Map<String, Object>> gethtDCBCollectionSplitFYWise(HttpServletRequest request) {
		String fin_year = request.getParameter("year");
		String fromdate = "01-04-" + fin_year.split("-")[0];
		String todate = "31-03-" + fin_year.split("-")[1];
		String circle = request.getParameter("circle");

		if (circle.equals("ALL")) {
			try {
				String sql = "SELECT  UNIQUE LDT,SUBSTR(CTUSCNO,1,3) CIRNAME,COUNT(*)NOS,SUM(NVL(MN_KVAH,0))MN_KVAH,SUM(NVL(OB,0)) TOB,SUM(NVL(DEM,0)+NVL(DR_AMT,0)) DEMAND,\r\n"
						+ "SUM(CASE WHEN NVL(OB,0)>0 THEN CASE WHEN NVL(OB,0)>(NVL(PAY,0)+NVL(CR_AMT,0)) THEN (NVL(PAY,0)+NVL(CR_AMT,0)) ELSE NVL(OB,0) END END) COLL_ARREAR,\r\n"
						+ "SUM(CASE WHEN NVL(OB,0)>0 THEN CASE WHEN NVL(OB,0)<(NVL(PAY,0)+NVL(CR_AMT,0)) THEN (NVL(PAY,0)+NVL(CR_AMT,0)-NVL(OB,0)) END ELSE (NVL(PAY,0)+NVL(CR_AMT,0)) END )COLL_DEMAND,\r\n"
						+ "SUM(NVL(PAY,0)+NVL(CR_AMT,0)) COLLECTION, SUM(NVL(CB,0)) CB, SUM(NVL(CB_SD,0)) SD \r\n"
						+ "FROM CONS,\r\n"
						+ "(SELECT  USCNO,GET_FINANCIAL_YEAR_LEDGER(MON_YEAR) LDT,SUM(NVL(MN_KVAH,0))MN_KVAH,SUM((Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0))) OB,\r\n"
						+ "SUM((NVL(TOT_PAY,0))) PAY,SUM((NVL(CMD,0)+NVL(CCLPC,0))) DEM,SUM(NVL(CBTOT,0)+NVL(CB_CCLPC,0)+NVL(CB_OTH,0)) CB ,SUM(NVL(CB_SD,0)) CB_SD \r\n"
						+ "FROM LEDGER_HT_HIST WHERE TRUNC(TO_DATE(MON_YEAR,'MON-YYYY'),'MM') BETWEEN TO_DATE(?,'DD-MM-YYYY') AND \r\n"
						+ "TO_DATE(?,'DD-MM-YYYY') AND SUBSTR(USCNO,1,3) IN('GNT','VJA','ONG','CRD') GROUP BY USCNO,GET_FINANCIAL_YEAR_LEDGER(MON_YEAR)) L,\r\n"
						+ "(select uscno,GET_FINANCIAL_YEAR_BILL(rjdt) RDT,SUM(CASE WHEN RJTYPE='DR' THEN \r\n"
						+ "(nvl(ENGCHG,0)+nvl(THEFT,0)+nvl(OTHCHG,0)+nvl(CUSTCHG,0)+nvl(DEMCHG,0)+nvl(ED,0)+nvl(LPC,0)+nvl(EDI,0)+nvl(FSA,0)+NVL(CC_LPC,0)+NVL(CC_OTH,0)) END) DR_AMT,\r\n"
						+ "SUM(CASE WHEN RJTYPE='CR'THEN \r\n"
						+ "(nvl(ENGCHG,0)+nvl(THEFT,0)+nvl(OTHCHG,0)+nvl(CUSTCHG,0)+nvl(DEMCHG,0)+nvl(ED,0)+nvl(LPC,0)+nvl(EDI,0)+nvl(FSA,0)+NVL(CC_LPC,0)+NVL(CC_OTH,0)) END) CR_AMT \r\n"
						+ "from JOURNAL_HIST WHERE TRUNC(rjdt,'MM') BETWEEN TO_DATE(?,'DD-MM-YYYY') AND TO_DATE(?,'DD-MM-YYYY') \r\n"
						+ "and SUBSTR(USCNO,1,3) IN('GNT','VJA','ONG','CRD') AND TRIM(STATUS) NOT IN ('X','E') GROUP BY USCNO,GET_FINANCIAL_YEAR_BILL(rjdt)) RJ\r\n"
						+ "WHERE CTUSCNO=L.USCNO AND L.USCNO=RJ.USCNO(+) AND LDT=RDT(+) \r\n"
						+ "GROUP BY LDT,SUBSTR(CTUSCNO,1,3)\r\n" + "ORDER BY LDT";
				log.info(sql);
				return jdbcTemplate.queryForList(sql, new Object[] { fromdate, todate, fromdate, todate });
			} catch (DataAccessException e) {
				e.printStackTrace();
				log.error(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}
		} else {
			try {
				String sql = "SELECT  UNIQUE LDT,SUBSTR(CTUSCNO,1,3) CIRNAME,COUNT(*)NOS,SUM(NVL(MN_KVAH,0))MN_KVAH,SUM(NVL(OB,0)) TOB,SUM(NVL(DEM,0)+NVL(DR_AMT,0)) DEMAND,\r\n"
						+ "SUM(CASE WHEN NVL(OB,0)>0 THEN CASE WHEN NVL(OB,0)>(NVL(PAY,0)+NVL(CR_AMT,0)) THEN (NVL(PAY,0)+NVL(CR_AMT,0)) ELSE NVL(OB,0) END END) COLL_ARREAR,\r\n"
						+ "SUM(CASE WHEN NVL(OB,0)>0 THEN CASE WHEN NVL(OB,0)<(NVL(PAY,0)+NVL(CR_AMT,0)) THEN (NVL(PAY,0)+NVL(CR_AMT,0)-NVL(OB,0)) END ELSE (NVL(PAY,0)+NVL(CR_AMT,0)) END )COLL_DEMAND,\r\n"
						+ "SUM(NVL(PAY,0)+NVL(CR_AMT,0)) COLLECTION, SUM(NVL(CB,0)) CB, SUM(NVL(CB_SD,0)) SD \r\n"
						+ "FROM CONS,\r\n"
						+ "(SELECT  USCNO,GET_FINANCIAL_YEAR_LEDGER(MON_YEAR) LDT,SUM(NVL(MN_KVAH,0))MN_KVAH,SUM((Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0))) OB,\r\n"
						+ "SUM((NVL(TOT_PAY,0))) PAY,SUM((NVL(CMD,0)+NVL(CCLPC,0))) DEM,SUM(NVL(CBTOT,0)+NVL(CB_CCLPC,0)+NVL(CB_OTH,0)) CB ,SUM(NVL(CB_SD,0)) CB_SD \r\n"
						+ "FROM LEDGER_HT_HIST WHERE TRUNC(TO_DATE(MON_YEAR,'MON-YYYY'),'MM') BETWEEN TO_DATE(?,'DD-MM-YYYY') AND \r\n"
						+ "TO_DATE(?,'DD-MM-YYYY') AND SUBSTR(USCNO,1,3) IN('GNT','VJA','ONG','CRD') GROUP BY USCNO,GET_FINANCIAL_YEAR_LEDGER(MON_YEAR)) L,\r\n"
						+ "(select uscno,GET_FINANCIAL_YEAR_BILL(rjdt) RDT,SUM(CASE WHEN RJTYPE='DR' THEN \r\n"
						+ "(nvl(ENGCHG,0)+nvl(THEFT,0)+nvl(OTHCHG,0)+nvl(CUSTCHG,0)+nvl(DEMCHG,0)+nvl(ED,0)+nvl(LPC,0)+nvl(EDI,0)+nvl(FSA,0)+NVL(CC_LPC,0)+NVL(CC_OTH,0)) END) DR_AMT,\r\n"
						+ "SUM(CASE WHEN RJTYPE='CR'THEN \r\n"
						+ "(nvl(ENGCHG,0)+nvl(THEFT,0)+nvl(OTHCHG,0)+nvl(CUSTCHG,0)+nvl(DEMCHG,0)+nvl(ED,0)+nvl(LPC,0)+nvl(EDI,0)+nvl(FSA,0)+NVL(CC_LPC,0)+NVL(CC_OTH,0)) END) CR_AMT \r\n"
						+ "from JOURNAL_HIST WHERE TRUNC(rjdt,'MM') BETWEEN TO_DATE(?,'DD-MM-YYYY') AND TO_DATE(?,'DD-MM-YYYY') \r\n"
						+ "and SUBSTR(USCNO,1,3) IN('GNT','VJA','ONG','CRD') AND TRIM(STATUS) NOT IN ('X','E') GROUP BY USCNO,GET_FINANCIAL_YEAR_BILL(rjdt)) RJ\r\n"
						+ "WHERE CTUSCNO=L.USCNO AND L.USCNO=RJ.USCNO(+) AND LDT=RDT(+) \r\n"
						+ "AND SUBSTR(CTUSCNO,1,3)=? \r\n" + "GROUP BY LDT,SUBSTR(CTUSCNO,1,3)\r\n" + "ORDER BY LDT";
				System.out.println("ddddddddd");
				System.out.println(fromdate + todate + circle);
				log.info(sql);
				return jdbcTemplate.queryForList(sql, new Object[] { fromdate, todate, fromdate, todate, circle });
			} catch (DataAccessException e) {
				e.printStackTrace();
				log.error(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}

		}
	}

	// 131
	public List<Map<String, Object>> getHtSolarMonthReport(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		String date = request.getParameter("month") + "-" + request.getParameter("year");
		String status = request.getParameter("status");

		if (circle.equals("ALL")) {
			try {
				String sql = "select nvl(CIRCLE,'APCPDCL') CIRCLE,nvl(TYPE,'TOTAL') TYPE, nvl(CIRCLE,'APCPDCL')||nvl(TYPE,'TOTAL') CIR_TYPE ,nvl(CTCAT,'TOTAL') CTCAT,STATUS,SUM(SCS)SCS,\r\n"
						+ "SUM(CAPACITY)CAPACITY,SUM(SALES)SALES_MU,SUM(DEMAND)DEMAND_LAKHS,SUM(COLLECTION)COLLECTION_LAKHS,SUM(CB)CB_LAKHS FROM(\r\n"
						+ "\r\n"
						+ "SELECT SUBSTR(CTUSCNO,1,3)CIRCLE,MON_YEAR,DECODE(CTGOVT_PVT,'Y','GOVT','N','NON-GOVT')TYPE,CTCAT,DECODE(CTSTATUS,'1','LIVE','0','BILLSTOP')STATUS,COUNT(*)SCS,\r\n"
						+ "SUM(CAPACITY)CAPACITY,SUM(SALES)SALES,SUM(DEMAND)DEMAND,SUM(COLLECTION)COLLECTION,SUM(CB)CB FROM CONS,\r\n"
						+ "\r\n"
						+ "(SELECT USCNO,MON_YEAR,SUM(LOAD)CAPACITY,ROUND(SUM(MN_KVAH)/1000,2) SALES,ROUND(SUM(NVL(CMD,0) +NVL(CCLPC,0)+NVL(DRJ,0)+NVL(RJ_CCLPC,0)+NVL(RJ_OTH,0))/100000,2)DEMAND,\r\n"
						+ "ROUND(SUM(NVL(CRJ,0)+NVL(TOT_PAY,0))/100000,2) COLLECTION,round(SUM(Nvl(Cbtot,0)+Nvl(Cb_Oth,0)+Nvl(Cb_Cclpc,0))/100000,2)CB\r\n"
						+ "FROM LEDGER_HT_HIST WHERE MON_YEAR=?\r\n" + "GROUP BY USCNO,MON_YEAR)A\r\n"
						+ "WHERE CTUSCNO=A.USCNO \r\n" + "AND NVL(CTSOLAR_FLAG,'N')='Y'\r\n" + "AND CTSTATUS=?\r\n"
						+ "GROUP BY SUBSTR(CTUSCNO,1,3),MON_YEAR,CTCAT,DECODE(CTSTATUS,'1','LIVE','0','BILLSTOP'),DECODE(CTGOVT_PVT,'Y','GOVT','N','NON-GOVT')\r\n"
						+ "ORDER BY TO_DATE(MON_YEAR,'MON-YYYY'),CTCAT,TYPE)\r\n"
						+ "GROUP BY CUBE (CIRCLE,TYPE,CTCAT),STATUS\r\n" + "ORDER BY\r\n"
						+ "						case when CIRCLE = 'VJA' then '001' when CIRCLE = 'GNT' then '002' when CIRCLE = 'ONG' then '003'  \r\n"
						+ "            when CIRCLE = 'CRD' then '009' else CIRCLE end,case when TYPE='GOVT' then '001' when TYPE = 'NON-GOVT' then '002' else TYPE end,CTCAT";
				log.info(sql);
				return jdbcTemplate.queryForList(sql, new Object[] { date, status });
			} catch (DataAccessException e) {
				e.printStackTrace();
				log.error(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}
		} else {
			try {
				String sql = "SELECT * FROM(\r\n"
						+ "select CIRCLE,nvl(TYPE,'TOTAL') TYPE, nvl(CIRCLE,'APCPDCL')||nvl(TYPE,'TOTAL') CIR_TYPE ,nvl(CTCAT,'TOTAL') CTCAT,STATUS,SUM(SCS)SCS,\r\n"
						+ "SUM(CAPACITY)CAPACITY,SUM(SALES)SALES_MU,SUM(DEMAND)DEMAND_LAKHS,SUM(COLLECTION)COLLECTION_LAKHS,SUM(CB)CB_LAKHS FROM(\r\n"
						+ "\r\n"
						+ "SELECT SUBSTR(CTUSCNO,1,3)CIRCLE,MON_YEAR,DECODE(CTGOVT_PVT,'Y','GOVT','N','NON-GOVT')TYPE,CTCAT,DECODE(CTSTATUS,'1','LIVE','0','BILLSTOP')STATUS,COUNT(*)SCS,\r\n"
						+ "SUM(CAPACITY)CAPACITY,SUM(SALES)SALES,SUM(DEMAND)DEMAND,SUM(COLLECTION)COLLECTION,SUM(CB)CB FROM CONS,\r\n"
						+ "\r\n"
						+ "(SELECT USCNO,MON_YEAR,SUM(LOAD)CAPACITY,ROUND(SUM(MN_KVAH)/1000,2) SALES,ROUND(SUM(NVL(CMD,0) +NVL(CCLPC,0)+NVL(DRJ,0)+NVL(RJ_CCLPC,0)+NVL(RJ_OTH,0))/100000,2)DEMAND,\r\n"
						+ "ROUND(SUM(NVL(CRJ,0)+NVL(TOT_PAY,0))/100000,2) COLLECTION,round(SUM(Nvl(Cbtot,0)+Nvl(Cb_Oth,0)+Nvl(Cb_Cclpc,0))/100000,2)CB\r\n"
						+ "FROM LEDGER_HT_HIST WHERE MON_YEAR=?\r\n" + "GROUP BY USCNO,MON_YEAR)A\r\n" + "\r\n"
						+ "WHERE CTUSCNO=A.USCNO \r\n" + "AND NVL(CTSOLAR_FLAG,'N')='Y'\r\n" + "AND CTSTATUS=?\r\n"
						+ "AND SUBSTR(CTUSCNO,1,3)=?\r\n"
						+ "GROUP BY SUBSTR(CTUSCNO,1,3),MON_YEAR,CTCAT,DECODE(CTSTATUS,'1','LIVE','0','BILLSTOP'),DECODE(CTGOVT_PVT,'Y','GOVT','N','NON-GOVT')\r\n"
						+ "ORDER BY TO_DATE(MON_YEAR,'MON-YYYY'),CTCAT,TYPE)\r\n"
						+ "GROUP BY CUBE (CIRCLE,TYPE,CTCAT),STATUS\r\n"
						+ "ORDER BY case when CIRCLE = 'VJA' then '001' when CIRCLE = 'GNT' then '002' when CIRCLE = 'ONG' then '003'  \r\n"
						+ "         when CIRCLE = 'CRD' then '009' else CIRCLE end,case when TYPE='GOVT' then '001' when TYPE = 'NON-GOVT' then '002' else TYPE end,CTCAT)\r\n"
						+ "         WHERE CIRCLE IS NOT NULL";
				log.info(sql);
				return jdbcTemplate.queryForList(sql, new Object[] { date, status, circle });
			} catch (DataAccessException e) {
				e.printStackTrace();
				log.error(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}

		}

	}

	public List<Map<String, Object>> getHtCategoryWiseDivisionWiseSolarReport(String circle, String mon_year) {
		if (circle.equalsIgnoreCase("APCPDCL")) {
			try {
				String sql = "SELECT * FROM(\r\n"
						+ "select CIRCLE,nvl(TYPE,'TOTAL') TYPE, NVL(CIRCLE,'APCPDCL')||nvl(TYPE,'TOTAL') CIR_TYPE ,nvl(CTCAT,'TOTAL') CTCAT,STATUS,SUM(SCS)SCS,\r\n"
						+ "SUM(CAPACITY)CAPACITY,SUM(SALES)SALES_MU,SUM(DEMAND)DEMAND_LAKHS,SUM(COLLECTION)COLLECTION_LAKHS,SUM(CB)CB_LAKHS FROM(\r\n"
						+ "\r\n"
						+ "SELECT DIVNAME CIRCLE,MON_YEAR,DECODE(CTGOVT_PVT,'Y','GOVT','N','NON-GOVT')TYPE,CTCAT,DECODE(CTSTATUS,'1','LIVE','0','BILLSTOP')STATUS,COUNT(*)SCS,\r\n"
						+ "SUM(CAPACITY)CAPACITY,SUM(SALES)SALES,SUM(DEMAND)DEMAND,SUM(COLLECTION)COLLECTION,SUM(CB)CB FROM CONS,MASTER.SPDCLMASTER,\r\n"
						+ "(SELECT USCNO,MON_YEAR,SUM(LOAD)CAPACITY,ROUND(SUM(MN_KVAH)/1000,2) SALES,ROUND(SUM(NVL(CMD,0) +NVL(CCLPC,0)+NVL(DRJ,0)+NVL(RJ_CCLPC,0)+NVL(RJ_OTH,0))/100000,2)DEMAND,\r\n"
						+ "ROUND(SUM(NVL(CRJ,0)+NVL(TOT_PAY,0))/100000,2) COLLECTION,round(SUM(Nvl(Cbtot,0)+Nvl(Cb_Oth,0)+Nvl(Cb_Cclpc,0))/100000,2)CB\r\n"
						+ "FROM LEDGER_HT_HIST WHERE MON_YEAR=?\r\n" + "GROUP BY USCNO,MON_YEAR)A\r\n"
						+ "WHERE CTUSCNO=A.USCNO \r\n" + "AND NVL(CTSOLAR_FLAG,'N')='Y'\r\n"
						+ "AND SUBSTR(CTSECCD,-5)=SECCD(+)\r\n"
						+ "GROUP BY DIVNAME,MON_YEAR,CTCAT,DECODE(CTSTATUS,'1','LIVE','0','BILLSTOP'),DECODE(CTGOVT_PVT,'Y','GOVT','N','NON-GOVT'))\r\n"
						+ "--ORDER BY DIVNAME,TO_DATE(MON_YEAR,'MON-YYYY'),CTCAT,TYPE)\r\n"
						+ "GROUP BY CUBE (CIRCLE,TYPE,CTCAT),STATUS\r\n" + "ORDER BY\r\n"
						+ "CIRCLE,case when TYPE='GOVT' then '001' when TYPE = 'NON-GOVT' then '002' else TYPE end,CTCAT) WHERE CIRCLE IS NOT NULL";
				log.info(sql);

				log.info(sql);
				return jdbcTemplate.queryForList(sql, new Object[] { mon_year });
			} catch (DataAccessException e) {
				e.printStackTrace();
				log.error(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}
		} else {
			try {
				String sql = "SELECT * FROM(\r\n"
						+ "select CIRCLE,nvl(TYPE,'TOTAL') TYPE, NVL(CIRCLE,'APCPDCL')||nvl(TYPE,'TOTAL') CIR_TYPE ,nvl(CTCAT,'TOTAL') CTCAT,STATUS,SUM(SCS)SCS,\r\n"
						+ "SUM(CAPACITY)CAPACITY,SUM(SALES)SALES_MU,SUM(DEMAND)DEMAND_LAKHS,SUM(COLLECTION)COLLECTION_LAKHS,SUM(CB)CB_LAKHS FROM(\r\n"
						+ "\r\n"
						+ "SELECT DIVNAME CIRCLE,MON_YEAR,DECODE(CTGOVT_PVT,'Y','GOVT','N','NON-GOVT')TYPE,CTCAT,DECODE(CTSTATUS,'1','LIVE','0','BILLSTOP')STATUS,COUNT(*)SCS,\r\n"
						+ "SUM(CAPACITY)CAPACITY,SUM(SALES)SALES,SUM(DEMAND)DEMAND,SUM(COLLECTION)COLLECTION,SUM(CB)CB FROM CONS,MASTER.SPDCLMASTER,\r\n"
						+ "\r\n"
						+ "(SELECT USCNO,MON_YEAR,SUM(LOAD)CAPACITY,ROUND(SUM(MN_KVAH)/1000,2) SALES,ROUND(SUM(NVL(CMD,0) +NVL(CCLPC,0)+NVL(DRJ,0)+NVL(RJ_CCLPC,0)+NVL(RJ_OTH,0))/100000,2)DEMAND,\r\n"
						+ "ROUND(SUM(NVL(CRJ,0)+NVL(TOT_PAY,0))/100000,2) COLLECTION,round(SUM(Nvl(Cbtot,0)+Nvl(Cb_Oth,0)+Nvl(Cb_Cclpc,0))/100000,2)CB\r\n"
						+ "FROM LEDGER_HT_HIST WHERE MON_YEAR=?\r\n" + "GROUP BY USCNO,MON_YEAR)A\r\n"
						+ "WHERE CTUSCNO=A.USCNO \r\n" + "AND NVL(CTSOLAR_FLAG,'N')='Y'\r\n"
						+ "AND SUBSTR(CTUSCNO,1,3)=?\r\n" + "AND SUBSTR(CTSECCD,-5)=SECCD(+)\r\n"
						+ "GROUP BY DIVNAME,MON_YEAR,CTCAT,DECODE(CTSTATUS,'1','LIVE','0','BILLSTOP'),DECODE(CTGOVT_PVT,'Y','GOVT','N','NON-GOVT'))\r\n"
						+ "--ORDER BY DIVNAME,TO_DATE(MON_YEAR,'MON-YYYY'),CTCAT,TYPE)\r\n"
						+ "GROUP BY CUBE (CIRCLE,TYPE,CTCAT),STATUS\r\n" + "ORDER BY\r\n"
						+ "CIRCLE,case when TYPE='GOVT' then '001' when TYPE = 'NON-GOVT' then '002' else TYPE end,CTCAT) WHERE CIRCLE IS NOT NULL";
				log.info(sql);
				return jdbcTemplate.queryForList(sql, new Object[] { mon_year, circle });
			} catch (DataAccessException e) {
				e.printStackTrace();
				log.error(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}

		}
	}

	public List<Map<String, Object>> getHtCategoryWiseSubDivisionWiseDemandReport(String circle, String year) {
		if (circle.equalsIgnoreCase("APCPDCL")) {
			try {
				String sql = "SELECT * FROM(\r\n"
						+ "select CIRCLE,nvl(TYPE,'TOTAL') TYPE, NVL(CIRCLE,'APCPDCL')||nvl(TYPE,'TOTAL') CIR_TYPE ,nvl(CTCAT,'TOTAL') CTCAT,STATUS,SUM(SCS)SCS,\r\n"
						+ "SUM(CAPACITY)CAPACITY,SUM(SALES)SALES_MU,SUM(DEMAND)DEMAND_LAKHS,SUM(COLLECTION)COLLECTION_LAKHS,SUM(CB)CB_LAKHS FROM(\r\n"
						+ "\r\n"
						+ "SELECT SUBNAME CIRCLE,MON_YEAR,DECODE(CTGOVT_PVT,'Y','GOVT','N','NON-GOVT')TYPE,CTCAT,DECODE(CTSTATUS,'1','LIVE','0','BILLSTOP')STATUS,COUNT(*)SCS,\r\n"
						+ "SUM(CAPACITY)CAPACITY,SUM(SALES)SALES,SUM(DEMAND)DEMAND,SUM(COLLECTION)COLLECTION,SUM(CB)CB FROM CONS,MASTER.SPDCLMASTER,\r\n"
						+ "\r\n"
						+ "(SELECT USCNO,MON_YEAR,SUM(LOAD)CAPACITY,ROUND(SUM(MN_KVAH)/1000,2) SALES,ROUND(SUM(NVL(CMD,0) +NVL(CCLPC,0)+NVL(DRJ,0)+NVL(RJ_CCLPC,0)+NVL(RJ_OTH,0))/100000,2)DEMAND,\r\n"
						+ "ROUND(SUM(NVL(CRJ,0)+NVL(TOT_PAY,0))/100000,2) COLLECTION,round(SUM(Nvl(Cbtot,0)+Nvl(Cb_Oth,0)+Nvl(Cb_Cclpc,0))/100000,2)CB\r\n"
						+ "FROM LEDGER_HT_HIST WHERE MON_YEAR=?\r\n" + "GROUP BY USCNO,MON_YEAR)A\r\n"
						+ "WHERE CTUSCNO=A.USCNO \r\n" + "AND NVL(CTSOLAR_FLAG,'N')='Y'\r\n"
						+ "AND SUBSTR(CTSECCD,-5)=SECCD(+)\r\n"
						+ "GROUP BY SUBNAME,MON_YEAR,CTCAT,DECODE(CTSTATUS,'1','LIVE','0','BILLSTOP'),DECODE(CTGOVT_PVT,'Y','GOVT','N','NON-GOVT')\r\n"
						+ "ORDER BY DIVNAME,TO_DATE(MON_YEAR,'MON-YYYY'),CTCAT,TYPE)\r\n"
						+ "GROUP BY CUBE (CIRCLE,TYPE,CTCAT),STATUS\r\n" + "ORDER BY\r\n"
						+ "CIRCLE,case when TYPE='GOVT' then '001' when TYPE = 'NON-GOVT' then '002' else TYPE end,CTCAT) WHERE CIRCLE IS NOT NULL";
				log.info(sql);

				log.info(sql);
				return jdbcTemplate.queryForList(sql, new Object[] { year });
			} catch (DataAccessException e) {
				e.printStackTrace();
				log.error(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}
		} else {
			try {
				String sql = "SELECT * FROM(\r\n"
						+ "select CIRCLE,nvl(TYPE,'TOTAL') TYPE, NVL(CIRCLE,'APCPDCL')||nvl(TYPE,'TOTAL') CIR_TYPE ,nvl(CTCAT,'TOTAL') CTCAT,STATUS,SUM(SCS)SCS,\r\n"
						+ "SUM(CAPACITY)CAPACITY,SUM(SALES)SALES_MU,SUM(DEMAND)DEMAND_LAKHS,SUM(COLLECTION)COLLECTION_LAKHS,SUM(CB)CB_LAKHS FROM(\r\n"
						+ "\r\n"
						+ "SELECT SUBNAME CIRCLE,MON_YEAR,DECODE(CTGOVT_PVT,'Y','GOVT','N','NON-GOVT')TYPE,CTCAT,DECODE(CTSTATUS,'1','LIVE','0','BILLSTOP')STATUS,COUNT(*)SCS,\r\n"
						+ "SUM(CAPACITY)CAPACITY,SUM(SALES)SALES,SUM(DEMAND)DEMAND,SUM(COLLECTION)COLLECTION,SUM(CB)CB FROM CONS,MASTER.SPDCLMASTER,\r\n"
						+ "\r\n"
						+ "(SELECT USCNO,MON_YEAR,SUM(LOAD)CAPACITY,ROUND(SUM(MN_KVAH)/1000,2) SALES,ROUND(SUM(NVL(CMD,0) +NVL(CCLPC,0)+NVL(DRJ,0)+NVL(RJ_CCLPC,0)+NVL(RJ_OTH,0))/100000,2)DEMAND,\r\n"
						+ "ROUND(SUM(NVL(CRJ,0)+NVL(TOT_PAY,0))/100000,2) COLLECTION,round(SUM(Nvl(Cbtot,0)+Nvl(Cb_Oth,0)+Nvl(Cb_Cclpc,0))/100000,2)CB\r\n"
						+ "FROM LEDGER_HT_HIST WHERE MON_YEAR=?\r\n" + "GROUP BY USCNO,MON_YEAR)A\r\n"
						+ "WHERE CTUSCNO=A.USCNO \r\n" + "AND NVL(CTSOLAR_FLAG,'N')='Y'\r\n" + "AND DIVNAME=?\r\n"
						+ "AND SUBSTR(CTSECCD,-5)=SECCD(+)\r\n"
						+ "GROUP BY SUBNAME,MON_YEAR,CTCAT,DECODE(CTSTATUS,'1','LIVE','0','BILLSTOP'),DECODE(CTGOVT_PVT,'Y','GOVT','N','NON-GOVT')\r\n"
						+ "ORDER BY DIVNAME,TO_DATE(MON_YEAR,'MON-YYYY'),CTCAT,TYPE)\r\n"
						+ "GROUP BY CUBE (CIRCLE,TYPE,CTCAT),STATUS\r\n" + "ORDER BY\r\n"
						+ "CIRCLE,case when TYPE='GOVT' then '001' when TYPE = 'NON-GOVT' then '002' else TYPE end,CTCAT) WHERE CIRCLE IS NOT NULL";
				log.info(sql);
				return jdbcTemplate.queryForList(sql, new Object[] { year, circle });
			} catch (DataAccessException e) {
				e.printStackTrace();
				log.error(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}

		}

	}

	// 132
	public List<Map<String, Object>> getOpenAccessReport(HttpServletRequest request) {
		String fyear[] = request.getParameter("year").split("-");

		try {
			String sql = ("SELECT * FROM\r\n"
					+ "(SELECT SUBSTR(OAUSCNO,1,3)CIRCLE,BILL_MON||'-'|| BILL_YEAR MON_YEAR,count(*)scs,SUM(KVAH_ADJ_ENG)OA_CONSUMPTION FROM OPEN_ACCESS_HIST \r\n"
					+ "WHERE TO_DATE(BILL_MON||'-'|| BILL_YEAR,'MON-YYYY') BETWEEN TO_DATE('01-04-FI','DD-MM-YYYY') AND TO_DATE('31-03-SI','DD-MM-YYYY')\r\n"
					+ "AND SUBSTR(OAUSCNO,1,3) IN ('CRD','GNT','ONG','VJA') GROUP BY SUBSTR(OAUSCNO,1,3),BILL_MON||'-'|| BILL_YEAR \r\n"
					+ "ORDER BY SUBSTR(OAUSCNO,1,3),TO_DATE(BILL_MON||'-'|| BILL_YEAR,'MON-YYYY') asc\r\n" + ")\r\n"
					+ "PIVOT\r\n" + "(\r\n" + "  sum(scs) as scs,SUM(OA_CONSUMPTION) as OA_CONSUMPTION\r\n"
					+ "  FOR MON_YEAR IN ('APR-FI' APR_FI,'MAY-FI'MAY_FI,'JUN-FI'JUN_FI,'JUL-FI'JUL_FI,'AUG-FI'AUG_FI,'SEP-FI'SEP_FI,\r\n"
					+ "  'OCT-FI'OCT_FI,'NOV-FI'NOV_FI,'DEC-FI'DEC_FI,'JAN-SI'JAN_SI,'FEB-SI'FEB_SI,'MAR-SI' MAR_SI)\r\n"
					+ ")\r\n" + "ORDER BY CIRCLE").replace("FI", fyear[0]).replace("SI", fyear[1]);
			log.info(sql);
			return jdbcTemplate.queryForList(sql, new Object[] {});
		} catch (Exception e) {
			log.info(e.getMessage());
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	public List<Map<String, Object>> getOpenAccessReportForDivision(String circle, String year) {
		String fyear[] = year.split("-");

		try {
			String sql = ("SELECT * FROM\r\n"
					+ "(SELECT DIVNAME,BILL_MON||'-'|| BILL_YEAR MON_YEAR,count(*)scs,SUM(KVAH_ADJ_ENG)OA_CONSUMPTION FROM OPEN_ACCESS_HIST,CONS,MASTER.SPDCLMASTER\r\n"
					+ "WHERE CTUSCNO=OAUSCNO AND SUBSTR(CTSECCD,-5)=SECCD AND\r\n"
					+ "TO_DATE(BILL_MON||'-'|| BILL_YEAR,'MON-YYYY') BETWEEN TO_DATE('01-04-FI','DD-MM-YYYY') AND TO_DATE('31-03-SI','DD-MM-YYYY')\r\n"
					+ "AND SUBSTR(OAUSCNO,1,3) IN ('CRD','GNT','ONG','VJA') and SUBSTR(OAUSCNO,1,3)=?\r\n"
					+ "GROUP BY DIVNAME,BILL_MON||'-'|| BILL_YEAR \r\n"
					+ "ORDER BY DIVNAME,TO_DATE(BILL_MON||'-'|| BILL_YEAR,'MON-YYYY')\r\n" + ")\r\n" + "PIVOT\r\n"
					+ "(\r\n" + "  sum(scs) as scs,SUM(OA_CONSUMPTION) as OA_CONSUMPTION\r\n"
					+ "  FOR MON_YEAR IN ('APR-FI' APR_FI,'MAY-FI'MAY_FI,'JUN-FI'JUN_FI,'JUL-FI'JUL_FI,'AUG-FI'AUG_FI,'SEP-FI'SEP_FI,\r\n"
					+ "  'OCT-FI'OCT_FI,'NOV-FI'NOV_FI,'DEC-FI'DEC_FI,'JAN-SI'JAN_SI,'FEB-SI'FEB_SI,'MAR-SI' MAR_SI)\r\n"
					+ ")\r\n" + "ORDER BY DIVNAME").replace("FI", fyear[0]).replace("SI", fyear[1]);
			log.info(sql);
			return jdbcTemplate.queryForList(sql, new Object[] { circle });
		} catch (Exception e) {
			log.info(e.getMessage());
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	public List<Map<String, Object>> getOpenAccessReportForSubDivision(String division, String year) {
		String fyear[] = year.split("-");

		try {
			String sql = ("SELECT * FROM\r\n"
					+ "(SELECT SUBNAME,BILL_MON||'-'|| BILL_YEAR MON_YEAR,count(*)scs,SUM(KVAH_ADJ_ENG)OA_CONSUMPTION FROM OPEN_ACCESS_HIST,CONS,MASTER.SPDCLMASTER\r\n"
					+ "WHERE CTUSCNO=OAUSCNO AND SUBSTR(CTSECCD,-5)=SECCD AND\r\n"
					+ "TO_DATE(BILL_MON||'-'|| BILL_YEAR,'MON-YYYY') BETWEEN TO_DATE('01-04-FI','DD-MM-YYYY') AND TO_DATE('31-03-SI','DD-MM-YYYY')\r\n"
					+ "AND SUBSTR(OAUSCNO,1,3) IN ('CRD','GNT','ONG','VJA') and DIVNAME=?\r\n"
					+ "GROUP BY SUBNAME,BILL_MON||'-'|| BILL_YEAR \r\n"
					+ "ORDER BY SUBNAME,TO_DATE(BILL_MON||'-'|| BILL_YEAR,'MON-YYYY')\r\n" + ")\r\n" + "PIVOT\r\n"
					+ "(\r\n" + "  sum(scs) as scs,SUM(OA_CONSUMPTION) as OA_CONSUMPTION\r\n"
					+ "  FOR MON_YEAR IN ('APR-FI' APR_FI,'MAY-FI'MAY_FI,'JUN-FI'JUN_FI,'JUL-FI'JUL_FI,'AUG-FI'AUG_FI,'SEP-FI'SEP_FI,\r\n"
					+ "  'OCT-FI'OCT_FI,'NOV-FI'NOV_FI,'DEC-FI'DEC_FI,'JAN-SI'JAN_SI,'FEB-SI'FEB_SI,'MAR-SI' MAR_SI)\r\n"
					+ ")\r\n" + "ORDER BY SUBNAME").replace("FI", fyear[0]).replace("SI", fyear[1]);
			log.info(sql);
			return jdbcTemplate.queryForList(sql, new Object[] { division });
		} catch (Exception e) {
			log.info(e.getMessage());
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	public List<Map<String, Object>> getOpenAccessReportForSection(String subDivision, String year) {
		String fyear[] = year.split("-");

		try {
			String sql = ("SELECT * FROM\r\n"
					+ "(SELECT SECNAME,BILL_MON||'-'|| BILL_YEAR MON_YEAR,count(*)scs,SUM(KVAH_ADJ_ENG)OA_CONSUMPTION FROM OPEN_ACCESS_HIST,CONS,MASTER.SPDCLMASTER\r\n"
					+ "WHERE CTUSCNO=OAUSCNO AND SUBSTR(CTSECCD,-5)=SECCD AND\r\n"
					+ "TO_DATE(BILL_MON||'-'|| BILL_YEAR,'MON-YYYY') BETWEEN TO_DATE('01-04-FI','DD-MM-YYYY') AND TO_DATE('31-03-SI','DD-MM-YYYY')\r\n"
					+ "AND SUBSTR(OAUSCNO,1,3) IN ('CRD','GNT','ONG','VJA') and SUBNAME=?\r\n"
					+ "GROUP BY SECNAME,BILL_MON||'-'|| BILL_YEAR \r\n"
					+ "ORDER BY SECNAME,TO_DATE(BILL_MON||'-'|| BILL_YEAR,'MON-YYYY')\r\n" + ")\r\n" + "PIVOT\r\n"
					+ "(\r\n" + "  sum(scs) as scs,SUM(OA_CONSUMPTION) as OA_CONSUMPTION\r\n"
					+ "  FOR MON_YEAR IN ('APR-FI' APR_FI,'MAY-FI'MAY_FI,'JUN-FI'JUN_FI,'JUL-FI'JUL_FI,'AUG-FI'AUG_FI,'SEP-FI'SEP_FI,\r\n"
					+ "  'OCT-FI'OCT_FI,'NOV-FI'NOV_FI,'DEC-FI'DEC_FI,'JAN-SI'JAN_SI,'FEB-SI'FEB_SI,'MAR-SI' MAR_SI)\r\n"
					+ ")\r\n" + "ORDER BY SECNAME").replace("FI", fyear[0]).replace("SI", fyear[1]);
			log.info(sql);
			return jdbcTemplate.queryForList(sql, new Object[] { subDivision });
		} catch (Exception e) {
			log.info(e.getMessage());
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	// 133
	public List<Map<String, Object>> getOpenAccessCrossSubsidyReport(HttpServletRequest request) {
		String fyear[] = request.getParameter("year").split("-");

		try {
			String sql = ("SELECT * FROM\r\n" + "(\r\n"
					+ "SELECT SUBSTR(OAUSCNO,1,3)CIRCLE,BILL_MON||'-'|| BILL_YEAR MON_YEAR,count(*)scs,SUM(CS_CHARGES)CS_CHARGES FROM OPEN_ACCESS_HIST\r\n"
					+ "WHERE TO_DATE(BILL_MON||'-'|| BILL_YEAR,'MON-YYYY') BETWEEN TO_DATE('01-04-FI','DD-MM-YYYY') AND TO_DATE('31-03-SI','DD-MM-YYYY')\r\n"
					+ "AND SUBSTR(OAUSCNO,1,3) IN ('CRD','GNT','ONG','VJA') GROUP BY SUBSTR(OAUSCNO,1,3),BILL_MON||'-'|| BILL_YEAR\r\n"
					+ "ORDER BY SUBSTR(OAUSCNO,1,3),TO_DATE(BILL_MON||'-'|| BILL_YEAR,'MON-YYYY')asc\r\n" + ")\r\n"
					+ "PIVOT\r\n" + "(\r\n" + "  sum(scs) as scs,SUM(CS_CHARGES) as CS_CHARGES\r\n"
					+ "  FOR MON_YEAR IN ('APR-FI' APR_FI,'MAY-FI'MAY_FI,'JUN-FI'JUN_FI,'JUL-FI'JUL_FI,'AUG-FI'AUG_FI,'SEP-FI'SEP_FI,\r\n"
					+ "  'OCT-FI'OCT_FI,'NOV-FI'NOV_FI,'DEC-FI'DEC_FI,'JAN-SI'JAN_SI,'FEB-SI'FEB_SI,'MAR-SI' MAR_SI)\r\n"
					+ ")\r\n" + "ORDER BY CIRCLE").replace("FI", fyear[0]).replace("SI", fyear[1]);
			log.info(sql);
			return jdbcTemplate.queryForList(sql, new Object[] {});
		} catch (Exception e) {
			log.info(e.getMessage());
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	public List<Map<String, Object>> getOpenAccessCrossSubsidyReportForDivision(String circle, String year) {
		String fyear[] = year.split("-");

		try {
			String sql = ("SELECT * FROM\r\n" + "(\r\n"
					+ "SELECT DIVNAME,BILL_MON||'-'|| BILL_YEAR MON_YEAR,count(*)scs,SUM(CS_CHARGES)CS_CHARGES FROM OPEN_ACCESS_HIST,CONS,MASTER.SPDCLMASTER\r\n"
					+ "WHERE TO_DATE(BILL_MON||'-'|| BILL_YEAR,'MON-YYYY') BETWEEN TO_DATE('01-04-FI','DD-MM-YYYY') AND TO_DATE('31-03-SI','DD-MM-YYYY')\r\n"
					+ "AND SUBSTR(OAUSCNO,1,3) IN ('CRD','GNT','ONG','VJA') AND CTUSCNO=OAUSCNO AND SUBSTR(CTSECCD,-5)=SECCD AND SUBSTR(OAUSCNO,1,3)=?\r\n"
					+ "GROUP BY DIVNAME,BILL_MON||'-'|| BILL_YEAR\r\n"
					+ "ORDER BY DIVNAME,TO_DATE(BILL_MON||'-'|| BILL_YEAR,'MON-YYYY')asc\r\n" + ")\r\n" + "PIVOT\r\n"
					+ "(\r\n" + "  sum(scs) as scs,SUM(CS_CHARGES) as CS_CHARGES\r\n"
					+ "  FOR MON_YEAR IN ('APR-FI' APR_FI,'MAY-FI'MAY_FI,'JUN-FI'JUN_FI,'JUL-FI'JUL_FI,'AUG-FI'AUG_FI,'SEP-FI'SEP_FI,\r\n"
					+ "  'OCT-FI'OCT_FI,'NOV-FI'NOV_FI,'DEC-FI'DEC_FI,'JAN-SI'JAN_SI,'FEB-SI'FEB_SI,'MAR-SI' MAR_SI)\r\n"
					+ ")\r\n" + "ORDER BY DIVNAME").replace("FI", fyear[0]).replace("SI", fyear[1]);
			log.info(sql);
			return jdbcTemplate.queryForList(sql, new Object[] { circle });
		} catch (Exception e) {
			log.info(e.getMessage());
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	public List<Map<String, Object>> getOpenAccessCrossSubsidyReportSubDivision(String division, String year) {
		String fyear[] = year.split("-");

		try {
			String sql = ("SELECT * FROM\r\n" + "(\r\n"
					+ "SELECT SUBNAME,BILL_MON||'-'|| BILL_YEAR MON_YEAR,count(*)scs,SUM(CS_CHARGES)CS_CHARGES FROM OPEN_ACCESS_HIST,CONS,MASTER.SPDCLMASTER\r\n"
					+ "WHERE TO_DATE(BILL_MON||'-'|| BILL_YEAR,'MON-YYYY') BETWEEN TO_DATE('01-04-FI','DD-MM-YYYY') AND TO_DATE('31-03-SI','DD-MM-YYYY')\r\n"
					+ "AND SUBSTR(OAUSCNO,1,3) IN ('CRD','GNT','ONG','VJA') AND CTUSCNO=OAUSCNO AND SUBSTR(CTSECCD,-5)=SECCD AND DIVNAME=?\r\n"
					+ "GROUP BY SUBNAME,BILL_MON||'-'|| BILL_YEAR\r\n"
					+ "ORDER BY SUBNAME,TO_DATE(BILL_MON||'-'|| BILL_YEAR,'MON-YYYY')asc\r\n" + ")\r\n" + "PIVOT\r\n"
					+ "(\r\n" + "  sum(scs) as scs,SUM(CS_CHARGES) as CS_CHARGES\r\n"
					+ "  FOR MON_YEAR IN ('APR-FI' APR_FI,'MAY-FI'MAY_FI,'JUN-FI'JUN_FI,'JUL-FI'JUL_FI,'AUG-FI'AUG_FI,'SEP-FI'SEP_FI,\r\n"
					+ "  'OCT-FI'OCT_FI,'NOV-FI'NOV_FI,'DEC-FI'DEC_FI,'JAN-SI'JAN_SI,'FEB-SI'FEB_SI,'MAR-SI' MAR_SI)\r\n"
					+ ")\r\n" + "ORDER BY SUBNAME").replace("FI", fyear[0]).replace("SI", fyear[1]);
			log.info(sql);
			return jdbcTemplate.queryForList(sql, new Object[] { division });
		} catch (Exception e) {
			log.info(e.getMessage());
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	public List<Map<String, Object>> getOpenAccessCrossSubsidyReportForSection(String subDivision, String year) {
		String fyear[] = year.split("-");

		try {
			String sql = ("SELECT * FROM\r\n" + "(\r\n"
					+ "SELECT SECNAME,BILL_MON||'-'|| BILL_YEAR MON_YEAR,count(*)scs,SUM(CS_CHARGES)CS_CHARGES FROM OPEN_ACCESS_HIST,CONS,MASTER.SPDCLMASTER\r\n"
					+ "WHERE TO_DATE(BILL_MON||'-'|| BILL_YEAR,'MON-YYYY') BETWEEN TO_DATE('01-04-FI','DD-MM-YYYY') AND TO_DATE('31-03-SI','DD-MM-YYYY')\r\n"
					+ "AND SUBSTR(OAUSCNO,1,3) IN ('CRD','GNT','ONG','VJA') AND CTUSCNO=OAUSCNO AND SUBSTR(CTSECCD,-5)=SECCD AND SUBNAME=?\r\n"
					+ "GROUP BY SECNAME,BILL_MON||'-'|| BILL_YEAR\r\n"
					+ "ORDER BY SECNAME,TO_DATE(BILL_MON||'-'|| BILL_YEAR,'MON-YYYY')asc\r\n" + ")\r\n" + "PIVOT\r\n"
					+ "(\r\n" + "  sum(scs) as scs,SUM(CS_CHARGES) as CS_CHARGES\r\n"
					+ "  FOR MON_YEAR IN ('APR-FI' APR_FI,'MAY-FI'MAY_FI,'JUN-FI'JUN_FI,'JUL-FI'JUL_FI,'AUG-FI'AUG_FI,'SEP-FI'SEP_FI,\r\n"
					+ "  'OCT-FI'OCT_FI,'NOV-FI'NOV_FI,'DEC-FI'DEC_FI,'JAN-SI'JAN_SI,'FEB-SI'FEB_SI,'MAR-SI' MAR_SI)\r\n"
					+ ")\r\n" + "ORDER BY SECNAME").replace("FI", fyear[0]).replace("SI", fyear[1]);
			log.info(sql);
			return jdbcTemplate.queryForList(sql, new Object[] { subDivision });
		} catch (Exception e) {
			log.info(e.getMessage());
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	// 134
	public List<Map<String, Object>> getOpenAccessWheelingChargesReport(HttpServletRequest request) {
		String fyear[] = request.getParameter("year").split("-");

		try {
			String sql = ("SELECT * FROM\r\n" + "(\r\n"
					+ "SELECT SUBSTR(OAUSCNO,1,3)CIRCLE,BILL_MON||'-'|| BILL_YEAR MON_YEAR,count(*)scs,SUM(NVL(WHELL_CHARGES,0))WHELL_CHARGES FROM OPEN_ACCESS_HIST \r\n"
					+ "WHERE TO_DATE(BILL_MON||'-'|| BILL_YEAR,'MON-YYYY') BETWEEN TO_DATE('01-04-FI','DD-MM-YYYY') AND TO_DATE('31-03-SI','DD-MM-YYYY')\r\n"
					+ "AND SUBSTR(OAUSCNO,1,3) IN ('CRD','GNT','ONG','VJA') GROUP BY SUBSTR(OAUSCNO,1,3),BILL_MON||'-'|| BILL_YEAR\r\n"
					+ "ORDER BY SUBSTR(OAUSCNO,1,3),TO_DATE(BILL_MON||'-'|| BILL_YEAR,'MON-YYYY')asc\r\n" + ")\r\n"
					+ "PIVOT\r\n" + "(\r\n" + "  sum(scs) as scs,SUM(WHELL_CHARGES) as WHELL_CHARGES\r\n"
					+ "  FOR MON_YEAR IN ('APR-FI' APR_FI,'MAY-FI'MAY_FI,'JUN-FI'JUN_FI,'JUL-FI'JUL_FI,'AUG-FI'AUG_FI,'SEP-FI'SEP_FI,\r\n"
					+ "  'OCT-FI'OCT_FI,'NOV-FI'NOV_FI,'DEC-FI'DEC_FI,'JAN-SI'JAN_SI,'FEB-SI'FEB_SI,'MAR-SI' MAR_SI)\r\n"
					+ ")\r\n" + "ORDER BY CIRCLE").replace("FI", fyear[0]).replace("SI", fyear[1]);
			log.info(sql);
			return jdbcTemplate.queryForList(sql, new Object[] {});
		} catch (Exception e) {
			log.info(e.getMessage());
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	public List<Map<String, Object>> getOpenAccessWheelingChargesReportForDivision(String circle, String year) {
		String fyear[] = year.split("-");

		try {
			String sql = ("SELECT * FROM\r\n" + "(\r\n"
					+ "SELECT DIVNAME,BILL_MON||'-'|| BILL_YEAR MON_YEAR,count(*)scs,SUM(NVL(WHELL_CHARGES,0))WHELL_CHARGES FROM OPEN_ACCESS_HIST,CONS,MASTER.SPDCLMASTER\r\n"
					+ "WHERE TO_DATE(BILL_MON||'-'|| BILL_YEAR,'MON-YYYY') BETWEEN TO_DATE('01-04-FI','DD-MM-YYYY') AND TO_DATE('31-03-SI','DD-MM-YYYY')\r\n"
					+ "AND SUBSTR(OAUSCNO,1,3) IN ('CRD','GNT','ONG','VJA') AND CTUSCNO=OAUSCNO AND SUBSTR(CTSECCD,-5)=SECCD AND SUBSTR(OAUSCNO,1,3)=?\r\n"
					+ "GROUP BY DIVNAME,BILL_MON||'-'|| BILL_YEAR\r\n"
					+ "ORDER BY DIVNAME,TO_DATE(BILL_MON||'-'|| BILL_YEAR,'MON-YYYY')asc\r\n" + ")\r\n" + "PIVOT\r\n"
					+ "(\r\n" + "  sum(scs) as scs,SUM(WHELL_CHARGES) as WHELL_CHARGES\r\n"
					+ "  FOR MON_YEAR IN ('APR-FI' APR_FI,'MAY-FI'MAY_FI,'JUN-FI'JUN_FI,'JUL-FI'JUL_FI,'AUG-FI'AUG_FI,'SEP-FI'SEP_FI,\r\n"
					+ "  'OCT-FI'OCT_FI,'NOV-FI'NOV_FI,'DEC-FI'DEC_FI,'JAN-SI'JAN_SI,'FEB-SI'FEB_SI,'MAR-SI' MAR_SI)\r\n"
					+ ")\r\n" + "ORDER BY DIVNAME").replace("FI", fyear[0]).replace("SI", fyear[1]);
			log.info(sql);
			return jdbcTemplate.queryForList(sql, new Object[] { circle });
		} catch (Exception e) {
			log.info(e.getMessage());
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	public List<Map<String, Object>> getOpenAccessWheelingChargesReportSubDivision(String division, String year) {
		String fyear[] = year.split("-");

		try {
			String sql = ("SELECT * FROM\r\n" + "(\r\n"
					+ "SELECT SUBNAME,BILL_MON||'-'|| BILL_YEAR MON_YEAR,count(*)scs,SUM(NVL(WHELL_CHARGES,0))WHELL_CHARGES FROM OPEN_ACCESS_HIST,CONS,MASTER.SPDCLMASTER\r\n"
					+ "WHERE TO_DATE(BILL_MON||'-'|| BILL_YEAR,'MON-YYYY') BETWEEN TO_DATE('01-04-FI','DD-MM-YYYY') AND TO_DATE('31-03-SI','DD-MM-YYYY')\r\n"
					+ "AND SUBSTR(OAUSCNO,1,3) IN ('CRD','GNT','ONG','VJA') AND CTUSCNO=OAUSCNO AND SUBSTR(CTSECCD,-5)=SECCD AND DIVNAME=?\r\n"
					+ "GROUP BY SUBNAME,BILL_MON||'-'|| BILL_YEAR\r\n"
					+ "ORDER BY SUBNAME,TO_DATE(BILL_MON||'-'|| BILL_YEAR,'MON-YYYY')asc\r\n" + ")\r\n" + "PIVOT\r\n"
					+ "(\r\n" + "  sum(scs) as scs,SUM(WHELL_CHARGES) as WHELL_CHARGES\r\n"
					+ "  FOR MON_YEAR IN ('APR-FI' APR_FI,'MAY-FI'MAY_FI,'JUN-FI'JUN_FI,'JUL-FI'JUL_FI,'AUG-FI'AUG_FI,'SEP-FI'SEP_FI,\r\n"
					+ "  'OCT-FI'OCT_FI,'NOV-FI'NOV_FI,'DEC-FI'DEC_FI,'JAN-SI'JAN_SI,'FEB-SI'FEB_SI,'MAR-SI' MAR_SI)\r\n"
					+ ")\r\n" + "ORDER BY SUBNAME").replace("FI", fyear[0]).replace("SI", fyear[1]);
			log.info(sql);
			return jdbcTemplate.queryForList(sql, new Object[] { division });
		} catch (Exception e) {
			log.info(e.getMessage());
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	public List<Map<String, Object>> getOpenAccessWheelingChargesReportForSection(String subDivision, String year) {
		String fyear[] = year.split("-");

		try {
			String sql = ("SELECT * FROM\r\n" + "(\r\n"
					+ "SELECT SECNAME,BILL_MON||'-'|| BILL_YEAR MON_YEAR,count(*)scs,SUM(NVL(WHELL_CHARGES,0))WHELL_CHARGES FROM OPEN_ACCESS_HIST,CONS,MASTER.SPDCLMASTER\r\n"
					+ "WHERE TO_DATE(BILL_MON||'-'|| BILL_YEAR,'MON-YYYY') BETWEEN TO_DATE('01-04-FI','DD-MM-YYYY') AND TO_DATE('31-03-SI','DD-MM-YYYY')\r\n"
					+ "AND SUBSTR(OAUSCNO,1,3) IN ('CRD','GNT','ONG','VJA') AND CTUSCNO=OAUSCNO AND SUBSTR(CTSECCD,-5)=SECCD AND SUBNAME=?\r\n"
					+ "GROUP BY SECNAME,BILL_MON||'-'|| BILL_YEAR\r\n"
					+ "ORDER BY SECNAME,TO_DATE(BILL_MON||'-'|| BILL_YEAR,'MON-YYYY')asc\r\n" + ")\r\n" + "PIVOT\r\n"
					+ "(\r\n" + "  sum(scs) as scs,SUM(WHELL_CHARGES) as WHELL_CHARGES\r\n"
					+ "  FOR MON_YEAR IN ('APR-FI' APR_FI,'MAY-FI'MAY_FI,'JUN-FI'JUN_FI,'JUL-FI'JUL_FI,'AUG-FI'AUG_FI,'SEP-FI'SEP_FI,\r\n"
					+ "  'OCT-FI'OCT_FI,'NOV-FI'NOV_FI,'DEC-FI'DEC_FI,'JAN-SI'JAN_SI,'FEB-SI'FEB_SI,'MAR-SI' MAR_SI)\r\n"
					+ ")\r\n" + "ORDER BY SECNAME").replace("FI", fyear[0]).replace("SI", fyear[1]);
			log.info(sql);
			return jdbcTemplate.queryForList(sql, new Object[] { subDivision });
		} catch (Exception e) {
			log.info(e.getMessage());
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	// 135
	public List<Map<String, Object>> getCumilativeReport(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		String fromMonthYear = "01-" + request.getParameter("fmonth") + "-" + request.getParameter("fyear");
		String toMonthYear = "01-" + request.getParameter("tmonth") + "-" + request.getParameter("tyear");
		if (circle.equalsIgnoreCase("ALL")) {
			try {
				String sql = "SELECT SUBSTR(CTUSCNO,1,3) CIRCLE,CASE WHEN DIVNAME IS NULL THEN 'TOTAL' ELSE DIVNAME END DIVNAME,CASE WHEN SUBNAME IS NULL THEN 'TOTAL' ELSE SUBNAME END SUBNAME,\r\n"
						+ "CASE WHEN SECNAME IS NULL THEN 'TOTAL' ELSE SECNAME END SECNAME,CASE WHEN TYPE IS NULL THEN 'TOTAL' ELSE TYPE END TYPE,\r\n"
						+ "ROUND(SUM(OB/10000000),2)OB,ROUND(SUM(DEMAND/10000000),2)DEMAND,ROUND(SUM(COLLECTION/10000000),2)COLLECTION,ROUND(SUM(CB/10000000),2)CB FROM \r\n"
						+ "(SELECT CTUSCNO,CTSECCD,CASE WHEN CTGOVT_PVT='Y' THEN 'GOVT' WHEN CTGOVT_PVT='N' THEN 'PVT' END TYPE FROM CONS), \r\n"
						+ "(SELECT USCNO,SUM(ROUND(NVL(TOT_OB,0))+ROUND(NVL(OB_OTH,0))+ROUND(NVL(OB_CCLPC,0))) OB FROM LEDGER_HT_HIST WHERE MON_YEAR=TO_CHAR(TO_DATE(?,'DD-MM-YYYY'),'MON-YYYY') AND  \r\n"
						+ "SUBSTR(USCNO,1,3) IN('CRD','GNT','ONG','VJA')GROUP BY USCNO)A, \r\n"
						+ "(SELECT USCNO,roUND(SUM(NVL(CMD,0) +NVL(CCLPC,0)+NVL(DRJ,0)+NVL(RJ_CCLPC,0)+NVL(RJ_OTH,0))) DEMAND, \r\n"
						+ "ROUND(SUM(NVL(CRJ,0)+NVL(TOT_PAY,0))) COLLECTION FROM LEDGER_HT_HIST where \r\n"
						+ "to_date(mon_year,'MON-YYYY') between TO_DATE(?,'DD-MM-YYYY') and TO_DATE(?,'DD-MM-YYYY') \r\n"
						+ "AND SUBSTR(USCNO,1,3) IN('CRD','GNT','ONG','VJA') group by uscno)B, \r\n"
						+ "(SELECT USCNO,SUM(ROUND(NVL(CBTOT,0))+ROUND(NVL(CB_CCLPC,0))+ROUND(NVL(CB_OTH,0))) CB FROM LEDGER_HT_HIST WHERE MON_YEAR=TO_CHAR(TO_DATE(?,'DD-MM-YYYY'),'MON-YYYY')\r\n"
						+ "AND SUBSTR(USCNO,1,3) IN('CRD','GNT','ONG','VJA') GROUP BY USCNO)C, \r\n"
						+ "(SELECT * FROM MASTER.SPDCLMASTER)D \r\n" + "WHERE CTUSCNO=A.USCNO(+)  \r\n"
						+ "AND CTUSCNO=B.USCNO(+) \r\n" + "AND CTUSCNO=C.USCNO(+) \r\n"
						+ "AND SUBSTR(CTSECCD,-5)=SECCD \r\n"
						+ "GROUP BY grouping sets((SUBSTR(CTUSCNO,1,3),DIVNAME,SUBNAME,SECNAME,type),(SUBSTR(CTUSCNO,1,3),DIVNAME,SUBNAME,SECNAME),(SUBSTR(CTUSCNO,1,3),DIVNAME,SUBNAME),\r\n"
						+ "(SUBSTR(CTUSCNO,1,3),DIVNAME), (SUBSTR(CTUSCNO,1,3)))\r\n"
						+ "ORDER BY CIRCLE,DIVNAME,SUBNAME,SECNAME,TYPE";
				log.info(sql);
				return jdbcTemplate.queryForList(sql,
						new Object[] { fromMonthYear, fromMonthYear, toMonthYear, toMonthYear });
			} catch (DataAccessException e) {
				e.printStackTrace();
				log.error(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}
		} else {
			try {
				String sql = "SELECT SUBSTR(CTUSCNO,1,3) CIRCLE,CASE WHEN DIVNAME IS NULL THEN 'TOTAL' ELSE DIVNAME END DIVNAME,CASE WHEN SUBNAME IS NULL THEN 'TOTAL' ELSE SUBNAME END SUBNAME,\r\n"
						+ "CASE WHEN SECNAME IS NULL THEN 'TOTAL' ELSE SECNAME END SECNAME,CASE WHEN TYPE IS NULL THEN 'TOTAL' ELSE TYPE END TYPE,\r\n"
						+ "ROUND(SUM(OB/10000000),2)OB,ROUND(SUM(DEMAND/10000000),2)DEMAND,ROUND(SUM(COLLECTION/10000000),2)COLLECTION,ROUND(SUM(CB/10000000),2)CB FROM \r\n"
						+ "(SELECT CTUSCNO,CTSECCD,CASE WHEN CTGOVT_PVT='Y' THEN 'GOVT' WHEN CTGOVT_PVT='N' THEN 'PVT' END TYPE FROM CONS), \r\n"
						+ "(SELECT USCNO,SUM(ROUND(NVL(TOT_OB,0))+ROUND(NVL(OB_OTH,0))+ROUND(NVL(OB_CCLPC,0))) OB FROM LEDGER_HT_HIST WHERE MON_YEAR=TO_CHAR(TO_DATE(?,'DD-MM-YYYY'),'MON-YYYY')  AND  \r\n"
						+ "SUBSTR(USCNO,1,3) IN('CRD','GNT','ONG','VJA')GROUP BY USCNO)A, \r\n"
						+ "(SELECT USCNO,roUND(SUM(NVL(CMD,0) +NVL(CCLPC,0)+NVL(DRJ,0)+NVL(RJ_CCLPC,0)+NVL(RJ_OTH,0))) DEMAND, \r\n"
						+ "ROUND(SUM(NVL(CRJ,0)+NVL(TOT_PAY,0))) COLLECTION FROM LEDGER_HT_HIST where \r\n"
						+ "to_date(mon_year,'MON-YYYY') between TO_DATE(?,'DD-MM-YYYY') and TO_DATE(?,'DD-MM-YYYY') \r\n"
						+ "AND SUBSTR(USCNO,1,3) IN('CRD','GNT','ONG','VJA') group by uscno)B, \r\n"
						+ "(SELECT USCNO,SUM(ROUND(NVL(CBTOT,0))+ROUND(NVL(CB_CCLPC,0))+ROUND(NVL(CB_OTH,0))) CB FROM LEDGER_HT_HIST WHERE MON_YEAR=TO_CHAR(TO_DATE(?,'DD-MM-YYYY'),'MON-YYYY') \r\n"
						+ "AND SUBSTR(USCNO,1,3) IN('CRD','GNT','ONG','VJA') GROUP BY USCNO)C, \r\n"
						+ "(SELECT * FROM MASTER.SPDCLMASTER)D \r\n" + "WHERE CTUSCNO=A.USCNO(+)  \r\n"
						+ "AND CTUSCNO=B.USCNO(+) \r\n" + "AND CTUSCNO=C.USCNO(+) \r\n"
						+ "AND SUBSTR(CTSECCD,-5)=SECCD \r\n" + "AND SUBSTR(CTUSCNO,1,3)=?\r\n"
						+ "GROUP BY grouping sets((SUBSTR(CTUSCNO,1,3),DIVNAME,SUBNAME,SECNAME,type),(SUBSTR(CTUSCNO,1,3),DIVNAME,SUBNAME,SECNAME),(SUBSTR(CTUSCNO,1,3),DIVNAME,SUBNAME),\r\n"
						+ "(SUBSTR(CTUSCNO,1,3),DIVNAME), (SUBSTR(CTUSCNO,1,3)))\r\n"
						+ "ORDER BY CIRCLE,DIVNAME,SUBNAME,SECNAME,TYPE";
				log.info(sql);
				return jdbcTemplate.queryForList(sql,
						new Object[] { fromMonthYear, fromMonthYear, toMonthYear, toMonthYear, circle });
			} catch (DataAccessException e) {
				e.printStackTrace();
				log.error(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}

		}
	}

	public List<Map<String, Object>> getCumilativeReportForServices(String circle, String division, String subdiv,
			String section, String fyear, String tyear, String type) {
		try {
			String sql = "SELECT SUBSTR(CTUSCNO,1,3) CIRCLE,CASE WHEN DIVNAME IS NULL THEN 'TOTAL' ELSE DIVNAME END DIVNAME,CASE WHEN SUBNAME IS NULL THEN 'TOTAL' ELSE SUBNAME END SUBNAME,\r\n"
					+ "CASE WHEN SECNAME IS NULL THEN 'TOTAL' ELSE SECNAME END SECNAME,CASE WHEN TYPE IS NULL THEN 'TOTAL' ELSE TYPE END TYPE,CTUSCNO,\r\n"
					+ "ROUND(SUM(OB/10000000),2)OB,ROUND(SUM(DEMAND/10000000),2)DEMAND,ROUND(SUM(COLLECTION/10000000),2)COLLECTION,ROUND(SUM(CB/10000000),2)CB FROM \r\n"
					+ "(SELECT CTUSCNO,CTSECCD,CASE WHEN CTGOVT_PVT='Y' THEN 'GOVT' WHEN CTGOVT_PVT='N' THEN 'PVT' END TYPE FROM CONS), \r\n"
					+ "(SELECT USCNO,SUM(ROUND(NVL(TOT_OB,0))+ROUND(NVL(OB_OTH,0))+ROUND(NVL(OB_CCLPC,0))) OB FROM LEDGER_HT_HIST WHERE MON_YEAR=TO_CHAR(TO_DATE(?,'DD-MM-YYYY'),'MON-YYYY')  AND  \r\n"
					+ "SUBSTR(USCNO,1,3) IN('CRD','GNT','ONG','VJA')GROUP BY USCNO)A, \r\n"
					+ "(SELECT USCNO,roUND(SUM(NVL(CMD,0) +NVL(CCLPC,0)+NVL(DRJ,0)+NVL(RJ_CCLPC,0)+NVL(RJ_OTH,0))) DEMAND, \r\n"
					+ "ROUND(SUM(NVL(CRJ,0)+NVL(TOT_PAY,0))) COLLECTION FROM LEDGER_HT_HIST where \r\n"
					+ "to_date(mon_year,'MON-YYYY') between TO_DATE(?,'DD-MM-YYYY') and TO_DATE(?,'DD-MM-YYYY') \r\n"
					+ "AND SUBSTR(USCNO,1,3) IN('CRD','GNT','ONG','VJA') group by uscno)B, \r\n"
					+ "(SELECT USCNO,SUM(ROUND(NVL(CBTOT,0))+ROUND(NVL(CB_CCLPC,0))+ROUND(NVL(CB_OTH,0))) CB FROM LEDGER_HT_HIST WHERE MON_YEAR=TO_CHAR(TO_DATE(?,'DD-MM-YYYY'),'MON-YYYY') \r\n"
					+ "AND SUBSTR(USCNO,1,3) IN('CRD','GNT','ONG','VJA') GROUP BY USCNO)C, \r\n"
					+ "(SELECT * FROM MASTER.SPDCLMASTER)D \r\n"
					+ "WHERE CTUSCNO=A.USCNO(+)  AND CTUSCNO=B.USCNO(+) AND CTUSCNO=C.USCNO(+) \r\n"
					+ "AND SUBSTR(CTSECCD,-5)=SECCD AND SUBSTR(CTUSCNO,1,3)=?AND DIVNAME=?\r\n"
					+ "AND SUBNAME=?AND SECNAME=? AND TYPE=?\r\n"
					+ "GROUP BY SUBSTR(CTUSCNO,1,3),DIVNAME,SUBNAME,SECNAME,type,CTUSCNO\r\n"
					+ "ORDER BY CIRCLE,DIVNAME,SUBNAME,SECNAME,TYPE,CTUSCNO";
			log.info(sql);
			return jdbcTemplate.queryForList(sql,
					new Object[] { fyear, fyear, tyear, tyear, circle, division, subdiv, section, type });
		} catch (DataAccessException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	// 136
	public List<Map<String, Object>> getArrearsStatusReport(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		String monthYear = "01-" + request.getParameter("month") + " - " + request.getParameter("year");

		String cbAmount = request.getParameter("cbamount");
		if (request.getParameter("dropdown").equals("ALL")) {
			cbAmount = request.getParameter("cbamount");
		} else {
			cbAmount = "50000";
		}
		System.out.println(cbAmount);
		if (circle.equalsIgnoreCase("ALL")) {
			try {
				String sql = "SELECT  A.CIRCLE,CASE WHEN A.DIVISION IS NULL THEN 'TOTAL' ELSE A.DIVISION END DIVISION,\r\n"
						+ "CASE WHEN A.SUBDIVISION IS NULL THEN 'TOTAL' ELSE A.SUBDIVISION END SUBDIVISION,CASE WHEN A.SECTION IS NULL THEN 'TOTAL' ELSE A.SECTION END SECTION,\r\n"
						+ "SUM(NVL(TOT_SCS,0))TOT_SCS,SUM(NVL(TOT_CB,0))TOT_ARREARS,SUM(NVL(GOVT_SCS,0))GOVT_SCS, SUM(NVL(GOVT_CB,0))GOVT_ARREARS, SUM(NVL(PVT_SCS,0))PVT_SCS, \r\n"
						+ "SUM(NVL(PVT_CB,0))PVT_ARREARS,SUM(NVL(LIVE_SCS,0))LIVE_SCS, SUM(NVL(LIVE_CB,0))LIVE_ARREARS, SUM(NVL(UDC_SCS,0))UDC_SCS, SUM(NVL(UDC_CB,0))UDC_ARREARS, \r\n"
						+ "SUM(NVL(BILLSTOP_SCS,0))BS_SCS, SUM(NVL(BILLSTOP_CB,0))BS_ARREARS\r\n" + "FROM(\r\n"
						+ "(SELECT SUBSTR(CTUSCNO,1,3)CIRCLE,DIVISION,SUBDIVISION,SECTION,COUNT(*)TOT_SCS,SUM(NVL(CB,0))TOT_CB FROM(\r\n"
						+ "SELECT CTUSCNO,DIVISION,SUBDIVISION,SECTION,(round(NVL(CB_OTH,0))+round(NVL(CBTOT,0)))CB\r\n"
						+ "FROM LEDGER_HT_HIST,CONS \r\n"
						+ "WHERE  TO_DATE(MON_YEAR,'MON-YYYY')=? AND CTUSCNO=USCNO \r\n"
						+ "AND (round(NVL(CB_OTH,0))+round(NVL(CBTOT,0)))>?\r\n"
						+ "ORDER BY CTUSCNO,DIVISION,SUBDIVISION,SECTION,STATUS)\r\n"
						+ "GROUP BY SUBSTR(CTUSCNO,1,3),DIVISION,SUBDIVISION,SECTION\r\n"
						+ "ORDER BY CIRCLE,DIVISION,SUBDIVISION,SECTION))A,\r\n" + "\r\n" + "(SELECT * FROM\r\n"
						+ "(SELECT SUBSTR(CTUSCNO,1,3)CIRCLE,DIVISION,SUBDIVISION,SECTION,DECODE(CTGOVT_PVT,'Y','GOVT','N','PVT')TYPE,COUNT(*)SCS,SUM(round(NVL(CB_OTH,0))+round(NVL(CBTOT,0)))CB\r\n"
						+ "FROM LEDGER_HT_HIST,CONS \r\n"
						+ "WHERE  TO_DATE(MON_YEAR,'MON-YYYY')=? AND CTUSCNO=USCNO \r\n"
						+ "AND (round(NVL(CB_OTH,0))+round(NVL(CBTOT,0)))>?\r\n"
						+ "GROUP BY SUBSTR(CTUSCNO,1,3),DIVISION,SUBDIVISION,SECTION,DECODE(CTGOVT_PVT,'Y','GOVT','N','PVT')\r\n"
						+ "ORDER BY CIRCLE,DIVISION,SUBDIVISION,SECTION,TYPE)\r\n" + "PIVOT(\r\n"
						+ "SUM(SCS)AS SCS,SUM(CB)AS CB FOR TYPE IN('GOVT' GOVT,'PVT' PVT)) ORDER BY CIRCLE,DIVISION,SUBDIVISION,SECTION)B,\r\n"
						+ "\r\n" + "(SELECT * FROM(\r\n"
						+ "SELECT SUBSTR(CTUSCNO,1,3)CIRCLE,DIVISION,SUBDIVISION,SECTION,case when status='01' then 'Live' when status='02' then 'Billstop' when status='03' then 'Udc' end status_desc,COUNT(*)SCS,SUM(NVL(CB,0))CB FROM(\r\n"
						+ "SELECT CTUSCNO,DIVISION,SUBDIVISION,SECTION,nvl((select '0'||MDCLKWHSTAT_HT from mtrdat_HIST where MDMONTH= ? and MDCLKWHSTAT_HT=3 and MSCNO=ctuscno),\r\n"
						+ "           '0'||case when CTSTATUS=0 then 2 else CTSTATUS end)STATUS,(round(NVL(CB_OTH,0))+round(NVL(CBTOT,0)))CB\r\n"
						+ "FROM LEDGER_HT_HIST,CONS \r\n"
						+ "WHERE TO_DATE(MON_YEAR,'MON-YYYY')=? AND CTUSCNO=USCNO \r\n"
						+ "AND (round(NVL(CB_OTH,0))+round(NVL(CBTOT,0)))>?\r\n"
						+ "ORDER BY CTUSCNO,DIVISION,SUBDIVISION,SECTION,STATUS)\r\n"
						+ "GROUP BY SUBSTR(CTUSCNO,1,3),DIVISION,SUBDIVISION,SECTION,case when status='01' then 'Live' when status='02' then 'Billstop' when status='03' then 'Udc' end\r\n"
						+ "ORDER BY CIRCLE,DIVISION,SUBDIVISION,SECTION,status_desc)\r\n" + "PIVOT(\r\n"
						+ "SUM(SCS) AS SCS,SUM(CB) AS CB FOR status_desc IN('Live' LIVE,'Udc' UDC,'Billstop' BILLSTOP))\r\n"
						+ "ORDER BY CIRCLE,DIVISION,SUBDIVISION,SECTION)C\r\n"
						+ "WHERE A.CIRCLE=B.CIRCLE AND A.CIRCLE=C.CIRCLE AND  A.DIVISION=B.DIVISION AND A.DIVISION=C.DIVISION AND A.SUBDIVISION=B.SUBDIVISION AND \r\n"
						+ "A.SUBDIVISION=C.SUBDIVISION AND A.SECTION=B.SECTION AND  A.SECTION=C.SECTION\r\n"
						+ "GROUP BY GROUPING SETS((A.CIRCLE,A.DIVISION,A.SUBDIVISION,A.SECTION),(A.CIRCLE,A.DIVISION,A.SUBDIVISION),(A.CIRCLE,A.DIVISION),(A.CIRCLE))\r\n"
						+ "ORDER BY A.CIRCLE,A.DIVISION,A.SUBDIVISION,A.SECTION";
				log.info(sql);
				return jdbcTemplate.queryForList(sql,
						new Object[] { monthYear, cbAmount, monthYear, cbAmount, monthYear, monthYear, cbAmount });
			} catch (DataAccessException e) {
				e.printStackTrace();
				log.error(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}
		} else {
			try {
				String sql = "SELECT  A.CIRCLE,CASE WHEN A.DIVISION IS NULL THEN 'TOTAL' ELSE A.DIVISION END DIVISION,\r\n"
						+ "CASE WHEN A.SUBDIVISION IS NULL THEN 'TOTAL' ELSE A.SUBDIVISION END SUBDIVISION,CASE WHEN A.SECTION IS NULL THEN 'TOTAL' ELSE A.SECTION END SECTION,\r\n"
						+ "SUM(NVL(TOT_SCS,0))TOT_SCS,SUM(NVL(TOT_CB,0))TOT_ARREARS,SUM(NVL(GOVT_SCS,0))GOVT_SCS, SUM(NVL(GOVT_CB,0))GOVT_ARREARS, SUM(NVL(PVT_SCS,0))PVT_SCS, \r\n"
						+ "SUM(NVL(PVT_CB,0))PVT_ARREARS,SUM(NVL(LIVE_SCS,0))LIVE_SCS, SUM(NVL(LIVE_CB,0))LIVE_ARREARS, SUM(NVL(UDC_SCS,0))UDC_SCS, SUM(NVL(UDC_CB,0))UDC_ARREARS, \r\n"
						+ "SUM(NVL(BILLSTOP_SCS,0))BS_SCS, SUM(NVL(BILLSTOP_CB,0))BS_ARREARS\r\n" + "FROM(\r\n"
						+ "(SELECT SUBSTR(CTUSCNO,1,3)CIRCLE,DIVISION,SUBDIVISION,SECTION,COUNT(*)TOT_SCS,SUM(NVL(CB,0))TOT_CB FROM(\r\n"
						+ "SELECT CTUSCNO,DIVISION,SUBDIVISION,SECTION,(round(NVL(CB_OTH,0))+round(NVL(CBTOT,0)))CB\r\n"
						+ "FROM LEDGER_HT_HIST,CONS \r\n"
						+ "WHERE  TO_DATE(MON_YEAR,'MON-YYYY')=? AND CTUSCNO=USCNO \r\n"
						+ "AND SUBSTR(CTUSCNO,1,3)=?\r\n" + "AND (round(NVL(CB_OTH,0))+round(NVL(CBTOT,0)))>?\r\n"
						+ "ORDER BY CTUSCNO,DIVISION,SUBDIVISION,SECTION,STATUS)\r\n"
						+ "GROUP BY SUBSTR(CTUSCNO,1,3),DIVISION,SUBDIVISION,SECTION\r\n"
						+ "ORDER BY CIRCLE,DIVISION,SUBDIVISION,SECTION))A,\r\n" + "\r\n" + "(SELECT * FROM\r\n"
						+ "(SELECT SUBSTR(CTUSCNO,1,3)CIRCLE,DIVISION,SUBDIVISION,SECTION,DECODE(CTGOVT_PVT,'Y','GOVT','N','PVT')TYPE,COUNT(*)SCS,SUM(round(NVL(CB_OTH,0))+round(NVL(CBTOT,0)))CB\r\n"
						+ "FROM LEDGER_HT_HIST,CONS \r\n"
						+ "WHERE  TO_DATE(MON_YEAR,'MON-YYYY')=? AND CTUSCNO=USCNO \r\n"
						+ "AND SUBSTR(CTUSCNO,1,3)=?\r\n" + "AND (round(NVL(CB_OTH,0))+round(NVL(CBTOT,0)))>?\r\n"
						+ "GROUP BY SUBSTR(CTUSCNO,1,3),DIVISION,SUBDIVISION,SECTION,DECODE(CTGOVT_PVT,'Y','GOVT','N','PVT')\r\n"
						+ "ORDER BY CIRCLE,DIVISION,SUBDIVISION,SECTION,TYPE)\r\n" + "PIVOT(\r\n"
						+ "SUM(SCS)AS SCS,SUM(CB)AS CB FOR TYPE IN('GOVT' GOVT,'PVT' PVT)) ORDER BY CIRCLE,DIVISION,SUBDIVISION,SECTION)B,\r\n"
						+ "\r\n" + "(SELECT * FROM(\r\n"
						+ "SELECT SUBSTR(CTUSCNO,1,3)CIRCLE,DIVISION,SUBDIVISION,SECTION,case when status='01' then 'Live' when status='02' then 'Billstop' when status='03' then 'Udc' end status_desc,COUNT(*)SCS,SUM(NVL(CB,0))CB FROM(\r\n"
						+ "SELECT CTUSCNO,DIVISION,SUBDIVISION,SECTION,nvl((select '0'||MDCLKWHSTAT_HT from mtrdat_HIST where MDMONTH= ? and MDCLKWHSTAT_HT=3 and MSCNO=ctuscno),\r\n"
						+ "           '0'||case when CTSTATUS=0 then 2 else CTSTATUS end)STATUS,(round(NVL(CB_OTH,0))+round(NVL(CBTOT,0)))CB\r\n"
						+ "FROM LEDGER_HT_HIST,CONS \r\n"
						+ "WHERE TO_DATE(MON_YEAR,'MON-YYYY')=? AND CTUSCNO=USCNO \r\n"
						+ "AND SUBSTR(CTUSCNO,1,3)=?\r\n" + "AND (round(NVL(CB_OTH,0))+round(NVL(CBTOT,0)))>?\r\n"
						+ "ORDER BY CTUSCNO,DIVISION,SUBDIVISION,SECTION,STATUS)\r\n"
						+ "GROUP BY SUBSTR(CTUSCNO,1,3),DIVISION,SUBDIVISION,SECTION,case when status='01' then 'Live' when status='02' then 'Billstop' when status='03' then 'Udc' end\r\n"
						+ "ORDER BY CIRCLE,DIVISION,SUBDIVISION,SECTION,status_desc)\r\n" + "PIVOT(\r\n"
						+ "SUM(SCS) AS SCS,SUM(CB) AS CB FOR status_desc IN('Live' LIVE,'Udc' UDC,'Billstop' BILLSTOP))\r\n"
						+ "ORDER BY CIRCLE,DIVISION,SUBDIVISION,SECTION)C\r\n"
						+ "WHERE A.CIRCLE=B.CIRCLE AND A.CIRCLE=C.CIRCLE AND  A.DIVISION=B.DIVISION AND A.DIVISION=C.DIVISION AND A.SUBDIVISION=B.SUBDIVISION AND \r\n"
						+ "A.SUBDIVISION=C.SUBDIVISION AND A.SECTION=B.SECTION AND  A.SECTION=C.SECTION\r\n"
						+ "GROUP BY GROUPING SETS((A.CIRCLE,A.DIVISION,A.SUBDIVISION,A.SECTION),(A.CIRCLE,A.DIVISION,A.SUBDIVISION),(A.CIRCLE,A.DIVISION),(A.CIRCLE))\r\n"
						+ "ORDER BY A.CIRCLE,A.DIVISION,A.SUBDIVISION,A.SECTION";
				log.info(sql);
				return jdbcTemplate.queryForList(sql, new Object[] { monthYear, circle, cbAmount, monthYear, circle,
						cbAmount, monthYear, monthYear, circle, cbAmount });
			} catch (DataAccessException e) {
				e.printStackTrace();
				log.error(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}

		}
	}

	// 137
	public List<Map<String, Object>> getServiceTypesubdivabstract(HttpServletRequest request) {
		String mon_year = request.getParameter("month") + "-" + request.getParameter("year");
		String circle = request.getParameter("circle");
		String division = request.getParameter("division");
		String subdivision = request.getParameter("subdivision");
		String serviceType = request.getParameter("servicetype");

		try {
			StringBuilder sqlBuilder = new StringBuilder();
			sqlBuilder.append("Select SUBSTR(CTUSCNO,1,3)CIRCLE,DIVNAME,DIVCD,SUBNAME,SUBCD,\r\n");
			sqlBuilder.append(
					"b.CTSERVTYPE servtype,c.stdesc,b.CTCAT,count(distinct(ctuscno)) NOS,SUM(LOAD) LOAD,SUM(REC_MD) REC_MD,\r\n");
			sqlBuilder.append("SUM(Round(Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0))) Ob,SUM(Mn_Kvah) Sales\r\n");
			sqlBuilder.append(
					",SUM(round(Nvl(Cmd,0)+Nvl(Cclpc,0))) Demand,SUM(Nvl(round(CASE WHEN Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)>0 THEN\r\n");
			sqlBuilder.append(
					"CASE WHEN Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)>(NVL(Tot_Pay,0)) THEN (NVL(Tot_Pay,0)) ELSE Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0) END END),0)) COLL_ARREAR, \r\n");
			sqlBuilder.append("SUM(Nvl(round(CASE WHEN Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)>0\r\n");
			sqlBuilder.append("THEN CASE WHEN Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)<(NVL(Tot_Pay,0))\r\n");
			sqlBuilder.append(
					"THEN (NVL(Tot_Pay,0)-(Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0))) END ELSE (NVL(Tot_Pay,0)) END ),0)) COLL_DEMAND, \r\n");
			sqlBuilder.append(
					"SUM(round(Nvl(Tot_Pay,0))) Collection,SUM(round(Nvl(Rj_Oth,0)+Nvl(Drj,0)+Nvl(Rj_Cclpc,0))) Drj,SUM(round(Nvl(Crj,0))) Crj,\r\n");
			sqlBuilder.append("SUM(round(Nvl(Cbtot,0)+Nvl(Cb_Oth,0)+Nvl(Cb_Cclpc,0))) Cb\r\n");
			sqlBuilder.append("From (select * from Ledger_Ht_HIST  where Mon_Year=?)A,\r\n");
			sqlBuilder.append("CONS B,servtype C,MASTER.SPDCLMASTER\r\n");
			sqlBuilder.append("Where A.Uscno=B.CTUscno And b.ctservtype=c.stcode AND SUBSTR(CTSECCD,-5)=SECCD(+)\r\n");

			List<Object> params = new ArrayList<>();
			params.add(mon_year);

			if (circle != null && !circle.isEmpty()) {
				sqlBuilder.append("AND SUBSTR(CTUSCNO,1,3)=? \r\n");
				params.add(circle);
			}

			if (division != null && !division.isEmpty() && !division.equals("0")) {
				sqlBuilder.append("AND DIVCD=? \r\n");
				params.add(division);
			} else {
				sqlBuilder.append("");
			}

			if (subdivision != null && !subdivision.isEmpty() && !subdivision.equals("0")) {
				sqlBuilder.append("AND SUBCD=?\r\n");
				params.add(subdivision);
			} else {
				sqlBuilder.append("");
			}

			if (serviceType != null && !serviceType.isEmpty() && !serviceType.equals("ALL")) {
				sqlBuilder.append("AND b.CTSERVTYPE=? \r\n");
				params.add(serviceType);
			} else {
				sqlBuilder.append("");
			}

			sqlBuilder.append(
					"GROUP BY SUBSTR(CTUSCNO,1,3),DIVNAME,DIVCD,SUBCD,SUBNAME, b.ctservtype,c.stdesc,b.CTCAT\r\n");
			sqlBuilder.append("Order By 1,2,3,4,5,6,7,8,9\r\n");

			String sql = sqlBuilder.toString();
			log.info(sql);

			return jdbcTemplate.queryForList(sql, params.toArray());
		} catch (DataAccessException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return Collections.emptyList();
		}
	}

	// 138
	public List<Map<String, Object>> getServiceTypeFYabstract(HttpServletRequest request) {
		String fin_year = request.getParameter("year");
		String fromdate = "01-04-" + fin_year.split("-")[0];
		String todate = "31-03-" + fin_year.split("-")[1];
		String circle = request.getParameter("circle");
		String division = request.getParameter("division");
		String subdivision = request.getParameter("subdivision");
		String serviceType = request.getParameter("servicetype");

		try {
			StringBuilder sqlBuilder = new StringBuilder();
			sqlBuilder.append("Select MON_YEAR,SUBSTR(CTUSCNO,1,3)CIRCLE,DIVNAME,SUBNAME,DIVCD,SUBCD,\r\n");
			sqlBuilder.append(
					"b.CTSERVTYPE servtype,c.stdesc,b.CTCAT,count(distinct(ctuscno)) NOS,SUM(LOAD) LOAD,SUM(REC_MD) REC_MD,\r\n");
			sqlBuilder.append("SUM(Round(Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0))) Ob,SUM(Mn_Kvah) Sales\r\n");
			sqlBuilder.append(
					",SUM(round(Nvl(Cmd,0)+Nvl(Cclpc,0))) Demand,SUM(Nvl(round(CASE WHEN Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)>0 THEN\r\n");
			sqlBuilder.append(
					"CASE WHEN Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)>(NVL(Tot_Pay,0)) THEN (NVL(Tot_Pay,0)) ELSE Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0) END END),0)) COLL_ARREAR,\r\n");
			sqlBuilder.append("SUM(Nvl(round(CASE WHEN Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)>0\r\n");
			sqlBuilder.append("THEN CASE WHEN Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)<(NVL(Tot_Pay,0))\r\n");
			sqlBuilder.append(
					"THEN (NVL(Tot_Pay,0)-(Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0))) END ELSE (NVL(Tot_Pay,0)) END ),0)) COLL_DEMAND,\r\n");
			sqlBuilder.append(
					"SUM(round(Nvl(Tot_Pay,0))) Collection,SUM(round(Nvl(Rj_Oth,0)+Nvl(Drj,0)+Nvl(Rj_Cclpc,0))) Drj,SUM(round(Nvl(Crj,0))) Crj,\r\n");
			sqlBuilder.append("SUM(round(Nvl(Cbtot,0)+Nvl(Cb_Oth,0)+Nvl(Cb_Cclpc,0))) Cb\r\n");
			sqlBuilder.append("From (select LHH.*,'' STATUS_NEW, '' GOVT_PVT from Ledger_Ht_HIST LHH where \r\n");
			sqlBuilder.append(
					"to_date(MON_YEAR,'MON-YYYY') between to_date(?,'DD-MM-YYYY') and to_date(?,'DD-MM-YYYY'))A,\r\n");
			sqlBuilder.append("CONS B,servtype C,MASTER.SPDCLMASTER\r\n");
			sqlBuilder.append("Where A.Uscno=B.CTUscno And b.ctservtype=c.stcode AND SUBSTR(CTSECCD,-5)=SECCD\r\n");

			List<Object> params = new ArrayList<>();
			params.add(fromdate);
			params.add(todate);

			if (circle != null && !circle.isEmpty()) {
				sqlBuilder.append("AND SUBSTR(CTUSCNO,1,3)=?\r\n");
				params.add(circle);
			}

			if (division != null && !division.isEmpty() && !division.equals("0")) {
				sqlBuilder.append("AND DIVCD=? \r\n");
				params.add(division);
			} else {
				sqlBuilder.append("");
			}

			if (subdivision != null && !subdivision.isEmpty() && !subdivision.equals("0")) {
				sqlBuilder.append("AND SUBCD=?\r\n");
				params.add(subdivision);
			} else {
				sqlBuilder.append("");
			}

			if (serviceType != null && !serviceType.isEmpty() && !serviceType.equals("ALL")) {
				sqlBuilder.append("AND b.CTSERVTYPE=? \r\n");
				params.add(serviceType);
			} else {
				sqlBuilder.append("");
			}

			sqlBuilder.append(
					"GROUP BY MON_YEAR,SUBSTR(CTUSCNO,1,3),DIVNAME,SUBNAME,DIVCD,SUBCD,b.ctservtype,c.stdesc,b.CTCAT\r\n");
			sqlBuilder.append("Order By TO_DATE(MON_YEAR,'MON-YYYY'),DIVNAME,SUBNAME,circle,c.stdesc,b.CTCAT\r\n");

			String sql = sqlBuilder.toString();
			log.info(sql);

			return jdbcTemplate.queryForList(sql, params.toArray());
		} catch (DataAccessException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return Collections.emptyList();
		}
	}

	public List<Map<String, Object>> getFeederWiseSubDivAbstarctForNOS(String circle, String division, String subdiv,
			String month, String year, String feedercode) {

		String Mon_Year = month + "-" + year;
		try {
			String sql = "Select UNIQUE ctuscno,sum(nvl(mn_kvah,0)) sales,SUM(nvl(REC_KWH,0)) KWH_UNITS,\r\n"
					+ "SUM(nvl(Mn_Kvah,0)) BKVA_UNITS,SUM(Round(Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0))) Ob,\r\n"
					+ "SUM(round(Nvl(Cmd,0)+Nvl(Cclpc,0))) Demand,\r\n"
					+ "SUM(Nvl(round(CASE WHEN Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)>0 THEN\r\n"
					+ "CASE WHEN Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)>(NVL(Tot_Pay,0)) THEN (NVL(Tot_Pay,0)) ELSE Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0) END END),0)) COLL_ARREAR, \r\n"
					+ "SUM(Nvl(round(CASE WHEN Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)>0 THEN\r\n"
					+ "CASE WHEN Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)<(NVL(Tot_Pay,0)) THEN (NVL(Tot_Pay,0)-(Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0))) END ELSE (NVL(Tot_Pay,0)) END ),0)) COLL_DEMAND,\r\n"
					+ "SUM(round(Nvl(Tot_Pay,0))) Collection,SUM(round(Nvl(Rj_Oth,0)+Nvl(Drj,0)+Nvl(Rj_Cclpc,0))) Drj,SUM(round(Nvl(Crj,0))) Crj, \r\n"
					+ "SUM(round(Nvl(Cbtot,0)+Nvl(Cb_Oth,0)+Nvl(Cb_Cclpc,0))) Cb from ledger_ht_hist a,cons b,feedermast,MASTER.SPDCLMASTER\r\n"
					+ "where TO_DATE(MON_YEAR,'MON-YYYY') IN TO_DATE(?,'MON-YYYY')\r\n"
					+ "and  A.Uscno=B.CTUscno and ctfeeder_code=fmsapfcode AND SUBSTR(CTSECCD,-5)=SECCD\r\n"
					+ "AND SUBSTR(CTUSCNO,1,3)=?\r\n" + "AND DIVNAME=? \r\n" + "AND SUBNAME=?\r\n"
					+ "AND ctfeeder_code=? \r\n" + "GROUP BY ctuscno\r\n" + "Order By ctuscno";
			log.info(sql);
			return jdbcTemplate.queryForList(sql, new Object[] { Mon_Year, circle, division, subdiv, feedercode });
		} catch (DataAccessException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	// 139
	public List<Map<String, Object>> getEdCourtCasesReport(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		String circleString = circle.equals(("ALL")) ? "" : "AND substr(ctuscno,1,3) = '" + circle + "'";
		try {
			String sql = "SELECT * FROM(\r\n" + "SELECT circle ,ctcat,\r\n"
					+ "CASE WHEN ED_COUNT IS NOT NULL THEN ED_COUNT ELSE 0 END MIN_EDRATE,\r\n"
					+ "CASE WHEN BILL_COUNT IS NOT NULL THEN BILL_COUNT ELSE 0 END MAX_EDRATE\r\n" + "FROM(\r\n"
					+ "select substr(ctuscno,1,3) circle ,ctcat,\r\n"
					+ "COUNT(CASE WHEN btscno in(select distinct htscno from ed_court_cases) AND ctcat in('HT2','HT3') THEN BTSCNO ELSE NULL END) ED_COUNT,\r\n"
					+ "COUNT(CASE WHEN btscno NOT in(select distinct htscno from ed_court_cases) and ctcat in('HT2','HT3')  THEN BTSCNO ELSE NULL END) BILL_COUNT\r\n"
					+ "from cons,bill where ctuscno=btscno and ctcat in('HT2','HT3')\r\n" + circleString
					+ "GROUP BY substr(ctuscno,1,3),ctcat\r\n" + "order by circle,ctcat))\r\n" + "PIVOT(\r\n"
					+ "SUM(MIN_EDRATE) AS MIN_EDRATE,SUM(MAX_EDRATE) AS MAX_EDRATE FOR CTCAT IN ('HT2' HT2,'HT3' HT3))\r\n"
					+ "ORDER BY CIRCLE";
			log.info(sql);
			return jdbcTemplate.queryForList(sql, new Object[] {});
		} catch (DataAccessException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	public List<Map<String, Object>> getEdCourtCasesForHT2MinEDRate(String circle) {
		try {
			String sql = "select cirname,divname,subname,secname,ctuscno,ctname,ctcat,DECODE(ctgovt_pvt,'Y','GOVT','N','PVT')TYPE,BTRKVAH_HT,BTBKVAH,bted,bted*0.06 \r\n"
					+ "from cons,bill,master.spdclmaster\r\n"
					+ "where ctuscno=btscno and substr(ctseccd,-5)=seccd and substr(ctuscno,1,3) = ? and ctcat='HT2' AND BTSCNO IN(SELECT HTSCNO FROM ED_COURT_CASES)";
			log.info(sql);
			return jdbcTemplate.queryForList(sql, new Object[] { circle });
		} catch (DataAccessException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	public List<Map<String, Object>> getEdCourtCasesForHT2MaxEDRate(String circle) {
		try {
			String sql = "select cirname,divname,subname,secname,ctuscno,ctname,ctcat,DECODE(ctgovt_pvt,'Y','GOVT','N','PVT')TYPE,BTRKVAH_HT,BTBKVAH,bted,bted*1 from cons,bill,master.spdclmaster\r\n"
					+ "where ctuscno=btscno and substr(ctseccd,-5)=seccd and substr(ctuscno,1,3) = ? and ctcat='HT2' AND BTSCNO NOT IN(SELECT HTSCNO FROM ED_COURT_CASES)";
			log.info(sql);
			return jdbcTemplate.queryForList(sql, new Object[] { circle });
		} catch (DataAccessException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	public List<Map<String, Object>> getEdCourtCasesForHT3MinEDRate(String circle) {
		try {
			String sql = "select cirname,divname,subname,secname,ctuscno,ctname,ctcat,DECODE(ctgovt_pvt,'Y','GOVT','N','PVT')TYPE,BTRKVAH_HT,BTBKVAH,bted,bted*0.06 from cons,bill,master.spdclmaster\r\n"
					+ "where ctuscno=btscno and substr(ctseccd,-5)=seccd and substr(ctuscno,1,3) = ? and ctcat='HT3' AND BTSCNO IN(SELECT HTSCNO FROM ED_COURT_CASES)";
			log.info(sql);
			return jdbcTemplate.queryForList(sql, new Object[] { circle });
		} catch (DataAccessException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	public List<Map<String, Object>> getEdCourtCasesForHT3MaxEDRate(String circle) {
		try {
			String sql = "select cirname,divname,subname,secname,ctuscno,ctname,ctcat,DECODE(ctgovt_pvt,'Y','GOVT','N','PVT')TYPE,BTRKVAH_HT,BTBKVAH,bted,bted*1\r\n"
					+ "from cons,bill,master.spdclmaster\r\n"
					+ "where ctuscno=btscno and substr(ctseccd,-5)=seccd and substr(ctuscno,1,3) = ? \r\n"
					+ "and ctcat='HT3' AND BTSCNO NOT IN(SELECT HTSCNO FROM ED_COURT_CASES)";
			log.info(sql);
			return jdbcTemplate.queryForList(sql, new Object[] { circle });
		} catch (DataAccessException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	// 140
	public List<Map<String, Object>> getHighAndLowGridReport(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		String serviceType = request.getParameter("servicetype");
		String year = request.getParameter("year");
		String fyear = "01-04-" + year.split("-")[0];
		String tyear = "31-03-" + year.split("-")[1];
		String serviceString = serviceType.equals("ALL") ? "" : "AND CTSERVTYPE='" + serviceType + "'";
		String circleString = circle.equals(("ALL")) ? "" : "AND substr(ctuscno,1,3) = '" + circle + "'";
		String loadYear = "";
		LocalDate now = LocalDate.now();
		int lyear = now.getYear();
		if (lyear > Integer.parseInt(year.split("-")[1])) {
			loadYear = "MAR-" + year.split("-")[1];
		} else {
			loadYear = "FEB-" + year.split("-")[1];
		}
		try {
			String sql = "SELECT \r\n" + "SUBSTR(CTUSCNO,1,3)CIRCLE,\r\n"
					+ "COUNT(*) SCS,ROUND(SUM(A.LOAD))LOAD,NVL(STDESC,'NA')PURPOSE_OF_SUPPLY,\r\n"
					+ "CASE WHEN TO_CHAR(TO_DATE(L.MON_YEAR,'MON-YYYY'),'MON') IN('FEB','MAR','APR','MAY','SEP','OCT') THEN 'HIGH_GRID'\r\n"
					+ "WHEN TO_CHAR(TO_DATE(L.MON_YEAR,'MON-YYYY'),'MON') IN('JUN','JUL','AUG','NOV','DEC','JAN') THEN  'LOW_GRID' END HGD_LGD_NOR,L.MON_YEAR,\r\n"
					+ "sum(NVL(BTTOD2,0)+NVL(BTTOD5,0))PEAK, \r\n" + "sum(NVL(BTTOD3,0)+NVL(BTTOD1,0)) OFFPEAK,  \r\n"
					+ "sum(NVL(BTTOD4,0)+NVL(BTTOD6,0)) NOR, \r\n"
					+ "SUM(NVL(BTRKVAH_HT,0))REC_KVAH,SUM(NVL(BTBKVAH,0))BILLED_KVAH,SUM(NVL(BTBLCOLNY_HT,0))COL_UNITS, SUM(NVL(BTBLSOLAR_HT,0))SOLAR_UNITS,SUM(NVL(BTOA_KVAH,0))OA_UNITS,\r\n"
					+ "SUM(NVL(OA_PEAK,0))OA_PEAK,SUM(NVL(OA_OFFPEAK,0))OA_OFFPEAK FROM CONS,\r\n"
					+ "(SELECT USCNO,MON_YEAR,LOAD FROM LEDGER_HT_HIST WHERE MON_YEAR=?)A,\r\n" + "SERVTYPE,\r\n"
					+ "(SELECT * FROM LEDGER_HT_HIST WHERE TO_DATE(MON_YEAR,'MON-YYYY') BETWEEN TO_DATE(?,'DD-MM-YYYY') AND TO_DATE(?,'DD-MM-YYYY')) L,\r\n"
					+ "(SELECT * FROM BILL_HIST WHERE BTBLDT BETWEEN TO_DATE(?,'DD-MM-YYYY') AND TO_DATE(?,'DD-MM-YYYY')),\r\n"
					+ "(SELECT OAUSCNO,BILL_MON ||'-'|| BILL_YEAR BYEAR,SUM(TOD_ADJ_PEAK)OA_PEAK,SUM(TOD_ADJ_OFF)OA_OFFPEAK FROM OPEN_ACCESS_HIST WHERE \r\n"
					+ "TO_DATE(BILL_MON ||'-'|| BILL_YEAR,'MON-YYYY') BETWEEN TO_DATE(?,'DD-MM-YYYY') AND TO_DATE(?,'DD-MM-YYYY')\r\n"
					+ "GROUP BY OAUSCNO,BILL_MON ||'-'|| BILL_YEAR)O,MASTER.SPDCLMASTER\r\n"
					+ "WHERE CTUSCNO=A.USCNO(+) AND CTUSCNO=L.USCNO(+) AND CTUSCNO=BTSCNO(+) AND CTSERVTYPE=STCODE(+) AND L.MON_YEAR=TO_CHAR(BTBLDT,'MON-YYYY')\r\n"
					+ "AND CTUSCNO=OAUSCNO(+) AND L.MON_YEAR=BYEAR(+) AND SUBSTR(CTSECCD,-5)=SECCD(+)\r\n"
					+ circleString + serviceString + "GROUP BY \r\n" + "SUBSTR(CTUSCNO,1,3),\r\n" + "STDESC,\r\n"
					+ "CASE WHEN TO_CHAR(TO_DATE(L.MON_YEAR,'MON-YYYY'),'MON') IN('FEB','MAR','APR','MAY','SEP','OCT') THEN 'HIGH_GRID'\r\n"
					+ "WHEN TO_CHAR(TO_DATE(L.MON_YEAR,'MON-YYYY'),'MON') IN('JUN','JUL','AUG','NOV','DEC','JAN') THEN  'LOW_GRID' END,L.MON_YEAR\r\n"
					+ "ORDER BY \r\n" + "CIRCLE,\r\n" + "PURPOSE_OF_SUPPLY,HGD_LGD_NOR,TO_DATE(L.MON_YEAR,'MON-YYYY')";
			log.info(sql);
			return jdbcTemplate.queryForList(sql, new Object[] { loadYear, fyear, tyear, fyear, tyear, fyear, tyear });
		} catch (DataAccessException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	public List<Map<String, Object>> getHighAndLowGridReportForNOS(String circle, String fyear, String serviceType,
			String year) {
		String fromyear = "01-04-" + year.split("-")[0];
		String tyear = "31-03-" + year.split("-")[1];
		String serviceString = serviceType.equals("ALL") ? "" : "AND STDESC='" + serviceType + "'";
		String circleString = circle.equals(("ALL")) ? "" : "AND substr(ctuscno,1,3) = '" + circle + "'";
		String loadYear = "";
		LocalDate now = LocalDate.now();
		int lyear = now.getYear();
		if (lyear > Integer.parseInt(year.split("-")[1])) {
			loadYear = "MAR-" + year.split("-")[1];
		} else {
			loadYear = "FEB-" + year.split("-")[1];
		}
		try {
			String sql = "SELECT SUBSTR(CTUSCNO,1,3)CIRCLE,DIVNAME,SUBNAME,SECNAME,CTUSCNO USCNO,CTNAME NAME,CTCAT CAT,CTSUBCAT SUBCAT,SUM(A.LOAD)LOAD,NVL(STDESC,'NA')PURPOSE_OF_SUPPLY,\r\n"
					+ "CASE WHEN TO_CHAR(TO_DATE(L.MON_YEAR,'MON-YYYY'),'MON') IN('FEB','MAR','APR','MAY','SEP','OCT') THEN 'HIGH_GRID'\r\n"
					+ "WHEN TO_CHAR(TO_DATE(L.MON_YEAR,'MON-YYYY'),'MON') IN('JUN','JUL','AUG','NOV','DEC','JAN') THEN  'LOW_GRID' END HGD_LGD_NOR,L.MON_YEAR,\r\n"
					+ "sum(NVL(BTTOD2,0)+NVL(BTTOD5,0))PEAK, \r\n" + "sum(NVL(BTTOD3,0)+NVL(BTTOD1,0)) OFFPEAK,  \r\n"
					+ "sum(NVL(BTTOD4,0)+NVL(BTTOD6,0)) NOR, \r\n"
					+ "SUM(NVL(BTRKVAH_HT,0))REC_KVAH,SUM(NVL(BTBKVAH,0))BILLED_KVAH,SUM(NVL(BTBLCOLNY_HT,0))COL_UNITS, SUM(NVL(BTBLSOLAR_HT,0))SOLAR_UNITS,SUM(NVL(BTOA_KVAH,0))OA_UNITS,\r\n"
					+ "SUM(NVL(OA_PEAK,0))OA_PEAK,SUM(NVL(OA_OFFPEAK,0))OA_OFFPEAK FROM CONS,\r\n"
					+ "(SELECT USCNO,MON_YEAR,LOAD FROM LEDGER_HT_HIST WHERE MON_YEAR=?)A,\r\n" + "SERVTYPE,\r\n"
					+ "(SELECT * FROM LEDGER_HT_HIST WHERE TO_DATE(MON_YEAR,'MON-YYYY') BETWEEN TO_DATE(?,'DD-MM-YYYY') AND TO_DATE(?,'DD-MM-YYYY')) L,\r\n"
					+ "(SELECT * FROM BILL_HIST WHERE BTBLDT BETWEEN TO_DATE(?,'DD-MM-YYYY') AND TO_DATE(?,'DD-MM-YYYY')),\r\n"
					+ "(SELECT OAUSCNO,BILL_MON ||'-'|| BILL_YEAR BYEAR,SUM(TOD_ADJ_PEAK)OA_PEAK,SUM(TOD_ADJ_OFF)OA_OFFPEAK FROM OPEN_ACCESS_HIST WHERE \r\n"
					+ "TO_DATE(BILL_MON ||'-'|| BILL_YEAR,'MON-YYYY') BETWEEN TO_DATE(?,'DD-MM-YYYY') AND TO_DATE(?,'DD-MM-YYYY')\r\n"
					+ "GROUP BY OAUSCNO,BILL_MON ||'-'|| BILL_YEAR)O,MASTER.SPDCLMASTER\r\n"
					+ "WHERE CTUSCNO=A.USCNO(+) AND CTUSCNO=L.USCNO(+) AND CTUSCNO=BTSCNO(+) AND CTSERVTYPE=STCODE(+) AND L.MON_YEAR=TO_CHAR(BTBLDT,'MON-YYYY')\r\n"
					+ "AND CTUSCNO=OAUSCNO(+) AND L.MON_YEAR=BYEAR(+) AND SUBSTR(CTSECCD,-5)=SECCD(+)  AND L.MON_YEAR=?\r\n"
					+ circleString + serviceString
					+ "GROUP BY SUBSTR(CTUSCNO,1,3),DIVNAME,SUBNAME,SECNAME,CTUSCNO,CTNAME,CTCAT,CTSUBCAT,STDESC,\r\n"
					+ "CASE WHEN TO_CHAR(TO_DATE(L.MON_YEAR,'MON-YYYY'),'MON') IN('FEB','MAR','APR','MAY','SEP','OCT') THEN 'HIGH_GRID'\r\n"
					+ "WHEN TO_CHAR(TO_DATE(L.MON_YEAR,'MON-YYYY'),'MON') IN('JUN','JUL','AUG','NOV','DEC','JAN') THEN  'LOW_GRID' END,L.MON_YEAR\r\n"
					+ "ORDER BY CTUSCNO,CTNAME,CTCAT,CTSUBCAT,STDESC,HGD_LGD_NOR,TO_DATE(L.MON_YEAR,'MON-YYYY')";
			log.info(sql);
			return jdbcTemplate.queryForList(sql,
					new Object[] { loadYear, fromyear, tyear, fromyear, tyear, fromyear, tyear, fyear });
		} catch (DataAccessException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	public List<Map<String, Object>> getSingleServiceRedingReport(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		String serviceType = request.getParameter("servicetype");
		String mon_year = request.getParameter("month") + "-" + request.getParameter("year");
		String serviceString = serviceType.equals("ALL") ? "" : "AND CTSERVTYPE='" + serviceType + "'";
		String circleString = circle.equals(("ALL")) ? "" : "AND substr(ctuscno,1,3) = '" + circle + "'";

		try {
			String sql = "SELECT SUBSTR(CTUSCNO,1,3)CIRCLE,DIVNAME,SUBNAME,SECNAME,A.CTUSCNO USCNO,A.CTNAME NAME,B.CAT, B.SCAT,TO_DATE(B.MON_YEAR,'MON-YYYY')MON_YEAR,DECODE(A.CTGOVT_PVT,'Y''GOVT','N','PVT')TYPE ,NVL(B.LOAD,0)LOAD,\r\n"
					+ "NVL(C.STDESC,'NA') SERVICETYPE,A.CTACTUAL_KV VOLTAGE,A.CTSTATUS STATUS,NVL(D.MDOPNKWH_HT,0)MDOPNKWH_HT, NVL(D.MDCLKWH_HT,0)MDCLKWH_HT,NVL(D.MDRKWH_HT,0)MDRKWH_HT,\r\n"
					+ "NVL(D.MDMF_HT,0)MDMF_HT,NVL(D.MDRECKWH_HT,0)MDRECKWH_HT,NVL(D.MDOPNKVAH_HT,0)MDOPNKVAH_HT, NVL(D.MDCLKVAH_HT,0)MDCLKVAH_HT, NVL(D.MDRKVAH_HT,0)MDRKVAH_HT,\r\n"
					+ "NVL(D.MDRECKVAH_HT,0)MDRECKVAH_HT,NVL(D.MDTOD1_OPN,0)MDTOD1_OPN, NVL(D.MDTOD1_CLS,0)MDTOD1_CLS,NVL(D.MDTOD1_RECKVAH,0)MDTOD1_RECKVAH,NVL(D.MDTOD2_OPN,0)MDTOD2_OPN,\r\n"
					+ "NVL(D.MDTOD2_CLS,0)MDTOD2_CLS,NVL(D.MDTOD2_RECKVAH,0)MDTOD2_RECKVAH,NVL(D.MDTOD3_OPN,0)MDTOD3_OPN, NVL(D.MDTOD3_CLS,0)MDTOD3_CLS,NVL(D.MDTOD3_RECKVAH,0)MDTOD3_RECKVAH,\r\n"
					+ "NVL(D.MDTOD4_OPN,0)MDTOD4_OPN, NVL(D.MDTOD4_CLS,0)MDTOD4_CLS,NVL(D.MDTOD4_RECKVAH,0)MDTOD4_RECKVAH,NVL(D.MDTOD5_OPN,0)MDTOD5_OPN, NVL(D.MDTOD5_CLS,0)MDTOD5_CLS,\r\n"
					+ "NVL(D.MDTOD5_RECKVAH,0)MDTOD5_RECKVAH,NVL(D.MDTOD6_OPN,0)MDTOD6_OPN, NVL(D.MDTOD6_CLS,0)MDTOD6_CLS,NVL(D.MDTOD6_RECKVAH,0)MDTOD6_RECKVAH,NVL(D.TOD_TOTAL_UNITS,0)TOD_TOTAL_UNITS,\r\n"
					+ "NVL(MDTOD_PEAK,0)TOD_PEAK,NVL(MDTOD_OFFPEAK,0)TOD_OFFPEAK,\r\n"
					+ "NVL(E.COLOPNRDG,0)COLOPNRDG, NVL(E.COLCLRDG,0)COLCLRDG,NVL(E.COLMTRMF,0)COLMTRMF,NVL(E.COLMETKVAH,0)COLMETKVAH,NVL(F.SKWH_OPN,0)SKWH_OPN, NVL(F.SKWH_CLS,0)SKWH_CLS,\r\n"
					+ "NVL(F.SRECKWH,0)SRECKWH,NVL(G.KVAH_ADJ_ENG,0)OA_KVAH_ADJ_ENG,NVL(G.OA_PEAK,0)OA_PEAK,NVL(G.OA_OFFPEAK,0)OA_OFFPEAK\r\n"
					+ "FROM \r\n"
					+ "(SELECT * FROM CONS) A,(SELECT USCNO,MON_YEAR,CAT,SCAT,SUM(LOAD)LOAD FROM LEDGER_HT_HIST WHERE MON_YEAR=? GROUP BY USCNO,MON_YEAR,CAT,SCAT) B,\r\n"
					+ "(SELECT * FROM SERVTYPE) C,\r\n"
					+ "(SELECT MDMONTH,MSCNO, SUM(MDOPNKWH_HT)MDOPNKWH_HT, SUM(MDCLKWH_HT)MDCLKWH_HT,\r\n"
					+ "SUM(MDRKWH_HT)MDRKWH_HT,SUM(MDMF_HT)MDMF_HT,SUM(MDRECKWH_HT)MDRECKWH_HT,SUM(MDOPNKVAH_HT)MDOPNKVAH_HT, SUM(MDCLKVAH_HT)MDCLKVAH_HT, \r\n"
					+ "SUM(MDRKVAH_HT)MDRKVAH_HT, SUM(MDRECKVAH_HT)MDRECKVAH_HT,SUM(MDTOD1_OPN)MDTOD1_OPN,SUM(MDTOD1_CLS)MDTOD1_CLS,SUM(MDTOD1_RECKVAH)MDTOD1_RECKVAH,\r\n"
					+ "SUM(MDTOD2_OPN)MDTOD2_OPN, SUM(MDTOD2_CLS)MDTOD2_CLS,SUM(MDTOD2_RECKVAH)MDTOD2_RECKVAH,SUM(MDTOD3_OPN)MDTOD3_OPN, \r\n"
					+ "SUM(MDTOD3_CLS)MDTOD3_CLS,SUM(MDTOD3_RECKVAH)MDTOD3_RECKVAH,SUM(MDTOD4_OPN)MDTOD4_OPN, \r\n"
					+ "SUM(MDTOD4_CLS)MDTOD4_CLS,SUM(MDTOD4_RECKVAH)MDTOD4_RECKVAH,SUM(MDTOD5_OPN)MDTOD5_OPN, SUM(MDTOD5_CLS)MDTOD5_CLS,SUM(MDTOD5_RECKVAH)MDTOD5_RECKVAH,\r\n"
					+ "SUM(MDTOD6_OPN)MDTOD6_OPN, SUM(MDTOD6_CLS)MDTOD6_CLS,SUM(MDTOD6_RECKVAH)MDTOD6_RECKVAH,\r\n"
					+ "SUM(MDTOD1_RECKVAH)+SUM(MDTOD2_RECKVAH)+SUM(MDTOD3_RECKVAH)+SUM(MDTOD4_RECKVAH)+SUM(MDTOD5_RECKVAH)+SUM(MDTOD6_RECKVAH) TOD_TOTAL_UNITS,\r\n"
					+ "SUM(MDTOD_PEAK)MDTOD_PEAK,SUM(MDTOD_OFFPEAK)MDTOD_OFFPEAK FROM MTRDAT_HIST \r\n"
					+ "WHERE TO_CHAR(MDMONTH,'MON-YYYY')=?\r\n" + "GROUP BY MSCNO,MDMONTH)D,\r\n"
					+ "(SELECT COLSCNO,COLCONMTH,SUM(COLOPNRDG)COLOPNRDG, SUM(COLCLRDG)COLCLRDG,SUM(COLMTRMF)COLMTRMF,SUM(COLMETKVAH) COLMETKVAH FROM CLMETDAT_HIST \r\n"
					+ "WHERE TO_CHAR(COLCONMTH,'MON-YYYY')=?\r\n" + "GROUP BY COLSCNO,COLCONMTH)E,\r\n"
					+ "(SELECT SUSCNO,SMONTH,SUM(SKWH_OPN)SKWH_OPN, SUM(SKWH_CLS)SKWH_CLS,SUM(SRECKWH)SRECKWH FROM MTRDAT_SOLAR_HIST \r\n"
					+ "WHERE TO_CHAR(SMONTH,'MON-YYYY')=?\r\n" + "GROUP BY SUSCNO,SMONTH)F,\r\n"
					+ "(SELECT OAUSCNO,BILL_MON ||'-'|| BILL_YEAR BDATE,SUM(KVAH_ADJ_ENG)KVAH_ADJ_ENG,SUM(TOD_ADJ_PEAK)OA_PEAK,SUM(TOD_ADJ_OFF)OA_OFFPEAK FROM OPEN_ACCESS_HIST \r\n"
					+ "WHERE BILL_MON ||'-'|| BILL_YEAR=?\r\n"
					+ "GROUP BY OAUSCNO,BILL_MON ||'-'|| BILL_YEAR) G,MASTER.SPDCLMASTER\r\n"
					+ "WHERE A.CTUSCNO=B.USCNO(+) AND A.CTSERVTYPE=C.STCODE(+) AND A.CTUSCNO=D.MSCNO(+) AND A.CTUSCNO=E.COLSCNO(+) AND A.CTUSCNO=F.SUSCNO(+) \r\n"
					+ "AND A.CTUSCNO=G.OAUSCNO(+) AND SUBSTR(CTSECCD,-5)=SECCD(+) \r\n" + circleString + serviceString
					+ "ORDER BY A.CTUSCNO,A.CTNAME,B.CAT, B.SCAT,MON_YEAR,TYPE,VOLTAGE,STATUS";
			log.info(sql);
			return jdbcTemplate.queryForList(sql, new Object[] { mon_year, mon_year, mon_year, mon_year, mon_year });
		} catch (DataAccessException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			e.printStackTrace();
			return Collections.emptyList();
		}
	}
	
	
    //100 For Nos
	public List<Map<String, Object>> getServicetypewiseabstarctForNOS(String circle, String month, String service,
			String year) {
		String mon_year = month + "-" + year;
		try {
			String sql = "Select b.CTSERVTYPE servtype,c.stdesc,ctuscno uscno,SUM(LOAD) LOAD,SUM(REC_MD) REC_MD,\r\n"
					+ "SUM(Round(Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0))) Ob,\r\n"
					+ "SUM(Mn_Kvah) Sales\r\n"
					+ ",SUM(round(Nvl(Cmd,0)+Nvl(Cclpc,0))) Demand,SUM(Nvl(round(CASE WHEN Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)>0 THEN CASE WHEN Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)>(NVL(Tot_Pay,0)) THEN (NVL(Tot_Pay,0)) ELSE Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0) END END),0)) COLL_ARREAR,\r\n"
					+ "SUM(Nvl(round(CASE WHEN Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)>0 THEN CASE WHEN Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)<(NVL(Tot_Pay,0)) THEN (NVL(Tot_Pay,0)-(Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0))) END ELSE (NVL(Tot_Pay,0)) END ),0)) COLL_DEMAND,SUM(round(Nvl(Tot_Pay,0))) Collection,\r\n"
					+ "SUM(round(Nvl(Rj_Oth,0)+Nvl(Drj,0)+Nvl(Rj_Cclpc,0))) Drj,SUM(round(Nvl(Crj,0))) Crj,SUM(round(Nvl(Cbtot,0)+Nvl(Cb_Oth,0)+Nvl(Cb_Cclpc,0))) Cb\r\n"
					+ "From (select LHH.*,'' STATUS_NEW, '' GOVT_PVT from Ledger_Ht_HIST LHH where Mon_Year=? union all select * from accountcopy where Mon_Year=?) A,CONS B,servtype C\r\n"
					+ "Where A.Uscno=B.CTUscno \r\n"
					+ "And b.ctservtype=c.stcode \r\n"
					+ "and CTSERVTYPE=?\r\n"
					+ "AND SUBSTR(CTUSCNO,1,3)= ? \r\n"
					+ "GROUP BY  b.ctservtype,c.stdesc,ctuscno\r\n"
					+ "Order By 2,3,4";
			log.info(sql);
			return jdbcTemplate.queryForList(sql, new Object[] {mon_year , mon_year , service , circle });
		} catch (DataAccessException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

}
