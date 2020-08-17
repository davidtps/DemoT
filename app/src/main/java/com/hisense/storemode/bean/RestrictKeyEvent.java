package com.hisense.storemode.bean;

/**
 * Created by tianpengsheng on 2020年08月09日 18时01分.
 */
public class RestrictKeyEvent {
    public int restrict_flag = 0;

    //0 don't restrict any key; 1 restrict some key
    public RestrictKeyEvent(int restrict_flag) {
        this.restrict_flag = restrict_flag;
    }
}
