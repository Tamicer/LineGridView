package com.tamic.linegridview;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * BaseAdapter上封装了一层, 只需实现getView(int position, View convertView, ViewGroup parent)方法
 * 供Listview,gridView使用
 * @author LIUYONGKUI726
 * 
 * @param <T> 泛型
 */
public abstract class PaAbsAdapter<T> extends BaseAdapter {
    public ArrayList<T> mList = new ArrayList<T>();
    public Context mContext;

    public PaAbsAdapter(Context context, List<T> list) {
        mContext = context;
        if (list != null) {
            mList.addAll(list);
        }
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList == null ? null : mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public abstract View getView(int position, View convertView, ViewGroup parent);

    /**
     * 更新ListView
     * 
     * @param list
     */
    public void notifyDataSetChanged(ArrayList<T> list) {
        if (mList != list) {
            mList.clear();
            if (list != null) {
                mList.addAll(list);
            }
        }
        if (mContext != null) {
            ((Activity)mContext).runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    notifyDataSetChanged();
                }
            });
        }
    }
}
