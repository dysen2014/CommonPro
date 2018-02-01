package com.dysen.commom_library.views;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dysen.commom_library.R;

import java.util.List;

/**
 * Created by dy on 2016-08-26.
 */
public class ViewUtils {

    public ViewUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     *  设置view的左上右下图标
     * @param context
     * @param view
     * @param drawableId
     * @param orientationIndex 0 left, 1 top, 2 right, 3 bottom
     */
    public static void setViewOrientationBg(Context context, View view, @DrawableRes @ColorRes int drawableId, int orientationIndex) {
        Drawable rightDrawable = context.getResources().getDrawable(drawableId);
        rightDrawable.setBounds(0, 0, rightDrawable.getMinimumWidth(), rightDrawable.getMinimumHeight());
        if (view instanceof TextView) {
            switch (orientationIndex) {
                case 0:
                    ((TextView) view).setCompoundDrawables(rightDrawable, null, null, null);
                    break;
                case 1:
                    ((TextView) view).setCompoundDrawables(null, rightDrawable, null, null);
                    break;
                case 2:
                    ((TextView) view).setCompoundDrawables(null, null, rightDrawable, null);
                    break;
                case 3:
                    ((TextView) view).setCompoundDrawables(null, null, null, rightDrawable);
                    break;
            }
        } else if (view instanceof Button) {
            switch (orientationIndex) {
                case 0:
                    ((Button) view).setCompoundDrawables(rightDrawable, null, null, null);
                    break;
                case 1:
                    ((Button) view).setCompoundDrawables(null, rightDrawable, null, null);
                    break;
                case 2:
                    ((Button) view).setCompoundDrawables(null, null, rightDrawable, null);
                    break;
                case 3:
                    ((Button) view).setCompoundDrawables(null, null, null, rightDrawable);
                    break;
            }
        } else if (view instanceof EditText) {
            switch (orientationIndex) {
                case 0:
                    ((EditText) view).setCompoundDrawables(rightDrawable, null, null, null);
                    break;
                case 1:
                    ((EditText) view).setCompoundDrawables(null, rightDrawable, null, null);
                    break;
                case 2:
                    ((EditText) view).setCompoundDrawables(null, null, rightDrawable, null);
                    break;
                case 3:
                    ((EditText) view).setCompoundDrawables(null, null, null, rightDrawable);
                    break;
            }
        } else {

        }
    }

    /**
     * 设置 TextView, Button, EditText等的内容
     *
     * @param context
     * @param value
     * @param object
     */
    public static void setText(Context context, String value, Object object) {

        if (object instanceof TextView) {
            ((TextView) object).setText(value);
        } else if (object instanceof Button) {
            ((Button) object).setText(value);
        } else if (object instanceof EditText) {
            ((EditText) object).setText(value);
        } else {

        }
    }

    public static void setText(String value, Object object) {

        if (object instanceof TextView) {
            ((TextView) object).setText(value);
        } else if (object instanceof Button) {
            ((Button) object).setText(value);
        } else if (object instanceof EditText) {
            ((EditText) object).setText(value);
        } else {

        }
    }

    public static void setHint(String value, Object object) {

        if (object instanceof TextView) {
            ((TextView) object).setHint(value);
        } else if (object instanceof Button) {
            ((Button) object).setHint(value);
        } else if (object instanceof EditText) {
            ((EditText) object).setHint(value);
        } else {

        }
    }

    /**
     * 获取 TextView, Button, EditText等的内容
     *
     * @param object
     * @return
     */
    public static String getText(Object object) {

        if (object instanceof TextView) {
            return ((TextView) object).getText().toString();
        } else if (object instanceof Button) {
            return ((Button) object).getText().toString();
        } else if (object instanceof EditText) {
            return ((EditText) object).getText().toString();
        } else {

        }
        return "";
    }

    public static String getHint(Object object) {

        if (object instanceof TextView) {
            return ((TextView) object).getHint().toString();
        } else if (object instanceof Button) {
            return ((Button) object).getHint().toString();
        } else if (object instanceof EditText) {
            return ((EditText) object).getHint().toString();
        } else {

        }
        return "";
    }

    /**
     * 设置text 空值 hint 显示
     * @param viewList
     * @param valueList
     */
    public static void setViewsValue(List<View> viewList, List<String> valueList) {
        for (int i = 0; i < viewList.size(); i++) {
            ViewUtils.setText("", viewList.get(i));
            ViewUtils.setHint(valueList.get(i), viewList.get(i));
        }
    }

    public static void setViewEnable(Context context, Object object , boolean flag) {

        if (object instanceof TextView) {
            if (flag){
                ((TextView) object).setBackgroundResource(R.mipmap.btn_bg_red);
                ((TextView) object).setTextColor(context.getResources().getColor(R.color.white));
            } else{
                ((TextView) object).setBackgroundResource(R.mipmap.btn_bg_gray);
                ((TextView) object).setTextColor(context.getResources().getColor(R.color.black));
            }
            ((TextView) object).setEnabled(flag);
        } else if (object instanceof Button) {
            if (flag){
                ((Button) object).setBackgroundResource(R.mipmap.btn_bg_red);
                ((Button) object).setTextColor(context.getResources().getColor(R.color.white));
            } else {
                ((Button) object).setBackgroundResource(R.mipmap.btn_bg_gray);
                ((Button) object).setTextColor(context.getResources().getColor(R.color.black));
            }
            ((Button) object).setEnabled(flag);
        } else if (object instanceof EditText) {
            if (flag){
                ((EditText) object).setBackgroundResource(R.mipmap.btn_bg_red);
                ((EditText) object).setTextColor(context.getResources().getColor(R.color.white));
            } else{
                ((EditText) object).setBackgroundResource(R.mipmap.btn_bg_gray);
                ((EditText) object).setTextColor(context.getResources().getColor(R.color.black));
            }
            ((EditText) object).setEnabled(flag);
        } else {

        }
    }
}

