/**
 * Filename:    BdHomeGridView.java
 * Description:
 * Copyright:   Baidu MIC Copyright(c)2011
 *
 * @author: Jacob
 * @version: 1.0
 * Create at:   2013-7-3 下午4:25:45
 * <p/>
 * Modification History:
 * Date         Author      Version     Description
 * ------------------------------------------------------------------
 * 2015-6-16     Jacob      1.0         1.0 Version
 */
package com.tamic.linegridview;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 宫格视图抽象类 其子类为baseGridview
 * 提供最基本的绘制和属性初始化工作,添加删除工作以及条目
 * @author LIUYONGKUI
 */
public abstract class PaAbsGridView extends ViewGroup {

    /**
     * 视图holder的标志，用于标记View中的tag
     */
    public static final int VIEW_HOLDER_TAG = 0x0fffff00;

    /**
     * 默认列数
     */
    public static final int DEF_COLCOUNT = 3;

    /**
     * 行数
     */
    private int mRowCount;
    /**
     * 列数
     */
    private int mColCount;
    /**
     * 宫格宽度
     */
    private float mCellWidth;
    /**
     * 宫格高度
     */
    private int mCellHeight;
    /**
     * 临时区域
     */
    private Rect mAssistRect = new Rect();
    /**
     * 插槽
     */
    private FrameLayout mSlotView;
    /**
     * 插槽的偏移量: y
     */
    private int mSlotOffsetY;
    /**
     * 插槽的偏移行
     */
    private int mSlotOffsetRow;
    /**
     * 分割线是否显示
     */
    private boolean mIsDividerEnable;
    /**
     * 分割线
     */
    private int mDividerWidth;
    /**
     * 交叉线行数
     */
    private int mDividerRow;
    /**
     * 交叉线列数
     */
    private int mDividerCol;
    /**
     * 分割线图片（白天）
     */
    private Drawable mDividerDay;
    /**
     * 分割线图片（夜晚）
     */
    private Drawable mDividerNight;
    /**
     * 数据适配器
     */
    private BaseAdapter mAdapter;

    /**
     * 构造函数
     *
     * @param context  上下文
     * @param aAdapter adapter
     * @param aColCount 列数
     */
    public PaAbsGridView(Context context, BaseAdapter aAdapter, int aColCount) {
        super(context);

        // 基本属性
        this.setWillNotDraw(false);
        mColCount = aColCount;
        mAdapter = aAdapter;
        mAdapter.registerDataSetObserver(new BdCellDataObserver());

        // 视图属性
        mCellHeight = getCellHeight();
        mDividerWidth = getDividerWidthDef();
        mDividerDay = getDividerDay();
        mDividerNight = getDividerDay();

        // 刷新视图
        refreshViews();
    }



    public void setAdapter(BaseAdapter mAdapter) {
        this.mAdapter = mAdapter;
    }


    public void setColCount(int mColCount) {
        this.mColCount = mColCount;
    }

    /**
     * 初始化视图
     */
    public void refreshViews() {

        if (mSlotView == null) {
            mSlotView = new FrameLayout(getContext());
            mSlotOffsetRow = 3;
        }

        final List<View> cacheList = new ArrayList<View>();
        for (int i = 0; i < getIconViewCount(); i++) {
            cacheList.add(getIconView(i));
        }

        removeAllIconViews();

        // 添加单元项
        for (int i = 0; i < mAdapter.getCount(); i++) {
            addItemView(queryCacheItemView(cacheList, i), false);
        }

        cacheList.clear();
    }

    /**
     * getDrviderDay
     * @return
     */
    protected Drawable getDividerDay() {
       return getResources().getDrawable(R.drawable.home_divider_day);
    }


    /**
     * getDrivderNight
     * @return
     */
    protected Drawable getDividerNight() {
        return getResources().getDrawable(R.drawable.home_divider_night);
    }


    /**
     * @return 适配器
     */
    public BaseAdapter getAdapter() {
        return mAdapter;
    }

    /**
     * @param aIsEnable 分割线是否有效
     */
    public void setIsDividerEnable(boolean aIsEnable) {
        mIsDividerEnable = aIsEnable;
    }

    /**
     * 获取缓存的单元视图
     *
     * @param aCacheList 缓存列表
     * @param aDataIndex 数据索引
     * @return 单元视图
     */
    private View queryCacheItemView(List<View> aCacheList, int aDataIndex) {
        final int count = ((aCacheList != null) ? aCacheList.size() : 0);

        View cacheView = null;

        // 寻找数据项匹配的单元视图
        Object item = mAdapter.getItem(aDataIndex);
        if (item != null) {
            for (int i = 0; i < count; i++) {
                View itemView = aCacheList.get(i);
                Object itemData = getViewHolder(itemView).getData();
                if ((itemData != null) && (itemData.equals(item))) {
                    aCacheList.remove(i);
                    cacheView = itemView;
                    break;
                }
            }
        }

        // 寻找类型匹配的单元视图
        View targetView = mAdapter.getView(aDataIndex, cacheView, null);
        getViewHolder(targetView).setData(item);
        return targetView;
    }

    /**
     * 往插槽里填充视图
     *
     * @param aView 视图
     */
    public void addToSlot(View aView) {
        if ((aView != null) && (mSlotView.indexOfChild(aView) < 0)) {
            mSlotView.addView(aView,
                new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        }
    }

    /**
     * 添加单元格视图
     *
     * @param aIconView 图标单元格视图
     */
    public void addIconView(View aIconView) {
        addView(aIconView);
    }

    /**
     * 添加单元格视图
     *
     * @param aIconView  单元格视图
     * @param aIconIndex 位置
     */
    public void addIconView(View aIconView, int aIconIndex) {
        addView(aIconView, aIconIndex + 1);
    }

    /**
     * 移除单元格视图
     *
     * @param aIconView 单元格视图
     */
    public void removeIconView(View aIconView) {
        removeView(aIconView);
    }

    /**
     * @return 单元格视图的总个数
     */
    public int getIconViewCount() {
        return getChildCount() - 1;
    }

    /**
     * @param aIndex 索引
     * @return 单元格视图
     */
    public View getIconView(int aIndex) {
        return getChildAt(aIndex + 1);
    }

    /**
     * 移除所有的单元格视图
     */
    public void removeAllIconViews() {
        removeAllViews();
        addView(mSlotView);
    }

    /**
     * 释放所有视图资源
     */
    public void releaseAllViews() {
        removeAllViews();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // 获取给定尺寸
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int nChildCount = getIconViewCount();

        // 计算总行数和总列数
        mColCount = getColCount();
        int row = nChildCount / mColCount;
        int col = nChildCount % mColCount;
        mRowCount = ((col == 0) ? row : row + 1);

        // 计算单元格尺寸
        int dividerW = getDividerWidth() * (mColCount + 1);
        mCellWidth = 1.0f * (width - getPaddingLeft() - getPaddingRight() - dividerW) / mColCount;

        // 遍历子view的尺寸设置
        for (int i = 0; i < nChildCount; i++) {
            View child = getIconView(i);
            child.measure((int) mCellWidth, (int) mCellHeight);
        }

        // slot
        int slotHeightMeasureSpec =
            getChildMeasureSpec(heightMeasureSpec, 0, LayoutParams.WRAP_CONTENT);
        mSlotView.measure(widthMeasureSpec, slotHeightMeasureSpec);
        int slotRow = getSlotRow();
        mSlotOffsetY = (int) (slotRow * mCellHeight + (slotRow + 1) * getDividerWidth());

        // 计算总尺寸
        int nViewHeight = Math.round(mRowCount * mCellHeight + (mRowCount + 1) * getDividerWidth());
        nViewHeight = nViewHeight + mSlotView.getMeasuredHeight();
        if (slotRow < mRowCount && mSlotView.getMeasuredHeight() > 0) {
            nViewHeight = nViewHeight + getDividerWidth();
        }
        nViewHeight = nViewHeight + getPaddingTop() + getPaddingBottom();

        // 设置总尺寸
        setMeasuredDimension(width, nViewHeight);

        // 分割线初始化
        initDividerData();
    }

    @Override
    protected void onLayout(boolean change, int l, int t, int r, int b) {
        mSlotView.layout(0, mSlotOffsetY,
                mSlotView.getMeasuredWidth(), mSlotOffsetY + mSlotView.getMeasuredHeight());

        // 单元视图
        int count = getIconViewCount();
        for (int i = 0; i < count; i++) {
            View childView = getIconView(i);
            AnimInfo animInfo = getViewHolder(childView).getAnimInfo();
            layoutItem(childView, i, animInfo.isMove());
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 分割线
        if (mIsDividerEnable) {
            drawDivider(canvas);
        }
    }

    /**
     * 初始化分割线数据
     */
    public void initDividerData() {
        // 计算绘制行数
        int row = getIconViewCount() / mColCount;
        int col = getIconViewCount() % mColCount;
        mDividerRow = ((col == 0) ? row + 1 : row + 2);

        // 计算绘制列
        mDividerCol = mColCount + 1;
    }

    /**
     * 绘制分割线
     *
     * @param aCanvas 画布
     */
    public void drawDivider(Canvas aCanvas) {
        // 绘制交叉线
        drawCrossLine(aCanvas, null, 0, getDividerWidth());
    }

    /**
     * @return 分割线图片
     */
    public Drawable getDivider() {
       /* if (BdThemeManager.getInstance().isNightT5()) {
            return mDividerNight;
        } else {
            return mDividerDay;
        }*/

        return mDividerDay;
    }

    /**
     * 绘制分割线
     *
     * @param aCanvas     画布
     * @param aPaint      画笔
     * @param aLineOffset 偏移
     * @param aLineWidth  线宽度
     */
    public void drawCrossLine(Canvas aCanvas, Paint aPaint, int aLineOffset, int aLineWidth) {
        // 变量
        int max;
        int offset;

        int lastCol = getIconViewCount() % mColCount;
        int bannerRow = getSlotRow();
        Drawable divider = getDivider();

        // Horizontal line
        max = mDividerRow;

        int left = getPaddingLeft();
        int right = getMeasuredWidth() - getPaddingRight();
        offset = getPaddingTop() + aLineOffset;

        for (int i = 0; i < max; i++) {
            if (i == max - 1) { // 最后一行停留在某个格子右边
                // 右边终点重新确定
                if (lastCol > 0) {
                    right = (int) (getPaddingLeft() + lastCol * mCellWidth + (lastCol + 1) * getDividerWidth());
                }
            } else if (i == bannerRow && mSlotView.getMeasuredHeight() > 0) { // banner特殊处理
                divider.setBounds(left, offset, right, offset + aLineWidth);
                divider.draw(aCanvas);

                offset += getDividerWidth() + mSlotView.getMeasuredHeight();
            }
            // 绘制
            divider.setBounds(left, offset, right, offset + aLineWidth);
            divider.draw(aCanvas);

            offset += mCellHeight + getDividerWidth();
        }

        // Vertical line
        max = mDividerCol;

        int top = getPaddingTop();
        int bottom = 0;

        for (int i = 0; i < max; i++) {
            offset = (int) (getPaddingLeft() + aLineOffset + i * (mCellWidth + getDividerWidth()));
            if (i > lastCol && lastCol > 0) {
                bottom = (int) (getPaddingTop() + mCellHeight * (mRowCount - 1) + getDividerWidth() * mRowCount);
            } else {
                bottom = (int) (getPaddingTop() + mCellHeight * mRowCount + getDividerWidth() * (mRowCount + 1));
            }
            if (bottom >= mSlotOffsetY && mSlotView.getMeasuredHeight() > 0) {
                int midBottom = mSlotOffsetY - getDividerWidth();
                int midTop = mSlotOffsetY + mSlotView.getMeasuredHeight();
                bottom += mSlotView.getMeasuredHeight();

                divider.setBounds(offset, top, offset + aLineWidth, midBottom);
                divider.draw(aCanvas);

                divider.setBounds(offset, midTop, offset + aLineWidth, bottom);
                divider.draw(aCanvas);
            } else {
                divider.setBounds(offset, top, offset + aLineWidth, bottom);
                divider.draw(aCanvas);
            }
        }
    }

    /**
     * 增加数据项对应的子项
     *
     * @param aItemView   item view
     * @param isAfterInit init
     */
    private void addItemView(View aItemView, boolean isAfterInit) {
        if (isAfterInit) {
            addIconView(aItemView, getIconViewCount() - 1);
        } else {
            addIconView(aItemView);
        }
    }

    /**
     * 布局子项；可重载，允许子类有实现其它功能的机会
     *
     * @param aChild 子项
     * @param aPos   位置
     * @param aIsAnim 是否做动画
     */
    public void layoutItem(View aChild, int aPos, boolean aIsAnim) {
        // 获取视图的动画信息
        ViewHolder holder = getViewHolder(aChild);
        AnimInfo animInfo = holder.getAnimInfo();

        // 获取相应位置的矩形区域
        final Rect r = mAssistRect;
        getChildArea(r, aChild, aPos);

        // 如果动画进行中，重新定义动画属性； 否则直接布局
        if (aIsAnim) {
            animInfo.beginMove(aChild.getLeft(), aChild.getTop(), r.left, r.top);
        } else {
            aChild.layout(r.left, r.top, r.right, r.bottom);
        }

        // 重新定义视图位置
        holder.setPosition(aPos);
    }

    /**
     * 更新动画中子项的状态
     */
    public void updateAnimInfo(float aFactor) {
        // 更新移动项
        int count = getIconViewCount();
        for (int i = 0; i < count; i++) {
            View itemView = getIconView(i);
            AnimInfo animInfo = getViewHolder(itemView).getAnimInfo();
            if (animInfo.isMove()) {
                animInfo.move(aFactor);
            }
        }
    }

    /**
     * 更新动画中子项的布局
     */
    public void updateAnimLayout(float aFactor) {
        // 重新布局
        int count = getIconViewCount();
        for (int i = 0; i < count; i++) {
            View itemView = getIconView(i);
            AnimInfo animInfo = getViewHolder(itemView).getAnimInfo();
            if (animInfo.isMove()) {
                itemView.layout(animInfo.getX(), animInfo.getY(),
                        animInfo.getX() + itemView.getMeasuredWidth(),
                        animInfo.getY() + itemView.getMeasuredHeight());

                // 结束标记
                if (aFactor == 1.0f) {
                    animInfo.endMove();
                }
            }
        }
    }

    /**
     * 获取给定位置上的子项区域
     *
     * @param outRect 存储区域
     * @param child   子项
     * @param pos     子项位置
     */
    public void getChildArea(Rect outRect, View child, int pos) {
        getCellArea(outRect, pos);

        int offsetX = (int) (outRect.left + (mCellWidth - child.getMeasuredWidth()) / 2);
        int offsetY = (int) (outRect.top + (mCellHeight - child.getMeasuredHeight()) / 2);

        outRect.set(offsetX, offsetY, offsetX + child.getMeasuredWidth(), offsetY + child.getMeasuredHeight());
    }

    /**
     * 获取单元格的区域
     *
     * @param outRect 所在区域
     * @param pos     单元格位置
     */
    public void getCellArea(Rect outRect, int pos) {
        int row = pos / mColCount;
        int col = pos % mColCount;

        int bannerRow = (mRowCount > mSlotOffsetRow ? mSlotOffsetRow : mRowCount);
        int startX = (int) (getPaddingLeft() + (col + 1) * getDividerWidth() + col * mCellWidth);
        int startY = (int) (getPaddingTop() + (row + 1) * getDividerWidth() + row * mCellHeight);
        // 如果banner在宫格的上方存在，那么要多加一条分割线
        if ((row >= bannerRow) && mSlotView.getMeasuredHeight() > 0) {
            startY = startY + mSlotView.getMeasuredHeight() + getDividerWidth();
        }
        outRect.set(startX, startY, (int) (startX + mCellWidth), (int) (startY + mCellHeight));
    }

    /**
     * @return banner所在行
     */
    public int getSlotRow() {
        return (mRowCount > mSlotOffsetRow ? mSlotOffsetRow : mRowCount);
    }

    /**
     * 快速定位位置
     *
     * @param x 当前坐标x
     * @param y 当前坐标y
     * @return 命中的单元格索引
     */
    public int getCellPosition(int x, int y) {
        // 先去除banner的偏移
        if (y > mSlotOffsetY) {
            y -= mSlotView.getMeasuredHeight();
        }

        int row = (int) ((y - getPaddingTop()) / mCellHeight);
        int col = (int) ((x - getPaddingLeft()) / mCellWidth);
        return row * mColCount + col;
    }

    /**
     * @return 单元格宽度
     */
    public int getCellWidth() {
        return (int) mCellWidth;
    }

    /**
     * @return 单元格高度
     */
    public int getCellHeight() {


        return mCellHeight == -1 ? getResources().getDimensionPixelSize(R.dimen.home_item_height): mCellHeight;
    }

    /**
     * @return 分割线宽度
     */
    public int getDividerWidth() {
        return (mIsDividerEnable ? mDividerWidth : 0);
    }

    /**
     * @return 默认分割线宽度
     */
    public int getDividerWidthDef() {
        return mDividerWidth == -1 ? getResources().getDimensionPixelOffset(R.dimen.home_divider_width): mDividerWidth;
    }




    /**
     * @return 总行数
     */
    public int getRowCount() {
        return mRowCount;
    }

    /**
     * @return 总列数
     */
    public int getColCount() {
        return mColCount == -1 ? DEF_COLCOUNT : mColCount;
    }

    /**
     * 改变子项的位置
     *
     * @param aFromPosition 原始位置
     * @param aToPosition   终点位置
     * @param isAnimation   是否附带动画效果
     */
    public void changeItemPosition(int aFromPosition, int aToPosition, boolean isAnimation) {
        int nChildCount = getIconViewCount();

        // 边界判断
        if ((aFromPosition < 0) || (aFromPosition >= nChildCount) || (aToPosition < 0)
            || (aToPosition >= nChildCount)) {
            return;
        }

        // 寻找子项
        View fromView = null;
        View toView = null;
        for (int i = 0; i < nChildCount; i++) {
            View childView = getIconView(i);
            if (getViewPosition(childView) == aFromPosition) {
                fromView = childView;
            }
            if (getViewPosition(childView) == aToPosition) {
                toView = childView;
            }
        }

        // 设置新的项
        if (fromView == toView) {
            layoutItem(fromView, aToPosition, isAnimation);
        } else {
            layoutItem(fromView, aToPosition, isAnimation);
            layoutItem(toView, aFromPosition, isAnimation);
        }
    }

    /**
     * @param aView 视图
     * @return 视图位置
     */
    public int getViewPosition(View aView) {
        return getViewHolder(aView).getPosition();
    }

    /**
     * @param aView 视图
     * @param aPos 视图位置
     */
    public void setViewPosition(View aView, int aPos) {
        getViewHolder(aView).setPosition(aPos);
    }

    /**
     * @param aView 视图
     * @return holder
     */
    private ViewHolder getViewHolder(View aView) {
        ViewHolder holder = (ViewHolder) aView.getTag(VIEW_HOLDER_TAG);
        if (holder == null) {
            holder = new ViewHolder();
            aView.setTag(VIEW_HOLDER_TAG, holder);
        }
        return holder;
    }

    /**
     * 数据更新类
     */
    class BdCellDataObserver extends DataSetObserver {
        @Override
        public void onChanged() {
            super.onChanged();

            // 刷新视图
            refreshViews();
        }
    }

    /**
     * 视图holder
     */
    class ViewHolder {
        /**
         * 动画信息
         */
        AnimInfo mAnimInfo;
        /**
         * 视图数据
         */
        Object mViewData;
        /**
         * 视图位置
         */
        int mViewPosition;

        /**
         * 构造函数
         */
        public ViewHolder() {
            mAnimInfo = new AnimInfo();
        }

        /**
         * @return 动画信息
         */
        public AnimInfo getAnimInfo() {
            return mAnimInfo;
        }

        /**
         * @param aData 数据
         */
        public void setData(Object aData) {
            mViewData = aData;
        }

        /**
         * @return 数据
         */
        public Object getData() {
            return mViewData;
        }

        /**
         * @param aPosition 位置
         */
        public void setPosition(int aPosition) {
            mViewPosition = aPosition;
        }

        /**
         * @return 位置
         */
        public int getPosition() {
            return mViewPosition;
        }

    }

    /**
     * 动画信息
     */
    class AnimInfo {

        /**
         * 目标坐标
         */
        int mStartX;

        /**
         * 目标坐标
         */
        int mStartY;

        /**
         * 目标坐标
         */
        int mTargetX;

        /**
         * 目标坐标
         */
        private int mTargetY;

        /**
         * 目标坐标
         */
        private int mMoveX;

        /**
         * 目标坐标
         */
        private int mMoveY;

        /**
         * 目标坐标
         */
        private boolean mIsMove;

        /**
         * 开始移动
         *
         * @param aStartX  起始x值
         * @param aStartY  起始y值
         * @param aTargetX 目标x值
         * @param aTargetY 目标y值
         */
        public void beginMove(int aStartX, int aStartY, int aTargetX, int aTargetY) {
            setIsMove(true);

            mTargetX = aTargetX;
            mTargetY = aTargetY;

            mStartX = aStartX;
            mStartY = aStartY;
        }

        /**
         * 结束移动
         */
        public void endMove() {
            setIsMove(false);
        }

        /**
         * @param aFactor 比例
         */
        public void move(float aFactor) {
            if (isMove()) {
                mMoveX = mStartX + (int) ((mTargetX - mStartX) * aFactor);
                mMoveY = mStartY + (int) ((mTargetY - mStartY) * aFactor);
            }
        }

        /**
         * 设置是否在移动
         *
         * @param isMove 标志
         */
        public void setIsMove(boolean isMove) {
            mIsMove = isMove;
        }

        /**
         * 判断是否在移动
         *
         * @return 判断结果
         */
        public boolean isMove() {
            return mIsMove;
        }

        /**
         * @return x
         */
        public int getX() {
            return mMoveX;
        }

        /**
         * @return y
         */
        public int getY() {
            return mMoveY;
        }

    }

}
