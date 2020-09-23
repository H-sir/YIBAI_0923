package com.ybw.yibai.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.ybw.yibai.R;
import com.ybw.yibai.common.utils.DensityUtil;

/**
 * 自定义圆角矩形
 *
 * @author sjl
 */
public class RectangleView extends View {

    /**
     * 圆角
     */
    private int angle;

    /**
     * 背景颜色
     */
    private int backgroundColor;

    /**
     * 画笔对象
     */
    private Paint mPaint;

    /**
     * 上下文对象
     */
    private Context mContext;

    public RectangleView(Context context) {
        super(context);
        init(context, null);
    }

    public RectangleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RectangleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mContext = context;
        // 先对自定义的属性进行处理.只有当前控件是在布局文件中使用的时候,attrs才不是null
        if (attrs != null) {
            // 获取由自定义属性组成的数组
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RectangleView);
            angle = typedArray.getInteger(R.styleable.RectangleView_angle, DensityUtil.dpToPx(context, 2));
            backgroundColor = typedArray.getColor(R.styleable.RectangleView_backgroundColor, Color.TRANSPARENT);
            // 回收释放
            typedArray.recycle();
        }
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(backgroundColor);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 默认的大小
        int mDefaultSize = DensityUtil.dpToPx(mContext, 30);

        // 分别获取测量模式 和 测量大小
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        // 如果是精确度模式,就按xml中定义的来
        // 如果是最大值模式,就按我们定义的来
        if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(mDefaultSize, mDefaultSize);
        } else if (widthMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(mDefaultSize, heightSize);
        } else if (heightMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSize, mDefaultSize);
        } else {
            setMeasuredDimension(widthSize, heightSize);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        canvas.drawRoundRect(0, 0, width, height, angle, angle, mPaint);
    }

    @Override
    public void setBackgroundColor(int backgroundColor) {
        mPaint.setColor(backgroundColor);
        this.backgroundColor = backgroundColor;
    }
}
