package com.dysen.opencard.ui;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.dysen.commom_library.base.ParentActivity;
import com.dysen.commom_library.utils.DatetimeUtil;
import com.dysen.commom_library.utils.DialogUtils;
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

public class MainActivity extends ParentActivity {

    /**/
    private final int OPEN_CARD = 101;
    private final int ACTIVATION_CARD = 102;
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
    @BindView(R.id.btn_forced_withdrawal)
    Button btnForcedWithdrawal;
    @BindView(R.id.btn_temp_withdrawal)
    Button btnTempWithdrawal;
    @BindView(R.id.ll_title)
    LinearLayout llTitle;
    @BindView(R.id.rbtn_open_card)
    RadioButton rbtnOpenCard;
    @BindView(R.id.rbtn_activation_card)
    RadioButton rbtnActivationCard;
    @BindView(R.id.ll_content_left)
    LinearLayout llContentLeft;
    @BindView(R.id.fl_content)
    FrameLayout flContent;
    @BindView(R.id.ll_content_right)
    LinearLayout llContentRight;
    @BindView(R.id.ll_content)
    LinearLayout llContent;
    @BindView(R.id.tv_org_id)
    TextView tvOrgId;
    @BindView(R.id.tv_teller_name)
    TextView tvTellerName;
    @BindView(R.id.tv_teller_id)
    TextView tvTellerId;
    @BindView(R.id.tv_login_date)
    TextView tvLoginDate;
    @BindView(R.id.ll_bottom)
    LinearLayout llBottom;

    private FragmentManager fragmentManager;
    private OpenCardFragment openCardFragment;//银行开卡
    private ActivationCardFragment activationCardFragment;//卡激活
    private List<String> transCodeList;
    private List<String> responsList;
    private Dialog dialogAuthorize, dialogWithdrawalTellerId;//本地授权弹窗
    private Dialog dialogWithdrawal;//临时签退弹窗
    private String finger;
    //
    private EditText edtTellerFinger;
    private String transCode;
    private TextView tvCheckFinger, tvReadFinger;
    private Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initView();

        setFragmentAll(OPEN_CARD);
    }

    private void initView() {
        aty = this;
        fragmentManager = getSupportFragmentManager();
        txtTitle.setText(getString(R.string.app_name_cn));
        tvOrgId.setText(ParamUtils.orgId);
        tvTellerName.setText(ParamUtils.tellerName);
        tvTellerId.setText(ParamUtils.tellerId);
        tvLoginDate.setText(DatetimeUtil.getTodayF());

        backActivity(this, layBack);
    }

    //先隐藏其他所有的fragment
    private void setItem(Fragment fragment) {

        if (fragment instanceof OpenCardFragment) {
            rbtnActivationCard.setTextColor(aty.getResources().getColor(R.color.gray));
            rbtnOpenCard.setTextColor(aty.getResources().getColor(R.color.white));
        }
        if (fragment instanceof ActivationCardFragment) {
            rbtnOpenCard.setTextColor(aty.getResources().getColor(R.color.gray));
            rbtnActivationCard.setTextColor(aty.getResources().getColor(R.color.white));
        }
    }

    //先隐藏其他所有的fragment
    private void hideFragments(FragmentTransaction transaction) {

        if (openCardFragment != null) {
            transaction.hide(openCardFragment);
        }
        if (activationCardFragment != null) {
            transaction.hide(activationCardFragment);
        }
    }

    //all
    private void setFragmentAll(int index) {

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideFragments(transaction);

        switch (index) {
            case OPEN_CARD://银行开卡
                setItem(openCardFragment);
                if (openCardFragment == null) {
                    openCardFragment = new OpenCardFragment();
                    transaction.add(R.id.fl_content, openCardFragment);
                } else {
                    transaction.show(openCardFragment);
                }
                break;
            case ACTIVATION_CARD:// 卡激活
                setItem(activationCardFragment);
                if (activationCardFragment == null) {
                    activationCardFragment = new ActivationCardFragment();
                    transaction.add(R.id.fl_content, activationCardFragment);
                } else {
                    transaction.show(activationCardFragment);
                }
                break;

        }
        transaction.commitAllowingStateLoss();
    }

    @OnClick({R.id.btn_forced_withdrawal, R.id.btn_temp_withdrawal, R.id.rbtn_open_card, R.id.rbtn_activation_card})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_forced_withdrawal://强制签退
//                transCode(SocketThread.forcedWithdrawal);
                showWithdrawalTellerId();
                break;
            case R.id.btn_temp_withdrawal://临时签退
                SignOutTempActivity.setData(SocketThread.tempWithdrawal);
                gotoNextActivity(SignOutTempActivity.class);
//                transCode(SocketThread.tempWithdrawal);
                break;
            case R.id.rbtn_open_card://开卡
                setFragmentAll(OPEN_CARD);
                break;
            case R.id.rbtn_activation_card://卡激活
                setFragmentAll(ACTIVATION_CARD);
                break;
        }
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
        if (transCode.equals(SocketThread.tempWithdrawal)) {
            toast("临时签退");
            finish();
        } else if (transCode.equals(SocketThread.localAuthorization)) {

            transCodeList = new ArrayList<>();
            transCodeList.add(ParamUtils.tellerId);
            SocketThread.transCode(aty, SocketThread.forcedWithdrawal, transCodeList, handler);
        } else if (transCode.equals(SocketThread.forcedWithdrawal)) {
            toast("强制签退");
            dialogAuthorize.dismiss();
        }
//        gotoNextActivity(SignOutForcedActivity.class);
        responsList = (List<String>) obj;
    }

    private void transCode(String transCode) {
        this.transCode = transCode;
        if (transCode.equals(SocketThread.tempWithdrawal)) {
            showWithdrawal();
        } else if (transCode.equals(SocketThread.forcedWithdrawal)) {
            showWithdrawalTellerId();
        }

//        if (!ParamUtils.tellerId.isEmpty() && !ParamUtils.orgId.isEmpty() &&
//                !ParamUtils.terminalId.isEmpty()) {
//
//            SocketThread.transCode(aty, transCode, transCodeList, handler);
//        } else {
//            toast("输入不能为空！！！");
//        }
    }

    /**
     * 授权柜员号 弹窗
     */
    public void showWithdrawalTellerId() {

        dimBackground(1.0f, 0.5f);
        View view = LayoutInflater.from(aty).inflate(R.layout.dialog_withdrawal_teller_id, null);

        dialogWithdrawalTellerId = DialogUtils.showCloseDialog(aty, view);
        final EditText edtTellerId = (EditText) dialogWithdrawalTellerId.getWindow().findViewById(R.id.edt_teller_id);
        Button btnSubmit = (Button) dialogWithdrawalTellerId.getWindow().findViewById(R.id
                .btn_submit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                toast("请求授权！！！");
                if (!ViewUtils.getText(edtTellerId).isEmpty()) {
                    dialogWithdrawalTellerId.dismiss();
                    dimBackground(0.5f, 1.0f);
                    gotoNextActivity(SignOutForcedActivity.class);
//                showAuthorize();
                } else {
                    toast("请输入柜员号");
                }
//                dialogWithdrawalTellerId.dismiss();
            }
        });
    }

    /**
     * 本地授权弹窗
     */
    public void showAuthorize() {

        View view = LayoutInflater.from(aty).inflate(R.layout.dialog_authorize, null);

        dialogAuthorize = DialogUtils.showCloseDialog(aty, view);
        btnSubmit = (Button) this.dialogAuthorize.getWindow().findViewById(R.id.submit);
        ViewUtils.setViewEnable(aty, btnSubmit, false);
        final EditText edtTellerId = (EditText) this.dialogAuthorize.getWindow().findViewById(R.id.edt_teller_id);
        Button btnReadFinger = (Button) this.dialogAuthorize.getWindow().findViewById(R.id.btn_read_finger);
        Button submit = (Button) this.dialogAuthorize.getWindow().findViewById(R.id.submit);
        tvCheckFinger = (TextView) this.dialogAuthorize.getWindow().findViewById(R.id
                .tv_check_finger);
        tvReadFinger = (TextView) this.dialogAuthorize.getWindow().findViewById(R.id
                .tv_read_finger);
        TextView tvTellerLevel = (TextView) this.dialogAuthorize.getWindow().findViewById(R.id.tv_teller_level);
        TextView tvAuthorizeCode = (TextView) this.dialogAuthorize.getWindow().findViewById(R.id.tv_authorize_code);
        TextView tvAuthorizeInfo = (TextView) this.dialogAuthorize.getWindow().findViewById(R.id.tv_authorize_info);
        TextView tv = (TextView) this.dialogAuthorize.getWindow().findViewById(R.id.tv_);
        TextView tvActivationAuthorize = (TextView) this.dialogAuthorize.getWindow().findViewById(R.id
                .tv_activation_authorize);
        clsOpenKeyboard(edtTellerId, false);
        transCodeList = new ArrayList<>();
        transCodeList.add(ViewUtils.getText(edtTellerId));
        transCodeList.add(ParamUtils.orgId);
        transCodeList.add("1");
        transCodeList.add("0");
        btnReadFinger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                readInfo(IDCheck.READ_FINGER);
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                toast("请求授权！！！");
                SocketThread.transCode(aty, SocketThread.localAuthorization, transCodeList, handler);
            }
        });
    }

    /**
     * 临时授权弹窗
     */
    public void showWithdrawal() {
        View view = LayoutInflater.from(aty).inflate(R.layout.dialog_sign_out, null);

        dialogWithdrawal = DialogUtils.showCloseDialog(aty, view);
        EditText edtTellerId = (EditText) dialogWithdrawal.getWindow().findViewById(R.id
                .edt_teller_id);
        EditText edtOrgId = (EditText) dialogWithdrawal.getWindow().findViewById(R.id
                .edt_org_id);
        EditText edtTerminalId = (EditText) dialogWithdrawal.getWindow().findViewById(R.id
                .edt_terminal_id);
        edtTellerFinger = (EditText) dialogWithdrawal.getWindow().findViewById(R.id
                .edt_teller_finger);
        Button btnReadFinger = (Button) dialogWithdrawal.getWindow().findViewById(R.id
                .btn_read_finger);
        clsOpenKeyboard(edtTellerId, false);
        if (!ParamUtils.tellerId.isEmpty() && !ParamUtils.orgId.isEmpty() &&
                !ParamUtils.terminalId.isEmpty()) {
            ViewUtils.setText(ParamUtils.tellerId, edtTellerId);
            ViewUtils.setText(ParamUtils.orgId, edtOrgId);
            ViewUtils.setText(ParamUtils.terminalId, edtTerminalId);

            transCodeList = new ArrayList<>();
            transCodeList.add(ViewUtils.getText(edtTellerId));
            transCodeList.add("");
            transCodeList.add(ViewUtils.getText(edtOrgId));
            transCodeList.add(ViewUtils.getText(edtTerminalId));

        } else {
            toast("输入不能为空！！！");
        }
        btnReadFinger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                readInfo(IDCheck.READ_FINGER);
            }
        });
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
                    if (transCode.equals(SocketThread.tempWithdrawal)) {
//                        transCodeList.add(finger);
                        transCodeList.add(ParamUtils.terllerFinger);
                        transCodeList.add("0");
                        transCodeList.add("");
//                        dialogWithdrawal.dismiss();
                        SocketThread.transCode(aty, transCode, transCodeList, handler);
                    } else if (transCode.equals(SocketThread.forcedWithdrawal)) {
                        tvReadFinger.setVisibility(View.VISIBLE);
//                        transCodeList.add(finger);
                        transCodeList.add(ParamUtils.terllerFinger2);
                        ViewUtils.setViewEnable(aty, btnSubmit, true);
                    }
//                    toast("指纹：" + finger);
                    LogUtils.d("指纹采取：" + finger);
                    break;
                default:
                    break;
            }
    }
}
