package com.hisense.storemode.ui.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import butterknife.ButterKnife;
import me.yokeyword.fragmentation.SupportFragment;


/**
 * fragment基类
 */
public abstract class BaseFragment extends SupportFragment {
    protected View mPrivateView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initArguments();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        mPrivateView = inflater.inflate(getLayoutId(), container, false);
        ButterKnife.bind(this, mPrivateView);
        onCreateView();
        return mPrivateView;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    public void initArguments() {
    }


    protected abstract int getLayoutId();

    protected abstract void onCreateView();


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            // TODO: 4/19/19  isNeed permission
        }
    }

    /**
     * 权限允许，在各界面自行处理回掉事件
     */
    public void onPermissionGranted(String[] permissions) {

    }


    @Nullable
    @Override
    public View getView() {
        View view = super.getView();
        if (view == null) {
            return mPrivateView;
        }
        return view;
    }

}
