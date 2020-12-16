package com.digitzones.test;

import com.digitzones.model.Pager;
import com.digitzones.model.WorkSheet;
import com.digitzones.service.IWorkSheetService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xuxf
 * @date 2020-7-20
 * @time 16:20
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations= {"classpath*:springContext-*.xml"})
public class test {
    @Resource
    private IWorkSheetService workSheetService;
    @Test
    public void test(){
        /*String sql="select * from WorkSheet w left join (select MIN(mr.pickingDate) as firstMaterialDate,SUM(md.amount) as materialCount,SUM(md.amountOfBoxes) as materialBoxNum,mr.WORKSHEETNO as no from MaterialRequisitionDetail md " +
        "left join MaterialRequisition mr on mr.formNo=md.materialRequisition.formNo group by mr.workSheet.no) as material on w.no=material.no " +
        "left join (select MAX(jf.jobBookingDate) as lastJobbookingDate,SUM(jd.amountOfJobBooking) as jobbookingCount,SUM(jd.amountOfBoxes) as jobbookingBoxNum," +
        "SUM(jd.amountOfInWarehouse) as jobbookingInwarehouseCount,jd.no as no from JobBookingFormDetail jd left join JobBookingForm jf on jf.formNo=jd.jobBookingForm.formNo group by jd.no)  as jobbooking on w.no=jobbooking.no where 1=1 ";
*/

        String sql="select w.*,material.firstMaterialDate,material.materialCount,material.materialBoxNum,jobbooking.lastJobbookingDate,jobbooking.jobbookingCount,jobbooking.jobbookingBoxNum,jobbooking.jobbookingInwarehouseCount from WORKSHEET w left join (select MIN(mr.pickingDate) as firstMaterialDate,SUM(md.amount) as materialCount,SUM(md.amountOfBoxes)as materialBoxNum,mr.WORKSHEETNO as no from MaterialRequisitionDetail md \n" +
                "left join MaterialRequisition mr on mr.formNo=md.MATERIAL_REQUISITION_FORMNO group by mr.WORKSHEETNO) as material on w.no=material.no \n" +
                "left join (select MAX(jf.jobBookingDate)as lastJobbookingDate,SUM(jd.amountOfJobBooking)as jobbookingCount,SUM(jd.amountOfBoxes) as jobbookingBoxNum,\n" +
                "SUM(jd.amountOfInWarehouse)as jobbookingInwarehouseCount,jd.no as no from JobBookingFormDetail jd left join JobBookingForm jf on jf.formNo=jd.JOBBOOKING_CODE group by jd.no)  as jobbooking on w.no=jobbooking.no \n";

        //List<WorkSheet> list=workSheetDao.queryByMonitoring(sql);
        List<Object> data = new ArrayList<>();
        Pager<WorkSheet> pager=workSheetService.queryByMonitoring(sql,1,20,data.toArray());
        System.out.println();
    }
}
