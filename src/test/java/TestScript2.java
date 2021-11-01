import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

public class TestScript2 extends Base{
	
	@Test
	public void TC02_Verify_DFW_NYK_ticket() throws IOException
	{
		/*
		 * Validation to be covered: 
		 * 1.Search flight from Dallas to New York, One-way, 1 Traveler, date +2 weeks from
			current date. “Use quick search url(direct flight search without first home page
			screen)” – Example url - https://www.oojo.com/result/DFW-NYC/2021-12-09
			2.
			
			Wait for search to finish
			3.
			Assert that every found flight contains price & assert that (give your suggestion
			what
			to check)
			4.
			Go to the bottom of the search, click on “Show more results” & assert that more
			flights are viewed rather than the amount what was viewed before. Also check
			that after showing more results, new viewed flights are not equal to old previous
			ones.
		 */
		
		log.info("STARTING test TC02_Verify_DFW_NYK_ticket");
		String modifiedUrl;
		
		//Initialized driver
		driver=initializeDriver();
		WebDriverWait wait = new WebDriverWait(driver, longTimeout);
		//Modify the default url for quick search
		LocalDate travelDate = LocalDate.now().plusWeeks(2);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("YYYY-MM-dd");
		System.out.println(formatter.format(travelDate));
		
		modifiedUrl=readData("url")+"/result/DFW-NYC/"+toString().valueOf(formatter.format(travelDate));
		driver.get(modifiedUrl);
		
		driver.manage().window().maximize();
		log.info("PASS: "+modifiedUrl+" - URL loaded");
		
		//Wait for search to finish
		wait.until(ExpectedConditions.attributeToBe(By.tagName("html"),"class", " "));

		//Assert that found flights have price listed 
		int totalPriceElements=driver.findElements(By.cssSelector("div[class*='tripOptionPrice']")).size();
		int totalFlightContainers=driver.findElements(By.cssSelector("div[class*='tripOptionPQ']")).size();
		Assert.assertTrue("FAIL: Prices does not match with flight containers", totalPriceElements==totalFlightContainers);
		log.info("PASS: Price details are present for all flights ");
		
//		Go to the bottom of the search, click on “Show more results” & assert that more
//		flights are viewed rather than the amount what was viewed before. Also check
//		that after showing more results, new viewed flights are not equal to old previous
//		ones.
		int currentFlightsOnPage = driver.findElements(By.cssSelector("div[class*='tripOptionPQ']")).size();
		
		//click on show more
		try {
		driver.findElement(getElement("page.showMore.button")).click();
		}
		catch(ElementClickInterceptedException e)
		{
			if(driver.findElement(By.cssSelector("button[id='onetrust-accept-btn-handler']")).isEnabled())
			{
			driver.findElement(By.cssSelector("button[id='onetrust-accept-btn-handler']")).click();
			}
			else
			{
				log.info("ERROR: some other issue");
			}
		}
		wait.until(ExpectedConditions.invisibilityOf(driver.findElement(getElement("page.showMore.button"))));
		
		int flightsOnPageShowMore=driver.findElements(By.cssSelector("div[class*='tripOptionPQ']")).size();
		System.out.println(flightsOnPageShowMore+"        "+currentFlightsOnPage);
		//Assert that new flights are more than before
		Assert.assertTrue("FAIL: After show more results> the flights are not updated",flightsOnPageShowMore>currentFlightsOnPage);
		log.info("PASS: Show more results button is working fine");
		
		//close driver
		driver.close();
	}

}
