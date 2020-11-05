package com.ybw.yibai.module.design;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
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
import com.ybw.yibai.common.bean.BaseBean;
import com.ybw.yibai.common.bean.DesignList;
import com.ybw.yibai.common.bean.NetworkType;
import com.ybw.yibai.common.bean.SceneInfo;
import com.ybw.yibai.common.utils.PopupWindowUtil;
import com.ybw.yibai.common.utils.ExceptionUtil;
import com.ybw.yibai.common.utils.ImageDispose;
import com.ybw.yibai.common.utils.ImageUtil;
import com.ybw.yibai.common.utils.MessageUtil;
import com.ybw.yibai.common.utils.OtherUtil;
import com.ybw.yibai.common.utils.TimeUtil;
import com.ybw.yibai.common.widget.WaitDialog;
import com.ybw.yibai.common.widget.nestlistview.NestFullListView;
import com.ybw.yibai.common.widget.nestlistview.NestFullListViewAdapter;
import com.ybw.yibai.common.widget.nestlistview.NestFullViewHolder;
import com.ybw.yibai.module.drawing.SimulationDrawingActivity;
import com.ybw.yibai.module.main.MainActivity;
import com.ybw.yibai.module.scene.SceneActivity;
import com.ybw.yibai.module.user.UserContract;
import com.ybw.yibai.module.user.UserPresenterImpl;

import org.greenrobot.eventbus.EventBus;
import org.xutils.DbManager;
import org.xutils.ex.DbException;

import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.ybw.yibai.common.constants.HttpUrls.BASE_URL;
import static com.ybw.yibai.common.constants.Preferences.CUSTOMER_NAME;
import static com.ybw.yibai.common.constants.Preferences.DESIGN_CREATE;
import static com.ybw.yibai.common.constants.Preferences.ORDER_NUMBER;
import static com.ybw.yibai.common.constants.Preferences.ORDER_SHARE_URL_TYPE;
import static com.ybw.yibai.common.constants.Preferences.TYPE;
import static com.ybw.yibai.common.constants.Preferences.URL;
import static com.ybw.yibai.common.constants.Preferences.USER_INFO;
import static com.ybw.yibai.common.constants.Preferences.VIP_LEVEL;

/**
 * <pre>
 *     author : HKR
 *     time   : 2020/09/02
 *     desc   :
 * </pre>
 */
public class DesignActivity extends BaseActivity implements DesignContract.DesignView,
        UserContract.UserView {
    private String TAG = "DesignActivity";
    private DesignActivity mDesignActivity = null;
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
    NestFullListView designListView;

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

    /**
     * 设计详情列表适配器
     */
    private NestFullListViewAdapter mNestFullListViewAdapter;

    @Override
    protected int setLayout() {
        mDesignActivity = this;
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
    }

    @Override
    protected void initEvent() {
        mDesignPresenter.getEditSceneInfo();
    }

    @Override
    public void onFindEditSceneInfo(List<SceneInfo> defaultSceneInfoList) {
        if (defaultSceneInfoList != null && defaultSceneInfoList.size() > 0)
            sceneInfo = defaultSceneInfoList.get(0);

        mDesignPresenter.getDesignList();
    }

    @Override
    public void onGetDesignListSuccess(DesignList designList) {
        mNestFullListViewAdapter = new NestFullListViewAdapter<DesignList.DataBean.ListBean>(
                R.layout.listview_design_list_item_layout, designList.getData().getList()) {
            @Override
            public void onBind(int pos, DesignList.DataBean.ListBean dataBean, NestFullViewHolder holder) {
                TextView mDesignName = holder.getView(R.id.designName);
                TextView mDesignStats = holder.getView(R.id.designStats);
                TextView mDesignDelete = holder.getView(R.id.designDelete);
                TextView mDesignShare = holder.getView(R.id.designShare);

                mDesignName.setText(dataBean.getNumber());
                String twoDay = TimeUtil.getTwoDay(TimeUtil.getStringToday(), dataBean.getLasttime());

                boolean sceneInfoFlag = false;
                boolean flag = false;
                for (Iterator<DesignList.DataBean.ListBean.SchemelistBean> iterator = dataBean.getSchemelist().iterator(); iterator.hasNext(); ) {
                    DesignList.DataBean.ListBean.SchemelistBean schemelistBean = iterator.next();
                    if (sceneInfo != null && schemelistBean.getSchemeId().equals(sceneInfo.getScheme_id())) {
                        mDesignStats.setText("正在编辑");
                        sceneInfoFlag = true;
                        flag = true;
                    }
                }
                if (!flag) {
                    if (twoDay != null && !twoDay.isEmpty() && Integer.parseInt(twoDay) < 5) {
                        mDesignStats.setText("完成于" + twoDay + "天前");
                    } else {
                        mDesignStats.setText(dataBean.getLasttime());
                    }
                }
                mDesignDelete.setOnClickListener(view -> {
                    onDesignDelete(dataBean);
                });
                mDesignShare.setOnClickListener(view -> {
                    onDesignShare(dataBean);
                });

                //第二层
                NestFullListView view = (NestFullListView) holder.getView(R.id.designListView);
                view.setOrientation(LinearLayout.HORIZONTAL);
                boolean finalSceneInfoFlag = sceneInfoFlag;
                view.setAdapter(new NestFullListViewAdapter<DesignList.DataBean.ListBean.SchemelistBean>
                        (R.layout.listview_design_scheme_list_item_layout,
                                dataBean.getSchemelist()) {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onBind(int pos, DesignList.DataBean.ListBean.SchemelistBean schemelistBean, NestFullViewHolder holder) {
                        ImageView mDesignSchemeImage = holder.getView(R.id.designSchemeImage);
                        TextView mDesignSchemeName = holder.getView(R.id.designSchemeName);

                        mDesignSchemeName.setText(schemelistBean.getSchemeName());

                        ImageUtil.displayImage(getApplicationContext(), mDesignSchemeImage, schemelistBean.getBgpic());
                        mDesignSchemeImage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                onDesignSchemeImage(schemelistBean, finalSceneInfoFlag);
                            }
                        });
                    }
                });
            }
        };
        designListView.setAdapter(mNestFullListViewAdapter);
    }

    @Override
    public void onNetworkStateChange(NetworkType networkType) {

    }

    /**
     * 调用删除按钮
     */
    public void onDesignDelete(DesignList.DataBean.ListBean dataBean) {
        deleteListBead = dataBean;
        mDesignPresenter.deleteDesign(deleteListBead);
    }

    /**
     * 删除接口返回
     */
    @Override
    public void onDeleteDesignSuccess(BaseBean baseBean) {
        try {
            DbManager dbManager = YiBaiApplication.getDbManager();
            // 查找当前正在编辑的这一个场景
            List<SceneInfo> defaultSceneInfoList = dbManager.selector(SceneInfo.class)
                    .where("number", "=", deleteListBead.getNumber())
                    .findAll();
            if (defaultSceneInfoList != null && defaultSceneInfoList.size() > 0) {
                dbManager.delete(defaultSceneInfoList);
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        mDesignPresenter.getDesignList();
    }

    /**
     * 调用分享按钮分享到微信小程序
     * <p>
     * SendMessageToWX.Req.WXSceneSession；分享到微信好友
     * SendMessageToWX.Req.WXSceneTimeline；分享到微信朋友圈
     * SendMessageToWX.Req.WXSceneFavorite；添加到微信收藏夹
     */
    public void onDesignShare(DesignList.DataBean.ListBean dataBean) {
        WXMiniProgramObject miniProgram = new WXMiniProgramObject();
        miniProgram.webpageUrl = BASE_URL;//兼容低版本的网页链接
        miniProgram.userName = "gh_a532df421aeb";//小程序端提供参数
        miniProgram.path = "/pages/index/index?number=" + dataBean.getNumber(); //小程序页面路径；对于小游戏，可以只传入 query 部分，来实现传参效果，如：传入 "?foo=bar"
        WXMediaMessage mediaMessage = new WXMediaMessage(miniProgram);
        mediaMessage.title = "亲，请查看您的绿植设计方案~" + dataBean.getNumber();//自定义
        mediaMessage.description = dataBean.getNumber();//自定义
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

    }

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

    private void existSceneInfoPopupWindow(DesignList.DataBean.ListBean.SchemelistBean
                                                   schemelistBean, SceneInfo sceneInfo) {
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
                SharedPreferences preferences = getSharedPreferences(USER_INFO, MODE_PRIVATE);
                int vipLevel = preferences.getInt(VIP_LEVEL, 0);
                if (1 == vipLevel) {
                    PopupWindowUtil.displayUpdateVipPopupWindow(mDesignActivity, mRootLayout);
                    return;
                }
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
        /**
         * 发送数据到{@link HomeFragment#onHome(String)}
         * 使其跳转到对应的Fragment
         */
        EventBus.getDefault().postSticky("Design");
        super.onBackPressed();
    }
}
