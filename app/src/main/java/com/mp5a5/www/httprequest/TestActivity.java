package com.mp5a5.www.httprequest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;
import androidx.annotation.Nullable;
import com.mp5a5.www.httprequest.net.api.java_nba.NBANoSingletonService;
import com.mp5a5.www.httprequest.net.entity.NBAJEntity;
import com.mp5a5.www.library.use.BaseObserver;
import com.mp5a5.www.library.utils.ApiConfig;
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author ：mp5a5 on 2019/1/7 14：03
 * @describe
 * @email：wwb199055@126.com
 */
public class TestActivity extends RxAppCompatActivity {

    private QuitAppReceiver mQuitAppReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initReceiver();


        new NBANoSingletonService()
                .getNBAInfo("6949e822e6844ae6453fca0cf83379d3")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.bindToLifecycle())
                .subscribe(new BaseObserver<NBAJEntity>(this, true) {
                    @Override
                    public void onSuccess(NBAJEntity response) {
                        Toast.makeText(TestActivity.this, response.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                });


    }

    private void initReceiver() {
        mQuitAppReceiver = new QuitAppReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ApiConfig.getTokenInvalidBroadcastFilter());
        registerReceiver(mQuitAppReceiver, filter);
    }


    private class QuitAppReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (ApiConfig.getTokenInvalidBroadcastFilter().equals(intent.getAction())) {

                String msg = intent.getStringExtra(BaseObserver.TOKEN_INVALID_TAG);
                if (!TextUtils.isEmpty(msg)) {
                    Toast.makeText(TestActivity.this, msg, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}

