package com.base.common.model.http.upLoad;



import com.base.common.model.bean.BankBean;
import com.base.common.model.bean.UploadImageBean;
import com.base.common.model.http.HttpUtils;

import java.util.HashMap;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface UploadService {


    static UploadService getUpLoadFilesService(String baseUrl) {
        return HttpUtils.getInstance(baseUrl).getUpLoadFilesService(UploadService.class);
    }


    /**
     * 银行卡验证地址
     * @param url https://ccdcapi.alipay.com/validateAndCacheCardInfo.json?_input_charset=utf-8&cardNo=%1$s&cardBinCheck=true
     * @return
     */
    @GET
    Observable<BankBean> bank_discern_url(@Url String url, @Query("_input_charset") String _input_charset, @Query("cardNo") String cardNo, @Query("cardBinCheck") String cardBinCheck);





    /**
     * 多文件上传
     *
     * @param params
     * @return
     */
    @POST("p/oss/upload")
    Observable<Object> ossUpload(@Body HashMap<String, Object> params);


    /**
     * 单文件上传
     * /api/sysUser/upload
     *
     * @param part
     * @return
     */
    @POST("api/sysUser/uploadImg")
    @Multipart
    Observable<UploadImageBean> ossUpload(@Part MultipartBody.Part part);


    /**
     * 文件下载
     */
    @Streaming
    @GET
    Observable<ResponseBody> downLoadFileApk(@Url String url);

}
