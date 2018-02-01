package com.dysen.commom_library.utils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by dysen on 2017/7/19.
 */

public class ParamUtils {

    public static String pageSize = "20";
    public static String UserId =
//            "001810";
            "E071316";
    public static String userName;
    public static String orgId = "110599079901";
    public static String orgName;
    public static String approvals = "Y";
    public static String returnCredit = "N";

    public static String UserIdApproval = "B129103";
    public static String paramIp =
//            "http://192.168.1.100:9080";
            "http://27.17.37.104:8001";
    public static String paramIpTemp =
                "http://192.168.1.100:9080";
    public static String crmIp =
            "http://27.17.37.104:9995";
//    	"http://192.168.2.111:7001";
    public static String param;
    public static String urlTemp = paramIp + "/ALS7M/JSONService?method=", url, urlTest;
    public static String urlTempCrm = crmIp + "/services/", urlCrm;
    public static JSONObject jsonObject, jsonObject2;
    public static String[] paramKeysVariate, paramKeys = new String[]{"UserId", "BalanceItemNo", "PageSize", "CurPage"};
    public static Object[] paramValues = new Object[]{};
    public static String funcType;

    public static String CERTCODE;//证件号
    public static String staId;//岗位ID
    public static String brCode;// 机构号
    public static String userLogNum;//登录统计
    public static String UserCode;

    public static void setUserId(String id) {
        UserId = id;
    }

    public static String setParamsCrm(String brCode, String chnlCode, String jsonData, String seriNo, String transCode
            , String userCode) {

        return "XDAppService?method=getJSON"
                + "&brCode=" + brCode
                + "&chnlCode=" + chnlCode
                + "&jsonData=" + jsonData
                + "&seriNo=" + seriNo
                + "&transCode=" + transCode
                + "&userCode=" + "UR" + userCode;
    }

    /**
     * menu
     *
     * @param brCode
     * @param chnlCode
     * @param jsonData
     * @param seriNo
     * @param spareOne
     * @param transCode
     * @param userCode
     * @return
     */
    public static String setParamsCrm(String brCode, String chnlCode, String jsonData, String offset, String seriNo, String spareOne, String transCode
            , String userCode) {

        return "XDAppService?method=getJSON"
                + "&brCode=" + brCode
                + "&chnlCode=" + chnlCode
                + "&jsonData=" + jsonData
                + "&offset=" + offset
                + "&seriNo=" + seriNo
                + "&spareOne=" + spareOne
                + "&transCode=" + transCode
                + "&userCode=" + "UR" + userCode;
    }

    /**
     * info
     *
     * @param brCode
     * @param chnlCode
     * @param jsonData
     * @param offset
     * @param seriNo
     * @param spareOne
     * @param spareTwo
     * @param transCode
     * @param userCode
     * @return
     */
    public static String setParamsCrm(String brCode, String chnlCode, String jsonData, String offset, String seriNo,
                                      String spareOne, String spareTwo, String transCode
            , String userCode) {

        return "XDAppService?method=getJSON"
                + "&brCode=" + brCode
                + "&chnlCode=" + chnlCode
                + "&jsonData=" + jsonData
                + "&offset=" + offset
                + "&seriNo=" + seriNo
                + "&spareOne=" + spareOne
                + "&spareTwo=" + spareTwo
                + "&transCode=" + transCode
                + "&userCode=" + "UR" + userCode;
    }

    public static String setParamsCrm2(String userCode, String brCode, String seriNo, String
            chnlCode, String transCode,  String spareTwo, String jsonData) {

        return "T000068?method=getJSON"
                + "&userCode=" + "UR" + userCode
                + "&seriNo=" + seriNo
                + "&brCode=" + brCode
                + "&chnlCode=" + chnlCode
                + "&transCode=" + transCode
                + "&spareTwo=" + spareTwo
                + "&jsonData=" + jsonData;
    }

    /**
     * "UserId":登录机构
     * "BalanceItemNo":对应的金额区间标识
     * "OverDayItemNo":对应的逾期天数区间标识
     * "PageSize":每页数据条数
     * "CurPage": 页码
     */
    public static JSONObject setParams(String mParam, Object[] mObjects, int paramCount) {

        param = mParam;
        url = urlTemp + param;
        urlTest = paramIpTemp+ "/ALS7M/JSONService?method=" + param;
        paramValues = mObjects;
        paramKeysVariate = initParamKey(paramCount);

        return initParam(paramKeysVariate, paramValues);
    }

    public static String[] initParamKey(int paramCount) {

        switch (paramCount) {
            /*warn*/
            case 1:
                paramKeysVariate = new String[]{"UserId"};
                break;
            case 2:
                paramKeysVariate = new String[]{"UserId", "ItemNo"};
                break;
            case 4:
                paramKeysVariate = new String[]{"UserId", "BalanceItemNo", "PageSize", "CurPage"};
                break;
            case 5:
                paramKeysVariate = new String[]{"UserId", "BalanceItemNo", "OverDayItemNo", "PageSize", "CurPage"};
                break;
            /*service*/
			/*1.study*/
			/*2.rate*/

            default:
                break;
        }

        return paramKeysVariate;
    }

    public static JSONObject setParams(String funcName, String mParam, Object[] mObjects, int paramCount) {

        param = mParam;
        url = urlTemp + param;
        urlTest = paramIpTemp+ "/ALS7M/JSONService?method=" + param;
        paramValues = mObjects;
        paramKeysVariate = initParamKey(funcName, paramCount);

        return initParam(paramKeysVariate, paramValues);
    }

    /**
     * 参数拼接
     *
     * @param funcName
     * @param paramCount
     * @return
     */
    public static String[] initParamKey(String funcName, int paramCount) {


        if (funcName.equals("login")) {//CertID:登陆人证件号码
            switch (paramCount) {
                case 1://login
                    paramKeysVariate = new String[]{"CertID"};
                    break;
                case 2://crmApprovalsLogon  {"deviceType":"Android","RequestParams":{UserID:登陆用户, PassWord:登陆密码}}

                    paramKeysVariate = new String[]{"UserID", "PassWord"};
                    break;
            }
        } else if (funcName.equals("crmAlarmBlackQuery")) {//crmAlarmBlackQuery 客户筛查
            //UserId：登陆用户,CustomerName：客户名称，CertTypeName：证件类型, CertId：证件号, CurPage:页码, PageSize:每页条数
            paramKeysVariate = new String[]{"UserID", "CustomerName", "CertTypeName", "CertId", "CurPage", "PageSize"};

        } else if (funcName.equals("crmCustomerRelation")) {//relevanceSearch 关联查询
            paramKeysVariate = new String[]{"UserID", "CustomerID"};

        }else if(funcName.equals("crmCreditOrg")){//网点选择器
            //UserId：登陆用户, OrgId：机构ID（首选界面给当前登录机构OrgId），QueryOrgId(查询机构I), OrgType:选择类型(市：4 县:8 网点：12，默认为
            // 市：4)
            paramKeysVariate = new String[]{"UserID", "OrgId", "QueryOrgId", "OrgType"};

        }else if (funcName.equals("crmBusinessType")) {//crmBusinessType 业务品种
            // UserId:登陆用户,SortNo:业务品种类
            paramKeysVariate = new String[]{"UserID", "SortNo"};

        }else if (funcName.equals("alsAdminOpinion")) {//suggest 建议
            paramKeysVariate = new String[]{"UserID", "Content"};

        } else if (funcName.equals("examine")) {
            //BSTypeFlag:流程树区分条件（贷款：credit || 额度：account || 贷后：putout  *目前只需输入：credit），UserId:登陆用户id，
//			FinishedFlag:未完成或已完成标示（未完成：N || 已完成：Y）SearchKey：查询条件，CurPage:页码, PageSize:每页数据条数
            paramKeysVariate = new String[]{"BSTypeFlag", "UserId", "FinishedFlag", "SearchKey", "CurPage", "PageSize"};
        } else if (funcName.equals("applyInfo")) {
            //SerialNo:贷款流水号，
            paramKeysVariate = new String[]{"SerialNo"};
        } else if (funcName.equals("customerInfo")) {
            //CustomerID:客户编号
            paramKeysVariate = new String[]{"CustomerID"};
        } else if (funcName.equals("guarantyInfo")) {
            //SerialNo:贷款流水号
            paramKeysVariate = new String[]{"SerialNo"};
        } else if (funcName.equals("customerReport")) {
            //SerialNo:贷款流水号，objectType:贷款类型
            paramKeysVariate = new String[]{"SerialNo", "objectType"};
        } else if (funcName.equals("opinionInfo")) {
            // FlowTaskNo:贷款流程树编号,ObjectNo:贷款流水号，ObjectType:贷款类型, PhaseNo:阶段号，FlowNo：流程数据编号，
            paramKeysVariate = new String[]{"FlowTaskNo", "ObjectNo", "ObjectType", "PhaseNo", "FlowNo"};
        } else if (funcName.equals("AlsLoanOpinion")) {
            //SerialNo:贷款流水号,UserID:登陆用户id，DataContent:图片对象（图片二进制，多张图片，传入结构）,PhotoType:图片类型(默认给 “MB”)，
            paramKeysVariate = new String[]{"SerialNo", "UserId", "DataContent", "PhotoType"};
        } else if (funcName.equals("AlsLoanOpinionSee")) {
            //SerialNo:贷款流水号,ObjectType:贷款类型
            paramKeysVariate = new String[]{"SerialNo", "ObjectType"};
        } else if (funcName.equals("backToLast")) {
            //FlowTaskNo:贷款流程树编号,ObjectNo:贷款编号, UserId:登陆用户id
            paramKeysVariate = new String[]{"FlowTaskNo", "ObjectNo", "UserId"};

        } else if (funcName.equals("saveOpinion")) {
            switch (paramCount) {
                case 3:
                    //FTSerialNo:贷款流程树编号,ObjectNo:贷款流水号,IsFlag:"true" 当 IsFlag = "true" 为查看
                    paramKeysVariate = new String[]{"FTSerialNo", "ObjectNo", "IsFlag"};
                    break;
                case 9:
                    //FTSerialNo:贷款流程树编号,ObjectNo:贷款流水号,ObjectType:贷款类型，PhaseOpinion:意见内容（签署意见的内容），Idea:选择的意见（给 "同意"||"不同意" 即可），
                    //FlowNo：流程数据编号，PhaseNo:阶段号，IsFlag:"false", UserId:登陆用户id
                    paramKeysVariate = new String[]{"FTSerialNo", "ObjectNo", "ObjectType", "PhaseOpinion", "Idea", "FlowNo", "PhaseNo", "IsFlag", "UserId"};
                    break;
            }

        } else if (funcName.equals("nextActionOperator")) {
            //nexNewPhaseAction:给空值即可（流程定制需要次参数，目前无需给值）,UserId:登陆用户id, ftSerialNo:贷款流程树编号,phaseOpinion:("同意"||"不同意")获取上个界面Idea
            paramKeysVariate = new String[]{"nexNewPhaseAction", "UserId", "ftSerialNo", "phaseOpinion"};
        } else if (funcName.equals("doMySubmit")) {
            //ftSerialNo:贷款流程树编号,phaseAction:下一步提交人（由下一步（获取提交人）获得）,phaseOpinion:选择的意见（"同意"||"不同意"  由签署意见里获得）,
//			nexNewPhaseAction:给空（该参数为流程定制，目前无需给值）,UserId:登陆用户id
            paramKeysVariate = new String[]{"ftSerialNo", "phaseAction", "phaseOpinion", "nexNewPhaseAction", "UserId"};
        } else if (funcName.equals("crmRiskSignalTX")) {
            //crmRiskSignalTX   SerialNo:贷款流水号,UserId:登陆用户ID
            paramKeysVariate = new String[]{"SerialNo", "UserId"};
        } else if (funcName.equals("warn")) {
            switch (paramCount) {

                case 1://crmDailyReminders  crm日常提醒(贷后)/到期提醒/逾期||crmBeOverdue   crm-逾期金额    "UserId" :登陆人
                    paramKeysVariate = new String[]{"UserId"};
                    break;
                case 2://crmOverdueReminder 逾期提醒-（7、30、60、90、90以上）     "UserId":登陆人,"ItemNo":对应逾期金额区间标识
                    paramKeysVariate = new String[]{"UserId", "ItemNo"};
                    break;
                case 4://crmExpirationReminder  到期提醒详情列表||crmAfterTheLoan   日常贷后详情列表   "UserId":登录人,
                    // "BalanceItemNo":对应的到期提醒节点标识, "PageSize":每页数据条数, "CurPage": 页码
                    paramKeysVariate = new String[]{"UserId", "BalanceItemNo", "PageSize", "CurPage"};
                    break;
                case 5://crmOverdueCustomer crm逾期客户详情列表    "UserId":登录机构,"BalanceItemNo":对应的金额区间标识, "OverDayItemNo":对应的逾期天数区间标识, "PageSize":每页数据条数, "CurPage": 页码
                    paramKeysVariate = new String[]{"UserId", "BalanceItemNo", "OverDayItemNo", "PageSize", "CurPage"};
                    break;
                default:
                    break;
            }
        } else if (funcName.equals("search")) {

            switch (paramCount) {
                case 17://crmBusinessQuery 业务查询
                    // UserID：登陆用户, BusinessSum:合同金额, OverDueDay:逾期天数, CertType:证件类型, OverDueBalance:逾期金额, LcaTimes:逾期期数, ClassifyResult:五级分类
                    // , BusinessTypeBg:业务产品, CustomerName:客户名称,CustomerType:客户类型,MobileTelephone:手机号码, Balance:贷款余额,ActualMaturity:到期日期
                    // ,ActualPutOutDate:发放日期,CertID:证件号码, CurPage:页码, PageSize:每页条数
//                    paramKeysVariate = new String[]{"UserID", "BusinessSum", "OverDueDay", "CertType", "OverDueBalance", "LcaTimes", "ClassifyResult", "BusinessTypeBg"
//                            , "CustomerName", "CustomerType", "MobileTelephone", "Balance", "ActualMaturity", "ActualPutOutDate", "CertID", "CurPage", "PageSize"};
                    paramKeysVariate = new String[]{"UserID", "BusinessSum", "OverDueDay", "CertType", "OverDueBalance", "LcaTimes", "ClassifyResult", "SortNo"
                            , "CustomerName", "CustomerType", "MobileTelephone", "Balance", "ActualMaturity", "ActualPutOutDate", "CertID", "CurPage", "PageSize"};
                    break;
                case 1://crmDuebillInfo 业务查询table-借据详情  SerialNo：借据流水号
                    paramKeysVariate = new String[]{"SerialNo"};
                    break;
                case 2://crmBusinessCountract   业务查询table-合同详情  SerialNo：合同流水号(取业务查询里的ContractNo)，BusinessType：业务品种(取业务查询里的TypeNo)
                    paramKeysVariate = new String[]{"SerialNo", "BusinessType"};
                    break;
                case 3://业务品种type   businessType
                    // UserID:登陆用户id, SortNo:业务品种编号 FlagNo:调用业务品种模块编号(C:利率测算接口调用，Y：其他接口调用，必传项)
                    paramKeysVariate = new String[]{"UserID", "SortNo", "FlagNo"};
                    break;
            }
        } else if (funcName.equals("CustomerSearch")) {
            switch (paramCount) {
                case 9://客户查询    crmCustomerQuery    CustomerType:客户类型（必输项，不能为空）,CustomerName:客户名称, CertTypeName:证件类型,
                    // CertID:证件号码, MobilePhone:手机号码, IrscreditLevel:评级等级, UserID:登陆用户id CurPage:当前页码, PageSize:每页显示的条数
                    paramKeysVariate = new String[]{"CustomerType", "CustomerName", "CertTypeName", "CertID", "MobilePhone", "IrscreditLevel"
                            , "UserID", "CurPage", "PageSize"};
                    break;
                case 1://客户查询table-基本信息 crmCustomerInfo CustomerID：客户id
                    //客户查询tab-贷款证（农户） crmCreditProve  CustomerID：客户id(目前1.100里，只有20070927000004 有数据)
                    paramKeysVariate = new String[]{"CustomerID"};
                    break;
                case 4://客户查询tab-关键人 crmEntKeyman    CustomerID:客户id,CustomerType:客户类型, CurPage:页码, PageSize:每页条数
                    paramKeysVariate = new String[]{"CustomerID", "CustomerType", "CurPage", "PageSize"};
                    break;
                case 3://客户查询tab-未结清授信业务 crmLoanAfter     CustomerID:客户id,CurPage:页码, PageSize:每页条数
                    //客户查询tab-资本构成（企业）  crmEntOwner      CustomerID:证件号码,CurPage:页码, PageSize:每页条数
                    paramKeysVariate = new String[]{"CustomerID", "CurPage", "PageSize"};
                    break;
//                case :
//                paramKeysVariate = new String[]{};
//                break;
            }
        } else if (funcName.equals("study")) {
            switch (paramCount) {
                case 1://crmStudyType
                    paramKeysVariate = new String[]{"UserId"};
                    break;
                case 4://studyList  学习园地 	pageSize:每页数据条数,pageNo:页码, UserId:登陆用户id, SearchKey:查询条件
                    paramKeysVariate = new String[]{"pageSize", "pageNo", "UserId", "SearchKey"};
                    break;
            }
        } else if (funcName.equals("download")) {
            switch (paramCount) {
                case 4://下载	docNo = DocNo, attachmentNo = AttachmentNo, fileName = FileName, FullPath = Address
                    paramKeysVariate = new String[]{"docNo", "attachmentNo", "fileName", "FullPath"};
                    break;
                case 5://studyList
                    paramKeysVariate = new String[]{"pageSize", "pageNo", "UserId", "SearchKey", "DocNo"};

                    break;
            }
        } else if (funcName.equals("rate")) {
            switch (paramCount) {
                case 2://crmBaseInterestRate    当期贷款基础利率	UserId:登陆用户id, BusinessType:业务品种编号
                    paramKeysVariate = new String[]{"UserId", "BusinessType"};
                    break;
                case 6://crmCustomerList    利率测算(选择客户)		UserId:登陆用户id,CustomerName:客户名称，CustomerType:客户类型, CertID:证件号码，CurPage:页码, PageSize:每页条数
                    paramKeysVariate = new String[]{"UserId", "CustomerName", "CustomerType", "CertID", "CurPage", "PageSize"};
                    break;
                case 66://crmInterest    利率测算项
                    // UserId:登陆用户id,OrgId:登陆机构CustomerID:客户id,CustomerType:客户类型,BusinessType:业务品种,InteresType:
                    paramKeysVariate = new String[]{"UserId", "OrgId", "CustomerID", "CustomerType", "BusinessType", "InteresType"};
                    break;
                case 7://crmInterest    利率测算项 和 结果
                    // UserId:登陆用户id,OrgId:登陆机构CustomerID:客户id,CustomerType:客户类型,BusinessType:业务品种,InteresType:
                    // 测算方式（010，获取测算项。020，计算测算项结果 InteresArray:当 InteresType 为020时，传入组合参数：name + @ + ID + @ + key + &
                    paramKeysVariate = new String[]{"UserId", "OrgId", "CustomerID", "CustomerType", "BusinessType", "InteresType", "InteresArray"};
                    break;
            }

        } else if (funcName.equals("")) {

        } else if (funcName.equals("")) {

        } else if (funcName.equals("")) {

        }

        return paramKeysVariate;
    }

    public static JSONObject initParam(String[] paramKey, Object[] paramValue) {

        jsonObject = new JSONObject();
        jsonObject2 = new JSONObject();

        if (paramKey.length != paramValue.length) {
            return null;
        } else {

            try {
                for (int i = 0; i < paramKey.length; i++) {
                    jsonObject2.put(paramKey[i], paramValue[i]);
                }
                jsonObject.put("deviceType", "Android");
                jsonObject.put("RequestParams", jsonObject2);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return jsonObject;
        }
    }
}
