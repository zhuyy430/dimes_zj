package com.digitzones.model;

import com.digitzones.procurement.model.BoxBar;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;


/**
 * 发货单详情与箱子关联实体
 */
@Entity
@GenericGenerator(name="id_generator",strategy = "uuid")
public class DeliveryPlanDetailBoxBarMapping {

    @Id
    @GeneratedValue(generator = "id_generator")
    private String id;

    @ManyToOne
    @JoinColumn(name="barCode")
    private BoxBar boxBar;

    @ManyToOne
    @JoinColumn(name="DELIVERYPLANDETAIL_ID")
    private DeliveryPlanDetail deliveryPlanDetail;

    /**领取数量*/
    private Double number;

    /**领取时间*/
    private Date takeOutTime;

    public Date getTakeOutTime() {
        return takeOutTime;
    }

    public void setTakeOutTime(Date takeOutTime) {
        this.takeOutTime = takeOutTime;
    }

    public Double getNumber() {
        return number;
    }

    public void setNumber(Double number) {
        this.number = number;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BoxBar getBoxBar() {
        return boxBar;
    }

    public void setBoxBar(BoxBar boxBar) {
        this.boxBar = boxBar;
    }

    public DeliveryPlanDetail getDeliveryPlanDetail() {
        return deliveryPlanDetail;
    }

    public void setDeliveryPlanDetail(DeliveryPlanDetail deliveryPlanDetail) {
        this.deliveryPlanDetail = deliveryPlanDetail;
    }
}
