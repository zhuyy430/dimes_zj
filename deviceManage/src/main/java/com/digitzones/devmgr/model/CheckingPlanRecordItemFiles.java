package com.digitzones.devmgr.model;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
/**
 * 设备点检上传的文件实体
 */
@Entity
@GenericGenerator(name="id_generator",strategy = "uuid")
public class CheckingPlanRecordItemFiles implements Serializable {
    @Id
    @GeneratedValue(generator = "id_generator")
    private String id;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
    /**上传日期*/
    private Date uploadDate;
    /**文件名称*/
    private String fileName;
    /**文件类型*/
    private String contentType;
    /**文件大小：单位byte 字节*/
    private long fileSize;
    /**上传用户编码*/
    private String uploaderCode;
    /**上传用户名称*/
    private String uploaderName;
    /**图片上传路径*/
    private String url;
    /**点检项*/
    @ManyToOne
    @JoinColumn(name="ITEM_ID",foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private CheckingPlanRecordItem checkingPlanRecordItem;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public Date getUploadDate() {
        return uploadDate;
    }
    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }
    public String getFileName() {
        return fileName;
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    public String getContentType() {
        return contentType;
    }
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
    public long getFileSize() {
        return fileSize;
    }
    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }
    public String getUploaderCode() {
        return uploaderCode;
    }
    public void setUploaderCode(String uploaderCode) {
        this.uploaderCode = uploaderCode;
    }
    public String getUploaderName() {
        return uploaderName;
    }
    public void setUploaderName(String uploaderName) {
        this.uploaderName = uploaderName;
    }
    public CheckingPlanRecordItem getCheckingPlanRecordItem() {
        return checkingPlanRecordItem;
    }
    public void setCheckingPlanRecordItem(CheckingPlanRecordItem checkingPlanRecordItem) {
        this.checkingPlanRecordItem = checkingPlanRecordItem;
    }
}
