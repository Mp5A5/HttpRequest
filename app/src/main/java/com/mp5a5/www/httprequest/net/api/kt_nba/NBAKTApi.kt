package com.mp5a5.www.httprequest.net.api.kt_nba

import android.util.ArrayMap
import com.mp5a5.www.httprequest.net.entity.NBAJEntity
import com.mp5a5.www.httprequest.net.entity.NBAKTEntity
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.QueryMap

/**
 * @describe
 * @author ：mp5a5 on 2018/12/28 19：49
 * @email：wwb199055@126.com
 */
interface NBAKTApi {

    @GET("onebox/basketball/nba")
    fun getJNBAInfo(@QueryMap map: ArrayMap<String, @JvmSuppressWildcards Any>): Observable<NBAJEntity>

    @GET("onebox/basketball/nba")
    fun getKTNBAInfo(@QueryMap map: ArrayMap<String, @JvmSuppressWildcards Any>): Observable<NBAKTEntity>
}