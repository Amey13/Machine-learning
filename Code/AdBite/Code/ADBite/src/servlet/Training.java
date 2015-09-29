package servlet;

import java.io.*;
import java.util.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import app.*;

/**
 * Servlet implementation class Training
 */
public class Training extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Training() {
        super();
        // TODO Auto-generated constructor stub
    }

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
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		try
		{
			System.out.println("Training Started...");
			//Reset FeatureSets
			ADBiteUtil.reset();
	
			Connection conn=connection.DBConnection.connect();
			Statement st=conn.createStatement();
			ResultSet rs=null;
	
			Statement st2=conn.createStatement();
			st2.execute("delete from linkfeature");
			ResultSet rs2=st2.executeQuery("select * from page where train=1");
	
			Page p=null;
			HashMap<Long,Page> pages=new HashMap<Long,Page>(10,10);
			long pageid,linkid;
			int height,width,local,i=0;
			double aspectratio;
			String targeturl,caption,imgalt,imgsrc,baseurl;
			while(rs2.next())
			{
				pageid=rs2.getLong("pageid");
				baseurl=rs2.getString("baseurl");
				p=new Page(pageid,baseurl);
	
				rs=st.executeQuery("select * from link where pageid="+pageid);
				i=0;
				while(rs.next())
				{i++;
					linkid=rs.getLong("linkid");
					targeturl=rs.getString("targeturl");
					caption=rs.getString("caption");
					local=rs.getInt("local");
					height=rs.getInt("height");
					width=rs.getInt("width");
					aspectratio=rs.getDouble("aspectratio");
					imgalt=rs.getString("imgalt");
					imgsrc=rs.getString("imgsrc");
					
					p.put(linkid,new Link(linkid,pageid,baseurl,targeturl,caption,imgalt,imgsrc,height,width,aspectratio,local));
				}
				System.out.println("Link :"+baseurl+" ->> "+i);
				rs.close();
	
				pages.put(pageid,p);
			}
	
			ADBiteUtil.saveFrequentFeatureset();
	
			//Release featureset
			ADBiteUtil.reset();

			long featureid;
			String featurename;
			rs=st.executeQuery("SELECT linkid,pageid,featureid,featurename FROM (select * from link NATURAL JOIN page where train=1) as temp,feature");
			System.setOut(new PrintStream("C:\\queries.sql"));
			System.out.println("INSERT INTO linkfeature (linkid,featureid,value) VALUES ");
			while(rs.next())
			{
				linkid=rs.getLong("linkid");
				pageid=rs.getLong("pageid");
				featureid=rs.getLong("featureid");
				featurename=rs.getString("featurename");
				boolean value=false;
				
				//System.out.println("pages has "+pageid+" -> "+pages.containsKey(pageid));
				if(pages.containsKey(pageid))
				{
					//System.out.println(pageid+" has "+linkid+" -> "+pages.get(pageid).atags.containsKey(linkid));
					if(pages.get(pageid).atags.containsKey(linkid))
						value=pages.get(pageid).atags.get(linkid).usefulpart.contains(featurename);
				}
				
				if(value)
					//System.out.println("insert into linkfeature VALUE("+linkid+" ,"+featureid+" ,1 );");
					System.out.println("("+linkid+" ,"+featureid+" ,1 ),");
				else
					//System.out.println("insert into linkfeature VALUE("+linkid+" ,"+featureid+" ,0 );");
					System.out.println("("+linkid+" ,"+featureid+" ,0 ),");
			}
			//st2.executeBatch();
			conn.close();
			System.out.println("Training Complete");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}