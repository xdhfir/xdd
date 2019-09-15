package com.my.yeshttp.base;

/**
 * 网络请求的通用返回格式
 */
public class DataResponse<T> {
    public String status;
    public String message;
    public String dataCount;
    public T data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
