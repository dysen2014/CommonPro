package com.dysen.socket_library;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.dysen.socket_library.utils.LogUtils;
import com.dysen.socket_library.utils.ParamUtils;
import com.examples.socketrequestdemo.data.GlobalData;
import com.examples.socketrequestdemo.net.SocketManager;
import com.examples.socketrequestdemo.request.RequestMsg;
import com.examples.socketrequestdemo.request.RequestMsgBody;
import com.examples.socketrequestdemo.request.RequestMsgHead;
import com.examples.socketrequestdemo.response.ResponseMsg;
import com.examples.socketrequestdemo.response.ResponseMsgBody;

import org.dom4j.DocumentException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TranUtil {

    private static String sn;
    private static String preUUID = "";
    private static List<String> responsList;
    private static List<String> responsHeader;
    private static String myFileName;
    private static RequestMsgHead head;
    private static String respMsg;
//    private static byte[] respMsgByte;

    public static void setSn(String no) {
        sn = no;
    }

    public static RequestMsgHead packMsgHead(String tranCode) {

//    LogUtils.TAG = TranUtil.class.getName();
        LogUtils.e("============================generateUUID()=============" + generateUUID());

        RequestMsgHead rmh = new RequestMsgHead();

        rmh.RRSpecificNo.setValue(ParamUtils.sn);
        rmh.HB_Branch_Number.setValue(ParamUtils.orgId);//机构号
        rmh.HB_Workstation_Number.setValue(ParamUtils.terminalId);//终端号
        rmh.HB_Teller_Number.setValue(ParamUtils.tellerId);//柜员号
        rmh.BkPinNode.setValue("");//加密节点号
        rmh.GDTA_SVCNAME.setValue(tranCode);//交易号
        rmh.HB_FLAG2.setValue("0");//内部标志2，默认为0，AuthErrorFlag为SUP且授权成功赋值为2
        rmh.HB_FLAG4.setValue("5");//渠道 默认为5
        rmh.HB_Supervisor_ID.setValue("");//核心授权柜员号码 AuthErrorFlag为SUP时且授权成功时赋授权柜员号
        rmh.HB_UUID_NO.setValue(generateUUID());
        preUUID = rmh.HB_UUID_NO.getValue();
        rmh.AuthSupervisor_ID.setValue("");//授权柜员号
        rmh.AuthSpeck.setValue("");//为空
        rmh.AuthAmtFlag.setValue("0");//现转标志 0-现金，1-转账
        rmh.AuthSpecSign.setValue("0");//特殊授权标志 默认为0-需要检查授权  1-不检查授权
        rmh.AuthFlag.setValue("0");//授权成功标志 授权成功时上送，默认-0，本地授权成功-1 集中授权成功-2
        rmh.AuthOthSeqNo.setValue("");//远程授权流水号 集中授权平台生成的授权流水号（第一次交易时为空，授权成功需上送流水号）
        rmh.AuthSeqNo.setValue("");//本地授权流水号 前置返回授权流水号（第一次交易时为空，授权成功需上送流水号）
        rmh.AuthAmt.setValue("0.00");//交易金额 默认为0.00
        //自助柜面终端标志位 默认值-centerm
        return rmh;
    }

    public static RequestMsgHead packMsgHead(String tranCode, String authSpecSign, String
            authSeqNo) {

//    LogUtils.TAG = TranUtil.class.getName();
        LogUtils.e("============================generateUUID()=============" + generateUUID());

        head = new RequestMsgHead();

        head.RRSpecificNo.setValue(ParamUtils.sn);
        head.HB_Branch_Number.setValue(ParamUtils.orgId);//机构号
        head.HB_Workstation_Number.setValue(ParamUtils.terminalId);//终端号
        head.HB_Teller_Number.setValue(ParamUtils.tellerId);//柜员号
        head.BkPinNode.setValue("");//加密节点号
        head.GDTA_SVCNAME.setValue(tranCode);//交易号
        head.HB_FLAG2.setValue("0");//内部标志2，默认为0，AuthErrorFlag为SUP且授权成功赋值为2
        head.HB_FLAG4.setValue("5");//渠道 默认为5
        head.HB_Supervisor_ID.setValue("");//核心授权柜员号码 AuthErrorFlag为SUP时且授权成功时赋授权柜员号
        head.HB_UUID_NO.setValue(generateUUID());
        preUUID = head.HB_UUID_NO.getValue();
        head.AuthSupervisor_ID.setValue("");//授权柜员号
        head.AuthSpeck.setValue("");//为空
        head.AuthAmtFlag.setValue("0");//现转标志 0-现金，1-转账
        head.AuthSpecSign.setValue(authSpecSign);//特殊授权标志 默认为0-需要检查授权  1-不检查授权
        head.AuthFlag.setValue("0");//授权成功标志 授权成功时上送，默认-0，本地授权成功-1 集中授权成功-2
        head.AuthOthSeqNo.setValue("");//远程授权流水号 集中授权平台生成的授权流水号（第一次交易时为空，授权成功需上送流水号）
        head.AuthSeqNo.setValue(authSeqNo);//本地授权流水号 前置返回授权流水号（第一次交易时为空，授权成功需上送流水号）
        head.AuthAmt.setValue("0.00");//交易金额 默认为0.00
        //自助柜面终端标志位 默认值-centerm

        return head;
    }

    private static String generateUUID() {
        long timemillis = System.currentTimeMillis();
        Date currentDate = new Date(timemillis);
        SimpleDateFormat date = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat serialNo = new SimpleDateFormat("MMddhhmmss");
        String uuid = "019999000099" + date.format(currentDate) + "00" + serialNo.format(currentDate);

        if (preUUID.equals(uuid)) {
            try {
                Thread.sleep(500L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return generateUUID();
        }
        return uuid;
    }

    /**
     * 发送，接收报文
     *
     * @param tranCode
     * @param head
     * @param body
     * @param context
     * @param handler
     * @return
     */
    public static ResponseMsg startTran(String tranCode, RequestMsgHead head, RequestMsgBody body, Context context, Handler handler) {
        RequestMsg rm = new RequestMsg(head, body);
        SocketManager sm = new SocketManager();
        for (int i = 0; i < body.getBodyData().size(); i++) {

            LogUtils.d(body.getBodyData().get(i).getName() + "=========" + body.getBodyData().get(i)
                    .getValue());
        }
        LogUtils.i(tranCode + "交易提交开始");
        byte[] retMsg;
        try {
            int timeout = 60;
//      retMsg = sm.transmitMsg("192.168.1.90",
//              Integer.valueOf("8088").intValue(), timeout * 10000, rm.getMsg());
            retMsg = sm.transmitMsg(ParamUtils.serverIp,
                    Integer.valueOf(ParamUtils.serverPort).intValue(), timeout * 10000, rm.getMsg());
            LogUtils.i("发送报文=" + new String(rm.getMsg()));
        } catch (UnknownHostException e) {
            e.printStackTrace();
            LogUtils.e("SocketManager 传输报文异常1");
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            LogUtils.e("SocketManager 传输报文异常2");
            return null;
        }
        if (retMsg == null) {
            LogUtils.e("SocketManager 返回报文为空");
            return null;
        }
        LogUtils.e("生成返回报文体");
        ResponseMsgBody rrmb;
        try {
            rrmb = new ResponseMsgBody(tranCode, context);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            LogUtils.e("ResponseMsgBody 解析报文异常1");
            return null;
        } catch (DocumentException e) {
            e.printStackTrace();
            LogUtils.e("ResponseMsgBody 解析报文异常2");
            return null;
        }
        //ResponseMsgBody rrmb;
//        LogUtils.d("---------------------"+byte2Hex(retMsg)); ;
        try {
            LogUtils.e(new String(retMsg, "GBK").length()+"生成返回报文=" + new String(retMsg, "gbk"));
            LogUtils.e(new String(retMsg, "GB18030").length()+"=======" + new String(retMsg, "ISO-8859-1").length());
//            Message msg = new Message();
//            msg.obj = new String(retMsg, "gbk");
//            handler.sendMessage(msg);
            ParamUtils.respMsgByte = retMsg;
            respMsg = new String(retMsg, "GB18030");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        ResponseMsg result;
        try {
            result = new ResponseMsg(retMsg, rrmb);
            LogUtils.e("应答报文不为空");
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e("ResponseMsg 解析报文异常:" + e.getMessage());
            return null;
        }
        //ResponseMsg result;
        return result;
    }

    protected static String byte2Hex(byte[] bytes) {
        StringBuilder buf = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) { // 使用String的format方法进行转换
            buf.append(String.format("%02x", new Integer(b & 0xff)));
        }
        return buf.toString();
    }

    public static void getNotice(Context context, String transCode, List<String> list, Handler handler) {
        responsList = new ArrayList<>();
        responsHeader = new ArrayList<>();
        if (transCode.equals(SocketThread.signIn)) {
            ParamUtils.tellerId = list.get(0);
            ParamUtils.orgId = list.get(2);
            ParamUtils.terminalId = list.get(3);
        }
        RequestMsgHead head = packMsgHead(transCode, ParamUtils.authSpecSign, ParamUtils.authSeqNo);
        RequestMsgBody body = null;
        try {
            try {
                body = new RequestMsgBody(transCode, context);
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            LogUtils.i(transCode + "交易----lis.size=" + list.size() + "\t\tlist=" + list);
            if (transCode.equals(SocketThread.signIn)) {
                if (list != null && list.size() == 7) {
                    body.getMsgField("HB_tellerNo1").setValue(list.get(0));
                    body.getMsgField("HB_Teller_PW").setValue(list.get(1));
                    body.getMsgField("HB_branch1").setValue(list.get(2));
                    body.getMsgField("HB_terminalId1").setValue(list.get(3));
                    body.getMsgField("FPI_tx_model").setValue(list.get(4));
                    body.getMsgField("BkCertType").setValue(list.get(5));
                    body.getMsgField("BkAuthTeller").setValue(list.get(6));
                }
            } else if (transCode.equals(SocketThread.signOut)) {
                if (list != null && list.size() > 0) {
                    body.getMsgField("").setValue(list.get(0));
                }
            } else if (transCode.equals(SocketThread.tempWithdrawal)) {
                if (list != null && list.size() == 7) {
                    body.getMsgField("HB_tellerNo1").setValue(list.get(0));
                    body.getMsgField("HB_Teller_PW").setValue(list.get(1));
                    body.getMsgField("HB_branch1").setValue(list.get(2));
                    body.getMsgField("HB_terminalId1").setValue(list.get(3));
                    body.getMsgField("FPI_tx_model").setValue(list.get(4));
                    body.getMsgField("BkCertType").setValue(list.get(5));
                    body.getMsgField("BkAuthTeller").setValue(list.get(6));
                }
            } else if (transCode.equals(SocketThread.forcedWithdrawal)) {
                if (list != null && list.size() == 1) {
                    body.getMsgField("HB_DefInteger2").setValue(list.get(0));
                }
            } else if (transCode.equals(SocketThread.localAuthorization)) {
                if (list != null && list.size() == 5) {
                    body.getMsgField("AuthSupervisor_ID").setValue(list.get(0));
                    body.getMsgField("HB_Authorization_Branch").setValue(list.get(1));
                    body.getMsgField("HB_Authorization_Option").setValue(list.get(2));
                    body.getMsgField("BkFlag1").setValue(list.get(3));
                    body.getMsgField("FPI_tx_model").setValue(list.get(4));
                }
            } else if (transCode.equals(SocketThread.selectCus)) {
                if (list != null && list.size() == 3) {
                    body.getMsgField("HB_CERT_1").setValue(list.get(0));
                    body.getMsgField("HB_CertType1").setValue(list.get(1));
                    body.getMsgField("HB_GirdName").setValue(list.get(2));
                }
            } else if (transCode.equals(SocketThread.createCus)) {
                if (list != null && list.size() == 18) {
                    body.getMsgField("HB_CustType").setValue(list.get(0));
                    body.getMsgField("HB_CustSubtype").setValue(list.get(1));
                    body.getMsgField("HB_IDType").setValue(list.get(2));
                    body.getMsgField("HB_IDNum").setValue(list.get(3));
                    body.getMsgField("HB_LanguageCode").setValue(list.get(4));
                    body.getMsgField("HB_IDIssueDate").setValue(list.get(5));
                    body.getMsgField("HB_IDExpiryDate").setValue(list.get(6));
                    body.getMsgField("HB_LastName").setValue(list.get(7));
                    body.getMsgField("HB_Mobile").setValue(list.get(8));
                    body.getMsgField("HB_HomeAddr1").setValue(list.get(9));
                    body.getMsgField("HB_HomeAddr2").setValue(list.get(10));
                    body.getMsgField("HB_HomeAddr3").setValue(list.get(11));
                    body.getMsgField("HB_HomeAddr4").setValue(list.get(12));
                    body.getMsgField("HB_PostCode1").setValue(list.get(13));
                    body.getMsgField("HB_Nationality").setValue(list.get(14));
                    body.getMsgField("HB_SexCode").setValue(list.get(15));
                    body.getMsgField("HB_IsResident").setValue(list.get(16));
                    body.getMsgField("HB_OccupationCode").setValue(list.get(17));
                }
            } else if (transCode.equals(SocketThread.openCard)) {
                if (list != null && list.size() == 6) {
                    body.getMsgField("HB_CustNumber").setValue(list.get(0));
                    body.getMsgField("HB_Pan").setValue(list.get(1));
                    body.getMsgField("HB_Pwd").setValue(list.get(2));
                    body.getMsgField("HB_DefInteger1").setValue(list.get(3));
                    body.getMsgField("HB_DefInteger2").setValue(list.get(4));
                    body.getMsgField("HB_Flag").setValue(list.get(5));
                }
            } else if (transCode.equals(SocketThread.selectPwd)) {
                if (list != null && list.size() == 2) {
                    body.getMsgField("HB_Pan").setValue(list.get(0));
                    body.getMsgField("HB_Track2").setValue(list.get(1));
                }
            } else if (transCode.equals(SocketThread.changePwd)) {
                if (list != null && list.size() == 17) {
                    body.getMsgField("HB_CardNumber").setValue(list.get(0));
                    body.getMsgField("HB_CardDesc").setValue(list.get(1));
                    body.getMsgField("HB_customerNo").setValue(list.get(2));
                    body.getMsgField("HB_Name").setValue(list.get(3));
                    body.getMsgField("HB_Address1").setValue(list.get(4));
                    body.getMsgField("HB_Address2").setValue(list.get(5));
                    body.getMsgField("HB_Address3").setValue(list.get(6));
                    body.getMsgField("HB_Address4").setValue(list.get(7));
                    body.getMsgField("HB_postCode").setValue(list.get(8));
                    body.getMsgField("HB_cardName1").setValue(list.get(9));
                    body.getMsgField("HB_cardName2").setValue(list.get(10));
                    body.getMsgField("HB_cardExpiry").setValue(list.get(11));
                    body.getMsgField("HB_Bin").setValue(list.get(12));
                    body.getMsgField("HB_issuingInd").setValue(list.get(13));
                    body.getMsgField("HB_type").setValue(list.get(14));
                    body.getMsgField("HB_oldPIN").setValue(list.get(15));
                    body.getMsgField("HB_firstPIN").setValue(list.get(16));
                }
            } else if (transCode.equals(SocketThread.cardType)) {
                if (list != null && list.size() == 1) {
                    body.getMsgField("HB_Pan").setValue(list.get(0));
                }
            }else if (transCode.equals(SocketThread.cardPwdSelect)) {
                if (list != null && list.size() == 2) {
                    body.getMsgField("HB_Pan").setValue(list.get(0));
                    body.getMsgField("HB_Track2").setValue(list.get(1));
                }
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        ResponseMsg result = null;
        try {
            result = startTran(transCode, head, body, context, handler);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (result == null) {
            handler.sendEmptyMessage(-2);//"交易超时"
        } else if (result.ERR_RET.getValue().contains(GlobalData.RET_SUCCESS)) {
            LogUtils.e(transCode + "交易执行成功");
            handler.sendEmptyMessage(100);

            responsHeader.add(result.responseMsgHead.AuthSeqNo.getFieldValue());
            responsHeader.add(result.responseMsgHead.AuthErrorType.getFieldValue());
            responsHeader.add(result.responseMsgHead.AuthErrorFlag.getFieldValue());
            responsHeader.add(result.responseMsgHead.BkSeqNo.getFieldValue());
            responsHeader.add(result.responseMsgHead.AuthCapability.getFieldValue());
            responsHeader.add(result.responseMsgHead.BkNum1.getFieldValue());
            responsHeader.add(result.responseMsgHead.AuthErrorCode.getFieldValue());
            responsHeader.add(result.responseMsgHead.AuthErrorMessage.getFieldValue());
            responsHeader.add(result.responseMsgHead.AuthSealMessage.getFieldValue());

            if (transCode.equals(SocketThread.signIn)) {
                responsList.add(result.responseMsgBody.getMsgField("HB_tellerNo").getValue());
            } else if (transCode.equals(SocketThread.signOut)) {
                responsList.add(result.responseMsgBody.getMsgField("").getValue());
            } else if (transCode.equals(SocketThread.tempWithdrawal)) {
//                responsList.add(result.responseMsgBody.getMsgField("HB_tellerNo").getValue());
                responsList.add("签退成功");
            } else if (transCode.equals(SocketThread.forcedWithdrawal)) {
//                responsList.add(result.responseMsgBody.getMsgField("HB_DefInteger2").getValue());
                responsList.add("签退成功");
            } else if (transCode.equals(SocketThread.localAuthorization)) {
                responsList.add(result.responseMsgBody.getMsgField("").getValue());
            } else if (transCode.equals(SocketThread.selectCus)) {
                responsList.add(result.responseMsgBody.getMsgField("HB_cust_no").getValue());
                responsList.add(result.responseMsgBody.getMsgField("HB_GirdName").getValue());
            } else if (transCode.equals(SocketThread.createCus)) {
                responsList.add(result.responseMsgBody.getMsgField("HB_CustNum").getValue());
            } else if (transCode.equals(SocketThread.openCard)) {
                responsList.add(result.responseMsgBody.getMsgField("HB_accNo").getValue());
            } else if (transCode.equals(SocketThread.selectPwd)) {
                responsList.add(result.responseMsgBody.getMsgField("HB_CardNumber").getValue());
                responsList.add(result.responseMsgBody.getMsgField("HB_CardDesc").getValue());
                responsList.add(result.responseMsgBody.getMsgField("HB_customerNo").getValue());
                responsList.add(result.responseMsgBody.getMsgField("HB_Name").getValue());
                responsList.add(result.responseMsgBody.getMsgField("HB_Address1").getValue());
                responsList.add(result.responseMsgBody.getMsgField("HB_Address2").getValue());
                responsList.add(result.responseMsgBody.getMsgField("HB_Address3").getValue());
                responsList.add(result.responseMsgBody.getMsgField("HB_Address4").getValue());
                responsList.add(result.responseMsgBody.getMsgField("HB_postCode").getValue());
                responsList.add(result.responseMsgBody.getMsgField("HB_cardName1").getValue());
                responsList.add(result.responseMsgBody.getMsgField("HB_cardName2").getValue());
                responsList.add(result.responseMsgBody.getMsgField("HB_cardExpiryDate").getValue());
                responsList.add(result.responseMsgBody.getMsgField("HB_Bin").getValue());
                responsList.add(result.responseMsgBody.getMsgField("HB_issuingIndex").getValue());
                responsList.add(result.responseMsgBody.getMsgField("HB_type").getValue());
            } else if (transCode.equals(SocketThread.changePwd)) {
                responsList.add(result.responseMsgBody.getMsgField("").getValue());
            } else if (transCode.equals(SocketThread.cardType)) {
                responsList.add(result.responseMsgBody.getMsgField("HB_cardProduct").getValue());
                responsList.add(result.responseMsgBody.getMsgField("HB_CustName").getValue());
                responsList.add(result.responseMsgBody.getMsgField("HB_linkAccount1").getValue());
                responsList.add(result.responseMsgBody.getMsgField("HB_IDNumber").getValue());
            } else if (transCode.equals(SocketThread.cardPwdSelect)) {
                responsList.add(result.responseMsgBody.getMsgField("HB_CardNumber").getValue());
                responsList.add(result.responseMsgBody.getMsgField("HB_CardDesc").getValue());
                responsList.add(result.responseMsgBody.getMsgField("HB_customerNo").getValue());
                responsList.add(result.responseMsgBody.getMsgField("HB_Name").getValue());
                responsList.add(result.responseMsgBody.getMsgField("HB_Address1").getValue());
                responsList.add(result.responseMsgBody.getMsgField("HB_Address2").getValue());
                responsList.add(result.responseMsgBody.getMsgField("HB_Address3").getValue());
                responsList.add(result.responseMsgBody.getMsgField("HB_Address4").getValue());
                responsList.add(result.responseMsgBody.getMsgField("HB_postCode").getValue());
                responsList.add(result.responseMsgBody.getMsgField("HB_cardName1").getValue());
                responsList.add(result.responseMsgBody.getMsgField("HB_cardName2").getValue());
                responsList.add(result.responseMsgBody.getMsgField("HB_cardExpiryDate").getValue());
                responsList.add(result.responseMsgBody.getMsgField("HB_Bin").getValue());
                responsList.add(result.responseMsgBody.getMsgField("HB_issuingIndex").getValue());
                responsList.add(result.responseMsgBody.getMsgField("HB_type").getValue());
            }
//            myFileName = result.responseMsgBody.getMsgField("HB_tellerNo").getValue();
//
//            try {
                try {
                    LogUtils.i(new String(responsList.get(0).getBytes("gbk"), "gbk")
                            + "交易返回解析的数据=" + responsList);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                ParamUtils.responsHeader = responsHeader;
                Message message = new Message();
//                message.obj = respMsg;
                    message.obj = responsList;
                handler.sendMessage(message);
            } else {
                LogUtils.e(transCode + "交易执行失败");
                handler.sendEmptyMessage(-3);//result.ERR_MSG.getValue();
            }
        }

  /*public static void main(String[] args){
      TranUtil util=new TranUtil();
	  util.getNotice("");
  }*/
}