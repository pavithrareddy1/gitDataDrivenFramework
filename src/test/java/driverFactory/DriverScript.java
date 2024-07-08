package driverFactory;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import commonFunctions.FunctionLibaray;
import utilities.ExcelFileUtil;

public class DriverScript {
	public static WebDriver driver;
	String inputpath ="./FileInput/Controller.xlsx";
	String outputpath ="./FileOutput/HybridResults.xlsx";
	String TCSheet ="MasterTestCases";
	String TCModule ="";
	ExtentReports reports;
	ExtentTest logger;
	public void startTest() throws Throwable
	{
		String Module_status="";
		String Module_new="";
		//create object for Excelfileutil
		ExcelFileUtil xl = new ExcelFileUtil(inputpath);
		//iterate all rows in TCSheet
		for(int i=1;i<=xl.rowCount(TCSheet);i++)
		{
			if(xl.getCellData(TCSheet, i, 2).equalsIgnoreCase("Y"))
			{
				
				//store corresponding sheet or testcases into TCModule
				TCModule =xl.getCellData(TCSheet, i, 1);
				//define path for html report
				reports = new ExtentReports("./target/ExtentReports/"+TCModule+FunctionLibaray.generateDate()+".html");
				logger =reports.startTest(TCModule);
				logger.assignAuthor("Ranga");
				//iterate corresponding sheet
				for(int j=1;j<=xl.rowCount(TCModule);j++)
				{
					//read each cell from TCModule sheet
					String Description = xl.getCellData(TCModule, j, 0);
					String Object_type = xl.getCellData(TCModule, j, 1);
					String Ltype = xl.getCellData(TCModule, j, 2);
					String Lvalue = xl.getCellData(TCModule, j, 3);
					String TData = xl.getCellData(TCModule, j, 4);
					try {
						if(Object_type.equalsIgnoreCase("startBrowser"))
						{
							driver =FunctionLibaray.startBrowser();
							logger.log(LogStatus.INFO, Description);
						}
						if(Object_type.equalsIgnoreCase("openUrl"))
						{
							FunctionLibaray.openUrl();
							logger.log(LogStatus.INFO, Description);
						}
						if(Object_type.equalsIgnoreCase("waitForElement"))
						{
							FunctionLibaray.waitForElement(Ltype, Lvalue, TData);
							logger.log(LogStatus.INFO, Description);
						}
						if(Object_type.equalsIgnoreCase("typeAction"))
						{
							FunctionLibaray.typeAction(Ltype, Lvalue, TData);
							logger.log(LogStatus.INFO, Description);
						}
						if(Object_type.equalsIgnoreCase("clickAction"))
						{
							FunctionLibaray.clickAction(Ltype, Lvalue);
							logger.log(LogStatus.INFO, Description);
						}
						if(Object_type.equalsIgnoreCase("validateTitle"))
						{
							FunctionLibaray.validateTitle(TData);
							logger.log(LogStatus.INFO, Description);
						}
						if(Object_type.equalsIgnoreCase("closeBrowser"))
						{
							FunctionLibaray.closeBrowser();
							logger.log(LogStatus.INFO, Description);
						}
						if(Object_type.equalsIgnoreCase("dropDownAction"))
						{
							FunctionLibaray.dropDownAction(Ltype, Lvalue, TData);
							logger.log(LogStatus.INFO, Description);
						}
						if(Object_type.equalsIgnoreCase("capStock"))
						{
							FunctionLibaray.capStock(Ltype, Lvalue);
							logger.log(LogStatus.INFO, Description);
						}
						if(Object_type.equalsIgnoreCase("stockTable"))
						{
							FunctionLibaray.stockTable();
							logger.log(LogStatus.INFO, Description);
						}
						if(Object_type.equalsIgnoreCase("capsup"))
						{
							FunctionLibaray.capsup(Ltype, Lvalue);
							logger.log(LogStatus.INFO, Description);
						}
						
						if(Object_type.equalsIgnoreCase("supplierTable"))
						{
							FunctionLibaray.supplierTable();
							logger.log(LogStatus.INFO, Description);
							
						}
						if(Object_type.equalsIgnoreCase("captuerCustomer"))
						{
							FunctionLibaray.captuerCustomer(Ltype, Lvalue);
							logger.log(LogStatus.INFO, Description);
						}
						if(Object_type.equalsIgnoreCase("customerTable"))
						{
							FunctionLibaray.customerTable();
							logger.log(LogStatus.INFO, Description);
						}
						//write a spass into status cell TCModule
						xl.setCellData(TCModule, j, 5, "Pass", outputpath);
						logger.log(LogStatus.PASS, Description);
						Module_status="True";
					}catch(Exception e)
					{
						System.out.println(e.getMessage());
						//write as Fail into status cell TCModule
						xl.setCellData(TCModule, j, 5, "Fail", outputpath);
						logger.log(LogStatus.FAIL, Description);
						Module_new="False";
						File screen =((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
						FileUtils.copyFile(screen, new File("./target/Screenshot/"+TCModule+Description+FunctionLibaray.generateDate()+".png"));
					}
					reports.endTest(logger);
					reports.flush();
					if(Module_status.equalsIgnoreCase("True"))
					{
						//write as pass into TCSheet
						xl.setCellData(TCSheet, i, 3, "Pass", outputpath);
					}
					if(Module_new.equalsIgnoreCase("False"))
					{
						//write as Fail into TCSheet
						xl.setCellData(TCSheet, i, 3, "Fail", outputpath);
					}
				}
			}
			else
			{
				//write as blocked into status cell in TCSheet
				xl.setCellData(TCSheet, i, 3, "Blocked", outputpath);
			}
		}
	}
}












