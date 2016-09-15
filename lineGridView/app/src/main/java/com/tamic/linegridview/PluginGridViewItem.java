package com.tamic.linegridview;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


/*
* Class info：GridViewItem
* Created by：liuyongkui.
*/
public class PluginGridViewItem extends RelativeLayout {

	/** 完全不透明度 */
	private static final int FULL_ALPHA = 255;

	/** 半不透明度 */
	private static final int HALF_ALPHA = 128;

	/** icon名称文字的大小，单位dp */
	private static final int SUG_NAME_TEXT_SIZE = 12;

	/** ICON 视图的ID */
	private static final int SUG_ICON_ID = 0x0101;

	/**  数据 */
	private PluginConfigModle mGuideModle;

	/** ICON */
	private ImageView mSugIcon;

	/** 名称 */
	private TextView mSugName;

	/** 上下文 */
	private Context mContext;

	/** 图片加载器 */
	private Picasso mImageLoader;

	/** 该视图宽度 */
	private int mWidth;

	/** 该视图高度 */
	private int mHeight;

	/** ICON 的宽高 */
	private int mIconWidth;

	/** SUG 名称视图的高度 */
	private int mTextHeight;

	/** SUG 名称视图的宽度 */
	private int mTextWidth;

	/** 是否按下 */
	private boolean mIsPressed;

	/** 按下时的背景 */
	private Drawable mCellPressDrawable;

	/** 夜间模式下按下时的背景 */
	private Drawable mNightCellPressDrawable;

	/**
	 * 默认图片，使用static减少对象数
	 */
	private static Bitmap mDefaultIcon;

	/** 点击事件监听器 */
	private OnItemClickListener mItemClickListener;

	private PluginGridViewItem(Context aContext, AttributeSet aAttrs) {
		super(aContext, aAttrs);
	}

	private PluginGridViewItem(Context aContext) {
		this(aContext, null);
	}

	public PluginGridViewItem(Context aContext,
							  PluginConfigModle aGuideModle, Picasso aImageLoader) {
		this(aContext);
		mGuideModle = aGuideModle;
		mContext = aContext;
		mImageLoader = aImageLoader;
		init();
	}

	public PluginConfigModle getPluginModle() {
		return mGuideModle;
	}

	public void setPluginModle(PluginConfigModle mGuideModle) {
		this.mGuideModle = mGuideModle;
	}

	/***
	 * 检查屏幕的方向.
	 *
	 * @return false
	 */
	private boolean isLandscape() {
		if (mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 计算ICON的宽度，以及该视图的宽高
	 */
	private void initCellWidth() {
		int screenHeight = mContext.getResources().getDisplayMetrics().heightPixels;
		int screenWidth = mContext.getResources().getDisplayMetrics().widthPixels;

		if (screenWidth > screenHeight) {
			int temp = screenWidth;
			screenWidth = screenHeight;
			screenHeight = temp;
		}

		if (isLandscape()) {

			int m = PluginGridView.mColCount;
			int n = m - 1;

			int padding = (int) (PluginGridView.PADDING_LANDSCAPE * screenHeight / 1280);
			int spacing = (int) (PluginGridView.ICON_SPACING_LANDSCAPE
					* screenHeight / 1280);
			mIconWidth = (screenHeight - n  * spacing - 2 * padding) / m ;

			spacing = (int) ((PluginGridView.ICON_SPACING_LANDSCAPE - PluginGridView.PADDING_LANDSCAPE)
					* screenHeight / 1280);
			mWidth = (screenHeight - n * spacing - padding) / m;

		} else {

			int m = PluginGridView.mColCount;
			int n = m - 1;
			int padding = (int) (PluginGridView.PADDING_PORTRAIT * screenWidth / 720);
			int spacing = (int) (PluginGridView.ICON_SPACING_PORTRAIT
					* screenWidth / 720);
			mIconWidth = (screenWidth - n * spacing - 2 * padding) / m;

			padding = (int) (PluginGridView.PADDING_PORTRAIT * screenWidth / 720) / 2;
			spacing = 0;
			mWidth = (screenWidth - padding * 2) / m;
		}
	}

	/**
	 * 初始化视图
	 */
	private void init() {

		mCellPressDrawable = getResources().getDrawable(
				R.drawable.home_item_bg);

		mNightCellPressDrawable = getResources().getDrawable(
				R.drawable.home_item_night_bg);

	/*	mDefaultIcon = BitmapFactory.decodeResource(getResources(),
				R.drawable.sug_default_icon);*/

/*		// init the icon view
		initCellWidth();
		/*mSugIcon = new BdImageView(mContext);
		mSugIcon.setImageBitmap(mDefaultIcon);
		//mSugIcon.setId(SUG_ICON_ID);
		mSugIcon.setScaleType(ScaleType.FIT_XY);
		mSugIcon.setRadius(mContext.getResources().getDimensionPixelSize(
				R.dimen.sug_icon_corner_radius));
		mSugIcon.setUrl(mGuideModle.getAppIcon(), true);*/

		mSugIcon = new ImageView(mContext);
		//mSugIcon.setImageBitmap(mDefaultIcon);
		//mSugIcon.setId(SUG_ICON_ID);
		mSugIcon.setScaleType(ImageView.ScaleType.FIT_XY);
		mSugIcon.setMaxHeight(mWidth);
		mSugIcon.setMaxWidth(mWidth);

		mImageLoader.load(mGuideModle.getAppIcon()).into(mSugIcon);

		LayoutParams params = new LayoutParams(
				mWidth, mWidth);
		params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
		params.topMargin = mContext.getResources().getDimensionPixelSize(
				R.dimen.sug_item_icon_top_margin);
		this.addView(mSugIcon, params);

		// init the name view
		mSugName = new TextView(mContext);
		mSugName.setMaxLines(1);
		mSugName.setText(mGuideModle.getPluginName());
		mSugName.setTextSize(TypedValue.COMPLEX_UNIT_SP, SUG_NAME_TEXT_SIZE);
		mSugName.setGravity(Gravity.CENTER);
		mSugName.setTextColor(mContext.getResources().getColor(
				R.color.sug_item_name_color));
		LayoutParams txtParams = new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		txtParams.topMargin = mContext.getResources().getDimensionPixelSize(
				R.dimen.sug_item_text_top_margin);
		txtParams.bottomMargin = mContext.getResources().getDimensionPixelSize(
				R.dimen.sug_item_text_bottom_margin);
		txtParams.addRule(RelativeLayout.BELOW, SUG_ICON_ID);
		txtParams
				.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
		mSugName.setLayoutParams(txtParams);
		this.addView(mSugName);

		mTextHeight = (int) (mSugName.getPaint().getFontMetrics().descent - mSugName
				.getPaint().getFontMetrics().top);
		mTextWidth = (int) mSugName.getPaint().measureText(
				mGuideModle.getPluginName());

		//onThemeChanged(0);
	}

	/**
	 * 设置点击事件监听器
	 *
	 * @param aClickListener
	 *            点击事件监听器
	 */
	public void setOnItemClickListener(OnItemClickListener aClickListener) {
		mItemClickListener = aClickListener;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		initCellWidth();

		mHeight = mIconWidth
				+ mContext.getResources().getDimensionPixelSize(
						R.dimen.sug_item_icon_top_margin)
				+ mContext.getResources().getDimensionPixelSize(
						R.dimen.sug_item_text_top_margin)
				+ mTextHeight
				+ mContext.getResources().getDimensionPixelSize(
						R.dimen.sug_item_text_bottom_margin);

		setMeasuredDimension(mWidth, mHeight);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		if (mSugIcon != null) {
			mSugIcon.layout(
					(mWidth - mIconWidth) / 2,
					mContext.getResources().getDimensionPixelSize(
							R.dimen.sug_item_icon_top_margin),
					(mWidth + mIconWidth) / 2,
					mContext.getResources().getDimensionPixelSize(
							R.dimen.sug_item_icon_top_margin)
							+ mIconWidth);
		}

		if (mSugName != null) {
			int top = mContext.getResources().getDimensionPixelSize(
					R.dimen.sug_item_icon_top_margin)
					+ mIconWidth
					+ mContext.getResources().getDimensionPixelSize(
							R.dimen.sug_item_text_top_margin);
			int bottom = mHeight
					- mContext.getResources().getDimensionPixelSize(
							R.dimen.sug_item_text_bottom_margin);

			if (mTextWidth > mWidth) {
				mSugName.layout(0, top, mWidth, bottom);
			} else {
				mSugName.layout((mWidth - mTextWidth) / 2, top,
						(mWidth + mTextWidth) / 2, bottom);
			}

		}
	}

	/**
	 * 处理手势.
	 *
	 * @see android.view.View#onTouchEvent(MotionEvent)
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:

			mIsPressed = true;
			if (mItemClickListener != null) {
				mItemClickListener.onPressDown(PluginGridViewItem.this);
			}
			break;

		case MotionEvent.ACTION_UP:
			mIsPressed = false;
			if (mItemClickListener != null) {
				mItemClickListener.onClick(PluginGridViewItem.this,
						mGuideModle);
			}
			break;

		case MotionEvent.ACTION_CANCEL:
			mIsPressed = false;
			if (mItemClickListener != null) {
				mItemClickListener.onPressUp(PluginGridViewItem.this);
			}
			break;

		default:
			break;
		}

		return super.onTouchEvent(event);
	}

	/**
	 * Item点击监听.
	 */
	public interface OnItemClickListener {

		/**
		 * 点击.
		 */
		void onClick(PluginGridViewItem v, PluginConfigModle model);


		/**
		 * 按下.
		 */
		void onPressDown(PluginGridViewItem v);

		/**
		 * 弹起.
		 */
		void onPressUp(PluginGridViewItem v);
	}


	public void setBackground() {
		if (mIsPressed) {
			setBackgroundDrawable(mCellPressDrawable);
		} else {
			setBackgroundDrawable(null);
		}
	}
}
