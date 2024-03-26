package com.spdcl.dao;

import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.math.Fraction;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.spdcl.model.ConsumerDetails;
import com.spdcl.model.IdTypes;

@Repository
@Transactional
public class ReportDao {

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

	private static Logger log = Logger.getLogger(ReportDao.class);

	public String getCoustomerName() {
		String sql = "SELECT CTNAME FROM CONS WHERE CTUSCNO = ?";
		String name = jdbcTemplate.queryForObject(sql, String.class, "TPT019");
		return name;

	}

	public String getCoustomerSD(String uscno) {
		String sql = "select NVL(CB_SD,'0') SD from ledger_ht_hist where to_date(MON_YEAR,'MON-YYYY') = (select max(to_date(MON_YEAR,'MON-YYYY')) from ledger_ht_hist where uscno = ? ) and uscno = ?";
		return jdbcTemplate.queryForObject(sql, new Object[] { uscno, uscno }, String.class);

	}

	public Map<String, Object> getCircleList() {
		String sql = "SELECT CIRCLE_CODE,CIRNAME FROM CIRCLE_CODE ORDER BY CIRCLE_CODE";
		return jdbcTemplate.query(sql, (ResultSet rs) -> {
			LinkedHashMap<String, Object> results = new LinkedHashMap<>();
			while (rs.next()) {
				results.put(rs.getString("CIRCLE_CODE"), rs.getString("CIRNAME"));
			}
			return results;
		});
	}

	public Map<String, Object> getCategoryList() {
		String sql = "select cat,cat_desc from category_ht where FROM_DT in (select max(FROM_DT) from category_ht) group by cat,cat_desc order by cat";
		return jdbcTemplate.query(sql, (ResultSet rs) -> {
			LinkedHashMap<String, Object> results = new LinkedHashMap<>();
			while (rs.next()) {
				results.put(rs.getString("cat"), rs.getString("cat") + "   (" + rs.getString("cat_desc") + ")");
			}
			return results;
		});
	}

	/*
	 * public Map<String, Object> getHODDepts() { String sql =
	 * "select distinct gmdeptcode GMDEP ,gmdeptname DEPT_NAME from deptmast where   gmdeptname is not null order by gmdeptname"
	 * ; log.info(sql); return jdbcTemplate.query(sql, (ResultSet rs) -> {
	 * LinkedHashMap<String, Object> results = new LinkedHashMap<>(); while
	 * (rs.next()) { results.put(rs.getString("GMDEP"), rs.getString("DEPT_NAME"));
	 * } return results; }); }
	 */

	public Map<String, Object> getHODDepts() {
		String sql = "select distinct GMDEP,DEPT_NAME from SPDCL_HOD_HT where GMDEP is not null order by DEPT_NAME";
		return jdbcTemplate.query(sql, (ResultSet rs) -> {
			LinkedHashMap<String, Object> results = new LinkedHashMap<>();
			while (rs.next()) {
				results.put(rs.getString("GMDEP"), rs.getString("DEPT_NAME"));
			}
			return results;
		});
	}

	public List<Map<String, Object>> getSingleLineIsdReportDetails(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		String fin_year = request.getParameter("year");
		if (!circle.equals("ALL")) {
			try {
				String sql = "SELECT ISCNO,CTCAT,FIN_YEAR,round(NVL(OB_SD,0)) OB_SD,round(NVL(PAID_ACD,0)) PAID_ACD,round(NVL(RJ_SD,0)) RJ_SD,round(NVL(CB_SD,0)) CB_SD, round(NVL(TOT_ISD,0)) TOT_ISD ,round(NVL(TDS_AMT,0))TDS_AMT,round(NVL(NET_ISD,0)) NET_ISD \r\n"
						+ "FROM SD_LEDGER_TEMP,CONS\r\n"
						+ "WHERE CTUSCNO=ISCNO AND FIN_YEAR=? AND CTSTATUS!=0 AND SUBSTR(ISCNO,1,3)=? AND LEVI_FLG='P'  ORDER BY CTCAT";
				log.info(sql);
				return jdbcTemplate.queryForList(sql, new Object[] { fin_year, circle });
			} catch (DataAccessException e) {
				log.error(e);
				return Collections.emptyList();
			}
		} else {
			try {
				String sql = "SELECT ISCNO,CTCAT,FIN_YEAR,round(NVL(OB_SD,0)) OB_SD,round(NVL(PAID_ACD,0)) PAID_ACD,round(NVL(RJ_SD,0)) RJ_SD,round(NVL(CB_SD,0)) CB_SD, round(NVL(TOT_ISD,0)) TOT_ISD ,round(NVL(TDS_AMT,0))TDS_AMT,round(NVL(NET_ISD,0)) NET_ISD \r\n"
						+ "FROM SD_LEDGER_TEMP,CONS\r\n"
						+ "WHERE CTUSCNO=ISCNO AND FIN_YEAR=?  AND CTSTATUS!=0  AND LEVI_FLG='P'  ORDER BY CTCAT";
				log.info(sql);
				return jdbcTemplate.queryForList(sql, new Object[] { fin_year });
			} catch (DataAccessException e) {
				log.error(e);
				return Collections.emptyList();
			}

		}
	}

	public List<Map<String, Object>> getTDSReportDetails(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		String fin_year = request.getParameter("year");
		if (!circle.equals("ALL")) {
			try {
				String sql = "select C.CTUSCNO,C.CTCAT,S.CTPANNO,SD.FIN_YEAR,SD.TOT_ISD,SD.TDS_AMT,STDESC,DECODE(C.CTGOVT_PVT,'Y','GOVT','NON-GOVT') TYPE from (\r\n"
						+ "select CTUSCNO,CTPANNO from cons where substr(CTUSCNO,1,3) in ('VJA','CRD','ONG','GNT')  and \r\n"
						+ "CTUSCNO not in (select uscno CTUSCNO   from ID_TYPES where ID_TYPE_CD='PAN' and substr(uscno,1,3) in ('VJA','CRD','ONG','GNT'))\r\n"
						+ "union \r\n"
						+ "select uscno CTUSCNO ,per_id_nbr CTPANNO  from ID_TYPES where ID_TYPE_CD='PAN' and substr(uscno,1,3) in ('VJA','CRD','ONG','GNT') ) S ,CONS C, SD_LEDGER_TEMP SD,SERVTYPE \r\n"
						+ "where S.CTUSCNO=C.CTUSCNO(+)\r\n" + "and C.CTUSCNO = ISCNO(+)\r\n"
						+ " and C.CTSERVTYPE= STCODE " + "and SD.FIN_YEAR =? " + " and  C.CTSTATUS!=0 "
						+ " and  SD.LEVI_FLG='P' " + " and substr(C.CTUSCNO,1,3) = ? order by C.CTUSCNO";
				log.info(sql);
				return jdbcTemplate.queryForList(sql, new Object[] { fin_year, circle });
			} catch (DataAccessException e) {
				log.error(e);
				return Collections.emptyList();
			}
		} else {
			try {
				String sql = "select C.CTUSCNO,C.CTCAT,S.CTPANNO,SD.FIN_YEAR,SD.TOT_ISD,SD.TDS_AMT,STDESC,DECODE(C.CTGOVT_PVT,'Y','GOVT','NON-GOVT') TYPE from (\r\n"
						+ "select CTUSCNO,CTPANNO from cons where substr(CTUSCNO,1,3) in ('VJA','CRD','ONG','GNT')  and \r\n"
						+ "CTUSCNO not in (select uscno CTUSCNO   from ID_TYPES where ID_TYPE_CD='PAN' and substr(uscno,1,3) in ('VJA','CRD','ONG','GNT'))\r\n"
						+ "union \r\n"
						+ "select uscno CTUSCNO ,per_id_nbr CTPANNO  from ID_TYPES where ID_TYPE_CD='PAN' and substr(uscno,1,3) in ('VJA','CRD','ONG','GNT') ) S ,CONS C, SD_LEDGER_TEMP SD,SERVTYPE\r\n"
						+ "where S.CTUSCNO=C.CTUSCNO(+)\r\n" + "and C.CTUSCNO = ISCNO(+)\r\n"
						+ " and C.CTSERVTYPE= STCODE " + " and  C.CTSTATUS!=0 " + " and  SD.LEVI_FLG='P' "
						+ "and SD.FIN_YEAR =? order by C.CTUSCNO";
				log.info(sql);
				return jdbcTemplate.queryForList(sql, new Object[] { fin_year });
			} catch (DataAccessException e) {
				log.error(e);
				return Collections.emptyList();
			}

		}
	}

	public List<Map<String, Object>> getHTSalesDCBDetails(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		String cat = request.getParameter("cat");
		String fin_year = request.getParameter("year");
		String fromdate = "01-APR-" + fin_year.split("-")[0];
		String todate = "31-MAR-" + fin_year.split("-")[1];
		String cbtodate = "01-MAR-" + fin_year.split("-")[1];
		String sql;
		if (!circle.equals("ALL")) {
			try {
				if (cat.equals("CAT")) {
					sql = "SELECT CASE WHEN CTCAT='HT4' AND CTSUBCAT='A' THEN CTCAT||'-(A)'\r\n"
							+ "            WHEN CTCAT='HT4' AND CTSUBCAT='D' THEN CTCAT||'-(D)'\r\n"
							+ "            WHEN CTCAT='HT4' AND CTSUBCAT IN('B','C') THEN CTCAT||'-(B AND C)' ELSE CTCAT END CATEGORY,\r\n"
							+ "count(distinct(case when LDT between ? and  ? then CASE WHEN STAT>0 THEN USCNO END END)) nos,\r\n"
							+ "ROUND(SUM(case when LDT between ? and  ? then NVL(REC_KWH,0)/1000000 END),2) REC_KWH,\r\n"
							+ "ROUND(SUM(case when LDT between ? and  ? then NVL(MN_KVAH,0)/1000000 END),2) MN_KVAH,\r\n"
							+ "ROUND(SUM(case when TRUNC(LDT,'MM')= ?  then NVL(OB,0)/10000000 END),2) OB,\r\n"
							+ "ROUND(SUM(case when LDT between ? and  ? then NVL(DEMAND,0)/10000000 END),2) DEM,\r\n"
							+ "ROUND(SUM(case when LDT between ? and  ? then NVL(COLLECTION,0)/10000000 END),2) COLL,\r\n"
							+ "ROUND(SUM(case when TRUNC(LDT,'MM')=?  then NVL(CB,0)/10000000 END),2) CB\r\n"
							+ "FROM CONS,\r\n"
							+ "(SELECT USCNO ,TRUNC(TO_DATE(MON_YEAR,'MON-YYYY')) LDT,(DECODE(STATUS,NULL,0,'NEW',1,1,1,0)) STAT,\r\n"
							+ "(NVL(MN_KVAH,0)) MN_KVAH,(NVL(REC_KWH,0)) REC_KWH,Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0) OB,\r\n"
							+ "(NVL(CMD,0)+NVL(CCLPC,0)+Nvl(Rj_Oth,0)+Nvl(Drj,0)+Nvl(Rj_Cclpc,0) ) DEMAND,(Nvl(Tot_Pay,0)+NVL(CRJ,0)) COLLECTION,Nvl(Cbtot,0)+Nvl(Cb_Oth,0)+Nvl(Cb_Cclpc,0) cb\r\n"
							+ " from ledger_ht_hist  WHERE TO_DATE(MON_YEAR,'MON-YYYY') between  ? and ? AND SUBSTR(USCNO,1,3) IN('GNT','VJA','ONG','CRD'))\r\n"
							+ " WHERE USCNO=CTUSCNO AND SUBSTR(CTUSCNO,1,3)=? \r\n"
							+ "GROUP BY CASE WHEN CTCAT='HT4' AND CTSUBCAT='A' THEN CTCAT||'-(A)'\r\n"
							+ "            WHEN CTCAT='HT4' AND CTSUBCAT='D' THEN CTCAT||'-(D)'\r\n"
							+ "            WHEN CTCAT='HT4' AND CTSUBCAT IN('B','C') THEN CTCAT||'-(B AND C)' ELSE CTCAT END ORDER BY 1";

				} else {
					sql = "SELECT CTCAT CATEGORY,CTSUBCAT,\r\n"
							+ "count(distinct(case when LDT between ? and ? then CASE WHEN STAT>0 THEN USCNO END END)) nos,\r\n"
							+ "ROUND(SUM(case when LDT between ? and  ? then NVL(REC_KWH,0)/1000000 END),2) REC_KWH,\r\n"
							+ "ROUND(SUM(case when LDT between ? and  ? then NVL(MN_KVAH,0)/1000000 END),2) MN_KVAH,\r\n"
							+ "ROUND(SUM(case when TRUNC(LDT,'MM')=?  then NVL(OB,0)/10000000 END),2) OB,\r\n"
							+ "ROUND(SUM(case when LDT between ? and ? then NVL(DEMAND,0)/10000000 END),2) DEM,\r\n"
							+ "ROUND(SUM(case when LDT between ? and ? then NVL(COLLECTION,0)/10000000 END),2) COLL,\r\n"
							+ "ROUND(SUM(case when TRUNC(LDT,'MM')=?  then NVL(CB,0)/10000000 END),2) CB\r\n"
							+ "FROM CONS,\r\n"
							+ "(SELECT USCNO ,TRUNC(TO_DATE(MON_YEAR,'MON-YYYY')) LDT,(DECODE(STATUS,NULL,0,'NEW',1,1,1,0)) STAT,\r\n"
							+ "(NVL(MN_KVAH,0)) MN_KVAH,(NVL(REC_KWH,0)) REC_KWH,Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0) OB,\r\n"
							+ "(NVL(CMD,0)+NVL(CCLPC,0)+Nvl(Rj_Oth,0)+Nvl(Drj,0)+Nvl(Rj_Cclpc,0) ) DEMAND,(Nvl(Tot_Pay,0)+NVL(CRJ,0)) COLLECTION,Nvl(Cbtot,0)+Nvl(Cb_Oth,0)+Nvl(Cb_Cclpc,0) cb\r\n"
							+ " from ledger_ht_hist  WHERE TO_DATE(MON_YEAR,'MON-YYYY') between ? and ? AND SUBSTR(USCNO,1,3) IN('GNT','VJA','ONG','CRD'))\r\n"
							+ " WHERE USCNO=CTUSCNO AND SUBSTR(CTUSCNO,1,3)=? \r\n" + "GROUP BY CTCAT,CTSUBCAT\r\n"
							+ "ORDER BY CTCAT,CTSUBCAT";
				}
				log.info(sql);
				return jdbcTemplate.queryForList(sql, new Object[] { fromdate, todate, fromdate, todate, fromdate,
						todate, fromdate, fromdate, todate, fromdate, todate, cbtodate, fromdate, todate, circle });
			} catch (DataAccessException e) {
				log.error(e);
				return Collections.emptyList();
			}
		} else {
			try {
				if (cat.equals("CAT")) {
					sql = "SELECT CASE WHEN CTCAT='HT4' AND CTSUBCAT='A' THEN CTCAT||'-(A)'\r\n"
							+ "            WHEN CTCAT='HT4' AND CTSUBCAT='D' THEN CTCAT||'-(D)'\r\n"
							+ "            WHEN CTCAT='HT4' AND CTSUBCAT IN('B','C') THEN CTCAT||'-(B AND C)' ELSE CTCAT END CATEGORY,\r\n"
							+ "count(distinct(case when LDT between ? and  ? then CASE WHEN STAT>0 THEN USCNO END END)) nos,\r\n"
							+ "ROUND(SUM(case when LDT between ? and  ? then NVL(REC_KWH,0)/1000000 END),2) REC_KWH,\r\n"
							+ "ROUND(SUM(case when LDT between ? and  ? then NVL(MN_KVAH,0)/1000000 END),2) MN_KVAH,\r\n"
							+ "ROUND(SUM(case when TRUNC(LDT,'MM')= ?  then NVL(OB,0)/10000000 END),2) OB,\r\n"
							+ "ROUND(SUM(case when LDT between ? and  ? then NVL(DEMAND,0)/10000000 END),2) DEM,\r\n"
							+ "ROUND(SUM(case when LDT between ? and  ? then NVL(COLLECTION,0)/10000000 END),2) COLL,\r\n"
							+ "ROUND(SUM(case when TRUNC(LDT,'MM')=?  then NVL(CB,0)/10000000 END),2) CB\r\n"
							+ "FROM CONS,\r\n"
							+ "(SELECT USCNO ,TRUNC(TO_DATE(MON_YEAR,'MON-YYYY')) LDT,(DECODE(STATUS,NULL,0,'NEW',1,1,1,0)) STAT,\r\n"
							+ "(NVL(MN_KVAH,0)) MN_KVAH,(NVL(REC_KWH,0)) REC_KWH,Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0) OB,\r\n"
							+ "(NVL(CMD,0)+NVL(CCLPC,0)+Nvl(Rj_Oth,0)+Nvl(Drj,0)+Nvl(Rj_Cclpc,0) ) DEMAND,(Nvl(Tot_Pay,0)+NVL(CRJ,0)) COLLECTION,Nvl(Cbtot,0)+Nvl(Cb_Oth,0)+Nvl(Cb_Cclpc,0) cb\r\n"
							+ " from ledger_ht_hist  WHERE TO_DATE(MON_YEAR,'MON-YYYY') between  ? and ? AND SUBSTR(USCNO,1,3) IN('GNT','VJA','ONG','CRD'))\r\n"
							+ " WHERE USCNO=CTUSCNO \r\n"
							+ "GROUP BY CASE WHEN CTCAT='HT4' AND CTSUBCAT='A' THEN CTCAT||'-(A)'\r\n"
							+ "            WHEN CTCAT='HT4' AND CTSUBCAT='D' THEN CTCAT||'-(D)'\r\n"
							+ "            WHEN CTCAT='HT4' AND CTSUBCAT IN('B','C') THEN CTCAT||'-(B AND C)' ELSE CTCAT END ORDER BY 1";
				} else {

					sql = "SELECT CTCAT CATEGORY,CTSUBCAT,\r\n"
							+ "count(distinct(case when LDT between ? and ? then CASE WHEN STAT>0 THEN USCNO END END)) nos,\r\n"
							+ "ROUND(SUM(case when LDT between ? and  ? then NVL(REC_KWH,0)/1000000 END),2) REC_KWH,\r\n"
							+ "ROUND(SUM(case when LDT between ? and  ? then NVL(MN_KVAH,0)/1000000 END),2) MN_KVAH,\r\n"
							+ "ROUND(SUM(case when TRUNC(LDT,'MM')=?  then NVL(OB,0)/10000000 END),2) OB,\r\n"
							+ "ROUND(SUM(case when LDT between ? and ? then NVL(DEMAND,0)/10000000 END),2) DEM,\r\n"
							+ "ROUND(SUM(case when LDT between ? and ? then NVL(COLLECTION,0)/10000000 END),2) COLL,\r\n"
							+ "ROUND(SUM(case when TRUNC(LDT,'MM')=?  then NVL(CB,0)/10000000 END),2) CB\r\n"
							+ "FROM CONS,\r\n"
							+ "(SELECT USCNO ,TRUNC(TO_DATE(MON_YEAR,'MON-YYYY')) LDT,(DECODE(STATUS,NULL,0,'NEW',1,1,1,0)) STAT,\r\n"
							+ "(NVL(MN_KVAH,0)) MN_KVAH,(NVL(REC_KWH,0)) REC_KWH,Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0) OB,\r\n"
							+ "(NVL(CMD,0)+NVL(CCLPC,0)+Nvl(Rj_Oth,0)+Nvl(Drj,0)+Nvl(Rj_Cclpc,0) ) DEMAND,(Nvl(Tot_Pay,0)+NVL(CRJ,0)) COLLECTION,Nvl(Cbtot,0)+Nvl(Cb_Oth,0)+Nvl(Cb_Cclpc,0) cb\r\n"
							+ " from ledger_ht_hist  WHERE TO_DATE(MON_YEAR,'MON-YYYY') between ? and ? AND SUBSTR(USCNO,1,3) IN('GNT','VJA','ONG','CRD'))\r\n"
							+ " WHERE USCNO=CTUSCNO \r\n" + "GROUP BY CTCAT,CTSUBCAT\r\n" + "ORDER BY CTCAT,CTSUBCAT";
				}

				log.info(sql);
				return jdbcTemplate.queryForList(sql, new Object[] { fromdate, todate, fromdate, todate, fromdate,
						todate, fromdate, fromdate, todate, fromdate, todate, cbtodate, fromdate, todate });
			} catch (DataAccessException e) {
				log.error(e);
				return Collections.emptyList();
			}

		}
	}

	public List<Map<String, Object>> getISDCalculation(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		String fin_year = request.getParameter("year");
		if (circle.equals("ALL")) {
			try {
				String sql = "select A.*,B.CTNAME from (\r\n"
						+ "select  ACCT_ID,ISCNO,to_char(FROM_DT,'DD-MM-YYYY') FROM_DT,to_char(TO_DT,'DD-MM-YYYY')TO_DT,SD_SEG_AMT,0 SD_ADD ,ISD_SEG_AMT, 0 ADD_SEG_AMT,ISD_RATE,NUM_OF_DAYS from isd_calc_lines where from_dt  between  to_date('01-04-'||?,'DD-MM-YYYY') and to_date('31-03-'||?,'DD-MM-YYYY') \r\n"
						+ ") A , CONS B where CTUSCNO=ISCNO AND CTSTATUS!=0  order by ISCNO,FROM_DT\r\n";
				log.info(sql);
				return jdbcTemplate.queryForList(sql, new Object[] { fin_year.split("-")[0], fin_year.split("-")[1] });
			} catch (DataAccessException e) {
				log.error(e);
				return Collections.emptyList();
			}
		} else {
			try {

				String sql = "select A.*,B.CTNAME from (\r\n"
						+ "select  ACCT_ID,ISCNO,to_char(FROM_DT,'DD-MM-YYYY') FROM_DT,to_char(TO_DT,'DD-MM-YYYY')TO_DT,SD_SEG_AMT,0 SD_ADD ,ISD_SEG_AMT, 0 ADD_SEG_AMT,ISD_RATE,NUM_OF_DAYS from isd_calc_lines where from_dt  between  to_date('01-04-'||?,'DD-MM-YYYY') and to_date('31-03-'||?,'DD-MM-YYYY') \r\n"
						+ ") A , CONS B where CTUSCNO=ISCNO AND CTSTATUS!=0 AND substr(ISCNO,1,3)=?  order by ISCNO,FROM_DT\r\n";

				/*
				 * String sql = "select A.*,B.CTNAME from (\r\n" +
				 * "select  ACCT_ID,ISCNO,to_char(FROM_DT,'DD-MM-YYYY') FROM_DT,to_char(TO_DT,'DD-MM-YYYY')TO_DT,SD_SEG_AMT,0 SD_ADD ,ISD_SEG_AMT, 0 ADD_SEG_AMT,ISD_RATE,NUM_OF_DAYS from isd_calc_lines where from_dt  in (select FROM_DATE from ISD_INT_RATES where FY_YEAR=?) \r\n"
				 * +
				 * ") A , CONS B where CTUSCNO=ISCNO AND substr(ISCNO,1,3)=? AND CTSTATUS!=0 AND SD_SEG_AMT >0 AND ISD_SEG_AMT>0 order by ISCNO,FROM_DT\r\n"
				 * ;
				 */
				log.info(sql);
				return jdbcTemplate.queryForList(sql,
						new Object[] { fin_year.split("-")[0], fin_year.split("-")[1], circle });
			} catch (DataAccessException e) {
				log.error(e);
				return Collections.emptyList();
			}

		}
	}

	public List<Map<String, Object>> getConsumptionDetails(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		String monthYear = request.getParameter("month") + "-" + request.getParameter("year");
		String servicetype = request.getParameter("servicetype").equals("ALL") ? ""
				: "  AND A.STCODE = " + request.getParameter("servicetype");

		if (!circle.equals("ALL")) {
			try {
				String sql = "SELECT A.CIRCLE CIRCLE,A.CTUSCNO CTUSCNO,A.CTCMD_HT CTCMD_HT,B.BTRKVAH_HT PRE_RECKVAH,A.BTRKVAH_HT CUR_RECKVAH, B.REC_TOD PRE_REC_TOD,A.REC_TOD CUR_REC_TOD, B.BTBLKVA_HT PRE_BMD,A.BTBLKVA_HT CUR_BMD,B.BTBKVAH PRE_BTBKVAH,A.BTBKVAH CUR_BTBKVAH ,A.BTTP_KVAH BTTP_KVAH,B.BTBKVAH-A.BTBKVAH  DIFF ,ROUND(((B.BTBKVAH-A.BTBKVAH)/(B.BTBKVAH)*100),2) PRE_DIFF,A.STCODE,A.STDESC,A.CTNAME FROM(\r\n"
						+ "SELECT SUBSTR(CTUSCNO,0,3)CIRCLE,CTUSCNO,CTCMD_HT,NVL(BTRKVAH_HT,0)BTRKVAH_HT ,((NVL(BTTOD2,0)+NVL(BTTOD5,0))-NVL(BTTOD1,0)+NVL(BTTOD6,0)) REC_TOD, NVL(BTBLKVA_HT,0)BTBLKVA_HT,NVL(BTBKVAH,0)BTBKVAH,NVL(BTTP_KVAH,0)BTTP_KVAH,STCODE,STDESC,CTNAME\r\n"
						+ "FROM CONS,BILL,SERVTYPE \r\n"
						+ "WHERE BTSCNO=CTUSCNO AND SUBSTR(BTSCNO,0,3)=? AND TO_CHAR(BTBLDT,'MON-YYYY')=? AND CTSERVTYPE=STCODE\r\n"
						+ "UNION ALL\r\n"
						+ "SELECT SUBSTR(CTUSCNO,0,3)CIRCLE,CTUSCNO,CTCMD_HT,NVL(BTRKVAH_HT,0)BTRKVAH_HT ,((NVL(BTTOD2,0)+NVL(BTTOD5,0))-NVL(BTTOD1,0)+NVL(BTTOD6,0)) REC_TOD, NVL(BTBLKVA_HT,0)BTBLKVA_HT,NVL(BTBKVAH,0)BTBKVAH,NVL(BTTP_KVAH,0)BTTP_KVAH,STCODE,STDESC,CTNAME\r\n"
						+ "FROM CONS,BILL_HIST,SERVTYPE \r\n"
						+ "WHERE BTSCNO=CTUSCNO AND SUBSTR(BTSCNO,0,3)=? AND TO_CHAR(BTBLDT,'MON-YYYY')=? AND CTSERVTYPE=STCODE ) A\r\n"
						+ "JOIN\r\n"
						+ "(SELECT SUBSTR(CTUSCNO,0,3)CIRCLE,CTUSCNO,CTCMD_HT,NVL(BTRKVAH_HT,0)BTRKVAH_HT ,((NVL(BTTOD2,0)+NVL(BTTOD5,0))-NVL(BTTOD1,0)+NVL(BTTOD6,0)) REC_TOD, NVL(BTBLKVA_HT,0)BTBLKVA_HT,NVL(BTBKVAH,0)BTBKVAH,NVL(BTTP_KVAH,0)BTTP_KVAH,STCODE,STDESC,CTNAME\r\n"
						+ "FROM CONS,BILL,SERVTYPE \r\n"
						+ "WHERE BTSCNO=CTUSCNO AND SUBSTR(BTSCNO,0,3)=? AND TO_CHAR(BTBLDT,'MON-YYYY')=TO_CHAR(ADD_MONTHS(TO_DATE(?,'MON-YYYY'),-1),'MON-YYYY') AND CTSERVTYPE=STCODE \r\n"
						+ "UNION ALL\r\n"
						+ "SELECT SUBSTR(CTUSCNO,0,3)CIRCLE,CTUSCNO,CTCMD_HT,NVL(BTRKVAH_HT,0)BTRKVAH_HT ,((NVL(BTTOD2,0)+NVL(BTTOD5,0))-NVL(BTTOD1,0)+NVL(BTTOD6,0)) REC_TOD, NVL(BTBLKVA_HT,0)BTBLKVA_HT,NVL(BTBKVAH,0)BTBKVAH,NVL(BTTP_KVAH,0)BTTP_KVAH,STCODE,STDESC,CTNAME\r\n"
						+ "FROM CONS,BILL_HIST,SERVTYPE \r\n"
						+ "WHERE BTSCNO=CTUSCNO AND SUBSTR(BTSCNO,0,3)=? AND TO_CHAR(BTBLDT,'MON-YYYY')=TO_CHAR(ADD_MONTHS(TO_DATE(?,'MON-YYYY'),-1),'MON-YYYY') AND CTSERVTYPE=STCODE) B\r\n"
						+ "ON  A.CTUSCNO=B.CTUSCNO\r\n" + "AND B.BTBKVAH-A.BTBKVAH >0\r\n"
						+ "AND ((B.BTBKVAH-A.BTBKVAH)/(B.BTBKVAH)*100)>10 " + servicetype + "  ORDER BY A.CTUSCNO";
				log.info(sql);
				return jdbcTemplate.queryForList(sql,
						new Object[] { circle, monthYear, circle, monthYear, circle, monthYear, circle, monthYear });
			} catch (DataAccessException e) {
				e.printStackTrace();
				return Collections.emptyList();
			}

		} else {
			try {
				String sql = "SELECT A.CIRCLE CIRCLE,A.CTUSCNO CTUSCNO,A.CTCMD_HT CTCMD_HT,B.BTRKVAH_HT PRE_RECKVAH,A.BTRKVAH_HT CUR_RECKVAH, B.REC_TOD PRE_REC_TOD,A.REC_TOD CUR_REC_TOD, B.BTBLKVA_HT PRE_BMD,A.BTBLKVA_HT CUR_BMD,B.BTBKVAH PRE_BTBKVAH,A.BTBKVAH CUR_BTBKVAH ,A.BTTP_KVAH BTTP_KVAH,B.BTBKVAH-A.BTBKVAH  DIFF ,ROUND(((B.BTBKVAH-A.BTBKVAH)/(B.BTBKVAH)*100),2) PRE_DIFF,A.STCODE,A.STDESC,A.CTNAME  FROM(\r\n"
						+ "SELECT SUBSTR(CTUSCNO,0,3)CIRCLE,CTUSCNO,CTCMD_HT,NVL(BTRKVAH_HT,0)BTRKVAH_HT ,((NVL(BTTOD2,0)+NVL(BTTOD5,0))-NVL(BTTOD1,0)+NVL(BTTOD6,0)) REC_TOD, NVL(BTBLKVA_HT,0)BTBLKVA_HT,NVL(BTBKVAH,0)BTBKVAH,NVL(BTTP_KVAH,0)BTTP_KVAH,STCODE,STDESC,CTNAME\r\n"
						+ "FROM CONS,BILL,SERVTYPE \r\n"
						+ "WHERE BTSCNO=CTUSCNO  AND TO_CHAR(BTBLDT,'MON-YYYY')=?  AND CTSERVTYPE=STCODE \r\n"
						+ "UNION ALL\r\n"
						+ "SELECT SUBSTR(CTUSCNO,0,3)CIRCLE,CTUSCNO,CTCMD_HT,NVL(BTRKVAH_HT,0)BTRKVAH_HT ,((NVL(BTTOD2,0)+NVL(BTTOD5,0))-NVL(BTTOD1,0)+NVL(BTTOD6,0)) REC_TOD, NVL(BTBLKVA_HT,0)BTBLKVA_HT,NVL(BTBKVAH,0)BTBKVAH,NVL(BTTP_KVAH,0)BTTP_KVAH,STCODE,STDESC,CTNAME\r\n"
						+ "FROM CONS,BILL_HIST,SERVTYPE \r\n"
						+ "WHERE BTSCNO=CTUSCNO  AND TO_CHAR(BTBLDT,'MON-YYYY')=? AND CTSERVTYPE=STCODE) A\r\n"
						+ "JOIN\r\n"
						+ "(SELECT SUBSTR(CTUSCNO,0,3)CIRCLE,CTUSCNO,CTCMD_HT,NVL(BTRKVAH_HT,0)BTRKVAH_HT ,((NVL(BTTOD2,0)+NVL(BTTOD5,0))-NVL(BTTOD1,0)+NVL(BTTOD6,0)) REC_TOD, NVL(BTBLKVA_HT,0)BTBLKVA_HT,NVL(BTBKVAH,0)BTBKVAH,NVL(BTTP_KVAH,0)BTTP_KVAH,STCODE,STDESC,CTNAME\r\n"
						+ "FROM CONS,BILL,SERVTYPE \r\n"
						+ "WHERE BTSCNO=CTUSCNO  AND TO_CHAR(BTBLDT,'MON-YYYY')=TO_CHAR(ADD_MONTHS(TO_DATE(?,'MON-YYYY'),-1),'MON-YYYY') AND CTSERVTYPE=STCODE \r\n"
						+ "UNION ALL\r\n"
						+ "SELECT SUBSTR(CTUSCNO,0,3)CIRCLE,CTUSCNO,CTCMD_HT,NVL(BTRKVAH_HT,0)BTRKVAH_HT ,((NVL(BTTOD2,0)+NVL(BTTOD5,0))-NVL(BTTOD1,0)+NVL(BTTOD6,0)) REC_TOD, NVL(BTBLKVA_HT,0)BTBLKVA_HT,NVL(BTBKVAH,0)BTBKVAH,NVL(BTTP_KVAH,0)BTTP_KVAH,STCODE,STDESC,CTNAME\r\n"
						+ "FROM CONS,BILL_HIST,SERVTYPE \r\n"
						+ "WHERE BTSCNO=CTUSCNO  AND TO_CHAR(BTBLDT,'MON-YYYY')=TO_CHAR(ADD_MONTHS(TO_DATE(?,'MON-YYYY'),-1),'MON-YYYY') AND CTSERVTYPE=STCODE) B\r\n"
						+ "ON  A.CTUSCNO=B.CTUSCNO\r\n" + "AND B.BTBKVAH-A.BTBKVAH >0\r\n"
						+ "AND ((B.BTBKVAH-A.BTBKVAH)/(B.BTBKVAH)*100)>10 " + servicetype + "  ORDER BY A.CTUSCNO";
				log.info(sql);
				return jdbcTemplate.queryForList(sql, new Object[] { monthYear, monthYear, monthYear, monthYear });
			} catch (DataAccessException e) {
				e.printStackTrace();
				e.printStackTrace();
				return Collections.emptyList();
			}

		}

	}

	public String getPreviousDate(String monthYear) {
		return jdbcTemplate.queryForObject(
				"SELECT TO_CHAR(ADD_MONTHS(TO_DATE(?,'MON-YYYY'),-1),'MON-YYYY') AS PRE FROM DUAL",
				new Object[] { monthYear }, String.class);
	}

	public List<Map<String, Object>> getAccountCopyDetails(HttpServletRequest request) {
		String serviceNo = request.getParameter("scno");
		String monthYear = request.getParameter("month") + "-" + request.getParameter("year");
		try {
			String sql = "Select uscno SCNO,nam NAME_OF_CONSUMER,mon_year LD_DATE,nvl(tot_ob,0) OB_WITHOUT_COURT,nvl(ob_oth,0)+nvl(Ob_Cclpc,0) OB_COURT,\r\n"
					+ "nvl(tot_ob,0)+nvl(ob_oth,0)+nvl(Ob_Cclpc,0) tot_ob,\r\n"
					+ "nvl(cmd,0) DEMAND_WITHOUT_COURT_LPC,nvl(CCLPC,0) court_lpc,tot_pay COLLECTION\r\n"
					+ ",nvl(rj_oth,0) COURT_RJ_CC,nvl(Rj_Cclpc,0) court_RJ_LPC,nvl(drj,0) drj,nvl(crj,0) CRJ\r\n"
					+ ",nvl(cbtot,0) cb_without_court,nvl(cb_oth,0)+nvl(CB_CCLPC,0) cb_court,(nvl(cbtot,0)+nvl(cb_oth,0)+nvl(CB_CCLPC,0)) tot_cb,CB_SD sd,to_date('01-'||mon_year,'DD-MON-YYYY') dateorder,CAT||'-'||SCAT CAT,DECODE(TRIM(STATUS),'1','LIVE','NEW','LIVE','0','BILLSTOP','STP','BILLSTOP','') STATUS \r\n"
					+ "from ledger_ht_HIST A,CONS B  \r\n"
					+ "where  a.acct_id=b.ctacct_id AND ctuscno=? and to_date('01-'||mon_year,'DD-MON-YYYY')>='01'|| ? \r\n"
					+ "union all\r\n"
					+ "Select uscno SCNO,nam NAME_OF_CONSUMER,mon_year LD_DATE,nvl(tot_ob,0) OB_WITHOUT_COURT,nvl(ob_oth,0)+nvl(Ob_Cclpc,0) OB_COURT,\r\n"
					+ "nvl(tot_ob,0)+nvl(ob_oth,0)+nvl(Ob_Cclpc,0) tot_ob,\r\n"
					+ "nvl(cmd,0) DEMAND_WITHOUT_COURT_LPC,nvl(CCLPC,0) court_lpc,tot_pay COLLECTION\r\n"
					+ ",nvl(rj_oth,0) COURT_RJ_CC,nvl(Rj_Cclpc,0) court_RJ_LPC,nvl(drj,0) drj,nvl(crj,0) CRJ\r\n"
					+ ",nvl(cbtot,0) cb_without_court,nvl(cb_oth,0)+nvl(CB_CCLPC,0) cb_court,(nvl(cbtot,0)+nvl(cb_oth,0)+nvl(CB_CCLPC,0)) tot_cb,CB_SD sd,to_date('01-'||mon_year,'DD-MON-YYYY') dateorder,CAT||'-'||SCAT CAT,DECODE(TRIM(STATUS),'1','LIVE','NEW','LIVE','0','BILLSTOP','STP','BILLSTOP','') STATUS \r\n"
					+ "from ACCOUNTCOPY A,CONS B \r\n"
					+ "where a.acct_id=b.ctacct_id AND ctuscno=? and to_date('01-'||mon_year,'DD-MON-YYYY')>='01'|| ? \r\n"
					+ "order by dateorder";
			log.info(sql);
			return jdbcTemplate.queryForList(sql, new Object[] { serviceNo, monthYear, serviceNo, monthYear });
		} catch (DataAccessException e) {
			log.error(e.getMessage());
			log.error(e);
			return Collections.emptyList();
		}
	}

	public List<Map<String, Object>> getPayments(HttpServletRequest request) {
		String serviceNo = request.getParameter("scno");
		String fmonthYear = "01-" + request.getParameter("fmonth") + "-" + request.getParameter("fyear");
		String tmonthYear = "01-" + request.getParameter("tmonth") + "-" + request.getParameter("tyear");
		try {
			String sql = "SELECT USCNO,TO_CHAR(PAY_DATE,'DD-MM-YYYY') D_PAY_DATE,PAY_DATE,NVL(PCMD,0) PAY_CC,NVL(PACD,0) PAY_ACD FROM v_ht_pay ,CONS WHERE CTUSCNO=USCNO AND USCNO=? AND TRIM(PAY_STA_FLG) NOT IN ('E','X') \r\n"
					+ "AND TO_CHAR(PAY_DATE,'MON-YYYY') IN ( select distinct(to_char(last_day(to_date(td.end_date + 1 - rownum)),'MON-YYYY'))from all_objects,(\r\n"
					+ " select to_date(?,'DD-MM-YYYY') start_date,to_date(?,'DD-MM-YYYY') end_date FROM   DUAL  ) td\r\n"
					+ " where trunc(td.end_date + 1 - rownum,'MM') >= trunc(td.start_date,'MM'))\r\n"
					+ "ORDER BY PAY_DATE DESC";
			log.info(sql);
			return jdbcTemplate.queryForList(sql, new Object[] { serviceNo, fmonthYear, tmonthYear });
		} catch (DataAccessException e) {
			log.error(e.getMessage());
			log.error(e);
			return Collections.emptyList();
		}
	}

	/*
	 * public List<Map<String, Object>> getJournalHist(HttpServletRequest request) {
	 * String serviceNo = request.getParameter("scno"); String fmonthYear =
	 * request.getParameter("fmonth") + "-" + request.getParameter("fyear"); String
	 * tmonthYear = request.getParameter("tmonth") + "-" +
	 * request.getParameter("tyear"); try { String sql =
	 * "select CTNAME,USCNO,BILL_MON||'-'||BILL_YEAR MON_YEAR, ENG_SRC_TYPE, KVAH_ALLOCATED_ENG, KVAH_ADJ_ENG, \r\n"
	 * +
	 * "TOD_ALLOCATED_ENG, TOD_ADJ_ENG, TOD_ALLOCATED_PEAK, TOD_ADJ_PEAK, TOD_ALLOCATED_OFF, TOD_ADJ_OFF from old_oa,cons where ctuscno=uscno and ctuscno=? order by to_date(BILL_MON||'-'||BILL_YEAR,'MON-YYYY') asc"
	 * ; log.info(sql); return jdbcTemplate.queryForList(sql,new Object[]
	 * {serviceNo}); } catch (DataAccessException e) { log.error(e.getMessage());
	 * log.error(e); return Collections.emptyList(); } }
	 */
	public List<Map<String, Object>> getJournalHist(HttpServletRequest request) {
		String serviceNo = request.getParameter("scno");
		String fmonthYear = request.getParameter("fmonth") + "-" + request.getParameter("fyear");
		String tmonthYear = request.getParameter("tmonth") + "-" + request.getParameter("tyear");
		try {
			String sql = "select SAPRJ RJ_TYPE,RJNO RJ_NO,to_char(rjdt,'DD-MON-YYYY') RJ_DATE,rjdt,\r\n"
					+ "CASE WHEN RJTYPE='CR' THEN (NVL(TOTRJ_AMT,0)) ELSE 0 END CR_AMT,\r\n"
					+ "CASE WHEN RJTYPE='DR'THEN (NVL(TOTRJ_AMT,0)) ELSE 0 END DR_AMT,COMMENTS from \r\n"
					+ "(select SAPRJ,USCNO,RJTYPE,TOTRJ_AMT,rjdt,STATUS,RJNO,COMMENTS   from JOURNAL union all select SAPRJ,USCNO,RJTYPE,TOTRJ_AMT,rjdt,STATUS,RJNO,COMMENTS   from JOURNAL_HIST)\r\n"
					+ "where USCNO=? and rjdt between to_date(?,'MON-YYYY') and LAST_DAY(to_date(?,'MON-YYYY')) order by rjdt desc";
			log.info(sql);
			return jdbcTemplate.queryForList(sql, new Object[] { serviceNo, fmonthYear, tmonthYear });
		} catch (DataAccessException e) {
			log.error(e.getMessage());
			log.error(e);
			return Collections.emptyList();
		}
	}

	public List<Map<String, Object>> getAccountCopyDetails1(HttpServletRequest request) {
		String serviceNo = request.getParameter("scno");
		String monthYear = request.getParameter("month") + "-" + request.getParameter("year");
		try {

			/*
			 * String sql = "SELECT * FROM \r\n" +
			 * "(SELECT USCNO SCNO,NAM NAME_OF_CONSUMER,CTADD1,CTADD2,CTADD3,CTADD4,LOAD,MON_YEAR LD_DATE,NVL(TOT_OB,0) OB_WITHOUT_COURT,NVL(OB_OTH,0)+NVL(OB_CCLPC,0) OB_COURT,\r\n"
			 * + "NVL(TOT_OB,0)+NVL(OB_OTH,0)+NVL(OB_CCLPC,0) TOT_OB,\r\n" +
			 * "NVL(CMD,0) DEMAND_WITHOUT_COURT_LPC,NVL(CCLPC,0) COURT_LPC,TOT_PAY COLLECTION,\r\n"
			 * +
			 * "NVL(RJ_OTH,0) COURT_RJ_CC,NVL(RJ_CCLPC,0) COURT_RJ_LPC,NVL(DRJ,0) DRJ,NVL(CRJ,0) CRJ, (NVL(RJ_OTH,0)+NVL(RJ_CCLPC,0)) COURT_RJ,\r\n"
			 * +
			 * "NVL(LEDGER_HT_HIST.CBTOT,0) CB_WITHOUT_COURT,NVL(LEDGER_HT_HIST.CB_OTH,0)+NVL(LEDGER_HT_HIST.CB_CCLPC,0) CB_COURT,(NVL(LEDGER_HT_HIST.CBTOT,0)+NVL(LEDGER_HT_HIST.CB_OTH,0)+NVL(LEDGER_HT_HIST.CB_CCLPC,0)) TOT_CB,CB_SD SD \r\n"
			 * + "FROM LEDGER_HT_HIST,CONS\r\n" + "WHERE CTUSCNO=USCNO\r\n" +
			 * "AND USCNO=? AND TO_DATE('01-'||MON_YEAR,'DD-MON-YYYY')>='01-'|| ?\r\n" +
			 * "ORDER BY TO_DATE('01-'||MON_YEAR,'DD-MON-YYYY')),\r\n" +
			 * "(SELECT MSCNO,MDOPNKVAH_HT,MDCLKVAH_HT,MDMF_HT ,MDCLRDG_DT,TO_CHAR(MDCLRDG_DT,'MON-YYYY')MYEAR FROM mtrdat_HIST\r\n"
			 * +
			 * "WHERE MSCNO=? AND TO_DATE('01'||TO_CHAR(MDCLRDG_DT,'MON-YYYY'),'DD-MON-YYYY')>='01-'|| ? \r\n"
			 * +
			 * "ORDER BY TO_DATE('01'||TO_CHAR(MDCLRDG_DT,'MON-YYYY'),'DD-MON-YYYY')),\r\n"
			 * +
			 * "(SELECT BTSCNO,BTTODCHG_HT,BTRKVA_HT,BTBLKVA_HT,BTRKWH_HT,BTRKVAH_HT,BTBKVAH,TO_CHAR(BTBLCLSDT,'MON-YYYY')BTYEAR,NVL(BTBLSOLAR_HT,0) BTBLSOLAR_HT  FROM BILL_HIST WHERE BTSCNO=? AND TO_DATE('01'||TO_CHAR(BTBLCLSDT,'MON-YYYY'),'DD-MON-YYYY')>='01-'||?)\r\n"
			 * + "WHERE SCNO=MSCNO\r\n" + "AND BTSCNO=SCNO\r\n" + "AND LD_DATE=MYEAR\r\n" +
			 * "AND LD_DATE=BTYEAR\r\n UNION ALL SELECT * FROM (SELECT USCNO SCNO,NAM NAME_OF_CONSUMER,CTADD1,CTADD2,CTADD3,CTADD4,LOAD,MON_YEAR LD_DATE,NVL(TOT_OB,0) OB_WITHOUT_COURT,NVL(OB_OTH,0)+NVL(OB_CCLPC,0) OB_COURT, NVL(TOT_OB,0)+NVL(OB_OTH,0)+NVL(OB_CCLPC,0) TOT_OB, NVL(CMD,0) DEMAND_WITHOUT_COURT_LPC,NVL(CCLPC,0) COURT_LPC,TOT_PAY COLLECTION, NVL(RJ_OTH,0) COURT_RJ_CC,NVL(RJ_CCLPC,0) COURT_RJ_LPC,NVL(DRJ,0) DRJ,NVL(CRJ,0) CRJ ,(NVL(RJ_OTH,0)+NVL(RJ_CCLPC,0)) COURT_RJ, NVL(ACCOUNTCO	PY.CBTOT,0) CB_WITHOUT_COURT,NVL(ACCOUNTCOPY.CB_OTH,0)+NVL(ACCOUNTCOPY.CB_CCLPC,0) CB_COURT,(NVL(ACCOUNTCOPY.CBTOT,0)+NVL(ACCOUNTCOPY.CB_OTH,0)+NVL(ACCOUNTCOPY.CB_CCLPC,0)) TOT_CB,CB_SD SD FROM ACCOUNTCOPY,CONS WHERE CTUSCNO=USCNO AND USCNO=? AND TO_DATE('01-'||MON_YEAR,'DD-MON-YYYY')>='01-'|| ? ORDER BY TO_DATE('01-'||MON_YEAR,'DD-MON-YYYY')), (SELECT MSCNO,MDOPNKVAH_HT,MDCLKVAH_HT,MDMF_HT ,MDCLRDG_DT,TO_CHAR(MDCLRDG_DT,'MON-YYYY')MYEAR FROM mtrdat WHERE MSCNO=? AND TO_DATE('01'||TO_CHAR(MDCLRDG_DT,'MON-YYYY'),'DD-MON-YYYY')>='01-'|| ? ORDER BY TO_DATE('01'||TO_CHAR(MDCLRDG_DT,'MON-YYYY'),'DD-MON-YYYY')), (SELECT BTSCNO,BTTODCHG_HT,BTRKVA_HT,BTBLKVA_HT,BTRKWH_HT,BTRKVAH_HT,BTBKVAH,TO_CHAR(BTBLCLSDT,'MON-YYYY')BTYEAR,NVL(BTBLSOLAR_HT,0) BTBLSOLAR_HT  FROM BILL WHERE BTSCNO=? AND TO_DATE('01'||TO_CHAR(BTBLCLSDT,'MON-YYYY'),'DD-MON-YYYY')>='01-'|| ?)WHERE SCNO=MSCNO AND BTSCNO=SCNO AND LD_DATE=MYEAR AND LD_DATE=BTYEAR "
			 * + "";
			 */
			String sql = "SELECT * FROM (\r\n" + "SELECT * FROM \r\n"
					+ "(SELECT USCNO SCNO,NAM NAME_OF_CONSUMER,CTADD1,CTADD2,CTADD3,CTADD4,LOAD,MON_YEAR LD_DATE,ROUND(NVL(TOT_OB,0)) \r\n"
					+ "OB_WITHOUT_COURT,ROUND(NVL(OB_OTH,0))+ROUND(NVL(OB_CCLPC,0)) OB_COURT,\r\n"
					+ "ROUND(NVL(TOT_OB,0))+ROUND(NVL(OB_OTH,0))+ROUND(NVL(OB_CCLPC,0)) TOT_OB,\r\n"
					+ "ROUND(NVL(CMD,0)) DEMAND_WITHOUT_COURT_LPC,ROUND(NVL(CCLPC,0)) COURT_LPC,ROUND(TOT_PAY) COLLECTION,\r\n"
					+ "ROUND(NVL(RJ_OTH,0)) COURT_RJ_CC,ROUND(NVL(RJ_CCLPC,0)) COURT_RJ_LPC,ROUND(NVL(DRJ,0)) DRJ,ROUND(NVL(CRJ,0)) CRJ, ROUND((NVL(RJ_OTH,0))+ROUND(NVL(RJ_CCLPC,0))) COURT_RJ,\r\n"
					+ "ROUND(NVL(LEDGER_HT_HIST.CBTOT,0)) CB_WITHOUT_COURT,ROUND(NVL(LEDGER_HT_HIST.CB_OTH,0))+ROUND(NVL(LEDGER_HT_HIST.CB_CCLPC,0)) CB_COURT,\r\n"
					+ "(NVL(LEDGER_HT_HIST.CBTOT,0)+NVL(LEDGER_HT_HIST.CB_OTH,0)+NVL(LEDGER_HT_HIST.CB_CCLPC,0)) TOT_CB,CB_SD SD,\r\n"
					+ "DECODE(TRIM(STATUS),'1','LIVE','NEW','LIVE','0','BILLSTOP','STP','BILLSTOP','')STATUS, CAT||'-'||SCAT CAT \r\n"
					+ "FROM LEDGER_HT_HIST,CONS\r\n" + "WHERE CTUSCNO=USCNO\r\n"
					+ "AND USCNO=? AND TO_DATE('01-'||MON_YEAR,'DD-MON-YYYY')>='01-'||? \r\n"
					+ "ORDER BY TO_DATE('01-'||MON_YEAR,'DD-MON-YYYY')),\r\n"
					+ "(SELECT MSCNO,MDOPNKVAH_HT,MDCLKVAH_HT,MDMF_HT ,MDCLRDG_DT,TO_CHAR(MDCLRDG_DT,'MON-YYYY')MYEAR FROM mtrdat_HIST\r\n"
					+ "WHERE MSCNO=? AND TO_DATE('01'||TO_CHAR(MDCLRDG_DT,'MON-YYYY'),'DD-MON-YYYY')>='01-'|| ? \r\n"
					+ "ORDER BY TO_DATE('01'||TO_CHAR(MDCLRDG_DT,'MON-YYYY'),'DD-MON-YYYY')),\r\n"
					+ "(SELECT BTSCNO,BTTODCHG_HT,BTRKVA_HT,BTBLKVA_HT,BTRKWH_HT,BTRKVAH_HT,BTBKVAH,TO_CHAR(BTBLCLSDT,'MON-YYYY')BTYEAR,\r\n"
					+ "NVL(BTBLSOLAR_HT,0) BTBLSOLAR_HT  FROM BILL_HIST WHERE BTSCNO=? AND \r\n"
					+ "TO_DATE('01'||TO_CHAR(BTBLCLSDT,'MON-YYYY'),'DD-MON-YYYY')>='01-'||? )\r\n"
					+ "WHERE SCNO=MSCNO(+)\r\n" + "AND LD_DATE=MYEAR(+)\r\n" + "AND SCNO=BTSCNO(+)\r\n"
					+ "AND LD_DATE=BTYEAR(+)\r\n" + "UNION ALL\r\n" + "SELECT * FROM \r\n"
					+ "(SELECT USCNO SCNO,NAM NAME_OF_CONSUMER,CTADD1,CTADD2,CTADD3,CTADD4,LOAD,MON_YEAR LD_DATE,ROUND(NVL(TOT_OB,0)) OB_WITHOUT_COURT,\r\n"
					+ "ROUND(NVL(OB_OTH,0))+ROUND(NVL(OB_CCLPC,0)) OB_COURT,\r\n"
					+ "ROUND(NVL(TOT_OB,0))+ROUND(NVL(OB_OTH,0))+ROUND(NVL(OB_CCLPC,0)) TOT_OB,\r\n"
					+ "ROUND(NVL(CMD,0)) DEMAND_WITHOUT_COURT_LPC,ROUND(NVL(CCLPC,0)) COURT_LPC,ROUND(TOT_PAY) COLLECTION,\r\n"
					+ "ROUND(NVL(RJ_OTH,0)) COURT_RJ_CC,ROUND(NVL(RJ_CCLPC,0)) COURT_RJ_LPC,ROUND(NVL(DRJ,0)) DRJ,ROUND(NVL(CRJ,0)) CRJ, (ROUND(NVL(RJ_OTH,0))+\r\n"
					+ "ROUND(NVL(RJ_CCLPC,0))) COURT_RJ,\r\n"
					+ "ROUND(NVL(ACCOUNTCOPY.CBTOT,0)) CB_WITHOUT_COURT,ROUND(NVL(ACCOUNTCOPY.CB_OTH,0))+ROUND(NVL(ACCOUNTCOPY.CB_CCLPC,0)) CB_COURT,\r\n"
					+ "(ROUND(NVL(ACCOUNTCOPY.CBTOT,0))+ROUND(NVL(ACCOUNTCOPY.CB_OTH,0))+ROUND(NVL(ACCOUNTCOPY.CB_CCLPC,0))) TOT_CB,CB_SD SD \r\n"
					+ ",DECODE(TRIM(STATUS),'1','LIVE','NEW','LIVE','0','BILLSTOP','STP','BILLSTOP','')STATUS,CAT||'-'||SCAT CAT\r\n"
					+ "FROM ACCOUNTCOPY,CONS\r\n" + "WHERE CTUSCNO=USCNO\r\n"
					+ "AND USCNO=? AND TO_DATE('01-'||MON_YEAR,'DD-MON-YYYY')>='01-'||  ? \r\n"
					+ "ORDER BY TO_DATE('01-'||MON_YEAR,'DD-MON-YYYY')),\r\n"
					+ "(SELECT MSCNO,MDOPNKVAH_HT,MDCLKVAH_HT,MDMF_HT ,MDCLRDG_DT,TO_CHAR(MDCLRDG_DT,'MON-YYYY')MYEAR FROM mtrdat_HIST\r\n"
					+ "WHERE MSCNO=? AND TO_DATE('01'||TO_CHAR(MDCLRDG_DT,'MON-YYYY'),'DD-MON-YYYY')>='01-'|| ? \r\n"
					+ "ORDER BY TO_DATE('01'||TO_CHAR(MDCLRDG_DT,'MON-YYYY'),'DD-MON-YYYY')),\r\n"
					+ "(SELECT BTSCNO,BTTODCHG_HT,BTRKVA_HT,BTBLKVA_HT,BTRKWH_HT,BTRKVAH_HT,BTBKVAH,TO_CHAR(BTBLCLSDT,'MON-YYYY')BTYEAR,\r\n"
					+ "NVL(BTBLSOLAR_HT,0) BTBLSOLAR_HT  FROM BILL_HIST WHERE BTSCNO=? AND \r\n"
					+ "TO_DATE('01'||TO_CHAR(BTBLCLSDT,'MON-YYYY'),'DD-MON-YYYY')>='01-'||?)\r\n"
					+ "WHERE SCNO=MSCNO(+)\r\n" + "AND LD_DATE=MYEAR(+)\r\n" + "AND SCNO=BTSCNO(+)\r\n"
					+ "AND LD_DATE=BTYEAR(+)\r\n" + ") ORDER BY TO_DATE(LD_DATE,'MON-YYYY')";

			log.info(sql);
			return jdbcTemplate.queryForList(sql, new Object[] { serviceNo, monthYear, serviceNo, monthYear, serviceNo,
					monthYear, serviceNo, monthYear, serviceNo, monthYear, serviceNo, monthYear });
		} catch (DataAccessException e) {
			log.error(e.getMessage());
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	public List<Map<String, Object>> getServiceWiseCBSplit(HttpServletRequest request) {
		String serviceNo = request.getParameter("scno");
		String monthYear = request.getParameter("month") + "-" + request.getParameter("year");
		try {

			String sql = "select USCNO SCNO,NAM NAME_OF_CONSUMER,CTADD1,CTADD2,CTADD3,CTADD4,LOAD,MON_YEAR LD_DATE,\r\n"
					+ "CB_EC CBEC ,CB_LPC CBLPC , CB_ED CBED , CB_IED CBIED , CB_FSA CBFSA,NVL(CB_TUPC,0)CBTUPC ,NVL(CB_OLD_FPP,0) CBOLDFPP,NVL(CB_NEW_FPP,0)CBNEWFPP , CBTOT \r\n"
					+ ",DECODE(TRIM(STATUS),'1','LIVE','NEW','LIVE','0','BILLSTOP','STP','BILLSTOP','')STATUS, CAT||'-'||SCAT CAT \r\n"
					+ "from LEDGER_HT_HIST,CONS\r\n" + "WHERE CTUSCNO=USCNO\r\n"
					+ "AND USCNO=? AND TO_DATE('01-'||MON_YEAR,'DD-MON-YYYY')>='01-'||? \r\n"
					+ "ORDER BY TO_DATE('01-'||MON_YEAR,'DD-MON-YYYY')";

			log.info(sql);
			return jdbcTemplate.queryForList(sql, new Object[] { serviceNo, monthYear });
		} catch (DataAccessException e) {
			log.error(e.getMessage());
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	public List<Map<String, Object>> getServiceChangeHistory(HttpServletRequest request) {
		String serviceNo = request.getParameter("scno");

		try {
			String sql = "select uscno,TYPE,OLD_VAL,NEW_VAL,DEPT_ORDER_NO,to_char(DEPT_ORDER_DT,'DD-MM-YYYY') DEPT_ORDER_DT ,to_char(EFF_DT,'DD-MM-YYYY') EFF_DT ,to_char(CRE_DTTM,'DD-MM-YYYY') CRE_DTTM,CHANGED_BY from change_history,MASTER_CHANGE_CODES where CHG_TYPE_CD=CODE\r\n"
					+ "and substr(uscno,1,3) in ('VJA','GNT','ONG','CRD') and uscno=? order by CRE_DTTM desc ";

			log.info(sql);
			return jdbcTemplate.queryForList(sql, new Object[] { serviceNo });
		} catch (DataAccessException e) {
			log.error(e.getMessage());
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	public List<Map<String, Object>> getServiceChangeHistoryMF(HttpServletRequest request) {

		String serviceNo = request.getParameter("scno");

		try {
			String sql = " SELECT A.*,case when TYPE_CHANGE ='C' then 'Cubical' when TYPE_CHANGE ='M' then 'Meter' else 'Cubical & Meter' end TYPE_CHANGE_C ,to_char(CHANGED_ON,'DD-MON-YYYY') CHANGED_ON_C,NVL(CREATED_BY,'--') CREATED_BY_C FROM MNP_METER_CHANGE A "
					+ " WHERE  USCNO=? ";

			log.info(sql);
			return jdbcTemplate.queryForList(sql, new Object[] { serviceNo });
		} catch (DataAccessException e) {
			log.error(e.getMessage());
			e.printStackTrace();
			return Collections.emptyList();
		}

	}

	public List<Map<String, Object>> getServiceHistory(HttpServletRequest request) {
		String serviceNo = request.getParameter("scno");
		String fmonthYear = "01-" + request.getParameter("fmonth") + "-" + request.getParameter("fyear");
		String tmonthYear = "01-" + request.getParameter("tmonth") + "-" + request.getParameter("tyear");
		// System.out.println(fmonthYear+":"+tmonthYear+":"+serviceNo);
		try {

			String sql = "SELECT  UNIQUE TRUNC(TO_DATE(MON_YEAR,'MON-YYYY')) MON_YR,MON_YEAR,ROUND(LOAD) LOAD,\r\n"
					+ "CAT,SCAT,ROUND(MDCLKWH_HT) MDCLKWH_HT ,ROUND(MDCLKVAH_HT) MDCLKVAH_HT , ROUND(MDMF_HT) MDMF_HT , ROUND(MDRKWH_HT) MDRKWH_HT,ROUND(MDRKVAH_HT) MDRKVAH_HT,ROUND(REC_KVAH) REC_KVAH, ROUND(MN_KVAH) MN_KVAH,ROUND(MDTOD1_RECKVAH) MDTOD1_RECKVAH,ROUND(MDTOD2_RECKVAH) MDTOD2_RECKVAH,ROUND(MDTOD5_RECKVAH) MDTOD5_RECKVAH ,ROUND(MDTOD6_RECKVAH) MDTOD6_RECKVAH,ROUND(MDTOD1_CLS) MDTOD1_CLS,ROUND(MDTOD2_CLS)MDTOD2_CLS,ROUND(MDTOD5_CLS) MDTOD5_CLS,ROUND(MDTOD6_CLS)MDTOD6_CLS,DECODE(STATUS,NULL,'0','NEW','LIVE',1,'LIVE','BS') STAT,ROUND(REC_KWH) REC_KWH ,ROUND(REC_MD) REC_MD,\r\n"
					+ "(ROUND(NVL(OB_OTH,0)) + ROUND(NVL(OB_CCLPC,0))+ROUND(NVL(TOT_OB,0))) TOT_OB,(ROUND(NVL(CMD,0))+ROUND(NVL(CCLPC,0))) DEMAND,\r\n"
					+ "(ROUND(NVL(DRJ,0))+ROUND(NVL(RJ_OTH,0))+ROUND(NVL(RJ_CCLPC,0))) DEBIT_RJ,(ROUND(NVL(TOT_PAY,0))) COLLECTION,ROUND(NVL(CRJ,0)) CREDIT_RJ,\r\n"
					+ "(ROUND(NVL(CBTOT,0))+ROUND(NVL(CB_CCLPC,0))+ROUND(NVL(CB_OTH,0))) TOTAL_CB,ROUND(NVL(CB_SD,0)) SD ,ROUND(NVL(CMD,0))-ROUND(NVL(LPC,0)) DWOCC, ROUND(NVL(LPC,0)) LPC ,ROUND(NVL(CCLPC,0))  DWCC \r\n"
					+ "FROM CONS,ledger_ht_hist L1, (select to_char(MDMONTH,'MON-YYYY') TEMP_MON,A.* from mtrdat_hist A) WHERE MON_YEAR IN ("
					+ "select distinct(to_char(last_day(to_date(td.end_date + 1 - rownum)),'MON-YYYY'))from all_objects,(\r\n"
					+ " select to_date(?,'DD-MM-YYYY') start_date,to_date(?,'DD-MM-YYYY') end_date FROM   DUAL  ) td\r\n"
					+ " where trunc(td.end_date + 1 - rownum,'MM') >= trunc(td.start_date,'MM')) AND \r\n"
					+ " SUBSTR(USCNO,1,3) IN('GNT','VJA','ONG','CRD') AND CTUSCNO=USCNO(+)  AND CTUSCNO = ? "
					+ " AND  USCNO = MSCNO(+) and MON_YEAR = TEMP_MON(+) " /* and MDCLKWHSTAT_HT=1 */
					+ " ORDER BY MON_YR DESC";

			log.info(sql);
			return jdbcTemplate.queryForList(sql, new Object[] { fmonthYear, tmonthYear, serviceNo });
		} catch (DataAccessException e) {
			log.error(e.getMessage());
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	public ConsumerDetails getConsumerDetails(String serviceNo) {
		String sql = "SELECT CIRNAME,DIVNAME,ERONAME,SUBNAME,SECNAME,NVL(STDESC,'')STDESC,to_char(CTENTDT,'DD-MM-YYYY') TCTENTDT,A.LAST_PAID_DATE, (-1*CONS.CTSDTOTAMT) CTSDTOTAMT_R,case nvl(CTPANNO,'N')  when 'N' then  (select PER_ID_NBR from id_types where ID_TYPE_CD='PAN' and USCNO=cons.CTUSCNO)  else CTPANNO end CTPANNO_TEMP, CONS.* FROM CONS,SPDCLMASTER,SERVTYPE, (select uscno,to_char(max(pay_date),'DD-MM-YYYY') LAST_PAID_DATE from PAY_HT_HIST where SUBSTR(uscno,1,3) IN('GNT','VJA','ONG','CRD') group by uscno) A WHERE CTUSCNO= ?  and SUBSTR(CTSECCD,-5)=SECCD and NVL(CTSERVTYPE,0)=STCODE(+) and CTUSCNO=A.uscno(+)";
		log.info(sql);
		try {
			ConsumerDetails cons = jdbcTemplate.queryForObject(sql, new Object[] { serviceNo },
					BeanPropertyRowMapper.newInstance(ConsumerDetails.class));
			if (cons != null) {
				String idsql = "SELECT USCNO,ID_TYPE_CD,PER_ID_NBR,VERSION FROM ID_TYPES WHERE USCNO=?";
				List<IdTypes> idlist = jdbcTemplate.query(idsql, new Object[] { serviceNo },
						new BeanPropertyRowMapper(IdTypes.class));
				cons.setIdTypes(idlist);
			}
			return cons;
		} catch (DataAccessException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	public List<Map<String, Object>> getLedgerHistory(HttpServletRequest request) {
		String uscno = request.getParameter("scno");
		try {
			String sql = "select ACCT_ID, MON_YEAR, NAM, USCNO, DISCOM, CIRCLE, DIVISION, ERO, SUBDIVISION, SECTION, LOAD, CAT, SCAT,\n"
					+ "NVL(REC_KVAH,0) REC_KVAH , 	NVL(MN_KVAH,0) MN_KVAH , 	NVL(REC_MD,0) REC_MD , 	NVL(PF,0) PF , 	NVL(OB_EC,0) OB_EC , 	NVL(OB_ED,0) OB_ED , 	NVL(OB_LPC,0) OB_LPC , 	NVL(OB_EDI,0) OB_EDI , 	NVL(OB_FSA,0) OB_FSA , 	NVL(TOT_OB,0) TOT_OB , 	NVL(EC,0) EC , 	NVL(LPC,0) LPC , 	NVL(ED,0) ED , 	NVL(EDI,0) EDI , 	NVL(FSA,0) FSA , 	NVL(CMD,0) CMD , 	NVL(DRJ_EC,0) DRJ_EC , 	NVL(DRJ_ED,0) DRJ_ED , 	NVL(DRJ_LPC,0) DRJ_LPC , 	NVL(DRJ_IED,0) DRJ_IED , 	NVL(DRJ_FSA,0) DRJ_FSA , 	NVL(DRJ,0) DRJ , 	NVL(CRJ_EC,0) CRJ_EC , 	NVL(CRJ_ED,0) CRJ_ED , 	NVL(CRJ_LPC,0) CRJ_LPC , 	NVL(CRJ_IED,0) CRJ_IED , 	NVL(CRJ_FSA,0) CRJ_FSA , 	NVL(CRJ,0) CRJ , 	NVL(PAID_EC,0) PAID_EC , 	NVL(PAID_ED,0) PAID_ED , 	NVL(PAID_LPC,0) PAID_LPC , 	NVL(PAID_EDI,0) PAID_EDI , 	NVL(PAID_FSA,0) PAID_FSA , 	NVL(TOT_PAY,0) TOT_PAY , 	NVL(CB_EC,0) CB_EC , 	NVL(CB_LPC,0) CB_LPC , 	NVL(CB_ED,0) CB_ED , 	NVL(CB_IED,0) CB_IED , 	NVL(CB_FSA,0) CB_FSA , 	NVL(CBTOT,0) CBTOT , 	NVL(REC_KWH,0) REC_KWH , 	NVL(CB_SD,0) CB_SD , 	NVL(STATUS,0) STATUS , 	NVL(BMD,0) BMD , 	NVL(OB_OTH,0) OB_OTH , 	NVL(RJ_OTH,0) RJ_OTH , 	NVL(CB_OTH,0) CB_OTH , 	NVL(OB_CCLPC,0) OB_CCLPC , 	NVL(CCLPC,0) CCLPC , 	NVL(RJ_CCLPC,0) RJ_CCLPC , 	NVL(CB_CCLPC,0) CB_CCLPC , 	NVL(OB_SD,0) OB_SD , 	NVL(OBNSC_SD,0) OBNSC_SD , 	NVL(DRJ_SD,0) DRJ_SD , 	NVL(CRJ_SD,0) CRJ_SD , 	NVL(PAID_SD,0) PAID_SD , 	NVL(OB_TUPC,0) OB_TUPC , 	NVL(TUPC,0) TUPC , 	NVL(DRJ_TUPC,0) DRJ_TUPC , 	NVL(CRJ_TUPC,0) CRJ_TUPC , 	NVL(PAID_TUPC,0) PAID_TUPC , 	NVL(CB_TUPC,0) CB_TUPC , 	NVL(OB_OLD_FPP,0) OB_OLD_FPP , 	NVL(OLD_FPP,0) OLD_FPP , 	NVL(DRJ_OLD_FPP,0) DRJ_OLD_FPP , 	NVL(CRJ_OLD_FPP,0) CRJ_OLD_FPP , 	NVL(PAID_OLD_FPP,0) PAID_OLD_FPP , 	NVL(CB_OLD_FPP,0) CB_OLD_FPP , 	NVL(OB_NEW_FPP,0) OB_NEW_FPP , 	NVL(NEW_FPP,0) NEW_FPP , 	NVL(DRJ_NEW_FPP,0) DRJ_NEW_FPP , 	NVL(CRJ_NEW_FPP,0) CRJ_NEW_FPP , 	NVL(PAID_NEW_FPP,0) PAID_NEW_FPP , 	NVL(CB_NEW_FPP,0) CB_NEW_FPP \n"
					+ " from LEDGER_HT_HIST where USCNO=? order by TO_DATE(MON_YEAR,'MON-YYYY')";
			log.info(sql);
			return jdbcTemplate.queryForList(sql, new Object[] { uscno });
		} catch (DataAccessException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	public List<Map<String, Object>> getThirdPartySalesDetails(HttpServletRequest request) {
		String monthYear = request.getParameter("month") + "-" + request.getParameter("year");
		try {
			String sql = "SELECT CTNAME,DEV_NAME,DEV_CODE,TPUSCNO,CTCAT,NVL(CTCMD_HT,0)CTCMD_HT,NVL(CTACTUAL_KV,0)CTACTUAL_KV, NVL(WHEEL_PER,0)WHEEL_PER,NVL(BTRKVAH_HT,0)BTRKVAH_HT,NVL(BTRKVA_HT,0)BTRKVA_HT,\r\n"
					+ "(BTTOD5+BTTOD2) REC_PEAK_TOD,(BTTOD1+BTTOD6) REC_OFFPEAK_TOD,NVL(BTPF_HT,0)BTPF_HT,NVL(TP_ADJ_ENG,0)ALLOC_ENG_GROSS,NVL(TP_ADJ_ENG,0)ADJ_ENG_GROSS,NVL(BTTP_KVAH,0) ADJ_ENG_NET,\r\n"
					+ "(BTTP_TOD_PEAK+BTTP_TOD_OFFPEAK)ADJ_TOD_NET,BTBKVAH,BTBLKVA_HT,ROUND((WHEEL_PER/100)*TP_ADJ_ENG)WHEELING_LOSS_KVAH,(TP_ADJ_ENG-TP_ADJ_ENG)UNUTLIZED_UNITS,TP_WHEEL_CHGS \r\n"
					+ "FROM TP_ENERGY,CONS,BILL WHERE TPUSCNO=CTUSCNO AND TPUSCNO=BTSCNO AND TO_CHAR(BTBLDT,'MON-YYYY')=? AND  CANCEL_FLAG='C' \r\n"
					+ "UNION ALL\r\n"
					+ "SELECT CTNAME,DEV_NAME,DEV_CODE,TPUSCNO,CTCAT,NVL(CTCMD_HT,0)CTCMD_HT,NVL(CTACTUAL_KV,0)CTACTUAL_KV, NVL(WHEEL_PER,0)WHEEL_PER,NVL(BTRKVAH_HT,0)BTRKVAH_HT,NVL(BTRKVA_HT,0)BTRKVA_HT,\r\n"
					+ "(BTTOD5+BTTOD2) REC_PEAK_TOD,(BTTOD1+BTTOD6) REC_OFFPEAK_TOD,NVL(BTPF_HT,0)BTPF_HT,NVL(TP_ADJ_ENG,0)ALLOC_ENG_GROSS,NVL(TP_ADJ_ENG,0)ADJ_ENG_GROSS,NVL(BTTP_KVAH,0) ADJ_ENG_NET,\r\n"
					+ "(BTTP_TOD_PEAK+BTTP_TOD_OFFPEAK)ADJ_TOD_NET,BTBKVAH,BTBLKVA_HT,ROUND((WHEEL_PER/100)*TP_ADJ_ENG)WHEELING_LOSS_KVAH,(TP_ADJ_ENG-TP_ADJ_ENG)UNUTLIZED_UNITS,TP_WHEEL_CHGS \r\n"
					+ "FROM TP_ENERGY,CONS,BILL_HIST WHERE TPUSCNO=CTUSCNO AND TPUSCNO=BTSCNO AND TO_CHAR(BTBLDT,'MON-YYYY')=?  AND  CANCEL_FLAG='C'  \r\n"
					+ "";
			log.info(sql);
			return jdbcTemplate.queryForList(sql, new Object[] { monthYear, monthYear });
		} catch (DataAccessException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	public List<Map<String, Object>> getCBAndDemandAbstractDetails(HttpServletRequest request) {
		String monthYear = request.getParameter("month") + "-" + request.getParameter("year");
		String circle = request.getParameter("circle");
		if (circle.equalsIgnoreCase("ALL")) {
			try {
				String sql = "select nvl(CIRNAME,'ZZZ') CIRNAME,nvl(TYPE,'TOTAL') TYPE,nvl(CTCAT,'TOTAL') CTCAT,sum(CAT_COUNT) CAT_COUNT,sum(NEG_CB)NEG_CB,sum(POS_CB)POS_CB,sum(TOTAL_CB)TOTAL_CB,sum(DEMAND_PART)DEMAND_PART,sum(ARREAR_PART)ARREAR_PART,nvl(CIRNAME,'ZZZ')||nvl(TYPE,'ZZZT') CIR_TYPE from "
						+ "( SELECT  UNIQUE SUBSTR(CTUSCNO,1,3) CIRNAME,DECODE(CTGOVT_PVT,'Y','GOVT','NON-GOVT') TYPE,CTCAT,count(CTCAT) CAT_COUNT,\r\n"
						+ "--NVL(OB,0) TOB,NVL(DEM,0)+NVL(DR_AMT,0) TDEM,\r\n"
						+ "--NVL(COLL,0)+NVL(CR_AMT,0) TCOLL, \r\n"
						+ "SUM(CASE WHEN NVL(CB,0)<0 THEN NVL(CB,0) ELSE 0 END ) NEG_CB,\r\n"
						+ "SUM(CASE WHEN NVL(CB,0)>0 THEN NVL(CB,0) ELSE 0 END ) POS_CB,\r\n"
						+ "SUM(NVL(CB,0)) TOTAL_CB,\r\n"
						+ "SUM(CASE WHEN NVL(CB,0)>(NVL(DEM,0)+NVL(DR_AMT,0)) THEN (NVL(DEM,0)+NVL(DR_AMT,0)) ELSE CASE WHEN NVL(CB,0)>0 THEN NVL(CB,0) ELSE 0 END  END) DEMAND_PART,\r\n"
						+ "SUM(CASE WHEN NVL(CB,0)>(NVL(DEM,0)+NVL(DR_AMT,0)) THEN NVL(CB,0)-(NVL(DEM,0)+NVL(DR_AMT,0))  ELSE 0 END) ARREAR_PART\r\n"
						+ "FROM CONS,\r\n"
						+ "(SELECT USCNO,SUM(NVL(OB_OTH,0) +NVL(OB_CCLPC,0)+NVL(TOT_OB,0)) OB,SUM(NVL(CMD,0)+NVL(CCLPC,0)) Dem,SUM(NVL(TOT_PAY,0)) COLL,SUM(NVL(CBTOT,0)+NVL(CB_CCLPC,0)+NVL(CB_OTH,0)) CB FROM LEDGER_HT_HIST WHERE MON_YEAR=? AND SUBSTR(USCNO,1,3) IN('GNT','VJA','ONG','CRD') GROUP BY USCNO) L,\r\n"
						+ "(select uscno,SUM(CASE WHEN RJTYPE='DR' THEN (nvl(ENGCHG,0)+nvl(THEFT,0)+nvl(OTHCHG,0)+nvl(CUSTCHG,0)+nvl(DEMCHG,0)+nvl(ED,0)+nvl(LPC,0)+nvl(EDI,0)+nvl(FSA,0)+NVL(CC_LPC,0)+NVL(CC_OTH,0)) END) DR_AMT,SUM(CASE WHEN RJTYPE='CR'THEN (nvl(ENGCHG,0)+nvl(THEFT,0)+nvl(OTHCHG,0)+nvl(CUSTCHG,0)+nvl(DEMCHG,0)+nvl(ED,0)+nvl(LPC,0)+nvl(EDI,0)+nvl(FSA,0)+NVL(CC_LPC,0)+NVL(CC_OTH,0)) END) CR_AMT  from JOURNAL_HIST WHERE to_char(TRUNC(rjdt,'MM'),'MON-YYYY')=? and SUBSTR(USCNO,1,3) IN('GNT','VJA','ONG','CRD') AND TRIM(STATUS) NOT IN ('X') GROUP BY USCNO) RJ\r\n"
						+ "WHERE CTUSCNO=L.USCNO(+) AND CTUSCNO=RJ.USCNO(+) GROUP BY SUBSTR(CTUSCNO,1,3),DECODE(CTGOVT_PVT,'Y','GOVT','NON-GOVT'),CTCAT ORDER BY CIRNAME,TYPE,CTCAT)\r\n"
						+ "GROUP BY CUBE (CIRNAME,TYPE,CTCAT)\r\n" + "ORDER BY CIRNAME,TYPE,CTCAT";
				log.info(sql);
				return jdbcTemplate.queryForList(sql, new Object[] { monthYear, monthYear });
			} catch (DataAccessException e) {
				e.printStackTrace();
				log.error(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}
		} else {
			try {
				String sql = "select * from (  select CIRNAME,nvl(TYPE,'TOTAL') TYPE,nvl(CTCAT,'TOTAL') CTCAT,sum(CAT_COUNT) CAT_COUNT,sum(NEG_CB)NEG_CB,sum(POS_CB)POS_CB,sum(TOTAL_CB)TOTAL_CB,sum(DEMAND_PART)DEMAND_PART,sum(ARREAR_PART)ARREAR_PART,nvl(CIRNAME,'ZZZ')||nvl(TYPE,'ZZZT') CIR_TYPE from "
						+ "( SELECT  UNIQUE SUBSTR(CTUSCNO,1,3) CIRNAME,DECODE(CTGOVT_PVT,'Y','GOVT','NON-GOVT') TYPE,CTCAT,count(CTCAT) CAT_COUNT,\r\n"
						+ "--NVL(OB,0) TOB,NVL(DEM,0)+NVL(DR_AMT,0) TDEM,\r\n"
						+ "--NVL(COLL,0)+NVL(CR_AMT,0) TCOLL, \r\n"
						+ "SUM(CASE WHEN NVL(CB,0)<0 THEN NVL(CB,0) ELSE 0 END ) NEG_CB,\r\n"
						+ "SUM(CASE WHEN NVL(CB,0)>0 THEN NVL(CB,0) ELSE 0 END ) POS_CB,\r\n"
						+ "SUM(NVL(CB,0)) TOTAL_CB,\r\n"
						+ "SUM(CASE WHEN NVL(CB,0)>(NVL(DEM,0)+NVL(DR_AMT,0)) THEN (NVL(DEM,0)+NVL(DR_AMT,0)) ELSE CASE WHEN NVL(CB,0)>0 THEN NVL(CB,0) ELSE 0 END  END) DEMAND_PART,\r\n"
						+ "SUM(CASE WHEN NVL(CB,0)>(NVL(DEM,0)+NVL(DR_AMT,0)) THEN NVL(CB,0)-(NVL(DEM,0)+NVL(DR_AMT,0))  ELSE 0 END) ARREAR_PART\r\n"
						+ "FROM CONS,\r\n"
						+ "(SELECT USCNO,SUM(NVL(OB_OTH,0) +NVL(OB_CCLPC,0)+NVL(TOT_OB,0)) OB,SUM(NVL(CMD,0)+NVL(CCLPC,0)) Dem,SUM(NVL(TOT_PAY,0)) COLL,SUM(NVL(CBTOT,0)+NVL(CB_CCLPC,0)+NVL(CB_OTH,0)) CB FROM LEDGER_HT_HIST WHERE MON_YEAR=? AND SUBSTR(USCNO,1,3) IN('GNT','VJA','ONG','CRD') GROUP BY USCNO) L,\r\n"
						+ "(select uscno,SUM(CASE WHEN RJTYPE='DR' THEN (nvl(ENGCHG,0)+nvl(THEFT,0)+nvl(OTHCHG,0)+nvl(CUSTCHG,0)+nvl(DEMCHG,0)+nvl(ED,0)+nvl(LPC,0)+nvl(EDI,0)+nvl(FSA,0)+NVL(CC_LPC,0)+NVL(CC_OTH,0)) END) DR_AMT,SUM(CASE WHEN RJTYPE='CR'THEN (nvl(ENGCHG,0)+nvl(THEFT,0)+nvl(OTHCHG,0)+nvl(CUSTCHG,0)+nvl(DEMCHG,0)+nvl(ED,0)+nvl(LPC,0)+nvl(EDI,0)+nvl(FSA,0)+NVL(CC_LPC,0)+NVL(CC_OTH,0)) END) CR_AMT  from JOURNAL_HIST WHERE to_char(TRUNC(rjdt,'MM'),'MON-YYYY')=? and SUBSTR(USCNO,1,3) IN('GNT','VJA','ONG','CRD') AND TRIM(STATUS) NOT IN ('X') GROUP BY USCNO) RJ\r\n"
						+ "WHERE CTUSCNO=L.USCNO(+) AND CTUSCNO=RJ.USCNO(+) GROUP BY SUBSTR(CTUSCNO,1,3),DECODE(CTGOVT_PVT,'Y','GOVT','NON-GOVT'),CTCAT ORDER BY CIRNAME,TYPE,CTCAT)\r\n"
						+ " where CIRNAME = ?" + "GROUP BY CUBE (CIRNAME,TYPE,CTCAT)\r\n"
						+ "ORDER BY CIRNAME,TYPE,CTCAT  ) where CIRNAME is not null";

				/*
				 * String sql =
				 * "select * from ( select  CIRNAME,TYPE,nvl(CTCAT,'TOTAL') CTCAT,sum(CTCAT) CAT_COUNT,sum(NEG_CB)NEG_CB,sum(POS_CB)POS_CB,sum(TOTAL_CB)TOTAL_CB,sum(DEMAND_PART)DEMAND_PART,sum(ARREAR_PART)ARREAR_PART,nvl(CIRNAME,'ZZZ')||nvl(TYPE,'ZZZT') CIR_TYPE from"
				 * +
				 * " ( SELECT  UNIQUE SUBSTR(CTUSCNO,1,3) CIRNAME,DECODE(CTGOVT_PVT,'Y','GOVT','NON-GOVT') TYPE,CTCAT,CAT_COUNT,\r\n"
				 * + "--NVL(OB,0) TOB,NVL(DEM,0)+NVL(DR_AMT,0) TDEM,\r\n" +
				 * "--NVL(COLL,0)+NVL(CR_AMT,0) TCOLL, \r\n" +
				 * "SUM(CASE WHEN NVL(CB,0)<0 THEN NVL(CB,0) ELSE 0 END ) NEG_CB,\r\n" +
				 * "SUM(CASE WHEN NVL(CB,0)>0 THEN NVL(CB,0) ELSE 0 END ) POS_CB,\r\n" +
				 * "SUM(NVL(CB,0)) TOTAL_CB,\r\n" +
				 * "SUM(CASE WHEN NVL(CB,0)>(NVL(DEM,0)+NVL(DR_AMT,0)) THEN (NVL(DEM,0)+NVL(DR_AMT,0)) ELSE CASE WHEN NVL(CB,0)>0 THEN NVL(CB,0) ELSE 0 END  END) DEMAND_PART,\r\n"
				 * +
				 * "SUM(CASE WHEN NVL(CB,0)>(NVL(DEM,0)+NVL(DR_AMT,0)) THEN NVL(CB,0)-(NVL(DEM,0)+NVL(DR_AMT,0))  ELSE 0 END) ARREAR_PART\r\n"
				 * + "FROM CONS,\r\n" +
				 * "(SELECT USCNO,SUM(NVL(OB_OTH,0) +NVL(OB_CCLPC,0)+NVL(TOT_OB,0)) OB,SUM(NVL(CMD,0)+NVL(CCLPC,0)) Dem,SUM(NVL(TOT_PAY,0)) COLL,SUM(NVL(CBTOT,0)+NVL(CB_CCLPC,0)+NVL(CB_OTH,0)) CB FROM LEDGER_HT_HIST WHERE MON_YEAR=? AND SUBSTR(USCNO,1,3) IN('GNT','VJA','ONG','CRD') GROUP BY USCNO) L,\r\n"
				 * +
				 * "(select uscno,SUM(CASE WHEN RJTYPE='DR' THEN (nvl(ENGCHG,0)+nvl(THEFT,0)+nvl(OTHCHG,0)+nvl(CUSTCHG,0)+nvl(DEMCHG,0)+nvl(ED,0)+nvl(LPC,0)+nvl(EDI,0)+nvl(FSA,0)+NVL(CC_LPC,0)+NVL(CC_OTH,0)) END) DR_AMT,SUM(CASE WHEN RJTYPE='CR'THEN (nvl(ENGCHG,0)+nvl(THEFT,0)+nvl(OTHCHG,0)+nvl(CUSTCHG,0)+nvl(DEMCHG,0)+nvl(ED,0)+nvl(LPC,0)+nvl(EDI,0)+nvl(FSA,0)+NVL(CC_LPC,0)+NVL(CC_OTH,0)) END) CR_AMT  from JOURNAL_HIST WHERE to_char(TRUNC(rjdt,'MM'),'MON-YYYY')=? and SUBSTR(USCNO,1,3) IN('GNT','VJA','ONG','CRD') AND TRIM(STATUS) NOT IN ('X') GROUP BY USCNO) RJ\r\n"
				 * +
				 * "WHERE CTUSCNO=L.USCNO(+) AND CTUSCNO=RJ.USCNO(+) GROUP BY SUBSTR(CTUSCNO,1,3),DECODE(CTGOVT_PVT,'Y','GOVT','NON-GOVT'),CTCAT ORDER BY CIRNAME,TYPE,CTCAT)\r\n "
				 * + " where CIRNAME = ?" + "GROUP BY CUBE (CIRNAME,TYPE,CTCAT)\r\n" +
				 * "ORDER BY CIRNAME,TYPE,CTCAT ) where CIRNAME is not null ";
				 */
				return jdbcTemplate.queryForList(sql, new Object[] { monthYear, monthYear, circle });
				/*
				 * String sql =
				 * "select * from ( select  CIRNAME,TYPE,nvl(CTCAT,'TOTAL') CTCAT,sum(NEG_CB)NEG_CB,sum(POS_CB)POS_CB,sum(TOTAL_CB)TOTAL_CB,sum(DEMAND_PART)DEMAND_PART,sum(ARREAR_PART)ARREAR_PART,nvl(CIRNAME,'ZZZ')||nvl(TYPE,'ZZZT') CIR_TYPE  from ( select * from CB_SPLIT  where CIRNAME = ?)\r\n"
				 * + "GROUP BY CUBE (CIRNAME,TYPE,CTCAT)\r\n" +
				 * "ORDER BY CIRNAME,TYPE,CTCAT ) where CIRNAME is not null"; log.info(sql);
				 * return jdbcTemplate.queryForList(sql,new Object[] {circle});
				 */
			} catch (DataAccessException e) {
				e.printStackTrace();
				log.error(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}

		}
	}

	public List<Map<String, Object>> getPVTCollection(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		if (circle.equalsIgnoreCase("ALL")) {
			try {
				String sql = "SELECT * FROM (\r\n"
						+ "SELECT CIRNAME,DIVNAME,ERONAME, SUBNAME, SECNAME,CTUSCNO,SUM(round(NVL(OB,0)))ob, \r\n"
						+ "SUM(Nvl(round(CASE WHEN ob>0 THEN  \r\n" + "CASE WHEN ob>(NVL(TOTAL_AMT,0)) THEN  \r\n"
						+ "(NVL(TOTAL_AMT,0))  \r\n" + "ELSE ob END END),0))  \r\n" + "COLL_ob, \r\n"
						+ "SUM(round(Nvl(BTCURDEM,0))+round(Nvl(BTCOURT_LPC,0))) CMD, \r\n"
						+ "sum(round(CASE WHEN  ob>0 THEN  \r\n" + "CASE WHEN ob>(NVL(TOTAL_AMT,0)) THEN  \r\n"
						+ "0 ELSE (NVL(TOTAL_AMT,0))-ob end \r\n" + "when ob<=0 then \r\n"
						+ "NVL(TOTAL_AMT,0)end)) coll_cmd, \r\n"
						+ "SUM(round(OB))+SUM(round(Nvl(BTCURDEM,0))+round(Nvl(BTCOURT_LPC,0))) TOTAL_ARREARS, \r\n"
						+ "sum((NVL(TOTAL_AMT,0))) TOTAL_COLLECTION ,TO_CHAR(SYSDATE,'MM-YYYY') MON_YEAR,\r\n"
						+ "CASE \r\n" + "     WHEN CTSTATUS='0' THEN 'BILLSTOP'\r\n"
						+ "     WHEN CTSTATUS='1' THEN 'LIVE' \r\n" + "     END STATUS\r\n" + "FROM \r\n"
						+ "(SELECT * FROM HT.CONS WHERE CTGOVT_PVT='N')A, \r\n"
						+ "(SELECT USCNO,ROUND(NVL(CBTOT,0))+ROUND(NVL(CB_CCLPC,0))+ROUND(NVL(CB_OTH,0)) ob FROM HT.LEDGER_HT_HIST\r\n"
						+ "WHERE SUBSTR(USCNO,1,3) IN ('CRD','GNT','ONG','VJA') AND mon_year='NOV-2023' )L, \r\n"
						+ "(SELECT USCNO,SUM(PCMD)TOTAL_AMT FROM HT.PAY_HT WHERE SUBSTR(USCNO,1,3) IN ('CRD','GNT','ONG','VJA') AND PAY_STA_FLG='C' GROUP BY USCNO)P, \r\n"
						+ "(SELECT BTSCNO,BTCURDEM,BTCOURT_LPC,BTBLCAT FROM HT.BILL\r\n"
						+ "WHERE SUBSTR(BTSCNO,1,3) IN ('CRD','GNT','ONG','VJA'))B, \r\n"
						+ "(SELECT * FROM MASTER.SPDCLMASTER)\r\n" + "WHERE  \r\n" + "A.CTUSCNO=L.USCNO(+)\r\n"
						+ "AND A.CTUSCNO=P.USCNO(+)\r\n" + "AND A.CTUSCNO=B.BTSCNO(+)\r\n"
						+ "AND SUBSTR(CTSECCD,-5)=SECCD(+)\r\n"
						+ "GROUP BY CIRNAME,DIVNAME,ERONAME, SUBNAME, SECNAME,CTUSCNO,\r\n" + "case\r\n"
						+ "WHEN CTSTATUS='0' THEN 'BILLSTOP'\r\n" + "                WHEN CTSTATUS='1' THEN 'LIVE' \r\n"
						+ "                 END \r\n" + "ORDER BY CIRNAME,DIVNAME,ERONAME, SUBNAME, SECNAME\r\n"
						+ ") where cirname is not null";
				log.info(sql);
				return jdbcTemplate.queryForList(sql, new Object[] {});
			} catch (DataAccessException e) {
				e.printStackTrace();
				log.error(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}
		} else {
			try {
				String sql = "SELECT * FROM (\r\n"
						+ "SELECT CIRNAME,DIVNAME,ERONAME, SUBNAME, SECNAME,CTUSCNO,SUM(round(NVL(OB,0)))ob, \r\n"
						+ "SUM(Nvl(round(CASE WHEN ob>0 THEN  \r\n" + "CASE WHEN ob>(NVL(TOTAL_AMT,0)) THEN  \r\n"
						+ "(NVL(TOTAL_AMT,0))  \r\n" + "ELSE ob END END),0))  \r\n" + "COLL_ob, \r\n"
						+ "SUM(round(Nvl(BTCURDEM,0))+round(Nvl(BTCOURT_LPC,0))) CMD, \r\n"
						+ "sum(round(CASE WHEN  ob>0 THEN  \r\n" + "CASE WHEN ob>(NVL(TOTAL_AMT,0)) THEN  \r\n"
						+ "0 ELSE (NVL(TOTAL_AMT,0))-ob end \r\n" + "when ob<=0 then \r\n"
						+ "NVL(TOTAL_AMT,0)end)) coll_cmd, \r\n"
						+ "SUM(round(OB))+SUM(round(Nvl(BTCURDEM,0))+round(Nvl(BTCOURT_LPC,0))) TOTAL_ARREARS, \r\n"
						+ "sum((NVL(TOTAL_AMT,0))) TOTAL_COLLECTION ,TO_CHAR(SYSDATE,'MM-YYYY') MON_YEAR,\r\n"
						+ "CASE \r\n" + "     WHEN CTSTATUS='0' THEN 'BILLSTOP'\r\n"
						+ "     WHEN CTSTATUS='1' THEN 'LIVE' \r\n" + "     END STATUS\r\n" + "FROM \r\n"
						+ "(SELECT * FROM HT.CONS WHERE CTGOVT_PVT='N')A, \r\n"
						+ "(SELECT USCNO,ROUND(NVL(CBTOT,0))+ROUND(NVL(CB_CCLPC,0))+ROUND(NVL(CB_OTH,0)) ob FROM HT.LEDGER_HT_HIST\r\n"
						+ "WHERE SUBSTR(USCNO,1,3) IN ('CRD','GNT','ONG','VJA') AND mon_year='NOV-2023' )L, \r\n"
						+ "(SELECT USCNO,SUM(PCMD)TOTAL_AMT FROM HT.PAY_HT WHERE SUBSTR(USCNO,1,3) IN ('CRD','GNT','ONG','VJA') AND PAY_STA_FLG='C' GROUP BY USCNO)P, \r\n"
						+ "(SELECT BTSCNO,BTCURDEM,BTCOURT_LPC,BTBLCAT FROM HT.BILL\r\n"
						+ "WHERE SUBSTR(BTSCNO,1,3) IN ('CRD','GNT','ONG','VJA'))B, \r\n"
						+ "(SELECT * FROM MASTER.SPDCLMASTER)\r\n" + "WHERE  \r\n" + "A.CTUSCNO=L.USCNO(+)\r\n"
						+ "AND A.CTUSCNO=P.USCNO(+)\r\n" + "AND A.CTUSCNO=B.BTSCNO(+)\r\n"
						+ "AND SUBSTR(CTSECCD,-5)=SECCD(+)\r\n"
						+ "GROUP BY CIRNAME,DIVNAME,ERONAME, SUBNAME, SECNAME,CTUSCNO,\r\n" + "case\r\n"
						+ "WHEN CTSTATUS='0' THEN 'BILLSTOP'\r\n" + "                WHEN CTSTATUS='1' THEN 'LIVE' \r\n"
						+ "                 END \r\n" + "ORDER BY CIRNAME,DIVNAME,ERONAME, SUBNAME, SECNAME\r\n"
						+ ") where cirname is not null";
				return jdbcTemplate.queryForList(sql, new Object[] {});

			} catch (DataAccessException e) {
				e.printStackTrace();
				log.error(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}

		}
	}

	public List<Map<String, Object>> getDivisonWiseCBAndDemandAbstractDetails(String circle, String monthYear) {

		if (circle.equalsIgnoreCase("ALL")) {
			try {
				String sql = "select nvl(CIRNAME,'ZZZ') CIRNAME,nvl(TYPE,'TOTAL') TYPE,nvl(CTCAT,'TOTAL') CTCAT,sum(CAT_COUNT) CAT_COUNT,sum(NEG_CB)NEG_CB,sum(POS_CB)POS_CB,sum(TOTAL_CB)TOTAL_CB,sum(DEMAND_PART)DEMAND_PART,sum(ARREAR_PART)ARREAR_PART,nvl(CIRNAME,'ZZZ')||nvl(TYPE,'ZZZT') CIR_TYPE from "
						+ "( SELECT  UNIQUE DIVNAME CIRNAME,DECODE(CTGOVT_PVT,'Y','GOVT','NON-GOVT') TYPE,CTCAT,count(CTCAT) CAT_COUNT,\r\n"
						+ "--NVL(OB,0) TOB,NVL(DEM,0)+NVL(DR_AMT,0) TDEM,\r\n"
						+ "--NVL(COLL,0)+NVL(CR_AMT,0) TCOLL, \r\n"
						+ "SUM(CASE WHEN NVL(CB,0)<0 THEN NVL(CB,0) ELSE 0 END ) NEG_CB,\r\n"
						+ "SUM(CASE WHEN NVL(CB,0)>0 THEN NVL(CB,0) ELSE 0 END ) POS_CB,\r\n"
						+ "SUM(NVL(CB,0)) TOTAL_CB,\r\n"
						+ "SUM(CASE WHEN NVL(CB,0)>(NVL(DEM,0)+NVL(DR_AMT,0)) THEN (NVL(DEM,0)+NVL(DR_AMT,0)) ELSE CASE WHEN NVL(CB,0)>0 THEN NVL(CB,0) ELSE 0 END  END) DEMAND_PART,\r\n"
						+ "SUM(CASE WHEN NVL(CB,0)>(NVL(DEM,0)+NVL(DR_AMT,0)) THEN NVL(CB,0)-(NVL(DEM,0)+NVL(DR_AMT,0))  ELSE 0 END) ARREAR_PART\r\n"
						+ "FROM CONS,master.spdclmaster,\r\n"
						+ "(SELECT USCNO,SUM(NVL(OB_OTH,0) +NVL(OB_CCLPC,0)+NVL(TOT_OB,0)) OB,SUM(NVL(CMD,0)+NVL(CCLPC,0)) Dem,SUM(NVL(TOT_PAY,0)) COLL,SUM(NVL(CBTOT,0)+NVL(CB_CCLPC,0)+NVL(CB_OTH,0)) CB FROM LEDGER_HT_HIST WHERE MON_YEAR=? AND SUBSTR(USCNO,1,3) IN('GNT','VJA','ONG','CRD') GROUP BY USCNO) L,\r\n"
						+ "(select uscno,SUM(CASE WHEN RJTYPE='DR' THEN (nvl(ENGCHG,0)+nvl(THEFT,0)+nvl(OTHCHG,0)+nvl(CUSTCHG,0)+nvl(DEMCHG,0)+nvl(ED,0)+nvl(LPC,0)+nvl(EDI,0)+nvl(FSA,0)+NVL(CC_LPC,0)+NVL(CC_OTH,0)) END) DR_AMT,SUM(CASE WHEN RJTYPE='CR'THEN (nvl(ENGCHG,0)+nvl(THEFT,0)+nvl(OTHCHG,0)+nvl(CUSTCHG,0)+nvl(DEMCHG,0)+nvl(ED,0)+nvl(LPC,0)+nvl(EDI,0)+nvl(FSA,0)+NVL(CC_LPC,0)+NVL(CC_OTH,0)) END) CR_AMT  from JOURNAL_HIST WHERE to_char(TRUNC(rjdt,'MM'),'MON-YYYY')=? and SUBSTR(USCNO,1,3) IN('GNT','VJA','ONG','CRD') AND TRIM(STATUS) NOT IN ('X') GROUP BY USCNO) RJ\r\n"
						+ "WHERE CTUSCNO=L.USCNO(+) " + "and SUBSTR(CTSECCD,-5)=SECCD(+) "
						+ "AND CTUSCNO=RJ.USCNO(+) GROUP BY DIVNAME,DECODE(CTGOVT_PVT,'Y','GOVT','NON-GOVT'),CTCAT ORDER BY CIRNAME,TYPE,CTCAT)\r\n"
						+ "GROUP BY CUBE (CIRNAME,TYPE,CTCAT)\r\n" + "ORDER BY CIRNAME,TYPE,CTCAT";
				log.info(sql);
				return jdbcTemplate.queryForList(sql, new Object[] { monthYear, monthYear });

			} catch (DataAccessException e) {
				e.printStackTrace();
				log.error(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}
		} else {
			try {
				String sql = "select * from (  select CIRNAME,nvl(TYPE,'TOTAL') TYPE,nvl(CTCAT,'TOTAL') CTCAT,sum(CAT_COUNT) CAT_COUNT,sum(NEG_CB)NEG_CB,sum(POS_CB)POS_CB,sum(TOTAL_CB)TOTAL_CB,sum(DEMAND_PART)DEMAND_PART,sum(ARREAR_PART)ARREAR_PART,nvl(CIRNAME,'ZZZ')||nvl(TYPE,'ZZZT') CIR_TYPE from "
						+ "( SELECT  UNIQUE DIVNAME CIRNAME,DECODE(CTGOVT_PVT,'Y','GOVT','NON-GOVT') TYPE,CTCAT,count(CTCAT) CAT_COUNT,\r\n"
						+ "--NVL(OB,0) TOB,NVL(DEM,0)+NVL(DR_AMT,0) TDEM,\r\n"
						+ "--NVL(COLL,0)+NVL(CR_AMT,0) TCOLL, \r\n"
						+ "SUM(CASE WHEN NVL(CB,0)<0 THEN NVL(CB,0) ELSE 0 END ) NEG_CB,\r\n"
						+ "SUM(CASE WHEN NVL(CB,0)>0 THEN NVL(CB,0) ELSE 0 END ) POS_CB,\r\n"
						+ "SUM(NVL(CB,0)) TOTAL_CB,\r\n"
						+ "SUM(CASE WHEN NVL(CB,0)>(NVL(DEM,0)+NVL(DR_AMT,0)) THEN (NVL(DEM,0)+NVL(DR_AMT,0)) ELSE CASE WHEN NVL(CB,0)>0 THEN NVL(CB,0) ELSE 0 END  END) DEMAND_PART,\r\n"
						+ "SUM(CASE WHEN NVL(CB,0)>(NVL(DEM,0)+NVL(DR_AMT,0)) THEN NVL(CB,0)-(NVL(DEM,0)+NVL(DR_AMT,0))  ELSE 0 END) ARREAR_PART\r\n"
						+ "FROM CONS,master.spdclmaster,\r\n"
						+ "(SELECT USCNO,SUM(NVL(OB_OTH,0) +NVL(OB_CCLPC,0)+NVL(TOT_OB,0)) OB,SUM(NVL(CMD,0)+NVL(CCLPC,0)) Dem,SUM(NVL(TOT_PAY,0)) COLL,SUM(NVL(CBTOT,0)+NVL(CB_CCLPC,0)+NVL(CB_OTH,0)) CB FROM LEDGER_HT_HIST WHERE MON_YEAR=? AND SUBSTR(USCNO,1,3) IN('GNT','VJA','ONG','CRD') GROUP BY USCNO) L,\r\n"
						+ "(select uscno,SUM(CASE WHEN RJTYPE='DR' THEN (nvl(ENGCHG,0)+nvl(THEFT,0)+nvl(OTHCHG,0)+nvl(CUSTCHG,0)+nvl(DEMCHG,0)+nvl(ED,0)+nvl(LPC,0)+nvl(EDI,0)+nvl(FSA,0)+NVL(CC_LPC,0)+NVL(CC_OTH,0)) END) DR_AMT,SUM(CASE WHEN RJTYPE='CR'THEN (nvl(ENGCHG,0)+nvl(THEFT,0)+nvl(OTHCHG,0)+nvl(CUSTCHG,0)+nvl(DEMCHG,0)+nvl(ED,0)+nvl(LPC,0)+nvl(EDI,0)+nvl(FSA,0)+NVL(CC_LPC,0)+NVL(CC_OTH,0)) END) CR_AMT  from JOURNAL_HIST WHERE to_char(TRUNC(rjdt,'MM'),'MON-YYYY')=? and SUBSTR(USCNO,1,3) IN('GNT','VJA','ONG','CRD') AND TRIM(STATUS) NOT IN ('X') GROUP BY USCNO) RJ\r\n"
						+ "WHERE CTUSCNO=L.USCNO(+) " + "and SUBSTR(CTSECCD,-5)=SECCD(+) "
						+ "AND CTUSCNO=RJ.USCNO(+) and substr(CTUSCNO,1,3)=? GROUP BY DIVNAME,DECODE(CTGOVT_PVT,'Y','GOVT','NON-GOVT'),CTCAT ORDER BY CIRNAME,TYPE,CTCAT) \r\n"
						+ " GROUP BY CUBE (CIRNAME,TYPE,CTCAT)\r\n"
						+ "ORDER BY CIRNAME,TYPE,CTCAT  ) where CIRNAME is not null";
				log.info(sql);
				return jdbcTemplate.queryForList(sql, new Object[] { monthYear, monthYear, circle });
			} catch (DataAccessException e) {
				e.printStackTrace();
				log.error(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}

		}
	}

	public List<Map<String, Object>> getSubDivisonWiseCBAndDemandAbstractDetails(String circle, String monthYear) {

		if (circle.equalsIgnoreCase("ALL")) {
			try {
				String sql = "select nvl(CIRNAME,'ZZZ') CIRNAME,nvl(TYPE,'TOTAL') TYPE,nvl(CTCAT,'TOTAL') CTCAT,sum(CAT_COUNT) CAT_COUNT,sum(NEG_CB)NEG_CB,sum(POS_CB)POS_CB,sum(TOTAL_CB)TOTAL_CB,sum(DEMAND_PART)DEMAND_PART,sum(ARREAR_PART)ARREAR_PART,nvl(CIRNAME,'ZZZ')||nvl(TYPE,'ZZZT') CIR_TYPE from "
						+ "( SELECT  UNIQUE SUBNAME CIRNAME,DECODE(CTGOVT_PVT,'Y','GOVT','NON-GOVT') TYPE,CTCAT,count(CTCAT) CAT_COUNT,\r\n"
						+ "--NVL(OB,0) TOB,NVL(DEM,0)+NVL(DR_AMT,0) TDEM,\r\n"
						+ "--NVL(COLL,0)+NVL(CR_AMT,0) TCOLL, \r\n"
						+ "SUM(CASE WHEN NVL(CB,0)<0 THEN NVL(CB,0) ELSE 0 END ) NEG_CB,\r\n"
						+ "SUM(CASE WHEN NVL(CB,0)>0 THEN NVL(CB,0) ELSE 0 END ) POS_CB,\r\n"
						+ "SUM(NVL(CB,0)) TOTAL_CB,\r\n"
						+ "SUM(CASE WHEN NVL(CB,0)>(NVL(DEM,0)+NVL(DR_AMT,0)) THEN (NVL(DEM,0)+NVL(DR_AMT,0)) ELSE CASE WHEN NVL(CB,0)>0 THEN NVL(CB,0) ELSE 0 END  END) DEMAND_PART,\r\n"
						+ "SUM(CASE WHEN NVL(CB,0)>(NVL(DEM,0)+NVL(DR_AMT,0)) THEN NVL(CB,0)-(NVL(DEM,0)+NVL(DR_AMT,0))  ELSE 0 END) ARREAR_PART\r\n"
						+ "FROM CONS,master.spdclmaster,\r\n"
						+ "(SELECT USCNO,SUM(NVL(OB_OTH,0) +NVL(OB_CCLPC,0)+NVL(TOT_OB,0)) OB,SUM(NVL(CMD,0)+NVL(CCLPC,0)) Dem,SUM(NVL(TOT_PAY,0)) COLL,SUM(NVL(CBTOT,0)+NVL(CB_CCLPC,0)+NVL(CB_OTH,0)) CB FROM LEDGER_HT_HIST WHERE MON_YEAR=? AND SUBSTR(USCNO,1,3) IN('GNT','VJA','ONG','CRD') GROUP BY USCNO) L,\r\n"
						+ "(select uscno,SUM(CASE WHEN RJTYPE='DR' THEN (nvl(ENGCHG,0)+nvl(THEFT,0)+nvl(OTHCHG,0)+nvl(CUSTCHG,0)+nvl(DEMCHG,0)+nvl(ED,0)+nvl(LPC,0)+nvl(EDI,0)+nvl(FSA,0)+NVL(CC_LPC,0)+NVL(CC_OTH,0)) END) DR_AMT,SUM(CASE WHEN RJTYPE='CR'THEN (nvl(ENGCHG,0)+nvl(THEFT,0)+nvl(OTHCHG,0)+nvl(CUSTCHG,0)+nvl(DEMCHG,0)+nvl(ED,0)+nvl(LPC,0)+nvl(EDI,0)+nvl(FSA,0)+NVL(CC_LPC,0)+NVL(CC_OTH,0)) END) CR_AMT  from JOURNAL_HIST WHERE to_char(TRUNC(rjdt,'MM'),'MON-YYYY')=? and SUBSTR(USCNO,1,3) IN('GNT','VJA','ONG','CRD') AND TRIM(STATUS) NOT IN ('X') GROUP BY USCNO) RJ\r\n"
						+ "WHERE CTUSCNO=L.USCNO(+) " + "and SUBSTR(CTSECCD,-5)=SECCD(+) "
						+ "AND CTUSCNO=RJ.USCNO(+) GROUP BY SUBNAME,DECODE(CTGOVT_PVT,'Y','GOVT','NON-GOVT'),CTCAT ORDER BY CIRNAME,TYPE,CTCAT)\r\n"
						+ "GROUP BY CUBE (CIRNAME,TYPE,CTCAT)\r\n" + "ORDER BY CIRNAME,TYPE,CTCAT";
				log.info(sql);
				return jdbcTemplate.queryForList(sql, new Object[] { monthYear, monthYear });

			} catch (DataAccessException e) {
				e.printStackTrace();
				log.error(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}
		} else {
			try {
				String sql = "select * from (  select CIRNAME,nvl(TYPE,'TOTAL') TYPE,nvl(CTCAT,'TOTAL') CTCAT,sum(CAT_COUNT) CAT_COUNT,sum(NEG_CB)NEG_CB,sum(POS_CB)POS_CB,sum(TOTAL_CB)TOTAL_CB,sum(DEMAND_PART)DEMAND_PART,sum(ARREAR_PART)ARREAR_PART,nvl(CIRNAME,'ZZZ')||nvl(TYPE,'ZZZT') CIR_TYPE from "
						+ "( SELECT  UNIQUE SUBNAME CIRNAME,DECODE(CTGOVT_PVT,'Y','GOVT','NON-GOVT') TYPE,CTCAT,count(CTCAT) CAT_COUNT,\r\n"
						+ "--NVL(OB,0) TOB,NVL(DEM,0)+NVL(DR_AMT,0) TDEM,\r\n"
						+ "--NVL(COLL,0)+NVL(CR_AMT,0) TCOLL, \r\n"
						+ "SUM(CASE WHEN NVL(CB,0)<0 THEN NVL(CB,0) ELSE 0 END ) NEG_CB,\r\n"
						+ "SUM(CASE WHEN NVL(CB,0)>0 THEN NVL(CB,0) ELSE 0 END ) POS_CB,\r\n"
						+ "SUM(NVL(CB,0)) TOTAL_CB,\r\n"
						+ "SUM(CASE WHEN NVL(CB,0)>(NVL(DEM,0)+NVL(DR_AMT,0)) THEN (NVL(DEM,0)+NVL(DR_AMT,0)) ELSE CASE WHEN NVL(CB,0)>0 THEN NVL(CB,0) ELSE 0 END  END) DEMAND_PART,\r\n"
						+ "SUM(CASE WHEN NVL(CB,0)>(NVL(DEM,0)+NVL(DR_AMT,0)) THEN NVL(CB,0)-(NVL(DEM,0)+NVL(DR_AMT,0))  ELSE 0 END) ARREAR_PART\r\n"
						+ "FROM CONS,master.spdclmaster,\r\n"
						+ "(SELECT USCNO,SUM(NVL(OB_OTH,0) +NVL(OB_CCLPC,0)+NVL(TOT_OB,0)) OB,SUM(NVL(CMD,0)+NVL(CCLPC,0)) Dem,SUM(NVL(TOT_PAY,0)) COLL,SUM(NVL(CBTOT,0)+NVL(CB_CCLPC,0)+NVL(CB_OTH,0)) CB FROM LEDGER_HT_HIST WHERE MON_YEAR=? AND SUBSTR(USCNO,1,3) IN('GNT','VJA','ONG','CRD') GROUP BY USCNO) L,\r\n"
						+ "(select uscno,SUM(CASE WHEN RJTYPE='DR' THEN (nvl(ENGCHG,0)+nvl(THEFT,0)+nvl(OTHCHG,0)+nvl(CUSTCHG,0)+nvl(DEMCHG,0)+nvl(ED,0)+nvl(LPC,0)+nvl(EDI,0)+nvl(FSA,0)+NVL(CC_LPC,0)+NVL(CC_OTH,0)) END) DR_AMT,SUM(CASE WHEN RJTYPE='CR'THEN (nvl(ENGCHG,0)+nvl(THEFT,0)+nvl(OTHCHG,0)+nvl(CUSTCHG,0)+nvl(DEMCHG,0)+nvl(ED,0)+nvl(LPC,0)+nvl(EDI,0)+nvl(FSA,0)+NVL(CC_LPC,0)+NVL(CC_OTH,0)) END) CR_AMT  from JOURNAL_HIST WHERE to_char(TRUNC(rjdt,'MM'),'MON-YYYY')=? and SUBSTR(USCNO,1,3) IN('GNT','VJA','ONG','CRD') AND TRIM(STATUS) NOT IN ('X') GROUP BY USCNO) RJ\r\n"
						+ "WHERE CTUSCNO=L.USCNO(+) " + "and SUBSTR(CTSECCD,-5)=SECCD(+) "
						+ "AND CTUSCNO=RJ.USCNO(+) and DIVNAME=? GROUP BY SUBNAME,DECODE(CTGOVT_PVT,'Y','GOVT','NON-GOVT'),CTCAT ORDER BY CIRNAME,TYPE,CTCAT) \r\n"
						+ " GROUP BY CUBE (CIRNAME,TYPE,CTCAT)\r\n"
						+ "ORDER BY CIRNAME,TYPE,CTCAT  ) where CIRNAME is not null";
				log.info(sql);
				return jdbcTemplate.queryForList(sql, new Object[] { monthYear, monthYear, circle });
			} catch (DataAccessException e) {
				e.printStackTrace();
				log.error(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}

		}
	}

	public List<Map<String, Object>> getAgeWiseConsumerLedgerAbstractDetails(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		String status = request.getParameter("status");
		String constring = status.equals("LB") ? " "
				: status.equals("L") ? "nvl(status,'BS')  in ('LIVE','1','NEW','UDC')  AND "
						: " nvl(status,'BS') not in ('LIVE','1','NEW','UDC') AND ";
		String fmonthYear = request.getParameter("fmonth") + "-" + request.getParameter("fyear");
		System.out.println(fmonthYear);

		if (circle.equalsIgnoreCase("ALL")) {
			try {
				String sql = "select * from ( select nvl(CIRCLE,'APCPDCL') CIRCLE,nvl(AGE,'TOTAL') AGE,sum(NOS) NOS,round(sum(TCC)) TCC,round(sum(TED)) TED,round(sum(TEDI)) TEDI,round(sum(TLPC)) TLPC,round(sum(TOTH))TOTH,round(sum(TCB))TCB\r\n"
						+ "from (SELECT SUBSTR(CTUSCNO,1,3) CIRCLE,AGE,COUNT(USCNO) NOS,SUM(NVL(CC,0)) TCC,SUM(NVL(ED,0)) TED,SUM(NVL(EDI,0)) TEDI,SUM(NVL(LPC,0)) TLPC,SUM(NVL(OTH,0)) TOTH,SUM(NVL(CB,0)) TCB FROM CONS,\r\n"
						+ "(SELECT USCNO,PAY_DT,mon_year,MONTHS_BETWEEN(TRUNC(LAST_DAY(to_date(?,'MM-YYYY')),'MM'),PAY_DT)/12 LAST_PAID_AGE,\r\n"
						+ "CASE WHEN (MONTHS_BETWEEN(TRUNC(LAST_DAY(to_date(?,'MM-YYYY')),'MM'),PAY_DT)/12)>0 AND (MONTHS_BETWEEN(TRUNC(LAST_DAY(to_date(?,'MM-YYYY')),'MM'),PAY_DT)/12)<=0.5 THEN 'UPTO_6_MONTHS' ELSE \r\n"
						+ "CASE WHEN (MONTHS_BETWEEN(TRUNC(LAST_DAY(to_date(?,'MM-YYYY')),'MM'),PAY_DT)/12)>0.5 AND (MONTHS_BETWEEN(TRUNC(LAST_DAY(to_date(?,'MM-YYYY')),'MM'),PAY_DT)/12)<=1 THEN 'BELOW_1_YR_AND_ABV_6_MONTHS' ELSE \r\n"
						+ "CASE WHEN (MONTHS_BETWEEN(TRUNC(LAST_DAY(to_date(?,'MM-YYYY')),'MM'),PAY_DT)/12)>1 AND (MONTHS_BETWEEN(TRUNC(LAST_DAY(to_date(?,'MM-YYYY')),'MM'),PAY_DT)/12)<=2 THEN 'ABOVE_1_YR' ELSE \r\n"
						+ "CASE WHEN (MONTHS_BETWEEN(TRUNC(LAST_DAY(to_date(?,'MM-YYYY')),'MM'),PAY_DT)/12)>2 AND (MONTHS_BETWEEN(TRUNC(LAST_DAY(to_date(?,'MM-YYYY')),'MM'),PAY_DT)/12)<=3 THEN 'ABOVE_2_YR' ELSE\r\n"
						+ "CASE WHEN (MONTHS_BETWEEN(TRUNC(LAST_DAY(to_date(?,'MM-YYYY')),'MM'),PAY_DT)/12)>3 AND (MONTHS_BETWEEN(TRUNC(LAST_DAY(to_date(?,'MM-YYYY')),'MM'),PAY_DT)/12)<=4 THEN 'ABOVE_3_YR' ELSE\r\n"
						+ "CASE WHEN (MONTHS_BETWEEN(TRUNC(LAST_DAY(to_date(?,'MM-YYYY')),'MM'),PAY_DT)/12)>4 AND (MONTHS_BETWEEN(TRUNC(LAST_DAY(to_date(?,'MM-YYYY')),\r\n"
						+ "'MM'),PAY_DT)/12)<=5 THEN 'ABOVE_4_YR' ELSE\r\n"
						+ "CASE WHEN (MONTHS_BETWEEN(TRUNC(LAST_DAY(to_date(?,'MM-YYYY')),'MM'),PAY_DT)/12)>5 THEN 'ABOVE_5_YR' ELSE 'BELOW' END END END END END END END AGE,\r\n"
						+ "nvl(CB_EC,0)+NVL(CB_FSA,0) CC,NVL(CB_ED,0) ED,NVL(CB_IED,0) EDI,NVL(CB_LPC,0)+NVL(CB_CCLPC,0) LPC,NVL(CB_OTH,0) OTH,NVL(CBTOT,0)+NVL(CB_CCLPC,0)+NVL(CB_OTH,0) CB FROM  LEDGER_HT_HIST,\r\n"
						+ "(SELECT USCNO SCNO,MAX(TRUNC(TO_DATE(MON_YEAR,'MON-YYYY'),'MM')) PAY_DT FROM LEDGER_HT_HIST  WHERE "
						+ constring
						+ " SUBSTR(USCNO,1,3) IN('GNT','VJA','ONG','CRD') AND  NVL(TOT_PAY,0)+NVL(CRJ,0)>0 GROUP BY USCNO\r\n"
						+ "UNION\r\n"
						+ "SELECT USCNO SCNO,MIN(TRUNC(TO_DATE(MON_YEAR,'MON-YYYY'),'MM')) PAY_DT FROM LEDGER_HT_HIST  WHERE "
						+ constring
						+ "  SUBSTR(USCNO,1,3) IN('GNT','VJA','ONG','CRD') GROUP BY USCNO HAVING SUM(NVL(TOT_PAY,0)+NVL(CRJ,0))=0) \r\n"
						+ "WHERE USCNO=SCNO AND TRUNC(TO_DATE(MON_YEAR,'MON-YYYY'),'MM')=ADD_MONTHS(TRUNC(LAST_DAY(to_date(?,'MM-YYYY')),'MM'),-1) and (NVL(CBTOT,0)+NVL(CB_CCLPC,0)+NVL(CB_OTH,0))>1) WHERE USCNO=CTUSCNO \r\n"
						+ "GROUP BY SUBSTR(CTUSCNO,1,3) ,AGE\r\n"
						+ " ORDER BY CIRCLE,SUBSTR(AGE,1,5),SUBSTR(AGE,7)  DESC)\r\n"
						+ " GROUP BY CUBE (CIRCLE, AGE)\r\n"
						+ "ORDER BY  case when CIRCLE = 'VJA' then '001' when CIRCLE = 'GNT' then '002' when CIRCLE = 'ONG' then '003'  when CIRCLE = 'CRD' then '009' else CIRCLE end, "
						+ " case when AGE = 'ABOVE_5_YR' then '001' " + " when AGE = 'ABOVE_4_YR' then '002'  "
						+ " when AGE = 'ABOVE_3_YR' then '003' " + " when AGE = 'ABOVE_2_YR' then '004' "
						+ " when AGE = 'ABOVE_1_YR' then '005' "
						+ " when AGE = 'BELOW_1_YR_AND_ABV_6_MONTHS' then '006' "
						+ " when AGE = 'UPTO_6_MONTHS' then '007' " + "else AGE end " + ""
						+ " ) where CIRCLE is not null";
				log.info(sql);
				return jdbcTemplate.queryForList(sql,
						new Object[] { fmonthYear, fmonthYear, fmonthYear, fmonthYear, fmonthYear, fmonthYear,
								fmonthYear, fmonthYear, fmonthYear, fmonthYear, fmonthYear, fmonthYear, fmonthYear,
								fmonthYear, fmonthYear });
			} catch (DataAccessException e) {
				e.printStackTrace();
				log.error(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}
		} else {
			try {

				String sql = "select * from ( select nvl(CIRCLE,'APCPDCL') CIRCLE,nvl(AGE,'TOTAL') AGE,sum(NOS) NOS,round(sum(TCC)) TCC,round(sum(TED)) TED,round(sum(TEDI)) TEDI,round(sum(TLPC)) TLPC,round(sum(TOTH))TOTH,round(sum(TCB))TCB\r\n"
						+ "from (SELECT SUBSTR(CTUSCNO,1,3) CIRCLE,AGE,COUNT(USCNO) NOS,SUM(NVL(CC,0)) TCC,SUM(NVL(ED,0)) TED,SUM(NVL(EDI,0)) TEDI,SUM(NVL(LPC,0)) TLPC,SUM(NVL(OTH,0)) TOTH,SUM(NVL(CB,0)) TCB FROM CONS,\r\n"
						+ "(SELECT USCNO,PAY_DT,mon_year,MONTHS_BETWEEN(TRUNC(LAST_DAY(to_date(?,'MM-YYYY')),'MM'),PAY_DT)/12 LAST_PAID_AGE,\r\n"
						+ "CASE WHEN (MONTHS_BETWEEN(TRUNC(LAST_DAY(to_date(?,'MM-YYYY')),'MM'),PAY_DT)/12)>0 AND (MONTHS_BETWEEN(TRUNC(LAST_DAY(to_date(?,'MM-YYYY')),'MM'),PAY_DT)/12)<=0.5 THEN 'UPTO_6_MONTHS' ELSE \r\n"
						+ "CASE WHEN (MONTHS_BETWEEN(TRUNC(LAST_DAY(to_date(?,'MM-YYYY')),'MM'),PAY_DT)/12)>0.5 AND (MONTHS_BETWEEN(TRUNC(LAST_DAY(to_date(?,'MM-YYYY')),'MM'),PAY_DT)/12)<=1 THEN 'BELOW_1_YR_AND_ABV_6_MONTHS' ELSE \r\n"
						+ "CASE WHEN (MONTHS_BETWEEN(TRUNC(LAST_DAY(to_date(?,'MM-YYYY')),'MM'),PAY_DT)/12)>1 AND (MONTHS_BETWEEN(TRUNC(LAST_DAY(to_date(?,'MM-YYYY')),'MM'),PAY_DT)/12)<=2 THEN 'ABOVE_1_YR' ELSE \r\n"
						+ "CASE WHEN (MONTHS_BETWEEN(TRUNC(LAST_DAY(to_date(?,'MM-YYYY')),'MM'),PAY_DT)/12)>2 AND (MONTHS_BETWEEN(TRUNC(LAST_DAY(to_date(?,'MM-YYYY')),'MM'),PAY_DT)/12)<=3 THEN 'ABOVE_2_YR' ELSE\r\n"
						+ "CASE WHEN (MONTHS_BETWEEN(TRUNC(LAST_DAY(to_date(?,'MM-YYYY')),'MM'),PAY_DT)/12)>3 AND (MONTHS_BETWEEN(TRUNC(LAST_DAY(to_date(?,'MM-YYYY')),'MM'),PAY_DT)/12)<=4 THEN 'ABOVE_3_YR' ELSE\r\n"
						+ "CASE WHEN (MONTHS_BETWEEN(TRUNC(LAST_DAY(to_date(?,'MM-YYYY')),'MM'),PAY_DT)/12)>4 AND (MONTHS_BETWEEN(TRUNC(LAST_DAY(to_date(?,'MM-YYYY')),\r\n"
						+ "'MM'),PAY_DT)/12)<=5 THEN 'ABOVE_4_YR' ELSE\r\n"
						+ "CASE WHEN (MONTHS_BETWEEN(TRUNC(LAST_DAY(to_date(?,'MM-YYYY')),'MM'),PAY_DT)/12)>5 THEN 'ABOVE_5_YR' ELSE 'BELOW' END END END END END END END AGE,\r\n"
						+ "nvl(CB_EC,0)+NVL(CB_FSA,0) CC,NVL(CB_ED,0) ED,NVL(CB_IED,0) EDI,NVL(CB_LPC,0)+NVL(CB_CCLPC,0) LPC,NVL(CB_OTH,0) OTH,NVL(CBTOT,0)+NVL(CB_CCLPC,0)+NVL(CB_OTH,0) CB FROM  LEDGER_HT_HIST,\r\n"
						+ "(SELECT USCNO SCNO,MAX(TRUNC(TO_DATE(MON_YEAR,'MON-YYYY'),'MM')) PAY_DT FROM LEDGER_HT_HIST  WHERE "
						+ constring
						+ " SUBSTR(USCNO,1,3) IN('GNT','VJA','ONG','CRD') AND  NVL(TOT_PAY,0)+NVL(CRJ,0)>0 GROUP BY USCNO\r\n"
						+ "UNION\r\n"
						+ "SELECT USCNO SCNO,MIN(TRUNC(TO_DATE(MON_YEAR,'MON-YYYY'),'MM')) PAY_DT FROM LEDGER_HT_HIST  WHERE "
						+ constring
						+ "  SUBSTR(USCNO,1,3) IN('GNT','VJA','ONG','CRD') GROUP BY USCNO HAVING SUM(NVL(TOT_PAY,0)+NVL(CRJ,0))=0) \r\n"
						+ "WHERE USCNO=SCNO AND TRUNC(TO_DATE(MON_YEAR,'MON-YYYY'),'MM')=ADD_MONTHS(TRUNC(LAST_DAY(to_date(?,'MM-YYYY')),'MM'),-1) and (NVL(CBTOT,0)+NVL(CB_CCLPC,0)+NVL(CB_OTH,0))>1) WHERE USCNO=CTUSCNO \r\n"
						+ "GROUP BY SUBSTR(CTUSCNO,1,3) ,AGE\r\n"
						+ " ORDER BY CIRCLE,SUBSTR(AGE,1,5),SUBSTR(AGE,7)  DESC) where CIRCLE=? \r\n"
						+ " GROUP BY CUBE (CIRCLE, AGE)\r\n"
						+ "ORDER BY  case when CIRCLE = 'VJA' then '001' when CIRCLE = 'GNT' then '002' when CIRCLE = 'ONG' then '003'  when CIRCLE = 'CRD' then '009' else CIRCLE end, "
						+ " case when AGE = 'ABOVE_5_YR' then '001' " + " when AGE = 'ABOVE_4_YR' then '002'  "
						+ " when AGE = 'ABOVE_3_YR' then '003' " + " when AGE = 'ABOVE_2_YR' then '004' "
						+ " when AGE = 'ABOVE_1_YR' then '005' "
						+ " when AGE = 'BELOW_1_YR_AND_ABV_6_MONTHS' then '006' "
						+ " when AGE = 'UPTO_6_MONTHS' then '007' " + "else AGE end " + ""
						+ " ) where CIRCLE is not null";
				log.info(sql);
				return jdbcTemplate.queryForList(sql,
						new Object[] { fmonthYear, fmonthYear, fmonthYear, fmonthYear, fmonthYear, fmonthYear,
								fmonthYear, fmonthYear, fmonthYear, fmonthYear, fmonthYear, fmonthYear, fmonthYear,
								fmonthYear, fmonthYear, circle });
			} catch (DataAccessException e) {
				e.printStackTrace();
				log.error(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}

		}
	}

	public List<Map<String, Object>> getAgeWiseGovtPvtStatusLedgerAbstract(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		if (circle.equalsIgnoreCase("ALL")) {
			try {
				String sql = "select nvl(CIRCLE,'APCPDCL') CIRCLE,nvl(AGE,'TOTAL') AGE, SUM(G_LIVE_NOS) G_LIVE_NOS,SUM(G_LIVE_CB) G_LIVE_CB,SUM(G_UDC_NOS) G_UDC_NOS,\r\n"
						+ "SUM(G_UDC_CB) G_UDC_CB,SUM(G_BS_NOS) G_BS_NOS,SUM(G_BS_CB) G_BS_CB,SUM(P_LIVE_NOS) P_LIVE_NOS,SUM(P_LIVE_CB) P_LIVE_CB,SUM(P_UDC_NOS) P_UDC_NOS,\r\n"
						+ "SUM(P_UDC_CB) P_UDC_CB,SUM(P_BS_NOS) P_BS_NOS,SUM(P_BS_CB) P_BS_CB,SUM(TOT_LIVE_NOS) TOT_LIVE_NOS,SUM(TOT_LIVE_CB) TOT_LIVE_CB,\r\n"
						+ "SUM(TOT_UDC_NOS) TOT_UDC_NOS,SUM(TOT_UDC_CB) TOT_UDC_CB,SUM(TOT_BS_NOS) TOT_BS_NOS,SUM(TOT_BS_CB) TOT_BS_CB,SUM(TOT_NOS) TOT_NOS,\r\n"
						+ "SUM(TOT_CB) TOT_CB from ( SELECT SUBSTR(CTUSCNO,1,3) CIRCLE,AGE,\r\n"
						+ "COUNT(CASE WHEN NVL(CTGOVT_PVT,'N')='Y' AND STATUS='LIVE' THEN USCNO END) G_LIVE_NOS,\r\n"
						+ "SUM(CASE WHEN NVL(CTGOVT_PVT,'N')='Y' AND STATUS='LIVE' THEN NVL(CB,0)  ELSE 0 END) G_LIVE_CB,\r\n"
						+ "COUNT(CASE WHEN NVL(CTGOVT_PVT,'N')='Y' AND STATUS='UDC' THEN USCNO END) G_UDC_NOS,\r\n"
						+ "SUM(CASE WHEN NVL(CTGOVT_PVT,'N')='Y' AND STATUS='UDC' THEN NVL(CB,0) ELSE 0  END) G_UDC_CB,\r\n"
						+ "COUNT(CASE WHEN NVL(CTGOVT_PVT,'N')='Y' AND STATUS='BS' THEN USCNO END) G_BS_NOS,\r\n"
						+ "SUM(CASE WHEN NVL(CTGOVT_PVT,'N')='Y' AND STATUS='BS' THEN NVL(CB,0) ELSE 0  END) G_BS_CB,\r\n"
						+ "COUNT(CASE WHEN NVL(CTGOVT_PVT,'N')='N' AND STATUS='LIVE' THEN USCNO END) P_LIVE_NOS,\r\n"
						+ "SUM(CASE WHEN NVL(CTGOVT_PVT,'N')='N' AND STATUS='LIVE' THEN NVL(CB,0) ELSE 0 END)   P_LIVE_CB,\r\n"
						+ "COUNT(CASE WHEN NVL(CTGOVT_PVT,'N')='N' AND STATUS='UDC' THEN USCNO END) P_UDC_NOS,\r\n"
						+ "SUM(CASE WHEN NVL(CTGOVT_PVT,'N')='N' AND STATUS='UDC' THEN NVL(CB,0) ELSE 0 END) P_UDC_CB,\r\n"
						+ "COUNT(CASE WHEN NVL(CTGOVT_PVT,'N')='N' AND STATUS='BS' THEN USCNO END) P_BS_NOS,\r\n"
						+ "SUM(CASE WHEN NVL(CTGOVT_PVT,'N')='N' AND STATUS='BS' THEN NVL(CB,0)  ELSE 0 END) P_BS_CB,\r\n"
						+ "COUNT(CASE WHEN STATUS='LIVE' THEN USCNO END) TOT_LIVE_NOS,\r\n"
						+ "SUM(CASE WHEN STATUS='LIVE' THEN NVL(CB,0) ELSE 0 END)   TOT_LIVE_CB,\r\n"
						+ "COUNT(CASE WHEN STATUS='UDC' THEN USCNO END) TOT_UDC_NOS,\r\n"
						+ "SUM(CASE WHEN STATUS='UDC' THEN NVL(CB,0) ELSE 0 END) TOT_UDC_CB,\r\n"
						+ "COUNT(CASE WHEN STATUS='BS' THEN USCNO END) TOT_BS_NOS,\r\n"
						+ "SUM(CASE WHEN STATUS='BS' THEN NVL(CB,0)  ELSE 0 END) TOT_BS_CB,\r\n"
						+ "COUNT(USCNO) TOT_NOS,\r\n" + "SUM(NVL(CB,0))   TOT_CB\r\n" + "FROM CONS,\r\n"
						+ "(SELECT USCNO,CASE WHEN STAT='UD' THEN 'UDC' WHEN STAT IS NULL THEN 'BS' WHEN STAT='LIV' THEN 'LIVE' END STATUS,PAY_DT,mon_year,MONTHS_BETWEEN(TRUNC(SYSDATE,'MM'),PAY_DT)/12 LAST_PAID_AGE,\r\n"
						+ "CASE WHEN (MONTHS_BETWEEN(TRUNC(SYSDATE,'MM'),PAY_DT)/12)>0 AND (MONTHS_BETWEEN(TRUNC(SYSDATE,'MM'),PAY_DT)/12)<=0.5 THEN 'UPTO_6_MONTHS' ELSE \r\n"
						+ "CASE WHEN (MONTHS_BETWEEN(TRUNC(SYSDATE,'MM'),PAY_DT)/12)>0.5 AND (MONTHS_BETWEEN(TRUNC(SYSDATE,'MM'),PAY_DT)/12)<=1 THEN 'BELOW_1_YR_AND_ABV_6_MONTHS' ELSE \r\n"
						+ "CASE WHEN (MONTHS_BETWEEN(TRUNC(SYSDATE,'MM'),PAY_DT)/12)>1 AND (MONTHS_BETWEEN(TRUNC(SYSDATE,'MM'),PAY_DT)/12)<=2 THEN 'ABOVE_1_YR' ELSE \r\n"
						+ "CASE WHEN (MONTHS_BETWEEN(TRUNC(SYSDATE,'MM'),PAY_DT)/12)>2 AND (MONTHS_BETWEEN(TRUNC(SYSDATE,'MM'),PAY_DT)/12)<=3 THEN 'ABOVE_2_YR' ELSE\r\n"
						+ "CASE WHEN (MONTHS_BETWEEN(TRUNC(SYSDATE,'MM'),PAY_DT)/12)>3 AND (MONTHS_BETWEEN(TRUNC(SYSDATE,'MM'),PAY_DT)/12)<=4 THEN 'ABOVE_3_YR' ELSE\r\n"
						+ "CASE WHEN (MONTHS_BETWEEN(TRUNC(SYSDATE,'MM'),PAY_DT)/12)>4 AND (MONTHS_BETWEEN(TRUNC(SYSDATE,'MM'),PAY_DT)/12)<=5 THEN 'ABOVE_4_YR' ELSE\r\n"
						+ "CASE WHEN (MONTHS_BETWEEN(TRUNC(SYSDATE,'MM'),PAY_DT)/12)>5 THEN 'ABOVE_5_YR' ELSE 'BELOW' END END END END END END END AGE,\r\n"
						+ "nvl(CB_EC,0)+NVL(CB_FSA,0) CC,NVL(CB_ED,0) ED,NVL(CB_IED,0) EDI,NVL(CB_LPC,0)+NVL(CB_CCLPC,0) LPC,NVL(CB_OTH,0) OTH,\r\n"
						+ "NVL(CBTOT,0)+NVL(CB_CCLPC,0)+NVL(CB_OTH,0) CB FROM  LEDGER_HT_HIST,(SELECT UNIQUE MSCNO,CASE WHEN MDCLKWHSTAT_HT='3' THEN 'UD' ELSE 'LIV' END STAT FROM MTRDAT ),\r\n"
						+ "(SELECT USCNO SCNO,MAX(TRUNC(TO_DATE(MON_YEAR,'MON-YYYY'),'MM')) PAY_DT FROM LEDGER_HT_HIST  WHERE SUBSTR(USCNO,1,3) IN('GNT','VJA','ONG','CRD') AND  NVL(TOT_PAY,0)+NVL(CRJ,0)>0 GROUP BY USCNO\r\n"
						+ "UNION\r\n"
						+ "SELECT USCNO SCNO,MIN(TRUNC(TO_DATE(MON_YEAR,'MON-YYYY'),'MM')) PAY_DT FROM LEDGER_HT_HIST  WHERE SUBSTR(USCNO,1,3) IN('GNT','VJA','ONG','CRD') GROUP BY USCNO HAVING SUM(NVL(TOT_PAY,0)+NVL(CRJ,0))=0) \r\n"
						+ "WHERE USCNO=SCNO AND TRUNC(TO_DATE(MON_YEAR,'MON-YYYY'),'MM')=ADD_MONTHS(TRUNC(SYSDATE,'MM'),-1) and (NVL(CBTOT,0)+NVL(CB_CCLPC,0)+NVL(CB_OTH,0))>1 AND USCNO=MSCNO(+)) WHERE USCNO=CTUSCNO \r\n"
						+ "GROUP BY SUBSTR(CTUSCNO,1,3) ,AGE\r\n"
						+ " ORDER BY CIRCLE,SUBSTR(AGE,1,5),SUBSTR(AGE,7)  DESC)\r\n"
						+ "  GROUP BY CUBE (CIRCLE, AGE)\r\n"
						+ "  ORDER BY  case when CIRCLE = 'VJA' then '001' when CIRCLE = 'GNT' then '002' when CIRCLE = 'ONG' then '003'  when CIRCLE = 'CRD' then '009' else CIRCLE end,  case when AGE = 'ABOVE_5_YR' then '001'  when AGE = 'ABOVE_4_YR' then '002'   when AGE = 'ABOVE_3_YR' then '003'  when AGE = 'ABOVE_2_YR' then '004'  when AGE = 'ABOVE_1_YR' then '005'  when AGE = 'BELOW_1_YR_AND_ABV_6_MONTHS' then '006'  when AGE = 'UPTO_6_MONTHS' then '007' else AGE end  ";
				log.info(sql);
				return jdbcTemplate.queryForList(sql, new Object[] {});
			} catch (DataAccessException e) {
				e.printStackTrace();
				log.error(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}
		} else {
			try {
				String sql = "select * from ( select CIRCLE,nvl(AGE,'TOTAL') AGE, SUM(G_LIVE_NOS) G_LIVE_NOS,SUM(G_LIVE_CB) G_LIVE_CB,SUM(G_UDC_NOS) G_UDC_NOS,\r\n"
						+ "SUM(G_UDC_CB) G_UDC_CB,SUM(G_BS_NOS) G_BS_NOS,SUM(G_BS_CB) G_BS_CB,SUM(P_LIVE_NOS) P_LIVE_NOS,SUM(P_LIVE_CB) P_LIVE_CB,SUM(P_UDC_NOS) P_UDC_NOS,\r\n"
						+ "SUM(P_UDC_CB) P_UDC_CB,SUM(P_BS_NOS) P_BS_NOS,SUM(P_BS_CB) P_BS_CB,SUM(TOT_LIVE_NOS) TOT_LIVE_NOS,SUM(TOT_LIVE_CB) TOT_LIVE_CB,\r\n"
						+ "SUM(TOT_UDC_NOS) TOT_UDC_NOS,SUM(TOT_UDC_CB) TOT_UDC_CB,SUM(TOT_BS_NOS) TOT_BS_NOS,SUM(TOT_BS_CB) TOT_BS_CB,SUM(TOT_NOS) TOT_NOS,\r\n"
						+ "SUM(TOT_CB) TOT_CB from ( SELECT SUBSTR(CTUSCNO,1,3) CIRCLE,AGE,\r\n"
						+ "COUNT(CASE WHEN NVL(CTGOVT_PVT,'N')='Y' AND STATUS='LIVE' THEN USCNO END) G_LIVE_NOS,\r\n"
						+ "SUM(CASE WHEN NVL(CTGOVT_PVT,'N')='Y' AND STATUS='LIVE' THEN NVL(CB,0)  ELSE 0 END) G_LIVE_CB,\r\n"
						+ "COUNT(CASE WHEN NVL(CTGOVT_PVT,'N')='Y' AND STATUS='UDC' THEN USCNO END) G_UDC_NOS,\r\n"
						+ "SUM(CASE WHEN NVL(CTGOVT_PVT,'N')='Y' AND STATUS='UDC' THEN NVL(CB,0) ELSE 0  END) G_UDC_CB,\r\n"
						+ "COUNT(CASE WHEN NVL(CTGOVT_PVT,'N')='Y' AND STATUS='BS' THEN USCNO END) G_BS_NOS,\r\n"
						+ "SUM(CASE WHEN NVL(CTGOVT_PVT,'N')='Y' AND STATUS='BS' THEN NVL(CB,0) ELSE 0  END) G_BS_CB,\r\n"
						+ "COUNT(CASE WHEN NVL(CTGOVT_PVT,'N')='N' AND STATUS='LIVE' THEN USCNO END) P_LIVE_NOS,\r\n"
						+ "SUM(CASE WHEN NVL(CTGOVT_PVT,'N')='N' AND STATUS='LIVE' THEN NVL(CB,0) ELSE 0 END)   P_LIVE_CB,\r\n"
						+ "COUNT(CASE WHEN NVL(CTGOVT_PVT,'N')='N' AND STATUS='UDC' THEN USCNO END) P_UDC_NOS,\r\n"
						+ "SUM(CASE WHEN NVL(CTGOVT_PVT,'N')='N' AND STATUS='UDC' THEN NVL(CB,0) ELSE 0 END) P_UDC_CB,\r\n"
						+ "COUNT(CASE WHEN NVL(CTGOVT_PVT,'N')='N' AND STATUS='BS' THEN USCNO END) P_BS_NOS,\r\n"
						+ "SUM(CASE WHEN NVL(CTGOVT_PVT,'N')='N' AND STATUS='BS' THEN NVL(CB,0)  ELSE 0 END) P_BS_CB,\r\n"
						+ "COUNT(CASE WHEN STATUS='LIVE' THEN USCNO END) TOT_LIVE_NOS,\r\n"
						+ "SUM(CASE WHEN STATUS='LIVE' THEN NVL(CB,0) ELSE 0 END)   TOT_LIVE_CB,\r\n"
						+ "COUNT(CASE WHEN STATUS='UDC' THEN USCNO END) TOT_UDC_NOS,\r\n"
						+ "SUM(CASE WHEN STATUS='UDC' THEN NVL(CB,0) ELSE 0 END) TOT_UDC_CB,\r\n"
						+ "COUNT(CASE WHEN STATUS='BS' THEN USCNO END) TOT_BS_NOS,\r\n"
						+ "SUM(CASE WHEN STATUS='BS' THEN NVL(CB,0)  ELSE 0 END) TOT_BS_CB,\r\n"
						+ "COUNT(USCNO) TOT_NOS,\r\n" + "SUM(NVL(CB,0))   TOT_CB\r\n" + "FROM CONS,\r\n"
						+ "(SELECT USCNO,CASE WHEN STAT='UD' THEN 'UDC' WHEN STAT IS NULL THEN 'BS' WHEN STAT='LIV' THEN 'LIVE' END STATUS,PAY_DT,mon_year,MONTHS_BETWEEN(TRUNC(SYSDATE,'MM'),PAY_DT)/12 LAST_PAID_AGE,\r\n"
						+ "CASE WHEN (MONTHS_BETWEEN(TRUNC(SYSDATE,'MM'),PAY_DT)/12)>0 AND (MONTHS_BETWEEN(TRUNC(SYSDATE,'MM'),PAY_DT)/12)<=0.5 THEN 'UPTO_6_MONTHS' ELSE \r\n"
						+ "CASE WHEN (MONTHS_BETWEEN(TRUNC(SYSDATE,'MM'),PAY_DT)/12)>0.5 AND (MONTHS_BETWEEN(TRUNC(SYSDATE,'MM'),PAY_DT)/12)<=1 THEN 'BELOW_1_YR_AND_ABV_6_MONTHS' ELSE \r\n"
						+ "CASE WHEN (MONTHS_BETWEEN(TRUNC(SYSDATE,'MM'),PAY_DT)/12)>1 AND (MONTHS_BETWEEN(TRUNC(SYSDATE,'MM'),PAY_DT)/12)<=2 THEN 'ABOVE_1_YR' ELSE \r\n"
						+ "CASE WHEN (MONTHS_BETWEEN(TRUNC(SYSDATE,'MM'),PAY_DT)/12)>2 AND (MONTHS_BETWEEN(TRUNC(SYSDATE,'MM'),PAY_DT)/12)<=3 THEN 'ABOVE_2_YR' ELSE\r\n"
						+ "CASE WHEN (MONTHS_BETWEEN(TRUNC(SYSDATE,'MM'),PAY_DT)/12)>3 AND (MONTHS_BETWEEN(TRUNC(SYSDATE,'MM'),PAY_DT)/12)<=4 THEN 'ABOVE_3_YR' ELSE\r\n"
						+ "CASE WHEN (MONTHS_BETWEEN(TRUNC(SYSDATE,'MM'),PAY_DT)/12)>4 AND (MONTHS_BETWEEN(TRUNC(SYSDATE,'MM'),PAY_DT)/12)<=5 THEN 'ABOVE_4_YR' ELSE\r\n"
						+ "CASE WHEN (MONTHS_BETWEEN(TRUNC(SYSDATE,'MM'),PAY_DT)/12)>5 THEN 'ABOVE_5_YR' ELSE 'BELOW' END END END END END END END AGE,\r\n"
						+ "nvl(CB_EC,0)+NVL(CB_FSA,0) CC,NVL(CB_ED,0) ED,NVL(CB_IED,0) EDI,NVL(CB_LPC,0)+NVL(CB_CCLPC,0) LPC,NVL(CB_OTH,0) OTH,\r\n"
						+ "NVL(CBTOT,0)+NVL(CB_CCLPC,0)+NVL(CB_OTH,0) CB FROM  LEDGER_HT_HIST,(SELECT UNIQUE MSCNO,CASE WHEN MDCLKWHSTAT_HT='3' THEN 'UD' ELSE 'LIV' END STAT FROM MTRDAT ),\r\n"
						+ "(SELECT USCNO SCNO,MAX(TRUNC(TO_DATE(MON_YEAR,'MON-YYYY'),'MM')) PAY_DT FROM LEDGER_HT_HIST  WHERE SUBSTR(USCNO,1,3) IN('GNT','VJA','ONG','CRD') AND  NVL(TOT_PAY,0)+NVL(CRJ,0)>0 GROUP BY USCNO\r\n"
						+ "UNION\r\n"
						+ "SELECT USCNO SCNO,MIN(TRUNC(TO_DATE(MON_YEAR,'MON-YYYY'),'MM')) PAY_DT FROM LEDGER_HT_HIST  WHERE SUBSTR(USCNO,1,3) IN('GNT','VJA','ONG','CRD') GROUP BY USCNO HAVING SUM(NVL(TOT_PAY,0)+NVL(CRJ,0))=0) \r\n"
						+ "WHERE USCNO=SCNO AND TRUNC(TO_DATE(MON_YEAR,'MON-YYYY'),'MM')=ADD_MONTHS(TRUNC(SYSDATE,'MM'),-1) and (NVL(CBTOT,0)+NVL(CB_CCLPC,0)+NVL(CB_OTH,0))>1 AND USCNO=MSCNO(+)) WHERE USCNO=CTUSCNO \r\n"
						+ "GROUP BY SUBSTR(CTUSCNO,1,3) ,AGE\r\n"
						+ " ORDER BY CIRCLE,SUBSTR(AGE,1,5),SUBSTR(AGE,7)  DESC)\r\n"
						+ "  GROUP BY CUBE (CIRCLE, AGE)\r\n"
						+ "  ORDER BY  case when CIRCLE = 'VJA' then '001' when CIRCLE = 'GNT' then '002' when CIRCLE = 'ONG' then '003'  when CIRCLE = 'CRD' then '009' else CIRCLE end,  case when AGE = 'ABOVE_5_YR' then '001'  when AGE = 'ABOVE_4_YR' then '002'   when AGE = 'ABOVE_3_YR' then '003'  when AGE = 'ABOVE_2_YR' then '004'  when AGE = 'ABOVE_1_YR' then '005'  when AGE = 'BELOW_1_YR_AND_ABV_6_MONTHS' then '006'  when AGE = 'UPTO_6_MONTHS' then '007' else AGE end "
						+ "  ) where CIRCLE is not null and CIRCLE = ?";
				log.info(sql);
				return jdbcTemplate.queryForList(sql, new Object[] { circle });
			} catch (DataAccessException e) {
				e.printStackTrace();
				log.error(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}

		}
	}

	public List<Map<String, Object>> getHtDCBCollectionSplitMonthlyAbstract(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		String levi_month = "01-" + request.getParameter("fmonth") + "-" + request.getParameter("fyear");
		if (circle.equalsIgnoreCase("ALL")) {
			try {
				String sql = "select  NVL(T_LDT,'TOTAL')LDT,NVL(CIRNAME,'TOTAL') CIRNAME,T_LDT,SUM(TOB) TOB,SUM(DEMAND) DEMAND,SUM(COLL_ARREAR) COLL_ARREAR,SUM(COLL_DEMAND)COLL_DEMAND,SUM(COLLECTION) COLLECTION,SUM(CB) CB,sum(SD) SD from \r\n"
						+ "(SELECT  UNIQUE TO_CHAR(LDT,'MON-YYYY') T_LDT,SUBSTR(CTUSCNO,1,3) CIRNAME,SUM(NVL(OB,0)) TOB,SUM(NVL(DEM,0)+NVL(DR_AMT,0)) DEMAND,\r\n"
						+ "SUM(CASE WHEN NVL(OB,0)>0 THEN CASE WHEN NVL(OB,0)>(NVL(PAY,0)+NVL(CR_AMT,0)) THEN (NVL(PAY,0)+NVL(CR_AMT,0)) ELSE NVL(OB,0) END END) COLL_ARREAR,\r\n"
						+ "SUM(CASE WHEN NVL(OB,0)>0 THEN CASE WHEN NVL(OB,0)<(NVL(PAY,0)+NVL(CR_AMT,0)) THEN (NVL(PAY,0)+NVL(CR_AMT,0)-NVL(OB,0)) END ELSE (NVL(PAY,0)+NVL(CR_AMT,0)) END )COLL_DEMAND,\r\n"
						+ "SUM(NVL(PAY,0)+NVL(CR_AMT,0)) COLLECTION, SUM(NVL(CB,0)) CB , SUM(NVL(CB_SD,0)) SD \r\n"
						+ "FROM CONS,\r\n"
						+ "(SELECT USCNO,TRUNC(TO_DATE(MON_YEAR,'MON-YYYY'),'MM') LDT,(Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)) OB,(NVL(TOT_PAY,0)) PAY,(NVL(CMD,0)+NVL(CCLPC,0)) DEM,NVL(CBTOT,0)+NVL(CB_CCLPC,0)+NVL(CB_OTH,0) CB ,NVL(CB_SD,0) CB_SD\r\n"
						+ "FROM LEDGER_HT_HIST WHERE TRUNC(TO_DATE(MON_YEAR,'MON-YYYY'),'MM')>=? AND SUBSTR(USCNO,1,3) IN('GNT','VJA','ONG','CRD')) L,\r\n"
						+ "(select uscno,TRUNC(rjdt,'MM') RDT,SUM(CASE WHEN RJTYPE='DR' THEN (nvl(ENGCHG,0)+nvl(THEFT,0)+nvl(OTHCHG,0)+nvl(CUSTCHG,0)+nvl(DEMCHG,0)+nvl(ED,0)+nvl(LPC,0)+nvl(EDI,0)+nvl(FSA,0)+NVL(CC_LPC,0)+NVL(CC_OTH,0)) END) DR_AMT,\r\n"
						+ "SUM(CASE WHEN RJTYPE='CR'THEN (nvl(ENGCHG,0)+nvl(THEFT,0)+nvl(OTHCHG,0)+nvl(CUSTCHG,0)+nvl(DEMCHG,0)+nvl(ED,0)+nvl(LPC,0)+nvl(EDI,0)+nvl(FSA,0)+NVL(CC_LPC,0)+NVL(CC_OTH,0)) END) CR_AMT \r\n"
						+ "from JOURNAL_HIST WHERE TRUNC(rjdt,'MM')>=?  and SUBSTR(USCNO,1,3) IN('GNT','VJA','ONG','CRD') AND TRIM(STATUS) NOT IN ('X','E') GROUP BY USCNO,TRUNC(rjdt,'MM')) RJ\r\n"
						+ "WHERE CTUSCNO=L.USCNO AND L.USCNO=RJ.USCNO(+) AND LDT=RDT(+) GROUP BY LDT,SUBSTR(CTUSCNO,1,3) )\r\n"
						+ "GROUP BY CUBE(T_LDT,CIRNAME)\r\n"
						+ "ORDER BY TO_DATE(T_LDT,'MON-YYYY'),case when CIRNAME = 'VJA' then '001' when CIRNAME = 'GNT' then '002' when CIRNAME = 'ONG' then '003'  when CIRNAME = 'CRD' then '009' else CIRNAME end";
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
				String sql = "SELECT  UNIQUE TO_CHAR(LDT,'MON-YYYY') LDT,SUBSTR(CTUSCNO,1,3) CIRNAME,SUM(NVL(OB,0)) TOB,SUM(NVL(DEM,0)+NVL(DR_AMT,0)) DEMAND,\r\n"
						+ "SUM(CASE WHEN NVL(OB,0)>0 THEN CASE WHEN NVL(OB,0)>(NVL(PAY,0)+NVL(CR_AMT,0)) THEN (NVL(PAY,0)+NVL(CR_AMT,0)) ELSE NVL(OB,0) END END) COLL_ARREAR,\r\n"
						+ "SUM(CASE WHEN NVL(OB,0)>0 THEN CASE WHEN NVL(OB,0)<(NVL(PAY,0)+NVL(CR_AMT,0)) THEN (NVL(PAY,0)+NVL(CR_AMT,0)-NVL(OB,0)) END ELSE (NVL(PAY,0)+NVL(CR_AMT,0)) END )COLL_DEMAND,\r\n"
						+ "SUM(NVL(PAY,0)+NVL(CR_AMT,0)) COLLECTION, SUM(NVL(CB,0)) CB, SUM(NVL(CB_SD,0)) SD \r\n"
						+ "FROM CONS,\r\n"
						+ "(SELECT USCNO,TRUNC(TO_DATE(MON_YEAR,'MON-YYYY'),'MM') LDT,(Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)) OB,(NVL(TOT_PAY,0)) PAY,(NVL(CMD,0)+NVL(CCLPC,0)) DEM,NVL(CBTOT,0)+NVL(CB_CCLPC,0)+NVL(CB_OTH,0) CB ,NVL(CB_SD,0) CB_SD \r\n"
						+ "FROM LEDGER_HT_HIST WHERE TRUNC(TO_DATE(MON_YEAR,'MON-YYYY'),'MM')>=? AND SUBSTR(USCNO,1,3) IN('GNT','VJA','ONG','CRD')) L,\r\n"
						+ "(select uscno,TRUNC(rjdt,'MM') RDT,SUM(CASE WHEN RJTYPE='DR' THEN (nvl(ENGCHG,0)+nvl(THEFT,0)+nvl(OTHCHG,0)+nvl(CUSTCHG,0)+nvl(DEMCHG,0)+nvl(ED,0)+nvl(LPC,0)+nvl(EDI,0)+nvl(FSA,0)+NVL(CC_LPC,0)+NVL(CC_OTH,0)) END) DR_AMT,\r\n"
						+ "SUM(CASE WHEN RJTYPE='CR'THEN (nvl(ENGCHG,0)+nvl(THEFT,0)+nvl(OTHCHG,0)+nvl(CUSTCHG,0)+nvl(DEMCHG,0)+nvl(ED,0)+nvl(LPC,0)+nvl(EDI,0)+nvl(FSA,0)+NVL(CC_LPC,0)+NVL(CC_OTH,0)) END) CR_AMT \r\n"
						+ "from JOURNAL_HIST WHERE TRUNC(rjdt,'MM')>=?  and SUBSTR(USCNO,1,3) IN('GNT','VJA','ONG','CRD') AND TRIM(STATUS) NOT IN ('X','E') GROUP BY USCNO,TRUNC(rjdt,'MM')) RJ\r\n"
						+ "WHERE CTUSCNO=L.USCNO AND L.USCNO=RJ.USCNO(+) AND LDT=RDT(+) AND SUBSTR(CTUSCNO,1,3)=? GROUP BY LDT,SUBSTR(CTUSCNO,1,3)\r\n"
						+ "ORDER BY TO_DATE(LDT,'MON-YYYY')";
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

	public List<Map<String, Object>> getHtDCBCollectionMonthlyAbstract(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		String levi_month = "01-" + request.getParameter("fmonth") + "-" + request.getParameter("fyear");
		if (circle.equalsIgnoreCase("ALL")) {
			try {
				String sql = "SELECT * FROM ( select  T_LDT LDT ,NVL(CIRNAME,'TOTAL') CIRNAME,T_LDT,ROUND(SUM(TOB)) TOB,ROUND(SUM(DEMAND)) DEMAND,ROUND(sum(DR_DEMAND))DR_DEMAND ,ROUND(SUM(COLL_ARREAR)) COLL_ARREAR,ROUND(SUM(COLL_DEMAND))COLL_DEMAND,ROUND(SUM(COLLECTION)) COLLECTION,ROUND(SUM(CR_COLLECTION))  CR_COLLECTION,ROUND(SUM(CB)) CB,ROUND(sum(SD)) SD from \r\n"
						+ "(SELECT  UNIQUE TO_CHAR(LDT,'MON-YYYY') T_LDT,SUBSTR(CTUSCNO,1,3) CIRNAME,SUM(NVL(OB,0)) TOB,SUM(NVL(DEM,0)) DEMAND, SUM(NVL(DR_AMT,0)) DR_DEMAND, \r\n"
						+ "SUM(CASE WHEN NVL(OB,0)>0 THEN CASE WHEN NVL(OB,0)>(NVL(PAY,0)+NVL(CR_AMT,0)) THEN (NVL(PAY,0)+NVL(CR_AMT,0)) ELSE NVL(OB,0) END END) COLL_ARREAR,\r\n"
						+ "SUM(CASE WHEN NVL(OB,0)>0 THEN CASE WHEN NVL(OB,0)<(NVL(PAY,0)+NVL(CR_AMT,0)) THEN (NVL(PAY,0)+NVL(CR_AMT,0)-NVL(OB,0)) END ELSE (NVL(PAY,0)+NVL(CR_AMT,0)) END )COLL_DEMAND,\r\n"
						+ "SUM(NVL(PAY,0)) COLLECTION,SUM(NVL(CR_AMT,0)) CR_COLLECTION, SUM(NVL(CB,0)) CB , SUM(NVL(CB_SD,0)) SD \r\n"
						+ "FROM CONS,\r\n"
						+ "(SELECT USCNO,TRUNC(TO_DATE(MON_YEAR,'MON-YYYY'),'MM') LDT,(Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)) OB,(NVL(TOT_PAY,0)) PAY,(NVL(CMD,0)+NVL(CCLPC,0)) DEM,NVL(CBTOT,0)+NVL(CB_CCLPC,0)+NVL(CB_OTH,0) CB ,NVL(CB_SD,0) CB_SD\r\n"
						+ "FROM LEDGER_HT_HIST WHERE TRUNC(TO_DATE(MON_YEAR,'MON-YYYY'),'MM')>=? AND SUBSTR(USCNO,1,3) IN('GNT','VJA','ONG','CRD')) L,\r\n"
						+ "(select uscno,TRUNC(rjdt,'MM') RDT,SUM(CASE WHEN RJTYPE='DR' THEN (nvl(ENGCHG,0)+nvl(THEFT,0)+nvl(OTHCHG,0)+nvl(CUSTCHG,0)+nvl(DEMCHG,0)+nvl(ED,0)+nvl(LPC,0)+nvl(EDI,0)+nvl(FSA,0)+NVL(CC_LPC,0)+NVL(CC_OTH,0)) END) DR_AMT,\r\n"
						+ "SUM(CASE WHEN RJTYPE='CR'THEN (nvl(ENGCHG,0)+nvl(THEFT,0)+nvl(OTHCHG,0)+nvl(CUSTCHG,0)+nvl(DEMCHG,0)+nvl(ED,0)+nvl(LPC,0)+nvl(EDI,0)+nvl(FSA,0)+NVL(CC_LPC,0)+NVL(CC_OTH,0)) END) CR_AMT \r\n"
						+ "from JOURNAL_HIST WHERE TRUNC(rjdt,'MM')>=?  and SUBSTR(USCNO,1,3) IN('GNT','VJA','ONG','CRD') AND TRIM(STATUS) NOT IN ('X','E') GROUP BY USCNO,TRUNC(rjdt,'MM')) RJ\r\n"
						+ "WHERE CTUSCNO=L.USCNO AND L.USCNO=RJ.USCNO(+) AND LDT=RDT(+) GROUP BY LDT,SUBSTR(CTUSCNO,1,3) )\r\n"
						+ "GROUP BY CUBE(T_LDT,CIRNAME)\r\n"
						+ "ORDER BY TO_DATE(T_LDT,'MON-YYYY'),case when CIRNAME = 'VJA' then '001' when CIRNAME = 'GNT' then '002' when CIRNAME = 'ONG' then '003'  when CIRNAME = 'CRD' then '009' else CIRNAME end) WHERE LDT IS NOT NULL ";
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
				String sql = "SELECT  UNIQUE TO_CHAR(LDT,'MON-YYYY') LDT,SUBSTR(CTUSCNO,1,3) CIRNAME,ROUND(SUM(NVL(OB,0))) TOB,ROUND(SUM(NVL(DEM,0))) DEMAND,ROUND(SUM(NVL(DR_AMT,0)))DR_DEMAND, \r\n"
						+ "SUM(CASE WHEN NVL(OB,0)>0 THEN CASE WHEN NVL(OB,0)>(NVL(PAY,0)+NVL(CR_AMT,0)) THEN (NVL(PAY,0)+NVL(CR_AMT,0)) ELSE NVL(OB,0) END END) COLL_ARREAR,\r\n"
						+ "SUM(CASE WHEN NVL(OB,0)>0 THEN CASE WHEN NVL(OB,0)<(NVL(PAY,0)+NVL(CR_AMT,0)) THEN (NVL(PAY,0)+NVL(CR_AMT,0)-NVL(OB,0)) END ELSE (NVL(PAY,0)+NVL(CR_AMT,0)) END )COLL_DEMAND,\r\n"
						+ "ROUND(SUM(NVL(PAY,0))) COLLECTION,ROUND(SUM(NVL(CR_AMT,0))) CR_COLLECTION, ROUND(SUM(NVL(CB,0))) CB, ROUND(SUM(NVL(CB_SD,0))) SD \r\n"
						+ "FROM CONS,\r\n"
						+ "(SELECT USCNO,TRUNC(TO_DATE(MON_YEAR,'MON-YYYY'),'MM') LDT,(Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)) OB,(NVL(TOT_PAY,0)) PAY,(NVL(CMD,0)+NVL(CCLPC,0)) DEM,NVL(CBTOT,0)+NVL(CB_CCLPC,0)+NVL(CB_OTH,0) CB ,NVL(CB_SD,0) CB_SD \r\n"
						+ "FROM LEDGER_HT_HIST WHERE TRUNC(TO_DATE(MON_YEAR,'MON-YYYY'),'MM')>=? AND SUBSTR(USCNO,1,3) IN('GNT','VJA','ONG','CRD')) L,\r\n"
						+ "(select uscno,TRUNC(rjdt,'MM') RDT,SUM(CASE WHEN RJTYPE='DR' THEN (nvl(ENGCHG,0)+nvl(THEFT,0)+nvl(OTHCHG,0)+nvl(CUSTCHG,0)+nvl(DEMCHG,0)+nvl(ED,0)+nvl(LPC,0)+nvl(EDI,0)+nvl(FSA,0)+NVL(CC_LPC,0)+NVL(CC_OTH,0)) END) DR_AMT,\r\n"
						+ "SUM(CASE WHEN RJTYPE='CR'THEN (nvl(ENGCHG,0)+nvl(THEFT,0)+nvl(OTHCHG,0)+nvl(CUSTCHG,0)+nvl(DEMCHG,0)+nvl(ED,0)+nvl(LPC,0)+nvl(EDI,0)+nvl(FSA,0)+NVL(CC_LPC,0)+NVL(CC_OTH,0)) END) CR_AMT \r\n"
						+ "from JOURNAL_HIST WHERE TRUNC(rjdt,'MM')>=?  and SUBSTR(USCNO,1,3) IN('GNT','VJA','ONG','CRD') AND TRIM(STATUS) NOT IN ('X','E') GROUP BY USCNO,TRUNC(rjdt,'MM')) RJ\r\n"
						+ "WHERE CTUSCNO=L.USCNO AND L.USCNO=RJ.USCNO(+) AND LDT=RDT(+) AND SUBSTR(CTUSCNO,1,3)=? GROUP BY LDT,SUBSTR(CTUSCNO,1,3)\r\n"
						+ "ORDER BY TO_DATE(LDT,'MON-YYYY')";
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

	public List<Map<String, Object>> getISCAbstract(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		String year = request.getParameter("year");

		if (circle.equalsIgnoreCase("ALL")) {
			try {
				String sql = " select    nvl(CIRCLE,'APCPDCL') CIRCLE,nvl(TYPE,'TOTAL') TYPE, nvl(CIRCLE,'APCPDCL')||nvl(TYPE,'TOTAL') CIR_TYPE ,nvl(CTCAT,'TOTAL') CTCAT,count(*) NOS,SUM(OB_SD) OB_SD,SUM(PAID_ACD) PAID_ACD,SUM(RJ_SD)RJ_SD,SUM(CB_SD) CB_SD,SUM(round(TOT_ISD)) TOT_ISD,SUM(round(TDS_AMT))TDS_AMT,SUM(NET_ISD) NET_ISD from (                        \r\n"
						+ "SELECT substr(ISCNO,1,3) CIRCLE ,DECODE(CTGOVT_PVT,'Y','GOVT','NON-GOVT') TYPE,CTCAT,NVL(OB_SD,0)OB_SD,NVL(PAID_ACD,0)PAID_ACD,NVL(RJ_SD,0)RJ_SD,NVL(CB_SD,0)CB_SD, NVL(TOT_ISD,0)TOT_ISD ,NVL(TDS_AMT,0)TDS_AMT,NVL(NET_ISD,0)NET_ISD\r\n"
						+ "FROM SD_LEDGER_TEMP,CONS\r\n"
						+ "WHERE CTUSCNO=ISCNO AND FIN_YEAR=? AND CTSTATUS!=0  AND LEVI_FLG='P' ORDER BY CTCAT)\r\n"
						+ "GROUP BY \r\n" + "    CUBE(CIRCLE,TYPE,CTCAT)\r\n" + "ORDER BY \r\n"
						+ "case when CIRCLE = 'VJA' then '001' when CIRCLE = 'GNT' then '002' when CIRCLE = 'ONG' then '003'  when CIRCLE = 'CRD' then '009' else CIRCLE end,case when TYPE='GOVT' then '001' when TYPE = 'NON-GOVT' then '002' else TYPE end,CTCAT";
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

				String sql = " select * from ( select  CIRCLE,nvl(TYPE,'TOTAL') TYPE,nvl(CIRCLE,'APCPDCL')||nvl(TYPE,'TOTAL') CIR_TYPE  ,nvl(CTCAT,'TOTAL') CTCAT,count(*) NOS,SUM(OB_SD) OB_SD,SUM(PAID_ACD) PAID_ACD,SUM(RJ_SD)RJ_SD,SUM(CB_SD) CB_SD,SUM(round(TOT_ISD)) TOT_ISD,SUM(round(TDS_AMT))TDS_AMT,SUM(NET_ISD) NET_ISD from (                        \r\n"
						+ " SELECT substr(ISCNO,1,3) CIRCLE ,DECODE(CTGOVT_PVT,'Y','GOVT','NON-GOVT') TYPE,CTCAT,NVL(OB_SD,0)OB_SD,NVL(PAID_ACD,0)PAID_ACD,NVL(RJ_SD,0)RJ_SD,NVL(CB_SD,0)CB_SD, NVL(TOT_ISD,0)TOT_ISD ,NVL(TDS_AMT,0)TDS_AMT,NVL(NET_ISD,0)NET_ISD\r\n"
						+ " FROM SD_LEDGER_TEMP,CONS\r\n"
						+ " WHERE CTUSCNO=ISCNO AND FIN_YEAR=? AND  substr(ISCNO,1,3) = ? AND CTSTATUS!=0  AND LEVI_FLG='P'  ORDER BY CTCAT)\r\n"
						+ " GROUP BY \r\n" + " CUBE(CIRCLE,TYPE,CTCAT)\r\n" + " ORDER BY \r\n"
						+ " case when CIRCLE = 'VJA' then '001' when CIRCLE = 'GNT' then '002' when CIRCLE = 'ONG' then '003'  when CIRCLE = 'CRD' then '009' else CIRCLE end,case when TYPE='GOVT' then '001' when TYPE = 'NON-GOVT' then '002' else TYPE end,CTCAT ) where  circle is not null ";
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

	public List<Map<String, Object>> getCollectionAbstract(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		String monthYear = request.getParameter("month") + "-" + request.getParameter("year");

		if (circle.equalsIgnoreCase("ALL")) {
			try {
				String sql = "select    nvl(CIRCLE,'APCPDCL') CIRCLE,nvl(TYPE,'TOTAL') TYPE, nvl(CIRCLE,'APCPDCL')||nvl(TYPE,'TOTAL') CIR_TYPE ,nvl(CTCAT,'TOTAL') CTCAT,SUM(NOS) NOS,sum(COLLECTION) COLLECTION from (\r\n"
						+ "select SUBSTR(CTUSCNO,1,3)CIRCLE,DECODE(CTGOVT_PVT,'Y','GOVT','NON-GOVT') TYPE,CTCAT,SUM(NVL(PAY,0)) COLLECTION ,count(*) NOS from cons,spdclmaster,\r\n"
						+ "(SELECT USCNO SCNO,SUM(NVL(PCMD,0)) PAY FROM (select USCNO,PCMD,PAY_DATE,pay_sta_flg from pay_ht union all select USCNO,PCMD,PAY_DATE,pay_sta_flg  from pay_ht_hist) WHERE TO_DATE(PAY_DATE) between TRUNC(to_date(?,'MON-YYYY'))\r\n"
						+ "and LAST_DAY(TRUNC(to_date(?,'MON-YYYY')) ) AND pay_sta_flg='C' AND SUBSTR(USCNO,1,3) IN('GNT','VJA','ONG','CRD') GROUP BY USCNO) P\r\n"
						+ "where CTUSCNO=SCNO  AND SUBSTR(CTSECCD,-5)=SECCD \r\n"
						+ "group by SUBSTR(CTUSCNO,1,3),DECODE(CTGOVT_PVT,'Y','GOVT','NON-GOVT'),CTCAT)\r\n"
						+ "GROUP BY \r\n" + "    CUBE(CIRCLE,TYPE,CTCAT)\r\n" + "ORDER BY \r\n"
						+ "case when CIRCLE = 'VJA' then '001' when CIRCLE = 'GNT' then '002' when CIRCLE = 'ONG' then '003'  when CIRCLE = 'CRD' then '009' else CIRCLE end,case when TYPE='GOVT' then '001' when TYPE = 'NON-GOVT' then '002' else TYPE end,CTCAT\r\n"
						+ "";
				log.info(sql);
				return jdbcTemplate.queryForList(sql, new Object[] { monthYear, monthYear });
			} catch (DataAccessException e) {
				e.printStackTrace();
				log.error(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}
		} else {
			try {

				String sql = "select * from ( select   CIRCLE,nvl(TYPE,'TOTAL') TYPE, nvl(CIRCLE,'APCPDCL')||nvl(TYPE,'TOTAL') CIR_TYPE ,nvl(CTCAT,'TOTAL') CTCAT,count(*) NOS,sum(COLLECTION) COLLECTION from (\r\n"
						+ "select SUBSTR(CTUSCNO,1,3)CIRCLE,DECODE(CTGOVT_PVT,'Y','GOVT','NON-GOVT') TYPE,CTCAT,SUM(NVL(PAY,0)) COLLECTION,,count(*) NOS from cons,spdclmaster,\r\n"
						+ "(SELECT USCNO SCNO,SUM(NVL(PCMD,0)) PAY FROM (select USCNO,PCMD,PAY_DATE,pay_sta_flg from pay_ht union all select USCNO,PCMD,PAY_DATE,pay_sta_flg  from pay_ht_hist) WHERE TO_DATE(PAY_DATE) between TRUNC(to_date(?,'MON-YYYY'))\r\n"
						+ "and LAST_DAY(TRUNC(to_date(?,'MON-YYYY')) ) AND pay_sta_flg='C' AND SUBSTR(USCNO,1,3) IN('GNT','VJA','ONG','CRD') GROUP BY USCNO) P\r\n"
						+ "where CTUSCNO=SCNO  AND SUBSTR(CTSECCD,-5)=SECCD AND SUBSTR(CTUSCNO,1,3)=? \r\n"
						+ "group by SUBSTR(CTUSCNO,1,3),DECODE(CTGOVT_PVT,'Y','GOVT','NON-GOVT'),CTCAT)\r\n"
						+ "GROUP BY \r\n" + "    CUBE(CIRCLE,TYPE,CTCAT)\r\n" + "ORDER BY \r\n"
						+ "case when CIRCLE = 'VJA' then '001' when CIRCLE = 'GNT' then '002' when CIRCLE = 'ONG' then '003'  when CIRCLE = 'CRD' then '009' else CIRCLE end,case when TYPE='GOVT' then '001' when TYPE = 'NON-GOVT' then '002' else TYPE end,CTCAT)"
						+ " where  circle is not null \r\n";
				log.info(sql);
				return jdbcTemplate.queryForList(sql, new Object[] { monthYear, monthYear, circle });
			} catch (DataAccessException e) {
				e.printStackTrace();
				log.error(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}

		}
	}

	public List<Map<String, Object>> getDCDivisionWiseReport(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		String year = request.getParameter("year");

		if (circle.equalsIgnoreCase("ALL")) {
			try {
				String sql = "select nvl(TYPE,'TOTAL') TYPE, nvl(CIRNAME,'APCPDCL') CIRNAME,nvl(TYPE,'APCPDCL')||nvl(CIRNAME,'TOTAL') CIR_TYPE , nvl(DIVNAME,'TOTAL')DIVNAME,SUM(NOS) NOS,SUM(CURR_DEMAND) CURR_DEMAND,SUM(ARREAR_AMOUNT) ARREAR_AMOUNT, SUM(TOTAL) TOTAL ,\r\n"
						+ "SUM(NOS_COLLECTED) NOS_COLLECTED ,SUM(CURR_DEM_COLLECTED) CURR_DEM_COLLECTED,SUM(COLLECTED_AGA_CMD) COLLECTED_AGA_CMD,\r\n"
						+ "SUM(ARREAR_COLLECTED) ARREAR_COLLECTED,SUM(ARR_C_AGA_CMD) ARR_C_AGA_CMD ,SUM(COLL_AGA_TOT_AMT) COLL_AGA_TOT_AMT, SUM(TOTAL_DEUES) TOTAL_DEUES  from \r\n"
						+ "(\r\n"
						+ "select TYPE,CIRNAME,DIVNAME,NOS,round(CURR_DEMAND/100000,2) CURR_DEMAND ,round(ARREAR_AMOUNT/100000,2) ARREAR_AMOUNT,round(TOTAL/100000,2) TOTAL,\r\n"
						+ "NOS_COLLECTED,round(CURR_DEM_COLLECTED/100000,2) CURR_DEM_COLLECTED , round((CURR_DEM_COLLECTED/CURR_DEMAND)*100,2) COLLECTED_AGA_CMD,\r\n"
						+ "round(ARREAR_COLLECTED/100000,2) ARREAR_COLLECTED,round((ARREAR_COLLECTED/ARREAR_AMOUNT)*100,2) ARR_C_AGA_CMD, round(((CURR_DEM_COLLECTED+ARREAR_COLLECTED)/TOTAL)*100,2) COLL_AGA_TOT_AMT,\r\n"
						+ "round((TOTAL -(CURR_DEM_COLLECTED+ARREAR_COLLECTED))/100000,2) TOTAL_DEUES from (\r\n"
						+ "SELECT \r\n"
						+ " CASE WHEN NVL(CTGOVT_PVT,'N')='Y'  THEN 'GOVT' ELSE 'PVT' END TYPE, CIRNAME,DIVNAME,COUNT(SCNO) NOS,SUM(NVL(DEM,0)) CURR_DEMAND,SUM(NVL(TOB,0)) ARREAR_AMOUNT,SUM(NVL(TOT,0)) TOTAL,\r\n"
						+ "SUM(CASE WHEN NVL(COLLECTION,0)>0 THEN 1 ELSE 0 END) NOS_COLLECTED,SUM(CASE WHEN NVL(TOB,0)>0 THEN   CASE WHEN (NVL(TOB,0)-NVL(COLLECTION,0))>0 THEN 0 ELSE (NVL(COLLECTION,0)-NVL(TOB,0)) END ELSE NVL(COLLECTION,0) END) CURR_DEM_COLLECTED, \r\n"
						+ "SUM(CASE WHEN NVL(TOB,0)>0 THEN CASE WHEN NVL(TOB,0)>NVL(COLLECTION,0) THEN NVL(COLLECTION,0) ELSE NVL(TOB,0) END END) ARREAR_COLLECTED,\r\n"
						+ "SUM(NVL( TCB,0)) CB FROM CONS,SPDCLMASTER, \r\n"
						+ " (SELECT  SCNO,SUM(NVL(OB,0)) TOB,SUM(NVL(DEM,0)+NVL(DR_AMT,0)) DEM,SUM(NVL(OB,0)+NVL(DEM,0)+NVL(DR_AMT,0)) TOT,\r\n"
						+ "SUM(NVL(PAY,0)+NVL(CR_AMT,0)) COLLECTION,SUM(NVL(OB,0)+(NVL(DEM,0)+NVL(DR_AMT,0))-(NVL(PAY,0)+NVL(CR_AMT,0))) TCB\r\n"
						+ "FROM \r\n"
						+ "(SELECT BTSCNO SCNO,0 OB,SUM(NVL(BTCURDEM,0)+NVL(BTCOURT_LPC,0)) DEM,0 DR_AMT,0 CR_AMT,0 PAY FROM BILL WHERE BTBLDT between TRUNC(SYSDATE,'MONTH') and TO_DATE(sysdate - 1) AND SUBSTR(BTSCNO,1,3) IN('GNT','VJA','ONG','CRD') GROUP BY BTSCNO\r\n"
						+ "UNION\r\n"
						+ "SELECT USCNO SCNO,NVL(CBTOT,0)+NVL(CB_CCLPC,0)+NVL(CB_OTH,0) OB,0 DEM,0 DR_AMT,0 CR_AMT,0 PAY FROM LEDGER_HT_HIST WHERE TRUNC(TO_DATE(MON_YEAR,'MON-YYYY'),'MM')=ADD_MONTHS(TRUNC(SYSDATE,'MONTH'),-1) AND SUBSTR(USCNO,1,3) IN('GNT','VJA','ONG','CRD')\r\n"
						+ "UNION\r\n"
						+ "select uscno SCNO,0 OB,0 DEM,SUM(CASE WHEN RJTYPE='DR' THEN (nvl(ENGCHG,0)+nvl(THEFT,0)+nvl(OTHCHG,0)+nvl(CUSTCHG,0)+nvl(DEMCHG,0)+nvl(ED,0)+nvl(LPC,0)+nvl(EDI,0)+nvl(FSA,0)+NVL(CC_LPC,0)+NVL(CC_OTH,0)) END) DR_AMT,SUM(CASE WHEN RJTYPE='CR'THEN (nvl(ENGCHG,0)+nvl(THEFT,0)+nvl(OTHCHG,0)+nvl(CUSTCHG,0)+nvl(DEMCHG,0)+nvl(ED,0)+nvl(LPC,0)+nvl(EDI,0)+nvl(FSA,0)+NVL(CC_LPC,0)+NVL(CC_OTH,0)) END) CR_AMT,0 PAY  from JOURNAL WHERE TRUNC(rjdt,'MM')=TRUNC(SYSDATE,'MONTH')  and SUBSTR(USCNO,1,3) IN('GNT','VJA','ONG','CRD') AND TRIM(STATUS) NOT IN ('X','E') GROUP BY USCNO\r\n"
						+ "UNION\r\n"
						+ "SELECT USCNO SCNO,0 OB,0 DEM,0 DR_AMT,0 CR_AMT,SUM(NVL(PCMD,0)) PAY FROM PAY_ht WHERE TO_DATE(PAY_DATE) between TRUNC(SYSDATE,'MONTH') and TO_DATE(sysdate - 1) AND pay_sta_flg='C' AND SUBSTR(USCNO,1,3) IN('GNT','VJA','ONG','CRD') GROUP BY USCNO) GROUP BY SCNO)\r\n"
						+ "\r\n" + "WHERE CTUSCNO=SCNO  AND SUBSTR(CTSECCD,-5)=SECCD \r\n" + "GROUP BY \r\n"
						+ "CASE WHEN NVL(CTGOVT_PVT,'N')='Y'  THEN 'GOVT' ELSE 'PVT' END ,\r\n" + "CIRNAME,\r\n"
						+ "DIVNAME))\r\n" + "GROUP BY CUBE (TYPE,CIRNAME,DIVNAME) order by\r\n"
						+ "case when TYPE='GOVT' then '001' when TYPE = 'NON-GOVT' then '002' else TYPE end,\r\n"
						+ "case when CIRNAME = 'VIJAYAWADA' then '001' when CIRNAME = 'GUNTUR' then '002' when CIRNAME = 'ONGOLE' then '003'  when CIRNAME = 'CRDA' then '009' else CIRNAME end,\r\n"
						+ "case when DIVNAME='TOTAL' then 'ZZZZZZZZZ' else DIVNAME end";
				log.info(sql);
				return jdbcTemplate.queryForList(sql, new Object[] {});
			} catch (DataAccessException e) {
				e.printStackTrace();
				log.error(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}
		} else {
			try {

				/*
				 * String sql =
				 * "select * from (select  nvl(TYPE,'TOTAL') TYPE, nvl(CIRNAME,'APCPDCL') CIRNAME,nvl(TYPE,'APCPDCL')||nvl(CIRNAME,'TOTAL') CIR_TYPE , nvl(DIVNAME,'TOTAL')DIVNAME,SUM(NOS) NOS,SUM(CURR_DEMAND) CURR_DEMAND,SUM(ARREAR_AMOUNT) ARREAR_AMOUNT, SUM(TOTAL) TOTAL ,\r\n"
				 * +
				 * "SUM(NOS_COLLECTED) NOS_COLLECTED ,SUM(CURR_DEM_COLLECTED) CURR_DEM_COLLECTED,SUM(COLLECTED_AGA_CMD) COLLECTED_AGA_CMD,\r\n"
				 * +
				 * "SUM(ARREAR_COLLECTED) ARREAR_COLLECTED,SUM(ARR_C_AGA_CMD) ARR_C_AGA_CMD ,SUM(COLL_AGA_TOT_AMT) COLL_AGA_TOT_AMT, SUM(TOTAL_DEUES) TOTAL_DEUES  from \r\n"
				 * + "(\r\n" +
				 * "select TYPE,CIRNAME,DIVNAME,NOS,round(CURR_DEMAND/100000,2) CURR_DEMAND ,round(ARREAR_AMOUNT/100000,2) ARREAR_AMOUNT,round(TOTAL/100000,2) TOTAL,\r\n"
				 * +
				 * "NOS_COLLECTED,round(CURR_DEM_COLLECTED/100000,2) CURR_DEM_COLLECTED , round((CURR_DEM_COLLECTED/CURR_DEMAND)*100,2) COLLECTED_AGA_CMD,\r\n"
				 * +
				 * "round(ARREAR_COLLECTED/100000,2) ARREAR_COLLECTED,round((ARREAR_COLLECTED/ARREAR_AMOUNT)*100,2) ARR_C_AGA_CMD, round(((CURR_DEM_COLLECTED+ARREAR_COLLECTED)/TOTAL)*100,2) COLL_AGA_TOT_AMT,\r\n"
				 * +
				 * "round((TOTAL -(CURR_DEM_COLLECTED+ARREAR_COLLECTED))/100000,2) TOTAL_DEUES from (\r\n"
				 * + "SELECT \r\n" +
				 * " CASE WHEN NVL(CTGOVT_PVT,'N')='Y'  THEN 'GOVT' ELSE 'PVT' END TYPE, CIRNAME,DIVNAME,COUNT(SCNO) NOS,SUM(NVL(DEM,0)) CURR_DEMAND,SUM(NVL(TOB,0)) ARREAR_AMOUNT,SUM(NVL(TOT,0)) TOTAL,\r\n"
				 * +
				 * "SUM(CASE WHEN NVL(COLLECTION,0)>0 THEN 1 ELSE 0 END) NOS_COLLECTED,SUM(CASE WHEN NVL(TOB,0)>0 THEN   CASE WHEN (NVL(TOB,0)-NVL(COLLECTION,0))>0 THEN 0 ELSE (NVL(COLLECTION,0)-NVL(TOB,0)) END ELSE NVL(COLLECTION,0) END) CURR_DEM_COLLECTED, \r\n"
				 * +
				 * "SUM(CASE WHEN NVL(TOB,0)>0 THEN CASE WHEN NVL(TOB,0)>NVL(COLLECTION,0) THEN NVL(COLLECTION,0) ELSE NVL(TOB,0) END END) ARREAR_COLLECTED,\r\n"
				 * + "SUM(NVL( TCB,0)) CB FROM CONS,SPDCLMASTER, \r\n" +
				 * " (SELECT  SCNO,SUM(NVL(OB,0)) TOB,SUM(NVL(DEM,0)+NVL(DR_AMT,0)) DEM,SUM(NVL(OB,0)+NVL(DEM,0)+NVL(DR_AMT,0)) TOT,\r\n"
				 * +
				 * "SUM(NVL(PAY,0)+NVL(CR_AMT,0)) COLLECTION,SUM(NVL(OB,0)+(NVL(DEM,0)+NVL(DR_AMT,0))-(NVL(PAY,0)+NVL(CR_AMT,0))) TCB\r\n"
				 * + "FROM \r\n" +
				 * "(SELECT BTSCNO SCNO,0 OB,SUM(NVL(BTCURDEM,0)+NVL(BTCOURT_LPC,0)) DEM,0 DR_AMT,0 CR_AMT,0 PAY FROM BILL WHERE BTBLDT between TRUNC(SYSDATE,'MONTH') and TO_DATE(sysdate - 1) AND SUBSTR(BTSCNO,1,3) IN('GNT','VJA','ONG','CRD') GROUP BY BTSCNO\r\n"
				 * + "UNION\r\n" +
				 * "SELECT USCNO SCNO,NVL(CBTOT,0)+NVL(CB_CCLPC,0)+NVL(CB_OTH,0) OB,0 DEM,0 DR_AMT,0 CR_AMT,0 PAY FROM LEDGER_HT_HIST WHERE TRUNC(TO_DATE(MON_YEAR,'MON-YYYY'),'MM')=ADD_MONTHS(TRUNC(SYSDATE,'MONTH'),-1) AND SUBSTR(USCNO,1,3) IN('GNT','VJA','ONG','CRD')\r\n"
				 * + "UNION\r\n" +
				 * "select uscno SCNO,0 OB,0 DEM,SUM(CASE WHEN RJTYPE='DR' THEN (nvl(ENGCHG,0)+nvl(THEFT,0)+nvl(OTHCHG,0)+nvl(CUSTCHG,0)+nvl(DEMCHG,0)+nvl(ED,0)+nvl(LPC,0)+nvl(EDI,0)+nvl(FSA,0)+NVL(CC_LPC,0)+NVL(CC_OTH,0)) END) DR_AMT,SUM(CASE WHEN RJTYPE='CR'THEN (nvl(ENGCHG,0)+nvl(THEFT,0)+nvl(OTHCHG,0)+nvl(CUSTCHG,0)+nvl(DEMCHG,0)+nvl(ED,0)+nvl(LPC,0)+nvl(EDI,0)+nvl(FSA,0)+NVL(CC_LPC,0)+NVL(CC_OTH,0)) END) CR_AMT,0 PAY  from JOURNAL WHERE TRUNC(rjdt,'MM')=TRUNC(SYSDATE,'MONTH')  and SUBSTR(USCNO,1,3) IN('GNT','VJA','ONG','CRD') AND TRIM(STATUS) NOT IN ('X','E') GROUP BY USCNO\r\n"
				 * + "UNION\r\n" +
				 * "SELECT USCNO SCNO,0 OB,0 DEM,0 DR_AMT,0 CR_AMT,SUM(NVL(PCMD,0)) PAY FROM PAY_ht WHERE TO_DATE(PAY_DATE) between TRUNC(SYSDATE,'MONTH') and TO_DATE(sysdate - 1) AND pay_sta_flg='C' AND SUBSTR(USCNO,1,3) IN('GNT','VJA','ONG','CRD') GROUP BY USCNO) GROUP BY SCNO)\r\n"
				 * + "\r\n" +
				 * "WHERE CTUSCNO=SCNO  AND SUBSTR(CTSECCD,-5)=SECCD AND SUBSTR(CTUSCNO,1,3)=?\r\n"
				 * + "GROUP BY \r\n" +
				 * "CASE WHEN NVL(CTGOVT_PVT,'N')='Y'  THEN 'GOVT' ELSE 'PVT' END ,\r\n" +
				 * "CIRNAME,\r\n" + "DIVNAME))\r\n" +
				 * "GROUP BY CUBE (TYPE,CIRNAME,DIVNAME) order by\r\n" +
				 * "case when TYPE='GOVT' then '001' when TYPE = 'NON-GOVT' then '002' else TYPE end,\r\n"
				 * +
				 * "case when CIRNAME = 'VIJAYAWADA' then '001' when CIRNAME = 'GUNTUR' then '002' when CIRNAME = 'ONGOLE' then '003'  when CIRNAME = 'CRDA' then '009' else CIRNAME end,\r\n"
				 * +
				 * "case when DIVNAME='TOTAL' then 'ZZZZZZZZZ' else DIVNAME end) where type is not null and cirname is not null"
				 * ;
				 */
				String sql = "select * from (select  case when type is null and cirname is null then 'GOVT-PVT' else TYPE end  TYPE, \n"
						+ "case when type is null and cirname is null then 'TOTAL' else CIRNAME end \n" + "CIRNAME,\n"
						+ "case when type is null and cirname is null then 'YES' else 'NO' end FLAG,\n"
						+ "nvl(TYPE,'APCPDCL')||nvl(CIRNAME,'TOTAL') CIR_TYPE , nvl(DIVNAME,'TOTAL')DIVNAME,SUM(NOS) NOS,SUM(CURR_DEMAND) CURR_DEMAND,SUM(ARREAR_AMOUNT) ARREAR_AMOUNT, SUM(TOTAL) TOTAL ,\n"
						+ "SUM(NOS_COLLECTED) NOS_COLLECTED ,SUM(CURR_DEM_COLLECTED) CURR_DEM_COLLECTED,SUM(COLLECTED_AGA_CMD) COLLECTED_AGA_CMD,\n"
						+ "SUM(ARREAR_COLLECTED) ARREAR_COLLECTED,SUM(ARR_C_AGA_CMD) ARR_C_AGA_CMD ,SUM(COLL_AGA_TOT_AMT) COLL_AGA_TOT_AMT, SUM(TOTAL_DEUES) TOTAL_DEUES  from \n"
						+ "(\n"
						+ "select TYPE,CIRNAME,DIVNAME,NOS,round(CURR_DEMAND/100000,2) CURR_DEMAND ,round(ARREAR_AMOUNT/100000,2) ARREAR_AMOUNT,round(TOTAL/100000,2) TOTAL,\n"
						+ "NOS_COLLECTED,round(CURR_DEM_COLLECTED/100000,2) CURR_DEM_COLLECTED , round((CURR_DEM_COLLECTED/CURR_DEMAND)*100,2) COLLECTED_AGA_CMD,\n"
						+ "round(ARREAR_COLLECTED/100000,2) ARREAR_COLLECTED,round((ARREAR_COLLECTED/ARREAR_AMOUNT)*100,2) ARR_C_AGA_CMD, round(((CURR_DEM_COLLECTED+ARREAR_COLLECTED)/TOTAL)*100,2) COLL_AGA_TOT_AMT,\n"
						+ "round((TOTAL -(CURR_DEM_COLLECTED+ARREAR_COLLECTED))/100000,2) TOTAL_DEUES from (\n"
						+ "SELECT \n"
						+ " CASE WHEN NVL(CTGOVT_PVT,'N')='Y'  THEN 'GOVT' ELSE 'PVT' END TYPE, CIRNAME,DIVNAME,COUNT(SCNO) NOS,SUM(NVL(DEM,0)) CURR_DEMAND,SUM(NVL(TOB,0)) ARREAR_AMOUNT,SUM(NVL(TOT,0)) TOTAL,\n"
						+ "SUM(CASE WHEN NVL(COLLECTION,0)>0 THEN 1 ELSE 0 END) NOS_COLLECTED,SUM(CASE WHEN NVL(TOB,0)>0 THEN   CASE WHEN (NVL(TOB,0)-NVL(COLLECTION,0))>0 THEN 0 ELSE (NVL(COLLECTION,0)-NVL(TOB,0)) END ELSE NVL(COLLECTION,0) END) CURR_DEM_COLLECTED, \n"
						+ "SUM(CASE WHEN NVL(TOB,0)>0 THEN CASE WHEN NVL(TOB,0)>NVL(COLLECTION,0) THEN NVL(COLLECTION,0) ELSE NVL(TOB,0) END END) ARREAR_COLLECTED,\n"
						+ "SUM(NVL( TCB,0)) CB FROM CONS,SPDCLMASTER, \n"
						+ " (SELECT  SCNO,SUM(NVL(OB,0)) TOB,SUM(NVL(DEM,0)+NVL(DR_AMT,0)) DEM,SUM(NVL(OB,0)+NVL(DEM,0)+NVL(DR_AMT,0)) TOT,\n"
						+ "SUM(NVL(PAY,0)+NVL(CR_AMT,0)) COLLECTION,SUM(NVL(OB,0)+(NVL(DEM,0)+NVL(DR_AMT,0))-(NVL(PAY,0)+NVL(CR_AMT,0))) TCB\n"
						+ "FROM \n"
						+ "(SELECT BTSCNO SCNO,0 OB,SUM(NVL(BTCURDEM,0)+NVL(BTCOURT_LPC,0)) DEM,0 DR_AMT,0 CR_AMT,0 PAY FROM BILL WHERE BTBLDT between TRUNC(SYSDATE,'MONTH') and TO_DATE(sysdate - 1) AND SUBSTR(BTSCNO,1,3) IN('GNT','VJA','ONG','CRD') GROUP BY BTSCNO\n"
						+ "UNION\n"
						+ "SELECT USCNO SCNO,NVL(CBTOT,0)+NVL(CB_CCLPC,0)+NVL(CB_OTH,0) OB,0 DEM,0 DR_AMT,0 CR_AMT,0 PAY FROM LEDGER_HT_HIST WHERE TRUNC(TO_DATE(MON_YEAR,'MON-YYYY'),'MM')=ADD_MONTHS(TRUNC(SYSDATE,'MONTH'),-1) AND SUBSTR(USCNO,1,3) IN('GNT','VJA','ONG','CRD')\n"
						+ "UNION\n"
						+ "select uscno SCNO,0 OB,0 DEM,SUM(CASE WHEN RJTYPE='DR' THEN (nvl(ENGCHG,0)+nvl(THEFT,0)+nvl(OTHCHG,0)+nvl(CUSTCHG,0)+nvl(DEMCHG,0)+nvl(ED,0)+nvl(LPC,0)+nvl(EDI,0)+nvl(FSA,0)+NVL(CC_LPC,0)+NVL(CC_OTH,0)) END) DR_AMT,SUM(CASE WHEN RJTYPE='CR'THEN (nvl(ENGCHG,0)+nvl(THEFT,0)+nvl(OTHCHG,0)+nvl(CUSTCHG,0)+nvl(DEMCHG,0)+nvl(ED,0)+nvl(LPC,0)+nvl(EDI,0)+nvl(FSA,0)+NVL(CC_LPC,0)+NVL(CC_OTH,0)) END) CR_AMT,0 PAY  from JOURNAL WHERE TRUNC(rjdt,'MM')=TRUNC(SYSDATE,'MONTH')  and SUBSTR(USCNO,1,3) IN('GNT','VJA','ONG','CRD') AND TRIM(STATUS) NOT IN ('X','E') GROUP BY USCNO\n"
						+ "UNION\n"
						+ "SELECT USCNO SCNO,0 OB,0 DEM,0 DR_AMT,0 CR_AMT,SUM(NVL(PCMD,0)) PAY FROM PAY_ht WHERE TO_DATE(PAY_DATE) between TRUNC(SYSDATE,'MONTH') and TO_DATE(sysdate - 1) AND pay_sta_flg='C' AND SUBSTR(USCNO,1,3) IN('GNT','VJA','ONG','CRD') GROUP BY USCNO) GROUP BY SCNO)\n"
						+ "\n" + "WHERE CTUSCNO=SCNO  AND SUBSTR(CTSECCD,-5)=SECCD AND SUBSTR(CTUSCNO,1,3)=?\n"
						+ "GROUP BY \n" + "CASE WHEN NVL(CTGOVT_PVT,'N')='Y'  THEN 'GOVT' ELSE 'PVT' END ,\n"
						+ "CIRNAME,\n" + "DIVNAME))\n" + "GROUP BY CUBE (TYPE,CIRNAME,DIVNAME) order by\n"
						+ "case when TYPE='GOVT' then '001' when TYPE = 'PVT' then '002' else TYPE end,\n"
						+ "case when CIRNAME = 'VIJAYAWADA' then '001' when CIRNAME = 'GUNTUR' then '002' when CIRNAME = 'ONGOLE' then '003'  when CIRNAME = 'CRDA' then '009' else CIRNAME end,\n"
						+ "case when DIVNAME='TOTAL' then 'ZZZZZZZZZ' else DIVNAME end) where type is not null and cirname is not null  or FLAG='YES'";
				log.info(sql);
				return jdbcTemplate.queryForList(sql, new Object[] { circle });
			} catch (DataAccessException e) {
				e.printStackTrace();
				log.error(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}

		}
	}

	public List<Map<String, Object>> getDCSectionWiseReport(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		String year = request.getParameter("year");

		if (circle.equalsIgnoreCase("ALL")) {
			try {
				String sql = "select * from (select nvl(TYPE,'TOTAL') TYPE, nvl(CIRNAME,'APCPDCL') CIRNAME,nvl(TYPE,'APCPDCL')||nvl(CIRNAME,'TOTAL') CIR_TYPE , nvl(DIVNAME,'TOTAL') DIVNAME,\r\n"
						+ "nvl(SECNAME,'TOTAL')  SECNAME,case when (divname is null and secname is not null) then 'T' else 'F'end FLAG,nvl(TYPE,'APCPDCL')||nvl(CIRNAME,'TOTAL')||nvl(DIVNAME,'TOTAL') DIV_TYPE ,\r\n"
						+ "SUM(NOS) NOS,SUM(CURR_DEMAND) CURR_DEMAND,SUM(ARREAR_AMOUNT) ARREAR_AMOUNT, SUM(TOTAL) TOTAL ,\r\n"
						+ "SUM(NOS_COLLECTED) NOS_COLLECTED ,SUM(CURR_DEM_COLLECTED) CURR_DEM_COLLECTED,SUM(COLLECTED_AGA_CMD) COLLECTED_AGA_CMD,\r\n"
						+ "NVL(SUM(ARREAR_COLLECTED),0) ARREAR_COLLECTED,SUM(ARR_C_AGA_CMD) ARR_C_AGA_CMD ,SUM(COLL_AGA_TOT_AMT) COLL_AGA_TOT_AMT, SUM(TOTAL_DEUES) TOTAL_DEUES  from \r\n"
						+ "(\r\n"
						+ "select TYPE,CIRNAME,DIVNAME,SECNAME,NOS,round(CURR_DEMAND/100000,2) CURR_DEMAND ,round(ARREAR_AMOUNT/100000,2) ARREAR_AMOUNT,round(TOTAL/100000,2) TOTAL,\r\n"
						+ "NOS_COLLECTED,round(CURR_DEM_COLLECTED/100000,2) CURR_DEM_COLLECTED , 0 COLLECTED_AGA_CMD,\r\n"
						+ "round(ARREAR_COLLECTED/100000,2) ARREAR_COLLECTED,0 ARR_C_AGA_CMD, 0 COLL_AGA_TOT_AMT,\r\n"
						+ "round((TOTAL -(CURR_DEM_COLLECTED+ARREAR_COLLECTED))/100000,2) TOTAL_DEUES from (\r\n"
						+ "SELECT \r\n"
						+ " CASE WHEN NVL(CTGOVT_PVT,'N')='Y'  THEN 'GOVT' ELSE 'PVT' END TYPE, CIRNAME,DIVNAME,SECNAME,COUNT(SCNO) NOS,SUM(NVL(DEM,0)) CURR_DEMAND,SUM(NVL(TOB,0)) ARREAR_AMOUNT,SUM(NVL(TOT,0)) TOTAL,\r\n"
						+ "SUM(CASE WHEN NVL(COLLECTION,0)>0 THEN 1 ELSE 0 END) NOS_COLLECTED,SUM(CASE WHEN NVL(TOB,0)>0 THEN   CASE WHEN (NVL(TOB,0)-NVL(COLLECTION,0))>0 THEN 0 ELSE (NVL(COLLECTION,0)-NVL(TOB,0)) END ELSE NVL(COLLECTION,0) END) CURR_DEM_COLLECTED, \r\n"
						+ "SUM(CASE WHEN NVL(TOB,0)>0 THEN CASE WHEN NVL(TOB,0)>NVL(COLLECTION,0) THEN NVL(COLLECTION,0) ELSE NVL(TOB,0) END END) ARREAR_COLLECTED,\r\n"
						+ "SUM(NVL( TCB,0)) CB FROM CONS,SPDCLMASTER, \r\n"
						+ " (SELECT  SCNO,SUM(NVL(OB,0)) TOB,SUM(NVL(DEM,0)+NVL(DR_AMT,0)) DEM,SUM(NVL(OB,0)+NVL(DEM,0)+NVL(DR_AMT,0)) TOT,\r\n"
						+ "SUM(NVL(PAY,0)+NVL(CR_AMT,0)) COLLECTION,SUM(NVL(OB,0)+(NVL(DEM,0)+NVL(DR_AMT,0))-(NVL(PAY,0)+NVL(CR_AMT,0))) TCB\r\n"
						+ "FROM \r\n"
						+ "(SELECT BTSCNO SCNO,0 OB,SUM(NVL(BTCURDEM,0)+NVL(BTCOURT_LPC,0)) DEM,0 DR_AMT,0 CR_AMT,0 PAY FROM BILL WHERE BTBLDT between TRUNC(SYSDATE,'MONTH') and TO_DATE(sysdate - 1) AND SUBSTR(BTSCNO,1,3) IN('GNT','VJA','ONG','CRD') GROUP BY BTSCNO\r\n"
						+ "UNION\r\n"
						+ "SELECT USCNO SCNO,NVL(CBTOT,0)+NVL(CB_CCLPC,0)+NVL(CB_OTH,0) OB,0 DEM,0 DR_AMT,0 CR_AMT,0 PAY FROM LEDGER_HT_HIST WHERE TRUNC(TO_DATE(MON_YEAR,'MON-YYYY'),'MM')=ADD_MONTHS(TRUNC(SYSDATE,'MONTH'),-1) AND SUBSTR(USCNO,1,3) IN('GNT','VJA','ONG','CRD')\r\n"
						+ "UNION\r\n"
						+ "select uscno SCNO,0 OB,0 DEM,SUM(CASE WHEN RJTYPE='DR' THEN (nvl(ENGCHG,0)+nvl(THEFT,0)+nvl(OTHCHG,0)+nvl(CUSTCHG,0)+nvl(DEMCHG,0)+nvl(ED,0)+nvl(LPC,0)+nvl(EDI,0)+nvl(FSA,0)+NVL(CC_LPC,0)+NVL(CC_OTH,0)) END) DR_AMT,SUM(CASE WHEN RJTYPE='CR'THEN (nvl(ENGCHG,0)+nvl(THEFT,0)+nvl(OTHCHG,0)+nvl(CUSTCHG,0)+nvl(DEMCHG,0)+nvl(ED,0)+nvl(LPC,0)+nvl(EDI,0)+nvl(FSA,0)+NVL(CC_LPC,0)+NVL(CC_OTH,0)) END) CR_AMT,0 PAY  from JOURNAL WHERE TRUNC(rjdt,'MM')=TRUNC(SYSDATE,'MONTH')  and SUBSTR(USCNO,1,3) IN('GNT','VJA','ONG','CRD') AND TRIM(STATUS) NOT IN ('X','E') GROUP BY USCNO\r\n"
						+ "UNION\r\n"
						+ "SELECT USCNO SCNO,0 OB,0 DEM,0 DR_AMT,0 CR_AMT,SUM(NVL(PCMD,0)) PAY FROM PAY_ht WHERE TO_DATE(PAY_DATE) between TRUNC(SYSDATE,'MONTH') and TO_DATE(sysdate - 1) AND pay_sta_flg='C' AND SUBSTR(USCNO,1,3) IN('GNT','VJA','ONG','CRD') GROUP BY USCNO) GROUP BY SCNO)\r\n"
						+ "\r\n" + "WHERE CTUSCNO=SCNO  AND SUBSTR(CTSECCD,-5)=SECCD \r\n" + "GROUP BY \r\n"
						+ "CASE WHEN NVL(CTGOVT_PVT,'N')='Y'  THEN 'GOVT' ELSE 'PVT' END ,\r\n" + "CIRNAME,\r\n"
						+ "DIVNAME,SECNAME))\r\n" + "GROUP BY CUBE (TYPE,CIRNAME,DIVNAME,SECNAME) order by\r\n"
						+ "case when TYPE='GOVT' then '001' when TYPE = 'NON-GOVT' then '002' else TYPE end,\r\n"
						+ "case when CIRNAME = 'VIJAYAWADA' then '001' when CIRNAME = 'GUNTUR' then '002' when CIRNAME = 'ONGOLE' then '003'  when CIRNAME = 'CRDA' then '009' else CIRNAME end,\r\n"
						+ "case when DIVNAME='TOTAL' then 'ZZZZZZZZZ' else DIVNAME end,\r\n"
						+ "case when SECNAME='TOTAL' then 'ZZZZZZZZZZZ' else SECNAME end)\r\n" + "where FLAG='F'";
				log.info(sql);
				return jdbcTemplate.queryForList(sql, new Object[] {});
			} catch (DataAccessException e) {
				e.printStackTrace();
				log.error(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}
		} else {
			try {

				String sql = "select * from (select TYPE, CIRNAME,nvl(TYPE,'APCPDCL')||nvl(CIRNAME,'TOTAL') CIR_TYPE , nvl(DIVNAME,'TOTAL') DIVNAME,\r\n"
						+ "nvl(SECNAME,'TOTAL')  SECNAME,case when (divname is null and secname is not null) then 'T' else 'F'end FLAG,nvl(TYPE,'APCPDCL')||nvl(CIRNAME,'TOTAL')||nvl(DIVNAME,'TOTAL') DIV_TYPE ,\r\n"
						+ "SUM(NOS) NOS,SUM(CURR_DEMAND) CURR_DEMAND,SUM(ARREAR_AMOUNT) ARREAR_AMOUNT, SUM(TOTAL) TOTAL ,\r\n"
						+ "SUM(NOS_COLLECTED) NOS_COLLECTED ,SUM(CURR_DEM_COLLECTED) CURR_DEM_COLLECTED,SUM(COLLECTED_AGA_CMD) COLLECTED_AGA_CMD,\r\n"
						+ "NVL(SUM(ARREAR_COLLECTED),0) ARREAR_COLLECTED,SUM(ARR_C_AGA_CMD) ARR_C_AGA_CMD ,SUM(COLL_AGA_TOT_AMT) COLL_AGA_TOT_AMT, SUM(TOTAL_DEUES) TOTAL_DEUES  from \r\n"
						+ "(\r\n"
						+ "select TYPE,CIRNAME,DIVNAME,SECNAME,NOS,round(CURR_DEMAND/100000,2) CURR_DEMAND ,round(ARREAR_AMOUNT/100000,2) ARREAR_AMOUNT,round(TOTAL/100000,2) TOTAL,\r\n"
						+ "NOS_COLLECTED,round(CURR_DEM_COLLECTED/100000,2) CURR_DEM_COLLECTED , 0 COLLECTED_AGA_CMD,\r\n"
						+ "round(ARREAR_COLLECTED/100000,2) ARREAR_COLLECTED,0 ARR_C_AGA_CMD, 0 COLL_AGA_TOT_AMT,\r\n"
						+ "round((TOTAL -(CURR_DEM_COLLECTED+ARREAR_COLLECTED))/100000,2) TOTAL_DEUES from (\r\n"
						+ "SELECT \r\n"
						+ " CASE WHEN NVL(CTGOVT_PVT,'N')='Y'  THEN 'GOVT' ELSE 'PVT' END TYPE, CIRNAME,DIVNAME,SECNAME,COUNT(SCNO) NOS,SUM(NVL(DEM,0)) CURR_DEMAND,SUM(NVL(TOB,0)) ARREAR_AMOUNT,SUM(NVL(TOT,0)) TOTAL,\r\n"
						+ "SUM(CASE WHEN NVL(COLLECTION,0)>0 THEN 1 ELSE 0 END) NOS_COLLECTED,SUM(CASE WHEN NVL(TOB,0)>0 THEN   CASE WHEN (NVL(TOB,0)-NVL(COLLECTION,0))>0 THEN 0 ELSE (NVL(COLLECTION,0)-NVL(TOB,0)) END ELSE NVL(COLLECTION,0) END) CURR_DEM_COLLECTED, \r\n"
						+ "SUM(CASE WHEN NVL(TOB,0)>0 THEN CASE WHEN NVL(TOB,0)>NVL(COLLECTION,0) THEN NVL(COLLECTION,0) ELSE NVL(TOB,0) END END) ARREAR_COLLECTED,\r\n"
						+ "SUM(NVL( TCB,0)) CB FROM CONS,SPDCLMASTER, \r\n"
						+ " (SELECT  SCNO,SUM(NVL(OB,0)) TOB,SUM(NVL(DEM,0)+NVL(DR_AMT,0)) DEM,SUM(NVL(OB,0)+NVL(DEM,0)+NVL(DR_AMT,0)) TOT,\r\n"
						+ "SUM(NVL(PAY,0)+NVL(CR_AMT,0)) COLLECTION,SUM(NVL(OB,0)+(NVL(DEM,0)+NVL(DR_AMT,0))-(NVL(PAY,0)+NVL(CR_AMT,0))) TCB\r\n"
						+ "FROM \r\n"
						+ "(SELECT BTSCNO SCNO,0 OB,SUM(NVL(BTCURDEM,0)+NVL(BTCOURT_LPC,0)) DEM,0 DR_AMT,0 CR_AMT,0 PAY FROM BILL WHERE BTBLDT between TRUNC(SYSDATE,'MONTH') and TO_DATE(sysdate - 1) AND SUBSTR(BTSCNO,1,3) IN('GNT','VJA','ONG','CRD') GROUP BY BTSCNO\r\n"
						+ "UNION\r\n"
						+ "SELECT USCNO SCNO,NVL(CBTOT,0)+NVL(CB_CCLPC,0)+NVL(CB_OTH,0) OB,0 DEM,0 DR_AMT,0 CR_AMT,0 PAY FROM LEDGER_HT_HIST WHERE TRUNC(TO_DATE(MON_YEAR,'MON-YYYY'),'MM')=ADD_MONTHS(TRUNC(SYSDATE,'MONTH'),-1) AND SUBSTR(USCNO,1,3) IN('GNT','VJA','ONG','CRD')\r\n"
						+ "UNION\r\n"
						+ "select uscno SCNO,0 OB,0 DEM,SUM(CASE WHEN RJTYPE='DR' THEN (nvl(ENGCHG,0)+nvl(THEFT,0)+nvl(OTHCHG,0)+nvl(CUSTCHG,0)+nvl(DEMCHG,0)+nvl(ED,0)+nvl(LPC,0)+nvl(EDI,0)+nvl(FSA,0)+NVL(CC_LPC,0)+NVL(CC_OTH,0)) END) DR_AMT,SUM(CASE WHEN RJTYPE='CR'THEN (nvl(ENGCHG,0)+nvl(THEFT,0)+nvl(OTHCHG,0)+nvl(CUSTCHG,0)+nvl(DEMCHG,0)+nvl(ED,0)+nvl(LPC,0)+nvl(EDI,0)+nvl(FSA,0)+NVL(CC_LPC,0)+NVL(CC_OTH,0)) END) CR_AMT,0 PAY  from JOURNAL WHERE TRUNC(rjdt,'MM')=TRUNC(SYSDATE,'MONTH')  and SUBSTR(USCNO,1,3) IN('GNT','VJA','ONG','CRD') AND TRIM(STATUS) NOT IN ('X','E') GROUP BY USCNO\r\n"
						+ "UNION\r\n"
						+ "SELECT USCNO SCNO,0 OB,0 DEM,0 DR_AMT,0 CR_AMT,SUM(NVL(PCMD,0)) PAY FROM PAY_ht WHERE TO_DATE(PAY_DATE) between TRUNC(SYSDATE,'MONTH') and TO_DATE(sysdate - 1) AND pay_sta_flg='C' AND SUBSTR(USCNO,1,3) IN('GNT','VJA','ONG','CRD') GROUP BY USCNO) GROUP BY SCNO)\r\n"
						+ "\r\n" + "WHERE CTUSCNO=SCNO  AND SUBSTR(CTSECCD,-5)=SECCD  AND SUBSTR(CTUSCNO,1,3)=?\r\n"
						+ "GROUP BY \r\n" + "CASE WHEN NVL(CTGOVT_PVT,'N')='Y'  THEN 'GOVT' ELSE 'PVT' END ,\r\n"
						+ "CIRNAME,\r\n" + "DIVNAME,SECNAME))\r\n"
						+ "GROUP BY CUBE (TYPE,CIRNAME,DIVNAME,SECNAME) order by\r\n"
						+ "case when TYPE='GOVT' then '001' when TYPE = 'NON-GOVT' then '002' else TYPE end,\r\n"
						+ "case when CIRNAME = 'VIJAYAWADA' then '001' when CIRNAME = 'GUNTUR' then '002' when CIRNAME = 'ONGOLE' then '003'  when CIRNAME = 'CRDA' then '009' else CIRNAME end,\r\n"
						+ "case when DIVNAME='TOTAL' then 'ZZZZZZZZZ' else DIVNAME end,\r\n"
						+ "case when SECNAME='TOTAL' then 'ZZZZZZZZZZZ' else SECNAME end)\r\n"
						+ "where FLAG='F' and type is not null and cirname is not null";
				log.info(sql);

				/*
				 * String sql =
				 * "select * from (select  TYPE,  CIRNAME,nvl(TYPE,'APCPDCL')||nvl(CIRNAME,'TOTAL') CIR_TYPE , nvl(DIVNAME,'TOTAL')DIVNAME,SUM(NOS) NOS,SUM(CURR_DEMAND) CURR_DEMAND,SUM(ARREAR_AMOUNT) ARREAR_AMOUNT, SUM(TOTAL) TOTAL ,\r\n"
				 * +
				 * "SUM(NOS_COLLECTED) NOS_COLLECTED ,SUM(CURR_DEM_COLLECTED) CURR_DEM_COLLECTED,SUM(COLLECTED_AGA_CMD) COLLECTED_AGA_CMD,\r\n"
				 * +
				 * "SUM(ARREAR_COLLECTED) ARREAR_COLLECTED,SUM(ARR_C_AGA_CMD) ARR_C_AGA_CMD ,SUM(COLL_AGA_TOT_AMT) COLL_AGA_TOT_AMT, SUM(TOTAL_DEUES) TOTAL_DEUES  from \r\n"
				 * + "(\r\n" +
				 * "select TYPE,CIRNAME,DIVNAME,NOS,round(CURR_DEMAND/100000,2) CURR_DEMAND ,round(ARREAR_AMOUNT/100000,2) ARREAR_AMOUNT,round(TOTAL/100000,2) TOTAL,\r\n"
				 * +
				 * "NOS_COLLECTED,round(CURR_DEM_COLLECTED/100000,2) CURR_DEM_COLLECTED , round((CURR_DEM_COLLECTED/CURR_DEMAND)*100,2) COLLECTED_AGA_CMD,\r\n"
				 * +
				 * "round(ARREAR_COLLECTED/100000,2) ARREAR_COLLECTED,round((ARREAR_COLLECTED/ARREAR_AMOUNT)*100,2) ARR_C_AGA_CMD, round(((CURR_DEM_COLLECTED+ARREAR_COLLECTED)/TOTAL)*100,2) COLL_AGA_TOT_AMT,\r\n"
				 * +
				 * "round((TOTAL -(CURR_DEM_COLLECTED+ARREAR_COLLECTED))/100000,2) TOTAL_DEUES from (\r\n"
				 * + "SELECT \r\n" +
				 * " CASE WHEN NVL(CTGOVT_PVT,'N')='Y'  THEN 'GOVT' ELSE 'PVT' END TYPE, CIRNAME,DIVNAME,COUNT(SCNO) NOS,SUM(NVL(DEM,0)) CURR_DEMAND,SUM(NVL(TOB,0)) ARREAR_AMOUNT,SUM(NVL(TOT,0)) TOTAL,\r\n"
				 * +
				 * "SUM(CASE WHEN NVL(COLLECTION,0)>0 THEN 1 ELSE 0 END) NOS_COLLECTED,SUM(CASE WHEN NVL(TOB,0)>0 THEN   CASE WHEN (NVL(TOB,0)-NVL(COLLECTION,0))>0 THEN 0 ELSE (NVL(COLLECTION,0)-NVL(TOB,0)) END ELSE NVL(COLLECTION,0) END) CURR_DEM_COLLECTED, \r\n"
				 * +
				 * "SUM(CASE WHEN NVL(TOB,0)>0 THEN CASE WHEN NVL(TOB,0)>NVL(COLLECTION,0) THEN NVL(COLLECTION,0) ELSE NVL(TOB,0) END END) ARREAR_COLLECTED,\r\n"
				 * + "SUM(NVL( TCB,0)) CB FROM CONS,SPDCLMASTER, \r\n" +
				 * " (SELECT  SCNO,SUM(NVL(OB,0)) TOB,SUM(NVL(DEM,0)+NVL(DR_AMT,0)) DEM,SUM(NVL(OB,0)+NVL(DEM,0)+NVL(DR_AMT,0)) TOT,\r\n"
				 * +
				 * "SUM(NVL(PAY,0)+NVL(CR_AMT,0)) COLLECTION,SUM(NVL(OB,0)+(NVL(DEM,0)+NVL(DR_AMT,0))-(NVL(PAY,0)+NVL(CR_AMT,0))) TCB\r\n"
				 * + "FROM \r\n" +
				 * "(SELECT BTSCNO SCNO,0 OB,SUM(NVL(BTCURDEM,0)+NVL(BTCOURT_LPC,0)) DEM,0 DR_AMT,0 CR_AMT,0 PAY FROM BILL WHERE BTBLDT between TRUNC(SYSDATE,'MONTH') and TO_DATE(sysdate - 1) AND SUBSTR(BTSCNO,1,3) IN('GNT','VJA','ONG','CRD') GROUP BY BTSCNO\r\n"
				 * + "UNION\r\n" +
				 * "SELECT USCNO SCNO,NVL(CBTOT,0)+NVL(CB_CCLPC,0)+NVL(CB_OTH,0) OB,0 DEM,0 DR_AMT,0 CR_AMT,0 PAY FROM LEDGER_HT_HIST WHERE TRUNC(TO_DATE(MON_YEAR,'MON-YYYY'),'MM')=ADD_MONTHS(TRUNC(SYSDATE,'MONTH'),-1) AND SUBSTR(USCNO,1,3) IN('GNT','VJA','ONG','CRD')\r\n"
				 * + "UNION\r\n" +
				 * "select uscno SCNO,0 OB,0 DEM,SUM(CASE WHEN RJTYPE='DR' THEN (nvl(ENGCHG,0)+nvl(THEFT,0)+nvl(OTHCHG,0)+nvl(CUSTCHG,0)+nvl(DEMCHG,0)+nvl(ED,0)+nvl(LPC,0)+nvl(EDI,0)+nvl(FSA,0)+NVL(CC_LPC,0)+NVL(CC_OTH,0)) END) DR_AMT,SUM(CASE WHEN RJTYPE='CR'THEN (nvl(ENGCHG,0)+nvl(THEFT,0)+nvl(OTHCHG,0)+nvl(CUSTCHG,0)+nvl(DEMCHG,0)+nvl(ED,0)+nvl(LPC,0)+nvl(EDI,0)+nvl(FSA,0)+NVL(CC_LPC,0)+NVL(CC_OTH,0)) END) CR_AMT,0 PAY  from JOURNAL WHERE TRUNC(rjdt,'MM')=TRUNC(SYSDATE,'MONTH')  and SUBSTR(USCNO,1,3) IN('GNT','VJA','ONG','CRD') AND TRIM(STATUS) NOT IN ('X','E') GROUP BY USCNO\r\n"
				 * + "UNION\r\n" +
				 * "SELECT USCNO SCNO,0 OB,0 DEM,0 DR_AMT,0 CR_AMT,SUM(NVL(PCMD,0)) PAY FROM PAY_ht WHERE TO_DATE(PAY_DATE) between TRUNC(SYSDATE,'MONTH') and TO_DATE(sysdate - 1) AND pay_sta_flg='C' AND SUBSTR(USCNO,1,3) IN('GNT','VJA','ONG','CRD') GROUP BY USCNO) GROUP BY SCNO)\r\n"
				 * + "\r\n" +
				 * "WHERE CTUSCNO=SCNO  AND SUBSTR(CTSECCD,-5)=SECCD AND SUBSTR(CTUSCNO,1,3)=?\r\n"
				 * + "GROUP BY \r\n" +
				 * "CASE WHEN NVL(CTGOVT_PVT,'N')='Y'  THEN 'GOVT' ELSE 'PVT' END ,\r\n" +
				 * "CIRNAME,\r\n" + "DIVNAME))\r\n" +
				 * "GROUP BY CUBE (TYPE,CIRNAME,DIVNAME) order by\r\n" +
				 * "case when TYPE='GOVT' then '001' when TYPE = 'NON-GOVT' then '002' else TYPE end,\r\n"
				 * +
				 * "case when CIRNAME = 'VIJAYAWADA' then '001' when CIRNAME = 'GUNTUR' then '002' when CIRNAME = 'ONGOLE' then '003'  when CIRNAME = 'CRDA' then '009' else CIRNAME end,\r\n"
				 * +
				 * "case when DIVNAME='TOTAL' then 'ZZZZZZZZZ' else DIVNAME end) where type is not null and cirname is not null"
				 * ;
				 */
				log.info(sql);
				return jdbcTemplate.queryForList(sql, new Object[] { circle });
			} catch (DataAccessException e) {
				e.printStackTrace();
				log.error(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}

		}
	}

	public List<Map<String, Object>> getACDMonthyNoticeDetails(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		String monthYear = request.getParameter("month") + "-" + request.getParameter("year");
		System.out.println("Month :" + monthYear);

		if (circle.equalsIgnoreCase("ALL")) {
			try {
				String sql = " SELECT CIRNAME, DIVNAME, SUBNAME, SECNAME,USCNO,NVL(NET_ACD,0) ACD,DECODE(TRIM(CTGOVT_PVT),'Y','GOVT','N','PVT','') TYPE,A.LEVI_MTH\r\n"
						+ "FROM acd_calc_ht_month A,CONS B,SPDCLMASTER C\r\n"
						+ "WHERE A.ACCT_ID=B.CTACCT_ID AND SUBSTR(B.CTSECCD,-5)=C.SECCD AND LEVI_MTH=? AND TRIM(LEVI_FLG)='Y'";
				log.info(sql);
				return jdbcTemplate.queryForList(sql, new Object[] { monthYear });
			} catch (DataAccessException e) {
				e.printStackTrace();
				log.error(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}
		} else {
			try {

				String sql = "SELECT CIRNAME, DIVNAME, SUBNAME, SECNAME,USCNO,NVL(NET_ACD,0) ACD,DECODE(TRIM(CTGOVT_PVT),'Y','GOVT','N','PVT','') TYPE,A.LEVI_MTH\r\n"
						+ "FROM acd_calc_ht_month A,CONS B,SPDCLMASTER C\r\n"
						+ "WHERE A.ACCT_ID=B.CTACCT_ID AND SUBSTR(B.CTSECCD,-5)=C.SECCD AND LEVI_MTH='AUG-2021' AND TRIM(LEVI_FLG)='Y' AND  SUBSTR(USCNO,1,3)=?";
				log.info(sql);
				return jdbcTemplate.queryForList(sql, new Object[] { monthYear, circle });
			} catch (DataAccessException e) {
				e.printStackTrace();
				log.error(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}

		}
	}

	public List<Map<String, Object>> getLedgerCatAbstract(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		String monthYear = request.getParameter("fmonth") + "-" + request.getParameter("fyear");
		String status = request.getParameter("status");
		String constring = status.equals("LB") ? " "
				: status.equals("L") ? " AND nvl(status,'BS')  in ('LIVE','1','NEW','UDC') "
						: " AND nvl(status,'BS') not in ('LIVE','1','NEW','UDC') ";

		if (circle.equalsIgnoreCase("ALL")) {
			try {
				String sql = " select nvl(CIRCLE,'APCPDCL') CIRCLE,nvl(TYPE,'TOTAL') TYPE, nvl(CIRCLE,'APCPDCL')||nvl(TYPE,'TOTAL') CIR_TYPE ,nvl(CTCAT,'TOTAL') CTCAT,SUM(NOS) NOS,sum(OB_OTHERTHAN_COURT) OB_OTHERTHAN_COURT,sum(OB_COURT) OB_COURT,sum(TOT_OB) TOT_OB,\r\n"
						+ "sum(DEMAND_WITHOUT_COURT) DEMAND_WITHOUT_COURT ,sum(COURT_LPC) COURT_LPC,sum(DR_RJ) DR_RJ,sum(CR_RJ) CR_RJ,sum(COURT_RJ) COURT_RJ ,sum(COLLECTION) COLLECTION, \r\n"
						+ "sum(CB_OTHERTHAN_COURT) CB_OTHERTHAN_COURT ,sum(CB_COURT) CB_COURT ,sum(TOTAL_CB) TOTAL_CB,sum(CB_SD) CB_SD\r\n"
						+ "from (\r\n" + "select\r\n"
						+ "substr(ctuscno,1,3) CIRCLE ,DECODE(CTGOVT_PVT,'Y','GOVT','NON-GOVT') TYPE,CTCAT,count(*) NOS,\r\n"
						+ "SUM(ROUND(NVL(TOT_OB,0))) OB_OTHERTHAN_COURT,\r\n"
						+ "SUM(ROUND(NVL(OB_OTH,0)) + ROUND(NVL(OB_CCLPC,0))) OB_COURT,\r\n"
						+ "SUM(ROUND(NVL(OB_OTH,0)) + ROUND(NVL(OB_CCLPC,0))+ ROUND(NVL(TOT_OB,0))) TOT_OB,\r\n"
						+ "SUM(ROUND(NVL(CMD,0))) DEMAND_WITHOUT_COURT,\r\n"
						+ "SUM(ROUND(NVL(CCLPC,0))) COURT_LPC ,\r\n" + "SUM(ROUND(NVL(DRJ,0))) DR_RJ,\r\n"
						+ "SUM(ROUND(NVL(CRJ,0))) CR_RJ,\r\n"
						+ "SUM(ROUND(NVL(RJ_CCLPC,0)) + ROUND(NVL(RJ_OTH,0))) COURT_RJ,\r\n"
						+ "SUM(ROUND(NVL(TOT_PAY,0))) COLLECTION,\r\n"
						+ "SUM(ROUND(NVL(CBTOT,0))) CB_OTHERTHAN_COURT,\r\n"
						+ "SUM(ROUND(NVL(CB_CCLPC,0))+ROUND(NVL(CB_OTH,0))) CB_COURT,\r\n"
						+ "SUM(ROUND(NVL(CBTOT,0))+ROUND(NVL(CB_CCLPC,0))+ROUND(NVL(CB_OTH,0))) TOTAL_CB,\r\n"
						+ "SUM(ROUND(NVL(CB_SD,0))) CB_SD \r\n" + "from cons,ledger_ht_hist L\r\n"
						+ "where  USCNO=CTUSCNO    and SUBSTR(CTUSCNO,1,3) IN('GNT','VJA','ONG','CRD') \r\n" + constring
						+ "and MON_YEAR = ? GROUP BY substr(ctuscno,1,3),DECODE(CTGOVT_PVT,'Y','GOVT','NON-GOVT'),CTCAT ORDER BY CIRCLE)\r\n"
						+ "GROUP BY CUBE (CIRCLE,TYPE,CTCAT)\r\n" + "ORDER BY \r\n"
						+ "case when CIRCLE = 'VJA' then '001' when CIRCLE = 'GNT' then '002' when CIRCLE = 'ONG' then '003'  when CIRCLE = 'CRD' then '009' else CIRCLE end,case when TYPE='GOVT' then '001' when TYPE = 'NON-GOVT' then '002' else TYPE end,CTCAT\r\n";
				log.info(sql);
				return jdbcTemplate.queryForList(sql, new Object[] { monthYear });
			} catch (DataAccessException e) {
				e.printStackTrace();
				log.error(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}
		} else {
			try {
				String sql = " select * from (select  CIRCLE,nvl(TYPE,'TOTAL') TYPE, nvl(CIRCLE,'APCPDCL')||nvl(TYPE,'TOTAL') CIR_TYPE ,nvl(CTCAT,'TOTAL') CTCAT,SUM(NOS) NOS,sum(OB_OTHERTHAN_COURT) OB_OTHERTHAN_COURT,sum(OB_COURT) OB_COURT,sum(TOT_OB) TOT_OB,\r\n"
						+ "sum(DEMAND_WITHOUT_COURT) DEMAND_WITHOUT_COURT ,sum(COURT_LPC) COURT_LPC,sum(DR_RJ) DR_RJ,sum(CR_RJ) CR_RJ,sum(COURT_RJ) COURT_RJ ,sum(COLLECTION) COLLECTION, \r\n"
						+ "sum(CB_OTHERTHAN_COURT) CB_OTHERTHAN_COURT ,sum(CB_COURT) CB_COURT ,sum(TOTAL_CB) TOTAL_CB,sum(CB_SD) CB_SD\r\n"
						+ "from (\r\n" + "select\r\n"
						+ "substr(ctuscno,1,3) CIRCLE ,DECODE(CTGOVT_PVT,'Y','GOVT','NON-GOVT') TYPE,CTCAT,count(*) NOS,\r\n"
						+ "SUM(ROUND(NVL(TOT_OB,0))) OB_OTHERTHAN_COURT,\r\n"
						+ "SUM(ROUND(NVL(OB_OTH,0)) + ROUND(NVL(OB_CCLPC,0))) OB_COURT,\r\n"
						+ "SUM(ROUND(NVL(OB_OTH,0)) + ROUND(NVL(OB_CCLPC,0))+ ROUND(NVL(TOT_OB,0))) TOT_OB,\r\n"
						+ "SUM(ROUND(NVL(CMD,0))) DEMAND_WITHOUT_COURT,\r\n"
						+ "SUM(ROUND(NVL(CCLPC,0))) COURT_LPC ,\r\n" + "SUM(ROUND(NVL(DRJ,0))) DR_RJ,\r\n"
						+ "SUM(ROUND(NVL(CRJ,0))) CR_RJ,\r\n"
						+ "SUM(ROUND(NVL(RJ_CCLPC,0)) + ROUND(NVL(RJ_OTH,0))) COURT_RJ,\r\n"
						+ "SUM(ROUND(NVL(TOT_PAY,0))) COLLECTION,\r\n"
						+ "SUM(ROUND(NVL(CBTOT,0))) CB_OTHERTHAN_COURT,\r\n"
						+ "SUM(ROUND(NVL(CB_CCLPC,0))+ROUND(NVL(CB_OTH,0))) CB_COURT,\r\n"
						+ "SUM(ROUND(NVL(CBTOT,0))+ROUND(NVL(CB_CCLPC,0))+ROUND(NVL(CB_OTH,0))) TOTAL_CB,\r\n"
						+ "SUM(ROUND(NVL(CB_SD,0))) CB_SD \r\n" + "from cons,ledger_ht_hist L\r\n"
						+ "where  USCNO=CTUSCNO    and SUBSTR(CTUSCNO,1,3) IN('GNT','VJA','ONG','CRD')\r\n" + constring
						+ "and MON_YEAR = ? and SUBSTR(CTUSCNO,1,3)=? GROUP BY substr(ctuscno,1,3),DECODE(CTGOVT_PVT,'Y','GOVT','NON-GOVT'),CTCAT ORDER BY CIRCLE)\r\n"
						+ "GROUP BY CUBE (CIRCLE,TYPE,CTCAT)\r\n" + "ORDER BY \r\n"
						+ "case when CIRCLE = 'VJA' then '001' when CIRCLE = 'GNT' then '002' when CIRCLE = 'ONG' then '003'  when CIRCLE = 'CRD' then '009' else CIRCLE end,case when TYPE='GOVT' then '001' when TYPE = 'NON-GOVT' then '002' else TYPE end,CTCAT ) \r\n "
						+ "  where  circle is not null  ";

				log.info(sql);
				return jdbcTemplate.queryForList(sql, new Object[] { monthYear, circle });
			} catch (DataAccessException e) {
				e.printStackTrace();
				log.error(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}

		}
	}

	public List<Map<String, Object>> getDivLedgerCatAbstract(String circle, String monthYear, String status) {
		String constring = status.equals("LB") ? " "
				: status.equals("L") ? " AND nvl(status,'BS')  in ('LIVE','1','NEW','UDC') "
						: " AND nvl(status,'BS') not in ('LIVE','1','NEW','UDC') ";

		if (circle.equalsIgnoreCase("ALL")) {
			try {
				String sql = " select nvl(CIRCLE,'APCPDCL') CIRCLE,nvl(TYPE,'TOTAL') TYPE, nvl(CIRCLE,'APCPDCL')||nvl(TYPE,'TOTAL') CIR_TYPE ,nvl(CTCAT,'TOTAL') CTCAT,SUM(NOS) NOS,sum(OB_OTHERTHAN_COURT) OB_OTHERTHAN_COURT,sum(OB_COURT) OB_COURT,sum(TOT_OB) TOT_OB,\r\n"
						+ "sum(DEMAND_WITHOUT_COURT) DEMAND_WITHOUT_COURT ,sum(COURT_LPC) COURT_LPC,sum(DR_RJ) DR_RJ,sum(CR_RJ) CR_RJ,sum(COURT_RJ) COURT_RJ ,sum(COLLECTION) COLLECTION, \r\n"
						+ "sum(CB_OTHERTHAN_COURT) CB_OTHERTHAN_COURT ,sum(CB_COURT) CB_COURT ,sum(TOTAL_CB) TOTAL_CB,sum(CB_SD) CB_SD\r\n"
						+ "from (\r\n" + "select\r\n"
						+ "substr(ctuscno,1,3) CIRCLE ,DECODE(CTGOVT_PVT,'Y','GOVT','NON-GOVT') TYPE,CTCAT,count(*) NOS,\r\n"
						+ "SUM(ROUND(NVL(TOT_OB,0))) OB_OTHERTHAN_COURT,\r\n"
						+ "SUM(ROUND(NVL(OB_OTH,0)) + ROUND(NVL(OB_CCLPC,0))) OB_COURT,\r\n"
						+ "SUM(ROUND(NVL(OB_OTH,0)) + ROUND(NVL(OB_CCLPC,0))+ ROUND(NVL(TOT_OB,0))) TOT_OB,\r\n"
						+ "SUM(ROUND(NVL(CMD,0))) DEMAND_WITHOUT_COURT,\r\n"
						+ "SUM(ROUND(NVL(CCLPC,0))) COURT_LPC ,\r\n" + "SUM(ROUND(NVL(DRJ,0))) DR_RJ,\r\n"
						+ "SUM(ROUND(NVL(CRJ,0))) CR_RJ,\r\n"
						+ "SUM(ROUND(NVL(RJ_CCLPC,0)) + ROUND(NVL(RJ_OTH,0))) COURT_RJ,\r\n"
						+ "SUM(ROUND(NVL(TOT_PAY,0))) COLLECTION,\r\n"
						+ "SUM(ROUND(NVL(CBTOT,0))) CB_OTHERTHAN_COURT,\r\n"
						+ "SUM(ROUND(NVL(CB_CCLPC,0))+ROUND(NVL(CB_OTH,0))) CB_COURT,\r\n"
						+ "SUM(ROUND(NVL(CBTOT,0))+ROUND(NVL(CB_CCLPC,0))+ROUND(NVL(CB_OTH,0))) TOTAL_CB,\r\n"
						+ "SUM(ROUND(NVL(CB_SD,0))) CB_SD \r\n" + "from cons,ledger_ht_hist L\r\n"
						+ "where  USCNO=CTUSCNO    and SUBSTR(CTUSCNO,1,3) IN('GNT','VJA','ONG','CRD') \r\n" + constring
						+ "and MON_YEAR = ? GROUP BY substr(ctuscno,1,3),DECODE(CTGOVT_PVT,'Y','GOVT','NON-GOVT'),CTCAT ORDER BY CIRCLE)\r\n"
						+ "GROUP BY CUBE (CIRCLE,TYPE,CTCAT)\r\n" + "ORDER BY \r\n"
						+ "case when CIRCLE = 'VJA' then '001' when CIRCLE = 'GNT' then '002' when CIRCLE = 'ONG' then '003'  when CIRCLE = 'CRD' then '009' else CIRCLE end,case when TYPE='GOVT' then '001' when TYPE = 'NON-GOVT' then '002' else TYPE end,CTCAT\r\n";
				log.info(sql);
				return jdbcTemplate.queryForList(sql, new Object[] { monthYear });
			} catch (DataAccessException e) {
				e.printStackTrace();
				log.error(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}
		} else {
			try {
				String sql = " select * from (select  CIRCLE,nvl(TYPE,'TOTAL') TYPE, nvl(CIRCLE,'APCPDCL')||nvl(TYPE,'TOTAL') CIR_TYPE ,nvl(CTCAT,'TOTAL') CTCAT,SUM(NOS) NOS,sum(OB_OTHERTHAN_COURT) OB_OTHERTHAN_COURT,sum(OB_COURT) OB_COURT,sum(TOT_OB) TOT_OB,\r\n"
						+ "sum(DEMAND_WITHOUT_COURT) DEMAND_WITHOUT_COURT ,sum(COURT_LPC) COURT_LPC,sum(DR_RJ) DR_RJ,sum(CR_RJ) CR_RJ,sum(COURT_RJ) COURT_RJ ,sum(COLLECTION) COLLECTION, \r\n"
						+ "sum(CB_OTHERTHAN_COURT) CB_OTHERTHAN_COURT ,sum(CB_COURT) CB_COURT ,sum(TOTAL_CB) TOTAL_CB,sum(CB_SD) CB_SD\r\n"
						+ "from (\r\n" + "select\r\n"
						+ "DIVNAME CIRCLE ,DECODE(CTGOVT_PVT,'Y','GOVT','NON-GOVT') TYPE,CTCAT,count(*) NOS,\r\n"
						+ "SUM(ROUND(NVL(TOT_OB,0))) OB_OTHERTHAN_COURT,\r\n"
						+ "SUM(ROUND(NVL(OB_OTH,0)) + ROUND(NVL(OB_CCLPC,0))) OB_COURT,\r\n"
						+ "SUM(ROUND(NVL(OB_OTH,0)) + ROUND(NVL(OB_CCLPC,0))+ ROUND(NVL(TOT_OB,0))) TOT_OB,\r\n"
						+ "SUM(ROUND(NVL(CMD,0))) DEMAND_WITHOUT_COURT,\r\n"
						+ "SUM(ROUND(NVL(CCLPC,0))) COURT_LPC ,\r\n" + "SUM(ROUND(NVL(DRJ,0))) DR_RJ,\r\n"
						+ "SUM(ROUND(NVL(CRJ,0))) CR_RJ,\r\n"
						+ "SUM(ROUND(NVL(RJ_CCLPC,0)) + ROUND(NVL(RJ_OTH,0))) COURT_RJ,\r\n"
						+ "SUM(ROUND(NVL(TOT_PAY,0))) COLLECTION,\r\n"
						+ "SUM(ROUND(NVL(CBTOT,0))) CB_OTHERTHAN_COURT,\r\n"
						+ "SUM(ROUND(NVL(CB_CCLPC,0))+ROUND(NVL(CB_OTH,0))) CB_COURT,\r\n"
						+ "SUM(ROUND(NVL(CBTOT,0))+ROUND(NVL(CB_CCLPC,0))+ROUND(NVL(CB_OTH,0))) TOTAL_CB,\r\n"
						+ "SUM(ROUND(NVL(CB_SD,0))) CB_SD \r\n" + "from cons,master.spdclmaster,ledger_ht_hist L\r\n"
						+ "where  USCNO=CTUSCNO   " + " and SUBSTR(CTSECCD,-5)=SECCD(+) "
						+ "and SUBSTR(CTUSCNO,1,3) IN('GNT','VJA','ONG','CRD')\r\n" + constring
						+ "and MON_YEAR = ? and SUBSTR(CTUSCNO,1,3)=? GROUP BY DIVNAME,DECODE(CTGOVT_PVT,'Y','GOVT','NON-GOVT'),CTCAT ORDER BY CIRCLE)\r\n"
						+ "GROUP BY CUBE (CIRCLE,TYPE,CTCAT)\r\n" + "ORDER BY \r\n"
						+ "case when CIRCLE = 'VJA' then '001' when CIRCLE = 'GNT' then '002' when CIRCLE = 'ONG' then '003'  when CIRCLE = 'CRD' then '009' else CIRCLE end,case when TYPE='GOVT' then '001' when TYPE = 'NON-GOVT' then '002' else TYPE end,CTCAT ) \r\n "
						+ "  where  circle is not null  ";

				log.info(sql);
				return jdbcTemplate.queryForList(sql, new Object[] { monthYear, circle });
			} catch (DataAccessException e) {
				e.printStackTrace();
				log.error(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}

		}
	}

	public List<Map<String, Object>> getSubDivLedgerCatAbstract(String circle, String monthYear, String status) {
		String constring = status.equals("LB") ? " "
				: status.equals("L") ? " AND nvl(status,'BS')  in ('LIVE','1','NEW','UDC') "
						: " AND nvl(status,'BS') not in ('LIVE','1','NEW','UDC') ";

		if (circle.equalsIgnoreCase("ALL")) {
			try {
				String sql = " select nvl(CIRCLE,'APCPDCL') CIRCLE,nvl(TYPE,'TOTAL') TYPE, nvl(CIRCLE,'APCPDCL')||nvl(TYPE,'TOTAL') CIR_TYPE ,nvl(CTCAT,'TOTAL') CTCAT,SUM(NOS) NOS,sum(OB_OTHERTHAN_COURT) OB_OTHERTHAN_COURT,sum(OB_COURT) OB_COURT,sum(TOT_OB) TOT_OB,\r\n"
						+ "sum(DEMAND_WITHOUT_COURT) DEMAND_WITHOUT_COURT ,sum(COURT_LPC) COURT_LPC,sum(DR_RJ) DR_RJ,sum(CR_RJ) CR_RJ,sum(COURT_RJ) COURT_RJ ,sum(COLLECTION) COLLECTION, \r\n"
						+ "sum(CB_OTHERTHAN_COURT) CB_OTHERTHAN_COURT ,sum(CB_COURT) CB_COURT ,sum(TOTAL_CB) TOTAL_CB,sum(CB_SD) CB_SD\r\n"
						+ "from (\r\n" + "select\r\n"
						+ "substr(ctuscno,1,3) CIRCLE ,DECODE(CTGOVT_PVT,'Y','GOVT','NON-GOVT') TYPE,CTCAT,count(*) NOS,\r\n"
						+ "SUM(ROUND(NVL(TOT_OB,0))) OB_OTHERTHAN_COURT,\r\n"
						+ "SUM(ROUND(NVL(OB_OTH,0)) + ROUND(NVL(OB_CCLPC,0))) OB_COURT,\r\n"
						+ "SUM(ROUND(NVL(OB_OTH,0)) + ROUND(NVL(OB_CCLPC,0))+ ROUND(NVL(TOT_OB,0))) TOT_OB,\r\n"
						+ "SUM(ROUND(NVL(CMD,0))) DEMAND_WITHOUT_COURT,\r\n"
						+ "SUM(ROUND(NVL(CCLPC,0))) COURT_LPC ,\r\n" + "SUM(ROUND(NVL(DRJ,0))) DR_RJ,\r\n"
						+ "SUM(ROUND(NVL(CRJ,0))) CR_RJ,\r\n"
						+ "SUM(ROUND(NVL(RJ_CCLPC,0)) + ROUND(NVL(RJ_OTH,0))) COURT_RJ,\r\n"
						+ "SUM(ROUND(NVL(TOT_PAY,0))) COLLECTION,\r\n"
						+ "SUM(ROUND(NVL(CBTOT,0))) CB_OTHERTHAN_COURT,\r\n"
						+ "SUM(ROUND(NVL(CB_CCLPC,0))+ROUND(NVL(CB_OTH,0))) CB_COURT,\r\n"
						+ "SUM(ROUND(NVL(CBTOT,0))+ROUND(NVL(CB_CCLPC,0))+ROUND(NVL(CB_OTH,0))) TOTAL_CB,\r\n"
						+ "SUM(ROUND(NVL(CB_SD,0))) CB_SD \r\n" + "from cons,ledger_ht_hist L\r\n"
						+ "where  USCNO=CTUSCNO    and SUBSTR(CTUSCNO,1,3) IN('GNT','VJA','ONG','CRD') \r\n" + constring
						+ "and MON_YEAR = ? GROUP BY substr(ctuscno,1,3),DECODE(CTGOVT_PVT,'Y','GOVT','NON-GOVT'),CTCAT ORDER BY CIRCLE)\r\n"
						+ "GROUP BY CUBE (CIRCLE,TYPE,CTCAT)\r\n" + "ORDER BY \r\n"
						+ "case when CIRCLE = 'VJA' then '001' when CIRCLE = 'GNT' then '002' when CIRCLE = 'ONG' then '003'  when CIRCLE = 'CRD' then '009' else CIRCLE end,case when TYPE='GOVT' then '001' when TYPE = 'NON-GOVT' then '002' else TYPE end,CTCAT\r\n";
				log.info(sql);
				return jdbcTemplate.queryForList(sql, new Object[] { monthYear });
			} catch (DataAccessException e) {
				e.printStackTrace();
				log.error(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}
		} else {
			try {
				String sql = " select * from (select  CIRCLE,nvl(TYPE,'TOTAL') TYPE, nvl(CIRCLE,'APCPDCL')||nvl(TYPE,'TOTAL') CIR_TYPE ,nvl(CTCAT,'TOTAL') CTCAT,SUM(NOS) NOS,sum(OB_OTHERTHAN_COURT) OB_OTHERTHAN_COURT,sum(OB_COURT) OB_COURT,sum(TOT_OB) TOT_OB,\r\n"
						+ "sum(DEMAND_WITHOUT_COURT) DEMAND_WITHOUT_COURT ,sum(COURT_LPC) COURT_LPC,sum(DR_RJ) DR_RJ,sum(CR_RJ) CR_RJ,sum(COURT_RJ) COURT_RJ ,sum(COLLECTION) COLLECTION, \r\n"
						+ "sum(CB_OTHERTHAN_COURT) CB_OTHERTHAN_COURT ,sum(CB_COURT) CB_COURT ,sum(TOTAL_CB) TOTAL_CB,sum(CB_SD) CB_SD\r\n"
						+ "from (\r\n" + "select\r\n"
						+ "SUBNAME CIRCLE ,DECODE(CTGOVT_PVT,'Y','GOVT','NON-GOVT') TYPE,CTCAT,count(*) NOS,\r\n"
						+ "SUM(ROUND(NVL(TOT_OB,0))) OB_OTHERTHAN_COURT,\r\n"
						+ "SUM(ROUND(NVL(OB_OTH,0)) + ROUND(NVL(OB_CCLPC,0))) OB_COURT,\r\n"
						+ "SUM(ROUND(NVL(OB_OTH,0)) + ROUND(NVL(OB_CCLPC,0))+ ROUND(NVL(TOT_OB,0))) TOT_OB,\r\n"
						+ "SUM(ROUND(NVL(CMD,0))) DEMAND_WITHOUT_COURT,\r\n"
						+ "SUM(ROUND(NVL(CCLPC,0))) COURT_LPC ,\r\n" + "SUM(ROUND(NVL(DRJ,0))) DR_RJ,\r\n"
						+ "SUM(ROUND(NVL(CRJ,0))) CR_RJ,\r\n"
						+ "SUM(ROUND(NVL(RJ_CCLPC,0)) + ROUND(NVL(RJ_OTH,0))) COURT_RJ,\r\n"
						+ "SUM(ROUND(NVL(TOT_PAY,0))) COLLECTION,\r\n"
						+ "SUM(ROUND(NVL(CBTOT,0))) CB_OTHERTHAN_COURT,\r\n"
						+ "SUM(ROUND(NVL(CB_CCLPC,0))+ROUND(NVL(CB_OTH,0))) CB_COURT,\r\n"
						+ "SUM(ROUND(NVL(CBTOT,0))+ROUND(NVL(CB_CCLPC,0))+ROUND(NVL(CB_OTH,0))) TOTAL_CB,\r\n"
						+ "SUM(ROUND(NVL(CB_SD,0))) CB_SD \r\n" + "from cons,master.spdclmaster,ledger_ht_hist L\r\n"
						+ "where  USCNO=CTUSCNO   " + " and SUBSTR(CTSECCD,-5)=SECCD(+) "
						+ "and SUBSTR(CTUSCNO,1,3) IN('GNT','VJA','ONG','CRD')\r\n" + constring
						+ "and MON_YEAR = ? and DIVNAME=? GROUP BY SUBNAME,DECODE(CTGOVT_PVT,'Y','GOVT','NON-GOVT'),CTCAT ORDER BY CIRCLE)\r\n"
						+ "GROUP BY CUBE (CIRCLE,TYPE,CTCAT)\r\n" + "ORDER BY \r\n"
						+ "case when CIRCLE = 'VJA' then '001' when CIRCLE = 'GNT' then '002' when CIRCLE = 'ONG' then '003'  when CIRCLE = 'CRD' then '009' else CIRCLE end,case when TYPE='GOVT' then '001' when TYPE = 'NON-GOVT' then '002' else TYPE end,CTCAT ) \r\n "
						+ "  where  circle is not null  ";

				log.info(sql);
				return jdbcTemplate.queryForList(sql, new Object[] { monthYear, circle });
			} catch (DataAccessException e) {
				e.printStackTrace();
				log.error(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}

		}
	}

	public List<Map<String, Object>> getACDCollectionAbstract(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		String fy = request.getParameter("year");

		if (circle.equalsIgnoreCase("ALL")) {
			try {
				String sql = queryBuilderForACDCollection(getDatesAndMonths(fy, "MAY"));
				log.info(sql);
				return jdbcTemplate.queryForList(sql, new Object[] {});
			} catch (DataAccessException e) {
				e.printStackTrace();
				log.error(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}
		} else {
			try {
				String sql = " select * from (  " + queryBuilderForACDCollection(getDatesAndMonths(fy, "MAY"))
						+ "  \r\n " + " ) where  circle !='APCPDCL' and circle= ? ";

				log.info(sql);
				return jdbcTemplate.queryForList(sql, new Object[] { circle });
			} catch (DataAccessException e) {
				e.printStackTrace();
				log.error(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}

		}
	}

	public List<Map<String, Object>> getTrueUpAbstract(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		String fy = request.getParameter("year");

		if (circle.equalsIgnoreCase("ALL")) {
			try {
				String sql = queryBuilderForTrueUpCharges(getDatesAndMonths(fy, "APR"), fy);
				log.info(sql);
				return jdbcTemplate.queryForList(sql, new Object[] {});
			} catch (DataAccessException e) {
				e.printStackTrace();
				log.error(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}
		} else {
			try {
				String sql = " select * from (  " + queryBuilderForTrueUpCharges(getDatesAndMonths(fy, "APR"), fy)
						+ "  \r\n " + " ) where  circle !='APCPDCL' and circle= ? ";

				log.info(sql);
				return jdbcTemplate.queryForList(sql, new Object[] { circle });
			} catch (DataAccessException e) {
				e.printStackTrace();
				log.error(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}

		}
	}

	public List<Map<String, Object>> getACDAbstract(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		String year = request.getParameter("year");
		String levi_month = request.getParameter("fmonth") + "-" + request.getParameter("fyear");

		if (circle.equalsIgnoreCase("ALL")) {
			try {
				String sql = "select  nvl(CIRCLE,'APCPDCL') CIRCLE,nvl(TYPE,'TOTAL') TYPE, nvl(CIRCLE,'APCPDCL')||nvl(TYPE,'TOTAL') CIR_TYPE ,nvl(CTCAT,'TOTAL') CTCAT,\r\n"
						+ "sum(NOS) NOS,sum(SD_TO_BE_MAINTAINED) SD_TO_BE_MAINTAINED,sum(SD_AVAILABLE) SD_AVAILABLE,sum(ACD_CALCULATED) ACD_CALCULATED ,sum(ISD_ADJ) ISD_ADJ , sum( ACD_BAL_PAY) ACD_BAL_PAY from (\r\n"
						+ "SELECT CASE\r\n" + "    WHEN SUBSTR(CTUSCNO,1,3)='VJA' THEN  'VIJAYAWADA'\r\n"
						+ "    WHEN SUBSTR(CTUSCNO,1,3)='GNT' THEN 'GUNTUR'\r\n"
						+ "    WHEN SUBSTR(CTUSCNO,1,3)='ONG' THEN 'ONGOLE'\r\n"
						+ "    WHEN SUBSTR(CTUSCNO,1,3)='CRD' THEN 'CRDA' END CIRCLE, CASE  WHEN NVL(CTGOVT_PVT,'N')='Y' THEN 'GOVT' ELSE 'NON-GOVT' END  TYPE ,CTCAT ,COUNT(*) NOS,SUM(NVL(REQ_SD,0)) SD_TO_BE_MAINTAINED,SUM(NVL(AVAIL_SD,0)) SD_AVAILABLE,SUM(NVL(NET_ACD,0)) ACD_CALCULATED ,sum(ISD_ADJ) ISD_ADJ , sum( ACD_BAL_PAY) ACD_BAL_PAY FROM ACD_CALC_HT,CONS WHERE USCNO=CTUSCNO\r\n"
						+ " AND LEVI_FLG='Y' AND FIN_YEAR=? AND LEVI_MTH=? GROUP BY CASE\r\n"
						+ "    WHEN SUBSTR(CTUSCNO,1,3)='VJA' THEN  'VIJAYAWADA'\r\n"
						+ "    WHEN SUBSTR(CTUSCNO,1,3)='GNT' THEN 'GUNTUR'\r\n"
						+ "    WHEN SUBSTR(CTUSCNO,1,3)='ONG' THEN 'ONGOLE'\r\n"
						+ "    WHEN SUBSTR(CTUSCNO,1,3)='CRD' THEN 'CRDA' END ,CTCAT, CASE  WHEN NVL(CTGOVT_PVT,'N')='Y' THEN 'GOVT' ELSE 'NON-GOVT' END)\r\n"
						+ "    GROUP BY\r\n" + "    CUBE(CIRCLE,TYPE,CTCAT)\r\n" + "    ORDER BY\r\n"
						+ "    case when CIRCLE = 'VIJAYAWADA' then '001' when CIRCLE = 'GUNTUR' then '002' when CIRCLE = 'ONGOLE' then '003'  when CIRCLE = 'CRDA' then '009' else CIRCLE end,case when TYPE='GOVT' then '001' when TYPE = 'NON-GOVT' then '002' else TYPE end,CTCAT";
				log.info(sql);
				return jdbcTemplate.queryForList(sql, new Object[] { year, levi_month });
			} catch (DataAccessException e) {
				e.printStackTrace();
				log.error(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}
		} else {
			try {

				String sql = " select * from (select   CIRCLE,nvl(TYPE,'TOTAL') TYPE, nvl(CIRCLE,'APCPDCL')||nvl(TYPE,'TOTAL') CIR_TYPE ,nvl(CTCAT,'TOTAL') CTCAT,\r\n"
						+ "sum(NOS) NOS,sum(SD_TO_BE_MAINTAINED) SD_TO_BE_MAINTAINED,sum(SD_AVAILABLE) SD_AVAILABLE,sum(ACD_CALCULATED) ACD_CALCULATED from (\r\n"
						+ "SELECT SUBSTR(CTUSCNO,1,3)  CIRCLE, CASE  WHEN NVL(CTGOVT_PVT,'N')='Y' THEN 'GOVT' ELSE 'NON-GOVT' END  TYPE ,CTCAT ,COUNT(*) NOS,SUM(NVL(REQ_SD,0)) SD_TO_BE_MAINTAINED,SUM(NVL(AVAIL_SD,0)) SD_AVAILABLE,SUM(NVL(NET_ACD,0)) ACD_CALCULATED FROM ACD_CALC_HT,CONS WHERE USCNO=CTUSCNO\r\n"
						+ " AND LEVI_FLG='Y' AND FIN_YEAR=? AND LEVI_MTH=? AND SUBSTR(CTUSCNO,1,3)=? GROUP BY SUBSTR(CTUSCNO,1,3) ,CTCAT, CASE  WHEN NVL(CTGOVT_PVT,'N')='Y' THEN 'GOVT' ELSE 'NON-GOVT' END)\r\n"
						+ "    GROUP BY\r\n" + "    CUBE(CIRCLE,TYPE,CTCAT)\r\n" + "    ORDER BY\r\n"
						+ "    case when CIRCLE = 'VJA' then '001' when CIRCLE = 'GNT' then '002' when CIRCLE = 'ONG' then '003'  when CIRCLE = 'CRD' then '009' else CIRCLE end,case when TYPE='GOVT' then '001' when TYPE = 'NON-GOVT' then '002' else TYPE end,CTCAT \r\n"
						+ "	) where  circle is not null ";
				log.info(sql);
				return jdbcTemplate.queryForList(sql, new Object[] { year, levi_month, circle });
			} catch (DataAccessException e) {
				e.printStackTrace();
				log.error(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}

		}
	}

	public List<Map<String, Object>> getACDAbstract2022(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		String year = request.getParameter("year");
		String levi_month = request.getParameter("fmonth") + "-" + request.getParameter("fyear");

		if (circle.equalsIgnoreCase("ALL")) {
			try {
				String sql = "select  nvl(CIRCLE,'APCPDCL') CIRCLE,nvl(TYPE,'TOTAL') TYPE, nvl(CIRCLE,'APCPDCL')||nvl(TYPE,'TOTAL') CIR_TYPE ,nvl(CTCAT,'TOTAL') CTCAT,\r\n"
						+ "sum(NOS) NOS,sum(SD_TO_BE_MAINTAINED) SD_TO_BE_MAINTAINED,sum(SD_AVAILABLE) SD_AVAILABLE,sum(ACD_CALCULATED) ACD_CALCULATED from (\r\n"
						+ "SELECT CASE\r\n" + "    WHEN SUBSTR(CTUSCNO,1,3)='VJA' THEN  'VIJAYAWADA'\r\n"
						+ "    WHEN SUBSTR(CTUSCNO,1,3)='GNT' THEN 'GUNTUR'\r\n"
						+ "    WHEN SUBSTR(CTUSCNO,1,3)='ONG' THEN 'ONGOLE'\r\n"
						+ "    WHEN SUBSTR(CTUSCNO,1,3)='CRD' THEN 'CRDA' END CIRCLE, CASE  WHEN NVL(CTGOVT_PVT,'N')='Y' THEN 'GOVT' ELSE 'NON-GOVT' END  TYPE ,CTCAT ,COUNT(*) NOS,SUM(NVL(REQ_SD,0)) SD_TO_BE_MAINTAINED,SUM(NVL(AVAIL_SD,0)) SD_AVAILABLE,SUM(NVL(NET_ACD,0)) ACD_CALCULATED FROM ACD_CALC_HT_revised,CONS WHERE USCNO=CTUSCNO\r\n"
						+ " AND LEVI_FLG='Y' AND FIN_YEAR=? AND LEVI_MTH=? GROUP BY CASE\r\n"
						+ "    WHEN SUBSTR(CTUSCNO,1,3)='VJA' THEN  'VIJAYAWADA'\r\n"
						+ "    WHEN SUBSTR(CTUSCNO,1,3)='GNT' THEN 'GUNTUR'\r\n"
						+ "    WHEN SUBSTR(CTUSCNO,1,3)='ONG' THEN 'ONGOLE'\r\n"
						+ "    WHEN SUBSTR(CTUSCNO,1,3)='CRD' THEN 'CRDA' END ,CTCAT, CASE  WHEN NVL(CTGOVT_PVT,'N')='Y' THEN 'GOVT' ELSE 'NON-GOVT' END)\r\n"
						+ "    GROUP BY\r\n" + "    CUBE(CIRCLE,TYPE,CTCAT)\r\n" + "    ORDER BY\r\n"
						+ "    case when CIRCLE = 'VIJAYAWADA' then '001' when CIRCLE = 'GUNTUR' then '002' when CIRCLE = 'ONGOLE' then '003'  when CIRCLE = 'CRDA' then '009' else CIRCLE end,case when TYPE='GOVT' then '001' when TYPE = 'NON-GOVT' then '002' else TYPE end,CTCAT";
				log.info(sql);
				return jdbcTemplate.queryForList(sql, new Object[] { year, levi_month });
			} catch (DataAccessException e) {
				e.printStackTrace();
				log.error(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}
		} else {
			try {

				String sql = " select * from (select   CIRCLE,nvl(TYPE,'TOTAL') TYPE, nvl(CIRCLE,'APCPDCL')||nvl(TYPE,'TOTAL') CIR_TYPE ,nvl(CTCAT,'TOTAL') CTCAT,\r\n"
						+ "sum(NOS) NOS,sum(SD_TO_BE_MAINTAINED) SD_TO_BE_MAINTAINED,sum(SD_AVAILABLE) SD_AVAILABLE,sum(ACD_CALCULATED) ACD_CALCULATED from (\r\n"
						+ "SELECT SUBSTR(CTUSCNO,1,3)  CIRCLE, CASE  WHEN NVL(CTGOVT_PVT,'N')='Y' THEN 'GOVT' ELSE 'NON-GOVT' END  TYPE ,CTCAT ,COUNT(*) NOS,SUM(NVL(REQ_SD,0)) SD_TO_BE_MAINTAINED,SUM(NVL(AVAIL_SD,0)) SD_AVAILABLE,SUM(NVL(NET_ACD,0)) ACD_CALCULATED FROM ACD_CALC_HT_revised,CONS WHERE USCNO=CTUSCNO\r\n"
						+ " AND LEVI_FLG='Y' AND FIN_YEAR=? AND LEVI_MTH=? AND SUBSTR(CTUSCNO,1,3)=? GROUP BY SUBSTR(CTUSCNO,1,3) ,CTCAT, CASE  WHEN NVL(CTGOVT_PVT,'N')='Y' THEN 'GOVT' ELSE 'NON-GOVT' END)\r\n"
						+ "    GROUP BY\r\n" + "    CUBE(CIRCLE,TYPE,CTCAT)\r\n" + "    ORDER BY\r\n"
						+ "    case when CIRCLE = 'VJA' then '001' when CIRCLE = 'GNT' then '002' when CIRCLE = 'ONG' then '003'  when CIRCLE = 'CRD' then '009' else CIRCLE end,case when TYPE='GOVT' then '001' when TYPE = 'NON-GOVT' then '002' else TYPE end,CTCAT \r\n"
						+ "	) where  circle is not null ";
				log.info(sql);
				return jdbcTemplate.queryForList(sql, new Object[] { year, levi_month, circle });
			} catch (DataAccessException e) {
				e.printStackTrace();
				log.error(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}

		}
	}

	public List<Map<String, Object>> getTDSAbstract(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		String year = request.getParameter("year");

		if (circle.equalsIgnoreCase("ALL")) {
			try {
				String sql = " select    nvl(CIRCLE,'APCPDCL') CIRCLE,nvl(TYPE,'TOTAL') TYPE, nvl(CIRCLE,'APCPDCL')||nvl(TYPE,'TOTAL') CIR_TYPE ,nvl(CTCAT,'TOTAL') CTCAT,count(*) NOS,SUM(OB_SD) OB_SD,SUM(PAID_ACD) PAID_ACD,SUM(RJ_SD)RJ_SD,SUM(CB_SD) CB_SD,SUM(TOT_ISD) TOT_ISD,SUM(TDS_AMT)TDS_AMT,SUM(NET_ISD) NET_ISD from (                        \r\n"
						+ "SELECT substr(ISCNO,1,3) CIRCLE ,DECODE(CTGOVT_PVT,'Y','GOVT','NON-GOVT') TYPE,CTCAT,NVL(OB_SD,0)OB_SD,NVL(PAID_ACD,0)PAID_ACD,NVL(RJ_SD,0)RJ_SD,NVL(CB_SD,0)CB_SD, NVL(TOT_ISD,0)TOT_ISD ,NVL(TDS_AMT,0)TDS_AMT,NVL(NET_ISD,0)NET_ISD\r\n"
						+ "FROM SD_LEDGER_TEMP,CONS\r\n"
						+ "WHERE CTUSCNO=ISCNO AND FIN_YEAR=? AND TDS_AMT!=0 AND CTSTATUS!=0  AND LEVI_FLG='P' ORDER BY CTCAT)\r\n"
						+ "GROUP BY \r\n" + "    CUBE(CIRCLE,TYPE,CTCAT)\r\n" + "ORDER BY \r\n"
						+ "case when CIRCLE = 'VJA' then '001' when CIRCLE = 'GNT' then '002' when CIRCLE = 'ONG' then '003'  when CIRCLE = 'CRD' then '009' else CIRCLE end,case when TYPE='GOVT' then '001' when TYPE = 'NON-GOVT' then '002' else TYPE end,CTCAT";
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

				String sql = " select * from ( select  CIRCLE,nvl(TYPE,'TOTAL') TYPE,nvl(CIRCLE,'APCPDCL')||nvl(TYPE,'TOTAL') CIR_TYPE  ,nvl(CTCAT,'TOTAL') CTCAT,count(*) NOS,SUM(OB_SD) OB_SD,SUM(PAID_ACD) PAID_ACD,SUM(RJ_SD)RJ_SD,SUM(CB_SD) CB_SD,SUM(TOT_ISD) TOT_ISD,SUM(TDS_AMT)TDS_AMT,SUM(NET_ISD) NET_ISD from (                        \r\n"
						+ " SELECT substr(ISCNO,1,3) CIRCLE ,DECODE(CTGOVT_PVT,'Y','GOVT','NON-GOVT') TYPE,CTCAT,NVL(OB_SD,0)OB_SD,NVL(PAID_ACD,0)PAID_ACD,NVL(RJ_SD,0)RJ_SD,NVL(CB_SD,0)CB_SD, NVL(TOT_ISD,0)TOT_ISD ,NVL(TDS_AMT,0)TDS_AMT,NVL(NET_ISD,0)NET_ISD\r\n"
						+ " FROM SD_LEDGER_TEMP,CONS\r\n"
						+ " WHERE CTUSCNO=ISCNO AND FIN_YEAR=? AND  substr(ISCNO,1,3) = ? AND TDS_AMT!=0 AND CTSTATUS!=0  AND LEVI_FLG='P' ORDER BY CTCAT)\r\n"
						+ " GROUP BY \r\n" + " CUBE(CIRCLE,TYPE,CTCAT)\r\n" + " ORDER BY \r\n"
						+ " case when CIRCLE = 'VJA' then '001' when CIRCLE = 'GNT' then '002' when CIRCLE = 'ONG' then '003'  when CIRCLE = 'CRD' then '009' else CIRCLE end,case when TYPE='GOVT' then '001' when TYPE = 'NON-GOVT' then '002' else TYPE end,CTCAT ) where  circle is not null ";
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

	public List<Map<String, Object>> getEDAbstract(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		String levi_month = "01-" + request.getParameter("fmonth") + "-" + request.getParameter("fyear");
		System.out.println(levi_month);
		String edflag = request.getParameter("edflag");
		System.out.println(edflag);
		if (circle.equalsIgnoreCase("ALL")) {
			try {
				String sql = "select * from \n"
						+ "(select to_char(BTBLDT,'MON-YYYY') BDT,NVL(substr(BTSCNO,1,3),'TOTAL') CIRCLE,sum(BTBKVAH) KVAH,sum(BTED) ED_AMT from \n"
						+ "(select * from bill_hist union all select * from bill) ,cons \n" + "where BTSCNO=CTUSCNO \n"
						+ "and NVL(CTEDFLAG,'N')=? \n" + "and BTBLDT >=  to_date(?,'DD-MON-YYYY') \n"
						+ "group by ROLLUP(to_char(BTBLDT,'MON-YYYY'),substr(BTSCNO,1,3)) \n"
						+ "order by TO_DATE(BDT,'MON-YYYY'), \n"
						+ "case when CIRCLE = 'VJA' then '001' when CIRCLE = 'GNT' then '002' when CIRCLE = 'ONG' then '003'   when CIRCLE = 'CRD'  then '009' else CIRCLE end ) where BDT IS NOT NULL";
				log.info(sql);
				return jdbcTemplate.queryForList(sql, new Object[] { edflag, levi_month });
			} catch (DataAccessException e) {
				e.printStackTrace();
				log.error(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}
		} else {
			try {
				String sql = "select * from ( select  to_char(BTBLDT,'MON-YYYY') BDT,substr(BTSCNO,1,3) CIRCLE,sum(BTEDKVAH) KVAH,sum(BTED) ED_AMT from  (select * from bill_hist union all select * from bill) ,cons where BTSCNO=CTUSCNO\r\n"
						+ "and NVL(CTEDFLAG,'N')=? and BTBLDT >=  to_date(?,'DD-MON-YYYY') and substr(BTSCNO,1,3) = ? group by ROLLUP(to_char(BTBLDT,'MON-YYYY'),substr(BTSCNO,1,3)) order by TO_DATE(BDT,'MON-YYYY'),\r\n"
						+ "case when CIRCLE = 'VJA' then '001' when CIRCLE = 'GNT' then '002' when CIRCLE = 'ONG' then '003'  when CIRCLE = 'CRD' then '009' else CIRCLE end )  where CIRCLE IS NOT NULL";
				log.info(sql);
				return jdbcTemplate.queryForList(sql, new Object[] { edflag, levi_month, circle });
			} catch (DataAccessException e) {
				e.printStackTrace();
				log.error(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}

		}
	}

	public List<Map<String, Object>> getServiceTypeServices(String circle, String year, String servicetype) {

		String fmonthYear = "APR-" + year.split("-")[0];
		String tmonthYear = "MAR-" + year.split("-")[1];
		String emptoyear = tmonthYear.equals("MAR-2024") ? "JAN-2024" : tmonthYear;

		if (circle.equalsIgnoreCase("ALL")) {
			try {
				String sql = "Select ctuscno,ctname,\r\n" + "sum(case when Mon_Year = '" + emptoyear
						+ "' then Round(Nvl(LOAD,0)) else 0 end ) LOAD,\r\n" + "sum(case when Mon_Year = '" + emptoyear
						+ "' then Round(Nvl(REC_MD,0)) else 0 end ) REC_MD,\r\n" + "sum(case when Mon_Year = '"
						+ fmonthYear + "' then Round(Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)) else 0 end ) Ob,\r\n"
						+ "sum(Mn_Kvah) Sales\r\n" + ",sum(round(Nvl(Cmd,0)+Nvl(Cclpc,0))) Demand,\r\n"
						+ "sum(Nvl(round(CASE WHEN Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)>0 THEN CASE WHEN Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)>(NVL(Tot_Pay,0)) THEN (NVL(Tot_Pay,0)) ELSE Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0) END END),0)) COLL_ARREAR,\r\n"
						+ "sum(Nvl(round(CASE WHEN Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)>0 THEN CASE WHEN Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)<(NVL(Tot_Pay,0)) THEN (NVL(Tot_Pay,0)-(Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0))) END ELSE (NVL(Tot_Pay,0)) END ),0)) COLL_DEMAND,\r\n"
						+ "sum(round(Nvl(Tot_Pay,0))) Collection,sum(round(Nvl(Rj_Oth,0)+Nvl(Drj,0)+Nvl(Rj_Cclpc,0))) Drj,sum(round(Nvl(Crj,0))) Crj,"
						+ "sum(case when Mon_Year = '" + fmonthYear
						+ "' then  round(Nvl(Cbtot,0)+Nvl(Cb_Oth,0)+Nvl(Cb_Cclpc,0)) else 0 end) Cb\r\n"
						+ "From (select LHH.*,'' STATUS_NEW, '' GOVT_PVT from Ledger_Ht_HIST LHH where to_date(Mon_Year,'MON-YYYY') between to_date(?,'MON-YYYY') and LAST_DAY(to_date(?,'MON-YYYY')) union all select * from accountcopy where to_date(Mon_Year,'MON-YYYY') between to_date(?,'MON-YYYY') and LAST_DAY(to_date(?,'MON-YYYY'))) A,CONS B,servtype C\r\n"
						+ "Where A.Uscno=B.CTUscno \r\n" + "And b.ctservtype=c.stcode\r\n" + " AND b.CTSERVTYPE =?\r\n"
						+ " group by ctuscno,ctname\r\n" + "Order By 1,2,3,4";
				log.info(sql);
				return jdbcTemplate.queryForList(sql,
						new Object[] { fmonthYear, tmonthYear, fmonthYear, tmonthYear, servicetype });
			} catch (DataAccessException e) {
				e.printStackTrace();
				log.error(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}
		} else {
			try {

				String sql = "Select ctuscno,ctname,\r\n" + "sum(case when Mon_Year = '" + emptoyear
						+ "' then Round(Nvl(LOAD,0)) else 0 end ) LOAD,\r\n" + "sum(case when Mon_Year = '" + emptoyear
						+ "' then Round(Nvl(REC_MD,0)) else 0 end ) REC_MD,\r\n" + "sum(case when Mon_Year = '"
						+ fmonthYear + "' then Round(Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)) else 0 end ) Ob,\r\n"
						+ "sum(Mn_Kvah) Sales\r\n" + ",sum(round(Nvl(Cmd,0)+Nvl(Cclpc,0))) Demand,\r\n"
						+ "sum(Nvl(round(CASE WHEN Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)>0 THEN CASE WHEN Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)>(NVL(Tot_Pay,0)) THEN (NVL(Tot_Pay,0)) ELSE Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0) END END),0)) COLL_ARREAR,\r\n"
						+ "sum(Nvl(round(CASE WHEN Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)>0 THEN CASE WHEN Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)<(NVL(Tot_Pay,0)) THEN (NVL(Tot_Pay,0)-(Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0))) END ELSE (NVL(Tot_Pay,0)) END ),0)) COLL_DEMAND,\r\n"
						+ "sum(round(Nvl(Tot_Pay,0))) Collection,sum(round(Nvl(Rj_Oth,0)+Nvl(Drj,0)+Nvl(Rj_Cclpc,0))) Drj,sum(round(Nvl(Crj,0))) Crj,"
						+ "sum(case when Mon_Year = '" + fmonthYear
						+ "' then  round(Nvl(Cbtot,0)+Nvl(Cb_Oth,0)+Nvl(Cb_Cclpc,0)) else 0 end) Cb\r\n"
						+ "From (select LHH.*,'' STATUS_NEW, '' GOVT_PVT from Ledger_Ht_HIST LHH where to_date(Mon_Year,'MON-YYYY') between to_date(?,'MON-YYYY') and LAST_DAY(to_date(?,'MON-YYYY')) union all select * from accountcopy where to_date(Mon_Year,'MON-YYYY') between to_date(?,'MON-YYYY') and LAST_DAY(to_date(?,'MON-YYYY'))) A,CONS B,servtype C\r\n"
						+ "Where A.Uscno=B.CTUscno \r\n" + "And b.ctservtype=c.stcode\r\n"
						+ " AND b.CTSERVTYPE =?  AND SUBSTR(A.Uscno,1,3) = ?\r\n" + " group by ctuscno,ctname\r\n"
						+ "Order By 1,2,3,4";
				log.info(sql);

				return jdbcTemplate.queryForList(sql,
						new Object[] { fmonthYear, tmonthYear, fmonthYear, tmonthYear, servicetype, circle });
			} catch (DataAccessException e) {
				e.printStackTrace();
				log.error(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}

		}
	}

	public List<Map<String, Object>> getCircleCatWiseTrueUpAbstract(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		String status = request.getParameter("status");
		String constring = status.equals("LB") ? " and status<>2 "
				: status.equals("L") ? "  and status =1  " : "   and status = 0 ";
		if (circle.equalsIgnoreCase("ALL")) {
			try {
				/*
				 * String sql =
				 * "select NVL(CIRCLE,'APCPDCL') CIRCLE, NVL(CAT,'TOTAL') CAT  ,round(sum(TOT_KVAH)) TOT_KVAH, round(sum(TOT_AMT)) TOT_AMT , round(sum(TU_MON_CHG)) TU_MON_CHG from (\r\n"
				 * + "select  CASE\r\n" +
				 * "    WHEN SUBSTR(USCNO,1,3)='VJA' THEN  'VIJAYAWADA'\r\n" +
				 * "    WHEN SUBSTR(USCNO,1,3)='GNT' THEN 'GUNTUR'\r\n" +
				 * "    WHEN SUBSTR(USCNO,1,3)='ONG' THEN 'ONGOLE'\r\n" +
				 * "    WHEN SUBSTR(USCNO,1,3)='CRD' THEN 'CRDA' END CIRCLE,USCNO,CTCAT CAT,TOT_KVAH, TOT_AMT, TU_MON_CHG,  DECODE(CTSTATUS ,1,'LIVE','BILLSTOP') STATUS from ht_trueup_chg,cons where uscno=ctuscno\r\n"
				 * + constring +
				 * "   ) group by cube(CIRCLE,CAT) order by case when CIRCLE='APCPDCL' then 'zzzzzzz' else CIRCLE end,CAT "
				 * ;
				 */
				String sql = "select NVL(CIRCLE,'APCPDCL') CIRCLE, nvl(TYPE,'TOTAL') TYPE,nvl(CIRCLE,'APCPDCL')||nvl(TYPE,'TOTAL') CIR_TYPE, NVL(CAT,'TOTAL') CAT  ,COUNT(*) SCS, round(sum(TOT_KVAH)) TOT_KVAH, round(sum(TOT_AMT)) TOT_AMT , round(sum(TU_MON_CHG)) TU_MON_CHG from (\r\n"
						+ "select  CASE\r\n" + "    WHEN SUBSTR(USCNO,1,3)='VJA' THEN  'VIJAYAWADA'\r\n"
						+ "    WHEN SUBSTR(USCNO,1,3)='GNT' THEN 'GUNTUR'\r\n"
						+ "    WHEN SUBSTR(USCNO,1,3)='ONG' THEN 'ONGOLE'\r\n"
						+ "    WHEN SUBSTR(USCNO,1,3)='CRD' THEN 'CRDA' END CIRCLE,DECODE(CTGOVT_PVT,'Y','GOVT','NON-GOVT') TYPE,USCNO,CTCAT CAT,TOT_KVAH, TOT_AMT, TU_MON_CHG,  DECODE(CTSTATUS ,1,'LIVE','BILLSTOP') STATUS from ht_trueup_chg,cons where uscno=ctuscno  \r\n"
						+ constring
						+ "    ) group by cube(CIRCLE,TYPE,CAT) order by case when CIRCLE='APCPDCL' then 'zzzzzzz' else CIRCLE end,case when TYPE='GOVT' then '001' when TYPE = 'NON-GOVT' then '002' else TYPE end,CAT";
				log.info(sql);
				return jdbcTemplate.queryForList(sql, new Object[] {});
			} catch (DataAccessException e) {
				e.printStackTrace();
				log.error(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}
		} else {
			try {

				String sql = "select * from (   select NVL(CIRCLE,'APCPDCL') CIRCLE, nvl(TYPE,'TOTAL') TYPE,nvl(CIRCLE,'APCPDCL')||nvl(TYPE,'TOTAL') CIR_TYPE, NVL(CAT,'TOTAL') CAT  ,COUNT(*) SCS, round(sum(TOT_KVAH)) TOT_KVAH, round(sum(TOT_AMT)) TOT_AMT , round(sum(TU_MON_CHG)) TU_MON_CHG from (\r\n"
						+ "select  CASE\r\n" + "    WHEN SUBSTR(USCNO,1,3)='VJA' THEN  'VIJAYAWADA'\r\n"
						+ "    WHEN SUBSTR(USCNO,1,3)='GNT' THEN 'GUNTUR'\r\n"
						+ "    WHEN SUBSTR(USCNO,1,3)='ONG' THEN 'ONGOLE'\r\n"
						+ "    WHEN SUBSTR(USCNO,1,3)='CRD' THEN 'CRDA' END CIRCLE,DECODE(CTGOVT_PVT,'Y','GOVT','NON-GOVT') TYPE,USCNO,CTCAT CAT,TOT_KVAH, TOT_AMT, TU_MON_CHG,  DECODE(CTSTATUS ,1,'LIVE','BILLSTOP') STATUS from ht_trueup_chg,cons where uscno=ctuscno and substr(uscno,1,3) = ? \r\n"
						+ constring
						+ "    ) group by cube(CIRCLE,TYPE,CAT) order by case when CIRCLE='APCPDCL' then 'zzzzzzz' else CIRCLE end,case when TYPE='GOVT' then '001' when TYPE = 'NON-GOVT' then '002' else TYPE end,CAT  ) where circle !='APCPDCL'";
				log.info(sql);

				/*
				 * String sql =
				 * "select * from (  select NVL(CIRCLE,'APCPDCL') CIRCLE , NVL(CAT,'TOTAL') CAT  ,round(sum(TOT_KVAH)) TOT_KVAH, round(sum(TOT_AMT)) TOT_AMT , round(sum(TU_MON_CHG)) TU_MON_CHG from (\r\n"
				 * + "select  CASE\r\n" +
				 * "    WHEN SUBSTR(USCNO,1,3)='VJA' THEN  'VIJAYAWADA'\r\n" +
				 * "    WHEN SUBSTR(USCNO,1,3)='GNT' THEN 'GUNTUR'\r\n" +
				 * "    WHEN SUBSTR(USCNO,1,3)='ONG' THEN 'ONGOLE'\r\n" +
				 * "    WHEN SUBSTR(USCNO,1,3)='CRD' THEN 'CRDA' END CIRCLE,USCNO,CTCAT CAT,TOT_KVAH, TOT_AMT, TU_MON_CHG,  DECODE(CTSTATUS ,1,'LIVE','BILLSTOP') STATUS from ht_trueup_chg,cons where uscno=ctuscno and substr(uscno,1,3) = ? \r\n"
				 * + constring +
				 * "   ) group by cube(CIRCLE,CAT) order by case when CIRCLE='APCPDCL' then 'zzzzzzz' else CIRCLE end,CAT ) where circle !='APCPDCL'"
				 * ; log.info(sql);
				 */
				return jdbcTemplate.queryForList(sql, new Object[] { circle });
			} catch (DataAccessException e) {
				e.printStackTrace();
				log.error(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}

		}
	}

	public List<Map<String, Object>> getAgeWiseServicesLedgerAbstractDetails(String circle, String agewise,
			String status, String fmonthYear) {
		String constring = status.equals("LB") ? " "
				: status.equals("L") ? "nvl(status,'BS')  in ('LIVE','1','NEW','UDC')  AND "
						: " nvl(status,'BS') not in ('LIVE','1','NEW','UDC') AND ";

		if (circle.equalsIgnoreCase("APCPDCL")) {
			try {
				String sql = "select * from ( SELECT SUBSTR(CTUSCNO,1,3) CIRNAME,S.CIRNAME S_CIRNAME, S.DIVNAME,S.SUBNAME,S.SECNAME,USCNO,CTNAME,DECODE(CTGOVT_PVT,'Y','GOVT','NON-GOVT') TYPE,CTCAT,to_char(PAY_DT,'mm-dd-yyyy') LAST_PAID_MONTH,\r\n"
						+ "CASE WHEN (MONTHS_BETWEEN(TRUNC(LAST_DAY(to_date(?,'MM-YYYY')),'MM'),PAY_DT)/12)>0 AND (MONTHS_BETWEEN(TRUNC(LAST_DAY(to_date(?,'MM-YYYY')),'MM'),PAY_DT)/12)<=0.5 THEN 'UPTO_6_MONTHS' ELSE \r\n"
						+ "CASE WHEN (MONTHS_BETWEEN(TRUNC(LAST_DAY(to_date(?,'MM-YYYY')),'MM'),PAY_DT)/12)>0.5 AND (MONTHS_BETWEEN(TRUNC(LAST_DAY(to_date(?,'MM-YYYY')),'MM'),PAY_DT)/12)<=1 THEN 'BELOW_1_YR_AND_ABV_6_MONTHS' ELSE \r\n"
						+ "CASE WHEN (MONTHS_BETWEEN(TRUNC(LAST_DAY(to_date(?,'MM-YYYY')),'MM'),PAY_DT)/12)>1 AND (MONTHS_BETWEEN(TRUNC(LAST_DAY(to_date(?,'MM-YYYY')),'MM'),PAY_DT)/12)<=2 THEN 'ABOVE_1_YR' ELSE \r\n"
						+ "CASE WHEN (MONTHS_BETWEEN(TRUNC(LAST_DAY(to_date(?,'MM-YYYY')),'MM'),PAY_DT)/12)>2 AND (MONTHS_BETWEEN(TRUNC(LAST_DAY(to_date(?,'MM-YYYY')),'MM'),PAY_DT)/12)<=3 THEN 'ABOVE_2_YR' ELSE\r\n"
						+ "CASE WHEN (MONTHS_BETWEEN(TRUNC(LAST_DAY(to_date(?,'MM-YYYY')),'MM'),PAY_DT)/12)>3 AND (MONTHS_BETWEEN(TRUNC(LAST_DAY(to_date(?,'MM-YYYY')),'MM'),PAY_DT)/12)<=4 THEN 'ABOVE_3_YR' ELSE\r\n"
						+ "CASE WHEN (MONTHS_BETWEEN(TRUNC(LAST_DAY(to_date(?,'MM-YYYY')),'MM'),PAY_DT)/12)>4 AND (MONTHS_BETWEEN(TRUNC(LAST_DAY(to_date(?,'MM-YYYY')),'MM'),PAY_DT)/12)<=5 THEN 'ABOVE_4_YR' ELSE\r\n"
						+ "CASE WHEN (MONTHS_BETWEEN(TRUNC(LAST_DAY(to_date(?,'MM-YYYY')),'MM'),PAY_DT)/12)>5 THEN 'ABOVE_5_YR' ELSE 'BELOW' END END END END END END END AGE,\r\n"
						+ "\r\n" + "nvl(CB_EC,0)+NVL(CB_FSA,0) CC,\r\n" + "\r\n" + "NVL(CB_ED,0) ED,\r\n"
						+ "NVL(CB_IED,0) EDI,\r\n" + "\r\n" + "NVL(CB_LPC,0)+NVL(CB_CCLPC,0) LPC,\r\n"
						+ "NVL(CB_OTH,0) OTH,\r\n" + "\r\n"
						+ "NVL(CBTOT,0)+NVL(CB_CCLPC,0)+NVL(CB_OTH,0) CB FROM  LEDGER_HT_HIST,CONS,SPDCLMASTER S,\r\n"
						+ "(SELECT USCNO SCNO,MAX(TRUNC(TO_DATE(MON_YEAR,'MON-YYYY'),'MM')) PAY_DT FROM LEDGER_HT_HIST  WHERE "
						+ constring
						+ " SUBSTR(USCNO,1,3) IN('GNT','VJA','ONG','CRD') AND  NVL(TOT_PAY,0)+NVL(CRJ,0)>0 GROUP BY USCNO\r\n"
						+ "UNION\r\n"
						+ "SELECT USCNO SCNO,MIN(TRUNC(TO_DATE(MON_YEAR,'MON-YYYY'),'MM')) PAY_DT FROM LEDGER_HT_HIST  WHERE "
						+ constring
						+ " SUBSTR(USCNO,1,3) IN('GNT','VJA','ONG','CRD') GROUP BY USCNO HAVING SUM(NVL(TOT_PAY,0)+NVL(CRJ,0))=0) \r\n"
						+ "WHERE USCNO=SCNO AND USCNO=CTUSCNO AND TRUNC(TO_DATE(MON_YEAR,'MON-YYYY'),'MM')=ADD_MONTHS(TRUNC(LAST_DAY(to_date(?,'MM-YYYY')),'MM'),-1) and (NVL(CBTOT,0)+NVL(CB_CCLPC,0)+NVL(CB_OTH,0))>1\r\n "
						+ "and SECCD = SUBSTR(CONS.CTSECCD,-5) "
						+ " ORDER BY USCNO,SUBSTR(AGE,1,5),SUBSTR(AGE,7)  DESC )"
						+ (agewise.equals("TOTAL") ? "" : " where age = ?");
				log.info(sql);
				if (agewise.equals("TOTAL"))
					return jdbcTemplate.queryForList(sql,
							new Object[] { fmonthYear, fmonthYear, fmonthYear, fmonthYear, fmonthYear, fmonthYear,
									fmonthYear, fmonthYear, fmonthYear, fmonthYear, fmonthYear, fmonthYear, fmonthYear,
									fmonthYear });
				else
					return jdbcTemplate.queryForList(sql,
							new Object[] { fmonthYear, fmonthYear, fmonthYear, fmonthYear, fmonthYear, fmonthYear,
									fmonthYear, fmonthYear, fmonthYear, fmonthYear, fmonthYear, fmonthYear, fmonthYear,
									fmonthYear, agewise });
			} catch (DataAccessException e) {
				e.printStackTrace();
				log.error(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}
		} else {
			try {
				String sql = "select * from ( SELECT SUBSTR(CTUSCNO,1,3) CIRNAME,S.CIRNAME S_CIRNAME, S.DIVNAME,S.SUBNAME,S.SECNAME,USCNO,CTNAME,DECODE(CTGOVT_PVT,'Y','GOVT','NON-GOVT') TYPE,CTCAT,to_char(PAY_DT,'mm-dd-yyyy') LAST_PAID_MONTH,\r\n"
						+ "CASE WHEN (MONTHS_BETWEEN(TRUNC(LAST_DAY(to_date(?,'MM-YYYY')),'MM'),PAY_DT)/12)>0 AND (MONTHS_BETWEEN(TRUNC(LAST_DAY(to_date(?,'MM-YYYY')),'MM'),PAY_DT)/12)<=0.5 THEN 'UPTO_6_MONTHS' ELSE \r\n"
						+ "CASE WHEN (MONTHS_BETWEEN(TRUNC(LAST_DAY(to_date(?,'MM-YYYY')),'MM'),PAY_DT)/12)>0.5 AND (MONTHS_BETWEEN(TRUNC(LAST_DAY(to_date(?,'MM-YYYY')),'MM'),PAY_DT)/12)<=1 THEN 'BELOW_1_YR_AND_ABV_6_MONTHS' ELSE \r\n"
						+ "CASE WHEN (MONTHS_BETWEEN(TRUNC(LAST_DAY(to_date(?,'MM-YYYY')),'MM'),PAY_DT)/12)>1 AND (MONTHS_BETWEEN(TRUNC(LAST_DAY(to_date(?,'MM-YYYY')),'MM'),PAY_DT)/12)<=2 THEN 'ABOVE_1_YR' ELSE \r\n"
						+ "CASE WHEN (MONTHS_BETWEEN(TRUNC(LAST_DAY(to_date(?,'MM-YYYY')),'MM'),PAY_DT)/12)>2 AND (MONTHS_BETWEEN(TRUNC(LAST_DAY(to_date(?,'MM-YYYY')),'MM'),PAY_DT)/12)<=3 THEN 'ABOVE_2_YR' ELSE\r\n"
						+ "CASE WHEN (MONTHS_BETWEEN(TRUNC(LAST_DAY(to_date(?,'MM-YYYY')),'MM'),PAY_DT)/12)>3 AND (MONTHS_BETWEEN(TRUNC(LAST_DAY(to_date(?,'MM-YYYY')),'MM'),PAY_DT)/12)<=4 THEN 'ABOVE_3_YR' ELSE\r\n"
						+ "CASE WHEN (MONTHS_BETWEEN(TRUNC(LAST_DAY(to_date(?,'MM-YYYY')),'MM'),PAY_DT)/12)>4 AND (MONTHS_BETWEEN(TRUNC(LAST_DAY(to_date(?,'MM-YYYY')),'MM'),PAY_DT)/12)<=5 THEN 'ABOVE_4_YR' ELSE\r\n"
						+ "CASE WHEN (MONTHS_BETWEEN(TRUNC(LAST_DAY(to_date(?,'MM-YYYY')),'MM'),PAY_DT)/12)>5 THEN 'ABOVE_5_YR' ELSE 'BELOW' END END END END END END END AGE,\r\n"
						+ "\r\n" + "nvl(CB_EC,0)+NVL(CB_FSA,0) CC,\r\n" + "\r\n" + "NVL(CB_ED,0) ED,\r\n"
						+ "NVL(CB_IED,0) EDI,\r\n" + "\r\n" + "NVL(CB_LPC,0)+NVL(CB_CCLPC,0) LPC,\r\n"
						+ "NVL(CB_OTH,0) OTH,\r\n" + "\r\n"
						+ "NVL(CBTOT,0)+NVL(CB_CCLPC,0)+NVL(CB_OTH,0) CB FROM  LEDGER_HT_HIST,CONS,SPDCLMASTER S,\r\n"
						+ "(SELECT USCNO SCNO,MAX(TRUNC(TO_DATE(MON_YEAR,'MON-YYYY'),'MM')) PAY_DT FROM LEDGER_HT_HIST  WHERE "
						+ constring
						+ " SUBSTR(USCNO,1,3) IN('GNT','VJA','ONG','CRD') AND  NVL(TOT_PAY,0)+NVL(CRJ,0)>0 GROUP BY USCNO\r\n"
						+ "UNION\r\n"
						+ "SELECT USCNO SCNO,MIN(TRUNC(TO_DATE(MON_YEAR,'MON-YYYY'),'MM')) PAY_DT FROM LEDGER_HT_HIST  WHERE "
						+ constring
						+ " SUBSTR(USCNO,1,3) IN('GNT','VJA','ONG','CRD') GROUP BY USCNO HAVING SUM(NVL(TOT_PAY,0)+NVL(CRJ,0))=0) \r\n"
						+ "WHERE USCNO=SCNO AND USCNO=CTUSCNO AND TRUNC(TO_DATE(MON_YEAR,'MON-YYYY'),'MM')=ADD_MONTHS(TRUNC(LAST_DAY(to_date(?,'MM-YYYY')),'MM'),-1) and (NVL(CBTOT,0)+NVL(CB_CCLPC,0)+NVL(CB_OTH,0))>1\r\n "
						+ "and SECCD = SUBSTR(CONS.CTSECCD,-5) "
						+ " ORDER BY USCNO,SUBSTR(AGE,1,5),SUBSTR(AGE,7)  DESC )"
						+ (agewise.equals("TOTAL") ? " where cirname = ? " : " where cirname = ? and age = ?");
				log.info(sql);
				if (agewise.equals("TOTAL"))
					return jdbcTemplate.queryForList(sql,
							new Object[] { fmonthYear, fmonthYear, fmonthYear, fmonthYear, fmonthYear, fmonthYear,
									fmonthYear, fmonthYear, fmonthYear, fmonthYear, fmonthYear, fmonthYear, fmonthYear,
									fmonthYear, circle });
				else
					return jdbcTemplate.queryForList(sql,
							new Object[] { fmonthYear, fmonthYear, fmonthYear, fmonthYear, fmonthYear, fmonthYear,
									fmonthYear, fmonthYear, fmonthYear, fmonthYear, fmonthYear, fmonthYear, fmonthYear,
									fmonthYear, circle, agewise });
			} catch (DataAccessException e) {
				e.printStackTrace();
				log.error(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}

		}
	}

	public List<Map<String, Object>> getThirdPartySalesDetailsTwo(HttpServletRequest request) {
		String monthYear = request.getParameter("month") + "-" + request.getParameter("year");
		try {
			String sql = " SELECT CTNAME,DEV_NAME,DEV_CODE,TPUSCNO,CTCAT,NVL(CTCMD_HT,0)CTCMD_HT,NVL(CTACTUAL_KV,0)CTACTUAL_KV, NVL(WHEEL_PER,0)WHEEL_PER,ROUND((TP_ADJ_ENG*100)/(100-WHEEL_PER),2) ADJ_ENG_GROSS,"
					+ " ROUND(((TP_ADJ_ENG*100)/(100-WHEEL_PER)*WHEEL_PER)/100,2) WHEELING_LOSS_KVAH,(TP_ADJ_ENG-TP_ADJ_ENG)UNUTILIZED_UNITS,NVL(BTRKVAH_HT,0)BTRKVAH_HT,TP_ADJ_ENG,TP_WHEEL_CHGS "
					+ " FROM TP_ENERGY,CONS,BILL WHERE TPUSCNO=CTUSCNO AND TRIM(CANCEL_FLAG)='C' AND BILL_MON||'-'||BILL_YEAR=? AND TPUSCNO=BTSCNO AND TO_CHAR(BTBLDT,'MON-YYYY')=? "
					+ " UNION ALL"
					+ " SELECT CTNAME,DEV_NAME,DEV_CODE,TPUSCNO,CTCAT,NVL(CTCMD_HT,0)CTCMD_HT,NVL(CTACTUAL_KV,0)CTACTUAL_KV, NVL(WHEEL_PER,0)WHEEL_PER,ROUND((TP_ADJ_ENG*100)/(100-WHEEL_PER),2) ADJ_ENG_GROSS,"
					+ " ROUND(((TP_ADJ_ENG*100)/(100-WHEEL_PER)*WHEEL_PER)/100,2) WHEELING_LOSS_KVAH,(TP_ADJ_ENG-TP_ADJ_ENG)UNUTILIZED_UNITS,NVL(BTRKVAH_HT,0)BTRKVAH_HT,TP_ADJ_ENG ,TP_WHEEL_CHGS "
					+ " FROM TP_ENERGY_HIST,CONS,BILL_HIST WHERE TPUSCNO=CTUSCNO AND TRIM(CANCEL_FLAG)='C' AND BILL_MON||'-'||BILL_YEAR=? AND TPUSCNO=BTSCNO AND TO_CHAR(BTBLDT,'MON-YYYY')=? ";
			log.info(sql);
			return jdbcTemplate.queryForList(sql, new Object[] { monthYear, monthYear, monthYear, monthYear });
		} catch (DataAccessException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	public List<Map<String, Object>> getOAalesDetails(HttpServletRequest request) {

		try {
			String sql = "select SUBSTR(CTUSCNO,1,3) CIRCLE,CTACTUAL_KV,count(*) NOS,SUM(ROUND(NVL(KVAH_ADJ_ENG,0)))KVAH_ADJ_ENG,SUM(ROUND(NVL(TOD_ADJ_PEAK,0)))TOD_ADJ_PEAK,SUM(ROUND(NVL(TOD_ADJ_OFF,0))) TOD_ADJ_OFF,SUM((ROUND(TOD_ADJ_PEAK)-ROUND(TOD_ADJ_OFF)))TOD_ADJ_ENG,\r\n"
					+ "SUM(ROUND(CS_CHARGES)) CS_CHARGES,SUM(ROUND(WHELL_CHARGES))WHELL_CHARGES\r\n"
					+ "FROM (select * from open_access union all select * from open_access_hist),CONS\r\n"
					+ "where CTUSCNO=OAUSCNO AND BILL_MON=? AND BILL_YEAR=?\r\n"
					+ "group by SUBSTR(CTUSCNO,1,3),CTACTUAL_KV order by CTACTUAL_KV ,SUBSTR(CTUSCNO,1,3) ";
			log.info(sql);
			return jdbcTemplate.queryForList(sql,
					new Object[] { request.getParameter("month"), request.getParameter("year") });
		} catch (DataAccessException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	public List<Map<String, Object>> getCategoryWiseDemandReport(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		String year = request.getParameter("month") + "-" + request.getParameter("year");

		if (circle.equalsIgnoreCase("ALL")) {
			try {
				String sql = " select nvl(CIRCLE,'APCPDCL') CIRCLE,nvl(TYPE,'TOTAL') TYPE, nvl(CIRCLE,'APCPDCL')||nvl(TYPE,'TOTAL') CIR_TYPE ,nvl(CTCAT,'TOTAL') CTCAT,SUM(NOS) NOS,\r\n"
						+ "sum(BTRKWH_HT) BTRKWH_HT,sum(BTBKVAH) BTBKVAH,sum(KVAH) KVAH,\r\n"
						+ "sum(ADJ_UNITS) ADJ_UNITS,sum(ENERGY_CHARGES) ENERGY_CHARGES,sum(FIXED_CHARGES) FIXED_CHARGES,sum(TODCHG) TODCHG,sum(CUSTOMER_CHARGES) CUSTOMER_CHARGES,sum(LPC) LPC,\r\n"
						+ "sum(ED) ED,sum(EDI) EDI,sum(FSA) FSA,sum(OTHER_CHARGES) OTHER_CHARGES,sum(TRUE_UP) TRUE_UP,sum(ISD) ISD,sum(WITHOUT_CLPC) WITHOUT_CLPC,sum(COURT_LPC) COURT_LPC,sum(TOTAL) TOTAL, sum(round(Nvl(BT_FPP_CHG,0))) BT_FPP_CHG , sum(round(Nvl(BT_PFPP_CHG,0))) BT_PFPP_CHG , sum(round(Nvl(BT_LT_FPP_CHG,0))) BT_LT_FPP_CHG   \r\n"
						+ "from  (\r\n" + "select \r\n"
						+ "substr(BTSCNO,1,3) CIRCLE,DECODE(CTGOVT_PVT,'Y','GOVT','NON-GOVT') TYPE,'HT'||SUBSTR(BTBLCAT,0,1)CTCAT,\r\n"
						+ "COUNT(SUBSTR(BTBLCAT,0,1)) NOS,\r\n" + "SUM(BTRKWH_HT)BTRKWH_HT ,\r\n"
						+ "SUM(BTBKVAH)BTBKVAH ,\r\n" + "sum(round(nvl(BTBKVAH,0))) KVAH,\r\n"
						+ "SUM(BTOA_KVAH+BTTP_KVA) ADJ_UNITS,\r\n"
						+ "sum(Nvl(BTENGCHG_NOR,0))+sum(Nvl(BTENGCHG_PEN,0)) Energy_Charges,\r\n"
						+ "sum(Nvl(BTDEMCHG_NOR,0))+sum(Nvl(BTDEMCHG_PEN,0)) Fixed_Charges,\r\n"
						+ "sum(Nvl(BTTODCHG_HT,0)) Todchg,\r\n" + "sum(Nvl(BTCUSTCHG,0)) Customer_Charges,\r\n"
						+ "sum(Nvl(BTADLCHG,0))+sum(nvl(BTACDSURCHG,0)) LPC,\r\n" + "sum(nvl(BTED,0)) ED,\r\n"
						+ "sum(nvl(BTED_INT,0)) EDI,\r\n" + "sum(nvl(btfsachg,0)) FSA,\r\n"
						+ "sum(nvl(BTOTHERCHG,0))+sum(nvl(BTDTRHIRE_CHG,0))+sum(Nvl(BTDTRHIRE_SGST,0))+sum(Nvl(BTDTRHIRE_CGST,0))+\r\n"
						+ "sum(Nvl(BTCOLNYCHG_HT,0))+sum(Nvl(TCS_COLL,0))+sum(Nvl(BTCROSSSUBCHG,0))+sum(Nvl(BTWHEELCHGCASH_HT,0))+sum(Nvl(BTAQUASUB_CHG,0))+sum(nvl(BTVOLTSURCHG,0))+sum(nvl(BTROUNDAMT,0)) Other_Charges,\r\n"
						+ "sum(Nvl(BT_TU_CHG,0)+Nvl(BT_LTU_CHG,0)) True_up,\r\n" + "sum(Nvl(BT_NET_ISD,0)) ISD,\r\n"
						+ "sum(round(Nvl(BTCURDEM,0))) Without_Clpc,\r\n"
						+ "sum(round(Nvl(BTCOURT_LPC,0))) Court_Lpc,\r\n"
						+ "sum(round(Nvl(BTCURDEM,0)))+sum(round(Nvl(BTCOURT_LPC,0))) TOTAL, sum(round(Nvl(BT_FPP_CHG,0))) BT_FPP_CHG , sum(round(Nvl(BT_PFPP_CHG,0))) BT_PFPP_CHG ,sum(round(Nvl(BT_LT_FPP_CHG,0))) BT_LT_FPP_CHG  \r\n"
						+ " from (select * from bill union all select * from bill_hist),cons where \r\n"
						+ "BTSCNO=CTUSCNO AND\r\n" + "to_char(BTBLDT,'MON-YYYY')=? \r\n"
						+ "group by substr(BTSCNO,1,3),DECODE(CTGOVT_PVT,'Y','GOVT','NON-GOVT'),SUBSTR(BTBLCAT,0,1))\r\n"
						+ "GROUP BY CUBE (CIRCLE,TYPE,CTCAT)\r\n" + "ORDER BY \r\n"
						+ "case when CIRCLE = 'VJA' then '001' when CIRCLE = 'GNT' then '002' when CIRCLE = 'ONG' then '003'  when CIRCLE = 'CRD' then '009' else CIRCLE end,case when TYPE='GOVT' then '001' when TYPE = 'NON-GOVT' then '002' else TYPE end,CTCAT\r\n"
						+ "";
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
				String sql = " select * from ( select  CIRCLE,nvl(TYPE,'TOTAL') TYPE, nvl(CIRCLE,'APCPDCL')||nvl(TYPE,'TOTAL') CIR_TYPE ,nvl(CTCAT,'TOTAL') CTCAT,SUM(NOS) NOS,\r\n"
						+ "sum(BTRKWH_HT) BTRKWH_HT,sum(BTBKVAH) BTBKVAH,sum(KVAH) KVAH,\r\n"
						+ "sum(ADJ_UNITS) ADJ_UNITS,sum(ENERGY_CHARGES) ENERGY_CHARGES,sum(FIXED_CHARGES) FIXED_CHARGES,sum(TODCHG) TODCHG,sum(CUSTOMER_CHARGES) CUSTOMER_CHARGES,sum(LPC) LPC,\r\n"
						+ "sum(ED) ED,sum(EDI) EDI,sum(FSA) FSA,sum(OTHER_CHARGES) OTHER_CHARGES,sum(TRUE_UP) TRUE_UP,sum(ISD) ISD,sum(WITHOUT_CLPC) WITHOUT_CLPC,sum(COURT_LPC) COURT_LPC,sum(TOTAL) TOTAL , sum(round(Nvl(BT_FPP_CHG,0))) BT_FPP_CHG, sum(round(Nvl(BT_PFPP_CHG,0))) BT_PFPP_CHG  , sum(round(Nvl(BT_LT_FPP_CHG,0))) BT_LT_FPP_CHG  \r\n"
						+ "from  (\r\n" + "select \r\n"
						+ "substr(BTSCNO,1,3) CIRCLE,DECODE(CTGOVT_PVT,'Y','GOVT','NON-GOVT') TYPE,'HT'||SUBSTR(BTBLCAT,0,1)CTCAT,\r\n"
						+ "COUNT(SUBSTR(BTBLCAT,0,1)) NOS,\r\n" + "SUM(BTRKWH_HT)BTRKWH_HT ,\r\n"
						+ "SUM(BTBKVAH)BTBKVAH ,\r\n" + "sum(round(nvl(BTBKVAH,0))) KVAH,\r\n"
						+ "SUM(BTOA_KVAH+BTTP_KVA) ADJ_UNITS,\r\n"
						+ "sum(Nvl(BTENGCHG_NOR,0))+sum(Nvl(BTENGCHG_PEN,0)) Energy_Charges,\r\n"
						+ "sum(Nvl(BTDEMCHG_NOR,0))+sum(Nvl(BTDEMCHG_PEN,0)) Fixed_Charges,\r\n"
						+ "sum(Nvl(BTTODCHG_HT,0)) Todchg,\r\n" + "sum(Nvl(BTCUSTCHG,0)) Customer_Charges,\r\n"
						+ "sum(Nvl(BTADLCHG,0))+sum(nvl(BTACDSURCHG,0)) LPC,\r\n" + "sum(nvl(BTED,0)) ED,\r\n"
						+ "sum(nvl(BTED_INT,0)) EDI,\r\n" + "sum(nvl(btfsachg,0)) FSA,\r\n"
						+ "sum(nvl(BTOTHERCHG,0))+sum(nvl(BTDTRHIRE_CHG,0))+sum(Nvl(BTDTRHIRE_SGST,0))+sum(Nvl(BTDTRHIRE_CGST,0))+\r\n"
						+ "sum(Nvl(BTCOLNYCHG_HT,0))+sum(Nvl(TCS_COLL,0))+sum(Nvl(BTCROSSSUBCHG,0))+sum(Nvl(BTWHEELCHGCASH_HT,0))+sum(Nvl(BTAQUASUB_CHG,0))+sum(nvl(BTVOLTSURCHG,0))+sum(nvl(BTROUNDAMT,0)) Other_Charges,\r\n"
						+ "sum(Nvl(BT_TU_CHG,0)+Nvl(BT_LTU_CHG,0)) True_up,\r\n" + "sum(Nvl(BT_NET_ISD,0)) ISD,\r\n"
						+ "sum(round(Nvl(BTCURDEM,0))) Without_Clpc,\r\n"
						+ "sum(round(Nvl(BTCOURT_LPC,0))) Court_Lpc,\r\n"
						+ "sum(round(Nvl(BTCURDEM,0)))+sum(round(Nvl(BTCOURT_LPC,0))) TOTAL , sum(round(Nvl(BT_FPP_CHG,0))) BT_FPP_CHG , sum(round(Nvl(BT_PFPP_CHG,0))) BT_PFPP_CHG,sum(round(Nvl(BT_LT_FPP_CHG,0))) BT_LT_FPP_CHG   \r\n"
						+ " from (select * from bill union all select * from bill_hist),cons where \r\n"
						+ "BTSCNO=CTUSCNO AND\r\n" + "to_char(BTBLDT,'MON-YYYY')=? \r\n"
						+ "group by substr(BTSCNO,1,3),DECODE(CTGOVT_PVT,'Y','GOVT','NON-GOVT'),SUBSTR(BTBLCAT,0,1))\r\n"
						+ "GROUP BY CUBE (CIRCLE,TYPE,CTCAT)\r\n" + "ORDER BY \r\n"
						+ "case when CIRCLE = 'VJA' then '001' when CIRCLE = 'GNT' then '002' when CIRCLE = 'ONG' then '003'  when CIRCLE = 'CRD' then '009' else CIRCLE end,case when TYPE='GOVT' then '001' when TYPE = 'NON-GOVT' then '002' else TYPE end,CTCAT\r\n"
						+ " ) where CIRCLE= ? ";
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

	public List<Map<String, Object>> getCategoryWiseDivisionWiseDemandReport(String circle, String year) {

		if (circle.equalsIgnoreCase("APCPDCL")) {
			try {
				String sql = "select * from ( select   CIRCLE,nvl(TYPE,'TOTAL') TYPE, nvl(CIRCLE,'APCPDCL')||nvl(TYPE,'TOTAL') CIR_TYPE ,nvl(CTCAT,'TOTAL') CTCAT,SUM(NOS) NOS,\r\n"
						+ "sum(BTRKWH_HT) BTRKWH_HT,sum(BTBKVAH) BTBKVAH,sum(KVAH) KVAH,\r\n"
						+ "sum(ADJ_UNITS) ADJ_UNITS,sum(ENERGY_CHARGES) ENERGY_CHARGES,sum(FIXED_CHARGES) FIXED_CHARGES,sum(TODCHG) TODCHG,sum(CUSTOMER_CHARGES) CUSTOMER_CHARGES,sum(LPC) LPC,\r\n"
						+ "sum(ED) ED,sum(EDI) EDI,sum(FSA) FSA,sum(OTHER_CHARGES) OTHER_CHARGES,sum(TRUE_UP) TRUE_UP,sum(ISD) ISD,sum(WITHOUT_CLPC) WITHOUT_CLPC,sum(COURT_LPC) COURT_LPC,sum(TOTAL) TOTAL , sum(round(Nvl(BT_FPP_CHG,0))) BT_FPP_CHG, sum(round(Nvl(BT_PFPP_CHG,0))) BT_PFPP_CHG,sum(round(Nvl(BT_LT_FPP_CHG,0))) BT_LT_FPP_CHG    \r\n"
						+ "from  (\r\n" + "select \r\n"
						+ "divname CIRCLE,DECODE(CTGOVT_PVT,'Y','GOVT','NON-GOVT') TYPE,'HT'||SUBSTR(BTBLCAT,0,1)CTCAT,\r\n"
						+ "COUNT(SUBSTR(BTBLCAT,0,1)) NOS,\r\n" + "SUM(BTRKWH_HT)BTRKWH_HT ,\r\n"
						+ "SUM(BTBKVAH)BTBKVAH ,\r\n" + "sum(round(nvl(BTBKVAH,0))) KVAH,\r\n"
						+ "SUM(BTOA_KVAH+BTTP_KVA) ADJ_UNITS,\r\n"
						+ "sum(Nvl(BTENGCHG_NOR,0))+sum(Nvl(BTENGCHG_PEN,0)) Energy_Charges,\r\n"
						+ "sum(Nvl(BTDEMCHG_NOR,0))+sum(Nvl(BTDEMCHG_PEN,0)) Fixed_Charges,\r\n"
						+ "sum(Nvl(BTTODCHG_HT,0)) Todchg,\r\n" + "sum(Nvl(BTCUSTCHG,0)) Customer_Charges,\r\n"
						+ "sum(Nvl(BTADLCHG,0))+sum(nvl(BTACDSURCHG,0)) LPC,\r\n" + "sum(nvl(BTED,0)) ED,\r\n"
						+ "sum(nvl(BTED_INT,0)) EDI,\r\n" + "sum(nvl(btfsachg,0)) FSA,\r\n"
						+ "sum(nvl(BTOTHERCHG,0))+sum(nvl(BTDTRHIRE_CHG,0))+sum(Nvl(BTDTRHIRE_SGST,0))+sum(Nvl(BTDTRHIRE_CGST,0))+\r\n"
						+ "sum(Nvl(BTCOLNYCHG_HT,0))+sum(Nvl(TCS_COLL,0))+sum(Nvl(BTCROSSSUBCHG,0))+sum(Nvl(BTWHEELCHGCASH_HT,0))+sum(Nvl(BTAQUASUB_CHG,0))+sum(nvl(BTVOLTSURCHG,0))+sum(nvl(BTROUNDAMT,0)) Other_Charges,\r\n"
						+ "sum(Nvl(BT_TU_CHG,0)+Nvl(BT_LTU_CHG,0)) True_up,\r\n" + "sum(Nvl(BT_NET_ISD,0)) ISD,\r\n"
						+ "sum(round(Nvl(BTCURDEM,0))) Without_Clpc,\r\n"
						+ "sum(round(Nvl(BTCOURT_LPC,0))) Court_Lpc,\r\n"
						+ "sum(round(Nvl(BTCURDEM,0)))+sum(round(Nvl(BTCOURT_LPC,0))) TOTAL , sum(round(Nvl(BT_FPP_CHG,0))) BT_FPP_CHG , sum(round(Nvl(BT_PFPP_CHG,0))) BT_PFPP_CHG,sum(round(Nvl(BT_LT_FPP_CHG,0))) BT_LT_FPP_CHG  \r\n"
						+ " from (select * from bill union all select * from bill_hist),cons,master.spdclmaster where \r\n"
						+ "BTSCNO=CTUSCNO AND\r\n" + "SUBSTR(CTSECCD,-5)=SECCD(+) and\r\n"
						+ "to_char(BTBLDT,'MON-YYYY')=? \r\n"
						+ "group by divname,DECODE(CTGOVT_PVT,'Y','GOVT','NON-GOVT'),SUBSTR(BTBLCAT,0,1))\r\n"
						+ "GROUP BY CUBE (CIRCLE,TYPE,CTCAT)\r\n" + "ORDER BY \r\n"
						+ "CIRCLE,case when TYPE='GOVT' then '001' when TYPE = 'NON-GOVT' then '002' else TYPE end,CTCAT ) where CIRCLE is not null";
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
				String sql = "select * from ( select   CIRCLE,nvl(TYPE,'TOTAL') TYPE, nvl(CIRCLE,'APCPDCL')||nvl(TYPE,'TOTAL') CIR_TYPE ,nvl(CTCAT,'TOTAL') CTCAT,SUM(NOS) NOS,\r\n"
						+ "sum(BTRKWH_HT) BTRKWH_HT,sum(BTBKVAH) BTBKVAH,sum(KVAH) KVAH,\r\n"
						+ "sum(ADJ_UNITS) ADJ_UNITS,sum(ENERGY_CHARGES) ENERGY_CHARGES,sum(FIXED_CHARGES) FIXED_CHARGES,sum(TODCHG) TODCHG,sum(CUSTOMER_CHARGES) CUSTOMER_CHARGES,sum(LPC) LPC,\r\n"
						+ "sum(ED) ED,sum(EDI) EDI,sum(FSA) FSA,sum(OTHER_CHARGES) OTHER_CHARGES,sum(TRUE_UP) TRUE_UP,sum(ISD) ISD,sum(WITHOUT_CLPC) WITHOUT_CLPC,sum(COURT_LPC) COURT_LPC,sum(TOTAL) TOTAL , sum(round(Nvl(BT_FPP_CHG,0))) BT_FPP_CHG, sum(round(Nvl(BT_PFPP_CHG,0))) BT_PFPP_CHG,sum(round(Nvl(BT_LT_FPP_CHG,0))) BT_LT_FPP_CHG    \r\n"
						+ "from  (\r\n" + "select \r\n"
						+ "divname CIRCLE,DECODE(CTGOVT_PVT,'Y','GOVT','NON-GOVT') TYPE,'HT'||SUBSTR(BTBLCAT,0,1)CTCAT,\r\n"
						+ "COUNT(SUBSTR(BTBLCAT,0,1)) NOS,\r\n" + "SUM(BTRKWH_HT)BTRKWH_HT ,\r\n"
						+ "SUM(BTBKVAH)BTBKVAH ,\r\n" + "sum(round(nvl(BTBKVAH,0))) KVAH,\r\n"
						+ "SUM(BTOA_KVAH+BTTP_KVA) ADJ_UNITS,\r\n"
						+ "sum(Nvl(BTENGCHG_NOR,0))+sum(Nvl(BTENGCHG_PEN,0)) Energy_Charges,\r\n"
						+ "sum(Nvl(BTDEMCHG_NOR,0))+sum(Nvl(BTDEMCHG_PEN,0)) Fixed_Charges,\r\n"
						+ "sum(Nvl(BTTODCHG_HT,0)) Todchg,\r\n" + "sum(Nvl(BTCUSTCHG,0)) Customer_Charges,\r\n"
						+ "sum(Nvl(BTADLCHG,0))+sum(nvl(BTACDSURCHG,0)) LPC,\r\n" + "sum(nvl(BTED,0)) ED,\r\n"
						+ "sum(nvl(BTED_INT,0)) EDI,\r\n" + "sum(nvl(btfsachg,0)) FSA,\r\n"
						+ "sum(nvl(BTOTHERCHG,0))+sum(nvl(BTDTRHIRE_CHG,0))+sum(Nvl(BTDTRHIRE_SGST,0))+sum(Nvl(BTDTRHIRE_CGST,0))+\r\n"
						+ "sum(Nvl(BTCOLNYCHG_HT,0))+sum(Nvl(TCS_COLL,0))+sum(Nvl(BTCROSSSUBCHG,0))+sum(Nvl(BTWHEELCHGCASH_HT,0))+sum(Nvl(BTAQUASUB_CHG,0))+sum(nvl(BTVOLTSURCHG,0))+sum(nvl(BTROUNDAMT,0)) Other_Charges,\r\n"
						+ "sum(Nvl(BT_TU_CHG,0)+Nvl(BT_LTU_CHG,0)) True_up,\r\n" + "sum(Nvl(BT_NET_ISD,0)) ISD,\r\n"
						+ "sum(round(Nvl(BTCURDEM,0))) Without_Clpc,\r\n"
						+ "sum(round(Nvl(BTCOURT_LPC,0))) Court_Lpc,\r\n"
						+ "sum(round(Nvl(BTCURDEM,0)))+sum(round(Nvl(BTCOURT_LPC,0))) TOTAL , sum(round(Nvl(BT_FPP_CHG,0))) BT_FPP_CHG , sum(round(Nvl(BT_PFPP_CHG,0))) BT_PFPP_CHG ,sum(round(Nvl(BT_LT_FPP_CHG,0))) BT_LT_FPP_CHG  \r\n"
						+ " from (select * from bill union all select * from bill_hist),cons,master.spdclmaster where \r\n"
						+ "BTSCNO=CTUSCNO AND\r\n" + "substr(BTSCNO,1,3)=? and\r\n"
						+ "SUBSTR(CTSECCD,-5)=SECCD(+) and\r\n" + "to_char(BTBLDT,'MON-YYYY')=? \r\n"
						+ "group by divname,DECODE(CTGOVT_PVT,'Y','GOVT','NON-GOVT'),SUBSTR(BTBLCAT,0,1))\r\n"
						+ "GROUP BY CUBE (CIRCLE,TYPE,CTCAT)\r\n" + "ORDER BY \r\n"
						+ "CIRCLE,case when TYPE='GOVT' then '001' when TYPE = 'NON-GOVT' then '002' else TYPE end,CTCAT ) where CIRCLE is not null";
				log.info(sql);
				return jdbcTemplate.queryForList(sql, new Object[] { circle, year });
			} catch (DataAccessException e) {
				e.printStackTrace();
				log.error(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}

		}
	}

	public List<Map<String, Object>> getCategoryWiseSubDivisionWiseDemandReport(String circle, String year) {

		if (circle.equalsIgnoreCase("APCPDCL")) {
			try {
				String sql = "select * from ( select   CIRCLE,nvl(TYPE,'TOTAL') TYPE, nvl(CIRCLE,'APCPDCL')||nvl(TYPE,'TOTAL') CIR_TYPE ,nvl(CTCAT,'TOTAL') CTCAT,SUM(NOS) NOS,\r\n"
						+ "sum(BTRKWH_HT) BTRKWH_HT,sum(BTBKVAH) BTBKVAH,sum(KVAH) KVAH,\r\n"
						+ "sum(ADJ_UNITS) ADJ_UNITS,sum(ENERGY_CHARGES) ENERGY_CHARGES,sum(FIXED_CHARGES) FIXED_CHARGES,sum(TODCHG) TODCHG,sum(CUSTOMER_CHARGES) CUSTOMER_CHARGES,sum(LPC) LPC,\r\n"
						+ "sum(ED) ED,sum(EDI) EDI,sum(FSA) FSA,sum(OTHER_CHARGES) OTHER_CHARGES,sum(TRUE_UP) TRUE_UP,sum(ISD) ISD,sum(WITHOUT_CLPC) WITHOUT_CLPC,sum(COURT_LPC) COURT_LPC,sum(TOTAL) TOTAL , sum(round(Nvl(BT_FPP_CHG,0))) BT_FPP_CHG, sum(round(Nvl(BT_PFPP_CHG,0))) BT_PFPP_CHG,sum(round(Nvl(BT_LT_FPP_CHG,0))) BT_LT_FPP_CHG   \r\n"
						+ "from  (\r\n" + "select \r\n"
						+ "SUBNAME CIRCLE,DECODE(CTGOVT_PVT,'Y','GOVT','NON-GOVT') TYPE,'HT'||SUBSTR(BTBLCAT,0,1)CTCAT,\r\n"
						+ "COUNT(SUBSTR(BTBLCAT,0,1)) NOS,\r\n" + "SUM(BTRKWH_HT)BTRKWH_HT ,\r\n"
						+ "SUM(BTBKVAH)BTBKVAH ,\r\n" + "sum(round(nvl(BTBKVAH,0))) KVAH,\r\n"
						+ "SUM(BTOA_KVAH+BTTP_KVA) ADJ_UNITS,\r\n"
						+ "sum(Nvl(BTENGCHG_NOR,0))+sum(Nvl(BTENGCHG_PEN,0)) Energy_Charges,\r\n"
						+ "sum(Nvl(BTDEMCHG_NOR,0))+sum(Nvl(BTDEMCHG_PEN,0)) Fixed_Charges,\r\n"
						+ "sum(Nvl(BTTODCHG_HT,0)) Todchg,\r\n" + "sum(Nvl(BTCUSTCHG,0)) Customer_Charges,\r\n"
						+ "sum(Nvl(BTADLCHG,0))+sum(nvl(BTACDSURCHG,0)) LPC,\r\n" + "sum(nvl(BTED,0)) ED,\r\n"
						+ "sum(nvl(BTED_INT,0)) EDI,\r\n" + "sum(nvl(btfsachg,0)) FSA,\r\n"
						+ "sum(nvl(BTOTHERCHG,0))+sum(nvl(BTDTRHIRE_CHG,0))+sum(Nvl(BTDTRHIRE_SGST,0))+sum(Nvl(BTDTRHIRE_CGST,0))+\r\n"
						+ "sum(Nvl(BTCOLNYCHG_HT,0))+sum(Nvl(TCS_COLL,0))+sum(Nvl(BTCROSSSUBCHG,0))+sum(Nvl(BTWHEELCHGCASH_HT,0))+sum(Nvl(BTAQUASUB_CHG,0))+sum(nvl(BTVOLTSURCHG,0))+sum(nvl(BTROUNDAMT,0)) Other_Charges,\r\n"
						+ "sum(Nvl(BT_TU_CHG,0)+Nvl(BT_LTU_CHG,0)) True_up,\r\n" + "sum(Nvl(BT_NET_ISD,0)) ISD,\r\n"
						+ "sum(round(Nvl(BTCURDEM,0))) Without_Clpc,\r\n"
						+ "sum(round(Nvl(BTCOURT_LPC,0))) Court_Lpc,\r\n"
						+ "sum(round(Nvl(BTCURDEM,0)))+sum(round(Nvl(BTCOURT_LPC,0))) TOTAL , sum(round(Nvl(BT_FPP_CHG,0))) BT_FPP_CHG , sum(round(Nvl(BT_PFPP_CHG,0))) BT_PFPP_CHG ,sum(round(Nvl(BT_LT_FPP_CHG,0))) BT_LT_FPP_CHG  \r\n"
						+ " from (select * from bill union all select * from bill_hist),cons,master.spdclmaster where \r\n"
						+ "BTSCNO=CTUSCNO AND\r\n" + "SUBSTR(CTSECCD,-5)=SECCD(+) and\r\n"
						+ "to_char(BTBLDT,'MON-YYYY')=? \r\n"
						+ "group by SUBNAME,DECODE(CTGOVT_PVT,'Y','GOVT','NON-GOVT'),SUBSTR(BTBLCAT,0,1))\r\n"
						+ "GROUP BY CUBE (CIRCLE,TYPE,CTCAT)\r\n" + "ORDER BY \r\n"
						+ "CIRCLE,case when TYPE='GOVT' then '001' when TYPE = 'NON-GOVT' then '002' else TYPE end,CTCAT ) where CIRCLE is not null";
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
				String sql = "select * from ( select   CIRCLE,nvl(TYPE,'TOTAL') TYPE, nvl(CIRCLE,'APCPDCL')||nvl(TYPE,'TOTAL') CIR_TYPE ,nvl(CTCAT,'TOTAL') CTCAT,SUM(NOS) NOS,\r\n"
						+ "sum(BTRKWH_HT) BTRKWH_HT,sum(BTBKVAH) BTBKVAH,sum(KVAH) KVAH,\r\n"
						+ "sum(ADJ_UNITS) ADJ_UNITS,sum(ENERGY_CHARGES) ENERGY_CHARGES,sum(FIXED_CHARGES) FIXED_CHARGES,sum(TODCHG) TODCHG,sum(CUSTOMER_CHARGES) CUSTOMER_CHARGES,sum(LPC) LPC,\r\n"
						+ "sum(ED) ED,sum(EDI) EDI,sum(FSA) FSA,sum(OTHER_CHARGES) OTHER_CHARGES,sum(TRUE_UP) TRUE_UP,sum(ISD) ISD,sum(WITHOUT_CLPC) WITHOUT_CLPC,sum(COURT_LPC) COURT_LPC,sum(TOTAL) TOTAL , sum(round(Nvl(BT_FPP_CHG,0))) BT_FPP_CHG, sum(round(Nvl(BT_PFPP_CHG,0))) BT_PFPP_CHG ,sum(round(Nvl(BT_LT_FPP_CHG,0))) BT_LT_FPP_CHG   \r\n"
						+ "from  (\r\n" + "select \r\n"
						+ "SUBNAME CIRCLE,DECODE(CTGOVT_PVT,'Y','GOVT','NON-GOVT') TYPE,'HT'||SUBSTR(BTBLCAT,0,1)CTCAT,\r\n"
						+ "COUNT(SUBSTR(BTBLCAT,0,1)) NOS,\r\n" + "SUM(BTRKWH_HT)BTRKWH_HT ,\r\n"
						+ "SUM(BTBKVAH)BTBKVAH ,\r\n" + "sum(round(nvl(BTBKVAH,0))) KVAH,\r\n"
						+ "SUM(BTOA_KVAH+BTTP_KVA) ADJ_UNITS,\r\n"
						+ "sum(Nvl(BTENGCHG_NOR,0))+sum(Nvl(BTENGCHG_PEN,0)) Energy_Charges,\r\n"
						+ "sum(Nvl(BTDEMCHG_NOR,0))+sum(Nvl(BTDEMCHG_PEN,0)) Fixed_Charges,\r\n"
						+ "sum(Nvl(BTTODCHG_HT,0)) Todchg,\r\n" + "sum(Nvl(BTCUSTCHG,0)) Customer_Charges,\r\n"
						+ "sum(Nvl(BTADLCHG,0))+sum(nvl(BTACDSURCHG,0)) LPC,\r\n" + "sum(nvl(BTED,0)) ED,\r\n"
						+ "sum(nvl(BTED_INT,0)) EDI,\r\n" + "sum(nvl(btfsachg,0)) FSA,\r\n"
						+ "sum(nvl(BTOTHERCHG,0))+sum(nvl(BTDTRHIRE_CHG,0))+sum(Nvl(BTDTRHIRE_SGST,0))+sum(Nvl(BTDTRHIRE_CGST,0))+\r\n"
						+ "sum(Nvl(BTCOLNYCHG_HT,0))+sum(Nvl(TCS_COLL,0))+sum(Nvl(BTCROSSSUBCHG,0))+sum(Nvl(BTWHEELCHGCASH_HT,0))+sum(Nvl(BTAQUASUB_CHG,0))+sum(nvl(BTVOLTSURCHG,0))+sum(nvl(BTROUNDAMT,0)) Other_Charges,\r\n"
						+ "sum(Nvl(BT_TU_CHG,0)+Nvl(BT_LTU_CHG,0)) True_up,\r\n" + "sum(Nvl(BT_NET_ISD,0)) ISD,\r\n"
						+ "sum(round(Nvl(BTCURDEM,0))) Without_Clpc,\r\n"
						+ "sum(round(Nvl(BTCOURT_LPC,0))) Court_Lpc,\r\n"
						+ "sum(round(Nvl(BTCURDEM,0)))+sum(round(Nvl(BTCOURT_LPC,0))) TOTAL , sum(round(Nvl(BT_FPP_CHG,0))) BT_FPP_CHG , sum(round(Nvl(BT_PFPP_CHG,0))) BT_PFPP_CHG  ,sum(round(Nvl(BT_LT_FPP_CHG,0))) BT_LT_FPP_CHG \r\n"
						+ " from (select * from bill union all select * from bill_hist),cons,master.spdclmaster where \r\n"
						+ "BTSCNO=CTUSCNO AND\r\n" + "divname=? and\r\n" + "SUBSTR(CTSECCD,-5)=SECCD(+) and\r\n"
						+ "to_char(BTBLDT,'MON-YYYY')=? \r\n"
						+ "group by SUBNAME,DECODE(CTGOVT_PVT,'Y','GOVT','NON-GOVT'),SUBSTR(BTBLCAT,0,1))\r\n"
						+ "GROUP BY CUBE (CIRCLE,TYPE,CTCAT)\r\n" + "ORDER BY \r\n"
						+ "CIRCLE,case when TYPE='GOVT' then '001' when TYPE = 'NON-GOVT' then '002' else TYPE end,CTCAT ) where CIRCLE is not null";
				log.info(sql);
				return jdbcTemplate.queryForList(sql, new Object[] { circle, year });
			} catch (DataAccessException e) {
				e.printStackTrace();
				log.error(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}

		}
	}
	/*
	 * public List<Map<String, Object>>
	 * getCategoryWiseDemandReport(HttpServletRequest request) { String monthYear =
	 * request.getParameter("month") + "-" + request.getParameter("year"); String
	 * circle = request.getParameter("circle"); if (circle.equalsIgnoreCase("ALL"))
	 * { try { String sql =
	 * " SELECT SUBSTR(BTSCNO,1,3)CIRCLE, SUBSTR(BTBLCAT,0,1)CAT,COUNT(SUBSTR(BTBLCAT,0,1)) NOS,SUM(BTRKWH_HT)BTRKWH_HT, SUM(BTRKVAH_HT)BTRKVAH_HT,SUM(BTBKVAH)BTBKVAH ,SUM(BTOA_KVAH+BTTP_KVA) Adjusted_Units, SUM(BTCURDEM)BTCURDEM, SUM(BTCOURT_LPC) BTCOURT_LPC,SUM(BTCURDEM+BTCOURT_LPC) TOTAL_DEMAND "
	 * +
	 * " FROM BILL_HIST WHERE TO_CHAR(BTBLDT,'MON-YYYY')=? GROUP BY SUBSTR(BTBLCAT,0,1),SUBSTR(BTSCNO,1,3) "
	 * + " UNION ALL " +
	 * " SELECT SUBSTR(BTSCNO,1,3)CIRCLE, SUBSTR(BTBLCAT,0,1)CAT,COUNT(SUBSTR(BTBLCAT,0,1)) NOS,SUM(BTRKWH_HT)BTRKWH_HT, SUM(BTRKVAH_HT)BTRKVAH_HT,SUM(BTBKVAH)BTBKVAH ,SUM(BTOA_KVAH+BTTP_KVA) Adjusted_Units, SUM(BTCURDEM)BTCURDEM, SUM(BTCOURT_LPC) BTCOURT_LPC,SUM(BTCURDEM+BTCOURT_LPC) TOTAL_DEMAND "
	 * +
	 * " FROM BILL WHERE TO_CHAR(BTBLDT,'MON-YYYY')=? GROUP BY SUBSTR(BTBLCAT,0,1),SUBSTR(BTSCNO,1,3) ORDER BY CAT,CIRCLE"
	 * ; log.info(sql); return jdbcTemplate.queryForList(sql,new Object[]
	 * {monthYear,monthYear}); } catch (DataAccessException e) {
	 * log.error(e.getMessage()); e.printStackTrace(); return
	 * Collections.emptyList();
	 * 
	 * } }
	 * 
	 * else if (circle.equalsIgnoreCase("APCPDCL")) { try { String sql =
	 * "SELECT NVL(SUBSTR(BTSCNO,1,3),'TOTAL')CIRCLE, NVL(SUBSTR(BTBLCAT,0,1),'TOTAL')CAT,SUM(BTCURDEM+BTCOURT_LPC) TOTAL_DEMAND \r\n"
	 * +
	 * "FROM BILL_HIST WHERE TO_CHAR(BTBLDT,'MON-YYYY')=? and SUBSTR(BTSCNO,1,3) in ('CRD','ONG','GNT','VJA') GROUP BY CUBE (SUBSTR(BTBLCAT,0,1),SUBSTR(BTSCNO,1,3)) \r\n"
	 * + "UNION ALL \r\n" +
	 * "SELECT NVL(SUBSTR(BTSCNO,1,3),'TOTAL')CIRCLE,  NVL(SUBSTR(BTBLCAT,0,1),'TOTAL')CAT,SUM(BTCURDEM+BTCOURT_LPC) TOTAL_DEMAND \r\n"
	 * +
	 * "FROM BILL WHERE TO_CHAR(BTBLDT,'MON-YYYY')=? and SUBSTR(BTSCNO,1,3) in ('CRD','ONG','GNT','VJA')  GROUP BY CUBE (SUBSTR(BTBLCAT,0,1),SUBSTR(BTSCNO,1,3)) ORDER BY CIRCLE,CAT"
	 * ; log.info(sql); return jdbcTemplate.queryForList(sql,new Object[]
	 * {monthYear,monthYear}); } catch (DataAccessException e) {
	 * log.error(e.getMessage()); e.printStackTrace(); return
	 * Collections.emptyList();
	 * 
	 * } } else { try { String sql = "SELECT * FROM (\r\n" +
	 * "(SELECT  SUBSTR(BTSCNO,1,3)CIRCLE,SUBSTR(BTBLCAT,0,1)CAT,COUNT(SUBSTR(BTBLCAT,0,1)) NOS,SUM(BTRKWH_HT)BTRKWH_HT, SUM(BTRKVAH_HT)BTRKVAH_HT,SUM(BTBKVAH)BTBKVAH ,SUM(BTOA_KVAH+BTTP_KVA) Adjusted_Units, SUM(BTCURDEM)BTCURDEM, SUM(BTCOURT_LPC) BTCOURT_LPC,SUM(BTCURDEM+BTCOURT_LPC) TOTAL_DEMAND \r\n"
	 * + "FROM BILL \r\n" + "WHERE SUBSTR(BTSCNO,0,3)=? \r\n" +
	 * "AND TO_CHAR(BTBLDT,'MON-YYYY')=? \r\n" +
	 * "GROUP BY SUBSTR(BTBLCAT,0,1),SUBSTR(BTSCNO,1,3))\r\n" + "UNION ALL\r\n" +
	 * "(SELECT  SUBSTR(BTSCNO,1,3)CIRCLE,SUBSTR(BTBLCAT,0,1)CAT,COUNT(SUBSTR(BTBLCAT,0,1)) NOS,SUM(BTRKWH_HT)BTRKWH_HT, SUM(BTRKVAH_HT)BTRKVAH_HT,SUM(BTBKVAH)BTBKVAH ,SUM(BTOA_KVAH+BTTP_KVA) Adjusted_Units, SUM(BTCURDEM)BTCURDEM, SUM(BTCOURT_LPC) BTCOURT_LPC,SUM(BTCURDEM+BTCOURT_LPC) TOTAL_DEMAND \r\n"
	 * + "FROM BILL_HIST \r\n" + "WHERE SUBSTR(BTSCNO,0,3)=? \r\n" +
	 * "AND TO_CHAR(BTBLDT,'MON-YYYY')=? \r\n" +
	 * "GROUP BY SUBSTR(BTBLCAT,0,1),SUBSTR(BTSCNO,1,3) \r\n" + "))\r\n" +
	 * "ORDER BY CAT,CIRCLE\r\n" + ""; log.info(sql); return
	 * jdbcTemplate.queryForList(sql,new Object[]
	 * {circle,monthYear,circle,monthYear}); } catch (DataAccessException e) {
	 * log.error(e.getMessage()); e.printStackTrace(); return
	 * Collections.emptyList();
	 * 
	 * } } }
	 */

	public List<Map<String, Object>> getAcdReportDetails2022(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		String acd = request.getParameter("acd");
		String year = request.getParameter("year");
		String Levi_month = request.getParameter("monthyear") + "-" + request.getParameter("yearonly");
		if (!circle.equals("ALL")) {
			if (!acd.equals("ALL")) {
				try {
					String sql = "SELECT * FROM(\r\n"
							+ "SELECT CTACCT_ID,SUBSTR(CTUSCNO,1,3)CIRCLE ,CTUSCNO,decode(TRIM(ctgovt_pvt),'Y','GOVT','N','PVT','') type,CTCMD_HT,CTCAT,CTSUBCAT,CTACTUAL_KV,AVG_CONS_QTY AVG_BKVAH,ROUND(AVG(NET_TOD),1) AVG_TOD, REQ_SD TWO_MONTHS,AVAIL_SD DEP,NET_ACD BALANCE\r\n"
							+ "FROM CONS,ACD_CALC_HT_revised,ACD_UNITS_DTLS_revised\r\n"
							+ "WHERE ACD_UNITS_DTLS_revised.ACCT_ID=ACD_CALC_HT_revised.ACCT_ID(+) \r\n"
							+ "AND ACD_UNITS_DTLS_revised.ACCT_ID=CONS.CTACCT_ID(+)\r\n"
							+ "AND  SUBSTR(CTUSCNO,1,3)=? \r\n" + "AND ACD_CALC_HT.FIN_YEAR=? \r\n"
							+ "AND ACD_UNITS_DTLS_revised.FIN_YEAR= ? \r\n" + "AND LEVI_FLG=? \r\n"
							+ "GROUP BY SUBSTR(CTUSCNO,1,3),CTUSCNO,CTCMD_HT,CTCAT ,CTSUBCAT,CTACTUAL_KV,AVG_CONS_QTY,ACD_UNITS_DTLS_revised.ACCT_ID,CONS.CTACCT_ID,REQ_SD,AVAIL_SD,NET_ACD,ctgovt_pvt) A\r\n"
							+ "JOIN\r\n" + "(\r\n" + "SELECT ACCT_ID,FIN_YEAR,ENG,CUST,DMD,ED,TOD FROM \r\n"
							+ "(SELECT ACCT_ID,CHARGE_TYPE,FIN_YEAR,CHARGE_AMT FROM ACD_BILL_CALC_DTLS_revised WHERE FIN_YEAR=?)\r\n"
							+ "PIVOT (SUM(CHARGE_AMT)\r\n"
							+ "FOR CHARGE_TYPE IN ('ENG' ENG,'CUST' CUST,'DMD' DMD,'ED' ED,'TOD' TOD)))B\r\n"
							+ "ON A.CTACCT_ID=B.ACCT_ID\r\n" + "ORDER BY CTUSCNO";
					log.info(sql);
					return jdbcTemplate.queryForList(sql, new Object[] { circle, year, year, acd, year });
				} catch (DataAccessException e) {
					log.error(e.getMessage());
					log.error(e);
					return Collections.emptyList();
				}
			} else {
				try {
					String sql = "SELECT * FROM(\r\n"
							+ "SELECT CTACCT_ID,SUBSTR(CTUSCNO,1,3)CIRCLE ,decode(TRIM(ctgovt_pvt),'Y','GOVT','N','PVT','') type,CTUSCNO,CTCMD_HT,CTCAT,CTSUBCAT,CTACTUAL_KV,AVG_CONS_QTY AVG_BKVAH,ROUND(AVG(NET_TOD),1) AVG_TOD,REQ_SD TWO_MONTHS,AVAIL_SD DEP,NET_ACD BALANCE \r\n"
							+ "FROM CONS,ACD_CALC_HT_revised,ACD_UNITS_DTLS_revised\r\n"
							+ "WHERE ACD_UNITS_DTLS_revised.ACCT_ID=ACD_CALC_HT_revised.ACCT_ID(+) \r\n"
							+ "AND ACD_UNITS_DTLS_revised.ACCT_ID=CONS.CTACCT_ID(+)\r\n"
							+ "AND  SUBSTR(CTUSCNO,1,3)=? \r\n" + "AND ACD_CALC_HT_revised.FIN_YEAR=? \r\n"
							+ "AND ACD_UNITS_DTLS_revised.FIN_YEAR=?  \r\n"
							+ "GROUP BY SUBSTR(CTUSCNO,1,3),CTUSCNO,CTCMD_HT,CTCAT ,CTSUBCAT,CTACTUAL_KV,AVG_CONS_QTY,ACD_UNITS_DTLS_revised.ACCT_ID,CONS.CTACCT_ID,REQ_SD,AVAIL_SD,NET_ACD,ctgovt_pvt) A\r\n"
							+ "JOIN\r\n" + "(\r\n" + "SELECT ACCT_ID,FIN_YEAR,ENG,CUST,DMD,ED,TOD FROM \r\n"
							+ "(SELECT ACCT_ID,CHARGE_TYPE,FIN_YEAR,CHARGE_AMT FROM ACD_BILL_CALC_DTLS_revised WHERE FIN_YEAR=?)\r\n"
							+ "PIVOT (SUM(CHARGE_AMT)\r\n"
							+ "    FOR CHARGE_TYPE IN ('ENG' ENG,'CUST' CUST,'DMD' DMD,'ED' ED,'TOD' TOD)))B\r\n"
							+ "ON A.CTACCT_ID=B.ACCT_ID\r\n" + "ORDER BY CTUSCNO";
					log.info(sql);
					return jdbcTemplate.queryForList(sql, new Object[] { circle, year, year, year });
				} catch (DataAccessException e) {
					log.error(e.getMessage());
					e.printStackTrace();
					return Collections.emptyList();
				}
			}

		} else {
			try {
				String sql = "SELECT * FROM(\r\n"
						+ "SELECT CTACCT_ID,SUBSTR(CTUSCNO,1,3)CIRCLE ,CTUSCNO,decode(TRIM(ctgovt_pvt),'Y','GOVT','N','PVT','') type,CTCMD_HT,CTCAT,CTSUBCAT,CTACTUAL_KV,AVG_CONS_QTY AVG_BKVAH,ROUND(AVG(NET_TOD),1) AVG_TOD , REQ_SD TWO_MONTHS,AVAIL_SD DEP,NET_ACD BALANCE\r\n"
						+ "FROM CONS,ACD_CALC_HT_revised,ACD_UNITS_DTLS_revised\r\n"
						+ "WHERE ACD_UNITS_DTLS_revised.ACCT_ID=ACD_CALC_HT_revised.ACCT_ID(+) \r\n"
						+ "AND ACD_UNITS_DTLS_revised.ACCT_ID=CONS.CTACCT_ID(+)\r\n" + "AND ACD_CALC_HT.FIN_YEAR=? \r\n"
						+ "AND ACD_UNITS_DTLS_revised.FIN_YEAR=?  AND LEVI_MTH=? \r\n"
						+ "GROUP BY SUBSTR(CTUSCNO,1,3),CTUSCNO,CTCMD_HT,CTCAT ,CTSUBCAT,CTACTUAL_KV,AVG_CONS_QTY,ACD_UNITS_DTLS_revised.ACCT_ID,CONS.CTACCT_ID,REQ_SD,AVAIL_SD,NET_ACD,ctgovt_pvt) A\r\n"
						+ "JOIN\r\n" + "(\r\n" + "SELECT ACCT_ID,FIN_YEAR,ENG,CUST,DMD,ED,TOD FROM \r\n"
						+ "(SELECT ACCT_ID,CHARGE_TYPE,FIN_YEAR,CHARGE_AMT FROM ACD_BILL_CALC_DTLS_revised WHERE FIN_YEAR=?)\r\n"
						+ "PIVOT (SUM(CHARGE_AMT)\r\n"
						+ "    FOR CHARGE_TYPE IN ('ENG' ENG,'CUST' CUST,'DMD' DMD,'ED' ED,'TOD' TOD)))B\r\n"
						+ "ON A.CTACCT_ID=B.ACCT_ID\r\n" + "ORDER BY CTUSCNO";
				return jdbcTemplate.queryForList(sql, new Object[] { year, year, Levi_month, year });
			} catch (DataAccessException e) {
				log.error(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}

		}

	}

	public List<Map<String, Object>> getAcdReportDetails(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		String acd = request.getParameter("acd");
		String year = request.getParameter("year");
		String Levi_month = request.getParameter("monthyear") + "-" + request.getParameter("yearonly");
		if (!circle.equals("ALL")) {
			if (!acd.equals("ALL")) {
				try {
					String sql = "SELECT * FROM(\r\n"
							+ "SELECT CTACCT_ID,SUBSTR(CTUSCNO,1,3)CIRCLE ,CTUSCNO,decode(TRIM(ctgovt_pvt),'Y','GOVT','N','PVT','') type,CTCMD_HT,CTCAT,CTSUBCAT,CTACTUAL_KV,AVG_CONS_QTY AVG_BKVAH,ROUND(AVG(NET_TOD),1) AVG_TOD, REQ_SD TWO_MONTHS,AVAIL_SD DEP,NET_ACD BALANCE\r\n"
							+ "FROM CONS,ACD_CALC_HT,ACD_UNITS_DTLS\r\n"
							+ "WHERE ACD_UNITS_DTLS.ACCT_ID=ACD_CALC_HT.ACCT_ID(+) \r\n"
							+ "AND ACD_UNITS_DTLS.ACCT_ID=CONS.CTACCT_ID(+)\r\n" + "AND  SUBSTR(CTUSCNO,1,3)=? \r\n"
							+ "AND ACD_CALC_HT.FIN_YEAR=? \r\n" + "AND ACD_UNITS_DTLS.FIN_YEAR= ? \r\n"
							+ "AND LEVI_FLG=? \r\n"
							+ "GROUP BY SUBSTR(CTUSCNO,1,3),CTUSCNO,CTCMD_HT,CTCAT ,CTSUBCAT,CTACTUAL_KV,AVG_CONS_QTY,ACD_UNITS_DTLS.ACCT_ID,CONS.CTACCT_ID,REQ_SD,AVAIL_SD,NET_ACD,ctgovt_pvt) A\r\n"
							+ "JOIN\r\n" + "(\r\n" + "SELECT ACCT_ID,FIN_YEAR,ENG,CUST,DMD,ED,TOD FROM \r\n"
							+ "(SELECT ACCT_ID,CHARGE_TYPE,FIN_YEAR,CHARGE_AMT FROM ACD_BILL_CALC_DTLS WHERE FIN_YEAR=?)\r\n"
							+ "PIVOT (SUM(CHARGE_AMT)\r\n"
							+ "FOR CHARGE_TYPE IN ('ENG' ENG,'CUST' CUST,'DMD' DMD,'ED' ED,'TOD' TOD)))B\r\n"
							+ "ON A.CTACCT_ID=B.ACCT_ID\r\n" + "ORDER BY CTUSCNO";
					log.info(sql);
					return jdbcTemplate.queryForList(sql, new Object[] { circle, year, year, acd, year });
				} catch (DataAccessException e) {
					log.error(e.getMessage());
					log.error(e);
					return Collections.emptyList();
				}
			} else {
				try {
					String sql = "SELECT * FROM(\r\n"
							+ "SELECT CTACCT_ID,SUBSTR(CTUSCNO,1,3)CIRCLE ,decode(TRIM(ctgovt_pvt),'Y','GOVT','N','PVT','') type,CTUSCNO,CTCMD_HT,CTCAT,CTSUBCAT,CTACTUAL_KV,AVG_CONS_QTY AVG_BKVAH,ROUND(AVG(NET_TOD),1) AVG_TOD,REQ_SD TWO_MONTHS,AVAIL_SD DEP,NET_ACD BALANCE \r\n"
							+ "FROM CONS,ACD_CALC_HT,ACD_UNITS_DTLS\r\n"
							+ "WHERE ACD_UNITS_DTLS.ACCT_ID=ACD_CALC_HT.ACCT_ID(+) \r\n"
							+ "AND ACD_UNITS_DTLS.ACCT_ID=CONS.CTACCT_ID(+)\r\n" + "AND  SUBSTR(CTUSCNO,1,3)=? \r\n"
							+ "AND ACD_CALC_HT.FIN_YEAR=? \r\n" + "AND ACD_UNITS_DTLS.FIN_YEAR=?  \r\n"
							+ "GROUP BY SUBSTR(CTUSCNO,1,3),CTUSCNO,CTCMD_HT,CTCAT ,CTSUBCAT,CTACTUAL_KV,AVG_CONS_QTY,ACD_UNITS_DTLS.ACCT_ID,CONS.CTACCT_ID,REQ_SD,AVAIL_SD,NET_ACD,ctgovt_pvt) A\r\n"
							+ "JOIN\r\n" + "(\r\n" + "SELECT ACCT_ID,FIN_YEAR,ENG,CUST,DMD,ED,TOD FROM \r\n"
							+ "(SELECT ACCT_ID,CHARGE_TYPE,FIN_YEAR,CHARGE_AMT FROM ACD_BILL_CALC_DTLS WHERE FIN_YEAR=?)\r\n"
							+ "PIVOT (SUM(CHARGE_AMT)\r\n"
							+ "    FOR CHARGE_TYPE IN ('ENG' ENG,'CUST' CUST,'DMD' DMD,'ED' ED,'TOD' TOD)))B\r\n"
							+ "ON A.CTACCT_ID=B.ACCT_ID\r\n" + "ORDER BY CTUSCNO";
					log.info(sql);
					return jdbcTemplate.queryForList(sql, new Object[] { circle, year, year, year });
				} catch (DataAccessException e) {
					log.error(e.getMessage());
					e.printStackTrace();
					return Collections.emptyList();
				}
			}

		} else {
			try {
				String sql = "SELECT * FROM(\r\n"
						+ "SELECT CTACCT_ID,SUBSTR(CTUSCNO,1,3)CIRCLE ,CTUSCNO,decode(TRIM(ctgovt_pvt),'Y','GOVT','N','PVT','') type,CTCMD_HT,CTCAT,CTSUBCAT,CTACTUAL_KV,AVG_CONS_QTY AVG_BKVAH,ROUND(AVG(NET_TOD),1) AVG_TOD , REQ_SD TWO_MONTHS,AVAIL_SD DEP,NET_ACD BALANCE\r\n"
						+ "FROM CONS,ACD_CALC_HT,ACD_UNITS_DTLS\r\n"
						+ "WHERE ACD_UNITS_DTLS.ACCT_ID=ACD_CALC_HT.ACCT_ID(+) \r\n"
						+ "AND ACD_UNITS_DTLS.ACCT_ID=CONS.CTACCT_ID(+)\r\n" + "AND ACD_CALC_HT.FIN_YEAR=? \r\n"
						+ "AND ACD_UNITS_DTLS.FIN_YEAR=?  AND LEVI_MTH=? \r\n"
						+ "GROUP BY SUBSTR(CTUSCNO,1,3),CTUSCNO,CTCMD_HT,CTCAT ,CTSUBCAT,CTACTUAL_KV,AVG_CONS_QTY,ACD_UNITS_DTLS.ACCT_ID,CONS.CTACCT_ID,REQ_SD,AVAIL_SD,NET_ACD,ctgovt_pvt) A\r\n"
						+ "JOIN\r\n" + "(\r\n" + "SELECT ACCT_ID,FIN_YEAR,ENG,CUST,DMD,ED,TOD FROM \r\n"
						+ "(SELECT ACCT_ID,CHARGE_TYPE,FIN_YEAR,CHARGE_AMT FROM ACD_BILL_CALC_DTLS WHERE FIN_YEAR=?)\r\n"
						+ "PIVOT (SUM(CHARGE_AMT)\r\n"
						+ "    FOR CHARGE_TYPE IN ('ENG' ENG,'CUST' CUST,'DMD' DMD,'ED' ED,'TOD' TOD)))B\r\n"
						+ "ON A.CTACCT_ID=B.ACCT_ID\r\n" + "ORDER BY CTUSCNO";
				return jdbcTemplate.queryForList(sql, new Object[] { year, year, Levi_month, year });
			} catch (DataAccessException e) {
				log.error(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}

		}

	}

	public List<Map<String, Object>> getAcdRateCharges(HttpServletRequest request) {
		String year = request.getParameter("year");
		try {
			String sql = "SELECT ACCT_ID,FIN_YEAR,NVL(ENG,0)ENG,NVL(CUST,0)CUST,NVL(DMD,0)DMD,NVL(ED,0)ED,NVL(TOD,0)TOD FROM \r\n"
					+ "(SELECT ACCT_ID,CHARGE_TYPE,FIN_YEAR,CHARGE_AMT FROM ACD_BILL_CALC_DTLS WHERE FIN_YEAR=?)\r\n"
					+ "PIVOT (SUM(CHARGE_AMT)\r\n"
					+ "FOR CHARGE_TYPE IN ('ENG' ENG,'CUST' CUST,'DMD' DMD,'ED' ED,'TOD' TOD))";
			log.info(sql);
			return jdbcTemplate.queryForList(sql, new Object[] { year });
		} catch (DataAccessException e) {
			log.error(e.getMessage());
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	public List<Map<String, Object>> getAcdReportDetailsCurrent(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		String acd = request.getParameter("acd");
		String year = request.getParameter("year");
		String monthyear = request.getParameter("monthyear") + "-" + request.getParameter("yearonly");
		if (!circle.equals("ALL")) {
			if (!acd.equals("ALL")) {
				try {
					String sql = "SELECT * FROM(\r\n"
							+ "SELECT CTACCT_ID,SUBSTR(CTUSCNO,1,3)CIRCLE ,CTUSCNO,decode(TRIM(ctgovt_pvt),'Y','GOVT','N','PVT','') type,CTCMD_HT,CTCAT,CTSUBCAT,CTACTUAL_KV,AVG_CONS_QTY AVG_BKVAH,ROUND(AVG(NET_TOD),1) AVG_TOD, REQ_SD TWO_MONTHS,AVAIL_SD DEP,NET_ACD BALANCE, nvl(AVG(TRUE_UP_CHG),0) TRUE_UP_CHG, nvl(AVG(FPPCA_CHG),0) FPPCA_CHG, nvl(AVG(FPPCA_CHG_P),0) FPPCA_CHG_P, nvl(AVG(ISD_ADJ),0) ISD_ADJ,nvl(AVG(ACD_BAL_PAY),0) ACD_BAL_PAY   \r\n"
							+ "FROM CONS,ACD_CALC_HT,ACD_UNITS_DTLS\r\n"
							+ "WHERE ACD_UNITS_DTLS.ACCT_ID=ACD_CALC_HT.ACCT_ID(+) \r\n"
							+ "AND ACD_UNITS_DTLS.ACCT_ID=CONS.CTACCT_ID(+)\r\n" + "AND  SUBSTR(CTUSCNO,1,3)=? \r\n"
							+ "AND ACD_CALC_HT.FIN_YEAR=? \r\n"
							/* + "AND  MONTHS_BETWEEN(to_Date('"+monthyear+"','MON-YYYY'),CTSUPCONDT)>=3" */
							+ "AND ACD_UNITS_DTLS.FIN_YEAR=? \r\n" + "AND LEVI_FLG= ?  AND LEVI_MTH= ? \r\n"
							+ " GROUP BY SUBSTR(CTUSCNO,1,3),CTUSCNO,decode(TRIM(ctgovt_pvt),'Y','GOVT','N','PVT',''),CTCMD_HT,CTCAT ,CTSUBCAT,CTACTUAL_KV,AVG_CONS_QTY,ACD_UNITS_DTLS.ACCT_ID,CONS.CTACCT_ID,REQ_SD,AVAIL_SD,NET_ACD) A\r\n"
							+ "JOIN\r\n"
							+ "(SELECT ACCT_ID,FIN_YEAR,ENG,CUST,DMD,ED,NVL(TOD,0) + NVL(TOD_OP_HGD,0)+NVL(TOD_OP_LGD,0)+NVL(TOD_P_HGD,0)+NVL(TOD_P_LGD,0) TOD FROM \r\n"
							+ "(SELECT ACCT_ID,CHARGE_TYPE,FIN_YEAR,CHARGE_AMT FROM ACD_BILL_CALC_DTLS WHERE FIN_YEAR='"
							+ year + "' AND LEVI_MTH=? )\r\n" + "PIVOT (SUM(CHARGE_AMT)\r\n"
							+ "    FOR CHARGE_TYPE IN ('ENG' ENG,'CUST' CUST,'DMD' DMD,'ED' ED,'TOD' TOD,'TOD-OP-HGD' TOD_OP_HGD,'TOD-OP-LGD' TOD_OP_LGD,'TOD-P-HGD' TOD_P_HGD,'TOD-P-LGD' TOD_P_LGD)))B\r\n"
							+ "ON A.CTACCT_ID=B.ACCT_ID\r\n" + "ORDER BY CTUSCNO";
					log.info(sql);
					return jdbcTemplate.queryForList(sql,
							new Object[] { circle, year, year, acd, monthyear, monthyear });
				} catch (DataAccessException e) {
					log.error(e.getMessage());
					e.printStackTrace();
					return Collections.emptyList();
				}
			} else {
				try {
					String sql = "SELECT * FROM(\r\n"
							+ "SELECT CTACCT_ID,SUBSTR(CTUSCNO,1,3)CIRCLE ,CTUSCNO,decode(TRIM(ctgovt_pvt),'Y','GOVT','N','PVT','') type,CTCMD_HT,CTCAT,CTSUBCAT,CTACTUAL_KV,AVG_CONS_QTY AVG_BKVAH,ROUND(AVG(NET_TOD),1) AVG_TOD,REQ_SD TWO_MONTHS,AVAIL_SD DEP,NET_ACD BALANCE ,nvl(AVG(TRUE_UP_CHG),0) TRUE_UP_CHG, nvl(AVG(FPPCA_CHG),0) FPPCA_CHG, nvl(AVG(FPPCA_CHG_P),0) FPPCA_CHG_P, nvl(AVG(ISD_ADJ),0) ISD_ADJ,nvl(AVG(ACD_BAL_PAY),0) ACD_BAL_PAY    \r\n"
							+ "FROM CONS,ACD_CALC_HT,ACD_UNITS_DTLS\r\n"
							+ "WHERE ACD_UNITS_DTLS.ACCT_ID=ACD_CALC_HT.ACCT_ID(+) \r\n"
							+ "AND ACD_UNITS_DTLS.ACCT_ID=CONS.CTACCT_ID(+)\r\n" + "AND  SUBSTR(CTUSCNO,1,3)=?\r\n"
							+ "AND ACD_CALC_HT.FIN_YEAR=?\r\n"
							/* + "AND  MONTHS_BETWEEN(to_Date('"+monthyear+"','MON-YYYY'),CTSUPCONDT)>=3" */
							+ "AND ACD_UNITS_DTLS.FIN_YEAR=?  AND LEVI_MTH=? \r\n"
							+ "GROUP BY SUBSTR(CTUSCNO,1,3),CTUSCNO,CTCMD_HT,CTCAT ,CTSUBCAT,CTACTUAL_KV,AVG_CONS_QTY,ACD_UNITS_DTLS.ACCT_ID,CONS.CTACCT_ID,REQ_SD,AVAIL_SD,NET_ACD,ctgovt_pvt) A\r\n"
							+ "JOIN\r\n"
							+ "(SELECT ACCT_ID,FIN_YEAR,ENG,CUST,DMD,ED,NVL(TOD,0) + NVL(TOD_OP_HGD,0)+NVL(TOD_OP_LGD,0)+NVL(TOD_P_HGD,0)+NVL(TOD_P_LGD,0) TOD FROM \r\n"
							+ "(SELECT ACCT_ID,CHARGE_TYPE,FIN_YEAR,CHARGE_AMT FROM ACD_BILL_CALC_DTLS WHERE FIN_YEAR='"
							+ year + "' AND LEVI_MTH=? )\r\n" + "PIVOT (SUM(CHARGE_AMT)\r\n"
							+ "    FOR CHARGE_TYPE IN ('ENG' ENG,'CUST' CUST,'DMD' DMD,'ED' ED,'TOD' TOD,'TOD-OP-HGD' TOD_OP_HGD,'TOD-OP-LGD' TOD_OP_LGD,'TOD-P-HGD' TOD_P_HGD,'TOD-P-LGD' TOD_P_LGD)))B\r\n"
							+ "ON A.CTACCT_ID=B.ACCT_ID\r\n" + "ORDER BY CTUSCNO";
					log.info(sql);
					return jdbcTemplate.queryForList(sql, new Object[] { circle, year, year, monthyear, monthyear });
				} catch (DataAccessException e) {
					log.error(e.getMessage());
					e.printStackTrace();
					return Collections.emptyList();
				}
			}

		} else {
			if (!acd.equals("ALL")) {
				try {
					String sql = "SELECT * FROM(\r\n"
							+ "SELECT CTACCT_ID,SUBSTR(CTUSCNO,1,3)CIRCLE ,CTUSCNO,decode(TRIM(ctgovt_pvt),'Y','GOVT','N','PVT','') type,CTCMD_HT,CTCAT,CTSUBCAT,CTACTUAL_KV,AVG_CONS_QTY AVG_BKVAH,ROUND(AVG(NET_TOD),1) AVG_TOD , REQ_SD TWO_MONTHS,AVAIL_SD DEP,NET_ACD BALANCE ,nvl(AVG(TRUE_UP_CHG),0) TRUE_UP_CHG, nvl(AVG(FPPCA_CHG),0) FPPCA_CHG, nvl(AVG(FPPCA_CHG_P),0) FPPCA_CHG_P, nvl(AVG(ISD_ADJ),0) ISD_ADJ,nvl(AVG(ACD_BAL_PAY),0) ACD_BAL_PAY    \r\n"
							+ "FROM CONS,ACD_CALC_HT,ACD_UNITS_DTLS\r\n"
							+ "WHERE ACD_UNITS_DTLS.ACCT_ID=ACD_CALC_HT.ACCT_ID(+) \r\n"
							+ "AND ACD_UNITS_DTLS.ACCT_ID=CONS.CTACCT_ID(+)\r\n" + "AND ACD_CALC_HT.FIN_YEAR=?\r\n"
							+ "AND ACD_UNITS_DTLS.FIN_YEAR=?  AND LEVI_MTH=? AND LEVI_FLG= ? \r\n"
							+ "GROUP BY SUBSTR(CTUSCNO,1,3),CTUSCNO,CTCMD_HT,CTCAT ,CTSUBCAT,CTACTUAL_KV,AVG_CONS_QTY,ACD_UNITS_DTLS.ACCT_ID,CONS.CTACCT_ID,REQ_SD,AVAIL_SD,NET_ACD,ctgovt_pvt) A\r\n"
							+ "JOIN\r\n"
							+ "(SELECT ACCT_ID,FIN_YEAR,ENG,CUST,DMD,ED,NVL(TOD,0) + NVL(TOD_OP_HGD,0)+NVL(TOD_OP_LGD,0)+NVL(TOD_P_HGD,0)+NVL(TOD_P_LGD,0) TOD FROM \r\n"
							+ "(SELECT ACCT_ID,CHARGE_TYPE,FIN_YEAR,CHARGE_AMT FROM ACD_BILL_CALC_DTLS WHERE FIN_YEAR='"
							+ year + "' AND LEVI_MTH=? )\r\n" + "PIVOT (SUM(CHARGE_AMT)\r\n"
							+ "    FOR CHARGE_TYPE IN ('ENG' ENG,'CUST' CUST,'DMD' DMD,'ED' ED,'TOD' TOD,'TOD-OP-HGD' TOD_OP_HGD,'TOD-OP-LGD' TOD_OP_LGD,'TOD-P-HGD' TOD_P_HGD,'TOD-P-LGD' TOD_P_LGD)))B\r\n"
							+ "ON A.CTACCT_ID=B.ACCT_ID\r\n" + "ORDER BY CTUSCNO";
					log.info(sql);
					return jdbcTemplate.queryForList(sql, new Object[] { year, year, monthyear, acd, monthyear });
				} catch (DataAccessException e) {
					log.error(e.getMessage());
					e.printStackTrace();
					return Collections.emptyList();
				}

			} else {

				try {
					String sql = "SELECT * FROM(\r\n"
							+ "SELECT CTACCT_ID,SUBSTR(CTUSCNO,1,3)CIRCLE ,CTUSCNO,decode(TRIM(ctgovt_pvt),'Y','GOVT','N','PVT','') type,CTCMD_HT,CTCAT,CTSUBCAT,CTACTUAL_KV,AVG_CONS_QTY AVG_BKVAH,ROUND(AVG(NET_TOD),1) AVG_TOD , REQ_SD TWO_MONTHS,AVAIL_SD DEP,NET_ACD BALANCE ,nvl(AVG(TRUE_UP_CHG),0) TRUE_UP_CHG, nvl(AVG(FPPCA_CHG),0) FPPCA_CHG, nvl(AVG(FPPCA_CHG_P),0) FPPCA_CHG_P, nvl(AVG(ISD_ADJ),0) ISD_ADJ,nvl(AVG(ACD_BAL_PAY),0) ACD_BAL_PAY    \r\n"
							+ "FROM CONS,ACD_CALC_HT,ACD_UNITS_DTLS\r\n"
							+ "WHERE ACD_UNITS_DTLS.ACCT_ID=ACD_CALC_HT.ACCT_ID(+) \r\n"
							+ "AND ACD_UNITS_DTLS.ACCT_ID=CONS.CTACCT_ID(+)\r\n" + "AND ACD_CALC_HT.FIN_YEAR=?\r\n"
							+ "AND ACD_UNITS_DTLS.FIN_YEAR=?  AND LEVI_MTH=? \r\n"
							+ "GROUP BY SUBSTR(CTUSCNO,1,3),CTUSCNO,CTCMD_HT,CTCAT ,CTSUBCAT,CTACTUAL_KV,AVG_CONS_QTY,ACD_UNITS_DTLS.ACCT_ID,CONS.CTACCT_ID,REQ_SD,AVAIL_SD,NET_ACD,ctgovt_pvt) A\r\n"
							+ "JOIN\r\n"
							+ "(SELECT ACCT_ID,FIN_YEAR,ENG,CUST,DMD,ED,NVL(TOD,0) + NVL(TOD_OP_HGD,0)+NVL(TOD_OP_LGD,0)+NVL(TOD_P_HGD,0)+NVL(TOD_P_LGD,0) TOD FROM \r\n"
							+ "(SELECT ACCT_ID,CHARGE_TYPE,FIN_YEAR,CHARGE_AMT FROM ACD_BILL_CALC_DTLS WHERE FIN_YEAR='"
							+ year + "' AND LEVI_MTH=? )\r\n" + "PIVOT (SUM(CHARGE_AMT)\r\n"
							+ "    FOR CHARGE_TYPE IN ('ENG' ENG,'CUST' CUST,'DMD' DMD,'ED' ED,'TOD' TOD,'TOD-OP-HGD' TOD_OP_HGD,'TOD-OP-LGD' TOD_OP_LGD,'TOD-P-HGD' TOD_P_HGD,'TOD-P-LGD' TOD_P_LGD)))B\r\n"
							+ "ON A.CTACCT_ID=B.ACCT_ID\r\n" + "ORDER BY CTUSCNO";
					log.info(sql);
					return jdbcTemplate.queryForList(sql, new Object[] { year, year, monthyear, monthyear });
				} catch (DataAccessException e) {
					log.error(e.getMessage());
					e.printStackTrace();
					return Collections.emptyList();
				}

			}

		}

	}

	public List<Map<String, Object>> getAcdReportDetailsCurrent2022(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		String acd = request.getParameter("acd");
		String year = request.getParameter("year");
		String monthyear = request.getParameter("monthyear") + "-" + request.getParameter("yearonly");
		if (!circle.equals("ALL")) {
			if (!acd.equals("ALL")) {
				try {
					String sql = "SELECT * FROM(\r\n"
							+ "SELECT CTACCT_ID,SUBSTR(CTUSCNO,1,3)CIRCLE ,CTUSCNO,decode(TRIM(ctgovt_pvt),'Y','GOVT','N','PVT','') type,CTCMD_HT,CTCAT,CTSUBCAT,CTACTUAL_KV,AVG_CONS_QTY AVG_BKVAH,ROUND(AVG(NET_TOD),1) AVG_TOD, REQ_SD TWO_MONTHS,AVAIL_SD DEP,NET_ACD BALANCE\r\n"
							+ "FROM CONS,ACD_CALC_HT_revised,ACD_UNITS_DTLS_revised\r\n"
							+ "WHERE ACD_UNITS_DTLS_revised.ACCT_ID=ACD_CALC_HT_revised.ACCT_ID(+) \r\n"
							+ "AND ACD_UNITS_DTLS_revised.ACCT_ID=CONS.CTACCT_ID(+)\r\n"
							+ "AND  SUBSTR(CTUSCNO,1,3)=? \r\n" + "AND ACD_CALC_HT_revised.FIN_YEAR=? \r\n"
							+ "AND ACD_UNITS_DTLS_revised.FIN_YEAR=? \r\n" + "AND LEVI_FLG= ?  AND LEVI_MTH= ? \r\n"
							+ "GROUP BY SUBSTR(CTUSCNO,1,3),CTUSCNO,CTCMD_HT,CTCAT ,CTSUBCAT,CTACTUAL_KV,AVG_CONS_QTY,ACD_UNITS_DTLS_revised.ACCT_ID,CONS.CTACCT_ID,REQ_SD,AVAIL_SD,NET_ACD,ctgovt_pvt) A\r\n"
							+ "JOIN\r\n"
							+ "(SELECT ACCT_ID,FIN_YEAR,ENG,CUST,DMD,ED,NVL(TOD,0) + NVL(TOD_OP_HGD,0)+NVL(TOD_OP_LGD,0)+NVL(TOD_P_HGD,0)+NVL(TOD_P_LGD,0) TOD FROM \r\n"
							+ "(SELECT ACCT_ID,CHARGE_TYPE,FIN_YEAR,CHARGE_AMT FROM ACD_BILL_CALC_DTLS_revised WHERE FIN_YEAR=? AND LEVI_MTH=? )\r\n"
							+ "PIVOT (SUM(CHARGE_AMT)\r\n"
							+ "    FOR CHARGE_TYPE IN ('ENG' ENG,'CUST' CUST,'DMD' DMD,'ED' ED,'TOD' TOD,'TOD-OP-HGD' TOD_OP_HGD,'TOD-OP-LGD' TOD_OP_LGD,'TOD-P-HGD' TOD_P_HGD,'TOD-P-LGD' TOD_P_LGD)))B\r\n"
							+ "ON A.CTACCT_ID=B.ACCT_ID\r\n" + "ORDER BY CTUSCNO";
					log.info(sql);
					return jdbcTemplate.queryForList(sql,
							new Object[] { circle, year, year, acd, monthyear, year, monthyear });
				} catch (DataAccessException e) {
					log.error(e.getMessage());
					e.printStackTrace();
					return Collections.emptyList();
				}
			} else {
				try {
					String sql = "SELECT * FROM(\r\n"
							+ "SELECT CTACCT_ID,SUBSTR(CTUSCNO,1,3)CIRCLE ,CTUSCNO,decode(TRIM(ctgovt_pvt),'Y','GOVT','N','PVT','') type,CTCMD_HT,CTCAT,CTSUBCAT,CTACTUAL_KV,AVG_CONS_QTY AVG_BKVAH,ROUND(AVG(NET_TOD),1) AVG_TOD,REQ_SD TWO_MONTHS,AVAIL_SD DEP,NET_ACD BALANCE \r\n"
							+ "FROM CONS,ACD_CALC_HT_revised,ACD_UNITS_DTLS_revised\r\n"
							+ "WHERE ACD_UNITS_DTLS_revised.ACCT_ID=ACD_CALC_HT_revised.ACCT_ID(+) \r\n"
							+ "AND ACD_UNITS_DTLS_revised.ACCT_ID=CONS.CTACCT_ID(+)\r\n"
							+ "AND  SUBSTR(CTUSCNO,1,3)=?\r\n" + "AND ACD_CALC_HT_revised.FIN_YEAR=?\r\n"
							+ "AND ACD_UNITS_DTLS_revised.FIN_YEAR=?  AND LEVI_MTH=? \r\n"
							+ "GROUP BY SUBSTR(CTUSCNO,1,3),CTUSCNO,CTCMD_HT,CTCAT ,CTSUBCAT,CTACTUAL_KV,AVG_CONS_QTY,ACD_UNITS_DTLS_revised.ACCT_ID,CONS.CTACCT_ID,REQ_SD,AVAIL_SD,NET_ACD,ctgovt_pvt) A\r\n"
							+ "JOIN\r\n"
							+ "(SELECT ACCT_ID,FIN_YEAR,ENG,CUST,DMD,ED,NVL(TOD,0) + NVL(TOD_OP_HGD,0)+NVL(TOD_OP_LGD,0)+NVL(TOD_P_HGD,0)+NVL(TOD_P_LGD,0) TOD FROM \r\n"
							+ "(SELECT ACCT_ID,CHARGE_TYPE,FIN_YEAR,CHARGE_AMT FROM ACD_BILL_CALC_DTLS_revised WHERE FIN_YEAR==? AND LEVI_MTH=? )\r\n"
							+ "PIVOT (SUM(CHARGE_AMT)\r\n"
							+ "    FOR CHARGE_TYPE IN ('ENG' ENG,'CUST' CUST,'DMD' DMD,'ED' ED,'TOD' TOD,'TOD-OP-HGD' TOD_OP_HGD,'TOD-OP-LGD' TOD_OP_LGD,'TOD-P-HGD' TOD_P_HGD,'TOD-P-LGD' TOD_P_LGD)))B\r\n"
							+ "ON A.CTACCT_ID=B.ACCT_ID\r\n" + "ORDER BY CTUSCNO";
					log.info(sql);
					return jdbcTemplate.queryForList(sql,
							new Object[] { circle, year, year, monthyear, year, monthyear });
				} catch (DataAccessException e) {
					log.error(e.getMessage());
					e.printStackTrace();
					return Collections.emptyList();
				}
			}

		} else {
			if (!acd.equals("ALL")) {
				try {
					String sql = "SELECT * FROM(\r\n"
							+ "SELECT CTACCT_ID,SUBSTR(CTUSCNO,1,3)CIRCLE ,CTUSCNO,decode(TRIM(ctgovt_pvt),'Y','GOVT','N','PVT','') type,CTCMD_HT,CTCAT,CTSUBCAT,CTACTUAL_KV,AVG_CONS_QTY AVG_BKVAH,ROUND(AVG(NET_TOD),1) AVG_TOD , REQ_SD TWO_MONTHS,AVAIL_SD DEP,NET_ACD BALANCE\r\n"
							+ "FROM CONS,ACD_CALC_HT_revised,ACD_UNITS_DTLS_revised\r\n"
							+ "WHERE ACD_UNITS_DTLS_revised.ACCT_ID=ACD_CALC_HT_revised.ACCT_ID(+) \r\n"
							+ "AND ACD_UNITS_DTLS_revised.ACCT_ID=CONS.CTACCT_ID(+)\r\n"
							+ "AND ACD_CALC_HT_revised.FIN_YEAR=?\r\n"
							+ "AND ACD_UNITS_DTLS_revised.FIN_YEAR=?  AND LEVI_MTH=? AND LEVI_FLG= ? \r\n"
							+ "GROUP BY SUBSTR(CTUSCNO,1,3),CTUSCNO,CTCMD_HT,CTCAT ,CTSUBCAT,CTACTUAL_KV,AVG_CONS_QTY,ACD_UNITS_DTLS_revised.ACCT_ID,CONS.CTACCT_ID,REQ_SD,AVAIL_SD,NET_ACD,ctgovt_pvt) A\r\n"
							+ "JOIN\r\n"
							+ "(SELECT ACCT_ID,FIN_YEAR,ENG,CUST,DMD,ED,NVL(TOD,0) + NVL(TOD_OP_HGD,0)+NVL(TOD_OP_LGD,0)+NVL(TOD_P_HGD,0)+NVL(TOD_P_LGD,0) TOD FROM \r\n"
							+ "(SELECT ACCT_ID,CHARGE_TYPE,FIN_YEAR,CHARGE_AMT FROM ACD_BILL_CALC_DTLS_revised WHERE FIN_YEAR='"
							+ year + "' AND LEVI_MTH=? )\r\n" + "PIVOT (SUM(CHARGE_AMT)\r\n"
							+ "    FOR CHARGE_TYPE IN ('ENG' ENG,'CUST' CUST,'DMD' DMD,'ED' ED,'TOD' TOD,'TOD-OP-HGD' TOD_OP_HGD,'TOD-OP-LGD' TOD_OP_LGD,'TOD-P-HGD' TOD_P_HGD,'TOD-P-LGD' TOD_P_LGD)))B\r\n"
							+ "ON A.CTACCT_ID=B.ACCT_ID\r\n" + "ORDER BY CTUSCNO";
					log.info(sql);
					return jdbcTemplate.queryForList(sql, new Object[] { year, year, monthyear, acd, monthyear });
				} catch (DataAccessException e) {
					log.error(e.getMessage());
					e.printStackTrace();
					return Collections.emptyList();
				}

			} else {

				try {
					String sql = "SELECT * FROM(\r\n"
							+ "SELECT CTACCT_ID,SUBSTR(CTUSCNO,1,3)CIRCLE ,CTUSCNO,decode(TRIM(ctgovt_pvt),'Y','GOVT','N','PVT','') type,CTCMD_HT,CTCAT,CTSUBCAT,CTACTUAL_KV,AVG_CONS_QTY AVG_BKVAH,ROUND(AVG(NET_TOD),1) AVG_TOD , REQ_SD TWO_MONTHS,AVAIL_SD DEP,NET_ACD BALANCE\r\n"
							+ "FROM CONS,ACD_CALC_HT_revised,ACD_UNITS_DTLS_revised\r\n"
							+ "WHERE ACD_UNITS_DTLS_revised.ACCT_ID=ACD_CALC_HT_revised.ACCT_ID(+) \r\n"
							+ "AND ACD_UNITS_DTLS_revised.ACCT_ID=CONS.CTACCT_ID(+)\r\n"
							+ "AND ACD_CALC_HT_revised.FIN_YEAR=?\r\n"
							+ "AND ACD_UNITS_DTLS_revised.FIN_YEAR=?  AND LEVI_MTH=? \r\n"
							+ "GROUP BY SUBSTR(CTUSCNO,1,3),CTUSCNO,CTCMD_HT,CTCAT ,CTSUBCAT,CTACTUAL_KV,AVG_CONS_QTY,ACD_UNITS_DTLS_revised.ACCT_ID,CONS.CTACCT_ID,REQ_SD,AVAIL_SD,NET_ACD,ctgovt_pvt) A\r\n"
							+ "JOIN\r\n"
							+ "(SELECT ACCT_ID,FIN_YEAR,ENG,CUST,DMD,ED,NVL(TOD,0) + NVL(TOD_OP_HGD,0)+NVL(TOD_OP_LGD,0)+NVL(TOD_P_HGD,0)+NVL(TOD_P_LGD,0) TOD FROM \r\n"
							+ "(SELECT ACCT_ID,CHARGE_TYPE,FIN_YEAR,CHARGE_AMT FROM ACD_BILL_CALC_DTLS_revised WHERE FIN_YEAR='"
							+ year + "' AND LEVI_MTH=? )\r\n" + "PIVOT (SUM(CHARGE_AMT)\r\n"
							+ "    FOR CHARGE_TYPE IN ('ENG' ENG,'CUST' CUST,'DMD' DMD,'ED' ED,'TOD' TOD,'TOD-OP-HGD' TOD_OP_HGD,'TOD-OP-LGD' TOD_OP_LGD,'TOD-P-HGD' TOD_P_HGD,'TOD-P-LGD' TOD_P_LGD)))B\r\n"
							+ "ON A.CTACCT_ID=B.ACCT_ID\r\n" + "ORDER BY CTUSCNO";
					log.info(sql);
					return jdbcTemplate.queryForList(sql, new Object[] { year, year, monthyear, monthyear });
				} catch (DataAccessException e) {
					log.error(e.getMessage());
					e.printStackTrace();
					return Collections.emptyList();
				}

			}

		}

	}

	public List<Map<String, Object>> getMasterReport(HttpServletRequest request) {
		try {
			String circle = request.getParameter("circle");
			String mcode = request.getParameter("mcode");
			String fmonthYear = request.getParameter("fmonth") + "-" + request.getParameter("fyear");
			String tmonthYear = request.getParameter("tmonth") + "-" + request.getParameter("tyear");
			if (circle.equals("ALL")) {
				if (mcode.equalsIgnoreCase("ALL")) {
					String mastersql = "SELECT  USCNO,CHG_TYPE_CD,OLD_VAL,NEW_VAL,TO_CHAR(EFF_DT, 'DD-MM-YYYY') EFF_DT,DEPT_ORDER_NO,TO_CHAR(DEPT_ORDER_DT,'YYYY-MM-DD')DEPT_ORDER_DT,CHANGED_BY,TO_CHAR(CRE_DTTM, 'DD-MM-YYYY') CRE_DTTM FROM CHANGE_HISTORY WHERE CRE_DTTM BETWEEN to_date(?,'MON-YYYY') and  to_date(?,'MON-YYYY')  ORDER BY TO_NUMBER(substr(USCNO,4,6))";
					log.info(mastersql);
					return jdbcTemplate.queryForList(mastersql, new Object[] { fmonthYear,tmonthYear });
				} else {
					String mastersql = "SELECT  USCNO,CHG_TYPE_CD,OLD_VAL,NEW_VAL,TO_CHAR(EFF_DT, 'DD-MM-YYYY') EFF_DT,DEPT_ORDER_NO,TO_CHAR(DEPT_ORDER_DT,'YYYY-MM-DD')DEPT_ORDER_DT,CHANGED_BY,TO_CHAR(CRE_DTTM, 'DD-MM-YYYY') CRE_DTTM FROM CHANGE_HISTORY WHERE CRE_DTTM BETWEEN to_date(?,'MON-YYYY') and  to_date(?,'MON-YYYY') AND CHG_TYPE_CD=? ORDER BY TO_NUMBER(substr(USCNO,4,6))";
					log.info(mastersql);
					return jdbcTemplate.queryForList(mastersql, new Object[] { fmonthYear,tmonthYear , mcode });
				}

			} else {
				if (mcode.equalsIgnoreCase("ALL")) {
					String mastersql = "SELECT  USCNO,CHG_TYPE_CD,OLD_VAL,NEW_VAL,TO_CHAR(EFF_DT, 'DD-MM-YYYY') EFF_DT,DEPT_ORDER_NO,TO_CHAR(DEPT_ORDER_DT,'YYYY-MM-DD')DEPT_ORDER_DT,CHANGED_BY,TO_CHAR(CRE_DTTM, 'DD-MM-YYYY') CRE_DTTM FROM CHANGE_HISTORY WHERE CRE_DTTM BETWEEN to_date(?,'MON-YYYY') and  to_date(?,'MON-YYYY') and substr(USCNO,1,3) = ?  ORDER BY TO_NUMBER(substr(USCNO,4,6))";
					log.info(mastersql);
					return jdbcTemplate.queryForList(mastersql, new Object[] { fmonthYear,tmonthYear , circle });
				} else {
					String mastersql = "SELECT  USCNO,CHG_TYPE_CD,OLD_VAL,NEW_VAL,TO_CHAR(EFF_DT, 'DD-MM-YYYY') EFF_DT,DEPT_ORDER_NO,TO_CHAR(DEPT_ORDER_DT,'YYYY-MM-DD')DEPT_ORDER_DT,CHANGED_BY,TO_CHAR(CRE_DTTM, 'DD-MM-YYYY') CRE_DTTM FROM CHANGE_HISTORY WHERE CRE_DTTM BETWEEN to_date(?,'MON-YYYY') and  to_date(?,'MON-YYYY') and substr(USCNO,1,3) = ? AND CHG_TYPE_CD=? ORDER BY TO_NUMBER(substr(USCNO,4,6))";
					log.info(mastersql);
					return jdbcTemplate.queryForList(mastersql, new Object[] { fmonthYear,tmonthYear , circle, mcode });
				}
			}
		} catch (DataAccessException e) {
			log.error(e.getMessage());
			e.printStackTrace();
			return Collections.emptyList();
		}

	}
	
	public List<Map<String, Object>> getSerMasterReport(HttpServletRequest request) {
		try {
			String serviceno = request.getParameter("scno");
			String fmonthYear = request.getParameter("fmonth") + "-" + request.getParameter("fyear");
			String tmonthYear = request.getParameter("tmonth") + "-" + request.getParameter("tyear");

			String mastersql = "SELECT  USCNO,CHG_TYPE_CD,OLD_VAL,NEW_VAL,TO_CHAR(EFF_DT, 'DD-MM-YYYY') EFF_DT,DEPT_ORDER_NO,TO_CHAR(DEPT_ORDER_DT,'YYYY-MM-DD')DEPT_ORDER_DT,CHANGED_BY,TO_CHAR(CRE_DTTM, 'DD-MM-YYYY') CRE_DTTM FROM CHANGE_HISTORY WHERE CRE_DTTM BETWEEN to_date(?,'MON-YYYY') and  to_date(?,'MON-YYYY') and USCNO=?  ORDER BY TO_NUMBER(substr(USCNO,4,6))";
			log.info(mastersql);
			return jdbcTemplate.queryForList(mastersql, new Object[] { fmonthYear, tmonthYear,serviceno });
				
			
		} catch (DataAccessException e) {
			log.error(e.getMessage());
			e.printStackTrace();
			return Collections.emptyList();
		}

	}

	public List<Map<String, Object>> getCircleWiseDemandReport(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		String monthYear = request.getParameter("month") + "-" + request.getParameter("year");
		if (circle.equals("ALL")) {
			try {
				String circleWisedemandsql = "SELECT NVL(DIVNAME,'---')DIVNAME,BTSCNO,BTCURDEM,BTCURDEM+BTCOURT_LPC  WC_BTCURDEM FROM BILL,CONS,SPDCLMASTER \r\n"
						+ "WHERE BTSCNO=CTUSCNO \r\n" + "AND SECCD = SUBSTR(CONS.CTSECCD,-5)\r\n"
						+ "AND TO_CHAR(BTBLDT,'MON-YYYY')=?  \r\n" + "UNION ALL\r\n"
						+ "SELECT NVL(DIVNAME,'---')DIVNAME,BTSCNO,BTCURDEM,BTCURDEM+BTCOURT_LPC WC_BTCURDEM FROM BILL_HIST,CONS,SPDCLMASTER \r\n"
						+ "WHERE BTSCNO=CTUSCNO \r\n" + "AND SECCD = SUBSTR(CONS.CTSECCD,-5)\r\n"
						+ "AND TO_CHAR(BTBLDT,'MON-YYYY')=?  \r\n" + "ORDER BY BTSCNO";
				log.info(circleWisedemandsql);
				return jdbcTemplate.queryForList(circleWisedemandsql, new Object[] { monthYear, monthYear });
			} catch (DataAccessException e) {
				log.error(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}
		} else {
			try {
				String circleWisedemandsql = "SELECT NVL(DIVNAME,'---')DIVNAME,BTSCNO,BTCURDEM,BTCURDEM+BTCOURT_LPC  WC_BTCURDEM FROM BILL,CONS,SPDCLMASTER \r\n"
						+ "WHERE BTSCNO=CTUSCNO \r\n" + "AND SECCD = SUBSTR(CONS.CTSECCD,-5)\r\n"
						+ "AND TO_CHAR(BTBLDT,'MON-YYYY')=? AND SUBSTR(BTSCNO,1,3)=? \r\n" + "UNION ALL\r\n"
						+ "SELECT NVL(DIVNAME,'---')DIVNAME,BTSCNO,BTCURDEM,BTCURDEM+BTCOURT_LPC WC_BTCURDEM FROM BILL_HIST,CONS,SPDCLMASTER \r\n"
						+ "WHERE BTSCNO=CTUSCNO \r\n" + "AND SECCD = SUBSTR(CONS.CTSECCD,-5)\r\n"
						+ "AND TO_CHAR(BTBLDT,'MON-YYYY')=? AND SUBSTR(BTSCNO,1,3)=? \r\n" + "ORDER BY BTSCNO";
				log.info(circleWisedemandsql);
				return jdbcTemplate.queryForList(circleWisedemandsql,
						new Object[] { monthYear, circle, monthYear, circle });
			} catch (DataAccessException e) {
				log.error(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}
		}
	}

	public List<Map<String, Object>> getServeTypeCollection(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		String monthYear = request.getParameter("month") + "-" + request.getParameter("year");
		if (circle.equals("ALL")) {
			try {
				String circleWisedemandsql = "SELECT CTSERVTYPE,NVL(STDESC,'Others') STDESC,COUNT(*) NOS,SUM(NVL(PAY,0)) COLLECTION FROM CONS,SPDCLMASTER,servtype,\r\n"
						+ "(SELECT USCNO SCNO,SUM(NVL(PCMD,0)) PAY FROM (select USCNO,PCMD,PAY_DATE,pay_sta_flg from pay_ht union all select USCNO,PCMD,PAY_DATE,pay_sta_flg  from pay_ht_hist) \r\n"
						+ "WHERE TO_DATE(PAY_DATE) between TRUNC(to_date(?,'MON-YYYY'))\r\n"
						+ "and LAST_DAY(TRUNC(to_date(?,'MON-YYYY')) ) AND pay_sta_flg='C' AND SUBSTR(USCNO,1,3) IN('GNT','VJA','ONG','CRD') GROUP BY USCNO)\r\n"
						+ "WHERE CTUSCNO=SCNO  AND SUBSTR(CTSECCD,-5)=SECCD  and CTSERVTYPE = STCODE(+) \r\n"
						+ "GROUP BY CTSERVTYPE ,STDESC order by CASE WHEN  STDESC ='Others' THEN 'zzzzzz' ELSE STDESC END";
				log.info(circleWisedemandsql);
				return jdbcTemplate.queryForList(circleWisedemandsql, new Object[] { monthYear, monthYear });
			} catch (DataAccessException e) {
				log.error(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}
		} else {
			try {
				String circleWisedemandsql = "SELECT CTSERVTYPE,NVL(STDESC,'Others') STDESC,COUNT(*) NOS,SUM(NVL(PAY,0)) COLLECTION FROM CONS,SPDCLMASTER,servtype,\r\n"
						+ "(SELECT USCNO SCNO,SUM(NVL(PCMD,0)) PAY FROM (select USCNO,PCMD,PAY_DATE,pay_sta_flg from pay_ht union all select USCNO,PCMD,PAY_DATE,pay_sta_flg  from pay_ht_hist) \r\n"
						+ "WHERE TO_DATE(PAY_DATE) between TRUNC(to_date(?,'MON-YYYY'))\r\n"
						+ "and LAST_DAY(TRUNC(to_date(?,'MON-YYYY')) ) AND pay_sta_flg='C' AND SUBSTR(USCNO,1,3) IN('GNT','VJA','ONG','CRD') GROUP BY USCNO)\r\n"
						+ "WHERE CTUSCNO=SCNO  AND SUBSTR(CTSECCD,-5)=SECCD  and CTSERVTYPE = STCODE(+) AND SUBSTR(CTUSCNO,1,3)=?\r\n"
						+ "GROUP BY CTSERVTYPE ,STDESC order by CASE WHEN  STDESC ='Others' THEN 'zzzzzz' ELSE STDESC END";
				log.info(circleWisedemandsql);
				return jdbcTemplate.queryForList(circleWisedemandsql, new Object[] { monthYear, monthYear, circle });
			} catch (DataAccessException e) {
				log.error(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}
		}
	}

	public List<Map<String, Object>> getSCServices(String servtype, String monyear, String circle, String flag) {

		String con = circle.equals("ALL") ? " " : " AND SUBSTR(SCNO,1,3)='" + circle + "'";
		if (!flag.equals("TOT")) {
			try {
				String sql = "select SCNO,CTNAME,CTCAT||CTSUBCAT CAT,PAY  from cons, \r\n"
						+ "(SELECT USCNO SCNO,SUM(NVL(PCMD,0)) PAY FROM (select USCNO,PCMD,PAY_DATE,pay_sta_flg from pay_ht union all select USCNO,PCMD,PAY_DATE,pay_sta_flg  from pay_ht_hist) \r\n"
						+ "WHERE TO_DATE(PAY_DATE) between TRUNC(to_date(?,'MON-YYYY'))\r\n"
						+ "and LAST_DAY(TRUNC(to_date(?,'MON-YYYY')) ) AND pay_sta_flg='C' AND SUBSTR(USCNO,1,3) IN('GNT','VJA','ONG','CRD') GROUP BY USCNO)\r\n"
						+ "WHERE CTUSCNO=SCNO AND CTSERVTYPE=? " + con;
				log.info(sql);
				return jdbcTemplate.queryForList(sql, new Object[] { monyear, monyear, servtype });
			} catch (DataAccessException e) {
				log.error(e.getMessage());
				log.error(e);
				return Collections.emptyList();
			}
		} else {

			try {
				String sql = "select SCNO,CTNAME,CTCAT||CTSUBCAT CAT,PAY  from cons, \r\n"
						+ "(SELECT USCNO SCNO,SUM(NVL(PCMD,0)) PAY FROM (select USCNO,PCMD,PAY_DATE,pay_sta_flg from pay_ht union all select USCNO,PCMD,PAY_DATE,pay_sta_flg  from pay_ht_hist) \r\n"
						+ "WHERE TO_DATE(PAY_DATE) between TRUNC(to_date(?,'MON-YYYY'))\r\n"
						+ "and LAST_DAY(TRUNC(to_date(?,'MON-YYYY')) ) AND pay_sta_flg='C' AND SUBSTR(USCNO,1,3) IN('GNT','VJA','ONG','CRD') GROUP BY USCNO)\r\n"
						+ "WHERE CTUSCNO=SCNO " + con;
				log.info(sql);
				return jdbcTemplate.queryForList(sql, new Object[] { monyear, monyear });
			} catch (DataAccessException e) {
				log.error(e.getMessage());
				log.error(e);
				return Collections.emptyList();
			}
		}
	}

	public List<Map<String, Object>> getHCServices(String servtype, String monyear, String circle, String flag) {

		String con = circle.equals("ALL") ? " " : " AND SUBSTR(SCNO,1,3)='" + circle + "'";
		if (!flag.equals("TOT")) {
			try {

				String sql = "SELECT SCNO,CTNAME,CTCAT||CTSUBCAT CAT,PAY FROM CONS,SPDCLMASTER,\r\n"
						+ "(SELECT USCNO SCNO,SUM(NVL(PCMD,0)) PAY FROM (select USCNO,PCMD,PAY_DATE,pay_sta_flg from pay_ht union all select USCNO,PCMD,PAY_DATE,pay_sta_flg  from pay_ht_hist) \r\n"
						+ "WHERE TO_DATE(PAY_DATE) between TRUNC(to_date(?,'MON-YYYY'))\r\n"
						+ "and LAST_DAY(TRUNC(to_date(?,'MON-YYYY')) ) AND pay_sta_flg='C' AND SUBSTR(USCNO,1,3) IN('GNT','VJA','ONG','CRD') GROUP BY USCNO),\r\n"
						+ "(SELECT GMDEPTCODE, GMDEPTNAME,GMTYPECODE FROM deptmast group by GMDEPTCODE, GMDEPTNAME,GMTYPECODE\r\n"
						+ "union all\r\n"
						+ "select 'OTH' GMDEPTCODE ,'Others' GMDEPTNAME,'OTH' GMTYPECODE from dual) where NVL(CTHODDEP,'OTH')=GMDEPTCODE\r\n"
						+ "and NVL(CTHODTYPE,'OTH')=GMTYPECODE\r\n"
						+ "and CTUSCNO=SCNO  AND SUBSTR(CTSECCD,-5)=SECCD AND NVL(CTGOVT_PVT,'N')='Y' AND NVL(CTHODDEP,'OTH')=? "
						+ con;
				/*
				 * String sql =
				 * "SELECT SCNO,CTNAME,CTCAT||CTSUBCAT CAT,PAY FROM CONS,SPDCLMASTER,\r\n" +
				 * "(SELECT USCNO SCNO,SUM(NVL(PCMD,0)) PAY FROM (select USCNO,PCMD,PAY_DATE,pay_sta_flg from pay_ht union all select USCNO,PCMD,PAY_DATE,pay_sta_flg  from pay_ht_hist) \r\n"
				 * + "WHERE TO_DATE(PAY_DATE) between TRUNC(to_date(?,'MON-YYYY'))\r\n" +
				 * "and LAST_DAY(TRUNC(to_date(?,'MON-YYYY')) ) AND pay_sta_flg='C' AND SUBSTR(USCNO,1,3) IN('GNT','VJA','ONG','CRD') GROUP BY USCNO)\r\n"
				 * +
				 * "WHERE CTUSCNO=SCNO  AND SUBSTR(CTSECCD,-5)=SECCD AND NVL(CTGOVT_PVT,'N')='Y' AND NVL(CTHODDEP,'OTH')=?"
				 * + con;
				 */
				log.info(sql);
				return jdbcTemplate.queryForList(sql, new Object[] { monyear, monyear, servtype });
			} catch (DataAccessException e) {
				log.error(e.getMessage());
				log.error(e);
				return Collections.emptyList();
			}
		} else {

			try {
				String sql = "SELECT SCNO,CTNAME,CTCAT||CTSUBCAT CAT,PAY FROM CONS,SPDCLMASTER,\r\n"
						+ "(SELECT USCNO SCNO,SUM(NVL(PCMD,0)) PAY FROM (select USCNO,PCMD,PAY_DATE,pay_sta_flg from pay_ht union all select USCNO,PCMD,PAY_DATE,pay_sta_flg  from pay_ht_hist) \r\n"
						+ "WHERE TO_DATE(PAY_DATE) between TRUNC(to_date(?,'MON-YYYY'))\r\n"
						+ "and LAST_DAY(TRUNC(to_date(?,'MON-YYYY')) ) AND pay_sta_flg='C' AND SUBSTR(USCNO,1,3) IN('GNT','VJA','ONG','CRD') GROUP BY USCNO),\r\n"
						+ "(SELECT GMDEPTCODE, GMDEPTNAME,GMTYPECODE FROM deptmast group by GMDEPTCODE, GMDEPTNAME,GMTYPECODE\r\n"
						+ "union all\r\n"
						+ "select 'OTH' GMDEPTCODE ,'Others' GMDEPTNAME,'OTH' GMTYPECODE from dual) where NVL(CTHODDEP,'OTH')=GMDEPTCODE\r\n"
						+ "and NVL(CTHODTYPE,'OTH')=GMTYPECODE\r\n"
						+ "and CTUSCNO=SCNO  AND SUBSTR(CTSECCD,-5)=SECCD AND NVL(CTGOVT_PVT,'N')='Y' " + con;
				/*
				 * String sql =
				 * "SELECT SCNO,CTNAME,CTCAT||CTSUBCAT CAT,PAY FROM CONS,SPDCLMASTER,\r\n" +
				 * "(SELECT USCNO SCNO,SUM(NVL(PCMD,0)) PAY FROM (select USCNO,PCMD,PAY_DATE,pay_sta_flg from pay_ht union all select USCNO,PCMD,PAY_DATE,pay_sta_flg  from pay_ht_hist) \r\n"
				 * + "WHERE TO_DATE(PAY_DATE) between TRUNC(to_date(?,'MON-YYYY'))\r\n" +
				 * "and LAST_DAY(TRUNC(to_date(?,'MON-YYYY')) ) AND pay_sta_flg='C' AND SUBSTR(USCNO,1,3) IN('GNT','VJA','ONG','CRD') GROUP BY USCNO)\r\n"
				 * +
				 * "WHERE CTUSCNO=SCNO  AND SUBSTR(CTSECCD,-5)=SECCD AND NVL(CTGOVT_PVT,'N')='Y' "
				 * + con;
				 */
				log.info(sql);
				return jdbcTemplate.queryForList(sql, new Object[] { monyear, monyear });
			} catch (DataAccessException e) {
				log.error(e.getMessage());
				log.error(e);
				return Collections.emptyList();
			}
		}
	}

	public List<Map<String, Object>> getHODTypeCollection(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		String monthYear = request.getParameter("month") + "-" + request.getParameter("year");
		if (circle.equals("ALL")) {
			try {
				String circleWisedemandsql = "select GMDEPTCODE,GMDEPTNAME TYPE ,NOS, COLLECTION from (SELECT \r\n"
						+ " NVL(CTHODDEP,'OTH') TYPE,NVL(CTHODTYPE,'OTH') CTHODTYPE,COUNT(*) NOS,\r\n"
						+ "SUM(NVL(PAY,0)) COLLECTION FROM CONS,SPDCLMASTER,\r\n"
						+ "(SELECT USCNO SCNO,SUM(NVL(PCMD,0)) PAY FROM (select USCNO,PCMD,PAY_DATE,pay_sta_flg from pay_ht union all select USCNO,PCMD,PAY_DATE,pay_sta_flg  from pay_ht_hist) \r\n"
						+ "WHERE TO_DATE(PAY_DATE) between TRUNC(to_date(?,'MON-YYYY'))\r\n"
						+ "and LAST_DAY(TRUNC(to_date(?,'MON-YYYY')) ) AND pay_sta_flg='C' AND SUBSTR(USCNO,1,3) IN('GNT','VJA','ONG','CRD') GROUP BY USCNO)\r\n"
						+ "WHERE CTUSCNO=SCNO  AND SUBSTR(CTSECCD,-5)=SECCD AND NVL(CTGOVT_PVT,'N')='Y'\r\n"
						+ "GROUP BY \r\n" + "CTHODTYPE,CTHODDEP\r\n"
						+ "order by CASE WHEN  CTHODDEP ='Others' THEN 'zzzzzz' ELSE CTHODDEP END),\r\n"
						+ "(SELECT GMDEPTCODE, GMDEPTNAME,GMTYPECODE FROM deptmast group by GMDEPTCODE, GMDEPTNAME,GMTYPECODE\r\n"
						+ "union all\r\n"
						+ "select 'OTH' GMDEPTCODE ,'Others' GMDEPTNAME,'OTH' GMTYPECODE from dual) where GMDEPTCODE=TYPE\r\n"
						+ "and CTHODTYPE=GMTYPECODE";
				log.info(circleWisedemandsql);
				return jdbcTemplate.queryForList(circleWisedemandsql, new Object[] { monthYear, monthYear });
			} catch (DataAccessException e) {
				log.error(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}
		} else {
			try {

				String circleWisedemandsql = "select GMDEPTCODE,GMDEPTNAME TYPE ,NOS, COLLECTION from (SELECT \r\n"
						+ " NVL(CTHODDEP,'OTH') TYPE,NVL(CTHODTYPE,'OTH') CTHODTYPE,COUNT(*) NOS,\r\n"
						+ "SUM(NVL(PAY,0)) COLLECTION FROM CONS,SPDCLMASTER,\r\n"
						+ "(SELECT USCNO SCNO,SUM(NVL(PCMD,0)) PAY FROM (select USCNO,PCMD,PAY_DATE,pay_sta_flg from pay_ht union all select USCNO,PCMD,PAY_DATE,pay_sta_flg  from pay_ht_hist) \r\n"
						+ "WHERE TO_DATE(PAY_DATE) between TRUNC(to_date(?,'MON-YYYY'))\r\n"
						+ "and LAST_DAY(TRUNC(to_date(?,'MON-YYYY')) ) AND pay_sta_flg='C' AND SUBSTR(USCNO,1,3) IN('GNT','VJA','ONG','CRD') GROUP BY USCNO)\r\n"
						+ "WHERE CTUSCNO=SCNO  AND SUBSTR(CTSECCD,-5)=SECCD AND NVL(CTGOVT_PVT,'N')='Y' AND SUBSTR(CTUSCNO,1,3)=? \r\n"
						+ "GROUP BY \r\n" + "CTHODTYPE,CTHODDEP\r\n"
						+ "order by CASE WHEN  CTHODDEP ='Others' THEN 'zzzzzz' ELSE CTHODDEP END),\r\n"
						+ "(SELECT GMDEPTCODE, GMDEPTNAME,GMTYPECODE FROM deptmast group by GMDEPTCODE, GMDEPTNAME,GMTYPECODE\r\n"
						+ "union all\r\n"
						+ "select 'OTH' GMDEPTCODE ,'Others' GMDEPTNAME,'OTH' GMTYPECODE from dual) where GMDEPTCODE=TYPE\r\n"
						+ "and CTHODTYPE=GMTYPECODE";
				log.info(circleWisedemandsql);
				return jdbcTemplate.queryForList(circleWisedemandsql, new Object[] { monthYear, monthYear, circle });
			} catch (DataAccessException e) {
				log.error(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}
		}
	}

	public List<Map<String, Object>> getRJServiceWiseReport(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		String monthYear = request.getParameter("month") + "-" + request.getParameter("year");
		if (circle.equals("ALL")) {
			try {
				String circleWisedemandsql = "select SUBSTR(uscno,1,3) CIR,USCNO SCNO,SAPRJ RJ_TYPE,RJNO RJ_NO,\r\n"
						+ "SUM(CASE WHEN RJTYPE='CR' THEN (NVL(TOTRJ_AMT,0)) ELSE 0 END) CR_AMT,\r\n"
						+ "SUM(CASE WHEN RJTYPE='DR'THEN (NVL(TOTRJ_AMT,0)) ELSE 0 END) DR_AMT,\r\n"
						+ "SUM((NVL(TOTRJ_AMT,0)) ) TOTAL_AMT\r\n"
						+ "from (select SAPRJ,USCNO,RJTYPE,TOTRJ_AMT,rjdt,STATUS,RJNO   from JOURNAL WHERE to_char(TRUNC(rjdt,'MM'),'MON-YYYY')=?  \r\n"
						+ "union all select SAPRJ,USCNO,RJTYPE,TOTRJ_AMT,rjdt,STATUS,RJNO   from JOURNAL_HIST WHERE to_char(TRUNC(rjdt,'MM'),'MON-YYYY')=? )\r\n"
						+ "WHERE  SUBSTR(USCNO,1,3) IN('GNT','VJA','ONG','CRD') AND TRIM(STATUS) NOT IN ('X','E') GROUP BY SUBSTR(USCNO,1,3),USCNO,SAPRJ,RJNO ORDER BY 1,2,3";
				log.info(circleWisedemandsql);
				return jdbcTemplate.queryForList(circleWisedemandsql, new Object[] { monthYear, monthYear });
			} catch (DataAccessException e) {
				log.error(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}
		} else {
			try {
				String circleWisedemandsql = "select SUBSTR(uscno,1,3) CIR,USCNO SCNO,SAPRJ RJ_TYPE,RJNO RJ_NO,\r\n"
						+ "SUM(CASE WHEN RJTYPE='CR' THEN (NVL(TOTRJ_AMT,0)) ELSE 0 END) CR_AMT,\r\n"
						+ "SUM(CASE WHEN RJTYPE='DR'THEN (NVL(TOTRJ_AMT,0)) ELSE 0 END) DR_AMT,\r\n"
						+ "SUM((NVL(TOTRJ_AMT,0)) ) TOTAL_AMT\r\n"
						+ "from (select SAPRJ,USCNO,RJTYPE,TOTRJ_AMT,rjdt,STATUS,RJNO   from JOURNAL WHERE to_char(TRUNC(rjdt,'MM'),'MON-YYYY')=?   "
						+ " union all select SAPRJ,USCNO,RJTYPE,TOTRJ_AMT,rjdt,STATUS,RJNO   from JOURNAL_HIST WHERE to_char(TRUNC(rjdt,'MM'),'MON-YYYY')=? )\r\n"
						+ "WHERE SUBSTR(USCNO,1,3)=? AND TRIM(STATUS) NOT IN ('X','E') GROUP BY SUBSTR(USCNO,1,3),USCNO,SAPRJ,RJNO ORDER BY 1,2,3";

				log.info(circleWisedemandsql);
				return jdbcTemplate.queryForList(circleWisedemandsql, new Object[] { monthYear, monthYear, circle });
			} catch (DataAccessException e) {
				log.error(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}
		}
	}

	public List<Map<String, Object>> getRJTypeWiseReport(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		String monthYear = request.getParameter("month") + "-" + request.getParameter("year");
		if (circle.equals("ALL")) {
			try {
				String circleWisedemandsql = "select 'ALL' CIR,SAPRJ RJ_TYPE,\r\n"
						+ "NVL(SUM(CASE WHEN RJTYPE='CR' THEN (NVL(TOTRJ_AMT,0)) END),0) CR_AMT,\r\n"
						+ "NVL(SUM(CASE WHEN RJTYPE='DR'THEN (NVL(TOTRJ_AMT,0)) END),0) DR_AMT,\r\n"
						+ "SUM((NVL(TOTRJ_AMT,0)) ) TOTAL_AMT\r\n"
						+ "from (select SAPRJ,USCNO,RJTYPE,TOTRJ_AMT,rjdt,STATUS   from JOURNAL WHERE to_char(TRUNC(rjdt,'MM'),'MON-YYYY')=?  "
						+ "union all select SAPRJ,USCNO,RJTYPE,TOTRJ_AMT,rjdt,STATUS   from JOURNAL_HIST WHERE to_char(TRUNC(rjdt,'MM'),'MON-YYYY')=? ) \r\n"
						+ "WHERE  SUBSTR(USCNO,1,3) IN('GNT','VJA','ONG','CRD') AND TRIM(STATUS) NOT IN ('X','E') GROUP BY SAPRJ ORDER BY 2,1\r\n"
						+ "";
				log.info(circleWisedemandsql);
				return jdbcTemplate.queryForList(circleWisedemandsql, new Object[] { monthYear, monthYear });
			} catch (DataAccessException e) {
				log.error(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}
		} else {
			try {
				String circleWisedemandsql = "select SUBSTR(uscno,1,3) CIR,SAPRJ RJ_TYPE,\r\n"
						+ "NVL(SUM(CASE WHEN RJTYPE='CR' THEN (NVL(TOTRJ_AMT,0)) END),0) CR_AMT,\r\n"
						+ "NVL(SUM(CASE WHEN RJTYPE='DR'THEN (NVL(TOTRJ_AMT,0)) END),0) DR_AMT,\r\n"
						+ "SUM((NVL(TOTRJ_AMT,0)) ) TOTAL_AMT\r\n"
						+ "from (select SAPRJ,USCNO,RJTYPE,TOTRJ_AMT,rjdt,STATUS   from JOURNAL WHERE to_char(TRUNC(rjdt,'MM'),'MON-YYYY')=? "
						+ " union all select SAPRJ,USCNO,RJTYPE,TOTRJ_AMT,rjdt,STATUS   from JOURNAL_HIST WHERE to_char(TRUNC(rjdt,'MM'),'MON-YYYY')=? )\r\n"
						+ "WHERE SUBSTR(USCNO,1,3)=? AND TRIM(STATUS) NOT IN ('X','E') GROUP BY SUBSTR(USCNO,1,3),SAPRJ ORDER BY 2,1\r\n"
						+ "";

				log.info(circleWisedemandsql);
				return jdbcTemplate.queryForList(circleWisedemandsql, new Object[] { monthYear, monthYear, circle });
			} catch (DataAccessException e) {
				log.error(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}
		}
	}

	public List<Map<String, Object>> getRJHODWiseReport(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		String monthYear = request.getParameter("month") + "-" + request.getParameter("year");
		if (circle.equals("ALL")) {
			try {
				String circleWisedemandsql = "select 'ALL' CIR, GMDEPTCODE,GMDEPTNAME TYPE ,NOS, CR_AMT,DR_AMT ,TOTAL_AMT from (SELECT \r\n"
						+ " NVL(CTHODDEP,'OTH') TYPE,NVL(CTHODTYPE,'OTH') CTHODTYPE,COUNT(*) NOS,\r\n"
						+ "SUM(CASE WHEN RJTYPE='CR' THEN (NVL(TOTRJ_AMT,0)) ELSE 0 END) CR_AMT,\r\n"
						+ "SUM(CASE WHEN RJTYPE='DR'THEN (NVL(TOTRJ_AMT,0)) ELSE 0 END) DR_AMT,\r\n"
						+ "SUM((NVL(TOTRJ_AMT,0)) ) TOTAL_AMT FROM CONS,SPDCLMASTER,\r\n"
						+ "(select SAPRJ,USCNO,RJTYPE,TOTRJ_AMT,rjdt,STATUS,RJNO   from JOURNAL    union all select SAPRJ,USCNO,RJTYPE,TOTRJ_AMT,rjdt,STATUS,RJNO   from JOURNAL_HIST \r\n"
						+ "WHERE TO_DATE(rjdt) between TRUNC(to_date(?,'MON-YYYY'))\r\n"
						+ "and LAST_DAY(TRUNC(to_date(?,'MON-YYYY')) ) AND TRIM(STATUS) NOT IN ('X','E') AND SUBSTR(USCNO,1,3) IN('GNT','VJA','ONG','CRD') )\r\n"
						+ "WHERE CTUSCNO=USCNO  AND SUBSTR(CTSECCD,-5)=SECCD AND NVL(CTGOVT_PVT,'N')='Y'\r\n"
						+ "GROUP BY \r\n" + "CTHODTYPE,CTHODDEP\r\n"
						+ "order by CASE WHEN  CTHODDEP ='Others' THEN 'zzzzzz' ELSE CTHODDEP END),\r\n"
						+ "(SELECT GMDEPTCODE, GMDEPTNAME,GMTYPECODE FROM deptmast group by GMDEPTCODE, GMDEPTNAME,GMTYPECODE\r\n"
						+ "union all\r\n"
						+ "select 'OTH' GMDEPTCODE ,'Others' GMDEPTNAME,'OTH' GMTYPECODE from dual) where GMDEPTCODE=TYPE\r\n"
						+ "and CTHODTYPE=GMTYPECODE";
				log.info(circleWisedemandsql);
				return jdbcTemplate.queryForList(circleWisedemandsql, new Object[] { monthYear, monthYear });
			} catch (DataAccessException e) {
				log.error(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}
		} else {
			try {

				String circleWisedemandsql = "select '" + circle
						+ "' CIR, GMDEPTCODE,GMDEPTNAME TYPE ,NOS, CR_AMT,DR_AMT ,TOTAL_AMT from (SELECT \r\n"
						+ " NVL(CTHODDEP,'OTH') TYPE,NVL(CTHODTYPE,'OTH') CTHODTYPE,COUNT(*) NOS,\r\n"
						+ "SUM(CASE WHEN RJTYPE='CR' THEN (NVL(TOTRJ_AMT,0)) ELSE 0 END) CR_AMT,\r\n"
						+ "SUM(CASE WHEN RJTYPE='DR'THEN (NVL(TOTRJ_AMT,0)) ELSE 0 END) DR_AMT,\r\n"
						+ "SUM((NVL(TOTRJ_AMT,0)) ) TOTAL_AMT FROM CONS,SPDCLMASTER,\r\n"
						+ "(select SAPRJ,USCNO,RJTYPE,TOTRJ_AMT,rjdt,STATUS,RJNO   from JOURNAL    union all select SAPRJ,USCNO,RJTYPE,TOTRJ_AMT,rjdt,STATUS,RJNO   from JOURNAL_HIST \r\n"
						+ "WHERE TO_DATE(rjdt) between TRUNC(to_date(?,'MON-YYYY'))\r\n"
						+ "and LAST_DAY(TRUNC(to_date(?,'MON-YYYY')) ) AND TRIM(STATUS) NOT IN ('X','E') AND SUBSTR(USCNO,1,3) IN(?) )\r\n"
						+ "WHERE CTUSCNO=USCNO  AND SUBSTR(CTSECCD,-5)=SECCD AND NVL(CTGOVT_PVT,'N')='Y'  \r\n"
						+ "GROUP BY \r\n" + "CTHODTYPE,CTHODDEP\r\n"
						+ "order by CASE WHEN  CTHODDEP ='Others' THEN 'zzzzzz' ELSE CTHODDEP END),\r\n"
						+ "(SELECT GMDEPTCODE, GMDEPTNAME,GMTYPECODE FROM deptmast group by GMDEPTCODE, GMDEPTNAME,GMTYPECODE\r\n"
						+ "union all\r\n"
						+ "select 'OTH' GMDEPTCODE ,'Others' GMDEPTNAME,'OTH' GMTYPECODE from dual) where GMDEPTCODE=TYPE\r\n"
						+ "and CTHODTYPE=GMTYPECODE";
				log.info(circleWisedemandsql);
				return jdbcTemplate.queryForList(circleWisedemandsql, new Object[] { monthYear, monthYear, circle });
			} catch (DataAccessException e) {
				log.error(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}
		}
	}

	public List<Map<String, Object>> getRJNoWiseReport(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		String monthYear = request.getParameter("month") + "-" + request.getParameter("year");
		if (circle.equals("ALL")) {
			try {
				String circleWisedemandsql = "select SUBSTR(uscno,1,3) CIR,RJNO RJ_NO,\r\n"
						+ "SUM(CASE WHEN RJTYPE='CR' THEN (NVL(TOTRJ_AMT,0)) ELSE 0 END) CR_AMT,\r\n"
						+ "SUM(CASE WHEN RJTYPE='DR'THEN (NVL(TOTRJ_AMT,0)) ELSE 0 END) DR_AMT,\r\n"
						+ "SUM((NVL(TOTRJ_AMT,0)) ) TOTAL_AMT\r\n"
						+ "from (select SAPRJ,USCNO,RJTYPE,TOTRJ_AMT,rjdt,STATUS,RJNO   from JOURNAL WHERE to_char(TRUNC(rjdt,'MM'),'MON-YYYY')=?    union all select SAPRJ,USCNO,RJTYPE,TOTRJ_AMT,rjdt,STATUS,RJNO   from JOURNAL_HIST WHERE to_char(TRUNC(rjdt,'MM'),'MON-YYYY')=?)\r\n"
						+ "WHERE  SUBSTR(USCNO,1,3) IN('GNT','VJA','ONG','CRD') AND TRIM(STATUS) NOT IN ('X','E') GROUP BY SUBSTR(USCNO,1,3),RJNO ORDER BY 1,2";
				log.info(circleWisedemandsql);
				return jdbcTemplate.queryForList(circleWisedemandsql, new Object[] { monthYear, monthYear });
			} catch (DataAccessException e) {
				log.error(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}
		} else {
			try {
				String circleWisedemandsql = "select SUBSTR(uscno,1,3) CIR,RJNO RJ_NO,\r\n"
						+ "SUM(CASE WHEN RJTYPE='CR' THEN (NVL(TOTRJ_AMT,0)) ELSE 0 END) CR_AMT,\r\n"
						+ "SUM(CASE WHEN RJTYPE='DR'THEN (NVL(TOTRJ_AMT,0)) ELSE 0 END) DR_AMT,\r\n"
						+ "SUM((NVL(TOTRJ_AMT,0)) ) TOTAL_AMT\r\n"
						+ "from (select SAPRJ,USCNO,RJTYPE,TOTRJ_AMT,rjdt,STATUS,RJNO   from JOURNAL WHERE to_char(TRUNC(rjdt,'MM'),'MON-YYYY')=?    union all select SAPRJ,USCNO,RJTYPE,TOTRJ_AMT,rjdt,STATUS,RJNO   from JOURNAL_HIST WHERE to_char(TRUNC(rjdt,'MM'),'MON-YYYY')=?)\r\n"
						+ "WHERE SUBSTR(USCNO,1,3)=? AND TRIM(STATUS) NOT IN ('X','E') GROUP BY SUBSTR(USCNO,1,3),RJNO ORDER BY 1,2";

				log.info(circleWisedemandsql);
				return jdbcTemplate.queryForList(circleWisedemandsql, new Object[] { monthYear, monthYear, circle });
			} catch (DataAccessException e) {
				log.error(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}
		}
	}

	public List<Map<String, Object>> getFeederData(HttpServletRequest request) {

		String monthYear = request.getParameter("month") + "-" + request.getParameter("year");
		System.out.println(monthYear);

		try {
			String circleWisedemandsql = "SELECT  UNIQUE \r\n"
					+ "cirname CIRCLE,divname DIVISION,subname SUB_DIVISION,secname SECTION,CTUSCNO,CTNAME NAME,CTMOBILE PHONE_NO,CTEMAILID EMAIL,CTCAT CAT,CTACTUAL_KV VOLTAGE,\r\n"
					+ "CTSUBCAT SUBCAT,'' SOCCAT,CTADD1||','||CTADD2||','||CTADD3 LOCATION,''  DTRNAME,FMFNAME FEEDER_NAME,fmsapfcode FEEDER_CD,\r\n"
					+ "CTCMD_HT CMD,BMD RMD,BPF PF,BDT MONTH,OPNRDG OP_RDG,CLRDG CL_RDG,KWH KWH_UNITS,KVAH KVAH_UNITS,NVL(OPUNITS,0) OFF_PEAK,NVL(PUNITS,0) PEAK,\r\n"
					+ "NVL(BEC,0) EC,NVL(BTOD,0) TOD_CHG,NVL(BCC,0) CC,NVL(BED,0) ED,NVL(BSUR,0) SURCHG,NVL(BEDI,0) IED,NVL(FIXED,0) DEMAND_CHGRS,NVL(DEM,0) TOTAL_BILL_AMOUNT,\r\n"
					+ "NVL(CB,0) AMT_DUE,DECODE(LPDT,NULL,'NA',LPDT) last_pay_dt,DECODE(LPAY,NULL,'NA',LPAY) last_pay_AMT,BTDUEDT,BTDISCDT\r\n"
					+ "from cons,SPDCLMASTER,feedermast,\r\n"
					+ "(SELECT USCNO,TRUNC(TO_DATE(MON_YEAR,'MON-YYYY'),'MM') LDT,(NVL(TOT_PAY,0)) PAY,NVL(CBTOT,0)+NVL(CB_CCLPC,0)+NVL(CB_OTH,0) CB,NVL(REC_KWH,0) KWH,NVL(MN_KVAH,0) KVAH \r\n"
					+ "FROM LEDGER_HT_HIST WHERE TRUNC(TO_DATE(MON_YEAR,'MON-YYYY'),'MM')=add_months(trunc(to_date(?,'MON-YYYY'),'mm'),-1) ) L,\r\n"
					+ "(SELECT MSCNO,NVL(MDOPNKVAH_HT,0) OPNRDG,NVL(MDCLKVAH_HT,0) CLRDG FROM MTRDAT WHERE MDMONTH=to_date(?,'MON-YYYY')) M,\r\n"
					+ "(SELECT BTSCNO,TO_CHAR(BTBLDT,'MON-YYYY') BDT,NVL(BTRKVA_HT,0) BMD,\r\n"
					+ "NVL(BTRKVAH_HT,0) RUNITS,(NVL(BTBKVAH,0)) BUNIT,NVL(BTTOD1,0)+NVL(BTTOD6,0) OPUNITS,NVL(BTTOD2,0)+NVL(BTTOD5,0) PUNITS,\r\n"
					+ "NVL(BTBLCOLNY_HT,0) COLUNITS,NVL(BTCURDEM,0)-(NVL(BTCUSTCHG,0)+NVL(BTADLCHG,0)+NVL(BTED,0)+NVL(BTED_INT,0)+NVL(BTDEMCHG_NOR,0)+NVL(BTDEMCHG_PEN,0)+NVL(BTOTHERCHG,0)) BEC,\r\n"
					+ "NVL(BTCUSTCHG,0) BCC,NVL(BTADLCHG,0) BSUR,NVL(BTDEMCHG_NOR,0)+NVL(BTDEMCHG_PEN,0) FIXED,\r\n"
					+ "NVL(BTED,0) BED,NVL(BTPF_HT,0)  BPF,NVL(BTED_INT,0) BEDI,NVL(BTVOLTSURCHG,0) VSUR,NVL(BTTOD1,0)+NVL(BTTOD6,0) OFPU,\r\n"
					+ "NVL(BTTOD2,0)+NVL(BTTOD5,0) PEU,NVL(BTBLCOLNY_HT,0) COL,NVL(BTTODCHG_HT,0) BTOD,NVL(BTLFINCENTIVE_HT,0) INC,\r\n"
					+ "case when nvl(CBTOT,0) >=0 then nvl(CBTOT,0) else 0 end + case when nvl(BTACD_DUE,0) >=0 then nvl(BTACD_DUE,0) else 0 end + nvl(BTCURDEM,0) DEM,to_char(BTDUEDT,'DD-MON-YYYY') BTDUEDT ,to_char(BTDISCDT,'DD-MON-YYYY')  BTDISCDT FROM BILL WHERE to_char(BTBLDT,'MON-YYYY')=?),\r\n"
					+ "(SELECT USCNO SCNO,PAY_DATE LPDT,SUM(NVL(PCMD,0)) lpay from pay_ht where (uscno,pay_date) IN(SELECT USCNO SCNO,max(pay_date) mdt FROM PAY_ht WHERE pay_sta_flg='C' GROUP BY USCNO) AND pay_sta_flg='C' GROUP BY USCNO,PAY_DATE\r\n"
					+ "  union\r\n"
					+ "SELECT USCNO SCNO,PAY_DATE LPDT,SUM(NVL(PCMD,0)) lpay FROM PAY_ht_hist WHERE (uscno,pay_date) IN(SELECT USCNO SCNO,max(pay_date) mdt FROM PAY_ht_HIST WHERE SUBSTR(USCNO,1,3) IN('GNT','VJA','ONG','CRD') AND pay_sta_flg='C' GROUP BY USCNO) AND pay_sta_flg='C' AND SUBSTR(USCNO,1,3) IN('GNT','VJA','ONG','CRD') GROUP BY USCNO,PAY_DATE)\r\n"
					+ "WHERE substr(ctseccd,-5)=seccd AND CTUSCNO=BTSCNO AND CTUSCNO=USCNO(+) AND CTUSCNO=MSCNO(+)  AND ctuscno=scno(+) and \r\n"
					+ "ctfeeder_code=fmsapfcode AND CTFEEDER_CODE IN('301522240305','301422340202','309141440201','309141440202','302112440101','302112440102',\r\n"
					+ "'309141440206','302112340601','302112340101','302112240103','309141440102') order by fmsapfcode";
			log.info(circleWisedemandsql);
			return jdbcTemplate.queryForList(circleWisedemandsql, new Object[] { monthYear, monthYear, monthYear });
		} catch (DataAccessException e) {
			log.error(e.getMessage());
			e.printStackTrace();
			return Collections.emptyList();
		}

	}

	public List<Map<String, Object>> getICEData(HttpServletRequest request) {

		String monthYear = request.getParameter("month") + "-" + request.getParameter("year");
		System.out.println(monthYear);

		try {
			String circleWisedemandsql = "SELECT  UNIQUE \r\n"
					+ "cirname CIRCLE,divname DIVISION,subname SUB_DIVISION,secname SECTION,stdesc TYPE,CTUSCNO,CTNAME NAME,CTMOBILE PHONE_NO,CTEMAILID EMAIL,CTCAT CAT,\r\n"
					+ "CTACTUAL_KV VOLTAGE,CTSUBCAT SUBCAT,'' SOCCAT,CTADD1||','||CTADD2||','||CTADD3 LOCATION,''  DTRNAME,FMFNAME FEEDER_NAME,fmsapfcode FEEDER_CD,\r\n"
					+ "CTCMD_HT CMD,BMD RMD,BPF PF,BDT MONTH,OPNRDG OP_RDG,CLRDG CL_RDG,KWH KWH_UNITS,KVAH KVAH_UNITS,NVL(OPUNITS,0) OFF_PEAK,NVL(PUNITS,0) PEAK,NVL(BEC,0) EC,\r\n"
					+ "NVL(BTOD,0) TOD_CHG,NVL(BCC,0) CC,NVL(BED,0) ED,NVL(BSUR,0) SURCHG,NVL(BEDI,0) IED,NVL(FIXED,0) DEMAND_CHGRS,NVL(DEM,0) TOTAL_BILL_AMOUNT,\r\n"
					+ "NVL(CB,0) AMT_DUE,DECODE(LPDT,NULL,'NA',LPDT) last_pay_dt,DECODE(LPAY,NULL,'NA',LPAY) last_pay_AMT,BTDUEDT,BTDISCDT\r\n"
					+ "from cons,SPDCLMASTER,feedermast,servtype,\r\n"
					+ "(SELECT USCNO,TRUNC(TO_DATE(MON_YEAR,'MON-YYYY'),'MM') LDT,(NVL(TOT_PAY,0)) PAY,NVL(CBTOT,0)+NVL(CB_CCLPC,0)+NVL(CB_OTH,0) CB,NVL(REC_KWH,0) KWH,NVL(MN_KVAH,0) KVAH \r\n"
					+ "FROM LEDGER_HT_HIST WHERE TRUNC(TO_DATE(MON_YEAR,'MON-YYYY'),'MM')=add_months(trunc(to_date(?,'MON-YYYY'),'mm'),-1) ) L,\r\n"
					+ "(SELECT MSCNO,NVL(MDOPNKVAH_HT,0) OPNRDG,NVL(MDCLKVAH_HT,0) CLRDG FROM MTRDAT WHERE MDMONTH=to_date(?,'MON-YYYY')) M,\r\n"
					+ "(SELECT BTSCNO,TO_CHAR(BTBLDT,'MON-YYYY') BDT,NVL(BTRKVA_HT,0) BMD,\r\n"
					+ "NVL(BTRKVAH_HT,0) RUNITS,(NVL(BTBKVAH,0)) BUNIT,NVL(BTTOD1,0)+NVL(BTTOD6,0) OPUNITS,NVL(BTTOD2,0)+NVL(BTTOD5,0) PUNITS,NVL(BTBLCOLNY_HT,0) COLUNITS,\r\n"
					+ "NVL(BTCURDEM,0)-(NVL(BTCUSTCHG,0)+NVL(BTADLCHG,0)+NVL(BTED,0)+NVL(BTED_INT,0)+NVL(BTDEMCHG_NOR,0)+NVL(BTDEMCHG_PEN,0)+NVL(BTOTHERCHG,0)) BEC,\r\n"
					+ "NVL(BTCUSTCHG,0) BCC,NVL(BTADLCHG,0) BSUR,NVL(BTDEMCHG_NOR,0)+NVL(BTDEMCHG_PEN,0) FIXED,NVL(BTED,0) BED,NVL(BTPF_HT,0)  BPF,NVL(BTED_INT,0) BEDI,\r\n"
					+ "NVL(BTVOLTSURCHG,0) VSUR,NVL(BTTOD1,0)+NVL(BTTOD6,0) OFPU,NVL(BTTOD2,0)+NVL(BTTOD5,0) PEU,NVL(BTBLCOLNY_HT,0) COL,NVL(BTTODCHG_HT,0) BTOD,NVL(BTLFINCENTIVE_HT,0) INC,\r\n"
					+ "case when nvl(CBTOT,0) >=0 then nvl(CBTOT,0) else 0 end + case when nvl(BTACD_DUE,0) >=0 then nvl(BTACD_DUE,0) else 0 end + nvl(BTCURDEM,0) DEM,to_char(BTDUEDT,'DD-MON-YYYY') BTDUEDT ,to_char(BTDISCDT,'DD-MON-YYYY')  BTDISCDT  FROM BILL WHERE to_char(BTBLDT,'MON-YYYY')=?),\r\n"
					+ "--(NVL(BTCURDEM,0)+NVL(BTCOURT_LPC,0)) DEM FROM BILL WHERE BTBLDT LIKE '%-12-21'),\r\n"
					+ "(SELECT USCNO SCNO,PAY_DATE LPDT,SUM(NVL(PCMD,0)) lpay from pay_ht where (uscno,pay_date) IN(SELECT USCNO SCNO,max(pay_date) mdt FROM PAY_ht WHERE pay_sta_flg='C' GROUP BY USCNO) AND pay_sta_flg='C' GROUP BY USCNO,PAY_DATE\r\n"
					+ "  union\r\n"
					+ "SELECT USCNO SCNO,PAY_DATE LPDT,SUM(NVL(PCMD,0)) lpay FROM PAY_ht_hist WHERE (uscno,pay_date) IN(SELECT USCNO SCNO,max(pay_date) mdt FROM PAY_ht_HIST WHERE SUBSTR(USCNO,1,3) IN('GNT','VJA','ONG','CRD') AND pay_sta_flg='C' GROUP BY USCNO) AND pay_sta_flg='C' AND SUBSTR(USCNO,1,3) IN('GNT','VJA','ONG','CRD') GROUP BY USCNO,PAY_DATE)\r\n"
					+ "WHERE substr(ctseccd,-5)=seccd AND CTUSCNO=BTSCNO AND CTUSCNO=USCNO(+) AND CTUSCNO=MSCNO(+)  AND ctuscno=scno(+) and \r\n"
					+ "ctfeeder_code=fmsapfcode AND ctservtype=stcode and ctservtype IN('62','5') order by stdesc";
			log.info(circleWisedemandsql);
			return jdbcTemplate.queryForList(circleWisedemandsql, new Object[] { monthYear, monthYear, monthYear });
		} catch (DataAccessException e) {
			log.error(e.getMessage());
			e.printStackTrace();
			return Collections.emptyList();
		}

	}

	public List<Map<String, Object>> getHTSalesYearlyReport(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		// String fmonthYear = request.getParameter("fmonth") + "-" +
		// request.getParameter("fyear");
		String fyear[] = request.getParameter("year").split("-");
		String fromyear = "01-04-" + fyear[0];
		String toyear = "31-03-" + fyear[1];

		// System.out.println(fmonthYear+"::"+circle);
		if (circle.equals("ALL")) {
			try {
				String circleWisedemandsql = "sELECT  UNIQUE BDT BILL_DT,cirname CIRCLE,HT_CAT CAT,HT_SUBCAT SUBCAT,DESC_HT DESCRIPT ,\r\n"
						+ "B.BTACTUAL_KV VOLTAGE,COUNT(*) NOS,ROUND(SUM(CTCMD_HT)) CMD,ROUND(SUM(NVL(BMD,0))) RMD,ROUND(SUM(NVL(REC_KWH,0))) REC_KWH,ROUND(SUM(NVL(RUNITS,0))) REC_UNITS,ROUND(SUM(NVL(BUNIT,0))) BILLED_UNITS,\r\n"
						+ "ROUND(SUM(NVL(OFPU,0))) OFF_PEAK_UNITS,ROUND(SUM(NVL(PEU,0))) PEAK_UNITS,ROUND(SUM(NVL(COL,0))) COLONY_UNITS,ROUND(SUM(NVL(EC,0))) ECHG,ROUND(SUM(NVL(FIXED,0))) FCHG,\r\n"
						+ "ROUND(SUM(NVL(CC,0))) CCHG,ROUND(SUM(NVL(ED,0))) EDCHG,ROUND(SUM(NVL(EC,0)+NVL(FIXED,0)+NVL(CC,0)+NVL(ED,0))) DEMAND_CHGS,ROUND(SUM(NVL(CR_AMT,0)+NVL(PAY,0))) COLL,\r\n"
						+ "ROUND(SUM(NVL(INC,0))) INCENTIVE_AMT,ROUND(SUM(NVL(VSUR,0))) VOLTAGE_SURCHARGE_AMT from cons,SPDCLMASTER,HT_TARIFF,\r\n"
						+ "(SELECT BTSCNO,TO_CHAR(BTBLDT,'MON-YYYY') BDT,NVL(BTRKVA_HT,0) BMD,NVL(BTRKWH_HT,0) REC_KWH, NVL(BTRKVAH_HT,0) RUNITS,(NVL(BTBKVAH,0)) BUNIT,\r\n"
						+ "case when TRIM(BTBLCAT)||TRIM(BTBLSCAT) NOT IN ('3A')then 0 else NVL(BTTOD1,0) END +case when TRIM(BTBLCAT)||TRIM(BTBLSCAT) NOT IN ('3A')then 0 else NVL(BTTOD6,0) END OPUNITS,\r\n"
						+ "case when TRIM(BTBLCAT)||TRIM(BTBLSCAT) NOT IN ('3A')then 0 else NVL(BTTOD2,0) END+ NVL(BTTOD5,0) PUNITS,\r\n"
						+ "NVL(BTBLCOLNY_HT,0) COLUNITS,NVL(BTCURDEM,0)-(NVL(BTCUSTCHG,0)+NVL(BTADLCHG,0)+NVL(BTED,0)+NVL(BTED_INT,0)+NVL(BTDEMCHG_NOR,0)+NVL(BTDEMCHG_PEN,0)+NVL(BTOTHERCHG,0)) EC,\r\n"
						+ "NVL(BTCUSTCHG,0) CC,NVL(BTDEMCHG_NOR,0)+NVL(BTDEMCHG_PEN,0) FIXED,NVL(BTED,0) ED,NVL(BTVOLTSURCHG,0) VSUR,\r\n"
						+ "case when TRIM(BTBLCAT)||TRIM(BTBLSCAT) NOT IN ('3A')then 0 else NVL(BTTOD1,0) END +case when TRIM(BTBLCAT)||TRIM(BTBLSCAT) NOT IN ('3A')then 0 else NVL(BTTOD6,0) END OFPU,\r\n"
						+ "case when TRIM(BTBLCAT)||TRIM(BTBLSCAT) NOT IN ('3A')then 0 else NVL(BTTOD2,0) END+ NVL(BTTOD5,0) PEU,\r\n"
						+ "NVL(BTBLCOLNY_HT,0) COL,NVL(BTLFINCENTIVE_HT,0) INC,\r\n"
						+ "(NVL(BTCURDEM,0)+NVL(BTCOURT_LPC,0)) DEM,trim(BTBLCAT)||trim(BTBLSCAT) BCAT,BTACTUAL_KV FROM BILL \r\n"
						+ "UNION\r\n"
						+ "SELECT BTSCNO,TO_CHAR(BTBLDT,'MON-YYYY') BDT,NVL(BTRKVA_HT,0) BMD,NVL(BTRKWH_HT,0) REC_KWH,NVL(BTRKVAH_HT,0) RUNITS,(NVL(BTBKVAH,0)) BUNIT,\r\n"
						+ "case when TRIM(BTBLCAT)||TRIM(BTBLSCAT) NOT IN ('3A')then 0 else NVL(BTTOD1,0) END +case when TRIM(BTBLCAT)||TRIM(BTBLSCAT) NOT IN ('3A')then 0 else NVL(BTTOD6,0) END OPUNITS,\r\n"
						+ "case when TRIM(BTBLCAT)||TRIM(BTBLSCAT) NOT IN ('3A')then 0 else NVL(BTTOD2,0) END+ NVL(BTTOD5,0) PUNITS,\r\n"
						+ "NVL(BTBLCOLNY_HT,0) COLUNITS,NVL(BTCURDEM,0)-(NVL(BTCUSTCHG,0)+NVL(BTADLCHG,0)+NVL(BTED,0)+NVL(BTED_INT,0)+NVL(BTDEMCHG_NOR,0)+NVL(BTDEMCHG_PEN,0)+NVL(BTOTHERCHG,0)) EC,\r\n"
						+ "NVL(BTCUSTCHG,0) CC,NVL(BTDEMCHG_NOR,0)+NVL(BTDEMCHG_PEN,0) FIXED,NVL(BTED,0) ED,NVL(BTVOLTSURCHG,0) VSUR,\r\n"
						+ "case when TRIM(BTBLCAT)||TRIM(BTBLSCAT) NOT IN ('3A')then 0 else NVL(BTTOD1,0) END +case when TRIM(BTBLCAT)||TRIM(BTBLSCAT) NOT IN ('3A')then 0 else NVL(BTTOD6,0) END OFPU,\r\n"
						+ "case when TRIM(BTBLCAT)||TRIM(BTBLSCAT) NOT IN ('3A')then 0 else NVL(BTTOD2,0) END+ NVL(BTTOD5,0) PEU,\r\n"
						+ "NVL(BTBLCOLNY_HT,0) COL,NVL(BTLFINCENTIVE_HT,0) INC,\r\n"
						+ "(NVL(BTCURDEM,0)+NVL(BTCOURT_LPC,0)) DEM ,trim(BTBLCAT)||trim(BTBLSCAT) BCAT,BTACTUAL_KV FROM BILL_HIST  ) B, \r\n"
						+ "(select uscno,sum(pay) PAY ,PDT from\r\n"
						+ "(select USCNO,NVL(PCMD,0) PAY,to_char(PAY_DATE,'MON-YYYY') PDT,pay_sta_flg from pay_ht \r\n"
						+ "union all select USCNO,NVL(PCMD,0) PAY,to_char(PAY_DATE,'MON-YYYY') PDT,pay_sta_flg from pay_ht_hist) where pay_sta_flg='C' AND\r\n"
						+ "to_date(PDT,'MON-YYYY') BETWEEN trunc(TO_DATE(?,'DD-MM-YYYY')) AND trunc(TO_DATE(?,'DD-MM-YYYY')) group by uscno,pdt) P,\r\n"
						+ "(select uscno,SUM(CASE WHEN RJTYPE='CR'THEN (nvl(ENGCHG,0)+nvl(THEFT,0)+nvl(OTHCHG,0)+nvl(CUSTCHG,0)+nvl(DEMCHG,0)+nvl(ED,0)+nvl(LPC,0)+nvl(EDI,0)\r\n"
						+ "+nvl(FSA,0)+NVL(CC_LPC,0)+NVL(CC_OTH,0)) END) CR_AMT ,to_char(rjdt,'MON-YYYY') RJDT\r\n"
						+ "from (select uscno,RJTYPE,ENGCHG,THEFT,OTHCHG,CUSTCHG,DEMCHG,ED,LPC,EDI,FSA,CC_LPC,CC_OTH,rjdt,status from JOURNAL \r\n"
						+ "union all select uscno,RJTYPE,ENGCHG,THEFT,OTHCHG,CUSTCHG,DEMCHG,ED,LPC,EDI,FSA,CC_LPC,CC_OTH,rjdt,status from JOURNAL_HIST) WHERE  SUBSTR(USCNO,1,3) IN('GNT','VJA','ONG','CRD') AND TRIM(STATUS) NOT IN ('X')\r\n"
						+ " and RJDT BETWEEN trunc(TO_DATE(?,'DD-MM-YYYY')) AND trunc(TO_DATE(?,'DD-MM-YYYY'))\r\n"
						+ " GROUP BY USCNO,to_char(rjdt,'MON-YYYY')) RJ\r\n"
						+ "WHERE substr(ctseccd,-5)=seccd AND CTUSCNO=BTSCNO AND CTUSCNO=P.USCNO(+)  AND B.BDT=P.PDT(+) AND p.PDT=RJ.RJDT(+) "
						+ "AND to_date(BDT,'MON-YYYY') BETWEEN trunc(TO_DATE(?,'DD-MM-YYYY')) AND trunc(TO_DATE(?,'DD-MM-YYYY')) AND BCAT=CATEGORY AND to_char(FROM_DT,'dd-mm-yyyy')=?\r\n"
						+ "AND CTUSCNO=RJ.USCNO(+)  AND CTACTUAL_KV=VOLTAGE GROUP BY BDT,cirname,HT_CAT,HT_SUBCAT,B.BTACTUAL_KV,DESC_HT ORDER BY BDT,cirname,HT_CAT,HT_SUBCAT,B.BTACTUAL_KV,DESC_HT";
				log.info(circleWisedemandsql);
				return jdbcTemplate.queryForList(circleWisedemandsql,
						new Object[] { fromyear, toyear, fromyear, toyear, fromyear, toyear, fromyear });
			} catch (DataAccessException e) {
				log.error(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}
		} else {
			try {
				String circleWisedemandsql = "sELECT  UNIQUE BDT BILL_DT,cirname CIRCLE,HT_CAT CAT,HT_SUBCAT SUBCAT,DESC_HT DESCRIPT ,\r\n"
						+ "B.BTACTUAL_KV VOLTAGE,COUNT(*) NOS,ROUND(SUM(CTCMD_HT)) CMD,ROUND(SUM(NVL(BMD,0))) RMD,ROUND(SUM(NVL(REC_KWH,0))) REC_KWH,ROUND(SUM(NVL(RUNITS,0))) REC_UNITS,ROUND(SUM(NVL(BUNIT,0))) BILLED_UNITS,\r\n"
						+ "ROUND(SUM(NVL(OFPU,0))) OFF_PEAK_UNITS,ROUND(SUM(NVL(PEU,0))) PEAK_UNITS,ROUND(SUM(NVL(COL,0))) COLONY_UNITS,ROUND(SUM(NVL(EC,0))) ECHG,ROUND(SUM(NVL(FIXED,0))) FCHG,\r\n"
						+ "ROUND(SUM(NVL(CC,0))) CCHG,ROUND(SUM(NVL(ED,0))) EDCHG,ROUND(SUM(NVL(EC,0)+NVL(FIXED,0)+NVL(CC,0)+NVL(ED,0))) DEMAND_CHGS,ROUND(SUM(NVL(CR_AMT,0)+NVL(PAY,0))) COLL,\r\n"
						+ "ROUND(SUM(NVL(INC,0))) INCENTIVE_AMT,ROUND(SUM(NVL(VSUR,0))) VOLTAGE_SURCHARGE_AMT from cons,SPDCLMASTER,HT_TARIFF,\r\n"
						+ "(SELECT BTSCNO,TO_CHAR(BTBLDT,'MON-YYYY') BDT,NVL(BTRKVA_HT,0) BMD,NVL(BTRKWH_HT,0) REC_KWH,NVL(BTRKVAH_HT,0) RUNITS,(NVL(BTBKVAH,0)) BUNIT,\r\n"
						+ "case when TRIM(BTBLCAT)||TRIM(BTBLSCAT) NOT IN ('3A')then 0 else NVL(BTTOD1,0) END +case when TRIM(BTBLCAT)||TRIM(BTBLSCAT) NOT IN ('3A')then 0 else NVL(BTTOD6,0) END OPUNITS,\r\n"
						+ "case when TRIM(BTBLCAT)||TRIM(BTBLSCAT) NOT IN ('3A')then 0 else NVL(BTTOD2,0) END+ NVL(BTTOD5,0) PUNITS,\r\n"
						+ "NVL(BTBLCOLNY_HT,0) COLUNITS,NVL(BTCURDEM,0)-(NVL(BTCUSTCHG,0)+NVL(BTADLCHG,0)+NVL(BTED,0)+NVL(BTED_INT,0)+NVL(BTDEMCHG_NOR,0)+NVL(BTDEMCHG_PEN,0)+NVL(BTOTHERCHG,0)) EC,\r\n"
						+ "NVL(BTCUSTCHG,0) CC,NVL(BTDEMCHG_NOR,0)+NVL(BTDEMCHG_PEN,0) FIXED,NVL(BTED,0) ED,NVL(BTVOLTSURCHG,0) VSUR,\r\n"
						+ "case when TRIM(BTBLCAT)||TRIM(BTBLSCAT) NOT IN ('3A')then 0 else NVL(BTTOD1,0) END +case when TRIM(BTBLCAT)||TRIM(BTBLSCAT) NOT IN ('3A')then 0 else NVL(BTTOD6,0) END OFPU,\r\n"
						+ "case when TRIM(BTBLCAT)||TRIM(BTBLSCAT) NOT IN ('3A')then 0 else NVL(BTTOD2,0) END+ NVL(BTTOD5,0) PEU,\r\n"
						+ "NVL(BTBLCOLNY_HT,0) COL,NVL(BTLFINCENTIVE_HT,0) INC,\r\n"
						+ "(NVL(BTCURDEM,0)+NVL(BTCOURT_LPC,0)) DEM,trim(BTBLCAT)||trim(BTBLSCAT) BCAT,BTACTUAL_KV FROM BILL \r\n"
						+ "UNION\r\n"
						+ "SELECT BTSCNO,TO_CHAR(BTBLDT,'MON-YYYY') BDT,NVL(BTRKVA_HT,0) BMD,NVL(BTRKWH_HT,0) REC_KWH,NVL(BTRKVAH_HT,0) RUNITS,(NVL(BTBKVAH,0)) BUNIT,\r\n"
						+ "case when TRIM(BTBLCAT)||TRIM(BTBLSCAT) NOT IN ('3A')then 0 else NVL(BTTOD1,0) END +case when TRIM(BTBLCAT)||TRIM(BTBLSCAT) NOT IN ('3A')then 0 else NVL(BTTOD6,0) END OPUNITS,\r\n"
						+ "case when TRIM(BTBLCAT)||TRIM(BTBLSCAT) NOT IN ('3A')then 0 else NVL(BTTOD2,0) END+ NVL(BTTOD5,0) PUNITS,\r\n"
						+ "NVL(BTBLCOLNY_HT,0) COLUNITS,NVL(BTCURDEM,0)-(NVL(BTCUSTCHG,0)+NVL(BTADLCHG,0)+NVL(BTED,0)+NVL(BTED_INT,0)+NVL(BTDEMCHG_NOR,0)+NVL(BTDEMCHG_PEN,0)+NVL(BTOTHERCHG,0)) EC,\r\n"
						+ "NVL(BTCUSTCHG,0) CC,NVL(BTDEMCHG_NOR,0)+NVL(BTDEMCHG_PEN,0) FIXED,NVL(BTED,0) ED,NVL(BTVOLTSURCHG,0) VSUR,\r\n"
						+ "case when TRIM(BTBLCAT)||TRIM(BTBLSCAT) NOT IN ('3A')then 0 else NVL(BTTOD1,0) END +case when TRIM(BTBLCAT)||TRIM(BTBLSCAT) NOT IN ('3A')then 0 else NVL(BTTOD6,0) END OFPU,\r\n"
						+ "case when TRIM(BTBLCAT)||TRIM(BTBLSCAT) NOT IN ('3A')then 0 else NVL(BTTOD2,0) END+ NVL(BTTOD5,0) PEU,\r\n"
						+ "NVL(BTBLCOLNY_HT,0) COL,NVL(BTLFINCENTIVE_HT,0) INC,\r\n"
						+ "(NVL(BTCURDEM,0)+NVL(BTCOURT_LPC,0)) DEM ,trim(BTBLCAT)||trim(BTBLSCAT) BCAT,BTACTUAL_KV FROM BILL_HIST  ) B, \r\n"
						+ "(select uscno,sum(pay) PAY ,PDT from\r\n"
						+ "(select USCNO,NVL(PCMD,0) PAY,to_char(PAY_DATE,'MON-YYYY') PDT,pay_sta_flg from pay_ht \r\n"
						+ "union all select USCNO,NVL(PCMD,0) PAY,to_char(PAY_DATE,'MON-YYYY') PDT,pay_sta_flg from pay_ht_hist) where pay_sta_flg='C' AND\r\n"
						+ "to_date(PDT,'MON-YYYY') BETWEEN trunc(TO_DATE(?,'DD-MM-YYYY')) AND trunc(TO_DATE(?,'DD-MM-YYYY')) group by uscno,pdt) P,\r\n"
						+ "(select uscno,SUM(CASE WHEN RJTYPE='CR'THEN (nvl(ENGCHG,0)+nvl(THEFT,0)+nvl(OTHCHG,0)+nvl(CUSTCHG,0)+nvl(DEMCHG,0)+nvl(ED,0)+nvl(LPC,0)+nvl(EDI,0)\r\n"
						+ "+nvl(FSA,0)+NVL(CC_LPC,0)+NVL(CC_OTH,0)) END) CR_AMT ,to_char(rjdt,'MON-YYYY') RJDT\r\n"
						+ "from (select uscno,RJTYPE,ENGCHG,THEFT,OTHCHG,CUSTCHG,DEMCHG,ED,LPC,EDI,FSA,CC_LPC,CC_OTH,rjdt,status from JOURNAL \r\n"
						+ "union all select uscno,RJTYPE,ENGCHG,THEFT,OTHCHG,CUSTCHG,DEMCHG,ED,LPC,EDI,FSA,CC_LPC,CC_OTH,rjdt,status from JOURNAL_HIST) WHERE  SUBSTR(USCNO,1,3) IN('GNT','VJA','ONG','CRD') AND TRIM(STATUS) NOT IN ('X')\r\n"
						+ " and RJDT BETWEEN trunc(TO_DATE(?,'DD-MM-YYYY')) AND trunc(TO_DATE(?,'DD-MM-YYYY'))\r\n"
						+ " GROUP BY USCNO,to_char(rjdt,'MON-YYYY')) RJ\r\n"
						+ "WHERE substr(ctseccd,-5)=seccd AND CTUSCNO=BTSCNO AND CTUSCNO=P.USCNO(+)  AND B.BDT=P.PDT(+) AND p.PDT=RJ.RJDT(+) "
						+ "AND to_date(BDT,'MON-YYYY') BETWEEN trunc(TO_DATE(?,'DD-MM-YYYY')) AND trunc(TO_DATE(?,'DD-MM-YYYY')) AND BCAT=CATEGORY AND to_char(FROM_DT,'dd-mm-yyyy')=?\r\n"
						+ "AND CTUSCNO=RJ.USCNO(+)  AND CTACTUAL_KV=VOLTAGE AND SUBSTR(CTUSCNO,1,3) = ? GROUP BY BDT,cirname,HT_CAT,HT_SUBCAT,B.BTACTUAL_KV,DESC_HT ORDER BY BDT,cirname,HT_CAT,HT_SUBCAT,B.BTACTUAL_KV,DESC_HT";
				log.info(circleWisedemandsql);
				return jdbcTemplate.queryForList(circleWisedemandsql,
						new Object[] { fromyear, toyear, fromyear, toyear, fromyear, toyear, fromyear, circle });
			} catch (DataAccessException e) {
				log.error(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}

		}
	}

	public Object[] getUrbanRuralConsumers(HttpServletRequest request) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		String circle = request.getParameter("circle");
		String fy = request.getParameter("year");
		String fyear[] = request.getParameter("year").split("-");
		String fromdate = null;
		String todate = null;

		String fromyear = null;
		String toyear = null;

		String cfy = null;
		int year = Calendar.getInstance().get(Calendar.YEAR);

		int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
		// System.out.println("Financial month : " + month);
		if (month < 3) {
			cfy = (year - 1) + "-" + year;
		} else {
			cfy = year + "-" + (year + 1);
		}

		if (cfy.equals(fy)) {
			Date today = new Date();

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(today);

			calendar.add(Calendar.MONTH, 1);
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			calendar.add(Calendar.DATE, -1);

			Date lastDayOfMonth = calendar.getTime();

			DateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
			DateFormat mmmyyyy = new SimpleDateFormat("MMM-yyyy");
			fromdate = "01-APR-" + fyear[0];
			todate = sdf.format(lastDayOfMonth).toUpperCase();

			fromyear = "APR-" + fyear[0];
			toyear = mmmyyyy.format(lastDayOfMonth).toUpperCase();

		} else {
			fromdate = "01-APR-" + fyear[0];
			todate = "31-MAR-" + fyear[1];
			fromyear = "APR-" + fyear[0];
			toyear = "MAR-" + fyear[1];
		}

		log.info(fromdate + "/" + todate + "/" + fromyear + "/" + toyear);
		// System.out.println(fmonthYear+"::"+circle);
		if (circle.equals("ALL")) {
			try {
				String circleWisedemandsql = "SELECT UNIQUE CASE WHEN ctcat='HT1' THEN 'DOMESTIC' WHEN CTCAT='HT2' THEN 'COMMERCIAL' WHEN CTCAT='HT3'  THEN CASE WHEN  CTACTUAL_KV=11 THEN 'INDUSTRIAL-LOW_MED_V' \r\n"
						+ "WHEN CTACTUAL_KV=33 THEN 'INDUSTRIAL-HV'  WHEN CTACTUAL_KV>33 THEN 'INDUSTRIAL-EHV' END  WHEN CTCAT='HT4' AND CTSUBCAT='D' THEN 'RAILWAYS'\r\n"
						+ "WHEN CTCAT='HT5' AND CTSUBCAT='E' THEN 'IRRIGATION' WHEN CTCAT='HT4' AND CTSUBCAT='A' THEN 'PUBLIC_UTILITIES' WHEN CTCAT='HT4' AND CTSUBCAT IN ('B','C') THEN 'OTHERS'\r\n"
						+ "WHEN CTCAT='HT5' AND CTSUBCAT IN ('B') THEN 'OTHERS' END CATEGORY,\r\n"
						+ "COUNT(DISTINCT(case when (NVL(ctloca_type,'P') like '%C%' OR NVL(ctloca_type,'P') like '%M%') AND MON_YEAR=? then L.USCNO  END)) BEGIN_URBAN_NOS,\r\n"
						+ "COUNT(DISTINCT(case when NVL(ctloca_type,'P') not in('C','M') AND MON_YEAR=? then L.USCNO  END)) BEGIN_RURAL_NOS,\r\n"
						+ "COUNT(DISTINCT(case when (NVL(ctloca_type,'P') like '%C%' OR NVL(ctloca_type,'P') like '%M%')  then N.USCNO  END)) DURING_URBAN_NOS,\r\n"
						+ "COUNT(DISTINCT(case when  NVL(ctloca_type,'P') not in('C','M')  then N.USCNO  END)) DURING_RURAL_NOS,\r\n"
						+ "COUNT(DISTINCT(case when (NVL(ctloca_type,'P') like '%C%' OR NVL(ctloca_type,'P') like '%M%') AND MON_YEAR=? then L.USCNO  END)) END_URBAN_NOS,\r\n"
						+ "COUNT(DISTINCT(case when  NVL(ctloca_type,'P') not in('C','M') AND MON_YEAR=? then L.USCNO  END)) END_RURAL_NOS,\r\n"
						+ "SUM(case when (NVL(ctloca_type,'P') like '%C%' OR NVL(ctloca_type,'P') like '%M%') AND MON_YEAR=? then NVL(LOAD,0)  END) BEGIN_URBAN_LOAD,\r\n"
						+ "SUM(case when  NVL(ctloca_type,'P') not in('C','M') AND MON_YEAR=? then NVL(LOAD,0)  END) BEGIN_RURAL_LOAD,\r\n"
						+ "SUM(case when (NVL(ctloca_type,'P') like '%C%' OR NVL(ctloca_type,'P') like '%M%') AND MON_YEAR=? AND N.USCNO IS NOT NULL then NVL(LOAD,0)  END) DURING_URBAN_LOAD,\r\n"
						+ "SUM(case when  NVL(ctloca_type,'P') not in('C','M') AND MON_YEAR=? AND N.USCNO IS NOT NULL THEN NVL(LOAD,0)  END) DURING_RURAL_LOAD,\r\n"
						+ "SUM(case when (NVL(ctloca_type,'P') like '%C%' OR NVL(ctloca_type,'P') like '%M%') AND MON_YEAR=? then NVL(LOAD,0)  END) END_URBAN_LOAD,\r\n"
						+ "SUM(case when  NVL(ctloca_type,'P') not in('C','M') AND MON_YEAR=? then NVL(LOAD,0)  END) END_RURAL_LOAD,\r\n"
						+ "SUM(case when (NVL(ctloca_type,'P') like '%C%' OR NVL(ctloca_type,'P') like '%M%')  then NVL(UNITS,0)  END) KWH_URBAN,\r\n"
						+ "SUM(case when  NVL(ctloca_type,'P') not in('C','M') then NVL(UNITS,0)  END) KWH_RURAL from cons,\r\n"
						+ "(SELECT USCNO,CAT,MON_YEAR,LOAD,NVL(REC_KWH,0) UNITS FROM V_LEDGER WHERE TO_DATE(MON_YEAR,'MON-YYYY') BETWEEN ? AND ?) L,\r\n"
						+ "(SELECT USCNO FROM V_LEDGER WHERE MON_YEAR=? MINUS SELECT USCNO FROM V_LEDGER WHERE MON_YEAR=?) N\r\n"
						+ "where CTUSCNO=L.USCNO  AND CTUSCNO=N.USCNO(+) GROUP BY CASE WHEN ctcat='HT1' THEN 'DOMESTIC'\r\n"
						+ "WHEN CTCAT='HT2' THEN 'COMMERCIAL' WHEN CTCAT='HT3'  THEN CASE WHEN  CTACTUAL_KV=11 THEN 'INDUSTRIAL-LOW_MED_V'  WHEN CTACTUAL_KV=33 THEN 'INDUSTRIAL-HV'  \r\n"
						+ "WHEN CTACTUAL_KV>33 THEN 'INDUSTRIAL-EHV' END  WHEN CTCAT='HT4' AND CTSUBCAT='D' THEN 'RAILWAYS' WHEN CTCAT='HT5' AND CTSUBCAT='E' THEN 'IRRIGATION'\r\n"
						+ "WHEN CTCAT='HT4' AND CTSUBCAT='A' THEN 'PUBLIC_UTILITIES' WHEN CTCAT='HT4' AND CTSUBCAT IN ('B','C') THEN 'OTHERS' WHEN CTCAT='HT5' AND CTSUBCAT IN ('B') THEN 'OTHERS' END "
						+ " ORDER BY \r\n" + "     case when CATEGORY='DOMESTIC' then 1 \r\n"
						+ "     when CATEGORY='COMMERCIAL' then 2 \r\n"
						+ "     when CATEGORY='INDUSTRIAL-LOW_MED_V' then 3 \r\n"
						+ "     when CATEGORY='INDUSTRIAL-HV' then 4\r\n"
						+ "     when CATEGORY='INDUSTRIAL-EHV' then 5 \r\n"
						+ "      when CATEGORY='RAILWAYS' then 6\r\n" + "       when CATEGORY='IRRIGATION' then 7 \r\n"
						+ "        when CATEGORY='PUBLIC_UTILITIES' then 8 \r\n"
						+ "        when CATEGORY='OTHERS' then 9\r\n" + "     end";
				log.info(circleWisedemandsql);
				list.addAll(jdbcTemplate.queryForList(circleWisedemandsql,
						new Object[] { fromyear, fromyear, toyear, toyear, fromyear, fromyear, toyear, toyear, toyear,
								toyear, fromdate, todate, toyear, fromyear }));
			} catch (DataAccessException e) {
				log.error(e.getMessage());
				e.printStackTrace();
				list.addAll(Collections.emptyList());
			}
		} else {
			try {
				String circleWisedemandsql = "SELECT UNIQUE CASE WHEN ctcat='HT1' THEN 'DOMESTIC' WHEN CTCAT='HT2' THEN 'COMMERCIAL' WHEN CTCAT='HT3'  THEN CASE WHEN  CTACTUAL_KV=11 THEN 'INDUSTRIAL-LOW_MED_V' \r\n"
						+ "WHEN CTACTUAL_KV=33 THEN 'INDUSTRIAL-HV'  WHEN CTACTUAL_KV>33 THEN 'INDUSTRIAL-EHV' END  WHEN CTCAT='HT4' AND CTSUBCAT='D' THEN 'RAILWAYS'\r\n"
						+ "WHEN CTCAT='HT5' AND CTSUBCAT='E' THEN 'IRRIGATION' WHEN CTCAT='HT4' AND CTSUBCAT='A' THEN 'PUBLIC_UTILITIES' WHEN CTCAT='HT4' AND CTSUBCAT IN ('B','C') THEN 'OTHERS'\r\n"
						+ "WHEN CTCAT='HT5' AND CTSUBCAT IN ('B') THEN 'OTHERS' END CATEGORY,\r\n"
						+ "COUNT(DISTINCT(case when (NVL(ctloca_type,'P') like '%C%' OR NVL(ctloca_type,'P') like '%M%') AND MON_YEAR=? then L.USCNO  END)) BEGIN_URBAN_NOS,\r\n"
						+ "COUNT(DISTINCT(case when NVL(ctloca_type,'P') not in('C','M') AND MON_YEAR=? then L.USCNO  END)) BEGIN_RURAL_NOS,\r\n"
						+ "COUNT(DISTINCT(case when (NVL(ctloca_type,'P') like '%C%' OR NVL(ctloca_type,'P') like '%M%')  then N.USCNO  END)) DURING_URBAN_NOS,\r\n"
						+ "COUNT(DISTINCT(case when  NVL(ctloca_type,'P') not in('C','M')  then N.USCNO  END)) DURING_RURAL_NOS,\r\n"
						+ "COUNT(DISTINCT(case when (NVL(ctloca_type,'P') like '%C%' OR NVL(ctloca_type,'P') like '%M%') AND MON_YEAR=? then L.USCNO  END)) END_URBAN_NOS,\r\n"
						+ "COUNT(DISTINCT(case when  NVL(ctloca_type,'P') not in('C','M') AND MON_YEAR=? then L.USCNO  END)) END_RURAL_NOS,\r\n"
						+ "SUM(case when (NVL(ctloca_type,'P') like '%C%' OR NVL(ctloca_type,'P') like '%M%') AND MON_YEAR=? then NVL(LOAD,0)  END) BEGIN_URBAN_LOAD,\r\n"
						+ "SUM(case when  NVL(ctloca_type,'P') not in('C','M') AND MON_YEAR=? then NVL(LOAD,0)  END) BEGIN_RURAL_LOAD,\r\n"
						+ "SUM(case when (NVL(ctloca_type,'P') like '%C%' OR NVL(ctloca_type,'P') like '%M%') AND MON_YEAR=? AND N.USCNO IS NOT NULL then NVL(LOAD,0)  END) DURING_URBAN_LOAD,\r\n"
						+ "SUM(case when  NVL(ctloca_type,'P') not in('C','M') AND MON_YEAR=? AND N.USCNO IS NOT NULL THEN NVL(LOAD,0)  END) DURING_RURAL_LOAD,\r\n"
						+ "SUM(case when (NVL(ctloca_type,'P') like '%C%' OR NVL(ctloca_type,'P') like '%M%') AND MON_YEAR=? then NVL(LOAD,0)  END) END_URBAN_LOAD,\r\n"
						+ "SUM(case when  NVL(ctloca_type,'P') not in('C','M') AND MON_YEAR=? then NVL(LOAD,0)  END) END_RURAL_LOAD,\r\n"
						+ "SUM(case when (NVL(ctloca_type,'P') like '%C%' OR NVL(ctloca_type,'P') like '%M%')  then NVL(UNITS,0)  END) KWH_URBAN,\r\n"
						+ "SUM(case when  NVL(ctloca_type,'P') not in('C','M') then NVL(UNITS,0)  END) KWH_RURAL from cons,\r\n"
						+ "(SELECT USCNO,CAT,MON_YEAR,LOAD,NVL(REC_KWH,0) UNITS FROM V_LEDGER WHERE TO_DATE(MON_YEAR,'MON-YYYY') BETWEEN ? AND ?) L,\r\n"
						+ "(SELECT USCNO FROM V_LEDGER WHERE MON_YEAR=? MINUS SELECT USCNO FROM V_LEDGER WHERE MON_YEAR=?) N\r\n"
						+ "where CTUSCNO=L.USCNO and ctstatus=1 AND CTUSCNO=N.USCNO(+) AND SUBSTR(CTUSCNO,1,3)=? GROUP BY CASE WHEN ctcat='HT1' THEN 'DOMESTIC'\r\n"
						+ "WHEN CTCAT='HT2' THEN 'COMMERCIAL' WHEN CTCAT='HT3'  THEN CASE WHEN  CTACTUAL_KV=11 THEN 'INDUSTRIAL-LOW_MED_V'  WHEN CTACTUAL_KV=33 THEN 'INDUSTRIAL-HV'  \r\n"
						+ "WHEN CTACTUAL_KV>33 THEN 'INDUSTRIAL-EHV' END  WHEN CTCAT='HT4' AND CTSUBCAT='D' THEN 'RAILWAYS' WHEN CTCAT='HT5' AND CTSUBCAT='E' THEN 'IRRIGATION'\r\n"
						+ "WHEN CTCAT='HT4' AND CTSUBCAT='A' THEN 'PUBLIC_UTILITIES' WHEN CTCAT='HT4' AND CTSUBCAT IN ('B','C') THEN 'OTHERS' WHEN CTCAT='HT5' AND CTSUBCAT IN ('B') THEN 'OTHERS' END "
						+ " ORDER BY \r\n" + "     case when CATEGORY='DOMESTIC' then 1 \r\n"
						+ "     when CATEGORY='COMMERCIAL' then 2 \r\n"
						+ "     when CATEGORY='INDUSTRIAL-LOW_MED_V' then 3 \r\n"
						+ "     when CATEGORY='INDUSTRIAL-HV' then 4\r\n"
						+ "     when CATEGORY='INDUSTRIAL-EHV' then 5 \r\n"
						+ "      when CATEGORY='RAILWAYS' then 6\r\n" + "       when CATEGORY='IRRIGATION' then 7 \r\n"
						+ "        when CATEGORY='PUBLIC_UTILITIES' then 8 \r\n"
						+ "        when CATEGORY='OTHERS' then 9\r\n" + "     end";
				log.info(circleWisedemandsql);
				list.addAll(jdbcTemplate.queryForList(circleWisedemandsql,
						new Object[] { fromyear, fromyear, toyear, toyear, fromyear, fromyear, toyear, toyear, toyear,
								toyear, fromdate, todate, toyear, fromyear, circle }));
			} catch (DataAccessException e) {
				log.error(e.getMessage());
				e.printStackTrace();
				list.addAll(Collections.emptyList());
			}

		}
		Object[] obj = new Object[3];
		obj[0] = list;
		obj[1] = fromyear;
		obj[2] = toyear;

		return obj;
	}

	public List<Map<String, Object>> getHTSalesReport(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		String fmonthYear = request.getParameter("fmonth") + "-" + request.getParameter("fyear");
		int month = Integer.parseInt(request.getParameter("fmonth"));
		int year = Integer.parseInt(request.getParameter("fyear"));
		int fyear = 0;
		if (month <= 3) {
			fyear = year - 1;
			/* System.out.println("Financial Year : " + (year - 1) + "-" + year); */
		} else {
			fyear = year;
			System.out.println("Financial Year : " + year + "-" + (year + 1));
		}
		// System.out.println(fmonthYear+"::"+circle);
		if (circle.equals("ALL")) {
			try {
				String circleWisedemandsql = "sELECT  UNIQUE BDT BILL_DT,cirname CIRCLE,divname DIVISION,subname SUB_DIVISION,secname SECTION,HT_CAT CAT,HT_SUBCAT SUBCAT,DESC_HT DESCRIPT ,\r\n"
						+ "B.BTACTUAL_KV VOLTAGE,COUNT(*) NOS,ROUND(SUM(CTCMD_HT)) CMD,ROUND(SUM(NVL(BMD,0))) RMD,ROUND(SUM(NVL(RUNITS,0))) REC_UNITS,ROUND(SUM(NVL(BUNIT,0))) BILLED_UNITS,\r\n"
						+ "ROUND(SUM(NVL(OFPU,0))) OFF_PEAK_UNITS,ROUND(SUM(NVL(PEU,0))) PEAK_UNITS,ROUND(SUM(NVL(COL,0))) COLONY_UNITS,ROUND(SUM(NVL(EC,0))) ECHG,ROUND(SUM(NVL(FIXED,0))) FCHG,\r\n"
						+ "ROUND(SUM(NVL(CC,0))) CCHG,ROUND(SUM(NVL(ED,0))) EDCHG,ROUND(SUM(NVL(EC,0)+NVL(FIXED,0)+NVL(CC,0)+NVL(ED,0))) DEMAND_CHGS,ROUND(SUM(NVL(CR_AMT,0)+NVL(PAY,0))) COLL,\r\n"
						+ "ROUND(SUM(NVL(INC,0))) INCENTIVE_AMT,ROUND(SUM(NVL(VSUR,0))) VOLTAGE_SURCHARGE_AMT from cons,SPDCLMASTER,HT_TARIFF,\r\n"
						+ "(SELECT BTSCNO,TO_CHAR(BTBLDT,'MON-YYYY') BDT,NVL(BTRKVA_HT,0) BMD,NVL(BTRKVAH_HT,0) RUNITS,(NVL(BTBKVAH,0)) BUNIT,\r\n"
						+ "case when TRIM(BTBLCAT)||TRIM(BTBLSCAT) NOT IN ('3A')then 0 else NVL(BTTOD1,0) END +case when TRIM(BTBLCAT)||TRIM(BTBLSCAT) NOT IN ('3A')then 0 else NVL(BTTOD6,0) END OPUNITS,\r\n"
						+ "case when TRIM(BTBLCAT)||TRIM(BTBLSCAT) NOT IN ('3A')then 0 else NVL(BTTOD2,0) END+ NVL(BTTOD5,0) PUNITS,\r\n"
						+ "NVL(BTBLCOLNY_HT,0) COLUNITS,NVL(BTCURDEM,0)-(NVL(BTCUSTCHG,0)+NVL(BTADLCHG,0)+NVL(BTED,0)+NVL(BTED_INT,0)+NVL(BTDEMCHG_NOR,0)+NVL(BTDEMCHG_PEN,0)+NVL(BTOTHERCHG,0)) EC,\r\n"
						+ "NVL(BTCUSTCHG,0) CC,NVL(BTDEMCHG_NOR,0)+NVL(BTDEMCHG_PEN,0) FIXED,NVL(BTED,0) ED,NVL(BTVOLTSURCHG,0) VSUR,\r\n"
						+ "case when TRIM(BTBLCAT)||TRIM(BTBLSCAT) NOT IN ('3A')then 0 else NVL(BTTOD1,0) END +case when TRIM(BTBLCAT)||TRIM(BTBLSCAT) NOT IN ('3A')then 0 else NVL(BTTOD6,0) END OFPU,\r\n"
						+ "case when TRIM(BTBLCAT)||TRIM(BTBLSCAT) NOT IN ('3A')then 0 else NVL(BTTOD2,0) END+ NVL(BTTOD5,0) PEU,\r\n"
						+ "NVL(BTBLCOLNY_HT,0) COL,NVL(BTLFINCENTIVE_HT,0) INC,\r\n"
						+ "(NVL(BTCURDEM,0)+NVL(BTCOURT_LPC,0)) DEM,trim(BTBLCAT)||trim(BTBLSCAT) BCAT,BTACTUAL_KV FROM BILL WHERE to_char(BTBLDT,'mm-yyyy')=? \r\n"
						+ "UNION\r\n"
						+ "SELECT BTSCNO,TO_CHAR(BTBLDT,'MON-YYYY') BDT,NVL(BTRKVA_HT,0) BMD,NVL(BTRKVAH_HT,0) RUNITS,(NVL(BTBKVAH,0)) BUNIT,\r\n"
						+ "case when TRIM(BTBLCAT)||TRIM(BTBLSCAT) NOT IN ('3A')then 0 else NVL(BTTOD1,0) END +case when TRIM(BTBLCAT)||TRIM(BTBLSCAT) NOT IN ('3A')then 0 else NVL(BTTOD6,0) END OPUNITS,\r\n"
						+ "case when TRIM(BTBLCAT)||TRIM(BTBLSCAT) NOT IN ('3A')then 0 else NVL(BTTOD2,0) END+ NVL(BTTOD5,0) PUNITS,\r\n"
						+ "NVL(BTBLCOLNY_HT,0) COLUNITS,NVL(BTCURDEM,0)-(NVL(BTCUSTCHG,0)+NVL(BTADLCHG,0)+NVL(BTED,0)+NVL(BTED_INT,0)+NVL(BTDEMCHG_NOR,0)+NVL(BTDEMCHG_PEN,0)+NVL(BTOTHERCHG,0)) EC,\r\n"
						+ "NVL(BTCUSTCHG,0) CC,NVL(BTDEMCHG_NOR,0)+NVL(BTDEMCHG_PEN,0) FIXED,NVL(BTED,0) ED,NVL(BTVOLTSURCHG,0) VSUR,\r\n"
						+ "case when TRIM(BTBLCAT)||TRIM(BTBLSCAT) NOT IN ('3A')then 0 else NVL(BTTOD1,0) END +case when TRIM(BTBLCAT)||TRIM(BTBLSCAT) NOT IN ('3A')then 0 else NVL(BTTOD6,0) END OFPU,\r\n"
						+ "case when TRIM(BTBLCAT)||TRIM(BTBLSCAT) NOT IN ('3A')then 0 else NVL(BTTOD2,0) END+ NVL(BTTOD5,0) PEU,\r\n"
						+ "NVL(BTBLCOLNY_HT,0) COL,NVL(BTLFINCENTIVE_HT,0) INC,\r\n"
						+ "(NVL(BTCURDEM,0)+NVL(BTCOURT_LPC,0)) DEM ,trim(BTBLCAT)||trim(BTBLSCAT) BCAT,BTACTUAL_KV FROM BILL_HIST WHERE to_char(BTBLDT,'mm-yyyy')=? ) B, \r\n"
						+ "(SELECT USCNO,SUM(NVL(PCMD,0)) PAY FROM PAY_ht WHERE pay_sta_flg='C' GROUP BY USCNO) P,\r\n"
						+ "(select uscno,SUM(CASE WHEN RJTYPE='CR'THEN (nvl(ENGCHG,0)+nvl(THEFT,0)+nvl(OTHCHG,0)+nvl(CUSTCHG,0)+nvl(DEMCHG,0)+nvl(ED,0)+nvl(LPC,0)+nvl(EDI,0)+nvl(FSA,0)+NVL(CC_LPC,0)+NVL(CC_OTH,0)) END) CR_AMT  from JOURNAL_HIST WHERE to_char(rjdt,'MM-YYYY')=?    and SUBSTR(USCNO,1,3) IN('GNT','VJA','ONG','CRD') AND TRIM(STATUS) NOT IN ('X') GROUP BY USCNO) RJ\r\n"
						+ "WHERE substr(ctseccd,-5)=seccd AND CTUSCNO=BTSCNO AND CTUSCNO=P.USCNO(+) AND BCAT=CATEGORY AND to_char(FROM_DT,'dd-mm-yyyy')='01-04-"
						+ fyear + "'\r\n"
						+ "AND CTUSCNO=RJ.USCNO(+)  AND CTACTUAL_KV=VOLTAGE GROUP BY BDT,cirname,divname,subname,secname,HT_CAT,HT_SUBCAT,B.BTACTUAL_KV,DESC_HT ORDER BY CIRCLE,DIVISION,SUB_DIVISION,SECTION,CAT,SUBCAT,VOLTAGE";
				log.info(circleWisedemandsql);
				return jdbcTemplate.queryForList(circleWisedemandsql,
						new Object[] { fmonthYear, fmonthYear, fmonthYear });
			} catch (DataAccessException e) {
				log.error(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}
		} else {
			try {
				/*
				 * String circleWisedemandsql =
				 * "sELECT  UNIQUE BDT BILL_DT,cirname CIRCLE,divname DIVISION,subname SUB_DIVISION,secname SECTION,CTCAT CAT,CTSUBCAT SUBCAT,DESC_HT DESCRIPT ,\r\n"
				 * +
				 * "CTACTUAL_KV VOLTAGE,COUNT(*) NOS,ROUND(SUM(CTCMD_HT)) CMD,ROUND(SUM(NVL(BMD,0))) RMD,ROUND(SUM(NVL(RUNITS,0))) REC_UNITS,ROUND(SUM(NVL(BUNIT,0))) BILLED_UNITS,\r\n"
				 * +
				 * "ROUND(SUM(NVL(OFPU,0))) OFF_PEAK_UNITS,ROUND(SUM(NVL(PEU,0))) PEAK_UNITS,ROUND(SUM(NVL(COL,0))) COLONY_UNITS,ROUND(SUM(NVL(EC,0))) ECHG,ROUND(SUM(NVL(FIXED,0))) FCHG,\r\n"
				 * +
				 * "ROUND(SUM(NVL(CC,0))) CCHG,ROUND(SUM(NVL(ED,0))) EDCHG,ROUND(SUM(NVL(EC,0)+NVL(FIXED,0)+NVL(CC,0)+NVL(ED,0))) DEMAND_CHGS,ROUND(SUM(NVL(CR_AMT,0)+NVL(PAY,0))) COLL,\r\n"
				 * +
				 * "ROUND(SUM(NVL(INC,0))) INCENTIVE_AMT,ROUND(SUM(NVL(VSUR,0))) VOLTAGE_SURCHARGE_AMT from cons,SPDCLMASTER,HT_TARIFF,\r\n"
				 * +
				 * "(SELECT BTSCNO,TO_CHAR(BTBLDT,'MON-YYYY') BDT,NVL(BTRKVA_HT,0) BMD,NVL(BTRKVAH_HT,0) RUNITS,(NVL(BTBKVAH,0)) BUNIT,\r\n"
				 * +
				 * "case when TRIM(BTBLCAT)||TRIM(BTBLSCAT) NOT IN ('3A')then 0 else NVL(BTTOD1,0) END +case when TRIM(BTBLCAT)||TRIM(BTBLSCAT) NOT IN ('3A')then 0 else NVL(BTTOD6,0) END OPUNITS,\r\n"
				 * +
				 * "case when TRIM(BTBLCAT)||TRIM(BTBLSCAT) NOT IN ('3A')then 0 else NVL(BTTOD2,0) END+ NVL(BTTOD5,0) PUNITS,\r\n"
				 * +
				 * "NVL(BTBLCOLNY_HT,0) COLUNITS,NVL(BTCURDEM,0)-(NVL(BTCUSTCHG,0)+NVL(BTADLCHG,0)+NVL(BTED,0)+NVL(BTED_INT,0)+NVL(BTDEMCHG_NOR,0)+NVL(BTDEMCHG_PEN,0)+NVL(BTOTHERCHG,0)) EC,\r\n"
				 * +
				 * "NVL(BTCUSTCHG,0) CC,NVL(BTDEMCHG_NOR,0)+NVL(BTDEMCHG_PEN,0) FIXED,NVL(BTED,0) ED,NVL(BTVOLTSURCHG,0) VSUR,\r\n"
				 * +
				 * "case when TRIM(BTBLCAT)||TRIM(BTBLSCAT) NOT IN ('3A')then 0 else NVL(BTTOD1,0) END +case when TRIM(BTBLCAT)||TRIM(BTBLSCAT) NOT IN ('3A')then 0 else NVL(BTTOD6,0) END OFPU,\r\n"
				 * +
				 * "case when TRIM(BTBLCAT)||TRIM(BTBLSCAT) NOT IN ('3A')then 0 else NVL(BTTOD2,0) END+ NVL(BTTOD5,0) PEU,\r\n"
				 * + "NVL(BTBLCOLNY_HT,0) COL,NVL(BTLFINCENTIVE_HT,0) INC,\r\n" +
				 * "(NVL(BTCURDEM,0)+NVL(BTCOURT_LPC,0)) DEM FROM BILL WHERE to_char(BTBLDT,'mm-yyyy')=? \r\n"
				 * + "UNION\r\n" +
				 * "SELECT BTSCNO,TO_CHAR(BTBLDT,'MON-YYYY') BDT,NVL(BTRKVA_HT,0) BMD,NVL(BTRKVAH_HT,0) RUNITS,(NVL(BTBKVAH,0)) BUNIT,\r\n"
				 * +
				 * "case when TRIM(BTBLCAT)||TRIM(BTBLSCAT) NOT IN ('3A')then 0 else NVL(BTTOD1,0) END +case when TRIM(BTBLCAT)||TRIM(BTBLSCAT) NOT IN ('3A')then 0 else NVL(BTTOD6,0) END OPUNITS,\r\n"
				 * +
				 * "case when TRIM(BTBLCAT)||TRIM(BTBLSCAT) NOT IN ('3A')then 0 else NVL(BTTOD2,0) END+ NVL(BTTOD5,0) PUNITS,\r\n"
				 * +
				 * "NVL(BTBLCOLNY_HT,0) COLUNITS,NVL(BTCURDEM,0)-(NVL(BTCUSTCHG,0)+NVL(BTADLCHG,0)+NVL(BTED,0)+NVL(BTED_INT,0)+NVL(BTDEMCHG_NOR,0)+NVL(BTDEMCHG_PEN,0)+NVL(BTOTHERCHG,0)) EC,\r\n"
				 * +
				 * "NVL(BTCUSTCHG,0) CC,NVL(BTDEMCHG_NOR,0)+NVL(BTDEMCHG_PEN,0) FIXED,NVL(BTED,0) ED,NVL(BTVOLTSURCHG,0) VSUR,\r\n"
				 * +
				 * "case when TRIM(BTBLCAT)||TRIM(BTBLSCAT) NOT IN ('3A')then 0 else NVL(BTTOD1,0) END +case when TRIM(BTBLCAT)||TRIM(BTBLSCAT) NOT IN ('3A')then 0 else NVL(BTTOD6,0) END OFPU,\r\n"
				 * +
				 * "case when TRIM(BTBLCAT)||TRIM(BTBLSCAT) NOT IN ('3A')then 0 else NVL(BTTOD2,0) END+ NVL(BTTOD5,0) PEU,\r\n"
				 * + "NVL(BTBLCOLNY_HT,0) COL,NVL(BTLFINCENTIVE_HT,0) INC,\r\n" +
				 * "(NVL(BTCURDEM,0)+NVL(BTCOURT_LPC,0)) DEM FROM BILL_HIST WHERE to_char(BTBLDT,'mm-yyyy')=? ) B, \r\n"
				 * +
				 * "(SELECT USCNO,SUM(NVL(PCMD,0)) PAY FROM PAY_ht WHERE pay_sta_flg='C' GROUP BY USCNO) P,\r\n"
				 * +
				 * "(select uscno,SUM(CASE WHEN RJTYPE='CR'THEN (nvl(ENGCHG,0)+nvl(THEFT,0)+nvl(OTHCHG,0)+nvl(CUSTCHG,0)+nvl(DEMCHG,0)+nvl(ED,0)+nvl(LPC,0)+nvl(EDI,0)+nvl(FSA,0)+NVL(CC_LPC,0)+NVL(CC_OTH,0)) END) CR_AMT  from JOURNAL_HIST WHERE to_char(rjdt,'MM-YYYY')=?    and SUBSTR(USCNO,1,3) IN('GNT','VJA','ONG','CRD') AND TRIM(STATUS) NOT IN ('X') GROUP BY USCNO) RJ\r\n"
				 * +
				 * "WHERE substr(ctseccd,-5)=seccd AND CTUSCNO=BTSCNO AND CTUSCNO=P.USCNO(+) AND CTCAT=HT_CAT AND CTSUBCAT=HT_SUBCAT AND to_char(FROM_DT,'dd-mm-yyyy')='01-04-2020'\r\n"
				 * +
				 * "AND CTUSCNO=RJ.USCNO(+) AND SUBSTR(CTUSCNO,1,3)=? GROUP BY BDT,cirname,divname,subname,secname,CTCAT,CTSUBCAT,CTACTUAL_KV,DESC_HT ORDER BY CIRCLE,DIVISION,SUB_DIVISION,SECTION,CAT,SUBCAT,VOLTAGE\r\n"
				 * + "";
				 */
				String circleWisedemandsql = "sELECT  UNIQUE BDT BILL_DT,cirname CIRCLE,divname DIVISION,subname SUB_DIVISION,secname SECTION,HT_CAT CAT,HT_SUBCAT SUBCAT,DESC_HT DESCRIPT ,\r\n"
						+ "B.BTACTUAL_KV VOLTAGE,COUNT(*) NOS,ROUND(SUM(CTCMD_HT)) CMD,ROUND(SUM(NVL(BMD,0))) RMD,ROUND(SUM(NVL(RUNITS,0))) REC_UNITS,ROUND(SUM(NVL(BUNIT,0))) BILLED_UNITS,\r\n"
						+ "ROUND(SUM(NVL(OFPU,0))) OFF_PEAK_UNITS,ROUND(SUM(NVL(PEU,0))) PEAK_UNITS,ROUND(SUM(NVL(COL,0))) COLONY_UNITS,ROUND(SUM(NVL(EC,0))) ECHG,ROUND(SUM(NVL(FIXED,0))) FCHG,\r\n"
						+ "ROUND(SUM(NVL(CC,0))) CCHG,ROUND(SUM(NVL(ED,0))) EDCHG,ROUND(SUM(NVL(EC,0)+NVL(FIXED,0)+NVL(CC,0)+NVL(ED,0))) DEMAND_CHGS,ROUND(SUM(NVL(CR_AMT,0)+NVL(PAY,0))) COLL,\r\n"
						+ "ROUND(SUM(NVL(INC,0))) INCENTIVE_AMT,ROUND(SUM(NVL(VSUR,0))) VOLTAGE_SURCHARGE_AMT from cons,SPDCLMASTER,HT_TARIFF,\r\n"
						+ "(SELECT BTSCNO,TO_CHAR(BTBLDT,'MON-YYYY') BDT,NVL(BTRKVA_HT,0) BMD,NVL(BTRKVAH_HT,0) RUNITS,(NVL(BTBKVAH,0)) BUNIT,\r\n"
						+ "case when TRIM(BTBLCAT)||TRIM(BTBLSCAT) NOT IN ('3A')then 0 else NVL(BTTOD1,0) END +case when TRIM(BTBLCAT)||TRIM(BTBLSCAT) NOT IN ('3A')then 0 else NVL(BTTOD6,0) END OPUNITS,\r\n"
						+ "case when TRIM(BTBLCAT)||TRIM(BTBLSCAT) NOT IN ('3A')then 0 else NVL(BTTOD2,0) END+ NVL(BTTOD5,0) PUNITS,\r\n"
						+ "NVL(BTBLCOLNY_HT,0) COLUNITS,NVL(BTCURDEM,0)-(NVL(BTCUSTCHG,0)+NVL(BTADLCHG,0)+NVL(BTED,0)+NVL(BTED_INT,0)+NVL(BTDEMCHG_NOR,0)+NVL(BTDEMCHG_PEN,0)+NVL(BTOTHERCHG,0)) EC,\r\n"
						+ "NVL(BTCUSTCHG,0) CC,NVL(BTDEMCHG_NOR,0)+NVL(BTDEMCHG_PEN,0) FIXED,NVL(BTED,0) ED,NVL(BTVOLTSURCHG,0) VSUR,\r\n"
						+ "case when TRIM(BTBLCAT)||TRIM(BTBLSCAT) NOT IN ('3A')then 0 else NVL(BTTOD1,0) END +case when TRIM(BTBLCAT)||TRIM(BTBLSCAT) NOT IN ('3A')then 0 else NVL(BTTOD6,0) END OFPU,\r\n"
						+ "case when TRIM(BTBLCAT)||TRIM(BTBLSCAT) NOT IN ('3A')then 0 else NVL(BTTOD2,0) END+ NVL(BTTOD5,0) PEU,\r\n"
						+ "NVL(BTBLCOLNY_HT,0) COL,NVL(BTLFINCENTIVE_HT,0) INC,\r\n"
						+ "(NVL(BTCURDEM,0)+NVL(BTCOURT_LPC,0)) DEM,trim(BTBLCAT)||trim(BTBLSCAT) BCAT,BTACTUAL_KV FROM BILL WHERE to_char(BTBLDT,'mm-yyyy')=? \r\n"
						+ "UNION\r\n"
						+ "SELECT BTSCNO,TO_CHAR(BTBLDT,'MON-YYYY') BDT,NVL(BTRKVA_HT,0) BMD,NVL(BTRKVAH_HT,0) RUNITS,(NVL(BTBKVAH,0)) BUNIT,\r\n"
						+ "case when TRIM(BTBLCAT)||TRIM(BTBLSCAT) NOT IN ('3A')then 0 else NVL(BTTOD1,0) END +case when TRIM(BTBLCAT)||TRIM(BTBLSCAT) NOT IN ('3A')then 0 else NVL(BTTOD6,0) END OPUNITS,\r\n"
						+ "case when TRIM(BTBLCAT)||TRIM(BTBLSCAT) NOT IN ('3A')then 0 else NVL(BTTOD2,0) END+ NVL(BTTOD5,0) PUNITS,\r\n"
						+ "NVL(BTBLCOLNY_HT,0) COLUNITS,NVL(BTCURDEM,0)-(NVL(BTCUSTCHG,0)+NVL(BTADLCHG,0)+NVL(BTED,0)+NVL(BTED_INT,0)+NVL(BTDEMCHG_NOR,0)+NVL(BTDEMCHG_PEN,0)+NVL(BTOTHERCHG,0)) EC,\r\n"
						+ "NVL(BTCUSTCHG,0) CC,NVL(BTDEMCHG_NOR,0)+NVL(BTDEMCHG_PEN,0) FIXED,NVL(BTED,0) ED,NVL(BTVOLTSURCHG,0) VSUR,\r\n"
						+ "case when TRIM(BTBLCAT)||TRIM(BTBLSCAT) NOT IN ('3A')then 0 else NVL(BTTOD1,0) END +case when TRIM(BTBLCAT)||TRIM(BTBLSCAT) NOT IN ('3A')then 0 else NVL(BTTOD6,0) END OFPU,\r\n"
						+ "case when TRIM(BTBLCAT)||TRIM(BTBLSCAT) NOT IN ('3A')then 0 else NVL(BTTOD2,0) END+ NVL(BTTOD5,0) PEU,\r\n"
						+ "NVL(BTBLCOLNY_HT,0) COL,NVL(BTLFINCENTIVE_HT,0) INC,\r\n"
						+ "(NVL(BTCURDEM,0)+NVL(BTCOURT_LPC,0)) DEM ,trim(BTBLCAT)||trim(BTBLSCAT) BCAT,BTACTUAL_KV FROM BILL_HIST WHERE to_char(BTBLDT,'mm-yyyy')=? ) B, \r\n"
						+ "(SELECT USCNO,SUM(NVL(PCMD,0)) PAY FROM PAY_ht WHERE pay_sta_flg='C' GROUP BY USCNO) P,\r\n"
						+ "(select uscno,SUM(CASE WHEN RJTYPE='CR'THEN (nvl(ENGCHG,0)+nvl(THEFT,0)+nvl(OTHCHG,0)+nvl(CUSTCHG,0)+nvl(DEMCHG,0)+nvl(ED,0)+nvl(LPC,0)+nvl(EDI,0)+nvl(FSA,0)+NVL(CC_LPC,0)+NVL(CC_OTH,0)) END) CR_AMT  from JOURNAL_HIST WHERE to_char(rjdt,'MM-YYYY')=?    and SUBSTR(USCNO,1,3) IN('GNT','VJA','ONG','CRD') AND TRIM(STATUS) NOT IN ('X') GROUP BY USCNO) RJ\r\n"
						+ "WHERE substr(ctseccd,-5)=seccd AND CTUSCNO=BTSCNO AND CTUSCNO=P.USCNO(+) AND BCAT=CATEGORY AND to_char(FROM_DT,'dd-mm-yyyy')='01-04-"
						+ fyear + "'\r\n"
						+ "AND CTUSCNO=RJ.USCNO(+)  AND SUBSTR(CTUSCNO,1,3)=?  AND CTACTUAL_KV=VOLTAGE GROUP BY BDT,cirname,divname,subname,secname,HT_CAT,HT_SUBCAT,B.BTACTUAL_KV,DESC_HT ORDER BY CIRCLE,DIVISION,SUB_DIVISION,SECTION,CAT,SUBCAT,VOLTAGE";

				log.info(circleWisedemandsql);
				return jdbcTemplate.queryForList(circleWisedemandsql,
						new Object[] { fmonthYear, fmonthYear, fmonthYear, circle });
			} catch (DataAccessException e) {
				log.error(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}

		}
	}

	public List<Map<String, Object>> getHtCatStatusWiseServicesWithLoadPage(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		String fmonthYear = request.getParameter("fmonth") + "-" + request.getParameter("fyear");
		/*
		 * String date = "01-"+request.getParameter("fmonth") + "-" +
		 * request.getParameter("fyear");
		 */
		System.out.println(fmonthYear + "::" + circle);
		try {
			String circleWisedemandsql = "select CIRCLE,\r\n" + "NVL(HT1_BS_NOS,0)  HT1_BS_NOS,\r\n"
					+ "NVL(HT1_BS_LOAD,0) HT1_BS_LOAD,\r\n" + "NVL(HT1_BS_CB,0)   HT1_BS_CB,\r\n"
					+ "NVL(HT1_LIVE_NOS,0) HT1_LIVE_NOS,\r\n" + "NVL(HT1_LIVE_LOAD,0) HT1_LIVE_LOAD,\r\n"
					+ "NVL(HT1_LIVE_CB,0)   HT1_LIVE_CB,\r\n" + "NVL(HT1_UDC_NOS,0)   HT1_UDC_NOS,\r\n"
					+ "NVL(HT1_UDC_LOAD,0) HT1_UDC_LOAD,\r\n" + "NVL(HT1_UDC_CB,0) HT1_UDC_CB,\r\n"
					+ "nvl(HT1_BS_NOS,0)+nvl(HT1_LIVE_NOS,0)+nvl(HT1_UDC_NOS,0) HT1_NOS_TOT,\r\n"
					+ "nvl(HT1_BS_LOAD,0)+nvl(HT1_LIVE_LOAD,0)+nvl(HT1_UDC_LOAD,0) HT1_LOAD_TOT,\r\n"
					+ "nvl(HT1_BS_CB,0)+nvl(HT1_LIVE_CB,0)+nvl(HT1_UDC_CB,0) HT1_CB_TOT,\r\n"
					+ "NVL(HT2_BS_NOS,0) HT2_BS_NOS, \r\n" + "NVL(HT2_BS_LOAD,0) HT2_BS_LOAD, \r\n"
					+ "NVL(HT2_BS_CB,0) HT2_BS_CB,\r\n" + "NVL(HT2_LIVE_NOS,0) HT2_LIVE_NOS,\r\n"
					+ "NVL(HT2_LIVE_LOAD,0) HT2_LIVE_LOAD,\r\n" + "NVL(HT2_LIVE_CB,0) HT2_LIVE_CB,\r\n"
					+ "NVL(HT2_UDC_NOS,0) HT2_UDC_NOS,\r\n" + "NVL(HT2_UDC_LOAD,0) HT2_UDC_LOAD,\r\n"
					+ "NVL(HT2_UDC_CB,0) HT2_UDC_CB,\r\n"
					+ "nvl(HT2_BS_NOS,0)+nvl(HT2_LIVE_NOS,0)+nvl(HT2_UDC_NOS,0) HT2_NOS_TOT,\r\n"
					+ "nvl(HT2_BS_LOAD,0)+nvl(HT2_LIVE_LOAD,0)+nvl(HT2_UDC_LOAD,0) HT2_LOAD_TOT,\r\n"
					+ "nvl(HT2_BS_CB,0)+nvl(HT2_LIVE_CB,0)+nvl(HT2_UDC_CB,0) HT2_CB_TOT,\r\n"
					+ "NVL(HT3_BS_NOS,0) HT3_BS_NOS,\r\n" + "NVL(HT3_BS_LOAD,0) HT3_BS_LOAD,\r\n"
					+ "NVL(HT3_BS_CB,0) HT3_BS_CB,\r\n" + "NVL(HT3_LIVE_NOS,0) HT3_LIVE_NOS,\r\n"
					+ "NVL(HT3_LIVE_LOAD,0) HT3_LIVE_LOAD,\r\n" + "NVL(HT3_LIVE_CB,0) HT3_LIVE_CB,\r\n"
					+ "NVL(HT3_UDC_NOS,0) HT3_UDC_NOS,\r\n" + "NVL(HT3_UDC_LOAD,0) HT3_UDC_LOAD,\r\n"
					+ "NVL(HT3_UDC_CB,0) HT3_UDC_CB,\r\n"
					+ "nvl(HT3_BS_NOS,0)+nvl(HT3_LIVE_NOS,0)+nvl(HT3_UDC_NOS,0) HT3_NOS_TOT,\r\n"
					+ "nvl(HT3_BS_LOAD,0)+nvl(HT3_LIVE_LOAD,0)+nvl(HT3_UDC_LOAD,0) HT3_LOAD_TOT,\r\n"
					+ "nvl(HT3_BS_CB,0)+nvl(HT3_LIVE_CB,0)+nvl(HT3_UDC_CB,0) HT3_CB_TOT,\r\n"
					+ "NVL(HT4_BS_NOS,0) HT4_BS_NOS, \r\n" + "NVL(HT4_BS_LOAD,0) HT4_BS_LOAD,\r\n"
					+ "NVL(HT4_BS_CB,0) HT4_BS_CB,\r\n" + "NVL(HT4_LIVE_NOS,0) HT4_LIVE_NOS,\r\n"
					+ "NVL(HT4_LIVE_LOAD,0) HT4_LIVE_LOAD,\r\n" + "NVL(HT4_LIVE_CB,0) HT4_LIVE_CB,\r\n"
					+ "NVL(HT4_UDC_NOS,0) HT4_UDC_NOS,\r\n" + "NVL(HT4_UDC_LOAD,0) HT4_UDC_LOAD,\r\n"
					+ "NVL(HT4_UDC_CB,0) HT4_UDC_CB,\r\n"
					+ "nvl(HT4_BS_NOS,0)+nvl(HT4_LIVE_NOS,0)+nvl(HT4_UDC_NOS,0) HT4_NOS_TOT,\r\n"
					+ "nvl(HT4_BS_LOAD,0)+nvl(HT4_LIVE_LOAD,0)+nvl(HT4_UDC_LOAD,0) HT4_LOAD_TOT,\r\n"
					+ "nvl(HT4_BS_CB,0)+nvl(HT4_LIVE_CB,0)+nvl(HT4_UDC_CB,0) HT4_CB_TOT,\r\n"
					+ "NVL(HT5_BS_NOS,0) HT5_BS_NOS, \r\n" + "NVL(HT5_BS_LOAD,0) HT5_BS_LOAD,\r\n"
					+ "NVL(HT5_BS_CB,0) HT5_BS_CB,\r\n" + "NVL(HT5_LIVE_NOS,0) HT5_LIVE_NOS,\r\n"
					+ "NVL(HT5_LIVE_LOAD,0) HT5_LIVE_LOAD, \r\n" + "NVL(HT5_LIVE_CB,0) HT5_LIVE_CB,\r\n"
					+ "NVL(HT5_UDC_NOS,0) HT5_UDC_NOS,\r\n" + "NVL(HT5_UDC_LOAD,0) HT5_UDC_LOAD,\r\n"
					+ "NVL(HT5_UDC_CB,0) HT5_UDC_CB,\r\n"
					+ "nvl(HT5_BS_NOS,0)+nvl(HT5_LIVE_NOS,0)+nvl(HT5_UDC_NOS,0) HT5_NOS_TOT,\r\n"
					+ "nvl(HT5_BS_LOAD,0)+nvl(HT5_LIVE_LOAD,0)+nvl(HT5_UDC_LOAD,0) HT5_LOAD_TOT,\r\n"
					+ "nvl(HT5_BS_CB,0)+nvl(HT5_LIVE_CB,0)+nvl(HT5_UDC_CB,0) HT5_CB_TOT,\r\n"
					+ "nvl(HT1_BS_NOS,0)+nvl(HT2_BS_NOS,0)+nvl(HT3_BS_NOS,0)+nvl(HT4_BS_NOS,0)+nvl(HT5_BS_NOS,0) BS_NOS_TOT,\r\n"
					+ "nvl(HT1_BS_LOAD,0)+nvl(HT2_BS_LOAD,0)+nvl(HT3_BS_LOAD,0)+nvl(HT4_BS_LOAD,0)+nvl(HT5_BS_LOAD,0) BS_LOAD_TOT,\r\n"
					+ "nvl(HT1_BS_CB,0)+nvl(HT2_BS_CB,0)+nvl(HT3_BS_CB,0)+nvl(HT4_BS_CB,0)+nvl(HT5_BS_CB,0) BS_CB_TOT,\r\n"
					+ "nvl(HT1_LIVE_NOS,0)+nvl(HT2_LIVE_NOS,0)+nvl(HT3_LIVE_NOS,0)+nvl(HT4_LIVE_NOS,0)+nvl(HT5_LIVE_NOS,0) LIVE_NOS_TOT,\r\n"
					+ "nvl(HT1_LIVE_LOAD,0)+nvl(HT2_LIVE_LOAD,0)+nvl(HT3_LIVE_LOAD,0)+nvl(HT4_LIVE_LOAD,0)+nvl(HT5_LIVE_LOAD,0) LIVE_LOAD_TOT,\r\n"
					+ "nvl(HT1_LIVE_CB,0)+nvl(HT2_LIVE_CB,0)+nvl(HT3_LIVE_CB,0)+nvl(HT4_LIVE_CB,0)+nvl(HT5_LIVE_CB,0) LIVE_CB_TOT,\r\n"
					+ "nvl(HT1_UDC_NOS,0)+nvl(HT2_UDC_NOS,0)+nvl(HT3_UDC_NOS,0)+nvl(HT4_UDC_NOS,0)+nvl(HT5_UDC_NOS,0) UDC_NOS_TOT,\r\n"
					+ "nvl(HT1_UDC_LOAD,0)+nvl(HT2_UDC_LOAD,0)+nvl(HT3_UDC_LOAD,0)+nvl(HT4_UDC_LOAD,0)+nvl(HT5_UDC_LOAD,0) UDC_LOAD_TOT,\r\n"
					+ "nvl(HT1_UDC_CB,0)+nvl(HT2_UDC_CB,0)+nvl(HT3_UDC_CB,0)+nvl(HT4_UDC_CB,0)+nvl(HT5_UDC_CB,0) UDC_CB_TOT,\r\n"
					+ "nvl(HT1_BS_NOS,0)+nvl(HT1_LIVE_NOS,0)+nvl(HT1_UDC_NOS,0)+nvl(HT2_BS_NOS,0)+nvl(HT2_LIVE_NOS,0)+nvl(HT2_UDC_NOS,0)+nvl(HT3_BS_NOS,0)+nvl(HT3_LIVE_NOS,0)+nvl(HT3_UDC_NOS,0)+nvl(HT4_BS_NOS,0)+nvl(HT4_LIVE_NOS,0)+nvl(HT4_UDC_NOS,0)+nvl(HT5_BS_NOS,0)+nvl(HT5_LIVE_NOS,0)+nvl(HT5_UDC_NOS,0) NOS_TOT,\r\n"
					+ "nvl(HT1_BS_LOAD,0)+nvl(HT1_LIVE_LOAD,0)+nvl(HT1_UDC_LOAD,0)+nvl(HT2_BS_LOAD,0)+nvl(HT2_LIVE_LOAD,0)+nvl(HT2_UDC_LOAD,0)+nvl(HT3_BS_LOAD,0)+nvl(HT3_LIVE_LOAD,0)+nvl(HT3_UDC_LOAD,0)+nvl(HT4_BS_LOAD,0)+nvl(HT4_LIVE_LOAD,0)+nvl(HT4_UDC_LOAD,0)+nvl(HT5_BS_LOAD,0)+nvl(HT5_LIVE_LOAD,0)+nvl(HT5_UDC_LOAD,0) LOAD_TOT,\r\n"
					+ "nvl(HT1_BS_CB,0)+nvl(HT1_LIVE_CB,0)+nvl(HT1_UDC_CB,0)+nvl(HT2_BS_CB,0)+nvl(HT2_LIVE_CB,0)+nvl(HT2_UDC_CB,0)+nvl(HT3_BS_CB,0)+nvl(HT3_LIVE_CB,0)+nvl(HT3_UDC_CB,0)+nvl(HT4_BS_CB,0)+nvl(HT4_LIVE_CB,0)+nvl(HT4_UDC_CB,0)+nvl(HT5_BS_CB,0)+nvl(HT5_LIVE_CB,0)+nvl(HT5_UDC_CB,0) CB_TOT\r\n"
					+ "from (SELECT SUBSTR(CTUSCNO,1,3) CIRCLE,\r\n"
					+ "COUNT(CASE WHEN CAT=1  AND  STATUS='BS' THEN USCNO END ) HT1_BS_NOS,\r\n"
					+ "sum(CASE WHEN CAT=1 AND STATUS='BS' THEN LOAD END ) HT1_BS_LOAD,\r\n"
					+ "SUM(CASE WHEN CAT=1 AND STATUS='BS' THEN round(CB) END ) HT1_BS_CB,\r\n"
					+ "COUNT(CASE WHEN CAT=1  AND  STATUS='LIVE' THEN USCNO END ) HT1_LIVE_NOS,\r\n"
					+ "sum(CASE WHEN CAT=1 AND STATUS='LIVE' THEN LOAD END ) HT1_LIVE_LOAD,\r\n"
					+ "SUM(CASE WHEN CAT=1 AND STATUS='LIVE' THEN round(CB) END ) HT1_LIVE_CB,\r\n"
					+ "COUNT(CASE WHEN CAT=1  AND  STATUS='UDC' THEN USCNO END ) HT1_UDC_NOS,\r\n"
					+ "sum(CASE WHEN CAT=1 AND  STATUS='UDC' THEN LOAD END ) HT1_UDC_LOAD,\r\n"
					+ "SUM(CASE WHEN CAT=1 AND  STATUS='UDC' THEN round(CB) END ) HT1_UDC_CB,\r\n"
					+ "COUNT(CASE WHEN CAT=2  AND  STATUS='BS' THEN USCNO END ) HT2_BS_NOS,\r\n"
					+ "sum(CASE WHEN CAT=2 AND  STATUS='BS' THEN LOAD END ) HT2_BS_LOAD,\r\n"
					+ "SUM(CASE WHEN CAT=2 AND  STATUS='BS' THEN round(CB) END ) HT2_BS_CB,\r\n"
					+ "COUNT(CASE WHEN CAT=2  AND STATUS='LIVE' THEN USCNO END ) HT2_LIVE_NOS,\r\n"
					+ "sum(CASE WHEN CAT=2 AND  STATUS='LIVE' THEN LOAD END ) HT2_LIVE_LOAD,\r\n"
					+ "SUM(CASE WHEN CAT=2 AND  STATUS='LIVE' THEN round(CB) END ) HT2_LIVE_CB,\r\n"
					+ "COUNT(CASE WHEN CAT=2  AND  STATUS='UDC' THEN USCNO END ) HT2_UDC_NOS,\r\n"
					+ "sum(CASE WHEN CAT=2 AND  STATUS='UDC' THEN LOAD END ) HT2_UDC_LOAD,\r\n"
					+ "SUM(CASE WHEN CAT=2 AND  STATUS='UDC' THEN round(CB) END ) HT2_UDC_CB,\r\n"
					+ "COUNT(CASE WHEN CAT=3  AND  STATUS='BS' THEN USCNO END ) HT3_BS_NOS,\r\n"
					+ "sum(CASE WHEN CAT=3 AND  STATUS='BS' THEN LOAD END ) HT3_BS_LOAD,\r\n"
					+ "SUM(CASE WHEN CAT=3 AND  STATUS='BS' THEN round(CB) END ) HT3_BS_CB,\r\n"
					+ "COUNT(CASE WHEN CAT=3  AND  STATUS='LIVE' THEN USCNO END ) HT3_LIVE_NOS,\r\n"
					+ "sum(CASE WHEN CAT=3 AND  STATUS='LIVE' THEN LOAD END ) HT3_LIVE_LOAD,\r\n"
					+ "SUM(CASE WHEN CAT=3 AND  STATUS='LIVE' THEN round(CB) END ) HT3_LIVE_CB,\r\n"
					+ "COUNT(CASE WHEN CAT=3  AND  STATUS='UDC' THEN USCNO END ) HT3_UDC_NOS,\r\n"
					+ "sum(CASE WHEN CAT=3 AND  STATUS='UDC' THEN LOAD END ) HT3_UDC_LOAD,\r\n"
					+ "SUM(CASE WHEN CAT=3 AND  STATUS='UDC' THEN round(CB) END ) HT3_UDC_CB,\r\n"
					+ "COUNT(CASE WHEN CAT=4  AND  STATUS='BS' THEN USCNO END ) HT4_BS_NOS,\r\n"
					+ "sum(CASE WHEN CAT=4 AND  STATUS='BS' THEN LOAD END ) HT4_BS_LOAD,\r\n"
					+ "SUM(CASE WHEN CAT=4 AND  STATUS='BS' THEN round(CB) END ) HT4_BS_CB,\r\n"
					+ "COUNT(CASE WHEN CAT=4  AND STATUS='LIVE' THEN USCNO END ) HT4_LIVE_NOS,\r\n"
					+ "sum(CASE WHEN CAT=4 AND  STATUS='LIVE' THEN LOAD END ) HT4_LIVE_LOAD,\r\n"
					+ "SUM(CASE WHEN CAT=4 AND  STATUS='LIVE' THEN round(CB) END ) HT4_LIVE_CB,\r\n"
					+ "COUNT(CASE WHEN CAT=4  AND  STATUS='UDC' THEN USCNO END ) HT4_UDC_NOS,\r\n"
					+ "sum(CASE WHEN CAT=4 AND  STATUS='UDC' THEN LOAD END ) HT4_UDC_LOAD,\r\n"
					+ "SUM(CASE WHEN CAT=4 AND  STATUS='UDC' THEN round(CB) END ) HT4_UDC_CB,\r\n"
					+ "COUNT(CASE WHEN CAT=5  AND  STATUS='BS' THEN USCNO END ) HT5_BS_NOS,\r\n"
					+ "sum(CASE WHEN CAT=5 AND  STATUS='BS' THEN LOAD END ) HT5_BS_LOAD,\r\n"
					+ "SUM(CASE WHEN CAT=5 AND  STATUS='BS' THEN round(CB) END ) HT5_BS_CB,\r\n"
					+ "COUNT(CASE WHEN CAT=5  AND  STATUS='LIVE' THEN USCNO END ) HT5_LIVE_NOS,\r\n"
					+ "sum(CASE WHEN CAT=5 AND  STATUS='LIVE' THEN LOAD END ) HT5_LIVE_LOAD,\r\n"
					+ "SUM(CASE WHEN CAT=5 AND  STATUS='LIVE' THEN round(CB) END ) HT5_LIVE_CB,\r\n"
					+ "COUNT(CASE WHEN CAT=5  AND  STATUS='UDC' THEN USCNO END ) HT5_UDC_NOS,\r\n"
					+ "sum(CASE WHEN CAT=5 AND  STATUS='UDC' THEN LOAD END ) HT5_UDC_LOAD,\r\n"
					+ "SUM(CASE WHEN CAT=5 AND  STATUS='UDC' THEN round(CB) END ) HT5_UDC_CB\r\n"
					+ "FROM CONS,(SELECT USCNO,CASE WHEN STATUS = '0'  THEN 'BS' WHEN STAT='LIV' THEN 'LIVE' WHEN STAT='UD' THEN 'UDC'  END STATUS,CAT,NVL(LOAD,0) LOAD,(round(NVL(CBTOT,0))+round(NVL(CB_CCLPC,0))+round(NVL(CB_OTH,0))) CB\r\n"
					+ " FROM  LEDGER_HT_HIST,(SELECT UNIQUE MSCNO,CASE WHEN MDCLKWHSTAT_HT='3' THEN 'UD' ELSE 'LIV' END STAT FROM MTRDAT_HIST WHERE TO_CHAR(MDMONTH,'MON-YYYY')=?) \r\n"
					+ "WHERE USCNO=MSCNO(+) AND MON_YEAR=?) \r\n" + "WHERE CTUSCNO=USCNO GROUP BY SUBSTR(CTUSCNO,1,3))";
			log.info(circleWisedemandsql);
			return jdbcTemplate.queryForList(circleWisedemandsql, new Object[] { fmonthYear, fmonthYear });
		} catch (DataAccessException e) {
			log.error(e.getMessage());
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	public List<Map<String, Object>> getBillStopServicesStatus(HttpServletRequest request) {
		String fmonthYear = request.getParameter("fmonth") + "-" + request.getParameter("fyear");
		/*
		 * String date = "01-"+request.getParameter("fmonth") + "-" +
		 * request.getParameter("fyear");
		 */
		try {
			String circleWisedemandsql = "SELECT SUBSTR(CTUSCNO,1,3) CIRCLE,\r\n"
					+ "COUNT(CASE WHEN  STATUS='BS' THEN L1.USCNO END )-COUNT(CASE WHEN ADDBS IS  NOT NULL THEN ADDBS END )+COUNT(CASE WHEN BSTOLIV IS  NOT NULL THEN BSTOLIV END ) BS_OB_NOS,\r\n"
					+ "SUM(CASE WHEN STATUS='BS' THEN ROUND(NVL(TOT_OB,0)) END ) BS_OB_AMT,\r\n"
					+ "COUNT(CASE WHEN ADDBS IS  NOT NULL THEN ADDBS END ) BS_DURING_NOS,\r\n"
					+ "SUM(CASE WHEN ADDBS IS  NOT NULL THEN ROUND(NVL(TOT_OB,0)) END ) BS_DURING_AMT,\r\n"
					+ "COUNT(CASE WHEN BSTOLIV IS  NOT NULL THEN BSTOLIV END ) LIVE_DURING_NOS,\r\n"
					+ "SUM(CASE WHEN BSTOLIV IS  NOT NULL THEN ROUND(NVL(TOT_OB,0)) END ) LIVE_DURING_AMT,\r\n"
					+ "COUNT(CASE WHEN STATUS='BS' AND (NVL(COLLECTION,0)+NVL(CR_AMT,0))>0 THEN L1.USCNO END ) BS_COLLECTED_NOS,\r\n"
					+ "SUM(CASE WHEN STATUS='BS' THEN ROUND(NVL(COLLECTION,0)+NVL(CR_AMT,0)) END ) BS_COLLECTED_AMT,\r\n"
					+ "COUNT(CASE WHEN STATUS='BS'  THEN L1.USCNO END ) BS_PENDING_NOS,\r\n"
					+ "SUM(CASE WHEN STATUS='BS' THEN ROUND(NVL(TOTAL_CB,0)) END ) BS_PENDING_AMT\r\n"
					+ "FROM CONS,\r\n"
					+ "(SELECT L.USCNO,CASE WHEN STAT='UD' THEN 'UDC' WHEN STAT IS NULL THEN 'BS' WHEN STAT='LIV' THEN 'LIVE' END STATUS,CAT,LOAD,CASE WHEN M2.MSCNO IS NOT NULL THEN M2.MSCNO END ADDBS,\r\n"
					+ "CASE WHEN M3.USCNO IS NOT NULL THEN M3.USCNO END BSTOLIV,\r\n"
					+ "(NVL(OB_OTH,0) +NVL(OB_CCLPC,0)+NVL(TOT_OB,0)) TOT_OB,\r\n" + "(NVL(TOT_PAY,0)) COLLECTION,\r\n"
					+ "(NVL(CBTOT,0)+NVL(CB_CCLPC,0)+NVL(CB_OTH,0)) TOTAL_CB\r\n"
					+ " FROM  LEDGER_HT_HIST L,(SELECT UNIQUE MSCNO,CASE WHEN MDCLKWHSTAT_HT='3' THEN 'UD' ELSE 'LIV' END STAT FROM MTRDAT_HIST WHERE TO_CHAR(MDMONTH,'MON-YYYY')=?) M1,\r\n"
					+ "(SELECT MSCNO FROM MTRDAT_HIST WHERE to_char(MDMONTH,'MON-YYYY') =to_char(add_months(trunc(to_date(?,'MON-YYYY'), 'MM'), -1), 'MON-YYYY') MINUS SELECT MSCNO FROM MTRDAT_HIST WHERE TO_CHAR(MDMONTH,'MON-YYYY')=?) M2,\r\n"
					+ "(SELECT USCNO FROM LEDGER_HT_HIST  WHERE MON_YEAR=to_char(add_months(trunc(to_date(?,'MON-YYYY'), 'MM'), -1), 'MON-YYYY') AND USCNO IN(SELECT MSCNO FROM MTRDAT_HIST WHERE  TO_CHAR(MDMONTH,'MON-YYYY')=? MINUS \r\n"
					+ "SELECT MSCNO FROM MTRDAT_HIST WHERE to_char(MDMONTH,'MON-YYYY') =to_char(add_months(trunc(to_date(?,'MON-YYYY'), 'MM'), -1), 'MON-YYYY')) ) M3 WHERE L.USCNO=M3.USCNO(+) AND  L.USCNO=M1.MSCNO(+) AND L.USCNO=M2.MSCNO(+) AND MON_YEAR=?) L1,\r\n"
					+ " (select uscno,TRUNC(RJDT,'MM') DT,SUM(CASE WHEN RJTYPE='DR' THEN (nvl(ENGCHG,0)+nvl(THEFT,0)+nvl(OTHCHG,0)+nvl(CUSTCHG,0)+nvl(DEMCHG,0)+nvl(ED,0)+nvl(LPC,0)+nvl(EDI,0)+nvl(FSA,0)+NVL(CC_LPC,0)+NVL(CC_OTH,0)) END) DR_AMT,\r\n"
					+ " SUM(CASE WHEN RJTYPE='CR'THEN (nvl(ENGCHG,0)+nvl(THEFT,0)+nvl(OTHCHG,0)+nvl(CUSTCHG,0)+nvl(DEMCHG,0)+nvl(ED,0)+nvl(LPC,0)+nvl(EDI,0)+nvl(FSA,0)+NVL(CC_LPC,0)+NVL(CC_OTH,0)) END) CR_AMT  from \r\n"
					+ " JOURNAL_HIST WHERE rjdt='01-'||? and SUBSTR(USCNO,1,3) IN('GNT','VJA','ONG','CRD') AND TRIM(STATUS) NOT IN ('X') GROUP BY USCNO,TRUNC(RJDT,'MM') ) R1\r\n"
					+ "WHERE CTUSCNO=L1.USCNO AND L1.USCNO=R1.USCNO(+)  GROUP BY SUBSTR(CTUSCNO,1,3) ORDER BY "
					+ "case when CIRCLE = 'VJA' then '001' when CIRCLE = 'GNT' then '002' when CIRCLE = 'ONG' then '003'  when CIRCLE = 'CRD' then '009' else CIRCLE end";
			log.info(circleWisedemandsql);
			return jdbcTemplate.queryForList(circleWisedemandsql, new Object[] { fmonthYear, fmonthYear, fmonthYear,
					fmonthYear, fmonthYear, fmonthYear, fmonthYear, fmonthYear });
		} catch (DataAccessException e) {
			log.error(e.getMessage());
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	public List<Map<String, Object>> getCircleWiseCBAndDemand(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		String monthYear = request.getParameter("month") + "-" + request.getParameter("year");
		try {
			if (circle.equals("ALL")) {
				String circleWisedemandsql = "SELECT CTUSCNO,CTNAME,CIRNAME,DIVNAME,SUBNAME,SECNAME,DECODE(CTGOVT_PVT,'Y','GOVT','NON-GOVT') TYPE,DECODE(TRIM(CTSTATUS),'1','LIVE','0','STOP','') CTSTATUS,\r\n"
						+ "NVL(OB,0) TOB,NVL(DEM,0)+NVL(DR_AMT,0) TDEM,\r\n" + "NVL(COLL,0)+NVL(CR_AMT,0) TCOLL, \r\n"
						+ "(NVL(CB,0)) CB,\r\n"
						+ "(CASE WHEN NVL(CB,0)>(NVL(DEM,0)+NVL(DR_AMT,0)) THEN (NVL(DEM,0)+NVL(DR_AMT,0)) ELSE CASE WHEN NVL(CB,0)>0 THEN NVL(CB,0) ELSE 0 END  END) DEMAND_PART,\r\n"
						+ "(CASE WHEN NVL(CB,0)>(NVL(DEM,0)+NVL(DR_AMT,0)) THEN NVL(CB,0)-(NVL(DEM,0)+NVL(DR_AMT,0))  ELSE 0 END) ARREAR_PART\r\n"
						+ " FROM CONS,SPDCLMASTER,\r\n"
						+ "(SELECT USCNO,SUM(NVL(OB_OTH,0) +NVL(OB_CCLPC,0)+NVL(TOT_OB,0)) OB,SUM(NVL(CMD,0)+NVL(CCLPC,0)) Dem,SUM(NVL(TOT_PAY,0)) COLL,SUM(NVL(CBTOT,0)+NVL(CB_CCLPC,0)+NVL(CB_OTH,0)) CB FROM LEDGER_HT_HIST WHERE MON_YEAR=? AND SUBSTR(USCNO,1,3) IN('GNT','VJA','ONG','CRD') GROUP BY USCNO) L,\r\n"
						+ "(select uscno,SUM(CASE WHEN RJTYPE='DR' THEN (nvl(ENGCHG,0)+nvl(THEFT,0)+nvl(OTHCHG,0)+nvl(CUSTCHG,0)+nvl(DEMCHG,0)+nvl(ED,0)+nvl(LPC,0)+nvl(EDI,0)+nvl(FSA,0)+NVL(CC_LPC,0)+NVL(CC_OTH,0)) END) DR_AMT,SUM(CASE WHEN RJTYPE='CR'THEN (nvl(ENGCHG,0)+nvl(THEFT,0)+nvl(OTHCHG,0)+nvl(CUSTCHG,0)+nvl(DEMCHG,0)+nvl(ED,0)+nvl(LPC,0)+nvl(EDI,0)+nvl(FSA,0)+NVL(CC_LPC,0)+NVL(CC_OTH,0)) END) CR_AMT  from JOURNAL_HIST WHERE to_char(TRUNC(rjdt,'MM'),'MON-YYYY')=? and SUBSTR(USCNO,1,3) IN('GNT','VJA','ONG','CRD') AND TRIM(STATUS) NOT IN ('X') GROUP BY USCNO) RJ\r\n"
						+ "WHERE CTUSCNO=L.USCNO(+) AND CTUSCNO=RJ.USCNO(+) \r\n" + " and substr(ctseccd,-5)=SECCD "
						+ "ORDER BY CTUSCNO";
				log.info(circleWisedemandsql);
				return jdbcTemplate.queryForList(circleWisedemandsql, new Object[] { monthYear, monthYear });
			} else {
				String circleWisedemandsql = "SELECT CTUSCNO,CTNAME,CIRNAME,DIVNAME,SUBNAME,SECNAME,DECODE(CTGOVT_PVT,'Y','GOVT','NON-GOVT') TYPE,DECODE(TRIM(CTSTATUS),'1','LIVE','0','STOP','') CTSTATUS, \r\n"
						+ "NVL(OB,0) TOB,NVL(DEM,0)+NVL(DR_AMT,0) TDEM,\r\n" + "NVL(COLL,0)+NVL(CR_AMT,0) TCOLL, \r\n"
						+ "(NVL(CB,0)) CB,\r\n"
						+ "(CASE WHEN NVL(CB,0)>(NVL(DEM,0)+NVL(DR_AMT,0)) THEN (NVL(DEM,0)+NVL(DR_AMT,0)) ELSE CASE WHEN NVL(CB,0)>0 THEN NVL(CB,0) ELSE 0 END  END) DEMAND_PART,\r\n"
						+ "(CASE WHEN NVL(CB,0)>(NVL(DEM,0)+NVL(DR_AMT,0)) THEN NVL(CB,0)-(NVL(DEM,0)+NVL(DR_AMT,0))  ELSE 0 END) ARREAR_PART\r\n"
						+ " FROM CONS,SPDCLMASTER,\r\n"
						+ "(SELECT USCNO,SUM(NVL(OB_OTH,0) +NVL(OB_CCLPC,0)+NVL(TOT_OB,0)) OB,SUM(NVL(CMD,0)+NVL(CCLPC,0)) Dem,SUM(NVL(TOT_PAY,0)) COLL,SUM(NVL(CBTOT,0)+NVL(CB_CCLPC,0)+NVL(CB_OTH,0)) CB FROM LEDGER_HT_HIST WHERE MON_YEAR=? AND SUBSTR(USCNO,1,3) IN('GNT','VJA','ONG','CRD') GROUP BY USCNO) L,\r\n"
						+ "(select uscno,SUM(CASE WHEN RJTYPE='DR' THEN (nvl(ENGCHG,0)+nvl(THEFT,0)+nvl(OTHCHG,0)+nvl(CUSTCHG,0)+nvl(DEMCHG,0)+nvl(ED,0)+nvl(LPC,0)+nvl(EDI,0)+nvl(FSA,0)+NVL(CC_LPC,0)+NVL(CC_OTH,0)) END) DR_AMT,SUM(CASE WHEN RJTYPE='CR'THEN (nvl(ENGCHG,0)+nvl(THEFT,0)+nvl(OTHCHG,0)+nvl(CUSTCHG,0)+nvl(DEMCHG,0)+nvl(ED,0)+nvl(LPC,0)+nvl(EDI,0)+nvl(FSA,0)+NVL(CC_LPC,0)+NVL(CC_OTH,0)) END) CR_AMT  from JOURNAL_HIST WHERE to_char(TRUNC(rjdt,'MM'),'MON-YYYY')=? and SUBSTR(USCNO,1,3) IN('GNT','VJA','ONG','CRD') AND TRIM(STATUS) NOT IN ('X') GROUP BY USCNO) RJ\r\n"
						+ "WHERE CTUSCNO=L.USCNO(+) AND CTUSCNO=RJ.USCNO(+)  AND SUBSTR(CTUSCNO,1,3) = ?\r\n "
						+ " and substr(ctseccd,-5)=SECCD " + "ORDER BY CTUSCNO";
				log.info(circleWisedemandsql);
				return jdbcTemplate.queryForList(circleWisedemandsql, new Object[] { monthYear, monthYear, circle });
			}
		} catch (DataAccessException e) {
			log.error(e.getMessage());
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	public List<Map<String, Object>> getCircleWiseAbstract(HttpServletRequest request) {
		String monthYear = request.getParameter("month") + "-" + request.getParameter("year");
		try {
			String circleAbstractsql = "SELECT SUBSTR(BTSCNO,1,3)CIRCLE,SUM(NVL(BTCURDEM,0))DEMAND,SUM(NVL(BTCURDEM,0))+SUM(NVL(BTCOURT_LPC,0)) WC_DEMAND FROM BILL\r\n"
					+ "WHERE  TO_CHAR(BTBLDT,'MON-YYYY')=? and SUBSTR(BTSCNO,1,3) in ('GNT','VJA','ONG','CRD')  \r\n"
					+ "GROUP BY SUBSTR(BTSCNO,1,3)\r\n" + "UNION ALL\r\n"
					+ "SELECT SUBSTR(BTSCNO,1,3)CIRCLE,SUM(NVL(BTCURDEM,0))DEMAND,SUM(NVL(BTCURDEM,0))+SUM(NVL(BTCOURT_LPC,0)) WC_DEMAND FROM BILL_HIST\r\n"
					+ "WHERE  TO_CHAR(BTBLDT,'MON-YYYY')=?  and SUBSTR(BTSCNO,1,3) in ('GNT','VJA','ONG','CRD') \r\n"
					+ "GROUP BY SUBSTR(BTSCNO,1,3)\r\n" + "ORDER BY CIRCLE";
			log.info(circleAbstractsql);
			return jdbcTemplate.queryForList(circleAbstractsql, new Object[] { monthYear, monthYear });
		} catch (DataAccessException e) {
			log.error(e.getMessage());
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	public List<Map<String, Object>> getSectionWiseArrearsDetails(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		String monthYear = request.getParameter("month") + "-" + request.getParameter("year");
		if (circle.equals("ALL")) {
			try {
				String sectionSql = "SELECT NAM,CIRCLE,DIVISION,SUBDIVISION,SECTION,CAT,SCAT,CB_SD SD,REC_KVAH,MN_KVAH,USCNO,NVL(NVL(TOT_OB,0)+NVL(OB_OTH,0)+NVL(OB_CCLPC,0),0) OP_BALANCE,CMD DEMAND,TOT_PAY COLLECTION\r\n"
						+ ",DRJ-CRJ JOURNAL,(NVL(CBTOT,0)+NVL(CB_OTH,0)+NVL(CB_CCLPC,0)) CL_BALANCE,MON_YEAR\r\n"
						+ "FROM LEDGER_HT_HIST WHERE MON_YEAR=?  \r\n" + "UNION ALL\r\n"
						+ "SELECT NAM,CIRCLE,DIVISION,SUBDIVISION,SECTION,CAT,SCAT,CB_SD SD,REC_KVAH,MN_KVAH,USCNO,NVL(NVL(TOT_OB,0)+NVL(OB_OTH,0)+NVL(OB_CCLPC,0),0) OP_BALANCE,CMD DEMAND,TOT_PAY COLLECTION\r\n"
						+ ",DRJ-CRJ JOURNAL,(NVL(CBTOT,0)+NVL(CB_OTH,0)+NVL(CB_CCLPC,0)) CL_BALANCE,MON_YEAR\r\n"
						+ "FROM LEDGER_HT WHERE MON_YEAR=?   ORDER BY SECTION ";
				log.info(sectionSql);
				return jdbcTemplate.queryForList(sectionSql, new Object[] { monthYear, monthYear });
			} catch (DataAccessException e) {
				log.error(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}
		} else {
			try {
				String sectionSql = "SELECT NAM,CIRCLE,DIVISION,SUBDIVISION,SECTION,CAT,SCAT,CB_SD SD,REC_KVAH,MN_KVAH,USCNO,NVL(NVL(TOT_OB,0)+NVL(OB_OTH,0)+NVL(OB_CCLPC,0),0) OP_BALANCE,CMD DEMAND,TOT_PAY COLLECTION\r\n"
						+ ",DRJ-CRJ JOURNAL,(NVL(CBTOT,0)+NVL(CB_OTH,0)+NVL(CB_CCLPC,0)) CL_BALANCE,MON_YEAR\r\n"
						+ "FROM LEDGER_HT_HIST WHERE MON_YEAR=? AND  SUBSTR(USCNO,1,3)=? \r\n" + "UNION ALL\r\n"
						+ "SELECT NAM,CIRCLE,DIVISION,SUBDIVISION,SECTION,CAT,SCAT,CB_SD SD,REC_KVAH,MN_KVAH,USCNO,NVL(NVL(TOT_OB,0)+NVL(OB_OTH,0)+NVL(OB_CCLPC,0),0) OP_BALANCE,CMD DEMAND,TOT_PAY COLLECTION\r\n"
						+ ",DRJ-CRJ JOURNAL,(NVL(CBTOT,0)+NVL(CB_OTH,0)+NVL(CB_CCLPC,0)) CL_BALANCE,MON_YEAR\r\n"
						+ "FROM LEDGER_HT WHERE MON_YEAR=? AND SUBSTR(USCNO,1,3)=?  ORDER BY SECTION ";
				log.info(sectionSql);
				return jdbcTemplate.queryForList(sectionSql, new Object[] { monthYear, circle, monthYear, circle });
			} catch (DataAccessException e) {
				log.error(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}
		}
	}

	public List<Map<String, Object>> getLiveAndBillDetails(HttpServletRequest request) {
		String monthYear = request.getParameter("month") + "-" + request.getParameter("year");
		try {
			String liveandbillsql = "SELECT CIRCD,MTR,BILL FROM \r\n"
					+ "(SELECT COUNT(mscno)MTR,SUBSTR(MSCNO,0,3)CIRCD FROM (select distinct mscno mscno from mtrdat  WHERE TO_CHAR(MDCLRDG_DT,'MON-YYYY')=?) GROUP BY SUBSTR(MSCNO,0,3) ORDER BY SUBSTR(MSCNO,0,3)),\r\n"
					+ "(SELECT COUNT(BTSCNO)BILL,SUBSTR(BTSCNO,0,3)SCNO FROM (SELECT BTSCNO FROM BILL WHERE TO_CHAR(BTBLDT,'MON-YYYY')=?) GROUP BY SUBSTR(BTSCNO,0,3) ORDER BY SUBSTR(BTSCNO,0,3))\r\n"
					+ "WHERE CIRCD=SCNO UNION ALL\r\n" + "SELECT CIRCD,MTR,BILL FROM \r\n"
					+ "(SELECT COUNT(mscno)MTR,SUBSTR(MSCNO,0,3)CIRCD FROM (select distinct mscno mscno from mtrdat_hist WHERE TO_CHAR(MDCLRDG_DT,'MON-YYYY')=?) GROUP BY SUBSTR(MSCNO,0,3) ORDER BY SUBSTR(MSCNO,0,3)),\r\n"
					+ "(SELECT COUNT(BTSCNO)BILL,SUBSTR(BTSCNO,0,3)SCNO FROM (SELECT BTSCNO FROM BILL_hist WHERE TO_CHAR(BTBLDT,'MON-YYYY')=?) GROUP BY SUBSTR(BTSCNO,0,3) ORDER BY SUBSTR(BTSCNO,0,3))\r\n"
					+ "WHERE CIRCD=SCNO \r\n" + "";
			log.info(liveandbillsql);
			return jdbcTemplate.queryForList(liveandbillsql,
					new Object[] { monthYear, monthYear, monthYear, monthYear });
		} catch (DataAccessException e) {
			log.error(e.getMessage());
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	public List<Map<String, Object>> getSolarEnergyReport(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		String monthYear = request.getParameter("month") + "-" + request.getParameter("year");
		if (circle.equals("ALL")) {
			try {
				String sersql = "SELECT CTUSCNO,CTNAME, CTCAT,CTSUBCAT,BTRKVAH_HT,BTBLSOLAR_HT,BTBKVAH ,PLANT_CAP, UNIT_OF_MEASURE    FROM BILL,CONS,Solar_plant_capacity "
						+ "WHERE BTSCNO=CTUSCNO  AND CTUSCNO=USCNO(+)  AND TRIM(CTSOLAR_FLAG)='Y' AND TO_CHAR(BTBLDT,'MON-YYYY')=?"
						+ "UNION ALL "
						+ "SELECT CTUSCNO,CTNAME,CTCAT,CTSUBCAT, BTRKVAH_HT,BTBLSOLAR_HT,BTBKVAH ,PLANT_CAP, UNIT_OF_MEASURE   FROM BILL_HIST,CONS,Solar_plant_capacity "
						+ "WHERE BTSCNO=CTUSCNO  AND CTUSCNO=USCNO(+)  AND TRIM(CTSOLAR_FLAG)='Y' AND TO_CHAR(BTBLDT,'MON-YYYY')=?"
						+ "ORDER BY CTUSCNO";
				log.info(sersql);
				return jdbcTemplate.queryForList(sersql, new Object[] { monthYear, monthYear });
			} catch (DataAccessException e) {
				log.error(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}
		} else {
			try {
				String sersql = "SELECT CTUSCNO,CTNAME, CTCAT,CTSUBCAT,BTRKVAH_HT,BTBLSOLAR_HT,BTBKVAH,PLANT_CAP, UNIT_OF_MEASURE   FROM BILL,CONS ,Solar_plant_capacity "
						+ "WHERE BTSCNO=CTUSCNO  AND CTUSCNO=USCNO(+)  AND TRIM(CTSOLAR_FLAG)='Y' AND TO_CHAR(BTBLDT,'MON-YYYY')=? AND SUBSTR(CTUSCNO,1,3)=? "
						+ "UNION ALL "
						+ "SELECT CTUSCNO,CTNAME, CTCAT,CTSUBCAT,BTRKVAH_HT,BTBLSOLAR_HT,BTBKVAH,PLANT_CAP, UNIT_OF_MEASURE   PLANT_CAPACITY  FROM BILL_HIST,CONS,Solar_plant_capacity "
						+ "WHERE BTSCNO=CTUSCNO  AND CTUSCNO=USCNO(+)  AND TRIM(CTSOLAR_FLAG)='Y' AND TO_CHAR(BTBLDT,'MON-YYYY')=? AND SUBSTR(CTUSCNO,1,3)=? ORDER BY CTUSCNO";
				log.info(sersql);
				return jdbcTemplate.queryForList(sersql, new Object[] { monthYear, circle, monthYear, circle });
			} catch (DataAccessException e) {
				log.error(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();

			}
		}
	}

	public List<Map<String, Object>> getAquaDetails(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		String monthYear = request.getParameter("month") + "-" + request.getParameter("year");
		if (circle.equals("ALL")) {

			try {
				String sql = "SELECT SUBSTR(CTUSCNO,1,3)CIRCLE, CTUSCNO,CTNAME,CTCMD_HT,CTCAT,CTACTUAL_KV, BTBKVAH,BTENGCHG_NOR, BTENGCHG_NOR+BTAQUASUB_CHG BTAQUAENG_CHG,BTAQUASUB_CHG,MTSTATUS CTSTATUS \r\n"
						+ "FROM BILL,CONS ,(select mscno,max(MDCLKWHSTAT_HT) MDCLKWHSTAT_HT from mtrdat  where to_char(mdmonth,'MON-YYYY')=?  group by mscno),METER_STATUS\r\n"
						+ "WHERE BTSCNO=CTUSCNO\r\n" + "AND CTUSCNO=mscno\r\n"
						+ "AND MDCLKWHSTAT_HT=to_number(MTCODE)\r\n" + "AND TRIM(CTAQUA_FLAG)='Y'\r\n"
						+ "AND TO_CHAR(BTBLDT,'MON-YYYY')=?\r\n" + "UNION ALL\r\n"
						+ "SELECT SUBSTR(CTUSCNO,1,3)CIRCLE, CTUSCNO,CTNAME,CTCMD_HT,CTCAT,CTACTUAL_KV, BTBKVAH,BTENGCHG_NOR, BTENGCHG_NOR+BTAQUASUB_CHG BTAQUAENG_CHG, BTAQUASUB_CHG,MTSTATUS CTSTATUS\r\n"
						+ "FROM BILL_HIST,CONS ,(select mscno,max(MDCLKWHSTAT_HT) MDCLKWHSTAT_HT from mtrdat_hist  where to_char(mdmonth,'MON-YYYY')=?  group by mscno),METER_STATUS\r\n"
						+ "WHERE BTSCNO=CTUSCNO\r\n" + "AND CTUSCNO=mscno\r\n"
						+ "AND MDCLKWHSTAT_HT=to_number(MTCODE)\r\n" + "AND TRIM(CTAQUA_FLAG)='Y'\r\n"
						+ "AND TO_CHAR(BTBLDT,'MON-YYYY')=? ORDER BY CTUSCNO";
				log.info(sql);
				return jdbcTemplate.queryForList(sql, new Object[] { monthYear, monthYear, monthYear, monthYear });
			} catch (DataAccessException e) {
				log.error(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}

		} else {
			try {
				String sql = "SELECT SUBSTR(CTUSCNO,1,3)CIRCLE, CTUSCNO,CTNAME,CTCMD_HT,CTCAT,CTACTUAL_KV, BTBKVAH,BTENGCHG_NOR, BTENGCHG_NOR+BTAQUASUB_CHG BTAQUAENG_CHG,BTAQUASUB_CHG,MTSTATUS CTSTATUS \r\n"
						+ "FROM BILL,CONS ,(select mscno,max(MDCLKWHSTAT_HT) MDCLKWHSTAT_HT from mtrdat  where to_char(mdmonth,'MON-YYYY')=?  group by mscno),METER_STATUS\r\n"
						+ "WHERE BTSCNO=CTUSCNO\r\n" + "AND CTUSCNO=mscno\r\n"
						+ "AND MDCLKWHSTAT_HT=to_number(MTCODE)\r\n" + "AND TRIM(CTAQUA_FLAG)='Y'\r\n"
						+ "AND TO_CHAR(BTBLDT,'MON-YYYY')=? AND SUBSTR(CTUSCNO,1,3)=?\r\n" + "" + "UNION ALL\r\n"
						+ "SELECT SUBSTR(CTUSCNO,1,3)CIRCLE, CTUSCNO,CTNAME,CTCMD_HT,CTCAT,CTACTUAL_KV, BTBKVAH,BTENGCHG_NOR, BTENGCHG_NOR+BTAQUASUB_CHG BTAQUAENG_CHG, BTAQUASUB_CHG,MTSTATUS CTSTATUS\r\n"
						+ "FROM BILL_HIST,CONS ,(select mscno,max(MDCLKWHSTAT_HT) MDCLKWHSTAT_HT from mtrdat_hist  where to_char(mdmonth,'MON-YYYY')=?  group by mscno),METER_STATUS\r\n"
						+ "WHERE BTSCNO=CTUSCNO\r\n" + "AND CTUSCNO=mscno\r\n"
						+ "AND MDCLKWHSTAT_HT=to_number(MTCODE)\r\n" + "AND TRIM(CTAQUA_FLAG)='Y'\r\n"
						+ "AND TO_CHAR(BTBLDT,'MON-YYYY')=? AND SUBSTR(CTUSCNO,1,3)=? ORDER BY CTUSCNO";
				/*
				 * String sql =
				 * "SELECT SUBSTR(CTUSCNO,1,3)CIRCLE, CTUSCNO,CTNAME,CTCMD_HT,CTCAT,CTACTUAL_KV, BTBKVAH,BTENGCHG_NOR,BTENGCHG_NOR+BTAQUASUB_CHG BTAQUAENG_CHG,BTAQUASUB_CHG,DECODE(TRIM(CTSTATUS),'1','LIVE','0','STOP','') CTSTATUS FROM BILL,CONS \r\n"
				 * + "WHERE BTSCNO=CTUSCNO\r\n" + "AND TRIM(CTAQUA_FLAG)='Y'\r\n" +
				 * "AND TO_CHAR(BTBLDT,'MON-YYYY')= ? AND SUBSTR(CTUSCNO,1,3)=? \r\n" +
				 * "UNION ALL\r\n" +
				 * "SELECT SUBSTR(CTUSCNO,1,3)CIRCLE, CTUSCNO,CTNAME,CTCMD_HT,CTCAT,CTACTUAL_KV, BTBKVAH,BTENGCHG_NOR, BTENGCHG_NOR+BTAQUASUB_CHG BTAQUAENG_CHG, BTAQUASUB_CHG,DECODE(TRIM(CTSTATUS),'1','LIVE','0','STOP','') CTSTATUS FROM BILL_HIST,CONS \r\n"
				 * + "WHERE BTSCNO=CTUSCNO\r\n" + "AND TRIM(CTAQUA_FLAG)='Y'\r\n" +
				 * "AND TO_CHAR(BTBLDT,'MON-YYYY')=?  AND SUBSTR(CTUSCNO,1,3)=? ";
				 */
				log.info(sql);
				return jdbcTemplate.queryForList(sql,
						new Object[] { monthYear, monthYear, circle, monthYear, monthYear, circle });
			} catch (DataAccessException e) {
				log.error(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}
		}
	}

	public List<Map<String, Object>> getLFIDetails(HttpServletRequest request) {

		String circle = request.getParameter("circle");
		String monthYear = request.getParameter("month") + "-" + request.getParameter("year");
		if (circle.equals("ALL")) {
			try {
				String sql = "SELECT BTSCNO,CTNAME,BTLFINCENTIVE_HT,BTLDFACTOR_HT FROM BILL_HIST,CONS \r\n"
						+ "WHERE BTLDFACTOR_HT>50 \r\n" + "AND BTSCNO=CTUSCNO\r\n" + "AND TO_CHAR(BTBLDT,'MON-YYYY')=?"
						+ "UNION ALL\r\n" + "SELECT BTSCNO,CTNAME,BTLFINCENTIVE_HT,BTLDFACTOR_HT FROM BILL,CONS \r\n"
						+ "WHERE BTLDFACTOR_HT>50 \r\n" + "AND BTSCNO=CTUSCNO\r\n" + "AND TO_CHAR(BTBLDT,'MON-YYYY')=?";
				log.info(sql);
				return jdbcTemplate.queryForList(sql, new Object[] { monthYear, monthYear });
			} catch (DataAccessException e) {
				log.error(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}
		} else {
			try {
				String sql = "SELECT BTSCNO,CTNAME,BTLFINCENTIVE_HT,BTLDFACTOR_HT FROM BILL_HIST,CONS \r\n"
						+ "WHERE BTLDFACTOR_HT>50 \r\n" + "AND BTSCNO=CTUSCNO\r\n"
						+ "AND TO_CHAR(BTBLDT,'MON-YYYY')=? AND SUBSTR(BTSCNO,0,3)= ? " + "UNION ALL\r\n"
						+ "SELECT BTSCNO,CTNAME,BTLFINCENTIVE_HT,BTLDFACTOR_HT FROM BILL,CONS \r\n"
						+ "WHERE BTLDFACTOR_HT>50 \r\n" + "AND BTSCNO=CTUSCNO\r\n"
						+ "AND TO_CHAR(BTBLDT,'MON-YYYY')=? AND SUBSTR(BTSCNO,0,3)= ? ";
				log.info(sql);
				return jdbcTemplate.queryForList(sql, new Object[] { monthYear, circle, monthYear, circle });
			} catch (DataAccessException e) {
				log.error(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}

		}
	}

	public List<Map<String, Object>> getCWCESLDetails(HttpServletRequest request) {

		String circle = request.getParameter("circle");
		if (circle.equals("ALL")) {
			try {
				String sql = "SELECT C.CIRCLE CIRCLE,D.CTCAT CTCAT,NVL(A.COUNT_USCNO,0)LIVE,NVL(A.CTCMD_HT,0) LIVE_CMD,NVL(B.COUNT_USCNO,0) BILLSTOP,NVL(B.CTCMD_HT,0) BILLSTOP_CMD,NVL(A.COUNT_USCNO,0)+NVL(B.COUNT_USCNO,0) TOTAL,NVL(A.CTCMD_HT,0)+NVL(B.CTCMD_HT,0) TOTAL_CMD FROM\r\n"
						+ "(SELECT COUNT(CTUSCNO)COUNT_USCNO,SUBSTR(CTUSCNO,0,3)CIRCLE,CTCAT,SUM(CTCMD_HT)CTCMD_HT FROM CONS WHERE CTSTATUS='1' AND TRIM(CTUSCNO) NOT IN ('ATPCEN','KNLCEN','ATPNEW','KNLNEW') AND TRIM(CTUSCNO) IS NOT NULL AND TRIM(CTUSCNO) NOT IN ('TPT2323789','VJA99999')  GROUP BY SUBSTR(CTUSCNO,0,3),CTCAT ORDER BY SUBSTR(CTUSCNO,0,3))A,\r\n"
						+ "(SELECT COUNT(CTUSCNO)COUNT_USCNO,SUBSTR(CTUSCNO,0,3)CIRCLE,CTCAT,SUM(CTCMD_HT)CTCMD_HT FROM CONS WHERE CTSTATUS='0' AND TRIM(CTUSCNO) NOT IN ('ATPCEN','KNLCEN','ATPNEW','KNLNEW') AND TRIM(CTUSCNO) IS NOT NULL AND TRIM(CTUSCNO) NOT IN ('TPT2323789','VJA99999')  GROUP BY SUBSTR(CTUSCNO,0,3),CTCAT ORDER BY SUBSTR(CTUSCNO,0,3))B,\r\n"
						+ "(SELECT DISTINCT SUBSTR(CTUSCNO,0,3)CIRCLE FROM CONS)C,\r\n"
						+ "(SELECT DISTINCT CTCAT CTCAT FROM CONS)D\r\n"
						+ "WHERE A.CIRCLE(+)=C.CIRCLE AND  B.CIRCLE(+)=C.CIRCLE AND A.CTCAT(+)=D.CTCAT AND B.CTCAT(+)=D.CTCAT  ORDER BY D.CTCAT\r\n"
						+ "";
				log.info(sql);
				return jdbcTemplate.queryForList(sql);
			} catch (DataAccessException e) {
				log.error(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}
		} else {

			try {
				String sql = "SELECT C.CIRCLE CIRCLE,D.CTCAT CTCAT,NVL(A.COUNT_USCNO,0)LIVE,NVL(A.CTCMD_HT,0) LIVE_CMD,NVL(B.COUNT_USCNO,0) BILLSTOP,NVL(B.CTCMD_HT,0) BILLSTOP_CMD,NVL(A.COUNT_USCNO,0)+NVL(B.COUNT_USCNO,0) TOTAL,NVL(A.CTCMD_HT,0)+NVL(B.CTCMD_HT,0) TOTAL_CMD FROM\r\n"
						+ "(SELECT COUNT(CTUSCNO)COUNT_USCNO,SUBSTR(CTUSCNO,0,3)CIRCLE,CTCAT,SUM(CTCMD_HT)CTCMD_HT FROM CONS WHERE CTSTATUS='1' AND TRIM(CTUSCNO) NOT IN ('ATPCEN','KNLCEN','ATPNEW','KNLNEW') AND TRIM(CTUSCNO) IS NOT NULL AND TRIM(CTUSCNO) NOT IN ('TPT2323789','VJA99999') GROUP BY SUBSTR(CTUSCNO,0,3),CTCAT ORDER BY SUBSTR(CTUSCNO,0,3))A,\r\n"
						+ "(SELECT COUNT(CTUSCNO)COUNT_USCNO,SUBSTR(CTUSCNO,0,3)CIRCLE,CTCAT,SUM(CTCMD_HT)CTCMD_HT FROM CONS WHERE CTSTATUS='0' AND TRIM(CTUSCNO) NOT IN ('ATPCEN','KNLCEN','ATPNEW','KNLNEW') AND TRIM(CTUSCNO) IS NOT NULL AND TRIM(CTUSCNO) NOT IN ('TPT2323789','VJA99999') GROUP BY SUBSTR(CTUSCNO,0,3),CTCAT ORDER BY SUBSTR(CTUSCNO,0,3))B,\r\n"
						+ "(SELECT DISTINCT SUBSTR(CTUSCNO,0,3)CIRCLE FROM CONS)C,\r\n"
						+ "(SELECT DISTINCT CTCAT CTCAT FROM CONS)D\r\n"
						+ "WHERE A.CIRCLE(+)=C.CIRCLE AND  B.CIRCLE(+)=C.CIRCLE AND A.CTCAT(+)=D.CTCAT AND B.CTCAT(+)=D.CTCAT AND C.CIRCLE= ? ORDER BY D.CTCAT\r\n"
						+ "";
				log.info(sql);
				return jdbcTemplate.queryForList(sql, new Object[] { circle });
			} catch (DataAccessException e) {
				log.error(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}

		}
	}

	public List<Map<String, Object>> getTCSExemptionDetails(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		String flag = request.getParameter("flag");
		if (circle.equals("ALL")) {

			try {
				String sql = "SELECT CONS.CTUSCNO,DIVNAME,SUBNAME,CONS.CTNAME,CONS.CTCAT,CONS.CTSUBCAT,CONS.CTCMD_HT,TO_CHAR(CONS.CTSUPCONDT,'DD-MM-YYYY')CTSUPCONDT,\r\n"
						+ "DECODE(TRIM(CONS.CTSTATUS),'1','LIVE','0','STOP','') CTSTATUS,CONS.CTACTUAL_KV,STDESC,\r\n"
						+ "case when NVL(CTTCSFLAG_HT,'Y')='N' then 'TCS Should Not Be Levied' else 'TCS Should Be Levied' end TCS_FLAG\r\n"
						+ "FROM CONS,SPDCLMASTER,servtype  WHERE SECCD=SUBSTR(CONS.CTSECCD,-5)  AND CONS.CTSERVTYPE=STCODE and NVL(CTTCSFLAG_HT,'Y')=?";
				log.info(sql);
				return jdbcTemplate.queryForList(sql, new Object[] { flag });
			} catch (DataAccessException e) {
				log.error(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}
		} else {
			try {
				String sql = "SELECT CONS.CTUSCNO,DIVNAME,SUBNAME,CONS.CTNAME,CONS.CTCAT,CONS.CTSUBCAT,CONS.CTCMD_HT,TO_CHAR(CONS.CTSUPCONDT,'DD-MM-YYYY')CTSUPCONDT,\r\n"
						+ "DECODE(TRIM(CONS.CTSTATUS),'1','LIVE','0','STOP','') CTSTATUS,CONS.CTACTUAL_KV,STDESC,\r\n"
						+ "case when NVL(CTTCSFLAG_HT,'Y')='N' then 'TCS Should Not Be Levied' else 'TCS Should Be Levied' end TCS_FLAG\r\n"
						+ "FROM CONS,SPDCLMASTER,servtype  WHERE SECCD=SUBSTR(CONS.CTSECCD,-5)  AND CONS.CTSERVTYPE=STCODE and NVL(CTTCSFLAG_HT,'Y')=? AND SUBSTR(CONS.CTUSCNO,1,3)=? ";
				log.info(sql);
				return jdbcTemplate.queryForList(sql, new Object[] { flag, circle });
			} catch (DataAccessException e) {
				log.error(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}
		}

	}

	public List<Map<String, Object>> getConsumerMasterDetails(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		if (circle.equals("ALL")) {
			try {
				/*
				 * String sql =
				 * "SELECT CTUSCNO,SUBNAME,CTFEEDER_CODE,CTNAME,CTCAT,CTSUBCAT,CTCMD_HT,TO_CHAR(CTSUPCONDT,'DD-MM-YYYY')CTSUPCONDT,DECODE(TRIM(CTSTATUS),'1','LIVE','0','STOP','') CTSTATUS,CTACTUAL_KV,TO_CHAR( MDMF_HT)MDMF_HT\r\n"
				 * + "FROM CONS,SPDCLMASTER,MTRDAT\r\n" + "WHERE SECCD=SUBSTR(CTSECCD,-5)\r\n" +
				 * "AND CTUSCNO=MSCNO(+)\r\n" + "UNION ALL\r\n" +
				 * "SELECT CTUSCNO,'' SUBNAME,CTFEEDER_CODE,CTNAME,CTCAT,CTSUBCAT,CTCMD_HT,TO_CHAR(CTSUPCONDT,'DD-MM-YYYY')CTSUPCONDT,DECODE(TRIM(CTSTATUS),'1','LIVE','0','STOP','') CTSTATUS,CTACTUAL_KV,'' MDMF_HT\r\n"
				 * + "FROM CONS\r\n" +
				 * "WHERE  NVL(SUBSTR(CTSECCD,-5),'0') NOT IN (SELECT SECCD FROM SPDCLMASTER)\r\n"
				 * + "ORDER BY 1\r\n" + "";
				 */

				String sql = " select A.*,B.METER_MAKE,"
						+ "(select NVL(CB_SD,'0') from ledger_ht_hist where to_date(MON_YEAR,'MON-YYYY') = (select max(to_date(MON_YEAR,'MON-YYYY')) from ledger_ht_hist where uscno = A.CTUSCNO ) and uscno = A.CTUSCNO ) SD,"
						+ "(select NVL(CBTOT,'0') from ledger_ht_hist where to_date(MON_YEAR,'MON-YYYY') = (select max(to_date(MON_YEAR,'MON-YYYY')) from ledger_ht_hist where uscno = A.CTUSCNO ) and uscno = A.CTUSCNO ) CB"
						+ " from ( SELECT CONS.CTUSCNO,SUBNAME,CONS.CTFEEDER_CODE,CONS.CTNAME,CONS.CTCAT,CONS.CTSUBCAT,CONS.CTCMD_HT,TO_CHAR(CONS.CTSUPCONDT,'DD-MM-YYYY')CTSUPCONDT,DECODE(TRIM(CONS.CTSTATUS),'1','LIVE','0','STOP','') CTSTATUS,CONS.CTACTUAL_KV,TO_CHAR(MDMF_HT)MDMF_HT,NVL(EXTENT_OF_LAND,0)EXTENT_OF_LAND,NVL(SURVEY_NUMBER,'-')SURVEY_NUMBER,NVL(LINKED_SCNO1,'-')LINKED_SCNO1, NVL(TYPE_OF_LAND,'-')TYPE_OF_LAND ,STDESC,CONS.CTADD1,CONS.CTADD2,CONS.CTADD3,CONS.CTADD4,CONS.CTMOBILE,CONS.CTEMAILID,NVL(MDMTRNO,'--')MDMTRNO ,"
						+ "DECODE(CONS.CTEDFLAG,'Y','YES','NO') CTEDFLAG,DECODE(CONS.CTTDS_FLAG,'Y','YES','NO')CTTDS_FLAG,DECODE(CONS.CTSOLAR_FLAG,'Y','YES','NO') CTSOLAR_FLAG,DECODE(CONS.CTSCSTFLAG,'Y','YES','NO')CTSCSTFLAG,DECODE(CONS.CTAQUA_FLAG,'Y','YES','NO') CTAQUA_FLAG,DECODE(CONS.CTCOLNYFLAG_HT,'Y','YES','NO')CTCOLNYFLAG_HT,DECODE(CONS.CTLF_FLAG,'Y','YES','NO')CTLF_FLAG,DECODE(CONS.CT_METERSIDE_FLAG,'1','YES','NO')CT_METERSIDE_FLAG,DECODE(CONS.CTSEASFLAG_HT,'Y','YES','NO')CTSEASFLAG_HT,DECODE(TRIM(CONS.CTGOVT_PVT),'Y','GOVT','N','PVT','') TYPE,NVL(CONS.CTHODDEP,'OTH')  deptcode,GMDEPTNAME dept_name,GMSUBDEPTNAME Hod_Name"
						+ " FROM CONS,MASTER.SPDCLMASTER,MTRDAT,CONS_FLAGS,servtype,"
						+ " (SELECT distinct GMTYPECODE,GMTYPE, GMDEPTCODE,GMDEPTNAME,GMSUBDEPTCODE, GMSUBDEPTNAME FROM deptmast group by GMTYPECODE,GMTYPE, GMDEPTCODE,GMDEPTNAME,GMSUBDEPTCODE, GMSUBDEPTNAME \r\n"
						+ "					union all\r\n"
						+ "					select 'OTH' GMTYPECODE,'OTH'GMTYPE, 'OTH' GMDEPTCODE,'OTH' GMDEPTNAME,'OTH' GMSUBDEPTCODE, 'OTH' GMSUBDEPTNAME from dual) "
						+ " WHERE SECCD=SUBSTR(CONS.CTSECCD,-5) AND  CONS.CTUSCNO=MSCNO(+) AND CONS.CTUSCNO=CONS_FLAGS.CTUSCNO AND CONS.CTSERVTYPE=STCODE(+) and "
						+ " CONS.CTHODTYPE=GMTYPECODE(+) and CONS.CTHODDEP=GMDEPTCODE(+) and CONS.CTHODSUBDEP=GMSUBDEPTCODE(+)"
						+
						/* + " AND MDCLKWHSTAT_HT NOT IN('04','14','4') "+ */
						" UNION ALL "
						+ " SELECT CONS.CTUSCNO,'' SUBNAME,CONS.CTFEEDER_CODE,CONS.CTNAME,CONS.CTCAT,CONS.CTSUBCAT,CONS.CTCMD_HT,TO_CHAR(CONS.CTSUPCONDT,'DD-MM-YYYY')CTSUPCONDT,DECODE(TRIM(CONS.CTSTATUS),'1','LIVE','0','STOP','') CTSTATUS,CONS.CTACTUAL_KV,'' MDMF_HT,NVL(EXTENT_OF_LAND,0)EXTENT_OF_LAND,NVL(SURVEY_NUMBER,'-')SURVEY_NUMBER,NVL(LINKED_SCNO1,'-')LINKED_SCNO1, NVL(TYPE_OF_LAND,'-')TYPE_OF_LAND ,STDESC,CONS.CTADD1,CONS.CTADD2,CONS.CTADD3,CONS.CTADD4,CONS.CTMOBILE,CONS.CTEMAILID,'0' ,"
						+ "DECODE(CONS.CTEDFLAG,'Y','YES','NO') CTEDFLAG,DECODE(CONS.CTTDS_FLAG,'Y','YES','NO')CTTDS_FLAG,DECODE(CONS.CTSOLAR_FLAG,'Y','YES','NO') CTSOLAR_FLAG,DECODE(CONS.CTSCSTFLAG,'Y','YES','NO')CTSCSTFLAG,DECODE(CONS.CTAQUA_FLAG,'Y','YES','NO') CTAQUA_FLAG,DECODE(CONS.CTCOLNYFLAG_HT,'Y','YES','NO')CTCOLNYFLAG_HT,DECODE(CONS.CTLF_FLAG,'Y','YES','NO')CTLF_FLAG,DECODE(CONS.CT_METERSIDE_FLAG,'1','YES','NO')CT_METERSIDE_FLAG,DECODE(CONS.CTSEASFLAG_HT,'Y','YES','NO')CTSEASFLAG_HT,DECODE(TRIM(CONS.CTGOVT_PVT),'Y','GOVT','N','PVT','') TYPE,NVL(CONS.CTHODDEP,'OTH')  deptcode,GMDEPTNAME dept_name,GMSUBDEPTNAME Hod_Name"
						+ " FROM CONS,CONS_FLAGS,servtype ,"
						+ "(SELECT distinct GMTYPECODE,GMTYPE, GMDEPTCODE,GMDEPTNAME,GMSUBDEPTCODE, GMSUBDEPTNAME FROM deptmast group by GMTYPECODE,GMTYPE, GMDEPTCODE,GMDEPTNAME,GMSUBDEPTCODE, GMSUBDEPTNAME \r\n"
						+ "					union all\r\n"
						+ "					select 'OTH' GMTYPECODE,'OTH'GMTYPE, 'OTH' GMDEPTCODE,'OTH' GMDEPTNAME,'OTH' GMSUBDEPTCODE, 'OTH' GMSUBDEPTNAME from dual)"
						+ " WHERE  NVL(SUBSTR(CONS.CTSECCD,-5),'0') NOT IN (SELECT SECCD FROM MASTER.SPDCLMASTER)  AND CONS.CTUSCNO=CONS_FLAGS.CTUSCNO AND CONS.CTSERVTYPE=STCODE(+) "
						+ " and CONS.CTHODTYPE=GMTYPECODE(+) and CONS.CTHODDEP=GMDEPTCODE(+) and CONS.CTHODSUBDEP=GMSUBDEPTCODE(+) "
						+ " ORDER BY 1 "
						+ ") A,(select distinct USCNO,trim(upper(METER_MAKE))METER_MAKE ,MNO from NEW_METER_ENTRY_TEMP A where  MNO is not null\r\n"
						+ "and CREATION_DATE = (select max(CREATION_DATE) from NEW_METER_ENTRY_TEMP where A.MNO=NEW_METER_ENTRY_TEMP.MNO))B\r\n"
						+ "where A.CTUSCNO=uscno(+) AND trim(A.MDMTRNO) = trim(B.MNO(+))order by 1";
				log.info(sql);
				return jdbcTemplate.queryForList(sql);
			} catch (DataAccessException e) {
				log.error(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}

		} else if (circle.equalsIgnoreCase("SPDCL")) {
			try {
				String sql = " select A.*,B.METER_MAKE from (  SELECT CONS.CTUSCNO,SUBNAME,CONS.CTFEEDER_CODE,CONS.CTNAME,CONS.CTCAT,CONS.CTSUBCAT,CONS.CTCMD_HT,TO_CHAR(CONS.CTSUPCONDT,'DD-MM-YYYY')CTSUPCONDT,DECODE(TRIM(CONS.CTSTATUS),'1','LIVE','0','STOP','') CTSTATUS,CONS.CTACTUAL_KV,TO_CHAR(MDMF_HT)MDMF_HT,NVL(EXTENT_OF_LAND,0)EXTENT_OF_LAND,NVL(SURVEY_NUMBER,'-')SURVEY_NUMBER,NVL(LINKED_SCNO1,'-')LINKED_SCNO1, NVL(TYPE_OF_LAND,'-')TYPE_OF_LAND ,STDESC,CONS.CTADD1,CONS.CTADD2,CONS.CTADD3,CONS.CTADD4,CONS.CTMOBILE,CONS.CTEMAILID,NVL(MDMTRNO,'--')MDMTRNO ,,DECODE(TRIM(CONS.CTGOVT_PVT),'Y','GOVT','N','PVT','') TYPE FROM CONS,MASTER.SPDCLMASTER,MTRDAT,CONS_FLAGS,servtype  \r\n"
						+ "WHERE SECCD=SUBSTR(CONS.CTSECCD,-5)\r\n" + "AND CONS.CTUSCNO=MSCNO(+)\r\n"
						+ "AND CONS.CTUSCNO=CONS_FLAGS.CTUSCNO\r\n" + "AND CONS.CTSERVTYPE=STCODE(+)\r\n"
						+ "AND MDCLKWHSTAT_HT NOT IN('04','14','4') \r\n"
						+ "AND SUBSTR(CONS.CTUSCNO,1,3) IN ('TPT','ATP','NLR','CDP','KNL')\r\n" + "UNION ALL \r\n"
						+ "SELECT CONS.CTUSCNO,'' SUBNAME,CONS.CTFEEDER_CODE,CONS.CTNAME,CONS.CTCAT,CONS.CTSUBCAT,CONS.CTCMD_HT,TO_CHAR(CONS.CTSUPCONDT,'DD-MM-YYYY')CTSUPCONDT,DECODE(TRIM(CONS.CTSTATUS),'1','LIVE','0','STOP','') CTSTATUS,CONS.CTACTUAL_KV,'' MDMF_HT,NVL(EXTENT_OF_LAND,0)EXTENT_OF_LAND,NVL(SURVEY_NUMBER,'-')SURVEY_NUMBER,NVL(LINKED_SCNO1,'-')LINKED_SCNO1, NVL(TYPE_OF_LAND,'-')TYPE_OF_LAND ,STDESC,CONS.CTADD1,CONS.CTADD2,CONS.CTADD3,CONS.CTADD4,CONS.CTMOBILE,CONS.CTEMAILID,'0'  ,DECODE(TRIM(CONS.CTGOVT_PVT),'Y','GOVT','N','PVT','') TYPE FROM CONS,CONS_FLAGS,servtype\r\n"
						+ "WHERE  NVL(SUBSTR(CONS.CTSECCD,-5),'0')\r\n"
						+ "NOT IN (SELECT SECCD FROM MASTER.SPDCLMASTER) \r\n"
						+ "AND CONS.CTUSCNO=CONS_FLAGS.CTUSCNO AND CONS.CTSERVTYPE=STCODE(+)\r\n"
						+ "AND SUBSTR(CONS.CTUSCNO,1,3) IN ('TPT','ATP','NLR','CDP','KNL')\r\n" + "ORDER BY 1"
						+ ") A,(select distinct USCNO,trim(upper(METER_MAKE))METER_MAKE ,MNO from NEW_METER_ENTRY_TEMP A where  MNO is not null\r\n"
						+ "and CREATION_DATE = (select max(CREATION_DATE) from NEW_METER_ENTRY_TEMP where A.MNO=NEW_METER_ENTRY_TEMP.MNO))B\r\n"
						+ "where A.CTUSCNO=uscno(+) AND trim(A.MDMTRNO) = trim(B.MNO(+))order by 1";
				;
				log.info(sql);
				return jdbcTemplate.queryForList(sql);
			} catch (DataAccessException e) {
				log.error(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}
		} else {

			try {
				/*
				 * String sql =
				 * "SELECT CTUSCNO,SUBNAME,CTFEEDER_CODE,CTNAME,CTCAT,CTSUBCAT,CTCMD_HT,TO_CHAR(CTSUPCONDT,'DD-MM-YYYY')CTSUPCONDT,DECODE(TRIM(CTSTATUS),'1','LIVE','0','STOP','') CTSTATUS,CTACTUAL_KV,TO_CHAR( MDMF_HT)MDMF_HT\r\n"
				 * + "FROM CONS,SPDCLMASTER,MTRDAT\r\n" +
				 * "WHERE SECCD=SUBSTR(CTSECCD,-5) AND SUBSTR(CTUSCNO,1,3)=? AND CTUSCNO=MSCNO(+)\r\n"
				 * + "UNION ALL\r\n" +
				 * "SELECT CTUSCNO,'' SUBNAME,CTFEEDER_CODE,CTNAME,CTCAT,CTSUBCAT,CTCMD_HT,TO_CHAR(CTSUPCONDT,'DD-MM-YYYY')CTSUPCONDT,DECODE(TRIM(CTSTATUS),'1','LIVE','0','STOP','') CTSTATUS,CTACTUAL_KV,'' MDMF_HT\r\n"
				 * + "FROM CONS\r\n" +
				 * "WHERE  NVL(SUBSTR(CTSECCD,-5),'0') NOT IN (SELECT SECCD FROM SPDCLMASTER) AND SUBSTR(CTUSCNO,1,3)=? \r\n"
				 * + "ORDER BY 1\r\n" + "";
				 */

				/*
				 * String sql =
				 * "SELECT CONS.CTUSCNO,SUBNAME,CONS.CTFEEDER_CODE,CONS.CTNAME,CONS.CTCAT,CONS.CTSUBCAT,CONS.CTCMD_HT,TO_CHAR(CONS.CTSUPCONDT,'DD-MM-YYYY')CTSUPCONDT,DECODE(TRIM(CONS.CTSTATUS),'1','LIVE','0','STOP','') CTSTATUS,CONS.CTACTUAL_KV,TO_CHAR(MDMF_HT)MDMF_HT,NVL(EXTENT_OF_LAND,0)EXTENT_OF_LAND,NVL(SURVEY_NUMBER,'-')SURVEY_NUMBER,NVL(LINKED_SCNO1,'-')LINKED_SCNO1, NVL(TYPE_OF_LAND,'-')TYPE_OF_LAND ,STDESC,CONS.CTADD1,CONS.CTADD2,CONS.CTADD3,CONS.CTADD4,CONS.CTMOBILE,CONS.CTEMAILID,NVL(MDMTRNO,'--')MDMTRNO "
				 * + " FROM CONS,SPDCLMASTER,MTRDAT,CONS_FLAGS,servtype " +
				 * " WHERE SECCD=SUBSTR(CONS.CTSECCD,-5) AND SUBSTR(CONS.CTUSCNO,1,3)=? AND CONS.CTUSCNO=MSCNO(+) AND CONS.CTUSCNO=CONS_FLAGS.CTUSCNO  AND CONS.CTSERVTYPE=STCODE(+) AND MDCLKWHSTAT_HT NOT IN('04','14','4')"
				 * + " UNION ALL " +
				 * " SELECT CONS.CTUSCNO,'' SUBNAME,CONS.CTFEEDER_CODE,CONS.CTNAME,CONS.CTCAT,CONS.CTSUBCAT,CONS.CTCMD_HT,TO_CHAR(CONS.CTSUPCONDT,'DD-MM-YYYY')CTSUPCONDT,DECODE(TRIM(CONS.CTSTATUS),'1','LIVE','0','STOP','') CTSTATUS,CONS.CTACTUAL_KV,'' MDMF_HT,NVL(EXTENT_OF_LAND,0)EXTENT_OF_LAND,NVL(SURVEY_NUMBER,'-')SURVEY_NUMBER,NVL(LINKED_SCNO1,'-')LINKED_SCNO1, NVL(TYPE_OF_LAND,'-')TYPE_OF_LAND ,STDESC,CONS.CTADD1,CONS.CTADD2,CONS.CTADD3,CONS.CTADD4,CONS.CTMOBILE,CONS.CTEMAILID,'0' "
				 * + " FROM CONS,CONS_FLAGS ,servtype" +
				 * " WHERE  NVL(SUBSTR(CONS.CTSECCD,-5),'0') NOT IN (SELECT SECCD FROM SPDCLMASTER) AND SUBSTR(CONS.CTUSCNO,1,3)=? AND CONS.CTUSCNO=CONS_FLAGS.CTUSCNO  AND CONS.CTSERVTYPE=STCODE(+)"
				 * + " ORDER BY 1";
				 */

				/*
				 * String
				 * sql="  select A.*,B.METER_MAKE from (   SELECT CONS.CTUSCNO,SUBNAME,CONS.CTFEEDER_CODE,CONS.CTNAME,CONS.CTCAT,CONS.CTSUBCAT,CONS.CTCMD_HT,TO_CHAR(CONS.CTSUPCONDT,'DD-MM-YYYY')CTSUPCONDT,DECODE(TRIM(CONS.CTSTATUS),'1','LIVE','0','STOP','') CTSTATUS,CONS.CTACTUAL_KV,TO_CHAR(MDMF_HT)MDMF_HT,NVL(EXTENT_OF_LAND,0)EXTENT_OF_LAND,NVL(SURVEY_NUMBER,'-')SURVEY_NUMBER,NVL(LINKED_SCNO1,'-')LINKED_SCNO1, NVL(TYPE_OF_LAND,'-')TYPE_OF_LAND ,STDESC,CONS.CTADD1,CONS.CTADD2,CONS.CTADD3,CONS.CTADD4,CONS.CTMOBILE,CONS.CTEMAILID,NVL(MDMTRNO,'--')MDMTRNO ,"
				 * +
				 * "DECODE(CONS.CTEDFLAG,'Y','YES','NO') CTEDFLAG,DECODE(CONS.CTTDS_FLAG,'Y','YES','NO')CTTDS_FLAG,DECODE(CONS.CTSOLAR_FLAG,'Y','YES','NO') CTSOLAR_FLAG,DECODE(CONS.CTSCSTFLAG,'Y','YES','NO')CTSCSTFLAG,DECODE(CONS.CTAQUA_FLAG,'Y','YES','NO') CTAQUA_FLAG,DECODE(CONS.CTCOLNYFLAG_HT,'Y','YES','NO')CTCOLNYFLAG_HT,DECODE(CONS.CTLF_FLAG,'Y','YES','NO')CTLF_FLAG,DECODE(CONS.CT_METERSIDE_FLAG,'1','YES','NO')CT_METERSIDE_FLAG,DECODE(CONS.CTSEASFLAG_HT,'Y','YES','NO')CTSEASFLAG_HT"
				 * + " FROM CONS,SPDCLMASTER,MTRDAT,CONS_FLAGS,servtype, " +
				 * "(SELECT distinct GMTYPECODE,GMTYPE, GMDEPTCODE,GMDEPTNAME,GMSUBDEPTCODE, GMSUBDEPTNAME FROM deptmast group by GMTYPECODE,GMTYPE, GMDEPTCODE,GMDEPTNAME,GMSUBDEPTCODE, GMSUBDEPTNAME \r\n"
				 * + "					union all\r\n" +
				 * "					select 'OTH' GMTYPECODE,'OTH'GMTYPE, 'OTH' GMDEPTCODE,'OTH' GMDEPTNAME,'OTH' GMSUBDEPTCODE, 'OTH' GMSUBDEPTNAME from dual)"
				 * +
				 * " WHERE SECCD=SUBSTR(CONS.CTSECCD,-5) AND  CONS.CTUSCNO=MSCNO(+) AND CONS.CTUSCNO=CONS_FLAGS.CTUSCNO AND CONS.CTSERVTYPE=STCODE(+) AND MDCLKWHSTAT_HT NOT IN('04','14','4') AND SUBSTR(CONS.CTUSCNO,1,3)=? and CTHODTYPE=GMTYPECODE(+) and CTHODDEP=GMDEPTCODE(+) and CTHODSUBDEP=GMSUBDEPTCODE(+)"
				 * + " UNION ALL " +
				 * " SELECT CONS.CTUSCNO,'' SUBNAME,CONS.CTFEEDER_CODE,CONS.CTNAME,CONS.CTCAT,CONS.CTSUBCAT,CONS.CTCMD_HT,TO_CHAR(CONS.CTSUPCONDT,'DD-MM-YYYY')CTSUPCONDT,DECODE(TRIM(CONS.CTSTATUS),'1','LIVE','0','STOP','') CTSTATUS,CONS.CTACTUAL_KV,'' MDMF_HT,NVL(EXTENT_OF_LAND,0)EXTENT_OF_LAND,NVL(SURVEY_NUMBER,'-')SURVEY_NUMBER,NVL(LINKED_SCNO1,'-')LINKED_SCNO1, NVL(TYPE_OF_LAND,'-')TYPE_OF_LAND ,STDESC,CONS.CTADD1,CONS.CTADD2,CONS.CTADD3,CONS.CTADD4,CONS.CTMOBILE,CONS.CTEMAILID,'0' ,"
				 * +
				 * "DECODE(CONS.CTEDFLAG,'Y','YES','NO') CTEDFLAG,DECODE(CONS.CTTDS_FLAG,'Y','YES','NO')CTTDS_FLAG,DECODE(CONS.CTSOLAR_FLAG,'Y','YES','NO') CTSOLAR_FLAG,DECODE(CONS.CTSCSTFLAG,'Y','YES','NO')CTSCSTFLAG,DECODE(CONS.CTAQUA_FLAG,'Y','YES','NO') CTAQUA_FLAG,DECODE(CONS.CTCOLNYFLAG_HT,'Y','YES','NO')CTCOLNYFLAG_HT,DECODE(CONS.CTLF_FLAG,'Y','YES','NO')CTLF_FLAG,DECODE(CONS.CT_METERSIDE_FLAG,'1','YES','NO')CT_METERSIDE_FLAG,DECODE(CONS.CTSEASFLAG_HT,'Y','YES','NO')CTSEASFLAG_HT"
				 * + " FROM CONS,CONS_FLAGS,servtype , " +
				 * "(SELECT distinct GMTYPECODE,GMTYPE, GMDEPTCODE,GMDEPTNAME,GMSUBDEPTCODE, GMSUBDEPTNAME FROM deptmast group by GMTYPECODE,GMTYPE, GMDEPTCODE,GMDEPTNAME,GMSUBDEPTCODE, GMSUBDEPTNAME \r\n"
				 * + "					union all\r\n" +
				 * "					select 'OTH' GMTYPECODE,'OTH'GMTYPE, 'OTH' GMDEPTCODE,'OTH' GMDEPTNAME,'OTH' GMSUBDEPTCODE, 'OTH' GMSUBDEPTNAME from dual) "
				 * +
				 * " WHERE  NVL(SUBSTR(CONS.CTSECCD,-5),'0') NOT IN (SELECT SECCD FROM SPDCLMASTER)  AND CONS.CTUSCNO=CONS_FLAGS.CTUSCNO AND CONS.CTSERVTYPE=STCODE(+) AND SUBSTR(CONS.CTUSCNO,1,3)=? , "
				 * +
				 * " CTHODTYPE=GMTYPECODE(+) and CTHODDEP=GMDEPTCODE(+) and CTHODSUBDEP=GMSUBDEPTCODE(+)"
				 * + " ORDER BY 1"+
				 * ") A,(select distinct USCNO,trim(upper(METER_MAKE))METER_MAKE ,MNO from NEW_METER_ENTRY_TEMP A where  MNO is not null\r\n"
				 * +
				 * "and CREATION_DATE = (select max(CREATION_DATE) from NEW_METER_ENTRY_TEMP where A.MNO=NEW_METER_ENTRY_TEMP.MNO))B\r\n"
				 * + "where A.CTUSCNO=uscno(+) AND trim(A.MDMTRNO) = trim(B.MNO(+))order by 1";
				 */

				String sql = " select * from ( select A.*,B.METER_MAKE,"
						+ "(select NVL(CB_SD,'0') from ledger_ht_hist where to_date(MON_YEAR,'MON-YYYY') = (select max(to_date(MON_YEAR,'MON-YYYY')) from ledger_ht_hist where uscno = A.CTUSCNO ) and uscno = A.CTUSCNO ) SD,"
						+ "(select NVL(CBTOT,'0') from ledger_ht_hist where to_date(MON_YEAR,'MON-YYYY') = (select max(to_date(MON_YEAR,'MON-YYYY')) from ledger_ht_hist where uscno = A.CTUSCNO ) and uscno = A.CTUSCNO ) CB"
						+ " from ( SELECT CONS.CTUSCNO,SUBNAME,CONS.CTFEEDER_CODE,CONS.CTNAME,CONS.CTCAT,CONS.CTSUBCAT,CONS.CTCMD_HT,TO_CHAR(CONS.CTSUPCONDT,'DD-MM-YYYY')CTSUPCONDT,DECODE(TRIM(CONS.CTSTATUS),'1','LIVE','0','STOP','') CTSTATUS,CONS.CTACTUAL_KV,TO_CHAR(MDMF_HT)MDMF_HT,NVL(EXTENT_OF_LAND,0)EXTENT_OF_LAND,NVL(SURVEY_NUMBER,'-')SURVEY_NUMBER,NVL(LINKED_SCNO1,'-')LINKED_SCNO1, NVL(TYPE_OF_LAND,'-')TYPE_OF_LAND ,STDESC,CONS.CTADD1,CONS.CTADD2,CONS.CTADD3,CONS.CTADD4,CONS.CTMOBILE,CONS.CTEMAILID,NVL(MDMTRNO,'--')MDMTRNO ,"
						+ "DECODE(CONS.CTEDFLAG,'Y','YES','NO') CTEDFLAG,DECODE(CONS.CTTDS_FLAG,'Y','YES','NO')CTTDS_FLAG,DECODE(CONS.CTSOLAR_FLAG,'Y','YES','NO') CTSOLAR_FLAG,DECODE(CONS.CTSCSTFLAG,'Y','YES','NO')CTSCSTFLAG,DECODE(CONS.CTAQUA_FLAG,'Y','YES','NO') CTAQUA_FLAG,DECODE(CONS.CTCOLNYFLAG_HT,'Y','YES','NO')CTCOLNYFLAG_HT,DECODE(CONS.CTLF_FLAG,'Y','YES','NO')CTLF_FLAG,DECODE(CONS.CT_METERSIDE_FLAG,'1','YES','NO')CT_METERSIDE_FLAG,DECODE(CONS.CTSEASFLAG_HT,'Y','YES','NO')CTSEASFLAG_HT,DECODE(TRIM(CONS.CTGOVT_PVT),'Y','GOVT','N','PVT','') TYPE,NVL(CONS.CTHODDEP,'OTH')  deptcode,GMDEPTNAME dept_name,GMSUBDEPTNAME Hod_Name"
						+ " FROM CONS,MASTER.SPDCLMASTER,MTRDAT,CONS_FLAGS,servtype,"
						+ " (SELECT distinct GMTYPECODE,GMTYPE, GMDEPTCODE,GMDEPTNAME,GMSUBDEPTCODE, GMSUBDEPTNAME FROM deptmast group by GMTYPECODE,GMTYPE, GMDEPTCODE,GMDEPTNAME,GMSUBDEPTCODE, GMSUBDEPTNAME \r\n"
						+ "					union all\r\n"
						+ "					select 'OTH' GMTYPECODE,'OTH'GMTYPE, 'OTH' GMDEPTCODE,'OTH' GMDEPTNAME,'OTH' GMSUBDEPTCODE, 'OTH' GMSUBDEPTNAME from dual) "
						+ " WHERE SECCD=SUBSTR(CONS.CTSECCD,-5) AND  CONS.CTUSCNO=MSCNO(+) AND CONS.CTUSCNO=CONS_FLAGS.CTUSCNO AND CONS.CTSERVTYPE=STCODE(+) and "
						+ " CONS.CTHODTYPE=GMTYPECODE(+) and CONS.CTHODDEP=GMDEPTCODE(+) and CONS.CTHODSUBDEP=GMSUBDEPTCODE(+)"
						+
						/* + " AND MDCLKWHSTAT_HT NOT IN('04','14','4') "+ */
						" UNION ALL "
						+ " SELECT CONS.CTUSCNO,'' SUBNAME,CONS.CTFEEDER_CODE,CONS.CTNAME,CONS.CTCAT,CONS.CTSUBCAT,CONS.CTCMD_HT,TO_CHAR(CONS.CTSUPCONDT,'DD-MM-YYYY')CTSUPCONDT,DECODE(TRIM(CONS.CTSTATUS),'1','LIVE','0','STOP','') CTSTATUS,CONS.CTACTUAL_KV,'' MDMF_HT,NVL(EXTENT_OF_LAND,0)EXTENT_OF_LAND,NVL(SURVEY_NUMBER,'-')SURVEY_NUMBER,NVL(LINKED_SCNO1,'-')LINKED_SCNO1, NVL(TYPE_OF_LAND,'-')TYPE_OF_LAND ,STDESC,CONS.CTADD1,CONS.CTADD2,CONS.CTADD3,CONS.CTADD4,CONS.CTMOBILE,CONS.CTEMAILID,'0' ,"
						+ "DECODE(CONS.CTEDFLAG,'Y','YES','NO') CTEDFLAG,DECODE(CONS.CTTDS_FLAG,'Y','YES','NO')CTTDS_FLAG,DECODE(CONS.CTSOLAR_FLAG,'Y','YES','NO') CTSOLAR_FLAG,DECODE(CONS.CTSCSTFLAG,'Y','YES','NO')CTSCSTFLAG,DECODE(CONS.CTAQUA_FLAG,'Y','YES','NO') CTAQUA_FLAG,DECODE(CONS.CTCOLNYFLAG_HT,'Y','YES','NO')CTCOLNYFLAG_HT,DECODE(CONS.CTLF_FLAG,'Y','YES','NO')CTLF_FLAG,DECODE(CONS.CT_METERSIDE_FLAG,'1','YES','NO')CT_METERSIDE_FLAG,DECODE(CONS.CTSEASFLAG_HT,'Y','YES','NO')CTSEASFLAG_HT,DECODE(TRIM(CONS.CTGOVT_PVT),'Y','GOVT','N','PVT','') TYPE,NVL(CONS.CTHODDEP,'OTH')  deptcode,GMDEPTNAME dept_name,GMSUBDEPTNAME Hod_Name"
						+ " FROM CONS,CONS_FLAGS,servtype ,"
						+ "(SELECT distinct GMTYPECODE,GMTYPE, GMDEPTCODE,GMDEPTNAME,GMSUBDEPTCODE, GMSUBDEPTNAME FROM deptmast group by GMTYPECODE,GMTYPE, GMDEPTCODE,GMDEPTNAME,GMSUBDEPTCODE, GMSUBDEPTNAME \r\n"
						+ "					union all\r\n"
						+ "					select 'OTH' GMTYPECODE,'OTH'GMTYPE, 'OTH' GMDEPTCODE,'OTH' GMDEPTNAME,'OTH' GMSUBDEPTCODE, 'OTH' GMSUBDEPTNAME from dual)"
						+ " WHERE  NVL(SUBSTR(CONS.CTSECCD,-5),'0') NOT IN (SELECT SECCD FROM MASTER.SPDCLMASTER)  AND CONS.CTUSCNO=CONS_FLAGS.CTUSCNO AND CONS.CTSERVTYPE=STCODE(+) "
						+ " and CONS.CTHODTYPE=GMTYPECODE(+) and CONS.CTHODDEP=GMDEPTCODE(+) and CONS.CTHODSUBDEP=GMSUBDEPTCODE(+) "
						+ " ORDER BY 1 "
						+ ") A,(select distinct USCNO,trim(upper(METER_MAKE))METER_MAKE ,MNO from NEW_METER_ENTRY_TEMP A where  MNO is not null\r\n"
						+ "and CREATION_DATE = (select max(CREATION_DATE) from NEW_METER_ENTRY_TEMP where A.MNO=NEW_METER_ENTRY_TEMP.MNO))B\r\n"
						+ "where A.CTUSCNO=uscno(+) AND trim(A.MDMTRNO) = trim(B.MNO(+))) where SUBSTR(CTUSCNO,1,3)=? order by 1";

				log.info(sql);
				return jdbcTemplate.queryForList(sql, new Object[] { circle });
			} catch (DataAccessException e) {
				log.error(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}

		}
	}

	public List<Map<String, Object>> getKYCAbs() {
		try {
			String plantsql = "select A.CIRNAME, KYC_COUNT, NKYC_COUNT  from (select CIRNAME, sum(COUNT) KYC_COUNT from (select  case when substr(USCNO,1,3)='CRD' then 'CRDA'\r\n"
					+ "            when substr(USCNO,1,3)='ONG' then 'ONGOLE'\r\n"
					+ "            when substr(USCNO,1,3)='VJA' then 'VIJAYAWADA'\r\n"
					+ "            when substr(USCNO,1,3)='GNT' then 'GUNTUR' END CIRNAME,count(*) COUNT from KYC_DETAILS group by substr(USCNO,1,3)\r\n"
					+ " union \r\n" + "select distinct trim(CIRNAME) CIRNAME, 0 count  from MASTER.SPDCLMASTER)\r\n"
					+ "group by CIRNAME) A ,\r\n" + "(select case when substr(ctuscno,1,3)='CRD' then 'CRDA'\r\n"
					+ "            when substr(ctuscno,1,3)='ONG' then 'ONGOLE'\r\n"
					+ "            when substr(ctuscno,1,3)='VJA' then 'VIJAYAWADA'\r\n"
					+ "            when substr(ctuscno,1,3)='GNT' then 'GUNTUR' END CIRNAME,count(*) NKYC_COUNT from cons where ctuscno not in (select uscno from KYC_DETAILS) group by substr(ctuscno,1,3)) B\r\n"
					+ "            where A.CIRNAME = B.CIRNAME";
			log.info(plantsql);
			return jdbcTemplate.queryForList(plantsql);
		} catch (DataAccessException e) {
			log.error(e.getMessage());
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	public List<Map<String, Object>> getKYC(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		String con = request.getParameter("type").equals("KYC") ? " and CTUSCNO in (select uscno from KYC_DETAILS) "
				: "  and CTUSCNO not in (select uscno from KYC_DETAILS)  ";
		if (circle.equals("ALL")) {
			try {
				String plantsql = "select CTUSCNO, CTNAME, DIVNAME, SUBNAME, SECNAME  from CONS ,MASTER.SPDCLMASTER where  SECCD=SUBSTR(CONS.CTSECCD,-5) "
						+ con + " order by ctuscno";
				log.info(plantsql);
				return jdbcTemplate.queryForList(plantsql);
			} catch (DataAccessException e) {
				log.error(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}
		} else {

			try {
				String plantsql = "select CTUSCNO, CTNAME, DIVNAME, SUBNAME, SECNAME  from CONS ,MASTER.SPDCLMASTER where  SECCD=SUBSTR(CONS.CTSECCD,-5) and substr(CTUSCNO,1,3) = ? "
						+ con + " order by ctuscno";
				;
				log.info(plantsql);
				return jdbcTemplate.queryForList(plantsql, new Object[] { circle });
			} catch (DataAccessException e) {
				log.error(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}
		}
	}

	public List<Map<String, Object>> getStartupPowerServicesDetails(HttpServletRequest request) {
		try {
			String plantsql = "SELECT USCNO,GEN_TYP,PLANT_CAP,GEN_UNITS,MAX_CAP,ALLW_MDPERC,ALLW_MDKVA FROM TWOD_PLANT_CAPACITY";
			log.info(plantsql);
			return jdbcTemplate.queryForList(plantsql);
		} catch (DataAccessException e) {
			log.error(e.getMessage());
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	public List<Map<String, Object>> getServiceReleaseDetails(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		String fmonthYear = request.getParameter("fmonth") + "-" + request.getParameter("fyear");
		String tmonthYear = request.getParameter("tmonth") + "-" + request.getParameter("tyear");

		String con = request.getParameter("category").equals("ALL") ? " "
				: " AND CTCAT = '" + request.getParameter("category") + "' ";

		if (circle.equals("ALL")) {
			try {
				String serviceSql = "SELECT DISTINCT(USCNO),CIRNAME,CTNAME,DIVNAME,SUBNAME,SECNAME,CTCAT,CTSUBCAT,CTCMD_HT,TO_CHAR(CTSUPCONDT,'DD-MM-YYYY')CTSUPCONDT,decode(TRIM(CTSTATUS),'1','LIVE','0','STOP','') CTSTATUS,CTACTUAL_KV,CB_SD SD,NVL((NVL(CBTOT,0)+NVL(CB_OTH,0)+NVL(CB_CCLPC,0)),0) CB_TOT,MDMF_HT FROM ledger_ht_hist,cons,spdclmaster,MTRDAT\r\n"
						+ "WHERE  TRUNC(CTSUPCONDT) between TRUNC(to_date(?,'MON_YYYY')) and LAST_DAY(to_date(?,'MON_YYYY'))   AND TO_DATE(MON_YEAR,'MON-YYYY') IN ( SELECT MAX(TO_DATE(MON_YEAR,'MON-YYYY')) FROM ledger_ht_hist ) \r\n"
						+ " AND CTUSCNO=USCNO(+)\r\n" + "AND SECCD=SUBSTR(CTSECCD,-5)\r\n" + "AND CTUSCNO=MSCNO(+) \r\n"
						+ con + " ORDER BY USCNO ";
				log.info(serviceSql);
				return jdbcTemplate.queryForList(serviceSql, new Object[] { fmonthYear, tmonthYear });
			} catch (DataAccessException e) {
				log.error(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}
		} else {
			try {
				String serviceSql = "SELECT DISTINCT(USCNO),CIRNAME,CTNAME,DIVNAME,SUBNAME,SECNAME,CTCAT,CTSUBCAT,CTCMD_HT,TO_CHAR(CTSUPCONDT,'DD-MM-YYYY')CTSUPCONDT,decode(TRIM(CTSTATUS),'1','LIVE','0','STOP','') CTSTATUS,CTACTUAL_KV,CB_SD SD,NVL((NVL(CBTOT,0)+NVL(CB_OTH,0)+NVL(CB_CCLPC,0)),0) CB_TOT,MDMF_HT FROM ledger_ht_hist,cons,spdclmaster,MTRDAT\r\n"
						+ "WHERE   TRUNC(CTSUPCONDT) between TRUNC(to_date(?,'MON_YYYY')) and LAST_DAY(to_date(?,'MON_YYYY'))  AND TO_DATE(MON_YEAR,'MON-YYYY') IN ( SELECT MAX(TO_DATE(MON_YEAR,'MON-YYYY')) FROM ledger_ht_hist ) \r\n"
						+ "AND SUBSTR(USCNO,0,3)=? AND CTUSCNO=USCNO(+)\r\n" + "AND SECCD=SUBSTR(CTSECCD,-5)\r\n"
						+ "AND CTUSCNO=MSCNO(+)\r\n" + con + " ORDER BY USCNO ";
				log.info(serviceSql);
				return jdbcTemplate.queryForList(serviceSql, new Object[] { fmonthYear, tmonthYear, circle });
			} catch (DataAccessException e) {
				log.error(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}
		}
	}

	public List<Map<String, Object>> getServiceWiseLedgerDetails(HttpServletRequest request, String financiyalYearFrom,
			String financiyalYearTo) {
		String circle = request.getParameter("circle");
		if (circle.equals("ALL")) {
			try {
				String serviceWiseSdsql = "SELECT CIRCLE,USCNO,CAT||SCAT SCAT,NAM,MON_YEAR,decode(TRIM(STATUS),'1','LIVE','0','STP','')STATUS,NVL(OB_SD,0)OB_SD,(NVL(OBNSC_SD,0)+NVL(PAID_SD,0))PAID_SD,(NVL(DRJ_SD,0)+NVL(CRJ_SD,0))ADJ_SD,NVL(CB_SD,0)CB_SD\r\n"
						+ "FROM ACCOUNTCOPY  ORDER BY USCNO";
				log.info(serviceWiseSdsql);
				return jdbcTemplate.queryForList(serviceWiseSdsql);
			} catch (DataAccessException e) {
				log.error(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}
		} else {
			try {
				System.out.println(circle);
				String serviceWiseSdsql = "SELECT CIRCLE,USCNO,CAT||SCAT SCAT,NAM,MON_YEAR,decode(TRIM(STATUS),'1','LIVE','0','STP','')STATUS,NVL(OB_SD,0)OB_SD,(NVL(OBNSC_SD,0)+NVL(PAID_SD,0))PAID_SD,(NVL(DRJ_SD,0)+NVL(CRJ_SD,0))ADJ_SD,NVL(CB_SD,0)CB_SD\r\n"
						+ "FROM ACCOUNTCOPY WHERE SUBSTR(USCNO,0,3)=? ORDER BY USCNO";
				log.info(serviceWiseSdsql);
				return jdbcTemplate.queryForList(serviceWiseSdsql, new Object[] { circle });
			} catch (DataAccessException e) {
				e.printStackTrace();
				return Collections.emptyList();
			}
		}

	}

	public List<Map<String, Object>> getPayDetails(String uscno, String from, String to) {
		try {
			String payhistSql = "SELECT SUBSTR(USCNO,1,3)CIRCLE,USCNO,TO_CHAR(PAY_DATE,'DD-MON-YYYY') ACCOUNT_DATE,'PAYMENT' TRANS_TYPE,NVL(PACD,0)SD_ACD FROM PAY_HT_HIST WHERE  PAY_DATE BETWEEN  TO_DATE(?,'DD-MM-YYYY') AND TO_DATE(?,'DD-MM-YYYY') AND USCNO=? AND PACD>0 \r\n"
					+ "UNION ALL\r\n"
					+ "SELECT SUBSTR(USCNO,1,3)CIRCLE,USCNO,TO_CHAR(RJDT,'DD-MON-YYYY') ACCOUNT_DATE,'ADJUSTMENT'  TRANS_TYPE,NVL(ACD,0)SD_ACD  FROM journal_hist WHERE   RJDT BETWEEN TO_DATE(?,'DD-MM-YYYY') AND TO_DATE(?,'DD-MM-YYYY') AND USCNO=? AND ACD!=0 \r\n"
					+ "";
			log.info(payhistSql);
			return jdbcTemplate.queryForList(payhistSql, new Object[] { from, to, uscno, from, to, uscno });
		} catch (DataAccessException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return Collections.emptyList();
		}
	}

	public List<Map<String, Object>> getEmailChekListDetails(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		if (!circle.equals("ALL")) {
			try {
				String sql = "SELECT CTUSCNO,CTNAME,CTCAT,CTSUBCAT,CTEMAILID FROM CONS WHERE CTEMAILID NOT IN(' ','NA','---','0','-','--') AND SUBSTR(CTUSCNO,1,3)=?  ORDER BY  SUBSTR(CTUSCNO,1,3) ";
				log.info(sql);
				return jdbcTemplate.queryForList(sql, new Object[] { circle }).stream()
						.sorted(Comparator
								.comparingInt(s -> Integer.parseInt(s.get("CTUSCNO").toString().substring(3))))
						.collect(Collectors.toList());
			} catch (DataAccessException e) {
				e.printStackTrace();
				log.info(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}
		} else {
			try {
				String sql = "SELECT CTUSCNO,CTNAME,CTCAT,CTSUBCAT,CTEMAILID FROM CONS WHERE  CTEMAILID NOT IN(' ','NA','---','0','-','--')   ORDER BY  SUBSTR(CTUSCNO,1,3)";
				log.info(sql);
				return jdbcTemplate.queryForList(sql).stream()
						.sorted(Comparator
								.comparingInt(s -> Integer.parseInt(s.get("CTUSCNO").toString().substring(3))))
						.collect(Collectors.toList());
			} catch (DataAccessException e) {
				e.printStackTrace();
				log.info(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}
		}

	}

	public List<Map<String, Object>> getLTHTTrueUpCharges(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		String status = request.getParameter("status");
		String constring = status.equals("LB") ? " and CTSTATUS<>2 "
				: status.equals("L") ? "  and CTSTATUS =1  " : "   and CTSTATUS = 0 ";

		if (!circle.equals("ALL")) {
			try {
				String sql = " select  CASE\r\n" + "    WHEN SUBSTR(CTUSCNO,1,3)='VJA' THEN  'KRISHNA'\r\n"
						+ "    WHEN SUBSTR(CTUSCNO,1,3)='GNT' THEN 'GUNTUR'\r\n"
						+ "    WHEN SUBSTR(CTUSCNO,1,3)='ONG' THEN 'ONGOLE'\r\n"
						+ "    WHEN SUBSTR(CTUSCNO,1,3)='CRD' THEN 'CRDA' END CIRCLE,CTUSCNO,LT_USCNO,TOT_UNITS, CTCAT||'-'||CTSUBCAT CAT, TOT_AMOUNT, TRUEUP_PERMONTH , DECODE(CTSTATUS ,1,'LIVE','BILLSTOP') STATUS from LT_HT_TU_RJ,cons  where CTUSCNO=HT_USCNO and substr(CTUSCNO,1,3)= ? "
						+ constring + " \r\n" + "    order by CTUSCNO";
				log.info(sql);
				return jdbcTemplate.queryForList(sql, new Object[] { circle }).stream()
						.sorted(Comparator
								.comparingInt(s -> Integer.parseInt(s.get("CTUSCNO").toString().substring(3))))
						.collect(Collectors.toList());
			} catch (DataAccessException e) {
				e.printStackTrace();
				log.info(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}
		} else {
			try {
				String sql = " select  CASE\r\n" + "    WHEN SUBSTR(CTUSCNO,1,3)='VJA' THEN  'KRISHNA'\r\n"
						+ "    WHEN SUBSTR(CTUSCNO,1,3)='GNT' THEN 'GUNTUR'\r\n"
						+ "    WHEN SUBSTR(CTUSCNO,1,3)='ONG' THEN 'ONGOLE'\r\n"
						+ "    WHEN SUBSTR(CTUSCNO,1,3)='CRD' THEN 'CRDA' END CIRCLE,CTUSCNO,LT_USCNO,CTCAT||'-'||CTSUBCAT CAT,TOT_UNITS, TOT_AMOUNT, TRUEUP_PERMONTH , DECODE(CTSTATUS ,1,'LIVE','BILLSTOP') STATUS from LT_HT_TU_RJ,cons  where CTUSCNO=HT_USCNO  "
						+ constring + " \r\n" + "    order by CTUSCNO";

				log.info(sql);
				return jdbcTemplate.queryForList(sql).stream()
						.sorted(Comparator
								.comparingInt(s -> Integer.parseInt(s.get("CTUSCNO").toString().substring(3))))
						.collect(Collectors.toList());
			} catch (DataAccessException e) {
				e.printStackTrace();
				log.info(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}
		}

	}

	public List<Map<String, Object>> getLTHTFPPCACharges(HttpServletRequest request) {
		String circle = request.getParameter("circle");

		if (!circle.equals("ALL")) {
			try {
				String sql = "select * from V_QTRWISE_FPP_CHG where substr(HT_SCNO,1,3)=?";
				log.info(sql);
				return jdbcTemplate.queryForList(sql, new Object[] { circle });
			} catch (DataAccessException e) {
				e.printStackTrace();
				log.info(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}
		} else {
			try {
				String sql = "select * from V_QTRWISE_FPP_CHG";

				log.info(sql);
				return jdbcTemplate.queryForList(sql);
			} catch (DataAccessException e) {
				e.printStackTrace();
				log.info(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}
		}

	}

	/*
	 * public List<Map<String, Object>> getLTHTFPPCACharges(HttpServletRequest
	 * request) { String circle = request.getParameter("circle"); if
	 * (!circle.equals("ALL")) { try { String sql =" select  CASE\r\n" +
	 * "    WHEN SUBSTR(CTUSCNO,1,3)='VJA' THEN  'KRISHNA'\r\n" +
	 * "    WHEN SUBSTR(CTUSCNO,1,3)='GNT' THEN 'GUNTUR'\r\n" +
	 * "    WHEN SUBSTR(CTUSCNO,1,3)='ONG' THEN 'ONGOLE'\r\n" +
	 * "    WHEN SUBSTR(CTUSCNO,1,3)='CRD' THEN 'CRDA' END CIRCLE,CTUSCNO,BLUSCNO LT_USCNO,CTCAT||'-'||CTSUBCAT CAT,BLD_UTS TOT_UNITS, TO_NUMBER(BLD_UTS) * 0.201 TOT_AMOUNT,  0.201 TRUEUP_PERMONTH , DECODE(CTSTATUS ,1,'LIVE','BILLSTOP') STATUS  from V_LT_HT_FPPCA_21_22,cons  where CTUSCNO=HT_SCNO and substr(CTUSCNO,1,3)= ? "
	 * + "    order by CTUSCNO"; log.info(sql); return
	 * jdbcTemplate.queryForList(sql,new Object[] {circle}).stream()
	 * .sorted(Comparator .comparingInt(s ->
	 * Integer.parseInt(s.get("CTUSCNO").toString().substring(3))))
	 * .collect(Collectors.toList()); } catch (DataAccessException e) {
	 * e.printStackTrace(); log.info(e.getMessage()); e.printStackTrace(); return
	 * Collections.emptyList(); } } else { try { String sql =" select  CASE\r\n" +
	 * "    WHEN SUBSTR(CTUSCNO,1,3)='VJA' THEN  'KRISHNA'\r\n" +
	 * "    WHEN SUBSTR(CTUSCNO,1,3)='GNT' THEN 'GUNTUR'\r\n" +
	 * "    WHEN SUBSTR(CTUSCNO,1,3)='ONG' THEN 'ONGOLE'\r\n" +
	 * "    WHEN SUBSTR(CTUSCNO,1,3)='CRD' THEN 'CRDA' END CIRCLE,CTUSCNO,BLUSCNO LT_USCNO,CTCAT||'-'||CTSUBCAT CAT,BLD_UTS TOT_UNITS, TO_NUMBER(BLD_UTS) * 0.201 TOT_AMOUNT,  0.201 TRUEUP_PERMONTH , DECODE(CTSTATUS ,1,'LIVE','BILLSTOP') STATUS  from V_LT_HT_FPPCA_21_22,cons  where CTUSCNO=HT_SCNO  "
	 * + "    order by CTUSCNO";
	 * 
	 * log.info(sql); return jdbcTemplate.queryForList(sql).stream()
	 * .sorted(Comparator .comparingInt(s ->
	 * Integer.parseInt(s.get("CTUSCNO").toString().substring(3))))
	 * .collect(Collectors.toList()); } catch (DataAccessException e) {
	 * e.printStackTrace(); log.info(e.getMessage()); e.printStackTrace(); return
	 * Collections.emptyList(); } }
	 * 
	 * }
	 */

	public List<Map<String, Object>> getHTBSFPPCACharges(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		String status = request.getParameter("status");
		String constring = status.equals("LB") ? " and CTSTATUS<>2 "
				: status.equals("L") ? "  and CTSTATUS =1  " : "   and CTSTATUS = 0 ";

		if (!circle.equals("ALL")) {
			try {
				String sql = "select * from V_BS_QTRWISE_FPP_CHG where substr(BTSCNO,1,3)=?";
				log.info(sql);
				return jdbcTemplate.queryForList(sql, new Object[] { circle });
			} catch (DataAccessException e) {
				e.printStackTrace();
				log.info(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}
		} else {
			try {
				String sql = "select * from V_BS_QTRWISE_FPP_CHG";

				log.info(sql);
				return jdbcTemplate.queryForList(sql);
			} catch (DataAccessException e) {
				e.printStackTrace();
				log.info(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}
		}

	}

	public List<Map<String, Object>> getTrueUpListDetails(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		String status = request.getParameter("status");
		String constring = status.equals("LB") ? " and status<>2 "
				: status.equals("L") ? "  and status =1  " : "   and status = 0 ";

		if (!circle.equals("ALL")) {
			try {
				String sql = "select  CASE\r\n" + "    WHEN SUBSTR(USCNO,1,3)='VJA' THEN  'VIJAYAWADA'\r\n"
						+ "    WHEN SUBSTR(USCNO,1,3)='GNT' THEN 'GUNTUR'\r\n"
						+ "    WHEN SUBSTR(USCNO,1,3)='ONG' THEN 'ONGOLE'\r\n"
						+ "    WHEN SUBSTR(USCNO,1,3)='CRD' THEN 'CRDA' END CIRCLE,USCNO,CTCAT||'-'||CTSUBCAT CAT,TOT_KVAH, TOT_AMT, TU_MON_CHG,  DECODE(CTSTATUS ,1,'LIVE','BILLSTOP') STATUS from ht_trueup_chg,cons where uscno=ctuscno and substr(uscno,1,3)= ? "
						+ constring + " order by uscno";
				log.info(sql);
				return jdbcTemplate.queryForList(sql, new Object[] { circle }).stream()
						.sorted(Comparator.comparingInt(s -> Integer.parseInt(s.get("USCNO").toString().substring(3))))
						.collect(Collectors.toList());
			} catch (DataAccessException e) {
				e.printStackTrace();
				log.info(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}
		} else {
			try {
				String sql = "select  CASE\r\n" + "    WHEN SUBSTR(USCNO,1,3)='VJA' THEN  'KRISHNA'\r\n"
						+ "    WHEN SUBSTR(USCNO,1,3)='GNT' THEN 'GUNTUR'\r\n"
						+ "    WHEN SUBSTR(USCNO,1,3)='ONG' THEN 'ONGOLE'\r\n"
						+ "    WHEN SUBSTR(USCNO,1,3)='CRD' THEN 'CRDA' END CIRCLE,USCNO,CTCAT||'-'||CTSUBCAT CAT,TOT_KVAH, TOT_AMT, TU_MON_CHG,  DECODE(CTSTATUS ,1,'LIVE','BILLSTOP') STATUS from ht_trueup_chg,cons where uscno=ctuscno  "
						+ constring + "  order by uscno";
				log.info(sql);
				return jdbcTemplate.queryForList(sql).stream()
						.sorted(Comparator.comparingInt(s -> Integer.parseInt(s.get("USCNO").toString().substring(3))))
						.collect(Collectors.toList());
			} catch (DataAccessException e) {
				e.printStackTrace();
				log.info(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}
		}

	}

	public List<Map<String, Object>> getOldColony(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		if (!circle.equals("ALL")) {
			try {
				String sql = "select CIRNAME, DIVNAME, SUBNAME, SECNAME,USCNO,CTNAME, to_char(OPEN_DT,'DD-MON-YYYY') OPEN_DT, to_char(END_DT,'DD-MON-YYYY') END_DT,KVAH_OP_READING, KVAH_CLOS_RDG, MF, REC_KVAH from cons,HT_OLD_COLONY_RDG,master.SPDCLMASTER where ctuscno=uscno  AND SECCD = SUBSTR(CONS.CTSECCD,-5)  and substr(ctuscno,1,3)=? order by USCNO,to_date(OPEN_DT,'DD-MON-YYYY') ";
				log.info(sql);
				return jdbcTemplate.queryForList(sql, new Object[] { circle });
			} catch (DataAccessException e) {
				e.printStackTrace();
				log.info(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}
		} else {
			try {
				String sql = "select CIRNAME, DIVNAME, SUBNAME, SECNAME,USCNO,CTNAME, to_char(OPEN_DT,'DD-MON-YYYY') OPEN_DT, to_char(END_DT,'DD-MON-YYYY') END_DT, KVAH_OP_READING, KVAH_CLOS_RDG, MF, REC_KVAH from cons,HT_OLD_COLONY_RDG,master.SPDCLMASTER where ctuscno=uscno  AND SECCD = SUBSTR(CONS.CTSECCD,-5) order by USCNO,to_date(OPEN_DT,'DD-MON-YYYY')";
				log.info(sql);
				return jdbcTemplate.queryForList(sql);
			} catch (DataAccessException e) {
				e.printStackTrace();
				log.info(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}
		}

	}

	public List<Map<String, Object>> getOldColonyFPP(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		if (!circle.equals("ALL")) {
			try {
				String sql = " select  CIRNAME, DIVNAME, SUBNAME, SECNAME,USCNO,CTNAME\r\n"
						+ " ,sum(nvl(REC_KVAH,0)) REC_KVAH , '0.22'  RATE , sum(nvl(REC_KVAH,0))*0.22 TOT_AMT\r\n"
						+ " from cons,HT_OLD_COLONY_RDG,master.SPDCLMASTER \r\n" + " where \r\n"
						+ "  ctuscno=uscno  \r\n" + " AND SECCD = SUBSTR(CTSECCD,-5) \r\n"
						+ " and substr(ctuscno,1,3) = ? "
						+ "and to_date(OPEN_DT,'DD-MM-YY') between to_date('01-03-2014','DD-MM-YYYY') and to_date('28-02-2019','DD-MM-YYYY')\r\n"
						+ " group by CIRNAME, DIVNAME, SUBNAME, SECNAME,USCNO,CTNAME order by CIRNAME, DIVNAME, SUBNAME, SECNAME,USCNO,CTNAME ";
				log.info(sql);
				return jdbcTemplate.queryForList(sql, new Object[] { circle });
			} catch (DataAccessException e) {
				e.printStackTrace();
				log.info(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}
		} else {
			try {
				String sql = " select  CIRNAME, DIVNAME, SUBNAME, SECNAME,USCNO,CTNAME\r\n"
						+ " ,sum(nvl(REC_KVAH,0)) REC_KVAH , '0.22'  RATE , sum(nvl(REC_KVAH,0))*0.22 TOT_AMT\r\n"
						+ " from cons,HT_OLD_COLONY_RDG,master.SPDCLMASTER \r\n" + " where \r\n"
						+ "  ctuscno=uscno  \r\n" + " AND SECCD = SUBSTR(CTSECCD,-5) \r\n"
						+ "and to_date(OPEN_DT,'DD-MM-YY') between to_date('01-03-2014','DD-MM-YYYY') and to_date('28-02-2019','DD-MM-YYYY')\r\n"
						+ " group by CIRNAME, DIVNAME, SUBNAME, SECNAME,USCNO,CTNAME  order by CIRNAME, DIVNAME, SUBNAME, SECNAME,USCNO,CTNAME ";
				log.info(sql);
				return jdbcTemplate.queryForList(sql);
			} catch (DataAccessException e) {
				e.printStackTrace();
				log.info(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}
		}

	}

	public List<Map<String, Object>> getNoemailsDetails(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		if (!circle.equals("ALL")) {
			try {
				String sql = "SELECT CTUSCNO,CTNAME,CTCAT,CTSUBCAT,CTEMAILID FROM CONS WHERE CTEMAILID  IN (' ','NA','---','0','-','--') AND SUBSTR(CTUSCNO,1,3)=?  ORDER BY  SUBSTR(CTUSCNO,1,3) ";
				log.info(sql);
				return jdbcTemplate.queryForList(sql, new Object[] { circle }).stream()
						.sorted(Comparator
								.comparingInt(s -> Integer.parseInt(s.get("CTUSCNO").toString().substring(3))))
						.collect(Collectors.toList());
			} catch (DataAccessException e) {
				e.printStackTrace();
				log.info(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}
		} else {
			try {
				String sql = "SELECT CTUSCNO,CTNAME,CTCAT,CTSUBCAT,CTEMAILID FROM CONS WHERE  CTEMAILID  IN (' ','NA','---','0','-','--')   ORDER BY  SUBSTR(CTUSCNO,1,3)";
				log.info(sql);
				return jdbcTemplate.queryForList(sql);
			} catch (DataAccessException e) {
				e.printStackTrace();
				log.info(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}
		}

	}

	public List<Map<String, Object>> getPanCheckListDetails(HttpServletRequest request) {

		String circle = request.getParameter("circle");
		if (!circle.equals("ALL")) {
			try {
				String sql = "SELECT CTUSCNO,CTNAME,CTCAT,CTSUBCAT,ID_TYPE_CD,PER_ID_NBR \r\n"
						+ "FROM CONS,ID_TYPES\r\n"
						+ "WHERE   CTSTATUS=1 AND USCNO=CTUSCNO AND ID_TYPE_CD='PAN' AND SUBSTR(CTUSCNO,1,3)=? ORDER BY  SUBSTR(CTUSCNO,1,3)";
				log.info(sql);
				return jdbcTemplate.queryForList(sql, new Object[] { circle });
			} catch (DataAccessException e) {
				e.printStackTrace();
				log.info(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}
		} else {
			try {
				String sql = "SELECT CTUSCNO,CTNAME,CTCAT,CTSUBCAT,ID_TYPE_CD,PER_ID_NBR \r\n"
						+ "	FROM CONS,ID_TYPES\r\n"
						+ "	WHERE   CTSTATUS=1 AND USCNO=CTUSCNO AND ID_TYPE_CD='PAN' ORDER BY  SUBSTR(CTUSCNO,1,3)";
				log.info(sql);
				return jdbcTemplate.queryForList(sql);
			} catch (DataAccessException e) {
				e.printStackTrace();
				log.info(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}
		}
	}

	public List<Map<String, Object>> getMobileCheckListDetails(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		if (!circle.equals("ALL")) {
			try {
				String sql = "SELECT CTUSCNO,CTNAME,CTCAT,CTSUBCAT,CTMOBILE FROM CONS WHERE ctmobile NOT IN('0','NA','-','--','---',' ') AND SUBSTR(CTUSCNO,0,3)=? AND LENGTH(CTMOBILE)=10";
				log.info(sql);
				return jdbcTemplate.queryForList(sql, new Object[] { circle });
			} catch (DataAccessException e) {
				e.printStackTrace();
				log.info(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}
		} else {
			try {
				String sql = "SELECT CTUSCNO,CTNAME,CTCAT,CTSUBCAT,CTMOBILE FROM CONS WHERE ctmobile NOT IN('0','NA','-','--','---',' ') AND LENGTH(CTMOBILE)=10 ";
				log.info(sql);
				return jdbcTemplate.queryForList(sql);
			} catch (DataAccessException e) {
				log.info(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}
		}
	}

	public List<Map<String, Object>> getNoMobileCheckListDetails(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		if (!circle.equals("ALL")) {
			try {
				String sql = "SELECT CTUSCNO,CTNAME,CTCAT,CTSUBCAT,CTMOBILE FROM CONS WHERE ctmobile IN('0','NA','-','--','---',' ') AND SUBSTR(CTUSCNO,0,3)=? AND LENGTH(CTMOBILE)<10 ";
				log.info(sql);
				return jdbcTemplate.queryForList(sql, new Object[] { circle });
			} catch (DataAccessException e) {
				e.printStackTrace();
				log.info(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}
		} else {
			try {
				String sql = "SELECT CTUSCNO,CTNAME,CTCAT,CTSUBCAT,CTMOBILE FROM CONS WHERE ctmobile  IN('0','NA','-','--','---',' ') AND LENGTH(CTMOBILE)<10 ";
				log.info(sql);
				return jdbcTemplate.queryForList(sql);
			} catch (DataAccessException e) {
				log.info(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}
		}
	}

	public List<Map<String, Object>> getMfReportDetails(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		String monthYear = request.getParameter("month") + "-" + request.getParameter("year");

		try {
			String mfsql = "SELECT SUBSTR(MSCNO,1,3)CIRCLE ,MSCNO,TO_CHAR(MDCLRDG_DT,'MON')MON,TO_CHAR(MDCLRDG_DT,'YYYY')YEAR ,MDMF_HT MF \r\n"
					+ "FROM MTRDAT\r\n" + "WHERE MDCLKWHSTAT_HT NOT IN (4,14) \r\n" + "AND SUBSTR(MSCNO,1,3)=?\r\n"
					+ "AND TO_CHAR(MDCLRDG_DT,'MON-YYYY')=?\r\n" + "UNION ALL\r\n"
					+ "SELECT SUBSTR(MSCNO,1,3)CIRCLE ,MSCNO,TO_CHAR(MDCLRDG_DT,'MON')MON,TO_CHAR(MDCLRDG_DT,'YYYY')YEAR ,MDMF_HT MF \r\n"
					+ "FROM MTRDAT_HIST\r\n" + "WHERE MDCLKWHSTAT_HT NOT IN (4,14) \r\n"
					+ "AND SUBSTR(MSCNO,1,3)= ? \r\n" + "AND TO_CHAR(MDCLRDG_DT,'MON-YYYY')=?\r\n" + "ORDER BY MSCNO";
			log.info(mfsql);
			return jdbcTemplate.queryForList(mfsql, new Object[] { circle, monthYear, circle, monthYear }).stream()
					.sorted(Comparator.comparingInt(s -> Integer.parseInt(s.get("MSCNO").toString().substring(3))))
					.collect(Collectors.toList());
		} catch (DataAccessException e) {
			log.info(e.getMessage());
			e.printStackTrace();
			return Collections.emptyList();
		}

	}

	public List<Map<String, Object>> getMonthClosingDetails(HttpServletRequest request) {

		String monthYear = request.getParameter("month") + "-" + request.getParameter("year");
		String circle = request.getParameter("circle");
		if (circle.equalsIgnoreCase("ALL")) {

			try {
				String cbsql = "SELECT CIRCLE,USCNO,MON_YEAR,(NVL(CBTOT,0)+NVL(CB_OTH,0)+NVL(CB_CCLPC,0)) CL_BALANCE FROM LEDGER_HT\r\n"
						+ "WHERE MON_YEAR=? \r\n" + "UNION ALL\r\n"
						+ "SELECT CIRCLE,USCNO,MON_YEAR,(NVL(CBTOT,0)+NVL(CB_OTH,0)+NVL(CB_CCLPC,0)) CL_BALANCE FROM LEDGER_HT_HIST\r\n"
						+ "WHERE MON_YEAR=? \r\n" + "ORDER BY CIRCLE,USCNO";
				log.info(cbsql);
				return jdbcTemplate.queryForList(cbsql, new Object[] { monthYear, monthYear });
			} catch (DataAccessException e) {
				log.info(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}
		} else {
			try {
				String cbsql = "SELECT CIRCLE,USCNO,MON_YEAR,(NVL(CBTOT,0)+NVL(CB_OTH,0)+NVL(CB_CCLPC,0)) CL_BALANCE FROM LEDGER_HT\r\n"
						+ "WHERE MON_YEAR=? AND SUBSTR(USCNO,1,3)=?  \r\n" + "UNION ALL\r\n"
						+ "SELECT CIRCLE,USCNO,MON_YEAR,(NVL(CBTOT,0)+NVL(CB_OTH,0)+NVL(CB_CCLPC,0)) CL_BALANCE FROM LEDGER_HT_HIST\r\n"
						+ "WHERE MON_YEAR=? AND SUBSTR(USCNO,1,3)=? \r\n" + "ORDER BY CIRCLE,USCNO";
				log.info(cbsql);
				return jdbcTemplate.queryForList(cbsql, new Object[] { monthYear, circle, monthYear, circle });
			} catch (DataAccessException e) {
				log.info(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}
		}
	}

	public List<Map<String, Object>> getGridSupportingDetails(HttpServletRequest request) {

		String monthYear = request.getParameter("month") + "-" + request.getParameter("year");
		String circle = request.getParameter("circle");
		if (circle.equalsIgnoreCase("ALL")) {

			try {
				String cbsql = "select SCNO,C.CTNAME,CTCAT,CTSUBCAT, B.HT_GEN_TYPE_DESC GENERATION_TYPE, CLFC_GEN_DESC, CONTRACT_CAPACITY, INSTALLED_CAPACITY,RATE, A.FIN_YEAR  ,D.BT_GS_CHG\r\n"
						+ "from HT_GRID_SUPPORT_DETAILS A ,HT_GRID_TYPE B ,CONS C,(select * from bill union all select * from bill_hist) D where C.CTUSCNO=D.BTSCNO AND A.GEN_TYPE_ID=B.HT_GEN_TYPE_ID AND A.SCNO=C.CTUSCNO AND  D.BT_GS_CHG>0 AND TO_CHAR(BTBLDT,'MON-YYYY')=? ORDER BY SCNO";
				log.info(cbsql);
				return jdbcTemplate.queryForList(cbsql, new Object[] { monthYear });
			} catch (DataAccessException e) {
				log.info(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}
		} else {
			try {
				String cbsql = "select SCNO,C.CTNAME,CTCAT,CTSUBCAT, B.HT_GEN_TYPE_DESC GENERATION_TYPE, CLFC_GEN_DESC, CONTRACT_CAPACITY, INSTALLED_CAPACITY,RATE, A.FIN_YEAR  ,nvl(INSTALLED_CAPACITY,0) * nvl(RATE,0) BT_GS_CHG\r\n"
						+ "from HT_GRID_SUPPORT_DETAILS A ,HT_GRID_TYPE B ,CONS C,(select * from bill union all select * from bill_hist) D where C.CTUSCNO=D.BTSCNO AND A.GEN_TYPE_ID=B.HT_GEN_TYPE_ID AND A.SCNO=C.CTUSCNO AND  D.BT_GS_CHG>0 AND TO_CHAR(BTBLDT,'MON-YYYY')=? AND SUBSTR(A.SCNO,1,3)=? ORDER BY SCNO";
				log.info(cbsql);
				return jdbcTemplate.queryForList(cbsql, new Object[] { monthYear, circle });
			} catch (DataAccessException e) {
				log.info(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}
		}
	}

	public List<Map<String, Object>> getGridCircle(HttpServletRequest request) {

		String monthYear = request.getParameter("month") + "-" + request.getParameter("year");
		String circle = request.getParameter("circle");
		if (circle.equalsIgnoreCase("ALL")) {

			try {
				String cbsql = "select SUBSTR(SCNO,1,3) SCNO ,sum(BT_GS_CHG) BT_GS_CHG \r\n"
						+ "from HT_GRID_SUPPORT_DETAILS A ,HT_GRID_TYPE B ,CONS C,(select * from bill union all select * from bill_hist) D where C.CTUSCNO=D.BTSCNO AND A.GEN_TYPE_ID=B.HT_GEN_TYPE_ID AND A.SCNO=C.CTUSCNO AND  D.BT_GS_CHG>0 AND TO_CHAR(BTBLDT,'MON-YYYY')=? group by SUBSTR(SCNO,1,3) ORDER BY SCNO";
				log.info(cbsql);
				return jdbcTemplate.queryForList(cbsql, new Object[] { monthYear });
			} catch (DataAccessException e) {
				log.info(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}
		} else {
			try {
				String cbsql = "select SUBSTR(SCNO,1,3) SCNO ,sum(BT_GS_CHG) BT_GS_CHG \r\n"
						+ "from HT_GRID_SUPPORT_DETAILS A ,HT_GRID_TYPE B ,CONS C,(select * from bill union all select * from bill_hist) D where C.CTUSCNO=D.BTSCNO AND A.GEN_TYPE_ID=B.HT_GEN_TYPE_ID AND A.SCNO=C.CTUSCNO AND  D.BT_GS_CHG>0 AND TO_CHAR(BTBLDT,'MON-YYYY')=? AND SUBSTR(A.SCNO,1,3)=? group by SUBSTR(SCNO,1,3) ORDER BY SCNO";
				log.info(cbsql);
				return jdbcTemplate.queryForList(cbsql, new Object[] { monthYear, circle });
			} catch (DataAccessException e) {
				log.info(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}
		}
	}

	public List<Map<String, Object>> getGridCatCircle(HttpServletRequest request) {

		String monthYear = request.getParameter("month") + "-" + request.getParameter("year");
		String circle = request.getParameter("circle");
		if (circle.equalsIgnoreCase("ALL")) {

			try {
				String cbsql = "select SUBSTR(SCNO,1,3) SCNO ,BTBLCAT,sum(BT_GS_CHG) BT_GS_CHG \r\n"
						+ "from HT_GRID_SUPPORT_DETAILS A ,HT_GRID_TYPE B ,CONS C,(select * from bill union all select * from bill_hist) D where C.CTUSCNO=D.BTSCNO AND A.GEN_TYPE_ID=B.HT_GEN_TYPE_ID AND A.SCNO=C.CTUSCNO AND  D.BT_GS_CHG>0 AND TO_CHAR(BTBLDT,'MON-YYYY')=? group by SUBSTR(SCNO,1,3),BTBLCAT ORDER BY SCNO,BTBLCAT";
				log.info(cbsql);
				return jdbcTemplate.queryForList(cbsql, new Object[] { monthYear });
			} catch (DataAccessException e) {
				log.info(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}
		} else {
			try {
				String cbsql = "select SUBSTR(SCNO,1,3) SCNO,BTBLCAT ,sum(BT_GS_CHG) BT_GS_CHG \r\n"
						+ "from HT_GRID_SUPPORT_DETAILS A ,HT_GRID_TYPE B ,CONS C,(select * from bill union all select * from bill_hist) D where C.CTUSCNO=D.BTSCNO AND A.GEN_TYPE_ID=B.HT_GEN_TYPE_ID AND A.SCNO=C.CTUSCNO AND  D.BT_GS_CHG>0 AND TO_CHAR(BTBLDT,'MON-YYYY')=? AND SUBSTR(A.SCNO,1,3)=? group by SUBSTR(SCNO,1,3),BTBLCAT ORDER BY SCNO,BTBLCAT";
				log.info(cbsql);
				return jdbcTemplate.queryForList(cbsql, new Object[] { monthYear, circle });
			} catch (DataAccessException e) {
				log.info(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}
		}
	}

	public List<Map<String, Object>> getVirtaulBankgDetails(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		if (!circle.equalsIgnoreCase("ALL")) {
			try {
				String virtaulSql = "SELECT CTUSCNO,CIRNAME,ERONAME,SUBNAME,SECNAME ,CTNAME,CTCAT,NVL(CTBANKACNO,0)CTBANKACNO,BANK_CODE,BANK_ADD,IFSC_CODE \r\n"
						+ "FROM CONS,v_virtual_ifsc,SPDCLMASTER \r\n" + "WHERE SUBSTR(CTUSCNO,1,3)=? \r\n"
						+ "AND CTSTATUS='1'\r\n" + "AND CTBANKACNO!=0\r\n" + "AND ctuscno=v_virtual_ifsc.uscno \r\n"
						+ "AND SECCD = SUBSTR(CONS.CTSECCD,-5) \r\n" + "ORDER BY CTUSCNO";
				log.info(virtaulSql);
				return jdbcTemplate.queryForList(virtaulSql, new Object[] { circle }).stream()
						.sorted(Comparator
								.comparingInt(s -> Integer.parseInt(s.get("CTUSCNO").toString().substring(3))))
						.collect(Collectors.toList());
			} catch (DataAccessException e) {
				log.info(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}
		} else {

			try {
				String virtaulSql = "SELECT CTUSCNO,CIRNAME,ERONAME,SUBNAME,SECNAME ,CTNAME,CTCAT,NVL(CTBANKACNO,0)CTBANKACNO,BANK_CODE,BANK_ADD,IFSC_CODE \r\n"
						+ "FROM CONS,v_virtual_ifsc,SPDCLMASTER \r\n" + "WHERE CTSTATUS='1'\r\n"
						+ "AND CTBANKACNO!=0\r\n" + "AND ctuscno=v_virtual_ifsc.uscno \r\n"
						+ "AND SECCD = SUBSTR(CONS.CTSECCD,-5) \r\n" + "ORDER BY CTUSCNO";
				log.info(virtaulSql);
				return jdbcTemplate.queryForList(virtaulSql);
			} catch (DataAccessException e) {
				log.info(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}

		}
	}

	public String getSupplyDate(String uscno) {
		return jdbcTemplate.queryForObject(
				"SELECT TO_CHAR(CTSUPCONDT,'DD-MON-YYYY')CTSUPCONDT  FROM CONS WHERE CTUSCNO=?", new Object[] { uscno },
				String.class);
	}

	public List<Map<String, Object>> getNegativeCbDetails(HttpServletRequest request) {
		String monthYear = request.getParameter("month") + "-" + request.getParameter("year");
		String circle = request.getParameter("circle");
		if (circle.equalsIgnoreCase("ALL")) {
			try {
				String negativeSql = "SELECT USCNO,NAM,MON_YEAR,CBTOT FROM LEDGER_HT_HIST \r\n"
						+ "WHERE MON_YEAR=? AND CBTOT<0 \r\n" + "UNION ALL\r\n"
						+ "SELECT USCNO,NAM,MON_YEAR,CBTOT FROM LEDGER_HT \r\n" + "WHERE MON_YEAR=? AND CBTOT<0 \r\n"
						+ "ORDER BY USCNO";
				log.info(negativeSql);
				return jdbcTemplate.queryForList(negativeSql, new Object[] { monthYear, monthYear });
			} catch (DataAccessException e) {
				log.info(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}
		} else {
			try {
				String negativeSql = "SELECT USCNO,NAM,MON_YEAR,CBTOT FROM LEDGER_HT_HIST \r\n"
						+ "WHERE SUBSTR(USCNO,1,3)=?\r\n" + "AND MON_YEAR=? AND CBTOT<0 \r\n" + "UNION ALL\r\n"
						+ "SELECT USCNO,NAM,MON_YEAR,CBTOT FROM LEDGER_HT \r\n" + "WHERE SUBSTR(USCNO,1,3)=?\r\n"
						+ "AND MON_YEAR= ? AND CBTOT<0 \r\n" + "ORDER BY USCNO";
				log.info(negativeSql);
				return jdbcTemplate.queryForList(negativeSql, new Object[] { circle, monthYear, circle, monthYear });
			} catch (DataAccessException e) {
				log.info(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}

		}
	}

	public List<Map<String, Object>> getComparisonDemandReport(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		String monthYear = request.getParameter("month") + "-" + request.getParameter("year");
		log.info(circle);
		try {
			if (!circle.equals("ALL")) {
				String sql = "SELECT BTSCNO,NVL(DIVNAME,'NA')DIVNAME,CTCAT,CTSUBCAT ,ROUND(NVL(CUR_BTCMD_HT,0)) CUR_BTCMD_HT,ROUND(NVL(PRE_BTCMD_HT,0))PRE_BTCMD_HT ,ROUND(NVL(CUR_BTBLKVA_HT,0))CUR_BTBLKVA_HT,ROUND(NVL(PRE_BTBLKVA_HT,0))PRE_BTBLKVA_HT,\r\n"
						+ "        ROUND(NVL(CUR_ENGCHG,0))CUR_ENGCHG,ROUND(NVL(PRE_ENGCHG,0))PRE_ENGCHG,ROUND(NVL(CUR_CMD,0))CUR_CMD,ROUND(NVL(PRE_CMD,0))PRE_CMD,ROUND(NVL((CUR_CMD-PRE_CMD),0)) DIFF_CMD  FROM\r\n"
						+ "(SELECT BTSCNO,BTCMD_HT CUR_BTCMD_HT,BTBLKVA_HT CUR_BTBLKVA_HT,ENGCHG CUR_ENGCHG,CMD CUR_CMD FROM\r\n"
						+ "(\r\n"
						+ "SELECT BTSCNO,BTCMD_HT,BTBLKVA_HT,(BTENGCHG_NOR+BTENGCHG_PEN)ENGCHG ,(BTCURDEM+BTCOURT_LPC)CMD,BTBLDT FROM BILL\r\n"
						+ "WHERE SUBSTR(BTSCNO,0,3)=? AND TO_CHAR(BTBLDT,'MON-YYYY')=?\r\n" + "UNION ALL\r\n"
						+ "SELECT BTSCNO,BTCMD_HT,BTBLKVA_HT,(BTENGCHG_NOR+BTENGCHG_PEN)ENGCHG ,(BTCURDEM+BTCOURT_LPC)CMD,BTBLDT FROM BILL_HIST\r\n"
						+ "WHERE SUBSTR(BTSCNO,0,3)=? AND TO_CHAR(BTBLDT,'MON-YYYY')=?\r\n" + "))  C,\r\n"
						+ "(SELECT BTSCNO PRE_BTSCNO,PRE_BTCMD_HT,BTBLKVA_HT PRE_BTBLKVA_HT,ENGCHG PRE_ENGCHG,CMD PRE_CMD FROM\r\n"
						+ "(SELECT BTSCNO,BTCMD_HT PRE_BTCMD_HT,BTBLKVA_HT,(BTENGCHG_NOR+BTENGCHG_PEN)ENGCHG ,(BTCURDEM+BTCOURT_LPC)CMD,BTBLDT FROM BILL\r\n"
						+ "WHERE SUBSTR(BTSCNO,0,3)=? AND TO_CHAR(BTBLDT,'MON-YYYY')=TO_CHAR(ADD_MONTHS(TO_DATE(?,'MON-YYYY'),-1),'MON-YYYY')\r\n"
						+ "UNION ALL\r\n"
						+ "SELECT BTSCNO,BTCMD_HT PRE_BTCMD_HT,BTBLKVA_HT,(BTENGCHG_NOR+BTENGCHG_PEN)ENGCHG ,(BTCURDEM+BTCOURT_LPC)CMD,BTBLDT FROM BILL_HIST\r\n"
						+ "WHERE SUBSTR(BTSCNO,0,3)=? AND TO_CHAR(BTBLDT,'MON-YYYY')=TO_CHAR(ADD_MONTHS(TO_DATE(?,'MON-YYYY'),-1),'MON-YYYY')\r\n"
						+ ") ) P,\r\n"
						+ "(SELECT DIVNAME,CTUSCNO,CTCAT,CTSUBCAT  --CTUSCNO,CIRNAME,DIVNAME,ERONAME,SUBNAME,SECNAME\r\n"
						+ "            FROM SPDCLMASTER,CONS\r\n"
						+ "                WHERE  SECCD = SUBSTR(CONS.CTSECCD,-5 )) CON \r\n"
						+ "        WHERE C.BTSCNO=P.PRE_BTSCNO(+) AND C.BTSCNO=CON.CTUSCNO(+) ORDER BY TO_NUMBER(SUBSTR(CTUSCNO,4,8))";
				log.info(sql);
				return jdbcTemplate.queryForList(sql,
						new Object[] { circle, monthYear, circle, monthYear, circle, monthYear, circle, monthYear });
			} else {
				String sql = "SELECT BTSCNO,NVL(DIVNAME,'NA')DIVNAME,CTCAT,CTSUBCAT ,ROUND(NVL(CUR_BTCMD_HT,0)) CUR_BTCMD_HT,ROUND(NVL(PRE_BTCMD_HT,0))PRE_BTCMD_HT ,ROUND(NVL(CUR_BTBLKVA_HT,0))CUR_BTBLKVA_HT,ROUND(NVL(PRE_BTBLKVA_HT,0))PRE_BTBLKVA_HT,\r\n"
						+ "        ROUND(NVL(CUR_ENGCHG,0))CUR_ENGCHG,ROUND(NVL(PRE_ENGCHG,0))PRE_ENGCHG,ROUND(NVL(CUR_CMD,0))CUR_CMD,ROUND(NVL(PRE_CMD,0))PRE_CMD,ROUND(NVL((CUR_CMD-PRE_CMD),0)) DIFF_CMD  FROM\r\n"
						+ "(SELECT BTSCNO,BTCMD_HT CUR_BTCMD_HT,BTBLKVA_HT CUR_BTBLKVA_HT,ENGCHG CUR_ENGCHG,CMD CUR_CMD FROM\r\n"
						+ "(\r\n"
						+ "SELECT BTSCNO,BTCMD_HT,BTBLKVA_HT,(BTENGCHG_NOR+BTENGCHG_PEN)ENGCHG ,(BTCURDEM+BTCOURT_LPC)CMD,BTBLDT FROM BILL\r\n"
						+ "WHERE  TO_CHAR(BTBLDT,'MON-YYYY')=?\r\n" + "UNION ALL\r\n"
						+ "SELECT BTSCNO,BTCMD_HT,BTBLKVA_HT,(BTENGCHG_NOR+BTENGCHG_PEN)ENGCHG ,(BTCURDEM+BTCOURT_LPC)CMD,BTBLDT FROM BILL_HIST\r\n"
						+ "WHERE   TO_CHAR(BTBLDT,'MON-YYYY')=?\r\n" + "))  C,\r\n"
						+ "(SELECT BTSCNO PRE_BTSCNO,PRE_BTCMD_HT,BTBLKVA_HT PRE_BTBLKVA_HT,ENGCHG PRE_ENGCHG,CMD PRE_CMD FROM\r\n"
						+ "(SELECT BTSCNO,BTCMD_HT PRE_BTCMD_HT,BTBLKVA_HT,(BTENGCHG_NOR+BTENGCHG_PEN)ENGCHG ,(BTCURDEM+BTCOURT_LPC)CMD,BTBLDT FROM BILL\r\n"
						+ "WHERE  TO_CHAR(BTBLDT,'MON-YYYY')=TO_CHAR(ADD_MONTHS(TO_DATE(?,'MON-YYYY'),-1),'MON-YYYY')\r\n"
						+ "UNION ALL\r\n"
						+ "SELECT BTSCNO,BTCMD_HT PRE_BTCMD_HT,BTBLKVA_HT,(BTENGCHG_NOR+BTENGCHG_PEN)ENGCHG ,(BTCURDEM+BTCOURT_LPC)CMD,BTBLDT FROM BILL_HIST\r\n"
						+ "WHERE   TO_CHAR(BTBLDT,'MON-YYYY')=TO_CHAR(ADD_MONTHS(TO_DATE(?,'MON-YYYY'),-1),'MON-YYYY')\r\n"
						+ ") ) P,\r\n"
						+ "(SELECT DIVNAME,CTUSCNO,CTCAT,CTSUBCAT  --CTUSCNO,CIRNAME,DIVNAME,ERONAME,SUBNAME,SECNAME\r\n"
						+ "            FROM SPDCLMASTER,CONS\r\n"
						+ "                WHERE  SECCD = SUBSTR(CONS.CTSECCD,-5 )) CON \r\n"
						+ "        WHERE C.BTSCNO=P.PRE_BTSCNO(+) AND C.BTSCNO=CON.CTUSCNO(+) ORDER BY TO_NUMBER(SUBSTR(CTUSCNO,4,8))";
				log.info(sql);
				return jdbcTemplate.queryForList(sql, new Object[] { monthYear, monthYear, monthYear, monthYear });
			}

		} catch (DataAccessException e) {
			log.info(e.getMessage());
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	public List<Map<String, Object>> getOpenAccessSalesDetails(HttpServletRequest request) {

		String monthYear = request.getParameter("month") + "-" + request.getParameter("year");
		try {
			String sql = "SELECT OAUSCNO,CTNAME,CTCAT,NVL(CTACTUAL_KV,0)CTACTUAL_KV,ENG_SRC_TYPE,CTCMD_HT,NVL(BTRKVAH_HT,0)RECKVAH,NVL(BTRKVA_HT,0)RMD,\r\n"
					+ "(NVL(BTTOD5,0)+NVL(BTTOD2,0)) REC_PEAK_TOD,(NVL(BTTOD1,0)+NVL(BTTOD6,0)) REC_OFFPEAK_TOD,NVL(KVAH_ADJ_ENG,0)KVAH_ADJ_ENG,NVL(TOD_ADJ_PEAK,0)TOD_ADJ_PEAK,NVL(TOD_ADJ_OFF,0)TOD_ADJ_OFF ,(TOD_ADJ_PEAK-TOD_ADJ_OFF)TOD_ADJ_ENG,\r\n"
					+ "CS_CHARGES,nvl(BTMINKVAH_HT,0)MINCHG,(NVL(BTRKVAH_HT,0)-nvl(BTMINKVAH_HT,0))DIFFMIN,BTBKVAH,BTBLKVA_HT,BTTODCHG_HT,WHELL_CHARGES\r\n"
					+ "FROM open_access,CONS,BILL\r\n"
					+ "WHERE OAUSCNO=CTUSCNO AND BTSCNO=OAUSCNO AND TO_CHAR(BTBLDT,'MON-YYYY')=? AND  BILL_MON=? AND BILL_YEAR=?  AND NVL(CANCEL_FLAG,'C')='C' \r\n"
					+ "UNION ALL\r\n"
					+ "SELECT OAUSCNO,CTNAME,CTCAT,NVL(CTACTUAL_KV,0)CTACTUAL_KV,ENG_SRC_TYPE,CTCMD_HT,NVL(BTRKVAH_HT,0)RECKVAH,NVL(BTRKVA_HT,0)RMD,\r\n"
					+ "(NVL(BTTOD5,0)+NVL(BTTOD2,0)) REC_PEAK_TOD,(NVL(BTTOD1,0)+NVL(BTTOD6,0)) REC_OFFPEAK_TOD,NVL(KVAH_ADJ_ENG,0)KVAH_ADJ_ENG,NVL(TOD_ADJ_PEAK,0)TOD_ADJ_PEAK,NVL(TOD_ADJ_OFF,0)TOD_ADJ_OFF,(TOD_ADJ_PEAK-TOD_ADJ_OFF)TOD_ADJ_ENG,\r\n"
					+ "CS_CHARGES,nvl(BTMINKVAH_HT,0)MINCHG,BTBKVAH,(NVL(BTRKVAH_HT,0)-nvl(BTMINKVAH_HT,0))DIFFMIN,BTBLKVA_HT,BTTODCHG_HT,WHELL_CHARGES\r\n"
					+ "FROM open_access_HIST,CONS,BILL_HIST\r\n"
					+ "WHERE OAUSCNO=CTUSCNO AND BTSCNO=OAUSCNO AND TO_CHAR(BTBLDT,'MON-YYYY')=? AND BILL_MON=? AND BILL_YEAR=?  AND NVL(CANCEL_FLAG,'C')='C' \r\n"
					+ "ORDER BY CTACTUAL_KV";
			log.info(sql);
			return jdbcTemplate.queryForList(sql,
					new Object[] { monthYear, request.getParameter("month").trim(), request.getParameter("year").trim(),
							monthYear, request.getParameter("month").trim(), request.getParameter("year").trim() });
		} catch (DataAccessException e) {
			e.printStackTrace();
			log.info(e.getMessage());
			e.printStackTrace();
			return Collections.emptyList();
		}

	}

	public List<Map<String, Object>> getSectionWiseSplitDemandDetails(HttpServletRequest request) {
		String monthYear = request.getParameter("month") + "-" + request.getParameter("year");
		String circle = request.getParameter("circle");

		if (circle.equals("ALL")) {
			try {
				String sql = "SELECT CIRNAME,SECNAME,SUM(BTBLKVA_HT)BTBLKVA_HT,SUM(BTBKVAH)BTBKVAH,SUM(BTDEMCHG_NOR)BTDEMCHG_NOR,SUM(BTENGCHG_NOR)BTENGCHG_NOR,SUM(BTDEMCHG_PEN)BTDEMCHG_PEN,SUM(BTENGCHG_PEN)BTENGCHG_PEN,SUM(BTVOLTSURCHG)BTVOLTSURCHG,SUM(BTOTHERCHG)BTOTHERCHG,SUM(BTED)BTED,SUM(BTED_INT)BTED_INT,SUM(btadlchg)btadlchg,SUM(BTCUSTCHG)BTCUSTCHG,SUM(NVL(BTFSACHG,0))BTFSACHG,\r\n"
						+ "SUM(NVL(BTDTRHIRE_CHG,0))BTDTRHIRE_CHG,SUM(BTCURDEM)BTCURDEM,SUM(BTTODCHG_HT) BTTODCHG_HT,SUM(BTCOURT_LPC) BTCOURT_LPC,SUM(BTACDSURCHG) BTACDSURCHG,SUM(nvl(BT_TU_CHG,0)+nvl(BT_LTU_CHG,0)) BT_TU_CHG\r\n"
						+ "FROM BILL,SPDCLMASTER \r\n" + "WHERE SECCD=SUBSTR(BTSECCD,-5) \r\n"
						+ "AND TO_CHAR(BTBLDT,'MON-YYYY')=? \r\n" + "GROUP BY SECNAME,CIRNAME\r\n" + "UNION ALL\r\n"
						+ "SELECT CIRNAME,SECNAME,SUM(BTBLKVA_HT)BTBLKVA_HT,SUM(BTBKVAH)BTBKVAH,SUM(BTDEMCHG_NOR)BTDEMCHG_NOR,SUM(BTENGCHG_NOR)BTENGCHG_NOR,SUM(BTDEMCHG_PEN)BTDEMCHG_PEN,SUM(BTENGCHG_PEN)BTENGCHG_PEN,SUM(BTVOLTSURCHG)BTVOLTSURCHG,SUM(BTOTHERCHG)BTOTHERCHG,SUM(BTED)BTED,SUM(BTED_INT)BTED_INT,SUM(btadlchg)btadlchg,SUM(BTCUSTCHG)BTCUSTCHG,SUM(NVL(BTFSACHG,0))BTFSACHG,\r\n"
						+ "SUM(NVL(BTDTRHIRE_CHG,0))BTDTRHIRE_CHG,SUM(BTCURDEM)BTCURDEM,SUM(BTTODCHG_HT) BTTODCHG_HT,SUM(BTCOURT_LPC) BTCOURT_LPC,SUM(BTACDSURCHG) BTACDSURCHG,SUM(nvl(BT_TU_CHG,0)+nvl(BT_LTU_CHG,0)) BT_TU_CHG\r\n"
						+ "FROM BILL_HIST,SPDCLMASTER \r\n" + "WHERE SECCD=SUBSTR(BTSECCD,-5)\r\n"
						+ "AND TO_CHAR(BTBLDT,'MON-YYYY')=?\r\n" + "GROUP BY SECNAME,CIRNAME\r\n" + "ORDER BY SECNAME";
				log.info(sql);
				return jdbcTemplate.queryForList(sql, new Object[] { monthYear, monthYear });
			} catch (DataAccessException e) {
				log.info(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}
		} else {

			try {
				String sql = "SELECT CIRNAME,SECNAME,SUM(BTBLKVA_HT)BTBLKVA_HT,SUM(BTBKVAH)BTBKVAH,SUM(BTDEMCHG_NOR)BTDEMCHG_NOR,SUM(BTENGCHG_NOR)BTENGCHG_NOR,SUM(BTDEMCHG_PEN)BTDEMCHG_PEN,SUM(BTENGCHG_PEN)BTENGCHG_PEN,SUM(BTVOLTSURCHG)BTVOLTSURCHG,SUM(BTOTHERCHG)BTOTHERCHG,SUM(BTED)BTED,SUM(BTED_INT)BTED_INT,SUM(btadlchg)btadlchg,SUM(BTCUSTCHG)BTCUSTCHG,SUM(NVL(BTFSACHG,0))BTFSACHG,\r\n"
						+ "SUM(NVL(BTDTRHIRE_CHG,0))BTDTRHIRE_CHG,SUM(BTCURDEM)BTCURDEM,SUM(BTTODCHG_HT) BTTODCHG_HT,SUM(BTCOURT_LPC) BTCOURT_LPC,SUM(BTACDSURCHG) BTACDSURCHG,SUM(nvl(BT_TU_CHG,0)+nvl(BT_LTU_CHG,0)) BT_TU_CHG\r\n"
						+ "FROM BILL,SPDCLMASTER \r\n" + "WHERE SECCD=SUBSTR(BTSECCD,-5) \r\n"
						+ "AND TO_CHAR(BTBLDT,'MON-YYYY')=? \r\n" + "AND SUBSTR(BTSCNO,1,3)=? \r\n"
						+ "GROUP BY SECNAME,CIRNAME\r\n" + "UNION ALL\r\n"
						+ "SELECT CIRNAME,SECNAME,SUM(BTBLKVA_HT)BTBLKVA_HT,SUM(BTBKVAH)BTBKVAH,SUM(BTDEMCHG_NOR)BTDEMCHG_NOR,SUM(BTENGCHG_NOR)BTENGCHG_NOR,SUM(BTDEMCHG_PEN)BTDEMCHG_PEN,SUM(BTENGCHG_PEN)BTENGCHG_PEN,SUM(BTVOLTSURCHG)BTVOLTSURCHG,SUM(BTOTHERCHG)BTOTHERCHG,SUM(BTED)BTED,SUM(BTED_INT)BTED_INT,SUM(btadlchg)btadlchg,SUM(BTCUSTCHG)BTCUSTCHG,SUM(NVL(BTFSACHG,0))BTFSACHG,\r\n"
						+ "SUM(NVL(BTDTRHIRE_CHG,0))BTDTRHIRE_CHG,SUM(BTCURDEM)BTCURDEM,SUM(BTTODCHG_HT) BTTODCHG_HT,SUM(BTCOURT_LPC) BTCOURT_LPC,SUM(BTACDSURCHG) BTACDSURCHG,SUM(nvl(BT_TU_CHG,0)+nvl(BT_LTU_CHG,0)) BT_TU_CHG \r\n"
						+ "FROM BILL_HIST,SPDCLMASTER \r\n" + "WHERE SECCD=SUBSTR(BTSECCD,-5)\r\n"
						+ "AND TO_CHAR(BTBLDT,'MON-YYYY')=?\r\n" + "AND SUBSTR(BTSCNO,1,3)=?\r\n"
						+ "GROUP BY SECNAME,CIRNAME\r\n" + "ORDER BY SECNAME";
				log.info(sql);
				return jdbcTemplate.queryForList(sql, new Object[] { monthYear, circle, monthYear, circle });
			} catch (DataAccessException e) {
				log.info(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}
		}

	}

	public List<Map<String, Object>> getCateWiseSplitDemandDetails(HttpServletRequest request) {
		String monthYear = request.getParameter("month") + "-" + request.getParameter("year");
		String circle = request.getParameter("circle");
		try {
			if (!circle.equalsIgnoreCase("ALL")) {
				String sql = " SELECT SUBSTR(BTSCNO,1,3) CIRCLE,BTBLCAT,SUM(BTBLKVA_HT)BTBLKVA_HT,SUM(BTBKVAH)BTBKVAH,SUM(BTDEMCHG_NOR)BTDEMCHG_NOR,SUM(BTENGCHG_NOR)BTENGCHG_NOR,SUM(BTDEMCHG_PEN)BTDEMCHG_PEN,SUM(BTENGCHG_PEN)BTENGCHG_PEN,SUM(BTVOLTSURCHG)BTVOLTSURCHG,SUM(BTOTHERCHG)BTOTHERCHG,SUM(BTED)BTED,SUM(BTED_INT)BTED_INT,SUM(btadlchg)btadlchg,SUM(BTCUSTCHG)BTCUSTCHG,SUM(NVL(BTFSACHG,0))BTFSACHG, "
						+ " SUM(NVL(BTDTRHIRE_CHG,0))BTDTRHIRE_CHG,SUM(BTCURDEM)BTCURDEM,SUM(BTCOURT_LPC)BTCOURT_LPC "
						+ " FROM BILL_HIST " + " WHERE TO_CHAR(BTBLDT,'MON-YYYY')=? " + " AND SUBSTR(BTSCNO,1,3)=? "
						+ " GROUP BY BTBLCAT,SUBSTR(BTSCNO,1,3) " + " UNION ALL "
						+ " SELECT SUBSTR(BTSCNO,1,3) CIRCLE,BTBLCAT,SUM(BTBLKVA_HT)BTBLKVA_HT,SUM(BTBKVAH)BTBKVAH,SUM(BTDEMCHG_NOR)BTDEMCHG_NOR,SUM(BTENGCHG_NOR)BTENGCHG_NOR,SUM(BTDEMCHG_PEN)BTDEMCHG_PEN,SUM(BTENGCHG_PEN)BTENGCHG_PEN,SUM(BTVOLTSURCHG)BTVOLTSURCHG,SUM(BTOTHERCHG)BTOTHERCHG,SUM(BTED)BTED,SUM(BTED_INT)BTED_INT,SUM(btadlchg)btadlchg,SUM(BTCUSTCHG)BTCUSTCHG,SUM(NVL(BTFSACHG,0))BTFSACHG, "
						+ " SUM(NVL(BTDTRHIRE_CHG,0))BTDTRHIRE_CHG,SUM(BTCURDEM)BTCURDEM ,SUM(BTCOURT_LPC)BTCOURT_LPC "
						+ " FROM BILL " + " WHERE TO_CHAR(BTBLDT,'MON-YYYY')=? " + " AND SUBSTR(BTSCNO,1,3)=? "
						+ " GROUP BY BTBLCAT,SUBSTR(BTSCNO,1,3) " + " ORDER BY BTBLCAT,CIRCLE";
				log.info(sql);
				return jdbcTemplate.queryForList(sql, new Object[] { monthYear, circle, monthYear, circle });
			} else {

				String sql = " SELECT SUBSTR(BTSCNO,1,3) CIRCLE,BTBLCAT,SUM(BTBLKVA_HT)BTBLKVA_HT,SUM(BTBKVAH)BTBKVAH,SUM(BTDEMCHG_NOR)BTDEMCHG_NOR,SUM(BTENGCHG_NOR)BTENGCHG_NOR,SUM(BTDEMCHG_PEN)BTDEMCHG_PEN,SUM(BTENGCHG_PEN)BTENGCHG_PEN,SUM(BTVOLTSURCHG)BTVOLTSURCHG,SUM(BTOTHERCHG)BTOTHERCHG,SUM(BTED)BTED,SUM(BTED_INT)BTED_INT,SUM(btadlchg)btadlchg,SUM(BTCUSTCHG)BTCUSTCHG,SUM(NVL(BTFSACHG,0))BTFSACHG,"
						+ " SUM(NVL(BTDTRHIRE_CHG,0))BTDTRHIRE_CHG,SUM(BTCURDEM)BTCURDEM ,SUM(BTCOURT_LPC)BTCOURT_LPC "
						+ " FROM BILL_HIST " + " WHERE TO_CHAR(BTBLDT,'MON-YYYY')= ? "
						+ " GROUP BY BTBLCAT,SUBSTR(BTSCNO,1,3) " + " UNION ALL "
						+ " SELECT SUBSTR(BTSCNO,1,3) CIRCLE,BTBLCAT,SUM(BTBLKVA_HT)BTBLKVA_HT,SUM(BTBKVAH)BTBKVAH,SUM(BTDEMCHG_NOR)BTDEMCHG_NOR,SUM(BTENGCHG_NOR)BTENGCHG_NOR,SUM(BTDEMCHG_PEN)BTDEMCHG_PEN,SUM(BTENGCHG_PEN)BTENGCHG_PEN,SUM(BTVOLTSURCHG)BTVOLTSURCHG,SUM(BTOTHERCHG)BTOTHERCHG,SUM(BTED)BTED,SUM(BTED_INT)BTED_INT,SUM(btadlchg)btadlchg,SUM(BTCUSTCHG)BTCUSTCHG,SUM(NVL(BTFSACHG,0))BTFSACHG, "
						+ " SUM(NVL(BTDTRHIRE_CHG,0))BTDTRHIRE_CHG,SUM(BTCURDEM)BTCURDEM ,SUM(BTCOURT_LPC)BTCOURT_LPC "
						+ " FROM BILL " + " WHERE TO_CHAR(BTBLDT,'MON-YYYY')= ? "
						+ " GROUP BY BTBLCAT,SUBSTR(BTSCNO,1,3) " + " ORDER BY BTBLCAT,CIRCLE";
				log.info(sql);
				return jdbcTemplate.queryForList(sql, new Object[] { monthYear, monthYear });
			}

		} catch (DataAccessException e) {
			log.info(e.getMessage());
			e.printStackTrace();
			return Collections.emptyList();
		}

	}

	public List<Map<String, Object>> getSubDivisionWiseSplitDemandDetails(HttpServletRequest request) {
		String monthYear = request.getParameter("month") + "-" + request.getParameter("year");
		String circle = request.getParameter("circle");
		if (circle.equals("ALL")) {
			try {
				String sql = "SELECT CIRNAME,SUBNAME,SUM(BTBLKVA_HT)BTBLKVA_HT,SUM(BTBKVAH)BTBKVAH,SUM(BTDEMCHG_NOR)BTDEMCHG_NOR,SUM(BTENGCHG_NOR)BTENGCHG_NOR,SUM(BTDEMCHG_PEN)BTDEMCHG_PEN,SUM(BTENGCHG_PEN)BTENGCHG_PEN,SUM(BTVOLTSURCHG)BTVOLTSURCHG,SUM(BTOTHERCHG)BTOTHERCHG,SUM(BTED)BTED,SUM(BTED_INT)BTED_INT,SUM(btadlchg)btadlchg,SUM(BTCUSTCHG)BTCUSTCHG,SUM(NVL(BTFSACHG,0))BTFSACHG,\r\n"
						+ "SUM(NVL(BTDTRHIRE_CHG,0))BTDTRHIRE_CHG,SUM(BTCURDEM)BTCURDEM,SUM(BTTODCHG_HT) BTTODCHG_HT,SUM(BTCOURT_LPC) BTCOURT_LPC,SUM(BTACDSURCHG) BTACDSURCHG, SUM(NVL(BT_TU_CHG,0)+nvl(BT_LTU_CHG,0)) BT_TU_CHG \r\n"
						+ "FROM BILL,SPDCLMASTER \r\n" + "WHERE SECCD=SUBSTR(BTSECCD,-5) \r\n"
						+ "AND TO_CHAR(BTBLDT,'MON-YYYY')=?\r\n" + "GROUP BY SUBNAME,CIRNAME\r\n" + "UNION ALL\r\n"
						+ "SELECT CIRNAME,SUBNAME,SUM(BTBLKVA_HT)BTBLKVA_HT,SUM(BTBKVAH)BTBKVAH,SUM(BTDEMCHG_NOR)BTDEMCHG_NOR,SUM(BTENGCHG_NOR)BTENGCHG_NOR,SUM(BTDEMCHG_PEN)BTDEMCHG_PEN,SUM(BTENGCHG_PEN)BTENGCHG_PEN,SUM(BTVOLTSURCHG)BTVOLTSURCHG,SUM(BTOTHERCHG)BTOTHERCHG,SUM(BTED)BTED,SUM(BTED_INT)BTED_INT,SUM(btadlchg)btadlchg,SUM(BTCUSTCHG)BTCUSTCHG,SUM(NVL(BTFSACHG,0))BTFSACHG,\r\n"
						+ "SUM(NVL(BTDTRHIRE_CHG,0))BTDTRHIRE_CHG,SUM(BTCURDEM)BTCURDEM,SUM(BTTODCHG_HT) BTTODCHG_HT,SUM(BTCOURT_LPC) BTCOURT_LPC,SUM(BTACDSURCHG) BTACDSURCHG, SUM(NVL(BT_TU_CHG,0)+nvl(BT_LTU_CHG,0)) BT_TU_CHG \r\n"
						+ "FROM BILL_HIST,SPDCLMASTER \r\n" + "WHERE SECCD=SUBSTR(BTSECCD,-5)\r\n"
						+ "AND TO_CHAR(BTBLDT,'MON-YYYY')=?\r\n" + "GROUP BY SUBNAME,CIRNAME\r\n" + "ORDER BY SUBNAME";
				log.info(sql);
				return jdbcTemplate.queryForList(sql, monthYear, monthYear);
			} catch (DataAccessException e) {
				log.info(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}
		} else {
			try {
				String sql = "SELECT CIRNAME,SUBNAME,SUM(BTBLKVA_HT)BTBLKVA_HT,SUM(BTBKVAH)BTBKVAH,SUM(BTDEMCHG_NOR)BTDEMCHG_NOR,SUM(BTENGCHG_NOR)BTENGCHG_NOR,SUM(BTDEMCHG_PEN)BTDEMCHG_PEN,SUM(BTENGCHG_PEN)BTENGCHG_PEN,SUM(BTVOLTSURCHG)BTVOLTSURCHG,SUM(BTOTHERCHG)BTOTHERCHG,SUM(BTED)BTED,SUM(BTED_INT)BTED_INT,SUM(btadlchg)btadlchg,SUM(BTCUSTCHG)BTCUSTCHG,SUM(NVL(BTFSACHG,0))BTFSACHG,\r\n"
						+ "SUM(NVL(BTDTRHIRE_CHG,0))BTDTRHIRE_CHG,SUM(BTCURDEM)BTCURDEM,SUM(BTTODCHG_HT) BTTODCHG_HT,SUM(BTCOURT_LPC) BTCOURT_LPC,SUM(BTACDSURCHG) BTACDSURCHG, SUM(NVL(BT_TU_CHG,0)+nvl(BT_LTU_CHG,0)) BT_TU_CHG \r\n"
						+ "FROM BILL,SPDCLMASTER \r\n" + "WHERE SECCD=SUBSTR(BTSECCD,-5) \r\n"
						+ "AND TO_CHAR(BTBLDT,'MON-YYYY')=?\r\n" + "AND SUBSTR(BTSCNO,1,3)=?\r\n"
						+ "GROUP BY SUBNAME,CIRNAME\r\n" + "UNION ALL\r\n"
						+ "SELECT CIRNAME,SUBNAME,SUM(BTBLKVA_HT)BTBLKVA_HT,SUM(BTBKVAH)BTBKVAH,SUM(BTDEMCHG_NOR)BTDEMCHG_NOR,SUM(BTENGCHG_NOR)BTENGCHG_NOR,SUM(BTDEMCHG_PEN)BTDEMCHG_PEN,SUM(BTENGCHG_PEN)BTENGCHG_PEN,SUM(BTVOLTSURCHG)BTVOLTSURCHG,SUM(BTOTHERCHG)BTOTHERCHG,SUM(BTED)BTED,SUM(BTED_INT)BTED_INT,SUM(btadlchg)btadlchg,SUM(BTCUSTCHG)BTCUSTCHG,SUM(NVL(BTFSACHG,0))BTFSACHG,\r\n"
						+ "SUM(NVL(BTDTRHIRE_CHG,0))BTDTRHIRE_CHG,SUM(BTCURDEM)BTCURDEM,SUM(BTTODCHG_HT) BTTODCHG_HT,SUM(BTCOURT_LPC) BTCOURT_LPC,SUM(BTACDSURCHG) BTACDSURCHG, SUM(NVL(BT_TU_CHG,0)+nvl(BT_LTU_CHG,0)) BT_TU_CHG \r\n"
						+ "FROM BILL_HIST,SPDCLMASTER \r\n" + "WHERE SECCD=SUBSTR(BTSECCD,-5)\r\n"
						+ "AND TO_CHAR(BTBLDT,'MON-YYYY')=?\r\n" + "AND SUBSTR(BTSCNO,1,3)=?\r\n"
						+ "GROUP BY SUBNAME,CIRNAME\r\n" + "ORDER BY SUBNAME";
				log.info(sql);
				return jdbcTemplate.queryForList(sql, monthYear, circle, monthYear, circle);
			} catch (DataAccessException e) {
				log.info(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}

		}
	}

	public List<Map<String, Object>> getLowVoltageSurchargeDetails(HttpServletRequest request) {
		String monthYear = request.getParameter("month") + "-" + request.getParameter("year");
		try {
			String lowSql = "SELECT BTSCNO HTSCNO,CTNAME NAME,BTCMD_HT CMD,BTBLCAT CAT,BTBLSCAT SUBCAT,BTACTUAL_KV VOLTAGE,CTINDFEEDFLAG_HT FEEDER_TYPE,BTRKVA_HT RMD,BTBLKVA_HT BMD, BTRKVAH_HT RKVAH,BTBKVAH BKVAH, BTVOLTSURCHG LOW_VOLTAGE_SURCHARGE \r\n"
					+ "FROM CONS,BILL \r\n" + "WHERE CTUSCNO=BTSCNO\r\n" + "AND TO_CHAR(BTBLDT,'MON-YYYY')=?\r\n"
					+ "AND BTVOLTSURCHG!=0\r\n" + "UNION ALL\r\n"
					+ "SELECT BTSCNO HTSCNO,CTNAME NAME,BTCMD_HT CMD,BTBLCAT CAT,BTBLSCAT SUBCAT,BTACTUAL_KV VOLTAGE,CTINDFEEDFLAG_HT FEEDER_TYPE,BTRKVA_HT RMD,BTBLKVA_HT BMD, BTRKVAH_HT RKVAH,BTBKVAH BKVAH, BTVOLTSURCHG LOW_VOLTAGE_SURCHARGE \r\n"
					+ "FROM CONS,BILL_HIST \r\n" + "WHERE CTUSCNO=BTSCNO\r\n" + "AND TO_CHAR(BTBLDT,'MON-YYYY')=?\r\n"
					+ "AND BTVOLTSURCHG!=0\r\n" + "ORDER BY HTSCNO";
			log.info(lowSql);
			return jdbcTemplate.queryForList(lowSql, new Object[] { monthYear, monthYear });
		} catch (DataAccessException e) {
			log.info(e.getMessage());
			e.printStackTrace();
			return Collections.emptyList();
		}

	}

	public List<Map<String, Object>> getServiceWiseEDDetails(HttpServletRequest request) {
		String monthYear = request.getParameter("month") + "-" + request.getParameter("year");
		String circle = request.getParameter("circle");
		if (circle.equals("ALL")) {

			try {
				String edSql = "SELECT BTSCNO,CTNAME,CIRNAME,DIVNAME,SUBNAME,SECNAME,DECODE(CTGOVT_PVT,'Y','GOVT','N','PVT')TYPE,BTED,BTED_INT,BTBLCAT || '-' ||BTBLSCAT CAT,BTEDKVAH FROM BILL,CONS,SPDCLMASTER \r\n"
						+ "WHERE BTSCNO=CTUSCNO\r\n" + "AND TO_CHAR(BTBLDT,'MON-YYYY')=?\r\n"
						+ "AND SECCD=SUBSTR(BTSECCD,-5)\r\n" + "UNION ALL\r\n"
						+ "SELECT BTSCNO,CTNAME,CIRNAME,DIVNAME,SUBNAME,SECNAME,DECODE(CTGOVT_PVT,'Y','GOVT','N','PVT')TYPE,BTED,BTED_INT,BTBLCAT || '-' ||BTBLSCAT CAT,BTEDKVAH FROM BILL_HIST,CONS,SPDCLMASTER \r\n"
						+ "WHERE BTSCNO=CTUSCNO\r\n" + "AND TO_CHAR(BTBLDT,'MON-YYYY')=?\r\n"
						+ "AND SECCD=SUBSTR(BTSECCD,-5)\r\n" + "ORDER BY BTSCNO";
				log.info(edSql);
				return jdbcTemplate.queryForList(edSql, new Object[] { monthYear, monthYear });
			} catch (DataAccessException e) {
				log.info(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}
		} else {
			try {
				String edSql = "SELECT BTSCNO,CTNAME,CIRNAME,DIVNAME,SUBNAME,SECNAME,DECODE(CTGOVT_PVT,'Y','GOVT','N','PVT')TYPE,BTED,BTED_INT,BTBLCAT || '-' ||BTBLSCAT CAT,BTEDKVAH FROM BILL,CONS,SPDCLMASTER \r\n"
						+ "WHERE BTSCNO=CTUSCNO\r\n" + "AND SUBSTR(BTSCNO,1,3)=?\r\n"
						+ "AND TO_CHAR(BTBLDT,'MON-YYYY')=?\r\n" + "AND SECCD=SUBSTR(BTSECCD,-5)\r\n" + "UNION ALL\r\n"
						+ "SELECT BTSCNO,CTNAME,CIRNAME,DIVNAME,SUBNAME,SECNAME,DECODE(CTGOVT_PVT,'Y','GOVT','N','PVT')TYPE,BTED,BTED_INT,BTBLCAT || '-' ||BTBLSCAT CAT,BTEDKVAH FROM BILL_HIST,CONS,SPDCLMASTER \r\n"
						+ "WHERE BTSCNO=CTUSCNO\r\n" + "AND SUBSTR(BTSCNO,1,3)=?\r\n"
						+ "AND TO_CHAR(BTBLDT,'MON-YYYY')=?\r\n" + "AND SECCD=SUBSTR(BTSECCD,-5)\r\n"
						+ "ORDER BY BTSCNO";
				log.info(edSql);
				return jdbcTemplate.queryForList(edSql, new Object[] { circle, monthYear, circle, monthYear });
			} catch (DataAccessException e) {
				log.info(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}

		}

	}

	public List<Map<String, Object>> getDailyCollectionDetails(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		String fromDate = request.getParameter("fromdate");
		String toDate = request.getParameter("todate");
		if (circle.equals("ALL")) {
			try {
				String dailsql = "SELECT  * FROM (\r\n"
						+ "SELECT USCNO,TO_CHAR(PAY_DATE,'DD-MON-YYYY')PAY_DATE,SAP_NO,SAP_DT, BTCURDEM, PCMD,PRC,PACD,TOTAL_AMT,PAY_DATE TEMP FROM PAY_HT,BILL WHERE USCNO=BTSCNO(+)   AND TRIM(PAY_STA_FLG)='C'  AND TO_CHAR(PAY_DATE,'MON-YYYY')=TO_CHAR(BTBLDT(+),'MON-YYYY')\r\n"
						+ "UNION ALL\r\n"
						+ "SELECT USCNO,TO_CHAR(PAY_DATE,'DD-MON-YYYY')PAY_DATE,SAP_NO,SAP_DT, BTCURDEM, PCMD,PRC,PACD,TOTAL_AMT,PAY_DATE TEMP FROM PAY_HT_HIST,BILL_HIST WHERE USCNO=BTSCNO(+)  AND TRIM(PAY_STA_FLG)='C'  AND TO_CHAR(PAY_DATE,'MON-YYYY')=TO_CHAR(BTBLDT(+),'MON-YYYY')\r\n"
						+ ")WHERE  TEMP BETWEEN TO_DATE(?,'DD-MM-YYYY') AND TO_DATE(?,'DD-MM-YYYY')  ORDER BY USCNO";
				log.info(dailsql);
				return jdbcTemplate.queryForList(dailsql, new Object[] { fromDate, toDate });
			} catch (DataAccessException e) {
				log.info(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}

		} else {
			try {
				String dailsql = "SELECT  * FROM (\r\n"
						+ "SELECT USCNO,TO_CHAR(PAY_DATE,'DD-MON-YYYY')PAY_DATE,SAP_NO,SAP_DT, BTCURDEM, PCMD,PRC,PACD,TOTAL_AMT,PAY_DATE TEMP FROM PAY_HT,BILL WHERE USCNO=BTSCNO(+)   AND TRIM(PAY_STA_FLG)='C'  AND TO_CHAR(PAY_DATE,'MON-YYYY')=TO_CHAR(BTBLDT(+),'MON-YYYY')\r\n"
						+ "UNION ALL\r\n"
						+ "SELECT USCNO,TO_CHAR(PAY_DATE,'DD-MON-YYYY')PAY_DATE,SAP_NO,SAP_DT, BTCURDEM, PCMD,PRC,PACD,TOTAL_AMT,PAY_DATE TEMP FROM PAY_HT_HIST,BILL_HIST WHERE USCNO=BTSCNO(+)  AND TRIM(PAY_STA_FLG)='C'  AND TO_CHAR(PAY_DATE,'MON-YYYY')=TO_CHAR(BTBLDT(+),'MON-YYYY')\r\n"
						+ ")WHERE  TEMP BETWEEN TO_DATE(?,'DD-MM-YYYY') AND TO_DATE(?,'DD-MM-YYYY') AND SUBSTR(USCNO,0,3) = ? ORDER BY PAY_DATE";
				log.info(dailsql);
				return jdbcTemplate.queryForList(dailsql, new Object[] { fromDate, toDate, circle });
			} catch (DataAccessException e) {
				log.info(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}
		}
	}

	public List<Map<String, Object>> getAmrDetails(HttpServletRequest request) {

		String circle = request.getParameter("circle");
		String monthYear = request.getParameter("month") + "-" + request.getParameter("year");
		if (circle.equals("ALL")) {
			try {
				String amrall = "SELECT MSCNO,CTNAME,CTCAT,MDMF_HT,MDOPNRDG_DT,MDCLRDG_DT,MDOPNKWH_HT,MDCLKWH_HT,MDRKWH_HT,MDRECKWH_HT, MDOPNKVAH_HT,MDCLKVAH_HT,MDRKVAH_HT,MDRECKVAH_HT,MDRECKVA_HT, MDTOD1_OPN,MDTOD1_CLS,MDTOD1_RKVAH,MDTOD1_RECKVAH, MDTOD2_OPN,MDTOD2_CLS,MDTOD2_RKVAH,MDTOD2_RECKVAH, MDTOD5_OPN,MDTOD5_CLS,MDTOD5_RKVAH,MDTOD5_RECKVAH, MDTOD6_OPN,MDTOD6_CLS,MDTOD6_RKVAH,MDTOD6_RECKVAH,MDAMR_FLAG FROM MTRDAT,CONS WHERE  CTUSCNO=MSCNO AND MDAMR_FLAG='AMR' AND  TO_CHAR(MDMONTH,'MON-YYYY')=?"
						+ "UNION ALL "
						+ "SELECT MSCNO,CTNAME,CTCAT,MDMF_HT,MDOPNRDG_DT,MDCLRDG_DT,MDOPNKWH_HT,MDCLKWH_HT,MDRKWH_HT,MDRECKWH_HT, MDOPNKVAH_HT,MDCLKVAH_HT,MDRKVAH_HT,MDRECKVAH_HT,MDRECKVA_HT, MDTOD1_OPN,MDTOD1_CLS,MDTOD1_RKVAH,MDTOD1_RECKVAH, MDTOD2_OPN,MDTOD2_CLS,MDTOD2_RKVAH,MDTOD2_RECKVAH, MDTOD5_OPN,MDTOD5_CLS,MDTOD5_RKVAH,MDTOD5_RECKVAH, MDTOD6_OPN,MDTOD6_CLS,MDTOD6_RKVAH,MDTOD6_RECKVAH,MDAMR_FLAG FROM MTRDAT_HIST,CONS WHERE  CTUSCNO=MSCNO AND MDAMR_FLAG='AMR' AND  TO_CHAR(MDMONTH,'MON-YYYY')=?";
				log.info(amrall);
				return jdbcTemplate.queryForList(amrall, new Object[] { monthYear, monthYear });
			} catch (DataAccessException e) {
				log.info(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}
		} else {
			try {
				String amrall = "SELECT MSCNO,CTNAME,CTCAT,MDMF_HT,MDOPNRDG_DT,MDCLRDG_DT,MDOPNKWH_HT,MDCLKWH_HT,MDRKWH_HT,MDRECKWH_HT, MDOPNKVAH_HT,MDCLKVAH_HT,MDRKVAH_HT,MDRECKVAH_HT,MDRECKVA_HT, MDTOD1_OPN,MDTOD1_CLS,MDTOD1_RKVAH,MDTOD1_RECKVAH, MDTOD2_OPN,MDTOD2_CLS,MDTOD2_RKVAH,MDTOD2_RECKVAH, MDTOD5_OPN,MDTOD5_CLS,MDTOD5_RKVAH,MDTOD5_RECKVAH, MDTOD6_OPN,MDTOD6_CLS,MDTOD6_RKVAH,MDTOD6_RECKVAH,MDAMR_FLAG FROM MTRDAT,CONS WHERE  CTUSCNO=MSCNO AND MDAMR_FLAG='AMR' AND  TO_CHAR(MDMONTH,'MON-YYYY')=? AND SUBSTR(MSCNO,0,3)=? "
						+ "UNION ALL "
						+ "SELECT MSCNO,CTNAME,CTCAT,MDMF_HT,MDOPNRDG_DT,MDCLRDG_DT,MDOPNKWH_HT,MDCLKWH_HT,MDRKWH_HT,MDRECKWH_HT, MDOPNKVAH_HT,MDCLKVAH_HT,MDRKVAH_HT,MDRECKVAH_HT,MDRECKVA_HT, MDTOD1_OPN,MDTOD1_CLS,MDTOD1_RKVAH,MDTOD1_RECKVAH, MDTOD2_OPN,MDTOD2_CLS,MDTOD2_RKVAH,MDTOD2_RECKVAH, MDTOD5_OPN,MDTOD5_CLS,MDTOD5_RKVAH,MDTOD5_RECKVAH, MDTOD6_OPN,MDTOD6_CLS,MDTOD6_RKVAH,MDTOD6_RECKVAH,MDAMR_FLAG FROM MTRDAT_HIST,CONS WHERE  CTUSCNO=MSCNO AND MDAMR_FLAG='AMR' AND  TO_CHAR(MDMONTH,'MON-YYYY')=? AND SUBSTR(MSCNO,0,3)=? ";
				log.info(amrall);
				return jdbcTemplate.queryForList(amrall, new Object[] { monthYear, circle, monthYear, circle });
			} catch (DataAccessException e) {
				log.info(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}
		}

	}

	public List<Map<String, Object>> getLedgerFreezDetails() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Map<String, Object>> getDlistDetails(HttpServletRequest request, String demand, String ob) {

		try {
			SimpleJdbcCall partjdbcCall = new SimpleJdbcCall(jdbcTemplate.getDataSource()).withProcedureName("DCLIST");
			Map<String, String> partinputs = new HashMap<String, String>();
			log.info("Executing Procedure { exec DCLIST ()}");
			SqlParameterSource partin = new MapSqlParameterSource().addValues(partinputs);
			Map<String, Object> partout = partjdbcCall.execute(partin);
			log.info("Procedure Result " + partout);
		} catch (Exception e) {
			log.info(e.getMessage());
			e.printStackTrace();

		}

		String circle = request.getParameter("circle");
		String load = request.getParameter("cmd");
		String ddv = request.getParameter("dropdown");
		String status = request.getParameter("status");
		String type = request.getParameter("type");

		if (!circle.equals("ALL")) {
			try {
				String sql = " SELECT TO_CHAR(LAST_PAID_DT,'DD-MM-YYYY') LPD,round(RKVAH) RKVAH,round(OB) OB,round(DEMAND) DEMAND,round(NET_RJ) NET_RJ, round(CASH_COLL) CASH_COLL,round(CB) CB,round(LAST_PAID_AMT) LAST_PAID_AMT,round(ACD_DEMAND) ACD_DEMAND ,round(ACD_BAL) ACD_BAL,round(CB_CCLPC) CB_CCLPC, round(CB_OTH) CB_OTH ,D.*,"
						+ "(select CB_ED from (select * from ledger_ht union all select * from ledger_ht_hist)where MON_YEAR=  TO_CHAR(ADD_MONTHS(to_date(MTH,'MON-YYYY'),-1),'MON-YYYY') and uscno=scno) ED  FROM D_LIST D WHERE SUBSTR(SCNO,0,3)=? ";
				if (load != null && load.equalsIgnoreCase("gte1000")) {
					sql = sql + "and nvl(load,0)>=1000 ";
					sql = sql + (ddv.equals("ALL") ? "" : " AND CB >= " + request.getParameter("cbamount"));
				} else if (load != null && load.equalsIgnoreCase("lt1000")) {
					sql = sql + "and nvl(load,0)<1000 ";
					sql = sql + (ddv.equals("ALL") ? "" : " AND CB >= " + request.getParameter("cbamount"));
				}
				if (load.equals("All")) {
					sql = sql + (ddv.equals("ALL") ? "" : " AND CB >= " + request.getParameter("cbamount"));
				}
				if (sql.contains("WHERE")) {

					sql = sql + (status.equals("All") ? "" : " AND STATUS = '" + status + "'");
				} else {
					sql = sql + (status.equals("All") ? "" : " WHERE STATUS = '" + status + "'");
				}
				if (sql.contains("WHERE")) {
					sql = sql + (type.equals("All") ? "" : " AND GOV_PVT = '" + type + "'");
				} else {
					sql = sql + (type.equals("All") ? "" : " WHERE GOV_PVT = '" + type + "'");
				}

				sql = sql + " order by SCNO";
				log.info("Circle:" + sql);
				return jdbcTemplate.queryForList(sql, new Object[] { circle });
			} catch (DataAccessException e) {
				log.info(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}
		} else {
			try {

				String sql = " SELECT TO_CHAR(LAST_PAID_DT,'DD-MM-YYYY') LPD,round(RKVAH) RKVAH,round(OB) OB,round(DEMAND) DEMAND,round(NET_RJ) NET_RJ, round(CASH_COLL) CASH_COLL,round(CB) CB,round(LAST_PAID_AMT) LAST_PAID_AMT,round(ACD_DEMAND) ACD_DEMAND ,round(ACD_BAL) ACD_BAL,round(CB_CCLPC) CB_CCLPC, round(CB_OTH) CB_OTH  ,D.*,"
						+ "(select CB_ED from (select * from ledger_ht union all select * from ledger_ht_hist)where MON_YEAR=  TO_CHAR(ADD_MONTHS(to_date(MTH,'MON-YYYY'),-1),'MON-YYYY') and uscno=scno) ED  FROM D_LIST D ";
				if (load != null && load.equalsIgnoreCase("gte1000")) {
					sql = sql + "WHERE nvl(load,0)>=1000 ";
					sql = sql + (ddv.equals("ALL") ? "" : " AND CB >= " + request.getParameter("cbamount"));
				} else if (load != null && load.equalsIgnoreCase("lt1000")) {
					sql = sql + "WHERE nvl(load,0)<1000 ";
					sql = sql + (ddv.equals("ALL") ? "" : " AND CB >= " + request.getParameter("cbamount"));
				}
				if (load.equals("All")) {
					sql = sql + (ddv.equals("ALL") ? "" : " WHERE CB >= " + request.getParameter("cbamount"));
				}
				if (sql.contains("WHERE")) {

					sql = sql + (status.equals("All") ? "" : " AND STATUS = '" + status + "'");
				} else {
					sql = sql + (status.equals("All") ? "" : " WHERE STATUS = '" + status + "'");
				}
				if (sql.contains("WHERE")) {
					sql = sql + (type.equals("All") ? "" : " AND GOV_PVT = '" + type + "'");
				} else {
					sql = sql + (type.equals("All") ? "" : " WHERE GOV_PVT = '" + type + "'");
				}
				sql = sql + " order by SCNO";
				log.info("All:" + sql);
				return jdbcTemplate.queryForList(sql);

			} catch (DataAccessException e) {
				log.info(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}
		}
	}

	public Map<String, String> getMasterCodes() {
		String sql = "SELECT TYPE,CODE FROM MASTER_CHANGE_CODES ORDER BY CODE";
		log.info("Executing Query { " + sql + " }");
		return jdbcTemplate.query(sql, (ResultSet rs) -> {
			HashMap<String, String> results = new LinkedHashMap<>();
			while (rs.next()) {
				results.put(rs.getString("CODE"), rs.getString("TYPE"));
			}
			return results;
		});
	}

	public Map<String, String> getSeasonalServices() {
		String sql = "  select SUSCNO, trim( (JAN|| FEB|| MAR|| APR|| MAY|| JUN|| JUL|| AUG|| SEP|| OCT|| NOV|| DEC)) SEASONAL\r\n"
				+ "from SEASONAL_CONS where substr(SUSCNO,1,3) in ('VJA','CRD','GNT','ONG')\r\n"
				+ "and trim( (JAN|| FEB|| MAR|| APR|| MAY|| JUN|| JUL|| AUG|| SEP|| OCT|| NOV|| DEC)) not in ('NNNNNNNNNNNN','NNNNYNNNNNNN','NNNNNNNNYYNN','NNNNNNNNYNNN','NNNNNNNYYNNN') ";
		log.info("Executing Query { " + sql + " }");
		return jdbcTemplateDev.query(sql, (ResultSet rs) -> {
			HashMap<String, String> results = new LinkedHashMap<>();
			while (rs.next()) {
				results.put(rs.getString("SUSCNO"), rs.getString("SEASONAL"));
			}
			return results;
		});
	}

	public Map<String, String> getTrueUpRemaingAmt() {
		/*
		 * String sql="select BTSCNO , TOT_AMT - DEM_TU  TO_PAID_AMT from \r\n" +
		 * "(select BTSCNO , sum(nvl(BT_TU_CHG,0)) + sum(nvl(BT_LTU_CHG,0)) DEM_TU from bill_hist \r\n"
		 * +
		 * "where  BTBLDT between to_date('01-04-2022','DD-MM-YYYY') and to_date('31-03-2023','DD-MM-YYYY')\r\n"
		 * + "-- and substr(BTSCNO,1,3)='ONG' \r\n" +
		 * "group by BTSCNO having sum(nvl(BT_TU_CHG,0)) + sum(nvl(BT_LTU_CHG,0)) >0) A,\r\n"
		 * + "\r\n" + "(select USCNO, sum(TOT_AMT) TOT_AMT from (\r\n" +
		 * "select USCNO, TOT_AMT from ht_trueup_chg\r\n" + "union all\r\n" +
		 * "select HT_USCNO USCNO ,TOT_AMOUNT TOT_AMT from lt_ht_tu_rj where HT_USCNO is not null)\r\n"
		 * + "group by uscno) B where A.BTSCNO = B.USCNO(+)";
		 */

		/* Need to Change Remaing Months for next Mock */

		/*
		 * String sql =
		 * "select A.USCNO BTSCNO,HT_AMT,LT_AMT  , nvl(HT_AMT,0) + nvl(LT_AMT,0) TO_PAID_AMT  from \r\n"
		 * +
		 * "(select USCNO,  round((NVL(TOT_KVAH,0)/36)*0.22,2)*28 HT_AMT  from ht_trueup_chg) A ,\r\n"
		 * +
		 * "(select HT_USCNO,round((sum(NVL(TOT_UNITS,0))/36)*0.22,2)*28 LT_AMT from lt_ht_tu_rj group by HT_USCNO) B\r\n"
		 * + "where A.USCNO= B.HT_USCNO(+)";
		 */
		// NOTE : OCT : 22
		String sql = "select uscno BTSCNO ,sum(HT_AMT) TO_PAID_AMT from  (\r\n"
				+ "(select USCNO,  round(((NVL(TOT_KVAH,0)-NVL(TOT_OA_KVAH,0))/36)*0.22,2)*18 HT_AMT  from ht_trueup_chg)\r\n"
				+ "union all\r\n"
				+ "(select HT_USCNO USCNO, round((sum(NVL(TOT_UNITS,0))/36)*0.22,2)*18 HT_AMT from lt_ht_tu_rj group by HT_USCNO))\r\n"
				+ " group by USCNO ";
		log.info("Executing Query { " + sql + " }");
		return jdbcTemplateDev.query(sql, (ResultSet rs) -> {
			HashMap<String, String> results = new LinkedHashMap<>();
			while (rs.next()) {
				results.put(rs.getString("BTSCNO"), rs.getString("TO_PAID_AMT"));
			}
			return results;
		});
	}

	public Map<String, String> getFYKWH() {
		String sql = "select btscno ,sum(NVL(BTRKWH_HT,0)) Q_KWH_FY from bill_hist where  BTBLDT between to_date('01-05-2022','DD-MM-YYYY') \r\n"
				+ "and to_date('31-12-2023','DD-MM-YYYY') group by btscno";
		log.info("Executing Query { " + sql + " }");
		return jdbcTemplateDev.query(sql, (ResultSet rs) -> {
			HashMap<String, String> results = new LinkedHashMap<>();
			while (rs.next()) {
				results.put(rs.getString("BTSCNO"), rs.getString("Q_KWH_FY"));
			}
			return results;
		});
	}

	public Map<String, String> getGridInstlDt() {
		/*
		 * String
		 * sql="select BTSCNO,case when to_char(min(BTBLOPNDT),'MON-YYYY')='APR-2022' then to_char(min(BTBLOPNDT),'YYYYMMDD') else  to_char(min(BTBLOPNDT)+1,'YYYYMMDD') end  BTBLDT   from bill_hist\r\n"
		 * + "where BTBLDT between to_date('01-04-2022','DD-MM-YYYY') \r\n" +
		 * "and to_date('31-01-2023','DD-MM-YYYY') and BT_GS_CHG<>0  group by BTSCNO";
		 */

		String sql = "select SCNO BTSCNO, to_char(CREATION_DATE,'YYYYMMDD')  BTBLDT from HT_GRID_SUPPORT_DETAILS ";
		log.info("Executing Query { " + sql + " }");
		return jdbcTemplateDev.query(sql, (ResultSet rs) -> {
			HashMap<String, String> results = new LinkedHashMap<>();
			while (rs.next()) {
				results.put(rs.getString("BTSCNO"), rs.getString("BTBLDT"));
			}
			return results;
		});
	}

	public Map<String, String> getFy21() {
		/*
		 * String
		 * sql="select BTSCNO,case when to_char(min(BTBLOPNDT),'MON-YYYY')='APR-2022' then to_char(min(BTBLOPNDT),'YYYYMMDD') else  to_char(min(BTBLOPNDT)+1,'YYYYMMDD') end  BTBLDT   from bill_hist\r\n"
		 * + "where BTBLDT between to_date('01-04-2022','DD-MM-YYYY') \r\n" +
		 * "and to_date('31-01-2023','DD-MM-YYYY') and BT_GS_CHG<>0  group by BTSCNO";
		 */

		String sql = "select BTSCNO,count(*) COUNT from V_M4_BILL_FACTS where Q_FAC_FY21>0 group by BTSCNO";
		log.info("Executing Query { " + sql + " }");
		return jdbcTemplateDev.query(sql, (ResultSet rs) -> {
			HashMap<String, String> results = new LinkedHashMap<>();
			while (rs.next()) {
				results.put(rs.getString("BTSCNO"), rs.getString("COUNT"));
			}
			return results;
		});
	}

	public Map<String, String> getHTFy21() {
		/*
		 * String
		 * sql="select BTSCNO,case when to_char(min(BTBLOPNDT),'MON-YYYY')='APR-2022' then to_char(min(BTBLOPNDT),'YYYYMMDD') else  to_char(min(BTBLOPNDT)+1,'YYYYMMDD') end  BTBLDT   from bill_hist\r\n"
		 * + "where BTBLDT between to_date('01-04-2022','DD-MM-YYYY') \r\n" +
		 * "and to_date('31-01-2023','DD-MM-YYYY') and BT_GS_CHG<>0  group by BTSCNO";
		 */

		String sql = "select BTSCNO,count(*) COUNT from V_M4_BILL_FACTS where Q_FA_HFY21>0 group by BTSCNO";
		log.info("Executing Query { " + sql + " }");
		return jdbcTemplateDev.query(sql, (ResultSet rs) -> {
			HashMap<String, String> results = new LinkedHashMap<>();
			while (rs.next()) {
				results.put(rs.getString("BTSCNO"), rs.getString("COUNT"));
			}
			return results;
		});
	}

	public Map<String, String> getCrossChg() {
		String sql = "select BTSCNO,count(*) COUNT from  V_M4_FACTS where BTWHEELCHGCASH_HT>0 group by BTSCNO";
		log.info("Executing Query { " + sql + " }");
		return jdbcTemplateDev.query(sql, (ResultSet rs) -> {
			HashMap<String, String> results = new LinkedHashMap<>();
			while (rs.next()) {
				results.put(rs.getString("BTSCNO"), rs.getString("COUNT"));
			}
			return results;
		});
	}

	public Map<String, String> getHtMeterCol() {
		String sql = "select BTSCNO,count(*) COUNT from  V_M4_FACTS where H_MTR_COL>0 group by BTSCNO";
		log.info("Executing Query { " + sql + " }");
		return jdbcTemplateDev.query(sql, (ResultSet rs) -> {
			HashMap<String, String> results = new LinkedHashMap<>();
			while (rs.next()) {
				results.put(rs.getString("BTSCNO"), rs.getString("COUNT"));
			}
			return results;
		});
	}

	public Map<String, String> getSeasonalDetails() {
		String sql = "select SUSCNO, FLAG from V_M4_SEASONAL_CONS";
		log.info("Executing Query { " + sql + " }");
		return jdbcTemplateDev.query(sql, (ResultSet rs) -> {
			HashMap<String, String> results = new LinkedHashMap<>();
			while (rs.next()) {
				results.put(rs.getString("SUSCNO"), rs.getString("FLAG"));
			}
			return results;
		});
	}

	public Map<String, String> getWheelChg() {
		String sql = "select BTSCNO,count(*) COUNT from  V_M4_FACTS where BTWHEELCHGCASH_HT>0 group by BTSCNO";
		log.info("Executing Query { " + sql + " }");
		return jdbcTemplateDev.query(sql, (ResultSet rs) -> {
			HashMap<String, String> results = new LinkedHashMap<>();
			while (rs.next()) {
				results.put(rs.getString("BTSCNO"), rs.getString("COUNT"));
			}
			return results;
		});
	}

	public Map<String, String> getfppca2() {
		String sql = "select BTSCNO,count(*) COUNT from  V_M4_FACTS where H_FPPCA2>0 group by BTSCNO";
		log.info("Executing Query { " + sql + " }");
		return jdbcTemplateDev.query(sql, (ResultSet rs) -> {
			HashMap<String, String> results = new LinkedHashMap<>();
			while (rs.next()) {
				results.put(rs.getString("BTSCNO"), rs.getString("COUNT"));
			}
			return results;
		});
	}

	public Map<String, String> getfppcach() {
		String sql = "select BTSCNO,count(*) COUNT from  V_M4_FACTS where H_FPPCA_CH>0 group by BTSCNO";
		log.info("Executing Query { " + sql + " }");
		return jdbcTemplateDev.query(sql, (ResultSet rs) -> {
			HashMap<String, String> results = new LinkedHashMap<>();
			while (rs.next()) {
				results.put(rs.getString("BTSCNO"), rs.getString("COUNT"));
			}
			return results;
		});
	}

	public List<Map<String, Object>> getTotalBillstopServices(HttpServletRequest request) {
		String con = request.getParameter("dropdown").equals("ALL") ? ""
				: request.getParameter("dropdown").equals("POS")
						? "  AND round(NVL(CBTOT,0))+round(NVL(CB_OTH,0))+round(NVL(CB_CCLPC,0))>= "
								+ request.getParameter("cbamount")
						: " AND round(NVL(CBTOT,0))+round(NVL(CB_OTH,0))+round(NVL(CB_CCLPC,0))<= -"
								+ request.getParameter("cbamount");
		String circle = request.getParameter("circle");

		if (circle.equalsIgnoreCase("ALL")) {
			try {
				String sql = "SELECT distinct CIRCLE, DIVISION,SUBDIVISION,SECTION,USCNO,NAM, round(NVL(CBTOT,0))+round(NVL(CB_OTH,0))+round(NVL(CB_CCLPC,0)) CBTOT,round(CB_SD) CB_SD,TO_CHAR(MTR.MDCLRDG_DT,'DD-MM-YYYY')MDCLRDG_DT,round(NVL(MTR.MDCLKVAH_HT,0)) MDCLKVAH_HT,DECODE(TRIM(CTGOVT_PVT),'Y','GOVT','N','PVT','') TYPE  FROM\r\n"
						+ "ACCOUNTCOPY,CONS,\r\n"
						+ "(SELECT MSCNO,MDCLRDG_DT,MDCLKVAH_HT FROM MTRDAT_HIST A WHERE MDCLRDG_DT IN \r\n"
						+ "(SELECT MAX(MDCLRDG_DT) FROM MTRDAT_HIST WHERE A.MSCNO=MSCNO GROUP BY MSCNO)) MTR\r\n"
						+ "WHERE USCNO=CTUSCNO(+)\r\n" + "AND USCNO=MTR.MSCNO(+)\r\n"
						+ " AND MON_YEAR = (select to_char(max(to_date(MON_YEAR,'MON-YYYY')),'MON-YYYY') from ACCOUNTCOPY) "
						+ "AND CTSTATUS<>1 " + con + " ORDER BY CIRCLE,DIVISION,SUBDIVISION,SECTION";
				log.info(sql);
				return jdbcTemplate.queryForList(sql, new Object[] {});
			} catch (DataAccessException e) {
				log.info(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}
		} else {
			try {
				String sql = "SELECT distinct CIRCLE, DIVISION,SUBDIVISION,SECTION,USCNO,NAM, round(NVL(CBTOT,0))+round(NVL(CB_OTH,0))+round(NVL(CB_CCLPC,0)) CBTOT,round(CB_SD) CB_SD,TO_CHAR(MTR.MDCLRDG_DT,'DD-MM-YYYY')MDCLRDG_DT,round(NVL(MTR.MDCLKVAH_HT,0)) MDCLKVAH_HT,DECODE(TRIM(CTGOVT_PVT),'Y','GOVT','N','PVT','') TYPE  FROM\r\n"
						+ "ACCOUNTCOPY,CONS,\r\n"
						+ "(SELECT MSCNO,MDCLRDG_DT,MDCLKVAH_HT FROM MTRDAT_HIST A WHERE MDCLRDG_DT IN \r\n"
						+ "(SELECT MAX(MDCLRDG_DT) FROM MTRDAT_HIST WHERE A.MSCNO=MSCNO GROUP BY MSCNO)) MTR\r\n"
						+ "WHERE USCNO=CTUSCNO(+)\r\n" + "AND USCNO=MTR.MSCNO(+)\r\n"
						+ " AND MON_YEAR = (select to_char(max(to_date(MON_YEAR,'MON-YYYY')),'MON-YYYY') from ACCOUNTCOPY) "
						+ "AND CTSTATUS<>1 AND SUBSTR(USCNO,1,3)=?  " + con + " ORDER BY CIRCLE,DIVISION,SUBDIVISION,SECTION";
				log.info(sql);
				return jdbcTemplate.queryForList(sql, new Object[] { circle });
			} catch (DataAccessException e) {
				log.info(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}
		}
	}

	/*
	 * public List<Map<String,Object>> getDListReason(HttpServletRequest request){
	 * String circle = request.getParameter("circle"); if
	 * (circle.equalsIgnoreCase("ALL")) { String sql=
	 * " select SCNO  , REMARKS, REA_FOR_PEN from D_LIST_BCK where (MTH=  to_char(add_months(sysdate,-1),'MON-YYYY') or MTH= to_char(sysdate,'MON-YYYY')) "
	 * ;
	 * 
	 * return jdbcTemplate.queryForList(sql,new Object[] {});
	 * 
	 * } else { String sql=
	 * " select SCNO  , REMARKS, REA_FOR_PEN from D_LIST_BCK where (MTH=  to_char(add_months(sysdate,-1),'MON-YYYY') or MTH= to_char(sysdate,'MON-YYYY'))  AND  SUBSTR(SCNO,0,3)= ?  "
	 * ; return jdbcTemplate.queryForList(sql,new Object[] {circle}); } }
	 */

	public Map<String, String> getDListReason() {
		String sql = "select SCNO  ,  REA_FOR_PEN ||'-'||REMARKS REA from D_LIST_BCK where (MTH=  to_char(add_months(sysdate,-1),'MON-YYYY') or MTH= to_char(sysdate,'MON-YYYY')) ";
		log.info("Executing Query { " + sql + " }");
		return jdbcTemplate.query(sql, (ResultSet rs) -> {
			HashMap<String, String> results = new LinkedHashMap<>();
			while (rs.next()) {
				results.put(rs.getString("SCNO"), rs.getString("REA"));
			}
			return results;
		});
	}

	public List<Map<String, Object>> getUDCDetails(HttpServletRequest request) {

		String circle = request.getParameter("circle");
		String monthYear = request.getParameter("month") + "-" + request.getParameter("year");
		if (circle.equalsIgnoreCase("ALL")) {
			String sql = "SELECT CIRNAME,DIVNAME,SUBNAME,SECNAME,BTBLDT,BTSCNO,CTNAME,CTADD1,BTBLCAT||BTBLSCAT BTBLCAT,BTCMD_HT,CTACTUAL_KV,BTRKVA_HT RMD,BTBLKVA_HT BMD,BTBKVAH,BTRKVAH_HT  \n"
					+ "FROM BILL,CONS,MTRDAT,MASTER.SPDCLMASTER  WHERE TO_CHAR(BTBLDT,'MON-YYYY')= ?  AND MDCLKWHSTAT_HT=3  AND BTSCNO=MSCNO  AND CTUSCNO=BTSCNO  \n"
					+ "AND SUBSTR(CTSECCD,-5)=SECCD\n" + "UNION ALL  \n"
					+ "SELECT CIRNAME,DIVNAME,SUBNAME,SECNAME,BTBLDT,BTSCNO,CTNAME,CTADD1,BTBLCAT||BTBLSCAT,BTCMD_HT,CTACTUAL_KV,BTRKVA_HT RMD,BTBLKVA_HT BMD,BTBKVAH,BTRKVAH_HT  \n"
					+ "FROM BILL_HIST,CONS,MTRDAT_HIST,MASTER.SPDCLMASTER  WHERE TO_CHAR(BTBLDT,'MON-YYYY')= ? and TO_CHAR(MDMONTH,'MON-YYYY')= ?  \n"
					+ "AND MDCLKWHSTAT_HT=3  AND BTSCNO=MSCNO  AND CTUSCNO=BTSCNO AND SUBSTR(CTSECCD,-5)=SECCD\n"
					+ "ORDER BY CIRNAME,DIVNAME,SUBNAME,SECNAME";
			log.info(sql + "" + monthYear);
			return jdbcTemplate.queryForList(sql, new Object[] { monthYear, monthYear, monthYear });

		} else {
			String sql = "SELECT SUBSTR(BTSCNO,1,3)CIRCLE,DIVNAME,SUBNAME,SECNAME,BTBLDT,BTSCNO,CTNAME,CTADD1,BTBLCAT||BTBLSCAT BTBLCAT,BTCMD_HT,CTACTUAL_KV,BTRKVA_HT RMD,BTBLKVA_HT BMD,BTBKVAH,BTRKVAH_HT  \n"
					+ "FROM BILL,CONS,MTRDAT,MASTER.SPDCLMASTER  WHERE TO_CHAR(BTBLDT,'MON-YYYY')=?  AND MDCLKWHSTAT_HT=3  AND BTSCNO=MSCNO  AND CTUSCNO=BTSCNO  \n"
					+ "AND SUBSTR(CTSECCD,-5)=SECCD AND SUBSTR(BTSCNO,1,3)=?\n" + "UNION ALL  \n"
					+ "SELECT SUBSTR(BTSCNO,1,3) CIRCLE,DIVNAME,SUBNAME,SECNAME,BTBLDT,BTSCNO,CTNAME,CTADD1,BTBLCAT||BTBLSCAT,BTCMD_HT,CTACTUAL_KV,BTRKVA_HT RMD,BTBLKVA_HT BMD,BTBKVAH,BTRKVAH_HT  \n"
					+ "FROM BILL_HIST,CONS,MTRDAT_HIST,MASTER.SPDCLMASTER  WHERE TO_CHAR(BTBLDT,'MON-YYYY')= ? and TO_CHAR(MDMONTH,'MON-YYYY')= ?  \n"
					+ "AND MDCLKWHSTAT_HT=3  AND BTSCNO=MSCNO  AND CTUSCNO=BTSCNO AND SUBSTR(CTSECCD,-5)=SECCD AND SUBSTR(BTSCNO,1,3)=?\n"
					+ "ORDER BY CIRCLE,DIVNAME,SUBNAME,SECNAME";
			log.info(sql + "---" + monthYear + circle);
			return jdbcTemplate.queryForList(sql, new Object[] { monthYear, circle, monthYear, monthYear, circle });
		}
	}

	public List<Map<String, Object>> getGovtServices(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		if (!circle.equalsIgnoreCase("ALL")) {
			String sql = "SELECT CIRNAME,DIVNAME,SUBNAME,CTUSCNO,CTNAME,CTADD1,CTCAT||CTSUBCAT CTCAT,CTCMD_HT,CTACTUAL_KV,SECNAME,CBTOT FROM CONS,SPDCLMASTER,ledger_ht_hist\r\n"
					+ "WHERE SECCD=SUBSTR(CTSECCD,-5) AND ACCT_ID=cons.ctacct_id \r\n" + "AND TRIM(CTGOVT_PVT)='Y'\r\n"
					+ "AND CTSTATUS='1' \r\n"
					+ "AND MON_YEAR =(SELECT TO_CHAR(MAX(TO_DATE(MON_YEAR,'MON-YYYY')),'MON-YYYY') FROM ledger_ht_hist)\r\n"
					+ "AND SUBSTR(CTUSCNO,0,3)=? \r\n" + "ORDER BY DIVNAME,SUBNAME,SECNAME,CTUSCNO";
			log.info(sql);
			return jdbcTemplate.queryForList(sql, new Object[] { circle });
		} else {
			String sql = "SELECT CIRNAME,DIVNAME,SUBNAME,CTUSCNO,CTNAME,CTADD1,CTCAT||CTSUBCAT CTCAT,CTCMD_HT,CTACTUAL_KV,SECNAME,CBTOT FROM CONS,SPDCLMASTER,LEDGER_HT_HIST\r\n"
					+ "WHERE SECCD=SUBSTR(CTSECCD,-5) AND ACCT_ID=CONS.CTACCT_ID \r\n" + "AND TRIM(CTGOVT_PVT)='Y'\r\n"
					+ "AND CTSTATUS='1' \r\n"
					+ "AND MON_YEAR =(SELECT TO_CHAR(MAX(TO_DATE(MON_YEAR,'MON-YYYY')),'MON-YYYY') FROM LEDGER_HT_HIST)\r\n"
					+ "ORDER BY DIVNAME,SUBNAME,SECNAME,CTUSCNO";
			log.info(sql);
			return jdbcTemplate.queryForList(sql);
		}
	}

	public List<Map<String, Object>> getFeederConsumptionDetails(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		String monthYear = request.getParameter("month") + "-" + request.getParameter("year");
		if (!circle.equals("ALL")) {
			String sql = " SELECT CIRNAME,CTUSCNO,CTFEEDER_CODE,CTFEEDER_NAME, DIVNAME,SUBNAME,SECNAME,CTSS_NAME,TO_CHAR(BTBLDT,'MON-YYYY')BTBLDT,CTACTUAL_KV,BTRKWH_HT,(BTTOD2+BTTOD5)-(BTTOD1+BTTOD6) RECTOD,BTRKVAH_HT,BTRKVA_HT RMD,BTBKVAH FROM CONS,SPDCLMASTER,BILL_HIST "
					+ " WHERE SECCD=SUBSTR(CTSECCD,-5) " + " AND CTUSCNO=BTSCNO " + " AND TO_CHAR(BTBLDT,'MON-YYYY')=? "
					+ " AND CTSTATUS='1'" + " AND SUBSTR(CTUSCNO,0,3)=? " + " UNION ALL "
					+ " SELECT CIRNAME,CTUSCNO,CTFEEDER_CODE,CTFEEDER_NAME, DIVNAME,SUBNAME,SECNAME,CTSS_NAME,TO_CHAR(BTBLDT,'MON-YYYY')BTBLDT,CTACTUAL_KV,BTRKWH_HT,(BTTOD2+BTTOD5)-(BTTOD1+BTTOD6) RECTOD,BTRKVAH_HT,BTRKVA_HT RMD,BTBKVAH FROM CONS,SPDCLMASTER,BILL "
					+ " WHERE SECCD=SUBSTR(CTSECCD,-5)  " + " AND CTUSCNO=BTSCNO "
					+ " AND TO_CHAR(BTBLDT,'MON-YYYY')=? " + " AND CTSTATUS='1' " + " AND SUBSTR(CTUSCNO,0,3)= ? ";
			log.info(sql);
			return jdbcTemplate.queryForList(sql, new Object[] { monthYear, circle, monthYear, circle });
		} else {

			String sql = " SELECT CIRNAME,CTUSCNO,CTFEEDER_CODE,CTFEEDER_NAME, DIVNAME,SUBNAME,SECNAME,CTSS_NAME,TO_CHAR(BTBLDT,'MON-YYYY')BTBLDT,CTACTUAL_KV,BTRKWH_HT,(BTTOD2+BTTOD5)-(BTTOD1+BTTOD6) RECTOD,BTRKVAH_HT,BTRKVA_HT RMD,BTBKVAH FROM CONS,SPDCLMASTER,BILL_HIST "
					+ " WHERE SECCD=SUBSTR(CTSECCD,-5) " + " AND CTUSCNO=BTSCNO "
					+ " AND TO_CHAR(BTBLDT,'MON-YYYY')= ? " + " AND CTSTATUS='1'" + " UNION ALL "
					+ " SELECT CIRNAME,CTUSCNO,CTFEEDER_CODE,CTFEEDER_NAME, DIVNAME,SUBNAME,SECNAME,CTSS_NAME,TO_CHAR(BTBLDT,'MON-YYYY')BTBLDT,CTACTUAL_KV,BTRKWH_HT,(BTTOD2+BTTOD5)-(BTTOD1+BTTOD6) RECTOD,BTRKVAH_HT,BTRKVA_HT RMD,BTBKVAH FROM CONS,SPDCLMASTER,BILL "
					+ " WHERE SECCD=SUBSTR(CTSECCD,-5)  " + " AND CTUSCNO=BTSCNO "
					+ " AND TO_CHAR(BTBLDT,'MON-YYYY')=? " + " AND CTSTATUS='1'";
			log.info(sql);
			return jdbcTemplate.queryForList(sql, new Object[] { monthYear, monthYear });
		}
	}

	public List<Map<String, Object>> getbmdDetails(HttpServletRequest request) {

		String monthYear = request.getParameter("month") + "-" + request.getParameter("year");
		String sql = "SELECT CIRCLE,COUNT(1) AS SCS,SUM(NVL(PENAL_MD,0))PENAL_MD,SUM(NVL(PENAL_DEMAND_CHARGES,0))PENAL_DEMAND_CHARGES,SUM(NVL(CMD,0))CMD,SUM(NVL(BMD,0))BMD "
				+ " FROM CMD_BMD_S WHERE TO_CHAR(MONTH,'MON-YYYY')=? " + " GROUP BY CIRCLE ORDER BY CIRCLE";
		return jdbcTemplate1.queryForList(sql, new Object[] { monthYear });
	}

	public List<Map<String, Object>> getExistingMeterDetails(String circle) {
		if (!circle.equals("ALL")) {
			String sql = "select A.CIRNAME, A.DIVNAME,A.SUBNAME,NVL(A.TOT_SCS,0) TOT_SCS ,NVL(B.ADE_CRM_SCS,0) ADE_CRM_SCS ,NVL(C.MRT_CRM_SCS,0) MRT_CRM_SCS from \r\n"
					+ "(select CIRNAME, DIVNAME,SUBNAME,count(*) TOT_SCS from cons,master.spdclmaster where  SUBSTR(CTSECCD,-5)=SECCD  and SUBSTR(CTUSCNO,1,3)=?\r\n"
					+ "group by CIRNAME, DIVNAME,SUBNAME,SUBCD\r\n" + "order by CIRNAME, DIVNAME, SUBNAME,SUBCD) A,\r\n"
					+ "\r\n"
					+ "(select CIRNAME, DIVNAME,SUBNAME,count(*) ADE_CRM_SCS from cons,ht_mnp_cubical_details,master.spdclmaster where  CTUSCNO=USCNO and SUBSTR(CTSECCD,-5)=SECCD and SUBSTR(CTUSCNO,1,3)=? and STATUS='Confirmed By DEE'\r\n"
					+ "group by CIRNAME, DIVNAME,SUBNAME,SUBCD\r\n" + "order by CIRNAME, DIVNAME, SUBNAME,SUBCD) B,\r\n"
					+ "\r\n"
					+ "(select CIRNAME, DIVNAME,SUBNAME,count(*) MRT_CRM_SCS from cons,ht_mnp_cubical_details,master.spdclmaster where  CTUSCNO=USCNO and SUBSTR(CTSECCD,-5)=SECCD and SUBSTR(CTUSCNO,1,3)=? and STATUS='Confirmed By MRT'\r\n"
					+ "group by CIRNAME, DIVNAME,SUBNAME,SUBCD\r\n" + "order by CIRNAME, DIVNAME, SUBNAME,SUBCD) C\r\n"
					+ "where A.CIRNAME=B.CIRNAME(+) and A.DIVNAME=B.DIVNAME(+) and A.SUBNAME=B.SUBNAME(+)\r\n"
					+ "and A.CIRNAME=C.CIRNAME(+) and A.DIVNAME=C.DIVNAME(+) and A.SUBNAME=C.SUBNAME(+)";
			log.info(sql);
			return jdbcTemplate.queryForList(sql, new Object[] { circle, circle, circle });
		} else {
			String sql = "select A.CIRNAME, A.DIVNAME,A.SUBNAME,NVL(A.TOT_SCS,0) TOT_SCS ,NVL(B.ADE_CRM_SCS,0) ADE_CRM_SCS ,NVL(C.MRT_CRM_SCS,0) MRT_CRM_SCS  from \r\n"
					+ "(select CIRNAME, DIVNAME,SUBNAME,count(*) TOT_SCS from cons,master.spdclmaster where  SUBSTR(CTSECCD,-5)=SECCD \r\n"
					+ "group by CIRNAME, DIVNAME,SUBNAME,SUBCD\r\n" + "order by CIRNAME, DIVNAME, SUBNAME,SUBCD) A,\r\n"
					+ "\r\n"
					+ "(select CIRNAME, DIVNAME,SUBNAME,count(*) ADE_CRM_SCS from cons,ht_mnp_cubical_details,master.spdclmaster where  CTUSCNO=USCNO and SUBSTR(CTSECCD,-5)=SECCD and STATUS='Confirmed By DEE'\r\n"
					+ "group by CIRNAME, DIVNAME,SUBNAME,SUBCD\r\n" + "order by CIRNAME, DIVNAME, SUBNAME,SUBCD) B,\r\n"
					+ "\r\n"
					+ "(select CIRNAME, DIVNAME,SUBNAME,count(*) MRT_CRM_SCS from cons,ht_mnp_cubical_details,master.spdclmaster where  CTUSCNO=USCNO and SUBSTR(CTSECCD,-5)=SECCD and STATUS='Confirmed By MRT'\r\n"
					+ "group by CIRNAME, DIVNAME,SUBNAME,SUBCD\r\n" + "order by CIRNAME, DIVNAME, SUBNAME,SUBCD) C\r\n"
					+ "where A.CIRNAME=B.CIRNAME(+) and A.DIVNAME=B.DIVNAME(+) and A.SUBNAME=B.SUBNAME(+)\r\n"
					+ "and A.CIRNAME=C.CIRNAME(+) and A.DIVNAME=C.DIVNAME(+) and A.SUBNAME=C.SUBNAME(+)";
			log.info(sql);
			return jdbcTemplate.queryForList(sql, new Object[] {});
		}
	}

	public List<Map<String, Object>> getExistingMasterDetails(HttpServletRequest request) {
		String circle = (String.valueOf(request.getParameter("circle")).equals("null") ? "ALL"
				: request.getParameter("circle"));
		String status = (String.valueOf(request.getParameter("status")).equals("null") ? "LB"
				: request.getParameter("status"));
		String constring = status.equals("LB") ? "  "
				: status.equals("L") ? "  and ctstatus =1  " : "   and ctstatus = 0 ";

		if (!circle.equals("ALL")) {
			String sql = "select A.CIRNAME, A.DIVNAME,A.SUBNAME,NVL(A.TOT_SCS,0) TOT_SCS ,NVL(B.ADE_CRM_SCS,0) ADE_CRM_SCS  from \r\n"
					+ "(select CIRNAME, DIVNAME,SUBNAME,count(*) TOT_SCS from cons,master.spdclmaster where  SUBSTR(CTSECCD,-5)=SECCD and SUBSTR(CTUSCNO,1,3)=?  \r\n"
					+ constring + "group by CIRNAME, DIVNAME,SUBNAME,SUBCD\r\n"
					+ "order by CIRNAME, DIVNAME, SUBNAME,SUBCD) A,\r\n"
					+ "(select CIRNAME, DIVNAME,SUBNAME,count(*) ADE_CRM_SCS from cons,ht_master,master.spdclmaster where  CTUSCNO=USCNO and SUBSTR(CTSECCD,-5)=SECCD  and SUBSTR(CTUSCNO,1,3)=?  \r\n"
					+ constring + "group by CIRNAME, DIVNAME,SUBNAME,SUBCD\r\n"
					+ "order by CIRNAME, DIVNAME, SUBNAME,SUBCD) B\r\n"
					+ "where A.CIRNAME=B.CIRNAME(+) and A.DIVNAME=B.DIVNAME(+) and A.SUBNAME=B.SUBNAME(+) ";
			log.info(sql);
			return jdbcTemplate.queryForList(sql, new Object[] { circle, circle });
		} else {
			String sql = "select A.CIRNAME, A.DIVNAME,A.SUBNAME,NVL(A.TOT_SCS,0) TOT_SCS ,NVL(B.ADE_CRM_SCS,0) ADE_CRM_SCS  from \r\n"
					+ "(select CIRNAME, DIVNAME,SUBNAME,count(*) TOT_SCS from cons,master.spdclmaster where  SUBSTR(CTSECCD,-5)=SECCD \r\n"
					+ constring + "group by CIRNAME, DIVNAME,SUBNAME,SUBCD\r\n"
					+ "order by CIRNAME, DIVNAME, SUBNAME,SUBCD) A,\r\n"
					+ "(select CIRNAME, DIVNAME,SUBNAME,count(*) ADE_CRM_SCS from cons,ht_master,master.spdclmaster where  CTUSCNO=USCNO and SUBSTR(CTSECCD,-5)=SECCD \r\n"
					+ constring + "group by CIRNAME, DIVNAME,SUBNAME,SUBCD\r\n"
					+ "order by CIRNAME, DIVNAME, SUBNAME,SUBCD) B\r\n"
					+ "where A.CIRNAME=B.CIRNAME(+) and A.DIVNAME=B.DIVNAME(+) and A.SUBNAME=B.SUBNAME(+) ";
			log.info(sql);
			return jdbcTemplate.queryForList(sql, new Object[] {});
		}
	}

	public List<Map<String, Object>> getcbmdDetails(HttpServletRequest request) {

		String monthYear = request.getParameter("month") + "-" + request.getParameter("year");
		String sql = "SELECT CIRCLE,COUNT(1) AS SCS,SUM(NVL(PENAL_MD,0))PENAL_MD,SUM(NVL(PENAL_DEMAND_CHARGES,0))PENAL_DEMAND_CHARGES,SUM(NVL(CMD,0))CMD,SUM(NVL(BMD,0))BMD "
				+ " FROM CMD_BMD_V WHERE TO_CHAR(MONTH,'MON-YYYY')=? " + " GROUP BY CIRCLE ORDER BY CIRCLE";
		log.info(sql);
		return jdbcTemplate2.queryForList(sql, new Object[] { monthYear });
	}

	public List<Map<String, Object>> getbmdDetailsDivision(String cirleName, String mon_year) {
		String sql = "SELECT DIVNAME,COUNT(1) AS SCS,SUM(NVL(PENAL_MD,0))PENAL_MD,SUM(NVL(PENAL_DEMAND_CHARGES,0))PENAL_DEMAND_CHARGES,SUM(NVL(CMD,0)),SUM(NVL(BMD,0))"
				+ " FROM CMD_BMD_S "
				+ " WHERE TO_CHAR(MONTH,'MON-YYYY')=? AND CIRCLE=? GROUP BY DIVNAME ORDER BY DIVNAME";
		log.info(sql);
		return jdbcTemplate1.queryForList(sql, new Object[] { mon_year, cirleName });
	}

	public List<Map<String, Object>> getcbmdDetailsDivision(String cirleName, String mon_year) {
		String sql = "SELECT DIVNAME,COUNT(1) AS SCS,SUM(NVL(PENAL_MD,0))PENAL_MD,SUM(NVL(PENAL_DEMAND_CHARGES,0))PENAL_DEMAND_CHARGES,SUM(NVL(CMD,0)),SUM(NVL(BMD,0))"
				+ " FROM CMD_BMD_V "
				+ " WHERE TO_CHAR(MONTH,'MON-YYYY')=? AND CIRCLE=? GROUP BY DIVNAME ORDER BY DIVNAME";
		log.info(sql);
		return jdbcTemplate2.queryForList(sql, new Object[] { mon_year, cirleName });
	}

	public List<Map<String, Object>> getbmdDetailsDivisionServices(String cirleName, String mon_year) {
		String sql = "SELECT * FROM CMD_BMD_S WHERE TO_CHAR(MONTH,'MON-YYYY')=?  AND CIRCLE=?";
		log.info(sql);
		return jdbcTemplate1.queryForList(sql, new Object[] { mon_year, cirleName });
	}

	public List<Map<String, Object>> getcbmdDetailsDivisionServices(String cirleName, String mon_year) {
		String sql = "SELECT * FROM CMD_BMD_V WHERE TO_CHAR(MONTH,'MON-YYYY')=?  AND CIRCLE=?";
		log.info(sql);
		return jdbcTemplate2.queryForList(sql, new Object[] { mon_year, cirleName });
	}

	public List<Map<String, Object>> getbmdDetailsSubDivision(String cirleName, String mon_year, String division) {
		String sql = "SELECT SUBNAME,COUNT(1) AS SCS,SUM(NVL(PENAL_MD,0))PENAL_MD,SUM(NVL(PENAL_DEMAND_CHARGES,0))PENAL_DEMAND_CHARGES,SUM(NVL(CMD,0)),SUM(NVL(BMD,0))"
				+ " FROM CMD_BMD_S  " + " WHERE TO_CHAR(MONTH,'MON-YYYY')=? " + " AND CIRCLE=? " + " AND DIVNAME=? "
				+ " GROUP BY SUBNAME ORDER BY SUBNAME";
		log.info(sql);
		return jdbcTemplate1.queryForList(sql, new Object[] { mon_year, cirleName, division });
	}

	public List<Map<String, Object>> getcbmdDetailsSubDivision(String cirleName, String mon_year, String division) {
		String sql = "SELECT SUBNAME,COUNT(1) AS SCS,SUM(NVL(PENAL_MD,0))PENAL_MD,SUM(NVL(PENAL_DEMAND_CHARGES,0))PENAL_DEMAND_CHARGES,SUM(NVL(CMD,0)),SUM(NVL(BMD,0))"
				+ " FROM CMD_BMD_V  " + " WHERE TO_CHAR(MONTH,'MON-YYYY')=? " + " AND CIRCLE=? " + " AND DIVNAME=? "
				+ " GROUP BY SUBNAME ORDER BY SUBNAME";
		log.info(sql);
		return jdbcTemplate2.queryForList(sql, new Object[] { mon_year, cirleName, division });
	}

	public List<Map<String, Object>> getbmdDetailsSubDivisionServices(String cirleName, String mon_year,
			String division) {
		String sql = "SELECT * FROM CMD_BMD_S WHERE TO_CHAR(MONTH,'MON-YYYY')=?  AND CIRCLE=? AND DIVNAME=?";
		log.info(sql);
		return jdbcTemplate1.queryForList(sql, new Object[] { mon_year, cirleName, division });
	}

	public List<Map<String, Object>> getcbmdDetailsSubDivisionServices(String cirleName, String mon_year,
			String division) {
		String sql = "SELECT * FROM CMD_BMD_V WHERE TO_CHAR(MONTH,'MON-YYYY')=?  AND CIRCLE=? AND DIVNAME=?";
		log.info(sql);
		return jdbcTemplate2.queryForList(sql, new Object[] { mon_year, cirleName, division });
	}

	public List<Map<String, Object>> getbmdDetailsSectionDivision(String cirleName, String mon_year, String division,
			String subdivision) {

		String sql = "SELECT SECNAME,COUNT(1) AS SCS,SUM(NVL(PENAL_MD,0))PENAL_MD,SUM(NVL(PENAL_DEMAND_CHARGES,0))PENAL_DEMAND_CHARGES,SUM(NVL(CMD,0)),SUM(NVL(BMD,0)) "
				+ " FROM CMD_BMD_S WHERE TO_CHAR(MONTH,'MON-YYYY')= ? " + " AND CIRCLE = ? " + " AND DIVNAME =? "
				+ " AND SUBNAME =? " + " GROUP BY SECNAME ORDER BY SECNAME ";
		log.info(sql);
		return jdbcTemplate1.queryForList(sql, new Object[] { mon_year, cirleName, division, subdivision });
	}

	public List<Map<String, Object>> getcbmdDetailsSectionDivision(String cirleName, String mon_year, String division,
			String subdivision) {

		String sql = "SELECT SECNAME,COUNT(1) AS SCS,SUM(NVL(PENAL_MD,0))PENAL_MD,SUM(NVL(PENAL_DEMAND_CHARGES,0))PENAL_DEMAND_CHARGES,SUM(NVL(CMD,0)),SUM(NVL(BMD,0)) "
				+ " FROM CMD_BMD_V WHERE TO_CHAR(MONTH,'MON-YYYY')= ? " + " AND CIRCLE = ? " + " AND DIVNAME =? "
				+ " AND SUBNAME =? " + " GROUP BY SECNAME ORDER BY SECNAME ";
		log.info(sql);
		return jdbcTemplate2.queryForList(sql, new Object[] { mon_year, cirleName, division, subdivision });
	}

	public List<Map<String, Object>> getbmdDetailsSectionDivisionServices(String cirleName, String mon_year,
			String division, String subdivision) {

		String sql = "SELECT * FROM CMD_BMD_S WHERE TO_CHAR(MONTH,'MON-YYYY')= ? " + " AND CIRCLE = ? "
				+ " AND DIVNAME =? " + " AND SUBNAME =? " + " ORDER BY SECNAME ";
		log.info(sql);
		return jdbcTemplate1.queryForList(sql, new Object[] { mon_year, cirleName, division, subdivision });
	}

	public List<Map<String, Object>> getcbmdDetailsSectionDivisionServices(String cirleName, String mon_year,
			String division, String subdivision) {

		String sql = "SELECT * FROM CMD_BMD_V WHERE TO_CHAR(MONTH,'MON-YYYY')= ? " + " AND CIRCLE = ? "
				+ " AND DIVNAME =? " + " AND SUBNAME =? " + " ORDER BY SECNAME ";
		log.info(sql);
		return jdbcTemplate2.queryForList(sql, new Object[] { mon_year, cirleName, division, subdivision });
	}

	/*
	 * public String getAccountCopyProcedure() { SimpleJdbcCall partjdbcCall = new
	 * SimpleJdbcCall(jdbcTemplate.getDataSource())
	 * .withProcedureName("ACCOUNTCOPY_HT"); Map<String, String> partinputs = new
	 * HashMap<String, String>(); partinputs.put("P_CIRCLE", "ALL");
	 * partinputs.put("P_MONYY", "JUN-2020"); partinputs.put("P_USC_NO", "ALL");
	 * SqlParameterSource partin = new
	 * MapSqlParameterSource().addValues(partinputs); Map<String, Object> partout =
	 * partjdbcCall.execute(partin); log.info("Procedure Result " + partout); return
	 * "success";
	 * 
	 * }
	 */

	public List<Map<String, Object>> getMeterAndMfChangeReportDetails(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		String monthYear = request.getParameter("month") + "-" + request.getParameter("year");
		if (circle.equalsIgnoreCase("ALL")) {
			String sql = " SELECT USCNO,METER_NO_OLD,MF_OLD,METER_MAKE_OLD,METER_NO_NEW,MF_NEW,NVL(METER_MAKE_NEW,'--')METER_MAKE_NEW FROM MNP_METER_CHANGE "
					+ " WHERE  TO_CHAR(CHANGED_ON,'MON-YYYY')=?";
			log.info(sql);
			return jdbcTemplate.queryForList(sql, new Object[] { monthYear });
		} else {
			String sql = " SELECT USCNO,METER_NO_OLD,MF_OLD,METER_MAKE_OLD,METER_NO_NEW,MF_NEW,NVL(METER_MAKE_NEW,'--')METER_MAKE_NEW FROM MNP_METER_CHANGE "
					+ " WHERE  TO_CHAR(CHANGED_ON,'MON-YYYY')=? AND SUBSTR(USCNO,0,3)=? ";
			log.info(sql);
			return jdbcTemplate.queryForList(sql, new Object[] { monthYear, circle });
		}
	}

	public List<Map<String, Object>> getConsumerDetails(HttpServletRequest request, String type) {
		String mtrno = request.getParameter("scno");
		if (type.equalsIgnoreCase("M")) {
			String sql = "SELECT CTNAME,CTCAT,CTCMD_HT,MDMTRNO,MSCNO,TO_CHAR(CTSUPCONDT,'DD-MM-YYYY')CTSUPCONDT,CTSTATUS,MDMF_HT FROM CONS,MTRDAT  WHERE MSCNO=CTUSCNO AND  MDMTRNO=? AND MDCLKWHSTAT_HT IN(1,13,11,3)";
			return jdbcTemplate.queryForList(sql, new Object[] { mtrno.trim() });
		} else {
			String sql = "SELECT CTNAME,CTCAT,CTCMD_HT,MDMTRNO,MSCNO,TO_CHAR(CTSUPCONDT,'DD-MM-YYYY')CTSUPCONDT,CTSTATUS,MDMF_HT FROM CONS,MTRDAT  WHERE MSCNO=CTUSCNO AND  MSCNO=? AND MDCLKWHSTAT_HT IN(1,13,11,3)";
			return jdbcTemplate.queryForList(sql, new Object[] { mtrno.trim() });
		}
	}

	public List<Map<String, Object>> getallbmdServices(String mon_year) {
		String sql = "SELECT * FROM  CMD_BMD_S WHERE TO_CHAR(MONTH,'MON-YYYY')=?";
		return jdbcTemplate1.queryForList(sql, new Object[] { mon_year });
	}

	public List<Map<String, Object>> getcallbmdServices(String mon_year) {
		String sql = "SELECT * FROM  CMD_BMD_V WHERE TO_CHAR(MONTH,'MON-YYYY')=?";
		return jdbcTemplate2.queryForList(sql, new Object[] { mon_year });
	}

	public List<Map<String, Object>> getallDivisionbmdServices(String mon_year, String circleName) {
		String sql = "SELECT * FROM CMD_BMD_S WHERE TO_CHAR(MONTH,'MON-YYYY')=?  AND CIRCLE=?";
		return jdbcTemplate1.queryForList(sql, new Object[] { mon_year, circleName });
	}

	public List<Map<String, Object>> getcallDivisionbmdServices(String mon_year, String circleName) {
		String sql = "SELECT * FROM CMD_BMD_V WHERE TO_CHAR(MONTH,'MON-YYYY')=?  AND CIRCLE=?";
		return jdbcTemplate2.queryForList(sql, new Object[] { mon_year, circleName });
	}

	public List<Map<String, Object>> getallSubDivisionbmdServices(String division, String mon_year, String circleName) {
		String sql = "SELECT * FROM CMD_BMD_S WHERE TO_CHAR(MONTH,'MON-YYYY')=?  AND CIRCLE=?  AND DIVNAME=?";
		return jdbcTemplate1.queryForList(sql, new Object[] { mon_year, circleName, division });
	}

	public List<Map<String, Object>> getcallSubDivisionbmdServices(String division, String mon_year,
			String circleName) {
		String sql = "SELECT * FROM CMD_BMD_V WHERE TO_CHAR(MONTH,'MON-YYYY')=?  AND CIRCLE=?  AND DIVNAME=?";
		return jdbcTemplate2.queryForList(sql, new Object[] { mon_year, circleName, division });
	}

	public List<Map<String, Object>> getallSectionDivisionbmdServices(String division, String mon_year,
			String circleName, String sub) {
		String sql = "SELECT * FROM CMD_BMD_S WHERE TO_CHAR(MONTH,'MON-YYYY')=?  AND CIRCLE=?  AND DIVNAME=? AND  SUBNAME=? ";
		return jdbcTemplate1.queryForList(sql, new Object[] { mon_year, circleName, division, sub });
	}

	public List<Map<String, Object>> getcallSectionDivisionbmdServices(String division, String mon_year,
			String circleName, String sub) {
		String sql = "SELECT * FROM CMD_BMD_V WHERE TO_CHAR(MONTH,'MON-YYYY')=?  AND CIRCLE=?  AND DIVNAME=? AND  SUBNAME=? ";
		return jdbcTemplate2.queryForList(sql, new Object[] { mon_year, circleName, division, sub });
	}

	public List<Map<String, Object>> threebmd(HttpServletRequest request) {
		String monthYear = request.getParameter("month") + "-" + request.getParameter("year");
		String sql = "SELECT CIRCLE,COUNT(1) AS SCS FROM THREE_MONTHS_BMD WHERE TO_CHAR(MONTH,'MON-YYYY')=? AND SUBSTR(USCNO,1,3) IN('GNT','VJA','ONG','CRD') GROUP BY CIRCLE ORDER BY CIRCLE";
		return jdbcTemplate1.queryForList(sql, new Object[] { monthYear });
	}

	public List<Map<String, Object>> threebmddivisions(String circle, String mon_year) {
		String sql = "SELECT DIVNAME,COUNT(1) AS SCS FROM THREE_MONTHS_BMD WHERE TO_CHAR(MONTH,'MON-YYYY')=? AND CIRCLE=?  AND SUBSTR(USCNO,1,3) IN('GNT','VJA','ONG','CRD') GROUP BY DIVNAME ORDER BY DIVNAME";
		return jdbcTemplate1.queryForList(sql, new Object[] { mon_year, circle });
	}

	public List<Map<String, Object>> threebmdSubdivisions(String circle, String mon_year, String division) {
		String sql = "SELECT SUBNAME,COUNT(1) AS SCS FROM THREE_MONTHS_BMD WHERE TO_CHAR(MONTH,'MON-YYYY')=? AND CIRCLE=? AND DIVNAME=?  AND SUBSTR(USCNO,1,3) IN('GNT','VJA','ONG','CRD') GROUP BY SUBNAME ORDER BY SUBNAME";
		return jdbcTemplate1.queryForList(sql, new Object[] { mon_year, circle, division });
	}

	public List<Map<String, Object>> threebmdSection(String circle, String mon_year, String division, String sub) {
		System.out.println(circle + "\t" + mon_year + "\t" + division + "\t" + sub);
		String sql = "SELECT SECNAME,COUNT(1) AS SCS FROM THREE_MONTHS_BMD WHERE TO_CHAR(MONTH,'MON-YYYY')=? AND CIRCLE=? AND DIVNAME=? AND SUBNAME=?  AND SUBSTR(USCNO,1,3) IN('GNT','VJA','ONG','CRD') GROUP BY SECNAME ORDER BY SECNAME";
		return jdbcTemplate1.queryForList(sql, new Object[] { mon_year, circle, division, sub });
	}

	public List<Map<String, Object>> allthreebmdscsall(String circle, String division, String sub, String mon_year,
			String type) {
		List<Map<String, Object>> result = null;
		if (type.equalsIgnoreCase("cir")) {
			String sql = "SELECT NVL(CIRCLE,'-')CIRCLE,NVL(DIVNAME,'-')DIVNAME,NVL(SUBNAME,'-')SUBNAME,NVL(SECNAME,'-')SECNAME,NVL(USCNO,'-')USCNO,NVL(NAME,'-')NAME,NVL(CATEGORY,'-')CATEGORY,NVL(SUM(CMD),0)CMD,NVL(SUM(BMD_1),0)BMD_1,NVL(SUM(BMD_2),0)BMD_2,NVL(SUM(BMD_3),0)BMD_3 FROM THREE_MONTHS_BMD WHERE TO_CHAR(MONTH,'MON-YYYY')=?  AND SUBSTR(USCNO,1,3) IN('GNT','VJA','ONG','CRD') GROUP BY CIRCLE,DIVNAME,SUBNAME,SECNAME,USCNO,NAME,CATEGORY";
			result = jdbcTemplate1.queryForList(sql, new Object[] { mon_year });
		} else if (type.equalsIgnoreCase("div")) {
			String sql = "SELECT NVL(CIRCLE,'-')CIRCLE,NVL(DIVNAME,'-')DIVNAME,NVL(SUBNAME,'-')SUBNAME,NVL(SECNAME,'-')SECNAME,NVL(USCNO,'-')USCNO,NVL(NAME,'-')NAME,NVL(CATEGORY,'-')CATEGORY,NVL(SUM(CMD),0)CMD,NVL(SUM(BMD_1),0)BMD_1,NVL(SUM(BMD_2),0)BMD_2,NVL(SUM(BMD_3),0)BMD_3 FROM THREE_MONTHS_BMD WHERE TO_CHAR(MONTH,'MON-YYYY')=? AND CIRCLE=?  AND SUBSTR(USCNO,1,3) IN('GNT','VJA','ONG','CRD') GROUP BY CIRCLE,DIVNAME,SUBNAME,SECNAME,USCNO,NAME,CATEGORY";
			result = jdbcTemplate1.queryForList(sql, new Object[] { mon_year, circle });
		} else if (type.equalsIgnoreCase("sub")) {
			String sql = "SELECT NVL(CIRCLE,'-')CIRCLE,NVL(DIVNAME,'-')DIVNAME,NVL(SUBNAME,'-')SUBNAME,NVL(SECNAME,'-')SECNAME,NVL(USCNO,'-')USCNO,NVL(NAME,'-')NAME,NVL(CATEGORY,'-')CATEGORY,NVL(SUM(CMD),0)CMD,NVL(SUM(BMD_1),0)BMD_1,NVL(SUM(BMD_2),0)BMD_2,NVL(SUM(BMD_3),0)BMD_3 FROM THREE_MONTHS_BMD WHERE TO_CHAR(MONTH,'MON-YYYY')=? AND CIRCLE=?  AND DIVNAME=?  AND SUBSTR(USCNO,1,3) IN('GNT','VJA','ONG','CRD') GROUP BY CIRCLE,DIVNAME,SUBNAME,SECNAME,USCNO,NAME,CATEGORY";
			result = jdbcTemplate1.queryForList(sql, new Object[] { mon_year, circle, division });
		} else if (type.equalsIgnoreCase("sec")) {
			String sql = "SELECT NVL(CIRCLE,'-')CIRCLE,NVL(DIVNAME,'-')DIVNAME,NVL(SUBNAME,'-')SUBNAME,NVL(SECNAME,'-')SECNAME,NVL(USCNO,'-')USCNO,NVL(NAME,'-')NAME,NVL(CATEGORY,'-')CATEGORY,NVL(SUM(CMD),0)CMD,NVL(SUM(BMD_1),0)BMD_1,NVL(SUM(BMD_2),0)BMD_2,NVL(SUM(BMD_3),0)BMD_3 FROM THREE_MONTHS_BMD WHERE TO_CHAR(MONTH,'MON-YYYY')=? AND CIRCLE=?  AND DIVNAME=? AND SUBNAME=?  AND SUBSTR(USCNO,1,3) IN('GNT','VJA','ONG','CRD')  GROUP BY CIRCLE,DIVNAME,SUBNAME,SECNAME,USCNO,NAME,CATEGORY";
			result = jdbcTemplate1.queryForList(sql, new Object[] { mon_year, circle, division, sub });
		}
		return result;
	}

	public List<Map<String, Object>> allthreebmdscs(String circle, String division, String sub, String type,
			String mon_year, String section) {

		List<Map<String, Object>> result = null;
		if (type.equalsIgnoreCase("cir")) {
			String sql = "SELECT NVL(CIRCLE,'-')CIRCLE,NVL(DIVNAME,'-')DIVNAME,NVL(SUBNAME,'-')SUBNAME,NVL(SECNAME,'-')SECNAME,NVL(USCNO,'-')USCNO,NVL(NAME,'-')NAME,NVL(CATEGORY,'-')CATEGORY,NVL(SUM(CMD),0)CMD,NVL(SUM(BMD_1),0)BMD_1,NVL(SUM(BMD_2),0)BMD_2,NVL(SUM(BMD_3),0)BMD_3 FROM THREE_MONTHS_BMD WHERE TO_CHAR(MONTH,'MON-YYYY')=? AND CIRCLE=?  AND SUBSTR(USCNO,1,3) IN('GNT','VJA','ONG','CRD') GROUP BY CIRCLE,DIVNAME,SUBNAME,SECNAME,USCNO,NAME,CATEGORY";
			result = jdbcTemplate1.queryForList(sql, new Object[] { mon_year, circle });
		} else if (type.equalsIgnoreCase("div")) {
			String sql = "SELECT NVL(CIRCLE,'-')CIRCLE,NVL(DIVNAME,'-')DIVNAME,NVL(SUBNAME,'-')SUBNAME,NVL(SECNAME,'-')SECNAME,NVL(USCNO,'-')USCNO,NVL(NAME,'-')NAME,NVL(CATEGORY,'-')CATEGORY,NVL(SUM(CMD),0)CMD,NVL(SUM(BMD_1),0)BMD_1,NVL(SUM(BMD_2),0)BMD_2,NVL(SUM(BMD_3),0)BMD_3 FROM THREE_MONTHS_BMD WHERE TO_CHAR(MONTH,'MON-YYYY')=? AND CIRCLE=? AND DIVNAME=?  AND SUBSTR(USCNO,1,3) IN('GNT','VJA','ONG','CRD') GROUP BY CIRCLE,DIVNAME,SUBNAME,SECNAME,USCNO,NAME,CATEGORY";
			result = jdbcTemplate1.queryForList(sql, new Object[] { mon_year, circle, division });
		} else if (type.equalsIgnoreCase("sub")) {
			String sql = "SELECT NVL(CIRCLE,'-')CIRCLE,NVL(DIVNAME,'-')DIVNAME,NVL(SUBNAME,'-')SUBNAME,NVL(SECNAME,'-')SECNAME,NVL(USCNO,'-')USCNO,NVL(NAME,'-')NAME,NVL(CATEGORY,'-')CATEGORY,NVL(SUM(CMD),0)CMD,NVL(SUM(BMD_1),0)BMD_1,NVL(SUM(BMD_2),0)BMD_2,NVL(SUM(BMD_3),0)BMD_3 FROM THREE_MONTHS_BMD WHERE TO_CHAR(MONTH,'MON-YYYY')=? AND CIRCLE=? AND DIVNAME=? AND SUBNAME=?  AND SUBSTR(USCNO,1,3) IN('GNT','VJA','ONG','CRD') GROUP BY CIRCLE,DIVNAME,SUBNAME,SECNAME,USCNO,NAME,CATEGORY";
			result = jdbcTemplate1.queryForList(sql, new Object[] { mon_year, circle, division, sub });
		} else if (type.equalsIgnoreCase("sec")) {
			String sql = "SELECT NVL(CIRCLE,'-')CIRCLE,NVL(DIVNAME,'-')DIVNAME,NVL(SUBNAME,'-')SUBNAME,NVL(SECNAME,'-')SECNAME,NVL(USCNO,'-')USCNO,NVL(NAME,'-')NAME,NVL(CATEGORY,'-')CATEGORY,NVL(SUM(CMD),0)CMD,NVL(SUM(BMD_1),0)BMD_1,NVL(SUM(BMD_2),0)BMD_2,NVL(SUM(BMD_3),0)BMD_3 FROM THREE_MONTHS_BMD WHERE TO_CHAR(MONTH,'MON-YYYY')=? AND CIRCLE=? AND DIVNAME=? AND SUBNAME=? AND SECNAME=?  AND SUBSTR(USCNO,1,3) IN('GNT','VJA','ONG','CRD') GROUP BY CIRCLE,DIVNAME,SUBNAME,SECNAME,USCNO,NAME,CATEGORY";
			result = jdbcTemplate1.queryForList(sql, new Object[] { mon_year, circle, division, sub, section });
		}
		return result;
	}

	public List<Map<String, Object>> getAcdDlistReport(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		String type = request.getParameter("type");
		if (circle.equals("ALL")) {
			String sql = "SELECT * FROM D_LIST WHERE ACD_BAL>0 "
					+ (type.equals("All") ? "" : " AND GOV_PVT = '" + type + "'");
			return jdbcTemplate.queryForList(sql);
		} else {
			String sql = "SELECT * FROM D_LIST WHERE SUBSTR(SCNO,0,3)=? AND  ACD_BAL>0 "
					+ (type.equals("All") ? "" : " AND GOV_PVT = '" + type + "'");
			return jdbcTemplate.queryForList(sql, new Object[] { circle });
		}
	}

	public List<Map<String, Object>> getAcdNoticeReport(HttpServletRequest request) {

		String circle = request.getParameter("circle");
		String monthYear = request.getParameter("month") + "-" + request.getParameter("year");
		if (circle.equalsIgnoreCase("ALL")) {
			String sql = "SELECT CIRNAME, DIVNAME, SUBNAME, SECNAME,USCNO,NVL(NET_ACD,0) ACD,DECODE(TRIM(CTGOVT_PVT),'Y','GOVT','N','PVT','') TYPE,A.LEVI_MTH\r\n"
					+ "FROM ACD_CALC_HT A,CONS B,SPDCLMASTER C\r\n"
					+ "WHERE A.ACCT_ID=B.CTACCT_ID AND SUBSTR(B.CTSECCD,-5)=C.SECCD AND LEVI_MTH=? AND TRIM(LEVI_FLG)='Y' \r\n"
					+ "UNION ALL\r\n"
					+ "SELECT CIRNAME, DIVNAME, SUBNAME, SECNAME,USCNO,NVL(NET_ACD,0) ACD,DECODE(TRIM(CTGOVT_PVT),'Y','GOVT','N','PVT','') TYPE,A.LEVI_MTH\r\n"
					+ "FROM ACD_CALC_HT_HIST A,CONS B,SPDCLMASTER C\r\n"
					+ "WHERE A.ACCT_ID=B.CTACCT_ID AND SUBSTR(B.CTSECCD,-5)=C.SECCD AND LEVI_MTH=? AND TRIM(LEVI_FLG)='Y' \r\n"
					+ "ORDER BY 1,2,3,4,5";
			log.info(sql);
			return jdbcTemplate.queryForList(sql, new Object[] { monthYear, monthYear });
		} else if (circle.equalsIgnoreCase("SPDCL")) {

			String sql = "SELECT CIRNAME,SUM(GOVT_SCS) GOVT_SCS,SUM(GOVT_ACD) GOVT_ACD,SUM(PVT_SCS) PVT_SCS,SUM(PVT_ACD) PVT_ACD FROM (\r\n"
					+ "SELECT CIRNAME,SUM(DECODE(TRIM(CTGOVT_PVT),'Y',1,0)) AS Govt_Scs,\r\n"
					+ "SUM(DECODE(TRIM(CTGOVT_PVT),'N',1,0)) AS PVT_Scs,\r\n" + "SUM(NVL(NET_ACD,0)) ACD,\r\n"
					+ "DECODE(TRIM(CTGOVT_PVT),'Y','GOVT','N','PVT','') TYPE \r\n"
					+ "FROM ACD_CALC_HT A,CONS B,SPDCLMASTER C\r\n" + "WHERE A.ACCT_ID=B.CTACCT_ID\r\n"
					+ "AND SUBSTR(B.CTSECCD,-5)=C.SECCD \r\n" + "AND LEVI_MTH=? \r\n" + "AND TRIM(LEVI_FLG)='Y' \r\n"
					+ "AND SUBSTR(USCNO,0,3)  IN ('TPT','KNL','CDP','NLR','ATP') \r\n"
					+ "GROUP BY CIRNAME,DECODE(TRIM(CTGOVT_PVT),'Y','GOVT','N','PVT','') \r\n"
					+ "ORDER BY CIRNAME ,DECODE(TRIM(CTGOVT_PVT),'Y','GOVT','N','PVT','')) SRS\r\n" + "PIVOT \r\n"
					+ "(\r\n" + "MAX(ACD)\r\n" + "FOR TYPE IN ('GOVT' GOVT_ACD,'PVT' PVT_ACD)\r\n"
					+ ") GROUP BY CIRNAME\r\n" + "";
			return jdbcTemplate.queryForList(sql, new Object[] { monthYear });
		} else if (circle.equalsIgnoreCase("CPDCL")) {

			String sql = "SELECT CIRNAME,SUM(GOVT_SCS) GOVT_SCS,SUM(GOVT_ACD) GOVT_ACD,SUM(PVT_SCS) PVT_SCS,SUM(PVT_ACD) PVT_ACD FROM (\r\n"
					+ "SELECT CIRNAME,SUM(DECODE(TRIM(CTGOVT_PVT),'Y',1,0)) AS Govt_Scs,\r\n"
					+ "SUM(DECODE(TRIM(CTGOVT_PVT),'N',1,0)) AS PVT_Scs,\r\n" + "SUM(NVL(NET_ACD,0)) ACD,\r\n"
					+ "DECODE(TRIM(CTGOVT_PVT),'Y','GOVT','N','PVT','') TYPE \r\n"
					+ "FROM ACD_CALC_HT A,CONS B,SPDCLMASTER C\r\n" + "WHERE A.ACCT_ID=B.CTACCT_ID\r\n"
					+ "AND SUBSTR(B.CTSECCD,-5)=C.SECCD \r\n" + "AND LEVI_MTH=? \r\n" + "AND TRIM(LEVI_FLG)='Y' \r\n"
					+ "AND SUBSTR(USCNO,0,3)  NOT IN ('TPT','KNL','CDP','NLR','ATP') \r\n"
					+ "GROUP BY CIRNAME,DECODE(TRIM(CTGOVT_PVT),'Y','GOVT','N','PVT','') \r\n"
					+ "ORDER BY CIRNAME ,DECODE(TRIM(CTGOVT_PVT),'Y','GOVT','N','PVT','')) SRS\r\n" + "PIVOT \r\n"
					+ "(\r\n" + "MAX(ACD)\r\n" + "FOR TYPE IN ('GOVT' GOVT_ACD,'PVT' PVT_ACD)\r\n"
					+ ") GROUP BY CIRNAME\r\n" + "";
			return jdbcTemplate.queryForList(sql, new Object[] { monthYear });
		} else {

			String sql = "SELECT CIRNAME, DIVNAME, SUBNAME, SECNAME,USCNO,NVL(NET_ACD,0) ACD,DECODE(TRIM(CTGOVT_PVT),'Y','GOVT','N','PVT','') TYPE,A.LEVI_MTH\r\n"
					+ "FROM ACD_CALC_HT A,CONS B,SPDCLMASTER C\r\n"
					+ "WHERE A.ACCT_ID=B.CTACCT_ID AND SUBSTR(B.CTSECCD,-5)=C.SECCD AND LEVI_MTH=? AND TRIM(LEVI_FLG)='Y'  AND SUBSTR(USCNO,0,3)=? \r\n"
					+ "UNION ALL\r\n"
					+ "SELECT CIRNAME, DIVNAME, SUBNAME, SECNAME,USCNO,NVL(NET_ACD,0) ACD,DECODE(TRIM(CTGOVT_PVT),'Y','GOVT','N','PVT','') TYPE,A.LEVI_MTH\r\n"
					+ "FROM ACD_CALC_HT_HIST A,CONS B,SPDCLMASTER C\r\n"
					+ "WHERE A.ACCT_ID=B.CTACCT_ID AND SUBSTR(B.CTSECCD,-5)=C.SECCD AND LEVI_MTH=? AND TRIM(LEVI_FLG)='Y' AND SUBSTR(USCNO,0,3)=? \r\n"
					+ "ORDER BY 1,2,3,4,5";
			log.info(sql);
			return jdbcTemplate.queryForList(sql, new Object[] { monthYear, circle, monthYear, circle });

		}
	}

	public List<Map<String, Object>> getAllCirclesACDAbstract(String type, String circle, String division,
			String month_year, String subdivision, String section) {
		if (type.equalsIgnoreCase("div")) {
			String sql = "SELECT DIVNAME,SUM(GOVT_SCS) GOVT_SCS,SUM(GOVT_ACD) GOVT_ACD,SUM(PVT_SCS) PVT_SCS,SUM(PVT_ACD) PVT_ACD FROM (\r\n"
					+ "SELECT DIVNAME,SUM(DECODE(TRIM(CTGOVT_PVT),'Y',1,0)) AS Govt_Scs,\r\n"
					+ "SUM(DECODE(TRIM(CTGOVT_PVT),'N',1,0)) AS PVT_Scs,\r\n" + "SUM(NVL(NET_ACD,0)) ACD,\r\n"
					+ "DECODE(TRIM(CTGOVT_PVT),'Y','GOVT','N','PVT','') TYPE \r\n"
					+ "FROM ACD_CALC_HT A,CONS B,SPDCLMASTER C\r\n" + "WHERE A.ACCT_ID=B.CTACCT_ID\r\n"
					+ "AND SUBSTR(B.CTSECCD,-5)=C.SECCD \r\n" + "AND LEVI_MTH=?\r\n" + "AND TRIM(LEVI_FLG)='Y' \r\n"
					+ "AND CIRNAME  =? \r\n" + "GROUP BY DIVNAME,DECODE(TRIM(CTGOVT_PVT),'Y','GOVT','N','PVT','') \r\n"
					+ "ORDER BY DIVNAME ,DECODE(TRIM(CTGOVT_PVT),'Y','GOVT','N','PVT','')) SRS\r\n" + "PIVOT \r\n"
					+ "(\r\n" + "MAX(ACD)\r\n" + "FOR TYPE IN ('GOVT' GOVT_ACD,'PVT' PVT_ACD)\r\n"
					+ ") GROUP BY DIVNAME\r\n" + "";
			return jdbcTemplate.queryForList(sql, new Object[] { month_year, circle });

		} else if (type.equalsIgnoreCase("sub")) {
			String sql = "SELECT SUBNAME,SUM(GOVT_SCS) GOVT_SCS,SUM(GOVT_ACD) GOVT_ACD,SUM(PVT_SCS) PVT_SCS,SUM(PVT_ACD) PVT_ACD FROM (\r\n"
					+ "SELECT SUBNAME,SUM(DECODE(TRIM(CTGOVT_PVT),'Y',1,0)) AS Govt_Scs,\r\n"
					+ "SUM(DECODE(TRIM(CTGOVT_PVT),'N',1,0)) AS PVT_Scs,\r\n" + "SUM(NVL(NET_ACD,0)) ACD,\r\n"
					+ "DECODE(TRIM(CTGOVT_PVT),'Y','GOVT','N','PVT','') TYPE \r\n"
					+ "FROM ACD_CALC_HT A,CONS B,SPDCLMASTER C\r\n" + "WHERE A.ACCT_ID=B.CTACCT_ID\r\n"
					+ "AND SUBSTR(B.CTSECCD,-5)=C.SECCD \r\n" + "AND LEVI_MTH=? \r\n" + "AND TRIM(LEVI_FLG)='Y' \r\n"
					+ "AND CIRNAME  =? \r\n" + "AND DIVNAME  =? \r\n"
					+ "GROUP BY SUBNAME,DECODE(TRIM(CTGOVT_PVT),'Y','GOVT','N','PVT','') \r\n"
					+ "ORDER BY SUBNAME ,DECODE(TRIM(CTGOVT_PVT),'Y','GOVT','N','PVT','')) SRS\r\n" + "PIVOT \r\n"
					+ "(\r\n" + "MAX(ACD)\r\n" + "FOR TYPE IN ('GOVT' GOVT_ACD,'PVT' PVT_ACD)\r\n"
					+ ") GROUP BY SUBNAME";
			return jdbcTemplate.queryForList(sql, new Object[] { month_year, circle, division });
		} else if (type.equalsIgnoreCase("sec")) {
			String sql = "SELECT SECNAME,SUM(GOVT_SCS) GOVT_SCS,SUM(GOVT_ACD) GOVT_ACD,SUM(PVT_SCS) PVT_SCS,SUM(PVT_ACD) PVT_ACD FROM (\r\n"
					+ "SELECT SECNAME,SUM(DECODE(TRIM(CTGOVT_PVT),'Y',1,0)) AS Govt_Scs,\r\n"
					+ "SUM(DECODE(TRIM(CTGOVT_PVT),'N',1,0)) AS PVT_Scs,\r\n" + "SUM(NVL(NET_ACD,0)) ACD,\r\n"
					+ "DECODE(TRIM(CTGOVT_PVT),'Y','GOVT','N','PVT','') TYPE \r\n"
					+ "FROM ACD_CALC_HT A,CONS B,SPDCLMASTER C\r\n" + "WHERE A.ACCT_ID=B.CTACCT_ID\r\n"
					+ "AND SUBSTR(B.CTSECCD,-5)=C.SECCD \r\n" + "AND LEVI_MTH=? \r\n" + "AND TRIM(LEVI_FLG)='Y' \r\n"
					+ "AND CIRNAME  =? \r\n" + "AND DIVNAME  =? \r\n" + "AND SUBNAME  =? \r\n"
					+ "GROUP BY SECNAME,DECODE(TRIM(CTGOVT_PVT),'Y','GOVT','N','PVT','') \r\n"
					+ "ORDER BY SECNAME,DECODE(TRIM(CTGOVT_PVT),'Y','GOVT','N','PVT','')) SRS\r\n" + "PIVOT \r\n"
					+ "(\r\n" + "MAX(ACD)\r\n" + "FOR TYPE IN ('GOVT' GOVT_ACD,'PVT' PVT_ACD)\r\n"
					+ ") GROUP BY SECNAME";
			return jdbcTemplate.queryForList(sql, new Object[] { month_year, circle, division, subdivision });
		}
		return Collections.emptyList();

	}

	public List<Map<String, Object>> getAllCirclesACDAbstractServices(HttpServletRequest request) {
		String type = request.getParameter("type");
		String circle = request.getParameter("cir").trim();
		String division = request.getParameter("div").trim();
		String month_year = request.getParameter("mon_year").trim();
		String subdivision = request.getParameter("sub").trim();
		String section = request.getParameter("sec").trim();
		String status = request.getParameter("status").trim();
		if (type.equalsIgnoreCase("cir")) {
			String sql = "SELECT CIRNAME, DIVNAME, SUBNAME, SECNAME,USCNO,NVL(NET_ACD,0) ACD,DECODE(TRIM(CTGOVT_PVT),'Y','GOVT','N','PVT','') TYPE,A.LEVI_MTH \r\n"
					+ "FROM ACD_CALC_HT A,CONS B,SPDCLMASTER C\r\n"
					+ "WHERE A.ACCT_ID=B.CTACCT_ID AND SUBSTR(B.CTSECCD,-5)=C.SECCD AND LEVI_MTH=? AND TRIM(LEVI_FLG)='Y'  AND CIRNAME=? AND CTGOVT_PVT=?  \r\n"
					+ "UNION ALL\r\n"
					+ "SELECT CIRNAME, DIVNAME, SUBNAME, SECNAME,USCNO,NVL(NET_ACD,0) ACD,DECODE(TRIM(CTGOVT_PVT),'Y','GOVT','N','PVT','') TYPE,A.LEVI_MTH \r\n"
					+ "FROM ACD_CALC_HT_HIST A,CONS B,SPDCLMASTER C\r\n"
					+ "WHERE A.ACCT_ID=B.CTACCT_ID AND SUBSTR(B.CTSECCD,-5)=C.SECCD AND LEVI_MTH=? AND TRIM(LEVI_FLG)='Y' AND CIRNAME=?  AND CTGOVT_PVT=?  \r\n"
					+ "ORDER BY 1,2,3,4,5";
			return jdbcTemplate.queryForList(sql,
					new Object[] { month_year, circle, status, month_year, circle, status });
		} else if (type.equalsIgnoreCase("div")) {

			String sql = "SELECT CIRNAME, DIVNAME, SUBNAME, SECNAME,USCNO,NVL(NET_ACD,0) ACD,DECODE(TRIM(CTGOVT_PVT),'Y','GOVT','N','PVT','') TYPE,A.LEVI_MTH \r\n"
					+ "FROM ACD_CALC_HT A,CONS B,SPDCLMASTER C\r\n"
					+ "WHERE A.ACCT_ID=B.CTACCT_ID AND SUBSTR(B.CTSECCD,-5)=C.SECCD AND LEVI_MTH=? AND TRIM(LEVI_FLG)='Y'  AND CIRNAME=? AND CTGOVT_PVT=? AND TRIM(DIVNAME)=? \r\n"
					+ "UNION ALL\r\n"
					+ "SELECT CIRNAME, DIVNAME, SUBNAME, SECNAME,USCNO,NVL(NET_ACD,0) ACD,DECODE(TRIM(CTGOVT_PVT),'Y','GOVT','N','PVT','') TYPE,A.LEVI_MTH \r\n"
					+ "FROM ACD_CALC_HT_HIST A,CONS B,SPDCLMASTER C\r\n"
					+ "WHERE A.ACCT_ID=B.CTACCT_ID AND SUBSTR(B.CTSECCD,-5)=C.SECCD AND LEVI_MTH=? AND TRIM(LEVI_FLG)='Y' AND CIRNAME=?  AND CTGOVT_PVT=? AND TRIM(DIVNAME)=?  \r\n"
					+ "ORDER BY 1,2,3,4,5";
			return jdbcTemplate.queryForList(sql,
					new Object[] { month_year, circle, status, division, month_year, circle, status, division });
		} else if (type.equalsIgnoreCase("sub")) {

			String sql = "SELECT CIRNAME, DIVNAME, SUBNAME, SECNAME,USCNO,NVL(NET_ACD,0) ACD,DECODE(TRIM(CTGOVT_PVT),'Y','GOVT','N','PVT','') TYPE,A.LEVI_MTH \r\n"
					+ "FROM ACD_CALC_HT A,CONS B,SPDCLMASTER C\r\n"
					+ "WHERE A.ACCT_ID=B.CTACCT_ID AND SUBSTR(B.CTSECCD,-5)=C.SECCD AND LEVI_MTH=? AND TRIM(LEVI_FLG)='Y'  AND CIRNAME=? AND CTGOVT_PVT=? AND TRIM(DIVNAME)=? AND TRIM(SUBNAME)=? \r\n"
					+ "UNION ALL\r\n"
					+ "SELECT CIRNAME, DIVNAME, SUBNAME, SECNAME,USCNO,NVL(NET_ACD,0) ACD,DECODE(TRIM(CTGOVT_PVT),'Y','GOVT','N','PVT','') TYPE,A.LEVI_MTH \r\n"
					+ "FROM ACD_CALC_HT_HIST A,CONS B,SPDCLMASTER C\r\n"
					+ "WHERE A.ACCT_ID=B.CTACCT_ID AND SUBSTR(B.CTSECCD,-5)=C.SECCD AND LEVI_MTH=? AND TRIM(LEVI_FLG)='Y' AND CIRNAME=?  AND CTGOVT_PVT=? AND TRIM(DIVNAME)=?  AND TRIM(SUBNAME)=? \r\n"
					+ "ORDER BY 1,2,3,4,5";
			return jdbcTemplate.queryForList(sql, new Object[] { month_year, circle, status, division, subdivision,
					month_year, circle, status, division, subdivision });
		} else if (type.equalsIgnoreCase("sec")) {

			String sql = "SELECT CIRNAME, DIVNAME, SUBNAME, SECNAME,USCNO,NVL(NET_ACD,0) ACD,DECODE(TRIM(CTGOVT_PVT),'Y','GOVT','N','PVT','') TYPE,A.LEVI_MTH \r\n"
					+ "FROM ACD_CALC_HT A,CONS B,SPDCLMASTER C\r\n"
					+ "WHERE A.ACCT_ID=B.CTACCT_ID AND SUBSTR(B.CTSECCD,-5)=C.SECCD AND LEVI_MTH=? AND TRIM(LEVI_FLG)='Y'  AND CIRNAME=? AND CTGOVT_PVT=? AND TRIM(DIVNAME)=? AND TRIM(SUBNAME)=? AND TRIM(SECNAME)=? \r\n"
					+ "UNION ALL\r\n"
					+ "SELECT CIRNAME, DIVNAME, SUBNAME, SECNAME,USCNO,NVL(NET_ACD,0) ACD,DECODE(TRIM(CTGOVT_PVT),'Y','GOVT','N','PVT','') TYPE,A.LEVI_MTH \r\n"
					+ "FROM ACD_CALC_HT_HIST A,CONS B,SPDCLMASTER C\r\n"
					+ "WHERE A.ACCT_ID=B.CTACCT_ID AND SUBSTR(B.CTSECCD,-5)=C.SECCD AND LEVI_MTH=? AND TRIM(LEVI_FLG)='Y' AND CIRNAME=?  AND CTGOVT_PVT=? AND TRIM(DIVNAME)=?  AND TRIM(SUBNAME)=? AND TRIM(SECNAME)=?  \r\n"
					+ "ORDER BY 1,2,3,4,5";
			return jdbcTemplate.queryForList(sql, new Object[] { month_year, circle, status, division, subdivision,
					section, month_year, circle, status, division, subdivision, section });
		}
		return Collections.emptyList();
	}

	public List<Map<String, Object>> getAllCirclesACDAbstractServicesTotal(HttpServletRequest request) {
		String type = request.getParameter("type");
		String circle = request.getParameter("cir").trim();
		String division = request.getParameter("div").trim();
		String month_year = request.getParameter("mon_year").trim();
		String subdivision = request.getParameter("sub").trim();
		String section = request.getParameter("sec").trim();
		String status = request.getParameter("status").trim();
		String stype = request.getParameter("stype");
		if (type.equalsIgnoreCase("all")) {
			if (stype.equalsIgnoreCase("CPDCL")) {
				String sql = " SELECT CIRNAME, DIVNAME, SUBNAME, SECNAME,USCNO,NVL(NET_ACD,0) ACD,DECODE(TRIM(CTGOVT_PVT),'Y','GOVT','N','PVT','') TYPE,A.LEVI_MTH\r\n"
						+ "    FROM ACD_CALC_HT A,CONS B,SPDCLMASTER C\r\n"
						+ "    WHERE A.ACCT_ID=B.CTACCT_ID AND SUBSTR(B.CTSECCD,-5)=C.SECCD AND LEVI_MTH=? AND TRIM(LEVI_FLG)='Y'  AND CIRNAME NOT IN ('ANANTHAPUR','TIRUPATI','KADAPA','NELLORE','KURNOOL') AND CTGOVT_PVT=?  \r\n"
						+ "    UNION ALL\r\n"
						+ "    SELECT CIRNAME, DIVNAME, SUBNAME, SECNAME,USCNO,NVL(NET_ACD,0) ACD,DECODE(TRIM(CTGOVT_PVT),'Y','GOVT','N','PVT','') TYPE,A.LEVI_MTH\r\n"
						+ "    FROM ACD_CALC_HT_HIST A,CONS B,SPDCLMASTER C\r\n"
						+ "    WHERE A.ACCT_ID=B.CTACCT_ID AND SUBSTR(B.CTSECCD,-5)=C.SECCD AND LEVI_MTH=? AND TRIM(LEVI_FLG)='Y' AND CIRNAME NOT IN ('ANANTHAPUR','TIRUPATI','KADAPA','NELLORE','KURNOOL')  AND CTGOVT_PVT=? \r\n"
						+ "    ORDER BY 1,2,3,4,5";
				return jdbcTemplate.queryForList(sql, new Object[] { month_year, status, month_year, status });
			} else {
				String sql = " SELECT CIRNAME, DIVNAME, SUBNAME, SECNAME,USCNO,NVL(NET_ACD,0) ACD,DECODE(TRIM(CTGOVT_PVT),'Y','GOVT','N','PVT','') TYPE,A.LEVI_MTH\r\n"
						+ "    FROM ACD_CALC_HT A,CONS B,SPDCLMASTER C\r\n"
						+ "    WHERE A.ACCT_ID=B.CTACCT_ID AND SUBSTR(B.CTSECCD,-5)=C.SECCD AND LEVI_MTH=? AND TRIM(LEVI_FLG)='Y'  AND CIRNAME  IN ('ANANTHAPUR','TIRUPATI','KADAPA','NELLORE','KURNOOL') AND CTGOVT_PVT=?  \r\n"
						+ "    UNION ALL\r\n"
						+ "    SELECT CIRNAME, DIVNAME, SUBNAME, SECNAME,USCNO,NVL(NET_ACD,0) ACD,DECODE(TRIM(CTGOVT_PVT),'Y','GOVT','N','PVT','') TYPE,A.LEVI_MTH\r\n"
						+ "    FROM ACD_CALC_HT_HIST A,CONS B,SPDCLMASTER C\r\n"
						+ "    WHERE A.ACCT_ID=B.CTACCT_ID AND SUBSTR(B.CTSECCD,-5)=C.SECCD AND LEVI_MTH=? AND TRIM(LEVI_FLG)='Y' AND CIRNAME   IN ('ANANTHAPUR','TIRUPATI','KADAPA','NELLORE','KURNOOL')  AND CTGOVT_PVT=? \r\n"
						+ "    ORDER BY 1,2,3,4,5";
				return jdbcTemplate.queryForList(sql, new Object[] { month_year, status, month_year, status });
			}
		} else if (type.equalsIgnoreCase("div")) {
			String sql = "SELECT CIRNAME, DIVNAME, SUBNAME, SECNAME,USCNO,NVL(NET_ACD,0) ACD,DECODE(TRIM(CTGOVT_PVT),'Y','GOVT','N','PVT','') TYPE,A.LEVI_MTH \r\n"
					+ "    FROM ACD_CALC_HT A,CONS B,SPDCLMASTER C\r\n"
					+ "    WHERE A.ACCT_ID=B.CTACCT_ID AND SUBSTR(B.CTSECCD,-5)=C.SECCD AND LEVI_MTH=? AND TRIM(LEVI_FLG)='Y'  AND CIRNAME=?  AND CTGOVT_PVT=?  \r\n"
					+ "    UNION ALL\r\n"
					+ "    SELECT CIRNAME, DIVNAME, SUBNAME, SECNAME,USCNO,NVL(NET_ACD,0) ACD,DECODE(TRIM(CTGOVT_PVT),'Y','GOVT','N','PVT','') TYPE,A.LEVI_MTH \r\n"
					+ "    FROM ACD_CALC_HT_HIST A,CONS B,SPDCLMASTER C\r\n"
					+ "    WHERE A.ACCT_ID=B.CTACCT_ID AND SUBSTR(B.CTSECCD,-5)=C.SECCD AND LEVI_MTH=? AND TRIM(LEVI_FLG)='Y' AND CIRNAME=?   AND CTGOVT_PVT=?  \r\n"
					+ "    ORDER BY 1,2,3,4,5";
			return jdbcTemplate.queryForList(sql,
					new Object[] { month_year, circle, status, month_year, circle, status });
		} else if (type.equalsIgnoreCase("sub")) {
			String sql = "SELECT CIRNAME, DIVNAME, SUBNAME, SECNAME,USCNO,NVL(NET_ACD,0) ACD,DECODE(TRIM(CTGOVT_PVT),'Y','GOVT','N','PVT','') TYPE,A.LEVI_MTH \r\n"
					+ "    FROM ACD_CALC_HT A,CONS B,SPDCLMASTER C\r\n"
					+ "    WHERE A.ACCT_ID=B.CTACCT_ID AND SUBSTR(B.CTSECCD,-5)=C.SECCD AND LEVI_MTH=? AND TRIM(LEVI_FLG)='Y'  AND CIRNAME=?  AND CTGOVT_PVT=? AND DIVNAME=?  \r\n"
					+ "    UNION ALL\r\n"
					+ "    SELECT CIRNAME, DIVNAME, SUBNAME, SECNAME,USCNO,NVL(NET_ACD,0) ACD,DECODE(TRIM(CTGOVT_PVT),'Y','GOVT','N','PVT','') TYPE,A.LEVI_MTH \r\n"
					+ "    FROM ACD_CALC_HT_HIST A,CONS B,SPDCLMASTER C\r\n"
					+ "    WHERE A.ACCT_ID=B.CTACCT_ID AND SUBSTR(B.CTSECCD,-5)=C.SECCD AND LEVI_MTH=? AND TRIM(LEVI_FLG)='Y' AND CIRNAME=?   AND CTGOVT_PVT=?  AND DIVNAME=?  \r\n"
					+ "    ORDER BY 1,2,3,4,5";
			return jdbcTemplate.queryForList(sql,
					new Object[] { month_year, circle, status, division, month_year, circle, status, division });
		} else if (type.equalsIgnoreCase("sec")) {
			String sql = "SELECT CIRNAME, DIVNAME, SUBNAME, SECNAME,USCNO,NVL(NET_ACD,0) ACD,DECODE(TRIM(CTGOVT_PVT),'Y','GOVT','N','PVT','') TYPE,A.LEVI_MTH \r\n"
					+ "    FROM ACD_CALC_HT A,CONS B,SPDCLMASTER C\r\n"
					+ "    WHERE A.ACCT_ID=B.CTACCT_ID AND SUBSTR(B.CTSECCD,-5)=C.SECCD AND LEVI_MTH=? AND TRIM(LEVI_FLG)='Y'  AND CIRNAME=?  AND CTGOVT_PVT=? AND DIVNAME=?  AND SUBNAME=? \r\n"
					+ "    UNION ALL\r\n"
					+ "    SELECT CIRNAME, DIVNAME, SUBNAME, SECNAME,USCNO,NVL(NET_ACD,0) ACD,DECODE(TRIM(CTGOVT_PVT),'Y','GOVT','N','PVT','') TYPE,A.LEVI_MTH \r\n"
					+ "    FROM ACD_CALC_HT_HIST A,CONS B,SPDCLMASTER C\r\n"
					+ "    WHERE A.ACCT_ID=B.CTACCT_ID AND SUBSTR(B.CTSECCD,-5)=C.SECCD AND LEVI_MTH=? AND TRIM(LEVI_FLG)='Y' AND CIRNAME=?   AND CTGOVT_PVT=?  AND DIVNAME=? AND SUBNAME=? \r\n"
					+ "    ORDER BY 1,2,3,4,5";
			return jdbcTemplate.queryForList(sql, new Object[] { month_year, circle, status, division, subdivision,
					month_year, circle, status, division, subdivision });
		}
		return null;
	}

	public List<Map<String, Object>> getAcdSurcharAndBalanceRepot(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		String monthYear = request.getParameter("month") + "-" + request.getParameter("year");
		if (!circle.equalsIgnoreCase("ALL")) {
			String sql = "SELECT CTUSCNO,CTNAME,SURCHG ACD_SURCHG,NVL(CB_ACD,0) ACD_BALANCE FROM CONS,\r\n"
					+ "(SELECT USCNO,SUM(SURAMT) SURCHG FROM ACD_SURCHG WHERE LEVI_MTH=TO_CHAR(ADD_MONTHS(TO_DATE(?,'MON-YYYY'),-1),'MON-YYYY') GROUP BY USCNO) ACD_SURCHG,\r\n"
					+ "(SELECT USCNO,CB_ACD FROM ACD_SURCHG WHERE LEVI_MTH=TO_CHAR(ADD_MONTHS(TO_DATE(?,'MON-YYYY'),-1),'MON-YYYY') AND TRTYPE='CB' AND CB_ACD>=0) ACD_BAL \r\n"
					+ "WHERE CTUSCNO=ACD_SURCHG.USCNO(+)\r\n"
					+ "AND CTUSCNO=ACD_BAL.USCNO(+) AND SURCHG>0 AND SUBSTR(CTUSCNO,0,3)=? \r\n" + "UNION ALL\r\n"
					+ "SELECT CTUSCNO,CTNAME,SURCHG ACD_SURCHG,NVL(CB_ACD,0) ACD_BALANCE FROM CONS,\r\n"
					+ "(SELECT USCNO,SUM(SURAMT) SURCHG FROM ACD_SURCHG_HIST WHERE LEVI_MTH=TO_CHAR(ADD_MONTHS(TO_DATE(?,'MON-YYYY'),-1),'MON-YYYY') GROUP BY USCNO) ACD_SURCHG,\r\n"
					+ "(SELECT USCNO,CB_ACD FROM ACD_SURCHG_HIST WHERE LEVI_MTH=TO_CHAR(ADD_MONTHS(TO_DATE(?,'MON-YYYY'),-1),'MON-YYYY') AND TRTYPE='CB' AND CB_ACD>=0) ACD_BAL \r\n"
					+ "WHERE CTUSCNO=ACD_SURCHG.USCNO(+)\r\n"
					+ "AND CTUSCNO=ACD_BAL.USCNO(+) AND SURCHG>0 AND SUBSTR(CTUSCNO,0,3)=? ";
			log.info(sql);
			return jdbcTemplate.queryForList(sql,
					new Object[] { monthYear, monthYear, circle, monthYear, monthYear, circle });
		} else {
			String sql = "SELECT CTUSCNO,CTNAME,SURCHG ACD_SURCHG,NVL(CB_ACD,0) ACD_BALANCE FROM CONS,\r\n"
					+ "(SELECT USCNO,SUM(SURAMT) SURCHG FROM ACD_SURCHG WHERE LEVI_MTH=TO_CHAR(ADD_MONTHS(TO_DATE(?,'MON-YYYY'),-1),'MON-YYYY') GROUP BY USCNO) ACD_SURCHG,\r\n"
					+ "(SELECT USCNO,CB_ACD FROM ACD_SURCHG WHERE LEVI_MTH=TO_CHAR(ADD_MONTHS(TO_DATE(?,'MON-YYYY'),-1),'MON-YYYY') AND TRTYPE='CB' AND CB_ACD>=0) ACD_BAL \r\n"
					+ "WHERE CTUSCNO=ACD_SURCHG.USCNO(+)\r\n" + "AND CTUSCNO=ACD_BAL.USCNO(+) AND SURCHG>0  \r\n"
					+ "UNION ALL\r\n"
					+ "SELECT CTUSCNO,CTNAME,SURCHG ACD_SURCHG,NVL(CB_ACD,0) ACD_BALANCE FROM CONS,\r\n"
					+ "(SELECT USCNO,SUM(SURAMT) SURCHG FROM ACD_SURCHG_HIST WHERE LEVI_MTH=TO_CHAR(ADD_MONTHS(TO_DATE(?,'MON-YYYY'),-1),'MON-YYYY') GROUP BY USCNO) ACD_SURCHG,\r\n"
					+ "(SELECT USCNO,CB_ACD FROM ACD_SURCHG_HIST WHERE LEVI_MTH=TO_CHAR(ADD_MONTHS(TO_DATE(?,'MON-YYYY'),-1),'MON-YYYY') AND TRTYPE='CB' AND CB_ACD>=0) ACD_BAL \r\n"
					+ "WHERE CTUSCNO=ACD_SURCHG.USCNO(+)\r\n" + "AND CTUSCNO=ACD_BAL.USCNO(+) AND SURCHG>0  ";
			log.info(sql);
			return jdbcTemplate.queryForList(sql, new Object[] { monthYear, monthYear, monthYear, monthYear });
		}

	}

	public List<Map<String, Object>> getDlistAbstractReport(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		if (circle.equals("ALL")) {
			String sql = "SELECT CIRCLE,SUM(DECODE(TRIM(GOV_PVT),'GOVT',1,0)) GOVT_SCS,round(SUM(DECODE(TRIM(GOV_PVT),'GOVT',ROUND(NVL(CB,0)),0))/10000000,2) GOVT_CB,\r\n"
					+ "SUM(DECODE(TRIM(GOV_PVT),'PVT',1,0)) PVT_SCS,round(SUM(DECODE(TRIM(GOV_PVT),'PVT',ROUND(NVL(CB,0)),0))/10000000,2 ) PVT_CB\r\n"
					+ "FROM D_LIST \r\n" + "GROUP BY CIRCLE ORDER BY 1";
			return jdbcTemplate.queryForList(sql);

		} else if (circle.equalsIgnoreCase("SPDCL")) {
			String sql = "SELECT CIRCLE,SUM(DECODE(TRIM(GOV_PVT),'GOVT',1,0)) GOVT_SCS,round(SUM(DECODE(TRIM(GOV_PVT),'GOVT',ROUND(NVL(CB,0)),0))/10000000,2) GOVT_CB,\r\n"
					+ "SUM(DECODE(TRIM(GOV_PVT),'PVT',1,0)) PVT_SCS,round(SUM(DECODE(TRIM(GOV_PVT),'PVT',ROUND(NVL(CB,0)),0))/10000000,2 ) PVT_CB\r\n"
					+ "FROM D_LIST WHERE SUBSTR(SCNO,1,3)  NOT IN ('ONG','GNT','VJA','CRD')\r\n"
					+ "GROUP BY CIRCLE ORDER BY 1";
			return jdbcTemplate.queryForList(sql);
		} else {
			String sql = "SELECT CIRCLE,SUM(DECODE(TRIM(GOV_PVT),'GOVT',1,0)) GOVT_SCS,round(SUM(DECODE(TRIM(GOV_PVT),'GOVT',ROUND(NVL(CB,0)),0))/10000000,2) GOVT_CB,\r\n"
					+ "SUM(DECODE(TRIM(GOV_PVT),'PVT',1,0)) PVT_SCS,round(SUM(DECODE(TRIM(GOV_PVT),'PVT',ROUND(NVL(CB,0)),0))/10000000,2 ) PVT_CB\r\n"
					+ "FROM D_LIST WHERE SUBSTR(SCNO,1,3)   IN ('ONG','GNT','VJA','CRD')\r\n"
					+ "GROUP BY CIRCLE ORDER BY 1";
			return jdbcTemplate.queryForList(sql);
		}
	}

	public List<Map<String, Object>> getDlistReportServices(HttpServletRequest request) {
		String type = request.getParameter("type");
		String circle = request.getParameter("cir").trim();
		String division = request.getParameter("div").trim();
		String subdivision = request.getParameter("sub").trim();
		String section = request.getParameter("sec").trim();
		String status = request.getParameter("status").trim();
		String stype = request.getParameter("stype");
		if (type.equalsIgnoreCase("all")) {
			if (stype.equalsIgnoreCase("ALL")) {
				String sql = "SELECT CIRCLE,DIVISION,SUB_DIV,SECNAME,SCNO,CAT,LOAD,GOV_PVT,CB FROM D_LIST WHERE GOV_PVT=?";
				return jdbcTemplate.queryForList(sql, new Object[] { status });
			} else if (stype.equalsIgnoreCase("SPDCL")) {
				String sql = "SELECT CIRCLE,DIVISION,SUB_DIV,SECNAME,SCNO,CAT,LOAD,GOV_PVT,CB FROM D_LIST WHERE GOV_PVT=? \r\n"
						+ "AND  SUBSTR(SCNO,1,3)  NOT IN ('ONG','GNT','VJA','CRD')\r\n" + "ORDER BY CIRCLE";
				return jdbcTemplate.queryForList(sql, new Object[] { status });
			} else {
				String sql = "SELECT CIRCLE,DIVISION,SUB_DIV,SECNAME,SCNO,CAT,LOAD,GOV_PVT,CB FROM D_LIST WHERE GOV_PVT=? \r\n"
						+ "AND  SUBSTR(SCNO,1,3)   IN ('ONG','GNT','VJA','CRD')\r\n" + "ORDER BY CIRCLE";
				return jdbcTemplate.queryForList(sql, new Object[] { status });
			}
		} else if (type.equalsIgnoreCase("div")) {
			String sql = "SELECT CIRCLE,DIVISION,SUB_DIV,SECNAME,SCNO,CAT,LOAD,GOV_PVT,CB FROM D_LIST WHERE GOV_PVT=? AND CIRCLE=? ";
			return jdbcTemplate.queryForList(sql, new Object[] { status, circle });
		} else if (type.equalsIgnoreCase("sub")) {
			String sql = "SELECT CIRCLE,DIVISION,SUB_DIV,SECNAME,SCNO,CAT,LOAD,GOV_PVT,CB FROM D_LIST WHERE GOV_PVT=? AND CIRCLE=? AND DIVISION=?";
			return jdbcTemplate.queryForList(sql, new Object[] { status, circle, division });
		} else if (type.equalsIgnoreCase("sec")) {
			String sql = "SELECT CIRCLE,DIVISION,SUB_DIV,SECNAME,SCNO,CAT,LOAD,GOV_PVT,CB FROM D_LIST WHERE GOV_PVT=? AND CIRCLE=? AND DIVISION=? AND SUB_DIV=? ";
			return jdbcTemplate.queryForList(sql, new Object[] { status, circle, division, subdivision });
		}
		return Collections.emptyList();
	}

	public List<Map<String, Object>> getAllCirclesDlist(String type, String circle, String division, String month_year,
			String subdivision, String section) {
		if (type.equalsIgnoreCase("div")) {
			String sql = "SELECT DIVISION,SUM(DECODE(TRIM(GOV_PVT),'GOVT',1,0)) GOVT_SCS,round(SUM(DECODE(TRIM(GOV_PVT),'GOVT',ROUND(NVL(CB,0)),0))/10000000,2) GOVT_CB,\r\n"
					+ "SUM(DECODE(TRIM(GOV_PVT),'PVT',1,0)) PVT_SCS,round(SUM(DECODE(TRIM(GOV_PVT),'PVT',ROUND(NVL(CB,0)),0))/10000000,2 ) PVT_CB\r\n"
					+ "FROM D_LIST WHERE  CIRCLE =? \r\n" + "GROUP BY DIVISION ORDER BY 1";
			return jdbcTemplate.queryForList(sql, new Object[] { circle });
		} else if (type.equalsIgnoreCase("sub")) {
			String sql = "SELECT SUB_DIV,SUM(DECODE(TRIM(GOV_PVT),'GOVT',1,0)) GOVT_SCS,round(SUM(DECODE(TRIM(GOV_PVT),'GOVT',ROUND(NVL(CB,0)),0))/10000000,2) GOVT_CB,\r\n"
					+ "SUM(DECODE(TRIM(GOV_PVT),'PVT',1,0)) PVT_SCS,round(SUM(DECODE(TRIM(GOV_PVT),'PVT',ROUND(NVL(CB,0)),0))/10000000,2 ) PVT_CB\r\n"
					+ "FROM D_LIST WHERE   CIRCLE =? AND DIVISION=? \r\n" + "GROUP BY SUB_DIV ORDER BY 1";
			return jdbcTemplate.queryForList(sql, new Object[] { circle, division });
		} else if (type.equalsIgnoreCase("sec")) {
			String sql = "SELECT SECNAME,SUM(DECODE(TRIM(GOV_PVT),'GOVT',1,0)) GOVT_SCS,round(SUM(DECODE(TRIM(GOV_PVT),'GOVT',ROUND(NVL(CB,0)),0))/10000000,2) GOVT_CB,\r\n"
					+ "SUM(DECODE(TRIM(GOV_PVT),'PVT',1,0)) PVT_SCS,round(SUM(DECODE(TRIM(GOV_PVT),'PVT',ROUND(NVL(CB,0)),0))/10000000,2 ) PVT_CB\r\n"
					+ "FROM D_LIST WHERE   CIRCLE =? AND DIVISION=? AND SUB_DIV=?\r\n" + "GROUP BY SECNAME ORDER BY 1";
			return jdbcTemplate.queryForList(sql, new Object[] { circle, division, subdivision });
		}
		return Collections.emptyList();
	}

	public List<Map<String, Object>> getAllCirclesDlistServices(HttpServletRequest request) {
		String type = request.getParameter("type");
		String circle = request.getParameter("cir").trim();
		String division = request.getParameter("div").trim();
		String subdivision = request.getParameter("sub").trim();
		String section = request.getParameter("sec").trim();
		String status = request.getParameter("status").trim();
		if (type.equalsIgnoreCase("cir")) {
			String sql = "SELECT CIRCLE,DIVISION,SUB_DIV,SECNAME,SCNO,CAT,LOAD,GOV_PVT,CB FROM D_LIST WHERE GOV_PVT=? AND CIRCLE=? ";
			return jdbcTemplate.queryForList(sql, new Object[] { status, circle });
		} else if (type.equalsIgnoreCase("div")) {
			String sql = "SELECT CIRCLE,DIVISION,SUB_DIV,SECNAME,SCNO,CAT,LOAD,GOV_PVT,CB FROM D_LIST WHERE GOV_PVT=? AND CIRCLE=? AND DIVISION=?";
			return jdbcTemplate.queryForList(sql, new Object[] { status, circle, division });
		} else if (type.equalsIgnoreCase("sub")) {
			String sql = "SELECT CIRCLE,DIVISION,SUB_DIV,SECNAME,SCNO,CAT,LOAD,GOV_PVT,CB FROM D_LIST WHERE GOV_PVT=? AND CIRCLE=? AND DIVISION=? AND SUB_DIV=? ";
			return jdbcTemplate.queryForList(sql, new Object[] { status, circle, division, subdivision });
		} else if (type.equalsIgnoreCase("sec")) {
			String sql = "SELECT CIRCLE,DIVISION,SUB_DIV,SECNAME,SCNO,CAT,LOAD,GOV_PVT,CB FROM D_LIST WHERE GOV_PVT=? AND CIRCLE=? AND DIVISION=? AND SUB_DIV=? AND SECNAME=? ";
			return jdbcTemplate.queryForList(sql, new Object[] { status, circle, division, subdivision, section });
		}
		return Collections.emptyList();
	}

	public List<Map<String, Object>> getNoemailsSentDetails(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		String monthYear = request.getParameter("month") + "-" + request.getParameter("year");
		if (!circle.equals("ALL")) {
			try {
				/*
				 * String sql =
				 * "SELECT A.CIRCLE,A.CTEMAILID,B.EMAIL,c.mobile,d.ctmobile FROM\r\n" +
				 * "(SELECT SUBSTR(CTUSCNO,1,3)CIRCLE,COUNT(CTEMAILID)CTEMAILID FROM CONS WHERE  CTEMAILID NOT IN(' ','NA','---','0','-','--')   GROUP BY SUBSTR(CTUSCNO,1,3))A,\r\n"
				 * +
				 * "(SELECT SUBSTR(SCNO,1,3)CIRCLE,COUNT(EMAIL)EMAIL FROM SMS_EMAIL_SENT WHERE MOBILE='-' AND EMAIL!='-' AND FLAG='S' AND TO_CHAR(MTH,'MON-YYYY')=? GROUP BY  SUBSTR(SCNO,1,3))B,\r\n"
				 * +
				 * "(SELECT SUBSTR(SCNO,1,3)CIRCLE,COUNT(MOBILE)MOBILE FROM SMS_EMAIL_SENT WHERE MOBILE!='-' AND EMAIL='-' AND FLAG='S' AND TO_CHAR(MTH,'MON-YYYY')=? GROUP BY  SUBSTR(SCNO,1,3))C,\r\n"
				 * +
				 * "(SELECT SUBSTR(CTUSCNO,1,3)CIRCLE,COUNT(ctmobile)ctmobile FROM CONS WHERE  ctmobile NOT IN(' ','NA','---','0','-','--')  and CONS.CTSTATUS=1  GROUP BY SUBSTR(CTUSCNO,1,3))D\r\n"
				 * +
				 * "WHERE A.CIRCLE=B.CIRCLE(+) AND A.CIRCLE=C.CIRCLE(+) AND A.CIRCLE=D.CIRCLE(+) AND A.CIRCLE=? ORDER BY CIRCLE"
				 * ;
				 */
				String sql = " SELECT A.CIRCLE,A.CTEMAILID,B.EMAIL,c.mobile,d.ctmobile FROM\r\n"
						+ "(SELECT SUBSTR(CTUSCNO,1,3)CIRCLE,COUNT(CTEMAILID)CTEMAILID FROM CONS WHERE  CTEMAILID NOT IN(' ','NA','---','0','-','--')   GROUP BY SUBSTR(CTUSCNO,1,3))A,\r\n"
						+ "(SELECT SUBSTR(SCNO,1,3)CIRCLE,COUNT(EMAIL)EMAIL FROM (select SCNO,EMAIL,MOBILE,FLAG,TO_CHAR(MTH,'MON-YYYY') MTH from \r\n"
						+ "SMS_EMAIL_SENT WHERE MOBILE='-' AND EMAIL!='-' AND FLAG='S' AND TO_CHAR(MTH,'MON-YYYY')=? group by SCNO,EMAIL,MOBILE,FLAG,TO_CHAR(MTH,'MON-YYYY'))GROUP BY  SUBSTR(SCNO,1,3))B,\r\n"
						+ "(SELECT SUBSTR(SCNO,1,3)CIRCLE,COUNT(MOBILE)MOBILE FROM (select SCNO,EMAIL,MOBILE,FLAG,TO_CHAR(MTH,'MON-YYYY') MTH from \r\n"
						+ "SMS_EMAIL_SENT \r\n"
						+ "WHERE MOBILE!='-' AND EMAIL='-' AND FLAG='S' AND TO_CHAR(MTH,'MON-YYYY')=?  group by SCNO,EMAIL,MOBILE,FLAG,TO_CHAR(MTH,'MON-YYYY') ) GROUP BY  SUBSTR(SCNO,1,3))C,\r\n"
						+ "(SELECT SUBSTR(CTUSCNO,1,3)CIRCLE,COUNT(ctmobile)ctmobile FROM CONS WHERE  ctmobile NOT IN(' ','NA','---','0','-','--')  and CONS.CTSTATUS=1  GROUP BY SUBSTR(CTUSCNO,1,3))D\r\n"
						+ "WHERE A.CIRCLE=B.CIRCLE(+) AND A.CIRCLE=C.CIRCLE(+) AND A.CIRCLE=D.CIRCLE(+)  AND A.CIRCLE=? ORDER BY CIRCLE";
				log.info(sql);
				return jdbcTemplate.queryForList(sql, new Object[] { monthYear, monthYear, circle });
			} catch (DataAccessException e) {
				e.printStackTrace();
				log.info(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}
		} else {
			try {
				String sql = " SELECT A.CIRCLE,A.CTEMAILID,B.EMAIL,c.mobile,d.ctmobile FROM\r\n"
						+ "(SELECT SUBSTR(CTUSCNO,1,3)CIRCLE,COUNT(CTEMAILID)CTEMAILID FROM CONS WHERE  CTEMAILID NOT IN(' ','NA','---','0','-','--')   GROUP BY SUBSTR(CTUSCNO,1,3))A,\r\n"
						+ "(SELECT SUBSTR(SCNO,1,3)CIRCLE,COUNT(EMAIL)EMAIL FROM (select SCNO,EMAIL,MOBILE,FLAG,TO_CHAR(MTH,'MON-YYYY') MTH from \r\n"
						+ "SMS_EMAIL_SENT WHERE MOBILE='-' AND EMAIL!='-' AND FLAG='S' AND TO_CHAR(MTH,'MON-YYYY')=? group by SCNO,EMAIL,MOBILE,FLAG,TO_CHAR(MTH,'MON-YYYY'))GROUP BY  SUBSTR(SCNO,1,3))B,\r\n"
						+ "(SELECT SUBSTR(SCNO,1,3)CIRCLE,COUNT(MOBILE)MOBILE FROM (select SCNO,EMAIL,MOBILE,FLAG,TO_CHAR(MTH,'MON-YYYY') MTH from \r\n"
						+ "SMS_EMAIL_SENT \r\n"
						+ "WHERE MOBILE!='-' AND EMAIL='-' AND FLAG='S' AND TO_CHAR(MTH,'MON-YYYY')=?  group by SCNO,EMAIL,MOBILE,FLAG,TO_CHAR(MTH,'MON-YYYY') ) GROUP BY  SUBSTR(SCNO,1,3))C,\r\n"
						+ "(SELECT SUBSTR(CTUSCNO,1,3)CIRCLE,COUNT(ctmobile)ctmobile FROM CONS WHERE  ctmobile NOT IN(' ','NA','---','0','-','--')  and CONS.CTSTATUS=1  GROUP BY SUBSTR(CTUSCNO,1,3))D\r\n"
						+ "WHERE A.CIRCLE=B.CIRCLE(+) AND A.CIRCLE=C.CIRCLE(+) AND A.CIRCLE=D.CIRCLE(+) ORDER BY CIRCLE";
				/*
				 * String sql =
				 * "SELECT A.CIRCLE,A.CTEMAILID,B.EMAIL,c.mobile,d.ctmobile FROM\r\n" +
				 * "(SELECT SUBSTR(CTUSCNO,1,3)CIRCLE,COUNT(CTEMAILID)CTEMAILID FROM CONS WHERE  CTEMAILID NOT IN(' ','NA','---','0','-','--')   GROUP BY SUBSTR(CTUSCNO,1,3))A,\r\n"
				 * +
				 * "(SELECT SUBSTR(SCNO,1,3)CIRCLE,COUNT(EMAIL)EMAIL FROM SMS_EMAIL_SENT WHERE MOBILE='-' AND EMAIL!='-' AND FLAG='S' AND TO_CHAR(MTH,'MON-YYYY')=? GROUP BY  SUBSTR(SCNO,1,3))B,\r\n"
				 * +
				 * "(SELECT SUBSTR(SCNO,1,3)CIRCLE,COUNT(MOBILE)MOBILE FROM SMS_EMAIL_SENT WHERE MOBILE!='-' AND EMAIL='-' AND FLAG='S' AND TO_CHAR(MTH,'MON-YYYY')=? GROUP BY  SUBSTR(SCNO,1,3))C,\r\n"
				 * +
				 * "(SELECT SUBSTR(CTUSCNO,1,3)CIRCLE,COUNT(ctmobile)ctmobile FROM CONS WHERE  ctmobile NOT IN(' ','NA','---','0','-','--')  and CONS.CTSTATUS=1  GROUP BY SUBSTR(CTUSCNO,1,3))D\r\n"
				 * +
				 * "WHERE A.CIRCLE=B.CIRCLE(+) AND A.CIRCLE=C.CIRCLE(+) AND A.CIRCLE=D.CIRCLE(+) ORDER BY CIRCLE"
				 * ;
				 */
				log.info(sql);
				return jdbcTemplate.queryForList(sql, new Object[] { monthYear, monthYear });
			} catch (DataAccessException e) {
				e.printStackTrace();
				log.info(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}
		}
	}

	public List<Map<String, Object>> getBillAnalysisReport(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		String monthYear = request.getParameter("month") + "-" + request.getParameter("year");
		if (circle.equalsIgnoreCase("ALL")) {

			String sql = "SELECT  TO_CHAR(BTBLDT,'MON-YYYY')BTBLDT,BTBLCAT||BTBLSCAT BTBLCAT,BTCMD_HT,BTACTUAL_KV,BTSCNO,BTRKWH_HT,BTRKVAH_HT,BTBKVAH,((BTTOD2+BTTOD5)-(BTTOD1+BTTOD6))TOD_UNITS,\r\n"
					+ "BTDEMCHG_NOR,BTENGCHG_NOR,BTTODCHG_HT,BTED,BTCOLNYCHG_HT,BTDEMCHG_PEN,BTENGCHG_PEN,BTVOLTSURCHG,BTAQUASUB_CHG,BTLFINCENTIVE_HT,BTCROSSSUBCHG,BTADLCHG,BTED_INT,BTOTHERCHG,BTDTRHIRE_SGST,BTDTRHIRE_CGST,BTACDSURCHG,BTCUSTCHG,BTWHEELCHGCASH_HT,\r\n"
					+ "BTBLKVA_HT BMD,BTRKVA_HT RMD,BTCOURT_LPC,BTCURDEM, Nvl(BT_TU_CHG,0)+nvl(BT_LTU_CHG,0) BT_TU_CHG\r\n"
					+ "FROM BILL_HIST\r\n" + "WHERE TO_CHAR(BTBLDT,'MON-YYYY')=? \r\n" + "UNION ALL\r\n"
					+ "SELECT  TO_CHAR(BTBLDT,'MON-YYYY')BTBLDT,BTBLCAT||BTBLSCAT BTBLCAT,BTCMD_HT,BTACTUAL_KV,BTSCNO,BTRKWH_HT,BTRKVAH_HT,BTBKVAH,((BTTOD2+BTTOD5)-(BTTOD1+BTTOD6))TOD_UNITS,\r\n"
					+ "BTDEMCHG_NOR,BTENGCHG_NOR,BTTODCHG_HT,BTED,BTCOLNYCHG_HT,BTDEMCHG_PEN,BTENGCHG_PEN,BTVOLTSURCHG,BTAQUASUB_CHG,BTLFINCENTIVE_HT,BTCROSSSUBCHG,BTADLCHG,BTED_INT,BTOTHERCHG,BTDTRHIRE_SGST,BTDTRHIRE_CGST,BTACDSURCHG,BTCUSTCHG,BTWHEELCHGCASH_HT,\r\n"
					+ "BTBLKVA_HT BMD,BTRKVA_HT RMD,BTCOURT_LPC,BTCURDEM,Nvl(BT_TU_CHG,0)+nvl(BT_LTU_CHG,0) BT_TU_CHG\r\n"
					+ "FROM BILL\r\n" + "WHERE TO_CHAR(BTBLDT,'MON-YYYY')=? \r\n" + "";
			log.info(sql);
			return jdbcTemplate.queryForList(sql, new Object[] { monthYear, monthYear });
		} else {
			String sql = "SELECT  TO_CHAR(BTBLDT,'MON-YYYY')BTBLDT,BTBLCAT||BTBLSCAT BTBLCAT,BTCMD_HT,BTACTUAL_KV,BTSCNO,BTRKWH_HT,BTRKVAH_HT,BTBKVAH,((BTTOD2+BTTOD5)-(BTTOD1+BTTOD6))TOD_UNITS,\r\n"
					+ "BTDEMCHG_NOR,BTENGCHG_NOR,BTTODCHG_HT,BTED,BTCOLNYCHG_HT,BTDEMCHG_PEN,BTENGCHG_PEN,BTVOLTSURCHG,BTAQUASUB_CHG,BTLFINCENTIVE_HT,BTCROSSSUBCHG,BTADLCHG,BTED_INT,BTOTHERCHG,BTDTRHIRE_SGST,BTDTRHIRE_CGST,BTACDSURCHG,BTCUSTCHG,BTWHEELCHGCASH_HT,\r\n"
					+ "BTBLKVA_HT BMD,BTRKVA_HT RMD,BTCOURT_LPC,BTCURDEM,Nvl(BT_TU_CHG,0)+nvl(BT_LTU_CHG,0) BT_TU_CHG\r\n"
					+ "FROM BILL_HIST\r\n" + "WHERE TO_CHAR(BTBLDT,'MON-YYYY')=? AND SUBSTR(BTSCNO,0,3)=? \r\n"
					+ "UNION ALL\r\n"
					+ "SELECT  TO_CHAR(BTBLDT,'MON-YYYY')BTBLDT,BTBLCAT||BTBLSCAT BTBLCAT,BTCMD_HT,BTACTUAL_KV,BTSCNO,BTRKWH_HT,BTRKVAH_HT,BTBKVAH,((BTTOD2+BTTOD5)-(BTTOD1+BTTOD6))TOD_UNITS,\r\n"
					+ "BTDEMCHG_NOR,BTENGCHG_NOR,BTTODCHG_HT,BTED,BTCOLNYCHG_HT,BTDEMCHG_PEN,BTENGCHG_PEN,BTVOLTSURCHG,BTAQUASUB_CHG,BTLFINCENTIVE_HT,BTCROSSSUBCHG,BTADLCHG,BTED_INT,BTOTHERCHG,BTDTRHIRE_SGST,BTDTRHIRE_CGST,BTACDSURCHG,BTCUSTCHG,BTWHEELCHGCASH_HT,\r\n"
					+ "BTBLKVA_HT BMD,BTRKVA_HT RMD,BTCOURT_LPC,BTCURDEM,Nvl(BT_TU_CHG,0)+nvl(BT_LTU_CHG,0) BT_TU_CHG\r\n"
					+ "FROM BILL\r\n" + "WHERE TO_CHAR(BTBLDT,'MON-YYYY')=? AND SUBSTR(BTSCNO,0,3)=? \r\n" + "";
			log.info(sql);
			return jdbcTemplate.queryForList(sql, new Object[] { monthYear, circle, monthYear, circle });
		}
	}

	public List<Map<String, Object>> getVoltagedemand(HttpServletRequest request) {

		String circle = request.getParameter("circle");
		String monthYear = request.getParameter("month") + "-" + request.getParameter("year");
		if (circle.equals("ALL")) {
			String sql = "SELECT SUBSTR(BTSCNO,0,3)CIRCLE,COUNT(*)AS NOS,BTACTUAL_KV,SUM(BTRKWH_HT)BTRKWH_HT,SUM(BTBKVAH)BTBKVAH, SUM(CB_CCLPC)CB_CCLPC,SUM(BTCURDEM)BTCURDEM \r\n"
					+ "FROM BILL WHERE TO_CHAR(BTBLDT,'MON-YYYY')=?  \r\n"
					+ "GROUP BY BTACTUAL_KV,SUBSTR(BTSCNO,0,3)\r\n" + "UNION ALL\r\n"
					+ "SELECT SUBSTR(BTSCNO,0,3)CIRCLE,COUNT(*)AS NOS,BTACTUAL_KV,SUM(BTRKWH_HT)BTRKWH_HT,SUM(BTBKVAH)BTBKVAH, SUM(CB_CCLPC)CB_CCLPC,SUM(BTCURDEM)BTCURDEM \r\n"
					+ "FROM BILL_HIST WHERE TO_CHAR(BTBLDT,'MON-YYYY')=? \r\n"
					+ "GROUP BY BTACTUAL_KV,SUBSTR(BTSCNO,0,3)\r\n" + "ORDER BY CIRCLE,BTACTUAL_KV";
			log.info(sql);
			return jdbcTemplate.queryForList(sql, new Object[] { monthYear, monthYear });
		} else {
			String sql = "SELECT SUBSTR(BTSCNO,0,3)CIRCLE,COUNT(*)AS NOS,BTACTUAL_KV,SUM(BTRKWH_HT)BTRKWH_HT,SUM(BTBKVAH)BTBKVAH, SUM(CB_CCLPC)CB_CCLPC,SUM(BTCURDEM)BTCURDEM \r\n"
					+ "FROM BILL WHERE TO_CHAR(BTBLDT,'MON-YYYY')=? AND SUBSTR(BTSCNO,0,3)=? \r\n"
					+ "GROUP BY BTACTUAL_KV,SUBSTR(BTSCNO,0,3)\r\n" + "UNION ALL\r\n"
					+ "SELECT SUBSTR(BTSCNO,0,3)CIRCLE,COUNT(*)AS NOS,BTACTUAL_KV,SUM(BTRKWH_HT)BTRKWH_HT,SUM(BTBKVAH)BTBKVAH, SUM(CB_CCLPC)CB_CCLPC,SUM(BTCURDEM)BTCURDEM \r\n"
					+ "FROM BILL_HIST WHERE TO_CHAR(BTBLDT,'MON-YYYY')=? AND SUBSTR(BTSCNO,0,3)=? \r\n"
					+ "GROUP BY BTACTUAL_KV,SUBSTR(BTSCNO,0,3)\r\n" + "ORDER BY CIRCLE,BTACTUAL_KV";
			log.info(sql);
			return jdbcTemplate.queryForList(sql, new Object[] { monthYear, circle, monthYear, circle });
		}
	}

	public List<Map<String, Object>> getFYVoltagedemand(HttpServletRequest request) {

		String circle = request.getParameter("circle");
		String fin_year = request.getParameter("year");
		String fromdate = "01-APR-" + fin_year.split("-")[0];
		String todate = "31-MAR-" + fin_year.split("-")[1];
		if (circle.equals("ALL")) {
			String sql = "SELECT UNIQUE CTACTUAL_KV,SUM(NVL(REC_KWH,0)) RKWH,SUM(NVL(REC_KVAH,0)) RKVAH FROM CONS,LEDGER_HT_HIST WHERE CTUSCNO=USCNO AND TO_DATE(MON_YEAR,'MON-YYYY') BETWEEN ? AND ? GROUP BY CTACTUAL_KV";
			log.info(sql);
			return jdbcTemplate.queryForList(sql, new Object[] { fromdate, todate });
		} else {
			String sql = "SELECT UNIQUE CTACTUAL_KV,SUM(NVL(REC_KWH,0)) RKWH,SUM(NVL(REC_KVAH,0)) RKVAH FROM CONS,LEDGER_HT_HIST WHERE CTUSCNO=USCNO AND SUBSTR(CTUSCNO,1,3) = ? AND TO_DATE(MON_YEAR,'MON-YYYY') BETWEEN ? AND ? GROUP BY CTACTUAL_KV";
			log.info(sql);
			return jdbcTemplate.queryForList(sql, new Object[] { circle, fromdate, todate });
		}
	}

	public List<Map<String, Object>> getKwhmonthlyreport(HttpServletRequest request) {

		String circle = request.getParameter("circle");
		String fin_year = request.getParameter("year");
		String fromdate = "01-04-" + fin_year.split("-")[0];
		String todate = "31-03-" + fin_year.split("-")[1];
		if (circle.equals("ALL")) {
			String sql = "SELECT * FROM\r\n" + "(\r\n"
					+ "SELECT UNIQUE CASE WHEN ctcat='HT1' THEN 'DOMESTIC' WHEN CTCAT='HT2' THEN 'COMMERCIAL' WHEN CTCAT='HT3'  THEN CASE WHEN  CTACTUAL_KV=11 THEN 'INDUSTRIAL-LOW_MED_V' \r\n"
					+ "WHEN CTACTUAL_KV=33 THEN 'INDUSTRIAL-HV'  WHEN CTACTUAL_KV>33 THEN 'INDUSTRIAL-EHV' END  WHEN CTCAT='HT4' AND CTSUBCAT='D' THEN 'RAILWAYS'\r\n"
					+ "WHEN CTCAT='HT5' AND CTSUBCAT='E' THEN 'IRRIGATION' WHEN CTCAT='HT4' AND CTSUBCAT='A' THEN 'PUBLIC_UTILITIES' \r\n"
					+ "WHEN CTCAT='HT4' AND CTSUBCAT IN ('B','C') THEN 'OTHERS'\r\n"
					+ "WHEN CTCAT='HT5' AND CTSUBCAT IN ('B') THEN 'OTHERS' END CATEGORY,MON_YEAR,ROUND(SUM(NVL(REC_KWH,0)/1000000),2) REC_KWH_MU from cons,ledger_ht_hist \r\n"
					+ "where CTUSCNO=USCNO and to_date(MON_YEAR,'MON-YYYY') between to_date(?,'DD-MM-YYYY') and to_date(?,'DD-MM-YYYY')\r\n"
					+ "GROUP BY \r\n" + "CASE WHEN ctcat='HT1' THEN 'DOMESTIC'\r\n"
					+ "WHEN CTCAT='HT2' THEN 'COMMERCIAL' \r\n" + "WHEN CTCAT='HT3'  THEN \r\n"
					+ "CASE WHEN  CTACTUAL_KV=11 THEN 'INDUSTRIAL-LOW_MED_V' \r\n"
					+ "WHEN CTACTUAL_KV=33 THEN 'INDUSTRIAL-HV'  \r\n"
					+ "WHEN CTACTUAL_KV>33 THEN 'INDUSTRIAL-EHV' END  \r\n"
					+ "WHEN CTCAT='HT4' AND CTSUBCAT='D' THEN 'RAILWAYS' \r\n"
					+ "WHEN CTCAT='HT5' AND CTSUBCAT='E' THEN 'IRRIGATION'\r\n"
					+ "WHEN CTCAT='HT4' AND CTSUBCAT='A' THEN 'PUBLIC_UTILITIES' \r\n"
					+ "WHEN CTCAT='HT4' AND CTSUBCAT IN ('B','C') THEN 'OTHERS' \r\n"
					+ "WHEN CTCAT='HT5' AND CTSUBCAT IN ('B') THEN 'OTHERS' END,MON_YEAR  ORDER BY \r\n"
					+ "     case when CATEGORY='DOMESTIC' then 1 \r\n" + "     when CATEGORY='COMMERCIAL' then 2 \r\n"
					+ "     when CATEGORY='INDUSTRIAL-LOW_MED_V' then 3 \r\n"
					+ "     when CATEGORY='INDUSTRIAL-HV' then 4\r\n"
					+ "     when CATEGORY='INDUSTRIAL-EHV' then 5 \r\n" + "      when CATEGORY='RAILWAYS' then 6\r\n"
					+ "       when CATEGORY='IRRIGATION' then 7 \r\n"
					+ "        when CATEGORY='PUBLIC_UTILITIES' then 8 \r\n"
					+ "        when CATEGORY='OTHERS' then 9\r\n" + "     end,to_date(MON_YEAR,'MON-YYYY') \r\n"
					+ "     )\r\n" + "PIVOT\r\n" + "(\r\n" + "sum(REC_KWH_MU)\r\n"
					+ "FOR category in ('DOMESTIC' DOMESTIC,'COMMERCIAL'COMMERCIAL,'INDUSTRIAL-LOW_MED_V'INDUSTRIAL_LOW_MED_V,'INDUSTRIAL-HV'INDUSTRIAL_HV,'INDUSTRIAL-EHV'INDUSTRIAL_EHV,'RAILWAYS'RAILWAYS,'IRRIGATION'IRRIGATION,'PUBLIC_UTILITIES'PUBLIC_UTILITIES,'OTHERS' OTHERS))\r\n"
					+ "order by to_date(MON_YEAR,'MON-YYYY')";
			log.info(sql);
			return jdbcTemplate.queryForList(sql, new Object[] { fromdate, todate });
		} else {
			String sql = "SELECT * FROM\r\n" + "(\r\n"
					+ "SELECT UNIQUE CASE WHEN ctcat='HT1' THEN 'DOMESTIC' WHEN CTCAT='HT2' THEN 'COMMERCIAL' WHEN CTCAT='HT3'  THEN CASE WHEN  CTACTUAL_KV=11 THEN 'INDUSTRIAL-LOW_MED_V' \r\n"
					+ "WHEN CTACTUAL_KV=33 THEN 'INDUSTRIAL-HV'  WHEN CTACTUAL_KV>33 THEN 'INDUSTRIAL-EHV' END  WHEN CTCAT='HT4' AND CTSUBCAT='D' THEN 'RAILWAYS'\r\n"
					+ "WHEN CTCAT='HT5' AND CTSUBCAT='E' THEN 'IRRIGATION' WHEN CTCAT='HT4' AND CTSUBCAT='A' THEN 'PUBLIC_UTILITIES' \r\n"
					+ "WHEN CTCAT='HT4' AND CTSUBCAT IN ('B','C') THEN 'OTHERS'\r\n"
					+ "WHEN CTCAT='HT5' AND CTSUBCAT IN ('B') THEN 'OTHERS' END CATEGORY,MON_YEAR,ROUND(SUM(NVL(REC_KWH,0)/1000000),2) REC_KWH_MU from cons,ledger_ht_hist \r\n"
					+ "where CTUSCNO=USCNO and SUBSTR(CTUSCNO,1,3)=? and to_date(MON_YEAR,'MON-YYYY') between to_date(?,'DD-MM-YYYY') and to_date(?,'DD-MM-YYYY')\r\n"
					+ "GROUP BY \r\n" + "CASE WHEN ctcat='HT1' THEN 'DOMESTIC'\r\n"
					+ "WHEN CTCAT='HT2' THEN 'COMMERCIAL' \r\n" + "WHEN CTCAT='HT3'  THEN \r\n"
					+ "CASE WHEN  CTACTUAL_KV=11 THEN 'INDUSTRIAL-LOW_MED_V' \r\n"
					+ "WHEN CTACTUAL_KV=33 THEN 'INDUSTRIAL-HV'  \r\n"
					+ "WHEN CTACTUAL_KV>33 THEN 'INDUSTRIAL-EHV' END  \r\n"
					+ "WHEN CTCAT='HT4' AND CTSUBCAT='D' THEN 'RAILWAYS' \r\n"
					+ "WHEN CTCAT='HT5' AND CTSUBCAT='E' THEN 'IRRIGATION'\r\n"
					+ "WHEN CTCAT='HT4' AND CTSUBCAT='A' THEN 'PUBLIC_UTILITIES' \r\n"
					+ "WHEN CTCAT='HT4' AND CTSUBCAT IN ('B','C') THEN 'OTHERS' \r\n"
					+ "WHEN CTCAT='HT5' AND CTSUBCAT IN ('B') THEN 'OTHERS' END,MON_YEAR  ORDER BY \r\n"
					+ "     case when CATEGORY='DOMESTIC' then 1 \r\n" + "     when CATEGORY='COMMERCIAL' then 2 \r\n"
					+ "     when CATEGORY='INDUSTRIAL-LOW_MED_V' then 3 \r\n"
					+ "     when CATEGORY='INDUSTRIAL-HV' then 4\r\n"
					+ "     when CATEGORY='INDUSTRIAL-EHV' then 5 \r\n" + "      when CATEGORY='RAILWAYS' then 6\r\n"
					+ "       when CATEGORY='IRRIGATION' then 7 \r\n"
					+ "        when CATEGORY='PUBLIC_UTILITIES' then 8 \r\n"
					+ "        when CATEGORY='OTHERS' then 9\r\n" + "     end,to_date(MON_YEAR,'MON-YYYY') \r\n"
					+ "     )\r\n" + "PIVOT\r\n" + "(\r\n" + "sum(REC_KWH_MU)\r\n"
					+ "FOR category in ('DOMESTIC' DOMESTIC,'COMMERCIAL'COMMERCIAL,'INDUSTRIAL-LOW_MED_V'INDUSTRIAL_LOW_MED_V,'INDUSTRIAL-HV'INDUSTRIAL_HV,'INDUSTRIAL-EHV'INDUSTRIAL_EHV,'RAILWAYS'RAILWAYS,'IRRIGATION'IRRIGATION,'PUBLIC_UTILITIES'PUBLIC_UTILITIES,'OTHERS' OTHERS))\r\n"
					+ "order by to_date(MON_YEAR,'MON-YYYY')";
			log.info(sql);
			return jdbcTemplate.queryForList(sql, new Object[] { circle, fromdate, todate });
		}
	}

	public List<Map<String, Object>> getTpDetails(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		if (circle.equals("ALL")) {
			String sql = "SELECT COUNT(*) AS NOS,SUM(TP_ADJ_ENG)TP_ADJ_ENG, CTACTUAL_KV FROM TP_ENERGY,CONS WHERE CTUSCNO=TPUSCNO AND BILL_MON=? AND BILL_YEAR=? GROUP BY CTACTUAL_KV \r\n"
					+ "UNION ALL\r\n"
					+ "SELECT COUNT(*) AS NOS,SUM(TP_ADJ_ENG)TP_ADJ_ENG, CTACTUAL_KV FROM TP_ENERGY_HIST,CONS WHERE CTUSCNO=TPUSCNO  AND BILL_MON=? AND BILL_YEAR=? GROUP BY CTACTUAL_KV \r\n"
					+ "ORDER BY CTACTUAL_KV";
			return jdbcTemplate.queryForList(sql, new Object[] { request.getParameter("month"),
					request.getParameter("year"), request.getParameter("month"), request.getParameter("year") });

		} else {
			String sql = "SELECT COUNT(*) AS NOS,SUM(TP_ADJ_ENG)TP_ADJ_ENG, CTACTUAL_KV FROM TP_ENERGY,CONS WHERE CTUSCNO=TPUSCNO AND SUBSTR(TPUSCNO,0,3)=? AND BILL_MON=? AND BILL_YEAR=? GROUP BY CTACTUAL_KV \r\n"
					+ "UNION ALL\r\n"
					+ "SELECT COUNT(*) AS NOS,SUM(TP_ADJ_ENG)TP_ADJ_ENG, CTACTUAL_KV FROM TP_ENERGY_HIST,CONS WHERE CTUSCNO=TPUSCNO AND SUBSTR(TPUSCNO,0,3)=? AND BILL_MON=? AND BILL_YEAR=? GROUP BY CTACTUAL_KV \r\n"
					+ "ORDER BY CTACTUAL_KV";
			return jdbcTemplate.queryForList(sql,
					new Object[] { circle, request.getParameter("month"), request.getParameter("year"), circle,
							request.getParameter("month"), request.getParameter("year") });
		}
	}

	public List<Map<String, Object>> getFYTpDetails(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		String fin_year = request.getParameter("year");
		String fromdate = "01-APR-" + fin_year.split("-")[0];
		String todate = "31-MAR-" + fin_year.split("-")[1];
		if (circle.equals("ALL")) {
			String sql = "SELECT UNIQUE CTACTUAL_KV,SUM(NVL(BTTP_KVAH,0)) THIRD_PARTY,SUM(NVL(BTOA_KVAH,0)) OPEN_ACCESS FROM CONS,BILL_HIST WHERE CTUSCNO=BTSCNO AND  BTBLDT  BETWEEN ? AND ? GROUP BY CTACTUAL_KV";
			return jdbcTemplate.queryForList(sql, new Object[] { fromdate, todate });
		} else {
			String sql = "SELECT UNIQUE CTACTUAL_KV,SUM(NVL(BTTP_KVAH,0)) THIRD_PARTY,SUM(NVL(BTOA_KVAH,0)) OPEN_ACCESS FROM CONS,BILL_HIST WHERE CTUSCNO=BTSCNO AND  BTBLDT  BETWEEN ? AND ? GROUP BY CTACTUAL_KV";
			return jdbcTemplate.queryForList(sql, new Object[] { circle, fromdate, todate });
		}
	}

	public List<Map<String, Object>> getdemandSplitDetails(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		String monthYear = request.getParameter("month") + "-" + request.getParameter("year");
		if (circle.equals("ALL")) {
			String sql = "SELECT TO_CHAR(BTBLDT,'MON-YYYY') BTBLDT,BTSCNO,BTBLCAT,ROUND(NVL(BTBLKVA_HT,0)) BTBLKVA_HT,ROUND(NVL(BTBKVAH,0)) BTBKVAH ,ROUND(NVL(BTDEMCHG_NOR,0)) BTDEMCHG_NOR,ROUND(NVL(BTENGCHG_NOR,0)) BTENGCHG_NOR,ROUND(NVL(BTDEMCHG_PEN,0)) BTDEMCHG_PEN,ROUND(NVL(BTENGCHG_PEN,0)) BTENGCHG_PEN,ROUND(NVL(BTVOLTSURCHG,0)) BTVOLTSURCHG,ROUND(NVL(BTOTHERCHG,0)) BTOTHERCHG, ROUND(NVL(BTED,0)) BTED,ROUND(NVL(BTED_INT,0)) BTED_INT,ROUND(NVL(BTCOURT_LPC,0)) BTCOURT_LPC,ROUND(NVL(BTCUSTCHG,0)) BTCUSTCHG, ROUND(NVL(BTTODCHG_HT,0)) BTTODCHG_HT , ROUND(NVL(BTADLCHG,0)) BTADLCHG,ROUND(NVL(BT_TU_CHG,0)+nvl(BT_LTU_CHG,0)) BT_TU_CHG\r\n"
					+ ",ROUND(NVL(BTFSACHG,0))  BTFSACHG,ROUND(NVL(BTDTRHIRE_CHG,0)) BTDTRHIRE_CHG,ROUND(NVL(BTCURDEM,0)) BTCURDEM,ROUND(NVL(BTACTUAL_KV,0)) BTACTUAL_KV FROM BILL WHERE TO_CHAR(BTBLDT,'MON-YYYY')=? AND substr(BTSCNO,1,3) in ('VJA','GNT','ONG','CRD') \r\n"
					+ "UNION ALL\r\n"
					+ "SELECT TO_CHAR(BTBLDT,'MON-YYYY') BTBLDT,BTSCNO,BTBLCAT,ROUND(NVL(BTBLKVA_HT,0)) BTBLKVA_HT,ROUND(NVL(BTBKVAH,0)) BTBKVAH ,ROUND(NVL(BTDEMCHG_NOR,0)) BTDEMCHG_NOR,ROUND(NVL(BTENGCHG_NOR,0)) BTENGCHG_NOR,ROUND(NVL(BTDEMCHG_PEN,0)) BTDEMCHG_PEN,ROUND(NVL(BTENGCHG_PEN,0)) BTENGCHG_PEN,ROUND(NVL(BTVOLTSURCHG,0)) BTVOLTSURCHG,ROUND(NVL(BTOTHERCHG,0)) BTOTHERCHG, ROUND(NVL(BTED,0)) BTED,ROUND(NVL(BTED_INT,0)) BTED_INT,ROUND(NVL(BTCOURT_LPC,0)) BTCOURT_LPC,ROUND(NVL(BTCUSTCHG,0)) BTCUSTCHG, ROUND(NVL(BTTODCHG_HT,0)) BTTODCHG_HT , ROUND(NVL(BTADLCHG,0)) BTADLCHG,ROUND(NVL(BT_TU_CHG,0)+nvl(BT_LTU_CHG,0)) BT_TU_CHG \r\n"
					+ ",ROUND(NVL(BTFSACHG,0))  BTFSACHG,ROUND(NVL(BTDTRHIRE_CHG,0)) BTDTRHIRE_CHG,ROUND(NVL(BTCURDEM,0)) BTCURDEM,ROUND(NVL(BTACTUAL_KV,0)) BTACTUAL_KV FROM BILL_HIST WHERE TO_CHAR(BTBLDT,'MON-YYYY')=? AND substr(BTSCNO,1,3) in ('VJA','GNT','ONG','CRD') \r\n "
					+ "ORDER BY BTSCNO";
			log.info(sql);
			return jdbcTemplate.queryForList(sql, new Object[] { monthYear, monthYear });
		} else {
			String sql = "SELECT TO_CHAR(BTBLDT,'MON-YYYY') BTBLDT,BTSCNO,BTBLCAT,ROUND(NVL(BTBLKVA_HT,0)) BTBLKVA_HT,ROUND(NVL(BTBKVAH,0)) BTBKVAH ,ROUND(NVL(BTDEMCHG_NOR,0)) BTDEMCHG_NOR,ROUND(NVL(BTENGCHG_NOR,0)) BTENGCHG_NOR,ROUND(NVL(BTDEMCHG_PEN,0)) BTDEMCHG_PEN,ROUND(NVL(BTENGCHG_PEN,0)) BTENGCHG_PEN,ROUND(NVL(BTVOLTSURCHG,0)) BTVOLTSURCHG,ROUND(NVL(BTOTHERCHG,0)) BTOTHERCHG, ROUND(NVL(BTED,0)) BTED,ROUND(NVL(BTED_INT,0)) BTED_INT,ROUND(NVL(BTCOURT_LPC,0)) BTCOURT_LPC,ROUND(NVL(BTCUSTCHG,0)) BTCUSTCHG, ROUND(NVL(BTTODCHG_HT,0)) BTTODCHG_HT , ROUND(NVL(BTADLCHG,0)) BTADLCHG,ROUND(NVL(BT_TU_CHG,0)+nvl(BT_LTU_CHG,0)) BT_TU_CHG \r\n"
					+ ",ROUND(NVL(BTFSACHG,0))  BTFSACHG,ROUND(NVL(BTDTRHIRE_CHG,0)) BTDTRHIRE_CHG,ROUND(NVL(BTCURDEM,0)) BTCURDEM,ROUND(NVL(BTACTUAL_KV,0)) BTACTUAL_KV FROM BILL WHERE TO_CHAR(BTBLDT,'MON-YYYY')=? AND SUBSTR(BTSCNO,0,3)=? \r\n"
					+ "UNION ALL\r\n"
					+ "SELECT TO_CHAR(BTBLDT,'MON-YYYY') BTBLDT,BTSCNO,BTBLCAT,ROUND(NVL(BTBLKVA_HT,0)) BTBLKVA_HT,ROUND(NVL(BTBKVAH,0)) BTBKVAH ,ROUND(NVL(BTDEMCHG_NOR,0)) BTDEMCHG_NOR,ROUND(NVL(BTENGCHG_NOR,0)) BTENGCHG_NOR,ROUND(NVL(BTDEMCHG_PEN,0)) BTDEMCHG_PEN,ROUND(NVL(BTENGCHG_PEN,0)) BTENGCHG_PEN,ROUND(NVL(BTVOLTSURCHG,0)) BTVOLTSURCHG,ROUND(NVL(BTOTHERCHG,0)) BTOTHERCHG, ROUND(NVL(BTED,0)) BTED,ROUND(NVL(BTED_INT,0)) BTED_INT,ROUND(NVL(BTCOURT_LPC,0)) BTCOURT_LPC,ROUND(NVL(BTCUSTCHG,0)) BTCUSTCHG, ROUND(NVL(BTTODCHG_HT,0)) BTTODCHG_HT , ROUND(NVL(BTADLCHG,0)) BTADLCHG,ROUND(NVL(BT_TU_CHG,0)+nvl(BT_LTU_CHG,0)) BT_TU_CHG \r\n"
					+ ",ROUND(NVL(BTFSACHG,0))  BTFSACHG,ROUND(NVL(BTDTRHIRE_CHG,0)) BTDTRHIRE_CHG,ROUND(NVL(BTCURDEM,0)) BTCURDEM,ROUND(NVL(BTACTUAL_KV,0)) BTACTUAL_KV FROM BILL_HIST WHERE TO_CHAR(BTBLDT,'MON-YYYY')=? AND SUBSTR(BTSCNO,0,3)=? \r\n"
					+ "ORDER BY BTSCNO";
			log.info(sql);
			return jdbcTemplate.queryForList(sql, new Object[] { monthYear, circle, monthYear, circle });
		}
	}

	public List<Map<String, Object>> getMeterStatus(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		String monthYear = request.getParameter("month") + "-" + request.getParameter("year");
		if (circle.equals("ALL")) {
			String sql = "SELECT CASE WHEN  CAT='1' THEN 'DOMESTIC' WHEN CAT='2' THEN 'COMMERCIAL' WHEN CAT='3' THEN 'INDUSTRIAL'  WHEN CAT='5' THEN 'AGRICULTURE' WHEN CAT='4' THEN CASE WHEN SCAT='D' THEN 'TRACTION' WHEN SCAT='A' THEN 'PWS' ELSE 'OTHERS' END ELSE CAT END  CATEGORY,\r\n"
					+ "case when NVL(ctloca_type,'P') like '%C%' OR NVL(ctloca_type,'P') like '%M%' then 'Urban' else 'Rural' end area,COUNT(*) NOS,SUM(CASE WHEN STATUS=0 THEN 0 ELSE 1 END) BILLED_SCS,\r\n"
					+ "SUM(NVL(RKWH,0)) REC_KWH,SUM(NVL(RKVAH,0)) REC_KVAH,SUM(NVL(BUNITS,0)) BILLED_KVAH,SUM(NVL(DEM,0)+NVL(DR_AMT,0)) DEMAND,SUM(NVL(COLL,0)+NVL(CR_AMT,0)) COLLECTION, \r\n"
					+ "SUM(NVL(CB,0)) TOTAL_CB\r\n" + "FROM CONS,\r\n"
					+ "(SELECT USCNO,STATUS,CAT,SCAT,NVL(REC_KWH,0) RKWH,(NVL(MN_KVAH,0)) BUNITS,nvl(REC_KVAH,0) RKVAH,(NVL(OB_OTH,0) +NVL(OB_CCLPC,0)+NVL(TOT_OB,0)) OB,\r\n"
					+ "(NVL(CMD,0)+NVL(CCLPC,0)) DEM,(NVL(TOT_PAY,0)) COLL,(NVL(CBTOT,0)+NVL(CB_CCLPC,0)+NVL(CB_OTH,0)) CB\r\n"
					+ "FROM  V_LEDGER WHERE MON_YEAR=? )L1,\r\n"
					+ "(select uscno,TRUNC(RJDT,'MM') DT,SUM(CASE WHEN RJTYPE='DR' THEN (nvl(ENGCHG,0)+nvl(TUPC,0)+nvl(THEFT,0)+nvl(OTHCHG,0)+nvl(CUSTCHG,0)+nvl(DEMCHG,0)+nvl(ED,0)+nvl(LPC,0)+nvl(EDI,0)+nvl(FSA,0)+NVL(CC_LPC,0)+NVL(CC_OTH,0)) END) DR_AMT,SUM(CASE WHEN RJTYPE='CR'THEN (nvl(ENGCHG,0)+nvl(TUPC,0)+nvl(THEFT,0)+nvl(OTHCHG,0)+nvl(CUSTCHG,0)+nvl(DEMCHG,0)+nvl(ED,0)+nvl(LPC,0)+nvl(EDI,0)+nvl(FSA,0)+NVL(CC_LPC,0)+NVL(CC_OTH,0)) END) CR_AMT  from JOURNAL_HIST WHERE rjdt=? and SUBSTR(USCNO,1,3) IN('GNT','VJA','ONG','CRD') AND TRIM(STATUS) NOT IN ('X') GROUP BY USCNO,TRUNC(RJDT,'MM') ) R1\r\n"
					+ "WHERE L1.USCNO=R1.USCNO(+) AND CTUSCNO=L1.USCNO  GROUP BY CASE WHEN  CAT='1' THEN 'DOMESTIC' WHEN CAT='2' THEN 'COMMERCIAL' WHEN CAT='3' THEN 'INDUSTRIAL'  WHEN CAT='5' THEN 'AGRICULTURE' WHEN CAT='4' THEN CASE WHEN SCAT='D' THEN 'TRACTION' WHEN SCAT='A' THEN 'PWS' ELSE 'OTHERS' END ELSE CAT END ,\r\n"
					+ "case when NVL(ctloca_type,'P') like '%C%' OR NVL(ctloca_type,'P') like '%M%' then 'Urban' else 'Rural' end ORDER BY case when CATEGORY='DOMESTIC' then 1 \r\n"
					+ "     when CATEGORY='COMMERCIAL' then 2 \r\n" + "     when CATEGORY='INDUSTRIAL' then 3 \r\n"
					+ "     when CATEGORY='AGRICULTURE' then 4\r\n" + "     when CATEGORY='TRACTION' then 5 \r\n"
					+ "      when CATEGORY='RAILWAYS' then 6\r\n" + "       when CATEGORY='PWS' then 7 \r\n"
					+ "        when CATEGORY='OTHERS' then 8 \r\n"
					+ "     end,case when area='Urban' then 1 else 2 end";
			log.info(sql);
			return jdbcTemplate.queryForList(sql, new Object[] { monthYear, "01-" + monthYear });
		} else {
			String sql = "SELECT CASE WHEN  CAT='1' THEN 'DOMESTIC' WHEN CAT='2' THEN 'COMMERCIAL' WHEN CAT='3' THEN 'INDUSTRIAL'  WHEN CAT='5' THEN 'AGRICULTURE' WHEN CAT='4' THEN CASE WHEN SCAT='D' THEN 'TRACTION' WHEN SCAT='A' THEN 'PWS' ELSE 'OTHERS' END ELSE CAT END  CATEGORY,\r\n"
					+ "case when NVL(ctloca_type,'P') like '%C%' OR NVL(ctloca_type,'P') like '%M%' then 'Urban' else 'Rural' end area,COUNT(*) NOS,SUM(CASE WHEN STATUS=0 THEN 0 ELSE 1 END) BILLED_SCS,\r\n"
					+ "SUM(NVL(RKWH,0)) REC_KWH,SUM(NVL(RKVAH,0)) REC_KVAH,SUM(NVL(BUNITS,0)) BILLED_KVAH,SUM(NVL(DEM,0)+NVL(DR_AMT,0)) DEMAND,SUM(NVL(COLL,0)+NVL(CR_AMT,0)) COLLECTION, \r\n"
					+ "SUM(NVL(CB,0)) TOTAL_CB\r\n" + "FROM CONS,\r\n"
					+ "(SELECT USCNO,STATUS,CAT,SCAT,NVL(REC_KWH,0) RKWH,(NVL(MN_KVAH,0)) BUNITS,nvl(REC_KVAH,0) RKVAH,(NVL(OB_OTH,0) +NVL(OB_CCLPC,0)+NVL(TOT_OB,0)) OB,\r\n"
					+ "(NVL(CMD,0)+NVL(CCLPC,0)) DEM,(NVL(TOT_PAY,0)) COLL,(NVL(CBTOT,0)+NVL(CB_CCLPC,0)+NVL(CB_OTH,0)) CB\r\n"
					+ "FROM  V_LEDGER WHERE MON_YEAR=? )L1,\r\n"
					+ "(select uscno,TRUNC(RJDT,'MM') DT,SUM(CASE WHEN RJTYPE='DR' THEN (nvl(ENGCHG,0)+nvl(TUPC,0)+nvl(THEFT,0)+nvl(OTHCHG,0)+nvl(CUSTCHG,0)+nvl(DEMCHG,0)+nvl(ED,0)+nvl(LPC,0)+nvl(EDI,0)+nvl(FSA,0)+NVL(CC_LPC,0)+NVL(CC_OTH,0)) END) DR_AMT,SUM(CASE WHEN RJTYPE='CR'THEN (nvl(ENGCHG,0)+nvl(TUPC,0)+nvl(THEFT,0)+nvl(OTHCHG,0)+nvl(CUSTCHG,0)+nvl(DEMCHG,0)+nvl(ED,0)+nvl(LPC,0)+nvl(EDI,0)+nvl(FSA,0)+NVL(CC_LPC,0)+NVL(CC_OTH,0)) END) CR_AMT  from JOURNAL_HIST WHERE rjdt=? and SUBSTR(USCNO,1,3) IN('GNT','VJA','ONG','CRD') AND TRIM(STATUS) NOT IN ('X') GROUP BY USCNO,TRUNC(RJDT,'MM') ) R1\r\n"
					+ "WHERE L1.USCNO=R1.USCNO(+) AND CTUSCNO=L1.USCNO AND SUBSTR(CTUSCNO,1,3)=? GROUP BY CASE WHEN  CAT='1' THEN 'DOMESTIC' WHEN CAT='2' THEN 'COMMERCIAL' WHEN CAT='3' THEN 'INDUSTRIAL'  WHEN CAT='5' THEN 'AGRICULTURE' WHEN CAT='4' THEN CASE WHEN SCAT='D' THEN 'TRACTION' WHEN SCAT='A' THEN 'PWS' ELSE 'OTHERS' END ELSE CAT END ,\r\n"
					+ "case when NVL(ctloca_type,'P') like '%C%' OR NVL(ctloca_type,'P') like '%M%' then 'Urban' else 'Rural' end ORDER BY case when CATEGORY='DOMESTIC' then 1 \r\n"
					+ "     when CATEGORY='COMMERCIAL' then 2 \r\n" + "     when CATEGORY='INDUSTRIAL' then 3 \r\n"
					+ "     when CATEGORY='AGRICULTURE' then 4\r\n" + "     when CATEGORY='TRACTION' then 5 \r\n"
					+ "      when CATEGORY='RAILWAYS' then 6\r\n" + "       when CATEGORY='PWS' then 7 \r\n"
					+ "        when CATEGORY='OTHERS' then 8 \r\n"
					+ "     end,case when area='Urban' then 1 else 2 end";
			log.info(sql);
			return jdbcTemplate.queryForList(sql, new Object[] { monthYear, "01-" + monthYear, circle });
		}
	}

	public List<Map<String, Object>> getSolarMeterStatus(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		String monthYear = request.getParameter("month") + "-" + request.getParameter("year");
		if (circle.equals("ALL")) {
			String sql = "SELECT CASE WHEN  CAT='1' THEN 'DOMESTIC' WHEN CAT='2' THEN 'COMMERCIAL' WHEN CAT='3' THEN 'INDUSTRIAL'  WHEN CAT='5' THEN 'AGRICULTURE' WHEN CAT='4' THEN CASE WHEN SCAT='D' THEN 'TRACTION' WHEN SCAT='A' THEN 'PWS' ELSE 'OTHERS' END ELSE CAT END  CATEGORY,\r\n"
					+ "case when NVL(ctloca_type,'P') like '%C%' OR NVL(ctloca_type,'P') like '%M%' then 'Urban' else 'Rural' end area,COUNT(*) NOS,SUM(CASE WHEN STATUS=0 THEN 0 ELSE 1 END) BILLED_SCS,\r\n"
					+ "SUM(NVL(RKWH,0)) REC_KWH,SUM(NVL(RKVAH,0)) REC_KVAH,SUM(NVL(BUNITS,0)) BILLED_KVAH,SUM(NVL(DEM,0)+NVL(DR_AMT,0)) DEMAND,SUM(NVL(COLL,0)+NVL(CR_AMT,0)) COLLECTION, \r\n"
					+ "SUM(NVL(CB,0)) TOTAL_CB\r\n" + "FROM CONS,\r\n"
					+ "(SELECT USCNO,STATUS,CAT,SCAT,NVL(REC_KWH,0) RKWH,(NVL(MN_KVAH,0)) BUNITS,nvl(REC_KVAH,0) RKVAH,(NVL(OB_OTH,0) +NVL(OB_CCLPC,0)+NVL(TOT_OB,0)) OB,\r\n"
					+ "(NVL(CMD,0)+NVL(CCLPC,0)) DEM,(NVL(TOT_PAY,0)) COLL,(NVL(CBTOT,0)+NVL(CB_CCLPC,0)+NVL(CB_OTH,0)) CB\r\n"
					+ "FROM  V_LEDGER WHERE MON_YEAR=? )L1,\r\n"
					+ "(select uscno,TRUNC(RJDT,'MM') DT,SUM(CASE WHEN RJTYPE='DR' THEN (nvl(ENGCHG,0)+nvl(TUPC,0)+nvl(THEFT,0)+nvl(OTHCHG,0)+nvl(CUSTCHG,0)+nvl(DEMCHG,0)+nvl(ED,0)+nvl(LPC,0)+nvl(EDI,0)+nvl(FSA,0)+NVL(CC_LPC,0)+NVL(CC_OTH,0)) END) DR_AMT,SUM(CASE WHEN RJTYPE='CR'THEN (nvl(ENGCHG,0)+nvl(TUPC,0)+nvl(THEFT,0)+nvl(OTHCHG,0)+nvl(CUSTCHG,0)+nvl(DEMCHG,0)+nvl(ED,0)+nvl(LPC,0)+nvl(EDI,0)+nvl(FSA,0)+NVL(CC_LPC,0)+NVL(CC_OTH,0)) END) CR_AMT  from JOURNAL_HIST WHERE rjdt=? and SUBSTR(USCNO,1,3) IN('GNT','VJA','ONG','CRD') AND TRIM(STATUS) NOT IN ('X') GROUP BY USCNO,TRUNC(RJDT,'MM') ) R1\r\n"
					+ "WHERE L1.USCNO=R1.USCNO(+) AND CTUSCNO=L1.USCNO AND  CTSOLAR_FLAG='Y' GROUP BY CASE WHEN  CAT='1' THEN 'DOMESTIC' WHEN CAT='2' THEN 'COMMERCIAL' WHEN CAT='3' THEN 'INDUSTRIAL'  WHEN CAT='5' THEN 'AGRICULTURE' WHEN CAT='4' THEN CASE WHEN SCAT='D' THEN 'TRACTION' WHEN SCAT='A' THEN 'PWS' ELSE 'OTHERS' END ELSE CAT END ,\r\n"
					+ "case when NVL(ctloca_type,'P') like '%C%' OR NVL(ctloca_type,'P') like '%M%' then 'Urban' else 'Rural' end ORDER BY case when CATEGORY='DOMESTIC' then 1 \r\n"
					+ "     when CATEGORY='COMMERCIAL' then 2 \r\n" + "     when CATEGORY='INDUSTRIAL' then 3 \r\n"
					+ "     when CATEGORY='AGRICULTURE' then 4\r\n" + "     when CATEGORY='TRACTION' then 5 \r\n"
					+ "      when CATEGORY='RAILWAYS' then 6\r\n" + "       when CATEGORY='PWS' then 7 \r\n"
					+ "        when CATEGORY='OTHERS' then 8 \r\n"
					+ "     end,case when area='Urban' then 1 else 2 end";
			log.info(sql);
			return jdbcTemplate.queryForList(sql, new Object[] { monthYear, "01-" + monthYear });
		} else {
			String sql = "SELECT CASE WHEN  CAT='1' THEN 'DOMESTIC' WHEN CAT='2' THEN 'COMMERCIAL' WHEN CAT='3' THEN 'INDUSTRIAL'  WHEN CAT='5' THEN 'AGRICULTURE' WHEN CAT='4' THEN CASE WHEN SCAT='D' THEN 'TRACTION' WHEN SCAT='A' THEN 'PWS' ELSE 'OTHERS' END ELSE CAT END  CATEGORY,\r\n"
					+ "case when NVL(ctloca_type,'P') like '%C%' OR NVL(ctloca_type,'P') like '%M%' then 'Urban' else 'Rural' end area,COUNT(*) NOS,SUM(CASE WHEN STATUS=0 THEN 0 ELSE 1 END) BILLED_SCS,\r\n"
					+ "SUM(NVL(RKWH,0)) REC_KWH,SUM(NVL(RKVAH,0)) REC_KVAH,SUM(NVL(BUNITS,0)) BILLED_KVAH,SUM(NVL(DEM,0)+NVL(DR_AMT,0)) DEMAND,SUM(NVL(COLL,0)+NVL(CR_AMT,0)) COLLECTION, \r\n"
					+ "SUM(NVL(CB,0)) TOTAL_CB\r\n" + "FROM CONS,\r\n"
					+ "(SELECT USCNO,STATUS,CAT,SCAT,NVL(REC_KWH,0) RKWH,(NVL(MN_KVAH,0)) BUNITS,nvl(REC_KVAH,0) RKVAH,(NVL(OB_OTH,0) +NVL(OB_CCLPC,0)+NVL(TOT_OB,0)) OB,\r\n"
					+ "(NVL(CMD,0)+NVL(CCLPC,0)) DEM,(NVL(TOT_PAY,0)) COLL,(NVL(CBTOT,0)+NVL(CB_CCLPC,0)+NVL(CB_OTH,0)) CB\r\n"
					+ "FROM  V_LEDGER WHERE MON_YEAR=? )L1,\r\n"
					+ "(select uscno,TRUNC(RJDT,'MM') DT,SUM(CASE WHEN RJTYPE='DR' THEN (nvl(ENGCHG,0)+nvl(TUPC,0)+nvl(THEFT,0)+nvl(OTHCHG,0)+nvl(CUSTCHG,0)+nvl(DEMCHG,0)+nvl(ED,0)+nvl(LPC,0)+nvl(EDI,0)+nvl(FSA,0)+NVL(CC_LPC,0)+NVL(CC_OTH,0)) END) DR_AMT,SUM(CASE WHEN RJTYPE='CR'THEN (nvl(ENGCHG,0)+nvl(TUPC,0)+nvl(THEFT,0)+nvl(OTHCHG,0)+nvl(CUSTCHG,0)+nvl(DEMCHG,0)+nvl(ED,0)+nvl(LPC,0)+nvl(EDI,0)+nvl(FSA,0)+NVL(CC_LPC,0)+NVL(CC_OTH,0)) END) CR_AMT  from JOURNAL_HIST WHERE rjdt=? and SUBSTR(USCNO,1,3) IN('GNT','VJA','ONG','CRD') AND TRIM(STATUS) NOT IN ('X') GROUP BY USCNO,TRUNC(RJDT,'MM') ) R1\r\n"
					+ "WHERE L1.USCNO=R1.USCNO(+) AND CTUSCNO=L1.USCNO AND SUBSTR(CTUSCNO,1,3)=? AND  CTSOLAR_FLAG='Y'GROUP BY CASE WHEN  CAT='1' THEN 'DOMESTIC' WHEN CAT='2' THEN 'COMMERCIAL' WHEN CAT='3' THEN 'INDUSTRIAL'  WHEN CAT='5' THEN 'AGRICULTURE' WHEN CAT='4' THEN CASE WHEN SCAT='D' THEN 'TRACTION' WHEN SCAT='A' THEN 'PWS' ELSE 'OTHERS' END ELSE CAT END ,\r\n"
					+ "case when NVL(ctloca_type,'P') like '%C%' OR NVL(ctloca_type,'P') like '%M%' then 'Urban' else 'Rural' end ORDER BY case when CATEGORY='DOMESTIC' then 1 \r\n"
					+ "     when CATEGORY='COMMERCIAL' then 2 \r\n" + "     when CATEGORY='INDUSTRIAL' then 3 \r\n"
					+ "     when CATEGORY='AGRICULTURE' then 4\r\n" + "     when CATEGORY='TRACTION' then 5 \r\n"
					+ "      when CATEGORY='RAILWAYS' then 6\r\n" + "       when CATEGORY='PWS' then 7 \r\n"
					+ "        when CATEGORY='OTHERS' then 8 \r\n"
					+ "     end,case when area='Urban' then 1 else 2 end";
			log.info(sql);
			return jdbcTemplate.queryForList(sql, new Object[] { monthYear, "01-" + monthYear, circle });
		}
	}

	public List<Map<String, Object>> getSeaDemandSplitDetails(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		String monthYear = request.getParameter("fmonth") + "-" + request.getParameter("fyear");
		String seaflag = request.getParameter("seasonal").equals("ALL") ? "'Y','N'"
				: (request.getParameter("seasonal").equals("OFFSEA") ? "'Y'" : "'N'");
		if (circle.equals("ALL")) {
			String sql = "select  CIRNAME,DIVNAME, SUBNAME, SECNAME, CTNAME,CTCMD_HT,to_number(CTCMD_HT) * 0.3 TY_PER_CMD , A.* from cons,("
					+ "select * from ( SELECT TO_CHAR(BTBLDT,'MON-YYYY') BTBLDT,BTSCNO,BTBLCAT||BTBLSCAT BTBLCAT,ROUND(NVL(BTBLKVA_HT,0)) BTBLKVA_HT,ROUND(NVL(BTBKVAH,0)) BTBKVAH ,ROUND(NVL(BTDEMCHG_NOR,0)) BTDEMCHG_NOR,ROUND(NVL(BTENGCHG_NOR,0)) BTENGCHG_NOR,ROUND(NVL(BTDEMCHG_PEN,0)) BTDEMCHG_PEN,ROUND(NVL(BTENGCHG_PEN,0)) BTENGCHG_PEN,ROUND(NVL(BTVOLTSURCHG,0)) BTVOLTSURCHG,ROUND(NVL(BTOTHERCHG,0)) BTOTHERCHG, ROUND(NVL(BTED,0)) BTED,ROUND(NVL(BTED_INT,0)) BTED_INT,ROUND(NVL(BTCOURT_LPC,0)) BTCOURT_LPC,ROUND(NVL(BTCUSTCHG,0)) BTCUSTCHG, ROUND(NVL(BTTODCHG_HT,0)) BTTODCHG_HT , ROUND(NVL(BTADLCHG,0)) BTADLCHG\r\n"
					+ ",ROUND(NVL(BTFSACHG,0))  BTFSACHG,ROUND(NVL(BTDTRHIRE_CHG,0)) BTDTRHIRE_CHG,ROUND(NVL(BTCURDEM,0)) BTCURDEM,ROUND(NVL(BTACTUAL_KV,0)) BTACTUAL_KV FROM BILL WHERE TO_CHAR(BTBLDT,'MON-YYYY')=? AND substr(BTSCNO,1,3) in ('VJA','GNT','ONG','CRD') \r\n"
					+ "UNION ALL\r\n"
					+ "SELECT TO_CHAR(BTBLDT,'MON-YYYY') BTBLDT,BTSCNO,BTBLCAT,ROUND(NVL(BTBLKVA_HT,0)) BTBLKVA_HT,ROUND(NVL(BTBKVAH,0)) BTBKVAH ,ROUND(NVL(BTDEMCHG_NOR,0)) BTDEMCHG_NOR,ROUND(NVL(BTENGCHG_NOR,0)) BTENGCHG_NOR,ROUND(NVL(BTDEMCHG_PEN,0)) BTDEMCHG_PEN,ROUND(NVL(BTENGCHG_PEN,0)) BTENGCHG_PEN,ROUND(NVL(BTVOLTSURCHG,0)) BTVOLTSURCHG,ROUND(NVL(BTOTHERCHG,0)) BTOTHERCHG, ROUND(NVL(BTED,0)) BTED,ROUND(NVL(BTED_INT,0)) BTED_INT,ROUND(NVL(BTCOURT_LPC,0)) BTCOURT_LPC,ROUND(NVL(BTCUSTCHG,0)) BTCUSTCHG, ROUND(NVL(BTTODCHG_HT,0)) BTTODCHG_HT , ROUND(NVL(BTADLCHG,0)) BTADLCHG \r\n"
					+ ",ROUND(NVL(BTFSACHG,0))  BTFSACHG,ROUND(NVL(BTDTRHIRE_CHG,0)) BTDTRHIRE_CHG,ROUND(NVL(BTCURDEM,0)) BTCURDEM,ROUND(NVL(BTACTUAL_KV,0)) BTACTUAL_KV FROM BILL_HIST WHERE TO_CHAR(BTBLDT,'MON-YYYY')=? AND substr(BTSCNO,1,3) in ('VJA','GNT','ONG','CRD') \r\n"
					+ " ORDER BY BTSCNO ) A, ( select SUSCNO ,\r\n"
					+ " case when TRIM(SUBSTR(JAN||FEB||MAR||APR||MAY||JUN||JUL||AUG||SEP||OCT||NOV||DEC,TO_NUMBER(TO_CHAR(ADD_MONTHS(TO_DATE('01-'||?),-1),'MM')),1))='Y' then 'Off Season' else 'Season'   End SEASON from SEASONAL_CONS\r\n"
					+ " where TRIM(SUBSTR(JAN||FEB||MAR||APR||MAY||JUN||JUL||AUG||SEP||OCT||NOV||DEC,TO_NUMBER(TO_CHAR(ADD_MONTHS(TO_DATE('01-'||?),-1),'MM')),1))is not null \r\n"
					+ " AND TRIM(SUBSTR(JAN||FEB||MAR||APR||MAY||JUN||JUL||AUG||SEP||OCT||NOV||DEC,TO_NUMBER(TO_CHAR(ADD_MONTHS(TO_DATE('01-'||?),-1),'MM')),1)) in ("
					+ seaflag + ") ) B\r\n"
					+ " where BTSCNO= SUSCNO order by BTSCNO) A, master.spdclmaster where CTUSCNO = BTSCNO and SUBSTR(CTSECCD,-5)=SECCD(+) ";
			log.info(sql);
			return jdbcTemplate.queryForList(sql,
					new Object[] { monthYear, monthYear, monthYear, monthYear, monthYear });
		} else {
			String sql = "select  CIRNAME,DIVNAME, SUBNAME, SECNAME, CTNAME,CTCMD_HT,to_number(CTCMD_HT) * 0.3 TY_PER_CMD , A.* from cons,("
					+ "select * from ( SELECT TO_CHAR(BTBLDT,'MON-YYYY') BTBLDT,BTSCNO,BTBLCAT,ROUND(NVL(BTBLKVA_HT,0)) BTBLKVA_HT,ROUND(NVL(BTBKVAH,0)) BTBKVAH ,ROUND(NVL(BTDEMCHG_NOR,0)) BTDEMCHG_NOR,ROUND(NVL(BTENGCHG_NOR,0)) BTENGCHG_NOR,ROUND(NVL(BTDEMCHG_PEN,0)) BTDEMCHG_PEN,ROUND(NVL(BTENGCHG_PEN,0)) BTENGCHG_PEN,ROUND(NVL(BTVOLTSURCHG,0)) BTVOLTSURCHG,ROUND(NVL(BTOTHERCHG,0)) BTOTHERCHG, ROUND(NVL(BTED,0)) BTED,ROUND(NVL(BTED_INT,0)) BTED_INT,ROUND(NVL(BTCOURT_LPC,0)) BTCOURT_LPC,ROUND(NVL(BTCUSTCHG,0)) BTCUSTCHG, ROUND(NVL(BTTODCHG_HT,0)) BTTODCHG_HT , ROUND(NVL(BTADLCHG,0)) BTADLCHG\r\n"
					+ ",ROUND(NVL(BTFSACHG,0))  BTFSACHG,ROUND(NVL(BTDTRHIRE_CHG,0)) BTDTRHIRE_CHG,ROUND(NVL(BTCURDEM,0)) BTCURDEM,ROUND(NVL(BTACTUAL_KV,0)) BTACTUAL_KV FROM BILL WHERE TO_CHAR(BTBLDT,'MON-YYYY')=? AND substr(BTSCNO,1,3) in ('VJA','GNT','ONG','CRD') \r\n"
					+ "UNION ALL\r\n"
					+ "SELECT TO_CHAR(BTBLDT,'MON-YYYY') BTBLDT,BTSCNO,BTBLCAT,ROUND(NVL(BTBLKVA_HT,0)) BTBLKVA_HT,ROUND(NVL(BTBKVAH,0)) BTBKVAH ,ROUND(NVL(BTDEMCHG_NOR,0)) BTDEMCHG_NOR,ROUND(NVL(BTENGCHG_NOR,0)) BTENGCHG_NOR,ROUND(NVL(BTDEMCHG_PEN,0)) BTDEMCHG_PEN,ROUND(NVL(BTENGCHG_PEN,0)) BTENGCHG_PEN,ROUND(NVL(BTVOLTSURCHG,0)) BTVOLTSURCHG,ROUND(NVL(BTOTHERCHG,0)) BTOTHERCHG, ROUND(NVL(BTED,0)) BTED,ROUND(NVL(BTED_INT,0)) BTED_INT,ROUND(NVL(BTCOURT_LPC,0)) BTCOURT_LPC,ROUND(NVL(BTCUSTCHG,0)) BTCUSTCHG, ROUND(NVL(BTTODCHG_HT,0)) BTTODCHG_HT , ROUND(NVL(BTADLCHG,0)) BTADLCHG \r\n"
					+ ",ROUND(NVL(BTFSACHG,0))  BTFSACHG,ROUND(NVL(BTDTRHIRE_CHG,0)) BTDTRHIRE_CHG,ROUND(NVL(BTCURDEM,0)) BTCURDEM,ROUND(NVL(BTACTUAL_KV,0)) BTACTUAL_KV FROM BILL_HIST WHERE TO_CHAR(BTBLDT,'MON-YYYY')=? AND substr(BTSCNO,1,3) in ('VJA','GNT','ONG','CRD') \r\n"
					+ " ORDER BY BTSCNO ) A, ( select SUSCNO ,\r\n"
					+ " case when TRIM(SUBSTR(JAN||FEB||MAR||APR||MAY||JUN||JUL||AUG||SEP||OCT||NOV||DEC,TO_NUMBER(TO_CHAR(ADD_MONTHS(TO_DATE('01-'||?),-1),'MM')),1))='Y' then 'Off Season' else 'Season'   End SEASON from SEASONAL_CONS\r\n"
					+ " where TRIM(SUBSTR(JAN||FEB||MAR||APR||MAY||JUN||JUL||AUG||SEP||OCT||NOV||DEC,TO_NUMBER(TO_CHAR(ADD_MONTHS(TO_DATE('01-'||?),-1),'MM')),1))is not null \r\n"
					+ " AND TRIM(SUBSTR(JAN||FEB||MAR||APR||MAY||JUN||JUL||AUG||SEP||OCT||NOV||DEC,TO_NUMBER(TO_CHAR(ADD_MONTHS(TO_DATE('01-'||?),-1),'MM')),1)) in ("
					+ seaflag + ") ) B\r\n"
					+ " where BTSCNO= SUSCNO AND SUBSTR(BTSCNO,0,3)=? order by BTSCNO) A, master.spdclmaster where CTUSCNO = BTSCNO and SUBSTR(CTSECCD,-5)=SECCD(+)";
			log.info(sql);
			return jdbcTemplate.queryForList(sql,
					new Object[] { monthYear, monthYear, monthYear, monthYear, monthYear, circle });
		}
	}

	public List<Map<String, Object>> getTrueUpKvahHistory(HttpServletRequest request) {
		String serviceNo = request.getParameter("scno");
		String fmonthYear = "01-" + request.getParameter("fmonth") + "-" + request.getParameter("fyear");
		String tmonthYear = "01-" + request.getParameter("tmonth") + "-" + request.getParameter("tyear");
		String sql = "select MON_YEAR,uscno,CIRCLE, DIVISION, SUBDIVISION, SECTION,NVL(MN_KVAH,0) MN_KVAH  from ledger_ht_hist\r\n"
				+ "where to_date(MON_YEAR,'MON-YYYY') between to_date(?,'DD-MM-YYYY') and to_date(?,'DD-MM-YYYY') and substr(USCNO,1,3) "
				+ "in ('GNT','VJA','ONG','CRD') and uscno =?\r\n" + "order by to_date(MON_YEAR,'MON-YYYY')";
		log.info(sql);
		return jdbcTemplate.queryForList(sql, new Object[] { fmonthYear, tmonthYear, serviceNo });

	}

	public List<Map<String, Object>> getServiceWiseDemandSplitDetails(HttpServletRequest request) {
		String serviceNo = request.getParameter("scno");
		String fmonthYear = "01-" + request.getParameter("fmonth") + "-" + request.getParameter("fyear");
		String tmonthYear = "01-" + request.getParameter("tmonth") + "-" + request.getParameter("tyear");

		String sql = "SELECT TO_CHAR(BTBLDT,'MON-YYYY') BTBLDT,BTBLDT T_BTBLDT,BTSCNO,BTBLCAT,BTBLKVA_HT,BTBKVAH,BTDEMCHG_NOR,BTENGCHG_NOR,BTDEMCHG_PEN,BTENGCHG_PEN,BTVOLTSURCHG,BTOTHERCHG,BTED,BTED_INT,BTCOURT_LPC,BTCUSTCHG,BTTODCHG_HT,BTADLCHG\r\n"
				+ ",BTFSACHG ,BTDTRHIRE_CHG,BTCURDEM,BTACTUAL_KV FROM BILL WHERE TO_CHAR(BTBLDT,'MON-YYYY') in  (select distinct(to_char(last_day(to_date(td.end_date + 1 - rownum)),'MON-YYYY'))from all_objects,(\r\n"
				+ " select to_date(?,'DD-MM-YYYY') start_date,to_date(?,'DD-MM-YYYY') end_date FROM   DUAL  ) td\r\n"
				+ " where trunc(td.end_date + 1 - rownum,'MM') >= trunc(td.start_date,'MM')) AND BTSCNO = ? \r\n"
				+ "UNION ALL\r\n"
				+ "SELECT TO_CHAR(BTBLDT,'MON-YYYY') BTBLDT,BTBLDT T_BTBLDT,BTSCNO,BTBLCAT,BTBLKVA_HT,BTBKVAH,BTDEMCHG_NOR,BTENGCHG_NOR,BTDEMCHG_PEN,BTENGCHG_PEN,BTVOLTSURCHG,BTOTHERCHG,BTED,BTED_INT,BTCOURT_LPC,BTCUSTCHG,BTTODCHG_HT,BTADLCHG\r\n"
				+ ",BTFSACHG ,BTDTRHIRE_CHG,BTCURDEM,BTACTUAL_KV FROM BILL_HIST WHERE TO_CHAR(BTBLDT,'MON-YYYY')in (select distinct(to_char(last_day(to_date(td.end_date + 1 - rownum)),'MON-YYYY'))from all_objects,(\r\n"
				+ " select to_date(?,'DD-MM-YYYY') start_date,to_date(?,'DD-MM-YYYY') end_date FROM   DUAL  ) td\r\n"
				+ " where trunc(td.end_date + 1 - rownum,'MM') >= trunc(td.start_date,'MM'))  AND BTSCNO = ? \r\n"
				+ "ORDER BY T_BTBLDT";
		log.info(sql);
		return jdbcTemplate.queryForList(sql,
				new Object[] { fmonthYear, tmonthYear, serviceNo, fmonthYear, tmonthYear, serviceNo });

	}

	public List<Map<String, Object>> getArrearsDetails(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		String monthYear = request.getParameter("month") + "-" + request.getParameter("year");

		String status = request.getParameter("status");

		if (circle.equals("ALL")) {
			String sql = "SELECT DIVISION,SUBDIVISION,SECTION,USCNO,NAM,CTADD1,CTADD2,CTADD3,CAT,SCAT,STATUS,round(CBTOT) CBTOT,DECODE(CTSTATUS,'1','LIVE','0','BILLSTOP','') STS ,DECODE(TRIM(CTGOVT_PVT),'Y','GOVT','N','PVT','') GOVT_PVT,round(CB_OTH) CB_OTH, round(CB_OTH)+round(CBTOT) TOTTAL \r\n"
					+ "FROM LEDGER_HT_HIST,CONS \r\n" + "WHERE MON_YEAR=? AND CTUSCNO=USCNO\r\n"
					+ "AND (round(CB_OTH)+round(CBTOT))>50000 \r\n"
					+ (status.equals("All") ? ""
							: " AND DECODE(CTSTATUS,'1','LIVE','0','BILLSTOP','') = '" + status + "' ")
					+ "UNION ALL\r\n"
					+ "SELECT DIVISION,SUBDIVISION,SECTION,USCNO,NAM,CTADD1,CTADD2,CTADD3,CAT,SCAT,STATUS,round(CBTOT) CBTOT,DECODE(CTSTATUS,'1','LIVE','0','BILLSTOP','') STS,DECODE(TRIM(CTGOVT_PVT),'Y','GOVT','N','PVT','') GOVT_PVT,round(CB_OTH) CB_OTH, round(CB_OTH)+round(CBTOT) TOTTAL \r\n"
					+ "FROM ACCOUNTCOPY,CONS\r\n" + "WHERE MON_YEAR=? AND CTUSCNO=USCNO\r\n"
					+ "AND (round(CB_OTH)+round(CBTOT))>50000 \r\n"
					+ (status.equals("All") ? ""
							: " AND DECODE(CTSTATUS,'1','LIVE','0','BILLSTOP','') = '" + status + "' ")
					+ "ORDER BY USCNO";
			log.info(sql);
			return jdbcTemplate.queryForList(sql, new Object[] { monthYear, monthYear });
		} else {
			String sql = "SELECT DIVISION,SUBDIVISION,SECTION,USCNO,NAM,CTADD1,CTADD2,CTADD3,CAT,SCAT,STATUS,round(CBTOT) CBTOT,DECODE(CTSTATUS,'1','LIVE','0','BILLSTOP','') STS ,DECODE(TRIM(CTGOVT_PVT),'Y','GOVT','N','PVT','') GOVT_PVT,round(CB_OTH) CB_OTH, round(CB_OTH)+round(CBTOT) TOTTAL \r\n"
					+ "FROM LEDGER_HT_HIST,CONS \r\n" + "WHERE MON_YEAR=? AND CTUSCNO=USCNO\r\n"
					+ "AND SUBSTR(USCNO,0,3)=? \r\n" + "AND (round(CB_OTH)+round(CBTOT))>50000 \r\n"
					+ (status.equals("All") ? ""
							: " AND DECODE(CTSTATUS,'1','LIVE','0','BILLSTOP','') = '" + status + "' ")
					+ "UNION ALL\r\n"
					+ "SELECT DIVISION,SUBDIVISION,SECTION,USCNO,NAM,CTADD1,CTADD2,CTADD3,CAT,SCAT,STATUS,round(CBTOT) CBTOT,DECODE(CTSTATUS,'1','LIVE','0','BILLSTOP','') STS,DECODE(TRIM(CTGOVT_PVT),'Y','GOVT','N','PVT','') GOVT_PVT,round(CB_OTH) CB_OTH, round(CB_OTH)+round(CBTOT) TOTTAL \r\n"
					+ "FROM ACCOUNTCOPY,CONS\r\n" + "WHERE MON_YEAR=? AND CTUSCNO=USCNO\r\n"
					+ "AND SUBSTR(USCNO,0,3)=? \r\n" + "AND (round(CB_OTH)+round(CBTOT))>50000 \r\n"
					+ (status.equals("All") ? ""
							: " AND DECODE(CTSTATUS,'1','LIVE','0','BILLSTOP','') = '" + status + "' ")
					+ "ORDER BY USCNO";
			log.info(sql);
			return jdbcTemplate.queryForList(sql, new Object[] { monthYear, circle, monthYear, circle });
		}
	}

	public List<Map<String, Object>> getCourtCaseDetails(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		String monthYear = request.getParameter("month") + "-" + request.getParameter("year");
		if (circle.equals("ALL")) {
			String sql = "select USCNO,NAM , CIRCLE, DIVISION, SUBDIVISION, SECTION,CB_OTH,CB_CCLPC,RJ_OTH,RJ_CCLPC from ledger_ht_hist where mon_year=? and CB_OTH>0 ORDER BY USCNO";
			log.info(sql);
			return jdbcTemplate.queryForList(sql, new Object[] { monthYear });
		} else {
			String sql = "select USCNO,NAM , CIRCLE, DIVISION, SUBDIVISION, SECTION,CB_OTH,CB_CCLPC,RJ_OTH,RJ_CCLPC from ledger_ht_hist where mon_year=? and substr(uscno,1,3) = ? and CB_OTH>0 ORDER BY USCNO";
			log.info(sql);
			log.info(sql);
			return jdbcTemplate.queryForList(sql, new Object[] { monthYear, circle });
		}
	}

	public List<Map<String, Object>> getDListAppearedList(HttpServletRequest request) {

		SimpleJdbcCall partjdbcCall = new SimpleJdbcCall(jdbcTemplate.getDataSource()).withProcedureName("DCLIST");
		Map<String, String> partinputs = new HashMap<String, String>();
		log.info("Executing Procedure { exec DCLIST ()}");
		SqlParameterSource partin = new MapSqlParameterSource().addValues(partinputs);
		Map<String, Object> partout = partjdbcCall.execute(partin);
		log.info("Procedure Result " + partout);

		String circle = request.getParameter("circle");
		String monthYear = request.getParameter("month") + "-" + request.getParameter("year");
		if (circle.equals("ALL")) {
			String sql = "SELECT NVL(BILLED.CIRNAME,'Y_GTT') BIL_CIRCLE, NVL(BILLED.DIVNAME,'Y_TOT') BILDIV, (BILLED.GOVT_SCS) BIL_GOVTSCS, \r\n"
					+ "(BILLED.GOVT_DEM) BIL_GOVTDEM, \r\n"
					+ "(BILLED.PVT_SCS) BIL_PVTSCS, (BILLED.PVT_DEM) BIL_PVTDEM,\r\n"
					+ "(DLIST.GOVT_SCS) DLIST_GOVTSCS, (DLIST.GOVT_DEM) DLIST_GOVTDEM, (DLIST.GOVT_CB) DLIST_GOVTCB, (DLIST.PVT_SCS) DLIST_PVTSCS, \r\n"
					+ "(DLIST.PVT_DEM) DLIST_PVTDEM, (DLIST.PVT_CB) DLIST_PVTCB ,\r\n"
					+ "(ROUND((DLIST.GOVT_SCS/BILLED.GOVT_SCS)*100,2)) PER_GOVTSCS,(ROUND((DLIST.GOVT_DEM/BILLED.GOVT_DEM)*100,2)) PER_GOVDEM,\r\n"
					+ "(ROUND((DLIST.PVT_SCS/BILLED.PVT_SCS)*100,2)) PER_PVTSCS,(ROUND((DLIST.PVT_DEM/BILLED.PVT_DEM)*100,2)) PER_PVTDEM FROM\r\n"
					+ "(SELECT CIRNAME,DIVNAME,SUM(DECODE(TRIM(B.CTGOVT_PVT),'Y',1,0)) GOVT_SCS,SUM(DECODE(TRIM(B.CTGOVT_PVT),'Y',\r\n"
					+ "NVL(BTCURDEM,0),0)) GOVT_DEM\r\n"
					+ ",SUM(DECODE(TRIM(B.CTGOVT_PVT),'N',1,0)) PVT_SCS,SUM(DECODE(TRIM(B.CTGOVT_PVT),'N',NVL(BTCURDEM,0),0)) PVT_DEM\r\n"
					+ "FROM BILL A,CONS B,SPDCLMASTER C WHERE A.BT_ACCT_ID=B.CTACCT_ID AND TO_CHAR(BTBLDT,'MON-YYYY')=? \r\n"
					+ "AND C.SECCD=SUBSTR(B.CTSECCD,-5)\r\n" + "AND SUBSTR(BTSCNO,1,3) IN ('ONG','GNT','VJA','CRD')\r\n"
					+ "GROUP BY CIRNAME,DIVNAME ) BILLED,\r\n" + "(SELECT CIRCLE, DIVISION\r\n"
					+ ",SUM(DECODE(TRIM(B.CTGOVT_PVT),'Y',1,0)) GOVT_SCS,SUM(DECODE(TRIM(B.CTGOVT_PVT),'Y',NVL(DEMAND,0),0)) GOVT_DEM,\r\n"
					+ "SUM(DECODE(TRIM(B.CTGOVT_PVT),'Y',NVL(CB,0),0)) GOVT_CB\r\n"
					+ ",SUM(DECODE(TRIM(B.CTGOVT_PVT),'N',1,0)) PVT_SCS,SUM(DECODE(TRIM(B.CTGOVT_PVT),'N',NVL(DEMAND,0),0)) PVT_DEM,\r\n"
					+ "SUM(DECODE(TRIM(B.CTGOVT_PVT),'N',NVL(CB,0),0)) PVT_CB\r\n"
					+ "FROM D_LIST A,CONS B WHERE A.ACCT_ID=B.CTACCT_ID --AND BTBLDT='05-OCT-20'\r\n"
					+ "AND SUBSTR(SCNO,1,3) IN ('ONG','GNT','VJA','CRD')\r\n" + "GROUP BY CIRCLE, DIVISION ) DLIST \r\n"
					+ "    WHERE BILLED.CIRNAME=DLIST.CIRCLE \r\n" + "        AND BILLED.DIVNAME=DLIST.DIVISION \r\n"
					+ "        UNION ALL\r\n"
					+ "SELECT 'Y_TOTAL '  BIL_CIRCLE, 'Y_TOTAL ' BILDIV, (BILLED.GOVT_SCS) BIL_GOVTSCS, \r\n"
					+ "(BILLED.GOVT_DEM) BIL_GOVTDEM, \r\n"
					+ "(BILLED.PVT_SCS) BIL_PVTSCS, (BILLED.PVT_DEM) BIL_PVTDEM,\r\n"
					+ "(DLIST.GOVT_SCS) DLIST_GOVTSCS, (DLIST.GOVT_DEM) DLIST_GOVTDEM, (DLIST.GOVT_CB) DLIST_GOVTCB, (DLIST.PVT_SCS) DLIST_PVTSCS, \r\n"
					+ "(DLIST.PVT_DEM) DLIST_PVTDEM, (DLIST.PVT_CB) DLIST_PVTCB ,\r\n"
					+ "(ROUND((DLIST.GOVT_SCS/BILLED.GOVT_SCS)*100,2)) PER_GOVTSCS,(ROUND((DLIST.GOVT_DEM/BILLED.GOVT_DEM)*100,2)) PER_GOVDEM,\r\n"
					+ "(ROUND((DLIST.PVT_SCS/BILLED.PVT_SCS)*100,2)) PER_PVTSCS,(ROUND((DLIST.PVT_DEM/BILLED.PVT_DEM)*100,2)) PER_PVTDEM FROM\r\n"
					+ "(SELECT SUM(DECODE(TRIM(B.CTGOVT_PVT),'Y',1,0)) GOVT_SCS,SUM(DECODE(TRIM(B.CTGOVT_PVT),'Y',\r\n"
					+ "NVL(BTCURDEM,0),0)) GOVT_DEM\r\n"
					+ ",SUM(DECODE(TRIM(B.CTGOVT_PVT),'N',1,0)) PVT_SCS,SUM(DECODE(TRIM(B.CTGOVT_PVT),'N',NVL(BTCURDEM,0),0)) PVT_DEM\r\n"
					+ "FROM BILL A,CONS B,SPDCLMASTER C WHERE A.BT_ACCT_ID=B.CTACCT_ID AND TO_CHAR(BTBLDT,'MON-YYYY')=? AND C.SECCD=SUBSTR(B.CTSECCD,-5)\r\n"
					+ "AND SUBSTR(BTSCNO,1,3) IN ('ONG','GNT','VJA','CRD')\r\n" + " ) BILLED,\r\n" + "(SELECT \r\n"
					+ "SUM(DECODE(TRIM(B.CTGOVT_PVT),'Y',1,0)) GOVT_SCS,SUM(DECODE(TRIM(B.CTGOVT_PVT),'Y',NVL(DEMAND,0),0)) GOVT_DEM,\r\n"
					+ "SUM(DECODE(TRIM(B.CTGOVT_PVT),'Y',NVL(CB,0),0)) GOVT_CB\r\n"
					+ ",SUM(DECODE(TRIM(B.CTGOVT_PVT),'N',1,0)) PVT_SCS,SUM(DECODE(TRIM(B.CTGOVT_PVT),'N',NVL(DEMAND,0),0)) PVT_DEM,\r\n"
					+ "SUM(DECODE(TRIM(B.CTGOVT_PVT),'N',NVL(CB,0),0)) PVT_CB\r\n"
					+ "FROM D_LIST A,CONS B WHERE A.ACCT_ID=B.CTACCT_ID --AND BTBLDT='05-OCT-20'\r\n"
					+ "AND SUBSTR(SCNO,1,3) IN ('ONG','GNT','VJA','CRD')\r\n"
					+ " ) DLIST where (ROUND((DLIST.PVT_DEM/BILLED.PVT_DEM)*100,2)) is not null\r\n" + " \r\n"
					+ " UNION ALL\r\n"
					+ " SELECT NVL(BILLED.CIRNAME,'Y_GTT') BIL_CIRCLE, NVL(BILLED.DIVNAME,'Y_TOT') BILDIV, (BILLED.GOVT_SCS) BIL_GOVTSCS, (BILLED.GOVT_DEM) BIL_GOVTDEM, \r\n"
					+ "(BILLED.PVT_SCS) BIL_PVTSCS, (BILLED.PVT_DEM) BIL_PVTDEM,\r\n"
					+ "(DLIST.GOVT_SCS) DLIST_GOVTSCS, (DLIST.GOVT_DEM) DLIST_GOVTDEM, (DLIST.GOVT_CB) DLIST_GOVTCB, (DLIST.PVT_SCS) DLIST_PVTSCS, \r\n"
					+ "(DLIST.PVT_DEM) DLIST_PVTDEM, (DLIST.PVT_CB) DLIST_PVTCB ,\r\n"
					+ "(ROUND((DLIST.GOVT_SCS/BILLED.GOVT_SCS)*100,2)) PER_GOVTSCS,(ROUND((DLIST.GOVT_DEM/BILLED.GOVT_DEM)*100,2)) PER_GOVDEM,\r\n"
					+ "(ROUND((DLIST.PVT_SCS/BILLED.PVT_SCS)*100,2)) PER_PVTSCS,(ROUND((DLIST.PVT_DEM/BILLED.PVT_DEM)*100,2)) PER_PVTDEM FROM\r\n"
					+ "(SELECT CIRNAME,DIVNAME,SUM(DECODE(TRIM(B.CTGOVT_PVT),'Y',1,0)) GOVT_SCS,SUM(DECODE(TRIM(B.CTGOVT_PVT),'Y',\r\n"
					+ "NVL(BTCURDEM,0),0)) GOVT_DEM\r\n"
					+ ",SUM(DECODE(TRIM(B.CTGOVT_PVT),'N',1,0)) PVT_SCS,SUM(DECODE(TRIM(B.CTGOVT_PVT),'N',NVL(BTCURDEM,0),0)) PVT_DEM\r\n"
					+ "FROM \r\n" + "BILL_HIST A,CONS B,SPDCLMASTER C \r\n"
					+ "WHERE A.BT_ACCT_ID=B.CTACCT_ID AND TO_CHAR(BTBLDT,'MON-YYYY')=?  AND C.SECCD=SUBSTR(B.CTSECCD,-5)\r\n"
					+ "AND SUBSTR(BTSCNO,1,3) IN ('ONG','GNT','VJA','CRD')\r\n"
					+ "GROUP BY CIRNAME,DIVNAME ) BILLED,\r\n" + "(SELECT CIRCLE, DIVISION\r\n"
					+ ",SUM(DECODE(TRIM(B.CTGOVT_PVT),'Y',1,0)) GOVT_SCS,SUM(DECODE(TRIM(B.CTGOVT_PVT),'Y',NVL(DEMAND,0),0)) GOVT_DEM,\r\n"
					+ "SUM(DECODE(TRIM(B.CTGOVT_PVT),'Y',NVL(CB,0),0)) GOVT_CB\r\n"
					+ ",SUM(DECODE(TRIM(B.CTGOVT_PVT),'N',1,0)) PVT_SCS,SUM(DECODE(TRIM(B.CTGOVT_PVT),'N',NVL(DEMAND,0),0)) PVT_DEM,\r\n"
					+ "SUM(DECODE(TRIM(B.CTGOVT_PVT),'N',NVL(CB,0),0)) PVT_CB\r\n"
					+ "FROM D_LIST A,CONS B WHERE A.ACCT_ID=B.CTACCT_ID --AND BTBLDT='05-OCT-20'\r\n"
					+ "AND SUBSTR(SCNO,1,3) IN ('ONG','GNT','VJA','CRD')\r\n" + "GROUP BY CIRCLE, DIVISION ) DLIST \r\n"
					+ "    WHERE BILLED.CIRNAME=DLIST.CIRCLE \r\n" + "        AND BILLED.DIVNAME=DLIST.DIVISION \r\n"
					+ "        UNION ALL\r\n"
					+ "SELECT 'Y_TOTAL '  BIL_CIRCLE, 'Y_TOTAL ' BILDIV, (BILLED.GOVT_SCS) BIL_GOVTSCS, \r\n"
					+ "(BILLED.GOVT_DEM) BIL_GOVTDEM, \r\n"
					+ "(BILLED.PVT_SCS) BIL_PVTSCS, (BILLED.PVT_DEM) BIL_PVTDEM,\r\n"
					+ "(DLIST.GOVT_SCS) DLIST_GOVTSCS, (DLIST.GOVT_DEM) DLIST_GOVTDEM, (DLIST.GOVT_CB) DLIST_GOVTCB, (DLIST.PVT_SCS) DLIST_PVTSCS, \r\n"
					+ "(DLIST.PVT_DEM) DLIST_PVTDEM, (DLIST.PVT_CB) DLIST_PVTCB ,\r\n"
					+ "(ROUND((DLIST.GOVT_SCS/BILLED.GOVT_SCS)*100,2)) PER_GOVTSCS,(ROUND((DLIST.GOVT_DEM/BILLED.GOVT_DEM)*100,2)) PER_GOVDEM,\r\n"
					+ "(ROUND((DLIST.PVT_SCS/BILLED.PVT_SCS)*100,2)) PER_PVTSCS,(ROUND((DLIST.PVT_DEM/BILLED.PVT_DEM)*100,2)) PER_PVTDEM FROM\r\n"
					+ "(SELECT SUM(DECODE(TRIM(B.CTGOVT_PVT),'Y',1,0)) GOVT_SCS,SUM(DECODE(TRIM(B.CTGOVT_PVT),'Y',\r\n"
					+ "NVL(BTCURDEM,0),0)) GOVT_DEM\r\n"
					+ ",SUM(DECODE(TRIM(B.CTGOVT_PVT),'N',1,0)) PVT_SCS,SUM(DECODE(TRIM(B.CTGOVT_PVT),'N',NVL(BTCURDEM,0),0)) PVT_DEM\r\n"
					+ "FROM BILL_HIST A,CONS B,SPDCLMASTER C WHERE A.BT_ACCT_ID=B.CTACCT_ID AND TO_CHAR(BTBLDT,'MON-YYYY')=? \r\n"
					+ "AND C.SECCD=SUBSTR(B.CTSECCD,-5)\r\n" + "AND SUBSTR(BTSCNO,1,3) IN ('ONG','GNT','VJA','CRD')\r\n"
					+ " ) BILLED,\r\n" + "(SELECT \r\n"
					+ "SUM(DECODE(TRIM(B.CTGOVT_PVT),'Y',1,0)) GOVT_SCS,SUM(DECODE(TRIM(B.CTGOVT_PVT),'Y',NVL(DEMAND,0),0)) GOVT_DEM,\r\n"
					+ "SUM(DECODE(TRIM(B.CTGOVT_PVT),'Y',NVL(CB,0),0)) GOVT_CB\r\n"
					+ ",SUM(DECODE(TRIM(B.CTGOVT_PVT),'N',1,0)) PVT_SCS,SUM(DECODE(TRIM(B.CTGOVT_PVT),'N',NVL(DEMAND,0),0)) PVT_DEM,\r\n"
					+ "SUM(DECODE(TRIM(B.CTGOVT_PVT),'N',NVL(CB,0),0)) PVT_CB\r\n"
					+ "FROM D_LIST A,CONS B WHERE A.ACCT_ID=B.CTACCT_ID --AND BTBLDT='05-OCT-20'\r\n"
					+ "AND SUBSTR(SCNO,1,3) IN ('ONG','GNT','VJA','CRD')\r\n"
					+ " ) DLIST where (ROUND((DLIST.PVT_DEM/BILLED.PVT_DEM)*100,2)) is not null\r\n" + " ORDER BY 1,2";
			log.info(sql);
			return jdbcTemplate.queryForList(sql, new Object[] { monthYear, monthYear, monthYear, monthYear });
		} else {
			String sql = "SELECT NVL(BILLED.CIRNAME,'Y_GTT') BIL_CIRCLE, NVL(BILLED.DIVNAME,'Y_TOT') BILDIV, (BILLED.GOVT_SCS) BIL_GOVTSCS, \r\n"
					+ "(BILLED.GOVT_DEM) BIL_GOVTDEM, \r\n"
					+ "(BILLED.PVT_SCS) BIL_PVTSCS, (BILLED.PVT_DEM) BIL_PVTDEM,\r\n"
					+ "(DLIST.GOVT_SCS) DLIST_GOVTSCS, (DLIST.GOVT_DEM) DLIST_GOVTDEM, (DLIST.GOVT_CB) DLIST_GOVTCB, (DLIST.PVT_SCS) DLIST_PVTSCS, \r\n"
					+ "(DLIST.PVT_DEM) DLIST_PVTDEM, (DLIST.PVT_CB) DLIST_PVTCB ,\r\n"
					+ "(ROUND((DLIST.GOVT_SCS/BILLED.GOVT_SCS)*100,2)) PER_GOVTSCS,(ROUND((DLIST.GOVT_DEM/BILLED.GOVT_DEM)*100,2)) PER_GOVDEM,\r\n"
					+ "(ROUND((DLIST.PVT_SCS/BILLED.PVT_SCS)*100,2)) PER_PVTSCS,(ROUND((DLIST.PVT_DEM/BILLED.PVT_DEM)*100,2)) PER_PVTDEM FROM\r\n"
					+ "(SELECT CIRNAME,DIVNAME,SUM(DECODE(TRIM(B.CTGOVT_PVT),'Y',1,0)) GOVT_SCS,SUM(DECODE(TRIM(B.CTGOVT_PVT),'Y',\r\n"
					+ "NVL(BTCURDEM,0),0)) GOVT_DEM\r\n"
					+ ",SUM(DECODE(TRIM(B.CTGOVT_PVT),'N',1,0)) PVT_SCS,SUM(DECODE(TRIM(B.CTGOVT_PVT),'N',NVL(BTCURDEM,0),0)) PVT_DEM\r\n"
					+ "FROM BILL A,CONS B,SPDCLMASTER C WHERE A.BT_ACCT_ID=B.CTACCT_ID AND TO_CHAR(BTBLDT,'MON-YYYY')=? \r\n"
					+ "AND C.SECCD=SUBSTR(B.CTSECCD,-5)\r\n" + "AND SUBSTR(BTSCNO,1,3) = ? \r\n"
					+ "GROUP BY CIRNAME,DIVNAME ) BILLED,\r\n" + "(SELECT CIRCLE, DIVISION\r\n"
					+ ",SUM(DECODE(TRIM(B.CTGOVT_PVT),'Y',1,0)) GOVT_SCS,SUM(DECODE(TRIM(B.CTGOVT_PVT),'Y',NVL(DEMAND,0),0)) GOVT_DEM,\r\n"
					+ "SUM(DECODE(TRIM(B.CTGOVT_PVT),'Y',NVL(CB,0),0)) GOVT_CB\r\n"
					+ ",SUM(DECODE(TRIM(B.CTGOVT_PVT),'N',1,0)) PVT_SCS,SUM(DECODE(TRIM(B.CTGOVT_PVT),'N',NVL(DEMAND,0),0)) PVT_DEM,\r\n"
					+ "SUM(DECODE(TRIM(B.CTGOVT_PVT),'N',NVL(CB,0),0)) PVT_CB\r\n"
					+ "FROM D_LIST A,CONS B WHERE A.ACCT_ID=B.CTACCT_ID --AND BTBLDT='05-OCT-20'\r\n"
					+ "AND SUBSTR(SCNO,1,3) = ? \r\n" + "GROUP BY CIRCLE, DIVISION ) DLIST \r\n"
					+ "    WHERE BILLED.CIRNAME=DLIST.CIRCLE \r\n" + "        AND BILLED.DIVNAME=DLIST.DIVISION \r\n"
					+ "        UNION ALL\r\n"
					+ "SELECT 'Y_TOTAL '  BIL_CIRCLE, 'Y_TOTAL ' BILDIV, (BILLED.GOVT_SCS) BIL_GOVTSCS, \r\n"
					+ "(BILLED.GOVT_DEM) BIL_GOVTDEM, \r\n"
					+ "(BILLED.PVT_SCS) BIL_PVTSCS, (BILLED.PVT_DEM) BIL_PVTDEM,\r\n"
					+ "(DLIST.GOVT_SCS) DLIST_GOVTSCS, (DLIST.GOVT_DEM) DLIST_GOVTDEM, (DLIST.GOVT_CB) DLIST_GOVTCB, (DLIST.PVT_SCS) DLIST_PVTSCS, \r\n"
					+ "(DLIST.PVT_DEM) DLIST_PVTDEM, (DLIST.PVT_CB) DLIST_PVTCB ,\r\n"
					+ "(ROUND((DLIST.GOVT_SCS/BILLED.GOVT_SCS)*100,2)) PER_GOVTSCS,(ROUND((DLIST.GOVT_DEM/BILLED.GOVT_DEM)*100,2)) PER_GOVDEM,\r\n"
					+ "(ROUND((DLIST.PVT_SCS/BILLED.PVT_SCS)*100,2)) PER_PVTSCS,(ROUND((DLIST.PVT_DEM/BILLED.PVT_DEM)*100,2)) PER_PVTDEM FROM\r\n"
					+ "(SELECT SUM(DECODE(TRIM(B.CTGOVT_PVT),'Y',1,0)) GOVT_SCS,SUM(DECODE(TRIM(B.CTGOVT_PVT),'Y',\r\n"
					+ "NVL(BTCURDEM,0),0)) GOVT_DEM\r\n"
					+ ",SUM(DECODE(TRIM(B.CTGOVT_PVT),'N',1,0)) PVT_SCS,SUM(DECODE(TRIM(B.CTGOVT_PVT),'N',NVL(BTCURDEM,0),0)) PVT_DEM\r\n"
					+ "FROM BILL A,CONS B,SPDCLMASTER C WHERE A.BT_ACCT_ID=B.CTACCT_ID AND TO_CHAR(BTBLDT,'MON-YYYY')=? AND C.SECCD=SUBSTR(B.CTSECCD,-5)\r\n"
					+ "AND SUBSTR(BTSCNO,1,3) =?\r\n" + " ) BILLED,\r\n" + "(SELECT \r\n"
					+ "SUM(DECODE(TRIM(B.CTGOVT_PVT),'Y',1,0)) GOVT_SCS,SUM(DECODE(TRIM(B.CTGOVT_PVT),'Y',NVL(DEMAND,0),0)) GOVT_DEM,\r\n"
					+ "SUM(DECODE(TRIM(B.CTGOVT_PVT),'Y',NVL(CB,0),0)) GOVT_CB\r\n"
					+ ",SUM(DECODE(TRIM(B.CTGOVT_PVT),'N',1,0)) PVT_SCS,SUM(DECODE(TRIM(B.CTGOVT_PVT),'N',NVL(DEMAND,0),0)) PVT_DEM,\r\n"
					+ "SUM(DECODE(TRIM(B.CTGOVT_PVT),'N',NVL(CB,0),0)) PVT_CB\r\n"
					+ "FROM D_LIST A,CONS B WHERE A.ACCT_ID=B.CTACCT_ID --AND BTBLDT='05-OCT-20'\r\n"
					+ "AND SUBSTR(SCNO,1,3) =?\r\n"
					+ " ) DLIST where (ROUND((DLIST.PVT_DEM/BILLED.PVT_DEM)*100,2)) is not null\r\n" + " \r\n"
					+ " UNION ALL\r\n"
					+ " SELECT NVL(BILLED.CIRNAME,'Y_GTT') BIL_CIRCLE, NVL(BILLED.DIVNAME,'Y_TOT') BILDIV, (BILLED.GOVT_SCS) BIL_GOVTSCS, (BILLED.GOVT_DEM) BIL_GOVTDEM, \r\n"
					+ "(BILLED.PVT_SCS) BIL_PVTSCS, (BILLED.PVT_DEM) BIL_PVTDEM,\r\n"
					+ "(DLIST.GOVT_SCS) DLIST_GOVTSCS, (DLIST.GOVT_DEM) DLIST_GOVTDEM, (DLIST.GOVT_CB) DLIST_GOVTCB, (DLIST.PVT_SCS) DLIST_PVTSCS, \r\n"
					+ "(DLIST.PVT_DEM) DLIST_PVTDEM, (DLIST.PVT_CB) DLIST_PVTCB ,\r\n"
					+ "(ROUND((DLIST.GOVT_SCS/BILLED.GOVT_SCS)*100,2)) PER_GOVTSCS,(ROUND((DLIST.GOVT_DEM/BILLED.GOVT_DEM)*100,2)) PER_GOVDEM,\r\n"
					+ "(ROUND((DLIST.PVT_SCS/BILLED.PVT_SCS)*100,2)) PER_PVTSCS,(ROUND((DLIST.PVT_DEM/BILLED.PVT_DEM)*100,2)) PER_PVTDEM FROM\r\n"
					+ "(SELECT CIRNAME,DIVNAME,SUM(DECODE(TRIM(B.CTGOVT_PVT),'Y',1,0)) GOVT_SCS,SUM(DECODE(TRIM(B.CTGOVT_PVT),'Y',\r\n"
					+ "NVL(BTCURDEM,0),0)) GOVT_DEM\r\n"
					+ ",SUM(DECODE(TRIM(B.CTGOVT_PVT),'N',1,0)) PVT_SCS,SUM(DECODE(TRIM(B.CTGOVT_PVT),'N',NVL(BTCURDEM,0),0)) PVT_DEM\r\n"
					+ "FROM \r\n" + "BILL_HIST A,CONS B,SPDCLMASTER C \r\n"
					+ "WHERE A.BT_ACCT_ID=B.CTACCT_ID AND TO_CHAR(BTBLDT,'MON-YYYY')=?  AND C.SECCD=SUBSTR(B.CTSECCD,-5)\r\n"
					+ "AND SUBSTR(BTSCNO,1,3) =?\r\n" + "GROUP BY CIRNAME,DIVNAME ) BILLED,\r\n"
					+ "(SELECT CIRCLE, DIVISION\r\n"
					+ ",SUM(DECODE(TRIM(B.CTGOVT_PVT),'Y',1,0)) GOVT_SCS,SUM(DECODE(TRIM(B.CTGOVT_PVT),'Y',NVL(DEMAND,0),0)) GOVT_DEM,\r\n"
					+ "SUM(DECODE(TRIM(B.CTGOVT_PVT),'Y',NVL(CB,0),0)) GOVT_CB\r\n"
					+ ",SUM(DECODE(TRIM(B.CTGOVT_PVT),'N',1,0)) PVT_SCS,SUM(DECODE(TRIM(B.CTGOVT_PVT),'N',NVL(DEMAND,0),0)) PVT_DEM,\r\n"
					+ "SUM(DECODE(TRIM(B.CTGOVT_PVT),'N',NVL(CB,0),0)) PVT_CB\r\n"
					+ "FROM D_LIST A,CONS B WHERE A.ACCT_ID=B.CTACCT_ID --AND BTBLDT='05-OCT-20'\r\n"
					+ "AND SUBSTR(SCNO,1,3) =?\r\n" + "GROUP BY CIRCLE, DIVISION ) DLIST \r\n"
					+ "    WHERE BILLED.CIRNAME=DLIST.CIRCLE \r\n" + "        AND BILLED.DIVNAME=DLIST.DIVISION \r\n"
					+ "        UNION ALL\r\n"
					+ "SELECT 'Y_TOTAL '  BIL_CIRCLE, 'Y_TOTAL ' BILDIV, (BILLED.GOVT_SCS) BIL_GOVTSCS, \r\n"
					+ "(BILLED.GOVT_DEM) BIL_GOVTDEM, \r\n"
					+ "(BILLED.PVT_SCS) BIL_PVTSCS, (BILLED.PVT_DEM) BIL_PVTDEM,\r\n"
					+ "(DLIST.GOVT_SCS) DLIST_GOVTSCS, (DLIST.GOVT_DEM) DLIST_GOVTDEM, (DLIST.GOVT_CB) DLIST_GOVTCB, (DLIST.PVT_SCS) DLIST_PVTSCS, \r\n"
					+ "(DLIST.PVT_DEM) DLIST_PVTDEM, (DLIST.PVT_CB) DLIST_PVTCB ,\r\n"
					+ "(ROUND((DLIST.GOVT_SCS/BILLED.GOVT_SCS)*100,2)) PER_GOVTSCS,(ROUND((DLIST.GOVT_DEM/BILLED.GOVT_DEM)*100,2)) PER_GOVDEM,\r\n"
					+ "(ROUND((DLIST.PVT_SCS/BILLED.PVT_SCS)*100,2)) PER_PVTSCS,(ROUND((DLIST.PVT_DEM/BILLED.PVT_DEM)*100,2)) PER_PVTDEM FROM\r\n"
					+ "(SELECT SUM(DECODE(TRIM(B.CTGOVT_PVT),'Y',1,0)) GOVT_SCS,SUM(DECODE(TRIM(B.CTGOVT_PVT),'Y',\r\n"
					+ "NVL(BTCURDEM,0),0)) GOVT_DEM\r\n"
					+ ",SUM(DECODE(TRIM(B.CTGOVT_PVT),'N',1,0)) PVT_SCS,SUM(DECODE(TRIM(B.CTGOVT_PVT),'N',NVL(BTCURDEM,0),0)) PVT_DEM\r\n"
					+ "FROM BILL_HIST A,CONS B,SPDCLMASTER C WHERE A.BT_ACCT_ID=B.CTACCT_ID AND TO_CHAR(BTBLDT,'MON-YYYY')=? \r\n"
					+ "AND C.SECCD=SUBSTR(B.CTSECCD,-5)\r\n" + "AND SUBSTR(BTSCNO,1,3) =?\r\n" + " ) BILLED,\r\n"
					+ "(SELECT \r\n"
					+ "SUM(DECODE(TRIM(B.CTGOVT_PVT),'Y',1,0)) GOVT_SCS,SUM(DECODE(TRIM(B.CTGOVT_PVT),'Y',NVL(DEMAND,0),0)) GOVT_DEM,\r\n"
					+ "SUM(DECODE(TRIM(B.CTGOVT_PVT),'Y',NVL(CB,0),0)) GOVT_CB\r\n"
					+ ",SUM(DECODE(TRIM(B.CTGOVT_PVT),'N',1,0)) PVT_SCS,SUM(DECODE(TRIM(B.CTGOVT_PVT),'N',NVL(DEMAND,0),0)) PVT_DEM,\r\n"
					+ "SUM(DECODE(TRIM(B.CTGOVT_PVT),'N',NVL(CB,0),0)) PVT_CB\r\n"
					+ "FROM D_LIST A,CONS B WHERE A.ACCT_ID=B.CTACCT_ID --AND BTBLDT='05-OCT-20'\r\n"
					+ "AND SUBSTR(SCNO,1,3) =?\r\n"
					+ " ) DLIST where (ROUND((DLIST.PVT_DEM/BILLED.PVT_DEM)*100,2)) is not null\r\n" + " ORDER BY 1,2";
			log.info(sql);
			return jdbcTemplate.queryForList(sql, new Object[] { monthYear, circle, circle, monthYear, circle, circle,
					monthYear, circle, circle, monthYear, circle, circle });
		}

	}

	public List<Map<String, Object>> getDlistDivision(HttpServletRequest request) {

		SimpleJdbcCall partjdbcCall = new SimpleJdbcCall(jdbcTemplate.getDataSource()).withProcedureName("DCLIST");
		Map<String, String> partinputs = new HashMap<String, String>();
		log.info("Executing Procedure { exec DCLIST ()}");
		SqlParameterSource partin = new MapSqlParameterSource().addValues(partinputs);
		Map<String, Object> partout = partjdbcCall.execute(partin);
		log.info("Procedure Result " + partout);

		String circle = request.getParameter("circle");
		String monthYear = request.getParameter("month") + "-" + request.getParameter("year");
		if (circle.equalsIgnoreCase("ALL")) {
			String sql = "SELECT NVL(BILLED.CIRNAME,'Y_GTT') BIL_CIRCLE, NVL(BILLED.DIVNAME,'Y_TOT') BILDIV, (BILLED.GOVT_SCS) BIL_GOVTSCS, (BILLED.GOVT_DEM) BIL_GOVTDEM, \r\n"
					+ "(BILLED.PVT_SCS) BIL_PVTSCS, (BILLED.PVT_DEM) BIL_PVTDEM,\r\n"
					+ "(DLIST.GOVT_SCS) DLIST_GOVTSCS, (DLIST.GOVT_DEM) DLIST_GOVTDEM, (DLIST.GOVT_CB/10000000) DLIST_GOVTCB, (DLIST.PVT_SCS) DLIST_PVTSCS, \r\n"
					+ "(DLIST.PVT_DEM) DLIST_PVTDEM, (DLIST.PVT_CB/10000000) DLIST_PVTCB , ROUND((DLIST.GOVT_CB/10000000),3)+ROUND((DLIST.PVT_CB/10000000),3) SUMCB,\r\n"
					+ "(ROUND((DLIST.GOVT_SCS/BILLED.GOVT_SCS)*100,2)) PER_GOVTSCS,(ROUND((DLIST.GOVT_DEM/BILLED.GOVT_DEM)*100,2)) PER_GOVDEM,\r\n"
					+ "(ROUND((DLIST.PVT_SCS/BILLED.PVT_SCS)*100,2)) PER_PVTSCS,(ROUND((DLIST.PVT_DEM/BILLED.PVT_DEM)*100,2)) PER_PVTDEM FROM\r\n"
					+ "(SELECT CIRNAME,DIVNAME,SUM(DECODE(TRIM(B.CTGOVT_PVT),'Y',1,0)) GOVT_SCS,SUM(DECODE(TRIM(B.CTGOVT_PVT),'Y',\r\n"
					+ "NVL(BTCURDEM,0),0)) GOVT_DEM\r\n"
					+ ",SUM(DECODE(TRIM(B.CTGOVT_PVT),'N',1,0)) PVT_SCS,SUM(DECODE(TRIM(B.CTGOVT_PVT),'N',NVL(BTCURDEM,0),0)) PVT_DEM\r\n"
					+ "FROM (select * from bill union all select * from BILL_HIST) A,CONS B,SPDCLMASTER C WHERE A.BT_ACCT_ID=B.CTACCT_ID AND TO_CHAR(BTBLDT,'MON-YYYY')=? AND C.SECCD=SUBSTR(B.CTSECCD,-5)\r\n"
					+ "AND SUBSTR(BTSCNO,1,3)  IN ('ONG','GNT','VJA','CRD')\r\n"
					+ "GROUP BY CIRNAME,DIVNAME ) BILLED,\r\n" + "(SELECT CIRCLE, DIVISION\r\n"
					+ ",SUM(DECODE(TRIM(B.CTGOVT_PVT),'Y',1,0)) GOVT_SCS,SUM(DECODE(TRIM(B.CTGOVT_PVT),'Y',NVL(DEMAND,0),0)) GOVT_DEM,\r\n"
					+ "SUM(DECODE(TRIM(B.CTGOVT_PVT),'Y',NVL(CB,0),0)) GOVT_CB\r\n"
					+ ",SUM(DECODE(TRIM(B.CTGOVT_PVT),'N',1,0)) PVT_SCS,SUM(DECODE(TRIM(B.CTGOVT_PVT),'N',NVL(DEMAND,0),0)) PVT_DEM,\r\n"
					+ "SUM(DECODE(TRIM(B.CTGOVT_PVT),'N',NVL(CB,0),0)) PVT_CB\r\n"
					+ "FROM D_LIST A,CONS B WHERE A.ACCT_ID=B.CTACCT_ID \r\n"
					+ "AND SUBSTR(SCNO,1,3)  IN ('ONG','GNT','VJA','CRD')\r\n"
					+ "GROUP BY CIRCLE, DIVISION ) DLIST \r\n" + "    WHERE BILLED.CIRNAME=DLIST.CIRCLE \r\n"
					+ "        AND BILLED.DIVNAME=DLIST.DIVISION \r\n" + "        UNION ALL\r\n"
					+ "SELECT 'Y_TOTAL '  BIL_CIRCLE, 'Y_TOTAL ' BILDIV, (BILLED.GOVT_SCS) BIL_GOVTSCS, \r\n"
					+ "(BILLED.GOVT_DEM) BIL_GOVTDEM, \r\n"
					+ "(BILLED.PVT_SCS) BIL_PVTSCS, (BILLED.PVT_DEM) BIL_PVTDEM,\r\n"
					+ "(DLIST.GOVT_SCS) DLIST_GOVTSCS, (DLIST.GOVT_DEM) DLIST_GOVTDEM, (DLIST.GOVT_CB/10000000) DLIST_GOVTCB, (DLIST.PVT_SCS) DLIST_PVTSCS, \r\n"
					+ "(DLIST.PVT_DEM) DLIST_PVTDEM, (DLIST.PVT_CB/10000000) DLIST_PVTCB ,ROUND((DLIST.GOVT_CB/10000000),3)+ROUND((DLIST.PVT_CB/10000000),3) SUMCB,\r\n"
					+ "(ROUND((DLIST.GOVT_SCS/BILLED.GOVT_SCS)*100,2)) PER_GOVTSCS,(ROUND((DLIST.GOVT_DEM/BILLED.GOVT_DEM)*100,2)) PER_GOVDEM,\r\n"
					+ "(ROUND((DLIST.PVT_SCS/BILLED.PVT_SCS)*100,2)) PER_PVTSCS,(ROUND((DLIST.PVT_DEM/BILLED.PVT_DEM)*100,2)) PER_PVTDEM FROM\r\n"
					+ "(SELECT SUM(DECODE(TRIM(B.CTGOVT_PVT),'Y',1,0)) GOVT_SCS,SUM(DECODE(TRIM(B.CTGOVT_PVT),'Y',\r\n"
					+ "NVL(BTCURDEM,0),0)) GOVT_DEM\r\n"
					+ ",SUM(DECODE(TRIM(B.CTGOVT_PVT),'N',1,0)) PVT_SCS,SUM(DECODE(TRIM(B.CTGOVT_PVT),'N',NVL(BTCURDEM,0),0)) PVT_DEM\r\n"
					+ "FROM (select * from bill union all select * from BILL_HIST) A,CONS B,SPDCLMASTER C WHERE A.BT_ACCT_ID=B.CTACCT_ID AND TO_CHAR(BTBLDT,'MON-YYYY')=? AND C.SECCD=SUBSTR(B.CTSECCD,-5)\r\n"
					+ "AND SUBSTR(BTSCNO,1,3)  IN ('ONG','GNT','VJA','CRD')\r\n" + " ) BILLED,\r\n" + "(SELECT \r\n"
					+ "SUM(DECODE(TRIM(B.CTGOVT_PVT),'Y',1,0)) GOVT_SCS,SUM(DECODE(TRIM(B.CTGOVT_PVT),'Y',NVL(DEMAND,0),0)) GOVT_DEM,\r\n"
					+ "SUM(DECODE(TRIM(B.CTGOVT_PVT),'Y',NVL(CB,0),0)) GOVT_CB\r\n"
					+ ",SUM(DECODE(TRIM(B.CTGOVT_PVT),'N',1,0)) PVT_SCS,SUM(DECODE(TRIM(B.CTGOVT_PVT),'N',NVL(DEMAND,0),0)) PVT_DEM,\r\n"
					+ "SUM(DECODE(TRIM(B.CTGOVT_PVT),'N',NVL(CB,0),0)) PVT_CB\r\n"
					+ "FROM D_LIST A,CONS B WHERE A.ACCT_ID=B.CTACCT_ID --AND BTBLDT='05-OCT-20'\r\n"
					+ "AND SUBSTR(SCNO,1,3)  IN ('ONG','GNT','VJA','CRD')\r\n" + " ) DLIST \r\n" + " ORDER BY 1,2";
			log.info(sql);
			return jdbcTemplate.queryForList(sql, new Object[] { monthYear, monthYear });

		} else {
			String sql = "SELECT NVL(BILLED.CIRNAME,'Y_GTT') BIL_CIRCLE, NVL(BILLED.DIVNAME,'Y_TOT') BILDIV, (BILLED.GOVT_SCS) BIL_GOVTSCS, (BILLED.GOVT_DEM) BIL_GOVTDEM, \r\n"
					+ "(BILLED.PVT_SCS) BIL_PVTSCS, (BILLED.PVT_DEM) BIL_PVTDEM,\r\n"
					+ "(DLIST.GOVT_SCS) DLIST_GOVTSCS, (DLIST.GOVT_DEM) DLIST_GOVTDEM, (DLIST.GOVT_CB/10000000) DLIST_GOVTCB, (DLIST.PVT_SCS) DLIST_PVTSCS, \r\n"
					+ "(DLIST.PVT_DEM) DLIST_PVTDEM, (DLIST.PVT_CB/10000000) DLIST_PVTCB , ROUND((DLIST.GOVT_CB/10000000),3)+ROUND((DLIST.PVT_CB/10000000),3) SUMCB,\r\n"
					+ "(ROUND((DLIST.GOVT_SCS/BILLED.GOVT_SCS)*100,2)) PER_GOVTSCS,(ROUND((DLIST.GOVT_DEM/BILLED.GOVT_DEM)*100,2)) PER_GOVDEM,\r\n"
					+ "(ROUND((DLIST.PVT_SCS/BILLED.PVT_SCS)*100,2)) PER_PVTSCS,(ROUND((DLIST.PVT_DEM/BILLED.PVT_DEM)*100,2)) PER_PVTDEM FROM\r\n"
					+ "(SELECT CIRNAME,DIVNAME,SUM(DECODE(TRIM(B.CTGOVT_PVT),'Y',1,0)) GOVT_SCS,SUM(DECODE(TRIM(B.CTGOVT_PVT),'Y',\r\n"
					+ "NVL(BTCURDEM,0),0)) GOVT_DEM\r\n"
					+ ",SUM(DECODE(TRIM(B.CTGOVT_PVT),'N',1,0)) PVT_SCS,SUM(DECODE(TRIM(B.CTGOVT_PVT),'N',NVL(BTCURDEM,0),0)) PVT_DEM\r\n"
					+ "FROM (select * from bill union all select * from BILL_HIST) A,CONS B,SPDCLMASTER C WHERE A.BT_ACCT_ID=B.CTACCT_ID AND TO_CHAR(BTBLDT,'MON-YYYY')=? AND C.SECCD=SUBSTR(B.CTSECCD,-5)\r\n"
					+ "AND SUBSTR(BTSCNO,1,3)  =? \r\n" + "GROUP BY CIRNAME,DIVNAME ) BILLED,\r\n"
					+ "(SELECT CIRCLE, DIVISION\r\n"
					+ ",SUM(DECODE(TRIM(B.CTGOVT_PVT),'Y',1,0)) GOVT_SCS,SUM(DECODE(TRIM(B.CTGOVT_PVT),'Y',NVL(DEMAND,0),0)) GOVT_DEM,\r\n"
					+ "SUM(DECODE(TRIM(B.CTGOVT_PVT),'Y',NVL(CB,0),0)) GOVT_CB\r\n"
					+ ",SUM(DECODE(TRIM(B.CTGOVT_PVT),'N',1,0)) PVT_SCS,SUM(DECODE(TRIM(B.CTGOVT_PVT),'N',NVL(DEMAND,0),0)) PVT_DEM,\r\n"
					+ "SUM(DECODE(TRIM(B.CTGOVT_PVT),'N',NVL(CB,0),0)) PVT_CB\r\n"
					+ "FROM D_LIST A,CONS B WHERE A.ACCT_ID=B.CTACCT_ID \r\n" + "AND SUBSTR(SCNO,1,3)  = ? \r\n"
					+ "GROUP BY CIRCLE, DIVISION ) DLIST \r\n" + "    WHERE BILLED.CIRNAME=DLIST.CIRCLE \r\n"
					+ "        AND BILLED.DIVNAME=DLIST.DIVISION \r\n" + "        UNION ALL\r\n"
					+ "SELECT 'Y_TOTAL '  BIL_CIRCLE, 'Y_TOTAL ' BILDIV, (BILLED.GOVT_SCS) BIL_GOVTSCS, \r\n"
					+ "(BILLED.GOVT_DEM) BIL_GOVTDEM, \r\n"
					+ "(BILLED.PVT_SCS) BIL_PVTSCS, (BILLED.PVT_DEM) BIL_PVTDEM,\r\n"
					+ "(DLIST.GOVT_SCS) DLIST_GOVTSCS, (DLIST.GOVT_DEM) DLIST_GOVTDEM, (DLIST.GOVT_CB/10000000) DLIST_GOVTCB, (DLIST.PVT_SCS) DLIST_PVTSCS, \r\n"
					+ "(DLIST.PVT_DEM) DLIST_PVTDEM, (DLIST.PVT_CB/10000000) DLIST_PVTCB ,ROUND((DLIST.GOVT_CB/10000000),3)+ROUND((DLIST.PVT_CB/10000000),3) SUMCB,\r\n"
					+ "(ROUND((DLIST.GOVT_SCS/BILLED.GOVT_SCS)*100,2)) PER_GOVTSCS,(ROUND((DLIST.GOVT_DEM/BILLED.GOVT_DEM)*100,2)) PER_GOVDEM,\r\n"
					+ "(ROUND((DLIST.PVT_SCS/BILLED.PVT_SCS)*100,2)) PER_PVTSCS,(ROUND((DLIST.PVT_DEM/BILLED.PVT_DEM)*100,2)) PER_PVTDEM FROM\r\n"
					+ "(SELECT SUM(DECODE(TRIM(B.CTGOVT_PVT),'Y',1,0)) GOVT_SCS,SUM(DECODE(TRIM(B.CTGOVT_PVT),'Y',\r\n"
					+ "NVL(BTCURDEM,0),0)) GOVT_DEM\r\n"
					+ ",SUM(DECODE(TRIM(B.CTGOVT_PVT),'N',1,0)) PVT_SCS,SUM(DECODE(TRIM(B.CTGOVT_PVT),'N',NVL(BTCURDEM,0),0)) PVT_DEM\r\n"
					+ "FROM (select * from bill union all select * from BILL_HIST) A,CONS B,SPDCLMASTER C WHERE A.BT_ACCT_ID=B.CTACCT_ID AND TO_CHAR(BTBLDT,'MON-YYYY')=? AND C.SECCD=SUBSTR(B.CTSECCD,-5)\r\n"
					+ "AND SUBSTR(BTSCNO,1,3)  = ? \r\n" + " ) BILLED,\r\n" + "(SELECT \r\n"
					+ "SUM(DECODE(TRIM(B.CTGOVT_PVT),'Y',1,0)) GOVT_SCS,SUM(DECODE(TRIM(B.CTGOVT_PVT),'Y',NVL(DEMAND,0),0)) GOVT_DEM,\r\n"
					+ "SUM(DECODE(TRIM(B.CTGOVT_PVT),'Y',NVL(CB,0),0)) GOVT_CB\r\n"
					+ ",SUM(DECODE(TRIM(B.CTGOVT_PVT),'N',1,0)) PVT_SCS,SUM(DECODE(TRIM(B.CTGOVT_PVT),'N',NVL(DEMAND,0),0)) PVT_DEM,\r\n"
					+ "SUM(DECODE(TRIM(B.CTGOVT_PVT),'N',NVL(CB,0),0)) PVT_CB\r\n"
					+ "FROM D_LIST A,CONS B WHERE A.ACCT_ID=B.CTACCT_ID --AND BTBLDT='05-OCT-20'\r\n"
					+ "AND SUBSTR(SCNO,1,3) = ? \r\n" + " ) DLIST \r\n" + " ORDER BY 1,2";
			log.info(sql);
			return jdbcTemplate.queryForList(sql,
					new Object[] { monthYear, circle, circle, monthYear, circle, circle });
		}
	}

	public List<Map<String, Object>> getUnpaidServices(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		String monthYear = request.getParameter("month") + "-" + request.getParameter("year");
		if (!circle.equalsIgnoreCase("ALL")) {
			String sql = "SELECT USCNO,NAM,SCAT,CTACTUAL_KV,DIVISION,SUBDIVISION,DECODE(TRIM(CONS.CTSTATUS), '1', 'LIVE', '0', 'STOP', '') CTSTATUS,"
					+ " DECODE(TRIM(CTGOVT_PVT), 'Y', 'GOVT', 'N', 'PVT', '') GOVT_PVT,"
					+ " OB_OTH + OB_CCLPC AS_PER_COURT_OB, TOT_OB OTHER_TAHN_COURT_OB,OB_OTH + OB_CCLPC + TOT_OB OB_TOTAL,CMD DEMAND_OTHER_THAN_COURT,   "
					+ " CCLPC DEMAND_OTHER_THAN_COURT_LPC, TOT_PAY COLLECTION, CB_OTH + CB_CCLPC AS_PER_COURT_CB,CBTOT OTHER_TAHN_COURT_CB,CB_OTH + CB_CCLPC + CBTOT CB_TOTAL,"
					+ " MD_STATUS.CTSTA,MON_YEAR FROM LEDGER_HT_HIST, CONS,(SELECT DISTINCT   MSCNO, CASE WHEN MDCLKWHSTAT_HT = 3 THEN  'UDC' ELSE   'LIVE'  END  CTSTA  FROM  MTRDAT_HIST  WHERE  TO_CHAR(MDMONTH, 'MON-YYYY') = ? "
					+ " AND MDCLKWHSTAT_HT NOT IN ('4', '04', '14') UNION ALL  SELECT DISTINCT  MSCNO,  CASE   WHEN  MDCLKWHSTAT_HT = 3  THEN   'UDC'   ELSE  'LIVE'  END  CTSTA  FROM   MTRDAT WHERE   TO_CHAR(MDMONTH, 'MON-YYYY') = ? "
					+ " AND MDCLKWHSTAT_HT NOT IN( '4', '04', '14'  ) )    MD_STATUS WHERE    CTUSCNO = USCNO    AND USCNO = MD_STATUS.MSCNO    AND MON_YEAR = ?    AND SUBSTR(USCNO, 1, 3) = ?    AND CBTOT > 0"
					+ " UNION ALL"
					+ " SELECT USCNO,NAM,SCAT,CTACTUAL_KV,DIVISION,SUBDIVISION,DECODE(TRIM(CONS.CTSTATUS), '1', 'LIVE', '0', 'STOP', '') CTSTATUS, "
					+ " DECODE(TRIM(CTGOVT_PVT), 'Y', 'GOVT', 'N', 'PVT', '') GOVT_PVT,  "
					+ " OB_OTH + OB_CCLPC AS_PER_COURT_OB, TOT_OB OTHER_TAHN_COURT_OB,OB_OTH + OB_CCLPC + TOT_OB OB_TOTAL,CMD DEMAND_OTHER_THAN_COURT,  "
					+ " CCLPC DEMAND_OTHER_THAN_COURT_LPC,TOT_PAY COLLECTION,CB_OTH + CB_CCLPC AS_PER_COURT_CB,CBTOT OTHER_TAHN_COURT_CB,CB_OTH + CB_CCLPC + CBTOT CB_TOTAL,  "
					+ " MD_STATUS.CTSTA,MON_YEAR FROM ACCOUNTCOPY,CONS,(SELECT DISTINCT MSCNO,CASE WHEN MDCLKWHSTAT_HT = 3 THEN    'UDC'   ELSE    'LIVE' END   CTSTA  FROM  MTRDAT_HIST  WHERE  TO_CHAR(MDMONTH, 'MON-YYYY') = ? "
					+ " AND MDCLKWHSTAT_HT NOT IN ( '4', '04', '14' )  UNION ALL SELECT DISTINCT  MSCNO,  CASE    WHEN  MDCLKWHSTAT_HT = 3   THEN   'UDC'   ELSE   'LIVE'   END  CTSTA  FROM   MTRDAT       WHERE   TO_CHAR(MDMONTH, 'MON-YYYY') = ? "
					+ " AND MDCLKWHSTAT_HT NOT IN ('4', '04', '14' ))  MD_STATUS WHERE    CTUSCNO = USCNO    AND USCNO = MD_STATUS.MSCNO    AND MON_YEAR = ?    AND SUBSTR(USCNO, 1, 3) = ?    AND CBTOT > 0  ORDER BY USCNO";
			log.info(sql);
			return jdbcTemplate.queryForList(sql,
					new Object[] { monthYear, monthYear, monthYear, circle, monthYear, monthYear, monthYear, circle });
		} else {
			String sql = "SELECT USCNO,NAM,SCAT,CTACTUAL_KV,DIVISION,SUBDIVISION,DECODE(TRIM(CONS.CTSTATUS), '1', 'LIVE', '0', 'STOP', '') CTSTATUS,"
					+ " DECODE(TRIM(CTGOVT_PVT), 'Y', 'GOVT', 'N', 'PVT', '') GOVT_PVT,"
					+ " OB_OTH + OB_CCLPC AS_PER_COURT_OB, TOT_OB OTHER_TAHN_COURT_OB,OB_OTH + OB_CCLPC + TOT_OB OB_TOTAL,CMD DEMAND_OTHER_THAN_COURT,   "
					+ " CCLPC DEMAND_OTHER_THAN_COURT_LPC, TOT_PAY COLLECTION, CB_OTH + CB_CCLPC AS_PER_COURT_CB,CBTOT OTHER_TAHN_COURT_CB,CB_OTH + CB_CCLPC + CBTOT CB_TOTAL,"
					+ " MD_STATUS.CTSTA,MON_YEAR FROM LEDGER_HT_HIST, CONS,(SELECT DISTINCT   MSCNO, CASE WHEN MDCLKWHSTAT_HT = 3 THEN  'UDC' ELSE   'LIVE'  END  CTSTA  FROM  MTRDAT_HIST  WHERE  TO_CHAR(MDMONTH, 'MON-YYYY') = ? "
					+ " AND MDCLKWHSTAT_HT NOT IN ('4', '04', '14') UNION ALL  SELECT DISTINCT  MSCNO,  CASE   WHEN  MDCLKWHSTAT_HT = 3  THEN   'UDC'   ELSE  'LIVE'  END  CTSTA  FROM   MTRDAT WHERE   TO_CHAR(MDMONTH, 'MON-YYYY') = ? "
					+ " AND MDCLKWHSTAT_HT NOT IN( '4', '04', '14'  ) )    MD_STATUS WHERE    CTUSCNO = USCNO    AND USCNO = MD_STATUS.MSCNO    AND MON_YEAR = ?    AND CBTOT > 0"
					+ " UNION ALL"
					+ " SELECT USCNO,NAM,SCAT,CTACTUAL_KV,DIVISION,SUBDIVISION,DECODE(TRIM(CONS.CTSTATUS), '1', 'LIVE', '0', 'STOP', '') CTSTATUS, "
					+ " DECODE(TRIM(CTGOVT_PVT), 'Y', 'GOVT', 'N', 'PVT', '') GOVT_PVT,  "
					+ " OB_OTH + OB_CCLPC AS_PER_COURT_OB, TOT_OB OTHER_TAHN_COURT_OB,OB_OTH + OB_CCLPC + TOT_OB OB_TOTAL,CMD DEMAND_OTHER_THAN_COURT,  "
					+ " CCLPC DEMAND_OTHER_THAN_COURT_LPC,TOT_PAY COLLECTION,CB_OTH + CB_CCLPC AS_PER_COURT_CB,CBTOT OTHER_TAHN_COURT_CB,CB_OTH + CB_CCLPC + CBTOT CB_TOTAL,  "
					+ " MD_STATUS.CTSTA,MON_YEAR FROM ACCOUNTCOPY,CONS,(SELECT DISTINCT MSCNO,CASE WHEN MDCLKWHSTAT_HT = 3 THEN    'UDC'   ELSE    'LIVE' END   CTSTA  FROM  MTRDAT_HIST  WHERE  TO_CHAR(MDMONTH, 'MON-YYYY') = ? "
					+ " AND MDCLKWHSTAT_HT NOT IN ( '4', '04', '14' )  UNION ALL SELECT DISTINCT  MSCNO,  CASE    WHEN  MDCLKWHSTAT_HT = 3   THEN   'UDC'   ELSE   'LIVE'   END  CTSTA  FROM   MTRDAT       WHERE   TO_CHAR(MDMONTH, 'MON-YYYY') = ? "
					+ " AND MDCLKWHSTAT_HT NOT IN ('4', '04', '14' ))  MD_STATUS WHERE    CTUSCNO = USCNO    AND USCNO = MD_STATUS.MSCNO    AND MON_YEAR = ?        AND CBTOT > 0  ORDER BY USCNO";
			log.info(sql);
			return jdbcTemplate.queryForList(sql,
					new Object[] { monthYear, monthYear, monthYear, monthYear, monthYear, monthYear });
		}

	}

	public List<Map<String, Object>> getAcdBalanceReport(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		String monthYear = request.getParameter("month") + "-" + request.getParameter("year");
		if (circle.equalsIgnoreCase("ALL")) {
			String sql = "SELECT USCNO, ACDDEMAND, PAYACD, ACD_RJ, CLOSING_ACD,DECODE(TRIM(CTGOVT_PVT),'Y','GOVT','N','PVT','')CTGOVT_PVT   FROM (             \r\n"
					+ "SELECT ACD_CALC_HT.USCNO USCNO,CASE WHEN REVISED_DT IS NULL THEN NET_ACD\r\n"
					+ "                            WHEN REVISED_DT IS NOT NULL THEN  REVISED_AMT END AS ACDDEMAND,NVL(PAYACD.PACD,0) PAYACD,\r\n"
					+ "                NVL(RJACD.ACD_RJ,0) ACD_RJ,(CASE WHEN REVISED_DT IS NULL THEN NVL(NET_ACD,0)\r\n"
					+ "                            WHEN REVISED_DT IS NOT NULL THEN  NVL(REVISED_AMT,0) END)-(NVL(ACD_RJ,0)+NVL(PACD,0)) CLOSING_ACD\r\n"
					+ "                    FROM ACD_CALC_HT,\r\n"
					+ "        (SELECT USCNO,SUM(NVL(PACD,0)) PACD FROM\r\n" + "                (\r\n"
					+ "                SELECT USCNO,(NVL(PACD,0)) PACD FROM PAY_HT_HIST \r\n"
					+ "                        WHERE PAY_DATE>'05-'||TO_CHAR(ADD_MONTHS(TO_DATE(?,'MON-YYYY'),-1),'MON-YYYY')\r\n"
					+ "                    AND NVL(TRIM(PAY_STA_FLG),0) NOT IN('X','E') AND PACD<>0\r\n"
					+ "                   UNION ALL \r\n"
					+ "                SELECT USCNO,(NVL(PACD,0)) PACD FROM PAY_HT \r\n"
					+ "                        WHERE NVL(TRIM(PAY_STA_FLG),0) NOT IN('X','E') AND PACD<>0\r\n"
					+ "                ) GROUP BY USCNO) PAYACD,\r\n"
					+ "        (SELECT USCNO,SUM(NVL(ACD_RJ,0)) ACD_RJ FROM\r\n"
					+ "                 (SELECT USCNO,(CASE WHEN RJTYPE='CR' THEN 1*NVL(ACD,0)\r\n"
					+ "                                         ELSE 0 END) AS ACD_RJ\r\n"
					+ "                                \r\n"
					+ "                   FROM JOURNAL_HIST WHERE RJDT>'05-'||TO_CHAR(ADD_MONTHS(TO_DATE(?,'MON-YYYY'),-1),'MON-YYYY')\r\n"
					+ "                                            AND  TRIM(STATUS) NOT IN ('X','E')  \r\n"
					+ "                            AND TRIM(SAPRJ)!='COURT' AND NVL(ACD,0)<>0\r\n"
					+ "                   UNION ALL \r\n"
					+ "                  SELECT USCNO,(CASE WHEN RJTYPE='CR' THEN 1*NVL(ACD,0)\r\n"
					+ "                                        ELSE 0 END) AS ACD_RJ\r\n"
					+ "                                \r\n"
					+ "                   FROM JOURNAL WHERE  TRIM(STATUS) NOT IN ('X','E')  \r\n"
					+ "                            AND TRIM(SAPRJ)!='COURT'\r\n"
					+ "                            AND NVL(ACD,0)<>0  ) GROUP BY USCNO) RJACD\r\n"
					+ "        WHERE TRIM(LEVI_FLG) = 'Y' AND LEVI_MTH = ? \r\n"
					+ "            AND ACD_CALC_HT.USCNO=PAYACD.USCNO(+)\r\n"
					+ "            AND ACD_CALC_HT.USCNO=RJACD.USCNO(+)\r\n"
					+ "            AND (CASE WHEN REVISED_DT IS NULL THEN NVL(NET_ACD,0)\r\n"
					+ "                    WHEN REVISED_DT IS NOT NULL THEN  NVL(REVISED_AMT,0) END)-(NVL(ACD_RJ,0)+NVL(PACD,0))>0)\r\n"
					+ "                    ,CONS WHERE USCNO=CTUSCNO";
			log.info(sql);
			return jdbcTemplate.queryForList(sql, new Object[] { monthYear, monthYear, monthYear });
		} else {
			String sql = "SELECT USCNO, ACDDEMAND, PAYACD, ACD_RJ, CLOSING_ACD,DECODE(TRIM(CTGOVT_PVT),'Y','GOVT','N','PVT','')CTGOVT_PVT   FROM (             \r\n"
					+ "SELECT ACD_CALC_HT.USCNO USCNO,CASE WHEN REVISED_DT IS NULL THEN NET_ACD\r\n"
					+ "                            WHEN REVISED_DT IS NOT NULL THEN  REVISED_AMT END AS ACDDEMAND,NVL(PAYACD.PACD,0) PAYACD,\r\n"
					+ "                NVL(RJACD.ACD_RJ,0) ACD_RJ,(CASE WHEN REVISED_DT IS NULL THEN NVL(NET_ACD,0)\r\n"
					+ "                            WHEN REVISED_DT IS NOT NULL THEN  NVL(REVISED_AMT,0) END)-(NVL(ACD_RJ,0)+NVL(PACD,0)) CLOSING_ACD\r\n"
					+ "                    FROM ACD_CALC_HT,\r\n"
					+ "        (SELECT USCNO,SUM(NVL(PACD,0)) PACD FROM\r\n" + "                (\r\n"
					+ "                SELECT USCNO,(NVL(PACD,0)) PACD FROM PAY_HT_HIST \r\n"
					+ "                        WHERE PAY_DATE>'05-'||TO_CHAR(ADD_MONTHS(TO_DATE(?,'MON-YYYY'),-1),'MON-YYYY')\r\n"
					+ "                    AND NVL(TRIM(PAY_STA_FLG),0) NOT IN('X','E') AND PACD<>0\r\n"
					+ "                   UNION ALL \r\n"
					+ "                SELECT USCNO,(NVL(PACD,0)) PACD FROM PAY_HT \r\n"
					+ "                        WHERE NVL(TRIM(PAY_STA_FLG),0) NOT IN('X','E') AND PACD<>0\r\n"
					+ "                ) GROUP BY USCNO) PAYACD,\r\n"
					+ "        (SELECT USCNO,SUM(NVL(ACD_RJ,0)) ACD_RJ FROM\r\n"
					+ "                 (SELECT USCNO,(CASE WHEN RJTYPE='CR' THEN 1*NVL(ACD,0)\r\n"
					+ "                                         ELSE 0 END) AS ACD_RJ\r\n"
					+ "                                \r\n"
					+ "                   FROM JOURNAL_HIST WHERE RJDT>'05-'||TO_CHAR(ADD_MONTHS(TO_DATE(?,'MON-YYYY'),-1),'MON-YYYY')\r\n"
					+ "                                            AND  TRIM(STATUS) NOT IN ('X','E')  \r\n"
					+ "                            AND TRIM(SAPRJ)!='COURT' AND NVL(ACD,0)<>0\r\n"
					+ "                   UNION ALL \r\n"
					+ "                  SELECT USCNO,(CASE WHEN RJTYPE='CR' THEN 1*NVL(ACD,0)\r\n"
					+ "                                        ELSE 0 END) AS ACD_RJ\r\n"
					+ "                                \r\n"
					+ "                   FROM JOURNAL WHERE  TRIM(STATUS) NOT IN ('X','E')  \r\n"
					+ "                            AND TRIM(SAPRJ)!='COURT'\r\n"
					+ "                            AND NVL(ACD,0)<>0  ) GROUP BY USCNO) RJACD\r\n"
					+ "        WHERE TRIM(LEVI_FLG) = 'Y' AND LEVI_MTH = ? \r\n"
					+ "            AND ACD_CALC_HT.USCNO=PAYACD.USCNO(+)\r\n"
					+ "            AND ACD_CALC_HT.USCNO=RJACD.USCNO(+)\r\n"
					+ "            AND (CASE WHEN REVISED_DT IS NULL THEN NVL(NET_ACD,0)\r\n"
					+ "                    WHEN REVISED_DT IS NOT NULL THEN  NVL(REVISED_AMT,0) END)-(NVL(ACD_RJ,0)+NVL(PACD,0))>0)\r\n"
					+ "                    ,CONS WHERE USCNO=CTUSCNO AND SUBSTR(CTUSCNO,1,3)=?";
			log.info(sql);
			return jdbcTemplate.queryForList(sql, new Object[] { monthYear, monthYear, monthYear, circle });
		}
	}

	public List<Map<String, Object>> getAcdBalanceAbstract(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		String monthYear = request.getParameter("month") + "-" + request.getParameter("year");
		if (circle.equalsIgnoreCase("ALL")) {
			String sql = "SELECT SUBSTR(USCNO,1,3)CIRCLE,\r\n" + "SUM(DECODE(TRIM(CTGOVT_PVT),'Y',1,0)) GOVT_SCS,\r\n"
					+ "SUM(DECODE(TRIM(CTGOVT_PVT),'Y',NVL(ACDDEMAND,0),0))GOVT_ACDDEM,\r\n"
					+ "SUM(DECODE(TRIM(CTGOVT_PVT),'Y',NVL(PAYACD,0),0))GOVT_PAYACD,\r\n"
					+ "SUM(DECODE(TRIM(CTGOVT_PVT),'Y',NVL(ACD_RJ,0),0))GOVT_ACDRJ,\r\n"
					+ "SUM(DECODE(TRIM(CTGOVT_PVT),'Y',NVL(CLOSING_ACD,0),0))GOVT_CLOSINGACD,\r\n"
					+ "SUM(DECODE(TRIM(CTGOVT_PVT),'N',1,0)) PVT_SCS,\r\n"
					+ "SUM(DECODE(TRIM(CTGOVT_PVT),'N',NVL(ACDDEMAND,0),0))PVT_ACDDEM,\r\n"
					+ "SUM(DECODE(TRIM(CTGOVT_PVT),'N',NVL(PAYACD,0),0))PVT_PAYACD,\r\n"
					+ "SUM(DECODE(TRIM(CTGOVT_PVT),'N',NVL(ACD_RJ,0),0))PVT_ACDRJ,\r\n"
					+ "SUM(DECODE(TRIM(CTGOVT_PVT),'N',NVL(CLOSING_ACD,0),0))PVT_CLOSINGACD FROM\r\n" + "(\r\n"
					+ "SELECT ACD_CALC_HT.USCNO USCNO,\r\n" + "CASE WHEN REVISED_DT IS NULL THEN NET_ACD\r\n"
					+ "WHEN REVISED_DT IS NOT NULL THEN  REVISED_AMT END AS ACDDEMAND,NVL(PAYACD.PACD,0) PAYACD,\r\n"
					+ "NVL(RJACD.ACD_RJ,0) ACD_RJ,\r\n" + "(CASE WHEN REVISED_DT IS NULL THEN NVL(NET_ACD,0)\r\n"
					+ "WHEN REVISED_DT IS NOT NULL THEN  NVL(REVISED_AMT,0) END)-(NVL(ACD_RJ,0)+NVL(PACD,0)) CLOSING_ACD\r\n"
					+ "FROM ACD_CALC_HT,\r\n" + "(SELECT USCNO,SUM(NVL(PACD,0)) PACD FROM\r\n" + "(\r\n"
					+ "SELECT USCNO,(NVL(PACD,0)) PACD FROM PAY_HT_HIST \r\n"
					+ "WHERE PAY_DATE>'05-'||TO_CHAR(TO_DATE(?,'MON-YYYY'),'MON-YYYY')\r\n"
					+ "AND NVL(TRIM(PAY_STA_FLG),0) NOT IN('X','E') AND PACD<>0  AND NVL(ACD_ICD,0) NOT IN 'ICD'\r\n"
					+ "UNION ALL \r\n" + "SELECT USCNO,(NVL(PACD,0)) PACD FROM PAY_HT \r\n"
					+ "WHERE NVL(TRIM(PAY_STA_FLG),0) NOT IN('X','E') AND PACD<>0 AND NVL(ACD_ICD,0) NOT IN 'ICD'\r\n"
					+ ") GROUP BY USCNO\r\n" + ") PAYACD,\r\n" + "(SELECT USCNO,SUM(NVL(ACD_RJ,0)) ACD_RJ FROM\r\n"
					+ "(SELECT USCNO,(CASE WHEN RJTYPE='CR' THEN 1*NVL(ACD,0) ELSE 0 END) AS ACD_RJ \r\n"
					+ "FROM JOURNAL_HIST WHERE RJDT>'05-'||TO_CHAR(TO_DATE(?,'MON-YYYY'),'MON-YYYY')\r\n"
					+ "AND  TRIM(STATUS) NOT IN ('X','E')  \r\n" + "AND TRIM(SAPRJ)!='COURT' AND NVL(ACD,0)<>0\r\n"
					+ "UNION ALL \r\n"
					+ "SELECT USCNO,(CASE WHEN RJTYPE='CR' THEN 1*NVL(ACD,0) ELSE 0 END) AS ACD_RJ \r\n"
					+ "FROM JOURNAL WHERE  TRIM(STATUS) NOT IN ('X','E')  \r\n" + "AND TRIM(SAPRJ)!='COURT'\r\n"
					+ "AND NVL(ACD,0)<>0  ) GROUP BY USCNO\r\n" + ") RJACD\r\n"
					+ "WHERE TRIM(LEVI_FLG) = 'Y' AND LEVI_MTH = ? \r\n" + "AND ACD_CALC_HT.USCNO=PAYACD.USCNO(+)\r\n"
					+ "AND ACD_CALC_HT.USCNO=RJACD.USCNO(+)\r\n"
					+ "AND (CASE WHEN REVISED_DT IS NULL THEN NVL(NET_ACD,0)\r\n"
					+ "WHEN REVISED_DT IS NOT NULL THEN  NVL(REVISED_AMT,0) END)-(NVL(ACD_RJ,0)+NVL(PACD,0))>0\r\n"
					+ ") ,CONS WHERE CTUSCNO=USCNO\r\n" + "GROUP BY SUBSTR(USCNO,1,3) ORDER BY  SUBSTR(USCNO,1,3)";
			return jdbcTemplate.queryForList(sql, new Object[] { monthYear, monthYear, monthYear });
		} else if (circle.equalsIgnoreCase("CPDCL")) {
			String sql = "SELECT SUBSTR(USCNO,1,3)CIRCLE,\r\n" + "SUM(DECODE(TRIM(CTGOVT_PVT),'Y',1,0)) GOVT_SCS,\r\n"
					+ "SUM(DECODE(TRIM(CTGOVT_PVT),'Y',NVL(ACDDEMAND,0),0))GOVT_ACDDEM,\r\n"
					+ "SUM(DECODE(TRIM(CTGOVT_PVT),'Y',NVL(PAYACD,0),0))GOVT_PAYACD,\r\n"
					+ "SUM(DECODE(TRIM(CTGOVT_PVT),'Y',NVL(ACD_RJ,0),0))GOVT_ACDRJ,\r\n"
					+ "SUM(DECODE(TRIM(CTGOVT_PVT),'Y',NVL(CLOSING_ACD,0),0))GOVT_CLOSINGACD,\r\n"
					+ "SUM(DECODE(TRIM(CTGOVT_PVT),'N',1,0)) PVT_SCS,\r\n"
					+ "SUM(DECODE(TRIM(CTGOVT_PVT),'N',NVL(ACDDEMAND,0),0))PVT_ACDDEM,\r\n"
					+ "SUM(DECODE(TRIM(CTGOVT_PVT),'N',NVL(PAYACD,0),0))PVT_PAYACD,\r\n"
					+ "SUM(DECODE(TRIM(CTGOVT_PVT),'N',NVL(ACD_RJ,0),0))PVT_ACDRJ,\r\n"
					+ "SUM(DECODE(TRIM(CTGOVT_PVT),'N',NVL(CLOSING_ACD,0),0))PVT_CLOSINGACD FROM\r\n" + "(\r\n"
					+ "SELECT ACD_CALC_HT.USCNO USCNO,\r\n" + "CASE WHEN REVISED_DT IS NULL THEN NET_ACD\r\n"
					+ "WHEN REVISED_DT IS NOT NULL THEN  REVISED_AMT END AS ACDDEMAND,NVL(PAYACD.PACD,0) PAYACD,\r\n"
					+ "NVL(RJACD.ACD_RJ,0) ACD_RJ,\r\n" + "(CASE WHEN REVISED_DT IS NULL THEN NVL(NET_ACD,0)\r\n"
					+ "WHEN REVISED_DT IS NOT NULL THEN  NVL(REVISED_AMT,0) END)-(NVL(ACD_RJ,0)+NVL(PACD,0)) CLOSING_ACD\r\n"
					+ "FROM ACD_CALC_HT,\r\n" + "(SELECT USCNO,SUM(NVL(PACD,0)) PACD FROM\r\n" + "(\r\n"
					+ "SELECT USCNO,(NVL(PACD,0)) PACD FROM PAY_HT_HIST \r\n"
					+ "WHERE PAY_DATE>'05-'||TO_CHAR(TO_DATE(?,'MON-YYYY'),'MON-YYYY')\r\n"
					+ "AND NVL(TRIM(PAY_STA_FLG),0) NOT IN('X','E') AND PACD<>0  AND NVL(ACD_ICD,0) NOT IN 'ICD'\r\n"
					+ "UNION ALL \r\n" + "SELECT USCNO,(NVL(PACD,0)) PACD FROM PAY_HT \r\n"
					+ "WHERE NVL(TRIM(PAY_STA_FLG),0) NOT IN('X','E') AND PACD<>0 AND NVL(ACD_ICD,0) NOT IN 'ICD'\r\n"
					+ ") GROUP BY USCNO\r\n" + ") PAYACD,\r\n" + "(SELECT USCNO,SUM(NVL(ACD_RJ,0)) ACD_RJ FROM\r\n"
					+ "(SELECT USCNO,(CASE WHEN RJTYPE='CR' THEN 1*NVL(ACD,0) ELSE 0 END) AS ACD_RJ \r\n"
					+ "FROM JOURNAL_HIST WHERE RJDT>'05-'||TO_CHAR(TO_DATE(?,'MON-YYYY'),'MON-YYYY')\r\n"
					+ "AND  TRIM(STATUS) NOT IN ('X','E')  \r\n" + "AND TRIM(SAPRJ)!='COURT' AND NVL(ACD,0)<>0\r\n"
					+ "UNION ALL \r\n"
					+ "SELECT USCNO,(CASE WHEN RJTYPE='CR' THEN 1*NVL(ACD,0) ELSE 0 END) AS ACD_RJ \r\n"
					+ "FROM JOURNAL WHERE  TRIM(STATUS) NOT IN ('X','E')  \r\n" + "AND TRIM(SAPRJ)!='COURT'\r\n"
					+ "AND NVL(ACD,0)<>0  ) GROUP BY USCNO\r\n" + ") RJACD\r\n"
					+ "WHERE TRIM(LEVI_FLG) = 'Y' AND LEVI_MTH = ? \r\n" + "AND ACD_CALC_HT.USCNO=PAYACD.USCNO(+)\r\n"
					+ "AND ACD_CALC_HT.USCNO=RJACD.USCNO(+)\r\n"
					+ "AND (CASE WHEN REVISED_DT IS NULL THEN NVL(NET_ACD,0)\r\n"
					+ "WHEN REVISED_DT IS NOT NULL THEN  NVL(REVISED_AMT,0) END)-(NVL(ACD_RJ,0)+NVL(PACD,0))>0\r\n"
					+ ") ,CONS WHERE CTUSCNO=USCNO AND SUBSTR(USCNO,1,3) IN('ATP','CDP','NLR','TPT','KNL') \r\n"
					+ "GROUP BY SUBSTR(USCNO,1,3) ORDER BY  SUBSTR(USCNO,1,3)";
			log.info(sql);
			return jdbcTemplate.queryForList(sql, new Object[] { monthYear, monthYear, monthYear });
		} else {
			String sql = "SELECT SUBSTR(USCNO,1,3)CIRCLE,\r\n" + "SUM(DECODE(TRIM(CTGOVT_PVT),'Y',1,0)) GOVT_SCS,\r\n"
					+ "SUM(DECODE(TRIM(CTGOVT_PVT),'Y',NVL(ACDDEMAND,0),0))GOVT_ACDDEM,\r\n"
					+ "SUM(DECODE(TRIM(CTGOVT_PVT),'Y',NVL(PAYACD,0),0))GOVT_PAYACD,\r\n"
					+ "SUM(DECODE(TRIM(CTGOVT_PVT),'Y',NVL(ACD_RJ,0),0))GOVT_ACDRJ,\r\n"
					+ "SUM(DECODE(TRIM(CTGOVT_PVT),'Y',NVL(CLOSING_ACD,0),0))GOVT_CLOSINGACD,\r\n"
					+ "SUM(DECODE(TRIM(CTGOVT_PVT),'N',1,0)) PVT_SCS,\r\n"
					+ "SUM(DECODE(TRIM(CTGOVT_PVT),'N',NVL(ACDDEMAND,0),0))PVT_ACDDEM,\r\n"
					+ "SUM(DECODE(TRIM(CTGOVT_PVT),'N',NVL(PAYACD,0),0))PVT_PAYACD,\r\n"
					+ "SUM(DECODE(TRIM(CTGOVT_PVT),'N',NVL(ACD_RJ,0),0))PVT_ACDRJ,\r\n"
					+ "SUM(DECODE(TRIM(CTGOVT_PVT),'N',NVL(CLOSING_ACD,0),0))PVT_CLOSINGACD FROM\r\n" + "(\r\n"
					+ "SELECT ACD_CALC_HT.USCNO USCNO,\r\n" + "CASE WHEN REVISED_DT IS NULL THEN NET_ACD\r\n"
					+ "WHEN REVISED_DT IS NOT NULL THEN  REVISED_AMT END AS ACDDEMAND,NVL(PAYACD.PACD,0) PAYACD,\r\n"
					+ "NVL(RJACD.ACD_RJ,0) ACD_RJ,\r\n" + "(CASE WHEN REVISED_DT IS NULL THEN NVL(NET_ACD,0)\r\n"
					+ "WHEN REVISED_DT IS NOT NULL THEN  NVL(REVISED_AMT,0) END)-(NVL(ACD_RJ,0)+NVL(PACD,0)) CLOSING_ACD\r\n"
					+ "FROM ACD_CALC_HT,\r\n" + "(SELECT USCNO,SUM(NVL(PACD,0)) PACD FROM\r\n" + "(\r\n"
					+ "SELECT USCNO,(NVL(PACD,0)) PACD FROM PAY_HT_HIST \r\n"
					+ "WHERE PAY_DATE>'05-'||TO_CHAR(TO_DATE(?,'MON-YYYY'),'MON-YYYY')\r\n"
					+ "AND NVL(TRIM(PAY_STA_FLG),0) NOT IN('X','E') AND PACD<>0  AND NVL(ACD_ICD,0) NOT IN 'ICD'\r\n"
					+ "UNION ALL \r\n" + "SELECT USCNO,(NVL(PACD,0)) PACD FROM PAY_HT \r\n"
					+ "WHERE NVL(TRIM(PAY_STA_FLG),0) NOT IN('X','E') AND PACD<>0 AND NVL(ACD_ICD,0) NOT IN 'ICD'\r\n"
					+ ") GROUP BY USCNO\r\n" + ") PAYACD,\r\n" + "(SELECT USCNO,SUM(NVL(ACD_RJ,0)) ACD_RJ FROM\r\n"
					+ "(SELECT USCNO,(CASE WHEN RJTYPE='CR' THEN 1*NVL(ACD,0) ELSE 0 END) AS ACD_RJ \r\n"
					+ "FROM JOURNAL_HIST WHERE RJDT>'05-'||TO_CHAR(TO_DATE(?,'MON-YYYY'),'MON-YYYY')\r\n"
					+ "AND  TRIM(STATUS) NOT IN ('X','E')  \r\n" + "AND TRIM(SAPRJ)!='COURT' AND NVL(ACD,0)<>0\r\n"
					+ "UNION ALL \r\n"
					+ "SELECT USCNO,(CASE WHEN RJTYPE='CR' THEN 1*NVL(ACD,0) ELSE 0 END) AS ACD_RJ \r\n"
					+ "FROM JOURNAL WHERE  TRIM(STATUS) NOT IN ('X','E')  \r\n" + "AND TRIM(SAPRJ)!='COURT'\r\n"
					+ "AND NVL(ACD,0)<>0  ) GROUP BY USCNO\r\n" + ") RJACD\r\n"
					+ "WHERE TRIM(LEVI_FLG) = 'Y' AND LEVI_MTH = ? \r\n" + "AND ACD_CALC_HT.USCNO=PAYACD.USCNO(+)\r\n"
					+ "AND ACD_CALC_HT.USCNO=RJACD.USCNO(+)\r\n"
					+ "AND (CASE WHEN REVISED_DT IS NULL THEN NVL(NET_ACD,0)\r\n"
					+ "WHEN REVISED_DT IS NOT NULL THEN  NVL(REVISED_AMT,0) END)-(NVL(ACD_RJ,0)+NVL(PACD,0))>0\r\n"
					+ ") ,CONS WHERE CTUSCNO=USCNO AND SUBSTR(USCNO,1,3) NOT IN('ATP','CDP','NLR','TPT','KNL') \r\n"
					+ "GROUP BY SUBSTR(USCNO,1,3) ORDER BY  SUBSTR(USCNO,1,3)";
			return jdbcTemplate.queryForList(sql, new Object[] { monthYear, monthYear, monthYear });
		}

	}

	public List<Map<String, Object>> getCMDCA(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		String monthYear = request.getParameter("month") + "-" + request.getParameter("year");

		if (!circle.equalsIgnoreCase("ALL")) {

			String sql = "select CIRCLE CIRNAME,DIVISION DIVNAME,ERO ERONAME,SUBDIVISION SUBNAME,SECTION SECNAME,CAT,SCAT,DECODE(TRIM(L.STATUS),'1',DECODE(TRIM(M.STAT_HT),'3','UDC','LIVE'),'BSTOP') STATUS,COUNT(*) NOS,\r\n"
					+ "SUM(NVL(TOT_OB,0)) OB_OTHERTHAN_COURT,SUM(NVL(OB_OTH,0) +NVL(OB_CCLPC,0)) OB_COURT,SUM(NVL(OB_OTH,0) +NVL(OB_CCLPC,0)+NVL(TOT_OB,0)) TOT_OB,SUM(NVL(CMD,0)) DEMAND_WITHOUT_COURT,\r\n"
					+ "SUM(NVL(CCLPC,0)) COURT_LPC ,SUM(NVL(DRJ,0)) DR_RJ,SUM(NVL(CRJ,0)) CR_RJ,SUM(NVL(RJ_CCLPC,0)+NVL(RJ_OTH,0)) COURT_RJ,SUM(NVL(TOT_PAY,0)) COLLECTION,\r\n"
					+ "SUM(NVL(CBTOT,0)) CB_OTHERTHAN_COURT,SUM(NVL(CB_CCLPC,0)+NVL(CB_OTH,0)) CB_COURT,\r\n"
					+ "SUM(NVL(CBTOT,0)+NVL(CB_CCLPC,0)+NVL(CB_OTH,0)) TOTAL_CB\r\n" + "---SUM(NVL(CB_SD,0)) CB_SD \r\n"
					+ "from cons,ledger_ht_hist L,(SELECT MSCNO,MIN(MDCLKWHSTAT_HT) STAT_HT FROM MTRDAT_HIST WHERE to_char(MDCLRDG_DT,'MON-YYYY')=? GROUP BY MSCNO) M \r\n"
					+ "where  USCNO=CTUSCNO   AND CTUSCNO=M.MSCNO(+) and SUBSTR(CTUSCNO,1,3) IN('GNT','VJA','ONG','CRD')\r\n"
					+ "and MON_YEAR = ? AND SUBSTR(CTUSCNO,1,3)=? GROUP BY CIRCLE,DIVISION,ERO,SUBDIVISION,SECTION,CAT,SCAT,DECODE(TRIM(L.STATUS),'1',DECODE(TRIM(M.STAT_HT),'3','UDC','LIVE'),'BSTOP') ORDER BY CIRNAME,DIVNAME,ERO,SUBNAME,SECNAME,CAT,SCAT,STATUS";
			log.info(sql);
			return jdbcTemplate.queryForList(sql, new Object[] { monthYear, monthYear, circle });
		} else {
			String sql = "select CIRCLE CIRNAME,DIVISION DIVNAME,ERO ERONAME,SUBDIVISION SUBNAME,SECTION SECNAME,CAT,SCAT,DECODE(TRIM(L.STATUS),'1',DECODE(TRIM(M.STAT_HT),'3','UDC','LIVE'),'BSTOP') STATUS,COUNT(*) NOS,\r\n"
					+ "SUM(NVL(TOT_OB,0)) OB_OTHERTHAN_COURT,SUM(NVL(OB_OTH,0) +NVL(OB_CCLPC,0)) OB_COURT,SUM(NVL(OB_OTH,0) +NVL(OB_CCLPC,0)+NVL(TOT_OB,0)) TOT_OB,SUM(NVL(CMD,0)) DEMAND_WITHOUT_COURT,\r\n"
					+ "SUM(NVL(CCLPC,0)) COURT_LPC ,SUM(NVL(DRJ,0)) DR_RJ,SUM(NVL(CRJ,0)) CR_RJ,SUM(NVL(RJ_CCLPC,0)+NVL(RJ_OTH,0)) COURT_RJ,SUM(NVL(TOT_PAY,0)) COLLECTION,\r\n"
					+ "SUM(NVL(CBTOT,0)) CB_OTHERTHAN_COURT,SUM(NVL(CB_CCLPC,0)+NVL(CB_OTH,0)) CB_COURT,\r\n"
					+ "SUM(NVL(CBTOT,0)+NVL(CB_CCLPC,0)+NVL(CB_OTH,0)) TOTAL_CB\r\n" + "---SUM(NVL(CB_SD,0)) CB_SD \r\n"
					+ "from cons,ledger_ht_hist L,(SELECT MSCNO,MIN(MDCLKWHSTAT_HT) STAT_HT FROM MTRDAT_HIST WHERE to_char(MDCLRDG_DT,'MON-YYYY')=? GROUP BY MSCNO) M \r\n"
					+ "where  USCNO=CTUSCNO   AND CTUSCNO=M.MSCNO(+) and SUBSTR(CTUSCNO,1,3) IN('GNT','VJA','ONG','CRD')\r\n"
					+ "and MON_YEAR = ? GROUP BY CIRCLE,DIVISION,ERO,SUBDIVISION,SECTION,CAT,SCAT,DECODE(TRIM(L.STATUS),'1',DECODE(TRIM(M.STAT_HT),'3','UDC','LIVE'),'BSTOP') ORDER BY CIRNAME,DIVNAME,ERO,SUBNAME,SECNAME,CAT,SCAT,STATUS";
			log.info(sql);
			return jdbcTemplate.queryForList(sql, new Object[] { monthYear, monthYear });
		}
	}

	public List<Map<String, Object>> getDCBAbstract(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		String monthYear = request.getParameter("month") + "-" + request.getParameter("year");

		if (!circle.equalsIgnoreCase("ALL")) {

			String sql = "select CIRCLE CIRNAME,DIVISION DIVNAME,ERO ERONAME,SUBDIVISION SUBNAME,SECTION SECNAME,CAT,SCAT,DECODE(TRIM(L.STATUS),'1',DECODE(TRIM(M.STAT_HT),'3','UDC','LIVE'),'BSTOP') STATUS,COUNT(*) NOS,\r\n"
					+ "SUM(NVL(MN_KVAH,0)) UNITS,SUM(NVL(OB_OTH,0) +NVL(OB_CCLPC,0)+NVL(TOT_OB,0)) TOT_OB,SUM(NVL(CMD,0)+NVL(CCLPC,0)) DEMAND_NET,SUM(NVL(CMD,0)+NVL(CCLPC,0)-NVL(B.AQUA,0)) DEMAND_GROSS,\r\n"
					+ "sum(nvl(DR_TDA,0)) DR_TDA,sum(nvl(DR_DRC,0)) DR_DRC,sum(nvl(DR_OTH,0)) DR_OTH,\r\n"
					+ "SUM(CASE WHEN (NVL(OB_OTH,0) +NVL(OB_CCLPC,0)+NVL(TOT_OB,0))>0 THEN CASE WHEN (NVL(OB_OTH,0) +NVL(OB_CCLPC,0)+NVL(TOT_OB,0))>NVL(TOT_PAY,0) THEN NVL(TOT_PAY,0) ELSE (NVL(OB_OTH,0) +NVL(OB_CCLPC,0)+NVL(TOT_OB,0)) END END) ARREARS_COLL,\r\n"
					+ "SUM(CASE WHEN (NVL(OB_OTH,0) +NVL(OB_CCLPC,0)+NVL(TOT_OB,0))>0 THEN CASE WHEN (NVL(OB_OTH,0) +NVL(OB_CCLPC,0)+NVL(TOT_OB,0))<NVL(TOT_PAY,0) THEN NVL(TOT_PAY,0)-(NVL(OB_OTH,0) +NVL(OB_CCLPC,0)+NVL(TOT_OB,0)) END  ELSE NVL(TOT_PAY,0) END)  CURR_MON_DEM_COLL,\r\n"
					+ "sum(nvl(CR_TCA,0)) CR_TCA,sum(nvl(CR_DWC,0)) CR_DWC,sum(nvl(CR_DWC_SCS,0)) CR_DWC_SCS,sum(nvl(CR_OTH,0)) CR_OTH, \r\n"
					+ "SUM(NVL(CBTOT,0)+NVL(CB_CCLPC,0)+NVL(CB_OTH,0)) TOTAL_CB\r\n" + "from cons,ledger_ht_hist L,\r\n"
					+ "(SELECT BTSCNO,NVL(BTAQUASUB_CHG,0) AQUA FROM BILL_HIST WHERE to_char(BTBLDT,'MON-YYYY')= ?) B\r\n"
					+ ",(select unique uscno\r\n"
					+ ",sum(case when SAPRJ like'%TDA%' and rjtype like'%DR%' then nvl(TOTRJ_AMT,0) end) DR_TDA\r\n"
					+ ",sum(case when SAPRJ like'%DRC%' and rjtype like'%DR%' then nvl(TOTRJ_AMT,0) end) DR_DRC\r\n"
					+ ",sum(case when SAPRJ not like'%TDA%' and SAPRJ not like'%DRC%' and rjtype like'%DR%' then nvl(TOTRJ_AMT,0) end) DR_OTH\r\n"
					+ ",sum(case when SAPRJ like'%TCA%' and rjtype like'%CR%' then nvl(TOTRJ_AMT,0) end) CR_TCA\r\n"
					+ ",sum(case when SAPRJ like'%DWC%' and rjtype like'%CR%' then nvl(TOTRJ_AMT,0) end) CR_DWC\r\n"
					+ ",sum(case when SAPRJ like'%DWC%' and rjtype like'%CR%' then 1 end) CR_DWC_SCS\r\n"
					+ ",sum(case when SAPRJ not like'%TCA%' and SAPRJ not like'%DWC%' and rjtype like'%CR%' then nvl(TOTRJ_AMT,0) end) CR_OTH\r\n"
					+ "from \r\n"
					+ "(select uscno,rjtype,SAPRJ,nvl(ENGCHG,0)+nvl(THEFT,0)+nvl(OTHCHG,0)+nvl(CUSTCHG,0)+nvl(DEMCHG,0)+nvl(ED,0)+nvl(LPC,0)+nvl(EDI,0)+nvl(FSA,0)+NVL(CC_LPC,0)+NVL(CC_OTH,0) TOTRJ_amt from JOURNAL_HIST WHERE to_char(rjdt,'MON-YYYY')=? and SUBSTR(USCNO,1,3) IN('GNT','VJA','ONG','CRD') AND TRIM(STATUS) NOT IN ('X')) R GROUP BY USCNO) RJ,\r\n"
					+ "(SELECT MSCNO,MIN(MDCLKWHSTAT_HT) STAT_HT FROM MTRDAT_HIST WHERE to_char(MDCLRDG_DT,'MON-YYYY')=? GROUP BY MSCNO) M \r\n"
					+ "where  L.USCNO=CTUSCNO AND L.USCNO=B.BTSCNO(+) AND   L.USCNO=RJ.USCNO(+) AND CTUSCNO=M.MSCNO(+) and SUBSTR(CTUSCNO,1,3) IN('GNT','VJA','ONG','CRD')\r\n"
					+ "and MON_YEAR = ? AND SUBSTR(CTUSCNO,1,3)=? GROUP BY CIRCLE,DIVISION,ERO,SUBDIVISION,SECTION,CAT,SCAT,DECODE(TRIM(L.STATUS),'1',DECODE(TRIM(M.STAT_HT),'3','UDC','LIVE'),'BSTOP') ORDER BY CIRNAME,DIVNAME,ERO,SUBNAME,SECNAME,CAT,SCAT";
			log.info(sql);
			return jdbcTemplate.queryForList(sql, new Object[] { monthYear, monthYear, monthYear, monthYear, circle });
		} else {
			String sql = "select CIRCLE CIRNAME,DIVISION DIVNAME,ERO ERONAME,SUBDIVISION SUBNAME,SECTION SECNAME,CAT,SCAT,DECODE(TRIM(L.STATUS),'1',DECODE(TRIM(M.STAT_HT),'3','UDC','LIVE'),'BSTOP') STATUS,COUNT(*) NOS,\r\n"
					+ "SUM(NVL(MN_KVAH,0)) UNITS,SUM(NVL(OB_OTH,0) +NVL(OB_CCLPC,0)+NVL(TOT_OB,0)) TOT_OB,SUM(NVL(CMD,0)+NVL(CCLPC,0)) DEMAND_NET,SUM(NVL(CMD,0)+NVL(CCLPC,0)-NVL(B.AQUA,0)) DEMAND_GROSS,\r\n"
					+ "sum(nvl(DR_TDA,0)) DR_TDA,sum(nvl(DR_DRC,0)) DR_DRC,sum(nvl(DR_OTH,0)) DR_OTH,\r\n"
					+ "SUM(CASE WHEN (NVL(OB_OTH,0) +NVL(OB_CCLPC,0)+NVL(TOT_OB,0))>0 THEN CASE WHEN (NVL(OB_OTH,0) +NVL(OB_CCLPC,0)+NVL(TOT_OB,0))>NVL(TOT_PAY,0) THEN NVL(TOT_PAY,0) ELSE (NVL(OB_OTH,0) +NVL(OB_CCLPC,0)+NVL(TOT_OB,0)) END END) ARREARS_COLL,\r\n"
					+ "SUM(CASE WHEN (NVL(OB_OTH,0) +NVL(OB_CCLPC,0)+NVL(TOT_OB,0))>0 THEN CASE WHEN (NVL(OB_OTH,0) +NVL(OB_CCLPC,0)+NVL(TOT_OB,0))<NVL(TOT_PAY,0) THEN NVL(TOT_PAY,0)-(NVL(OB_OTH,0) +NVL(OB_CCLPC,0)+NVL(TOT_OB,0)) END  ELSE NVL(TOT_PAY,0) END)  CURR_MON_DEM_COLL,\r\n"
					+ "sum(nvl(CR_TCA,0)) CR_TCA,sum(nvl(CR_DWC,0)) CR_DWC,sum(nvl(CR_DWC_SCS,0)) CR_DWC_SCS,sum(nvl(CR_OTH,0)) CR_OTH, \r\n"
					+ "SUM(NVL(CBTOT,0)+NVL(CB_CCLPC,0)+NVL(CB_OTH,0)) TOTAL_CB\r\n" + "from cons,ledger_ht_hist L,\r\n"
					+ "(SELECT BTSCNO,NVL(BTAQUASUB_CHG,0) AQUA FROM BILL_HIST WHERE to_char(BTBLDT,'MON-YYYY')= ?) B\r\n"
					+ ",(select unique uscno\r\n"
					+ ",sum(case when SAPRJ like'%TDA%' and rjtype like'%DR%' then nvl(TOTRJ_AMT,0) end) DR_TDA\r\n"
					+ ",sum(case when SAPRJ like'%DRC%' and rjtype like'%DR%' then nvl(TOTRJ_AMT,0) end) DR_DRC\r\n"
					+ ",sum(case when SAPRJ not like'%TDA%' and SAPRJ not like'%DRC%' and rjtype like'%DR%' then nvl(TOTRJ_AMT,0) end) DR_OTH\r\n"
					+ ",sum(case when SAPRJ like'%TCA%' and rjtype like'%CR%' then nvl(TOTRJ_AMT,0) end) CR_TCA\r\n"
					+ ",sum(case when SAPRJ like'%DWC%' and rjtype like'%CR%' then nvl(TOTRJ_AMT,0) end) CR_DWC\r\n"
					+ ",sum(case when SAPRJ like'%DWC%' and rjtype like'%CR%' then 1 end) CR_DWC_SCS\r\n"
					+ ",sum(case when SAPRJ not like'%TCA%' and SAPRJ not like'%DWC%' and rjtype like'%CR%' then nvl(TOTRJ_AMT,0) end) CR_OTH\r\n"
					+ "from \r\n"
					+ "(select uscno,rjtype,SAPRJ,nvl(ENGCHG,0)+nvl(THEFT,0)+nvl(OTHCHG,0)+nvl(CUSTCHG,0)+nvl(DEMCHG,0)+nvl(ED,0)+nvl(LPC,0)+nvl(EDI,0)+nvl(FSA,0)+NVL(CC_LPC,0)+NVL(CC_OTH,0) TOTRJ_amt from JOURNAL_HIST WHERE to_char(rjdt,'MON-YYYY')=? and SUBSTR(USCNO,1,3) IN('GNT','VJA','ONG','CRD') AND TRIM(STATUS) NOT IN ('X')) R GROUP BY USCNO) RJ,\r\n"
					+ "(SELECT MSCNO,MIN(MDCLKWHSTAT_HT) STAT_HT FROM MTRDAT_HIST WHERE to_char(MDCLRDG_DT,'MON-YYYY')=? GROUP BY MSCNO) M \r\n"
					+ "where  L.USCNO=CTUSCNO AND L.USCNO=B.BTSCNO(+) AND   L.USCNO=RJ.USCNO(+) AND CTUSCNO=M.MSCNO(+) and SUBSTR(CTUSCNO,1,3) IN('GNT','VJA','ONG','CRD')\r\n"
					+ "and MON_YEAR = ?  GROUP BY CIRCLE,DIVISION,ERO,SUBDIVISION,SECTION,CAT,SCAT,DECODE(TRIM(L.STATUS),'1',DECODE(TRIM(M.STAT_HT),'3','UDC','LIVE'),'BSTOP') ORDER BY CIRNAME,DIVNAME,ERO,SUBNAME,SECNAME,CAT,SCAT";

			log.info(sql);
			return jdbcTemplate.queryForList(sql, new Object[] { monthYear, monthYear, monthYear, monthYear });
		}
	}

	public List<Map<String, Object>> getLGDCodes(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		String monthYear = request.getParameter("month") + "-" + request.getParameter("year");

		if (!circle.equalsIgnoreCase("ALL")) {

			String sql = "SELECT CASE\r\n" + "    WHEN SUBSTR(SCNO,1,3)='VJA' THEN  'KRISHNA'\r\n"
					+ "    WHEN SUBSTR(SCNO,1,3)='GNT' THEN 'GUNTUR'\r\n"
					+ "    WHEN SUBSTR(SCNO,1,3)='ONG' THEN 'ONGOLE'\r\n"
					+ "    WHEN SUBSTR(SCNO,1,3)='CRD' THEN 'CRDA' END DISTRICT,GPNAME NAME,GPTYPE NAME_TYPE,LGDCODE,DDOCODE,CMFSCODE,MON_YEAR MONTH,ROUND(SUM(NVL(CBTOT,0)+NVL(CB_CCLPC,0)+NVL(CB_OTH,0))) CB FROM HT_LGDCODES ,DDOMASTER,LEDGER_HT_HIST  \r\n"
					+ "    WHERE MON_YEAR=? AND SCNO=USCNO AND GPDDO=DDOCODE AND SUBSTR(SCNO,1,3)=? GROUP BY CASE\r\n"
					+ "    WHEN SUBSTR(SCNO,1,3)='VJA' THEN  'KRISHNA'\r\n"
					+ "    WHEN SUBSTR(SCNO,1,3)='GNT' THEN 'GUNTUR'\r\n"
					+ "    WHEN SUBSTR(SCNO,1,3)='ONG' THEN 'ONGOLE'\r\n"
					+ "    WHEN SUBSTR(SCNO,1,3)='CRD' THEN 'CRDA' END,GPNAME,GPTYPE,LGDCODE,DDOCODE,CMFSCODE,MON_YEAR ORDER BY DISTRICT,LGDCODE";
			log.info(sql);
			return jdbcTemplate.queryForList(sql, new Object[] { monthYear, circle });
		} else {
			String sql = "SELECT CASE\r\n" + "    WHEN SUBSTR(SCNO,1,3)='VJA' THEN  'KRISHNA'\r\n"
					+ "    WHEN SUBSTR(SCNO,1,3)='GNT' THEN 'GUNTUR'\r\n"
					+ "    WHEN SUBSTR(SCNO,1,3)='ONG' THEN 'ONGOLE'\r\n"
					+ "    WHEN SUBSTR(SCNO,1,3)='CRD' THEN 'CRDA' END DISTRICT,GPNAME NAME,GPTYPE NAME_TYPE,LGDCODE,DDOCODE,CMFSCODE,MON_YEAR MONTH,ROUND(SUM(NVL(CBTOT,0)+NVL(CB_CCLPC,0)+NVL(CB_OTH,0))) CB FROM HT_LGDCODES ,DDOMASTER,LEDGER_HT_HIST  \r\n"
					+ "    WHERE MON_YEAR=? AND SCNO=USCNO AND GPDDO=DDOCODE GROUP BY CASE\r\n"
					+ "    WHEN SUBSTR(SCNO,1,3)='VJA' THEN  'KRISHNA'\r\n"
					+ "    WHEN SUBSTR(SCNO,1,3)='GNT' THEN 'GUNTUR'\r\n"
					+ "    WHEN SUBSTR(SCNO,1,3)='ONG' THEN 'ONGOLE'\r\n"
					+ "    WHEN SUBSTR(SCNO,1,3)='CRD' THEN 'CRDA' END,GPNAME,GPTYPE,LGDCODE,DDOCODE,CMFSCODE,MON_YEAR ORDER BY DISTRICT,LGDCODE";

			log.info(sql);
			return jdbcTemplate.queryForList(sql, new Object[] { monthYear });
		}
	}

	public List<Map<String, Object>> getLGDServiceWiseCodes(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		String monthYear = request.getParameter("month") + "-" + request.getParameter("year");

		if (!circle.equalsIgnoreCase("ALL")) {

			String sql = "SELECT USCNO,CASE\r\n" + "    WHEN SUBSTR(SCNO,1,3)='VJA' THEN  'KRISHNA'\r\n"
					+ "    WHEN SUBSTR(SCNO,1,3)='GNT' THEN 'GUNTUR'\r\n"
					+ "    WHEN SUBSTR(SCNO,1,3)='ONG' THEN 'ONGOLE'\r\n"
					+ "    WHEN SUBSTR(SCNO,1,3)='CRD' THEN 'CRDA' END DISTRICT,GPNAME NAME,GPTYPE NAME_TYPE,LGDCODE,DDOCODE,CMFSCODE,MON_YEAR MONTH,ROUND(NVL(CBTOT,0)+NVL(CB_CCLPC,0)+NVL(CB_OTH,0)) CB FROM HT_LGDCODES ,DDOMASTER,LEDGER_HT_HIST  \r\n"
					+ "    WHERE MON_YEAR=? AND SCNO=USCNO AND GPDDO=DDOCODE AND SUBSTR(SCNO,1,3)=? ORDER BY DISTRICT,LGDCODE";
			log.info(sql);
			return jdbcTemplate.queryForList(sql, new Object[] { monthYear, circle });
		} else {
			String sql = "SELECT USCNO,CASE\r\n" + "    WHEN SUBSTR(SCNO,1,3)='VJA' THEN  'KRISHNA'\r\n"
					+ "    WHEN SUBSTR(SCNO,1,3)='GNT' THEN 'GUNTUR'\r\n"
					+ "    WHEN SUBSTR(SCNO,1,3)='ONG' THEN 'ONGOLE'\r\n"
					+ "    WHEN SUBSTR(SCNO,1,3)='CRD' THEN 'CRDA' END DISTRICT,GPNAME NAME,GPTYPE NAME_TYPE,LGDCODE,DDOCODE,CMFSCODE,MON_YEAR MONTH,ROUND(NVL(CBTOT,0)+NVL(CB_CCLPC,0)+NVL(CB_OTH,0)) CB FROM HT_LGDCODES ,DDOMASTER,LEDGER_HT_HIST  \r\n"
					+ "    WHERE MON_YEAR=? AND SCNO=USCNO AND GPDDO=DDOCODE ORDER BY DISTRICT,LGDCODE";

			log.info(sql);
			return jdbcTemplate.queryForList(sql, new Object[] { monthYear });
		}
	}
	/*
	 * public List<Map<String, Object>> getHODReport(HttpServletRequest request) {
	 * String circle = request.getParameter("circle"); String monthYear =
	 * request.getParameter("month") + "-" + request.getParameter("year"); String
	 * deptcode = request.getParameter("hoddept").equals("ALL")?"" :
	 * "AND C.GMDEP='"+request.getParameter("hoddept")+"'";
	 * 
	 * if(!circle.equalsIgnoreCase("ALL")) {
	 * 
	 * String
	 * sql="Select D.CIRNAME CIRCLE,D.divname DIVISION,D.subname SUB_DIVISION,D.secname SECTION\r\n"
	 * + ",A.Uscno,B.CtName,C.gmdeptname DEPT_NAME,C.GMSUBDEPTNAME Hod_Name\r\n" +
	 * ",B.ctcat CAT,B.CTSUBCAT,CTHODTYPE HOD_TYPE,Decode(Status,'1','LIVE','0','BILL-STOP','') Status,\r\n"
	 * + "Round(Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)) Ob,\r\n" +
	 * "(Mn_Kvah) Sales\r\n" +
	 * ",round(Nvl(Cmd,0)+Nvl(Cclpc,0)) Demand,round(Nvl(Tot_Pay,0)) Collection,round(Nvl(Rj_Oth,0)+Nvl(Drj,0)+Nvl(Rj_Cclpc,0)) Drj,round(Nvl(Crj,0)) Crj,round(Nvl(Cbtot,0)+Nvl(Cb_Oth,0)+Nvl(Cb_Cclpc,0)) Cb\r\n"
	 * +
	 * "From (select LHH.*,'' STATUS_NEW, '' GOVT_PVT from Ledger_Ht_HIST LHH where Mon_Year=?  union all select * from accountcopy where Mon_Year=? ) A,CONS B,deptmast C,spdclmaster D\r\n"
	 * +
	 * "Where A.Uscno=B.CTUscno And B.CTHODDEP=C.gmdeptcode and  B.CTHODSUBDEP=C.GMSUBDEPTCODE \r\n"
	 * + "And substr(trim(B.ctseccd),-5)=D.seccd\r\n" +
	 * "AND SUBSTR(A.USCNO,1,3)=? \r\n" + deptcode +
	 * " Order By D.CIRNAME ,D.divname ,D.subname ,D.secname,A.Uscno";
	 * log.info(sql); return jdbcTemplate.queryForList(sql,new Object[]
	 * {monthYear,monthYear,circle}); }else { String
	 * sql="Select D.CIRNAME CIRCLE,D.divname DIVISION,D.subname SUB_DIVISION,D.secname SECTION\r\n"
	 * + ",A.Uscno,B.CtName,C.gmdeptname DEPT_NAME,C.GMSUBDEPTNAME Hod_Name\r\n" +
	 * ",B.ctcat CAT,B.CTSUBCAT,CTHODTYPE HOD_TYPE,Decode(Status,'1','LIVE','0','BILL-STOP','') Status,\r\n"
	 * + "Round(Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)) Ob,\r\n" +
	 * "(Mn_Kvah) Sales\r\n" +
	 * ",round(Nvl(Cmd,0)+Nvl(Cclpc,0)) Demand,round(Nvl(Tot_Pay,0)) Collection,round(Nvl(Rj_Oth,0)+Nvl(Drj,0)+Nvl(Rj_Cclpc,0)) Drj,round(Nvl(Crj,0)) Crj,round(Nvl(Cbtot,0)+Nvl(Cb_Oth,0)+Nvl(Cb_Cclpc,0)) Cb\r\n"
	 * +
	 * "From (select LHH.*,'' STATUS_NEW, '' GOVT_PVT from Ledger_Ht_HIST LHH where Mon_Year=?  union all select * from accountcopy where Mon_Year=? ) A,CONS B,deptmast C,spdclmaster D\r\n"
	 * +
	 * "Where A.Uscno=B.CTUscno And B.CTHODDEP=C.gmdeptcode and  B.CTHODSUBDEP=C.GMSUBDEPTCODE \r\n"
	 * + "And substr(trim(B.ctseccd),-5)=D.seccd\r\n" + deptcode +
	 * " Order By D.CIRNAME ,D.divname ,D.subname ,D.secname,A.Uscno";
	 * 
	 * log.info(sql); return jdbcTemplate.queryForList(sql,new Object[]
	 * {monthYear,monthYear}); } }
	 */

	public List<Map<String, Object>> getHODReport(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		String monthYear = request.getParameter("month") + "-" + request.getParameter("year");
		String deptcode = request.getParameter("hoddept").equals("ALL") ? " "
				: "AND NVL(CTHODDEP,'OTH')='" + request.getParameter("hoddept") + "'";

		if (!circle.equalsIgnoreCase("ALL")) {

			/*
			 * String
			 * sql="Select D.CIRNAME CIRCLE,D.divname DIVISION,D.subname SUB_DIVISION,D.secname SECTION\r\n"
			 * + ",A.Uscno,B.CtName,C.gmdep deptcode,C.dept_name\r\n" +
			 * ",Subdept_Name Hod_Name,B.ctcat CAT,B.CTSUBCAT,CTHODTYPE HOD_TYPE,Decode(Status,'1','LIVE','0','BILL-STOP','') Status,\r\n"
			 * + "Round(Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)) Ob,\r\n" +
			 * "(Mn_Kvah) Sales\r\n" +
			 * ",round(Nvl(Cmd,0)+Nvl(Cclpc,0)) Demand,round(Nvl(Tot_Pay,0)) Collection,round(Nvl(Rj_Oth,0)+Nvl(Drj,0)+Nvl(Rj_Cclpc,0)) Drj,round(Nvl(Crj,0)) Crj,round(Nvl(Cbtot,0)+Nvl(Cb_Oth,0)+Nvl(Cb_Cclpc,0)) Cb\r\n"
			 * +
			 * "From (select LHH.*,'' STATUS_NEW, '' GOVT_PVT from Ledger_Ht_HIST LHH where Mon_Year=?  union all select * from accountcopy where Mon_Year=? ) A,CONS B,SPDCL_HOD_HT C,spdclmaster D\r\n"
			 * + "Where A.Uscno=B.CTUscno And A.Uscno=C.Uscno \r\n" +
			 * "And substr(trim(B.ctseccd),-5)=D.seccd\r\n" +
			 * "AND SUBSTR(A.USCNO,1,3)=? \r\n" + deptcode +
			 * " Order By D.CIRNAME ,D.divname ,D.subname ,D.secname,A.Uscno";
			 */

			String sql = " select  D.CIRNAME CIRCLE,D.divname DIVISION,D.subname SUB_DIVISION,D.secname SECTION\r\n"
					+ ",A.Uscno, B.CtName,NVL(CTHODDEP,'OTH')  deptcode,GMDEPTNAME dept_name\r\n"
					+ ",GMSUBDEPTNAME Hod_Name,B.ctcat CAT,B.CTSUBCAT,CTHODTYPE HOD_TYPE,Decode(Status,'1','LIVE','0','BILL-STOP','') Status,\r\n"
					+ "Round(Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)) Ob,\r\n" + "(Mn_Kvah) Sales\r\n"
					+ ",round(Nvl(Cmd,0)+Nvl(Cclpc,0)) Demand,round(Nvl(Tot_Pay,0)) Collection,"
					+ "round(Nvl(Rj_Oth,0)+Nvl(Drj,0)+Nvl(Rj_Cclpc,0)) Drj," + "round(Nvl(Crj,0)) Crj,"
					+ "round(Nvl(Cbtot,0)-Nvl(Cb_lpc,0)+Nvl(Cb_Oth,0)) oth_Cb," + " round(Nvl(Cb_Cclpc,0)) CCLPC,"
					+ "round(Nvl(Cbtot,0)+Nvl(Cb_Oth,0)+Nvl(Cb_Cclpc,0)) CB , \r\n" + "round(Nvl(Cb_lpc,0)) LPC \r\n"
					+ "from cons B,\r\n"
					+ " (SELECT distinct GMTYPECODE,GMTYPE, GMDEPTCODE,GMDEPTNAME,GMSUBDEPTCODE, GMSUBDEPTNAME FROM deptmast group by GMTYPECODE,GMTYPE, GMDEPTCODE,GMDEPTNAME,GMSUBDEPTCODE, GMSUBDEPTNAME\r\n"
					+ "union all\r\n"
					+ "select 'OTH' GMTYPECODE,'OTH'GMTYPE, 'OTH' GMDEPTCODE,'OTH' GMDEPTNAME,'OTH' GMSUBDEPTCODE, 'OTH' GMSUBDEPTNAME from dual),\r\n"
					+ "(select LHH.*,'' STATUS_NEW, '' GOVT_PVT from Ledger_Ht_HIST LHH where Mon_Year=?  union all select * from accountcopy where Mon_Year=?) A,spdclmaster D\r\n"
					+ "where B.CTUscno=A.Uscno and CTHODTYPE=GMTYPECODE(+) and CTHODDEP=GMDEPTCODE(+) and CTHODSUBDEP=GMSUBDEPTCODE(+) and DECODE(CTGOVT_PVT,'Y','GOVT','NON-GOVT')='GOVT'\r\n"
					+ "And substr(trim(ctseccd),-5)=D.seccd AND SUBSTR(A.USCNO,1,3)=? \r\n" + deptcode
					+ "Order By D.CIRNAME ,D.divname ,D.subname ,D.secname,A.Uscno";
			log.info(sql);
			return jdbcTemplate.queryForList(sql, new Object[] { monthYear, monthYear, circle });
		} else {
			/*
			 * String
			 * sql="Select D.CIRNAME CIRCLE,D.divname DIVISION,D.subname SUB_DIVISION,D.secname SECTION\r\n"
			 * + ",A.Uscno,B.CtName,C.gmdep deptcode,C.dept_name\r\n" +
			 * ",Subdept_Name Hod_Name,B.ctcat CAT,B.CTSUBCAT,CTHODTYPE HOD_TYPE,Decode(Status,'1','LIVE','0','BILL-STOP','') Status,\r\n"
			 * + "Round(Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)) Ob,\r\n" +
			 * "(Mn_Kvah) Sales\r\n" +
			 * ",round(Nvl(Cmd,0)+Nvl(Cclpc,0)) Demand,round(Nvl(Tot_Pay,0)) Collection,round(Nvl(Rj_Oth,0)+Nvl(Drj,0)+Nvl(Rj_Cclpc,0)) Drj,round(Nvl(Crj,0)) Crj,round(Nvl(Cbtot,0)+Nvl(Cb_Oth,0)+Nvl(Cb_Cclpc,0)) Cb\r\n"
			 * +
			 * "From (select LHH.*,'' STATUS_NEW, '' GOVT_PVT from Ledger_Ht_HIST LHH where Mon_Year=?  union all select * from accountcopy where Mon_Year=? ) A,CONS B,SPDCL_HOD_HT C,spdclmaster D\r\n"
			 * + "Where A.Uscno=B.CTUscno And A.Uscno=C.Uscno \r\n" +
			 * "And substr(trim(B.ctseccd),-5)=D.seccd\r\n" + deptcode +
			 * " Order By D.CIRNAME ,D.divname ,D.subname ,D.secname,A.Uscno";
			 */
			String sql = " select  D.CIRNAME CIRCLE,D.divname DIVISION,D.subname SUB_DIVISION,D.secname SECTION\r\n"
					+ ",A.Uscno, B.CtName,NVL(CTHODDEP,'OTH')  deptcode,GMDEPTNAME dept_name\r\n"
					+ ",GMSUBDEPTNAME Hod_Name,B.ctcat CAT,B.CTSUBCAT,CTHODTYPE HOD_TYPE,Decode(Status,'1','LIVE','0','BILL-STOP','') Status,\r\n"
					+ "Round(Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)) Ob,\r\n" + "(Mn_Kvah) Sales\r\n"
					+ ",round(Nvl(Cmd,0)+Nvl(Cclpc,0)) Demand," + "round(Nvl(Tot_Pay,0)) Collection,"
					+ "round(Nvl(Rj_Oth,0)+Nvl(Drj,0)+Nvl(Rj_Cclpc,0)) Drj," + "round(Nvl(Crj,0)) Crj,"
					+ "round(Nvl(Cbtot,0)-Nvl(Cb_lpc,0)+Nvl(Cb_Oth,0)) oth_Cb," + " round(Nvl(Cb_Cclpc,0)) CCLPC,"
					+ "round(Nvl(Cbtot,0)+Nvl(Cb_Oth,0)+Nvl(Cb_Cclpc,0)) CB,\r\n" + "round(Nvl(Cb_lpc,0)) LPC \r\n" +

					"from cons B,\r\n"
					+ " (SELECT distinct GMTYPECODE,GMTYPE, GMDEPTCODE,GMDEPTNAME,GMSUBDEPTCODE, GMSUBDEPTNAME FROM deptmast group by GMTYPECODE,GMTYPE, GMDEPTCODE,GMDEPTNAME,GMSUBDEPTCODE, GMSUBDEPTNAME\r\n"
					+ "union all\r\n"
					+ "select 'OTH' GMTYPECODE,'OTH'GMTYPE, 'OTH' GMDEPTCODE,'OTH' GMDEPTNAME,'OTH' GMSUBDEPTCODE, 'OTH' GMSUBDEPTNAME from dual),\r\n"
					+ "(select LHH.*,'' STATUS_NEW, '' GOVT_PVT from Ledger_Ht_HIST LHH where Mon_Year=?  union all select * from accountcopy where Mon_Year=?) A,spdclmaster D\r\n"
					+ "where B.CTUscno=A.Uscno and CTHODTYPE=GMTYPECODE(+) and CTHODDEP=GMDEPTCODE(+) and CTHODSUBDEP=GMSUBDEPTCODE(+) and DECODE(CTGOVT_PVT,'Y','GOVT','NON-GOVT')='GOVT'\r\n"
					+ "And substr(trim(ctseccd),-5)=D.seccd \r\n" + deptcode
					+ "Order By D.CIRNAME ,D.divname ,D.subname ,D.secname,A.Uscno";

			log.info(sql);
			return jdbcTemplate.queryForList(sql, new Object[] { monthYear, monthYear });
		}
	}

	/*
	 * public List<Map<String, Object>> getHODAbstract(HttpServletRequest request) {
	 * String circle = request.getParameter("circle"); String monthYear =
	 * request.getParameter("month") + "-" + request.getParameter("year"); String
	 * deptcode = request.getParameter("hoddept").equals("ALL")?"" :
	 * "AND C.GMDEP='"+request.getParameter("hoddept")+"'";
	 * 
	 * if(!circle.equalsIgnoreCase("ALL")) {
	 * 
	 * String
	 * sql="Select D.CIRNAME CIRCLE,D.divname DIVISION,D.subname SUB_DIVISION,D.secname SECTION\r\n"
	 * + ",C.gmdeptcode deptcode,C.gmdeptname DEPT_NAME,count(*) NOS,\r\n" +
	 * "SUM(Round(Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0))) Ob,\r\n" +
	 * "SUM(Mn_Kvah) Sales\r\n" +
	 * ",SUM(round(Nvl(Cmd,0)+Nvl(Cclpc,0))) Demand,SUM(round(Nvl(Tot_Pay,0))) Collection,SUM(round(Nvl(Rj_Oth,0)+Nvl(Drj,0)+Nvl(Rj_Cclpc,0))) Drj,SUM(round(Nvl(Crj,0))) Crj,SUM(round(Nvl(Cbtot,0)+Nvl(Cb_Oth,0)+Nvl(Cb_Cclpc,0))) Cb\r\n"
	 * +
	 * "From (select LHH.*,'' STATUS_NEW, '' GOVT_PVT from Ledger_Ht_HIST LHH where Mon_Year=?  union all select * from accountcopy where Mon_Year=? ) A,CONS B,deptmast C,spdclmaster D\r\n"
	 * +
	 * "Where A.Uscno=B.CTUscno And B.CTHODDEP=C.gmdeptcode and  B.CTHODSUBDEP=C.GMSUBDEPTCODE \r\n"
	 * + "And substr(trim(B.ctseccd),-5)=D.seccd\r\n" +
	 * "AND SUBSTR(A.Uscno,1,3)=? "+ deptcode
	 * +" GROUP BY D.CIRNAME,D.divname,D.subname ,D.secname\r\n" +
	 * ",C.gmdeptcode ,C.gmdeptname\r\n" + "Order By 1,2,3,4 "; log.info(sql);
	 * return jdbcTemplate.queryForList(sql,new Object[]
	 * {monthYear,monthYear,circle}); }else { String
	 * sql="Select D.CIRNAME CIRCLE,D.divname DIVISION,D.subname SUB_DIVISION,D.secname SECTION\r\n"
	 * + ",C.gmdeptcode deptcode,C.gmdeptname DEPT_NAME,count(*) NOS,\r\n" +
	 * "SUM(Round(Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0))) Ob,\r\n" +
	 * "SUM(Mn_Kvah) Sales\r\n" +
	 * ",SUM(round(Nvl(Cmd,0)+Nvl(Cclpc,0))) Demand,SUM(round(Nvl(Tot_Pay,0))) Collection,SUM(round(Nvl(Rj_Oth,0)+Nvl(Drj,0)+Nvl(Rj_Cclpc,0))) Drj,SUM(round(Nvl(Crj,0))) Crj,SUM(round(Nvl(Cbtot,0)+Nvl(Cb_Oth,0)+Nvl(Cb_Cclpc,0))) Cb\r\n"
	 * +
	 * "From (select LHH.*,'' STATUS_NEW, '' GOVT_PVT from Ledger_Ht_HIST LHH where Mon_Year=?  union all select * from accountcopy where Mon_Year=? ) A,CONS B,deptmast C,spdclmaster D\r\n"
	 * +
	 * "Where A.Uscno=B.CTUscno And B.CTHODDEP=C.gmdeptcode and  B.CTHODSUBDEP=C.GMSUBDEPTCODE \r\n"
	 * + "And substr(trim(B.ctseccd),-5)=D.seccd\r\n" + deptcode
	 * +" GROUP BY D.CIRNAME,D.divname,D.subname ,D.secname\r\n" +
	 * ",C.gmdeptcode ,C.gmdeptname\r\n" + "Order By 1,2,3,4 ";
	 * 
	 * log.info(sql); return jdbcTemplate.queryForList(sql,new Object[]
	 * {monthYear,monthYear}); } }
	 */

	public List<Map<String, Object>> getHODAbstract(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		String monthYear = request.getParameter("month") + "-" + request.getParameter("year");
		String deptcode = request.getParameter("hoddept").equals("ALL") ? ""
				: "AND NVL(CTHODDEP,'OTH')='" + request.getParameter("hoddept") + "'";

		if (!circle.equalsIgnoreCase("ALL")) {

			/*
			 * String
			 * sql="Select D.CIRNAME CIRCLE,D.divname DIVISION,D.subname SUB_DIVISION,D.secname SECTION\r\n"
			 * + ",C.gmdep deptcode,C.dept_name,count(*) NOS,\r\n" +
			 * "SUM(Round(Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0))) Ob,\r\n" +
			 * "SUM(Mn_Kvah) Sales\r\n" +
			 * ",SUM(round(Nvl(Cmd,0)+Nvl(Cclpc,0))) Demand,SUM(round(Nvl(Tot_Pay,0))) Collection,SUM(round(Nvl(Rj_Oth,0)+Nvl(Drj,0)+Nvl(Rj_Cclpc,0))) Drj,SUM(round(Nvl(Crj,0))) Crj,SUM(round(Nvl(Cbtot,0)+Nvl(Cb_Oth,0)+Nvl(Cb_Cclpc,0))) Cb\r\n"
			 * +
			 * "From (select LHH.*,'' STATUS_NEW, '' GOVT_PVT from Ledger_Ht_HIST LHH where Mon_Year=?  union all select * from accountcopy where Mon_Year=? ) A,CONS B,SPDCL_HOD_HT C,spdclmaster D\r\n"
			 * + "Where A.Uscno=B.CTUscno And A.Uscno=C.Uscno \r\n" +
			 * "And substr(trim(B.ctseccd),-5)=D.seccd\r\n" + "AND SUBSTR(A.Uscno,1,3)=? "+
			 * deptcode +" GROUP BY D.CIRNAME,D.divname,D.subname ,D.secname\r\n" +
			 * ",C.gmdep ,C.dept_name\r\n" + "Order By 1,2,3,4 ";
			 */
			String sql = " select  D.CIRNAME CIRCLE,D.divname DIVISION,D.subname SUB_DIVISION,D.secname SECTION\r\n"
					+ ",NVL(CTHODDEP,'OTH')  deptcode,GMDEPTNAME dept_name,count(*) NOS,\r\n"
					+ "SUM(Round(Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0))) Ob,\r\n" + "SUM((Mn_Kvah)) Sales,\r\n"
					+ "SUM(round(Nvl(Cmd,0)+Nvl(Cclpc,0))) Demand,\r\n" + "SUM(round(Nvl(Tot_Pay,0))) Collection,\r\n"
					+ "SUM(round(Nvl(Rj_Oth,0)+Nvl(Drj,0)+Nvl(Rj_Cclpc,0))) Drj,\r\n"
					+ "SUM(round(Nvl(Crj,0))) Crj,\r\n"
					+ "SUM(round(Nvl(Cbtot,0)+Nvl(Cb_Oth,0)+Nvl(Cb_Cclpc,0))) Cb,\r\n"
					+ "SUM(round(Nvl(Cbtot,0)-Nvl(Cb_lpc,0)+Nvl(Cb_Oth,0))) OTH_Cb,\r\n"
					+ "SUM(round(Nvl(Cb_Cclpc,0))) CCLPC ,\r\n" + "SUM(round(Nvl(Cb_lpc,0))) LPC \r\n"
					+ "from cons B,\r\n"
					+ " (SELECT distinct GMTYPECODE,GMTYPE, GMDEPTCODE,GMDEPTNAME,GMSUBDEPTCODE, GMSUBDEPTNAME FROM deptmast group by GMTYPECODE,GMTYPE, GMDEPTCODE,GMDEPTNAME,GMSUBDEPTCODE, GMSUBDEPTNAME\r\n"
					+ "union all\r\n"
					+ "select 'OTH' GMTYPECODE,'OTH'GMTYPE, 'OTH' GMDEPTCODE,'OTH' GMDEPTNAME,'OTH' GMSUBDEPTCODE, 'OTH' GMSUBDEPTNAME from dual),\r\n"
					+ "(select LHH.*,'' STATUS_NEW, '' GOVT_PVT from Ledger_Ht_HIST LHH where Mon_Year=?  union all select * from accountcopy where Mon_Year=? ) A,spdclmaster D\r\n"
					+ "where B.CTUscno=A.Uscno and CTHODTYPE=GMTYPECODE(+) and CTHODDEP=GMDEPTCODE(+) and CTHODSUBDEP=GMSUBDEPTCODE(+) and DECODE(CTGOVT_PVT,'Y','GOVT','NON-GOVT')='GOVT'\r\n"
					+ "And substr(trim(ctseccd),-5)=D.seccd AND SUBSTR(A.Uscno,1,3)=?  \r\n" + deptcode
					+ " GROUP BY D.CIRNAME,D.divname,D.subname ,D.secname\r\n" + ",NVL(CTHODDEP,'OTH') ,GMDEPTNAME\r\n"
					+ "Order By D.CIRNAME ,D.divname ,D.subname ,D.secname";
			log.info(sql);
			return jdbcTemplate.queryForList(sql, new Object[] { monthYear, monthYear, circle });
		} else {
			/*
			 * String
			 * sql="Select D.CIRNAME CIRCLE,D.divname DIVISION,D.subname SUB_DIVISION,D.secname SECTION\r\n"
			 * + ",C.gmdep deptcode,C.dept_name,count(*) NOS,\r\n" +
			 * "SUM(Round(Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0))) Ob,\r\n" +
			 * "SUM(Mn_Kvah) Sales\r\n" +
			 * ",SUM(round(Nvl(Cmd,0)+Nvl(Cclpc,0))) Demand,SUM(round(Nvl(Tot_Pay,0))) Collection,SUM(round(Nvl(Rj_Oth,0)+Nvl(Drj,0)+Nvl(Rj_Cclpc,0))) Drj,SUM(round(Nvl(Crj,0))) Crj,SUM(round(Nvl(Cbtot,0)+Nvl(Cb_Oth,0)+Nvl(Cb_Cclpc,0))) Cb\r\n"
			 * +
			 * "From (select LHH.*,'' STATUS_NEW, '' GOVT_PVT from Ledger_Ht_HIST LHH where Mon_Year=?  union all select * from accountcopy where Mon_Year=? ) A,CONS B,SPDCL_HOD_HT C,spdclmaster D\r\n"
			 * + "Where A.Uscno=B.CTUscno And A.Uscno=C.Uscno \r\n" +
			 * "And substr(trim(B.ctseccd),-5)=D.seccd\r\n" + deptcode
			 * +" GROUP BY D.CIRNAME,D.divname,D.subname ,D.secname\r\n" +
			 * ",C.gmdep ,C.dept_name\r\n" + "Order By 1,2,3,4 ";
			 */
			String sql = " select  D.CIRNAME CIRCLE,D.divname DIVISION,D.subname SUB_DIVISION,D.secname SECTION\r\n"
					+ ",NVL(CTHODDEP,'OTH')  deptcode,GMDEPTNAME dept_name,count(*) NOS,\r\n"
					+ "SUM(Round(Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0))) Ob,\r\n" + "SUM((Mn_Kvah)) Sales,\r\n"
					+ "SUM(round(Nvl(Cmd,0)+Nvl(Cclpc,0))) Demand,\r\n" + "SUM(round(Nvl(Tot_Pay,0))) Collection,\r\n"
					+ "SUM(round(Nvl(Rj_Oth,0)+Nvl(Drj,0)+Nvl(Rj_Cclpc,0))) Drj,\r\n"
					+ "SUM(round(Nvl(Crj,0))) Crj,\r\n"
					+ "SUM(round(Nvl(Cbtot,0)+Nvl(Cb_Oth,0)+Nvl(Cb_Cclpc,0))) Cb,\r\n"
					+ "SUM(round(Nvl(Cbtot,0)-Nvl(Cb_lpc,0) +Nvl(Cb_Oth,0))) OTH_Cb,\r\n"
					+ "SUM(round(Nvl(Cb_Cclpc,0))) CCLPC, \r\n" + "SUM(round(Nvl(Cb_lpc,0))) LPC \r\n"
					+ "from cons B,\r\n"
					+ " (SELECT distinct GMTYPECODE,GMTYPE, GMDEPTCODE,GMDEPTNAME,GMSUBDEPTCODE, GMSUBDEPTNAME FROM deptmast group by GMTYPECODE,GMTYPE, GMDEPTCODE,GMDEPTNAME,GMSUBDEPTCODE, GMSUBDEPTNAME\r\n"
					+ "union all\r\n"
					+ "select 'OTH' GMTYPECODE,'OTH'GMTYPE, 'OTH' GMDEPTCODE,'OTH' GMDEPTNAME,'OTH' GMSUBDEPTCODE, 'OTH' GMSUBDEPTNAME from dual),\r\n"
					+ "(select LHH.*,'' STATUS_NEW, '' GOVT_PVT from Ledger_Ht_HIST LHH where Mon_Year=?  union all select * from accountcopy where Mon_Year=? ) A,spdclmaster D\r\n"
					+ "where B.CTUscno=A.Uscno and CTHODTYPE=GMTYPECODE(+) and CTHODDEP=GMDEPTCODE(+) and CTHODSUBDEP=GMSUBDEPTCODE(+) and DECODE(CTGOVT_PVT,'Y','GOVT','NON-GOVT')='GOVT'\r\n"
					+ "And substr(trim(ctseccd),-5)=D.seccd  \r\n" + deptcode
					+ " GROUP BY D.CIRNAME,D.divname,D.subname ,D.secname\r\n" + ",NVL(CTHODDEP,'OTH') ,GMDEPTNAME\r\n"
					+ "Order By D.CIRNAME ,D.divname ,D.subname ,D.secname";

			log.info(sql);
			return jdbcTemplate.queryForList(sql, new Object[] { monthYear, monthYear });
		}
	}

	/*
	 * public List<Map<String, Object>> getHODWiseAbstract(HttpServletRequest
	 * request) { String circle = request.getParameter("circle"); String monthYear =
	 * request.getParameter("month") + "-" + request.getParameter("year"); String
	 * deptcode = request.getParameter("hoddept").equals("ALL")?"" :
	 * "AND C.GMDEP='"+request.getParameter("hoddept")+"'";
	 * if(circle.equalsIgnoreCase("ALL")) {
	 * 
	 * String
	 * sql="Select C.gmdeptcode deptcode,C.gmdeptname DEPT_NAME,count(*) NOS,\r\n" +
	 * "SUM(Round(Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0))) Ob,\r\n" +
	 * "SUM(Mn_Kvah) Sales\r\n" + ",SUM(round(Nvl(Cmd,0)+Nvl(Cclpc,0))) Demand,"
	 * +"SUM(Nvl(round(CASE WHEN Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)>0 THEN CASE WHEN Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)>(NVL(Tot_Pay,0)) THEN (NVL(Tot_Pay,0)) ELSE Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0) END END),0)) COLL_ARREAR,\r\n"
	 * +
	 * "SUM(Nvl(round(CASE WHEN Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)>0 THEN CASE WHEN Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)<(NVL(Tot_Pay,0)) THEN (NVL(Tot_Pay,0)-(Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0))) END ELSE (NVL(Tot_Pay,0)) END ),0)) COLL_DEMAND,"
	 * +
	 * "SUM(round(Nvl(Tot_Pay,0))) Collection,SUM(round(Nvl(Rj_Oth,0)+Nvl(Drj,0)+Nvl(Rj_Cclpc,0))) Drj,SUM(round(Nvl(Crj,0))) Crj,SUM(round(Nvl(Cbtot,0)+Nvl(Cb_Oth,0)+Nvl(Cb_Cclpc,0))) Cb\r\n"
	 * +
	 * "From (select LHH.*,'' STATUS_NEW, '' GOVT_PVT from Ledger_Ht_HIST LHH where Mon_Year=?  union all select * from accountcopy where Mon_Year=? ) A,CONS B,deptmast C,spdclmaster D\r\n"
	 * +
	 * "Where A.Uscno=B.CTUscno And B.CTHODDEP=C.gmdeptcode and  B.CTHODSUBDEP=C.GMSUBDEPTCODE  \r\n"
	 * + "And substr(trim(B.ctseccd),-5)=D.seccd\r\n" + deptcode
	 * +" GROUP BY  C.gmdeptcode ,C.gmdeptname\r\n" + "Order By 1,2,3,4 ";
	 * 
	 * log.info(sql); return jdbcTemplate.queryForList(sql,new Object[]
	 * {monthYear,monthYear}); }else { String
	 * sql="Select C.gmdeptcode deptcode,C.gmdeptname DEPT_NAME,count(*) NOS,\r\n" +
	 * "SUM(Round(Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0))) Ob,\r\n" +
	 * "SUM(Mn_Kvah) Sales\r\n" + ",SUM(round(Nvl(Cmd,0)+Nvl(Cclpc,0))) Demand,"
	 * +"SUM(Nvl(round(CASE WHEN Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)>0 THEN CASE WHEN Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)>(NVL(Tot_Pay,0)) THEN (NVL(Tot_Pay,0)) ELSE Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0) END END),0)) COLL_ARREAR,\r\n"
	 * +
	 * "SUM(Nvl(round(CASE WHEN Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)>0 THEN CASE WHEN Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)<(NVL(Tot_Pay,0)) THEN (NVL(Tot_Pay,0)-(Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0))) END ELSE (NVL(Tot_Pay,0)) END ),0)) COLL_DEMAND,"
	 * +
	 * "SUM(round(Nvl(Tot_Pay,0))) Collection,SUM(round(Nvl(Rj_Oth,0)+Nvl(Drj,0)+Nvl(Rj_Cclpc,0))) Drj,SUM(round(Nvl(Crj,0))) Crj,SUM(round(Nvl(Cbtot,0)+Nvl(Cb_Oth,0)+Nvl(Cb_Cclpc,0))) Cb\r\n"
	 * +
	 * "From (select LHH.*,'' STATUS_NEW, '' GOVT_PVT from Ledger_Ht_HIST LHH where Mon_Year=?  union all select * from accountcopy where Mon_Year=? ) A,CONS B,deptmast C,spdclmaster D\r\n"
	 * +
	 * "Where A.Uscno=B.CTUscno And B.CTHODDEP=C.gmdeptcode and  B.CTHODSUBDEP=C.GMSUBDEPTCODE  \r\n"
	 * + "And substr(trim(B.ctseccd),-5)=D.seccd AND SUBSTR(A.Uscno,1,3) = ?\r\n" +
	 * deptcode +" GROUP BY  C.gmdeptcode,C.gmdeptname \r\n" + "Order By 1,2,3,4 ";
	 * 
	 * log.info(sql); return jdbcTemplate.queryForList(sql,new Object[]
	 * {monthYear,monthYear,circle});
	 * 
	 * }
	 * 
	 * }
	 */
	public List<Map<String, Object>> getSTWiseAbstract(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		String monthYear = request.getParameter("month") + "-" + request.getParameter("year");
		String deptcode = request.getParameter("servicetype").equals("ALL") ? ""
				: "AND b.CTSERVTYPE='" + request.getParameter("servicetype") + "'";
		if (circle.equalsIgnoreCase("ALL")) {

			String sql = "Select b.CTSERVTYPE servtype,SUBSTR(CTUSCNO,1,3)CIRCLE,c.stdesc,count(distinct(ctuscno)) NOS,SUM(LOAD) LOAD,SUM(REC_MD) REC_MD,\r\n"
					+ "SUM(Round(Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0))) Ob,\r\n" + "SUM(Mn_Kvah) Sales\r\n"
					+ ",SUM(round(Nvl(Cmd,0)+Nvl(Cclpc,0))) Demand,SUM(Nvl(round(CASE WHEN Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)>0 THEN CASE WHEN Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)>(NVL(Tot_Pay,0)) THEN (NVL(Tot_Pay,0)) ELSE Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0) END END),0)) COLL_ARREAR,\r\n"
					+ "SUM(Nvl(round(CASE WHEN Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)>0 THEN CASE WHEN Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)<(NVL(Tot_Pay,0)) THEN (NVL(Tot_Pay,0)-(Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0))) END ELSE (NVL(Tot_Pay,0)) END ),0)) COLL_DEMAND,SUM(round(Nvl(Tot_Pay,0))) Collection,SUM(round(Nvl(Rj_Oth,0)+Nvl(Drj,0)+Nvl(Rj_Cclpc,0))) Drj,SUM(round(Nvl(Crj,0))) Crj,SUM(round(Nvl(Cbtot,0)+Nvl(Cb_Oth,0)+Nvl(Cb_Cclpc,0))) Cb\r\n"
					+ "From (select LHH.*,'' STATUS_NEW, '' GOVT_PVT from Ledger_Ht_HIST LHH where Mon_Year=? union all select * from accountcopy where Mon_Year=?) A,CONS B,servtype C\r\n"
					+ "Where A.Uscno=B.CTUscno \r\n" + "And b.ctservtype=c.stcode\r\n" + deptcode
					+ " GROUP BY  SUBSTR(CTUSCNO,1,3),b.ctservtype,c.stdesc\r\n" + "Order By 1,2,3,4";

			log.info(sql);
			return jdbcTemplate.queryForList(sql, new Object[] { monthYear, monthYear });
		} else {
			String sql = "Select b.CTSERVTYPE servtype,SUBSTR(CTUSCNO,1,3)CIRCLE,c.stdesc,count(distinct(ctuscno)) NOS,SUM(LOAD) LOAD,SUM(REC_MD) REC_MD,\r\n"
					+ "SUM(Round(Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0))) Ob,\r\n" + "SUM(Mn_Kvah) Sales\r\n"
					+ ",SUM(round(Nvl(Cmd,0)+Nvl(Cclpc,0))) Demand,SUM(Nvl(round(CASE WHEN Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)>0 THEN CASE WHEN Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)>(NVL(Tot_Pay,0)) THEN (NVL(Tot_Pay,0)) ELSE Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0) END END),0)) COLL_ARREAR,\r\n"
					+ "SUM(Nvl(round(CASE WHEN Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)>0 THEN CASE WHEN Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)<(NVL(Tot_Pay,0)) THEN (NVL(Tot_Pay,0)-(Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0))) END ELSE (NVL(Tot_Pay,0)) END ),0)) COLL_DEMAND,SUM(round(Nvl(Tot_Pay,0))) Collection,SUM(round(Nvl(Rj_Oth,0)+Nvl(Drj,0)+Nvl(Rj_Cclpc,0))) Drj,SUM(round(Nvl(Crj,0))) Crj,SUM(round(Nvl(Cbtot,0)+Nvl(Cb_Oth,0)+Nvl(Cb_Cclpc,0))) Cb\r\n"
					+ "From (select LHH.*,'' STATUS_NEW, '' GOVT_PVT from Ledger_Ht_HIST LHH where Mon_Year=? union all select * from accountcopy where Mon_Year=?) A,CONS B,servtype C\r\n"
					+ "Where A.Uscno=B.CTUscno  AND SUBSTR(A.Uscno,1,3) = ?\r\n" + "And b.ctservtype=c.stcode\r\n"
					+ deptcode + " GROUP BY SUBSTR(CTUSCNO,1,3), b.ctservtype,c.stdesc\r\n" + "Order By 1,2,3,4";

			log.info(sql);
			return jdbcTemplate.queryForList(sql, new Object[] { monthYear, monthYear, circle });

		}

	}

	public List<Map<String, Object>> getSTCatWiseAbstract(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		String monthYear = request.getParameter("month") + "-" + request.getParameter("year");
		String deptcode = request.getParameter("servicetype").equals("ALL") ? ""
				: "AND b.CTSERVTYPE='" + request.getParameter("servicetype") + "'";
		if (circle.equalsIgnoreCase("ALL")) {

			String sql = "Select SUBSTR(CTUSCNO,1,3)CIRCLE,b.CTSERVTYPE servtype,c.stdesc,b.CTCAT,count(distinct(ctuscno)) NOS,SUM(LOAD) LOAD,SUM(REC_MD) REC_MD,\r\n"
					+ "SUM(Round(Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0))) Ob,\r\n" + "SUM(Mn_Kvah) Sales\r\n"
					+ ",SUM(round(Nvl(Cmd,0)+Nvl(Cclpc,0))) Demand,SUM(Nvl(round(CASE WHEN Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)>0 THEN CASE WHEN Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)>(NVL(Tot_Pay,0)) THEN (NVL(Tot_Pay,0)) ELSE Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0) END END),0)) COLL_ARREAR,\r\n"
					+ "SUM(Nvl(round(CASE WHEN Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)>0 THEN CASE WHEN Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)<(NVL(Tot_Pay,0)) THEN (NVL(Tot_Pay,0)-(Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0))) END ELSE (NVL(Tot_Pay,0)) END ),0)) COLL_DEMAND,SUM(round(Nvl(Tot_Pay,0))) Collection,SUM(round(Nvl(Rj_Oth,0)+Nvl(Drj,0)+Nvl(Rj_Cclpc,0))) Drj,SUM(round(Nvl(Crj,0))) Crj,SUM(round(Nvl(Cbtot,0)+Nvl(Cb_Oth,0)+Nvl(Cb_Cclpc,0))) Cb\r\n"
					+ "From (select LHH.*,'' STATUS_NEW, '' GOVT_PVT from Ledger_Ht_HIST LHH where Mon_Year=? union all select * from accountcopy where Mon_Year=?) A,CONS B,servtype C\r\n"
					+ "Where A.Uscno=B.CTUscno \r\n" + "And b.ctservtype=c.stcode\r\n" + deptcode
					+ " GROUP BY  SUBSTR(CTUSCNO,1,3),b.ctservtype,c.stdesc,b.CTCAT \r\n" + "Order By 1,2,3,4";

			log.info(sql);
			return jdbcTemplate.queryForList(sql, new Object[] { monthYear, monthYear });
		} else {
			String sql = "Select SUBSTR(CTUSCNO,1,3)CIRCLE,b.CTSERVTYPE servtype,c.stdesc,b.CTCAT,count(distinct(ctuscno)) NOS,SUM(LOAD) LOAD,SUM(REC_MD) REC_MD,\r\n"
					+ "SUM(Round(Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0))) Ob,\r\n" + "SUM(Mn_Kvah) Sales\r\n"
					+ ",SUM(round(Nvl(Cmd,0)+Nvl(Cclpc,0))) Demand,SUM(Nvl(round(CASE WHEN Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)>0 THEN CASE WHEN Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)>(NVL(Tot_Pay,0)) THEN (NVL(Tot_Pay,0)) ELSE Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0) END END),0)) COLL_ARREAR,\r\n"
					+ "SUM(Nvl(round(CASE WHEN Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)>0 THEN CASE WHEN Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)<(NVL(Tot_Pay,0)) THEN (NVL(Tot_Pay,0)-(Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0))) END ELSE (NVL(Tot_Pay,0)) END ),0)) COLL_DEMAND,SUM(round(Nvl(Tot_Pay,0))) Collection,SUM(round(Nvl(Rj_Oth,0)+Nvl(Drj,0)+Nvl(Rj_Cclpc,0))) Drj,SUM(round(Nvl(Crj,0))) Crj,SUM(round(Nvl(Cbtot,0)+Nvl(Cb_Oth,0)+Nvl(Cb_Cclpc,0))) Cb\r\n"
					+ "From (select LHH.*,'' STATUS_NEW, '' GOVT_PVT from Ledger_Ht_HIST LHH where Mon_Year=? union all select * from accountcopy where Mon_Year=?) A,CONS B,servtype C\r\n"
					+ "Where A.Uscno=B.CTUscno  AND SUBSTR(A.Uscno,1,3) = ?\r\n" + "And b.ctservtype=c.stcode\r\n"
					+ deptcode + " GROUP BY SUBSTR(CTUSCNO,1,3), b.ctservtype,c.stdesc,b.CTCAT\r\n" + "Order By 1,2,3,4";

			log.info(sql);
			return jdbcTemplate.queryForList(sql, new Object[] { monthYear, monthYear, circle });

		}

	}

	public List<Map<String, Object>> getFYSTWiseAbstract(HttpServletRequest request) {
		String circle = request.getParameter("circle");

		String fmonthYear = "APR-" + request.getParameter("year").split("-")[0];
		String tmonthYear = "MAR-" + request.getParameter("year").split("-")[1];
		String emptoyear = tmonthYear.equals("MAR-2024") ? "JAN-2024" : tmonthYear;

		//String st = "";
		String serviceType = request.getParameter("servicetype");
		/*
		 * for (String s : items) { st = st + s + ","; }
		 */
		//st = st.substring(0, st.length() - 1);
		String deptcode = serviceType.equals("ALL") ? "" : " AND b.CTSERVTYPE ='" + serviceType + "'";
		System.out.println(deptcode);

		if (circle.equalsIgnoreCase("ALL")) {

			String sql = "Select b.CTSERVTYPE servtype,c.stdesc,count(distinct(ctuscno)) NOS,"
					+ "sum(case when Mon_Year = '" + emptoyear + "' then Round(Nvl(LOAD,0)) else 0 end ) LOAD,\r\n"
					+ "sum(case when Mon_Year = '" + emptoyear + "' then Round(Nvl(REC_MD,0)) else 0 end ) REC_MD,\r\n"
					+ "SUM(Round(Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0))) Ob,\r\n" + "SUM(Mn_Kvah) Sales\r\n"
					+ ",SUM(round(Nvl(Cmd,0)+Nvl(Cclpc,0))) Demand,SUM(Nvl(round(CASE WHEN Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)>0 THEN CASE WHEN Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)>(NVL(Tot_Pay,0)) THEN (NVL(Tot_Pay,0)) ELSE Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0) END END),0)) COLL_ARREAR,\r\n"
					+ "SUM(Nvl(round(CASE WHEN Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)>0 THEN CASE WHEN Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)<(NVL(Tot_Pay,0)) THEN (NVL(Tot_Pay,0)-(Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0))) END ELSE (NVL(Tot_Pay,0)) END ),0)) COLL_DEMAND,SUM(round(Nvl(Tot_Pay,0))) Collection,SUM(round(Nvl(Rj_Oth,0)+Nvl(Drj,0)+Nvl(Rj_Cclpc,0))) Drj,SUM(round(Nvl(Crj,0))) Crj,SUM(round(Nvl(Cbtot,0)+Nvl(Cb_Oth,0)+Nvl(Cb_Cclpc,0))) Cb\r\n"
					+ "From (select LHH.*,'' STATUS_NEW, '' GOVT_PVT from Ledger_Ht_HIST LHH where to_date(Mon_Year,'MON-YYYY') between to_date(?,'MON-YYYY') and LAST_DAY(to_date(?,'MON-YYYY')) union all select * from accountcopy where to_date(Mon_Year,'MON-YYYY')between to_date(?,'MON-YYYY') and LAST_DAY(to_date(?,'MON-YYYY')) ) A,CONS B,servtype C\r\n"
					+ "Where A.Uscno=B.CTUscno \r\n" + "And b.ctservtype=c.stcode\r\n" + deptcode
					+ " GROUP BY  b.ctservtype,c.stdesc\r\n" + "Order By 1,2,3,4";

			log.info(sql);
			return jdbcTemplate.queryForList(sql, new Object[] { fmonthYear, tmonthYear, fmonthYear, tmonthYear });
		} else {
			String sql = "Select b.CTSERVTYPE servtype,c.stdesc,count(distinct(ctuscno)) NOS,"
					+ "sum(case when Mon_Year = '" + tmonthYear + "' then Round(Nvl(LOAD,0)) else 0 end ) LOAD,\r\n"
					+ "sum(case when Mon_Year = '" + tmonthYear + "' then Round(Nvl(REC_MD,0)) else 0 end ) REC_MD,\r\n"
					+ "SUM(Round(Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0))) Ob,\r\n" + "SUM(Mn_Kvah) Sales\r\n"
					+ ",SUM(round(Nvl(Cmd,0)+Nvl(Cclpc,0))) Demand,SUM(Nvl(round(CASE WHEN Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)>0 THEN CASE WHEN Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)>(NVL(Tot_Pay,0)) THEN (NVL(Tot_Pay,0)) ELSE Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0) END END),0)) COLL_ARREAR,\r\n"
					+ "SUM(Nvl(round(CASE WHEN Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)>0 THEN CASE WHEN Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)<(NVL(Tot_Pay,0)) THEN (NVL(Tot_Pay,0)-(Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0))) END ELSE (NVL(Tot_Pay,0)) END ),0)) COLL_DEMAND,SUM(round(Nvl(Tot_Pay,0))) Collection,SUM(round(Nvl(Rj_Oth,0)+Nvl(Drj,0)+Nvl(Rj_Cclpc,0))) Drj,SUM(round(Nvl(Crj,0))) Crj,SUM(round(Nvl(Cbtot,0)+Nvl(Cb_Oth,0)+Nvl(Cb_Cclpc,0))) Cb\r\n"
					+ "From (select LHH.*,'' STATUS_NEW, '' GOVT_PVT from Ledger_Ht_HIST LHH where to_date(Mon_Year,'MON-YYYY') between to_date(?,'MON-YYYY') and LAST_DAY(to_date(?,'MON-YYYY')) union all select * from accountcopy where to_date(Mon_Year,'MON-YYYY') between to_date(?,'MON-YYYY') and LAST_DAY(to_date(?,'MON-YYYY'))) A,CONS B,servtype C\r\n"
					+ "Where A.Uscno=B.CTUscno  AND SUBSTR(A.Uscno,1,3) = ?\r\n" + "And b.ctservtype=c.stcode\r\n" + deptcode
					+ deptcode + " GROUP BY  b.ctservtype,c.stdesc\r\n" + "Order By 1,2,3,4";

			log.info(sql);
			return jdbcTemplate.queryForList(sql,
					new Object[] { fmonthYear, tmonthYear, fmonthYear, tmonthYear, circle });

		}

	}

	public List<Map<String, Object>> getFDWiseAbstract(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		String monthYear = request.getParameter("month") + "-" + request.getParameter("year");
		String deptcode = request.getParameter("feeder").equals("ALL") ? ""
				: "AND ctfeeder_code='" + request.getParameter("feeder") + "'";
		if (circle.equalsIgnoreCase("ALL")) {

			String sql = "Select UNIQUE fmsapfcode FEEDER_CD,FMFNAME FEEDER_NAME,count(distinct(ctuscno)) NOS,\r\n"
					+ "SUM(nvl(REC_KWH,0)) KWH_UNITS,\r\n" + "SUM(nvl(Mn_Kvah,0)) BKVA_UNITS,\r\n"
					+ "SUM(Mn_Kvah) Sales,\r\n" + "SUM(Round(Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0))) Ob,\r\n"
					+ "SUM(round(Nvl(Cmd,0)+Nvl(Cclpc,0))) Demand,\r\n"
					+ "SUM(Nvl(round(CASE WHEN Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)>0 THEN \r\n"
					+ "CASE WHEN Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)>(NVL(Tot_Pay,0)) THEN (NVL(Tot_Pay,0)) ELSE Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0) END END),0)) COLL_ARREAR,\r\n"
					+ "SUM(Nvl(round(CASE WHEN Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)>0 THEN \r\n"
					+ "CASE WHEN Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)<(NVL(Tot_Pay,0)) THEN (NVL(Tot_Pay,0)-(Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0))) END ELSE (NVL(Tot_Pay,0)) END ),0)) COLL_DEMAND,\r\n"
					+ "SUM(round(Nvl(Tot_Pay,0))) Collection,SUM(round(Nvl(Rj_Oth,0)+Nvl(Drj,0)+Nvl(Rj_Cclpc,0))) Drj,SUM(round(Nvl(Crj,0))) Crj,\r\n"
					+ "SUM(round(Nvl(Cbtot,0)+Nvl(Cb_Oth,0)+Nvl(Cb_Cclpc,0))) Cb from ledger_ht_hist a,cons b,feedermast\r\n"
					+ "where Mon_Year=?\r\n" + "and  A.Uscno=B.CTUscno and ctfeeder_code=fmsapfcode\r\n" + deptcode
					+ " GROUP BY  FMFNAME,fmsapfcode\r\n" + "Order By fmsapfcode";

			log.info(sql);
			return jdbcTemplate.queryForList(sql, new Object[] { monthYear });
		} else {
			String sql = "Select UNIQUE fmsapfcode FEEDER_CD,FMFNAME FEEDER_NAME,count(distinct(ctuscno)) NOS,\r\n"
					+ "SUM(nvl(REC_KWH,0)) KWH_UNITS,\r\n" + "SUM(nvl(Mn_Kvah,0)) BKVA_UNITS,\r\n"
					+ "SUM(Mn_Kvah) Sales,\r\n" + "SUM(Round(Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0))) Ob,\r\n"
					+ "SUM(round(Nvl(Cmd,0)+Nvl(Cclpc,0))) Demand,\r\n"
					+ "SUM(Nvl(round(CASE WHEN Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)>0 THEN \r\n"
					+ "CASE WHEN Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)>(NVL(Tot_Pay,0)) THEN (NVL(Tot_Pay,0)) ELSE Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0) END END),0)) COLL_ARREAR,\r\n"
					+ "SUM(Nvl(round(CASE WHEN Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)>0 THEN \r\n"
					+ "CASE WHEN Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)<(NVL(Tot_Pay,0)) THEN (NVL(Tot_Pay,0)-(Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0))) END ELSE (NVL(Tot_Pay,0)) END ),0)) COLL_DEMAND,\r\n"
					+ "SUM(round(Nvl(Tot_Pay,0))) Collection,SUM(round(Nvl(Rj_Oth,0)+Nvl(Drj,0)+Nvl(Rj_Cclpc,0))) Drj,SUM(round(Nvl(Crj,0))) Crj,\r\n"
					+ "SUM(round(Nvl(Cbtot,0)+Nvl(Cb_Oth,0)+Nvl(Cb_Cclpc,0))) Cb from ledger_ht_hist a,cons b,feedermast\r\n"
					+ "where Mon_Year=?\r\n"
					+ "and  A.Uscno=B.CTUscno and ctfeeder_code=fmsapfcode and substr(b.CTUscno,1,3) = ? \r\n"
					+ deptcode + " GROUP BY  FMFNAME,fmsapfcode\r\n" + "Order By fmsapfcode";

			log.info(sql);
			return jdbcTemplate.queryForList(sql, new Object[] { monthYear, circle });

		}

	}

	public List<Map<String, Object>> getFDWiseSubDivisionAbstract(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		String division = request.getParameter("division");
		String subdivision = request.getParameter("subdivision");
		String monthYear = request.getParameter("month") + "-" + request.getParameter("year");
		String deptcode = request.getParameter("feeder");
		try {
			StringBuilder sqlBuilder = new StringBuilder();
			sqlBuilder.append("Select UNIQUE MON_YEAR,substr(b.CTUscno,1,3)CIRNAME,DIVNAME,SUBNAME, fmsapfcode FEEDER_CD,FMFNAME FEEDER_NAME,count(distinct(ctuscno)) NOS,\r\n");
			sqlBuilder.append("SUM(nvl(REC_KWH,0)) KWH_UNITS,SUM(nvl(Mn_Kvah,0)) BKVA_UNITS,SUM(Mn_Kvah) Sales,\r\n");
			sqlBuilder.append("SUM(Round(Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0))) Ob,SUM(round(Nvl(Cmd,0)+Nvl(Cclpc,0))) Demand,\r\n");
			sqlBuilder.append("SUM(Nvl(round(CASE WHEN Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)>0 THEN \r\n");
			sqlBuilder.append("CASE WHEN Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)>(NVL(Tot_Pay,0)) THEN (NVL(Tot_Pay,0)) ELSE Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0) END END),0)) COLL_ARREAR,\r\n");
			sqlBuilder.append("SUM(Nvl(round(CASE WHEN Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)>0 THEN\r\n");
			sqlBuilder.append("CASE WHEN Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)<(NVL(Tot_Pay,0)) THEN (NVL(Tot_Pay,0)-(Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0))) END ELSE (NVL(Tot_Pay,0)) END ),0)) COLL_DEMAND,\r\n");
			sqlBuilder.append("SUM(round(Nvl(Tot_Pay,0))) Collection,SUM(round(Nvl(Rj_Oth,0)+Nvl(Drj,0)+Nvl(Rj_Cclpc,0))) Drj,SUM(round(Nvl(Crj,0))) Crj,\r\n");
			sqlBuilder.append("SUM(round(Nvl(Cbtot,0)+Nvl(Cb_Oth,0)+Nvl(Cb_Cclpc,0))) Cb from ledger_ht_hist a,cons b,feedermast,MASTER.SPDCLMASTER\r\n");
			sqlBuilder.append("where Mon_Year=?\r\n");
			sqlBuilder.append("and  A.Uscno=B.CTUscno and ctfeeder_code=fmsapfcode\r\n");
			
			List<Object> params = new ArrayList<>();
			params.add(monthYear);

			if (circle != null && !circle.isEmpty()) {
				sqlBuilder.append("and substr(b.CTUscno,1,3) = ?\r\n");
				params.add(circle);
			}

			if (division != null && !division.isEmpty() && !division.equals("0")) {
				sqlBuilder.append("and DIVCD = ? \r\n");
				params.add(division);
			} else {
				sqlBuilder.append("");
			}

			if (subdivision != null && !subdivision.isEmpty() && !subdivision.equals("0")) {
				sqlBuilder.append("and ctfeeder_code in (select ctfeeder_code from cons,master.spdclmaster where SUBSTR(CTSECCD,-5)=SECCD(+) and SUBCD = ? )\r\n");
				params.add(subdivision);
			} else {
				sqlBuilder.append("and ctfeeder_code in (select ctfeeder_code from cons,master.spdclmaster where SUBSTR(CTSECCD,-5)=SECCD(+))");
			}

			if (deptcode != null && !deptcode.isEmpty()&& !deptcode.equals("ALL")) {
				sqlBuilder.append("AND ctfeeder_code=? \r\n");
				params.add(deptcode);
			}else {
				sqlBuilder.append("");
			}
			sqlBuilder.append("AND SUBSTR(CTSECCD,-5)=SECCD\r\n");
			sqlBuilder.append(
					"GROUP BY MON_YEAR,substr(b.CTUscno,1,3),DIVNAME,SUBNAME,FMFNAME,fmsapfcode\r\n");
			sqlBuilder.append("Order By MON_YEAR,CIRNAME,DIVNAME,SUBNAME,fmsapfcode\r\n");

			String sql = sqlBuilder.toString();
			log.info(sql);

			return jdbcTemplate.queryForList(sql, params.toArray());
		} catch (DataAccessException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return Collections.emptyList();
		}

	}

	public List<Map<String, Object>> getVoltageWiseAbstract(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		String monthYear = request.getParameter("month") + "-" + request.getParameter("year");
		String deptcode = request.getParameter("voltage").equals("ALL") ? ""
				: "AND  b.CTACTUAL_KV=" + request.getParameter("voltage") + "";
		if (circle.equalsIgnoreCase("ALL")) {

			String sql = "Select UNIQUE b.CTACTUAL_KV VOLTAGE,count(distinct(ctuscno)) NOS,\r\n"
					+ "SUM(Round(Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0))) Ob,\r\n" + "SUM(Mn_Kvah) Sales\r\n"
					+ ",SUM(round(Nvl(Cmd,0)+Nvl(Cclpc,0))) Demand,\r\n"
					+ "SUM(Nvl(round(CASE WHEN Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)>0 THEN \r\n"
					+ "CASE WHEN Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)>(NVL(Tot_Pay,0)) THEN (NVL(Tot_Pay,0)) ELSE Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0) END END),0)) COLL_ARREAR,\r\n"
					+ "SUM(Nvl(round(CASE WHEN Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)>0 THEN \r\n"
					+ "CASE WHEN Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)<(NVL(Tot_Pay,0)) THEN (NVL(Tot_Pay,0)-(Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0))) END ELSE (NVL(Tot_Pay,0)) END ),0)) COLL_DEMAND,\r\n"
					+ "SUM(round(Nvl(Tot_Pay,0))) Collection,SUM(round(Nvl(Rj_Oth,0)+Nvl(Drj,0)+Nvl(Rj_Cclpc,0))) Drj,SUM(round(Nvl(Crj,0))) Crj,\r\n"
					+ "SUM(round(Nvl(Cbtot,0)+Nvl(Cb_Oth,0)+Nvl(Cb_Cclpc,0))) Cb from ledger_ht_hist a,cons b\r\n"
					+ "where Mon_Year=?\r\n" + "and  A.Uscno=B.CTUscno  \r\n" + deptcode
					+ " GROUP BY  b.CTACTUAL_KV\r\n" + "Order By b.CTACTUAL_KV";

			log.info(sql);
			return jdbcTemplate.queryForList(sql, new Object[] { monthYear });
		} else {
			String sql = "Select UNIQUE b.CTACTUAL_KV VOLTAGE,count(distinct(ctuscno)) NOS,\r\n"
					+ "SUM(Round(Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0))) Ob,\r\n" + "SUM(Mn_Kvah) Sales\r\n"
					+ ",SUM(round(Nvl(Cmd,0)+Nvl(Cclpc,0))) Demand,\r\n"
					+ "SUM(Nvl(round(CASE WHEN Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)>0 THEN \r\n"
					+ "CASE WHEN Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)>(NVL(Tot_Pay,0)) THEN (NVL(Tot_Pay,0)) ELSE Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0) END END),0)) COLL_ARREAR,\r\n"
					+ "SUM(Nvl(round(CASE WHEN Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)>0 THEN \r\n"
					+ "CASE WHEN Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)<(NVL(Tot_Pay,0)) THEN (NVL(Tot_Pay,0)-(Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0))) END ELSE (NVL(Tot_Pay,0)) END ),0)) COLL_DEMAND,\r\n"
					+ "SUM(round(Nvl(Tot_Pay,0))) Collection,SUM(round(Nvl(Rj_Oth,0)+Nvl(Drj,0)+Nvl(Rj_Cclpc,0))) Drj,SUM(round(Nvl(Crj,0))) Crj,\r\n"
					+ "SUM(round(Nvl(Cbtot,0)+Nvl(Cb_Oth,0)+Nvl(Cb_Cclpc,0))) Cb from ledger_ht_hist a,cons b\r\n"
					+ "where Mon_Year=?\r\n" + "and  A.Uscno=B.CTUscno AND SUBSTR(B.CTUscno,1,3) = ? \r\n" + deptcode
					+ " GROUP BY  b.CTACTUAL_KV\r\n" + "Order By b.CTACTUAL_KV";

			log.info(sql);
			return jdbcTemplate.queryForList(sql, new Object[] { monthYear, circle });

		}

	}

	public List<Map<String, Object>> getMSMEWiseAbstract(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		String monthYear = request.getParameter("month") + "-" + request.getParameter("year");
		String deptcode = request.getParameter("mnsetype").equals("ALL") ? ""
				: "  and TYPE_OF_SC='" + request.getParameter("mnsetype") + "'";
		if (circle.equalsIgnoreCase("ALL")) {

			String sql = "select CIRNAME,MANDALNAME,CTNAME Enterprise_Name ,TYPE_OF_SC Category ,STDESC POLICY_NAME, TIN_NO,CTUSCNO SERVICE_NO,NVL(PAN_NO,'') PAN_NO,NVL(CBTOT,0)+NVL(CB_CCLPC,0)+NVL(CB_OTH,0) Arrears\r\n"
					+ " from cons,V_LEDGER,MSME_SCS,SPDCLMASTER,servtype, (select USCNO P_USCNO ,NVL(PER_ID_NBR,'') PAN_NO from ID_TYPES where ID_TYPE_CD='PAN') PAN,\r\n"
					+ " (select USCNO T_USCNO ,NVL(PER_ID_NBR,'0') TIN_NO from ID_TYPES where ID_TYPE_CD='COM') TIN\r\n"
					+ " where  \r\n" + " CTUSCNO = USCNO \r\n" + " and USCNO = MSCNO(+)\r\n"
					+ " and USCNO = P_USCNO(+)\r\n" + "  and USCNO = T_USCNO(+)\r\n" + " and CTSERVTYPE = STCODE(+)\r\n"
					+ " and MON_YEAR = ?  \r\n" + " and MSME_FLAG='Y'\r\n" + "and substr(ctseccd,-5) =SECCD "
					+ deptcode;

			log.info(sql);
			return jdbcTemplate.queryForList(sql, new Object[] { monthYear });
		} else {
			String sql = "select CIRNAME,MANDALNAME,CTNAME Enterprise_Name ,TYPE_OF_SC Category ,STDESC POLICY_NAME, TIN_NO,CTUSCNO SERVICE_NO,NVL(PAN_NO,'') PAN_NO,NVL(CBTOT,0)+NVL(CB_CCLPC,0)+NVL(CB_OTH,0) Arrears\r\n"
					+ " from cons,V_LEDGER,MSME_SCS,SPDCLMASTER,servtype, (select USCNO P_USCNO ,NVL(PER_ID_NBR,'') PAN_NO from ID_TYPES where ID_TYPE_CD='PAN') PAN,\r\n"
					+ " (select USCNO T_USCNO ,NVL(PER_ID_NBR,'0') TIN_NO from ID_TYPES where ID_TYPE_CD='COM') TIN\r\n"
					+ " where  \r\n" + " CTUSCNO = USCNO \r\n" + " and USCNO = MSCNO(+)\r\n"
					+ " and USCNO = P_USCNO(+)\r\n" + "  and USCNO = T_USCNO(+)\r\n" + " and CTSERVTYPE = STCODE(+)\r\n"
					+ " and MON_YEAR = ?  \r\n" + " and MSME_FLAG='Y'\r\n" + " AND SUBSTR(CTUSCNO,1,3) = ?"
					+ "and substr(ctseccd,-5) =SECCD " + deptcode;

			log.info(sql);
			return jdbcTemplate.queryForList(sql, new Object[] { monthYear, circle });

		}

	}

	public List<Map<String, Object>> getMSMETYPEWiseAbstract(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		String monthYear = request.getParameter("month") + "-" + request.getParameter("year");
		String deptcode = request.getParameter("mnsetype").equals("ALL") ? ""
				: "  and TYPE_OF_SC='" + request.getParameter("mnsetype") + "'";
		if (circle.equalsIgnoreCase("ALL")) {

			String sql = "Select TYPE_OF_SC Category,STDESC POLICY_NAME,count(distinct(ctuscno)) NOS,\r\n"
					+ "SUM(Round(Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0))) Ob,\r\n" + "SUM(Mn_Kvah) Sales\r\n"
					+ ",SUM(round(Nvl(Cmd,0)+Nvl(Cclpc,0))) Demand,SUM(Nvl(round(CASE WHEN Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)>0 THEN CASE WHEN Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)>(NVL(Tot_Pay,0)) THEN (NVL(Tot_Pay,0)) ELSE Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0) END END),0)) COLL_ARREAR,\r\n"
					+ "SUM(Nvl(round(CASE WHEN Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)>0 THEN CASE WHEN Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)<(NVL(Tot_Pay,0)) THEN (NVL(Tot_Pay,0)-(Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0))) END ELSE (NVL(Tot_Pay,0)) END ),0)) COLL_DEMAND,SUM(round(Nvl(Tot_Pay,0))) Collection,SUM(round(Nvl(Rj_Oth,0)+Nvl(Drj,0)+Nvl(Rj_Cclpc,0))) Drj,SUM(round(Nvl(Crj,0))) Crj,SUM(round(Nvl(Cbtot,0)+Nvl(Cb_Oth,0)+Nvl(Cb_Cclpc,0))) Cb\r\n"
					+ "From (select LHH.*,'' STATUS_NEW, '' GOVT_PVT from Ledger_Ht_HIST LHH where Mon_Year=? union all select * from accountcopy where Mon_Year=?) A,CONS B,MSME_SCS,servtype C,spdclmaster D\r\n"
					+ "Where A.Uscno=B.CTUscno \r\n"
					+ "And b.ctservtype=c.stcode and USCNO = MSCNO(+) and MSME_FLAG='Y'\r\n" + deptcode
					+ " GROUP BY  TYPE_OF_SC,STDESC\r\n" + "Order By 1,2,3,4";

			log.info(sql);
			return jdbcTemplate.queryForList(sql, new Object[] { monthYear, monthYear });
		} else {
			String sql = "Select TYPE_OF_SC Category,STDESC POLICY_NAME,count(distinct(ctuscno)) NOS,\r\n"
					+ "SUM(Round(Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0))) Ob,\r\n" + "SUM(Mn_Kvah) Sales\r\n"
					+ ",SUM(round(Nvl(Cmd,0)+Nvl(Cclpc,0))) Demand,SUM(Nvl(round(CASE WHEN Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)>0 THEN CASE WHEN Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)>(NVL(Tot_Pay,0)) THEN (NVL(Tot_Pay,0)) ELSE Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0) END END),0)) COLL_ARREAR,\r\n"
					+ "SUM(Nvl(round(CASE WHEN Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)>0 THEN CASE WHEN Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)<(NVL(Tot_Pay,0)) THEN (NVL(Tot_Pay,0)-(Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0))) END ELSE (NVL(Tot_Pay,0)) END ),0)) COLL_DEMAND,SUM(round(Nvl(Tot_Pay,0))) Collection,SUM(round(Nvl(Rj_Oth,0)+Nvl(Drj,0)+Nvl(Rj_Cclpc,0))) Drj,SUM(round(Nvl(Crj,0))) Crj,SUM(round(Nvl(Cbtot,0)+Nvl(Cb_Oth,0)+Nvl(Cb_Cclpc,0))) Cb\r\n"
					+ "From (select LHH.*,'' STATUS_NEW, '' GOVT_PVT from Ledger_Ht_HIST LHH where Mon_Year=? union all select * from accountcopy where Mon_Year=?) A,CONS B,MSME_SCS,servtype C,spdclmaster D\r\n"
					+ "Where A.Uscno=B.CTUscno \r\n"
					+ "And b.ctservtype=c.stcode and USCNO = MSCNO(+) and MSME_FLAG='Y' AND SUBSTR(CTUSCNO,1,3) = ? \r\n"
					+ deptcode + " GROUP BY  TYPE_OF_SC,STDESC\r\n" + "Order By 1,2,3,4";

			log.info(sql);
			return jdbcTemplate.queryForList(sql, new Object[] { monthYear, monthYear, circle });

		}

	}

	public List<Map<String, Object>> getHODWiseAbstract(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		String monthYear = request.getParameter("month") + "-" + request.getParameter("year");
		String deptcode = request.getParameter("hoddept").equals("ALL") ? ""
				: "AND NVL(CTHODDEP,'OTH')='" + request.getParameter("hoddept") + "'";
		if (circle.equalsIgnoreCase("ALL")) {

			/*
			 * String sql="Select C.gmdep deptcode,C.dept_name,count(*) NOS,\r\n" +
			 * "SUM(Round(Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0))) Ob,\r\n" +
			 * "SUM(Mn_Kvah) Sales\r\n" + ",SUM(round(Nvl(Cmd,0)+Nvl(Cclpc,0))) Demand,"
			 * +"SUM(Nvl(round(CASE WHEN Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)>0 THEN CASE WHEN Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)>(NVL(Tot_Pay,0)) THEN (NVL(Tot_Pay,0)) ELSE Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0) END END),0)) COLL_ARREAR,\r\n"
			 * +
			 * "SUM(Nvl(round(CASE WHEN Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)>0 THEN CASE WHEN Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)<(NVL(Tot_Pay,0)) THEN (NVL(Tot_Pay,0)-(Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0))) END ELSE (NVL(Tot_Pay,0)) END ),0)) COLL_DEMAND,"
			 * +
			 * "SUM(round(Nvl(Tot_Pay,0))) Collection,SUM(round(Nvl(Rj_Oth,0)+Nvl(Drj,0)+Nvl(Rj_Cclpc,0))) Drj,SUM(round(Nvl(Crj,0))) Crj,SUM(round(Nvl(Cbtot,0)+Nvl(Cb_Oth,0)+Nvl(Cb_Cclpc,0))) Cb\r\n"
			 * +
			 * "From (select LHH.*,'' STATUS_NEW, '' GOVT_PVT from Ledger_Ht_HIST LHH where Mon_Year=?  union all select * from accountcopy where Mon_Year=? ) A,CONS B,SPDCL_HOD_HT C,spdclmaster D\r\n"
			 * + "Where A.Uscno=B.CTUscno And A.Uscno=C.Uscno \r\n" +
			 * "And substr(trim(B.ctseccd),-5)=D.seccd\r\n" + deptcode
			 * +" GROUP BY  C.gmdep ,C.dept_name\r\n" + "Order By 1,2,3,4 ";
			 */
			String sql = " select  NVL(CTHODDEP,'OTH')  deptcode,NVL(GMDEPTNAME,'Others') dept_name,count(*) NOS,\r\n"
					+ "SUM(Round(Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0))) Ob,\r\n" + "SUM((Mn_Kvah)) Sales,\r\n"
					+ "SUM(round(Nvl(Cmd,0)+Nvl(Cclpc,0))) Demand,\r\n"
					+ "SUM(Nvl(round(CASE WHEN Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)>0 THEN CASE WHEN Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)>(NVL(Tot_Pay,0)) THEN (NVL(Tot_Pay,0)) ELSE Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0) END END),0)) COLL_ARREAR,\r\n"
					+ "SUM(Nvl(round(CASE WHEN Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)>0 THEN CASE WHEN Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)<(NVL(Tot_Pay,0)) THEN (NVL(Tot_Pay,0)-(Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0))) END ELSE (NVL(Tot_Pay,0)) END ),0)) COLL_DEMAND,"
					+ "SUM(round(Nvl(Tot_Pay,0))) Collection,\r\n"
					+ "SUM(round(Nvl(Rj_Oth,0)+Nvl(Drj,0)+Nvl(Rj_Cclpc,0))) Drj,\r\n"
					+ "SUM(round(Nvl(Crj,0))) Crj,\r\n"
					+ "SUM(round(Nvl(Cbtot,0)+Nvl(Cb_Oth,0)+Nvl(Cb_Cclpc,0))) Cb\r\n" + "from cons B,\r\n"
					+ " (SELECT distinct GMTYPECODE, GMDEPTCODE,GMDEPTNAME FROM deptmast group by GMTYPECODE,GMDEPTCODE,GMDEPTNAME\r\n"
					+ "union all\r\n" + "select 'OTH' GMTYPECODE ,'OTH' GMDEPTCODE,'OTH' GMDEPTNAME from dual),\r\n"
					+ "(select LHH.*,'' STATUS_NEW, '' GOVT_PVT from Ledger_Ht_HIST LHH where Mon_Year=?  union all select * from accountcopy where Mon_Year=? ) A,spdclmaster D\r\n"
					+ "where B.CTUscno=A.Uscno  and CTHODDEP=GMDEPTCODE(+)  and DECODE(CTGOVT_PVT,'Y','GOVT','NON-GOVT')='GOVT' and CTHODTYPE=GMTYPECODE\r\n"
					+ "And substr(trim(ctseccd),-5)=D.seccd \r\n" + deptcode
					+ " GROUP BY NVL(CTHODDEP,'OTH') ,GMDEPTNAME\r\n" + "Order By NVL(CTHODDEP,'OTH') ,GMDEPTNAME";

			log.info(sql);
			return jdbcTemplate.queryForList(sql, new Object[] { monthYear, monthYear });
		} else {
			/*
			 * String sql="Select C.gmdep deptcode,C.dept_name,count(*) NOS,\r\n" +
			 * "SUM(Round(Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0))) Ob,\r\n" +
			 * "SUM(Mn_Kvah) Sales\r\n" + ",SUM(round(Nvl(Cmd,0)+Nvl(Cclpc,0))) Demand,"
			 * +"SUM(Nvl(round(CASE WHEN Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)>0 THEN CASE WHEN Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)>(NVL(Tot_Pay,0)) THEN (NVL(Tot_Pay,0)) ELSE Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0) END END),0)) COLL_ARREAR,\r\n"
			 * +
			 * "SUM(Nvl(round(CASE WHEN Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)>0 THEN CASE WHEN Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)<(NVL(Tot_Pay,0)) THEN (NVL(Tot_Pay,0)-(Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0))) END ELSE (NVL(Tot_Pay,0)) END ),0)) COLL_DEMAND,"
			 * +
			 * "SUM(round(Nvl(Tot_Pay,0))) Collection,SUM(round(Nvl(Rj_Oth,0)+Nvl(Drj,0)+Nvl(Rj_Cclpc,0))) Drj,SUM(round(Nvl(Crj,0))) Crj,SUM(round(Nvl(Cbtot,0)+Nvl(Cb_Oth,0)+Nvl(Cb_Cclpc,0))) Cb\r\n"
			 * +
			 * "From (select LHH.*,'' STATUS_NEW, '' GOVT_PVT from Ledger_Ht_HIST LHH where Mon_Year=?  union all select * from accountcopy where Mon_Year=? ) A,CONS B,SPDCL_HOD_HT C,spdclmaster D\r\n"
			 * + "Where A.Uscno=B.CTUscno And A.Uscno=C.Uscno \r\n" +
			 * "And substr(trim(B.ctseccd),-5)=D.seccd AND SUBSTR(A.Uscno,1,3) = ?\r\n" +
			 * deptcode +" GROUP BY  C.gmdep ,C.dept_name\r\n" + "Order By 1,2,3,4 ";
			 */

			String sql = " select  NVL(CTHODDEP,'OTH')  deptcode,NVL(GMDEPTNAME,'Others') dept_name,count(*) NOS,\r\n"
					+ "SUM(Round(Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0))) Ob,\r\n" + "SUM((Mn_Kvah)) Sales,\r\n"
					+ "SUM(round(Nvl(Cmd,0)+Nvl(Cclpc,0))) Demand,\r\n"
					+ "SUM(Nvl(round(CASE WHEN Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)>0 THEN CASE WHEN Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)>(NVL(Tot_Pay,0)) THEN (NVL(Tot_Pay,0)) ELSE Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0) END END),0)) COLL_ARREAR,\r\n"
					+ "SUM(Nvl(round(CASE WHEN Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)>0 THEN CASE WHEN Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0)<(NVL(Tot_Pay,0)) THEN (NVL(Tot_Pay,0)-(Nvl(Tot_Ob,0)+Nvl(Ob_Oth,0)+Nvl(Ob_Cclpc,0))) END ELSE (NVL(Tot_Pay,0)) END ),0)) COLL_DEMAND,"
					+ "SUM(round(Nvl(Tot_Pay,0))) Collection,\r\n"
					+ "SUM(round(Nvl(Rj_Oth,0)+Nvl(Drj,0)+Nvl(Rj_Cclpc,0))) Drj,\r\n"
					+ "SUM(round(Nvl(Crj,0))) Crj,\r\n"
					+ "SUM(round(Nvl(Cbtot,0)+Nvl(Cb_Oth,0)+Nvl(Cb_Cclpc,0))) Cb\r\n" + "from cons B,\r\n"
					+ " (SELECT distinct  GMDEPTCODE,GMDEPTNAME FROM deptmast group by GMDEPTCODE,GMDEPTNAME\r\n"
					+ "union all\r\n" + "select 'OTH' GMDEPTCODE,'OTH' GMDEPTNAME from dual),\r\n"
					+ "(select LHH.*,'' STATUS_NEW, '' GOVT_PVT from Ledger_Ht_HIST LHH where Mon_Year=?  union all select * from accountcopy where Mon_Year=? ) A,spdclmaster D\r\n"
					+ "where B.CTUscno=A.Uscno  and CTHODDEP=GMDEPTCODE(+)  and DECODE(CTGOVT_PVT,'Y','GOVT','NON-GOVT')='GOVT'\r\n"
					+ "And substr(trim(ctseccd),-5)=D.seccd  AND SUBSTR(A.Uscno,1,3) = ? \r\n" + deptcode
					+ " GROUP BY NVL(CTHODDEP,'OTH') ,GMDEPTNAME\r\n" + "Order By NVL(CTHODDEP,'OTH') ,GMDEPTNAME";

			log.info(sql);
			return jdbcTemplate.queryForList(sql, new Object[] { monthYear, monthYear, circle });

		}

	}

	public List<Map<String, Object>> getHODDemandSplit(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		String monthYear = request.getParameter("month") + "-" + request.getParameter("year");
		String deptcode = request.getParameter("hoddept").equals("ALL") ? ""
				: "AND C.GMDEP='" + request.getParameter("hoddept") + "'";

		if (!circle.equalsIgnoreCase("ALL")) {

			String sql = "Select D.CIRNAME CIRCLE,D.divname DIVISION,D.subname SUB_DIVISION,D.secname SECTION\r\n"
					+ ",A.Uscno,B.CtName,C.gmdeptcode deptcode,C.gmdeptname DEPT_NAME,C.GMSUBDEPTNAME Hod_Name\r\n"
					+ ",B.ctcat CAT,B.CTSUBCAT,CTHODTYPE HOD_TYPE,Decode(Status,'1','LIVE','0','BILL-STOP','') Status,\r\n"
					+ "(round(Mn_Kvah)) Sales,\r\n"
					+ "round(NVL(EC,0)) EC_CHG,round(NVL(ED,0)) ED_CHG,round(NVL(EDI,0)) EDINT_CHG,round(NVL(LPC,0)) SURCHARGE\r\n"
					+ ",round(Nvl(Cmd,0))+round(Nvl(Cclpc,0)) Demand\r\n"
					+ "From Ledger_Ht_HIST A,CONS B,deptmast C,spdclmaster D \r\n"
					+ "Where A.Uscno=B.CTUscno And  B.CTHODDEP=C.gmdeptcode and  B.CTHODSUBDEP=C.GMSUBDEPTCODE \r\n"
					+ "And substr(trim(B.ctseccd),-5)=D.seccd\r\n" + "And Mon_Year=? AND SUBSTR(A.Uscno,1,3)=? "
					+ deptcode + " \r\n" + "Order By D.CIRNAME ,D.divname ,D.subname ,D.secname,A.Uscno";
			log.info(sql);
			return jdbcTemplate.queryForList(sql, new Object[] { monthYear, circle });
		} else {
			String sql = "Select D.CIRNAME CIRCLE,D.divname DIVISION,D.subname SUB_DIVISION,D.secname SECTION\r\n"
					+ ",A.Uscno,B.CtName,C.gmdeptcode deptcode,C.gmdeptname DEPT_NAME,C.GMSUBDEPTNAME Hod_Name\r\n"
					+ ",B.ctcat CAT,B.CTSUBCAT,CTHODTYPE HOD_TYPE,Decode(Status,'1','LIVE','0','BILL-STOP','') Status,\r\n"
					+ "(round(Mn_Kvah)) Sales,\r\n"
					+ "round(NVL(EC,0)) EC_CHG,round(NVL(ED,0)) ED_CHG,round(NVL(EDI,0)) EDINT_CHG,round(NVL(LPC,0)) SURCHARGE\r\n"
					+ ",round(Nvl(Cmd,0))+round(Nvl(Cclpc,0)) Demand\r\n"
					+ "From Ledger_Ht_HIST A,CONS B,deptmast C,spdclmaster D\r\n"
					+ "Where A.Uscno=B.CTUscno And B.CTHODDEP=C.gmdeptcode and  B.CTHODSUBDEP=C.GMSUBDEPTCODE \r\n"
					+ "And substr(trim(B.ctseccd),-5)=D.seccd\r\n" + "And Mon_Year=? " + deptcode + " \r\n"
					+ "Order By D.CIRNAME ,D.divname ,D.subname ,D.secname,A.Uscno";

			log.info(sql);
			return jdbcTemplate.queryForList(sql, new Object[] { monthYear });
		}
	}

	public List<Map<String, Object>> getHODDemandAbstract(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		String monthYear = request.getParameter("month") + "-" + request.getParameter("year");
		String deptcode = request.getParameter("hoddept").equals("ALL") ? ""
				: "AND C.GMDEP='" + request.getParameter("hoddept") + "'";

		if (!circle.equalsIgnoreCase("ALL")) {

			String sql = " Select D.CIRNAME CIRCLE,D.divname DIVISION,D.subname SUB_DIVISION,D.secname SECTION\r\n"
					+ ",C.gmdeptcode deptcode,C.gmdeptname DEPT_NAME,\r\n" + "SUM(round(Mn_Kvah)) Sales,\r\n"
					+ "SUM(round(NVL(EC,0))) EC_CHG,SUM(round(NVL(ED,0))) ED_CHG,SUM(round(NVL(EDI,0))) EDINT_CHG,SUM(round(NVL(LPC,0))) SURCHARGE,SUM(round(NVL(CCLPC,0))) COURT_LPC\r\n"
					+ ",SUM(round(Nvl(Cmd,0))+round(Nvl(Cclpc,0))) Demand\r\n"
					+ "From Ledger_Ht_HIST A,CONS B,deptmast C,spdclmaster D\r\n"
					+ "Where A.Uscno=B.CTUscno And B.CTHODDEP=C.gmdeptcode and  B.CTHODSUBDEP=C.GMSUBDEPTCODE  \r\n"
					+ "And substr(trim(B.ctseccd),-5)=D.seccd\r\n" + "And Mon_Year=? AND SUBSTR(A.Uscno,1,3)=? "
					+ deptcode + " GROUP BY D.CIRNAME,D.divname,D.subname ,D.secname\r\n"
					+ ",C.gmdeptcode ,C.gmdeptname\r\n" + "Order By 1,2,3,4 ";
			log.info(sql);
			return jdbcTemplate.queryForList(sql, new Object[] { monthYear, circle });
		} else {
			String sql = " Select D.CIRNAME CIRCLE,D.divname DIVISION,D.subname SUB_DIVISION,D.secname SECTION\r\n"
					+ ",C.gmdeptcode deptcode,C.gmdeptname DEPT_NAME,\r\n" + "SUM(round(Mn_Kvah)) Sales,\r\n"
					+ "SUM(round(NVL(EC,0))) EC_CHG,SUM(round(NVL(ED,0))) ED_CHG,SUM(round(NVL(EDI,0))) EDINT_CHG,SUM(round(NVL(LPC,0))) SURCHARGE,SUM(round(NVL(CCLPC,0))) COURT_LPC\r\n"
					+ ",SUM(round(Nvl(Cmd,0))+round(Nvl(Cclpc,0))) Demand\r\n"
					+ "From Ledger_Ht_HIST A,CONS B,deptmast C,spdclmaster D\r\n"
					+ "Where A.Uscno=B.CTUscno And B.CTHODDEP=C.gmdeptcode and  B.CTHODSUBDEP=C.GMSUBDEPTCODE  \r\n"
					+ "And substr(trim(B.ctseccd),-5)=D.seccd\r\n" + "And Mon_Year=?  " + deptcode
					+ " GROUP BY D.CIRNAME,D.divname,D.subname ,D.secname\r\n" + ",C.gmdeptcode,C.gmdeptname\r\n"
					+ "Order By 1,2,3,4 ";

			log.info(sql);
			return jdbcTemplate.queryForList(sql, new Object[] { monthYear });
		}
	}

	public List<Map<String, Object>> getServicesWiseLedgerClosingBalance(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		String monthYear = request.getParameter("month") + "-" + request.getParameter("year");
		String status = request.getParameter("status");

		String constring = status.equals("A") ? " "
				: status.equals("L") ? " WHERE STATUS  = 'LIVE'   "
						: status.equals("B") ? " WHERE STATUS  = 'BSTOP'   " : " WHERE STATUS  = 'UDC'   ";

		List<Map<String, Object>> list = null;

		if (!circle.equalsIgnoreCase("ALL")) {

			String sql = "select * from ( select scno,LED_MONTH,CIRNAME,DIVNAME,SUBNAME,SECNAME,NAME,CAT,SUBCAT,LOAD,STATUS,VOLTAGE,BKVAH,OB_OTHERTHAN_COURT,OB_COURT,TOT_OB,DEMAND_WITHOUT_COURT,COURT_LPC,DRJ,CRJ,COURT_RJ,COLLECTION,CB_OTHERTHAN_COURT,CB_COURT, TOTAL_CB,CB_SD ,TYPE_OF_SERVICE,REC_KWH,REC_KVAH,to_char(CTSUPCONDT,'DD-MM-YYYY') CTSUPCONDT,to_char(BILL_STOP_DATE,'DD-MM-YYYY') BILL_STOP_DATE , to_char(DISMANTLE_DATE,'DD-MM-YYYY') DISMANTLE_DATE,CTLOCA_TYPE,"
					+ "(select to_char(max(pay_date),'DD-MM-YYYY') LAST_PAID_DATE from (select uscno , pay_date from PAY_HT_HIST union all select uscno , pay_date from PAY_HT)\r\n"
					+ "where SUBSTR(uscno,1,3) IN('GNT','VJA','ONG','CRD') and uscno=A.SCNo group by uscno)  LAST_PAID_DATE \r\n"
					+ "from (select MON_YEAR LED_MONTH,CIRCLE CIRNAME,DIVISION DIVNAME,SUBDIVISION SUBNAME,SECTION SECNAME,USCNO SCNO,NAM NAME,CTCat CAT,ctsubcat SUBCAT,LOAD,DECODE(TRIM(L.STATUS),'1',DECODE(TRIM(M.STAT_HT),'3','UDC','LIVE'),'BSTOP') STATUS, CTACTUAL_KV VOLTAGE,MN_KVAH BKVAH,\r\n"
					+ "TOT_OB OB_OTHERTHAN_COURT,(OB_OTH +OB_CCLPC) OB_COURT,(OB_OTH +OB_CCLPC+TOT_OB)TOT_OB,\r\n"
					+ "CMD DEMAND_WITHOUT_COURT,CCLPC COURT_LPC ,DRJ,CRJ,NVL(RJ_CCLPC,0)+NVL(RJ_OTH,0) COURT_RJ,TOT_PAY COLLECTION,\r\n"
					+ "CBTOT CB_OTHERTHAN_COURT,(CB_CCLPC+CB_OTH) CB_COURT,\r\n"
					+ "(CBTOT+CB_CCLPC+CB_OTH) TOTAL_CB,\r\n"
					+ "NVL(CB_SD,0)CB_SD ,DECODE(TRIM(CTGOVT_PVT),'Y','GOVT','N','NON-GOVT','') TYPE_OF_SERVICE,REC_KWH,REC_KVAH,CTSUPCONDT,\r\n"
					+ "case when CTLOCA_TYPE='C' then 'URBAN' when CTLOCA_TYPE='M' then 'URBAN' when CTLOCA_TYPE='P' then 'RURAL' else 'RURAL' end CTLOCA_TYPE"
					+ " from cons,ledger_ht_hist L,(SELECT MSCNO,min(MDCLKWHSTAT_HT) STAT_HT FROM MTRDAT_HIST WHERE SUBSTR(MSCNO,1,3) IN('GNT','VJA','ONG','CRD') and to_char(MDCLRDG_DT,'MON-YYYY')=?  group by MSCNO) m\r\n"
					+ "where  USCNO=CTUSCNO    AND CTUSCNO=MSCNO(+)  \r\n" + "UNION ALL\r\n"
					+ "select MON_YEAR LED_MONTH,CIRCLE CIRNAME,DIVISION DIVNAME,SUBDIVISION SUBNAME,SECTION SECNAME,USCNO SCNO,NAM NAME,CTCat CAT,ctsubcat SUBCAT,LOAD,DECODE(TRIM(L.STATUS),'1',DECODE(TRIM(M.STAT_HT),'3','UDC','LIVE'),'BSTOP') STATUS, CTACTUAL_KV VOLTAGE,MN_KVAH BKVAH,\r\n"
					+ "TOT_OB OB_OTHERTHAN_COURT,(OB_OTH +OB_CCLPC) OB_COURT,(OB_OTH +OB_CCLPC+TOT_OB)TOT_OB,\r\n"
					+ "CMD DEMAND_WITHOUT_COURT,CCLPC COURT_LPC ,DRJ,CRJ,NVL(RJ_CCLPC,0)+NVL(RJ_OTH,0) COURT_RJ,TOT_PAY COLLECTION,\r\n"
					+ "CBTOT CB_OTHERTHAN_COURT,(CB_CCLPC+CB_OTH) CB_COURT,\r\n"
					+ "(CBTOT+CB_CCLPC+CB_OTH) TOTAL_CB,\r\n"
					+ "NVL(CB_SD,0)CB_SD ,DECODE(TRIM(CTGOVT_PVT),'Y','GOVT','N','NON-GOVT','') TYPE_OF_SERVICE,REC_KWH,REC_KVAH,CTSUPCONDT,\r\n"
					+ "case when CTLOCA_TYPE='C' then 'URBAN' when CTLOCA_TYPE='M' then 'URBAN' when CTLOCA_TYPE='P' then 'RURAL' else 'RURAL' end CTLOCA_TYPE"
					+ " from cons,ACCOUNTCOPY L,(SELECT MSCNO,min(MDCLKWHSTAT_HT) STAT_HT FROM MTRDAT WHERE SUBSTR(MSCNO,1,3) IN('GNT','VJA','ONG','CRD') and to_char(MDCLRDG_DT,'MON-YYYY')=? group by MSCNO) m\r\n"
					+ "where  USCNO=CTUSCNO    AND CTUSCNO=MSCNO(+) \r\n" + " UNION ALL\r\n"
					+ " select MON_YEAR LED_MONTH,SUBSTR(CTUSCNO,1,3) CIRNAME,DIVISION DIVNAME,SUBDIVISION SUBNAME,SECTION SECNAME,CTUSCNO SCNO,NAM NAME,CTCat CAT,ctsubcat SUBCAT,LOAD,DECODE(TRIM(L.STATUS),'1',DECODE(TRIM(M.STAT_HT),'3','UDC','LIVE'),'BSTOP') STATUS, CTACTUAL_KV VOLTAGE,MN_KVAH BKVAH,\r\n"
					+ "TOT_OB OB_OTHERTHAN_COURT,(OB_OTH +OB_CCLPC) OB_COURT,(OB_OTH +OB_CCLPC+TOT_OB)TOT_OB,\r\n"
					+ "CMD DEMAND_WITHOUT_COURT,CCLPC COURT_LPC ,DRJ,CRJ,NVL(RJ_CCLPC,0)+NVL(RJ_OTH,0) COURT_RJ,TOT_PAY COLLECTION,\r\n"
					+ "CBTOT CB_OTHERTHAN_COURT,(CB_CCLPC+CB_OTH) CB_COURT,\r\n"
					+ "(CBTOT+CB_CCLPC+CB_OTH) TOTAL_CB,\r\n"
					+ "NVL(CB_SD,0)CB_SD ,DECODE(TRIM(CTGOVT_PVT),'Y','GOVT','N','NON-GOVT','') TYPE_OF_SERVICE,REC_KWH,REC_KVAH,CTSUPCONDT\r\n"
					+ ", case when CTLOCA_TYPE='C' then 'URBAN' when CTLOCA_TYPE='M' then 'URBAN' when CTLOCA_TYPE='P' then 'RURAL' else 'RURAL' end CTLOCA_TYPE "
					+ " from cons,ledger_ht_hist L,(SELECT MSCNO,min(MDCLKWHSTAT_HT) STAT_HT FROM MTRDAT_HIST WHERE SUBSTR(MSCNO,1,3) IN('GNT','VJA','ONG','CRD')  and to_char(MDCLRDG_DT,'MON-YYYY')=?  group by MSCNO ) m\r\n"
					+ "where  SUBSTR(USCNO,4)=SUBSTR(CTUSCNO,4)   AND USCNO=MSCNO(+)   AND USCNO IN( select UNIQUE USCNO from ledger_ht_hist,cons where substr(uscno,4)=substr(ctuscno,4) and ctname=nam AND USCNO LIKE 'GNT%' AND CTUSCNO LIKE 'CRD%')\r\n"
					+ "UNION ALL \r\n"
					+ "  select MON_YEAR LED_MONTH,SUBSTR(CTUSCNO,1,3) CIRNAME,DIVISION DIVNAME,SUBDIVISION SUBNAME,SECTION SECNAME,CTUSCNO SCNO,NAM NAME,CTCat CAT,ctsubcat SUBCAT,LOAD,DECODE(TRIM(L.STATUS),'1',DECODE(TRIM(M.STAT_HT),'3','UDC','LIVE'),'BSTOP') STATUS, CTACTUAL_KV VOLTAGE,MN_KVAH BKVAH,\r\n"
					+ "TOT_OB OB_OTHERTHAN_COURT,(OB_OTH +OB_CCLPC) OB_COURT,(OB_OTH +OB_CCLPC+TOT_OB)TOT_OB,\r\n"
					+ "CMD DEMAND_WITHOUT_COURT,CCLPC COURT_LPC ,DRJ,CRJ,NVL(RJ_CCLPC,0)+NVL(RJ_OTH,0) COURT_RJ,TOT_PAY COLLECTION,\r\n"
					+ "CBTOT CB_OTHERTHAN_COURT,(CB_CCLPC+CB_OTH) CB_COURT,\r\n"
					+ "(CBTOT+CB_CCLPC+CB_OTH) TOTAL_CB,\r\n"
					+ "NVL(CB_SD,0)CB_SD ,DECODE(TRIM(CTGOVT_PVT),'Y','GOVT','N','NON-GOVT','') TYPE_OF_SERVICE,REC_KWH,REC_KVAH,CTSUPCONDT\r\n,"
					+ "case when CTLOCA_TYPE='C' then 'URBAN' when CTLOCA_TYPE='M' then 'URBAN' when CTLOCA_TYPE='P' then 'RURAL' else 'RURAL' end CTLOCA_TYPE"
					+ " from cons,ACCOUNTCOPY L,(SELECT MSCNO,min(MDCLKWHSTAT_HT) STAT_HT FROM MTRDAT WHERE SUBSTR(MSCNO,1,3) IN('GNT','VJA','ONG','CRD') and to_char(MDCLRDG_DT,'MON-YYYY')=?   group by MSCNO ) m\r\n"
					+ "where  SUBSTR(USCNO,4)=SUBSTR(CTUSCNO,4)   AND USCNO=MSCNO(+)   AND USCNO IN( select UNIQUE USCNO from ledger_ht_hist,cons where substr(uscno,4)=substr(ctuscno,4) and ctname=nam AND USCNO LIKE 'GNT%' AND CTUSCNO LIKE 'CRD%')\r\n"
					+ " ) A ,( select uscno,bill_stop_date,DISMANTLE_DATE from\r\n"
					+ "(select uscno,min(to_date(MON_YEAR,'MON-YYYY')) BILL_STOP_DATE from ledger_ht_hist where status in ('0','BILLSTOP','STP') and substr(uscno,1,3) in ('VJA','CRD','ONG','GNT')\r\n"
					+ "and uscno not in (select ctuscno from cons where ctstatus=1)\r\n" + "group by uscno),\r\n"
					+ "(select ctuscno,DISMANTLE_DATE from cons_flags where DISMANTLE_DATE is not null and substr(ctuscno,1,3) in ('VJA','CRD','ONG','GNT'))\r\n"
					+ "where uscno=ctuscno(+)) B where A.SCNo=B.uscno(+) AND A.LED_MONTH=? and SUBSTR(A.SCNO,1,3)=?  ORDER BY A.SCNO,TO_DATE(A.LED_MONTH,'MON-YYYY') ) "
					+ constring;
			list = jdbcTemplate.queryForList(sql,
					new Object[] { monthYear, monthYear, monthYear, monthYear, monthYear, circle });
		} else {

			String sql = "select * from (  select scno,LED_MONTH,CIRNAME,DIVNAME,SUBNAME,SECNAME,NAME,CAT,SUBCAT,LOAD,STATUS,VOLTAGE,BKVAH,OB_OTHERTHAN_COURT,OB_COURT,TOT_OB,DEMAND_WITHOUT_COURT,COURT_LPC,DRJ,CRJ,COURT_RJ,COLLECTION,CB_OTHERTHAN_COURT,CB_COURT, TOTAL_CB,CB_SD ,TYPE_OF_SERVICE,REC_KWH,REC_KVAH,to_char(CTSUPCONDT,'DD-MM-YYYY') CTSUPCONDT,to_char(BILL_STOP_DATE,'DD-MM-YYYY') BILL_STOP_DATE , to_char(DISMANTLE_DATE,'DD-MM-YYYY') DISMANTLE_DATE  ,CTLOCA_TYPE,"
					+ "(select to_char(max(pay_date),'DD-MM-YYYY') LAST_PAID_DATE from (select uscno , pay_date from PAY_HT_HIST union all select uscno , pay_date from PAY_HT)\r\n"
					+ "where SUBSTR(uscno,1,3) IN('GNT','VJA','ONG','CRD') and uscno=A.scno group by uscno) LAST_PAID_DATE \r\n"
					+ "from (select MON_YEAR LED_MONTH,CIRCLE CIRNAME,DIVISION DIVNAME,SUBDIVISION SUBNAME,SECTION SECNAME,USCNO SCNO,NAM NAME,CTCat CAT,ctsubcat SUBCAT,LOAD,DECODE(TRIM(L.STATUS),'1',DECODE(TRIM(M.STAT_HT),'3','UDC','LIVE'),'BSTOP') STATUS, CTACTUAL_KV VOLTAGE,MN_KVAH BKVAH,\r\n"
					+ "TOT_OB OB_OTHERTHAN_COURT,(OB_OTH +OB_CCLPC) OB_COURT,(OB_OTH +OB_CCLPC+TOT_OB)TOT_OB,\r\n"
					+ "CMD DEMAND_WITHOUT_COURT,CCLPC COURT_LPC ,DRJ,CRJ,NVL(RJ_CCLPC,0)+NVL(RJ_OTH,0) COURT_RJ,TOT_PAY COLLECTION,\r\n"
					+ "CBTOT CB_OTHERTHAN_COURT,(CB_CCLPC+CB_OTH) CB_COURT,\r\n"
					+ "(CBTOT+CB_CCLPC+CB_OTH) TOTAL_CB,\r\n"
					+ "NVL(CB_SD,0)CB_SD ,DECODE(TRIM(CTGOVT_PVT),'Y','GOVT','N','NON-GOVT','') TYPE_OF_SERVICE,REC_KWH,REC_KVAH,CTSUPCONDT,\r\n"
					+ "case when CTLOCA_TYPE='C' then 'URBAN' when CTLOCA_TYPE='M' then 'URBAN' when CTLOCA_TYPE='P' then 'RURAL' else 'RURAL' end CTLOCA_TYPE"
					+ " from cons,ledger_ht_hist L,(SELECT MSCNO,min(MDCLKWHSTAT_HT) STAT_HT FROM MTRDAT_HIST WHERE SUBSTR(MSCNO,1,3) IN('GNT','VJA','ONG','CRD') and to_char(MDCLRDG_DT,'MON-YYYY')=?  group by MSCNO) m\r\n"
					+ "where  USCNO=CTUSCNO    AND CTUSCNO=MSCNO(+)  \r\n" + "UNION ALL\r\n"
					+ "select MON_YEAR LED_MONTH,CIRCLE CIRNAME,DIVISION DIVNAME,SUBDIVISION SUBNAME,SECTION SECNAME,USCNO SCNO,NAM NAME,CTCat CAT,ctsubcat SUBCAT,LOAD,DECODE(TRIM(L.STATUS),'1',DECODE(TRIM(M.STAT_HT),'3','UDC','LIVE'),'BSTOP') STATUS, CTACTUAL_KV VOLTAGE,MN_KVAH BKVAH,\r\n"
					+ "TOT_OB OB_OTHERTHAN_COURT,(OB_OTH +OB_CCLPC) OB_COURT,(OB_OTH +OB_CCLPC+TOT_OB)TOT_OB,\r\n"
					+ "CMD DEMAND_WITHOUT_COURT,CCLPC COURT_LPC ,DRJ,CRJ,NVL(RJ_CCLPC,0)+NVL(RJ_OTH,0) COURT_RJ,TOT_PAY COLLECTION,\r\n"
					+ "CBTOT CB_OTHERTHAN_COURT,(CB_CCLPC+CB_OTH) CB_COURT,\r\n"
					+ "(CBTOT+CB_CCLPC+CB_OTH) TOTAL_CB,\r\n"
					+ "NVL(CB_SD,0)CB_SD ,DECODE(TRIM(CTGOVT_PVT),'Y','GOVT','N','NON-GOVT','') TYPE_OF_SERVICE,REC_KWH,REC_KVAH,CTSUPCONDT,\r\n"
					+ "case when CTLOCA_TYPE='C' then 'URBAN' when CTLOCA_TYPE='M' then 'URBAN' when CTLOCA_TYPE='P' then 'RURAL' else 'RURAL' end CTLOCA_TYPE "
					+ " from cons,ACCOUNTCOPY L,(SELECT MSCNO,min(MDCLKWHSTAT_HT) STAT_HT FROM MTRDAT WHERE SUBSTR(MSCNO,1,3) IN('GNT','VJA','ONG','CRD') and to_char(MDCLRDG_DT,'MON-YYYY')=? group by MSCNO) m\r\n"
					+ "where  USCNO=CTUSCNO    AND CTUSCNO=MSCNO(+) \r\n" + " UNION ALL\r\n"
					+ " select MON_YEAR LED_MONTH,SUBSTR(CTUSCNO,1,3) CIRNAME,DIVISION DIVNAME,SUBDIVISION SUBNAME,SECTION SECNAME,CTUSCNO SCNO,NAM NAME,CTCat CAT,ctsubcat SUBCAT,LOAD,DECODE(TRIM(L.STATUS),'1',DECODE(TRIM(M.STAT_HT),'3','UDC','LIVE'),'BSTOP') STATUS, CTACTUAL_KV VOLTAGE,MN_KVAH BKVAH,\r\n"
					+ "TOT_OB OB_OTHERTHAN_COURT,(OB_OTH +OB_CCLPC) OB_COURT,(OB_OTH +OB_CCLPC+TOT_OB)TOT_OB,\r\n"
					+ "CMD DEMAND_WITHOUT_COURT,CCLPC COURT_LPC ,DRJ,CRJ,NVL(RJ_CCLPC,0)+NVL(RJ_OTH,0) COURT_RJ,TOT_PAY COLLECTION,\r\n"
					+ "CBTOT CB_OTHERTHAN_COURT,(CB_CCLPC+CB_OTH) CB_COURT,\r\n"
					+ "(CBTOT+CB_CCLPC+CB_OTH) TOTAL_CB,\r\n"
					+ "NVL(CB_SD,0)CB_SD ,DECODE(TRIM(CTGOVT_PVT),'Y','GOVT','N','NON-GOVT','') TYPE_OF_SERVICE,REC_KWH,REC_KVAH,CTSUPCONDT\r\n,"
					+ "case when CTLOCA_TYPE='C' then 'URBAN' when CTLOCA_TYPE='M' then 'URBAN' when CTLOCA_TYPE='P' then 'RURAL' else 'RURAL' end CTLOCA_TYPE"
					+ " from cons,ledger_ht_hist L,(SELECT MSCNO,min(MDCLKWHSTAT_HT) STAT_HT FROM MTRDAT_HIST WHERE SUBSTR(MSCNO,1,3) IN('GNT','VJA','ONG','CRD')  and to_char(MDCLRDG_DT,'MON-YYYY')=?  group by MSCNO ) m\r\n"
					+ "where  SUBSTR(USCNO,4)=SUBSTR(CTUSCNO,4)   AND USCNO=MSCNO(+)   AND USCNO IN( select UNIQUE USCNO from ledger_ht_hist,cons where substr(uscno,4)=substr(ctuscno,4) and ctname=nam AND USCNO LIKE 'GNT%' AND CTUSCNO LIKE 'CRD%')\r\n"
					+ "UNION ALL \r\n"
					+ "  select MON_YEAR LED_MONTH,SUBSTR(CTUSCNO,1,3) CIRNAME,DIVISION DIVNAME,SUBDIVISION SUBNAME,SECTION SECNAME,CTUSCNO SCNO,NAM NAME,CTCat CAT,ctsubcat SUBCAT,LOAD,DECODE(TRIM(L.STATUS),'1',DECODE(TRIM(M.STAT_HT),'3','UDC','LIVE'),'BSTOP') STATUS, CTACTUAL_KV VOLTAGE,MN_KVAH BKVAH,\r\n"
					+ "TOT_OB OB_OTHERTHAN_COURT,(OB_OTH +OB_CCLPC) OB_COURT,(OB_OTH +OB_CCLPC+TOT_OB)TOT_OB,\r\n"
					+ "CMD DEMAND_WITHOUT_COURT,CCLPC COURT_LPC ,DRJ,CRJ,NVL(RJ_CCLPC,0)+NVL(RJ_OTH,0) COURT_RJ,TOT_PAY COLLECTION,\r\n"
					+ "CBTOT CB_OTHERTHAN_COURT,(CB_CCLPC+CB_OTH) CB_COURT,\r\n"
					+ "(CBTOT+CB_CCLPC+CB_OTH) TOTAL_CB,\r\n"
					+ "NVL(CB_SD,0)CB_SD ,DECODE(TRIM(CTGOVT_PVT),'Y','GOVT','N','NON-GOVT','') TYPE_OF_SERVICE,REC_KWH,REC_KVAH,CTSUPCONDT\r\n,"
					+ "case when CTLOCA_TYPE='C' then 'URBAN' when CTLOCA_TYPE='M' then 'URBAN' when CTLOCA_TYPE='P' then 'RURAL' else 'RURAL' end CTLOCA_TYPE "
					+ " from cons,ACCOUNTCOPY L,(SELECT MSCNO,min(MDCLKWHSTAT_HT) STAT_HT FROM MTRDAT WHERE SUBSTR(MSCNO,1,3) IN('GNT','VJA','ONG','CRD') and to_char(MDCLRDG_DT,'MON-YYYY')=?   group by MSCNO ) m\r\n"
					+ "where  SUBSTR(USCNO,4)=SUBSTR(CTUSCNO,4)   AND USCNO=MSCNO(+)   AND USCNO IN( select UNIQUE USCNO from ledger_ht_hist,cons where substr(uscno,4)=substr(ctuscno,4) and ctname=nam AND USCNO LIKE 'GNT%' AND CTUSCNO LIKE 'CRD%')\r\n"
					+ " ) A ,(select uscno,bill_stop_date,DISMANTLE_DATE from\r\n"
					+ "(select uscno,min(to_date(MON_YEAR,'MON-YYYY')) BILL_STOP_DATE from ledger_ht_hist where status in ('0','BILLSTOP','STP') and substr(uscno,1,3) in ('VJA','CRD','ONG','GNT')\r\n"
					+ "and uscno not in (select ctuscno from cons where ctstatus=1)\r\n" + "group by uscno),\r\n"
					+ "(select ctuscno,DISMANTLE_DATE from cons_flags where DISMANTLE_DATE is not null and substr(ctuscno,1,3) in ('VJA','CRD','ONG','GNT'))\r\n"
					+ "where uscno=ctuscno(+)) B where A.SCNo=B.uscno(+) AND A.LED_MONTH=?   ORDER BY SCNO,TO_DATE(A.LED_MONTH,'MON-YYYY')) "
					+ constring;
			log.info(sql);
			list = jdbcTemplate.queryForList(sql,
					new Object[] { monthYear, monthYear, monthYear, monthYear, monthYear });
		}

		return list;
	}

	public List<Map<String, Object>> getLedgerWiseAbstract(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		String monthYear = request.getParameter("month") + "-" + request.getParameter("year");
		log.info(circle + "-" + monthYear);
		if (!circle.equalsIgnoreCase("ALL")) {
			/*
			 * select CIRCLE
			 * CIRNAME,DECODE(TRIM(L.STATUS),'1',DECODE(TRIM(M.STAT_HT),'3','UDC','LIVE'),'
			 * BSTOP') STATUS,COUNT(*) NOS, SUM(NVL(TOT_OB,0)) OB_OTHERTHAN_COURT,
			 * SUM(NVL(OB_OTH,0) +NVL(OB_CCLPC,0)) OB_COURT, SUM(NVL(OB_OTH,0)
			 * +NVL(OB_CCLPC,0)+NVL(TOT_OB,0)) TOT_OB, SUM(NVL(CMD,0)) DEMAND_WITHOUT_COURT,
			 * SUM(NVL(CCLPC,0)) COURT_LPC , SUM(NVL(DRJ,0)) DR_RJ, SUM(NVL(CRJ,0)) CR_RJ,
			 * SUM(NVL(RJ_CCLPC,0)+NVL(RJ_OTH,0)) COURT_RJ, SUM(NVL(TOT_PAY,0)) COLLECTION,
			 * SUM(NVL(CBTOT,0)) CB_OTHERTHAN_COURT, SUM(NVL(CB_CCLPC,0)+NVL(CB_OTH,0))
			 * CB_COURT, SUM(NVL(CBTOT,0)+NVL(CB_CCLPC,0)+NVL(CB_OTH,0)) TOTAL_CB
			 * SUM(NVL(CB_SD,0)) CB_SD from cons,ledger_ht_hist L,(SELECT
			 * MSCNO,MDCLKWHSTAT_HT STAT_HT FROM MTRDAT_HIST WHERE
			 * to_char(MDCLRDG_DT,'MON-YYYY')='JAN-2021' GROUP BY MSCNO,MDCLKWHSTAT_HT) M
			 * where USCNO=CTUSCNO AND CTUSCNO=M.MSCNO(+) and SUBSTR(CTUSCNO,1,3)
			 * IN('GNT','VJA','ONG','CRD') and MON_YEAR = 'JAN-2021' GROUP BY
			 * CIRCLE,DECODE(TRIM(L.STATUS),'1',DECODE(TRIM(M.STAT_HT),'3','UDC','LIVE'),'
			 * BSTOP') order by cirname ;
			 */
			String sql = "select * from (select cir,status  ,round(sum(USCNO_COUNT)) USCNO_COUNT ,round(sum(OB_OTHERTHAN_COURT)) OB_OTHERTHAN_COURT,round(sum(OB_COURT)) OB_COURT,round(sum(TOT_OB)) TOT_OB,\r\n"
					+ "round(sum(DEMAND_WITHOUT_COURT)) DEMAND_WITHOUT_COURT ,round(sum(COURT_LPC)) COURT_LPC,round(sum(DR_RJ)) DR_RJ,round(sum(CR_RJ)) CR_RJ,round(sum(COURT_RJ)) COURT_RJ ,round(sum(COLLECTION)) COLLECTION, \r\n"
					+ "round(sum(CB_OTHERTHAN_COURT)) CB_OTHERTHAN_COURT ,round(sum(CB_COURT)) CB_COURT ,round(sum(TOTAL_CB)) TOTAL_CB,round(sum(CB_SD)) CB_SD\r\n"
					+ "from (\r\n"
					+ "select substr(ctuscno,1,3) cir,count(ctuscno) USCNO_COUNT ,DECODE(TRIM(L.STATUS),'1',DECODE(TRIM(M.STAT_HT),'3','UDC','LIVE'),'BSTOP') STATUS,\r\n"
					+ "SUM(NVL(TOT_OB,0)) OB_OTHERTHAN_COURT,\r\n" + "SUM(NVL(OB_OTH,0) +NVL(OB_CCLPC,0)) OB_COURT,\r\n"
					+ "SUM(NVL(OB_OTH,0) +NVL(OB_CCLPC,0)+NVL(TOT_OB,0)) TOT_OB,\r\n"
					+ "SUM(NVL(CMD,0)) DEMAND_WITHOUT_COURT,\r\n" + "SUM(NVL(CCLPC,0)) COURT_LPC ,\r\n"
					+ "SUM(NVL(DRJ,0)) DR_RJ,\r\n" + "SUM(NVL(CRJ,0)) CR_RJ,\r\n"
					+ "SUM(NVL(RJ_CCLPC,0)+NVL(RJ_OTH,0)) COURT_RJ,\r\n" + "SUM(NVL(TOT_PAY,0)) COLLECTION,\r\n"
					+ "SUM(NVL(CBTOT,0)) CB_OTHERTHAN_COURT,\r\n" + "SUM(NVL(CB_CCLPC,0)+NVL(CB_OTH,0)) CB_COURT,\r\n"
					+ "SUM(NVL(CBTOT,0)+NVL(CB_CCLPC,0)+NVL(CB_OTH,0)) TOTAL_CB,\r\n" + "SUM(NVL(CB_SD,0)) CB_SD \r\n"
					+ "from cons,ledger_ht_hist L,(SELECT MSCNO,min(MDCLKWHSTAT_HT) STAT_HT FROM MTRDAT_HIST WHERE to_char(MDCLRDG_DT,'MON-YYYY')=? GROUP BY MSCNO ) M \r\n"
					+ "where  USCNO=CTUSCNO   AND CTUSCNO=M.MSCNO(+) and SUBSTR(CTUSCNO,1,3) IN(?)\r\n"
					+ "and MON_YEAR = ? GROUP BY substr(ctuscno,1,3),DECODE(TRIM(L.STATUS),'1',DECODE(TRIM(M.STAT_HT),'3','UDC','LIVE'),'BSTOP') ORDER BY CIR,STATUS)\r\n"
					+ "GROUP BY CUBE (cir,status)\r\n" + "ORDER BY cir,status) where cir is not null\r\n ";
			log.info(sql);
			return jdbcTemplate.queryForList(sql, new Object[] { monthYear, circle, monthYear });
		} else {
			/*
			 * String
			 * sql="SELECT MON_YEAR,CIRCLE CIRNAME,SECTION SECNAME,SUBDIVISION SUBNAME,DIVISION DIVNAME,USCNO,NAM,CAT,SCAT,MN_KVAH BKVAH,\r\n"
			 * +
			 * "TOT_OB OB_OTHERTHAN_COURT,(OB_OTH +OB_CCLPC) OB_COURT,(OB_OTH +OB_CCLPC+TOT_OB)TOT_OB,\r\n"
			 * +
			 * "CMD DEMAND_WITHOUT_COURT,CCLPC COURT_LPC ,DRJ,CRJ,RJ_CCLPC COURT_RJ,TOT_PAY COLLECTION,\r\n"
			 * + "CBTOT CB_OTHERTHAN_COURT,(CB_CCLPC+CB_OTH) CB_COURT,\r\n" +
			 * "(CBTOT+CB_CCLPC+CB_OTH) TOTAL_CB,\r\n" +
			 * "DECODE(TRIM(CTSTATUS),1,'LIVE',0,'STOP',15,'DISMANTLED',16,'HT_TO_LT','UDC')STAUS,\r\n"
			 * +
			 * "DECODE(TRIM(CTGOVT_PVT),'Y','GOVT','N','NON-GOVT','') TYPE_OF_SERVICE,NVL(CB_SD,0)CB_SD \r\n"
			 * + "FROM  ledger_ht_hist,cons_flags\r\n" +
			 * "WHERE USCNO=CTUSCNO  AND MON_YEAR=? \r\n" + "UNION ALL\r\n" +
			 * "SELECT MON_YEAR,CIRCLE CIRNAME,SECTION SECNAME,SUBDIVISION SUBNAME,DIVISION DIVNAME,USCNO,NAM,CAT,SCAT,MN_KVAH BKVAH,\r\n"
			 * +
			 * "TOT_OB OB_OTHERTHAN_COURT,(OB_OTH +OB_CCLPC) OB_COURT,(OB_OTH +OB_CCLPC+TOT_OB)TOT_OB,\r\n"
			 * +
			 * "CMD DEMAND_WITHOUT_COURT,CCLPC COURT_LPC ,DRJ,CRJ,RJ_CCLPC COURT_RJ,TOT_PAY COLLECTION,\r\n"
			 * + "CBTOT CB_OTHERTHAN_COURT,(CB_CCLPC+CB_OTH) CB_COURT,\r\n" +
			 * "(CBTOT+CB_CCLPC+CB_OTH) TOTAL_CB,\r\n" +
			 * "DECODE(TRIM(CTSTATUS),1,'LIVE',0,'STOP',15,'DISMANTLED',16,'HT_TO_LT','UDC')STAUS,\r\n"
			 * +
			 * "DECODE(TRIM(CTGOVT_PVT),'Y','GOVT','N','NON-GOVT','') TYPE_OF_SERVICE,NVL(CB_SD,0)CB_SD \r\n"
			 * + "FROM ACCOUNTCOPY,cons_flags\r\n" +
			 * "WHERE USCNO=CTUSCNO  AND MON_YEAR=? ORDER BY CIRNAME";
			 */
			String sql = "select * from (select cir,status  ,round(sum(USCNO_COUNT)) USCNO_COUNT ,round(sum(OB_OTHERTHAN_COURT)) OB_OTHERTHAN_COURT,round(sum(OB_COURT)) OB_COURT,round(sum(TOT_OB)) TOT_OB,\r\n"
					+ "round(sum(DEMAND_WITHOUT_COURT)) DEMAND_WITHOUT_COURT ,round(sum(COURT_LPC)) COURT_LPC,round(sum(DR_RJ)) DR_RJ,round(sum(CR_RJ)) CR_RJ,round(sum(COURT_RJ)) COURT_RJ ,round(sum(COLLECTION)) COLLECTION, \r\n"
					+ "round(sum(CB_OTHERTHAN_COURT)) CB_OTHERTHAN_COURT ,round(sum(CB_COURT)) CB_COURT ,round(sum(TOTAL_CB)) TOTAL_CB,round(sum(CB_SD)) CB_SD\r\n"
					+ "from (\r\n"
					+ "select substr(ctuscno,1,3) cir,count(ctuscno) USCNO_COUNT ,DECODE(TRIM(L.STATUS),'1',DECODE(TRIM(M.STAT_HT),'3','UDC','LIVE'),'BSTOP') STATUS,\r\n"
					+ "SUM(NVL(TOT_OB,0)) OB_OTHERTHAN_COURT,\r\n" + "SUM(NVL(OB_OTH,0) +NVL(OB_CCLPC,0)) OB_COURT,\r\n"
					+ "SUM(NVL(OB_OTH,0) +NVL(OB_CCLPC,0)+NVL(TOT_OB,0)) TOT_OB,\r\n"
					+ "SUM(NVL(CMD,0)) DEMAND_WITHOUT_COURT,\r\n" + "SUM(NVL(CCLPC,0)) COURT_LPC ,\r\n"
					+ "SUM(NVL(DRJ,0)) DR_RJ,\r\n" + "SUM(NVL(CRJ,0)) CR_RJ,\r\n"
					+ "SUM(NVL(RJ_CCLPC,0)+NVL(RJ_OTH,0)) COURT_RJ,\r\n" + "SUM(NVL(TOT_PAY,0)) COLLECTION,\r\n"
					+ "SUM(NVL(CBTOT,0)) CB_OTHERTHAN_COURT,\r\n" + "SUM(NVL(CB_CCLPC,0)+NVL(CB_OTH,0)) CB_COURT,\r\n"
					+ "SUM(NVL(CBTOT,0)+NVL(CB_CCLPC,0)+NVL(CB_OTH,0)) TOTAL_CB,\r\n" + "SUM(NVL(CB_SD,0)) CB_SD \r\n"
					+ "from cons,ledger_ht_hist L,(SELECT MSCNO,min(MDCLKWHSTAT_HT) STAT_HT FROM MTRDAT_HIST WHERE to_char(MDCLRDG_DT,'MON-YYYY')=? GROUP BY MSCNO                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 ) M \r\n"
					+ "where  USCNO=CTUSCNO   AND CTUSCNO=M.MSCNO(+) and SUBSTR(CTUSCNO,1,3) IN('GNT','VJA','ONG','CRD')\r\n"
					+ "and MON_YEAR = ? GROUP BY substr(ctuscno,1,3),DECODE(TRIM(L.STATUS),'1',DECODE(TRIM(M.STAT_HT),'3','UDC','LIVE'),'BSTOP') ORDER BY CIR,STATUS)\r\n"
					+ "GROUP BY CUBE (cir,status)\r\n" + "ORDER BY cir,status)\r\n ";
			log.info(sql);
			return jdbcTemplate.queryForList(sql, new Object[] { monthYear, monthYear });
		}
	}

	public List<Map<String, Object>> getDivisionWiseLedgerWiseAbstract(String circle, String monthYear) {
		log.info(circle + "-" + monthYear);
		if (!circle.equalsIgnoreCase("ALL")) {
			String sql = "select * from (select cir,status  ,round(sum(USCNO_COUNT)) USCNO_COUNT ,round(sum(OB_OTHERTHAN_COURT)) OB_OTHERTHAN_COURT,round(sum(OB_COURT)) OB_COURT,round(sum(TOT_OB)) TOT_OB,\r\n"
					+ "round(sum(DEMAND_WITHOUT_COURT)) DEMAND_WITHOUT_COURT ,round(sum(COURT_LPC)) COURT_LPC,round(sum(DR_RJ)) DR_RJ,round(sum(CR_RJ)) CR_RJ,round(sum(COURT_RJ)) COURT_RJ ,round(sum(COLLECTION)) COLLECTION, \r\n"
					+ "round(sum(CB_OTHERTHAN_COURT)) CB_OTHERTHAN_COURT ,round(sum(CB_COURT)) CB_COURT ,round(sum(TOTAL_CB)) TOTAL_CB,round(sum(CB_SD)) CB_SD\r\n"
					+ "from (\r\n"
					+ "select nvl(DIVNAME ,'OTH') cir,count(ctuscno) USCNO_COUNT ,DECODE(TRIM(L.STATUS),'1',DECODE(TRIM(M.STAT_HT),'3','UDC','LIVE'),'BSTOP') STATUS,\r\n"
					+ "SUM(NVL(TOT_OB,0)) OB_OTHERTHAN_COURT,\r\n" + "SUM(NVL(OB_OTH,0) +NVL(OB_CCLPC,0)) OB_COURT,\r\n"
					+ "SUM(NVL(OB_OTH,0) +NVL(OB_CCLPC,0)+NVL(TOT_OB,0)) TOT_OB,\r\n"
					+ "SUM(NVL(CMD,0)) DEMAND_WITHOUT_COURT,\r\n" + "SUM(NVL(CCLPC,0)) COURT_LPC ,\r\n"
					+ "SUM(NVL(DRJ,0)) DR_RJ,\r\n" + "SUM(NVL(CRJ,0)) CR_RJ,\r\n"
					+ "SUM(NVL(RJ_CCLPC,0)+NVL(RJ_OTH,0)) COURT_RJ,\r\n" + "SUM(NVL(TOT_PAY,0)) COLLECTION,\r\n"
					+ "SUM(NVL(CBTOT,0)) CB_OTHERTHAN_COURT,\r\n" + "SUM(NVL(CB_CCLPC,0)+NVL(CB_OTH,0)) CB_COURT,\r\n"
					+ "SUM(NVL(CBTOT,0)+NVL(CB_CCLPC,0)+NVL(CB_OTH,0)) TOTAL_CB,\r\n" + "SUM(NVL(CB_SD,0)) CB_SD \r\n"
					+ "from cons,master.spdclmaster,ledger_ht_hist L,(SELECT MSCNO,min(MDCLKWHSTAT_HT) STAT_HT FROM MTRDAT_HIST WHERE to_char(MDCLRDG_DT,'MON-YYYY')=? GROUP BY MSCNO ) M \r\n"
					+ "where  USCNO=CTUSCNO  " + "and SUBSTR(CTSECCD,-5)=SECCD(+) "
					+ "AND CTUSCNO=M.MSCNO(+) and SUBSTR(CTUSCNO,1,3) IN(?)\r\n"
					+ "and MON_YEAR = ? GROUP BY DIVNAME,DECODE(TRIM(L.STATUS),'1',DECODE(TRIM(M.STAT_HT),'3','UDC','LIVE'),'BSTOP') ORDER BY CIR,STATUS)\r\n"
					+ "GROUP BY CUBE (cir,status)\r\n" + "ORDER BY cir,status) where cir is not null\r\n ";
			log.info(sql);
			return jdbcTemplate.queryForList(sql, new Object[] { monthYear, circle, monthYear });
		} else {

			String sql = "select * from (select nvl(DIVNAME ,'OTH') cir,status  ,round(sum(USCNO_COUNT)) USCNO_COUNT ,round(sum(OB_OTHERTHAN_COURT)) OB_OTHERTHAN_COURT,round(sum(OB_COURT)) OB_COURT,round(sum(TOT_OB)) TOT_OB,\r\n"
					+ "round(sum(DEMAND_WITHOUT_COURT)) DEMAND_WITHOUT_COURT ,round(sum(COURT_LPC)) COURT_LPC,round(sum(DR_RJ)) DR_RJ,round(sum(CR_RJ)) CR_RJ,round(sum(COURT_RJ)) COURT_RJ ,round(sum(COLLECTION)) COLLECTION, \r\n"
					+ "round(sum(CB_OTHERTHAN_COURT)) CB_OTHERTHAN_COURT ,round(sum(CB_COURT)) CB_COURT ,round(sum(TOTAL_CB)) TOTAL_CB,round(sum(CB_SD)) CB_SD\r\n"
					+ "from (\r\n"
					+ "select substr(ctuscno,1,3) cir,count(ctuscno) USCNO_COUNT ,DECODE(TRIM(L.STATUS),'1',DECODE(TRIM(M.STAT_HT),'3','UDC','LIVE'),'BSTOP') STATUS,\r\n"
					+ "SUM(NVL(TOT_OB,0)) OB_OTHERTHAN_COURT,\r\n" + "SUM(NVL(OB_OTH,0) +NVL(OB_CCLPC,0)) OB_COURT,\r\n"
					+ "SUM(NVL(OB_OTH,0) +NVL(OB_CCLPC,0)+NVL(TOT_OB,0)) TOT_OB,\r\n"
					+ "SUM(NVL(CMD,0)) DEMAND_WITHOUT_COURT,\r\n" + "SUM(NVL(CCLPC,0)) COURT_LPC ,\r\n"
					+ "SUM(NVL(DRJ,0)) DR_RJ,\r\n" + "SUM(NVL(CRJ,0)) CR_RJ,\r\n"
					+ "SUM(NVL(RJ_CCLPC,0)+NVL(RJ_OTH,0)) COURT_RJ,\r\n" + "SUM(NVL(TOT_PAY,0)) COLLECTION,\r\n"
					+ "SUM(NVL(CBTOT,0)) CB_OTHERTHAN_COURT,\r\n" + "SUM(NVL(CB_CCLPC,0)+NVL(CB_OTH,0)) CB_COURT,\r\n"
					+ "SUM(NVL(CBTOT,0)+NVL(CB_CCLPC,0)+NVL(CB_OTH,0)) TOTAL_CB,\r\n" + "SUM(NVL(CB_SD,0)) CB_SD \r\n"
					+ "from cons,master.spdclmaster,ledger_ht_hist L,(SELECT MSCNO,min(MDCLKWHSTAT_HT) STAT_HT FROM MTRDAT_HIST WHERE to_char(MDCLRDG_DT,'MON-YYYY')=? GROUP BY MSCNO                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 ) M \r\n"
					+ "where  USCNO=CTUSCNO  " + " and SUBSTR(CTSECCD,-5)=SECCD(+) "
					+ " AND CTUSCNO=M.MSCNO(+) and SUBSTR(CTUSCNO,1,3) IN('GNT','VJA','ONG','CRD')\r\n"
					+ "and MON_YEAR = ? GROUP BY substr(ctuscno,1,3),DECODE(TRIM(L.STATUS),'1',DECODE(TRIM(M.STAT_HT),'3','UDC','LIVE'),'BSTOP') ORDER BY CIR,STATUS)\r\n"
					+ "GROUP BY CUBE (cir,status)\r\n" + "ORDER BY cir,status)\r\n ";
			log.info(sql);
			return jdbcTemplate.queryForList(sql, new Object[] { monthYear, monthYear });
		}
	}

	public List<Map<String, Object>> getSubDivisionWiseLedgerWiseAbstract(String circle, String monthYear) {
		log.info(circle + "-" + monthYear);
		if (!circle.equalsIgnoreCase("ALL")) {
			String sql = "select * from (select cir,status  ,round(sum(USCNO_COUNT)) USCNO_COUNT ,round(sum(OB_OTHERTHAN_COURT)) OB_OTHERTHAN_COURT,round(sum(OB_COURT)) OB_COURT,round(sum(TOT_OB)) TOT_OB,\r\n"
					+ "round(sum(DEMAND_WITHOUT_COURT)) DEMAND_WITHOUT_COURT ,round(sum(COURT_LPC)) COURT_LPC,round(sum(DR_RJ)) DR_RJ,round(sum(CR_RJ)) CR_RJ,round(sum(COURT_RJ)) COURT_RJ ,round(sum(COLLECTION)) COLLECTION, \r\n"
					+ "round(sum(CB_OTHERTHAN_COURT)) CB_OTHERTHAN_COURT ,round(sum(CB_COURT)) CB_COURT ,round(sum(TOTAL_CB)) TOTAL_CB,round(sum(CB_SD)) CB_SD\r\n"
					+ "from (\r\n"
					+ "select nvl(SUBNAME ,'OTH') cir,count(ctuscno) USCNO_COUNT ,DECODE(TRIM(L.STATUS),'1',DECODE(TRIM(M.STAT_HT),'3','UDC','LIVE'),'BSTOP') STATUS,\r\n"
					+ "SUM(NVL(TOT_OB,0)) OB_OTHERTHAN_COURT,\r\n" + "SUM(NVL(OB_OTH,0) +NVL(OB_CCLPC,0)) OB_COURT,\r\n"
					+ "SUM(NVL(OB_OTH,0) +NVL(OB_CCLPC,0)+NVL(TOT_OB,0)) TOT_OB,\r\n"
					+ "SUM(NVL(CMD,0)) DEMAND_WITHOUT_COURT,\r\n" + "SUM(NVL(CCLPC,0)) COURT_LPC ,\r\n"
					+ "SUM(NVL(DRJ,0)) DR_RJ,\r\n" + "SUM(NVL(CRJ,0)) CR_RJ,\r\n"
					+ "SUM(NVL(RJ_CCLPC,0)+NVL(RJ_OTH,0)) COURT_RJ,\r\n" + "SUM(NVL(TOT_PAY,0)) COLLECTION,\r\n"
					+ "SUM(NVL(CBTOT,0)) CB_OTHERTHAN_COURT,\r\n" + "SUM(NVL(CB_CCLPC,0)+NVL(CB_OTH,0)) CB_COURT,\r\n"
					+ "SUM(NVL(CBTOT,0)+NVL(CB_CCLPC,0)+NVL(CB_OTH,0)) TOTAL_CB,\r\n" + "SUM(NVL(CB_SD,0)) CB_SD \r\n"
					+ "from cons,master.spdclmaster,ledger_ht_hist L,(SELECT MSCNO,min(MDCLKWHSTAT_HT) STAT_HT FROM MTRDAT_HIST WHERE to_char(MDCLRDG_DT,'MON-YYYY')=? GROUP BY MSCNO ) M \r\n"
					+ "where  USCNO=CTUSCNO  " + "and SUBSTR(CTSECCD,-5)=SECCD(+) "
					+ "AND CTUSCNO=M.MSCNO(+) and DIVNAME IN(?)\r\n"
					+ "and MON_YEAR = ? GROUP BY SUBNAME,DECODE(TRIM(L.STATUS),'1',DECODE(TRIM(M.STAT_HT),'3','UDC','LIVE'),'BSTOP') ORDER BY CIR,STATUS)\r\n"
					+ "GROUP BY CUBE (cir,status)\r\n" + "ORDER BY cir,status) where cir is not null\r\n ";
			log.info(sql);
			return jdbcTemplate.queryForList(sql, new Object[] { monthYear, circle, monthYear });
		} else {

			String sql = "select * from (select nvl(DIVNAME ,'OTH') cir,status  ,round(sum(USCNO_COUNT)) USCNO_COUNT ,round(sum(OB_OTHERTHAN_COURT)) OB_OTHERTHAN_COURT,round(sum(OB_COURT)) OB_COURT,round(sum(TOT_OB)) TOT_OB,\r\n"
					+ "round(sum(DEMAND_WITHOUT_COURT)) DEMAND_WITHOUT_COURT ,round(sum(COURT_LPC)) COURT_LPC,round(sum(DR_RJ)) DR_RJ,round(sum(CR_RJ)) CR_RJ,round(sum(COURT_RJ)) COURT_RJ ,round(sum(COLLECTION)) COLLECTION, \r\n"
					+ "round(sum(CB_OTHERTHAN_COURT)) CB_OTHERTHAN_COURT ,round(sum(CB_COURT)) CB_COURT ,round(sum(TOTAL_CB)) TOTAL_CB,round(sum(CB_SD)) CB_SD\r\n"
					+ "from (\r\n"
					+ "select substr(ctuscno,1,3) cir,count(ctuscno) USCNO_COUNT ,DECODE(TRIM(L.STATUS),'1',DECODE(TRIM(M.STAT_HT),'3','UDC','LIVE'),'BSTOP') STATUS,\r\n"
					+ "SUM(NVL(TOT_OB,0)) OB_OTHERTHAN_COURT,\r\n" + "SUM(NVL(OB_OTH,0) +NVL(OB_CCLPC,0)) OB_COURT,\r\n"
					+ "SUM(NVL(OB_OTH,0) +NVL(OB_CCLPC,0)+NVL(TOT_OB,0)) TOT_OB,\r\n"
					+ "SUM(NVL(CMD,0)) DEMAND_WITHOUT_COURT,\r\n" + "SUM(NVL(CCLPC,0)) COURT_LPC ,\r\n"
					+ "SUM(NVL(DRJ,0)) DR_RJ,\r\n" + "SUM(NVL(CRJ,0)) CR_RJ,\r\n"
					+ "SUM(NVL(RJ_CCLPC,0)+NVL(RJ_OTH,0)) COURT_RJ,\r\n" + "SUM(NVL(TOT_PAY,0)) COLLECTION,\r\n"
					+ "SUM(NVL(CBTOT,0)) CB_OTHERTHAN_COURT,\r\n" + "SUM(NVL(CB_CCLPC,0)+NVL(CB_OTH,0)) CB_COURT,\r\n"
					+ "SUM(NVL(CBTOT,0)+NVL(CB_CCLPC,0)+NVL(CB_OTH,0)) TOTAL_CB,\r\n" + "SUM(NVL(CB_SD,0)) CB_SD \r\n"
					+ "from cons,master.spdclmaster,ledger_ht_hist L,(SELECT MSCNO,min(MDCLKWHSTAT_HT) STAT_HT FROM MTRDAT_HIST WHERE to_char(MDCLRDG_DT,'MON-YYYY')=? GROUP BY MSCNO                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 ) M \r\n"
					+ "where  USCNO=CTUSCNO  " + " and SUBSTR(CTSECCD,-5)=SECCD(+) "
					+ " AND CTUSCNO=M.MSCNO(+) and SUBSTR(CTUSCNO,1,3) IN('GNT','VJA','ONG','CRD')\r\n"
					+ "and MON_YEAR = ? GROUP BY substr(ctuscno,1,3),DECODE(TRIM(L.STATUS),'1',DECODE(TRIM(M.STAT_HT),'3','UDC','LIVE'),'BSTOP') ORDER BY CIR,STATUS)\r\n"
					+ "GROUP BY CUBE (cir,status)\r\n" + "ORDER BY cir,status)\r\n ";
			log.info(sql);
			return jdbcTemplate.queryForList(sql, new Object[] { monthYear, monthYear });
		}
	}
	public List<Map<String, Object>> getEnergyAuditSales(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		String fromDate = request.getParameter("fromdate");
		String toDate = request.getParameter("todate");
		if (circle.equals("ALL")) {
			try {
				String dailsql = "SELECT  A.BTSCNO USCNO,B.CTNAME NAME,SUBSTR(A.BTSCNO,1,3) CIRCLE,C.DIVNAME,C.SUBNAME,C.SECNAME,A.BTRKWH_HT REC_KWH,A.BTRKVAH_HT REC_KVAH,A.BTBLCOLNY_HT COLONY,A.BTBKVAH BILLED_KVAH,A.BTTP_KVAH TIRD_PARTY,A.BTOA_KVAH OPEN_ACCESS,A.BTBLSOLAR_HT SOLAR,\r\n"
						+ "TO_CHAR(A.BTBLDT,'DD-MM-YYYY') BILL_DATE, \r\n"
						+ "A.BTBLCAT CAT,A.BTBLSCAT SUB_CAT,B.CTCMD_HT,DECODE(TRIM(B.CTSTATUS),1,'LIVE',0,'STOP',15,'DISMANTLED',16,'HT_TO_LT','UDC') CTSTATUS, B.CTACTUAL_KV VOLTAGE,B.CTFEEDER_CODE FEEDER_CODE,B.CTFEEDER_NAME FEEDER_NAME,MDTOD_PEAK,MDTOD_OFFPEAK,BTOA_KVAH,STDESC, R.KVAH_UNITS ,NVL(BB.KVAH_UNITS,0) BB_KVAH_UNITS \r\n"
						+ "FROM (select * from BILL_HIST union all select * from BILL ) A, (select MSCNO , MDMONTH, sum(MDTOD2_RECKVAH+MDTOD5_RECKVAH)MDTOD_PEAK, sum(MDTOD1_RECKVAH+MDTOD6_RECKVAH)MDTOD_OFFPEAK from mtrdat_hist group by mscno,MDMONTH union all select MSCNO , MDMONTH, sum(MDTOD2_RECKVAH+MDTOD5_RECKVAH)MDTOD_PEAK, sum(MDTOD1_RECKVAH+MDTOD6_RECKVAH)MDTOD_OFFPEAK  from mtrdat group by mscno,MDMONTH) M ,\r\n"
						+ "CONS B, SPDCLMASTER C ,SERVTYPE , "
						+ "(select USCNO,to_char(RJDT,'MON-YYYY') RJDT,sum(KVAH_UNITS) KVAH_UNITS from journal_hist where KVAH_UNITS is not null and SAPRJ in ('DWC-OA','DRC-OA') and STATUS='C' and RJDT "
						+ " BETWEEN TO_DATE(?,'DD-MM-YY') AND TO_DATE(?,'DD-MM-YY') group by USCNO,to_char(RJDT,'MON-YYYY') union all select USCNO,to_char(RJDT,'MON-YYYY') RJDT,sum(KVAH_UNITS) KVAH_UNITS from journal where KVAH_UNITS is not null and SAPRJ in ('DWC-OA','DRC-OA') and STATUS='C'  and RJDT "
						+ "  BETWEEN TO_DATE(?,'DD-MM-YY') AND TO_DATE(?,'DD-MM-YY') group by USCNO,to_char(RJDT,'MON-YYYY') ) R ,"
						+ "(select USCNO,to_char(RJDT,'MON-YYYY') RJDT,sum(KVAH_UNITS) KVAH_UNITS from journal_hist where KVAH_UNITS is not null and SAPRJ in ('DRC-BB','DWC-BB') and STATUS='C' and RJDT  BETWEEN TO_DATE(?,'DD-MM-YY') AND TO_DATE(?,'DD-MM-YY') group by USCNO,to_char(RJDT,'MON-YYYY')\r\n" + 
						"union all \r\n" + 
						"select USCNO,to_char(RJDT,'MON-YYYY') RJDT,sum(KVAH_UNITS) KVAH_UNITS from journal where KVAH_UNITS is not null and SAPRJ in ('DRC-BB','DWC-BB') and STATUS='C'  and RJDT   BETWEEN TO_DATE(?,'DD-MM-YY') AND TO_DATE(?,'DD-MM-YY') group by USCNO,to_char(RJDT,'MON-YYYY') ) BB"
						+ " WHERE B.CTSERVTYPE= STCODE(+)  " + "AND A.BTSCNO = R.USCNO(+) AND A.BTSCNO = BB.USCNO(+) and "
						+ "A.BTSCNO = B.CTUSCNO AND A.BTSCNO = M.MSCNO(+) AND TO_CHAR(A.BTBLDT,'MON-YYYY')=TO_CHAR(M.MDMONTH,'MON-YYYY') "
						/* + " AND TO_CHAR(A.BTBLDT,'MON-YYYY')=TO_CHAR(R.RJDT,'MON-YYYY') " */
						+ "AND SUBSTR(CTSECCD,-5) = SECCD(+)  AND A.BTBLDT BETWEEN TO_DATE(?,'DD-MM-YY') AND TO_DATE(?,'DD-MM-YY') ORDER BY BTSCNO,BTBLDT\r\n"
						+ "";
				log.info(dailsql);
				return jdbcTemplate.queryForList(dailsql,
						new Object[] { fromDate, toDate, fromDate, toDate, fromDate, toDate, fromDate, toDate, fromDate, toDate  });
			} catch (DataAccessException e) {
				log.info(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}

		} else {
			try {
				String dailsql = "SELECT  A.BTSCNO USCNO,B.CTNAME NAME,SUBSTR(A.BTSCNO,1,3) CIRCLE,C.DIVNAME,C.SUBNAME,C.SECNAME,A.BTRKWH_HT REC_KWH,A.BTRKVAH_HT REC_KVAH,A.BTBLCOLNY_HT COLONY,A.BTBKVAH BILLED_KVAH,A.BTTP_KVAH TIRD_PARTY,A.BTOA_KVAH OPEN_ACCESS,A.BTBLSOLAR_HT SOLAR,\r\n"
						+ "TO_CHAR(A.BTBLDT,'DD-MM-YYYY') BILL_DATE, \r\n"
						+ "A.BTBLCAT CAT,A.BTBLSCAT SUB_CAT,B.CTCMD_HT,DECODE(TRIM(B.CTSTATUS),1,'LIVE',0,'STOP',15,'DISMANTLED',16,'HT_TO_LT','UDC') CTSTATUS, B.CTACTUAL_KV VOLTAGE,B.CTFEEDER_CODE FEEDER_CODE,B.CTFEEDER_NAME FEEDER_NAME,MDTOD_PEAK,MDTOD_OFFPEAK,BTOA_KVAH,STDESC, R.KVAH_UNITS,NVL(BB.KVAH_UNITS,0) BB_KVAH_UNITS  \r\n"
						+ "FROM (select * from BILL_HIST union all select * from BILL ) A, (select MSCNO , MDMONTH, sum(MDTOD2_RECKVAH+MDTOD5_RECKVAH)MDTOD_PEAK, sum(MDTOD1_RECKVAH+MDTOD6_RECKVAH)MDTOD_OFFPEAK from mtrdat_hist group by mscno,MDMONTH union all select MSCNO , MDMONTH, sum(MDTOD2_RECKVAH+MDTOD5_RECKVAH)MDTOD_PEAK, sum(MDTOD1_RECKVAH+MDTOD6_RECKVAH)MDTOD_OFFPEAK  from mtrdat group by mscno,MDMONTH) M ,\r\n"
						+ "CONS B, SPDCLMASTER C ,SERVTYPE , "
						+ "(select USCNO,to_char(RJDT,'MON-YYYY') RJDT,sum(KVAH_UNITS) KVAH_UNITS from journal_hist where KVAH_UNITS is not null and SAPRJ in ('DWC-OA','DRC-OA') and STATUS='C' and RJDT "
						+ " BETWEEN TO_DATE(?,'DD-MM-YY') AND TO_DATE(?,'DD-MM-YY')  group by USCNO,to_char(RJDT,'MON-YYYY')  union all select USCNO,to_char(RJDT,'MON-YYYY') RJDT,sum(KVAH_UNITS) KVAH_UNITS from journal where KVAH_UNITS is not null and SAPRJ in ('DWC-OA','DRC-OA') and STATUS='C'  and RJDT "
						+ "  BETWEEN TO_DATE(?,'DD-MM-YY') AND TO_DATE(?,'DD-MM-YY')  group by USCNO,to_char(RJDT,'MON-YYYY') ) R,"
						+ " (select USCNO,to_char(RJDT,'MON-YYYY') RJDT,sum(KVAH_UNITS) KVAH_UNITS from journal_hist where KVAH_UNITS is not null and SAPRJ in ('DRC-BB','DWC-BB') and STATUS='C' and RJDT  BETWEEN TO_DATE(?,'DD-MM-YY') AND TO_DATE(?,'DD-MM-YY') group by USCNO,to_char(RJDT,'MON-YYYY')\r\n" + 
						"union all \r\n" + 
						"select USCNO,to_char(RJDT,'MON-YYYY') RJDT,sum(KVAH_UNITS) KVAH_UNITS from journal where KVAH_UNITS is not null and SAPRJ in ('DRC-BB','DWC-BB') and STATUS='C'  and RJDT   BETWEEN TO_DATE(?,'DD-MM-YY') AND TO_DATE(?,'DD-MM-YY') group by USCNO,to_char(RJDT,'MON-YYYY') ) BB"
						+ " WHERE B.CTSERVTYPE= STCODE(+)  " + "AND A.BTSCNO = R.USCNO(+) AND A.BTSCNO = BB.USCNO(+)  and "
						+ "A.BTSCNO = B.CTUSCNO AND A.BTSCNO = M.MSCNO(+) AND TO_CHAR(A.BTBLDT,'MON-YYYY')=TO_CHAR(M.MDMONTH,'MON-YYYY') "
						/* + " AND TO_CHAR(A.BTBLDT,'MON-YYYY')=TO_CHAR(R.RJDT,'MON-YYYY') " */
						+ "AND SUBSTR(CTSECCD,-5) = SECCD(+) AND SUBSTR(A.BTSCNO,1,3) = ? AND A.BTBLDT BETWEEN TO_DATE(?,'DD-MM-YY') AND TO_DATE(?,'DD-MM-YY') ORDER BY BTSCNO,BTBLDT\r\n"
						+ "";

				log.info(dailsql);
				return jdbcTemplate.queryForList(dailsql,
						new Object[] { fromDate, toDate, fromDate, toDate,  fromDate, toDate, fromDate, toDate, circle, fromDate, toDate });
			} catch (DataAccessException e) {
				log.info(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}
		}
	}

	public List<Map<String, Object>> getEnergyAuditSalesOA(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		String fromDate = request.getParameter("fromdate");
		String toDate = request.getParameter("todate");
		if (circle.equals("ALL")) {
			try {

				String dailsql = "SELECT  A.BTSCNO USCNO,B.CTNAME NAME,SUBSTR(A.BTSCNO,1,3) CIRCLE,C.DIVNAME,C.SUBNAME,C.SECNAME,A.BTRKWH_HT REC_KWH,A.BTRKVAH_HT REC_KVAH,A.BTBLCOLNY_HT COLONY,A.BTBKVAH BILLED_KVAH,A.BTTP_KVAH TIRD_PARTY,A.BTOA_KVAH OPEN_ACCESS,A.BTBLSOLAR_HT SOLAR,\r\n"
						+ "TO_CHAR(A.BTBLDT,'DD-MM-YYYY') BILL_DATE, \r\n"
						+ "B.CTCAT CAT,B.CTSUBCAT SUB_CAT,B.CTCMD_HT,DECODE(TRIM(B.CTSTATUS),1,'LIVE',0,'STOP',15,'DISMANTLED',16,'HT_TO_LT','UDC') CTSTATUS, B.CTACTUAL_KV VOLTAGE,B.CTFEEDER_CODE FEEDER_CODE,B.CTFEEDER_NAME FEEDER_NAME,MDTOD_PEAK,MDTOD_OFFPEAK,round(nvl(BTOA_KVAH,0)/1000000,3) BTOA_KVAH,STDESC, NVL(R.FINAL_OA_30_PER,0) KVAH_UNITS \r\n"
						+ "FROM (select * from BILL_HIST union all select * from BILL ) A, (select MSCNO , MDMONTH, sum(MDTOD2_RECKVAH+MDTOD5_RECKVAH)MDTOD_PEAK, sum(MDTOD1_RECKVAH+MDTOD6_RECKVAH)MDTOD_OFFPEAK from mtrdat_hist group by mscno,MDMONTH union all select MSCNO , MDMONTH, sum(MDTOD2_RECKVAH+MDTOD5_RECKVAH)MDTOD_PEAK, sum(MDTOD1_RECKVAH+MDTOD6_RECKVAH)MDTOD_OFFPEAK  from mtrdat group by mscno,MDMONTH) M ,\r\n"
						+ "CONS B, SPDCLMASTER C ,SERVTYPE , HT_FINAL_OA_30_PER_SALES R"
						+ " WHERE B.CTSERVTYPE= STCODE  " + "AND A.BTSCNO = R.SCNO(+) and "
						+ "A.BTSCNO = B.CTUSCNO AND A.BTSCNO = M.MSCNO(+) AND TO_CHAR(A.BTBLDT,'MON-YYYY')=TO_CHAR(M.MDMONTH,'MON-YYYY') "
						/* + " AND TO_CHAR(A.BTBLDT,'MON-YYYY')=TO_CHAR(R.RJDT,'MON-YYYY') " */
						+ "AND SUBSTR(CTSECCD,-5) = SECCD AND A.BTBLDT BETWEEN TO_DATE(?,'DD-MM-YY') AND TO_DATE(?,'DD-MM-YY') ORDER BY BTSCNO,BTBLDT\r\n"
						+ "";
				log.info(dailsql);

				log.info(dailsql);
				return jdbcTemplate.queryForList(dailsql, new Object[] { fromDate, toDate });
			} catch (DataAccessException e) {
				log.info(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}

		} else {
			try {
				/*
				 * String
				 * dailsql="SELECT A.BTSCNO USCNO,B.CTNAME NAME,SUBSTR(A.BTSCNO,1,3) CIRCLE,C.DIVNAME,C.SUBNAME,C.SECNAME,A.BTRKWH_HT REC_KWH,A.BTRKVAH_HT REC_KVAH,A.BTBLCOLNY_HT COLONY,A.BTBKVAH BILLED_KVAH,A.BTTP_KVAH TIRD_PARTY,A.BTOA_KVAH OPEN_ACCESS,A.BTBLSOLAR_HT SOLAR,\r\n"
				 * + "TO_CHAR(A.BTBLDT,'DD-MM-YYYY') BILL_DATE, \r\n" +
				 * "B.CTCAT CAT,B.CTSUBCAT SUB_CAT, B.CTACTUAL_KV VOLTAGE,B.CTFEEDER_CODE FEEDER_CODE,B.CTFEEDER_NAME FEEDER_NAME\r\n"
				 * +
				 * "FROM  (select * from BILL_HIST union all select * from BILL )  A, CONS B, SPDCLMASTER C WHERE A.BTSCNO = B.CTUSCNO AND SUBSTR(A.BTSCNO,1,3) = ? --IN ('GNT','CRD','VJA','ONG') \r\n"
				 * +
				 * "AND SUBSTR(CTSECCD,-5) = SECCD AND A.BTBLDT BETWEEN TO_DATE(?,'DD-MM-YY') AND TO_DATE(?,'DD-MM-YY') ORDER BY BTSCNO,BTBLDT"
				 * ;
				 */

				/*
				 * String
				 * dailsql="SELECT A.BTSCNO USCNO,B.CTNAME NAME,SUBSTR(A.BTSCNO,1,3) CIRCLE,C.DIVNAME,C.SUBNAME,C.SECNAME,A.BTRKWH_HT REC_KWH,A.BTRKVAH_HT REC_KVAH,A.BTBLCOLNY_HT COLONY,A.BTBKVAH BILLED_KVAH,A.BTTP_KVAH TIRD_PARTY,A.BTOA_KVAH OPEN_ACCESS,A.BTBLSOLAR_HT SOLAR,\r\n"
				 * + "TO_CHAR(A.BTBLDT,'DD-MM-YYYY') BILL_DATE, \r\n" +
				 * "B.CTCAT CAT,B.CTSUBCAT SUB_CAT,B.CTCMD_HT,DECODE(TRIM(B.CTSTATUS),1,'LIVE',0,'STOP',15,'DISMANTLED',16,'HT_TO_LT','UDC') CTSTATUS, B.CTACTUAL_KV VOLTAGE,B.CTFEEDER_CODE FEEDER_CODE,B.CTFEEDER_NAME FEEDER_NAME,MDTOD_PEAK,MDTOD_OFFPEAK,BTOA_KVAH,STDESC \r\n"
				 * +
				 * "FROM (select * from BILL_HIST union all select * from BILL ) A, (select MSCNO , MDMONTH, sum(MDTOD2_RECKVAH+MDTOD5_RECKVAH)MDTOD_PEAK, sum(MDTOD1_RECKVAH+MDTOD6_RECKVAH)MDTOD_OFFPEAK from mtrdat_hist group by mscno,MDMONTH union all select MSCNO , MDMONTH, sum(MDTOD2_RECKVAH+MDTOD5_RECKVAH)MDTOD_PEAK, sum(MDTOD1_RECKVAH+MDTOD6_RECKVAH)MDTOD_OFFPEAK  from mtrdat group by mscno,MDMONTH) M ,\r\n"
				 * +
				 * "CONS B, SPDCLMASTER C,SERVTYPE WHERE B.CTSERVTYPE= STCODE and A.BTSCNO = B.CTUSCNO AND A.BTSCNO = M.MSCNO(+) AND TO_CHAR(A.BTBLDT,'MON-YYYY')=TO_CHAR(M.MDMONTH,'MON-YYYY') AND SUBSTR(CTSECCD,-5) = SECCD  AND SUBSTR(A.BTSCNO,1,3) = ? AND A.BTBLDT BETWEEN TO_DATE(?,'DD-MM-YY') AND TO_DATE(?,'DD-MM-YY') ORDER BY BTSCNO,BTBLDT\r\n"
				 * + "";
				 */

				String dailsql = "SELECT  A.BTSCNO USCNO,B.CTNAME NAME,SUBSTR(A.BTSCNO,1,3) CIRCLE,C.DIVNAME,C.SUBNAME,C.SECNAME,A.BTRKWH_HT REC_KWH,A.BTRKVAH_HT REC_KVAH,A.BTBLCOLNY_HT COLONY,A.BTBKVAH BILLED_KVAH,A.BTTP_KVAH TIRD_PARTY,A.BTOA_KVAH OPEN_ACCESS,A.BTBLSOLAR_HT SOLAR,\r\n"
						+ "TO_CHAR(A.BTBLDT,'DD-MM-YYYY') BILL_DATE, \r\n"
						+ "B.CTCAT CAT,B.CTSUBCAT SUB_CAT,B.CTCMD_HT,DECODE(TRIM(B.CTSTATUS),1,'LIVE',0,'STOP',15,'DISMANTLED',16,'HT_TO_LT','UDC') CTSTATUS, B.CTACTUAL_KV VOLTAGE,B.CTFEEDER_CODE FEEDER_CODE,B.CTFEEDER_NAME FEEDER_NAME,MDTOD_PEAK,MDTOD_OFFPEAK,round(nvl(BTOA_KVAH,0)/1000000,3) BTOA_KVAH,STDESC, NVL(R.FINAL_OA_30_PER,0) KVAH_UNITS \r\n"
						+ "FROM (select * from BILL_HIST union all select * from BILL ) A, (select MSCNO , MDMONTH, sum(MDTOD2_RECKVAH+MDTOD5_RECKVAH)MDTOD_PEAK, sum(MDTOD1_RECKVAH+MDTOD6_RECKVAH)MDTOD_OFFPEAK from mtrdat_hist group by mscno,MDMONTH union all select MSCNO , MDMONTH, sum(MDTOD2_RECKVAH+MDTOD5_RECKVAH)MDTOD_PEAK, sum(MDTOD1_RECKVAH+MDTOD6_RECKVAH)MDTOD_OFFPEAK  from mtrdat group by mscno,MDMONTH) M ,\r\n"
						+ "CONS B, SPDCLMASTER C ,SERVTYPE , HT_FINAL_OA_30_PER_SALES R"
						+ " WHERE B.CTSERVTYPE= STCODE  " + "AND A.BTSCNO = R.SCNO(+) and "
						+ "A.BTSCNO = B.CTUSCNO AND A.BTSCNO = M.MSCNO(+) AND TO_CHAR(A.BTBLDT,'MON-YYYY')=TO_CHAR(M.MDMONTH,'MON-YYYY') "
						/* + " AND TO_CHAR(A.BTBLDT,'MON-YYYY')=TO_CHAR(R.RJDT,'MON-YYYY') " */
						+ "AND SUBSTR(CTSECCD,-5) = SECCD AND SUBSTR(A.BTSCNO,1,3) = ? AND A.BTBLDT BETWEEN TO_DATE(?,'DD-MM-YY') AND TO_DATE(?,'DD-MM-YY') ORDER BY BTSCNO,BTBLDT\r\n"
						+ "";
				log.info(dailsql);

				log.info(dailsql);
				return jdbcTemplate.queryForList(dailsql, new Object[] { circle, fromDate, toDate });
			} catch (DataAccessException e) {
				log.info(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}
		}
	}

	public List<Map<String, Object>> getEnergyAuditSalesOAMW(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		String fromDate = request.getParameter("fromdate");
		String toDate = request.getParameter("todate");
		if (circle.equals("ALL")) {
			try {

				String dailsql = "SELECT  A.BTSCNO USCNO,B.CTNAME NAME,SUBSTR(A.BTSCNO,1,3) CIRCLE,C.DIVNAME,C.SUBNAME,C.SECNAME,A.BTRKWH_HT REC_KWH,A.BTRKVAH_HT REC_KVAH,A.BTBLCOLNY_HT COLONY,A.BTBKVAH BILLED_KVAH,A.BTTP_KVAH TIRD_PARTY,A.BTOA_KVAH OPEN_ACCESS,A.BTBLSOLAR_HT SOLAR,\r\n"
						+ "TO_CHAR(A.BTBLDT,'DD-MM-YYYY') BILL_DATE, \r\n"
						+ "B.CTCAT CAT,B.CTSUBCAT SUB_CAT,B.CTCMD_HT,DECODE(TRIM(B.CTSTATUS),1,'LIVE',0,'STOP',15,'DISMANTLED',16,'HT_TO_LT','UDC') CTSTATUS, B.CTACTUAL_KV VOLTAGE,B.CTFEEDER_CODE FEEDER_CODE,B.CTFEEDER_NAME FEEDER_NAME,MDTOD_PEAK,MDTOD_OFFPEAK,round(nvl(BTOA_KVAH,0)/1000000,3) BTOA_KVAH,STDESC, NVL(R.OA_30_PER,0) KVAH_UNITS \r\n"
						+ "FROM (select * from BILL_HIST union all select * from BILL ) A, (select MSCNO , MDMONTH, sum(MDTOD2_RECKVAH+MDTOD5_RECKVAH)MDTOD_PEAK, sum(MDTOD1_RECKVAH+MDTOD6_RECKVAH)MDTOD_OFFPEAK from mtrdat_hist group by mscno,MDMONTH union all select MSCNO , MDMONTH, sum(MDTOD2_RECKVAH+MDTOD5_RECKVAH)MDTOD_PEAK, sum(MDTOD1_RECKVAH+MDTOD6_RECKVAH)MDTOD_OFFPEAK  from mtrdat group by mscno,MDMONTH) M ,\r\n"
						+ "CONS B, SPDCLMASTER C ,SERVTYPE , HT_OA_30_PER_SETL_MON_WISE R"
						+ " WHERE B.CTSERVTYPE= STCODE  " + "AND A.BTSCNO = R.SCNO(+) and "
						+ "A.BTSCNO = B.CTUSCNO AND A.BTSCNO = M.MSCNO(+) AND TO_CHAR(A.BTBLDT,'MON-YYYY')=TO_CHAR(M.MDMONTH,'MON-YYYY') and  TO_CHAR(A.BTBLDT,'MON-YYYY')=R.MON_YEAR(+)"
						+ "AND SUBSTR(CTSECCD,-5) = SECCD AND A.BTBLDT BETWEEN TO_DATE(?,'DD-MM-YY') AND TO_DATE(?,'DD-MM-YY') ORDER BY BTSCNO,BTBLDT\r\n"
						+ "";
				log.info(dailsql);

				log.info(dailsql);
				return jdbcTemplate.queryForList(dailsql, new Object[] { fromDate, toDate });
			} catch (DataAccessException e) {
				log.info(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}

		} else {
			try {
				String dailsql = "SELECT  A.BTSCNO USCNO,B.CTNAME NAME,SUBSTR(A.BTSCNO,1,3) CIRCLE,C.DIVNAME,C.SUBNAME,C.SECNAME,A.BTRKWH_HT REC_KWH,A.BTRKVAH_HT REC_KVAH,A.BTBLCOLNY_HT COLONY,A.BTBKVAH BILLED_KVAH,A.BTTP_KVAH TIRD_PARTY,A.BTOA_KVAH OPEN_ACCESS,A.BTBLSOLAR_HT SOLAR,\r\n"
						+ "TO_CHAR(A.BTBLDT,'DD-MM-YYYY') BILL_DATE, \r\n"
						+ "B.CTCAT CAT,B.CTSUBCAT SUB_CAT,B.CTCMD_HT,DECODE(TRIM(B.CTSTATUS),1,'LIVE',0,'STOP',15,'DISMANTLED',16,'HT_TO_LT','UDC') CTSTATUS, B.CTACTUAL_KV VOLTAGE,B.CTFEEDER_CODE FEEDER_CODE,B.CTFEEDER_NAME FEEDER_NAME,MDTOD_PEAK,MDTOD_OFFPEAK,round(nvl(BTOA_KVAH,0)/1000000,3) BTOA_KVAH,STDESC, NVL(R.OA_30_PER,0) KVAH_UNITS \r\n"
						+ "FROM (select * from BILL_HIST union all select * from BILL ) A, (select MSCNO , MDMONTH, sum(MDTOD2_RECKVAH+MDTOD5_RECKVAH)MDTOD_PEAK, sum(MDTOD1_RECKVAH+MDTOD6_RECKVAH)MDTOD_OFFPEAK from mtrdat_hist group by mscno,MDMONTH union all select MSCNO , MDMONTH, sum(MDTOD2_RECKVAH+MDTOD5_RECKVAH)MDTOD_PEAK, sum(MDTOD1_RECKVAH+MDTOD6_RECKVAH)MDTOD_OFFPEAK  from mtrdat group by mscno,MDMONTH) M ,\r\n"
						+ "CONS B, SPDCLMASTER C ,SERVTYPE , HT_OA_30_PER_SETL_MON_WISE R"
						+ " WHERE B.CTSERVTYPE= STCODE  " + "AND A.BTSCNO = R.SCNO(+) and "
						+ "A.BTSCNO = B.CTUSCNO AND A.BTSCNO = M.MSCNO(+) AND TO_CHAR(A.BTBLDT,'MON-YYYY')=TO_CHAR(M.MDMONTH,'MON-YYYY')  and  TO_CHAR(A.BTBLDT,'MON-YYYY')=R.MON_YEAR(+)"
						+ "AND SUBSTR(CTSECCD,-5) = SECCD AND SUBSTR(A.BTSCNO,1,3) = ? AND A.BTBLDT BETWEEN TO_DATE(?,'DD-MM-YY') AND TO_DATE(?,'DD-MM-YY') ORDER BY BTSCNO,BTBLDT\r\n"
						+ "";
				log.info(dailsql);

				log.info(dailsql);
				return jdbcTemplate.queryForList(dailsql, new Object[] { circle, fromDate, toDate });
			} catch (DataAccessException e) {
				log.info(e.getMessage());
				e.printStackTrace();
				return Collections.emptyList();
			}
		}
	}

	public List<Map<String, Object>> getApgpcl(HttpServletRequest request) {

		String monthYear = request.getParameter("month") + "-" + request.getParameter("year");
		try {
			String dailsql = "SELECT MON_YEAR,USCNO,NAM,CAT,SCAT,CBTOT CB_OTHERTHAN_COURT,CB_OTH COURT_CC,CB_CCLPC COURT_LPC,DECODE(TRIM(CTSTATUS),1,'LIVE',0,'STOP',15,'DISMANTLED',16,'HT_TO_LT','UDC')CTSTATUS,\r\n"
					+ "DECODE(TRIM(CTEMPTY_3),'A','APGPCL','F','FERRO','') TYPE\r\n"
					+ "FROM CONS_FLAGS,LEDGER_HT_HIST \r\n" + "WHERE CTEMPTY_3 IN('F','A')\r\n"
					+ "AND CTUSCNO=USCNO AND MON_YEAR=? ORDER BY TYPE,USCNO";
			log.info(dailsql);
			return jdbcTemplate.queryForList(dailsql, new Object[] { monthYear });
		} catch (Exception e) {
			log.info(e.getMessage());
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	public List<Map<String, Object>> getSolarExport(HttpServletRequest request) {

		String fyear[] = request.getParameter("year").split("-");

		try {
			String dailsql = ("SELECT * FROM\r\n" + "(\r\n"
					+ "  SELECT SUBSTR(CTUSCNO,1,3) CIRCLE,to_char(btbldt,'MON-YYYY') MONTH ,sum(BTBLSOLAR_HT) EXPORT_SOLAR FROM BILL_HIST,\r\n"
					+ "CONS WHERE BTSCNO=CTUSCNO AND TRIM(CTSOLAR_FLAG)='Y' and btbldt BETWEEN TO_DATE ('01-04-FI', 'DD-MM-YYYY')\r\n"
					+ "AND TO_DATE ('31-03-SI', 'DD-MM-YYYY') group by SUBSTR(CTUSCNO,1,3),to_char(btbldt,'MON-YYYY')\r\n"
					+ "order by CIRCLE,to_date(MONTH,'MON-YYYY') asc\r\n" + ")\r\n" + "PIVOT\r\n" + "(\r\n"
					+ "  SUM(EXPORT_SOLAR)\r\n"
					+ "  FOR MONTH IN ('APR-FI' APR_FI,'MAY-FI'MAY_FI,'JUN-FI'JUN_FI,'JUL-FI'JUL_FI,'AUG-FI'AUG_FI,'SEP-FI'SEP_FI,\r\n"
					+ "  'OCT-FI'OCT_FI,'NOV-FI'NOV_FI,'DEC-FI'DEC_FI,'JAN-SI'JAN_SI,'FEB-SI'FEB_SI,'MAR-SI' MAR_SI)\r\n"
					+ ")\r\n" + "ORDER BY CIRCLE").replace("FI", fyear[0]).replace("SI", fyear[1]);
			log.info(dailsql);
			return jdbcTemplate.queryForList(dailsql, new Object[] {});
		} catch (Exception e) {
			log.info(e.getMessage());
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	public static Object[] getDatesAndMonths(String fy, String Startmonth) {
		Object[] obj = new Object[3];
		List<String> months = new ArrayList<String>();
		DateFormat formater = new SimpleDateFormat("MMM-yyyy");
		Calendar beginCalendar = Calendar.getInstance();
		Calendar currentCalendar = Calendar.getInstance();
		Calendar endCalendar = Calendar.getInstance();
		try {
			beginCalendar.setTime(formater.parse(Startmonth + "-" + fy.split("-")[0]));
			currentCalendar.setTime(new Date());
			endCalendar.setTime(formater.parse("MAR-" + fy.split("-")[1]));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		endCalendar.add(Calendar.MONTH, 1);
		endCalendar.set(Calendar.DAY_OF_MONTH, 1);
		endCalendar.add(Calendar.DATE, -1);
		Date lastDayOfMonth = endCalendar.getTime();
		DateFormat sdf = new SimpleDateFormat("dd-MM-yy");
		obj[1] = sdf.format(lastDayOfMonth);
		Date firstDayOfMonth = beginCalendar.getTime();
		obj[0] = sdf.format(firstDayOfMonth);
		DateFormat formaterYd = new SimpleDateFormat("MMM-yyyy");
		if (currentCalendar.before(endCalendar)) {
			while (beginCalendar.before(currentCalendar)) {
				months.add(formaterYd.format(beginCalendar.getTime()).toUpperCase());
				beginCalendar.add(Calendar.MONTH, 1);
			}
		} else {
			while (beginCalendar.before(endCalendar)) {
				months.add(formaterYd.format(beginCalendar.getTime()).toUpperCase());
				beginCalendar.add(Calendar.MONTH, 1);
			}
		}
		obj[2] = months;
		return obj;
	}

	/*
	 * public static String queryBuilder(Object[] obj) {
	 * 
	 * String monthscon = ","; String totmonthscon = ","; String summonth = ",";
	 * Iterator<String> itr = ((List<String>)obj[2]).iterator();
	 * while(itr.hasNext()){ String mon = itr.next(); monthscon = monthscon +
	 * "NVL(SUM(CASE WHEN TO_CHAR(PDT,'MON-YYYY')='"
	 * +mon+"' THEN ROUND(NVL(ACD_COLL,0)) END),0)  "+mon.replace("-", "_") +" ,";
	 * totmonthscon = totmonthscon + "NVL(SUM(CASE WHEN TO_CHAR(PDT,'MON-YYYY')='"
	 * +mon+"' THEN ROUND(NVL(ACD_COLL,0)) END),0) " +"+"; summonth = summonth +
	 * "sum("+"CNT_"+mon.replace("-", "_")+") "+"CNT_"+mon.replace("-", "_") +" ," +
	 * "sum("+mon.replace("-", "_")+") "+mon.replace("-", "_") +" ,";//sum(MAY_2021)
	 * MAY_2021 } monthscon = monthscon.substring(0, monthscon.length() - 1)+" ";
	 * totmonthscon = totmonthscon.substring(0, totmonthscon.length() -
	 * 1)+" "+" TOT_MONTH_SUM"; summonth =
	 * summonth+"sum(TOT_MONTH_SUM)  TOT_MONTH_SUM  ";
	 * 
	 * String sql =
	 * "select nvl(CIRCLE,'APCPDCL') CIRCLE,nvl(TYPE,'TOTAL') TYPE, nvl(CIRCLE,'APCPDCL')||nvl(TYPE,'TOTAL') CIR_TYPE ,nvl(CTCAT,'TOTAL') CTCAT,SUM(NOS) NOS,\r\n"
	 * +
	 * "sum(SD_TO_BE_MAINTAINED) SD_TO_BE_MAINTAINED,sum(SD_AVAILABLE) SD_AVAILABLE,sum(ACD_CALCULATED) ACD_CALCULATED,SUM(NVL(DR_ACD,0)) TOT_DR_ACD\r\n"
	 * + summonth+ "from \r\n" +
	 * "(select SUBSTR(CTUSCNO,1,3) CIRCLE, CASE  WHEN NVL(CTGOVT_PVT,'N')='Y' THEN 'GOVT' ELSE 'NON-GOVT' END  TYPE ,CTCAT ,COUNT(*) NOS,\r\n"
	 * +
	 * "    sum(ROUND(SD_TO_BE_MAINTAINED)) SD_TO_BE_MAINTAINED,sum(ROUND(SD_AVAILABLE)) SD_AVAILABLE,sum(ROUND(ACD_CALCULATED)) ACD_CALCULATED\r\n"
	 * + monthscon +""+totmonthscon + " "+
	 * " from CONS,(SELECT USCNO,(NVL(REQ_SD,0)) SD_TO_BE_MAINTAINED,(NVL(AVAIL_SD,0)) SD_AVAILABLE,(NVL(NET_ACD,0)) ACD_CALCULATED FROM ACD_CALC_HT,CONS WHERE USCNO=CTUSCNO  AND LEVI_FLG='Y' AND FIN_YEAR='2020-2021' AND LEVI_MTH='MAY-2021') A,\r\n"
	 * +
	 * "(select USCNO,TRUNC(PAY_DATE,'MM') PDT,sum(NVL(PACD,0) ) ACD_COLL from PAY_HT  where NVL(trim(PAY_STA_FLG),0) not in('X','E')  GROUP BY USCNO,TRUNC(PAY_DATE,'MM')\r\n"
	 * +
	 * "UNION select USCNO,TRUNC(PAY_DATE,'MM') PDT,sum(NVL(PACD,0) ) ACD_COLL\r\n"
	 * + " from PAY_HT_HIST\r\n" +
	 * " where NVL(trim(PAY_STA_FLG),0) not in('X','E') AND TO_DATE(PAY_DATE,'dd-mm-yy')>=TO_DATE('"
	 * +obj[0] +"','dd-mm-yy') and TO_DATE(PAY_DATE,'dd-mm-yy')<=TO_DATE('"+obj[1]
	 * +"','dd-mm-yy') GROUP BY USCNO,TRUNC(PAY_DATE,'MM')) P\r\n" +
	 * " WHERE CTUSCNO=P.USCNO(+) AND CTUSCNO=A.USCNO \r\n" +
	 * " GROUP BY SUBSTR(CTUSCNO,1,3),CTCAT, CASE  WHEN NVL(CTGOVT_PVT,'N')='Y' THEN 'GOVT' ELSE 'NON-GOVT' END ORDER BY CIRCLE)\r\n"
	 * + "GROUP BY CUBE (CIRCLE,TYPE,CTCAT)\r\n" + "ORDER BY \r\n" +
	 * "case when CIRCLE = 'VJA' then '001' when CIRCLE = 'GNT' then '002' when CIRCLE = 'ONG' then '003'  when CIRCLE = 'CRD' then '009' else CIRCLE end,case when TYPE='GOVT' then '001' when TYPE = 'NON-GOVT' then '002' else TYPE end,CTCAT\r\n"
	 * ; log.info(sql); return sql; }
	 */

	public static String queryBuilderForACDCollection(Object[] obj) {

		String monthscon = ",";
		String summonth = ",";
		Iterator<String> itr = ((List<String>) obj[2]).iterator();
		while (itr.hasNext()) {
			String mon = itr.next();
			monthscon = monthscon + "NVL(COUNT(DISTINCT(CASE WHEN TO_CHAR(PDT,'MON-YYYY')='" + mon
					+ "' THEN P.USCNO END)),0)  CNT_" + mon.replace("-", "_")
					+ "\n,NVL(SUM(CASE WHEN TO_CHAR(PDT,'MON-YYYY')='" + mon + "' THEN ROUND(NVL(ACD_COLL,0)) END),0) "
					+ mon.replace("-", "_") + " ,\n";
			summonth = summonth + "sum(" + "CNT_" + mon.replace("-", "_") + ") " + "CNT_" + mon.replace("-", "_") + " ,"
					+ "sum(" + mon.replace("-", "_") + ") " + mon.replace("-", "_") + " ,\n";// sum(MAY_2021) MAY_2021
		}
		monthscon = monthscon.substring(0, monthscon.length() - 1) + " ";
		summonth = summonth.substring(0, summonth.length() - 1) + " ";

		String sql = "select nvl(CIRCLE,'APCPDCL') CIRCLE,nvl(TYPE,'TOTAL') TYPE, nvl(CIRCLE,'APCPDCL')||nvl(TYPE,'TOTAL') CIR_TYPE ,nvl(CTCAT,'TOTAL') CTCAT,SUM(NOS) NOS,\r\n"
				+ "sum(SD_TO_BE_MAINTAINED) SD_TO_BE_MAINTAINED,sum(SD_AVAILABLE) SD_AVAILABLE,sum(ACD_CALCULATED) ACD_CALCULATED,SUM(NVL(DR_ACD,0)) TOT_DR_ACD "
				+ summonth
				+ " sum(TOT_CNT) TOTAL_CNT,sum(TOT_PAY_ACD)  TOT_ACD_PAY,SUM(NVL(CR_ACD,0)) TOT_CR_ACD,SUM(NVL(BAL_NOS,0)) BAL_CNT,SUM(NVL(BAL_ACD,0)) BAL_ACD_TO_BE_PAID "
				+ "from \r\n"
				+ "(select SUBSTR(CTUSCNO,1,3) CIRCLE, CASE  WHEN NVL(CTGOVT_PVT,'N')='Y' THEN 'GOVT' ELSE 'NON-GOVT' END  TYPE ,CTCAT ,COUNT(distinct(a.uscno)) NOS,\r\n"
				+ "    sum(ROUND(SD_TO_BE_MAINTAINED)) SD_TO_BE_MAINTAINED,sum(ROUND(SD_AVAILABLE)) SD_AVAILABLE,sum(ROUND(ACD_CALCULATED)) ACD_CALCULATED,sum(nvl(dr_acd_coll,0)) DR_ACD \r\n"
				+ monthscon
				+ "NVL(COUNT(DISTINCT(P.USCNO)),0)  TOT_CNT,NVL(SUM(NVL(ACD_COLL,0)),0)  TOT_PAY_ACD,sum(nvl(Cr_acd_coll,0)) CR_ACD,COUNT(distinct(a.uscno))-COUNT(distinct(P.uscno)) BAL_NOS,sum(ROUND(NVL(ACD_CALCULATED,0))-NVL(ACD_COLL,0)) BAL_ACD \n"
				+ "from CONS,(SELECT USCNO,(NVL(REQ_SD,0)) SD_TO_BE_MAINTAINED,(NVL(AVAIL_SD,0)) SD_AVAILABLE,(NVL(NET_ACD,0)) ACD_CALCULATED FROM ACD_CALC_HT,CONS WHERE USCNO=CTUSCNO  AND LEVI_FLG='Y' AND FIN_YEAR='2021-2022' AND LEVI_MTH='MAY-2022') A,\r\n"
				+ "(select USCNO,TRUNC(PAY_DATE,'MM') PDT,sum(NVL(PACD,0) ) ACD_COLL from PAY_HT  where NVL(trim(PAY_STA_FLG),0) not in('X','E')  AND NVL(PACD,0)>0 GROUP BY USCNO,TRUNC(PAY_DATE,'MM')\r\n"
				+ "UNION select USCNO,TRUNC(PAY_DATE,'MM') PDT,sum(NVL(PACD,0) ) ACD_COLL\r\n" + " from PAY_HT_HIST\r\n"
				+ " where NVL(trim(PAY_STA_FLG),0) not in('X','E') AND NVL(PACD,0)>0 AND TO_DATE(PAY_DATE,'dd-mm-yy')>=TO_DATE('"
				+ obj[0] + "','dd-mm-yy') and TO_DATE(PAY_DATE,'dd-mm-yy')<=TO_DATE('" + obj[1]
				+ "','dd-mm-yy') GROUP BY USCNO,TRUNC(PAY_DATE,'MM')) P,\r\n"
				+ "(select USCNO,sum(CASE WHEN RJTYPE='CR' THEN NVL(ACD,0) ELSE 0 END) CR_ACD_COLL,sum(CASE WHEN RJTYPE='DR' THEN NVL(ACD,0) ELSE 0 END) DR_ACD_COLL from JOURNAL  where NVL(trim(status),'Y')  in('C')  AND NVL(ACD,0)>0 GROUP BY USCNO\r\n"
				+ "UNION select USCNO,sum(CASE WHEN RJTYPE='CR' THEN NVL(ACD,0) ELSE 0 END) CR_ACD_COLL,sum(CASE WHEN RJTYPE='DR' THEN NVL(ACD,0) ELSE 0 END) DR_ACD_COLL\r\n"
				+ " from JOURNAL_HIST\r\n"
				+ " where NVL(trim(status),'Y')  in('C') AND NVL(ACD,0)>0 AND TO_DATE(RJDT,'dd-mm-yy')>=TO_DATE('"
				+ obj[0] + "','dd-mm-yy') and TO_DATE(RJDT,'dd-mm-yy')<=TO_DATE('" + obj[1]
				+ "','dd-mm-yy') GROUP BY USCNO) J\r\n"
				+ " WHERE CTUSCNO=P.USCNO(+) AND CTUSCNO=A.USCNO AND CTUSCNO=J.USCNO(+)\r\n"
				+ " GROUP BY SUBSTR(CTUSCNO,1,3), CASE  WHEN NVL(CTGOVT_PVT,'N')='Y' THEN 'GOVT' ELSE 'NON-GOVT' END,CTCAT ORDER BY CIRCLE)\r\n"
				+ "GROUP BY CUBE (CIRCLE,TYPE,CTCAT)\r\n" + "ORDER BY \r\n"
				+ "case when CIRCLE = 'VJA' then '001' when CIRCLE = 'GNT' then '002' when CIRCLE = 'ONG' then '003'  when CIRCLE = 'CRD' then '009' else CIRCLE end,case when TYPE='GOVT' then '001' when TYPE = 'NON-GOVT' then '002' else TYPE end,CTCAT";

		return sql;
	}

	public LinkedHashMap<String, Object> getServiceTypes() {
		String sql = "select STCODE,STDESC from SERVTYPE order by stdesc asc";
		return jdbcTemplate.query(sql, (ResultSet rs) -> {
			LinkedHashMap<String, Object> results = new LinkedHashMap<>();
			while (rs.next()) {
				results.put(rs.getString("STCODE"), rs.getString("STDESC"));
			}
			return results;
		});
	}

	public LinkedHashMap<String, Object> getFeeders(String cir) {
		String sql = cir.equals("ALL")
				? "select distinct fmsapfcode,FMFNAME from feedermast where fmsapfcode in (select ctfeeder_code from cons)"
				: "select distinct fmsapfcode,FMFNAME from feedermast where fmsapfcode in (select ctfeeder_code from cons where substr(ctuscno,1,3)='"
						+ cir + "')";
		return jdbcTemplate.query(sql, (ResultSet rs) -> {
			LinkedHashMap<String, Object> results = new LinkedHashMap<>();
			while (rs.next()) {
				results.put(rs.getString("fmsapfcode"), rs.getString("FMFNAME"));
			}
			return results;
		});
	}

	public LinkedHashMap<String, Object> getSubDivisonFeeders(String subdivid) {
		String sql = "";
		if (subdivid.equals("0")) {
			sql = "select distinct fmsapfcode,FMFNAME from feedermast\n"
					+ "where fmsapfcode in (select ctfeeder_code from cons,master.spdclmaster where SUBSTR(CTSECCD,-5)=SECCD(+))";
			log.info(sql);
			return jdbcTemplate.query(sql, new Object[] {}, (ResultSet rs) -> {
				LinkedHashMap<String, Object> results = new LinkedHashMap<>();
				while (rs.next()) {
					results.put(rs.getString("fmsapfcode"), rs.getString("FMFNAME"));
				}

				return results;
			});
		} else {
			sql = " select distinct fmsapfcode,FMFNAME from feedermast where fmsapfcode in (select ctfeeder_code from cons,master.spdclmaster where SUBSTR(CTSECCD,-5)=SECCD(+) and SUBCD = ? )";
			log.info(sql);

			return jdbcTemplate.query(sql, new Object[] { subdivid }, (ResultSet rs) -> {
				LinkedHashMap<String, Object> results = new LinkedHashMap<>();
				while (rs.next()) {
					results.put(rs.getString("fmsapfcode"), rs.getString("FMFNAME"));
				}

				return results;
			});
		}

	}

	public List<Map<String, Object>> getISUTXTData() {
		String sql = "select * from V_HT_ISU_DATA,V_VIRTUAL_IFSC where ctuscno = uscno";
		log.info(sql);
		return jdbcTemplate.queryForList(sql, new Object[] {});
	}

	public List<Map<String, Object>> m3getISUTXTData() {
		String sql = "select * from V_M4_HT_ISU_DATA,V_VIRTUAL_IFSC where ctuscno = uscno ";
		log.info(sql);
		return jdbcTemplateDev.queryForList(sql, new Object[] {});
	}

	public List<Map<String, Object>> m3getMoveIn() {
		String sql = "select  *  from V_M4_HT_ISU_DATA where MTR_STATUS in ('03','01')";
		log.info(sql);
		return jdbcTemplateDev.queryForList(sql, new Object[] {});
	}

	public List<Map<String, Object>> m3getSecurityData() {
		String sql = "select * from V_M4_HT_ISU_DATA where  ( CB_SD>0 or CB_ACD>0) ";
		log.info(sql);
		return jdbcTemplateDev.queryForList(sql, new Object[] {});
	}

	public List<Map<String, Object>> m3getACDData() {
		String sql = "select * from V_M4_HT_ISU_DATA where  ACD>0 ";
		log.info(sql);
		return jdbcTemplateDev.queryForList(sql, new Object[] {});
	}

	public List<Map<String, Object>> m3getUDC() {
		String sql = "select * from V_M4_DISC";
		log.info(sql);
		return jdbcTemplateDev.queryForList(sql, new Object[] {});
	}

	public List<Map<String, Object>> m3getCONNOBJECT() {
		String sql = "select * from V_M4_HT_ISU_DATA,V_VIRTUAL_IFSC where ctuscno = uscno and mtr_status in ('01','02','03')";
		log.info(sql);
		return jdbcTemplateDev.queryForList(sql, new Object[] {});
	}

	public List<Map<String, Object>> getM3_LT_DM_DEVLOC() {
		String sql = "select distinct USCNO CTUSCNO,  HT_AUTH_GROUP from V_M4_LT_MTR_CT_PT_DATA";
		log.info(sql);
		return jdbcTemplateLTDev.queryForList(sql, new Object[] {});
	}

	public List<Map<String, Object>> getISU_CS_FICA_ACCOUNT_V1() {
		String sql = "select * from V_HT_ISU_DATA";
		log.info(sql);
		return jdbcTemplate.queryForList(sql, new Object[] {});
	}

	public List<Map<String, Object>> getM3ISU_CS_FICA_ACCOUNT_V1() {
		String sql = "select * from V_M4_HT_ISU_DATA";
		log.info(sql);
		return jdbcTemplateDev.queryForList(sql, new Object[] {});
	}

	public List<Map<String, Object>> getHTMASTER() {
		String sql = "select * from v_ht_master";
		log.info(sql);
		return jdbcTemplateDev.queryForList(sql, new Object[] {});
	}

	public List<Map<String, Object>> getDM_FULL_INST_DATA() {

		String sql = "select * from FULL_INST_DATA where REG_GROUP is not null and REG_CODE is not null and SERIAL_NO is not null ";
		// String sql = "select * from V_FULL_INST_DATA_CT";
		log.info(sql);
		return jdbcTemplate.queryForList(sql, new Object[] {});
	}

	public List<Map<String, Object>> getV_M3_LT_FULL_INST_data() {

		String sql = "select * from V_M4_LT_FULL_INST_data where REG_GROUP is not null and REG_CODE is not null and SERIAL_NO is not null  ";
		// String sql = "select * from V_FULL_INST_DATA_CT";
		log.info(sql);
		return jdbcTemplateLTDev.queryForList(sql, new Object[] {});
	}

	public List<Map<String, Object>> getV_M3_HT_FULL_INST_data() {
		String sql = "select * from VJA_FULL_INST_DATA where ctuscno in ("
				+ "select SERIAL_NO  from HT_MNP_METER_DETAILS_ALL_DEC23 where METER_POSITION in ('MM','Colony Meter') and uscno like 'VJA%' union all "
				+ "select SERIAL_NO  from HT_MNP_METER_DETAILS_CTM where METER_POSITION in ('MM','Colony Meter') and uscno like 'VJA%'"
				+ ") ";
		// String sql = "select * from crd_full_inst_data_temp";
		log.info(sql);
		return jdbcTemplateDev.queryForList(sql, new Object[] {});
	}

	public List<Map<String, Object>> getCTV_M3_HT_FULL_INST_data() {
		String sql = "select  * from V_M4_FULL_INST_DATA_CT";
		log.info(sql);
		return jdbcTemplateDev.queryForList(sql, new Object[] {});
	}

	public List<Map<String, Object>> getTECHINSTALLATION_DATA() {
		String sql = "select * from V_M2_TECHINSTALLATION";
		log.info(sql);
		return jdbcTemplate.queryForList(sql, new Object[] {});
	}

	public List<Map<String, Object>> getLTTECHINSTALLATION_DATA() {
		String sql = "select * from V_M4_LT_TECHINSTALLATION";
		log.info(sql);
		return jdbcTemplateLTDev.queryForList(sql, new Object[] {});
	}

	public List<Map<String, Object>> getHTTECHINSTALLATION_DATA() {
		String sql = "select * from V_M3_HT_TECHINSTALLATION";
		log.info(sql);
		return jdbcTemplate.queryForList(sql, new Object[] {});
	}

	public List<Map<String, Object>> getV_DEVICEGRP() {
		String sql = "select * from V_M3_DEVICEGRP";
		log.info(sql);
		return jdbcTemplate.queryForList(sql, new Object[] {});
	}

	public List<Map<String, Object>> getm3V_DEVICEGRP() {
		String sql = "select * from V_M4_DEVICEGRP ";
		log.info(sql);
		return jdbcTemplateDev.queryForList(sql, new Object[] {});
	}

	public List<Map<String, Object>> getOPENITEMS() {
		String sql = "select * from V_M4_OPENITEMS_DATA";
		log.info(sql);
		return jdbcTemplateDev.queryForList(sql, new Object[] {});
	}

	public List<Map<String, Object>> getMTR_RDG_UNPIVOT() {
		String sql = "select * from vja_mtr_rdg_data where MDCLRDG_DT is not null order by SERIAL_NO,to_date(MDCLRDG_DT,'YYYYMMDD'),to_number(reg)";
		log.info(sql);
		return jdbcTemplateDev.queryForList(sql, new Object[] {});
	}

	public List<Map<String, Object>> getMTR_RDG_UNPIVOTCT() {
		String sql = "select * from V_M4_MTR_RDG_DATA_CT";
		log.info(sql);
		return jdbcTemplateDev.queryForList(sql, new Object[] {});
	}

	public List<Map<String, Object>> getMTR_RDG_UNPIVOT_01() {
		String sql = "select * from V_M3_MTR_RDG_DATA   order by TEMP_SERIAL_NO,to_date(MDCLRDG_DT,'YYYYMMDD'),REG";
		log.info(sql);
		return jdbcTemplate.queryForList(sql, new Object[] {});
	}

	public List<Map<String, Object>> getFacts() {
		String sql = "select * from V_M4_FACTS ";
		log.info(sql);
		return jdbcTemplateDev.queryForList(sql, new Object[] {});
	}

	public List<Map<String, Object>> getNewFacts() {
		String sql = "select * from V_M4_NEW_SERVICE_FACTS ";
		log.info(sql);
		return jdbcTemplateDev.queryForList(sql, new Object[] {});
	}
	/*
	 * public List<Map<String, Object>> getHT_CT_PT_DATA() { String sql =
	 * "select * from HT_CT_PT_DATA"; log.info(sql); return
	 * jdbcTemplate.queryForList(sql,new Object[]{}); }
	 */

	public List<Map<String, Object>> getDMDEVICECTPT() {
		String sql = "select * from V_M2_CT_PT_DATA";
		log.info(sql);
		return jdbcTemplate.queryForList(sql, new Object[] {});
	}

	public List<Map<String, Object>> getM3DMDEVICECTPT() {
		String sql = "select * from V_M4_CT_PT_DATA";
		log.info(sql);
		return jdbcTemplateDev.queryForList(sql, new Object[] {});
	}

	public List<Map<String, Object>> getM3DMDEVICECTPTCC() {
		String sql = "select * from V_M4_CT_PT_DATA_CC";
		log.info(sql);
		return jdbcTemplateDev.queryForList(sql, new Object[] {});
	}

	public List<Map<String, Object>> getLTDMDEVICECTPT() {
		String sql = "select * from V_M4_LT_CT_PT_DATA";
		log.info(sql);
		return jdbcTemplateLTDev.queryForList(sql, new Object[] {});
	}

	public List<Map<String, Object>> getHT_CT_PT_DATA_33_132() {
		String sql = "select * from V_M2_CT_PT_DATA_33_132 where  USCNO_T in ('GNT4000','GNT4111')";
		/*
		 * String sql = "select * from V_M2_CT_PT_DATA_33_132 where USCNO_T='CRD643'";
		 */
		log.info(sql);
		return jdbcTemplate.queryForList(sql, new Object[] {});
	}

	public List<Map<String, Object>> getm3HT_CT_PT_DATA_33_132() {
		String sql = "select * from V_M4_CT_PT_DATA_33_132 ";
		/*
		 * String sql =
		 * "select * from V_M4_CT_PT_DATA_33_132  where USCNO like 'GNT625%'";
		 */
		/*
		 * String sql = "select * from V_M2_CT_PT_DATA_33_132 where USCNO_T='CRD643'";
		 */
		log.info(sql);
		return jdbcTemplateDev.queryForList(sql, new Object[] {});
	}

	public List<Map<String, Object>> getm3HT_CT_PT_DATA_33_132_CC() {
		String sql = "select * from V_M4_CT_PT_DATA_33_132_CC ";
		/*
		 * String sql =
		 * "select * from V_M4_CT_PT_DATA_33_132  where USCNO like 'GNT625%'";
		 */
		/*
		 * String sql = "select * from V_M2_CT_PT_DATA_33_132 where USCNO_T='CRD643'";
		 */
		log.info(sql);
		return jdbcTemplateDev.queryForList(sql, new Object[] {});
	}

	public List<Map<String, Object>> getm3SMCMTECHINSTALLATION() {
		String sql = "select distinct CTUSCNO, REG_GROUP, SERIAL_NO, C_SERIAL_NO, VALIDFROMT from V_M4_SMCM_FULL_INST_DATA";
		/* String sql = "select * from V_M4_SMCMTECHINSTALLATION"; */
		/*
		 * String sql = "select * from V_M2_CT_PT_DATA_33_132 where USCNO_T='CRD643'";
		 */
		log.info(sql);
		return jdbcTemplateDev.queryForList(sql, new Object[] {});
	}

	public List<Map<String, Object>> getHT_MTR_CT_PT_DATA() {
		// String sql = "select * from HT_MTR_CT_PT_DATA";
		/*
		 * String sql =
		 * "select USCNO, M_CTR, M_PTR, VALID_FROM, YEAR, MON_YEAR, SERIAL_NO, DEVICECA, METER_MAKE, CER_NUM, INS_YEAR, CER_DATE, NEXT_REP, METER_POSITION, ACT_COM_DT, CTACTUAL_KV, \r\n"
		 * + "HT_AUTH_GROUP, METER_POSITION_T, MAIN_PLAN, METER_OWNER, M_CT_CLASS, \r\n"
		 * +
		 * "case when (select max(MDMONTH) from mtrdat_hist where to_char(MDMONTH,'MON-YYYY')='APR-2022' and  mscno=USCNO and MDMTRNO=SERIAL_NO group by mscno,MDMTRNO) is null then\r\n"
		 * + "VALIDFROMT else '20220401' end \r\n" +
		 * "VALIDFROMT ,MNO ,EQTYP from V_M2_HT_MTR_CT_PT_DATA";
		 */
		String sql = "select USCNO, M_CTR, M_PTR, VALID_FROM, YEAR, MON_YEAR, SERIAL_NO, DEVICECA, METER_MAKE, CER_NUM, INS_YEAR, CER_DATE, NEXT_REP, METER_POSITION, ACT_COM_DT, CTACTUAL_KV, \r\n"
				+ "HT_AUTH_GROUP, METER_POSITION_T, MAIN_PLAN, METER_OWNER, M_CT_CLASS, \r\n"
				+ "VALIDFROMT ,MNO ,EQTYP,MNO_TEMP from V_M2_HT_MTR_CT_PT_DATA";
		log.info(sql);
		return jdbcTemplate.queryForList(sql, new Object[] {});
	}

	public List<Map<String, Object>> getLT_MTR_CT_PT_DATA() {
		// String sql = "select * from HT_MTR_CT_PT_DATA";
		/*
		 * String sql =
		 * "select USCNO, M_CTR, M_PTR, VALID_FROM, YEAR, MON_YEAR, SERIAL_NO, DEVICECA, METER_MAKE, CER_NUM, INS_YEAR, CER_DATE, NEXT_REP, METER_POSITION, ACT_COM_DT, CTACTUAL_KV, \r\n"
		 * + "HT_AUTH_GROUP, METER_POSITION_T, MAIN_PLAN, METER_OWNER, M_CT_CLASS, \r\n"
		 * +
		 * "case when (select max(MDMONTH) from mtrdat_hist where to_char(MDMONTH,'MON-YYYY')='APR-2022' and  mscno=USCNO and MDMTRNO=SERIAL_NO group by mscno,MDMTRNO) is null then\r\n"
		 * + "VALIDFROMT else '20220401' end \r\n" +
		 * "VALIDFROMT ,MNO ,EQTYP from V_M2_HT_MTR_CT_PT_DATA";
		 */
		String sql = "select * from V_M4_LT_MTR_CT_PT_DATA";
		log.info(sql);
		return jdbcTemplateLTDev.queryForList(sql, new Object[] {});
	}

	public List<Map<String, Object>> getm3HT_MTR_CT_PT_DATA() {
		String sql = "select * from V_M4_HT_MTR_CT_PT_DATA ";
		log.info(sql);
		return jdbcTemplateDev.queryForList(sql, new Object[] {});
	}

	public List<Map<String, Object>> getm3HT_MTR_CT_PT_DATA_MC() {
		String sql = "select * from V_M4_HT_MTR_CT_PT_DATA_MC ";
		log.info(sql);
		return jdbcTemplateDev.queryForList(sql, new Object[] {});
	}

	public List<Map<String, Object>> getm3HT_MTR_CT_DATA() {
		String sql = "select * from V_M4_HT_MTR_CT_PT_DATA_CTM";
		log.info(sql);
		return jdbcTemplateDev.queryForList(sql, new Object[] {});
	}

	public List<Map<String, Object>> getHT_MTR_CT_PT_DATA_NO_DATA() {
		// String sql = "select * from HT_MTR_CT_PT_DATA";
		/*
		 * String sql =
		 * "select USCNO, M_CTR, M_PTR, VALID_FROM, YEAR, MON_YEAR, SERIAL_NO, DEVICECA, METER_MAKE, CER_NUM, INS_YEAR, CER_DATE, NEXT_REP, METER_POSITION, ACT_COM_DT, CTACTUAL_KV, \r\n"
		 * + "HT_AUTH_GROUP, METER_POSITION_T, MAIN_PLAN, METER_OWNER, M_CT_CLASS, \r\n"
		 * +
		 * "case when (select max(MDMONTH) from mtrdat_hist where to_char(MDMONTH,'MON-YYYY')='APR-2022' and  mscno=USCNO and MDMTRNO=SERIAL_NO group by mscno,MDMTRNO) is null then\r\n"
		 * + "VALIDFROMT else '20220401' end \r\n" +
		 * "VALIDFROMT ,MNO ,EQTYP from V_M2_HT_MTR_CT_PT_DATA";
		 */
		String sql = "select USCNO, M_CTR, M_PTR, VALID_FROM, YEAR, MON_YEAR, SERIAL_NO, DEVICECA, METER_MAKE, CER_NUM, INS_YEAR, CER_DATE, NEXT_REP, METER_POSITION, ACT_COM_DT, CTACTUAL_KV, \r\n"
				+ "HT_AUTH_GROUP, METER_POSITION_T, MAIN_PLAN, METER_OWNER, M_CT_CLASS, \r\n"
				+ "VALIDFROMT ,MNO ,EQTYP,MNO_TEMP from V_M2_HT_MTR_CT_PT_DATA_NO_DATA";
		log.info(sql);
		return jdbcTemplate.queryForList(sql, new Object[] {});
	}

	public List<Map<String, Object>> getEqpSeq() {
		// String sql = "select * from HT_MTR_CT_PT_DATA";
		// String sql = "select EQUIPMENT, MM_CODE from PM_EQUIPMENTS where MM_CODE is
		// not null order by MM_CODE,EQUIPMENT";
		// String sql ="select EQUIPMENT_ID EQUIPMENT,DEVICE_CAT MM_CODE from
		// HT_MNP_CUBICAL_DETAILS where DEVICE_CAT is not null";
		// String sql ="select ROWID EQUIPMENT,DEVICE_CAT MM_CODE from
		// HT_CT_PT_DEVICE_CAT where DEVICE_CAT is not null";

		// FOR NULL SEQ
		// String sql ="select EQUIPMENT_ID EQUIPMENT,DEVICE_CAT MM_CODE from
		// HT_MNP_CUBICAL_DETAILS where upper(CUBICAL_TYPE) like 'C%' and seq is null
		// and PM_EQUIPMENT_NO is null and DEVICE_CAT is not null";

		// MM_CODE NULL
		// String sql = "select EQUIPMENT,rowid MM_CODE from PM_EQUIPMENTS where MM_CODE
		// is null order by MM_CODE,EQUIPMENT";

		// String sql ="select ROWID EQUIPMENT,DEVICE_CAT MM_CODE from
		// HT_CT_PT_DEVICE_CAT where PM_EQUIPMENT is null and DEVICE_CAT is not null";

		String sql = "select  PM_EQUIPMENT EQUIPMENT ,USCNOCTPT MM_CODE from HT_CT_PT_DEVICE_CAT_MOCK2 order by USCNOCTPT, EQUIP_IND";
		log.info(sql);
		/*
		 * Map<String, String> map = new HashMap<>(); Map<String, Map<String, Integer>>
		 * seqmap = new HashMap<>(); List<Map<String, Object>> list =
		 * jdbcTemplate.queryForList(sql,new Object[]{}); for(Map<String, Object> d :
		 * list) { map.put(d.get("MM_CODE").toString(), d.get("EQUIPMENT").toString());
		 * }
		 */
		return jdbcTemplate.queryForList(sql, new Object[] {});
	}

	public Map<String, List<IdTypes>> getIDTypes() {
		String sql = "select * from V_ID_TYPES ";
		log.info(sql);
		/* return jdbcTemplate.queryForList(sql,new Object[]{}); */
		Map<String, List<IdTypes>> map = new HashMap<String, List<IdTypes>>();
		List<IdTypes> list = jdbcTemplateDev.query(sql, new Object[] {}, new BeanPropertyRowMapper(IdTypes.class));
		for (IdTypes id : list) {
			if (map.containsKey(id.getUSCNO())) {
				map.get(id.getUSCNO()).add(id);
			} else {
				List<IdTypes> temp = new ArrayList<IdTypes>();
				temp.add(id);
				map.put(id.getUSCNO(), temp);
			}
		}

		return map;
	}

	public static String queryBuilderForTrueUpCharges(Object[] obj, String fy) {
		String monthscon = ",";
		String summonth = ",";
		String sfy[] = fy.split("-");
		Iterator<String> itr = ((List<String>) obj[2]).iterator();
		while (itr.hasNext()) {
			String mon = itr.next();
			monthscon = monthscon + "count(distinct(case when to_char(LDT,'MON-YYYY')='" + mon
					+ "'  THEN USCNO END)) NOS_" + mon.replace("-", "_") + ",\r\n"
					+ "round(nvl(SUM(case when to_char(LDT,'MON-YYYY')='" + mon
					+ "'  then NVL(T_UNITS,0) END),0)) SALES_" + mon.replace("-", "_") + ",\r\n"
					+ "round(nvl(SUM(case when to_char(LDT,'MON-YYYY')='" + mon
					+ "'  then NVL(T_DEMAND,0) END),0)) DEM_" + mon.replace("-", "_") + ",\r\n"
					+ "round(nvl(SUM(case when to_char(LDT,'MON-YYYY')='" + mon
					+ "'   then NVL(T_COLLECTION,0) END),0)) COLL_" + mon.replace("-", "_") + ",\r\n"
					+ "round(nvl(SUM(case when to_char(LDT,'MON-YYYY')='" + mon + "'   then NVL(T_CB,0) END),0)) CB_"
					+ mon.replace("-", "_") + ",";

			summonth = summonth + "sum(" + "NOS_" + mon.replace("-", "_") + ") " + "NOS_" + mon.replace("-", "_")
					+ " ,\n" + "sum(SALES_" + mon.replace("-", "_") + ") SALES_" + mon.replace("-", "_") + " ,\n"
					+ "sum(DEM_" + mon.replace("-", "_") + ") DEM_" + mon.replace("-", "_") + " ,\n" + "sum(COLL_"
					+ mon.replace("-", "_") + ") COLL_" + mon.replace("-", "_") + " ,\n" + "sum(CB_"
					+ mon.replace("-", "_") + ") CB_" + mon.replace("-", "_") + " ,";// sum(MAY_2021) MAY_2021

		}
		monthscon = monthscon.substring(0, monthscon.length() - 1) + " ";
		summonth = summonth.substring(0, summonth.length() - 1) + " ";

		String sql = "select nvl(CIRCLE,'APCPDCL') CIRCLE,nvl(TYPE,'TOTAL') TYPE, nvl(CIRCLE,'APCPDCL')||nvl(TYPE,'TOTAL') CIR_TYPE ,nvl(CTCAT,'TOTAL') CTCAT \n"
				+ summonth
				+ "from ( select SUBSTR(USCNO,1,3) CIRCLE,CASE  WHEN NVL(CTGOVT_PVT,'N')='Y' THEN 'GOVT' ELSE 'NON-GOVT' END  TYPE,CTCAT \n"
				+ monthscon + " from cons,\r\n"
				+ "(SELECT USCNO ,CAT,SCAT,TRUNC(TO_DATE(MON_YEAR,'MON-YYYY')) LDT,(DECODE(STATUS,NULL,0,'NEW',1,'LIVE',1,1,1,0)) STAT,\r\n"
				+ "(NVL(MN_KVAH,0)) T_UNITS,Nvl(OB_TUPC,0) T_OB,\r\n"
				+ "(NVL(TUPC,0)+Nvl(Drj_TUPC,0)) T_DEMAND,(Nvl(PAID_TUPC,0)+NVL(CRJ_TUPC,0)) T_COLLECTION,Nvl(CB_TUPC,0) T_CB\r\n"
				+ " from  ( select USCNO,CAT,SCAT,MON_YEAR,STATUS,MN_KVAH,OB_TUPC,TUPC,Drj_TUPC,PAID_TUPC,CRJ_TUPC,CB_TUPC from ledger_ht_hist \r\n"
				+ " union all \r\n"
				+ " select USCNO,CAT,SCAT,MON_YEAR,STATUS,MN_KVAH,OB_TUPC,TUPC,Drj_TUPC,PAID_TUPC,CRJ_TUPC,CB_TUPC from accountcopy )  WHERE TO_DATE(MON_YEAR,'MON-YYYY') between '01-APR-"
				+ sfy[0] + "' and '31-MAR-" + sfy[1]
				+ "' AND TUPC>0 and SUBSTR(USCNO,1,3) IN('GNT','VJA','ONG','CRD'))\r\n" + " WHERE USCNO=CTUSCNO  \r\n"
				+ "  GROUP BY SUBSTR(USCNO,1,3),CASE  WHEN NVL(CTGOVT_PVT,'N')='Y' THEN 'GOVT' ELSE 'NON-GOVT' END,CTCAT\r\n"
				+ "ORDER BY  CIRCLE)\r\n" + "GROUP BY CUBE (CIRCLE,TYPE,CTCAT)\r\n" + "ORDER BY \r\n"
				+ "case when CIRCLE = 'VJA' then '001' when CIRCLE = 'GNT' then '002' when CIRCLE = 'ONG' then '003'  when CIRCLE = 'CRD' then '009' else CIRCLE end,case when TYPE='GOVT' then '001' when TYPE = 'NON-GOVT' then '002' else TYPE end,CTCAT\r\n";

		return sql;
	}

	public Map<String, Integer> getServiceNoCount() {
		String sql = "select TEMP_SERIAL_NO,count(*) COUNT from vja_mtr_rdg_data  group by TEMP_SERIAL_NO ";
		log.info("Executing Query { " + sql + " }");
		return jdbcTemplateDev.query(sql, (ResultSet rs) -> {
			HashMap<String, Integer> results = new LinkedHashMap<>();
			while (rs.next()) {
				results.put(rs.getString("TEMP_SERIAL_NO"), rs.getInt("COUNT"));
			}
			return results;
		});
	}

	public Map<String, String> getDivisons(String circleid) {
		circleid = circleid.replace("ONG", "4").replace("GNT", "1").replace("VJA", "6").replace("CRD", "9");

		HashMap<String, String> results = new HashMap<>();
		results.put("0", "ALL");

		String sql = "select DIVCD,DIVNAME from spdclmaster where CIRCD=? group by DIVCD,DIVNAME order by DIVCD ";
		log.info(sql);
		return jdbcTemplate.query(sql, new Object[] { circleid.trim() }, (ResultSet rs) -> {

			while (rs.next()) {
				results.put(rs.getString("DIVCD"), rs.getString("DIVNAME"));
			}
			return results;
		});

	}

	public Map<String, String> getSubDivisons(String divisionid) {
		HashMap<String, String> results = new HashMap<>();
		results.put("0", "ALL");

		if (divisionid.equals("0")) {
			String sql = "select SUBCD,SUBNAME from spdclmaster group by SUBCD,SUBNAME order by SUBCD";
			return jdbcTemplate.query(sql, (ResultSet rs) -> {
				while (rs.next()) {
					results.put(rs.getString("SUBCD"), rs.getString("SUBNAME"));
				}
				return results;
			});
		} else {
			String sql = "select SUBCD,SUBNAME from spdclmaster where DIVCD=? group by SUBCD,SUBNAME order by SUBCD";
			return jdbcTemplate.query(sql, new Object[] { divisionid.trim() }, (ResultSet rs) -> {
				while (rs.next()) {
					results.put(rs.getString("SUBCD"), rs.getString("SUBNAME"));
				}
				return results;
			});
		}
	}

	public Map<String, String> getSection(String subdivisionid) {
		String sql = "select SECCD,SECNAME from spdclmaster where SUBCD=? group by SECCD,SECNAME order by SECCD";
		return jdbcTemplate.query(sql, new Object[] { subdivisionid.trim() }, (ResultSet rs) -> {
			HashMap<String, String> results = new HashMap<>();
			while (rs.next()) {
				results.put(rs.getString("SECCD"), rs.getString("SECNAME"));
			}
			return results;
		});
	}
}
