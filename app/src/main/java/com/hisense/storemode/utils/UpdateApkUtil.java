package com.hisense.storemode.utils;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageInstaller;
import android.net.Uri;
import android.util.Log;

import com.hisense.storemode.StoreModeApplication;
import com.hisense.storemode.receiver.InstallResultReceiver;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by cuihuihui on 2019年06月24日 17时24分.
 */
public class UpdateApkUtil {
    private static final String TAG = "UpdateApkUtil";
    private static final String TYPE = "application/vnd.android.package-archive";

    //not silense install apk
    public void Upgrade(String path) {
        try {
            Log.d(TAG, "path =" + path);
            File apkFile = new File(path);
            Log.d(TAG, "is apkFile .exist =" + apkFile.exists());
            if (!apkFile.exists()) {
                Log.d(TAG, "the apk file deos not exist");
                return;
            }
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.fromFile(apkFile), TYPE);
            Log.d(TAG, "install apk startActivity ");
            StoreModeApplication.sContext.startActivity(intent);
        } catch (Error error) {
            Log.d(TAG, "ERROR : " + error.toString());
        }

    }


//    /**
//     * void installPackageAsUser(in String originPath,
//     * in IPackageInstallObserver2 observer,
//     * int flags,
//     * in String installerPackageName,
//     * int userId);
//     *
//     * @param installPath
//     */
//    public void installO(String installPath, String packageName) {
//        Class<?> pmService;
//        Class<?> activityTherad;
//        Method method;
//        try {
//            activityTherad = Class.forName("android.app.ActivityThread");
//            Class<?> paramTypes[] = getParamTypes(activityTherad, "getPackageManager");
//            method = activityTherad.getMethod("getPackageManager", paramTypes);
//            Object PackageManagerService = method.invoke(activityTherad);
//            pmService = PackageManagerService.getClass();
//            Class<?> paramTypes1[] = getParamTypes(pmService, "installPackageAsUser");
//            method = pmService.getMethod("installPackageAsUser", paramTypes1);
//            method.invoke(PackageManagerService, installPath, null, 0x00000040, packageName, getUserId(Binder.getCallingUid()));//getUserId
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private Class<?>[] getParamTypes(Class<?> cls, String mName) {
//        Class<?> cs[] = null;
//        Method[] mtd = cls.getMethods();
//        for (int i = 0; i < mtd.length; i++) {
//            if (!mtd[i].getName().equals(mName)) {
//                continue;
//            }
//
//            cs = mtd[i].getParameterTypes();
//        }
//        return cs;
//    }
//
//    public static final int PER_USER_RANGE = 100000;
//
//    public static int getUserId(int uid) {
//        return uid / PER_USER_RANGE;
//    }

    // 适配android9的安装方法。
    public void installP(String apkFilePath) {
        File apkFile = new File(apkFilePath);
        PackageInstaller packageInstaller = StoreModeApplication.sContext.getPackageManager().getPackageInstaller();
        PackageInstaller.SessionParams sessionParams
                = new PackageInstaller.SessionParams(PackageInstaller
                .SessionParams.MODE_FULL_INSTALL);
        sessionParams.setSize(apkFile.length());

        int sessionId = createSession(packageInstaller, sessionParams);
        if (sessionId != -1) {
            boolean copySuccess = copyInstallFile(packageInstaller, sessionId, apkFilePath);
            LogUtils.d(TAG, "copyInstallFile  succ:" + copySuccess);
            if (copySuccess) {
                execInstallCommand(packageInstaller, sessionId);
            }
        }
    }

    private int createSession(PackageInstaller packageInstaller,
                              PackageInstaller.SessionParams sessionParams) {
        int sessionId = -1;
        try {
            sessionId = packageInstaller.createSession(sessionParams);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sessionId;
    }

    private boolean copyInstallFile(PackageInstaller packageInstaller,
                                    int sessionId, String apkFilePath) {
        InputStream in = null;
        OutputStream out = null;
        PackageInstaller.Session session = null;
        boolean success = false;
        try {
            File apkFile = new File(apkFilePath);
            session = packageInstaller.openSession(sessionId);
            out = session.openWrite("base.apk", 0, apkFile.length());
            in = new FileInputStream(apkFile);
            int total = 0, c;
            byte[] buffer = new byte[65536];
            while ((c = in.read(buffer)) != -1) {
                total += c;
                out.write(buffer, 0, c);
            }
            session.fsync(out);
            Log.i(TAG, "streamed " + total + " bytes");
            success = true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeQuietly(out);
            closeQuietly(in);
            closeQuietly(session);
        }
        return success;
    }

    private void execInstallCommand(PackageInstaller packageInstaller, int sessionId) {
        PackageInstaller.Session session = null;
        try {
            session = packageInstaller.openSession(sessionId);
            Intent intent = new Intent(StoreModeApplication.sContext, InstallResultReceiver.class);
            intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
            intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(StoreModeApplication.sContext,
                    1, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            session.commit(pendingIntent.getIntentSender());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeQuietly(session);
        }
    }

    private void closeQuietly(Object obj) {
        LogUtils.d(TAG, "closeQuietly()   object:" + obj);
        if (obj == null) return;
        if (obj instanceof PackageInstaller.Session) {
            ((PackageInstaller.Session) obj).close();
        } else if (obj instanceof InputStream) {
            try {
                ((InputStream) obj).close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (obj instanceof OutputStream) {
            try {
                ((OutputStream) obj).close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}