package com.example.tongmin.mywifip2p.debugutil;

import android.os.Environment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by TongMin on 2015/8/27.
 */
public class Utils {
    /**
     * 创建文件夹
     *
     * @param dirName
     */


    public static void MakeDir(String dirName)
    {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
        {
            File destDir = new File(dirName);
            if (!destDir.exists())
            {
                destDir.mkdirs();
            }
        }
    }

    public static String getCurTime()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }
}
