package com.ybw.yibai.common.widget.selectsspecview;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.ybw.yibai.R;
import com.ybw.yibai.common.bean.ProductDetails.DataBean.SpecBean;
import com.ybw.yibai.common.utils.DensityUtil;

import java.util.List;

/**
 * 选择规格视图
 *
 * @author sjl
 */
public class SelectsSpecView extends LinearLayout {

    /**
     * 规格标题栏的文本间距
     */
    private int titleLeftMargin = 16;

    /**
     * 规格标题栏的文本间距
     */
    private int titleTopMargin = 12;

    /**
     * 规格标题栏的文本间距
     */
    private int titleRightMargin = 16;

    /**
     * 规格标题栏的文本间距
     */
    private int titleBottomMargin = 10;

    /*----------*/

    /**
     * 整个商品属性的左右间距
     */
    private int flowLayoutMargin = 16;

    /**
     * 属性按钮的高度
     */
    private int buttonHeight = 26;

    /**
     * 属性按钮之间的左边距
     */
    private int buttonLeftMargin = 12;

    /**
     * 属性按钮之间的上边距
     */
    private int buttonTopMargin = 8;

    /**
     * 文字与按钮的边距
     */
    private int textPadding = 8;

    /**
     * 上下文
     */
    private Context mContext;

    /**
     * 数据源
     */
    private List<SpecBean> mBigClassList;

    /**
     * 选择后的回调监听
     */
    private OnSpecSelectedListener mOnSpecSelectedListener;

    public SelectsSpecView(Context context) {
        super(context);
        initView(context);
    }

    public SelectsSpecView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        mContext = context;
        setOrientation(LinearLayout.VERTICAL);
    }

    public void setData(List<SpecBean> list) {
        mBigClassList = list;
        getView();
    }

    public void getView() {
        for (int i = 0; i < mBigClassList.size(); i++) {
            SpecBean attr = mBigClassList.get(i);
            // 设置规格分类的标题
            TextView textView = new TextView(mContext);
            textView.setTextColor(ContextCompat.getColor(mContext, R.color.main_text_color));
            textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            textView.setText(attr.getName());
            textView.setTextSize(14);
            LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(
                    DensityUtil.dpToPx(mContext, titleLeftMargin), DensityUtil.dpToPx(mContext, titleTopMargin),
                    DensityUtil.dpToPx(mContext, titleRightMargin), DensityUtil.dpToPx(mContext, titleBottomMargin)
            );
            textView.setLayoutParams(params);
            addView(textView);

            // 设置一个大规格下的所有小规格
            FlowLayout layout = new FlowLayout(mContext);
            layout.setPadding(DensityUtil.dpToPx(mContext, flowLayoutMargin), 0, DensityUtil.dpToPx(mContext, flowLayoutMargin), 0);
            layout.setTitle(attr.getName());
            layout.setTitlePosition(i);
            //设置选择监听
            if (null != mOnSpecSelectedListener) {
                layout.setSpecSelectedListener(mOnSpecSelectedListener);
            }

            for (int j = 0; j < attr.getSon().size(); j++) {
                // 属性按钮
                RadioButton button = new RadioButton(mContext);
                // 默认选中第一个
                /*if (j == 0) {
                    button.setChecked(true);
                }*/
                // 设置被选中
                if(attr.getSon().get(j).isSelected()){
                    button.setChecked(true);
                }
                // 设置按钮的参数
                LayoutParams buttonParams = new LayoutParams(LayoutParams.WRAP_CONTENT, DensityUtil.dpToPx(mContext, buttonHeight));
                // 设置margin属性,需传入LayoutParams否则会丢失原有的布局参数
                MarginLayoutParams marginParams = new MarginLayoutParams(buttonParams);
                marginParams.leftMargin = DensityUtil.dpToPx(mContext, buttonLeftMargin);
                marginParams.topMargin = DensityUtil.dpToPx(mContext, buttonTopMargin);
                button.setLayoutParams(marginParams);

                // 设置文字的边距
                int padding = DensityUtil.dpToPx(mContext, textPadding);
                button.setPadding(padding, 0, padding, 0);

                SpecBean.SonBean smallAttr = attr.getSon().get(j);
                button.setText(smallAttr.getName());
                button.setTextSize(12);
                button.setGravity(Gravity.CENTER);
                button.setButtonDrawable(android.R.color.transparent);
                button.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                button.setBackgroundResource(R.drawable.selects_spec_view_style);
                button.setTextColor(ContextCompat.getColorStateList(mContext, R.color.selects_spec_view_text_color_style));
                layout.addView(button);
            }
            addView(layout);
        }
    }

    public void setOnSelectedListener(OnSpecSelectedListener onSpecSelectedListener) {
        this.mOnSpecSelectedListener = onSpecSelectedListener;
    }
}
