package com.ybw.yibai.common.widget.stickerview;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntDef;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.ybw.yibai.common.widget.stickerview.event.StickerUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author sjl
 */
public abstract class BaseSticker {

    /**
     * 存放位置
     */
    private Object tag;

    /**
     * 存放finallySkuId
     */
    private Object sign;

    /**
     * 盆栽名称
     */
    private String pottedName;

    /**
     * 盆栽高度
     */
    private String pottedHeight;

    /**
     * 对应的ID
     */
    private String productAndaugmentedSkuId;

    private boolean isHabit = false;

    private String habitUrl = "";

    /*
      /\_/\
    =( °w° )=
      )   (
     (__ __)
    */

    @IntDef(flag = true, value = {
            Position.CENTER,
            Position.TOP,
            Position.LEFT,
            Position.RIGHT,
            Position.BOTTOM
    })

    @Retention(RetentionPolicy.SOURCE)
    public @interface Position {
        int CENTER = 1;
        int TOP = 1 << 1;
        int LEFT = 1 << 2;
        int RIGHT = 1 << 3;
        int BOTTOM = 1 << 4;
    }

    /*
      /\_/\
    =( °w° )=
      )   (
     (__ __)
    */

    /**
     * 为垂直翻转
     */
    private boolean isFlippedVertically;

    /**
     * 为水平翻转
     */
    private boolean isFlippedHorizontally;

    /**
     *
     */
    private final Matrix mMatrix = new Matrix();

    /**
     *
     */
    private final RectF mTrappedRect = new RectF();

    /**
     *
     */
    private final float[] mMatrixValues = new float[9];

    /**
     *
     */
    private final float[] mBoundPoints = new float[8];

    /**
     *
     */
    private final float[] mMappedBounds = new float[8];

    /**
     *
     */
    private final float[] mUnRotatedWrapperCorner = new float[8];

    /**
     *
     */
    private final float[] mUnRotatedPoint = new float[2];

    /*
      /\_/\
    =( °w° )=
      )   (
     (__ __)
    */

    /**
     * 获取View宽度
     *
     * @return View宽度
     */
    public abstract int getWidth();

    /**
     * 获取View高度
     *
     * @return View高度
     */
    public abstract int getHeight();

    /**
     * 获取Drawable
     *
     * @return Drawable对象
     */
    @NonNull
    public abstract Drawable getDrawable();

    /**
     * 设置Drawable
     *
     * @param drawable Drawable对象
     * @return
     */
    public abstract BaseSticker setDrawable(@NonNull Drawable drawable);

    /**
     * 设置透明度
     *
     * @param alpha 透明度
     * @return
     */
    @NonNull
    public abstract BaseSticker setAlpha(@IntRange(from = 0, to = 255) int alpha);

    /**
     * 画图
     *
     * @param canvas 画布对象
     */
    public abstract void draw(@NonNull Canvas canvas);

    /*
      /\_/\
    =( °w° )=
      )   (
     (__ __)
    */

    public boolean isFlippedVertically() {
        return isFlippedVertically;
    }

    @NonNull
    public BaseSticker setFlippedVertically(boolean flippedVertically) {
        isFlippedVertically = flippedVertically;
        return this;
    }

    public boolean isFlippedHorizontally() {
        return isFlippedHorizontally;
    }

    @NonNull
    public BaseSticker setFlippedHorizontally(boolean flippedHorizontally) {
        isFlippedHorizontally = flippedHorizontally;
        return this;
    }

    @NonNull
    public Matrix getMatrix() {
        return mMatrix;
    }

    public BaseSticker setMatrix(@Nullable Matrix matrix) {
        this.mMatrix.set(matrix);
        return this;
    }

    /*
      /\_/\
    =( °w° )=
      )   (
     (__ __)
    */

    /**
     * 获取View范围点的坐标
     *
     * @return View范围点的坐标
     */
    public float[] getBoundPoints() {
        float[] points = new float[8];
        getBoundPoints(points);
        return points;
    }

    /**
     * 获取View范围点的坐标
     *
     * @param points 存取View范围点的坐标
     */
    public void getBoundPoints(@NonNull float[] points) {
        if (!isFlippedHorizontally) {
            if (!isFlippedVertically) {
                // 最开始的样子

                /*坐标原点(0.0)*/
                /*1-------2*---------X轴*/
                /*         */
                /*         */
                /*         */
                /*3-------4*/
                /*
                 *
                 *Y轴
                 */

                // 左上角
                points[0] = 0f;
                points[1] = 0f;

                // 右上角
                points[2] = getWidth();
                points[3] = 0f;

                // 左下角
                points[4] = 0f;
                points[5] = getHeight();

                // 右下角
                points[6] = getWidth();
                points[7] = getHeight();
            } else {
                // 垂直翻转

                /*坐标原点(0.0)*/
                /*3-------4*---------X轴*/
                /*         */
                /*---------*/ /*这根线就是翻转中心线*/
                /*         */
                /*1-------2*/
                /*
                 *
                 *Y轴
                 */

                // 左下角
                points[0] = 0f;
                points[1] = getHeight();

                // 右下角
                points[2] = getWidth();
                points[3] = getHeight();

                // 左上角
                points[4] = 0f;
                points[5] = 0f;

                // 左上角
                points[6] = getWidth();
                points[7] = 0f;
            }
        } else {
            if (!isFlippedVertically) {

                /*坐标原点(0.0)*/
                /*2-------1*---------X轴*/
                /*    -    */
                /*    -    */
                /*    -    */
                /*4-------3*/
                /*    这
                 *     根
                 *Y轴  线
                 */

                points[0] = getWidth();
                points[1] = 0f;

                points[2] = 0f;
                points[3] = 0f;

                points[4] = getWidth();
                points[5] = getHeight();

                points[6] = 0f;
                points[7] = getHeight();
            } else {
                /*坐标原点(0.0)*/
                /*4-------2*---------X轴*/
                /*      -  */
                /*    -    */
                /*  -      */
                /*3-------1*/
                /*
                 *
                 *Y轴
                 */

                points[0] = getWidth();
                points[1] = getHeight();

                points[2] = 0f;
                points[3] = getHeight();

                points[4] = getWidth();
                points[5] = 0f;

                points[6] = 0f;
                points[7] = 0f;
            }
        }
    }

    /*----------*/

    /**
     * 得到映射范围的点,即View的范围
     *
     * @return View范围点的坐标的二维点数组
     */
    @NonNull
    public float[] getMappedBoundPoints() {
        float[] dst = new float[8];
        getMappedPoints(dst, getBoundPoints());
        return dst;
    }

    /**
     * 得到映射点
     *
     * @param dst 存取View范围点的坐标的二维点数组
     * @param src View范围点的坐标
     */
    public void getMappedPoints(@NonNull float[] dst, @NonNull float[] src) {
        // 将这个矩阵应用到src指定的二维点数组中
        mMatrix.mapPoints(dst, src);
    }

    /**
     * 得到映射点
     *
     * @param src View范围点的坐标
     */
    @NonNull
    public float[] getMappedPoints(@NonNull float[] src) {
        float[] dst = new float[src.length];
        mMatrix.mapPoints(dst, src);
        return dst;
    }

    /*----------*/

    public Object getTag() {
        return tag;
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }

    public Object getSign() {
        return sign;
    }

    public void setSign(Object sign) {
        this.sign = sign;
    }

    public String getPottedName() {
        return pottedName;
    }

    public void setPottedName(String pottedName) {
        this.pottedName = pottedName;
    }

    public String getPottedHeight() {
        return pottedHeight;
    }

    public void setPottedHeight(String pottedHeight) {
        this.pottedHeight = pottedHeight;
    }

    public Matrix getmMatrix() {
        return mMatrix;
    }

    public RectF getmTrappedRect() {
        return mTrappedRect;
    }

    public float[] getmMatrixValues() {
        return mMatrixValues;
    }

    public float[] getmBoundPoints() {
        return mBoundPoints;
    }

    public float[] getmMappedBounds() {
        return mMappedBounds;
    }

    public float[] getmUnRotatedWrapperCorner() {
        return mUnRotatedWrapperCorner;
    }

    public float[] getmUnRotatedPoint() {
        return mUnRotatedPoint;
    }

    public String getProductAndaugmentedSkuId() {
        return productAndaugmentedSkuId;
    }

    public void setProductAndaugmentedSkuId(String productAndaugmentedSkuId) {
        this.productAndaugmentedSkuId = productAndaugmentedSkuId;
    }

    public boolean isHabit() {
        return isHabit;
    }

    public void setHabit(boolean habit) {
        isHabit = habit;
    }

    public String getHabitUrl() {
        return habitUrl;
    }

    public void setHabitUrl(String habitUrl) {
        this.habitUrl = habitUrl;
    }

    @NonNull
    public RectF getBound() {
        RectF bound = new RectF();
        getBound(bound);
        return bound;
    }

    public void getBound(@NonNull RectF dst) {
        dst.set(0, 0, getWidth(), getHeight());
    }

    /*----------*/

    @NonNull
    public RectF getMappedBound() {
        RectF dst = new RectF();
        getMappedBound(dst, getBound());
        return dst;
    }

    public void getMappedBound(@NonNull RectF dst, @NonNull RectF bound) {
        mMatrix.mapRect(dst, bound);
    }

    /*----------*/

    @NonNull
    public PointF getMappedCenterPoint() {
        PointF pointF = getCenterPoint();
        getMappedCenterPoint(pointF, new float[2], new float[2]);
        return pointF;
    }

    /**
     * 得到映射中心点
     *
     * @param dst
     * @param mappedPoints
     * @param src
     */
    public void getMappedCenterPoint(@NonNull PointF dst, @NonNull float[] mappedPoints, @NonNull float[] src) {
        getCenterPoint(dst);
        src[0] = dst.x;
        src[1] = dst.y;
        getMappedPoints(mappedPoints, src);
        dst.set(mappedPoints[0], mappedPoints[1]);
    }

    /*----------*/

    @NonNull
    public PointF getCenterPoint() {
        PointF center = new PointF();
        getCenterPoint(center);
        return center;
    }

    public void getCenterPoint(@NonNull PointF dst) {
        dst.set(getWidth() * 1f / 2, getHeight() * 1f / 2);
    }

    /*
      /\_/\
    =( °w° )=
      )   (
     (__ __)
    */

    public float getCurrentScale() {
        return getMatrixScale(mMatrix);
    }

    public float getCurrentHeight() {
        return getMatrixScale(mMatrix) * getHeight();
    }

    public float getCurrentWidth() {
        return getMatrixScale(mMatrix) * getWidth();
    }

    /**
     * 该方法计算给定矩阵对象的尺度值。
     */
    public float getMatrixScale(@NonNull Matrix matrix) {
        return (float) Math.sqrt(Math.pow(getMatrixValue(matrix, Matrix.MSCALE_X), 2) + Math.pow(getMatrixValue(matrix, Matrix.MSKEW_Y), 2));
    }

    /**
     * @return - 当前图像旋转角
     */
    public float getCurrentAngle() {
        return getMatrixAngle(mMatrix);
    }

    /**
     * 该方法计算给定矩阵对象的旋转角度
     */
    public float getMatrixAngle(@NonNull Matrix matrix) {
        return (float) Math.toDegrees(-(Math.atan2(getMatrixValue(matrix, Matrix.MSKEW_X), getMatrixValue(matrix, Matrix.MSCALE_X))));
    }

    public float getMatrixValue(@NonNull Matrix matrix, @IntRange(from = 0, to = 9) int valueIndex) {
        matrix.getValues(mMatrixValues);
        return mMatrixValues[valueIndex];
    }

    public boolean contains(float x, float y) {
        return contains(new float[]{x, y});
    }

    public boolean contains(@NonNull float[] point) {
        Matrix tempMatrix = new Matrix();
        tempMatrix.setRotate(-getCurrentAngle());
        getBoundPoints(mBoundPoints);
        getMappedPoints(mMappedBounds, mBoundPoints);
        tempMatrix.mapPoints(mUnRotatedWrapperCorner, mMappedBounds);
        tempMatrix.mapPoints(mUnRotatedPoint, point);
        StickerUtils.trapToRect(mTrappedRect, mUnRotatedWrapperCorner);
        return mTrappedRect.contains(mUnRotatedPoint[0], mUnRotatedPoint[1]);
    }

    public void release() {
    }
}
