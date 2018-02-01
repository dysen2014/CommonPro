package com.dysen.socket_library;

import android.content.Context;
import android.os.Handler;

import com.dysen.socket_library.utils.ParamUtils;

import java.util.List;

/**
 * Created by dysen on 1/19/2018.
 */

public class SocketThread extends Thread {

    public static String signIn = "991052";//991052柜员签到交易
    public static String signOut = "991053";//991053柜员签退交易
    public static String tempWithdrawal = "991055";//991055柜员临时签退交易
    public static String forcedWithdrawal = "991056";//991056柜员强制签退交易
    public static String localAuthorization = "991034";//991034本地授权交易
    public static String selectCus = "997350";//997350根据证件号及户名查询客户信息
    public static String createCus = "992001";//992001个人客户信息基本创建
    public static String openCard = "997204";//997204客户开卡
    public static String selectPwd = "997340";//997340卡密码查询
    public static String changePwd = "997341";//997341卡密码修改
    public static String cardType =  "997440";//997440卡类型查询
    public static String cardPwdSelect =  "997340";//997340卡密码查询

    /**
     * 交易接口
     * @param context
     * @param transCodeList
     */
    public static void transCode(final Context context, final String transCode, final
    List<String> transCodeList, final Handler
            handler) {
        new Thread(){
            @Override
            public void run() {
                super.run();
//
                TranUtil.getNotice(context, transCode, transCodeList, handler);
            }
        }.start();
    }

    public static void setSn(String androidId) {
        TranUtil.setSn(androidId);
        ParamUtils.sn = androidId;
    }

    public static void setPackMsgHead(String tranCode, String authSpecSign, String authSeqNo){
//        TranUtil.packMsgHead(tranCode, authFlag, authSeqNo);
        ParamUtils.authSpecSign = authSpecSign;
        ParamUtils.authSeqNo = authSeqNo;
    }
}
