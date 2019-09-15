package com.my.baselib.lib.view.nodeprogressview;

import java.io.Serializable;

/**
 */
public class LogisticsData implements Serializable {
    private String content;//内容
    private String ftime;
    private String time;//时间

    public LogisticsData(String time, String content) {
        this.content = content;
        this.time = time;
    }

    public String getFtime() {
        return ftime;
    }

    public LogisticsData setFtime(String ftime) {
        this.ftime = ftime;
        return this;

    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "LogisticsData{" +
                "context='" + content + '\'' +
                ", ftime='" + ftime + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
