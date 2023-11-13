package com.myproject.baseclass;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import com.myproject.utilities.Utilities;

public class BaseClass {

	protected Utilities obj = Utilities.getInstance();
	Properties prop;
	protected static Logger log = LogManager.getLogger(BaseClass.class.getName());
	private static final String PROJECTPATH = System.getProperty("user.dir");
	protected String activeBrowser;
	

	/**
	 * method to open browser on the basis of value passed from testng.xml file.
	 * 
	 * @param browser
	 */
	@BeforeMethod
	@Parameters("browser")
	public void launchUrl(@Optional String browser) {
		obj.setDriver(browser);
		String url = getConfigProperty().get("url").toString();
		obj.getDriver().get(url);
	}

	/**
	 * close the browser.
	 */
	@AfterMethod
	public void close() {
		obj.closeDriver();
	}

	/**
	 * set config.properties file.
	 * 
	 */
	public void setConfigFile() {
		prop = new Properties();
		String pathConfigFile = PROJECTPATH + "/src/main/java/com/myproject/properties/config.properties";

		try (FileInputStream fis = new FileInputStream(pathConfigFile)) {
			prop.load(fis);
		} catch (IOException e) {
			log.error("Config.properties file does not exist.");
		}
	}

	public Properties getConfigProperty() {
		return prop;
	}

	/**
	 * method to take screenshot
	 * 
	 * @param testCaseName
	 * @return
	 */
	public String takeScreenShot(String testCaseName) {
		TakesScreenshot screenShot = (TakesScreenshot) obj.getDriver();
		File src = screenShot.getScreenshotAs(OutputType.FILE);
		String destination = PROJECTPATH + "/ScreenShot/" + testCaseName + ".png";

		try {
			FileUtils.copyFile(src, new File(destination));
		} catch (IOException e) {
			log.error("Exception occured in copying file.");
		}

		return destination;
	}

}
