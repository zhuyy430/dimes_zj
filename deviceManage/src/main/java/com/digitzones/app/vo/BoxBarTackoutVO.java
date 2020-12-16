package com.digitzones.app.vo;

import java.util.Date;

public class BoxBarTackoutVO {
    /**发生时间*/
    private Date date;
    /**发生时间*/
    private String dateToString;
    /**单据*/
    private String type;
    /**数量变化*/
    private String num;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDateToString() {
        return dateToString;
    }

    public void setDateToString(String dateToString) {
        this.dateToString = dateToString;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }
}
