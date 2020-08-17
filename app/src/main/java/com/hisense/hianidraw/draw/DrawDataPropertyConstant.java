package com.hisense.hianidraw.draw;

/**
 * Create by xuchongxiang at 2019-05-07 9:23 AM
 */

public final class DrawDataPropertyConstant {

    public static final String X = "x";
    public static final String Y = "y";
    public static final String W = "w";
    public static final String H = "h";
    public static final String ALPHA = "alpha";
    public static final String SCALE = "scale";
    public static final String ROTATION = "rotation";
    public static final String CONTENT = "content";
    public static final String COLOR = "color";
    public static final String SHADER_STRING = "shader";
    public static final String CLIP = "clip";
    public static final String LAYOUTORDER = "layoutorder";
    public static final String NEWPAINT = "newpaint";

    public static final String TYPE = "type";
    public static final String PIC = "pic";
    public static final String RECT_STRING = "rect";

    public static final class SHAPE {
        public static final String SHAPE_RECT = "rect";
        public static final String SHAPE_CIRCLE = "circle";
    }

    public static final class SHADER {
        public static final String SHADER_LINEAR_GRADIENT = "LinearGradient";
    }


    public enum STATE {
        DISAPPER,
        IN,
        KEEP,
        OUT,
        IN_OUT,
        APPER
    }
}
