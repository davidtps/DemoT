package com.hisense.storemode.utils;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;

import com.hisense.storemode.StoreModeApplication;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by zengyi on 2018/10/25.
 */
public class UsbUtil {

    private static final String TAG = "UsbUtil";

    //is have usb devices
    public static boolean isHaveUsbDevice() {
        return getUsbPath().size() > 0;
    }

    public static List<UsbPathAndLabel> getUsbPath() {
        String filterPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        List<UsbPathAndLabel> externalPath = new ArrayList<>();
        StorageManager storageManager = (StorageManager) StoreModeApplication.sContext.getSystemService(Context
                .STORAGE_SERVICE);
        if (storageManager != null) {
            try {
                Method getVolumeList = storageManager.getClass()
                        .getDeclaredMethod("getVolumeList", null);
                StorageVolume[] storageVolumes = (StorageVolume[])
                        getVolumeList.invoke(storageManager, null);
                ArrayList<StorageVolume> list = new ArrayList<>();
                Collections.addAll(list, storageVolumes);
                for (int i = list.size() - 1; i >= 0; i--) {
                    StorageVolume volume = list.get(i);
                    String path = receivePath(volume).getAbsolutePath();
                    if (filterPath.contains(path) || path.contains(filterPath)) {
                        list.remove(volume);
                    }
                }
                for (int i = 0; i < list.size(); i++) {
                    UsbPathAndLabel usbPathAndLabel = new UsbPathAndLabel();
                    usbPathAndLabel.mPath = receivePath(list.get(i)).getAbsolutePath();
                    usbPathAndLabel.mLabel = receiveName(list.get(i));
                    //usbPathAndLabel.mVolumeId = receiveId(list.get(i));
                    if (ConstantConfig.EJECT_STORAGE_PATH.contains(usbPathAndLabel.mPath)) {
                        LogUtils.d(TAG, "remove  usbPathAndLabel   path:" + usbPathAndLabel.mPath + "  label:" + usbPathAndLabel.mLabel);
                        ConstantConfig.EJECT_STORAGE_PATH = "";
                        continue;
                    }
                    externalPath.add(usbPathAndLabel);
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        return externalPath;
    }

    private static File receivePath(StorageVolume volume) throws NoSuchFieldException,
            IllegalAccessException {
        Field field = volume.getClass().getDeclaredField("mPath");
        field.setAccessible(true);
        return (File) field.get(volume);
    }

    private static String receiveName(StorageVolume volume) throws NoSuchFieldException,
            IllegalAccessException {
        Field field = volume.getClass().getDeclaredField("mDescription");
        field.setAccessible(true);
        return (String) field.get(volume);
    }

    private static int receiveId(StorageVolume volume) throws NoSuchFieldException,
            IllegalAccessException {
        Field field = volume.getClass().getDeclaredField("mStorageId");
        field.setAccessible(true);
        return (Integer) field.get(volume);
    }

    private static Object getFieldValue(Object obj, String name) throws NoSuchFieldException,
            IllegalAccessException {
        Field field = obj.getClass().getDeclaredField(name);
        field.setAccessible(true);
        return field.get(obj);
    }

    private static Method getMethod(Object obj, String methodName, Class[] cls)
            throws NoSuchFieldException,
            IllegalAccessException {
        Method method = null;
        try {
            method = obj.getClass().getMethod(methodName, cls);
            method.setAccessible(true);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return method;
    }

    public static void formatUsb(String path, Context context) {
        StorageManager storageManager = (StorageManager) context.getSystemService(Context
                .STORAGE_SERVICE);
        //IMountService
        try {
            Object iMountService = getFieldValue(storageManager, "mMountService");
            Class[] cls = {String.class};
            Method formatVolume = getMethod(iMountService, "formatVolume", cls);
            formatVolume.invoke(iMountService, path);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public static void mountVolume(String mountPoint, Context context) {
        LogUtils.e("UsbUtil" + ":", "mountVolume: " + mountPoint);
        StorageManager storageManager = (StorageManager) context.getSystemService(Context
                .STORAGE_SERVICE);
        //IMountService
        try {
            Object iMountService = getFieldValue(storageManager, "mMountService");
            Class[] cls = {String.class};
            Method formatVolume = getMethod(iMountService, "mountVolume", cls);
            formatVolume.invoke(iMountService, mountPoint);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获得sd卡使用率
     *
     * @return rate %
     */
    public static String getUsbUsage(String path) {
        try {
            StatFs stat = new StatFs(path);
            // long blockSize = stat.getBlockSize();
            long availableBlocks = stat.getAvailableBlocks();
            long totalBlockCount = stat.getBlockCount();
            int usageRate = (int) (((totalBlockCount - availableBlocks) * 100 / totalBlockCount));
            if (usageRate < 0) {
                usageRate = 0;
            }
            return String.valueOf(usageRate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0 + "";

    }

    public static class UsbPathAndLabel {
        public String mPath;
        public String mLabel;
        public int mVolumeId;
    }

}
