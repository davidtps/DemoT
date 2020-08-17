package com.hisense.storemode.ui.fragment;

import android.content.Context;
import android.os.Bundle;

import com.hisense.hianidraw.cache.SeriesUtils;
import com.hisense.storemode.R;
import com.hisense.storemode.manager.epos.EPosManager;
import com.hisense.storemode.utils.ConstantConfig;
import com.hisense.storemode.utils.PreferenceUtils;
import com.hisense.storemode.widget.RadioButtonPreference;

import java.util.ArrayList;
import java.util.List;

import androidx.leanback.preference.LeanbackPreferenceFragment;
import androidx.preference.Preference;

/**
 * Create by cuihuihui at 2019-06-11
 */

public class EposPositionAdjustFragment extends LeanbackPreferenceFragment {

    private static final String TAG = "EposPositionAdjustFragment";
    private RadioButtonPreference mLeftPreference;
    private RadioButtonPreference mRightPreference;
    private RadioButtonPreference mTopPreference;
    private RadioButtonPreference mBottomPreference;
    private List<RadioButtonPreference> mCheckBoxPreferenceList = new ArrayList<>();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        initView();
        initData();
    }

    private void initView() {
        addPreferencesFromResource(R.xml.epos_position_adjust_setup);
        mLeftPreference = (RadioButtonPreference) findPreference(getString(R.string.key_epos_position_left));
        mTopPreference = (RadioButtonPreference) findPreference(getString(R.string.key_epos_position_top));
        mRightPreference = (RadioButtonPreference) findPreference(getString(R.string.key_epos_position_right));
        mBottomPreference = (RadioButtonPreference) findPreference(getString(R.string.key_epos_position_bottom));
        mCheckBoxPreferenceList.add(mLeftPreference);
        mCheckBoxPreferenceList.add(mTopPreference);
        mCheckBoxPreferenceList.add(mRightPreference);
        mCheckBoxPreferenceList.add(mBottomPreference);
    }

    private void initPreferences(int pos) {
        for (int i = 0; i < mCheckBoxPreferenceList.size(); i++) {
            if (i == pos) {
                mCheckBoxPreferenceList.get(i).setChecked(true);
//                mCheckBoxPreferenceList.get(i).setEnabled(false);
            } else {
                mCheckBoxPreferenceList.get(i).setChecked(false);
//                mCheckBoxPreferenceList.get(i).setEnabled(true);
            }
        }
    }

    private void initData() {
        boolean[] arr = SeriesUtils.getEposPositionBooleans();
        mLeftPreference.setVisible(arr[0]);
        mTopPreference.setVisible(arr[1]);
        mRightPreference.setVisible(arr[2]);
        mBottomPreference.setVisible(arr[3]);
        initPreferences(ConstantConfig.EPOS_LAYOUT_POSITION);

    }

    @Override
    public boolean onPreferenceTreeClick(Preference preference) {
        String key = preference.getKey();
        PreferenceUtils.getInstance().save(ConstantConfig.RESET_PALY_POLICY, true);


        if (key.equals(getString(R.string.key_epos_position_left))) {
            EPosManager.getInstance().setEposPosition(0);
            setPreference(0);
            return true;
        }
        if (key.equals(getString(R.string.key_epos_position_top))) {
            EPosManager.getInstance().setEposPosition(1);
            setPreference(1);

            return true;
        }
        if (key.equals(getString(R.string.key_epos_position_right))) {
            EPosManager.getInstance().setEposPosition(2);
            setPreference(2);

            return true;
        }
        if (key.equals(getString(R.string.key_epos_position_bottom))) {
            EPosManager.getInstance().setEposPosition(3);
            setPreference(3);

            return true;
        }
        return super.onPreferenceTreeClick(preference);
    }


    private void setPreference(int pos) {
        for (int j = 0; j < mCheckBoxPreferenceList.size(); j++) {
            if (j == pos) {
//                mCheckBoxPreferenceList.get(j).setEnabled(false);
                mCheckBoxPreferenceList.get(j).setChecked(true);
            } else {
//                mCheckBoxPreferenceList.get(j).setEnabled(true);
                mCheckBoxPreferenceList.get(j).setChecked(false);
            }
        }

    }

}
