package com.digitzones.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
/**
 * 文件路径配置类
 * @author zdq
 * 2018年12月6日
 */
@Component
public class FilePathConfig {
	/**设备相关文档存储目录*/
	@Value("${sys.doc.deviceDocsPath}")
	private String deviceDocsPath;
	/**备品备件相关文档存储目录*/
	@Value("${sys.doc.sparepartDocsPath}")
	private String sparepartDocsPath;
	public String getDeviceDocsPath() {
		return deviceDocsPath;
	}
	public void setDeviceDocsPath(String deviceDocsPath) {
		this.deviceDocsPath = deviceDocsPath;
	}
	public String getSparepartDocsPath() {
		return sparepartDocsPath;
	}
	public void setSparepartDocsPath(String sparepartDocsPath) {
		this.sparepartDocsPath = sparepartDocsPath;
	}
}
