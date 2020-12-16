package com.digitzones.service.impl;

import com.digitzones.dao.IDeliveryPlanDetailDao;
import com.digitzones.model.DeliveryPlanDetail;
import com.digitzones.model.DeliveryPlanDetailBoxBarMapping;
import com.digitzones.model.Pager;
import com.digitzones.model.SalesSlip;
import com.digitzones.procurement.model.BoxBar;
import com.digitzones.procurement.model.Warehouse;
import com.digitzones.procurement.service.IBoxBarService;
import com.digitzones.procurement.service.IWarehouseService;
import com.digitzones.service.IDeliveryPlanDetailBoxBarMappingService;
import com.digitzones.service.IDeliveryPlanDetailService;
import com.digitzones.service.IJobBookingFormDetailService;
import com.digitzones.service.ISalesSlipService;
import com.digitzones.xml.DeliverySlipUtil;
import com.digitzones.xml.model.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

@Service
public class DeliveryPlanDetailServiceImpl implements IDeliveryPlanDetailService {
    @Autowired
    private IDeliveryPlanDetailDao deliveryPlanDetailDao;
    @Autowired
    private IBoxBarService boxBarService;
    @Autowired
    private IJobBookingFormDetailService jobBookingFormDetailService;
    @Autowired
    private IDeliveryPlanDetailService deliveryPlanDetailService;
    @Autowired
    private IDeliveryPlanDetailBoxBarMappingService deliveryPlanDetailBoxBarMappingService;
    @Autowired
    private DeliverySlipUtil deliverySlipUtil;
    @Autowired
    private ISalesSlipService salesSlipService;
    @Autowired
    private IWarehouseService warehouseService;
    /**
     * 分页查询对象
     * @param hql
     * @param pageNo
     * @param pageSize
     * @param values
     * @return
     */
    @Override
    public Pager queryObjs(String hql, int pageNo, int pageSize, Object... values) {
        return deliveryPlanDetailDao.findByPage(hql, pageNo, pageSize, values);
    }
    /**
     * 更新对象
     * @param obj
     */
    @Override
    public void updateObj(DeliveryPlanDetail obj) {
        deliveryPlanDetailDao.update(obj);
    }
    /**
     * 根据属性查询对象
     * @param name  属性名称
     * @param value 属性值
     * @return
     */
    @Override
    public DeliveryPlanDetail queryByProperty(String name, String value) {
        return deliveryPlanDetailDao.findSingleByProperty(name,value);
    }

    /**
     * 添加对象
     *
     * @param obj
     * @return
     */
    @Override
    public Serializable addObj(DeliveryPlanDetail obj) {
        return deliveryPlanDetailDao.save(obj);
    }
    /**
     * 根据id查询对象
     * @param id
     * @return
     */
    @Override
    public DeliveryPlanDetail queryObjById(Serializable id) {
        return deliveryPlanDetailDao.findById(id);
    }
    /**
     * 根据id删除对象
     * @param id
     */
    @Override
    public void deleteObj(Serializable id) {
        deliveryPlanDetailDao.deleteById(id);
    }
    /**
     * 根据报工单编号查找报工单详情信息
     * @param formNo 报工单编号
     * @return
     */
    @Override
    public List<DeliveryPlanDetail> queryByDeliveryPlanNo(String formNo) {
        return deliveryPlanDetailDao.findByHQL("from DeliveryPlanDetail detail where detail.deliveryPlan.formNo=?0",new Object[]{formNo});
    }
    /**
     * 销售出库
     */
    @Override
    public void outWarehouse(String[] barcodes,String[] ramounts,String deliverPlanDetailId,String warehouseCode) {
        Warehouse warehouse=warehouseService.queryByProperty("cWhCode",warehouseCode);
        for(int i=0;i<barcodes.length;i++){

            BoxBar b=boxBarService.queryBoxBarBybarCode(Long.valueOf(barcodes[i]));
            if(b.getSurplusNum()<Double.parseDouble(ramounts[i])){
                throw new RuntimeException(b.getBarCode()+"箱余量改变,请修改后重新提交");
            }

            DeliveryPlanDetail deliveryPlanDetail=deliveryPlanDetailService.queryObjById(deliverPlanDetailId);

            /*if(b.getBoxBarType().equals("报工单")){
                JobBookingFormDetail detail = jobBookingFormDetailService.queryObjById(b.getFkey());
                deliveryPlanDetail.setWarehouseCode(detail.getWarehouseCode());
                deliveryPlanDetail.setWarehouseName(detail.getWarehouseName());
            }else if(b.getBoxBarType().equals("临时条码单")){
                TemporaryBarcodeDetail detail=temporaryBarcodeDetailService.queryObjById(b.getFkey());

            }*/
            deliveryPlanDetail.setWarehouseCode(warehouse.getcWhCode());
            deliveryPlanDetail.setWarehouseName(warehouse.getcWhName());


            b.setSurplusNum(b.getSurplusNum()-Double.valueOf(ramounts[i]));
            if(b.getPackingNum()!=null){
                b.setPackingNum(b.getPackingNum()+Double.valueOf(ramounts[i]));
            }else{
                b.setPackingNum(Double.valueOf(ramounts[i]));
            }

            if(deliveryPlanDetail.getAmountOfSended()!=null){
                deliveryPlanDetail.setAmountOfSended(deliveryPlanDetail.getAmountOfSended()+Double.valueOf(ramounts[i]));
            }else{
                deliveryPlanDetail.setAmountOfSended(Double.valueOf(ramounts[i]));
            }
            if(deliveryPlanDetail.getAmountOfSended()>=deliveryPlanDetail.getAmountOfPlan()){
                deliveryPlanDetail.setStatus("完成");
            }else{
                deliveryPlanDetail.setStatus("部分发货");
            }
            boxBarService.updateObj(b);
            deliveryPlanDetailService.updateObj(deliveryPlanDetail);
            DeliveryPlanDetailBoxBarMapping dbm=new DeliveryPlanDetailBoxBarMapping();
            dbm.setBoxBar(b);
            dbm.setDeliveryPlanDetail(deliveryPlanDetail);
            dbm.setNumber(Double.valueOf(ramounts[i]));
            deliveryPlanDetailBoxBarMappingService.addObj(dbm);
        }
        DeliveryPlanDetail detail = deliveryPlanDetailDao.findById(deliverPlanDetailId);
        if(detail!=null){
            if(detail.getDeliveryPlan().getMakerCode()==null||detail.getDeliveryPlan().getMakerName()==null){
                detail.getDeliveryPlan().setMakerCode("admin");
                detail.getDeliveryPlan().setMakerName("admin");
            }
            SalesSlip salesSlip = salesSlipService.queryObjById(detail.getAutoId());
            if(salesSlip==null){
                throw new RuntimeException("销售订单子表记录丢失，操作失败!");
            }else{
                //生成ERP发货单
                Result result = deliverySlipUtil.generateDeliverySlip(detail,salesSlip);
                if(result.getStatusCode()!=200){
                    throw new RuntimeException("生成ERP发货单失败:" + result.getMessage());
                }
            }
        }else{
            throw new RuntimeException("没有找到该销售出库信息!");
        }
    }
}
