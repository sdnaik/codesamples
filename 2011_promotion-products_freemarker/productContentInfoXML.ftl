[#assign searchStartDate = '' /]
[#assign searchEndDate = '' /]

[#assign searchVariable =  'ports' /]
[#assign displayBoxes = 'false' /]

[#if productContentParentcontent.productContent?has_content]
	<ProductContentList>		 
		[#list productContentParentcontent.productContent?children as productContentInfo]		
			<ProductContent>
				[#if productContentInfo??]
					<Name>${productContentInfo.collectionName!}</Name>	
					<Anchor>${productContentInfo.anchor!}</Anchor>	
					<Thumbnail>${productContentInfo.headercollectionImage!}</Thumbnail>
					<Image>${productContentInfo.collectionImage!}</Image>
					<ImageAlt>${productContentInfo.collectionImageAlt!}</ImageAlt>
					<Copy><![CDATA[${productContentInfo.collectionCopy!}]]></Copy>
					
					[#if productContentInfo.useOrigSearchBucket?? && productContentInfo.useOrigSearchBucket?has_content]
						<UseOrigSearchBucket>${productContentInfo.useOrigSearchBucket}</UseOrigSearchBucket>										
						[#assign useOrigSearchBucket = productContentInfo.useOrigSearchBucket/]
						[#if inheritedPage.SearchStart?? && inheritedPage.SearchStart?has_content]
							[#assign searchStartDate = inheritedPage.SearchStart /]
						[/#if]
						[#if inheritedPage.SearchEnd?? && inheritedPage.SearchEnd?has_content]
							[#assign searchEndDate = inheritedPage.SearchEnd /]
						[/#if]
					[#else]
						[#assign useOrigSearchBucket = 'true' /]
						<UseOrigSearchBucket>${useOrigSearchBucket}</UseOrigSearchBucket>
					[/#if]
					[#if productContentInfo.numColumns?? && productContentInfo.numColumns?has_content]
						[#assign numColumns = productContentInfo.numColumns?number /]	
						<NumColumns>${numColumns}</NumColumns>
					[/#if]					
					[#if productContentInfo.NumDays?? && productContentInfo.NumDays?has_content]
						[#assign numDays = productContentInfo.NumDays /]	
						<NumDays>${numDays}</NumDays>
					[#else]
						[#assign numDays = '' /]
					[/#if]
					[#if productContentInfo.searchStartDate?? && productContentInfo.searchStartDate?has_content && useOrigSearchBucket != "true"]
						[#assign searchStartDate = productContentInfo.searchStartDate?string["MM/dd/yyyy"] /]							
					[#else]							
						[#if inheritedPage.SearchStart?? && inheritedPage.SearchStart?has_content && numDays = '']
							[#assign searchStartDate = inheritedPage.SearchStart /]
						[#else]
							[#assign searchStartDate = '']	
						[/#if]					
					[/#if]
					[#if productContentInfo.searchEndDate?? && productContentInfo.searchEndDate?has_content && useOrigSearchBucket != "true" ]
						[#assign searchEndDate = productContentInfo.searchEndDate?string["MM/dd/yyyy"] /]	
					[#else]
						[#if inheritedPage.SearchEnd?? && inheritedPage.SearchEnd?has_content && numDays = '']
							[#assign searchEndDate = inheritedPage.SearchEnd /]
						[#else]	
							[#assign searchEndDate = '']
						[/#if]							
					[/#if]
					<SearchStartDate>${searchStartDate}</SearchStartDate>
					<SearchEndDate>${searchEndDate}</SearchEndDate>
					[#if useOrigSearchBucket != "true"]
						[#if productContentInfo.searchVariable?? && productContentInfo.searchVariable?has_content]
							[#assign searchVariable = productContentInfo.searchVariable /]
							<SearchVariable>${searchVariable}</SearchVariable>
						[#elseif inheritedPage.pagetype?has_content]
							[#if inheritedPage.pagetype?html == 'cruiseline']
								[#assign searchVariable = 'destinations' /]
								<SearchVariable>${searchVariable}</SearchVariable>
							[#elseif inheritedPage.pagetype?html == 'destination']
								[#assign searchVariable = 'cruiselines' /]
								<SearchVariable>${searchVariable}</SearchVariable>
							[/#if]
						[/#if]
					[#else]
						[#assign searchVariable = 'ports' /]
						<SearchVariable>${searchVariable}</SearchVariable>
					[/#if]
					[#if productContentInfo.HeaderName?? && productContentInfo.HeaderName?has_content]
						<SearchHeader>${productContentInfo.HeaderName}</SearchHeader>
					[/#if]
					[#if (inheritedPage.useTabs!)?html == 'true']
						[#assign displayBoxes = 'true' /]
						<DisplayBoxes>${displayBoxes}</DisplayBoxes>
					[/#if]
					
					[#if productContentInfo.cruiseline?? && productContentInfo.cruiseline?has_content]
						<CruiseLine>${productContentInfo.cruiseline!}</CruiseLine>
					[/#if]
					<Location>${productContentInfo.location!}</Location>
					[#if productContentInfo.destinationOptions?has_content]
						<Destinations>
							[#list productContentInfo.destinationOptions!?values as region]
								<Destination>${region}</Destination>
							[/#list]
						</Destinations>
					[/#if]
					[#if productContentInfo.cruiselineOptions?has_content]
						<CruiseLines>
							[#list productContentInfo.cruiselineOptions!?values as cruiselines]		
								<CruiseLine>${cruiselines}</CruiseLine>
							[/#list]
						</CruiseLines>
					[/#if]
					[#if productContentInfo.ports?has_content && useOrigSearchBucket == "true"]
						<Ports>
							[#list productContentInfo.ports!?values as val] 
								${val} | 
							[/#list]
						</Ports>
					[#elseif productContentInfo.portOptions?has_content]
						<Ports>
							[#list productContentInfo.portOptions!?values as ports]		
								<Port>${ports}</Port>
							[/#list]
						</Ports>
					[/#if]
					
				[/#if]
			</ProductContent>	
		[/#list]
	</ProductContentList>
[/#if]