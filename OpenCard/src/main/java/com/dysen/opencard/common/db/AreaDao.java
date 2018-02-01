package com.dysen.opencard.common.db;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.dysen.opencard.common.bean.AreaBean;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class AreaDao {
	public AreaDao(Context context) {
		database = openDatabase(context);
	}

	// 数据库存储路径
	public static String filePath = "/hbcrm_dbank/db/Address.sqlite";
	public static String pathStr = "/hbcrm_dbank/db";

	SQLiteDatabase database;

	public SQLiteDatabase openDatabase(Context context) {
		File pathDb = Environment.getExternalStorageDirectory();
		System.out.println("filePath:" + filePath);
		File jhPath = new File(pathDb, filePath);
		// 查看数据库文件是否存在
		if (jhPath.exists()) {
			// 存在则直接返回打开的数据库
			return SQLiteDatabase.openOrCreateDatabase(jhPath, null);
		} else {
			// 不存在先创建文件夹
			File path = new File(pathDb, pathStr);
			if (path.mkdirs()) {
				System.out.println("创建成功");
			} else {
				System.out.println("创建失败");
			}
			
			try {
				// 得到资源
				AssetManager am = context.getAssets();
				// 得到数据库的输入流
				InputStream is = am.open("Address.sqlite");
				// 用输出流写到SDcard上面
				FileOutputStream fos = new FileOutputStream(jhPath);
				// 创建byte数组 用于1KB写一次
				byte[] buffer = new byte[1024];
				int count = 0;
				while ((count = is.read(buffer)) > 0) {
					fos.write(buffer, 0, count);
				}
				// 最后关闭就可以了
				fos.flush();
				fos.close();
				is.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
			// 如果没有这个数据库 我们已经把他写到SD卡上了，然后在执行一次这个方法 就可以返回数据库了
			return openDatabase(context);
		}
	}

	/**
	 * 
	 * @param upAreaCode
	 * @return
	 */
	public List<AreaBean> getAllMessage(String upAreaCode) {
		List<AreaBean> list = null;
		// database=helper.getReadableDatabase();
		String[] columns = { "ADDRESS_ID", "ADDRESS_NAME" };
		String selection = "SUPERID=?";
		String[] selectionArgs = { upAreaCode };
		Cursor cursor = database.query("IFS_ADDRESS", columns, selection,
				selectionArgs, null, null, "ADDRESS_ID ASC");
		list = new ArrayList<AreaBean>();
		if (cursor != null) {
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
				AreaBean m = new AreaBean();
				m.setId(cursor.getString(cursor.getColumnIndex("ADDRESS_ID")));
				m.setName(cursor.getString(cursor.getColumnIndex("ADDRESS_NAME")));
				list.add(m);
			}
			cursor.close();
		}
		return list;
	}

	// 根据码值查询地址
	public String getAreaName(String Code) {
		String retuenInfo = "";
		// Cursor cursor = database.rawQuery(
		// "select mapping_code from IFS_ADDRESS WHERE ADDRESS_ID  = '"
		// + Code + "'", null);
		String[] selectionArgs = { Code };
		String[] columns_mapping_code = { "MAPPING_CODE"};
		String[] columns_address_name = { "ADDRESS_NAME"};
		
		
		Cursor cursor = database.query("IFS_ADDRESS", columns_mapping_code,
				"ADDRESS_ID=?", selectionArgs, null, null, null);
		if (cursor != null && cursor.moveToFirst()){
//			cursor.moveToNext();
			String mapping_code = cursor.getString(cursor
					.getColumnIndex("MAPPING_CODE"));
			String[] theAreaCode = mapping_code.split(",");

			for (int i = 0; i < theAreaCode.length; i++) {
				
				String[] theArgs = { theAreaCode[i] };
				
				Cursor selectInfo =database.query("IFS_ADDRESS", columns_address_name,
						"ADDRESS_ID=?", theArgs, null, null, null);
				if (selectInfo != null && selectInfo.moveToFirst()) {
					retuenInfo += selectInfo.getString(selectInfo.getColumnIndex("ADDRESS_NAME"));
					selectInfo.moveToNext();
				}
			}
            cursor.close();
		}

		return retuenInfo;
	}

	public void closeDataBase() {
		database.close();
	}
}
