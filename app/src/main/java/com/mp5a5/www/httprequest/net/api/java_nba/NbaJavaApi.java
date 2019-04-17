package com.mp5a5.www.httprequest.net.api.java_nba;

import android.util.ArrayMap;
import com.mp5a5.www.httprequest.net.entity.NBAJEntity;
import com.mp5a5.www.httprequest.net.entity.NBAKTEntity;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * @author ：mp5a5 on 2019/1/16 10：08
 * @describe
 * @email：wwb199055@126.com
 */
public interface NbaJavaApi {

    @GET("onebox/basketball/nba")
    Observable<NBAJEntity> getJONBAInfo(@QueryMap ArrayMap<String, Object> map);

    @GET("onebox/basketball/nba")
    Flowable<NBAKTEntity> getNBAJFTInfo(@QueryMap ArrayMap<String, Object> map);
}
