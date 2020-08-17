package com.hisense.hianidraw.json.hisense838;

import com.hisense.hianidraw.data.DrawItem;
import com.hisense.hianidraw.data.TransForm;
import com.hisense.hianidraw.draw.DrawProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Create by xuchongxiang at 2019-05-07 2:45 PM
 * This is Gson parser bean, do not modify filed name. if modify, will cause error.
 */

public class HiElement implements TransForm<DrawItem> {

    private String name;
    private ArrayList<HiValueProperty> propertys;

    public void setName(String name) {
        this.name = name;
    }

    public void setPropertys(ArrayList<HiValueProperty> propertys) {
        this.propertys = propertys;
    }

    public final String getName() {
        return name;
    }

    public final ArrayList<HiValueProperty> getPropertys() {
        return propertys;
    }

    public HiElement(String name, ArrayList<HiValueProperty> propertys) {
        super();
        this.name = name;
        this.propertys = propertys;
    }

    @Override
    public DrawItem transform() {
        Map<String, DrawProperty> proMap = new HashMap<>();
        for (HiValueProperty item : propertys) {
            proMap.put(item.getName(), new DrawProperty(item.getName(), item.transform()));
        }
        return new DrawItem(name, proMap);
    }
}
