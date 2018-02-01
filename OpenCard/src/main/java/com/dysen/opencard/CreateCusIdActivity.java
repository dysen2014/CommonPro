package com.dysen.opencard;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.dysen.commom_library.base.ParentActivity;
import com.dysen.commom_library.bean.CommonBean;
import com.dysen.commom_library.utils.DialogUtils;
import com.dysen.commom_library.utils.FileUtils;
import com.dysen.commom_library.utils.FormatUtil;
import com.dysen.commom_library.utils.LogUtils;
import com.dysen.commom_library.views.ViewUtils;
import com.dysen.opencard.backClip.IDCheck;
import com.dysen.opencard.common.ParamUtils;
import com.dysen.opencard.common.bean.AreaBean;
import com.dysen.opencard.common.bean.CityBean;
import com.dysen.opencard.common.db.AreaDao;
import com.dysen.socket_library.SocketThread;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreateCusIdActivity extends ParentActivity {

    public static final int CUS_ID = 10001;
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
    @BindView(R.id.country)
    Spinner country;
    @BindView(R.id.province)
    Spinner province;
    @BindView(R.id.county)
    Spinner county;
    @BindView(R.id.cus_add_info)
    EditText cusAddInfo;
    @BindView(R.id.edt_telephone)
    EditText edtTelephone;
    @BindView(R.id.edt_postal_code)
    EditText edtPostalCode;
    @BindView(R.id.professional_code)
    Spinner professionalCode;
    @BindView(R.id.submit)
    Button submit;
    @BindView(R.id.cancel)
    Button cancel;
    @BindView(R.id.ll_cus_id)
    LinearLayout llCusId;

    private Dialog dialog;
    private String finger;
    List<CityBean> cityBeans;

    final public static int CUS_NUM = 101, CUS_NUM_NO = 102;
    private static List<AreaBean> listProvince = new ArrayList<AreaBean>();// 省
    private List<AreaBean> listCity = new ArrayList<AreaBean>();// 市
    private List<AreaBean> listArea = new ArrayList<AreaBean>();// 区
    private List<AreaBean> listCounty = new ArrayList<AreaBean>();// 县
    private AreaDao dao;
    private int indexHuBei;
    private String[] chars;
    private List<String> list;
    private List<String> lists;
    private List<String> transCodeList;
    private String certName, certTypeStr, certId, sex, beginDate, endDate;
    private String countryStr;//国家
    private String provinceStr;//省
    private String countyStr;//市
    private String professionalCodeStr;//职业代码
    private List<String> listHeader = new ArrayList<>();
    private List<String> listBody = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_cus_id);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        aty = this;
        txtBack.setText(R.string.open_card);
        backActivity(this, layBack, -1);
        cusAddInfo.setText("武昌区汉街总部国际");
        edtPostalCode.setText("430000");
        edtTelephone.setText("18971642933");
        setValue();
        String[] cert_type = getResources().getStringArray(R.array.cert_type);
        String[] professional_code = getResources().getStringArray(R.array.professional_code);
//        commonSpinner(aty, cert_type, certType);
        commonSpinners(aty, professional_code, professionalCode);

//        HttpThread.sendRequestGet("http://192.168.112.163:8080/city.json", handler);
        dao = new AreaDao(aty);

//        selectProvince(1);//选择省份
//        selectProvince2();
        selectCountry();//选择国家
    }

    private void selectProvince2() {
        List<CommonBean.CountryBean> countArrayList = FileUtils.getCountArrayList(
                FileUtils.readFromAssets(aty, "province.txt"), 3);
    }


    public void setValue() {

        certName = getIntent().getStringExtra("cert_name");
        certTypeStr = getIntent().getStringExtra("cert_type_str");
        certId = getIntent().getStringExtra("cert_id");
        sex = getIntent().getStringExtra("cert_sex");
        beginDate = getIntent().getStringExtra("cert_begin");
        endDate = getIntent().getStringExtra("cert_end");

        ViewUtils.setText(aty, certName, customerName);
        ViewUtils.setText(aty, certId, certNumber);
        ViewUtils.setText(aty, beginDate, certIssuedStart);
        ViewUtils.setText(aty, endDate, certIssuedEnd);
        commonSpinners(aty, new String[]{certTypeStr}, certType);//客户证件信息反显
        LogUtils.i("=====================" + sex);
        if (sex.equals("女"))
            rbt1.setChecked(true);//女
        else if (sex.equals("男"))
            rbt0.setChecked(true);//男
    }

    private void selectCountry() {
     /*   List<CommonBean.CountryBean> countArrayList = FileUtils.getCountArrayList(
                FileUtils.readFromAssets(aty, "country.txt"), 2);
        String[] str = new String[countArrayList.size()];*/

   /*     for (int i = 0; i < countArrayList.size(); i++) {
            str[i] = countArrayList.get(i).getId().replace("\"", "")+":"+countArrayList.get(i)
                    .getName().replace("\"", "");
        }*/
//        String[] str = FileUtils.getArryString(FileUtils.readFromAssets(aty,"country.txt"),2);
        String[] str = ParamUtils.countrys;
        commonSpinners(aty, str, country);
    }

    //选择省份
    private void selectProvince(int index) {
        listProvince.clear();
        if (index == 0) {
            listProvince = dao.getAllMessage("000000");

        } else {
            listProvince.add(new AreaBean("0", "境外"));
        }
        if (!listProvince.isEmpty()) {
            list = new ArrayList<>();
            for (int i = 0, size = listProvince.size(); i < size; i++) {
//                if (listProvince.get(i).getName().equals("湖北省")) {
//                    indexHuBei = i;
//                    list.add(0, listProvince.get(i).getName());
//                }else
                list.add(i, listProvince.get(i).getName());
            }
        }
        chars = (String[]) list.toArray(new String[list.size()]);
        commonSpinners(aty, chars, province);
    }

    private void selectCounty(int index) {
        listCity.clear();
        if (listProvince.get(0).getName().equals("境外")) {
            listCity.add(new AreaBean("0", "境外"));
        } else {
            LogUtils.d(indexHuBei + "*****************************" + index);
//            if (index == 0)
//                listCity = dao.getAllMessage(listProvince.get(indexHuBei).getId());
//            else
            listCity = dao.getAllMessage(listProvince.get(index).getId());
        }
        lists = new ArrayList<>();
        for (int i = 0; i < listCity.size(); i++) {
//            if (listCity.get(i).getName().equals("武汉市")) {
//                list.add(0, listCity.get(i).getName());
//            }else
            lists.add(i, listCity.get(i).getName());

        }
        chars = (String[]) lists.toArray(new String[lists.size()]);
        commonSpinners(aty, chars, county);
    }

    int certTypeIndex = 0;

    private void commonSpinners(final Activity aty, final String[] type, final Spinner spinner) {
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
                if (spinner.equals(country)) {
                    countryStr = type[pos];
                    selectProvince(pos);
                }
                if (spinner.equals(province)) {
                    provinceStr = type[pos];
                    selectCounty(pos);//选择市，区
                }
                if (spinner.equals(county)) {
                    countyStr = type[pos];
                }
                if (spinner.equals(professionalCode)) {
                    professionalCodeStr = type[pos];
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });
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
        listBody = (List<String>) obj;
        listHeader = com.dysen.socket_library.utils.ParamUtils.responsHeader;
        if (listBody.size() == 1) {
            Intent intent = new Intent();
            intent.putExtra("cus_num", String.valueOf(Integer.valueOf(listBody.get(0))));
            mySetResult(CUS_NUM, intent);
        }
        LogUtils.i("respons=" + listBody);
    }

    @OnClick({R.id.submit, R.id.cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.submit:
//                toast("点击了 授权！！！");
//                show();
                createCusId();
                break;
            case R.id.cancel:
                mySetResult(CUS_NUM_NO, new Intent());
                break;
        }
    }

    /**
     * 开户
     */
    private void createCusId() {
        if (FormatUtil.isMobileNO(ViewUtils.getText(edtTelephone))) {

        } else {
            toast("移动电话不合法！");
        }
        initList();
        SocketThread.transCode(aty, SocketThread.createCus, transCodeList, handler);
    }

    public void show() {

        View view = LayoutInflater.from(aty).inflate(R.layout.dialog_authorize, null);

        dialog = DialogUtils.showCloseDialog(aty, view);
        Button btnSubmit = (Button) this.dialog.getWindow().findViewById(R.id.submit);
        EditText edtTellerId = (EditText) this.dialog.getWindow().findViewById(R.id.edt_teller_id);
        Button btnReadFinger = (Button) this.dialog.getWindow().findViewById(R.id.btn_read_finger);
        final TextView tvReadFinger = (TextView) this.dialog.getWindow().findViewById(R.id.tv_read_finger);
        Button submit = (Button) this.dialog.getWindow().findViewById(R.id.submit);
        TextView tvTellerLevel = (TextView) this.dialog.getWindow().findViewById(R.id.tv_teller_level);
        TextView tvAuthorizeCode = (TextView) this.dialog.getWindow().findViewById(R.id.tv_authorize_code);
        TextView tvAuthorizeInfo = (TextView) this.dialog.getWindow().findViewById(R.id.tv_authorize_info);
        TextView tv = (TextView) this.dialog.getWindow().findViewById(R.id.tv_);
        TextView tvActivationAuthorize = (TextView) this.dialog.getWindow().findViewById(R.id.tv_activation_authorize);
        clsOpenKeyboard(edtTellerId, false);
        btnReadFinger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                readInfo(IDCheck.READ_FINGER);
                tvReadFinger.setVisibility(View.VISIBLE);
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                toast("请求授权！！！");
//                initList();
                SocketThread.transCode(aty, SocketThread.localAuthorization, transCodeList, handler);
                dialog.dismiss();
            }
        });
    }

    private List<String> initList() {

        transCodeList = new ArrayList<>();

        transCodeList.add("01");                            // HB_CustType         客户类型         默认为-01   个人客户
        transCodeList.add("101");                                   // HB_CustSubtype      客户子类型        默认为-101  个人
        transCodeList.add(certTypeStr);                             // HB_IDType           证件类型         默认为-01   身份证
        transCodeList.add(certId);                                  // HB_IDNum            证件号码
        transCodeList.add("12");                                    // HB_LanguageCode     通讯语言         默认为-12   中文
        transCodeList.add(beginDate);                               // HB_IDIssueDate      证件签发日期       DDMMYYYY
        transCodeList.add(endDate);                                 // HB_IDExpiryDate     证件到期日        DDMMYYYY
        transCodeList.add(certName);                                // HB_LastName         姓名
        transCodeList.add(ViewUtils.getText(edtTelephone));         // HB_Mobile           移动电话
        transCodeList.add(ViewUtils.getText(cusAddInfo));           // HB_HomeAddr1        家庭地址
        transCodeList.add(countyStr);                               // HB_HomeAddr2        区/县          　
        transCodeList.add(provinceStr);                             // HB_HomeAddr3        省/市
        transCodeList.add(countryStr);                              // HB_HomeAddr4        国家
        transCodeList.add(ViewUtils.getText(edtPostalCode));        // HB_PostCode1        邮政编码
        transCodeList.add("CN");                                    // HB_Nationality      国籍           默认为-CN 中国
        transCodeList.add(sex);                                     // HB_SexCode          性别           1－男   0－女
        transCodeList.add("1");                                     // HB_IsResident       居民/非居民       默认为1     1是  2否
        transCodeList.add(professionalCodeStr);                     // HB_OccupationCode   职业代码
        return transCodeList;
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
                    toast("指纹：" + finger);
                    break;
                default:
                    break;
            }
    }
}
