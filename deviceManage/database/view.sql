EXEC sp_addlinkedserver
@server='ERP',--被访问的服务器别名（习惯上直接使用目标服务器IP，或取个别名如：JOY）
@srvproduct='',
@provider='SQLOLEDB',
--@datasrc='192.168.18.214' --要访问的服务器
@datasrc='192.168.10.199'
--使用sp_addlinkedsrvlogin 来增加用户登录链接
EXEC sp_addlinkedsrvlogin
'ERP', --被访问的服务器别名（如果上面sp_addlinkedserver中使用别名JOY，则这里也是JOY）
'false',
NULL,
'sa', --帐号
--'1!deshine' --密码
'digitzones'
go
 /*创建物料视图*/
 IF EXISTS(SELECT 1 FROM sys.views WHERE name='WORKPIECE')
DROP VIEW WORKPIECE
GO
 CREATE  VIEW  WORKPIECE AS SELECT 
  i.cInvCode ,i.cInvAddCode ,i.cInvName ,i.cInvStd ,
  i.cInvCCode ,ic.cInvCName ,i.cPosition ,i.bPurchase ,
  i.bSelf ,i.bSale ,i.iSafeNum ,i.iTopSum ,
  i.cInvDefine14,
  i.iLowSum ,i.cComUnitCode ,c.cComUnitName ,i.cEngineerFigNo,i.cDefWareHouse
  FROM ERP.UFDATA_999_2019.dbo.Inventory i INNER JOIN ERP.UFDATA_999_2019.dbo.InventoryClass ic ON i.cInvCCode = ic.cInvCCode
  INNER JOIN ERP.UFDATA_999_2019.dbo.ComputationUnit c ON i.cComUnitCode = c.cComunitCode AND i.cGroupCode = c.cGroupCode;
go
 /*创建计量单位视图*/
  IF EXISTS(SELECT 1 FROM sys.views WHERE name='View_ComputationUnit')
DROP VIEW View_ComputationUnit
GO
CREATE  VIEW  View_ComputationUnit AS SELECT c.cUnitRefInvCode,c.cComunitCode,c.cComUnitName,c.cGroupCode FROM  ERP.UFDATA_999_2019.dbo.ComputationUnit c;
go
 /*创建物料类别视图*/
  IF EXISTS(SELECT 1 FROM sys.views WHERE name='View_InventoryClass')
DROP VIEW View_InventoryClass
GO
CREATE  VIEW  View_InventoryClass AS SELECT c.cInvCCode,c.cInvCName,c.iInvCGrade,c.bInvCEnd FROM  ERP.UFDATA_999_2019.dbo.inventoryClass c;
go
/*创建客户视图*/
  IF EXISTS(SELECT 1 FROM sys.views WHERE name='View_Customer')
DROP VIEW View_Customer
GO
CREATE  VIEW  View_Customer AS SELECT c.cCusCode,c.cCusName,c.cCusAbbName,c.cCCCode,c.cTrade,c.cCusAddress FROM ERP.UFDATA_999_2019.dbo.Customer c;
go
/*创建部门视图*/
  IF EXISTS(SELECT 1 FROM sys.views WHERE name='View_Department')
DROP VIEW View_Department
GO
CREATE  VIEW  View_Department AS SELECT d.cDepCode,d.cDepName,d.iDepGrade,d.cDepPerson,d.cDepType,d.iDepOrder FROM ERP.UFDATA_999_2019.dbo.Department d;
go
/*创建员工视图*/
  IF EXISTS(SELECT 1 FROM sys.views WHERE name='View_Person')
DROP VIEW View_Person
GO
CREATE  VIEW  View_Person AS SELECT p.cPsn_Num,p.cPsn_Name,p.cDept_Num,p.rSex,p.cPsnMobilePhone,p.cPsnEmail
 ,d.cDepName  FROM ERP.UFDATA_999_2019.dbo.hr_hi_person p inner join View_Department d on p.cDept_Num=d.cDepCode;
go
/*创建采购订单子表视图*/
 IF EXISTS(SELECT 1 FROM sys.views WHERE name='View_PO_Podetails')
DROP VIEW View_PO_Podetails
GO
CREATE  VIEW  View_PO_Podetails AS SELECT pp.iTaxPrice,pp.iUnitPrice,pp.iMoney,pp.iTax,pp.iSum,pp.iNatUnitPrice,pp.iNatMoney,pp.iNatTax,pp.iNatSum
,pp.iPerTaxRate,p.cpoId,p.cDefine7, p.POID, pp.ID ,ic.cInvCName ,pp.cInvCode ,i.cInvName ,i.cInvStd ,pp.iQuantity ,c.cComUnitName ,pp.dArriveDate ,pp.iflag ,pp.iarrqty  ,
i.cPosition FROM ERP.UFDATA_999_2019.dbo.PO_Podetails pp inner join ERP.UFDATA_999_2019.dbo.PO_Pomain p on p.POID = pp.POID inner join ERP.UFDATA_999_2019.dbo.Inventory i ON pp.cInvCode = i.cInvCode 
inner join ERP.UFDATA_999_2019.dbo.InventoryClass ic ON i.cInvCCode = ic.cInvCCode INNER JOIN ERP.UFDATA_999_2019.dbo.ComputationUnit c ON i.cComUnitCode = c.cComunitCode
where i.cInvCode like 'YL%';

go
/*创建采购订单视图*/
 IF EXISTS(SELECT 1 FROM sys.views WHERE name='View_PO_Pomain')
DROP VIEW View_PO_Pomain
GO
CREATE  VIEW  View_PO_Pomain AS SELECT top 100 percent
 p.cDepCode,p.cPersonCode,
 p.POID,p.cBusType ,p.cPOID ,p.dPODate ,p.cVenCode ,p.cMaker ,p.cVerifier ,p.cState ,p.cMemo ,v.cVenPerson ,v.cVenPhone,v.cVenName
 FROM ERP.UFDATA_999_2019.dbo.PO_Pomain p INNER JOIN ERP.UFDATA_999_2019.dbo.Vendor v ON p.cVenCode = v.cVenCode order by p.dPODate desc;
go
/*供应商类别视图*/
 IF EXISTS(SELECT 1 FROM sys.views WHERE name='VIEW_VENDORCLASS')
DROP VIEW VIEW_VENDORCLASS
GO
CREATE VIEW VIEW_VENDORCLASS AS SELECT cVCCode,cVCName from ERP.UFDATA_999_2019.dbo.vendorClass
go
/*创建供应商视图*/
 IF EXISTS(SELECT 1 FROM sys.views WHERE name='View_Vendor')
DROP VIEW View_Vendor
GO
CREATE  VIEW  View_Vendor AS SELECT v.cVenCode,v.cVenName,v.cVenAbbName,v.cVCCode,v.cVenPerson ,v.cVenPhone FROM ERP.UFDATA_999_2019.dbo.Vendor v;
go
/*创建仓库视图*/
 IF EXISTS(SELECT 1 FROM sys.views WHERE name='View_Warehouse')
DROP VIEW View_Warehouse
GO
CREATE  VIEW  View_Warehouse AS SELECT w.cWhCode,w.cWhName,w.cDepCode,w.cWhAddress FROM ERP.UFDATA_999_2019.dbo.Warehouse w;
go
--销售订单视图
 IF EXISTS(SELECT 1 FROM sys.views WHERE name='view_SalesSlip')
DROP VIEW view_SalesSlip
GO
CREATE VIEW view_SalesSlip AS  SELECT SO_SOMain.cPersonCode , SO_SOMain.cexch_name,SO_SOMain.iexchRate,SO_SOMain.iTaxRate,SO_SODetails.iUnitPrice,SO_SODetails.iTaxUnitPrice,
SO_SODetails.iDiscount,SO_SODetails.iNatUnitPrice,SO_SODetails.iNatDiscount,SO_SODetails.iSOsId,
SO_SOMain.cSOCode,SO_SOMain.cCusCode ,SO_SOMain.cCusName ,SO_SODetails.AutoID ,SO_SODetails.cDefine22,
  Inventory.cInvStd,SO_SODetails.cInvCode,SO_SODetails.irowno,SO_SODetails.iQuantity,
  SO_SODetails.iFHQuantity,SO_SODetails.cInvName,SO_SODetails.dPreDate,SO_SODetails.cMemo
    FROM ERP.UFDATA_999_2019.dbo.SO_SOMain SO_SOMain INNER JOIN ERP.UFDATA_999_2019.dbo.SO_SODetails SO_SODetails ON SO_SOMain.ID = SO_SODetails.ID
  AND SO_SOMain.cSOCode = SO_SODetails.cSOCode INNER JOIN ERP.UFDATA_999_2019.dbo.Inventory  ON SO_SODetails.cInvCode = Inventory.cInvCode  ;
go
/**工单详情及子件信息*/
IF EXISTS(SELECT 1 FROM sys.views WHERE name='View_mom_orderdetail')
DROP VIEW View_mom_orderdetail
GO
CREATE  VIEW  View_mom_orderdetail AS SELECT mom_orderdetail.MoDId as MoDId,mom_orderdetail.MoId AS MoId,mom_order.mocode AS mocode,
mom_orderdetail.SortSeq AS SortSeq,mom_morder.StartDate as StartDate,mom_order.CreateTime AS createTime,mom_orderdetail.Qty AS detailQty,mom_orderdetail.MoLotCode AS MoLotCode,
mom_orderdetail.WhCode AS WhCode, mom_orderdetail.MDeptCode as MDeptCode, mom_orderdetail.Status as Status ,mom_orderdetail.PartId as PartId,
mom_orderdetail.InvCode detailInvCode,mom_orderdetail.RelsTime as RelsTime,mom_orderdetail.RelsUser as RelsUser,mom_orderdetail.Define24 AS Define24
,mom_orderdetail.Define33 as Define33,mom_moallocate.invcode as moallocateInvcode,Inventory.cInvName as cInvName,Inventory.cInvStd as cInvStd,
 mom_moallocate.Qty as moallocateQty,mom_moallocate.AllocateId as LotNo
FROM ERP.UFDATA_999_2019.dbo.mom_orderdetail as mom_orderdetail inner join   ERP.UFDATA_999_2019.dbo.mom_order on  
mom_orderdetail.MoId=mom_order.moid inner join ERP.UFDATA_999_2019.dbo.mom_moallocate on  
mom_orderdetail.MoDId=mom_moallocate.MoDId inner join   ERP.UFDATA_999_2019.dbo.mom_morder on  
mom_orderdetail.MoDId=mom_morder.MoDId inner join ERP.UFDATA_999_2019.dbo.Inventory on mom_orderdetail.Invcode=Inventory.cInvCode
where mom_orderdetail.InvCode not like 'MC%' and mom_moallocate.invcode not like 'MC%' and mom_orderdetail.Status=3
go
IF EXISTS(SELECT 1 FROM sys.views WHERE name='view_UserDefine')
DROP VIEW view_UserDefine
GO
create view view_UserDefine as select cId,cValue,cAlias from ERP.UFDATA_999_2019.dbo.UserDefine where cid=207
go
IF EXISTS(SELECT 1 FROM sys.views WHERE name='view_Position')
DROP VIEW view_Position
GO
create view view_Position as select cPosCode,cPosName,bPosEnd,cWhCode from ERP.UFDATA_999_2019.dbo.Position  where cWhCode='20' and bPosEnd=1
go


IF EXISTS(SELECT 1 FROM sys.views WHERE name='view_Equipment')
DROP VIEW view_Equipment
GO
create view view_Equipment as SELECT row_number() over(ORDER BY c.cInvCode desc) as id, c.cInvCode AS code, d.cInvName AS name, d.cInvStd AS unitType, c.cBatch AS batchNo, c.iQuantity AS qty, d.cInvDefine11 AS useLife, c.dVeriDate AS inWarehouseDate
FROM         (SELECT     a.cInvCode, a.cBatch, a.iQuantity, b.dVeriDate
                       FROM          ERP.UFDATA_999_2019.dbo.rdrecords01 AS a LEFT OUTER JOIN
                                              ERP.UFDATA_999_2019.dbo.RdRecord01 AS b ON a.ID = b.ID
                       WHERE      (b.cHandler IS NOT NULL)
                       UNION ALL
                       SELECT     a.cInvCode, a.cBatch, a.iQuantity, b.dVeriDate
                       FROM         ERP.UFDATA_999_2019.dbo.rdrecords08 AS a LEFT OUTER JOIN
                                             ERP.UFDATA_999_2019.dbo.RdRecord08 AS b ON a.ID = b.ID
                       WHERE     (b.cHandler IS NOT NULL)
                       UNION ALL
                       SELECT     a.cInvCode, a.cBatch, a.iQuantity, b.dVeriDate
                       FROM         ERP.UFDATA_999_2019.dbo.rdrecords10 AS a LEFT OUTER JOIN
                                             ERP.UFDATA_999_2019.dbo.rdrecord10 AS b ON a.ID = b.ID
                       WHERE     (b.cHandler IS NOT NULL)) AS c LEFT OUTER JOIN
                      ERP.UFDATA_999_2019.dbo.Inventory AS d ON c.cInvCode = d.cInvCode
WHERE     (c.cInvCode LIKE 'MC%H1') OR
                      (c.cInvCode LIKE 'MC%H2') OR
                      (c.cInvCode LIKE 'MC%F1') OR
                      (c.cInvCode LIKE 'MC%F2')
go



