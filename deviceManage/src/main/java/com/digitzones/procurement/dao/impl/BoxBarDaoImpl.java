package com.digitzones.procurement.dao.impl;

import com.digitzones.dao.impl.CommonDaoImpl;
import com.digitzones.procurement.dao.IBoxBarDao;
import com.digitzones.procurement.model.BoxBar;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Repository
public class BoxBarDaoImpl extends CommonDaoImpl<BoxBar> implements IBoxBarDao {
    public BoxBarDaoImpl() {
        super(BoxBar.class);
    }

    /**
     * 查找最大箱号条码
     * @return
     */
    @Override
    public Integer queryMaxBoxBarCode() {
        Object maxValue =  getSession().createNativeQuery("select MAX(barCode) from BoxBar").uniqueResult();
        if(maxValue!=null){
            return ((BigInteger)maxValue).intValue();
        }
        return null;
    }

    /**
     * 根据入库申请单号删除箱号条码信息
     * @param formNo
     */
    @Override
    public void deleteByFormNo(String formNo) {
        getSession().createNativeQuery("delete from BoxBar where formNo=?0").setParameter(0,formNo).executeUpdate();
    }
    /**
     * 根据工单编号查找最大报工单箱号
     * @param no
     * @return
     */
    @Override
    public Integer queryMaxBoxNumByNoAndProcessCode(String no,String processCode) {
        Integer maxValue =  (Integer)getSession().createNativeQuery("select MAX(bar.boxNum) from BoxBar bar inner join JobBookingFormDetail detail" +
                " on bar.barCode=detail.barCode where detail.no=?0 and detail.processCode=?1")
                .setParameter(0,no)
                .setParameter(1,processCode).uniqueResult();
        if(maxValue!=null){
            return maxValue;
        }
        return 0;
    }

    @Override
    public String queryMaxWarehouseNoByboxBarType(String type) {
        Date now=new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(now);
        return (String) getSession().createNativeQuery("select MAX(WarehouseNo) from BoxBar where year(WarehouseDate)=?0" +
                " and month(WarehouseDate)=?1 and day(WarehouseDate)=?2 and boxBarType=?3")
                .setParameter(0,c.get(Calendar.YEAR)).setParameter(1,c.get(Calendar.MONTH)+1)
                .setParameter(2,c.get(Calendar.DATE)).setParameter(3,type).uniqueResult();
    }
    /**
     * 根据箱号条码查找原材料箱号条码信息
     * @param boxBar
     * @return
     */
    @Override
    public List<BoxBar> queryRawMaterialByBoxBar(String boxBar) {
        return getSession().createNativeQuery("select * from BoxBar bar where bar.barCode in (select rm.barCode from RawMaterial rm inner join BoxBar boxBar on rm.JOBBOOKINGFORMDETAIL_ID=boxBar.fkey where boxBar.barCode=?0)")
                .setParameter(0,Integer.parseInt(boxBar))
                .addEntity(BoxBar.class)
                .list();
    }

    /**
     * 根据报工单 详情id删除箱号条码信息
     *
     * @param detailId
     */
    @Override
    public void deleteByJobBookingFormDetailId(String detailId) {
         getSession().createNativeQuery("delete from boxbar where fkey=?0 and tablename='JobBookingFormDetail'")
                .setParameter(0,detailId).executeUpdate();
    }
    /**
     * 根据箱条码和工单单号查找
     * @param barCode
     * @param no
     * @return
     */
    @Override
    public BoxBar queryByBarCodeAndNo(Long barCode, String no) {
        String sql = "select bar.* from BoxBar bar inner join WorkSheet ws on bar.inventoryCode=ws.moallocateInvcode" +
                " where ws.no=?0 and bar.barCode=?1  and bar.furnaceNumber=ws.stoveNumber" ;
        return (BoxBar) getSession().createNativeQuery(sql).setParameter(0,no).setParameter(1,barCode).addEntity(BoxBar.class).uniqueResult();
    }

    /**
     * 根据条码查询，是否同一个班次
     * @param barCodes
     * @return
     */
    @Override
    public boolean queryIsTheSameClass(String[] barCodes) {
        String sql = "select count(1) from JobBookingFormDetail   where barCode in (:barCodes) group by allClassCodes,forJobBookingDate";
        List list = getSession().createNativeQuery(sql).setParameterList("barCodes", barCodes).list();
       if(!CollectionUtils.isEmpty(list)&&list.size()>1){
           return false;
       }
        return true;
    }

    /**
     * 查询是否为同一个单据类型
     *
     * @param barCodeArr
     * @return
     */
    @Override
    public int queryIsTheSameBillType(String[] barCodeArr) {
        String sql = "select count(1) from BoxBar where barCode in (:barCodeArr) group by boxBarType";
        List list = getSession().createNativeQuery(sql).setParameterList("barCodeArr", barCodeArr).list();
        if(CollectionUtils.isEmpty(list)){
            return 0;
        }
        return list.size();
    }

    /**
     * 判断是否为报工单
     *
     * @param barCodeArr
     * @return
     */
    @Override
    public boolean queryIsTheJobBookingType(String[] barCodeArr) {
        String sql = "select count(1) from BoxBar where barCode in (:barCodes) and boxBarType='报工单'";
        int count = (int)getSession().createNativeQuery(sql).setParameterList("barCodes",barCodeArr).uniqueResult();
        if(count==barCodeArr.length){
           return true;
        }
        return false;
    }

    @Override
    public List<Object[]> queryInventoryByPositon(String ANum, String rowNum, String type) {
        /*String sql = "select  b.inventoryName,b.inventoryCode,case when SUBSTRING(positonCode,2,2)like '0%' then substring(positonCode,3,1) else substring(positonCode,2,2) end from (" +
                "select   * ,ROW_NUMBER() over ( PARTITION BY positonCode ORDER BY SurplusNum DESC ) rid FROM BoxBar) AS b WHERE rid = 1 and b.warehouseCode='20' and b.SurplusNum>0 and b.positonCode is not null and positonCode like ?0+'%' and positonCode like '%'+?1 ";
        if("after".equals(type)){
            sql+=" and SUBSTRING(positonCode,2,2)<=30";
        }else{
            sql+=" and SUBSTRING(positonCode,2,2)>30 and SUBSTRING(positonCode,2,2)<=60";
        }*/
        /*String sql = "select  b.inventoryName,b.inventoryCode,case when SUBSTRING(b.positonCode,2,2)like '0%' then substring(b.positonCode,3,1) else substring(b.positonCode,2,2) end from (" +
                "select *,ROW_NUMBER() over ( PARTITION BY boxbar.positonCode ORDER BY boxbar.SurplusNum DESC ) rid FROM BoxBar boxbar) AS b WHERE rid = 1 and b.warehouseCode='20' and b.SurplusNum>0 and b.positonCode is not null and b.positonCode like ?0+'%' and b.positonCode like '%'+?1 ";
        */
        String sql="";

        if(ANum.equals("X")){
            sql+="select b.inventoryName,b.inventoryCode,case when SUBSTRING(b.positonCode,2,2)like '0%' then substring(b.positonCode,3,1) else substring(b.positonCode,2,2) end as no,DATEDIFF(DD,b.WarehouseDate,GETDATE()) as dateNum from BoxBar b where exists(select * from (select boxbar.positonCode as pcode,MAX(boxbar.SurplusNum) as pNum from BoxBar boxbar group by boxbar.positonCode) as b1 where b1.pcode=b.positonCode and b1.pNum=b.SurplusNum) and b.warehouseCode='20' and b.SurplusNum>0 and b.positonCode is not null " +
                    "and b.positonCode like ?0+'%'";
        }else{
            sql+="select b.inventoryName,b.inventoryCode,case when SUBSTRING(b.positonCode,2,2)like '0%' then substring(b.positonCode,3,1) else substring(b.positonCode,2,2) end as no,DATEDIFF(DD,b.WarehouseDate,GETDATE()) as dateNum,b.positonCode from BoxBar b where exists(select * from (select boxbar.positonCode as pcode,MAX(boxbar.SurplusNum) as pNum from BoxBar boxbar group by boxbar.positonCode) as b1 where b1.pcode=b.positonCode and b1.pNum=b.SurplusNum) and b.warehouseCode='20' and b.SurplusNum>0 and b.positonCode is not null " +
                    "and b.positonCode like ?0+'%' and b.positonCode like '%'+?1 ";
        }


        if("before".equals(type)){
            sql+=" and SUBSTRING(positonCode,2,2)<=30";
        }else{
            sql+=" and SUBSTRING(positonCode,2,2)>30 and SUBSTRING(positonCode,2,2)<=60";
        }
        sql+=" order by b.positonCode";
        if(ANum.equals("X")){
            return getSession().createNativeQuery(sql).setParameter(0, ANum).list();
        }
        return getSession().createNativeQuery(sql).setParameter(0, ANum).setParameter(1, rowNum).list();
    }
}
