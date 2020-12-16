package com.digitzones.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 系统配置
 * @author zdq
 * 2018年8月29日
 */
@Component
public class SysConfig {
	/**阈值*/
	@Value("${threshold}")
	private int threshold;
	@Value("${mcPowers}")
	private String mcPowers;
	public int getThreshold() {
		return threshold;
	}

	public void setThreshold(int threshold) {
		this.threshold = threshold;
	}

	public String getMcPowers() {
		return mcPowers;
	}

	public void setMcPowers(String mcPowers) {
		this.mcPowers = mcPowers;
	}
}
