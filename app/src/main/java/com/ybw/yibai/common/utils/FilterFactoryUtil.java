package com.ybw.yibai.common.utils;

import jp.co.cyberagent.android.gpuimage.filter.GPUImageBrightnessFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageContrastFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageSaturationFilter;

/**
 * GPUImage 滤镜调节工厂工具类
 *
 * @author sjl
 * https://blog.csdn.net/weixin_41101173/article/details/80374455
 */
public class FilterFactoryUtil {

    public static final String TAG = "FilterFactoryUtil";

    private AbstractFilter<? extends GPUImageFilter> mFilter;

    /**
     * 根据filter初始化对应的滤镜
     *
     * @param filter ImageFilter
     */
    public FilterFactoryUtil(final GPUImageFilter filter) {
        if (filter instanceof GPUImageBrightnessFilter) {
            // 亮度滤镜
            mFilter = new BrightnessFilter().filter(filter);
        } else if (filter instanceof GPUImageContrastFilter) {
            // 对比度滤镜
            mFilter = new ContrastFilter().filter(filter);
        } else if (filter instanceof GPUImageSaturationFilter) {
            // 饱和度滤镜
            mFilter = new SaturationFilter().filter(filter);
        }
    }

    /**
     * 调节滤镜的值
     *
     * @param percentage 值
     */
    public void adjust(final int percentage) {
        if (null != mFilter) {
            mFilter.adjust(percentage);
        }
    }

    private abstract class AbstractFilter<T extends GPUImageFilter> {

        private T filter;

        public AbstractFilter<T> filter(final GPUImageFilter filter) {
            this.filter = (T) filter;
            return this;
        }

        public T getFilter() {
            return filter;
        }

        /**
         * 调节滤镜的值
         *
         * @param percentage 滤镜的值
         */
        protected abstract void adjust(int percentage);

        protected int range(final int start, final int end, final int percentage) {
            return (end - start) * percentage / 100 + start;
        }

        protected float range(final float start, final float end, final int percentage) {
            return (end - start) * percentage / 100.0f + start;
        }
    }

    /**
     * 亮度滤镜,值的范围是-1.0到1.0,正常水平是0.0
     */
    private class BrightnessFilter extends AbstractFilter<GPUImageBrightnessFilter> {

        @Override
        public void adjust(final int percentage) {
            float range = range(-1.0f, 1.0f, percentage);
            getFilter().setBrightness(range);
        }
    }

    /**
     * 对比度滤镜,值范围从0.0到4.0,以1.0为正常水平
     */
    private class ContrastFilter extends AbstractFilter<GPUImageContrastFilter> {

        @Override
        public void adjust(int percentage) {
            float range = range(-0.0f, 4.0f, percentage);
            getFilter().setContrast(range);
        }
    }

    /**
     * 饱和度滤镜,值范围从0.0-2.0,默认值为1.0
     */
    private class SaturationFilter extends AbstractFilter<GPUImageSaturationFilter> {

        @Override
        public void adjust(int percentage) {
            float range = range(-0.0f, 2.0f, percentage);
            getFilter().setSaturation(range);
        }
    }
}