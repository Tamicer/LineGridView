package com.tamic.linegridview;

import android.content.Context;
import android.view.View;

/**
 * Created by liuyongkui on 2016-09-02.
 */
public class PluginGridview2 extends BaseGridView {



    public PluginGridview2(Context context, PaAbsAdapter adapter, int aCount) {
        super(context, adapter, aCount);
    }

    public PluginGridview2(Context context, PaAbsAdapter adapter) {
        super(context, adapter);
    }

    @Override
    public void onItemClick(View v) {

    }

    @Override
    public boolean onItemLongClick(View v) {
        return false;
    }
}
