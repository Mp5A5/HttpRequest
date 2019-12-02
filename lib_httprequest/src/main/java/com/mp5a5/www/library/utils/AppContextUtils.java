package com.mp5a5.www.library.utils;

import android.annotation.SuppressLint;
import android.content.Context;

/**
 * @author ：mp5a5 on 2018/12/28 11：04
 * @describe 上下文工具类 {@link ApiConfig init()方法中默认初始化}
 * @email：wwb199055@126.com
 */
public final class AppContextUtils {

    @SuppressLint("StaticFieldLeak")
    private static Context mContext;

    private AppContextUtils() {
        throw new UnsupportedOperationException("You can't instantiate me...");
    }

    /**
     * 初始化工具类
     *
     * @param context 上下文
     */
    public static void init(Context context) {
        AppContextUtils.mContext = context.getApplicationContext();
    }

    /**
     * 获取ApplicationContext
     *
     * @return ApplicationContext
     */
    public static Context getContext() {
        if (mContext != null) return mContext;
        throw new NullPointerException("You must init first");
    }
}
