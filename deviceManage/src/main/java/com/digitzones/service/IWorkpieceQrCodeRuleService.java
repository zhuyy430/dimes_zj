package com.digitzones.service;

import java.util.List;

import com.digitzones.model.WorkpieceQrCodeRule;
/**
 * 工件二维码规则Service
 * @author Administrator
 */
public interface IWorkpieceQrCodeRuleService extends ICommonService<WorkpieceQrCodeRule> {
	/**
	 * 根据打印机服务器IP查找正在使用的工件二维码对象
	 * @param printerIp 
	 * @param status 
	 * @return
	 */
	public List<WorkpieceQrCodeRule> queryByPrinterIp(String printerIp,boolean status);
	
	/**
	 * 更新工件二维码规则
	 * @param workpieceQrCodeRule
	 */
	public void updateWorkpieceQRCodeRule(WorkpieceQrCodeRule workpieceQrCodeRule);
}
