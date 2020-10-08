package com.ybw.yibai.module.design;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXMiniProgramObject;
import com.ybw.yibai.R;
import com.ybw.yibai.base.BaseActivity;
import com.ybw.yibai.base.YiBaiApplication;
import com.ybw.yibai.common.adapter.DesignListAdapter;
import com.ybw.yibai.common.bean.BaseBean;
import com.ybw.yibai.common.bean.DesignList;
import com.ybw.yibai.common.bean.NetworkType;
import com.ybw.yibai.common.bean.SceneInfo;
import com.ybw.yibai.common.classs.GridSpacingItemDecoration;
import com.ybw.yibai.common.utils.DensityUtil;
import com.ybw.yibai.common.utils.ExceptionUtil;
import com.ybw.yibai.common.utils.ImageDispose;
import com.ybw.yibai.common.utils.MessageUtil;
import com.ybw.yibai.common.utils.OtherUtil;
import com.ybw.yibai.common.widget.WaitDialog;
import com.ybw.yibai.module.drawing.SimulationDrawingActivity;
import com.ybw.yibai.module.main.MainActivity;
import com.ybw.yibai.module.scene.SceneActivity;
import com.ybw.yibai.module.user.UserContract;
import com.ybw.yibai.module.user.UserPresenterImpl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static android.support.v7.widget.StaggeredGridLayoutManager.VERTICAL;
import static com.ybw.yibai.common.constants.HttpUrls.BASE_URL;
import static com.ybw.yibai.common.constants.Preferences.DESIGN_CREATE;

/**
 * <pre>
 *     author : HKR
 *     time   : 2020/09/02
 *     desc   :
 * </pre>
 */
public class DesignActivity extends BaseActivity implements DesignContract.DesignView,
        DesignListAdapter.OnDesignDeleteClickListener,
        DesignListAdapter.OnDesignShareClickListener,
        DesignListAdapter.OnDesignSchemeImageClickListener, UserContract.UserView {
    private String TAG = "DesignActivity";

    /**
     * 标题
     */
    @BindView(R.id.titleTextView)
    TextView titleTextView;
    @BindView(R.id.rootLayout)
    LinearLayout mRootLayout;
    @BindView(R.id.barView)
    RelativeLayout barView;
    /**
     * 设计列表
     */
    @BindView(R.id.designListView)
    RecyclerView designListView;

    /**
     * 设计列表适配器
     */
    private DesignListAdapter mDesignListAdapter;
    /**
     * 设计列表集合
     */
    private List<DesignList.DataBean.ListBean> designLists = new ArrayList<>();

    /**
     * 删除的设计
     */
    private DesignList.DataBean.ListBean deleteListBead = null;

    private DesignContract.DesignPresenter mDesignPresenter;

    private UserContract.UserPresenter mUserPresenter;

    /**
     * 自定义等待Dialog
     */
    private WaitDialog mWaitDialog;

    /**
     * 正在编辑的场景
     */
    private SceneInfo sceneInfo = null;

    /**
     * 弹窗提示
     */
    private PopupWindow mPopupWindow = null;

    /**
     * 跳转的场景
     */
    private DesignList.DataBean.ListBean.SchemelistBean mSchemelistBean;

    @Override
    protected int setLayout() {
        return R.layout.activity_design_layout;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        mWaitDialog = new WaitDialog(this);
        mDesignPresenter = new DesignPresenterImpl(this);
        mUserPresenter = new UserPresenterImpl(this);
        // 获取GridLayout布局管理器设置参数控制RecyclerView显示的样式
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(1, VERTICAL);
        // 设置RecyclerView间距
        int gap = DensityUtil.dpToPx(getApplicationContext(), 8);
        GridSpacingItemDecoration decoration = new GridSpacingItemDecoration(1, gap, false);
        // 给RecyclerView设置布局管理器(必须设置)
        designListView.setLayoutManager(manager);
        designListView.addItemDecoration(decoration);
        designListView.setNestedScrollingEnabled(false);
        designListView.setHasFixedSize(false);

    }

    @Override
    protected void initEvent() {
        mDesignPresenter.getEditSceneInfo();
    }

    @Override
    public void onFindEditSceneInfo(List<SceneInfo> defaultSceneInfoList) {
        if (defaultSceneInfoList != null && defaultSceneInfoList.size() > 0)
            sceneInfo = defaultSceneInfoList.get(0);

        mDesignListAdapter = new DesignListAdapter(this, designLists, sceneInfo);
        designListView.setAdapter(mDesignListAdapter);

        mDesignListAdapter.setDesignSchemeImageClickListener(this);
        mDesignListAdapter.setOnDesignDeleteClickListener(this);
        mDesignListAdapter.setOnDesignShareClickListener(this);

        mDesignPresenter.getDesignList();
    }

    @Override
    public void onGetDesignListSuccess(DesignList designList) {
        designLists.clear();
        if (designList.getData() != null && designList.getData().getList() != null && designList.getData().getList().size() > 0)
            designLists.addAll(designList.getData().getList());
        mDesignListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onNetworkStateChange(NetworkType networkType) {

    }

    /**
     * 调用删除按钮
     */
    @Override
    public void onDesignDelete(int position) {
        deleteListBead = designLists.get(position);
        mDesignPresenter.deleteDesign(deleteListBead);
    }

    /**
     * 删除接口返回
     */
    @Override
    public void onDeleteDesignSuccess(BaseBean baseBean) {
        designLists.remove(deleteListBead);
        mDesignListAdapter.notifyDataSetChanged();
    }

    /**
     * 调用分享按钮分享到微信小程序
     * <p>
     * SendMessageToWX.Req.WXSceneSession；分享到微信好友
     * SendMessageToWX.Req.WXSceneTimeline；分享到微信朋友圈
     * SendMessageToWX.Req.WXSceneFavorite；添加到微信收藏夹
     */
    @Override
    public void onDesignShare(int position) {
        WXMiniProgramObject miniProgram = new WXMiniProgramObject();
        miniProgram.webpageUrl = BASE_URL;//兼容低版本的网页链接
        miniProgram.userName = "gh_a532df421aeb";//小程序端提供参数
        miniProgram.path = "/pages/index/index?number=" + designLists.get(position).getNumber(); //小程序页面路径；对于小游戏，可以只传入 query 部分，来实现传参效果，如：传入 "?foo=bar"
        WXMediaMessage mediaMessage = new WXMediaMessage(miniProgram);
        mediaMessage.title = "亲，请查看您的绿植设计方案~" + designLists.get(position).getNumber();//自定义
        mediaMessage.description = designLists.get(position).getNumber();//自定义
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

//        Bitmap bitmap = BitmapFactory.decodeResource(DesignActivity.this.getResources(), R.mipmap.share_img);
//        Bitmap sendBitmap = Bitmap.createScaledBitmap(bitmap, 200, 200, true);
//        bitmap.recycle();
//        mediaMessage.thumbData = ImageDispose.Bitmap2Bytes(sendBitmap);
//        SendMessageToWX.Req req = new SendMessageToWX.Req();
//        req.transaction = "";
//        req.scene = SendMessageToWX.Req.WXSceneSession;
//        req.message = mediaMessage;
//        YiBaiApplication.getIWXAPI().sendReq(req);
    }

    @Override
    public void onDesignSchemeImage(DesignList.DataBean.ListBean.SchemelistBean schemelistBean,
                                    boolean sceneInfoFlag) {
        if (sceneInfoFlag) {
            mSchemelistBean = schemelistBean;
            mUserPresenter.findUserSceneListInfo();
        }
//        if (sceneInfo != null) {
//            sceneInfo.setEditScene(true);
//            mUserPresenter.updateUserScene(sceneInfo);
//        } else {
//            mUserPresenter.findUserSceneListInfo();
//        }
//        if (sceneInfoFlag) {
//            for (Iterator<DesignList.DataBean.ListBean.SchemelistBean.ImglistBean> iterator = schemelistBean.getImglist().iterator(); iterator.hasNext(); ) {
//                DesignList.DataBean.ListBean.SchemelistBean.ImglistBean bean = iterator.next();
//                if (bean.getId().equals(imglistBean.getId())) {
//                    bean.setPicFlag(true);
//                    break;
//                }
//            }
//            existSceneInfoPopupWindow(schemelistBean, null);
//        }
////        else {
////            mDesignPresenter.updateSceneInfo(schemelistBean);
////        }
    }

    @Override

    public void onUpdateSceneInfo() {
        Intent intent = new Intent(DesignActivity.this, SceneActivity.class);
        startActivity(intent);
    }

    @Override
    public void onFindUserSceneInfoSuccess(List<SceneInfo> sceneInfoList) {
        if (sceneInfoList != null && sceneInfoList.size() > 0) {
            for (Iterator<SceneInfo> iterator = sceneInfoList.iterator(); iterator.hasNext(); ) {
                SceneInfo info = iterator.next();
                if (mSchemelistBean.getSchemeId().equals(String.valueOf(info.getScheme_id()))) {
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
        Intent intent = new Intent(DesignActivity.this, SceneActivity.class);
        startActivity(intent);
    }

    @Override
    public void onUpdateUserSceneSuccess() {

    }

    private void existSceneInfoPopupWindow(DesignList.DataBean.ListBean.SchemelistBean schemelistBean, SceneInfo sceneInfo) {
        if (null == mPopupWindow) {
            mSchemelistBean = schemelistBean;
            View view = getLayoutInflater().inflate(R.layout.popup_window_exist_scene_layout, null);
            mPopupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            TextView existSceneCancel = view.findViewById(R.id.existSceneCancel);
            TextView existSceneContinue = view.findViewById(R.id.existSceneContinue);
            existSceneCancel.setOnClickListener(v -> {
                if (null != mPopupWindow && mPopupWindow.isShowing()) {
                    mPopupWindow.dismiss();
                }
            });
            existSceneContinue.setOnClickListener(v -> {
                if (null != mPopupWindow && mPopupWindow.isShowing()) {
                    mPopupWindow.dismiss();
                    if (schemelistBean == null) {
                        addScene();
                    } else {
                        if (sceneInfo != null) {
                            sceneInfo.setEditScene(true);
                            mUserPresenter.updateUserScene(sceneInfo);
                        } else {
                            mUserPresenter.findUserSceneListInfo();
                        }
                    }
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
            mPopupWindow.showAtLocation(mRootLayout, Gravity.CENTER, 0, 0);
        } else {
            // 在弹出PopupWindow设置屏幕透明度
            OtherUtil.setBackgroundAlpha(this, 0.6f);
            // 添加PopupWindow窗口关闭事件
            mPopupWindow.setOnDismissListener(OtherUtil.popupDismissListener(this, 1f));
            mPopupWindow.showAtLocation(mRootLayout, Gravity.CENTER, 0, 0);
        }
    }


    private void addScene() {
        Intent intent = new Intent(DesignActivity.this, SceneActivity.class);
        intent.putExtra(DESIGN_CREATE, true);
        startActivity(intent);
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

    @OnClick({R.id.backImageView, R.id.createDesign, R.id.checkDesignDrawing})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backImageView:
                onBackPressed();
                break;
            case R.id.createDesign:
                if (sceneInfo != null) {
                    existSceneInfoPopupWindow(null, sceneInfo);
                } else {
                    addScene();
                }
                break;
            case R.id.checkDesignDrawing:
                Intent intent = new Intent(DesignActivity.this, SimulationDrawingActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(DesignActivity.this, MainActivity.class);
        startActivity(intent);
        super.onBackPressed();
    }
}
