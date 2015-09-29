package app;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

@SuppressWarnings("serial")
public class FeatureSet implements Serializable
{
	HashMap<String,Feature> fs;
	
	FeatureSet()
	{
		fs=new HashMap<String,Feature>(10,5);
	}
	
	public void addFeature(String fn)
	{
		fs.put(fn.toLowerCase(),new Feature(fn));
	}
	
	public void addLinkToFeature(String fn,long linkid,long linkct)
	{
		fn=fn.toLowerCase();
		if(fs.containsKey(fn))
		{
			fs.get(fn).addLink(linkid,linkct);
		}
		else
		{
			fs.put(fn,new Feature(fn));
			fs.get(fn).addLink(linkid,linkct);
		}
	}
	
	public void addAllToFeatureSet(long linkid,Vector<String> flist)
	{
		for(int i=0;i<flist.size();i++)
			addLinkToFeature(flist.get(i),linkid,1);
	}
	
	public FeatureSet extractFrequentFeatureSet()
	{
		FeatureSet temp=null;
		try
		{
			temp=new FeatureSet();
			Iterator<String> it=fs.keySet().iterator();
			Iterator<Long> it2;
			String key=null;
			Feature f;
			long linkid=0,linkct=0;
			while(it.hasNext())
			{
				key=(String)it.next();
				f=(Feature)fs.get(key);
				if(f.getCount()>=ADBiteUtil.MINIMUM_PHRASE_COUNT)
				{
					it2=f.occursIn.keySet().iterator();
					while(it2.hasNext())
					{
						linkid=(Long)it2.next();
						linkct=(Long)f.occursIn.get(linkid);
						temp.addLinkToFeature(key,linkid,linkct);
					}
				}
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return temp;
	}
	
	public String toString()
	{
		String op="";
		Iterator<String> it=fs.keySet().iterator();
    	while(it.hasNext())
    	{
    		op+=((Feature)fs.get(it.next()))+"\n";
    	}
    	
    	return op;
	}
}