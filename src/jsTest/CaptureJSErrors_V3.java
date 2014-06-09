package jsTest;

/*
 * This test case verifies if the High Engagement urls have JS errors and add report them to a text file
 * for further investigation.
 * Currently set to run on a QA environment, can be run on Production simply by commenting out
 * the references to the proxy.
 * The urls to be tested come from a simple html file that contains the High Engagement urls
 * defined at the time the test case was written, but the html can be modified as needed.
 */

import java.io.*;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List; 
import net.jsourcerer.webdriver.jserrorcollector.JavaScriptError; 
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver; 
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver; 
import org.openqa.selenium.firefox.FirefoxProfile; 
import org.testng.annotations.AfterClass; 
import org.testng.annotations.BeforeClass; 
import org.testng.annotations.Test; 

public class CaptureJSErrors_V3 
{ 
	private static WebDriver driver; 
	private FirefoxDriver firefoxDriver;
	private static String proxyURL = "qa-026.proxy.dp.discovery.com";
	private int port = 80;
	
	DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	Calendar cal = Calendar.getInstance();
			
	public static void setProxy(FirefoxProfile fp, String pURL, int port) throws UnknownHostException
	{
		fp.setPreference("network.proxy.ssl_port", port);
		fp.setPreference("network.proxy.ssl", proxyURL);
		fp.setPreference("network.proxy.http_port", port);
		fp.setPreference("network.proxy.http", proxyURL);
		fp.setPreference("network.proxy.type", 1);    
    	
	}
	
	@BeforeClass 
	
	public void setUp() throws IOException 
	{ 
		FirefoxProfile ffProfile = new FirefoxProfile(); 
		setProxy(ffProfile, proxyURL, port); // Comment this line to run the test case in production
		JavaScriptError.addExtension(ffProfile); 
		driver = new FirefoxDriver(ffProfile); 
		firefoxDriver = new FirefoxDriver(ffProfile);
	} 
	
	@AfterClass 
	
	public void tearDown() throws IOException 
	{  
		PrintWriter outFile = new PrintWriter(new FileWriter("JS_ErrorsReport.txt", false));
						
		// Obtaining the urls defined by the user in "DNAT_Test_HE_JS.html"
		// Update the path to the html file as needed
		driver.navigate().to("file:///C:/Users/Eduardo/Desktop/DNAT_Test_HE_JS.html");
    	
    	// The "A" tag is a hyperlink tag need to find all these tags on page
    	java.util.List<WebElement> linksList = driver.findElements(By.tagName("a"));
    	
    	
    	outFile.println("### JS errors detection test started on " + dateFormat.format(cal.getTime()) + " ###");
		outFile.println("/--------------------------------------------------------------------------------------------/");
    	outFile.println();	
    	
    	for(WebElement linkElement: linksList)
    	{
    		String link =linkElement.getAttribute("href");
    		if(link!=null)
    		{
    			if(!isLink(link))
    			{
    				continue;
    			}
    			firefoxDriver.get(link);
    		}
    		
    		List<JavaScriptError> jsErrors = JavaScriptError.readErrors(firefoxDriver); 
    		
    		outFile.println("### Start displaying errors ###  " + link);
    		outFile.println();

    		if (jsErrors.size()!=0)
    		{	
    			for(int i = 0; i < jsErrors.size(); i++) 
    			{	 
    				outFile.println("Error message:  " + jsErrors.get(i).getErrorMessage()); 
    				outFile.println("Line number:  " + jsErrors.get(i).getLineNumber()); 
    				outFile.println("Source name:  " + jsErrors.get(i).getSourceName());
    				outFile.println();
    			}
    		}
    		else
    		{
    			outFile.println("** No JS errors were found for this url **");
    			outFile.println();
    		}
		
    		outFile.println("### Stop displaying errors ###  " + link);
    		outFile.println();
    		outFile.println("//------------------------------------------------------------------------------------------------------------------------------------------------------//");
    		outFile.println();
        }
    	outFile.close();
    	firefoxDriver.quit();
		driver.close(); 
		driver.quit(); 
	} 
	
	public boolean isLink(String link)
	{	
	    return link.contains("http://") && !link.contains("mailto");
	}
	
	@Test 
	
	public void returnJavascriptErrors() throws InterruptedException 
	{ 
		Thread.sleep(5000); 
	} 
} 