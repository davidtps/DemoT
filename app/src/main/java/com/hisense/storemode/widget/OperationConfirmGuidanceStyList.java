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
 * Created by tianpengsheng on 2019年05月08日 18时57分.
 */
public class OperationConfirmGuidanceStyList extends GuidanceStylist {

    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.summary)
    TextView mSummary;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Guidance guidance) {

        View view = inflater.inflate(R.layout.widget_operation_confirm_guidance, null);
        ButterKnife.bind(this, view);

        mTitle.setText(guidance.getTitle());
        mSummary.setText(guidance.getDescription());
        return view;
    }


}
