package com.base.common.model.http.upLoad;


import java.io.IOException;
import java.util.HashMap;

import okhttp3.RequestBody;
import retrofit2.Converter;

/**
 * 上传文件 请求转换器
 * Created by fangs on 2018/11/12.
 */
public class FileRequestBodyConverter implements Converter<HashMap<String, Object>, RequestBody> {


    public FileRequestBodyConverter() {

    }

    @Override
    public RequestBody convert(HashMap<String, Object> params) throws IOException {
        UploadOnSubscribe uploadOnSubscribe = (UploadOnSubscribe) params.get(UploadOnSubscribe.class.getSimpleName());
        if (uploadOnSubscribe == null) return null;
        if (params.containsKey("files")) {
            Object files = params.get("files");
            return UploadImageRetrofit.filesToMultipartBody(files, uploadOnSubscribe);

        }
        return null;
    }


}
