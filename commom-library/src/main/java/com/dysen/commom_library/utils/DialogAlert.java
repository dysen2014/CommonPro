package com.dysen.commom_library.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.dysen.common_res.R;

/**
 * 提示框
 * @author Administrator
 */
public class DialogAlert extends Dialog implements OnClickListener {
	public static final int DEFAULT_STYLE = R.style.dialog;
	public Context context;
	private Button btnOK;
	private TextView tvTitle;
	private TextView tvMsg;

	private DialogOnClickListener onOkListener;

	public DialogAlert(Context context) {
		this(context, DEFAULT_STYLE);
	}

	public DialogAlert(Context context, DialogOnClickListener onclickListener) {
		this(context, DEFAULT_STYLE);
		this.onOkListener = onclickListener;
	}

	public DialogAlert(Context context, int style) {
		super(context, style);
		this.context = context;
		this.setCanceledOnTouchOutside(false);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_alert);

		findViews();
	}

	private void findViews() {
		tvTitle = (TextView) findViewById(R.id.tv_dialog_title);
		tvMsg = (TextView) findViewById(R.id.tv_dialog_msg);
		btnOK = (Button) findViewById(R.id.btn_ok);
		btnOK.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		int i = v.getId();
		if (i == R.id.btn_ok) {
			if (onOkListener != null) {
				onOkListener.onClick(v);
			}
			close();

		} else {
		}
	}
	
	/**
	 * 设置标题
	 * @param title
	 */
	public void setTitle(String title){
		tvTitle.setText(title);
	}
	
	/**
	 * 设置内容
	 * @param context
	 */
	public void setMsg(String context){
		tvMsg.setText(context);
	}

	public void close() {
		if (this != null && this.isShowing()) {
			dismiss();
		}
	}
	
	public interface DialogOnClickListener{
		void onClick(View v);
	}
}