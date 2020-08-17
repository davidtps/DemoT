package com.hisense.storemode.manager.play;

import com.hisense.hianidraw.cache.SeriesUtils;
import com.hisense.storemode.bean.Player;
import com.hisense.storemode.manager.play.player.IPlayer;
import com.hisense.storemode.manager.play.player.PicturePlayer;
import com.hisense.storemode.manager.play.player.SignalPlayer;
import com.hisense.storemode.manager.play.player.videoplayer.VideoPlayer;
import com.hisense.storemode.manager.play.strategy.IPlayStrategy;
import com.hisense.storemode.manager.play.strategy.PicStrategy;
import com.hisense.storemode.manager.play.strategy.SignalStrategy;
import com.hisense.storemode.manager.play.strategy.StrategyContext;
import com.hisense.storemode.manager.play.strategy.VideoStrategy;
import com.hisense.storemode.utils.ConstantConfig;
import com.hisense.storemode.utils.FileUtil;
import com.hisense.storemode.utils.LogUtils;
import com.hisense.storemode.utils.PreferenceUtils;
import com.hisense.storemode.utils.UsbUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tianpengsheng on 2019年04月22日 19时21分.
 * <p>
 * play strategy  execute instance
 * require play  mode ; paly group polling ;
 */
public class PlayPolicy {
    private static final String TAG = "PlayPolicy";

    private List<Player> mPlayerList = new ArrayList<>();
    private IPlayStrategy mCurrentPlayStrategy;
    private IPlayer mCurrentPlayer;
    private Player mCurrentPlayerBean;
    private boolean mFlag = true;
    private int mCurrentIndex = -1;

    public Player getCurrentPlayerBean() {
        return mCurrentPlayerBean;
    }

    public synchronized void playNext() {
        if (mFlag) {
            mFlag = false;
            mPlayerList = getPlayerList();
        } else {
            boolean isResetPlayPolicy = (Boolean) PreferenceUtils.getInstance().get(ConstantConfig.RESET_PALY_POLICY, false);
            LogUtils.d(TAG, "playNext()  is need reset getPlayerList():" + isResetPlayPolicy);
            if (isResetPlayPolicy) {
                mPlayerList = getPlayerList();
            }
        }

        mCurrentIndex++;
        LogUtils.i(TAG, "mCurrentIndex:" + mCurrentIndex + "  mPlayerList.size():" + mPlayerList.size());
        if (mCurrentIndex >= mPlayerList.size()) {
            mCurrentIndex = 0;
        }

        mCurrentPlayerBean = mPlayerList.get(mCurrentIndex);
        int type = mCurrentPlayerBean.mType;

        LogUtils.i(TAG, "playNext  switch:" + type
                + "  mCurrentIndex:" + mCurrentIndex +
                "\n     * // 1:usb video\n" +
                "     * // 2:build-in video\n" +
                "     * // 3:usb pic\n" +
                "     * // 4:build-in pic\n" +
                "     * // 5:signal\n" +
                "     * // 6:apk drawable pic---if no build-in pic,pic default show\n" +
                "     * // 7:all pic res");

        if (type == ConstantConfig.TYPE_VALUE_USB_VIDEO) {
            ConstantConfig.isUsbResourcePlaying = true;
            ConstantConfig.currentPlayPath = mCurrentPlayerBean.mPath;
        } else if (type == ConstantConfig.TYPE_VALUE_USB_PIC) {
            ConstantConfig.isUsbResourcePlaying = true;
        } else {
            ConstantConfig.isUsbResourcePlaying = false;
            ConstantConfig.currentPlayPath = null;
        }
        LogUtils.d(TAG, "isUsbResourcePlaying = " + ConstantConfig.isUsbResourcePlaying);


        //start------ before change policy,stop  player  release resource
        if (mCurrentPlayer != null) {
            LogUtils.d(TAG, "playNext() mCurrentPlayer:" + mCurrentPlayer);
            mCurrentPlayer.stop();
        }
        //end------ before change policy,stop  player  release resource

        switch (type) {
//            case ConstantConfig.TYPE_VALUE_BUILD_IN_PIC:
//            case ConstantConfig.TYPE_VALUE_USB_PIC:
            case ConstantConfig.TYPE_VALUE_ALL_PIC_RES: {
                LogUtils.d(TAG, "playNext    TYPE_VALUE_ALL_PIC_RES");
                if (!(mCurrentPlayer != null && mCurrentPlayer instanceof PicturePlayer)) {
                    mCurrentPlayer = new PicturePlayer(ConstantConfig.PLANTFORM_NORMAL_PICTURE, mCurrentPlayerBean.mAllPicResPath);
                    PlayManager.getInstance().setCurrentPlayer(mCurrentPlayer);
                } else {
                    ((PicturePlayer) mCurrentPlayer).setPath(mCurrentPlayerBean.mAllPicResPath);
                }
                mCurrentPlayStrategy = new PicStrategy(mCurrentPlayerBean.mPath);
                break;
            }

            case ConstantConfig.TYPE_VALUE_BUILD_IN_VIDEO:
                LogUtils.d(TAG, "playNext    TYPE_VALUE_BUILD_IN_VIDEO");

            case ConstantConfig.TYPE_VALUE_USB_VIDEO: {
                LogUtils.d(TAG, "playNext    TYPE_VALUE_USB_VIDEO");
                LogUtils.i(TAG, "playNext() !(mCurrentPlayer != null && mCurrentPlayer instanceof VideoPlayer): " + (!(mCurrentPlayer != null && mCurrentPlayer instanceof VideoPlayer)));
                if (!(mCurrentPlayer != null && mCurrentPlayer instanceof VideoPlayer)) {
                    mCurrentPlayer = new VideoPlayer(ConstantConfig.getCurrentPlatform(), mCurrentPlayerBean.mPath);
                    PlayManager.getInstance().setCurrentPlayer(mCurrentPlayer);
                }
                mCurrentPlayStrategy = new VideoStrategy(mCurrentPlayerBean.mPath);

                break;
            }

            case ConstantConfig.TYPE_VALUE_SIGNAL: {
                LogUtils.d(TAG, "playNext    TYPE_VALUE_SIGNAL  mCurrentPlayer:" + mCurrentPlayer);
                if (!(mCurrentPlayer != null && mCurrentPlayer instanceof SignalPlayer)) {
                    mCurrentPlayer = new SignalPlayer();
                    PlayManager.getInstance().setCurrentPlayer(mCurrentPlayer);
                }
                LogUtils.d(TAG, "playNext()  signalPlayer TYPE_VALUE_SIGNAL" + mCurrentPlayer);
                mCurrentPlayStrategy = new SignalStrategy();

                break;
            }

//            default: {//TYPE_VALUE_APK_DRAWABLE_PIC
//                if (!(mCurrentPlayer != null && mCurrentPlayer instanceof PicturePlayer)) {
//                    mCurrentPlayer = new PicturePlayer(ConstantConfig.PLANTFORM_NORMAL_PICTURE, mCurrentPlayerBean.mResourceId);
//                }
//                mCurrentPlayStrategy = new PicStrategy(mCurrentPlayerBean.mResourceId);
//                break;
//            }
        }


        StrategyContext strategyContext = new StrategyContext(mCurrentPlayStrategy);
        strategyContext.contextMethod();


        // return mCurrentPlayer;
    }


    //get play mType and mPath
    private synchronized List<Player> getPlayerList() {
        mCurrentIndex = -1; //reset index;
        List<UsbUtil.UsbPathAndLabel> mUsbFilePaths = UsbUtil.getUsbPath();

        LogUtils.d(TAG, "getPlayerList() usb storage parentPath size:" + mUsbFilePaths.size() + "  thread id:" + Thread.currentThread().getId());
        for (int i = 0; i < mUsbFilePaths.size(); i++) {
            LogUtils.d(TAG, "getPlayerList() usb storage parentPath label:" + mUsbFilePaths.get(i).mLabel + "       mPath:" + mUsbFilePaths.get(i).mPath);
        }
        //reset flag  set false
        PreferenceUtils.getInstance().save(ConstantConfig.RESET_PALY_POLICY, false);

        List<Player> playerList = new ArrayList<>();
        List<Object> allPicResource = new ArrayList<>();


        if (ConstantConfig.is4KMode()) {
            requireBuildInVideoPath(playerList);
            if (playerList.size() == 0) {
                initBuildInApkResDrawable(allPicResource);
                playerList.add(new Player(allPicResource, ConstantConfig.TYPE_VALUE_ALL_PIC_RES));
            }
            return playerList;
        }


        boolean sginalCheck = (Boolean) PreferenceUtils.getInstance().get(ConstantConfig.SHAREPREF_SIGNAL_CHECK, false);
        if (sginalCheck) {
            playerList.add(new Player("", ConstantConfig.TYPE_VALUE_SIGNAL));
        }

        boolean usbVideoCheck = (Boolean) PreferenceUtils.getInstance().get(ConstantConfig.SHAREPREF_USB_VIDEO_CHECK, false);
        boolean usbResPriorityCheck = (Boolean) PreferenceUtils.getInstance().get(ConstantConfig.SHAREPREF_USB_RES_PRIORITY_CHECK, false);
        ConstantConfig.IS_HAVE_USB_VIDEO = false;
        if (usbVideoCheck) {
            // usb storage resource list  (mp4 or pic)
            for (UsbUtil.UsbPathAndLabel usbFile : mUsbFilePaths) {
                // FileUtil.getFilesPathByTypeAndName  mType:pic and video  /  file name:**
                List<String> paths = FileUtil.getFilesPathByTypeAndName(usbFile.mPath, ConstantConfig.VIDEO_TYPE, "");
                if (paths.size() != 0) {
                    for (String filePath : paths) {
//                        if (!FileTypeUtil.isVideoTypeFile(new File(filePath))) {
//                            LogUtils.d(TAG, "getPlayerList()  usbVideoPath  usb video file not mp4 or mkv" + filePath);
//                            continue;
//                        }
                        if (FileUtil.isEmptyFile(filePath)) {
                            LogUtils.d(TAG, "getPlayerList()  usbVideoPath  usb video file is empty 0kb" + filePath);
                            continue;
                        }
                        ConstantConfig.IS_HAVE_USB_VIDEO = true;
                        if (usbResPriorityCheck) {//
                            playerList.add(0, new Player(filePath, ConstantConfig.TYPE_VALUE_USB_VIDEO));
                        } else {
                            playerList.add(new Player(filePath, ConstantConfig.TYPE_VALUE_USB_VIDEO));
                        }
                        LogUtils.d(TAG, "getPlayerList()  usbVideoPath find usb video file :" + filePath);
                    }
                } else {
                    LogUtils.d(TAG, "getPlayerList()  no find usb video file  ");
                }
            }

        }

        boolean buildInVideoCheck = (Boolean) PreferenceUtils.getInstance().get(ConstantConfig.SHAREPREF_BUILD_IN_VIDEO_CHECK, false);
        if (buildInVideoCheck) {
            requireBuildInVideoPath(playerList);
        }


        boolean usbPicCheck = (Boolean) PreferenceUtils.getInstance().get(ConstantConfig.SHAREPREF_USB_PIC_CHECK, false);
        ConstantConfig.IS_HAVE_USB_PIC = false;
        if (usbPicCheck) {
            // usb storage resource list  (mp4 or pic)
            for (UsbUtil.UsbPathAndLabel usbFile : mUsbFilePaths) {
                // FileUtil.getFilesPathByTypeAndName  mType:pic and video  /  file name:**
                List<String> paths = FileUtil.getFilesPathByTypeAndName(usbFile.mPath,
                        ConstantConfig.PIC_TYPE, "");

                if (paths.size() != 0) {//find success usb picture resource
                    ConstantConfig.IS_HAVE_USB_PIC = true;
                    for (String filePath : paths) {
                        if (FileUtil.isEmptyFile(filePath)) {
                            LogUtils.d(TAG, "getPlayerList()  usbPicPath usb pic file is empty 0kb" + filePath);
                            continue;
                        }
//                        playerList.add(new Player(filePath, ConstantConfig.TYPE_VALUE_USB_PIC));
                        allPicResource.add(filePath);
                        LogUtils.d(TAG, "usbPicPath find usb picture file :" + filePath);
                    }
                } else {
                    LogUtils.d(TAG, "no find usb picture file  ");
                }
            }

        }

        boolean buildInPicCheck = (Boolean) PreferenceUtils.getInstance().get(ConstantConfig.SHAREPREF_BUILD_IN_PIC_CHECK, false);

        if (buildInPicCheck) {

            // FileUtil.getFilesPathByTypeAndName  mType:pic and video  /  file name:**
            List<String> paths = FileUtil.getFilesPathByTypeAndName(ConstantConfig.PICTURE_BUILD_IN_PATH,
                    ConstantConfig.PIC_TYPE, ConstantConfig.BACKGROUND_PIC_NAME_PREFIX);

            if (paths.size() != 0) {
                for (String filePath : paths) {
//                    playerList.add(new Player(filePath, ConstantConfig.TYPE_VALUE_BUILD_IN_PIC));
                    allPicResource.add(filePath);
                    LogUtils.d(TAG, "build-in PicPath find file :" + filePath);
                }
            } else {
                LogUtils.d(TAG, "no find build-in Pic file  ");
                initBuildInApkResDrawable(allPicResource);
            }

        }

        if (!buildInPicCheck && !(ConstantConfig.IS_HAVE_USB_PIC || ConstantConfig.IS_HAVE_USB_VIDEO)) {
            if (!buildInVideoCheck) {
                if (SeriesUtils.DEFAULT_PLAY_SOURECE_BUILT_IN_VIDEO) {
                    requireBuildInVideoPath(playerList);
                } else {
                    initBuildInApkResDrawable(allPicResource);
                }
            } else {//video check select but no built-in video file;
                if (playerList.size() == 0) {
                    initBuildInApkResDrawable(allPicResource);
                } else if (playerList.size() == 1 && playerList.get(0).mType == ConstantConfig.TYPE_VALUE_SIGNAL) {
                    initBuildInApkResDrawable(allPicResource);
                }
            }
        }
        playerList.add(new Player(allPicResource, ConstantConfig.TYPE_VALUE_ALL_PIC_RES));


        if (ConstantConfig.LOG_SWITCH) {
            StringBuffer logBuffer = new StringBuffer();
            logBuffer.append("\n buildInPicCheck: " + buildInPicCheck);
            logBuffer.append("\n buildInVideoCheck: " + buildInVideoCheck);
            logBuffer.append("\n sginalCheck: " + sginalCheck);
            logBuffer.append("\n usbPicCheck: " + usbPicCheck);
            logBuffer.append("\n usbVideoCheck: " + usbVideoCheck);
            LogUtils.i(TAG, "getPlayerList  play switch list: " + logBuffer.toString());


            LogUtils.e(TAG, "getPlayerList   start afresh  get play mType and mPath");
            for (Player player : playerList) {
                LogUtils.d(TAG, "player Path:" + player.mPath + "   Type:" + player.mType);
            }
            LogUtils.e(TAG, "getPlayerList   end afresh  get play mType and mPath");

            LogUtils.e(TAG, "getPlayerList   start afresh allPicResource");
            for (Object object : allPicResource) {
                if (object instanceof String) {
                    LogUtils.d(TAG, "allPicResource string Path:" + object);
                } else {
                    LogUtils.d(TAG, "allPicResource int Path:" + object);
                }
            }
            LogUtils.e(TAG, "getPlayerList   end afresh  allPicResource");
        }

        return playerList;
    }

    private void requireBuildInVideoPath(List<Player> playerList) {
        // FileUtil.getFilesPathByTypeAndName  mType:pic and video  /  file name:**
//        if (FileUtil.isFileExist(ConstantConfig.VIDEO_BUILD_IN_UPDATE_FILE_PATH)) {
//            playerList.add(new Player(ConstantConfig.VIDEO_BUILD_IN_UPDATE_FILE_PATH, ConstantConfig.TYPE_VALUE_BUILD_IN_VIDEO));
//            LogUtils.d(TAG, "build-in video app path find file :" + ConstantConfig.VIDEO_BUILD_IN_UPDATE_FILE_PATH);
//        } else
        if (!FileUtil.isEmptyFile(ConstantConfig.getCurrentVideoBuildInPath())) {
            playerList.add(new Player(ConstantConfig.getCurrentVideoBuildInPath(), ConstantConfig.TYPE_VALUE_BUILD_IN_VIDEO));
            LogUtils.d(TAG, "build-in video vdow path find file :" + ConstantConfig.getCurrentVideoBuildInPath());
        } else {
            LogUtils.d(TAG, "no find build-in video file  ");
        }
    }

    //add apk drawable resource in list
    private void initBuildInApkResDrawable(List<Object> allPicResource) {
        LogUtils.d(TAG, "no find build-in Pic file initBuildInApkResDrawable ");
        for (int resourceId : SeriesUtils.LOCAL_DRAWABLE_RESOURCE) {
//            mPlayerList.add(new Player(resourceId, ConstantConfig.TYPE_VALUE_APK_DRAWABLE_PIC));
            allPicResource.add(resourceId);
        }
    }
}
