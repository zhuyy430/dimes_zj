--生成erp取值的装备视图
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












--添加装备条码导入菜单
insert into MODULE (disabled,leaf,name,url,PARENT_ID) values (0,1,'装备条码导入','console/jsp/equipment_import.jsp',(select id from MODULE where name='装备信息' and leaf=0))

--添加装备条码导入菜单admin权限
insert into ROLE_MODULE (MODULE_ID,ROLE_ID) values ((select m.id from MODULE m where name='装备条码导入'),(select r.id from ROLE r where roleName='admin'))










--添加页面信息
INSERT INTO MODULE (disabled,leaf,name,priority,url,PARENT_ID) values(0,1,'外协入库申请单',3,'procurement/outsourcingWarehousingApplicationForm.jsp',(select id from MODULE where name='采购管理' and PARENT_ID is not null and leaf=0))
INSERT INTO MODULE (disabled,leaf,name,priority,url,PARENT_ID) values(0,1,'装备故障',NUll,'console/jsp/equipmentNGType.jsp',(select id from MODULE where name='装备信息' and PARENT_ID is not null and leaf=0))
INSERT INTO MODULE (disabled,leaf,name,priority,url,PARENT_ID) values(0,1,'装备维修记录',11,'console/jsp/equipmentRapirRecord.jsp',(select id from MODULE where name='生产管理' and PARENT_ID is not null and leaf=0))

--分配页面
INSERT INTO ROLE_MODULE (MODULE_ID,ROLE_ID) values((select id from MODULE where name='外协入库申请单'),(select id from ROLE where roleName='admin'))
INSERT INTO ROLE_MODULE (MODULE_ID,ROLE_ID) values((select id from MODULE where name='装备故障'),(select id from ROLE where roleName='admin'))
INSERT INTO ROLE_MODULE (MODULE_ID,ROLE_ID) values((select id from MODULE where name='装备维修记录'),(select id from ROLE where roleName='admin'))

--新增外协入库申请单页面权限
INSERT INTO POWER (createDate,disable,_group,note,powerCode,powerName) values (getdate(),0,'外协入库申请单','采购管理->采购管理->外协入库申请单','OS_QUERY_WAREHOUSINGAPPLICATIONFORM','外协查询入库申请单')
INSERT INTO POWER (createDate,disable,_group,note,powerCode,powerName) values (getdate(),0,'外协入库申请单','采购管理->采购管理->外协入库申请单','OS_EXPORT_WAREHOUSINGAPPLICATIONFORM','外协导出入库申请单')
INSERT INTO POWER (createDate,disable,_group,note,powerCode,powerName) values (getdate(),0,'外协入库申请单','采购管理->采购管理->外协入库申请单','OS_ADD_WAREHOUSINGAPPLICATIONFORM','外协新增入库申请单')
INSERT INTO POWER (createDate,disable,_group,note,powerCode,powerName) values (getdate(),0,'外协入库申请单','采购管理->采购管理->外协入库申请单','OS_DEL_WAREHOUSINGAPPLICATIONFORM','外协删除入库申请单')
INSERT INTO POWER (createDate,disable,_group,note,powerCode,powerName) values (getdate(),0,'外协入库申请单','采购管理->采购管理->外协入库申请单','OS_SEE_WAREHOUSINGAPPLICATIONFORM','外协查看入库申请单')
INSERT INTO POWER (createDate,disable,_group,note,powerCode,powerName) values (getdate(),0,'外协入库申请单','采购管理->采购管理->外协入库申请单','OS_EXAMINE_WAREHOUSINGAPPLICATIONFORM',' 外协审核入库申请单')
INSERT INTO POWER (createDate,disable,_group,note,powerCode,powerName) values (getdate(),0,'外协入库申请单','采购管理->采购管理->外协入库申请单','OS_REVERSEEXAMINE_WAREHOUSINGAPPLICATIONFORM','外协反审核入库申请单')

--新增外协入库申请单详情页面权限
INSERT INTO POWER (createDate,disable,_group,note,powerCode,powerName) values (getdate(),0,'外协入库申请单详情','采购管理->采购管理->外协入库申请单->外协入库申请单详情','OS_ADDLINE_WAREHOUSINGAPPLICATIONFORMDETAIL','外协入库申请单详情添加行')
INSERT INTO POWER (createDate,disable,_group,note,powerCode,powerName) values (getdate(),0,'外协入库申请单详情','采购管理->采购管理->外协入库申请单->外协入库申请单详情','OS_DELLINE_WAREHOUSINGAPPLICATIONFORMDETAIL','外协入库申请单详情删除行')
INSERT INTO POWER (createDate,disable,_group,note,powerCode,powerName) values (getdate(),0,'外协入库申请单详情','采购管理->采购管理->外协入库申请单->外协入库申请单详情','OS_SPLITBOX_WAREHOUSINGAPPLICATIONFORMDETAIL','外协入库申请单详情拆箱')
INSERT INTO POWER (createDate,disable,_group,note,powerCode,powerName) values (getdate(),0,'外协入库申请单详情','采购管理->采购管理->外协入库申请单->外协入库申请单详情','OS_SAVE_WAREHOUSINGAPPLICATIONFORMDETAIL','外协入库申请单详情保存')
INSERT INTO POWER (createDate,disable,_group,note,powerCode,powerName) values (getdate(),0,'外协入库申请单详情','采购管理->采购管理->外协入库申请单->外协入库申请单详情','OS_EXAMINE_WAREHOUSINGAPPLICATIONFORMDETAIL','外协入库申请单详情审核')
INSERT INTO POWER (createDate,disable,_group,note,powerCode,powerName) values (getdate(),0,'外协入库申请单详情','采购管理->采购管理->外协入库申请单->外协入库申请单详情','OS_REVERSEEXAMINE_WAREHOUSINGAPPLICATIONFORMDETAIL','外协入库申请单详情反审核')
INSERT INTO POWER (createDate,disable,_group,note,powerCode,powerName) values (getdate(),0,'外协入库申请单详情','采购管理->采购管理->外协入库申请单->外协入库申请单详情','OS_BOXBAR_WAREHOUSINGAPPLICATIONFORMDETAIL','外协箱号条码')
INSERT INTO POWER (createDate,disable,_group,note,powerCode,powerName) values (getdate(),0,'外协入库申请单详情','采购管理->采购管理->外协入库申请单->外协入库申请单详情','OS_PRINT_WAREHOUSINGAPPLICATIONFORMDETAIL','外协条码打印预览')
INSERT INTO POWER (createDate,disable,_group,note,powerCode,powerName) values (getdate(),0,'外协入库申请单详情','采购管理->采购管理->外协入库申请单->外协入库申请单详情','OS_INWAREHOUSE_WAREHOUSINGAPPLICATIONFORMDETAIL','外协入库')

--装备故障页面权限
INSERT INTO POWER (createDate,disable,_group,note,powerCode,powerName) values (getdate(),0,'装备故障','基础资料->装备信息->装备故障','ADD_EQUIPMENTNGTYPE','新增故障原因类别')
INSERT INTO POWER (createDate,disable,_group,note,powerCode,powerName) values (getdate(),0,'装备故障','基础资料->装备信息->装备故障','EDIT_EQUIPMENTNGTYPE','编辑故障原因类别')
INSERT INTO POWER (createDate,disable,_group,note,powerCode,powerName) values (getdate(),0,'装备故障','基础资料->装备信息->装备故障','DELTYPE_EQUIPMENTNGTYPE','删除故障原因类别')
INSERT INTO POWER (createDate,disable,_group,note,powerCode,powerName) values (getdate(),0,'装备故障','基础资料->装备信息->装备故障','DISABLETYPE_EQUIPMENTNGTYPE','停用故障原因类别')
INSERT INTO POWER (createDate,disable,_group,note,powerCode,powerName) values (getdate(),0,'故障原因信息','基础资料->装备信息->装备故障->故障原因信息','ADD_EQUIPMENTNG','新增故障原因')
INSERT INTO POWER (createDate,disable,_group,note,powerCode,powerName) values (getdate(),0,'故障原因信息','基础资料->装备信息->装备故障->故障原因信息','EDIT_EQUIPMENTNG','编辑故障原因')
INSERT INTO POWER (createDate,disable,_group,note,powerCode,powerName) values (getdate(),0,'故障原因信息','基础资料->装备信息->装备故障->故障原因信息','DEL_EQUIPMENTNG','删除故障原因')

--新增装备维修记录页面权限
INSERT INTO POWER (createDate,disable,_group,note,powerCode,powerName) values (getdate(),0,'装备维修记录','生产管理->生产管理->装备维修记录','ADD_EQUIPMENTRAPIRRECORD','新增装备维修记录')
INSERT INTO POWER (createDate,disable,_group,note,powerCode,powerName) values (getdate(),0,'装备维修记录','生产管理->生产管理->装备维修记录','EDIT_EQUIPMENTRAPIRRECORD','编辑装备维修记录')
INSERT INTO POWER (createDate,disable,_group,note,powerCode,powerName) values (getdate(),0,'装备维修记录','生产管理->生产管理->装备维修记录','DEL_EQUIPMENTRAPIRRECORD','删除装备维修记录')

--超级管理员分配外协入库申请单权限
INSERT INTO ROLE_POWER (POWER_ID,ROLE_ID) values ((select id from POWER where powerCode='OS_QUERY_WAREHOUSINGAPPLICATIONFORM'),(select id from ROLE where roleName='admin'))
INSERT INTO ROLE_POWER (POWER_ID,ROLE_ID) values ((select id from POWER where powerCode='OS_EXPORT_WAREHOUSINGAPPLICATIONFORM'),(select id from ROLE where roleName='admin'))
INSERT INTO ROLE_POWER (POWER_ID,ROLE_ID) values ((select id from POWER where powerCode='OS_ADD_WAREHOUSINGAPPLICATIONFORM'),(select id from ROLE where roleName='admin'))
INSERT INTO ROLE_POWER (POWER_ID,ROLE_ID) values ((select id from POWER where powerCode='OS_DEL_WAREHOUSINGAPPLICATIONFORM'),(select id from ROLE where roleName='admin'))
INSERT INTO ROLE_POWER (POWER_ID,ROLE_ID) values ((select id from POWER where powerCode='OS_SEE_WAREHOUSINGAPPLICATIONFORM'),(select id from ROLE where roleName='admin'))
INSERT INTO ROLE_POWER (POWER_ID,ROLE_ID) values ((select id from POWER where powerCode='OS_EXAMINE_WAREHOUSINGAPPLICATIONFORM'),(select id from ROLE where roleName='admin'))
INSERT INTO ROLE_POWER (POWER_ID,ROLE_ID) values ((select id from POWER where powerCode='OS_REVERSEEXAMINE_WAREHOUSINGAPPLICATIONFORM'),(select id from ROLE where roleName='admin'))

--超级管理员分配外协入库申请单详情权限
INSERT INTO ROLE_POWER (POWER_ID,ROLE_ID) values ((select id from POWER where powerCode='OS_ADDLINE_WAREHOUSINGAPPLICATIONFORMDETAIL'),(select id from ROLE where roleName='admin'))
INSERT INTO ROLE_POWER (POWER_ID,ROLE_ID) values ((select id from POWER where powerCode='OS_DELLINE_WAREHOUSINGAPPLICATIONFORMDETAIL'),(select id from ROLE where roleName='admin'))
INSERT INTO ROLE_POWER (POWER_ID,ROLE_ID) values ((select id from POWER where powerCode='OS_SPLITBOX_WAREHOUSINGAPPLICATIONFORMDETAIL'),(select id from ROLE where roleName='admin'))
INSERT INTO ROLE_POWER (POWER_ID,ROLE_ID) values ((select id from POWER where powerCode='OS_SAVE_WAREHOUSINGAPPLICATIONFORMDETAIL'),(select id from ROLE where roleName='admin'))
INSERT INTO ROLE_POWER (POWER_ID,ROLE_ID) values ((select id from POWER where powerCode='OS_EXAMINE_WAREHOUSINGAPPLICATIONFORMDETAIL'),(select id from ROLE where roleName='admin'))
INSERT INTO ROLE_POWER (POWER_ID,ROLE_ID) values ((select id from POWER where powerCode='OS_REVERSEEXAMINE_WAREHOUSINGAPPLICATIONFORMDETAIL'),(select id from ROLE where roleName='admin'))
INSERT INTO ROLE_POWER (POWER_ID,ROLE_ID) values ((select id from POWER where powerCode='OS_BOXBAR_WAREHOUSINGAPPLICATIONFORMDETAIL'),(select id from ROLE where roleName='admin'))
INSERT INTO ROLE_POWER (POWER_ID,ROLE_ID) values ((select id from POWER where powerCode='OS_PRINT_WAREHOUSINGAPPLICATIONFORMDETAIL'),(select id from ROLE where roleName='admin'))
INSERT INTO ROLE_POWER (POWER_ID,ROLE_ID) values ((select id from POWER where powerCode='OS_INWAREHOUSE_WAREHOUSINGAPPLICATIONFORMDETAIL'),(select id from ROLE where roleName='admin'))

--超级管理员分配装备故障页面权限
INSERT INTO ROLE_POWER (POWER_ID,ROLE_ID) values ((select id from POWER where powerCode='ADD_EQUIPMENTNGTYPE'),(select id from ROLE where roleName='admin'))
INSERT INTO ROLE_POWER (POWER_ID,ROLE_ID) values ((select id from POWER where powerCode='EDIT_EQUIPMENTNGTYPE'),(select id from ROLE where roleName='admin'))
INSERT INTO ROLE_POWER (POWER_ID,ROLE_ID) values ((select id from POWER where powerCode='DELTYPE_EQUIPMENTNGTYPE'),(select id from ROLE where roleName='admin'))
INSERT INTO ROLE_POWER (POWER_ID,ROLE_ID) values ((select id from POWER where powerCode='DISABLETYPE_EQUIPMENTNGTYPE'),(select id from ROLE where roleName='admin'))
INSERT INTO ROLE_POWER (POWER_ID,ROLE_ID) values ((select id from POWER where powerCode='ADD_EQUIPMENTNG'),(select id from ROLE where roleName='admin'))
INSERT INTO ROLE_POWER (POWER_ID,ROLE_ID) values ((select id from POWER where powerCode='EDIT_EQUIPMENTNG'),(select id from ROLE where roleName='admin'))
INSERT INTO ROLE_POWER (POWER_ID,ROLE_ID) values ((select id from POWER where powerCode='DEL_EQUIPMENTNG'),(select id from ROLE where roleName='admin'))

--超级管理员分配装备维修记录页面权限
INSERT INTO ROLE_POWER (POWER_ID,ROLE_ID) values ((select id from POWER where powerCode='ADD_EQUIPMENTRAPIRRECORD'),(select id from ROLE where roleName='admin'))
INSERT INTO ROLE_POWER (POWER_ID,ROLE_ID) values ((select id from POWER where powerCode='EDIT_EQUIPMENTRAPIRRECORD'),(select id from ROLE where roleName='admin'))
INSERT INTO ROLE_POWER (POWER_ID,ROLE_ID) values ((select id from POWER where powerCode='DEL_EQUIPMENTRAPIRRECORD'),(select id from ROLE where roleName='admin'))

