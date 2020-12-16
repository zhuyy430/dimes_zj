package com.digitzones.xml.model;

/**
 * xml操作结束后的结果实体
 */
public class Result {
    /**返回的消息字符串*/
    private String message = "操作失败!";
    /**状态码*/
    private Integer statusCode = 300;

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
}
