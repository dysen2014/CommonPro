package com.dysen.commom_library.views;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
}

