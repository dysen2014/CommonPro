package com.dysen.commom_library.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 图片相关类
 * 
 * @author whooo
 * 
 */
public class BitmapUtils {

	public static final int TAKE_PICTURE_FROM_CAMERA = 0x01;
	public static final int TAKE_PICTURE_FROM_PHOTO_ALBUM = 0x11;
	public static final int PHOTO_ZOOM = 0x16;// 22
	public static final int PHOTO_DELETE = 0x15;// 22

	public static final String photoPath = Environment.getExternalStorageDirectory().getPath() + "/DCIM/Camera";

	public static final Uri CROP_TMP_FILE_URI = Uri.fromFile(new File(photoPath + "/_CROP_TMP_.jpg"));

	private static Uri lastGenerateUri = null;

	public static final String getPhotoPath() {
		File file = new File(photoPath);
		if (!file.exists()) {
			file.mkdirs();
		}
		return file.getAbsolutePath();
	}

	/** 调用相机 */
	public static void getPictureFromCamera(Activity activity) {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		File file = new File(getPhotoPath(), new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".jpg");
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
		lastGenerateUri = Uri.fromFile(file);

		activity.startActivityForResult(intent, TAKE_PICTURE_FROM_CAMERA);
	}

	public static Uri getLastGenerateUri() {
		return lastGenerateUri;
	}

	/** 调用相册 */
	public static void getPictureFromPhotoAlbum(Activity activity) {
		Intent intent = new Intent(Intent.ACTION_PICK, null);
		intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
		activity.startActivityForResult(intent, TAKE_PICTURE_FROM_PHOTO_ALBUM);
	}

	/** 删除图片按钮 */
	public static void deletePic(Activity activity) {
		// Intent intent=new Intent();
		// activity.startActivityForResult(intent, Sy.PHOTO_DELETE);
		// activity.startActivityForResult(intent, Constants.PHOTO_DELETE);
		Message msg = new Message();
		Bundle data = new Bundle();
		data.putString("value", "" + PHOTO_DELETE);
		msg.setData(data);
//		if (activity instanceof ParentActivity) {
//			((ParentActivity) activity).handler.sendMessage(msg);
//			EventBus.getDefault().post(new DeletePhotoEvent());
//		}
	}

	/**
	 * 裁剪图片方法实现
	 * 
	 */
	public static void startPhotoZoom(Activity activity, Uri fromFile, int aspectX, int aspectY, int width,
			int height) {
	
		Log.i("info", "activity=" + activity.toString() + ",fromFile:" + fromFile);
		
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(fromFile, "image/*");
		intent.putExtra("crop", "true");

		intent.putExtra(MediaStore.EXTRA_OUTPUT, CROP_TMP_FILE_URI);

		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("return-data", false);

		intent.putExtra("aspectX", aspectX);
		intent.putExtra("aspectY", aspectY);
		intent.putExtra("outputX", width);
		intent.putExtra("outputY", height);

		activity.startActivityForResult(intent, PHOTO_ZOOM);
	}

	public static void startPhotoZoom(Activity activity, Uri fromFile) {
		startPhotoZoom(activity, fromFile, 4, 3, 1024, 768);
	}

	public static Bitmap toRoundBitmap(Context context, Bitmap bitmap) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		float roundPx;
		float left, top, right, bottom;
		if (width <= height) {
			roundPx = width / 10;
			top = 0;
			left = 0;
			bottom = width;
			right = width;
			height = width;

		} else {
			roundPx = height / 10;
			float clip = (width - height) / 10;
			left = clip;
			right = width - clip;
			top = 0;
			bottom = height;
			width = height;
		}

		Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final Paint paint = new Paint();
		final Rect src = new Rect((int) left, (int) top, (int) right, (int) bottom);
		final RectF rectF = new RectF(src);

		paint.setAntiAlias(true);

		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(Color.WHITE);
		paint.setStrokeWidth(4);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, src, src, paint);

		return output;
	}

	/**
	 * 图片的质量压缩方法
	 * 
	 * @param image
	 * @return
	 */
	public static byte[] compressImage(Bitmap image, int sizeLimit) {
		int options = 100;
		ByteArrayOutputStream baos = new ByteArrayOutputStream(sizeLimit * 2);
		do {
			baos.reset();
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);
			options -= 5;
		} while (baos.size() > sizeLimit);

		return baos.toByteArray();
	}

	/**
	 * 获取图片大小
	 * 
	 * @param image
	 * @return 返回KB
	 */
	public static long getBitmapsize(Bitmap image) {

		return image.getRowBytes() * image.getHeight() / 1024;

	}

	/**
	 * 将图片转换为流文件
	 * 
	 * @param bitmap
	 * @return
	 */
	public static String getBitmapByte(Bitmap bitmap) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
		String photo = "";
		try {
			byte[] buffer = out.toByteArray();
			photo = Base64.encodeToString(buffer, 0, buffer.length, Base64.DEFAULT);
			out.flush();
			out.close();
		} catch (IOException e) {

		}
		return photo;
	}

	/**
	 * 圆形图片
	 * 
	 * @param bitmap
	 * @return
	 */
	public static Bitmap toCircleBitmap(Bitmap bitmap) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		float roundPx;
		float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
		if (width <= height) {
			roundPx = width / 2;
			top = 0;
			bottom = width;
			left = 0;
			right = width;
			height = width;
			dst_left = 0;
			dst_top = 0;
			dst_right = width;
			dst_bottom = width;
		} else {
			roundPx = height / 2;
			float clip = (width - height) / 2;
			left = clip;
			right = width - clip;
			top = 0;
			bottom = height;
			width = height;
			dst_left = 0;
			dst_top = 0;
			dst_right = height;
			dst_bottom = height;
		}
		Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect src = new Rect((int) left, (int) top, (int) right, (int) bottom);
		final Rect dst = new Rect((int) dst_left, (int) dst_top, (int) dst_right, (int) dst_bottom);
		final RectF rectF = new RectF(dst);
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, src, dst, paint);
		return output;
	}
}
