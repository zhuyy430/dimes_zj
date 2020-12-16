package com.digitzones.devmgr.model;

import java.util.Date;

import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.format.annotation.DateTimeFormat;

import com.digitzones.devmgr.model.MaintenanceStaffRecord;
import com.digitzones.model.RelatedDocumentType;

@Entity
@Table(name="MaintenanceRelatedDocument")
public class MaintenanceRelatedDocument {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	/**文件名称*/
	private String name;
	/**原始文件名称*/
	private String srcName;
	/**文件类型*/
	private String contentType;
	/**文件大小：字节*/
	private long fileSize;
	/**存储路径*/
	private String url;
	/**说明*/
	private String note;
	/**上传日期*/
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date uploadDate;
	/**上传用户名称*/
	private String uploadUsername;
	/**关联文档类型*/
	@ManyToOne
	@JoinColumn(name="Record_ID",foreignKey=@ForeignKey(value=ConstraintMode.NO_CONSTRAINT))
	@NotFound(action=NotFoundAction.IGNORE)
	private MaintenanceStaffRecord maintenanceStaffRecord;
	/**关联实体的id*/
	private Long relatedId;
	public MaintenanceStaffRecord getMaintenanceStaffRecord() {
		return maintenanceStaffRecord;
	}
	public void setMaintenanceStaffRecord(MaintenanceStaffRecord maintenanceStaffRecord) {
		this.maintenanceStaffRecord = maintenanceStaffRecord;
	}
	public Long getRelatedId() {
		return relatedId;
	}
	public void setRelatedId(Long relatedId) {
		this.relatedId = relatedId;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSrcName() {
		return srcName;
	}
	public void setSrcName(String srcName) {
		this.srcName = srcName;
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
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public Date getUploadDate() {
		return uploadDate;
	}
	public void setUploadDate(Date uploadDate) {
		this.uploadDate = uploadDate;
	}
	public String getUploadUsername() {
		return uploadUsername;
	}
	public void setUploadUsername(String uploadUsername) {
		this.uploadUsername = uploadUsername;
	}
}
