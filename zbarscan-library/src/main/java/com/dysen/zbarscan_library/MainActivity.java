/*
 * Copyright © Yan Zhenjie
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.dysen.zbarscan_library;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.dysen.zbarscan_library.camera.CameraPreview;
import com.dysen.zbarscan_library.camera.ScanCallback;
import com.dysen.zbarscan_library.common.LogUtils;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import java.util.List;


/**
 * @author dysen 扫码 工具类
 * @code 99
 */
public class MainActivity extends AppCompatActivity {

	private RelativeLayout mScanCropView;
	private ImageView mScanLine;
	private ValueAnimator mScanAnimator;
	private CameraPreview mPreviewView;

	public static int code = 99;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Toolbar
//		Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
//		setSupportActionBar(mToolbar);

		mPreviewView = (CameraPreview)this.findViewById(R.id.capture_preview);
		mScanCropView = (RelativeLayout) findViewById(R.id.capture_crop_view);
		mScanLine = (ImageView) findViewById(R.id.capture_scan_line);

		LogUtils.d("mPreviewView==="+mPreviewView);
		mPreviewView.setScanCallback(resultCallback);

		findViewById(R.id.capture_restart_scan).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startScanUnKnowPermission();
			}
		});
	}

	/**
	 * Accept scan result.
	 */
	private ScanCallback resultCallback = new ScanCallback() {
		@Override
		public void onScanResult(String result) {
			stopScan();

			Log.d("dysen", "result==="+result);
//			Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show();
			// 添加震动效果，提示用户扫描完成
//			Vibrator mVibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
//			mVibrator.vibrate(200);
//			myResult(result);
		}
	};

	@Override
	protected void onResume() {
		super.onResume();
		if (mScanAnimator != null) {
			startScanUnKnowPermission();
		}
	}

	@Override
	public void onPause() {
		// Must be called here, otherwise the camera should not be released properly.
		stopScan();
		super.onPause();
	}

	/**
	 * Do not have permission to request for permission and start scanning.
	 */
	private void startScanUnKnowPermission() {
		AndPermission.with(this)
				.permission(Manifest.permission.CAMERA)
				.callback(new PermissionListener() {
					@Override
					public void onSucceed(int requestCode, @NonNull List<String> grantPermissions) {
						startScanWithPermission();
					}

					@Override
					public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {
						AndPermission.defaultSettingDialog(MainActivity.this).show();
					}
				})
				.start();
	}

	/**
	 * There is a camera when the direct scan.
	 */
	private void startScanWithPermission() {
		if (mPreviewView.start()) {
			mScanAnimator.start();
		} else {
			new AlertDialog.Builder(this)
					.setTitle(R.string.camera_failure)
					.setMessage(R.string.camera_hint)
					.setCancelable(false)
					.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							finish();
						}
					})
					.show();
		}
	}

	/**
	 * Stop scan.
	 */
	private void stopScan() {
		mScanAnimator.cancel();
		mPreviewView.stop();
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if (mScanAnimator == null) {
			int height = mScanCropView.getMeasuredHeight() - 25;
			mScanAnimator = ObjectAnimator.ofFloat(mScanLine, "translationY", 0F, height).setDuration(3000);
			mScanAnimator.setInterpolator(new LinearInterpolator());
			mScanAnimator.setRepeatCount(ValueAnimator.INFINITE);
			mScanAnimator.setRepeatMode(ValueAnimator.REVERSE);

			startScanUnKnowPermission();
		}
	}

	void myResult(String result) {

		try {// 过滤 含字符的扫码结果

			// 数据是使用Intent返回
			Intent intent = new Intent();
			// 把返回数据存入Intent
			intent.putExtra("result", result);
			// 设置返回数据
			MainActivity.this.setResult(code, intent);
			// 关闭Activity
			MainActivity.this.finish();
		} catch (Exception e) {
			// return false;// 如果抛出异常，返回False
//			tvHintTitle.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			myResult("");
		}
		return false;
	}
}

