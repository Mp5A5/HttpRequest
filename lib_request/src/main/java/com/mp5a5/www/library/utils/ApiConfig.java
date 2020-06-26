package com.mp5a5.www.library.utils;

import android.content.Context;
import android.util.ArrayMap;

import java.io.Serializable;
import java.util.List;

import io.reactivex.annotations.NonNull;
import okhttp3.Interceptor;

/**
 * @author ：mp5a5 on 2018/12/27 20：51
 * @describe 网络请求配置文件
 * @email：wwb199055@126.com
 */
public class ApiConfig implements Serializable {

    private static int mInvalidToken;
    private static int mQuitCode;
    private static String mBaseUrl;
    private static int mDefaultTimeout = 2000;
    private static int mSucceedCode;
    private static String mTokenInvalidBroadcastFilter;
    private static String mQuitBroadcastFilter;
    private static ArrayMap<String, String> mHeads;
    private static String mToken = "";
    private static boolean mOpenHttps;
    private static SslSocketConfigure mSslSocketConfigure;
    private static List<Interceptor> mInterceptors;

    private ApiConfig(Builder builder) {
        mInvalidToken = builder.invalidateToken;
        mQuitCode = builder.quitCode;
        mBaseUrl = builder.baseUrl;
        mDefaultTimeout = builder.defaultTimeout;
        mSucceedCode = builder.succeedCode;
        mTokenInvalidBroadcastFilter = builder.tokenInvalidBroadcastFilter;
        mQuitBroadcastFilter = builder.quitBroadcastFilter;
        mHeads = builder.heads;
        mOpenHttps = builder.openHttps;
        mSslSocketConfigure = builder.sslSocketConfigure;
        mInterceptors=builder.interceptors;
    }

    public void init(Context appContext) {
        AppContextUtils.init(appContext);
    }

    public static int getInvalidToken() {
        return mInvalidToken;
    }

    public static String getBaseUrl() {
        return mBaseUrl;
    }

    public static int getDefaultTimeout() {
        return mDefaultTimeout;
    }

    public static int getSucceedCode() {
        return mSucceedCode;
    }

    public static String getTokenInvalidBroadcastFilter() {
        return mTokenInvalidBroadcastFilter;
    }

    public static String getQuitBroadcastFilter() {
        return mQuitBroadcastFilter;
    }

    public static ArrayMap<String, String> getHeads() {
        return mHeads;
    }

    public static void setHeads(ArrayMap<String, String> mHeads) {
        ApiConfig.mHeads = mHeads;
    }

    public static String getToken() {
        return mToken;
    }

    public static void setToken(String mToken) {
        ApiConfig.mToken = mToken;
    }

    public static boolean getOpenHttps() {
        return mOpenHttps;
    }

    public static int getQuitCode() {
        return mQuitCode;
    }

    public static SslSocketConfigure getSslSocketConfigure() {
        return mSslSocketConfigure;
    }

    public static List<Interceptor> getInterceptors() {
        return mInterceptors;
    }

    public static final class Builder {

        private int invalidateToken;

        private int quitCode;

        private String baseUrl;

        private int defaultTimeout;

        private int succeedCode;

        private String quitBroadcastFilter;

        private String tokenInvalidBroadcastFilter;

        private ArrayMap<String, String> heads;

        private boolean openHttps = false;

        private SslSocketConfigure sslSocketConfigure;

        private List<Interceptor> interceptors;

        public Builder setHeads(ArrayMap<String, String> heads) {
            this.heads = heads;
            return this;
        }

        public Builder setTokenInvalidFilter(@NonNull String filter) {
            this.tokenInvalidBroadcastFilter = filter;
            return this;
        }

        public Builder setQuitFilter(@NonNull String filter) {
            this.quitBroadcastFilter = filter;
            return this;
        }


        public Builder setSucceedCode(int succeedCode) {
            this.succeedCode = succeedCode;
            return this;
        }

        public Builder setBaseUrl(String mBaseUrl) {
            this.baseUrl = mBaseUrl;
            return this;
        }

        public Builder setInvalidToken(int invalidateToken) {
            this.invalidateToken = invalidateToken;
            return this;
        }

        public Builder setQuitCode(int quitCode) {
            this.quitCode = quitCode;
            return this;
        }

        public Builder setDefaultTimeout(int defaultTimeout) {
            this.defaultTimeout = defaultTimeout;
            return this;
        }

        public Builder setOpenHttps(boolean open) {
            this.openHttps = open;
            return this;
        }

        public Builder setSslSocketConfigure(SslSocketConfigure sslSocketConfigure) {
            this.sslSocketConfigure = sslSocketConfigure;
            return this;
        }

        public Builder setInterceptor(List<Interceptor> interceptors){
            this.interceptors= interceptors;
            return this;
        }

        public ApiConfig build() {
            return new ApiConfig(this);
        }
    }

    public enum QuitType {

        /**
         * 退出app标志位
         */
        QUIT_APP_TAG(0, "quit_app_tag"),

        /**
         * 退出app event
         */
        QUIT_APP(1, "quit_app");

        public static final String TOKEN_INVALID_TAG = "token_invalid";
        public static final String REFRESH_TOKEN = "refresh_token";

        private int code;
        private String name;

        QuitType(int code, String name) {
            this.code = code;
            this.name = name;
        }


        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public enum TokenType {

        /**
         * 退出token失效标志位
         */
        TOKEN_INVALID_TAG(0, "token_invalid_tag"),

        /**
         * 刷新token
         */
        TOKEN_REFRESH(1, "refresh_token");

        private int code;
        private String name;

        TokenType(int code, String name) {
            this.code = code;
            this.name = name;
        }


        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
