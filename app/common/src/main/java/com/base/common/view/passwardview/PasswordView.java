package com.base.common.view.passwardview;


import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.base.common.R;
import com.base.common.view.passwardview.grid.GridPasswordView;

import java.util.ArrayList;
import java.util.List;

public class PasswordView extends RelativeLayout
{

    private View mView;
    private ImageView mClose;
    private TextView mTitle;
    private TextView mForgetPwd;
    private GridPasswordView mPassword;
    private PasswordKeyboardView mKeyboard;
    private List<String> passwordList;//记录键盘输入的值
    private StringBuilder mValue;//最后保存的密码

    public PasswordView(Context context) {
        this(context, null);
    }

    public PasswordView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PasswordView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mView = View.inflate(context, R.layout.password_keyboard_view, null);

        initView();
        initEvent();

        addView(mView);
    }

    private void initView(){
        mPassword = (GridPasswordView) mView.findViewById(R.id.view_password);
        mClose = (ImageView) mView.findViewById(R.id.img_close);
        mTitle = (TextView) mView.findViewById(R.id.tv_title);
        mForgetPwd = (TextView) mView.findViewById(R.id.tv_forgetPwd);

        mKeyboard = (PasswordKeyboardView) mView.findViewById(R.id.view_keyboard);
        //打乱数字的位置
        mKeyboard.shuffleKeyboard();
    }

    private void initEvent(){
        mValue = new StringBuilder();
        passwordList = new ArrayList<>();
        mKeyboard.setIOnKeyboardListener(new PasswordKeyboardView.IOnKeyboardListener() {

            @Override
            public void onInsertKeyEvent(String text) {
                mValue.setLength(0);
                passwordList.add(text);
                for (int i = 0; i < passwordList.size(); i++) {
                    mValue.append(passwordList.get(i));
                }
                mPassword.setPassword(mValue.toString());
            }

            @Override
            public void onDeleteKeyEvent() {
                mValue.setLength(0);
                if(passwordList.size() != 0){
                    passwordList.remove(passwordList.size()-1);
                    for (int i = 0; i < passwordList.size(); i++) {
                        mValue.append(passwordList.get(i));
                    }
                    mPassword.setPassword(mValue.toString());
                }
            }
        });
    }

    //获取输入的密码
    public String getPassword(){
        return mValue.toString();
    }

    //取消
    public ImageView getCloseImageView(){
        return mClose;
    }

    //标题
    public TextView getTitleTextView(){
        return mTitle;
    }

    //忘记密码
    public TextView getForgetTextView() {
        return mForgetPwd;
    }

    //输入密码控件
    public GridPasswordView getPswView(){
        return mPassword;
    }


    public void setPasswordViewEnabled(boolean enabled) {
        int cout = getPswView().getChildCount();
        for (int i = 0; i < cout; i++) {
            View v = getPswView().getChildAt(i);
            if (v instanceof EditText) {
                v.setEnabled(false);
                break;
            }
        }
    }
}
