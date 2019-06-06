set statistics io on
set statistics time on


select top 10 * from vBOI_MonTxn (nolock)

-- Take major queries from the SQL script
-- Turn on statistics by clicking query>client statatistic
-- save results from the statistics tab to a file