package com.hisense.storemode.manager.resource;

/**
 * Created by tianpengsheng on 2019年04月18日 16时40分.
 * 视频、图片拷贝
 */
@Deprecated
public class ResManamger {

    private volatile static ResManamger sInstance;

    private ResManamger() {
    }

    public static ResManamger getInstance() {    //对获取实例的方法进行同步
        if (sInstance == null) {
            synchronized (ResManamger.class) {
                if (sInstance == null)
                    sInstance = new ResManamger();
            }
        }
        return sInstance;
    }


}
