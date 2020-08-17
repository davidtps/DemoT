package com.hisense.hianidraw.draw;

import com.hisense.hianidraw.cache.DrawableCache;
import com.hisense.hianidraw.data.Getter;

import java.util.Iterator;
import java.util.List;

/**
 * Create by xuchongxiang at 2019-05-07 10:44 AM
 */

public class DrawHelper {
    private long mDuration;
    private final List<DrawElement> mElements;
    private DrawableCache mDrawableCache;

    public DrawHelper(Getter<DrawElement> elementGetter) {
        super();
        mElements = elementGetter.getContent();
        mDrawableCache = new DrawableCache();
        calElements();
    }

    public long getDuration() {
        return mDuration;
    }

    public void setDuration(long duration) {
        mDuration = duration;
    }

    public List<DrawElement> getElements() {
        return mElements;
    }

    public DrawableCache getDrawableCache() {
        return mDrawableCache;
    }

    public void setDrawableCache(DrawableCache drawableCache) {
        this.mDrawableCache = drawableCache;
    }

    public void processStagesInfo() {

    }

    private long calElements() {
        long duration = 0L;

        Iterator<DrawElement> iterator = mElements.iterator();
        while (iterator.hasNext()) {
            DrawElement d = iterator.next();
            d.injectDrawHelper(this);
        }
        return duration;
    }


}
