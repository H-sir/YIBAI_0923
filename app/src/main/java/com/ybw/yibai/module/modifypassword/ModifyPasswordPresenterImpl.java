package com.ybw.yibai.module.modifypassword;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.widget.TextView;

import com.ybw.yibai.R;
import com.ybw.yibai.base.BasePresenterImpl;
import com.ybw.yibai.common.bean.ModifyPassword;
import com.ybw.yibai.common.bean.VerificationCode;
import com.ybw.yibai.common.utils.MessageUtil;
import com.ybw.yibai.common.utils.NetworkStateUtil;
import com.ybw.yibai.module.modifypassword.ModifyPasswordContract.CallBack;
import com.ybw.yibai.module.modifypassword.ModifyPasswordContract.ModifyPasswordModel;
import com.ybw.yibai.module.modifypassword.ModifyPasswordContract.ModifyPasswordPresenter;
import com.ybw.yibai.module.modifypassword.ModifyPasswordContract.ModifyPasswordView;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

import static com.ybw.yibai.common.constants.Encoded.CODE_SUCCEED;

/**
 * 通过手机号码修改密码Presenter实现类
 *
 * @author sjl
 */
public class ModifyPasswordPresenterImpl extends BasePresenterImpl<ModifyPasswordView> implements ModifyPasswordPresenter, CallBack {

    /**
     * 获取验证码TextView
     */
    private TextView mGetVerificationCodeTextView;

    /**
     * PresenterImpl 持有 View的接口引用
     */
    private ModifyPasswordView mModifyPasswordView;

    /**
     * PresenterImpl 持有 Model的接口引用
     */
    private ModifyPasswordModel mModifyPasswordModel;

    /**
     * 构造方法
     *
     * @param view View的对象
     */
    public ModifyPasswordPresenterImpl(ModifyPasswordView view) {
        super(view);

        // 调用父类的方法获取View的对象
        this.mModifyPasswordView = getView();
        mModifyPasswordModel = new ModifyPasswordModelImpl();
    }

    /**
     * 获取验证码
     *
     * @param getVerificationCodeTextView 获取验证码的TextView
     * @param phoneNumber                 手机号
     */
    @Override
    public void getVerificationCode(TextView getVerificationCodeTextView, String phoneNumber) {
        Activity activity = (Activity) mModifyPasswordView;
        if (!NetworkStateUtil.isNetworkAvailable(activity)) {
            // 没有网络
            MessageUtil.showNoNetwork();
            return;
        }
        if (TextUtils.isEmpty(phoneNumber)) {
            // 手机号码为空
            MessageUtil.showMessage(activity.getResources().getString(R.string.phone_number_is_empty));
            return;
        }
        // 获取验证码
        mModifyPasswordModel.getVerificationCode(phoneNumber, this);
        mGetVerificationCodeTextView = getVerificationCodeTextView;
    }

    /**
     * 在获取验证码成功时回调
     *
     * @param verificationCode 验证码
     */
    @Override
    public void onGetVerificationCodeSuccess(VerificationCode verificationCode) {
        mModifyPasswordView.onGetVerificationCodeSuccess(verificationCode);
        if (CODE_SUCCEED != verificationCode.getCode()) {
            return;
        }
        Activity activity = (Activity) mModifyPasswordView;
        // 设置60秒计数
        final long count = 60;
        Function<Long, Long> function = new Function<Long, Long>() {
            @Override
            public Long apply(@NonNull Long aLong) throws Exception {
                // 由于是倒计时,需要将倒计时的数字反过来
                return count - aLong;
            }
        };
        Consumer<Disposable> consumer = new Consumer<Disposable>() {
            @Override
            public void accept(@NonNull Disposable disposable) throws Exception {
                mGetVerificationCodeTextView.setEnabled(false);
                int color = ContextCompat.getColor(activity, R.color.prompt_low_text_color);
                mGetVerificationCodeTextView.setTextColor(color);
            }
        };
        Observer<Long> observer = new Observer<Long>() {
            @Override
            public void onSubscribe(Disposable disposable) {
            }

            @Override
            public void onNext(Long aLong) {
                if (aLong == 0) {
                    mGetVerificationCodeTextView.setText(activity.getResources().getString(R.string.reacquire));
                } else {
                    String string = activity.getResources().getString(R.string.reacquire) + aLong;
                    mGetVerificationCodeTextView.setText(string);
                }
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {
                mGetVerificationCodeTextView.setEnabled(true);
                int color = ContextCompat.getColor(activity, R.color.colorPrimary);
                mGetVerificationCodeTextView.setTextColor(color);
            }
        };
        Observable
                .interval(0, 1, TimeUnit.SECONDS)
                .take(count + 1)
                .map(function)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(consumer)
                .subscribe(observer);
    }

    /**
     * 修改密码
     *
     * @param phoneNumber          手机号
     * @param verificationCode     短信验证码
     * @param newPassword          新密码
     * @param determineNewPassword 确认密码
     */
    @Override
    public void modifyPassword(String phoneNumber, String verificationCode, String newPassword, String determineNewPassword) {
        Activity activity = (Activity) mModifyPasswordView;
        if (TextUtils.isEmpty(phoneNumber) || TextUtils.isEmpty(verificationCode) || TextUtils.isEmpty(newPassword) || TextUtils.isEmpty(determineNewPassword)) {
            MessageUtil.showMessage(activity.getResources().getString(R.string.the_input_content_has_empty_data));
        } else if (!newPassword.equals(determineNewPassword)) {
            MessageUtil.showMessage(activity.getResources().getString(R.string.inconsistent_password_entered_twice));
        } else {
            mModifyPasswordModel.modifyPassword(verificationCode,newPassword,this);
        }
    }

    /**
     * 在修改账号密码成功时回调
     *
     * @param modifyPassword 在修改账号密码成功时服务器端返回的数据
     */
    @Override
    public void onModifyPasswordSuccess(ModifyPassword modifyPassword) {
        mModifyPasswordView.onModifyPasswordSuccess(modifyPassword);
    }
}
