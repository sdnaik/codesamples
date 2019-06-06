SET NOCOUNT ON

IF EXISTS (SELECT NULL FROM tempdb.dbo.sysobjects (NOLOCK)
            WHERE name LIKE '#TempTable%')
    DROP TABLE #TempTable
    
CREATE TABLE #TempTable ( personid INT NOT NULL )

INSERT INTO #TempTable VALUES ( 8959018 )
INSERT INTO #TempTable VALUES ( 8922877 )
INSERT INTO #TempTable VALUES ( 8951466 )

SELECT * FROM #TempTable (nolock)

DROP TABLE #TempTable
