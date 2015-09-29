package app;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;

@SuppressWarnings("serial")
public class Feature implements Serializable
{
	String featureName;
	HashMap<Long,Long> occursIn;
	
	Feature(){}
	Feature(String fn)
	{
		featureName=fn;
		occursIn=new HashMap<Long,Long>(10,10);
	}

	public void addLink(long linkid,long linkct)
	{
		if(occursIn.containsKey(linkid))
			occursIn.put(linkid,occursIn.get(linkid)+linkct);
		else occursIn.put(linkid,linkct);
	}
	
	public String toString()
	{
		String op="FEATURE SET : "+featureName+"\n";
		long temp=0;
		Iterator<Long> it=occursIn.keySet().iterator();
		
		while(it.hasNext())
		{
			temp=(Long)it.next();
			op+="LinkID : "+temp+" , Count:"+ occursIn.get(temp)+"\n";
		}
		return op;
	}
	
	public boolean hasLink(long linkid)
	{
		return occursIn.containsKey(linkid);
	}
	
	public long getCount()
	{
		long temp=0;
		long ct=0;
		Iterator<Long> it=occursIn.keySet().iterator();
		while(it.hasNext())
		{
			temp=(Long)it.next();
			ct+=(Long)occursIn.get(temp);
		}
		return ct;
	}
}