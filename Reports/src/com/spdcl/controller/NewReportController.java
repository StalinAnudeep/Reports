package com.spdcl.controller;

import java.util.List;
import java.util.Map;

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

}
