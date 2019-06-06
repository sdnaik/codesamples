<cfquery name="QryGetBranch" datasource="#Attributes.DSN#">
select * from DLMBranches
where BranchID = #Attributes.BranchID#
</cfquery>

<cfset Attributes.BranchName = QryGetBranch.BranchName>