package com.mp5a5.www.library.net.revert

import com.mp5a5.www.library.utils.ApiConfig
import java.io.Serializable

/**
 * @author ：mp5a5 on 2019/4/16 19：00
 * @describe
 * @email：wwb199055@126.com
 */
open class BaseResponseEntity<T> : Serializable {

    open var code: Int = -1

    open var msg: String = ""

    companion object {
        private const val serialVersionUID = 1L
    }

    open fun success(): Boolean {
        return ApiConfig.getSucceedCode() == code
    }

    open fun tokenInvalid(): Boolean {
        return ApiConfig.getInvalidateToken() == code
    }

    var data: T? = null

}
