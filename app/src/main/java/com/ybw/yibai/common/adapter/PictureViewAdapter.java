package com.ybw.yibai.common.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.github.chrisbanes.photoview.PhotoView;
import com.ybw.yibai.common.utils.ImageUtil;
import com.ybw.yibai.module.pictureview.PictureViewActivity;

import java.io.File;
import java.util.List;

/**
 * 查看照片的适配器
 *
 * @author sjl
 * {@link PictureViewActivity}
 */
public class PictureViewAdapter extends PagerAdapter {

    private int type;

    /**
     * 上下文对象
     */
    private Context mContext;

    /**
     * 图片地址列表
     */
    private List<File> mPicFileList;

    /**
     * 图片地址列表
     */
    private List<String> mPicUrlList;

    public PictureViewAdapter(Context context, int type, List<File> picFileList, List<String> picUrlList) {
        this.type = type;
        this.mContext = context;
        this.mPicFileList = picFileList;
        this.mPicUrlList = picUrlList;
    }

    /**
     * 该方法是计算页面的个数并返回页面的个数
     *
     * @return 页面的个数
     */
    @Override
    public int getCount() {
        if (1 == type) {
            return mPicFileList == null ? 0 : mPicFileList.size();
        } else {
            return mPicUrlList == null ? 0 : mPicUrlList.size();
        }
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
        PhotoView photoView = new PhotoView(mContext);
        if (1 == type) {
            ImageUtil.displayImage(mContext, photoView, mPicFileList.get(position));
        } else {
            ImageUtil.displayImage(mContext, photoView, mPicUrlList.get(position));
        }
        //把这个position位置要显示的View添加到ViewPager中
        container.addView(photoView);
        return photoView;
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
}
