package com.mp5a5.www.library.net.https;

import com.mp5a5.www.library.utils.ApiConfig;
import com.mp5a5.www.library.utils.AppContextUtils;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateFactory;

/**
 * @author ：mp5a5 on 2019/1/3 16：39
 * @describe
 * @email：wwb199055@126.com
 */
@SuppressWarnings("ALL")
public class SslSocketFactory {

    private final static String CLIENT_PRI_KEY = "client.bks";
    private final static String TRUSTSTORE_PUB_KEY = "truststore.bks";
    private final static String CLIENT_BKS_PASSWORD = "123456";
    private final static String TRUSTSTORE_BKS_PASSWORD = "123456";
    private final static String KEYSTORE_TYPE = "BKS";
    private final static String PROTOCOL_TYPE = "TLS";
    private final static String CERTIFICATE_FORMAT = "X.509";

    /**
     * HTTPS单向认证
     *
     * @return
     */
    public static SSLSocketFactory getSSLSocketFactory(InputStream... certificates) {
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance(ApiConfig.getSslSocketConfigure().getCertificateType());
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null);
            int index = 0;
            for (InputStream input : certificates) {
                String iAlias = Integer.toString(index++);
                keyStore.setCertificateEntry(iAlias, certificateFactory.generateCertificate(input));
                try {
                    if (null != input) {
                        input.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            SSLContext sslContext = SSLContext.getInstance(ApiConfig.getSslSocketConfigure().getProtocolType());
            TrustManagerFactory managerF = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            managerF.init(keyStore);
            sslContext.init(null, managerF.getTrustManagers(), new SecureRandom());
            return sslContext.getSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * HTTPS双向认证
     *
     * @return
     */
    public static SSLSocketFactory getSSLSocketFactory() {

        try {
            // 服务器端需要验证的客户端证书，客户端的keystore
            // 客户端信任的服务器端证书
            KeyStore keyStore = KeyStore.getInstance(ApiConfig.getSslSocketConfigure().getKeystoreType());
            //读取证书
            KeyStore trustStore = KeyStore.getInstance(ApiConfig.getSslSocketConfigure().getKeystoreType());
            InputStream keyInput = AppContextUtils.getContext().getAssets().open(ApiConfig.getSslSocketConfigure().getClientPriKey());
            //加载证书
            InputStream trustInput = AppContextUtils.getContext().getAssets().open(ApiConfig.getSslSocketConfigure().getTrustPubKey());
            keyStore.load(keyInput, ApiConfig.getSslSocketConfigure().getClientBKSPassword().toCharArray());
            trustStore.load(trustInput, ApiConfig.getSslSocketConfigure().getTruststoreBKSPassword().toCharArray());

            try {
                if (null != keyInput) {
                    keyInput.close();
                }
                if (null != keyInput) {
                    trustInput.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            SSLContext sslContext = SSLContext.getInstance(ApiConfig.getSslSocketConfigure().getProtocolType());
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(ApiConfig.getSslSocketConfigure().getCertificateType());
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(ApiConfig.getSslSocketConfigure().getCertificateType());
            trustManagerFactory.init(trustStore);
            keyManagerFactory.init(keyStore, ApiConfig.getSslSocketConfigure().getClientBKSPassword().toCharArray());
            sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), new SecureRandom());
            return sslContext.getSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}