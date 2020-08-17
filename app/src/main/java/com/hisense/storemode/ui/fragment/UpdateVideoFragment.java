package com.hisense.storemode.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.View;

import com.hisense.hianidraw.cache.SeriesUtils;
import com.hisense.storemode.R;
import com.hisense.storemode.bean.FinishEvent;
import com.hisense.storemode.manager.StoreModeManager;
import com.hisense.storemode.manager.resource.FileCopyCoverTask;
import com.hisense.storemode.utils.ConstantConfig;
import com.hisense.storemode.utils.FileUtil;
import com.hisense.storemode.utils.LogUtils;
import com.hisense.storemode.utils.PreferenceUtils;
import com.hisense.storemode.utils.StringUtil;
import com.hisense.storemode.utils.ToastUtil;
import com.hisense.storemode.utils.UsbUtil;
import com.hisense.storemode.widget.UpdateVideoGuidanceStyList;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.leanback.app.GuidedStepFragment;
import androidx.leanback.widget.GuidanceStylist;
import androidx.leanback.widget.GuidedAction;

/**
 * @author tianpengsheng
 * create at   5/8/19 4:05 PM
 */
public class UpdateVideoFragment extends GuidedStepFragment implements FileCopyCoverTask.FileCopyListener {
    private static final String TAG = "UpdateVideoFragment";
    private static final int ACTION_ID_POSITIVE = 1;
    private static final int ACTION_ID_NEGATIVE = ACTION_ID_POSITIVE + 1;
    private static final int CLICK_DURATION_TIME = 500;

    private FileCopyCoverTask mUpdateVideoTask;
    private UpdateVideoGuidanceStyList mUpdateViewGuidanceStyList;
    private HandlerThread mHandlerThread;
    private Handler mTaskHandler;

    @NonNull
    @Override
    public GuidanceStylist.Guidance onCreateGuidance(Bundle savedInstanceState) {
        String title = getString(R.string.video_update_title);

        if (mHandlerThread == null) {
            mHandlerThread = new HandlerThread(TAG);
            mHandlerThread.start();
        }
        if (mTaskHandler == null) {
            mTaskHandler = new Handler(mHandlerThread.getLooper());
        }

        return new UpdateVideoGuidanceStyList.Guidance(title, "", "", null);
    }

    @Override
    public GuidanceStylist onCreateGuidanceStylist() {
        mUpdateViewGuidanceStyList = new UpdateVideoGuidanceStyList();
        return mUpdateViewGuidanceStyList;
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

            mTaskHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (mUpdateVideoTask != null && mUpdateVideoTask.isDownloading()) {
                        LogUtils.d(TAG, "is updating  please wait a moment!");
                        ToastUtil.showToast(getString(R.string.updating_tips_text));
                    } else {
                        updateVideo();
                    }
                }
            });


        } else {
            mTaskHandler.post(new Runnable() {
                @Override
                public void run() {

                    if (mUpdateVideoTask != null) {
                        if (mUpdateVideoTask.isDownloading()) {
                            LogUtils.d(TAG, "onGuidedActionClicked()-- mUpdateVideoTask.isDownloading() :" + mUpdateVideoTask.isDownloading());
                            String tips = getString(R.string.update_build_in_video_toast_tips);
                            ToastUtil.showToast(tips);
//                            getActionItemView(1).setEnabled(false);
//                            ToastUtil.showToastTime(tips, 2000, new Handler(Looper.getMainLooper()));
                        } else {
                            LogUtils.d(TAG, "onGuidedActionClicked()-- mUpdateVideoTask.isDownloading() :" + mUpdateVideoTask.isDownloading());
                            getActivity().finish();
                        }
                    } else {
                        getActivity().finish();
                    }
                }
            });
        }
    }


    private void updateVideo() {
        String fromPath = "";
        String toPath = ConstantConfig.getCurrentVideoBuildInPath();//ConstantConfig.VIDEO_BUILD_IN_UPDATE_PATH;

        List<UsbUtil.UsbPathAndLabel> mPaths = UsbUtil.getUsbPath();
        LogUtils.d(TAG, "updateVideo()  usb storage parentPath size:" + mPaths.size());

        if (mPaths.size() == 0) {
            ToastUtil.showToast(getString(R.string.usb_not_found));
            return;
        }
        // usb storage resource list  (mp4 or pic)
        for (UsbUtil.UsbPathAndLabel usbPath : mPaths) {
            LogUtils.d(TAG, "updateVideo()  usb storage parentPath label:" + usbPath.mLabel + "       path:" + usbPath.mPath);

            // FileUtil.getFilesPathByTypeAndName  type:pic and video
            List<String> paths = FileUtil.getFilesPathByTypeAndNameEquals(usbPath.mPath,
                    ConstantConfig.VIDEO_TYPE, ConstantConfig.VIDEO_USB_UPDATE_NAME + "-" + ConstantConfig.VIDEO_USB_UPDATE_NAME2);

            if (paths.size() != 0) {//find success video resource
//                for (int i = 0; i < paths.size(); i++) {
//                    if (FileTypeUtil.isVideoTypeFile(new File(paths.get(i)))) {
//                        fromPath = paths.get(i);
//                    }
//                }
                fromPath = paths.get(0);
                LogUtils.d(TAG, "updateVideo() video file fromPath name:" + fromPath);
            } else {
                ToastUtil.showToast(String.format(getString(R.string.not_find_update_file_tips2),
                        ConstantConfig.VIDEO_USB_UPDATE_NAME2, ConstantConfig.VIDEO_USB_UPDATE_NAME));
            }
        }

        if (StringUtil.isNotEmpty(fromPath)) {
            Long fileSize = FileUtil.getFileSizeUnitM(fromPath);
            LogUtils.d(TAG, "updateVideo() file size = " + fileSize);
            if (fileSize > SeriesUtils.UPDATE_VIDEO_FILE_MAX_SIZE) {
                ToastUtil.showToast(String.format(
                        getString(R.string.update_video_max_size_tips_text), fileSize,
                        SeriesUtils.UPDATE_VIDEO_FILE_MAX_SIZE));
                return;
            }
            if (ConstantConfig.IS_UPDATE_OPERATION_ENABLE) {
                ConstantConfig.IS_UPDATE_OPERATION_ENABLE = false;
                mUpdateVideoTask = new FileCopyCoverTask();
                mUpdateVideoTask.setLisener(this);
                mUpdateVideoTask.execute(fromPath, toPath);
                ToastUtil.showToast(getString(R.string.updating_tips_text));
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String desc = getString(R.string.prompt_build_in_video_update);
                        mUpdateViewGuidanceStyList.setmSummary(desc);
                        getActionItemView(0).setVisibility(View.GONE);
                    }
                });
            }
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
        if (mUpdateVideoTask != null) {
            mUpdateVideoTask.stopCopy();//stop copy
            mUpdateVideoTask.cancel(true);
        }
    }

    @Override
    public void onProcessUpdate(Integer value) {
        mUpdateViewGuidanceStyList.setProgress(value);
        LogUtils.d("onProcessUpdate()  process = " + value + "    task:" + mUpdateVideoTask);
    }

    @Override
    public void onCopySuccessed() {
        EventBus.getDefault().post(new FinishEvent());
        startPlay();
        ToastUtil.showToast(getString(R.string.udate_success));
        LogUtils.d("onCopySuccessed() is invoked");
    }


    @Override
    public void onCopyFailed() {
//        startPlay();
        LogUtils.d("onCopyFailed() is invoked");
    }

    private void startPlay() {
        PreferenceUtils.getInstance().save(ConstantConfig.RESET_PALY_POLICY, true);
        StoreModeManager.getInstance().start();
        getActivity().finish();
    }

}
