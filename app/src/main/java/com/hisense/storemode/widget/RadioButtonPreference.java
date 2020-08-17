package com.hisense.storemode.widget;

import android.content.Context;

import androidx.annotation.RestrictTo;

import android.util.AttributeSet;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.Checkable;
import android.widget.CompoundButton;

import com.hisense.storemode.R;

import androidx.preference.PreferenceManager;
import androidx.preference.PreferenceViewHolder;
import androidx.preference.TwoStatePreference;


/**
 * Created by tianpengsheng on 2019年07月23日 10时25分.
 */
public class RadioButtonPreference extends TwoStatePreference {
    private final RadioButtonPreference.Listener mListener;

    public RadioButtonPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public RadioButtonPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mListener = new RadioButtonPreference.Listener();
        setLayoutResource(R.layout.widget_radio_preference);
//        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RadioPreference, defStyleAttr, defStyleRes);
//        this.setSummaryOn(a.getString(R.styleable.CheckBoxPreference_summaryOn));
//        this.setSummaryOff(a.getString(R.styleable.CheckBoxPreference_summaryOff));
//        a.recycle();
    }

    public RadioButtonPreference(Context context, AttributeSet attrs) {
//        this(context, attrs, TypedArrayUtils.getAttr(context, android.R.attr.checkBoxPreferenceStyle, 16842895));
        this(context, attrs, 0);
    }


    public RadioButtonPreference(Context context) {
        this(context, (AttributeSet) null);
    }

    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);
        this.syncRadioButtonView(holder.findViewById(R.id.radio_button_preference));
        this.syncSummaryView(holder);
    }

    @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
    protected void performClick(View view) {
//        super.performClick(view);
        if (this.isEnabled()) {
            this.onClick();
//            if (this.mOnClickListener == null || !this.mOnClickListener.onPreferenceClick(this)) {
            PreferenceManager preferenceManager = this.getPreferenceManager();
            if (preferenceManager != null) {
                PreferenceManager.OnPreferenceTreeClickListener listener = preferenceManager.getOnPreferenceTreeClickListener();
                if (listener != null && listener.onPreferenceTreeClick(this)) {
                    return;
                }
            }
//            }
        }
        this.syncViewIfAccessibilityEnabled(view);
    }

    private void syncViewIfAccessibilityEnabled(View view) {
        AccessibilityManager accessibilityManager = (AccessibilityManager) this.getContext().getSystemService(Context.ACCESSIBILITY_SERVICE);
        if (accessibilityManager.isEnabled()) {
            View radioView = view.findViewById(R.id.radio_button_preference);
            this.syncRadioButtonView(radioView);
//            View summaryView = view.findViewById(16908304);
//            this.syncSummaryView(summaryView);
        }
    }

    private void syncRadioButtonView(View view) {
        if (view instanceof CompoundButton) {
            ((CompoundButton) view).setOnCheckedChangeListener((CompoundButton.OnCheckedChangeListener) null);
        }

        if (view instanceof Checkable) {
            ((Checkable) view).setChecked(this.mChecked);
        }

        if (view instanceof CompoundButton) {
            ((CompoundButton) view).setOnCheckedChangeListener(this.mListener);
        }

    }

    private class Listener implements CompoundButton.OnCheckedChangeListener {
        Listener() {
        }

        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (!RadioButtonPreference.this.callChangeListener(isChecked)) {
                buttonView.setChecked(!isChecked);
            } else {
                RadioButtonPreference.this.setChecked(isChecked);
            }
        }
    }
}
