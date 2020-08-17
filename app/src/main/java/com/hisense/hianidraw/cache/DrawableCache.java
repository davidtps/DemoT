package com.hisense.hianidraw.cache;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.hisense.storemode.utils.LogUtils;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Create by xuchongxiang at 2019-05-06 7:24 PM
 */

public class DrawableCache {
    protected HashMap<String, Drawable> mContents = new HashMap<>();
    protected Context mContext;

    public void injectContext(Context context) {
        mContext = context;
    }

    public Drawable getDrawableBykey(String key) {
        Drawable content = mContents.get(key);
        if (content == null) {
            content = this.loadDrawable(key);
        }

        return content;
    }

    public Drawable loadDrawable(String key) {
        if (mContext != null) {
            Drawable result = getDrawable(key, mContext);
            if (result != null) {
                mContents.put(key, result);
                return result;
            }
        }
        return null;
    }

    public Drawable getDrawable(String key, Context context) {
        return null;
    }

    public void recycleDrawable(String name) {
        Drawable remove = mContents.remove(name);
        if (remove != null) {
            remove.setCallback(null);
        }

    }


    public void clear() {
        Iterator<Drawable> iterator = mContents.values().iterator();
        while (iterator.hasNext()) {
            Drawable next = iterator.next();
            next.setCallback(null);
        }
        mContents.clear();
    }

}
