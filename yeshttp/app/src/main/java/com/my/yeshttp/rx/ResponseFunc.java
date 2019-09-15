package com.my.yeshttp.rx;


import com.my.yeshttp.base.DataResponse;

import rx.functions.Func1;

/**
 * 预处理类
 */
public class ResponseFunc<T> implements Func1<DataResponse<T>, T> {
    private static final int RESP_OK = 200;
    //此处逻辑根据约定报文进行修改，实现逻辑即可
    @Override
    public T call(DataResponse<T> httpResult) {
        if (httpResult.getStatus().equals("0")) {
            throw new IllegalStateException(httpResult.message);
        }
        return httpResult.getData();
    }
}
