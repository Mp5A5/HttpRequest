package com.mp5a5.www.library.utils;

import java.io.InputStream;
import java.io.Serializable;

/**
 * @author ：mp5a5 on 2019/1/4 17：19
 * @describe Https单双向认证配置
 * @email：wwb199055@126.com
 */
public class SslSocketConfigure implements Serializable {

    /**
     * One way certification or Two way certification.If one you must init 1,others stand for two.*****单向双向验证
     */
    private int verifyType;
    /**
     * client private keystore name*******客户端keystore名称 eg:client.bks
     */
    private String clientPriKey;
    /**
     * 受信任密钥库keystore名称 eg:truststore.bks
     */
    private String trustPubKey;

    /**
     * 客户端密码 eg:123456
     */
    private String clientBKSPassword;

    /**
     * 受信任密钥库密码 eg:123456
     */
    private String trustStoreBKSPassword;

    /**
     * 客户端密钥类型 eg:BKS
     */
    private String keystoreType;

    /**
     * 协议类型 eg:TLS
     */
    private String protocolType;

    /**
     * 证书类型 eg:X.509
     */
    private String certificateType;

    /**
     * 证书*****单向验证使用
     */
    private InputStream inputStream;

    private SslSocketConfigure(Builder builder) {
        this.verifyType = builder.verifyType;
        this.clientPriKey = builder.clientPriKey;
        this.trustPubKey = builder.trustPubKey;
        this.clientBKSPassword = builder.clientBKSPassword;
        this.trustStoreBKSPassword = builder.truststoreBKSPassword;
        this.keystoreType = builder.keystoreType;
        this.protocolType = builder.protocolType;
        this.certificateType = builder.certificateType;
        this.inputStream = builder.inputStream;
    }

    /**
     * One way certification or Two way certification.If one you must init 1,others stand for two.*****单向双向验证
     */
    public int getVerifyType() {
        return verifyType;
    }

    /**
     * client private keystore name*******客户端keystore名称 eg:client.bks
     */
    public String getClientPriKey() {
        return clientPriKey;
    }

    /**
     * 受信任密钥库keystore名称 eg:truststore.bks
     */
    public String getTrustPubKey() {
        return trustPubKey;
    }

    /**
     * 客户端密码 eg:123456
     */
    public String getClientBKSPassword() {
        return clientBKSPassword;
    }

    /**
     * 受信任密钥库密码 eg:123456
     */
    public String getTruststoreBKSPassword() {
        return trustStoreBKSPassword;
    }

    /**
     * 客户端密钥类型 eg:BKS
     */
    public String getKeystoreType() {
        return keystoreType;
    }

    /**
     * 协议类型 eg:TLS
     */
    public String getProtocolType() {
        return protocolType;
    }

    /**
     * 证书类型 eg:X.509
     */
    public String getCertificateType() {
        return certificateType;
    }

    /**
     * 证书*****单向验证使用
     */
    public InputStream getCertificateInputStream() {
        return inputStream;
    }


    public static final class Builder {
        private int verifyType;
        private String clientPriKey;
        private String trustPubKey;
        private String clientBKSPassword;
        private String truststoreBKSPassword;
        private String keystoreType;
        private String protocolType;
        private String certificateType;
        private InputStream inputStream;


        /**
         * One way certification or Two way certification.If one you must init 1,others stand for two.*****单向双向验证
         */
        public Builder setVerifyType(int verifyType) {
            this.verifyType = verifyType;
            return this;
        }

        /**
         * client private keystore name*******客户端keystore名称 eg:client.bks
         */
        public Builder setClientPriKey(String clientPriKey) {
            this.clientPriKey = clientPriKey;
            return this;
        }

        /**
         * 受信任密钥库keystore名称 eg:truststore.bks
         */
        public Builder setTrustPubKey(String trustPubKey) {
            this.trustPubKey = trustPubKey;
            return this;
        }

        /**
         * 客户端密码 eg:123456
         */
        public Builder setClientBKSPassword(String clientBKSPassword) {
            this.clientBKSPassword = clientBKSPassword;
            return this;
        }

        /**
         * 受信任密钥库密码 eg:123456
         */
        public Builder setTruststoreBKSPassword(String truststoreBKSPassword) {
            this.truststoreBKSPassword = truststoreBKSPassword;
            return this;
        }

        /**
         * 客户端密钥类型 eg:BKS
         */
        public Builder setKeystoreType(String keystoreType) {
            this.keystoreType = keystoreType;
            return this;
        }

        /**
         * 协议类型 eg:TLS
         */
        public Builder setProtocolType(String protocolType) {
            this.protocolType = protocolType;
            return this;
        }

        /**
         * 证书类型 eg:X.509
         */
        public Builder setCertificateType(String certificateType) {
            this.certificateType = certificateType;
            return this;
        }

        /**
         * 证书流*****单向验证使用
         */
        public Builder setInputStream(InputStream inputStream) {
            this.inputStream = inputStream;
            return this;
        }

        public SslSocketConfigure build() {
            return new SslSocketConfigure(this);
        }
    }


}
