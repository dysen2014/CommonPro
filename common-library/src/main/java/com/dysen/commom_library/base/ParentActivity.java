package com.dysen.commom_library.base;

import android.Manifest;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dysen.commom_library.R;
import com.dysen.commom_library.utils.DialogAlert;
import com.dysen.commom_library.utils.FormatUtil;
import com.dysen.commom_library.views.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

import q.rorbin.badgeview.QBadgeView;


public class ParentActivity extends FragmentActivity {

    public static Context context;
    public static Activity aty;
    protected int curPage = 1;
    private static final int REQUEST_CODE = 0; // 请求码// 所需的全部权限
    static final String[] PERMISSIONS = new String[]{
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.MODIFY_AUDIO_SETTINGS
    };

    protected BluetoothAdapter mBlueToothAdapter;
    public int mIndex;
    private String certTypeIndex;
    private BluetoothManager blueToothManager;

    private List<Activity> activityList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        aty = this;
        context = this;
        activityList.add(this);
        StatusBarUtil.setColor(this, Color.parseColor("#ea452f"), 0);
//		StatusBarUtil.setColorDiff(this, Color.parseColor("#ea452f"));
        blueToothManager=(BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            mBlueToothAdapter=blueToothManager.getAdapter();
        }
    }

    public static void backActivity(final Activity activity, LinearLayout layBack) {
        if (layBack != null) {
            layBack.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    activity.finish();
                }
            });
        }
    }

    public void backActivity(final Activity activity, LinearLayout layBack, final int returnCode) {
        if (layBack != null) {
            layBack.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    setResult(returnCode, new Intent());
                    activity.finish();
                }
            });
        }
    }

    /**
     * 调整窗口的透明度
     * @param from>=0&&from<=1.0f
     * @param to>=0&&to<=1.0f
     *
     * */
    protected void dimBackground(final float from, final float to) {
        final Window window = getWindow();
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(from, to);
        valueAnimator.setDuration(500);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                WindowManager.LayoutParams params = window.getAttributes();
                params.alpha = (Float) animation.getAnimatedValue();
                window.setAttributes(params);
            }
        });

        valueAnimator.start();
    }

    protected String commonSpinner(final Activity aty, final String[] type, final Spinner spinner) {
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

                certTypeIndex = type[pos];
                toast(type[pos]+""+pos);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });
        return certTypeIndex;
    }

    public static class MyUtils {

        public static void setSelItemColor(CardView view, @ColorInt int colorId) {

//		LogUtils.d("colorId:"+colorId);
            if (oldView == null) {
                //第一次点击
                oldView = view;
                oldColor = view.getDrawingCacheBackgroundColor();//当前 iew 的颜色
                view.setCardBackgroundColor(colorId);

            } else {
                //非第一次点击
                //把上一次点击的 变化
                oldView.setBackgroundResource(oldColor);
                view.setCardBackgroundColor(Color.TRANSPARENT);

                oldView = view;
            }
        }
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        setStatusBar();
    }

    protected void setStatusBar() {
//		StatusBarUtil.setTranslucentDiff(this);

    }

    protected void backActivity(View v) {
        finish();
    }

    /**
     * 打开蓝牙 并连接
     *
     * @return
     */
    protected boolean openBle() {
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
     * 绑定 View
     */
    @SuppressWarnings("unchecked")
    protected <T extends View> T bindView(int id) {
        return (T) findViewById(id);
    }

    @SuppressWarnings("unchecked")
    protected <T extends View> T bindView(View view, int id) {
        return (T) view.findViewById(id);
    }

    private static View oldView;
    private static TextView oldTextView;
    static int oldColor;

    /**
     * 设置item 被选中时的效果
     */
    public static void setSelectorItemColor(View view) {

//		LogUtils.d("colorId:"+colorId);
        if (oldView == null) {
            //第一次点击
            oldView = view;
            oldColor = view.getDrawingCacheBackgroundColor();//当前 iew 的颜色
            view.setBackgroundResource(R.color.common_yellow);
        } else {
            //非第一次点击
            //把上一次点击的 变化
            oldView.setBackgroundResource(oldColor);
            view.setBackgroundResource(R.color.common_yellow);
            oldView = view;
        }
    }

    /**
     * 设置item 被选中时的效果
     */
    public static void setSelectorItemColor(View view, @ColorRes @DrawableRes int colorId) {

//		LogUtils.d("colorId:"+colorId);
        if (oldView == null) {
            //第一次点击
            oldView = view;
            oldColor = view.getDrawingCacheBackgroundColor();//当前 iew 的颜色
            view.setBackgroundResource(colorId <= Long.parseLong("7F000000", 16) ? R.color.common_yellow : colorId);
        } else {
            //非第一次点击
            //把上一次点击的 变化
            oldView.setBackgroundResource(oldColor);
            view.setBackgroundResource(colorId <= Long.parseLong("7F000000", 16) ? R.color.common_yellow : colorId);
            oldView = view;
        }
    }

    public static void setSelectorItemColor(View view, @ColorRes @DrawableRes int colorId, @ColorRes @DrawableRes int oldColorId) {

//		LogUtils.d("colorId:"+colorId);
        if (oldView == null) {
            //第一次点击
            oldView = view;
            oldColor = oldColorId;//当前 iew 的颜色
            view.setBackgroundResource(colorId <= Long.parseLong("7F000000", 16) ? R.color.common_yellow : colorId);
        } else {
            //非第一次点击
            //把上一次点击的 变化
            oldView.setBackgroundResource(oldColor);
            view.setBackgroundResource(colorId <= Long.parseLong("7F000000", 16) ? R.color.common_yellow : colorId);
            oldView = view;
        }
    }

    private long lastTime = 0;

    protected boolean checkObjValid(Object obj) {
        if (obj != null)
            return true;
        else
            return false;
    }

    protected boolean checkListValid(List list) {
        if (list != null && list.size() > 0)
            return true;
        else
            return false;
    }

    /**
     * 页面跳转
     *
     * @param cls
     */
    public void gotoNextActivity(Class<?> cls) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        startActivity(intent);
    }

    /**
     * 页面跳转传参数
     *
     * @param cls
     * @param bundle
     */
    public void gotoNextActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    /**
     * Toast消息提示
     *
     * @param text
     */
    public void toast(CharSequence text) {
        // 2s内只提示一次
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastTime > 2000) {
            lastTime = currentTime;
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
        }
    }

    private LinearLayoutManager mLayoutManager;

    protected RecyclerView setRecyclerView(RecyclerView recyclerView) {

        //设置固定大小
        recyclerView.setHasFixedSize(true);
        //创建线性布局
        mLayoutManager = new LinearLayoutManager(this);
        //垂直方向
        mLayoutManager.setOrientation(OrientationHelper.HORIZONTAL);
        //添加分割线
//		recyclerView.addItemDecoration(new TestDecoration(this));
        //给RecyclerView设置布局管理器
        recyclerView.setLayoutManager(mLayoutManager);

        return recyclerView;
    }

    protected RecyclerView setRecyclerView(RecyclerView recyclerView, int orientation) {

        //设置固定大小
        recyclerView.setHasFixedSize(true);
        //创建线性布局
        mLayoutManager = new LinearLayoutManager(context);
        //垂直方向 OrientationHelper.HORIZONTAL 0 OrientationHelper.VERTICAL 1
        mLayoutManager.setOrientation(orientation);
        //添加分割线
//		recyclerView.addItemDecoration(new TestDecoration(this));
        //给RecyclerView设置布局管理器
        recyclerView.setLayoutManager(mLayoutManager);

        return recyclerView;
    }

    protected RecyclerView setRecyclerView(RecyclerView recyclerView, RecyclerView.LayoutManager mLayoutManager) {

        //设置固定大小
        recyclerView.setHasFixedSize(true);
        //创建线性布局
//		mLayoutManager = new LinearLayoutManager(this);
        //垂直方向
//		mLayoutManager.setOrientation(OrientationHelper.HORIZONTAL);
        //添加分割线
//		recyclerView.addItemDecoration(new TestDecoration(this));
        //给RecyclerView设置布局管理器
        recyclerView.setLayoutManager(mLayoutManager);

        return recyclerView;
    }

    /**
     * 提示框
     *
     * @param context
     * @param str
     * @return
     */
    public DialogAlert ShowDialog(Context context, String str) {
        DialogAlert dialog = new DialogAlert(context);
        dialog.show();
        dialog.setMsg(str);
//		dialog.setMsg("抱歉，查询条件不能为空！");

        return dialog;
    }

    public DialogAlert ShowDialog(Context context, View view) {
        DialogAlert dialog = new DialogAlert(context);
        dialog.setContentView(view);
        dialog.show();
//		dialog.setMsg("抱歉，查询条件不能为空！");

        return dialog;
    }

    /**
     * 显示/隐藏 View 设值
     *
     * @param view
     * @param text
     * @param bl
     */
    public void setTextHide(View view, String text, boolean bl) {
        TextView mTextView = null;
        Button btn = null;
        if (bl)
            view.setVisibility(View.INVISIBLE);
        else {
            view.setVisibility(View.VISIBLE);
            if (view instanceof TextView)
                mTextView.setText(text);
            else if (view instanceof Button)
                btn.setText(text);
        }
    }

    /**
     * 弹出软键盘
     */
    private void showKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(view, 0);
    }

    protected void mySetResult(int resultCode, Intent intent) {
        setResult(resultCode, intent);
        finish();
    }

    /**
     * 关闭软键盘
     */
    protected void closeKeyboard() {
        View view = getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    protected void clsOpenKeyboard(EditText editText, boolean flag){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (!flag)
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);//开启或者关闭软键盘
        else
            imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);//开启或者关闭软键盘
        imm.showSoftInput(editText, InputMethodManager.SHOW_FORCED);//弹出软键盘时，焦点定位在输
    }

    /**
     * 屏蔽一系列实体键
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_HOME:
                return true;
//			 case KeyEvent.KEYCODE_BACK:
//			 return true;
            case KeyEvent.KEYCODE_CALL:
                return true;
            case KeyEvent.KEYCODE_SYM:
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                return true;
            case KeyEvent.KEYCODE_VOLUME_UP:
                return true;
            case KeyEvent.KEYCODE_STAR:
                return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    public void setBadgeView(Context context, View view, String text) {

        QBadgeView badge = new QBadgeView(context);
        badge.bindTarget(view);
        badge.setBadgeTextSize(16, true);
        if (FormatUtil.isNumeric(text))
            badge.setBadgeNumber(Integer.parseInt(text));
        else
            badge.setBadgeText(text);
    }

    public void syncScroll(final RecyclerView leftList, final RecyclerView rightList) {
        leftList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (recyclerView.getScrollState() != RecyclerView.SCROLL_STATE_IDLE) {
                    // note: scrollBy() not trigger OnScrollListener
                    // This is a known issue. It is caused by the fact that RecyclerView does not know how LayoutManager will handle the scroll or if it will handle it at all.
                    rightList.scrollBy(dx, dy);
                }
            }
        });

        rightList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (recyclerView.getScrollState() != RecyclerView.SCROLL_STATE_IDLE) {
                    leftList.scrollBy(dx, dy);
                }
            }
        });
    }
    /**
     * 完完全全退出应用程序
     *  弹框
     */

    public void exitApp() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("提示");
        builder.setMessage("您确定要退出程序吗?");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (activityList.size() > 0) {
                    for (Activity activity : activityList) {

                        activity.finish();
                    }

                    android.os.Process.killProcess(android.os.Process.myPid());

                }

            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.show();
    }

    /**
     * 直接退出
     */
    protected void exitApps(){
        if (activityList.size() > 0) {
            for (Activity activity : activityList) {

                activity.finish();
            }
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }
}
