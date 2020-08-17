package com.hisense.storemode.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;

import com.hisense.storemode.R;
import com.hisense.storemode.StoreModeApplication;
import com.hisense.storemode.manager.AppManager;
import com.hisense.storemode.manager.StoreModeManager;
import com.hisense.storemode.manager.play.PlayManager;
import com.hisense.storemode.manager.play.player.IPlayer;
import com.hisense.storemode.manager.play.player.videoplayer.VideoPlayer;
import com.hisense.storemode.utils.LogUtils;
import com.hisense.storemode.utils.StringUtil;

import butterknife.BindView;

/**
 * Created by cuihuihui .
 */
public class VideoFragment extends BaseFragment {
    @BindView(R.id.surfaceView)
    SurfaceView mSurfaceView;
    @BindView(R.id.load)
    FrameLayout mLoadFL;
    private SurfaceHolder mSurfaceHolder;
    private static final String TAG = "VideoFragment";
    private IPlayer mPlayer;
    private boolean mIsSurfaceCreated;
    public boolean mIsActive = false;
    private static final String VIDEO_PATH = "videoPath";
    private View mView;

    private SurfaceHolder.Callback mSHCallback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            LogUtils.d(TAG, "mSHCallback mSHCallback.surfaceCreated");
            mSurfaceHolder = holder;
            mIsSurfaceCreated = true;

            if (mPlayer == null) {
                mPlayer = PlayManager.getInstance().getCurrentPlayer();
            }
            if (mSurfaceHolder != null && mPlayer instanceof VideoPlayer) {
                showLoading(true);
                mPlayer.start(mSurfaceHolder);
            } else {
                StoreModeManager.getInstance().start();
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            LogUtils.d(TAG, "mSHCallback mSHCallback.surfaceChanged");

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            LogUtils.d(TAG, "mSHCallback mSHCallback.surfaceDestroyed");
            mIsSurfaceCreated = false;
            if (PlayManager.getInstance().getCurrentPlayer() instanceof VideoPlayer) {
                createSurface();
            }

        }
    };

    @Override
    public void onResume() {
        LogUtils.d(TAG, " onResume()");
        super.onResume();
        mIsActive = true;

        if (!mIsSurfaceCreated) {
            createSurface();
        }

        if (mPlayer instanceof VideoPlayer) {
            LogUtils.d(TAG, " initData() mPlayer instanceof VideoPlayer");
            ((VideoPlayer) mPlayer).setFragment(this);
        } else {
            StoreModeManager.getInstance().start();
        }
    }

    public static VideoFragment newInstance(String path) {
        LogUtils.d(TAG, "newInstance() : " + path);

        Bundle bundle = new Bundle();
        bundle.putString(VIDEO_PATH, path);

        VideoFragment videoFragment = new VideoFragment();
        videoFragment.setArguments(bundle);
        return videoFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_video;
    }

    @Override
    protected void onCreateView() {
        initView();
        initData();
    }

    @Override
    public void onPause() {
        super.onPause();
        LogUtils.d(TAG, "onPause");
        mIsActive = false;
    }

    @Override
    public void onStart() {
        super.onStart();
        LogUtils.d(TAG, "onStart");
        if (!mIsSurfaceCreated) {
            createSurface();
        } else {
            LogUtils.d(TAG, "onStart IsSurfaceCreated");
            if (mSurfaceHolder != null && mPlayer instanceof VideoPlayer) {
                showLoading(true);
                mPlayer.start(mSurfaceHolder);
            } else {
                StoreModeManager.getInstance().start();
            }
        }
    }

    private void createSurface() {
        LogUtils.d(TAG, "createSurface()");

        if (mSurfaceView != null) {
            mSurfaceView.getHolder().removeCallback(mSHCallback);
            mSurfaceView.getHolder().addCallback(mSHCallback);
            mSurfaceView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }

    }

    @Override
    public void onDestroy() {
        LogUtils.d(TAG, "onDestroy()");
        super.onDestroy();
    }

    @Override
    public void onStop() {
        super.onStop();
        LogUtils.d(TAG, "onStop()");
        if (mPlayer instanceof VideoPlayer) {
            mPlayer.stop();
            mPlayer = null;
        }
    }


    private void initData() {
        LogUtils.d(TAG, "initData()");

        mPlayer = PlayManager.getInstance().getCurrentPlayer();
        if (!(mPlayer instanceof VideoPlayer)) {
            StoreModeManager.getInstance().start();
            return;
        }

        LogUtils.d(TAG, " initData() mPlayer instanceof VideoPlayer");
        ((VideoPlayer) mPlayer).setFragment(this);

        Bundle bundle = getArguments();
        if (bundle != null) {
            String stringPath = bundle.getString(VIDEO_PATH, "");
            if (!StringUtil.isEmpty(stringPath)) {
                playNext(stringPath);
            }
        }

    }

    private void initView() {
        LogUtils.d(TAG, "initView()");
        mView = LayoutInflater.from(StoreModeApplication.sContext).inflate(R.layout.fragment_video, null);
        showLoading(true);
        createSurface();
    }

    public void showLoading(boolean show) {
        if (AppManager.getInstance().currentActivity() == null) {
            LogUtils.d(TAG, "showLoading() currentActivity =null");
            return;
        }
        AppManager.getInstance().currentActivity().runOnUiThread(() -> {

            if (mLoadFL == null) {
                LogUtils.d(TAG, "showLoading() mLoadFL =null");
                mLoadFL = (FrameLayout) mView.findViewById(R.id.load);
            }
            LogUtils.d(TAG, "showLoading() 2 show =" + show);
            if (show) {
                mLoadFL.setVisibility(View.VISIBLE);
                mLoadFL.bringToFront();
            } else {
                mLoadFL.setVisibility(View.INVISIBLE);
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    public void playNext(Object newFilePath) {
        LogUtils.d(TAG, "playNext(), newFilePath = " + newFilePath);
        showLoading(true);

        if (!(newFilePath instanceof String)) {
            LogUtils.d(TAG, "playNext(), newFilePath is not String.");
            return;
        }
        if (mPlayer == null) {
            mPlayer = PlayManager.getInstance().getCurrentPlayer();
        }

        if (!(mPlayer instanceof VideoPlayer)) {
            StoreModeManager.getInstance().start();
            return;
        }
        mPlayer.playNext(newFilePath);

        if (!mIsSurfaceCreated) {
            createSurface();
        } else {
            mPlayer.start(mSurfaceHolder);
        }
    }

}




