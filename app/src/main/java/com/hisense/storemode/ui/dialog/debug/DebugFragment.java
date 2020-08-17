package com.hisense.storemode.ui.dialog.debug;

import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hisense.storemode.R;
import com.hisense.storemode.ui.dialog.BaseDialogFragment;
import com.hisense.storemode.utils.ScreenUtils;

import butterknife.BindView;

/**
 * Create by xuchongxiang at 2019-05-08 11:45 AM
 */

public class DebugFragment extends BaseDialogFragment implements View.OnClickListener {

    public static final String TAG = "DebugFragment";

    @BindView(R.id.tv_open_log_left)
    protected TextView mTvOpenLogLeft;
    @BindView(R.id.tv_open_log_right)
    protected TextView mTvOpenLogRight;
    @BindView(R.id.tv_close_log)
    protected TextView mTvCloseLog;
    @BindView(R.id.tv_reset_aqpq)
    protected TextView mTvResetAqpq;
    @BindView(R.id.tv_play_next)
    protected TextView mTvPlayNext;
    @BindView(R.id.et_input_signal_time)
    protected EditText mEtInputSignalTime;
    @BindView(R.id.et_input_video_time)
    protected EditText mEtInputVideoTime;
    @BindView(R.id.tv_close)
    protected TextView mTvClose;
    @BindView(R.id.tv_set_time)
    protected TextView mTvSetTime;
    @BindView(R.id.ll_signal_content)
    protected LinearLayout mLlsignalContent;
    @BindView(R.id.ll_video_content)
    protected LinearLayout mLlVideoContent;

    /*epos*/
    @BindView(R.id.ll_epos_content)
    protected LinearLayout mLlEposContent;
    @BindView(R.id.et_input_epos_location)
    protected EditText mEtInputEposLocation;
    @BindView(R.id.tv_set_epos_location)
    protected TextView mTvSetEposLocation;

    private DebugClickListener mListener;

    public void setDebugClickListener(DebugClickListener listener) {
        mListener = listener;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_debug;
    }

    @Override
    protected void onCreateView() {
        mTvOpenLogLeft.setOnClickListener(this);
        mTvOpenLogRight.setOnClickListener(this);
        mTvSetTime.setOnClickListener(this);
        mTvCloseLog.setOnClickListener(this);
        mTvClose.setOnClickListener(this);
        mTvResetAqpq.setOnClickListener(this);
        mTvPlayNext.setOnClickListener(this);
        mLlsignalContent.setOnClickListener(this);
        mLlVideoContent.setOnClickListener(this);

        //epos
        mLlEposContent.setOnClickListener(this);
        mTvSetEposLocation.setOnClickListener(this);
    }

    @Override
    protected WindowManager.LayoutParams getCustomLayoutParams(WindowManager.LayoutParams layoutParams) {
        layoutParams.width = ScreenUtils.getDpToPx(440);
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.gravity = Gravity.BOTTOM;
        return layoutParams;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_open_log_left:
                if (mListener != null) {
                    mListener.onShowLog(true);
                }
                break;
            case R.id.tv_open_log_right:
                if (mListener != null) {
                    mListener.onShowLog(false);
                }
                break;
            case R.id.tv_close_log:
                if (mListener != null) {
                    mListener.onCloseLog();
                }
                break;
            case R.id.tv_reset_aqpq:
                if (mListener != null) {
                    mListener.onResetAqPq();
                }
                break;
            case R.id.tv_play_next:
                if (mListener != null) {
                    mListener.onPlayNext();
                }
                break;
            case R.id.tv_set_time:
                if (mListener != null) {
                    String signalTime = mEtInputSignalTime.getText().toString().trim();
                    String videoTime = mEtInputVideoTime.getText().toString().trim();
                    mListener.onChangeSignalOrVideoTime(signalTime, videoTime);
                }
                break;
            case R.id.tv_close:
                if (mListener != null) {
                    mListener.onCloseDebugFragment();
                }
                break;
            case R.id.ll_signal_content:
                mEtInputSignalTime.setFocusable(true);
                mEtInputSignalTime.setFocusableInTouchMode(true);
                mEtInputSignalTime.requestFocus();
                mEtInputSignalTime.findFocus();
                break;
            case R.id.ll_video_content:
                mEtInputVideoTime.setFocusable(true);
                mEtInputVideoTime.setFocusableInTouchMode(true);
                mEtInputVideoTime.requestFocus();
                mEtInputVideoTime.findFocus();
                break;
            case R.id.ll_epos_content:
                mEtInputEposLocation.setFocusable(true);
                mEtInputEposLocation.setFocusableInTouchMode(true);
                mEtInputEposLocation.requestFocus();
                mEtInputEposLocation.findFocus();
                break;
            case R.id.tv_set_epos_location:
                if (mListener != null) {
                    // location like : 0,1,2,3
                    String location = mEtInputEposLocation.getText().toString().trim();
                    mListener.onChangeEposPosition(location);
                }
                break;
            default:
                break;
        }
    }
}
