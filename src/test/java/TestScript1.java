import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.lang.model.element.Element;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

public class TestScript1 extends Base{
	
	@SuppressWarnings("deprecation")
	@Test
	public void TS01_Verify_ticket_booking() throws Exception
	{
		/*   Validation to be done withing the script:
		 *    1.	Search flight from Dallas to New York, One-way, 1 Traveler, date +1 month from current date
		        2.	Click View on details for the first found flight
				3.Compare the price from the first flight in the pre-book popover
				4.Click on Book Flight
				5.Compare that the book screen total flight price is equal to the price from the search result screen
				6.Fill all the passenger’s information. Select Date of Birth – passenger is 18 years old
				and assert that the selected date was set in the input field  
				
		*/
		log.info("STARTING test TS01_Verify_ticket_booking");
		//Initialized driver
		driver=initializeDriver();
		
		//Get the url to be loaded
		driver.get(readData("url"));
		driver.manage().window().maximize();
		log.info("PASS: url loaded");
		//Search flight from Dallas to New York, One-way, 1 Traveler, date +1 month from current date
		//Select the one way trip
		//try {
		driver.findElement(getElement("onewaytrip.checkbox")).click();
		driver.findElement(getElement("onewaytrip.option")).click();
		log.info("STEP: One Way Detail selected");
		//enter from and to places
		WebDriverWait wait = new WebDriverWait(driver, shortTimeout);
		wait.until(ExpectedConditions.elementToBeClickable(getElement("search.from.input")));
		Actions ac = new Actions(driver);
		ac.click(driver.findElement(getElement("search.from.input"))).sendKeys("Dallas").pause(shortTimeout).sendKeys(Keys.ENTER).build().perform();
		wait.until(ExpectedConditions.attributeContains(driver.findElement(getElement("search.from.input")),"value","Dallas"));
		ac.click(driver.findElement(getElement("search.to.input"))).sendKeys("New York").pause(shortTimeout).sendKeys(Keys.ENTER).build().perform();
		wait.until(ExpectedConditions.attributeContains(driver.findElement(getElement("search.to.input")),"value","New York"));
		log.info("STEP: To and From places updated.");
		//enter the date
		LocalDate travelDate = LocalDate.now().plusMonths(1);
		String travelDayNumber = toString().valueOf(travelDate.getDayOfMonth());
		wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//div[contains(@class,'dateComingMonth')]/div[@class='rdrDays']"))));
		driver.findElement(By.xpath("//div[contains(@class,'dateComingMonth')]/div[@class='rdrDays']/button[contains(@class,'rdrDay')]/span[@class='rdrDayNumber']/span[contains(text(),'"+travelDayNumber+"')]")).click();
		log.info("STEP: Travel date entered");
		//click on search button
		driver.findElement(getElement("search.main.button")).click();
		log.info("STEP: Search button is clicked");
		
		
		//2.	Click View on details for the first found flight
		//Store the price for first flight
		wait.until(ExpectedConditions.invisibilityOf(driver.findElement(By.cssSelector("div[id='progress-bar-container']"))));
		String Price=driver.findElement(By.xpath("//div[contains(@class,'itemContextPrice')]/div/div[contains(@class,'tripOptionPrice')][1]")).getText();
		System.out.println(Price);
		
		//Click on View details
		//wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(getElement("flight.viewDetails.button"))));
		driver.findElement(getElement("flight.viewDetails.button")).click();
//		}
//		catch(ElementClickInterceptedException e)
//		{
//			System.out.println("Subscriber model");
//		}
		//Close driver
		driver.close();
	}

}
