package app;

import java.net.*;
import java.sql.*;
import java.util.*;

import org.htmlparser.*;
import org.htmlparser.filters.*;
import org.htmlparser.util.*;

public class Page
{    
    public HashMap<Long,Link> atags;
	String url;
	public long pageid;
	boolean istobetrained=false;
	public NodeList root=null;
	public Page(){}
	public Page(String u, boolean train)
	{
		url=u;
		atags=new HashMap<Long,Link>(10,5);

		istobetrained=train;
		saveToDB();
		processURL();
	}
	
	public Page(String u, boolean train,NodeList root)
	{
		url=u;
		atags=new HashMap<Long,Link>(10,5);
		this.root=root;		
		istobetrained=train;
		saveToDB();
		processURL();
	}
	
	public Page(long dbpid,String url)
	{
		pageid=dbpid;
		this.url=url;
		atags=new HashMap<Long,Link>(10,5);
	}
	
	protected void saveToDB()
	{
		Connection conn=null;
		Statement st=null;
		try
		{
			conn=connection.DBConnection.connect();
			conn.setAutoCommit(false);
			
			st=conn.createStatement();
			
			ResultSet rs=st.executeQuery("select max(pageid) as maxpageid from page");
			pageid=1;
			if(rs.next())
				pageid=rs.getInt("maxpageid")+1;
			System.out.println("PAGEID : "+pageid);
			if(istobetrained)
				st.execute("INSERT INTO page(pageid,baseurl,train) VALUES("+pageid+",'"+org.apache.commons.lang.StringEscapeUtils.escapeSql(url)+"',1)");
			else
				st.execute("INSERT INTO page(pageid,baseurl) VALUES("+pageid+",'"+org.apache.commons.lang.StringEscapeUtils.escapeSql(url)+"')");
			conn.commit();
			conn.close();
		}
		catch(SQLException sqle)
		{
			if(conn!=null)
			{
				try
				{
					conn.rollback(); 
					conn.close();
					saveToDB();
				}catch(SQLException e){ e.printStackTrace(); }
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public synchronized void processURL()
    {
    	try
    	{
    		
	    	Parser par=null;
	    	if(root==null)
	    	{
		    	par=new Parser(new URL(url).openConnection());
		    	root=par.parse(null);
	    	}
	    	
	    	NodeList nl= root.extractAllNodesThatMatch(new TagNameFilter("A"),true);
	    	SimpleNodeIterator sni= nl.elements(),aChildren;
	    	String aTitle="",aHref="",aCaption="",imgAlt="",imgHt="",imgWt="",imgSrc="";
	    	Tag aChildTag=null,imgTag=null;
	    	Tag n;
	    	Link l=null;
	    	Node tempn=null;
	    	boolean found=false;
	    	long linkid=0;
	    	
	    	Vector<Tag> listImgtags=null;
	    	HashMap<Long, Vector<Tag>> map=new HashMap<Long, Vector<Tag>>(10,2);
	    	
	    	while(sni.hasMoreNodes())
	    	{
	    		n=(Tag)sni.nextNode();
	    		listImgtags=new Vector<Tag>(10,2);
	    		aTitle=n.getAttribute("title");
	    		aHref=n.getAttribute("href");
	    		
	    		try{ aChildren=n.getChildren().elements(); }catch(Exception e){ continue; }
	    		aCaption="";
	    		found=false;
	    		while(aChildren.hasMoreNodes())
	    		{
	    			//System.out.println ("("+aChildren.nextNode().toHtml());
		    		try
		    		{
		    			tempn=aChildren.nextNode();
		    			aChildTag=(Tag)(tempn);
		    			if(aChildTag.getTagName().equals("IMG"))
		    			{
		    				imgTag=aChildTag;
			    			imgHt=aChildTag.getAttribute("height");
			    			imgWt=aChildTag.getAttribute("width");
			    			imgAlt=aChildTag.getAttribute("alt");
			    			imgSrc=aChildTag.getAttribute("src");

			    			imgTag.setAttribute("IMGNO", ""+Link.getLinkId());
			    			n.setAttribute("ANO", ""+Link.getLinkId());
			    			
			    			listImgtags.add(imgTag);
			    			
			    			found=true;
		    			}
		    		}
		    		catch(ClassCastException cce)
		    		{
		    			aCaption=tempn.getText();
		    		}
	    		}

	    		if(found)
	    		{
	    			System.out.println("IN PAGE -> UTARGET : ["+aHref+"]");
	    			Link link=new Link(url,aTitle,aHref,aCaption,imgAlt,imgHt,imgWt,imgSrc,pageid);
	    			map.put(link.linkid,listImgtags);
		    		put(link);
		    		//System.out.println(atags.get(i));
	    		}
	    	}
	    	
	    	if(!istobetrained)
	    	{
	    		Vector<Long> ads=new Vector<Long>(10,10);

	    		Connection conn=connection.DBConnection.connect();
				Statement st=conn.createStatement();
				ResultSet rs;
				
				/* 
				 * CALL C FUNCTION TO CLASSIFY THE ADS,  HERE ->
				 */
				ProcessBuilder pb=new ProcessBuilder("SVM.exe",""+pageid);
				pb.redirectErrorStream(true);
				Process proc=pb.start();
				proc.waitFor();
				System.out.println("Classification Complete!");
				String type="";
				rs=st.executeQuery("select * from link where pageid="+pageid);
				while(rs.next())
				{
					type=rs.getString("type");
					if(type.equals("ad"))
						ads.add(rs.getLong("linkid"));
				}
				
				conn.close();
	    		
	    		boolean isad=false;
	    		/*
	    		nl= root.extractAllNodesThatMatch(new TagNameFilter("A"),true);
	    		sni= nl.elements();
	    		while(sni.hasMoreNodes())
	 	    	{
	    			isad=false;
	    			n=(Tag)sni.nextNode();
	    			if(n.getAttribute("ANO")!=null)
	    			{
	    				aChildren=n.getChildren().extractAllNodesThatMatch(new TagNameFilter("IMG"),true).elements();
	    				while(aChildren.hasMoreNodes())
	    	    		{
			    			aChildTag=(Tag)(aChildren.nextNode());
			    			linkid=0;
			    			if(aChildTag.getAttribute("IMGNO")!=null)
			    			{
			    				linkid=Long.parseLong(aChildTag.getAttribute("IMGNO"));
			    				if(ads.contains(linkid))
			    				{
			    					isad=true;
			    					aChildTag.setAttribute("src", "/images/ad.jpg");
			    				}
			    			}
	    	    		}
	    			}
	    			if(isad)
	    			{
	    				n.setAttribute("alt", "AD FILTERED BY ADBIE.COM");
	    				n.setAttribute("href", "javascript:void(0);");
	    				n.setAttribute("onclick", "");
	    			}
	 	    	}
	    		*/
	    		Iterator it=map.keySet().iterator();
	    		long tl=0;
	    		Vector<Tag> vtg=new Vector<Tag>(10,2);
	    		Tag t=null,p;
	    		int i=0;
	    		while(it.hasNext())
	    		{
	    			tl=(Long)it.next();
	    			if(ads.contains(tl))
	    			{
		    			vtg=(Vector<Tag>)map.get(tl);
		    			for(i=0;i<vtg.size();i++)
		    			{
			    			t=vtg.get(i);
			    			
			    			t.setAttribute("src", "/images/ad.gif");
		    				do{ p=(Tag)t.getParent(); }
		    				while(p!=null && !p.getTagName().equals("A"));
		    				if(p!=null)
		    				{
		    					p.setAttribute("alt", "AD FILTERED BY ADBIE.COM");
				    			p.setAttribute("href", "javascript:void(0);");
				    			p.setAttribute("onclick", "");
				    			p.setAttribute("FILTERED", "1");
		    				}
		    				t.setAttribute("FILTERED", "1");
		    			}
	    			}
	    		}
	    	}
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
	}
	
	public void put(Link l)
	{
		atags.put(l.linkid, l);
	}
	
	public void put(long id,Link l)
	{
		atags.put(id, l);
	}
}