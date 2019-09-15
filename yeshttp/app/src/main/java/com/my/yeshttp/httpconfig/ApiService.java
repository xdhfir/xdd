package com.my.yeshttp.httpconfig;



import com.my.yeshttp.base.DataResponse;
import com.my.yeshttp.bean.DemoBean;

import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 网络接口
 */
public interface ApiService {
    @FormUrlEncoded
    @POST(MURL.DEMOONEURL)
    Observable<DataResponse<DemoBean>> getDemoOne(@FieldMap Map<String, String> map);//demo1示例

    @GET(MURL.DEMOTWOURL)
    Observable<DataResponse<List<DemoBean>>> getDemoTwo();//demo2示例

}