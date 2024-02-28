package com.spdcl.dao;

import java.sql.CallableStatement;
import java.sql.Types;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
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

	public List<Map<String, Object>> getFinancialConsumption(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		String fin_year = request.getParameter("year");
		String fromdate = "01-APR-" + fin_year.split("-")[0];
		String todate = "31-MAR-" + fin_year.split("-")[1];
		if (circle.equals("ALL")) {
			String sql = "SELECT CIRCLE,HT1scs,HT1units,HT1demand,HT1ec,HT1SPECIFIC_CONSUMPTION,HT1SPECIFIC_REVENUE,\r\n"
					+ "HT2scs,HT2units,HT2demand,HT2ec,HT2SPECIFIC_CONSUMPTION,HT2SPECIFIC_REVENUE,\r\n"
					+ "HT3scs,HT3units,HT3demand,HT3ec,HT3SPECIFIC_CONSUMPTION,HT3SPECIFIC_REVENUE,\r\n"
					+ "HT4scs,HT4units,HT4demand,HT4ec,HT4SPECIFIC_CONSUMPTION,HT4SPECIFIC_REVENUE,\r\n"
					+ "HT5Bscs,HT5Bunits,HT5Bdemand,HT5Bec,HT5BSPECIFIC_CONSUMPTION,HT5BSPECIFIC_REVENUE,\r\n"
					+ "HT5Escs,HT5Eunits,HT5Edemand,HT5Eec,HT5ESPECIFIC_CONSUMPTION,HT5ESPECIFIC_REVENUE\r\n" + "\r\n"
					+ "FROM (SELECT CIRCLE                       \r\n" + "\r\n"
					+ ",SUM(case when CTCAT='HT1' then SCS END) HT1scs,\r\n"
					+ "ROUND(SUM(case when CTCAT='HT1' then UNITS END)) HT1units,\r\n"
					+ "ROUND(SUM(case when CTCAT='HT1' then DEMAND END)) HT1demand,\r\n"
					+ "ROUND(SUM(case when CTCAT='HT1' then EC END)) HT1ec,\r\n"
					+ "ROUND(SUM(case when CTCAT='HT1' then SPECIFIC_CONSUMPTION END)) HT1SPECIFIC_CONSUMPTION,\r\n"
					+ "ROUND(SUM(case when CTCAT='HT1' then SPECIFIC_REVENUE END)) HT1SPECIFIC_REVENUE\r\n" + "\r\n"
					+ ",SUM(case when CTCAT='HT2' then SCS END) HT2scs,\r\n"
					+ "ROUND(SUM(case when CTCAT='HT2' then UNITS END)) HT2units,\r\n"
					+ "ROUND(SUM(case when CTCAT='HT2' then DEMAND END)) HT2demand,\r\n"
					+ "ROUND(SUM(case when CTCAT='HT2' then EC END)) HT2ec,\r\n"
					+ "ROUND(SUM(case when CTCAT='HT2' then SPECIFIC_CONSUMPTION END)) HT2SPECIFIC_CONSUMPTION,\r\n"
					+ "ROUND(SUM(case when CTCAT='HT2' then SPECIFIC_REVENUE END)) HT2SPECIFIC_REVENUE\r\n" + "\r\n"
					+ ",SUM(case when CTCAT='HT3' then SCS END) HT3scs,\r\n"
					+ "ROUND(SUM(case when CTCAT='HT3' then UNITS END)) HT3units,\r\n"
					+ "ROUND(SUM(case when CTCAT='HT3' then DEMAND END)) HT3demand,\r\n"
					+ "ROUND(SUM(case when CTCAT='HT3' then EC END)) HT3ec,\r\n"
					+ "ROUND(SUM(case when CTCAT='HT3' then SPECIFIC_CONSUMPTION END)) HT3SPECIFIC_CONSUMPTION,\r\n"
					+ "ROUND(SUM(case when CTCAT='HT3' then SPECIFIC_REVENUE END)) HT3SPECIFIC_REVENUE\r\n" + "\r\n"
					+ ",SUM(case when CTCAT='HT4' then SCS END) HT4scs,\r\n"
					+ "ROUND(SUM(case when CTCAT='HT4' then UNITS END)) HT4units,\r\n"
					+ "ROUND(SUM(case when CTCAT='HT4' then DEMAND END)) HT4demand,\r\n"
					+ "ROUND(SUM(case when CTCAT='HT4' then EC END)) HT4ec,\r\n"
					+ "ROUND(SUM(case when CTCAT='HT4' then SPECIFIC_CONSUMPTION END)) HT4SPECIFIC_CONSUMPTION,\r\n"
					+ "ROUND(SUM(case when CTCAT='HT4' then SPECIFIC_REVENUE END)) HT4SPECIFIC_REVENUE\r\n" + "\r\n"
					+ ",SUM(case when CTCAT='HT5B' then SCS END) HT5Bscs,\r\n"
					+ "ROUND(SUM(case when CTCAT='HT5B' then UNITS END)) HT5Bunits,\r\n"
					+ "ROUND(SUM(case when CTCAT='HT5B' then DEMAND END)) HT5Bdemand,\r\n"
					+ "ROUND(SUM(case when CTCAT='HT5B' then EC END)) HT5Bec,\r\n"
					+ "ROUND(SUM(case when CTCAT='HT5B' then SPECIFIC_CONSUMPTION END)) HT5BSPECIFIC_CONSUMPTION,\r\n"
					+ "ROUND(SUM(case when CTCAT='HT5B' then SPECIFIC_REVENUE END)) HT5BSPECIFIC_REVENUE\r\n" + "\r\n"
					+ ",SUM(case when CTCAT='HT5E' then SCS END) HT5Escs,\r\n"
					+ "ROUND(SUM(case when CTCAT='HT5E' then UNITS END)) HT5Eunits,\r\n"
					+ "ROUND(SUM(case when CTCAT='HT5E' then DEMAND END)) HT5Edemand,\r\n"
					+ "ROUND(SUM(case when CTCAT='HT5E' then EC END)) HT5Eec,\r\n"
					+ "ROUND(SUM(case when CTCAT='HT5E' then SPECIFIC_CONSUMPTION END)) HT5ESPECIFIC_CONSUMPTION,\r\n"
					+ "ROUND(SUM(case when CTCAT='HT5E' then SPECIFIC_REVENUE END)) HT5ESPECIFIC_REVENUE\r\n" + "\r\n"
					+ "FROM\r\n" + "(SELECT SUBSTR(CTUSCNO,1,3)CIRCLE, \r\n"
					+ "case when CTCAT='HT5' AND CTSUBCAT='B' THEN 'HT5B'\r\n"
					+ "WHEN CTCAT='HT5' AND CTSUBCAT='E' THEN 'HT5E' ELSE CTCAT END CTCAT,\r\n" + "COUNT(*)SCS,\r\n"
					+ "SUM(MN_KVAH) UNITS,\r\n" + "SUM(DEMAND) DEMAND,\r\n" + "SUM(EC)EC,\r\n"
					+ "ROUND(SUM(MN_KVAH)/COUNT(*),2) SPECIFIC_CONSUMPTION,\r\n"
					+ "ROUND(SUM(EC)/SUM(MN_KVAH),2) SPECIFIC_REVENUE FROM CONS,\r\n"
					+ "(SELECT USCNO,SUM(MN_KVAH)MN_KVAH,SUM(NVL(CMD,0) +NVL(CCLPC,0)+NVL(DRJ,0)+NVL(RJ_CCLPC,0)+NVL(RJ_OTH,0))DEMAND,\r\n"
					+ "SUM(NVL(BTENGCHG_NOR,0)+ NVL(BTENGCHG_PEN,0))EC\r\n" + "FROM LEDGER_HT_HIST ,BILL_HIST \r\n"
					+ "WHERE uscno = btscno and to_char(BTBLDT,'MON-YYYY')=MON_YEAR and to_date(MON_YEAR,'MON-YYYY') between \r\n"
					+ "to_date(?,'DD-MM-YYYY') and to_date(?,'DD-MM-YYYY')\r\n" + "GROUP BY USCNO)\r\n"
					+ "WHERE CTUSCNO=USCNO  \r\n"
					+ "GROUP BY SUBSTR(CTUSCNO,1,3),case when CTCAT='HT5' AND CTSUBCAT='B' THEN 'HT5B'\r\n"
					+ "WHEN CTCAT='HT5' AND CTSUBCAT='E' THEN 'HT5E' ELSE CTCAT END\r\n"
					+ "ORDER BY CIRCLE,CTCAT)GROUP BY CIRCLE ORDER BY CIRCLE)";
			log.info(sql);
			return jdbcTemplate.queryForList(sql, new Object[] { fromdate, todate });
		} else {
			String sql = "SELECT CIRCLE,HT1scs,HT1units,HT1demand,HT1ec,HT1SPECIFIC_CONSUMPTION,HT1SPECIFIC_REVENUE,\r\n"
					+ "HT2scs,HT2units,HT2demand,HT2ec,HT2SPECIFIC_CONSUMPTION,HT2SPECIFIC_REVENUE,\r\n"
					+ "HT3scs,HT3units,HT3demand,HT3ec,HT3SPECIFIC_CONSUMPTION,HT3SPECIFIC_REVENUE,\r\n"
					+ "HT4scs,HT4units,HT4demand,HT4ec,HT4SPECIFIC_CONSUMPTION,HT4SPECIFIC_REVENUE,\r\n"
					+ "HT5Bscs,HT5Bunits,HT5Bdemand,HT5Bec,HT5BSPECIFIC_CONSUMPTION,HT5BSPECIFIC_REVENUE,\r\n"
					+ "HT5Escs,HT5Eunits,HT5Edemand,HT5Eec,HT5ESPECIFIC_CONSUMPTION,HT5ESPECIFIC_REVENUE\r\n" + "\r\n"
					+ "FROM (SELECT CIRCLE                       \r\n" + "\r\n"
					+ ",SUM(case when CTCAT='HT1' then SCS END) HT1scs,\r\n"
					+ "SUM(case when CTCAT='HT1' then UNITS END) HT1units,\r\n"
					+ "ROUND(SUM(case when CTCAT='HT1' then DEMAND END)) HT1demand,\r\n"
					+ "ROUND(SUM(case when CTCAT='HT1' then EC END)) HT1ec,\r\n"
					+ "ROUND(SUM(case when CTCAT='HT1' then SPECIFIC_CONSUMPTION END)) HT1SPECIFIC_CONSUMPTION,\r\n"
					+ "ROUND(SUM(case when CTCAT='HT1' then SPECIFIC_REVENUE END)) HT1SPECIFIC_REVENUE\r\n" + "\r\n"
					+ ",SUM(case when CTCAT='HT2' then SCS END) HT2scs,\r\n"
					+ "ROUND(SUM(case when CTCAT='HT2' then UNITS END)) HT2units,\r\n"
					+ "ROUND(SUM(case when CTCAT='HT2' then DEMAND END)) HT2demand,\r\n"
					+ "ROUND(SUM(case when CTCAT='HT2' then EC END)) HT2ec,\r\n"
					+ "ROUND(SUM(case when CTCAT='HT2' then SPECIFIC_CONSUMPTION END)) HT2SPECIFIC_CONSUMPTION,\r\n"
					+ "ROUND(SUM(case when CTCAT='HT2' then SPECIFIC_REVENUE END)) HT2SPECIFIC_REVENUE\r\n" + "\r\n"
					+ ",SUM(case when CTCAT='HT3' then SCS END) HT3scs,\r\n"
					+ "ROUND(SUM(case when CTCAT='HT3' then UNITS END)) HT3units,\r\n"
					+ "ROUND(SUM(case when CTCAT='HT3' then DEMAND END)) HT3demand,\r\n"
					+ "ROUND(SUM(case when CTCAT='HT3' then EC END)) HT3ec,\r\n"
					+ "ROUND(SUM(case when CTCAT='HT3' then SPECIFIC_CONSUMPTION END)) HT3SPECIFIC_CONSUMPTION,\r\n"
					+ "ROUND(SUM(case when CTCAT='HT3' then SPECIFIC_REVENUE END)) HT3SPECIFIC_REVENUE\r\n" + "\r\n"
					+ ",SUM(case when CTCAT='HT4' then SCS END) HT4scs,\r\n"
					+ "ROUND(SUM(case when CTCAT='HT4' then UNITS END)) HT4units,\r\n"
					+ "ROUND(SUM(case when CTCAT='HT4' then DEMAND END)) HT4demand,\r\n"
					+ "ROUND(SUM(case when CTCAT='HT4' then EC END)) HT4ec,\r\n"
					+ "ROUND(SUM(case when CTCAT='HT4' then SPECIFIC_CONSUMPTION END)) HT4SPECIFIC_CONSUMPTION,\r\n"
					+ "ROUND(SUM(case when CTCAT='HT4' then SPECIFIC_REVENUE END)) HT4SPECIFIC_REVENUE\r\n" + "\r\n"
					+ ",SUM(case when CTCAT='HT5B' then SCS END) HT5Bscs,\r\n"
					+ "ROUND(SUM(case when CTCAT='HT5B' then UNITS END)) HT5Bunits,\r\n"
					+ "ROUND(SUM(case when CTCAT='HT5B' then DEMAND END)) HT5Bdemand,\r\n"
					+ "ROUND(SUM(case when CTCAT='HT5B' then EC END)) HT5Bec,\r\n"
					+ "ROUND(SUM(case when CTCAT='HT5B' then SPECIFIC_CONSUMPTION END)) HT5BSPECIFIC_CONSUMPTION,\r\n"
					+ "ROUND(SUM(case when CTCAT='HT5B' then SPECIFIC_REVENUE END)) HT5BSPECIFIC_REVENUE\r\n" + "\r\n"
					+ ",SUM(case when CTCAT='HT5E' then SCS END) HT5Escs,\r\n"
					+ "ROUND(SUM(case when CTCAT='HT5E' then UNITS END)) HT5Eunits,\r\n"
					+ "ROUND(SUM(case when CTCAT='HT5E' then DEMAND END)) HT5Edemand,\r\n"
					+ "ROUND(SUM(case when CTCAT='HT5E' then EC END)) HT5Eec,\r\n"
					+ "ROUND(SUM(case when CTCAT='HT5E' then SPECIFIC_CONSUMPTION END)) HT5ESPECIFIC_CONSUMPTION,\r\n"
					+ "ROUND(SUM(case when CTCAT='HT5E' then SPECIFIC_REVENUE END)) HT5ESPECIFIC_REVENUE\r\n" + "\r\n"
					+ "FROM\r\n" + "(SELECT SUBSTR(CTUSCNO,1,3)CIRCLE, \r\n"
					+ "case when CTCAT='HT5' AND CTSUBCAT='B' THEN 'HT5B'\r\n"
					+ "WHEN CTCAT='HT5' AND CTSUBCAT='E' THEN 'HT5E' ELSE CTCAT END CTCAT,\r\n" + "COUNT(*)SCS,\r\n"
					+ "SUM(MN_KVAH) UNITS,\r\n" + "SUM(DEMAND) DEMAND,\r\n" + "SUM(EC)EC,\r\n"
					+ "ROUND(SUM(MN_KVAH)/COUNT(*),2) SPECIFIC_CONSUMPTION,\r\n"
					+ "ROUND(SUM(EC)/SUM(MN_KVAH),2) SPECIFIC_REVENUE FROM CONS,\r\n"
					+ "(SELECT USCNO,SUM(MN_KVAH)MN_KVAH,SUM(NVL(CMD,0) +NVL(CCLPC,0)+NVL(DRJ,0)+NVL(RJ_CCLPC,0)+NVL(RJ_OTH,0))DEMAND,\r\n"
					+ "SUM(NVL(BTENGCHG_NOR,0)+ NVL(BTENGCHG_PEN,0))EC\r\n" + "FROM LEDGER_HT_HIST ,BILL_HIST \r\n"
					+ "WHERE uscno = btscno and to_char(BTBLDT,'MON-YYYY')=MON_YEAR and to_date(MON_YEAR,'MON-YYYY') between \r\n"
					+ "to_date(?,'DD-MM-YYYY') and to_date(?,'DD-MM-YYYY')\r\n" + "GROUP BY USCNO)\r\n"
					+ "WHERE CTUSCNO=USCNO  \r\n"
					+ "GROUP BY SUBSTR(CTUSCNO,1,3),case when CTCAT='HT5' AND CTSUBCAT='B' THEN 'HT5B'\r\n"
					+ "WHEN CTCAT='HT5' AND CTSUBCAT='E' THEN 'HT5E' ELSE CTCAT END\r\n" + "ORDER BY CIRCLE,CTCAT) \r\n"
					+ "WHERE CIRCLE=? \r\n" + "GROUP BY CIRCLE ORDER BY CIRCLE)";

			log.info(sql);

			return jdbcTemplate.queryForList(sql, new Object[] { fromdate, todate, circle });
		}
	}

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

	public List<Map<String, Object>> getFyConsumption2(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		String fromYear = "01-APR-" + request.getParameter("fyear");
		String toYear = "31-MAR-" + request.getParameter("tyear");
		
		System.out.println(fromYear);
        
		SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate.getDataSource()).withProcedureName("FINANCIAL_YEAR_CONSUMOTION_REPORT");
		Map<String, String> inputs = new HashMap<String, String>();
		inputs.put("CIR",circle);
		inputs.put("FROM_YEAR",fromYear);
		inputs.put("TO_YEAR", toYear);
		
		log.info("Executing Procedure { exec HT_BILL_CALC ('" +circle + "','" + fromYear + "','"
				+ toYear + "'}'");
		SqlParameterSource in = new MapSqlParameterSource().addValues(inputs);
		Map<String, Object> out = jdbcCall.execute(in);
		log.info("Procedure Result " + out);
		
		
		return  (List<Map<String, Object>>) out.get("PRC");
    }
	
}
