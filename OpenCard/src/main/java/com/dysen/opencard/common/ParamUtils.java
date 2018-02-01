package com.dysen.opencard.common;

import com.dysen.opencard.common.bean.AreaBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dysen on 2018/1/16.
 *
 * @Info  参数 配置工具类
 */

public class ParamUtils {

    public static String serverIp = "192.168.1.90";
    public static String serverPort = "8088";
    public static String tellerId;
    public static String tellerName;
    public static String tellerIdAuthorize;
    public static String orgId;
    public static String terminalId;
    public static String terllerFinger = "67=<?>;;>9;1<69;<<48?=4;550:07990>26<7=292<::=?0:72396203>616<?2654=:<;9?9:1<69;<<48?=4;550:07990>26<7=292<::=>4?254619=9=8907=70<806?007=486;588003679?;=6>?:1>0543731<020379422:4:9?47;?2>6392323=4:2432:<9>:8?2;3>702=8078?0?0<75<=<3<558><::429708:5572762:45>39=4:8;<16:=992:436090812<;?4280?0<=9?>::08><1?25126=?7;06<;31?:?01174>;14;;;21;794;:=1=;9>8267<?385;5=92601273098:=4384>332>>=9?11005451=7:2770?441?7>9;6;;25;29:7;6>2>76114<1;9?2:9<82===04>=9?11005451=7:2770?441?7>9;6;;25;29:7;6>2>76114<1;9?2:9<82===082";
    public static String terllerFinger2 = "67=<;7;9??;3<699=>5:>?594718158;1<34=5<080=8;?>2;53184322<737>>0775?;>:;>;;3=489=>5:>?594718158;1<34=5<080=8;?<2=6<;??27247136>26::8401;3>2996:567<>6=;5:=6:27=21:80>6:8:<11:>880909>910>07?>722?91<::=46;49?2=4844511?8930>07;7;62780;;5<?54;349>;747=>0=37:815=752>1:86653>6>75?81><2380>?>46696?61=7;9<=>???16:9>2=89;8;?;07;387:5349<28::8><=13;8<78?6:9:43::=85647131690>53048035839=<2<?51<6>>0?1:5:0265386?>;5>>8?6:9:43::=85647131690>53048035839=<2<?51<6>>0?1:5:0265386?>;5>>8?6:9:43::=85647131690>53048035839=<2<?9=";

    public static List<AreaBean> countryList = new ArrayList<>();
    public static String[] countrys = new String[]{};//地址 国家
    public static String transStateCode;//交易状态码
    public static String transState;//交易状态
    public static String TRANS_SUCCESS = "交易成功";
    public static String TRANS_PUB = "前置错误交易失败";
    public static String TRANS_OTHER = "贷记卡错误交易失败";
    public static String TRANS_HB = "没有找到客户记录";
}
