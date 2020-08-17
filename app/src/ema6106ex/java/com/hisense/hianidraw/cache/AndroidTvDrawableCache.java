package com.hisense.hianidraw.cache;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.hisense.storemode.R;
import com.hisense.storemode.utils.ConstantConfig;

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
        Drawable drawable;
        switch (ConstantConfig.mProductName) {
            case "HX70A6109FUWT":
            ////case "HX58A6151FUW":
            case "58A71FXAT":///rename HX58A6151FUW
            case "HX43A6151FUW":
            case "HX50A6151FUW":
            case "HX55A6151FUW": {
                drawable = getA6151FUWEpsDrawable(key, context);
                break;
            }
            case "HX50A6106FUW":
            case "HX55A6106FUW":
            case "HX65A6106FUW": {
                if (ConstantConfig.mBuildSoftVersion[0].equals("V0010")) {
                    drawable = getA6106FUW0010EpsDrawable(key, context);
                }
                else{
                    drawable = getA6106EXEpsDrawable(key, context);
                }
                break;
            }
            case "HX55A6900FUWT":
            case "HX65A6900FUWT":{
                drawable = getA6900FUWTEpsDrawable(key, context);
                break;
            }
            case "55A6106EX": {
                drawable = getA6106EXEpsDrawable(key, context);
                break;
            }
            ////case "43A53EXAT":
            ////case "50A53EXAT":
            case "55A53EXAT":
            case "65A53EXAT": {
                drawable = getA53EXATEpsDrawable(key, context);
                break;
            }
            default: {
                drawable = getA6106EXEpsDrawable(key, context);
                break;
            }
        }


        return drawable;
    }

    //A6106Ex epos resource
    private Drawable getA6106EXEpsDrawable(String key, Context context) {
        Drawable drawable;
        switch (key) {
            case "epos_hisense_bg":
                drawable = context.getDrawable(R.drawable.epos_hisense_bg);
                break;
            case "epos_hisense":
                drawable = context.getDrawable(R.drawable.epos_hisense);
                break;
            case "epos_android_tv":
                drawable = context.getDrawable(R.drawable.epos_android_tv);
                break;
            case "epos_4k_ultra_hd":
                drawable = context.getDrawable(R.drawable.epos_4k_ultra_hd);
                break;
            case "epos_hdr":
                drawable = context.getDrawable(R.drawable.epos_hdr);
                break;
            case "epos_4kuhd":
                drawable = context.getDrawable(R.drawable.epos_4kuhd);
                break;
            case "epos_depthenhancer":
                drawable = context.getDrawable(R.drawable.epos_depthenhancer);
                break;
            case "epos_dts_virtual":
                drawable = context.getDrawable(R.drawable.epos_dts_virtual);
                break;
            case "epos_precisioncolour":
                drawable = context.getDrawable(R.drawable.epos_precisioncolour);
                break;
            case "epos_voice":
                drawable = context.getDrawable(R.drawable.epos_voice);
                break;
            case "epos_hdr_pic":
                drawable = context.getDrawable(R.drawable.epos_hdr_pic);
                break;
            case "epos_android_tv_pic":
                drawable = context.getDrawable(R.drawable.epos_android_tv_pic);
                break;
            case "epos_4k_ultra_hd_pic":
                drawable = context.getDrawable(R.drawable.epos_4k_ultra_hd_pic);
                break;
            case "epos_4kuhd_pic":
                drawable = context.getDrawable(R.drawable.epos_4kuhd_pic);
                break;
            case "epos_dts_virtual_pic":
                drawable = context.getDrawable(R.drawable.epos_dts_virtual_pic);
                break;
            case "epos_depthenhancer_pic":
                drawable = context.getDrawable(R.drawable.epos_depthenhancer_pic);
                break;
            case "epos_precisioncolour_pic":
                drawable = context.getDrawable(R.drawable.epos_precisioncolour_pic);
                break;
            case "epos_voice_pic":
                drawable = context.getDrawable(R.drawable.epos_voice_pic);
                break;
            default:
                drawable = context.getDrawable(R.drawable.ic_launcher);
                break;
        }
        return drawable;
    }
    ///A6151FUW
    private Drawable getA6151FUWEpsDrawable(String key, Context context) {
        Drawable drawable;
        switch (key) {
            case "epos_hi_top_a6151fuw":
                drawable = context.getDrawable(R.drawable.epos_hi_top_a6151fuw);
                break;
            case "epos_hi_bg_a6151fuw":
                drawable = context.getDrawable(R.drawable.epos_hi_bg_a6151fuw);
                break;
            case "epos_hi_pic_des1_a6151fuw":
                drawable = context.getDrawable(R.drawable.epos_hi_pic_des1_a6151fuw);
                break;
            case "epos_hi_pic_des2_a6151fuw":
                drawable = context.getDrawable(R.drawable.epos_hi_pic_des2_a6151fuw);
                break;
            case "epos_hi_pic_des3_43inch_a6151fuw":////24W Powerful Sound
                drawable = context.getDrawable(R.drawable.epos_hi_pic_des3_43inch_a6151fuw);
                break;
            case "epos_hi_pic_des3_50inch_a6151fuw":////30W Powerful Sound
                drawable = context.getDrawable(R.drawable.epos_hi_pic_des3_50inch_a6151fuw);
                break;
            case "epos_hi_pic_des3_70a6109fuwt":////36W Powerful Sound
                drawable = context.getDrawable(R.drawable.epos_hi_pic_des3_70a6109fuwt);
                break;
            case "epos_hi_pic_des4_a6151fuw":
                drawable = context.getDrawable(R.drawable.epos_hi_pic_des4_a6151fuw);
                break;
            case "epos_hi_pic_des5_a6151fuw":
                drawable = context.getDrawable(R.drawable.epos_hi_pic_des5_a6151fuw);
                break;
            case "epos_hi_pic_des6_a6151fuw":
                drawable = context.getDrawable(R.drawable.epos_hi_pic_des6_a6151fuw);
                break;
            case "epos_hi_pic_des7_a6151fuw":
                drawable = context.getDrawable(R.drawable.epos_hi_pic_des7_a6151fuw);
                break;
            case "epos_hi_pic_des8_a6151fuw":
                drawable = context.getDrawable(R.drawable.epos_hi_pic_des8_a6151fuw);
                break;
            case "epos_hi_pic_ic1_a6151fuw":
                drawable = context.getDrawable(R.drawable.epos_hi_pic_ic1_a6151fuw);
                break;
            case "epos_hi_pic_ic2_a6151fuw":
                drawable = context.getDrawable(R.drawable.epos_hi_pic_ic2_a6151fuw);
                break;
            case "epos_hi_pic_ic3_a6151fuw":
                drawable = context.getDrawable(R.drawable.epos_hi_pic_ic3_a6151fuw);
                break;
            case "epos_hi_pic_ic4_a6151fuw":
                drawable = context.getDrawable(R.drawable.epos_hi_pic_ic4_a6151fuw);
                break;
            case "epos_hi_pic_ic5_a6151fuw":
                drawable = context.getDrawable(R.drawable.epos_hi_pic_ic5_a6151fuw);
                break;
            case "epos_hi_pic_ic6_a6151fuw":
                drawable = context.getDrawable(R.drawable.epos_hi_pic_ic6_a6151fuw);
                break;
            case "epos_hi_pic_ic7_a6151fuw":
                drawable = context.getDrawable(R.drawable.epos_hi_pic_ic7_a6151fuw);
                break;
            case "epos_hi_pic_ic8_a6151fuw":
                drawable = context.getDrawable(R.drawable.epos_hi_pic_ic8_a6151fuw);
                break;
            default:
                drawable = context.getDrawable(R.drawable.ic_launcher);
                break;
        }
        return drawable;
    }
    ///A6106FUW(0010)
    private Drawable getA6106FUW0010EpsDrawable(String key, Context context) {
        Drawable drawable;
        switch (key) {
            case "epos_hi_top_a6106fuw0010":
                drawable = context.getDrawable(R.drawable.epos_hi_top_a6106fuw0010);
                break;
            case "epos_hi_bg_a6106fuw0010":
                drawable = context.getDrawable(R.drawable.epos_hi_bg_a6106fuw0010);
                break;
            case "epos_hi_pic_des1_a6106fuw0010":
                drawable = context.getDrawable(R.drawable.epos_hi_pic_des1_a6106fuw0010);
                break;
            case "epos_hi_pic_des2_a6106fuw0010":
                drawable = context.getDrawable(R.drawable.epos_hi_pic_des2_a6106fuw0010);
                break;
            case "epos_hi_pic_des3_a6106fuw0010":
                drawable = context.getDrawable(R.drawable.epos_hi_pic_des3_a6106fuw0010);
                break;
            case "epos_hi_pic_des4_a6106fuw0010":
                drawable = context.getDrawable(R.drawable.epos_hi_pic_des4_a6106fuw0010);
                break;
            case "epos_hi_pic_des5_a6106fuw0010":
                drawable = context.getDrawable(R.drawable.epos_hi_pic_des5_a6106fuw0010);
                break;
            case "epos_hi_pic_des6_a6106fuw0010":
                drawable = context.getDrawable(R.drawable.epos_hi_pic_des6_a6106fuw0010);
                break;
            case "epos_hi_pic_des7_a6106fuw0010":
                drawable = context.getDrawable(R.drawable.epos_hi_pic_des7_a6106fuw0010);
                break;
            case "epos_hi_pic_des8_55a6106fuw0010":
                drawable = context.getDrawable(R.drawable.epos_hi_pic_des8_55a6106fuw0010);
                break;
            case "epos_hi_pic_des8_65a6106fuw0010":
                drawable = context.getDrawable(R.drawable.epos_hi_pic_des8_65a6106fuw0010);
                break;
            case "epos_hi_pic_ic1_a6106fuw0010":
                drawable = context.getDrawable(R.drawable.epos_hi_pic_ic1_a6106fuw0010);
                break;
            case "epos_hi_pic_ic2_a6106fuw0010":
                drawable = context.getDrawable(R.drawable.epos_hi_pic_ic2_a6106fuw0010);
                break;
            case "epos_hi_pic_ic3_a6106fuw0010":
                drawable = context.getDrawable(R.drawable.epos_hi_pic_ic3_a6106fuw0010);
                break;
            case "epos_hi_pic_ic4_a6106fuw0010":
                drawable = context.getDrawable(R.drawable.epos_hi_pic_ic4_a6106fuw0010);
                break;
            case "epos_hi_pic_ic5_a6106fuw0010":
                drawable = context.getDrawable(R.drawable.epos_hi_pic_ic5_a6106fuw0010);
                break;
            case "epos_hi_pic_ic6_a6106fuw0010":
                drawable = context.getDrawable(R.drawable.epos_hi_pic_ic6_a6106fuw0010);
                break;
            case "epos_hi_pic_ic7_a6106fuw0010":
                drawable = context.getDrawable(R.drawable.epos_hi_pic_ic7_a6106fuw0010);
                break;
            case "epos_hi_pic_ic8_55a6106fuw0010":
                drawable = context.getDrawable(R.drawable.epos_hi_pic_ic8_55a6106fuw0010);
                break;
            case "epos_hi_pic_ic8_65a6106fuw0010":
                drawable = context.getDrawable(R.drawable.epos_hi_pic_ic8_65a6106fuw0010);
                break;
            default:
                drawable = context.getDrawable(R.drawable.ic_launcher);
                break;
        }
        return drawable;
    }
    ///A6900FUWT
    private Drawable getA6900FUWTEpsDrawable(String key, Context context) {
        Drawable drawable;
        switch (key) {
            case "epos_hi_top_a6900fuwt":
                drawable = context.getDrawable(R.drawable.epos_hi_top_a6900fuwt);
                break;
            case "epos_hi_bg_a6900fuwt":
                drawable = context.getDrawable(R.drawable.epos_hi_bg_a6900fuwt);
                break;
            case "epos_hi_pic_des1_a6900fuwt":
                drawable = context.getDrawable(R.drawable.epos_hi_pic_des1_a6900fuwt);
                break;
            case "epos_hi_pic_des2_a6900fuwt":
                drawable = context.getDrawable(R.drawable.epos_hi_pic_des2_a6900fuwt);
                break;
            case "epos_hi_pic_des3_a6900fuwt":
                drawable = context.getDrawable(R.drawable.epos_hi_pic_des3_a6900fuwt);
                break;
            case "epos_hi_pic_des4_a6900fuwt":
                drawable = context.getDrawable(R.drawable.epos_hi_pic_des4_a6900fuwt);
                break;
            case "epos_hi_pic_des5_a6900fuwt":
                drawable = context.getDrawable(R.drawable.epos_hi_pic_des5_a6900fuwt);
                break;
            case "epos_hi_pic_des6_a6900fuwt":
                drawable = context.getDrawable(R.drawable.epos_hi_pic_des6_a6900fuwt);
                break;
            case "epos_hi_pic_des7_a6900fuwt":
                drawable = context.getDrawable(R.drawable.epos_hi_pic_des7_a6900fuwt);
                break;
            case "epos_hi_pic_des8_a6900fuwt":
                drawable = context.getDrawable(R.drawable.epos_hi_pic_des8_a6900fuwt);
                break;
            case "epos_hi_pic_ic1_a6900fuwt":
                drawable = context.getDrawable(R.drawable.epos_hi_pic_ic1_a6900fuwt);
                break;
            case "epos_hi_pic_ic2_a6900fuwt":
                drawable = context.getDrawable(R.drawable.epos_hi_pic_ic2_a6900fuwt);
                break;
            case "epos_hi_pic_ic3_a6900fuwt":
                drawable = context.getDrawable(R.drawable.epos_hi_pic_ic3_a6900fuwt);
                break;
            case "epos_hi_pic_ic4_a6900fuwt":
                drawable = context.getDrawable(R.drawable.epos_hi_pic_ic4_a6900fuwt);
                break;
            case "epos_hi_pic_ic5_a6900fuwt":
                drawable = context.getDrawable(R.drawable.epos_hi_pic_ic5_a6900fuwt);
                break;
            case "epos_hi_pic_ic6_a6900fuwt":
                drawable = context.getDrawable(R.drawable.epos_hi_pic_ic6_a6900fuwt);
                break;
            case "epos_hi_pic_ic7_a6900fuwt":
                drawable = context.getDrawable(R.drawable.epos_hi_pic_ic7_a6900fuwt);
                break;
            case "epos_hi_pic_ic8_a6900fuwt":
                drawable = context.getDrawable(R.drawable.epos_hi_pic_ic8_a6900fuwt);
                break;
            default:
                drawable = context.getDrawable(R.drawable.ic_launcher);
                break;
        }
        return drawable;
    }
    ///A53EXAT
    private Drawable getA53EXATEpsDrawable(String key, Context context) {
        Drawable drawable;
        switch (key) {
            case "epos_hi_top_a53exat":
                drawable = context.getDrawable(R.drawable.epos_hi_top_a53exat);
                break;
            case "epos_hi_bg_a53exat":
                drawable = context.getDrawable(R.drawable.epos_hi_bg_a53exat);
                break;
            case "epos_hi_pic_des1_55inch_a53exat":///for 55/65 inch
                drawable = context.getDrawable(R.drawable.epos_hi_pic_des1_55inch_a53exat);
                break;
            case "epos_hi_pic_des2_a53exat":
                drawable = context.getDrawable(R.drawable.epos_hi_pic_des2_a53exat);
                break;
            case "epos_hi_pic_des3_a53exat":
                drawable = context.getDrawable(R.drawable.epos_hi_pic_des3_a53exat);
                break;
            case "epos_hi_pic_des4_a53exat":
                drawable = context.getDrawable(R.drawable.epos_hi_pic_des4_a53exat);
                break;
            case "epos_hi_pic_des5_a53exat":
                drawable = context.getDrawable(R.drawable.epos_hi_pic_des5_a53exat);
                break;
            case "epos_hi_pic_des6_a53exat":
                drawable = context.getDrawable(R.drawable.epos_hi_pic_des6_a53exat);
                break;
            case "epos_hi_pic_des7_a53exat":
                drawable = context.getDrawable(R.drawable.epos_hi_pic_des7_a53exat);
                break;
            case "epos_hi_pic_des8_a53exat":
                drawable = context.getDrawable(R.drawable.epos_hi_pic_des8_a53exat);
                break;
            case "epos_hi_pic_ic1_a53exat":
                drawable = context.getDrawable(R.drawable.epos_hi_pic_ic1_a53exat);
                break;
            case "epos_hi_pic_ic2_a53exat":
                drawable = context.getDrawable(R.drawable.epos_hi_pic_ic2_a53exat);
                break;
            case "epos_hi_pic_ic3_a53exat":
                drawable = context.getDrawable(R.drawable.epos_hi_pic_ic3_a53exat);
                break;
            case "epos_hi_pic_ic4_a53exat":
                drawable = context.getDrawable(R.drawable.epos_hi_pic_ic4_a53exat);
                break;
            case "epos_hi_pic_ic5_a53exat":
                drawable = context.getDrawable(R.drawable.epos_hi_pic_ic5_a53exat);
                break;
            case "epos_hi_pic_ic6_a53exat":
                drawable = context.getDrawable(R.drawable.epos_hi_pic_ic6_a53exat);
                break;
            case "epos_hi_pic_ic7_a53exat":
                drawable = context.getDrawable(R.drawable.epos_hi_pic_ic7_a53exat);
                break;
            case "epos_hi_pic_ic8_a53exat":
                drawable = context.getDrawable(R.drawable.epos_hi_pic_ic8_a53exat);
                break;
            default:
                drawable = context.getDrawable(R.drawable.ic_launcher);
                break;
        }
        return drawable;
    }
}
