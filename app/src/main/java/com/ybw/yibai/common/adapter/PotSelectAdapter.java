package com.ybw.yibai.common.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ybw.yibai.R;
import com.ybw.yibai.common.bean.ListBean;
import com.ybw.yibai.common.utils.ImageUtil;
import com.ybw.yibai.module.collocation.CollocationActivity;

import java.util.List;

/**
 * 盆栽详情界面盆器选择适配器
 * {@link CollocationActivity}
 *
 * @author sjl
 */
public class PotSelectAdapter extends PagerAdapter {

    /**
     * 上下文对象
     */
    private Context mContext;

    /**
     * 盆器列表数据
     */
    private List<ListBean> mPotList;

    /**
     * true 单图模式/false 上下搭配
     */
    private boolean mSingle;

    public PotSelectAdapter(Context context, List<ListBean> potList) {
        mContext = context;
        mPotList = potList;
    }

    public PotSelectAdapter(Context context, List<ListBean> potList, boolean single) {
        mContext = context;
        mPotList = potList;
        mSingle = single;
    }

    /**
     * 该方法是计算页面的个数并返回页面的个数
     *
     * @return 页面的个数
     */
    @Override
    public int getCount() {
        return mPotList == null ? 0 : mPotList.size();
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
        ImageView imageView = new ImageView(mContext);
        imageView.setOnLongClickListener(v -> {
            // 4.在点击事件中调用接口里的方法
            if (null != onItemLongClickListener) {
                onItemLongClickListener.onPotViewPagerItemLongClick(position);
            }
            return false;
        });
//        imageView.setBackgroundResource(R.drawable.bg_borde_sr);
        ListBean listBean = mPotList.get(position);
        String pic2 = listBean.getPic2();
        String pic3 = listBean.getPic3();
        if (mSingle && !TextUtils.isEmpty(pic2)) {
            ImageUtil.displayImage(mContext, imageView, pic2);
        } else if (!TextUtils.isEmpty(pic3)) {
            ImageUtil.displayImage(mContext, imageView, pic3);
        }

        //把这个position位置要显示的View添加到ViewPager中
        container.addView(imageView);
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
        container.removeView((View) object);
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
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
    public interface OnItemLongClickListener {

        /**
         * 在ViewPager Item 长按时回调
         *
         * @param position 被长按的Item位置
         */
        void onPotViewPagerItemLongClick(int position);
    }

    /**
     * 2.声明一个接口变量
     */
    private OnItemLongClickListener onItemLongClickListener;

    /**
     * 3.初始化接口变量
     *
     * @param onItemLongClickListener ViewPager Item点击的侦听器
     */
    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }
}
