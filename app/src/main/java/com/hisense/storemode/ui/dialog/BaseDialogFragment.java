package com.hisense.storemode.ui.dialog;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;

import android.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.hisense.storemode.R;
import com.hisense.storemode.utils.ScreenUtils;

import butterknife.ButterKnife;

/**
 * dialogfragment 基类
 * 全屏展示的
 */

public abstract class BaseDialogFragment extends DialogFragment {

    protected Context mContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setStyle(STYLE_NORMAL, R.style.dialog_full_screen);

        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        final View view = inflater.inflate(getLayoutId(), container);
        ButterKnife.bind(this, view);
        onCreateView();
        return view;
    }

    protected abstract int getLayoutId();


    protected abstract void onCreateView();

    @Override
    public void onStart() {
        final WindowManager.LayoutParams layoutParams = getDialog().getWindow().getAttributes();
        getDialog().getWindow().setAttributes(getCustomLayoutParams(layoutParams));
        getDialog().getWindow().setWindowAnimations(R.style.dialog_enter_exit);  //添加动画
        super.onStart();
    }

    protected WindowManager.LayoutParams getCustomLayoutParams(WindowManager.LayoutParams layoutParams) {
        layoutParams.width = ScreenUtils.mWidth;
        layoutParams.height = ScreenUtils.mHeight;
        layoutParams.gravity = Gravity.BOTTOM;
        return layoutParams;
    }

}
