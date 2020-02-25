package com.mp5a5.www.library.net.revert

import com.mp5a5.www.library.utils.ApiConfig
import java.io.Serializable

/**
 * @author ：mp5a5 on 2019/4/16 19：00
 * @describe
 * @email：wwb199055@126.com
 */
open class BaseResponseEntity : Serializable {

    open var code: Int = -1

    open var msg: String = ""

    companion object {
        private const val serialVersionUID = 1L
    }

    /**
     * 请求成功返回
     */
    open fun success(): Boolean {
        return ApiConfig.getSucceedCode() == code
    }

    /**
     * 请求返回token失效的code
     */
    open fun tokenInvalid(): Boolean {
        return ApiConfig.getInvalidToken() == code
    }

    /**
     * 请求返回退出APP的code
     */
    open fun quitApp(): Boolean {
        return ApiConfig.getQuitCode() == code
    }

}
