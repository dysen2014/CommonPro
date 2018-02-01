package com.dysen.opencard;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dysen.commom_library.base.ParentActivity;
import com.dysen.commom_library.utils.CodeFormatUtils;
import com.dysen.commom_library.utils.FileUtils;
import com.dysen.commom_library.utils.GetDeviceSerialNumberUtil;
import com.dysen.commom_library.utils.LogUtils;
import com.dysen.commom_library.views.UberProgressView;
import com.dysen.commom_library.views.ViewUtils;
import com.dysen.opencard.backClip.IDCheck;
import com.dysen.opencard.common.ParamUtils;
import com.dysen.opencard.common.StrUtils;
import com.dysen.opencard.common.bean.AreaBean;
import com.dysen.opencard.ui.MainActivity;
import com.dysen.socket_library.SocketThread;

import org.kymjs.kjframe.KJDB;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends ParentActivity {


    @BindView(R.id.txt_back)
    TextView txtBack;
    @BindView(R.id.lay_back)
    LinearLayout layBack;
    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.txt_)
    TextView txt;
    @BindView(R.id.iv_org_img)
    ImageView ivOrgImg;
    @BindView(R.id.edt_teller_id)
    EditText edtTellerId;
    @BindView(R.id.edt_org_id)
    EditText edtOrgId;
    @BindView(R.id.edt_terminal_id)
    EditText edtTerminalId;
    @BindView(R.id.edt_teller_finger)
    EditText edtTellerFinger;
    @BindView(R.id.btn_read_finger)
    Button btnReadFinger;
    @BindView(R.id.btn_teller_login)
    Button btnTellerLogin;
    @BindView(R.id.ll_cus_id)
    LinearLayout llCusId;
    @BindView(R.id.uber_pgsview)
    UberProgressView uberPgsview;
    @BindView(R.id.tv_pgs_txt)
    TextView tvPgsTxt;
    private int mIndex;
    private String finger;
    private List<String> transCodeList;
    private List<String> responsList;
    private List<String> transCodeListHead;
    private KJDB kjdb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {

        kjdb = KJDB.create(aty, "country");
        txtTitle.setText(getString(R.string.app_name_cn));
        layBack.setVisibility(View.INVISIBLE);
        uberPgsview.setVisibility(View.INVISIBLE);


        edtTellerId.setText("9999933");
        edtOrgId.setText("57220");
        edtTerminalId.setText("021");
        aty = this;
    }

    @OnClick({R.id.btn_read_finger, R.id.btn_teller_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_read_finger://读指纹
                readInfo(IDCheck.READ_FINGER);
                break;
            case R.id.btn_teller_login://登录
                login();
//                gotoNextActivity(MainActivity.class);
                break;
        }
    }

    public String s = "";
    public Object obj = null;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            uberPgsview.setVisibility(View.INVISIBLE);
            if (msg.what == -1) {
                s = "组装报文异常";
            }
            if (msg.what == -2) {
                s = "交易超时";
            }
            if (msg.what == -3) {
                s = "交易执行失败";
            }
            if (msg.what == 100) {
                s = "交易执行成功";
            }
            if (msg.obj != null) {
                obj = msg.obj;
                parseData(obj);
            }
        }
    };

    private void parseData(Object obj) {
//        responsList = (List<String>) obj;
//
//        LogUtils.d(responsList.size() + "交易返回数据：" + responsList != null ? responsList.get(0) : "no " +
//                "data!!!");
//        toast(responsList.size() + "交易返回数据：" + responsList != null ? responsList.get(0) : "no " +
//                "data!!!");
        byte[] bytes = com.dysen.socket_library.utils.ParamUtils.respMsgByte;
        try {
            ParamUtils.tellerName = CodeFormatUtils.byte2StrIntercept(bytes, bytes.length - 20, 20);
            ParamUtils.transStateCode = StrUtils.getTransStateeCode(bytes);
//                    new String(bytes, bytes.length-20, 20, "gbk").trim();
            LogUtils.i(new String(bytes, "gbk") + "交易返回数据：" + StrUtils.getState(bytes) + "---" + ParamUtils.transState + "------------" + ParamUtils.tellerName);
            toast(StrUtils.getState(bytes));
            if (StrUtils.getState(bytes).equals(ParamUtils.TRANS_SUCCESS)) {

                gotoNextActivity(MainActivity.class);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private List<String> initList() {

        transCodeList = new ArrayList<>();

//            HB_tellerNo1 柜员号
//            HB_Teller_PW 密码
//            HB_branch1 机构号
//            HB_terminalId1 终端号
//            FPI_tx_model 指纹特征值
//            BkCertType 校验方式
//            BkAuthTeller 授权柜员
        transCodeList.add(ParamUtils.tellerId);
        transCodeList.add("");
        transCodeList.add(ParamUtils.orgId);
        transCodeList.add(ParamUtils.terminalId);
//        transCodeList.add(ParamUtils.terllerFinger);
        transCodeList.add(ParamUtils.terllerFinger);
        transCodeList.add("0");//0-指纹， 1-密码 暂时只支持指纹登录
        transCodeList.add("");
        return transCodeList;
    }

    private void login() {


        ParamUtils.tellerId = ViewUtils.getText(edtTellerId);
        ParamUtils.orgId = ViewUtils.getText(edtOrgId);
        ParamUtils.terminalId = ViewUtils.getText(edtTerminalId);
//        ParamUtils.terllerFinger = ViewUtils.getText(edtTellerFinger);

        if (!ParamUtils.tellerId.isEmpty() && !ParamUtils.orgId.isEmpty() &&
                !ParamUtils.terminalId.isEmpty()) {
            uberPgsview.setVisibility(View.VISIBLE);
            initList();
            ParamUtils.countryList = kjdb.findAll(AreaBean.class);
            if (ParamUtils.countrys.length == 0) {

                ParamUtils.countrys = FileUtils.getArryString(FileUtils.readFromAssets(aty, "country.txt"), 2);

            }
            SocketThread.setSn(GetDeviceSerialNumberUtil.getAndroidId(aty));
            SocketThread.transCode(LoginActivity.this, SocketThread.signIn,
                    transCodeList,
                    handler);
//            SocketThread.smsCheck(LoginActivity.this);
        } else {
            toast("输入不能为空！！！");
        }
    }

    /**
     * 初始化 地址  显示国家
     */
    private void initCountry() {
        String[] str = FileUtils.getArryString(FileUtils.readFromAssets(aty, "country.txt"), 2);

        for (int i = 0; i < str.length; i++) {
            kjdb.save(new AreaBean("" + i, str[i]));
        }
        ParamUtils.countryList = kjdb.findAll(AreaBean.class);
        ParamUtils.countrys = ParamUtils.countryList.toArray(new String[ParamUtils.countryList.size()]);
    }

    /**
     * 读数据
     */
    private void readInfo(int index) {
        if (openBle()) {
            this.mIndex = index;
            Intent intent = new Intent(this, IDCheck.class);
            LogUtils.d(mIndex + "-----------------------mIndex----------------" + index);
            intent.putExtra(IDCheck.FUNC_NAME, index);
            startActivityForResult(intent, index);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == mIndex)
            switch (resultCode) {
                case IDCheck.READ_FINGER://读指纹
                    finger = intent.getStringExtra("finger");
                    ParamUtils.terllerFinger = finger;
//                    edtTellerFinger.setText(finger);
                    edtTellerFinger.setText("指纹录取成功！！！");
//                    edtTellerFinger.setText("");
//                    edtTellerFinger.setHint("");
//                    edtTellerFinger.setBackground(aty.getResources().getDrawable(R.mipmap
//                            .finger_tick2));
//                    toast("指纹：" + finger);
                    LogUtils.d("指纹采取：" + finger);
                    break;
                default:
                    break;
            }
    }
}
