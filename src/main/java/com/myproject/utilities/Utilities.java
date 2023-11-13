package com.myproject.utilities;

/**
 * @author Yatindra Kinker
 * 
 */
import java.time.Duration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;

public class Utilities {

	private static Utilities instance = null;
	private static ThreadLocal<WebDriver> webDriver = new ThreadLocal<>();
	private ThreadLocal<String> sessionId = new ThreadLocal<>();
	private static Logger log = LogManager.getLogger(Utilities.class.getName());

	Actions actions;
	JavascriptExecutor jsExecutor;

//	Constructor
	private Utilities() {

	}

	/**
	 * getInstance method to retrieve active driver instance
	 * 
	 * @return
	 */
	public static Utilities getInstance() {

		if (instance == null) {
			instance = new Utilities();
		}

		return instance;
	}

	/**
	 * 
	 * @return
	 */

	public WebDriver getDriver() {
		return webDriver.get();
	}

	/**
	 * setDriver method to create driver instance
	 * 
	 * @param browser
	 */

	public final void setDriver(String browserName) {
		if (browserName == null || browserName.equalsIgnoreCase("chrome")) {
			ChromeOptions options = new ChromeOptions();
			options.addArguments("--remote-allow-origins=*");
			webDriver.set(new ChromeDriver(options));
		} else if (browserName.equalsIgnoreCase("firefox")) {
			FirefoxOptions options = new FirefoxOptions();
			options.setAcceptInsecureCerts(true);
			webDriver.set(new FirefoxDriver(options));
		} else if (browserName.equalsIgnoreCase("safari")) {
			SafariOptions options = new SafariOptions();
			webDriver.set(new SafariDriver(options));

		} else if (browserName.equalsIgnoreCase("edge")) {
			EdgeOptions options = new EdgeOptions();
			webDriver.set(new EdgeDriver(options));
		}

		sessionId.set(((RemoteWebDriver) webDriver.get()).getSessionId().toString());

		getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(GlobalSettings.IMPLICITWAIT));
		getDriver().manage().timeouts().pageLoadTimeout(Duration.ofSeconds(GlobalSettings.PAGELOADTIMEOUT));
		getDriver().manage().window().maximize();
	}

	/**
	 * closeDriver method to close active driver
	 * 
	 */
	public void closeDriver() {
		if (webDriver != null) {
			getDriver().quit();
		}
	}

	/**
	 * 
	 * @return
	 */
	public String getSessionId() {
		return sessionId.get();
	}

	/**
	 * holdScript() is used to hold the script for a specific amount of time.
	 * 
	 * @param sec
	 */
	public void holdScript(int sec) {
		int seconds = sec * 1000;

		try {
			Thread.sleep(seconds);
		} catch (InterruptedException e) {
			log.error("Exception occured in holdScript().");
			Thread.currentThread().interrupt();
		}
	}

	/**
	 * method to click on an element.
	 * 
	 * @param ele
	 */
	public void clickOnElement(WebElement ele) {
		try {
			ele.click();
		} catch (ElementClickInterceptedException | StaleElementReferenceException e) {
			jsClick(ele);
		}
	}

	/**
	 * method to click on an element using actions class.
	 * 
	 * @param ele
	 */
	public void actionsClick(WebElement ele) {
		actions = new Actions(getDriver());
		actions.moveToElement(ele).build().perform();
		actions.click(ele).build().perform();
	}

	/**
	 * click using JavaScript code.
	 * 
	 * @param ele
	 */
	public void jsClick(WebElement ele) {
		jsExecutor = (JavascriptExecutor) getDriver();
		jsExecutor.executeScript("arguments[0].scrollIntoView(true);", ele);
		jsExecutor.executeScript("arguments[0].click();", ele);
	}

	/**
	 * move to desired element using JavaScript code.
	 * 
	 * @param ele
	 */
	public void jsMoveToElement(WebElement ele) {
		jsExecutor = (JavascriptExecutor) getDriver();
		jsExecutor.executeScript("arguments[0].scrollIntoView(true);", ele);
	}

	/**
	 * scroll to the top of web page using JavaScript code.
	 * 
	 */
	public void jsMoveToTopOfWebPage() {
		jsExecutor = (JavascriptExecutor) getDriver();
		jsExecutor.executeScript("0, -(document.body.scrollHeight)");
	}

	/**
	 * scroll to the bottom of web page using JavaScript code.
	 * 
	 */
	public void jsMoveToBottomOfWebPage() {
		jsExecutor = (JavascriptExecutor) getDriver();
		jsExecutor.executeScript("0, document.body.scrollHeight");
	}

}
