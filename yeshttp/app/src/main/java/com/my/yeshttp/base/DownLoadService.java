package com.my.yeshttp.base;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 *
 */

public interface DownLoadService {
    @GET("{apk}")
    Call<ResponseBody> retrofitDownload(@Path("apk") String apk);//下载
}
