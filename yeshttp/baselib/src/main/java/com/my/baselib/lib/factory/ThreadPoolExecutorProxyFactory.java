package com.my.baselib.lib.factory;
/**
 *xd
 */
public class ThreadPoolExecutorProxyFactory {
    static ThreadPoolExecutorProxy sNormalPoolExecutorProxy;

    /**
     * @return 得到普通的线程池代理
     */
    public static ThreadPoolExecutorProxy getNormalPoolExecutorProxy() {
        if (sNormalPoolExecutorProxy == null) {
            synchronized (ThreadPoolExecutorProxyFactory.class) {
                if (sNormalPoolExecutorProxy == null) {
                    sNormalPoolExecutorProxy = new ThreadPoolExecutorProxy(5, 5, 3000);
                }
            }
        }
        return sNormalPoolExecutorProxy;
    }
}
