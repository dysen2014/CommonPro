package com.dysen.commom_library.base;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dysen.common_res.R;
import com.dysen.common_res.common.utils.DialogAlert;
import com.dysen.common_res.common.utils.FormatUtil;
import com.dysen.common_res.common.utils.permission.PermissionsActivity;
import com.dysen.common_res.common.utils.permission.PermissionsChecker;
import com.jaeger.library.StatusBarUtil;

import java.util.List;

import q.rorbin.badgeview.QBadgeView;


public class ParentActivity extends FragmentActivity {

    static Context context;
    protected int curPage = 1;
    private static final int REQUEST_CODE = 0; // 请求码// 所需的全部权限
    static final String[] PERMISSIONS = new String[]{
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.MODIFY_AUDIO_SETTINGS
    };
    private PermissionsChecker mPermissionsChecker; // 权限检测器

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        context = this;
        mPermissionsChecker = new PermissionsChecker(this);
        StatusBarUtil.setColor(this, Color.parseColor("#ea452f"), 0);
//		StatusBarUtil.setColorDiff(this, Color.parseColor("#ea452f"));
    }

    @Override
    protected void onResume() {

        super.onResume();

        // 缺少权限时, 进入权限配置页面
        if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
            startPermissionsActivity();
        }
    }

    private void startPermissionsActivity() {
        PermissionsActivity.startActivityForResult(this, REQUEST_CODE, PERMISSIONS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        // 拒绝时, 关闭页面, 缺少主要权限, 无法运行
        if (requestCode == REQUEST_CODE && resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
            finish();
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
            view.setBackgroundResource(colorId <= Long.parseLong("7F000000", 16) ? com.dysen.common_res.R.color.common_yellow : colorId);
        } else {
            //非第一次点击
            //把上一次点击的 变化
            oldView.setBackgroundResource(oldColor);
            view.setBackgroundResource(colorId <= Long.parseLong("7F000000", 16) ? com.dysen.common_res.R.color.common_yellow : colorId);
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
}
