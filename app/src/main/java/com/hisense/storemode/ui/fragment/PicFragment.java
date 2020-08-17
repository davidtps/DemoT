package com.hisense.storemode.ui.fragment;

import android.widget.FrameLayout;

import com.hisense.storemode.R;
import com.hisense.storemode.manager.StoreModeManager;
import com.hisense.storemode.manager.play.PlayManager;
import com.hisense.storemode.manager.play.player.IPlayer;
import com.hisense.storemode.manager.play.player.PicturePlayer;
import com.hisense.storemode.utils.LogUtils;

import butterknife.BindView;

/**
 * Created by cuihuihui.
 */
public class PicFragment extends BaseFragment {
    private static final String TAG = "PicFragment";
    @BindView(R.id.fl_container)
    FrameLayout mFrameLayout;
    private IPlayer mPicturePlayer;
    //    private static final String PIC_STRING_PATH = "picStringPath";
//    private static final String PIC_INT_PATH = "picIntPath";
    public boolean isActive = false;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_pic;
    }

    @Override
    protected void onCreateView() {
        initData();
    }

//    public static PicFragment newInstance(Object path) {
//        LogUtils.d(TAG, "newInstance()");
//
//        Bundle bundle = new Bundle();
//        if (path instanceof String) {
//            LogUtils.d(TAG, "newInstance() path is string : " + path);
//            bundle.putString(PIC_STRING_PATH, (String) path);
//        } else if (path instanceof Integer) {
//            LogUtils.d(TAG, "newInstance() path is int : " + path);
//            bundle.putInt(PIC_INT_PATH, (int) path);
//        }
//
//        PicFragment picFragment = new PicFragment();
//        picFragment.setArguments(bundle);
//        return picFragment;
//    }

    private void initData() {
        LogUtils.d(TAG, "initData()");

//        Bundle bundle = getArguments();
//        if (bundle != null) {
//            String stringPath = getArguments().getString(PIC_STRING_PATH, "");
//            int intPath = getArguments().getInt(PIC_INT_PATH, -1);
//            LogUtils.d(TAG, "initData() stringPath = " + stringPath + "; intPath = " + intPath);
//        }

        mPicturePlayer = PlayManager.getInstance().getCurrentPlayer();

        if (!(mPicturePlayer instanceof PicturePlayer)) {
            LogUtils.d(TAG, "mPicturePlayer not instanceof PicturePlayer");
            StoreModeManager.getInstance().start();
            return;
        }

//        if (mAutoSwitchImageView != null) {
//            ((PicturePlayer)mPicturePlayer).setFragment(this);
//            mPicturePlayer.start(mAutoSwitchImageView);
//        }
        if (mFrameLayout != null) {
            ((PicturePlayer) mPicturePlayer).setFragment(this);
            mPicturePlayer.start(mFrameLayout);
        }

    }


    @Override
    public void onDestroy() {
        LogUtils.d(TAG, "onDestroy()");
//easy happen  black screen first pic --tps
//        if (mPicturePlayer instanceof PicturePlayer) {
//            mPicturePlayer.stop();
//        }
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    @Override
    public void onPause() {
        super.onPause();
        isActive = false;
    }

    @Override
    public void onResume() {
        super.onResume();
        isActive = true;
    }

//    public void playNext(Object path) {
//        if (path == null) {
//            return;
//        }
//
//        if (mPicturePlayer == null) {
//            mPicturePlayer = PlayManager.getInstance().getCurrentPlayer();
//        }
//
//        if (!(mPicturePlayer instanceof PicturePlayer)) {
//            StoreModeManager.getInstance().start();
//            return;
//        }
//        mPicturePlayer.playNext(path);
//    }

}
