package com.digitzones.dto;
import com.digitzones.model.InspectionRecord;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 检验记录详情
 * @author zdq
 */
public class InspectionRecordDetailDto implements Cloneable,Serializable{
	private static final long serialVersionUID = 1L;
	private String id;
	/**参数代码*/
	private String parameterCode;
	/**参数名称*/
	private String parameterName;
	/**单位*/
	private String unit;
	/**备注*/
	private String note;
	/**上线*/
	private Float upLine = 0f;
	/**下线*/
	private Float lowLine = 0f;
	/**标准值*/
	private Float standardValue = 0f;
	/**参数值*/
	private String parameterValue ;
	/**检验结果:NG或OK*/
	private String inspectionResult;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getParameterCode() {
        return parameterCode;
    }
    public void setParameterCode(String parameterCode) {
        this.parameterCode = parameterCode;
    }
    public String getParameterName() {
        return parameterName;
    }
    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }
    public String getUnit() {
        return unit;
    }
    public void setUnit(String unit) {
        this.unit = unit;
    }
    public String getNote() {
        return note;
    }
    public void setNote(String note) {
        this.note = note;
    }
    public Float getUpLine() {
        return upLine;
    }
    public void setUpLine(Float upLine) {
        this.upLine = upLine;
    }
    public Float getLowLine() {
        return lowLine;
    }
    public void setLowLine(Float lowLine) {
        this.lowLine = lowLine;
    }
    public Float getStandardValue() {
        return standardValue;
    }
    public void setStandardValue(Float standardValue) {
        this.standardValue = standardValue;
    }
    public String getParameterValue() {
        return parameterValue;
    }
    public void setParameterValue(String parameterValue) {
        this.parameterValue = parameterValue;
    }
    public String getInspectionResult() {
        return inspectionResult;
    }
    public void setInspectionResult(String inspectionResult) {
        this.inspectionResult = inspectionResult;
    }
}
