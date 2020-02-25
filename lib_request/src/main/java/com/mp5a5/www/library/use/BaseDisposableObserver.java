package com.mp5a5.www.library.use;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.Toast;
import com.google.gson.JsonParseException;
import com.mp5a5.www.library.net.dialog.CustomProgressDialogUtils;
import com.mp5a5.www.library.net.revert.BaseResponseEntity;
import com.mp5a5.www.library.utils.ApiConfig;
import com.mp5a5.www.library.utils.AppContextUtils;
import com.mp5a5.www.library.utils.VariableUtils;
import io.reactivex.observers.DisposableObserver;
import org.json.JSONException;

import java.io.InterruptedIOException;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.text.ParseException;

/**
 * @author ：mp5a5 on 2019/4/14 14：35
 * @describe : Observable 通过subscribeWith接收并返回Disposable对象，经过CompositeDisposable管理Disposable对象，参考{@link BaseDisposableSubscriber}
 * @email：wwb199055@126.com
 */

public abstract class BaseDisposableObserver<T extends BaseResponseEntity> extends DisposableObserver<T> {

    /**
     * dialog 显示文字
     */
    private String mMsg;
    private CustomProgressDialogUtils progressDialogUtils;
    private Context mContext;
    private boolean mShowLoading = false;

    /**
     * token失效 发送广播标识
     */
    public static final String TOKEN_INVALID_TAG = "token_invalid";
    public static final String REFRESH_TOKEN = "refresh_token";

    /**
     * 退出app 发送广播标识
     */
    public static final String QUIT_APP_TAG = "quit_app_tag";
    public static final String QUIT_APP = "quit_app";

    private static final String CONNECT_ERROR = "网络连接失败,请检查网络";
    private static final String CONNECT_TIMEOUT = "连接超时,请稍后再试";
    private static final String BAD_NETWORK = "服务器异常";
    private static final String PARSE_ERROR = "解析服务器响应数据失败";
    private static final String UNKNOWN_ERROR = "未知错误";
    private static final String RESPONSE_RETURN_ERROR = "服务器返回数据失败";

    public BaseDisposableObserver() {
    }

    /**
     * 如果传入上下文，那么表示您将开启自定义的进度条
     *
     * @param context 上下文
     */
    public BaseDisposableObserver(Context context, boolean isShow) {
        this.mContext = context;
        this.mShowLoading = isShow;
    }

    /**
     * 如果传入上下文，那么表示您将开启自定义的进度条
     *
     * @param context 上下文
     */
    public BaseDisposableObserver(Context context, boolean isShow, String msg) {
        this.mContext = context;
        this.mShowLoading = isShow;
        this.mMsg = msg;
    }

    @Override
    protected void onStart() {
        super.onStart();
        onRequestStart();
    }


    @Override
    public void onNext(T response) {
        if (response.success()) {
            try {
                onSuccess(response);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (response.tokenInvalid()) {
            //token失效捕捉，发送广播，在项目中接收该动态广播然后做退出登录等一些列操作
            VariableUtils.receiveTokenCount.incrementAndGet();
            if (1 == VariableUtils.receiveTokenCount.get()) {
                sendTokenInvalidBroadcast();
            } else if (VariableUtils.receiveTokenCount.get() > 1) {
                if (System.currentTimeMillis() - VariableUtils.tokenInvalidIncTime.get() > 1000) {
                    sendTokenInvalidBroadcast();
                }
            }
            VariableUtils.tokenInvalidIncTime.set(System.currentTimeMillis());
        } else if (response.quitApp()) {
            VariableUtils.receiveQuitAppCount.incrementAndGet();
            if (1 == VariableUtils.receiveQuitAppCount.get()) {
                sendQuiteAppBroadcast();
            } else if (VariableUtils.receiveQuitAppCount.get() > 1) {
                if (System.currentTimeMillis() - VariableUtils.quitAppIncTime.get() > 1000) {
                    sendQuiteAppBroadcast();
                }
            }
            VariableUtils.quitAppIncTime.set(System.currentTimeMillis());
        } else {
            try {
                onFailing(response);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void sendTokenInvalidBroadcast() {
        Intent intent = new Intent();
        intent.setAction(ApiConfig.getTokenInvalidBroadcastFilter());
        intent.putExtra(TOKEN_INVALID_TAG, REFRESH_TOKEN);
        AppContextUtils.getContext().sendBroadcast(intent);
    }

    private void sendQuiteAppBroadcast() {
        Intent intent = new Intent();
        intent.setAction(ApiConfig.getQuitBroadcastFilter());
        intent.putExtra(QUIT_APP_TAG, QUIT_APP);
        AppContextUtils.getContext().sendBroadcast(intent);
    }


    @Override
    public void onError(Throwable e) {
        onRequestEnd();
        if (e instanceof retrofit2.HttpException) {
            //HTTP错误
            onException(ExceptionReason.BAD_NETWORK);
        } else if (e instanceof ConnectException || e instanceof UnknownHostException) {
            //连接错误
            onException(ExceptionReason.CONNECT_ERROR);
        } else if (e instanceof InterruptedIOException) {
            //连接超时
            onException(ExceptionReason.CONNECT_TIMEOUT);
        } else if (e instanceof JsonParseException || e instanceof JSONException || e instanceof ParseException) {
            //解析错误
            onException(ExceptionReason.PARSE_ERROR);
        } else {
            //其他错误
            onException(ExceptionReason.UNKNOWN_ERROR);
        }
    }

    private void onException(ExceptionReason reason) {
        switch (reason) {
            case CONNECT_ERROR:
                Toast.makeText(AppContextUtils.getContext(), CONNECT_ERROR, Toast.LENGTH_SHORT).show();
                break;

            case CONNECT_TIMEOUT:
                Toast.makeText(AppContextUtils.getContext(), CONNECT_TIMEOUT, Toast.LENGTH_SHORT).show();
                break;

            case BAD_NETWORK:
                Toast.makeText(AppContextUtils.getContext(), BAD_NETWORK, Toast.LENGTH_SHORT).show();
                break;

            case PARSE_ERROR:
                Toast.makeText(AppContextUtils.getContext(), PARSE_ERROR, Toast.LENGTH_SHORT).show();
                break;

            case UNKNOWN_ERROR:
            default:
                Toast.makeText(AppContextUtils.getContext(), UNKNOWN_ERROR, Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onComplete() {
        onRequestEnd();
    }

    /**
     * 网络请求成功并返回正确值
     *
     * @param response 返回值
     */
    public abstract void onSuccess(T response);

    /**
     * 网络请求成功但是返回值是错误的
     *
     * @param response 返回值
     */
    public void onFailing(T response) {
        String message = response.getMsg();
        if (TextUtils.isEmpty(message)) {
            Toast.makeText(AppContextUtils.getContext(), RESPONSE_RETURN_ERROR, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(AppContextUtils.getContext(), message, Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 网络请求失败原因
     */
    public enum ExceptionReason {
        /**
         * 解析数据失败
         */
        PARSE_ERROR,
        /**
         * 网络问题
         */
        BAD_NETWORK,
        /**
         * 连接错误
         */
        CONNECT_ERROR,
        /**
         * 连接超时
         */
        CONNECT_TIMEOUT,
        /**
         * 未知错误
         */
        UNKNOWN_ERROR
    }

    /**
     * 网络请求开始
     */
    protected void onRequestStart() {
        if (mShowLoading) {
            showProgressDialog();
        }
    }

    /**
     * 网络请求结束
     */
    protected void onRequestEnd() {
        closeProgressDialog();
    }

    /**
     * 开启Dialog
     */
    private void showProgressDialog() {
        progressDialogUtils = new CustomProgressDialogUtils();
        if (TextUtils.isEmpty(mMsg)) {
            progressDialogUtils.showProgress(mContext);
        } else {
            progressDialogUtils.showProgress(mContext, mMsg);
        }
    }

    /**
     * 关闭Dialog
     */
    private void closeProgressDialog() {
        if (progressDialogUtils != null) {
            progressDialogUtils.dismissProgress();
        }
    }

}
