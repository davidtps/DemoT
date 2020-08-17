
package com.hisense.storemode.ui.activity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import com.hisense.storemode.ui.fragment.UpdatePictureFragment;
import com.hisense.storemode.utils.LogUtils;

import androidx.leanback.app.GuidedStepFragment;

/**
 * Created by cuihuihui on 2019年05月09日 15时30分.
 */
public class UpdatePictureActivity extends BaseNoCompatActivity {

    private static final String TAG = "UpdatePictureActivity";

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#21272A")));

        if (savedInstanceState == null) {
            LogUtils.d(TAG, "savedInstanceState == null");
            GuidedStepFragment fragment = new UpdatePictureFragment();
            GuidedStepFragment.addAsRoot(this, fragment, android.R.id.content);
        }
    }

    @Override
    public void onBackPressed() {

    }
}
