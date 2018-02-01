package com.dysen.opencard.ui;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.dysen.commom_library.base.ParentFragment;
import com.dysen.commom_library.utils.CodeFormatUtils;
import com.dysen.commom_library.views.ViewUtils;
import com.dysen.opencard.CreateCusIdActivity;
import com.dysen.opencard.R;
import com.dysen.opencard.backClip.IDCheck;
import com.dysen.opencard.common.ParamUtils;
import com.dysen.opencard.common.StrUtils;
import com.dysen.socket_library.SocketThread;
import com.dysen.socket_library.utils.LogUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 银行开卡
 */
public class OpenCardFragment extends ParentFragment {


    @BindView(R.id.customerName)
    EditText customerName;
    @BindView(R.id.iv_ocr)
    ImageView ivOcr;
    @BindView(R.id.rbt_0)
    RadioButton rbt0;
    @BindView(R.id.rbt_1)
    RadioButton rbt1;
    @BindView(R.id.certType)
    Spinner certType;
    @BindView(R.id.certNumber)
    EditText certNumber;
    @BindView(R.id.cert_issued_start)
    EditText certIssuedStart;
    @BindView(R.id.cert_issued_end)
    EditText certIssuedEnd;
    @BindView(R.id.read_cert)
    Button readCert;
    @BindView(R.id.seach)
    Button seach;
    @BindView(R.id.ll_cus_id)
    LinearLayout llCusId;
    @BindView(R.id.line)
    View line;
    @BindView(R.id.tv_cus_id_info)
    TextView tvCusIdInfo;
    @BindView(R.id.tv_create_person_cus_id)
    TextView tvCreatePersonCusId;
    @BindView(R.id.ll_no_cus_id_info)
    LinearLayout llNoCusIdInfo;
    @BindView(R.id.customerNum)
    EditText customerNum;
    @BindView(R.id.account_type)
    Spinner accountType;
    @BindView(R.id.cardNumber)
    EditText cardNumber;
    @BindView(R.id.card_password)
    EditText cardPassword;
    @BindView(R.id.card_pwd2)
    EditText cardPwd2;
    @BindView(R.id.read_card)
    Button readCard;
    @BindView(R.id.read_card_password)
    Button readCardPassword;
    @BindView(R.id.card_type_product)
    EditText cardTypeProduct;
    @BindView(R.id.account_type_product)
    Spinner accountTypeProduct;
    @BindView(R.id.product_child)
    Spinner productChild;
    @BindView(R.id.submit)
    Button submit;
    @BindView(R.id.cancel)
    Button cancel;
    @BindView(R.id.ll_cus_id_info)
    LinearLayout llCusIdInfo;

    private BluetoothAdapter mBlueToothAdapter;
    Activity aty;
    private int index;
    String[] product_child0;
    String[] product_child1;
    String[] product_child2;
    String[] product_child3;
    private String[] account_type_product;
    private String[] account_type;

    private String certName, certId, sex, beginDate, endDate, cardId, cardPwd, magCardId, finger;
    private List<String> transCodeList;
    private List<String> responsList;
    private String certTypeId;
    private String certTypeStr, productStr, productChildStr;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_open_card, container, false);

        ButterKnife.bind(this, view);

        initView();

        return view;
    }

    private void initView() {
        tvCreatePersonCusId.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
//        tvCreatPersonCusId.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        aty = getActivity();
        customerNum.setText("0000080000004428");
        mBlueToothAdapter = BluetoothAdapter.getDefaultAdapter();
        tvCreatePersonCusId.setEnabled(false);
        final String[] cert_type = getResources().getStringArray(R.array.cert_type);
        account_type_product = getResources().getStringArray(R.array.account_type_product);
        account_type = getResources().getStringArray(R.array.account_type);
        product_child0 = getResources().getStringArray(R.array.product_child0);
        product_child1 = getResources().getStringArray(R.array.product_child1);
        product_child2 = getResources().getStringArray(R.array.product_child2);
        product_child3 = getResources().getStringArray(R.array.product_child3);

        commonSpinner(aty, cert_type, certType);
//        certTypeId = certTypeStr.substring(0, 2);//证件类型2个长度
        commonSpinner(aty, account_type, accountType);
    }

    int certTypeIndex = 0;

    private void commonSpinner(final Activity aty, final String[] type, final Spinner spinner) {
        // 建立Adapter并且绑定数据源
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(aty, android.R.layout
                .simple_spinner_item, type);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //绑定 Adapter到控件
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {

                certTypeIndex = pos;
                toast(type[pos] + "" + pos);
                if (spinner.equals(certType)) {
                    certTypeStr = type[pos];
                }
                if (spinner.equals(accountTypeProduct)) {
                    productChild(pos);
                    productStr = type[pos];
                }
                if (spinner.equals(productChild)) {
                    productChildStr = type[pos];
                }
                if (spinner.equals(accountType)) {
                    accountTypeProduct(pos);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });
        LogUtils.i(certTypeIndex + "--------type--------" + type.length);
    }

    private void accountTypeProduct(int pos) {
        switch (pos) {
            case 0:
                commonSpinner(aty, new
                        String[]{account_type_product[0]}, accountTypeProduct);
                break;
            case 1:
                commonSpinner(aty, new
                        String[]{account_type_product[1]}, accountTypeProduct);
                break;
            case 2:
                commonSpinner(aty, new
                        String[]{account_type_product[2]}, accountTypeProduct);
                break;
            case 3:
                commonSpinner(aty, new
                        String[]{account_type_product[3]}, accountTypeProduct);
                break;
        }
    }

    /**
     * 子产品
     *
     * @param pos
     */
    private void productChild(int pos) {
        LogUtils.i("" + pos);
        switch (pos) {
            case 0:
                commonSpinner(aty, product_child0, productChild);
                break;
            case 1:
                commonSpinner(aty, product_child1, productChild);
                break;
            case 2:
                commonSpinner(aty, product_child2, productChild);
                break;
            case 3:
                commonSpinner(aty, product_child3, productChild);
                break;
        }
    }

    @OnClick({R.id.read_cert, R.id.seach, R.id.tv_create_person_cus_id, R.id.read_card, R.id
            .read_card_password, R.id.submit, R.id.cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.read_cert://读身份证
                readInfo(IDCheck.READ_CERT);
                break;
            case R.id.read_card://读银行卡
                readInfo(IDCheck.READ_CARD);
                break;
            case R.id.read_card_password://读密码
                readInfo(IDCheck.READ_PASSWORD);
                break;
            case R.id.seach://搜索
                transCode(SocketThread.selectCus);
                break;
            case R.id.tv_create_person_cus_id://创建个人客户号
                createCusId();
                break;
            case R.id.submit://提交
                break;
            case R.id.cancel://关闭
                break;
        }
    }

    private void createCusId() {
        //开户界面客户信息 自动填充
//        new CreateCusIdActivity().setCertData(certName, certTypeStr, certId, sex, beginDate, endDate);
//        certName=certTypeStr=certId= sex= beginDate= endDate="";
        Intent intent = new Intent(aty, CreateCusIdActivity.class);
        intent.putExtra("cert_name", certName);
        intent.putExtra("cert_type_str", certTypeStr);
        intent.putExtra("cert_id", certId);
        intent.putExtra("cert_sex", sex);
        beginDate = beginDate.substring(beginDate.length() - 2) + beginDate.substring(beginDate.length() - 4, beginDate.length() - 2) + beginDate.substring(0, beginDate.length() - 4);
        endDate = endDate.substring(endDate.length() - 2) + endDate.substring(beginDate.length() - 4, beginDate.length() - 2) + beginDate.substring(0, endDate.length() - 4);
        LogUtils.i(beginDate + "-----------end----------" + endDate);
        intent.putExtra("cert_begin", beginDate);
        intent.putExtra("cert_end", endDate);
        LogUtils.i("certName=" + certName + "\tcertTypeStr=" + certTypeStr + "\tcertId=" + certId + "\tsex=" + sex + "\tbeginDate=" + beginDate + "\tendDate=" + endDate);
        if (!certName.isEmpty() && !certTypeStr.isEmpty() && !certId.isEmpty() && !sex.isEmpty() && !beginDate.isEmpty() && !endDate.isEmpty())
            startActivityForResult(intent,
                    CreateCusIdActivity.CUS_ID);
        else
            toast("上面参数有误！");
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
            LogUtils.d((s + "======================================(List<String>) " +
                    "obj===========================" + (List<String>) obj));
        }
    };

    private void parseData(Object obj) {
        responsList = (List<String>) obj;
        if (responsList != null && responsList.size() > 0) {
            cardNumber.setText(responsList.get(0));
        } else {//没有客户号，创建客户信息(开户)
            llNoCusIdInfo.setVisibility(View.VISIBLE);
            llCusIdInfo.setVisibility(View.GONE);
        }
        byte[] bytes = com.dysen.socket_library.utils.ParamUtils.respMsgByte;

        String cardNum = CodeFormatUtils.byte2StrIntercept(bytes, 158, 19);
        String cardProductName = CodeFormatUtils.byte2StrIntercept(bytes, 177, 40);
        String cusNum = CodeFormatUtils.byte2StrIntercept(bytes, 217, 17);
        String cusName = CodeFormatUtils.byte2StrIntercept(bytes, 234, 40);
        String addr1 = CodeFormatUtils.byte2StrIntercept(bytes, 274, 40);
        String addr2 = CodeFormatUtils.byte2StrIntercept(bytes, 314, 20);
        String addr3 = CodeFormatUtils.byte2StrIntercept(bytes, 334, 20);
        String addr4 = CodeFormatUtils.byte2StrIntercept(bytes, 354, 20);
        String postalCode = CodeFormatUtils.byte2StrIntercept(bytes, 374, 8);
        String cardName = CodeFormatUtils.byte2StrIntercept(bytes, 382, 26);
        String cardName2 = CodeFormatUtils.byte2StrIntercept(bytes, 408, 26);
        String cardDate = CodeFormatUtils.byte2StrIntercept(bytes, 434, 8);
        String cardBin = CodeFormatUtils.byte2StrIntercept(bytes, 442, 40);
        String cardId = CodeFormatUtils.byte2StrIntercept(bytes, 482, 1);
        String cardType = CodeFormatUtils.byte2StrIntercept(bytes, 483, 5);
        LogUtils.i(
                "" + cardNum
                        + "" + cardProductName
                        + "" + cusNum
                        + "" + cusName
                        + "" + addr1
                        + "" + addr2
                        + "" + addr3
                        + "" + addr4
                        + "" + postalCode
                        + "" + cardName
                        + "" + cardName2
                        + "" + cardDate
                        + "" + cardBin
                        + "" + cardId
                        + "" + cardType);
        toast(StrUtils.getState(bytes));
        if (StrUtils.getState(bytes).equals(ParamUtils.TRANS_SUCCESS)) {

        }
    }

    private List<String> initList(String transCode) {

        transCodeList = new ArrayList<>();

        if (transCode.equals(SocketThread.selectCus)) {
            transCodeList.add(ViewUtils.getText(certNumber));
            transCodeList.add(certTypeStr.substring(0, 2));
            transCodeList.add(ViewUtils.getText(customerName));
        } else if (transCode.equals(SocketThread.openCard)) {
            transCodeList.add(ViewUtils.getText(customerNum));
            transCodeList.add(ViewUtils.getText(cardNumber));
            transCodeList.add(ViewUtils.getText(cardPassword));
            transCodeList.add(productStr.substring(0, 4));
            transCodeList.add(productChildStr.substring(0, 4));
        }
        return transCodeList;
    }

    private void transCode(String transCode) {
        if ((!ViewUtils.getText(certNumber).isEmpty() && !ViewUtils.getText(customerName).isEmpty
                ()) || (!ViewUtils.getText(customerNum).isEmpty() && !ViewUtils.getText(cardNumber).isEmpty
                () && !ViewUtils.getText(cardPassword).isEmpty())) {
            initList(transCode);
            SocketThread.transCode(aty, transCode, transCodeList, handler);
        } else {
            toast("输入不能为空！！！");
            llNoCusIdInfo.setVisibility(View.VISIBLE);
            llCusIdInfo.setVisibility(View.GONE);
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
     * 读身份证
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
                case IDCheck.READ_CERT://读身份证
                    certName = intent.getStringExtra("cert_name");
                    certId = intent.getStringExtra("cert_id");
                    sex = intent.getStringExtra("cert_sex");
                    beginDate = intent.getStringExtra("cert_begin");
                    endDate = intent.getStringExtra("cert_end");

                    ViewUtils.setText(aty, certName, customerName);
                    ViewUtils.setText(aty, certId, certNumber);
                    ViewUtils.setText(aty, beginDate, certIssuedStart);
                    ViewUtils.setText(aty, endDate, certIssuedEnd);
                    LogUtils.d("性别：" + sex);
                    if (sex.equals("女"))
                        rbt1.setChecked(true);//女
                    else if (sex.equals("男"))
                        rbt0.setChecked(true);//男
//                    String certSex = certId.substring(certId.length() - 2, certId.length() - 1);
//                    if (Integer.parseInt(certSex) % 2 == 0)
//                        rbt1.setChecked(true);//女
//                    else
//                        rbt0.setChecked(true);//男
                    tvCreatePersonCusId.setEnabled(true);
                    break;
                case IDCheck.READ_CARD://读银行卡
                    cardId = intent.getStringExtra("card_id");
                    ViewUtils.setText(aty, cardId, cardNumber);
                    SocketThread.transCode(aty, SocketThread.cardPwdSelect, Arrays.asList(cardId, ""), handler);
                    break;
                case IDCheck.READ_PASSWORD://读密码
                    cardPwd = intent.getStringExtra("card_pwd");
                    ViewUtils.setText(aty, cardPwd, cardPassword);
                    break;
                case IDCheck.READ_MAGCARD://读磁条卡
                    magCardId = intent.getStringExtra("mag_card_id");
                    toast("磁条卡：" + magCardId);
                    break;
                case IDCheck.READ_FINGER://读指纹
                    finger = intent.getStringExtra("finger");
                    toast("指纹：" + finger);
                    break;
                case CreateCusIdActivity.CUS_NUM:
                    String cus_num = intent.getStringExtra("cus_num");
                    ViewUtils.setText(aty, cus_num, customerNum);
                    break;
                case CreateCusIdActivity.CUS_NUM_NO:

                    break;
                default:
                    break;
            }
        } else if (requestCode == CreateCusIdActivity.CUS_ID) {

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
