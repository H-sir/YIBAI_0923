package com.ybw.yibai.common.widget.stickerview;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntDef;
import android.view.MotionEvent;

import com.ybw.yibai.common.widget.stickerview.event.StickerIconEvent;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 绘制"贴纸"四个方向的Icon图标
 *
 * @author sjl
 */
public class BitmapStickerIcon extends DrawableSticker implements StickerIconEvent {

    /**
     * 左上角
     */
    public static final int LEFT_TOP = 0;

    /**
     * 右上角
     */
    public static final int RIGHT_TOP = 1;

    /**
     * 左下角
     */
    public static final int LEFT_BOTTOM = 2;

    /**
     * 右下角
     */
    public static final int RIGHT_BOTTOM = 3;

    /**
     * 默认Icon半径
     */
    private static final float DEFAULT_ICON_RADIUS = 30f;

    /**
     * 默认Icon额外半径
     */
    private static final float DEFAULT_ICON_EXTRA_RADIUS = 10f;

    @IntDef({LEFT_TOP, RIGHT_TOP, LEFT_BOTTOM, RIGHT_BOTTOM})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Gravity {

    }

    @Gravity
    private int position = LEFT_TOP;

    /*
      /\_/\
    =( °w° )=
      )   (
     (__ __)
    */

    /**
     * 绘制圆的圆心X的坐标
     */
    private float x;

    /**
     * 绘制圆的圆心Y的坐标
     */
    private float y;

    /**
     * Icon半径
     */
    private float iconRadius = DEFAULT_ICON_RADIUS;

    /**
     * Icon额外半径
     */
    private float iconExtraRadius = DEFAULT_ICON_EXTRA_RADIUS;

    /**
     * "贴纸"四个方向的Icon图标的事件
     */
    private StickerIconEvent mIconEvent;

    /*
      /\_/\
    =( °w° )=
      )   (
     (__ __)
    */

    public BitmapStickerIcon(Drawable drawable) {
        super(drawable);
    }

    public BitmapStickerIcon(Drawable drawable, @Gravity int gravity) {
        super(drawable);
        this.position = gravity;
    }

    /**
     * 绘制Icon
     *
     * @param canvas
     */
    @Override
    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.TRANSPARENT);
        // 使用指定的绘制画出指定的圆
        // 绘制一个圆心坐标在(x,y)，半径为 iconRadius 的圆
        canvas.drawCircle(x, y, iconRadius, paint);
        super.draw(canvas);
    }

    /**
     * 绘制Icon
     *
     * @param canvas
     * @param paint
     */
    public void draw(Canvas canvas, Paint paint) {
        // 使用指定的绘制画出指定的圆
        // 绘制一个圆心坐标在(x,y)，半径为 iconRadius 的圆
        canvas.drawCircle(x, y, iconRadius, paint);
        super.draw(canvas);
    }

    /*----------*/

    /**
     * 获取绘制Icon图标的位置
     *
     * @return 绘制Icon图标的位置
     */
    @Gravity
    public int getPosition() {
        return position;
    }

    /**
     * 设置绘制Icon图标的位置
     *
     * @param position 绘制Icon图标的位置
     */
    public void setPosition(@Gravity int position) {
        this.position = position;
    }

    /*----------*/

    /**
     * 获取绘制圆的圆心X的坐标
     *
     * @return 圆心X的坐标
     */
    public float getX() {
        return x;
    }

    /**
     * 设置绘制圆的圆心X的坐标
     *
     * @param x 圆心X的坐标
     */
    public void setX(float x) {
        this.x = x;
    }

    /*----------*/

    /**
     * 获取绘制圆的圆心Y的坐标
     *
     * @return 圆心X的坐标
     */
    public float getY() {
        return y;
    }

    /**
     * 设置绘制圆的圆心Y的坐标
     *
     * @param y 圆心Y的坐标
     */
    public void setY(float y) {
        this.y = y;
    }

    /*----------*/

    /**
     * 获取Icon半径
     *
     * @return Icon半径
     */
    public float getIconRadius() {
        return iconRadius;
    }

    /**
     * 设置Icon半径
     *
     * @param iconRadius Icon半径
     */
    public void setIconRadius(float iconRadius) {
        this.iconRadius = iconRadius;
    }

    /*----------*/

    public float getIconExtraRadius() {
        return iconExtraRadius;
    }

    public void setIconExtraRadius(float iconExtraRadius) {
        this.iconExtraRadius = iconExtraRadius;
    }

    /*----------*/

    /**
     * 获取"贴纸"四个方向的Icon图标的事件
     *
     * @return "贴纸"四个方向的Icon图标的事件
     */
    public StickerIconEvent getIconEvent() {
        return mIconEvent;
    }

    /**
     * 设置"贴纸"四个方向的Icon图标的事件
     *
     * @param iconEvent "贴纸"四个方向的Icon图标的事件
     */
    public void setIconEvent(StickerIconEvent iconEvent) {
        this.mIconEvent = iconEvent;
    }

    /*----------*/

    /**
     * 按下手势
     *
     * @param stickerView 事件对象(贴纸View)
     * @param event       触屏事件
     */
    @Override
    public void onActionDown(StickerView stickerView, MotionEvent event) {
        if (null != mIconEvent) {
            mIconEvent.onActionDown(stickerView, event);
        }
    }

    /**
     * 移动手势
     *
     * @param stickerView 事件对象(贴纸View)
     * @param event       触屏事件
     */
    @Override
    public void onActionMove(StickerView stickerView, MotionEvent event) {
        if (null != mIconEvent) {
            mIconEvent.onActionMove(stickerView, event);
        }
    }

    /**
     * 抬起手势
     *
     * @param stickerView 事件对象(贴纸View)
     * @param event       触屏事件
     */
    @Override
    public void onActionUp(StickerView stickerView, MotionEvent event) {
        if (null != mIconEvent) {
            mIconEvent.onActionUp(stickerView, event);
        }
    }
}
