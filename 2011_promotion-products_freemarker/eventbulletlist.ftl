[#assign cms=JspTaglibs["cms-taglib"]]
[#assign cmsu=JspTaglibs["cms-util-taglib"]]
[#include "./../../templates/macros/wthcomponent.ftl"]
[#attempt]
 [@cms.editBar editLabel="Edit eventbulletlist" paragraph="eventbulletlist" /]
	[@displayList/]	
[#--end popup msg--]	

[#macro displayList ]	
[@cms.newBar  contentNodeCollectionName="eventtextbullet" newLabel="New eventtextbullet (New Bullet)" paragraph="eventtextbullet" /]	
[#if content.eventtextbullet?has_content ]				
	<ul style="margin-top: 0px; margin-bottom: 0px;">
	   	[@wthcomponent nodename="eventtextbullet" paragraphname="eventtextbullet" /]      	
	</ul>
[/#if]

[/#macro]
 
[#recover]

[#include "includes/errorhandler.ftl"/]
[/#attempt]