package staticAssetsTest;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.SeleneseTestBase;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.By;



public class ImagesTest_v3 extends SeleneseTestBase 
{
	 
	 private static WebDriver driver; 
	 private FirefoxDriver firefoxDriver;
	 String temp;
	 public DefaultSelenium selenium;
	 private static String proxyURL = "qa-026.proxy.dp.discovery.com";
	 private int port = 80;
			  
	 DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	 Calendar cal = Calendar.getInstance();
				
	 	 
	 public static void setProxy(FirefoxProfile fp, String pURL, int port1) throws UnknownHostException
	{
		fp.setPreference("network.proxy.ssl_port", port1);
		fp.setPreference("network.proxy.ssl", proxyURL);
		fp.setPreference("network.proxy.http_port", port1);
		fp.setPreference("network.proxy.http", proxyURL);
		fp.setPreference("network.proxy.type", 1);    
	}

	
	@BeforeTest
	
	public void setUp() throws IOException 
	{ 
		FirefoxProfile ffProfile = new FirefoxProfile(); 
	//	setProxy(ffProfile, proxyURL, port);
		driver = new FirefoxDriver(ffProfile); 
		firefoxDriver = new FirefoxDriver(ffProfile);
	} 
	

@Test
	public void saveAllLinks() throws IOException
	{
		PrintWriter outFile = new PrintWriter(new FileWriter("Assets_Status_Report.txt", false));
					   
		// Obtaining the urls defined by the user in "DNAT_Test_HE.html"
		// the file location should be updated as needed
	
    	driver.navigate().to("file:///C:/Users/Eduardo/Desktop/DNAT_Test_HE2.html");
		
		// Obtain the urls from the html file
    	java.util.List<WebElement> linksList = driver.findElements(By.tagName("a"));
		
		outFile.println("### Status detection test started on " + dateFormat.format(cal.getTime()) + " ###");
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
    		// The "img" tag is a hyperlink tag needed to find all these tags on page
    		// Obtain the the images contained in a webpage
    		java.util.List<WebElement> linksList2 = firefoxDriver.findElements(By.tagName("img"));
    		
    		outFile.println();
    		outFile.println("### Start displaying errors ###  " + link);
    		outFile.println();
				
			for(WebElement linkElement2: linksList2)
			{
				String link2 =linkElement2.getAttribute("src");
				if(link2!=null)
				{
					if(!isImageLink(link2))
					{
						continue;
					}
					outFile.println();
					verifyLinkActive(link2, outFile);
				}
			}
    	}
		outFile.close();
    	firefoxDriver.quit();
		driver.close(); 
		driver.quit();
		   	
	}	

	/**
	 * Methods verify some specific link attributes
	 * It can be used for find particular type links.
	 * @param link - link(URL)
	 * @return - true/false
	 */

	// Verifies that the image belongs to one of the requested static assets
	public boolean isImageLink(String link)
	{
		return link.contains("http://") && link.contains("static.howstuffworks.com") || link.contains("r.howstuffworks.com") || link.contains("static.ddmcdn.com") || link.contains("r.ddmcdn.com") && !link.contains("mailto");
	}

	public boolean isLink(String link)
	{	
	    return link.contains("http://") && !link.contains("mailto");
	}
	
	
	// Verifies the status of the image
	// Currently all the images are displayed, so all the results are 200 status. 
	// Ideally, the 200 status message as well as other unimportant status messages
	// should be commented in order to include only the desired status messages
	public void verifyLinkActive(String linkUrl, PrintWriter f)
	{
		try 
		{
			URL url = new URL(linkUrl);
			HttpURLConnection httpURLConnect=(HttpURLConnection)url.openConnection();
			httpURLConnect.setConnectTimeout(3000);
			httpURLConnect.connect();
    
			// The following code is used to show the status obtained in the HTTP response
			// for the tested urls
			// Unnecessary messages can be commented in the code
			// Currently the results are stored in a file but they could be dispayed in an html report
       
			switch (httpURLConnect.getResponseCode()) 
			{
            	// HTTP Status-Code 202: Accepted.  
       			case HttpURLConnection.HTTP_ACCEPTED:
       				f.println(linkUrl+" - "+httpURLConnect.getResponseMessage()
                                  + " - "+ HttpURLConnection.HTTP_ACCEPTED);
            	break;
       
            	//HTTP Status-Code 502: Bad Gateway.
       			case HttpURLConnection.HTTP_BAD_GATEWAY:
       				f.println(linkUrl+" - "+httpURLConnect.getResponseMessage()
                                  + " - "+ HttpURLConnection.HTTP_BAD_GATEWAY);
       			break;
       

       			//HTTP Status-Code 405: Method Not Allowed.
       			case HttpURLConnection.HTTP_BAD_METHOD:
       				f.println(linkUrl+" - "+httpURLConnect.getResponseMessage()
                                  + " - "+ HttpURLConnection.HTTP_BAD_METHOD);
                break;
       
                //HTTP Status-Code 400: Bad Request.
       			case HttpURLConnection.HTTP_BAD_REQUEST:
       				f.println(linkUrl+" - "+httpURLConnect.getResponseMessage()
                                  + " - "+ HttpURLConnection.HTTP_BAD_REQUEST);
                break;
       
                //HTTP Status-Code 408: Request Time-Out.
       			case HttpURLConnection.HTTP_CLIENT_TIMEOUT:
       				f.println(linkUrl+" - "+httpURLConnect.getResponseMessage()
                                  + " - "+ HttpURLConnection.HTTP_CLIENT_TIMEOUT);
       			break;
       
       			//HTTP Status-Code 409: Conflict.
       			case HttpURLConnection.HTTP_CONFLICT:
       				f.println(linkUrl+" - "+httpURLConnect.getResponseMessage()
                                  + " - "+ HttpURLConnection.HTTP_CONFLICT);
       			break;
       
       			//HTTP Status-Code 201: Created.
       			case HttpURLConnection.HTTP_CREATED:
       				f.println(linkUrl+" - "+httpURLConnect.getResponseMessage()
                                  + " - "+ HttpURLConnection.HTTP_CREATED);
       			break;
       
       			//HTTP Status-Code 413: Request Entity Too Large.
       			case HttpURLConnection.HTTP_ENTITY_TOO_LARGE:
       				f.println(linkUrl+" - "+httpURLConnect.getResponseMessage()
                                  + " - "+ HttpURLConnection.HTTP_ENTITY_TOO_LARGE);
       			break;
       
       			//HTTP Status-Code 403: Forbidden.
       			case HttpURLConnection.HTTP_FORBIDDEN:
       				f.println(linkUrl+" - "+httpURLConnect.getResponseMessage()
                                  + " - "+ HttpURLConnection.HTTP_FORBIDDEN);
       			break;
       
       			//HTTP Status-Code 504: Gateway Timeout.
       			case HttpURLConnection.HTTP_GATEWAY_TIMEOUT:
       				f.println(linkUrl+" - "+httpURLConnect.getResponseMessage()
                                  + " - "+ HttpURLConnection.HTTP_GATEWAY_TIMEOUT);
       			break;
       
       			//HTTP Status-Code 410: Gone.
       			case HttpURLConnection.HTTP_GONE:
       				f.println(linkUrl+" - "+httpURLConnect.getResponseMessage()
                                  + " - "+ HttpURLConnection.HTTP_GONE);
       			break;
       
       			//HTTP Status-Code 500: Internal Server Error.
       			case HttpURLConnection.HTTP_INTERNAL_ERROR:
       				f.println(linkUrl+" - "+httpURLConnect.getResponseMessage()
                                  + " - "+ HttpURLConnection.HTTP_INTERNAL_ERROR);
       			break;
       
       			//HTTP Status-Code 411: Length Required.
       			case HttpURLConnection.HTTP_LENGTH_REQUIRED:
       				f.println(linkUrl+" - "+httpURLConnect.getResponseMessage()
                                  + " - "+ HttpURLConnection.HTTP_LENGTH_REQUIRED);
       			break;
       
       			//HTTP Status-Code 301: Moved Permanently.
       			case HttpURLConnection.HTTP_MOVED_PERM:
       				f.println(linkUrl+" - "+httpURLConnect.getResponseMessage()
                                  + " - "+ HttpURLConnection.HTTP_MOVED_PERM);
       			break;
       
       			//HTTP Status-Code 302: Temporary Redirect.
       			case HttpURLConnection.HTTP_MOVED_TEMP:
       				f.println(linkUrl+" - "+httpURLConnect.getResponseMessage()
                                  + " - "+ HttpURLConnection.HTTP_MOVED_TEMP);
       			break;
       
       			//HTTP Status-Code 300: Multiple Choices.
       			case HttpURLConnection.HTTP_MULT_CHOICE:
       				f.println(linkUrl+" - "+httpURLConnect.getResponseMessage()
                                  + " - "+ HttpURLConnection.HTTP_MULT_CHOICE);
       			break;
       
       			//HTTP Status-Code 204: No Content.
       			case HttpURLConnection.HTTP_NO_CONTENT:
       				f.println(linkUrl+" - "+httpURLConnect.getResponseMessage()
                                  + " - "+ HttpURLConnection.HTTP_NO_CONTENT);
       			break;
                         
       			//HTTP Status-Code 406: Not Acceptable.
       			case HttpURLConnection.HTTP_NOT_ACCEPTABLE:
       				f.println(linkUrl+" - "+httpURLConnect.getResponseMessage()
                                  + " - "+ HttpURLConnection.HTTP_NOT_ACCEPTABLE);
       			break;
       
       			//HTTP Status-Code 203: Non-Authoritative Information.
       			case HttpURLConnection.HTTP_NOT_AUTHORITATIVE:
       				f.println(linkUrl+" - "+httpURLConnect.getResponseMessage()
                                  + " - "+ HttpURLConnection.HTTP_NOT_AUTHORITATIVE);
       			break;
       
       			//HTTP Status-Code 404: Not Found.
       			case HttpURLConnection.HTTP_NOT_FOUND:
       				f.println(linkUrl+" - "+httpURLConnect.getResponseMessage()
                                  + " - "+ HttpURLConnection.HTTP_NOT_FOUND);
       			break;
       
       			//HTTP Status-Code 501: Not Implemented.
       			case HttpURLConnection.HTTP_NOT_IMPLEMENTED:
       				f.println(linkUrl+" - "+httpURLConnect.getResponseMessage()
                                  + " - "+ HttpURLConnection.HTTP_NOT_IMPLEMENTED);
       			break;
       
       			//HTTP Status-Code 304: Not Modified.
       			case HttpURLConnection.HTTP_NOT_MODIFIED:
       				f.println(linkUrl+" - "+httpURLConnect.getResponseMessage()
                                  + " - "+ HttpURLConnection.HTTP_NOT_MODIFIED);
       			break;
       
       			//HTTP Status-Code 200: OK.
       			case HttpURLConnection.HTTP_OK:
       				f.println(linkUrl+" - "+httpURLConnect.getResponseMessage()
                                  + " - "+ HttpURLConnection.HTTP_OK);
       			break;
       
       			//HTTP Status-Code 206: Partial Content.
       			case HttpURLConnection.HTTP_PARTIAL:
       				f.println(linkUrl+" - "+httpURLConnect.getResponseMessage()
                                  + " - "+ HttpURLConnection.HTTP_PARTIAL);
       			break;
       
       			//HTTP Status-Code 402: Payment Required.
       			case HttpURLConnection.HTTP_PAYMENT_REQUIRED:
       				f.println(linkUrl+" - "+httpURLConnect.getResponseMessage()
                                  + " - "+ HttpURLConnection.HTTP_PAYMENT_REQUIRED);
       			break;
       
       			//HTTP Status-Code 412: Precondition Failed.
       			case HttpURLConnection.HTTP_PRECON_FAILED:
       				f.println(linkUrl+" - "+httpURLConnect.getResponseMessage()
                                  + " - "+ HttpURLConnection.HTTP_PRECON_FAILED);
       			break;
       
       			//HTTP Status-Code 407: Proxy Authentication Required.
       			case HttpURLConnection.HTTP_PROXY_AUTH:
       				f.println(linkUrl+" - "+httpURLConnect.getResponseMessage()
                                  + " - "+ HttpURLConnection.HTTP_PROXY_AUTH);
       			break;
       
       			//HTTP Status-Code 414: Request-URI Too Large.
       			case HttpURLConnection.HTTP_REQ_TOO_LONG:
       				f.println(linkUrl+" - "+httpURLConnect.getResponseMessage()
                                  + " - "+ HttpURLConnection.HTTP_REQ_TOO_LONG);
       			break;
       
       			//HTTP Status-Code 205: Reset Content.
       			case HttpURLConnection.HTTP_RESET:
       				f.println(linkUrl+" - "+httpURLConnect.getResponseMessage()
                                  + " - "+ HttpURLConnection.HTTP_RESET);
       			break;
       
       			//HTTP Status-Code 303: See Other.
       			case HttpURLConnection.HTTP_SEE_OTHER:
       				f.println(linkUrl+" - "+httpURLConnect.getResponseMessage()
                                  + " - "+ HttpURLConnection.HTTP_SEE_OTHER);
       			break;
       
       			//HTTP Status-Code 401: Unauthorized.
       			case HttpURLConnection.HTTP_UNAUTHORIZED:
       				f.println(linkUrl+" - "+httpURLConnect.getResponseMessage()
                                  + " - "+ HttpURLConnection.HTTP_UNAUTHORIZED);
       			break;
       
       			//HTTP Status-Code 503: Service Unavailable.
       			case HttpURLConnection.HTTP_UNAVAILABLE:
       				f.println(linkUrl+" - "+httpURLConnect.getResponseMessage()
                                  + " - "+ HttpURLConnection.HTTP_UNAVAILABLE);
       			break;
       
       			//HTTP Status-Code 415: Unsupported Media Type.
       			case HttpURLConnection.HTTP_UNSUPPORTED_TYPE:
       				f.println(linkUrl+" - "+httpURLConnect.getResponseMessage()
                                  + " - "+ HttpURLConnection.HTTP_UNSUPPORTED_TYPE);
       			break;
       
       			//HTTP Status-Code 305: Use Proxy.
       			case HttpURLConnection.HTTP_USE_PROXY:
       				f.println(linkUrl+" - "+httpURLConnect.getResponseMessage()
                                  + " - "+ HttpURLConnection.HTTP_USE_PROXY);
       			break;
       
       			//HTTP Status-Code 505: HTTP Version Not Supported.
       			case HttpURLConnection.HTTP_VERSION:
       				f.println(linkUrl+" - "+httpURLConnect.getResponseMessage()
                                  + " - "+ HttpURLConnection.HTTP_VERSION);
       			break;
                  
       			default:
                break;
            }
		}
		
		catch (MalformedURLException e) 
		{
			f.println(linkUrl+" - "+ "Invalid URL"); //e.printStackTrace();
		} 
		catch (IOException e) 
		{
			f.println(linkUrl+" - "+ "Invalid URL"); //e.printStackTrace();
		}
	} 
}