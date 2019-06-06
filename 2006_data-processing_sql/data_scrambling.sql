/************************************
--   Prepared By: Siddharth Naik							
--   Reviewed By: Balavenkatesh Pandurangan
--   Server: BASEDB1SUN / BASEDB1ORL	
--   Database: gemini									
--   Expected Run Time: < 1 Min 				
--   Expected Row Update: 
--   Confidential Data (Y /N): N  
--   Project: a wellknown financial company
--   Purpose: Prefix ClientUniqueID by XXX so that the member can reenroll
*************************************/

Begin tran	

Declare @GivenMemberIDs varchar(1000)
Declare @CurrentMemberID varchar(20)
Declare @NewMemberID varchar(20)
Declare @index int

DECLARE @TmpMemberIDs TABLE (MemberID VARCHAR(20))

--CREATE TABLE #TmpMemberIDs (MemberID VARCHAR(20))

Set @GivenMemberIDs = '123455,78897867,897867' -- Please add given MemberID here
-- USAA MemberIDs are mostly of 8 digits; so for IDs < 8 digits, prepend the IDs with 0s to find correct MemberID

-- If temptable is not null drop table

while len(@GivenMemberIDs) > 0
begin
	set @index = charindex(',', @GivenMemberIDs)
	if (@index > 0)
		begin
			set @CurrentMemberID = substring(@GivenMemberIDs,0,@index)
			insert into @TmpMemberIDs (memberID) values (@CurrentMemberID)				
			set @GivenMemberIDs = right(@GivenMemberIDs, (len(@GivenMemberIDs)-@index))
			select @GivenMemberIDs
			select len(@GivenMemberIDs)
		end
	else
		set @GivenMemberIDs = null
end


/*

DECLARE c1 CURSOR FOR
SELECT 
FROM 

OPEN c1

FETCH NEXT FROM c1
INTO @AuthorID


WHILE @@FETCH_STATUS = 0
BEGIN

    PRINT @AuthorID

    FETCH NEXT FROM c1
    INTO @AuthorID 

END

CLOSE c1
DEALLOCATE c1

*/

DECLARE c1 CURSOR FOR
SELECT MemberID
FROM @TmpMemberIDs

OPEN c1

FETCH NEXT FROM c1
INTO @ThisMemberID

WHILE @@FETCH_STATUS = 0
BEGIN

Set @NewMemberID = 'XXX' + @ThisMemberID

Select @Relation = relation from cc_person (nolock)where ClientUniqueID = @ThisMemberID and clientid = 30765

if @@rowcount = 0
	begin
		print 'The MemberID' + ' ' + convert(varchar(8), @ThisMemberIDs) + ' ' +
		'does not exist. Try prefixing MemberID with 0s upto total of 8 digits and confirm teen names with USAA before update'
	end
else
	if @Relation <> 0
		begin
			print 'The MemberID' + ' ' + convert(varchar(8), @ThisMemberID) + ' ' + 'is not of a teen. We should only XXX teen''s MemberID'
		end
	else
		begin
			update cc_person set
			ClientUniqueID = @NewMemberID,
			Updated = getDate()
			where ClientUniqueID = @ThisMemberID and clientid = 30765 and relation = 0
		end

if @@error = 0
	commit tran 
else
	rollback tran

Print 'MemberID after update:'
select ClientUniqueID, ClientID, PersonID, First, Last, Relation, Updated from cc_person (nolock)where ClientUniqueID = @NewMemberID and clientid = 30765 and relation = 0



END

CLOSE c1
DEALLOCATE c1


/*
select top 10 clientuniqueid, relation, * from cc_person (nolock)
where clientid = 104639
and personid is not null
and relation = 0

begin tran

select top 10 clientuniqueid, relation, * from cc_person (nolock)
where clientuniqueid in (
'12041244',
'12341814',
'THANKS2'
)
and	clientid = 104639 and relation = 0

		update cc_person set
			ClientUniqueID = 'XXX' + ClientUniqueID
			where ClientUniqueID in 
	(
'12041244',
'12341814',
'THANKS2'
	)		
	and	clientid = 104639 and relation = 0

select top 10 clientuniqueid, relation, * from cc_person (nolock)
where clientuniqueid in (
'XXX12041244',
'XXX12341814',
'XXXTHANKS2'
)
	and	clientid = 104639 and relation = 0

rollback tran
*/

