package com.myproject.testnglistener;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.myproject.baseclass.BaseClass;
import com.myproject.extentreport.ExtentReportNg;

public class Listeners extends BaseClass implements ITestListener {

	ExtentTest test;
	ExtentReports extent;
	ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();
	String browser;

	@Override
	public void onStart(ITestContext context) {
		browser = context.getCurrentXmlTest().getParameter("browser");
		extent = ExtentReportNg.getReportObject(browser);
	}

	@Override
	public void onTestStart(ITestResult result) {
		test = extent.createTest(result.getMethod().getMethodName());
		test.assignCategory(result.getInstanceName());
		extentTest.set(test);
	}

	@Override
	public void onTestSuccess(ITestResult result) {
		extentTest.get().log(Status.PASS, "Test Passed");
	}

	@Override
	public void onTestFailure(ITestResult result) {
		extentTest.get().fail(result.getThrowable());
		String testMethodName = result.getMethod().getMethodName();
		try {
			result.getTestClass().getRealClass().getField("driver").get(result.getInstance());
		} catch (Exception e) {
			log.error("Exception occured in onTestFailure in Listeners.java.");
		}
		extentTest.get().addScreenCaptureFromPath(takeScreenShot(testMethodName), testMethodName);

	}

	@Override
	public void onTestSkipped(ITestResult result) {
		extentTest.get().log(Status.SKIP, "Test Skipped");
	}

	@Override
	public void onFinish(ITestContext context) {
		extent.flush();
	}

}
