package com.my.yeshttp.utils;

import com.my.yeshttp.rx.RxSchedulersHelper;

import rx.Observable;
import rx.Subscriber;

/**
 *
 */
public class HttpUtils {
    public static Object register(String startUrl,Class c){
        return HttpMethod.getInstance(startUrl).getBuilder().create(c);
    }
    @SuppressWarnings("unchecked")
    public static void connectNet(Observable observable, Subscriber subscriber) {
        observable.compose(RxSchedulersHelper.io_main()).subscribe(subscriber);
    }
}
