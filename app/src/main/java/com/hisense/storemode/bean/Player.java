package com.hisense.storemode.bean;

import java.util.List;

/**
 * Created by tianpengsheng on 2019年04月23日 17时29分.
 */
public class Player {
    public int mResourceId; //R.drawable.* resource
    public String mPath;    //video or pic
    public List<Object> mAllPicResPath;// object:string/int
    /**
     * // 1:usb video
     * // 2:build-in video
     * // 3:usb pic
     * // 4:build-in pic
     * // 5:signal
     * // 6:apk drawable pic---if no build-in pic,pic default show
     * // 7:all pic res
     */
    public int mType;

    public Player(String path, int type) {
        this.mPath = path;
        this.mType = type;
    }

    public Player(int resId, int type) {
        this.mResourceId = resId;
        this.mType = type;
    }

    public Player(List<Object> mAllPicResPath, int mType) {
        this.mAllPicResPath = mAllPicResPath;
        this.mType = mType;
    }
}
