-- MES 生成到货单，修改单据类型为钢材到货单
CREATE TRIGGER [dbo].[dbo.PU_ArrivalVouch_Insert]
ON PU_ArrivalVouch
FOR INSERT
AS
declare @cFree10 nvarchar(20),
@cCode nvarchar(30)
select @cCode=cCode FROM PU_ArrivalVouch
if @cCode like 'RKSQ%'
begin
 update PU_ArrivalVouch  set  iVTid='8169' where cCode=@cCode
end
GO
--PU_ArrivalVouchs_Insert触发器  ERP到货单触发器
IF OBJECT_ID (N'PU_ArrivalVouchs_Insert', N'tr') IS NOT NULL
DROP TRIGGER PU_ArrivalVouchs_Insert;
GO
CREATE TRIGGER PU_ArrivalVouchs_Insert
ON PU_ArrivalVouchs
FOR INSERT
AS
declare @cFree10 nvarchar(20)
select @cFree10=PU_ArrivalVouchs.cFree10 FROM PU_ArrivalVouchs INNER JOIN inserted ON PU_ArrivalVouchs.cFree10 =inserted.cFree10 
if @cFree10 is not null
begin
	update PU_ArrivalVouchs  set PU_ArrivalVouchs.iPOsID=@cFree10 where ID=(select ID from inserted)
	update PU_ArrivalVouchs  set PU_ArrivalVouchs.cFree10=null where ID=(select ID from inserted)
end
go
--ERP发货单触发器
IF OBJECT_ID (N'DispatchLists_Insert', N'tr') IS NOT NULL
DROP TRIGGER DispatchLists_Insert;
GO
CREATE TRIGGER DispatchLists_Insert
ON DispatchLists
FOR INSERT
AS
declare @cDefine30 nvarchar(20)
select @cDefine30=DispatchLists.cDefine30 FROM DispatchLists INNER JOIN inserted ON DispatchLists.cDefine30 =inserted.cDefine30
if @cDefine30 is not null
begin
	update DispatchLists  set DispatchLists.iSOsID=@cDefine30 where autoId=(select autoId from inserted)
end
go
