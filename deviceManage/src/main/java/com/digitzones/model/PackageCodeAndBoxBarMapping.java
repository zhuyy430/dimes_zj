package com.digitzones.model;

import com.digitzones.procurement.model.BoxBar;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity
@GenericGenerator(name="id_generator",strategy = "uuid")
public class PackageCodeAndBoxBarMapping {
    @Id
    @GeneratedValue(generator = "id_generator")
    private String id;

    @ManyToOne
    @JoinColumn(name="barCode")
    private BoxBar boxBar;

    /**是否是客户提供条码*/
    private boolean isCustomerProvisionPackageCode=false;

    /**包装箱条码*/
    private String PackageCode;

    /**包装数量*/
    private Double number;

    /**包装时间*/
    private Date takeOutTime;

    /**包装人编码*/
    private String employeeCode;

    /**包装人名称*/
    private String employeeName;

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

    public String getPackageCode() {
        return PackageCode;
    }

    public void setPackageCode(String packageCode) {
        PackageCode = packageCode;
    }

    public Double getNumber() {
        return number;
    }

    public void setNumber(Double number) {
        this.number = number;
    }

    public Date getTakeOutTime() {
        return takeOutTime;
    }

    public void setTakeOutTime(Date takeOutTime) {
        this.takeOutTime = takeOutTime;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public boolean isCustomerProvisionPackageCode() {
        return isCustomerProvisionPackageCode;
    }

    public void setCustomerProvisionPackageCode(boolean customerProvisionPackageCode) {
        isCustomerProvisionPackageCode = customerProvisionPackageCode;
    }
}
