package com.dysen.commom_library.utils;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsManager;

import java.util.ArrayList;

/**
 * Created by dysen on 2017/10/11.
 *
 * @Info
 */

public class CallAndSMS {

    /**
     * 调用拨号功能 直接拨号
     * @param phone 电话号码
     */
    public static void call2(Context mContext, String phone) {

        LogUtils.v("tel:"+phone);
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mContext.startActivity(intent);
    }

    /**
     * 调用拨号界面
     * @param phone 电话号码
     */
    public static void call(Context mContext, String phone) {
        LogUtils.v("tel:"+phone);
        Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+phone));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mContext.startActivity(intent);
    }

    /**
     * 调用系统发送信息
     *
     * @param number
     * @param content
     */
    public static void sendSms(String number, String content) {
        SmsManager sm = SmsManager.getDefault();

        //拆分长短信
        ArrayList<String> smss = sm.divideMessage(content);
        for (String sms : smss) {
            sm.sendTextMessage(number, null, sms, null, null);
        }
    }
}
