package com.hisense.storemode.utils;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.os.SystemClock;

import com.hisense.hianidraw.cache.SeriesUtils;
import com.hisense.storemode.StoreModeApplication;
import com.hisense.storemode.manager.StoreModeManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * <h3>全局捕获异常</h3>
 * <br>
 * 当程序发生Uncaught异常的时候,有该类来接管程序,并记录错误日志
 *
 * @author tianpengsheng
 */
public class CrashHandler implements UncaughtExceptionHandler {
    private static final long QUICK_CRASH_ELAPSE = 10 * 1000;
    private static final int MAX_CRASH_COUNT = 3;

    private static String TAG = "CrashHandler";
    // 系统默认的UncaughtException处理类
    private UncaughtExceptionHandler mDefaultHandler;

    // 用来存储设备信息和异常信息
    private Map<String, String> infos = new HashMap<>();

    // 用于格式化日期,作为日志文件名的一部分
    private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd E a HH:mm:ss");


    private volatile static CrashHandler sInstance;

    private CrashHandler() {
    }

    /**
     * 获取CrashHandler实例 ,单例模式
     */
    public static CrashHandler getInstance() {    //对获取实例的方法进行同步
        if (sInstance == null) {
            synchronized (CrashHandler.class) {
                if (sInstance == null)
                    sInstance = new CrashHandler();
            }
        }
        return sInstance;
    }

    /**
     * 初始化
     */
    public void init() {
        // 获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        // 设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 当UncaughtException发生时会转入该函数来处理
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        LogUtils.d(TAG, "uncaughtException()  method invoke!");
        SeriesUtils.modifyGoogleServiceSwitch(true);
        boolean isfastcrash = isFastCrash();
        if (isfastcrash) {
            LogUtils.d(TAG, "uncaughtException()  method invoke  isfastcrash:" + isfastcrash);
            //快速连续崩溃，那么不再重启
            mDefaultHandler.uncaughtException(thread, ex);
            return;
        }
        if (!handleException(ex) && mDefaultHandler != null) {
            // 如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            SystemClock.sleep(1000);
            // 退出程序
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     *
     * @param ex
     * @return true:如果处理了该异常信息; 否则返回false.
     */
    private boolean handleException(Throwable ex) {
        if (ex == null)
            return false;

        try {
            // 使用Toast来显示异常信息
//            new Thread() {
//                @Override
//                public void run() {
//                    Looper.prepare();
//                    Toast.makeText(applicationContext, "很抱歉,程序出现异常,即将重启.",
//                            Toast.LENGTH_LONG).show();
//                    Looper.loop();
//                }
//            }.start();
            // 收集设备参数信息
            collectDeviceInfo();
            // 保存日志文件
            saveCrashInfoFile(ex);
            //reboot
//            Intent i = StoreModeApplication.sContext.getPackageManager()
//                    .getLaunchIntentForPackage
//                            (StoreModeApplication.sContext.getPackageName());
//            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            StoreModeApplication.sContext.startActivity(i);
//            SystemClock.sleep(1000);

//            PlayManager.getInstance().startPlay();
//            BackgroundTaskManager.getInstance().stopWorker();
            boolean isStoreMode = ConstantConfig.isStoreMode();
            LogUtils.d(TAG, "handleException()  method invoke!   isStoreMode:" + isStoreMode);
//            if (!PlayManager.getInstance().isBuildInVideoPlaying() && isStoreMode) {
            if (isStoreMode) {
                LogUtils.d(TAG, "handleException()  reboot  storemode app");
                PreferenceUtils.getInstance().save(ConstantConfig.RESET_PALY_POLICY, true);
                PreferenceUtils.getInstance().save(ConstantConfig.CRASH_JUST_NOW, true);
                StoreModeManager.getInstance().start();
            }
        } catch (Exception e) {
            LogUtils.d(TAG, "handleException()  invoke  happen  exception");
            e.printStackTrace();
        }

        return true;
    }

    //avoid fast many times to reboot.limit three times
    private boolean isFastCrash() {
        if (StoreModeApplication.sContext == null) {
            return false;
        }
        long preTime = (long) PreferenceUtils.getInstance().get(ConstantConfig.FAST_CRASH_CURRENT_TIME, System.currentTimeMillis());
        long currentTime = System.currentTimeMillis();

        PreferenceUtils.getInstance().save(ConstantConfig.FAST_CRASH_CURRENT_TIME, currentTime);

        final long elapsedTime = currentTime - preTime;

        if (elapsedTime < QUICK_CRASH_ELAPSE) {
            int crashcount = (int) PreferenceUtils.getInstance().get(ConstantConfig.FAST_CRASH_COUNT, 0) + 1;
            if (crashcount >= MAX_CRASH_COUNT) {
                return true;
            } else {
                PreferenceUtils.getInstance().save(ConstantConfig.FAST_CRASH_COUNT, crashcount);
            }
        } else {
            PreferenceUtils.getInstance().delete(ConstantConfig.FAST_CRASH_COUNT);
        }
        return false;
    }

    /**
     * 收集设备参数信息
     */
    public void collectDeviceInfo() {
        try {
            PackageManager pm = StoreModeApplication.sContext.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(StoreModeApplication.sContext.getPackageName(),
                    PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName + "";
                String versionCode = pi.versionCode + "";
                infos.put("versionName", versionName);
                infos.put("versionCode", versionCode);
            }
        } catch (NameNotFoundException e) {
            LogUtils.e(TAG, "an error occured when collect package info", e);
        }
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                infos.put(field.getName(), field.get(null).toString());
            } catch (Exception e) {
                LogUtils.e(TAG, "an error occured when collect crash info", e);
            }
        }
    }


    public String printDeviceMsg() {
        collectDeviceInfo();

        StringBuffer sb = new StringBuffer();

        String date = DateUtil.getTodayTimeFormat();
        sb.append("\r\n" + date + "\n");
        for (Map.Entry<String, String> entry : infos.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key).append("=").append(value).append("\n");
        }


        return sb.toString();
    }

    /**
     * 保存错误信息到文件中
     *
     * @return 返回文件名称, 便于将文件传送到服务器
     * @throws Exception
     */
    private String saveCrashInfoFile(Throwable ex) throws Exception {
        StringBuffer sb = new StringBuffer();
        try {
            String date = DateUtil.getTodayTimeFormat();
            sb.append("\r\n" + date + "\n");
            for (Map.Entry<String, String> entry : infos.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                sb.append(key).append("=").append(value).append("\n");
            }

            Writer writer = new StringWriter();
            PrintWriter printWriter = new PrintWriter(writer);
            ex.printStackTrace(printWriter);
            Throwable cause = ex.getCause();
            while (cause != null) {
                cause.printStackTrace(printWriter);
                cause = cause.getCause();
            }
            printWriter.flush();
            printWriter.close();
            String result = writer.toString();
            sb.append(result);
            return writeFile(sb.toString());
        } catch (Exception e) {
            LogUtils.e(TAG, "an error occured while writing file...", e);
            sb.append("an error occured while writing file...\r\n");
            writeFile(sb.toString());
        }
        return null;
    }

    private String writeFile(String sb) throws Exception {
        String time = formatter.format(new Date());
        String fileName = "crash-" + time + ".log";
        if (FileUtil.hasSdcard()) {
            String path = getGlobalpath();
            File dir = new File(path);
            if (!dir.exists())
                dir.mkdirs();

            FileOutputStream fos = new FileOutputStream(path + fileName, true);
            fos.write(sb.getBytes());
            fos.flush();
            fos.close();
        }
        return fileName;
    }

    public static String getGlobalpath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator + "crash" + File.separator + "storeMode" + File.separator;
    }


}
