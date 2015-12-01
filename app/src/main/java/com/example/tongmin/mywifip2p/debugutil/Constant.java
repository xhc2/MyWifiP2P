package com.example.tongmin.mywifip2p.debugutil;

import java.io.File;

/**
 * Created by Administrator on 2015/8/23.
 */
public class Constant {
    public static final String DEBUGLINE = "*******************************Debug*****************************";
    public static final String EXCEPTION = "*******************************EXCEPTION******************************";
    public static final String DIVIDER = "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@";
    public static final String ERROR = "*******************************ERROR******************************";
    public static String myPath = File.separator + "Debug" + File.separator;
    public static String error_log_path = android.os.Environment
            .getExternalStorageDirectory() + "" + myPath;
}
