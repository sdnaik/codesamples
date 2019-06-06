<cfsetting enablecfoutputonly="Yes">

<!--- Get mort ins --->

<cfstoredproc datasource="#Attributes.DSN#" procedure="spGetMortIns">
<cfprocparam cfsqltype="CF_SQL_INTEGER" type="In" value="#qryProgs.programID#">
<cfprocparam cfsqltype="CF_SQL_TINYINT" type="In" value="1">
<cfprocparam cfsqltype="CF_SQL_DECIMAL" scale="4" type="In" value="#Attributes.ltv#">
<cfprocresult name="qryMortIns">
</cfstoredproc>

<!--- get fees --->

<cfstoredproc datasource="#Attributes.DSN#" procedure="spGetFees">
<cfprocparam cfsqltype="CF_SQL_INTEGER" type="In" value="#qryProgs.programID#">
<cfprocparam cfsqltype="CF_SQL_TINYINT" type="In" value="1">
<cfprocparam cfsqltype="CF_SQL_INTEGER" type="In" value="#LoanAmount#">
<cfprocresult name="qryFees">
</cfstoredproc>

<!--- get other fees --->

<cfquery datasource="#Attributes.DSN#" name="qryOther">
	select fo.otherID, fo.otherName, fo.fieldType, fo.apr, fo.sortorder,
		fr.amt_mul, fr.amt_add
	from feeOther fo inner join feeOtherRanges fr on fo.otherID = fr.otherID
	where fo.feeID = #qryFees.feeID#
		and fo.fieldType = 1
		and (fr.minval is null or fr.minval < #LoanAmount#)
		and (fr.maxval is null or fr.maxval >= #LoanAmount#)
		and display = 1
	union
	select fo.otherID, fo.otherName, fo.fieldType, fo.apr, fo.sortorder,
		fr.amt_mul, fr.amt_add
	from feeOther fo inner join feeOtherRanges fr on fo.otherID = fr.otherID
	where fo.feeID = #qryFees.feeID#
		and fo.fieldType = 2
		and (fr.minval is null or fr.minval < #propertyValue#)
		and (fr.maxval is null or fr.maxval >= #propertyValue#)
		and display = 1
	order by sortOrder
</cfquery>

<!--- get payment add-ons --->
<cfset proptax = mortObj.getPropTax(qryFees.tax_mul, qryFees.tax_add, PropertyValue) / 12>
<cfset hazard = mortObj.getHazard(qryFees.insr_mul, qryFees.insr_add, LoanAmount) / 12>
<cfif qryMortIns.factor LTE 0>
	<cfset mi = 0>
<cfelse>
	<cfset mi = (qryMortIns.factor * LoanAmount) / 1200>
</cfif>

<!--- get closing costs --->

<cfset closing = mortObj.getClosing(LoanAmount, propertyValue, qryFees, qryOther,1)>

<!--- Get Rates --->

<cfstoredproc datasource="#Attributes.DSN#" procedure="spGetRates">
<cfprocparam cfsqltype="CF_SQL_INTEGER" type="In" value="#qryProgs.sheetID#">
<cfprocparam cfsqltype="CF_SQL_DECIMAL" scale="3" type="In" value="#adj_val#">
<cfprocparam cfsqltype="CF_SQL_DECIMAL" scale="3" type="In" value="#Attributes.gpp#">
<cfprocparam cfsqltype="CF_SQL_DECIMAL" scale="4" type="In" value="#LoanAmount#">
<cfprocparam cfsqltype="CF_SQL_TINYINT" type="In" value="#Attributes.term#">
<cfprocparam cfsqltype="cf_sql_TINYINT" type="in" value="30">

<cfprocresult name="qryRates">
</cfstoredproc>

<!--- Third party costs and impounds --->

<cfset third_total = (qryFees.appraisal + qryFees.escrow + qryFees.endorse + qryFees.courier1 + qryFees.recording)>

<cfloop query="qryFees">
<cfif Len(qryFees.fieldName)>
	<cfif qryFees.fieldID EQ 2>
		<cfset third_total = third_total + (((qryFees.amt_mul * LoanAmount) + qryFees.amt_add) / 2)>
	<cfelse>
		<cfset third_total = third_total + (qryFees.amt_mul * LoanAmount) + qryFees.amt_add>
	</cfif>
</cfif>
</cfloop>

<cfloop query="qryOther">
	<cfif qryOther.fieldType EQ 1>
		<cfset third_total = third_total + (qryOther.amt_mul * LoanAmount) + qryOther.amt_add>
	<cfelse>
		<cfset third_total = third_total + (qryOther.amt_mul * propertyValue) + qryOther.amt_add>
	</cfif>			
</cfloop>


<cfsetting enablecfoutputonly="No">
