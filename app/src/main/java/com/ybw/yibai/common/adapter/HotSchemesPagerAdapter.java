package com.ybw.yibai.common.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ybw.yibai.R;
import com.ybw.yibai.common.bean.HotSchemes.DataBean.ListBean;
import com.ybw.yibai.common.utils.DensityUtil;
import com.ybw.yibai.common.utils.ImageUtil;
import com.ybw.yibai.module.home.HomeFragment;

import java.util.List;

/**
 * 首页显示新热门方案的适配器
 * <p>
 * {@link HomeFragment}
 *
 * @author sjl
 */
public class HotSchemesPagerAdapter extends PagerAdapter implements View.OnTouchListener {

    /**
     * 上下文对象
     */
    private Context mContext;

    /**
     * 热门方案列表
     */
    private List<ListBean> mHotSchemeList;

    public HotSchemesPagerAdapter(Context context, List<ListBean> hotSchemeList) {
        this.mContext = context;
        this.mHotSchemeList = hotSchemeList;
    }

    /**
     * 该方法是计算页面的个数并返回页面的个数
     *
     * @return 页面的个数
     */
    @Override
    public int getCount() {
        return (null == mHotSchemeList || 0 == mHotSchemeList.size()) ? 0 : Integer.MAX_VALUE;
    }

    /**
     * 该方法是判断第一个参数和第二个参数是否是同一个对象
     */
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    /**
     * 该方法是初始化每页的View对象,并把这个View对象添加到ViewPager中
     *
     * @param container ViewPager本身
     * @param position  每个Pager(页面)的position(位置)
     * @return 通常大部分情况，就是返回每个Pager显示的那个View对象
     */
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        // 重新获取position防止角标越界异常
        position = position % mHotSchemeList.size();
        ImageView imageView = new ImageView(mContext);
        imageView.setElevation(5f);
        String coverPic = mHotSchemeList.get(position).getCover_pic();
        imageView.setBackground(mContext.getDrawable(R.drawable.background_hot_scheme));
        ImageUtil.displayImage(mContext, imageView, coverPic, DensityUtil.dpToPx(mContext, 20));
        // 把这个position位置要显示的View添加到ViewPager中
        container.addView(imageView);

        int pos = position;
        imageView.setOnTouchListener(this);
        imageView.setOnClickListener(v -> {
            // 4.在点击事件中调用接口里的方法
            if (null != onViewPagerItemClickListener) {
                onViewPagerItemClickListener.onViewPagerItemClick(pos);
            }
        });

        return imageView;
    }

    /**
     * 该方法是把不在查看的Pager(页面)从ViewPager中删除
     *
     * @param container ViewPager本身
     * @param position  是想要删除的那个Pager(页面)的position(位置)
     * @param object    是想要删除的那个Pager(页面)个对象
     */
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        // 不要在这里调用removeView,因为已经在instantiateItem()方法中已经处理了removeView的逻辑
        // ,如果这里在加上了removeView的调用,则会出现ViewPager的内容为空的情况
        // container.removeView((View) object);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            // zoomAnimation(v, 1f, 0.98f);
        } else if (action == MotionEvent.ACTION_UP) {
            // zoomAnimation(v, 0.98f, 1f);
        }
        return false;
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
    public interface OnViewPagerItemClickListener {

        /**
         * 在ViewPager Item 点击时回调
         *
         * @param position 被点击的Item位置
         */
        void onViewPagerItemClick(int position);
    }

    /**
     * 2.声明一个接口变量
     */
    private OnViewPagerItemClickListener onViewPagerItemClickListener;

    /**
     * 3.初始化接口变量
     *
     * @param onViewPagerItemClickListener ViewPager Item点击的侦听器
     */
    public void setOnViewPagerItemClickListener(OnViewPagerItemClickListener onViewPagerItemClickListener) {
        this.onViewPagerItemClickListener = onViewPagerItemClickListener;
    }
}
