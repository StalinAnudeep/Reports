
package com.spdcl.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.spdcl.dao.NewReportDao;
import com.spdcl.model.TodDetails;

@Controller
public class NewReportController {
	private static Logger log = Logger.getLogger(ReportController.class);
	@Autowired
	NewReportDao newReportDao;

	// 125
	@GetMapping("/fyConsumption")
	public String getConsumptionReport() {
		return "fyConsumption";
	}

	@PostMapping("/fyConsumption")
	public ModelAndView getFinancialConsumption(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("fyConsumption");
		List<Map<String, Object>> fyConsumptionReport = newReportDao.getFinancialConsumption(request);
		String type = request.getParameter("circle");
		String year = request.getParameter("year");
		System.out.println(fyConsumptionReport);
		if (fyConsumptionReport.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("fyConsumptionReport", fyConsumptionReport);
			mav.addObject("type", type);
			mav.addObject("CIRCOUNT", countFrequencies(fyConsumptionReport));

			if (type.equals("ALL")) {

				mav.addObject("title", "Financial Year Consumption Report For APCPDCL - " + year);
			} else {
				mav.addObject("title", "Financial Year Consumption Report For - " + year);

			}

		}
		return mav;

	}

	// 23A
	@GetMapping("/sentEmails")
	public String getAccountCopyPage() {
		return "sentEmails";
	}

	@PostMapping("/sentEmails")
	public ModelAndView getEmailsAndSms(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("sentEmails");
		List<Map<String, Object>> mailAndSmsReport = newReportDao.getEmailsAndSmsReport(request);
		String type = request.getParameter("type");
		String circle = request.getParameter("circle");
		String monthYear = request.getParameter("month") + "-" + request.getParameter("year");
		System.out.println(mailAndSmsReport);

		if (mailAndSmsReport.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("mailAndSmsReport", mailAndSmsReport);
			mav.addObject("type", type);

			if (circle.equals("ALL")) {

				mav.addObject("title", "Number Of " + type + " sent For APCPDCL - " + monthYear);
			} else {
				mav.addObject("title", "Number Of " + type + " sent For " + circle + "  - " + monthYear);
			}

		}
		return mav;

	}

	// 97D
	@GetMapping("/HtDCBCollectionSplitMonthlyWise")
	public String getHtDCBCollectionSplitMonthlyWiseAbstract() {
		return "HtDCBCollectionSplitMonthlyWise";
	}

	@PostMapping("/HtDCBCollectionSplitMonthlyWise")
	public ModelAndView getHtDCBCollectionSplitMonthlyWiseAbstract(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		ModelAndView mav = new ModelAndView("HtDCBCollectionSplitMonthlyWise");
		List<Map<String, Object>> tp_sale = newReportDao.getHtDCBCollectionSplitMonthlyWiseAbstract(request);

		System.out.println(tp_sale);
		if (tp_sale.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("tp_sale", tp_sale);
			mav.addObject("CIRCOUNT", countFrequencies(tp_sale));
			mav.addObject("CIR", circle);
			mav.addObject("title",
					"HT DCB COLLECTION SPLIT MONTHLY ABSTRACT  FOR " + (circle.equals("ALL") ? "APCPDCL" : circle));
		}
		return mav;

	}

	// 123
	@GetMapping("/monthWiseTariffReport")
	public String getMonthWiseTariffReport() {
		return "monthWiseTariffReport";
	}

	@PostMapping("/monthWiseTariffReport")
	public ModelAndView getMonthWiseTariffReport(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("monthWiseTariffReport");
		List<Map<String, Object>> monthWiseTariff = newReportDao.getMonthWiseTariffReport(request);

		System.out.println(monthWiseTariff);

		if (monthWiseTariff.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("monthWiseTariff", monthWiseTariff);
			mav.addObject("title", "Month Wise Tariff Report For - " + request.getParameter("month") + " - "
					+ request.getParameter("year"));

		}
		return mav;

	}

	// 124
	@GetMapping("/financialYearTariffReport")
	public String getFinancialYearTariffReport() {
		return "financialYearTariffReport";
	}

	@PostMapping("/financialYearTariffReport")
	public ModelAndView getFinancialYearTariffReport(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("financialYearTariffReport");
		List<Map<String, Object>> financialYearTariff = newReportDao.getFinancialYearTariffReport(request);

		System.out.println(financialYearTariff);

		if (financialYearTariff.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("financialYearTariff", financialYearTariff);
			mav.addObject("title", "Financial Year Tariff Report For - " + request.getParameter("year"));

		}
		return mav;

	}

	// 126
	@GetMapping("/fyConsumption2")
	public String getFyConsumption2() {
		return "fyConsumption2";
	}

	@PostMapping("/fyConsumption2")
	public ModelAndView getFyConsumption2(HttpServletRequest request) throws ParseException {
		ModelAndView mav = new ModelAndView("fyConsumption2");
		String year = request.getParameter("fyear") + " - " + request.getParameter("tyear");
		List<Map<String, Object>> consumptionDetails = newReportDao.getFyConsumption2(request);
		consumptionDetails.sort(Comparator.comparing((Map<String, Object> map) -> (String) map.get("FINANCIAL_YEAR"))
				.thenComparing((Map<String, Object> map) -> "Z" + ((String) map.get("TEMPCIRCLE"))));
		System.out.println(consumptionDetails);

		if (consumptionDetails.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("consumptionDetails", consumptionDetails);
			mav.addObject("circle", request.getParameter("circle"));
			mav.addObject("CIRCOUNT", countFrequencies(consumptionDetails));
			mav.addObject("title", "Financial Year Report From - " + year);

		}

		return mav;

	}

	// 127
	@GetMapping("/todConsumptionOfFyReport")
	public String getTodConsumptionOfFyReport() {
		return "todConsumptionOfFyReport";
	}

	@PostMapping("/todConsumptionOfFyReport")
	public ModelAndView getTodConsumptionOfFyReport(HttpServletRequest request) throws ParseException {
		ModelAndView mav = new ModelAndView("todConsumptionOfFyReport");
		List<Map<String, Object>> todDetails = newReportDao.getTodConsumptionOfFyReport(request);
		String year = request.getParameter("year");

		System.out.println(todDetails);

		if (todDetails.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("todDetails", todDetails);
			mav.addObject("title", "Voltage Wise,Category Wise,TOD Consumption Of FinancialYear Report - " + year);

		}

		return mav;

	}

	// 128
	@GetMapping("/todConsumptionMonthReport")
	public String getTodConsumptionMonthReport() {
		return "todConsumptionMonthReport";
	}

	@PostMapping("/todConsumptionMonthReport")
	public ModelAndView getTodConsumptionMonthReport(HttpServletRequest request) throws ParseException {
		ModelAndView mav = new ModelAndView("todConsumptionMonthReport");
		String year = request.getParameter("year");
		List<Map<String, Object>> todMonthDetails = newReportDao.getTodConsumptionMonthReport(request);
		System.out.println(todMonthDetails);

		List<Map<String, Object>> kv11Details = todMonthDetails.stream()
				.filter(details -> details.containsKey("CTACTUAL_KV")
						&& String.valueOf(details.get("CTACTUAL_KV")).equals("11"))
				.collect(Collectors.toList());

		List<Map<String, Object>> kv33Details = todMonthDetails.stream()
				.filter(details -> details.containsKey("CTACTUAL_KV")
						&& String.valueOf(details.get("CTACTUAL_KV")).equals("33"))
				.collect(Collectors.toList());

		List<Map<String, Object>> kv132Details = todMonthDetails.stream()
				.filter(details -> details.containsKey("CTACTUAL_KV")
						&& String.valueOf(details.get("CTACTUAL_KV")).equals("132"))
				.collect(Collectors.toList());

		List<TodDetails> list = new ArrayList<TodDetails>();
		for (Map<String, Object> map11 : kv11Details) {
			list.add(new TodDetails(map11.get("BILL_MON").toString(), map11.get("CTCAT").toString(),
					map11.get("CTSUBCAT").toString(), map11.get("SCS").toString(), map11.get("LOAD").toString(),
					map11.get("PEAK").toString(), map11.get("OFFPEAK").toString(), map11.get("NORMAL").toString(),
					map11.get("COLONY").toString(), null, null, null, null, null, null, null, null, null, null, null,
					null, null, null, null, null, null, null));
		}

		for (Map<String, Object> map33 : kv33Details) {
			for (TodDetails td : list) {

				if (td.getMONTH_YEAR_11().equals(map33.get("BILL_MON").toString())
						&& td.getCAT_11().equals(map33.get("CTCAT").toString())
						&& td.getSUBCAT_11().equals(map33.get("CTSUBCAT").toString())) {
					td.setMONTH_YEAR_33(map33.get("BILL_MON").toString());
					td.setCAT_33(map33.get("CTCAT").toString());
					td.setSUBCAT_33(map33.get("CTSUBCAT").toString());
					td.setSCS_33(map33.get("SCS").toString());
					td.setLOAD_33(map33.get("LOAD").toString());
					td.setPEAK_33(map33.get("PEAK").toString());
					td.setOFFPEAK_33(map33.get("OFFPEAK").toString());
					td.setNORMAL_33(map33.get("NORMAL").toString());
					td.setCOLONY_33(map33.get("COLONY").toString());
				}
			}
		}

		for (Map<String, Object> map132 : kv132Details) {
			for (TodDetails td : list) {

				if (td.getMONTH_YEAR_11().equals(map132.get("BILL_MON").toString())
						&& td.getCAT_11().equals(map132.get("CTCAT").toString())
						&& td.getSUBCAT_11().equals(map132.get("CTSUBCAT").toString())) {
					td.setMONTH_YEAR_132(map132.get("BILL_MON").toString());
					td.setCAT_132(map132.get("CTCAT").toString());
					td.setSUBCAT_132(map132.get("CTSUBCAT").toString());
					td.setSCS_132(map132.get("SCS").toString());
					td.setLOAD_132(map132.get("LOAD").toString());
					td.setPEAK_132(map132.get("PEAK").toString());
					td.setOFFPEAK_132(map132.get("OFFPEAK").toString());
					td.setNORMAL_132(map132.get("NORMAL").toString());
					td.setCOLONY_132(map132.get("COLONY").toString());
				}
			}
		}

		System.out.println(todMonthDetails);
		if (todMonthDetails.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("todDetails", todMonthDetails);
			mav.addObject("title", "Voltage Wise,Category Wise,TOD Consumption Month Report - " + year);
			mav.addObject("list", list);

		}

		return mav;

	}

	// 129
	@GetMapping("/fySalesReport")
	public String getFySalesReport() {
		return "fySalesReport";
	}

	@PostMapping("/fySalesReport")
	public ModelAndView getFySalesReport(HttpServletRequest request) throws ParseException {
		ModelAndView mav = new ModelAndView("fySalesReport");
		String year = request.getParameter("year");
		List<Map<String, Object>> salesDetails = newReportDao.getFySalesReport(request);

		if (salesDetails.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("salesDetails", salesDetails);
			mav.addObject("CIRCLE", request.getParameter("circle"));
			mav.addObject("fy_year", year);
			mav.addObject("CIRCOUNT", countFrequencies(salesDetails));
			mav.addObject("title", "Financial Year Report From - " + year);

		}

		return mav;

	}

	@GetMapping("/fySalesReportForDivision")
	public ModelAndView getFySalesReportForDivision(@RequestParam(name = "cir") String circle,
			@RequestParam(name = "fy_year") String year) throws ParseException {
		ModelAndView mav = new ModelAndView("fySalesReportForDivision");
		List<Map<String, Object>> divisionSalesDetails = newReportDao.getFySalesReportForDivision(circle, year);

		if (divisionSalesDetails.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("divisionSalesDetails", divisionSalesDetails);
			mav.addObject("CIRCLE", circle);
			mav.addObject("fy_year", year);
			mav.addObject("CIRCOUNT", countFrequencies(divisionSalesDetails));
			mav.addObject("title", "Financial Year Report From - " + year);

		}

		return mav;

	}

	@GetMapping("/fySalesReportForSubDivision")
	public ModelAndView getFySalesReportForSubDivision(@RequestParam(name = "division") String division,
			@RequestParam(name = "fy_year") String year) throws ParseException {
		ModelAndView mav = new ModelAndView("fySalesReportForSubDivision");
		List<Map<String, Object>> subDivisionSalesDetails = newReportDao.getFySalesReportForSubDivision(division, year);

		if (subDivisionSalesDetails.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("subDivisionSalesDetails", subDivisionSalesDetails);
			mav.addObject("fy_year", year);
			mav.addObject("CIRCOUNT", countFrequencies(subDivisionSalesDetails));
			mav.addObject("title", "Financial Year Report From - " + year);

		}

		return mav;

	}

	@GetMapping("/fySalesReportForSection")
	public ModelAndView getFySalesReportForSection(@RequestParam(name = "subDivision") String subDivision,
			@RequestParam(name = "fy_year") String year) throws ParseException {
		ModelAndView mav = new ModelAndView("fySalesReportForSection");
		List<Map<String, Object>> sectionSalesDetails = newReportDao.getFySalesReportForSection(subDivision, year);

		if (sectionSalesDetails.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("sectionSalesDetails", sectionSalesDetails);
			mav.addObject("CIRCOUNT", countFrequencies(sectionSalesDetails));
			mav.addObject("title", "Financial Year Report From - " + year);

		}

		return mav;

	}

	// 130
	@GetMapping("/monthSalesReport")
	public String getMonthSalesReport() {
		return "monthSalesReport";
	}

	@PostMapping("/monthSalesReport")
	public ModelAndView getMonthSalesReport(HttpServletRequest request) throws ParseException {
		ModelAndView mav = new ModelAndView("monthSalesReport");
		String year = request.getParameter("year");
		List<Map<String, Object>> monthSalesDetails = newReportDao.getMonthSalesReport(request);
		System.out.println(monthSalesDetails);

		if (monthSalesDetails.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("monthSalesDetails", monthSalesDetails);
			mav.addObject("CIRCOUNT", countFrequencies(monthSalesDetails));
			mav.addObject("MONTHCOUNT", countFrequencies(monthSalesDetails));
			mav.addObject("title", "Financial Year Report From - " + year);

		}

		return mav;

	}

	// 102A
	@GetMapping("/voltagewiseFinancialYearAbstract")
	public String getVoltagewiseFinancialYearAbstract() {
		return "voltagewiseFinancialYearAbstract";
	}

	@PostMapping("/voltagewiseFinancialYearAbstract")
	public ModelAndView getVoltagewiseFinancialYearAbstract(HttpServletRequest request) throws ParseException {
		String year = request.getParameter("year");
		ModelAndView mav = new ModelAndView("voltagewiseFinancialYearAbstract");
		List<Map<String, Object>> voltageDetails = newReportDao.getVoltagewiseFinancialYearAbstract(request);
		System.out.println(voltageDetails);

		if (voltageDetails.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("voltageDetails", voltageDetails);
			mav.addObject("title", "Financial Year Report For - " + request.getParameter("circle") + " - " + year);
			mav.addObject("CIRCOUNT", countFrequencies(voltageDetails));

		}

		return mav;

	}

	// 103B
	@GetMapping("/feederwiseFYConsumption")
	public String getFeederwiseFYConsumption() {
		return "feederwiseFYConsumption";
	}

	@PostMapping("/feederwiseFYConsumption")
	public ModelAndView getFeederwiseFYConsumption(HttpServletRequest request) throws ParseException {
		String year = request.getParameter("year");
		ModelAndView mav = new ModelAndView("feederwiseFYConsumption");
		List<Map<String, Object>> feederDetails = newReportDao.getFeederwiseFYConsumption(request);
		System.out.println(feederDetails);
		if (feederDetails.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("feederDetails", feederDetails);
			mav.addObject("title", "Financial Year Report For - " + request.getParameter("circle") + " - " + year);
			mav.addObject("CIRCOUNT", countFrequencies(feederDetails));
			mav.addObject("year", year);
			mav.addObject("circle", request.getParameter("circle"));
			mav.addObject("division", request.getParameter("division"));
			mav.addObject("subdivision", request.getParameter("subdivision"));
			mav.addObject("feeder", request.getParameter("feeder"));

		}

		return mav;

	}

	@GetMapping("/feederWiseConsumptionForNOS")
	public ModelAndView getFeederWiseConsumptionForNOS(@RequestParam(name = "cir") String circle,
			@RequestParam(name = "divsion") String division, @RequestParam(name = "subdiv") String subdiv,
			@RequestParam(name = "fyear") String year, @RequestParam(name = "feeder") String feedercode,
			@RequestParam(name = "fcir") String fcircle, @RequestParam(name = "fdivision") String fdivision,
			@RequestParam(name = "fsubdiv") String fsubdiv, @RequestParam(name = "year") String fyear,
			@RequestParam(name = "ffeeder") String ffeeder) throws ParseException {
		ModelAndView mav = new ModelAndView("feederWiseConsumptionForNOS");
		List<Map<String, Object>> NOSfeederDetails = newReportDao.getFeederWiseConsumptionForNOS(circle, division,
				subdiv, year, feedercode);
		System.out.println(NOSfeederDetails);
		if (NOSfeederDetails.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("NOSfeederDetails", NOSfeederDetails);
			mav.addObject("title", "Financial Year Report For - " + circle + " - " + year);
			mav.addObject("CIRCOUNT", countFrequencies(NOSfeederDetails));
			mav.addObject("year", fyear);
			mav.addObject("circle", fcircle);
			mav.addObject("division", fdivision);
			mav.addObject("subdivision", fsubdiv);
			mav.addObject("feeder", ffeeder);

		}

		return mav;

	}

	// 97C1
	@GetMapping("/HtDCBCollectionSplitFYWise")
	public String gethtDCBCollectionSplitFYWise() {
		return "HtDCBCollectionSplitFYWise";
	}

	@PostMapping("/HtDCBCollectionSplitFYWise")
	public ModelAndView gethtDCBCollectionSplitFYWise(HttpServletRequest request) throws ParseException {
		String year = request.getParameter("year");
		String circle = request.getParameter("circle");
		ModelAndView mav = new ModelAndView("HtDCBCollectionSplitFYWise");
		List<Map<String, Object>> collectionDetails = newReportDao.gethtDCBCollectionSplitFYWise(request);
		System.out.println(collectionDetails);

		if (collectionDetails.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("collectionDetails", collectionDetails);
			mav.addObject("CIR", circle);
			if (circle.equals("ALL")) {
				mav.addObject("title", "Financial Year Report For - APCPDCL - " + year);
			} else {
				mav.addObject("title", "Financial Year Report For - " + circle + " - " + year);
			}
			mav.addObject("CIRCOUNT", countFrequencies(collectionDetails));
		}

		return mav;

	}

	// 131
	@GetMapping("/htSolarMonthReport")
	public String getHtSolarMonthReport() {
		return "htSolarMonthReport";
	}

	@PostMapping("/htSolarMonthReport")
	public ModelAndView getHtSolarMonthReport(HttpServletRequest request) throws ParseException {
		String year = request.getParameter("year");
		ModelAndView mav = new ModelAndView("htSolarMonthReport");
		List<Map<String, Object>> solarDetails = newReportDao.getHtSolarMonthReport(request);
		System.out.println(solarDetails);

		if (solarDetails.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("solarDetails", solarDetails);
			mav.addObject("title", "Financial Year Report For - " + request.getParameter("circle") + " - " + year);
			mav.addObject("TYPECOUNT", countFrequencies(solarDetails));
			mav.addObject("CIRCOUNT", countFrequencies(solarDetails));
			mav.addObject("mon", request.getParameter("month") + "-" + request.getParameter("year"));
		}

		return mav;

	}

	@GetMapping("/HtCategoryWiseDivisionWiseSolarReport")
	public ModelAndView getHtCategoryWiseDivisionWiseSolarReport(@RequestParam(name = "cir") String circle,
			@RequestParam(name = "mon_year") String mon_year) {

		ModelAndView mav = new ModelAndView("HtCategoryWiseDivisionWiseSolarReport");
		List<Map<String, Object>> solarDetails = newReportDao.getHtCategoryWiseDivisionWiseSolarReport(circle,
				mon_year);
		System.out.println(solarDetails);

		if (solarDetails.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("solarDetails", solarDetails);
			mav.addObject("CIRCOUNT", countFrequencies(solarDetails));
			mav.addObject("TYPECOUNT", countFrequencies(solarDetails));
			mav.addObject("title", circle + "- Category Wise Division Wise Demand Abstract For " + mon_year);
			mav.addObject("status", "");
			mav.addObject("mon", mon_year);
		}
		return mav;

	}

	@GetMapping("/HtCategoryWiseSubDivisionWiseDemandReport")
	public ModelAndView getHtCategoryWiseSubDivisionWiseDemandReport(@RequestParam(name = "Division") String circle,
			@RequestParam(name = "mon_year") String mon_year) {

		ModelAndView mav = new ModelAndView("HtCategoryWiseSubDivisionWiseDemandReport");
		List<Map<String, Object>> solarDetails = newReportDao.getHtCategoryWiseSubDivisionWiseDemandReport(circle,
				mon_year);
		System.out.println(solarDetails);

		if (solarDetails.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("solarDetails", solarDetails);
			mav.addObject("CIRCOUNT", countFrequencies(solarDetails));
			mav.addObject("TYPECOUNT", countFrequencies(solarDetails));
			mav.addObject("title", circle + "- Category Wise Sub Division Wise Demand Abstract For " + mon_year);
			mav.addObject("status", "");
		}
		return mav;

	}

	// 132
	@GetMapping("/openAccessReport")
	public String getOpenAccessReport() {
		return "openAccessReport";
	}

	@PostMapping("/openAccessReport")
	public ModelAndView getOpenAccessReport(HttpServletRequest request) {
		String fyear[] = request.getParameter("year").split("-");
		ModelAndView mav = new ModelAndView("openAccessReport");
		List<Map<String, Object>> openAccessDetails = newReportDao.getOpenAccessReport(request);
		System.out.println(openAccessDetails);

		if (openAccessDetails.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("openAccessDetails", openAccessDetails);
			mav.addObject("FI", fyear[0]);
			mav.addObject("SI", fyear[1]);
			mav.addObject("year", request.getParameter("year"));
			mav.addObject("title", "APCPDCL - Open Access KVAH_ADJ_ENG Report For -" + request.getParameter("year"));
		}
		return mav;
	}

	@GetMapping("/openAccessReportForDivision")
	public ModelAndView getOpenAccessReportForDivision(@RequestParam(name = "cir") String circle,
			@RequestParam(name = "fyear") String year) {
		String fyear[] = year.split("-");
		ModelAndView mav = new ModelAndView("openAccessReportForDivision");
		List<Map<String, Object>> openAccessDivisionDetails = newReportDao.getOpenAccessReportForDivision(circle, year);
		System.out.println(openAccessDivisionDetails);

		if (openAccessDivisionDetails.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("openAccessDivisionDetails", openAccessDivisionDetails);
			mav.addObject("FI", fyear[0]);
			mav.addObject("SI", fyear[1]);
			mav.addObject("year", year);
			mav.addObject("title", circle + " - Open Access KVAH_ADJ_ENG Report For -" + year);
		}
		return mav;
	}

	@GetMapping("/openAccessReportForSubDivision")
	public ModelAndView getOpenAccessReportForSubDivision(@RequestParam(name = "div") String division,
			@RequestParam(name = "fyear") String year) {
		String fyear[] = year.split("-");
		ModelAndView mav = new ModelAndView("openAccessReportForSubDivision");
		List<Map<String, Object>> openAccessSubDivisionDetails = newReportDao
				.getOpenAccessReportForSubDivision(division, year);
		System.out.println(openAccessSubDivisionDetails);

		if (openAccessSubDivisionDetails.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("openAccessSubDivisionDetails", openAccessSubDivisionDetails);
			mav.addObject("FI", fyear[0]);
			mav.addObject("SI", fyear[1]);
			mav.addObject("year", year);
			mav.addObject("title", division + " - Open Access KVAH_ADJ_ENG Report For -" + year);
		}
		return mav;
	}

	@GetMapping("/openAccessReportForSection")
	public ModelAndView getOpenAccessReportForSection(@RequestParam(name = "subDivision") String subDivision,
			@RequestParam(name = "fyear") String year) {
		String fyear[] = year.split("-");
		ModelAndView mav = new ModelAndView("openAccessReportForSection");
		List<Map<String, Object>> openAccessSectionDetails = newReportDao.getOpenAccessReportForSection(subDivision,
				year);
		System.out.println(openAccessSectionDetails);

		if (openAccessSectionDetails.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("openAccessSectionDetails", openAccessSectionDetails);
			mav.addObject("FI", fyear[0]);
			mav.addObject("SI", fyear[1]);
			mav.addObject("title", subDivision + " - Open Access KVAH_ADJ_ENG Report For -" + year);
		}
		return mav;
	}

	// 133
	@GetMapping("/openAccessCrossSubsidyReport")
	public String openAccessCrossSubsidyReport() {
		return "openAccessCrossSubsidyReport";
	}

	@PostMapping("/openAccessCrossSubsidyReport")
	public ModelAndView getOpenAccessCrossSubsidyReport(HttpServletRequest request) {
		String fyear[] = request.getParameter("year").split("-");
		ModelAndView mav = new ModelAndView("openAccessCrossSubsidyReport");
		List<Map<String, Object>> crossSubsidyDetails = newReportDao.getOpenAccessCrossSubsidyReport(request);
		System.out.println(crossSubsidyDetails);
		if (crossSubsidyDetails.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("crossSubsidyDetails", crossSubsidyDetails);
			mav.addObject("FI", fyear[0]);
			mav.addObject("SI", fyear[1]);
			mav.addObject("year", request.getParameter("year"));
			mav.addObject("title", "APCPDCL - Open Access CrossSubsidy Report For -" + request.getParameter("year"));

		}
		return mav;
	}

	@GetMapping("/openAccessCrossSubsidyReportForDivision")
	public ModelAndView getOpenAccessCrossSubsidyReportForDivision(@RequestParam(name = "cir") String circle,
			@RequestParam(name = "fyear") String year) {
		String fyear[] = year.split("-");
		ModelAndView mav = new ModelAndView("openAccessCrossSubsidyReportForDivision");
		List<Map<String, Object>> crossSubsidyDivisionDetails = newReportDao
				.getOpenAccessCrossSubsidyReportForDivision(circle, year);
		System.out.println(crossSubsidyDivisionDetails);
		if (crossSubsidyDivisionDetails.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("crossSubsidyDivisionDetails", crossSubsidyDivisionDetails);
			mav.addObject("FI", fyear[0]);
			mav.addObject("SI", fyear[1]);
			mav.addObject("year", year);
			mav.addObject("title", circle + " - Open Access CrossSubsidy Report For -" + year);

		}
		return mav;
	}

	@GetMapping("/openAccessCrossSubsidyReportSubDivision")
	public ModelAndView geOpenAccessCrossSubsidyReportSubDivision(@RequestParam(name = "div") String division,
			@RequestParam(name = "fyear") String year) {
		String fyear[] = year.split("-");
		ModelAndView mav = new ModelAndView("openAccessCrossSubsidyReportSubDivision");
		List<Map<String, Object>> crossSubsidySubDivisionDetails = newReportDao
				.getOpenAccessCrossSubsidyReportSubDivision(division, year);
		System.out.println(crossSubsidySubDivisionDetails);
		if (crossSubsidySubDivisionDetails.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("crossSubsidySubDivisionDetails", crossSubsidySubDivisionDetails);
			mav.addObject("FI", fyear[0]);
			mav.addObject("SI", fyear[1]);
			mav.addObject("year", year);
			mav.addObject("title", division + " - Open Access CrossSubsidy Report For -" + year);

		}
		return mav;
	}

	@GetMapping("/openAccessCrossSubsidyReportForSection")
	public ModelAndView getOpenAccessCrossSubsidyReportForSection(
			@RequestParam(name = "subDivision") String subDivision, @RequestParam(name = "fyear") String year) {
		String fyear[] = year.split("-");
		ModelAndView mav = new ModelAndView("openAccessCrossSubsidyReportForSection");
		List<Map<String, Object>> crossSubsidySectionDetails = newReportDao
				.getOpenAccessCrossSubsidyReportForSection(subDivision, year);
		System.out.println(crossSubsidySectionDetails);
		if (crossSubsidySectionDetails.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("crossSubsidySectionDetails", crossSubsidySectionDetails);
			mav.addObject("FI", fyear[0]);
			mav.addObject("SI", fyear[1]);
			mav.addObject("title", subDivision + " - Open Access CrossSubsidy Report For -" + year);

		}
		return mav;
	}

	// 134
	@GetMapping("/openAccessWheelingChargesReport")
	public String openAccessWheelingChargesReport() {
		return "openAccessWheelingChargesReport";
	}

	@PostMapping("/openAccessWheelingChargesReport")
	public ModelAndView getOpenAccessWheelingChargesReport(HttpServletRequest request) {
		String year = request.getParameter("year");
		String fyear[] = year.split("-");
		ModelAndView mav = new ModelAndView("openAccessWheelingChargesReport");
		List<Map<String, Object>> wheelingChargesDetails = newReportDao.getOpenAccessWheelingChargesReport(request);
		System.out.println(wheelingChargesDetails);
		if (wheelingChargesDetails.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("wheelingChargesDetails", wheelingChargesDetails);
			mav.addObject("FI", fyear[0]);
			mav.addObject("SI", fyear[1]);
			mav.addObject("year", year);
			mav.addObject("title", "APCPDCL - Open Access WheelingCharges Report For -" + request.getParameter("year"));

		}
		return mav;
	}

	@GetMapping("/openAccessWheelingChargesReportForDivision")
	public ModelAndView getOpenAccessWheelingChargesReportForDivision(@RequestParam(name = "cir") String circle,
			@RequestParam(name = "fyear") String year) {
		String fyear[] = year.split("-");
		ModelAndView mav = new ModelAndView("openAccessWheelingChargesReportForDivision");
		List<Map<String, Object>> wheelingChargesDivisionDetails = newReportDao
				.getOpenAccessWheelingChargesReportForDivision(circle, year);
		System.out.println(wheelingChargesDivisionDetails);
		if (wheelingChargesDivisionDetails.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("wheelingChargesDivisionDetails", wheelingChargesDivisionDetails);
			mav.addObject("FI", fyear[0]);
			mav.addObject("SI", fyear[1]);
			mav.addObject("year", year);
			mav.addObject("title", circle + " - Open Access WheelingCharges Report For -" + year);

		}
		return mav;
	}

	@GetMapping("/openAccessWheelingChargesReportSubDivision")
	public ModelAndView geOpenAccessWheelingChargesReportSubDivision(@RequestParam(name = "div") String division,
			@RequestParam(name = "fyear") String year) {
		String fyear[] = year.split("-");
		ModelAndView mav = new ModelAndView("openAccessWheelingChargesReportSubDivision");
		List<Map<String, Object>> wheelingChargesSubDivisionDetails = newReportDao
				.getOpenAccessWheelingChargesReportSubDivision(division, year);
		System.out.println(wheelingChargesSubDivisionDetails);
		if (wheelingChargesSubDivisionDetails.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("wheelingChargesSubDivisionDetails", wheelingChargesSubDivisionDetails);
			mav.addObject("FI", fyear[0]);
			mav.addObject("SI", fyear[1]);
			mav.addObject("year", year);
			mav.addObject("title", division + " - Open Access WheelingCharges Report For -" + year);

		}
		return mav;
	}

	@GetMapping("/openAccessWheelingChargesReportForSection")
	public ModelAndView getOpenAccessWheelingChargesReportForSection(
			@RequestParam(name = "subDivision") String subDivision, @RequestParam(name = "fyear") String year) {
		String fyear[] = year.split("-");
		ModelAndView mav = new ModelAndView("openAccessWheelingChargesReportForSection");
		List<Map<String, Object>> wheelingChargesSectionDetails = newReportDao
				.getOpenAccessWheelingChargesReportForSection(subDivision, year);
		System.out.println(wheelingChargesSectionDetails);
		if (wheelingChargesSectionDetails.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("wheelingChargesSectionDetails", wheelingChargesSectionDetails);
			mav.addObject("FI", fyear[0]);
			mav.addObject("SI", fyear[1]);
			mav.addObject("title", subDivision + " - Open Access WheelingCharges Report For -" + year);

		}
		return mav;
	}

	// 135
	@GetMapping("/cumilativeReport")
	public String getCumilativeReport() {
		return "cumilativeReport";
	}

	@PostMapping("/cumilativeReport")
	public ModelAndView getCumilativeReport(HttpServletRequest request) throws ParseException {
		String circle = request.getParameter("circle");
		String fromMonthYear = "01-" + request.getParameter("fmonth") + "-" + request.getParameter("fyear");
		String toMonthYear = "01-" + request.getParameter("tmonth") + "-" + request.getParameter("tyear");
		ModelAndView mav = new ModelAndView("cumilativeReport");
		List<Map<String, Object>> collectionDetails = newReportDao.getCumilativeReport(request);
		System.out.println(collectionDetails);

		if (collectionDetails.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("collectionDetails", collectionDetails);
			mav.addObject("fyear", fromMonthYear);
			mav.addObject("tyear", toMonthYear);
			mav.addObject("CIRCOUNT", countFrequencies(collectionDetails));
			if (circle.equals("ALL")) {
				mav.addObject("title", "Circle/Div/SD/SEC wise CUM Dem Collection Report For - APCPDCL - "
						+ fromMonthYear + " - " + toMonthYear);
			} else {
				mav.addObject("title", "Circle/Div/SD/SEC wise CUM Dem Collection Report For - " + circle + " - "
						+ fromMonthYear + " - " + toMonthYear);
			}
		}

		return mav;

	}

	@GetMapping("/cumilativeReportForServices")
	public ModelAndView getCumilativeReportForServices(@RequestParam(name = "cir") String circle,
			@RequestParam(name = "divsion") String division, @RequestParam(name = "subdiv") String subdiv,
			@RequestParam(name = "section") String section, @RequestParam(name = "fyear") String fyear,
			@RequestParam(name = "tyear") String tyear, @RequestParam(name = "type") String type)
			throws ParseException {
		ModelAndView mav = new ModelAndView("cumilativeReportForServices");
		List<Map<String, Object>> cumilativeServiceDetails = newReportDao.getCumilativeReportForServices(circle,
				division, subdiv, section, fyear, tyear, type);
		System.out.println(cumilativeServiceDetails);
		if (cumilativeServiceDetails.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("cumilativeServiceDetails", cumilativeServiceDetails);
			mav.addObject("title", "Financial Year Report For - " + circle + " - " + fyear + " to " + tyear);
		}

		return mav;

	}

	// 136
	@GetMapping("/arrearsStatusReport")
	public String getArrearsStatusReport() {
		return "arrearsStatusReport";
	}

	@PostMapping("/arrearsStatusReport")
	public ModelAndView gwtArrearsStatusReport(HttpServletRequest request) throws ParseException {
		String circle = request.getParameter("circle");
		String monthYear = request.getParameter("month") + " - " + request.getParameter("year");
		ModelAndView mav = new ModelAndView("arrearsStatusReport");
		List<Map<String, Object>> arrearsDetails = newReportDao.getArrearsStatusReport(request);
		System.out.println(arrearsDetails);

		if (arrearsDetails.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("arrearsDetails", arrearsDetails);
			mav.addObject("monthYear", monthYear);
			mav.addObject("CIRCOUNT", countFrequencies(arrearsDetails));
			if (circle.equals("ALL")) {
				mav.addObject("title", "Break up Arrears Status Wise And Govt/Pvt Report - APCPDCL - " + monthYear);
			} else {
				mav.addObject("title",
						"Break up Arrears Status Wise And Govt/Pvt Report For - " + circle + " - " + monthYear);
			}
		}

		return mav;

	}

	// 137
	@GetMapping("/serviceTypesubdivabstract")
	public String getServiceTypesubdivabstract() {
		return "serviceTypesubdivabstract";
	}

	@PostMapping("/serviceTypesubdivabstract")
	public ModelAndView getServiceTypesubdivabstract(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("serviceTypesubdivabstract");
		List<Map<String, Object>> acdbalacne = newReportDao.getServiceTypesubdivabstract(request);
		System.out.println(acdbalacne);
		String circle = request.getParameter("circle");
		String monthYear = request.getParameter("month") + "-" + request.getParameter("year");
		if (acdbalacne.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("acd", acdbalacne);
			mav.addObject("title", "Service Type,Category Wise DCB Abstract For  "
					+ (circle.equals("ALL") ? "APCPDCL" : circle) + ", " + monthYear);
		}
		return mav;
	}

	// 138
	@GetMapping("/serviceTypeFYabstract")
	public String getServiceTypeFYabstract() {
		return "serviceTypeFYabstract";
	}

	@PostMapping("/serviceTypeFYabstract")
	public ModelAndView getServiceTypeFYabstract(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("serviceTypeFYabstract");
		List<Map<String, Object>> acdbalacne = newReportDao.getServiceTypeFYabstract(request);
		System.out.println(acdbalacne);
		String circle = request.getParameter("circle");
		String monthYear = request.getParameter("year");
		if (acdbalacne.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("acd", acdbalacne);
			mav.addObject("title", "Service Type,Category Wise DCB Abstract For  "
					+ (circle.equals("ALL") ? "APCPDCL" : circle) + ", " + monthYear);
			mav.addObject("CIRCOUNT", countFrequencies(acdbalacne));
		}
		return mav;
	}

	// 103AForNos
	@GetMapping("/feederWiseSubDivAbstarctForNOS")
	public ModelAndView getFeederWiseSubDivAbstarctForNOS(@RequestParam(name = "cir") String circle,
			@RequestParam(name = "divsion") String division, @RequestParam(name = "subdiv") String subdiv,
			@RequestParam(name = "month") String month, @RequestParam(name = "year") String year,
			@RequestParam(name = "feeder") String feedercode, @RequestParam(name = "fcir") String fcircle,
			@RequestParam(name = "fdivision") String fdivision, @RequestParam(name = "fsubdiv") String fsubdiv,
			@RequestParam(name = "fyear") String fyear, @RequestParam(name = "ffeeder") String ffeeder,
			@RequestParam(name = "fmonth") String fmonth) throws ParseException {
		ModelAndView mav = new ModelAndView("feederWiseSubDivAbstarctForNOS");
		List<Map<String, Object>> feederDetailsForNos = newReportDao.getFeederWiseSubDivAbstarctForNOS(circle, division,
				subdiv, month, year, feedercode);
		System.out.println(feederDetailsForNos);
		if (feederDetailsForNos.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("feederDetailsForNos", feederDetailsForNos);
			mav.addObject("title", "Financial Year Report For - " + circle + " - " + year);
			mav.addObject("CIRCOUNT", countFrequencies(feederDetailsForNos));
			mav.addObject("year", year);
			mav.addObject("fyear", fyear);
			mav.addObject("circle", fcircle);
			mav.addObject("division", fdivision);
			mav.addObject("subdivision", fsubdiv);
			mav.addObject("feeder", ffeeder);
			mav.addObject("fmonth", fmonth);

		}

		return mav;

	}

	// 139
	@GetMapping("/edCourtCasesReport")
	public String getEdCourtCasesReport() {
		return "edCourtCasesReport";
	}

	@PostMapping("/edCourtCasesReport")
	public ModelAndView getEdCourtCasesReport(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("edCourtCasesReport");
		List<Map<String, Object>> caseDetails = newReportDao.getEdCourtCasesReport(request);
		System.out.println(caseDetails);
		String circle = request.getParameter("circle");
		if (caseDetails.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("caseDetails", caseDetails);
			mav.addObject("circle", request.getParameter("circle"));
			mav.addObject("title", "ED Court Cases For  " + (circle.equals("ALL") ? "APCPDCL" : circle));
		}
		return mav;
	}

	@GetMapping("/edCourtCasesForHT2MinEDRate")
	public ModelAndView getEdCourtCasesForHT2MinEDRate(@RequestParam(name = "circle") String circle,
			@RequestParam(name = "fcircle") String fcircle) throws ParseException {
		ModelAndView mav = new ModelAndView("edCourtCasesForHT2MinEDRate");
		List<Map<String, Object>> courtDetais = newReportDao.getEdCourtCasesForHT2MinEDRate(circle);
		System.out.println(courtDetais);
		if (courtDetais.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("courtDetais", courtDetais);
			mav.addObject("circle", fcircle);
			mav.addObject("title", "ED Court cases For - " + circle);
		}

		return mav;

	}

	@GetMapping("/edCourtCasesForHT2MaxEDRate")
	public ModelAndView getEdCourtCasesForHT2MaxEDRate(@RequestParam(name = "cir") String circle,
			@RequestParam(name = "fcircle") String fcircle) throws ParseException {
		ModelAndView mav = new ModelAndView("edCourtCasesForHT2MaxEDRate");
		List<Map<String, Object>> courtDetais = newReportDao.getEdCourtCasesForHT2MaxEDRate(circle);
		System.out.println(courtDetais);
		if (courtDetais.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("courtDetais", courtDetais);
			mav.addObject("circle", fcircle);
			mav.addObject("title", "ED Court cases For - " + circle);
		}

		return mav;

	}

	@GetMapping("/edCourtCasesForHT3MinEDRate")
	public ModelAndView getEdCourtCasesForHT3MinEDRate(@RequestParam(name = "cir") String circle,
			@RequestParam(name = "fcircle") String fcircle) throws ParseException {
		ModelAndView mav = new ModelAndView("edCourtCasesForHT3MinEDRate");
		List<Map<String, Object>> courtDetais = newReportDao.getEdCourtCasesForHT3MinEDRate(circle);
		System.out.println(courtDetais);
		if (courtDetais.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("courtDetais", courtDetais);
			mav.addObject("circle", fcircle);
			mav.addObject("title", "ED Court cases For - " + circle);
		}

		return mav;

	}

	@GetMapping("/edCourtCasesForHT3MaxEDRate")
	public ModelAndView getEdCourtCasesForHT3MaxEDRate(@RequestParam(name = "cir") String circle,
			@RequestParam(name = "fcircle") String fcircle) throws ParseException {
		ModelAndView mav = new ModelAndView("edCourtCasesForHT3MaxEDRate");
		List<Map<String, Object>> courtDetais = newReportDao.getEdCourtCasesForHT3MaxEDRate(circle);
		System.out.println(courtDetais);
		if (courtDetais.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("courtDetais", courtDetais);
			mav.addObject("circle", fcircle);
			mav.addObject("title", "ED Court cases For - " + circle);
		}

		return mav;

	}

	// 140
	@GetMapping("/highAndLowGridReport")
	public String getHighAndLowGridReport() {
		return "highAndLowGridReport";
	}

	@PostMapping("/highAndLowGridReport")
	public ModelAndView getHighAndLowGridReport(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("highAndLowGridReport");
		List<Map<String, Object>> serviceDetails = newReportDao.getHighAndLowGridReport(request);
		System.out.println(serviceDetails);
		String circle = request.getParameter("circle");
		if (serviceDetails.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("serviceDetails", serviceDetails);
			mav.addObject("circle", request.getParameter("circle"));
			mav.addObject("year", request.getParameter("year"));
			mav.addObject("service", request.getParameter("servicetype"));
			mav.addObject("title", "Single Service High Grid _Low Grid Month wise consumption Details For "
					+ (circle.equals("ALL") ? "APCPDCL" : circle) + "  " + request.getParameter("year"));
		}
		return mav;
	}

	@GetMapping("/highAndLowGridReportForNOS")
	public ModelAndView getHighAndLowGridReportForNOS(@RequestParam(name = "cir") String circle,
			@RequestParam(name = "fyear") String fyear, @RequestParam(name = "service") String service,
			@RequestParam(name = "fcir") String fcircle, @RequestParam(name = "fservice") String fservice,
			@RequestParam(name = "year") String year) throws ParseException {
		ModelAndView mav = new ModelAndView("highAndLowGridReportForNOS");
		List<Map<String, Object>> courtDetaisForNos = newReportDao.getHighAndLowGridReportForNOS(circle, fyear , service ,year);
		System.out.println(courtDetaisForNos);
		if (courtDetaisForNos.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("courtDetaisForNos", courtDetaisForNos);
			mav.addObject("circle", fcircle);
			mav.addObject("service", fservice);
			mav.addObject("year", year);
			mav.addObject("title", "Single Service High Grid _Low Grid Month wise consumption Details For - " + circle);
		}

		return mav;

	}

	public Map<String, Integer> countFrequencies(List<Map<String, Object>> list) {
		Map<String, Integer> countmap = new HashMap<String, Integer>();
		List<String> templist = new ArrayList<String>();
		Iterator<Map<String, Object>> itr = list.iterator();
		while (itr.hasNext()) {
			Map<String, Object> tm = itr.next();
			for (Map.Entry<String, Object> pair : tm.entrySet()) {
				if (pair.getKey().equals("CIRNAME")) {
					templist.add(pair.getValue().toString());
				}
				if (pair.getKey().equals("CIRCLE")) {
					templist.add(pair.getValue().toString());
				}
				if (pair.getKey().equals("CIR_TYPE")) {
					templist.add(pair.getValue().toString());
				}
				if (pair.getKey().equals("ISCNO")) {
					templist.add(pair.getValue().toString());
				}
				if (pair.getKey().equals("LDT")) {
					templist.add(pair.getValue().toString());
				}
				if (pair.getKey().equals("BDT")) {
					templist.add(pair.getValue().toString());
				}
				if (pair.getKey().equals("TYPE")) {
					templist.add(pair.getValue().toString());
				}
				if (pair.getKey().equals("DIV_TYPE")) {
					templist.add(pair.getValue().toString());
				}
				if (pair.getKey().equals("FINANCIAL_YEAR")) {
					templist.add(pair.getValue().toString());
				}
				if (pair.getKey().equals("CTCAT")) {
					templist.add(pair.getValue().toString());
				}
				if (pair.getKey().equals("MON_YEAR")) {
					templist.add(pair.getValue().toString());
				}
				if (pair.getKey().equals("DIVNAME")) {
					templist.add(pair.getValue().toString());
				}
				if (pair.getKey().equals("SUBNAME")) {
					templist.add(pair.getValue().toString());
				}
				if (pair.getKey().equals("SECNAME")) {
					templist.add(pair.getValue().toString());
				}
				if (pair.getKey().equals("HGD_LGD_NOR")) {
					templist.add(pair.getValue().toString());
				}
				if (pair.getKey().equals("USCNO")) {
					templist.add(pair.getValue().toString());
				}

			}
		}

		Set<String> st = new HashSet<String>(templist);
		for (String s : st) {
			countmap.put(s, Collections.frequency(templist, s));

		}

		return countmap;

	}

}
