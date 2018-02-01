package com.dysen.opencard;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.dysen.commom_library.base.ParentActivity;
import com.dysen.commom_library.views.ViewUtils;
import com.dysen.opencard.backClip.IDCheck;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OpenCusActivity extends ParentActivity {


    @BindView(R.id.txt_back)
    TextView txtBack;
    @BindView(R.id.lay_back)
    LinearLayout layBack;
    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.txt_)
    TextView txt;
    @BindView(R.id.customerName)
    EditText customerName;
    @BindView(R.id.rbt_0)
    RadioButton rbt0;
    @BindView(R.id.rbt_1)
    RadioButton rbt1;
    @BindView(R.id.iv_ocr)
    ImageView ivOcr;
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
    @BindView(R.id.read_card)
    ImageView readCard;
    @BindView(R.id.card_password)
    EditText cardPassword;
    @BindView(R.id.read_card_password)
    ImageView readCardPassword;
    @BindView(R.id.card_type_product)
    EditText cardTypeProduct;
    @BindView(R.id.account_type_product)
    Spinner accountTypeProduct;
    @BindView(R.id.product_child)
    Spinner productChild;
    @BindView(R.id.card_pwd2)
    EditText cardPwd2;
    @BindView(R.id.submit)
    Button submit;
    @BindView(R.id.cancel)
    Button cancel;
    @BindView(R.id.ll_cus_id_info)
    LinearLayout llCusIdInfo;
    private BluetoothAdapter mBlueToothAdapter;
    Activity aty;
    private int index;
    private String certName, certId, cardId, cardPwd, magCardId, finger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_cus);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        txtTitle.setText("");
        tvCreatePersonCusId.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
//        tvCreatPersonCusId.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        aty = this;
        mBlueToothAdapter = BluetoothAdapter.getDefaultAdapter();
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
                llNoCusIdInfo.setVisibility(View.VISIBLE);
                break;
            case R.id.tv_create_person_cus_id://创建个人客户号
                startActivityForResult(new Intent(aty, CreateCusIdActivity.class),
                        CreateCusIdActivity.CUS_ID);
                break;
            case R.id.submit://提交
                break;
            case R.id.cancel://关闭
                break;
        }
    }
    /**
     * 打开蓝牙 并连接
     *
     * @return
     */
//    protected boolean openBle() {
//        if (mBlueToothAdapter != null && !mBlueToothAdapter.isEnabled()) {
//            mBlueToothAdapter.enable();
//            toast("opne blue success!");
//        }
//        SystemClock.sleep(1000);
//        if (mBlueToothAdapter.isEnabled()) {
//            toast("蓝牙已打开，请操作！");
//            return true;
//        } else {
//            toast("请先打开蓝牙，再操作！");
//        }
//        return false;
//    }

    /**
     * 读身份证
     */
    private void readInfo(int index) {
        if (openBle()) {
            this.index = index;
            Intent intent = new Intent(this, IDCheck.class);
            intent.putExtra(IDCheck.FUNC_NAME, index);
            startActivityForResult(intent, index);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == index)
            switch (resultCode) {
                case IDCheck.READ_CERT://读身份证
                    certName = intent.getStringExtra("cert_name");
                    certId = intent.getStringExtra("cert_id");
                    ViewUtils.setText(aty, certName, customerName);
                    ViewUtils.setText(aty, certId, certNumber);
                    String certSex = certId.substring(certId.length() - 2);
                    if (Integer.parseInt(certSex) % 2 == 0)
                        rbt1.setChecked(true);//女
                    else
                        rbt0.setChecked(true);//男

                    break;
                case IDCheck.READ_CARD://读银行卡
                    cardId = intent.getStringExtra("card_id");
                    ViewUtils.setText(aty, cardId, cardNumber);
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
                default:
                    break;
            }
    }
}
