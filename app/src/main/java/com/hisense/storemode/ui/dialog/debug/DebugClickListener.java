package com.hisense.storemode.ui.dialog.debug;

/**
 * Create by xuchongxiang at 2019-05-09 2:12 PM
 */

public interface DebugClickListener {
    void onShowLog(boolean isShowLeft);

    void onCloseLog();

    void onResetAqPq();

    void onPlayNext();

    void onChangeSignalOrVideoTime(String signalTime, String videoTime);

    void onChangeEposPosition(String location);

    void onCloseDebugFragment();
}
