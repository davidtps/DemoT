package com.hisense.storemode.manager.play.player;

import android.content.ContentUris;
import android.database.Cursor;
import android.media.tv.TvContentRating;
import android.media.tv.TvContract;
import android.media.tv.TvInputInfo;
import android.media.tv.TvTrackInfo;
import android.media.tv.TvView;
import android.media.tv.TvView.TvInputCallback;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;

import com.hisense.storemode.StoreModeApplication;
import com.hisense.storemode.manager.AppManager;
import com.hisense.storemode.manager.StoreModeManager;
import com.hisense.storemode.ui.fragment.SignalFragment;
import com.hisense.storemode.utils.ConstantConfig;
import com.hisense.storemode.utils.LogUtils;
import com.hisense.storemode.utils.PreferenceUtils;
import com.hisense.storemode.utils.StringUtil;
import com.mediatek.twoworlds.tv.MtkTvChannelList;
import com.mediatek.twoworlds.tv.MtkTvConfig;
import com.mediatek.twoworlds.tv.common.MtkTvChCommonBase;
import com.mediatek.twoworlds.tv.common.MtkTvConfigType;
import com.mediatek.twoworlds.tv.common.MtkTvConfigTypeBase;
import com.mediatek.twoworlds.tv.model.MtkTvChannelInfoBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cuihuihui on 2019年04月22日 11时42分.
 */
public class SignalPlayer implements IPlayer {
    private static final String TAG = "SignalPlayer";
    private static final int CH_LIST_MASK = MtkTvChCommonBase.SB_VNET_ACTIVE;
    private static final int CH_LIST_AVL = MtkTvChCommonBase.SB_VNET_ACTIVE;

    private static final String TUNER_MODE_PREFER_SAT = MtkTvConfigTypeBase.CFG_TWO_SAT_CHLIST_PREFERRED_SAT;
    private static final String CHANNEL_LIST_TYPE = MtkTvConfigType.CFG_MISC_CH_LST_TYPE;
    private static final String TUNER_MODE = MtkTvConfigType.CFG_BS_BS_SRC;

    private String mTvId;
    private TvView mTvView;
    private SignalFragment mSignalFragment;
    private static Handler mHandler;
    private TvInputInfo mTvInputInfo;
    private static final String CUR_CHANNEL_ID = MtkTvConfigType.CFG_NAV_AIR_CRNT_CH;
    private static final String LAST_CHANNEL_ID = MtkTvConfigType.CFG_NAV_AIR_LAST_CH;

    private static final String SELECTION = "";
    private static final String SELECTION_WITH_SVLID = SELECTION + "substr(cast(" +
            TvContract.Channels.COLUMN_INTERNAL_PROVIDER_DATA + " as varchar),7,5) = ?";

    private static final String SELECTION_WITH_SVLID_CHANNELID = SELECTION_WITH_SVLID +
            " and substr(cast(" + TvContract.Channels.COLUMN_INTERNAL_PROVIDER_DATA +
            " as varchar),19,10) = ?";
    private static final String ORDERBY = "substr(cast(" +
            TvContract.Channels.COLUMN_INTERNAL_PROVIDER_DATA + " as varchar),19,10)";// DTV00607832

    private List<MtkTvChannelInfoBase> listChannels;

    private Cursor mSignalCursor = null;

    private TvViewCallback mCallback = new TvViewCallback();
    private static InputResolutionRunnable inputResolutionRunnable;
    private static PlayNextRunnable playNextRunnable;

    private class TvViewCallback extends TvInputCallback {

        @Override
        public void onChannelRetuned(String inputId, Uri channelUri) {
            super.onChannelRetuned(inputId, channelUri);
            LogUtils.d(TAG, "onChannelRetuned inputId =  " + inputId + "; channelUri =" + channelUri);
        }

        @Override
        public void onContentAllowed(String inputId) {
            super.onContentAllowed(inputId);
            LogUtils.d(TAG, "onContentAllowed inputId =  " + inputId);

        }

        @Override
        public void onConnectionFailed(String inputId) {
            super.onConnectionFailed(inputId);
            LogUtils.d(TAG, "onConnectionFailed inputId =  " + inputId);

        }

        @Override
        public void onContentBlocked(String inputId, TvContentRating rating) {
            super.onContentBlocked(inputId, rating);
            LogUtils.d(TAG, "onContentBlocked inputId =  " + inputId + "; rating = " + rating);

        }

        @Override
        public void onDisconnected(String inputId) {
            super.onDisconnected(inputId);
            LogUtils.d(TAG, "onDisconnected inputId =  " + inputId);

        }

        @Override
        public void onTimeShiftStatusChanged(String inputId, int status) {
            super.onTimeShiftStatusChanged(inputId, status);
            LogUtils.d(TAG, "onTimeShiftStatusChanged inputId =  " + inputId + "; status = " + status);

        }

        @Override
        public void onTracksChanged(String inputId, List<TvTrackInfo> tracks) {
            super.onTracksChanged(inputId, tracks);
            LogUtils.d(TAG, "onTracksChanged inputId =  " + inputId + "; tracks = " + tracks);

        }

        @Override
        public void onTrackSelected(String inputId, int type, String trackId) {
            super.onTrackSelected(inputId, type, trackId);
            LogUtils.d(TAG, "onTrackSelected inputId =  " + inputId + "; type = " + type + ";trackId = " + trackId);

        }

        @Override
        public void onVideoAvailable(String inputId) {
            super.onVideoAvailable(inputId);
            LogUtils.d(TAG, "onVideoAvailable inputId =  " + inputId);
        }

        @Override
        public void onVideoSizeChanged(String inputId, int width, int height) {
            super.onVideoSizeChanged(inputId, width, height);
            LogUtils.d(TAG, "onVideoSizeChanged inputId =  " + inputId + ";width = " + width + "; height = " + height);

        }

        @Override
        public void onVideoUnavailable(String inputId, int reason) {
            super.onVideoUnavailable(inputId, reason);
            LogUtils.d(TAG, "onVideoUnavailable inputId =  " + inputId + "; reason=" + reason);

        }
    }

    String[] projection = {
            TvContract.Channels.COLUMN_DISPLAY_NUMBER,
            TvContract.Channels.COLUMN_DISPLAY_NAME,
            TvContract.Channels.COLUMN_INTERNAL_PROVIDER_DATA,
            TvContract.Channels.COLUMN_PACKAGE_NAME,
            TvContract.Channels.COLUMN_BROWSABLE,
            TvContract.Channels.COLUMN_INPUT_ID,
            TvContract.Channels._ID
    };

    @Override
    public void start(Object view) {
        LogUtils.d(TAG, "start() is invoked.");
        if (mHandler == null) {
            mHandler = new Handler(Looper.getMainLooper());
        }
        if (!(view instanceof TvView)) {
            LogUtils.d(TAG, "!(view instanceof TvView");
            if (mSignalFragment != null && !(mSignalFragment.isSignalLoss)) {
                PreferenceUtils.getInstance().save(ConstantConfig.RESET_PALY_POLICY, true);
                StoreModeManager.getInstance().start();
            }
            return;
        }

        mTvView = (TvView) (view);
        mTvView.setCallback(mCallback);
        LogUtils.d(TAG, "start() is invoked.   mTvView:" + mTvView);

        if (mSignalFragment != null && !(mSignalFragment.isSignalLoss)) {
            startSignal();
        }
    }

    @Override
    public void stop() {
        LogUtils.d(TAG, "stop() is invoked. mHandler:" + mHandler);

        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }

        if (mSignalFragment != null) {
            mSignalFragment.setSignalInfoBarViewVisible(false);
        }
        if (mCallback != null) {
            mCallback = null;
        }

        if (mHandler == null) { //have a bug !!  background play foreground, stop() method will invoke
            mHandler = new Handler(Looper.getMainLooper());
        }
        mHandler.post(() -> {
            if (mTvView != null) {
                LogUtils.d(TAG, "mTvView  reset() is invoked.  mTvView:" + mTvView);
                mTvView.reset();
            } else {
                LogUtils.d(TAG, " stop() is invoked. but mTvView  is null");
            }
        });
    }

    public void setFragemnt(SignalFragment fragemnt) {
        mSignalFragment = fragemnt;
        LogUtils.d(TAG, "setFragemnt    fragment:" + mSignalFragment);
    }

    @Override
    public void playNext(Object newFilePath) {
        LogUtils.d(TAG, "playNext(), newFilePath = " + newFilePath);

        start(null);
    }

    private void startSignal() {
        boolean isStarted = startSignalInternal();
        LogUtils.d(TAG, "startSignal(), isStarted = " + isStarted);

        if (!isStarted) {
            if (mSignalFragment != null && mSignalFragment.isSignalLoss) {
                LogUtils.d(TAG, "mSignalFragment != null && mSignalFragment.isSignalLoss");
                return;
            }

            mSignalFragment.dealWithLoss();
//            StoreModeManager.getInstance().start();
        } else {

            playNextType();
        }
    }

    private void playNextType() {
//        if (mHandler == null) {
//            mHandler = new Handler(Looper.getMainLooper());
//        }
//        mHandler.removeCallbacksAndMessages(null);
        if (playNextRunnable == null) {
            playNextRunnable = new PlayNextRunnable();
        }
        mHandler.postDelayed(playNextRunnable, ConstantConfig.SIGNAL_PLAY_TIME * 1000);
    }

    class PlayNextRunnable implements Runnable {

        @Override
        public void run() {
            boolean onlyPlaySignal = ConstantConfig.isOnlySignalChecked();
            LogUtils.d(TAG, "signal play end ,play next onlyPlaySignal:" + onlyPlaySignal + "  signalplayer:" + SignalPlayer.this);
            if (!onlyPlaySignal) {
                if (mSignalFragment != null && !(mSignalFragment.mIsActive)) {
                    LogUtils.d(TAG, "signal play end inner, signalfragment is onPause status; replay signal 5 minutes  mSignalFragment.mIsActive:" + mSignalFragment.mIsActive + "   mSignalFragment:" + mSignalFragment);
                    mHandler.postDelayed(this, ConstantConfig.SIGNAL_PLAY_TIME * 1000);
                } else {
                    LogUtils.d(TAG, "signal play end inner, signalfragment 5 minutes ending,start next sources;");
                    StoreModeManager.getInstance().start();
                }
            } else {
                LogUtils.d(TAG, "only signal paly delay");
                mHandler.postDelayed(this, ConstantConfig.SIGNAL_PLAY_TIME * 1000);
            }
        }
    }

    public void setTVid(String id) {
        mTvId = id;
    }

    public void setInputInfo(TvInputInfo inputInfo) {
        mTvInputInfo = inputInfo;
    }


    private Boolean startSignalInternal() {
        boolean res = false;
        LogUtils.d(TAG, "startSignalInternal() invoke");
        if (mTvView == null || StringUtil.isEmpty(mTvId)) {
            LogUtils.d(TAG, "MtvView = " + mTvView + "; mTvId = " + mTvId);
            return false;
        }

        LogUtils.d(TAG, " TYPE_TUNER = " + mTvInputInfo.getType());

        if (mTvInputInfo.getType() == TvInputInfo.TYPE_TUNER) {
            res = startTV();
        } else if (mTvInputInfo.getType() == TvInputInfo.TYPE_HDMI) {
            res = startHdmiAndOther();
        } else {
            res = startAv();
        }

        return res;
    }

    private boolean startHdmiAndOther() {
        LogUtils.d(TAG, "startHdmiAndOther() TYPE_TUNER = " + "HdmiAndOther");

        if (mSignalFragment != null) {
            mSignalFragment.setmSource(String.valueOf(mTvInputInfo.loadLabel(StoreModeApplication.sContext)));
        }
        Uri uri = Uri.parse("content://main");
        LogUtils.d(TAG, "else info id = " + mTvInputInfo.getId());

        LogUtils.d(TAG, "startSignalInternal() else tune");


        tune(mTvInputInfo.getId(), uri);

        if (inputResolutionRunnable == null) {
            inputResolutionRunnable = new InputResolutionRunnable();
        }
        //show input resolution
        mHandler.postDelayed(inputResolutionRunnable, 1500);

        return true;
    }

    class InputResolutionRunnable implements Runnable {

        @Override
        public void run() {
            if (mSignalFragment != null) {
                mSignalFragment.setSignalName(ConstantConfig.getInputresolution());
            }
        }
    }

    private void tune(String id, Uri uri) {

        AppManager.getInstance().currentActivity().runOnUiThread(() -> {
            mTvView.tune(id, uri);
        });
    }

    private boolean startAv() {
        LogUtils.d(TAG, "startAv() TYPE_TUNER = " + "Av");

        if (mSignalFragment != null) {
            mSignalFragment.setmSource(String.valueOf(mTvInputInfo.loadLabel(StoreModeApplication.sContext)));
        }
        //Uri uri = Uri.parse("content://main");

        Uri uri = TvContract.buildChannelUriForPassthroughInput(mTvInputInfo.getId());

        LogUtils.d(TAG, "else info id = " + mTvInputInfo.getId());

        LogUtils.d(TAG, "startSignalInternal() else tune");
        tune(mTvInputInfo.getId(), uri);

        return true;
    }

    private boolean startTV() {
        boolean isHasChannel = isHasScanChannel();
        if (!isHasChannel) {
            LogUtils.d(TAG, " isHasChannel = null");
            return false;
        }

        LogUtils.d(TAG, "startTV() TYPE_TUNER = " + "tv");

        Cursor cursor = StoreModeApplication.sContext.getContentResolver().query(TvContract.Channels.CONTENT_URI, projection, null, null, null);
        try {
            if (cursor == null) {
                LogUtils.d(TAG, "cursor = null");
                return false;
            }
            LogUtils.d(TAG, "cursor != null");

            long chanId = getCurrentChannel();
            LogUtils.d(TAG, "startTV() chid = " + chanId);


            findIdByChannel(cursor, chanId);
            if (mSignalCursor == null) {
                cursor.moveToFirst();
                findIdByTvinfo(cursor);
            }

            if (mSignalCursor == null) {
                LogUtils.d(TAG, "startSignalInternal() mSignalCursor =null");
                return false;
            }
            boolean tuneSuccess = tuneTv();
            if (!tuneSuccess) {
                return false;
            }
            return true;

        } catch (Exception e) {
            LogUtils.e(TAG, "startSignalInternal(), unexpected Exception = " + e.toString());
        } finally {
            LogUtils.e(TAG, "startSignalInternal(), finally execute  c?:" + cursor);
            closeCursor(cursor);
            closeCursor(mSignalCursor);
        }
        return false;
    }

    private void closeCursor(Cursor cursor) {
        if (cursor != null) {
            synchronized (cursor) {
                if (!cursor.isClosed()) {
                    cursor.close();
                    cursor = null;
                }
            }
        }
    }

    private boolean tuneTv() {

        String mInputId = mSignalCursor.getString(
                mSignalCursor.getColumnIndex(TvContract.Channels.COLUMN_INPUT_ID));
        String mDiplayNumber = mSignalCursor.getString(
                mSignalCursor.getColumnIndex(TvContract.Channels.COLUMN_DISPLAY_NUMBER));
        String mDispalyName = mSignalCursor.getString(
                mSignalCursor.getColumnIndex(TvContract.Channels.COLUMN_DISPLAY_NAME));
        long mChannelId = mSignalCursor.getLong(
                mSignalCursor.getColumnIndex(TvContract.Channels._ID));

        LogUtils.d(TAG, "mSignalCursor mChannelId = " + mChannelId);
        LogUtils.d(TAG, "mSignalCursor mInputId = " + mInputId);
        LogUtils.d(TAG, "mSignalCursor mDiplayNumber = " + mDiplayNumber);
        LogUtils.d(TAG, "mSignalCursor mDispalyName = " + mDispalyName);

        Uri uri = ContentUris.withAppendedId(TvContract.Channels.CONTENT_URI,
                mChannelId);
        LogUtils.d(TAG, "TV uri = " + uri);

        if (mSignalFragment != null) {
            mSignalFragment.setmSource(String.valueOf(mDiplayNumber));
            mSignalFragment.setSignalName(mDispalyName);

        }

        if (mSignalFragment != null && mSignalFragment.isSignalLoss) {
            return false;

        }
        LogUtils.d(TAG, "startSignalInternal() tv tune");

        tune(mInputId, uri);
        return true;
    }

    private void findIdByChannel(Cursor cursor, long chanId) {
        LogUtils.d(TAG, "hasCurrentChannel = true");

        while (cursor.moveToNext()) {
            LogUtils.d(TAG, "findIdByChannel() cursor1111 != null");

            long channelId = cursor.getLong(
                    cursor.getColumnIndex(TvContract.Channels._ID));

            LogUtils.d(TAG, "findIdByChannel() cursor channelId = " + channelId);

            if (channelId == chanId) {
                LogUtils.d(TAG, "equal");
                mSignalCursor = cursor;
                break;
            }
        }
    }

    private void findIdByTvinfo(Cursor cursor) {
        LogUtils.d(TAG, "findIdByTvinfo ");
        while (cursor.moveToNext()) {
            LogUtils.d(TAG, "cursor222 != null");

            String inputId = cursor.getString(
                    cursor.getColumnIndex(TvContract.Channels.COLUMN_INPUT_ID));

            LogUtils.d(TAG, "cursor inputId = " + inputId);

            if (mTvInputInfo.getId().equals(inputId)) {
                LogUtils.d(TAG, "equal");
                mSignalCursor = cursor;
                break;
            }
        }
    }

    private long getCurrentChannel() {
        int chId = MtkTvConfig.getInstance().getConfigValue(CUR_CHANNEL_ID);
        Cursor c = StoreModeApplication.sContext.getContentResolver().query(TvContract.Channels.CONTENT_URI, null,
                SELECTION_WITH_SVLID_CHANNELID, getSvlIdAndChannelIdSelectionArgs(chId), ORDERBY);
        if (c == null) {
            return -1;
        }

        while (c.moveToNext()) {
            long mId = c.getLong(c.getColumnIndex(TvContract.Channels._ID));
            try {
                byte[] mData = c.getBlob(c.getColumnIndex(TvContract.Channels.COLUMN_INTERNAL_PROVIDER_DATA));
                String data = new String(mData);
                LogUtils.d(TAG, "getCurrentChannel()  data = " + data);
                int id = getChannelId(data);
                LogUtils.d(TAG, "getCurrentChannel()  id = " + id);
                if (id == chId) {
                    return mId;
                }

            } catch (Exception ex) {
            }
        }
        c.close();
        return -1;
    }

    private int getChannelId(String data) {
        long v[] = new long[6];
        //MtkLog.d(TAG, "data:" + data);
        if (data == null) {
            return -1;
        }
        String value[] = data.split(",");
        if (value.length < 5) {
            LogUtils.d(TAG, "getChannelId() parserTIFChannelData data.length <6");
            return -1;
        }
        long mSvlId = Long.parseLong(value[1]);
        long mSvlRecId = Long.parseLong(value[2]);
        long channelId = Long.parseLong(value[3]);
        // int mHashcode = Integer.parseInt(value[4]);
        long mKey = (mSvlId << 16) + mSvlRecId;
        v[0] = mSvlId;
        v[1] = mSvlRecId;
        v[2] = channelId;
        // v[3] = mHashcode;
        v[4] = (mSvlId << 16) + mSvlRecId;
        if (value.length == 6) {
            long mServiceType = Long.parseLong(value[5]);
            v[5] = mServiceType;
            return (int) v[2];
        }
        return -1;
    }


    private String[] getSvlIdAndChannelIdSelectionArgs(int channelId) {
        long newId = (channelId & 0xffffffffL);
        LogUtils.d(TAG, "channelId>>>" + channelId + ">>" + newId);
        int svlId = getSVL();
        String[] selectionArgs = {
                String.format("%05d", svlId), String.format("%010d", newId)
        };
        return selectionArgs;
    }


    private Boolean isHasScanChannel() {
        LogUtils.d(TAG, "isHasScanChannel() is invoked.");
        listChannels = getChannelListByMaskFilter(0,
                MtkTvChannelList.CHLST_ITERATE_DIR_FROM_FIRST, 1, CH_LIST_MASK, CH_LIST_AVL);
        return (listChannels != null && listChannels.size() > 0);
    }

    private List<MtkTvChannelInfoBase> getChannelListByMaskFilter(int chId, int dir, int count, int mask, int valId) {
        int chLen = MtkTvChannelList.getInstance().getChannelCountByMask(getSVL(), mask, valId);

        List<MtkTvChannelInfoBase> res;
        if (chLen <= 0) {
            res = new ArrayList<>();
        } else {
            int realCount = count > chLen ? chLen : count;
            res = MtkTvChannelList.getInstance().getChannelListByMask(getSVL(), mask, valId, dir, chId, realCount);
        }
        LogUtils.d(TAG, "getChannelListByMaskFilter() is invoked. list size():" + res.size());
        if (res.size() != 0) {
            MtkTvChannelInfoBase mtkTvChannelInfoBase = res.get(0);
            // mDisplayNumber = mtkTvChannelInfoBase.getChannelNumber();
            LogUtils.d(TAG, "mtkTvChannelInfoBase = " + mtkTvChannelInfoBase.toString());
        }
        return res;
    }

    private int getSVL() {
        LogUtils.d(TAG, "getSVL() is invoked.");

        boolean hasCAM = false;
        int type = MtkTvConfig.getInstance().isConfigVisible(CHANNEL_LIST_TYPE);
        if (MtkTvConfigType.CFGR_VISIBLE == type) {
            int value = MtkTvConfig.getInstance().getConfigValue(CHANNEL_LIST_TYPE);
            hasCAM = (value > 0);
        }

        int tunerMode = MtkTvConfig.getInstance().getConfigValue(TUNER_MODE);
        LogUtils.d(TAG, "getSVL(), tunerMode = " + tunerMode + "; hasCAM = " + hasCAM);

        int res = -1;
        switch (tunerMode) {
            case ConstantConfig.DB_AIR_OPTID: {
                LogUtils.d(TAG, "tunerMode = DB_AIR_OPTID");
                res = (hasCAM ? ConstantConfig.DB_CI_PLUS_SVLID_AIR : ConstantConfig.DB_AIR_SVLID);
                LogUtils.d(TAG, "res= " + res);
                break;
            }

            case ConstantConfig.DB_CAB_OPTID: {
                LogUtils.d(TAG, "tunerMode = DB_CAB_OPTID");
                res = (hasCAM ? ConstantConfig.DB_CI_PLUS_SVLID_CAB : ConstantConfig.DB_CAB_SVLID);
                break;
            }

            case ConstantConfig.DB_SAT_OPTID:
            case ConstantConfig.DB_GENERAL_SAT_OPTID: {
                if (hasCAM) {
                    res = ConstantConfig.DB_CI_PLUS_SVLID_SAT;
                } else {
                    int prefer = MtkTvConfig.getInstance().getConfigValue(TUNER_MODE_PREFER_SAT);
                    res = (prefer != 0 ? ConstantConfig.DB_SAT_PRF_SVLID : ConstantConfig.DB_SAT_SVLID);
                }
                break;
            }

            default: {
                res = (hasCAM ? ConstantConfig.DB_CI_PLUS_SVLID_AIR : ConstantConfig.DB_AIR_SVLID);
                break;
            }
        }

        LogUtils.d(TAG, "getSVL(), res = " + res);
        setTunerMode(res);
        return res;
    }

    private void setTunerMode(int mode) {
        if (mode == 1) {
            if (mSignalFragment != null) {
                mSignalFragment.setTunerMode("Antenna");
            }
        } else {
            if (mSignalFragment != null) {
                mSignalFragment.setTunerMode("cable");
            }
        }
    }


}

