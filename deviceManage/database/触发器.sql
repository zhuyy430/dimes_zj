CREATE TRIGGER BATCH
-- 原料（YL）自动将材料编号保存为批号。
ON BoxBar
FOR INSERT
AS
declare @barCode bigint,
@inventoryCode varchar(255),
@batchNumber  varchar(255)
select @barCode=barCode,@inventoryCode=inventoryCode,@batchNumber=batchNumber FROM BoxBar
if @inventoryCode like 'YL%' and @batchNumber=''
begin
 update BoxBar  set  batchNumber=furnaceNumber where @barCode=barCode
end
GO
