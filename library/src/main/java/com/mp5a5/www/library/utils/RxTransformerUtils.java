package com.mp5a5.www.library.utils;

import io.reactivex.FlowableTransformer;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author ：mp5a5 on 2019/4/14 17：33
 * @describe 用于RxJava线程切换
 * @email：wwb199055@126.com
 */
public class RxTransformerUtils {
    public static <T> ObservableTransformer<T, T> observableTransformer() {
        return tObservable -> tObservable.subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public static <T> FlowableTransformer<T, T> flowableTransformer() {
        return upstream -> upstream.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }
}
