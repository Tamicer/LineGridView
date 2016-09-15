package com.tamic.linegridview;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;


/**
 * 基础 baseGridview
 * 提供事件绑定操作
 * Created by LIUYONGKUI on 2016-03-03.
 */
public abstract class BaseGridView extends PaAbsGridView implements OnClickListener, OnLongClickListener{

    /**
     * @param context  上下文
     * @param adapter 适配器
     */
    public BaseGridView(Context context, PaAbsAdapter adapter) {
       this(context, adapter, -1);
    }

    /**
     * @param context  上下文
     * @param adapter 适配器
     * @param aCount 列数（默认3）
     */
    public BaseGridView(Context context, PaAbsAdapter adapter, int aCount) {

        super(context, adapter, aCount <= 0 ? -1 : aCount);

    }

    @Override
    public void layoutItem(View aChild, int aPos, boolean aIsAnim) {
        super.layoutItem(aChild, aPos, aIsAnim);
        aChild.setOnClickListener(this);
        aChild.setOnLongClickListener(this);
    }


    @Override
    public void onClick(View v) {
        onItemClick(v);
    }

    @Override
    public boolean onLongClick(View v) {
        return onItemLongClick(v);
    }

    /**
     * item 短按事件
     * @param v
     */
    public abstract void onItemClick(View v);

    /**
     * item 长按事件
     * @param v
     */
    public abstract boolean onItemLongClick(View v);


}
