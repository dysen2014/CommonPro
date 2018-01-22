package com.dysen.commom_library.utils;

import android.os.Environment;

import java.io.File;

/**
 * Created by dysen on 2017/9/14.
 */

public class FileUtils {

    /**
     * 获取SD卡的路径
     * @param newDir 新建的文件夹
     * @return
     */
    public static File getSDdir(String newDir) {
        String dirPath = Environment.getExternalStorageDirectory() + File.separator + newDir + File.separator;
        //建立文件下载的目录
        final File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }
}
