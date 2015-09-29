<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="app.* , java.sql.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.text.NumberFormat"%>
<%@page import="servlet.GetDataFromUrl"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>ADEATER</title>
<script type="text/javascript" src="../js/jquery-1.4.2.min.js"></script>
<script type="text/javascript" src="../js/jquery-ui-1.8rc3.custom.min.js"></script>
<link href="../css/jquery-ui-1.8rc3.custom.css" rel="stylesheet"/>
<script type="text/javascript">
$(function(){
	$(".button").button();
});
function deleteLink(id)
{
	if(confirm("Are You Sure ?"))
	{
		$.post("Delete.jsp",{type: 'link', linkid : id },function(){
			$("#l"+id).fadeOut(1000, function(){
				$("#l"+id).remove();
			});
		});
	}
}
</script>
<style type="text/css">
.button
{
font-size: 10px;
}
label, input, .pointer
{
cursor: pointer;
}
</style>
</head>
<body>
<%
Connection conn=connection.DBConnection.connect();
Statement st=conn.createStatement();
ResultSet rs;

String pageid=request.getParameter("pageid");
String pg=request.getParameter("pageno");
long pageno=1;
long RECS=10;

try{ pageno=Long.parseLong(pg); }
catch(Exception e){ }

long low=(pageno-1)*RECS+1;
long high=pageno*RECS;

String baseurl="";
rs=st.executeQuery("select baseurl from page where pageid="+pageid);
if(rs.next())
	baseurl=rs.getString("baseurl");

rs=st.executeQuery("select * from link where pageid="+pageid+" order by linkid asc");
long ct=1;
%>
<form method="post" action="TrainSVM.jsp"><input type="hidden" name="pageid" value="<%=pageid %>"/>
Base URL : <%= baseurl%>
<table width="99%" border="1" cellspacing="0" cellpadding="0">
<tr class="ui-widget-header ui-corner-all">
<th width="10%">Linkid</th>
<th width="20%">Target URL</th>
<th width="5%">Caption</th>
<th width="5%">Image ALT</th>
<th width="20%">Image SRC</th>
<th width="5%">Height</th>
<th width="5%">Width</th>
<th width="5%">Aspect Ratio</th>
<th width="5%">AD / NON-AD</th>
</tr>
<%
NumberFormat nf=NumberFormat.getInstance();
nf.setMaximumFractionDigits(3);
long linkid=0;
boolean found=false;
String imgsrc="";
while(rs.next())
{
	if(ct>=low && ct<=high)
	{
		found=true;	
		linkid=rs.getLong("linkid");
		imgsrc=ADBiteUtil.getCompleteUrl(baseurl, rs.getString("imgsrc"));
		out.print("<tr id='l"+linkid+"' onclick='$(\"#limg"+linkid+"\").toggle();' class='pointer' title='Click to View the Image'>"+
					"<td width='10%'>"+linkid+"</td>"+
					"<td width='25%'>"+rs.getString("targeturl")+"</td>"+
					"<td width='5%'>"+rs.getString("caption")+"</td>"+
					"<td width='5%'>"+rs.getString("imgalt")+"</td>"+
					"<td width='25%'>"+imgsrc+"</td>"+
					"<td align='center' width='5%'>"+nf.format(rs.getDouble("height"))+"</td>"+
					"<td align='center' width='5%'>"+nf.format(rs.getDouble("width"))+"</td>"+
					"<td align='center' width='5%'>"+nf.format(rs.getDouble("aspectratio"))+"</td>"+
					"<td align='center' width='5%'><input type='hidden' name='linkid' value='"+linkid+"'/><input type='checkbox' name='ads' value='"+linkid+"' "+(rs.getString("type").equals("ad")?"checked='checked'":"")+"/></td>"+
					"<td align='center' width='10%'><input type='button' class='button' value='Delete' onclick='deleteLink("+linkid+")'/></td>"+
				  "</tr><tr><td colspan='10' id='limg"+linkid+"' style='display: none;'><img src='"+imgsrc+"'/></td></tr>");
	}
	ct++;
}
if(found){
if(low>1 && ct>0)
	out.println("<tr><td colspan='10' align='right'><a href='ShowLinks.jsp?pageid="+pageid+"&pageno="+(pageno-1)+"' class='button'>Previous</a>");

if(ct>high)
{
	if(low==1)
		out.println("<tr><td colspan='10' align='right'><a href='ShowLinks.jsp?pageid="+pageid+"&pageno="+(pageno+1)+"' class='button'>Next</a></td></tr>");
	else
		out.println("| <a href='ShowLinks.jsp?pageid="+pageid+"&pageno="+(pageno+1)+"' class='button'>Next</a></td></tr>");
}
%>
<tr><td colspan="10" align="center"><input type="hidden" id="pageno" name="pageno" value="<%=pageno%>"/><input type="hidden" id="save" name="save" value="0"/>
<a href="Delete.jsp?pageid=<%=pageid%>&type=link&linkid=all" onclick="return confirm('ARE YOU SURE ?');" class="button">Delete All</a> &nbsp;
<input type="submit" class='button' value="Save" onclick="$('#save').val(1); return true;"/>
<input type="submit" class='button' value="Move to Training Page" onclick='window.location.href="TrainSVM.jsp";'/>
</td>
</tr>
<%
}
else
{%><tr><td colspan="10" align="center">No Records Found</td></tr><%}
conn.close();
%>
</table>
</form>
<a href="ShowPages.jsp">Back to List of Pages</a>
</body>
</html>