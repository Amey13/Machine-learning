package app;

import java.io.*;
import java.net.URL;
import java.sql.*;
import java.util.*;

public class ADBiteUtil
{
	public final static int MAXIMUM_PHRASE_LENGTH=2;  //maximum phrase length
    public final static int MINIMUM_PHRASE_COUNT=10; //minimum phrase count
   	public final static String[] STOPLIST={"http","www","html","com","php","jpg","gif","png","javascript","javascript:;"}; // List of Common Words which are not important

	public static final String LINKS_FILENAME="LINKS.TXT";
	
	public static class FeatureSets
	{
		public HashMap<String, FeatureSet> FS;
	    
	    public FeatureSets()
	    {
	    	FS=new HashMap<String, FeatureSet>(10,10);
	    	FS.put("caption", new FeatureSet());
	    	FS.put("alt", new FeatureSet());
	    	FS.put("ubase", new FeatureSet());
	    	FS.put("utarget", new FeatureSet());
	    	FS.put("uimg", new FeatureSet());
	    }
	}
	
	public static FeatureSets featureSets;

	public static void reset()
	{
		featureSets=new FeatureSets();
	}
	
    public static void scanPage(String url)
	{
		new Page(url,true);
	}

	public static void scanPagesFromFile()
	{
		try
		{
			FileInputStream fis=new FileInputStream(LINKS_FILENAME);
			String buffer="";
			int i=-1;
			while((i=fis.read())!=-1)
			{
				if(i=='\n')
				{
					buffer=buffer.trim();
					if(!buffer.equals(""))
						scanPage(buffer);
					buffer="";
				}
				else
					buffer+=(char)i;
			}
			buffer=buffer.trim();
			if(!buffer.equals(""))
				scanPage(buffer);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

    public static void main (String[] args)
    {
    	scanPagesFromFile();

    	System.out.println(featureSets.FS.get("utarget").extractFrequentFeatureSet());

    	saveFrequentFeatureset();
    }

    public static void saveFrequentFeatureset()
    {
    	try
    	{
    		Connection conn=connection.DBConnection.connect();
    		Statement st=conn.createStatement();
    		ResultSet rs;
    		String key="";
    		Iterator<String> it;
    		
    		//Delete All Features
    		st.execute("delete from feature");
    		st.execute("delete from featureset");

    		Iterator<String> it2=featureSets.FS.keySet().iterator();
    		rs=st.executeQuery("select max(featuresetid) as mfid from featureset");
    		long fsid=1;
    		if(rs.next())
    			fsid=rs.getLong("mfid")+1;
    		String type="";
    		while(it2.hasNext())
    		{
    			type=it2.next();
    			st.execute("insert into featureset(featuresetid,featuresetname) VALUES("+fsid+" , '"+type+"')");
	    		it=featureSets.FS.get(type).extractFrequentFeatureSet().fs.keySet().iterator();
	    		while(it.hasNext())
	    		{
	    			key=(String)it.next();
	    			st.execute("insert into feature(featuresetid,featurename) values("+fsid+",'"+key+"')");
	    		}
	    		fsid++;
    		}
    		
    		conn.close();
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    }
    
    public static boolean isInStopList(String t)
    {
    	t=t.toLowerCase();
    	for(int i=0;i<STOPLIST.length;i++)
    	{
    		if(t.equals(STOPLIST[i]))
    		{
    			return true;
    		}
    	}
    	return false;
    }
    /*
	public static String getCompleteUrl(String urlstr,String file)
	{
		System.out.println("getCompleteUrl("+urlstr+","+file+")");
		String ret=file;
		try
		{
			URL url=new URL(urlstr);
			if(file.startsWith("/"))
				ret=url.getProtocol()+"://"+url.getHost()+(url.getPort()!=-1?(":"+url.getPort()):"")+file;
			else
				if(file.startsWith("../"))
				{
					//System.out.println("FILENAME : ["+url.getPath()+"]");
					String path=url.getPath().trim();
					ret=url.getProtocol()+"://"+url.getHost()+(url.getPort()!=-1?(":"+url.getPort()):"")+(path.equals("")?"":path.substring(0,path.lastIndexOf("/")));
					//System.out.println("RET :["+ret+"]");
					return getCompleteUrl(ret,file.substring(3));
				}
			else
				if(!file.contains("://"))
				{
					String path=url.getPath().trim();
					ret=url.getProtocol()+"://"+url.getHost()+(url.getPort()!=-1?(":"+url.getPort()):"")+(path.equals("")?"":path.substring(0,path.lastIndexOf("/")))+"/"+file;
				}
		}
		catch (Exception e) { System.out.println("Error on URL : "+urlstr); }
		System.out.println(ret);
		return ret;
	}*/
    public static  String getCompleteUrl(String base,String relative)
	{
		try
		{
			URL u=new URL(new URL(base),relative);
			return u.toString();
		} catch (Exception e) {
			//e.printStackTrace();
			System.out.println("BAD URL");
			return base;
		}
	}
}