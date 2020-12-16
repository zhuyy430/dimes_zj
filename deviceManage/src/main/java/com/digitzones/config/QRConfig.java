package com.digitzones.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
/**
 * 二维码功能配置
 * @author zdq
 * 2018年7月14日
 */
@Component
public class QRConfig {
	@Value("${employeeQR}")
	private String employee;
	@Value("${qrPath}")
	private String qrPath;
	public String getQrPath() {
		return qrPath;
	}
	public void setQrPath(String qrPath) {
		this.qrPath = qrPath;
	}
	public String getEmployee() {
		return employee;
	}
	public void setEmployee(String employee) {
		this.employee = employee;
	}
}
