package com.dysen.commom_library.base;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dysen.commom_library.R;
import com.dysen.commom_library.utils.DialogAlert;
import com.dysen.commom_library.utils.FormatUtil;
import com.dysen.commom_library.views.StatusBarUtil;

import q.rorbin.badgeview.QBadgeView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ParentFragment extends Fragment {


    private LinearLayoutManager mLayoutManager;

    public ParentFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        TextView textView = new TextView(getActivity());

        StatusBarUtil.setColor(getActivity(), Color.parseColor("#ea452f"), 0);
        return textView;
    }

    protected static String getTypeName(String customerType) {

        String name = "";
        if (customerType.equals("010"))
            name = "公";
        if (customerType.equals("030"))
            name = "个";
        if (customerType.equals("040"))
            name = "农";

        return name;
    }

    /**
     * 绑定 View
     */
    @SuppressWarnings("unchecked")
    protected <T extends View> T bindView(View view, int id) {
        return (T) view.findViewById(id);
    }

    private static View oldView;
    static int oldColor;

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

    /**
     * Toast消息提示
     *
     * @param text
     */
    public void toast(String text) {
        // 2s内只提示一次
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastTime > 2000) {
            lastTime = currentTime;
            Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
        }
    }

    protected boolean checkObjValid(Object obj) {
        if (obj != null)
            return true;
        else
            return false;
    }

//    protected void backActivity(View v) {
//        getActivity().finish();
//    }

    protected RecyclerView setRecyclerView(RecyclerView recyclerView) {

        //设置固定大小
        recyclerView.setHasFixedSize(true);
        //创建线性布局
        mLayoutManager = new LinearLayoutManager(getActivity());
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
        mLayoutManager = new LinearLayoutManager(getActivity());
        //垂直方向 OrientationHelper.HORIZONTAL 0 OrientationHelper.VERTICAL 1
        mLayoutManager.setOrientation(orientation);
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
    protected DialogAlert ShowDialog(Context context, String str) {
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

    public void setBadgeView(View view, int count) {

        QBadgeView badge = new QBadgeView(getContext());
        badge.bindTarget(view);

        badge.setBadgeTextSize(16, true);
        badge.setBadgeNumber(count);
    }

    public void setBadgeView(View view, String text) {

        QBadgeView badge = new QBadgeView(getContext());
        badge.bindTarget(view);
        badge.setBadgeTextSize(16, true);
        if (!text.equals(""))
            if (FormatUtil.isNumeric(text))
                badge.setBadgeNumber(Integer.parseInt(text));
            else
                badge.setBadgeText(text);
        else return;
    }


    protected void syncScroll(final RecyclerView leftList,  final RecyclerView rightList) {
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
