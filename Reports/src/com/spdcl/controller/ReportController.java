package com.spdcl.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;


import com.google.gson.Gson;
import com.spdcl.dao.ReportDao;
import com.spdcl.model.ConsumerDetails;
import com.spdcl.util.DisplayQUANT;
import com.spdcl.util.GenerateACD;
import com.spdcl.util.GenerateBIFACTS;
import com.spdcl.util.GenerateBINEWFACTS;
import com.spdcl.util.GenerateCS_PARTNER_V1;
import com.spdcl.util.GenerateCTDMFULLINSTALLATION2;
import com.spdcl.util.GenerateCTMETER_READ;
import com.spdcl.util.GenerateDEVICE;
import com.spdcl.util.GenerateDEVICEGRP;
import com.spdcl.util.GenerateDISCDOC;
import com.spdcl.util.GenerateDISCENTER;
import com.spdcl.util.GenerateDISCORDER;
import com.spdcl.util.GenerateDMDEVICECTPT;
import com.spdcl.util.GenerateDMDEVICECTPT_33_132;
import com.spdcl.util.GenerateDMDEVICEMETER;
import com.spdcl.util.GenerateDMDEVLOC;
import com.spdcl.util.GenerateDMFULLINSTALLATION;
import com.spdcl.util.GenerateINSTALLATION;
import com.spdcl.util.GenerateISU_CS_FICA_ACCOUNT_V1;
import com.spdcl.util.GenerateISU_CS_PART_REL_V1;
import com.spdcl.util.GenerateISU_DM_CONNOBJ_V1;
import com.spdcl.util.GenerateISU_DM_PREMISE_V1;
import com.spdcl.util.GenerateLTDMDEVICECTPT;
import com.spdcl.util.GenerateLTDMDEVICEMETER;
import com.spdcl.util.GenerateLTDMDEVLOC;
import com.spdcl.util.GenerateLTDMFULLINSTALLATION;
import com.spdcl.util.GenerateLTTECHINSTALLATION;
import com.spdcl.util.GenerateMETER_BILL_READ;
import com.spdcl.util.GenerateMETER_READ;
import com.spdcl.util.GenerateMOVE_IN;
import com.spdcl.util.GenerateOPEN_ITEMS;
import com.spdcl.util.GeneratePAYMENT;
import com.spdcl.util.GenerateSECURITY;
import com.spdcl.util.GenerateSMCMTECHINSTALLATION;
import com.spdcl.util.GenerateTECHINSTALLATION;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class ReportController {
	private static Logger log = Logger.getLogger(ReportController.class);
	@Autowired
	ReportDao reportDao;

	@GetMapping(value = { "/login", "/", "/logout" })
	public String showLoginPage() {
		return "home";
	}
	@PostMapping("/login")
	public String loginPage(@RequestParam(value = "error", required = false) String error,
			@RequestParam(value = "logout", required = false) String logout, Model model) {
		String errorMessge = null;
		System.out.println(error);
		if (error != null) {
			errorMessge = "Username or Password is incorrect !!";
			System.out.println(error);
		} else {
			return "login";
		}
		if (logout != null) {
			errorMessge = "You have been successfully logged out !!";
		}
		model.addAttribute("errorMessge", errorMessge);
		return "login";
	}

	@GetMapping(value = { "/hello", "/SingleLineIsdReport" })
	public String sayHello() {
		return "hello";
	}

	@PostMapping("/hello")
	public String getName() {

		String name = reportDao.getCoustomerName();
		System.out.println("service" + name);
		return name;
	}

	@ResponseBody
	@PostMapping("/getCircles")
	public String getCircleCodes() {
		Map<String, Object> map = reportDao.getCircleList();
		Gson gson = new Gson();
		String json = gson.toJson(map);
		return json;
	}
	
	@ResponseBody
	@PostMapping("/getCategory")
	public String getCategory() {
		Map<String, Object> map = reportDao.getCategoryList();
		Gson gson = new Gson();
		String json = gson.toJson(map);
		return json;
	}
	
	
	
	
	
	@ResponseBody
	@PostMapping("/getHODDepts")
	public String getHODDepts() {
		Map<String, Object> map = reportDao.getHODDepts();
		Gson gson = new Gson();
		String json = gson.toJson(map);
		return json;
	}

	@GetMapping("/home")
	public String saySomePage() {
		return "home";
	}

	@PostMapping("/SingleLineIsdReport")
	public ModelAndView getSingleLineIsdReportDetails(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("hello");
		List<Map<String, Object>> singleIsd = reportDao.getSingleLineIsdReportDetails(request);
		if (singleIsd.isEmpty()) {
			mav.addObject("fail", "No Data Found");
		} else {
			mav.addObject("single", singleIsd);
		}
		return mav;
	}
	
	
	@GetMapping("/isdcalculation")
	public String isdcalculationPage() {
		return "isdcalculation";
	}
	
	@PostMapping("/isdcalculation")
	public ModelAndView isdcalculationDetails(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("isdcalculation");
		List<Map<String, Object>> singleIsd = reportDao.getISDCalculation(request);
		if (singleIsd.isEmpty()) {
			mav.addObject("fail", "No Data Found");
		} else {
			mav.addObject("single", singleIsd);
			mav.addObject("CIRCOUNT",countFrequencies(singleIsd));
			mav.addObject("title", "ISD Calculation");
		}
		return mav;
	}

	@GetMapping("/consumptionVariation")
	public String consumptionPage() {
		return "consumptionVariation";
	}

	@PostMapping("/consumptionVariation")
	public ModelAndView getConsumptionDetails(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("consumptionVariation");
		List<Map<String, Object>> consumption = reportDao.getConsumptionDetails(request);
		if (consumption.isEmpty()) {
			mav.addObject("fail", "No Data Found");
		} else {
			mav.addObject("consumption", consumption);
			String monthYear = request.getParameter("month") + "-" + request.getParameter("year");
			mav.addObject("monthYear", monthYear);
			mav.addObject("premonth", reportDao.getPreviousDate(monthYear));
		}
		return mav;
	}
	@GetMapping("/tdsreport")
	public String tdsreportPage() {
		return "tdsreport";
	}
	
	@PostMapping("/tdsreport")
	public ModelAndView gettdsreportReportDetails(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("tdsreport");
		List<Map<String, Object>> singleIsd = reportDao.getTDSReportDetails(request);
		if (singleIsd.isEmpty()) {
			mav.addObject("fail", "No Data Found");
		} else {
			mav.addObject("single", singleIsd);
		}
		return mav;
	}
	
	@GetMapping("/accountCopy")
	public String getAccountCopyPage() {
		return "accountCopy";
	}

	
	
	@PostMapping("/accountCopy")
	public ModelAndView getAccountCopyDetail(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("accountCopy");
		List<Map<String, Object>> account = reportDao.getAccountCopyDetails1(request);
		if (account.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("supply", reportDao.getSupplyDate(request.getParameter("scno")));
			mav.addObject("account", account);
		}
		return mav;

	}
	@GetMapping("/pvtcoll")
	public String getpvtcollPage() {
		return "pvtcoll";
	}

	
	
	@PostMapping("/pvtcoll")
	public ModelAndView getpvtcollDetail(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("pvtcoll");
		List<Map<String, Object>> account = reportDao.getPVTCollection(request);
		if (account.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("account", account);
		}
		return mav;

	}
	
	@GetMapping("/servicewiseledgersplit")
	public String getservicewisedemandsplitPage() {
		return "servicewiseledgersplit";
	}

	
	
	@PostMapping("/servicewiseledgersplit")
	public ModelAndView getservicewisedemandsplitDetail(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("servicewiseledgersplit");
		List<Map<String, Object>> account = reportDao.getServiceWiseCBSplit(request);
		if (account.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("supply", reportDao.getSupplyDate(request.getParameter("scno")));
			mav.addObject("account", account);
		}
		return mav;

	}
	@GetMapping("/servicechangehistory")
	public String getServiceChangeHistoryPage() {
		return "servicechangehistory";
	}
	@PostMapping("/servicechangehistory")
	public ModelAndView getServiceChangeHistory(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("servicechangehistory");
		List<Map<String, Object>> account = reportDao.getServiceChangeHistory(request);
		List<Map<String, Object>> meter = reportDao.getServiceChangeHistoryMF(request);
		if (account.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("account", account);
			mav.addObject("meter", meter);
		}
		return mav;

	}
	
	@GetMapping("/acdnoticemonthly")
	public String acdnoticemonthlyPage() {
		return "acdnoticemonthly";
	}

	
	
	@PostMapping("/acdnoticemonthly")
	public ModelAndView acdnoticemonthlyDetail(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("acdnoticemonthly");
		List<Map<String, Object>> account = reportDao.getACDMonthyNoticeDetails(request);
		if (account.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("account", account);
		}
		return mav;

	}
	
	@GetMapping("/servicehistory")
	public String getservicehistoryPage(HttpServletRequest request) {
		request.getSession().setAttribute("consumerdetails", null);
		request.getSession().setAttribute("account", null);
		return "servicehistory";
	}

	@PostMapping("/servicehistory")
	public ModelAndView getServiceDetail(HttpServletRequest request) throws ParseException {
		ModelAndView mav = new ModelAndView("servicehistory");
		List<Map<String, Object>> account = reportDao.getServiceHistory(request);
	/*	String sd = (String) account.get(0).get("SD");*/
		ConsumerDetails consumerdetails=reportDao.getConsumerDetails(request.getParameter("scno"));
		String sd = reportDao.getCoustomerSD(request.getParameter("scno"));
		String fmonthYear = request.getParameter("fmonth") + "-" + request.getParameter("fyear");
		String tmonthYear = request.getParameter("tmonth") + "-" + request.getParameter("tyear");
		
		SimpleDateFormat sdformat = new SimpleDateFormat("dd-MMM-yyyy");
	      Date d1 = sdformat.parse("01-"+fmonthYear);
	      Date d2 = sdformat.parse("01-"+tmonthYear);
	    //  System.out.println("The date 1 is: " + sdformat.format(d1));
	    //  System.out.println("The date 2 is: " + sdformat.format(d2));
	      if(d1.compareTo(d2) < 0) {
	         /*System.out.println("Date 1 occurs before Date 2");*/
	      
		
		if (Objects.isNull(consumerdetails)) {
			mav.addObject("fail", "NO DATA FOUND");
			request.getSession().setAttribute("consumerdetails", null);
			request.getSession().setAttribute("account", null);
			request.getSession().setAttribute("fmonthYear", null);
			request.getSession().setAttribute("tmonthYear", null);
			request.getSession().setAttribute("sd", null);
		} else {			
			mav.addObject("consumerdetails", consumerdetails);
			mav.addObject("account", account);
			mav.addObject("sd",sd);
			request.getSession().setAttribute("consumerdetails", consumerdetails);
			request.getSession().setAttribute("account", account);
			request.getSession().setAttribute("sd", sd);			
			request.getSession().setAttribute("fmonthYear", fmonthYear);
			request.getSession().setAttribute("tmonthYear", tmonthYear);
		}
	      }else {
	    	  mav.addObject("fail", "INVALID DATE RANGE");
				request.getSession().setAttribute("consumerdetails", null);
				request.getSession().setAttribute("account", null);
				request.getSession().setAttribute("fmonthYear", null);
				request.getSession().setAttribute("tmonthYear", null);
				request.getSession().setAttribute("sd", null);
	      }
		return mav;

	}
	@GetMapping("/paymenthistory")
	public String getPaymentHistoryPage(HttpServletRequest request) {
		request.getSession().setAttribute("consumerdetails", null);
		request.getSession().setAttribute("account", null);
		return "paymenthistory";
	}
	@PostMapping("/paymenthistory")
	public ModelAndView getPaymentHistoryDetail(HttpServletRequest request) throws ParseException {

		 ModelAndView mav = new ModelAndView("paymenthistory");
		String fmonthYear = request.getParameter("fmonth") + "-" + request.getParameter("fyear");
		String tmonthYear = request.getParameter("tmonth") + "-" + request.getParameter("tyear");
		
		SimpleDateFormat sdformat = new SimpleDateFormat("dd-MMM-yyyy");
	      Date d1 = sdformat.parse("01-"+fmonthYear);
	      Date d2 = sdformat.parse("01-"+tmonthYear);
	    //  System.out.println("The date 1 is: " + sdformat.format(d1));
	    //  System.out.println("The date 2 is: " + sdformat.format(d2));
	      if(d1.compareTo(d2) < 0) {
	         /*System.out.println("Date 1 occurs before Date 2");*/
	      
	    	 
	  		List<Map<String, Object>> account = reportDao.getPayments(request);
	  		if (account.isEmpty()) {
	  			mav.addObject("fail", "NO DATA FOUND");
	  		} else {
	  			mav.addObject("account", account);
	  		}

		
		
	      }else {
	    	  mav.addObject("fail", "INVALID DATE RANGE");
				
	      }
		return mav;

	}
	
	
	@GetMapping("/journalhistory")
	public String getjournalhistoryPage(HttpServletRequest request) {
		request.getSession().setAttribute("consumerdetails", null);
		request.getSession().setAttribute("account", null);
		return "journalhistory";
	}
	@PostMapping("/journalhistory")
	public ModelAndView getjournalhistoryDetail(HttpServletRequest request) throws ParseException {

		 ModelAndView mav = new ModelAndView("journalhistory");
		String fmonthYear = request.getParameter("fmonth") + "-" + request.getParameter("fyear");
		String tmonthYear = request.getParameter("tmonth") + "-" + request.getParameter("tyear");
		
		SimpleDateFormat sdformat = new SimpleDateFormat("dd-MMM-yyyy");
	      Date d1 = sdformat.parse("01-"+fmonthYear);
	      Date d2 = sdformat.parse("01-"+tmonthYear);
	    //  System.out.println("The date 1 is: " + sdformat.format(d1));
	    //  System.out.println("The date 2 is: " + sdformat.format(d2));
	      if(d1.compareTo(d2) < 0) {
	         /*System.out.println("Date 1 occurs before Date 2");*/
	      
	    	 
	  		List<Map<String, Object>> account = reportDao.getJournalHist(request);
	  		if (account.isEmpty()) {
	  			mav.addObject("fail", "NO DATA FOUND");
	  		} else {
	  			mav.addObject("account", account);
	  		}

		
		
	      }else {
	    	  mav.addObject("fail", "INVALID DATE RANGE");
				
	      }
		return mav;

	}
	
	@GetMapping("/oahistory")
	public String getoahistoryPage(HttpServletRequest request) {
		request.getSession().setAttribute("consumerdetails", null);
		request.getSession().setAttribute("account", null);
		return "oahistory";
	}
	@PostMapping("/oahistory")
	public ModelAndView getoahistoryDetail(HttpServletRequest request) throws ParseException {

		 ModelAndView mav = new ModelAndView("oahistory");
	  		List<Map<String, Object>> account = reportDao.getJournalHist(request);
	  		if (account.isEmpty()) {
	  			mav.addObject("fail", "NO DATA FOUND");
	  		} else {
	  			mav.addObject("account", account);
	  		}

		return mav;

	}
	@GetMapping("/LedgerAccountCopy")
	public String showLedgerAccountCopy() {
		return "LedgerAccountCopy";
	}

	@PostMapping("/accountCopyledger")
	public ModelAndView getAccountCopyDetail1(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("LedgerAccountCopy");
		List<Map<String, Object>> account = reportDao.getAccountCopyDetails(request);
		if (account.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("supply", reportDao.getSupplyDate(request.getParameter("scno")));
			mav.addObject("account", account);
		}
		return mav;

	}

	@GetMapping("/Tp_Sales")
	public String getTp_SalesPage() {
		return "Tp_Sales";
	}

	@GetMapping("/Tp_Sales2")
	public String getTp_SalesPageTwo() {
		return "Tp_Sales2";
	}

	@PostMapping("/Tp_Sales")
	public ModelAndView getThirdPartySalesDetails(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("Tp_Sales");
		List<Map<String, Object>> tp_sales = reportDao.getThirdPartySalesDetails(request);
		System.out.println(tp_sales.isEmpty());
		if (tp_sales.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("tp", tp_sales);
		}
		return mav;
	}

	@PostMapping("/Tp_Sales2")
	public ModelAndView getThirdPartySalesDetailsTwo(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("Tp_Sales2");
		List<Map<String, Object>> tp_sales = reportDao.getThirdPartySalesDetailsTwo(request);
		//System.out.println(tp_sales.isEmpty());
		if (tp_sales.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("tp", tp_sales);
		}
		return mav;
	}
	
	@GetMapping("/openaccesssales")
	public String getopenaccesssales() {
		return "openaccesssales";
	}
	
	@PostMapping("/openaccesssales")
	public ModelAndView getOpenAccessSales(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("openaccesssales");
		List<Map<String, Object>> tp_sales = reportDao.getOAalesDetails(request);
		System.out.println(tp_sales.isEmpty());
		if (tp_sales.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("tp", tp_sales);
		}
		return mav;
	}

	@GetMapping("/CategoryWiseDemandReport")
	public String showCategoryWiseDemandPage() {
		return "CategoryWiseDemandReport";
	}
	
	@PostMapping("/CategoryWiseDemandReport")
	public ModelAndView getCategoryWiseDemandReport(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		ModelAndView mav = new ModelAndView("CategoryWiseDemandReport");
		List<Map<String, Object>> tp_sales = reportDao.getCategoryWiseDemandReport(request);
		
		if (tp_sales.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("tp", tp_sales);
			mav.addObject("CIRCOUNT",countFrequencies(tp_sales));
			mav.addObject("TYPECOUNT",countFrequencies(tp_sales));
			mav.addObject("title","Category Wise Demand Abstract For "+request.getParameter("month")+"-"+request.getParameter("year"));
			mav.addObject("status", request.getParameter("status"));
			mav.addObject("mon", request.getParameter("month")+"-"+request.getParameter("year"));
		}
		return mav;
	}
	
	/*@PostMapping("/CategoryWiseDemandReport")
	public ModelAndView getCategoryWiseDemandReport(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("CategoryWiseDemandReport");
		List<Map<String, Object>> cwdr = reportDao.getCategoryWiseDemandReport(request);
		if (cwdr.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			if (request.getParameter("circle").equalsIgnoreCase("APCPDCL")) {
				mav.addObject("consolidate", cwdr);
			} else {
				mav.addObject("cwdr", cwdr);
			}

		}
		return mav;
	}*/

	@GetMapping("/acdReport")
	public String getAcdReportPage() {
		return "acdReport";

	}

	@PostMapping("/acdReport")
	public ModelAndView getAcdReportDetails(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("acdReport");
		String year = request.getParameter("year");
		String monthyear = request.getParameter("monthyear") + "-" + request.getParameter("yearonly");
		String[] yearsplit = year.split("-");
		int finalyear = Integer.parseInt(yearsplit[1]);
		int testyear = 2019;
		System.out.println(finalyear);
		if (finalyear > testyear) {
			List<Map<String, Object>> basicdetails = reportDao.getAcdReportDetailsCurrent(request);
			if (basicdetails.isEmpty()) {
				mav.addObject("fail", "NO DATA FOUND");
			} else {
				mav.addObject("basic", basicdetails);
				mav.addObject("monthyear", monthyear);
			}
			return mav;
		} else {

			List<Map<String, Object>> basicdetails = reportDao.getAcdReportDetails(request);
			if (basicdetails.isEmpty()) {
				mav.addObject("fail", "NO DATA FOUND");
			} else {
				mav.addObject("basic", basicdetails);
				mav.addObject("monthyear", monthyear);
			}
			return mav;

		}
	}

	
	/*@GetMapping("/acdReport2022")
	public String getAcdReportPage2022() {
		return "acdReport2022";

	}

	@PostMapping("/acdReport2022")
	public ModelAndView getAcdReportDetails2022(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("acdReport2022");
		String year = request.getParameter("year");
		String monthyear = request.getParameter("monthyear") + "-" + request.getParameter("yearonly");
		String[] yearsplit = year.split("-");
		int finalyear = Integer.parseInt(yearsplit[1]);
		int testyear = 2019;
		System.out.println(finalyear);
		if (finalyear > testyear) {
			List<Map<String, Object>> basicdetails = reportDao.getAcdReportDetailsCurrent2022(request);
			if (basicdetails.isEmpty()) {
				mav.addObject("fail", "NO DATA FOUND");
			} else {
				mav.addObject("basic", basicdetails);
				mav.addObject("monthyear", monthyear);
			}
			return mav;
		} else {

			List<Map<String, Object>> basicdetails = reportDao.getAcdReportDetails2022(request);
			if (basicdetails.isEmpty()) {
				mav.addObject("fail", "NO DATA FOUND");
			} else {
				mav.addObject("basic", basicdetails);
				mav.addObject("monthyear", monthyear);
			}
			return mav;

		}
	}*/

	
	@GetMapping("/masterchangereport")
	public String changeHistoryPageShow() {
		return "masterchangereport";
	}

	@PostMapping("/masterchangereport")
	public ModelAndView getMasterChangeReport(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("masterchangereport");
		List<Map<String, Object>> list = reportDao.getMasterReport(request);
		if (list.isEmpty()) {
			mav.addObject("error", "NO DATA FOUND");
		} else {
			mav.addObject("list", list);
		}
		return mav;
	}
	@GetMapping("/SolarExport")
	public String SolarExport() {
		return "SolarExport";
	}

	@PostMapping("/SolarExport")
	public ModelAndView SolarExport(HttpServletRequest request) {
		String fyear[] = request.getParameter("year").split("-");
		ModelAndView mav = new ModelAndView("SolarExport");
		List<Map<String, Object>> list = reportDao.getSolarExport(request);
		if (list.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("basic", list);
			mav.addObject("FI", fyear[0]);
			mav.addObject("SI", fyear[1]);
			
		}
		return mav;
	}
	

	@GetMapping("/DemandReport")
	public String showDemandPage() {
		return "DemandReport";
	}
	
	@PostMapping("/DemandReport")
	public ModelAndView getCircleWiseDemandReport(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("DemandReport");
		List<Map<String, Object>> circleDemand = reportDao.getCircleWiseDemandReport(request);
		if (circleDemand.isEmpty()) {
			mav.addObject("error", "NO DATA FOUND");
		} else {
			mav.addObject("cdmd", circleDemand);
		}
		return mav;
	}
	
	
	@GetMapping("/ServTypeCollection")
	public String ServTypeCollection() {
		return "ServTypeCollection";
	}
	
	@PostMapping("/ServTypeCollection")
	public ModelAndView getServTypeCollection(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		String monthYear = request.getParameter("month") + "-" + request.getParameter("year");
		ModelAndView mav = new ModelAndView("ServTypeCollection");
		List<Map<String, Object>> circleDemand = reportDao.getServeTypeCollection(request);
		if (circleDemand.isEmpty()) {
			mav.addObject("error", "NO DATA FOUND");
		} else {
			mav.addObject("cdmd", circleDemand);
			mav.addObject("monyear", monthYear);
			mav.addObject("circle", circle);
			mav.addObject("title","Service Type Collection  Abstract For "+(circle.equals("ALL")?"APCPDCL":circle)+", "+monthYear);
		}
		return mav;
	}
	
	
	@GetMapping("/HODTypeCollection")
	public String HODTypeCollection() {
		return "HODTypeCollection";
	}
	
	@PostMapping("/HODTypeCollection")
	public ModelAndView gerHODTypeCollection(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		String monthYear = request.getParameter("month") + "-" + request.getParameter("year");
		ModelAndView mav = new ModelAndView("HODTypeCollection");
		List<Map<String, Object>> circleDemand = reportDao.getHODTypeCollection(request);
		if (circleDemand.isEmpty()) {
			mav.addObject("error", "NO DATA FOUND");
		} else {
			mav.addObject("cdmd", circleDemand);
			mav.addObject("monyear", monthYear);
			mav.addObject("circle", circle);
			mav.addObject("title","HOD Type Collection  Abstract For "+(circle.equals("ALL")?"APCPDCL":circle)+", "+monthYear);
		}
		return mav;
	}
	
	
	@GetMapping("/RJServiceWiseReport")
	public String RJServiceWiseReport() {
		return "RJServiceWiseReport";
	}
	
	@PostMapping("/RJServiceWiseReport")
	public ModelAndView getRJServiceWiseReport(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("RJServiceWiseReport");
		List<Map<String, Object>> circleDemand = reportDao.getRJServiceWiseReport(request);
		if (circleDemand.isEmpty()) {
			mav.addObject("error", "NO DATA FOUND");
		} else {
			mav.addObject("cdmd", circleDemand);
		}
		return mav;
	}
	
	@GetMapping("/RJTypeWiseReport")
	public String RJTypeWiseReport() {
		return "RJTypeWiseReport";
	}
	
	@PostMapping("/RJTypeWiseReport")
	public ModelAndView getRJTypeWiseReport(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("RJTypeWiseReport");
		List<Map<String, Object>> circleDemand = reportDao.getRJTypeWiseReport(request);
		if (circleDemand.isEmpty()) {
			mav.addObject("error", "NO DATA FOUND");
		} else {
			mav.addObject("cdmd", circleDemand);
			mav.addObject("circle", request.getParameter("circle"));
		}
		return mav;
	}
	@GetMapping("/RJHODWiseReport")
	public String RJHODWiseReport() {
		return "RJHODWiseReport";
	}
	
	@PostMapping("/RJHODWiseReport")
	public ModelAndView getRJHODWiseReport(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("RJHODWiseReport");
		List<Map<String, Object>> circleDemand = reportDao.getRJHODWiseReport(request);
		if (circleDemand.isEmpty()) {
			mav.addObject("error", "NO DATA FOUND");
		} else {
			mav.addObject("cdmd", circleDemand);
			mav.addObject("circle", request.getParameter("circle"));
		}
		return mav;
	}
	
	@GetMapping("/RJNoWiseReport")
	public String RJNoWiseReport() {
		return "RJNoWiseReport";
	}
	
	@PostMapping("/RJNoWiseReport")
	public ModelAndView getRJNoWiseReport(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("RJNoWiseReport");
		List<Map<String, Object>> circleDemand = reportDao.getRJNoWiseReport(request);
		if (circleDemand.isEmpty()) {
			mav.addObject("error", "NO DATA FOUND");
		} else {
			mav.addObject("cdmd", circleDemand);
		}
		return mav;
	}
	
	
	@GetMapping("/HtSales")
	public String HtSalesPage() {
		return "HtSales";
	}
	
	@PostMapping("/HtSales")
	public ModelAndView getHtSalesReport(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("HtSales");
		List<Map<String, Object>> circleDemand = reportDao.getHTSalesReport(request);
		if (circleDemand.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("cdmd", circleDemand);
		}
		return mav;
	}
	
	@GetMapping("/FeederData")
	public String FeederData() {
		return "FeederData";
	}
	
	@PostMapping("/FeederData")
	public ModelAndView getFeederData(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("FeederData");
		List<Map<String, Object>> circleDemand = reportDao.getFeederData(request);
		if (circleDemand.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("cdmd", circleDemand);
		}
		return mav;
	}
	
	@GetMapping("/ICEData")
	public String ICEData() {
		return "ICEData";
	}
	
	@PostMapping("/ICEData")
	public ModelAndView getICEData(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("ICEData");
		List<Map<String, Object>> circleDemand = reportDao.getICEData(request);
		if (circleDemand.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("cdmd", circleDemand);
		}
		return mav;
	}
	
	@GetMapping("/HtSalesYearly")
	public String HtSalesYearly() {
		return "HtSalesYearly";
	}
	
	@PostMapping("/HtSalesYearly")
	public ModelAndView HtSalesYearly(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("HtSalesYearly");
		List<Map<String, Object>> circleDemand = reportDao.getHTSalesYearlyReport(request);
		if (circleDemand.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("cdmd", circleDemand);
		}
		return mav;
	}
	@GetMapping("/UrbanRuralConsumers")
	public String UrbanRuralConsumers() {
		return "UrbanRuralConsumers";
	}
	
	@PostMapping("/UrbanRuralConsumers")
	public ModelAndView UrbanRuralConsumers(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("UrbanRuralConsumers");
		Object[] obj = reportDao.getUrbanRuralConsumers(request);
		List<Map<String, Object>> circleDemand = (List<Map<String, Object>>) obj[0];
		if (circleDemand.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("cdmd", circleDemand);
			mav.addObject("fyear", request.getParameter("year"));
			mav.addObject("title", "CEA -HT-"+request.getParameter("year")+" FORMAT-15(URBAN-RURAL)");
			mav.addObject("frommonth",(String)obj[1]);
			mav.addObject("tomonth",(String)obj[2]);
		}
		return mav;
	}
	
	@GetMapping("/HtCatStatusWiseServicesWithLoad")
	public String HtCatStatusWiseServicesWithLoadPage() {
		return "HtCatStatusWiseServicesWithLoad";
	}
	@PostMapping("/HtCatStatusWiseServicesWithLoad")
	public ModelAndView HtCatStatusWiseServicesWithLoadPage(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("HtCatStatusWiseServicesWithLoad");
		List<Map<String, Object>> circleDemand = reportDao.getHtCatStatusWiseServicesWithLoadPage(request);
		if (circleDemand.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("cdmd", circleDemand);
			mav.addObject("title","HT CAT Wise STATUS Wise Services With Load");
		}
		return mav;
	}
	
	@GetMapping("/CMDHtCatStatusWiseServicesWithLoad")
	public String CMDHtCatStatusWiseServicesWithLoad() {
		return "CMDHtCatStatusWiseServicesWithLoad";
	}
	@PostMapping("/CMDHtCatStatusWiseServicesWithLoad")
	public ModelAndView CMDHtCatStatusWiseServicesWithLoad(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("CMDHtCatStatusWiseServicesWithLoad");
		List<Map<String, Object>> circleDemand = reportDao.getHtCatStatusWiseServicesWithLoadPage(request);
		if (circleDemand.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("cdmd", circleDemand);
			mav.addObject("title","HT CAT Wise STATUS Wise Services With Load");
		}
		return mav;
	}
	
	
	
	@GetMapping("/BillStopServicesStatus")
	public String BillStopServicesStatus() {
		return "BillStopServicesStatus";
	}
	@PostMapping("/BillStopServicesStatus")
	public ModelAndView BillStopServicesStatus(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("BillStopServicesStatus");
		List<Map<String, Object>> circleDemand = reportDao.getBillStopServicesStatus(request);
		if (circleDemand.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("cdmd", circleDemand);
			mav.addObject("title","Bill Stop Services Status : "+request.getParameter("fmonth") + "-" + request.getParameter("fyear"));
			mav.addObject("month",request.getParameter("fmonth") + "-" + request.getParameter("fyear"));
		}
		return mav;
	}
	
	@GetMapping("/HtSalesDCB")
	public String HtSalesDCBPage() {
		return "htsalesdcb";
	}
	
	@PostMapping("/HtSalesDCB")
	public ModelAndView getHtSalesDCB(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("htsalesdcb");
		List<Map<String, Object>> singleIsd = reportDao.getHTSalesDCBDetails(request);
		if (singleIsd.isEmpty()) {
			mav.addObject("fail", "No Data Found");
		} else {
			mav.addObject("single", singleIsd);
			mav.addObject("CAT", request.getParameter("cat"));
			mav.addObject("title",request.getParameter("cat").equals("CAT")?"Ht Year Wise Category Wise Sales Dcb Abstract For "+request.getParameter("year"):"Ht Year Wise Category/Sub Category  Wise Sales Dcb Abstract For "+request.getParameter("year"));
		}
		return mav;
	}
	
	
	@GetMapping("/CBAndDemand")
	public String showCBAndDemandPage() {
		return "CBAndDemand";
	}
	
	
	@PostMapping("/CBAndDemand")
	public ModelAndView getCircleWiseCBAndDemand(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("CBAndDemand");
		List<Map<String, Object>> circleDemand = reportDao.getCircleWiseCBAndDemand(request);
		if (circleDemand.isEmpty()) {
			mav.addObject("error", "NO DATA FOUND");
		} else {
			mav.addObject("cdmd", circleDemand);
			mav.addObject("month", request.getParameter("month") + "-" + request.getParameter("year"));
		}
		return mav;
	}
	@GetMapping("/CBAndDemandAbstract")
	public String getCBAndDemandAbstractPage() {
		return "CBAndDemandAbstract";
	}
	@PostMapping("/CBAndDemandAbstract")
	public ModelAndView getCBAndDemandAbstractDetails(HttpServletRequest request) {
		String monthYear = request.getParameter("month") + "-" + request.getParameter("year");
		String circle = request.getParameter("circle");
		ModelAndView mav = new ModelAndView("CBAndDemandAbstract");
		List<Map<String, Object>> tp_sales = reportDao.getCBAndDemandAbstractDetails(request);
		
		if (tp_sales.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("tp", tp_sales);
			mav.addObject("CIRCOUNT",countFrequencies(tp_sales));
			mav.addObject("title","CB Split (Arrear, Demand Part)  Abstract For "+(circle.equals("ALL")?"APCPDCL":circle)+", "+monthYear);
			mav.addObject("circle", circle);
			mav.addObject("mon", request.getParameter("month") + "-" + request.getParameter("year"));
			
			
		}
		return mav;
	}
	@GetMapping("/DivisionWiseCBAndDemandAbstract")
	public ModelAndView getLedgerCatAbstract(@RequestParam(name = "cir") String circle,
			@RequestParam(name = "mon_year") String monthYear) {
		ModelAndView mav = new ModelAndView("DivisionWiseCBAndDemandAbstract");
		List<Map<String, Object>> tp_sales = reportDao.getDivisonWiseCBAndDemandAbstractDetails(circle,monthYear);
		
		if (tp_sales.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("tp", tp_sales);
			mav.addObject("CIRCOUNT",countFrequencies(tp_sales));
			mav.addObject("title",circle+" Division Wise CB Split (Arrear, Demand Part)  Abstract For "+(circle.equals("ALL")?"APCPDCL":circle)+", "+monthYear);
			mav.addObject("circle", circle);
			mav.addObject("mon", monthYear);
		}
		return mav;
	}
	@GetMapping("/SubDivisionWiseCBAndDemandAbstract")
	public ModelAndView getSubDivisionLedgerCatAbstract(@RequestParam(name = "cir") String circle,
			@RequestParam(name = "mon_year") String monthYear) {
		ModelAndView mav = new ModelAndView("SubDivisionWiseCBAndDemandAbstract");
		List<Map<String, Object>> tp_sales = reportDao.getSubDivisonWiseCBAndDemandAbstractDetails(circle,monthYear);
		
		if (tp_sales.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("tp", tp_sales);
			mav.addObject("CIRCOUNT",countFrequencies(tp_sales));
			mav.addObject("title",circle+" Sub Division Wise CB Split (Arrear, Demand Part)  Abstract For "+(circle.equals("ALL")?"APCPDCL":circle)+", "+monthYear);
			mav.addObject("circle", circle);
			mav.addObject("mon", monthYear);
		}
		return mav;
	}
	@GetMapping("/AgeWiseConsumerLedgerAbstract")
	public String getAgeWiseConsumerLedgerAbstractPage() {
		return "AgeWiseConsumerLedgerAbstract";
	}
	
	@PostMapping("/AgeWiseConsumerLedgerAbstract")
	public ModelAndView getAgeWiseConsumerLedgerAbstractDetails(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		ModelAndView mav = new ModelAndView("AgeWiseConsumerLedgerAbstract");
		List<Map<String, Object>> tp_sales = reportDao.getAgeWiseConsumerLedgerAbstractDetails(request);
		String fmonthYear = request.getParameter("fmonth") + "-" + request.getParameter("fyear");
		if (tp_sales.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("tp", tp_sales);
			mav.addObject("CIRCOUNT",countFrequencies(tp_sales));
			mav.addObject("title","Age Wise Arrears Abstract For "+(circle.equals("ALL")?"APCPDCL":circle));
			mav.addObject("status", request.getParameter("status"));
			mav.addObject("fmonthYear", fmonthYear);
		}
		return mav;
	}
	@GetMapping("/AgeWiseGovtPvtStatusLedgerAbstract")
	public String getAgeWiseGovtPvtStatusLedgerAbstract() {
		return "AgeWiseTypeWiseStatusWiseAbstract";
	}
	@PostMapping("/AgeWiseGovtPvtStatusLedgerAbstract")
	public ModelAndView getAgeWiseGovtPvtStatusLedgerAbstract(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		ModelAndView mav = new ModelAndView("AgeWiseTypeWiseStatusWiseAbstract");
		List<Map<String, Object>> tp_sales = reportDao.getAgeWiseGovtPvtStatusLedgerAbstract(request);
		
		if (tp_sales.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("tp", tp_sales);
			mav.addObject("CIRCOUNT",countFrequencies(tp_sales));
			mav.addObject("title","HT-AGE WISE CLOSING BALANCE (G0VT AND PVT)  FOR "+(circle.equals("ALL")?"APCPDCL":circle));
			/*mav.addObject("status", request.getParameter("status"));*/
		}
		return mav;
	}
	@GetMapping("/HtDCBCollectionSplitMonthly")
	public String getHtDCBCollectionSplitMonthlyAbstract() {
		return "HtDCBCollectionSplitMonthly";
	}
	@PostMapping("/HtDCBCollectionSplitMonthly")
	public ModelAndView getHtDCBCollectionSplitMonthlyAbstract(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		ModelAndView mav = new ModelAndView("HtDCBCollectionSplitMonthly");
		List<Map<String, Object>> tp_sales = reportDao.getHtDCBCollectionSplitMonthlyAbstract(request);
		
		if (tp_sales.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("tp", tp_sales);
			mav.addObject("CIRCOUNT",countFrequencies(tp_sales));
			mav.addObject("CIR", circle);
			mav.addObject("title","HT DCB COLLECTION SPLIT MONTHLY ABSTRACT  FOR "+(circle.equals("ALL")?"APCPDCL":circle));
			/*mav.addObject("status", request.getParameter("status"));*/
		}
		return mav;
	}
	
	@GetMapping("/HtDCBCollectionMonthly")
	public String getHtDCBCollectionMonthlyAbstract() {
		return "HtDCBCollectionMonthly";
	}
	@PostMapping("/HtDCBCollectionMonthly")
	public ModelAndView getHtDCBCollectionMonthlyAbstract(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		ModelAndView mav = new ModelAndView("HtDCBCollectionMonthly");
		List<Map<String, Object>> tp_sales = reportDao.getHtDCBCollectionMonthlyAbstract(request);
		
		if (tp_sales.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("tp", tp_sales);
			mav.addObject("CIRCOUNT",countFrequencies(tp_sales));
			mav.addObject("CIR", circle);
			mav.addObject("title","HT DCB Collection  Monthly Abstract  For "+(circle.equals("ALL")?"APCPDCL":circle));
			/*mav.addObject("status", request.getParameter("status"));*/
		}
		return mav;
	}
	
	@GetMapping("/ISDAbstract")
	public String ISDAbstract() {
		return "ISDAbstract";
	}
	@PostMapping("/ISDAbstract")
	public ModelAndView getISDAbstractAbstractDetails(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		ModelAndView mav = new ModelAndView("ISDAbstract");
		List<Map<String, Object>> tp_sales = reportDao.getISCAbstract(request);
		
		if (tp_sales.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("tp", tp_sales);			
			mav.addObject("CIRCOUNT",countFrequencies(tp_sales));
			mav.addObject("TYPECOUNT",countFrequencies(tp_sales));
			mav.addObject("title","Circle Wise, Category Wise ISD Abstract For "+request.getParameter("year"));
			mav.addObject("status", request.getParameter("status"));
		}
		return mav;
	}
	
	@GetMapping("/CollectionAbstract")
	public String CollectionAbstract() {
		return "CollectionAbstract";
	}
	@PostMapping("/CollectionAbstract")
	public ModelAndView getCollectionAbstract(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		ModelAndView mav = new ModelAndView("CollectionAbstract");
		List<Map<String, Object>> tp_sales = reportDao.getCollectionAbstract(request);
		
		if (tp_sales.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("tp", tp_sales);			
			mav.addObject("CIRCOUNT",countFrequencies(tp_sales));
			mav.addObject("TYPECOUNT",countFrequencies(tp_sales));
			mav.addObject("title","Collection Abstract For "+(circle.equals("ALL")?"APCPDCL":circle));
			mav.addObject("status", request.getParameter("status"));
		}
		return mav;
	}
	
	
	@GetMapping("/DCDivisionWiseReport")
	public String DCDivisionWiseReport() {
		return "DCDivisionWiseReport";
	}
	@PostMapping("/DCDivisionWiseReport")
	public ModelAndView getDCDivisionWiseReportDetails(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		ModelAndView mav = new ModelAndView("DCDivisionWiseReport");
		List<Map<String, Object>> tp_sales = reportDao.getDCDivisionWiseReport(request);
		
		if (tp_sales.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("tp", tp_sales);
			mav.addObject("TYPE",countFrequencies(tp_sales));
			mav.addObject("CIRCOUNT",countFrequencies(tp_sales));
			mav.addObject("TYPECOUNT",countFrequencies(tp_sales));
			mav.addObject("title","Division Wise Daily Collection Report");
			mav.addObject("status", request.getParameter("status"));
			Calendar cal  = Calendar.getInstance();
		    //subtracting a day
		    cal.add(Calendar.DATE, -1);
			DateFormat df = new SimpleDateFormat("MMMM-yyyy");
			String requiredDate = df.format(new Date(cal.getTimeInMillis())).toString();
			mav.addObject("month_year", requiredDate.split("-")[0]+"("+requiredDate.split("-")[1]+")");
			DateFormat df1 = new SimpleDateFormat("dd-MM-yyyy");
			String requiredDate1 = df1.format(new Date(cal.getTimeInMillis())).toString();
			mav.addObject("date", requiredDate1);
			
		}
		return mav;
	}
	
	@GetMapping("/DCSectionWiseReport")
	public String DCSectionWiseReport() {
		return "DCSectionWiseReport";
	}
	@PostMapping("/DCSectionWiseReport")
	public ModelAndView DCSectionWiseReport(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		ModelAndView mav = new ModelAndView("DCSectionWiseReport");
		List<Map<String, Object>> tp_sales = reportDao.getDCSectionWiseReport(request);
		
		if (tp_sales.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("tp", tp_sales);
			mav.addObject("TYPE",countFrequencies(tp_sales));
			mav.addObject("CIRCOUNT",countFrequencies(tp_sales));
			mav.addObject("TYPECOUNT",countFrequencies(tp_sales));
			mav.addObject("DIVCOUNT",countFrequencies(tp_sales));
			mav.addObject("title","Section Wise Daily Collection Report");
			mav.addObject("status", request.getParameter("status"));
			Calendar cal  = Calendar.getInstance();
		    //subtracting a day
		    cal.add(Calendar.DATE, -1);
			DateFormat df = new SimpleDateFormat("MMMM-yyyy");
			String requiredDate = df.format(new Date(cal.getTimeInMillis())).toString();
			mav.addObject("month_year", requiredDate.split("-")[0]+"("+requiredDate.split("-")[1]+")");
			DateFormat df1 = new SimpleDateFormat("dd-MM-yyyy");
			String requiredDate1 = df1.format(new Date(cal.getTimeInMillis())).toString();
			mav.addObject("date", requiredDate1);
			
		}
		return mav;
	}
	
	
	@GetMapping("/catcbledgerabs")
	public String LedgerCatAbstract() {
		return "LedgerCatAbstract";
	}
	@PostMapping("/catcbledgerabs")
	public ModelAndView getLedgerCatAbstract(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		ModelAndView mav = new ModelAndView("LedgerCatAbstract");
		List<Map<String, Object>> tp_sales = reportDao.getLedgerCatAbstract(request);
		
		if (tp_sales.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("tp", tp_sales);
			mav.addObject("CIRCOUNT",countFrequencies(tp_sales));
			mav.addObject("TYPECOUNT",countFrequencies(tp_sales));
			mav.addObject("title",circle+" Category Wise Ledger Closing Balances Abstract For"+(circle.equals("ALL")?"APCPDCL":circle)+"  the month of "+request.getParameter("fmonth") + "-" + request.getParameter("fyear"));
			mav.addObject("mon", request.getParameter("fmonth") + "-" + request.getParameter("fyear"));
			mav.addObject("status", request.getParameter("status"));
		}
		return mav;
	}
	@GetMapping("/divisionwisecatcbledgerabs")
	public ModelAndView getLedgerCatAbstract(@RequestParam(name = "cir") String circle,
			@RequestParam(name = "mon_year") String monthYear,@RequestParam(name="status") String status) {
		ModelAndView mav = new ModelAndView("DivisionLedgerCatAbstract");
		List<Map<String, Object>> tp_sales = reportDao.getDivLedgerCatAbstract(circle,monthYear,status);
		
		if (tp_sales.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("tp", tp_sales);
			mav.addObject("CIRCOUNT",countFrequencies(tp_sales));
			mav.addObject("TYPECOUNT",countFrequencies(tp_sales));
			mav.addObject("title",circle+" Division Wise Category Wise Ledger Closing Balances Abstract");
			/*mav.addObject("status", request.getParameter("status"));*/
			mav.addObject("mon", monthYear);
			mav.addObject("status", status);
		}
		return mav;
	}
	@GetMapping("/subdivisionwisecatcbledgerabs")
	public ModelAndView getSubDivLedgerCatAbstract(@RequestParam(name = "cir") String circle,
			@RequestParam(name = "mon_year") String monthYear,@RequestParam(name="status") String status) {
		ModelAndView mav = new ModelAndView("SubDivisionLedgerCatAbstract");
		List<Map<String, Object>> tp_sales = reportDao.getSubDivLedgerCatAbstract(circle,monthYear,status);
		
		if (tp_sales.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("tp", tp_sales);
			mav.addObject("CIRCOUNT",countFrequencies(tp_sales));
			mav.addObject("TYPECOUNT",countFrequencies(tp_sales));
			mav.addObject("title",circle+" Sub Division Wise Category Wise Ledger Closing Balances Abstract");
			/*mav.addObject("status", request.getParameter("status"));*/
		}
		return mav;
	}
	
	//DivisionLedgerCatAbstract
	@GetMapping("/ACDCollection")
	public String ACDCollectionAbstract() {
		return "ACDCollection";
	}
	@PostMapping("/ACDCollection")
	public ModelAndView getACDCollectionAbstract(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		ModelAndView mav = new ModelAndView("ACDCollection");
		List<Map<String, Object>> tp_sales = reportDao.getACDCollectionAbstract(request);	
		if (tp_sales.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			List<String> col = getCustomColumns(request.getParameter("year"));
			int i = 0;
			for (String s : col) {
				if (s.contains("MAY") || s.contains("OCT")) {
					i++;
				}
			}
			mav.addObject("tp", tp_sales);
			mav.addObject("CIRCOUNT",countFrequencies(tp_sales));
			mav.addObject("TYPECOUNT",countFrequencies(tp_sales));
			mav.addObject("title","ACD Collection Report "+request.getParameter("year"));
			mav.addObject("CUSTCOL",col);
			mav.addObject("COLSPAN",col.size()+8);
			mav.addObject("RCOL","ACD REVIEWED ("+(i==2?"MAY , OCT - ":"MAY - ")+request.getParameter("year").split("-")[0]+")");
			/*mav.addObject("status", request.getParameter("status"));*/
		}
		return mav;
	}
	
	@GetMapping("/TrueUpCollection")
	public String getTrueUpCollectionPage() {
		return "TrueUpCollection";
	}
	@PostMapping("/TrueUpCollection")
	public ModelAndView getTrueUpCollectionDetails(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		ModelAndView mav = new ModelAndView("TrueUpCollection");
		List<Map<String, Object>> tp_sales = reportDao.getTrueUpAbstract(request);	
		if (tp_sales.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			List<String> col = getTrueCustomColumns(request.getParameter("year"));
			
			mav.addObject("tp", tp_sales);
			mav.addObject("CIRCOUNT",countFrequencies(tp_sales));
			mav.addObject("TYPECOUNT",countFrequencies(tp_sales));
			mav.addObject("title","True-Up Charges DCB For "+request.getParameter("year"));
			mav.addObject("CUSTCOL",col);
			
			mav.addObject("MONTH",getMonthColumns(request.getParameter("year")));
			mav.addObject("COLSPAN",col.size()+8);
			mav.addObject("RCOL","True-Up Charges DCB");
			/*mav.addObject("status", request.getParameter("status"));*/
		}
		return mav;
	}
	
	
	@GetMapping("/ACDAbstract")
	public String ACDAbstract() {
		return "acdreviewabstract";
	}
	@PostMapping("/ACDAbstract")
	public ModelAndView getACDAbstractAbstractDetails(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		ModelAndView mav = new ModelAndView("acdreviewabstract");
		List<Map<String, Object>> tp_sales = reportDao.getACDAbstract(request);
		
		if (tp_sales.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("tp", tp_sales);
			mav.addObject("CIRCOUNT",countFrequencies(tp_sales));
			mav.addObject("TYPECOUNT",countFrequencies(tp_sales));
			mav.addObject("title","Circle Wise, Category Wise ACD Abstract For "+request.getParameter("year"));
			mav.addObject("status", request.getParameter("status"));
		}
		return mav;
	}
	
	/*@GetMapping("/ACDAbstract2022")
	public String ACDAbstract2022() {
		return "acdreviewabstract2022";
	}
	@PostMapping("/ACDAbstract2022")
	public ModelAndView getACDAbstractAbstractDetails2022(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		ModelAndView mav = new ModelAndView("acdreviewabstract2022");
		List<Map<String, Object>> tp_sales = reportDao.getACDAbstract2022(request);
		
		if (tp_sales.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("tp", tp_sales);
			mav.addObject("CIRCOUNT",countFrequencies(tp_sales));
			mav.addObject("TYPECOUNT",countFrequencies(tp_sales));
			mav.addObject("title","Circle Wise, Category Wise ACD Abstract For 2021-2022");
			mav.addObject("status", request.getParameter("status"));
		}
		return mav;
	}*/
	
	
	@GetMapping("/TDSAbstract")
	public String TDSAbstract() {
		return "TDSAbstract";
	}
	@PostMapping("/TDSAbstract")
	public ModelAndView getTDSAbstractDetails(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		ModelAndView mav = new ModelAndView("TDSAbstract");
		List<Map<String, Object>> tp_sales = reportDao.getTDSAbstract(request);
		
		if (tp_sales.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("tp", tp_sales);
			mav.addObject("CIRCOUNT",countFrequencies(tp_sales));
			mav.addObject("TYPECOUNT",countFrequencies(tp_sales));
			mav.addObject("title","Circle Wise, Category Wise TDS Abstract For "+request.getParameter("year"));
			mav.addObject("status", request.getParameter("status"));
		}
		return mav;
	}
	
	@GetMapping("/EDAbstract")
	public String EDAbstract() {
		return "EDAbstract";
	}
	
	
	@PostMapping("/EDAbstract")
	public ModelAndView getEDAbstract(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		ModelAndView mav = new ModelAndView("EDAbstract");
		List<Map<String, Object>> tp_sales = reportDao.getEDAbstract(request);
		try {
		if (tp_sales.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("tp", tp_sales);
			mav.addObject("CIRCOUNT",countFrequencies(tp_sales));
			mav.addObject("CIR", circle);
			mav.addObject("title","HT ED ABSTRACT  FOR "+(circle.equals("ALL")?"APCPDCL":circle));
			/*mav.addObject("status", request.getParameter("status"));*/
		}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return mav;
	}
	
	@GetMapping("/TrueUpAbstract")
	public String TrueUpAbstract() {
		return "TrueUpAbstract";
	}
	
	
	@PostMapping("/TrueUpAbstract")
	public ModelAndView getTrueUpAbstract(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		ModelAndView mav = new ModelAndView("TrueUpAbstract");
		List<Map<String, Object>> tp_sales = reportDao.getCircleCatWiseTrueUpAbstract(request);
		try {
		if (tp_sales.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			
			
			mav.addObject("tp", tp_sales);
			mav.addObject("CIRCOUNT",countFrequencies(tp_sales));
			mav.addObject("TYPECOUNT",countFrequencies(tp_sales));
			mav.addObject("CIR", circle);
			mav.addObject("title","HT TRUE UP ABSTRACT  FOR "+(circle.equals("ALL")?"APCPDCL":circle));
			/*mav.addObject("status", request.getParameter("status"));*/
		}
		}catch(Exception e) {
			e.printStackTrace();
		}
	
	return mav;
}
	
	@GetMapping("/isucufiledownload")
	public String isucufiledownload() {
		return "isucufiledownload";
	}
		
	@RequestMapping("/ISU_CS_PARTNER_V1")
	public void downloadTextFileExample5(HttpServletResponse response) throws IOException {
		ServletOutputStream ouputStream = null;
		response.setContentType(MediaType.TEXT_PLAIN_VALUE);
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + "M4PARH001.txt");	
		
		List<Map<String, Object>>  data = reportDao.getISUTXTData();
		List<Map<String, Object>> masterdata = reportDao.getHTMASTER();
		
		InputStream inputStream = GenerateCS_PARTNER_V1.generateBillTxt(data,reportDao.getIDTypes(),masterdata);
		// Copy file content to response output stream
		 try {
				ouputStream = response.getOutputStream();
				int b;
				while ((b = inputStream.read()) != -1) {
					ouputStream.write(b);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			response.flushBuffer();
			FileCopyUtils.copy(inputStream, response.getOutputStream());
	}
	
	
	@RequestMapping("/M3_ISU_CS_PARTNER_V1")
	public void m3downloadTextFileExample5(HttpServletResponse response) throws IOException {
		ServletOutputStream ouputStream = null;
		response.setContentType(MediaType.TEXT_PLAIN_VALUE);
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + "M8PARH001.txt");	
		
		List<Map<String, Object>>  data = reportDao.m3getISUTXTData();
		List<Map<String, Object>> masterdata = reportDao.getHTMASTER();
		
		InputStream inputStream = GenerateCS_PARTNER_V1.generateBillTxt(data,reportDao.getIDTypes(),masterdata);
		// Copy file content to response output stream
		 try {
				ouputStream = response.getOutputStream();
				int b;
				while ((b = inputStream.read()) != -1) {
					ouputStream.write(b);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			response.flushBuffer();
			FileCopyUtils.copy(inputStream, response.getOutputStream());
	}
	@RequestMapping("/ISU_CS_PART_REL_V1")
	public void ISU_CS_PART_REL_V1DownloadFile(HttpServletResponse response) throws IOException {
		ServletOutputStream ouputStream = null;
		response.setContentType(MediaType.TEXT_PLAIN_VALUE);
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + "ISU_CS_PART_REL_V1.txt");
		
		List<Map<String, Object>>  data = reportDao.getISUTXTData();
		InputStream inputStream = GenerateISU_CS_PART_REL_V1.generateBillTxt(data);
		// Copy file content to response output stream
		 try {
				ouputStream = response.getOutputStream();
				int b;
				while ((b = inputStream.read()) != -1) {
					ouputStream.write(b);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			response.flushBuffer();
			FileCopyUtils.copy(inputStream, response.getOutputStream());
	}
	
	@RequestMapping("/ISU_CS_FICA_ACCOUNT_V1")
	public void getISU_CS_FICA_ACCOUNT_V1(HttpServletResponse response) throws IOException {
		ServletOutputStream ouputStream = null;
		response.setContentType(MediaType.TEXT_PLAIN_VALUE);
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + "M4ACCH001.txt");
		
		List<Map<String, Object>>  data = reportDao.getISU_CS_FICA_ACCOUNT_V1();
		InputStream inputStream = GenerateISU_CS_FICA_ACCOUNT_V1.generateBillTxt(data);
		// Copy file content to response output stream
		 try {
				ouputStream = response.getOutputStream();
				int b;
				while ((b = inputStream.read()) != -1) {
					ouputStream.write(b);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			response.flushBuffer();
			FileCopyUtils.copy(inputStream, response.getOutputStream());
	}
	
	@RequestMapping("/M3_ISU_CS_FICA_ACCOUNT_V1")
	public void getM3_ISU_CS_FICA_ACCOUNT_V1(HttpServletResponse response) throws IOException {
		ServletOutputStream ouputStream = null;
		response.setContentType(MediaType.TEXT_PLAIN_VALUE);
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + "M8ACCH001.txt");
		
		List<Map<String, Object>>  data = reportDao.getM3ISU_CS_FICA_ACCOUNT_V1();
		InputStream inputStream = GenerateISU_CS_FICA_ACCOUNT_V1.generateBillTxt(data);
		// Copy file content to response output stream
		 try {
				ouputStream = response.getOutputStream();
				int b;
				while ((b = inputStream.read()) != -1) {
					ouputStream.write(b);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			response.flushBuffer();
			FileCopyUtils.copy(inputStream, response.getOutputStream());
	}
	
	@RequestMapping("/ISU_DM_CONNOBJ_V1")
	public void getISU_DM_CONNOBJ_V1(HttpServletResponse response) throws IOException {
		ServletOutputStream ouputStream = null;
		response.setContentType(MediaType.TEXT_PLAIN_VALUE);
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + "M4CONH001.txt");
		
		List<Map<String, Object>>  data = reportDao.getISUTXTData();
		List<Map<String, Object>> masterdata = reportDao.getHTMASTER();
		InputStream inputStream = GenerateISU_DM_CONNOBJ_V1.generateBillTxt(data,masterdata);
		// Copy file content to response output stream
		 try {
				ouputStream = response.getOutputStream();
				int b;
				while ((b = inputStream.read()) != -1) {
					ouputStream.write(b);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			response.flushBuffer();
			FileCopyUtils.copy(inputStream, response.getOutputStream());
	}
	
	@RequestMapping("/M3_ISU_DM_CONNOBJ_V1")
	public void getM3_ISU_DM_CONNOBJ_V1(HttpServletResponse response) throws IOException {
		ServletOutputStream ouputStream = null;
		response.setContentType(MediaType.TEXT_PLAIN_VALUE);
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + "M8CONH001.txt");
		
		List<Map<String, Object>>  data = reportDao.m3getCONNOBJECT();
		List<Map<String, Object>> masterdata = reportDao.getHTMASTER();
		InputStream inputStream = GenerateISU_DM_CONNOBJ_V1.generateBillTxt(data,masterdata);
		// Copy file content to response output stream
		 try {
				ouputStream = response.getOutputStream();
				int b;
				while ((b = inputStream.read()) != -1) {
					ouputStream.write(b);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			response.flushBuffer();
			FileCopyUtils.copy(inputStream, response.getOutputStream());
	}
	@RequestMapping("/ISU_DM_PREMISE_V1")
	public void getISU_DM_PREMISE_V1(HttpServletResponse response) throws IOException {
		ServletOutputStream ouputStream = null;
		response.setContentType(MediaType.TEXT_PLAIN_VALUE);
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + "M8PREH001.txt");
		
		List<Map<String, Object>>  data = reportDao.getISUTXTData();
		InputStream inputStream = GenerateISU_DM_PREMISE_V1.generateBillTxt(data);
		// Copy file content to response output stream
		 try {
				ouputStream = response.getOutputStream();
				int b;
				while ((b = inputStream.read()) != -1) {
					ouputStream.write(b);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			response.flushBuffer();
			FileCopyUtils.copy(inputStream, response.getOutputStream());
	}
	
	@RequestMapping("/M3_ISU_DM_PREMISE_V1")
	public void getM3ISU_DM_PREMISE_V1(HttpServletResponse response) throws IOException {
		ServletOutputStream ouputStream = null;
		response.setContentType(MediaType.TEXT_PLAIN_VALUE);
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + "M8PREH001.txt");
		
		List<Map<String, Object>>  data = reportDao.m3getCONNOBJECT();
		InputStream inputStream = GenerateISU_DM_PREMISE_V1.generateBillTxt(data);
		// Copy file content to response output stream
		 try {
				ouputStream = response.getOutputStream();
				int b;
				while ((b = inputStream.read()) != -1) {
					ouputStream.write(b);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			response.flushBuffer();
			FileCopyUtils.copy(inputStream, response.getOutputStream());
	}
	
	@RequestMapping("/INSTALLATION")
	public void getINSTALLATION(HttpServletResponse response) throws IOException {
		ServletOutputStream ouputStream = null;
		response.setContentType(MediaType.TEXT_PLAIN_VALUE);
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + "M4INSH001.txt");
		
		List<Map<String, Object>>  data = reportDao.getISUTXTData();
		InputStream inputStream = GenerateINSTALLATION.generateBillTxt(data);
		// Copy file content to response output stream
		 try {
				ouputStream = response.getOutputStream();
				int b;
				while ((b = inputStream.read()) != -1) {
					ouputStream.write(b);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			response.flushBuffer();
			FileCopyUtils.copy(inputStream, response.getOutputStream());
	}
	
	@RequestMapping("/M3_INSTALLATION")
	public void getM3INSTALLATION(HttpServletResponse response) throws IOException {
		ServletOutputStream ouputStream = null;
		response.setContentType(MediaType.TEXT_PLAIN_VALUE);
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + "M8INSH001.txt");
		
		List<Map<String, Object>>  data = reportDao.m3getCONNOBJECT();
		GenerateINSTALLATION.seasonal = reportDao.getSeasonalDetails();
		
		InputStream inputStream = GenerateINSTALLATION.generateBillTxt(data);
		
		// Copy file content to response output stream
		 try {
				ouputStream = response.getOutputStream();
				int b;
				while ((b = inputStream.read()) != -1) {
					ouputStream.write(b);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			response.flushBuffer();
			FileCopyUtils.copy(inputStream, response.getOutputStream());
	}
	
	@RequestMapping("/DEVICE")
	public void getDEVICE(HttpServletResponse response) throws IOException {
		ServletOutputStream ouputStream = null;
		response.setContentType(MediaType.TEXT_PLAIN_VALUE);
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + "DEVICE.txt");
		
		List<Map<String, Object>>  data = reportDao.getISUTXTData();
		InputStream inputStream = GenerateDEVICE.generateBillTxt(data);
		// Copy file content to response output stream
		 try {
				ouputStream = response.getOutputStream();
				int b;
				while ((b = inputStream.read()) != -1) {
					ouputStream.write(b);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			response.flushBuffer();
			FileCopyUtils.copy(inputStream, response.getOutputStream());
	}
	
	@RequestMapping("/TECHINSTALLATION")
	public void getFULLINSTALLATION(HttpServletResponse response) throws IOException {
		ServletOutputStream ouputStream = null;
		response.setContentType(MediaType.TEXT_PLAIN_VALUE);
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + "TECTINSTALLATION.txt");
		
		List<Map<String, Object>>  data = reportDao.getTECHINSTALLATION_DATA();
		InputStream inputStream = GenerateTECHINSTALLATION.generateBillTxt(data);
		// Copy file content to response output stream
		 try {
				ouputStream = response.getOutputStream();
				int b;
				while ((b = inputStream.read()) != -1) {
					ouputStream.write(b);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			response.flushBuffer();
			FileCopyUtils.copy(inputStream, response.getOutputStream());
	}
	
	
	@RequestMapping("/M3_LT_TECHINSTALLATION")
	public void getM3_LT_TECHINSTALLATION(HttpServletResponse response) throws IOException {
		ServletOutputStream ouputStream = null;
		response.setContentType(MediaType.TEXT_PLAIN_VALUE);
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + "M6INTHL001.txt");
		
		List<Map<String, Object>>  data = reportDao.getLTTECHINSTALLATION_DATA();
		InputStream inputStream = GenerateLTTECHINSTALLATION.generateBillTxt(data);
		// Copy file content to response output stream
		 try {
				ouputStream = response.getOutputStream();
				int b;
				while ((b = inputStream.read()) != -1) {
					ouputStream.write(b);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			response.flushBuffer();
			FileCopyUtils.copy(inputStream, response.getOutputStream());
	}
	
	@RequestMapping("/M3_CTPT_HT_TECHINSTALLATION")
	public void M3_CTPT_HT_TECHINSTALLATION(HttpServletResponse response) throws IOException {
		ServletOutputStream ouputStream = null;
		response.setContentType(MediaType.TEXT_PLAIN_VALUE);
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + "M8INTHCS001.txt");
		
		List<Map<String, Object>>  data = reportDao.getM3DMDEVICECTPT();
		InputStream inputStream = GenerateTECHINSTALLATION.generateBillTxt(data);
		// Copy file content to response output stream
		 try {
				ouputStream = response.getOutputStream();
				int b;
				while ((b = inputStream.read()) != -1) {
					ouputStream.write(b);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			response.flushBuffer();
			FileCopyUtils.copy(inputStream, response.getOutputStream());
	}
	
	@RequestMapping("/M3_CT_PT_HT_TECHINSTALLATION")
	public void M3_CT_PT_HT_TECHINSTALLATION(HttpServletResponse response) throws IOException {
		ServletOutputStream ouputStream = null;
		response.setContentType(MediaType.TEXT_PLAIN_VALUE);
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + "M8INTHCP001.txt");
		
		List<Map<String, Object>>  data = reportDao.getm3HT_CT_PT_DATA_33_132();
		InputStream inputStream = GenerateTECHINSTALLATION.generateBillTxt(data);
		// Copy file content to response output stream
		 try {
				ouputStream = response.getOutputStream();
				int b;
				while ((b = inputStream.read()) != -1) {
					ouputStream.write(b);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			response.flushBuffer();
			FileCopyUtils.copy(inputStream, response.getOutputStream());
	}
	
	
	@RequestMapping("/M3_SMCMTECHINSTALLATION")
	public void M3_SMCMTECHINSTALLATION(HttpServletResponse response) throws IOException {
		ServletOutputStream ouputStream = null;
		response.setContentType(MediaType.TEXT_PLAIN_VALUE);
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + "M8INTHCP001_CM_SM.txt");
		
		List<Map<String, Object>>  data = reportDao.getm3SMCMTECHINSTALLATION();
		InputStream inputStream = GenerateSMCMTECHINSTALLATION.generateBillTxt(data);
		// Copy file content to response output stream
		 try {
				ouputStream = response.getOutputStream();
				int b;
				while ((b = inputStream.read()) != -1) {
					ouputStream.write(b);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			response.flushBuffer();
			FileCopyUtils.copy(inputStream, response.getOutputStream());
	}
	
	@RequestMapping("/DEVICEGRP")
	public void getDEVICEGRP(HttpServletResponse response) throws IOException {
		ServletOutputStream ouputStream = null;
		response.setContentType(MediaType.TEXT_PLAIN_VALUE);
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + "DEVICEGRP.txt");
		
		List<Map<String, Object>>  data = reportDao.getV_DEVICEGRP();
		InputStream inputStream = GenerateDEVICEGRP.generateBillTxt(data);
		// Copy file content to response output stream
		 try {
				ouputStream = response.getOutputStream();
				int b;
				while ((b = inputStream.read()) != -1) {
					ouputStream.write(b);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			response.flushBuffer();
			FileCopyUtils.copy(inputStream, response.getOutputStream());
	}
	
	@RequestMapping("/M3_DEVICEGRP")
	public void getm3DEVICEGRP(HttpServletResponse response) throws IOException {
		ServletOutputStream ouputStream = null;
		response.setContentType(MediaType.TEXT_PLAIN_VALUE);
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + "M8DGRH001.txt");
		
		List<Map<String, Object>>  data = reportDao.getm3V_DEVICEGRP();
		InputStream inputStream = GenerateDEVICEGRP.generateBillTxt(data);
		// Copy file content to response output stream
		 try {
				ouputStream = response.getOutputStream();
				int b;
				while ((b = inputStream.read()) != -1) {
					ouputStream.write(b);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			response.flushBuffer();
			FileCopyUtils.copy(inputStream, response.getOutputStream());
	}
	
	@RequestMapping("/MOVE_IN")
	public void getMOVE_IN(HttpServletResponse response) throws IOException {
		ServletOutputStream ouputStream = null;
		response.setContentType(MediaType.TEXT_PLAIN_VALUE);
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + "M8MOIH001.txt");
		
		List<Map<String, Object>>  data = reportDao.m3getMoveIn();
		InputStream inputStream = GenerateMOVE_IN.generateBillTxt(data);
		// Copy file content to response output stream
		 try {
				ouputStream = response.getOutputStream();
				int b;
				while ((b = inputStream.read()) != -1) {
					ouputStream.write(b);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			response.flushBuffer();
			FileCopyUtils.copy(inputStream, response.getOutputStream());
	}
	
	@RequestMapping("/METER_READ")
	public void getMETER_READ(HttpServletResponse response) throws IOException {
		ServletOutputStream ouputStream = null;
		response.setContentType(MediaType.TEXT_PLAIN_VALUE);
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + "M8MRDH001.txt");
		
		List<Map<String, Object>>  data = reportDao.getMTR_RDG_UNPIVOT();
		GenerateMETER_READ.servicenocount = reportDao.getServiceNoCount();
		InputStream inputStream = GenerateMETER_READ.generateBillTxt(data);
		// Copy file content to response output stream
		 try {
				ouputStream = response.getOutputStream();
				int b;
				while ((b = inputStream.read()) != -1) {
					ouputStream.write(b);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			response.flushBuffer();
			FileCopyUtils.copy(inputStream, response.getOutputStream());
	}
	
	
	@RequestMapping("/METER_READ_CT")
	public void getMETER_READCT(HttpServletResponse response) throws IOException {
		ServletOutputStream ouputStream = null;
		response.setContentType(MediaType.TEXT_PLAIN_VALUE);
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + "M5MRDH001.txt");
		
		List<Map<String, Object>>  data = reportDao.getMTR_RDG_UNPIVOTCT();
		GenerateMETER_READ.servicenocount = reportDao.getServiceNoCount();
		InputStream inputStream = GenerateCTMETER_READ.generateBillTxt(data);
		// Copy file content to response output stream
		 try {
				ouputStream = response.getOutputStream();
				int b;
				while ((b = inputStream.read()) != -1) {
					ouputStream.write(b);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			response.flushBuffer();
			FileCopyUtils.copy(inputStream, response.getOutputStream());
	}
	
	
	@RequestMapping("/METER_READ_01")
	public void getMETER_READ_01(HttpServletResponse response) throws IOException {
		ServletOutputStream ouputStream = null;
		response.setContentType(MediaType.TEXT_PLAIN_VALUE);
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + "METER_READ_01.txt");
		
		List<Map<String, Object>>  data = reportDao.getMTR_RDG_UNPIVOT_01();
		InputStream inputStream = GenerateMETER_BILL_READ.generateBillTxt(data);
		// Copy file content to response output stream
		 try {
				ouputStream = response.getOutputStream();
				int b;
				while ((b = inputStream.read()) != -1) {
					ouputStream.write(b);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			response.flushBuffer();
			FileCopyUtils.copy(inputStream, response.getOutputStream());
	}
	
	
	@RequestMapping("/SECURITY")
	public void getSECURITY(HttpServletResponse response) throws IOException {
		ServletOutputStream ouputStream = null;
		response.setContentType(MediaType.TEXT_PLAIN_VALUE);
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + "M5SECH001.txt");
		
		List<Map<String, Object>>  data = reportDao.getISUTXTData();
		InputStream inputStream = GenerateSECURITY.generateBillTxt(data);
		// Copy file content to response output stream
		 try {
				ouputStream = response.getOutputStream();
				int b;
				while ((b = inputStream.read()) != -1) {
					ouputStream.write(b);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			response.flushBuffer();
			FileCopyUtils.copy(inputStream, response.getOutputStream());
	}
	@RequestMapping("/M3_SECURITY")
	public void getm3SECURITY(HttpServletResponse response) throws IOException {
		ServletOutputStream ouputStream = null;
		response.setContentType(MediaType.TEXT_PLAIN_VALUE);
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + "M8SECH001.txt");
		
		List<Map<String, Object>>  data = reportDao.m3getSecurityData();
		InputStream inputStream = GenerateSECURITY.generateBillTxt(data);
		// Copy file content to response output stream
		 try {
				ouputStream = response.getOutputStream();
				int b;
				while ((b = inputStream.read()) != -1) {
					ouputStream.write(b);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			response.flushBuffer();
			FileCopyUtils.copy(inputStream, response.getOutputStream());
	}
	
	@RequestMapping("/M3_ACD")
	public void getm3ACD(HttpServletResponse response) throws IOException {
		ServletOutputStream ouputStream = null;
		response.setContentType(MediaType.TEXT_PLAIN_VALUE);
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + "M4SEC_ACDH1.txt");
		
		List<Map<String, Object>>  data = reportDao.m3getACDData();
		InputStream inputStream = GenerateACD.generateBillTxt(data);
		// Copy file content to response output stream
		 try {
				ouputStream = response.getOutputStream();
				int b;
				while ((b = inputStream.read()) != -1) {
					ouputStream.write(b);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			response.flushBuffer();
			FileCopyUtils.copy(inputStream, response.getOutputStream());
	}
	@RequestMapping("/M3_DISCENTER")
	public void getmDISCENTER(HttpServletResponse response) throws IOException {
		ServletOutputStream ouputStream = null;
		response.setContentType(MediaType.TEXT_PLAIN_VALUE);
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + "M8DCEH001.txt");
		
		List<Map<String, Object>>  data = reportDao.m3getUDC();
		InputStream inputStream = GenerateDISCENTER.generateBillTxt(data);
		// Copy file content to response output stream
		 try {
				ouputStream = response.getOutputStream();
				int b;
				while ((b = inputStream.read()) != -1) {
					ouputStream.write(b);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			response.flushBuffer();
			FileCopyUtils.copy(inputStream, response.getOutputStream());
	}
	@RequestMapping("/M3_DISCORDER")
	public void getmDISCORDER(HttpServletResponse response) throws IOException {
		ServletOutputStream ouputStream = null;
		response.setContentType(MediaType.TEXT_PLAIN_VALUE);
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + "M8DCUH001.txt");
		
		List<Map<String, Object>>  data = reportDao.m3getUDC();
		InputStream inputStream = GenerateDISCORDER.generateBillTxt(data);
		// Copy file content to response output stream
		 try {
				ouputStream = response.getOutputStream();
				int b;
				while ((b = inputStream.read()) != -1) {
					ouputStream.write(b);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			response.flushBuffer();
			FileCopyUtils.copy(inputStream, response.getOutputStream());
	}
	@RequestMapping("/M3_DISCDOC")
	public void getmDISCDOC(HttpServletResponse response) throws IOException {
		ServletOutputStream ouputStream = null;
		response.setContentType(MediaType.TEXT_PLAIN_VALUE);
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + "M8DCDH001.txt");
		
		List<Map<String, Object>>  data = reportDao.m3getUDC();
		InputStream inputStream = GenerateDISCDOC.generateBillTxt(data);
		// Copy file content to response output stream
		 try {
				ouputStream = response.getOutputStream();
				int b;
				while ((b = inputStream.read()) != -1) {
					ouputStream.write(b);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			response.flushBuffer();
			FileCopyUtils.copy(inputStream, response.getOutputStream());
	}
	@RequestMapping("/PAYMENT")
	public void getPAYMENT(HttpServletResponse response) throws IOException {
		ServletOutputStream ouputStream = null;
		response.setContentType(MediaType.TEXT_PLAIN_VALUE);
		// Need to chanage Ledger Month for Next Mock
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + "M5PAYH001.txt");
		
		List<Map<String, Object>>  data = reportDao.getISUTXTData();
		InputStream inputStream = GeneratePAYMENT.generateBillTxt(data);
		// Copy file content to response output stream
		 try {
				ouputStream = response.getOutputStream();
				int b;
				while ((b = inputStream.read()) != -1) {
					ouputStream.write(b);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			response.flushBuffer();
			FileCopyUtils.copy(inputStream, response.getOutputStream());
	}
	
	@RequestMapping("/M3_PAYMENT")
	public void getm3PAYMENT(HttpServletResponse response) throws IOException {
		ServletOutputStream ouputStream = null;
		response.setContentType(MediaType.TEXT_PLAIN_VALUE);
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + "M8PAYH001.txt");
		
		List<Map<String, Object>>  data = reportDao.m3getSecurityData();
		InputStream inputStream = GeneratePAYMENT.generateBillTxt(data);
		// Copy file content to response output stream
		 try {
				ouputStream = response.getOutputStream();
				int b;
				while ((b = inputStream.read()) != -1) {
					ouputStream.write(b);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			response.flushBuffer();
			FileCopyUtils.copy(inputStream, response.getOutputStream());
	}
	@RequestMapping("/DM_DEVICE_CT_PT_11")
	public void getDM_DEVICE(HttpServletResponse response) throws IOException {
		ServletOutputStream ouputStream = null;
		response.setContentType(MediaType.TEXT_PLAIN_VALUE);
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + "M5DEVHCS1.txt");
		
		List<Map<String, Object>>  data = reportDao.getDMDEVICECTPT();//11
		//List<Map<String, Object>>  data = reportDao.getHT_CT_PT_DATA_33_132();//33
		
		InputStream inputStream = GenerateDMDEVICECTPT.generateBillTxt(data);
		// Copy file content to response output stream
		 try {
				ouputStream = response.getOutputStream();
				int b;
				while ((b = inputStream.read()) != -1) {
					ouputStream.write(b);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			response.flushBuffer();
			FileCopyUtils.copy(inputStream, response.getOutputStream());
	}
	
	@RequestMapping("/M3_DM_DEVICE_CT_PT_11")
	public void getM3DM_DEVICE(HttpServletResponse response) throws IOException {
		ServletOutputStream ouputStream = null;
		response.setContentType(MediaType.TEXT_PLAIN_VALUE);
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + "M8DEVHCS1.txt");
		
		List<Map<String, Object>>  data = reportDao.getM3DMDEVICECTPT();//11
		//List<Map<String, Object>>  data = reportDao.getHT_CT_PT_DATA_33_132();//33
		
		InputStream inputStream = GenerateDMDEVICECTPT.generateBillTxt(data);
		// Copy file content to response output stream
		 try {
				ouputStream = response.getOutputStream();
				int b;
				while ((b = inputStream.read()) != -1) {
					ouputStream.write(b);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			response.flushBuffer();
			FileCopyUtils.copy(inputStream, response.getOutputStream());
	}
	@RequestMapping("/M3_DM_DEVICE_CT_PT_11_CC")
	public void getM3DM_DEVICECC(HttpServletResponse response) throws IOException {
		ServletOutputStream ouputStream = null;
		response.setContentType(MediaType.TEXT_PLAIN_VALUE);
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + "M8DEVHCS1.txt");
		
		List<Map<String, Object>>  data = reportDao.getM3DMDEVICECTPTCC();//11
		//List<Map<String, Object>>  data = reportDao.getHT_CT_PT_DATA_33_132();//33
		
		InputStream inputStream = GenerateDMDEVICECTPT.generateBillTxt(data);
		// Copy file content to response output stream
		 try {
				ouputStream = response.getOutputStream();
				int b;
				while ((b = inputStream.read()) != -1) {
					ouputStream.write(b);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			response.flushBuffer();
			FileCopyUtils.copy(inputStream, response.getOutputStream());
	}
	@RequestMapping("/M3_LT_DM_DEVICE_CT_PT_11")
	public void getM3LTDM_DEVICE(HttpServletResponse response) throws IOException {
		ServletOutputStream ouputStream = null;
		response.setContentType(MediaType.TEXT_PLAIN_VALUE);
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + "M6_DEVHCS1.txt");
		
		List<Map<String, Object>>  data = reportDao.getLTDMDEVICECTPT();//11
		//List<Map<String, Object>>  data = reportDao.getHT_CT_PT_DATA_33_132();//33
		
		InputStream inputStream = GenerateLTDMDEVICECTPT.generateBillTxt(data);
		// Copy file content to response output stream
		 try {
				ouputStream = response.getOutputStream();
				int b;
				while ((b = inputStream.read()) != -1) {
					ouputStream.write(b);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			response.flushBuffer();
			FileCopyUtils.copy(inputStream, response.getOutputStream());
	}
	
	@RequestMapping("/DM_DEVICE_CT_PT_33_132")
	public void getDM_DEVICE_CT_PT_33_132(HttpServletResponse response) throws IOException {
		ServletOutputStream ouputStream = null;
		response.setContentType(MediaType.TEXT_PLAIN_VALUE);
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + "M4DEVHCT1.txt");		
		List<Map<String, Object>>  data = reportDao.getHT_CT_PT_DATA_33_132();	
		InputStream inputStream = GenerateDMDEVICECTPT_33_132.generateBillTxt(data);
		// Copy file content to response output stream
		 try {
				ouputStream = response.getOutputStream();
				int b;
				while ((b = inputStream.read()) != -1) {
					ouputStream.write(b);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			response.flushBuffer();
			FileCopyUtils.copy(inputStream, response.getOutputStream());
	}
	@RequestMapping("/M3_DM_DEVICE_CT_PT_33_132")
	public void getmeDM_DEVICE_CT_PT_33_132(HttpServletResponse response) throws IOException {
		ServletOutputStream ouputStream = null;
		response.setContentType(MediaType.TEXT_PLAIN_VALUE);
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + "M8DEVHCT1.txt");		
		List<Map<String, Object>>  data = reportDao.getm3HT_CT_PT_DATA_33_132();
		System.out.println("size" + data.size());
		InputStream inputStream = GenerateDMDEVICECTPT_33_132.generateBillTxt(data);
		// Copy file content to response output stream
		 try {
				ouputStream = response.getOutputStream();
				int b;
				while ((b = inputStream.read()) != -1) {
					ouputStream.write(b);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			response.flushBuffer();
			FileCopyUtils.copy(inputStream, response.getOutputStream());
	}
	@RequestMapping("/M3_DM_DEVICE_CT_PT_33_132_CC")
	public void getmeDM_DEVICE_CT_PT_33_132_CC(HttpServletResponse response) throws IOException {
		ServletOutputStream ouputStream = null;
		response.setContentType(MediaType.TEXT_PLAIN_VALUE);
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + "M8DEVHCT1.txt");		
		List<Map<String, Object>>  data = reportDao.getm3HT_CT_PT_DATA_33_132_CC();
		System.out.println("size" + data.size());
		InputStream inputStream = GenerateDMDEVICECTPT_33_132.generateBillTxt(data);
		// Copy file content to response output stream
		 try {
				ouputStream = response.getOutputStream();
				int b;
				while ((b = inputStream.read()) != -1) {
					ouputStream.write(b);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			response.flushBuffer();
			FileCopyUtils.copy(inputStream, response.getOutputStream());
	}
	@RequestMapping("/DM_DEVICE_MTR")
	public void getDM_DEVICEMTR(HttpServletResponse response) throws IOException {
		ServletOutputStream ouputStream = null;
		response.setContentType(MediaType.TEXT_PLAIN_VALUE);
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + "M5DEVH001.txt");
		
		/*List<Map<String, Object>>  data = reportDao.getHT_MTR_CT_PT_DATA();
		InputStream inputStream = GenerateDMDEVICEMETER.generateBillTxt(data);*/
		
		List<Map<String, Object>>  data = reportDao.getHT_MTR_CT_PT_DATA_NO_DATA();
		InputStream inputStream = GenerateDMDEVICEMETER.generateBillTxt(data);
		
		
		/*List<Map<String, Object>>  data = reportDao.getEqpSeq();
	    InputStream inputStream = GenerateTest.generateBillTxt(data);*/
		
		// Copy file content to response output stream
		 try {
				ouputStream = response.getOutputStream();
				int b;
				while ((b = inputStream.read()) != -1) {
					ouputStream.write(b);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			response.flushBuffer();
			FileCopyUtils.copy(inputStream, response.getOutputStream());
	}
	
	@RequestMapping("/M3_DM_DEVICE_MTR")
	public void getM3_DM_DEVICEMTR(HttpServletResponse response) throws IOException {
		ServletOutputStream ouputStream = null;
		response.setContentType(MediaType.TEXT_PLAIN_VALUE);
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + "M8DEVH001.txt");
		
		List<Map<String, Object>>  data = reportDao.getm3HT_MTR_CT_PT_DATA();
		InputStream inputStream = GenerateDMDEVICEMETER.generateBillTxt(data);
		
		/*List<Map<String, Object>>  data = reportDao.getHT_MTR_CT_PT_DATA_NO_DATA();
		InputStream inputStream = GenerateDMDEVICEMETER.generateBillTxt(data);*/
		
		
		/*List<Map<String, Object>>  data = reportDao.getEqpSeq();
	    InputStream inputStream = GenerateTest.generateBillTxt(data);*/
		
		// Copy file content to response output stream
		 try {
				ouputStream = response.getOutputStream();
				int b;
				while ((b = inputStream.read()) != -1) {
					ouputStream.write(b);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			response.flushBuffer();
			FileCopyUtils.copy(inputStream, response.getOutputStream());
	}
	
	@RequestMapping("/M3_DM_DEVICE_MTR_MC")
	public void getM3_DM_DEVICEMTRMC(HttpServletResponse response) throws IOException {
		ServletOutputStream ouputStream = null;
		response.setContentType(MediaType.TEXT_PLAIN_VALUE);
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + "M8DEVH001.txt");
		
		List<Map<String, Object>>  data = reportDao.getm3HT_MTR_CT_PT_DATA_MC();
		InputStream inputStream = GenerateDMDEVICEMETER.generateBillTxt(data);
		
		/*List<Map<String, Object>>  data = reportDao.getHT_MTR_CT_PT_DATA_NO_DATA();
		InputStream inputStream = GenerateDMDEVICEMETER.generateBillTxt(data);*/
		
		
		/*List<Map<String, Object>>  data = reportDao.getEqpSeq();
	    InputStream inputStream = GenerateTest.generateBillTxt(data);*/
		
		// Copy file content to response output stream
		 try {
				ouputStream = response.getOutputStream();
				int b;
				while ((b = inputStream.read()) != -1) {
					ouputStream.write(b);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			response.flushBuffer();
			FileCopyUtils.copy(inputStream, response.getOutputStream());
	}
	
	@RequestMapping("/M3_DM_DEVICE_MTR_CT")
	public void getM3_DM_DEVICEMTRCT(HttpServletResponse response) throws IOException {
		ServletOutputStream ouputStream = null;
		response.setContentType(MediaType.TEXT_PLAIN_VALUE);
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + "M8DEVC001.txt");
		
		List<Map<String, Object>>  data = reportDao.getm3HT_MTR_CT_DATA();
		InputStream inputStream = GenerateDMDEVICEMETER.generateBillTxt(data);
		
		/*List<Map<String, Object>>  data = reportDao.getHT_MTR_CT_PT_DATA_NO_DATA();
		InputStream inputStream = GenerateDMDEVICEMETER.generateBillTxt(data);*/
		
		
		/*List<Map<String, Object>>  data = reportDao.getEqpSeq();
	    InputStream inputStream = GenerateTest.generateBillTxt(data);*/
		
		// Copy file content to response output stream
		 try {
				ouputStream = response.getOutputStream();
				int b;
				while ((b = inputStream.read()) != -1) {
					ouputStream.write(b);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			response.flushBuffer();
			FileCopyUtils.copy(inputStream, response.getOutputStream());
	}
	
	@RequestMapping("/M3_LT_DM_DEVICE_MTR")
	public void getM3_LT_DM_DEVICEMTR(HttpServletResponse response) throws IOException {
		ServletOutputStream ouputStream = null;
		response.setContentType(MediaType.TEXT_PLAIN_VALUE);
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + "M6_DEVH001.txt");
		
		List<Map<String, Object>>  data = reportDao.getLT_MTR_CT_PT_DATA();
		InputStream inputStream = GenerateLTDMDEVICEMETER.generateBillTxt(data);
		
		/*List<Map<String, Object>>  data = reportDao.getHT_MTR_CT_PT_DATA_NO_DATA();
		InputStream inputStream = GenerateDMDEVICEMETER.generateBillTxt(data);*/
		
		
		/*List<Map<String, Object>>  data = reportDao.getEqpSeq();
	    InputStream inputStream = GenerateTest.generateBillTxt(data);*/
		
		// Copy file content to response output stream
		 try {
				ouputStream = response.getOutputStream();
				int b;
				while ((b = inputStream.read()) != -1) {
					ouputStream.write(b);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			response.flushBuffer();
			FileCopyUtils.copy(inputStream, response.getOutputStream());
	}
	
	@RequestMapping("/DM_DEVLOC")
	public void DM_DEVLOC(HttpServletResponse response) throws IOException {
		ServletOutputStream ouputStream = null;
		response.setContentType(MediaType.TEXT_PLAIN_VALUE);
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + "DM_DEVLOC.txt");
		
		List<Map<String, Object>>  data = reportDao.m3getISUTXTData();
		InputStream inputStream = GenerateDMDEVLOC.generateBillTxt(data);
		// Copy file content to response output stream
		 try {
				ouputStream = response.getOutputStream();
				int b;
				while ((b = inputStream.read()) != -1) {
					ouputStream.write(b);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			response.flushBuffer();
			FileCopyUtils.copy(inputStream, response.getOutputStream());
	}
	
	@RequestMapping("/M3_DM_DEVLOC")
	public void M3_DM_DEVLOC(HttpServletResponse response) throws IOException {
		ServletOutputStream ouputStream = null;
		response.setContentType(MediaType.TEXT_PLAIN_VALUE);
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + "M8DLCH001.txt");
		
		List<Map<String, Object>>  data = reportDao.m3getCONNOBJECT();
		InputStream inputStream = GenerateDMDEVLOC.generateBillTxt(data);
		// Copy file content to response output stream
		 try {
				ouputStream = response.getOutputStream();
				int b;
				while ((b = inputStream.read()) != -1) {
					ouputStream.write(b);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			response.flushBuffer();
			FileCopyUtils.copy(inputStream, response.getOutputStream());
	}
	@RequestMapping("/M3_LT_DM_DEVLOC")
	public void M3_LT_DM_DEVLOC(HttpServletResponse response) throws IOException {
		ServletOutputStream ouputStream = null;
		response.setContentType(MediaType.TEXT_PLAIN_VALUE);
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + "M6_DLCH001.txt");
		
		List<Map<String, Object>>  data = reportDao.getM3_LT_DM_DEVLOC();
		InputStream inputStream = GenerateLTDMDEVLOC.generateBillTxt(data);
		// Copy file content to response output stream
		 try {
				ouputStream = response.getOutputStream();
				int b;
				while ((b = inputStream.read()) != -1) {
					ouputStream.write(b);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			response.flushBuffer();
			FileCopyUtils.copy(inputStream, response.getOutputStream());
	}
	
	@RequestMapping("/BI_FACTS")
	public void BI_FACTS(HttpServletResponse response) throws IOException {
		ServletOutputStream ouputStream = null;
		response.setContentType(MediaType.TEXT_PLAIN_VALUE);
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + "BI_FACTS.txt");
		
		List<Map<String, Object>>  data = reportDao.getFacts();
		DisplayQUANT.season = reportDao. getSeasonalServices();
		DisplayQUANT.remainingamt = reportDao.getTrueUpRemaingAmt();
		DisplayQUANT.totkwh = reportDao.getFYKWH();
		DisplayQUANT.gridinstldt = reportDao.getGridInstlDt();
		InputStream inputStream = GenerateBIFACTS.generateBillTxt(data);
		// Copy file content to response output stream
		 try {
				ouputStream = response.getOutputStream();
				int b;
				while ((b = inputStream.read()) != -1) {
					ouputStream.write(b);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			response.flushBuffer();
			FileCopyUtils.copy(inputStream, response.getOutputStream());
	}
	
	@RequestMapping("/BI_M3_FACTS")
	public void BI_M3_FACTS(HttpServletResponse response) throws IOException {
		ServletOutputStream ouputStream = null;
		response.setContentType(MediaType.TEXT_PLAIN_VALUE);
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + "M8FACH001.txt");
		
		List<Map<String, Object>>  data = reportDao.getFacts();
		DisplayQUANT.season = reportDao. getSeasonalServices();
		DisplayQUANT.remainingamt = reportDao.getTrueUpRemaingAmt();// Need to change Remaining True Up Months
		DisplayQUANT.totkwh = reportDao.getFYKWH();// Need to Change To date
		DisplayQUANT.gridinstldt = reportDao.getGridInstlDt();
		DisplayQUANT.fy21flag = reportDao.getFy21();
		DisplayQUANT.htfy21flag = reportDao.getHTFy21();
		DisplayQUANT.crosslist =  reportDao.getCrossChg();
		DisplayQUANT.wheellist = reportDao.getWheelChg();
		DisplayQUANT.fppca2 = reportDao.getfppca2();
		DisplayQUANT.fppcach = reportDao.getfppcach();
		DisplayQUANT.htmetcol = reportDao.getHtMeterCol();
		
		InputStream inputStream = GenerateBIFACTS.generateBillTxt(data);
		// Copy file content to response output stream
		 try {
				ouputStream = response.getOutputStream();
				int b;
				while ((b = inputStream.read()) != -1) {
					ouputStream.write(b);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			response.flushBuffer();
			FileCopyUtils.copy(inputStream, response.getOutputStream());
	}
	
	@RequestMapping("/BI_M3_NEW_FACTS")
	public void BI_M3_NEW_FACTS(HttpServletResponse response) throws IOException {
		ServletOutputStream ouputStream = null;
		response.setContentType(MediaType.TEXT_PLAIN_VALUE);
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + "M8FACH001.txt");
		
		List<Map<String, Object>>  data = reportDao.getNewFacts();
		DisplayQUANT.season = reportDao. getSeasonalServices();
		DisplayQUANT.remainingamt = reportDao.getTrueUpRemaingAmt();// Need to change Remaining True Up Months
		DisplayQUANT.totkwh = reportDao.getFYKWH();// Need to Change To date
		DisplayQUANT.gridinstldt = reportDao.getGridInstlDt();
		DisplayQUANT.fy21flag = reportDao.getFy21();
		DisplayQUANT.htfy21flag = reportDao.getHTFy21();
		DisplayQUANT.crosslist =  reportDao.getCrossChg();
		DisplayQUANT.wheellist = reportDao.getWheelChg();
		DisplayQUANT.fppca2 = reportDao.getfppca2();
		DisplayQUANT.fppcach = reportDao.getfppcach();
		DisplayQUANT.htmetcol = reportDao.getHtMeterCol();
		
		InputStream inputStream = GenerateBINEWFACTS.generateBillTxt(data);
		// Copy file content to response output stream
		 try {
				ouputStream = response.getOutputStream();
				int b;
				while ((b = inputStream.read()) != -1) {
					ouputStream.write(b);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			response.flushBuffer();
			FileCopyUtils.copy(inputStream, response.getOutputStream());
	}
	
	@RequestMapping("/DMFULLINSTALLATION")
	public void TECHNICALINSTALLATION(HttpServletResponse response) throws IOException {
		ServletOutputStream ouputStream = null;
		response.setContentType(MediaType.TEXT_PLAIN_VALUE);
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + "M8INFH001.txt");
		
		List<Map<String, Object>>  data = reportDao.getV_M3_HT_FULL_INST_data();
		InputStream inputStream = GenerateDMFULLINSTALLATION.generateBillTxt(data);
		// Copy file content to response output stream
		 try {
				ouputStream = response.getOutputStream();
				int b;
				while ((b = inputStream.read()) != -1) {
					ouputStream.write(b);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			response.flushBuffer();
			FileCopyUtils.copy(inputStream, response.getOutputStream());
	}
	
	@RequestMapping("/CTDMFULLINSTALLATION")
	public void CTTECHNICALINSTALLATION(HttpServletResponse response) throws IOException {
		ServletOutputStream ouputStream = null;
		response.setContentType(MediaType.TEXT_PLAIN_VALUE);
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + "M4INFH001.txt");
		
		List<Map<String, Object>>  data = reportDao.getCTV_M3_HT_FULL_INST_data();
		InputStream inputStream = GenerateCTDMFULLINSTALLATION2.generateBillTxt(data);
		// Copy file content to response output stream
		 try {
				ouputStream = response.getOutputStream();
				int b;
				while ((b = inputStream.read()) != -1) {
					ouputStream.write(b);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			response.flushBuffer();
			FileCopyUtils.copy(inputStream, response.getOutputStream());
	}
	
	/*@RequestMapping("/M3_CTPT_DMFULLINSTALLATION")
	public void M3_CTPT_DMFULLINSTALLATION(HttpServletResponse response) throws IOException {
		ServletOutputStream ouputStream = null;
		response.setContentType(MediaType.TEXT_PLAIN_VALUE);
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + "FULLINSTALLATION.txt");
		
		List<Map<String, Object>>  data = reportDao.getDM_FULL_INST_DATA();
		InputStream inputStream = GenerateDMFULLINSTALLATION.generateBillTxt(data);
		// Copy file content to response output stream
		 try {
				ouputStream = response.getOutputStream();
				int b;
				while ((b = inputStream.read()) != -1) {
					ouputStream.write(b);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			response.flushBuffer();
			FileCopyUtils.copy(inputStream, response.getOutputStream());
	}
	
	@RequestMapping("/M3_CT_PT_DMFULLINSTALLATION")
	public void M3_CT_PT_DMFULLINSTALLATION(HttpServletResponse response) throws IOException {
		ServletOutputStream ouputStream = null;
		response.setContentType(MediaType.TEXT_PLAIN_VALUE);
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + "FULLINSTALLATION.txt");
		
		List<Map<String, Object>>  data = reportDao.getDM_FULL_INST_DATA();
		InputStream inputStream = GenerateDMFULLINSTALLATION.generateBillTxt(data);
		// Copy file content to response output stream
		 try {
				ouputStream = response.getOutputStream();
				int b;
				while ((b = inputStream.read()) != -1) {
					ouputStream.write(b);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			response.flushBuffer();
			FileCopyUtils.copy(inputStream, response.getOutputStream());
	}*/
	
	@RequestMapping("/M3_LT_DMFULLINSTALLATION")
	public void M3_LT_DMFULLINSTALLATION(HttpServletResponse response) throws IOException {
		ServletOutputStream ouputStream = null;
		response.setContentType(MediaType.TEXT_PLAIN_VALUE);
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + "M6_INFH001.txt");
		
		List<Map<String, Object>>  data = reportDao.getV_M3_LT_FULL_INST_data();
		InputStream inputStream = GenerateLTDMFULLINSTALLATION.generateBillTxt(data);
		// Copy file content to response output stream
		 try {
				ouputStream = response.getOutputStream();
				int b;
				while ((b = inputStream.read()) != -1) {
					ouputStream.write(b);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			response.flushBuffer();
			FileCopyUtils.copy(inputStream, response.getOutputStream());
	}
	
	@RequestMapping("/OPENITEMS")
	public void OPENITEMS(HttpServletResponse response) throws IOException {
		ServletOutputStream ouputStream = null;
		response.setContentType(MediaType.TEXT_PLAIN_VALUE);
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + "M8DOCH001.txt");
		
		List<Map<String, Object>>  data = reportDao.getOPENITEMS();
		InputStream inputStream = GenerateOPEN_ITEMS.generateBillTxt(data);
		// Copy file content to response output stream
		 try {
				ouputStream = response.getOutputStream();
				int b;
				while ((b = inputStream.read()) != -1) {
					ouputStream.write(b);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			response.flushBuffer();
			FileCopyUtils.copy(inputStream, response.getOutputStream());
	}
	
	
	@GetMapping("/AgeWiseServiceBalance")
	public ModelAndView getAgeWiseServiceLedgerAbstractPage(@RequestParam("circle") String circle,@RequestParam("agewise") String agewise,@RequestParam("status") String status,@RequestParam("monthyear") String monthyear) {
		ModelAndView mav = new ModelAndView("AgeWiseServiceBalance");
		List<Map<String, Object>> agewiselist = reportDao.getAgeWiseServicesLedgerAbstractDetails(circle,agewise,status,monthyear);
		if (agewiselist.isEmpty()) {
			mav.addObject("error", "NO DATA FOUND");
		} else {
			mav.addObject("agewise", agewiselist);
			
		}
		return mav;
	}

	@GetMapping("/servicetypeservices")
	public ModelAndView getServiceTypeSerivces(@RequestParam("circle") String circle,@RequestParam("fyear") String fyear,@RequestParam("servicetype") String servicetype,@RequestParam("stdesc") String stdesc) {
		ModelAndView mav = new ModelAndView("servicewiseservicetyperevenue");
		List<Map<String, Object>> agewiselist = reportDao.getServiceTypeServices(circle,fyear,servicetype);
		if (agewiselist.isEmpty()) {
			mav.addObject("error", "NO DATA FOUND");
		} else {
			mav.addObject("agewise", agewiselist);
			mav.addObject("title",  "List of "+stdesc +" Services "+fyear);
			
		}
		return mav;
	}
	
	@GetMapping("/printtextfile")
	@ResponseBody
	public void getPrintTextFile(HttpServletRequest request, HttpServletResponse response)  throws IOException {
		ConsumerDetails consumerdetails =  (ConsumerDetails)request.getSession().getAttribute("consumerdetails");
		/*System.out.println("Consumer Details :" +consumerdetails.getCIRNAME());
		System.out.println("Ledger Details :" +request.getParameter("account"));*/
		ServletOutputStream ouputStream = null;
		InputStream inputStream =  (InputStream) generateServiceTxt(request);
		response.setContentType("text/plain");
		response.addHeader("Content-Disposition", "inline;filename=" + consumerdetails.getCTUSCNO()+".txt");
		//response.addHeader("Content-Disposition", "attachment; filename=" + consumerdetails.getCTUSCNO()+".txt");
		try {
			ouputStream = response.getOutputStream();
			int b;
			while ((b = inputStream.read()) != -1) {
				ouputStream.write(b);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		response.flushBuffer();
		FileCopyUtils.copy(inputStream, response.getOutputStream());
	}
	
	@GetMapping("/circelwisedemandreport")
	public String showCircleWiseDeamndAbstractPage() {
		return "circelwisedemandreport";
	}

	@PostMapping("/circelwisedemandreport")
	public ModelAndView getCircelwisedemandreport(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("circelwisedemandreport");
		List<Map<String, Object>> circleDemandabt = reportDao.getCircleWiseAbstract(request);
		if (circleDemandabt.isEmpty()) {
			mav.addObject("error", "NO DATA FOUND");
		} else {
			mav.addObject("cdmd", circleDemandabt);
		}
		return mav;
	}

	@GetMapping("/SectionWiseArrears")
	public String showSectionWiseArrearsPage() {
		return "SectionWiseArrears";
	}

	@PostMapping("/SectionWiseArrears")
	public ModelAndView getSectionWiseArrearsDetails(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("SectionWiseArrears");
		List<Map<String, Object>> section = reportDao.getSectionWiseArrearsDetails(request);
		System.out.println(section.isEmpty());
		if (section.isEmpty()) {
			mav.addObject("error", "NO DATA FOUND");
		} else {
			mav.addObject("section", section);
		}
		return mav;

	}

	@GetMapping("/Liveandbill")
	public String showLiveAndBillPage() {
		return "Liveandbill";
	}

	@PostMapping("/Liveandbill")
	public ModelAndView getLiveAndBillDetails(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("Liveandbill");
		List<Map<String, Object>> live = reportDao.getLiveAndBillDetails(request);
		if (live.isEmpty()) {
			mav.addObject("error", "NO DATA FOUND");
		} else {
			mav.addObject("live", live);
		}
		return mav;
	}

	@GetMapping("/SolarEnergyReport")
	public String showSolarEnergyPage() {
		return "SolarEnergyReport";
	}

	@PostMapping("/SolarEnergyReport")
	public ModelAndView getSolarEnergyDetails(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("SolarEnergyReport");
		List<Map<String, Object>> ser = reportDao.getSolarEnergyReport(request);
		if (ser.isEmpty()) {
			mav.addObject("error", "NO DATA FOUND");
		} else {
			mav.addObject("ser", ser);
		}
		return mav;
	}

	@GetMapping("/aqua")
	public String showAquaPage() {
		return "aqua";
	}

	@PostMapping("/aqua")
	public ModelAndView getAquaDetails(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("aqua");
		List<Map<String, Object>> aqua = reportDao.getAquaDetails(request);
		if (aqua.isEmpty()) {
			mav.addObject("error", "NO DATA FOUND");
		} else {
			mav.addObject("aqua", aqua);
		}
		return mav;
	}

	@GetMapping("/Lfi")
	public String showLfiPage() {
		return "Lfi";
	}

	@PostMapping("/Lfi")
	public ModelAndView getLFIDetails(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("Lfi");
		List<Map<String, Object>> lfi = reportDao.getLFIDetails(request).stream()
				.sorted(Comparator.comparingInt(s -> Integer.parseInt(s.get("BTSCNO").toString().substring(3))))
				.collect(Collectors.toList());
		if (lfi.isEmpty()) {
			mav.addObject("error", "NO DATA FOUND");
		} else {
			mav.addObject("lfi", lfi);
		}
		return mav;
	}

	@GetMapping("/liveandbilstopcategory")
	public String showCWCESLPage() {
		return "liveandbilstopcategory";
	}

	@PostMapping("/liveandbilstopcategory")
	public ModelAndView getCWCESLDetails(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("liveandbilstopcategory");
		List<Map<String, Object>> cwcesl = reportDao.getCWCESLDetails(request);
		if (cwcesl.isEmpty()) {
			mav.addObject("error", "NO DATA FOUND");
		} else {
			mav.addObject("cwcesl", cwcesl);
		}
		return mav;
	}

	@GetMapping("/ConsumerMaster")
	public String showConsuemrMaster() {
		return "ConsumerMaster";
	}

	@PostMapping("/ConsumerMaster")
	public ModelAndView getConsumerMasterDetails(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("ConsumerMaster");
		String circle = request.getParameter("circle");
		List<Map<String, Object>> consumer = reportDao.getConsumerMasterDetails(request);
		if (consumer.isEmpty()) {
			mav.addObject("error", "NO DATA FOUND");
		} else {
			mav.addObject("consumer", consumer);
			mav.addObject("circle", circle);
		}
		return mav;
	}
	
	@GetMapping("/kyc")
	public ModelAndView kyc() {		
		ModelAndView mav = new ModelAndView("kyc");
		List<Map<String, Object>> kycabs = reportDao.getKYCAbs();
		if (kycabs.isEmpty()) {
			mav.addObject("error", "NO DATA FOUND");
		} else {
			mav.addObject("kycabs", kycabs);
		}
		return mav;
	}

	@PostMapping("/kyc")
	public ModelAndView kyc(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("kyc");
		String circle = request.getParameter("circle");
		List<Map<String, Object>> kycabs = reportDao.getKYCAbs();
		List<Map<String, Object>> consumer = reportDao.getKYC(request);
		if (consumer.isEmpty()) {
			mav.addObject("error", "NO DATA FOUND");
		} else {
			mav.addObject("consumer", consumer);
			mav.addObject("kycabs", kycabs);
			mav.addObject("circle", circle);
		}
		return mav;
	}
	
	@GetMapping("/TCSExemption")
	public String showTCSExemption() {
		return "TCSExemption";
	}

	@PostMapping("/TCSExemption")
	public ModelAndView getTCSExemptionDetails(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("TCSExemption");
		String circle = request.getParameter("circle");
		List<Map<String, Object>> consumer = reportDao.getTCSExemptionDetails(request);
		if (consumer.isEmpty()) {
			mav.addObject("error", "NO DATA FOUND");
		} else {
			mav.addObject("consumer", consumer);
			mav.addObject("circle", circle);
		}
		return mav;
	}
	

	@GetMapping("/sppcs")
	public ModelAndView getStartupPowerServicesDetails(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("sppcs");
		List<Map<String, Object>> sppcs = reportDao.getStartupPowerServicesDetails(request).stream()
				.sorted(Comparator.comparingInt(s -> Integer.parseInt(s.get("USCNO").toString().substring(3))))
				.collect(Collectors.toList());
		if (sppcs.isEmpty()) {
			mav.addObject("error", "NO DATA FOUND");
		} else {
			mav.addObject("sppcs", sppcs);
		}
		return mav;
	}

	@GetMapping("/ServiceRelease")
	public String showServiceReleasepage() {
		return "ServiceRelease";
	}

	@PostMapping("/ServiceRelease")
	public ModelAndView getServiceReleaseDetails(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("ServiceRelease");
		List<Map<String, Object>> service = reportDao.getServiceReleaseDetails(request);
		if (service.isEmpty()) {
			mav.addObject("error", "NO DATA FOUND");
		} else {
			mav.addObject("service", service);
		}
		return mav;
	}

	@GetMapping("/EmailChecklist")
	public String emailCheckListPage() {
		return "EmailChecklist";
	}

	@PostMapping("/EmailChecklist")
	public ModelAndView getEmailChekListDetails(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("EmailChecklist");
		List<Map<String, Object>> email = reportDao.getEmailChekListDetails(request);
		if (email.isEmpty()) {
			mav.addObject("fail", "No Data Found");
		} else {
			mav.addObject("email", email);
		}
		return mav;
	}

	@GetMapping("/trueupchg")
	public String trueupchgPage() {
		return "trueupchg";
	}

	@PostMapping("/trueupchg")
	public ModelAndView gettrueupchgDetails(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("trueupchg");
		List<Map<String, Object>> email = reportDao.getTrueUpListDetails(request);
		if (email.isEmpty()) {
			mav.addObject("fail", "No Data Found");
		} else {
			mav.addObject("trueup", email);
		}
		return mav;
	}
	
	@GetMapping("/oldcolonyrdng")
	public String oldcolonyrdngPage() {
		return "oldcolonyrdng";
	}

	@PostMapping("/oldcolonyrdng")
	public ModelAndView getoldcolonyrdngDetails(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("oldcolonyrdng");
		List<Map<String, Object>> email = reportDao.getOldColony(request);
		if (email.isEmpty()) {
			mav.addObject("fail", "No Data Found");
		} else {
			mav.addObject("trueup", email);
		}
		return mav;
	}
	
	@GetMapping("/oldcolonyrdngtu")
	public String oldcolonyrdngfppPage() {
		return "oldcolonyrdngtu";
	}

	@PostMapping("/oldcolonyrdngtu")
	public ModelAndView getoldcolonyrdngfppDetails(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("oldcolonyrdngtu");
		List<Map<String, Object>> email = reportDao.getOldColonyFPP(request);
		if (email.isEmpty()) {
			mav.addObject("fail", "No Data Found");
		} else {
			mav.addObject("trueup", email);
		}
		return mav;
	}
	
	@GetMapping("/lthttrueupchg")
	public String lthttrueupchgPage() {
		return "lthttrueupchg";
	}

	@PostMapping("/lthttrueupchg")
	public ModelAndView getlthttrueupchgDetails(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("lthttrueupchg");
		try {
		List<Map<String, Object>> email = reportDao.getLTHTTrueUpCharges(request);
		if (email.isEmpty()) {
			mav.addObject("fail", "No Data Found");
		} else {
			mav.addObject("trueup", email);
		}}catch(Exception e) {
			e.printStackTrace();
		}
		return mav;
	}
	
	
	@GetMapping("/lthtfppcachg")
	public String lthtfppcachgPage() {
		return "lthtfppcachg";
	}

	@PostMapping("/lthtfppcachg")
	public ModelAndView getlthtfppcachgDetails(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("lthtfppcachg");
		try {
		List<Map<String, Object>> email = reportDao.getLTHTFPPCACharges(request);
		if (email.isEmpty()) {
			mav.addObject("fail", "No Data Found");
		} else {
			mav.addObject("trueup", email);
		}}catch(Exception e) {
			e.printStackTrace();
		}
		return mav;
	}
	@GetMapping("/htbsfppcachg")
	public String htbsfppcachgPage() {
		return "htbsfppcachg";
	}

	@PostMapping("/htbsfppcachg")
	public ModelAndView gethtbsfppcachgDetails(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("htbsfppcachg");
		try {
		List<Map<String, Object>> email = reportDao.getHTBSFPPCACharges(request);
		if (email.isEmpty()) {
			mav.addObject("fail", "No Data Found");
		} else {
			mav.addObject("trueup", email);
		}}catch(Exception e) {
			e.printStackTrace();
		}
		return mav;
	}
	
	
	@GetMapping("/noemails")
	public String noEmailsPage() {
		return "noemails";
	}

	@PostMapping("/noemails")
	public ModelAndView getNoemailsDetails(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("noemails");
		List<Map<String, Object>> email = reportDao.getNoemailsDetails(request);
		if (email.isEmpty()) {
			mav.addObject("fail", "No Data Found");
		} else {
			mav.addObject("email", email);
		}
		return mav;
	}

	@GetMapping("/emailsent")
	public String showEmailSentPage() {
		return "emailsent";
	}

	@PostMapping("/emailsent")
	public ModelAndView getNoemailsSentDetails(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("emailsent");
		List<Map<String, Object>> email = reportDao.getNoemailsSentDetails(request);
		if (email.isEmpty()) {
			mav.addObject("fail", "No Data Found");
		} else {
			mav.addObject("email", email);
		}
		return mav;
	}

	@GetMapping("ssd")
	public String showServiceWiseLedgerSdReportPage() {
		return "ServiceWiseLedgerSdReport";
	}

	@PostMapping("/ssd")
	public ModelAndView getServiceWiseLedgerDetails(HttpServletRequest request) {

		int CurrentYear = Calendar.getInstance().get(Calendar.YEAR);
		int CurrentMonth = (Calendar.getInstance().get(Calendar.MONTH) + 1);
		String financiyalYearFrom = "";
		String financiyalYearTo = "";
		if (CurrentMonth < 4) {
			financiyalYearFrom = "01-04-" + (CurrentYear - 1);
			financiyalYearTo = "31-03-" + (CurrentYear);
		} else {
			financiyalYearFrom = "01-04-" + (CurrentYear - 1);
			financiyalYearTo = "31-03-" + (CurrentYear);
		}

		ModelAndView mav = new ModelAndView("ServiceWiseLedgerSdReport");
		List<Map<String, Object>> service = reportDao.getServiceWiseLedgerDetails(request, financiyalYearFrom.trim(),
				financiyalYearTo.trim());
		if (service.isEmpty()) {
			mav.addObject("error", "NO DATA FOUND");
		} else {
			mav.addObject("sd", service);
			mav.addObject("from", financiyalYearFrom);
			mav.addObject("to", financiyalYearTo);
		}
		return mav;
	}

	@GetMapping("/payhist")
	public ModelAndView showPayHistPage(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("payhist");
		String uscno = request.getParameter("no");
		String from = request.getParameter("from");
		String to = request.getParameter("to");
		List<Map<String, Object>> service = reportDao.getPayDetails(uscno, from, to);
		if (service.isEmpty()) {
			mav.addObject("error", "NO DATA FOUND");
		} else {
			mav.addObject("pay", service);
		}
		return mav;
	}

	@GetMapping("/PanCheckList")
	public String showPanCheckListPage() {
		return "PanCheckList";
	}

	@PostMapping("/PanCheckList")
	public ModelAndView getPanCheckListDetails(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("PanCheckList");
		List<Map<String, Object>> pan = reportDao.getPanCheckListDetails(request);
		if (pan.isEmpty()) {
			mav.addObject("error", "No Data Found");
		} else {
			mav.addObject("pan", pan);
		}
		return mav;
	}

	@GetMapping("/MobileCheckList")
	public String showMobileCheckListPage() {
		return "MobileCheckList";
	}

	@GetMapping("/nomobiles")
	public String showNoMobileCheckListPage() {
		return "nomobiles";
	}

	@PostMapping("/nomobiles")
	public ModelAndView getNoMobileCheckListDetails(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("nomobiles");
		List<Map<String, Object>> mobile = reportDao.getNoMobileCheckListDetails(request);
		if (mobile.isEmpty()) {
			mav.addObject("fail", "No Data Found");
		} else {
			mav.addObject("mobile", mobile);
		}
		return mav;
	}

	@PostMapping("/MobileCheckList")
	public ModelAndView getMobileCheckListDetails(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("MobileCheckList");
		List<Map<String, Object>> mobile = reportDao.getMobileCheckListDetails(request);
		if (mobile.isEmpty()) {
			mav.addObject("fail", "No Data Found");
		} else {
			mav.addObject("mobile", mobile);
		}
		return mav;
	}

	@GetMapping("/mf")
	public String showMFPage() {
		return "mf";
	}

	@PostMapping("/mf")
	public ModelAndView getMfReportDetails(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("mf");
		List<Map<String, Object>> mf = reportDao.getMfReportDetails(request);
		if (mf.isEmpty()) {
			mav.addObject("fail", "No Data Found");
		} else {
			mav.addObject("mf", mf);
		}
		return mav;
	}

	@GetMapping("/monthcb")
	public String showMonthcbPage() {
		return "monthcb";
	}

	@PostMapping("/monthcb")
	public ModelAndView getMonthClosingDetails(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("monthcb");
		List<Map<String, Object>> cb = reportDao.getMonthClosingDetails(request);
		if (cb.isEmpty()) {
			mav.addObject("fail", "No Data Found");
		} else {
			mav.addObject("cb", cb);
		}
		return mav;
	}
	
	@GetMapping("/gridsupporting")
	public String showGripsupporting() {
		return "gridsupporting";
	}

	@PostMapping("/gridsupporting")
	public ModelAndView getGripsupporting(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("gridsupporting");
		List<Map<String, Object>> cb = reportDao.getGridSupportingDetails(request);
		List<Map<String, Object>> circle = reportDao.getGridCircle(request);
		if (cb.isEmpty()) {
			mav.addObject("fail", "No Data Found");
		} else {
			mav.addObject("cb", cb);
			mav.addObject("circle", circle);
		}
		return mav;
	}

	@GetMapping("/catwisegridsupporting")
	public String showCWGripsupporting() {
		return "catwisegridsupporting";
	}

	@PostMapping("/catwisegridsupporting")
	public ModelAndView getCWGripsupporting(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("catwisegridsupporting");
		List<Map<String, Object>> circle = reportDao.getGridCatCircle(request);
		if (circle.isEmpty()) {
			mav.addObject("fail", "No Data Found");
		} else {

			mav.addObject("circle", circle);
		}
		return mav;
	}
	
	
	@GetMapping(value = { "/virtual" })
	public String showVirtualPage() {
		return "virtual";
	}

	@PostMapping("/virtual")
	public ModelAndView getVirtaulBankgDetails(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("virtual");
		List<Map<String, Object>> virtual = reportDao.getVirtaulBankgDetails(request);
		if (virtual.isEmpty()) {
			mav.addObject("fail", "No Data Found");
		} else {
			mav.addObject("virtual", virtual);
		}
		return mav;
	}

	@GetMapping("/NegativeCb")
	public String showNegativeCbPage() {
		return "NegativeCb";
	}

	@PostMapping("/NegativeCb")
	public ModelAndView getNegativeCbDetails(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("NegativeCb");
		List<Map<String, Object>> negative = reportDao.getNegativeCbDetails(request);
		if (negative.isEmpty()) {
			mav.addObject("fail", "No Data Found");
		} else {
			mav.addObject("negative", negative);
		}
		return mav;
	}

	@GetMapping("/ComparisonDemandReport")
	public String showComparisonDemandReportPage() {
		return "ComparisonDemandReport";
	}

	@PostMapping("/ComparisonDemandReport")
	public ModelAndView getComparisonDemandReport(HttpServletRequest request) {

		ModelAndView mav = new ModelAndView("ComparisonDemandReport");
		List<Map<String, Object>> cdr = reportDao.getComparisonDemandReport(request);
		if (cdr.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("cdr", cdr);
			String monthYear = request.getParameter("month") + "-" + request.getParameter("year");
			mav.addObject("monthYear", monthYear);
			mav.addObject("premonth", reportDao.getPreviousDate(monthYear));
		}
		return mav;
	}

	@GetMapping("/Volopen")
	public String voltagePageShow() {
		return "Volopen";
	}

	@PostMapping("/Volopen")
	public ModelAndView getOpenAccessSalesDetails(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("Volopen");
		List<Map<String, Object>> open_sales = reportDao.getOpenAccessSalesDetails(request);
		System.out.println(open_sales.isEmpty());
		if (open_sales.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("open", open_sales);
		}
		return mav;
	}

	@GetMapping("/secWiseSplitDemand")
	public String showSectionWiseSplitDemand() {
		return "secWiseSplitDemand";
	}

	@PostMapping("/sectionWiseDemand")
	public ModelAndView getSectionWiseSplitDemandDetails(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("secWiseSplitDemand");
		List<Map<String, Object>> sectiondemand = reportDao.getSectionWiseSplitDemandDetails(request);
		System.out.println(sectiondemand.isEmpty());
		if (sectiondemand.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("section", sectiondemand);
		}
		return mav;
	}

	@GetMapping("/catsplit")
	public String showCatWiseSplitDemand() {
		return "catsplit";
	}

	@PostMapping("/catsplit")
	public ModelAndView getCatWiseSplitDemandDetails(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("catsplit");
		List<Map<String, Object>> sectiondemand = reportDao.getCateWiseSplitDemandDetails(request);
		System.out.println(sectiondemand.isEmpty());
		if (sectiondemand.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("section", sectiondemand);
		}
		return mav;
	}

	@GetMapping("/dlist")
	public String showDlistPage() {
		return "dlist";
	}

	@PostMapping("/dlist")
	public ModelAndView getDlistPageDetails(HttpServletRequest request) {
		
		ModelAndView mav = new ModelAndView("dlist");
		YearMonth thisMonth = YearMonth.now();
		String ob;
		String demand;
		DateTimeFormatter monthYearFormatter = DateTimeFormatter.ofPattern("MMM-yyyy");
		LocalDateTime now = LocalDateTime.now();
		int day = now.getDayOfMonth();
		if (day < 5) {
			YearMonth lastMonth = thisMonth.minusMonths(1);
			YearMonth twoMonthsAgo = thisMonth.minusMonths(2);
			ob = lastMonth.format(monthYearFormatter);
			demand = twoMonthsAgo.format(monthYearFormatter);
		} else {
			YearMonth lastMonth = thisMonth.minusMonths(1);
			ob = thisMonth.format(monthYearFormatter);
			demand = lastMonth.format(monthYearFormatter);
		}
		List<Map<String, Object>> dlist = reportDao.getDlistDetails(request, demand.toString().toUpperCase(),
				ob.toString().toUpperCase());
		
		Map<String, String> dlistpend = reportDao.getDListReason();
		for(Map<String, Object> l : dlist) {
			try {
			l.put("REASON", Optional.ofNullable(dlistpend.get(l.get("SCNO")).trim().length()>1?dlistpend.get(l.get("SCNO")).split("-")[0]:"").orElse(""));
			l.put("REMARK", Optional.ofNullable(dlistpend.get(l.get("SCNO")).trim().length()>1?dlistpend.get(l.get("SCNO")).split("-")[1]:"").orElse(""));
			}catch (Exception e) {
				// TODO: handle exception
				l.put("REASON",null);
				l.put("REMARK",null);
			}
		}
		
		
		log.info(ob + "\t" + demand);
		if (dlist.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("dlist", dlist);
			mav.addObject("ob", ob);
			mav.addObject("demand", demand);
		}
		return mav;
		

	}

	@GetMapping("/subWiseSplitDemand")
	public String getSubDivisionWiseSplitDemandPageShow() {
		return "subWiseSplitDemand";
	}

	@PostMapping("/subWiseDemand")
	public ModelAndView getSubDivisionWiseSplitDemandDetails(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("subWiseSplitDemand");
		List<Map<String, Object>> sectiondemand = reportDao.getSubDivisionWiseSplitDemandDetails(request);
		System.out.println(sectiondemand.isEmpty());
		if (sectiondemand.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("section", sectiondemand);
		}
		return mav;
	}

	@GetMapping("dailyCollection")
	public String showDailyCollectionReportPage() {
		return "dailyCollection";
	}

	@PostMapping("/daily")
	public ModelAndView getDailyCollectionDetails(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("dailyCollection");
		List<Map<String, Object>> daily = reportDao.getDailyCollectionDetails(request);
		System.out.println(daily.isEmpty());
		if (daily.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("daily", daily);
		}

		return mav;
	}

	@GetMapping("/LowVoltage")
	public String lowVoltagePageShow() {
		return "LowVoltage";
	}

	@PostMapping("/low")
	public ModelAndView getLowVoltageSurchargeDetails(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("LowVoltage");
		List<Map<String, Object>> low = reportDao.getLowVoltageSurchargeDetails(request);
		if (low.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("low", low);
		}
		return mav;
	}

	@GetMapping("/ServiceWiseEd")
	public String showServiceWiseEdPage() {
		return "ServiceWiseEd";
	}

	@PostMapping("/ed")
	public ModelAndView getServiceWiseEDDetails(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("ServiceWiseEd");
		List<Map<String, Object>> ed = reportDao.getServiceWiseEDDetails(request);
		if (ed.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("ed", ed);
		}
		return mav;
	}

	@GetMapping("/amr")
	public String showAmrPage() {
		return "amr";
	}

	@PostMapping("/amr")
	public ModelAndView getAmrDetails(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("amr");
		List<Map<String, Object>> amr = reportDao.getAmrDetails(request);
		if (amr.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("amr", amr);
		}
		return mav;
	}

	@GetMapping("/LedgerFreez")
	public String showLedgerFreezPage() {
		List<Map<String, Object>> amr = reportDao.getLedgerFreezDetails();
		return null;
	}

	@ResponseBody
	@PostMapping("/getcodes")
	public String getMeterStatus() {
		Map<String, String> map = reportDao.getMasterCodes();
		Gson gson = new Gson();
		String json = gson.toJson(map);
		return json;
	}

	@GetMapping("/totbillstop")
	public String showTotalBillstopServices() {
		return "totbillstop";

	}

	@PostMapping("/totbillstop")
	public ModelAndView getTotalBilstopServices(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		ModelAndView mav = new ModelAndView("totbillstop");
		List<Map<String, Object>> bstop = reportDao.getTotalBillstopServices(request);
		if (bstop.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("bstop", bstop);
		}
		return mav;
	}

	@GetMapping("/udc")
	public String showUDCPage() {
		return "udc";
	}

	@PostMapping("/udc")
	public ModelAndView getUdcDetails(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("udc");
		List<Map<String, Object>> nill = reportDao.getUDCDetails(request);
		System.out.println(nill);
		if (nill.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("nill", nill);
		}
		return mav;

	}

	@GetMapping("/Govt")
	public String showGovt_page() {
		return "Govt";
	}

	@PostMapping("/Govt")
	public ModelAndView getGovt_Details(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("Govt");
		List<Map<String, Object>> govt = reportDao.getGovtServices(request);
		if (govt.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("govt", govt);
		}
		return mav;

	}

	@GetMapping("/feedercon")
	public String showFeederPage() {
		return "feedercon";
	}

	@PostMapping("/feedercon")
	public ModelAndView getFeederCodeDetails(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("feedercon");
		List<Map<String, Object>> feeder = reportDao.getFeederConsumptionDetails(request);
		if (feeder.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("feeder", feeder);
		}
		return mav;

	}

	/* ======================= */

	@GetMapping("/bmdexceed")
	public String bmdPageshow() {
		return "bmdexceed";
	}

	@PostMapping("/bmdexceed")
	public ModelAndView getbmdExceedDetaisl(HttpServletRequest request) {
		String monthYear = request.getParameter("month") + "-" + request.getParameter("year");
		ModelAndView mav = new ModelAndView("bmdexceed");
		List<Map<String, Object>> bmd = reportDao.getbmdDetails(request);
		if (bmd.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("bmd", bmd);
			mav.addObject("mon_year", monthYear);
		}
		return mav;
	}
	
	@GetMapping("/metersreport")
	public ModelAndView metersreport() {
		ModelAndView mav = new ModelAndView("metersreport");
		
		List<Map<String, Object>> bmd = reportDao.getExistingMeterDetails("ALL");
		if (bmd.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("bmd", bmd);
		}
		return mav;		
	}

	@GetMapping("/masterreport")
	public ModelAndView masterreport(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("masterreport");
		
		List<Map<String, Object>> bmd = reportDao.getExistingMasterDetails( request);
		if (bmd.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("bmd", bmd);
		}
		return mav;		
	}
	@PostMapping("/masterreport")
	public ModelAndView getDetails(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("masterreport");
		
		List<Map<String, Object>> bmd = reportDao.getExistingMasterDetails( request);
		if (bmd.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("bmd", bmd);
		}
		return mav;		
	}
	
	@PostMapping("/metersreport")
	public ModelAndView metersreport(HttpServletRequest request) {

		ModelAndView mav = new ModelAndView("metersreport");
		String circle = request.getParameter("circle");
		List<Map<String, Object>> bmd = reportDao.getExistingMeterDetails(circle);
		if (bmd.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("bmd", bmd);
		}
		return mav;
	}
	

	@GetMapping("/bmddivision")
	public ModelAndView getbmdExceedDetaislDivision(HttpServletRequest request,
			@RequestParam(name = "cir") String cirleName, @RequestParam(name = "mon_year") String mon_year) {
		ModelAndView mav = new ModelAndView("bmdexceed");
		System.out.println(cirleName + "" + mon_year);
		List<Map<String, Object>> division = reportDao.getbmdDetailsDivision(cirleName, mon_year);
		if (division.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("division", division);
			mav.addObject("mon_year", mon_year);
			mav.addObject("circle", cirleName);
		}
		return mav;
	}

	@GetMapping("/scscircle")
	public ModelAndView getbmdExceedDetaislDivisionServices(HttpServletRequest request,
			@RequestParam(name = "cir") String cirleName, @RequestParam(name = "mon_year") String mon_year) {
		ModelAndView mav = new ModelAndView("bmdexceed");
		System.out.println(cirleName + "" + mon_year);
		List<Map<String, Object>> divisionServices = reportDao.getbmdDetailsDivisionServices(cirleName, mon_year);
		if (divisionServices.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("divscs", divisionServices);
			mav.addObject("mon_year", mon_year);
			mav.addObject("circle", cirleName);
		}
		return mav;
	}

	@GetMapping("/bmdsubdivision")
	public ModelAndView getbmdExceedDetaislSubDivision(HttpServletRequest request,
			@RequestParam(name = "cir") String cirleName, @RequestParam(name = "mon_year") String mon_year,
			@RequestParam(name = "div") String division) {
		ModelAndView mav = new ModelAndView("bmdexceed");
		List<Map<String, Object>> subdivision = reportDao.getbmdDetailsSubDivision(cirleName, mon_year, division);
		if (subdivision.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("subdivision", subdivision);
			mav.addObject("mon_year", mon_year);
			mav.addObject("circle", cirleName);
			mav.addObject("divisionName", division);
		}
		return mav;
	}

	@GetMapping("/scsdivision")
	public ModelAndView getbmdExceedDetaislSubDivisionServices(HttpServletRequest request,
			@RequestParam(name = "cir") String cirleName, @RequestParam(name = "mon_year") String mon_year,
			@RequestParam(name = "div") String division) {
		ModelAndView mav = new ModelAndView("bmdexceed");
		List<Map<String, Object>> scsdivision = reportDao.getbmdDetailsSubDivisionServices(cirleName, mon_year,
				division);
		if (scsdivision.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("scsdivision", scsdivision);
			mav.addObject("mon_year", mon_year);
			mav.addObject("circle", cirleName);
			mav.addObject("divisionName", division);
		}
		return mav;
	}

	@GetMapping("/bmdsection")
	public ModelAndView getbmdExceedDetaislSetionDetails(HttpServletRequest request,
			@RequestParam(name = "cir") String cirleName, @RequestParam(name = "mon_year") String mon_year,
			@RequestParam(name = "div") String division, @RequestParam(name = "sub") String subdivision) {
		ModelAndView mav = new ModelAndView("bmdexceed");
		List<Map<String, Object>> section = reportDao.getbmdDetailsSectionDivision(cirleName, mon_year, division,
				subdivision);
		if (subdivision.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("section", section);
			mav.addObject("sub", subdivision);
			mav.addObject("mon_year", mon_year);
			mav.addObject("circle", cirleName);
			mav.addObject("divisionName", division);
		}
		return mav;
	}

	@GetMapping("/scssubdivision")
	public ModelAndView getbmdExceedDetaislSetionDetailsServices(HttpServletRequest request,
			@RequestParam(name = "cir") String cirleName, @RequestParam(name = "mon_year") String mon_year,
			@RequestParam(name = "div") String division, @RequestParam(name = "sub") String subdivision) {
		ModelAndView mav = new ModelAndView("bmdexceed");
		List<Map<String, Object>> section = reportDao.getbmdDetailsSectionDivisionServices(cirleName, mon_year,
				division, subdivision);
		if (subdivision.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("scssub", section);
		}
		return mav;
	}

	@GetMapping("/allcircle")
	public ModelAndView allCirclebmd(@RequestParam(name = "mon_year") String mon_year) {
		ModelAndView mav = new ModelAndView("bmdexceed");
		List<Map<String, Object>> allcircle = reportDao.getallbmdServices(mon_year);
		if (allcircle.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("allcircle", allcircle);
		}
		return mav;

	}

	@GetMapping("/alldivision")
	public ModelAndView alldivisionbmd(@RequestParam(name = "mon_year") String mon_year,
			@RequestParam(name = "cir") String circleName) {
		ModelAndView mav = new ModelAndView("bmdexceed");
		List<Map<String, Object>> allcircle = reportDao.getallDivisionbmdServices(mon_year, circleName);
		if (allcircle.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("allcircle", allcircle);
		}
		return mav;

	}

	@GetMapping("/allsubdivision")
	public ModelAndView allSubdivisionbmd(@RequestParam(name = "div") String division,
			@RequestParam(name = "mon_year") String mon_year, @RequestParam(name = "cir") String circleName) {
		ModelAndView mav = new ModelAndView("bmdexceed");
		List<Map<String, Object>> allcircle = reportDao.getallSubDivisionbmdServices(division, mon_year, circleName);
		if (allcircle.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("allcircle", allcircle);
		}
		return mav;

	}

	@GetMapping("/allsections")
	public ModelAndView callSectionsbmd(@RequestParam(name = "div") String division,
			@RequestParam(name = "mon_year") String mon_year, @RequestParam(name = "cir") String circleName,
			@RequestParam(name = "sub") String sub) {
		ModelAndView mav = new ModelAndView("bmdexceed");
		List<Map<String, Object>> allcircle = reportDao.getallSectionDivisionbmdServices(division, mon_year, circleName,
				sub);
		if (allcircle.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("allcircle", allcircle);
		}
		return mav;

	}

	/* ======================== */

	/* spdcl started here */

	@GetMapping("/cbmdexceed")
	public String cbmdPageshow() {
		return "cbmdexceed";
	}

	@PostMapping("/cbmdexceed")
	public ModelAndView getcbmdExceedDetaisl(HttpServletRequest request) {
		String monthYear = request.getParameter("month") + "-" + request.getParameter("year");
		ModelAndView mav = new ModelAndView("cbmdexceed");
		List<Map<String, Object>> bmd = reportDao.getcbmdDetails(request);
		if (bmd.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("bmd", bmd);
			mav.addObject("mon_year", monthYear);
		}
		return mav;
	}

	@GetMapping("/cbmddivision")
	public ModelAndView getcbmdExceedDetaislDivision(HttpServletRequest request,
			@RequestParam(name = "cir") String cirleName, @RequestParam(name = "mon_year") String mon_year) {
		ModelAndView mav = new ModelAndView("cbmdexceed");
		System.out.println(cirleName + "" + mon_year);
		List<Map<String, Object>> division = reportDao.getcbmdDetailsDivision(cirleName, mon_year);
		if (division.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("division", division);
			mav.addObject("mon_year", mon_year);
			mav.addObject("circle", cirleName);
		}
		return mav;
	}

	@GetMapping("/cscscircle")
	public ModelAndView getcbmdExceedDetaislDivisionServices(HttpServletRequest request,
			@RequestParam(name = "cir") String cirleName, @RequestParam(name = "mon_year") String mon_year) {
		ModelAndView mav = new ModelAndView("cbmdexceed");
		System.out.println(cirleName + "" + mon_year);
		List<Map<String, Object>> divisionServices = reportDao.getcbmdDetailsDivisionServices(cirleName, mon_year);
		if (divisionServices.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("divscs", divisionServices);
			mav.addObject("mon_year", mon_year);
			mav.addObject("circle", cirleName);
		}
		return mav;
	}

	@GetMapping("/cbmdsubdivision")
	public ModelAndView getcbmdExceedDetaislSubDivision(HttpServletRequest request,
			@RequestParam(name = "cir") String cirleName, @RequestParam(name = "mon_year") String mon_year,
			@RequestParam(name = "div") String division) {
		ModelAndView mav = new ModelAndView("cbmdexceed");
		List<Map<String, Object>> subdivision = reportDao.getcbmdDetailsSubDivision(cirleName, mon_year, division);
		if (subdivision.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("subdivision", subdivision);
			mav.addObject("mon_year", mon_year);
			mav.addObject("circle", cirleName);
			mav.addObject("divisionName", division);
		}
		return mav;
	}

	@GetMapping("/cscsdivision")
	public ModelAndView getcbmdExceedDetaislSubDivisionServices(HttpServletRequest request,
			@RequestParam(name = "cir") String cirleName, @RequestParam(name = "mon_year") String mon_year,
			@RequestParam(name = "div") String division) {
		ModelAndView mav = new ModelAndView("cbmdexceed");
		List<Map<String, Object>> scsdivision = reportDao.getcbmdDetailsSubDivisionServices(cirleName, mon_year,
				division);
		if (scsdivision.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("scsdivision", scsdivision);
			mav.addObject("mon_year", mon_year);
			mav.addObject("circle", cirleName);
			mav.addObject("divisionName", division);
		}
		return mav;
	}

	@GetMapping("/cbmdsection")
	public ModelAndView getcbmdExceedDetaislSetionDetails(HttpServletRequest request,
			@RequestParam(name = "cir") String cirleName, @RequestParam(name = "mon_year") String mon_year,
			@RequestParam(name = "div") String division, @RequestParam(name = "sub") String subdivision) {
		ModelAndView mav = new ModelAndView("cbmdexceed");
		List<Map<String, Object>> section = reportDao.getcbmdDetailsSectionDivision(cirleName, mon_year, division,
				subdivision);
		if (subdivision.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("section", section);
			mav.addObject("sub", subdivision);
			mav.addObject("mon_year", mon_year);
			mav.addObject("circle", cirleName);
			mav.addObject("divisionName", division);
		}
		return mav;
	}

	@GetMapping("/cscssubdivision")
	public ModelAndView getcbmdExceedDetaislSetionDetailsServices(HttpServletRequest request,
			@RequestParam(name = "cir") String cirleName, @RequestParam(name = "mon_year") String mon_year,
			@RequestParam(name = "div") String division, @RequestParam(name = "sub") String subdivision) {
		ModelAndView mav = new ModelAndView("cbmdexceed");
		List<Map<String, Object>> section = reportDao.getcbmdDetailsSectionDivisionServices(cirleName, mon_year,
				division, subdivision);
		if (subdivision.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("scssub", section);
		}
		return mav;
	}

	@GetMapping("/callcircle")
	public ModelAndView allCirclecbmd(@RequestParam(name = "mon_year") String mon_year) {
		ModelAndView mav = new ModelAndView("cbmdexceed");
		List<Map<String, Object>> allcircle = reportDao.getcallbmdServices(mon_year);
		if (allcircle.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("allcircle", allcircle);
		}
		return mav;

	}

	@GetMapping("/calldivision")
	public ModelAndView allcdivisionbmd(@RequestParam(name = "mon_year") String mon_year,
			@RequestParam(name = "cir") String circleName) {
		ModelAndView mav = new ModelAndView("cbmdexceed");
		List<Map<String, Object>> allcircle = reportDao.getcallDivisionbmdServices(mon_year, circleName);
		if (allcircle.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("allcircle", allcircle);
		}
		return mav;

	}

	@GetMapping("/callsubdivision")
	public ModelAndView callSubdivisionbmd(@RequestParam(name = "div") String division,
			@RequestParam(name = "mon_year") String mon_year, @RequestParam(name = "cir") String circleName) {
		ModelAndView mav = new ModelAndView("cbmdexceed");
		List<Map<String, Object>> allcircle = reportDao.getcallSubDivisionbmdServices(division, mon_year, circleName);
		if (allcircle.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("allcircle", allcircle);
		}
		return mav;

	}

	@GetMapping("/callsections")
	public ModelAndView allSectionsbmd(@RequestParam(name = "div") String division,
			@RequestParam(name = "mon_year") String mon_year, @RequestParam(name = "cir") String circleName,
			@RequestParam(name = "sub") String sub) {
		ModelAndView mav = new ModelAndView("cbmdexceed");
		List<Map<String, Object>> allcircle = reportDao.getcallSectionDivisionbmdServices(division, mon_year,
				circleName, sub);
		if (allcircle.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("allcircle", allcircle);
		}
		return mav;

	}

	@GetMapping("/metermf")
	public String showMeterChangeForm() {
		return "metermf";
	}

	@PostMapping("/metermf")
	public ModelAndView getMeterAndMfChangeReportDetails(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("metermf");
		List<Map<String, Object>> meter = reportDao.getMeterAndMfChangeReportDetails(request);
		if (meter.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("meter", meter);
		}
		return mav;
	}

	@GetMapping("/mtrnodetails")
	public String showConsumerDetailsPage() {
		return "mtrnodetails";
	}

	@PostMapping("/mtrnodetails")
	@ResponseBody
	public String getConsumerDetailsPage(HttpServletRequest request) {
		String type = request.getParameter("con");
		log.info(type);
		List<Map<String, Object>> meter = reportDao.getConsumerDetails(request, type);
		if (meter.isEmpty()) {
			return "Fail";
		} else {
			String htmlRespone = "<div class='card'><div class='card-body row-no-padding table-responsive-sm dataTables_wrapper'><table class='table card-table table-vcenter text-nowrap datatable display' style='width: 100%;'> <thead> <tr> <th>SCNO</th> <th>NAME</th> <th>CAT</th> <th>CMD</th> <th>METER NO</th> <th>SUPPLY DATE</th> <th>STATUS</th> <th>MF</th> </tr> </thead> <tbody>";
			for (Map<String, Object> object : meter) {
				htmlRespone += "<tr><td>" + object.get("MSCNO") + "</td><td>" + object.get("CTNAME") + "</td><td>"
						+ object.get("CTCAT") + "</td><td>" + object.get("CTCMD_HT") + "</td><td>"
						+ object.get("MDMTRNO") + "</td><td>" + object.get("CTSUPCONDT") + "</td><td>"
						+ object.get("CTSTATUS") + "</td><td>" + object.get("MDMF_HT") + "</td>";
			}
			htmlRespone += "<tbody></table></div></div></html>";
			return htmlRespone;
		}
	}

	@GetMapping("/threebmd")
	public String showThreeBmdPage() {
		return "threebmd";
	}

	@PostMapping("/threebmd")
	public ModelAndView getThreemontsbmd(HttpServletRequest request) {
		String monthYear = request.getParameter("month") + "-" + request.getParameter("year");
		ModelAndView mav = new ModelAndView("threebmd");
		List<Map<String, Object>> circle = reportDao.threebmd(request);
		if (circle.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("circle", circle);
			mav.addObject("mon_year", monthYear);
		}
		return mav;
	}

	@GetMapping("/threecircleall")
	public ModelAndView getThreemonthsBmdList(@RequestParam(name = "cir") String circle,
			@RequestParam(name = "mon_year") String mon_year, @RequestParam(name = "type") String type,
			@RequestParam(name = "div") String division, @RequestParam(name = "sub") String sub) {
		ModelAndView mav = new ModelAndView("threebmd");
		if (type.equalsIgnoreCase("cir")) {
			List<Map<String, Object>> getDivisions = reportDao.threebmddivisions(circle, mon_year);
			if (getDivisions.isEmpty()) {
				mav.addObject("fail", "NO DATA FOUND");
			} else {
				mav.addObject("division", getDivisions);
				mav.addObject("mon_year", mon_year);
				mav.addObject("circlereturn", circle);
			}
		} else if (type.equalsIgnoreCase("div")) {
			List<Map<String, Object>> getSubDivisions = reportDao.threebmdSubdivisions(circle, mon_year, division);
			if (getSubDivisions.isEmpty()) {
				mav.addObject("fail", "NO DATA FOUND");
			} else {
				mav.addObject("subdivision", getSubDivisions);
				mav.addObject("mon_year", mon_year);
				mav.addObject("divisionreturn", division);
				mav.addObject("circlereturn", circle);
			}
		} else if (type.equalsIgnoreCase("sub")) {
			List<Map<String, Object>> getsection = reportDao.threebmdSection(circle, mon_year, division, sub);
			if (getsection.isEmpty()) {
				mav.addObject("fail", "NO DATA FOUND");
			} else {
				mav.addObject("section", getsection);
				mav.addObject("divisionreturn", division);
				mav.addObject("circlereturn", circle);
				mav.addObject("subreturn", sub);
				mav.addObject("mon_year", mon_year);
			}
		}
		return mav;

	}
	
	@GetMapping("/CategoryWiseDivisionWiseDemandReport")
	public ModelAndView getCategoryWiseDivisionWiseDemandReport(@RequestParam(name = "cir") String circle,
			@RequestParam(name = "mon_year") String mon_year, @RequestParam(name = "type") String type,
			@RequestParam(name = "div") String division, @RequestParam(name = "sub") String sub) {

		ModelAndView mav = new ModelAndView("CategoryWiseDivisionWiseDemandReport");
		List<Map<String, Object>> tp_sales = reportDao.getCategoryWiseDivisionWiseDemandReport(circle,mon_year);
		
		if (tp_sales.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("tp", tp_sales);
			mav.addObject("CIRCOUNT",countFrequencies(tp_sales));
			mav.addObject("TYPECOUNT",countFrequencies(tp_sales));
			mav.addObject("title",circle + "- Category Wise Division Wise Demand Abstract For "+mon_year);
			mav.addObject("status", "");
			mav.addObject("mon", mon_year);
		}
		return mav;

	}


	@GetMapping("/CategoryWiseSubDivisionWiseDemandReport")
	public ModelAndView getCategoryWiseSubDivisionWiseDemandReport(@RequestParam(name = "cir") String circle,
			@RequestParam(name = "mon_year") String mon_year, @RequestParam(name = "type") String type,
			@RequestParam(name = "div") String division, @RequestParam(name = "sub") String sub) {

		ModelAndView mav = new ModelAndView("CategoryWiseSubDivisionWiseDemandReport");
		List<Map<String, Object>> tp_sales = reportDao.getCategoryWiseSubDivisionWiseDemandReport(circle,mon_year);
		
		if (tp_sales.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("tp", tp_sales);
			mav.addObject("CIRCOUNT",countFrequencies(tp_sales));
			mav.addObject("TYPECOUNT",countFrequencies(tp_sales));
			mav.addObject("title",circle + "- Category Wise Sub Division Wise Demand Abstract For "+mon_year);
			mav.addObject("status", "");
		}
		return mav;

	}
	
	@GetMapping("/allthreebmd")
	public ModelAndView getallthreebmdscs(@RequestParam(name = "cir") String circle,
			@RequestParam(name = "mon_year") String mon_year, @RequestParam(name = "type") String type,
			@RequestParam(name = "div") String division, @RequestParam(name = "sub") String sub) {
		String mon[] = mon_year.split("-");
		ModelAndView mav = new ModelAndView("threebmd");

		YearMonth thisMonth = YearMonth.of(Integer.parseInt(mon[1]), getMonth(mon[0]));
		YearMonth lastMonth = thisMonth.minusMonths(1);
		YearMonth twoMonthsAgo = thisMonth.minusMonths(2);
		DateTimeFormatter monthYearFormatter = DateTimeFormatter.ofPattern("MMM");
		String preset = thisMonth.format(monthYearFormatter).toString().toUpperCase();
		String previous = lastMonth.format(monthYearFormatter).toString().toUpperCase();
		String oneprevious = twoMonthsAgo.format(monthYearFormatter).toString().toUpperCase();

		List<Map<String, Object>> allscs = reportDao.allthreebmdscsall(circle, division, sub, mon_year, type);
		if (allscs.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("scs", allscs);
			mav.addObject("p", preset);
			mav.addObject("pre", previous);
			mav.addObject("opre", oneprevious);
			mav.addObject("year", mon[1]);
		}
		return mav;
	}

	@GetMapping("/threescsall")
	public ModelAndView getThreemonthsBmdListSCS(@RequestParam(name = "cir") String circle,
			@RequestParam(name = "mon_year") String mon_year, @RequestParam(name = "type") String type,
			@RequestParam(name = "div") String division, @RequestParam(name = "sub") String sub,
			@RequestParam(name = "sec") String section) {
		ModelAndView mav = new ModelAndView("threebmd");
		String mon[] = mon_year.split("-");
		YearMonth thisMonth = YearMonth.of(Integer.parseInt(mon[1]), getMonth(mon[0]));
		YearMonth lastMonth = thisMonth.minusMonths(1);
		YearMonth twoMonthsAgo = thisMonth.minusMonths(2);
		DateTimeFormatter monthYearFormatter = DateTimeFormatter.ofPattern("MMM");
		String preset = thisMonth.format(monthYearFormatter).toString().toUpperCase();
		String previous = lastMonth.format(monthYearFormatter).toString().toUpperCase();
		String oneprevious = twoMonthsAgo.format(monthYearFormatter).toString().toUpperCase();

		List<Map<String, Object>> allscs = reportDao.allthreebmdscs(circle, division, sub, type, mon_year, section);
		if (allscs.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("scs", allscs);
			mav.addObject("p", preset);
			mav.addObject("pre", previous);
			mav.addObject("opre", oneprevious);
			mav.addObject("year", mon[1]);

		}
		return mav;
	}

	public static int getMonth(String unitCode) {
		HashMap<String, Integer> monthMap = new HashMap<String, Integer>();
		monthMap.put("JAN", 1);
		monthMap.put("FEB", 2);
		monthMap.put("MAR", 3);
		monthMap.put("APR", 4);
		monthMap.put("MAY", 5);
		monthMap.put("JUN", 6);
		monthMap.put("JUL", 7);
		monthMap.put("AUG", 8);
		monthMap.put("SEP", 9);
		monthMap.put("OCT", 10);
		monthMap.put("NOV", 11);
		monthMap.put("DEC", 12);
		return monthMap.get(unitCode);
	}

	@GetMapping("/acddlist")
	public String showacdDlistPage() {
		return "acddlist";
	}

	@PostMapping("/acddlist")
	public ModelAndView getACDDlistReport(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("acddlist");
		List<Map<String, Object>> dlist = reportDao.getAcdDlistReport(request);
		if (dlist.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("dlist", dlist);
		}
		return mav;
	}

	@GetMapping("/acdnoticeabstract")
	public String showAcdNoticeReportPage() {
		return "acdnoticeabstract";
	}

	@PostMapping("/acdnoticeabstract")
	public ModelAndView getACDNoticeDetails(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("acdnoticeabstract");
		String circle = request.getParameter("circle");
		String monthYear = request.getParameter("month") + "-" + request.getParameter("year");
		if (circle.equalsIgnoreCase("CPDCL") || circle.equalsIgnoreCase("SPDCL")) {
			List<Map<String, Object>> dcl = reportDao.getAcdNoticeReport(request);
			if (dcl.isEmpty()) {
				mav.addObject("fail", "NO DATA FOUND");
			} else {
				mav.addObject("dcl", dcl);
				mav.addObject("mon_year", monthYear);
				mav.addObject("stype", circle);
			}
			return mav;
		} else {
			List<Map<String, Object>> dlist = reportDao.getAcdNoticeReport(request);
			if (dlist.isEmpty()) {
				mav.addObject("fail", "NO DATA FOUND");
			} else {
				mav.addObject("dlist", dlist);
			}
			return mav;
		}
	}

	@GetMapping("/allcircles")
	public ModelAndView getAllCircleAbstractDetails(HttpServletRequest request) {
		String type = request.getParameter("type");
		String circle = request.getParameter("cir");
		String division = request.getParameter("div");
		String month_year = request.getParameter("mon_year");
		String subdivision = request.getParameter("sub");
		String section = request.getParameter("sec");
		ModelAndView mav = new ModelAndView("acdnoticeabstract");
		if (type.equalsIgnoreCase("div")) {
			List<Map<String, Object>> di = reportDao.getAllCirclesACDAbstract(type, circle, division, month_year,
					subdivision, section);
			if (di.isEmpty()) {
				mav.addObject("fail", "NO DATA FOUND");
			} else {
				mav.addObject("di", di);
				mav.addObject("circle", circle);
				mav.addObject("division", division);
				mav.addObject("subdivision", subdivision);
				mav.addObject("section", section);
				mav.addObject("mon_year", month_year);
			}
		} else if (type.equalsIgnoreCase("sub")) {
			List<Map<String, Object>> sub = reportDao.getAllCirclesACDAbstract(type, circle, division, month_year,
					subdivision, section);
			if (sub.isEmpty()) {
				mav.addObject("fail", "NO DATA FOUND");
			} else {
				mav.addObject("sub", sub);
				mav.addObject("circle", circle);
				mav.addObject("division", division);
				mav.addObject("subdivision", subdivision);
				mav.addObject("section", section);
				mav.addObject("mon_year", month_year);
			}
		} else if (type.equalsIgnoreCase("sec")) {
			List<Map<String, Object>> sec = reportDao.getAllCirclesACDAbstract(type, circle, division, month_year,
					subdivision, section);
			if (sec.isEmpty()) {
				mav.addObject("fail", "NO DATA FOUND");
			} else {
				mav.addObject("sec", sec);
				mav.addObject("circle", circle);
				mav.addObject("division", division);
				mav.addObject("subdivision", subdivision);
				mav.addObject("section", section);
				mav.addObject("mon_year", month_year);
			}
		}
		return mav;
	}

	@GetMapping("/allservices")
	public ModelAndView getServiceWiseAcdAbstractDetails(HttpServletRequest request) {

		ModelAndView mav = new ModelAndView("acdnoticeabstract");
		List<Map<String, Object>> scs = reportDao.getAllCirclesACDAbstractServices(request);
		if (scs.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("dlist", scs);
		}
		return mav;
	}
	@GetMapping("/services")
	public ModelAndView getServices(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("services");
		String servtype = request.getParameter("servtype");
		String monyear = request.getParameter("monyear");
		String circle = request.getParameter("circle");
		String flag = request.getParameter("flag");
		String page = request.getParameter("page");
		String type = request.getParameter("type");
		List<Map<String, Object>> service = null;
		if(page.equals("SC")) {
			service = reportDao.getSCServices(servtype, monyear,circle,flag);
		}
		else if(page.equals("HC")){
			service = reportDao.getHCServices(servtype, monyear,circle,flag);
			
		}
		if (service.isEmpty()) {
			mav.addObject("error", "NO DATA FOUND");
		} else {
			mav.addObject("pay", service);
			mav.addObject("title", type+" Services For "+(circle.equals("ALL")?"APCPDCL":circle)+","+monyear);
		}
		return mav;
	}
	
	
	@GetMapping("/alltotal")
	public ModelAndView getServiceWiseAcdAbstractDetailsTotal(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("acdnoticeabstract");
		List<Map<String, Object>> scs = reportDao.getAllCirclesACDAbstractServicesTotal(request);
		if (scs.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("dlist", scs);
		}
		return mav;
	}

	@GetMapping("/acdsurcharg")
	public String acdSurchargePageShow() {
		return "acdsurcharg";
	}

	@PostMapping("/acdsurcharg")
	public ModelAndView getSurchargeReportAcd(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("acdsurcharg");
		List<Map<String, Object>> acdsur = reportDao.getAcdSurcharAndBalanceRepot(request);
		if (acdsur.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("acdsur", acdsur);
		}
		return mav;

	}

	@GetMapping("/dlistabstract")
	public String showDlistAbstractPage() {
		return "dlistabstract";
	}

	LocalDate currentDate = LocalDate.now();
	Month m = currentDate.getMonth();
	int y = currentDate.getYear();
	String month_year = m.toString() + "-" + String.valueOf(y);
	String types;

	@PostMapping("/dlistabstract")
	public ModelAndView getDlistDetails(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("dlistabstract");
		System.out.println(month_year);
		List<Map<String, Object>> dcl = reportDao.getDlistAbstractReport(request);
		if (dcl.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("dcl", dcl);
			mav.addObject("stype", request.getParameter("circle"));
			types = request.getParameter("circle");
			mav.addObject("month", month_year);
		}
		return mav;
	}

	@GetMapping("/dlistscs")
	public ModelAndView getDlistReportServices(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("dlistabstract");
		List<Map<String, Object>> dlist = reportDao.getDlistReportServices(request);
		if (dlist.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("dlist", dlist);
			mav.addObject("stype", types);
			mav.addObject("month", month_year);
		}
		return mav;
	}

	@GetMapping("/allcirclesdlist")
	public ModelAndView getAllCircleAbstractDetailsDlist(HttpServletRequest request) {
		String type = request.getParameter("type");
		String circle = request.getParameter("cir");
		String division = request.getParameter("div");
		String subdivision = request.getParameter("sub");
		String section = request.getParameter("sec");
		ModelAndView mav = new ModelAndView("dlistabstract");
		if (type.equalsIgnoreCase("div")) {
			List<Map<String, Object>> di = reportDao.getAllCirclesDlist(type, circle, division, month_year, subdivision,
					section);
			if (di.isEmpty()) {
				mav.addObject("fail", "NO DATA FOUND");
			} else {
				mav.addObject("di", di);
				mav.addObject("circle", circle);
				mav.addObject("division", division);
				mav.addObject("subdivision", subdivision);
				mav.addObject("section", section);
				mav.addObject("stype", types);
				mav.addObject("month", month_year);

			}
		} else if (type.equalsIgnoreCase("sub")) {
			List<Map<String, Object>> sub = reportDao.getAllCirclesDlist(type, circle, division, month_year,
					subdivision, section);
			if (sub.isEmpty()) {
				mav.addObject("fail", "NO DATA FOUND");
			} else {
				mav.addObject("sub", sub);
				mav.addObject("circle", circle);
				mav.addObject("division", division);
				mav.addObject("subdivision", subdivision);
				mav.addObject("section", section);
				mav.addObject("stype", types);
				mav.addObject("month", month_year);
			}
		} else if (type.equalsIgnoreCase("sec")) {
			List<Map<String, Object>> sec = reportDao.getAllCirclesDlist(type, circle, division, month_year,
					subdivision, section);
			if (sec.isEmpty()) {
				mav.addObject("fail", "NO DATA FOUND");
			} else {
				mav.addObject("sec", sec);
				mav.addObject("circle", circle);
				mav.addObject("division", division);
				mav.addObject("subdivision", subdivision);
				mav.addObject("section", section);
				mav.addObject("stype", types);
				mav.addObject("month", month_year);
			}
		}
		return mav;
	}

	@GetMapping("/serviceswise")
	public ModelAndView getServiceWiseDlistDetails(HttpServletRequest request) {

		ModelAndView mav = new ModelAndView("dlistabstract");
		List<Map<String, Object>> scs = reportDao.getAllCirclesDlistServices(request);
		if (scs.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("dlist", scs);
			mav.addObject("stype", types);
			mav.addObject("month", month_year);
		}
		return mav;
	}

	@GetMapping("/billanalysis")
	public String billanalysisPage() {
		return "billanalysis";
	}

	@PostMapping("/billanalysis")
	public ModelAndView getBillAnalysisReport(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("billanalysis");
		List<Map<String, Object>> bill = reportDao.getBillAnalysisReport(request);
		if (bill.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("bill", bill);
		}
		return mav;
	}

	@GetMapping("/voltagedemand")
	public String showVoltagedemandPage() {
		return "voltagedemand";
	}

	@PostMapping("/voltagedemand")
	public ModelAndView getVoltagedemand(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("voltagedemand");
		List<Map<String, Object>> vol = reportDao.getVoltagedemand(request);
		List<Map<String, Object>> tp = reportDao.getTpDetails(request);

		if (vol.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("vol", vol);
			mav.addObject("tp", tp);
		}
		return mav;
	}
	@GetMapping("/fyvoltagedemand")
	public String showFYVoltagedemandPage() {
		return "voltagedemandfinancialyear";
	}
	@PostMapping("/fyvoltagedemand")
	public ModelAndView getFYVoltagedemand(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("voltagedemandfinancialyear");
		List<Map<String, Object>> vol = reportDao.getFYVoltagedemand(request);
		List<Map<String, Object>> tp = reportDao.getFYTpDetails(request);

		if (vol.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("vol", vol);
			mav.addObject("tp", tp);
		}
		return mav;
	}
	
	
	@GetMapping("/kwhmonthlyreport")
	public String kwhmonthlyreport() {
		return "kwhmonthlyreport";
	}
	@PostMapping("/kwhmonthlyreport")
	public ModelAndView kwhmonthlyreport(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("kwhmonthlyreport");
		List<Map<String, Object>> vol = reportDao.getKwhmonthlyreport(request);
		String circle = request.getParameter("circle");
		if (vol.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("vol", vol);
			mav.addObject("title", "Category-wise Monthly Electricity Consumption (KWH in MUs) For "+(circle.equals("ALL")?"APCPDCL":circle)+", "+request.getParameter("year"));
		}
		return mav;
	}
	
	
	
	@GetMapping("/demandsplit")
	public String demandSplitPage() {
		return "demandsplit";
	}

	@PostMapping("/demandsplit")
	public ModelAndView getdemandSplitDetails(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("demandsplit");
		List<Map<String, Object>> demand = reportDao.getdemandSplitDetails(request);

		if (demand.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("demand", demand);
		}
		return mav;
	}
	
	@GetMapping("/meterstatus")
	public String meterstatusPage() {
		return "meterstatus";
	}

	@PostMapping("/meterstatus")
	public ModelAndView getmeterstatusDetails(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("meterstatus");
		List<Map<String, Object>> demand = reportDao.getMeterStatus(request);

		if (demand.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("demand", demand);
		}
		return mav;
	}
	@GetMapping("/solarmeterstatus")
	public String SolarmeterstatusPage() {
		return "solarmeterstatus";
	}

	@PostMapping("/solarmeterstatus")
	public ModelAndView getSolarmeterstatusDetails(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("solarmeterstatus");
		List<Map<String, Object>> demand = reportDao.getSolarMeterStatus(request);

		if (demand.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("demand", demand);
		}
		return mav;
	}
	
	
	@GetMapping("/seasonaldemandsplit")
	public String seasonaldemandsplitPage() {
		return "seasonaldemandsplit";
	}

	@PostMapping("/seasonaldemandsplit")
	public ModelAndView seasonaldemandsplitDetails(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("seasonaldemandsplit");
		List<Map<String, Object>> demand = reportDao.getSeaDemandSplitDetails(request);

		if (demand.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("demand", demand);
		}
		return mav;
	}

	@GetMapping("/servicewisedemandsplit")
	public String servicewiseDemandSplitPage() {
		return "servicewisedemandsplit";
	}

	@PostMapping("/servicewisedemandsplit")
	public ModelAndView getServiceWiseDemandSplitDetails(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("servicewisedemandsplit");
		List<Map<String, Object>> demand = reportDao.getServiceWiseDemandSplitDetails(request);

		if (demand.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("demand", demand);
		}
		return mav;
	}
	
	@GetMapping("/trueupkvahhistory")
	public String trueupkvahhistory() {
		return "trueupkvahhistory";
	}

	@PostMapping("/trueupkvahhistory")
	public ModelAndView trueupkvahhistory(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("trueupkvahhistory");
		List<Map<String, Object>> demand = reportDao.getTrueUpKvahHistory(request);
		ConsumerDetails consumerdetails=reportDao.getConsumerDetails(request.getParameter("scno"));

		if (demand.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("demand", demand);
			mav.addObject("consumerdetails", consumerdetails);
		}
		return mav;
	}
	
	@GetMapping("/arrears")
	public String showArrersPage() {
		return "arrears";
	}

	@PostMapping("/arrears")
	public ModelAndView getArrearsDetails(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("arrears");
		List<Map<String, Object>> arrears = reportDao.getArrearsDetails(request);

		if (arrears.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("arrears", arrears);
		}
		return mav;
	}
	
	@GetMapping("/courtcases")
	public String showcourtcasesPage() {
		return "courtcases";
	}

	@PostMapping("/courtcases")
	public ModelAndView getcourtcasesDetails(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("courtcases");
		List<Map<String, Object>> arrears = reportDao.getCourtCaseDetails(request);

		if (arrears.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("arrears", arrears);
		}
		return mav;
	}
	

	@GetMapping("/dlistappeared")
	public String dlistappearedPage() {
		return "dlistappeared";
	}

	@PostMapping("/dlistappeared")
	public ModelAndView getDListAppearedList(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("dlistappeared");
		List<Map<String, Object>> dlist = reportDao.getDListAppearedList(request);

		if (dlist.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("live", dlist);
			mav.addObject("size", dlist.size());
		}
		return mav;
	}

	@GetMapping("/dlistDivision")
	public String showdlistDivisionPage() {
		return "dlistDivision";
	}

	@PostMapping("/dlistDivision")
	public ModelAndView getDlistDivision(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("dlistDivision");
		List<Map<String, Object>> dlist = reportDao.getDlistDivision(request);

		if (dlist.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("live", dlist);
			mav.addObject("size", dlist.size());
		}
		return mav;
	}

	@GetMapping("/unpaid")
	public String showUnPaidPage() {
		return "unpaid";
	}

	@PostMapping("/unpaid")
	public ModelAndView getUnPaidServices(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("unpaid");
		List<Map<String, Object>> dlist = reportDao.getUnpaidServices(request);

		if (dlist.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("live", dlist);
		}
		return mav;
	}

	@GetMapping("/acdbalancereport")
	public String showAcdBalanceReportPage() {
		return "acdbalancereport";
	}

	@PostMapping("/acdbalancereport")
	public ModelAndView getACDBalanceDetails(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("acdbalancereport");
		List<Map<String, Object>> acdbalacne = reportDao.getAcdBalanceReport(request);
		if (acdbalacne.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("acd", acdbalacne);
		}
		return mav;
	}

	@GetMapping("/acdbalanceabstract")
	public String showAcdBalanceAbstract() {
		return "acdbalanceabstract";
	}

	@PostMapping("/acdbalanceabstract")
	public ModelAndView getAcdBalanceAbstract(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("acdbalanceabstract");
		List<Map<String, Object>> acdbalacne = reportDao.getAcdBalanceAbstract(request);
		if (acdbalacne.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("acd", acdbalacne);
		}
		return mav;
	}

	@GetMapping("/cbledger")
	public String showServicesWiseLedgerClosingBalance() {
		return "cbledger";
	}

	@PostMapping("/cbledger")
	public ModelAndView getServicesWiseLedgerClosingBalance(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("cbledger");
		List<Map<String, Object>> acdbalacne = reportDao.getServicesWiseLedgerClosingBalance(request);
		if (acdbalacne.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("acd", acdbalacne);
		}
		return mav;
	}
	
	@GetMapping("/cmdca")
	public String showCMDCA() {
		return "cmdca";
	}

	@PostMapping("/cmdca")
	public ModelAndView getCMDCA(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("cmdca");
		List<Map<String, Object>> acdbalacne = reportDao.getCMDCA(request);
		String circle = request.getParameter("circle");
		String monthYear = request.getParameter("month") + "-" + request.getParameter("year");
		if (acdbalacne.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("acd", acdbalacne);
			mav.addObject("title","DCB Report For "+(circle.equals("ALL")?"APCPDCL":circle)+", "+monthYear);
		}
		return mav;
	}
	
	@GetMapping("/dcbabstract")
	public String showDCBAbstract() {
		return "dcbabstract";
	}

	@PostMapping("/dcbabstract")
	public ModelAndView getDCBAbstract(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("dcbabstract");
		List<Map<String, Object>> acdbalacne = reportDao.getDCBAbstract(request);
		String circle = request.getParameter("circle");
		String monthYear = request.getParameter("month") + "-" + request.getParameter("year");
		if (acdbalacne.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("acd", acdbalacne);
			mav.addObject("title","DCB Split Report For "+(circle.equals("ALL")?"APCPDCL":circle)+", "+monthYear);
		}
		return mav;
	}
	
	@GetMapping("/lgdcodes")
	public String showLGDCodes() {
		return "lgdcodes";
	}

	@PostMapping("/lgdcodes")
	public ModelAndView getLGDCodes(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("lgdcodes");
		List<Map<String, Object>> acdbalacne = reportDao.getLGDCodes(request);
		String circle = request.getParameter("circle");
		String monthYear = request.getParameter("month") + "-" + request.getParameter("year");
		if (acdbalacne.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("acd", acdbalacne);
			mav.addObject("title","LGD Report For "+(circle.equals("ALL")?"APCPDCL":circle)+", "+monthYear);
		}
		return mav;
	}
	
	
	@GetMapping("/lgdcodesservicewise")
	public String showlgdcodesservicewise() {
		return "lgdcodesservicewise";
	}

	@PostMapping("/lgdcodesservicewise")
	public ModelAndView getlgdcodesservicewise(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("lgdcodesservicewise");
		List<Map<String, Object>> acdbalacne = reportDao.getLGDServiceWiseCodes(request);
		String circle = request.getParameter("circle");
		String monthYear = request.getParameter("month") + "-" + request.getParameter("year");
		if (acdbalacne.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("acd", acdbalacne);
			mav.addObject("title","LGD Service Wise Report For "+(circle.equals("ALL")?"APCPDCL":circle)+", "+monthYear);
		}
		return mav;
	}
	
	@GetMapping("/hodreport")
	public String showHODReport() {
		return "hodreport";
	}

	@PostMapping("/hodreport")
	public ModelAndView getHODReport(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("hodreport");
		List<Map<String, Object>> acdbalacne = reportDao.getHODReport(request);
		String circle = request.getParameter("circle");
		String monthYear = request.getParameter("month") + "-" + request.getParameter("year");
		if (acdbalacne.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("acd", acdbalacne);
			mav.addObject("title","Department Wise, Service Wise DCB Report For "+(circle.equals("ALL")?"APCPDCL":circle)+", "+monthYear);
		}
		return mav;
	}
	@GetMapping("/hodabstract")
	public String showHODAbstract() {
		return "hodabstract";
	}

	@PostMapping("/hodabstract")
	public ModelAndView getHODAbstract(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("hodabstract");
		List<Map<String, Object>> acdbalacne = reportDao.getHODAbstract(request);
		String circle = request.getParameter("circle");
		String monthYear = request.getParameter("month") + "-" + request.getParameter("year");
		if (acdbalacne.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("acd", acdbalacne);
			mav.addObject("title","Department Wise, Section Wise DCB Abstract For "+(circle.equals("ALL")?"APCPDCL":circle)+", "+monthYear);
		}
		return mav;
	}
	@GetMapping("/hodwiseabstract")
	public String showHODWiseAbstract() {
		return "hodwiseabstract";
	}

	@PostMapping("/hodwiseabstract")
	public ModelAndView getHODWiseAbstract(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("hodwiseabstract");
		List<Map<String, Object>> acdbalacne = reportDao.getHODWiseAbstract(request);
		String circle = request.getParameter("circle");
		String monthYear = request.getParameter("month") + "-" + request.getParameter("year");
		if (acdbalacne.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("acd", acdbalacne);
			mav.addObject("title","Department Wise DCB Abstract For  "+monthYear);
		}
		return mav;
	}
	@GetMapping("/servicetypewiseabstract")
	public String showservicetypewiseabstract() {
		return "servicetypewiseabstract";
	}

	@PostMapping("/servicetypewiseabstract")
	public ModelAndView getservicetypewiseabstract(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("servicetypewiseabstract");
		List<Map<String, Object>> acdbalacne = reportDao.getSTWiseAbstract(request);
		String circle = request.getParameter("circle");
		String monthYear = request.getParameter("month") + "-" + request.getParameter("year");
		if (acdbalacne.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("acd", acdbalacne);
			mav.addObject("title","Service Type Wise DCB Abstract For  "+(circle.equals("ALL")?"APCPDCL":circle)+", "+monthYear);
		}
		return mav;
	}
	@GetMapping("/servicetypecatwiseabstract")
	public String showservicetypecatwiseabstract() {
		return "servicetypecatwiseabstract";
	}

	@PostMapping("/servicetypecatwiseabstract")
	public ModelAndView getservicetypecatwiseabstract(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("servicetypecatwiseabstract");
		List<Map<String, Object>> acdbalacne = reportDao.getSTCatWiseAbstract(request);
		String circle = request.getParameter("circle");
		String monthYear = request.getParameter("month") + "-" + request.getParameter("year");
		if (acdbalacne.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("acd", acdbalacne);
			mav.addObject("title","Service Type,Category Wise DCB Abstract For  "+(circle.equals("ALL")?"APCPDCL":circle)+", "+monthYear);
		}
		return mav;
	}
	
	@GetMapping("/servicetyperevenue")
	public ModelAndView servicetyperevenue() {
		ModelAndView mav = new ModelAndView("servicetyperevenue");
		mav.addObject("servicetype", reportDao.getServiceTypes());
		return mav;
	}

	@PostMapping("/servicetyperevenue")
	public ModelAndView servicetyperevenue(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("servicetyperevenue");
		String[] st = request.getParameterValues("servicetype");
		mav.addObject("servicetype", reportDao.getServiceTypes());
	
		List<Map<String, Object>> acdbalacne = reportDao.getFYSTWiseAbstract(request);
		String circle = request.getParameter("circle");
		String monthYear =  request.getParameter("year");
		if (acdbalacne.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
			
		} else {
			mav.addObject("acd", acdbalacne);
			mav.addObject("circle", circle);
			mav.addObject("fyear",request.getParameter("year"));
			mav.addObject("title","Service Type Wise DCB Abstract For  "+(circle.equals("ALL")?"APCPDCL":circle)+", "+monthYear);
		}
		return mav;
	}
	
	@GetMapping("/feederwiseabstract")
	public String feederwiseabstract() {
		return "feederwiseabstract";
	}

	@PostMapping("/feederwiseabstract")
	public ModelAndView feederwiseabstract(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("feederwiseabstract");
		List<Map<String, Object>> acdbalacne = reportDao.getFDWiseAbstract(request);
		String circle = request.getParameter("circle");
		String monthYear = request.getParameter("month") + "-" + request.getParameter("year");
		if (acdbalacne.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("acd", acdbalacne);
			mav.addObject("title","Feeder Wise DCB Abstract For  "+(circle.equals("ALL")?"APCPDCL":circle)+", "+monthYear);
		}
		return mav;
	}
	@GetMapping("/feederwisesubdivabstract")
	public String feederwisesubdivabstract() {
		return "feederwisesubdivabstract";
	}

	@PostMapping("/feederwisesubdivabstract")
	public ModelAndView feederwisesubdivabstract(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("feederwisesubdivabstract");
		List<Map<String, Object>> acdbalacne = reportDao.getFDWiseSubDivisionAbstract(request);
		String selectedLabel = request.getParameter("selectedLabel");
		String subdivision = request.getParameter("subdivision");
		String monthYear = request.getParameter("month") + "-" + request.getParameter("year");
		if (acdbalacne.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("acd", acdbalacne);
			mav.addObject("title","Feeder Wise , Sub Division Wise DCB Abstract For  "+ selectedLabel +", "+monthYear);
		}
		return mav;
	}
	
	@GetMapping("/msmewiseabstract")
	public String msmewiseabstract() {
		return "msmewiseabstract";
	}

	@PostMapping("/msmewiseabstract")
	public ModelAndView msmewiseabstract(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("msmewiseabstract");
		List<Map<String, Object>> acdbalacne = reportDao.getMSMEWiseAbstract(request);
		String circle = request.getParameter("circle");
		String monthYear = request.getParameter("month") + "-" + request.getParameter("year");
		if (acdbalacne.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("acd", acdbalacne);
			mav.addObject("title","MSME Wise DCB Abstract For  "+(circle.equals("ALL")?"APCPDCL":circle)+", "+monthYear);
		}
		return mav;
	}
	
	@GetMapping("/msmetypewisedcb")
	public String msmetypewisedcb() {
		return "msmetypewisedcb";
	}

	@PostMapping("/msmetypewisedcb")
	public ModelAndView msmetypewisedcb(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("msmetypewisedcb");
		List<Map<String, Object>> acdbalacne = reportDao.getMSMETYPEWiseAbstract(request);
		String circle = request.getParameter("circle");
		String monthYear = request.getParameter("month") + "-" + request.getParameter("year");
		if (acdbalacne.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("acd", acdbalacne);
			mav.addObject("title","MSME Type Wise DCB Abstract For  "+(circle.equals("ALL")?"APCPDCL":circle)+", "+monthYear);
		}
		return mav;
	}
	
	
	@GetMapping("/voltagewiseabstract")
	public String voltagewiseabstract() {
		return "voltagewiseabstract";
	}

	@PostMapping("/voltagewiseabstract")
	public ModelAndView voltagewiseabstract(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("voltagewiseabstract");
		List<Map<String, Object>> acdbalacne = reportDao.getVoltageWiseAbstract(request);
		String circle = request.getParameter("circle");
		String monthYear = request.getParameter("month") + "-" + request.getParameter("year");
		if (acdbalacne.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("acd", acdbalacne);
			mav.addObject("title","Voltage Wise DCB Abstract For  "+(circle.equals("ALL")?"APCPDCL":circle)+", "+monthYear);
		}
		return mav;
	}
	
	
	@GetMapping("/hoddemandsplit")
	public String showHODDemandSplit() {
		return "hoddemandsplit";
	}

	@PostMapping("/hoddemandsplit")
	public ModelAndView getHODDemandSplit(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("hoddemandsplit");
		List<Map<String, Object>> acdbalacne = reportDao.getHODDemandSplit(request);
		String circle = request.getParameter("circle");
		String monthYear = request.getParameter("month") + "-" + request.getParameter("year");
		if (acdbalacne.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("acd", acdbalacne);
			mav.addObject("title","Department Wise, Service Wise Demand Split For "+(circle.equals("ALL")?"APCPDCL":circle)+", "+monthYear);
		}
		return mav;
	}
	
	
	@GetMapping("/hoddemandabstract")
	public String showHODDemandAbstract() {
		return "hoddemandabstract";
	}

	@PostMapping("/hoddemandabstract")
	public ModelAndView getHODDemandAbstract(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("hoddemandabstract");
		List<Map<String, Object>> acdbalacne = reportDao.getHODDemandAbstract(request);
		String circle = request.getParameter("circle");
		String monthYear = request.getParameter("month") + "-" + request.getParameter("year");
		if (acdbalacne.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("acd", acdbalacne);
			mav.addObject("title","Department Wise, Service Wise Demand Abstract For "+(circle.equals("ALL")?"APCPDCL":circle)+", "+monthYear);
		}
		return mav;
	}
	
	@GetMapping("/cbledgerabs")
	public String showLedgerClosingBalanceAbstract() {
		return "cbledgerabstract";
	}
	
	@ResponseBody
	@PostMapping("/getServiceTypes")
	public String getServiceTypes() {
		LinkedHashMap<String, Object> map = reportDao.getServiceTypes();
		Gson gson = new Gson();
		String json = gson.toJson(map);
		return json;
	}
	
	@ResponseBody
	@PostMapping("/getFeeders/{circle}")
	public String getFeeders(@PathVariable("circle") String circle) {
		LinkedHashMap<String, Object> map = reportDao.getFeeders(circle);
		Gson gson = new Gson();
		String json = gson.toJson(map);
		return json;
	}
	@ResponseBody
	@PostMapping("/getSubDivFeeders/{subdivision}")
	public String getSubDivFeeders(@PathVariable("subdivision") String subdivision) {
		LinkedHashMap<String, Object> map = reportDao.getSubDivisonFeeders(subdivision);
		Gson gson = new Gson();
		String json = gson.toJson(map);
		return json;
	}
	@PostMapping("/cbledgerabs")
	public ModelAndView getServicesWiseLedgerClosingBalanceAbstract(HttpServletRequest request) {
		String circle = request.getParameter("circle");
		String monthYear = request.getParameter("month") + "-" + request.getParameter("year");
		log.info(circle+"-"+monthYear);
		ModelAndView mav = new ModelAndView("cbledgerabstract");
		List<Map<String, Object>> acdbalacne = reportDao.getLedgerWiseAbstract(request);
		if (acdbalacne.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("acd", acdbalacne);
			mav.addObject("title", "Status Wise DCB Report  For "+(circle.equals("ALL")?"APCPDCL ":circle)+"  the month of "+monthYear);
			mav.addObject("circle", circle);
			mav.addObject("mon", monthYear);
		}
		return mav;
	}
	@GetMapping("/divisionwisecbledgerabs")
	public ModelAndView getDivisionWiseLedgerAbstract(@RequestParam(name = "cir") String circle,
			@RequestParam(name = "mon_year") String monthYear) {

		ModelAndView mav = new ModelAndView("divisionwisecbledgerabstract");
		List<Map<String, Object>> acdbalacne = reportDao.getDivisionWiseLedgerWiseAbstract(circle,monthYear);
		if (acdbalacne.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("acd", acdbalacne);
			mav.addObject("title", "Ledger Closing Balance Abstract For "+(circle.equals("ALL")?"APCPDCL ":circle)+" , "+monthYear);
			mav.addObject("circle", circle);
			mav.addObject("mon", monthYear);
		}
		return mav;
	}
	@GetMapping("/subdivisionwisecbledgerabs")
	public ModelAndView getSubDivisionWiseLedgerAbstract(@RequestParam(name = "cir") String circle,
			@RequestParam(name = "mon_year") String monthYear) {

		ModelAndView mav = new ModelAndView("subdivisionwisecbledgerabstract");
		List<Map<String, Object>> acdbalacne = reportDao.getSubDivisionWiseLedgerWiseAbstract(circle,monthYear);
		if (acdbalacne.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("acd", acdbalacne);
			mav.addObject("title", "Ledger Closing Balance Abstract For "+(circle.equals("ALL")?"APCPDCL ":circle)+" , "+monthYear);
		}
		return mav;
	}
	
	@GetMapping("/energysales")
	public String energyAudiSalesPage() {
		return "energysales";
	}

	@PostMapping("/energysales")
	public ModelAndView getEnergyAuditSales(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("energysales");
		List<Map<String, Object>> daily = reportDao.getEnergyAuditSales(request);
		System.out.println(daily.isEmpty());
		if (daily.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("daily", daily);
		}

		return mav;
	}
	@GetMapping("/energysalesoa")
	public String energyAudiSalesOAPage() {
		return "energysalesoa";
	}

	@PostMapping("/energysalesoa")
	public ModelAndView getEnergyAuditSalesoa(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("energysalesoa");
		List<Map<String, Object>> daily = reportDao.getEnergyAuditSalesOA(request);
		System.out.println(daily.isEmpty());
		if (daily.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("daily", daily);
		}

		return mav;
	}
	@GetMapping("/energysalesoamw")
	public String energyAudiSalesOAMWPage() {
		return "energysalesoamw";
	}

	@PostMapping("/energysalesoamw")
	public ModelAndView getEnergyAuditSalesoamw(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("energysalesoamw");
		List<Map<String, Object>> daily = reportDao.getEnergyAuditSalesOAMW(request);
		System.out.println(daily.isEmpty());
		if (daily.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("daily", daily);
		}

		return mav;
	}
	

	@GetMapping("/apgpcl")
	public String showapgpclPage() {
		return "apgpcl";
	}

	@PostMapping("/apgpcl")
	public ModelAndView getApgpcl(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("apgpcl");
		String type = request.getParameter("circle");
		List<Map<String, Object>> ap = reportDao.getApgpcl(request);

		if (ap.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			if (type.equalsIgnoreCase("A")) {
				mav.addObject("ap", ap.stream().filter(a -> a.get("TYPE").toString().equalsIgnoreCase("APGPCL"))
						.collect(Collectors.toList()));
			} else if (type.equalsIgnoreCase("F")) {
				mav.addObject("ap", ap.stream().filter(a -> a.get("TYPE").toString().equalsIgnoreCase("FERRO"))
						.collect(Collectors.toList()));
			} else {
				mav.addObject("ap", ap);
			}
		}
		return mav;
	}
	public  Map<String,Integer>  countFrequencies(List<Map<String,Object>> list) 
	{ 
		Map<String,Integer> countmap= new HashMap<String,Integer>();
		List<String> templist = new ArrayList<String>();
		Iterator<Map<String,Object>> itr = list.iterator();
		while(itr.hasNext()) {
			Map<String,Object> tm = itr.next();
			for (Map.Entry<String, Object> pair : tm.entrySet()) {
				if(pair.getKey().equals("CIRNAME")) {
				templist.add(pair.getValue().toString());
				}
				if(pair.getKey().equals("CIRCLE")) {
					templist.add(pair.getValue().toString());
					}
				if(pair.getKey().equals("CIR_TYPE")) {
					templist.add(pair.getValue().toString());
					}
				if(pair.getKey().equals("ISCNO")) {
					templist.add(pair.getValue().toString());
					}
				if(pair.getKey().equals("LDT")) {
					templist.add(pair.getValue().toString());
					}
				if(pair.getKey().equals("BDT")) {
					templist.add(pair.getValue().toString());
					}
				if(pair.getKey().equals("TYPE")) {
					templist.add(pair.getValue().toString());
					}
				if(pair.getKey().equals("DIV_TYPE")) {
					templist.add(pair.getValue().toString());
					}
				
				
			}
		}
		
	    Set<String> st = new HashSet<String>(templist); 
	    for (String s : st) {
	    	countmap.put(s, Collections.frequency(templist, s));
	    	//System.out.println(s + ": " + Collections.frequency(templist, s)); 
	    }

	    return countmap;
	        
	} 
	
	private ByteArrayInputStream generateServiceTxt(HttpServletRequest request) {
		ConsumerDetails c =  (ConsumerDetails)request.getSession().getAttribute("consumerdetails");
		String sd = (String) request.getSession().getAttribute("sd");
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(out);
		ps.println(spc(41) + "Andhra Pradesh Central Power Distribution Corporation Limited");
	/*	ps.println(spc(36) + "Corporate Office, Beside Govt. Polytechnic, ITI Road, Vijayawada-520008");*/
		ps.println(spc(20) + "Consumption, Billing, Collection And Arrears History Report For "+c.getCTUSCNO()+" From : "+request.getAttribute("fmonthYear") + " To : "+request.getAttribute("tmonthYear") );

		ps.println(printDoutedLine(160));
		ps.println("Service Number   : "+text_left(c.getCTUSCNO(),40)  +"CMD          : "+text_left(c.getCTCMD_HT(),35)         +"Circle Name    : "+text_left(c.getCIRNAME(),30));
		ps.println("Name             : "+text_left(c.getCTNAME(),40)   +"Voltage      : "+text_left(c.getCTACTUAL_KV(),35)  	+"Division Name  : "+text_left(c.getDIVNAME(),30));
		ps.println("Category         : "+text_left(c.getCTCAT(),40)    +"Feeder Code  : "+text_left(c.getCTFEEDER_CODE(),35)  	+"Sub-Div Name   : "+text_left(c.getSUBNAME(),30));
		ps.println("Sub-Category     : "+text_left(c.getCTSUBCAT(),40) +"Feeder Name  : "+text_left(c.getCTFEEDER_NAME(),35)    +"Section Code   : "+text_left(c.getCTSECCD(),30));
		ps.println("Add1             : "+text_left(c.getCTADD1(),40)   +"Sub-Station  : "+text_left(c.getCTSS_NAME(),35)      	+"Section Name   : "+text_left(c.getSECNAME(),30));
		ps.println("Add2             : "+text_left(c.getCTADD2(),40)   +"DTR Code     : "+text_left(c.getCTDTR_TYPE(),35)		+"ERO Name       : "+text_left(c.getERONAME(),30));
		ps.println("Add3             : "+text_left(c.getCTADD3(),40)   +"Last Paid Dt : "+text_left(c.getCTDTR_TYPE(),35)       +"HOD Department : "+text_left(c.getCTHODDEP(),30));
		ps.println("Add4             : "+text_left(c.getCTADD4(),40)   +"Status       : "+text_left(c.getCTSTATUS().equals("1")?"LIVE":"BILL STOP",35)         +"HOD Sub-Dept   : "+text_left(c.getCTHODSUBDEP(),30));
		ps.println("City             : "+text_left(c.getCTCITY(),40)   +"Sec Deposit  : "+text_left(sd,35)   );
		ps.println("Pincode          : "+text_left(c.getCTPINCODE(),40)+"Social Group : "+text_left(c.getCTSOCIALGROUP(),35));;
		ps.println("Mobile No        : "+text_left(c.getCTMOBILE(),40) +"GOVT/PVT     : "+text_left(c.getCTGOVT_PVT().equals("Y")?"GOVT":"PVT",35) );
		ps.println("Email            : "+text_left(c.getCTEMAILID(),40)+"Released On  : "+text_left(c.getTCTENTDT(),35));
		ps.println("PAN Number       : "+text_left(c.getCTPANNO(),40)  +"Account ID   : "+text_left(c.getCTACCT_ID(),35));
	
		
		
		
		/*ps.println(" Service Number : "+text_left(c.getCTUSCNO(),29)+"Name : "+text_left(c.getCTNAME(),55));
		ps.println("    Circle Name : "+text_left(c.getCIRNAME(),20)+"Division Name : "+text_left(c.getDIVNAME(),17)+"Add1 : "+text_left(c.getCTADD1(),55));
		ps.println("       ERO Name : "+text_left(c.getERONAME(),21)+"Sub-Div Name : "+text_left(c.getSUBNAME(),17)+"Add2 : "+text_left(c.getCTADD2(),55));
		ps.println("   Section Code : "+text_left(c.getCTSECCD(),21)+"Section Name : "+text_left(c.getSECNAME(),17)+"Add3 : "+text_left(c.getCTADD3(),55));
		ps.println("       Category : "+text_left(c.getCTCAT(),21)+"Sub-Category : "+text_left(c.getCTSUBCAT(),17)+"Add4 : "+text_left(c.getCTADD4(),55));
		ps.println("            CMD : "+text_left(c.getCTCMD_HT(),19)+"Connected Load : "+text_left(c.getCTCONNLD(),17)+"City : "+text_left(c.getCTCITY(),55));
		ps.println("      Actual KV : "+text_left(c.getCTACTUAL_KV(),22)+"Sec Deposit : "+text_left(c.getCTSDTOTAMT(),14)+"Pincode : "+text_left(c.getCTPINCODE(), 55));
		ps.println("    Feeder Code : "+text_left(c.getCTFEEDER_CODE(),22)+"Released On : "+text_left(c.getTCTENTDT(),12)+"Mobile No : "+text_left(c.getCTMOBILE(),55));
		ps.println("    Feeder Name : "+text_left(c.getCTFEEDER_NAME(),27)+"Status : "+text_left(c.getCTSTATUS(),16)+"Email : "+text_left(c.getCTEMAILID(),55));
		ps.println("    Sub-Station : "+text_left(c.getCTSS_NAME(),47)+"PAN Number : "+text_left(c.getCTPANNO(),55));
		ps.println("    Pole Number : "+text_left(c.getCTPOLENO(),21)+"PAA Code : "+text_left(c.getCTPAACODE(),7)+"Account ID : "+text_left(c.getCTACCT_ID(),55));
		ps.println("       HOD Type : "+text_left(c.getCTHODTYPE(),19)+"HOD Department : "+text_left(c.getCTHODDEP(),9)+"HOD Sub-Dept : "+text_left(c.getCTHODSUBDEP(),55));
		ps.println("       DTR Type : "+text_left(c.getCTDTR_TYPE(),21)+"GOVT/PVT : "+text_left(c.getCTGOVT_PVT().equals("Y")?"GOVT":"PVT",6)+"Meter Side Flag : "+text_left(c.getCT_METERSIDE_FLAG(),55));
		ps.println("          Group : "+text_left(c.getCTGROUP(),17)+"Social Group : "+text_left(c.getCTSOCIALGROUP(),2)+"Seasonal Flag   : "+text_left(c.getCTSEASFLAG_HT(),55));
		ps.println("        ED Flag : "+text_left(c.getCTEDFLAG(),24)+"SCST Flag : "+text_left(c.getCTSCSTFLAG(),1)+"IND Feeder Flag  : "+text_left(c.getCTINDFEEDFLAG_HT(),55));
		ps.println("       TDS Flag : "+text_left(c.getCTTDS_FLAG(),20)+"Aqua Flag : "+text_left(c.getCTAQUA_FLAG(),6)+"Main Meters : "+text_left(c.getCTMTRCOUNT_HT(),55));
		ps.println("     Solar Flag : "+text_left(c.getCTSOLAR_FLAG(),18)+"Colony Flag : "+text_left(c.getCTCOLNYFLAG_HT(),8)+"Colony Meters : "+text_left(c.getCTCOLNYFLAG_HT(),55));
		ps.println("        LF Flag : "+text_left(c.getCTLF_FLAG(),24)+"LF Meters : "+text_left(c.getCTMTRCOUNT_LF(),55));*/
		ps.println(".............................................................<< FLAGS >>........................................................................");
		ps.println("ED Flag        : "+text_left(c.getCTEDFLAG(),24)          +"SCST Flag   : "+text_left(c.getCTSCSTFLAG(),24)    +"Meter Side Flag : "+text_left(c.getCT_METERSIDE_FLAG(),12)+"Main Meters    : "+text_left(c.getCTMTRCOUNT_HT(),18));
		ps.println("TDS Flag       : "+text_left(c.getCTTDS_FLAG(),24)        +"Aqua Flag   : "+text_left(c.getCTAQUA_FLAG(),24)   +"Seasonal Flag   : "+text_left(c.getCTSEASFLAG_HT(),24));
		ps.println("Solar Flag     : "+text_left(c.getCTSOLAR_FLAG(),24)      +"Colony Flag : "+text_left(c.getCTCOLNYFLAG_HT(),24)+"Colony Meters   : "+text_left(c.getCTMTRCOUNT_CL(),24));
		ps.println("IND Feeder Flag: "+text_left(c.getCTINDFEEDFLAG_HT(),24)  +"LF Flag     : "+text_left(c.getCTLF_FLAG(),24)      +"LF Meters       : "+text_left(c.getCTMTRCOUNT_LF(),24));
		ps.println(printDoutedLine(160));
		//ps.println("S.NO	MON_YEAR	LOAD	CAT	SUB CAT	REC_KVAH	MN_KVAH	STATUS	REC_KWH	REC_MD	TOT_OB	DEMAND	DEBIT_RJ	COLLECTION	CREDIT_RJ	TOTAL_CB	SD");
		ps.println(text_left("S.NO",6)+text_left("Month",9)+text_left("Load",6)+text_left("Cat",5)+text_right("Rec KWH",10)+text_right("Rec KVAH",10)
		+text_right("Min KVAH",10)+text_right("Rec MD",10)+text_left("Status",7)+text_right("Tot OB",12)+text_right("DEMAND",12)
		+text_right("DR RJ",12)+text_right("Collection",10)+text_right("CR RJ",12)+text_right("Tot CB",12)+text_right("Tot SD",12));
		
		ps.println(printDoutedLine(160));
		List<Map<String, Object>> account = (List<Map<String, Object>>) request.getSession().getAttribute("account");
		Iterator<Map<String, Object>> itr = account.iterator();
		int i =1;
		while(itr.hasNext()) {
			Map<String, Object> map = itr.next();
			ps.println(text_left(String.valueOf(i),6)+text_left(String.valueOf(map.get("MON_YEAR")),9)+text_left(String.valueOf(map.get("LOAD")),6)
			+text_left(String.valueOf(map.get("CAT"))+String.valueOf(map.get("SCAT")),5)+text_right(String.valueOf(map.get("REC_KWH")),10)+text_right(String.valueOf(map.get("REC_KVAH")),10)
			+text_right(String.valueOf(map.get("MN_KVAH")),10)
			+text_right(String.valueOf(map.get("REC_MD")),10)+text_left(String.valueOf(map.get("STAT")),7)+text_right(String.valueOf(map.get("TOT_OB")),12)+text_right(String.valueOf(map.get("DEMAND")),12)
			+text_right(String.valueOf(map.get("DEBIT_RJ")),12)+text_right(String.valueOf(map.get("COLLECTION")),12)+text_right(String.valueOf(map.get("CREDIT_RJ")),12)
			+text_right(String.valueOf(map.get("TOTAL_CB")),12)
			+text_right(String.valueOf(map.get("SD")),12));
			i++;
		}
		ps.println(printDoutedLine(160));
		ps.close();
		return new ByteArrayInputStream(out.toByteArray());
	}
	public  String   text_right(String value,int valtotspc)
	{
		int beforespace = valtotspc - value.length();
		int afterspace = 2;
		beforespace = beforespace - afterspace ;
		
		return spc(beforespace)+value+spc(afterspace);
	}
	public    String spc(int i)
	{
		String s="";
			
		for(int j=0;j<i;j++)
			s+= " ";
				
		return (s);
	}
	public  String printDoutedLine(int i)
	{
		String s="";
			
		for(int j=0;j<i;j++)
			s+= "=";
				
		return (s);
	}
	public  String   text_left(String value,int valtotspc)
	{
		try {
		int afterspace = valtotspc - value.length();
		return value+spc(afterspace);
		}catch(NullPointerException ne) {
			return "-"+spc(valtotspc-1);
		}
	}
	public  List<String> getDatesAndMonths(String fy) {

		List<String> months = new ArrayList<String>();
	    DateFormat formater = new SimpleDateFormat("MMM-yyyy");
	    Calendar beginCalendar = Calendar.getInstance();
	    Calendar currentCalendar = Calendar.getInstance();
	    Calendar endCalendar = Calendar.getInstance();
	    try {
	        beginCalendar.setTime(formater.parse("MAY-"+fy.split("-")[0]));
	        currentCalendar.setTime(new Date());
	        endCalendar.setTime(formater.parse("APR-"+fy.split("-")[1]));
	    } catch (ParseException e) {
	        e.printStackTrace();
	    }
	    endCalendar.add(Calendar.MONTH, 1);  
	    endCalendar.set(Calendar.DAY_OF_MONTH, 1);  
	    endCalendar.add(Calendar.DATE, -1); 
	    DateFormat formaterYd = new SimpleDateFormat("MMM_yyyy");
	    
	    while (beginCalendar.before(currentCalendar)) {
	    	months.add(formaterYd.format(beginCalendar.getTime()).toUpperCase());
	        beginCalendar.add(Calendar.MONTH, 1);
	    }

	    return months;
	}
	public   List<String> getCustomColumns(String fy) {

		List<String> months = new ArrayList<String>();
	    DateFormat formater = new SimpleDateFormat("MMM-yyyy");
	    Calendar beginCalendar = Calendar.getInstance();
	    Calendar currentCalendar = Calendar.getInstance();
	    Calendar endCalendar = Calendar.getInstance();
	    try {
	        beginCalendar.setTime(formater.parse("MAY-"+fy.split("-")[0]));
	        currentCalendar.setTime(new Date());
	        endCalendar.setTime(formater.parse("APR-"+fy.split("-")[1]));
	    } catch (ParseException e) {
	        e.printStackTrace();
	    }
	    endCalendar.add(Calendar.MONTH, 1);  
	    endCalendar.set(Calendar.DAY_OF_MONTH, 1);  
	    endCalendar.add(Calendar.DATE, -1); 
	    DateFormat formaterYd = new SimpleDateFormat("MMM_yyyy");
	    
	    if(currentCalendar.before(endCalendar)) {
	        while (beginCalendar.before(currentCalendar)) {
	        	months.add("CNT_"+formaterYd.format(beginCalendar.getTime()).toUpperCase());
	        	months.add(formaterYd.format(beginCalendar.getTime()).toUpperCase());
	            beginCalendar.add(Calendar.MONTH, 1);
	        }
	        }else {
	        	 while (beginCalendar.before(endCalendar)) {
	        		 months.add("CNT_"+formaterYd.format(beginCalendar.getTime()).toUpperCase());
	        	    	months.add(formaterYd.format(beginCalendar.getTime()).toUpperCase());
	        	        beginCalendar.add(Calendar.MONTH, 1);
	        	    }	
	        }

	    return months;
	}

	public    List<String> getTrueCustomColumns(String fy) {

		List<String> months = new ArrayList<String>();
	    DateFormat formater = new SimpleDateFormat("MMM-yyyy");
	    Calendar beginCalendar = Calendar.getInstance();
	    Calendar currentCalendar = Calendar.getInstance();
	    Calendar endCalendar = Calendar.getInstance();
	    try {
	        beginCalendar.setTime(formater.parse("APR-"+fy.split("-")[0]));
	        currentCalendar.setTime(new Date());
	        endCalendar.setTime(formater.parse("MAR-"+fy.split("-")[1]));
	    } catch (ParseException e) {
	        e.printStackTrace();
	    }
	    endCalendar.add(Calendar.MONTH, 1);  
	    endCalendar.set(Calendar.DAY_OF_MONTH, 1);  
	    endCalendar.add(Calendar.DATE, -1); 
	    DateFormat formaterYd = new SimpleDateFormat("MMM_yyyy");
	    if(currentCalendar.before(endCalendar)) {
	        while (beginCalendar.before(currentCalendar)) {
	        	months.add("NOS_"+formaterYd.format(beginCalendar.getTime()).toUpperCase());
	        	months.add("SALES_"+formaterYd.format(beginCalendar.getTime()).toUpperCase());
	        	months.add("DEM_"+formaterYd.format(beginCalendar.getTime()).toUpperCase());
	        	months.add("COLL_"+formaterYd.format(beginCalendar.getTime()).toUpperCase());
	        	months.add("CB_"+formaterYd.format(beginCalendar.getTime()).toUpperCase());
	            beginCalendar.add(Calendar.MONTH, 1);
	        }
	        }else {
	        	 while (beginCalendar.before(endCalendar)) {
	        		 months.add("NOS_"+formaterYd.format(beginCalendar.getTime()).toUpperCase());
	             	months.add("SALES_"+formaterYd.format(beginCalendar.getTime()).toUpperCase());
	             	months.add("DEM_"+formaterYd.format(beginCalendar.getTime()).toUpperCase());
	             	months.add("COLL_"+formaterYd.format(beginCalendar.getTime()).toUpperCase());
	             	months.add("CB_"+formaterYd.format(beginCalendar.getTime()).toUpperCase());
	        	        beginCalendar.add(Calendar.MONTH, 1);
	        	    }	
	        }

	    return months;
	}
	List<String> getMonthColumns(String fy) {

		List<String> months = new ArrayList<String>();
	    DateFormat formater = new SimpleDateFormat("MMM-yyyy");
	    Calendar beginCalendar = Calendar.getInstance();
	    Calendar currentCalendar = Calendar.getInstance();
	    Calendar endCalendar = Calendar.getInstance();
	    try {
	        beginCalendar.setTime(formater.parse("APR-"+fy.split("-")[0]));
	        currentCalendar.setTime(new Date());
	        endCalendar.setTime(formater.parse("MAR-"+fy.split("-")[1]));
	    } catch (ParseException e) {
	        e.printStackTrace();
	    }
	    endCalendar.add(Calendar.MONTH, 1);  
	    endCalendar.set(Calendar.DAY_OF_MONTH, 1);  
	    endCalendar.add(Calendar.DATE, -1); 
	    DateFormat formaterYd = new SimpleDateFormat("MMM_yyyy");
	    if(currentCalendar.before(endCalendar)) {
	        while (beginCalendar.before(currentCalendar)) {
	        	months.add(formaterYd.format(beginCalendar.getTime()).toUpperCase());
	            beginCalendar.add(Calendar.MONTH, 1);
	        }
	        }else {
	        	 while (beginCalendar.before(endCalendar)) {
	             	months.add(formaterYd.format(beginCalendar.getTime()).toUpperCase());
	        	        beginCalendar.add(Calendar.MONTH, 1);
	        	    }	
	        }

	    return months;
	}
	
	
	@ExceptionHandler(NoHandlerFoundException.class)
	public String handle(Exception ex) {
	    return "redirect:/errors";
	}

/*	@RequestMapping(value = {"/404"}, method = RequestMethod.GET)
	public String NotFoudPage() {
	    return "404";

	}*/
	
	
	@RequestMapping(value = "/errors", method = RequestMethod.GET)
    public ModelAndView renderErrorPage(HttpServletRequest httpRequest) {
        
        ModelAndView errorPage = new ModelAndView("errorPage");
        String errorMsg = "";
        int httpErrorCode = getErrorCode(httpRequest);

        switch (httpErrorCode) {
            case 400: {
                errorMsg = "Http Error Code: 400. Bad Request";
                break;
            }
            case 401: {
                errorMsg = "Http Error Code: 401. Unauthorized";
                break;
            }
            case 404: {
                errorMsg = "Http Error Code: 404. Resource not found";
                break;
            }
            case 500: {
                errorMsg = "Http Error Code: 500. Internal Server Error";
                break;
            }
        }
        errorPage.addObject("errorMsg", errorMsg);
        return errorPage;
    }
    
    private int getErrorCode(HttpServletRequest httpRequest) {
        return (Integer) httpRequest
          .getAttribute("javax.servlet.error.status_code");
    }
    @RequestMapping(value = "/getdivision/{circleid}", method = RequestMethod.GET)
	public @ResponseBody String getDivisions(@PathVariable("circleid") String circleid) {
		ObjectMapper mapper = new ObjectMapper();
		String json = "";
		try {
			json = mapper.writeValueAsString(reportDao.getDivisons(circleid));

		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
	}
	@RequestMapping(value = "/getsubdivision/{divisionid}", method = RequestMethod.GET)
	public @ResponseBody String getSubDivisions(@PathVariable("divisionid") String divisionid) {
		ObjectMapper mapper = new ObjectMapper();
		String json = "";
		try {
			json = mapper.writeValueAsString(reportDao.getSubDivisons(divisionid));

		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
	}
	@RequestMapping(value = "/getsection/{subdivisionid}", method = RequestMethod.GET)
	public @ResponseBody String getSection(@PathVariable("subdivisionid") String subdivisionid) {
		
		ObjectMapper mapper = new ObjectMapper();
		String json = "";
		try {
			json = mapper.writeValueAsString(reportDao.getSection(subdivisionid));

		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
	}

}
