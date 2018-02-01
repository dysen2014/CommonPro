package com.dysen.commom_library.utils;

import java.io.UnsupportedEncodingException;

/**
 * Created by dysen on 1/31/2018.
 */

public class CodeFormatUtils {


    /**
     * byte数组截取转换成字符串
     * @param bytes     byte数组
     * @param index     下标
     * @param offset    截取长度
     * @return
     */
    public static String byte2StrIntercept(byte[] bytes, int index, int offset) {

        try {
            return new String(bytes, index, offset, "gbk").trim();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getUTF8StringFromGBKString(String gbkStr) {
        try {
            return new String(getUTF8BytesFromGBKString(gbkStr), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new InternalError();
        }
    }

    public static byte[] getUTF8BytesFromGBKString(String gbkStr) {
        int n = gbkStr.length();

        LogUtils.i("=========长度========" + n);
        byte[] utfBytes = new byte[3 * n];

        int k = 0;
        for (int i = 0; i < n; i++) {
            int m = gbkStr.charAt(i);
            if (m < 128 && m >= 0) {
                utfBytes[k++] = (byte) m;
                continue;
            }
            utfBytes[k++] = (byte) (0xe0 | (m >> 12));
            utfBytes[k++] = (byte) (0x80 | ((m >> 6) & 0x3f));
            utfBytes[k++] = (byte) (0x80 | (m & 0x3f));
        }
        if (k < utfBytes.length) {
            byte[] tmp = new byte[k];
            System.arraycopy(utfBytes, 0, tmp, 0, k);
            return tmp;
        }
        return utfBytes;
    }
}
