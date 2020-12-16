--����erpȡֵ��װ����ͼ
EXEC sp_addlinkedserver
@server='ERP',--�����ʵķ�����������ϰ����ֱ��ʹ��Ŀ�������IP����ȡ�������磺JOY��
@srvproduct='',
@provider='SQLOLEDB',
--@datasrc='192.168.18.214' --Ҫ���ʵķ�����
@datasrc='192.168.10.199'
--ʹ��sp_addlinkedsrvlogin �������û���¼����
EXEC sp_addlinkedsrvlogin
'ERP', --�����ʵķ������������������sp_addlinkedserver��ʹ�ñ���JOY��������Ҳ��JOY��
'false',
NULL,
'sa', --�ʺ�
--'1!deshine' --����
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












--���װ�����뵼��˵�
insert into MODULE (disabled,leaf,name,url,PARENT_ID) values (0,1,'װ�����뵼��','console/jsp/equipment_import.jsp',(select id from MODULE where name='װ����Ϣ' and leaf=0))

--���װ�����뵼��˵�adminȨ��
insert into ROLE_MODULE (MODULE_ID,ROLE_ID) values ((select m.id from MODULE m where name='װ�����뵼��'),(select r.id from ROLE r where roleName='admin'))










--���ҳ����Ϣ
INSERT INTO MODULE (disabled,leaf,name,priority,url,PARENT_ID) values(0,1,'��Э������뵥',3,'procurement/outsourcingWarehousingApplicationForm.jsp',(select id from MODULE where name='�ɹ�����' and PARENT_ID is not null and leaf=0))
INSERT INTO MODULE (disabled,leaf,name,priority,url,PARENT_ID) values(0,1,'װ������',NUll,'console/jsp/equipmentNGType.jsp',(select id from MODULE where name='װ����Ϣ' and PARENT_ID is not null and leaf=0))
INSERT INTO MODULE (disabled,leaf,name,priority,url,PARENT_ID) values(0,1,'װ��ά�޼�¼',11,'console/jsp/equipmentRapirRecord.jsp',(select id from MODULE where name='��������' and PARENT_ID is not null and leaf=0))

--����ҳ��
INSERT INTO ROLE_MODULE (MODULE_ID,ROLE_ID) values((select id from MODULE where name='��Э������뵥'),(select id from ROLE where roleName='admin'))
INSERT INTO ROLE_MODULE (MODULE_ID,ROLE_ID) values((select id from MODULE where name='װ������'),(select id from ROLE where roleName='admin'))
INSERT INTO ROLE_MODULE (MODULE_ID,ROLE_ID) values((select id from MODULE where name='װ��ά�޼�¼'),(select id from ROLE where roleName='admin'))

--������Э������뵥ҳ��Ȩ��
INSERT INTO POWER (createDate,disable,_group,note,powerCode,powerName) values (getdate(),0,'��Э������뵥','�ɹ�����->�ɹ�����->��Э������뵥','OS_QUERY_WAREHOUSINGAPPLICATIONFORM','��Э��ѯ������뵥')
INSERT INTO POWER (createDate,disable,_group,note,powerCode,powerName) values (getdate(),0,'��Э������뵥','�ɹ�����->�ɹ�����->��Э������뵥','OS_EXPORT_WAREHOUSINGAPPLICATIONFORM','��Э����������뵥')
INSERT INTO POWER (createDate,disable,_group,note,powerCode,powerName) values (getdate(),0,'��Э������뵥','�ɹ�����->�ɹ�����->��Э������뵥','OS_ADD_WAREHOUSINGAPPLICATIONFORM','��Э����������뵥')
INSERT INTO POWER (createDate,disable,_group,note,powerCode,powerName) values (getdate(),0,'��Э������뵥','�ɹ�����->�ɹ�����->��Э������뵥','OS_DEL_WAREHOUSINGAPPLICATIONFORM','��Эɾ��������뵥')
INSERT INTO POWER (createDate,disable,_group,note,powerCode,powerName) values (getdate(),0,'��Э������뵥','�ɹ�����->�ɹ�����->��Э������뵥','OS_SEE_WAREHOUSINGAPPLICATIONFORM','��Э�鿴������뵥')
INSERT INTO POWER (createDate,disable,_group,note,powerCode,powerName) values (getdate(),0,'��Э������뵥','�ɹ�����->�ɹ�����->��Э������뵥','OS_EXAMINE_WAREHOUSINGAPPLICATIONFORM',' ��Э���������뵥')
INSERT INTO POWER (createDate,disable,_group,note,powerCode,powerName) values (getdate(),0,'��Э������뵥','�ɹ�����->�ɹ�����->��Э������뵥','OS_REVERSEEXAMINE_WAREHOUSINGAPPLICATIONFORM','��Э�����������뵥')

--������Э������뵥����ҳ��Ȩ��
INSERT INTO POWER (createDate,disable,_group,note,powerCode,powerName) values (getdate(),0,'��Э������뵥����','�ɹ�����->�ɹ�����->��Э������뵥->��Э������뵥����','OS_ADDLINE_WAREHOUSINGAPPLICATIONFORMDETAIL','��Э������뵥���������')
INSERT INTO POWER (createDate,disable,_group,note,powerCode,powerName) values (getdate(),0,'��Э������뵥����','�ɹ�����->�ɹ�����->��Э������뵥->��Э������뵥����','OS_DELLINE_WAREHOUSINGAPPLICATIONFORMDETAIL','��Э������뵥����ɾ����')
INSERT INTO POWER (createDate,disable,_group,note,powerCode,powerName) values (getdate(),0,'��Э������뵥����','�ɹ�����->�ɹ�����->��Э������뵥->��Э������뵥����','OS_SPLITBOX_WAREHOUSINGAPPLICATIONFORMDETAIL','��Э������뵥�������')
INSERT INTO POWER (createDate,disable,_group,note,powerCode,powerName) values (getdate(),0,'��Э������뵥����','�ɹ�����->�ɹ�����->��Э������뵥->��Э������뵥����','OS_SAVE_WAREHOUSINGAPPLICATIONFORMDETAIL','��Э������뵥���鱣��')
INSERT INTO POWER (createDate,disable,_group,note,powerCode,powerName) values (getdate(),0,'��Э������뵥����','�ɹ�����->�ɹ�����->��Э������뵥->��Э������뵥����','OS_EXAMINE_WAREHOUSINGAPPLICATIONFORMDETAIL','��Э������뵥�������')
INSERT INTO POWER (createDate,disable,_group,note,powerCode,powerName) values (getdate(),0,'��Э������뵥����','�ɹ�����->�ɹ�����->��Э������뵥->��Э������뵥����','OS_REVERSEEXAMINE_WAREHOUSINGAPPLICATIONFORMDETAIL','��Э������뵥���鷴���')
INSERT INTO POWER (createDate,disable,_group,note,powerCode,powerName) values (getdate(),0,'��Э������뵥����','�ɹ�����->�ɹ�����->��Э������뵥->��Э������뵥����','OS_BOXBAR_WAREHOUSINGAPPLICATIONFORMDETAIL','��Э�������')
INSERT INTO POWER (createDate,disable,_group,note,powerCode,powerName) values (getdate(),0,'��Э������뵥����','�ɹ�����->�ɹ�����->��Э������뵥->��Э������뵥����','OS_PRINT_WAREHOUSINGAPPLICATIONFORMDETAIL','��Э�����ӡԤ��')
INSERT INTO POWER (createDate,disable,_group,note,powerCode,powerName) values (getdate(),0,'��Э������뵥����','�ɹ�����->�ɹ�����->��Э������뵥->��Э������뵥����','OS_INWAREHOUSE_WAREHOUSINGAPPLICATIONFORMDETAIL','��Э���')

--װ������ҳ��Ȩ��
INSERT INTO POWER (createDate,disable,_group,note,powerCode,powerName) values (getdate(),0,'װ������','��������->װ����Ϣ->װ������','ADD_EQUIPMENTNGTYPE','��������ԭ�����')
INSERT INTO POWER (createDate,disable,_group,note,powerCode,powerName) values (getdate(),0,'װ������','��������->װ����Ϣ->װ������','EDIT_EQUIPMENTNGTYPE','�༭����ԭ�����')
INSERT INTO POWER (createDate,disable,_group,note,powerCode,powerName) values (getdate(),0,'װ������','��������->װ����Ϣ->װ������','DELTYPE_EQUIPMENTNGTYPE','ɾ������ԭ�����')
INSERT INTO POWER (createDate,disable,_group,note,powerCode,powerName) values (getdate(),0,'װ������','��������->װ����Ϣ->װ������','DISABLETYPE_EQUIPMENTNGTYPE','ͣ�ù���ԭ�����')
INSERT INTO POWER (createDate,disable,_group,note,powerCode,powerName) values (getdate(),0,'����ԭ����Ϣ','��������->װ����Ϣ->װ������->����ԭ����Ϣ','ADD_EQUIPMENTNG','��������ԭ��')
INSERT INTO POWER (createDate,disable,_group,note,powerCode,powerName) values (getdate(),0,'����ԭ����Ϣ','��������->װ����Ϣ->װ������->����ԭ����Ϣ','EDIT_EQUIPMENTNG','�༭����ԭ��')
INSERT INTO POWER (createDate,disable,_group,note,powerCode,powerName) values (getdate(),0,'����ԭ����Ϣ','��������->װ����Ϣ->װ������->����ԭ����Ϣ','DEL_EQUIPMENTNG','ɾ������ԭ��')

--����װ��ά�޼�¼ҳ��Ȩ��
INSERT INTO POWER (createDate,disable,_group,note,powerCode,powerName) values (getdate(),0,'װ��ά�޼�¼','��������->��������->װ��ά�޼�¼','ADD_EQUIPMENTRAPIRRECORD','����װ��ά�޼�¼')
INSERT INTO POWER (createDate,disable,_group,note,powerCode,powerName) values (getdate(),0,'װ��ά�޼�¼','��������->��������->װ��ά�޼�¼','EDIT_EQUIPMENTRAPIRRECORD','�༭װ��ά�޼�¼')
INSERT INTO POWER (createDate,disable,_group,note,powerCode,powerName) values (getdate(),0,'װ��ά�޼�¼','��������->��������->װ��ά�޼�¼','DEL_EQUIPMENTRAPIRRECORD','ɾ��װ��ά�޼�¼')

--��������Ա������Э������뵥Ȩ��
INSERT INTO ROLE_POWER (POWER_ID,ROLE_ID) values ((select id from POWER where powerCode='OS_QUERY_WAREHOUSINGAPPLICATIONFORM'),(select id from ROLE where roleName='admin'))
INSERT INTO ROLE_POWER (POWER_ID,ROLE_ID) values ((select id from POWER where powerCode='OS_EXPORT_WAREHOUSINGAPPLICATIONFORM'),(select id from ROLE where roleName='admin'))
INSERT INTO ROLE_POWER (POWER_ID,ROLE_ID) values ((select id from POWER where powerCode='OS_ADD_WAREHOUSINGAPPLICATIONFORM'),(select id from ROLE where roleName='admin'))
INSERT INTO ROLE_POWER (POWER_ID,ROLE_ID) values ((select id from POWER where powerCode='OS_DEL_WAREHOUSINGAPPLICATIONFORM'),(select id from ROLE where roleName='admin'))
INSERT INTO ROLE_POWER (POWER_ID,ROLE_ID) values ((select id from POWER where powerCode='OS_SEE_WAREHOUSINGAPPLICATIONFORM'),(select id from ROLE where roleName='admin'))
INSERT INTO ROLE_POWER (POWER_ID,ROLE_ID) values ((select id from POWER where powerCode='OS_EXAMINE_WAREHOUSINGAPPLICATIONFORM'),(select id from ROLE where roleName='admin'))
INSERT INTO ROLE_POWER (POWER_ID,ROLE_ID) values ((select id from POWER where powerCode='OS_REVERSEEXAMINE_WAREHOUSINGAPPLICATIONFORM'),(select id from ROLE where roleName='admin'))

--��������Ա������Э������뵥����Ȩ��
INSERT INTO ROLE_POWER (POWER_ID,ROLE_ID) values ((select id from POWER where powerCode='OS_ADDLINE_WAREHOUSINGAPPLICATIONFORMDETAIL'),(select id from ROLE where roleName='admin'))
INSERT INTO ROLE_POWER (POWER_ID,ROLE_ID) values ((select id from POWER where powerCode='OS_DELLINE_WAREHOUSINGAPPLICATIONFORMDETAIL'),(select id from ROLE where roleName='admin'))
INSERT INTO ROLE_POWER (POWER_ID,ROLE_ID) values ((select id from POWER where powerCode='OS_SPLITBOX_WAREHOUSINGAPPLICATIONFORMDETAIL'),(select id from ROLE where roleName='admin'))
INSERT INTO ROLE_POWER (POWER_ID,ROLE_ID) values ((select id from POWER where powerCode='OS_SAVE_WAREHOUSINGAPPLICATIONFORMDETAIL'),(select id from ROLE where roleName='admin'))
INSERT INTO ROLE_POWER (POWER_ID,ROLE_ID) values ((select id from POWER where powerCode='OS_EXAMINE_WAREHOUSINGAPPLICATIONFORMDETAIL'),(select id from ROLE where roleName='admin'))
INSERT INTO ROLE_POWER (POWER_ID,ROLE_ID) values ((select id from POWER where powerCode='OS_REVERSEEXAMINE_WAREHOUSINGAPPLICATIONFORMDETAIL'),(select id from ROLE where roleName='admin'))
INSERT INTO ROLE_POWER (POWER_ID,ROLE_ID) values ((select id from POWER where powerCode='OS_BOXBAR_WAREHOUSINGAPPLICATIONFORMDETAIL'),(select id from ROLE where roleName='admin'))
INSERT INTO ROLE_POWER (POWER_ID,ROLE_ID) values ((select id from POWER where powerCode='OS_PRINT_WAREHOUSINGAPPLICATIONFORMDETAIL'),(select id from ROLE where roleName='admin'))
INSERT INTO ROLE_POWER (POWER_ID,ROLE_ID) values ((select id from POWER where powerCode='OS_INWAREHOUSE_WAREHOUSINGAPPLICATIONFORMDETAIL'),(select id from ROLE where roleName='admin'))

--��������Ա����װ������ҳ��Ȩ��
INSERT INTO ROLE_POWER (POWER_ID,ROLE_ID) values ((select id from POWER where powerCode='ADD_EQUIPMENTNGTYPE'),(select id from ROLE where roleName='admin'))
INSERT INTO ROLE_POWER (POWER_ID,ROLE_ID) values ((select id from POWER where powerCode='EDIT_EQUIPMENTNGTYPE'),(select id from ROLE where roleName='admin'))
INSERT INTO ROLE_POWER (POWER_ID,ROLE_ID) values ((select id from POWER where powerCode='DELTYPE_EQUIPMENTNGTYPE'),(select id from ROLE where roleName='admin'))
INSERT INTO ROLE_POWER (POWER_ID,ROLE_ID) values ((select id from POWER where powerCode='DISABLETYPE_EQUIPMENTNGTYPE'),(select id from ROLE where roleName='admin'))
INSERT INTO ROLE_POWER (POWER_ID,ROLE_ID) values ((select id from POWER where powerCode='ADD_EQUIPMENTNG'),(select id from ROLE where roleName='admin'))
INSERT INTO ROLE_POWER (POWER_ID,ROLE_ID) values ((select id from POWER where powerCode='EDIT_EQUIPMENTNG'),(select id from ROLE where roleName='admin'))
INSERT INTO ROLE_POWER (POWER_ID,ROLE_ID) values ((select id from POWER where powerCode='DEL_EQUIPMENTNG'),(select id from ROLE where roleName='admin'))

--��������Ա����װ��ά�޼�¼ҳ��Ȩ��
INSERT INTO ROLE_POWER (POWER_ID,ROLE_ID) values ((select id from POWER where powerCode='ADD_EQUIPMENTRAPIRRECORD'),(select id from ROLE where roleName='admin'))
INSERT INTO ROLE_POWER (POWER_ID,ROLE_ID) values ((select id from POWER where powerCode='EDIT_EQUIPMENTRAPIRRECORD'),(select id from ROLE where roleName='admin'))
INSERT INTO ROLE_POWER (POWER_ID,ROLE_ID) values ((select id from POWER where powerCode='DEL_EQUIPMENTRAPIRRECORD'),(select id from ROLE where roleName='admin'))

