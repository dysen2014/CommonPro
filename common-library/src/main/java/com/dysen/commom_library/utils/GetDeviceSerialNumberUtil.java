package com.dysen.commom_library.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import java.net.NetworkInterface;

public class GetDeviceSerialNumberUtil {

    /**
     * 手机唯一的标识
     *
     * @param context
     * @return
     */
    public static String getIMEI(Context context) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

            return "";
        }
        return ((TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
    }

    /**
     * 设备ANDROID_ID
     *
     * @param context
     * @return
     */
    public static String getAndroidId(Context context) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return "";
        }
        // androidID
        String androidID = Settings.Secure.getString(context.getContentResolver(),//eb9b3a8e8908dc7d
                Settings.Secure.ANDROID_ID);
        // 返回wifi MAC地址
        WifiManager wifi = (WifiManager) context.getSystemService(context.WIFI_SERVICE);
        if (!wifi.isWifiEnabled()) {
            wifi.setWifiEnabled(true);
        }
        String wifiAddress = wifi.getConnectionInfo().getMacAddress();//会得到虚拟的mac地址0200000000
        // 如果wifi地址为空或者为020000000000，用网络底层获取wifi地址
        if (TextUtils.isEmpty(wifiAddress) || wifiAddress.equals("02:00:00:00:00:00")) {//6c:5f:1c:db:4a:3e
            try {
                NetworkInterface networkInterface = NetworkInterface.getByName("wlan0");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
                    wifiAddress = convertMacAddressBytesToString(networkInterface.getHardwareAddress());
                }
//                Toast.makeText(context, "==您的移动设备串号为："+wifiAddress, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
//            Toast.makeText(context, "您的移动设备串号为："+wifiAddress, Toast.LENGTH_SHORT).show();  正式版本得去掉吐司
        }

        if (!TextUtils.isEmpty(wifiAddress)) {
            androidID = wifiAddress.replaceAll(":", "");
        }
//		}

//        return "111111";
		return androidID;//生产环境       联想pad的序列号6c5f1cdb4a3e
//        return "F9FQLBWDFCM8";
//        return "F9FQW577FCM8";//admin 管理员帐户()
//		return "dcee06a57858";//huawei pad
//		return "DMPP5K44FK11";//A001
//        return "123456789";//A002  生产环境测试号
//		 return "DLXJT06FF18P";//  001810测试环境
//		return "868038020719861";//Q890006 6271528
//		 return "b6558f0beb790bf7"; //--67010003
//		return "50F0D3A9B6F1";//65300002   123456
//		return "222222";//001811
//		return "www";//E010901
    }

    private static String convertMacAddressBytesToString(byte[] addres) {
        StringBuffer sb = new StringBuffer();
        if (addres != null && addres.length > 1) {
            sb.append(parseByte(addres[0])).append(":").append(
                    parseByte(addres[1])).append(":").append(
                    parseByte(addres[2])).append(":").append(
                    parseByte(addres[3])).append(":").append(
                    parseByte(addres[4])).append(":").append(
                    parseByte(addres[5]));
            return sb.toString();
        }
        return "00:00:00:00:00";
    }

    //格式化二进制
    private static String parseByte(byte b) {
        int intValue = 0;
        if (b >= 0) {
            intValue = b;
        } else {
            intValue = 256 + b;
        }
        return Integer.toHexString(intValue);
    }

    /**
     * 获取运营商sim卡imsi号
     *
     * @param context
     * @return
     */
    public static String getSimSerialNumber(Context context) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return "";
        }
        return ((TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE))
                .getSimSerialNumber();
    }

    /**
     * 设备序列号
     *
     * @return
     */
    public static String getSerialNumber() {

        return Build.SERIAL;
    }

    public static void displayDevice(Context context) {
        Log.i("displayDevice", "isTestDevice: "
                + " \nIMEI:" + getIMEI(context)
                + " \nANDROID ID:" + getAndroidId(context)
                + " \nSerialNumber:" + getSerialNumber()
                + " \nSimSerialNumber:" + getSimSerialNumber(context));

        LogUtils.v(" IMEI:" + getIMEI(context)
                + " \nANDROID ID:" + getAndroidId(context)
                + " \nSerialNumber:" + getSerialNumber()
                + " \nSimSerialNumber:" + getSimSerialNumber(context));
    }
}
