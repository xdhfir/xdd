package com.my.baselib.lib.view.nodeprogressview;
import java.util.List;
/**
 */
public interface NodeProgressAdapter{
    /**
     * 返回集合大小
     */
    int getCount();
    /**
     * 适配数据的集合
     */
    List<LogisticsData> getData();
}
