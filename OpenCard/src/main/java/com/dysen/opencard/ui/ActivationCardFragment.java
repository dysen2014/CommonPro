package com.dysen.opencard.ui;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.dysen.commom_library.base.ParentFragment;
import com.dysen.commom_library.views.ViewUtils;
import com.dysen.opencard.R;
import com.dysen.opencard.backClip.IDCheck;
import com.dysen.socket_library.SocketThread;
import com.dysen.socket_library.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 *
 */
public class ActivationCardFragment extends ParentFragment {


    @BindView(R.id.cardNumber)
    EditText cardNumber;
    @BindView(R.id.read_card)
    Button readCard;
    @BindView(R.id.old_card_password)
    EditText oldCardPassword;
    @BindView(R.id.read_old_card_password)
    Button readOldCardPassword;
    @BindView(R.id.new_card_password)
    EditText newCardPassword;
    @BindView(R.id.read_new_card_password)
    Button readNewCardPassword;
    @BindView(R.id.submit)
    Button submit;
    @BindView(R.id.cancel)
    Button cancel;
    @BindView(R.id.ll_cus_id)
    LinearLayout llCusId;
    Unbinder unbinder;
    private BluetoothAdapter mBlueToothAdapter;
    private int index;
    Activity aty;
    private String cardId, cardPwd, cardOldPwd,
            magCardId;
    private String idCardNumber;
    private String oldPassword, newPassword;
    private List<String> transCodeList;
    private List<String> responsList;
    private boolean isRespons = false;
    private int btn_Flage = 0;//读取密码标志位

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activation_card, container, false);

        unbinder = ButterKnife.bind(this, view);

        aty = getActivity();
        mBlueToothAdapter = BluetoothAdapter.getDefaultAdapter();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.read_old_card_password, R.id.read_new_card_password, R.id.submit, R.id.cancel, R.id.read_card})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.read_old_card_password:
                btn_Flage = 1;
                readInfo(IDCheck.READ_OLD_PASSWORD);
                break;
            case R.id.read_new_card_password:
                btn_Flage = 2;
                readInfo(IDCheck.READ_PASSWORD);
                break;
            case R.id.submit://提交验证
                cardActhiveSubmitData();
                break;
            case R.id.cancel:
                break;
            case R.id.read_card://读IC卡
                readInfo(IDCheck.READ_CARD);
                break;
        }
    }

    /**
     * 卡激活提交数据
     */
    private void cardActhiveSubmitData() {
        idCardNumber = ViewUtils.getText(cardNumber);
        oldPassword = ViewUtils.getText(oldCardPassword);
        newPassword = ViewUtils.getText(newCardPassword);
        if (TextUtils.isEmpty(idCardNumber)) {
            toast("卡号不能为空");
            return;
        } else if (TextUtils.isEmpty(oldPassword)) {
            toast("旧密码不能为空");
            return;
        } else if (TextUtils.isEmpty(newPassword)) {
            toast("新密码不能为空");
            return;
        } else if (TextUtils.equals(oldPassword, newPassword)) {
            toast("旧密码与新密码不能一致");
            return;
        }
        //验证通过，修改密码，先查询,再走修改交易
        transCode(SocketThread.selectPwd);
        if (isRespons) {
            requestUpdatePwdCode(SocketThread.changePwd);
        }
    }

    private void requestUpdatePwdCode(String transCode) {
        if (!TextUtils.isEmpty(idCardNumber)) {
            transCodeList = new ArrayList<String>();
            transCodeList.clear();
            transCodeList = responsList;
            transCodeList.add(oldPassword);
            transCodeList.add(newPassword);
            SocketThread.transCode(aty, transCode, transCodeList, handler);
        } else {
            toast("输入不能为空！！！");
        }
    }

    private void transCode(String transCode) {
        if (!TextUtils.isEmpty(idCardNumber)) {
            transCodeList = new ArrayList<String>();
            transCodeList.add(idCardNumber);
            transCodeList.add("");
            SocketThread.transCode(aty, transCode, transCodeList, handler);
        } else {
            toast("输入不能为空！！！");
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
                parseResponsData(obj);
            }
            LogUtils.d((s + "======================================(List<String>) " +
                    "obj===========================" + (List<String>) obj));
            toast("" + s);
            if (isRespons) {
                Toast.makeText(aty, "修改密码成功", Toast.LENGTH_LONG);
            }
        }
    };

    //解析报文返回来的数据
    private void parseResponsData(Object obj) {
        responsList = (List<String>) obj;
        String result[] = new String[responsList.size()];
        LogUtils.i("hut", "查询卡密码返回结果=" + responsList);
//        for(int i=0;i<responsList.size();i++){
//            result[i]= TransParseStringEncodUtil.getGbkToUtfString(responsList.get(i));
//        }
        LogUtils.i("hut", "查询卡密码返回数组结果=" + result.toString());
        if (responsList.size() > 0) {//返回有数据
            isRespons = true;
            LogUtils.i("hut", "查询卡密码返回有结果修改密码=" + isRespons);
        }
    }

    /**
     * 打开蓝牙 并连接
     *
     * @return
     */
    private boolean openBle() {
        if (mBlueToothAdapter != null && !mBlueToothAdapter.isEnabled()) {
            mBlueToothAdapter.enable();
            toast("opne blue success!");
        }
        SystemClock.sleep(1000);
        if (mBlueToothAdapter.isEnabled()) {
            toast("蓝牙已打开，请操作！");
            return true;
        } else {
            toast("请先打开蓝牙，再操作！");
        }
        return false;
    }

    /**
     * 读取背夹信息
     */
    private void readInfo(int index) {
        if (openBle()) {
            this.index = index;
            Intent intent = new Intent(aty, IDCheck.class);
            intent.putExtra(IDCheck.FUNC_NAME, index);
            startActivityForResult(intent, index);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == index) {
            switch (resultCode) {
                case IDCheck.READ_CARD://读银行卡
                    cardId = intent.getStringExtra("card_id");
                    ViewUtils.setText(aty, cardId, cardNumber);
                    break;
                case IDCheck.READ_PASSWORD://读密码
                    cardPwd = intent.getStringExtra("card_pwd");
                    if (btn_Flage == 1) {
                        toast("旧密码：" + cardPwd);
                        ViewUtils.setText(aty, cardPwd, oldCardPassword);
                    } else if (btn_Flage == 2) {
                        toast("新密码：" + cardPwd);
                        ViewUtils.setText(aty, cardPwd, newCardPassword);
                    }
                    break;
                case IDCheck.READ_MAGCARD://读磁条卡
                    magCardId = intent.getStringExtra("mag_card_id");
                    toast("磁条卡：" + magCardId);
                    break;
                default:
                    break;
            }
        }
    }
}
