package com.myproject.extentreport;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentReportNg {

	private ExtentReportNg() {

	}

	static ExtentReports extent;

	public static ExtentReports getReportObject(String browser) {
		LocalDateTime myDateObj = LocalDateTime.now();
		DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss");
//			Create directory where we want to generate reports.
		String path = System.getProperty("user.dir") + "/Reports/" + browser + myDateObj.format(myFormatObj)
				+ "__ExtentReport.html";
		ExtentSparkReporter reporter = new ExtentSparkReporter(path);

//			Set Report Name
		reporter.config().setReportName("Automation_Extent_Report");

//			Set Document title
		reporter.config().setDocumentTitle("Cedcommerce(Amazon Multi-Acnt)");

//			Creating class that drives all the reporting execution.
		extent = new ExtentReports();
		extent.attachReporter(reporter);

//			Adding tester name
		extent.setSystemInfo("User", System.getProperty("user.name"));
		extent.setSystemInfo("OS", System.getProperty("os.name"));
		extent.setSystemInfo("Version", System.getProperty("os.version"));
		extent.setSystemInfo("Browser", browser);
		extent.setSystemInfo("Architecture", System.getProperty("os.arch"));
		extent.setSystemInfo("Java Version", System.getProperty("java.specification.version"));

		return extent;
	}
}
