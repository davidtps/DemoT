package com.hisense.hianidraw.draw;

import com.hisense.hianidraw.draw.impl.CircleDrawElementImpl;
import com.hisense.hianidraw.draw.impl.DrawElementImpl;
import com.hisense.hianidraw.draw.impl.DrawableDrawElementImpl;
import com.hisense.hianidraw.draw.impl.RectDrawElementImpl;

/**
 * Create by xuchongxiang at 2019-05-06 8:17 PM
 */

public class DrawElementFactory {
    public enum Shape {
        PATH, PATH_SHAPE, RECT, OVAL, CIRCLE, ROUND_RECT, ARC, PIC
    }

    public static DrawElementImpl getDrawElement(DrawData drawData) {

        DrawElementImpl drawElement;
        switch (drawData.getShape()) {
            case RECT:
                drawElement = new RectDrawElementImpl(drawData);
                break;
            case PIC:
                drawElement = new DrawableDrawElementImpl(drawData);
                break;
            case CIRCLE:
                drawElement = new CircleDrawElementImpl(drawData);
                break;
            default:
                throw new IllegalArgumentException("not implemented");
        }
        return drawElement;
    }

}
