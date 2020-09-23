package com.ybw.yibai.common.adapter;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ybw.yibai.R;
import com.ybw.yibai.common.bean.SceneInfo;
import com.ybw.yibai.common.utils.ImageUtil;
import com.ybw.yibai.module.scene.SceneActivity;

import java.io.File;
import java.util.List;

import static com.ybw.yibai.common.utils.FileUtil.judeFileExists;

/**
 * 显示场景列表的适配器
 *
 * @author sjl
 * {@link SceneActivity}
 */
public class SceneListAdapter extends BaseAdapter {

    public static final String TAG = "SceneListAdapter";

    /**
     * 上下文对象
     */
    private Context mContext;

    /**
     * 用户场景信息列表的集合
     */
    private List<SceneInfo> mSceneInfoList;

    private LayoutInflater mInflater;

    public SceneListAdapter(Context context, List<SceneInfo> sceneInfoList) {
        this.mContext = context;
        this.mSceneInfoList = sceneInfoList;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mSceneInfoList == null ? 0 : mSceneInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return mSceneInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.listview_scene_list_item_layout, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.mRootLayout = convertView.findViewById(R.id.rootLayout);
            viewHolder.mCheckBox = convertView.findViewById(R.id.checkbox);
            viewHolder.mImageView = convertView.findViewById(R.id.iconImageView);
            viewHolder.mNameTextView = convertView.findViewById(R.id.nameTextView);
            viewHolder.mView = convertView.findViewById(R.id.view);
            viewHolder.mDividerView = convertView.findViewById(R.id.dividerView);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        SceneInfo sceneInfo = mSceneInfoList.get(position);
        // 是否显示CheckBox
        boolean showCheckBox = sceneInfo.isShowCheckBox();
        // 是否选中CheckBox
        boolean select = sceneInfo.isSelectCheckBox();
        // 场景背景
        String sceneBackground = sceneInfo.getSceneBackground();
        // 场景图片
        String name = sceneInfo.getSceneName();

        if (showCheckBox) {
            viewHolder.mCheckBox.setVisibility(View.VISIBLE);
            viewHolder.mView.setVisibility(View.VISIBLE);
            // 设置可以点击
            viewHolder.mNameTextView.setEnabled(true);
        } else {
            viewHolder.mCheckBox.setVisibility(View.GONE);
            viewHolder.mView.setVisibility(View.GONE);
            // 设置不可以点击
            viewHolder.mNameTextView.setEnabled(false);
        }
        if (select) {
            viewHolder.mCheckBox.setChecked(true);
        } else {
            viewHolder.mCheckBox.setChecked(false);
        }

        if (judeFileExists(sceneBackground)) {
            ImageUtil.displayImage(mContext, viewHolder.mImageView, new File(sceneBackground).toURI().toString());
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                viewHolder.mImageView.setBackground(mContext.getDrawable(R.mipmap.default_scene_background));
            }
        }

        if (TextUtils.isEmpty(name)) {
            viewHolder.mNameTextView.setText(" ");
        } else {
            viewHolder.mNameTextView.setText(name);
        }

        if (position == mSceneInfoList.size() - 1) {
            viewHolder.mDividerView.setVisibility(View.INVISIBLE);
        } else {
            viewHolder.mDividerView.setVisibility(View.VISIBLE);
        }

        /*----------*/

        viewHolder.mRootLayout.setOnClickListener(v -> {
            if (showCheckBox) {
                // 编辑状态
                boolean selectCheckBox = sceneInfo.isSelectCheckBox();
                if (selectCheckBox) {
                    // 如果之前选中,那现在设置没有选中
                    viewHolder.mCheckBox.setChecked(false);
                    sceneInfo.setSelectCheckBox(false);
                } else {
                    // 如果之前没有选中,那现在设置选中
                    viewHolder.mCheckBox.setChecked(true);
                    sceneInfo.setSelectCheckBox(true);
                }
            } else if (null != onItemClickListener) {
                // 正常状态
                onItemClickListener.onSceneListItemClick(position);
            }
        });

        viewHolder.mCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                sceneInfo.setSelectCheckBox(true);
            } else {
                sceneInfo.setSelectCheckBox(false);
            }
            if (null != mOnCheckBoxClickListener) {
                mOnCheckBoxClickListener.onCheckBoxClick(position);
            }
        });

        viewHolder.mNameTextView.setOnClickListener(v -> {
            if (null != onSceneNameClickListener) {
                onSceneNameClickListener.onSceneNameClick(position);
            }
        });

        return convertView;
    }

    private class ViewHolder {
        /**
         *
         */
        RelativeLayout mRootLayout;

        /**
         *
         */
        CheckBox mCheckBox;

        /**
         * 场景图片
         */
        ImageView mImageView;

        /**
         * 场景名称
         */
        TextView mNameTextView;

        /**
         * 场景名称下面的下划线
         */
        View mView;

        /**
         * 分隔线
         */
        View mDividerView;
    }

    /*----------*/
    /**
     * 自定义接口,用于回调多选状态下CheckBox点击事件
     */
    public interface OnCheckBoxClickListener {

        /**
         * 在CheckBox选择状态发生改变时回调
         *
         * @param position 位置
         */
        void onCheckBoxClick(int position);
    }

    /**
     * 2.声明一个接口变量
     */
    private OnCheckBoxClickListener mOnCheckBoxClickListener;

    /**
     * 3.初始化接口变量
     *
     * @param onCheckBoxClickListener CheckBox点击事件侦听器
     */
    public void setOnCheckBoxClickListener(OnCheckBoxClickListener onCheckBoxClickListener) {
        mOnCheckBoxClickListener = onCheckBoxClickListener;
    }

    /*----------*/

    /**
     * 1.定义一个接口
     */
    public interface OnSceneNameClickListener {

        /**
         * ListView 场景名称点击时回调
         *
         * @param position 被点击的Item位置
         */
        void onSceneNameClick(int position);
    }

    /**
     * 2.声明一个接口变量
     */
    private OnSceneNameClickListener onSceneNameClickListener;

    /**
     * 3.初始化接口变量
     *
     * @param onSceneNameClickListener ListView 场景名称点击的侦听器
     */
    public void setOnSceneNameClickListener(OnSceneNameClickListener onSceneNameClickListener) {
        this.onSceneNameClickListener = onSceneNameClickListener;
    }

    /*----------*/

    /**
     * 1.定义一个接口
     */
    public interface OnItemClickListener {

        /**
         * 在场景列表ListView Item 点击时回调
         *
         * @param position 被点击的Item位置
         */
        void onSceneListItemClick(int position);
    }

    /**
     * 2.声明一个接口变量
     */
    private OnItemClickListener onItemClickListener;

    /**
     * 3.初始化接口变量
     *
     * @param onItemClickListener ListView Item点击的侦听器
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}