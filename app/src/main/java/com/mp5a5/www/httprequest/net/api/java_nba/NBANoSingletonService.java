package com.mp5a5.www.httprequest.net.api.java_nba;

import android.util.ArrayMap;
import com.mp5a5.www.httprequest.net.entity.NBAJEntity;
import com.mp5a5.www.library.use.RetrofitFactory;
import io.reactivex.Observable;

/**
 * @author ：mp5a5 on 2019/1/7 14：55
 * @describe
 * @email：wwb199055@126.com
 */
public class NBANoSingletonService {

    private NbaJavaApi mNBAApi;

    public NBANoSingletonService() {
        //涉及到动态切换BaseUrl则用new Service()，不适用单例模式
        mNBAApi = RetrofitFactory.getInstance().create("http://op.juhe.cn/", NbaJavaApi.class);
    }

    public Observable<NBAJEntity> getNBAInfo(String key) {
        ArrayMap<String, Object> arrayMap = new ArrayMap<String, Object>();
        arrayMap.put("key", key);
        return mNBAApi.getJONBAInfo(arrayMap);
    }
}
