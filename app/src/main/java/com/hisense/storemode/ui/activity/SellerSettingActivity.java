package com.hisense.storemode.ui.activity;

import android.os.Bundle;

import com.hisense.storemode.R;
import com.hisense.storemode.bean.FinishEvent;
import com.hisense.storemode.ui.fragment.SellerSettingFragment;
import com.hisense.storemode.utils.LogUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class SellerSettingActivity extends BaseNoCompatActivity {

    private static final String TAG = "SellerSettingActivity";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_seller_setting;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        SellerSettingFragment fragment = new SellerSettingFragment();
        getFragmentManager().beginTransaction().add(R.id.fl_content, fragment,
                "SellerSettingFragment").commitAllowingStateLoss();

    }

    @Subscribe
    public void finishSelf(FinishEvent event) {
        LogUtils.d(TAG, "finishSelf() FinishEvent");
        finish();
    }

    @Override
    protected void onDestroy() {
        LogUtils.d(TAG, "onDestroy() onDestory()");

//        IPlayer currentPlayer = PlayManager.getInstance().getCurrentPlayer();
//        boolean isSignal = (boolean) PreferenceUtils.getInstance().get(SHAREPREF_SIGNAL_CHECK, false);//signal
//        if (currentPlayer instanceof SignalPlayer) {
//            LogUtils.d(TAG, "onDestroy() currentPlayer is signalpalyer");
//            if (isSignal) {
//                LogUtils.d(TAG, "onDestroy() isSignal is true");
//                ((SignalPlayer) currentPlayer).start(null);
//            } else {
//                LogUtils.d(TAG, "onDestroy() isSignal is false");
//
//                StoreModeManager.getInstance().start();
//            }
//        }
//
//        LogUtils.d(TAG, "onDestroy() onDestory()1");
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
