package com.hisense.storemode.widget;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hisense.storemode.R;

import androidx.leanback.widget.GuidanceStylist;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by tianpengsheng on 2019年05月08日 18时57分.
 */
public class UpdateVideoGuidanceStyList extends GuidanceStylist {

    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.summary)
    TextView mSummary;
    @BindView(R.id.progressbar)
    ProgressBar mProgressbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Guidance guidance) {

        View view = inflater.inflate(R.layout.widget_update_video_guidance, null);
        ButterKnife.bind(this, view);

        mTitle.setText(guidance.getTitle());
//        mSummary.setText(guidance.getDescription());
        mProgressbar.setProgress(0, true);
        return view;
    }


    public void setProgress(int value) {
        mProgressbar.setProgress(value, true);
    }


    public void setmSummary(String text) {
        mSummary.setText(text);
    }
}
