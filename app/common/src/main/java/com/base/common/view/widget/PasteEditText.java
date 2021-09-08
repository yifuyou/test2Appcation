package com.base.common.view.widget;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatEditText;

/**
 * 可监听粘贴事件的EditText
 */
public class PasteEditText extends AppCompatEditText {

    public PasteEditText(Context context) {
        super(context);
    }

    public PasteEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PasteEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private OnPasteCallback mOnPasteCallback;

    @Override
    public boolean onTextContextMenuItem(int id) {
        switch (id) {
            case android.R.id.cut:
                // 剪切
                break;
            case android.R.id.copy:
                // 复制
                break;
            case android.R.id.paste:
                // 粘贴
                if (mOnPasteCallback != null) {
                    mOnPasteCallback.onPaste();
                }
                //调用剪贴板
//                ClipboardManager clip = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                //改变剪贴板中Content
//                if (clip != null) clip.setText("改变剪贴板中Content" + clip.getText());
                break;
        }
        return super.onTextContextMenuItem(id);
    }


    public interface OnPasteCallback {
        void onPaste();
    }

    public void setOnPasteCallback(OnPasteCallback onPasteCallback) {
        mOnPasteCallback = onPasteCallback;
    }
}
