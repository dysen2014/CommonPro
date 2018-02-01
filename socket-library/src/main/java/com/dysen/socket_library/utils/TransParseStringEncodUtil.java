package com.dysen.socket_library.utils;

import com.dysen.socket_library.SocketThread;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hutian on 2018/1/31.
 *
 * 交易转换接口，转换字符编码
 */

public class TransParseStringEncodUtil {
    public static byte trans_997340[];
    public static byte trans_997341[];


    public static List<String> getResponList (byte transData[],String transCode){
        ArrayList<String> responseList=new ArrayList<String>();
        if (transCode.equals(SocketThread.signIn)) {//签名

        } else if (transCode.equals(SocketThread.signOut)) {//签退

        } else if (transCode.equals(SocketThread.tempWithdrawal)) {

        } else if (transCode.equals(SocketThread.forcedWithdrawal)) {

        } else if (transCode.equals(SocketThread.localAuthorization)) {

        } else if (transCode.equals(SocketThread.selectCus)) {

        } else if (transCode.equals(SocketThread.createCus)) {

        } else if (transCode.equals(SocketThread.openCard)) {
        } else if (transCode.equals(SocketThread.selectPwd)) {
//               String cardId=new String(transData)
        } else if (transCode.equals(SocketThread.changePwd)) {

        }
        return  responseList;
    }
    public static String getGbkToUtfString(String gbk){
        String str="";
        char[] chars=gbk.toCharArray();
        byte[] fullByte=new byte[3*chars.length];
        for (int i=0;i<chars.length;i++){
            String binary=Integer.toBinaryString(chars[i]);
            StringBuffer sb=new StringBuffer();
            int len=16-binary.length();
            for(int j=0;j<len;j++){
                sb.append("0");
            }
            sb.append(binary);
            //增加24位3个字节
            sb.insert(0,"1110");
            sb.insert(8,"10");
            sb.insert(16,"10");
            fullByte[i*3]=Integer.valueOf(sb.substring(0,8),2).byteValue();
            fullByte[i*3+1]=Integer.valueOf(sb.substring(8,16),2).byteValue();
            fullByte[i*3+2]=Integer.valueOf(sb.substring(16,24),2).byteValue();

        }
        try {
            str=new String(fullByte, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return  str;
    }
    public static String getEncodeToUtfString(String gbk) throws UnsupportedEncodingException {
        String  iso = new String(gbk.getBytes("UTF-8"), "ISO-8859-1");
        String str="";
        for (byte b:iso.getBytes("ISO-8859-1")){
            //LogUtils.d("hut字节",+b+"");
        }
        str=new String(iso.getBytes("UTF-8"), "ISO-8859-1");
        return  str;
    }
}
