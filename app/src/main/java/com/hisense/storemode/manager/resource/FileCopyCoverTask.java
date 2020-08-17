package com.hisense.storemode.manager.resource;

import android.os.AsyncTask;

import com.hisense.storemode.utils.LogUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by tianpengsheng on 2019年04月25日 11时46分.
 * <p>
 * cover copy
 */
public class FileCopyCoverTask extends AsyncTask<String, Integer, Boolean> {

    private static final String TAG = "FileCopyCoverTask";
    private FileCopyListener mCopyListener;
    private boolean mIsCopyCancel = false;
    private boolean mIsDownloading = false;

    @Override
    protected Boolean doInBackground(String... strings) {
        String fromPath = strings[0];
        String toPath = strings[1];

        File fromFile = new File(fromPath);
        File toFile = new File(toPath);

        long startTime, elapsedTime;
        int bufferSizeKB = 2;
        int bufferSize = bufferSizeKB * 1024;

        FileInputStream fis = null;
        FileOutputStream fos = null;

        try {

            //1:cover to target file;
            LogUtils.d(TAG, "toFile exists monitor  toPath:" + toPath);
            if (!toFile.getParentFile().exists()) {
                LogUtils.d(TAG, "ParentFile is not exists ");
                toFile.getParentFile().mkdirs();
            }

            if (!toFile.exists()) {
                LogUtils.d(TAG, "toFile is not exists ");
                toFile.createNewFile();
            }

            fos = new FileOutputStream(toFile, false);
            fis = new FileInputStream(fromFile);
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

            elapsedTime = System.nanoTime() - startTime;
            LogUtils.d("Elapsed Time is " + (elapsedTime / 1000000000.0) + " seconds   ");

        } catch (IOException e) {
            e.printStackTrace();
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