package com.hisense.storemode.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;

import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * activity for leanback
 */
public abstract class BaseNoCompatActivity extends Activity {
    private Unbinder mUnbinder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        if (getLayoutId() != 0) {
            setContentView(getLayoutId());
        }
        mUnbinder = ButterKnife.bind(this);
        afterCreate(savedInstanceState);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mUnbinder != null)
            mUnbinder.unbind();
    }


    /**
     * 添加当前界面的布局
     */
    protected abstract int getLayoutId();

    /**
     * 在父类create完之后运行
     */
    protected abstract void afterCreate(Bundle savedInstanceState);

}
