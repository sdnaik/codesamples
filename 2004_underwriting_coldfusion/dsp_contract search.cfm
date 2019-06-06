<CFModule Template="#Attributes.fuse_root#/Customtags/CTContentTop.cfm"
 Fuse_root= "#Attributes.fuse_root#" ContentTitle = "#Attributes.NavBName#">

<cfif isdefined("url.appID")>
<cfset UserType = 1>
<cfelse>
<cfset UserType = #qryLCNav.UserType#>
</cfif>

<!---
Get contact type for the dropdown menu
--->

<!---
Define user access rights here. 
The following statements about showing different contact type will depend upon who the user is.
For example Borrowers should not be able to search for other borrowers.
--->

<cfif UserType is 2>

<cfquery name="qryGetCategory" datasource="#Attributes.DSN#">
select *
from DLMContactType
where ACL > 0
</cfquery>

<cfelse>

<cfquery name="qryGetCategory" datasource="#Attributes.DSN#">
select *
from DLMContactType
where ACL > 1
</cfquery>

</cfif>

<!--
Get state for the drop down menu. 
The same table for state is used elsewhere - e.g. Loan Scenario, Loan Application etc.
--->

<cfquery name="qryGetState" datasource="#Attributes.DSN#">
select *
from States
</cfquery>


<cfquery name="qryGetEE" datasource="#Attributes.DSN#">
select CompanyPK, FName 
from DLMContact
where ContactTypeID = 101
</cfquery>

<p><strong>Search Contact By:</strong></p>
<form name="formSearch" method="post" action="Calendar/index.cfm?action=results">

 <cfif userType GT 0> 

  <table width="100%" border="0">
    <tr> 
      <td width="47%" class="FormText">Contract Name Contains:</td>
      <td width="53%"><input type="text" name="ContractName" class="FormBox150"></td>
    </tr>
    <tr>
      <td class="FormText">Contact Name Contains:</td>
      <td><input type="text" name="ContactName" class="FormBox150"></td>
    </tr>
    <tr> 
      <td class="FormText">Company Name Contains:</td>
      <td><input type="text" name="CompanyName" class="FormBox150"></td>
    </tr>

        <tr> 

      <td class="FormText">Running on a Given Day:</td>
      <td><select name="State">
          <option value="">All</option>
          <cfoutput query="qryGetState"> 
            <option value="#State#"> #State# </option>
          </cfoutput> </select></td>
    </tr>

    <tr> 
      <td class="FormText">Date Range:</td>
      <td><input type="text" name="Zip" class="FormBox150"></td>
    </tr>
	
</cfif>

  </table>
  
  <p> 
    <input type="submit" value="Search">
    <input type="reset" value="Reset">
  </p>
</form>
<br>
<a href="Community/index.cfm?action=searchCompany">Account Set up for advanced Greenpockets 
features </a><br>

<cfmodule Template="#Attributes.fuse_root#/Customtags/CTContentBot.cfm" Fuse_root = "#Attributes.fuse_root#">