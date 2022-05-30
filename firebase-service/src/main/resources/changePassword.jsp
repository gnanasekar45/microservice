+<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en">
<head>
      <title>Get Current Location </title>
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no">

  
  <meta charset="utf-8">

  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
  <link rel="stylesheet" href="taxi.css">
   <style>
      /* Always set the map height explicitly to define the size of the div
       * element that contains the map. */
      #map {
        height: 100%;
      }
      /* Optional: Makes the sample page fill the window. */
      html, body {
        height: 100%;
        margin: 0;
        padding: 0;           
      }
    </style>
 
</head>
<body>

<nav class="navbar navbar-inverse">
  <div class="container-fluid">
    <div class="navbar-header">
      <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#myNavbar">
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>                        
      </button>
      <a class="navbar-brand" href="#"><img src="../images/logonewwhite.png" width="100px"></a>
    </div>
    <div class="collapse navbar-collapse" id="myNavbar">
      <ul class="nav navbar-nav navbar-right">
		 <li class="sign-in"><a href="#">Sign In</a></li>
        <li><a href="#">Sign Up</a></li>
		
		<li class="mobile-icon"><a href="#"><img src="united-kingdom-flag-icon.png"><span>English</span></a></li>
		<li class="mobile-icon"><a href="#"><img src="germany-flag-icon.png"><span>Germany</span></a></li>
		<li class="mobile-icon"><a href="#"><img src="france-flag-icon.png"><span>France</span></a></li>
		<li class="mobile-icon"><a href="#"><img src="italy-flag-icon.png"><span>Italian</span></a></li>
		   
	   
		 <li class="dropdown">
          <a class="dropdown-toggle" data-toggle="dropdown" href="#"><img class="default-united-kingdom-flag" src="united-kingdom-flag-icon.png"></a>
          <ul class="dropdown-menu">
            <li><a href="#"><img src="united-kingdom-flag-icon.png"><span>English</span></a></li>
			<li><a href="#"><img src="germany-flag-icon.png"><span>Germany</span></a></li>
			<li><a href="#"><img src="france-flag-icon.png"><span>France</span></a></li>
			<li><a href="#"><img src="italy-flag-icon.png"><span>Italian</span></a></li>
			
           
          </ul>
        </li>
		
		
      </ul>
    </div>
  </div>
</nav>

  <div class="right-side-location">


  

    <div class="form-group pwd-section">
       <span style="color:#900;" id="successsub"></span>
      <input type="password" class="form-control" id="pwd" placeholder="Enter password"><br>
       <span style="color:#900;" id="error1"></span>
      
    </div><br>
    <div class="form-group pwd-section">
      
      <input type="password" onChange="checkPasswordMatch();" class="form-control" id="cpwd" placeholder="Enter Confirm password">
     <br>
      <span  id="error2"></span>
	     
    </div>

     <div>
              <input type="hidden" name="userToken" id="userToken" value=${resetKey}>
              <input type="hidden" name="email" id="email" value=${email}>
      </div>

	<div class="form-group pwd-section">
      <button type="button" id="sub_btn" onclick="loadDoc('/rest/ct?action=', myFunction)" class="btn btn-default get-location-btn">
      SUBMIT DATA</button>
    </div>
	         
	<div class="form-group pwd-section">
      <button type="button" id="geo_btn" onclick="getgeo()" class="btn btn-default get-location-btn">Get Location</button>
    </div>	

     
    
 
</div>
<br><br>
  	
    <div id="map"></div>
   
    <script>
      // Note: This example requires that you consent to location sharing when
      // prompted by your browser. If you see the error "The Geolocation service
      // failed.", it means you probably did not give permission for the browser to
      // locate you.

      function initMap() {
        var map = new google.maps.Map(document.getElementById('map'), {
          center: {lat: -34.397, lng: 150.644},
          zoom: 18
        });
        var infoWindow = new google.maps.InfoWindow({map: map});

        // Try HTML5 geolocation.
        if (navigator.geolocation) {
          navigator.geolocation.getCurrentPosition(function(position) {
            var pos = {
              lat: position.coords.latitude,
              lng: position.coords.longitude
            };

            infoWindow.setPosition(pos);
            infoWindow.setContent('Location found.');
            map.setCenter(pos);
          }, function() {
            handleLocationError(true, infoWindow, map.getCenter());
          });
        } else {
          // Browser doesn't support Geolocation
          handleLocationError(false, infoWindow, map.getCenter());
        }
      }

      function handleLocationError(browserHasGeolocation, infoWindow, pos) {
        infoWindow.setPosition(pos);
        infoWindow.setContent(browserHasGeolocation ?
                              'Error: The Geolocation service failed.' :
                              'Error: Your browser doesn\'t support geolocation.');
      }
    </script>
    <script async defer
    src="https://maps.googleapis.com/maps/api/js?key=AIzaSyAPLlMTCQ2mWRrh9lQkQhbs16jKx9OvHW4">
    </script>
	  

	  


  



 <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
	
	<script>

	 function checkPasswordMatch() {
        var password = $("#pwd").val();
        var confirmPassword = $("#cpwd").val();
        if (password != confirmPassword)
            $("#error2").html('<p style="color:#900;font-weight: 600;">Passwords do not match!</p>');
        else
            $("#error2").html('<p style="color:#11d235;font-weight: 600;"">Passwords match!</p>');
     }

    $(document).ready(function () {
            $("#pwd").keyup(function( event ) {
            $("#cpwd").val('');
            $("#error2").html('');
            });
     });
    $(document).ready(function () {
    $("#cpwd").keyup(checkPasswordMatch);
    });
		
		
		
		
function submitdata()
{    
	var password = $("#pwd").val();
    var confirmPassword = $("#cpwd").val();
    if (password != confirmPassword )
    {
            alert("PASSWORD NOT MATCH");
    }
    else if (password == "" || confirmPassword == "" )
    {
           alert("BOTH FIELDS MUST NOT BE BLANCK");
    }
    else
    {
            $("#pwd").remove();;
            $("#cpwd").remove();

            $("#sub_btn").remove();
            $("#geo_btn").remove();
            $("#error2").remove();

            $("#successsub").html('<p style="color:#11d235;font-weight: 600;"">successfully saved the password!</p>');

    }
 }

 function loadDoc(url, cfunc) {

        alert("1 " + url);
        alert("12 " + document.getElementById("pwd").value);
        var pass = document.getElementById("cpwd").value;
        var xhttp;

          xhttp=new XMLHttpRequest();
          xhttp.onreadystatechange = function() {
            if (this.readyState == 4 && this.status == 200) {
              cfunc(this);
            }
          };

        alert("134 -- " + key1);

      var key1 = document.getElementById("userToken").value;
            alert("3 -- " + key1);
      var email1 = document.getElementById("email").value;
            alert("4 -- " + email);

      <!-- xhttp.open("GET", url+pass+id, true); -->
      <!-- xhttp.open("GET",pass+"&"+id, true); -->

      xhttp.open("GET", url+pass+"&token="+key1+"&email="+email1,true);
      xhttp.send();
    }

    function myFunction(xhttp) {
      document.getElementById("demo").innerHTML = xhttp.responseText;
      alert(xhttp.responseText);
    }


		
		
function getgeo()
		{
			initMap();
		}
		
		
		
		
		
		
		
		
		
		
		
		

	 
	</script>
</body>
</html>
