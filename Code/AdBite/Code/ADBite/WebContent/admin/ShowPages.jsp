<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="app.* , java.sql.* , java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>List of Pages in Training</title>
<script type="text/javascript" src="../js/jquery-1.4.2.min.js"></script>
<script type="text/javascript" src="../js/jquery-ui-1.8rc3.custom.min.js"></script>
<link href="../css/jquery-ui-1.8rc3.custom.css" rel="stylesheet"/>
<style type="text/css">
.button
{
font-size: 10px;
}
label, input
{
cursor: pointer;
}
</style>
</head>
<body>
<table width="99%" border="1" cellspacing="0" cellpadding="0">
<tr>
<th>Page ID</th>
<th>Page URL</th>
<th>List of Links from this URL</th>
<th>Delete</th>
</tr>
<%
Connection conn=connection.DBConnection.connect();
Statement st=conn.createStatement();
ResultSet rs=st.executeQuery("select * from page where train=1");
String pageid="";
boolean found=false;
while(rs.next())
{
	found=true;
	pageid=rs.getString("pageid");
	%>
	<tr>
		<td><%=pageid %></td>
		<td><%=rs.getString("baseurl") %></td>
		<td align="center"><a href="ShowLinks.jsp?pageid=<%=pageid%>">SHOW</a></td>
		<td align="center"><a href="Delete.jsp?type=page&pageid=<%=pageid%>" onclick="return confirm('Are you sure ?');">DELETE</a></td>
	</tr>
	<%
}
if(!found)
	out.println("<tr><td colspan='4' align='center'>No Records Found.</td></tr>");
%>
</table>
</body>
</html>