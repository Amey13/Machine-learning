package app;

import java.util.*;
import java.sql.*;
import java.net.*;

import org.apache.commons.lang.StringEscapeUtils;

import app.ADBiteUtil.FeatureSets;

import servlet.GetDataFromUrl;

public class Link
{
	public long linkid;
	public long pageid;
	public String aTitle="";
	public String targetURL="";
	public String aCaption="";
	public String imgAlt="";
	public String imgSrc="";
	public String ubase="";
	public int local=0;
	public int height,width;
	public double aspectratio;
	public Vector<String> usefulpart;
	public FeatureSets featureSets=new FeatureSets();
	boolean istobetrained=false;
	
	public static int missingHeight=17;//35;
	public static int missingWidth=31;//60;
	
	
	Link(){}
	Link(String url,
		String aTitle,
		String aHref,
		String aCaption,
		String imgAlt,
		String imgHeight,
		String imgWidth,
		String imgSrc,long pageid)
	{
		ubase=preprocess(url,"ubase");
		this.aTitle=preprocess(aTitle,"");
		this.targetURL=preprocess(aHref,"utarget");
		this.aCaption=preprocess(aCaption,"caption");
		this.imgAlt=preprocess(imgAlt,"alt");
		this.imgSrc=preprocess(imgSrc,"uimg");
		
		if(imgHeight!=null)
			height=Integer.parseInt(imgHeight.trim());
		else
			height=missingHeight;//-1;
		if(imgWidth!=null)
			width=Integer.parseInt(imgWidth.trim());
		else
			width=missingWidth;//-1;
		if(height!=-1 && width!=-1)
			aspectratio=width*1.0/height;
		else
			aspectratio=-1;
		
		this.pageid=pageid;
		
		try
		{
			String t=ADBiteUtil.getCompleteUrl(ubase, imgSrc);
			if((new URL(ubase)).getHost().equalsIgnoreCase((new URL(t)).getHost()))
				local=1;
			else
				local=0;
		}catch (Exception e) { e.printStackTrace(); }
		
		saveToDB();
		System.out.println("Insertion Complete");
	}
	
	public Link(long linkid,long pageid,
			String url,
			String aHref,
			String aCaption,
			String imgAlt,
			String imgSrc,
			int imgHeight,			
			int imgWidth,
			double aratio,
			int local
			)
	{
		istobetrained=true;
		this.linkid=linkid;
		this.pageid=pageid;
		ubase=preprocess(url,"ubase");
		this.targetURL=preprocess(aHref,"utarget");
		this.aCaption=preprocess(aCaption,"caption");
		this.imgAlt=preprocess(imgAlt,"alt");
		this.imgSrc=preprocess(imgSrc,"uimg");
		height=imgHeight;
		width=imgWidth;
		aspectratio=aratio;
		this.local=local;
	}
	
	synchronized protected void saveToDB()
	{
		try
		{
			Connection conn=connection.DBConnection.connect();
			Statement st=conn.createStatement();
			
			//st.execute("delete from link where train='no'");
			
			ResultSet rs=st.executeQuery("select max(linkid) as maxlinkid from link");
			linkid=1;
			if(rs.next())
				linkid=rs.getInt("maxlinkid")+1;

			st.execute("INSERT INTO link(linkid,pageid,targeturl,caption,imgalt,imgsrc,local,height,width,aspectratio)"+
					            " VALUES("+linkid+","+pageid+",'"+StringEscapeUtils.escapeSql(targetURL)+"','"+StringEscapeUtils.escapeSql(aCaption)+"','"+StringEscapeUtils.escapeSql(imgAlt)+"','"+StringEscapeUtils.escapeSql(imgSrc)+"',"+local+","+height+","+width+","+aspectratio+")");
			if(!istobetrained)
			{
				rs=st.executeQuery("select * from feature natural join featureset");
				String fn="",fnid="",fsn;
				String query="INSERT INTO linkfeature(linkid,featureid,value) VALUES ";
				boolean c=false;
				while(rs.next())
				{
					fnid=rs.getString("featureid");
					fn=rs.getString("featurename");
					fsn=rs.getString("featuresetname");
					
					if(c) query+=",";
					else c=true;
						
					if(featureSets.FS.get(fsn).fs.containsKey(fn))
						query+="( "+linkid+", "+fnid+", 1) ";
					else
						query+="( "+linkid+", "+fnid+", 0) ";
				}
				//System.out.println(query);
				st.execute(query);
			}
			conn.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	protected String preprocess(String t,String type)
	{
		if(t!=null)
		{
			 t=t.trim();
			 splitPhrase(t,type);
			 if(!type.equals(""))
			 {
				 if(istobetrained)
				 {
					 if(ADBiteUtil.featureSets!=null)
						 ADBiteUtil.featureSets.FS.get(type).addAllToFeatureSet(linkid,usefulpart);
				 }
				 else
				 {
					 featureSets.FS.get(type).addAllToFeatureSet(linkid,usefulpart);
				 }
				 
			 }
		}
		else
			t="?";	
		return t;
	}
	
	protected Vector<String> splitPhrase(String text,String type)
	{
		usefulpart=new Vector<String>(10,5);
		text=text.toLowerCase();
		String[] parts=text.split("[\\p{Punct}\\p{Space}]+");
		
		for(int i=0;i<parts.length;i++)
		{
			if(!ADBiteUtil.isInStopList(parts[i]) && !parts[i].equals("") && parts[i].length()>2)
				usefulpart.add(parts[i]);
			//System.out.print("["+i+"] : "+parts[i] + ", ");
		}
		parts=usefulpart.toArray(new String[0]);
		
		for(int i=0;i<parts.length && parts.length>1;i++)
		{
			if(i+1<parts.length)
			{
				usefulpart.add(parts[i]+"+"+parts[i+1]);
				i++;
			}
			else
				break;
		}
		try
		{
			//System.out.println("LINK : ["+text+"]");
			if(type.equals("ubase"))
				usefulpart.add(new URL(text).getHost());
			else 
				if(type.equals("utarget"))
					usefulpart.add(new URL(ADBiteUtil.getCompleteUrl(ubase,targetURL)).getHost());
				else
					if(type.equals("uimg"))
						usefulpart.add(new URL(ADBiteUtil.getCompleteUrl(ubase,imgSrc)).getHost());
		}
		catch (Exception e) { e.printStackTrace(); }
		return usefulpart;
	}
	
	public static void isImageAnAdd()
	{
		
	}
	
	public static long getLinkId()
	{
		long t=1;
		try
		{
			Connection conn=connection.DBConnection.connect();
			Statement st=conn.createStatement();
			ResultSet rs=st.executeQuery("select max(linkid) as maxlinkid from link");
			if(rs.next()) t=rs.getInt("maxlinkid")+1;
			conn.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return t;
	}
	
	public String toString()
	{
		return "[ID: "+linkid+
				" , Title: '"+aTitle+
    			"', Target-URL: '"+targetURL+
    			"', Caption: '"+aCaption+
    			"', Image-Height: '"+height+
    			"', Image-Width: '"+width+
    			"', Image-Aspect-Ratio: '"+aspectratio+
    			"', Image-Alt: '"+imgAlt+
    			"', Image-Src: '"+imgSrc+"' ]";
	}
}