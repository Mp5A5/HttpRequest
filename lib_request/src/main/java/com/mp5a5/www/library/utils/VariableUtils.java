package com.mp5a5.www.library.utils;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author ：mp5a5 on 2019/3/15 10：59
 * @describe
 * @email：wwb199055@126.com
 */
public class VariableUtils {

    /**
     * 收到Token失效的时间记录值
     */
    public static AtomicLong tokenInvalidIncTime = new AtomicLong(0);

    /**
     * 接收到Token失效的次数
     */
    public static AtomicInteger receiveTokenCount = new AtomicInteger(0);

    /**
     * 接收到quit的次数
     */
    public static AtomicInteger receiveQuitAppCount = new AtomicInteger(0);

    public static AtomicLong quitAppIncTime = new AtomicLong(0);


}
