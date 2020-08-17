package com.hisense.storemode.utils;

import android.os.Parcel;
import android.os.Parcelable;

import com.hisense.storemode.R;

import me.yokeyword.fragmentation.anim.FragmentAnimator;

/**
 * @author tianpengsheng
 * create at   5/9/19 4:13 PM
 */
public class CustomFragmentAnimator extends FragmentAnimator implements Parcelable {

    public CustomFragmentAnimator() {
        enter = R.anim.pop_enter_anim;
        exit = R.anim.pop_exit_anim;
        popEnter = R.anim.pop_enter_anim;
        popExit = R.anim.pop_exit_anim;
    }

    protected CustomFragmentAnimator(Parcel in) {
        super(in);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CustomFragmentAnimator> CREATOR = new Creator<CustomFragmentAnimator>() {
        @Override
        public CustomFragmentAnimator createFromParcel(Parcel in) {
            return new CustomFragmentAnimator(in);
        }

        @Override
        public CustomFragmentAnimator[] newArray(int size) {
            return new CustomFragmentAnimator[size];
        }
    };
}
