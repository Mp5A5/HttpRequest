package com.mp5a5.www.httprequest.net.api.kt_nba

import android.util.ArrayMap
import com.mp5a5.www.httprequest.net.entity.NBAJEntity
import com.mp5a5.www.httprequest.net.entity.NBAKTEntity
import com.mp5a5.www.library.use.RetrofitFactory
import io.reactivex.Observable

/**
 * @describe
 * @author ：mp5a5 on 2018/12/28 19：52
 * @email：wwb199055@126.com
 */
object NBAKTService {

    private val mNBAApi by lazy {
        RetrofitFactory.getInstance().create( NBAKTApi::class.java)
    }


    fun getJNBAInfo(key: String): Observable<NBAJEntity> {
        val arrayMap = ArrayMap<String, Any>()
        arrayMap["key"] = key
        return mNBAApi.getJNBAInfo(arrayMap)
    }

    fun getKtNBAInfo(key: String): Observable<NBAKTEntity> {
        val arrayMap = ArrayMap<String, Any>()
        arrayMap["key"] = key
        return mNBAApi.getKTNBAInfo(arrayMap)
    }
}