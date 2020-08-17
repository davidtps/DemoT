package com.hisense.storemode.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by tianpengsheng on 2019年07月09日 09时50分.
 */

public class FileTypeUtil {
    private static String TAG = "FileTypeUtil";

    private final static Map<String, String> FILE_TYPE_MAP = new HashMap<String, String>();

    private FileTypeUtil() {
    }

    static {
        getAllFileType();  //初始化文件类型信息
    }

    private static void getAllFileType() {
        FILE_TYPE_MAP.put("1a45dfa3", "mkv");
        FILE_TYPE_MAP.put("667479706", "mp4");
        FILE_TYPE_MAP.put("000001ba210001000", "mp4");


//        FILE_TYPE_MAP.put("ffd8ffe000104a464946", "jpg"); //JPEG (jpg)
//        FILE_TYPE_MAP.put("89504e470d0a1a0a0000", "png"); //PNG (png)
//        FILE_TYPE_MAP.put("2e524d46000000120001", "rmvb"); //rmvb/rm相同
//        FILE_TYPE_MAP.put("464c5601050000000900", "flv"); //flv与f4v相同
//        FILE_TYPE_MAP.put("3026b2758e66cf11a6d9", "wmv"); //wmv与asf相同
//        FILE_TYPE_MAP.put("524946464694c9015741", "wav"); //Wave (wav)
//        FILE_TYPE_MAP.put("52494646d07d60074156", "avi");


    }

    /**
     * 得到上传文件的文件头
     *
     * @param src
     * @return
     */
    private static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder();
        if (null == src || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    public static boolean isVideoTypeFile(File file) {
        if (file != null && file.exists()) {
            String type = getFileType(file);
            LogUtils.d(TAG, "isVideoTypeFile  type:" + type + "   file:" + file.getPath());
            switch (type) {
                case "mp4":
                    return true;
                case "mkv":
                    return true;
                default:
                    return false;
            }
        }
        return false;
    }

    /**
     * 获取文件类型
     *
     * @param file
     * @return
     */
    public static String getFileType(File file) {
        if (file != null && file.exists()) {
        } else {
            return "";
        }
        String res = "";
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            //head 40
            byte[] b = new byte[20];
            fis.read(b, 0, b.length);
            String fileCode = bytesToHexString(b);
            Iterator<String> keyIter = FILE_TYPE_MAP.keySet().iterator();
            while (keyIter.hasNext()) {
                String key = keyIter.next();
                //contains special keycode
                if (fileCode.toLowerCase().contains(key.toLowerCase())) {
                    res = FILE_TYPE_MAP.get(key);
                    break;
                }
            }
            LogUtils.d("FileTypeUtil getFileType()" + file.getPath() + "  file head:" + fileCode + "-----file type:" + res);
            if (fis != null)
                fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null)
                    fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return res;
    }

}
