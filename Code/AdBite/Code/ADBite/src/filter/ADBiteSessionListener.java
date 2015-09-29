package filter;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * Application Lifecycle Listener implementation class ADESessionListener
 *
 */
public class ADBiteSessionListener implements HttpSessionListener {

    /**
     * Default constructor. 
     */
    public ADBiteSessionListener() {
        // TODO Auto-generated constructor stub
    }

	/**
     * @see HttpSessionListener#sessionCreated(HttpSessionEvent)
     */
    public void sessionCreated(HttpSessionEvent arg0) {
        // TODO Auto-generated method stub
    	System.out.println("SESSION CREATED ");
    	
    }

	/**
     * @see HttpSessionListener#sessionDestroyed(HttpSessionEvent)
     */
    public void sessionDestroyed(HttpSessionEvent arg0) {
        // TODO Auto-generated method stub
    	System.out.println("SESSION DESTROYED ");
    }
	
}
