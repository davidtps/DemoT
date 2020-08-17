
package com.hisense.storemode.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.View;

import com.bumptech.glide.Glide;
import com.hisense.storemode.R;
import com.hisense.storemode.StoreModeApplication;
import com.hisense.storemode.bean.FinishEvent;
import com.hisense.storemode.manager.StoreModeManager;
import com.hisense.storemode.manager.resource.PictureUpdateTask;
import com.hisense.storemode.utils.ConstantConfig;
import com.hisense.storemode.utils.FileUtil;
import com.hisense.storemode.utils.LogUtils;
import com.hisense.storemode.utils.PreferenceUtils;
import com.hisense.storemode.utils.ToastUtil;
import com.hisense.storemode.utils.UsbUtil;
import com.hisense.storemode.widget.UpdatePictureGuidanceStyList;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.leanback.app.GuidedStepFragment;
import androidx.leanback.widget.GuidanceStylist;
import androidx.leanback.widget.GuidedAction;

/**
 * Created by cuihuihui on 2019年05月09日 15时30分.
 */

public class UpdatePictureFragment extends GuidedStepFragment implements PictureUpdateTask.FileCopyListener {
    private static final String TAG = "UpdatePictureFragment";
    private int mPictureMaxNumber = ConstantConfig.BUIDL_IN_PIC_UPDATE_MAX_NUMBER;
    private int mCurrentNumber = 0;
    private String mFromPath = "";
    private String mToPath = ConstantConfig.PICTURE_BUILD_IN_PATH;
    private List<String> mPicturePaths = new ArrayList<>();

    private static final int ACTION_ID_POSITIVE = 1;
    private static final int ACTION_ID_NEGATIVE = ACTION_ID_POSITIVE + 1;


    private PictureUpdateTask mUpdatePictureTask;
    private UpdatePictureGuidanceStyList mUpdatePictureGuidanceStyList;
    private boolean mIsFinishDownload = false;
    private HandlerThread mHandlerThread;
    private Handler mTaskHandler;

    @NonNull
    @Override
    public GuidanceStylist.Guidance onCreateGuidance(Bundle savedInstanceState) {
        String title = getString(R.string.picture_update_title);
        if (mHandlerThread == null) {
            mHandlerThread = new HandlerThread(TAG);
            mHandlerThread.start();
        }
        if (mTaskHandler == null) {
            mTaskHandler = new Handler(mHandlerThread.getLooper());
        }

        return new GuidanceStylist.Guidance(title, "", "", null);
    }

    @Override
    public GuidanceStylist onCreateGuidanceStylist() {
        mUpdatePictureGuidanceStyList = new UpdatePictureGuidanceStyList();
        return mUpdatePictureGuidanceStyList;
    }

    @Override
    public void onCreateActions(@NonNull List<GuidedAction> actions, Bundle savedInstanceState) {
        GuidedAction action = new GuidedAction.Builder()
                .id(ACTION_ID_POSITIVE)
                .title(getString(R.string.video_update_button_update)).build();
        actions.add(action);
        action = new GuidedAction.Builder()
                .id(ACTION_ID_NEGATIVE)
                .title(getString(R.string.video_update_button_cancel)).build();
        actions.add(action);
    }

    @Override
    public void onGuidedActionClicked(GuidedAction action) {
        if (ACTION_ID_POSITIVE == action.getId()) {
            LogUtils.d(TAG, "onGuidedActionClicked() click update  is_update_operation_enable:" + ConstantConfig.IS_UPDATE_OPERATION_ENABLE);

            mTaskHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (mUpdatePictureTask != null && mUpdatePictureTask.isDownloading()) {
                        ToastUtil.showToast(getString(R.string.updating_tips_text));
                        LogUtils.d(TAG, "onGuidedActionClicked() update click() isDownloading");
                    } else {
                        prepareUpdate();
                    }
                }
            });

        } else {
            ConstantConfig.IS_UPDATE_OPERATION_ENABLE = true;
            mTaskHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (mUpdatePictureTask != null) {
                        if (mUpdatePictureTask.isDownloading()) {
                            String tips = getString(R.string.update_build_in_pic_toast_tips);
                            ToastUtil.showToast(tips);
//                            ToastUtil.showToastTime(tips, 2000, new Handler(Looper.getMainLooper()));
                        } else {
                            getActivity().finish();
                        }
                    } else {
                        getActivity().finish();
                    }
                }
            });

            LogUtils.d(TAG, "onGuidedActionClicked()  click cancel");

//            if (mUpdatePictureTask != null) {
//                mUpdatePictureTask.stopCopy();
//                mUpdatePictureTask.cancel(true);
//            }
//            getActivity().finish();
        }
    }

    private void prepareUpdate() {

        List<UsbUtil.UsbPathAndLabel> mPaths = UsbUtil.getUsbPath();
        LogUtils.d(TAG, "prepareUpdate() usb storage parentPath size:" + mPaths.size());

        if (mPaths.size() == 0) {
            ToastUtil.showToast(getString(R.string.usb_not_found));
            return;
        }
        // usb storage resource list  (pic)
        mPicturePaths.clear();
        for (UsbUtil.UsbPathAndLabel usbPath : mPaths) {
            LogUtils.d(TAG, "prepareUpdate() usb storage parentPath label:" + usbPath.mLabel + "       path:" + usbPath.mPath);

            // FileUtil.getFilesPathByTypeAndName  type:pic and video
            List<String> paths = FileUtil.getFilesPathByTypeAndName(usbPath.mPath,
                    ConstantConfig.PIC_TYPE, ConstantConfig.BACKGROUND_PIC_NAME_PREFIX);

            if (paths.size() != 0) {//find success video resource
                mPicturePaths.addAll(paths);
            } else {
                LogUtils.d(TAG, "prepareUpdate() paths.size =0 in " + usbPath.mPath);
            }
        }

        LogUtils.d(TAG, "prepareUpdate() picture file mPicturePaths.size():" + mPicturePaths.size());
        if (mPicturePaths.size() > ConstantConfig.BUIDL_IN_PIC_UPDATE_MAX_NUMBER) {
            mPictureMaxNumber = ConstantConfig.BUIDL_IN_PIC_UPDATE_MAX_NUMBER;
        } else {
            mPictureMaxNumber = mPicturePaths.size();
        }
        LogUtils.d(TAG, "prepareUpdate() mPictureMaxNumber = " + mPictureMaxNumber);

        //if no picture update,interrupt update.
        if (mPictureMaxNumber == 0) {
            ToastUtil.showToast(String.format(getString(R.string.no_find_update_pic_file_tips), ConstantConfig.BACKGROUND_PIC_NAME_PREFIX));
            return;
        }

        if (!ConstantConfig.IS_UPDATE_OPERATION_ENABLE) {
            return;
        }
        ConstantConfig.IS_UPDATE_OPERATION_ENABLE = false;

        LogUtils.d(TAG, "prepareUpdate()  picture build-in path is :" + mToPath);
        File picturesFile = new File(mToPath);
        if (picturesFile.exists()) {
            LogUtils.d(TAG, "updatePicture() deleteFile");
            deleteFile(picturesFile);
        }
        if (!picturesFile.exists()) {
            LogUtils.d(TAG, "prepareUpdate()   !picturesFile.exists()");
            boolean created = picturesFile.mkdirs();
            LogUtils.d(TAG, "prepareUpdate()   created = " + created);
        }

        mFromPath = mPicturePaths.get(0);
        updatePicture(mFromPath, mToPath);
        Glide.get(StoreModeApplication.sContext).clearDiskCache();
    }

    private void updatePicture(String fromPath, String toPath) {

        LogUtils.d(TAG, "updatePicture() fromPath = " + fromPath + "; toPath= " + toPath);
//        if (mUpdatePictureTask == null) {
//            mUpdatePictureTask = new PictureUpdateTask();
//            mUpdatePictureTask.setLisener(this);
//            mUpdatePictureTask.execute(fromPath, toPath);
////            ToastUtil.showToast(getString(R.string.updating_tips_text));
//            LogUtils.d(TAG, "updatePicture() ");
//        } else {

//            mUpdatePictureTask = null;
        mUpdatePictureTask = new PictureUpdateTask();
        mUpdatePictureTask.setLisener(this);
        mUpdatePictureTask.execute(fromPath, toPath);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String desc = getString(R.string.prompt_build_in_picture_update);
                mUpdatePictureGuidanceStyList.setmSummary(desc);
                getActionItemView(0).setVisibility(View.GONE);
                Glide.get(StoreModeApplication.sContext).clearMemory();
            }
        });
//        }
    }


    private void deleteFile(File file) {
        LogUtils.d(TAG, "deleteFile()");

        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
            } else {
                File[] listFiles = file.listFiles();
                for (File file2 : listFiles) {
                    deleteFile(file2);
                }
            }
            file.delete();
        } else {
            LogUtils.d(TAG, "deleteFile() the file does not exists");
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        ConstantConfig.IS_UPDATE_OPERATION_ENABLE = true;
        if (mHandlerThread != null) {
            mHandlerThread.quit();
            mHandlerThread = null;
        }
        if (mUpdatePictureTask != null) {
            mUpdatePictureTask.stopCopy();//stop copy
            mUpdatePictureTask.cancel(true);
        }
    }

    @Override
    public void onProcessUpdate(Integer value) {

//        if (value == 100) {
//            mIsFinishDownload = true;
//        }
//        LogUtils.d("onProcessUpdate()  process = " + value);
    }

    @Override
    public void onCopySuccessed() {
        mCurrentNumber++;
        LogUtils.d("onCopySuccessed() mCurrentNumber = " + mCurrentNumber);
        mUpdatePictureGuidanceStyList.setProgress(mCurrentNumber, mPictureMaxNumber);

        if (mCurrentNumber < mPictureMaxNumber) {
            mFromPath = mPicturePaths.get(mCurrentNumber);
            updatePicture(mFromPath, mToPath);
        } else {
            EventBus.getDefault().post(new FinishEvent());
            startPlay();
            ToastUtil.showToast(getString(R.string.udate_success));
            LogUtils.d("onCopySuccessed() is invoked");
        }
    }


    @Override
    public void onCopyFailed() {
        //  startPlay();
        LogUtils.d("onCopyFailed() is invoked");
    }

    private void startPlay() {
        PreferenceUtils.getInstance().save(ConstantConfig.RESET_PALY_POLICY, true);
        StoreModeManager.getInstance().start();
        getActivity().finish();
    }

}
