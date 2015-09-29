<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" session="true"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>ADBITE</title>
<script type="text/javascript" src="js/jquery-1.4.2.min.js"></script>
<script type="text/javascript" src="js/jquery-ui-1.8rc3.custom.min.js"></script>
<link href="css/jquery-ui-1.8rc3.custom.css" rel="stylesheet"/>
<style type="text/css">
html
{
overflow: hidden;
}
*
{
font: 14px Verdana;
color: #2E6E9E;
}
label
{
cursor: pointer;
}
#addressbar
{
border: 1px solid black;
display: block;
height: 10%;
width: 98%;
left: 1%;
position: absolute;
top: -1%;
z-index: 999;
padding-top: 1%;
background-color: white;
}
#pageframediv
{
position: absolute;
top: 11%;
display: block;
width: 99%;
}
.loginblock
{
font-size: 8px;
padding-right: 5px;
}
.loginblock a
{
background-color: #EBF5FD;
color: #2E6E9E;
font-weight: 700;
}
.loginblock span
{
padding: 4px;
}
#ui-dialog-title-dialogdiv
{
font-weight: 900;
color: black;
}
</style>
<script type="text/javascript">
$(function(){
	$("#gobutton").button();
	$("#dialogdiv").dialog({
		position: ['center','200'],
		autoOpen : false,
		modal : true,
		height: 400,
		width: 550,
		resizable: false});
	$(".loginblock").find("a").hover(function(){ $(this).addClass("ui-corner-all"); },
									 function(){ $(this).removeClass("ui-corner-all"); });
	$("#pageframe").load(function(){
		var loc=window.frames.pageframe.location.search;
		if(loc.indexOf("?")==0)
			loc=window.decodeURIComponent(loc.substring(5));
		
		$("#gotourl").val(""+loc);
		
		//alert("LOADING : "+$("#pageframe").attr("src"));
		//var ht=$("#pageframe").height();
		//$("#pageframediv").height(ht>650?ht:650);
		//$("#pageframe").height(ht>650?ht:650);
	});
});

function getDataFromUrl()
{
	var u=$("#gotourl").val();
	u=$.trim(u);
	if(u!="")
	{
		var t="GetDataFromUrl?url="+encodeURIComponent(u);
		window.open(t,"pageframe");
	}
	else
		alert("Please enter a URL");
}
function showProfile()
{
	//$("#dialogdiv").dialog("destroy");
	$("#ui-dialog-title-dialogdiv").html('Profile Details');
	$("#dialogdiv").html("<iframe src='ShowProfile.jsp' width='690' height='490' border='0' noborder='true' style='border: none;'><"+"/frame>");
	$("#dialogdiv").dialog({
		position: ['center','50'],
		modal : true,
		height: 550,
		width: 725,
		autoOpen: true
	});
}
</script>
</head>
<body>
<div id="addressbar" class="ui-corner-all">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
<tr><td rowspan="2" width="200"><img src="images/main-logo.png" width="200" height="65"/></td><td colspan="2" align="right" class="loginblock">
<span>
<a id="alogin" href="javascript:void(0);" onclick='$("#dialogdiv").dialog("open");'>About Us</a>
</span></td></tr>
<tr>
<td align="center"><label for="gotourl">Address : </label></td>
<td><input type="text" name="gotourl"  class="ui-corner-all" id="gotourl" size="100" title="Enter URL here" style="background-color: #EAF4FD;"/>&nbsp;<input type="button" value="GO" style="font-size: 11px;" id="gobutton" onclick="getDataFromUrl();"/></td>
</tr>
</table></div>
<div id="pageframediv">
<iframe id="pageframe" name="pageframe" scrolling="auto" frameborder="0" align="top" width="100%" height="650">
</iframe>
</div>
<div id="dialogdiv" title="About Us">
<table align="center">
<tr><td align="center"><img src="images/main-logo.png" width="500" height="200"/></td></tr>
<tr><td align="center">
ADBite.com filters out unwanted ADS from webpages.<br/>
It uses very efficient SVM algorithm to recognize ads.
<br/><br/></td></tr>
<tr><td align="center">Copyrights &copy; Techtycoon 2010</td></tr>
</table>
</div>
</body>
</html>