package com.digitzones.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ERPConfig {
    /**erp服务器url：eai导入xml文件使用*/
    @Value("${erp_url}")
    private String url;
    @Value("${sender}")
    private String sender;
    @Value("${receiver}")
    private String receiver;
    public String getSender() {
        return sender;
    }
    public void setSender(String sender) {
        this.sender = sender;
    }
    public String getReceiver() {
        return receiver;
    }
    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
}
