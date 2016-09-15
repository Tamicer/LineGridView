package com.tamic.linegridview;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * pligin icon GridView.
 *
 * @author liuyongkui
 *
 */
public class PluginGridView extends BaseGridView implements PluginGridViewItem.OnItemClickListener{

	/** 默认列数. */
	public static final int DEFAULT_COLUMN = 3;

	/**
	 * 行数
	 */
	public int mRowCount;
	/** 列数. */
	public static int mColCount = DEFAULT_COLUMN;
	/**
	 * 列数
	 *//*
	private int mColCount = DEFAULT_COLUMN;*/

	/** 横屏默认列数. */
	public static final int HSPAND_DEFAULT_COLUMN = 4;

	/** 分割线颜色. */
	public static final int UI_CROSSLINE_COLOR = 0x1a000000;

	/** 分割线夜间颜色. */
	public static final int UI_CROSSLINE_COLOR_NIGHT = 0x19000000;

	/** 竖屏时左右 Padding间距 */
	public static final float PADDING_PORTRAIT = 20f;

	/** 竖屏时ICON间距 */
	public static final float ICON_SPACING_PORTRAIT = 80f;

	/** 横屏时左右padding */
	public static float PADDING_LANDSCAPE = 200f;

	/** 横屏时ICON间距 */
	public static float ICON_SPACING_LANDSCAPE = 120f;

	/** TOP. */
	private static int PENDING_TOP = 0;

	/** BUTTOM. */
	private static int PENDING_BUTTOM = 0;

	/** 透明度. */
	public static final int ALPHA_HALF = 128;

	/** 透明度（无）. */
	public static final int ALPHA_NO = 255;
	/** 竖分割线. */
	private static Bitmap mDivider;

	/** 竖分割线夜间. */
	private static Bitmap mDividerNight;

	/** 横分割线画笔. */
	private Paint mPaint;

	/** 竖线画笔. */
	private Paint mPaint1;

	/** 行和行之间的间隔，列和列之间的间隔. */
	private int mSpacing;
	/** 是否需要分割线. */
	private boolean mIsNeedDivider;

	/** 屏幕密度. */
	private float mDensity;

	/** child view height. */
	private int mChildHeight;

	/** child view widht. */
	private int mChildWidth;

	/** data source. */
	private PluginAdapter mAdapter;

	/** child views. */
	private List<PluginGridViewItem> mChildViews;

	/** 字条目. */	private int childCount;

	/** 弹出安装对话框. *//*
	private BdPopupDialog mInstallDialog;*/

	/** Tag. */

	private String TAG = "PluginGridView";

	/** divider line color */
	public static final int UI_DIVIDER_LINE_COLOR = 0x14000000;

	/** divider line color night */
	public static final int UI_DIVIDER_LINE_COLOR_NIGHT = 0x0affffff;

	/** 视图左右Padding */
	private int mPadding;

	private String mkey;


	/**
	 * constructor.
	 *
	 * @param context
	 *            context
	 * @param aAdapter
	 *            data source
	 * */
	public PluginGridView(Context context, PluginAdapter aAdapter) {
		super(context, aAdapter);
		mAdapter = aAdapter;
		init();
	}

    /**
	 * constructor.
	 *
	 * @param context
	 *            context
	 * @param attrs
	 *            AttributeSet
	 * *//*
	public PluginGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	*//**
	 * constructor.
	 *
	 * @param context
	 *            context
	 * @param attrs
	 *            AttributeSet
	 * @param defStyle
	 *            int
	 * */
	/*public PluginGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}*/

	/**
	 * init.
	 * */
	private void init() {
		mDensity = getResources().getDisplayMetrics().density;
		mPaint = new Paint();
		mPaint.setStyle(Style.STROKE);
		mPaint.setStrokeWidth(1);
		mPaint.setColor(UI_CROSSLINE_COLOR);
		mPaint1 = new Paint();

		setWillNotDraw(false);

		mChildViews = new ArrayList<PluginGridViewItem>();
		for (int i = 0; i < mAdapter.getCount(); i++) {
			PluginGridViewItem childView = (PluginGridViewItem) mAdapter
					.getView(i, null, null);
			childView.setId(i);
			childView.setOnItemClickListener(PluginGridView.this);
			childView.setOnClickListener(PluginGridView.this);
			childView.setOnLongClickListener(PluginGridView.this);
			mChildViews.add(childView);
			addView(childView);
		}
	}

	/**
	 * refresh grid view.
	 * */
	public void refresh() {
		removeAllViews();
		init();
		this.requestLayout();
		this.postInvalidate();
	}

	/**
	 * 设置列数，默认为5.
	 *
	 * @param aCol
	 *            col to be set
	 * */
	public void setColumn(int aCol) {
		mColCount = aCol;
	}

	/**
	 * 设置行高.
	 *
	 * @param aItemHeight
	 *            item height to be set
	 * */
	public void setItemHeight(int aItemHeight) {
		mChildHeight = aItemHeight;
	}

	/**
	 * set is need to draw divider.
	 *
	 * @param aIsNeed
	 *            value to be set
	 * */
	public void setIsNeedDivider(boolean aIsNeed) {
		mIsNeedDivider = aIsNeed;
	}

	/**
	 * get divider bitmap.
	 *
	 * @return bitmap
	 * */
	private Bitmap getDividerBitmap() {
		if (mDivider == null) {


			mDivider = BitmapFactory.decodeResource(getResources(), R.drawable.home_navi_divider);

		/*	mDivider = PaResource.getImage(getContext(),
					R.drawable.home_navi_divider);*/

		}
		return mDivider;
	}

	/**
	 * get divider bitmap night.
	 *
	 * @return bitmap
	 * */
	private Bitmap getDividerBitmapNight() {
		if (mDividerNight == null) {
			mDividerNight =BitmapFactory.decodeResource(getResources(), R.drawable.home_navi_divider_night);
		/*	PaResource.getImage(getContext(),
					R.drawable.home_navi_divider_night);*/
		}
		return mDividerNight;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		int width = MeasureSpec.getSize(widthMeasureSpec);
		int height = 0;

		childCount = mChildViews.size();

		// 计算总行数和总列数
		int row = childCount / mColCount;
		int col = childCount % mColCount;
		mRowCount = ((col == 0) ? row : row + 1);

		int screenWidth = getResources().getDisplayMetrics().widthPixels;
		int screenHeight = getResources().getDisplayMetrics().heightPixels;
		if (screenWidth > screenHeight) {
			int temp = screenWidth;
			screenWidth = screenHeight;
			screenHeight = temp;
		}

		if (ViewUtils.isLandscape(getContext())) {
			mColCount = HSPAND_DEFAULT_COLUMN;
			mPadding = (int) (PADDING_LANDSCAPE * screenHeight / 1280)/2;
			mSpacing = (int) ((ICON_SPACING_LANDSCAPE - PADDING_LANDSCAPE)
					* screenHeight / 1280);
			mChildWidth = (width - (mColCount - 1) * mSpacing - 2 * mPadding)
					/ mColCount;
		} else {
			mColCount = DEFAULT_COLUMN;
			mPadding = (int) (PADDING_PORTRAIT * screenWidth / 720) / 2;
			mSpacing = 0;
			mChildWidth = (width - mPadding*2) / mColCount;
		}

		//childCount = mAdapter.getCount();
		/*if (mChildViews.size() < mColCount) {
			childCount = mChildViews.size();
		}*//*else{
			childCount = mColCount;
		}*/
		
		if (mChildViews.size() > 0) {
			for (int i = 0; i < childCount; i++) {
				if (mChildHeight == 0) {
					mChildHeight = getChildAt(i).getMeasuredHeight();
				}
				mChildViews.get(i).measure(
						MeasureSpec.makeMeasureSpec(mChildWidth,
								MeasureSpec.EXACTLY),
						MeasureSpec.makeMeasureSpec(mChildHeight,
								MeasureSpec.UNSPECIFIED));
			}
			height = Math.round(mRowCount * mChildHeight + (mRowCount -1) * getDividerBitmap().getWidth());
			setMeasuredDimension(width, height);
		} else {
			setMeasuredDimension(0, 0);
		}
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int offsetX = 0;
		int offsetY = 0;
		View childeView;
		for (int i = 0; i < childCount; i++) {
			int row = i / mColCount;
			int loc=i % mColCount;

			offsetX = mPadding + (mSpacing + mChildWidth)*loc;
			offsetY = PENDING_TOP + (mSpacing + mChildHeight)*row;
			childeView = mChildViews.get(i);
			childeView.layout(offsetX, offsetY,
					offsetX + childeView.getMeasuredWidth(),
					offsetY + childeView.getMeasuredHeight());
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {

		int width = getWidth();
		int height = getHeight();

		if (!mIsNeedDivider) {
			return;
		}
		try {

			if (childCount <= 0) {
				return;
			}
			setLinePaint();
			Bitmap drawable = null;
			drawable = getDividerBitmap();
			View childView = getChildAt(0);
			int childWidth = childView.getMeasuredWidth();
			int childHeight = childView.getMeasuredHeight();
			if (drawable == null) {
				int offsetY = (childHeight - drawable.getHeight()) >> 1;
				for (int i = 0; i < childCount; i++) {
					// 划竖线
					int rowIndex = i / mColCount;
					int colIndex = i % mColCount;
					/*int offsetX = mPadding + (mSpacing + childWidth)*colIndex ;
					* offsetY + rowIndex *(childHeight +  mSpacing)*/
					int offsetX = (colIndex + 1) * childWidth
							+ colIndex * mSpacing + mPadding;
					offsetY = PENDING_TOP + (mSpacing + childHeight)* rowIndex;
					if ((colIndex + 1) % (mColCount) != 0) {

						canvas.drawBitmap(drawable, offsetX, offsetY, mPaint1);
					}
				}
			} else {
				int offsetY = childHeight;

				for (int i = 0; i < childCount; i++) {
					// 划竖线
					int rowIndex = i / mColCount;
					int colIndex = i % mColCount;
					int offsetX = (colIndex + 1) * childWidth
							+ colIndex * mSpacing + mPadding;
					offsetY = (mSpacing + childHeight)* rowIndex;

					if ((colIndex + 1) % (mColCount) != 0) {
						canvas.drawLine(offsetX, offsetY, offsetX,
								offsetY + mChildHeight,
								mPaint);
					}
				}
			}
			// 划横线
			int rowNum = childCount / mColCount;
			if (childCount % mColCount != 0) {
				rowNum++;
			}
			for (int j = 0; j < rowNum - 1; j++) {
				canvas.drawLine(
						mSpacing,
						(j + 1) * (childHeight) + j * (mSpacing + PENDING_TOP),
						mColCount * (mSpacing + childWidth),
						(j + 1) * (childHeight) + j * (mSpacing + PENDING_TOP),
						mPaint);
			}

			if (mPaint == null) {
				mPaint = new Paint();
			}
			mPaint.setColor(UI_DIVIDER_LINE_COLOR);
			mPaint.setStrokeWidth(1.0f);
			canvas.drawLine(0, height-1, width, height - 1, mPaint);
		} catch (Exception e) {
			e.printStackTrace(); // 增加异常捕获
		}
		super.onDraw(canvas);
	}

	public static int getPENDING_TOP() {
		return PENDING_TOP;
	}

	public static void setPENDING_TOP(int pENDING_TOP) {
		PENDING_TOP = pENDING_TOP;
	}

	public static int getPENDING_BUTTOM() {
		return PENDING_BUTTOM;
	}

	public static void setPENDING_BUTTOM(int pENDING_BUTTOM) {
		PENDING_BUTTOM = pENDING_BUTTOM;
	}

	/**
	 * 设置夜间模式.
	 * */
	private void setLinePaint() {
		/*if (BdThemeManager.getInstance().isNightT5()) {
			mPaint.setColor(UI_CROSSLINE_COLOR_NIGHT);
			mPaint1.setAlpha(ALPHA_NO);
		} else {
			mPaint.setColor(UI_CROSSLINE_COLOR);
			mPaint1.setAlpha(ALPHA_NO);
		}*/

		mPaint.setColor(UI_CROSSLINE_COLOR);
		mPaint1.setAlpha(ALPHA_NO);
	}

	@Override
	public void onClick(PluginGridViewItem v, PluginConfigModle model) {

	}

	@Override
	public void onPressDown(PluginGridViewItem v) {

		v.setBackground();

	}

	@Override
	public void onPressUp(PluginGridViewItem v) {

		v.setBackground();

	}




	@Override
	public void onClick(View v) {
		Log.d(TAG, "onclick");
		if (!(v instanceof PluginGridViewItem)) {
			return;
		}
		((PluginGridViewItem)v).setBackground();
	}

/*
	@Override
	public boolean onLongClick(View v) {

		PluginConfigModle ConfigModle = ((PluginGridViewItem) v).getPluginModle();



		return true;
	}*/

	@Override
	public void onItemClick(View v) {

	}

	@Override
	public boolean onItemLongClick(View v) {
		return false;
	}
}
