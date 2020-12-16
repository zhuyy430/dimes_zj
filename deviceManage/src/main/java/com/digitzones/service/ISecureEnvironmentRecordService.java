package com.digitzones.service;

import java.util.Date;

import com.digitzones.model.SecureEnvironmentRecord;
/**
 * 安环记录service
 * @author zdq
 * 2018年7月19日
 */
public interface ISecureEnvironmentRecordService extends ICommonService<SecureEnvironmentRecord> {
	/**
	 * 查询最严重的故障记录
	 * @param date
	 * @return
	 */
	public SecureEnvironmentRecord queryMostSeriousSecureEnvironmentRecordByDate(Date date);
}
