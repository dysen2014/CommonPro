package com.dysen.commom_library.utils;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.dysen.common_res.R;
import com.dysen.common_res.common.views.uber.UberProgressView;

/**
 * Created by dysen on 11/28/2017.
 */
public class ProgressDialogFragment extends DialogFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE); //消除Title区域
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));  //将背景变为透明
        setCancelable(false);  //点击外部不可取消
        View root = inflater.inflate(R.layout.fragment_progress_dialog, container);
        initView(root);
        return root;
    }

    public static void initView(View root) {
        TextView tvPgsTxt = (TextView)root.findViewById(R.id.tv_pgs_txt);
        UberProgressView uberProgressView = (UberProgressView) root.findViewById(R.id.uber_pgsview);

    }

    public static ProgressDialogFragment newInstance() {
        return new ProgressDialogFragment();
    }
}
