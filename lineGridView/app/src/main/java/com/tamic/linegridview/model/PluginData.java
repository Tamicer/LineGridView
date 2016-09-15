package com.tamic.linegridview.model;

import com.tamic.linegridview.PluginConfigModle;

import java.io.Serializable;
import java.util.List;

/**
 * Created by LIUYONGKUI726 on 2016-02-02.
 */
public class PluginData implements Serializable {

    public int count;

    public List<PluginConfigModle> list;

    public List<PluginConfigModle> getList() {
        return list;
    }

    public void setList(List<PluginConfigModle> list) {
        this.list = list;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
