package com.example.tongmin.mywifip2p.debugutil;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.SyncStateContract;
import android.util.Log;
import android.widget.Toast;



public class DebugFile {
    /**
     * * 此类用来到后期调试代码使用
     * 程序出现的异常。和调试时写入的信息
     * 1.当在异常中使用的时候需要在文件中出现异常的时间和出现异常时在代码中的位置
     * 2.在调试时出现用户填写的信息外，还需要在文件中写入出现的时间和出现在代码中的位置
     */
    public static final boolean DEBUG = true;
    public static String path;
    public static String name = "XHCDEBUG.txt";
    public static String myPath = File.separator + "Debug" + File.separator;
    public static Context mContext;
    private static String appName;
    private static DebugFile object = new DebugFile();
    private static final String FileFormat = "GBK";
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    private DebugFile() {
    }

    ;

    public static DebugFile getInstance(Context context) {

        mContext = context;

        appName = AppUtils.getAppName(context);
        if (object != null) {

            object.createFileAndDirectory(name);
            return object;
        }
        return null;
    }


    private boolean isUseSdcard() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return true;
        }
        return false;
    }

    public String getBasePath(){
        if (isUseSdcard()) {
            File sdCardDir = Environment.getExternalStorageDirectory();
            return sdCardDir.getAbsolutePath() + myPath;
        }
        return null;
    }

    /*可以创建多层目录但是不能创建文件*/
    private void createDirectory() {
        if (isUseSdcard()) {
            File sdCardDir = Environment.getExternalStorageDirectory();
            File file = new File(getBasePath(), appName);
            path = file.getAbsolutePath();
            if (!file.exists()) {
                try {
                        /*只能创建目录*/
                    file.mkdirs();
                    Toast.makeText(mContext, path, Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    Log.e("xhc", " exception " + e.getMessage());
                    Toast.makeText(mContext, " exception " + e.getMessage(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        } else {
            Toast.makeText(mContext, "sdcard有问题", Toast.LENGTH_LONG).show();
        }
    }

    /*创建文件*/
    private void createFile(String name) {
        File file = new File(path + "/" + name);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /*只需要给出文件路径 文件名同一为Debug.txt*/
    public void createFileAndDirectory(String name) {
        createDirectory();
        createFile(name);
    }

    /*往文件中写入调试信息*/
    public void writeLog(String where, String content) {
        if (!DEBUG) return;
        String detail = Constant.DEBUGLINE+"\r\n time: ";
        detail +=sdf.format( new Date() )+ " \r\n POSITION:";
        detail += where + " \r\n CONTENT:";
        detail += content + "\r\n";
        detail += Constant.DIVIDER+"\r\n";
        File file = new File(path + "/" + name);
        FileOutputStream fileOutputStream = null;

        try {
            fileOutputStream = new FileOutputStream(file, true);
            byte[] buffer = detail.getBytes(FileFormat);
			/*是重这个数组中的第几个位置开始写*/
            fileOutputStream.write(buffer, 0, buffer.length);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (Exception e) {
                }

            }
        }
    }

    /*当用户捕捉的异常写在这个方法中*/
    public void writeException(String where, String content) {
        if (!DEBUG) return;
        String detail = Constant.EXCEPTION+" \r\n time:";
        detail += sdf.format( new Date() )+ " \r\n POSITION:";
        detail += where + " \r\n EXCEPTION:";
        detail += content + "\r\n";
        detail += Constant.DIVIDER+"\r\n";
        File file = new File(path + "/" + name);
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file, true);
            byte[] buffer = detail.getBytes(FileFormat);
				/*是重这个数组中的第几个位置开始写*/
            fileOutputStream.write(buffer, 0, buffer.length);
        } catch (Exception e) {
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (Exception e) {
                }

            }
        }
    }

    /**
     * 读取文件中的数据
     *
     * @param fileName
     * @return
     * @throws IOException
     */
    public String readFileSdcardFile(String fileName) throws IOException {
        String res = "";
        FileInputStream fin = null;
        ByteArrayOutputStream byteArrayOutputStream = null;
        try {
            fin = new FileInputStream(fileName);
            byteArrayOutputStream = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            int i = 0;
            while ((i = fin.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer,0,i);
            }
            res = byteArrayOutputStream.toString("GBK");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fin != null) {
                fin.close();
            }
            if(byteArrayOutputStream != null){
                byteArrayOutputStream.close();
            }
        }

        return res;
    }

    /**
     * 将文件全部列举出来
     * @param path
     * @return
     */
    public File[] getFileList(String path) {
        File[] listFile = null;
        if(isUseSdcard()){
            File file = new File(path);
            if(file.isDirectory()){
                listFile = file.listFiles();
            }
        }

        return listFile;
    }
}
