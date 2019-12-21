package com.mp5a5.www.httprequest

import android.app.Application
import android.util.ArrayMap
import com.mp5a5.www.library.utils.ApiConfig
import com.mp5a5.www.library.utils.SslSocketConfigure

/**
 * @author ：mp5a5 on 2018/12/28 15：12
 * @describe
 * @email：wwb199055@126.com
 */
class TestKTApp : Application() {
    override fun onCreate() {
        super.onCreate()

        val baseUrl = "http://op.juhe.cn/"
        val headMap = ArrayMap<String, String>()
        headMap["key1"] = "value1"
        headMap["key2"] = "value2"
        headMap["key3"] = "value3"

        val sslSocketConfigure = SslSocketConfigure.Builder()
            .setVerifyType(2)//单向双向验证
            .setClientPriKey("client.bks")//客户端keystore名称
            .setTrustPubKey("truststore.bks")//受信任密钥库keystore名称
            .setClientBKSPassword("123456")//客户端密码
            .setTruststoreBKSPassword("123456")//受信任密钥库密码
            .setKeystoreType("BKS")//客户端密钥类型
            .setProtocolType("TLS")//协议类型
            .setCertificateType("X.509")//证书类型
            .build();


        val build = ApiConfig.Builder()
            .setBaseUrl(baseUrl)//BaseUrl，这个地方加入后项目中默认使用该url
            .setInvalidToken(10)//Token失效码
            .setSucceedCode(0)//成功返回码  NBA的测试返回成功code为0  上传图片返回code为200 由于是不同接口 请大家注意
            /*
             *    Token失效后发送动态广播，配合BaseObserver中的标识进行接收使用
             *    public static final String TOKEN_INVALID_TAG = "token_invalid";
             *    public static final String QUIT_APP = "quit_app";
             *    注册Token失效，退出登录的动态广播
             *    private inner class QuitAppReceiver : BroadcastReceiver() {
             *          override fun onReceive(context: Context?, intent: Intent?) {
             *               if (ApiConfig.getQuitBroadcastReceiverFilter() == intent?.action) {
             *
             *                   val msg = intent?.getStringExtra(BaseObserver.TOKEN_INVALID_TAG)
             *
             *                   if (!TextUtils.isEmpty(msg)) {
             *                       toast("$msg")
             *                       //Toast.makeText(this@MainActivity,msg,Toast.LENGTH_SHORT).show()
             *                  }
             *               }
             *           }
             *       }
             *
             *
             *
             *
             *   onCreate中
             *   private fun initReceiver() {
             *   val filter = IntentFilter()
             *   filter.addAction(ApiConfig.getQuitBroadcastReceiverFilter())
             *   registerReceiver(quitAppReceiver, filter)
             *   }
             */
            .setTokenInvalidFilter("com.mp5a5.quit.broadcastFilter")////失效广播Filter设置
            //.setDefaultTimeout(2000)//响应时间，可以不设置，默认为2000毫秒
            //.setHeads(headMap)//动态添加的header，也可以在其他地方通过ApiConfig.setHeads()设置
            //.setOpenHttps(true)//开启HTTPS验证
            //.setSslSocketConfigure(sslSocketConfigure)//HTTPS认证配置
            .build()
        build.init(this)

    }
}
