package com.mp5a5.www.library.net.https;

import android.annotation.SuppressLint;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

/**
 * @author ：mp5a5 on 2019/1/4 14：50
 * @describe
 * @email：wwb199055@126.com
 */
public class UnSafeHostnameVerify implements HostnameVerifier {
    @SuppressLint("BadHostnameVerifier")
    @Override
    public boolean verify(String hostname, SSLSession session) {
        return true;
    }
}
