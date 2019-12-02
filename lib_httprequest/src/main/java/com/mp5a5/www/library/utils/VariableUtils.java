package com.mp5a5.www.library.utils;

/**
 * @author ：mp5a5 on 2019/3/15 10：59
 * @describe
 * @email：wwb199055@126.com
 */
public class VariableUtils {

    /**
     * 收到Token失效的时间记录值
     */
    public static volatile long temp_system_time = 0;

    /**
     * 接收到Token失效的次数
     */
    public static volatile int receive_token_count = 0;
}
