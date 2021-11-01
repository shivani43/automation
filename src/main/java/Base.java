import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeSuite;


public class Base {
	
	public WebDriver driver;
	 protected org.slf4j.Logger log = LoggerFactory.getLogger(getClass());
	 protected int shortTimeout = 15;
	 protected int longTimeout = 60;
	 protected static Properties elementMap;
	      
	
	public WebDriver initializeDriver() throws IOException
	{
		//Initialize the browser based on browser data
		Properties prop = new Properties();
		FileInputStream fis = new FileInputStream("C:\\automation\\src\\main\\resources\\data.properties");
		prop.load(fis);
		String browserName = prop.getProperty("browser");
		
		if(browserName.equals("chrome"))
		{
		System.setProperty("webdriver.chrome.driver","C:\\Drivers\\chromedriver.exe");
		driver = new ChromeDriver();
		log.info("Chrome driver initialized");
		}
		else if(browserName.equals("firefox"))
		{
		System.setProperty("webdriver.gecko.driver","C:\\Drivers\\geckodriver.exe");
		driver = new FirefoxDriver();
		log.info("Firefox driver initialized");
		}
		if(browserName.equals("edge"))
		{
		System.setProperty("webdriver.edge.driver","C:\\Drivers\\msedgedriver.exe");
		driver = new EdgeDriver();
		log.info("Edge driver initialized");
		}
		
		return driver;
	}
	
	public String readData(String key) throws IOException
	{
		//Return the value for the key present in data file
		Properties prop = new Properties();
		FileInputStream fis = new FileInputStream("C:\\automation\\src\\main\\resources\\data.properties");
		prop.load(fis);
		String value = prop.getProperty(key);
		return value;
	}
	
	@BeforeSuite(alwaysRun=true)
	public void suiteSetup() throws IOException
	{
		  // Load element Map properties
	    elementMap = new java.util.Properties();
	    FileInputStream in = new FileInputStream("C:\\automation\\src\\main\\resources\\ElementMap.properties");
	    try {
	      elementMap.load(in);
	    } catch (IOException e) {
	      Assert.fail("FAIL - Unable to load ElementMap.properties", e);
	      
	    }
	}

	/**
	   * Helper function to look up an element based off of a key String, which is
	   * used to retrieve a type + selector from elementMap, which is then used to
	   * generate a 'By' object which can be used in findElement(),
	   * waitForElement(), isElementPresent(), etc.
	   * 
	   * @param element_key
	   *          Property key to search for in elementMap
	   * @return By object with definition of type + selector found in elementMap
	   */
	  public By getElement(String element_key) {
	
	    By foundObject = null;
	
	    // log.info("DEBUG: select by string: " + element_key);
	
	    // elementMap is a properties object initialized in setupSuite from property
	    // file.
	    String selectorFull = elementMap.getProperty(element_key);
	    // Validate that key is found from property file.
	    Assert.assertNotNull(selectorFull, "FAIL - Did not find " + element_key + " in element map");
	
	    // Parse property value to determine the type of selector and selection
	    // string
	    // for the desired element. The first part of the value is the selector
	    // type, then
	    // a '!' separator token, followed by the string to be used by that
	    // selector.
	    // Example properties:
	    // DashboardSearchBtn=name!RecordLocator
	    // NewOppBtn=id!newopportunitybtn
	    // NewOppModalHeader=css!div.panel-heading.panel-modal-newopportunity > h4
	    int split = selectorFull.indexOf("!");
	    String selectorTyp = selectorFull.substring(0, split);
	    String selectorString = selectorFull.substring(split + 1);
	
	    switch (selectorTyp) {
	    case "css":
	      foundObject = By.cssSelector(selectorString);
	      break;
	    case "id":
	      foundObject = By.id(selectorString);
	      break;
	    case "name":
	      foundObject = By.name(selectorString);
	      break;
	    case "xpath":
	      foundObject = By.xpath(selectorString);
	      break;
	    case "linkText":
	      foundObject = By.linkText(selectorString);
	      break;
	    case "partialLinkText":
	      foundObject = By.partialLinkText(selectorString);
	      break;
	    default:
	      Assert.fail("FAIL - Invalid selector type [" + selectorTyp + "] in map for key [" + element_key + "]");
	    }
	
	    return foundObject;
	  }
	
	

}
