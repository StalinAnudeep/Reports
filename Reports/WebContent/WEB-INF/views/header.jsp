<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!doctype html>
<html lang="en" dir="ltr">
  <head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <meta http-equiv="Content-Language" content="en" />
    <meta name="msapplication-TileColor" content="#2d89ef">
    <meta name="theme-color" content="#4188c9">
    <meta name="apple-mobile-web-app-status-bar-style" content="black-translucent"/>
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="mobile-web-app-capable" content="yes">
    <meta name="HandheldFriendly" content="True">
    <meta name="MobileOptimized" content="320">
    <meta http-equiv="Content-Security-Policy">
    <link rel="icon" href="./demo/brand/cpdcl-logo.jpg" type="image/x-icon"/>
    <!-- <link rel="shortcut icon" type="image/x-icon" href="./favicon.ico" /> -->
    <!-- Generated: 2019-04-04 16:55:45 +0200 -->
    <title>HT_MIS_REPORTS</title>
    <!-- <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css"> -->
    <!-- <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Source+Sans+Pro:300,300i,400,400i,500,500i,600,600i,700,700i&amp;subset=latin-ext"> -->
    <script src="./assets/js/require.min.js"></script>
    <script>
      requirejs.config({
          baseUrl: '.'
      });
    </script>
    <!-- Dashboard Core -->
    <link href="./assets/css/dashboard.css" rel="stylesheet" />    
    <link href="./assets/css/datepicker.css" rel="stylesheet" />
    <script src="./assets/js/dashboard.js"></script>
    <link href="./assets/plugins/charts-c3/plugin.css" rel="stylesheet" />
    <script src="./assets/plugins/charts-c3/plugin.js"></script>
    <script src="./assets/plugins/input-mask/plugin.js"></script>
    <script src="./assets/plugins/datatables/plugin.js"></script>
    <script src="./assets/plugins/fullcalendar/plugin.js"></script>
<script>
	requirejs([ 'jquery' ], function($) {
		$(document).ready(function() {
			 clockUpdate();
			  setInterval(clockUpdate, 1000);
			 $("#search").on("keyup", function() {
				    var value = $(this).val().toLowerCase();
				    $(".table tr").filter(function() {
				      $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
				    });
				  });
			 
		});
		
		
			function clockUpdate() {
			  var date = new Date();
			  $('.digital-clock').css({'color': 'blue', 'text-shadow': '0 0 6px #ff0'});
			  function addZero(x) {
			    if (x < 10) {
			      return x = '0' + x;
			    } else {
			      return x;
			    }
			  }

			  function twelveHour(x) {
			    if (x > 12) {
			      return x = x - 12;
			    } else if (x == 0) {
			      return x = 12;
			    } else {
			      return x;
			    }
			  }

			  var h = addZero(twelveHour(date.getHours()));
			  var m = addZero(date.getMinutes());
			  var s = addZero(date.getSeconds());

			  $('.digital-clock').text(h + ':' + m + ':' + s)
			}
			
		
	});
</script>
 <style>
 sup {
    vertical-align: super;
    font-size: smaller;
}
 
 </style>   

  </head>
 <!--  Headern started -->
  <body class="">
	<div class="page">
		<div class="flex-fill">
			<div class="header py-4">
				<div class="container">
					<div class="d-flex">
						<a class="header-brand" href="home"> <img
							src="./demo/brand/logo1.png">
						</a>
						<div class="d-flex order-lg-2 ml-auto">
							<!-- <div class="dropdown d-none d-md-flex">
								<a class="nav-link icon" data-toggle="dropdown"> <i
									class="fe fe-bell"></i> <span class="nav-unread"></span>
								</a>
							</div> -->
							<div class="dropdown">
								<a href="#" class="nav-link pr-0 leading-none"
									data-toggle="dropdown"> <span class="avatar"
									style="background-image: url(./demo/brand/cpdcl-logo.jpg)"></span>
									<span class="ml-2 d-none d-lg-block"> <span
										class="text-default">${pageContext.request.userPrincipal.name}</span> <small
										class="text-muted d-block mt-1"></small>
								</span>
								</a>
								<div
									class="dropdown-menu dropdown-menu-right dropdown-menu-arrow">
									<a class="dropdown-item" href="http://10.64.2.14:8080/EBS/home"> <i
										class="dropdown-icon fe fe-log-out"></i> Sign out
									</a>
								</div>
								<div class="digital-clock">00:00:00</div>
							</div>
						</div>
						<a href="#" class="header-toggler d-lg-none ml-3 ml-lg-0"
							data-toggle="collapse" data-target="#headerMenuCollapse"> <span
							class="header-toggler-icon"></span>
						</a>
					</div>
				</div>
			</div>
			
				<div class="header collapse d-lg-flex p-0" id="headerMenuCollapse">
				<div class="container">
					<div class="row align-items-center">
						
						 <div class="col-lg-3 ml-auto">
							<form class="input-icon my-3 my-lg-0">
								<input type="search" class="form-control  on-page-search " tabindex="1" id="search">
								<div class="input-icon-addon">
									<i class="fe fe-search"></i>
								</div>
							</form>
						</div>
						<div class="col-lg order-lg-first">
							<ul class="nav nav-tabs border-0 flex-column flex-lg-row">
								<li class="nav-item"><a href="home"
									class="nav-link active"><i class="fe fe-home"></i> Home</a></li>
							
						
							<!-- 	<li class="nav-item dropdown"><a href="javascript:void(0)"
									class="nav-link" data-toggle="dropdown"><i
										class="fe fe-file"></i> Reports</a>
									<div class="dropdown-menu dropdown-menu-arrow" style="width: max-content;">
										<div class="row">
											<div class="col-3">
												<span class="dropdown-item text-success">Reports</span>
												<a href="#" class="dropdown-item ">Third Party Sales Confirmation</a>
												<a href="accountCopy" class="dropdown-item ">Account Copy Report</a> 
												<a href="CategoryWiseDemandReport" class="dropdown-item ">Category Wise Demand Report</a>
												<a href="masterchangereport"  class="dropdown-item">Change History HT</a>
												<a href="DemandReport" class="dropdown-item">Demand Report</a>
												<a href="circelwisedemandreport" class="dropdown-item">Circle Wise Demand Report</a>
												<a href="consumptionVariation" class="dropdown-item ">HT Variation In Consumption Report</a> 
												<a href="SectionWiseArrears" class="dropdown-item ">Section Wise Arrears Report</a>
												<a href="acdReport" class="dropdown-item ">ACD Report</a>
												<a href="hello" class="dropdown-item ">SINGLE LINE ISD REPORT</a>
											</div>
											<div class="col-3">
												<span class="dropdown-item text-success">Some links</span>
												<a href="./profile.html" class="dropdown-item ">Profile</a> 
												<a href="./login.html" class="dropdown-item ">Login</a> 
												<a href="./register.html" class="dropdown-item ">Register</a> 
												<a  href="javascript:void(0);" class="dropdown-item text-success">Some links</a>
												<a href="./profile.html" class="dropdown-item ">Profile</a> 
												<a href="./login.html" class="dropdown-item ">Login</a> 
												<a href="./register.html" class="dropdown-item ">Register</a> 
												<a href="./forgot-password.html" class="dropdown-item ">Forgot password</a>
				
											</div>
											<div class="col-3">
												<span class="dropdown-item text-success">Some links</span>
												<a href="./profile.html" class="dropdown-item ">Profile</a> 
												<a href="./login.html" class="dropdown-item ">Login</a> 
												<a href="./register.html" class="dropdown-item ">Register</a> 
												<a href="./forgot-password.html" class="dropdown-item ">Forgot password</a>
												<span class="dropdown-item text-success">Some links</span>
												<a href="./profile.html" class="dropdown-item ">Profile</a> 
												<a href="./login.html" class="dropdown-item ">Login</a> 
												<a href="./register.html" class="dropdown-item ">Register</a> 
												<a href="./forgot-password.html" class="dropdown-item ">Forgot password</a>
												
											</div>
											<div class="col-3">
												<span class="dropdown-item text-success">Some links</span>
												<a href="./profile.html" class="dropdown-item ">Profile</a> 
												<a href="./login.html" class="dropdown-item ">Login</a> 
												<a href="./register.html" class="dropdown-item ">Register</a> 
												<a href="./forgot-password.html" class="dropdown-item ">Forgot password</a>
									
											</div>
										</div>
										
									</div>
									
								</li> -->
						
							</ul>
						</div> 
						
					
						
					</div>
				</div>
			</div>

   <br>
  
	<div class="container">
	
<!-- <script> 
	requirejs([ 'jquery' ], function($) {
			$("td,th").each(function() { 
				if ($.isNumeric( $(this).text())) {
				    // It isn't a number	
				    $(this).html(parseFloat($(this).text()).toLocaleString('en-IN', {style: 'decimal', currency: 'INR'})); 
				}
			}
				
				
			)
			
	});
</script> -->
	