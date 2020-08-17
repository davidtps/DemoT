package com.hisense.storemode.ui.dialog.malllogo;

/**
 * Create by xuchongxiang at 2019-06-27 3:03 PM
 */

public class MallLogoBean {

    public static final int PIC_FROM_USB = 0;
    public static final int PIC_FROM_BUILT_IN = 1;
    public static final int PIC_GOTO_MEDIARCENTER = 2;

    public int type;//0 from usb 1 neizhi 2 last
    public boolean isSelect;
    public String logoLocation;

    public MallLogoBean() {
    }

    public MallLogoBean(boolean isSelect, String logoLocation) {
        this.isSelect = isSelect;
        this.logoLocation = logoLocation;
    }


    public MallLogoBean(int type, boolean isSelect, String logoLocation) {
        this.type = type;
        this.isSelect = isSelect;
        this.logoLocation = logoLocation;
    }

    @Override
    public String toString() {
        return "MallLogoBean{" +
                "type=" + type +
                ", isSelect=" + isSelect +
                ", logoLocation='" + logoLocation + '\'' +
                '}';
    }
}
