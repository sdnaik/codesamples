<!--- root > application.cfm --->
<cfapplication name="GP_App" sessionmanagement="Yes" sessiontimeout="#CreateTimeSpan(0,1,0,0)#">

<cflock name="session" type="READONLY" timeout="30">
	
	<cfparam name="Session.userID" default="0">
	<cfparam name="Session.GroupID" default="0">
	<cfparam name="Session.user_fname" default="">
	<cfparam name="Session.user_lname" default="">
	<cfparam name="Session.user_ACL" default="0">
	<cfparam name="Session.AppID" default="0">
	<cfparam name="Session.PropAddress" default="">
	<cfparam name="Session.LoanNumber" default="">
	<cfparam name="Session.BoFName" default="">
	<cfparam name="Session.BoLName" default="">
	<cfparam name="Session.MoreratesProgramID" default="">
	<cfparam name="Session.MoreIDSheet" default="">	
	<cfparam name="Session.MoreIDTerm" default="">
	<cfparam name="Session.LessIDSheet" default="">	
	<cfparam name="Session.LessIDTerm" default="">
	
	<cfset Attributes.session_userID = Session.userID>
	<cfset Attributes.session_groupID = Session.GroupID>
	<cfset Attributes.session_user_fname = Session.user_fname>
	<cfset Attributes.session_user_lname = Session.user_lname>
	<cfset Attributes.session_userACL  = Session.user_ACL>
	<cfset Attributes.session_AppID = Session.AppID>
	<cfset Attributes.session_PropAddress = Session.PropAddress>
	<cfset Attributes.session_LoanNumber = Session.LoanNumber>
	<cfset Attributes.session_BoFName = Session.BoFName>
	<cfset Attributes.session_BoLName = Session.BoLName>	
	<cfset Attributes.Session_MoreIDSheet = Session.MoreIDSheet>
	<cfset Attributes.Session_MoreIDTerm = Session.MoreIDTerm>	
	<cfset Attributes.Session_LessIDSheet = Session.LessIDSheet>
	<cfset Attributes.Session_LessIDTerm = Session.LessIDTerm>
	
</cflock>

<cfif isdefined("cookie.cfid") AND isdefined("cookie.cftoken")>
	<cfcookie name="cfid" value="#cookie.cfid#">
	<cfcookie name="cftoken" value="#cookie.cftoken#">
</cfif>

<!---
<cferror type="request" template="/showexception.cfm" mailto="help@greenpockets.com">
<cferror type="exception" template="/showexception.cfm" mailto="help@greenpockets.com">
 open first two for no debug
<cferror type="monitor" template="/showexception.cfm" mailto="help@greenpockets.com">
 --->