package homeLoadTest;

/*
 * This test case verifies if the High Engagement urls are returning a valid status (Code 200)
 * or if there is an error status that needs to be reported.
 * Currently set to run on a QA environment, can be run on Production simply by commenting out
 * the references to the proxy.
 * The urls to be tested come from a simple html file that contains the High Engagement urls
 * defined at the time the test case was written, but the html can be modified as needed.
 */

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
import org.testng.annotations.Test;
import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.SeleneseTestBase;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.By;


public class HomeTest_V2 extends SeleneseTestBase 
{
	 
	 public int invalidLink;
	 String currentLink;
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

		
@Test
	public void saveAllLinks() throws IOException
	{
		PrintWriter outFile = new PrintWriter(new FileWriter("Status_Report.txt", false));
		FirefoxProfile ffProfile = new FirefoxProfile();
		setProxy(ffProfile, proxyURL, port);
		WebDriver firefoxDriver = new FirefoxDriver(ffProfile);
		   
		// Checking links defined by the user in "DNAT_Test_HE.html"
		// Update the path to the html file as needed
		firefoxDriver.navigate().to("file:///C:/Users/Eduardo/Desktop/DNAT_Test_HE.html");
		// The "A" tag is a hyperlink tag need to find all these tags on page
		java.util.List<WebElement> linksList = firefoxDriver.findElements(By.tagName("a"));
		
		outFile.println("### Status detection test started on " + dateFormat.format(cal.getTime()) + " ###");
		outFile.println("/--------------------------------------------------------------------------------------------/");
    	outFile.println();
		
		for(WebElement linkElement: linksList)
		{
			outFile.println();
			String link =linkElement.getAttribute("href");
			if(link!=null)
			{
				if(!isLink(link))
				{
					continue;
				}
				verifyLinkActive(link, outFile);
			}
		}
		outFile.close();
		firefoxDriver.quit();
	}	

	/**
	 * Methods verify some specific link attributes
	 * It can be used for find particular type links.
	 * @param link - link(URL)
	 * @return - true/false
	 */

	public boolean isLink(String link)
	{
		return link.contains("http://") && !link.contains("mailto");
	}

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
			// Currently, the status messages are displayed in the console
			// It could be implemented to send the results to a file or an html report
       
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