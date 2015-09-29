package filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.*;

/**
 * Servlet Filter implementation class ADEaterFilter
 */
public class ADBiteFilter implements Filter {

    /**
     * Default constructor. 
     */
    public ADBiteFilter() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request1, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub
		// place your code here
		HttpServletRequest request=(HttpServletRequest)request1;
		System.out.println("IN FILTER, URI = "+request.getRequestURL());
		HttpSession session=request.getSession();
		/*
		if(request.getRealPath(request.getRequestURI())==null)
		{
			System.out.println("FILE NOT FOUND!!!!!!!!!!");
			Cookie[] cookies=request.getCookies();
			String value="";
			for(Cookie c : cookies)
			{
				if(c.getName().equals("BASE_URL"))
				{
					value=c.getValue();
					break;
				}
			}
			session.setAttribute("BASE_URL", value);
			session.setAttribute("url", request.getRequestURI());
			request.getRequestDispatcher("GetDataFromUrl").forward(request, response);
		}*/
			// pass the request along the filter chain
			session.removeAttribute("url");
			
			HttpServletResponse res=(HttpServletResponse)response;
			res.addHeader("Cache-Control", "no-cache");
			res.addHeader("Cache-Control", "no-store");
			res.addHeader("Cache-Control", "max-age=0");
			res.addHeader("Pragma", "no-cache");
			
			
			chain.doFilter(request, response);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}
}