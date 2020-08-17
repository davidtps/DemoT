package com.hisense.hianidraw.cache;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.hisense.storemode.R;

/**
 * Create by xuchongxiang at 2019-05-06 7:37 PM
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
            case "epos_logo":
                drawable = context.getDrawable(R.drawable.epos_logo);
                break;
            case "epos_android_pic":
                drawable = context.getDrawable(R.drawable.epos_android_pic);
                break;

            case "epos_googleplay_pic":
                drawable = context.getDrawable(R.drawable.epos_googleplay_pic);
                break;
            case "epos_dolby_pic":
                drawable = context.getDrawable(R.drawable.epos_dolby_pic);
                break;
            case "epos_glassistant_pic":
                drawable = context.getDrawable(R.drawable.epos_glassistant_pic);
                break;
            case "epos_smart_pic":
                drawable = context.getDrawable(R.drawable.epos_smart_pic);
                break;
            case "epos_4k_pic":
                drawable = context.getDrawable(R.drawable.epos_4k_pic);
                break;
            case "epos_android":
                drawable = context.getDrawable(R.drawable.epos_android);
                break;
            case "epos_googleplay":
                drawable = context.getDrawable(R.drawable.epos_googleplay);
                break;
            case "epos_dolby":
                drawable = context.getDrawable(R.drawable.epos_dolby);
                break;
            case "epos_glassistant":
                drawable = context.getDrawable(R.drawable.epos_glassistant);
                break;
            case "epos_smart":
                drawable = context.getDrawable(R.drawable.epos_smart);
                break;
            case "epos_4k":
                drawable = context.getDrawable(R.drawable.epos_4k);
                break;
            case "epos_android_des":
                drawable = context.getDrawable(R.drawable.epos_android_des);
                break;
            case "epos_googleplay_des":
                drawable = context.getDrawable(R.drawable.epos_googleplay_des);
                break;
            case "epos_dolby_des":
                drawable = context.getDrawable(R.drawable.epos_dolby_des);
                break;
            case "epos_glassistant_des":
                drawable = context.getDrawable(R.drawable.epos_glassistant_des);
                break;
            case "epos_smart_des":
                drawable = context.getDrawable(R.drawable.epos_smart_des);
                break;
            case "epos_4k_des":
                drawable = context.getDrawable(R.drawable.epos_4k_des);
                break;
            default:
                drawable = context.getDrawable(R.drawable.ic_launcher);
                break;
        }
        return drawable;
    }
}
