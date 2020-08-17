package com.hisense.storemode.ui.activity;

import android.os.Bundle;

import com.hisense.storemode.R;
import com.hisense.storemode.manager.StoreModeManager;
import com.hisense.storemode.ui.fragment.ModeSelectFragment;
import com.hisense.storemode.utils.LogUtils;

public class ModeSelectActivity extends BaseNoCompatActivity {
    private final String TAG = "ModeSelectActivity";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_mode_select;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        LogUtils.d(TAG, "afterCreate()");
        ModeSelectFragment fragment = new ModeSelectFragment();
        getFragmentManager().beginTransaction().add(R.id.fl_content, fragment, "mode")
                .commitNowAllowingStateLoss();
//        GuidedStepFragment.add(getFragmentManager(), fragment);
    }

    @Override
    public void onBackPressed() {
        StoreModeManager.getInstance().start();
        super.onBackPressed();
    }
}
