package com.hisense.hianidraw.json.hisense838;

import com.hisense.hianidraw.data.Getter;
import com.hisense.hianidraw.draw.DrawElement;
import com.hisense.hianidraw.draw.DrawElementFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by xyjk at 2019-05-07 3:55 PM
 * This is Gson parser bean, do not modify filed name. if modify, will cause error.
 */

public class Root implements Getter<DrawElement> {

    private ArrayList<HiElement> elements;
    private long duration;

    public final void setElements(ArrayList<HiElement> elements) {
        this.elements = elements;
    }

    public final void setDuration(long duration) {
        this.duration = duration;
    }

    public final ArrayList<HiElement> getElements() {
        return elements;
    }

    public final long getDuration() {
        return duration;
    }

    @Override
    public List<DrawElement> getContent() {

        List<HiElement> tmp = new ArrayList<>();
        if (elements != null) {
            tmp.addAll(elements);
        }
        List<DrawElement> list = new ArrayList<>();
        for (HiElement item : tmp) {
            list.add(DrawElementFactory.getDrawElement(item.transform()));
        }
        return list;
    }
}
