package com.hisense.hianidraw.json.hisense838;

import com.hisense.hianidraw.data.TransForm;
import com.hisense.hianidraw.draw.impl.DrawStageImpl;
import com.hisense.hianidraw.transform.FloatStageValueGetter;
import com.hisense.hianidraw.transform.StageValueGetter;
import com.hisense.hianidraw.transform.StateValueGetter;

/**
 * Create by xuchongxiang at 2019-05-07 2:56 PM
 * This is Gson parser bean, do not modify filed name. if modify, will cause error.
 */

public class HiStageData implements TransForm<StageValueGetter> {
    private String name;
    private String value;
    private long start;
    private long end;

    public final void setName(String name) {
        this.name = name;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public final String getName() {
        return name;
    }

    public final String getValue() {
        return value;
    }

    public final long getStart() {
        return start;
    }

    public final long getEnd() {
        return end;
    }

    public HiStageData(String value, long start, long end) {
        super();
        this.value = value;
        this.start = start;
        this.end = end;
        this.name = "to be modify";
    }

    @Override
    public StageValueGetter transform() {
        if (value.startsWith("(") && value.endsWith(")")) {
            return new FloatStageValueGetter(name, value, new DrawStageImpl(name, start, end), null);
        } else if (name.equals("state")) {
            return new StateValueGetter(name, value, new DrawStageImpl(name, start, end));
        } else {
            return new StageValueGetter(name, value, new DrawStageImpl(name, start, end));
        }
    }
}
