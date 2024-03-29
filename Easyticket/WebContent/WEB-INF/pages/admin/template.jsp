<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="/common/taglibs.jsp" %>
<html>
<c:if test="${sessionScope.user == null}">
      <c:redirect url="/admin/login"></c:redirect>
</c:if>
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8" />
	<title>Dashboard I Admin Panel</title>
	
	<link rel="stylesheet" href="<s:url value="/css/layout.css" />" type="text/css" media="screen" />
	<!--[if lt IE 9]>
	<link rel="stylesheet" href="css/ie.css" type="text/css" media="screen" />
	<script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
	<![endif]-->
	<script src="<s:url value="/js/jquery.min.js" />" type="text/javascript"></script>
	<script src="<s:url value="/js/hideshow.js" />" type="text/javascript"></script>
	<script src="<s:url value="/js/jquery.tablesorter.min.js" />" type="text/javascript"></script>
	<script type="text/javascript" src="<s:url value="/js/jquery.equalHeight.js" />"></script>
	<script type="text/javascript">
	$(document).ready(function() 
    	{ 
      	  $(".tablesorter").tablesorter(); 
   	 } 
	);
	$(document).ready(function() {

	//When page loads...
	$(".tab_content").hide(); //Hide all content
	$("ul.tabs li:first").addClass("active").show(); //Activate first tab
	$(".tab_content:first").show(); //Show first tab content

	//On Click Event
	$("ul.tabs li").click(function() {

		$("ul.tabs li").removeClass("active"); //Remove any "active" class
		$(this).addClass("active"); //Add "active" class to selected tab
		$(".tab_content").hide(); //Hide all tab content

		var activeTab = $(this).find("a").attr("href"); //Find the href attribute value to identify the active tab + content
		$(activeTab).fadeIn(); //Fade in the active ID content
		return false;
	});

});
    </script>
    <script type="text/javascript">
    $(function(){
        $('.column').equalHeight();
    });
</script>
      
</head>
<body>  
   
     <header id="header">
		<hgroup>
			<h1 class="site_title"><a href="index">Website Admin</a></h1>
			<h2 class="section_title">Dashboard</h2><div class="btn_view_site"><a href="${ctx}/index">View Site</a></div>
		</hgroup>
	</header> <!-- end of header bar -->
	
	<section id="secondary_bar">
		<div class="user">
			<p> <c:out value="${sessionScope.user.fullName}"></c:out>   (<a href="#">3 Messages</a>)</p>
		    <a class="logout_user" href=" <s:url value="logout" /> " title="Logout">Logout</a>
		</div>
		<div class="breadcrumbs_container">
			<article class="breadcrumbs"><a href=" <s:url value="index" /> ">Website Admin</a> <div class="breadcrumb_divider"></div> <a class="current">Dashboard</a></article>
		</div>
	</section><!-- end of secondary bar --> 
	
	<aside id="sidebar" class="column">
		<form class="quick_search" action="https://google.com.vn/search">
			<input type="text" value="Google Search" name="q" onfocus="if(!this._haschanged){this.value=''};this._haschanged=true;">
		</form>
		<hr/>
		<h3>Users</h3>
		<ul class="toggle">
			<li class="icn_add_user"><a href="newUser">Add New User</a></li>
			<li class="icn_view_users"><a href="listUsers">View Users</a></li>
			<li class="icn_security"><a href="roles">Roles</a></li>
			<li class="icn_profile"><a href="profile">Your Profile</a></li>
		</ul>
		<h3>Event Manager</h3>
		<ul class="toggle">
			<li class="icn_new_article"><a href="newEvent">New Event</a></li>
			<li class="icn_categories"><a href="listEvent">View Events</a></li>
			<li class="icn_categories"><a href="types">Event Types</a></li>
			<li class="icn_tags"><a href="seats">Seats</a></li>
		</ul>		
		<h3>System</h3>
		<ul class="toggle">
			<li class="icn_folder"><a href="booking">Booking</a></li>
			<li class="icn_categories"><a href="payment">Payment</a></li>
			<li class="icn_categories"><a href="city">City</a></li>
			<li class="icn_categories"><a href="contact">Contact</a></li>
			<li class="icn_categories"><a href="faq">FAQ</a></li>
		</ul>
		<h3>News</h3>
		<ul class="toggle">
			<li class="icn_categories"><a href="category">Category</a></li>
			<li class="icn_categories"><a href="new">News</a></li>
		</ul>
		<h3>Orther</h3>
		<ul class="toggle">
			<li class="icn_jump_back"><a href="<s:url value="logout" />" >Logout</a></li>
		</ul>
		
		<footer>
			<hr />
			<p><strong>Copyright &copy; 2013 Website Admin</strong></p>
			<p>Theme by <a href="${ctx}/about">group</a></p>
		</footer>
	</aside><!-- end of sidebar -->
	
</body>
</html>