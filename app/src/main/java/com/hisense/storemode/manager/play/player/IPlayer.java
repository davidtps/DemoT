package com.hisense.storemode.manager.play.player;

/**
 * Created by cuihuihui on 2019年04月18日 16时45分.
 */
public interface IPlayer {

    void start(Object o);

    void stop();

    // For input parameter: "newFilePath"
    // a. string for video and picture
    // b. long just for picture in res
    void playNext(Object newFilePath);


}

