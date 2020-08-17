package com.hisense.storemode.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import com.hisense.storemode.manager.StoreModeManager;
import com.hisense.storemode.ui.fragment.PicFragment;
import com.hisense.storemode.utils.ConstantConfig;
import com.hisense.storemode.utils.GlideUtils;
import com.hisense.storemode.utils.LogUtils;
import com.hisense.storemode.utils.PreferenceUtils;

import java.util.List;

import androidx.appcompat.widget.AppCompatImageView;

public class AutoSwitchImageView extends AppCompatImageView {
    private List<Object> images;
    private int index = 0;
    private MyTask task;
    //private boolean isCircle;
    private AutoSwitchImageView autoSwitchImageView;
    private final String TAG = "AutoSwitchImageView";
    private PicFragment mFragment;

    private static final long AUTO_SWITCH_TIME = ConstantConfig.EACH_PIC_SHOW_TIME * 1000;

    public AutoSwitchImageView(Context context) {
        super(context, null);
        autoSwitchImageView = this;

    }

    public AutoSwitchImageView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        autoSwitchImageView = this;

    }

    public AutoSwitchImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        autoSwitchImageView = this;
    }

    public void stopPlay() {
        LogUtils.d(TAG, "stopPlay()");
        if (task != null) {
            removeCallbacks(task);
            task = null;
        }
    }

    public void setFragemnt(PicFragment fragemnt) {
        mFragment = fragemnt;
    }

    public class MyTask implements Runnable {


        @Override
        public void run() {
            if (index == images.size() - 1) {

                LogUtils.d(TAG, "AutoSwitchImageView1");
                if (mFragment != null && mFragment.isActive) {
                    LogUtils.d(TAG, "mFragment.isActive");
                    stopPlay();
                    StoreModeManager.getInstance().start();

                    return;
                } else {
                    LogUtils.d(TAG, "!mFragment.isActive");
                    if (images.size() == 1) {
                        LogUtils.d(TAG, "images.size() == 1");
                        postDelayed(this, AUTO_SWITCH_TIME);
                    } else {
                        LogUtils.d(TAG, "images.size() != 1");
                        index = -1;
                        switchWithAnim(this);
                    }
                }
            } else {
                switchWithAnim(this);
            }
            LogUtils.d(TAG, "AutoSwitchImageView2");

        }

        public void start() {
            LogUtils.d(TAG, "task start() ConstantConfig.EACH_PIC_SHOW_TIME = " + ConstantConfig.EACH_PIC_SHOW_TIME);
            postDelayed(this, AUTO_SWITCH_TIME);
        }
    }

    /**
     * 执行动画实现图片的切换
     *
     * @param
     */
    public void switchWithAnim(MyTask tasks) {
        LogUtils.d(TAG, "switchWithAnim()");

        AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0.2f);
        alphaAnimation.setFillAfter(true);
        alphaAnimation.setDuration(500);
        startAnimation(alphaAnimation);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                LogUtils.d(TAG, "onAnimationEnd() index = " + index + "length = " + images.size());

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                LogUtils.d(TAG, "switchWithAnim() onAnimationEnd");

                if (index == images.size() - 1) {
                    //index = -1;
                    LogUtils.d(TAG, "onAnimationEnd()2");
                    if (mFragment != null && mFragment.isActive) {
                        stopPlay();
                        StoreModeManager.getInstance().start();
                        return;
                    } else {
                        index = -1;
                    }
                }

                boolean needReset = (boolean) PreferenceUtils.getInstance().get(ConstantConfig.RESET_PALY_POLICY, false);
                LogUtils.d(TAG, "switchWithAnim needReset = " + needReset);
                if (needReset) {
                    if (mFragment != null && mFragment.isActive) {
                        stopPlay();
                        StoreModeManager.getInstance().start();
                        return;
                    }
                }

                Object image = images.get(++index);
                if (image instanceof Integer) {
                    setImageResource((int) (image));
                } else if (image instanceof String) {
                    GlideUtils.loadLocalImageForAutoSwitch(autoSwitchImageView, (String) image);
                }

                AlphaAnimation anim = new AlphaAnimation(0.2f, 1);
                anim.setDuration(500);
                anim.setFillAfter(true);
                startAnimation(anim);
                anim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        LogUtils.d(TAG, "switchWithAnim() onAnimationEnd1");
                        postDelayed(tasks, AUTO_SWITCH_TIME);

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    /**
     * 设置图片数据
     *
     * @param
     * @return
     */
    public AutoSwitchImageView setImages(List<Object> images) {
        LogUtils.d(TAG, "setImages()");
        this.images = images;

        if (images != null && images.size() > 0) {
            Object image = images.get(index);
            if (image instanceof Integer) {
                setImageResource((int) (image));
            } else if (image instanceof String) {
                GlideUtils.loadLocalImageForAutoSwitch(autoSwitchImageView, (String) image);
            }
        }
        return this;
    }

    /**
     * 启动线程，执行任务
     */
    public void startTask() {
        LogUtils.d(TAG, "startTask()");
        if (images == null || images.size() == 0) {
            return;
        }
        if (task != null) {
            removeCallbacks(task);
            task = null;
        }
        task = new MyTask();
        task.start();
    }

    /**
     * 设置是否循环切换图片
     *
     * @param isCircle
     * @return
    //     */
//    public AutoSwitchImageView setIsCircle(boolean isCircle) {
//        this.isCircle = isCircle;
//        return this;
//    }
}
