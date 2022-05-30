<!DOCTYPE html>
<html>
<head>
<title>Reset password with TaxiApp</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<style type="text/css">
body {
 font-family: Verdana, Geneva, sans-serif;
 font-size: 12px;
 color: #464646;
}

p,h1,h2,h3,h4,h5,h6 {
 margin: 0;
 padding: 0;
}

ul {
 margin: 0;
 padding: 0;
 list-style: none;
}

img {
 margin: 0;
 padding: 0;
 border: none;
}

p {
 font-size: 12px;
 margin: 0;
 padding: 15px 0 0 0;
 line-height: 18px;
 text-align: justify;
}

ol {
 margin: 0;
 padding: 5px 0 5px 20px;
 font-size: 12px;
 line-height: 18px;
}

ol li,ul li {
 margin: 0;
 padding: 0;
 line-height: 18px;
 font-size: 12px;
}

ul {
 margin: 0;
 padding: 5px 0 5px 20px;
 list-style: disc;
}

a {
 color: #09F;
 text-decoration: none;
}

a:hover {
 color: #09f;
 text-decoration: underline;
}


#Table_01
{
	border:3px solid rgb(51, 122, 183);
	padding: 0px; 
	width: 650px; 
	height: 350px;
}
.mailBottom
{
	background:rgba(51, 122, 183, 0.9);;
	padding:10px;
}
.cellContent
{
	padding:8px;
}
.mailTitle
{
	color:#CCCCCC;
	font-size:24px;
	padding:14px 8px;
	background:url("taxi.png") no-repeat 98%; 
	background-size:32px;
}

</style>
</head>


<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0" marginwidth="0"
 marginheight="0">
 <!-- Save for Web Slices (Untitled-1) -->
 <table id="Table_01" align="center" cellpadding="0" cellspacing="0" style="border: solid 1px #AAA;">
 
  <tr style="background:rgb(51, 122, 183)">
   <td><!--<img src="taxi.png" width="120" style="padding:10px 8px">-->
   <h1 class="mailTitle"> TAXI DEAL </h1>
    
   </td>
  </tr>

  <!--<tr>
   <td colspan="3">
   <hr style="  border-top: 1px solid #337AB7;">
  </tr>-->

  <tr>
   <td colspan="3" class="cellContent">
    <p align="justify">Dear User,</p>
    <br/>
    <p align="justify">Please click on the link below to reset your password with us.</p>
    <p align="justify"><a href="${user.verifyUrl}">${user.verifyUrl}</a></p>
    <br/> 
    <p align="justify">If the above link doesn't work please copy and paste the following url in you browser directly.</p>
    <p align="justify">${user.verifyUrl}</p>
    <br/>

    <p>Thanks &amp; Regards,<br/>
    Team TaxiApp</p>
   </td>
  </tr>
  <br/>
	<tr class="mailBottom">
		<td>	
		<p style="text-align:center; padding-bottom:8px;color:#fff;">Click on this url to have a look at our <a href="http://taxifordeal.appspot.com/privacy.html" style="color:#ccc">Privacy Policy</a> </p>
		</td>
	</tr>

 </table>
 <!-- End Save for Web Slices -->
</body>
</html>