package commonFunctions;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;

public class FunctionLibaray {
	public static Properties conpro;
	public static WebDriver driver;
	//method for launching browser
	public static WebDriver startBrowser()throws Throwable
	{
		conpro = new Properties();
		//load propertyfile
		conpro.load(new FileInputStream("./PropertyFiles/Environment.properties"));
		if(conpro.getProperty("Browser").equalsIgnoreCase("chrome"))
		{
			driver = new ChromeDriver();
			driver.manage().window().maximize();
		}
		else if(conpro.getProperty("Browser").equalsIgnoreCase("firefox"))
		{
			driver = new FirefoxDriver();
		}
		else
		{
			Reporter.log("Browser values is not Matching",true);
		}
		return driver;
	}
	//method for launching url
	public static void openUrl()
	{
		driver.get(conpro.getProperty("Url"));
	}
	//method for waitfor element
	public static void waitForElement(String LocatorType,String Locatorvalue,String testData)
	{
		WebDriverWait mywait = new WebDriverWait(driver, Duration.ofSeconds(Integer.parseInt(testData)));
		if(LocatorType.equalsIgnoreCase("name"))
		{
			//wait until element is visible
			mywait.until(ExpectedConditions.visibilityOfElementLocated(By.name(Locatorvalue)));
		}
		if(LocatorType.equalsIgnoreCase("xpath"))
		{
			//wait until element is visible
			mywait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Locatorvalue)));
		}
		if(LocatorType.equalsIgnoreCase("id"))
		{
			mywait.until(ExpectedConditions.visibilityOfElementLocated(By.id(Locatorvalue)));
		}
	}
	//method for any textbox
	public static void typeAction(String LocatorType,String LocatorValue,String testData)
	{
		if(LocatorType.equalsIgnoreCase("xpath"))
		{
			driver.findElement(By.xpath(LocatorValue)).clear();
			driver.findElement(By.xpath(LocatorValue)).sendKeys(testData);
		}
		if(LocatorType.equalsIgnoreCase("name"))
		{
			driver.findElement(By.name(LocatorValue)).clear();
			driver.findElement(By.name(LocatorValue)).sendKeys(testData);
		}
		if(LocatorType.equalsIgnoreCase("id"))
		{
			driver.findElement(By.id(LocatorValue)).clear();
			driver.findElement(By.id(LocatorValue)).sendKeys(testData);
		}
	}
	//method for clickAction
	public static void clickAction(String LocatorType,String LocatorValue)
	{
		if(LocatorType.equalsIgnoreCase("xpath"))
		{
			driver.findElement(By.xpath(LocatorValue)).click();
		}
		if(LocatorType.equalsIgnoreCase("name"))
		{
			driver.findElement(By.name(LocatorValue)).click();
		}
		if(LocatorType.equalsIgnoreCase("id"))
		{
			driver.findElement(By.id(LocatorValue)).sendKeys(Keys.ENTER);
		}
	}
	//mehod for validate any page title
	public static void validateTitle(String Expected_Title)
	{
		String Actual_Title = driver.getTitle();
		try {
			Assert.assertEquals(Actual_Title, Expected_Title, "Title is Not Matching");
		}catch(AssertionError a)
		{
			System.out.println(a.getMessage());
		}
	}
	public static void closeBrowser()
	{
		driver.quit();
	}
	//method for generate date
	public static String generateDate()
	{
		Date date = new Date();
		DateFormat df = new SimpleDateFormat("YYYY_MM_dd mm_hh");
		return df.format(date);
	}
	//method for dropdown action
	public static void dropDownAction(String LocatorType,String LocatorValue,String testData)
	{
		if(LocatorType.equalsIgnoreCase("xpath"))
		{
			int value =Integer.parseInt(testData);
			Select element = new Select(driver.findElement(By.xpath(LocatorValue)));
			element.selectByIndex(value);
			
		}
		if(LocatorType.equalsIgnoreCase("name"))
		{
			int value =Integer.parseInt(testData);
			Select element = new Select(driver.findElement(By.name(LocatorValue)));
			element.selectByIndex(value);
			
		}
		if(LocatorType.equalsIgnoreCase("id"))
		{
			int value =Integer.parseInt(testData);
			Select element = new Select(driver.findElement(By.id(LocatorValue)));
			element.selectByIndex(value);
			
		}
	}
	//method for capture stck number into notepad
	public static void capStock(String LocatorType,String LocatorValue)throws Throwable
	{
		String stockNumber ="";
		if(LocatorType.equalsIgnoreCase("xpath"))
		{
			stockNumber = driver.findElement(By.xpath(LocatorValue)).getAttribute("value");
		}
		if(LocatorType.equalsIgnoreCase("id"))
		{
			stockNumber = driver.findElement(By.id(LocatorValue)).getAttribute("value");
		}
		if(LocatorType.equalsIgnoreCase("name"))
		{
			stockNumber = driver.findElement(By.name(LocatorValue)).getAttribute("value");
		}
		//create note pad under CaptureData folder
		FileWriter fw = new FileWriter("./CaptureData/stocknumber.txt");
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(stockNumber);
		bw.flush();
		bw.close();
		
	}
	//method stock table
	public static void stockTable()throws Throwable
	{
		//read stock number from note pad
		FileReader fr = new FileReader("./CaptureData/stocknumber.txt");
		BufferedReader br = new BufferedReader(fr);
		String Exp_Data =br.readLine();
		if(!driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).isDisplayed())
		driver.findElement(By.xpath(conpro.getProperty("search-panel"))).click();
		driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).clear();
		driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).sendKeys(Exp_Data);
		Thread.sleep(2000);
		driver.findElement(By.xpath(conpro.getProperty("search-button"))).click();
		Thread.sleep(3000);
		String Act_Data= driver.findElement(By.xpath("//table[@class='table ewTable']/tbody/tr[1]/td[8]/div/span/span")).getText();
		Reporter.log(Act_Data+"     "+Exp_Data,true);
		try {
		Assert.assertEquals(Act_Data, Exp_Data, "Stock Number Not Found in Table");
		}catch(Throwable t)
		{
			System.out.println(t.getMessage());
		}
		
		
	}
	//method for capturing supplier number into notepad
	public static void capsup(String LocatorType,String LocatorValue)throws Throwable
	{
		String supplierNum ="";
		if(LocatorType.equalsIgnoreCase("xpath"))
		{
			supplierNum = driver.findElement(By.xpath(LocatorValue)).getAttribute("value");
		}
		if(LocatorType.equalsIgnoreCase("id"))
		{
			supplierNum = driver.findElement(By.id(LocatorValue)).getAttribute("value");
		}
		if(LocatorType.equalsIgnoreCase("name"))
		{
			supplierNum = driver.findElement(By.name(LocatorValue)).getAttribute("value");
		}
		//create note pad under CaptureData folder
		FileWriter fw = new FileWriter("./CaptureData/suppliernumber.txt");
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(supplierNum);
		bw.flush();
		bw.close();
	}
	//method for supplier table
	public static void supplierTable()throws Throwable
	{
		FileReader fr = new FileReader("./CaptureData/suppliernumber.txt");
		BufferedReader br = new BufferedReader(fr);
		String Exp_Data =br.readLine();
		if(!driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).isDisplayed())
		driver.findElement(By.xpath(conpro.getProperty("search-panel"))).click();
		driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).clear();
		driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).sendKeys(Exp_Data);
		Thread.sleep(2000);
		driver.findElement(By.xpath(conpro.getProperty("search-button"))).click();
		Thread.sleep(3000);
		String Act_Data= driver.findElement(By.xpath("//table[@class='table ewTable']/tbody/tr[1]/td[6]/div/span/span")).getText();
		Reporter.log(Act_Data+"     "+Exp_Data,true);
		try {
		Assert.assertEquals(Act_Data, Exp_Data, "Supplier Number Not Found in Table");
		}catch(Throwable t)
		{
			System.out.println(t.getMessage());
		}
	}
	//method for capturing customer number into notepad
		public static void captuerCustomer(String LocatorType,String LocatorValue)throws Throwable
		{
			String CustomerNum ="";
			if(LocatorType.equalsIgnoreCase("xpath"))
			{
				CustomerNum = driver.findElement(By.xpath(LocatorValue)).getAttribute("value");
			}
			if(LocatorType.equalsIgnoreCase("id"))
			{
				CustomerNum = driver.findElement(By.id(LocatorValue)).getAttribute("value");
			}
			if(LocatorType.equalsIgnoreCase("name"))
			{
				CustomerNum = driver.findElement(By.name(LocatorValue)).getAttribute("value");
			}
			//create note pad under CaptureData folder
			FileWriter fw = new FileWriter("./CaptureData/Customernumber.txt");
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(CustomerNum);
			bw.flush();
			bw.close();
		}
		//method for customer table
		public static void customerTable()throws Throwable
		{
			FileReader fr = new FileReader("./CaptureData/Customernumber.txt");
			BufferedReader br = new BufferedReader(fr);
			String Exp_Data =br.readLine();
			if(!driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).isDisplayed())
			driver.findElement(By.xpath(conpro.getProperty("search-panel"))).click();
			driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).clear();
			driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).sendKeys(Exp_Data);
			Thread.sleep(2000);
			driver.findElement(By.xpath(conpro.getProperty("search-button"))).click();
			Thread.sleep(3000);
			String Act_Data= driver.findElement(By.xpath("//table[@class='table ewTable']/tbody/tr[1]/td[5]/div/span/span")).getText();
			Reporter.log(Act_Data+"     "+Exp_Data,true);
			try {
			Assert.assertEquals(Act_Data, Exp_Data, "Customer Number Not Found in Table");
			}catch(Throwable t)
			{
				System.out.println(t.getMessage());
			}
		}
}














