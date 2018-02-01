package com.dysen.opencard.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dysen.commom_library.base.ParentActivity;
import com.dysen.commom_library.views.ViewUtils;
import com.dysen.opencard.R;
import com.dysen.opencard.backClip.IDCheck;
import com.dysen.opencard.common.ParamUtils;
import com.dysen.socket_library.SocketThread;
import com.dysen.socket_library.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignOutForcedActivity extends ParentActivity {


    @BindView(R.id.tv_exit)
    TextView tvExit;
    @BindView(R.id.edt_teller_id)
    EditText edtTellerId;
    @BindView(R.id.btn_read_finger)
    Button btnReadFinger;
    @BindView(R.id.tv_read_finger)
    TextView tvReadFinger;
    @BindView(R.id.tv_check_finger)
    TextView tvCheckFinger;
    @BindView(R.id.submit)
    Button submit;
    @BindView(R.id.tv_teller_level)
    TextView tvTellerLevel;
    @BindView(R.id.tv_authorize_code)
    TextView tvAuthorizeCode;
    @BindView(R.id.tv_authorize_info)
    TextView tvAuthorizeInfo;
    @BindView(R.id.tv_)
    TextView tv;
    @BindView(R.id.tv_activation_authorize)
    TextView tvActivationAuthorize;
    private String finger;
    private List<String> transCodeList;
    private String transCode;
    private boolean flagFinger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_out_forced);
        ButterKnife.bind(this);
        this.setFinishOnTouchOutside(false);
        initView();
    }

    private void initView() {
        aty = this;

    }

    @OnClick({R.id.btn_read_finger, R.id.submit, R.id.tv_exit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_read_finger:
                readInfo(IDCheck.READ_FINGER);
                flagFinger = true;
                break;
            case R.id.submit:
                transCode = SocketThread.localAuthorization;
                if (flagFinger) {
                    transCodeList = new ArrayList<>();
                    transCodeList.add(ParamUtils.tellerId);
                    SocketThread.transCode(aty, SocketThread.forcedWithdrawal, transCodeList, handler);
                } else {
                    tvCheckFinger.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.tv_exit:
                finish();
                break;
        }
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
//                    edtTellerFinger.setText(finger);
                    tvReadFinger.setVisibility(View.VISIBLE);

                    initList(finger);

                    LogUtils.d("指纹采取：" + finger);
                    break;
                default:
                    break;
            }
    }

    private List<String> initList(String finger) {
        transCodeList = new ArrayList<>();

        //授权柜员号	AuthSupervisor_ID
        //机构号	HB_Authorization_Branch
        //空	HB_Authorization_Option
        //空	BkFlag1
        //指纹特征值	FPI_tx_model

        transCodeList.add(ViewUtils.getText(edtTellerId));
        transCodeList.add(ParamUtils.orgId);
        transCodeList.add("1");
        transCodeList.add("0");
        transCodeList.add(ParamUtils.terllerFinger2);

        ViewUtils.setViewEnable(aty, submit, true);
        return transCodeList;
    }

    public String s = "";
    public Object obj = null;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
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
        if (transCode.equals(SocketThread.forcedWithdrawal)) {
            transCode = SocketThread.localAuthorization;
//            SocketThread.transCode(aty, transCode, transCodeList, handler);
            SocketThread.setPackMsgHead(transCode, "1", "");
            SocketThread.transCode(aty, transCode, transCodeList, handler);
        } else if (transCode.equals(SocketThread.forcedWithdrawal)) {
            toast("强制签退");
            finish();
        }
//        responsList = (List<String>) obj;
    }
}
