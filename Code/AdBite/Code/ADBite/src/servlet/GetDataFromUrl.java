package servlet;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.regex.*;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.htmlparser.*;
import org.htmlparser.filters.*;
import org.htmlparser.util.*;

import filter.ADBiteFilter;

import app.ADBiteUtil;
import app.Page;

/**
 * Servlet implementation class GetDataFromUrl
 */
public class GetDataFromUrl extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public GetDataFromUrl() { }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	
	
	public class Params
	{
		public String url="";
		public String urldata="";
		public String cookie="";
		public Vector<Cookie> cookies;
		public String userAgent="";
		public String requestMethod="GET";
		public String postData="";
		public Map<String,List<String>> headerFields;
	}
	
	protected synchronized void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		response.addHeader("Cache-Control", "no-cache");
		response.addHeader("Pragma", "no-cache");
		
		HttpSession session=request.getSession();
		Params p=new Params();
		String key="",value="";
		p.url=request.getParameter("url");
		String baseurl=(String)session.getAttribute("BASE_URL");
		if(p.url==null)
		{
			p.url=(String)session.getAttribute("url");
		}
		String error=(String)session.getAttribute("error");
		
		System.out.println("URL : "+p.url);
		
		Map params=request.getParameterMap();
		Iterator it=params.keySet().iterator();
		while(it.hasNext())
		{
			key=(String)it.next();
			if(!key.equals("url"))
			p.postData+=key+"="+request.getParameter(key)+"&";
		}
		if(p.postData!=null && p.postData.length()>0 && p.postData.charAt(p.postData.length()-1)=='&')
		{
			p.postData=p.postData.substring(0,p.postData.length()-1);
		}
		System.out.println("Post DATA : "+p.postData);
		String head="";
		Enumeration<String> en=(Enumeration<String>)request.getHeaderNames();
		while(en.hasMoreElements())
		{
			head=(String)en.nextElement();
			//System.out.println(head+" : "+request.getHeader(head));
		}
		p.userAgent=request.getHeader("user-agent");
		p.cookie=request.getHeader("Cookie");
		p.requestMethod=request.getMethod();
		System.out.println("Cookies that i recieved : "+p.cookie);
		
		try{ p.urldata=getData(p); }
		catch(java.io.IOException ioex){ ioex.printStackTrace();  }
		catch(Exception e){ e.printStackTrace(); }
		
		if(!p.urldata.equals(""))
		{
			org.apache.commons.lang.StringEscapeUtils.escapeSql("");
			String data="";
			data=process(p);
			
			List<String> l=null;
	    	Iterator<String> it0=p.headerFields.keySet().iterator(),it2;
	    	
	    	while(it0.hasNext())
	    	{
	    		key=it0.next();
	    		l=p.headerFields.get(key);
	    		it2=l.iterator();
	    		//System.out.print(key+" : ");
	    		while(it2.hasNext())
	    		{
	    			value=it2.next();
	    			if(key!=null && !key.equalsIgnoreCase("Content-Length"))
	    			response.setHeader(key, value);
	    			//System.out.println(value);
	    		}
	    	}
    	
    		if(error==null || error.equals(""))
    		{
    			System.out.println("Setting BASE_URL");
    			session.setAttribute("BASE_URL", p.url);
    		}
    		
    		for(int i=0;i<p.cookies.size();i++)
    			response.addCookie(p.cookies.get(i));
    		
    		Cookie c=new Cookie("JSESSIONID", session.getId());
    		response.addCookie(c);
    		
			//response.setHeader("Set-Cookie", cookie);
			//response.setHeader("Expires", "-1");
			//System.out.println("DATA  :  "+data);
    		 
    		response.setContentType("text/html");
			ServletOutputStream out=response.getOutputStream();
			
			out.write(data.getBytes());
			//out.println(data);
			out.flush();
			out.close();
		}
		else
		{
			System.out.println("Redirecting to "+p.url);
			response.sendRedirect(p.url);
		}
			session.removeAttribute("error");
			
			p=null;
	}
	
	public String getData(Params p) throws Exception
	{
		String urldata="";

		HttpURLConnection huc=null;
		//huc.connect();
    	
    	String temp = "";
		String content=p.postData;
		String location="";
		boolean more=true;
		
		while(more)
		{
			huc=(HttpURLConnection)(new URL(p.url).openConnection());
			huc.setDoOutput(true);
			huc.setRequestProperty("User-Agent", p.userAgent);
			if(p.requestMethod.equals("POST"))
			{
				huc.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
				huc.setRequestProperty("Content-Length", String.valueOf(content.length()));
			}
			huc.setRequestProperty("Accept", "*"+"/*");
			huc.setRequestProperty("Cookie", p.cookie);
			System.out.println("Request Method : " + p.requestMethod);
			huc.setInstanceFollowRedirects(true);
			huc.setRequestMethod(p.requestMethod);
	
			if(p.requestMethod.equals("POST"))
			{
				PrintWriter pw = new PrintWriter(new OutputStreamWriter(huc.getOutputStream()), true);
				pw.print(content);
				pw.flush();
				pw.close();
			}
			
			location=huc.getHeaderField("Location");
			if(location!=null && !location.equals(""))
			{
				more=true;
				p.url=location;
			}
			else
				more=false;
		}
		System.out.println("Content Type : "+huc.getContentType());
		String ct=huc.getContentType();
		if(ct!=null){
			ct=ct.toLowerCase();
		if(ct.contains("text") && !ct.contains("javascript"))
		{
			BufferedReader br = new BufferedReader(new InputStreamReader(huc.getInputStream()));
			while ( (temp = br.readLine()) != null ) {
					urldata+=temp+"\n";
				}
			br.close();
			
			p.cookie = "";
			
			p.headerFields=huc.getHeaderFields();
	    	List<String> l=null;
	    	Iterator<String> it=p.headerFields.keySet().iterator(),it2;
	    	String key="",value="",t="",cname,cvalue,cexp,cpath,cparts[];
	    	p.cookies=new Vector<Cookie>();
	    	Cookie c;
	    	try{
	    	while(it.hasNext())
	    	{
	    		key=it.next();
	    		l=p.headerFields.get(key);
	    		it2=l.iterator();
	    		//System.out.print(key+" : ");
	    		while(it2.hasNext())
	    		{
	    			value=it2.next();
	    			if(key!=null && key.equals("Set-Cookie"))
	    			{
	    				p.cookie+=value+"\n";
	    				cparts=value.split(";");
	    				t=cparts[0];
	    				cname=t.substring(0,t.indexOf("="));
	    				cvalue=t.substring(t.indexOf("=")+1);
	    				
	    				t=cparts[1];
	    				cexp=t.substring(t.indexOf("=")+1);
	    				
	    				t=cparts[2];
	    				cpath=t.substring(t.indexOf("=")+1);
	    					    				
	    				c=new Cookie(cname,cvalue);
	    				c.setPath("/");
	    				p.cookies.add(c);
	    			}
	    			//System.out.println(value);
	    		}
	    	}}catch (Exception e) {
				
			}
	    	
	    	System.out.println ("Cookies Received : "+p.cookie);
		}
		else
		{
			urldata="";
		}
		}
		return urldata;
	}
	
	public String process(Params p)
	{
		
		String data="";
		try
		{
			Parser par=new Parser();

			
			par.setInputHTML(p.urldata);
			NodeList root=par.parse(null);
			Page pg=new Page(p.url,false,root);
			root=pg.root;
			String href="";
			NodeList nl= root.extractAllNodesThatMatch (new TagNameFilter("IMG"),true);
			SimpleNodeIterator sni= nl.elements();
			Tag n,pn;
			boolean childOfA=false;
			String src="";
			while(sni.hasMoreNodes())
	    	{
				n=(Tag)sni.nextNode();
				/*
				pn=(Tag)n.getParent();
				while(pn!=null && !pn.getTagName().equals("A"))
					pn=(Tag)pn.getParent();
				if(pn!=null && pn.getTagName().equals("A"))
				{
					src=n.getAttribute("src");
				}*/
				if(n.getAttribute("FILTERED")==null)
				{
					src=n.getAttribute("src");
					n.setAttribute("src", ADBiteUtil.getCompleteUrl(p.url,src));
				}
	    		//n.setAttribute("alt","AD FILTERED BY ADBITE.COM");
	    	}
			
			nl= root.extractAllNodesThatMatch (new TagNameFilter("A"),true);
			sni= nl.elements();
			while(sni.hasMoreNodes())
	    	{
	    		n=(Tag)sni.nextNode();
	    		href=n.getAttribute("href");
	    		if(n.getAttribute("FILTERED")==null)
	    		{
	    			if(href!=null && !href.startsWith("javascript"))
	    				n.setAttribute("href", "GetDataFromUrl?url="+URLEncoder.encode(ADBiteUtil.getCompleteUrl(p.url,href)));
	    		}
	    		//n.setAttribute("alt","THIS IS AN AD");
	    		n.setAttribute("target", "pageframe");
	    	}
			
			nl= root.extractAllNodesThatMatch (new TagNameFilter("AREA"),true);
			sni= nl.elements();
			while(sni.hasMoreNodes())
	    	{
	    		n=(Tag)sni.nextNode();
	    		href=n.getAttribute("href");
	    		if(href!=null && !href.startsWith("javascript"))
	    		n.setAttribute("href", "GetDataFromUrl?url="+URLEncoder.encode(ADBiteUtil.getCompleteUrl(p.url,href)));
	    		n.setAttribute("alt","THIS IS AN AD");
	    		n.setAttribute("target", "pageframe");
	    	}
			
			nl= root.extractAllNodesThatMatch (new TagNameFilter("FRAME"),true);
			sni= nl.elements();
			while(sni.hasMoreNodes())
	    	{
	    		n=(Tag)sni.nextNode();
	    		href=n.getAttribute("src");
	    		if(href!=null)
	    		n.setAttribute("src", "GetDataFromUrl?url="+URLEncoder.encode(ADBiteUtil.getCompleteUrl(p.url,href)));
	    	}
			
			nl= root.extractAllNodesThatMatch (new TagNameFilter("IFRAME"),true);
			sni= nl.elements();
			while(sni.hasMoreNodes())
	    	{
	    		n=(Tag)sni.nextNode();
	    		href=n.getAttribute("src");
	    		if(href!=null)
	    		n.setAttribute("src", "GetDataFromUrl?url="+URLEncoder.encode(ADBiteUtil.getCompleteUrl(p.url,href)));
	    	}
			
			nl= root.extractAllNodesThatMatch (new TagNameFilter("LINK"),true);
			sni= nl.elements();
			while(sni.hasMoreNodes())
	    	{
	    		n=(Tag)sni.nextNode();
	    		n.setAttribute("href",ADBiteUtil.getCompleteUrl(p.url,n.getAttribute("href")));
	    	}
			
			nl= root.extractAllNodesThatMatch (new TagNameFilter("FORM"),true);
			sni= nl.elements();
			while(sni.hasMoreNodes())
	    	{
	    		n=(Tag)sni.nextNode();
	    		n.setAttribute("action","GetDataFromUrl?url="+ADBiteUtil.getCompleteUrl(p.url,n.getAttribute("action")));
	    	}
			
			nl= root.extractAllNodesThatMatch (new TagNameFilter("SCRIPT"),true);
			sni= nl.elements();
			while(sni.hasMoreNodes())
	    	{
	    		n=(Tag)sni.nextNode();
	    		if(n.getAttribute("src")!=null)
	    		n.setAttribute("src",ADBiteUtil.getCompleteUrl(p.url,n.getAttribute("src")));
	    	}
			
			data=root.toHtml(true); 
			System.out.println("Processing Complete!");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}
	
	
	
}