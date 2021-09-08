package com.base.common.model.http.err;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;

import org.json.JSONException;

import java.io.EOFException;
import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.ParseException;

/**
 * 主要处理外部错误和解析错误
 */
public class ApiException extends Exception implements ERROR {


    private int code;
    private String message;
    private String url;

    public ApiException(Throwable cause, int code) {
        super(cause);
        this.message = cause.getMessage();
        this.code = code;
    }

    public static ApiException handleException(Throwable e) {
        ApiException ex = null;
        if (e instanceof HttpException) {             //HTTP错误
            HttpException httpException = (HttpException) e;
            switch (httpException.code()) {
                case ERR400:
                    ex = new ApiException(e, ERR400);
                    ex.setMessage("（错误请求）请求的语法错误");
                    break;
                case ERR401:
                    ex = new ApiException(e, ERR401);
                    ex.setMessage("（未授权）请求要求身份验证。");
                    break;
                case ERR403:
                    ex = new ApiException(e, ERR403);
                    ex.setMessage("（禁止）服务器拒绝请求。");
                    break;
                case ERR404:
                    ex = new ApiException(e, ERR404);
                    ex.setMessage("（未找到）服务器找不到请求");
                    break;
                case ERR405:
                    ex = new ApiException(e, ERR405);
                    ex.setMessage("（方法禁用）禁用请求中指定的方法。");
                    break;
                case ERR406:
                    ex = new ApiException(e, ERR406);
                    ex.setMessage("（不接受）无法使用请求的内容特性响应请求");
                    break;
                case ERR407:
                    ex = new ApiException(e, ERR407);
                    ex.setMessage("需要代理授权");
                    break;
                case ERR408:
                    ex = new ApiException(e, ERR408);
                    ex.setMessage("（请求超时）服务器等候请求时发生超时");
                    break;
                case ERR409:
                    ex = new ApiException(e, ERR409);
                    ex.setMessage("（冲突）服务器在完成请求时发生冲突。");
                    break;
                case ERR410:
                    ex = new ApiException(e, ERR410);
                    ex.setMessage("（已删除）如果请求的资源已永久删除");
                    break;
                case ERR411:
                    ex = new ApiException(e, ERR411);
                    ex.setMessage("（需要有效长度）服务器不接受不含有效内容长度标头字段的请求。");
                    break;
                case ERR412:
                    ex = new ApiException(e, ERR412);
                    ex.setMessage("（未满足前提条件）服务器未满足请求者在请求中设置的其中一个前提条件。");
                    break;
                case ERR413:
                    ex = new ApiException(e, ERR413);
                    ex.setMessage("（请求实体过大）服务器无法处理请求，因为请求实体过大，超出服务器的处理能力。");
                    break;
                case ERR414:
                    ex = new ApiException(e, ERR414);
                    ex.setMessage("（  请求的 URI  过长） 请求的 URI（通常为网址）过长，服务器无法处理。");
                    break;
                case ERR415:
                    ex = new ApiException(e, ERR415);
                    ex.setMessage("（不支持的媒体类型）请求的格式不受请求页面的支持。");
                    break;
                case ERR416:
                    ex = new ApiException(e, ERR416);
                    ex.setMessage("（请求范围不符合要求）如果页面无法提供请求的范围，则服务器会返回此状态代码。");
                    break;
                case ERR417:
                    ex = new ApiException(e, ERR417);
                    ex.setMessage("（未满足期望值）服务器未满足\"期望\"请求标头字段的要求。");
                    break;
                case ERR500:
                    ex = new ApiException(e, ERR500);
                    ex.setMessage("（服务器内部错误） 服务器遇到错误，无法完成请求。");
                    break;
                case ERR501:
                    ex = new ApiException(e, ERR501);
                    ex.setMessage(" 服务器不具备完成请求的功能。");
                    break;
                case ERR502:
                    ex = new ApiException(e, ERR502);
                    ex.setMessage("错误网关");
                    break;
                case ERR503:
                    ex = new ApiException(e, ERR503);
                    ex.setMessage("服务不可用");
                    break;
                case ERR504:
                    ex = new ApiException(e, ERR504);
                    ex.setMessage("网关超时");
                    break;
                case ERR505:
                    ex = new ApiException(e, ERR505);
                    ex.setMessage("HTTP 版本不受支持");
                    break;
                default:
                    try {
                        String ss2 = httpException.response().errorBody().source().readUtf8();
                        ex = new ApiException(e, UNKNOWN_ERR);
                        ex.setMessage(ss2); //均视为网络错误
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }

                    break;
            }

            if (ex != null) {
                ex.setUrl(httpException.response().toString());
            }
            return ex;
        } else if (e instanceof JsonMappingException
                || e instanceof JSONException
                || e instanceof ParseException) {


            ex = new ApiException(e, PARSE_ERR);
            ex.setMessage("解析错误   " + e.getMessage());           //均视为解析错误
            return ex;
        } else if (e instanceof ConnectException) {
            ex = new ApiException(e, NETWORK_ERROR);
            ex.setMessage("网络断开，请检查网络");  //均视为网络错误
            return ex;
        } else if (e instanceof SocketTimeoutException) {
            ex = new ApiException(e, NETWORK_ERROR);
            ex.setMessage("网络不给力，连接超时，请重试");  //均视为网络错误
            return ex;
        } else if (e instanceof EOFException) {
            ex = new ApiException(e, NETWORK_ERROR);
            ex.setMessage("服务器开了个小差~");          //未知错误
            return ex;
        } else if (e instanceof UnknownHostException) {
            ex = new ApiException(e, NETWORK_ERROR);
            ex.setMessage("网络异常,请检查网络设置");
            return ex;
        } else {
            ex = new ApiException(e, UNKNOWN_ERR);
            ex.setMessage(e.getMessage());          //未知错误
            return ex;
        }


    }


    public void setCode(int code) {
        this.code = code;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }


}
