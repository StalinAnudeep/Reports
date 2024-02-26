package com.spdcl.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.spdcl.dao.NewReportDao;

@Controller
public class NewReportController {
	private static Logger log = Logger.getLogger(ReportController.class);
	@Autowired
	NewReportDao newReportDao;

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
		if (fyConsumptionReport.isEmpty()) {
			mav.addObject("fail", "NO DATA FOUND");
		} else {
			mav.addObject("fyConsumptionReport", fyConsumptionReport);
			mav.addObject("type", type);

			if (type.equals("ALL")) {

				mav.addObject("title", "Financial Year Consumption Report For APCPDCL - " + year);
			} else {
				mav.addObject("title", "Financial Year Consumption Report For - " + year);
			}

		}
		return mav;

	}
	
	
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
			mav.addObject("title",
					"Month Wise Tariff Report For - " + request.getParameter("month") + " - " + request.getParameter("year"));

		}
		return mav;

	}
	
	
	
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
			mav.addObject("title",
					"Financial Year Tariff Report For - " + request.getParameter("year"));

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
	    	
	    }

	    return countmap;
	        
	}
}
