package com.dysen.commom_library.utils;

import android.view.View;

/**
 * Created by dysen on 2017/7/13.
 */

public interface OnItemClickCallback<T> {
	// 点击事件
	void onClick(View view, T info);
	// 长按事件
	void onLongClick(View view, T info);
}

