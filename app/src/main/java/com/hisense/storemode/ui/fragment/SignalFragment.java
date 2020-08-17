package com.hisense.storemode.ui.fragment;

import android.content.Context;
import android.media.tv.TvInputInfo;
import android.media.tv.TvInputManager;
import android.media.tv.TvView;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.hisense.storemode.BuildConfig;
import com.hisense.storemode.R;
import com.hisense.storemode.StoreModeApplication;
import com.hisense.storemode.manager.AppManager;
import com.hisense.storemode.manager.StoreModeManager;
import com.hisense.storemode.manager.epos.EPosManager;
import com.hisense.storemode.manager.play.PlayManager;
import com.hisense.storemode.manager.play.player.IPlayer;
import com.hisense.storemode.manager.play.player.SignalPlayer;
import com.hisense.storemode.utils.ConstantConfig;
import com.hisense.storemode.utils.LogUtils;
import com.hisense.storemode.utils.ScreenUtils;
import com.hisense.storemode.utils.WindowManagerUtils;
import com.mediatek.twoworlds.tv.MtkTvInputSource;
import com.mediatek.twoworlds.tv.MtkTvTVCallbackHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import butterknife.BindView;

import static android.media.tv.TvInputInfo.TYPE_COMPOSITE;

/**
 * Created by cuihuihui.
 */
public class SignalFragment extends BaseFragment {

    private static final String TAG = "SignalFragment";
    private final static int SVCTX_NTFY_CODE_SIGNAL_LOSS = 5;
    private final static int BANNER_MSG_LOCK_CH = 2;
    private final static int BANNER_MSG_LOCK_PROG = 3;
    private final static int BANNER_MSG_LOCK_INP = 4;
    private final static int SIGNAL_PLAY_DELAY_TIME = 1000;//ms
    private IPlayer mSignalPlayer;
    private TvInputManager mTvInputManager = null;
    private List<TvInputInfo> mSourceList = new ArrayList<TvInputInfo>();
    public boolean isSignalLoss = false;
    private String mTvPackageName = "com.hisense.tv.hitvinput";
    TextView mSource;
    TextView mSignalName;
    TextView mSignalTime;
    TextView mTunerMode;
    TextView mSourceBlank;
    TextView mSourceSpace;

    @BindView(R.id.tv_view)
    TvView mTvView;

    public MtkTvTVCallbackHandler mSignalCallbackHandler;
    public static boolean mIsActive;
    private TvInputInfo mCurrentSignal;
    private WindowManager.LayoutParams mTVInfoBarLayoutParams;
    private WindowManager mWindowManager;
    public static View mInfoView;
    private int mTipsShowTime = 10;//second
    private Handler handler = new Handler();
    private Handler mMainUIHandler;
    private SignalPlayRunnable mSignalPlayRunnable;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_signal;
    }

    TvInputManager.TvInputCallback tvInputCallback = new TvInputManager.TvInputCallback() {
        @Override
        public void onInputStateChanged(String inputId, int state) {
            super.onInputStateChanged(inputId, state);
            Log.e(TAG, "onInputStateChanged: inputId =" + inputId + ";state=" + state);
            if (state != 0) {
                Log.e(TAG, "onInputStateChanged: mCurrentSignal.getId() =" + mCurrentSignal.getId());
                if (inputId.equals(mCurrentSignal.getParentId())) {//hdmi signal inputId is parentid
                    dealWithInputLoss(mCurrentSignal.getParentId());
                    dealWithInputLoss(mCurrentSignal.getId());
                }
                if (inputId.equals(mCurrentSignal.getId())) {
                    dealWithInputLoss(inputId);
                }
            }
        }


    };


    @Override
    protected void onCreateView() {
        LogUtils.d(TAG, "onCreateView()");
    }

    @Override
    public void onStart() {
        super.onStart();
        LogUtils.d(TAG, "onStart()");
        intTVInfoBarView();

        mSignalPlayer = PlayManager.getInstance().getCurrentPlayer();
        mTvInputManager = (TvInputManager) StoreModeApplication.sContext
                .getSystemService(Context.TV_INPUT_SERVICE);

        LogUtils.d(TAG, "onCreateView 1");
        initSiganlListener();
        LogUtils.d(TAG, "onCreateView 2");
        mTvInputManager.registerCallback(tvInputCallback, handler);
        mSourceList = getAllConnectedList();
        if (mSourceList == null || mSourceList.size() == 0) {
            LogUtils.d(TAG, "mSourceList==null || mSourceList.size() ==0");
            StoreModeManager.getInstance().start();
            return;
        }

        removeGoogleFromeSource();
        removeNotHiTvInput();
        sortSource();

        for (int i = 0; i < mSourceList.size(); i++) {
            LogUtils.d(TAG, "mSourceList2 i=" + i + " type = " + mSourceList.get(i).getType() + ", id = " + mSourceList.get(i).getId());
            LogUtils.d(TAG, "mSourceList2 i=" + i + "  = " + mSourceList.get(i).getServiceInfo());
        }

        if (mSourceList == null || mSourceList.size() == 0) {
            LogUtils.d(TAG, "no connected source");
//            PreferenceUtils.getInstance().save(ConstantConfig.RESET_PALY_POLICY, true);
            StoreModeManager.getInstance().start();
            return;
        }
        String current = MtkTvInputSource.getInstance().getCurrentInputSourceName();
        //String current = MtkTvInputSource.getInstance().getI();
        Log.e(TAG, "current: " + current);

        startPlay();
    }


    private void intTVInfoBarView() {
        if (mInfoView == null) {
            mInfoView = LayoutInflater.from(getContext()).inflate(R.layout.widget_tv_info_bar_layout, null);
        }
        LogUtils.d(TAG, "intTVInfoBarView()  mInfoView:" + mInfoView);
        mSource = mInfoView.findViewById(R.id.img_source);
        mSignalName = mInfoView.findViewById(R.id.banner_name);
        mSignalTime = mInfoView.findViewById(R.id.banner_simple_time);
        mTunerMode = mInfoView.findViewById(R.id.banner_tuner_mode);
        mSourceBlank = mInfoView.findViewById(R.id.txt_source_blank);
        mSourceSpace = mInfoView.findViewById(R.id.txt_source_space);
    }

    /**
     * remove google signal from sourceList
     */
    private void removeGoogleFromeSource() {
        List<TvInputInfo> googleList = new ArrayList<TvInputInfo>();
        for (int i = 0; i < mSourceList.size(); i++) {
            LogUtils.d(TAG, "mSourceList1 i=" + i + " type = " + mSourceList.get(i).getType() + ", id = " + mSourceList.get(i).getId());
            TvInputInfo inputInfo = mSourceList.get(i);
            String tvId = inputInfo.getId();
            if (tvId.toLowerCase().contains("google")) {
                googleList.add(inputInfo);
            }
        }
        mSourceList.removeAll(googleList);
    }

    /**
     * remove the signal without mtk package  from sourceList
     */
    private void removeNotHiTvInput() {
        if (ConstantConfig.SERIES_US_A6101EU.equals(BuildConfig.CURR_PLATFORM)) {
            mTvPackageName = "com.mediatek.tvinput";
        }
        LogUtils.e(TAG, "mTvPackageName:" + mTvPackageName);
        List<TvInputInfo> notTvList = new ArrayList<TvInputInfo>();

        for (int j = 0; j < mSourceList.size(); j++) {
            TvInputInfo inputInfo = mSourceList.get(j);
            LogUtils.d(TAG, "mSourceList removeNotHiTvInput j=" + j + " type = " + mSourceList.get(j).getType() + ", id = " + mSourceList.get(j).getId());
            if (inputInfo.getType() == TvInputInfo.TYPE_TUNER) {
                String tvId = inputInfo.getId();
                if (!tvId.contains(mTvPackageName)) {
                    notTvList.add(inputInfo);
                }
            }
//            if (inputInfo.getId().contains("HW4")) { //for test
//                notTvList.add(inputInfo);
//            }
        }
        mSourceList.removeAll(notTvList);
    }


    /**
     * sort the signal with the same type from sourceList
     */
    private void sortSource() {
        for (int m = 0; m < mSourceList.size(); m++) {
            for (int n = m + 1; n < mSourceList.size(); n++) {
                TvInputInfo source1 = mSourceList.get(m);
                TvInputInfo source2 = mSourceList.get(n);
                if (source1.getType() == source2.getType()) {
                    String id1 = source1.getId();
                    String id2 = source2.getId();
                    try {
                        String nubmer1 = id1.substring(id1.length() - 1, id1.length());
                        String nubmer2 = id2.substring(id2.length() - 1, id2.length());
                        if (Integer.valueOf(nubmer1) > Integer.valueOf(nubmer2)) {
                            mSourceList.set(m, source2);
                            mSourceList.set(n, source1);
                        }
                    } catch (NumberFormatException e) {
                        continue;
                    }

                }
            }
        }

    }

    public void initSiganlListener() {
        if (mSignalCallbackHandler == null) {
            mSignalCallbackHandler = new MtkTvTVCallbackHandler() {
                @Override
                public int notifySvctxNotificationCode(int code) throws RemoteException {
                    LogUtils.d(TAG, "MtkTvTVCallbackHandler   notifySvctxNotificationCode=" + code);

                    if (code == SVCTX_NTFY_CODE_SIGNAL_LOSS) {
                        if (mSignalPlayer instanceof SignalPlayer) {
                            mSignalPlayer.stop();
                        }
                        LogUtils.d(TAG, "dealwithloss   notifySvctxNotificationCode:5");
                        dealWithLoss();
                    }
                    return 0;
                }

                @Override
                public int notifyConfigMessage(int notifyId, int data) throws RemoteException {
                    LogUtils.e(TAG, "MtkTvTVCallbackHandler   notifyConfigMessage_data=" + data);
                    return 0;
                }

                @Override
                public int notifyBannerMessage(int msgType, int msgName, int argv2, int argv3) {
                    LogUtils.e(TAG, "MtkTvTVCallbackHandler   notifyBannerMessage =" + msgName + "  msgType:" + msgType);
                    if (msgType == 0) {
                        if (BANNER_MSG_LOCK_CH == msgName || BANNER_MSG_LOCK_INP == msgName || BANNER_MSG_LOCK_PROG == msgName) {
                            LogUtils.e(TAG, "MtkTvTVCallbackHandler   notifyBannerMessage = start next resources" + msgName);
                            StoreModeManager.getInstance().start();
                        }
                    }
                    return 0;
                }

                @Override
                public int notifyChannelListUpdateMsg(int condition, int reason, int data) {
                    LogUtils.e(TAG, "MtkTvTVCallbackHandler   notifyChannelListUpdateMsg =" + reason);

                    return 0;
                }

            };
        }
    }

    public void dealWithTvLoss() {
        LogUtils.d(TAG, "dealWithTvLoss() TYPE_TUNER ");
        removeFromSource(TvInputInfo.TYPE_TUNER);
        LogUtils.d(TAG, "dealWithTvLoss() mSourceList.size() = " + mSourceList.size());
        if (mSourceList.size() == 0) {
            if (mSignalCallbackHandler != null) {
                mSignalCallbackHandler.removeListener();
            }
            isSignalLoss = true;
            LogUtils.d(TAG, "SignalCallbackHandler removeListener");
            StoreModeManager.getInstance().start();
        } else {
            isSignalLoss = false;
            startPlay();

        }
    }

    public void dealWithInputLoss(String inputId) {
        LogUtils.d(TAG, "dealWithInputLoss()  inputId:" + inputId);
        if (mSourceList.size() != 0) {
            removeInputFromSource(inputId);
        }
        if (mSourceList.size() == 0) {
            isSignalLoss = true;
            LogUtils.e(TAG, "dealWithInputLoss()   start next playpolicy");
            StoreModeManager.getInstance().start();
        } else {
            LogUtils.e(TAG, "dealWithInputLoss()   startPlay  signal");
            isSignalLoss = false;
            startPlay();
        }
    }

    public void removeInputFromSource(String inputId) {
        List<TvInputInfo> tvList = new ArrayList<TvInputInfo>();
        for (int i = 0; i < mSourceList.size(); i++) {
            if (mSourceList.get(i).getId().equals(inputId)) {
                tvList.add(mSourceList.get(i));
            }
        }
        mSourceList.removeAll(tvList);
        if (ConstantConfig.LOG_SWITCH) {
            for (int i = 0; i < mSourceList.size(); i++) {
                LogUtils.d(TAG, "removeInputFromSource()  mSourceList4() i=  " + i + "id = " + mSourceList.get(i).getId() + "; type = " + mSourceList.get(i).getType());
            }
        }
    }

    public void removeFromSource(int type) {
        List<TvInputInfo> tvList = new ArrayList<TvInputInfo>();
        for (int i = 0; i < mSourceList.size(); i++) {
            if (mSourceList.get(i).getType() == type) {
                tvList.add(mSourceList.get(i));
            }
        }
        mSourceList.removeAll(tvList);
        if (ConstantConfig.LOG_SWITCH) {
            for (int i = 0; i < mSourceList.size(); i++) {
                LogUtils.d(TAG, "mSourceList3() i=  " + i + "id = " + mSourceList.get(i).getId() + "; type = " + mSourceList.get(i).getType());
            }
        }
    }

    public void dealWithLoss() {
        LogUtils.d(TAG, "dealWithLoss() is invoke()");
        if (mCurrentSignal.getType() == TvInputInfo.TYPE_TUNER) {
            dealWithTvLoss();
        } else {
            dealWithInputLoss(mCurrentSignal.getId());
        }
    }

    public void startPlay() {
        if (mMainUIHandler == null) {
            mMainUIHandler = new Handler(Looper.getMainLooper());
        }
        if (mSignalPlayRunnable == null) {
            mSignalPlayRunnable = new SignalPlayRunnable();
        }

        mMainUIHandler.postDelayed(mSignalPlayRunnable, SIGNAL_PLAY_DELAY_TIME);
    }


    public void setSignalInfoBarViewVisible(boolean show) {
        LogUtils.d(TAG, "setSignalInfoBarViewVisible() show:" + show);
        if (show && ConstantConfig.isStoreModeTop()) {
            removeInfoView();
            WindowManagerUtils.getInstance().getWrapLayoutParams().x = ScreenUtils.getDpToPx(EPosManager.MALLLOGO_TOP_AND_RIGHT_INTERVAL);
            WindowManagerUtils.getInstance().getWrapLayoutParams().y = ScreenUtils.getDpToPx(EPosManager.MALLLOGO_TOP_AND_RIGHT_INTERVAL);
            LogUtils.d(TAG, "setSignalInfoBarViewVisible() is invoke before  isAttachedToWindow():" + mInfoView.isAttachedToWindow() + "       mInfoView.getParent():" + mInfoView.getParent());
            WindowManagerUtils.getInstance().getWindowManager().addView(mInfoView, WindowManagerUtils.getInstance().getWrapLayoutParams());
            LogUtils.d(TAG, "setSignalInfoBarViewVisible() is invoke after  isAttachedToWindow():" + mInfoView.isAttachedToWindow() + "       mInfoView.getParent():" + mInfoView.getParent());
            countDownTimeRemoveInfoBar();
        } else {
            removeInfoView();
        }

    }

    private void removeInfoView() {
        LogUtils.d(TAG, "removeInfoView() is invoke mInfoView:" + mInfoView);
        if (mInfoView != null) {
            LogUtils.d(TAG, "removeInfoView() is invoke mInfoView.isAttachedToWindow():" + mInfoView.isAttachedToWindow() + "       mInfoView.getParent():" + mInfoView.getParent());
        }
        if (mInfoView != null && (mInfoView.isAttachedToWindow() || mInfoView.getParent() != null)) {
            LogUtils.d(TAG, "removeInfoView()   remove view!!");
            WindowManagerUtils.getInstance().getWindowManager().removeView(mInfoView);
        }
    }

    private void countDownTimeRemoveInfoBar() {
        new CountDownTimer(mTipsShowTime * 1000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                LogUtils.d(TAG, "countDownTimeRemoveInfoBar() time onFinish.");
                removeInfoView();
            }
        }.start();

    }

    public void setTunerMode(String tunerMode) {
        AppManager.getInstance().currentActivity().runOnUiThread(() -> {
                    if (mTunerMode != null) {
                        mTunerMode.setText(tunerMode);
                    }
                }
        );

    }

    public void setmSource(String source) {
        AppManager.getInstance().currentActivity().runOnUiThread(() -> {
                    if (mSource != null) {
                        mSource.setText(source);
                    }
                }
        );

    }

    public void setSignalName(String name) {
        AppManager.getInstance().currentActivity().runOnUiThread(() -> {
                    if (mSignalName != null) {
                        mSignalName.setText(name);
                    }
                }
        );

    }

    @Override
    public void onPause() {
        super.onPause();
        mIsActive = false;
        LogUtils.d(TAG, "onPause()  mIsActive:" + mIsActive + " fragment:" + this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mIsActive = true;
        LogUtils.d(TAG, "onResume() mIsActive:" + mIsActive + " fragment:" + this);
    }

    @Override
    public void onStop() {
        super.onStop();
        LogUtils.d(TAG, "onStop()");
        removeInfoView();
        if (mSignalCallbackHandler != null) {
            mSignalCallbackHandler.removeListener();
            mSignalCallbackHandler = null;
        }
        if (mTvInputManager != null) {
            mTvInputManager.unregisterCallback(tvInputCallback);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.d(TAG, "onDestroy()");
        if (mMainUIHandler != null && mSignalPlayRunnable != null) {
            LogUtils.d(TAG, "mMainUIHandler!= null     mSignalPlayRunnable :" + mSignalPlayRunnable);
            mMainUIHandler.removeCallbacks(mSignalPlayRunnable);
        }
    }


    private List<TvInputInfo> getAllConnectedList() {
        List<TvInputInfo> stateList = new ArrayList<>();
        List<TvInputInfo> list = mTvInputManager.getTvInputList();
        for (int i = 0; i < list.size(); i++) {
            TvInputInfo each = list.get(i);
            String id = each.getId();
            int state = mTvInputManager.getInputState(id);
            switch (state) {
                case TvInputManager.INPUT_STATE_CONNECTED:
                    stateList.add(each);
                    break;
                case TvInputManager.INPUT_STATE_DISCONNECTED:
                    //stateList.add(each);
                    break;
                default:
                    break;
            }
        }

        TvInputInfo[] infos = new TvInputInfo[stateList.size()];
        //HDMI>TV>YUV>AV
        for (int i = 0; i < stateList.size(); i++) {
            TvInputInfo each = stateList.get(i);
            infos[i] = each;
        }
        sort(infos);
        for (int i = 0; i < infos.length; i++) {
            TvInputInfo info = infos[i];
            Log.e("DefaultService" + ":", "getAllConnectedList: " + info.getId());
        }
        List<TvInputInfo> infors = new ArrayList<>(Arrays.asList(infos));
        return infors;
    }

    private TvInputInfo[] sort(TvInputInfo[] infos) {
        for (int j = 0; j < infos.length; j++) {
            for (int k = 0; k < infos.length - 1 - j; k++) {
                changePosition(infos, k, k + 1);
            }
        }
        return infos;
    }

    private void changePosition(TvInputInfo[] origins, int from, int to) {
        int index1 = getIndex(origins[from].getType());
        int index2 = getIndex(origins[to].getType());
        if (index1 < index2) {
            TvInputInfo temp = origins[from];
            origins[from] = origins[to];
            origins[to] = temp;
        }
    }

    //HDMI>TV>YUV>AV
    private int getIndex(int type) {
        int index = 0;
        switch (type) {
            case TYPE_COMPOSITE:
                index = 0;
                break;
            case TvInputInfo.TYPE_COMPONENT:
                index = 1;
                break;
            case TvInputInfo.TYPE_TUNER:
                index = 2;
                break;
            case TvInputInfo.TYPE_HDMI:
                index = 3;
                break;
            default:
                index = -1;
                break;
        }
        return index;
    }

    class SignalPlayRunnable implements Runnable {
        @Override
        public void run() {

            LogUtils.d(TAG, "startPlay()");
            TvInputInfo inputInfo = mSourceList.get(0);
            if (inputInfo == null) {
                return;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("startPlay() " + inputInfo.getId() + "\n")
                    .append("inputInfo.getTunerCount " + inputInfo.getTunerCount() + "\n")
                    .append("inputInfo.getId " + inputInfo.getId() + "\n")
                    .append("inputInfo.getParentId " + inputInfo.getParentId() + "\n")
                    .append("inputInfo.getServiceInfo " + inputInfo.getServiceInfo().name + "\n")
                    .append("inputInfo.loadLabel " + inputInfo.loadLabel(StoreModeApplication.sContext) + "\n")
                    .append("inputinfo type= " + inputInfo.getType());

            LogUtils.d(TAG, stringBuilder.toString());
            Date date = new Date();
            String time = date.toLocaleString();

            mSignalTime.setText(time);
            mSignalName.setText("");
            if (inputInfo.getType() != TvInputInfo.TYPE_TUNER) {
                mSignalTime.setVisibility(View.INVISIBLE);
                mTunerMode.setVisibility(View.GONE);
                mSourceBlank.setVisibility(View.GONE);
                mSourceSpace.setVisibility(View.VISIBLE);
            } else {
                mSignalTime.setVisibility(View.VISIBLE);
                mTunerMode.setVisibility(View.VISIBLE);
                mSourceBlank.setVisibility(View.VISIBLE);
                mSourceSpace.setVisibility(View.GONE);
            }
            setSignalInfoBarViewVisible(true);

            mSignalPlayer = PlayManager.getInstance().getCurrentPlayer();

            if (!(mSignalPlayer instanceof SignalPlayer)) {
                LogUtils.d(TAG, "startPlay()  111");
                StoreModeManager.getInstance().start();
                return;
            }

            ((SignalPlayer) mSignalPlayer).setTVid(inputInfo.getId());
            ((SignalPlayer) mSignalPlayer).setFragemnt(SignalFragment.this);
            ((SignalPlayer) mSignalPlayer).setInputInfo(inputInfo);
            mCurrentSignal = inputInfo;
            LogUtils.e(TAG, "startPlay()  mCurrentSignal id:" + mCurrentSignal.getId() + "   parentID:" + mCurrentSignal.getParentId() + "  msignalFragment:" + SignalFragment.this);
            mSignalPlayer.start(mTvView);
        }
    }
}
