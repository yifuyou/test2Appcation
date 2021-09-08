package com.base.common.model.http.upLoad;


import androidx.annotation.NonNull;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static okhttp3.MultipartBody.FORM;


public class UploadImageRetrofit {

    private static String upLoadFileType = "file";//上传的文件类型key


    public static MultipartBody.Part getMultipartBody_part(String fileName, byte[] content) {
        // 创建 RequestBody，用于封装构建RequestBody
        RequestBody requestFile = RequestBody.create(FORM, content);
        try {
            fileName = URLEncoder.encode(fileName, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return MultipartBody.Part.createFormData(upLoadFileType, fileName, requestFile);
    }

    public static MultipartBody.Part getMultipartBody_part(File file) {
        // 创建 RequestBody，用于封装构建RequestBody
        RequestBody requestFile = RequestBody.create(FORM, file);
        String fileName = "";
        try {
            fileName = URLEncoder.encode(file.getName(), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // MultipartBody.Part  和后端约定文件上传 进度观察者 发射器（计算上传百分比）好Key，这里的partName是用image
        return MultipartBody.Part.createFormData(upLoadFileType, fileName, requestFile);
    }

    public static MultipartBody.Part getMultipartBody_part(String path) {
        File file = new File(path);
        // 创建 RequestBody，用于封装构建RequestBody
        RequestBody requestFile = RequestBody.create(FORM, file);
        String fileName = "";
        try {
            fileName = URLEncoder.encode(file.getName(), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // MultipartBody.Part  和后端约定文件上传 进度观察者 发射器（计算上传百分比）好Key，
        return MultipartBody.Part.createFormData(upLoadFileType, fileName, requestFile);
    }

    /**
     * 单文件上传
     */

    /**
     * 多文件上传
     * 用于把 File集合 或者 File路径集合 转化成 MultipartBody
     *
     * @param files             File列表或者 File 路径列表,或单一的文件和路经
     * @param uploadOnSubscribe 文件上传 进度观察者 发射器（计算上传百分比）
     * @return MultipartBody（retrofit 多文件文件上传）
     */
    public static synchronized MultipartBody filesToMultipartBody(Object files, UploadOnSubscribe uploadOnSubscribe) {

        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);


        long sumLeng = 0L;
        File file;
        String fileName = "";

        if (files instanceof String) {
            file = new File((String) files);
            sumLeng += file.length();
            try {
                fileName = URLEncoder.encode(file.getName(), "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            FileProgressRequestBody requestBody = getRequestBody(file, uploadOnSubscribe);
            builder.addFormDataPart(upLoadFileType, fileName, requestBody);
        } else if (files instanceof File) {
            file = (File) files;
            sumLeng += file.length();
            try {
                fileName = URLEncoder.encode(file.getName(), "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            FileProgressRequestBody requestBody = getRequestBody(file, uploadOnSubscribe);
            builder.addFormDataPart(upLoadFileType, fileName, requestBody);
        } else if (files instanceof List) {
            List list = (List) files;
            for (Object t : list) {

                if (t instanceof File) file = (File) t;
                else if (t instanceof String)
                    file = new File((String) t);//访问手机端的文件资源，保证手机端sdcdrd中必须有这个文件
                else break;

                sumLeng += file.length();

                FileProgressRequestBody requestBody = getRequestBody(file, uploadOnSubscribe);
                try {
                    fileName = URLEncoder.encode(file.getName(), "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                builder.addFormDataPart(upLoadFileType, fileName, requestBody);

            }
        } else return null;


        uploadOnSubscribe.setmSumLength(sumLeng);

        return builder.build();
    }


    public static MultipartBody.Part getPart(Object files, UploadOnSubscribe uploadOnSubscribe) {
        File file;
        if (files instanceof String) {
            file = new File((String) files);
        } else if (files instanceof File) {
            file = (File) files;
        } else return null;
        uploadOnSubscribe.setmSumLength(file.length());

        FileProgressRequestBody requestBody = getRequestBody(file, uploadOnSubscribe);

        String fileName = "";
        try {
            fileName = URLEncoder.encode(file.getName(), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return MultipartBody.Part.createFormData(upLoadFileType, fileName, requestBody);
    }


    /**
     * 生成单一的文件上传part
     *
     * @param file
     * @param subscribe
     * @return
     */
    private static FileProgressRequestBody getRequestBody(@NonNull File file, @NonNull UploadOnSubscribe subscribe) {
        return new FileProgressRequestBody(file, MultipartBody.FORM, subscribe);
    }


}
