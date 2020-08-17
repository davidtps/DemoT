package com.hisense.storemode.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.StatFs;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/**
 * @author tianpengsheng
 * @version create time：Jan 5, 2013 9:32:24 PM
 */
public class FileUtil {
    private static final String TAG = "FileUtil";
    private static final String SPLIT_FLAG = "-";
    private static final String LINK_DOT = ".";


    //1.type max count is 2  example:"aa-bb"
    //2.name  (path need contains this name)
    public static List<String> getFilesPathByTypeAndName(String parentPath, String type, String name) {
        String suffixFlag = "";
        String tempName = name.toLowerCase();
        String[] names = tempName.split(SPLIT_FLAG);
        List<String> paths = new ArrayList<>();
        LogUtils.d(TAG, "FileUtil method getFilesPathByTypeAndName parentPath:" + parentPath + "   type:" + type + "   fileName:" + name);
        if (StringUtil.isEmpty(parentPath)) {
            LogUtils.d(TAG, "FileUtil method getFilesPathByTypeAndName parameter parentPath is null");
            return paths;
        }

        File file = new File(parentPath);
        File[] files = file.listFiles();
        if (files == null) {
            LogUtils.d(TAG, "getFilesPathByTypeAndName()  parentPath:" + parentPath + "    catalog is empty");
            return paths;
        }
        String[] types = type.split(SPLIT_FLAG);
        //add file path
        for (int i = 0; i < files.length; i++) {
            String tempPath = files[i].getAbsolutePath().toLowerCase();
            String[] fileNames = tempPath.split("/");
            String fileName = fileNames[fileNames.length - 1];

//            LogUtils.d(TAG, "getFilesPathByTypeAndName() fileName======================:" + fileName);
            if (StringUtil.trim(fileName) != null && StringUtil.trim(fileName).length() > 4) {
                suffixFlag = StringUtil.trim(fileName).substring(fileName.length() - 4, fileName.length());
            } else {
                continue;
            }
//            LogUtils.d(TAG, "getFilesPathByTypeAndName() fileName======================handle after========:" + fileName);
            if (types.length == 1) {//type one
                if (names.length == 1) {
                    if (suffixFlag.contains(LINK_DOT + types[0]) && fileName.contains(names[0])) {
                        paths.add(files[i].getAbsolutePath());
                    }
                } else if (names.length == 2) {
                    if (suffixFlag.contains(LINK_DOT + types[0]) && (fileName.contains(names[0]) || fileName.contains(names[1]))) {
                        paths.add(files[i].getAbsolutePath());
                    }
                }
            } else if (types.length == 2) {//type two
                if (names.length == 1) {
                    if ((suffixFlag.contains(LINK_DOT + types[0]) || suffixFlag.contains(LINK_DOT + types[1]))
                            && fileName.contains(names[0])) {
                        paths.add(files[i].getAbsolutePath());
                    }
                } else if (names.length == 2) {
                    if ((suffixFlag.contains(LINK_DOT + types[0]) || suffixFlag.contains(LINK_DOT + types[1]))
                            && (fileName.contains(names[0]) || fileName.contains(names[1]))) {
                        paths.add(files[i].getAbsolutePath());
                    }
                }
            }
        }
        return paths;
    }

    //1.type max count is 2  example:"aa-bb"
    //2.name  (path file name need equals this name)
    public static List<String> getFilesPathByTypeAndNameEquals(String parentPath, String type, String name) {
        String tempName = name.toLowerCase();
        String[] names = tempName.split(SPLIT_FLAG);
        List<String> paths = new ArrayList<>();

        if (StringUtil.isEmpty(parentPath)) {
            LogUtils.d(TAG, "FileUtil method getFilesPathByTypeAndName parameter parentPath is null");
            return paths;
        }

        File file = new File(parentPath);
        File[] files = file.listFiles();
        if (files == null) {
            LogUtils.d(TAG, "getFilesPathByTypeAndName()  parentPath:" + parentPath + "    catalog is empty");
            return paths;
        }
        String[] types = type.split(SPLIT_FLAG);
        //add file path
        for (int i = 0; i < files.length; i++) {
            String tempPath = files[i].getAbsolutePath().toLowerCase();
            String[] fileNames = tempPath.split("/");
            String fileName = fileNames[fileNames.length - 1];
//            LogUtils.d(TAG, "getFilesPathByTypeAndName() fileName======================:" + fileName);
            if (types.length == 1) {//type one
                if (names.length == 1) {
                    if (fileName.contains(LINK_DOT + types[0]) && fileName.equals(names[0])) {
                        paths.add(files[i].getAbsolutePath());
                    }
                } else if (names.length == 2) {
                    if (fileName.contains(LINK_DOT + types[0]) && (fileName.equals(names[0]) || fileName.equals(names[1]))) {
                        paths.add(files[i].getAbsolutePath());
                    }
                }
            } else if (types.length == 2) {//type two
                if (names.length == 1) {
                    if ((fileName.contains(LINK_DOT + types[0]) || fileName.contains(LINK_DOT + types[1]))
                            && fileName.equals(names[0])) {
                        paths.add(files[i].getAbsolutePath());
                    }
                } else if (names.length == 2) {
                    if ((fileName.contains(LINK_DOT + types[0]) || fileName.contains(LINK_DOT + types[1]))
                            && (fileName.equals(names[0]) || fileName.equals(names[1]))) {
                        paths.add(files[i].getAbsolutePath());
                    }
                }
            }
        }
        return paths;
    }

    //1.type max count is 2  example:"aa-bb"
    public static List<String> getFilesPathByType(String parentPath, String type) {
        String suffixFlag = "";
        List<String> paths = new ArrayList<>();
        LogUtils.d(TAG, "FileUtil method getFilesPathByType() parentPath:" + parentPath + "   type:" + type);
        if (StringUtil.isEmpty(parentPath)) {
            LogUtils.d(TAG, "FileUtil method getFilesPathByType() parameter parentPath is null");
            return paths;
        }

        File file = new File(parentPath);
        File[] files = file.listFiles();
        if (files == null) {
            LogUtils.d(TAG, "getFilesPathByType()  parentPath:" + parentPath + "    catalog is empty");
            return paths;
        }
        String[] types = type.split(SPLIT_FLAG);
        //add file path
        for (int i = 0; i < files.length; i++) {
            String tempPath = files[i].getAbsolutePath().toLowerCase();
            String[] fileNames = tempPath.split("/");
            String fileName = fileNames[fileNames.length - 1];

//            LogUtils.d(TAG, "getFilesPathByType() fileName======================:" + fileName);
            if (StringUtil.trim(fileName) != null && StringUtil.trim(fileName).length() > 4) {
                suffixFlag = StringUtil.trim(fileName).substring(fileName.length() - 4, fileName.length());
            } else {
                continue;
            }
//            LogUtils.d(TAG, "getFilesPathByType() fileName======================handle after========:" + fileName);
            if (types.length == 1) {//type one
                if (suffixFlag.contains(LINK_DOT + types[0])) {
                    paths.add(files[i].getAbsolutePath());
                }
            } else if (types.length == 2) {//type two
                if (suffixFlag.contains(LINK_DOT + types[0]) || suffixFlag.contains(LINK_DOT + types[1])) {
                    paths.add(files[i].getAbsolutePath());
                }
            }
        }
        return paths;
    }


    /**
     * file copy
     *
     * @param fromPath
     * @param toPath
     */
    public static void copyFilePathToPath(String fromPath, String toPath) {
        final Path copy_from = Paths.get(fromPath);
        final Path copy_to = Paths.get(toPath);
        long startTime, elapsedTime;
        int bufferSizeKB = 4; //also tested for 16, 32, 64, 128, 256 and 1024
        int bufferSize = bufferSizeKB * 1024;

//        deleteCopied(copy_to);

        //FileChannel and non-direct buffer
        try (FileChannel fileChannel_from = (FileChannel.open(copy_from,
                EnumSet.of(StandardOpenOption.READ)));
             FileChannel fileChannel_to = (FileChannel.open(copy_to,
                     EnumSet.of(StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE)))) {

            startTime = System.nanoTime();

            // Allocate a non-direct ByteBuffer
            ByteBuffer bytebuffer = ByteBuffer.allocate(bufferSize);

            // Read data from file into ByteBuffer
            int bytesCount;
            while ((bytesCount = fileChannel_from.read(bytebuffer)) > 0) {
                //flip the buffer which set the limit to current position, and position to 0
                bytebuffer.flip();
                //write data from ByteBuffer to file
                fileChannel_to.write(bytebuffer);
                //for the next read
                bytebuffer.clear();
            }

            elapsedTime = System.nanoTime() - startTime;
            System.out.println("Elapsed Time is " + (elapsedTime / 1000000000.0) + " seconds");
        } catch (IOException ex) {
            System.err.println(ex);
        }

//        deleteCopied(copy_to);
    }


    public static boolean isDirExist(String dirPath) {
        File dir = new File(dirPath);
        return dir.exists() && dir.isDirectory();
    }
//
//    public static void createDir(String... dirPath) {
//        File dir = null;
//        for (int i = 0; i < dirPath.length; i++) {
//            dir = new File(dirPath[i]);
//            if (!dir.exists() && !dir.isDirectory()) {
//                dir.mkdirs();
//            }
//        }
//    }

    /**
     * 判断SD卡上的文件是否存在
     *
     * @param filePath
     * @return
     */
    public static boolean isFileExist(String filePath) {
        if (StringUtil.isEmpty(filePath)) {
            return false;
        }
        File file = new File(filePath);
        return file.exists();
    }

    /**
     * 在SD卡上创建文件
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static File createSDFile(File file) throws IOException {
        LogUtils.d(TAG, "createSDFile()  path:" + file.getPath());
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        if (!file.exists()) {
            file.createNewFile();
        }
        return file;
    }

    // 判断SD卡是否被挂载
    public static boolean isSDCardMounted() {
        // return Environment.getExternalStorageState().equals("mounted");
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    // 获取SD卡的根目录
    public static String getSDCardBaseDir() {
        if (isSDCardMounted()) {
            return Environment.getExternalStorageDirectory().getAbsolutePath();
        }
        return null;
    }

    // require special path space size，return MB
    public static long getPathSpaceSize(String path) {
        if (isDirExist(path)) {
            StatFs fs = new StatFs(path);
            long count = fs.getBlockCountLong();
            long size = fs.getBlockSizeLong();
            return count * size / 1024 / 1024;
        }
        return 0;
    }

    // 获取SD卡的完整空间大小，返回MB
    public static long getSDCardSize() {
        if (isSDCardMounted()) {
            StatFs fs = new StatFs(getSDCardBaseDir());
            long count = fs.getBlockCountLong();
            long size = fs.getBlockSizeLong();
            return count * size / 1024 / 1024;
        }
        return 0;
    }

    // 获取SD卡的剩余空间大小
    public static long getSDCardFreeSize() {
        if (isSDCardMounted()) {
            StatFs fs = new StatFs(getSDCardBaseDir());
            long count = fs.getFreeBlocksLong();
            long size = fs.getBlockSizeLong();
            return count * size / 1024 / 1024;
        }
        return 0;
    }

    public static long getFileSizeUnitM(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            return file.length() / 1024 / 1024;
        }
        return 0;
    }

    public static long getFileSizeUnitKB(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            return file.length();
        }
        return 0;
    }

    public static boolean isEmptyFile(String filePath) {
        long temp = getFileSizeUnitKB(filePath);
        if (temp == 0) {
            return true;
        }
        return false;
    }


    // 获取SD卡的可用空间大小
    public static long getSDCardAvailableSize() {
        if (isSDCardMounted()) {
            StatFs fs = new StatFs(getSDCardBaseDir());
            long count = fs.getAvailableBlocksLong();
            long size = fs.getBlockSizeLong();
            return count * size / 1024 / 1024;
        }
        return 0;
    }

    // 往SD卡的公有目录下保存文件
    public static boolean saveFileToSDCardPublicDir(byte[] data, String type,
                                                    String fileName) {
        BufferedOutputStream bos = null;
        if (isSDCardMounted()) {
            File file = Environment.getExternalStoragePublicDirectory(type);
            try {
                bos = new BufferedOutputStream(new FileOutputStream(new File(
                        file, fileName)));
                bos.write(data);
                bos.flush();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    // 往SD卡的自定义目录下保存文件
    public static boolean saveFileToSDCardCustomDir(byte[] data, String dir,
                                                    String fileName) {
        BufferedOutputStream bos = null;
        if (isSDCardMounted()) {
            File file = new File(getSDCardBaseDir() + File.separator + dir);
            if (!file.exists()) {
                file.mkdirs();// 递归创建自定义目录
            }
            try {
                bos = new BufferedOutputStream(new FileOutputStream(new File(
                        file, fileName)));
                bos.write(data);
                bos.flush();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    // 往SD卡的私有Files目录下保存文件
    public static boolean saveFileToSDCardPrivateFilesDir(byte[] data,
                                                          String type, String fileName, Context context) {
        BufferedOutputStream bos = null;
        if (isSDCardMounted()) {
            File file = context.getExternalFilesDir(type);
            try {
                bos = new BufferedOutputStream(new FileOutputStream(new File(
                        file, fileName)));
                bos.write(data);
                bos.flush();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    // 往SD卡的私有Cache目录下保存文件
    public static boolean saveFileToSDCardPrivateCacheDir(byte[] data,
                                                          String fileName, Context context) {
        BufferedOutputStream bos = null;
        if (isSDCardMounted()) {
            File file = context.getExternalCacheDir();
            try {
                bos = new BufferedOutputStream(new FileOutputStream(new File(
                        file, fileName)));
                bos.write(data);
                bos.flush();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    // 保存bitmap图片到SDCard的私有Cache目录
    public static boolean saveBitmapToSDCardPrivateCacheDir(Bitmap bitmap,
                                                            String fileName, Context context) {
        if (isSDCardMounted()) {
            BufferedOutputStream bos = null;
            // 获取私有的Cache缓存目录
            File file = context.getExternalCacheDir();

            try {
                bos = new BufferedOutputStream(new FileOutputStream(new File(
                        file, fileName)));
                if (fileName != null
                        && (fileName.contains(".png") || fileName
                        .contains(".PNG"))) {
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
                } else {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                }
                bos.flush();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (bos != null) {
                    try {
                        bos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return true;
        } else {
            return false;
        }
    }

    // 从SD卡获取文件
    public static byte[] loadFileFromSDCard(String fileDir) {
        BufferedInputStream bis = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            bis = new BufferedInputStream(
                    new FileInputStream(new File(fileDir)));
            byte[] buffer = new byte[8 * 1024];
            int c = 0;
            while ((c = bis.read(buffer)) != -1) {
                baos.write(buffer, 0, c);
                baos.flush();
            }
            return baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                baos.close();
                bis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    // 从SDCard中寻找指定目录下的文件，返回Bitmap
    public Bitmap loadBitmapFromSDCard(String filePath) {
        byte[] data = loadFileFromSDCard(filePath);
        if (data != null) {
            Bitmap bm = BitmapFactory.decodeByteArray(data, 0, data.length);
            if (bm != null) {
                return bm;
            }
        }
        return null;
    }

    // 获取SD卡公有目录的路径
    public static String getSDCardPublicDir(String type) {
        if (isSDCardMounted()) {
            return Environment.getExternalStoragePublicDirectory(type).toString();
        }
        return null;
    }

    // 获取SD卡私有Cache目录的路径
    public static String getSDCardPrivateCacheDir(Context context) {
        return context.getExternalCacheDir().getAbsolutePath();
    }

    // 获取SD卡私有Files目录的路径
    public static String getSDCardPrivateFilesDir(Context context, String type) {
        return context.getExternalFilesDir(type).getAbsolutePath();
    }


    // 从sdcard中删除文件
    public static boolean removeFileFromSDCard(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            try {
                file.delete();
                return true;
            } catch (Exception e) {
                return false;
            }
        } else {
            return false;
        }
    }


    /**
     * 判断SD卡是否可用
     *
     * @return SD卡可用返回true
     */
    public static boolean hasSdcard() {
        String status = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(status);
    }


    public static void copyFile(File file, File newfile) {
        LogUtils.d(TAG, "copyFile()  :srcpath:" + file.getPath() + "   desPath:" + newfile.getPath());
        try {
            createSDFile(newfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //复制的文件不止文本文件，所以采用字节流复制
        //缓冲字节流，字节数组复制
        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
             BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(newfile))
        ) {
            byte[] bytes = new byte[1024];
            int hasRead;
            while ((hasRead = bufferedInputStream.read(bytes)) > 0) {
                bufferedOutputStream.write(bytes, 0, hasRead);
                bufferedOutputStream.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //1.firstPath--epos
    public static String getFolderPathByName(String parentPath, String firstPath) {
        String paths = "";
        LogUtils.d(TAG, "FileUtil method getFolderPathByName parentPath:" + parentPath + "   firstPath:" + firstPath);
        if (StringUtil.isEmpty(parentPath)) {
            LogUtils.d(TAG, "FileUtil method getFolderPathByName parameter parentPath is null");
            return paths;
        }

        File file = new File(parentPath);
        File[] files = file.listFiles();
        if (files == null) {
            LogUtils.d(TAG, "getFolderPathByName()  parentPath:" + parentPath + "    catalog is empty");
            return paths;
        }
        for (int i = 0; i < files.length; i++) {
            String tempPath = files[i].getAbsolutePath();
            String[] fileNames = tempPath.split("/");
            String fileName = fileNames[fileNames.length - 1];
            if (firstPath.equals(fileName)) {
                paths = tempPath;
                LogUtils.d(TAG, "getFolderPathByName()  tempPath:" + tempPath);
                break;
            }

        }
        return paths;
    }

    //分析：
    //  A:封装数据源scrFile
    //  B:封装目的地destFile
    //  C:判断scrfile是文件夹还是文件
    //      a：是文件夹
    //          就在目的地下创建文件夹
    //          获取该File对象下所有文件或者文件夹File对象
    //          遍历得到每一个File对象
    //          回到C
    //      b：是文件
    //          复制（字节流）
    public static void copyFolder(File scrfile, File destfile) {
        LogUtils.d(TAG, "copyFolder()  :" + scrfile.getPath());
        if (scrfile.isDirectory()) {
            //不是文件
            //  就在目的地下创建文件夹
            File newFolder = new File(destfile, scrfile.getName());
            LogUtils.d(TAG, "copyFolder()  newFolder path:" + newFolder.getPath());
            newFolder.mkdirs();
            //获取该File对象下所有文件或者文件夹File对象
            File[] fileArray = scrfile.listFiles();
            if (fileArray == null) {
                LogUtils.d(TAG, "copyFolder()  scrfile.listFiles() null");
                return;
            }
            for (File file : fileArray) {
                copyFolder(file, newFolder);  //递归
            }
        } else {
            //是文件
            //复制
            File newfile = new File(destfile, scrfile.getName());
            copyFile(scrfile, newfile);  //调用复制文件的方法
        }
    }

    //递归删除多级文件夹或文件
    public static void deleteFolder(File file) {
        LogUtils.d(TAG, "deleteFolder()  deletepath:" + file.getPath());
        if (file.isFile()) {
            file.delete();
        } else {
            File[] files = file.listFiles();
            if (files == null) {
                LogUtils.d(TAG, "deleteFolder()  path:" + file.getPath() + "    catalog is empty");
                return;
            }
            for (File f : files) {
                if (f.isFile()) {
                    //如果是文件就直接删除
                    f.delete();
                } else {
                    //如果是文件夹就递归调用
                    deleteFolder(f);
                }
            }
            //删除空目录
            file.delete();
        }
    }
}
