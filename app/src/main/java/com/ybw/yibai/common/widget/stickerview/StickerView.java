package com.ybw.yibai.common.widget.stickerview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;

import com.ybw.yibai.R;
import com.ybw.yibai.base.YiBaiApplication;
import com.ybw.yibai.common.bean.SimulationData;
import com.ybw.yibai.common.utils.DensityUtil;
import com.ybw.yibai.common.utils.LogUtil;
import com.ybw.yibai.common.utils.SDCardHelperUtil;
import com.ybw.yibai.common.widget.stickerview.event.OnStickerOperationListener;
import com.ybw.yibai.common.widget.stickerview.event.StickerUtils;
import com.ybw.yibai.common.widget.stickerview.event.StickerViewEvent;
import com.ybw.yibai.common.widget.stickerview.event.StickerViewSelectedListener;

import java.io.File;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.os.Environment.DIRECTORY_PICTURES;
import static com.ybw.yibai.common.widget.stickerview.BitmapStickerIcon.LEFT_BOTTOM;
import static com.ybw.yibai.common.widget.stickerview.BitmapStickerIcon.LEFT_TOP;
import static com.ybw.yibai.common.widget.stickerview.BitmapStickerIcon.RIGHT_TOP;

/**
 * 贴纸View
 *
 * @author sjl
 */
public class StickerView extends FrameLayout {

    private static final String TAG = "StickerView";

    /**
     * 触发移动事件的最短距离
     */
    private int touchSlop;

    /**
     * "贴纸View"是否处于锁定状态
     */
    private boolean locked;

    /**
     * 是否显示"贴纸"四个方向的Icon图标
     */
    private boolean showIcons;

    /**
     * 是否显示"贴纸"四个方向的边框
     */
    private boolean showBorder;

    /**
     * 把当前贴纸显示到最前面
     */
    private boolean bringToFrontCurrentSticker;

    /**
     *
     */
    private boolean constrained;

    /*
      /\_/\
    =( °w° )=
      )   (
     (__ __)
    */

    @IntDef({
            ActionMode.NONE,                 // 无操作
            ActionMode.DRAG,                 // 拖曳
            ActionMode.CLICK,                // 单击
            ActionMode.ZOOM_WITH_TWO_FINGER, // 双指缩放
            ActionMode.ICON                  // 用户当前操作"贴纸"Icon图标
    })

    @Retention(RetentionPolicy.SOURCE)
    protected @interface ActionMode {
        int NONE = 0;
        int DRAG = 1;
        int CLICK = 2;
        int ZOOM_WITH_TWO_FINGER = 3;
        int ICON = 4;
    }

    /**
     * 当前动作类型
     */
    @ActionMode
    private int mCurrentMode = ActionMode.NONE;

    /**
     * 水平翻转
     */
    public static final int FLIP_HORIZONTALLY = 1;

    /**
     * 垂直翻转
     */
    public static final int FLIP_VERTICALLY = 1 << 1;

    @IntDef(flag = true, value = {FLIP_HORIZONTALLY, FLIP_VERTICALLY})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Flip {

    }

    /*
      /\_/\
    =( °w° )=
      )   (
     (__ __)
    */

    /**
     * 默认的最小点击延迟时间
     */
    private static final int DEFAULT_MIN_CLICK_DELAY_TIME = 200;

    /**
     *
     */
    private long lastClickTime = 0;

    /**
     *
     */
    private int minClickDelayTime = DEFAULT_MIN_CLICK_DELAY_TIME;

    /**
     * 用户按下手势时相对于View左边界的距离
     */
    private float downX;

    /**
     * 用户按下手势时相对于容器上边界的距离
     */
    private float downY;

    /**
     * 两个手指距离
     */
    private float oldDistance = 0f;

    /**
     * 旋转角度
     */
    private float oldRotation = 0f;

    /**
     * 当前正在操作的Sticker
     */
    private BaseSticker mHandlingSticker;

    /**
     * 用户当前操作的"贴纸"Icon图标
     */
    private BitmapStickerIcon mCurrentIcon;

    /**
     *
     */
    private final float[] mTemp = new float[2];

    /**
     *
     */
    private final float[] mPoint = new float[2];

    /**
     * 存取StickerView范围点的坐标
     */
    private final float[] mBounds = new float[8];

    /**
     * 存取StickerView范围点的坐标
     */
    private final float[] mBitmapPoints = new float[8];

    /**
     *
     */
    private final Matrix mSizeMatrix = new Matrix();

    /**
     *
     */
    private final Matrix mDownMatrix = new Matrix();

    /**
     *
     */
    private final Matrix mMoveMatrix = new Matrix();

    /**
     * 绘制"盆栽名称"的RectF
     */
    private RectF mPottedNameRectF = new RectF();

    /**
     * 绘制"盆栽高度"的RectF
     */
    private RectF mPottedHeightRectF = new RectF();

    /**
     * 两个点的坐标的中间点坐标
     */
    private PointF mMidPoint = new PointF();

    /**
     *
     */
    private final RectF mStickerRectF = new RectF();

    /**
     *
     */
    private final PointF mCurrentCenterPoint = new PointF();

    /**
     * 画"贴纸View"四个边框的画笔
     */
    private final Paint mBorderPaint = new Paint();

    /**
     * 画"贴纸View"顶部显示盆栽信息的画笔
     */
    private final Paint mPottingInfoPaint = new Paint();

    /**
     * 画文字使用的画笔
     */
    private final Paint mTextPaint = new Paint();

    /**
     * "贴纸View"的集合
     */
    private final List<BaseSticker> mStickerList = new ArrayList<>();

    /**
     * 存储"贴纸"四个方向的Icon图标的集合
     */
    private final List<BitmapStickerIcon> mIconList = new ArrayList<>(4);

    /**
     * "贴纸View"操作监听器
     */
    private OnStickerOperationListener mOnStickerOperationListener;

    /**
     * "贴纸View"移动操作监听器
     */
    private StickerViewEvent mStickerViewEvent;

    /**
     * "贴纸View"是否被选中状态侦听器
     */
    private StickerViewSelectedListener mStickerViewSelectedListener;

    /**
     * 上下文对象
     */
    private Context mContext;

    /*
      /\_/\
    =( °w° )=
      )   (
     (__ __)
    */

    public StickerView(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public StickerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public StickerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public void init(Context context, AttributeSet attrs) {
        mContext = context;
        // 它获得的是触发移动事件的最短距离,如果小于这个距离就不触发移动控件
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        // 先对自定义的属性进行处理.只有当前控件是在布局文件中使用的时候，attrs才不是null
        if (null != attrs) {
            // 获取由自定义属性组成的数组
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.StickerView);

            showIcons = typedArray.getBoolean(R.styleable.StickerView_showIcons, true);
            showBorder = typedArray.getBoolean(R.styleable.StickerView_showBorder, true);
            bringToFrontCurrentSticker = typedArray.getBoolean(R.styleable.StickerView_bringToFrontCurrentSticker, false);

            mBorderPaint.setAntiAlias(true);
            mBorderPaint.setColor(typedArray.getColor(R.styleable.StickerView_borderColor, Color.parseColor("#000027")));

            mPottingInfoPaint.setAntiAlias(true);

            mTextPaint.setAntiAlias(true);
            mTextPaint.setTextAlign(Paint.Align.LEFT);
            mTextPaint.setTypeface(Typeface.DEFAULT_BOLD);
            mTextPaint.setTextSize(DensityUtil.dpToPx(mContext, 12));
            mTextPaint.setColor(ContextCompat.getColor(context, R.color.main_text_color));

            // 回收释放
            typedArray.recycle();
            configDefaultIcons();
        }
        mBorderPaint.setStrokeWidth((float) DensityUtil.dpToPx(context, 0.8f));
    }

    /**
     * 配置"贴纸View"默认的角Icon
     */
    public void configDefaultIcons() {
        // 左上角"删除Icon"
        BitmapStickerIcon deleteIcon = new BitmapStickerIcon(
                ContextCompat.getDrawable(getContext(), R.mipmap.simulate_delete),
                BitmapStickerIcon.LEFT_TOP
        );

        // 右上角"翻转Icon"
        BitmapStickerIcon flipIcon = new BitmapStickerIcon(
                ContextCompat.getDrawable(getContext(), R.mipmap.simulate_copy),
                BitmapStickerIcon.RIGHT_TOP
        );

        // 右下角"缩放Icon"
        BitmapStickerIcon zoomIcon = new BitmapStickerIcon(
                ContextCompat.getDrawable(getContext(), R.mipmap.simulate_zoom),
                BitmapStickerIcon.RIGHT_BOTTOM
        );

        mIconList.clear();
        mIconList.add(deleteIcon);
        mIconList.add(zoomIcon);
        mIconList.add(flipIcon);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            mStickerRectF.left = left;
            mStickerRectF.top = top;
            mStickerRectF.right = right;
            mStickerRectF.bottom = bottom;
        }
    }

    /**
     * onDraw()先于dispatchDraw()执行,用于本身控件的绘制,dispatchDraw()用于子控件的绘制
     * onDraw()绘制的内容可能会被子控件覆盖而dispatchDraw()是子控件的绘制,所以是覆盖在onDraw()上的
     *
     * @param canvas 画布对象
     */
    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        drawStickers(canvas);
    }

    /**
     * 绘制"贴纸View"
     *
     * @param canvas 画布对象
     */
    protected void drawStickers(Canvas canvas) {
        for (int i = 0; i < mStickerList.size(); i++) {
            BaseSticker sticker = mStickerList.get(i);
            if (null != sticker) {
                sticker.draw(canvas);
            }
        }

        boolean isTrue = null != mHandlingSticker && !locked && (showBorder || showIcons);
        if (!isTrue) {
            return;
        }
        getStickerPoints(mHandlingSticker, mBitmapPoints);
        // 左上角
        float x1 = mBitmapPoints[0];
        float y1 = mBitmapPoints[1];
        // 右上角
        float x2 = mBitmapPoints[2];
        float y2 = mBitmapPoints[3];
        // 左下角
        float x3 = mBitmapPoints[4];
        float y3 = mBitmapPoints[5];
        // 右下角
        float x4 = mBitmapPoints[6];
        float y4 = mBitmapPoints[7];
        // 画"贴纸"四个方向的边框
        if (showBorder) {
            canvas.drawLine(x1, y1, x2, y2, mBorderPaint);
            canvas.drawLine(x1, y1, x3, y3, mBorderPaint);
            canvas.drawLine(x2, y2, x4, y4, mBorderPaint);
            canvas.drawLine(x3, y3, x4, y4, mBorderPaint);
        }

        // 画"贴纸"四个方向的Icon
        if (!showIcons) {
            return;
        }
        int iconWidth = 0;
        // 计算旋转角度
        float rotation = calculateRotation(x4, y4, x3, y3);
        for (int i = 0; i < mIconList.size(); i++) {
            BitmapStickerIcon icon = mIconList.get(i);
            // 获取绘制Icon图标的位置
            int position = icon.getPosition();
            if (LEFT_TOP == position) {
                // 左上角
                configIconMatrix(icon, x1, y1, rotation);
            } else if (RIGHT_TOP == position) {
                // 右上角
                configIconMatrix(icon, x2, y2, rotation);
            } else if (LEFT_BOTTOM == position) {
                // 左下角
                configIconMatrix(icon, x3, y3, rotation);
            } else {
                // 右下角
                configIconMatrix(icon, x4, y4, rotation);
            }
            icon.draw(canvas);
            iconWidth = icon.getWidth();
        }

        // 绘制盆栽名称背景
//        mPottedHeightRectF.left = x1;
//        mPottedHeightRectF.right = x2;
//        mPottedHeightRectF.top = y1 - iconWidth / 2 - DensityUtil.dpToPx(mContext, 20);
//        mPottedHeightRectF.bottom = y1 - iconWidth / 2;

        // 绘制盆栽高度背景
        mPottedHeightRectF.left = x2-50;
        mPottedHeightRectF.right = x2 + 150;
        mPottedHeightRectF.top = ((y4 - y2) / 2 + y2) - 50;
        mPottedHeightRectF.bottom = ((y4 - y2) / 2 + y2) + 50;
//        mPottedNameRectF.left = x1;
//        mPottedNameRectF.right = x2;
//        mPottedNameRectF.top = mPottedHeightRectF.top - DensityUtil.dpToPx(mContext, 18);
//        mPottedNameRectF.bottom = mPottedHeightRectF.top;

        // 先裁切画布(裁切方法之后的绘制内容,都会被限制在裁切范围内)
        canvas.clipRect(mPottedHeightRectF.left, mPottedHeightRectF.top, mPottedHeightRectF.right, mPottedHeightRectF.bottom);
//        canvas.clipRect(mPottedNameRectF.left, mPottedNameRectF.top, mPottedNameRectF.right, mPottedHeightRectF.bottom);

        // 在绘制文字
        if (null == mHandlingSticker) {
            return;
        }
        String pottedName = mHandlingSticker.getPottedName();
        if (TextUtils.isEmpty(pottedName)) {
            return;
        }
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        float distances = (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom;
//        float baseline = mPottedNameRectF.centerY() + distances;
//        canvas.drawText(pottedName, mPottedNameRectF.left, baseline, mTextPaint);


        String pottedHeight = mHandlingSticker.getPottedHeight();
        if (!TextUtils.isEmpty(pottedHeight)) {
            float baselines = mPottedHeightRectF.centerY() + distances;
            canvas.drawColor(getResources().getColor(R.color.white_fifty_percent_color));//绘制透明色
            canvas.drawText(pottedHeight, mPottedHeightRectF.left, baselines, mTextPaint);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        super.onSizeChanged(w, h, oldW, oldH);
        for (int i = 0; i < mStickerList.size(); i++) {
            BaseSticker sticker = mStickerList.get(i);
            if (null != sticker) {
                transformSticker(sticker);
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (null != mHandlingSticker) {
            // 请求父容器不要拦截子View触摸事件
            getParent().requestDisallowInterceptTouchEvent(true);
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (locked) {
            return super.onInterceptTouchEvent(ev);
        }
        // 按下手势
        if (MotionEvent.ACTION_DOWN == ev.getAction()) {
            // 用户按下手势时相对于容器左边界的距离
            downX = ev.getX();
            // 用户按下手势时相对于容器上边界的距离
            downY = ev.getY();
            // 返回true拦截,返回false不拦截,系统默认返回false
            return null != findCurrentIconTouched() || null != findHandlingSticker();
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    @SuppressLint("ClickableViewAccessibility")
    public boolean onTouchEvent(MotionEvent event) {
        if (locked) {
            return super.onTouchEvent(event);
        }

        int action = MotionEventCompat.getActionMasked(event);
        if (MotionEvent.ACTION_DOWN == action) {
            // 当屏幕检测到第一个触点按下之后就会触发到这个事件
            if (!onTouchDown(event)) {
                return false;
            }
        } else if (MotionEvent.ACTION_POINTER_DOWN == action) {
            // 当屏幕上已经有触点处于按下的状态的时候,再有新的触点被按下时触发

            // 计算中间点
            mMidPoint = calculateMidPoint(event);
            // 计算两个手指距离
            oldDistance = calculateDistance(event);
            // 计算旋转角度
            oldRotation = calculateRotation(event);
            // 查找已触摸的"贴纸"四个方向的Icon图标 && 当前触摸的位置是否在贴纸view区域
            if (null != mHandlingSticker && null == findCurrentIconTouched() &&
                    isInStickerArea(mHandlingSticker, event.getX(1), event.getY(1))) {
                mCurrentMode = ActionMode.ZOOM_WITH_TWO_FINGER;
            }
        } else if (MotionEvent.ACTION_MOVE == action) {
            // 当触点在屏幕上移动时触发
            handleCurrentMode(event);
            invalidate();
        } else if (MotionEvent.ACTION_POINTER_UP == action) {
            // 当屏幕上有多个点被按住,松开其中一个点时触发(即非最后一个点被放开时)触发
            if (mCurrentMode == ActionMode.ZOOM_WITH_TWO_FINGER && mHandlingSticker != null) {
                if (mOnStickerOperationListener != null) {
                    mOnStickerOperationListener.onStickerZoomFinished(mHandlingSticker);
                }
            }
            mCurrentMode = ActionMode.NONE;
        } else if (MotionEvent.ACTION_UP == action) {
            // 当触点松开时被触发
            onTouchUp(event);
        }

        return true;
    }

    /*----------*/

    /**
     * 获取贴纸Sticker的位置
     *
     * @param sticker 贴纸Sticker对象
     * @param dst     存取Sticker范围点的坐标的对象
     * @return StickerView范围点的坐标
     */
    public float[] getStickerPoints(@Nullable BaseSticker sticker, @NonNull float[] dst) {
        if (null == sticker) {
            Arrays.fill(dst, 0);
            return null;
        }
        // 获取View范围点的坐标
        sticker.getBoundPoints(mBounds);
        sticker.getMappedPoints(dst, mBounds);
        return mBitmapPoints;
    }

    /**
     * 计算旋转角度
     *
     * @param event 触屏事件
     */
    protected float calculateRotation(@Nullable MotionEvent event) {
        if (null == event || event.getPointerCount() < 2) {
            // 触控点的个数小于2个,说明不是两个手指
            return 0f;
        }
        return calculateRotation(event.getX(0), event.getY(0), event.getX(1), event.getY(1));
    }

    /**
     * 根据两个点的坐标计算旋转角度
     *
     * @param x1 第一个点X轴坐标
     * @param y1 第一个点Y轴坐标
     * @param x2 第二个点X轴坐标
     * @param y2 第二个点Y轴坐标
     * @return 旋转角度
     */
    protected float calculateRotation(float x1, float y1, float x2, float y2) {
        double x = x1 - x2;
        double y = y1 - y2;
        // 求出俩边之间夹角的度数
        double radians = Math.atan2(y, x);
        // 方法用于将参数转化为角度
        return (float) Math.toDegrees(radians);
    }

    /**
     * 计算两个手指距离
     *
     * @param event 触屏事件
     */
    protected float calculateDistance(@Nullable MotionEvent event) {
        if (null == event || event.getPointerCount() < 2) {
            // 触控点的个数小于2个,说明不是两个手指
            return 0f;
        }
        return calculateDistance(event.getX(0), event.getY(0), event.getX(1), event.getY(1));
    }

    /**
     * 根据两个点的坐标计算两个手指距离
     *
     * @param x1 第一个点X轴坐标
     * @param y1 第一个点Y轴坐标
     * @param x2 第二个点X轴坐标
     * @param y2 第二个点Y轴坐标
     */
    protected float calculateDistance(float x1, float y1, float x2, float y2) {
        double x = x1 - x2;
        double y = y1 - y2;
        return (float) Math.sqrt(x * x + y * y);
    }

    /**
     * 根据两个点的坐标计算中间点
     *
     * @param event 触屏事件
     * @return 二个点的坐标的中间点
     */
    @NonNull
    protected PointF calculateMidPoint(@Nullable MotionEvent event) {
        if (null == event || event.getPointerCount() < 2) {
            // 触控点的个数小于2个,说明不是两个手指
            mMidPoint.set(0, 0);
            return mMidPoint;
        }
        float x = (event.getX(0) + event.getX(1)) / 2;
        float y = (event.getY(0) + event.getY(1)) / 2;
        mMidPoint.set(x, y);
        return mMidPoint;
    }

    /**
     * 计算中间点
     *
     * @return 中间点坐标值
     */
    @NonNull
    protected PointF calculateMidPoint() {
        if (null == mHandlingSticker) {
            mMidPoint.set(0, 0);
            return mMidPoint;
        }
        mHandlingSticker.getMappedCenterPoint(mMidPoint, mPoint, mTemp);
        return mMidPoint;
    }

    /*----------*/

    /**
     * 配置Icon的矩阵
     *
     * @param icon     "贴纸"四个方向的Icon图标
     * @param x        绘制圆的圆心X的坐标
     * @param y        绘制圆的圆心Y的坐标
     * @param rotation 旋转角度
     */
    protected void configIconMatrix(@NonNull BitmapStickerIcon icon, float x, float y, float rotation) {
        icon.setX(x);
        icon.setY(y);
        icon.getMatrix().reset();
        icon.getMatrix().postRotate(rotation, icon.getWidth() / 2, icon.getHeight() / 2);
        icon.getMatrix().postTranslate(x - icon.getWidth() / 2, y - icon.getHeight() / 2);
    }

    /**
     * 根据用户手指按下去的坐标点,查找已触摸的"贴纸"四个方向的某一个Icon图标
     *
     * @return 触摸"贴纸"Icon图标
     */
    @Nullable
    protected BitmapStickerIcon findCurrentIconTouched() {
        for (BitmapStickerIcon icon : mIconList) {
            // 绘制圆的圆心X的坐标 - 用户按下手势时相对于容器的X位置坐标 = 距离
            float x = icon.getX() - downX;
            float y = icon.getY() - downY;
            // 获取用户按下手势时的位置相对容器的距离(直角三角形斜边^2 = 邻边a^2 +邻边b^2)
            float distance = x * x + y * y;
            if (distance <= Math.pow(icon.getIconRadius() + icon.getIconRadius(), 2)) {
                return icon;
            }
        }
        return null;
    }

    /**
     * 根据用户手指按下去的坐标点,找到正在操作的贴纸Sticker
     *
     * @return 操作的贴纸Sticker
     */
    @Nullable
    protected BaseSticker findHandlingSticker() {
        for (int i = mStickerList.size() - 1; i >= 0; i--) {
            if (isInStickerArea(mStickerList.get(i), downX, downY)) {
                return mStickerList.get(i);
            }
        }
        return null;
    }

    /**
     * 当前触摸的位置是否在贴纸view区域
     *
     * @param sticker 贴纸Sticker对象
     * @param downX   用户按下手势时相对于容器的X位置坐标
     * @param downY   用户按下手势时相对于容器的Y位置坐标
     * @return 触摸的位置是否在贴纸view区域
     */
    protected boolean isInStickerArea(@NonNull BaseSticker sticker, float downX, float downY) {
        mTemp[0] = downX;
        mTemp[1] = downY;

        return sticker.contains(mTemp);
    }

    /*----------*/

    /**
     * @param sticker
     */
    protected void transformSticker(@Nullable BaseSticker sticker) {
        if (null == sticker) {
            Log.e(TAG, "转换贴图:位图贴图为空或位图贴图为空");
            return;
        }
        // 重新设定矩阵
        mSizeMatrix.reset();

        // 返回视图的宽度
        float width = getWidth();
        // 返回视图的高度
        float height = getHeight();
        float stickerWidth = sticker.getWidth();
        float stickerHeight = sticker.getHeight();
        float offsetX = (width - stickerWidth) / 2;
        float offsetY = (height - stickerHeight) / 2;
        mSizeMatrix.postTranslate(offsetX, offsetY);

        float scaleFactor;
        if (width < height) {
            scaleFactor = width / stickerWidth;
        } else {
            scaleFactor = height / stickerHeight;
        }
        mSizeMatrix.postScale(scaleFactor / 2f, scaleFactor / 2f, width / 2f, height / 2f);
        sticker.getMatrix().reset();
        sticker.setMatrix(mSizeMatrix);
        invalidate();
    }

    /**
     * 当屏幕检测到第一个触点按下之后就会触发到这个事件
     *
     * @param event 触屏事件
     * @return
     */
    protected boolean onTouchDown(@NonNull MotionEvent event) {
        // 拖曳View
        mCurrentMode = ActionMode.DRAG;

        downX = event.getX();
        downY = event.getY();

        // 计算中间点
        mMidPoint = calculateMidPoint();
        // 根据两个点的坐标计算两个手指距离
        oldDistance = calculateDistance(mMidPoint.x, mMidPoint.y, downX, downY);
        // 根据两个点的坐标计算旋转角度
        oldRotation = calculateRotation(mMidPoint.x, mMidPoint.y, downX, downY);

        // 根据用户手指按下去的坐标点,查找已触摸的"贴纸"四个方向的某一个Icon图标
        mCurrentIcon = findCurrentIconTouched();
        if (null != mCurrentIcon) {
            // 用户在操作Icon图标
            mCurrentMode = ActionMode.ICON;
            mCurrentIcon.onActionDown(this, event);
        } else {
            // 用户在操作"贴纸View"
            mHandlingSticker = findHandlingSticker();
        }

        if (null != mHandlingSticker) {
            mDownMatrix.set(mHandlingSticker.getMatrix());
            if (bringToFrontCurrentSticker) {
                mStickerList.remove(mHandlingSticker);
                mStickerList.add(mHandlingSticker);
            }
            if (null != mOnStickerOperationListener) {
                mOnStickerOperationListener.onStickerTouchedDown(mHandlingSticker);
            }
        }

        // TODO 增加"贴纸View"是否被选中状态侦听器
        if (null != mStickerViewSelectedListener) {
            if (null != mHandlingSticker) {
                mStickerViewSelectedListener.onSelectedStickerView(mHandlingSticker);

            } else {
                mStickerViewSelectedListener.onUnselectedStickerView();
            }
        }

        // TODO 取消点击屏幕空白处依旧可以选中最后一个操作的贴纸的功能
        /*if (null == mCurrentIcon && null == mHandlingSticker) {
            return false;
        }*/

        // TODO 增加点击屏幕空白处,取消选中最后一个操作的贴纸的功能
        if (null == mCurrentIcon && null == mHandlingSticker) {
            invalidate();
            return false;
        }

        if (null != mStickerViewEvent && null != mHandlingSticker) {
            mStickerViewEvent.onActionDown(this, getStickerPoints(mHandlingSticker, mBitmapPoints));
        }

        invalidate();
        return true;
    }

    /**
     * 当触点在屏幕上移动时触发处理各种手势操作
     *
     * @param event 触屏事件
     */
    protected void handleCurrentMode(@NonNull MotionEvent event) {
        if (ActionMode.NONE == mCurrentMode) {

        } else if (ActionMode.DRAG == mCurrentMode) {
            if (null != mHandlingSticker) {
                mMoveMatrix.set(mDownMatrix);
                mMoveMatrix.postTranslate(event.getX() - downX, event.getY() - downY);
                mHandlingSticker.setMatrix(mMoveMatrix);
                if (constrained) {
                    constrainSticker(mHandlingSticker);
                }
            }
        } else if (ActionMode.CLICK == mCurrentMode) {

        } else if (ActionMode.ZOOM_WITH_TWO_FINGER == mCurrentMode) {
            if (null != mHandlingSticker) {
                float newDistance = calculateDistance(event);
                float newRotation = calculateRotation(event);

                mMoveMatrix.set(mDownMatrix);
                mMoveMatrix.postScale(newDistance / oldDistance, newDistance / oldDistance, mMidPoint.x, mMidPoint.y);
                // TODO 取消图片旋转
                /* mMoveMatrix.postRotate(newRotation - oldRotation, mMidPoint.x, mMidPoint.y); */
                mHandlingSticker.setMatrix(mMoveMatrix);
            }
        } else if (ActionMode.ICON == mCurrentMode) {
            if (null != mHandlingSticker && null != mCurrentIcon) {
                mCurrentIcon.onActionMove(this, event);
            }
        }

        if (null != mStickerViewEvent && null != mHandlingSticker) {
            mStickerViewEvent.onActionMove(this, getStickerPoints(mHandlingSticker, mBitmapPoints));
        }
    }

    protected void onTouchUp(@NonNull MotionEvent event) {
        long currentTime = SystemClock.uptimeMillis();
        if (mCurrentMode == ActionMode.ICON && mCurrentIcon != null && mHandlingSticker != null) {
            mCurrentIcon.onActionUp(this, event);
        }

        if (mCurrentMode == ActionMode.DRAG
                && Math.abs(event.getX() - downX) < touchSlop
                && Math.abs(event.getY() - downY) < touchSlop
                && mHandlingSticker != null) {
            mCurrentMode = ActionMode.CLICK;
            if (mOnStickerOperationListener != null) {
                mOnStickerOperationListener.onStickerClicked(mHandlingSticker);
            }
            if (currentTime - lastClickTime < minClickDelayTime) {
                if (mOnStickerOperationListener != null) {
                    mOnStickerOperationListener.onStickerDoubleTapped(mHandlingSticker);
                }
            }
        }

        if (mCurrentMode == ActionMode.DRAG && mHandlingSticker != null) {
            if (mOnStickerOperationListener != null) {
                mOnStickerOperationListener.onStickerDragFinished(mHandlingSticker);
            }
        }

        if (null != mStickerViewEvent && null != mHandlingSticker) {
            mStickerViewEvent.onActionUp(this, getStickerPoints(mHandlingSticker, mBitmapPoints));
        }

        mCurrentMode = ActionMode.NONE;
        lastClickTime = currentTime;
    }

    /**
     * 限制贴纸
     *
     * @param sticker 要限制的贴纸
     */
    protected void constrainSticker(@NonNull BaseSticker sticker) {
        float moveX = 0;
        float moveY = 0;
        int width = getWidth();
        int height = getHeight();
        sticker.getMappedCenterPoint(mCurrentCenterPoint, mPoint, mTemp);
        if (mCurrentCenterPoint.x < 0) {
            moveX = -mCurrentCenterPoint.x;
        }
        if (mCurrentCenterPoint.x > width) {
            moveX = width - mCurrentCenterPoint.x;
        }
        if (mCurrentCenterPoint.y < 0) {
            moveY = -mCurrentCenterPoint.y;
        }
        if (mCurrentCenterPoint.y > height) {
            moveY = height - mCurrentCenterPoint.y;
        }
        sticker.getMatrix().postTranslate(moveX, moveY);
    }

    /*----------*/

    /**
     * 移除当前正在操作的Sticker
     *
     * @return true 成功,false 失败
     */
    public boolean removeCurrentSticker() {
        return remove(mHandlingSticker);
    }

    /**
     * 移除Sticker
     *
     * @return true 成功,false 失败
     */
    public boolean remove(@Nullable BaseSticker sticker) {
        if (mStickerList.contains(sticker)) {
            mStickerList.remove(sticker);
            if (null != mOnStickerOperationListener) {
                mOnStickerOperationListener.onStickerDeleted(sticker);
            }
            if (mHandlingSticker == sticker) {
                mHandlingSticker = null;
            }
            invalidate();
            return true;
        } else {
            Log.e(TAG, "删除失败,Sticker不存在");
            return false;
        }
    }

    /**
     * 移除全部Sticker
     */
    public void removeAllStickers() {
        mStickerList.clear();
        if (null != mHandlingSticker) {
            mHandlingSticker.release();
            mHandlingSticker = null;
        }
        invalidate();
    }

    /*----------*/

    /**
     * 缩放和旋转当前贴纸
     *
     * @param event 触屏事件
     */
    public void zoomAndRotateCurrentSticker(@NonNull MotionEvent event) {
        zoomAndRotateSticker(mHandlingSticker, event);
    }

    /**
     * 缩放和旋转当前贴纸
     *
     * @param sticker 当前正在操作的Sticker
     * @param event   触屏事件
     */
    public void zoomAndRotateSticker(@Nullable BaseSticker sticker, @NonNull MotionEvent event) {
        if (null != sticker) {
            float newDistance = calculateDistance(mMidPoint.x, mMidPoint.y, event.getX(), event.getY());
            float newRotation = calculateRotation(mMidPoint.x, mMidPoint.y, event.getX(), event.getY());

            mMoveMatrix.set(mDownMatrix);
            mMoveMatrix.postScale(newDistance / oldDistance, newDistance / oldDistance, mMidPoint.x, mMidPoint.y);
            // TODO 取消图片旋转
            /* mMoveMatrix.postRotate(newRotation - oldRotation, mMidPoint.x, mMidPoint.y); */
            mHandlingSticker.setMatrix(mMoveMatrix);
        }
    }

    /*----------*/

    /**
     * 翻转当前贴纸
     *
     * @param direction 翻转方向
     */
    public void flipCurrentSticker(int direction) {
        flip(mHandlingSticker, direction);
    }

    /**
     * 翻转当前贴纸
     *
     * @param sticker   当前正在操作的Sticker
     * @param direction 翻转方向
     */
    public void flip(@Nullable BaseSticker sticker, @Flip int direction) {
        if (null != sticker) {
            sticker.getCenterPoint(mMidPoint);
            if ((direction & FLIP_HORIZONTALLY) > 0) {
                sticker.getMatrix().preScale(-1, 1, mMidPoint.x, mMidPoint.y);
                sticker.setFlippedHorizontally(!sticker.isFlippedHorizontally());
            }
            if ((direction & FLIP_VERTICALLY) > 0) {
                sticker.getMatrix().preScale(1, -1, mMidPoint.x, mMidPoint.y);
                sticker.setFlippedVertically(!sticker.isFlippedVertically());
            }
            if (null != mOnStickerOperationListener) {
                mOnStickerOperationListener.onStickerFlipped(sticker);
            }
            invalidate();
        }
    }

    /*----------*/

    /**
     * 添加"贴纸"
     *
     * @param sticker "贴纸"
     * @return 添加的"贴纸"
     */
    @NonNull
    public StickerView addSticker(@NonNull BaseSticker sticker) {
        return addSticker(sticker, BaseSticker.Position.CENTER);
    }

    /**
     * 添加"贴纸"
     *
     * @param sticker  "贴纸"
     * @param position 添加"贴纸"的位置
     * @return 添加的"贴纸"
     */
    private StickerView addSticker(@NonNull final BaseSticker sticker, final @BaseSticker.Position int position) {
        if (ViewCompat.isLaidOut(this)) {
            addStickerImmediately(sticker, position);
        } else {
            post(() -> addStickerImmediately(sticker, position));
        }
        return this;
    }

    /**
     * 添加"贴纸"
     *
     * @param sticker "贴纸"
     * @param x       贴纸左上角距离屏幕左上角X轴的距离
     * @param y       贴纸左上角距离屏幕左上角Y轴的距离
     * @param xScale  X轴的缩放比例
     * @param yScale  Y轴的缩放比例
     * @return 添加的"贴纸"
     */
    @NonNull
    public StickerView addSticker(@NonNull BaseSticker sticker, float x, float y, double xScale, double yScale) {
        if (ViewCompat.isLaidOut(this)) {
            if (0 == x && 0 == y) {
                // 一开始默认添加在屏幕正中间
                addStickerImmediately(sticker, BaseSticker.Position.CENTER);
            } else {
                addStickerImmediately(null, sticker, x, y, xScale, yScale);
            }
        } else {
            if (0 == x && 0 == y) {
                // 一开始默认添加在屏幕正中间
                addStickerImmediately(sticker, BaseSticker.Position.CENTER);
            } else {
                post(() -> addStickerImmediately(null, sticker, x, y, xScale, yScale));
            }
        }
        return this;
    }

    /**
     * 添加"贴纸"
     *
     * @param sticker      "贴纸"
     * @param x            贴纸左上角距离屏幕左上角X轴的距离
     * @param y            贴纸左上角距离屏幕左上角Y轴的距离
     * @param xScale       X轴的缩放比例
     * @param yScale       Y轴的缩放比例
     * @param tag          "贴纸"附带保存的数据
     * @param sign         "贴纸"附带保存的数据
     * @param pottedName   盆栽的名称
     * @param pottedHeight 盆栽的高度
     * @return 添加的"贴纸"
     */
    @NonNull
    public StickerView addSticker(SimulationData simulationData, @NonNull BaseSticker sticker, float x, float y, double xScale, double yScale,
                                  Object tag, Object sign, String pottedName, String pottedHeight) {
        sticker.setTag(tag);
        sticker.setSign(sign);
        sticker.setPottedName(pottedName);
        sticker.setPottedHeight(pottedHeight);
        sticker.setProductAndaugmentedSkuId(String.valueOf(sign) + String.valueOf(simulationData.getTimeStamp()));
        if (ViewCompat.isLaidOut(this)) {
            if (0 == x && 0 == y) {
                // 一开始默认添加在屏幕正中间
                addStickerImmediately(sticker, BaseSticker.Position.CENTER);
            } else {
                addStickerImmediately(simulationData, sticker, x, y, xScale, yScale);
            }
        } else {
            if (0 == x && 0 == y) {
                // 一开始默认添加在屏幕正中间
                addStickerImmediately(sticker, BaseSticker.Position.CENTER);
            } else {
                post(() -> addStickerImmediately(simulationData, sticker, x, y, xScale, yScale));
            }
        }
        return this;
    }

    /**
     * 立即添加"贴纸"
     *
     * @param sticker  "贴纸"
     * @param position 添加"贴纸"的位置
     */
    protected void addStickerImmediately(@NonNull BaseSticker sticker, @BaseSticker.Position int position) {
        setStickerPosition(sticker, position);
        // float widthScaleFactor, heightScaleFactor, scaleFactor;
        // widthScaleFactor = (float) getWidth() / sticker.getDrawable().getIntrinsicWidth();
        // heightScaleFactor = (float) getHeight() / sticker.getDrawable().getIntrinsicHeight();
        // scaleFactor = widthScaleFactor > heightScaleFactor ? heightScaleFactor : widthScaleFactor;
        // sticker.getMatrix().postScale(scaleFactor / 2, scaleFactor / 2, getWidth() / 2, getHeight() / 2);
        mHandlingSticker = sticker;
        mStickerList.add(sticker);
        if (null != mOnStickerOperationListener) {
            mOnStickerOperationListener.onStickerAdded(sticker);
        }
        invalidate();

        // TODO 在一开始添加"贴纸"的时候因为位置为0.0,所以调用了该方法,但是没有获取位置,所以在这里添加该方法来获取位置,为后续负责这个位置的"贴纸"通过位置参数
        new Handler().postDelayed(() -> {
            if (null != mStickerViewEvent && null != mHandlingSticker) {
                mStickerViewEvent.onActionUp(this, getStickerPoints(mHandlingSticker, mBitmapPoints));
            }
        }, 100);
    }

    /**
     * 立即添加"贴纸"
     *
     * @param sticker "贴纸"
     * @param x       贴纸左上角距离屏幕左上角X轴的距离
     * @param y       贴纸左上角距离屏幕左上角Y轴的距离
     * @param xScale  X轴的缩放比例
     * @param yScale  Y轴的缩放比例
     */
    protected void addStickerImmediately(SimulationData simulationData, @NonNull BaseSticker sticker,
                                         float x, float y, double xScale, double yScale) {
        LogUtil.e(TAG, "贴纸左上角距离屏幕左上角X轴的距离: " + x);
        LogUtil.e(TAG, "贴纸左上角距离屏幕左上角Y轴的距离: " + y);
        LogUtil.e(TAG, "X轴的缩放比例: " + xScale);
        LogUtil.e(TAG, "Y轴的缩放比例: " + yScale);
//        Drawable drawable = sticker.getDrawable();
//        float scaleFactorWidth = (float) (simulationData.getWidth() / drawable.getIntrinsicWidth());
//        float scaleFactorHeight = (float) (simulationData.getHeight() / drawable.getIntrinsicHeight());
//
//        sticker.getMatrix().setTranslate(x, y);
//        sticker.getMatrix().postScale((float) scaleFactorWidth, (float) scaleFactorHeight, x, y);
//        sticker.getMatrix().postScale((float) xScale, (float) yScale, x, y);
//        double currentStickerWidth = simulationData.getWidth();
//        double currentStickerHeight = simulationData.getHeight();
//        float currentStickerX = x;
//        if (currentStickerWidth > currentStickerHeight) {
//        } else {
//            currentStickerX = (float) (x - ((currentStickerHeight - currentStickerWidth) / 2));
//        }

//        sticker.getMatrix().postScale((float) xScale, (float) yScale, x, x);
        sticker.getMatrix().postScale((float) xScale/2, (float) yScale/2, 0, 0);


        setStickerPosition(sticker, x, y);
        /**
         * https://blog.csdn.net/maxchenfuhai/article/details/51690857
         *
         * float sx: X轴的缩放大小
         * float sy: Y轴的缩放大小
         * float px: X轴缩放中心点
         * float py: Y轴缩放中心点
         */
//        sticker.getMatrix().postScale((float) xScale, (float) yScale, x, y);

//        sticker.getMatrix().postTranslate(x, y);

        mHandlingSticker = sticker;
        mStickerList.add(sticker);


        if (null != mOnStickerOperationListener) {
            mOnStickerOperationListener.onStickerAdded(sticker);
        }
        invalidate();
    }

    /**
     * 设置"贴纸"的位置
     *
     * @param sticker  "贴纸"
     * @param position 添加"贴纸"的位置
     */
    protected void setStickerPosition(@NonNull BaseSticker sticker, @BaseSticker.Position int position) {
        float width = getWidth();
        float height = getHeight();
        float offsetX = width - sticker.getWidth();
        float offsetY = height - sticker.getHeight();

        if ((position & BaseSticker.Position.TOP) > 0) {
            offsetY /= 4f;
        } else if ((position & BaseSticker.Position.BOTTOM) > 0) {
            offsetY *= 3f / 4f;
        } else {
            offsetY /= 2f;
        }
        if ((position & BaseSticker.Position.LEFT) > 0) {
            offsetX /= 4f;
        } else if ((position & BaseSticker.Position.RIGHT) > 0) {
            offsetX *= 3f / 4f;
        } else {
            offsetX /= 2f;
        }
        sticker.getMatrix().postTranslate(offsetX, offsetY);
    }

    /**
     * 设置"贴纸"的位置
     *
     * @param sticker "贴纸"
     * @param x       贴纸左上角距离屏幕左上角X轴的距离
     * @param y       贴纸左上角距离屏幕左上角Y轴的距离
     */
    protected void setStickerPosition(@NonNull BaseSticker sticker, float x, float y) {
        sticker.getMatrix().postTranslate(x, y);
    }

    /*----------*/

    /**
     * 获取当前正在操作的贴纸
     *
     * @return 当前正在操作的贴纸
     */
    @Nullable
    public BaseSticker getCurrentSticker() {
        return mHandlingSticker;
    }

    /**
     * 获取全部的贴纸
     *
     * @return 全部的贴纸
     */
    public List<BaseSticker> getAllStickerList() {
        return mStickerList;
    }

    /**
     * 添加贴纸"四个方向的Icon图标
     *
     * @param icons 四个方向的Icon图标
     */
    public void setIcons(@NonNull List<BitmapStickerIcon> icons) {
        this.mIconList.clear();
        this.mIconList.addAll(icons);
        invalidate();
    }

    /**
     * 保存贴纸
     *
     * @param name 贴纸保存后的路径名称
     */
    public String saveSticker(@NonNull String name) {
        String pathName = SDCardHelperUtil.getSDCardPrivateFilesDir(YiBaiApplication.getContext(), DIRECTORY_PICTURES) + "/" + name + ".jpg";
        File file = new File(pathName);
        try {
            File saveImageFile = StickerUtils.saveImage(file, createBitmap());
            return saveImageFile.getAbsolutePath();
        } catch (IllegalArgumentException | IllegalStateException ignored) {
            ignored.fillInStackTrace();
            return null;
        }
    }

    /**
     * 保存贴纸
     *
     * @param name 贴纸保存后的路径名称
     */
    public String saveSticker(View v,View bg,@NonNull String name) {
        String pathName = SDCardHelperUtil.getSDCardPrivateFilesDir(YiBaiApplication.getContext(), DIRECTORY_PICTURES) + "/" + name + ".jpg";
        File file = new File(pathName);
        try {
            Bitmap bitmap = viewConversionBitmap(bg,v);
            File saveImageFile = StickerUtils.saveImage(file, bitmap);
            return saveImageFile.getAbsolutePath();
        } catch (IllegalArgumentException | IllegalStateException ignored) {
            ignored.fillInStackTrace();
            return null;
        }
    }

    /**
     * view转bitmap
     */
    public Bitmap viewConversionBitmap(View bg,View v) {
        int w = v.getWidth();
        int h = v.getHeight();

        Bitmap bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmp);
        Bitmap bitmap = loadBitmapFromView(bg);

        c.drawBitmap(bitmap,0,0,null);

        v.layout(0, 0, w, h);
        v.draw(c);

        return bmp;
    }

    public Bitmap loadBitmapFromView(View v) {
        int w = v.getWidth();
        int h = v.getHeight();

        Bitmap bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmp);

        c.drawColor(Color.WHITE);
        /** 如果不设置canvas画布为白色，则生成透明 */
        v.draw(c);
        return bmp;
    }

    /**
     * 保存贴纸
     *
     * @param file 保存贴纸的位置
     */
    public void saveSticker(@NonNull File file) {
        try {
            StickerUtils.saveImage(file, createBitmap());
        } catch (IllegalArgumentException | IllegalStateException ignored) {
            ignored.fillInStackTrace();
        }
    }

    @NonNull
    public Bitmap createBitmap() throws OutOfMemoryError {
        mHandlingSticker = null;
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        this.draw(canvas);
        return bitmap;
    }

    /**
     * 获取是否锁定贴纸
     *
     * @return true 锁定
     */
    public boolean isLocked() {
        return locked;
    }

    @NonNull
    public StickerView setLocked(boolean locked) {
        this.locked = locked;
        invalidate();
        return this;
    }

    public boolean isConstrained() {
        return constrained;
    }

    @NonNull
    public StickerView setConstrained(boolean constrained) {
        this.constrained = constrained;
        postInvalidate();
        return this;
    }

    @Nullable
    public OnStickerOperationListener getOnStickerOperationListener() {
        return mOnStickerOperationListener;
    }

    @NonNull
    public StickerView setOnStickerOperationListener(@Nullable OnStickerOperationListener onStickerOperationListener) {
        this.mOnStickerOperationListener = onStickerOperationListener;
        return this;
    }

    public void setStickerViewEvent(StickerViewEvent stickerViewEvent) {
        this.mStickerViewEvent = stickerViewEvent;
    }

    public void setStickerViewSelectedListener(StickerViewSelectedListener stickerViewSelectedListener) {
        this.mStickerViewSelectedListener = stickerViewSelectedListener;
    }
}
