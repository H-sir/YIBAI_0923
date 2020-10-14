package com.ybw.yibai.module.designdetails;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXMiniProgramObject;
import com.ybw.yibai.R;
import com.ybw.yibai.base.BaseActivity;
import com.ybw.yibai.base.YiBaiApplication;
import com.ybw.yibai.common.adapter.DesignDetailsListAdapter;
import com.ybw.yibai.common.bean.BaseBean;
import com.ybw.yibai.common.bean.DesignDetails;
import com.ybw.yibai.common.bean.DesignList;
import com.ybw.yibai.common.bean.NetworkType;
import com.ybw.yibai.common.bean.SceneInfo;
import com.ybw.yibai.common.bean.SkuMarketBean;
import com.ybw.yibai.common.classs.GridSpacingItemDecoration;
import com.ybw.yibai.common.helper.SceneHelper;
import com.ybw.yibai.common.utils.DensityUtil;
import com.ybw.yibai.common.utils.ExceptionUtil;
import com.ybw.yibai.common.utils.ImageDispose;
import com.ybw.yibai.common.utils.ImageUtil;
import com.ybw.yibai.common.utils.MessageUtil;
import com.ybw.yibai.common.utils.OtherUtil;
import com.ybw.yibai.common.widget.WaitDialog;
import com.ybw.yibai.common.widget.nestlistview.NestFullListView;
import com.ybw.yibai.common.widget.nestlistview.NestFullListViewAdapter;
import com.ybw.yibai.common.widget.nestlistview.NestFullViewHolder;
import com.ybw.yibai.module.design.DesignActivity;
import com.ybw.yibai.module.scene.SceneActivity;
import com.ybw.yibai.module.user.UserContract;
import com.ybw.yibai.module.user.UserPresenterImpl;

import org.xutils.DbManager;
import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static android.support.v7.widget.StaggeredGridLayoutManager.VERTICAL;
import static com.ybw.yibai.common.constants.Encoded.REQUEST_DESIGN_DETAILS;
import static com.ybw.yibai.common.constants.Encoded.REQUEST_DESIGN_DETAILS_CODE;
import static com.ybw.yibai.common.constants.Encoded.REQUEST_OPEN_PHOTOS_CODE;
import static com.ybw.yibai.common.constants.HttpUrls.BASE_URL;
import static com.ybw.yibai.common.constants.Preferences.DESIGN_NUMBER;

/**
 * <pre>
 *     author : HKR
 *     time   : 2020/09/03
 *     desc   :设计详情页
 * </pre>
 */
public class DesignDetailsActivity extends BaseActivity implements DesignDetailsContract.DesignDetailsView,
        UserContract.UserView {

    @BindView(R.id.design_number)
    TextView designNumber;
    @BindView(R.id.designDetailsListView)
    NestFullListView designDetailsListView;
    @BindView(R.id.rootLayout)
    LinearLayout rootLayout;

    /**
     * 设计详情列表适配器
     */
    private NestFullListViewAdapter mNestFullListViewAdapter;
    /**
     * 设计详情列表集合
     */
    private List<DesignDetails.DataBean.SchemelistBean> schemelistBeans = new ArrayList<>();

    /**
     * 自定义等待Dialog
     */
    private WaitDialog mWaitDialog;

    private DesignDetailsContract.DesignDetailsPresenter mDesignDetailsPresenter;

    /**
     * 当前设计
     */
    private DesignDetails mDesignDetails = null;
    private UserContract.UserPresenter mUserPresenter;
    /**
     * 跳转的场景
     */
    private DesignDetails.DataBean.SchemelistBean schemelistBean = null;

    @Override
    protected int setLayout() {
        return R.layout.activity_design_details_layout;
    }

    @Override
    protected void initView() {
        mWaitDialog = new WaitDialog(this);
        mDesignDetailsPresenter = new DesignDetailsPresenterImpl(this);
        mUserPresenter = new UserPresenterImpl(this);
    }

    @Override
    protected void initData() {

    }

    String mDdesignNumber;

    @Override
    protected void initEvent() {
        mDdesignNumber = (String) getIntent().getSerializableExtra(DESIGN_NUMBER);
        if (null == mDdesignNumber) {
            return;
        }
        mDesignDetailsPresenter.getDesignDetails(mDdesignNumber);
    }

    /**
     * 获取设计详情成功
     */
    @Override
    public void onGetDesignDetailsSuccess(DesignDetails designDetails) {
        if (designDetails.getCode() != 200) {
            MessageUtil.showMessage(designDetails.getMsg());
            return;
        }
        schemelistBeans.clear();
        mDesignDetails = designDetails;
        designNumber.setText(designDetails.getData().getNumber());
        schemelistBeans.addAll(designDetails.getData().getSchemelist());

        setAdapter(schemelistBeans);
    }

    private void setAdapter(List<DesignDetails.DataBean.SchemelistBean> mSchemelistBeans) {
        mNestFullListViewAdapter = new NestFullListViewAdapter<DesignDetails.DataBean.SchemelistBean>(
                R.layout.listview_design_details_list_item_layout, mSchemelistBeans) {
            @Override
            public void onBind(int pos, DesignDetails.DataBean.SchemelistBean schemelistBean, NestFullViewHolder holder) {
                ImageView mDesignBg = holder.getView(R.id.designBg);
                TextView mDesignDetailsName = holder.getView(R.id.designDetailsName);
                TextView mDesignDetailsRename = holder.getView(R.id.designDetailsRename);
                TextView mDesignDetailsDelete = holder.getView(R.id.designDetailsDelete);
                mDesignDetailsName.setText(schemelistBean.getSchemeName());
                if (schemelistBean.getBgpic() != null && !schemelistBean.getBgpic().isEmpty())
                    ImageUtil.displayImage(getApplicationContext(), mDesignBg, schemelistBean.getBgpic());

                mDesignDetailsName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SceneHelper.savePhotoNum(getApplicationContext(), schemelistBean.getImglist().size());
                        onSceneItemClick(schemelistBean);
                    }
                });

                mDesignDetailsDelete.setOnClickListener(view -> {
                    onDesignDetailsListDelete(schemelistBean);
                });

                mDesignDetailsRename.setOnClickListener(view -> {
                    onDesignDetailsRename(schemelistBean);
                });

                //第二层
                NestFullListView view = (NestFullListView) holder.getView(R.id.designDetailsListView);
                view.setOrientation(LinearLayout.HORIZONTAL);
                view.setAdapter(new NestFullListViewAdapter<DesignDetails.DataBean.SchemelistBean.ImaData>
                        (R.layout.listview_design_scheme_details_list_item_layout,
                                schemelistBean.getImglist()) {
                    @Override
                    public void onBind(int pos, DesignDetails.DataBean.SchemelistBean.ImaData imaData, NestFullViewHolder holder) {
                        ImageView mDesignSchemeImage = holder.getView(R.id.designSchemeImage);
                        ImageUtil.displayImage(getApplicationContext(), mDesignSchemeImage, imaData.getPic());
                        mDesignSchemeImage.setOnClickListener(v -> {
                            onDesignDetailsDelete(imaData);
                        });
                    }
                });
            }
        };
        designDetailsListView.setAdapter(mNestFullListViewAdapter);
    }

    /**
     * 场景点击跳转
     */
    public void onSceneItemClick(DesignDetails.DataBean.SchemelistBean schemelistBean) {
        this.schemelistBean = schemelistBean;
        mUserPresenter.findUserSceneListInfo();
    }

    /**
     * 查看用户场景
     */
    @Override
    public void onFindUserSceneInfoSuccess(List<SceneInfo> sceneInfoList) {
        if (sceneInfoList != null && sceneInfoList.size() > 0) {
            for (Iterator<SceneInfo> iterator = sceneInfoList.iterator(); iterator.hasNext(); ) {
                SceneInfo info = iterator.next();
                if (schemelistBean.getSchemeId().equals(String.valueOf(info.getScheme_id()))) {
                    info.setEditScene(true);
                    mUserPresenter.updateUserScene(info);
                    break;
                }
            }
        } else {
            MessageUtil.showMessage("该场景已经删除，请重新创建");
        }
    }

    @Override
    public void onUpdateUserSceneListSuccess() {
        Intent intent = new Intent(DesignDetailsActivity.this, SceneActivity.class);
        startActivity(intent);
    }

    @Override
    public void onUpdateUserSceneSuccess() {

    }

    /**
     * 删除设计图片
     */
    public void onDesignDetailsDelete(DesignDetails.DataBean.SchemelistBean.ImaData imaData) {
        mDesignDetailsPresenter.deleteSchemePic(imaData.getId());
    }

    /**
     * 删除设计图片成功
     */
    @Override
    public void onDeleteSchemePicSuccess(String pic) {
        mDesignDetailsPresenter.getDesignDetails(mDdesignNumber);
    }

    /**
     * 重新命名
     */
    public void onDesignDetailsRename(DesignDetails.DataBean.SchemelistBean schemelistBean) {
        existSceneInfoPopupWindow(schemelistBean);
    }

    PopupWindow mPopupWindow;
    DesignDetails.DataBean.SchemelistBean updateSchemelistBean;

    private void existSceneInfoPopupWindow(DesignDetails.DataBean.SchemelistBean schemelistBean) {
        if (null == mPopupWindow) {
            View view = getLayoutInflater().inflate(R.layout.popup_window_edit_scene_name_layout, null);
            mPopupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            EditText editText = view.findViewById(R.id.editText);
            Button cancelButton = view.findViewById(R.id.cancelButton);
            Button determineButton = view.findViewById(R.id.determineButton);
            cancelButton.setOnClickListener(v -> {
                if (null != mPopupWindow && mPopupWindow.isShowing()) {
                    mPopupWindow.dismiss();
                }
            });
            determineButton.setOnClickListener(v -> {
                String scnenName = editText.getText().toString();
                if (scnenName != null && !scnenName.isEmpty()) {
                    updateSchemelistBean = schemelistBean;
                    updateSchemelistBean.setSchemeName(scnenName);
                    if (null != mPopupWindow && mPopupWindow.isShowing()) {
                        mPopupWindow.dismiss();
                    }
                    mDesignDetailsPresenter.editSceneName(scnenName, schemelistBean);
                } else {
                    MessageUtil.showMessage("请输入修改的名称");
                }
            });
            mPopupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
            mPopupWindow.setOutsideTouchable(true);
            mPopupWindow.setFocusable(true);

            // 设置一个动画效果
            mPopupWindow.setAnimationStyle(R.style.PopupWindow_Anim);

            // 在弹出PopupWindow设置屏幕透明度
            OtherUtil.setBackgroundAlpha(this, 0.6f);
            // 添加PopupWindow窗口关闭事件
            mPopupWindow.setOnDismissListener(OtherUtil.popupDismissListener(this, 1f));
            mPopupWindow.showAtLocation(rootLayout, Gravity.CENTER, 0, 0);
        } else {
            // 在弹出PopupWindow设置屏幕透明度
            OtherUtil.setBackgroundAlpha(this, 0.6f);
            // 添加PopupWindow窗口关闭事件
            mPopupWindow.setOnDismissListener(OtherUtil.popupDismissListener(this, 1f));
            mPopupWindow.showAtLocation(rootLayout, Gravity.CENTER, 0, 0);
        }
    }

    @Override
    public void editSceneNameSuccess() {
        for (Iterator<DesignDetails.DataBean.SchemelistBean> iterator = schemelistBeans.iterator(); iterator.hasNext(); ) {
            DesignDetails.DataBean.SchemelistBean bean = iterator.next();
            if (bean.getSchemeId().equals(updateSchemelistBean.getSchemeId())) {
                bean.setSchemeName(updateSchemelistBean.getSchemeName());
                break;
            }
        }
        setAdapter(schemelistBeans);
    }

    /**
     * 删除场景
     */
    public void onDesignDetailsListDelete(DesignDetails.DataBean.SchemelistBean schemelistBean) {
        if (schemelistBeans.size() == 1) {
            MessageUtil.showMessage("最少保留一个场景!");
            return;
        }
        mDesignDetailsPresenter.deleteScheme(mDesignDetails, schemelistBean);
    }

    /**
     * 删除场景成功
     */
    @Override
    public void onDeleteSchemeSuccess(DesignDetails.DataBean.SchemelistBean schemelistBean) {
        schemelistBeans.remove(schemelistBean);

        try {
            DbManager dbManager = YiBaiApplication.getDbManager();
            // 查找当前正在编辑的这一个场景
            List<SceneInfo> defaultSceneInfoList = dbManager.selector(SceneInfo.class)
                    .where("scheme_id", "=", schemelistBean.getSchemeId())
                    .findAll();
            if (defaultSceneInfoList != null && defaultSceneInfoList.size() > 0) {
                dbManager.delete(defaultSceneInfoList);
            }
        } catch (DbException e) {
            e.printStackTrace();
        }

        setAdapter(schemelistBeans);
    }

    /**
     * 删除设计成功
     */
    @Override
    public void onDeleteDesignSuccess(BaseBean baseBean) {
        Intent intent = new Intent(DesignDetailsActivity.this, DesignActivity.class);
        startActivity(intent);
    }

    /**
     * 分享
     */
    private void designShare() {
        WXMiniProgramObject miniProgram = new WXMiniProgramObject();
        miniProgram.webpageUrl = BASE_URL;//兼容低版本的网页链接
        miniProgram.userName = "gh_a532df421aeb";//小程序端提供参数
        miniProgram.path = "/pages/index/index?number=" + mDesignDetails.getData().getNumber(); //小程序页面路径；对于小游戏，可以只传入 query 部分，来实现传参效果，如：传入 "?foo=bar"
        WXMediaMessage mediaMessage = new WXMediaMessage(miniProgram);
        mediaMessage.title = "亲，请查看您的绿植设计方案~" + mDesignDetails.getData().getNumber();//自定义
        mediaMessage.description = mDesignDetails.getData().getNumber();//自定义
        ImageDispose.returnBitMap("http://f.100ybw.com/images/wxminiprogramshare.png", new ImageDispose.CallBack() {
            @Override
            public void onCallBack(byte[] bytes) {
                if (bytes != null)
                    mediaMessage.thumbData = bytes;
                SendMessageToWX.Req req = new SendMessageToWX.Req();
                req.transaction = "";
                req.scene = SendMessageToWX.Req.WXSceneSession;
                req.message = mediaMessage;
                YiBaiApplication.getIWXAPI().sendReq(req);
            }
        });
//
//        Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.mipmap.share_img);
//        Bitmap sendBitmap = Bitmap.createScaledBitmap(bitmap, 200, 200, true);
//        bitmap.recycle();
//        mediaMessage.thumbData = ImageDispose.Bitmap2Bytes(sendBitmap);

    }

    /**
     * 在请求网络数据之前显示Loading界面
     */
    @Override
    public void onShowLoading() {
        if (!mWaitDialog.isShowing()) {
            mWaitDialog.setWaitDialogText(getResources().getString(R.string.loading));
            mWaitDialog.show();
        }
    }

    /**
     * 在请求网络数据完成隐藏Loading界面
     */
    @Override
    public void onHideLoading() {
        if (mWaitDialog.isShowing()) {
            mWaitDialog.dismiss();
        }
    }

    /**
     * 在请求网络数据失败时进行一些操作,如显示错误信息...
     *
     * @param throwable 异常类型
     */
    @Override
    public void onLoadDataFailure(Throwable throwable) {
        ExceptionUtil.handleException(throwable);
    }


    @Override
    public void onNetworkStateChange(NetworkType networkType) {

    }

    @OnClick({R.id.backImageView, R.id.designDetailsDelete, R.id.designDetailsFinish, R.id.designDetailsShare})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backImageView:
                onBackPressed();
                break;
            case R.id.designDetailsDelete:
                mDesignDetailsPresenter.deleteDesign(mDesignDetails);//删除设计
                break;
            case R.id.designDetailsFinish:
                Intent intent = new Intent(DesignDetailsActivity.this, DesignActivity.class);
                startActivity(intent);
                break;
            case R.id.designDetailsShare:
                designShare();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (schemelistBeans.size() > 0)
            SceneHelper.savePhotoNum(getApplicationContext(), schemelistBeans.get(0).getImglist().size());
        Intent intent = getIntent();
        setResult(REQUEST_DESIGN_DETAILS_CODE, intent);
        finish();
    }
}
