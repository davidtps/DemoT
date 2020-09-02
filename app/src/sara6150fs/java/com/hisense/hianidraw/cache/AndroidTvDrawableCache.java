package com.hisense.hianidraw.cache;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.hisense.storemode.R;


/**
 * @author tianpengsheng
 * create at   10/9/19 11:34 AM
 */

public class AndroidTvDrawableCache extends DrawableCache {

    private boolean mIsTmp;

    public boolean isTmp() {
        return mIsTmp;
    }

    @Override
    public Drawable getDrawable(String key, Context context) {
        if (mIsTmp) {
            return context.getDrawable(R.drawable.ic_launcher);
        }
        Drawable drawable = null;
        switch (key) {
            case "epos_hi_top":
                drawable = context.getDrawable(R.drawable.epos_hi_top);
                break;
            case "epos_hi_bg":
                drawable = context.getDrawable(R.drawable.epos_hi_bg);
                break;
            case "epos_hi_pic_des1":
                drawable = context.getDrawable(R.drawable.epos_hi_pic_des1);
                break;
            case "epos_hi_pic_des2":
                drawable = context.getDrawable(R.drawable.epos_hi_pic_des2);
                break;
            case "epos_hi_pic_des3":
                drawable = context.getDrawable(R.drawable.epos_hi_pic_des3);
                break;
            case "epos_hi_pic_des4":
                drawable = context.getDrawable(R.drawable.epos_hi_pic_des4);
                break;
            case "epos_hi_second_bg":
                drawable = context.getDrawable(R.drawable.epos_hi_second_bg);
                break;
            case "epos_hi_third_line":
                drawable = context.getDrawable(R.drawable.epos_hi_third_line);
                break;
            case "epos_hi_third_pic_ic1":
                drawable = context.getDrawable(R.drawable.epos_hi_third_pic_ic1);
                break;
            case "epos_hi_third_pic_ic2":
                drawable = context.getDrawable(R.drawable.epos_hi_third_pic_ic2);
                break;
            case "epos_hi_third_pic_ic3":
                drawable = context.getDrawable(R.drawable.epos_hi_third_pic_ic3);
                break;
            case "epos_hi_pic_des5":
                drawable = context.getDrawable(R.drawable.epos_hi_pic_des5);
                break;
            case "epos_hi_pic_des6":
                drawable = context.getDrawable(R.drawable.epos_hi_pic_des6);
                break;
            case "epos_hi_pic_des7":
                drawable = context.getDrawable(R.drawable.epos_hi_pic_des7);
                break;
            case "epos_hi_pic_des8":
                drawable = context.getDrawable(R.drawable.epos_hi_pic_des8);
                break;
            case "epos_hi_pic_ic1":
                drawable = context.getDrawable(R.drawable.epos_hi_pic_ic1);
                break;
            case "epos_hi_pic_ic2":
                drawable = context.getDrawable(R.drawable.epos_hi_pic_ic2);
                break;
            case "epos_hi_pic_ic3":
                drawable = context.getDrawable(R.drawable.epos_hi_pic_ic3);
                break;
            case "epos_hi_pic_ic4":
                drawable = context.getDrawable(R.drawable.epos_hi_pic_ic4);
                break;
            case "epos_hi_pic_ic5":
                drawable = context.getDrawable(R.drawable.epos_hi_pic_ic5);
                break;
            case "epos_hi_pic_ic6":
                drawable = context.getDrawable(R.drawable.epos_hi_pic_ic6);
                break;
            case "epos_hi_pic_ic7":
                drawable = context.getDrawable(R.drawable.epos_hi_pic_ic7);
                break;
            case "epos_hi_pic_ic8":
                drawable = context.getDrawable(R.drawable.epos_hi_pic_ic8);
                break;
            default:
                drawable = context.getDrawable(R.drawable.ic_launcher);
                break;
        }
        return drawable;
    }
}
