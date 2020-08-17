package com.hisense.storemode.manager;

import android.os.Handler;
import android.os.HandlerThread;

import com.hisense.storemode.manager.play.PlayManager;
import com.hisense.storemode.manager.task.BackgroundTaskManager;
import com.hisense.storemode.utils.ConstantConfig;
import com.hisense.storemode.utils.LogUtils;


/**
 * Created by tianpengsheng on 2019年04月19日 10时34分.
 * init ConstantConfig
 * invoke playManager and EposManager
 */
public class StoreModeManager {
    private final String TAG = "StoreModeManager";

    private volatile static StoreModeManager sInstance;

    private Handler mTaskHandler;

    private StoreModeManager() {
        init();
    }

    public static StoreModeManager getInstance() {
        if (sInstance == null) {
            synchronized (StoreModeManager.class) {
                if (sInstance == null) {
                    sInstance = new StoreModeManager();
                }
            }
        }
        return sInstance;
    }


    private void init() {        //search usb file is time-consuming operation
        LogUtils.d(TAG, "init()   StoreModeManager");
        HandlerThread thread = new HandlerThread(TAG);
        if (thread != null) {
            thread.start();
        }
        mTaskHandler = new Handler(thread.getLooper());
        ConstantConfig.initSharedPreference();
        ReceiverManager.getInstance().registerReceiver();
        BackgroundTaskManager.getInstance().startWorker();

    }

    public void start() {
        boolean isFactoryMode = ConstantConfig.isFactoryMode();
        boolean isAgingMode = ConstantConfig.isAgingMode();
        boolean isStoreMode = ConstantConfig.isStoreMode();

        LogUtils.d(TAG, "start()  isFactoryMode():" + isFactoryMode);
        LogUtils.d(TAG, "start()  isAgingMode():" + isAgingMode);
        LogUtils.d(TAG, "start()  isStoreMode():" + isStoreMode);

        if (!isStoreMode || isFactoryMode || isAgingMode) {
            return;
        }

        LogUtils.d(TAG, "storemode manager start()  time:" + System.currentTimeMillis());
        LogUtils.d(TAG, "start()  thread id:" + Thread.currentThread().getId());

        mTaskHandler.post(new Runnable() {
            @Override
            public void run() {
                LogUtils.i(TAG, " mTaskHandler post  thread id:" + Thread.currentThread().getId());

                PlayManager.getInstance().startPlay();

            }
        });
    }


}
