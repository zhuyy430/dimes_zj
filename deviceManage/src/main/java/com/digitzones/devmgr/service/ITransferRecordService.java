package com.digitzones.devmgr.service;

import java.util.Date;
import java.util.List;

import com.digitzones.devmgr.model.MaintenanceStaff;
import com.digitzones.devmgr.model.TransferRecord;
import com.digitzones.service.ICommonService;
/**
 * 转单业务逻辑
 * @author zhuyy430
 *
 */
public interface ITransferRecordService extends ICommonService<TransferRecord> {
	/**
	 * 转单分配
	 * @param emp
	 * @param trecord
	 */
	public void updateTransferRecordRecord(MaintenanceStaff maintenanceStaff,TransferRecord trecord,String operator);
	 /**
	    * 根据日期查找维修转单数
	    * @param date
	    * @return
	    */
	   public long queryTransferRecordCountByDate(Date date);
	  /**
	   * 查询转单记录
	   */
	   public List<TransferRecord> queryAllTransferRecord(boolean status);
}
