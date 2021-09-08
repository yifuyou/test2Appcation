package com.base.common.utils;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class MD5AndSHA {

    /**
     * 创建人 gc
     *
     * @return
     */
    public static String SHA256Encode(String message, String key) {
        String hash = "";
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
            sha256_HMAC.init(secret_key);
            byte[] bytes = sha256_HMAC.doFinal(message.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (byte item : bytes) {
                sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString().toUpperCase();
        } catch (Exception e) {
            System.out.println("Error HmacSHA256 ===========" + e.getMessage());
        }
        return hash;
    }


    /**
     * 创建人 gc
     *
     * @param string
     * @return
     */
    public static String md5Encode(String string) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(string.getBytes("UTF-8"));
            StringBuilder result = new StringBuilder();
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result.append(temp);
            }
            return result.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * MD5加密 生成32位md5码
     *
     * @param str             待加密字符串
     * @param encryptionCount 加密次数，为空则只加密一次
     * @return 返回32位md5码
     */
    public static String md5Encode(String str, int... encryptionCount) {
        return encipherment("MD5", str, encryptionCount.length == 0 ? 1 : encryptionCount[0]);
    }

    /**
     * SHA加密 生成40位SHA码
     *
     * @param encryptionCount 加密次数
     * @param str             待加密字符串
     * @return 返回40位SHA码
     */
    public static String shaEncode(String str, int... encryptionCount) {
        return encipherment("SHA", str, encryptionCount.length == 0 ? 1 : encryptionCount[0]);
    }

    public static String sha256_Encode(String str, int... encryptionCount) {
        return encipherment("SHA-256", str, encryptionCount.length == 0 ? 1 : encryptionCount[0]);
    }

    public static String sha256_Encode(String str, String salt, int... encryptionCount) {
        return encipherment("SHA-256", str, salt, encryptionCount.length == 0 ? 1 : encryptionCount[0]);
    }


    /**
     * @param algorithm 请求的算法的名称
     * @param str
     * @return
     */
    private static String encipherment(@NonNull String algorithm, String str, int encryptionCount) {
        return encipherment(algorithm, str, null, encryptionCount);
    }

    /**
     * @param algorithm 请求的算法的名称
     * @param str
     * @return
     */
    public static String encipherment(@NonNull String algorithm, String str, @Nullable String salt, int encryptionCount) {

        try {
            MessageDigest sha = MessageDigest.getInstance(algorithm);

            byte[] bytes = str.getBytes("UTF-8");

            if (!TextUtils.isEmpty(salt)) {
                byte[] bytes_salt = salt.getBytes("UTF-8");
                sha.reset();
                sha.update(bytes_salt);
            }

            int mcount = Math.max(1, encryptionCount);
            byte[] shaBytes = sha.digest(bytes);

            if (mcount > 1) {
                for (int i = 0; i < mcount - 1; i++) {
                    sha.reset();
                    shaBytes = sha.digest(shaBytes);
                }
            }


            StringBuilder hexValue = new StringBuilder();
            for (int i = 0; i < shaBytes.length; i++) {
                int val = ((int) shaBytes[i]) & 0xff;
                if (val < 16) {
                    hexValue.append("0");
                }
                hexValue.append(Integer.toHexString(val));
            }

            return hexValue.toString();


        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }


    }


}
