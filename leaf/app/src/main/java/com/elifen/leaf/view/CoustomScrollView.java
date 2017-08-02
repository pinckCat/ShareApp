package com.elifen.leaf.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by Elifen on 2017/4/24.
 */

public class CoustomScrollView extends ScrollView{
    private OnScrollListener onScrollListener;

    public CoustomScrollView(Context context) {
        this(context, null);
    }

    public CoustomScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CoustomScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * 设置滚动接口
     * @param onScrollListener
     */
    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (onScrollListener != null);
    }

        /**
         *
         * 滚动的回调接口
         */
        public interface OnScrollListener{
            /**
             * 回调方法， 返回CoustomScrollView滑动的Y方向距离
             * @param scrollY
             * 				、
             */
            public void onScroll(int scrollY);
        }
}
