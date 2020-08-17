package com.hisense.hianidraw.cache;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.hisense.storemode.R;
import com.hisense.storemode.utils.ConstantConfig;

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
        Drawable drawable;
        switch (ConstantConfig.mProductName){
            case "HU85A6881FUWA":{
                drawable = getHU85A6881FUWAEpsDrawable(key, context);
                break;
            }
            default:{
                drawable = getA6109FUWAEpsDrawable(key, context);
                break;
            }
        }
        return drawable;
    }

    private Drawable getA6109FUWAEpsDrawable(String key, Context context) {
        Drawable drawable;
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

    private Drawable getHU85A6881FUWAEpsDrawable(String key, Context context) {
        Drawable drawable;
        switch (key) {
            case "epos_hi_top":
                drawable = context.getDrawable(R.drawable.epos_hi_top);
                break;
            case "epos_hi_bg":
                drawable = context.getDrawable(R.drawable.epos_hi_bg);
                break;
            case "epos_hi_pic_des1_hu85a6881fuwa":
                drawable = context.getDrawable(R.drawable.epos_hi_pic_des1_hu85a6881fuwa);
                break;
            case "epos_hi_pic_des2_hu85a6881fuwa":
                drawable = context.getDrawable(R.drawable.epos_hi_pic_des2_hu85a6881fuwa);
                break;
            case "epos_hi_pic_des3_hu85a6881fuwa":
                drawable = context.getDrawable(R.drawable.epos_hi_pic_des3_hu85a6881fuwa);
                break;
            case "epos_hi_pic_des4_hu85a6881fuwa":
                drawable = context.getDrawable(R.drawable.epos_hi_pic_des4_hu85a6881fuwa);
                break;
            case "epos_hi_pic_des5_hu85a6881fuwa":
                drawable = context.getDrawable(R.drawable.epos_hi_pic_des5_hu85a6881fuwa);
                break;
            case "epos_hi_pic_des6_hu85a6881fuwa":
                drawable = context.getDrawable(R.drawable.epos_hi_pic_des6_hu85a6881fuwa);
                break;
            case "epos_hi_pic_des7_hu85a6881fuwa":
                drawable = context.getDrawable(R.drawable.epos_hi_pic_des7_hu85a6881fuwa);
                break;
            case "epos_hi_pic_des8_hu85a6881fuwa":
                drawable = context.getDrawable(R.drawable.epos_hi_pic_des8_hu85a6881fuwa);
                break;
            case "epos_hi_pic_ic1_hu85a6881fuwa":
                drawable = context.getDrawable(R.drawable.epos_hi_pic_ic1_hu85a6881fuwa);
                break;
            case "epos_hi_pic_ic2_hu85a6881fuwa":
                drawable = context.getDrawable(R.drawable.epos_hi_pic_ic2_hu85a6881fuwa);
                break;
            case "epos_hi_pic_ic3_hu85a6881fuwa":
                drawable = context.getDrawable(R.drawable.epos_hi_pic_ic3_hu85a6881fuwa);
                break;
            case "epos_hi_pic_ic4_hu85a6881fuwa":
                drawable = context.getDrawable(R.drawable.epos_hi_pic_ic4_hu85a6881fuwa);
                break;
            case "epos_hi_pic_ic5_hu85a6881fuwa":
                drawable = context.getDrawable(R.drawable.epos_hi_pic_ic5_hu85a6881fuwa);
                break;
            case "epos_hi_pic_ic6_hu85a6881fuwa":
                drawable = context.getDrawable(R.drawable.epos_hi_pic_ic6_hu85a6881fuwa);
                break;
            case "epos_hi_pic_ic7_hu85a6881fuwa":
                drawable = context.getDrawable(R.drawable.epos_hi_pic_ic7_hu85a6881fuwa);
                break;
            case "epos_hi_pic_ic8_hu85a6881fuwa":
                drawable = context.getDrawable(R.drawable.epos_hi_pic_ic8_hu85a6881fuwa);
                break;
            default:
                drawable = context.getDrawable(R.drawable.ic_launcher);
                break;
        }
        return drawable;
    }
}
