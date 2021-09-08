package com.base.common.app;


import com.base.common.utils.SPUtils;
import com.base.common.utils.UIUtils;
import com.jeremyliao.liveeventbus.LiveEventBus;


public class LoginUtils {



    /**
     * 获取用户id
     *
     * @return
     */
    public static int getUserId() {
        return SPUtils.getInt(BaseConstant.GATEWAY_USERID, -1);
    }

    public static String getToken() {
        return SPUtils.getString(BaseConstant.GATEWAY_TOKEN, "");
    }

    /**
     * 获取token过期时间   毫秒数
     *
     * @return
     */
    public static long getExpireTime() {
        return SPUtils.getLong(BaseConstant.EXPIRE_TIME, -1);
    }


    /**
     * 检查登录是否过期
     *
     * @return false 过期
     */
    public static boolean isLogin() {
        //检查id
//        int id = getUserId();
//        if (id == -1) return false;
//        long expireTime = getExpireTime();
//        if (expireTime == -1) return false;
//        if (expireTime - System.currentTimeMillis() > 1000) {
//            return true;
//        }
        //检查token
        boolean bb = UIUtils.isNotEmpty(getToken());
//        if (!bb) return false;

        return bb;
        //检查用户数据
//        return UIUtils.isNotEmpty(SPUtils.getString(BaseConstant.USER_BEAN, null));
    }


    /**
     * 保存刷的token
     */
    public static void saveToken(String token, int userId) {
//        SPUtils.putLong(BaseConstant.EXPIRE_TIME, DateUtils.getHour(0, 12));
        SPUtils.putString(BaseConstant.GATEWAY_TOKEN, token);
        SPUtils.putInt(BaseConstant.GATEWAY_USERID, userId);
    }

    public static void loginOut() {
        SPUtils.remove(BaseConstant.USER_BEAN);
        SPUtils.remove(BaseConstant.GATEWAY_USERID);
        removeToken();
        SPUtils.remove(BaseConstant.EXPIRE_TIME);
        LiveEventBus.get(BaseConstant.EventKey.LOGIN_OUT).post(-1);
    }


    public static void removeToken() {
        SPUtils.remove(BaseConstant.GATEWAY_TOKEN);
    }

    //如出需要登陆下件
    public static void loginIn() {
        LiveEventBus.get(BaseConstant.LOGIN_IN).post(1);
    }

}
