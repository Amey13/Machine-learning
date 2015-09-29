<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="app.* , java.sql.*"%>
<html>
<head>
<title>Delete</title>
</head>
<body>
<%
String type=request.getParameter("type");

if(type!=null)
{
	Connection conn=connection.DBConnection.connect();
	Statement st=conn.createStatement();
	String pageid=request.getParameter("pageid");
	
	if(type.equals("page"))
	{
		st.execute("delete from link where pageid="+pageid);
		st.execute("delete from page where pageid="+pageid);
		response.sendRedirect("ShowPages.jsp");
	}
	else
	if(type.equals("link"))
	{
		String linkid=request.getParameter("linkid");
		
		if(linkid!=null)
		{
			if(linkid.equals("all"))
			{
				st.executeUpdate("delete from link where pageid="+pageid);
				response.sendRedirect("ShowPages.jsp");
			}
			else
				st.executeUpdate("delete from link where pageid="+pageid+" AND linkid="+linkid);
		}			
	}
	conn.close();
}
%>
</body>
</html>