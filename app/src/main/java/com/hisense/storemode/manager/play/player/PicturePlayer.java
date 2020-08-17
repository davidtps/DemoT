package com.hisense.storemode.manager.play.player;

import android.animation.ObjectAnimator;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.hisense.storemode.StoreModeApplication;
import com.hisense.storemode.manager.AppManager;
import com.hisense.storemode.manager.StoreModeManager;
import com.hisense.storemode.ui.fragment.PicFragment;
import com.hisense.storemode.utils.ConstantConfig;
import com.hisense.storemode.utils.GlideUtils;
import com.hisense.storemode.utils.LogUtils;
import com.hisense.storemode.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cuihuihui on 2019年04月22日 11时42分.
 */
public class PicturePlayer implements IPlayer {
    private final String TAG = "PicturePlayer";
    private final String PLAY_TYPE_STRING = "type_string";
    private final String PLAY_TYPE_INT = "type_int";
    private List<Object> mAllPicResource = new ArrayList<>();

    private String mPlayType;
    private String mPlantForm;
    private String mPictureFilePath;
    private int mPictureResourceId = -1;

    //  private AutoSwitchImageView mImageView;
    private CountDownTimer mTimer;
    private PicFragment mFragment;
    private FrameLayout mFrameLayout;
    private List<ImageView> mImageViews = new ArrayList<>();
    private int mIndex = 0;
    private ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(null, "alpha", 0f, 1f);
    private static final long AUTO_SWITCH_TIME = ConstantConfig.EACH_PIC_SHOW_TIME * 1000;
    //private Timer mTimer;
    ImageView mInto = null;
    private Handler mMainHandler;

    public void changePicture() {

        if (AppManager.getInstance().currentActivity() == null) {
            LogUtils.d(TAG, "changePicture() currentActivity =null");
            return;
        }
        AppManager.getInstance().currentActivity().runOnUiThread(() -> {
            if (mAllPicResource.size() == 1) {
                if (mFragment != null && !mFragment.isActive) {
                    return;
                }
            }
            mIndex++;
            if (mIndex >= mAllPicResource.size()) {
                // mIndex = 0;
                LogUtils.d(TAG, "changePicture() mFragment = " + mFragment + mFragment.isActive);
                if (mFragment != null && !mFragment.isActive) {
                    mIndex = 0;
                } else {
                    StoreModeManager.getInstance().start();
                    return;
                }
            }
//                if(mFragment!=null && !mFragment.isActive){
//                    return;
//                }

            LogUtils.d(TAG, "changePicture() 2 ");
            mInto = null;
            if (mImageViews.size() >= 2) {
                mInto = mImageViews.get(0);
                mImageViews.remove(mInto);
                mFrameLayout.removeView(mInto);
                mImageViews.add(mInto);
                if (mInto.getParent() != null) {
                    ViewGroup parent = (ViewGroup) mInto.getParent();
                    parent.removeAllViews();
                }
                // mFrameLayout.addView(mInto);
            } else {
                ImageView imageView = new ImageView(StoreModeApplication.sContext);
                imageView.setLayoutParams(new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                mInto = imageView;
                mImageViews.add(mInto);
                // mFrameLayout.addView(mInto);
            }
            // Glide.with().load(mDataList.get(mIndex)).mInto(mInto);
            if (mAllPicResource.get(mIndex) instanceof String) {
                String picturePath = (String) mAllPicResource.get(mIndex);
                GlideUtils.loadImage(picturePath, mInto);
            } else if (mAllPicResource.get(mIndex) instanceof Integer) {
                mPictureResourceId = (int) mAllPicResource.get(mIndex);
                String localPicRes = ConstantConfig.getStringUriFromDrawable(mPictureResourceId);
                GlideUtils.loadImageLocalDrawable(localPicRes, mInto);
//                mInto.setImageResource(mPictureResourceId);

            }
            mFrameLayout.addView(mInto);
            objectAnimator.setTarget(mInto);
            objectAnimator.start();

        });

    }

    public void initTimer() {
        LogUtils.d(TAG, "task start() ConstantConfig.EACH_PIC_SHOW_TIME = " + ConstantConfig.EACH_PIC_SHOW_TIME);
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }

        mTimer = new CountDownTimer(AUTO_SWITCH_TIME, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
//                    LogUtils.d(TAG, "countDownTimeLogo()  time down:" + (millisUntilFinished / 1000) + " thread id:" + Thread.currentThread().getId());

            }

            @Override
            public void onFinish() {
                LogUtils.d(TAG, "countDownTimeLogo()   time down finish to showLogo(): thread id:" + Thread.currentThread().getId());
                changePicture();
                mTimer.start();
                if (!ConstantConfig.isStoreMode())
                    cancel();
            }
        }.start();


    }


    public PicturePlayer(String platForm, String file) {
        LogUtils.d(TAG, "PicturePlayer(string)");

        mPlayType = PLAY_TYPE_STRING;
        mPictureFilePath = file;
        mPlantForm = platForm;
        mPictureResourceId = -1;
    }

    public PicturePlayer(String platForm, int id) {
        LogUtils.d(TAG, "PicturePlayer(int)");

        mPlayType = PLAY_TYPE_INT;
        mPictureResourceId = id;
        mPlantForm = platForm;
        mPictureFilePath = null;
    }

    public PicturePlayer(String platForm, List<Object> allPicRes) {
        LogUtils.d(TAG, "PicturePlayer(List<Object>)");
        mAllPicResource = allPicRes;
        mPlantForm = platForm;
    }

    public void setPath(List<Object> allPicRes) {
        LogUtils.d(TAG, "setPath() path.size = " + allPicRes.size());
        mAllPicResource = allPicRes;

    }

    @Override
    public void start(Object object) {
        LogUtils.d(TAG, "start() is invoked, mPlayType = [" + mPlayType + "]; mPlantForm = [" + mPlantForm + "]");
        if (object == null || !(object instanceof FrameLayout)) {
            LogUtils.d(TAG, "start(), object is NULL, or is not instanceof ImageView.");
            return;
        }
        //  mImageView = (AutoSwitchImageView) object;
        mFrameLayout = (FrameLayout) object;

        if (StringUtil.isEmpty(mPlantForm)) {
            LogUtils.d(TAG, "start(), mPlantForm is NULL.");
            StoreModeManager.getInstance().start();
            return;
        }

        if (mAllPicResource == null || mAllPicResource.size() == 0) {
            LogUtils.d(TAG, "start(),mAllPicResourc=null");
            StoreModeManager.getInstance().start();
            return;
        }
        switch (mPlantForm) {
            case ConstantConfig.PLANTFORM_NORMAL_PICTURE: {
                playPicture();
                break;
            }

            case ConstantConfig.PLANTFORM_4K_PICTURE: {
                play4K();
                break;
            }

            default: {
                LogUtils.d(TAG, "start(), unexpected value of mPlantForm = " + mPlantForm);
                break;
            }
        }
    }

    @Override
    public void stop() {
        LogUtils.d(TAG, " stop() is invoked.");
        if (StringUtil.isEmpty(mPlantForm)) {
            LogUtils.d(TAG, "stop(), mPlantForm is NULL.");
            return;
        }

        switch (mPlantForm) {
            case ConstantConfig.PLANTFORM_NORMAL_PICTURE: {
                stopPicture();
                break;
            }

            case ConstantConfig.PLANTFORM_4K_PICTURE: {
                stop4K();
                break;
            }

            default: {
                LogUtils.d(TAG, "stop(), unexpected value of mPlantForm = " + mPlantForm);
                break;
            }
        }
        if (mMainHandler == null) {
            mMainHandler = new Handler(Looper.getMainLooper());
        }
        mMainHandler.post(() -> {
            //clear imageviews list,release resource
            LogUtils.d(TAG, "stop()  clear imageviews list,release resource");
            for (ImageView imageView : mImageViews) {
                imageView.setImageDrawable(null);
                imageView = null;
            }
            mImageViews.clear();
        });
//        AppManager.getInstance().currentActivity().runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                //clear imageviews list,release resource
//                for (ImageView imageView : mImageViews) {
//                    imageView.setImageDrawable(null);
//                    imageView = null;
//                }
//                mImageViews.clear();
//            }
//        });
    }

    @Override
    public void playNext(Object newFilePath) {
        LogUtils.d(TAG, "playNext(), newFilePath = " + newFilePath);

        if (newFilePath instanceof String) {
            mPlayType = PLAY_TYPE_STRING;
            mPictureFilePath = (String) newFilePath;

        } else if (newFilePath instanceof Integer) {
            mPlayType = PLAY_TYPE_INT;
            mPictureResourceId = (int) newFilePath;
        }

        //   stop();
        //start(mFrameLayout);
    }

    private void playPicture() {

        if (mAllPicResource == null || mAllPicResource.size() == 0) {
            LogUtils.d(TAG, "playPicture() mAllPicResource = null || mAllPicResource.size() == 0 ");
            StoreModeManager.getInstance().start();
            return;
        }
        mIndex = 0;
        if (AppManager.getInstance().currentActivity() == null) {
            LogUtils.d(TAG, "playPicture() currentActivity =null");
            return;
        }
        AppManager.getInstance().currentActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ImageView imageView = new ImageView(StoreModeApplication.sContext);
                imageView.setLayoutParams(new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);

                if (mAllPicResource.get(mIndex) instanceof String) {
                    String picturePath = (String) mAllPicResource.get(mIndex);
                    GlideUtils.loadImage(picturePath, imageView);
                } else if (mAllPicResource.get(mIndex) instanceof Integer) {
                    mPictureResourceId = (int) mAllPicResource.get(mIndex);
                    String localPicRes = ConstantConfig.getStringUriFromDrawable(mPictureResourceId);
                    GlideUtils.loadImageLocalDrawable(localPicRes, imageView);
//                    imageView.setImageResource(mPictureResourceId);

                }
                mFrameLayout.addView(imageView);
                mImageViews.add(imageView);
                objectAnimator.setDuration(1000);
                objectAnimator.setTarget(imageView);
                objectAnimator.start();
            }
        });
        initTimer();
    }


    private void stopPicture() {
        LogUtils.d(TAG, "StopPicture()");
        if (mTimer != null) {
            mTimer.cancel();
        }
        mTimer = null;
        //mImageView.stopPlay();

    }

    public void setFragment(PicFragment fragment) {
        mFragment = fragment;
    }

    private void play4K() {
        //todo
    }

    private void stop4K() {
        //TODO:
    }
}