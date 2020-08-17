package com.hisense.storemode.widget;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hisense.storemode.R;
import com.hisense.storemode.utils.StringUtil;

import androidx.leanback.widget.GuidanceStylist;

/**
 * Create by xuchongxiang at 2019-05-09 10:51 AM
 */

public class ModeSelectGuidanceStylist extends GuidanceStylist {


    private ProgressBar mPbProgress;

    public void setPbProgress(int progress) {
        mPbProgress.setProgress(progress);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Guidance guidance) {
        View view = inflater.inflate(R.layout.widget_mode_select_guidance, container, false);
        TextView tvTitle = view.findViewById(R.id.tv_title);
        TextView tvSummary = view.findViewById(R.id.tv_summary);
        tvTitle.setText(guidance.getTitle());
        tvSummary.setText(guidance.getBreadcrumb());
        if (StringUtil.isEmpty(guidance.getBreadcrumb())) {
            tvSummary.setVisibility(View.GONE);
        }
        mPbProgress = view.findViewById(R.id.pb_progress);
        mPbProgress.setProgress(Integer.valueOf(guidance.getDescription()));
        return view;
    }
}
