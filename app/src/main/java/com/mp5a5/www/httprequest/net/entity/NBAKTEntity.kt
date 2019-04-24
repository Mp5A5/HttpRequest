package com.mp5a5.www.httprequest.net.entity

import com.google.gson.annotations.SerializedName
import com.mp5a5.www.library.net.revert.BaseResponseEntity

/**
 * @describe
 * @author ：mp5a5 on 2019/4/16 20：59
 * @email：wwb199055@126.com
 */
data class NBAKTEntity(
    @SerializedName("error_code") override var code: Int, @SerializedName("reason") override var msg: String, var result: ResultEntity?
) : BaseResponseEntity() {

    data class ResultEntity(
        var _id: String?,
        var title: String?,
        var pic: String?,
        var year: Int,
        var month: Int,
        var day: Int,
        var des: String?,
        var lunar: String?
    )
}