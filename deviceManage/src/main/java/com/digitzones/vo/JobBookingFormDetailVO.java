package com.digitzones.vo;

public class JobBookingFormDetailVO {
    private String id;
    /**箱条码*/
    private String barCode;
    /**箱号*/
    private Integer boxNum;
    /**箱数*/
    private Double amountOfBoxes;
    /**报工数*/
    private Double amountOfJobBooking;
    /**报工人员名称*/
    private String jobBookerName;
    /**报工日期*/
    private String jobBookingDate;
    /**报工时间*/
    private String jobBookingTime;
    /**入库数量*/
    private Double amountOfInWarehouse;

    public String getJobBookingTime() {
        return jobBookingTime;
    }

    public void setJobBookingTime(String jobBookingTime) {
        this.jobBookingTime = jobBookingTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public Integer getBoxNum() {
        return boxNum;
    }

    public void setBoxNum(Integer boxNum) {
        this.boxNum = boxNum;
    }

    public Double getAmountOfBoxes() {
        return amountOfBoxes;
    }

    public void setAmountOfBoxes(Double amountOfBoxes) {
        this.amountOfBoxes = amountOfBoxes;
    }

    public Double getAmountOfJobBooking() {
        return amountOfJobBooking;
    }

    public void setAmountOfJobBooking(Double amountOfJobBooking) {
        this.amountOfJobBooking = amountOfJobBooking;
    }

    public String getJobBookerName() {
        return jobBookerName;
    }

    public void setJobBookerName(String jobBookerName) {
        this.jobBookerName = jobBookerName;
    }

    public String getJobBookingDate() {
        return jobBookingDate;
    }

    public void setJobBookingDate(String jobBookingDate) {
        this.jobBookingDate = jobBookingDate;
    }

    public Double getAmountOfInWarehouse() {
        return amountOfInWarehouse;
    }

    public void setAmountOfInWarehouse(Double amountOfInWarehouse) {
        this.amountOfInWarehouse = amountOfInWarehouse;
    }
}
