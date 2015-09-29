<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="app.* , java.sql.* , java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Train SVM</title>
<script type="text/javascript" src="../js/jquery-1.4.2.min.js"></script>
<script type="text/javascript" src="../js/jquery-ui-1.8rc3.custom.min.js"></script>
<link href="../css/jquery-ui-1.8rc3.custom.css" rel="stylesheet"/>
<script type="text/javascript">
$(function(){
	$("#dialogdiv").dialog({
		position: ['center','200'],
		autoOpen : false,
		modal : true,
		resizable: false});
	$("#progessbar").progressbar({
		value: 100
	});
	$("#starttraining").button();
	$("#starttraining").click(function(){
			$("#dialogdiv").dialog('open');
			$.get("../Training" ,function(){
				$("#dialogdiv").dialog('close');
			});
	});
});
</script>
<style type="text/css">
.ui-progressbar-value { background-image: url(../css/images/pbar-ani.gif); }
</style>
</head>
<body>
<%
Connection conn=connection.DBConnection.connect();
Statement st=conn.createStatement();
String action=request.getParameter("action");
String pageid=request.getParameter("pageid");
String save=request.getParameter("save");
if(action!=null && action.equals("delete"))
{
	st.execute("delete from link where pageid="+pageid);
	st.execute("delete from page where pageid="+pageid);
	response.sendRedirect("AddLinks.jsp");
}
else
if(save!=null && save.equals("1"))
{
String ads[]=request.getParameterValues("ads");
String linkid[]=request.getParameterValues("linkid");
String pg=request.getParameter("pageno");

String allads="-1";

long pageno=1;
long RECS=10;

try{ pageno=Long.parseLong(pg); }
catch(Exception e){ }

long low=(pageno-1)*RECS+1;
long high=pageno*RECS;

if(linkid!=null)
{
	for(int i=0;i<linkid.length;i++)
	{
		allads+=linkid[i]+(i==(linkid.length-1)?"":", ");
	}
	st.execute("update link set type='non_ad' where linkid in ("+allads+")");
}

if(ads!=null)
{
allads="";
for(int i=0;i<ads.length;i++)
{
	allads+=ads[i]+(i==(ads.length-1)?"":", ");
}
st.execute("update link set type='ad' where linkid in ("+allads+")");
}
response.sendRedirect("ShowLinks.jsp?pageid="+pageid+"&pageno="+pageno);
}
conn.close();
%>
<input type="button" id="starttraining" class="button" value="Start Training"/>
<div id="dialogdiv" title="Training SVM">
Training..........
<div id="progessbar" title="Training"></div>
</div>
</body>
</html>