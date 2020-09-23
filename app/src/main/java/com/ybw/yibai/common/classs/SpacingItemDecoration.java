package com.ybw.yibai.common.classs;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 设置RecyclerView的Item的间距的类(瀑布流专用)
 *
 * @author sjl
 */
public class SpacingItemDecoration extends RecyclerView.ItemDecoration {

    private int space;

    public SpacingItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left = space / 2;
        outRect.right = space / 2;
        outRect.bottom = space;
    }
}