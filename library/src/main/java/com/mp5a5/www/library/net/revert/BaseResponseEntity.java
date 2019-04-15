package com.mp5a5.www.library.net.revert;


import com.mp5a5.www.library.utils.ApiConfig;

import java.io.Serializable;

/**
 * @author ：mp5a5 on 2018/12/28 13：37
 * @describe：网络请求返回值
 * @email：wwb199055@126.com
 */
public class BaseResponseEntity<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    public int code;

    public String msg;

    public boolean success() {
        return ApiConfig.getSucceedCode() == code;
    }

    public boolean tokenInvalid() {
        return ApiConfig.getInvalidateToken() == code;
    }

    public T data;

}
