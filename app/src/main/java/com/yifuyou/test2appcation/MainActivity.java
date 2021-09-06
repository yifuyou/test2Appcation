package com.yifuyou.test2appcation;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.yifuyou.common_http.RequestAPI;
import com.yifuyou.common_http.util.LogUtil;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        RequestAPI.init();
    }


    @Override
    public int checkPermission(String permission, int pid, int uid) {
        return super.checkPermission(permission, pid, uid);
    }

    public void click(View view){
        RequestAPI.request("", new RequestAPI.CallBack() {
            @Override
            public void onSuccess(Object ss) {
                if(ss instanceof String){ LogUtil.i("tag",ss.toString());}
            }

            @Override
            public void onFail() {

            }
        },"data1008611248");
    }

}
