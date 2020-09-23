package com.ybw.yibai.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.ybw.yibai.R;
import com.ybw.yibai.common.utils.DensityUtil;

import static com.ybw.yibai.common.utils.ImageUtil.drawableToBitmap;

/**
 * 圆角ImageView
 *
 * @author sjl
 */
public class RoundImageView extends AppCompatImageView {

    /**
     * 圆角大小
     */
    private int angle;

    /**
     * 画笔对象
     */
    private Paint mPaint;

    /**
     * 3x3 矩阵,主要用于缩小放大
     */
    private Matrix mMatrix;

    /**
     * 渲染图像,使用图像为绘制图形着色
     */
    private BitmapShader mBitmapShader;

    public RoundImageView(Context context) {
        this(context, null);
        init(context, null);
    }

    public RoundImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        init(context, attrs);
    }

    public RoundImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        // 先对自定义的属性进行处理.只有当前控件是在布局文件中使用的时候,attrs才不是null
        if (attrs != null) {
            // 获取由自定义属性组成的数组
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RoundImageView);
            angle = typedArray.getInteger(R.styleable.RoundImageView_angles, DensityUtil.dpToPx(context, 2));
            // 回收释放
            typedArray.recycle();
        }

        mMatrix = new Matrix();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (getDrawable() == null) {
            return;
        }
        Bitmap bitmap = drawableToBitmap(getDrawable());
        if (null == mBitmapShader) {
            mBitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        }
        float scale = 1.0f;
        if (!(bitmap.getWidth() == getWidth() && bitmap.getHeight() == getHeight())) {
            // 如果图片的宽或者高与view的宽高不匹配,计算出需要缩放的比例;缩放后的图片的宽高,一定要大于我们view的宽高;所以我们这里取大值;
            scale = Math.max(getWidth() * 1.0f / bitmap.getWidth(), getHeight() * 1.0f / bitmap.getHeight());
        }
        // shader的变换矩阵,我们这里主要用于放大或者缩小
        mMatrix.setScale(scale, scale);
        // 设置变换矩阵
        mBitmapShader.setLocalMatrix(mMatrix);
        // 设置shader
        mPaint.setShader(mBitmapShader);
        canvas.drawRoundRect(0, 0, getWidth(), getHeight(), angle, angle, mPaint);
    }
}