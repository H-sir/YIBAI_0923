package com.ybw.yibai.common.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.squareup.picasso.Picasso;
import com.ybw.yibai.R;
import com.ybw.yibai.common.bean.ListBean;
import com.ybw.yibai.common.bean.SimulationData;
import com.ybw.yibai.common.utils.ImageUtil;
import com.ybw.yibai.common.utils.TimeUtil;
import com.ybw.yibai.common.widget.stickerview.DrawableSticker;
import com.ybw.yibai.common.widget.stickerview.StickerView;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.ybw.yibai.common.constants.Preferences.PLANT;
import static com.ybw.yibai.common.constants.Preferences.POT;
import static com.ybw.yibai.common.utils.DensityUtil.dpToPx;
import static com.ybw.yibai.common.utils.FileUtil.judeFileExists;
import static com.ybw.yibai.common.utils.ImageUtil.drawableToBitmap;
import static com.ybw.yibai.common.utils.ImageUtil.pictureSynthesis;
import static com.ybw.yibai.common.utils.OtherUtil.getStatusBarHeight;

/**
 * 多张搭配图片模拟效果对比适配器
 *
 * @author sjl
 */
public class ImageContrastAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public final String TAG = "ImageContrastAdapter";

    /**
     * 多张搭配图片模拟效果为2列
     */
    private final int COLUMN = 2;

    /**
     * 状态栏+标题栏+阴影图片的高度
     */
    private int topHeight;

    /**
     * Item高度
     */
    private int itemHeight;

    /**
     * 上下文对象
     */
    private Context mContext;

    /**
     * 场景背景图片文件的绝对路径
     */
    private String mFilePath;

    /**
     * 贴纸数据
     */
    SimulationData mSimulationData;
    private ListBean plant;
    private ListBean pot;
    /**
     * 推荐植物花盆
     */
    private List<ListBean> mList;

    public ImageContrastAdapter(Context context, String filePath, ListBean plant, ListBean pot,SimulationData mSimulationData, List<ListBean> list) {
        this.mContext = context;
        this.mFilePath = filePath;
        this.plant = plant;
        this.pot = pot;
        this.mSimulationData = mSimulationData;
        this.mList = list;

        topHeight = getStatusBarHeight(context) - dpToPx(context, 44 + 2);
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        itemHeight = dm.heightPixels / 2;
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        View view = LayoutInflater.from(mContext).inflate(R.layout.recyclerview_image_contrast_item_layout, null);
        viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        MyViewHolder myViewHolder = (MyViewHolder) viewHolder;

        if (judeFileExists(mFilePath)) {
            File file = new File(mFilePath);
            Picasso.with(mContext).load(file).into(myViewHolder.mSceneBackgroundImageView);
        } else {
            myViewHolder.mSceneBackgroundImageView.setBackground(mContext.getDrawable(R.mipmap.default_scene_background));
        }

        downloadImage(myViewHolder, position);
    }

    private void downloadImage(@NonNull MyViewHolder myViewHolder, int position) {
        // 主产品配图url地址
        String productPic3;
        // 主产品高度
        double productHeight;
        // 偏移量(如果该产品是植物)
        double productOffsetRatio;
        // 附加产品配图url地址
        String augmentedProductPic3;
        // 附加产品高度
        double augmentedProductHeight;
        // 偏移量(如果该产品是花盆)
        double augmentedProductOffsetRatio;

        ListBean listBean = mList.get(position);
        String categoryCode = listBean.getCategoryCode();
        if (PLANT.equals(categoryCode)) {
            productPic3 = listBean.getPic3();
            productHeight = listBean.getHeight();
            productOffsetRatio = listBean.getOffset_ratio();

            augmentedProductPic3 = pot.getPic3();
            augmentedProductHeight = pot.getHeight();
            augmentedProductOffsetRatio = pot.getOffset_ratio();
        } else {
            productPic3 = plant.getPic3();
            productHeight = plant.getHeight();
            productOffsetRatio = plant.getOffset_ratio();

            augmentedProductPic3 = listBean.getPic3();
            augmentedProductHeight = listBean.getHeight();
            augmentedProductOffsetRatio = listBean.getOffset_ratio();
        }

        List<Bitmap> bitmapList = new ArrayList<>();
        SimpleTarget<Drawable> augmentedSimpleTarget = new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                bitmapList.add(drawableToBitmap(resource));
                // 标记是否存在下载图片失败的情况
                boolean isFailed = false;
                Bitmap[] bitmaps = new Bitmap[bitmapList.size()];
                for (int i = 0; i < bitmapList.size(); i++) {
                    Bitmap bitmap = bitmapList.get(i);
                    if (null == bitmap) {
                        isFailed = true;
                        break;
                    } else {
                        bitmaps[i] = bitmap;
                    }
                }
                if (isFailed) {
                    // 下载图片失败return
                    return;
                }
                Bitmap bitmap = pictureSynthesis(
                        productHeight,
                        augmentedProductHeight,
                        productOffsetRatio,
                        augmentedProductOffsetRatio,
                        bitmaps
                );
                if (null == bitmap) {
                    // 合成图片失败return
                    return;
                }
                setImage(myViewHolder, bitmap, position);
            }
        };
        SimpleTarget<Drawable> simpleTarget = new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                bitmapList.add(drawableToBitmap(resource));
                Glide.with(mContext).load(augmentedProductPic3).into(augmentedSimpleTarget);
            }
        };
        Glide.with(mContext).load(productPic3).into(simpleTarget);
    }

    private void setImage(@NonNull MyViewHolder myViewHolder, @NonNull Bitmap bitmap, int position) {
        String picturePath = ImageUtil.saveImage(bitmap, String.valueOf(TimeUtil.getTimeStamp()));

        float oldX = mSimulationData.getX();
        float oldY = mSimulationData.getY();
        // 更换搭配之前贴纸实际的宽度
        double oldWidth = mSimulationData.getWidth();
        // 更换搭配之前贴纸实际的高度
        double oldHeight = mSimulationData.getHeight();
        // 更换搭配之前贴纸的X轴的缩放比例
        double oldXScale = mSimulationData.getxScale();
        // 更换搭配之前贴纸的Y轴的缩放比例
        double oldYScale = mSimulationData.getyScale();
        // 更换搭配之前贴纸显示的宽度
        double showWidth = oldWidth * oldXScale;
        // 更换搭配之前贴纸显示的高度
        double showHeight = oldHeight * oldYScale;

        // bitmap 宽度
        int width = bitmap.getWidth();
        // bitmap 高度
        int height = bitmap.getHeight();
        // 新合成的图片X轴的缩放?就与更换搭配之前贴纸显示的宽度一样
        BigDecimal xBigDecimal = new BigDecimal((float) showWidth / width);
        double xScale = xBigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        // 新合成的图片Y轴的缩放?就与更换搭配之前贴纸显示的高度一样
        BigDecimal yBigDecimal = new BigDecimal((float) showHeight / height);
        double yScale = yBigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

        Drawable drawable = new BitmapDrawable(mContext.getResources(), bitmap);
        DrawableSticker drawableSticker = new DrawableSticker(drawable);
        // 添加新的贴纸之前删除上一次保存的贴纸
        myViewHolder.mStickerView.removeAllStickers();
        myViewHolder.mStickerView.addSticker(
                drawableSticker,
                oldX / COLUMN,
                oldY / COLUMN - topHeight / COLUMN,
                yScale / COLUMN,
                yScale / COLUMN
        );
        myViewHolder.mView.setOnClickListener(v -> {
            if (null == onItemClickListener) {
                return;
            }
            onItemClickListener.onItemClick(position, picturePath);
        });
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout mRootView;

        StickerView mStickerView;

        ImageView mSceneBackgroundImageView;

        View mView;

        private MyViewHolder(View itemView) {
            super(itemView);

            mRootView = itemView.findViewById(R.id.rootLayout);
            mStickerView = itemView.findViewById(R.id.stickerView);
            mSceneBackgroundImageView = itemView.findViewById(R.id.sceneBackgroundImageView);
            mView = itemView.findViewById(R.id.view);

            ViewGroup.LayoutParams params = mRootView.getLayoutParams();
            params.height = itemHeight;
            mStickerView.setLayoutParams(params);
        }
    }

    /**
     * 1.定义一个接口
     */
    public interface OnItemClickListener {

        /**
         * 在RecyclerView Item 点击时回调
         *
         * @param position    被点击的Item位置
         * @param picturePath 组合图片在本地保存的路径
         */
        void onItemClick(int position, String picturePath);
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