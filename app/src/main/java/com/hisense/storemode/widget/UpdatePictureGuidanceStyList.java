package com.hisense.storemode.widget;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hisense.storemode.R;

import androidx.leanback.widget.GuidanceStylist;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by cuihuihui on 2019年05月09日 16时01分.
 */
public class UpdatePictureGuidanceStyList extends GuidanceStylist {

    @BindView(R.id.picture_title)
    TextView mTitleTV;
    @BindView(R.id.picture_summary)
    TextView mSummaryTV;
    @BindView(R.id.picture_progress)
    TextView mProgressTV;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Guidance guidance) {

        View view = inflater.inflate(R.layout.widget_update_picture_guidance, null);
        ButterKnife.bind(this, view);

        mTitleTV.setText(guidance.getTitle());
//        mSummaryTV.setText(guidance.getDescription());
        mProgressTV.setText(" ");
        return view;
    }


    public void setProgress(int value, int total) {
        mProgressTV.setText(String.valueOf(value) + "/" + String.valueOf(total));
    }

    public void setmSummary(String text) {
        mSummaryTV.setText(text);
    }
}