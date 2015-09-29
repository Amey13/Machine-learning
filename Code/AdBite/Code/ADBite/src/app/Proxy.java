package app;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

public class Proxy
{
	public static void setProxy()
	{
		try
		{
		    System.setProperty("http.proxyHost","172.18.61.10") ;
		    System.setProperty("http.proxyPort", "3128") ;
		 
		    Authenticator.setDefault(new Authenticator() {
		      protected PasswordAuthentication getPasswordAuthentication() {
		        return new
		           PasswordAuthentication("061080062","vjti123".toCharArray());
		    }});
		    System.out.println("Proxy Set");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}