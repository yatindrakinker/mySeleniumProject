package com.myproject.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.myproject.baseclass.BaseClass;

public class TestFirefox extends BaseClass {

	@BeforeClass(alwaysRun = true)
	public void initializeInstances() {
		setConfigFile();
	}

	@BeforeMethod
	public void launchUrl() {
		obj.setDriver("firefox");
		String url = getConfigProperty().get("url").toString();
		obj.getDriver().get(url);
		log.error("driver launched");
	}

	@Test
	public void testGoogleIsOpened() {
		WebElement textField = obj.getDriver().findElement(By.name("q"));
		Assert.assertTrue(!textField.isDisplayed(), "text field is not displayed.");
	}

	@AfterMethod
	public void close() {
		obj.closeDriver();
	}
}
