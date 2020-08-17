/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.hisense.storemode.ui.activity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import com.hisense.storemode.ui.fragment.UpdateVideoFragment;
import com.hisense.storemode.utils.LogUtils;

import androidx.leanback.app.GuidedStepFragment;

/**
 * @author tianpengsheng
 * create at   5/8/19 4:05 PM
 */
public class UpdateVideoActivity extends BaseNoCompatActivity {
    private static final String TAG = "UpdateVideoActivity";

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#21272A")));

        if (savedInstanceState == null) {
            LogUtils.d(TAG, "savedInstanceState == null");
            GuidedStepFragment fragment = new UpdateVideoFragment();
            GuidedStepFragment.addAsRoot(this, fragment, android.R.id.content);
        }
    }

    @Override
    public void onBackPressed() {

    }
}
