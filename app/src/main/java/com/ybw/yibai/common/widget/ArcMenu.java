package com.ybw.yibai.common.widget;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ybw.yibai.R;

import java.util.ArrayList;
import java.util.List;

import static com.ybw.yibai.common.utils.DensityUtil.dpToPx;

/**
 * 弧形菜单
 * https://github.com/linglongxin24/CircleMenu
 *
 * @author sjl
 */
public class ArcMenu extends FrameLayout implements View.OnClickListener {

    private int radius;

    private int viewSize;

    private int textSize;

    private RelativeLayout mChildRootView;

    private ImageView mMainImageView;

    private List<View> mChildViewList;

    private Context mContext;

    public ArcMenu(@NonNull Context context) {
        super(context);
        initView(context);
        initData();
        initEvent();
    }

    public ArcMenu(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
        initData();
        initEvent();
    }

    public ArcMenu(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
        initData();
        initEvent();
    }

    private void initView(Context context) {
        mContext = context;
        View view = View.inflate(getContext(), R.layout.ayc_menu_layout, this);
        mChildRootView = view.findViewById(R.id.childRootView);
        mMainImageView = view.findViewById(R.id.mainImageView);
        mMainImageView.setBackground(mContext.getDrawable(R.mipmap.menu_close));
    }

    private void initData() {
        mChildViewList = new ArrayList<>();
        radius = dpToPx(mContext, 70);
        viewSize = dpToPx(mContext, 44);
        textSize = dpToPx(mContext, 3);
    }

    private void initEvent() {
        mMainImageView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (arcMenuIsExpand()) {
            shrinkArcMenu();
        } else {
            expandArcMenu();
        }
    }

    /**
     * 添加子View
     *
     * @param text   子View显示的位置
     * @param object 子View附加的信息
     */
    public void addChildView(String text, Object object) {
        TextView textView = new TextView(mContext);
        textView.setText(text);
        textView.setTag(object);
        textView.setWidth(viewSize);
        textView.setHeight(viewSize);
        textView.setTextSize(textSize);
        textView.setTextColor(Color.WHITE);
        textView.setGravity(Gravity.CENTER);
        textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        textView.setBackground(ContextCompat.getDrawable(mContext, R.drawable.background_collocation));
        textView.setOnClickListener(v -> {
            if (arcMenuIsExpand()) {
                shrinkArcMenu();
            } else {
                expandArcMenu();
            }
            if (null != arcMenuStateChangeListener) {
                arcMenuStateChangeListener.onChildViewClick(v);
            }
        });
        mChildRootView.addView(textView);
        mChildViewList.add(textView);
    }

    /**
     * 显示圆形菜单
     */
    private void showCircleMenu() {
        mMainImageView.setBackground(mContext.getDrawable(R.mipmap.menu_open));
        for (int i = 0; i < mChildViewList.size(); i++) {
            PointF point = new PointF();
            int avgAngle = (175 / (mChildViewList.size() - 1));
            int angle = avgAngle * i + 95;
            point.x = (float) Math.cos(angle * (Math.PI / 180)) * radius;
            point.y = (float) Math.sin(angle * (Math.PI / 180)) * radius;
            ObjectAnimator objectAnimatorX = ObjectAnimator.ofFloat(mChildViewList.get(i), "translationX", 0, point.x);
            ObjectAnimator objectAnimatorY = ObjectAnimator.ofFloat(mChildViewList.get(i), "translationY", 0, point.y);
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.play(objectAnimatorX).with(objectAnimatorY);
            animatorSet.setDuration(200);
            animatorSet.start();
            animatorSet.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {

                }
            });
        }
        if (null != arcMenuStateChangeListener) {
            arcMenuStateChangeListener.onArcMenuShrink();
        }
    }

    /**
     * 关闭圆形菜单
     */
    private void closeCircleMenu() {
        mMainImageView.setBackground(mContext.getDrawable(R.mipmap.menu_close));
        for (int i = 0; i < mChildViewList.size(); i++) {
            PointF point = new PointF();
            int avgAngle = (175 / (mChildViewList.size() - 1));
            int angle = avgAngle * i + 95;
            point.x = (float) Math.cos(angle * (Math.PI / 180)) * radius;
            point.y = (float) Math.sin(angle * (Math.PI / 180)) * radius;
            ObjectAnimator objectAnimatorX = ObjectAnimator.ofFloat(mChildViewList.get(i), "translationX", point.x, 0);
            ObjectAnimator objectAnimatorY = ObjectAnimator.ofFloat(mChildViewList.get(i), "translationY", point.y, 0);
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.play(objectAnimatorX).with(objectAnimatorY);
            animatorSet.setDuration(200);
            animatorSet.start();
            animatorSet.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {

                }
            });
        }
        if (null != arcMenuStateChangeListener) {
            arcMenuStateChangeListener.onArcMenuExpand();
        }
    }

    public void show() {
        setVisibility(VISIBLE);
    }

    public void hide() {
        setVisibility(GONE);
        shrinkArcMenu();
    }

    /**
     * 获取ArcMenu状态
     *
     * @return true 打开状态/ false关闭状态
     */
    public boolean arcMenuIsExpand() {
        Boolean isShowing = (Boolean) mMainImageView.getTag();
        return null != isShowing && isShowing;
    }

    /**
     * 展开ArcMenu
     */
    public void expandArcMenu() {
        if (arcMenuIsExpand()) {
            return;
        }
        mMainImageView.setTag(true);
        showAllChildView();
        showCircleMenu();
    }

    /**
     * 收缩ArcMenu
     */
    public void shrinkArcMenu() {
        if (!arcMenuIsExpand()) {
            return;
        }
        mMainImageView.setTag(false);
        closeCircleMenu();
    }

    private void showAllChildView() {
        if (null == mChildViewList || mChildViewList.size() == 0) {
            return;
        }
        for (View view : mChildViewList) {
            if (VISIBLE == view.getVisibility()) {
                continue;
            }
            view.setVisibility(VISIBLE);
        }
    }

    private void hideAllChildView() {
        if (null == mChildViewList || mChildViewList.size() == 0) {
            return;
        }
        for (View view : mChildViewList) {
            if (GONE == view.getVisibility()) {
                continue;
            }
            view.setVisibility(GONE);
        }
    }

    /**
     * 1.定义一个接口
     * ArcMenu 各种状态侦听器
     */
    public interface ArcMenuStateListener {

        /**
         * 在ArcMenu展开时回调
         */
        void onArcMenuExpand();

        /**
         * 在ArcMenu收缩时回调
         */
        void onArcMenuShrink();

        /**
         * 在ArcMenu ChildView被点击时回调
         *
         * @param v 被点击的ChildView
         */
        void onChildViewClick(View v);
    }

    /**
     * 2.声明一个接口变量
     */
    private ArcMenuStateListener arcMenuStateChangeListener;

    /**
     * 3.初始化接口变量
     *
     * @param arcMenuStateChangeListener ArcMenu 各种状态侦听器
     */
    public void setArcMenuStateListener(ArcMenuStateListener arcMenuStateChangeListener) {
        this.arcMenuStateChangeListener = arcMenuStateChangeListener;
    }
}
