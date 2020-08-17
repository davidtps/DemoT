package com.hisense.storemode.ui.activity;

import android.os.Bundle;
import android.view.WindowManager;

import com.hisense.storemode.utils.CustomFragmentAnimator;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.yokeyword.fragmentation.SupportActivity;
import me.yokeyword.fragmentation.anim.FragmentAnimator;


/**
 * support activity的基类
 */
public abstract class BaseActivity extends SupportActivity {
    private Unbinder mUnbinder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(getLayoutId());
        mUnbinder = ButterKnife.bind(this);
        afterCreate();
        loadFragment(savedInstanceState);
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
    protected abstract void afterCreate();

    protected void loadFragment(Bundle bundle) {
    }

    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
        return new CustomFragmentAnimator();
    }
}
