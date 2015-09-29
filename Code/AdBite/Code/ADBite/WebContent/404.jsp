
<%@page import="app.ADBiteUtil"%><%
try{
response.setStatus(200);

ErrorData ed= pageContext.getErrorData();

String query="",paramName="",paramValue="";
java.util.Enumeration en=request.getParameterNames();
while(en.hasMoreElements())
{
	paramName=(String)en.nextElement();
	paramValue=request.getParameter(paramName);
	query+=paramName+"="+paramValue+"&";
}

String ruri=ed.getRequestURI();
String file=ruri.substring("/".length());
session.setAttribute("error","1");
String requrl=ADBiteUtil.getCompleteUrl((String)session.getAttribute("BASE_URL"),file)+(query.trim().equals("")?"":"?"+query);
System.out.println("IN FILTER FILE '"+ruri+"' , query="+query+" NOT FOUND!!!!!!!!!!");
System.out.println("Base URL : "+(String)session.getAttribute("BASE_URL")+" , Now trying : "+requrl);
session.setAttribute("url", requrl);

String postData="",key;
java.util.Map params=request.getParameterMap();
java.util.Iterator it=params.keySet().iterator();
while(it.hasNext())
{
	key=(String)it.next();
	if(!key.equals("url"))
	postData+=key+"="+request.getParameter(key)+"&";
}
if(postData!=null && postData.length()>0 && postData.charAt(postData.length()-1)=='&')
{
	postData=postData.substring(0,postData.length()-1);
}
System.out.println("Post DATA [404.jsp]: "+postData);

request.getRequestDispatcher("GetDataFromUrl").forward(request, response);
}
catch(Exception e){}
%>