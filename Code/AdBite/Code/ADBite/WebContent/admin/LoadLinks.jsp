<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="app.* , java.util.*, java.text.*, java.net.*"%>
<%
	ADBiteUtil.reset();

String url=request.getParameter("url");
if(url!=null){
Page p=new Page(URLDecoder.decode(url).replace(" ","%20"),true);

if(p.atags.size()>0){
	out.println("<input type='hidden' name='pageid' value='"+p.pageid+"'>");
%>
<table width="100%" border="1" cellspacing="0" cellpadding="0">
<tr>
<th width="20%">Title</th>
<th width="30%">Target URL</th>
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
Iterator<Long> it=p.atags.keySet().iterator();
long linkid=0;
Link l=null;
while(it.hasNext())
{
	linkid=it.next();
	l=p.atags.get(linkid);
	out.print("<tr>"+
				"<td>"+l.aTitle+"</td>"+
				"<td>"+l.targetURL+"</td>"+
				"<td>"+l.aCaption+"</td>"+
				"<td>"+l.imgAlt+"</td>"+
				"<td>"+l.imgSrc+"</td>"+
				"<td align='center'>"+nf.format(l.height)+"</td>"+
				"<td align='center'>"+nf.format(l.width)+"</td>"+
				"<td align='center'>"+nf.format(l.aspectratio)+"</td>"+
				"<td align='center'>"+
					"<input type='checkbox' name='ads' value='"+l.linkid+"'/>"+
				"</td>"+
			  "</tr>");
}
%>
<tr><td colspan="9" align="center">
<input type="submit" class='button' value="DISCARD" onclick="$('#action').val('delete'); return true;"/></td></tr>
</table>
<%
}
}%>