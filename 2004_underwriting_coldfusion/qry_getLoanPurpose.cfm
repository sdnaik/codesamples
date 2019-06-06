<cfquery name="QryGetLoanPurpose" datasource="#Attributes.DSN#">
select * from DLMLoanPurpose
where LoanPurposeID = #Attributes.LoanPurposeID#
</cfquery>

<cfset Attributes.LoanPurpose = QryGetLoanPurpose.LoanPurpose>