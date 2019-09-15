package com.my.yeshttp.rx;


import com.my.baselib.lib.utils.LogUtils;
import com.my.yeshttp.base.DataResponse;

import rx.Observable;
import rx.functions.Func1;

/**
 *
 */
public class ResolveTransform<T> implements Observable.Transformer<DataResponse<T>, T> {
    @Override
    public  Observable<T> call(Observable<DataResponse<T>> dataResponseObservable) {
        return dataResponseObservable.flatMap(new Func1<DataResponse<T>, Observable<T>>() {
            @Override
            public Observable<T> call(DataResponse<T> tDataResponse) {
                LogUtils.d("trans", tDataResponse.toString());
                if ("0".equals(tDataResponse.status)) {
                    LogUtils.d("response", tDataResponse.data.toString());
                    return Observable.just(tDataResponse.data);
                } else {
                    return Observable.error(new Exception(tDataResponse.message));
                }
            }
        });
    }
}
