package com.mp5a5.www.httprequest;

import android.app.Application;
import android.util.ArrayMap;
import com.mp5a5.www.library.utils.ApiConfig;
import com.mp5a5.www.library.utils.SslSocketConfigure;

/**
 * @author ：mp5a5 on 2019/1/7 13：50
 * @describe
 * @email：wwb199055@126.com
 */

public class TestJApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        String baseUrl = "http://op.juhe.cn/";
        ArrayMap<String, String> headMap = new ArrayMap<String, String>();
        headMap.put("key1", "value1");
        headMap.put("key2", "value2");
        headMap.put("key3", "value3");

        SslSocketConfigure sslSocketConfigure = new SslSocketConfigure.Builder()
                .setVerifyType(2)//单向双向验证 1单向  2 双向
                .setClientPriKey("client.bks")//客户端keystore名称
                .setTrustPubKey("truststore.bks")//受信任密钥库keystore名称
                .setClientBKSPassword("123456")//客户端密码
                .setTruststoreBKSPassword("123456")//受信任密钥库密码
                .setKeystoreType("BKS")//客户端密钥类型
                .setProtocolType("TLS")//协议类型
                .setCertificateType("X.509")//证书类型
                .build();


        ApiConfig build = new ApiConfig.Builder()
                .setBaseUrl(baseUrl)//BaseUrl，这个地方加入后项目中默认使用该url
                .setInvalidToken(0)//Token失效码
                .setSucceedCode(200)//成功返回码  NBA的测试返回成功code为0  上传图片返回code为200 由于是不同接口 请大家注意
                .setTokenInvalidFilter("com.mp5a5.quit.tokenInvalidBroadcastFilter")//失效广播Filter设置
                .setQuitCode(200)//退出app码
                .setTokenInvalidFilter("com.mp5a5.quit.quitAppBroadcastFilter")//失效广播Filter设置
                //.setDefaultTimeout(2000)//响应时间，可以不设置，默认为2000毫秒
                .setHeads(headMap)//动态添加的header，也可以在其他地方通过ApiConfig.setHeads()设置
                //.setOpenHttps(true)//开启HTTPS验证
                //.setSslSocketConfigure(sslSocketConfigure)//HTTPS认证配置
                .build();
        /*
         *     Token失效后发送动态广播的Filter，配合BaseObserver中的标识进行接收使用
         *     public static final String TOKEN_INVALID_TAG = "token_invalid"; ------------>>>>>>>>>>对应name
         *     public static final String REFRESH_TOKEN = "refresh_token"; ------------>>>>>>>>>>对应value
         *
         *
         *     oncreate()方法中初始化
         *     private void initReceiver() {
         *         mQuitAppReceiver = new QuitAppReceiver();
         *         IntentFilter filter = new IntentFilter();
         *         filter.addAction(ApiConfig.getTokenInvalidBroadcastFilter());
         *         registerReceiver(mQuitAppReceiver, filter);
         *     }
         *
         *
         *     private class QuitAppReceiver extends BroadcastReceiver {
         *
         *         @Override
         *         public void onReceive(Context context, Intent intent) {
         *             if (ApiConfig.getTokenInvalidBroadcastFilter().equals(intent.getAction())) {
         *
         *                 String msg = intent.getStringExtra(BaseObserver.TOKEN_INVALID_TAG);
         *                 if (!TextUtils.isEmpty(msg)) {
         *                     Toast.makeText(TestActivity.this, msg, Toast.LENGTH_SHORT).show();
         *                 }
         *             }
         *         }
         *     }
         *
         */

        build.init(this);
    }
}
