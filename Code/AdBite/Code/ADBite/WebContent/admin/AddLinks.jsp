<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Add Links</title>
<script type="text/javascript" src="../js/jquery-1.4.2.min.js"></script>
<script type="text/javascript" src="../js/jquery-ui-1.8rc3.custom.min.js"></script>
<link href="../css/jquery-ui-1.8rc3.custom.css" rel="stylesheet"/>
<script type="text/javascript">
$(function(){
	$("#loading_dialog").dialog({
			autoOpen : false , 
			modal : true });
	$("#linkstable").ajaxStart(function(){ $("#loading_dialog").dialog('open'); });
	$("#linkstable").ajaxStop(function(){ $("#loading_dialog").dialog('close'); });
	$("#linkstable").ajaxError(function(request, settings){ alert("An Error has occured. Please try again"); });
	$(".button").button();
});
function fetchImages()
{
	var u=$("#url").val();
	u=$.trim(u);
	if(u!="")
	{
		u=encodeURIComponent(u);
		$("#linkstable").load("LoadLinks.jsp", { url: u }, function(){ $(".button").button(); });
	}
}
</script>
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
<%

%>
<form method="post" action="TrainSVM.jsp">
<input type="hidden" id="action" name="action" value="none"/>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
<tr>
<td colspan="2"><a href="ShowPages.jsp" class='button' target="_new">Show All Pages Added for Training</a></td>
</tr>
<tr><td><label for="url">Enter URL that contains ads : </label></td><td><input type="text" size="100" id="url" class="ui-corner-all"><input type="button" value="FETCH LINKS WITH IMAGES" class="button" onclick="fetchImages();"/></td></tr>
<tr><td id="linkstable" colspan="2"></td></tr>
</table>
</form>
<div id="loading_dialog" title="Loading">
<h3>Loading.....</h3>
</div>
</body>
</html>