package app;
import java.io.*;
import java.sql.*;


public class extract
{
	public static void putFeaturesIntoDB()
	{
		try
    	{
    		Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/AdEater","root","123456789");
			Statement st=conn.createStatement();
			ResultSet rs=null;
		
			st.executeUpdate("DELETE FROM linkfeature");
			st.executeUpdate("DELETE FROM feature");
			st.executeUpdate("DELETE FROM featureset");
			st.executeUpdate("DELETE FROM link");
			st.executeUpdate("DELETE FROM page");
				
    		BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream("ad_attribute_names.txt")));
    		String line="",t,f,fs,prev="";
    		int ct=0;
    		long fsid=1;
    		while(br.ready())
    		{
    			line=br.readLine().trim();
	    			
    			if(!line.startsWith("|") && !line.equals(""))
    			{
    				fs=line.substring(0,line.indexOf("*"));
	    			f=line.substring(line.indexOf("*")+1,line.indexOf(":"));
	    			if(prev.equals("") || !prev.equals(fs))
	    			{
	    				if(fs.equals("url"))
	    					t="uimg";
	    				else
	    					if(fs.equals("origurl"))
	    						t="ubase";
	    					else
	    						if(fs.equals("ancurl"))
	    							t="utarget";
	    						else
	    							t=fs;
	    				rs=st.executeQuery("select max(featuresetid) from featureset");
	    				if(rs.next())
	    					fsid=rs.getLong(1)+1;
	    				
	    				st.executeUpdate("insert into featureset values( "+fsid+", '"+t+"')");
	    				
	    				prev=fs;
	    			}
	    			st.executeUpdate("insert into feature(featuresetid,featurename) values( "+fsid+", '"+f+"')");
	    			//System.out.println("fs : "+fs+" -> "+t);
	    			ct++;
    			}
    		}
    		
    		System.out.println ("Total Number of Attributes : "+ct);
    		
    		br.close();
    		conn.close();
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
	}
	
	public static void createTable()
	{
		Connection conn=null;
		try
    	{
    		Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/AdEater","root","123456789");
			Statement st=conn.createStatement();
			ResultSet rs=null;
			
			st.executeUpdate("DROP TABLE IF EXISTS temp_features");
			
			String query = "CREATE TABLE IF NOT EXISTS temp_features(";
			int MAX=1000;
			for(int i=0;i<MAX;i++)
			{
				query+="attr_"+i+" BOOL  ";
				if(i<MAX-1)
					query+=" , ";
			}
			query+=");";
			st.executeUpdate(query);
			System.out.println (query);
			conn.close();
    	}
    	catch(SQLException e)
    	{
    		System.out.println ("Error : "+e.getErrorCode()+"\n State"+e.getSQLState());
    		e.printStackTrace();
    	}
    	catch(Exception e)
    	{
    		//e.printStackTrace();
    	}
	}
	
	public static void formatInput()
	{
		Connection conn=null;
		try
    	{
    		Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/AdEater2","root","123456789");
			Statement st=conn.createStatement(),st2=conn.createStatement();
			ResultSet rs=null,rs2=null;
			PrintStream out=new PrintStream("ad_data_new_wrc.com.txt");
			long linkid=0;
			double height,width,aratio;
			int val,local,i;
			String type;
			
			rs=st.executeQuery("select * from link natural join page where pageid=77 order by linkid");
			while(rs.next())
			{
				linkid=rs.getLong("linkid");
				height=rs.getDouble("height");
				width=rs.getDouble("width");
				aratio=rs.getDouble("aspectratio");
				local=rs.getInt("local");
				type=rs.getString("type");
				
				if(height==-1) out.print("?");
				else out.print(height);
				out.print(",");
				
				if(width==-1) out.print("?");
				else out.print(width);
				out.print(",");
				
				if(aratio==-1) out.print("?");
				else out.print(aratio);
				out.print(",");
				
				out.print(local+",");
				
				rs2=st2.executeQuery("select * from linkfeature where linkid="+linkid+" order by featureid asc");

				while(rs2.next())
				{
					val=rs2.getInt("value");
					out.print(val+",");
				}
				
				if(type.equals("ad")) out.println("ad.");
				else out.println("nonad.");
			}
			
			out.close();
			conn.close();
    	}
    	catch(SQLException e)
    	{
    		System.out.println ("Error : "+e.getErrorCode()+"\n State"+e.getSQLState());
    		e.printStackTrace();
    	}
    	catch(Exception e)
    	{
    		//e.printStackTrace();
    	}
	}
	
	public static void main (String[] args) throws Exception
    {
    	//System.setOut(new PrintStream("output.txt"));
    	formatInput();
    }
}