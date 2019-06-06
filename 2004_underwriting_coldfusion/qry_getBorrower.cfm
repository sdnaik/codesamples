<cfquery name="QryGetBorrower" datasource="#Attributes.DSN#">
	select * from DLMClients
	where ClientID = #Attributes.session_userid#
</cfquery>

<cfset Attributes.BFName = qrygetBorrower.FName>
<cfset Attributes.BLName = qrygetBorrower.LName>		
<cfset Attributes.BName = "#Attributes.BFName# #Attributes.BLName#">			

