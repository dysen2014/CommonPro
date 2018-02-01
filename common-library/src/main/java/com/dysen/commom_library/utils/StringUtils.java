package com.dysen.commom_library.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by dysen on 2017/6/30.
 */

public class StringUtils {

    /**
     * 去除字符串里的特殊字符
     * @param str
     * @return
     */
    public static String replaceBlank(String str){
        String dest = "";
        if (str != null){
            Pattern pattern = Pattern.compile("\\s*|\t|\r|\n");
            Matcher matcher = pattern.matcher(str);
            dest = matcher.replaceAll("");
        }
        return dest;
    }
}
