package com.hisense.hianidraw.cache;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.hisense.storemode.R;
import com.hisense.storemode.utils.ConstantConfig;

/**
 * no need ,a8020ex don't need show epos
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
        Drawable drawable;
        switch (ConstantConfig.mProductName){
            case "50M5306EXT":////EM Toshiba
            case "55M5306EXT":
            case "65M5306EXT":
            case "75M5306EXT": {
                if (ConstantConfig.mBuildSoftVersion[0].equals("V1000")) {
                    drawable = getM5306EXT1000EpsDrawable(key, context);
                }
                else{
                    drawable = getM5306EXT1000EpsDrawable(key, context);
                }
                break;
            }
            case "TT43M540UW":////TW Toshiba
            case "TT50M540UW":
            case "TT65M540UW":
            case "TT55M540UW": {
                drawable = getTTM540UWEpsDrawable(key, context);
                break;
            }
            default: {
                drawable = getM5306EXT1000EpsDrawable(key, context);
                break;
            }
        }

        return drawable;
    }

    private Drawable getM5306EXT1000EpsDrawable(String key, Context context){
        Drawable drawable;
        switch (key) {
            case "epos_ic_one_m5306ext1000":
                drawable = context.getDrawable(R.drawable.epos_ic_one_m5306ext1000);
                break;
            case "epos_ic_two_m5306ext1000":
                drawable = context.getDrawable(R.drawable.epos_ic_two_m5306ext1000);
                break;
            default:
                drawable = context.getDrawable(R.drawable.ic_launcher);
                break;
        }

        return drawable;
    }

    private Drawable getTTM540UWEpsDrawable(String key, Context context){
        Drawable drawable;
        switch (key) {
            case "epos_ic_one_ttm540uw":
                drawable = context.getDrawable(R.drawable.epos_ic_one_ttm540uw);
                break;
            case "epos_ic_two_ttm540uw":
                drawable = context.getDrawable(R.drawable.epos_ic_two_ttm540uw);
                break;
            default:
                drawable = context.getDrawable(R.drawable.ic_launcher);
                break;
        }
        return drawable;
    }
}
