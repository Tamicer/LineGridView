package com.tamic.linegridview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * PluginAdapter baseAdapter
 * Liuyongkui
 * */
public class PluginAdapter extends PaAbsAdapter<PluginConfigModle> {
	/** mContext. */
	private Context mContext;
	/** PluginConfigModle datas. */
	private List<PluginConfigModle> mModles = new ArrayList<PluginConfigModle>();
	/** Loaders. */
	private Picasso mImageLoader;


	/**
	 * @param mContext
	 * @param  mModles
	 */
	public PluginAdapter(Context mContext, List<PluginConfigModle> mModles) {
		super(mContext, mModles);
		this.mContext = mContext;

	}


	/**
	 * @param mContext
	 * @param mImageLoader
	 * @param mModles
	 */
	public PluginAdapter(Context mContext, Picasso mImageLoader,
						 List<PluginConfigModle> mModles) {
		super(mContext, mModles);
		this.mContext = mContext;
		this.mModles = mModles;
		this.mImageLoader = mImageLoader;
	}

	public void setData(List<PluginConfigModle> aDatas) {
		mModles = aDatas;
	}

	@Override
	public int getCount() {
				
		return mModles == null ? 0 : mModles.size();
	}

	@Override
	public PluginConfigModle getItem(int position) {
		
		return mModles == null ? null : mModles.get(position);
	}

	@Override
	public long getItemId(int position) {

		return mModles == null ? null : Long.parseLong(mModles.get(position).getID());
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (mModles != null && mModles.size() != 0) {
			PluginGridViewItem itemView = new PluginGridViewItem(
					mContext, mModles.get(position), mImageLoader);
			convertView = itemView;
		}
		return convertView;
	}

}
