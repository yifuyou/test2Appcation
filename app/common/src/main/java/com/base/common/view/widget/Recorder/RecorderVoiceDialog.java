package com.base.common.view.widget.Recorder;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.base.common.R;
import com.base.common.utils.LogUtil;


/**
 * 名称：录音对话框
 * 附加注释：
 * 主要接口：
 */


public class RecorderVoiceDialog extends Dialog implements View.OnTouchListener {
    /**
     * 录音工具类
     */
    private RecorderUtil recorderUtil;
    /**
     * 上下文本
     */
    private Context context;
    /**
     * 点击录音按钮
     */
    private Button btnSpeck;
    /**
     * 记录当前状态图标
     */
    private ImageView ivVoice;
    /**
     * 记录音量
     */
    private ImageView ivVolume;
    /**
     * 描述
     */
    private TextView tvDes;
    /**
     * 当前手指的Y坐标位置
     */
    private float currentY;
    /**
     * 声音动画
     */
    private AnimationDrawable animationDrawable;
    private RelativeLayout rlDialog;

    public RecorderVoiceDialog(Context context) {
        super(context, R.style.dialog_bg);
    }

    public RecorderVoiceDialog(Context context, int themeResId) {
        super(context, themeResId);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_recorder_voice);
        btnSpeck = findViewById(R.id.speckBtn);
        ivVoice = findViewById(R.id.voiceIV);
        ivVolume = findViewById(R.id.volumeIV);
        rlDialog = findViewById(R.id.dialogRL);
        tvDes = findViewById(R.id.desTV);
        btnSpeck.setOnTouchListener(this);
        recorderUtil = new RecorderUtil(getContext());
        animationDrawable = (AnimationDrawable) getContext().getResources().getDrawable(
                R.drawable.anim_recorder);
    }

    /**
     * 手指是否上滑
     */
    private boolean isFingerUp;

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        LogUtil.e("xsasasasas", motionEvent.getY() + "");
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                rlDialog.setVisibility(View.VISIBLE);
                recorderUtil.startRecording();
                btnSpeck.setText(getContext().getText(R.string.press_speck));
                btnSpeck.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.C_FFFFFF));
                break;
            case MotionEvent.ACTION_MOVE:
                rlDialog.setVisibility(View.VISIBLE);
                currentY = motionEvent.getY();
                long timeInterval = recorderUtil.getTimeBetween();
                btnSpeck.setText(getContext().getText(R.string.up_finish));
                btnSpeck.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.C_A6A6A6));
                LogUtil.e("xsasasasas", timeInterval + "-------------------------");
                if (timeInterval > 1000) {
                    if (Math.abs(currentY) > 200) {
                        isFingerUp = true;
                        ivVoice.setImageResource(R.drawable.ic_recorder_cancel);
                        tvDes.setText(getContext().getString(R.string.up_point_cancel));
                        tvDes.setBackgroundResource(R.drawable.ic_recorder_cancel_bg);
                        ivVolume.setVisibility(View.GONE);
                        if (animationDrawable != null && animationDrawable.isRunning()) {
                            animationDrawable.stop();
                        }

                    } else {
                        isFingerUp = false;
                        ivVoice.setImageResource(R.drawable.ic_recorder_voice);
                        ivVolume.setVisibility(View.VISIBLE);
                        ivVolume.setBackground(animationDrawable);
                        tvDes.setBackgroundResource(R.color.C_00000000);
                        tvDes.setText(getContext().getString(R.string.up_send_cancel));
                        if (animationDrawable != null && !animationDrawable.isRunning()) {
                            animationDrawable.start();
                        }
                    }
                } else {
                    tvDes.setText(getContext().getString(R.string.speck_to_short));
                    ivVoice.setImageResource(R.drawable.ic_voice_to_short);
                    tvDes.setBackgroundResource(R.color.C_00000000);
                    ivVolume.setVisibility(View.GONE);
                }
                break;
            case MotionEvent.ACTION_UP:
                btnSpeck.setText(getContext().getText(R.string.press_speck));
                btnSpeck.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.C_FFFFFF));
                rlDialog.setVisibility(View.GONE);
                long time = recorderUtil.stopRecording();
                if (time < 1000) {
                    tvDes.setText(getContext().getString(R.string.speck_to_short));
                    ivVoice.setImageResource(R.drawable.ic_voice_to_short);
                    tvDes.setBackgroundResource(R.color.C_00000000);
                    ivVolume.setVisibility(View.GONE);
                    recorderUtil.cancelRecording();
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    dismiss();
                } else {
                    if (onRecorderListener != null && !isFingerUp) {
                        LogUtil.e("xsasasasas", recorderUtil.getFilePath() + "-------------------------" + recorderUtil.getTimeInterval());
                        onRecorderListener.onRecord(recorderUtil.getFilePath(), recorderUtil.getTimeInterval());
                    } else {
                        recorderUtil.cancelRecording();
                        dismiss();
                    }
                }

                break;
        }
        return false;
    }

    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        return this.btnSpeck.onTouchEvent(event);
    }

    public boolean dispatchTouchEvent(MotionEvent ev) {
        btnSpeck.onTouchEvent(ev);
        super.dispatchTouchEvent(ev);
        return true;
    }

    public void setOnRecorderListener(OnRecorderListener onRecorderListener) {
        this.onRecorderListener = onRecorderListener;
    }

    private OnRecorderListener onRecorderListener;

    public interface OnRecorderListener {
        void onRecord(String path, long duration);
    }
}
