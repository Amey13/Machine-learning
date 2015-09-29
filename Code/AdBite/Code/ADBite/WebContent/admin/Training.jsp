<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="app.* , java.sql.* , java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<%
	//Reset FeatureSets
ADBiteUtil.reset();

Connection conn=connection.DBConnection.connect();
Statement st=conn.createStatement();
ResultSet rs=null;

Statement st2=conn.createStatement();
st2.execute("delete from linkfeature");
ResultSet rs2=st2.executeQuery("select * from page");

Page p=null;
Link l=null;
HashMap<Long,Page> pages=new HashMap<Long,Page>(10,10);
long pageid,linkid;
int height,width,local;
double aspectratio;
String targeturl,caption,imgalt,imgsrc,baseurl;
while(rs2.next())
{
	pageid=rs2.getLong("pageid");
	baseurl=rs2.getString("baseurl");
	p=new Page(pageid,baseurl);

	rs=st.executeQuery("select * from link natural join page");
	
	while(rs.next())
	{
		linkid=rs.getLong("linkid");
		targeturl=rs.getString("targeturl");
		caption=rs.getString("caption");
		local=rs.getInt("local");
		height=rs.getInt("height");
		width=rs.getInt("width");
		aspectratio=rs.getDouble("aspectratio");
		imgalt=rs.getString("imgalt");
		imgsrc=rs.getString("imgsrc");
		
		p.put(new Link(linkid,pageid,baseurl,targeturl,caption,imgalt,imgsrc,height,width,aspectratio,local));
	}
	rs.close();

	pages.put(pageid,p);
}

ADBiteUtil.saveFrequentFeatureset();

//Release featureset
ADBiteUtil.reset();

long featureid;
String featurename;
rs=st.executeQuery("select * from link,feature");
while(rs.next())
{
	linkid=rs.getLong("linkid");
	pageid=rs.getLong("pageid");
	featureid=rs.getLong("featureid");
	featurename=rs.getString("featurename");
	
	if(pages.get(pageid).atags.get(linkid).usefulpart.contains(featurename))
		st2.execute("insert into linkfeature VALUE("+linkid+" ,"+featureid+" ,1 )");
	else
		st2.execute("insert into linkfeature VALUE("+linkid+" ,"+featureid+" ,0 )");
}

conn.close();
%>

</body>
</html>