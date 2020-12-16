/*创建装备和物料关联*/
insert into InventoryEquipmentTypeMapping (INVENTORY_CODE,EQUIPMENTTYPE_ID,useFrequency)
select bom.InvCode as INVENTORY_CODE,eq.id as EQUIPMENTTYPE_ID, '1' as useFrequency from  ERP.UFDATA_002_2019.dbo.zz_bom bom
inner join EQUIPMENTTYPE eq on bom.Expr1=eq.code



--查询所有JL开头的物料未关联工序的数据
insert into INVENTORY_PROCESS (processRoute,standardBeat,INVENTORY_CODE,PROCESS_ID)
select 1,0,e.cInvCode,p.id from  WORKPIECE e  left join PROCESSES p on 1=1  where e.cInvCode like 'JL%' and p.id=7 and not exists(
		select * from INVENTORY_PROCESS ip where ip.INVENTORY_CODE=e.cInvCode and ip.PROCESS_ID=p.id)
