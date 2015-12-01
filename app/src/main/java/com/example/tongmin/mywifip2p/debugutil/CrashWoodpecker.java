///*
// * The MIT License (MIT)
// *
// * Copyright (c) 2015 drakeet (drakeet.me@gmail.com)
// * http://drakeet.me
// *
// * Permission is hereby granted, free of charge, to any person obtaining a copy
// * of this software and associated documentation files (the "Software"), to deal
// * in the Software without restriction, including without limitation the rights
// * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// * copies of the Software, and to permit persons to whom the Software is
// * furnished to do so, subject to the following conditions:
// *
// * The above copyright notice and this permission notice shall be included in all
// * copies or substantial portions of the Software.
// *
// * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
// * SOFTWARE.
// */
//
//package com.example.tongmin.mywifip2p.debugutil;
//
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FilenameFilter;
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.io.StringWriter;
//import java.io.Writer;
//import java.lang.Thread.UncaughtExceptionHandler;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
//
//import android.content.Context;
//import android.content.Intent;
//import android.content.pm.PackageInfo;
//import android.os.Build;
//import android.os.Environment;
//import android.os.Handler;
//import android.util.Log;
//
//
///**
// * from github
// */
//public class CrashWoodpecker implements UncaughtExceptionHandler {
//
//    private final static String TAG = "CrashWoodpecker";
//    private static boolean mForceHandleByOrigin = false;
//
//    // Default log out time, 7days.
//    private final static long LOG_OUT_TIME = 1000 * 60 * 60 * 24 * 7;
//
//    private SimpleDateFormat mFormatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
//
//    private volatile UncaughtExceptionHandler mOriginHandler;
//    private volatile UncaughtExceptionInterceptor mInterceptor;
//    private volatile boolean mCrashing = false;
//
//    private Context mContext;
//    private String mVersion;
//
//
//    /**
//     * Install CrashWoodpecker.
//     *
//     * @return CrashWoodpecker instance.
//     */
//    public static CrashWoodpecker fly() {
//        return fly(false);
//    }
//
//
//    /**
//     * Install CrashWoodpecker with forceHandleByOrigin param.
//     *
//     * @param forceHandleByOrigin whether to force original UncaughtExceptionHandler handle again,
//     * by default false.
//     * @return CrashWoodpecker instance.
//     */
//    public static CrashWoodpecker fly(boolean forceHandleByOrigin) {
//        mForceHandleByOrigin = forceHandleByOrigin;
//        return new CrashWoodpecker();
//    }
//
//
//    public void to(Context context) {
//        mContext = context;
//        try {
//            PackageInfo info =
//                    context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
//            mVersion = info.versionName + "(" + info.versionCode + ")";
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//
//    private CrashWoodpecker() {
//        UncaughtExceptionHandler originHandler =
//                Thread.currentThread().getUncaughtExceptionHandler();
//
//        // check to prevent set again
//        if (this != originHandler) {
//            mOriginHandler = originHandler;
//            Thread.currentThread().setUncaughtExceptionHandler(this);
//            Thread.setDefaultUncaughtExceptionHandler(this);
//        }
//    }
//
//
//    private boolean handleException(Throwable throwable) {
//        boolean success = saveToFile(throwable);
//        try {
//            startCatchActivity(throwable);
//            byeByeLittleWood();
//        } catch (Exception e) {
//            success = false;
//        }
//        return success;
//    }
//
//
//    private void byeByeLittleWood() {
//        android.os.Process.killProcess(android.os.Process.myPid());
//        System.exit(0);
//    }
//
//
//    @Override public void uncaughtException(Thread thread, Throwable throwable) {
//        // Don't re-enter,  avoid infinite loops if crash-handler crashes.
//        if (mCrashing) {
//            return;
//        }
//        mCrashing = true;
//
//        // pass it to interceptor's before method
//        UncaughtExceptionInterceptor interceptor = mInterceptor;
//        if (interceptor != null && interceptor.onInterceptExceptionBefore(thread, throwable)) {
//            return;
//        }
//
//        boolean isHandle = handleException(throwable);
//
//        // pass it to interceptor's after method
//        if (interceptor != null && interceptor.onInterceptExceptionAfter(thread, throwable)) {
//            return;
//        }
//
//        if ((mForceHandleByOrigin || !isHandle) && mOriginHandler != null) {
//            mOriginHandler.uncaughtException(thread, throwable);
//        }
//    }
//
//
//    /**
//     * Set uncaught exception interceptor.
//     *
//     * @param interceptor uncaught exception interceptor.
//     */
//    public void setInterceptor(UncaughtExceptionInterceptor interceptor) {
//        mInterceptor = interceptor;
//    }
//
//
//    /**
//     * Delete outmoded logs.
//     */
//    public void deleteLogs() {
//        deleteLogs(LOG_OUT_TIME);
//    }
//
//
//    /**
//     * Delete outmoded logs.
//     *
//     * @param timeout outmoded timeout.
//     */
//    public void deleteLogs(final long timeout) {
//        final File logDir = new File(getCrashDir());
//        if (logDir == null) {
//            return;
//        }
//        try {
//            final long currTime = System.currentTimeMillis();
//            File[] files = logDir.listFiles(new FilenameFilter() {
//                @Override public boolean accept(File dir, String filename) {
//                    File f = new File(dir, filename);
//                    return currTime - f.lastModified() > timeout;
//                }
//            });
//            if (files != null) {
//                for (File f : files) {
//                    FileUtil.delete(f);
//                }
//            }
//        } catch (Exception e) {
//            Log.v(TAG, "exception occurs when deleting outmoded logs", e);
//        }
//    }
//
//
//    private String getCrashDir() {
//        String rootPath = Environment.getExternalStorageDirectory().getPath();
//        return rootPath + "/Ipearl/wrong/";
//    }
//
//
//    private void startCatchActivity(Throwable throwable) {
//        String traces = getStackTrace(throwable);
//        Intent intent = new Intent();
////        intent.setClass(mContext, CatchActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        String[] strings = traces.split("\n");
//        String[] newStrings = new String[strings.length];
//        for (int i = 0; i < strings.length; i++) {
//            newStrings[i] = strings[i].trim();
//        }
////        intent.putExtra(CatchActivity.EXTRA_PACKAGE, mContext.getPackageName());
////        intent.putExtra(CatchActivity.EXTRA_CRASH_LOGS, newStrings);
////        mContext.startActivity(intent);
//    }
//
//
//    private String getStackTrace(Throwable throwable) {
//        Writer writer = new StringWriter();
//        PrintWriter printWriter = new PrintWriter(writer);
//        throwable.printStackTrace(printWriter);
//        printWriter.close();
//        return writer.toString();
//    }
//
//
//    private boolean saveToFile(Throwable throwable) {
////    	Handler handler = new Handler();
////    	Controller controller = new Controller(mContext,handler);
//    	//错误日志
////    	controller.sendUserLog(R.string.cuowurizhi);
//        String time = mFormatter.format(new Date());
//        String fileName = "Crash-" + time + ".log";
//        String crashDir = getCrashDir();
//        String crashPath = crashDir + fileName;
//
//        String androidVersion = Build.VERSION.RELEASE;
//        String deviceModel = Build.MODEL;
//        String manufacturer = Build.MANUFACTURER;
//
//        File file = new File(crashPath);
//        if (file.exists()) {
//            file.delete();
//        }
//        else {
//            try {
//                new File(crashDir).mkdirs();
//                file.createNewFile();
//            } catch (IOException e) {
//                return false;
//            }
//        }
//
//        PrintWriter writer;
//        try {
//            writer = new PrintWriter(file);
//        } catch (FileNotFoundException e) {
//            return false;
//        }
//        writer.write("Device: " + manufacturer + ", " + deviceModel + "\n");
//        writer.write("Android Version: " + androidVersion + "\n");
//        if (mVersion != null) writer.write("App Version: " + mVersion + "\n");
//        writer.write("---------------------\n\n");
//        throwable.printStackTrace(writer);
//        writer.close();
//
//        return true;
//    }
//
//
//    public interface UncaughtExceptionInterceptor {
//        /**
//         * Called before this uncaught exception be handled by {@link CrashWoodpecker}.
//         *
//         * @return true if intercepted, which means this event won't be handled
//         * by {@link CrashWoodpecker}.
//         */
//        boolean onInterceptExceptionBefore(Thread t, Throwable ex);
//
//        /**
//         * Called after this uncaught exception be handled by
//         * {@link CrashWoodpecker} (but before {@link CrashWoodpecker}'s parent).
//         *
//         * @return true if intercepted, which means this event won't be handled
//         * by {@link CrashWoodpecker}'s parent.
//         */
//        boolean onInterceptExceptionAfter(Thread t, Throwable ex);
//    }
//}
