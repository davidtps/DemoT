package com.hisense.storemode.ui.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;

import com.hisense.storemode.R;
import com.hisense.storemode.bean.MallLogoDeleteEvent;
import com.hisense.storemode.widget.OperationConfirmGuidanceStyList;
import com.hisense.storemode.widget.UpdateVideoGuidanceStyList;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import androidx.leanback.app.GuidedStepFragment;
import androidx.leanback.widget.GuidanceStylist;
import androidx.leanback.widget.GuidedAction;

/**
 * @author tianpengsheng
 * create at   5/8/19 4:05 PM
 */
public class OperationConfirmFragment extends GuidedStepFragment {
    private static final String TAG = "OperationConfirmFragment";
    private static final int ACTION_ID_POSITIVE = 1;
    private static final int ACTION_ID_NEGATIVE = ACTION_ID_POSITIVE + 1;


    private OperationConfirmGuidanceStyList mOperationConfirmGuidanceStyList;

    @NonNull
    @Override
    public GuidanceStylist.Guidance onCreateGuidance(Bundle savedInstanceState) {
        String title = getString(R.string.delete_picture_dialog_title);
        String desc = getString(R.string.delete_picture_dialog_content);

        return new UpdateVideoGuidanceStyList.Guidance(title, desc, "", null);
    }

    @Override
    public GuidanceStylist onCreateGuidanceStylist() {
        mOperationConfirmGuidanceStyList = new OperationConfirmGuidanceStyList();
        return mOperationConfirmGuidanceStyList;
    }

    @Override
    public void onCreateActions(@NonNull List<GuidedAction> actions, Bundle savedInstanceState) {
        GuidedAction action = new GuidedAction.Builder()
                .id(ACTION_ID_POSITIVE)
                .title(getString(R.string.dialog_btn_delete_tip)).build();
        actions.add(action);
        action = new GuidedAction.Builder()
                .id(ACTION_ID_NEGATIVE)
                .title(getString(R.string.video_update_button_cancel)).build();
        actions.add(action);
    }

    @Override
    public void onGuidedActionClicked(GuidedAction action) {
        if (ACTION_ID_POSITIVE == action.getId()) {
            EventBus.getDefault().post(new MallLogoDeleteEvent());
        }
        getActivity().finish();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }


}
