package com.dysen.opencard.common;

import java.io.UnsupportedEncodingException;

/**
 * Created by dysen on 1/31/2018.
 */

public class StrUtils {

    /**
     * 获取交易状态码
     * @param bytes
     * @return
     */
    public static String getTransStateeCode(byte[] bytes) {

        try {
            return new String(bytes, 14, 20, "gbk").trim();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 	00000：交易成功
     * *	PUB_xxx：前置错误交易失败
     * 	其它：贷记卡错误交易失败
     * 	HB0188：没有找到客户记录
     * @param bytes
     * @return
     */
    public static String getState(byte[] bytes){

        String code = getTransStateeCode(bytes), str = "";
        if (code.equals("00000")) {
            str = ParamUtils.TRANS_SUCCESS;
        }else if (code.contains("PUB_xxx")) {
            str = ParamUtils.TRANS_PUB;
        }if (code.equals("其它")) {
            str = ParamUtils.TRANS_OTHER;
        }if (code.equals("HB0188")) {
            str = ParamUtils.TRANS_HB;
        }
        ParamUtils.transState = str;
        return str;
    }

    public static String getState(String code){

        String str = "";
        if (code.equals("0000")) {
            str = ParamUtils.TRANS_SUCCESS;
        }else if (code.contains("PUB_xxx")) {
            str = ParamUtils.TRANS_PUB;
        }if (code.equals("其它")) {
            str = ParamUtils.TRANS_OTHER;
        }if (code.equals("HB0188")) {
            str = ParamUtils.TRANS_HB;
        }
        ParamUtils.transState = str;
        return str;
    }
}
