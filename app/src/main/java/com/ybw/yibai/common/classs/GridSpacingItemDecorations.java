package com.ybw.yibai.common.classs;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 设置RecyclerView的Item的间距的类
 *
 * @author sjl
 */
public class GridSpacingItemDecorations extends RecyclerView.ItemDecoration {

    private int spanCount;
    private int leftRightSpacing;
    private int topBottomSpacing;
    private boolean includeEdge;

    /**
     * @param spanCount        每一行Item的数目
     * @param leftRightSpacing 左右Item之间的间隔
     * @param topBottomSpacing 上下Item之间的间隔
     * @param includeEdge      是否包括边缘
     */
    public GridSpacingItemDecorations(int spanCount, int leftRightSpacing, int topBottomSpacing, boolean includeEdge) {
        this.spanCount = spanCount;
        this.leftRightSpacing = leftRightSpacing;
        this.topBottomSpacing = topBottomSpacing;
        this.includeEdge = includeEdge;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        // item position
        int position = parent.getChildAdapterPosition(view);
        // item column
        int column = position % spanCount;
        if (includeEdge) {
            // spacing - column * ((1f / spanCount) * spacing)
            outRect.left = leftRightSpacing - column * leftRightSpacing / spanCount;
            // (column + 1) * ((1f / spanCount) * spacing)
            outRect.right = (column + 1) * leftRightSpacing / spanCount;
            // top edge
            if (position < spanCount) {
                outRect.top = topBottomSpacing;
            }
            // item bottom
            outRect.bottom = topBottomSpacing;
        } else {
            // column * ((1f / spanCount) * spacing)
            outRect.left = column * leftRightSpacing / spanCount;
            // spacing - (column + 1) * ((1f / spanCount) * spacing)
            outRect.right = leftRightSpacing - (column + 1) * leftRightSpacing / spanCount;
            if (position >= spanCount) {
                // item top
                outRect.top = topBottomSpacing;
            }
        }
    }
}
