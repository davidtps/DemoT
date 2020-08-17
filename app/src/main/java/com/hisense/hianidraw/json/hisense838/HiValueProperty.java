package com.hisense.hianidraw.json.hisense838;

import com.hisense.hianidraw.data.TransForm;
import com.hisense.hianidraw.draw.ValueGetterByTime;
import com.hisense.hianidraw.transform.DefaultValueGetter;
import com.hisense.hianidraw.transform.StageGroupValueGetter;
import com.hisense.hianidraw.transform.StageValueGetter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Create by xuchongxiang at 2019-05-07 2:53 PM
 * This is Gson parser bean, do not modify filed name. if modify, will cause error.
 */

public class HiValueProperty implements TransForm<ValueGetterByTime> {

    private String name;
    private String value;
    private ArrayList<HiStageData> stages;

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public final void setStages(ArrayList<HiStageData> stages) {
        this.stages = stages;
    }

    public final String getName() {
        return name;
    }

    public final String getValue() {
        return value;
    }

    public final ArrayList<HiStageData> getStages() {
        return stages;
    }


    public HiValueProperty(String name, String value, ArrayList<HiStageData> stages) {
        super();
        this.name = name;
        this.value = value;
        this.stages = stages;
    }

    @Override
    public ValueGetterByTime transform() {
        List<StageValueGetter> list = new ArrayList<>();
        if (stages != null) {
            for (HiStageData item : stages) {
                item.setName(name);
                StageValueGetter svg = item.transform();
                list.add(svg);
            }
            return new StageGroupValueGetter(name, list);
        }
        if (value != null) {
            return new DefaultValueGetter(name, value);

        }
        throw new IllegalArgumentException("json " + name + " must have someting wrong");
    }
}
