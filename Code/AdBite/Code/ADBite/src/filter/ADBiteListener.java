package filter;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;


/**
 * Application Lifecycle Listener implementation class ADBiteListener
 *
 */
public class ADBiteListener implements ServletContextListener {

    /**
     * Default constructor. 
     */
    public ADBiteListener() {
        // TODO Auto-generated constructor stub
    }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent arg0) {
        // TODO Auto-generated method stub
    	System.out.println("ADBite Server Started");
    	//app.Proxy.setProxy();
    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent arg0) {
        // TODO Auto-generated method stub
    }
	
}
