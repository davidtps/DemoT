package com.hisense.storemode.manager.resource;

import android.os.AsyncTask;

import com.hisense.storemode.utils.ConstantConfig;
import com.hisense.storemode.utils.LogUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by tianpengsheng on 2019年04月25日 11时46分.
 * file copy
 */
public class FileCopyForVideoUpdateTask extends AsyncTask<String, Integer, Boolean> {

    private static final String TAG = "FileCopyForVideoUpdateTask";
    private FileCopyListener mCopyListener;
    private boolean mIsCopyCancel = false;
    private boolean mIsDownloading = false;
    private String mTempVideoName = "/video_temp.mp4";

    @Override
    protected Boolean doInBackground(String... strings) {
        String fromPath = strings[0];
        String toPath = strings[1];


        File fromFile = new File(fromPath);
        File toFile = new File(toPath + mTempVideoName);
        File oldFile = new File(ConstantConfig.VIDEO_BUILD_IN_UPDATE_FILE_PATH);

        LogUtils.d(TAG, "fromPath:" + fromPath);
        LogUtils.d(TAG, "toFile Path:" + toFile.getAbsolutePath());
        LogUtils.d(TAG, "oldFile path:" + oldFile.getAbsolutePath());


        long startTime, elapsedTime;
        int bufferSizeKB = 2; //also tested for 16, 32, 64, 128, 256 and 1024
        int bufferSize = bufferSizeKB * 1024;

        FileInputStream fis = null;
        FileOutputStream fos = null;

        try {
            //1:copy starting delete toPath catalog file: video.mp4
            if (oldFile.exists()) {
                oldFile.delete();
            }
            if (!toFile.exists()) {
                toFile.createNewFile();
            }

            //2:copy to file to path ,fileName :video_temp.mp4;
            fis = new FileInputStream(fromFile);
            fos = new FileOutputStream(toFile);
            float totalLength = fis.available();
            float readed = 0;

            startTime = System.nanoTime();

            byte[] bytes = new byte[bufferSize];
            int bytesCount;
            while ((bytesCount = fis.read(bytes)) != -1 && !mIsCopyCancel) {
                mIsDownloading = true;
                fos.write(bytes, 0, bytesCount);
                readed += bytesCount;
                float progress = readed / totalLength * 100;
                if (progress > 100) {
                    progress = 100;
                }
                publishProgress((int) progress);
            }
            fos.flush();
            mIsDownloading = false;


            //3:copy finish,renameTo video.mp4;
            long a = System.nanoTime();
            boolean success = toFile.renameTo(oldFile);
            LogUtils.d(TAG, "renameto  time is :" + (System.nanoTime() - a) / 1000000000.0 + " seconds" + success);

            elapsedTime = System.nanoTime() - startTime;
            LogUtils.d(TAG, "Elapsed Time is " + (elapsedTime / 1000000000.0) + " seconds   ");
        } catch (IOException e) {
            e.printStackTrace();
            if (toFile.exists())
                toFile.delete();
            if (oldFile.exists())
                oldFile.delete();
            mIsDownloading = false;
            return false;
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    @Override
    protected void onPostExecute(Boolean success) {
        super.onPostExecute(success);
        if (mIsCopyCancel) {//cancel copy
            return;
        }
        if (success) {
            if (mCopyListener != null)
                mCopyListener.onCopySuccessed();
        } else {
            if (mCopyListener != null)
                mCopyListener.onCopyFailed();
        }
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        if (mCopyListener != null)
            mCopyListener.onProcessUpdate(values[0]);
    }

    public void setLisener(FileCopyListener listener) {
        mCopyListener = listener;
    }

    public void stopCopy() {
        mIsCopyCancel = true;
    }

    public interface FileCopyListener {

        void onProcessUpdate(Integer process);

        void onCopySuccessed();

        void onCopyFailed();
    }

    public boolean isDownloading() {
        return mIsDownloading;
    }
}