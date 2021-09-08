package com.base.zbar;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import com.base.common.utils.DrawableUtil;
import com.google.zxing.WriterException;

public class CreateQRCode {

    public static String createQRCode(@NonNull String content) {
        try {
            Bitmap bitmap = CodeCreator.createQRCode(content, 200, 200, null);
            if (bitmap != null) {
//                byte[] bytes = DrawableUtil.bitmapToByte(bitmap, true);
//                return Base64.getEncoder().encodeToString(bytes);
//                return new String(bytes, "utf-8");
                return DrawableUtil.bitmpToString(bitmap);
            }

        } catch (WriterException e) {
            e.printStackTrace();
        }

        return "";
    }

}
