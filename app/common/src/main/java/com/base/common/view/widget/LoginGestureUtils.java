package com.base.common.view.widget;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.base.common.utils.RxTimer;
import com.base.common.utils.SPUtils;
import com.base.common.utils.UIUtils;

public class LoginGestureUtils {

    private static final String LOCK9VIEWUTILS_PSW = "Lock9ViewUtils_psw";
    private static final String ERROR_COUNT = "error_count";//剩余错误次数
    private static final String ERROR_TIME = "error_time";//锁定开始的时间
    private static final int lock_time = 300;//300秒  5分钟
    private static final int error_count = 5;//最多错几次  达到后锁定5分钟


    private TextView hintDescTv;//提示
    private LoginGestureView loginGestureView;  //用于绘制手势
    private LoginGestureView linkageParentView;//联动的提示按扭
    private String password;
    private GestureCallback onGestureCallback;

    private boolean isUseLocalPwd = false;//是否使用本地密码

    private RxTimer rxTimer;

    //是否有手势登录
    public static boolean isLoginGesture() {
        return UIUtils.isNotEmpty(SPUtils.getString(LOCK9VIEWUTILS_PSW, ""));
    }


    public LoginGestureUtils(TextView hintDescTv, LoginGestureView loginGestureView, LoginGestureView linkageParentView, GestureCallback onGestureCallback) {
        this.hintDescTv = hintDescTv;
        this.linkageParentView = linkageParentView;
        this.loginGestureView = loginGestureView;
        this.onGestureCallback = onGestureCallback;
        init();
    }


    private void init() {
        password = SPUtils.getString(LOCK9VIEWUTILS_PSW, "");

        // 手势登录逻辑
        if (!password.isEmpty()) {

            loginGestureView.setSettingMode(false); // 输入模式
            if (linkageParentView != null) linkageParentView.setVisibility(View.GONE);
            hintDescTv.setText("请输入密码");//


            statusChecked();//状态检查
            loginGestureView.setGestureCallback(new LoginGestureView.GestureCallback() {

                @Override
                public void onNodeConnected(@NonNull int[] numbers) {
                }

                @Override
                public boolean onGestureFinished(@NonNull int[] numbers) {

                    if (numbers.length < 4) {
                        hintDescTv.setTextColor(Color.RED);
                        hintDescTv.setText("至少链接4个点,请重新绘制");

                        if (linkageParentView != null && linkageParentView.getVisibility() == View.VISIBLE) {
                            linkageParentView.clearLinkage();
                        }

                        return true;
                    }

                    StringBuilder builder = new StringBuilder();
                    for (int number : numbers) {
                        builder.append(number);
                    }
                    String inputPwd = builder.toString();

                    if (isUseLocalPwd) {
                        if (!password.equals(inputPwd)) {
                            setPasswordErr();
                        } else {
                            hintDescTv.setTextColor(Color.GRAY);
                            hintDescTv.setText("码输入正确");
                            SPUtils.putInt(ERROR_COUNT, error_count);
                            if (onGestureCallback != null) {
                                onGestureCallback.onGestureFinished(inputPwd);
                            }
                        }
                    }
                    //不使用本地密码，要请请求网络
                    else {
                        if (onGestureCallback != null) {
                            onGestureCallback.onGestureFinished(inputPwd);
                        }
                    }

                    return false;
                }

            });

        }
        //设置手势密码逻辑
        else {

            if (linkageParentView != null) {
                linkageParentView.setVisibility(View.VISIBLE);
                linkageParentView.setSettingMode(true);
            }

            loginGestureView.setGestureCallback(new LoginGestureView.GestureCallback() {

                @Override
                public void onNodeConnected(@NonNull int[] numbers) {

                    if (linkageParentView != null && linkageParentView.getVisibility() == View.VISIBLE) {
                        linkageParentView.autoLinkage(numbers);
                    }
                }

                @Override
                public boolean onGestureFinished(@NonNull int[] numbers) {
                    StringBuilder builder = new StringBuilder();
                    for (int number : numbers) {
                        builder.append(number);
                    }
                    String value = builder.toString();
                    String tmp = SPUtils.getString("tmp_password", "");
                    if (tmp.isEmpty()) {
                        if (numbers.length < 4) {
                            hintDescTv.setTextColor(Color.RED);
                            hintDescTv.setText("至少链接4个点,请重新绘制");

                            if (linkageParentView != null && linkageParentView.getVisibility() == View.VISIBLE) {
                                linkageParentView.clearLinkage();
                            }

                            return true;
                        } else {
                            if (linkageParentView != null && linkageParentView.getVisibility() == View.VISIBLE) {
                                linkageParentView.clearLinkage();
                            }
                            hintDescTv.setTextColor(Color.GRAY);
                            hintDescTv.setText("请再次绘制解锁图案");
                            SPUtils.putString("tmp_password", value);
                        }
                    } else {
                        if (numbers.length < 4) {
                            SPUtils.putString("tmp_password", "");
                            hintDescTv.setTextColor(Color.RED);
                            hintDescTv.setText("至少链接4个点,请重新绘制");
                            if (linkageParentView != null && linkageParentView.getVisibility() == View.VISIBLE) {
                                linkageParentView.clearLinkage();
                            }
                            return true;
                        } else {
                            if (tmp.equals(value)) {
                                hintDescTv.setText("设置手势密码成功");
                                hintDescTv.setTextColor(Color.GRAY);
                                SPUtils.putString("tmp_password", "");
                                if (isUseLocalPwd) {
                                    savePassWord(value);
                                } else {
//                                    savePassWord("111");
                                }

                                // 网络请求
                                // 提示
                                if (linkageParentView != null && linkageParentView.getVisibility() == View.VISIBLE) {
                                    linkageParentView.clearLinkage();
                                }
                                //密码设置成功
                                if (onGestureCallback != null) {
                                    onGestureCallback.onGestureFinished(value);
                                }
                            } else {
                                if (linkageParentView != null && linkageParentView.getVisibility() == View.VISIBLE) {
                                    linkageParentView.clearLinkage();
                                }
                                hintDescTv.setText("两次绘制不一致,请重新绘制");
                                hintDescTv.setTextColor(Color.RED);
                                SPUtils.putString("tmp_password", "");
                                return true;
                            }
                        }
                    }
                    return false;
                }

            });


        }
    }


    private int getErrCount() {
        return SPUtils.getInt(ERROR_COUNT, error_count);
    }

    private void statusChecked() {
        int errorCount = getErrCount();
        //禁用状态
        if (errorCount <= 0) {
            if (loginGestureView != null) {
                loginGestureView.setEnabled(false);
            }
            long end = SPUtils.getLong(ERROR_TIME, 0) / 1000 + lock_time;
            long now = System.currentTimeMillis() / 1000;
            if (end - now > 0) {
                if (hintDescTv != null) {
                    hintDescTv.setTextColor(Color.RED);
                    hintDescTv.setText("请在" + (end - now) + "秒后再试");
                    timeCount();
                }
                return;
            }
            return;
        }


        //倒计时结束，重新启用
        SPUtils.putInt(ERROR_COUNT, error_count);
        if (loginGestureView != null) {
            loginGestureView.setEnabled(true);
        }
        if (hintDescTv != null) {
            hintDescTv.setTextColor(Color.GRAY);
            hintDescTv.setText("请输入密码");
        }

    }

    private void timeCount() {
        if (rxTimer == null) {
            rxTimer = new RxTimer();
        } else {
            rxTimer.cancel();
        }
        if (loginGestureView != null) {
            loginGestureView.setEnabled(false);
        }
        rxTimer.interval(1000, new RxTimer.RxAction() {
            @Override
            public void action(long number) {
                long end = SPUtils.getLong(ERROR_TIME, 0) / 1000 + lock_time;
                long now = System.currentTimeMillis() / 1000;
                if (end - now > 0) {
                    if (hintDescTv != null) {
                        hintDescTv.setTextColor(Color.RED);
                        hintDescTv.setText("请在" + (end - now) + "秒后再试");

                    }
                } else {
                    rxTimer.cancel();

                    SPUtils.putInt(ERROR_COUNT, error_count);
                    if (loginGestureView != null) {
                        loginGestureView.setEnabled(true);
                    }
                    if (hintDescTv != null) {
                        hintDescTv.setTextColor(Color.GRAY);
                        hintDescTv.setText("请输入密码");
                    }
                }
            }
        });

    }

    public void onDestroy() {
        if (rxTimer != null) {
            rxTimer.cancel();
        }
        hintDescTv = null;
        loginGestureView = null;
        linkageParentView = null;
        onGestureCallback = null;
    }


    //密码输入错误
    private void setPasswordErr() {
        //错误次数
        int errorCount = getErrCount();
        //错误次数消耗完毕，5分钟后再试
        if (errorCount - 1 <= 0) {
            //倒计时
            SPUtils.putLong(ERROR_TIME, System.currentTimeMillis());
            SPUtils.putInt(ERROR_COUNT, 0);
            hintDescTv.setTextColor(Color.RED);
            hintDescTv.setText("请在" + lock_time + "秒后再试");
            timeCount();
        } else {
            hintDescTv.setTextColor(Color.RED);
            errorCount -= 1;
            hintDescTv.setText("手势密码不正确,剩余尝试次数" + errorCount + "次");
            SPUtils.putInt(ERROR_COUNT, errorCount);
        }
    }


    //设置验证失败，在网络验证
    public void setCheckERR() {
        if (!isUseLocalPwd) {
            setPasswordErr();
        }
    }

    public void setCheckSuccess() {
        if (!isUseLocalPwd) {
            //验证成功，重置错误次数
            SPUtils.putInt(ERROR_COUNT, error_count);
        }
    }

    //保存手势密码
    public static void savePassWord(String password) {
        if (UIUtils.isEmpty(password)) {
            SPUtils.remove(LOCK9VIEWUTILS_PSW);
        } else {
            SPUtils.putString(LOCK9VIEWUTILS_PSW, password);
        }
    }


    public interface GestureCallback {

        /**
         * 手势完成, 如果是设置密码，则两次输入一致
         */
//        void onGestureFinished(@NonNull int[] numbers);
        void onGestureFinished(@NonNull String numbers);
    }

}
