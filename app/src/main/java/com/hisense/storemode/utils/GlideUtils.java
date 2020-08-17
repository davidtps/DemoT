package com.hisense.storemode.utils;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.hisense.storemode.R;
import com.hisense.storemode.StoreModeApplication;
import com.hisense.storemode.manager.AppManager;
import com.hisense.storemode.manager.StoreModeManager;
import com.hisense.storemode.ui.activity.MainActivity;

import androidx.annotation.NonNull;

/**
 * Created by tianpengsheng on 2019年04月22日 09时53分.
 */
public class GlideUtils {

    private static final int sPlaceholderColor = R.color.black;
    private static final int sErrorColor = R.color.white;
    private static final int sPlaceColor = R.color.translucent;
    private static final String TAG = "GlideUtils";


    /*
     * load internet image (default)
     */
    public static void loadImageForMallLogo(String url, ImageView imageView, int width, int height) {
        RequestOptions options = new RequestOptions()
                .optionalFitCenter()
                .override(width, height)
                .placeholder(sPlaceholderColor)
                .error(sErrorColor)
                //.priority(Priority.HIGH)
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        getLoadUrl(url).apply(options).into(imageView);

    }

    public static void loadImageForMallLogo(String url, ImageView imageView) {
        LogUtils.d(TAG, "loadImageForMallLogo() file" + url);
        RequestOptions options = new RequestOptions()
                .fitCenter()
                .placeholder(sPlaceholderColor)
                .error(sErrorColor)
                .priority(Priority.HIGH)
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        getLoadUrl(url).apply(options).into(imageView);

    }

    public static void loadImage(String url, ImageView imageView) {
        LogUtils.d(TAG, "loadImage() file" + url);
        if (!FileUtil.isFileExist(url)) {
            LogUtils.d(TAG, "loadImage() file" + url + " is not exist");
            PreferenceUtils.getInstance().save(ConstantConfig.RESET_PALY_POLICY, true);
            StoreModeManager.getInstance().start();
            return;
        }
        RequestOptions options = new RequestOptions()
                .fitCenter()
                .placeholder(sPlaceholderColor)
                .error(sErrorColor)
                .priority(Priority.HIGH)
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        getLoadUrl(url).apply(options).into(imageView);

    }

    public static void loadImageLocalDrawable(String url, ImageView imageView) {
        LogUtils.d(TAG, "loadImageLocalDrawable() file  " + url);
        RequestOptions options = new RequestOptions()
                .fitCenter()
                .placeholder(sPlaceholderColor)
                .error(sErrorColor)
                .priority(Priority.HIGH)
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        getLoadUrl(url).apply(options).into(imageView);

    }


    /**
     * load local image
     *
     * @param iv
     * @param localPath
     */
    public static void loadLocalImageForAutoSwitch(ImageView iv, String localPath) {
        if (!FileUtil.isFileExist(localPath)) {
            LogUtils.d(TAG, "loadLocalImageForAutoSwitch() file" + localPath + " is not exist");
            PreferenceUtils.getInstance().save(ConstantConfig.RESET_PALY_POLICY, true);
            StoreModeManager.getInstance().start();
            return;
        }
        //local file
        RequestOptions options = new RequestOptions()
                .fitCenter()
                .placeholder(sPlaceColor)
                .error(sErrorColor)
                //  .priority(Priority.HIGH)
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        //load image
        getLoadUrl(localPath).apply(options).into(iv);
    }

    /**
     * custom width and height
     *
     * @param url
     * @param imageView
     * @param width
     * @param height
     */
    public static void loadImageSize(String url, ImageView imageView, int width, int height) {
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(sPlaceholderColor)
                .error(sErrorColor)
                .override(width, height)
                // .priority(Priority.HIGH)
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        getLoadUrl(url).apply(options).into(imageView);

    }

    @NonNull
    private static RequestBuilder<Drawable> getLoadUrl(String url) {
        Activity activity = AppManager.getInstance().currentActivity();
        if (activity != null && activity instanceof MainActivity) {
            LogUtils.d(TAG, "getLoadUrl()  activity:" + activity);
            return Glide.with(activity).load(url);
        } else {
            return Glide.with(StoreModeApplication.sContext).load(url);
        }
    }


}