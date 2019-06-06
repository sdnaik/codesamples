<!--- root > app_layout.cfm --->

<cfparam name="Request.bodycontent" default="">

<cfif Len(Attributes.headerfile)><cfinclude template="#Attributes.headerfile#"></cfif>
<cfif Len(Attributes.leftfile)><cfinclude template="#Attributes.leftfile#"></cfif>
<cfoutput>#Request.bodycontent#</cfoutput>
<cfif Len(Attributes.rightfile)><cfinclude template="#Attributes.rightfile#"></cfif>
<cfif Len(Attributes.footerfile)><cfinclude template="#Attributes.footerfile#"></cfif>


