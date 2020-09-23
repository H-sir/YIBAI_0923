package com.ybw.yibai.common.classs;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.ybw.yibai.R;
import com.ybw.yibai.common.utils.DensityUtil;
import com.ybw.yibai.common.utils.LogUtil;

import static com.ybw.yibai.common.utils.AnimationUtil.zoomAnimation;

/**
 * 主界面中间PopupMenu
 *
 * @author sjl
 */
public class MainPopupMenu implements View.OnTouchListener {

    public static final String TAG = "MainPopupMenu";

    private int height;

    /**
     * 导航栏高度
     */
    private int navigationBarHeight;

    /**
     * 动画执行的 属性值数组
     */
    private float[] animatorProperty;

    /*
      /\_/\
    =( °w° )=
      )   (
     (__ __)
    */

    private View mRootVew;

    private PopupWindow mPopupWindow;

    private View mCloseView;

    private TextView mTakePhotoTextView;

    private TextView mAlbumTextView;

    private TextView mBackgroundTemplateTextView;

    private ImageView mCloseImageView;

    /*
      /\_/\
    =( °w° )=
      )   (
     (__ __)
    */

    public static MainPopupMenu getInstance() {
        return new MainPopupMenu();
    }

    private void createView(Context context) {
        mRootVew = LayoutInflater.from(context).inflate(R.layout.main_popup_menu_layout, null);
        mPopupWindow = new PopupWindow(mRootVew, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

        initView();
        initData(context);
        initEvent(context);
    }

    /**
     * 初始化View
     */
    private void initView() {
        mCloseView = mRootVew.findViewById(R.id.closeView);
        mTakePhotoTextView = mRootVew.findViewById(R.id.takePhotoTextView);
        mAlbumTextView = mRootVew.findViewById(R.id.albumTextView);
        mBackgroundTemplateTextView = mRootVew.findViewById(R.id.backgroundTemplateTextView);
        mCloseImageView = mRootVew.findViewById(R.id.closeImageView);

        // 因为设置了mPopupWindow.setClippingEnabled(false);允许弹出窗口超出屏幕范围,所以PopupWindow 会从屏幕最底部显示
        // 所以这里要设置一个View的高度和导航栏高度一样的View
        if (0 != navigationBarHeight) {
            View mView = mRootVew.findViewById(R.id.view);
            ViewGroup.LayoutParams layoutParams = mView.getLayoutParams();
            layoutParams.height = navigationBarHeight;
            mView.setLayoutParams(layoutParams);
        }
    }

    /**
     * 初始化数据
     *
     * @param context 上下文对象
     */
    private void initData(Context context) {
        height = DensityUtil.dpToPx(context, 185);
        int v = DensityUtil.dpToPx(context, 10);
        if (null == animatorProperty) {
            animatorProperty = new float[]{height, -v, 0};
        }
    }

    /**
     * 初始化事件
     *
     * @param context 上下文对象
     */
    @SuppressWarnings("all")
    private void initEvent(Context context) {
        // 设置为失去焦点,方便监听返回键的监听
        mPopupWindow.setFocusable(false);
        // 如果想要PopupWindow遮挡住状态栏可以加上这句代码
        mPopupWindow.setClippingEnabled(false);
        // PopupWindow点击它之外的地方不消失
        mPopupWindow.setOutsideTouchable(false);

        mTakePhotoTextView.setOnTouchListener(this);
        mAlbumTextView.setOnTouchListener(this);
        mBackgroundTemplateTextView.setOnTouchListener(this);

        mTakePhotoTextView.setOnClickListener(new MyViewClick(1, context));
        mAlbumTextView.setOnClickListener(new MyViewClick(2, context));
        mBackgroundTemplateTextView.setOnClickListener(new MyViewClick(3, context));
        mCloseImageView.setOnClickListener(new MyViewClick(0, context));

        mCloseView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closePopupWindow();
            }
        });
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            zoomAnimation(v, 1f, 1.2f);
        } else if (action == MotionEvent.ACTION_UP) {
            zoomAnimation(v, 1.2f, 1f);
        }
        return false;
    }

    /**
     * 点击事件
     */
    private class MyViewClick implements View.OnClickListener {

        int index;
        Context context;

        public MyViewClick(int index, Context context) {
            this.index = index;
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            LogUtil.e(TAG, "onClick");
            if (index == 0) {
                // 加号按钮点击之后的执行
                closePopupWindow();
            } else if (null != onItemClickListener) {
                // 4.在点击事件中调用接口里的方法
                onItemClickListener.onPopupWindowItemClick(v, index);
            }
        }
    }

    /**
     * 刚打开popupWindow 执行的动画
     */
    private void openPopupWindow() {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mCloseImageView, "rotation", 0f, 90f);
        objectAnimator.setDuration(500);
        objectAnimator.start();

        startAnimation(mTakePhotoTextView, 400, animatorProperty);
        startAnimation(mAlbumTextView, 500, animatorProperty);
        startAnimation(mBackgroundTemplateTextView, 400, animatorProperty);
    }

    /**
     * 关闭popupWindow执行的动画
     */
    public void closePopupWindow() {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mCloseImageView, "rotation", 90f, 0f);
        objectAnimator.setDuration(200);
        objectAnimator.start();

        closeAnimation(mTakePhotoTextView, 300, height);
        closeAnimation(mAlbumTextView, 100, height);
        closeAnimation(mBackgroundTemplateTextView, 300, height);

        mCloseImageView.postDelayed(new Runnable() {
            @Override
            public void run() {
                close();
            }
        }, 300);
    }

    /**
     * 启动动画
     *
     * @param view     view
     * @param duration 执行时长
     * @param distance 执行的轨迹数组
     */
    private void startAnimation(View view, int duration, float[] distance) {
        ObjectAnimator anim = ObjectAnimator.ofFloat(view, "translationY", distance);
        anim.setDuration(duration);
        anim.start();
    }

    /**
     * 关闭 popupWindow 时的动画
     *
     * @param view     view
     * @param duration 动画执行时长
     * @param values   平移量
     */
    private void closeAnimation(View view, int duration, int values) {
        ObjectAnimator anim = ObjectAnimator.ofFloat(view, "translationY", 0f, values);
        anim.setDuration(duration);
        anim.start();
    }

    /**
     * 弹起popupWindow
     *
     * @param context             上下文对象
     * @param parent              parent
     * @param navigationBarHeight 导航栏的高度
     */
    public void show(Context context, View parent, int navigationBarHeight) {
        this.navigationBarHeight = navigationBarHeight;
        if (null == mPopupWindow) {
            createView(context);
        }
        mPopupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
        openPopupWindow();
    }

    /**
     * 关闭popupWindow
     */
    public void close() {
        if (null != mPopupWindow && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
            mPopupWindow = null;
        }
    }

    /**
     * 判断popupWindow 是否显示了
     *
     * @return popupWindow 是否显示了
     */
    public boolean isShowing() {
        return null != mPopupWindow && mPopupWindow.isShowing();
    }

    /*
      /\_/\
    =( °w° )=
      )   (
     (__ __)
    */

    /**
     * 1.定义一个接口
     */
    public interface OnItemClickListener {

        /**
         * 在PopupWindow Item 点击时回调
         *
         * @param v        被点击的View
         * @param position 被点击的Item位置
         */
        void onPopupWindowItemClick(View v, int position);
    }

    /**
     * 2.声明一个接口变量
     */
    private OnItemClickListener onItemClickListener;

    /**
     * 3.初始化接口变量
     *
     * @param onItemClickListener RecyclerView Item点击的侦听器
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
