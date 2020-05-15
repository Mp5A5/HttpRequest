# 基于Retrofit2+RxJava+OkHttp3+RxLifecycle3的网络请求框架

### 添加依赖和配置

工程添加依赖仓库，Add the JitPack repository to your build file

```
allprojects {
   repositories {
   		...
   	    maven { url 'https://jitpack.io' }
   }
}
```

```
compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }
```

```
dependencies {
       implementation 'com.github.Mp5A5:HttpRequest:1.2.1'
}
```


如果项目使用RxLifecycle管理网络请求，则不需要添加rxjava和rxandroid依赖因为RxLifecycle本身包含这两个包：
 
* 如果项目是用的support包则使用RxLifecycle2及对应的版本

```
    implementation 'com.trello.rxlifecycle2:rxlifecycle:latest-version'
    implementation 'com.trello.rxlifecycle2:rxlifecycle-android:latest-version'
    implementation 'com.trello.rxlifecycle2:rxlifecycle-components:version'
```

* 如果项目是用的androidx包则使用RxLifecycle3及对应的版本

```
    implementation 'com.trello.rxlifecycle3:rxlifecycle:latest-version'
    implementation 'com.trello.rxlifecycle3:rxlifecycle-android:latest-version'
    implementation 'com.trello.rxlifecycle3:rxlifecycle-components:latest-version'
```

* 如果项目不使用RxLifecycle管理网络请求，而是通过手动管理，则不需要添加RxLifecycle对应的包

推荐依赖
```
    // okhttp
    implementation 'com.squareup.okhttp3:okhttp:latest-version'
    implementation 'com.squareup.okhttp3:logging-interceptor:latest-version'
    //Retrofit
    implementation 'com.squareup.retrofit2:retrofit:latest-version'
    implementation 'com.squareup.retrofit2:converter-gson:latest-version'
    implementation 'com.squareup.retrofit2:adapter-rxjava2:latest-version'
    //RxLifecycle3
    implementation 'com.trello.rxlifecycle3:rxlifecycle:latest-version'
    implementation 'com.trello.rxlifecycle3:rxlifecycle-android:latest-version'
    implementation 'com.trello.rxlifecycle3:rxlifecycle-components:latest-version'
```
或者
```
    // rxandroid
    implementation 'io.reactivex.rxjava2:rxandroid:latest-version'
    // okhttp
    implementation 'com.squareup.okhttp3:okhttp:latest-version'
    implementation 'com.squareup.okhttp3:logging-interceptor:latest-version'
    //Retrofit
    implementation 'com.squareup.retrofit2:retrofit:latest-version'
    implementation 'com.squareup.retrofit2:converter-gson:latest-version'
    implementation 'com.squareup.retrofit2:adapter-rxjava2:latest-version'
```

### 简单使用步骤

#### 在Application类中进行初始化操作

```
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
                .setInvalidToken(10)//Token失效码
                .setSucceedCode(0)//成功返回码  NBA的测试返回成功code为0  上传图片返回code为200 由于是不同接口 请大家注意
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

```

#### 定义请求接口

```
public interface NBAApiT {

    @GET("onebox/basketball/nba")
    Observable<NBAEntity> getNBAInfo(@QueryMap ArrayMap<String, Object> map);
}
```

#### 创建请求实例

* 单例模式创建Service(推荐使用这种)

```java
public class NbaService {


    private NBAApiT nbaApiT;

    private NbaService() {
        nbaApiT = RetrofitFactory.getInstance().create(NBAApiT.class);
    }

    public static NbaService getInstance() {
        return NbaServiceHolder.S_INSTANCE;
    }

    private static class NbaServiceHolder {
        private static final NbaService S_INSTANCE = new NbaService();
    }


    public Observable<NBAEntity> getNBAInfo(String key) {
        ArrayMap<String, Object> arrayMap = new ArrayMap<String, Object>();
        arrayMap.put("key", key);
        return nbaApiT.getNBAInfo(arrayMap);
    }

}
```

* 使用new Service创建Service，这中用来做动态切换BaseUrl测试等

```Java
public class NBAService {

    private NBAApi mNbaApi;

    public NBAServiceTT(String baseUrl) {
        //涉及到动态切换BaseUrl则用new Service()，不使用单例模式
        mNbaApi = RetrofitFactory.getInstance().create(baseUrl, NBAApi.class);
    }

    public Observable<NBAEntity> getNBAInfo(String key) {
        ArrayMap<String, Object> arrayMap = new ArrayMap<String, Object>();
        arrayMap.put("key", key);
        return mNbaApi.getNBAInfo(arrayMap);
    }
}
```

#### 设置返回接收参数

实体类必须继承BaseResponseEntity

* 返回的Json中，如果错误码和错误日志在最外层，则直接继承BaseResponseEntity

```
{"code":200,"msg":"success","response":"2018"}
```

* 返回的Json中，如果错误码和错误码日志在最外层，并且错误码不叫code或者错误日志码不叫msg,则直接继承BaseResponseEntity,并且通过@SerializedName("value")起别名的方式重新赋值错误码或者错误日志码,然后重写success和tokenInvalid的方法

```
{"status":200,"msg":"success","response":"2018"}
```

```
public class XXEntity extends BaseResponseEntity {


    @SerializedName("status")
    private int code;

    @Override
    public boolean success() {
        return ApiConfig.getSucceedCode() == code;
    }

    @Override
    public boolean tokenInvalid() {
        return ApiConfig.getInvalidateToken() == code;
    }
    
    public String response;
}

// 或者使用泛型包装
public class XXEntity<T> extends BaseResponseEntity {


    @SerializedName("status")
    private int code;

    @Override
    public boolean success() {
        return ApiConfig.getSucceedCode() == code;
    }

    @Override
    public boolean tokenInvalid() {
        return ApiConfig.getInvalidateToken() == code;
    }
    
    public T response;
}

// kotlin也可以使用@SerializedName("value")然后选择重写code或者msg
data class XXEntity(
    @SerializedName("error_code") override var code: Int, @SerializedName("reason") override var msg: String, var result: ResultEntity?
) : BaseResponseEntity<NBAKTEntity>()

```

* 返回的Json中，如果错误码和错误日志不在最外层，继承BaseResponseEntity,重写success和tokenInvalid方法

```
{"responseHeader":{"status":200,"msg":"success"},"response":"success"}
```

```
public class XXEntity<T> extends BaseResponseEntity {

    public T response;

    private ResponseHeader responseHeader;

    public class ResponseHeader {
        public int status;
        public String msg;
    }

    @Override
    public int getCode() {
        return responseHeader.status;
    }

    @NotNull
    @Override
    public String getMsg() {
        return responseHeader.msg;
    }

    @Override
    public boolean success() {
        return ApiConfig.getSucceedCode() == responseHeader.status;
    }

    @Override
    public boolean tokenInvalid() {
        return ApiConfig.getInvalidateToken() == responseHeader.status;
    }

}
```
#### 发送请求,接收参数

```
NbaService.getInstance()
        .getNBAInfo("6949e822e6844ae6453fca0cf83379d3")
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .compose(this.bindToLifecycle())
        .subscribe(new BaseObserver<NBAEntity>(){

            @Override
            public void onSuccess(NBAEntity response) {
                Toast.makeText(TestNBAActivity.this, response.result.title, Toast.LENGTH_SHORT).show();
            }

        });


```

* 如果想使用系统提供的Dialog,但是重写了onError方法但是没有使用super.onError(e),那么必须调用onRequestEnd()方法，不然Dialog是不会消失的，至于原因自己参照Java多态机制；

```java
UploadManager
            .getInstance()
            .uploadMultiPicList(list)
            .subscribe(parts -> {
                UploadService.getInstance()
                        .uploadPic(parts)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .compose(this.bindToLifecycle())
                        .subscribe(new BaseObserver<BaseResponseEntity>(this, true) {
                            @Override
                            public void onSuccess(BaseResponseEntity response) {
                                Toast.makeText(MainActivity.this, response.getMsg(), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onError(Throwable e) {
                                //super.onError(e);
                                onRequestEnd();
                                Toast.makeText(MainActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                            }
                        });


            });
```

* 如果想使用自己自定义的dialog，则需要在需要重写onRequestStart()和onRequestEnd()两个方法，onRequestStart()用来开启加载动画，onRequestEnd()用来关闭加载动画；

```java
UploadService.getInstance()
            .uploadPic(parts)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .compose(this.bindToLifecycle())
            .subscribe(new BaseObserver<BaseResponseEntity>() {
                
                @Override
                protected void onRequestStart() {
                  //super.onRequestStart();
                  // loadStart....
                  showLoading();
                }
                
                @Override
                protected void onRequestEnd() {
                   //super.onRequestEnd();
                   // loadEnd....
                   cancelLoading();
                }
                
                @Override
                public void onSuccess(BaseResponseEntity response) {
                    ...
                }
                
            });
```

### 效果展示

![show.gif](img/show.gif)

### 参考资料请移交 https://www.jianshu.com/p/181227ca8a4d

