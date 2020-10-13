package com.ybw.yibai.common.widget.stickerview;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

public class DrawableSticker extends BaseSticker {

    private Rect mRect;

    private Drawable mDrawable;

    public DrawableSticker(Drawable drawable) {
        mDrawable = drawable;
        mRect = new Rect(0, 0, getWidth(), getHeight());
    }

    @Override
    public int getWidth() {
        // 返回可绘制图形的固有宽度
        return mDrawable == null ? 0 : mDrawable.getIntrinsicWidth();
    }

    @Override
    public int getHeight() {
        // 返回可绘制图形的固有高度
        return mDrawable == null ? 0 : mDrawable.getIntrinsicHeight();
    }

    @NonNull
    @Override
    public Drawable getDrawable() {
        return mDrawable;
    }

    @Override
    public DrawableSticker setDrawable(@NonNull Drawable drawable) {
        mDrawable = drawable;
        return this;
    }

    @NonNull
    @Override
    public BaseSticker setAlpha(int alpha) {
        mDrawable.setAlpha(alpha);
        return this;
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        // 保存当前状态
        canvas.save();
        // 使用指定的矩阵预先包含当前矩阵
        canvas.concat(getMatrix());
        // 为Drawable指定边界矩形,这是drawable在调用draw()方法时绘制的位置
        mDrawable.setBounds(mRect);
        mDrawable.draw(canvas);
        // 恢复Canvas之前保存的状态,防止save()方法代码之后对Canvas运行的操作
        canvas.restore();
    }

    @Override
    public void release() {
        super.release();
        if (null != mDrawable) {
            mDrawable = null;
        }
    }
}
